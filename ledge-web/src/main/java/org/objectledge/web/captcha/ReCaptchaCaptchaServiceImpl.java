package org.objectledge.web.captcha;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.Logger;
import org.objectledge.authentication.UserManager;
import org.objectledge.i18n.I18n;
import org.objectledge.parameters.Parameters;
import org.objectledge.parameters.RequestParameters;
import org.objectledge.web.HttpContext;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ReCaptchaCaptchaServiceImpl
    implements CaptchaService
{        
    private static final String PARAMETER_CHALLENGE = "recaptcha_challenge_field";

    private static final String PARAMETER_RESPONSE = "recaptcha_response_field";
    
    private static final String CAPTCHA_CACHE = "captcha_cache";

    private static final String I18n_PREFIX = "captcha";
    
    private final Logger log;

    private final ReCaptchaImpl reCaptcha;

    private final Map<String, String> defaultOptions = new HashMap<String, String>();

    private final String errorMessage;
    
    private final String emailTitle;
    
    private final String recaptchaServer;
    
    private final boolean includeNoscript;
    
    private final String publicKeyWidget;
    
    private final String publicKeyEmail;
    
    private final long cacheTimeLimit;
    
    private final long cacheHitLimit;

    private final I18n i18n;
    
    private final UserManager userManager;

    private final Map<CaptchaCacheKey, CaptchaCacheValue> captchaCache;

    private final ReCaptchaEmailHides reCaptchaEmailHides;

    public ReCaptchaCaptchaServiceImpl(I18n i18n, Configuration config, Logger log, UserManager userManager)
        throws ConfigurationException
    {
        this.i18n = i18n;
        this.log = log;
        this.reCaptcha = new ReCaptchaImpl();
        publicKeyWidget = config.getChild("widget").getChild("publicKey").getValue();
        publicKeyEmail = config.getChild("email").getChild("publicKey").getValue();
        reCaptcha.setPublicKey(publicKeyWidget);
        reCaptcha.setPrivateKey(config.getChild("widget").getChild("privateKey").getValue());
        recaptchaServer = config.getChild("recaptchaServer", true).getValue(
            "//www.google.com/recaptcha");

        if(recaptchaServer != null)
        {
            reCaptcha.setRecaptchaServer(recaptchaServer);
        }
        includeNoscript = config.getChild("widget").getChild("includeNoScript", true).getValueAsBoolean(false);
        reCaptcha.setIncludeNoscript(includeNoscript);
        this.errorMessage = config.getChild("widget").getChild("errorMessage", true).getValue(null);
        Configuration options = config.getChild("widget").getChild("options", true);
        for(Configuration option : options.getChildren())
        {
            defaultOptions.put(option.getAttribute("name"), option.getValue().trim());
        }
        cacheTimeLimit = config.getChild("widget").getChild("cacheValidity").getChild("timeLimit")
            .getValueAsLong(60000);
        cacheHitLimit = config.getChild("widget").getChild("cacheValidity").getChild("hitLimit").getValueAsLong(2);
        this.userManager = userManager;
        Cache<CaptchaCacheKey, CaptchaCacheValue> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(cacheTimeLimit, TimeUnit.MILLISECONDS).build();
        captchaCache = cache.asMap();
        reCaptchaEmailHides = new ReCaptchaEmailHides(config.getChild("email")
            .getChild("privateKey").getValue(), publicKeyEmail, recaptchaServer);
        emailTitle = config.getChild("email").getChild("title", true).getValue();
    }

    @Override
    public String createCaptchaWidget(Locale locale, Map<String, String> options)
    {
        Properties properties = new Properties();
        for(String option : defaultOptions.keySet())
        {
            if(!options.containsKey(option))
            {
                properties.setProperty(option, defaultOptions.get(option));
            }
        }
        for(String option : options.keySet())
        {
            properties.setProperty(option, options.get(option));
        }

        properties.put("custom_translations", getTranslations(locale));
        
        return createRecaptchaHtml(errorMessage, properties);
    }

    @Override
    public String createrCaptchaEmailWidget(String email)
    {
        if(email == null)
        {
            return null;
        }
        String url = reCaptchaEmailHides.createEmailHideUrl(email);
        String[] emailParts = email.split("@");        
        if(emailParts.length == 2)
        {
            return "<a href='#' onclick=\"window.open('"
                + url
                + "', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300');return false;\" title='"
                + emailTitle + "'>" + emailParts[0].substring(0, (int)emailParts[0].length() < 7 ? (int)emailParts[0].length() / 2 : 3) + "...@" + emailParts[1] + "</a>";
        }
        else
        {
            return "<a href='#' onclick=\"window.open('"
                + url
                + "', '', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300');return false;\" title='"
                + emailTitle + "'>...</a>";
        }
    }

    @Override
    public boolean checkCaptcha(String remoteAddr, String challenge, String response)
    {
        ReCaptchaResponse result = reCaptcha.checkAnswer(remoteAddr, challenge, response);
       
        if(!result.isValid() && result.getErrorMessage().equals("incorrect-captcha-sol"))
        {
            log.error("recaptcha verification failed: " + result.getErrorMessage());
        }
        return result.isValid();
    }

    @Override
    public boolean checkCaptcha(HttpContext httpContext, RequestParameters parameters)
    {
        String remoteAddr = httpContext.getRequest().getRemoteAddr();
        String challenge = parameters.get(PARAMETER_CHALLENGE, "");
        String response = parameters.get(PARAMETER_RESPONSE, "");
        
        CaptchaCacheKey captchaCacheKey = new CaptchaCacheKey(remoteAddr, challenge, response);
        CaptchaCacheValue captchaCacheValue = captchaCache.get(captchaCacheKey);

        if(captchaCacheValue != null)
        {
            if(System.currentTimeMillis() > captchaCacheValue.getTimestamp() + this.cacheTimeLimit
                || captchaCacheValue.getCounter() > this.cacheHitLimit)
            {
                return false;
            }
        }
        else
        {
            captchaCacheValue = new CaptchaCacheValue(checkCaptcha(remoteAddr, challenge, response));
            captchaCache.put(captchaCacheKey, captchaCacheValue);
        }

        return captchaCacheValue.getValue();
    }
    
    /**
     * Verify if CAPTCHA required by the principal.
     * 
     * @param parameters component or application configuration.
     *        principal subject's principal.
     * @return true if CAPTCHA required otherwise false.
     */
    public boolean isCaptchaRequired(Parameters config, Principal principal)
        throws Exception
    {
        if(config != null)
        {
            String captcha_verification = config.get("captcha_verification", CAPTCHA_DISABLED);
            Principal anonymous = userManager.getAnonymousAccount();

            if(CAPTCHA_FOR_EVERYONE.equals(captcha_verification))
            {
                return true;
            }
            else if(CAPTCHA_FOR_ANONYMOUS.equals(captcha_verification)
                && anonymous.equals(principal))
            {
                return true;
            }
        }
        return false; 
    }
    
    /**
     * Produces javascript array with the RecaptchaOptions encoded.
     * 
     * @param properties
     * @return
     */
    private String fetchJSOptions(Properties properties) {

        if (properties == null || properties.size() == 0) {
            return "";
        }

        StringBuffer jsOptions = new StringBuffer();
        jsOptions.append("<script type=\"text/javascript\">\r\n" + 
            "var RecaptchaOptions = ");
            
        appendDictionary(jsOptions, properties);
        
        jsOptions.append(";\r\n</script>\r\n");

        return jsOptions.toString();
    } 
    
    /**
     * Appends javascript dictionary representation of a Properties object to given StringBuffer 
     * 
     * @param jsOptions the target StringBuffer object.
     * @param properties a Properties object.
     */
    private void appendDictionary(StringBuffer jsOptions, Properties properties)
    {
        jsOptions.append("\r\n{");
        for (Enumeration e = properties.keys(); e.hasMoreElements(); ) {
            Object property = e.nextElement();
            jsOptions.append((String)property).append(": ");
            
            Object value = properties.get(property);
            if(value instanceof Properties) {
                appendDictionary(jsOptions, (Properties)value);
            } else {
                jsOptions.append("'").append(value).append("'");
            }
            
            if (e.hasMoreElements()) {
                jsOptions.append(",\r\n");
            }
        }
        jsOptions.append("}");
    }
    
    private String createRecaptchaHtml(String errorMessage, Properties options) {

        String errorPart = (errorMessage == null ? "" : "&amp;error=" + URLEncoder.encode(errorMessage));

        String message = fetchJSOptions(options);

        message += "<script type=\"text/javascript\" src=\"" + recaptchaServer + "/api/challenge?k=" + publicKeyWidget + errorPart + "\"></script>\r\n";

        if (includeNoscript) {
            String noscript = "<noscript>\r\n" + 
                    "   <iframe src=\""+recaptchaServer+"/api/noscript?k="+publicKeyWidget + errorPart + "\" height=\"300\" width=\"500\" frameborder=\"0\"></iframe><br>\r\n" + 
                    "   <textarea name=\"recaptcha_challenge_field\" rows=\"3\" cols=\"40\"></textarea>\r\n" + 
                    "   <input type=\"hidden\" name=\"recaptcha_response_field\" value=\"manual_challenge\">\r\n" + 
                    "</noscript>";
            message += noscript;
        }
        
        return message;
    }
    
    private Properties getTranslations(Locale locale)
    {
        Locale actualLocale = locale != null ? locale : i18n.getDefaultLocale();
        Properties translations = new Properties();
        for(String key : i18n.getKeys(actualLocale))
        {
            if(key.startsWith(I18n_PREFIX))
            {
                translations.setProperty(key.substring(I18n_PREFIX.length() + 1), i18n.get(
                    actualLocale, key));
            }
        }
        return translations;
    }

    private class ReCaptchaEmailHides
    {
        private String privateKey;

        private String publicKey;

        private String reCaptchaServer;

        private final Base64 base64 = new Base64();

        public ReCaptchaEmailHides(String privateKey, String publicKey, String reCaptchaServer)
        {

            this.privateKey = privateKey;
            this.publicKey = publicKey;
            this.reCaptchaServer = reCaptchaServer;
        }

        public String createEmailHideUrl(String email)
        {
            return reCaptchaServer + "/mailhide/d?k=" + publicKey + "&c="
                + b64(encryptAES(email.getBytes()));
        }

        private String b64(byte[] b)
        {
            String b64Str = new String(base64.encode(b));
            return b64Str.replace('+', '-').replace('/', '_');
        }

        private byte[] encryptAES(byte[] input)
        {
            try
            {
                SecretKey secret = new SecretKeySpec(hexStringToByteArray(privateKey),0, 16, "AES");
                // PKCS5Padding is exactly what google describes on http://code.google.com/apis/recaptcha/docs/mailhideapi.html
                // No need to implement that separately
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
                return cipher.doFinal(input);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        private byte[] hexStringToByteArray(String s)
        {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for(int i = 0; i < len; i += 2)
            {
                data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(
                    s.charAt(i + 1), 16));
            }
            return data;
        }
    }
}

package org.objectledge.web.captcha;

import java.util.Map;

import org.objectledge.parameters.RequestParameters;
import org.objectledge.web.HttpContext;

/**
 * A service that provides CAPTCHA functionality for web applications.
 * 
 * @author rafal 
 */
public interface CaptchaService
{
    /**
     * Create CAPTCHA widget with default options
     * 
     * @return HTML markup of CAPTCHA widget.
     */
    public String createCaptchaWidget();

    /**
     * Create CAPTCHA widget with specified options.
     * 
     * @param options implementation specific options.
     * @return HTML markup of CAPTCHA widget.
     */
    public String createCaptchaWidget(Map<String, String> properties);

    /**
     * Verify CAPTCHA solved by the user.
     * 
     * @param remoteAddr IP address of the user
     * @param challenge challenge generated by CAPTHA widget.
     * @param response response entered by the user.
     * @return true if the solution is correct.
     */
    public boolean checkCaptcha(String remoteAddr, String challenge, String response);

    /**
     * Verify CAPTCHA solved by the user.
     * 
     * @param parameters request parameters that contain CAPTCHA challenge and response. Parameter
     *        names are implementation specific.
     * @return
     */
    public boolean checkCaptcha(HttpContext httpContext, RequestParameters parameters);
}
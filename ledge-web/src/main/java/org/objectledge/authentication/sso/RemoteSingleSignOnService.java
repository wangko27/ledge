package org.objectledge.authentication.sso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Principal;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.Logger;
import org.objectledge.authentication.AuthenticationException;
import org.objectledge.authentication.UserManager;

public class RemoteSingleSignOnService
    implements SingleSignOnService
{
    private final UserManager userManager;

    private Logger log;

    private XmlRpcSingleSignOnService remote;

    public RemoteSingleSignOnService(UserManager userManager, Configuration config, Logger log)
        throws ConfigurationException
    {
        this.userManager = userManager;
        this.log = log;

        XmlRpcClientConfigImpl xmlRpcConfig = new XmlRpcClientConfigImpl();
        Configuration remoteUrlConfig = config.getChild("remoteUrl");
        URL remoteUrl;
        try
        {
            remoteUrl = new URL(remoteUrlConfig.getValue());
            xmlRpcConfig.setServerURL(remoteUrl);
        }
        catch(MalformedURLException e)
        {
            throw new ConfigurationException("invalid url " + remoteUrlConfig.getValue(),
                remoteUrlConfig.getPath(), remoteUrlConfig.getLocation(), e);
        }
        xmlRpcConfig.setEnabledForExtensions(true);
        xmlRpcConfig.setContentLengthOptional(true);
        String secret = config.getChild("secret").getValue();
        xmlRpcConfig.setBasicUserName("sso");
        xmlRpcConfig.setBasicPassword(secret);
        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(xmlRpcConfig);
        XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory(
            xmlRpcClient);
        Configuration sslConfig = config.getChild("sslKeyStore", false);
        if(remoteUrl.getProtocol().equals("https") && sslConfig != null)
        {
            Configuration urlConfig = sslConfig.getChild("url");
            String pass = sslConfig.getChild("password").getValue();
            try
            {
                URL url = new URL(urlConfig.getValue());
                HttpClient httpClient = new HttpClient();
                ProtocolSocketFactory sslSocketFactory = new AuthSSLProtocolSocketFactory(null,
                    null, url, pass);
                Protocol protocol = new Protocol("https", sslSocketFactory, remoteUrl.getPort());
                httpClient.getHostConfiguration().setHost(remoteUrl.getHost(), remoteUrl.getPort(),
                    protocol);
            }
            catch(MalformedURLException e)
            {
                throw new ConfigurationException("invalid url " + urlConfig.getValue(),
                    urlConfig.getPath(), urlConfig.getLocation(), e);
            }
            catch(IOException e)
            {
                throw new ConfigurationException("keystore access failed", sslConfig.getPath(),
                    sslConfig.getLocation(), e);
            }
            catch(GeneralSecurityException e)
            {
                throw new ConfigurationException("SSL setup failed", sslConfig.getPath(),
                    sslConfig.getLocation(), e);
            }
        }
        xmlRpcClient.setTransportFactory(transportFactory);
        ClientFactory clientFactory = new ClientFactory(xmlRpcClient);
        remote = (XmlRpcSingleSignOnService)clientFactory
            .newInstance(XmlRpcSingleSignOnService.class);
    }

    @Override
    public String generateTicket(Principal principal, String domain, String client)
    {
        throw new UnsupportedOperationException(
            "this functionality is available on realm controler only");
    }

    @Override
    public boolean validateApiRequest(String secret, String remoteAddr, boolean secure)
    {
        throw new UnsupportedOperationException(
            "this functionality is available on realm controler only");
    }

    @Override
    public Principal validateTicket(String ticket, String domain, String client)
    {
        try
        {
            String principal = remote.validateTicket(ticket, domain, client);
            if(principal != null)
            {
                try
                {
                    return userManager.getUserByName(principal);
                }
                catch(AuthenticationException e)
                {
                    log.error("DECLINED sso ticket - uknown user " + principal);
                    return null;
                }
            }
            return null;
        }
        catch(XmlRpcException e)
        {
            log.error("XmlRpc call failed", e);
            return null;
        }
    }

    @Override
    public void logIn(Principal principal, String domain)
    {
        try
        {
            remote.logIn(principal.getName(), domain);
        }
        catch(XmlRpcException e)
        {
            log.error("XmlRpc call failed", e);
        }
    }

    @Override
    public void logOut(Principal principal, String domain)
    {
        try
        {
            remote.logOut(principal.getName(), domain);
        }
        catch(XmlRpcException e)
        {
            log.error("XmlRpc call failed", e);
        }
    }

    @Override
    public LoginStatus checkStatus(Principal principal, String domain)
    {
        String loginStatus;
        try
        {
            loginStatus = remote.checkStatus(principal.getName(), domain);
            try
            {
                return LoginStatus.valueOf(loginStatus);
            }
            catch(IllegalArgumentException e)
            {
                log.error("Illegal LoginStatus " + loginStatus);
                return LoginStatus.UNKNOWN;
            }
        }
        catch(XmlRpcException e)
        {
            log.error("XmlRpc call failed", e);
            return LoginStatus.UNKNOWN;
        }
    }

    @Override
    public String ssoBaseUrl(String domain)
    {
        try
        {
            return remote.ssoBaseUrl(domain);
        }
        catch(XmlRpcException e)
        {
            log.error("XmlRpc call failed", e);
            return null;
        }
    }
}

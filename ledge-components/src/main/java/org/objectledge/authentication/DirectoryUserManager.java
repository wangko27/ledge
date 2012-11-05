// 
//Copyright (c) 2003, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
//All rights reserved. 
//   
//Redistribution and use in source and binary forms, with or without modification,  
//are permitted provided that the following conditions are met: 
//   
//* Redistributions of source code must retain the above copyright notice,  
//this list of conditions and the following disclaimer. 
//* Redistributions in binary form must reproduce the above copyright notice,  
//this list of conditions and the following disclaimer in the documentation  
//and/or other materials provided with the distribution. 
//* Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
//nor the names of its contributors may be used to endorse or promote products  
//derived from this software without specific prior written permission. 
// 
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  
//AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  
//WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
//IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,  
//INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,  
//BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
//OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,  
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)  
//ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE  
//POSSIBILITY OF SUCH DAMAGE. 
//

package org.objectledge.authentication;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.directory.shared.ldap.util.GeneralizedTime;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.Logger;
import org.objectledge.naming.ContextFactory;
import org.objectledge.naming.ContextHelper;
import org.objectledge.parameters.DefaultParameters;
import org.objectledge.parameters.Parameters;
import org.objectledge.parameters.UndefinedParameterException;
import org.objectledge.parameters.directory.DirectoryParameters;

/**
 * The user manager implementation based on ldap.
 * 
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 */
public class DirectoryUserManager
    extends UserManager
{
    /** Default value for login attribute name. */
    public static final String LOGIN_ATTRIBUTE_DEFAULT = "uid";

    /** Default value for email attribute name. */
    public static final String MAIL_ATTRIBUTE_DEFAULT = "mail";

    /** Default password attribute key name. */
    public static final String PASSWORD_ATTRIBUTE_DEFAULT = "userPassword";

    /** By default logon tracking is turned off */
    public static final boolean LOGON_TRACKING_ENABLED = false;
    
    /** Default logon count attribute key name. */
    public static final String LOGON_COUNT_ATTRIBUTE_DEFAULT = "logonCount";

    /** Default logon timestamp attribute key name. */
    public static final String LAST_LOGON_TIMESTAMP_ATTRIBUTE_DEFAULT = "lastLogonTimestamp";

    /**
     * Default value for login person object class (uidObject, simpleSecurityObject). Minimalistic
     * set of classes was chosen: uidObject (1.3.6.1.1.3.1), RFC2377 and simpleSecurityObject
     * (0.9.2342.19200300.100.4.19), RFC1274.
     */
    public static final String[] OBJECT_CLASS_DEFAULT = { "uidObject", "simpleSecurityObject" };

    /** the logger. */
    protected Logger logger;

    /** the login attribute key. */
    protected String loginAttribute;

    /** the email attribute key. */
    protected String mailAttribute;

    /** the password attribute key. */
    protected String passwordAttribute;
    
    /** logon tracking enabling flag */
    protected boolean isLogonTrackingEnabled;

    /** the logon count attribute key. */
    protected String logonCountAttribute;

    /** the last logon timestamp attribute key. */
    protected String lastLogonTimestampAttribute;

    /** the anonymous name. */
    protected String anonymousName;

    /** the superuser name. */
    protected String superuserName;

    /** The objectClass of newly created containers. */
    protected String[] objectClass;

    /** the directory context. */
    protected ContextHelper directory;

    /** The principal by name cache */
    private Map<String, String> loginByName;

    /** The name by login cache */
    private Map<String, String> nameByLogin;

    /** The default search controls */
    private SearchControls defaultSearchControls;

    /** The list of user management participants */
    private UserManagementParticipant[] participants;

    /**
     * Creates an instance of the user manager.
     * 
     * @param config the configuration.
     * @param logger the logger.
     * @param namingPolicy the namig policy to be used.
     * @param loginVerifier the login verifier.
     * @param passwordGenerator the password generator.
     * @param passwordDigester the password digester.
     * @param factory the factory to get context from.
     * @throws NamingException if the context could not be accessed.
     */
    public DirectoryUserManager(Configuration config, Logger logger, NamingPolicy namingPolicy,
        LoginVerifier loginVerifier, PasswordGenerator passwordGenerator,
        PasswordDigester passwordDigester, ContextFactory factory,
        UserManagementParticipant[] participants)
        throws NamingException
    {
        super(namingPolicy, loginVerifier, passwordGenerator, passwordDigester);
        this.logger = logger;
        loginByName = new HashMap<String, String>();
        nameByLogin = new HashMap<String, String>();
        defaultSearchControls = new SearchControls();
        loginAttribute = config.getChild("loginAttribute").getValue(LOGIN_ATTRIBUTE_DEFAULT);
        mailAttribute = config.getChild("mailAttribute").getValue(MAIL_ATTRIBUTE_DEFAULT);
        passwordAttribute = config.getChild("passwordAttribute").getValue(
            PASSWORD_ATTRIBUTE_DEFAULT);
        isLogonTrackingEnabled = config.getChild("isLogonTrackingEnabled").getValueAsBoolean(LOGON_TRACKING_ENABLED);
        logonCountAttribute = config.getChild("logonCountAttribute").getValue(
            LOGON_COUNT_ATTRIBUTE_DEFAULT);
        lastLogonTimestampAttribute = config.getChild("lastLogonTimestampAttribute").getValue(
            LAST_LOGON_TIMESTAMP_ATTRIBUTE_DEFAULT);
        anonymousName = config.getChild("anonymousName").getValue(null);
        superuserName = config.getChild("superuserName").getValue(null);
        String contextId = config.getChild("contextId").getValue("people");
        directory = new ContextHelper(factory, contextId, logger);

        String objectClassList = config.getChild("objectClass").getValue("");
        StringTokenizer st = new StringTokenizer(objectClassList, ",");
        objectClass = new String[st.countTokens()];
        for(int i = 0; st.hasMoreTokens(); i++)
        {
            objectClass[i] = st.nextToken().trim();
        }
        if(objectClass.length == 0)
        {
            objectClass = OBJECT_CLASS_DEFAULT;
        }
        this.participants = participants;
    }

    @Override
    public void changeUserAttribiutes(Principal account, Attributes attributes)
        throws AuthenticationException
    {
        DirContext ctx = null;
        try
        {
            ctx = directory.lookupDirContext(account.getName());
            if(ctx == null)
            {
                throw new UserUnknownException("user " + account.getName() + " does not exist");
            }

            ctx.modifyAttributes("", DirContext.REPLACE_ATTRIBUTE, attributes);
            logger.info("User " + account.getName() + "'s attribiutes changed");
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("Attribiutes modification failed", e);
        }
        finally
        {
            closeContext(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void changeUserPassword(Principal account, String password)
        throws AuthenticationException
    {
        DirContext ctx = null;
        try
        {
            ctx = directory.lookupDirContext(account.getName());
            if(ctx == null)
            {
                throw new UserUnknownException("user " + account.getName() + " does not exist");
            }
            Attributes attrs = new BasicAttributes(true);
            putPasswordAttribute(attrs, password, false);
            ctx.modifyAttributes("", DirContext.REPLACE_ATTRIBUTE, attrs);
            logger.info("User " + account.getName() + "'s password changed");
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("password modification failed", e);
        }
        finally
        {
            closeContext(ctx);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUserPassword(Principal account, String password)
        throws AuthenticationException
    {
        String storedPassword = getUserPassword(account);
        try
        {
            return passwordDigester.validateDigest(password, storedPassword);
        }
        catch(Exception e)
        {
            throw new AuthenticationException("password validation failed", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Principal createAccount(String login, String password)
        throws AuthenticationException
    {
        return this.createAccount(login, password, false, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Principal createAccount(String login, String password, Boolean blockPassword,
        Attributes attributes)
        throws AuthenticationException
    {
        Parameters params = new DefaultParameters();
        params.set(loginAttribute, login);
        String dn = createDN(params);
        if(!checkLogin(login))
        {
            throw new AuthenticationException("login '" + login + "' reserved");
        }
        DirContext ctx = null;
        try
        {
            ctx = directory.getBaseDirContext();
            Attributes attrs = new BasicAttributes(true);
            BasicAttribute oc = new BasicAttribute("objectClass");
            for(int i = 0; i < objectClass.length; i++)
            {
                oc.add(objectClass[i]);
            }
            attrs.put(oc);
            attrs.put(new BasicAttribute(loginAttribute, login));
            putPasswordAttribute(attrs, password, blockPassword);
            putAll(attrs, attributes);
            ctx.createSubcontext(directory.getRelativeName(dn), attrs);
            nameByLogin.put(login, dn);
            loginByName.put(dn, login);
            logger.info("User " + dn + " created");
        }
        catch(NameAlreadyBoundException e)
        {
            throw new UserAlreadyExistsException("user '" + dn + "' already exists");
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("account creation failed", e);
        }
        finally
        {
            closeContext(ctx);
        }
        Principal principal = new DefaultPrincipal(dn);
        for(UserManagementParticipant p : participants)
        {
            p.createAccount(principal);
        }
        return principal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean emailExists(String email)
    {
        boolean emailExists = false;
        try
        {
            List<String> list = lookupDNs(mailAttribute, email);
            if(list.size() > 0)
            {
                emailExists = true;
            }
        }
        catch(NamingException e)
        {
            // defaults to false
        }
        return emailExists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableUserPassword(Principal account)
        throws AuthenticationException
    {
        String password = getUserPassword(account);
        password = password.substring(1);
        changeUserPassword(account, password);
    }

    /**
     * {@inheritDoc}
     */
    public Principal getAnonymousAccount()
        throws AuthenticationException
    {
        if(anonymousName == null)
        {
            return null;
        }
        return new DefaultPrincipal(anonymousName);
    }

    /**
     * {@inheritDoc}
     */
    public DirContext getPersonalData(Principal account) throws AuthenticationException
    {
        try
        {
            DirContext ctx = directory.lookupDirContext(account.getName());
            return ctx;
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("Failed to lookup user personal data" +
                    " for principal: "+account.getName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Principal getSuperuserAccount()
        throws AuthenticationException
    {
        if(superuserName == null)
        {
            return null;
        }
        return new DefaultPrincipal(superuserName);
    }

    @Override
    public String getUserAttribute(Principal account, String attribute)
        throws AuthenticationException
    {
        {
            String storedAttribute = "";
            DirContext ctx = null;
            try
            {
                ctx = directory.lookupDirContext(account.getName());
                if(ctx == null)
                {
                    throw new UserUnknownException("user " + account.getName() + " does not exist");
                }
                String[] attrIds = { attribute };
                Attribute attr = ctx.getAttributes("", attrIds).get(attribute);
                Object obj = attr.get();
                if(obj instanceof String)
                {
                    storedAttribute = (String)obj;
                }
                else
                {
                    storedAttribute = new String((byte[])obj);
                }
            }
            catch(Exception e)
            {
                throw new AuthenticationException("attribute retreiveal failed", e);
            }
            finally
            {
                closeContext(ctx);
            }
            return storedAttribute;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Principal getUserByLogin(String login)
        throws AuthenticationException
    {
        try
        {
            List<String> list = lookupDNs(loginAttribute, login);
            if(list.size() == 0)
            {
                throw new UserUnknownException("failed to lookup user by login");
            }
            if(list.size() > 1)
            {
                StringBuilder message = new StringBuilder();
                message.append("ambigous login, following dn's shares the same login '");
                message.append(login);
                message.append("':\n");
                for(int i = 0; i < list.size(); i++)
                {
                    message.append(list.get(i));
                    message.append("\n");
                }
                throw new AuthenticationException(message.toString());
            }
            return new DefaultPrincipal(list.get(0));
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("Failed to loolup user by login");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Principal getUserByMail(String mail)
        throws AuthenticationException
    {
        try
        {
            List<String> list = lookupDNs(mailAttribute, mail);
            if(list.size() == 0)
            {
                throw new UserUnknownException("Failed to lookup user by mail");
            }
            else if(list.size() > 1)
            {
                // maybe add some new kind of exception?
                throw new AuthenticationException("Failed to look up user. Mail is ambiguous.");
            }
            return new DefaultPrincipal(list.get(0));
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("Failed to loolup user by login");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Principal getUserByName(String dn)
        throws AuthenticationException
    {
        // load the caches
        getLoginName(dn);
        return new DefaultPrincipal(dn);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Principal> lookupAccounts(String query)
        throws NamingException
    {
        return lookupAccounts(query, defaultSearchControls);
    }

    public Collection<Principal> lookupAccounts(String query, SearchControls searchControlls)
        throws NamingException
    {
        List<String> list = lookupDNs(query, searchControlls);
        Principal[] principals = new Principal[list.size()];
        int i = 0;
        for(String dn : list)
        {
            principals[i++] = new DefaultPrincipal(dn);
        }
        return Arrays.asList(principals);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Principal> lookupAccounts(String attribute, String value)
        throws NamingException
    {
        List<String> list = lookupDNs(attribute, value);
        Principal[] principals = new Principal[list.size()];
        int i = 0;
        for(String dn : list)
        {
            principals[i++] = new DefaultPrincipal(dn);
        }
        return Arrays.asList(principals);
    }

    /**
     * {@inheritDoc}
     */
    public void removeAccount(Principal account)
        throws AuthenticationException
    {
        String login = getLoginName(account.getName());
        DirContext ctx = null;
        try
        {
            ctx = directory.getBaseDirContext();
            ctx.destroySubcontext(directory.getRelativeName(account.getName()));
            nameByLogin.remove(login);
            loginByName.remove(account.getName());
            logger.info("User " + account.getName() + " deleted");
        }
        catch(NameNotFoundException e)
        {
            throw new UserUnknownException("user " + account.getName() + " does not exist");
        }
        catch(NamingException e)
        {
            throw new AuthenticationException("account removal failed", e);
        }
        finally
        {
            closeContext(ctx);
        }
        for(UserManagementParticipant p : participants)
        {
            if(p.supportsRemoval())
            {
                p.removeAccount(account);
            }
        }
    }

    // private helper methods.

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrackingInformation(Principal account)
        throws AuthenticationException, NamingException
    {
        if(isLogonTrackingEnabled)
        {
            DirContext dirContext = getPersonalData(account);
            DirectoryParameters params = new DirectoryParameters(dirContext);
            bumpUpLogonCounter(params);
            refreshTimestamp(params);
            dirContext.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean userExists(String dn)
    {
        try
        {
            getLoginName(dn);
            return true;
        }
        catch(AuthenticationException e)
        {
            return false;
        }
    }

    /**
     * Bumps up logon counter
     * 
     * @param params
     */
    private void bumpUpLogonCounter(DirectoryParameters params)
    {
        String logonCount = "0";
        boolean existed = true;
        try
        {
            logonCount = params.get(logonCountAttribute);
        }
        catch(UndefinedParameterException e)
        {
            // parameter was not defined so it remains as 0
            existed = false;
        }
        int bumpedCounter = Integer.parseInt(logonCount);
        bumpedCounter = bumpedCounter + 1;
        if(existed)
        {
            params.set(logonCountAttribute, Integer.valueOf(bumpedCounter).toString());
        }
        else
        {
            params.add(logonCountAttribute, Integer.valueOf(bumpedCounter).toString());
        }
    }

    /**
     * Close directory context silently.
     * 
     * @param ctx the context.
     */
    private void closeContext(Context ctx)
    {
        try
        {
            if(ctx != null)
            {
                ctx.close();
            }
        }
        catch(Exception e)
        {
            logger.error("closing context failed", e);
        }
    }

    /**
     * Map full user name to login name.
     * 
     * @param name full user name.
     * @return the login name, or <code>null</code> if not found.
     */
    private synchronized String getLoginName(String name)
        throws AuthenticationException
    {
        String login = loginByName.get(name);
        if(login == null)
        {
            DirContext user = null;
            try
            {
                user = directory.lookupDirContext(name);
                if(user != null)
                {
                    String[] attrIds = { loginAttribute };
                    Attribute attr = user.getAttributes("", attrIds).get(loginAttribute);
                    Object obj = attr.get();
                    if(obj instanceof String)
                    {
                        login = (String)obj;
                    }
                    else
                    {
                        login = new String((byte[])obj);
                    }
                    nameByLogin.put(login, name);
                    loginByName.put(name, login);
                }
            }
            catch(NamingException e)
            {
                throw new UserUnknownException("login name lookup failed", e);
            }
            finally
            {
                closeContext(user);
            }
        }
        return login;
    }

    /**
     * Returns user's password.
     * 
     * @param account the account
     * @return user's password
     * @throws AuthenticationException
     */
    private String getUserPassword(Principal account)
        throws AuthenticationException
    {
        String storedPassword = null;
        DirContext ctx = null;
        try
        {
            ctx = directory.lookupDirContext(account.getName());
            if(ctx == null)
            {
                throw new UserUnknownException("user " + account.getName() + " does not exist");
            }
            String[] attrIds = { passwordAttribute };
            Attribute attr = ctx.getAttributes("", attrIds).get(passwordAttribute);
            Object obj = attr.get();
            if(obj instanceof String)
            {
                storedPassword = (String)obj;
            }
            else
            {
                storedPassword = new String((byte[])obj);
            }
        }
        catch(Exception e)
        {
            throw new AuthenticationException("password retreiveal failed", e);
        }
        finally
        {
            closeContext(ctx);
        }
        return storedPassword;
    }

    /**
     * Find all dn of the context that match the attribute query given custom search controls.
     * 
     * @param query attribute query
     * @param searchControls the search controls to use for query
     * @return the list of the name of matched context.
     * @throws NamingException if lookup fails.
     */
    private List<String> lookupDNs(String query, SearchControls searchControls)
        throws NamingException
    {
        DirContext ctx = null;
        try
        {
            ctx = directory.getBaseDirContext();
            NamingEnumeration<SearchResult> answer = ctx.search("", query, searchControls);
            List<String> results = new ArrayList<String>();
            while(answer.hasMore())
            {
                SearchResult result = answer.next();
                results.add(result.getNameInNamespace());
            }
            return results;
        }
        finally
        {
            closeContext(ctx);
        }
    }

    /**
<<<<<<< HEAD
     * Find all dn of the context that match the attribute query.
     * 
     * @param attribute the attribute name.
     * @param value the attribute value.
     * @return the list of the name of matched context.
     * @throws NamingException if lookup fails.
     */
    private List<String> lookupDNs(String attribute, String value)
        throws NamingException
    {
        DirContext ctx = null;
        try
        {
            ctx = directory.getBaseDirContext();
            Attributes matchAttrs = new BasicAttributes(false);
            matchAttrs.put(new BasicAttribute(attribute, value));
            NamingEnumeration<SearchResult> answer = ctx.search("", matchAttrs, null);
            List<String> results = new ArrayList<String>();
            while(answer.hasMore())
            {
                SearchResult result = answer.next();
                results.add(result.getNameInNamespace());
            }
            return results;
        }
        finally
        {
            closeContext(ctx);
        }
    }

    /**
    * {@inheritDoc}
    */
   private List<String> lookupDNs(String query)
       throws NamingException
   {
       return lookupDNs(query, defaultSearchControls);
   }
    /**
     * Adds additional attributes to attrs. Changes state of attrs.
     * 
     * @param attrs
     * @param additionalAttributes the additional attributes to add to attrs
     * @throws NamingException
     */
    private void putAll(Attributes attrs, Attributes additionalAttributes)
        throws NamingException
    {
        if(additionalAttributes != null)
        {
            NamingEnumeration<String> ids = additionalAttributes.getIDs();
            while(ids.hasMore())
            {
                String attrId = ids.nextElement();
                attrs.put(additionalAttributes.get(attrId));
            }
        }
    }

    private void putPasswordAttribute(Attributes attrs, String password, Boolean blockPassword)
    {
        String hash = passwordDigester.generateDigest(password);
        if(blockPassword)
        {
            hash = "!" + hash;
        }
        attrs.put(new BasicAttribute(passwordAttribute, hash));
    }

    /**
     * Sets last logon timestamp to now.
     * 
     * @param params
     */
    private void refreshTimestamp(DirectoryParameters params)
    {
        GeneralizedTime timestamp = new GeneralizedTime(new GregorianCalendar());
        boolean existed = true;
        try
        {
            params.get(lastLogonTimestampAttribute);
        }
        catch(UndefinedParameterException e)
        {
            existed = false;
        }
        if(existed)
        {
            params.set(lastLogonTimestampAttribute, timestamp.toGeneralizedTime());
        }
        else
        {
            params.add(lastLogonTimestampAttribute, timestamp.toGeneralizedTime());
        }
    }

}

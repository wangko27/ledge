package org.objectledge.mail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.Logger;
import org.objectledge.filesystem.FileSystem;
import org.objectledge.mail.impl.FileSystemDataSource;
import org.objectledge.templating.Templating;

/**
 * Mail system component.
 *
 * @author <a href="mailto:rkrzewsk@caltha.pl">Rafal Krzewski</a>
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: MailSystem.java,v 1.2 2004-01-14 10:42:02 fil Exp $
 */
public class MailSystem
{
    // constants /////////////////////////////////////////////////////////////
    
    /** The configuration option for mime file (mimeFile). */
    public static final String MIME_FILE_KEY = "mimeFile";

    /** The configuration option for the Big Brother account (bigBroter). */
    public static final String BIG_BROTHER_KEY = "bigBrother";

    /** The default session name (default). */
    public static final String DEFAULT_SESSION = "default";
    
	/** The regex email address patter. */
	public static final String EMAIL_ADDRESS_PATTERN = "email_address_pattern";
        
    /** logger */
    private Logger logger;

    /** thread pool */
    //private ThreadPool threadPool;

    /** file service */
    private FileSystem fileSystem;
    
    /** templating */
    private Templating templating;
    
    /** map with mime types */
    private MimetypesFileTypeMap mimeMap;
    
    /** mail queue */
    private LinkedList mailQueue = new LinkedList();
    
    /** server list - not implemented */
    private Map sessionsMap = new HashMap();

    /** default provider */
    private String defaultSession;

    /** system mail logger */
    private String debugAddress;

	/** email address validator pattern */
	private Pattern emailAddressPattern; 
	
	/**
     * Initializes the component.
     * 
     * @param config the configuration.
     * @param logger the logger.
     * @param fileSystem the file system.
     * @param templating the templating.
     */
    public MailSystem(Configuration config, Logger logger,
    				   FileSystem fileSystem, Templating templating)
    {
		this.logger = logger;
		this.fileSystem = fileSystem;
		this.templating = templating;
        
        //TODO read the configuration...
    }

    /**
     * Returns the default mail session.
     *
     * @return default mail session.
     */
    public Session getSession() 
    {
        return getSession(defaultSession);
    }
    
    /**
     * Returns a mail session declared in the service configuration.
     *
     * @param name the name of the server.
     * @return the mail session.
     */
    public Session getSession(String name) 
    {
        return (Session)sessionsMap.get(name);
    }

    /**
     * Returns a <code>DataSource</code> backed by the <code>FileSystem</code>.
     *
     * @param name the pathname of the file.
     * @return the data source.
     */
    public DataSource getDataSource(String name)
    {
    	String contentType = getContentType(name);
        return new FileSystemDataSource(fileSystem, name, contentType);
    }

    /**
     * Create a new message based on default session.
     *
     * @return the mail message wrapper.
     */
    public LedgeMessage newMessage()
    {
        return newMessage(defaultSession);
    }
    
    /**
     * Create a new message based on session defined in the configuration file.
     *
     * @param sessionName the name of the server.
     * @return the mail message wrapper.
     */
    public LedgeMessage newMessage(String sessionName)
    {
        return new LedgeMessage(this, logger, templating, getSession(sessionName));
    }
    
    /**
     * Send the message.
     *
     * <p>If the <code>wait</code> parameter is false, the sending process will
     * proceed asynchronosly, and the method will return immediately,
     * otherwise the method will return only after the sending process is
     * complete.</p> 
     *
     * @param message the ledge mail message wrapper.
     * @param wait <code>true</code> to wait for operation completion.
     * @throws MessagingException thrown if message cannot be send.
     */
    public void send(LedgeMessage message, boolean wait) 
        throws MessagingException
    {
        if (wait)
        {
            Message msg = message.getMessage();
            if (debugAddress != null && (!debugAddress.equals("")))
            {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(debugAddress));
            }
            Transport.send(msg);
        }
        
        else 
        {
            synchronized(mailQueue)
            {
                mailQueue.addLast(message);
                mailQueue.notify();
            }
        }
    }

    /**
     * Guess MIME type from file name extension.
     *
     * <p>If the extension is missing or unknown
     * <code>application/octet-stream</code> type will be returned.</p>
     * 
     * @param filename the filename with extension.
     * @return the content type.
     */
    public String getContentType(String filename)
    {
        if (mimeMap != null) 
        {
            return mimeMap.getContentType(filename);
        }
        else
        {
            return "application/octet-stream";
        }
    }

	/**
	 * Checks if given address is valid.
	 * 
	 * @param address the address
	 * @return <code>true</code> if address is valid.
	 */
	public boolean isValidEmailAddress(String address)
	{
		Matcher m = emailAddressPattern.matcher(address);
		return m.matches();
	}
	    
    /**
     * The authenticator .
     */
    public static class LedgeAuthenticator
        extends Authenticator
    {
        private Map authInfo = new HashMap();

		/**
		 * Create new Ledge authentication.
		 * 
		 * @param config the configuration.
		 */
        public LedgeAuthenticator(Configuration config)
        {
        	//TODO configure the authenticator.
        }

		/**
		 * Add credentials for the specified protocol.
         * 
         * @param protocol the protocol.
         * @param credentials the credentials.
		 */
        public void addCredentials(String protocol, String credentials)
        {
        	throw new UnsupportedOperationException("not implemented yet");
       	}

		/**
		 * {@inheritDoc}
		 */
        public PasswordAuthentication getPasswordAuthentication()
        {
            Map credentials = (Map)authInfo.get(getRequestingProtocol());
            if(credentials == null)
            {
                credentials = (Map)authInfo.get(null);
            }
            if(credentials != null)
            {
                String user = getDefaultUserName();
                if((user == null || user.equals("")) && credentials.size() == 1)
                {
                    user = (String)credentials.keySet().toArray()[0];
                }
                String pass = (String)credentials.get(user);
                if(pass != null)
                {
                    return new PasswordAuthentication(user, pass);
                }
            }
            return null;
        }
    }
}

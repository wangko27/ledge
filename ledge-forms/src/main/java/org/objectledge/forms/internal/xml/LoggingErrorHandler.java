package org.objectledge.forms.internal.xml;

import org.jcontainer.dna.Logger;
import org.objectledge.forms.internal.xml.impl.Localizer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Error handler that logs all errors and warnings.
 *
 * @author <a href="mailto:zwierzem@ngo.pl">Damian Gajda</a>
 * @version $Id: LoggingErrorHandler.java,v 1.3 2006-04-28 10:02:24 pablo Exp $
 */
public class LoggingErrorHandler extends BaseErrorHandler
{
    public LoggingErrorHandler(Logger log)
    {
        this.log = log;
    }

    Logger log;

    //-----------------------------------------------------------------------
    // BaseErrorHandler methods
    protected void onTooManyErrors(SAXParseException e)
    throws SAXException, SAXParseException
    {
        log.error(Localizer.localize(MSG_TOO_MANY_ERRORS));
    }

    public void onFatalError( SAXParseException e )
    throws SAXException
    {
        log.error(getSAXParseException( e, MSG_FATAL ));
    }

    //-----------------------------------------------------------------------
    // ErrorHandler methods

    public void error( SAXParseException e )
    throws SAXException
    {
        super.error(e);
        log.error(getSAXParseException( e, MSG_ERROR ));
    }

    public void warning( SAXParseException e )
    {
        super.warning(e);
        log.warn(getSAXParseException( e, MSG_WARNING ));
    }

    //-----------------------------------------------------------------------
    // Other methods

    protected static String getSAXParseException( SAXParseException spe, String prop )
    {
        return Localizer.localize( prop, new Object[]
                                    {
                                        Integer.valueOf(spe.getLineNumber()),
                                        Integer.valueOf(spe.getColumnNumber()),
                                        spe.getSystemId(),
                                        spe.getLocalizedMessage()
                                    } );
    }

    public static final String MSG_TOO_MANY_ERRORS = //arg:1
    "ErrorHandler.TooManyErrors";
    public static final String MSG_ERROR = // arg:4
    "ErrorHandler.Error";
    public static final String MSG_WARNING = // arg:4
    "ErrorHandler.Warning";
    public static final String MSG_FATAL = // arg:4
    "ErrorHandler.Fatal";
}

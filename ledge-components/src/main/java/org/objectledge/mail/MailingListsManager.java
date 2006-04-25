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

package org.objectledge.mail;

import java.util.List;
import java.util.Locale;

import javax.mail.Message;

/**
 * Mailing manager component.
 * 
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: MailingListsManager.java,v 1.11 2006-04-25 15:06:24 rafal Exp $
 */
public interface MailingListsManager 
{
    public enum Status
    {
        /** The manager is installed and configured correctly. */
        OPERATIONAL,
        
        /** The manager is not configured correctly or it's required external dependencies 
         *  are not configured correctly.  */
        UNOPERATIONAL,
        
        /** The manager is not installed or was disabled by the administrator. */
        UNAVAILABLE
    }

    /**
     * Checks the manager's status.
     * <p>
     * When UNOPERATIONAL status is returned, all relevant information should be written to the
     * server logs.
     * </p>
     * <p>
     * When UNOPERATIONAL or UNAVAILABLE status is returned, all methods except this one are free to
     * throw any exceptions as will, with unspecified side effects. In short - don't call them.
     * </p>
     * 
     * @return the manager status.
     */
    public Status getStatus();    
    
    /**
     * Create new mailing list.
     *
     * @param name the name of the list.
     * @param domain the email domain.
     * @param administrators list of administrators's email addresses
     * @param password the administrator password, auto generated if <code>null</code>
     * @param notify if <code>true</code> send notification about list creation.
     * @param locale mailing list locale.
     * @param moderated should member postings be moderated.
     * @throws MailingListsException if ml creation failed.
     */
    public String createList(String name, String domain, String[] administrators, String password,
        boolean notify, Locale locale, boolean moderated)
    	throws MailingListsException;
    
    /**
     * Delete mailing list.
     *
     * @param name name of the list to delete.
     * @param deleteArchived delete if <code>true</code>
     * @throws MailingListsException if ml deletion failed.
     */
    public void deleteList(String name, boolean deleteArchived)
        throws MailingListsException;
    
    /**
     * Get mailing list.
     * 
     * @param name name of the list.
     * @param password the password.
     */
    public MailingList getList(String name, String password)
        throws MailingListsException;

    /**
     * Get mailing list, using system administrator's credantials.
     * 
     * @param name name of the list.
     */
    public MailingList getList(String name)
        throws MailingListsException;

    /**
     * Get all advertised lists.
     * 
     * @return the list of public lists.
     * @throws MailingListsException if something goes wrong.
     */
    public List<String> getPublicLists()
        throws MailingListsException;
    
    /**
     * Get all lists.
     * 
     * @return the list of all lists.
     * @throws MailingListsException if something goes wrong;
     */
    public List<String> getLists()
        throws MailingListsException;
    
    /**
     * Get all available locales.
     * 
     * @return the list of locales.
     * @throws MailingListsException
     */
    public List<Locale> getAvailableLocales()
        throws MailingListsException;
    
    /**
     * Get all e-mail domains configured in the system.
     *  
     * @return e-mail domains configured in the system.
     */
    public List<String> getAvailableDomains()
        throws MailingListsException;
    
    /**
     * Retrieve new messages from system account.
     * 
     * @return the list of new messages.
     * @throws MailingListsException
     */
    public List<Message> getNewMessages()
        throws MailingListsException;    
}

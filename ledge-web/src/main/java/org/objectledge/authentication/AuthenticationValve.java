// 
// Copyright (c) 2003, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
// All rights reserved. 
// 
// Redistribution and use in source and binary forms, with or without modification,  
// are permitted provided that the following conditions are met: 
//  
// * Redistributions of source code must retain the above copyright notice,  
//   this list of conditions and the following disclaimer. 
// * Redistributions in binary form must reproduce the above copyright notice,  
//   this list of conditions and the following disclaimer in the documentation  
//   and/or other materials provided with the distribution. 
// * Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
//   nor the names of its contributors may be used to endorse or promote products  
//   derived from this software without specific prior written permission. 
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED  
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
// IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,  
// INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,  
// BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
// OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,  
// WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)  
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE  
// POSSIBILITY OF SUCH DAMAGE. 
// 

package org.objectledge.authentication;

import java.security.Principal;

import org.objectledge.context.Context;
import org.objectledge.web.HttpContext;
import org.objectledge.web.HttpContext;
import org.objectledge.web.WebConstants;
import org.objectledge.web.mvc.MVCContext;
import org.objectledge.web.mvc.MVCContextImpl;

/**
 * Pipeline processing valve that initialize pipeline context.
 *
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: AuthenticationValve.java,v 1.2 2004-01-12 14:37:12 fil Exp $
 */
public class AuthenticationValve implements Runnable, WebConstants
{
	/** the context */
	private Context context;
	
	/** the authentication component */
	private Authentication authentication;
	
	/**
	 * Constructor
	 * 
	 * @param context the context.
     * @param authentication the authentication component.
	 */
	public AuthenticationValve(Context context, Authentication authentication)
	{
		this.context = context;		
		this.authentication = authentication;
	}
	
    /**
     * Run the pipeline valve - authenticate user.
     */
    public void run()
    {
    	HttpContext httpContext = HttpContext.retrieve(context);
		MVCContext mvcContext = MVCContextImpl.retrieve(context);
    	Principal principal = (Principal)httpContext.getRequest().
			getSession().getAttribute(PRINCIPAL_SESSION_KEY);
		Principal anonymous = authentication.getAnonymousUser();
		boolean authenticated = false;
		if(principal == null)
		{
			principal = anonymous;
		}
		else
		{
			authenticated = !principal.equals(anonymous);
		}
		mvcContext.setUserPrincipal(principal, authenticated);
    	httpContext.getRequest().getSession().setAttribute(PRINCIPAL_SESSION_KEY, principal);
    }
}

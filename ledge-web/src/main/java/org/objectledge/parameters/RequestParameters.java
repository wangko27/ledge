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

package org.objectledge.parameters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.objectledge.context.Context;

/**
 *
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: RequestParameters.java,v 1.4 2004-06-29 13:40:51 zwierzem Exp $
 */
public class RequestParameters extends DefaultParameters
{
	/**
	 * Usefull method to retrieve parameters from context.
	 *
	 * @param context the context.
	 * @return the request parameters.
	 */
	public static Parameters getRequestParameters(Context context)
	{
		return (Parameters)context.getAttribute(RequestParameters.class);
	}
	
    /**
     * Create the parameter container with parameters found in http request.
     *
     * @param request the request
     * @param encoding the encoding
     * @throws IllegalArgumentException if illegal escape sequences appears.
     */
    public RequestParameters(HttpServletRequest request, String encoding)
    	throws IllegalArgumentException
    {
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements())
        {
            String name = (String)names.nextElement();
            String[] values = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++)
            {
                add(name, values[i]);
            }
        }

        try
        {
            if (request.getPathInfo() != null)
            {
                StringTokenizer st = new StringTokenizer(request.getPathInfo(), "/");
                boolean isName = true;
                String name = null;
                String value = null;
                while (st.hasMoreTokens())
                {
                    if (isName)
                    {
                        name = URLDecoder.decode(st.nextToken(), encoding);
                    }
                    else
                    {
                        value = URLDecoder.decode(st.nextToken(), encoding);
                        add(name, value);
                    }
                    isName = !isName;
                }
            }
        }
        ///CLOVER:OFF
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Unsupported encoding exception " + e.getMessage());
        }
        ///CLOVER:ON
    }
}

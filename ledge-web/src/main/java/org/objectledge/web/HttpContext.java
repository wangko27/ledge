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

package org.objectledge.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.objectledge.context.Context;


/**
 * The web context contains all needed information about http request.
 *
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: HttpContext.java,v 1.4 2004-01-13 15:48:39 pablo Exp $
 */
public class HttpContext
{
    /** the key that points the http context is thread context. */ 
    public static final String CONTEXT_KEY = "objectledge.web.http_context";

	/**
	 *  Usefull method to retrieve http context from context.
	 *
	 * @param context the context.
	 * @return the http context.
	 */
	public static HttpContext retrieve(Context context)
	{
		return (HttpContext)context.getAttribute(CONTEXT_KEY);
	}
	
	/** http request */
	private HttpServletRequest request;
	
	/** http response */
	private HttpServletResponse response;
	
	/** direct response flag */
	private boolean directResponse;
	
	/** response content type */
	private String contentType;
	
	/** the output writer */
	private PrintWriter writer;
	
	/** the encoding */
	private String encoding;

	/**
	 * Construct new http context.
	 * 
	 * @param request the http request.
	 * @param response the http response.
	 */
	public HttpContext(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
		directResponse = false;
		contentType = request.getContentType();
		encoding = "ISO-8859-1";
	}
	
    /**
     * Get the servlet request.
     * 
     * @return the http request
     */
    public HttpServletRequest getRequest()
    {
    	return request;
    }
    
    /**
     * Get the servlet response.
     *
     * @return the http response.
     */
	public HttpServletResponse getResponse()
	{
		return response;
	}
	
    /**
     * Sends a temporary redirect response to new location
     *
     * @param location the redirect location URL.
     * @throws java.io.IOException If an input or output exception occurs.
     */
	public void sendRedirect(String location)
		throws IOException
	{
		directResponse = true;
		response.sendRedirect(location);
	}
	
    /**
     * Wrapping method for writing some data to response output stream.
     *  
     * @return an OutputStream.
     * @throws IOException if happens.
     */
	public OutputStream getOutputStream()
		throws IOException
	{
		directResponse = true;
		response.setContentType(getContentType());
		return response.getOutputStream();
	}

	/**
	 * Returns an PrintWriter for writing characters into the response.
	 * 
	 * @return a PrintWriter.
	 * @throws IOException if happened.
	 */
	public PrintWriter getPrintWriter()
		throws IOException
	{
		if(writer == null)
		{
			directResponse = true;
			Writer osw = new OutputStreamWriter(response.getOutputStream(),
												encoding);
			writer = new PrintWriter(osw, false);
			response.setContentType(getContentType());
		}
		return writer;
	}

    /**
     * Sets the direct response flag.
     */
	public void setDirectResponse()
	{
		directResponse = true;
	}

    /**
     * Returns the direct response flag.
     *
     * @return the direct response flag.
     */
	public boolean getDirectResponse()
	{
		return directResponse;
	}
	
    /**
     * Returns the content type.
     *
     * @return the content type.
     */
	public String getContentType()
	{
		return contentType;
	}

    /**
     * Sets the content type.
     *
     * @param type the content type.
     */
	public void setContentType(String type)
	{
		contentType = type;
	}
	
	/**
	 * Returns the encoding.
	 *
	 * @return the encoding.
	 */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * Sets the encoding.
	 *
	 * @param encoding the encoding.
	 */
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
}

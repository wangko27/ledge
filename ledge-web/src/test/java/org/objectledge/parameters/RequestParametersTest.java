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

package org.objectledge.parameters;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Mock;
import org.objectledge.context.Context;
import org.objectledge.utils.LedgeTestCase;
import org.objectledge.web.HttpContext;

/**
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RequestParametersTest extends LedgeTestCase
{
    private Context context;

    private Mock mockHttpServletRequest;
    private HttpServletRequest httpServletRequest;
    private Mock mockHttpServletResponse;
    private HttpServletResponse httpServletResponse;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        context = new Context();
        
        mockHttpServletRequest = mock(HttpServletRequest.class);
        httpServletRequest = (HttpServletRequest)mockHttpServletRequest.proxy();
        mockHttpServletRequest.stubs().method("getContentType").will(returnValue("text/html"));
        Vector parameterNames = new Vector();
        parameterNames.add("foo");
        mockHttpServletRequest.stubs().method("getParameterNames").
            will(returnValue(parameterNames.elements()));
        mockHttpServletRequest.stubs().method("getParameterValues").with(eq("foo")).
            will(returnValue(new String[] { "barek", "bar" }));
        mockHttpServletRequest.stubs().method("getQueryString").will(returnValue("foo=barek"));
        mockHttpServletRequest.stubs().method("getPathInfo").will(returnValue("view/Default"));

        HttpContext httpContext = new HttpContext(httpServletRequest, httpServletResponse);
        context.setAttribute(HttpContext.class, httpContext);
        RequestParametersLoaderValve paramsLoader = new RequestParametersLoaderValve();
        paramsLoader.process(context);
    }

    public void testInit()
    {
        Parameters parameters = RequestParameters.getRequestParameters(context);
        assertNotNull(parameters);
        assertEquals(parameters.get("view"), "Default");
        assertEquals(parameters.getStrings("foo")[0], "barek");
        assertEquals(parameters.getStrings("foo")[1], "bar");
    }

}

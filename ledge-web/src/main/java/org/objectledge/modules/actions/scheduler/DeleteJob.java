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
package org.objectledge.modules.actions.scheduler;

import org.objectledge.context.Context;
import org.objectledge.parameters.Parameters;
import org.objectledge.parameters.RequestParameters;
import org.objectledge.pipeline.ProcessingException;
import org.objectledge.scheduler.AbstractJobDescriptor;
import org.objectledge.scheduler.AbstractScheduler;
import org.objectledge.templating.TemplatingContext;
import org.objectledge.web.mvc.MVCContext;
import org.objectledge.web.mvc.builders.PolicyProtectedAction;
import org.objectledge.web.mvc.security.PolicySystem;

/**
 * Delete job.
 * 
 * @author <a href="mailto:pablo@caltha.pl">Pawel Potempski</a>
 * @version $Id: DeleteJob.java,v 1.2 2005-07-07 08:29:26 zwierzem Exp $
 */
public class DeleteJob 
    extends PolicyProtectedAction
{
    private AbstractScheduler scheduler;
    
    /**
     * Action constructor.
     */
    public DeleteJob(PolicySystem policySystemArg, AbstractScheduler scheduler)
    {
        super(policySystemArg);
		this.scheduler = scheduler;
    }

    /**
     * Run the valve.
     * 
     * @param context the context.
     * @throws ProcessingException if action processing fails.
     */
    public void process(Context context) throws ProcessingException
    {
        TemplatingContext templatingContext = TemplatingContext.getTemplatingContext(context);
		Parameters parameters = RequestParameters.getRequestParameters(context);
		String name = parameters.get("name", "");
		if(name.length() > 0)
		{
			AbstractJobDescriptor descriptor = scheduler.getJobDescriptor(name);
			try
			{
				scheduler.deleteJobDescriptor(descriptor);
				templatingContext.put("result", "scheduler.deleted_successfully");
				MVCContext mvcContext = MVCContext.getMVCContext(context);
				mvcContext.setView("scheduler.Jobs");
			}
			catch(Exception e)
			{
				throw new ProcessingException("failed to delete job");
			}
		}
    }
}


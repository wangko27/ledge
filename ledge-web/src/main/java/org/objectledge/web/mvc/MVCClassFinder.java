// 
// Copyright (c) 2003, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
// All rights reserved. 
// 
// Redistribution and use in source and binary forms, with or without modification,  
// are permitted provided that the following conditions are met: 
//  
// * Redistributions of source code must retain the above copyright notice,  
//	 this list of conditions and the following disclaimer. 
// * Redistributions in binary form must reproduce the above copyright notice,  
//	 this list of conditions and the following disclaimer in the documentation  
//	 and/or other materials provided with the distribution. 
// * Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
//	 nor the names of its contributors may be used to endorse or promote products  
//	 derived from this software without specific prior written permission. 
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
package org.objectledge.web.mvc;

import org.objectledge.web.mvc.builders.Builder;

/**
 * A class finder for finding MVC model classes.
 *
 * @author <a href="mailto:dgajda@caltha.pl">Damian Gajda</a>
 * @version $Id: MVCClassFinder.java,v 1.2 2003-12-30 14:41:37 zwierzem Exp $
 */
public interface MVCClassFinder
{
    /**
     * Returns a builder instance based on a given name. If no builder is found, a
     * {@link org.objectledge.web.mvc.builders.DefaultBuilder} is returned.
     * 
     * @param builderName part of a name of a builder class to be found/instantiated
     * @return found builder instance
     */
    public Builder findBuilder(String builderName);

	/**
	 * Returns an enclosing builder for a given builder. If no builder is found, a
	 * {@link org.objectledge.web.mvc.builders.DefaultBuilder} is returned.
	 * 
	 * @param builder a builder for which an eclosing builder must be found 
     * @return found builder instance
	 */
	public Builder findEnclosingBuilder(Builder builder);

    /**
     * Creates an MVC compoment name for a given class object.
     * 
     * @param clazz class to find MVC component name for
     * @return finder usable MVC compoment name
     */
    public String findName(Class clazz);
}

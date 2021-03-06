// 
// Copyright (c) 2003-2005, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
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
package org.objectledge.statistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SatisticsProvider imlemenation that implements {@link #getDataValue(String)} method by 
 * reflectively invoking get<em>name</em>Value() methods. 
 *
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: ReflectiveStatisticsProvider.java,v 1.3 2005-05-16 09:37:25 rafal Exp $
 */
public abstract class ReflectiveStatisticsProvider
    implements StatisticsProvider
{
    /**
     * {@inheritDoc}
     */
    public Number getDataValue(String name)
    {
        Method m;
        try
        {
            m = getClass().getMethod(getMethodName(name));
            return (Number)m.invoke(this);
        }
        catch(InvocationTargetException e)
        {
            throw new RuntimeException("failed to retrieve value", e.getTargetException());
        }
        catch(Exception e)
        {
            throw new RuntimeException("introspection problem", e);
        }
    }

    /**
     * Derives method name from DataSource label: foo.bar.baz -&gt; getFooBarBazValue. 
     * 
     * @param dataSourceLabel data source label.
     * @return method name.
     */
    protected String getMethodName(String dataSourceLabel)
    {
        String[] tokens = dataSourceLabel.split("_");
        StringBuilder buff = new StringBuilder(dataSourceLabel.length());
        buff.append("get");
        for(int i = 0; i < tokens.length; i++)
        {
            buff.append(Character.toUpperCase(tokens[i].charAt(0)));
            buff.append(tokens[i].substring(1).toLowerCase());
        }
        return buff.toString();
    }
}

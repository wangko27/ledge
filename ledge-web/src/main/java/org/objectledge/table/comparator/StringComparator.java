// 
// Copyright (c) 2003-2005, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
// All rights reserved. 
//   
// Redistribution and use in source and binary forms, with or without modification,  
// are permitted provided that the following conditions are met: 
//   
// * Redistributions of source code must retain the above copyright notice,  
// this list of conditions and the following disclaimer. 
// * Redistributions in binary form must reproduce the above copyright notice,  
// this list of conditions and the following disclaimer in the documentation  
// and/or other materials provided with the distribution. 
// * Neither the name of the Caltha - Gajda, Krzewski, Mach, Potempski Sp.J.  
// nor the names of its contributors may be used to endorse or promote products  
// derived from this software without specific prior written permission. 
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

package org.objectledge.table.comparator;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 *
 * @author <a href="rafal@caltha.pl">Rafał Krzewski</a>
 * @version $Id: StringComparator.java,v 1.2 2006-04-28 07:30:16 rafal Exp $
 */
public class StringComparator
    implements Comparator<String>
{
    /** The Collator to use for comparisons. */
    private final Collator collator;
    
    private static final Map<Locale, StringComparator> instances = 
        new HashMap<Locale, StringComparator>();

    /**
     * Factory method.
     * 
     * @param locale the Locale.
     * @return comparator instance.
     */
    public static synchronized StringComparator getInstance(Locale locale)
    {
        StringComparator instance = instances.get(locale);
        if(instance == null)
        {
            instance = new StringComparator(locale);
            instances.put(locale, instance);     
        }
        return instance;
    }
    
    /**
     * Private constructor, to enforce factory method use.
     *
     * @param locale the Locale.
     */
    private StringComparator(Locale locale)
    {
        this.collator = Collator.getInstance(locale);
    }
    
    /**
     * {@inheritDoc}
     */
    public int compare(String s1, String s2)
    {
        return collator.compare(s1, s2);
    }
}

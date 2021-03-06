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
package org.objectledge.selector;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.objectledge.filesystem.FileSystem;
import org.objectledge.test.LedgeTestCase;
import org.objectledge.xml.XMLGrammarCache;
import org.objectledge.xml.XMLValidator;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: SelectorTest.java,v 1.5 2005-11-16 23:38:15 zwierzem Exp $
 */
public class SelectorTest extends LedgeTestCase
{
    public Configuration getSelectorConfig(String name)
        throws Exception
    {
        FileSystem fs = FileSystem.getStandardFileSystem("src/test/resources");
        String configPath = "selector/"+name;
        URL configUrl = fs.getResource(configPath);
        URL schemaUrl = fs.getResource("org/objectledge/selector/Selector.rng");
        if(configUrl == null || schemaUrl == null)
        {
            throw new Exception("fs config invalid, or files missing");
        }
        XMLValidator validator = new XMLValidator(new XMLGrammarCache());
        try
        {
            validator.validate(configUrl, schemaUrl);
        }
        catch(SAXParseException e)
        {
            throw new Exception("parser error "+e.getMessage()+" in "+e.getSystemId()+" at line "+
                e.getLineNumber(), e);
        }
        return getConfig(fs, configPath);
    }

    public void testSelector()
        throws Exception
    {
        Configuration config = getSelectorConfig("Selector.xml");
        Object[] objects = new Object[3];
        objects[0] = new Integer(0);
        objects[1] = new Integer(1);
        objects[2] = new Integer(2);
        Selector selector = new Selector(config, objects);
        Map<String, Object> values = new HashMap<String, Object>();
        Variables vars = new IntrospectionVariables(values);
        values.put("number", new Integer(0));
        Integer i = (Integer)selector.select(vars);
        assertEquals(0, i.intValue());
        values.put("number", new Integer(1));
        i = (Integer)selector.select(vars);
        assertEquals(1, i.intValue());
        values.put("number", new Integer(99));
        i = (Integer)selector.select(vars);
        assertEquals(2, i.intValue());
    }
    
    public void testFallthrough()
        throws Exception
    {
        Configuration config = getSelectorConfig("Fallthrough.xml");
        Object[] objects = new Object[2];
        objects[0] = new Integer(0);
        objects[1] = new Integer(1);
        Selector selector = new Selector(config, objects);
        Map<String, Object> values = new HashMap<String, Object>();
        Variables vars = new IntrospectionVariables(values);
        values.put("number", new Integer(0));
        Integer i = (Integer)selector.select(vars);
        assertEquals(0, i.intValue());
        values.put("number", new Integer(1));
        i = (Integer)selector.select(vars);
        assertEquals(1, i.intValue());
        values.put("number", new Integer(99));
        i = (Integer)selector.select(vars);
        assertNull(i);                
    }
    
    public void testUnmatched()
        throws Exception
    {
        Configuration config = getSelectorConfig("Selector.xml");
        Object[] objects = new Object[2];
        objects[0] = new Integer(0);
        objects[1] = new Integer(1);
        try
        {
            new Selector(config, objects);
            fail("exception expected");
        }
        catch(Exception e)
        {
            assertEquals(ConfigurationException.class, e.getClass());
        }
    }
}

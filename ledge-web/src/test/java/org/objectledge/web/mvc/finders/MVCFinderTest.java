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
package org.objectledge.web.mvc.finders;

import junit.framework.TestCase;

import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.Logger;
import org.objectledge.configuration.ConfigurationFactory;
import org.objectledge.context.Context;
import org.objectledge.filesystem.FileSystem;
import org.objectledge.logging.LoggerFactory;
import org.objectledge.pipeline.Valve;
import org.objectledge.templating.Template;
import org.objectledge.templating.Templating;
import org.objectledge.templating.velocity.VelocityTemplating;
import org.objectledge.test.actions.foo.TestAction;
import org.objectledge.web.mvc.builders.Builder;
import org.objectledge.web.mvc.components.Component;
import org.objectledge.xml.XMLGrammarCache;
import org.objectledge.xml.XMLValidator;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * 
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: MVCFinderTest.java,v 1.13 2005-03-29 10:53:22 zwierzem Exp $
 */
public class MVCFinderTest extends TestCase
{
    private MVCFinder finder;
    
    private Templating templating;
    
    /**
     * Constructor for MVCFinderTest.
     * @param arg0
     */
    public MVCFinderTest(String arg0)
    {
        super(arg0);
    }

    public void setUp()
        throws Exception
    {
        FileSystem fs = FileSystem.getStandardFileSystem("src/test/resources/finder");
        XMLValidator validator = new XMLValidator(new XMLGrammarCache());        
        ConfigurationFactory configFactory = new ConfigurationFactory(fs, validator, ".");
        Configuration config = configFactory.
            getConfig(NameSequenceFactory.class, NameSequenceFactory.class);
        NameSequenceFactory nameSequenceFactory = new NameSequenceFactory(config);
        config = configFactory.getConfig(Templating.class, VelocityTemplating.class);
        LoggerFactory loggerFactory = new LoggerFactory(null);
        Logger logger = loggerFactory.getLogger(Templating.class);
        templating = new VelocityTemplating(config, logger, fs);
        logger = loggerFactory.getLogger(MVCFinder.class);
        MutablePicoContainer container = new DefaultPicoContainer();
        container.registerComponentImplementation(Context.class); 
        finder = new MVCFinder(container, logger, templating, nameSequenceFactory);
    }

    public void testFindBuilderTemplate()
    {
        Template template = finder.findBuilderTemplate("foo.Bar");
        assertEquals( "views/foo/Bar", template.getName());
        template = finder.findBuilderTemplate(null);
        assertNull(template);
        template = finder.findBuilderTemplate("foo.NonExistentView");
        assertEquals("views/foo/Default", template.getName());
        template = finder.findBuilderTemplate("NonExistentView");
        assertEquals("views/Default", template.getName());
        template = finder.findBuilderTemplate("nonexistentpackage.NonExistentView");
        assertEquals("views/Default", template.getName());
    }

    public void testGetAction()
        throws Exception
    {
        Valve action = finder.getAction("foo.TestAction");
        assertEquals(TestAction.class, action.getClass());
        action = finder.getAction("foo.NonExistentAction");
        assertNull(action);
    }

    public void testFindBuilder()
    {
        Builder builder = finder.findBuilder("foo.Bar");
        assertEquals(org.objectledge.test.views.foo.Bar.class, builder.getClass());
        builder = finder.findBuilder(null);
        assertNull(builder);
        builder = finder.findBuilder("foo.NotExistentClass");
        assertNull(builder);
        builder = finder.findBuilder("NotExistentClass");
        assertNull(builder);
        builder = finder.findBuilder("nonexistentpackage.NotExistentClass");
        assertNull(builder);
    }
    
    public void testFindEnclosingViewName()
    {
        String viewName = ("foo.Bar");
        Builder builder = finder.findBuilder(viewName);
        assertEquals(org.objectledge.test.views.foo.Bar.class, builder.getClass());
        viewName = finder.findEnclosingViewName(viewName);
        builder = finder.findBuilder(viewName);
        assertEquals(org.objectledge.test.views.foo.Default.class, builder.getClass());
        viewName = finder.findEnclosingViewName(viewName);
        builder = finder.findBuilder(viewName);
        assertEquals(org.objectledge.test.views.Default.class, builder.getClass());
        viewName = finder.findEnclosingViewName(viewName);
        builder = finder.findBuilder(viewName);
        assertNull(builder);        
    }

    public void testGetComponentTemplate()
    {
        Template template = finder.getComponentTemplate("one.Component");
        assertEquals("components/one/Component", template.getName());
        template = finder.getComponentTemplate(null);
        assertNull(template);
        template = finder.getComponentTemplate("one.NonexistentComponent");
        assertNull(template);
    }

    public void testGetComponent()
    {
        Component component = finder.getComponent("one.Component");
        assertEquals(org.objectledge.test.components.one.Component.class, component.getClass());
        component = finder.getComponent(null);
        assertNull(component);
        component = finder.getComponent("one.NonexistentComponent");
        assertNull(component);
    }
}

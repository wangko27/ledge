// 
//Copyright (c) 2003, 2004, Caltha - Gajda, Krzewski, Mach, Potempski Sp.J. 
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
package org.objectledge.xml;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.jcontainer.dna.Logger;
import org.objectledge.datatype.xml.LoggingGrammarReaderController;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.msv.grammar.Grammar;

/**
 * Grammar cache - loads and caches MSV's Grammar objects.
 *
 * @author <a href="mailto:dgajda@caltha.pl">Damian Gajda</a>
 * @version $Id: XMLGrammarCache.java,v 1.1 2004-05-12 09:54:04 zwierzem Exp $
 */
public class XMLGrammarCache
{
	private Logger logger;
	private SAXParserFactory parserFactory;
	private HashMap grammars = new HashMap();

	/**
	 * Creates a XML grammar cache.
	 * 
	 * @param logger used for login warnings and informative messages.
	 * @throws ParserConfigurationException on errors with XML parser configuration
	 * @throws SAXException on errors with XML parser configuration
	 */
    public XMLGrammarCache(Logger logger)
		throws ParserConfigurationException, SAXException
    {
		this.logger = logger;

		this.parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);
		parserFactory.setValidating(false);
    }

    /**
     * This method adds grammars to grammar map (cache), that's why it is <code>synchronized</code>.
     * 
     * @param grammarURI URI of a loaded grammar
     * @return loaded grammar
     * @throws Exception on errors while loading grammar
     */
    public synchronized Grammar getGrammar(String grammarURI)
	    throws Exception
    {
        if(grammars.containsKey(grammarURI))
        {
            return (Grammar)grammars.get(grammarURI);
        }

        Grammar grammar = null;
        if(grammarURI != null)
        {
            grammar = loadGrammar(grammarURI);
            grammars.put(grammarURI, grammar);
        }
        return grammar;
    }


    /**
     * This method loads grammars.
     * 
     * @param grammarURI grammar to be loaded
     * @throws Exception on errors while loading grammar
     */
    private Grammar loadGrammar(String grammarURI)
    	throws Exception
    {
        logger.info("loading grammar '"+grammarURI+"'");

        Grammar grammar=null;
        final long stime = System.currentTimeMillis();
        // parse schema and other XML-based grammars
        // GrammarLoader will detect the language.
		// TODO Add objectledge EntityResolver implementation
        org.xml.sax.EntityResolver er = null; 
		// TODO Add objectledge input source creation ??
        grammar = com.sun.msv.reader.util.GrammarLoader.loadSchema(
			new InputSource(grammarURI), 
        	new LoggingGrammarReaderController(grammarURI, logger, er),
        	parserFactory);

        if(grammar == null)
        {
            throw new Exception("Unknow reason for error when loading grammar '"+grammarURI+"'");
        }

        long parsingTime = System.currentTimeMillis();
        logger.info("grammar parsing time "+new Long(parsingTime-stime)+" '"+grammarURI+"'");

        return grammar;
    }
}

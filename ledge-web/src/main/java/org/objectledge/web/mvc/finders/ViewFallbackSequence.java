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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * Generates view falback sequence for templates and classes.
 * 
 * <p> For constructor arguments ("a.b.c", ".", "/", "Default") the following is generated: </p>
 * <p>
 *   <table>
 *     <tr>
 *       <th>iteration</th>
 *       <th>next()</th>
 *     </tr>
 *     <tr>
 *       <td>1</td>
 *       <td>a/b/c/Default</td>
 *     </tr>
 *     <tr>
 *       <td>2</td>
 *       <td>a/b/Default</td>
 *     </tr>
 *     <tr>
 *       <td>3</td>
 *       <td>a/Default</td>
 *     </tr>
 *     <tr>
 *       <td>4</td>
 *       <td>Default</td>
 *     </tr>
 *   </table>
 * </p>
 * 
 * <p> For constructor arguments ("a.b.C", ".", "/", "Default") the following is generated: </p>
 * <p>
 *   <table>
 *     <tr>
 *       <th>iteration</th>
 *       <th>next()</th>
 *     </tr>
 *     <tr>
 *       <td>1</td>
 *       <td>a/b/C</td>
 *     </tr>
 *     <tr>
 *       <td>2</td>
 *       <td>a/b/Default</td>
 *     </tr>
 *     <tr>
 *       <td>3</td>
 *       <td>a/Default</td>
 *     </tr>
 *     <tr>
 *       <td>4</td>
 *       <td>Default</td>
 *     </tr>
 *   </table>
 * </p>
 * 
 * <p> For constructor arguments ("a.b.Default", ".", "/", "Default") the following is 
 *     generated: </p>
 * <p>
 *   <table>
 *     <tr>
 *       <th>iteration</th>
 *       <th>next()</th>
 *     </tr>
 *     <tr>
 *       <td>1</td>
 *       <td>a/b/Default</td>
 *     </tr>
 *     <tr>
 *       <td>2</td>
 *       <td>a/Default</td>
 *     </tr>
 *     <tr>
 *       <td>3</td>
 *       <td>Default</td>
 *     </tr>
 *   </table>
 * </p>
 *  
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: ViewFallbackSequence.java,v 1.8 2005-02-21 13:51:17 zwierzem Exp $
 */
public class ViewFallbackSequence
    implements Sequence
{
    private String[] tokens;
    
    private int startPosition;
    
    private int position; 
    
    private StringBuilder buff = new StringBuilder();
    
    private StringBuilder buff2 = new StringBuilder();
    
    private char inSeparator;
    
    private char outSeparator;
    
    private String defaultSuffix;
    
    private String currentView;
    
    /**
     * Constructs a view fallback sequence.
     * @param path the view path.
     * @param inSeparator separator used in the path argument.
     * @param outSeparator separator to be used in generated paths.
     * @param defaultSuffix the suffix to append.
     * @param skipFirst skip first result - used for looking up enclosing views.
     */
    public ViewFallbackSequence(String path, char inSeparator, 
        char outSeparator, String defaultSuffix, boolean skipFirst)
    {
        StringTokenizer tokenizer = new StringTokenizer(path, ""+inSeparator);
        if(path.equals(defaultSuffix) || path.endsWith(inSeparator+defaultSuffix))
        {
            tokens = new String[tokenizer.countTokens()-1];
        }
        else
        {
            tokens = new String[tokenizer.countTokens()];
        }
        for(int i=0; i<tokens.length; i++)
        {
            tokens[i] = tokenizer.nextToken();
        }
        startPosition = skipFirst ? 1 : 0;
        position = startPosition;
        this.inSeparator = inSeparator;
        this.outSeparator = outSeparator;
        this.defaultSuffix = defaultSuffix;
    }
    
    /**
     * Reset the sequence to the beginning.
     */
    public void reset()
    {
        position = startPosition;
    }
    
    /**
     * Checks if there are more elements in the sequence.
     * 
     * @return <code>true</code> if there are more elements in the sequence.
     */
    public boolean hasNext()
    {
        return position < tokens.length+1;
    }
    
    /**
     * Returns the next path in the sequence.
     * 
     * @return the next path in the sequence.
     */
    public String next()
    {
        if(!hasNext())
        {
            throw new NoSuchElementException((position+1)+" > "+(tokens.length+1));
        }
        buff.setLength(0);
        buff2.setLength(0);
        for(int i=0; i<tokens.length - position; i++)
        {
            buff.append(tokens[i]);
            buff2.append(tokens[i]);
            if(i < tokens.length - position -1)
            {
                buff.append(outSeparator);
                buff2.append(inSeparator);
            }
        }
        if(tokens.length != 0)
        {
            if(position != 0 || !Character.isUpperCase(tokens[tokens.length-1].charAt(0)))
            {
                if(position < tokens.length)
                {
                    buff.append(outSeparator);
                    buff2.append(inSeparator);
                }
                buff.append(defaultSuffix);
                buff2.append(defaultSuffix);
            }
        }
        else
        {
            buff.append(defaultSuffix);
            buff2.append(defaultSuffix);
        }
        position++;
        currentView = buff2.toString();
        return buff.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    public String currentView()
    {
        return currentView;
    }
}
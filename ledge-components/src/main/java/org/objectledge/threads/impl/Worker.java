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
package org.objectledge.threads.impl;

import org.apache.commons.pool.ObjectPool;
import org.jcontainer.dna.Logger;
import org.objectledge.context.Context;
import org.objectledge.pipeline.Valve;
import org.objectledge.threads.Task;
import org.picocontainer.lifecycle.Stoppable;

/**
 * 
 * @author <a href="mailto:rafal@caltha.pl">Rafal Krzewski</a>
 * @version $Id: Worker.java,v 1.1 2004-01-30 15:52:27 fil Exp $
 */
public class Worker 
    implements Runnable, Stoppable
{
    private Thread thread;
    
    private String name;
    
    private Logger log;
    
    private Task task; 
    
    private Task currentTask;
    
    private Context context;
    
    private Valve cleanup;
    
    private ObjectPool pool;
    
    private boolean shutdown = false;

    /**
     * Creates a daemon thread.
     * 
     * @param name the worker's name.
     * @param priority the task's priority (see {@link java.lang.Thread} description).
     * @param pool the ObjectPool this worker belongs to.
     * @param threadGroup the thread group where the thread should belong.
     * @param log the logger to use.
     * @param context thread's processing context.
     * @param cleanup cleanup valve to invoke, should the task terminate.
     */
    public Worker(String name, int priority, 
        ObjectPool pool, ThreadGroup threadGroup, Logger log, Context context, Valve cleanup)
    {
        this.pool = pool;
        this.log = log;
        this.context = context;
        this.name = name;
        this.cleanup = cleanup;
        thread = new Thread(threadGroup, this, name);
        thread.setPriority(priority);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Dispatches a Task using this worker thread.
     * 
     * @param task the task to dispatch.
     */
    public synchronized void dispatch(Task task)
    {
        this.task = task;
        notify();
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        log.info("starting "+name);

        loop: while(!shutdown)
        {
            synchronized(this)
            {
                if(task == null)
                {
                    try
                    {
                        this.wait();
                    }
                    catch(InterruptedException e)
                    {
                        log.warn(name+" was interrupted while waiting tasks");
                        break loop;
                    }
                }
                currentTask = task;
                task = null;
            }
            if(!shutdown)
            {
                thread.setName(currentTask.getName());
                
                try
                {
                    log.debug(name+" starting "+task.getName());
                    task.process(context);
                    log.debug(name+" done "+task.getName());
                }
                catch(VirtualMachineError e)
                {
                    throw e;
                }
                catch(ThreadDeath e)
                {
                    log.warn(name+" was forcibly stopped while running "+currentTask.getName());
                    throw e;
                }
                catch(Throwable e)
                {
                    log.error("uncaught exception in "+currentTask.getName(), e);
                }
                
                if(cleanup != null)
                {
                    try
                    {
                        cleanup.process(context);
                    }
                    catch(VirtualMachineError e)
                    {
                        throw e;
                    }
                    catch(ThreadDeath e)
                    {
                        log.warn(name+" was forcibly stopped while during cleanup", e);
                        throw e;
                    }
                    catch(Throwable e)
                    {
                        log.error("uncaught exception in cleanup", e);
                    }
                }
                
                thread.setName(name);
                currentTask = null;
                try
                {
                    pool.returnObject(this);
                }
                catch(Exception e)
                {
                    log.error("failed to return worker to the pool", e);
                }
            }
        }
        log.info("finished "+name);
    }
    
    /**
     * Returns the task that is currently being executed by the worker.
     * 
     * @return the task that is currently being executed by the worker.
     */
    public Task getCurrentTask()
    {
        return currentTask;
    }
    
    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        
    }
}

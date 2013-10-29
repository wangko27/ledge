package org.objectledge.web.captcha;

import java.io.Serializable;


/**
 * A service that provides CAPTCHA functionality for web applications.
 * 
 * @author rafal
 */
public class CaptchaCacheValue
    implements Serializable
{
    private boolean value;

    private long timestamp;
    
    private long counter;

    public CaptchaCacheValue(boolean value)
    {
        this.value = value;
        this.counter = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean getValue()
    {
        this.counter++;
        return value;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
    
    public long getCounter()
    {
        return counter;
    }
}

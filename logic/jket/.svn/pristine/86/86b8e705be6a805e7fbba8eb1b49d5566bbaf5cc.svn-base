
package ket.util;

import java.util.concurrent.TimeUnit;

public class Timeout
{
	public Timeout(final long timeout)
	{
		this.timeout = timeout;
	}
	
	public Timeout(final long timeout, final TimeUnit timeUnit)
	{
		this.timeout = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
	}
	
	public final boolean isTimeout()
	{
		return System.currentTimeMillis() >= timeExpire;
	}	
	
	public final void reset()
	{
		timeExpire = System.currentTimeMillis() + timeout;
	}
	
	public final void clear()
	{
		timeExpire = 0;
	}
	
	final protected long timeout;
	protected long timeExpire = 0;
}


package ket.util;

import java.util.concurrent.TimeUnit;

public class Cooler extends Timeout
{
	public Cooler(final long coolTime)
	{
		super(coolTime);
	}
	
	public Cooler(final long coolTime, final TimeUnit timeUnit)
	{
		super(coolTime, timeUnit);
	}
	
	public final boolean isCooldown()
	{
		return isTimeout();
	}	
	
	public final boolean tryReset()
	{
		long now = System.currentTimeMillis();
		if( now < timeExpire )
			return false;		
		timeExpire = now + timeout;
		return true;
	}	
	
}

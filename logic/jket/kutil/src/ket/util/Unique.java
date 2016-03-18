
package ket.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Unique
{
	public static int getUniqueInteger()
	{
		return siAtomic.incrementAndGet();
	}
	
	private static AtomicInteger siAtomic = new AtomicInteger();
}

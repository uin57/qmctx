
package ket.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorTool
{
	public static ExecutorService newSingleThreadExecutor(final String threadName)
	{
		return Executors.newSingleThreadExecutor(
				new ThreadFactory()
				{
					@Override
					public Thread newThread(Runnable r)
					{
						Thread t = Executors.defaultThreadFactory().newThread(r);
						t.setName(threadName);
						return t;
					}

				});
	}
	
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(final String threadName)
	{
		return Executors.newSingleThreadScheduledExecutor(
				new ThreadFactory()
				{
					@Override
					public Thread newThread(Runnable r)
					{
						Thread t = Executors.defaultThreadFactory().newThread(r);
						t.setName(threadName);
						return t;
					}

				});
	}
	
	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, final String threadName)
	{
		return Executors.newScheduledThreadPool(corePoolSize,
				new ThreadFactory()
				{
					@Override
					public Thread newThread(Runnable r)
					{
						Thread t = Executors.defaultThreadFactory().newThread(r);
						t.setName(threadName + seq.incrementAndGet());
						return t;
					}
					AtomicInteger seq = new AtomicInteger();
				});
	}
}

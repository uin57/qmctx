package com.joypiegame.gameservice.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class TimerThread 
{ 
	  private ScheduledExecutorService timerExecutor = instanceExecutor(); 
	  private Map<String, ScheduledFuture<?>> timerfutures = new TreeMap<String, ScheduledFuture<?>>();
	  
	  public TimerThread()
	  {
		  
	  }
	  
	  public void start()
	  {
		  System.out.println("Service Thread start!");
	  }
	  
	  public void shutDown()
	  {
		  for (Map.Entry<String, ScheduledFuture<?>> e : timerfutures.entrySet())
		  {
			  ScheduledFuture<?> future = e.getValue();
			  future.cancel(false);
		  }
		  timerExecutor.shutdown();
		  try
		  {
			  while( ! timerExecutor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		  }
		  catch(Exception ex)
		  {			
		  }
		  System.out.println("Service Thread shutdown!");
	  }
	  
	  private ScheduledExecutorService instanceExecutor()
	  {
		  return Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
					{
						@Override
						public Thread newThread(Runnable r)
						{
							Thread t = Executors.defaultThreadFactory().newThread(r);
							return t;
						}

					});
	  }
	  
	  public boolean startTimeTask(String taskname, Runnable task, long initialDelay, long period, TimeUnit unit)
	  {
		  if (timerfutures.containsKey(taskname))
			  return false;
		  ScheduledFuture<?> future = timerExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
		  timerfutures.put(taskname, future);
		  return true;
	  }
	  
	  public void cancelTimerTask(String taskname)
	  {
		  ScheduledFuture<?> future = timerfutures.get(taskname);
		  if (future != null)
		  {
			  future.cancel(false);
		  }
	  }
}




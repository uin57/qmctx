
package i3k.proxy;


import i3k.gs.GameData;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.SKVMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
public class IDIPProxyServer implements SignalHandler
{

	public static class Config
	{		
		public NetAddress addrIDIP = new NetAddress("127.0.0.1", 4105);
		public NetAddress addrIDIPRedirect = new NetAddress("127.0.0.1", 4106);
		
		public String log4jCfgFileName = "ps.log4j.properties";
		
		public String gamesXMLFileName = "gameservers.xml";
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			managerRPC.onTimer();
			onTimer(getTime());
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	private void onTimer(int timeTick)
	{
		//每分钟的30秒检查
		if ( (timeTick + 30) % 60 == 0 )
		{
			for (Map.Entry<String, FileWatchdog> e : fileWatchdogs.entrySet())
			{
				e.getValue().run();
			}
		}
	}
	
	public void mointorFileChanged(String filename, Runnable dochange)
	{
		fileWatchdogs.put(filename, new FileWatchdog(filename, dochange));
	}
	
	private class FileWatchdog implements Runnable
	{ 
		  protected String filename;
		  File file;
		  long lastModif = 0; 
		  Runnable fileChangeHandler;
	
//		  FileWatchdog(Runnable fileChangeHandler)
//		  {
//			  this.fileChangeHandler = fileChangeHandler;
//		  }
		  
		  FileWatchdog(String filename, Runnable fileChangeHandler)
		  {
			  this.fileChangeHandler = fileChangeHandler;
			  setFile(filename); 
		  }
		  
		  void setFile(String filename) 
		  {
			  this.filename = filename;
			  file = new File(filename);
			  checkAndHandleChange();
		  }
	
		  protected void checkAndHandleChange() 
		  {
			  if (file == null)
				  return;
			  boolean fileExists;
			  try 
			  {
				  fileExists = file.exists();
			  } 
			  catch(SecurityException  e) 
			  {
				  return;
			  }
			  if(fileExists) 
			  {
				  long l = file.lastModified(); // this can also throw a SecurityException
				  if(l > lastModif) // however, if we reached this point this
				  {
					  lastModif = l;              // is very unlikely.
					  fileChangeHandler.run();
				  }
			  }
		  }
	  
		@Override
		public void run()
		{
			checkAndHandleChange();
		}
	}
	
	private void monitorLog4jConfiguerFile(final String filename)
	{
		mointorFileChanged(filename, new Runnable()
				{
					@Override
					public void run()
					{
						PropertyConfigurator.configure(filename);
					}
				});
	}
	
	public Config getConfig()
	{
		return cfg;
	}
	
	private void setConfig(File configFile)
	{
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;		
		
		cfg.addrIDIP.host = skv.getString("IDIPServer", "host", cfg.addrIDIP.host);
		cfg.addrIDIP.port = skv.getInteger("IDIPServer", "port", cfg.addrIDIP.port);
		
		cfg.gamesXMLFileName = skv.getString("IDIPServer", "gamesXMLFileName", cfg.gamesXMLFileName);
		cfg.log4jCfgFileName = skv.getString("Log4j", "cfgFile", cfg.log4jCfgFileName);
	}
	
	public int getTime()
	{
		// TODO
		return GameData.getInstance().getTime();
	}
	
	void init()
	{
		forwardtbl.init(cfg.gamesXMLFileName);
		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler()
				{
					@Override
					public void uncaughtException(Thread t, Throwable e)
					{
						logger.error("Uncaughted exception[" + e.getMessage()
								+ "], throwed by thread[" + t.getName() + "]", e);
						System.exit(0);
					}
					
				});
		// db callback
		start();
	}
	
	void destroy()
	{
		if ( timertask.future != null )
			timertask.future.cancel(false);
		managerRPC.destroy();
		logger.info("main executor shutdown start");
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
		logger.info("main executor shutdown ok");
	}
	
	void start()
	{
		//
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
		managerRPC.start();
		logger.info("idip proxy server start");
	}
	
	Logger getLogger()
	{
		return logger;
	}
	
	ForwardTable getForwardTable()
	{
		return forwardtbl;
	}
	
	RPCManager getRPCManager()
	{
		return managerRPC;
	}
	
	public void initSignal()
	{
		Signal.handle(new Signal("TERM"), this);
		ket.util.FileSys.pauseWaitSingal(latch);  
	}
	
	@Override
	public void handle(Signal sig)
	{
		// TODO Auto-generated method stub
		if( bSingalHandled )
			return;
		if( sig.getName().equals("TERM") )
		{
			System.out.println("recieved term");
			bSingalHandled = true;
			latch.countDown();
		}
	}
	
	public IDIPProxyServer(File configFile)
	{
		setConfig(configFile);
		monitorLog4jConfiguerFile(cfg.log4jCfgFileName);
		forwardtbl = new ForwardTable(this);
		managerRPC = new RPCManager(this);
	}
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		IDIPProxyServer ps = new IDIPProxyServer(new File(am.get("-cfgfile", "ps.cfg")));
		ps.init();
		if( am.containsKey("bg") )
		{
			ps.initSignal();
		}
		else
		{
			ket.util.FileSys.pauseWaitInput();
		}
		ps.destroy();
	}

	private volatile boolean bSingalHandled = false;
	private CountDownLatch latch = new CountDownLatch(1);
	private ForwardTable forwardtbl;
	private RPCManager managerRPC;
	private Logger logger = Logger.getLogger("psLogger");
	Config cfg = new Config();
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	private Map<String, FileWatchdog> fileWatchdogs = new TreeMap<String, FileWatchdog>();
}

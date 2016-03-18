
package i3k.friend;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import ket.kdb.DB;
import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.SKVMap;

@SuppressWarnings("restriction")
public class FriendServer implements SignalHandler
{

	public static class Config
	{
		public NetAddress addrListen = new NetAddress("0.0.0.0", 8106);
		public String dbcfgfile = "./dbfriend/dbcfg.xml";
		
		public int id = 1;
		
		public String log4jCfgFileName = "fs.log4j.properties";
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			managerRPC.onTimer();
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	private class FileWatchdog implements Runnable
	{ 
		  protected String filename;
		  File file;
		  long lastModif = 0; 
	
		  FileWatchdog()
		  {
			  
		  }
		  
		  void setFile(String filename) 
		  {
			  this.filename = filename;
			  file = new File(filename);
			  checkAndConfigure();
		  }
	 
		  protected void doOnChange()
		  {
			  PropertyConfigurator.configure(filename);
		  }
	
		  protected void checkAndConfigure() 
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
					  doOnChange();
				  }
			  }
		  }
	  
		@Override
		public void run()
		{
			checkAndConfigure();
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	public class WorldInitTrans implements Transaction
	{	
		public WorldInitTrans()
		{
		}

		@Override
		public boolean doTransaction()
		{	
			// TODO
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				start();
			}
			else
				logger.error("world init failed");
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
	
	}
	
	public FriendServer(File configFile)
	{
		setConfig(configFile);
		fileWatchdog.setFile(cfg.log4jCfgFileName);
		managerData = new DataManager(this);
		managerRPC = new RPCManagerFriendServer(this);		
	}
	
	public DataManager getDataManager()
	{
		return managerData;
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
		
		cfg.addrListen.host = skv.getString("FriendServer", "host", cfg.addrListen.host);
		cfg.addrListen.port = skv.getInteger("FriendServer", "port", cfg.addrListen.port);
		cfg.id = skv.getInteger("FriendServer", "id", cfg.id);
		
		cfg.log4jCfgFileName = skv.getString("Log4j", "cfgFile", cfg.log4jCfgFileName);		
	}
	
	void init()
	{
		
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
		
		db = ket.kdb.Factory.newDB();
		db.setLogger(logger);
		Path p = Paths.get(cfg.dbcfgfile);
		db.open(p.getParent(), p);
		
		db.execute(new WorldInitTrans());
	}
	
	void destroy()
	{
		if ( timertask.future != null )
			timertask.future.cancel(false);
		if ( fileWatchdog.future!=null )
			fileWatchdog.future.cancel(false);
		managerRPC.destroy();
		logger.info("fs main executor shutdown start");
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
		logger.info("fs main executor shutdown ok");
		db.close();
		logger.info("fs db close ok");
	}
	
	void start()
	{
		//
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
		fileWatchdog.future = executor.scheduleAtFixedRate(fileWatchdog, 1, 1, TimeUnit.MINUTES);
		managerRPC.start();
		logger.info("fs start");
	}
	
	Logger getLogger()
	{
		return logger;
	}
	
	RPCManagerFriendServer getRPCManager()
	{
		return managerRPC;
	}
	
	DB getDB()
	{
		return db;
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
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		FriendServer fs = new FriendServer(new File(am.get("-cfgfile", "fs.cfg")));
		PropertyConfigurator.configure(fs.getConfig().log4jCfgFileName);
		fs.init();
		if( am.containsKey("bg") )
		{
			fs.initSignal();
		}
		else
		{
			ket.util.FileSys.pauseWaitInput();
		}
		fs.destroy();
	}

	DB db;
	private volatile boolean bSingalHandled = false;
	private CountDownLatch latch = new CountDownLatch(1);
	private RPCManagerFriendServer managerRPC;
	private DataManager managerData;
	private Logger logger = Logger.getLogger("fsLogger");
	Config cfg = new Config();
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	private final FileWatchdog fileWatchdog = new FileWatchdog();
}

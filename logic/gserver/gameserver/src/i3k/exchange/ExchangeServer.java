
package i3k.exchange;

import i3k.gs.GameData;

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

public class ExchangeServer implements SignalHandler
{

	public static class Config
	{
		public NetAddress addrListen = new NetAddress("0.0.0.0", 5106);
		public String dbcfgfile = "./dbexchange/dbcfg.xml";
		
		public int id = 1;
		
		public String log4jCfgFileName = "es.log4j.properties";
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			managerRPC.onTimer();
			managerDuel.onTimer();
			managerExpiratBoss.onTimer();
			managerHeroesBoss.onTimer();
			managerSWar.onTimer();
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
	
	public ExchangeServer(File configFile)
	{
		setConfig(configFile);
		fileWatchdog.setFile(cfg.log4jCfgFileName);
		managerData = new DataManager(this);
		managerDuel = new DuelMatchManager(this);
		managerRPC = new RPCManagerExchangeServer(this);
		managerExpiratBoss = new ExpiratBossManager(this);
		managerHeroesBoss = new HeroesBossManager(this);
		managerSWar = new ForceSWarManager(this);
	}
	
	public DataManager getDataManager()
	{
		return managerData;
	}
	
	public DuelMatchManager getDuelManager()
	{
		return managerDuel;
	}
	
	public ForceSWarManager getSWarManager()
	{
		return managerSWar;
	}
	
	public ExpiratBossManager getExpiratBossManager()
	{
		return managerExpiratBoss;
	}
	
	public HeroesBossManager getHeroesBossManager()
	{
		return managerHeroesBoss;
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
		
		cfg.addrListen.host = skv.getString("ExchangeServer", "host", cfg.addrListen.host);
		cfg.addrListen.port = skv.getInteger("ExchangeServer", "port", cfg.addrListen.port);
		cfg.id = skv.getInteger("ExchangeServer", "id", cfg.id);
		
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
		try {
			int randomServer = managerRPC.table.getRandomServer();
			managerHeroesBoss.sendSyncHeroesInfoRes(randomServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( timertask.future != null )
			timertask.future.cancel(false);
		if ( fileWatchdog.future!=null )
			fileWatchdog.future.cancel(false);
		managerRPC.destroy();
		logger.info("es main executor shutdown start" );
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
		logger.info("es main executor shutdown ok");
		db.close();
		logger.info("es db close ok");
	}
	
	void start()
	{
		//
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
		fileWatchdog.future = executor.scheduleAtFixedRate(fileWatchdog, 1, 1, TimeUnit.MINUTES);
		managerRPC.start();
		logger.info("es start");
	}
	
	Logger getLogger()
	{
		return logger;
	}
	
	RPCManagerExchangeServer getRPCManager()
	{
		return managerRPC;
	}
	
	DB getDB()
	{
		return db;
	}

	public int getTime()
	{
		return GameData.getTime();
	}
	
	public int getWeekday(int day)
	{
		return (day-3)%7;
	}
	
	short getDayByOffset(short o)
	{
		return (short)((getTime() - o * 60) / 86400);
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
		ExchangeServer es = new ExchangeServer(new File(am.get("-cfgfile", "es.cfg")));
		PropertyConfigurator.configure(es.getConfig().log4jCfgFileName);
		es.init();
		if( am.containsKey("bg") )
		{
			es.initSignal();
		}
		else
		{
			ket.util.FileSys.pauseWaitInput();
		}
		es.destroy();
	}

	DB db;
	private volatile boolean bSingalHandled = false;
	private CountDownLatch latch = new CountDownLatch(1);
	private RPCManagerExchangeServer managerRPC;
	private DataManager managerData;
	private DuelMatchManager managerDuel;
	private ForceSWarManager managerSWar;
	private ExpiratBossManager managerExpiratBoss; 
	private HeroesBossManager managerHeroesBoss;
	private Logger logger = Logger.getLogger("esLogger");
	Config cfg = new Config();
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	private final FileWatchdog fileWatchdog = new FileWatchdog();
}

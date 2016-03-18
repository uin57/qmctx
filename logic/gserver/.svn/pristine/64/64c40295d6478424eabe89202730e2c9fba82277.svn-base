
package i3k.global;

import i3k.DBSocialUser;
import i3k.DBSocialMsg;
import i3k.SBean;
import i3k.gs.GameData;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
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
import ket.kdb.TableEntry;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.MD5Digester;
import ket.util.SKVMap;
import ket.util.Stream;

@SuppressWarnings("restriction")
public class GlobalServer implements SignalHandler
{

	public static class Config
	{
		public NetAddress addrListen = new NetAddress("0.0.0.0", 1107);
		//public String midasURL = "http://127.0.0.1";
		public String dbcfgfile = "./dbglobal/dbcfg.xml";
		public int nIOThread = 1;
		
		public int id = 1;
		public int cap = 8000;
		
		public String log4jCfgFileName = "gls.log4j.properties";
		public String serverCfgFileName = "dirty_words_cfg.dat";
		
		public int trustAllGS = 0;
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				managerRPC.onTimer();
				socialManager.onTimer(System.currentTimeMillis(), getTime());
				onTimer(getTime());	
			}
			catch(Throwable t)
			{
				logger.error("Uncaughted exception[" + t.getMessage()
						+ "], throwed by timer thread", t);
				System.exit(0);
			}
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	private void onTimer(int timeTick)
	{
		
		if ( timeTick % 600 == 0 )
		{
			//logger.warn("midas queue size is(" + midas.getHTTPTaskQueueSize() + "," + midas.getCallbackTaskQueueSize() + ")");
		}
		
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
				  if(l != lastModif) // however, if we reached this point this
				  {
					  lastModif = l;              // is very unlikely.
					  fileChangeHandler.run();
				  }
			  }
			  else
			  {
				  getLogger().info("FileWatchdog: file("+file+") is not exist!");
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
	
//	public class SocialDataInitTrans implements Transaction
//	{	
//		public SocialDataInitTrans()
//		{
//		}
//
//		@Override
//		public boolean doTransaction()
//		{	
//			socialManager.setUsers(users);
//			socialManager.setMsgs(msgs);
// 
//			return true;
//		}
//
//		@Override
//		public void onCallback(ErrorCode errcode)
//		{
//			if( errcode == ErrorCode.eOK )
//			{
//				start();
//			}
//			else
//			{
//				logger.error("social data init failed");
//			}
//		}
//		
//		@AutoInit
//		public TableReadonly<String, Long> maxids;
//		@AutoInit
//		public TableReadonly<Long, DBSocialUser> users;
//		@AutoInit
//		public TableReadonly<Long, DBSocialMsg> msgs;
//	
//	}
	
	public GlobalServer(File fileCfg)	
	{
		setConfig(fileCfg);
		managerRPC = new RPCManager(this);
		socialManager = new SocialManager(this);
	}
	
	public Config getConfig()
	{
		return cfg;
	}
	
	private void setConfig(File configFile)
	{
		if( configFile == null )
			return;
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;		
		
		cfg.nIOThread = skv.getInteger("KIO", "ioThreadCount", cfg.nIOThread);
		
		cfg.dbcfgfile = skv.getString("KDB", "cfgFile", cfg.dbcfgfile);
		
		cfg.log4jCfgFileName = skv.getString("Log4j", "cfgFile", cfg.log4jCfgFileName);
		PropertyConfigurator.configure(cfg.log4jCfgFileName);
		//fileWatchdog.setFile(cfg.log4jCfgFileName);
		monitorLog4jConfiguerFile(cfg.log4jCfgFileName);
		
		cfg.serverCfgFileName = skv.getString("ServerCfg", "cfgFile", cfg.serverCfgFileName);
		
		cfg.trustAllGS = skv.getInteger("TrustGS", "trustAll", cfg.trustAllGS);
		
		cfg.addrListen.host = skv.getString("GlobalServer", "host", cfg.addrListen.host);
		cfg.addrListen.port = skv.getInteger("GlobalServer", "port", cfg.addrListen.port);
		
		
		cfg.id = skv.getInteger("GlobalServer", "id", cfg.id);
		cfg.cap = skv.getInteger("GlobalServer", "cap", cfg.cap);
		
	
		logger.info("kio thread count is " + cfg.nIOThread);
	}
	
	public int getMinuteOfDay()
	{
		return (getTime()%86400)/60;
	}
	
	public int getSecsOfWeek()
	{
		int now = getTime();
		int w = (now/86400-3)%7;
		return w * 86400 + now % 86400; 
	}
	
	public String getTimeStampStr()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
	}
	
	public static String getTimeStampStr(int time)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date((long)(time-8*3600)*1000));
	}
	
	public static int getTime()
	{
		// TODO
		return GameData.getInstance().getTime();
	}
	
	public int getTimeByMinuteOffset(short m)
	{
		return getTimeH0() + m * 60;
	}
	
	public int getTimeGMT()
	{
		// TODO
		return (int)(new Date().getTime()/1000);
	}
	
	public int getTimeH0()
	{
		int t = getTime();
		return t - t % 86400;
	}
	
	public byte[] getChallengeKey(byte[] keyRand, byte[] arg)
	{
		// TODO
		byte[] data = new byte[keyRand.length + 4 + arg.length];
		System.arraycopy(keyRand, 0, data, 0, keyRand.length);
		System.arraycopy(arg, 0, data, keyRand.length + 4, arg.length);
		data[keyRand.length] = (byte)1;
		data[keyRand.length+1] = (byte)2;
		data[keyRand.length+2] = (byte)0;
		data[keyRand.length+3] = (byte)3;
		return new MD5Digester().digest(data, 0, data.length);
	}
	
	public static int getDay()
	{
		return getTime() / 86400;
	}
	

	// return 0~6 0:=sunday
	public static int getWeekday()
	{
		return (getDay()-3)%7;
	}
	
	public boolean isWeekend()
	{
		return getWeekday() == 0 || getWeekday() == 6;
	}
	
	static int getDay(int t)
	{
		return t / 86400;
	}
	
	static short getDayByOffset(short o)
	{
		return (short)((getTime() - o * 60) / 86400);
	}
	
	static short getDayByOffsetRandAdjust(short o, int secs)
	{
		int now = getTime() + secs;
		return (short)((now - o * 60) / 86400);
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
		
		socialManager.initReadDB();
	}
	
	void destroy()
	{
		if ( timertask.future != null )
			timertask.future.cancel(false);
//		if ( fileWatchdog.future!=null )
//			fileWatchdog.future.cancel(false);
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
		socialManager.saveData();
		db.close();
		logger.info("db close ok");
	}
	
	void start()
	{
		//
		logger.info("gls start...");
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
		//fileWatchdog.future = executor.scheduleAtFixedRate(fileWatchdog, 1, 1, TimeUnit.MINUTES);
		managerRPC.start();
		logger.info("gls start success");
	}
	
	Logger getLogger()
	{
		return logger;
	}
	
	
	RPCManager getRPCManager()
	{
		return managerRPC;
	}
	
	SocialManager getSocialManager()
	{
		return socialManager;
	}
	
	DB getDB()
	{
		return db;
	}

	boolean loadGameData(String filePath)
	{
		try
		{
			SBean.QQDirtyWordCFGS gamedata = new SBean.QQDirtyWordCFGS();
			if( ! Stream.loadObjLE(gamedata, new File(filePath)) )
				return false;
			dirtyWordsCfg = gamedata;
			logger.info("读取 gamedata 成功");
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean isQQDirtyWords(String name)
	{
		for(String e : dirtyWordsCfg.wordsICase)
		{
			if( name.toLowerCase().indexOf(e.toLowerCase()) >= 0 )
			{
				return true;
			}
		}
		for(String e : dirtyWordsCfg.words)
		{
			if( name.indexOf(e) >= 0 )
			{
				return true;
			}
		}
		for(SBean.QQDirtyWordCatCFGS c : dirtyWordsCfg.cats)
		{
			if( name.indexOf(c.key) >= 0 )
			{
				for(String e : c.words)
				{
					if( name.indexOf(e) >= 0 )
					{
						return true;
					}
				}
			}
		}
		return false;
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
	
	public void execute(Runnable runnable)
	{
		executor.execute(runnable);
	}
	
	public static void main(String[] args)
	{
		
		ArgsMap am = new ArgsMap(args);
		GlobalServer gs = new GlobalServer(new File(am.get("-cfgfile", "gls.cfg")));
		String serverCfgFilePath = gs.getConfig().serverCfgFileName;
		if( ! gs.loadGameData(serverCfgFilePath) )
		{
			System.out.println("load game data failed from " + serverCfgFilePath + ".");
			return;
		}
		
		gs.init();
		if( am.containsKey("bg") )
		{
			gs.initSignal();
		}
		else
		{
			ket.util.FileSys.pauseWaitInput();
		}
		gs.destroy();
	}

	DB db;
	private volatile boolean bSingalHandled = false;
	private CountDownLatch latch = new CountDownLatch(1);
	private RPCManager managerRPC;
	private SocialManager socialManager;
	private Logger logger = Logger.getLogger("glsLogger");
	Config cfg = new Config();
	SBean.QQDirtyWordCFGS dirtyWordsCfg;
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	//private final FileWatchdog fileWatchdog = new FileWatchdog();
	private Map<String, FileWatchdog> fileWatchdogs = new TreeMap<String, FileWatchdog>();
}

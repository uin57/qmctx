
package i3k.gs.test;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.SKVMap;
import ket.util.Stream;
import i3k.SBean;
import i3k.gs.GameData;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GameClient
{

	public static class Config
	{
		public NetAddress addrServer = new NetAddress("0.0.0.0", 1107);
		public int nClient = 32;
		public int reconnectInterval = 0;
		public int bTestTask = 0;
		public int bTestCombat = 0;
		
		public int nLoginBatchSize = 200;
		public int nLoginBatch = 20;
		
		public int robotIDStart = 0;
		
		public int useOpenKey = 0;
	}
	
	public static class Stat
	{
		public AtomicInteger nLogin = new AtomicInteger(0);
		public AtomicInteger nLoginOK = new AtomicInteger(0);
		public AtomicInteger nLoginErr = new AtomicInteger(0);
		
		public AtomicInteger nCombatStart = new AtomicInteger(0);
		public AtomicInteger nCombatStartOK = new AtomicInteger(0);
		public AtomicInteger nCombatStartErr = new AtomicInteger(0);
		
		public AtomicInteger nCombatFinish = new AtomicInteger(0);
		public AtomicInteger nCombatFinishOK = new AtomicInteger(0);
		public AtomicInteger nCombatFinishErr = new AtomicInteger(0);
		
		public AtomicInteger nDoConnect = new AtomicInteger(0);
		public AtomicInteger nReconnect = new AtomicInteger(0);
		public AtomicInteger nConnected = new AtomicInteger(0);
		public AtomicInteger nConnectFailed = new AtomicInteger(0);
		public AtomicInteger nClose = new AtomicInteger(0);
		
		public AtomicInteger nReconnectS = new AtomicInteger(0);
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			++tick;
			logger.info(tick + " STAT LOGIN: TOTAL=" + stat.nLogin.get() + ", SUCCESS=" + stat.nLoginOK.get() + ", ERROR=" + stat.nLoginErr.get()
					+ "\n"
					+ "      COMBATSTART: TOTAL=" + stat.nCombatStart.get() + ", SUCCESS=" + stat.nCombatStartOK.get() + ", ERROR=" + stat.nCombatStartErr.get()
					+ "\n"
					+ "      COMBATFINISH: TOTAL=" + stat.nCombatFinish.get() + ", SUCCESS=" + stat.nCombatFinishOK.get() + ", ERROR=" + stat.nCombatFinishErr.get()
					+ "\n"
					+ "      DOCONNECT=" + stat.nDoConnect.get() + "(R=" + stat.nReconnect.get() + "," + stat.nReconnectS+"), OK="
					+ stat.nConnected + ", CLOSE=" + stat.nClose + ", FAIL=" + stat.nConnectFailed
					+ "\n"
					);
			
			
			if( loginBatch.incrementAndGet() <= cfg.nLoginBatch )
			{
				for(int i = 0; i < cfg.nLoginBatchSize; ++i)
				{
					managerRPC.addRobot();
				}
			}
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	public GameClient()
	{
		managerRPC = new RPCManagerClient(this);
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
	}
	
	public void setConfig(Config config)
	{
		if( config != null )
			cfg = config;
	}
	
	public Config getConfig()
	{
		return cfg;
	}
	
	public void log(String msg)
	{
		logger.info(msg);
	}
	
	public void setConfig(File configFile)
	{
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;
		
		String strGameServerHost = skv.getString("GameServer", "host");
		if( strGameServerHost != null )
			cfg.addrServer.host = strGameServerHost;
		Integer iGameServerPort = skv.getInteger("GameServer", "port");
		if( iGameServerPort != null )
			cfg.addrServer.port = iGameServerPort.intValue();		
		
		cfg.nClient = skv.getInteger("Robot", "nClient", cfg.nClient);
		cfg.bTestTask = skv.getInteger("Robot", "testTask", cfg.bTestTask);
		cfg.bTestCombat = skv.getInteger("Robot", "testCombat", cfg.bTestCombat);
		cfg.reconnectInterval = skv.getInteger("Robot", "reconnectInterval", cfg.reconnectInterval);
		
		cfg.nLoginBatchSize = skv.getInteger("Robot", "nLoginBatchSize", cfg.nLoginBatchSize);
		cfg.nLoginBatch = skv.getInteger("Robot", "nLoginBatch", cfg.nLoginBatch);
		
		cfg.useOpenKey = skv.getInteger("Robot", "useOpenKey", cfg.useOpenKey);
		
		cfg.robotIDStart = skv.getInteger("Robot", "robotIDStart", cfg.robotIDStart);
		
		robotIDSeed.set(cfg.robotIDStart);
	}
	
	public void logStatLogin()
	{
		stat.nLogin.incrementAndGet();
	}
	
	public void logStatLogin(boolean bOK)
	{
		if( bOK )
			stat.nLoginOK.incrementAndGet();
		else
			stat.nLoginErr.incrementAndGet();
	}
	
	public void logStatCombatStart()
	{
		stat.nCombatStart.incrementAndGet();
	}
	
	public void logStatCombatStart(boolean bOK)
	{
		if( bOK )
			stat.nCombatStartOK.incrementAndGet();
		else
			stat.nCombatStartErr.incrementAndGet();
	}
	
	public void logStatCombatFinish()
	{
		stat.nCombatFinish.incrementAndGet();
	}
	
	public void logStatCombatFinish(boolean bOK)
	{
		if( bOK )
			stat.nCombatFinishOK.incrementAndGet();
		else
			stat.nCombatFinishErr.incrementAndGet();
	}
	
	public void logStatDoConnect(boolean bReconnect)
	{
		if( bReconnect )
			stat.nReconnect.incrementAndGet();
		stat.nDoConnect.incrementAndGet();
	}
	
	public void logStatDoConnectS()
	{
		stat.nReconnectS.incrementAndGet();
	}
	
	public void logStatConnected()
	{
		stat.nConnected.incrementAndGet();
	}
	
	public void logStatClose()
	{
		stat.nClose.incrementAndGet();
	}
	
	public void logStatConnectFailed()
	{
		stat.nConnectFailed.incrementAndGet();
	}
	
	public static int getTime()
	{
		// TODO
		return (int)(new java.util.Date().getTime()/1000) + (3600*8);
	}
	
	int getDay()
	{
		return getTime() / 86400;
	}
	
	int getDay(int t)
	{
		return t / 86400;
	}
	
	void init()
	{
	}
	
	void destroy()
	{
		if ( timertask.future != null )
			timertask.future.cancel(false);
		managerRPC.destroy();
		executor.shutdown();
		System.out.println("after executor shutdown");
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
	}
	
	void start()
	{
		managerRPC.start();
	}
	
	RPCManagerClient getRPCManager()
	{
		return managerRPC;
	}
	
	public ScheduledExecutorService getExecutor()
	{
		return executor;
	}
	
	boolean loadGameData(String dirCData)
	{
		try
		{
			SBean.GameDataCFGS gamedata = new SBean.GameDataCFGS();
			if( ! Stream.loadObjLE(gamedata, new File(dirCData + File.separator + "server_cfg.dat")) )
				return false;
			gameData = new GameData(gamedata);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public GameData getGameData()
	{
		return gameData;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		GameClient gc = new GameClient();
		gc.setConfig(new File(am.get("-cfgfile", "gc.cfg")));
		PropertyConfigurator.configure("gc.log4j.properties");
		String dirCData = am.get("-cfgdir", ".");
		if( ! gc.loadGameData(dirCData) )
		{
			System.out.println("load game data failed from " + dirCData + ".");
			return;
		}
		gc.init();
		gc.start();
		FileSys.pauseWaitInput();
		System.out.println("start destroy");
		gc.destroy();
		System.out.println("after destroy");
	}
	
	private Logger logger = Logger.getLogger("gcLogger");
	private RPCManagerClient managerRPC;
	private GameData gameData;
	Config cfg = new Config();
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	private Stat stat = new Stat();
	private volatile int tick = 0;
	
	public AtomicInteger loginBatch = new AtomicInteger(0);
	AtomicInteger robotIDSeed = new AtomicInteger(0);
}

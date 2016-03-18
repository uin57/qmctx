
package i3k.gs;

import i3k.DBArena;
import i3k.SBean;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ket.kdb.DB;
import ket.kdb.Table;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.MD5Digester;
import ket.util.SKVMap;
import ket.util.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class GameServer implements SignalHandler
{

	public static class Config
	{
		public NetAddress addrListen = new NetAddress("0.0.0.0", 1107);
		public NetAddress addrControl = new NetAddress("127.0.0.1", 10906);
		public NetAddress addrTLog = new NetAddress("115.28.16.3", 6667);
		public NetAddress addrExchange = new NetAddress("127.0.0.1", 5106);
		public NetAddress addrFriend = new NetAddress("127.0.0.1", 8106);
		public NetAddress addrIDIP = new NetAddress("127.0.0.1", 4016);
		public NetAddress addrWebService = new NetAddress("127.0.0.1", 8080);
		//public String midasURL = "http://opensdktest.tencent.com";
		public String midasURL = "http://msdktest.qq.com";
		//public String midasURL = "http://127.0.0.1";
		
		public String msdkURL = "http://msdktest.qq.com";
		public int msdkLoginVerify = 0;
		public String dbcfgfile = "./db/dbcfg.xml";
		public int nIOThread = 1;
		public short roleLevelLimit = 100;
		public short godModeRoleLevel = roleLevelLimit;
		public short godModeGeneralLevel = roleLevelLimit;
		public byte godModeGeneralFullSkillLevel = 0;
		public byte godModeGeneralAdvLevel = -1;
		public byte godModeGeneralEvoLevel = -1;
		public byte godModeGeneralGildLevel = -1;
		public byte godModeGeneralWeaponEvo = -1;
		public byte godModeGeneralWeaponLevel = -1;
		public byte godModeGeneralWeaponReset = -1;
		public int  godModeGeneralSeyen = -1;
		public short  godModeGeneralOfficialLvl = -1;
		public short  godModeGeneralOfficialSLvl = -1;
		
		public short godModePetLevel = roleLevelLimit;
		public byte godModePetEvoLevel = -1;
		
		public int godModeMoney = 9999999;
		public int godModeStone = 9999999;
		public int godModeSkillPoint = 30000;
		public int godModeArenaPoint = 9999999;
		public int godModeMarchPoint = 9999999;
		public int godModeForcePoint = 9999999;
		public int godModeRichPoint = 9999999;
		public int godModeJunzhengSkillPoint = 20;
		public byte godModeCombatPosNormalCIndex = -1;
		public byte godModeCombatPosNormalBIndex = -1;
		public byte godModeCombatPosHeroCIndex = -1;
		public byte godModeCombatPosHeroBIndex = -1;
		public int godModeRelation = 0;
		public int godModeSeyen = 0;
		public int godModeStone1 = 0;
		public int godMode = 0;
		public String allowOpenID = "";
		
		public List<Integer> newRoleGenerals;
		public List<Integer> newRoleEquips;
		public List<Integer> newRoleItems;
		public int newRoleCombatStage = 0;
		public int newRoleMoney = 0;
		public int newRoleYuanbao = 0;
		public int verMajor = 0;
		public int verMinor = 0;
		public String gameappID = "1000001472";
		public String gameappKey = "mgrRft1pwPOx4EpD";
		public String gameappIDGuest = "G_1000001472";
		public String gameappKeyGuest = "mgrRft1pwPOx4EpD";
		public String offerID = "1450001174";
		public String offerKey = "25er5nL2aemDiaBL2WOEpDW9kUDhLMh6";
		public String offerCookieSID = "openid";
		public String offerCookieSType = "kp_actoken";
		public String offerCookieSIDGuest = "hy_gameid";
		public String offerCookieSTypeGuest = "st_dummy";
		public int payState = 0;
		
		public byte areaID = 0;
		public byte platID = 0;
		public String channelID = "";
		public int id = 1;
		public int cap = 8000;
		public int sps = 1000;
		public int pps = 50;
		public int zoneID = 1; // TODO
		public String payDiscountID = "UM140904185153179"; // TODO
		public String payGiftID = "2751024175PID201409041851531928"; // TODO
		
		public int arenaVerifyFlag = 1;
		public int luaChannelVerifyFlag = 1;
		public int challengeFlag = 1;
		public String challengeFuncArg = "abcd1234efgh5678";
		public int qqReportScore = 1;
		public int httpThreadCount = 4;
		public int httpCallbackThreadCount = 1;
		public int httpTaskMaxCountL1 = 30;
		public int httpTaskMaxCountL2 = 5;
		
		public int forceWarVerifyFlag = 2;
		
		public String log4jCfgFileName = "gs.log4j.properties";
		public String activitiesDir = "activities";
		public String serverCfgFileName = "server_cfg.dat";
		public String whitelistFileName = "login.allow.cfg";
		public String registerlimitFileName = "register.limit.cfg";
		
		public int tssAntiState = 0;
		public int tssAntiUID = 1;
		public String tssAntiCfgFileName = "tss_sdk_conf.cfg";
		
		public int iosExamineServer = 0;
		public String arenaDebugFile = "";
		public int arenaDebugRank = 0;
		
		public int getServerState()
		{
			return iosExamineServer;
		}
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				hotSwap.onTimer();
				managerLogin.onTimer(System.currentTimeMillis(), getTime());
				managerArena.onTimer();
				managerMarch.onTimer();
				managerWar.onTimer();
				managerExchange.onTimer();
				managerRPC.onTimer();
				gameService.onTimer();
				managerCity.onTimer(getTime());
				managerRich.onTimer();
				managerZan.onTimer();
				managerForce.onTimer();
				managerDuel.onTimer();
				managerSura.onTimer();
				managerSWar.onTimer();
				managerDisk.onTimer();
				managerDisk2.onTimer();
				managerExpiratBoss.onTimer();
				managerHeroesBoss.onTimer();
				ipad.onTimer();
				managerTreasureMap.onTimer();
				managerBeMonster.onTimer();
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
	
	private class Timer100Task implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				managerAnti.onTimer();
			}
			catch(Throwable t)
			{
				logger.warn("Uncaughted exception[" + t.getMessage()
					+ "] in TssAntiTimer100, throwed by timer thread", t);
				throw t;
			}
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	private void onTimer(int timeTick)
	{
		//tlogÿ���ӷ���һ��������־
		if ( timeTick % 60 == 0 )
		{
			tlogger.logGameServerState();
		}
		
		if ( timeTick % 60 == 0 )
		{
			int q1 = midas.getHTTPTaskQueueSize();
			int q2 = midas.getCallbackTaskQueueSize();
			int r1 = midas.getHTTPRejectedTaskQueueSize();
			int r2 = midas.getCallbackRejectedTaskQueueSize();
			if( q1 >= 100 || q2 >= 100 || r1 > 0 || r2 > 0 )
				logger.warn("midas queue size is(q1=" + q1 + ",q2=" + q2 + ",r1=" + r1 + ",r2=" + r2 + ")");
			else
				logger.debug("midas queue size is(q1=" + q1 + ",q2=" + q2 + ",r1=" + r1 + ",r2=" + r2 + ")");
		}
		
		//ÿ���ӵ�30����
		if ( (timeTick + 30) % 60 == 0 )
		{
			for (Map.Entry<String, FileWatchdog> e : fileWatchdogs.entrySet())
			{
				e.getValue().run();
			}
		}
	}
	
	public class IPad
	{
		// TODO
		public short getRandItemID()
		{
			return GameData.getInstance().getIPadCFG().itemIDRand;
		}
		
		public short getIPadItemID()
		{
			SBean.IPadCFGS icfg = GameData.getInstance().getIPadCFG();
			for(SBean.IPadAreaCFGS e : icfg.areas)
			{
				if( e.gsID == cfg.id )
					return e.itemID;
			}
			return -1;
		}
		
		public int getCheatTime()
		{
			SBean.IPadCFGS icfg = GameData.getInstance().getIPadCFG();
			for(SBean.IPadAreaCFGS e : icfg.areas)
			{
				if( e.gsID == cfg.id )
					return e.timeCheat;
			}
			return 86400;
		}
		
		public boolean isPadServer()
		{
			return getRandItemID() > 0 && getIPadItemID() > 0;
		}
		
		public String getWorldKey()
		{
			return "padv2" + getRandItemID();
		}
		
		public void initCount(int count)
		{
			bTake = count > 0;
		}
		
		public SBean.DropEntry testDrop(short randItemID)
		{
			SBean.IPadCFGS icfg = GameData.getInstance().getIPadCFG();
			if( icfg.state == 0 )
				return null;
			int now = getTime();
			if( now <= icfg.timeStart || now >= icfg.timeEnd )
				return null;
			if( ! isPadServer() )
				return null;
			if( getRandItemID() != randItemID )
				return null;
			synchronized( this )
			{
				if( bTake )
					return null;
				short itemID = getIPadItemID();
				if( itemID <= 0 )
					return null;
				
				float prop = icfg.prop;
				//
				int last = getCheatTime();
				//
				if( icfg.timeEnd - now < last )
				{
					prop = 1 - (icfg.timeEnd - now) / (float)last;
				}
				
				if( GameData.getInstance().getRandom().nextFloat() < prop )
				{
					bTake = true;
					bSaveOK = false;
					return new SBean.DropEntry(GameData.COMMON_TYPE_ITEM, (byte)1, itemID);
				}
			}
			return null;
		}
		
		public synchronized void setSaveOK()
		{
			bSaveOK = true;
		}
		
		public void onTimer()
		{
			{
				int now = getTime();
				SBean.IPadCFGS icfg = GameData.getInstance().getIPadCFG();
				if( icfg.state == 1 && now >= icfg.timeStart && now < icfg.timeEnd
						&& isPadServer() )
				{
					logger.debug("pad server: id=" + cfg.id + ",state=" + icfg.state + ",btake="+ bTake + ",saveok=" + bSaveOK
							+ ",randID=" + getRandItemID() + ",itemID=" + getIPadItemID());
				}
			}
			synchronized( this )
			{
				if( ! bTake )
					return;
				if( bSaveOK )
					return;
			}
			doSave();
		}
		
		public class SaveTrans implements Transaction
		{
			@Override
			public boolean doTransaction()
			{
				byte[] key = Stream.encodeStringLE(getWorldKey());
				byte[] data = Stream.encodeIntegerLE(1);
				world.put(key, data);
				return true;
			}

			@AutoInit
			public Table<byte[], byte[]> world;
			
			@Override
			public void onCallback(ErrorCode errcode)
			{
				if( errcode == ErrorCode.eOK )
				{
					setSaveOK();
					getLogger().warn("ipad save ok");
				}
			}
		}
		
		private void doSave()
		{
			db.execute(new SaveTrans());
		}
		//
		
		private boolean bTake = false;
		private boolean bSaveOK = false;
	}
	
	public void mointorFileChanged(String filename, Runnable dochange)
	{
		fileWatchdogs.put(filename, new FileWatchdog(filename, dochange));
	}
	
	private class FileWatchdog implements Runnable
	{ 
		  //protected String filename;
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
			  //this.filename = filename;
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
	
	private void monitorGameServerConfiguerFile(final String filename)
	{
		mointorFileChanged(filename, new Runnable()
				{
					boolean firstLoad = true;
					@Override
					public void run()
					{
						if (firstLoad)
						{
							setConfig(filename);
							firstLoad = false;
						}
						else
						{
							reloadConfig(filename);
						}
					}
				});
	}
	
	private void monitorGameDataConfiguerFile(final String filename)
	{
		mointorFileChanged(filename, new Runnable()
				{
					boolean firstLoad = true;
					@Override
					public void run()
					{
						if (firstLoad)
						{
							firstLoad = false;
						}
						else
						{
							reloadGameData(filename);
						}
					}
				});
	}
	
	public class WorldInitTrans implements Transaction
	{	
		public WorldInitTrans()
		{
		}

		@Override
		public boolean doTransaction()
		{	
			try
			{
				{
					byte[] key = Stream.encodeStringLE("jubaoDay");
					byte[] data = world.get(key);
					if( data == null )
					{
						int jday = getDay();
						byte[] newdata = Stream.encodeIntegerLE(jday);
						world.put(key, newdata);
						getLoginManager().setJubaoDay(jday);
						logger.info("write jubao day = " + jday);
					}
					else
					{
						int jday = Stream.decodeIntegerLE(data);
						getLoginManager().setJubaoDay(jday);
						logger.info("read jubao day = " + jday);
					}
				}
				{
					byte[] key = Stream.encodeStringLE("worldMails");
					byte[] data = world.get(key);
					if( data == null )
					{
						logger.info("load world mails: 0");
					}
					else
					{
						List<SBean.SysMail> lst = Stream.decodeListLE(SBean.SysMail.class, data);
						getLoginManager().setWorldMails(lst);
						logger.info("load world mails: " + lst.size());
					}
				}
				{
					byte[] key = Stream.encodeStringLE("arena");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerArena.init(null);
					}
					else
					{
						DBArena arena = new DBArena(); 
						Stream.decodeLE(arena, data);
						managerArena.init(arena);
					}
					logger.info("load arena ok");
				}
				{
					byte[] key = Stream.encodeStringLE("superarena");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerArena.getSuperArena().setDBData(null);
					}
					else
					{
						SBean.DBSuperArena arena = new SBean.DBSuperArena(); 
						Stream.decodeLE(arena, data);
						managerArena.getSuperArena().setDBData(arena);
					}
					logger.info("load super arena ok");
				}
				{
					byte[] key = Stream.encodeStringLE("powerRanks");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerMarch.init(null);
					}
					else
					{
						SBean.DBPowerRankList rankList = new SBean.DBPowerRankList(); 
						Stream.decodeLE(rankList, data);
						managerMarch.init(rankList);
					}
					logger.info("load power rank ok");
				}
//				{
//					byte[] key = Stream.encodeStringLE("delayMails");
//					byte[] data = world.get(key);
//					if( data == null )
//					{
//						logger.info("load dalay mails: 0");
//					}
//					else
//					{
//						List<SBean.SysMail> lst = Stream.decodeListLE(SBean.SysMail.class, data);
//						getLoginManager().setDelayMails(lst);
//						logger.info("load delay mails: " + lst.size());
//					}
//				}
				{
					byte[] key = Stream.encodeStringLE("broadcasts");
					byte[] data = world.get(key);
					if( data == null )
					{
						logger.info("load broadcasts: 0");
					}
					else
					{
						List<SBean.Broadcast> lst = Stream.decodeListLE(SBean.Broadcast.class, data);
						getLoginManager().setBroadcasts(lst);
						logger.info("load braodcasts: " + lst.size());
					}
				}
				{
					byte[] key = Stream.encodeStringLE("cdkeys");
					byte[] data = world.get(key);
					if( data == null )
					{
						logger.info("load cdkeys: 0");
					}
					else
					{
						List<SBean.CDKeyLog> lst = Stream.decodeListLE(SBean.CDKeyLog.class, data);
						getLoginManager().setCDKeyUsedInfo(lst);
						logger.info("load cdkeys: " + lst.size());
					}
				}
				
				{
					Integer regNum = maxids.get("roleid");
					int iRegNum = regNum == null ? 0 : regNum.intValue();
					getLoginManager().setRegNum(iRegNum);
				}
				{
					byte[] key = Stream.encodeStringLE("forceWar");
					byte[] data = world.get(key);
					byte[] key2 = Stream.encodeStringLE("forceThief");
					byte[] data2 = world.get(key2);
					byte[] key3 = Stream.encodeStringLE("forceBeast2");
					byte[] data3 = world.get(key3);
					byte[] keynew = Stream.encodeStringLE("forceWar2");
					byte[] datanew = world.get(keynew);
					if( data == null && datanew == null )
					{
						managerWar.init(null, null, null);
					}
					else
					{
						SBean.DBForceWar war = null;
						if (datanew == null) {
							SBean.DBForceWarOld warOld = new SBean.DBForceWarOld(); 
							Stream.decodeLE(warOld, data);
							war = ForceWarManager.convertNewData(warOld);
						}
						else {
							war = new SBean.DBForceWar();
							Stream.decodeLE(war, datanew);
						}
						SBean.DBForceThief thief = null;
						if (data2 != null) {
							thief = new SBean.DBForceThief(); 
							Stream.decodeLE(thief, data2);
						}
						SBean.DBForceWarBeast beast = null;
						if (data3 != null) {
							beast = new SBean.DBForceWarBeast(); 
							Stream.decodeLE(beast, data3);
						}
						managerWar.init(war, thief, beast);
					}
					logger.info("load force war ok");
				}
				{
					byte[] key = Stream.encodeStringLE("rich2");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerRich.init(null);
					}
					else
					{
						SBean.DBRich rich = new SBean.DBRich(); 
						Stream.decodeLE(rich, data);
						managerRich.init(rich);
					}
					logger.info("load rich ok");
				}
				{
					byte[] key = Stream.encodeStringLE("petzan");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerZan.init(null);
					}
					else
					{
						List<SBean.DBPetZan> list = Stream.decodeListLE(SBean.DBPetZan.class, data);
						managerZan.init(list);
					}
					logger.info("load pet zan ok");
				}
//				{
//					byte[] key = Stream.encodeStringLE("levellimit");
//					byte[] data = world.get(key);
//					if ( data == null )
//					{
//						getLoginManager().getDynamicLevelLimit().init(null);
//						logger.info("load dynamic level limit 0");
//					}
//					else
//					{
//						List<SBean.LevelLimit> list = Stream.decodeListLE(SBean.LevelLimit.class, data);
//						getLoginManager().getDynamicLevelLimit().init(list);
//						logger.info("load dynamic level limit " + list.size());
//					}
//				}
				{
					byte[] key = Stream.encodeStringLE("toplevel");
					byte[] data = world.get(key);
					if ( data == null )
					{
						getLoginManager().getTopLevel().init(null);
						logger.info("load dynamic level limit 0");
					}
					else
					{
						SBean.TopLevelRoleInfo info = new SBean.TopLevelRoleInfo();
						Stream.decodeLE(info, data);
						getLoginManager().getTopLevel().init(info);
						logger.info("load top level " + info.level + " rid " + info.roleID);
					}
				}
				{
					byte[] key = Stream.encodeStringLE("dungeon");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerForce.initDungeon(null);
					}
					else
					{
						SBean.DBForceDungeonGlobal dungeon = new SBean.DBForceDungeonGlobal(); 
						Stream.decodeLE(dungeon, data);
						managerForce.initDungeon(dungeon);
					}
					logger.info("load dungeon ok");
				}
				{
					byte[] key = Stream.encodeStringLE("duel");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerDuel.init(null);
					}
					else
					{
						SBean.DBDuel duel = new SBean.DBDuel(); 
						Stream.decodeLE(duel, data);
						managerDuel.init(duel);
					}
					logger.info("load duel ok");
				}
				{
					byte[] key = Stream.encodeStringLE("expiratBoss");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerExpiratBoss.init(null);
					}
					else
					{
						SBean.DBExpiratBoss boss = new SBean.DBExpiratBoss(); 
						Stream.decodeLE(boss, data);
						managerExpiratBoss.init(boss);
					}
					logger.info("load expiratBoss ok");
				}{
					byte[] key = Stream.encodeStringLE("heroesBoss");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerHeroesBoss.init(null);
					}
					else
					{
						SBean.DBHeroesBoss boss = new SBean.DBHeroesBoss(); 
						Stream.decodeLE(boss, data);
						managerHeroesBoss.init(boss);
					}
					logger.info("load HeroesBoss ok");
				}
				{
					byte[] key = Stream.encodeStringLE("sura");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerSura.init(null);
					}
					else
					{
						SBean.DBSura sura = new SBean.DBSura(); 
						Stream.decodeLE(sura, data);
						managerSura.init(sura);
					}
					logger.info("load sura ok");
				}
				{
					byte[] key = Stream.encodeStringLE("diskBet");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerDisk.init(null);
					}
					else
					{
						SBean.DBDiskBet disk = new SBean.DBDiskBet(); 
						Stream.decodeLE(disk, data);
						managerDisk.init(disk);
					}
					logger.info("load DBDiskBet ok");
				}
				{
					byte[] key = Stream.encodeStringLE("diskBet2");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerDisk2.init(null);
					}
					else
					{
						SBean.DBDiskBet disk = new SBean.DBDiskBet(); 
						Stream.decodeLE(disk, data);
						managerDisk2.init(disk);
					}
					logger.info("load DBDiskBet2 ok");
				}
				{
					byte[] key = Stream.encodeStringLE("swar2");
					byte[] data = world.get(key);
					if( data == null )
					{
						managerSWar.init(null);
					}
					else
					{
						SBean.DBForceSWarGlobal swar = new SBean.DBForceSWarGlobal(); 
						Stream.decodeLE(swar, data);
						managerSWar.init(swar);
					}
					logger.info("load swar ok");
				}
				
				if( ipad.isPadServer() )
				{
					byte[] key = Stream.encodeStringLE(ipad.getWorldKey());
					byte[] data = world.get(key);
					if( data == null )
					{
						ipad.initCount(0);
						logger.warn("ipad init count null, set 0");
					}
					else
					{
						int count = Stream.decodeIntegerLE(data);
						ipad.initCount(count);
						logger.warn("ipad init count " + count);
					}
				}

				{
                    byte[] key = Stream.encodeStringLE("treasure");
                    byte[] data = world.get(key);
                    if( data == null )
                    {
                        managerTreasureMap.init(null);
                    }
                    else
                    {
                        SBean.DBTreasure treasure = new SBean.DBTreasure(); 
                        Stream.decodeLE(treasure, data);
                        managerTreasureMap.init(treasure);
                    }
                    logger.info("load treasure ok");
                }
				{
                    byte[] key = Stream.encodeStringLE("beMonster");
                    byte[] data = world.get(key);
                    if( data == null )
                    {
                        managerBeMonster.init(null);
                    }
                    else
                    {
                        SBean.DBBeMonster beMonster = new SBean.DBBeMonster(); 
                        Stream.decodeLE(beMonster, data);
                        managerBeMonster.init(beMonster);
                    }
                    logger.info("load beMonster ok");
                }
				return true;
			}
			catch(Stream.EOFException e)
			{
				return false;
			}
			catch(Stream.DecodeException e)
			{
				return false;
			}
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				start();
			}
			else
			{
				logger.error("world init failed");
				System.exit(0);
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
	
		@AutoInit
		public TableReadonly<String, Integer> maxids;
	}
	
	public GameServer(String fileCfg)	
	{
		monitorGameServerConfiguerFile(fileCfg);
		gameActivities = new GameActivities(this);
		managerRPC = new RPCManager(this);		
		managerLogin = new LoginManager(this);
		managerForce = new ForceManager(this);
		managerArena = new ArenaManager(this);
		managerExchange = new ExchangeManager(this);
		managerMarch = new MarchManager(this);
		managerWar = new ForceWarManager(this);
		managerAnti = new TSSAntiManager(this);
		managerCity = new CityManager(this);
		managerRich = new RichManager(this);
		managerZan = new PetZanManager(this);
		managerDuel = new DuelManager(this);
		managerSura = new SuraManager(this);
		managerSWar = new ForceSWarManager(this);
		managerExpiratBoss = new ExpiratBossManager(this);
		managerHeroesBoss =  new HeroesBossManager(this);
		managerTreasureMap = new TreasureMapManager(this);
		managerBeMonster = new BeMonsterManager(this);
		midas = new Midas(this);
		managerDisk = new DiskBetManager(this);
		managerDisk2 = new DiskBetManager2(this);
		gameService = new GameService(this);		
	}
	
	public Config getConfig()
	{
		return cfg;
	}
	
	public Map<String, String> getUserStateMap()
	{
		return userStateMap;
	}
	
	public String getUUIDWithGSID(byte payLvl)
	{
		return new Integer(cfg.id).toString() + "_" + new Byte(payLvl).toString() + "_" + UUID.randomUUID().toString();
	}
	
	public byte getPayLvlOfUUID(String uuid)
	{
		if( uuid == null )
			return (byte)1;
		String[] lst = uuid.split("_");
		if( lst.length <= 1 )
			return 1;
		return Byte.parseByte(lst[1]);
	}
	
	private void setConfig(String configFileName)
	{
		System.out.println("load gs config file : " + configFileName + " ......");
		File configFile = new File(configFileName);
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;		
		
		cfg.nIOThread = skv.getInteger("KIO", "ioThreadCount", cfg.nIOThread);
		
		cfg.dbcfgfile = skv.getString("KDB", "cfgFile", cfg.dbcfgfile);
		
		cfg.log4jCfgFileName = skv.getString("Log4j", "cfgFile", cfg.log4jCfgFileName);
		PropertyConfigurator.configure(cfg.log4jCfgFileName);
		//fileWatchdog.setFile(cfg.log4jCfgFileName);
		monitorLog4jConfiguerFile(cfg.log4jCfgFileName);
		
		cfg.activitiesDir = skv.getString("Activity", "cfgDir", cfg.activitiesDir);
		cfg.serverCfgFileName = skv.getString("ServerCfg", "cfgFile", cfg.serverCfgFileName);
		cfg.whitelistFileName = skv.getString("WhiteList", "cfgFile", cfg.whitelistFileName);
		cfg.registerlimitFileName = skv.getString("RegisterLimit", "cfgFile", cfg.registerlimitFileName);
		
		cfg.msdkLoginVerify = skv.getInteger("MSDK", "loginVerify", cfg.msdkLoginVerify);
		cfg.msdkURL = skv.getString("MSDK", "url", cfg.msdkURL);
		
		cfg.midasURL = skv.getString("Midas", "url", cfg.midasURL);
		cfg.payState = skv.getInteger("Midas", "payState", cfg.payState);
		
		cfg.offerID = skv.getString("Midas", "offerID", cfg.offerID);
		cfg.offerKey = skv.getString("Midas", "offerKey", cfg.offerKey);
		cfg.offerCookieSID = skv.getString("Midas", "offerCookieSID", cfg.offerCookieSID);
		cfg.offerCookieSType = skv.getString("Midas", "offerCookieSType", cfg.offerCookieSType);
		cfg.offerCookieSIDGuest = skv.getString("Midas", "offerCookieSIDGuest", cfg.offerCookieSIDGuest);
		cfg.offerCookieSTypeGuest = skv.getString("Midas", "offerCookieSTypeGuest", cfg.offerCookieSTypeGuest);
		cfg.payDiscountID = skv.getString("Midas", "payDiscountID", cfg.payDiscountID);
		cfg.payGiftID = skv.getString("Midas", "payGiftID", cfg.payGiftID);
		
		cfg.addrListen.host = skv.getString("GameServer", "host", cfg.addrListen.host);
		cfg.addrListen.port = skv.getInteger("GameServer", "port", cfg.addrListen.port);
		
		cfg.iosExamineServer = skv.getInteger("GameServer", "iosExamineServer", cfg.iosExamineServer);
		
		cfg.verMajor = skv.getInteger("GameServer", "verMajor", cfg.verMajor);
		cfg.verMinor = skv.getInteger("GameServer", "verMinor", cfg.verMinor);
		cfg.gameappID = skv.getString("GameServer", "gameappID", cfg.gameappID);
		cfg.gameappKey = skv.getString("GameServer", "gameappKey", cfg.gameappKey);
		cfg.gameappIDGuest = "G_" + skv.getString("GameServer", "gameappID", cfg.gameappID);
		cfg.gameappKeyGuest = cfg.gameappKey;
		cfg.areaID = skv.getByte("GameServer", "areaID", cfg.areaID);
		cfg.platID = skv.getByte("GameServer", "platID", cfg.platID);
		cfg.channelID = skv.getString("GameServer", "channelID", cfg.channelID);
		cfg.id = skv.getInteger("GameServer", "id", cfg.id);
		cfg.cap = skv.getInteger("GameServer", "cap", cfg.cap);
		cfg.sps = skv.getInteger("GameServer", "sps", cfg.sps);
		cfg.pps = skv.getInteger("GameServer", "pps", cfg.pps);
		cfg.roleLevelLimit = skv.getShort("GameServer", "roleLevelLimit", cfg.roleLevelLimit);
		
		cfg.zoneID = cfg.id;
		cfg.zoneID = skv.getInteger("Midas", "zoneID", cfg.zoneID);
		
		cfg.arenaVerifyFlag = skv.getInteger("GameServer", "arenaVerifyFlag", cfg.arenaVerifyFlag);
		cfg.arenaDebugFile = skv.getString("GameServer", "arenaDebugFile", cfg.arenaDebugFile);
		cfg.arenaDebugRank = skv.getInteger("GameServer", "arenaDebugRank", cfg.arenaDebugRank);
		cfg.luaChannelVerifyFlag = skv.getInteger("GameServer", "luaChannelVerifyFlag", cfg.luaChannelVerifyFlag);
		cfg.challengeFlag = skv.getInteger("GameServer", "challengeFlag", cfg.challengeFlag);
		cfg.challengeFuncArg = skv.getString("GameServer", "challengeFuncArg", cfg.challengeFuncArg);
		cfg.qqReportScore = skv.getInteger("GameServer", "qqReportScore", cfg.qqReportScore);
		cfg.httpThreadCount = skv.getInteger("GameServer", "httpThreadCount", cfg.httpThreadCount);
		cfg.httpTaskMaxCountL1 = skv.getInteger("GameServer", "httpTaskMaxCountL1", cfg.httpTaskMaxCountL1);
		cfg.httpTaskMaxCountL2 = skv.getInteger("GameServer", "httpTaskMaxCountL2", cfg.httpTaskMaxCountL2);
		cfg.httpCallbackThreadCount = skv.getInteger("GameServer", "httpCallbackThreadCount", cfg.httpCallbackThreadCount);
		
		cfg.forceWarVerifyFlag = skv.getInteger("GameServer", "forceWarVerifyFlag", cfg.forceWarVerifyFlag);
		
		cfg.addrControl.host = skv.getString("ManagerServer", "host", cfg.addrControl.host);
		cfg.addrControl.port = skv.getInteger("ManagerServer", "port", cfg.addrControl.port);
		
		cfg.addrTLog.host = skv.getString("TLogClient", "host", cfg.addrTLog.host);
		cfg.addrTLog.port = skv.getInteger("TLogClient", "port", cfg.addrTLog.port);
		
		cfg.addrExchange.host = skv.getString("ExchangeClient", "host", cfg.addrExchange.host);
		cfg.addrExchange.port = skv.getInteger("ExchangeClient", "port", cfg.addrExchange.port);
		
		cfg.addrFriend.host = skv.getString("FriendClient", "host", cfg.addrFriend.host);
		cfg.addrFriend.port = skv.getInteger("FriendClient", "port", cfg.addrFriend.port);
		
		cfg.addrIDIP.host = skv.getString("IDIPServer", "host", cfg.addrIDIP.host);
		cfg.addrIDIP.port = skv.getInteger("IDIPServer", "port", cfg.addrIDIP.port);
		
		cfg.addrWebService.host = skv.getString("WebService", "host", cfg.addrWebService.host);
		cfg.addrWebService.port = skv.getInteger("WebService", "port", cfg.addrWebService.port);
		
		cfg.newRoleGenerals = skv.getIntegerList("NewRole", "generals", ",");
		cfg.newRoleEquips = skv.getIntegerList("NewRole", "equips", ",");
		cfg.newRoleItems = skv.getIntegerList("NewRole", "items", ",");
		cfg.newRoleMoney = skv.getInteger("NewRole", "money", cfg.newRoleMoney);
		cfg.newRoleYuanbao = skv.getInteger("NewRole", "yuanbao", cfg.newRoleYuanbao);
		cfg.newRoleCombatStage = skv.getInteger("NewRole", "combatStage", cfg.newRoleCombatStage);
		
		cfg.godModeRoleLevel = skv.getShort("GodMode", "roleLevel", cfg.godModeRoleLevel);
		cfg.godModeGeneralLevel = skv.getShort("GodMode", "generalLevel", cfg.godModeGeneralLevel);
		cfg.godModeGeneralFullSkillLevel = skv.getByte("GodMode", "generalFullSkillLevel", cfg.godModeGeneralFullSkillLevel);
		cfg.godModeGeneralAdvLevel = skv.getByte("GodMode", "generalAdvLevel", cfg.godModeGeneralAdvLevel);
		cfg.godModeGeneralEvoLevel = skv.getByte("GodMode", "generalEvoLevel", cfg.godModeGeneralEvoLevel);
		cfg.godModeGeneralGildLevel = skv.getByte("GodMode", "generalGildLevel", cfg.godModeGeneralGildLevel);
		cfg.godModeGeneralWeaponReset = skv.getByte("GodMode", "generalWeaponReset", cfg.godModeGeneralWeaponReset);
		cfg.godModeGeneralSeyen = skv.getInteger("GodMode", "generalSeyen", cfg.godModeGeneralSeyen);
		cfg.godModeGeneralOfficialLvl = skv.getShort("GodMode", "generalOfficialLvl", cfg.godModeGeneralOfficialLvl);
		cfg.godModeGeneralOfficialSLvl = skv.getShort("GodMode", "generalOfficialSLvl", cfg.godModeGeneralOfficialSLvl);
		cfg.godModeRelation = skv.getInteger("GodMode", "relation", cfg.godModeRelation);
		cfg.godModeSeyen = skv.getInteger("GodMode", "seyen", cfg.godModeSeyen);
		cfg.godModeStone1 = skv.getInteger("GodMode", "generalstone", cfg.godModeStone1);
		String weaponCfg = skv.getString("GodMode", "generalWeaponEvo"
				, String.valueOf(cfg.godModeGeneralWeaponEvo) + ":" + String.valueOf(cfg.godModeGeneralWeaponLevel));
		try
		{
			if( weaponCfg != null && ! weaponCfg.equals("") )
			{
				String[] v = weaponCfg.split(":");
				cfg.godModeGeneralWeaponEvo = Byte.parseByte(v[0]);
				cfg.godModeGeneralWeaponLevel = Byte.parseByte(v[1]);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		cfg.godModePetLevel = skv.getShort("GodMode", "petLevel", cfg.godModePetLevel);
		cfg.godModePetEvoLevel = skv.getByte("GodMode", "petEvoLevel", cfg.godModePetEvoLevel);
		
		cfg.godModeMoney = skv.getInteger("GodMode", "money", cfg.godModeMoney);
		cfg.godModeStone = skv.getInteger("GodMode", "stone", cfg.godModeStone);
		cfg.godModeSkillPoint = skv.getInteger("GodMode", "skillPoint", cfg.godModeSkillPoint);
		cfg.godModeArenaPoint = skv.getInteger("GodMode", "arenaPoint", cfg.godModeArenaPoint);
		cfg.godModeMarchPoint = skv.getInteger("GodMode", "marchPoint", cfg.godModeMarchPoint);
		cfg.godModeForcePoint = skv.getInteger("GodMode", "forcePoint", cfg.godModeForcePoint);
		cfg.godModeRichPoint = skv.getInteger("GodMode", "richPoint", cfg.godModeRichPoint);
		cfg.godModeCombatPosNormalCIndex = skv.getByte("GodMode", "combatPosNormalCIndex", cfg.godModeCombatPosNormalCIndex);
		cfg.godModeCombatPosNormalBIndex = skv.getByte("GodMode", "combatPosNormalBIndex", cfg.godModeCombatPosNormalBIndex);
		cfg.godModeCombatPosHeroCIndex = skv.getByte("GodMode", "combatPosHeroCIndex", cfg.godModeCombatPosHeroCIndex);
		cfg.godModeCombatPosHeroBIndex = skv.getByte("GodMode", "combatPosHeroBIndex", cfg.godModeCombatPosHeroBIndex);
		cfg.godMode = skv.getInteger("Debug", "godMode", cfg.godMode);
		cfg.allowOpenID = skv.getString("Debug", "allowOpenID", cfg.allowOpenID);
		
		cfg.tssAntiState = skv.getInteger("TSSAnti", "state", cfg.tssAntiState);
		cfg.tssAntiUID = skv.getInteger("TSSAnti", "uid", cfg.tssAntiUID);
		cfg.tssAntiCfgFileName = skv.getString("TSSAnti", "cfgFileName", cfg.tssAntiCfgFileName);
		
		logger.info("gameappID is " + cfg.gameappID + ", platID is " + cfg.platID + ", channelID is " + cfg.channelID + ", gsID is " +  cfg.id);
		logger.info("server verions is " + cfg.verMajor + "." +  cfg.verMinor);
		logger.debug("role level limit is " + cfg.roleLevelLimit);
		logger.info("kio thread count is " + cfg.nIOThread);
		logger.info("godMode is " + cfg.godMode);
	}
	
	private void reloadConfig(String configFileName)
	{
		this.getLogger().info("reload gs config file : " + configFileName + " ......");
		File configFile = new File(configFileName);
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;
		
		cfg.cap = skv.getInteger("GameServer", "cap", cfg.cap);
		cfg.sps = skv.getInteger("GameServer", "sps", cfg.sps);
		cfg.pps = skv.getInteger("GameServer", "pps", cfg.pps);
		
		cfg.arenaVerifyFlag = skv.getInteger("GameServer", "arenaVerifyFlag", cfg.arenaVerifyFlag);
		cfg.luaChannelVerifyFlag = skv.getInteger("GameServer", "luaChannelVerifyFlag", cfg.luaChannelVerifyFlag);
		cfg.challengeFlag = skv.getInteger("GameServer", "challengeFlag", cfg.challengeFlag);
		cfg.challengeFuncArg = skv.getString("GameServer", "challengeFuncArg", cfg.challengeFuncArg);
		cfg.qqReportScore = skv.getInteger("GameServer", "qqReportScore", cfg.qqReportScore);
		cfg.httpThreadCount = skv.getInteger("GameServer", "httpThreadCount", cfg.httpThreadCount);
		cfg.httpTaskMaxCountL1 = skv.getInteger("GameServer", "httpTaskMaxCountL1", cfg.httpTaskMaxCountL1);
		cfg.httpTaskMaxCountL2 = skv.getInteger("GameServer", "httpTaskMaxCountL2", cfg.httpTaskMaxCountL2);
		cfg.httpCallbackThreadCount = skv.getInteger("GameServer", "httpCallbackThreadCount", cfg.httpCallbackThreadCount);
		
		cfg.forceWarVerifyFlag = skv.getInteger("GameServer", "forceWarVerifyFlag", cfg.forceWarVerifyFlag);
		
		cfg.godMode = skv.getInteger("Debug", "godMode", cfg.godMode);
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
	
	public int getTime()
	{
		// TODO
		return GameData.getTime();
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
	
	public int getDay()
	{
		return getTime() / 86400;
	}
	
	public int getDayCommon()
	{
		return getDayByOffset(gameData.getRoleCmnCFG().timeDayRefresh);
	}
	
	// return 0~6 0:=sunday
	public int getWeekday()
	{
		return (getDay()-3)%7;
	}
	
	public int getWeekday(int day)
	{
		return (day-3)%7;
	}
	
	public byte getWeekdayCommon()
	{
		return (byte)((getDayCommon()-3)%7);
	}
	
	public short getRoleLevelLimit()
	{
		return cfg.roleLevelLimit;
	}
	
	public boolean testWeekDays(List<Byte> lst)
	{
		byte dayNow = (byte)getWeekdayCommon();
		for(byte e : lst)
		{
			if( e == dayNow )
				return true;
		}
		return false;
	}
	
	public boolean isWeekend()
	{
		return getWeekday() == 0 || getWeekday() == 6;
	}
	
	public boolean isSunday()
	{
		return getWeekday() == 0;
	}
	
	int getDay(int t)
	{
		return t / 86400;
	}
	
	int getDayCommon(int t)
	{
		return (short)((t - gameData.getRoleCmnCFG().timeDayRefresh * 60) / 86400);
	}
	
	short getDayByOffset(short o)
	{
		return (short)((getTime() - o * 60) / 86400);
	}
	
	short getDayByOffsetRandAdjust(short o, int secs)
	{
		int now = getTime() + secs;
		return (short)((now - o * 60) / 86400);
	}
	
	void init()
	{
		String patchFileName = "patch.dat"; 
		if( ! hotSwap.init(patchFileName) )
		{
			throw new Error("HS init failed");
		}
		fileWatchdogs.put(patchFileName, new FileWatchdog(patchFileName, new Runnable()
		{
			@Override
			public void run()
			{
				hotSwap.reload();
			}
			
		}));
		managerAnti.init();
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
		//executor.
		
		db = ket.kdb.Factory.newDB();
		db.setLogger(logger);
		Path p = Paths.get(cfg.dbcfgfile);
		db.open(p.getParent(), p);
		
		db.execute(new WorldInitTrans());
	}
	
	void destroy()
	{
		if ( timer100task.future != null )
			timer100task.future.cancel(false);
		managerAnti.destroy();
		if ( timertask.future != null )
			timertask.future.cancel(false);
//		if ( fileWatchdog.future!=null )
//			fileWatchdog.future.cancel(false);
		managerRPC.destroy();
		midas.destroy();
		logger.info("main executor shutdown start");
		executor.shutdown();
		gameService.shutDown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
		logger.info("main executor shutdown ok");
		managerLogin.saveRoles();
		managerArena.save();
		managerCity.save();
		managerForce.save();
		managerWar.save();
		managerRich.save();
		managerZan.save();
		managerDuel.save();
		managerSura.save();
		managerSWar.save();
		managerDisk.save();
		managerDisk2.save();
		managerExpiratBoss.save();
        managerHeroesBoss.save();
        managerTreasureMap.save();
		db.close();
		logger.info("db close ok");
	}
	
	void start()
	{
		//
		logger.info("gs start1debug");
		gameActivities.init(this.getConfig().activitiesDir);
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
		//fileWatchdog.future = executor.scheduleAtFixedRate(fileWatchdog, 1, 1, TimeUnit.MINUTES);
		managerRPC.start();
		managerLogin.start();
		if( cfg.tssAntiState > 0 )
			timer100task.future = executor.scheduleAtFixedRate(timer100task, 10, 10, TimeUnit.MILLISECONDS);
		midas.start();
		gameService.init(cfg.addrWebService.host, cfg.addrWebService.port);
		logger.info("gs start " + getTime());
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	TLogger getTLogger()
	{
		return tlogger;
	}
	
	public LoginManager getLoginManager()
	{
		return managerLogin;
	}
	
	public HotSwapModule getHotChecker()
	{
		return hotSwap;
	}
	
	ForceManager getForceManager()
	{
		return managerForce;
	}
	
	TSSAntiManager getTSSAntiManager()
	{
		return managerAnti;
	}
	
	ArenaManager getArenaManager()
	{
		return managerArena;
	}
	
	DiskBetManager getDiskManager()
	{
		return managerDisk;
	}
	
	DiskBetManager2 getDiskManager2()
	{
		return managerDisk2;
	}
	
	MarchManager getMarchManager()
	{
		return managerMarch;
	}
	
	ForceWarManager getWarManager()
	{
		return managerWar;
	}
	
	public RPCManager getRPCManager()
	{
		return managerRPC;
	}
	
	ExchangeManager getExchangeManager()
	{
		return managerExchange;
	}
	
	RichManager getRichManager()
	{
		return managerRich;
	}
	
	ExpiratBossManager getExpiratBossManager()
	{
		return managerExpiratBoss;
	}
	
	HeroesBossManager getHeroesBossManager()
	{
		return managerHeroesBoss;
	}
	
	PetZanManager getZanManager()
	{
		return managerZan;
	}
	
	DuelManager getDuelManager()
	{
		return managerDuel;
	}
	
	SuraManager getSuraManager()
	{
		return managerSura;
	}
	
	ForceSWarManager getSWarManager()
	{
		return managerSWar;
	}
	
	GameService getGameService()
	{
		return gameService;
	}
	
	Midas getMidas()
	{
		return midas;
	}
	
	public String getGameAppID(boolean bGuest)
	{
		return bGuest ? cfg.gameappIDGuest : cfg.gameappID;
	}
	
	GameActivities getGameActivities()
	{
		return gameActivities;
	}
	
	CityManager getCityManager()
	{
		return managerCity;
	}
	
	TreasureMapManager getTreasureMapManager()
	{
	    return managerTreasureMap;
	}
	
	BeMonsterManager getBeMonsterManager()
	{
	    return managerBeMonster;
	}
	
	DB getDB()
	{
		return db;
	}
	
	boolean loadGameData(String filePath)
	{
		try
		{
			SBean.GameDataCFGS gamedata = new SBean.GameDataCFGS();
			if( ! Stream.loadObjLE(gamedata, new File(filePath)) )
				return false;
			gameData = new GameData(gamedata);
			logger.info("��ȡ gamedata �ɹ�");
			//
			monitorGameDataConfiguerFile(filePath);
			//
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	boolean reloadGameData(String filePath)
	{
		try
		{
			SBean.GameDataCFGS gamedata = new SBean.GameDataCFGS();
			if( ! Stream.loadObjLE(gamedata, new File(filePath)) )
				return false;
			boolean bOK = gameData.reload(gamedata);
			logger.info("reload gamedata " + bOK);
			return bOK;
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
	
	public IPad getIPad()
	{
		return ipad;
	}
	
	public void initSignal()
	{
		logger.info("before reg singal term");
		Signal.handle(new Signal("TERM"), this);
		logger.info("after reg singal term");
		ket.util.FileSys.pauseWaitSingal(latch);
		logger.info("after pause wait term");
	}
	
	public void shutdownBG()
	{
		logger.info("white list set close1");
		if( latch.getCount() != 1 )
			return;
		logger.info("white list set close2");
		latch.countDown();
		logger.info("white list set close3");
		try
		{
			SignalHandler handlerNow = Signal.handle(new Signal("TERM"), this);
			String name = "null";
			if( handlerNow != null )
			{
				if( handlerNow == SignalHandler.SIG_IGN )
					name = "IGN";
				else if( handlerNow == SignalHandler.SIG_DFL )
					name = "DFL";
				else
					name = handlerNow.getClass().getName();
			}
			logger.info("shutdown check sighandler, name is " + name);
		}
		catch(Exception ex)
		{
			logger.info("shutdown check sighandler failed", ex);
		}
	}
	
	public boolean testGMCommand()
	{
		return cfg.godMode != 0;
	}
	
	@Override
	public void handle(Signal sig)
	{
		logger.info("handler recieved sig " + sig.getName());
		// TODO Auto-generated method stub
		if( bSingalHandled )
			return;
		logger.info("handler recieved sig " + sig.getName() + "after handled");
		if( latch.getCount() != 1 )
			return;
		logger.info("handler recieved sig " + sig.getName() + "after test count");
		if( sig.getName().equals("TERM") )
		{
			logger.info("recieved term");
			bSingalHandled = true;
			latch.countDown();
			logger.info("recieved term after countdown");
		}
	}
	
	public void execute(Runnable runnable)
	{
		executor.execute(runnable);
	}
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		GameServer gs = new GameServer(am.get("-cfgfile", "gs.cfg"));
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
			// TODO
			//ket.util.FileSys.pauseWaitInput();
			if( System.console() != null )
			{
				while( true )
				{
					String line = System.console().readLine();
					System.out.println("read line is [" + line + "]");
					if( line.equals("") )
						break;
					if( line.equals("1") )
						gs.getRPCManager().setDisconnectMode(true);
					if( line.equals("2") )
						gs.getRPCManager().setDisconnectMode(false);
				}
			}
			else
			{
				ket.util.FileSys.pauseWaitInput();
			}
		}
		gs.destroy();
	}

	DB db;
	private volatile boolean bSingalHandled = false;
	private CountDownLatch latch = new CountDownLatch(1);
	private GameActivities gameActivities;
	private RPCManager managerRPC;
	private Midas midas;
	private LoginManager managerLogin;
	private ForceManager managerForce;
	private ArenaManager managerArena;
	private ExchangeManager managerExchange;
	private MarchManager managerMarch;
	private ForceWarManager managerWar;
	private GameService gameService;
	private TSSAntiManager managerAnti;
	private CityManager managerCity;
	private HotSwapModule hotSwap = new HotSwapModule(this);
	private RichManager managerRich;
	private ExpiratBossManager managerExpiratBoss;
	private HeroesBossManager managerHeroesBoss;
	private PetZanManager managerZan;
	private DuelManager managerDuel;
	private SuraManager managerSura;
	private ForceSWarManager managerSWar;
	private DiskBetManager managerDisk;
	private DiskBetManager2 managerDisk2;
	private Logger logger = Logger.getLogger("gsLogger");
	//private Logger tLogger = Logger.getLogger("tLogger");
	private TLogger tlogger = new TLogger(this);
	public Config cfg = new Config();
	GameData gameData;
	private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	private final TimerTask timertask = new TimerTask();
	private final Timer100Task timer100task = new Timer100Task();
	//private final FileWatchdog fileWatchdog = new FileWatchdog();
	private Map<String, FileWatchdog> fileWatchdogs = new TreeMap<String, FileWatchdog>();
	
	private Map<String, String> userStateMap = new HashMap<String, String>();
	
	private IPad ipad = new IPad();
	private TreasureMapManager managerTreasureMap;
	private BeMonsterManager managerBeMonster;
}

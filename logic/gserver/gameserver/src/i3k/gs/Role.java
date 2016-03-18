
package i3k.gs;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import i3k.DBFriend;
import i3k.DBMail;
import i3k.DBMail.MailAttachments;
import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.BeMonsterCFGS;
import i3k.SBean.BeMonsterUpHpCFG;
import i3k.SBean.BestowCFGS;
import i3k.SBean.BestowItemCFGS;
import i3k.SBean.BlessCFGS;
import i3k.SBean.CityInfo;
import i3k.SBean.CommomConstantCFGS;
import i3k.SBean.DBBeMonsterAttacker;
import i3k.SBean.DBBeMonsterBoss;
import i3k.SBean.DBBeMonsterGeneral;
import i3k.SBean.DBBeMonsterLineup;
import i3k.SBean.DBBestowLevel;
import i3k.SBean.DBBlessInfo;
import i3k.SBean.DBDiskBet;
import i3k.SBean.DBDiskBetInfo;
import i3k.SBean.DBDiskBetRank;
import i3k.SBean.DBExpiratBossTimes;
import i3k.SBean.DBForceWarGeneralStatus;
import i3k.SBean.DBGeneralBestow;
import i3k.SBean.DBGeneralBless;
import i3k.SBean.DBGeneralSeyen;
import i3k.SBean.DBGeneralStone;
import i3k.SBean.DBGeneralStoneProp;
import i3k.SBean.DBHeadIcon;
import i3k.SBean.DBHeadIconList;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBHeroesBossTimes;
import i3k.SBean.DBInvitationFriend;
import i3k.SBean.DBPetBrief;
import i3k.SBean.DBRichDailyTask;
import i3k.SBean.DBRoleDiskGift;
import i3k.SBean.DBRoleRechargeGift;
import i3k.SBean.DBRoleSeyen;
import i3k.SBean.DBTreasureLimitCnt;
import i3k.SBean.DBTreasureMap;
import i3k.SBean.DBTreaureReward;
import i3k.SBean.DBWeaponProp;
import i3k.SBean.DailyActivityRewardCFGS;
import i3k.SBean.DropEntry;
import i3k.SBean.DropEntryNew;
import i3k.SBean.DropTableEntryNew;
import i3k.SBean.ExpiratBossBasicCFGS;
import i3k.SBean.ExpiratBossItemsCFGS;
import i3k.SBean.ForceActivityRewardCFGS;
import i3k.SBean.GeneralSeyenExpAddCFGS;
import i3k.SBean.GeneralStoneBasicCFGS;
import i3k.SBean.GeneralStoneEvoCostCFGS;
import i3k.SBean.GeneralStonePosCFGS;
import i3k.SBean.GeneralStonePropCFGS;
import i3k.SBean.GeneralStonePropValueCFGS;
import i3k.SBean.GlobalRoleID;
import i3k.SBean.HeadIconCFGS;
import i3k.SBean.HeroesBossBuffCFGS;
import i3k.SBean.HeroesBossCFGS;
import i3k.SBean.HeroesBossScoreCFGS;
import i3k.SBean.HeroesRank;
import i3k.SBean.ItemCFGS;
import i3k.SBean.RoleDataRes;
import i3k.SBean.SuperArenaFightInfo;
import i3k.TLog;
import i3k.gs.CityManager.CityBrief;
import i3k.gs.GameActivities.DiskBetConfig;
import i3k.gs.GameActivities.DiskGiftConfig;
import i3k.gs.GameActivities.DiskLvlGift;
import i3k.gs.GameActivities.GeneralGift;
import i3k.gs.GameActivities.RechargeGiftConfig;
import i3k.gs.GameActivities.RechargeLvlGift;
import i3k.gs.GameActivities.RechargeRankConfig;
import i3k.gs.LoginManager.CDKeyExchange;
import i3k.gs.LoginManager.CommonRoleVisitor;
import i3k.gs.LoginManager.GlobalRoleIDEncoder;
import i3k.gs.LoginManager.RoleBriefCachePair;
import i3k.gs.LoginManager.WorldChatCache.Msg;
import i3k.gs.MarchManager.AttackFinishRes;
import i3k.gs.MarchManager.AttackStartRes;
import i3k.gs.Midas.BalanceResult;
import i3k.gs.Midas.UserInfo;
import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.StringConverter;

public class Role
{
	static final int MAX_STANDBY_TIME = 10 * 60 * 1000; // ms
	static final int SAVE_ROLE_INTERVAL_MIN = 15 * 60 * 1000; // ms
	static final int SAVE_ROLE_INTERVAL_RAND = 5 * 60 * 1000; // ms
	
	static final int ROLE_NEW = 0;
	static final int ROLE_LOAD_FROM_DB = 1;
	static final int ROLE_LOAD_FROM_MEMORY = 2;
	
	static final int ROLE_FLAG_GENDER = 0x1;				// 0: male 1: female
	static final int ROLE_FLAG_FIRST_CHECKIN_FINISHED = 0x2;	// 0: not finished 1: finished
	static final int ROLE_FLAG_CREATE_EXCHANGE = 0x4;	// 0: not finished 1: finished
	static final int ROLE_FLAG_CHECKIN = 0x8;	// 0: clear 1: set
	static final int ROLE_FLAG_EGG = 0x10;	// 0: clear 1: set
	static final int ROLE_FLAG_ALPHATEST_PAY_DOUBLE = 0x20;	// 0: clear 1: set
	public static final int ROLE_FLAG_QQ_VIP_NORMAL = 0x40;	// 0: clear 1: set
	public static final int ROLE_FLAG_QQ_VIP_SUPER = 0x80;	// 0: clear 1: set
	static final int ROLE_FLAG_SUPER_CHONGXING = 0x100;	// 0: clear 1: set
	static final int ROLE_FLAG_STRATEGY_IGNORE = 0x200;	// 0: clear 1: set
	static final int ROLE_FLAG_SOLDIER_COMBAT = 0x400;	// 0: clear 1: set
	static final int ROLE_FLAG_AUTO_FIGHTING = 0x800;	// 0: clear 1: set
	static final int ROLE_FLAG_1000 = 0x1000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_2000 = 0x2000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_4000 = 0x4000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_TIPS_BOSS = 0x8000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_TIPS_COUNTRY = 0x10000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_TIPS_ZHENFA = 0x20000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_TIPS_COMBAT = 0x40000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_FIRST_EGG5 = 0x80000;	// 0: not finished 1: finished
	static final int ROLE_FLAG_ALL = 0xFFFFFFFF;	// 0: clear 1: set
	
	static final int ROLE_FLAG_QQ_VIP_REWARD_NORMAL_NEW = 0x01;
	static final int ROLE_FLAG_QQ_VIP_REWARD_NORMAL_ADD = 0x02;
	static final int ROLE_FLAG_QQ_VIP_REWARD_SUPER_NEW = 0x04;
	static final int ROLE_FLAG_QQ_VIP_REWARD_SUPER_ADD = 0x08;
	static final int ROLE_FLAG_QQ_VIP_NORMAL_ADD = 0x10;
	static final int ROLE_FLAG_QQ_VIP_SUPER_ADD = 0x20;
	
	static final int DAY_FLAG_REPORT_SCORE = 0x1;				// 锟斤拷锟斤拷讯锟较憋拷锟斤拷锟斤拷
	
	static final int DAY_GIFT_FLAG_ALPHA_GIFT = 0x1000;				// 0: not used
	
	public static final int PVP_FLAG_COMBINE = 0x1;
	public static final int PVP_FLAG_ESCAPE = 0x2;
	
	public static final int SHOP_RICH_INDEX_STONE = 0;
	public static final int SHOP_RICH_INDEX_POINT = 1;
	public static final int SHOP_RICH_INDEX_GOLD = 2;
	private static final int SHOP_RICH_COUNT = 3;
	
	static final Role NULL_ROLE = new Role(0, null, 0);
	
	//private final static char TLOG_SEP = '|';
	
	public static class MSDKInfo
	{
		public MSDKInfo()
		{
			
		}
		
		public boolean isIOSGuest()
		{
			return bGuest;
		}
		
		public String gameappID = "";					// 锟斤拷戏APPID(锟斤拷锟斤拷)
		public String openID = "";						// 锟矫伙拷OPENID锟斤拷(锟斤拷锟斤拷)
		public int platID;								// ios0/ android 1(锟斤拷锟斤拷)
		public String clientVer = "";					// 锟酵伙拷锟剿版本锟斤拷
		public String systemSoftware = "";				// 锟狡讹拷锟秸端诧拷锟斤拷系统锟芥本
		public String systemHardware = "";				// 锟狡讹拷锟秸端伙拷锟斤拷
		public String telecomOper = "";				// 锟斤拷营锟斤拷(锟斤拷锟斤拷)
		public String network = "";					// 3G/WIFI/2G
		public int screenWidth;						// 锟斤拷示锟斤拷锟斤拷锟�
		public int screenHeight;						// 锟斤拷示锟斤拷锟竭讹拷
		public float density;							// 锟斤拷锟斤拷锟杰讹拷
		public int loginChannel;							// 注锟斤拷锟斤拷锟斤拷(锟斤拷锟斤拷)
		public String cpuHardware = "";				// cpu锟斤拷锟斤拷|频锟斤拷|锟斤拷锟斤拷
		public int memory;								// 锟节达拷锟斤拷息锟斤拷位M
		public String gLRender = "";					// opengl render锟斤拷息
		public String gLVersion = "";					// opengl锟芥本锟斤拷息
		public String deviceID = "";					// 锟借备ID
		
		public String openKey = "";
		public String payToken = "";
		public String pf = "";
		public String pfKey = "";
		
		public String ip = "";
		public int ipInt = 0;
		public int clientVerInt = 0;
		
		public byte loginType;
		public boolean bGuest = false;
	}
	
	public static class CheckinSyncInfo
	{
		public byte id;
		public byte n;
		public boolean bOK;
	}
	
	public static class MailGiftInfo
	{
		public MailGiftInfo()
		{
			
		}
		public MailGiftInfo(byte type, short id, int cnt)
		{
			this.type = type;
			this.id = id;
			this.cnt = cnt;
		}
		
		public byte type;
		public short id;
		public int cnt;
	}
	
	public static class VIPInfo
	{
		public int payTotal;
		public byte lvl;
		public List<Byte> vipRewards;
	}
	
	public static class JubaoInfo
	{
		public byte times;
	}
	
	public static class EggInfo
	{
		public byte id;
		public byte timesToday;
		public byte timesAll;
		public int freeTime;
		public byte times10;
		public byte firstEgg;
	}
	
	public static class SoulBoxInfo
	{
		public byte dayIndex;
		public SBean.SoulBoxCFGS cfg; 
		public short optionalGid;
	}
	
	public static class MedalInfo
	{
		public byte id;
		public byte seq;
		public byte state;
		public int max;
		public int cur;
	}
	
	public static class DayGiftRes
	{
		public String type;
		public byte res;
		public float r;
	}
	
	public static class MarchStateInfo
	{
		public int maxLvl;
		public byte stage;
		public byte rewardTaken;
		public int point;
		public byte timesUsed;
		public List<SBean.DBRoleMarchGeneralState> generalStates;
		public List<SBean.DBRoleMarchGeneralState> enemyGeneralStates;
		public List<SBean.DBRoleMarchEnemy> enemies;
	}
	
	public static class ArenaStateInfo
	{		
		public int maxLvl;
		public int maxWinTimes;
		public int power;
		public int point;
		public int rankNow;
		public int rankBest;
		public int rankReward;
		public short stampReward;
		public byte timesUsed;
		public byte timesBuy;
		public int lastTime;
		public List<SBean.DBRoleBrief> enemies;
		public int[] ranks;
		public short[] generals;
		public short[] pets;
	}
	
	public static class ArenaLog
	{
		public byte win;
		public int rankUp;
		public int id;
		public int time;
		public short headIconID;
		public short lvl;
		public String name = "";
		public int recordId;
	}
	
	public static class ArenaRank
	{
		public int id;
		public short lvl;
		public short headIconID;
		public String name = "";
	}
	
	public static class MarchWipeDrop
	{
		public int money;
		public int point;
		public SBean.DropEntry drop;
	}
	
	public static class ForceInfo
	{
		public ForceInfo(int id, int point, int contrib, String name, short iconId, int mobaiReward, short superMobai)
		{
			this.id = id;
			this.point = point;
			this.contrib = contrib;
			this.name = name;
			this.iconId = iconId;
			this.mobaiReward = mobaiReward;
			this.superMobai = superMobai;
		}
		
		public ForceInfo(int id, int point, int contrib, int joinTime, String name, short iconId, List<Integer> mobai, int mobaiReward, short superMobai)
		{
			this.id = id;
			this.point = point;
			this.contrib = contrib;
			this.name = name;
			this.iconId = iconId;
			this.joinTime = joinTime;
			this.mobai = mobai;
			this.mobaiReward = mobaiReward;
			this.superMobai = superMobai;
		}
		
		public ForceInfo kdclone()
		{
			return new ForceInfo(id, point, contrib, joinTime, name, iconId, new ArrayList<Integer>(mobai), mobaiReward, superMobai);
		}
		
		public boolean isInForce()
		{
			return id > 0;
		}
		
		public boolean isInForce(int id)
		{
			return id == this.id;
		}
		
		public void changeForce(int id, short iconId, String name, int now)
		{
			if( this.id <= 0 && id > 0 )
			{
				joinTime = now;
			}
			this.id = id;
			this.name = name;
			this.iconId = iconId;
		}
		
		public void clear()
		{
			mobai.clear();
		}
		
		public int id;
		public int point;
		public int contrib;
		public int joinTime;
		public String name;
		public short iconId;
		public List<Integer> mobai = new ArrayList<Integer>();
		public int mobaiReward;
		public short superMobai;
	}
	
	public boolean isNull()
	{
		return gs == null;
	}
	
	public class SaveRoleTrans implements Transaction
	{
		public SaveRoleTrans(DBRole dbRole)
		{
			this.dbRole = dbRole;
		}

		@Override
		public boolean doTransaction()
		{
			gs.getLogger().debug("save role " + dbRole.id + " to DB");
			role.put(dbRole.id, dbRole);
			return true;
		}
		
		@AutoInit
		public Table<Integer, DBRole> role;
		
		public final DBRole dbRole;

		@Override
		public void onCallback(ErrorCode errcode)
		{ // TODO
		}
	}
	
	public class SaveFriendTrans implements Transaction
	{
		public SaveFriendTrans(int roleID, DBFriend dbFriend)
		{
			this.roleID = roleID;
			this.dbFriend = dbFriend;
		}

		@Override
		public boolean doTransaction()
		{
			gs.getLogger().debug("save friends of " + roleID + " to DB");
			friend.put(roleID, dbFriend);
			return true;
		}
		
		@AutoInit
		public Table<Integer, DBFriend> friend;
		
		public final int roleID;
		public final DBFriend dbFriend;

		@Override
		public void onCallback(ErrorCode errcode)
		{ // TODO
		}
	}
	
	public class SaveMailTrans implements Transaction
	{
		public SaveMailTrans(int roleID, DBMail dbMail)
		{
			this.roleID = roleID;
			this.dbMail = dbMail;
		}

		@Override
		public boolean doTransaction()
		{
			gs.getLogger().debug("save mails of " + roleID + " to DB");
			mail.put(roleID, dbMail);
			return true;
		}
		
		@AutoInit
		public Table<Integer, DBMail> mail;
		
		public final int roleID;
		public final DBMail dbMail;

		@Override
		public void onCallback(ErrorCode errcode)
		{ // TODO
		}
	}	
	
	public void addSysMessage(byte subType, String title, String content, List<SBean.DropEntryNew> attLst, int lifeTime, boolean bNotifyOnline)
	{
		int newStamp = -1;
		synchronized( this )
		{
			mail.dbmail.addSysMessage(subType, title, content, attLst, gs, lifeTime);
			if( netsid != 0 )
				newStamp = 0; // TODO
		}
		if( bNotifyOnline && newStamp != -1 )
			gs.getRPCManager().notifyMailNew(netsid);
	}
	
	public void addUserMessage(String sendRoleName, String title, String content, List<SBean.DropEntryNew> attLst, int lifeTime, boolean bNotifyOnline)
	{
		int newStamp = -1;
		synchronized( this )
		{
			mail.dbmail.addUserMessage(sendRoleName, title, content, attLst, gs, lifeTime);
			if( netsid != 0 )
				newStamp = 0; // TODO
		}
		if( bNotifyOnline && newStamp != -1 )
			gs.getRPCManager().notifyMailNew(netsid);
	}
	
	public void addUserMessage(String msg, int srcID, String srcName)
	{
		/*
		int newStamp = -1;
		synchronized( this )
		{
			mail.dbmail.addUserMessage(srcID, srcName, msg, gs);
			if( netsid != 0 )
				newStamp = mail.dbmail.lastModifyTime;
		}
		if( newStamp != -1 )
			gs.getRPCManager().notifyMailNew(netsid, newStamp);
		*/
	}
	
	
	public boolean testNewMail()
	{
		synchronized( this )
		{
			return mail.dbmail.testNewMail();
		}
	}
	
	public List<DBMail.Message> syncMails()
	{
		int curTime = gs.getTime();
		synchronized( this )
		{
			return mail.dbmail.syncMails(curTime);
		}
	}
	
	public int mailSetRead(short mid)
	{
		int res = 0;
		DBMail.MailAttachments att = new MailAttachments();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			res = mail.dbmail.setRead(mid, att);
			if( res > 0 )
			{
				commonFlowRecord.addChanges(addDropsNew(att.attLst));
				commonFlowRecord.setEvent(TLog.AT_SYS_REWARD);
				commonFlowRecord.setArg(att.mailSubType, 0);
			}
		}
		if( res < 0 )
		{
			int srcID = -res;
			List<String> contents = new ArrayList<String>();
			if( gs.getLoginManager().getWorldMailContent(srcID, contents, att) )
			{
				synchronized( this )
				{
					commonFlowRecord.addChanges(addDropsNew(att.attLst));
					commonFlowRecord.setEvent(TLog.AT_WORLD_REWARD);
					commonFlowRecord.setArg(att.mailSubType, 0);
				}
			}
			else
				return 1;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return res;
	}
	
	public String mailReadContent(short mid, DBMail.MailAttachments att)
	{
		List<String> content = new ArrayList<String>();
		int res = 0;
		synchronized( this )
		{
			res = mail.dbmail.readContent(mid, content, att);
		}
		if( res < 0 )
			return "";
		if( res == 0 )
			return content.get(0);
		// res > 0
		{
			int srcID = res;
			if( gs.getLoginManager().getWorldMailContent(srcID, content, att) )
				return content.get(0);
		}
		return "";
	}
	
	public void doSave()
	{
		if( isNull() )
			return;
		DBMail dbmail = null;
		DBRole dbrole = null;		
		DBFriend dbfriend = null;
		synchronized( this )
		{
			dbmail = mail.copyDBMail();
			dbfriend = friend.copyDBFriend();
			dbrole = copyDBRoleWithoutLock();
		}

		gs.getDB().execute(new SaveRoleTrans(dbrole));
		if( dbfriend != null )
			gs.getDB().execute(new SaveFriendTrans(id, dbfriend));
		if( dbmail != null )
			gs.getDB().execute(new SaveMailTrans(id, dbmail));
	}
	
	public int isRecvWorldChat()
	{
		if( isNull() )
			return -1;
		if( netsid <= 0 )
			return -1;
		if( ! bRecvWorldChat )
			return -1;
		return netsid;
	}
	
	public int isRecvForceChat()
	{
		if( isNull() )
			return -1;
		if( netsid <= 0 )
			return -1;
		if( ! bRecvForceChat )
			return -1;
		return netsid;
	}
	
	public synchronized boolean checkSessionID(int sid)
	{
		return ! isNull() && netsid == sid;
	}
	
	public class General extends DBRoleGeneral
	{
		public General(short id, byte evoLvl)
		{
			this.id = id;
			lvl = 1;
			exp = 0;
			advLvl = 1;
			this.evoLvl = evoLvl;
			weapon = new SBean.DBWeapon(id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>());
			skills = new ArrayList<Short>();
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				skills.add((short)1);
			equips = new ArrayList<SBean.DBGeneralEquip>();
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				equips.add(new SBean.DBGeneralEquip((byte)-1, (short)0));
			generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
			generalSeyen.add(new SBean.DBGeneralSeyen());
			bestow = new ArrayList<SBean.DBGeneralBestow>();
			SBean.DBGeneralBestow gbestow = new SBean.DBGeneralBestow();
			gbestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
			bestow.add(gbestow);
			bless = new ArrayList<SBean.DBGeneralBless>();
			bless.add(new SBean.DBGeneralBless());
			official = new ArrayList<SBean.DBGeneralOfficial>();
			official.add(new SBean.DBGeneralOfficial());
		}
		
		public General(DBRoleGeneral dbg)
		{
			id = dbg.id;
			lvl = dbg.lvl;
			exp = dbg.exp;
			advLvl = dbg.advLvl;
			evoLvl = dbg.evoLvl;
			weapon = dbg.weapon;
			headicon = dbg.headicon;
			for (SBean.DBWeaponProp p : weapon.passes) {
				if (p.propId == 33 && p.value <= 0.1f)
					p.value = (float)(int)(p.value * 1000);
			}
			skills = new ArrayList<Short>(dbg.skills);
			equips = new ArrayList<SBean.DBGeneralEquip>(dbg.equips);
			if(dbg.generalSeyen!=null){
				generalSeyen = new ArrayList<SBean.DBGeneralSeyen>(dbg.generalSeyen);
			}else{
				generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
				generalSeyen.add(new SBean.DBGeneralSeyen());
			}
			if(dbg.bestow!=null){
				bestow = new ArrayList<SBean.DBGeneralBestow>(dbg.bestow);
			}else{
				bestow = new ArrayList<SBean.DBGeneralBestow>();
				SBean.DBGeneralBestow gbestow = new SBean.DBGeneralBestow();
				gbestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
				bestow.add(gbestow);
			}
			
			if(dbg.bless!=null){
				bless = new ArrayList<SBean.DBGeneralBless>(dbg.bless);
			}else{
				bless = new ArrayList<SBean.DBGeneralBless>();
				SBean.DBGeneralBless gbestow = new SBean.DBGeneralBless();
				bless.add(gbestow);
			}
			
			if(dbg.official!=null){
				official = new ArrayList<SBean.DBGeneralOfficial>(dbg.official);
			}else{
				official = new ArrayList<SBean.DBGeneralOfficial>();
			}
			if (official.size() == 0)
				official.add(new SBean.DBGeneralOfficial());
		}
		
		public DBRoleGeneral copyDBGeneralWithoutLock()
		{
			return kdClone();
		}	
		
		public void levelUp()
		{
			++lvl;
		}		
		
		public int getUpgradeExp()
		{
			return GameData.getInstance().getGeneralUpgradeExp(lvl);
		}
		
		public boolean addExp(int add)
		{
			int levellimit = gs.getRoleLevelLimit();
			//if( lvl >= gs.getRoleLevelLimit() )
			if ( lvl >= levellimit)
				return false;
			if( add < 0 )
				return false;
			if( add == 0 )
				return true;
			//
			int expUpgrade = getUpgradeExp();
			if( lvl == Role.this.lvl )
			{
				if( exp >= expUpgrade )
					return false;
			}
			exp += add;
			while( exp >= expUpgrade )
			{
				if( lvl >= Role.this.lvl )
				{
					exp = expUpgrade;
					return true;
				}
				exp -= expUpgrade;
				levelUp();
				//if( lvl >= gs.getRoleLevelLimit() )
				if ( lvl >= levellimit )
				{
					exp = 0;
					return true;
				}
				expUpgrade = getUpgradeExp();
			}
			//
			return true;
		}
		
		public boolean upgrade(int add)
		{
			int levellimit = gs.getRoleLevelLimit();
			if( add <= 0 )
				return false;
			//if( lvl >= gs.getRoleLevelLimit() )
			if ( lvl >= levellimit)
				return false;
			if( lvl >= Role.this.lvl )
				return false;
			lvl += add;
			if( lvl > Role.this.lvl )
			{
				lvl = Role.this.lvl;
			}
			return true;
		}
	}
	
	public class Pet extends SBean.DBPet
	{
		public Pet(short id, byte evoLvl, short growthRate)
		{
			this.id = id;
			lvl = 1;
			exp = 0;
			this.evoLvl = evoLvl;			
			lastBreedTime = 0;
			this.growthRate = (byte)growthRate;
			this.awakeLvl = 0;
			name = "";
			feedGain = 0;
			feedMode = 0;
			feedTime = 0;
			feedNeed = new SBean.DropEntry();
			hungryTimes = 0;
			hungryDay = 0;
		}
		
		public Pet(SBean.DBPet dbp)
		{
			id = dbp.id;
			lvl = dbp.lvl;
			exp = dbp.exp;
			evoLvl = dbp.evoLvl;
			lastBreedTime = dbp.lastBreedTime;
			growthRate = dbp.growthRate;
			awakeLvl = dbp.awakeLvl;
			name = dbp.name;
			feedGain = dbp.feedGain;
			feedMode = dbp.feedMode;
			feedTime = dbp.feedTime;
			feedNeed = dbp.feedNeed;
			hungryTimes = dbp.hungryTimes;
			hungryDay = dbp.hungryDay;
		}
		
		public SBean.DBPet copyDBPetWithoutLock()
		{
			return kdClone();
		}	
		
		public SBean.DBPetBrief copyDBFriendPetWithoutLock(byte deformStage)
		{
			return new SBean.DBPetBrief((byte)id, awakeLvl, lvl, evoLvl, (byte)growthRate, deformStage, name);
		}
		
		public void levelUp()
		{
			++lvl;
		}		
		
		public int getUpgradeExp()
		{
			return GameData.getInstance().getPetUpgradeExp(lvl);
		}
		
		public boolean addExp(int add)
		{
			int levellimit = gs.getRoleLevelLimit();
			//if( lvl >= gs.getRoleLevelLimit() )
			if ( lvl >= levellimit )
				return false;
			if( add < 0 )
				return false;
			if( add == 0 )
				return true;
			//
			int expUpgrade = getUpgradeExp();
			if( lvl == Role.this.lvl )
			{
				if( exp >= expUpgrade )
					return false;
			}
			exp += add;
			while( exp >= expUpgrade )
			{
				if( lvl >= Role.this.lvl )
				{
					exp = expUpgrade;
					return true;
				}
				exp -= expUpgrade;
				levelUp();
				//if( lvl >= gs.getRoleLevelLimit() )
				if ( lvl >= levellimit )
				{
					exp = 0;
					return true;
				}
				expUpgrade = getUpgradeExp();
			}
			//
			return true;
		}
		
		SBean.DropEntry hungry(boolean deformed)
		{
			SBean.PetEatCFGS eat = GameData.getInstance().getPetEatCFG(id, lvl, deformed);
			if (eat != null) {
				SBean.DropEntry dropEntry = gs.getGameData().getDropTableRandomEntry(eat.eatId);
				if (dropEntry != null)
					return dropEntry.kdClone();
			}
			
			return null;
		}
		
		SBean.DropEntry getProduct(boolean deformed)
		{
			SBean.PetEatCFGS eat = GameData.getInstance().getPetEatCFG(id, lvl, deformed);
			if (eat != null) {
				SBean.DropEntry dropEntry = gs.getGameData().getDropTableRandomEntry(eat.productId);
				if (dropEntry != null)
					return dropEntry.kdClone();
			}
			
			return null;
		}
	}
	
	public class Friend
	{
		Friend(DBFriend friend)
		{
			this.friend = friend;
			if( this.friend == null )
			{
				this.friend = new DBFriend();
			}
		}
		
		int getFriendCount()
		{
			if( friend == null || friend.friends == null )
				return 0;
			return friend.getFriendCount();
		}
		
		boolean isFriend(int serverID, int roleID)
		{
			if( friend == null || friend.friends == null )
				return false;
			return friend.isFriend(serverID, roleID);
		}
		
		public List<SBean.DBFriendData> setSearchFriendsRes(SBean.SearchFriendsRes res)
		{
			List<SBean.DBFriendData> newList = new ArrayList<SBean.DBFriendData>();
			if( res.record.roleID.serverID <= 0 ) // searching
			{
				SBean.DBFriendSearching dfs = null;
				for(SBean.DBFriendSearching e : friend.friendsSearching)
				{
					if( e.openID.equals(res.record.openID) )
					{
						dfs = e;
						break;
					}
				}
				if( dfs == null )
				{
					dfs = new SBean.DBFriendSearching(res.record.openID, gs.getTime(), 0);
					friend.friendsSearching.add(dfs);
				}
			}
			else
			{
				if( res.record.roleID.serverID == gs.getConfig().id && res.record.roleID.roleID == Role.this.id )
					return newList;
				SBean.DBFriendData data = null;
				for(SBean.DBFriendData d : friend.friends)
				{
					if( d.openID.equals(res.record.openID) )
					{
						if( d.roleID.serverID == res.record.roleID.serverID && d.roleID.roleID == res.record.roleID.roleID )
						{
							d.cool = 0; // TODO
							data = d;
							break;
						}
					}
				}
				if( data == null )
				{
					// TODO sync prop
					SBean.DBFriendData newd = new SBean.DBFriendData(res.record.openID, res.record.roleID, (byte)0, gs.getTime(), 0, 0, res.prop); 
					friend.friends.add(newd);
					newList.add(newd.kdClone());
					//
					{
						Iterator<SBean.DBFriendMessage> iter = friend.msgsBack.iterator();
						while( iter.hasNext() )
						{
							SBean.DBFriendMessage m = iter.next();
							if( m.roleID.serverID == newd.roleID.serverID && m.roleID.roleID == newd.roleID.roleID )
							{
								iter.remove();
								friend.msgs.add(m);
							}
						}
					}
				}
				else
				{
					data.lastPropSyncTime = gs.getTime();
					data.prop = res.prop;
					newList.add(data.kdClone());
					//
					//
					{
						Iterator<SBean.DBFriendMessage> iter = friend.msgsBack.iterator();
						while( iter.hasNext() )
						{
							SBean.DBFriendMessage m = iter.next();
							if( m.roleID.serverID == data.roleID.serverID && m.roleID.roleID == data.roleID.roleID )
							{
								iter.remove();
								friend.msgs.add(m);
							}
						}
					}
				}
			}
			return newList;
		}
		public void clear()
		{			
			friend.baoziVitToday = 0;
			friend.pointToday = 0;
			friend.recallCount = 0;
			friend.breedRewardToday = 0;
			friend.petExpItemToday = 0;
			for(SBean.DBFriendData e : friend.friends)
			{
				e.flag &= ~(
						DBFriend.DAY_FLAG_BAOZI
						| DBFriend.DAY_FLAG_RECALL
						| DBFriend.DAY_FLAG_PET_EXP
						| DBFriend.DAY_FLAG_PET_BREED );
			}
			{
				//
				{
					Iterator<SBean.DBFriendMessage> iter = friend.msgs.iterator();
					while( iter.hasNext() )
					{
						SBean.DBFriendMessage m = iter.next();
						if( gs.getDayCommon(m.time) < dayCommon )
						{
							iter.remove();
						}
					}
				}
				{
					Iterator<SBean.DBFriendApply> iter = friend.applyLst.iterator();
					while( iter.hasNext() )
					{
						SBean.DBFriendApply m = iter.next();
						if( gs.getDayCommon(m.time) < dayCommon )
						{
							iter.remove();
						}
					}
				}
				//
			}
			friend.msgsBack.clear();
			friend.lastGetHeartTime = 0;
		}
		
		DBFriend copyDBFriend()
		{
			return friend.kdClone();
		}
		
		Set<String> getFriendsNoSearch()
		{
			Set<String> set = new HashSet<String>();
			set.addAll(friend.friendsDel);
			int now = gs.getTime();
			for(SBean.DBFriendSearching f : friend.friendsSearching)
			{
				{
					boolean bDoSearch = true;
					//
					if( f.cool == 0 )
						f.cool = 60;
					else if( f.cool < 3600 )
					{
						f.cool += 60;
					}
					else if( f.cool < 86400 )
					{
						f.cool += 3600;
					}
					else
						f.cool += 86400;
					
					if( f.cool > 86400 * 7)
						f.cool = 86400 * 7;
					//
					if( f.lastSearchTime + f.cool < now )
					{
						bDoSearch = true;
						f.lastSearchTime = now;
					}
					//
					if( bDoSearch )
					{
						// TODO
					}
					else
					{
						set.add(f.openID);
					}
				}
			}
			
			return set;
		}
		
		boolean mailBaozi(int serverID, int roleID)
		{
			if( serverID <= 0 )
				return false;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID )
				{
					if( (e.flag&DBFriend.DAY_FLAG_BAOZI) == 0 )
					{
						e.flag |= DBFriend.DAY_FLAG_BAOZI;
						e.haogan++;
						int pointLeft = cfg.pointMaxPerDay - friend.pointToday;
						if( pointLeft > 0 )
						{
							int p = cfg.pointPerBaozi;
							if( p > pointLeft )
								p = pointLeft;
							friend.pointToday += p;
							friend.point += p;
						}
						return true;
					}
					break;
				}
			}
			return false;
		}
		
		List<SBean.GlobalRoleID> mailAllBaozi()
		{
			List<SBean.GlobalRoleID> lst = new ArrayList<SBean.GlobalRoleID>();
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.roleID.serverID > 0 && e.roleID.roleID > 0 )
				{
					if( (e.flag&DBFriend.DAY_FLAG_BAOZI) == 0 )
					{
						e.flag |= DBFriend.DAY_FLAG_BAOZI;
						e.haogan++;
						int pointLeft = cfg.pointMaxPerDay - friend.pointToday;
						if( pointLeft > 0 )
						{
							int p = cfg.pointPerBaozi;
							if( p > pointLeft )
								p = pointLeft;
							friend.pointToday += p;
							friend.point += p;
						}
						lst.add(new SBean.GlobalRoleID(e.roleID.serverID, e.roleID.roleID));
					}
				}
			}
			return lst;
		}
		
		boolean mailPetExp(int serverID, int roleID)
		{
			if( serverID <= 0 )
				return false;
			if( friend.heart <= 0 )
				return false;
			--friend.heart;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID )
				{
					if( (e.flag&DBFriend.DAY_FLAG_PET_EXP) == 0 )
					{
						e.flag |= DBFriend.DAY_FLAG_PET_EXP;
						e.haogan++;
						int pointLeft = cfg.pointMaxPerDay - friend.pointToday;
						if( pointLeft > 0 )
						{
							int p = cfg.pointPerBaozi;
							if( p > pointLeft )
								p = pointLeft;
							friend.pointToday += p;
							friend.point += p;
						}
						return true;
					}
					break;
				}
			}
			return false;
		}
		
		int recall(int serverID, int roleID, TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( serverID <= 0 )
				return -1;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			
			int now = gs.getTime();
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID )
				{
					if( e.prop.lastLoginTime + cfg.minRecallDay*86400 < now )
					{
						if( (e.flag&DBFriend.DAY_FLAG_RECALL) == 0 )
						{
							e.flag |= DBFriend.DAY_FLAG_RECALL;
							
							if( friend.recallCount < cfg.maxRecall )
							{
								friend.recallCount++;
								for(SBean.FriendRecallLevelCFGS l : cfg.recallLevels)
								{
									if( e.prop.lvl <= l.lvlCeil )
									{
										commonFlowRecord.addChange(addMoney(l.reward)); // TODO
										return l.reward;
									}
								}
							}
							return 0;
						}
					}
					break;
				}
			}
			return -1;
		}
		
		int recallAll(List<String> openIDs, TLogger.CommonFlowRecord commonFlowRecord)
		{
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();			
			int now = gs.getTime();
			int moneyAll = 0;
			List<Integer> rewards = new ArrayList<Integer>();
			int canRecallCount = cfg.maxRecall - friend.recallCount;
			if( canRecallCount < 0 ) canRecallCount = 0;
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.roleID.serverID > 0 && e.prop.lastLoginTime + cfg.minRecallDay*86400 < now && (e.flag&DBFriend.DAY_FLAG_RECALL) == 0 )
				{
					e.flag |= DBFriend.DAY_FLAG_RECALL;
					openIDs.add(e.openID);
					for(SBean.FriendRecallLevelCFGS l : cfg.recallLevels)
					{
						if( e.prop.lvl <= l.lvlCeil )
						{
							rewards.add(-l.reward); // TODO
							break;
						}
					}
					if( friend.recallCount < cfg.maxRecall )
					{
						++friend.recallCount;
					}
				}
			}
			// TODO 锟斤拷 rewards 锟斤拷选锟斤拷锟斤拷锟斤拷 canRecallCount 锟斤拷 锟斤拷锟桔硷拷锟斤拷锟斤拷
			Collections.sort(rewards);
			int nR = 0;
			for(int e : rewards)
			{
				if( nR >= canRecallCount )
					break;
				moneyAll -= e;
				++nR;
			}
			//
			commonFlowRecord.addChange(addMoney(moneyAll)); // TODO
			return moneyAll;
		}
		
		int eatBaozi(int serverID, int roleID, int time, TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( serverID <= 0 )
				return -1;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendMessage e : friend.msgs)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID && e.time == time && e.flag == 0 && e.type == DBFriend.MSG_TYPE_BAOZI )
				{
					e.flag = 1;
					int vitLeft = cfg.vitBaoziMaxPerDay - friend.baoziVitToday;
					int eat = 0;
					if( vitLeft > 0 )
					{
						eat = cfg.vitPerBaozi;
						if( eat > vitLeft )
							eat = vitLeft;
						friend.baoziVitToday += eat;
						commonFlowRecord.addChange(addVit2(eat));
					}
					return eat;
				}
			}
			return -1;
		}
		
		short syncHeart()
		{
			short heartNow = friend.heart;
			//
			short max = GameData.getInstance().getPetCmnCFG().feedingFriendMax;
			if( heartNow >= max )
				return heartNow;
			int now = gs.getTime();
			int interval = GameData.getInstance().getPetCmnCFG().feedingFriendInterval; 
			int d = now - friend.lastGetHeartTime;
			if( d < interval )
				return heartNow;
			int n = d / interval;
			int l = d - n * interval;
			heartNow += n;
			if( heartNow > max || heartNow < 0 )
				heartNow = max;
			//
			friend.heart = (short)heartNow;
			friend.lastGetHeartTime = now - l;
			return friend.heart;
		}
		
		int takePetExp(int serverID, int roleID, int time, TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( serverID <= 0 )
				return -1;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendMessage e : friend.msgs)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID && e.time == time && e.flag == 0 && e.type == DBFriend.MSG_TYPE_PET_EXP )
				{
					e.flag = 1;
					short itemID = e.arg1;
					int itemCnt = e.arg2;
					if( friend.petExpItemToday < cfg.petExpItemMaxPerDay )
					{
						friend.petExpItemToday++;
						if( GameData.getInstance().getItemCFG(itemID) != null && itemCnt > 0 )
							commonFlowRecord.addChange(addItem(itemID, (short)itemCnt));
						return 1;
					}
					return 0;
				}
			}
			return -1;
		}
		
		int takePetBreedReward(int serverID, int roleID, int time, TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( serverID <= 0 )
				return -1;
			int eat = 0;
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			for(SBean.DBFriendMessage e : friend.msgs)
			{
				if( e.roleID.serverID == serverID && e.roleID.roleID == roleID && e.time == time && e.flag == 0 && e.type == DBFriend.MSG_TYPE_PET_BREED )
				{
					e.flag = 1;
					short petID = e.arg1;
					int breedReward = e.arg2;
					
					if( GameData.getInstance().getPetCFG(petID, false) != null && breedReward > 0 )
					{
						int rewardLeft = cfg.breedRewardMaxPerDay - friend.breedRewardToday;
						if( rewardLeft > 0 )
						{
							if( breedReward > rewardLeft )
								breedReward = rewardLeft;
							commonFlowRecord.addChange(addMoney(breedReward));
							eat = breedReward;
							friend.breedRewardToday += breedReward;
						}
					}
					return eat;
				}
			}
			return eat;
		}
		
		boolean takeAll(TLogger.CommonFlowRecord commonFlowRecord, List<SBean.DropEntry> itemLst, int[] rets, int[] snsCount)
		{
			SBean.FriendCFGS cfg = GameData.getInstance().getFriendCFG();
			int vitAll = 0;
			int moneyAll = 0;
			for(SBean.DBFriendMessage e : friend.msgs)
			{
				if( e.roleID.serverID > 0 && e.flag == 0 && e.type == DBFriend.MSG_TYPE_BAOZI )
				{
					e.flag = 1;
					int vitLeft = cfg.vitBaoziMaxPerDay - friend.baoziVitToday;
					int eat = 0;
					if( vitLeft > 0 )
					{
						eat = cfg.vitPerBaozi;
						if( eat > vitLeft )
							eat = vitLeft;
						friend.baoziVitToday += eat;
						commonFlowRecord.addChange(addVit2(eat));
						snsCount[0]++;
					}
					vitAll += eat;
				}
				else if( e.roleID.serverID > 0 && e.flag == 0 && e.type == DBFriend.MSG_TYPE_PET_BREED )
				{
					e.flag = 1;
					int moneyLeft = cfg.breedRewardMaxPerDay - friend.breedRewardToday;
					int eat = 0;
					if( moneyLeft > 0 )
					{
						eat = e.arg2;
						if( eat > moneyLeft )
							eat = moneyLeft;
						friend.breedRewardToday += eat;
						commonFlowRecord.addChange(addMoney(eat));
						snsCount[2]++;
					}
					moneyAll += eat;
				}
				else if( e.roleID.serverID > 0 && e.flag == 0 && e.type == DBFriend.MSG_TYPE_PET_EXP )
				{
					e.flag = 1;
					if( cfg.petExpItemMaxPerDay > friend.petExpItemToday )
					{
						short itemID = e.arg1;
						int itemCnt = e.arg2;
						if( GameData.getInstance().getItemCFG(itemID) != null && itemCnt > 0 && itemCnt <= 127)
						{
							commonFlowRecord.addChange(addItem(itemID, (short)itemCnt));
							itemLst.add(new SBean.DropEntry(GameData.COMMON_TYPE_ITEM, (byte)itemCnt, itemID));
							++friend.petExpItemToday;
							snsCount[1]++;
						}
					}
				}
			}
			rets[0] = vitAll;
			rets[1] = moneyAll;
			return true;
		}
		
		public List<SBean.FriendBreedPet> listBreed(byte ptype)
		{
			List<SBean.FriendBreedPet> lst = new ArrayList<SBean.FriendBreedPet>();
			for(SBean.DBFriendData e : friend.friends)
			{
				if( (e.flag&DBFriend.DAY_FLAG_PET_BREED) == 0 )
				{
					for(SBean.DBPetBrief p : e.prop.pets.petsG1)
					{
						SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(p.id, false);
						if( cfg != null && cfg.type == ptype )
						{
							SBean.FriendBreedPet bp = new SBean.FriendBreedPet();
							bp.openID = e.openID;
							bp.roleID = e.roleID;
							bp.pet = p.kdClone();
							lst.add(bp);
							break;
						}
					}
				}
			}
			return lst;
		}
		
		public boolean breed(byte ptype, String openID, SBean.GlobalRoleID grid)
		{
			for(SBean.DBFriendData e : friend.friends)
			{
				if( e.openID.equals(openID) && e.roleID.serverID == grid.serverID && e.roleID.roleID == grid.roleID )
				{
					if( (e.flag&DBFriend.DAY_FLAG_PET_BREED) == 0 )
					{
						e.flag |= DBFriend.DAY_FLAG_PET_BREED;
						for(SBean.DBPetBrief p : e.prop.pets.petsG1)
						{
							SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(p.id, false);
							if( cfg != null && cfg.type == ptype )
							{
								return true;
							}
						}
					}
					break;
				}
			}
			return false;
		}
		DBFriend friend;
	}
	
	public static class ShopInfo
	{
		public byte refreshTimes;
		public int timeLeft;
		public float discount;
		public List<SBean.ShopGoods> goods;
		public List<Byte> logs;
	}
	
	public static class ShopRandBrief
	{
		public byte flag1;
		public int timeout;
	}
	
	public class Shop
	{
		public Shop(byte shopType)
		{
			shop = new SBean.DBShop();
			this.shopType = shopType;
			cfg = GameData.getInstance().getShopCFG(shopType);
			map = GameData.getInstance().getShopCFGMap(shopType);
			doRefresh();
			shop.refreshTimes = 0;
		}
		
		public Shop(byte shopType, SBean.DBShop dbshop)
		{
			this.shopType = shopType;
			cfg = GameData.getInstance().getShopCFG(shopType);
			map = GameData.getInstance().getShopCFGMap(shopType);
			shop = dbshop;
		}
		
		public ShopRandBrief getRandBrief()
		{
			ShopRandBrief b = new ShopRandBrief();
			b.flag1 = shop.flag1;
			b.timeout = shop.timeout;
			return b;
		}
		
		public ShopInfo getInfo()
		{
			ShopInfo info = new ShopInfo();
			info.discount = 0;
			switch( shopType )
			{
			case GameData.SHOP_TYPE_NORMAL:
			case GameData.SHOP_TYPE_RAND1:
			case GameData.SHOP_TYPE_RAND2:
			case GameData.SHOP_TYPE_RICH_STONE:
				{
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_ARENA:
				{
					info.timeLeft = arenaState.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_MARCH:
				{
					info.timeLeft = marchState.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_FORCE:
				{
					info.timeLeft = forceInfo.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_RICH_POINT:
				{
					info.timeLeft = rich == null ? 0 : rich.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_RICH_GOLD:
				{
					info.timeLeft = rich == null ? 0 : rich.gold;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
					info.discount = getRichTechPlusEffect(GameData.RICH_TECH_TYPE_BLACK_SHOP);
				}
				break;
			case GameData.SHOP_TYPE_SUPER_ARENA:
				{
					info.timeLeft = superArena == null ? 0 :superArena.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_DUEL:
				{
					info.timeLeft = duel == null ? 0 :duel.point;
					info.refreshTimes = shop.refreshTimes;
					info.logs = new ArrayList<Byte>(shop.logs);
					info.goods = new ArrayList<SBean.ShopGoods>();
					for(short gid : shop.ids)
					{
						info.goods.add(map.get(gid));
					}
				}
				break;
			case GameData.SHOP_TYPE_BLESS:
			{
				info.refreshTimes = shop.refreshTimes;
				info.logs = new ArrayList<Byte>(shop.logs);
				info.goods = new ArrayList<SBean.ShopGoods>();
				for(short gid : shop.ids)
				{
					info.goods.add(map.get(gid));
				}
			}
			break;	
			default:
				throw new Error("impossible");
			}
			return info;
		}
		
		public void tryRefresh()
		{			
			boolean bRefresh = false;
			int oldStamp = shop.stamp;
			List<Short> times = cfg.refreshTime;
			
			int now = gs.getTime();
			
			int lastTimeChange = -1;
			for(short m : times)
			{
				int t = gs.getTimeByMinuteOffset(m);
				if( now >= t )
				{
					lastTimeChange = t;
				}
			}
			if( lastTimeChange < 0 )
			{
				lastTimeChange = gs.getTimeByMinuteOffset(times.get(times.size()-1).shortValue()) - 86400;
			}
			
			bRefresh = lastTimeChange > oldStamp;
			if( bRefresh )
			{
				doRefresh();
			}
		}
		
		public boolean userRefresh(TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( shop == null )
				return false;
			if( shop.refreshTimes >= 100 /*TODO*/ )
				return false;
			int index = shop.refreshTimes;
			if( index >= cfg.refreshPrice.size() )
				index = cfg.refreshPrice.size() - 1;
			int price = cfg.refreshPrice.get(index);
			switch( cfg.refreshCurUnit )
			{
			case GameData.CURRENCY_UNIT_MONEY:
				{
					if( money < price )
						return false;
					commonFlowRecord.addChange(useMoney(price));
				}
				break;
			case GameData.CURRENCY_UNIT_STONE:
				{
					if( stone < price )
						return false;
					commonFlowRecord.addChange(useStone(price));
				}
				break;
			case GameData.CURRENCY_UNIT_ARENA:
				{
					if( arenaState.point < price )
						return false;
					commonFlowRecord.addChange(useArena(price));
				}
				break;
			case GameData.CURRENCY_UNIT_MARCH:
				{
					if( marchState.point < price )
						return false;
					commonFlowRecord.addChange(useMarch(price));
				}
				break;
			case GameData.CURRENCY_UNIT_FORCE:
				{
					if ( forceInfo.point < price)
						return false;
					commonFlowRecord.addChange(useForce(price));
				}
				break;
			case GameData.CURRENCY_UNIT_RICH_POINT:
				{
					if ( rich == null || rich.point < price)
						return false;
					commonFlowRecord.addChange(useRich(price));
				}
				break;
			case GameData.CURRENCY_UNIT_RICH_GOLD:
				{
					if ( rich == null || rich.gold < price)
						return false;
					commonFlowRecord.addChange(richDelGold(price));
				}
				break;
			case GameData.CURRENCY_UNIT_SUPER_ARENA:
				{
					if ( superArena == null || superArena.point < price)
						return false;
					commonFlowRecord.addChange(useSuperArena(price));
				}
				break;
			case GameData.CURRENCY_UNIT_DUEL:
				{
					if ( duel == null || duel.point < price)
						return false;
					useDuelPoint(price);
				}
				break;
			default:
				break;
			}
			++shop.refreshTimes;
			doRefresh();
			commonFlowRecord.setEvent(TLog.AT_SHOP_REFRESH);
			commonFlowRecord.setArg(shopType, shop.refreshTimes);
			return true;
		}
		
		public boolean userSummon(TLogger.CommonFlowRecord commonFlowRecord)
		{
			if( shop == null )
				return false;
			if( shopType != GameData.SHOP_TYPE_RAND1 && shopType != GameData.SHOP_TYPE_RAND2 )
				return false;
			if( shop.flag1 == 1 )
				return false;
			if( Role.this.stone < cfg.randSummonStone )
				return false;
			commonFlowRecord.addChange(useStone(cfg.randSummonStone));
			shop.flag1 = 1;
			commonFlowRecord.setEvent(TLog.AT_SHOP_SUMMON);
			commonFlowRecord.setArg(shopType);
			return true;
		}
		
		public boolean buy(byte seq, byte type, short id, TLogger.TLogEvent tlogEvent)
		{
			if( shop == null )
				return false;
			if( seq <= 0 || seq > shop.ids.size() || seq > shop.logs.size() )
				return false;
			if( shop.logs.get(seq-1) > 0 )
				return false;
			short gid = shop.ids.get(seq-1);
			SBean.ShopGoods goods = map.get(gid);
			if( goods == null )
				return false;
			if( goods.item.type != type || goods.item.id != id )
				return false;
			int rprice = goods.price;
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			switch( goods.curUnit )
			{
			case GameData.CURRENCY_UNIT_MONEY:
				{
					if( money < goods.price )
						return false;
					commonFlowRecord.addChange(useMoney(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_STONE:
				{
					if( stone < goods.price )
						return false;
					commonFlowRecord.addChange(useStone(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_ARENA:
				{
					if( arenaState.point < goods.price )
						return false;
					commonFlowRecord.addChange(useArena(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_MARCH:
				{
					if( marchState.point < goods.price )
						return false;
					commonFlowRecord.addChange(useMarch(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_FORCE:
				{
					if (forceInfo.point < goods.price)
						return false;
					commonFlowRecord.addChange(useForce(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_RICH_POINT:
				{
					if (rich == null || rich.point < goods.price)
						return false;
					commonFlowRecord.addChange(useRich(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_RICH_GOLD:
				{
					rprice = GameData.getInstance().getDiscountPrice(goods.price
							, getRichTechPlusEffect(GameData.RICH_TECH_TYPE_BLACK_SHOP));
					if (rich == null || rich.gold < rprice)
						return false;
					commonFlowRecord.addChange(richDelGold(rprice));
				}
				break;
			case GameData.CURRENCY_UNIT_SUPER_ARENA:
				{
					if (superArena == null || superArena.point < goods.price)
						return false;
					commonFlowRecord.addChange(useSuperArena(goods.price));
				}
				break;
			case GameData.CURRENCY_UNIT_DUEL:
				{
					if (duel == null || duel.point < goods.price)
						return false;
					useDuelPoint(goods.price);
				}
				break;
			default:
				break;
			}
			commonFlowRecord.addChange(addDrop(goods.item));
			commonFlowRecord.setEvent(TLog.AT_SHOP_BUY);
			commonFlowRecord.setArg(shopType, type, id);
			tlogEvent.addCommonFlow(commonFlowRecord);
			
			TLogger.ShopRecord record = new TLogger.ShopRecord(Role.this.lvl);
			record.setShopType(shopType);
			record.setBuyInfo(type, id, 1, rprice, goods.curUnit);
			tlogEvent.addRecord(record);
			shop.logs.set(seq-1, (byte)1);
			return true;
		}
		
		public void clear()
		{
			shop.refreshTimes = 0;
			if( shopType == GameData.SHOP_TYPE_RAND1 || shopType == GameData.SHOP_TYPE_RAND2 )
				shop.flag2 = 0;
		}
		
		private void doRefresh()
		{
			shop.ids = new ArrayList<Short>();
			List<SBean.ShopGoodsGroup> groups = null;
			for(SBean.ShopGoodsLevelCFGS l : cfg.levels)
			{
				if( Role.this.lvl <= l.lvlCeil )
				{
					groups = l.groups;
					break;
				}
			}
			
			for(int i = 1; i < groups.size(); ++i)
			{
				SBean.ShopGoods goods = GameData.getInstance().shopRandSelect(groups.get(i));
				shop.ids.add(goods.id);
			}
			while( shop.ids.size() < cfg.refreshCount )
			{
				SBean.ShopGoods goods = GameData.getInstance().shopRandSelect(groups.get(0));
				shop.ids.add(goods.id);
			}
			
			if( shopType == GameData.SHOP_TYPE_NORMAL )
				Collections.shuffle(shop.ids);
			
			shop.logs = new ArrayList<Byte>();
			for(int i = 0; i < shop.ids.size(); ++i)
				shop.logs.add((byte)0);
			shop.stamp = gs.getTime();
		}
		
		public SBean.DBShop copyDBShop()
		{
			return shop.kdClone();
		}
		
		final byte shopType;
		final SBean.ShopCFGS cfg;
		final SBean.DBShop shop;
		final Map<Short, SBean.ShopGoods> map;
	}
	
	public class Mail
	{
		public Mail(DBMail dbMail)
		{
			if( dbMail == null )
				dbMail = new DBMail();
			dbmail = dbMail;
		}				
		
		public DBMail copyDBMail() // save to db
		{
			return dbmail.copy(gs.getTime());
		}
				
		DBMail dbmail;
	}
	
	public Role(int id, GameServer gs, int sid)
	{
		this.id = id;
		this.gs = gs;
		this.netsid = sid;
	}
	
	///////
	public Role fromDBRole(DBRole dbRole, DBMail dbMail, DBFriend dbFriend)
	{
		flag = dbRole.flag;
		dayCommon = dbRole.dayCommon;
		lvlDay = dbRole.lvlDay;
		int levellimit = gs.getRoleLevelLimit();
		if( lvlDay < 1 ) lvlDay = 1;
		else if( lvlDay > levellimit ) lvlDay = (short)levellimit;
		dayFlag = dbRole.dayFlag;
		helpFlag1 = dbRole.helpFlag1;
		helpFlag2 = dbRole.helpFlag2;
		name = dbRole.name;
		userName = dbRole.userName;
		openID = dbRole.openID;
		money = dbRole.money;
		stone = dbRole.stone;
		stonePayMidas = dbRole.stonePayMidas;
		stonePayAlpha = dbRole.stonePayAlpha;
		stoneUsedTotal = dbRole.stoneUsedTotal;
		monthlyCardStartTime = dbRole.monthlyCardStartTime;
		payLogs = dbRole.payLogs;
		payLvlState = dbRole.payLvlState;
		lvlVIP = dbRole.lvlVIP;
		vipRewards = new TreeSet<Byte>();
		vipRewards.addAll(dbRole.vipRewards);
		exp = dbRole.exp;
		lvl = dbRole.lvl;
		headIconID = dbRole.headIconID;
		
		mail = new Mail(dbMail);
		friend = new Friend(dbFriend);
		Map<String, Set<Integer>> exist = new HashMap<String, Set<Integer>>();
		List<SBean.DBFriendData> delFriends = new ArrayList<SBean.DBFriendData>();
		for(SBean.DBFriendData e : friend.friend.friends)
		{
			Set<Integer> existServers = exist.get(e.openID);
			if (existServers == null) {
				existServers = new HashSet<Integer>();
				exist.put(e.openID, existServers);
			}
			if (existServers.contains(e.roleID.serverID))
				delFriends.add(e);
			else
				existServers.add(e.roleID.serverID);
		}
		for (SBean.DBFriendData e : delFriends)
			friend.friend.friends.remove(e);

		taskPos = dbRole.taskPos;
		//
		if (dbRole.forceIconId == 0)
			dbRole.forceIconId = 27000;
		forceInfo = new ForceInfo(dbRole.forceID, dbRole.forcePoint, dbRole.forceContrib, dbRole.forceJoinTime, dbRole.forceName, dbRole.forceIconId, dbRole.forceMobai, dbRole.forceMobaiReward, dbRole.forceSuperMobai);
		
		vitUsedToday = dbRole.vitUsedToday;
		vit = dbRole.vit;
		vitRecoverStart = dbRole.vitRecoverStart;
		skillPoint = dbRole.skillPoint;
		skillPointRecoverStart = dbRole.skillPointRecoverStart;
		createTime = dbRole.createTime;
		lastLoginTime = dbRole.lastLoginTime;
		lastLogoutTime = dbRole.lastLogoutTime;
		checkinLog = dbRole.checkinLog;
		
		arenaState = dbRole.arenaState;
		arenaBestRank = dbRole.arenaBestRank;
		arenaLogs = dbRole.arenaLogs;
		arenaGenerals = dbRole.arenaGenerals;
		arenaPets = dbRole.arenaPets;
		superArena = null;
		if (!dbRole.superArena.isEmpty())
		{
			superArena = dbRole.superArena.get(0);
		}

		marchState = dbRole.marchState;
		marchGeneralStates = dbRole.marchGeneralStates;
		marchEnemyGeneralStates = dbRole.marchEnemyGeneralStates;
		if (dbRole.marchCurrentEnemies.size() == 0)
			marchCurrentEnemies = null;
		else
			marchCurrentEnemies = dbRole.marchCurrentEnemies;
		marchPointReward = dbRole.marchPointReward;
		marchPointDay = dbRole.marchPointDay;

		buyMoneyTimes = dbRole.buyMoneyTimes;
		buyVitTimes = dbRole.buyVitTimes;
		buySkillPointTimes = dbRole.buySkillPointTimes;
		
		for(SBean.DBCombatLog cl : dbRole.combatFightLogs)
			combatFightLogs.put(cl.id, cl.times);
		for(SBean.DBCombatLog cl : dbRole.combatResetLogs)
			combatResetLogs.put(cl.id, cl.times);
		combatScores = dbRole.combatScores;
		combatPos = new SBean.DBCombatPos[dbRole.combatPos.size()];
		for(int i = 0; i < combatPos.length; ++i)
			combatPos[i] = dbRole.combatPos.get(i);
		eggLogs = dbRole.eggLogs;
		combatEventLogs = dbRole.combatEventLogs;
		for(SBean.DBRoleItem it : dbRole.items){
			int count =it.count;
			
			if(count<=0){
				count=60000+count;
			}
			items.put(it.id, count);
		}
			
		for(SBean.DBItemDropLog e : dbRole.itemDropLogs)
			itemDropLogs.put(e.id, e.times);
		for(SBean.DBRoleEquip eq : dbRole.equips)
			equips.put(eq.id, eq.count);
		for(DBRoleGeneral dbg : dbRole.generals)
		{
			General g = new General(dbg);
			generals.put(g.id, g);
		}
		for(SBean.DBPet dbp : dbRole.pets)
		{
			Pet p = new Pet(dbp);
			pets.put(p.id, p);
		}
		for(SBean.DBPetDeform dbp : dbRole.petDeforms)
		{
			petDeforms.put(dbp.id, dbp);
		}
		for (Pet p : pets.values())
		{
			if (!petDeforms.containsKey(p.id)) {
				SBean.DBPetDeform dbp = new SBean.DBPetDeform(p.id, (byte)0, 0, (byte)0, 0, (byte)0, (byte)0, 0, (byte)0, new ArrayList<DropEntry>(), (byte)0, (byte)0, 0, 0, 0);
				checkDeformHappiness(dbp, gs);
				
				petDeforms.put(p.id, dbp);
			}
		}
		if( dbRole.petsTop.isEmpty() )
			petsTop = 0;
		else
			petsTop = dbRole.petsTop.get(0).shortValue();
		petsFeeding = dbRole.petsFeeding;
		shopNormal = new Shop(GameData.SHOP_TYPE_NORMAL, dbRole.shopNormal);
		shopArena = new Shop(GameData.SHOP_TYPE_ARENA, dbRole.shopArena);
		shopMarch = new Shop(GameData.SHOP_TYPE_MARCH, dbRole.shopMarch);
		shopRand1 = new Shop(GameData.SHOP_TYPE_RAND1, dbRole.shopRand1);
		shopRand2 = new Shop(GameData.SHOP_TYPE_RAND2, dbRole.shopRand2);
		if( dbRole.shopForce.isEmpty() )
		{
			shopForce = new Shop(GameData.SHOP_TYPE_FORCE);
		}
		else
		{
			shopForce = new Shop(GameData.SHOP_TYPE_FORCE, dbRole.shopForce.get(0));
		}
		if( dbRole.shopRich.size() <= SHOP_RICH_INDEX_STONE  )
		{
			shopRich[SHOP_RICH_INDEX_STONE] = new Shop(GameData.SHOP_TYPE_RICH_STONE);
		}
		else
		{
			shopRich[SHOP_RICH_INDEX_STONE] = new Shop(GameData.SHOP_TYPE_RICH_STONE, dbRole.shopRich.get(SHOP_RICH_INDEX_STONE));			
		}
		if( dbRole.shopRich.size() <= SHOP_RICH_INDEX_POINT  )
		{
			shopRich[SHOP_RICH_INDEX_POINT] = new Shop(GameData.SHOP_TYPE_RICH_POINT);
		}
		else
		{
			shopRich[SHOP_RICH_INDEX_POINT] = new Shop(GameData.SHOP_TYPE_RICH_POINT, dbRole.shopRich.get(SHOP_RICH_INDEX_POINT));			
		}
		if( dbRole.shopRich.size() <= SHOP_RICH_INDEX_GOLD  )
		{
			shopRich[SHOP_RICH_INDEX_GOLD] = new Shop(GameData.SHOP_TYPE_RICH_GOLD);
		}
		else
		{
			shopRich[SHOP_RICH_INDEX_GOLD] = new Shop(GameData.SHOP_TYPE_RICH_GOLD, dbRole.shopRich.get(SHOP_RICH_INDEX_GOLD));			
		}
		if (!dbRole.shopSuperArena.isEmpty())
		{
			shopSuperArena = new Shop(GameData.SHOP_TYPE_SUPER_ARENA, dbRole.shopSuperArena.get(0));
		}
		else
		{
			shopSuperArena = new Shop(GameData.SHOP_TYPE_SUPER_ARENA);
		}
		if (!dbRole.shopDuel.isEmpty())
		{
			shopDuel = new Shop(GameData.SHOP_TYPE_DUEL, dbRole.shopDuel.get(0));
		}
		else
		{
			shopDuel = new Shop(GameData.SHOP_TYPE_DUEL);
		}
		
		if (!dbRole.shopBless.isEmpty())
		{
			shopBless = new Shop(GameData.SHOP_TYPE_BLESS, dbRole.shopBless.get(0));
		}
		else
		{
			shopBless = new Shop(GameData.SHOP_TYPE_BLESS);
		}
		dailyTask2Logs = new TreeMap<Byte, SBean.DBDailyTask2Log>();
		for(SBean.DBDailyTask2Log e : dbRole.dailyTask2Logs)
		{
			dailyTask2Logs.put(e.id, e);
		}
		//System.out.println("dblog size = " + dbRole.dailyTask2Logs.size());
		dailyActivity = dbRole.dailyActivity;
		dailyActivityRewards = dbRole.dailyActivityRewards;
		
		banData = new LoginManager.RoleBanData(dbRole.banData);
		loginGiftMail.clear();
		loginGiftMail.addAll(dbRole.loginGiftMail);
		checkinGift.clear();
		for (SBean.DBRoleCheckinGift cg : dbRole.checkinGift)
		{
			checkinGift.put(cg.id, cg);
		}
		firstPayGift.clear();
		for (SBean.DBRoleFirstPayGift pg : dbRole.firstPayGift)
		{
			firstPayGift.put(pg.id, pg);
		}
		consumeGift.clear();
		for (SBean.DBRoleConsumeGift cg : dbRole.consumeGift)
		{
			consumeGift.put(cg.id, cg);
		}
		consumeRebate.clear();
		for (SBean.DBRoleConsumeRebate cg : dbRole.consumeRebate)
		{
			consumeRebate.put(cg.id, cg);
		}
		upgradeGift.clear();
		for (SBean.DBRoleUpgradeGift cg : dbRole.upgradeGift)
		{
			upgradeGift.put(cg.id, cg);
		}
		gatherGift.clear();
		for (SBean.DBRoleGatherGift cg : dbRole.gatherGift)
		{
			gatherGift.put(cg.id, cg);
		}
		payGift.clear();
		for (SBean.DBRolePayGift cg : dbRole.payGift)
		{
			payGift.put(cg.id, cg);
		}
		diskGift.clear();
		for (DBRoleDiskGift cg : dbRole.diskGift)
		{
			diskGift.put(cg.id, cg);
		}
		rechargeGift.clear();
		for (DBRoleRechargeGift cg : dbRole.rechargeGift)
		{
			rechargeGift.put(cg.id, cg);
		}
		limitedShop.clear();
		for (SBean.DBRoleLimitedShop cg : dbRole.limitedShop)
		{
			limitedShop.put(cg.id, cg);
		}
		loginGift.clear();
		for (SBean.DBRoleLoginGift cg : dbRole.loginGift)
		{
			loginGift.put(cg.id, cg);
		}
		tradingCenter.clear();
		for (SBean.DBRoleTradingCenter tc : dbRole.tradingCenter)
		{
			tradingCenter.put(tc.id, tc);
		}
		
		try {
			generalStoneInfo = null;
			if (!dbRole.generalStoneInfos.isEmpty())
			{
				generalStoneInfo = dbRole.generalStoneInfos.get(0);
				generalStone.clear();
				if(generalStoneInfo.generalStone!=null){
					for (SBean.DBGeneralStone cg : generalStoneInfo.generalStone)
					{
						generalStone.put(cg.id, cg);
					}
				}
			}			
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		
		
		expiratBossInfo = null;
		if (!dbRole.expiratBossInfo.isEmpty())
		{
			expiratBossInfo = dbRole.expiratBossInfo.get(0);				
		}			
		
		heroesBossInfo = null;
		if (!dbRole.heroesBossInfo.isEmpty())
		{
			heroesBossInfo = dbRole.heroesBossInfo.get(0);				
		}	

		boughtMoney = dbRole.boughtMoney;
		soulBoxLogs.clear();
		for (SBean.DBSoulBoxLog e : dbRole.soulBoxLogs)
		{
			soulBoxLogs.put(e.boxID, e);
		}
		feedTotal = dbRole.feedTotal;
		
		citys = new CityManager.RoleCitys(this, dbRole.citys);
		
		activity = dbRole.activity;
		activityRewards = dbRole.activityRewards;
		
		if (dbRole.rich.size() > 0)
			rich = dbRole.rich.get(0);
		else
			rich = null;//RichManager.createDefaultRichRole();
		//richMovementRecoverStart = dbRole.richMovementRecoverStart;
		
		lastForceWarID = dbRole.lastForceWarID;
		
		invitation = null;
		if (!dbRole.invitations.isEmpty())
		{
			invitation = dbRole.invitations.get(0);
		}
		qqVipRewards = dbRole.qqVipRewards;
		relations.clear();
		for (SBean.DBRelation r : dbRole.relations) {
			relations.put(r.id, r);
		}
		for (SBean.DBMarchMercGeneral g : dbRole.mercGenerals) {
			if (g.type == 0)
				mercGeneral = g;
			else if (g.type == 1)
				mercGeneralSura = g;
		}
		if (dbRole.duel.size() > 0)
			duel = dbRole.duel.get(0);
		if (dbRole.sura.size() > 0)
			sura = dbRole.sura.get(0);
		if (dbRole.optionalHotPoint.size() > 0)
			optionalHotPoint = dbRole.optionalHotPoint.get(0);
		relationStone = dbRole.relationStone;
		vipShow = dbRole.vipShow;
		treasureLimitCnt = null;
		if (dbRole.treasureLimitCnt.size() > 0)
		    treasureLimitCnt = dbRole.treasureLimitCnt.get(0);
		
//		if(dbRole.diskBet!=null){
//			diskBet = dbRole.diskBet;
//		}
//		
		diskBet = null;
		if (!dbRole.diskBet.isEmpty())
		{
			diskBet = dbRole.diskBet.get(0);				
		}
		
		diskBet2 = null;
		if (!dbRole.diskBet2.isEmpty())
		{
			diskBet2 = dbRole.diskBet2.get(0);				
		}
		
		diskBet3 = null;
		if (!dbRole.diskBet3.isEmpty())
		{
			diskBet3 = dbRole.diskBet3.get(0);				
		}
		
		bless = null;
		if (!dbRole.bless.isEmpty())
		{
			bless = dbRole.bless.get(0);				
		}
		
		headicon = null;
		if (!dbRole.headicon.isEmpty())
		{
			headicon = dbRole.headicon.get(0);				
		}
		
		//System.out.println("loadlog size = " + dailyTask2Logs.size());
		tryRefresh(gs.getTime());
		return this;
	}
	
	public Role createRole(String name, short headIconID, String userName, String openID)
	{
		int now = gs.getTime();
		createTime = now;
		flag = 0;
		dayCommon = -1;
		lvlDay = 1;
		dayFlag = 0;
		helpFlag1 = 0;
		helpFlag2 = 0;
		this.name = name;
		this.headIconID = headIconID;
		this.userName = userName;
		this.openID = openID;
		money = gs.getConfig().newRoleMoney;
		stone = 0;
		monthlyCardStartTime = 0;
		payLvlState = new ArrayList<Byte>();
		exp = 0;
		lvl = 1;
		upgradeTime = now;
		addVit1(getVitMax());
		addSkillPoint(getSkillPointMax());
		forceInfo = new ForceInfo(0, 0, 0, "", (short)0, 0, (short)0);
		dailyTask2Logs = new TreeMap<Byte, SBean.DBDailyTask2Log>();
		dailyActivity = 0;
		dailyActivityRewards = new ArrayList<Byte>();
		taskPos = new ArrayList<SBean.DBTaskPos>();
		payLogs = new ArrayList<DBRole.PayLog>();
		
		petsFeeding = new ArrayList<SBean.DBFeedingPet>();
		for(int i = 0; i < GameData.getInstance().getPetCmnCFG().poundCapInit; ++i)
		{
			petsFeeding.add(new SBean.DBFeedingPet((short)-1, (short)0, 0));
		}
		
		short firstGeneral = -1;
		if( gs.getConfig().newRoleGenerals != null )
		{
			for(int gid : gs.getConfig().newRoleGenerals )
			{
				if( gs.getGameData().testGeneral((short)gid) ) {
					generals.put((short)gid, new General((short)gid, (byte)1));
					if (firstGeneral < 0)
						firstGeneral = (short)gid;
				}
			}
		}
		if( gs.getConfig().newRoleEquips != null )
		{
			for(int gid : gs.getConfig().newRoleEquips )
			{
				if( gs.getGameData().testEquip((short)gid) )
					equips.put((short)gid, (short)100);
			}
		}
		if( gs.getConfig().newRoleItems != null )
		{
			for(int gid : gs.getConfig().newRoleItems )
			{
				if( gs.getGameData().testItem((short)gid) )
					items.put((short)gid, 100);
			}
		}
		combatScores = new ArrayList<SBean.DBCombatScore>();
		combatPos = new SBean.DBCombatPos[2];
		for(byte ctype = 0; ctype< 2; ++ctype)
		{
			combatPos[ctype] = new SBean.DBCombatPos((byte)(gs.getConfig().newRoleCombatStage-1), (byte)0);
			//
			while( true )
			{
				if( gs.getGameData().verifyCombatPos(ctype, combatPos[ctype]) )
					break;
				if( ! gs.getGameData().advCombatPos(ctype, combatPos[ctype]) )
					break;
			}
			//
		}
		shopNormal = new Shop(GameData.SHOP_TYPE_NORMAL);
		shopArena = new Shop(GameData.SHOP_TYPE_ARENA);
		shopMarch = new Shop(GameData.SHOP_TYPE_MARCH);
		shopRand1 = new Shop(GameData.SHOP_TYPE_RAND1);
		shopRand2 = new Shop(GameData.SHOP_TYPE_RAND2);
		shopForce = new Shop(GameData.SHOP_TYPE_FORCE);
		shopRich[SHOP_RICH_INDEX_STONE] = new Shop(GameData.SHOP_TYPE_RICH_STONE);
		shopRich[SHOP_RICH_INDEX_POINT] = new Shop(GameData.SHOP_TYPE_RICH_POINT);
		shopRich[SHOP_RICH_INDEX_GOLD] = new Shop(GameData.SHOP_TYPE_RICH_GOLD);
		shopSuperArena = new Shop(GameData.SHOP_TYPE_SUPER_ARENA);
		shopDuel = new Shop(GameData.SHOP_TYPE_DUEL);
		shopBless = new Shop(GameData.SHOP_TYPE_BLESS);
		mail = new Mail(new DBMail());
		friend = new Friend(new DBFriend());
		arenaState = new SBean.DBRoleArenaState();
		arenaBestRank = 0;
		arenaLogs = new ArrayList<SBean.DBRoleArenaLog>();
		arenaGenerals = new ArrayList<Short>();
		short general = -1;
		arenaGenerals.add(firstGeneral);
		for (int i = 1; i < 5; i ++)
			arenaGenerals.add(Short.valueOf(general));
		arenaPets = new ArrayList<Short>();
		short pet = -1;
		arenaPets.add(Short.valueOf(pet));
		marchState = new SBean.DBRoleMarchState();
		marchState.timesUsed = 0;
		marchGeneralStates = new ArrayList<SBean.DBRoleMarchGeneralState>();
		marchEnemyGeneralStates = new ArrayList<SBean.DBRoleMarchGeneralState>();
		marchCurrentEnemies = null;
		marchPointReward = 3;
		marchPointDay = 0;
		stonePayMidas = 0;
		stonePayAlpha = 0;
		stoneUsedTotal = 0;
		lvlVIP = 0;
		vipRewards = new TreeSet<Byte>();
		eggLogs.add(new SBean.DBEggLog(GameData.EGG_MONEY, (byte)0, (byte)0, 0, (byte)0, 0));
		eggLogs.add(new SBean.DBEggLog(GameData.EGG_STONE, (byte)0, (byte)0, 0, (byte)0, 0));
		
		banData = new LoginManager.RoleBanData();
		
		loginGiftMail.clear();
		checkinGift.clear();
		firstPayGift.clear();
		consumeGift.clear();
		consumeRebate.clear();
		upgradeGift.clear();
		gatherGift.clear();
		payGift.clear();
		diskGift.clear();
		rechargeGift.clear();
		limitedShop.clear();
		loginGift.clear();
		tradingCenter.clear();
		
		generalStoneInfo=null;
		generalStone.clear();
		
		boughtMoney = 0;
		soulBoxLogs.clear();
		feedTotal = 0;
		
		citys = new CityManager.RoleCitys(this);
		
		activity = 0;
		activityRewards = new ArrayList<Byte>();
		
		rich = null;//RichManager.createDefaultRichRole();
//		rich.map = new SBean.DBRichMap();
//		rich.map.buildings = new ArrayList<Byte>();
//		rich.map.objects = new ArrayList<SBean.DBRichMapObject>();
//		rich.lotery = new ArrayList<Byte>();
		//richMovementRecoverStart = 0;
		
		lastForceWarID = 0;
		
		invitation = null;
		
		qqVipRewards = 0;
		relations.clear();
		mercGeneral = null;
		mercGeneralSura = null;
		duel = null;
		sura = null;
		optionalHotPoint = null;
		relationStone = 0;
		vipShow = 1;

		treasureLimitCnt = null;
		
		return this;
	}
	
	public DBRole copyDBRoleWithoutLock()
	{
		DBRole dbRole = new DBRole();
		dbRole.flag = flag;
		dbRole.id = id;
		dbRole.dayCommon = dayCommon;
		dbRole.lvlDay = lvlDay;
		dbRole.dayFlag = dayFlag;
		dbRole.helpFlag1 = helpFlag1;
		dbRole.helpFlag2 = helpFlag2;
		dbRole.name = name;
		dbRole.userName = userName;
		dbRole.openID = openID;
		dbRole.money = money;
		dbRole.stone = stone;
		dbRole.stonePayMidas = stonePayMidas;
		dbRole.stonePayAlpha = stonePayAlpha;
		dbRole.stoneUsedTotal = stoneUsedTotal;
		dbRole.monthlyCardStartTime = monthlyCardStartTime;
		dbRole.payLogs = new ArrayList<DBRole.PayLog>();
		for(DBRole.PayLog pl : payLogs)
		{
			dbRole.payLogs.add(pl.ksClone());
		}
		dbRole.payLvlState = copyPayLvlStateWithoutLock();
		dbRole.lvlVIP = lvlVIP;
		dbRole.vipRewards = new ArrayList<Byte>();
		dbRole.vipRewards.addAll(vipRewards);
		dbRole.exp = exp;
		dbRole.lvl = lvl;
		dbRole.headIconID = headIconID;
		dbRole.vit = vit;
		dbRole.forceID = forceInfo.id;
		dbRole.forcePoint = forceInfo.point;
		dbRole.forceContrib = forceInfo.contrib;
		dbRole.forceJoinTime = forceInfo.joinTime;
		dbRole.forceName = forceInfo.name;
		dbRole.forceIconId = forceInfo.iconId;
		dbRole.forceMobai = new ArrayList<Integer>(forceInfo.mobai);
		dbRole.vitRecoverStart = vitRecoverStart;
		dbRole.skillPoint = skillPoint;
		dbRole.skillPointRecoverStart = skillPointRecoverStart;
		dbRole.createTime = createTime;
		dbRole.lastLoginTime = lastLoginTime;
		dbRole.lastLogoutTime = lastLogoutTime;
		dbRole.checkinLog = checkinLog.ksClone();
		dbRole.upgradeTime = upgradeTime;
		dbRole.buyMoneyTimes = buyMoneyTimes;
		dbRole.buyVitTimes = buyVitTimes;
		dbRole.buySkillPointTimes = buySkillPointTimes;
		dbRole.taskPos = copyTaskPosWithoutLock();
		dbRole.combatFightLogs = copyCombatFightLogsWithoutLock();
		dbRole.combatResetLogs = copyCombatResetLogsWithoutLock();
		dbRole.combatPos = copyCombatPosWithoutLock();
		dbRole.combatScores = copyCombatScoresWithoutLock();
		dbRole.items = copyItemsWithoutLock1();
		dbRole.itemDropLogs = copyItemDropLogsWithoutLock();
		dbRole.equips = copyEquipsWithoutLock();
		dbRole.generals = copyGeneralsWithoutLock();
		dbRole.pets = copyPetsWithoutLock();
		dbRole.petDeforms = copyPetDeformsWithoutLock(true, new byte[]{0});
		dbRole.petsTop = new ArrayList<Short>();
		if( petsTop > 0 )
			dbRole.petsTop.add(petsTop);
		dbRole.petsFeeding = copyFeedingPetsWithoutLock();
		dbRole.eggLogs = copyEggLogsWithoutLock();
		dbRole.arenaState = arenaState.ksClone();
		dbRole.arenaBestRank = arenaBestRank;
		dbRole.arenaLogs = copyDBArenaLogsWithoutLock();
		dbRole.arenaGenerals = copyArenaGeneralIdsWithoutLock();
		dbRole.arenaPets = copyArenaPetIdsWithoutLock();
		dbRole.superArena = new ArrayList<SBean.DBRoleSuperArena>();
		if (superArena != null)
		{
			dbRole.superArena.add(superArena.kdClone());
		}
		dbRole.marchState = marchState.ksClone();
		dbRole.marchGeneralStates = copyMarchGeneralStatesWithoutLock(false, false);
		dbRole.marchEnemyGeneralStates = copyMarchGeneralStatesWithoutLock(true, false);
		dbRole.marchCurrentEnemies = copyMarchCurrentEnemiesWithoutLock();
		dbRole.marchPointReward = marchPointReward;
		dbRole.marchPointDay = marchPointDay;
		dbRole.combatEventLogs = copyCombatEventLogsWithoutLock();
		dbRole.shopNormal = shopNormal.copyDBShop();
		dbRole.shopArena = shopArena.copyDBShop();
		dbRole.shopMarch = shopMarch.copyDBShop();
		dbRole.shopRand1 = shopRand1.copyDBShop();
		dbRole.shopRand2 = shopRand2.copyDBShop();
		dbRole.shopForce = new ArrayList<SBean.DBShop>();
		dbRole.shopForce.add(shopForce.copyDBShop());
		dbRole.shopRich = new ArrayList<SBean.DBShop>();
		for(int i = 0; i < SHOP_RICH_COUNT; ++i)
			dbRole.shopRich.add(shopRich[i].copyDBShop());
		dbRole.shopSuperArena = new ArrayList<SBean.DBShop>();
		if (shopSuperArena != null)
		{
			dbRole.shopSuperArena.add(shopSuperArena.copyDBShop());
		}
		dbRole.shopDuel = new ArrayList<SBean.DBShop>();
		if (shopDuel != null)
		{
			dbRole.shopDuel.add(shopDuel.copyDBShop());
		}
		dbRole.shopBless = new ArrayList<SBean.DBShop>();
		if (shopBless != null)
		{
			dbRole.shopBless.add(shopBless.copyDBShop());
		}
		
		dbRole.dailyTask2Logs = new ArrayList<SBean.DBDailyTask2Log>();
		for(SBean.DBDailyTask2Log e : dailyTask2Logs.values())
			dbRole.dailyTask2Logs.add(e.ksClone());
		//System.out.println("log size = " + dailyTask2Logs.size());
		dbRole.dailyActivity = dailyActivity;
		dbRole.dailyActivityRewards = new ArrayList<Byte>();
		for (Byte b : dailyActivityRewards)
			dbRole.dailyActivityRewards.add(b);
		
		dbRole.banData = banData.getDBData();
		
		dbRole.loginGiftMail = new ArrayList<Integer>();
		dbRole.loginGiftMail.addAll(loginGiftMail);
		dbRole.checkinGift = new ArrayList<SBean.DBRoleCheckinGift>();
		for (SBean.DBRoleCheckinGift cg : checkinGift.values())
		{
			dbRole.checkinGift.add(cg.kdClone());
		}
		dbRole.firstPayGift = new ArrayList<SBean.DBRoleFirstPayGift>();
		for (SBean.DBRoleFirstPayGift pg : firstPayGift.values())
		{
			dbRole.firstPayGift.add(pg.kdClone());
		}
		dbRole.consumeGift = new ArrayList<SBean.DBRoleConsumeGift>();
		for (SBean.DBRoleConsumeGift pg : consumeGift.values())
		{
			dbRole.consumeGift.add(pg.kdClone());
		}
		dbRole.consumeRebate = new ArrayList<SBean.DBRoleConsumeRebate>();
		for (SBean.DBRoleConsumeRebate pg : consumeRebate.values())
		{
			dbRole.consumeRebate.add(pg.kdClone());
		}
		dbRole.upgradeGift = new ArrayList<SBean.DBRoleUpgradeGift>();
		for (SBean.DBRoleUpgradeGift pg : upgradeGift.values())
		{
			dbRole.upgradeGift.add(pg.kdClone());
		}
		dbRole.gatherGift = new ArrayList<SBean.DBRoleGatherGift>();
		for (SBean.DBRoleGatherGift gg : gatherGift.values())
		{
			dbRole.gatherGift.add(gg.kdClone());
		}
		dbRole.payGift = new ArrayList<SBean.DBRolePayGift>();
		for (SBean.DBRolePayGift pg : payGift.values())
		{
			dbRole.payGift.add(pg.kdClone());
		}
		dbRole.diskGift = new ArrayList<SBean.DBRoleDiskGift>();
		for (SBean.DBRoleDiskGift pg : diskGift.values())
		{
			dbRole.diskGift.add(pg.kdClone());
		}
		
		dbRole.rechargeGift = new ArrayList<SBean.DBRoleRechargeGift>();
		for (SBean.DBRoleRechargeGift pg : rechargeGift.values())
		{
			dbRole.rechargeGift.add(pg.kdClone());
		}
		
		dbRole.limitedShop = new ArrayList<SBean.DBRoleLimitedShop>();
		for (SBean.DBRoleLimitedShop pg : limitedShop.values())
		{
			dbRole.limitedShop.add(pg.kdClone());
		}
		dbRole.loginGift = new ArrayList<SBean.DBRoleLoginGift>();
		for (SBean.DBRoleLoginGift pg : loginGift.values())
		{
			dbRole.loginGift.add(pg.kdClone());
		}
		dbRole.tradingCenter = new ArrayList<SBean.DBRoleTradingCenter>();
		for (SBean.DBRoleTradingCenter tc : tradingCenter.values())
		{
			dbRole.tradingCenter.add(tc.kdClone());
		}
		
		try {
			dbRole.generalStoneInfos = new ArrayList<SBean.DBGeneralStoneInfo>();
			if(generalStoneInfo!=null){
				generalStoneInfo.generalStone = new ArrayList<SBean.DBGeneralStone>();
				for (DBGeneralStone cg : generalStone.values())
				{
					generalStoneInfo.generalStone.add(cg.kdClone());
				}
				
				if(generalStoneInfo.generalSeyen==null){
					generalStoneInfo.generalSeyen = new ArrayList<SBean.DBRoleSeyen>();
				}
				
				dbRole.generalStoneInfos.add(generalStoneInfo.kdClone());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		dbRole.expiratBossInfo = new ArrayList<SBean.DBExpiratBossInfo>();
		if(expiratBossInfo!=null){		
			dbRole.expiratBossInfo.add(expiratBossInfo.kdClone());
		}
		
		dbRole.heroesBossInfo = new ArrayList<SBean.DBHeroesBossInfo>();
		if(heroesBossInfo!=null&&heroesBossInfo.ranks!=null&&heroesBossInfo.times!=null){		//
			try {
				heroesBossInfo.ranks.removeAll(Collections.singleton(null));
				heroesBossInfo.times.removeAll(Collections.singleton(null));
			} catch (Exception e1) {
				
				e1.printStackTrace();
			} 
			dbRole.heroesBossInfo.add(heroesBossInfo.kdClone());
		}
		
		
		dbRole.boughtMoney = boughtMoney;
		dbRole.soulBoxLogs = new ArrayList<SBean.DBSoulBoxLog>();
		for (SBean.DBSoulBoxLog e : soulBoxLogs.values())
		{
			dbRole.soulBoxLogs.add(e.ksClone());
		}
		
		dbRole.feedTotal = feedTotal;
		
		dbRole.citys = citys.getDBCitys().kdClone();
		
		dbRole.activity = activity;
		dbRole.activityRewards = new ArrayList<Byte>();
		for (Byte b : activityRewards)
			dbRole.activityRewards.add(b);
		
		dbRole.rich = new ArrayList<SBean.DBRichRole>();
		if (rich != null)
			dbRole.rich.add(rich.kdClone());
		//dbRole.richMovementRecoverStart = richMovementRecoverStart;
		
		dbRole.lastForceWarID = lastForceWarID;
		
		dbRole.invitations = new ArrayList<SBean.DBInvitation>();
		if (invitation != null)
		{
			dbRole.invitations.add(invitation.kdClone());
		}
		
		dbRole.qqVipRewards = qqVipRewards;
		dbRole.relations = new ArrayList<SBean.DBRelation>();
		for (SBean.DBRelation r : relations.values())
			dbRole.relations.add(r.kdClone());
		dbRole.mercGenerals = new ArrayList<SBean.DBMarchMercGeneral>();
		if (mercGeneral != null)
			dbRole.mercGenerals.add(mercGeneral.kdClone());
		if (mercGeneralSura != null)
			dbRole.mercGenerals.add(mercGeneralSura.kdClone());
		if (duel != null)
			dbRole.duel.add(duel.kdClone());
		if (sura != null)
			dbRole.sura.add(sura.kdClone());
		if (optionalHotPoint != null)
			dbRole.optionalHotPoint.add(optionalHotPoint.kdClone());
		dbRole.relationStone = relationStone;
		dbRole.vipShow = vipShow;

		dbRole.treasureLimitCnt = new ArrayList<SBean.DBTreasureLimitCnt>();
		if (treasureLimitCnt != null)
		    dbRole.treasureLimitCnt.add(treasureLimitCnt.kdClone());

		dbRole.diskBet = new ArrayList<SBean.DBDiskBetInfo>();
		if(diskBet!=null){		
			dbRole.diskBet.add(diskBet.kdClone());
		}
		dbRole.diskBet2 = new ArrayList<SBean.DBDiskBetInfo>();
		if(diskBet2!=null){		
			dbRole.diskBet2.add(diskBet2.kdClone());
		}
		dbRole.diskBet3 = new ArrayList<SBean.DBDiskBetInfo>();
		if(diskBet3!=null){		
			dbRole.diskBet3.add(diskBet3.kdClone());
		}
		
		dbRole.bless = new ArrayList<SBean.DBBlessInfo>();
		if(bless!=null){		
			dbRole.bless.add(bless.kdClone());
		}
		
		dbRole.headicon = new ArrayList<SBean.DBHeadIcon>();
		if(headicon!=null){		
			dbRole.headicon.add(headicon.kdClone());
		}
		
		lastSaveTime = System.currentTimeMillis();
		return dbRole;
	}
	
	public List<SBean.DBRoleArenaLog> copyDBArenaLogsWithoutLock()
	{
		List<SBean.DBRoleArenaLog> lst = new ArrayList<SBean.DBRoleArenaLog>();
		Iterator<SBean.DBRoleArenaLog> iter = arenaLogs.iterator();
		while( iter.hasNext() )
		{
			SBean.DBRoleArenaLog e = iter.next();
			if( e.time + 86400 < gs.getTime() )
			{
				iter.remove();
			}
			else
				lst.add(e.ksClone());
		}
		return lst;
	}

	public List<Short> copyArenaGeneralIdsWithoutLock()
	{
		List<Short> lst = new ArrayList<Short>();
		Iterator<Short> iter = arenaGenerals.iterator();
		while( iter.hasNext() )
		{
			Short e = iter.next();
			lst.add(new Short(e.shortValue()));
		}
		return lst;
	}
	
	public List<Short> copyArenaPetIdsWithoutLock()
	{
		List<Short> lst = new ArrayList<Short>();
		Iterator<Short> iter = arenaPets.iterator();
		while( iter.hasNext() )
		{
			Short e = iter.next();
			lst.add(new Short(e.shortValue()));
		}
		return lst;
	}
	
	public List<DBRoleGeneral> copyGeneralsWithoutLock(List<Short> ids)
	{
		List<DBRoleGeneral> lst = new ArrayList<DBRoleGeneral>();
		Iterator<Short> iter = ids.iterator();
		while( iter.hasNext() )
		{
			Short e = iter.next();
			General g = generals.get(e);
			if (g != null) {
				DBRoleGeneral copyDBGeneral = g.copyDBGeneralWithoutLock();
				changeSeyenValue(copyDBGeneral);			
				lst.add(copyDBGeneral);
			}
		}
		return lst;
	}

	private void changeSeyenValue(DBRoleGeneral copyDBGeneral) {
		if(copyDBGeneral!=null){
			copyDBGeneral.generalStone = copyGeneralStoneWithoutLock(copyDBGeneral.id);
			if(copyDBGeneral.generalSeyen!=null&&copyDBGeneral.generalSeyen.size()>0&&copyDBGeneral.generalSeyen.get(0)!=null){
				copyDBGeneral.generalSeyen.get(0).seyenTotal=this.getRoleGeneralSeyenTotal();
			}
			
		}
	}
	
	public List<SBean.DBPetBrief> copyPetsWithoutLock(List<Short> ids)
	{
		List<SBean.DBPetBrief> lst = new ArrayList<SBean.DBPetBrief>();
		Iterator<Short> iter = ids.iterator();
		while( iter.hasNext() )
		{
			Short e = iter.next();
			Pet p = pets.get(e);
			if (p != null) {
				SBean.DBPetDeform deform = petDeforms.get(e);
				byte deformStage = 0;
				if (deform != null)
					deformStage = deform.deformStage;
				lst.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
			}
		}
		return lst;
	}
	
	public List<DBRoleGeneral> copyArenaGeneralsWithoutLock()
	{
		return copyGeneralsWithoutLock(arenaGenerals);
	}
	
	public List<SBean.DBPetBrief> copyArenaPetsWithoutLock()
	{
		return copyPetsWithoutLock(arenaPets);
	}
	
	public List<SBean.DBRoleMarchGeneralState> copyMarchGeneralStatesWithoutLock(boolean enemy, boolean useMerc)
	{
		List<SBean.DBRoleMarchGeneralState> lst = new ArrayList<SBean.DBRoleMarchGeneralState>();
		List<SBean.DBRoleMarchGeneralState> states = enemy ? marchEnemyGeneralStates : marchGeneralStates;
		Iterator<SBean.DBRoleMarchGeneralState> iter = states.iterator();
		while( iter.hasNext() )
		{
			SBean.DBRoleMarchGeneralState e = iter.next();
			if (useMerc && mercGeneral != null && mercGeneral.general.id == e.id)
				continue;
			if (e != null)
				lst.add(e.ksClone());
		}
		if (useMerc && mercGeneral != null && mercGeneral.status.id == mercGeneral.general.id)
			lst.add(mercGeneral.status);
		return lst;
	}

	public List<SBean.DBRoleMarchEnemy> copyMarchCurrentEnemiesWithoutLock()
	{
		List<SBean.DBRoleMarchEnemy> lst = new ArrayList<SBean.DBRoleMarchEnemy>();
		if (marchCurrentEnemies == null)
			return lst;
		Iterator<SBean.DBRoleMarchEnemy> iter = marchCurrentEnemies.iterator();
		while( iter.hasNext() )
		{
			SBean.DBRoleMarchEnemy e = iter.next();
			if (e != null) {
				if (e.generals == null)
					e.generals = new ArrayList<DBRoleGeneral>();
				if (e.pets == null)
					e.pets = new ArrayList<DBPetBrief>();
				lst.add(e.kdClone());
			}
		}
		return lst;
	}

	public List<SBean.DBCombatScore> copyCombatScoresWithoutLock()
	{
		List<SBean.DBCombatScore> lst = new ArrayList<SBean.DBCombatScore>();
		for(SBean.DBCombatScore s : combatScores)
			lst.add(s.kdClone());
		return lst;
	}
	
	//////////
	
	private int getUpgradeExp()
	{
		return gs.getGameData().getUpgradeExp(lvl);
	}
	
	public short getVitMax()
	{
		return gs.getGameData().getVitMax(lvl);
	}
	
	public short getSkillPointMax()
	{
		return gs.getGameData().getSkillPointMax(lvlVIP);
	}
	
	public void setLvl(short lvl)
	{
		this.lvl = lvl;
		vit = getVitMax();
	}
	
	public SBean.RoleBrief copyBriefWithoutLock()
	{
//		if(this.headicon!=null&&headicon.headicon!=null){
//			List<Short> rev = new ArrayList<Short>();
//			for(DBHeadIconList icon :headicon.headicon){
//				HeadIconCFGS cfg =gs.gameData.getHeadiconCFG(icon.headid);
//				if(cfg==null){
//					continue;
//				}
//				if(cfg.etime!=-1&&icon.headtime+cfg.etime*24*3600<=gs.getTime()){
//					rev.add(icon.headid);
//				}
//			}
//			
//			if(rev.size()>0){
//				for(short icon1 : rev){
//					if(icon1==headicon.headcur){
//						headIconID = (short)(headIconID - icon1*100);
//						headicon.headcur =0;
//						for(General general : generals.values()){
//							if(general!=null){								
//								general.headicon=(byte)0;	
//							}
//
//						}
//					}
//					headicon.headicon.remove((Short)icon1);
//					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
//					gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_HEAD_ICON_DELETE, "", ""+(short)icon1.headid, 0, true, attLst);
//				}
//			}
//		}
		checkHeadTime();
		
		if(lvl>=90){
			HeadIconCFGS cfg =gs.gameData.getHeadiconCFG((short)1);
			if(cfg!=null){
				if(getItemCount((short)cfg.type) < 1 ){
					boolean add = false;
					if(this.headicon!=null&&headicon.headicon!=null){
						
						if(!headicon.headicon.contains(cfg.sid)){
							SBean.DBHeadIconList list = new SBean.DBHeadIconList(cfg.sid,gs.getTime());
							add =true;//headicon.headicon.add(list);
						}
					}else{
						if(headicon==null){
							headicon = new DBHeadIcon((short)0,new  ArrayList<DBHeadIconList>(),0,(short)0,0,0);
						}
						if(!headicon.headicon.contains(cfg.sid)){
							SBean.DBHeadIconList list = new SBean.DBHeadIconList(cfg.sid,gs.getTime());
							add =true;//headicon.headicon.add(list);
						}
					}

					if(add&&headicon.mail==(short)0){
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						attLst.add(new SBean.DropEntryNew((byte) 0 ,(short)cfg.type, 1));
						gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_HEAD_ICON, "", ""+(short)cfg.type, 0, true, attLst);
						headicon.mail=(short)1;
					}
					
				}
			}
			
		}
		
		
		
		return new SBean.RoleBrief(id, dayCommon, name, flag, helpFlag1, helpFlag2, money, stone, exp, vit, lvl, headIconID,
				vitRecoverStart, skillPoint, skillPointRecoverStart, gs.getTime(),
				getArenaGeneralsPowerWithoutLock() + getArenaPetsPowerWithoutLock(), buyMoneyTimes, buyVitTimes, buySkillPointTimes);
	}
	
	public synchronized LoginManager.RoleBriefCache getCacheDataWithLock()
	{
		return getCacheDataWithoutLock();
	}
	
	public void setHelpFlag(int hid)
	{
		if( hid <= 32 )
			helpFlag1 |= (1<<(hid-1));
		else
			helpFlag2 |= (1<<(hid-32-1));
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
	    TLogger.GuideRecord record = new TLogger.GuideRecord(this.lvl, hid);
	    tlogEvent.addRecord(record);
	    gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public synchronized int getPVPStateWithLock()
	{
		int f = 0;
		return f;
	}
	
	public LoginManager.RoleBriefCache getCacheDataWithoutLock()
	{
		return new LoginManager.RoleBriefCache(lvl, headIconID, getArenaGeneralsPowerWithoutLock() + getArenaPetsPowerWithoutLock(), name, forceInfo.name, lastLoginTime, activity);
	}
	
	public List<SBean.DBEggLog> copyEggLogsWithoutLock()
	{
		List<SBean.DBEggLog> logs = new ArrayList<SBean.DBEggLog>();
		for(SBean.DBEggLog e : eggLogs)
			logs.add(e.ksClone());
		return logs;
	}
	
	public List<SBean.DBCombatEventLog> copyCombatEventLogsWithoutLock()
	{
		List<SBean.DBCombatEventLog> logs = new ArrayList<SBean.DBCombatEventLog>();
		for(SBean.DBCombatEventLog e : combatEventLogs)
			logs.add(e.ksClone());
		return logs;
	}
	
	public synchronized List<SBean.DBEggLog> copyEggLogsWithLock()
	{
		return copyEggLogsWithoutLock();
	}
	
	public List<SBean.DBTaskPos> copyTaskPosWithoutLock()
	{
		List<SBean.DBTaskPos> logs = new ArrayList<SBean.DBTaskPos>();
		for(SBean.DBTaskPos e : taskPos)
			logs.add(e.ksClone());
		return logs;
	}
	
	public List<DBGeneralStone> copyGeneralStoneWithoutLock()
	{
		List<DBGeneralStone> gStone = new ArrayList<SBean.DBGeneralStone>();
		try {
						
			for (DBGeneralStone cg : generalStone.values())
			{
				if(cg.gid==0){
					continue;
				}
				gStone.add(cg.kdClone());
			}
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return gStone;
	}
	
	public int getRoleGeneralSeyenTotal()
	{
		if(generalStoneInfo==null){
			return 0;
		}			
		if(generalStoneInfo.generalSeyen==null||generalStoneInfo.generalSeyen.size()==0){
			return 0;
		}
		DBRoleSeyen m =generalStoneInfo.generalSeyen.get(0);
		if(m==null){
			return 0;
		}
		return m.seyenTotal;
	}
	
	public int getRoleSeyen()
	{
		if(generalStoneInfo==null){
			return 0;
		}			
		if(generalStoneInfo.generalSeyen==null||generalStoneInfo.generalSeyen.size()==0){
			return 0;
		}
		DBRoleSeyen m =generalStoneInfo.generalSeyen.get(0);
		if(m==null){
			return 0;
		}
		return m.seyen;
	}
	
	public List<DBGeneralStone> copyGeneralStoneWithoutLock(short gid)
	{
		List<DBGeneralStone> gStone = new ArrayList<SBean.DBGeneralStone>();
		try {
						
			for (DBGeneralStone cg : generalStone.values())
			{
				if(cg.gid!=gid){
					continue;
				}
				gStone.add(cg.kdClone());
			}
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return gStone;
	}
	
	public synchronized DBFriend copyDBFriendWithLock()
	{
		return friend.copyDBFriend();
	}
	
	public synchronized List<SBean.DBTaskPos> copyTaskPosWithLock()
	{
		return copyTaskPosWithoutLock();
	}
	
	public List<SBean.DBCombatLog> copyCombatFightLogsWithoutLock()
	{
		List<SBean.DBCombatLog> logs = new ArrayList<SBean.DBCombatLog>();
		for(Map.Entry<Short, Byte> e : combatFightLogs.entrySet())
			logs.add(new SBean.DBCombatLog(e.getKey(), e.getValue()));
		return logs;
	}
	
	public List<SBean.DBCombatLog> copyCombatResetLogsWithoutLock()
	{
		List<SBean.DBCombatLog> logs = new ArrayList<SBean.DBCombatLog>();
		for(Map.Entry<Short, Byte> e : combatResetLogs.entrySet())
			logs.add(new SBean.DBCombatLog(e.getKey(), e.getValue()));
		return logs;
	}
	
	public List<SBean.DBCombatPos> copyCombatPosWithoutLock()
	{
		List<SBean.DBCombatPos> pos = new ArrayList<SBean.DBCombatPos>();
		for(SBean.DBCombatPos p : combatPos)
			pos.add(p);
		return pos;
	}
	
	public List<DBRoleGeneral> copyGeneralsWithoutLock()
	{
		List<DBRoleGeneral> lst = new ArrayList<DBRoleGeneral>();
		for(General g : generals.values())
		{
			lst.add(g.copyDBGeneralWithoutLock());
		}
		return lst;
	}
	
	public List<SBean.DBPet> copyPetsWithoutLock()
	{
		List<SBean.DBPet> lst = new ArrayList<SBean.DBPet>();
		for(Pet p : pets.values())
		{
			lst.add(p.copyDBPetWithoutLock());
		}
		return lst;
	}
	
	public List<SBean.DBPetDeform> copyPetDeformsWithoutLock(boolean includeVirt, byte[] takeTimes)
	{
		List<SBean.DBPetDeform> lst = new ArrayList<SBean.DBPetDeform>();
		for(SBean.DBPetDeform p : petDeforms.values())
		{
			boolean add = true;
			if (p.id == VIRT_PET_ID) {
				takeTimes[0] = p.takeTimes;
				if (!includeVirt)
					add = false;
			}
			
			if (add)
				lst.add(p.kdClone());
		}
		return lst;
	}
	
	public List<SBean.DBFeedingPet> copyFeedingPetsWithoutLock()
	{
		List<SBean.DBFeedingPet> lst = new ArrayList<SBean.DBFeedingPet>();
		for(SBean.DBFeedingPet p : petsFeeding)
		{
			lst.add(p.ksClone());
		}
		return lst;
	}
	
	public List<SBean.DBRoleItem2> copyItemsWithoutLock()
	{
		List<SBean.DBRoleItem2> lst = new ArrayList<SBean.DBRoleItem2>();
		for(Entry<Short, Integer> e : items.entrySet())
		{
			lst.add(new SBean.DBRoleItem2(e.getKey(), e.getValue()));
		}
		return lst;
	}
	
	public List<SBean.DBRoleItem> copyItemsWithoutLock1()
	{
		List<SBean.DBRoleItem> lst = new ArrayList<SBean.DBRoleItem>();
		for(Entry<Short, Integer> e : items.entrySet())
		{
			short count = 0;
			if(e.getValue()==0){
				items.remove(e.getKey());
				gs.getLogger().warn("copyItemsWithoutLock remove" +e.getKey()+";"+e.getValue());
			}
			if(e.getValue()>30000&&e.getValue()<=60000){
				count = (short) (e.getValue()-60000);
			}else if(e.getValue()<=30000){
				count = (short) (e.getValue()+0);
			}
			if(count==0){
				gs.getLogger().warn("copyItemsWithoutLock " +e.getKey()+";"+e.getValue());
			}
			lst.add(new SBean.DBRoleItem(e.getKey(), count));
		}
		return lst;
	}
	
	public List<SBean.DBItemDropLog> copyItemDropLogsWithoutLock()
	{
		List<SBean.DBItemDropLog> lst = new ArrayList<SBean.DBItemDropLog>();
		for(Map.Entry<Short, Short> e : itemDropLogs.entrySet())
		{
			lst.add(new SBean.DBItemDropLog(e.getKey(), e.getValue()));
		}
		return lst;
	}
	
	public List<SBean.DBRoleEquip> copyEquipsWithoutLock()
	{
		List<SBean.DBRoleEquip> lst = new ArrayList<SBean.DBRoleEquip>();
		for(Map.Entry<Short, Short> e : equips.entrySet())
		{
			lst.add(new SBean.DBRoleEquip(e.getKey(), e.getValue()));
		}
		return lst;
	}
	
	static class CurrentCombat
	{
		public SBean.CombatCFGS cfg;
		public byte ctype;
		public byte cindex;
		public byte bindex;
		public SBean.CombatBonus cb;
		public GameData.ItemDropLogTrans itemDropLogTrans;
		public short cid;
		public boolean bCurrent;
		public int startTime;
	}
	
	static class CurrentCombatEvent
	{
		public byte ceid;
		public SBean.CombatCFGS cfg;;
		public SBean.CombatBonus cb;
		public short cid;
		public int startTime;
	}
	
	public SBean.DBCombatEventLog combatEventSync(byte ceid)
	{
		SBean.CombatEventCFGS cecfg = GameData.getInstance().getCombatEventCFG(ceid);
		if( cecfg == null )
			return null;
		if( ! gs.testWeekDays(cecfg.weekdays) )
			return null;
		synchronized( this )
		{
			if( lvl < cecfg.lvlReq )
				return null;
			dayRefresh();
			for(SBean.DBCombatEventLog e : combatEventLogs)
			{
				if( e.id == ceid )
				{
					return e.ksClone();
				}
			}
		}
		return new SBean.DBCombatEventLog(ceid, (byte)0, 0);
	}
	
	public boolean combatEventStart(byte ceid, byte celvl, short cid, SBean.CombatBonus[] cbs, int clientStartTime, List<Short> generalIDs)
	{
		SBean.CombatEventCFGS cecfg = GameData.getInstance().getCombatEventCFG(ceid);
		if( cecfg == null )
			return false;
		if( ! gs.testWeekDays(cecfg.weekdays) )
			return false;
		if( celvl < 1 || celvl > cecfg.combatIDs.size() || cecfg.combatIDs.get(celvl-1).shortValue() != cid )
			return false;
		SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(cid);
		if( cfg == null )
			return false;
		Set<Short> gids = new TreeSet<Short>(generalIDs);
		if( /*gids.isEmpty() ||*/ gids.size() > GameData.GENERAL_TEAM_SIZE || gids.size() != generalIDs.size() )
			return false;
		BattleGeneralInfo[] generalsinfo = new BattleGeneralInfo[5];
		for (int i = 0; i < 5; ++i)
		{
			generalsinfo[i] = new BattleGeneralInfo();
		}
		BattlePetInfo petinfo = new BattlePetInfo();
		int index = 0;
		int power = 0;
		for(short gid : generalIDs)
		{
			General g = generals.get(gid);
			if( g == null )
			{
				return false;
			}
			else
			{
				power += calcGeneralPower(gid);
				if (index < 5)
				{
					BattleGeneralInfo info = generalsinfo[index];
					info.id = g.id;
					info.lvl = g.lvl;
					info.evoLvl = g.evoLvl;
					info.advlvl = g.advLvl;	
				}
			}
			index++;
		}

		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.SecRoundStartRecord secRecord = new  TLogger.SecRoundStartRecord(this.vit);
		switch (ceid)
		{
		case 1:
			secRecord.setChibi(cid, TLog.SEC_CHIBI_GRASSBOARD);
			break;
		case 2:
			secRecord.setChibi(cid, TLog.SEC_CHIBI_CHIBI);
			break;
		case 3:
			secRecord.setBagua(cid, TLog.SEC_BAGUA_LIFE);
			break;
		case 4:
			secRecord.setBagua(cid, TLog.SEC_BAGUA_DEATH);
			break;
		default:
			return false;
		}
		int activityType = GameActivities.CHIBI_BATTLE;
		int generalBattleType = TLog.GENERALEBATTLE_CHIBI;
		if ( ceid == 1  || ceid == 2 )
		{
			activityType = GameActivities.CHIBI_BATTLE;
			generalBattleType = TLog.GENERALEBATTLE_CHIBI;
		}
		else if( ceid == 3  || ceid == 4 || ceid == 5 )
		{
			activityType = GameActivities.BAGUAZHEN_BATTLE;
			generalBattleType = TLog.GENERALEBATTLE_BAGUAZHEN;
		}
		synchronized( this )
		{
			if ( ceid == 1  || ceid == 2 )
				if (isBanChibi())
					return false;
			dayRefresh();
			if( lvl < cfg.lvlReq || vit < cfg.vitCostStart + cfg.vitCostWin )
				return false;
			if( lvl < cecfg.lvlReq )
				return false;
			if( cfg.maxTimes > 0 && combatFightLogs.containsKey(id) && combatFightLogs.get(id).byteValue() >= cfg.maxTimes )
				return false;
			//
			{
				SBean.DBCombatEventLog log = null;
				for(SBean.DBCombatEventLog e : combatEventLogs)
				{
					if( ceid == e.id )
					{
						log = e;
						break;
					}
				}
				if( log == null )
				{
					log = new SBean.DBCombatEventLog(ceid, (byte)0, 0);
					combatEventLogs.add(log);
				}
				if( log.timesToday >= cecfg.maxTimes )
					return false;
				if( log.lastTime + cecfg.cool * 60 >= gs.getTime() )
					return false;
			}
			//
			commonFlowRecord.addChange(subVit(cfg.vitCostStart));
			curEventCombat = new CurrentCombatEvent();
			curEventCombat.cid = cid;
			curEventCombat.cfg = cfg;
			curEventCombat.ceid = ceid;
			curEventCombat.startTime = gs.getTime();
			byte nTimesExp = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_EXPERIENCE);
			byte nTimesStone = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_DIAMOND);
			byte nTimesMoney = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_MONEY);
			byte nTimesItem = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_ITEM);
			//gs.getLogger().error("nTimesExp="+nTimesExp+", nTimesMoney="+nTimesMoney+", nTimesItem="+nTimesItem);
			SBean.DropEntry extraDrop = gs.getGameActivities().getExtraDropActivity().getExtraDrop(activityType);
			GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, nTimesExp, nTimesStone, nTimesMoney, nTimesItem, extraDrop, null, null, (byte)0); 
			curEventCombat.cb = bonusInfo.cb;
			cbs[0] = curEventCombat.cb;
			commonFlowRecord.setEvent(activityType == GameActivities.CHIBI_BATTLE ? TLog.AT_CHIBI_START : TLog.AT_BAGUAZHENG_START);
			commonFlowRecord.setArg(ceid, cid);
			secRecord.setStartTime(curEventCombat.startTime, clientStartTime);
			secRecord.setConfig(cfg.lvlReq, cfg.vitCostStart + cfg.vitCostWin, cfg.fightPower, power, cfg.monsterWaveCnt, cfg.monsterCnt, 1, cfg.immune);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addRecord(secRecord);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		gs.getTLogger().logBattleGeneralsState(this, power, generalsinfo[0].id, generalsinfo[0].lvl, generalsinfo[0].evoLvl, generalsinfo[0].advlvl,
											                generalsinfo[1].id, generalsinfo[1].lvl, generalsinfo[1].evoLvl, generalsinfo[1].advlvl,
											                generalsinfo[2].id, generalsinfo[2].lvl, generalsinfo[2].evoLvl, generalsinfo[2].advlvl,
											                generalsinfo[3].id, generalsinfo[3].lvl, generalsinfo[3].evoLvl, generalsinfo[3].advlvl,
											                generalsinfo[4].id, generalsinfo[4].lvl, generalsinfo[4].evoLvl, generalsinfo[4].advlvl,
											                petinfo.id, petinfo.lvl, petinfo.evoLvl, generalBattleType);
		return true;
	}
	
	public boolean  combatEventFinish(byte ceid, byte celvl, short cid, byte star, int clientCostTime, int clientEndTime, List<Short> generalIDs, int waveCount, int dpsTotal, int clientSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		SBean.CombatBonus cb = null;
		Set<Short> gids = new TreeSet<Short>(generalIDs);
		if( /*gids.isEmpty() ||*/ gids.size() > GameData.GENERAL_TEAM_SIZE || gids.size() != generalIDs.size() )
			return false;
		if( star > 3 ) star = 3;
		SBean.CombatEventCFGS cecfg = GameData.getInstance().getCombatEventCFG(ceid);
		if( cecfg == null )
			return false;
		if( celvl < 1 || celvl > cecfg.combatIDs.size() || cecfg.combatIDs.get(celvl-1).shortValue() != cid )
			return false;
		SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(cid);
		if( cfg == null )
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		//锟节斤拷锟斤拷之前锟斤拷录锟饺硷拷锟斤拷锟斤拷锟斤拷战锟桔斤拷锟斤拷锟饺硷拷锟斤拷锟杰变化锟斤拷lvl锟斤拷锟斤拷锟斤拷实战锟桔等硷拷
		TLogger.EventBattleRecord bRecord = ( ceid == 1  || ceid == 2 ) ? new TLogger.ChibiBattleRecord(this.lvl) : new TLogger.BaguazhengBattleRecord(this.lvl);
		TLogger.SecRoundEndRecord secRecord = new  TLogger.SecRoundEndRecord();
		boolean cheat = false;
		synchronized( this )
		{
			if( curEventCombat == null || curEventCombat.ceid != ceid
					|| curEventCombat.cid != cid
					|| 0 == gs.getConfig().godMode && star > 0 && curEventCombat.startTime + GameData.MIN_COMBAT_TIME > gs.getTime() )
				return false;
			for(short gid : generalIDs)
			{
				if( generals.get(gid) == null )
					return false;
			}
			int curTime = gs.getTime();
			int svrCostTime = curTime - curEventCombat.startTime;
			cheat =  (0 == gs.getConfig().godMode && svrCostTime < clientCostTime*0.9);
			
			commonFlowRecord.setEvent( ( ceid == 1  || ceid == 2 ) ? TLog.AT_CHIBI_FINISH : TLog.AT_BAGUAZHENG_FINISH);
			commonFlowRecord.setArg(ceid, cid);

			bRecord.setBattle(ceid, celvl);
			bRecord.setScore(star);
			bRecord.setCostTime(svrCostTime);
			
			secRecord.setSvrEndCostTime(curTime, svrCostTime);
			secRecord.setClientEndCostTime(clientEndTime, clientCostTime);
			secRecord.setResult(cheat ? 1 : 0, star);
			secRecord.setClientStats(waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
			if( !cheat && star > 0 )
			{
				
				if( vit < curEventCombat.cfg.vitCostWin )
					return false;
				{
					//
					//
					cb = curEventCombat.cb;
					commonFlowRecord.addChange(subVit(curEventCombat.cfg.vitCostWin));
					if( ceid == 1  || ceid == 2 )
					{
						tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_EVENT12, 1));
						
					}
					if( ceid == 3  || ceid == 4 || ceid == 5 )
					{
						tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_EVENT345, 1));
					}
					// TODO 锟斤拷锟斤拷
					//logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_ANY, 1);
					//if( ctype == 1 )
					//	logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_HERO, 1);
					{
						SBean.DBCombatEventLog log = null;
						for(SBean.DBCombatEventLog e : combatEventLogs)
						{
							if( ceid == e.id )
							{
								log = e;
								break;
							}
						}
						if( log == null )
						{
							log = new SBean.DBCombatEventLog(ceid, (byte)0, 0);
							combatEventLogs.add(log);
						}
						++log.timesToday;
						log.lastTime = gs.getTime();
					}
					//
					Byte times = combatFightLogs.get(curEventCombat.cid);
					if( times == null )
						combatFightLogs.put(curEventCombat.cid, (byte)1);
					else
						combatFightLogs.put(curEventCombat.cid, (byte)(times.byteValue() + 1));
					commonFlowRecord.addChange(addMoney(cb.money));
					secRecord.addMoney(cb.money);
					commonFlowRecord.addChange(addStonePresent(cb.stone*cb.stoneTimes));
					commonFlowRecord.addChanges(addExp(cb.exp, tlogEvent));
					secRecord.addExp(cb.exp);
					for(short gid : generalIDs)
					{
						General g = generals.get(gid);
						int gLvlBefore = g.lvl;
						g.addExp(cb.generalExp/generalIDs.size());
						if (g.lvl > gLvlBefore)
						{
							TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, gLvlBefore, g.evoLvl, g.advLvl);
							grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_LEVELUP, g.lvl, g.evoLvl, g.advLvl);
							tlogEvent.addRecord(grecord);
						}
					}
					commonFlowRecord.addChanges(addDropsNew(cb.entryIDs));
					secRecord.addDrops(cb.entryIDs);
				}
			}
			curEventCombat = null;
		}
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(bRecord);			
		tlogEvent.addRecord(secRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return !cheat;
	}
	
	public boolean combatEventSkip(byte ceid, byte celvl, short cid, byte nTimes, SBean.CombatBonus[] cbs)
	{
		SBean.CombatEventCFGS cecfg = GameData.getInstance().getCombatEventCFG(ceid);
		if( cecfg == null )
			return false;
		if( ! gs.testWeekDays(cecfg.weekdays) )
			return false;
		if( celvl < 1 || celvl > cecfg.combatIDs.size() || cecfg.combatIDs.get(celvl-1).shortValue() != cid )
			return false;
		SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(cid);
		if( cfg == null )
			return false;
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		//锟节斤拷锟斤拷之前锟斤拷录锟饺硷拷锟斤拷锟斤拷锟斤拷战锟桔斤拷锟斤拷锟饺硷拷锟斤拷锟杰变化锟斤拷lvl锟斤拷锟斤拷锟斤拷实战锟桔等硷拷
		TLogger.EventBattleRecord bRecord = ( ceid == 1  || ceid == 2 ) ? new TLogger.ChibiBattleRecord(this.lvl) : new TLogger.BaguazhengBattleRecord(this.lvl);
		int activityType = GameActivities.CHIBI_BATTLE;
		if ( ceid == 1  || ceid == 2 )
		{
			activityType = GameActivities.CHIBI_BATTLE;
		}
		else if( ceid == 3  || ceid == 4 || ceid == 5 )
		{
			activityType = GameActivities.BAGUAZHEN_BATTLE;
		}
		synchronized( this )
		{
			if ( ceid == 1  || ceid == 2 )
				if (isBanChibi())
					return false;
			if (nTimes != 1)
				return false;
			dayRefresh();
			if( lvl < cfg.lvlReq || vit < cfg.vitCostStart + cfg.vitCostWin )
				return false;
			if( lvl < cecfg.lvlReq )
				return false;
			short skipItem = GameData.getInstance().getCombatCmnCFG().combatEventSkipItem;
			if (this.getItemCount(skipItem) < nTimes)
				return false;
			if( cfg.maxTimes > 0 && combatFightLogs.containsKey(id) && combatFightLogs.get(id).byteValue() >= cfg.maxTimes )
				return false;
			{
				SBean.DBCombatEventLog log = null;
				for(SBean.DBCombatEventLog e : combatEventLogs)
				{
					if( ceid == e.id )
					{
						log = e;
						break;
					}
				}
				if( log == null )
				{
					log = new SBean.DBCombatEventLog(ceid, (byte)0, 0);
					combatEventLogs.add(log);
				}
				if( log.timesToday >= cecfg.maxTimes )
					return false;
				if( log.lastTime + cecfg.cool * 60 >= gs.getTime() )
					return false;
				++log.timesToday;
				log.lastTime = gs.getTime();
			}
			commonFlowRecord.addChange(subVit(cfg.vitCostStart + cfg.vitCostWin));
			commonFlowRecord.addChange(this.delItem(skipItem, nTimes));
			if( ceid == 1  || ceid == 2 )
			{
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_EVENT12, 1));
				
			}
			if( ceid == 3  || ceid == 4 || ceid == 5 )
			{
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_EVENT345, 1));
			}
			
			Byte times = combatFightLogs.get((short)cid);
			if( times == null )
				combatFightLogs.put((short)cid, (byte)1);
			else
				combatFightLogs.put((short)cid, (byte)(times.byteValue() + 1));
			
			int totalGeneralExp = 0;
			for (int i = 0 ; i < nTimes; ++i)
			{
				byte nTimesExp = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_EXPERIENCE);
				byte nTimesStone = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_DIAMOND);
				byte nTimesMoney = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_MONEY);
				byte nTimesItem = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(activityType, GameActivities.DROP_ITEM);
				//gs.getLogger().error("nTimesExp="+nTimesExp+", nTimesMoney="+nTimesMoney+", nTimesItem="+nTimesItem);
				SBean.DropEntry extraDrop = gs.getGameActivities().getExtraDropActivity().getExtraDrop(activityType);
				GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, nTimesExp, nTimesStone, nTimesMoney, nTimesItem, extraDrop, null, null, (byte)0);
				cbs[i] = bonusInfo.cb;
				
				commonFlowRecord.addChange(addMoney(cbs[i].money));
				commonFlowRecord.addChange(addStonePresent(cbs[i].stone*cbs[i].stoneTimes));
				commonFlowRecord.addChanges(addExp(cbs[i].exp, tlogEvent));
				commonFlowRecord.addChanges(addDropsNew(cbs[i].entryIDs));
				totalGeneralExp += cbs[i].generalExp;
				cbs[i].generalExp = 0;
			}
			
			SBean.CombatBonus cbNew = new SBean.CombatBonus();
			cbs[nTimes] = cbNew;
			cbNew.exp = 0;
			cbNew.money = 0;
			cbNew.generalExp = 0;
			cbNew.entryIDs = new ArrayList<SBean.DropEntryNew>();
			List<Short> iids = GameData.getInstance().getCombatCmnCFG().generalExpItemIDs;
			SBean.ItemCFGS icfg1 = GameData.getInstance().getItemCFG(iids.get(0));
			SBean.ItemCFGS icfg2 = GameData.getInstance().getItemCFG(iids.get(1));
			final byte nMaxCount = 100;
			while( totalGeneralExp > 0 )
			{
				if( totalGeneralExp >= icfg1.arg1 * nMaxCount )
				{
					totalGeneralExp -= icfg1.arg1 * nMaxCount;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg1.id, nMaxCount));
				}
				else if( totalGeneralExp >= icfg1.arg1 )
				{
					byte n = (byte)(totalGeneralExp / icfg1.arg1);
					totalGeneralExp -= icfg1.arg1 * n;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg1.id, n));
				}
				else if( totalGeneralExp >= icfg2.arg1 * nMaxCount )
				{
					totalGeneralExp -= icfg2.arg1 * nMaxCount;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg2.id, nMaxCount));
				}
				else if( totalGeneralExp > 0 )
				{
					byte n = (byte)(totalGeneralExp / icfg2.arg1);
					totalGeneralExp -= icfg2.arg2 * n;
					if( totalGeneralExp > 0 )
					{
						totalGeneralExp = 0;
						++n;
					}
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg2.id, n));
				}
			}
			commonFlowRecord.addChanges(addDropsNew(cbNew.entryIDs));
			
			commonFlowRecord.setEvent(activityType == GameActivities.CHIBI_BATTLE ? TLog.AT_CHIBI_SKIP : TLog.AT_BAGUAZHENG_SKIP);
			commonFlowRecord.setArg(ceid, cid);
			
			bRecord.setBattle(ceid, celvl);
			bRecord.setScore(3);
			bRecord.setCostTime(0);
		}
		
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(bRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		
		
		return true;
	}
	
	public boolean combatsStart(byte ctype, byte cindex, byte bindex, short cid, SBean.CombatBonus[] cbs, int[] normalDropCount, int clientStartTime, List<Short> generalIDs)
	{
		short id = gs.getGameData().getCombatID(ctype, cindex, bindex);
		if( id <= 0 || id != cid )
			return false;
		SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(id);
		if( cfg == null )
			return false;
		if( cfg.type == 0 && ctype == 1 )
			return false;
		Set<Short> gids = new TreeSet<Short>(generalIDs);
		if( /*gids.isEmpty() ||*/ gids.size() > GameData.GENERAL_TEAM_SIZE || gids.size() != generalIDs.size() )
			return false;
		BattleGeneralInfo[] generalsinfo = new BattleGeneralInfo[5];
		for (int i = 0; i < 5; ++i)
		{
			generalsinfo[i] = new BattleGeneralInfo();
		}
		BattlePetInfo petinfo = new BattlePetInfo();
		int index = 0;
		int power = 0;
		for(short gid : generalIDs)
		{
			General g = generals.get(gid);
			if( g == null )
			{
				return false;
			}
			else
			{
				power += calcGeneralPower(gid);
				if (index < 5)
				{
					BattleGeneralInfo info = generalsinfo[index];
					info.id = g.id;
					info.lvl = g.lvl;
					info.evoLvl = g.evoLvl;
					info.advlvl = g.advLvl;	
				}
			}
			index++;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.setEvent(TLog.AT_COMBAT_START);
		commonFlowRecord.setArg(ctype, cindex, bindex, cid);
		TLogger.SecRoundStartRecord secRecord = new  TLogger.SecRoundStartRecord(this.vit);
		secRecord.setBattle(id, ctype == 0 ? TLog.SEC_BATTLE_COMMON : TLog.SEC_BATTLE_HERO);
		synchronized( this )
		{
			if (isBanBattle())
				return false;
			dayRefresh();
			if( ctype < 0 || ctype >= combatPos.length )
				return false;
			SBean.DBCombatPos curPos = combatPos[ctype];
			if( cindex > curPos.catIndex
					|| cindex == curPos.catIndex && bindex > curPos.combatIndex )
				return false;
			if( ctype == 1 )
			{
				SBean.DBCombatPos normalPos = combatPos[0];
				if( cindex > normalPos.catIndex
						|| cindex == normalPos.catIndex && bindex >= normalPos.combatIndex )
					return false;	
			}
			if( lvl < cfg.lvlReq || vit < cfg.vitCostStart + cfg.vitCostWin )
				return false;
			if( cfg.maxTimes > 0 && combatFightLogs.containsKey(id) && combatFightLogs.get(id).byteValue() >= cfg.maxTimes )
				return false;
			boolean bCurrent = cindex == curPos.catIndex && bindex == curPos.combatIndex;
			if( ! bCurrent && cfg.type == 0 )
				return false;
			commonFlowRecord.addChange(subVit(cfg.vitCostStart));
			curCombat = new CurrentCombat();
			curCombat.cid = id;
			curCombat.cfg = cfg;
			curCombat.ctype = ctype;
			curCombat.cindex = cindex;
			curCombat.bindex = bindex;
			curCombat.bCurrent = bCurrent;
			curCombat.startTime = gs.getTime();
			float levelLimitExpAdd = gs.getLoginManager().getTopLevel().getCurLevelExpAdd(this.lvl);
			byte nTimesExp = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_EXPERIENCE);
			byte nTimesStone = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_DIAMOND);
			byte nTimesMoney = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_MONEY);
			byte nTimesItem = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_ITEM);
			//gs.getLogger().error("nTimesExp="+nTimesExp+", nTimesMoney="+nTimesMoney+", nTimesItem="+nTimesItem);
			SBean.DropEntry extraDrop = gs.getGameActivities().getExtraDropActivity().getExtraDrop(ctype);
			List<Short> forceBattleExtraDropTblIDs = gs.getWarManager().getCurForceBattleExtraDropTblID(this, ctype != 0, cid);
			List<Short> forceMemExtraDropTblIDs = gs.getWarManager().getForceMemberExtraDropTblID(this, ctype != 0, cid);
			curCombat.itemDropLogTrans = new GameData.ItemDropLogTrans(itemDropLogs, false);
			GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, curCombat.itemDropLogTrans, levelLimitExpAdd, nTimesExp, nTimesStone, nTimesMoney, nTimesItem, extraDrop, forceBattleExtraDropTblIDs, forceMemExtraDropTblIDs, getQQVIP()); 
			curCombat.cb = bonusInfo.cb;
			cbs[0] = curCombat.cb; 
			normalDropCount[0] = bonusInfo.nornamlDropCount;
			secRecord.setStartTime(curCombat.startTime, clientStartTime);
			secRecord.setConfig(cfg.lvlReq, cfg.vitCostStart + cfg.vitCostWin, cfg.fightPower, power, cfg.monsterWaveCnt, cfg.monsterCnt, 1, cfg.immune);
			
		}
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(secRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		gs.getTLogger().logBattleGeneralsState(this, power, generalsinfo[0].id, generalsinfo[0].lvl, generalsinfo[0].evoLvl, generalsinfo[0].advlvl,
											                generalsinfo[1].id, generalsinfo[1].lvl, generalsinfo[1].evoLvl, generalsinfo[1].advlvl,
											                generalsinfo[2].id, generalsinfo[2].lvl, generalsinfo[2].evoLvl, generalsinfo[2].advlvl,
											                generalsinfo[3].id, generalsinfo[3].lvl, generalsinfo[3].evoLvl, generalsinfo[3].advlvl,
											                generalsinfo[4].id, generalsinfo[4].lvl, generalsinfo[4].evoLvl, generalsinfo[4].advlvl,
											                petinfo.id, petinfo.lvl, petinfo.evoLvl, ctype == 0 ? TLog.GENERALEBATTLE_NORMAL_BATTLE : TLog.GENERALEBATTLE_HERO_BATTLE);
		return true;
	}
	
	public boolean  combatsFinish(byte ctype, byte cindex, byte bindex, short cid, byte star, int clientCostTime, int clientEndTime, List<Short> generalIDs, int waveCount, int dpsTotal, int clientSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		SBean.CombatBonus cb = null;
		//List<SBean.FormatAnnounceData> faData = new ArrayList<SBean.FormatAnnounceData>();
		//String param = new String();
		Set<Short> gids = new TreeSet<Short>(generalIDs);
		if( /*gids.isEmpty() ||*/ gids.size() > GameData.GENERAL_TEAM_SIZE || gids.size() != generalIDs.size() )
			return false;
		if( star > 3 ) star = 3;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		//锟节斤拷锟斤拷之前锟斤拷录锟饺硷拷锟斤拷锟斤拷锟斤拷战锟桔斤拷锟斤拷锟饺硷拷锟斤拷锟杰变化锟斤拷lvl锟斤拷锟斤拷锟斤拷实战锟桔等硷拷
		TLogger.BattleRecord bRecord = new TLogger.BattleRecord(this.lvl);
		bRecord.setCombatType(TLog.COMBAT_COMMON);
		bRecord.setBattle(ctype == 0 ? TLog.BATTLE_COMMON : TLog.BATTLE_HERO, cindex+1, bindex+1);
		TLogger.SecRoundEndRecord secRecord = new  TLogger.SecRoundEndRecord();
		boolean cheat = false;
		synchronized( this )
		{
			if( curCombat == null || curCombat.ctype != ctype
					|| curCombat.cindex != cindex || curCombat.bindex != bindex
					|| curCombat.cid != cid
					|| 0 == gs.getConfig().godMode && star > 0 && curCombat.startTime + GameData.MIN_COMBAT_TIME > gs.getTime() )
				return false;
			for(short gid : generalIDs)
			{
				if( generals.get(gid) == null )
					return false;
			}
			int curTime = gs.getTime();
			int svrCostTime = curTime - curCombat.startTime;
			cheat = (0 == gs.getConfig().godMode && svrCostTime < clientCostTime*0.9);
			if( !cheat && star > 0 )
			{
				/*TODO
				if( costTime > 15 + gs.getTime() - curCombat.startTime )
				{
					gs.getLogger().warn("roleid=" + id + ",costTime=" + costTime + ",serverTime="+(gs.getTime()-curCombat.startTime)
							+",pos=["+type+","+curCombat.pos.catIndex+","+ curCombat.pos.combatIndex+"]");
				}
				*/
				if( vit < curCombat.cfg.vitCostWin )
					return false;
				{
					if( curCombat.bCurrent )
					{
						while( true )
						{
							if( ! gs.getGameData().advCombatPos(ctype, combatPos[ctype]) )
								break;
							if( gs.getGameData().verifyCombatPos(ctype, combatPos[ctype]) )
								break;
						}
					}
					cb = curCombat.cb;
					commonFlowRecord.addChange(subVit(curCombat.cfg.vitCostWin));
					// TODO 锟斤拷锟斤拷
					tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_ANY, 1));
					if( ctype == 1 )
						tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_HERO, 1));
					while( ctype >= combatScores.size() )
					{
						SBean.DBCombatScore cs = new SBean.DBCombatScore();
						cs.combatType = (byte)combatScores.size();
						cs.scores = new ArrayList<SBean.DBCombatTypeScore>();
						combatScores.add(cs);
					}
					List<SBean.DBCombatTypeScore> slst = combatScores.get(ctype).scores;
					while( curCombat.cindex >= slst.size() )
					{
						SBean.DBCombatTypeScore cs = new SBean.DBCombatTypeScore();
						cs.catID = (byte)slst.size();
						cs.flag = SBean.DBCombatTypeScore.eNull;
						cs.score = new ArrayList<Byte>();
						slst.add(cs);
					}
					SBean.DBCombatTypeScore cs = slst.get(curCombat.cindex);
					while( curCombat.bindex >= cs.score.size() )
					{
						cs.score.add((byte)0);
					}
					if( cs.score.get(curCombat.bindex) < star )
					{
						cs.score.set(curCombat.bindex, star); // TODO
						if( star == 3 )
						{
							//testMedalCombatStar(ctype, cindex);
						}
					}
					Byte times = combatFightLogs.get(curCombat.cid);
					if( times == null )
						combatFightLogs.put(curCombat.cid, (byte)1);
					else
						combatFightLogs.put(curCombat.cid, (byte)(times.byteValue() + 1));
					if( curCombat.itemDropLogTrans != null )
						curCombat.itemDropLogTrans.commit();
					commonFlowRecord.addChange(addMoney(cb.money));
					secRecord.addMoney(cb.money);
					commonFlowRecord.addChange(addStonePresent(cb.stone*cb.stoneTimes));
					commonFlowRecord.addChanges(addExp(cb.exp, tlogEvent));
					secRecord.addExp(cb.exp);
					for(short gid : generalIDs)
					{
						General g = generals.get(gid);
						int gLvlBefore = g.lvl;
						g.addExp(cb.generalExp/generalIDs.size());
						if (g.lvl > gLvlBefore)
						{
							TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, gLvlBefore, g.evoLvl, g.advLvl);
							grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_LEVELUP, g.lvl, g.evoLvl, g.advLvl);
							tlogEvent.addRecord(grecord);
						}
					}
					commonFlowRecord.addChanges(addDropsNew(cb.entryIDs));
					secRecord.addDrops(cb.entryIDs);
					if (ctype == 0)
					{
						testUnlockBaseCity(cid);
					}
				}
			}
			commonFlowRecord.setEvent(TLog.AT_COMBAT_FINISH);
			commonFlowRecord.setArg(ctype, cindex, bindex, cid);
			bRecord.setCostTime(svrCostTime);
			bRecord.setScore(star);
			secRecord.setSvrEndCostTime(curTime, svrCostTime);
			secRecord.setClientEndCostTime(clientEndTime, clientCostTime);
			secRecord.setResult(cheat ? 1 : 0, star);
			secRecord.setClientStats(waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
			
			curCombat = null;
		}
		/*
		if( ! faData.isEmpty() )
		{
			for(SBean.FormatAnnounceData d : faData)
				gs.getLoginManager().addFormatAnnounce(d);
		}
		if( cb != null )
		{
			gs.getGameStatLogger().roleAction(this.id, "FINISH COMBAT",param);
			gs.getRPCManager().notifyFinishCombat(netsid, res, cb);
			return true;
		}
		return false;
		*/
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(bRecord);
		tlogEvent.addRecord(secRecord);
		
		gs.getTLogger().logEvent(this, tlogEvent);
		return !cheat;
	}
	
	public boolean combatsReset(short cid)
	{
		SBean.CombatCFGS cfg = gs.getGameData().getCombatCFG(cid);
		if( cfg == null || cfg.type == 0 )
			return false;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		byte timesNow = 0;
		synchronized( this )
		{
			dayRefresh();
			byte maxReset = gs.getGameData().getCombatMaxResetTimes(lvlVIP);
			Byte resetTimes = combatResetLogs.get(cid);
			if( resetTimes != null )
				timesNow = resetTimes.byteValue();
			if( timesNow >= maxReset )
				return false;
			int cost = gs.getGameData().getCombatResetPrice(timesNow);
			if( cost > stone )
				return false;
			commonFlowRecord.addChange(useStone(cost));
			combatResetLogs.put(cid, ++timesNow);
			combatFightLogs.remove(new Short(cid));
		}
		commonFlowRecord.setEvent(TLog.AT_COMBAT_RESET);
		commonFlowRecord.setArg(cid, timesNow);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean combatsSkip(byte ctype, byte cindex, byte bindex, short cid, byte nTimes, SBean.CombatBonus[] cbs)
	{
		short id = gs.getGameData().getCombatID(ctype, cindex, bindex);
		if( id <= 0 || id != cid )
			return false;
		SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(id);
		if( cfg == null || cfg.type == 0 )
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		//锟节斤拷锟斤拷之前锟斤拷录锟饺硷拷锟斤拷锟斤拷锟斤拷战锟桔斤拷锟斤拷锟饺硷拷锟斤拷锟杰变化锟斤拷lvl锟斤拷锟斤拷锟斤拷实战锟桔等硷拷
	    TLogger.BattleRecord bRecord = new TLogger.BattleRecord(this.lvl);
	    bRecord.setCombatType(TLog.COMBAT_SKIP);
		bRecord.setBattle(ctype == 0 ? TLog.BATTLE_COMMON : TLog.BATTLE_HERO, cindex+1, bindex+1);
		synchronized( this )
		{
			if (isBanBattle())
				return false;
			if( ! GameData.getInstance().canSaodang(lvlVIP) )
				return false;
			if( nTimes > 1 && ! GameData.getInstance().canSaodang10(lvlVIP) )
				return false;
			dayRefresh();
			if( ctype < 0 || ctype >= combatPos.length )
				return false;
			SBean.DBCombatPos curPos = combatPos[ctype];
			if( cindex > curPos.catIndex
					|| cindex == curPos.catIndex && bindex >= curPos.combatIndex )
				return false;
			if( lvl < cfg.lvlReq || vit < (cfg.vitCostStart + cfg.vitCostWin) * nTimes )
				return false;
			if( cfg.maxTimes > 0 && combatFightLogs.containsKey(id) && combatFightLogs.get(id).byteValue() + nTimes > cfg.maxTimes )
				return false;
			byte scoreOld = -1;
			if( ctype >= 0 && type < combatScores.size() )
			{
				List<SBean.DBCombatTypeScore> lst = combatScores.get(ctype).scores;
				if( cindex >= 0 && cindex < lst.size() )
				{
					List<Byte> scores = lst.get(cindex).score;
					if( bindex >= 0 && bindex < scores.size() )
						scoreOld = scores.get(bindex);
				}
			}
			if( scoreOld < 3 )
				return false;
			commonFlowRecord.addChange(subVit((cfg.vitCostStart + cfg.vitCostWin) * nTimes));
			//锟斤拷锟斤拷
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_ANY, nTimes));
			if( ctype == 1 )
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_COMBAT_HERO, nTimes));
			Byte times = combatFightLogs.get(cid);
			if( times == null )
				combatFightLogs.put(cid, (byte)nTimes);
			else
				combatFightLogs.put(cid, (byte)(times.byteValue() + nTimes));
			int totalGeneralExp = 0;
			for(int i = 0; i < nTimes; ++i)
			{
				float levelLimitExpAdd = gs.getLoginManager().getTopLevel().getCurLevelExpAdd(this.lvl);
				byte nTimesExp = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_EXPERIENCE);
				byte nTimesStone = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_DIAMOND);
				byte nTimesMoney = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_MONEY);
				byte nTimesItem = (byte)gs.getGameActivities().getDoubleDropActivity().getDropMultiple(ctype, GameActivities.DROP_ITEM);
				SBean.DropEntry extraDrop = gs.getGameActivities().getExtraDropActivity().getExtraDrop(ctype);
				List<Short> forceBattleExtraDropTblIDs = gs.getWarManager().getCurForceBattleExtraDropTblID(this, ctype != 0, cid);
				List<Short> forceMemExtraDropTblIDs = gs.getWarManager().getForceMemberExtraDropTblID(this, ctype != 0, cid);
				GameData.BonusInfo bonusInfo = GameData.getInstance().getCombatBonus(cfg, new GameData.ItemDropLogTrans(itemDropLogs, true), levelLimitExpAdd, nTimesExp, nTimesStone, nTimesMoney
						, nTimesItem, extraDrop, forceBattleExtraDropTblIDs, forceMemExtraDropTblIDs, getQQVIP()); 
				cbs[i] = bonusInfo.cb;
				commonFlowRecord.addChanges(addExp(cbs[i].exp, tlogEvent));
				commonFlowRecord.addChange(addMoney(cbs[i].money));
				commonFlowRecord.addChange(addStonePresent(cbs[i].stone*cbs[i].stoneTimes));
				commonFlowRecord.addChanges(addDropsNew(cbs[i].entryIDs));
				totalGeneralExp += ( cbs[i].generalExp);
				cbs[i].generalExp = 0;
			}
			SBean.CombatBonus cbNew = new SBean.CombatBonus();
			cbs[nTimes] = cbNew;
			cbNew.exp = 0;
			cbNew.money = 0;
			cbNew.generalExp = 0;
			cbNew.entryIDs = new ArrayList<SBean.DropEntryNew>();
			List<Short> iids = GameData.getInstance().getCombatCmnCFG().generalExpItemIDs;
			SBean.ItemCFGS icfg1 = GameData.getInstance().getItemCFG(iids.get(0));
			SBean.ItemCFGS icfg2 = GameData.getInstance().getItemCFG(iids.get(1));
			final byte nMaxCount = 100;
			while( totalGeneralExp > 0 )
			{
				if( totalGeneralExp >= icfg1.arg1 * nMaxCount )
				{
					totalGeneralExp -= icfg1.arg1 * nMaxCount;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg1.id, nMaxCount));
				}
				else if( totalGeneralExp >= icfg1.arg1 )
				{
					byte n = (byte)(totalGeneralExp / icfg1.arg1);
					totalGeneralExp -= icfg1.arg1 * n;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg1.id, n));
				}
				else if( totalGeneralExp >= icfg2.arg1 * nMaxCount )
				{
					totalGeneralExp -= icfg2.arg1 * nMaxCount;
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg2.id, nMaxCount));
				}
				else if( totalGeneralExp > 0 )
				{
					byte n = (byte)(totalGeneralExp / icfg2.arg1);
					totalGeneralExp -= icfg2.arg2 * n;
					if( totalGeneralExp > 0 )
					{
						totalGeneralExp = 0;
						++n;
					}
					cbNew.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, icfg2.id, n));
				}
			}
			commonFlowRecord.addChanges(addDropsNew(cbNew.entryIDs));
			curCombat = null;
		}
		commonFlowRecord.setEvent(TLog.AT_COMBAT_SKIP);
		commonFlowRecord.setArg(ctype, cindex, bindex, cid);
		bRecord.setScore(3);
		bRecord.setCostTime(0);
		
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(bRecord);
		
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	private boolean canCheckIn()
	{
		CheckinSyncInfo info = new CheckinSyncInfo();
		byte[] now = new byte[2];
		SBean.CheckinCFGS cfg = GameData.getInstance().getCheckinNow(gs.getTimeGMT(), now);
		if( cfg == null )
			return false;
		// TODO
		byte checkinID = now[0];
		byte dayNow = now[1];
		synchronized( this )
		{
			info.id = checkinID;
			if( info.id != checkinLog.lastID )
			{
				info.n = 0;
				info.bOK = true;
			}
			else
			{
				info.n = checkinLog.nFinished;
				info.bOK = checkinLog.lastDay < dayNow;
			}
		}
		return info.bOK;
	}
	
	private boolean testWorldMail()
	{
		boolean bNew = false;
		if( mail.dbmail.worldMailID >= gs.getLoginManager().getLastWorldMailID() )
			return false;
		List<SBean.SysMail> worldMails = gs.getLoginManager().testWorldMails(createTime, mail.dbmail.worldMailID);			
		for(SBean.SysMail e : worldMails)
		{
			if( e.id > mail.dbmail.worldMailID )
				mail.dbmail.worldMailID = e.id;
			if( lvl >= e.lvlReq && lvlVIP >= e.vipReq )
			{
				mail.dbmail.addWorldMail(e);
				bNew = true;
			}
		}
		return bNew;
	}
	
	private boolean testBroadcasts()
	{
		int newLastModifyTime = gs.getLoginManager().getBroadcastLastModifyTime();
		if (broadcastsLastModifyTime < newLastModifyTime)
		{
			broadcastsLastModifyTime = newLastModifyTime;
			int now = gs.getTime();
			List<Integer> lst = new ArrayList<Integer>();
			List<SBean.Broadcast> curbroadcasts = gs.getLoginManager().getCurrentBroadcasts();
			for (SBean.Broadcast e : curbroadcasts)
			{
				if (now < e.timeStart + e.lifeTime)
					lst.add(e.id);
			}
			broadcasts = lst;
			//gs.getLogger().info("testBroadcasts true");
			return true;
		}		
		return false;
	}
	
	public void syncWorldMailWithLock()
	{
		boolean bNew = false;
		synchronized( this )
		{
			bNew = testWorldMail();
		}
		if( bNew && netsid > 0 )
			gs.getRPCManager().notifyMailNew(netsid);
	}
	/*
	 * @where 0: new user create role
	 *        1: old user load from db
	 *        2: old user load from memory 
	 */
	void onLoadOK(String username, final int where, MSDKInfo msdkInfo)
	{
		this.msdkInfo = msdkInfo;
		//
		if( gs.getHotChecker().checkRoleBeforeLoad(gs, this, where) != 0 )
		{
			// TODO
		}
		//
		gs.getLogger().debug("username " + username + " load ok, roleid is " + id);
		RPCManager.SessionInfo sinfo = gs.getRPCManager().getSessionInfo(netsid);
		if( sinfo != null )
		{
			// TODO
		}
		//
		for(byte ctype = 0; ctype< 2; ++ctype)
		{
			while( true )
			{
				if( gs.getGameData().verifyCombatPos(ctype, this.combatPos[ctype]) )
					break;
				if( ! gs.getGameData().advCombatPos(ctype, this.combatPos[ctype]) )
					break;
			}
			//
		}
		//
		//boolean bAlphaMail = false;
		boolean newuserMail = false;
		int now = gs.getTime();
		if (where==0)
		{
			if( sinfo != null )
			{
				gs.getLogger().debug("create role, uname = " 
						+ username + ", rname = "
						+ this.name + ", ip = "
						+ msdkInfo.ip + ", deviceID = "
						+ msdkInfo.deviceID);
			}
			gs.getTLogger().logRegister(this);
			gs.getExchangeManager().announceCreateRole(msdkInfo.openID, id);
			updateLoginTime(now);
			gs.getTLogger().logLogin(this);
			//bAlphaMail = true;
			newuserMail = true;
		}
		else if (where==1 || where==2)
		{
			if( ! testFlag(ROLE_FLAG_CREATE_EXCHANGE) )
			{
				gs.getExchangeManager().announceCreateRole(msdkInfo.openID, id);
			}
			if( where == 1)
			{
				updateLoginTime(now);
				gs.getTLogger().logLogin(this);
				gs.getTSSAntiManager().addUser(this);
				
				
			}
		}
		byte reserr = GameData.USERLOGIN_OK;
		byte resstate = (byte)gs.getConfig().godMode;
		SBean.RoleBrief brief = null;
		List<DBRoleGeneral> generals = null;
		List<SBean.DBPet> pets = null;
		List<SBean.DBFeedingPet> petsFeeding = null;
		List<SBean.DBRoleItem2> items = null;
		List<SBean.DBRoleEquip> equips = null;
		List<SBean.DBCombatLog> combatFightLogs = null;
		List<SBean.DBCombatLog> combatResetLogs = null;
		List<SBean.DBCombatPos> combatPos = null;
		List<SBean.DBTaskPos> taskPos = null;
		List<SBean.DBFriendData> friends = null;
		List<SBean.DBPetDeform> petsDeform = null;
		Set<String> friendsNoSearch = null;
		List<SBean.DBGeneralStone> generalStones = null;
		byte lvlVIP = 0;
		boolean bCanCheckin = false;
		boolean bCanActTip = false;
		byte[] takeZanTimes = {0};
		//boolean bAlphaGift = false;
		testRemoveForceWarItems();
		synchronized( this )
		{
			setFlag(ROLE_FLAG_CHECKIN);
			setFlag(ROLE_FLAG_EGG);
			if( where == 0 )
			{
				addStonePresent(gs.getConfig().newRoleYuanbao);
			}
			// TODO
			testLoginGiftMail();
			testWorldMail();
			// TODO
			if( gs.getConfig().godMode == 1 )
			{
				setQQVIP((byte)1);
				if( gs.getConfig().godModeRoleLevel > 0 && gs.getConfig().godModeRoleLevel <= gs.getRoleLevelLimit() )
				{
					setLvl((byte)gs.getConfig().godModeRoleLevel);
				}
				if( gs.getConfig().godModeMoney >= 0 )
					money = gs.getConfig().godModeMoney;
				if( gs.getConfig().godModeStone >= 0 )
					stone = gs.getConfig().godModeStone;
				if( gs.getConfig().godModeSkillPoint >= 0 )
					skillPoint = (short)gs.getConfig().godModeSkillPoint;
				if( gs.getConfig().godModeArenaPoint >= 0 )
					arenaState.point = gs.getConfig().godModeArenaPoint;
				if( gs.getConfig().godModeMarchPoint >= 0 )
					marchState.point = gs.getConfig().godModeMarchPoint;
				if( gs.getConfig().godModeForcePoint >= 0 )
					forceInfo.point = gs.getConfig().godModeForcePoint;
				if( gs.getConfig().godModeRichPoint >= 0 && rich != null )
					rich.point = gs.getConfig().godModeRichPoint;
				for(short i = 0; i < 10000; ++i)
				{
					if(i==6000){
						continue;
					}
					SBean.ItemCFGS icfg = gs.getGameData().getItemCFG(i);
					if( icfg != null && this.items.get(i) == null )
						addItem(i,100);
				}
				for(short i = 0; i < 10000; ++i)
				{
					SBean.EquipCFGS ecfg = gs.getGameData().getEquipCFG(i);
					if( ecfg != null && this.equips.get(i) == null )
						addEquip(i, (short)100);
				}
				List<Short> lstGenerals = new ArrayList<Short>();
				for(short i = 1; i <= 100; ++i)
				{
					lstGenerals.add(i);
				}
				for(short i : lstGenerals)
				{
					SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(i);
					if( gcfg == null )
						continue;
					addGeneral((short)i, (byte)1);
					
					SBean.PetCFGS pcfg = gs.getGameData().getPetCFG(i, false);
					if( pcfg == null )
						continue;
					addPet((short)i, (short)100);
				}
				{
					short glvl = gs.getConfig().godModeGeneralLevel;
					if( glvl > 0 )
					{
						if( glvl > lvl )
							glvl = lvl;
						for(General g : this.generals.values())
						{
							g.lvl = glvl;
							g.exp = 0;
						}
					}
					byte gadvlvl = gs.getConfig().godModeGeneralAdvLevel;
					if( gadvlvl > 0 )
					{
						if( gadvlvl > GameData.MAX_GENERAL_ADV_LEVEL )
							glvl = GameData.MAX_GENERAL_ADV_LEVEL;
						for(General g : this.generals.values())
						{
							g.advLvl = gadvlvl;
						}
					}
					byte gevolvl = gs.getConfig().godModeGeneralEvoLevel;
					if( gevolvl > 0 )
					{
						if( gevolvl > GameData.MAX_GENERAL_EVO_LEVEL )
							gevolvl = GameData.MAX_GENERAL_EVO_LEVEL;
						for(General g : this.generals.values())
						{
							g.evoLvl = gevolvl;
						}
					}
					byte ggildlvl = gs.getConfig().godModeGeneralGildLevel;
					if( ggildlvl == 0 || ggildlvl == 1 )
					{
						generalsSetGildLevel(ggildlvl);
					}
					if( gs.getConfig().godModeGeneralFullSkillLevel == 1 )
					{
						generalsSetSkillFullLevel();
					}
					byte gwevo = gs.getConfig().godModeGeneralWeaponEvo;
					byte gwlvl = gs.getConfig().godModeGeneralWeaponLevel;
					if( gwevo >= 0 && gwlvl >= 0 )
					{
						generalsSetWeaponEvoLevel(gwevo, gwlvl);
					}
					byte gwreset = gs.getConfig().godModeGeneralWeaponReset;
					if( gwreset == 0 || gwreset == 1 || gwreset == 2 )
					{
						generalsSetWeaponResetLevel(gwreset);
					}
					
					int seyen = gs.getConfig().godModeGeneralSeyen;
					if(seyen>0){
						this.addSeyenExp(seyen);
					}					
					
					short officialLvl = gs.getConfig().godModeGeneralOfficialLvl;
					if( officialLvl >= 0 )
					{
						for(General g : this.generals.values())
						{
							if (g.official != null && g.official.size() > 0) {
								SBean.DBGeneralOfficial official = g.official.get(0);
								official.lvl = officialLvl;
							}
						}
					}
					short officialSLvl = gs.getConfig().godModeGeneralOfficialSLvl;
					if( officialSLvl >= 0 )
					{
						for(General g : this.generals.values())
						{
							if (g.official != null && g.official.size() > 0) {
								SBean.DBGeneralOfficial official = g.official.get(0);
								official.skillLvl = officialSLvl;
							}
						}
					}
				}
				{
					short plvl = gs.getConfig().godModePetLevel;
					if( plvl > 0 )
					{
						if( plvl > lvl )
							plvl = lvl;
						for(Pet p : this.pets.values())
						{
							p.lvl = plvl;
							p.exp = 0;
						}
					}
					byte pevolvl = gs.getConfig().godModePetEvoLevel;
					if( pevolvl > 0 )
					{
						if( pevolvl > GameData.MAX_PET_EVO_LEVEL )
							pevolvl = GameData.MAX_PET_EVO_LEVEL;
						for(Pet p : this.pets.values())
						{
							p.evoLvl = pevolvl;
						}
					}
				}
				{
					setFlag(ROLE_FLAG_CHECKIN);
					setFlag(ROLE_FLAG_EGG);
					setFlag(ROLE_FLAG_AUTO_FIGHTING);
				}
				if( gs.getConfig().godModeCombatPosNormalCIndex > 0 && gs.getConfig().godModeCombatPosNormalBIndex > 0 )
				{
					gs.getGameData().updateGodCombatPos((byte)0, this.combatPos[0], (byte)(gs.getConfig().godModeCombatPosNormalCIndex-1), (byte)(gs.getConfig().godModeCombatPosNormalBIndex-1));
					for (int i = 1; i < this.combatPos[0].catIndex+1; ++i)
						if (GameData.getInstance().getCityCfg(i) != null)
							this.citys.unlockBaseCity(i);
				}
				if( gs.getConfig().godModeCombatPosHeroCIndex > 0 && gs.getConfig().godModeCombatPosHeroBIndex > 0 )
				{
					gs.getGameData().updateGodCombatPos((byte)1, this.combatPos[1], (byte)(gs.getConfig().godModeCombatPosHeroCIndex-1), (byte)(gs.getConfig().godModeCombatPosHeroBIndex-1));
				}
				
				if (gs.getConfig().godModeRelation > 0) {
					relations.clear();
					Collection<SBean.RelationCFGS> rels = gs.gameData.getAllRelationCFG();
					for (SBean.RelationCFGS r : rels) {
						SBean.DBRelation re = new SBean.DBRelation(r.id, new ArrayList<Short>(), 0, 0);
						for (short iid : r.activateItems) {
							if (gs.getConfig().godModeRelation == 1)
								re.lvls.add((short)1);
							else {
								short gid = gs.gameData.getRelationItemCFG(iid);
								Integer lvl = gs.gameData.getRelationLevelMaxCFG(gid);
								if (lvl != null)
									re.lvls.add(lvl.shortValue());
							}
						}

						relations.put(re.id, re);
					}
				}
                if(gs.getConfig().godModeSeyen >0){
					
					int sizet = gs.getConfig().godModeSeyen*this.generals.values().size();
					for(General g : this.generals.values())
					{
						if(g.generalSeyen==null||g.generalSeyen.size()==0){
							g.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
							g.generalSeyen.add(new SBean.DBGeneralSeyen());
						}
						if (g.generalSeyen != null && g.generalSeyen.size() > 0) {
							DBGeneralSeyen seyen = g.generalSeyen.get(0);
							seyen.lvl = (short) gs.getConfig().godModeSeyen;
							seyen.seyenTotal = sizet;
						}
					}
				}
                
                if(gs.getConfig().godModeStone1 >0){
					
                	for(General g : this.generals.values())
					{
                		GeneralStonePosCFGS rcfgPos = GameData.getInstance().getGeneralStonePosCFG(g.id);
                		
                		if(rcfgPos==null){
                			continue;
                		}
                		
                		if(rcfgPos.posType1!=-1&&rcfgPos.state1>0){
                			GeneralStoneBasicCFGS rcfgBasic = GameData.getInstance().getGeneralStoneBasicTypeCFG((short) rcfgPos.posType1,(short)gs.getConfig().godModeStone1);
                    		if(rcfgBasic !=null){
                    			if(generalStoneInfo==null){
                					generalStoneInfo = new SBean.DBGeneralStoneInfo();
                				}
                				if(generalStoneInfo.stoneId<=0){
                					generalStoneInfo.stoneId=800000;
                				}
                				generalStoneInfo.stoneId+=1;
                				DBGeneralStone stone = new  DBGeneralStone();
                				stone.id=generalStoneInfo.stoneId;
                				stone.itemId=rcfgBasic.sid;
                				stone.gid=g.id;
                				stone.pos=1;
                				stone.passes= new ArrayList<SBean.DBGeneralStoneProp>();
                				stone.resetPasses= new ArrayList<SBean.DBGeneralStoneProp>();
                				generalStone.put(stone.id,stone);
                    		}
                		}
                		
                		if(rcfgPos.posType2!=-1&&rcfgPos.state2>0){
                			GeneralStoneBasicCFGS rcfgBasic = GameData.getInstance().getGeneralStoneBasicTypeCFG((short) rcfgPos.posType2,(short)gs.getConfig().godModeStone1);
                    		if(rcfgBasic !=null){
                    			if(generalStoneInfo==null){
                					generalStoneInfo = new SBean.DBGeneralStoneInfo();
                				}
                				if(generalStoneInfo.stoneId<=0){
                					generalStoneInfo.stoneId=800000;
                				}
                				generalStoneInfo.stoneId+=1;
                				DBGeneralStone stone = new  DBGeneralStone();
                				stone.id=generalStoneInfo.stoneId;
                				stone.itemId=rcfgBasic.sid;
                				stone.gid=g.id;
                				stone.pos=2;
                				stone.passes= new ArrayList<SBean.DBGeneralStoneProp>();
                				stone.resetPasses= new ArrayList<SBean.DBGeneralStoneProp>();
                				generalStone.put(stone.id,stone);
                    		}
                		}
                		
                		if(rcfgPos.posType3!=-1&&rcfgPos.state3>0){
                			GeneralStoneBasicCFGS rcfgBasic = GameData.getInstance().getGeneralStoneBasicTypeCFG((short) rcfgPos.posType3,(short)gs.getConfig().godModeStone1);
                    		if(rcfgBasic !=null){
                    			if(generalStoneInfo==null){
                					generalStoneInfo = new SBean.DBGeneralStoneInfo();
                				}
                				if(generalStoneInfo.stoneId<=0){
                					generalStoneInfo.stoneId=800000;
                				}
                				generalStoneInfo.stoneId+=1;
                				DBGeneralStone stone = new  DBGeneralStone();
                				stone.id=generalStoneInfo.stoneId;
                				stone.itemId=rcfgBasic.sid;
                				stone.gid=g.id;
                				stone.pos=3;
                				stone.passes= new ArrayList<SBean.DBGeneralStoneProp>();
                				stone.resetPasses= new ArrayList<SBean.DBGeneralStoneProp>();
                				generalStone.put(stone.id,stone);
                    		}
                		}
                		
					}
				}
			}
			else if( gs.getConfig().godMode == 2 )
			{
				setQQVIP((byte)2);
			}
			else if( gs.getConfig().id == 11001 // test
					&& this.openID != null && this.openID.equalsIgnoreCase("ofkq1uD99hlV8wvh6VPPQoI0S6ys") 
					&& this.id == 2575 )
			{
				if( this.arenaGenerals != null && ! this.arenaGenerals.isEmpty() )
				{
					boolean bFoundChild = false;
					boolean bFoundOther = false;
					for(short gid : this.arenaGenerals )
					{
						if( gid == (short)55 )
							bFoundChild = true;
						else if( gid > 0 )
							bFoundOther = true;
					}
					if( bFoundChild && ! bFoundOther )
					{
						if( this.stone < 10000 )
						{
							this.stone += 10000;
							if( this.stonePayAlpha < 500000 )
							{
								this.stonePayAlpha += 5000;
								updateVipLevel();
							}
						}
					}
				}
			}
			//
			dayRefresh();
			brief = copyBriefWithoutLock();
			generals = copyGeneralsWithoutLock();
			pets = copyPetsWithoutLock();
			petsFeeding = copyFeedingPetsWithoutLock();
			petsDeform = copyPetDeformsWithoutLock(false, takeZanTimes);
			items = copyItemsWithoutLock();
			equips = copyEquipsWithoutLock();
			combatFightLogs = copyCombatFightLogsWithoutLock();
			combatResetLogs = copyCombatResetLogsWithoutLock();
			combatPos = copyCombatPosWithoutLock();
			taskPos = copyTaskPosWithoutLock();
			generalStones =  getGeneralStones();
			friendsNoSearch = friend.getFriendsNoSearch();
			friends = friend.friend.kdClone().friends;
			lvlVIP = this.lvlVIP;
			bCanCheckin = canCheckIn();
			bCanActTip = gs.getGameActivities().canShowDisplayActivityOpenedTip();
			randTick = GameData.getInstance().getRoleRandInterval();
			loginGiftLogLogin(gs.getTime());
			//
			if( (dayFlag & DAY_GIFT_FLAG_ALPHA_GIFT) == 0 )
			{
				//bAlphaGift = true;
				dayFlag |= DAY_GIFT_FLAG_ALPHA_GIFT;
			}
			forceThiefStamp = 0;
		}
		SBean.DBRoleBrief br = getDBRoleBriefWithoutLock();
		LoginManager.RoleBriefCache c = getCacheDataWithoutLock();
		gs.getLoginManager().actives.put(br);
		gs.getLoginManager().updateRoleBriefCache(br.id, c);
		gs.getLoginManager().updateLevelRoleCache(br.id, br.lvl);
		//
		List<String> luaPatch = gs.getHotChecker().getLuaPatch();
		for(String p : luaPatch)
		{
			gs.getRPCManager().sendLuaPacket2(netsid, LuaPacket.encodeLuaPatch(p));
		}
		//
		gs.getRPCManager().notifyForceInfoInit(netsid, forceInfo.id, forceInfo.name);
		gs.getRPCManager().notifyUserStones(netsid, generalStones);
		gs.getRPCManager().notifyUserLoginResponse(netsid, reserr, resstate, brief, generals, pets, petsFeeding, petsTop
				, items, equips, combatFightLogs, combatResetLogs, combatPos, taskPos, lvlVIP, getStonePayTotal(), feedTotal, 
				petsDeform, activity, takeZanTimes[0], getRelations(),this.getRoleSeyen());
		if( testNewMail() )
			gs.getRPCManager().notifyMailNew(netsid);
		testBroadcasts();
		gs.getRPCManager().notifyBroadcasts(netsid, broadcasts);
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeCheckinNotice(bCanCheckin));
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeActTipNotice(bCanActTip));
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeCitysNotice(canCityNotice()));
		
		List<Role.EggInfo> eggInfo = syncEggsInfo();
		if( eggInfo != null )
			//gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeEggsInfo(eggInfo, false, this.getSoulBoxNow(gs.getTime())));
		    gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeEggsInfo(eggInfo, false));
		// TODO
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendNoSearchSync(friendsNoSearch));

			final int BATCH_SIZE = 10;
			if( friends.size() <= BATCH_SIZE )
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSync(friends));
			}
			else
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncStart(null));
				final int nTotal = friends.size();
				int start = 0;
				while( start < nTotal)
				{
					int end = start + BATCH_SIZE;
					if( end > nTotal )
						end = nTotal;
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncStart(friends.subList(start, end)));
					start = end;
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncEnd());
			}
		}
		//
		testDTask2Notice();
		{
			List<SBean.OpenIDRecord> recordsLocal = new ArrayList<SBean.OpenIDRecord>();
			List<SBean.OpenIDRecord> recordsRemote = new ArrayList<SBean.OpenIDRecord>();
			for(SBean.DBFriendData e : friends)
			{
				boolean bLocalServer = e.roleID.serverID == gs.getConfig().id; 
				if( e.roleID.serverID > 0 && ( gs.getConfig().godMode == 1 
						|| ExchangeManager.needUpdate(bLocalServer, e.lastPropSyncTime, e.prop.lastLoginTime, now) ) )
				{
					if( bLocalServer )
						recordsLocal.add(new SBean.OpenIDRecord(e.openID, e.roleID));
					else
						recordsRemote.add(new SBean.OpenIDRecord(e.openID, e.roleID));
				}
			}
			if( ! recordsLocal.isEmpty() || ! recordsRemote.isEmpty() )
			{
				gs.getExchangeManager().updateFriendProp(id, recordsLocal, recordsRemote);
			}
			updateAllInvitationFriendsInfo();
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeShopRandBrief(shopRandGetBrief()));
		//
		// TODO
		//if( bAlphaGift )
		//{
		//	List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
		//	//attItemLst.add(new SBean.DropEntry(GameData.COMMON_TYPE_ITEM, (byte)50, (short)1));
		//	attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, 100000));
		//	attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, 1000));
		//	gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_ALPHA_GIFT, "", "", 0, true, attLst);
		//}
		//if( bAlphaMail )
		//{
		//	List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
		//	gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_ALPHA_MAIL, "", "", 15 * 86400, true, attLst);
		//}
		if (newuserMail)
		{
			gs.getGameActivities().getNewUserSysMailActivity().sendNewUserSysMail(id);
		}
		if (this.isBanPLay())
			gs.getRPCManager().notifyBanPlayInfo(netsid, this.getBanPlayInfo());
		gs.getRPCManager().notifyForceApplyExist(netsid, gs.getForceManager().isApplyExist(forceInfo.id, id));
		gs.getRPCManager().notifyArenaAttacked(netsid, gs.getArenaManager().isAttacked(id));
		gs.getRPCManager().notifySuperArenaAttacked(netsid, gs.getArenaManager().getSuperArena().isAttacked(id));

		if( msdkInfo.loginType == SBean.UserLoginRequest.eLoginNormal
				&& gs.getMidas().getHTTPTaskQueueSize() < gs.getConfig().httpTaskMaxCountL1 )
			queryQQVIP();
		if( msdkInfo.loginType == SBean.UserLoginRequest.eLoginNormal 
			&& (dayFlag & DAY_FLAG_REPORT_SCORE ) == 0 )
		{
			dayFlag |= DAY_FLAG_REPORT_SCORE;
			if( gs.getConfig().qqReportScore == 1 && gs.getMidas().getHTTPTaskQueueSize() < gs.getConfig().httpTaskMaxCountL2 )
			{
				gs.getMidas().reportScore(msdkInfo.openID, msdkInfo.openKey, lvl, brief.allscore);
			}
		}
		logBattleGeneralsInfo();
		if( gs.getConfig().godMode == 1 || gs.getConfig().godMode == 2 )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeQQVIPSync(getQQVIP(), isAddQQVip(1), isAddQQVip(2)));
			testIOSSendQQVipRewardsMail();
		}
		
		if (forceInfo.id > 0)
			gs.getForceManager().mercInitSync(netsid, forceInfo.id, this);
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSyncVipShow(vipShow > 0));
		
		if (msdkInfo.loginType != SBean.UserLoginRequest.eLoginReconnect && lvlVIP >= 16 && vipShow > 0)
			gs.getLoginManager().announceGMMessage("VIPLOGIN:" + name, (byte)1);
		
		/*
		DBRole dbrole = copyDBRoleWithoutLock();
		try {
			Stream.storeObjLE(dbrole, new File("Role.bean"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	static class BattleGeneralInfo
	{
		short id;
		short lvl;
		byte evoLvl;
		byte advlvl;
		
		BattleGeneralInfo()
		{
			
		}
	}
	
	static class BattlePetInfo
	{
		short id;
		short lvl;
		byte evoLvl;
		BattlePetInfo()
		{
			
		}
	}
	
	private void logBattleGeneralsInfo()
	{
		BattleGeneralInfo[] generalsinfo = new BattleGeneralInfo[5];
		for (int i = 0; i < 5; ++i)
		{
			generalsinfo[i] = new BattleGeneralInfo();
		}
		BattlePetInfo petinfo = new BattlePetInfo();
		int power = 0;
		synchronized( this )
		{
			for (int i = 0; i < 5; ++i)
			{
				BattleGeneralInfo info = generalsinfo[i];
				short gid = arenaGenerals.get(i);
				General g = generals.get(gid);
				if (g != null)
				{
					info.id = g.id;
					info.lvl = g.lvl;
					info.evoLvl = g.evoLvl;
					info.advlvl = g.advLvl;
				}
			}
			short pid = arenaPets.get(0);
			Pet p = pets.get(pid);
			if (p != null)
			{
				petinfo.id = p.id;
				petinfo.lvl = p.lvl;
				petinfo.evoLvl = p.evoLvl;
			}
			power = this.getArenaGeneralsPowerWithoutLock() + this.getArenaPetsPowerWithoutLock();
		}
		gs.getTLogger().logBattleGeneralsState(this, power, generalsinfo[0].id, generalsinfo[0].lvl, generalsinfo[0].evoLvl, generalsinfo[0].advlvl,
				                                            generalsinfo[1].id, generalsinfo[1].lvl, generalsinfo[1].evoLvl, generalsinfo[1].advlvl,
				                                            generalsinfo[2].id, generalsinfo[2].lvl, generalsinfo[2].evoLvl, generalsinfo[2].advlvl,
				                                            generalsinfo[3].id, generalsinfo[3].lvl, generalsinfo[3].evoLvl, generalsinfo[3].advlvl,
				                                            generalsinfo[4].id, generalsinfo[4].lvl, generalsinfo[4].evoLvl, generalsinfo[4].advlvl,
				                                            petinfo.id, petinfo.lvl, petinfo.evoLvl, TLog.GENERALEBATTLE_ARENA_DEFENSE);
	}
	
	private void testLoginGiftMail()
	{
		List<GameActivities.LoginGiftMailConfig> cfgs = gs.getGameActivities().getLoginGiftMailActivity().getOpenedConfigs();
		for (GameActivities.LoginGiftMailConfig cfg : cfgs)
		{
			int id = cfg.getID();
			if (!loginGiftMail.contains(id))
			{
				gs.getGameActivities().getLoginGiftMailActivity().sendLoginGiftMail(this.id, this.lvl, cfg);
				loginGiftMail.add(id);
			}
		}	
	}
	
	private void testRemoveForceWarItems()
	{
		SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(gs.getTime());
		if (cfg != null)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			synchronized( this )
			{
				if (cfg.id != lastForceWarID)
				{
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					SBean.ForceBattlesCFGS cfgs = GameData.getInstance().getForceBattleCfgs();
					for (SBean.ItemPlusEffectCFGS e : cfgs.itemPlusEffects)
					{
						SBean.ItemCFGS icfg = gs.getGameData().getItemCFG(e.itemID);
						if (icfg != null)
						{
							int count = getItemCount(e.itemID); 
							if (count > 0)
							{
								commonFlowRecord.addChange(delItem(e.itemID, count));
								commonFlowRecord.addChange(addMoney(icfg.price * count));
							}
						}
					}
					commonFlowRecord.setEvent(TLog.AT_FORCE_WAR_DESTORY_ITEMS);
					commonFlowRecord.setArg(lastForceWarID, 0);
					tlogEvent.addCommonFlow(commonFlowRecord);
					lastForceWarID = cfg.id;
					
				}
			}
			gs.getTLogger().logEvent(this, tlogEvent);
		}
	}
	
	public static class PrisonOPRes
	{
		public byte res;
		public boolean sur;
		public byte jiecao;
		public byte jiecao2;
		public byte jiecao3;
	}

	public void dayRefresh()
	{
		int newDay = gs.getDayCommon();
		if( newDay != dayCommon )
		{
			dayCommon = newDay;
			lvlDay = lvl;
			dayFlag = 0;
			shopNormal.clear();
			shopArena.clear();
			shopMarch.clear();
			shopRand1.clear();
			shopRand2.clear();
			shopForce.clear();
			for(int i = 0; i < SHOP_RICH_COUNT; ++i)
				shopRich[i].clear();
			shopSuperArena.clear();
			shopDuel.clear();
			shopBless.clear();
			combatFightLogs.clear();
			combatResetLogs.clear();
			for(SBean.DBEggLog e : eggLogs)
				e.timesToday = 0;
			for(SBean.DBCombatEventLog e : combatEventLogs)
			{
				e.timesToday = 0;
				e.lastTime = 0;
			}
			forceInfo.clear();
			friend.clear();
			dailyTask2Logs.clear();
			dailyActivity = 0;
			dailyActivityRewards.clear();
			vitUsedToday = 0;
			buyMoneyTimes = 0;
			buyVitTimes = 0;
			buySkillPointTimes = 0;
			arenaState.timesBuy = 0;
			arenaState.timesUsed = 0;
			if (superArena != null)
			{
				superArena.timesBuy = 0;
				superArena.timesUsed = 0;
			}
			marchState.timesUsed = 0;
			forceInfo.mobai.clear();
			forceInfo.mobaiReward = 0;
			forceInfo.superMobai = 0;
			feedTotal = 0;
			activity = 0;
			activityRewards.clear();
			if (marchPointReward > 1)
				marchPointReward --;
			for (SBean.DBPetDeform p : petDeforms.values()) {
				p.buyTryTimes = 0;
				p.zanTimes = 0;
				p.takeTimes = 0;
				p.tryTimes = 0;
				p.totalReward = 0;
				p.feedTimes = 0;
				
				refreshDeformFeedItems(p, gs);
			}
			if (duel != null) {
				duel.rewardTimes = 0;
				duel.fightTimes = 0;
				duel.buyTimes = 0;
			}
			if (sura != null) {
				sura.stage = 0;
				sura.deadGids.clear();
				sura.activatedGids.clear();
			}
			mercGeneralSura = null;
			if (treasureLimitCnt != null) {
			    treasureLimitCnt.useTreasureCnt = 0;
			    treasureLimitCnt.isTimeCntAdd = 0;
			}
			
			
//			setHeroesBossInfo();
			setPowerRank();
		}
	}
	
	public void setExpirateBossInfo(){
		if(expiratBossInfo!=null){
			expiratBossInfo.damage=0;
			expiratBossInfo.damageHis.clear();
			for(DBExpiratBossTimes i :expiratBossInfo.times){
				SBean.DBExpiratBossDamage damageHis= new SBean.DBExpiratBossDamage(i.id,i.damage);
				expiratBossInfo.damageHis.add(damageHis);
			}
			expiratBossInfo.times.clear();
			expiratBossInfo.level=0;
		}
	}
	
	public void setHeroesBossInfo(){
		if(heroesBossInfo!=null){
			heroesBossInfo.times.clear();
			heroesBossInfo.buffCount=0;
			heroesBossInfo.ranks.clear();
			heroesBossInfo.score1=0;
			heroesBossInfo.score2=0;
			heroesBossInfo.score3=0;
		}
	}
	
	private void setPowerRank()
	{
		if (lvl >= gs.getGameData().getArenaCFG().lvlReq) {
			int maxPowerFront = 0;
			int maxPowerMiddle = 0;
			int maxPowerBack = 0;
			int backUpPower1 = 0;
			int backUpPower2 = 0;
			short maxPowerFrontId = 0;
			short maxPowerMiddleId = 0;
			short maxPowerBackId = 0;
			short backUpId1 = 0;
			short backUpId2 = 0;
			for(General g : generals.values()) {
				int power = calcGeneralPower(g.id);
				SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(g.id);
				if (gcfg.stance == 1 && power > maxPowerFront) {
					if (maxPowerFront > backUpPower1) {
						backUpPower1 = maxPowerFront;
						backUpId1 = maxPowerFrontId;
					}
					else if (maxPowerFront > backUpPower2) {
						backUpPower2 = maxPowerFront;
						backUpId2 = maxPowerFrontId;
					}
					
					maxPowerFront = power;
					maxPowerFrontId = g.id;
				}
				else if (gcfg.stance == 2 && power > maxPowerMiddle) {
					if (maxPowerMiddle > backUpPower1) {
						backUpPower1 = maxPowerMiddle;
						backUpId1 = maxPowerMiddleId;
					}
					else if (maxPowerMiddle > backUpPower2) {
						backUpPower2 = maxPowerMiddle;
						backUpId2 = maxPowerMiddleId;
					}
					
					maxPowerMiddle = power;
					maxPowerMiddleId = g.id;
				}
				else if (gcfg.stance == 3 && power > maxPowerBack) {
					if (maxPowerBack > backUpPower1) {
						backUpPower1 = maxPowerBack;
						backUpId1 = maxPowerBackId;
					}
					else if (maxPowerBack > backUpPower2) {
						backUpPower2 = maxPowerBack;
						backUpId2 = maxPowerBackId;
					}
					
					maxPowerBack = power;
					maxPowerBackId = g.id;
				}
				else {
					if (power > backUpPower1) {
						backUpPower1 = power;
						backUpId1 = g.id;
					}
					else if (power > backUpPower2) {
						backUpPower2 = power;
						backUpId2 = g.id;
					}					
				}
			}
			
			List<Short> gids = new ArrayList<Short>();
			if (maxPowerFrontId > 0)
				gids.add(maxPowerFrontId);
			if (maxPowerMiddleId > 0)
				gids.add(maxPowerMiddleId);
			if (maxPowerBackId > 0)
				gids.add(maxPowerBackId);
			if (backUpId1 > 0)
				gids.add(backUpId1);
			if (backUpId2 > 0)
				gids.add(backUpId2);
			
			int maxPowerPet = 0;
			short maxPowerPetId = 0;
			for(Pet p : pets.values()) {
				int power = calcPetPower(p.id);
				if (power > maxPowerPet) {
					maxPowerPet = power;
					maxPowerPetId = p.id;
				}
			}
			
			int power = maxPowerFront + maxPowerMiddle + maxPowerBack + backUpPower1 + backUpPower2 + maxPowerPet;
			gs.getMarchManager().setRank(new SBean.DBPowerRank(id, power, gids, maxPowerPetId, gs.getTime()));				
		}		
	}
	
	
	public boolean activityRefresh()
	{
		int newDay = gs.getDayCommon();
		if( newDay != dayCommon )
		{
			return true;
		}
		return false;
	}
	
	public List<Byte> copyPayLvlStateWithoutLock()
	{
		return new ArrayList<Byte>(payLvlState);
	}
	
	public synchronized List<Byte> copyPayLvlStateWithLock()
	{
		return new ArrayList<Byte>(payLvlState);
	}
	
	static class FunctionCheck
	{
		public short sprCost;
		public int moneyCost;
	}
	
	public synchronized ForceInfo getForceInfo()
	{
		return forceInfo.kdclone();
	}
	
	public synchronized List<SBean.DBDailyTask2Log> getDailyTask2Info()
	{
		dayRefresh();
		List<SBean.DBDailyTask2Log> lst = new ArrayList<SBean.DBDailyTask2Log>();
		List<SBean.DailyTask2CFGS> cfgs = gs.getGameData().listDailyTask2CFG();
		for(SBean.DailyTask2CFGS e : cfgs)
		{
			if( e.lvlReq > lvl )
				continue;
			SBean.DBDailyTask2Log d = dailyTask2Logs.get(e.id);
			SBean.DBDailyTask2Log r = null;
			if( d != null )
				r = d.ksClone();
			else
			{
				r = new SBean.DBDailyTask2Log(e.id, (byte)0, (byte)0);
			}
			lst.add(r);
			short lvlReward = lvlDay;
			if( lvlReward < e.lvlReq )
				lvlReward = e.lvlReq;
			if( r.reward == 0 )
			{
				r.reward = (short)lvlReward;
			}
			else
			{
				r.reward = (short)-lvlReward;
			}
		}
		return lst;
	}
	
	public synchronized int getDailyActivity()
	{
		return dailyActivity;
	}
	
	public synchronized List<Byte> getDailyActivityRewards()
	{
		List<Byte> rewards = new ArrayList<Byte>();
		for (byte b : dailyActivityRewards)
			rewards.add(b);
		return rewards;
	}
	
	private TLogger.TLogRecord logDailyTask2(byte id, int times)
	{
		TLogger.DailyTaskRecord record = null;
		if( times <= 0 )
			return record;
		dayRefresh();
		SBean.DailyTask2CFGS cfg = gs.getGameData().getDailyTask2CFG(id);
		if( cfg == null )
			return record;
		SBean.DBDailyTask2Log e = dailyTask2Logs.get(id);
		if( e == null )
		{
			e = new SBean.DBDailyTask2Log(id, (byte)0, (short)0);
			dailyTask2Logs.put(id, e);
		}
		if( e.reward == 1 )
			return record;
		int newTimes = e.times + times;
		if( newTimes > cfg.times )
			newTimes = cfg.times;
		e.times = (byte)newTimes;
		if( cfg.lvlReq > lvl )
			return record;
		if( e.times >= cfg.times )
		{
			logEvent(GameData.EVENTS_NOTICE_DTASK2);
			record = new TLogger.DailyTaskRecord(lvl);
			record.setId(id);
		}
		return record;
	}
	
	private void logEvent(byte etype)
	{
		eventNoticeList.add(etype);
	}
	
	public synchronized short getDayLevel()
	{
		return lvlDay;
	}
	
	public boolean getDailyTask2Reward(byte id, short lvlReward[])
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.DailyTaskRewardRecord record = new TLogger.DailyTaskRewardRecord(this.lvl);
		synchronized( this )
		{
			dayRefresh();
			lvlReward[0] = lvlDay;
			SBean.DailyTask2CFGS cfg = gs.getGameData().getDailyTask2CFG(id);
			if( cfg == null )
				return false;
			if( lvl < cfg.lvlReq )
				return false;
			if( lvlReward[0] < cfg.lvlReq )
				lvlReward[0] = cfg.lvlReq;
			
			SBean.DBDailyTask2Log d = dailyTask2Logs.get(id);
			
			if( id == GameData.DAILYTASK2_TYPE_MONTHLY_CARD )
			{
				if( monthlyCardStartTime + gs.getGameData().getMonthlyCardCFG().days * 86400 < gs.getTime() )
					return false;
				if( d != null )
				{
					if( d.reward == 1 )
						return false;
				}
				else
				{
					d = new SBean.DBDailyTask2Log(id, (byte)0, (short)0);
					dailyTask2Logs.put(id, d);
				}
			}
			else if ( id >= GameData.DAILYTASK2_TYPE_FREE_VIT1 && id <= GameData.DAILYTASK2_TYPE_FREE_VIT3 )
			{
				int index = id-GameData.DAILYTASK2_TYPE_FREE_VIT1;
				SBean.FreeVit[] fvs = gs.getGameActivities().getFreePhysicalActivity().getFreeVit();
				int daytime = gs.getTime() % 86400;
				if (fvs == null || index >= fvs.length || fvs[index] == null || daytime < fvs[index].startTime || daytime > fvs[index].endTime)
					return false;
				cfg.reward1Arg1 = fvs[index].vit;
				if( d != null )
				{
					if( d.reward == 1 )
						return false;
				}
				else
				{
					d = new SBean.DBDailyTask2Log(id, (byte)0, (short)0);
					dailyTask2Logs.put(id, d);
				}
			}
			else 
			{
				if( d == null )
					return false;
				if( d.reward == 1 )
					return false;
				if( d.times < cfg.times )
					return false;
			}
			d.reward = 1;
			dailyActivity += cfg.activity;
			switch( cfg.reward1Type )
			{
			case GameData.DAILYTASK2_REWARD_EXP:
				commonFlowRecord.addChanges(addExp(cfg.reward1Arg1 + cfg.reward1Arg2 * lvlReward[0], tlogEvent));
				break;
			case GameData.DAILYTASK2_REWARD_MONEY:
				commonFlowRecord.addChange(addMoney(cfg.reward1Arg1 + cfg.reward1Arg2 * lvlReward[0]));
				break;
			case GameData.DAILYTASK2_REWARD_STONE:
				commonFlowRecord.addChange(addStonePresent(cfg.reward1Arg1 + cfg.reward1Arg2 * lvlReward[0]));
				break;
			case GameData.DAILYTASK2_REWARD_ITEM:
				commonFlowRecord.addChange(addItem(cfg.reward1ID, (short)(cfg.reward1Arg1 + cfg.reward1Arg2 * lvlReward[0])));
				break;
			case GameData.DAILYTASK2_REWARD_VIT:
				commonFlowRecord.addChange(addVit2((cfg.reward1Arg1 + cfg.reward1Arg2 * lvlReward[0])));
				break;
			default:
				break;
			}
			switch( cfg.reward2Type )
			{
			case GameData.DAILYTASK2_REWARD_EXP:
				commonFlowRecord.addChanges(addExp(cfg.reward2Arg1 + cfg.reward2Arg2 * lvlReward[0], tlogEvent));
				break;
			case GameData.DAILYTASK2_REWARD_MONEY:
				commonFlowRecord.addChange(addMoney(cfg.reward2Arg1 + cfg.reward2Arg2 * lvlReward[0]));
				break;
			case GameData.DAILYTASK2_REWARD_STONE:
				commonFlowRecord.addChange(addStonePresent(cfg.reward2Arg1 + cfg.reward2Arg2 * lvlReward[0]));
				break;
			case GameData.DAILYTASK2_REWARD_ITEM:
				commonFlowRecord.addChange(addItem(cfg.reward2ID, (short)(cfg.reward2Arg1 + cfg.reward2Arg2 * lvlReward[0])));
				break;
			case GameData.DAILYTASK2_REWARD_VIT:
				commonFlowRecord.addChange(addVit2((cfg.reward2Arg1 + cfg.reward2Arg2 * lvlReward[0])));
				break;
			default:
				break;
			}
		}
		commonFlowRecord.setEvent(TLog.AT_DAILY_TASK_REWARD);
		commonFlowRecord.setArg(id, 0);
		record.setId(id);
		
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public void dailyActivityReward(int reward)
	{
		for (Byte b : dailyActivityRewards) {
			if (b == (byte)reward) {
				gs.getRPCManager().notifyDailyActivityReward(netsid, reward, false, null);											
				return;
			}
		}
		
		DailyActivityRewardCFGS rcfg = gs.getGameData().getDailyActivityReward(reward-1);
		if (rcfg != null) {
			if (dailyActivity >= rcfg.activity) {
				for (SBean.DropEntryNew e : rcfg.drops)
					addDropNew(e);
				dailyActivityRewards.add((byte)reward);
				gs.getRPCManager().notifyDailyActivityReward(netsid, reward, true, rcfg.drops);
				testDTask2Notice();
			}
			else 
				gs.getRPCManager().notifyDailyActivityReward(netsid, reward, false, null);
		}
	}
	
	public VIPInfo syncVIPInfo()
	{
		VIPInfo info = new VIPInfo();
		synchronized( this )
		{
			info.payTotal = getStonePayTotal();
			info.lvl = lvlVIP;
			info.vipRewards = new ArrayList<Byte>();
			info.vipRewards.addAll(vipRewards);
		}
		return info;
	}
	
	public List<EggInfo> syncEggsInfo()
	{
		List<EggInfo> lst = new ArrayList<EggInfo>();
		
		byte[] eggids = { GameData.EGG_MONEY, GameData.EGG_STONE };
		
		synchronized( this )
		{
			if( ! testFlag(Role.ROLE_FLAG_EGG) )
				return null;
			dayRefresh();
			for(byte eid : eggids)
			{
				SBean.EggCFGS ecfg = gs.getGameData().getEggCFG(eid);
				if( ecfg == null )
					continue;
				EggInfo e = new EggInfo();
				e.id = eid;
				if( ecfg.freeTimes > 0 )
				{
					e.timesAll = ecfg.freeTimes;
					e.timesToday = 0;
					for(SBean.DBEggLog el : eggLogs)
					{
						if( el.eggID == eid )
						{
							e.timesToday = el.timesToday;
							break;
						}
					}
				}
				else
				{
					e.timesAll = 0;
					e.timesToday = 0;
				}
				e.freeTime = 0;
				for(SBean.DBEggLog el : eggLogs)
				{
					if( el.eggID == eid )
					{
						e.times10 = el.timesAll10;
						e.firstEgg = el.firstEgg;
						if( el.lastTime > 0 )
						{
							e.freeTime = el.lastTime + ecfg.freeCool * 60;
						}
						break;
					}
				}
				lst.add(e);
			}
		}
		return lst;
	}
	
	public SoulBoxInfo getSoulBoxNow(int now)
	{
		SoulBoxInfo info = null;
		List<SBean.SoulBoxCFGS> soulboxs = GameData.getInstance().getSoulBoxCfgs();
		for(SBean.SoulBoxCFGS e : soulboxs)
		{
			int timeStart = e.startTime + 17 * 60*60;
			int timeEnd = timeStart + 86400 * e.dayHotPoint.size();
			if( now >= timeStart && now < timeEnd )
			{
				byte index = (byte)((now - timeStart)/86400);
				info = new SoulBoxInfo();
				info.cfg = e;
				info.dayIndex = index;
				if (optionalHotPoint != null) {
					if (optionalHotPoint.sbid != e.id)
						optionalHotPoint = null;
				}
				
				if (optionalHotPoint != null)
					info.optionalGid = optionalHotPoint.gid;
				else
					info.optionalGid = 0;
				return info;
			}
		}
		return null;
	}
	
	public boolean optionalSoulBox(short gid)
	{
		int now = gs.getTime();
		List<SBean.SoulBoxCFGS> soulboxs = GameData.getInstance().getSoulBoxCfgs();
		for(SBean.SoulBoxCFGS e : soulboxs)
		{
			int timeStart = e.startTime + 17 * 60*60;
			int timeEnd = timeStart + 86400 * e.dayHotPoint.size();
			if( now >= timeStart && now < timeEnd )
			{
				for (short hpgid : e.optionalHotPoint)
					if (gid == hpgid) {
						optionalHotPoint = new SBean.DBOptionalHotPoint(e.id, gid);
						return true;
					}
			}
		}
		
		return false;
	}
	
	public int getStoneWithoutLock()
	{
		return stone;
	}
	
	public static class MedalNotice
	{
		public MedalNotice(byte id, byte seq)
		{
			this.id = id;
			this.seq = seq;
		}
		byte id;
		byte seq;
	}
	
//	private void logVIP(byte lvlVIP)
//	{
//		vipNoticeList.add(lvlVIP);
//	}
	
	public synchronized byte nextListForceLevel()
	{
		if( ++lvlForceSeek > GameData.MAX_FORCE_LEVEL )
			lvlForceSeek = 1;
		return lvlForceSeek;
	}
	
	public void forceCmnRes(final int fid, int newStamp)
	{
		synchronized( this )
		{
			bDBLock = false;
		}
		gs.getRPCManager().notifyForceCmnRes(netsid, fid, newStamp);
	}
	
	public synchronized boolean alreadyInForce(int fid)
	{
		return forceInfo.isInForce(fid);
	}
	public synchronized boolean canJoinForce()
	{
		if( forceInfo.isInForce() )
			return false;
		SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
		if( lvl < cfg.lvlCreate )
			return false;
		return true;
	}
	
	public void testDTask2Notice()
	{
		boolean bOK = false;
		synchronized( this )
		{
			bOK = checkDTask2WithoutLock();
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeDailyTask2Notice(bOK));
	}
	
	public boolean checkDTask2WithoutLock()
	{
		List<SBean.DailyTask2CFGS> cfgs = gs.getGameData().listDailyTask2CFG();
		for(SBean.DailyTask2CFGS e : cfgs)
		{
			if( e.lvlReq > lvl )
				continue;
			SBean.DBDailyTask2Log d = dailyTask2Logs.get(e.id);
			if( d == null )
			{
				d = new SBean.DBDailyTask2Log(e.id, (byte)0, (short)0);
				dailyTask2Logs.put(e.id, d);
			}
			if( d.id == GameData.DAILYTASK2_TYPE_MONTHLY_CARD && gs.getConfig().iosExamineServer == 0)
			{
				if( monthlyCardStartTime + gs.getGameData().getMonthlyCardCFG().days * 86400 < gs.getTime() )
					continue;
				if( d.reward == 1 )
					continue;
				return true;
			}
			else if ( d.id >= GameData.DAILYTASK2_TYPE_FREE_VIT1 && d.id <= GameData.DAILYTASK2_TYPE_FREE_VIT3 )
			{
				int index = d.id-GameData.DAILYTASK2_TYPE_FREE_VIT1;
				SBean.FreeVit[] fvs = gs.getGameActivities().getFreePhysicalActivity().getFreeVit();
				
				int daytime = gs.getTime() % 86400;
				if (fvs == null || index >= fvs.length || fvs[index] == null || daytime < fvs[index].startTime || daytime > fvs[index].endTime)
					continue;
				if( d.reward == 1 )
					continue;
				return true;
			}
			else if( d.reward == 0 && d.times >= e.times )
				return true;
		}
		List<SBean.DailyActivityRewardCFGS> dcfg = gs.getGameData().getDailyActivityRewardsCFG();
		byte index = 1;
		for (SBean.DailyActivityRewardCFGS d : dcfg) {
			if (dailyActivity >= d.activity) {
				boolean found = false;
				for (byte b : dailyActivityRewards) {
					if (b == index) {
						found = true;
						break;
					}
				}
				if (!found)
					return true;
			}
			index ++;
		}
		return false;
	}
	
	public boolean createForce(short iconID, String fname)
	{
		//String logMoneyType=new String();
		//int logCount=0;
		
		synchronized( this )
		{
			if( bDBLock )
				return false;
			if( forceInfo.isInForce() )
				return false;
			SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
			if( lvl < cfg.lvlCreate )
				return false;
			if( stone < cfg.stoneCreate )
				return false;
			if( forceInfo.joinTime + 86400 > gs.getTime() )
				return false;
			//锟斤拷锟斤拷锟斤拷锟斤拷锟揭拷锟角拷锟斤拷锟斤拷锟绞碉拷锟绞癸拷锟�锟斤拷掖锟斤拷锟斤拷晒锟斤拷锟绞癸拷锟�
			useStone(cfg.stoneCreate);
			//stone -= cfg.stoneCreate;
			//logMoneyType = "stone";
			//logCount=cfg.stoneCreate;
			bDBLock = true;
		}
		gs.getForceManager().createForce(this, fname, iconID);
		return true;
	}
	
	public synchronized int getForceID()
	{
		return forceInfo.id;
	}
	
//	interface SendForceMailCallback
//	{
//		void onCallback(int errorCode);
//	}
//	public void sendForceMail(int fid, String title, String content, final SendForceMailCallback callback)
//	{
//		final String rtitle = gs.getLoginManager().checkUserInput(title, gs.getGameData().getUserInputCFG().mail, false, true);
//		if (rtitle == null)
//		{
//			callback.onCallback(-1);;
//		}
//		final String rcontent = gs.getLoginManager().checkUserInput(content, gs.getGameData().getUserInputCFG().sysMail, false, true);
//		if (rcontent == null)
//		{
//			callback.onCallback(-2);;
//		}
//		if (fid <= 0 || fid != forceInfo.id)
//		{
//			callback.onCallback(-3);
//		}
//		else
//		{
//			gs.getForceManager().getForceMembersRoleID(fid, new ForceManager.QueryForceMembersCallback() 
//			{
//				@Override
//				public void onCallback(boolean success, int headID, List<Integer> forceMembers) 
//				{
//					if (!success)
//					{
//						callback.onCallback(-3);
//					}
//					else
//					{
//						if (headID != Role.this.id)
//						{
//							callback.onCallback(-4);
//						}
//						else
//						{
//							List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
//							for (int rid : forceMembers)
//							{
//								gs.getLoginManager().userSendMessage(Role.this.name, rid, rtitle, rcontent, 0, true, attLst);
//								
//							}
//							callback.onCallback(0);
//						}
//					}
//					
//				}
//			});	
//		}
//	}
	
	/*
    <macro name="SNS_RECEICEBUNS"             value="5"    desc="锟斤拷取锟斤拷锟斤拷"/>
    <macro name="SNS_RECEICEEXPITEMS"         value="7"    desc="锟斤拷取锟斤拷锟斤拷锟斤拷锟�/>
    <macro name="SNS_RECEICEMONEY"            value="9"    desc="锟斤拷取锟斤拷锟�/>
    */
	//
	private void logSNS(int type, int cnt)
	{
		if( cnt <= 0 )
			return;
		gs.getTLogger().logSns(this, cnt, cnt, type, 0);
	}
	private short getEquipCount(short eid)
	{
		Short s = equips.get(eid);
		if( s == null )
			return 0;
		return s.shortValue();
	}
	
	public int getItemCount(short iid)
	{
		Integer s = items.get(iid);
		if( s == null )
			return 0;
		return s;//.shortValue()
	}
	
	private short getGeneralEvo(short gid)
	{
		General g = generals.get(gid);
		if ( g == null )
			return 0;
		return g.evoLvl;
	}
	
	public boolean generalsSetEquip(short gid, short eid, byte pos)
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
			SBean.EquipCFGS ecfg = gs.getGameData().getEquipCFG(eid);
			if( gcfg == null || ecfg == null )
				return false;
			General g = generals.get(gid);
			if( g == null )
				return false;
			if( getEquipCount(eid) <= 0 )
				return false;
			if( pos < 1 || pos > GameData.GENERAL_EQUIP_COUNT )
				return false;
			if( g.equips.get(pos-1).lvl >= 0 )
				return false;
			if( g.lvl < ecfg.lvlReq )
				return false;
			int index = (g.advLvl-1) * GameData.GENERAL_EQUIP_COUNT + (pos-1);
			if( index < 0 || index >= gcfg.equips.size() )
				return false;
			if( gcfg.equips.get(index) != eid )
				return false;
			g.equips.set(pos-1, new SBean.DBGeneralEquip((byte)0, (short)0));
			commonFlowRecord.addChange(delEquip(eid, 1));
		}
		commonFlowRecord.setEvent(TLog.AT_GENERAL_SET_EQUIP);
		commonFlowRecord.setArg(pos, gid, eid);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean generalsGildEquip(short gid, short eid, byte pos, List<SBean.DropEntryNew> matList)
	{
		SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
		SBean.EquipCFGS ecfg = gs.getGameData().getEquipCFG(eid);
		if( gcfg == null || ecfg == null )
			return false;
		if( pos < 1 || pos > GameData.GENERAL_EQUIP_COUNT )
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.EquipGildRecord egRecord = new TLogger.EquipGildRecord(this.lvl);
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;		
			SBean.DBGeneralEquip ge = g.equips.get(pos-1); 
			if( ge.lvl < 0 )
				return false;
			int index = (g.advLvl-1) * GameData.GENERAL_EQUIP_COUNT + (pos-1);
			if( index < 0 || index >= gcfg.equips.size() )
				return false;
			if( gcfg.equips.get(index) != eid )
				return false;
			SBean.EquipRankCFGS rcfg = GameData.getInstance().getEquipCmnCFG().rank.get(ecfg.rank);
			if( rcfg == null )
				return false;
			byte lvlMax = (byte)rcfg.gild.size();
			if( ge.lvl >= lvlMax )
				return false;
			Set<Short> itemIDs = new TreeSet<Short>();
			Set<Short> equipIDs = new TreeSet<Short>();
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					if( itemIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getItemCount(e.id) < e.arg )
						return false;
					SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(e.id);
					if( icfg == null || icfg.gildVal < 0 )
						return false;
					itemIDs.add(e.id);
				}
				else
				{
					if( equipIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getEquipCount(e.id) < e.arg )
						return false;
					SBean.EquipCFGS ecfg2 = GameData.getInstance().getEquipCFG(e.id);
					if( ecfg2 == null || ecfg2.gildVal < 0 )
						return false;
					equipIDs.add(e.id);
				}
			}
			int sum = 0;
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(e.id);
					sum += icfg.gildVal * e.arg;
				}
				else
				{
					SBean.EquipCFGS ecfg2 = GameData.getInstance().getEquipCFG(e.id);
					sum += ecfg2.gildVal * e.arg;
				}
			}
			SBean.DBGeneralEquip newge = new SBean.DBGeneralEquip(ge.lvl, ge.exp);
			int moneyNeed = 0;
			boolean bUP = false;
			while( sum > 0 )
			{
				int expUp = rcfg.gild.get(newge.lvl);
				int expNeed = expUp - newge.exp;
				if( expNeed < 0 )
					expNeed = 0;
				if( sum < expNeed )
				{
					newge.exp += sum;
					moneyNeed += sum * ecfg.gildPrice;
					break;
				}
				++newge.lvl;
				bUP = true;
				newge.exp = 0;
				sum -= expNeed;
				moneyNeed += expNeed * ecfg.gildPrice;
				if( newge.lvl >= lvlMax )
					break;
			}
			if( moneyNeed > money )
				return false;
			commonFlowRecord.addChange(useMoney(moneyNeed));
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					commonFlowRecord.addChange(delItem(e.id, e.arg));
				}
				else
				{
					commonFlowRecord.addChange(delEquip(e.id, e.arg));
				}
			}
			g.equips.set(pos-1, newge);
			if( bUP )
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_GILD, 1));
			commonFlowRecord.setEvent(TLog.AT_GENERAL_EQUIP_GILD);
			commonFlowRecord.setArg(pos, newge.lvl, gid, eid);
			egRecord.setEquipGildInfo(gid, eid, pos, newge.lvl);
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(egRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean generalsUpgradeAdv(short gid, List<SBean.DropEntryNew> drops, int[] err)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
			if( g.advLvl >= GameData.MAX_GENERAL_ADV_LEVEL ) {
				err[0] = -1;
				return false;
			}
			int n = 0;
			for(SBean.DBGeneralEquip e : g.equips)
			{
				if( e.lvl >= 0 )
				{
					++n;
				}
			}
			if( n < GameData.GENERAL_EQUIP_COUNT )
				return false;
			Map<Short, Integer> backMap = new HashMap<Short, Integer>();
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
			{
				SBean.DBGeneralEquip equip = g.equips.get(i);
				SBean.EquipCmnCFGS cfg = gs.getGameData().getEquipCmnCFG();
				if (equip.lvl > 0) {
					int index = equip.lvl - 1;
					if (index < cfg.gildBack.size()) {
						List<SBean.DropEntryNew> back = cfg.gildBack.get(index).drops;
						for (SBean.DropEntryNew d : back) {
							commonFlowRecord.addChange(addDropNew(d));
							Integer count = backMap.get(d.id);
							if (count == null)
								count = 0;
							backMap.put(d.id, count + d.arg);
						}
					}
				}
				g.equips.set(i, new SBean.DBGeneralEquip((byte)-1, (short)0));
			}
			for (Map.Entry<Short, Integer> e : backMap.entrySet())
				drops.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, e.getKey(), e.getValue()));
			TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, g.lvl, g.evoLvl, g.advLvl);
			g.advLvl++;
			grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_ADVUP, g.lvl, g.evoLvl, g.advLvl);
			tlogEvent.addRecord(grecord);
			commonFlowRecord.setEvent(TLog.AT_GENERAL_ADV);
			commonFlowRecord.setArg(gid, g.lvl, g.evoLvl, g.advLvl);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean generalsUpgradeEvo(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
			if( g.evoLvl >= GameData.MAX_GENERAL_EVO_LEVEL )
				return false;
			SBean.GeneralCmnCFGS cmncfg = gs.getGameData().getGeneralCmnCFG();
			int moneyNeed = cmncfg.evoLevels.get(g.evoLvl).money;
			if( money < moneyNeed )
				return false;
			SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
			if( gcfg == null )
				return false;
			short iid = gcfg.stoneID;
			short pieceCountNeed = cmncfg.evoLevels.get(g.evoLvl).stone;
			if( getItemCount(iid) < pieceCountNeed )
				return false;
			commonFlowRecord.addChange(delItem(iid, pieceCountNeed));
			commonFlowRecord.addChange(useMoney(moneyNeed));
			TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, g.lvl, g.evoLvl, g.advLvl);
			g.evoLvl++;
			grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_STARUP, g.lvl, g.evoLvl, g.advLvl);
			tlogEvent.addRecord(grecord);
		}
		commonFlowRecord.setEvent(TLog.AT_GENERAL_EVOLUTION);
		commonFlowRecord.setArg(gid, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean petsUpgradeEvo(short pid)
	{
		Pet p = pets.get(pid);
		SBean.DBPetDeform deform = petDeforms.get(pid);
		if( p == null || deform == null )
			return false;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( p.evoLvl >= GameData.MAX_PET_EVO_LEVEL )
				return false;
			SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(pid, isPetDeformed(deform.deformStage));
			short pieceID = cfg.pieceID;
			int pieceCnt = cfg.evo.get(p.evoLvl-1).pieceCountEvo;
			int moneyNeed = cfg.pieceMoney * pieceCnt;
			if( money < moneyNeed )
				return false;
			
			if( getItemCount(pieceID) < pieceCnt )
				return false;
			commonFlowRecord.addChange(delItem(pieceID, pieceCnt));
			commonFlowRecord.addChange(useMoney(moneyNeed));
			p.evoLvl++;
		}
		commonFlowRecord.setEvent(TLog.AT_PET_EVOLUTION);
		commonFlowRecord.setArg(pid, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean petsSetTop(short pid)
	{
		if( ! pets.containsKey(pid) )
			return false;
		petsTop = pid;
		return true;
	}
	
	public boolean petsPoundUpgrade()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			int capNow = petsFeeding.size();
			SBean.PetCmnCFGS cfg = GameData.getInstance().getPetCmnCFG();
			if( capNow >= cfg.poundCapMax )
				return false;
			if( capNow > cfg.poundUpgradeCost.size() )
				return false;
			int cost = cfg.poundUpgradeCost.get(capNow-1);
			if( stone < cost )
				return false;
			// TODO tlog
			commonFlowRecord.addChange(useStone(cost));
			petsFeeding.add(new SBean.DBFeedingPet((short)-1, (short)0, 0));
			commonFlowRecord.setEvent(TLog.AT_PET_POUND_UPGRADE);
			commonFlowRecord.setArg(petsFeeding.size());
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(new TLogger.PetPoundRecord(TLog.PETPOUNDEVENT_UPGRADE));
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean petsPoundPut(short pid)
	{
		synchronized (this)
		{
			Pet p = pets.get(pid);
			if( p == null )
				return false;
			
			SBean.DBFeedingPet slot = null;
			for(int i = 0; i <petsFeeding.size(); ++i)
			{
				SBean.DBFeedingPet fp = petsFeeding.get(i); 
				if( fp.pid <= 0 )
				{
					if( slot == null )
						slot = fp;
				}
				else if( fp.pid == pid )
					return false;
			}
			
			if( slot == null )
				return false;
			
			slot.pid = pid;
			slot.lvlStart = p.lvl;
			slot.timeStart = gs.getTime();
			
			p.feedTime = slot.timeStart;
		}

		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addRecord(new TLogger.PetPoundRecord(TLog.PETPOUNDEVENT_PETPUT, pid));
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public int petsPoundGet(short pid)
	{
		int expAdd = -1;
		synchronized (this)
		{
			Pet p = pets.get(pid);
			if( p == null )
				return -1;
			
			SBean.DBFeedingPet slot = null;
			for(int i = 0; i <petsFeeding.size(); ++i)
			{
				SBean.DBFeedingPet fp = petsFeeding.get(i); 
				if( fp.pid == pid )
				{
					slot = fp;
					break;
				}
			}
			
			if( slot == null )
				return -1;
			
			SBean.PetCmnCFGS cfg = GameData.getInstance().getPetCmnCFG();
			
			if( slot.lvlStart < 1 || slot.lvlStart > cfg.feedingExp.size() )
				return -1;
			
			int expUnit = cfg.feedingExp.get(slot.lvlStart-1);
			int time = gs.getTime() - slot.timeStart;
			if( time > cfg.feedingMaxTime )
				time = cfg.feedingMaxTime;
			expAdd = (time/(cfg.feedingInterval)) * expUnit;
			if( expAdd < 0 )
				expAdd = 0;
			
			p.addExp(expAdd);
			
			slot.pid = -1;
			slot.lvlStart = 0;
			slot.timeStart = 0;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addRecord(new TLogger.PetPoundRecord(TLog.PETPOUNDEVENT_PETGET, pid));
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return expAdd;
	}
	
	public synchronized SBean.DropEntry petsPoundHungry(short pid)
	{
		SBean.PetFarmCFGS cfg = GameData.getInstance().getPetFarmCFG();
		if (lvl < cfg.lvlOpen)
			return null;
		
		Pet p = pets.get(pid);
		SBean.DBPetDeform deform = petDeforms.get(pid);
		if( p == null || deform == null )
			return null;
		if (p.lvl < cfg.lvlPet)
			return null;
		
		SBean.DBFeedingPet slot = null;
		for(int i = 0; i <petsFeeding.size(); ++i)
		{
			SBean.DBFeedingPet fp = petsFeeding.get(i); 
			if( fp.pid == pid )
			{
				slot = fp;
				break;
			}
		}
		
		if( slot == null )
			return null;
		
		int curTime = gs.getTime();
		int noHungryBegin = gs.getTimeByMinuteOffset(cfg.noHungryTimeBase);
		int noHungryEnd = noHungryBegin + cfg.noHungryTimeSpan;
		if (curTime > noHungryBegin && curTime < noHungryEnd)
			return null;
		
		if (p.hungryDay == 0)
			p.hungryDay = gs.getDayCommon();
		int newDay = gs.getDayCommon();
		if (newDay > p.hungryDay) {
			p.hungryDay = newDay;
			p.hungryTimes = 0;
		}
		
		if (p.hungryTimes == 0) {
			int time = curTime - p.feedTime;
			if( time < cfg.firstHungry )
				return null;			
		}
		else {
			int time = curTime - p.feedTime;
			if (time < cfg.hungryTimeBase)
				return null;
		}
		
		if (feedTotal > cfg.maxEatTimes * petsFeeding.size())
			return null;
		
		if (p.hungryTimes > cfg.petMaxEatTimes)
			return null;
		
		if (p.feedMode != 0)
			return null;
		
		SBean.DropEntry need = p.hungry(isPetDeformed(deform.deformStage));
		if (need == null)
			return null;
		p.hungryTimes ++;
		p.feedMode = 1;
		p.feedNeed = need;
		feedTotal ++;
		
		return need;
	}
	
	public int petsPoundFeed(short pid)
	{
		int gainTotal = -1;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			SBean.PetFarmCFGS cfg = GameData.getInstance().getPetFarmCFG();
			if (lvl < cfg.lvlOpen)
				return gainTotal;
			
			Pet p = pets.get(pid);
			if( p == null )
				return gainTotal;
			if (p.lvl < cfg.lvlPet)
				return gainTotal;
			
			SBean.DBFeedingPet slot = null;
			for(int i = 0; i <petsFeeding.size(); ++i)
			{
				SBean.DBFeedingPet fp = petsFeeding.get(i); 
				if( fp.pid == pid )
				{
					slot = fp;
					break;
				}
			}
			
			if( slot == null )
				return gainTotal;
			
			if (p.feedMode != 1)
				return gainTotal;
			
			TLogger.CommonItemChange record = delDrop(p.feedNeed);
			if (record == null)
				return gainTotal;
			commonFlowRecord.addChange(record);
			byte gain = (byte)(cfg.eatGainBase + GameData.getInstance().getRandom().nextInt(cfg.eatGainFloat + 1));
			p.feedGain += gain;
			p.feedMode = 0;
			p.feedTime = gs.getTime();
			gainTotal = p.feedGain;
			
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if (deform != null) {
				deform.happiness += gain;
				
				checkDeformHappiness(deform, gs);
			}
		}
		commonFlowRecord.setEvent(TLog.AT_PET_FEED);
		commonFlowRecord.setArg(pid, gainTotal);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(new TLogger.PetPoundRecord(TLog.PETPOUNDEVENT_FEED, pid));
		gs.getTLogger().logEvent(this, tlogEvent);
		return gainTotal;
	}
	
	public synchronized boolean petsPoundProduce(short pid)
	{
		SBean.PetFarmCFGS cfg = GameData.getInstance().getPetFarmCFG();
		if (lvl < cfg.lvlOpen)
			return false;
		
		Pet p = pets.get(pid);
		if( p == null )
			return false;
		if (p.lvl < cfg.lvlPet)
			return false;
		
		SBean.DBFeedingPet slot = null;
		for(int i = 0; i <petsFeeding.size(); ++i)
		{
			SBean.DBFeedingPet fp = petsFeeding.get(i); 
			if( fp.pid == pid )
			{
				slot = fp;
				break;
			}
		}
		
		if( slot == null )
			return false;
		
		if (p.feedMode == 2)
			return false;
		
		if (p.feedGain < cfg.produceCost)
			return false;
		
		p.feedGain -= cfg.produceCost;
		p.feedMode = 2;
		p.feedTime = gs.getTime();
		
		return true;
	}
	
	public SBean.DropEntry petsPoundGetProduct(short pid)
	{
		SBean.DropEntry product = null;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			SBean.PetFarmCFGS fcfg = GameData.getInstance().getPetFarmCFG();
			if (lvl < fcfg.lvlOpen)
				return product;
			
			Pet p = pets.get(pid);
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if( p == null || deform == null )
				return product;
			if (p.lvl < fcfg.lvlPet)
				return product;
			
			SBean.DBFeedingPet slot = null;
			for(int i = 0; i <petsFeeding.size(); ++i)
			{
				SBean.DBFeedingPet fp = petsFeeding.get(i); 
				if( fp.pid == pid )
				{
					slot = fp;
					break;
				}
			}
			
			if( slot == null )
				return product;
			
			if (p.feedMode != 2)
				return product;
			
			SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(pid, isPetDeformed(deform.deformStage));
			if (cfg == null)
				return product;
			
			if (gs.getTime() - p.feedTime < cfg.produceTime)
				return product;
			
			product = p.getProduct(isPetDeformed(deform.deformStage));
			if (product == null)
				return product;
			
			byte add = -1;
			for (SBean.PetFarmGrowthRateCFG g : fcfg.growthRateAffect) {
				if (g.growthRateMax > p.growthRate) {
					add = g.addCount;
					break;
				}
			}
			
			int count = fcfg.growthRateAffect.size();
			if (add < 0 && count > 0)
				add = fcfg.growthRateAffect.get(count - 1).addCount;
			if (add > 0)
				product.arg += add;
			
			p.feedGain -= fcfg.produceCost;
			if (p.feedGain < 0)
				p.feedGain = 0;
			commonFlowRecord.addChange(addDrop(product));
			p.feedMode = 0;
			p.feedTime = gs.getTime();
		}
		commonFlowRecord.setEvent(TLog.AT_PET_PRODUCE);
		commonFlowRecord.setArg(pid);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return product;
	}
	
	public byte petsChangeName(final short pid, final String newName, List<String> modName) 
	{
		if (newName == null)
			return GameData.CHANGENAME_INVALID;
		
		String rname = gs.getLoginManager().checkUserInput(newName, gs.getGameData().getUserInputCFG().roleName, true, true);
		if (rname == null) {
			return GameData.CHANGENAME_INVALID;
		}
		
		if( gs.getGameData().isBadName(rname) ) {
			return GameData.CHANGENAME_INVALID;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			Pet p = pets.get(pid);
			if( p == null )
				return GameData.CHANGENAME_INVALID;
			
			int cost = gs.getGameData().getRoleCmnCFG().changePetNameCost;
			if (getStoneWithoutLock() < cost) {
				return GameData.CHANGENAME_NOMONEY;
			}
			commonFlowRecord.addChange(useStone(cost));
			p.name = rname;
			lastSaveTime = -1;
			modName.add(rname);
			
			commonFlowRecord.setEvent(TLog.AT_PET_CHANGE_NAME);
			commonFlowRecord.setArg(0, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
			return GameData.CHANGENAME_OK;
		}
	}
	
	public synchronized List<SBean.FriendBreedPet> petsListBreed(short pid)
	{
		Pet p = pets.get(pid);
		SBean.DBPetDeform deform = petDeforms.get(pid);
		if( p == null || deform == null )
			return null;
		SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(pid, isPetDeformed(deform.deformStage));
		if( cfg == null || cfg.gender != 1 )
			return null;
		return friend.listBreed(cfg.type);
	}
	
	public short petsBreed(short pid, String openID, SBean.GlobalRoleID grid)
	{
		short eggItemID = -1;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		int breedReward = 0;
		synchronized (this)
		{
			Pet p = pets.get(pid);
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if( p == null || deform == null )
				return eggItemID;
			if( p.lvl < GameData.getInstance().getPetCmnCFG().breedReqLvl )
				return -1;
			SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(pid, isPetDeformed(deform.deformStage));
			if( cfg == null || cfg.gender != 1 )
				return eggItemID;
			if( money < cfg.breedMoney )
				return eggItemID;
			int now = gs.getTime();
			if( p.lastBreedTime + cfg.breedCool > now )
				return eggItemID;
			if( ! friend.breed(cfg.type, openID, grid) )
				return eggItemID;
			eggItemID = GameData.getInstance().getRandom().nextFloat() < cfg.proEgg2 ? cfg.eggID2 : cfg.eggID1;
			if( GameData.getInstance().getItemCFG(eggItemID) == null )
				return -1;
			// TODO log
			commonFlowRecord.addChange(useMoney(cfg.breedMoney));
			p.lastBreedTime = now;
			commonFlowRecord.addChange(addItem(eggItemID, (short)1));
			breedReward = (int)(cfg.breedMoney * GameData.getInstance().getFriendCFG().breedRewardProp);
		}
		commonFlowRecord.setEvent(TLog.AT_PET_BREED);
		commonFlowRecord.setArg(pid, eggItemID);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		//
		gs.getExchangeManager().sendMessageToFriend(id, grid, DBFriend.MSG_TYPE_PET_BREED, pid, breedReward);
		logSNS(TLog.SNS_SENDMONEY, 1);
		//
		return eggItemID;
	}
	
	public boolean taskFinish(short gid, short seq)
	{
		SBean.TaskGroupCFGS group = GameData.getInstance().getTaskGroupCFG(gid);
		if( group == null )
			return false;
		if( seq < 1 || seq > group.tasks.size() )
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		int beforeTaskFinishLvl = this.lvl;
		synchronized( this )
		{
			short posNow = 0;
			SBean.DBTaskPos tpos = null;
			for(SBean.DBTaskPos e : taskPos)
			{
				if( e.groupID == gid )
				{
					tpos = e;
					posNow = e.seqComp;
					break;
				}
			}
			if( seq != posNow + 1 )
				return false;
			SBean.TaskCFGS tcfg = group.tasks.get(seq-1);
			if( tcfg.lvlReq > lvl )
				return false;
			boolean bFinish = false;
			switch( tcfg.type )
			{
			case GameData.TASK_TYPE_FINISH_COMBAT:
				{
					SBean.DBCombatPos curPos = combatPos[tcfg.arg1];
					if( curPos.catIndex + 1 > tcfg.arg2 )
						bFinish = true;
					else if( curPos.catIndex + 1 == tcfg.arg2 )
					{
						if( curPos.combatIndex + 1 > tcfg.arg3 )
							bFinish = true;
					}
				}
				break;
			case GameData.TASK_TYPE_GENERAL_COUNT:
				{
					if( generals.size() >= tcfg.arg1 )
						bFinish = true;
				}
				break;
			case GameData.TASK_TYPE_GENERAL_ANY_ADV:
				{
					int n = 0;
					for(General g : generals.values())
					{
						if( g.advLvl >= tcfg.arg2 && g.evoLvl >= tcfg.arg3 )
							++n;
					}
					if( n >= tcfg.arg1 )
						bFinish = true;
				}
				break;
			case GameData.TASK_TYPE_GENERAL_ADV:
				{
					for(General g : generals.values())
					{
						if( g.id == tcfg.arg1 && g.advLvl >= tcfg.arg2 && g.evoLvl >= tcfg.arg3 )
						{
							bFinish = true;
							break;
						}
					}
				}
				break;
			case GameData.TASK_TYPE_LEVEL:
				{
					if( lvl >= tcfg.arg1 )
						bFinish = true;
				}
				break;
			default:
				break;
			}
			if( ! bFinish )
				return false;

			if( tpos != null )
				tpos.seqComp = seq;
			else
				taskPos.add(new SBean.DBTaskPos(gid, seq));
			commonFlowRecord.addChange(addMoney(tcfg.rewardMoney));
			commonFlowRecord.addChange(addStonePresent(tcfg.rewardStone));
			commonFlowRecord.addChanges(addExp(tcfg.rewardExp, tlogEvent));
			commonFlowRecord.addChanges(addDrops(tcfg.rewards));
		}
		commonFlowRecord.setEvent(TLog.AT_FINISH_TASK);
		commonFlowRecord.setArg(gid, seq);
		
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(new TLogger.TaskRecord(beforeTaskFinishLvl, gid, seq));
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized ShopInfo shopNormalSync()
	{
		if( shopNormal.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopNormal.tryRefresh();
		return shopNormal.getInfo();
	}
	
	public synchronized ShopInfo shopBlessSync()
	{
		if( shopBless.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopBless.tryRefresh();
		return shopBless.getInfo();
	}
	
	public boolean shopRandTest(short cid, byte shopid, int times)
	{
		Shop shop = shopid == 1 ? shopRand1 : shopRand2;
		boolean bOK = false;
		int now = gs.getTime();
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return false;
			if( shop.shop.flag1 == 1 )
				return false;
			if( shop.shop.timeout >= now )
				return false;
			for(int i = 0; i < times; ++i)
			{
				if( shop.cfg.randSummonCID == cid || GameData.getInstance().getRandom().nextFloat() < shop.cfg.randSummonPro )
				{
					bOK = true;
					break;
				}
			}
			if( ! bOK )
			{
				if( activity >= shop.cfg.randSummonVit && shop.shop.flag2 == 0 )
				{
					bOK = true;
				}
			}
			if( bOK )
			{
				shop.shop.flag2 = 1;
				shop.shop.timeout = now + shop.cfg.randSummonTime;
				shop.doRefresh();
			}
		}
		return bOK;
	}
	
	public synchronized ShopInfo shopRandSync(byte shopid)
	{
		Shop shop = shopid == 1 ? shopRand1 : shopRand2;
		if( shop.cfg.lvlReq > lvl )
			return null;
		if( shop.shop.flag1 == 0 && shop.shop.timeout < gs.getTime() )
			return null;
		dayRefresh();
		if( shop.shop.flag1 == 1 )
			shop.tryRefresh();
		return shop.getInfo();
	}
	
	public boolean shopRandBuy(byte shopid, byte seq, byte type, short id)
	{
		Shop shop = shopid == 1 ? shopRand1 : shopRand2;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return ret;
			if( shop.shop.flag1 == 0 && shop.shop.timeout < gs.getTime() )
				return ret;
			dayRefresh();
			shop.tryRefresh();
			ret = shop.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopRandRefresh(byte shopid)
	{
		Shop shop = shopid == 1 ? shopRand1 : shopRand2;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return ret;
			if( shop.shop.flag1 == 0 && shop.shop.timeout < gs.getTime() )
				return ret;
			dayRefresh();
			// TODO
			ret = shop.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopRandSummon(byte shopid)
	{
		Shop shop = shopid == 1 ? shopRand1 : shopRand2;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return ret;
			if( shop.cfg.lvlVIPReq > lvlVIP )
				return ret;
			// TODO
			ret = shop.userSummon(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized ShopInfo shopArenaSync()
	{
		if( shopArena.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopArena.tryRefresh();
		return shopArena.getInfo();
	}
	
	public synchronized ShopInfo shopSuperArenaSync()
	{
		if( shopSuperArena.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopSuperArena.tryRefresh();
		return shopSuperArena.getInfo();
	}
	
	public synchronized ShopInfo shopDuelSync()
	{
		if( shopDuel.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopDuel.tryRefresh();
		return shopDuel.getInfo();
	}
	
	public synchronized ShopInfo shopMarchSync()
	{
		if( shopMarch.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopMarch.tryRefresh();
		return shopMarch.getInfo();
	}
	
	public synchronized ShopInfo shopForceSync()
	{
		if( shopForce.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shopForce.tryRefresh();
		return shopForce.getInfo();
	}
	
	public synchronized ShopInfo shopRichSync(int index)
	{
		Shop shop = shopRich[index]; 
		if( shop.cfg.lvlReq > lvl )
			return null;
		dayRefresh();
		shop.tryRefresh();
		return shop.getInfo();
	}
	
	public synchronized List<ShopRandBrief> shopRandGetBrief()
	{
		List<ShopRandBrief> lst = new ArrayList<ShopRandBrief>();
		lst.add(shopRand1.getRandBrief());
		lst.add(shopRand2.getRandBrief());
		return lst;
	}
	
	private int getMaxBuyMoneyTimes()
	{
		return GameData.getInstance().getBuyMoneyMaxTimes(lvlVIP);
	}
	
	private int getMaxBuyVitTimes()
	{
		return GameData.getInstance().getBuyVitMaxTimes(lvlVIP);
	}
	
	private int getMaxBuySkillPointTimes()
	{
		return GameData.getInstance().getBuySkillPointMaxTimes(lvlVIP);
	}
	
	public List<Integer> roleBuyMoney(boolean buyOneTime)
	{
		List<Integer> ret = new ArrayList<Integer>();
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.setEvent(TLog.AT_BUY_MONEY);
		synchronized( this )
		{
			SBean.RoleCmnCFGS cfgs = GameData.getInstance().getRoleCmnCFG();
			dayRefresh();
			int canBuyMaxTimes = buyOneTime ? 1 : 10;
			int buyTimes = 0;
			int oneTimeStoneNeed = 0;
			while (buyTimes < canBuyMaxTimes && buyMoneyTimes+buyTimes < getMaxBuyMoneyTimes())
			{
				int index = buyMoneyTimes + buyTimes;
				if( index >= cfgs.buyMoneyPrice.size() )
					index = cfgs.buyMoneyPrice.size() - 1;
				int stoneNeed = cfgs.buyMoneyPrice.get(index).intValue();
				if (buyTimes == 0)
				{
					oneTimeStoneNeed = stoneNeed;
				}
				else
				{
					if (oneTimeStoneNeed != stoneNeed)
						break;
				}
				if (stone < (buyTimes+1)*oneTimeStoneNeed)
					break;
				++buyTimes;
			}
			if (buyTimes <= 0)
				return ret;
			int stoneNeedAll = buyTimes*oneTimeStoneNeed;
			if (stone < stoneNeedAll)
				return ret;
			commonFlowRecord.setArg(buyMoneyTimes, buyTimes);
			for (int i = 0; i < buyTimes; ++i)
			{
				int buyMoney = cfgs.buyMoneyBaseVal + cfgs.buyMoneylvlVal*this.lvl + cfgs.buyMoneyTimesVal*buyMoneyTimes;
				float crate = GameData.getInstance().getRandom().nextFloat();
				int critMultiplier = 1;
				//锟阶达拷使锟斤拷摇钱锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷X2
				if (boughtMoney == 0)
				{
					critMultiplier = 2;
					boughtMoney = 1;
				}
				else
				{
					for (SBean.CritCFGS c : cfgs.buyMoneyCrit)
					{
						if (crate < c.CritRate)
						{
							critMultiplier = c.CritMultiplier;
							break;
						}
					}
					gs.getLogger().debug("buyMoney crate=" + crate + ", critMultiplier=" + critMultiplier);
					//锟斤拷锟斤拷摇钱锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�锟侥憋拷锟斤拷时锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷2
					if ((buyMoneyTimes+1)%5 == 0 && critMultiplier == 1)
						critMultiplier = 2;	
				}
				commonFlowRecord.addChange(useStone(oneTimeStoneNeed));
				commonFlowRecord.addChange(addMoney(buyMoney*critMultiplier));
				++buyMoneyTimes;
				ret.add(critMultiplier);
			}
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_BUY_MONEY, 1));
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean roleBuyVit()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			SBean.RoleCmnCFGS cfgs = GameData.getInstance().getRoleCmnCFG();
			dayRefresh();
			if( buyVitTimes >= getMaxBuyVitTimes() )
				return false;
			int index = buyVitTimes;
			if( index >= cfgs.buyVitPrice.size() )
				index = cfgs.buyVitPrice.size() - 1;
			int stoneNeed = cfgs.buyVitPrice.get(index).intValue();
			if( stone < stoneNeed )
				return false;
			if( cfgs.buyVitVal + vit > cfgs.vitMax )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			commonFlowRecord.addChange(addVit2(cfgs.buyVitVal));
			++buyVitTimes;
		}
		commonFlowRecord.setEvent(TLog.AT_BUY_VIT);
		commonFlowRecord.setArg(buyVitTimes, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean roleBuySkillPoint()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			SBean.RoleCmnCFGS cfgs = GameData.getInstance().getRoleCmnCFG();
			dayRefresh();
			if( buySkillPointTimes >= getMaxBuySkillPointTimes() )
				return false;
			int index = buySkillPointTimes;
			if( index >= cfgs.buySkillPointPrice.size() )
				index = cfgs.buySkillPointPrice.size() - 1;
			int stoneNeed = cfgs.buySkillPointPrice.get(index).intValue();
			if( stone < stoneNeed )
				return false;
			if( skillPoint != 0 )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			commonFlowRecord.addChange(addSkillPoint(getSkillPointMax()));
			++buySkillPointTimes;
		}
		commonFlowRecord.setEvent(TLog.AT_BUY_SKILLPOINT);
		commonFlowRecord.setArg(buySkillPointTimes, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean roleSetHeadIcon(short headIconID)
	{
		this.headIconID = headIconID;
		return true;
	}
	
	public boolean friendMailBaozi(int serverID, int roleID)
	{
		boolean bOK = false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		synchronized( this )
		{
			dayRefresh();
			bOK = friend.mailBaozi(serverID, roleID);
			if( bOK )
			{
				// TODO
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_MAIL_BAOZI, 1));
			}
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		if( bOK )
		{
			logSNS(TLog.SNS_SENDBUNS, 1);
			gs.getExchangeManager().sendMessageToFriend(id, new SBean.GlobalRoleID(serverID, roleID), DBFriend.MSG_TYPE_BAOZI, (short)0, 0);
		}
		return bOK;
	}
	
	public void qqShareReport()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		synchronized (this)
		{
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_SHARE_PIC, 1));
		}
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public boolean friendMailAllBaozi()
	{
		List<SBean.GlobalRoleID> lst = null;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		synchronized( this )
		{
			dayRefresh();
			lst = friend.mailAllBaozi();
			if( lst != null )
			{
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_MAIL_BAOZI, lst.size()));
			}
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		if( lst == null )
			return false;
		for(SBean.GlobalRoleID e : lst)
		{
			gs.getExchangeManager().sendMessageToFriend(id, e, DBFriend.MSG_TYPE_BAOZI, (short)0, 0);
		}
		logSNS(TLog.SNS_SENDBUNS, lst.size());
		return true;
	}
	
	public String getOpenID()
	{
		return openID;
	}
	
	public boolean friendMailPetExp(int serverID, int roleID)
	{
		boolean bOK = false;
		synchronized( this )
		{
			dayRefresh();
			bOK = friend.mailPetExp(serverID, roleID);
		}
		if( bOK )
		{
			SBean.PetCmnCFGS cfg = GameData.getInstance().getPetCmnCFG();
			short itemID = 0;
			int itemCnt = 0;
			for(SBean.PetFriendExpItemCFGS e : cfg.friendExpItems)
			{
				if( e.lvlCeil >= lvl )
				{
					itemID = e.itemID;
					itemCnt = e.itemCnt;
					break;
				}
			}
			logSNS(TLog.SNS_SENDEXPITEMS, 1);
			gs.getExchangeManager().sendMessageToFriend(id, new SBean.GlobalRoleID(serverID, roleID), DBFriend.MSG_TYPE_PET_EXP, itemID, itemCnt);
		}
		return bOK;
	}
	
	public boolean friendBatchMailPetExp(List<SBean.GlobalRoleID> ids)
	{
		boolean ok = true;
		for (SBean.GlobalRoleID id : ids)
			if (!friendMailPetExp(id.serverID, id.roleID))
				ok = false;
		return ok;
	}
	
	public int friendRecall(int serverID, int roleID)
	{
		int ret = -1;
		int nRecall = 0;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int oldRecallCount = friend.friend.recallCount;
			dayRefresh();
			ret = friend.recall(serverID, roleID, commonFlowRecord);
			nRecall = friend.friend.recallCount - oldRecallCount;
		}
		commonFlowRecord.setEvent(TLog.AT_RECALL);
		commonFlowRecord.setArg(1, serverID, roleID);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		logSNS(TLog.SNS_RECALL, nRecall);
		return ret;
	}
	
	public int friendRecallAll(List<String> openIDs)
	{
		int ret = -1;
		int nRecall = 0;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int oldRecallCount = friend.friend.recallCount;
			dayRefresh();
			ret = friend.recallAll(openIDs, commonFlowRecord);
			nRecall = friend.friend.recallCount - oldRecallCount;
		}
		commonFlowRecord.setEvent(TLog.AT_RECALL);
		commonFlowRecord.setArg(openIDs.size(), 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		logSNS(TLog.SNS_RECALL, nRecall);
		return ret;
	}
	
	public int friendEatBaozi(int serverID, int roleID, int time)
	{
		int ret = 0;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			ret = friend.eatBaozi(serverID, roleID, time, commonFlowRecord);
		}
		commonFlowRecord.setEvent(TLog.AT_EAT_BUNS);
		commonFlowRecord.setArg(time, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		if( ret > 0 )
		{
			logSNS(TLog.SNS_RECEICEBUNS, 1);
		}
		return ret;
	}
	
	public int friendTakeBreedReward(int serverID, int roleID, int time)
	{
		int ret = 0;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			ret = friend.takePetBreedReward(serverID, roleID, time, commonFlowRecord);
		}
		commonFlowRecord.setEvent(TLog.AT_TAKE_FRIEND_BREED_REWARD);
		commonFlowRecord.setArg(time, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		if( ret > 0 )
		{
			logSNS(TLog.SNS_RECEICEMONEY, 1);
		}
		return ret;
	}
	
	public short friendSyncHeart()
	{
		short ret = 0;
		synchronized( this )
		{
			dayRefresh();
			ret = friend.syncHeart();
		}
		return ret;
	}
	
	public int friendTakePetExp(int serverID, int roleID, int time)
	{
		int ret = -1;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			ret = friend.takePetExp(serverID, roleID, time, commonFlowRecord);
		}
		commonFlowRecord.setEvent(TLog.AT_TAKE_FRIEND_ITEM);
		commonFlowRecord.setArg(time, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		if( ret > 0 )
		{
			logSNS(TLog.SNS_RECEICEEXPITEMS, 1);
		}
		return ret;
	}
	
	public boolean friendRefuse(final int applyRoleID)
	{
		synchronized( this )
		{
			friend.friend.deleteApply(applyRoleID);
		}
		return true;
	}
	
	public void friendSyncUpdate(final int serverID, final int roleID)
	{
		List<SBean.OpenIDRecord> recordsLocal = new ArrayList<SBean.OpenIDRecord>();
		List<SBean.OpenIDRecord> recordsRemote = new ArrayList<SBean.OpenIDRecord>();
		String openID = null;
		synchronized( this )
		{
			openID = friend.friend.checkOpenID(serverID, roleID);
		}
		if( openID == null )
			return;
		if( serverID == gs.getConfig().id )
			recordsLocal.add(new SBean.OpenIDRecord(openID, new SBean.GlobalRoleID(serverID, roleID)));
		else
			recordsRemote.add(new SBean.OpenIDRecord(openID, new SBean.GlobalRoleID(serverID, roleID)));
		gs.getExchangeManager().updateFriendProp(id, recordsLocal, recordsRemote);
	}
	
	public boolean friendDel(final int friendServerID, final int friendRoleID)
	{
		int ret = 0;
		synchronized( this )
		{
			ret = friend.friend.delFriend(friendServerID, friendRoleID);
		}
		if( ret == 1 ) // 删锟斤拷苑锟� 1:local 2:global
		{
			gs.getLoginManager().execCommonFriendVisitor(friendRoleID, new LoginManager.CommonFriendVisitor() {
				
				boolean bDel = false;
				@Override
				public byte visit(DBFriend dbfriend)
				{
					bDel = dbfriend.delLocalFriend(gs.getConfig().id, Role.this.id);
					return bDel ? LoginManager.CommonFriendVisitor.ERR_DB_OK_SAVE : LoginManager.CommonFriendVisitor.ERR_DB_OK_NOSAVE;
				}
				
				@Override
				public boolean visit(Role role)
				{
					synchronized( this )
					{
						bDel = role.friend.friend.delLocalFriend(gs.getConfig().id, Role.this.id);
					}
					if( bDel )
					{
						gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeFriendSyncDel(gs.getConfig().id, Role.this.id));
					}
					return true;
				}
				
				@Override
				public void onCallback(boolean bDB, byte errcode)
				{
					
				}
			});
		}
		return ret > 0;
	}
	
	public synchronized int friendUndelAll()
	{
		int count = friend.friend.friendsDel.size();
		friend.friend.friendsDel.clear();
		friend.friend.friendsSearching.clear();
		return count;
	}
	
	private int friendAccept2(SBean.DBFriendData friendData)
	{
		if( friendData == null )
			return DBFriend.ERR_ACCEPT_FAILED;
		synchronized( this )
		{
			friend.friend.deleteApply(friendData.roleID.roleID);
			if( friend.isFriend(friendData.roleID.serverID, friendData.roleID.roleID) )
			{
				return DBFriend.ERR_ACCEPT_OK;
			}
			friend.friend.friends.add(friendData.kdClone());
		}
		{
			List<SBean.DBFriendData> newList = new ArrayList<SBean.DBFriendData>();
			newList.add(friendData);
			// 通知 role
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncUpdate(newList));
		}
		return DBFriend.ERR_ACCEPT_OK;
	}
	
	private int friendApply2(SBean.DBFriendData friendData)
	{
		if( friendData == null )
			return DBFriend.ERR_APPLY_FAILED;
		synchronized( this )
		{
			if( friend.isFriend(friendData.roleID.serverID, friendData.roleID.roleID) )
			{
				return DBFriend.ERR_APPLY_OK;
			}
			friend.friend.friends.add(friendData.kdClone());
		}
		{
			List<SBean.DBFriendData> newList = new ArrayList<SBean.DBFriendData>();
			newList.add(friendData);
			// 通知 role
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncUpdate(newList));
		}
		return DBFriend.ERR_APPLY_OK;
	}
	
	public void friendAccept(final int applyRoleID)
	{
		if( applyRoleID <= 0 || applyRoleID == id )
			return;
		int ret = DBFriend.ERR_ACCEPT_FAILED;
		synchronized( this )
		{
			if( friend.isFriend(gs.getConfig().id, applyRoleID) )
			{
				ret = DBFriend.ERR_ACCEPT_EXIST;
				friend.friend.deleteApply(applyRoleID);
			}
			else if( friend.getFriendCount() >= GameData.getInstance().getFriendCFG().cap )
			{
				ret = DBFriend.ERR_ACCEPT_SELF_FULL;
			}
		}
		if( ret != DBFriend.ERR_ACCEPT_FAILED )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret));
			return;
		}
		gs.getLoginManager().execCommonFriendVisitor(applyRoleID, new LoginManager.CommonFriendVisitor() {
			
			int ret2 = DBFriend.ERR_ACCEPT_FAILED;
			
			@Override
			public byte visit(DBFriend dbfriend)
			{
				SBean.DBFriendData friendData = new SBean.DBFriendData(Role.this.getOpenID(),
						new SBean.GlobalRoleID(gs.getConfig().id, Role.this.id)
						, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, Role.this.getFriendPropWithLock());
				ret2 = dbfriend.applyAccepted(gs.getConfig().id, Role.this.id, friendData, gs.getTime());
				switch( ret2 )
				{
				case DBFriend.ERR_ACCEPT_OK:
					return LoginManager.CommonFriendVisitor.ERR_DB_OK_SAVE;
				case DBFriend.ERR_ACCEPT_FAILED:
				case DBFriend.ERR_ACCEPT_TARGET_FULL:
				case DBFriend.ERR_ACCEPT_TARGET_EXIST:
					return LoginManager.CommonFriendVisitor.ERR_DB_OK_NOSAVE;				
				default:
					break;
				}
				return 0;
			}
				
			@Override
			public boolean visit(Role role)
			{
				SBean.DBFriendData friendData = new SBean.DBFriendData(Role.this.getOpenID(),
						new SBean.GlobalRoleID(gs.getConfig().id, Role.this.id)
						, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, Role.this.getFriendPropWithLock());
				synchronized( role )
				{
					ret2 = role.friend.friend.applyAccepted(gs.getConfig().id, Role.this.id, friendData, gs.getTime());
				}
				if( ret2 == DBFriend.ERR_ACCEPT_OK )
				{
					List<SBean.DBFriendData> newList = new ArrayList<SBean.DBFriendData>();
					newList.add(friendData);
					// 通知 role
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeFriendSyncUpdate(newList));
				}
				return true;
			}
				
			@Override
			public void onCallback(boolean bDB, byte errcode)
			{
				if( ret2 == DBFriend.ERR_ACCEPT_TARGET_EXIST )
				{
					ret2 = DBFriend.ERR_ACCEPT_OK;
				}
				
				switch( ret2 )
				{	
				// 锟斤拷佣苑锟绞э拷锟�
				case DBFriend.ERR_ACCEPT_FAILED:
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
					break;
					
				// 锟皆硷拷锟斤拷啵拷锟斤拷失锟斤拷
				case DBFriend.ERR_ACCEPT_SELF_FULL:
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
					break;
					
				// 锟皆硷拷锟窖撅拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
				case DBFriend.ERR_ACCEPT_EXIST:
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
					break;
					
				// 锟皆凤拷锟斤拷锟窖癸拷锟�
				case DBFriend.ERR_ACCEPT_TARGET_FULL:
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
					break;					
					
				// 锟斤拷佣苑锟斤拷晒锟�
				case DBFriend.ERR_ACCEPT_OK:
					{
						gs.getLoginManager().execCommonRoleVisitor(applyRoleID, new LoginManager.CommonRoleVisitor()
						{

							SBean.DBFriendData friendDataApply = null;
							
							@Override
							public boolean visit(Role role)
							{
								synchronized( role )
								{
									friendDataApply = new SBean.DBFriendData(role.getOpenID(),
											new SBean.GlobalRoleID(gs.getConfig().id, role.id)
											, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, role.getFriendPropWithoutLock());
								}
								return true;
							}

							@Override
							public byte visit(DBRole dbrole)
							{
								friendDataApply = new SBean.DBFriendData(dbrole.openID,
										new SBean.GlobalRoleID(gs.getConfig().id, dbrole.id)
										, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, dbrole.getFriendProp());
								return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
							}

							@Override
							public void onCallback(boolean bDB, byte errcode, String rname)
							{
								ret2 = Role.this.friendAccept2(friendDataApply);
								switch( ret2 )
								{
								case DBFriend.ERR_ACCEPT_OK:
									gs.getRPCManager().sendLuaPacket(Role.this.netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
									break;
								case DBFriend.ERR_ACCEPT_FAILED:
									gs.getRPCManager().sendLuaPacket(Role.this.netsid, LuaPacket.encodeFriendAcceptRes(applyRoleID, ret2));
									break;
								default:
									break;
								}
							}
							
						});
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	public void friendApply(final int targetRoleID)
	{
		if( targetRoleID <= 0 || targetRoleID == id )
			return;
		int ret = DBFriend.ERR_APPLY_FAILED;
		synchronized( this )
		{			
			if( friend.isFriend(gs.getConfig().id, targetRoleID) )
			{
				ret = DBFriend.ERR_APPLY_EXIST;
			}
			else if( friend.getFriendCount() >= GameData.getInstance().getFriendCFG().cap )
			{
				ret = DBFriend.ERR_APPLY_SELF_FULL;
			}
		}
		if( ret != DBFriend.ERR_APPLY_FAILED )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendApplyRes(targetRoleID, ret));
			return;
		}
		gs.getLoginManager().execCommonFriendVisitor(targetRoleID, new LoginManager.CommonFriendVisitor() {
			
			int ret2 = DBFriend.ERR_APPLY_FAILED;
			
			@Override
			public byte visit(DBFriend dbfriend)
			{
				ret2 = dbfriend.addApply(gs.getConfig().id, Role.this.id, Role.this.headIconID, Role.this.name
						, Role.this.forceInfo.name, Role.this.lvl, gs.getTime());
				switch( ret2 )
				{
				case DBFriend.ERR_APPLY_OK:
					return LoginManager.CommonFriendVisitor.ERR_DB_OK_SAVE;
				case DBFriend.ERR_APPLY_FAILED:
				case DBFriend.ERR_APPLY_TARGET_FULL:
				case DBFriend.ERR_APPLY_TARGET_APPLY_FULL:
				case DBFriend.ERR_APPLY_TARGET_EXIST:
				case DBFriend.ERR_APPLY_TARGET_APPLY_EXIST:
					return LoginManager.CommonFriendVisitor.ERR_DB_OK_NOSAVE;				
				default:
					break;
				}
				return 0;
			}
				
			@Override
			public boolean visit(Role role)
			{
				synchronized( role )
				{
					ret2 = role.friend.friend.addApply(gs.getConfig().id, Role.this.id, Role.this.headIconID, Role.this.name
							, Role.this.forceInfo.name, Role.this.lvl, gs.getTime());	
				}
				return true;
			}
				
			@Override
			public void onCallback(boolean bDB, byte errcode)
			{
				boolean bTargetExist = false;
				if( ret2 == DBFriend.ERR_APPLY_TARGET_EXIST )
				{					
					bTargetExist = true;
					ret2 = DBFriend.ERR_APPLY_OK;
				}
				gs.getRPCManager().sendLuaPacket(Role.this.netsid, LuaPacket.encodeFriendApplyRes(targetRoleID, ret2));
				if( bTargetExist )
				{
					// TODO !!!!
					// 直锟斤拷锟斤拷锟竭憋拷锟斤拷锟斤拷锟斤拷锟斤拷
					gs.getLoginManager().execCommonRoleVisitor(targetRoleID, new LoginManager.CommonRoleVisitor()
					{

						SBean.DBFriendData friendDataTarget = null;
						
						@Override
						public boolean visit(Role role)
						{
							synchronized( role )
							{
								friendDataTarget = new SBean.DBFriendData(role.getOpenID(),
										new SBean.GlobalRoleID(gs.getConfig().id, role.id)
										, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, role.getFriendPropWithoutLock());
							}
							return true;
						}

						@Override
						public byte visit(DBRole dbrole)
						{
							friendDataTarget = new SBean.DBFriendData(dbrole.openID,
									new SBean.GlobalRoleID(gs.getConfig().id, dbrole.id)
									, DBFriend.FLAG_LOCAL, gs.getTime(), 0, 0, dbrole.getFriendProp());
							return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
						}

						@Override
						public void onCallback(boolean bDB, byte errcode, String rname)
						{
							ret2 = Role.this.friendApply2(friendDataTarget);
						}
						
					});
				}
			}
		});
	}
	
	public void setCreateRoleExchangeOK()
	{
		setFlag(ROLE_FLAG_CREATE_EXCHANGE);
	}
	
	public boolean friendTakeAll(List<SBean.DropEntry> itemLst, int[] rets)
	{		
		int[] snsCount = new int[3];
		boolean bOK = false;
		rets[0] = 0;
		rets[1] = 0;
		itemLst.clear();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			bOK = friend.takeAll(commonFlowRecord, itemLst, rets, snsCount);
		}
		commonFlowRecord.setEvent(TLog.AT_EAT_BUNS);
		commonFlowRecord.setArg(-1, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		if( bOK )
		{
			logSNS(TLog.SNS_RECEICEBUNS, snsCount[0]);
			logSNS(TLog.SNS_RECEICEEXPITEMS, snsCount[1]);
			logSNS(TLog.SNS_RECEICEMONEY, snsCount[2]);
		}
		return bOK;
	}
	
	public boolean shopNormalBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopNormal.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopNormal.tryRefresh();
			ret = shopNormal.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopBlessBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopBless.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopBless.tryRefresh();
			ret = shopBless.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopArenaBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopArena.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopArena.tryRefresh();
			ret = shopArena.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopSuperArenaBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopSuperArena.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopSuperArena.tryRefresh();
			ret = shopSuperArena.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopDuelBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopDuel.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopDuel.tryRefresh();
			ret = shopDuel.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopMarchBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopMarch.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopMarch.tryRefresh();
			ret = shopMarch.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopForceBuy(byte seq, byte type, short id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shopForce.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shopForce.tryRefresh();
			ret = shopForce.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopRichBuy(int index, byte seq, byte type, short id)
	{
		Shop shop = shopRich[index];
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		boolean ret = false;
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			shop.tryRefresh();
			ret = shop.buy(seq, type, id, tlogEvent);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopNormalRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopNormal.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopNormal.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public boolean shopBlessRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopBless.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopBless.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopArenaRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopArena.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopArena.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopSuperArenaRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopSuperArena.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopSuperArena.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopDuelRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopDuel.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			ret = shopDuel.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopMarchRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopMarch.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopMarch.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopForceRefresh()
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shopForce.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shopForce.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public synchronized boolean shopRichRefresh(int index)
	{
		Shop shop = shopRich[index];
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		boolean ret = false;
		synchronized( this )
		{
			if( shop.cfg.lvlReq > lvl )
				return ret;
			dayRefresh();
			// TODO
			ret = shop.userRefresh(commonFlowRecord);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return ret;
	}
	
	public int getStonePayTotal()
	{
		return stonePayMidas + stonePayAlpha;
	}
	
	public boolean generalsFirstEvo(short gid)
	{
		SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
		if( gcfg == null )
			return false;
		SBean.GeneralCmnCFGS cmncfg = gs.getGameData().getGeneralCmnCFG();
		short iid = gcfg.stoneID;
		byte initEvoLevel = gcfg.initEvoLevel;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g != null )
				return false;
			int moneyNeed = 0;
			short pieceCountNeed = 0;
			for(byte i = 0; i < initEvoLevel; ++i)
			{			
				moneyNeed += cmncfg.evoLevels.get(i).money;
				pieceCountNeed += cmncfg.evoLevels.get(i).stone;
			}				
			if( money < moneyNeed )
				return false;
			if( getItemCount(iid) < pieceCountNeed )
				return false;
			
			commonFlowRecord.addChange(delItem(iid, pieceCountNeed));
			commonFlowRecord.addChange(useMoney(moneyNeed));
			
			//g = new General(gid, initEvoLevel);
			//generals.put(gid, g);
			commonFlowRecord.addChange(addGeneral(gid, initEvoLevel));
		}
		commonFlowRecord.setEvent(TLog.AT_GENERAL_FIRST_EVOLUTION);
		commonFlowRecord.setArg(gid, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	// for debug mode
	private void generalsSetSkillFullLevel()
	{
		SBean.GeneralCmnCFGS cmncfg = gs.getGameData().getGeneralCmnCFG();
		for(General g : generals.values())
		{
			for(int pos = 1; pos <= GameData.GENERAL_SKILL_COUNT; ++pos)
			{
				SBean.GeneralSkillCFGS scfg = cmncfg.skills.get(pos-1);
				if( g.advLvl < scfg.reqAdvLvl )
					continue;
				short lvlMax = (short)(g.lvl - scfg.maxLvl);
				if( lvlMax < 1 )
					lvlMax = 1;
				g.skills.set(pos-1, lvlMax);
			}
		}
	}
	
	// for debug mode
	private void generalsSetGildLevel(byte gildLevel)
	{
		for(General g : generals.values())
		{
			SBean.GeneralCFGS gcfg = GameData.getInstance().getGeneralCFG(g.id);
			for(int pos = 1; pos <= GameData.GENERAL_EQUIP_COUNT; ++pos)
			{
				SBean.DBGeneralEquip e = g.equips.get(pos-1);
				if( e.lvl < 0 && gildLevel >= 0 )
					e.lvl = 0;
				if( e.lvl >= 0 )
				{
					if( gildLevel == 0 )
					{
						e.lvl = 0;
						e.exp = 0;
					}
					else
					{
						e.exp = 0;
						//
						int index = (g.advLvl-1) * GameData.GENERAL_EQUIP_COUNT + (pos-1);
						short eid = gcfg.equips.get(index);
						SBean.EquipCFGS ecfg = GameData.getInstance().getEquipCFG(eid);
						SBean.EquipRankCFGS rcfg = GameData.getInstance().getEquipCmnCFG().rank.get(ecfg.rank);
						byte lvlMax = (byte)rcfg.gild.size();
						e.lvl = lvlMax;
					}
				}
			}
		}
	}
	
	// for debug mode
	private void generalsSetWeaponEvoLevel(byte wevo, byte wlvl)
	{
		for(General g : generals.values())
		{
			//SBean.GeneralCFGS gcfg = GameData.getInstance().getGeneralCFG(g.id);
			SBean.DBWeapon w = g.weapon;
			SBean.WeaponEnhanceCFGS wcfg = gs.getGameData().getWeaponEnhanceCFG(g.id);
			byte maxEvo = (byte)wcfg.enhanceStages.size();
			if( wevo < 1 )
				wevo = 1;
			if( wevo > maxEvo )
				wevo = maxEvo;
			w.evo = (byte)(wevo-1);
			byte maxLvl = wcfg.enhanceStages.get(w.evo).enhanceCount;
			if( wlvl < 0 )
				wlvl = 0;
			if( wlvl > maxLvl )
				wlvl = maxLvl;
			//TODO
			w.lvl = wlvl;
			for (int i = 0; i < w.evo; i ++)
				w.lvl += wcfg.enhanceStages.get(i).enhanceCount;
		}
	}
	
	// for debug mode
	private void generalsSetWeaponResetLevel(byte wreset)
	{
		SBean.WeaponResetCFGS wcfg = gs.getGameData().getWeaponResetCFG();
		for(General g : generals.values())
		{
			SBean.DBWeapon w = g.weapon;
			w.passes.clear();
			w.resetPasses.clear();
			if (g.weapon.evo < 3) // purple
				continue;
			int count = 0;
			if (g.weapon.evo == 3) {
				count = 4;
			}
			else if (g.weapon.evo == 4) {
				count = 5;
			}
			if( wreset > 0 )
			{				
				List<DBWeaponProp> newProps = new ArrayList<DBWeaponProp>();
				for (int i = 0; i < count; i ++) {
					SBean.WeaponResetPropPassCFGS passCfg = wcfg.propPasses.get(i);
					SBean.WeaponResetPropCFGS prop = null;
					boolean dup = false;
					do {
						if (passCfg.pass.size() == 0)
							break;

						int total = 0;
						for (SBean.WeaponResetPropCFGS p : passCfg.pass) {
							total += p.poss;
						}
						int rand = gs.getGameData().getRandom().nextInt(total);
						total = 0;
						int randProp = 0;
						for (SBean.WeaponResetPropCFGS p : passCfg.pass) {
							total += p.poss;
							if (total > rand)
								break;
							randProp ++;
						}

						SBean.WeaponResetPropCFGS propCfg = passCfg.pass.get(randProp);
						prop = propCfg;
//						dup = false;
//						for (DBWeaponProp p : newProps) {
//							if (p.propId == propCfg.propId) {
//								dup = true;
//								break;
//							}
//						}
					}
					while (dup);

					SBean.WeaponResetRuleCFGS rule = null;
					for (SBean.WeaponResetRuleCFGS r : wcfg.rules) {
						if (r.id == prop.rule) {
							rule = r;
							break;
						}
					}

					if (rule != null)
					{
						int total = 0;
						for (byte p : rule.percent)
							total += p;
						int rand = gs.getGameData().getRandom().nextInt(total);
						int index = 0;
						total = 0;
						for (byte p : rule.percent) {
							total += p;
							if (total > rand)
								break;
							index ++;
						}

						if (wreset == 2) {
							byte rank = (byte)rule.percent.size();
							newProps.add(new SBean.DBWeaponProp(prop.propId, prop.type, rank, prop.valueMax));
						}
						else {
							float span = prop.valueMax - prop.valueMin;
							span /= rule.percent.size();
							float value = span * index;
							float randProp = gs.getGameData().getRandom().nextInt(100) * span / 99;
							value += randProp;
							byte rank = (byte)(value / span);
							value += prop.valueMin;
							newProps.add(new SBean.DBWeaponProp(prop.propId, prop.type, rank, value));
						}
					}
				}
				w.passes = newProps;
			}
		}
	}

	public boolean generalsUpgradeSkill(short gid, byte pos)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
			if( skillPoint < 1 )
				return false;
			if( pos < 1 || pos > GameData.GENERAL_SKILL_COUNT )
				return false;
			SBean.GeneralCmnCFGS cmncfg = gs.getGameData().getGeneralCmnCFG();
			if( pos > cmncfg.skills.size() )
				return false;
			SBean.GeneralSkillCFGS scfg = cmncfg.skills.get(pos-1);
			if( g.advLvl < scfg.reqAdvLvl )
				return false;
			short lvlNow = g.skills.get(pos-1).shortValue(); 
			if( lvlNow + scfg.maxLvl >= g.lvl ) 
				return false;
			if( lvlNow < 1 || lvlNow >= scfg.money.size() )
				return false;
			int moneyNeed = scfg.money.get(lvlNow-1);
			if( money < moneyNeed )
				return false;
			commonFlowRecord.addChange(useMoney(moneyNeed));
			g.skills.set(pos-1, (short)(lvlNow+1));
			useSkillPoint();
			
			commonFlowRecord.setEvent(TLog.AT_GENERAL_UPGRADE_SKILL);
			commonFlowRecord.setArg(gid, pos);
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_UPGRADE_GENERAL_SKILL, 1));
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean generalsActiveWeapon(short gid)
	{
		General g = generals.get(gid);
		if( g == null )
			return false;
		if (g.weapon.lvl >= 0)
			return false;
		SBean.WeaponBasicCFGS cfg = gs.getGameData().getWeaponBasicCFG(gid);
		if (cfg == null || g.advLvl < cfg.reqAdvLvl)
			return false;
		
		g.weapon.lvl = 0;
		return true;
	}
	
	public short generalsEnhanceWeapon(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		short weaponLvl = 0;
		synchronized (this)
		{
			General g = generals.get(gid);
			if( g == null )
				return -1;
			if (g.weapon.lvl < 0)
				return -2;
			SBean.WeaponEnhanceCFGS cfg = gs.getGameData().getWeaponEnhanceCFG(gid);
			if (cfg == null)
				return -3;
			if (cfg.enhanceStages.size() <= g.weapon.evo)
				return -4;
			byte stages = 0;
			for (int i = 0; i <= g.weapon.evo; i ++)
				stages += cfg.enhanceStages.get(i).enhanceCount;
			if (g.weapon.lvl >= stages)
				return -5;
			int index = g.weapon.lvl * 3;
			if (cfg.enhanceCosts.size() < index + 3)
				return -6;
			for (int i = 0; i < 3; i ++) {
				SBean.DropEntryNew e = cfg.enhanceCosts.get(index + i);
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					if( e.arg <= 0 || getItemCount(e.id) < e.arg )
						return -7;
				}
				else if( e.type == GameData.COMMON_TYPE_EQUIP )
				{
					if( e.arg <= 0 || getEquipCount(e.id) < e.arg )
						return -8;
				}
				else if (e.type == GameData.COMMON_TYPE_MONEY)
				{
					if (e.arg <= 0 || money < e.arg)
						return -9;
				}
				else if (e.type == GameData.COMMON_TYPE_STONE)
				{
					if (e.arg <= 0 || stone < e.arg)
						return -10;
				}
				else if (e.type == GameData.COMMON_TYPE_NULL) {
				}
				else
					return -11;	
			}
			
			for (int i = 0; i < 3; i ++) {
				SBean.DropEntryNew e = cfg.enhanceCosts.get(index + i);
				if (e.type != GameData.COMMON_TYPE_NULL)
				{
					TLogger.CommonItemChange change = delDropNew(e);
					if (change == null)
						return -12;
					commonFlowRecord.addChange(change);
				}
			}
			weaponLvl = ++(g.weapon.lvl);
			
			commonFlowRecord.setEvent(TLog.AT_GENERAL_WEAPON_ENHANCE);
			commonFlowRecord.setArg(gid, weaponLvl);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return weaponLvl;
	}
	
	public byte generalsEvoWeapon(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		byte weaponEvo = 0;
		synchronized (this)
		{
			General g = generals.get(gid);
			if( g == null )
				return -1;
			if (g.weapon.lvl < 0)
				return -2;
			SBean.WeaponEnhanceCFGS cfg = gs.getGameData().getWeaponEnhanceCFG(gid);
			if (cfg == null)
				return -3;
			if (cfg.enhanceStages.size() <= g.weapon.evo + 1)
				return -4;
			byte stages = 0;
			for (int i = 0; i <= g.weapon.evo; i ++)
				stages += cfg.enhanceStages.get(i).enhanceCount;
			if (g.weapon.lvl < stages)
				return -5;
			if (g.weapon.evo >= gs.getGameData().getWeaponEvoCostsCFG().size())
				return -6;
			SBean.WeaponEvoCostCFGS costCfg = gs.getGameData().getWeaponEvoCostsCFG().get(g.weapon.evo);
			if( money < costCfg.costMoney )
				return -7;
			SBean.GeneralCFGS gcfg = GameData.getInstance().getGeneralCFG(gid);
			short costEquipID = gcfg.type == 0 ? costCfg.costEquipID0 : costCfg.costEquipID1;
			if( costEquipID > 0 && getEquipCount(costEquipID) < 1 )
				return -8;
			commonFlowRecord.addChange(useMoney(costCfg.costMoney));
			commonFlowRecord.addChange(delEquip(costEquipID, 1)); // TODO		
			
			weaponEvo = ++(g.weapon.evo);
			
			commonFlowRecord.setEvent(TLog.AT_GENERAL_WEAPON_ADVANCE);
			commonFlowRecord.setArg(gid, weaponEvo);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return weaponEvo;
	}
	
	public boolean generalsResetWeapon(short gid, Set<Integer> lock, Set<Integer> lock2 ,List<SBean.DBWeaponProp> props)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
			
			if (g.weapon.lvl < 0)
				return false;
			SBean.WeaponResetCFGS cfg = gs.getGameData().getWeaponResetCFG();
			if (cfg == null)
				return false;
			if (g.weapon.evo < 3) // purple
				return false;
			
			int moneyCost = 0;
			int stoneCost = 0;
			switch (lock.size()) {
			case 0:
				moneyCost = cfg.noLock.costMoney;
				stoneCost = cfg.noLock.costStone;
				break;
			case 1:
				moneyCost = cfg.lock1.costMoney;
				stoneCost = cfg.lock1.costStone;
				break;
			case 2:
				moneyCost = cfg.lock2.costMoney;
				stoneCost = cfg.lock2.costStone;
				break;
			case 3:
				moneyCost = cfg.lock3.costMoney;
				stoneCost = cfg.lock3.costStone;
				break;
			default:
				return false;
			}
			
			if(lvlVIP<14&&lock2.size()>0){
				return false;
			}
			
			switch (lock2.size()) {
			case 0:
				moneyCost += 0;
				stoneCost += 0;
				break;
			case 1:
				moneyCost += cfg.nlock1.costMoney;
				stoneCost += cfg.nlock1.costStone;
				break;
			case 2:
				moneyCost += cfg.nlock2.costMoney;
				stoneCost += cfg.nlock2.costStone;
				break;
			case 3:
				moneyCost += cfg.nlock3.costMoney;
				stoneCost += cfg.nlock3.costStone;
				break;
			case 4:
				moneyCost += cfg.nlock4.costMoney;
				stoneCost += cfg.nlock4.costStone;
				break;
			case 5:
				moneyCost += cfg.nlock5.costMoney;
				stoneCost += cfg.nlock5.costStone;
				break;
			default:
				return false;
			}
			
			if (money < moneyCost || stone < stoneCost)
				return false;
			
			int count = g.weapon.passes.size();
			if (lock.size() > count)
				return false;
			List<SBean.WeaponResetNewPropCFGS> newProp = null;
			if (g.weapon.evo == 3) {
				newProp = cfg.newPropPurple;
			}
			else if (g.weapon.evo == 4) {
				newProp = cfg.newPropOrange;
			}
			if (newProp == null)
				return false;
			
			if (count >= newProp.size())
				return false;
			SBean.WeaponResetNewPropCFGS npcfg = newProp.get(count);
			if (npcfg == null)
				return false;
			int total = 0;
			for (byte p : npcfg.percent)
				total += p;
			int rand = gs.getGameData().getRandom().nextInt(total);
			total = 0;
			int newPass = 0;
			for (byte p : npcfg.percent) {
				total += p;
				if (total > rand)
					break;
				newPass ++;
			}
			count += newPass;
			
			List<DBWeaponProp> newProps = new ArrayList<DBWeaponProp>();
			for (int i = 0; i < count; i ++) {
				if (lock.contains(i + 1))
					newProps.add(g.weapon.passes.get(i));
				else {
					int passes = cfg.propPasses.size();
					if (passes <= i)
						return false;
					SBean.WeaponResetPropPassCFGS passCfg = cfg.propPasses.get(i);
					SBean.WeaponResetPropCFGS prop = null;
					boolean dup = false;
					do {
						if (passCfg.pass.size() == 0)
							break;
						
						total = 0;
						for (SBean.WeaponResetPropCFGS p : passCfg.pass) {
							total += p.poss;
						}
						rand = gs.getGameData().getRandom().nextInt(total);
						total = 0;
						int randProp = 0;
						for (SBean.WeaponResetPropCFGS p : passCfg.pass) {
							total += p.poss;
							if (total > rand)
								break;
							randProp ++;
						}

						SBean.WeaponResetPropCFGS propCfg = passCfg.pass.get(randProp);
						prop = propCfg;
//						dup = false;
//						for (DBWeaponProp p : newProps) {
//							if (p.propId == propCfg.propId) {
//								dup = true;
//								break;
//							}
//						}
					}
					while (dup);
					
					if (prop == null)
						return false;
					
					if (lock2.contains(i + 1)){
						for( SBean.WeaponResetPropCFGS propCfgLock2:passCfg.pass){
							if(g.weapon.passes.get(i).propId==propCfgLock2.propId){
								prop=propCfgLock2;
							}
						}
						
					}
					
					SBean.WeaponResetRuleCFGS rule = null;
					for (SBean.WeaponResetRuleCFGS r : cfg.rules) {
						if (r.id == prop.rule) {
							rule = r;
							break;
						}
					}
					
					if (rule == null)
						return false;
					
					total = 0;
					for (byte p : rule.percent)
						total += p;
					rand = gs.getGameData().getRandom().nextInt(total);
					int index = 0;
					total = 0;
					for (byte p : rule.percent) {
						total += p;
						if (total > rand)
							break;
						index ++;
					}
					
					float span = prop.valueMax - prop.valueMin;
					span /= rule.percent.size();
					float value = span * index;
					float randProp = gs.getGameData().getRandom().nextInt(100) * span / 99;
					value += randProp;
					byte rank = (byte)(value / span);
					value += prop.valueMin;
					
					if (lock2.contains(i + 1)){
						DBWeaponProp dbWeaponProp = g.weapon.passes.get(i);
						float propvalue= (dbWeaponProp.value>=value?dbWeaponProp.value:value);
						byte  proprank = (byte) (dbWeaponProp.value>=value?dbWeaponProp.rank:rank);
						newProps.add(new SBean.DBWeaponProp(prop.propId, prop.type, proprank, propvalue));
					}else{
						newProps.add(new SBean.DBWeaponProp(prop.propId, prop.type, rank, value));
					}
					
				}
			}
			commonFlowRecord.addChange(useMoney(moneyCost));
			commonFlowRecord.addChange(useStone(stoneCost));
			g.weapon.resetPasses = newProps;
			for (DBWeaponProp p : newProps)
				props.add(p.kdClone());
			
			commonFlowRecord.setEvent(TLog.AT_GENERAL_WEAPON_RESET);
			commonFlowRecord.setArg(gid, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean generalsSaveResetWeapon(short gid, List<SBean.DBWeaponProp> props)
	{
		General g = generals.get(gid);
		if( g == null )
			return false;
		
		if (g.weapon.lvl < 0)
			return false;
		if (g.weapon.evo < 3) // purple
			return false;
		
		if (g.weapon.resetPasses.size() == 0)
			return false;
		
		g.weapon.passes.clear();
		for (DBWeaponProp p : g.weapon.resetPasses)
			g.weapon.passes.add(p.kdClone());
		g.weapon.resetPasses.clear();
		for (DBWeaponProp p : g.weapon.passes)
			props.add(p.kdClone());
		return true;
	}
	
	public boolean generalsRevertSeyen(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		
		int cost =GameData.getInstance().generalSeyen.commonValue.reversCost;
		if(stone<cost){
			return false;
		}
		
		General g = generals.get(gid);
		if( g == null )
			return false;
		if(g.generalSeyen==null||g.generalSeyen.size()==0){
			return false;
		}
		SBean.DBGeneralSeyen seyen = g.generalSeyen.get(0);
		if(seyen.lvl==0&&seyen.exp==0){
			return false;
		}
		
		int count =seyen.exp;
		
		for(int i = 0 ;i<seyen.lvl;i++){
			
			count += GameData.getInstance().generalSeyenExp.get(i);
		}
				
		if(generalStoneInfo==null){
			generalStoneInfo = new SBean.DBGeneralStoneInfo();
			generalStoneInfo.generalSeyen = new ArrayList<SBean.DBRoleSeyen>();
		}
		if(generalStoneInfo.generalSeyen==null){
			generalStoneInfo.generalSeyen = new ArrayList<SBean.DBRoleSeyen>();
		}
		if(generalStoneInfo.generalSeyen.size()==0){
			DBRoleSeyen seyenTemp = new DBRoleSeyen();
			generalStoneInfo.generalSeyen.add(seyenTemp);
		}
		DBRoleSeyen m =generalStoneInfo.generalSeyen.get(0);
		
		if(m.seyenTotal-seyen.lvl<0){
			return false;
		}
		m.seyenTotal-=seyen.lvl;
				
		commonFlowRecord.addChange(useStone(cost));
		
		int seyenAdd = (int)(count*GameData.getInstance().generalSeyen.commonValue.reversRate);
		m.seyen+=seyenAdd;
		commonFlowRecord.addChange(new TLogger.CommonItemChange(GameData.COMMON_TYPE_SEYEN, 0, seyenAdd, m.seyen));
		if(seyen.lvl>0){
			
			for(General general : generals.values()){
				if(general!=null){
					if(general.generalSeyen==null||general.generalSeyen.size()==0){
						general.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
						general.generalSeyen.add(new SBean.DBGeneralSeyen());
					}
					general.generalSeyen.get(0).seyenTotal=m.seyenTotal;
					
				}
			}		
		}
		
		seyen.lvl=0;
		seyen.exp=0;
		
		commonFlowRecord.setEvent(TLog.AT_SEYEN_REVERT);
		commonFlowRecord.setArg(m.seyenTotal, gid);
 		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
		
	}
	
	public boolean generalsAddSeyenExp(short gid,byte type, List<Short> props)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
			
			if(generalStoneInfo==null){
				return false;
			}
			if(generalStoneInfo.generalSeyen==null||generalStoneInfo.generalSeyen.size()==0){
				return false;
			}
			
			DBRoleSeyen m =generalStoneInfo.generalSeyen.get(0);
			
			if(type==1&&m.seyen<GameData.getInstance().generalSeyen.commonValue.sItemId){
				return false;
			}else if(type==2&&m.seyen<GameData.getInstance().generalSeyen.commonValue.sItemId*10){
				return false;
			}
			
			if(g.generalSeyen==null||g.generalSeyen.size()==0){
				g.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
				g.generalSeyen.add(new SBean.DBGeneralSeyen());
			}
			
			SBean.DBGeneralSeyen seyen = g.generalSeyen.get(0);
			
			if(seyen==null){
				return false;
			}
			
			Integer levcfg = GameData.getInstance().generalSeyenExp.get((int)seyen.lvl);
			if(levcfg==null||levcfg<=0){
				return false;
			}
			
			Integer levcfgLimit = GameData.getInstance().generalSeyenLimit.get((int)seyen.lvl);
			if(levcfgLimit==null||levcfgLimit>this.lvl){
				return false;
			}
			
			int sum = 0;
			
			int expCount = 1;
			if(type==2){
				expCount = 10;
			}
			List<GeneralSeyenExpAddCFGS> rate =GameData.getInstance().generalSeyen.expAdd;
			
			int total1 =0 ;
			for(GeneralSeyenExpAddCFGS expRate:rate){
				total1+=expRate.weight;
			}
//			for(int i = 0 ; i<expCount ;i++){
			
				int rand = gs.getGameData().getRandom().nextInt(total1);
				int index = 0;
				int total = 0;
				for (GeneralSeyenExpAddCFGS p : rate) {
					total += p.weight;
					if (total > rand)
						break;
					index ++;
				}
				
				GeneralSeyenExpAddCFGS tempExp=rate.get(index);
				
				if(tempExp!=null){
					sum += tempExp.exp*GameData.getInstance().generalSeyen.commonValue.sItemId*expCount;
					props.add(tempExp.sid);
				}
//			}
			
			boolean bUP = false;
			int itemidTemp = seyen.lvl;
			int expTemp = seyen.exp;
			while( sum > 0 )
			{
				Integer seyenLev = GameData.getInstance().generalSeyenExp.get(itemidTemp);
				if(seyenLev==null||seyenLev<=0){
					break;
				}
				int expUp = seyenLev;
				int expNeed = expUp - expTemp;//stoneInfo.exp;
				if( expNeed < 0 )
					expNeed = 0;
				if( sum < expNeed )
				{
					expTemp += sum;//stoneInfo.exp += sum;
//					moneyNeed += 0;
					break;
				}
				m.seyenTotal++;
				bUP=true;
				itemidTemp+=1; //stoneInfo.itemId=rcfg.upIid;
				expTemp = 0; //stoneInfo.exp = 0;
				sum -= expNeed;				
				Integer seyenLev1 = GameData.getInstance().generalSeyenExp.get(itemidTemp);
				if(seyenLev1==null||seyenLev1<=0){
					break;
				}
			}
			
			if(type==1){
				int seyenSub = GameData.getInstance().generalSeyen.commonValue.sItemId;
				m.seyen-=seyenSub;
				commonFlowRecord.addChange(new TLogger.CommonItemChange(GameData.COMMON_TYPE_SEYEN, 0, -seyenSub, m.seyen));
			}else if(type==2){
				int seyenSub = GameData.getInstance().generalSeyen.commonValue.sItemId*10;
				m.seyen-=seyenSub;
				commonFlowRecord.addChange(new TLogger.CommonItemChange(GameData.COMMON_TYPE_SEYEN, 0, -seyenSub, m.seyen));
			}
			
			if(bUP){
				for(General general : generals.values()){
					if(general!=null){
						if(general.generalSeyen==null||general.generalSeyen.size()==0){
							general.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
							general.generalSeyen.add(new SBean.DBGeneralSeyen());
						}
						general.generalSeyen.get(0).seyenTotal=m.seyenTotal;
						
					}

				}
			}
						
			seyen.lvl=(short) itemidTemp;
			seyen.exp=expTemp;
			
			commonFlowRecord.setEvent(TLog.AT_SEYEN_ADD);
			commonFlowRecord.setArg(m.seyenTotal, gid, seyen.lvl, seyen.exp);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public int acceptForce()
	{
		synchronized( this )
		{
			if( bDBLock )
				return -1;
			if( forceInfo.isInForce() )
				return 1;
			bDBLock = true;
		}
		return 0;
	}
	
	public synchronized void clearForce()
	{
		forceInfo.changeForce(0, (short)0, "", 0);
	}
	
	public synchronized void clearForce(int fid, boolean bClearCool)
	{
		if( fid == forceInfo.id )
		{
			forceInfo.changeForce(0, (short)0, "", 0);
			if( bClearCool )
				forceInfo.joinTime = 0;
		}
	}
	
	public void setSearchFriendsRes(SBean.SearchFriendsRes res)
	{
		List<SBean.DBFriendData> newList;
		synchronized( this )
		{
			newList = friend.setSearchFriendsRes(res);
		}
		if( newList != null && ! newList.isEmpty() && netsid > 0 )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeFriendSyncUpdate(newList));
		}
	}
	
	public SBean.FriendPets copyFriendPetsWithoutLock()
	{
		SBean.FriendPets fpets = new SBean.FriendPets();
		fpets.petsG1 = new ArrayList<SBean.DBPetBrief>();
		fpets.petsTop = new ArrayList<SBean.DBPetBrief>();
		
		for(Pet p : pets.values())
		{
			byte deformStage = 0;
			SBean.DBPetDeform deform = petDeforms.get(p.id);
			if (deform != null)
				deformStage = deform.deformStage;
			if( GameData.getInstance().getPetCFG(p.id, false).gender == 1 && p.lvl >= GameData.getInstance().getPetCmnCFG().breedReqLvl )
				fpets.petsG1.add(p.copyDBFriendPetWithoutLock(deformStage));
			if( p.id == petsTop ) 
				fpets.petsTop.add(p.copyDBFriendPetWithoutLock(deformStage));
		}
		return fpets;
	}
	
	/*
	public List<SBean.FriendPetDeform> copyFriendPetDeformsWithoutLock(SBean.FriendPets fpets)
	{
		List<SBean.FriendPetDeform> petDeformsTop = new ArrayList<SBean.FriendPetDeform>();
		
		for(SBean.DBPetDeform p : petDeforms.values())
		{
			for (SBean.DBPetBrief fpb : fpets.petsTop)
				if (fpb.id == p.id)
					petDeformsTop.add(new SBean.FriendPetDeform(p.id, p.deformStage, 0, 0));
		}
		return petDeformsTop;
	}
	*/
	
	public SBean.FriendProp getFriendPropWithoutLock()
	{
		return new SBean.FriendProp(lvl, lastLoginTime, name, copyArenaGeneralsWithoutLock(),
				copyArenaPetsWithoutLock(), copyFriendPetsWithoutLock(), forceInfo.name, headIconID, getQQVIP(), 
				new ArrayList<SBean.FriendPetDeform>(), getActiveRelationsWithoutLock(),  getActiveGeneralStones(), (byte)0);
	}
	
	public synchronized SBean.FriendProp getFriendPropWithLock()
	{
		return getFriendPropWithoutLock();
	}
	
	public void recvMessageFromFriend(SBean.GlobalRoleID srcID, byte msgType, short arg1, int arg2)
	{
		friend.friend.recvMessageFromFriend(srcID, gs.getTime(), msgType, arg1, arg2);
	}
	
	public void setAcceptForceRes(ForceInfo info)
	{
		synchronized( this )
		{
			if( info != null )
			{
				forceInfo.changeForce(info.id, info.iconId, info.name, gs.getTime());
			}
			bDBLock = false;
		}
	}
	
	public void onCreateForceRes(final int fid, short iconId, String fname)
	{
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_CREATE);
		synchronized( this )
		{
			SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
			//锟斤拷锟斤拷锟斤拷锟角拷锟斤拷锟斤拷锟斤拷锟斤拷锟铰扣筹拷
			addStonePresent(cfg.stoneCreate);
			if( fid > 0 )
			{
				forceInfo.changeForce(fid, iconId, fname, gs.getTime());
				commonFlowRecord.addChange(useStone(cfg.stoneCreate));
			}
			bDBLock = false;
		}
		if( fid > 0 )
			doSave();
		gs.getRPCManager().notifyCreateForceRes(netsid, fid);
		commonFlowRecord.setEvent(TLog.AT_CREATE_FORCE);
		commonFlowRecord.setArg(fid, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public synchronized void getData(SBean.RoleDataRes res)
	{
		if( ( res.req.qtype & SBean.RoleDataReq.eBrief) != 0 )
		{
			res.brief = new SBean.RoleDataBrief();
			res.brief.id = id;
			res.brief.name = name;
			res.brief.uname = userName;
			res.brief.lvl = lvl;
			res.brief.money = money;
			res.brief.stone = stone;
			res.brief.gcount = generals.size();
			res.brief.icount = items.size();
			res.brief.ecount = equips.size();
			res.brief.headIconID = headIconID;
			res.brief.fname = forceInfo.name;
			res.brief.arenaPower = 0;
			res.brief.arenaWinTimes = arenaState.winTimes;
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eGeneral) != 0 )
		{
			res.generals = new ArrayList<SBean.RoleDataGeneral>();
			for(General g : generals.values())
			{
				res.generals.add(new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo));
			}
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eItem) != 0 )
		{
			res.items = copyItemsWithoutLock1();
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eEquip) != 0 )
		{
			res.equips = copyEquipsWithoutLock();
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eArenaGeneral) != 0 )
		{
			res.arenagenerals = new ArrayList<SBean.RoleDataGeneral>();
			for (short gid : arenaGenerals)
			{
				General g = generals.get(gid);
				if (g != null)
					res.arenagenerals.add(new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo));
			}
			res.arenapets = new ArrayList<SBean.RoleDataPet>();
			for (short pid : arenaPets)
			{
				Pet p = pets.get(pid);
				if (p != null)
					res.arenapets.add(new SBean.RoleDataPet(p.id, p.lvl, p.evoLvl, p.growthRate, p.name));
			}
			res.brief.arenaPower = getArenaGeneralsPowerWithoutLock();
			res.brief.arenaPower += getArenaPetsPowerWithoutLock();
		}
		if ( ( res.req.qtype & SBean.RoleDataReq.eSuperArenaTeam) != 0 )
		{
			if (superArena != null)
			{
				int curOrder = DBRole.getSuperArenaCurOrder(superArena);
				List<SBean.DBRoleArenaFightTeam> teams = ArenaManager.SuperArena.getOrderedFightTeams(superArena.team.teams, curOrder);
				res.superArena = new SBean.RoleDataSuperArena(new ArrayList<SBean.RoleDataFightTeam>(), superArena.winTimes);
				int hiddenTeams = DBRole.getSuperArenaTeamHideCount(superArena);
				for (int index = 0; index < teams.size(); ++index)
				{
					if (index < hiddenTeams)
					{
						res.superArena.teams.add(null);
					}
					else
					{
						SBean.DBRoleArenaFightTeam t = teams.get(index);
						res.superArena.teams.add(superArenaFillTeam(t));
					}
				}
			}
		}
		res.res = SBean.RoleDataRes.eOK;
	}
	
	public synchronized void getMarchEnemy(SBean.DBRoleMarchEnemy enemy)
	{
		enemy.id = id;
		enemy.name = name;
		enemy.headIconID = headIconID;
		enemy.fname = forceInfo.name;
		enemy.lvl = lvl;
		enemy.generals = new ArrayList<DBRoleGeneral>();
		enemy.pets = new ArrayList<SBean.DBPetBrief>();
		SBean.DBPowerRank rank = gs.getMarchManager().copyPowerRoleRankData(id);
		if (rank == null)
			return;
		for (short gid : rank.generals)
		{
			General g = generals.get(gid);
			if (g != null){
				DBRoleGeneral kdClone = g.kdClone();
				changeSeyenValue(kdClone);
				enemy.generals.add(kdClone);
			}
				
		}
		Pet p = pets.get(rank.pets);
		if (p != null) {
			byte deformStage = 0;
			SBean.DBPetDeform deform = petDeforms.get(p.id);
			if (deform != null)
				deformStage = deform.deformStage;
			enemy.pets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
		}
	}
	
	public synchronized void getSuraEnemy(SBean.DBSuraEnemy enemy)
	{
		enemy.id = id;
		enemy.name = name;
		enemy.headIconID = headIconID;
		enemy.fname = forceInfo.name;
		enemy.lvl = lvl;
		enemy.generals = new ArrayList<DBRoleGeneral>();
		enemy.pets = new ArrayList<SBean.DBPetBrief>();
		enemy.generals = copyArenaGeneralsWithoutLock();
		enemy.pets = copyArenaPetsWithoutLock();
	}
	
	public List<SBean.DropEntry> eggTake(byte eggID)
	{
		SBean.EggCFGS ecfg = gs.getGameData().getEggCFG(eggID);
		if( ecfg == null )
			return null;
		List<SBean.DropEntry> resList = new ArrayList<SBean.DropEntry>();
		// TODO
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ChestRecord record = new TLogger.ChestRecord(this.lvl);
		synchronized( this )
		{
			if( ! testFlag(Role.ROLE_FLAG_EGG) )
				return null;
			dayRefresh();
			SBean.DBEggLog log = null;
			for(SBean.DBEggLog e : eggLogs)
			{
				if( e.eggID == eggID )
				{
					log = e;
					break;
				}
			}
			if( log == null )
				return null;
			boolean bFree = false;

			int now = gs.getTime();
			if( log.lastTime + ecfg.freeCool * 60 < now )
			{
				if( ecfg.freeTimes <= 0 )
				{
					bFree = true;
				}
				else if( log.timesToday < ecfg.freeTimes )
				{
					bFree = true;
					++log.timesToday;
				}
			}
						
			if( bFree )
			{
				log.lastTime = now;
			}
			else
			{
				if( eggID == GameData.EGG_STONE )
				{
					if( stone < ecfg.cost )
						return null;
					commonFlowRecord.addChange(useStone(ecfg.cost));
				}
				else
				{
					if( money < ecfg.cost )
						return null;
					commonFlowRecord.addChange(useMoney(ecfg.cost));
				}
			}
			
			if( log.firstEgg == 0  )
			{
				resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.firstFreeDropTableID));
				log.firstEgg = 1;
			}
			else if( log.firstEgg == 1 && ! bFree && ecfg.firstDropTableID > 0 )
			{
				resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.firstDropTableID));
				log.firstEgg = 2;
			}
			
			if( resList.isEmpty() )
			{
				boolean bGeneral = GameData.getInstance().getRandom().nextFloat() < ecfg.propGeneral;
				if( log.poolPos >= ecfg.poolHigh )
					bGeneral = false;
				else if( log.poolPos <= ecfg.poolLow )
					bGeneral = true;
				if( bGeneral )
				{
					log.poolPos += ecfg.generalUp;
					resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID));
				}
				else
				{
					log.poolPos -= ecfg.itemDown;
					resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
				}
			}
						
			commonFlowRecord.addChanges(addDrops(resList));
			commonFlowRecord.addChange(addDrop(ecfg.itemFixed));
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_EGG, 1));
			record.setBuyInfo(eggID == GameData.EGG_STONE ? TLog.CHEST_GOLD : TLog.CHEST_BRONZE, 1, ecfg.cost, eggID);
		}
		tlogEvent.addRecord(record);
		commonFlowRecord.setEvent(eggID == GameData.EGG_STONE ? TLog.AT_GOLD_EGG : TLog.AT_BRONZE_EGG);
		commonFlowRecord.setArg(eggID, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return resList;
	}
	
	public List<SBean.DropEntry> eggTake10(byte eggID)
	{
		SBean.EggCFGS ecfg = gs.getGameData().getEggCFG(eggID);
		if( ecfg == null )
			return null;
		List<SBean.DropEntry> resList = new ArrayList<SBean.DropEntry>();
		// TODO
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ChestRecord record = new TLogger.ChestRecord(this.lvl);
		synchronized( this )
		{
			if( ! testFlag(Role.ROLE_FLAG_EGG) )
				return null;
			dayRefresh();
			SBean.DBEggLog log = null;
			for(SBean.DBEggLog e : eggLogs)
			{
				if( e.eggID == eggID )
				{
					log = e;
					break;
				}
			}
			if( log == null )
				return null;

			int cost10 = 0;
			if( eggID == GameData.EGG_STONE )
			{
				cost10 = ecfg.cost10;
				//
				if( log.timesAll10 < ecfg.cost10FirstBuy.size() )
				{
					cost10 = ecfg.cost10FirstBuy.get(log.timesAll10);
				}
				//
				if( stone < cost10 )
					return null;
				commonFlowRecord.addChange(useStone(cost10));
			}
			else
			{
				cost10 = ecfg.cost10;
				if( money < cost10 )
					return null;
				commonFlowRecord.addChange(useMoney(cost10));
			}
			
			if( eggID == GameData.EGG_STONE )
			{
				int nGeneral = GameData.getInstance().getRandInt(ecfg.minGeneral10, ecfg.maxGeneral10);
				boolean bProtect = false;
				// 元锟斤拷十锟斤拷锟斤拷前N锟斤拷锟斤拷锟斤拷锟解保锟斤拷  1) 10锟斤拷锟斤拷锟戒将锟斤拷锟截革拷  2) 锟截筹拷锟斤拷锟斤拷一锟斤拷锟斤拷锟戒将
				if( log.timesAll10 < ecfg.proectTimes10 )
				{
					if( generals.size() < 32 )
						bProtect = true;
					if( ++log.timesAll10 > ecfg.proectTimes10 )
					{
						log.timesAll10 = ecfg.proectTimes10;
					}
				}
				//
				if( log.timesAll10 == 1 && ecfg.dropTableIDsFirst10.size() > 1 )
				{
					for(int i = 0; i < 10; ++i)
					{
						if( i < ecfg.dropTableIDsFirst10.size() - 1 )
						{
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.dropTableIDsFirst10.get(i)));
						}
						else
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.dropTableIDsFirst10.get(ecfg.dropTableIDsFirst10.size()-1)));
					}
				}
				else
				{
					Set<Short> gidSetThisTime = new TreeSet<Short>();
					for(int i = 0; i < 10; ++i)
					{
						if( i < nGeneral )
						{
							SBean.DropEntry e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
							if( bProtect )
							{
								if( gidSetThisTime.isEmpty() )
								{
									int nMaxTimes = 0;
									while( generals.containsKey(e.id) )
									{
										e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
										if( ++nMaxTimes >= 1000 )
											break;
									}
								}
								else
								{
									while( gidSetThisTime.contains(e.id) )
									{
										e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
									}
								}
								gidSetThisTime.add(e.id);
							}
							resList.add(e);
						}
						else
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
					}
				}
				//
			}
			else // EGG_MONEY
			{
				for(int i = 0; i < 9; ++i)
				{
					boolean bGeneral = GameData.getInstance().getRandom().nextFloat() < ecfg.propGeneral;
					if( bGeneral )
						resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID));
					else
						resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
				}
				SBean.DropEntry eItemRank2 = GameData.getInstance().getDropTableRandomEntryItemRank2();
				if( eItemRank2 != null )
					resList.add(eItemRank2);
				else
					resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
			}
			Collections.shuffle(resList);
			commonFlowRecord.addChanges(addDrops(resList));
			for(int i = 0; i < 10; ++i)
			{
				commonFlowRecord.addChange(addDrop(ecfg.itemFixed));
			}
			record.setBuyInfo(eggID == GameData.EGG_STONE ? TLog.CHEST_GOLD : TLog.CHEST_BRONZE, 10, ecfg.cost10, eggID);
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_EGG, 10));
		}
		commonFlowRecord.setEvent(eggID == GameData.EGG_STONE ? TLog.AT_GOLD_EGG10 : TLog.AT_BRONZE_EGG10);
		commonFlowRecord.setArg(eggID, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
		return resList;
	}
	
	
	public List<SBean.DropEntry> eggTake100(byte eggID)
	{
		SBean.EggCFGS ecfg = gs.getGameData().getEggCFG(eggID);
		if( ecfg == null )
			return null;
		List<SBean.DropEntry> resList = new ArrayList<SBean.DropEntry>();
		// TODO
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ChestRecord record = new TLogger.ChestRecord(this.lvl);
		synchronized( this )
		{
			if( ! testFlag(Role.ROLE_FLAG_EGG) )
				return null;
			dayRefresh();
			SBean.DBEggLog log = null;
			for(SBean.DBEggLog e : eggLogs)
			{
				if( e.eggID == eggID )
				{
					log = e;
					break;
				}
			}
			if( log == null )
				return null;

			int cost100 = 0;
			int cost10 = 0;
			int count  = 10 ;
			if( eggID == GameData.EGG_STONE )
			{				
				for(int i = 0; i<count;i++){
					cost10 = ecfg.cost10;
					//
					if( log.timesAll10 < ecfg.cost10FirstBuy.size() )
					{
						cost10 = ecfg.cost10FirstBuy.get(log.timesAll10);
					}
					cost100+=cost10;
				}
				
				//
				if( stone < cost100 )
					return null;
				commonFlowRecord.addChange(useStone(cost100));
			}
			else
			{
				cost10 = ecfg.cost10;
				cost100=cost10*10;
				if( money < cost100 )
					return null;
				commonFlowRecord.addChange(useMoney(cost100));
			}
			
			if( eggID == GameData.EGG_STONE )
			{
				int nGeneral = GameData.getInstance().getRandInt(ecfg.minGeneral10, ecfg.maxGeneral10);
				boolean bProtect = false;
				// 元锟斤拷十锟斤拷锟斤拷前N锟斤拷锟斤拷锟斤拷锟解保锟斤拷  1) 10锟斤拷锟斤拷锟戒将锟斤拷锟截革拷  2) 锟截筹拷锟斤拷锟斤拷一锟斤拷锟斤拷锟戒将
				if( log.timesAll10 < ecfg.proectTimes10 )
				{
					if( generals.size() < 32 )
						bProtect = true;
					if( ++log.timesAll10 > ecfg.proectTimes10 )
					{
						log.timesAll10 = ecfg.proectTimes10;
					}
				}
				//
				if( log.timesAll10 == 1 && ecfg.dropTableIDsFirst10.size() > 1 )
				{
					for(int i = 0; i < 10; ++i)
					{
						if( i < ecfg.dropTableIDsFirst10.size() - 1 )
						{
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.dropTableIDsFirst10.get(i)));
						}
						else
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.dropTableIDsFirst10.get(ecfg.dropTableIDsFirst10.size()-1)));
					}
				}
				else
				{
					Set<Short> gidSetThisTime = new TreeSet<Short>();
					for(int i = 0; i < 10; ++i)
					{
						if( i < nGeneral )
						{
							SBean.DropEntry e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
							if( bProtect )
							{
								if( gidSetThisTime.isEmpty() )
								{
									int nMaxTimes = 0;
									while( generals.containsKey(e.id) )
									{
										e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
										if( ++nMaxTimes >= 1000 )
											break;
									}
								}
								else
								{
									while( gidSetThisTime.contains(e.id) )
									{
										e = GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID);
									}
								}
								gidSetThisTime.add(e.id);
							}
							resList.add(e);
						}
						else
							resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
					}
				}
				//
			}
			else // EGG_MONEY
			{
				for(int i = 0; i < 10; ++i)
				{
//					boolean bGeneral = GameData.getInstance().getRandom().nextFloat() < ecfg.propGeneral;
//					if( bGeneral )
//						resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.generalDropTableID));
//					else
						resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.dropTableIDs100));
				}
//				SBean.DropEntry eItemRank2 = GameData.getInstance().getDropTableRandomEntryItemRank2();
//				if( eItemRank2 != null )
//					resList.add(eItemRank2);
//				else
//					resList.add(GameData.getInstance().getDropTableRandomEntry(ecfg.itemDropTableID));
			}
			Collections.shuffle(resList);
			commonFlowRecord.addChanges(addDrops(resList));
			for(int i = 0; i < 10; ++i)
			{
				
				commonFlowRecord.addChange(addDrop(ecfg.itemFixed));
			}
			record.setBuyInfo(eggID == GameData.EGG_STONE ? TLog.CHEST_GOLD : TLog.CHEST_BRONZE, 100, ecfg.cost10*10, eggID);
			tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_EGG, 10));
		}
		commonFlowRecord.setEvent(eggID == GameData.EGG_STONE ? TLog.AT_GOLD_EGG10 : TLog.AT_BRONZE_EGG10);
		commonFlowRecord.setArg(eggID, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
		return resList;
	}
	
	public List<SBean.DropEntry> takeSoulBox(short boxID)
	{
		List<SBean.DropEntry> drops = new ArrayList<SBean.DropEntry>();
		SoulBoxInfo info = getSoulBoxNow(gs.getTime());
		if( info == null )
			return drops;
		if (info.cfg.id != boxID)
			return drops;
		// TODO
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ChestRecord record = new TLogger.ChestRecord(this.lvl);
		synchronized( this )
		{
			if( ! GameData.getInstance().canBuySoulBox(lvlVIP) )
				return drops;
			int stoneCost = info.cfg.cost;
			boolean dropHotPoint = GameData.getInstance().getRandom().nextFloat() < info.cfg.weekHotPointWeight/100.0f;
			SBean.DBSoulBoxLog log = soulBoxLogs.get(info.cfg.id);
			if (log == null)
			{
				log = new SBean.DBSoulBoxLog();
				log.boxID = info.cfg.id;
			}
			if (log.times+1 < info.cfg.weekHotPointFirstInterval || (log.lastWHPTime > 0 && log.times+1-log.lastWHPTime < info.cfg.weekHotPointOtherInterval))
				dropHotPoint = false;
			if (log.times+1-log.lastWHPTime >= info.cfg.weekHotPointMaxInterval)
				dropHotPoint = true;
			if (log.times == 0)
				stoneCost = stoneCost/2;
			if (stone < stoneCost)
				return drops;
			if (dropHotPoint)
			{
				short gid = info.cfg.weekHotPointGeneralID;
				if (optionalHotPoint != null)
					gid = optionalHotPoint.gid;
				drops.add(new SBean.DropEntry(GameData.COMMON_TYPE_GENERAL, (byte)3, gid));
			}
			float dropTimesRate = GameData.getInstance().getRandom().nextFloat();
			int dropTimes = info.cfg.dayHotPointDropTimes.get(info.cfg.dayHotPointDropTimes.size()-1).times;
			for (SBean.DropTimes dt : info.cfg.dayHotPointDropTimes)
			{
				if (dropTimesRate < dt.rate)
				{
					dropTimes = dt.times;
					break;
				}
			}
			for (int i = 0; i < dropTimes; ++i)
			{
				float dropCountRate = GameData.getInstance().getRandom().nextFloat();
				byte dropCount = info.cfg.dayHotPointDropCount.get(info.cfg.dayHotPointDropCount.size()-1).count;
				for (SBean.DropCount dc : info.cfg.dayHotPointDropCount)
				{
					if (dropCountRate < dc.rate)
					{
						dropCount = dc.count;
						break;
					}
				}
				SBean.DayHotPoint dhp = info.cfg.dayHotPoint.get(info.dayIndex);
				int gindex = (int)(GameData.getInstance().getRandom().nextFloat()*dhp.dayHotPoint.size());
				SBean.HotPoint hp = dhp.dayHotPoint.get(gindex);
				drops.add(new SBean.DropEntry(GameData.COMMON_TYPE_ITEM, dropCount, hp.soulItemID));
			}
			int normalDropCount = 8 - drops.size();
			for (int i = 0; i < normalDropCount; ++i)
			{
				drops.add(GameData.getInstance().getDropTableRandomEntry(info.cfg.normalDropTableID));
			}
			commonFlowRecord.addChange(useStone(stoneCost));
			Collections.shuffle(drops);
			commonFlowRecord.addChanges(addDrops(drops));
			log.times++;
			if (dropHotPoint)
				log.lastWHPTime = log.times;
			soulBoxLogs.put(log.boxID, log);
			record.setBuyInfo(TLog.CHEST_SOUL, 1, stoneCost, TLog.CHEST_PRICE_UNIT_DIAMOND);
			tlogEvent.addRecord(record);
			commonFlowRecord.setEvent(TLog.AT_SOUL_BOX);
			commonFlowRecord.setArg(boxID, info.dayIndex, log.times);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return drops;
	}
	
	public int getSoulBoxLogTimes(Role.SoulBoxInfo info)
	{
		if (info == null)
			return 0;
		SBean.DBSoulBoxLog log = soulBoxLogs.get(info.cfg.id);
		if (log == null)
			return 0;
		return log.times;
	}

////////////////////
	public boolean testUnlockBaseCity(int combatid)
	{
		return citys.testUnlockBaseCity(combatid);
	}
	
	void syncCitys()
	{
		citys.syncCitys();
	}
	
//	public synchronized List<Integer> getBaseCitysSyncInfo()
//	{
//		return citys.getBaseCitysSyncInfo();
//	}
	
	public synchronized List<CityManager.CityBrief> getBaseCitysBrief()
	{
		return citys.getBaseCitysBrief();
	}
	
	public synchronized List<CityManager.CityBrief> getOccupiedCitysBrief()
	{
		return citys.getOccupiedCitysBrief();
	}
	
	public synchronized List<Short> getCitysGids()
	{
		List<Short> gids = new ArrayList<Short>();
		List<CityManager.CityBrief> brief = citys.getBaseCitysBrief();
		for (CityManager.CityBrief cb : brief)
			for (short gid : cb.gids)
				gids.add(gid);
		
		brief = citys.getOccupiedCitysBrief();
		for (CityManager.CityBrief cb : brief)
			for (short gid : cb.gids)
				gids.add(gid);
		
		return gids;
	}
	
	public synchronized boolean removeCitysGid(short gid)
	{
		List<CityManager.CityBrief> brief = citys.getBaseCitysBrief();
		CityManager.CityBrief br = null;
		for (CityManager.CityBrief cb : brief) {
			for (short g : cb.gids)
				if (gid == g) {
					br = cb;
					break;
				}
			
			if (br != null)
				break;
		}
		
		if (br != null) {
			List<Short> newGids = new ArrayList<Short>();
			for (short g : br.gids)
				if (g != gid)
					newGids.add(g);
			
			if (newGids.isEmpty())
				return false;
			
			return citys.guardBaseCity(br.cityID, 0, newGids, br.pids) == CityManager.INVOKE_SUCCESS;
		}
		
		brief = citys.getOccupiedCitysBrief();
		for (CityManager.CityBrief cb : brief) {
			for (short g : cb.gids)
				if (gid == g) {
					br = cb;
					break;
				}
			
			if (br != null)
				break;
		}
		
		if (br != null) {
			List<Short> newGids = new ArrayList<Short>();
			for (short g : br.gids)
				if (g != gid)
					newGids.add(g);
			
			if (newGids.isEmpty())
				return false;
			
			return citys.guardOccpiedCity(br.baseGSID, br.baseRoleID, br.cityID, newGids, br.pids) == CityManager.INVOKE_SUCCESS;
		}
		
		return false;
	}
	
	public synchronized CityManager.InvokeRes<SBean.CityInfo> getBaseCityInfo(int cityId)
	{
		return citys.getBaseCityInfo(cityId);
	}
	
	public synchronized CityManager.InvokeRes<SBean.CityInfo> getOccupiedCityInfo(int baseGSID, int baseRoleID, int cityID)
	{
		return citys.getOccupiedCityInfo(baseGSID, baseRoleID, cityID);
	}
	
	public int guardBaseCity(int cityId, int guardLifeTimeType, List<Short> gids, List<Short> pids)
	{
		int ret = CityManager.INVOKE_UNKNOWN_ERROR;
		synchronized (this)
		{
			ret = citys.guardBaseCity(cityId, guardLifeTimeType, gids, pids);
		}
		if (ret == CityManager.INVOKE_SUCCESS)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CityWarRecord record = new TLogger.CityWarRecord(TLog.CITYWAREVENT_GUARD);
			record.setArgs(guardLifeTimeType, 0, 0, cityId);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		return ret;
	}
	
	public synchronized int guardOccpiedCity(int baseGSID, int baseRoleID, int cityId, List<Short> gids, List<Short> pids)
	{
		int ret = CityManager.INVOKE_UNKNOWN_ERROR;
		synchronized (this)
		{
			citys.syncCitys();
			if (citys.getGuardOccupiedCityCount() >= GameData.getInstance().getOccupyCityMaxCount(this.lvlVIP))
				return ret;
			
			ret = citys.guardOccpiedCity(baseGSID, baseRoleID, cityId, gids, pids);
		}
		if (ret == CityManager.INVOKE_SUCCESS)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CityWarRecord record = new TLogger.CityWarRecord(TLog.CITYWAREVENT_GUARD);
			record.setArgs(-1, baseGSID, baseRoleID, cityId);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		return ret;
	}
	
	public synchronized int guardRobotCity(String baseRoleName, int cityid, int guardStartTime, int guardLifeTime, List<Short> gids, List<Short> pids)
	{
		int ret = CityManager.INVOKE_UNKNOWN_ERROR;
		synchronized (this)
		{
			citys.syncCitys();
			if (citys.getGuardOccupiedCityCount() >= GameData.getInstance().getOccupyCityMaxCount(this.lvlVIP))
				return ret;
			
			ret = citys.guardRobotCity(baseRoleName, cityid, guardStartTime, guardLifeTime, gids, pids);
		}
		if (ret == CityManager.INVOKE_SUCCESS)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CityWarRecord record = new TLogger.CityWarRecord(TLog.CITYWAREVENT_GUARD);
			record.setArgs(-1, -1, -1, cityid);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		return ret;
	}
	
	public synchronized boolean canTakeOccupyCitysReward()
	{
		return citys.canTakeOccupyCitysReward();
	}
	
	public CityManager.CityReward takeBaseCityReward(int cityID)
	{
		return citys.takeBaseCityReward(cityID);
	}
	
	public CityManager.CityReward takeOccupyCityReward(int baseGSID, int baseRoleID, int cityID, int guardStartTime)
	{
		return citys.takeOccupyCityReward(baseGSID, baseRoleID, cityID, guardStartTime);
	}
	
	public String OneKeyCityReward(List<Integer> selfCities, List<String> otherCities) 
	{
	    StringBuilder sb = new StringBuilder();
	    sb.append(selfCities.size()).append(";");
	    for (int cityID : selfCities) {
	        sb.append(cityID).append(";");
	        CityManager.CityReward reward = citys.takeBaseCityReward(cityID);
	        sb.append(reward.rewards.size()).append(';');
	        for (SBean.DropEntryNew e : reward.rewards)
	        {
	            sb.append(e.type).append(';');
	            sb.append(e.id).append(';');
	            sb.append(e.arg).append(';');
	        }
	        sb.append(reward.nTimesStone).append(';');
	        sb.append(reward.nTimesMoney).append(';');
	        sb.append(reward.nTimesItem).append(';');
        }
	    sb.append(otherCities.size()).append(";");
	    for (String string : otherCities) {
	        String[] city = string.split(",");
	        int baseGSID = Integer.parseInt(city[0]);
	        int baseRoleID = Integer.parseInt(city[1]);
	        int cityID = Integer.parseInt(city[2]);
	        int guardStartTime = Integer.parseInt(city[3]);
	        CityManager.CityReward reward = citys.takeOccupyCityReward(baseGSID, baseRoleID, cityID, guardStartTime);
	        sb.append(baseGSID).append(";");
	        sb.append(baseRoleID).append(";");
	        sb.append(cityID).append(";");
	        sb.append(reward.rewards.size()).append(';');
	        for (SBean.DropEntryNew e : reward.rewards)
	        {
	            sb.append(e.type).append(';');
	            sb.append(e.id).append(';');
	            sb.append(e.arg).append(';');
	        }
	        sb.append(reward.nTimesStone).append(';');
	        sb.append(reward.nTimesMoney).append(';');
	        sb.append(reward.nTimesItem).append(';');
        }
	    return sb.toString();
	}
	
	public synchronized CityManager.CityRewardLog getOccupyCitysRewardLog()
	{
		return citys.getOccupyCitysRewardLog();
	}
	
	public void searchCity()
	{
		boolean canSearch = false;
		boolean maxOccupy = false;
		SBean.CaptureCityCFGS ccfg = GameData.getInstance().getCaptureCityCfg();
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			citys.syncCitys();
			if (citys.getOccupiedCityCount() < GameData.getInstance().getOccupyCityMaxCount(this.lvlVIP))
			{
				if (this.money > ccfg.searchCostMoney) {
					commonFlowRecord.addChange(this.useMoney(ccfg.searchCostMoney));
					canSearch = true;
				}
			}
			else
				maxOccupy = true;
		}
		final SBean.SearchCityReq req = new SBean.SearchCityReq(0, 0, new SBean.GlobalRoleID(gs.getConfig().id, this.id), this.lvl, citys.getBaseCityMax(), citys.getCurSearchType());
		gs.getLogger().debug("searchCity req : " + CityManager.toBriefString(req));
		if (canSearch)
		{
			gs.getCityManager().citySearchedByOther(req.roleID.serverID, req.roleID.roleID, req.level, req.cityIDMax, req.searchType, new CityManager.CitySearchedCallback()
				{
					public void onCallback(int error, SBean.CityInfo cinfo)
					{
						if (cinfo == null)
						{
							cinfo = CityManager.makeCityInfo();
							if (error == CityManager.INVOKE_SUCCESS)
								error = CityManager.INVOKE_UNKNOWN_ERROR;
						}
						if (error == CityManager.INVOKE_SUCCESS)
						{
							int r = gs.gameData.getRandInt(0, 100);
							if (r < 30) {
								error = CityManager.INVOKE_NOT_FIND_CITY_ERROR;
								cinfo = CityManager.makeCityInfo();
							}
							
							SBean.SearchCityRes res = CityManager.makeResponse(error, cinfo, req);
							gs.getLogger().debug("search city in self server success res : " + CityManager.toBriefString(res));
							gs.getRPCManager().notifySearchCityResult(netsid, res);
						}	
						else
						{
							gs.getLogger().debug("search city in self server failed error=" + error + ", try search from other server...");
							gs.getCityManager().searchCity(req);
						}
					}
				});
			
			commonFlowRecord.setEvent(TLog.AT_CITY_SEARCH);
			commonFlowRecord.setArg(0);
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(new TLogger.CityWarRecord(TLog.CITYWAREVENT_SEARCH));
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		else
		{
			if (maxOccupy)
				gs.getRPCManager().notifySearchCityResult(netsid, CityManager.makeResponse(CityManager.INVOKE_SEARCH_MAX_CITY_ERROR, CityManager.makeCityInfo(), req));
			else
				gs.getRPCManager().notifySearchCityResult(netsid, CityManager.makeResponse(CityManager.INVOKE_SEARCH_NOT_ENOUGH_MOENY_ERROR, CityManager.makeCityInfo(), req));
		}
	}
	
	public void attackCityStart(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid)
	{
		boolean canAttack = false;
		int selfgsid = gs.getConfig().id;
		synchronized (this)
		{
			if (baseGSID != selfgsid || baseRoleID != this.id)
			{
				citys.syncCitys();
				if (citys.getOccupiedCityCount() < GameData.getInstance().getOccupyCityMaxCount(this.lvlVIP))
					canAttack = true;
			}
			else
			{
				canAttack = true;
			}
		}
		final SBean.AttackCityStartReq req = new SBean.AttackCityStartReq();
		req.roleID = new SBean.GlobalRoleID(selfgsid, this.id);
		req.targetRoleID = new SBean.GlobalRoleID(gsid, roleid);
		req.baseRoleID = new SBean.GlobalRoleID(baseGSID, baseRoleID);
		req.cityID = cityid;
		gs.getLogger().debug("attackCityStart req : " + CityManager.toBriefString(req));
		if (canAttack)
		{
			if (gsid == selfgsid)
			{
				gs.getCityManager().cityAttackedStartByOther(req.roleID.serverID, req.roleID.roleID, req.targetRoleID.roleID, 
						req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, new CityManager.CityAttackedStartCallback()
					{
						public void onCallback(int errcode, SBean.CityInfo info)
						{
							SBean.AttackCityStartRes res = CityManager.makeResponse(errcode, info, req);
							handleAttackCityStartResult(res);
						}
					});
			}
			else
			{
				gs.getCityManager().attackCityStart(req);	
			}	
		}
		else
		{
			gs.getRPCManager().notifyAttackCityStartResult(netsid, CityManager.makeResponse(CityManager.INVOKE_ATTACK_OTHER_MAX_ERROR, CityManager.makeCityInfo(), req));
		}
		
	}
	
	public void attackCityEnd(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid, byte win)
	{
		int selfgsid = gs.getConfig().id;
		final SBean.AttackCityEndReq req = new SBean.AttackCityEndReq();
		req.roleID = new SBean.GlobalRoleID(selfgsid, this.id);
		req.roleName = this.name;
		req.targetRoleID = new SBean.GlobalRoleID(gsid, roleid);
		req.baseRoleID = new SBean.GlobalRoleID(baseGSID, baseRoleID);
		req.cityID = cityid;
		req.win = win;
		gs.getLogger().debug("attackCityEnd req : " + CityManager.toBriefString(req));
		if (!this.citys.checkCurAttackCity(gsid, roleid, baseGSID, baseRoleID, cityid))
		{
			gs.getRPCManager().notifyAttackCityEndResult(netsid, CityManager.makeResponse(CityManager.INVOKE_CITY_NOT_ATTACKED_START_BY_SELF_ERROR, CityManager.makeCityInfo(), req));
			return;
		}
		if (gsid == selfgsid)
		{
			gs.getCityManager().cityAttackedEndByOther(req.roleID.serverID, req.roleID.roleID, req.roleName, req.targetRoleID.roleID, 
					req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, req.win, new CityManager.CityAttackedEndCallback() 
			{
				public void onCallback(int errcode, SBean.CityInfo info) 
				{
					SBean.AttackCityEndRes res = CityManager.makeResponse(errcode, info, req);
					handleAttackCityEndResult(res);
				}
			});
		}
		else
		{
			gs.getCityManager().attackCityEnd(req);
		}
	}
	
	public void queryCityOwner(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid)
	{
		int selfgsid = gs.getConfig().id;
		final SBean.QueryCityOwnerReq req = new SBean.QueryCityOwnerReq();
		req.roleID = new SBean.GlobalRoleID(selfgsid, this.id);
		req.targetRoleID = new SBean.GlobalRoleID(gsid, roleid);
		req.baseRoleID = new SBean.GlobalRoleID(baseGSID, baseRoleID);
		req.cityID = cityid;
		gs.getLogger().debug("queryCityOwner req : " + CityManager.toBriefString(req));
		if (gsid == selfgsid)
		{
			gs.getCityManager().cityOwnerQueriedByOther(req.roleID.serverID, req.roleID.roleID, req.targetRoleID.roleID, 
					req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID,new CityManager.CityOwnerQueriedCallback() 
			{
				public void onCallback(int errcode, CityInfo cinfo) 
				{		
					if (cinfo == null)
					{
						cinfo = CityManager.makeCityInfo();
					}
					SBean.QueryCityOwnerRes res = CityManager.makeResponse(errcode, cinfo, req);
					handleQueryCityOwnerResult(res);
				}
			});
		}
		else
		{
			gs.getCityManager().queryCityOwner(req);
		}
	}
	
	public void handleSearchCityResult(SBean.SearchCityRes res)
	{
		int r = gs.gameData.getRandInt(0, 100);
		if (r < 30) {
			res.error = CityManager.INVOKE_NOT_FIND_CITY_ERROR;
			res.cityInfo = CityManager.makeCityInfo();
		}
		
		if (res.error != CityManager.INVOKE_SUCCESS)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			SBean.CaptureCityCFGS ccfg = GameData.getInstance().getCaptureCityCfg();
			synchronized (this)
			{
				commonFlowRecord.addChange(addMoney(ccfg.searchCostMoney));
			}
			commonFlowRecord.setEvent(TLog.AT_CITY_SEARCH_FAILED_RETURN);
			commonFlowRecord.setArg(0);
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		citys.onHandleSearchCityResponse(res.error == CityManager.INVOKE_SUCCESS);
		gs.getRPCManager().notifySearchCityResult(netsid, res);
	}

	public void handleAttackCityStartResult(SBean.AttackCityStartRes res)
	{
		citys.onHandleAttackCityStartResponse(res.error == CityManager.INVOKE_SUCCESS, res.cityInfo);
		gs.getRPCManager().notifyAttackCityStartResult(netsid, res);
	}
	
	public void handleAttackCityEndResult(SBean.AttackCityEndRes res)
	{
		citys.onHandleAttackCityEndResponse(res.error == CityManager.INVOKE_SUCCESS, res.cityInfo);
		gs.getRPCManager().notifyAttackCityEndResult(netsid, res);
		if (res.error == CityManager.INVOKE_SUCCESS)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CityWarRecord record = new TLogger.CityWarRecord(TLog.CITYWAREVENT_ATTACK);
			record.setArgs(res.cityInfo.baseRoleID.serverID, res.cityInfo.baseRoleID.roleID, res.cityInfo.cityID, (res.cityInfo.ownerRoleID.serverID == res.roleID.serverID && res.cityInfo.ownerRoleID.roleID == res.roleID.roleID) ? 1 : 0);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
	}
	
	public void handleQueryCityOwnerResult(SBean.QueryCityOwnerRes res)
	{
		gs.getRPCManager().notifyQueryCityOwnerResult(netsid, res);
	}

///////////////////////////////////////////////////////////////////////////////////////////////
	boolean haveDrop(SBean.DropEntry e)
	{
		boolean ret = false;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				if( GameData.getInstance().getItemCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getItemCount(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				if( GameData.getInstance().getEquipCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getEquipCount(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				if( GameData.getInstance().getGeneralCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getGeneralEvo(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				ret = this.money >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				ret = this.stone >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_ARENA_POINT:
			{
				ret = this.arenaState.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_MARCH_POINT:
			{
				ret = this.marchState.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_FORCE_POINT:
			{
				ret = this.forceInfo.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_RICH_POINT:
			{
				ret = this.rich == null ? false : this.rich.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_SUPER_ARENA_POINT:
			{
				ret = this.superArena == null ? false : this.superArena.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_DUEL_POINT:
			{
				ret = this.duel == null ? false : this.duel.point >= e.arg;
			}
			break;
		default:
			break;
		}
		return ret;
	}
	
	private TLogger.CommonItemChange addDrop(SBean.DropEntry e)
	{
		TLogger.CommonItemChange record = null;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				record = addItem(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				record = addEquip(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				record = addGeneral(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				record = addMoney(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				record = addStonePresent(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_ARENA_POINT:
			{
				record = addArena(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_MARCH_POINT:
			{
				record = addMarch(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_FORCE_POINT:
			{
				record = addForce(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_RICH_POINT:
			{
				record = addRich(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_RICH_GOLD:
			{
				record = richAddGold(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_SUPER_ARENA_POINT:
			{
				record = addSuperArena(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_DUEL_POINT:
			{
				addDuelPoint(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_FORCE_CUP:
			{
				gs.getForceManager().addCup(forceInfo.id, e.arg);
			}
			break;
		default:
			break;
		}
		return record;
	}
	
	private TLogger.CommonItemChange delDrop(SBean.DropEntry e)
	{
		TLogger.CommonItemChange record = null;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				record = delItem(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				record = delEquip(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				record = null;
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				record = useMoney(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				record = useStone(e.arg);
			}
			break;
		default:
			break;
		}
		return record;
	}
	
	private TLogger.CommonItemChange delDropNew(SBean.DropEntryNew e)
	{
		TLogger.CommonItemChange record = null;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				record = delItem(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				record = delEquip(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				record = null;
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				record = useMoney(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				record = useStone(e.arg);
			}
			break;
		default:
			break;
		}
		return record;
	}
	
	TLogger.CommonItemChange addDropNew(SBean.DropEntryNew e)
	{
		TLogger.CommonItemChange record = null;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				record = addItem(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				record = addEquip(e.id, e.arg);
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				record = addGeneral(e.id, (byte)e.arg);
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				record = addMoney(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				record = addStonePresent(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_ARENA_POINT:
			{
				record = addArena(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_MARCH_POINT:
			{
				record = addMarch(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_FORCE_POINT:
			{
				record = addForce(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_RICH_POINT:
			{
				record = addRich(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_RICH_GOLD:
			{
				record = richAddGold(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_SUPER_ARENA_POINT:
			{
				record = addSuperArena(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_DUEL_POINT:
			{
				addDuelPoint(e.arg);
			}
			break;
		case GameData.COMMON_TYPE_FORCE_CUP:
			{
				gs.getForceManager().addCup(forceInfo.id, e.arg);
			}
			break;
		default:
			break;
		}
		return record;
	}
	
	boolean haveDropNew(SBean.DropEntryNew e)
	{
		boolean ret = false;
		switch( e.type )
		{
		case GameData.COMMON_TYPE_ITEM:
			{
				if( GameData.getInstance().getItemCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getItemCount(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_EQUIP:
			{
				if( GameData.getInstance().getEquipCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getEquipCount(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_GENERAL:
			{
				if( GameData.getInstance().getGeneralCFG(e.id) != null )
				{
					ret = ( e.arg > 0 && getGeneralEvo(e.id) >= e.arg);
				}
			}
			break;
		case GameData.COMMON_TYPE_MONEY:
			{
				ret = this.money >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_STONE:
			{
				ret = this.stone >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_ARENA_POINT:
			{
				ret = this.arenaState.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_MARCH_POINT:
			{
				ret = this.marchState.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_FORCE_POINT:
			{
				ret = this.forceInfo.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_RICH_POINT:
			{
				ret = this.rich == null ? false : this.rich.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_SUPER_ARENA_POINT:
			{
				ret = this.superArena == null ? false : this.superArena.point >= e.arg;
			}
			break;
		case GameData.COMMON_TYPE_DUEL_POINT:
			{
				ret = this.duel == null ? false : this.duel.point >= e.arg;
			}
			break;
		default:
			break;
		}
		return ret;
	}
	
	boolean haveDropsNew(List<SBean.DropEntryNew> lst)
	{
		for(SBean.DropEntryNew e : lst)
		{
			if (!haveDropNew(e))
				return false;
		}
		return true;
	}
	
	boolean haveGeneral(short gid)
	{
		return ( GameData.getInstance().getGeneralCFG(gid) != null ) &&  (generals.get(gid) != null);
	}
	
	boolean haveGenerals(List<Short> gids)
	{
		for(short gid : gids)
		{
			if (!haveGeneral(gid))
				return false;
		}
		return true;
	}
	
	List<TLogger.CommonItemChange> addDrops(List<SBean.DropEntry> lst)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		for(SBean.DropEntry e : lst)
		{
			TLogger.CommonItemChange record = addDrop(e);
			if (record != null)
				records.add(record);
		}
		return records;
	}
	
	List<TLogger.CommonItemChange> addDropsNew(List<SBean.DropEntryNew> lst)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		for(SBean.DropEntryNew e : lst)
		{
			TLogger.CommonItemChange record = addDropNew(e);
			if (record != null)
				records.add(record);
		}
		return records;
	}
	
	List<TLogger.CommonItemChange> delDropsNew(List<SBean.DropEntryNew> lst)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		for(SBean.DropEntryNew e : lst)
		{
			TLogger.CommonItemChange record = delDropNew(e);
			if (record != null)
				records.add(record);
		}
		return records;
	}
	
	public boolean equipsSell(short equipID, short cnt, short cntNow)
	{
		SBean.EquipCFGS cfg = gs.getGameData().getEquipCFG(equipID);
		if( cfg == null )
			return false;				
		if( cfg.price <= 0 )
			return false;
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			short countNow = getEquipCount(equipID);
			if( countNow != cntNow )
				return false;
			if( countNow < cnt )
				return false;
			
			commonFlowRecord.addChange(delEquip(equipID, cnt));
			commonFlowRecord.addChange(addMoney(cfg.price * cnt));
		}
		commonFlowRecord.setEvent(TLog.AT_EQUIP_SELL);
		commonFlowRecord.setArg(equipID, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean equipsCombine(short equipID)
	{
		SBean.EquipCFGS cfg = gs.getGameData().getEquipCFG(equipID);
		if( cfg == null || cfg.combineMoney < 0 || cfg.combinePieces.isEmpty() )
			return false;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( money < cfg.combineMoney )
				return false;
			for(SBean.DropEntry e : cfg.combinePieces)
			{
				switch( e.type )
				{
				case GameData.COMMON_TYPE_ITEM:
					{	
						if( GameData.getInstance().getItemCFG(e.id) == null )
							return false;
						if( e.arg <= 0 )
							return false;
						if( getItemCount(e.id) < e.arg )
							return false;
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{	
						if( e.id == equipID || GameData.getInstance().getEquipCFG(e.id) == null )
							return false;
						if( e.arg <= 0 )
							return false;
						if( getEquipCount(e.id) < e.arg )
							return false;
					}
					break;
				default:
					return false;
				}
			}
			commonFlowRecord.addChange(useMoney(cfg.combineMoney));
			for(SBean.DropEntry e : cfg.combinePieces)
			{
				switch( e.type )
				{
				case GameData.COMMON_TYPE_ITEM:
					{	
						commonFlowRecord.addChange(delItem(e.id, e.arg));
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{	
						commonFlowRecord.addChange(delEquip(e.id, e.arg));
					}
					break;
				default:
					break;
				}
			}
			commonFlowRecord.addChange(addEquip(equipID, 1));
		}
		commonFlowRecord.setEvent(TLog.AT_EQUIP_COMBINE);
		commonFlowRecord.setArg(equipID, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean itemsSell(short itemID, int cnt, int cntNow)
	{
		SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
		if( cfg == null )
			return false;				
		if( cfg.price <= 0 )
			return false;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int countNow = getItemCount(itemID); 
			if( cntNow != countNow )
				return false;
			if( countNow < cnt )
				return false;
			
			commonFlowRecord.addChange(delItem(itemID, cnt));
			commonFlowRecord.addChange(addMoney(cfg.price * cnt));
		}
		commonFlowRecord.setEvent(TLog.AT_ITEM_SELL);
		commonFlowRecord.setArg(itemID, 0);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public int itemsSellAll()
	{
		List<TLogger.TLogEvent> tlogs = new ArrayList<TLogger.TLogEvent>();
		int moneyAll = 0;
		synchronized( this )
		{
			List<Map.Entry<Short, Integer>> lst = new ArrayList<Map.Entry<Short, Integer>>();
			for(Entry<Short, Integer> e : items.entrySet())
			{
				short itemID = e.getKey().shortValue();
				SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
				if( cfg == null || cfg.price <= 0 || e.getValue().intValue() <= 0 || cfg.type != GameData.ITEM_TYPE_MONEY )
					continue;				
				lst.add(e);
			}
			for(Entry<Short, Integer> e : lst)
			{
				short itemID = e.getKey().shortValue();
				short cnt = e.getValue().shortValue();
				int price = GameData.getInstance().getItemCFG(itemID).price * cnt;
				moneyAll += price;
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				commonFlowRecord.addChange(delItem(itemID, cnt));
				commonFlowRecord.addChange(addMoney(price));
				commonFlowRecord.setEvent(TLog.AT_ITEM_SELL);
				commonFlowRecord.setArg(itemID, 0);
				tlogEvent.addCommonFlow(commonFlowRecord);
				tlogs.add(tlogEvent);
			}
		}
		for(TLogger.TLogEvent l : tlogs)
		{
			gs.getTLogger().logEvent(this, l);
		}
		return moneyAll;
	}
	
	public List<List<DropEntry>> itemsGamble(short itemID, int icount)
	{
		SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
		if( cfg == null || cfg.type != GameData.ITEM_TYPE_GAMBLE )
			return null;
		
		synchronized( this )
		{
			if( lvl < cfg.lvlReq )
				return null;
			if( getItemCount(itemID) < icount )
				return null;
		}	
			
		List<List<SBean.DropEntry>> drops1 = new ArrayList<List<SBean.DropEntry>>();
		for (int i=0;i<icount;i++){
			List<SBean.DropEntry> drops = new ArrayList<SBean.DropEntry>();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			boolean bIPad = false;
			short ipadID = 0;
			synchronized( this )
			{
//				if( lvl < cfg.lvlReq )
//					return null;
//				if( getItemCount(itemID) < 1 )
//					return null;
				
				if( cfg.arg2 == 1 )
				{
					SBean.DropEntry drop = gs.getIPad().testDrop(itemID);
					if( drop == null )
						drop = GameData.getInstance().getDropTableRandomEntry((short)cfg.arg1);
					else
					{
						bIPad = true;
						ipadID = drop.id;
					}
					if( drop == null )
						return null;
					drops.add(drop);
				}
				else if( cfg.arg2 == 2 )
				{
					drops.addAll(GameData.getInstance().getDropTableList((short)cfg.arg1));
				}
				
				commonFlowRecord.addChange(delItem(itemID, 1));
				commonFlowRecord.addChanges(addDrops(drops));
				drops1.add(drops);
			}
			if( bIPad )
				gs.getLogger().warn("ipad! rid=" + this.id + ", item=" + ipadID + ", userName=" + userName);
			commonFlowRecord.setEvent(TLog.AT_ITEM_USE);
			commonFlowRecord.setArg(itemID, 0);
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		
		
		return drops1;
	}
	
	public short itemsUse(short itemID, short cnt, short gid, int[] dataRet)
	{
		SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
		if( cfg == null )
			return 0;
		short rcnt = 0;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( lvl < cfg.lvlReq )
				return 0;
			if( getItemCount(itemID) < cnt || cnt <= 0 )
				return 0;
			switch( cfg.type )
			{
			case GameData.ITEM_TYPE_STONE:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					commonFlowRecord.addChange(addStonePresent(add * cnt));
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_VIT:
				{
					int add = cfg.arg1;
					if( add <= 0 || cnt != 1 )
						return 0;
					TLogger.CommonItemChange rc = addVit2(add * cnt);
					if( rc == null )
						return 0;
					commonFlowRecord.addChange(rc);
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_EXP:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					do 
					{
						List<TLogger.CommonItemChange> changes = addExp(add, tlogEvent);
						if (!changes.isEmpty())
							commonFlowRecord.addChanges(changes);
						else
							break;
					} while (++rcnt < cnt);
				}
				break;
			case GameData.ITEM_TYPE_GENERAL_EXP:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
										
					General g = generals.get(gid);
					if( g == null )
						return 0;
					int gLvlBefore = g.lvl;
					while( g.addExp(add) )
					{
						if( ++rcnt >= cnt )
						{
							if (g.lvl > gLvlBefore)
							{
								TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, gLvlBefore, g.evoLvl, g.advLvl);
								grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_LEVELUP, g.lvl, g.evoLvl, g.advLvl);
								tlogEvent.addRecord(grecord);
							}
							break;
						}
					}
				}
				break;
			case GameData.ITEM_TYPE_PET_EXP:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
									
					Pet p = pets.get(gid);
					if( p == null )
						return 0;
					while( p.addExp(add) )
					{
						if( ++rcnt >= cnt )
							break;
					}
				}
				break;
			case GameData.ITEM_TYPE_PET_EGG:
				{
					if( cnt != 1 )
						return 0;
					short petID = (short)cfg.arg1;
					SBean.PetCFGS pcfg = GameData.getInstance().getPetCFG(petID, false);
					if( pcfg == null )
						return 0;
					rcnt = 1;
					//TODO
					short growthRate = GameData.getInstance().getRandomGrowthRate(pcfg);
					if( dataRet[0] > 0 )
						growthRate = (short)dataRet[0];
					dataRet[0] = growthRate;
					commonFlowRecord.addChange(addPet(petID, growthRate));
				}
				break;
			case GameData.ITEM_TYPE_UPGRADE_LEVEL:
				{
					int upLvl = cfg.arg1;
					int useLvlReq = cfg.arg2;
					if (upLvl <= 0)
						return 0;
					if (this.lvl >= useLvlReq)
						return 0;
					
					int levellimit = gs.getRoleLevelLimit();
					//while (this.lvl < gs.getRoleLevelLimit())
					while ( this.lvl < levellimit )
					{
						this.lvl += upLvl;
						//if (this.lvl >= gs.getRoleLevelLimit())
						if (this.lvl >= levellimit)
						{
							//this.lvl = gs.getRoleLevelLimit();
							this.lvl = (short)levellimit;
							this.exp = 0;
						}
						if (++rcnt >= cnt)
						{
							break;
						}
						TLogger.LevelUpRecord lur = new TLogger.LevelUpRecord(this.lvl);
						lur.setLastLevelUpTime(this.upgradeTime);
						this.upgradeTime = gs.getTime();
						lur.setCurLevelUpTime(this.upgradeTime);
						tlogEvent.addRecord(lur);
					}
					
				}
				break;
			case GameData.ITEM_TYPE_UPGRADE_GENERAL_LEVEL:
				{
					int upLvl = cfg.arg1;
					General g = generals.get(gid);
					if( g == null )
						return 0;
					int gLvlBefore = g.lvl;
					while (g.upgrade(upLvl))
					{
						if( ++rcnt >= cnt )
						{
							break;
						}
					} 
					TLogger.GeneralEvolutionRecord grecord = new TLogger.GeneralEvolutionRecord(g.id, gLvlBefore, g.evoLvl, g.advLvl);
					grecord.setGeneralEvoInfo(TLog.GENERALEVOLUTION_LEVELUP, g.lvl, g.evoLvl, g.advLvl);
					tlogEvent.addRecord(grecord);
				}
				break;
			case GameData.ITEM_TYPE_FORCE_ACTIVITY:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					activity += add* cnt;
					gs.getForceManager().addActivity(forceInfo.id, add * cnt);
					rcnt = cnt;
					gs.getForceManager().sendForceActivityLog(forceInfo.id, name, itemID);
				}
				break;
			case GameData.ITEM_TYPE_GENERAL_SEYEN:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					int seyenAdd = add;
					commonFlowRecord.addChange(addSeyenExp(seyenAdd* cnt));
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_FORCE_CUP:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					gs.getForceManager().addCup(forceInfo.id, add * cnt);
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_VIP_EXP:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					if (getStonePayTotal() >= gs.gameData.getVIPStoneMax())
						return 0;
					addPayVipExp(add * cnt);
					rcnt = cnt;
					dataRet[1] = GameData.ITEM_TYPE_VIP_EXP;
				}
				break;
			case GameData.ITEM_TYPE_SWAR_PROTECT:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					if (!gs.getSWarManager().addProtectDays(forceInfo.id, add * cnt))
						return 0;
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_ARENA_POINT:
				{
					int add = cfg.arg1;
					if( add <= 0 )
						return 0;
					commonFlowRecord.addChange(addArena(add * cnt));
					rcnt = cnt;
				}
				break;
			case GameData.ITEM_TYPE_HEADICON:
			{
				int add = cfg.arg1;
				if( add <= 0 )
					return 0;
				if(addheadicon((short)add)<0){
					return 0;
				}
				rcnt = cnt;
			}
			break;	
			case GameData.ITEM_TYPE_BESTOW:
			{
//				int add = cfg.arg1;
//				if( add <= 0 )
//					return 0;  //(short)add)<0
				dataRet[0] =useBestowItem2Weopen(cfg.id,cnt);
				if(dataRet[0]<0){
					return 0;
				}
				rcnt = cnt;
			}
			break;	
			case GameData.ITEM_TYPE_BESTOW_ALL:
			{
//				int add = cfg.arg1;
//				if( add <= 0 )
//					return 0;  //(short)add)<0
				dataRet[0] =useBestowItem();
				if(dataRet[0]<0){
					return 0;
				}
				rcnt = cnt;
			}
			break;	
			default:
				return 0;
			}
			if( rcnt > 0 )
			{
				commonFlowRecord.addChange(delItem(itemID, rcnt));
			}
		}
		commonFlowRecord.setEvent(TLog.AT_ITEM_USE);
		commonFlowRecord.setArg(itemID, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		
		gs.getTLogger().logEvent(this, tlogEvent);
		return rcnt;
	}
	
	public boolean itemsCombine(short itemID)
	{
		SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
		if( cfg == null )
			return false;
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		if( cfg.type == GameData.ITEM_TYPE_SCROLL_PIECE ) // TODO
		{
			short tid = (short)cfg.arg1;
			if( tid == itemID || GameData.getInstance().getItemCFG(tid) == null )
				return false;
			int nc = cfg.arg2;
			if( nc <= 0 )
				return false;
			int m = cfg.arg3;
			if( m < 0 )
				return false;
			synchronized( this )
			{
				Integer n = items.get(itemID);
				if( n == null )
					return false;
				if( n.shortValue() < nc )
					return false;
				if( money < m )
					return false;
				commonFlowRecord.addChange(useMoney(m));
				commonFlowRecord.addChange(delItem(itemID, nc));
				commonFlowRecord.addChange(addItem(tid, (short)1));
			}
		}
		else if( cfg.type == GameData.ITEM_TYPE_EQUIP_PIECE ) // TODO
		{
			short tid = (short)cfg.arg1;
			if( GameData.getInstance().getEquipCFG(tid) == null )
				return false;
			int nc = cfg.arg2;
			if( nc <= 0 )
				return false;
			int m = cfg.arg3;
			if( m < 0 )
				return false;
			synchronized( this )
			{
				Integer n = items.get(itemID);
				if( n == null )
					return false;
				if( n.shortValue() < nc )
					return false;
				if( money < m )
					return false;
				commonFlowRecord.addChange(useMoney(m));
				commonFlowRecord.addChange(delItem(itemID, nc));
				commonFlowRecord.addChange(addEquip(tid, (short)1));
			}
		}
		else
			return false;
		commonFlowRecord.setEvent(TLog.AT_ITEM_COMBINE);
		commonFlowRecord.setArg(itemID, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
//	private int getCombatStar3Size(byte ctype, byte ccid)
//	{
//		int n = 0;
//		if( ctype >= 0 && ctype < combatScores.size() )
//		{
//			SBean.DBCombatScore cs = combatScores.get(ctype);
//			if( ccid >= 0 && ccid < cs.scores.size() )
//			{
//				SBean.DBCombatTypeScore ts = cs.scores.get(ccid);
//				for(byte s : ts.score)
//				{
//					if( s >= 3 )
//						++n;
//				}
//			}
//		}
//		return n;
//	}
	
	public void marchSyncInfo()
	{		
		final MarchStateInfo info = new MarchStateInfo();
		synchronized( this )
		{
			info.enemies = marchCurrentEnemies;
			if (gs.getMarchManager().checkPowerRoleRank(id))
				setPowerRank();
		}
		final List<Integer> rids = gs.getMarchManager().getRoleInfo(id, info);
		if( lvl < gs.getGameData().getMarchCFG().lvlReq )
			return;
		int infoMaxLvl = gs.getArenaManager().getMaxLevel(lvl);
		synchronized( this )
		{
			dayRefresh();
			info.maxLvl = infoMaxLvl;
			info.stage = marchState.stage;
			info.rewardTaken = marchState.rewardTaken;
			info.point = marchState.point;
			info.timesUsed = marchState.timesUsed;
			info.generalStates = copyMarchGeneralStatesWithoutLock(false, false);
			info.enemyGeneralStates = copyMarchGeneralStatesWithoutLock(true, false);
			marchCurrentEnemies = info.enemies;
		}
		// sync march state info
		if( rids.isEmpty() )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchSyncInfo(info));
			return;
		}
		gs.getLoginManager().getMarchEnemies(rids, new LoginManager.GetMarchEnemiesCallback() {
			
			@Override
			public void onCallback(List<SBean.DBRoleMarchEnemy> lst)
			{
				Map<Integer, SBean.DBRoleMarchEnemy> rmap = new TreeMap<Integer, SBean.DBRoleMarchEnemy>();
				if (lst.size() != rids.size())
					gs.getLogger().warn("March enemies count doesn't match!!! lst=" + lst.size() + ", rids=" + rids.size());
				for(SBean.DBRoleMarchEnemy r : lst)
				{
					rmap.put(r.id, r);
				}
				if (lst.size() != rmap.size())
					gs.getLogger().warn("March enemies has duplicated ids!!! lst=" + lst.size() + ", rmap=" + rmap.size());
				for(int i = 0; i < info.enemies.size(); ++i)
				{
					int id = info.enemies.get(i).id;
					if( id > 0 )
					{
						SBean.DBRoleMarchEnemy enemy = rmap.get(id);
						if (enemy == null) {
							gs.getLogger().warn("March enemy id=" + id + "doesn't exist!!!");
							info.enemies.get(i).id = -id;
						}
						else {
							info.enemies.set(i, enemy);
						}
					}
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchSyncInfo(info));
			}
		});
	}
	
	public void marchReward()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.MarchRewardRecord record = new TLogger.MarchRewardRecord(lvl);
		synchronized(this)
		{
			if (marchState.rewardTaken > 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchReward(false, 0, 0, null));
			}
			else {
				marchState.rewardTaken = 1;
				int stage = marchState.stage - 1;
				if (stage >= 0 && stage < gs.getGameData().getMarchCFG().stages.size())
				{
					SBean.MarchStageCFGS cfg = gs.getGameData().getMarchCFG().stages.get(stage);
					int money = cfg.money1*lvl + cfg.money2;
					//short noVipReset = gs.getGameData().getMarchResetTimesMax((byte)0);
					int point = 0;
					if (marchPointReward > 0) //marchState.timesUsed <= noVipReset)
						point = cfg.point;
					short drop = -1;
					for (SBean.MarchDropCFGS d : cfg.drops) {
						if (lvl < d.maxLvl) {
							drop = d.drop;
							break;
						}
					}
					if (drop == -1 && cfg.drops.size() > 0)
						drop = cfg.drops.get(cfg.drops.size() - 1).drop;
					SBean.DropEntry dropEntry = gs.getGameData().getDropTableRandomEntry(drop);
					if (dropEntry != null)
						dropEntry = dropEntry.kdClone();
					if (gs.getGameData().getMarchRewardUp(lvlVIP) > 0) {
						money = (int)(money * 1.5f);
						if (dropEntry != null && (dropEntry.type == GameData.COMMON_TYPE_ITEM || dropEntry.type == GameData.COMMON_TYPE_EQUIP)) {
							int dropCount = (int)(dropEntry.arg * 1.5f);
							if (dropCount > 100)
								dropCount = 100;
							dropEntry.arg = (byte)(dropCount);
						}
					}
					if (dropEntry != null)
						commonFlowRecord.addChange(addDrop(dropEntry));
					commonFlowRecord.addChange(addMoney(money));
					commonFlowRecord.addChange(addMarch(point));
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchReward(true, money, point, dropEntry));
					
					record.setMarchReward(stage+1, point);
					
					commonFlowRecord.setEvent(TLog.AT_MARCH_REWARD);
					commonFlowRecord.setArg(marchState.stage, marchState.point);
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogEvent.addRecord(record);
				}
			}
		}
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void marchReset()
	{
		synchronized(this)
		{
			dayRefresh();
			
			short maxReset = gs.getGameData().getMarchResetTimesMax(lvlVIP);
			if( marchState.timesUsed >= maxReset )
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchReset(false));
			else {
				int newDay = gs.getDayCommon();
				if (newDay > marchPointDay && marchPointReward < 2) // 2 ==> first day
					marchPointReward = 1;
				else
					marchPointReward = 0;
				marchPointDay = newDay;
				marchState.timesUsed ++;
				marchState.stage = 0;
				marchState.rewardTaken = 0;
				marchCurrentEnemies = null;
				marchGeneralStates.clear();
				marchEnemyGeneralStates.clear();
				mercGeneral = null;
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchReset(true));				
			}
		}
	}
	
	
	
	public synchronized void marchSyncRobots(Map<Integer, SBean.DBRoleMarchEnemy> robots)
	{
		for(int i = 0; i < marchCurrentEnemies.size(); ++i)
		{
			SBean.DBRoleMarchEnemy e = marchCurrentEnemies.get(i);
			int id = e.id;
			if( id < 0 )
			{
				SBean.DBRoleMarchEnemy enemy = robots.get(id);
				if (enemy != null && enemy.id == id) {
					marchCurrentEnemies.set(i, enemy);
				}
			}
		}
	}
	
	public void marchWipe()
	{
		boolean succ = true;
		List<MarchWipeDrop> drops = new ArrayList<MarchWipeDrop>();
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.setEvent(TLog.AT_MARCH_SKIP);
		
		TLogger.MarchRecord record = new TLogger.MarchRecord(lvl);
		
		int stage = marchState.stage;
		synchronized( this )
		{
			if (marchState.stage <= 0)
				succ = false;
			
			if (succ) {
				SBean.MarchCFGS cfg = gs.getGameData().getMarchCFG();
				if (getItemCount(cfg.wipeItemId) > 0)
					commonFlowRecord.addChange(delItem(cfg.wipeItemId, 1));
				else
					succ = false;
			}
			
			if (succ) {
				dayRefresh();

				short maxReset = gs.getGameData().getMarchResetTimesMax(lvlVIP);
				if( marchState.timesUsed < maxReset ) {
					int newDay = gs.getDayCommon();
					if (newDay > marchPointDay && marchPointReward < 2) // 2 ==> first day
						marchPointReward = 1;
					else
						marchPointReward = 0;
					marchPointDay = newDay;
					marchState.timesUsed ++;
					tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_MARCH, 1));
				}
				else
					succ = false;
			}

			if (succ) {
				for (int i = 0; i < marchState.stage; i ++) {
					SBean.MarchStageCFGS cfg = gs.getGameData().getMarchCFG().stages.get(i);
					int money = cfg.money1*lvl + cfg.money2;
					int point = 0;
					if (marchPointReward > 0)
						point = cfg.point;
					short drop = -1;
					for (SBean.MarchDropCFGS d : cfg.drops) {
						if (lvl < d.maxLvl) {
							drop = d.drop;
							break;
						}
					}
					if (drop == -1 && cfg.drops.size() > 0)
						drop = cfg.drops.get(cfg.drops.size() - 1).drop;
					SBean.DropEntry dropEntry = gs.getGameData().getDropTableRandomEntry(drop);
					if (dropEntry != null)
						dropEntry = dropEntry.kdClone();
					if (gs.getGameData().getMarchRewardUp(lvlVIP) > 0) {
						money = (int)(money * 1.5f);
						if (dropEntry != null && (dropEntry.type == GameData.COMMON_TYPE_ITEM || dropEntry.type == GameData.COMMON_TYPE_EQUIP)) {
							int dropCount = (int)(dropEntry.arg * 1.5f);
							if (dropCount > 100)
								dropCount = 100;
							dropEntry.arg = (byte)(dropCount);
						}
					}
					if (dropEntry != null)
						commonFlowRecord.addChange(addDrop(dropEntry));
					commonFlowRecord.addChange(addMoney(money));
					commonFlowRecord.addChange(addMarch(point));
					MarchWipeDrop wipeDrop = new MarchWipeDrop();
					wipeDrop.money = money;
					wipeDrop.point = point;
					wipeDrop.drop = dropEntry;
					drops.add(wipeDrop);
				}
			}
		}
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchWipeRes(succ, drops));
		
		record.setMarchInfo(stage+1, 1);
		record.setCostTime(0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void marchFinishAttack(byte stage, byte star, int clientCostTime, int clientEndTime, List<SBean.DBRoleMarchGeneralState> sgeneralStates, List<SBean.DBRoleMarchGeneralState> egeneralStates, int waveCount, int dpsTotal, int clientSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal, boolean useMerc)
	{		
		synchronized(this) 
		{
			if( stage != marchState.stage )
				return;
			
			for (SBean.DBRoleMarchGeneralState s : sgeneralStates) {
				boolean found = false;
				if (useMerc && mercGeneral != null && mercGeneral.general.id == s.id) {
					mercGeneral.status = s.kdClone();
					continue;
				}
				for (SBean.DBRoleMarchGeneralState o : marchGeneralStates) {
					if (o.id == s.id) {
						o.hp = s.hp;
						o.mp = s.mp;
						o.state = s.state;
						found = true;
						break;
					}
				}
				if (!found)
					marchGeneralStates.add(s.ksClone());
			}
			
			for (SBean.DBRoleMarchGeneralState s : egeneralStates) {
				boolean found = false;
				for (SBean.DBRoleMarchGeneralState o : marchEnemyGeneralStates) {
					if (o.id == s.id) {
						o.hp = s.hp;
						o.mp = s.mp;
						o.state = s.state;
						found = true;
						break;
					}
				}
				if (!found)
					marchEnemyGeneralStates.add(s.ksClone());
			}
		}
		
		AttackFinishRes	res = new AttackFinishRes();
		res.bWin = star > 0;

		marchFinishAttackRes(stage, res, star, clientCostTime, clientEndTime, waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
	}
	
	public void marchStartAttack(byte stage, int clientStartTime, List<Short> generalIDs, SBean.DBPetBrief pet, boolean useMerc)
	{	
		MarchManager.AttackStartRes res = null;
		BattleGeneralInfo[] generalsinfo = new BattleGeneralInfo[5];
		for (int i = 0; i < 5; ++i)
		{
			generalsinfo[i] = new BattleGeneralInfo();
		}
		BattlePetInfo petinfo = new BattlePetInfo();
		int index = 0;
		int spower = 0;
		int mercIndex = -1;
		short mercGid = 0;
		int mercRid = -1;
		for(short gid : generalIDs)
		{
			if (gid > 0)
			{
				General g = generals.get(gid);
				if (useMerc && mercGeneral != null && mercGeneral.general.id == gid) {
					spower += DBRole.calcGeneralPower(mercGeneral.general);
					if (index < 5) {
						BattleGeneralInfo info = generalsinfo[index];
						info.id = mercGeneral.general.id;
						info.lvl = mercGeneral.general.lvl;
						info.evoLvl = mercGeneral.general.evoLvl;
						info.advlvl = mercGeneral.general.advLvl;	
					}
					mercIndex = index;
					mercGid = gid;
					mercRid = mercGeneral.rid;
				}
				else if( g == null )
				{
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchStartAttackRes(stage, null, (short)0, -1));
					return;
				}
				else
				{
					spower += calcGeneralPower(gid);
					if (index < 5)
					{
						BattleGeneralInfo info = generalsinfo[index];
						info.id = g.id;
						info.lvl = g.lvl;
						info.evoLvl = g.evoLvl;
						info.advlvl = g.advLvl;	
					}
				}
			}
			index++;
		}
		if (useMerc && mercIndex >= 0) {
			generalIDs.remove(mercIndex);
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.SecRoundStartRecord secRecord = new  TLogger.SecRoundStartRecord(this.vit);
		synchronized (this) 
		{
			if (this.isBanMarch())
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchStartAttackRes(stage, null, (short)0, -1));
				return;
			}
			SBean.MarchCFGS cfg = GameData.getInstance().getMarchCFG();
			if (stage < 0 && stage >= cfg.stages.size())
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchStartAttackRes(stage, null, (short)0, -1));
				return;
			}
			dayRefresh();
			if (marchState.stage > 0 && marchState.rewardTaken == 0) {
				gs.getRPCManager().sendLuaPacket(netsid,
						LuaPacket.encodeMarchStartAttackRes(stage, null, (short)0, -1));
				return;
			}
			if (marchCurrentEnemies == null || marchState.stage != stage
					|| marchCurrentEnemies.size() <= stage) {
				gs.getRPCManager().sendLuaPacket(netsid,
						LuaPacket.encodeMarchStartAttackRes(stage, null, (short)0, -1));
				return;
			}
			SBean.DBRoleMarchEnemy enemy = marchCurrentEnemies.get(stage);
			res = new AttackStartRes();
			res.roleID = enemy.id;
			res.sgenerals = copyGeneralsWithoutLock(generalIDs);
			if (useMerc && mercGeneral != null)
				res.sgenerals.add(mercGeneral.general.kdClone());
			res.pets = new ArrayList<SBean.DBPetBrief>();
			res.srelation = getActiveRelationsWithoutLock();
			res.sgStones = this.getActiveGeneralStones();
			if (pet != null)
				res.pets.add(pet);
			res.roleName = enemy.name;
			res.egenerals = enemy.generals;
			res.epets = enemy.pets;
			res.erelation = new ArrayList<SBean.DBRelation>(); // No relation current
			res.egStones = new ArrayList<SBean.DBGeneralStone>();
			marchStartTime = gs.getTime();
			secRecord.setMarch(marchState.stage+1, marchState.stage+1);
			secRecord.setStartTime(marchStartTime, clientStartTime);
			float scale = cfg.stages.get(stage).fightPowerScale;
			secRecord.setConfig(cfg.lvlReq, 0, (int)(scale*spower), spower, 1, enemy.generals.size(), 0, 0);
			tlogEvent.addRecord(secRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		gs.getTLogger().logBattleGeneralsState(this, spower, generalsinfo[0].id, generalsinfo[0].lvl, generalsinfo[0].evoLvl, generalsinfo[0].advlvl,
											                 generalsinfo[1].id, generalsinfo[1].lvl, generalsinfo[1].evoLvl, generalsinfo[1].advlvl,
											                 generalsinfo[2].id, generalsinfo[2].lvl, generalsinfo[2].evoLvl, generalsinfo[2].advlvl,
											                 generalsinfo[3].id, generalsinfo[3].lvl, generalsinfo[3].evoLvl, generalsinfo[3].advlvl,
											                 generalsinfo[4].id, generalsinfo[4].lvl, generalsinfo[4].evoLvl, generalsinfo[4].advlvl,
											                 petinfo.id, petinfo.lvl, petinfo.evoLvl, TLog.GENERALEBATTLE_MARCH);
		if (res != null)
			marchStartAttackRes(stage, res, useMerc, mercGid, mercRid);
	}
	
	public synchronized List<SBean.DBRoleMarchGeneralState> copyMarchGeneralStates(boolean enemy, boolean useMerc)
	{
		/*
		List<SBean.DBRoleMarchGeneralState> states = new ArrayList<SBean.DBRoleMarchGeneralState>();
		List<SBean.DBRoleMarchGeneralState> gStates = enemy ? marchEnemyGeneralStates : marchGeneralStates;
		for (SBean.DBRoleMarchGeneralState e : gStates)
			states.add(e.ksClone());
		return states;
		*/
		return copyMarchGeneralStatesWithoutLock(enemy, useMerc);
	}
		
	public void marchStartAttackRes(byte stage, MarchManager.AttackStartRes res, boolean useMerc, short mercGid, int mercRid)
	{
		res.sgeneralStates = copyMarchGeneralStates(false, useMerc);
		res.egeneralStates = copyMarchGeneralStates(true, false);
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchStartAttackRes(stage, res, mercGid, mercRid));
	}
	
	public void marchFinishAttackRes(byte stage, MarchManager.AttackFinishRes res, byte star, int clientCostTime, int clientEndTime, int waveCount, int dpsTotal, int clientSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		boolean cheat = false;
		if( res != null )
		{
			int curTime = gs.getTime();
			int svrCostTime = curTime - marchStartTime;
			cheat = (0 == gs.getConfig().godMode && svrCostTime < clientCostTime*0.9);
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			synchronized( this )
			{
				if( !cheat && res.bWin )
				{
					marchState.stage ++;
					marchEnemyGeneralStates.clear();
					marchState.rewardTaken = 0;
					res.stage = marchState.stage;
					tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_MARCH, 1));
				}
				else
				{
				}
			}
			
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			commonFlowRecord.setEvent(TLog.AT_MARCH_FINISH);
			
			TLogger.MarchRecord record = new TLogger.MarchRecord(lvl);
			record.setMarchInfo(stage+1, res.bWin ? 1 : 0);
			record.setCostTime(svrCostTime);
			
			TLogger.SecRoundEndRecord secRecord = new  TLogger.SecRoundEndRecord();
			secRecord.setSvrEndCostTime(curTime, svrCostTime);
			secRecord.setClientEndCostTime(clientEndTime, clientCostTime);
			secRecord.setResult(cheat ? 1 : 0, star);
			secRecord.setClientStats(waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
			
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(record);
			tlogEvent.addRecord(secRecord);
			
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		if (cheat)
			res = null;
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMarchFinishAttackRes(stage, res));
	}
	
	public int calcPetPower(short pid) 
	{
		float appraise = 0;

		SBean.DBPet pet = null;
		boolean deformed = false;
		synchronized(this) {
			Pet p = pets.get(pid);
			if (p != null)
				pet = p.copyDBPetWithoutLock();
			SBean.DBPetDeform d = petDeforms.get(pid);
			if (d != null)
				deformed = isPetDeformed(d.deformStage);
		}
		
		if (pet == null)
			return 0;
		
		SBean.PetCFGS pcfg = gs.getGameData().getPetCFG(pid, deformed);
		if (pcfg != null) {
			appraise = pcfg.initPower + pcfg.upPower * (pet.growthRate/100.0f) * (pet.lvl - 1);
			
			SBean.PetEvoCFGS evoCfg = pcfg.evo.get(pet.evoLvl - 1);
			if (evoCfg != null) {
				appraise += evoCfg.power;
			}
		}

		return (int)(appraise + 0.5f);
	}
	
	public int calcGeneralPower(short gid) 
	{
		DBRoleGeneral general = null;
		synchronized(this) {
			General g = generals.get(gid);
			if (g != null)
				general = g.copyDBGeneralWithoutLock();
		}
		
		return DBRole.calcGeneralPower(general);
	}
	
	/*
	public int calcGeneralPower(short gid) 
	{
		List<SBean.PowerCFGS> cfgs = gs.getGameData().getPowerCFG();
		SBean.PowerCFGS cfg = null;
		for (SBean.PowerCFGS e : cfgs) {
			if (e.id == gid) {
				cfg = e;
				break;
			}
		}
		
		if (cfg == null)
			return 0;
		
		DBRoleGeneral general = null;
		synchronized(this) {
			General g = generals.get(gid);
			if (g != null)
				general = g.copyDBGeneralWithoutLock();
		}
		
		if (general == null)
			return 0;
		
		int advLvl = general.advLvl - 1;
		int evoLvl = general.evoLvl - 1;
		
		int lvlApp = cfg.initPower + (general.lvl - 1) * cfg.upPower;
		
		int equApp = 0;
		SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
		for (int i = 0; i < GameData.GENERAL_EQUIP_COUNT; i ++) {
			DBGeneralEquip e = general.equips.get(i);
			if (e != null && e.lvl >= 0) {
				if (gcfg.equips.size() <= advLvl * GameData.GENERAL_EQUIP_COUNT + i)
					return 0;
				short eid = gcfg.equips.get(advLvl * GameData.GENERAL_EQUIP_COUNT + i).shortValue();
				SBean.EquipCFGS ecfg = gs.getGameData().getEquipCFG(eid);
				if (ecfg != null) {
					equApp += ecfg.powers.get(0);
					for (int j = 0; j < e.lvl; j ++)
						equApp += ecfg.powers.get(j + 1);
				}
			}
		}
		
		int sklApp = 0;
		SBean.GeneralCmnCFGS generalCmnCfg = gs.getGameData().getGeneralCmnCFG();
		for (int i = 0; i < GameData.GENERAL_SKILL_COUNT; i ++) {
			SBean.GeneralSkillCFGS skillcfg = generalCmnCfg.skills.get(i);
			if (general.advLvl >= skillcfg.reqAdvLvl) {
				short sklLvl = general.skills.get(i).shortValue();
				if (sklLvl >= (short)1) {
					int skillInitPower = 0;
					int size = generalCmnCfg.skillInitPower.size();
					if (i < size)
						skillInitPower = generalCmnCfg.skillInitPower.get(i);
					sklApp += skillInitPower + (sklLvl - 1) * generalCmnCfg.skillUpPower;
				}
			}
		}
		
		int advApp = 0;
		for (int i = 0; i < advLvl; i ++)
			if (gcfg.advPowerUps.size() > i)
				advApp += gcfg.advPowerUps.get(i);
		
		return (int)((lvlApp + equApp + advApp) * (1 + generalCmnCfg.evoPowerScales.get(evoLvl).floatValue()) + 0.5f) + sklApp; 
	}
	*/
	
	public int getArenaGeneralsPowerWithoutLock()
	{
		int power = 0;
		for (int i = 0; i < 5; i ++)
		{
			short gid = arenaGenerals.get(i);
			power += calcGeneralPower(gid);
		}
		return power;
	}
	
	public int getArenaPetsPowerWithoutLock()
	{
		int power = 0;
		short pid = arenaPets.get(0);
		power += calcPetPower(pid);
		return power;
	}
	
	public void arenaSyncInfo()
	{		
		int oldWinTimes = 0;
		final ArenaStateInfo info = new ArenaStateInfo();
		synchronized( this )
		{
			oldWinTimes = arenaState.winTimes;
			info.ranks = arenaCurrentEnemies;
		}
		final List<Integer> rids = gs.getArenaManager().getRoleInfo(id, info);
		if( lvl < gs.getGameData().getArenaCFG().lvlReq )
			return;
		int infoMaxLvl = gs.getArenaManager().getMaxLevel(lvl);
		int infoMaxWinTimes = gs.getArenaManager().getMaxWinTimes(oldWinTimes);
		synchronized( this )
		{
			dayRefresh();
			info.rankBest = (arenaBestRank <=0) ? (gs.getGameData().getArenaCFG().rankMax) : arenaBestRank;
			info.maxLvl = infoMaxLvl;
			info.maxWinTimes = infoMaxWinTimes;
			info.point = arenaState.point;
			info.timesUsed = arenaState.timesUsed;
			info.timesBuy = arenaState.timesBuy;
			info.lastTime = arenaState.lastTime;
			if( info.rankReward > 0 && arenaState.rewardStamp >= info.stampReward )
			{
				info.rankReward = 0;
			}
			arenaCurrentEnemies = info.ranks;
			info.generals = new short[5];
			int power = 0;
			for (int i = 0; i < 5; i ++)
			{
				info.generals[i] = arenaGenerals.get(i);
				power += calcGeneralPower(info.generals[i]);
			}
			info.pets = new short[1];
			info.pets[0] = arenaPets.get(0);
			power += calcPetPower(info.pets[0]);			
			info.power = power;
		}
		// sync arena state info
		if( rids.isEmpty() )
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncInfo(info));
			return;
		}
		gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
			
			@Override
			public void onCallback(List<RoleBriefCachePair> lst)
			{
				Map<Integer, SBean.DBRoleBrief> rmap = new TreeMap<Integer, SBean.DBRoleBrief>();
				for(RoleBriefCachePair e : lst)
				{
					rmap.put(e.id, new SBean.DBRoleBrief(e.id, e.cache.lvl, e.cache.headIconID
							, e.cache.name, e.cache.fname, e.cache.score));
				}
				for(int i = 0; i < info.enemies.size(); ++i)
				{
					if( info.enemies.get(i).id > 0 )
					{
						info.enemies.set(i, rmap.get(info.enemies.get(i).id));
					}
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncInfo(info));
			}
		});
	}

	public void arenaSyncBrief()
	{		
		int oldWinTimes = 0;
		synchronized( this )
		{
			oldWinTimes = arenaState.winTimes;
		}
		ArenaStateInfo info = new ArenaStateInfo();
		gs.getArenaManager().getRoleInfo(id, info);
		if( lvl < gs.getGameData().getArenaCFG().lvlReq )
			return;
		int infoMaxLvl = gs.getArenaManager().getMaxLevel(lvl);
		int infoMaxWinTimes = gs.getArenaManager().getMaxWinTimes(oldWinTimes);
		synchronized( this )
		{
			dayRefresh();
			info.maxLvl = infoMaxLvl;
			info.maxWinTimes = infoMaxWinTimes;
			info.point = arenaState.point;
			info.timesUsed = arenaState.timesUsed;
			info.timesBuy = arenaState.timesBuy;
			info.lastTime = arenaState.lastTime;
			if( info.rankReward > 0 && arenaState.rewardStamp >= info.stampReward )
			{
				info.rankReward = 0;
			}
			info.generals = new short[5];
			int power = 0;
			for (int i = 0; i < 5; i ++)
			{
				info.generals[i] = arenaGenerals.get(i);
				power += calcGeneralPower(info.generals[i]);
			}
			info.pets = new short[1];
			info.pets[0] = arenaPets.get(0);
			power += calcPetPower(info.pets[0]);
			info.power = power;
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncBrief(info));
	}
	
//	public boolean arenaTakeReward()
//	{
//		synchronized( this )
//		{
//			SBean.ArenaCFGS cfg = gs.getGameData().getArenaCFG();
//			ArenaStateInfo info = new ArenaStateInfo();
//			gs.getArenaManager().getRoleInfo(id, info);
//			if( info.rankReward > 0 && arenaState.rewardStamp >= info.stampReward )
//			{
//				info.rankReward = 0;
//			}
//			if( info.rankReward <= 0 )
//				return false;
//			SBean.ArenaRewardCFGS reward = null;
//			for(SBean.ArenaRewardCFGS e : cfg.rewards)
//			{
//				if( info.rankReward <= e.rankFloor )
//				{
//					reward = e;
//					break;
//				}
//			}
//			if( reward == null )
//				return false;
//			arenaState.rewardStamp = info.stampReward;
//			addMoney(reward.money);
//			addStone(reward.stone);
//			if (reward.item1 > 0)
//			{
//				addItem(reward.item1, reward.item1Count);
//			}
//			if (reward.item2 > 0)
//			{
//				addItem(reward.item2, reward.item2Count);
//			}
//			arenaState.point += reward.point;
//		}
//		return true;
//	}
	
	public boolean arenaClearCool()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int stoneNeed = gs.getGameData().getArenaCFG().stoneRefresh;
			if( stone < stoneNeed )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			arenaState.lastTime = 0;
		}
		commonFlowRecord.setEvent(TLog.AT_ARENA_CLEAR_COOL);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean arenaBuyTimes()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			short maxBuy = gs.getGameData().getArenaBuyTimesMax(lvlVIP);
			if( arenaState.timesBuy >= maxBuy )
				return false;
			List<Short> price = gs.getGameData().getArenaCFG().timesPrice;
			int index = arenaState.timesBuy;
			if( index >= price.size() )
				index = price.size() - 1;
			int stoneNeed = price.get(index);
			if( stone < stoneNeed )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			arenaState.timesBuy++;
		}
		commonFlowRecord.setEvent(TLog.AT_ARENA_BUY_TIMES);
		commonFlowRecord.setArg(arenaState.timesBuy, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	private void arenaAddLog(SBean.DBRoleArenaLog log)
	{
		arenaLogs.add(log);
		if( arenaLogs.size() > 10 )
		{
			arenaLogs.remove(0);
		}
	}
	
	public void arenaSetPassiveAttackLog(SBean.DBRoleArenaLog log)
	{
		synchronized( this )
		{
			arenaAddLog(log);
		}
	}
	
	public void arenaSyncLogs()
	{		
		final List<SBean.DBRoleArenaLog> rlogs;
		synchronized( this )
		{
			rlogs = copyDBArenaLogsWithoutLock();
		}
		final List<Integer> rids = new ArrayList<Integer>();
		for(SBean.DBRoleArenaLog e : rlogs)
		{
			if( e.id > 0 )
				rids.add(e.id);
		}
		// sync arena state info
		if( rids.isEmpty() )
		{
			List<ArenaLog> logs = new ArrayList<ArenaLog>();
			for(SBean.DBRoleArenaLog e : rlogs)
			{
				ArenaLog l = new ArenaLog();
				l.id = e.id;
				l.win = e.win;
				l.rankUp = e.rankUp;
				l.time = e.time;
				l.recordId = e.recordId;
				l.lvl = 1;
				l.headIconID = 1;
				l.name = "";
				logs.add(l);
			}
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncLogs(logs));
			return;
		}
		gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
			
			@Override
			public void onCallback(List<RoleBriefCachePair> lst)
			{
				Map<Integer, SBean.DBRoleBrief> rmap = new TreeMap<Integer, SBean.DBRoleBrief>();
				for(RoleBriefCachePair e : lst)
				{
					rmap.put(e.id, new SBean.DBRoleBrief(e.id, e.cache.lvl, e.cache.headIconID
							, e.cache.name, e.cache.fname, e.cache.score));
				}
				List<ArenaLog> logs = new ArrayList<ArenaLog>();
				for(SBean.DBRoleArenaLog e : rlogs)
				{
					ArenaLog l = new ArenaLog();
					l.id = e.id;
					l.win = e.win;
					l.rankUp = e.rankUp;
					l.time = e.time;
					l.recordId = e.recordId;
					if( l.id > 0 )
					{
						SBean.DBRoleBrief b = rmap.get(l.id);
						l.lvl = b.lvl;
						l.headIconID = b.headIconID;
						l.name = b.name;
					}
					else
					{
						l.lvl = 1;
						l.headIconID = 1;
						l.name = "";
					}
					logs.add(l);
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncLogs(logs));
			}
		});
	}
	
	public void arenaFinishAttackRes(int selfrank, int rank, ArenaManager.ActiveAttackFinishRes res, int recordId, int recordTime)
	{
		SBean.ArenaCFGS cfg = gs.getGameData().getArenaCFG();
		List<SBean.DropEntryNew> bestRankRewards = null;
		int bestRankUp = 0;
		if( res != null )
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			synchronized( this )
			{
				if( res.bWin )
				{
					arenaState.winTimes++;
					arenaState.lastTime=0;
				}
				else
				{
				}
				if(true) // res.rankUp > 0 ) // win log change
				{
					SBean.DBRoleArenaLog log = new SBean.DBRoleArenaLog();
					log.win = (byte)(res.bWin?1:0);
					log.id = res.roleID;
					log.time = recordTime;
					log.rankUp = res.rankUp;
					log.recordId = recordId;
					arenaAddLog(log);
				}
				if (res.rankUp > 0) // win
				{
					int oldBestRank = (arenaBestRank <=0) ? (cfg.rankMax) : arenaBestRank;
					if( rank > 0 && rank < oldBestRank )
					{
						bestRankRewards = GameData.getInstance().getArenaBestRankRewards(rank, arenaBestRank);
						if( bestRankRewards != null )
						{
							commonFlowRecord.addChanges(addDropsNew(bestRankRewards));
						}
						bestRankUp = oldBestRank - rank;
						arenaBestRank = rank;
					}
				}
			}
			commonFlowRecord.setEvent(TLog.AT_ARENA_FINISH);
			tlogEvent.addCommonFlow(commonFlowRecord);
			TLogger.ArenaRecord record = new TLogger.ArenaRecord(lvl);
			record.setArenaTwoSidesRank(selfrank, rank);
			record.setResult(res.bWin ? TLog.ARENARE_WIN : TLog.ARENARE_LOSE);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaFinishAttackRes(rank, res, bestRankUp, bestRankRewards));
		//gs.getGameStatLogger().roleAction(this.id, GameStateLogger.Actions.ARENAATTACK, arenaState.timesUsed, formatArenaAttackRecord(arenaState.timesUsed, res.rankUp, changePoint), 
        //                                 this.lvl, 0, this.money, this.stone, 
        //                                  0, 0, 0, changeMoney, 0, "", "", "");
	}
	
	public void arenaCalcAttackResult(List<DBRoleGeneral> generals1, List<DBPetBrief> pets1, List<SBean.DBRelation> relation1 ,List<SBean.DBGeneralStone> gStone1,List<DBRoleGeneral> generals2, List<DBPetBrief> pets2, List<SBean.DBRelation> relation2,
			List<SBean.DBGeneralStone> gStone2, int seed, int rid1, int rid2)
	{
		ArenaManager.CalcAttackStartReq req = new ArenaManager.CalcAttackStartReq();
		req.seed = seed;
		req.rid1 = rid1;
		req.rid2 = rid2;
		req.generals1 = generals1;
		req.generals2 = generals2;
		req.pets1 = pets1;
		req.pets2 = pets2;
		req.relation1 = relation1;
		req.relation2 = relation2;
		req.gStone1 = gStone1;
		req.gStone2 = gStone2;
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaCalcAttackResultReq(req));
	}
	
	public void arenaStartAttackRes(int rank, int err, ArenaManager.ActiveAttackStartRes res)
	{
		//SBean.ArenaCFGS cfg = gs.getGameData().getArenaCFG();
		if( res != null )
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			synchronized( this )
			{
				//logDailyTask2(GameData.DAILYTASK2_TYPE_ARENA, 1);
				++arenaState.timesUsed;
				
				arenaState.lastTime = gs.getTime();
				arenaCurrentEnemies = null;
				arenaCurrentAttackRank = rank;
				
				tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_ARENA, 1));
			}
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaStartAttackRes(rank, err, res));
		//gs.getGameStatLogger().roleAction(this.id, GameStateLogger.Actions.ARENAATTACK, arenaState.timesUsed, formatArenaAttackRecord(arenaState.timesUsed, res.rankUp, changePoint), 
        //                                 this.lvl, 0, this.money, this.stone, 
        //                                  0, 0, 0, changeMoney, 0, "", "", "");
	}
	
	public void arenaFinishAttack(int rank, boolean bWin, SBean.ArenaRecord record)
	{		
		synchronized( this )
		{
			if( rank != arenaCurrentAttackRank )
				return;
			arenaCurrentAttackRank = 0;
			arenaCurrentAttackRes = 0;
		}
		gs.getArenaManager().finishAttack(this, bWin, record);
	}
	
	public void arenaStartAttack(int rank, int erid, List<Short> generals, List<Short> pets)
	{	
		SBean.ArenaCFGS cfg = gs.getGameData().getArenaCFG();
		int now = gs.getTime();
		synchronized( this )
		{
			if (isBanArena())
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaStartAttackRes(rank, 0, null));
				return;
			}
			dayRefresh();
			boolean bFound = false;
			if( arenaCurrentEnemies != null )
			{
				for(int e : arenaCurrentEnemies)
				{
					if( e == rank )
					{
						bFound = true;
						break;
					}
				}
			}
			if( ! bFound )
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaStartAttackRes(rank, 1, null));
				return;
			}
			if( arenaState.lastTime + cfg.coolTime * 60 > now )
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaStartAttackRes(rank, 0, null));
				return;
			}
			if( arenaState.timesUsed >= arenaState.timesBuy + cfg.freeTimes )
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaStartAttackRes(rank, 0, null));
				return;
			}
		}
		BattleGeneralInfo[] generalsinfo = new BattleGeneralInfo[5];
		for (int i = 0; i < 5; ++i)
		{
			generalsinfo[i] = new BattleGeneralInfo();
		}
		BattlePetInfo petinfo = new BattlePetInfo();
		int power = 0;
		int index = 0;
		for (short gid : generals)
		{
			General g = this.generals.get(gid);
			if (g != null)
			{
				power += calcGeneralPower(gid);
				BattleGeneralInfo info = generalsinfo[index];
				info.id = g.id;
				info.lvl = g.lvl;
				info.evoLvl = g.evoLvl;
				info.advlvl = g.advLvl;
			}
			index++;
		}
		if (!pets.isEmpty())
		{
			short pid = pets.get(0);
			Pet p = this.pets.get(pid);
			if (p != null)
			{
				power += calcPetPower(pid);
				petinfo.id = p.id;
				petinfo.lvl = p.lvl;
				petinfo.evoLvl = p.evoLvl;
			}	
		}
		gs.getTLogger().logBattleGeneralsState(this, power, generalsinfo[0].id, generalsinfo[0].lvl, generalsinfo[0].evoLvl, generalsinfo[0].advlvl,
				                                            generalsinfo[1].id, generalsinfo[1].lvl, generalsinfo[1].evoLvl, generalsinfo[1].advlvl,
				                                            generalsinfo[2].id, generalsinfo[2].lvl, generalsinfo[2].evoLvl, generalsinfo[2].advlvl,
				                                            generalsinfo[3].id, generalsinfo[3].lvl, generalsinfo[3].evoLvl, generalsinfo[3].advlvl,
				                                            generalsinfo[4].id, generalsinfo[4].lvl, generalsinfo[4].evoLvl, generalsinfo[4].advlvl,
				                                            petinfo.id, petinfo.lvl, petinfo.evoLvl, TLog.GENERALEBATTLE_ARENA_ATTACK);
		gs.getArenaManager().startAttack(this, rank, erid, generals, pets);
	}
	
	public boolean arenaRefresh()
	{
		synchronized( this )
		{
			arenaCurrentEnemies = null;
		}
		return true;
	}
	
	public int setArenaGenerals(short[] agenerals, short apet)
	{
		if (agenerals == null || agenerals.length != 5)
			return 0;
		
		int minOfDay = gs.getMinuteOfDay();
		if (minOfDay >= gs.getGameData().getArenaCFG().refreshTime - 10 && minOfDay < gs.getGameData().getArenaCFG().refreshTime)
			return -1;
		
		synchronized( this )
		{
			boolean exist = false;
			for (int i = 0; i < 5; i ++) 
			{
				if (agenerals[i] == -1)
					continue;
				General g = generals.get(agenerals[i]);
				if (g == null)
					return 0;
				exist = true;
			}
			
			if (!exist)
				return 0;
			
			Pet p = pets.get(apet);
			if (apet > 0 && p == null)
				return 0;
			
			for (int i = 0; i < 5; i ++)
				arenaGenerals.set(i, Short.valueOf(agenerals[i]));
			arenaPets.set(0, apet);
		}
		logBattleGeneralsInfo();
		return 1;
	}
	
	//TODO super arena
	boolean syncSuperArenaState()
	{
		if (superArena == null)
		{
			List<SBean.DBRoleArenaFightTeam> fightTeams = new ArrayList<SBean.DBRoleArenaFightTeam>();
//			for (int i = 0; i < 3; ++i)
//			{
//				SBean.DBRoleArenaFightTeam ft = new SBean.DBRoleArenaFightTeam(new ArrayList<Short>(), new ArrayList<Short>());
//				fightTeams.add(ft);
//			}
			List<Integer> orders = new ArrayList<Integer>();
			orders.add(0);
//			for (int i = 0; i < ArenaManager.SuperArena.getMaxTeamOrderCount(); ++i)
//			{
//				orders.add(0);
//			}
			SBean.DBRoleSuperArenaTeam team = new SBean.DBRoleSuperArenaTeam(fightTeams, 0, 0, orders, 0);
			SBean.DBRoleSuperArena data = new SBean.DBRoleSuperArena(team, new ArrayList<SBean.DBRoleArenaLog>(), 0, (byte)0, (byte)0, 0, 0, 0, 0, 0, 0, 0);
			superArena = data;
			return true;
		}
		return false;
	}
	
	public static class SuperArenaRoleInfo
	{
		public int superarenaRank;
		public List<SBean.DBRoleArenaFightTeam> teams = new ArrayList<SBean.DBRoleArenaFightTeam>();
		public int timesUsed;
		public int timesBuy;
		public int lastFightTime;
		public int point;
		public int rankRewards;
	}
	public static class SuperArenaSyncInfo
	{
		public int arenaRank;
		public SuperArenaRoleInfo info;
	}
	public SuperArenaSyncInfo superArenaSyncInfo()
	{
		SuperArenaSyncInfo syncInfo = new SuperArenaSyncInfo();
		syncInfo.arenaRank = gs.getArenaManager().getRoleRank(this.id);
		if (superArena == null)
			return syncInfo;
		SuperArenaRoleInfo info = new SuperArenaRoleInfo();
		gs.getArenaManager().getSuperArena().newRoleJoin(this.id);
		synchronized (this)
		{
			for (SBean.DBRoleArenaFightTeam e : superArena.team.teams)
			{
				info.teams.add(e.kdClone());
			}
			info.timesUsed = superArena.timesUsed;
			info.timesBuy = superArena.timesBuy;
			info.lastFightTime = superArena.lastFightTime;
			info.point = superArena.point;
			info.rankRewards = superArena.rankRewards;
		}
		info.superarenaRank = gs.getArenaManager().getSuperArena().getRoleRank(this.id);
		syncInfo.info = info;
		gs.getArenaManager().getSuperArena().removeAttacked(this.id);
		return syncInfo;
	}
	
	public void superArenaListRanks()
	{
		final List<Integer> roles = gs.getArenaManager().getSuperArena().getTopRanks(50);
		gs.getLoginManager().getDataBrief(roles, false, new LoginManager.GetDataBriefCallback() 
		{
			
			@Override
			public void onCallback(List<RoleBriefCachePair> lst)
			{
				Map<Integer, SBean.DBRoleBrief> rmap = new TreeMap<Integer, SBean.DBRoleBrief>();
				for(RoleBriefCachePair e : lst)
				{
					rmap.put(e.id, new SBean.DBRoleBrief(e.id, e.cache.lvl, e.cache.headIconID, e.cache.name, e.cache.fname, e.cache.score));
				}
				List<Role.ArenaRank> ranks = new ArrayList<Role.ArenaRank>();
				for(int rid : roles)
				{
					Role.ArenaRank ar = new Role.ArenaRank();
					ar.id = rid;
					SBean.DBRoleBrief b = rmap.get(rid);
					if( b != null )
					{
						ar.lvl = b.lvl;
						ar.name = b.name;
						ar.headIconID = b.headIconID;
					}
					ranks.add(ar);
				}
				
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaSyncRanks(ranks));
			}
		});
	}
	
	
	public int superArenaTakeRankReward()
	{
		if (superArena == null)
			return 0;
		int reward = 0;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if (superArena.rankRewards <= 0)
				return 0;
			reward = superArena.rankRewards;
			commonFlowRecord.addChange(Role.this.addSuperArena(superArena.rankRewards));
			superArena.rankRewards = 0;
		}
		commonFlowRecord.setEvent(TLog.AT_SUPER_ARENA_REWARD);
		commonFlowRecord.setArg(1, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return reward;
	}
	
	public boolean superArenaSetGenerals(SBean.DBRoleArenaFightTeam[] teams)
	{
		boolean firstSet = false;
		synchronized (this)
		{
			for (int i = 0; i < teams.length; ++i)
			{
				SBean.DBRoleArenaFightTeam t = teams[i];
				if (t.generals.size() <= 0 || t.generals.size() > 5)
					return false;
				for (short gid : t.generals)
				{
					if (!this.generals.containsKey(gid))
						return false;
				}
				if (t.pets.size() < 0 || t.pets.size() > 5)
					return false;
				for (short pid : t.pets)
				{
					if (!this.pets.containsKey(pid))
						return false;
				}
				for (int j = i+1; j < teams.length; ++j)
				{
					SBean.DBRoleArenaFightTeam tt = teams[j];
					if (!Collections.disjoint(t.generals, tt.generals))
						return false;
					if (!Collections.disjoint(t.pets, tt.pets))
						return false;
				}
			}
			firstSet = syncSuperArenaState();
			superArena.team.teams.clear();
			for (int i = 0; i < teams.length; ++i)
			{
				SBean.DBRoleArenaFightTeam t = teams[i];
				superArena.team.teams.add(t);
			}
		}
		if (firstSet)
		{
			gs.getArenaManager().getSuperArena().newRoleJoin(this.id);
			this.doSave();
		}
		return true;
	}
	
	
	
	public static class SuperArenaTeamHideInfo
	{
		public int hideTime;
		public int hideTeams;
	}
	
	public synchronized SuperArenaTeamHideInfo superArenaGetTeamHideInfo()
	{
		if (superArena == null)
			return null;
		SBean.SuperArenaCFGS cfgs = GameData.getInstance().getSuperArenaCFG();
		int nowDay = gs.getDayCommon();
		superArenaUpdateTeamHide(cfgs, nowDay);
		SuperArenaTeamHideInfo info = new SuperArenaTeamHideInfo();
		info.hideTime = superArena.team.hideTime;
		info.hideTeams = superArena.team.hiddenTeams;
		return info;
	}
	
	void superArenaUpdateTeamHide(SBean.SuperArenaCFGS cfgs, int dayNow)
	{
		if (superArena.team.hideTime != 0)
		{
			if (superArena.team.hideTime + cfgs.hideTeamDaySpan <= dayNow)
			{
				superArena.team.hideTime = 0;
				superArena.team.hiddenTeams = 0;
			}	
		}
	}
	
	public boolean superArenaSetTeamHide(int count)
	{
		if (superArena == null)
			return false;
		SBean.SuperArenaCFGS cfgs = GameData.getInstance().getSuperArenaCFG();
		if (this.lvlVIP < cfgs.hideOrderVipReq)
			return false;
		if (count <= 0 || count > cfgs.hideTeamCost.size())
			return false;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			int nowDay = gs.getDayCommon();
			superArenaUpdateTeamHide(cfgs, nowDay);
			int oldCount = superArena.team.hiddenTeams > cfgs.hideTeamCost.size() ? cfgs.hideTeamCost.size() : superArena.team.hiddenTeams;
			if (count <= oldCount)
				return false;
			int oldCostPerDay = oldCount > 0 ? cfgs.hideTeamCost.get(oldCount-1) : 0;
			int newCostPerDay = cfgs.hideTeamCost.get(count-1);
			
			int leftDay = superArena.team.hideTime == 0 ? 0 : cfgs.hideTeamDaySpan - (nowDay - superArena.team.hideTime);
			if (leftDay < 0)
				leftDay = 0;
			int cost = newCostPerDay * cfgs.hideTeamDaySpan - leftDay * oldCostPerDay;
			if (this.stone < cost)
				return false;
			commonFlowRecord.addChange(this.useStone(cost));
			superArena.team.hideTime = nowDay;
			superArena.team.hiddenTeams = count;
		}
		commonFlowRecord.setEvent(TLog.AT_SUPER_ARENA_HIDE_TEAMS);
		commonFlowRecord.setArg(count);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return true;
	}
	
	public static class SuperArenaOrderInfo
	{
		public int curOrderSeq = 1;
		public List<Integer> orders = new ArrayList<Integer>();
	}
	public synchronized SuperArenaOrderInfo superArenaGetTeamFightOrders()
	{
		if (superArena == null)
			return null;
		SuperArenaOrderInfo info = new SuperArenaOrderInfo();
		info.curOrderSeq = superArena.team.curOrderIndex+1;
		for (int o :  superArena.team.orders)
		{
			info.orders.add(o+1);
		}
		return info;
	}
	
	public synchronized boolean superArenaSetTeamFightOrders(List<Integer> newOrders)
	{
		if (superArena == null)
			return false;
		SBean.SuperArenaCFGS cfgs = GameData.getInstance().getSuperArenaCFG();
		if (this.lvlVIP < cfgs.hideOrderVipReq)
			return false;
		if (newOrders.isEmpty() || newOrders.size() > ArenaManager.SuperArena.getMaxTeamOrderCount())
			return false;
		for (int o : newOrders)
		{
			if (!ArenaManager.SuperArena.isTeamOrderIndexInvalid(o-1))
				return false;
		}
		superArena.team.orders.clear();
		for (int o :  newOrders)
		{
			superArena.team.orders.add(o-1);
		}
		superArena.team.curOrderIndex = 0;
		return true;
	}
	
	public static class SuperArenaTarget
	{
		public int rank;
		public SBean.DBRoleBrief brief;
	}
	
	public void superArenaSearchEnemy()
	{
		final ArenaManager.RankRole[] rrs = gs.getArenaManager().getSuperArena().searchEnemy(this.id);
		if (rrs == null)
			return ;
		List<Integer> rids = new ArrayList<Integer>();
		for (ArenaManager.RankRole e : rrs)
		{
			if (e != null)
			{
				rids.add(e.rid);
			}
		}
		gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
			
			@Override
			public void onCallback(List<RoleBriefCachePair> lst)
			{
				Map<Integer, SBean.DBRoleBrief> rmap = new TreeMap<Integer, SBean.DBRoleBrief>();
				for(RoleBriefCachePair e : lst)
				{
					rmap.put(e.id, new SBean.DBRoleBrief(e.id, e.cache.lvl, e.cache.headIconID, e.cache.name, e.cache.fname, e.cache.score));
				}
				List<SuperArenaTarget> targets = new ArrayList<SuperArenaTarget>();
				for (ArenaManager.RankRole e : rrs)
				{
					if (e != null)
					{
						SuperArenaTarget t = new SuperArenaTarget();
						t.rank = e.rank;
						t.brief = rmap.get(e.rid);
						targets.add(t);
					}
					else
					{
						targets.add(null);
					}
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaTargetsInfo(targets));
			}
		});
	}
	
	public void superArenaViewEnemy(final int rid)
	{
		final int rankNow = gs.getArenaManager().getSuperArena().getRoleRank(rid);
		gs.getLoginManager().getRoleData(new SBean.RoleDataReq(SBean.RoleDataReq.eSuperArenaTeam | SBean.RoleDataReq.eBrief, rid), new LoginManager.GetRoleDataCallback() 
		{
				@Override
				public void onCallback(RoleDataRes res)
				{
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaViewRes(rid, rankNow, res.brief, res.superArena));
				}
			});
	}
	
	SBean.RoleDataGeneral getGeneralData(short gid)
	{
		General g = generals.get(gid);
		if (g != null)
			return new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo);
		return null;
	}
	
	SBean.RoleDataPet getPetData(short pid)
	{
		Pet p = pets.get(pid);
		if (p != null)
			return new SBean.RoleDataPet(p.id, p.lvl, p.evoLvl, p.growthRate, p.name);
		return null;
	}
	
	DBRoleGeneral getGeneral(short gid)
	{
		General g = generals.get(gid);
		if (g != null)
		{
			DBRoleGeneral rg = new DBRoleGeneral(g.id, g.lvl, g.exp, g.advLvl, g.evoLvl,
					g.weapon.kdClone(), new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>()
					,new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)g.headicon
					,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
			rg.skills.addAll(g.skills);
			rg.equips.addAll(g.equips);
			rg.generalStone = copyGeneralStoneWithoutLock(gid);
			if(g.generalSeyen!=null){
				rg.generalSeyen.addAll(g.generalSeyen);
			}
			if(g.bestow!=null){
				rg.bestow.addAll(g.bestow);
			}
			if(g.bless!=null){
				rg.bless.addAll(g.bless);
			}
			if (g.official != null)
				rg.official.addAll(g.official);
			
			return rg;
		}
		return null;
	}
	
	SBean.DBPetBrief getPetBrief(short pid)
	{
		Pet p = pets.get(pid);
		if (p != null) {
			SBean.DBPetDeform deform = petDeforms.get(p.id);
			byte deformStage = 0;
			if (deform != null)
				deformStage = deform.deformStage;
			return new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name);
		}
		return null;
	}
	
	SBean.RoleDataFightTeam superArenaFillTeam(SBean.DBRoleArenaFightTeam t)
	{
		SBean.RoleDataFightTeam team = new SBean.RoleDataFightTeam(new ArrayList<SBean.RoleDataGeneral>(), new ArrayList<SBean.RoleDataPet>());
		for (short gid : t.generals)
		{
			SBean.RoleDataGeneral g = getGeneralData(gid);
			team.generals.add(g);
		}
		for (short pid : t.pets)
		{
			SBean.RoleDataPet p = getPetData(pid);
			team.pets.add(p);
		}
		return team;
	}
	
	SBean.SuperArenaTeamDetail superArenaFillTeamDetail(SBean.DBRoleArenaFightTeam t)
	{
		SBean.SuperArenaTeamDetail team = new SBean.SuperArenaTeamDetail(new ArrayList<DBRoleGeneral>(), 
				new ArrayList<SBean.DBPetBrief>(), new ArrayList<SBean.DBRelation>(),new ArrayList<SBean.DBGeneralStone>());
		for (short gid : t.generals)
		{
			DBRoleGeneral g = getGeneral(gid);
			team.generals.add(g);
		}
		for (short pid : t.pets)
		{
			SBean.DBPetBrief p = getPetBrief(pid);
			team.pets.add(p);
		}
		team.relation = getActiveRelations();
		team.gStones = this.getActiveGeneralStones();
		return team;
	}
	
	public List<SBean.SuperArenaTeamDetail> superArenaGetFightTeamDetail(SBean.DBRoleSuperArena superArena)
	{
		List<SBean.SuperArenaTeamDetail> teamsdetail = new ArrayList<SBean.SuperArenaTeamDetail>();
		int curOrder = DBRole.getSuperArenaCurOrder(superArena);
		List<SBean.DBRoleArenaFightTeam> teams = ArenaManager.SuperArena.getOrderedFightTeams(superArena.team.teams, curOrder);
		for (int index = 0; index < teams.size(); ++index)
		{
			SBean.DBRoleArenaFightTeam t = teams.get(index);
			SBean.SuperArenaTeamDetail teamDetail = superArenaFillTeamDetail(t);
			teamsdetail.add(teamDetail);
		}
		return teamsdetail;
	}
	
	public SBean.SuperArenaFightInfo superArenaGetFightInfo()
	{
		if (superArena == null)
			return null;
		SBean.SuperArenaFightInfo info = new SBean.SuperArenaFightInfo(this.id, this.headIconID, this.name, this.lvl, this.superArenaGetFightTeamDetail(superArena));
		return info;
	}
	
	public void superArenaChangeFightTeamOrder()
	{
		if (superArena == null)
			return;
		superArena.team.curOrderIndex++;
		superArena.team.curOrderIndex = superArena.team.curOrderIndex % superArena.team.orders.size();
	}
	
	public void superArenaAddLog(SBean.DBRoleArenaLog log)
	{
		if (superArena == null)
			return;
		superArena.logs.add(log);
		if( superArena.logs.size() > 10 )
		{
			superArena.logs.remove(0);
		}
	}
	
	int superArenaTestCanStartAttack(int now, SBean.DBRoleArenaFightTeam[] teams)
	{
		SBean.SuperArenaCFGS cfg = gs.getGameData().getSuperArenaCFG();
		synchronized( this )
		{
			if (isBanArena())
				return -2;
			int startTime = gs.getTimeByMinuteOffset(cfg.fightStartTime);
			int endTime = gs.getTimeByMinuteOffset(cfg.fightEndTime);
			if (now < startTime || now > endTime)
				return -3;
			dayRefresh();
			if (superArena == null)
				return -4;
			if (superArena.lastFightTime + cfg.coolTime * 60  > now)
				return -5;
			if (superArena.timesUsed >= superArena.timesBuy + cfg.freeTimes)
				return -6;
			if (teams.length != 3)
				return -7;
			for (int i = 0; i < teams.length; ++i)
			{
				SBean.DBRoleArenaFightTeam t = teams[i];
				if (t.generals.isEmpty() || t.generals.size() > 5)
					return -8;
				if (t.pets.size() != 0 && t.pets.size() != 1)
					return -9;
				for (short gid : t.generals)
				{
					if (!this.generals.containsKey(gid))
						return -10;
				}
				for (short pid : t.pets)
				{
					if (!this.pets.containsKey(pid))
						return -11;
				}
				for (int j = i+1; j < teams.length; ++j)
				{
					SBean.DBRoleArenaFightTeam tt = teams[j];
					if (!Collections.disjoint(t.generals, tt.generals))
						return -12;
					if (!Collections.disjoint(t.pets, tt.pets))
						return -13;
				}
			}
		}
		return 0;
	}
	
	public void superArenaStartAttack(final int srank, final int trid, final int trank, SBean.DBRoleArenaFightTeam[] teams)
	{
		int now = gs.getTime();
		int error = superArenaTestCanStartAttack(now, teams);
		if (error != 0)
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaStartAttackRes(trid, error, 0, null));
			return;
		}
		
		final SBean.SuperArenaFightInfo self = new SBean.SuperArenaFightInfo();
		self.rid = this.id;
		self.headIconId = this.headIconID;
		self.name = this.name;
		self.lvl = this.lvl;	
		
		List<SBean.SuperArenaTeamDetail> selfteamsdetail = new ArrayList<SBean.SuperArenaTeamDetail>();
		for (int index = 0; index < teams.length; ++index)
		{
			SBean.DBRoleArenaFightTeam t = teams[index];
			SBean.SuperArenaTeamDetail teamDetail = superArenaFillTeamDetail(t);
			selfteamsdetail.add(teamDetail);
		}
		self.teams = selfteamsdetail;
		
		gs.getArenaManager().getSuperArena().startAttack(this.id, srank, trid, trank, new ArenaManager.SuperArena.StartAttackCallback() 
		{
			@Override
			public void onCallback(int errCode, SuperArenaFightInfo info) 
			{
				if (errCode == 0 && info == null)
					errCode = -1;
				int seed = GameData.getInstance().getRandom().nextInt();
				if (errCode == 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					
					synchronized (Role.this)
					{
						SBean.SuperArenaRecord record = new SBean.SuperArenaRecord();
						record.seed = seed;
						record.fteam1 = self;
						record.fteam2 = info;
						curSuperArenaAttack = record;
						superArena.lastFightTime = gs.getTime();
						superArena.timesUsed++;
						
						tlogEvent.addRecord(logDailyTask2(GameData.DAILYTASK2_TYPE_ARENA, 1));	
					}
					gs.getTLogger().logEvent(Role.this, tlogEvent);
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaStartAttackRes(trid, errCode, seed, new SBean.SuperArenaFightInfo[] {self, info}));
			}
		});
	}
	
	static boolean superArenaCompareFightInfo(SBean.SuperArenaFightInfo l, SBean.SuperArenaFightInfo r)
	{
		if (l.rid != r.rid || l.headIconId != r.headIconId || !l.name.equals(r.name) || l.lvl != r.lvl || l.teams.size() != r.teams.size())
			return false;
		for (int i = 0; i < l.teams.size(); ++i)
		{
			SBean.SuperArenaTeamDetail t1 = l.teams.get(i);
			SBean.SuperArenaTeamDetail t2 = r.teams.get(i);
			if (t1.generals.size() != t2.generals.size() || t1.pets.size() != t2.pets.size())
				return false;
			for (int j = 0; j < t1.generals.size(); ++j)
			{
				DBRoleGeneral g1 = t1.generals.get(j);
				DBRoleGeneral g2 = t2.generals.get(j);
				if (g1.id != g2.id)
					return false;
			}
			for (int j = 0; j < t1.pets.size(); ++j)
			{
				SBean.DBPetBrief p1 = t1.pets.get(j);
				SBean.DBPetBrief p2 = t2.pets.get(j);
				if (p1.id != p2.id)
					return false;
			}
		}
		return true;
	}
	
	static boolean superArenaCompareRecord(SBean.SuperArenaRecord l, SBean.SuperArenaRecord r)
	{
		if (l.seed != r.seed)
			return false;
		return superArenaCompareFightInfo(l.fteam1, r.fteam1) && superArenaCompareFightInfo(l.fteam2, r.fteam2);
	}
	
	synchronized int superArenaCheckFinishAttack(SBean.SuperArenaRecord record)
	{
		if (superArena == null)
			return -2;
		if (curSuperArenaAttack == null)
			return -3;
		if (!superArenaCompareRecord(curSuperArenaAttack, record))
			return -4;
		if (record.fteam1.rid != this.id)
			return -5;
		if (record.result.size() != 3)
			return -6;
		return 0;
	}
	
	public void superArenaFinishAttack(final SBean.SuperArenaRecord record)
	{
		int error = superArenaCheckFinishAttack(record);
		if (error != 0)
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaFinishAttackRes(record.fteam2.rid, error, 0, 0));
			return;
		}
		gs.getArenaManager().getSuperArena().finishAttack(record, new ArenaManager.SuperArena.FinishAttackCallback()
			{
				public void onCallback(int errCode, byte win, int rankUp)
				{
					int rewardPoint = 0;
					if (errCode == 0)
					{
						SBean.DBRoleArenaLog log = new SBean.DBRoleArenaLog(win, rankUp, record.time, record.fteam2.rid, record.id);
						SBean.SuperArenaCFGS cfg = gs.getGameData().getSuperArenaCFG();
						rewardPoint = win > 0 ? cfg.fightWinReward : cfg.fightLoseReward;
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
						synchronized( Role.this )
						{
							Role.this.superArenaAddLog(log);
							commonFlowRecord.addChange(Role.this.addSuperArena(rewardPoint));
//							superArena.lastFightTime = gs.getTime();
//							superArena.timesUsed++;
							if (win > 0){
								superArena.winTimes++;
								superArena.lastFightTime=0;
							}
								
						}
						commonFlowRecord.setEvent(TLog.AT_ARENA_FINISH);
						commonFlowRecord.setArg(1, 0);
						tlogEvent.addCommonFlow(commonFlowRecord);
						gs.getTLogger().logEvent(Role.this, tlogEvent);	
					}
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaFinishAttackRes(record.fteam2.rid, errCode, rankUp, rewardPoint));
				}
			});
	}
	
	public synchronized List<SBean.DBRoleArenaLog> getRecentSuperArenaLogs()
	{
		List<SBean.DBRoleArenaLog> lst = new ArrayList<SBean.DBRoleArenaLog>();
		if (superArena != null)
		{
			Iterator<SBean.DBRoleArenaLog> iter = superArena.logs.iterator();
			while( iter.hasNext() )
			{
				SBean.DBRoleArenaLog e = iter.next();
				if( e.time + 86400 < gs.getTime() )
				{
					iter.remove();
				}
				else
					lst.add(e.ksClone());
			}
		}
		return lst;
	}
	
	public void superArenaSyncLogs()
	{		
		final List<SBean.DBRoleArenaLog> rlogs = getRecentSuperArenaLogs();
		if (rlogs.isEmpty())
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaSyncLogs(new ArrayList<ArenaLog>()));
			return;
		}
		final List<Integer> rids = new ArrayList<Integer>();
		for(SBean.DBRoleArenaLog e : rlogs)
		{
			rids.add(e.id);
		}
		gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
			
			@Override
			public void onCallback(List<RoleBriefCachePair> lst)
			{
				Map<Integer, SBean.DBRoleBrief> rmap = new TreeMap<Integer, SBean.DBRoleBrief>();
				for(RoleBriefCachePair e : lst)
				{
					rmap.put(e.id, new SBean.DBRoleBrief(e.id, e.cache.lvl, e.cache.headIconID, e.cache.name, e.cache.fname, e.cache.score));
				}
				List<ArenaLog> logs = new ArrayList<ArenaLog>();
				for(SBean.DBRoleArenaLog e : rlogs)
				{
					SBean.DBRoleBrief b = rmap.get(e.id);
					if( b != null )
					{
						ArenaLog l = new ArenaLog();
						l.id = e.id;
						l.win = e.win;
						l.rankUp = e.rankUp;
						l.time = e.time;
						l.recordId = e.recordId;
						
						l.lvl = b.lvl;
						l.headIconID = b.headIconID;
						l.name = b.name;
						
						logs.add(l);
					}
				}
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuperArenaSyncLogs(logs));
			}
		});
	}
	
	public boolean superArenaClearCool()
	{
		if (superArena == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int stoneNeed = gs.getGameData().getSuperArenaCFG().stoneRefresh;
			if( stone < stoneNeed )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			superArena.lastFightTime = 0;
		}
		commonFlowRecord.setEvent(TLog.AT_ARENA_CLEAR_COOL);
		commonFlowRecord.setArg(1, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean superArenaBuyTimes()
	{
		if (superArena == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			short maxBuy = gs.getGameData().getArenaBuyTimesMax(lvlVIP);
			if( superArena.timesBuy >= maxBuy )
				return false;
			List<Short> price = gs.getGameData().getSuperArenaCFG().timesPrice;
			int index = superArena.timesBuy;
			if( index >= price.size() )
				index = price.size() - 1;
			int stoneNeed = price.get(index);
			if( stone < stoneNeed )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			superArena.timesBuy++;
		}
		commonFlowRecord.setEvent(TLog.AT_ARENA_BUY_TIMES);
		commonFlowRecord.setArg(superArena.timesBuy, 1);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized void superArenaAddRankReward(int reward)
	{
		if (reward <= 0 || superArena == null)
			return;
		if (superArena.rankRewards + reward >= GameData.MAX_POINT)
			superArena.rankRewards = GameData.MAX_POINT;
		else
			superArena.rankRewards += reward;
	}
	
	
	
	public static class SellMarketGoodsRes
	{
		public boolean bOK;
		public int price;
		public int taxAll;
		public boolean bLost;
	}
	
	public static class BuyMarketGoodsRes
	{
		public boolean bOK;
		public int price;
	}
	
	public CheckinSyncInfo checkinSync()
	{
		CheckinSyncInfo info = new CheckinSyncInfo();
		byte[] now = new byte[2];
		SBean.CheckinCFGS cfg = GameData.getInstance().getCheckinNow(gs.getTimeGMT(), now);
		if( cfg == null )
			return info;
		// TODO
		byte checkinID = now[0];
		byte dayNow = now[1];
		synchronized( this )
		{
			info.id = checkinID;
			if( info.id != checkinLog.lastID )
			{
				info.n = 0;
				info.bOK = true;
			}
			else
			{
				info.n = checkinLog.nFinished;
				info.bOK = checkinLog.lastDay < dayNow;
			}
		}
		return info;
	}
	
	public boolean checkinTake()
	{
		byte[] now = new byte[2];
		SBean.CheckinCFGS cfg = GameData.getInstance().getCheckinNow(gs.getTimeGMT(), now);
		if( cfg == null )
			return false;
		// TODO
		byte checkinID = now[0];
		byte dayNow = now[1];
		if( dayNow > cfg.rewards.size() )
			return false;
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			if( checkinID != checkinLog.lastID )
			{
				checkinLog.lastID = checkinID;
				checkinLog.nFinished = 0;
				checkinLog.lastDay = 0;
			}
			if( dayNow <= checkinLog.lastDay )
				return false;
			if( checkinLog.nFinished < 0 || checkinLog.nFinished >= cfg.rewards.size() )
				return false;
			
			SBean.CheckinRewardCFGS r = cfg.rewards.get(checkinLog.nFinished); 
			
			SBean.DropEntryNew dropReal = r.reward.kdClone();
			if( r.lvlVIP > 0 && lvlVIP >= r.lvlVIP )
				dropReal.arg *= 2;
			if( getQQVIP() != 0 && dropReal.type == GameData.COMMON_TYPE_STONE )
				dropReal.arg = (int)(dropReal.arg * 1.2f);
			record = addDropNew(dropReal);
			/*
			if( r.lvlVIP > 0 && lvlVIP >= r.lvlVIP )
				record = addDropNew(new SBean.DropEntryNew(r.reward.type, r.reward.id, r.reward.arg * 2));
			else
				record = addDropNew(r.reward);
			*/
			
			checkinLog.nFinished++;
			checkinLog.lastDay = dayNow;	
		}
		if (record != null)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			commonFlowRecord.setEvent(TLog.AT_CHECKIN);
			commonFlowRecord.setArg(checkinID, dayNow, checkinLog.nFinished);
			commonFlowRecord.addChange(record);
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(new TLogger.CheckinRecord(this.lvl, checkinID, dayNow));
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		return true;
	}

	List<Boolean> getGameActivityCanRewardStatus(List<GameActivities.ActivityDisplayConfig> cfgs)
	{
		List<Boolean> canRewards = new ArrayList<Boolean>();
		for (GameActivities.ActivityDisplayConfig cfg : cfgs)
		{
			canRewards.add(isGameActivityCanReward(cfg));
		}
		return canRewards;
	}
	
	boolean isGameActivityCanReward(GameActivities.ActivityDisplayConfig cfg)
	{
		boolean canReward = false;
		switch (cfg.getType())
		{
		case GameActivities.NEWUSERSYSMAIL:
		case GameActivities.LOGINGIFTMAIL:
		case GameActivities.DOUBLEDROP:
		case GameActivities.FREEPHYSICAL:
		case GameActivities.GIFTPACKAGE:
		case GameActivities.LIMITEDSHOP:
		case GameActivities.DISKBET:
			break;
		case GameActivities.CHECKINGIFT:
			{
				GameActivities.CheckinGiftConfig checkinGiftCfg = (GameActivities.CheckinGiftConfig)cfg;
				SBean.DBRoleCheckinGift cg = syncCheckinGiftState(checkinGiftCfg);
				canReward = canTakeCheckinGiftReward(checkinGiftCfg, cg, gs.getTime());
			}
			break;
		case GameActivities.FIRSTPAYGIFT:
			{
				GameActivities.FirstPayGiftConfig paygiftCfg = (GameActivities.FirstPayGiftConfig)cfg;
				SBean.DBRoleFirstPayGift pg = syncFirstPayGiftState(paygiftCfg);
				canReward = canTakeFirstPayGiftReward(paygiftCfg, pg, gs.getTime());
			}
			break;
		case GameActivities.CONSUMEGIFT:
			{
				GameActivities.ConsumeGiftConfig consumeGiftCfg = (GameActivities.ConsumeGiftConfig)cfg;
				SBean.DBRoleConsumeGift cg = syncConsumeGiftState(consumeGiftCfg);
				canReward = canTakeConsumeGiftReward(consumeGiftCfg, cg, gs.getTime());
			}
			break;
		case GameActivities.CONSUMEREBATE:
			{
				GameActivities.ConsumeRebateConfig consumeRebateCfg = (GameActivities.ConsumeRebateConfig)cfg;
				SBean.DBRoleConsumeRebate cr = syncConsumeRebateState(consumeRebateCfg);
				canReward = canTakeConsumeRebateReward(consumeRebateCfg, cr, gs.getTime());
			}
			break;
		case GameActivities.EXCHANGEGIFT:
			{
				GameActivities.ExchangeGiftConfig exchangeGiftCfg = (GameActivities.ExchangeGiftConfig)cfg;
				canReward = canTakeExchangeGiftReward(exchangeGiftCfg, gs.getTime());
			}
			break;
		case GameActivities.UPGRADEGIFT:
			{
				GameActivities.UpgradeGiftConfig upgradeGiftCfg = (GameActivities.UpgradeGiftConfig)cfg;
				SBean.DBRoleUpgradeGift ug = syncUpgradeGiftState(upgradeGiftCfg);
				canReward = canTakeUpgradeGiftReward(upgradeGiftCfg, ug, gs.getTime());
			}
			break;
		case GameActivities.GATHERGIFT:
			{
				GameActivities.GatherGiftConfig gatherGiftCfg = (GameActivities.GatherGiftConfig)cfg;
				SBean.DBRoleGatherGift gg = syncGatherGiftState(gatherGiftCfg);
				canReward = canTakeGatherGiftReward(gatherGiftCfg, gg, gs.getTime());
			}
			break;
		case GameActivities.PAYGIFT:
			{
				GameActivities.PayGiftConfig payGiftCfg = (GameActivities.PayGiftConfig)cfg;
				SBean.DBRolePayGift cg = syncPayGiftState(payGiftCfg);
				canReward = canTakePayGiftReward(payGiftCfg, cg, gs.getTime());
			}
			break;
		case GameActivities.DISKGIFT:
		{
			GameActivities.DiskGiftConfig payGiftCfg = (GameActivities.DiskGiftConfig)cfg;
			DBRoleDiskGift cg = syncDiskGiftState(payGiftCfg);
			canReward = canTakeDiskGiftReward(payGiftCfg, cg, gs.getTime());
		}
		break;	
		case GameActivities.LOGINGIFT:
			{
				GameActivities.LoginGiftConfig loginGiftCfg = (GameActivities.LoginGiftConfig)cfg;
				SBean.DBRoleLoginGift cg = syncLoginGiftState(loginGiftCfg);
				canReward = canTakeLoginGiftReward(loginGiftCfg, cg, gs.getTime());
			}
			break;
		case GameActivities.TRADINGCENTER:
			{
				GameActivities.TradingCenterConfig tradingCenterCfg = (GameActivities.TradingCenterConfig)cfg;
				SBean.DBRoleTradingCenter tc = syncTradingCenterState(tradingCenterCfg);
				canReward = canTakeTradingCenterReward(tradingCenterCfg, tc, gs.getTime());
			}
			break;
		default:
			break;
		}
		return canReward;
	}
	
	List<Boolean> getGameActivityAlreadyTakeAllRewardStatus(List<GameActivities.ActivityDisplayConfig> cfgs)
	{
		List<Boolean> alreadyTakeAllRewards = new ArrayList<Boolean>();
		for (GameActivities.ActivityDisplayConfig cfg : cfgs)
		{
			alreadyTakeAllRewards.add(isGameActivityAlreadyTakeAllReward(cfg));
		}
		return alreadyTakeAllRewards;
	}
	
	boolean isGameActivityAlreadyTakeAllReward(GameActivities.ActivityDisplayConfig cfg)
	{
		boolean alreadyTakeAllReward = false;
		switch (cfg.getType())
		{
		case GameActivities.NEWUSERSYSMAIL:
		case GameActivities.LOGINGIFTMAIL:
		case GameActivities.DOUBLEDROP:
		case GameActivities.FREEPHYSICAL:
		case GameActivities.GIFTPACKAGE:
		case GameActivities.EXCHANGEGIFT:
			break;
		case GameActivities.CHECKINGIFT:
			{
				GameActivities.CheckinGiftConfig checkinGiftCfg = (GameActivities.CheckinGiftConfig)cfg;
				SBean.DBRoleCheckinGift cg = syncCheckinGiftState(checkinGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllCheckinGiftReward(checkinGiftCfg, cg);
			}
			break;
		case GameActivities.FIRSTPAYGIFT:
			{
				GameActivities.FirstPayGiftConfig paygiftCfg = (GameActivities.FirstPayGiftConfig)cfg;
				SBean.DBRoleFirstPayGift pg = syncFirstPayGiftState(paygiftCfg);
				alreadyTakeAllReward = alreadyTakeAllFirstPayGiftReward(paygiftCfg, pg);
			}
			break;
		case GameActivities.CONSUMEGIFT:
			{
				GameActivities.ConsumeGiftConfig consumeGiftCfg = (GameActivities.ConsumeGiftConfig)cfg;
				SBean.DBRoleConsumeGift cg = syncConsumeGiftState(consumeGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllConsumeGiftReward(consumeGiftCfg, cg);
			}
			break;
		case GameActivities.CONSUMEREBATE:
			{
				GameActivities.ConsumeRebateConfig consumeRebateCfg = (GameActivities.ConsumeRebateConfig)cfg;
				SBean.DBRoleConsumeRebate cr = syncConsumeRebateState(consumeRebateCfg);
				alreadyTakeAllReward = alreadyTakeAllConsumeRebateReward(consumeRebateCfg, cr);
			}
			break;
		case GameActivities.LIMITEDSHOP:
			{
				GameActivities.LimitedShopConfig limitedShopCfg = (GameActivities.LimitedShopConfig)cfg;
				SBean.DBRoleLimitedShop ls = syncLimitedShopState(limitedShopCfg);
				alreadyTakeAllReward = alreadyTakeAllLimitedShopReward(limitedShopCfg, ls);
			}
			break;
		case GameActivities.UPGRADEGIFT:
			{
				GameActivities.UpgradeGiftConfig upgradeGiftCfg = (GameActivities.UpgradeGiftConfig)cfg;
				SBean.DBRoleUpgradeGift ug = syncUpgradeGiftState(upgradeGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllUpgradeGiftReward(upgradeGiftCfg, ug);
			}
			break;
		case GameActivities.GATHERGIFT:
			{
				GameActivities.GatherGiftConfig gatherGiftCfg = (GameActivities.GatherGiftConfig)cfg;
				SBean.DBRoleGatherGift gg = syncGatherGiftState(gatherGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllGatherGiftReward(gatherGiftCfg, gg);
			}
			break;
		case GameActivities.PAYGIFT:
			{
				GameActivities.PayGiftConfig payGiftCfg = (GameActivities.PayGiftConfig)cfg;
				SBean.DBRolePayGift cg = syncPayGiftState(payGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllPayGiftReward(payGiftCfg, cg);
			}
			break;
		case GameActivities.DISKGIFT:
		{
			GameActivities.DiskGiftConfig payGiftCfg = (GameActivities.DiskGiftConfig)cfg;
			SBean.DBRoleDiskGift cg = syncDiskGiftState(payGiftCfg);
			alreadyTakeAllReward = alreadyTakeAllDiskGiftReward(payGiftCfg, cg);
		}
		break;	
		case GameActivities.LOGINGIFT:
			{
				GameActivities.LoginGiftConfig loginGiftCfg = (GameActivities.LoginGiftConfig)cfg;
				SBean.DBRoleLoginGift cg = syncLoginGiftState(loginGiftCfg);
				alreadyTakeAllReward = alreadyTakeAllLoginGiftReward(loginGiftCfg, cg);
			}
			break;
		case GameActivities.TRADINGCENTER:
			{
				GameActivities.TradingCenterConfig tradingCenterCfg = (GameActivities.TradingCenterConfig)cfg;
				SBean.DBRoleTradingCenter tc = syncTradingCenterState(tradingCenterCfg);
				alreadyTakeAllReward = alreadyTakeAllTradingCenterReward(tradingCenterCfg, tc);
			}
			break;
		default:
			break;
		}
		return alreadyTakeAllReward;
	}
	
	SBean.DBRoleCheckinGift syncCheckinGiftState(GameActivities.CheckinGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleCheckinGift cg = this.checkinGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRoleCheckinGift();
			cg.id = id;
			cg.checkinDay = 0;
			cg.lastCheckinTime = 0;
			this.checkinGift.put(id, cg);
		}
		return cg;
	}
	
	public synchronized SBean.DBRoleCheckinGift checkinGiftSync(GameActivities.CheckinGiftConfig cfg)
	{
		SBean.DBRoleCheckinGift cg = syncCheckinGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	boolean canTakeCheckinGiftReward(GameActivities.CheckinGiftConfig cfg, SBean.DBRoleCheckinGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		int lastCheckinDayIndex = cfg.getTimeDayIndex(cg.lastCheckinTime);
		int curCheckinDayIndex = cfg.getTimeDayIndex(curTime);
		if (curCheckinDayIndex <= lastCheckinDayIndex)
			return false;
		int days = cfg.getDays();
		if (curCheckinDayIndex < 0 || curCheckinDayIndex >= days)
			return false;
		if (cg.checkinDay + 1 > days)
			return false;
		int lvlReq = cfg.getDayGiftLevelReq(cg.checkinDay);
		if (this.lvl < lvlReq)
			return false;
		return true;
	}
	
	boolean alreadyTakeAllCheckinGiftReward(GameActivities.CheckinGiftConfig cfg, SBean.DBRoleCheckinGift cg)
	{
		if (cg == null)
			return false;
		int days = cfg.getDays();
		return cg.checkinDay == days;
	}
	
	public boolean takeCheckinGiftReward(GameActivities.CheckinGiftConfig cfg, int id, int daySeq)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			int curTime = gs.getTime();
			SBean.DBRoleCheckinGift cg = syncCheckinGiftState(cfg);
			if (!canTakeCheckinGiftReward(cfg, cg, curTime))
				return false;
			if (cg.id != id || daySeq != cg.checkinDay + 1)
				return false;
			for (GameActivities.GiftItem gi : cfg.getDayGift(daySeq-1))
			{
				commonFlowRecord.addChange(addDropNew(gi.item));
				if (gi.doubleCountLvl > 0 && this.lvlVIP >= gi.doubleCountLvl)
					commonFlowRecord.addChange(addDropNew(gi.item));
			}
			cg.lastCheckinTime = curTime;
			cg.checkinDay += 1;
		}
		commonFlowRecord.setEvent(TLog.AT_CEHCKIN_GIFT);
		commonFlowRecord.setArg(id, daySeq);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	SBean.DBRoleFirstPayGift syncFirstPayGiftState(GameActivities.FirstPayGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleFirstPayGift pg = this.firstPayGift.get(id);
		if (pg == null)
		{
			pg = new SBean.DBRoleFirstPayGift();
			pg.id = id;
			pg.pay = 0;
			pg.reward = 0;
			this.firstPayGift.put(id, pg);
		}
		return pg;
	}
	public boolean canTakeFirstPayGiftReward(GameActivities.FirstPayGiftConfig cfg, SBean.DBRoleFirstPayGift pg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (pg == null)
			return false;
		if (pg.pay <= 0 || pg.reward != 0)
			return false;
		return true;
	}
	
	public boolean alreadyTakeAllFirstPayGiftReward(GameActivities.FirstPayGiftConfig cfg, SBean.DBRoleFirstPayGift pg)
	{
		if (pg == null)
			return false;
		return pg.reward != 0;
	}
	
	public boolean takeFirstPayGiftReward(GameActivities.FirstPayGiftConfig cfg, int id)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleFirstPayGift pg = syncFirstPayGiftState(cfg);
			if (!canTakeFirstPayGiftReward(cfg, pg, gs.getTime()))
				return false;
			if (pg.id != id)
				return false;
			for (SBean.DropEntryNew e : cfg.getGift())
			{
				commonFlowRecord.addChange(addDropNew(e));
			}
			pg.reward = 1;
		}
		commonFlowRecord.setEvent(TLog.AT_FIRST_PAY_GIFT);
		commonFlowRecord.setArg(id);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized SBean.DBRoleFirstPayGift firstPayGiftSync(GameActivities.FirstPayGiftConfig cfg)
	{
		SBean.DBRoleFirstPayGift pg = syncFirstPayGiftState(cfg);
		if (pg == null)
			return null;
		return pg.kdClone();
	}
	
	public void firstPayGiftLogPay(GameActivities.FirstPayGiftConfig cfg, int pay)
	{
		SBean.DBRoleFirstPayGift pg = syncFirstPayGiftState(cfg);
		if (pg == null)
			return;
		pg.pay += pay;
	}
	
	public void firstPayGiftLogPay(int pay)
	{
		List<GameActivities.FirstPayGiftConfig> cfgs = gs.getGameActivities().getFirstPayGiftActivity().getOpenedConfigs();
		for (GameActivities.FirstPayGiftConfig cfg : cfgs)
		{
			firstPayGiftLogPay(cfg, pay);
		}
	}
	
	SBean.DBRolePayGift syncPayGiftState(GameActivities.PayGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRolePayGift cg = this.payGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRolePayGift();
			cg.id = id;
			cg.pay = 0;
			cg.reward = new ArrayList<Integer>();
			this.payGift.put(id, cg);
		}
		return cg;
	}
	
	public synchronized SBean.DBRolePayGift payGiftSync(GameActivities.PayGiftConfig cfg)
	{
		SBean.DBRolePayGift cg = syncPayGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	DBRoleDiskGift syncDiskGiftState(GameActivities.DiskGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleDiskGift cg = this.diskGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRoleDiskGift();
			cg.id = id;
			cg.pay = 0;
			cg.reward = new ArrayList<Integer>();
			this.diskGift.put(id, cg);
		}
		
		DiskBetConfig cfgd = gs.getGameActivities().getDiskBetActivity().getOpenedConfigById(id);
		if (cfgd == null)
			return null;
		if(diskBet==null||diskBet.id!=id){
			diskBet = new SBean.DBDiskBetInfo();
			diskBet.id = id;
			DBDiskBet diskBet2 = gs.getDiskManager().diskBet;
			if(diskBet2!=null&&(cfgd.start!=diskBet2.statime||diskBet2.endtime != cfgd.end)){
				diskBet2.statime = cfgd.start;
				diskBet2.endtime = cfgd.end;
				diskBet2.minscore = cfgd.MIN_SCORE;
				diskBet2.minRmb = cfgd.MIN_RMB;
				gs.getDiskManager().mailatt3=cfgd.mailatt3;
				gs.getDiskManager().mailatt4=cfgd.mailatt4;
				gs.getDiskManager().mailatt5=cfgd.mailatt5;
				if(diskBet2.ranksHis!=null){
					diskBet2.ranksHis.clear();	
				}				
			}
			
		}
		if(diskBet!=null){
			cg.pay = diskBet.score;
		}
		return cg;
	}
	
	public synchronized DBRoleDiskGift diskGiftSync(GameActivities.DiskGiftConfig cfg)
	{
		DBRoleDiskGift cg = syncDiskGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	public boolean canTakeDiskGiftReward(GameActivities.DiskGiftConfig cfg, DBRoleDiskGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		for (GameActivities.DiskLvlGift clg : cfg.getDiskLvlGift())
		{
			if (cg.pay >= clg.payReq && !cg.reward.contains(clg.payReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllDiskGiftReward(GameActivities.DiskGiftConfig cfg, DBRoleDiskGift cg)
	{
		if (cg == null)
			return false;
		for (DiskLvlGift clg : cfg.getDiskLvlGift())
		{
			if (!cg.reward.contains(clg.payReq))
				return false;
		}
		return true;
	}
	
	DBRoleRechargeGift syncRechargeGiftState(GameActivities.RechargeGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleRechargeGift cg = this.rechargeGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRoleRechargeGift();
			cg.id = id;
			cg.pay = 0;
			cg.reward = new ArrayList<Integer>();
			this.rechargeGift.put(id, cg);
		}
		
		if(diskBet2!=null){
			cg.pay = diskBet2.rmb;
		}
		return cg;
	}
	public synchronized DBRoleRechargeGift diskRechargeSync(GameActivities.RechargeGiftConfig cfg)
	{
		DBRoleRechargeGift cg = syncRechargeGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	public boolean canTakeRechargeGiftReward(GameActivities.RechargeGiftConfig cfg, DBRoleRechargeGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		for (GameActivities.RechargeLvlGift clg : cfg.getRechargeLvlGift())
		{
			if (cg.pay >= clg.payReq && !cg.reward.contains(clg.payReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllRechargeGiftReward(GameActivities.RechargeGiftConfig cfg, DBRoleRechargeGift cg)
	{
		if (cg == null)
			return false;
		for (RechargeLvlGift clg : cfg.getRechargeLvlGift())
		{
			if (!cg.reward.contains(clg.payReq))
				return false;
		}
		return true;
	}
	
	public boolean canTakePayGiftReward(GameActivities.PayGiftConfig cfg, SBean.DBRolePayGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		for (GameActivities.PayLvlGift clg : cfg.getPayLvlGift())
		{
			if (cg.pay >= clg.payReq && !cg.reward.contains(clg.payReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllPayGiftReward(GameActivities.PayGiftConfig cfg, SBean.DBRolePayGift cg)
	{
		if (cg == null)
			return false;
		for (GameActivities.PayLvlGift clg : cfg.getPayLvlGift())
		{
			if (!cg.reward.contains(clg.payReq))
				return false;
		}
		return true;
	}
	
	public boolean takeDiskGiftReward(DiskGiftConfig cfg, int id, int pay)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			DBRoleDiskGift cg = syncDiskGiftState(cfg);
			if (cg.id != id)
				return false;
			if (!canTakeDiskGiftReward(cfg, cg, gs.getTime()))
				return false;
			DiskLvlGift clg = cfg.getPayLvlGift(pay);
			if (clg == null)
				return false;
			if(diskBet==null){
				return false;
			}
			
			if(diskBet.id!=id){
				return false;
			}
			
			if(diskBet.score<=0){
				return false;
			}
			
			if (diskBet.score < clg.payReq || cg.reward.contains(pay))//cg.pay
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(clg.gift));
			cg.reward.add(pay);
		}
		commonFlowRecord.setEvent(TLog.AT_PAY_GIFT);
		commonFlowRecord.setArg(id, pay);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	
	public boolean takeRechargeGiftReward(RechargeGiftConfig cfg, int id, int pay)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			DBRoleRechargeGift cg = syncRechargeGiftState(cfg);
			if (cg.id != id)
				return false;
			if (!canTakeRechargeGiftReward(cfg, cg, gs.getTime()))
				return false;
			RechargeLvlGift clg = cfg.getPayLvlGift(pay);
			if (clg == null)
				return false;
			
			if (diskBet3.rmb < clg.payReq || cg.reward.contains(pay))//cg.pay
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(clg.gift));
			cg.reward.add(pay);
		}
		commonFlowRecord.setEvent(TLog.AT_PAY_GIFT);
		commonFlowRecord.setArg(id, pay);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean takePayGiftReward(GameActivities.PayGiftConfig cfg, int id, int pay)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRolePayGift cg = syncPayGiftState(cfg);
			if (cg.id != id)
				return false;
			if (!canTakePayGiftReward(cfg, cg, gs.getTime()))
				return false;
			GameActivities.PayLvlGift clg = cfg.getPayLvlGift(pay);
			if (clg == null)
				return false;
			if (cg.pay < clg.payReq || cg.reward.contains(pay))
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(clg.gift));
			cg.reward.add(pay);
		}
		commonFlowRecord.setEvent(TLog.AT_PAY_GIFT);
		commonFlowRecord.setArg(id, pay);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public void payGiftLogPay(GameActivities.PayGiftConfig cfg, int pay)
	{
		SBean.DBRolePayGift cg = syncPayGiftState(cfg);
		if (cg == null)
			return;
		cg.pay += pay;
	}
	
	public void payGiftLogPay(int pay)
	{
		List<GameActivities.PayGiftConfig> cfgs = gs.getGameActivities().getPayGiftActivity().getOpenedConfigs();
		for (GameActivities.PayGiftConfig cfg : cfgs)
		{
			payGiftLogPay(cfg, pay);
		}
	}
	
	
	SBean.DBRoleConsumeGift syncConsumeGiftState(GameActivities.ConsumeGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleConsumeGift cg = this.consumeGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRoleConsumeGift();
			cg.id = id;
			cg.consume = 0;
			cg.reward = new ArrayList<Integer>();
			this.consumeGift.put(id, cg);
		}
		return cg;
	}
	
	public synchronized SBean.DBRoleConsumeGift consumeGiftSync(GameActivities.ConsumeGiftConfig cfg)
	{
		SBean.DBRoleConsumeGift cg = syncConsumeGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	public boolean canTakeConsumeGiftReward(GameActivities.ConsumeGiftConfig cfg, SBean.DBRoleConsumeGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		for (GameActivities.ConsumeLvlGift clg : cfg.getConsumeLvlGift())
		{
			if (cg.consume >= clg.consumeReq && !cg.reward.contains(clg.consumeReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllConsumeGiftReward(GameActivities.ConsumeGiftConfig cfg, SBean.DBRoleConsumeGift cg)
	{
		if (cg == null)
			return false;
		for (GameActivities.ConsumeLvlGift clg : cfg.getConsumeLvlGift())
		{
			if (!cg.reward.contains(clg.consumeReq))
				return false;
		}
		return true;
	}
	
	public boolean takeConsumeGiftReward(GameActivities.ConsumeGiftConfig cfg, int id, int consume)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleConsumeGift cg = syncConsumeGiftState(cfg);
			if (cg.id != id)
				return false;
			if (!canTakeConsumeGiftReward(cfg, cg, gs.getTime()))
				return false;
			GameActivities.ConsumeLvlGift clg = cfg.getConsumeLvlGift(consume);
			if (clg == null)
				return false;
			if (cg.consume < clg.consumeReq || cg.reward.contains(consume))
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(clg.gift));
			cg.reward.add(consume);
		}
		commonFlowRecord.setEvent(TLog.AT_CONSUME_GIFT);
		commonFlowRecord.setArg(id, consume);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public void consumeGiftLogConsume(GameActivities.ConsumeGiftConfig cfg, int consume)
	{
		SBean.DBRoleConsumeGift cg = syncConsumeGiftState(cfg);
		if (cg == null)
			return;
		cg.consume += consume;
	}
	
	public void consumeGiftLogConsume(int consume, boolean consumeMoney)
	{
		List<GameActivities.ConsumeGiftConfig> cfgs = gs.getGameActivities().getConsumeGiftActivity().getOpenedConfigs();
		for (GameActivities.ConsumeGiftConfig cfg : cfgs)
		{
			if (consumeMoney)
			{
				if (cfg.isConsumeMoney())
					consumeGiftLogConsume(cfg, consume);	
			}
			else
			{
				if (!cfg.isConsumeMoney())
					consumeGiftLogConsume(cfg, consume); 
			}
		}
	}
	
	SBean.DBRoleConsumeRebate syncConsumeRebateState(GameActivities.ConsumeRebateConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleConsumeRebate cr = this.consumeRebate.get(id);
		if (cr == null)
		{
			cr = new SBean.DBRoleConsumeRebate();
			cr.id = id;
			cr.consume = 0;
			cr.reward = new ArrayList<Integer>();
			this.consumeRebate.put(id, cr);
		}
		return cr;
	}
	
	public synchronized SBean.DBRoleConsumeRebate consumeRebateSync(GameActivities.ConsumeRebateConfig cfg)
	{
		SBean.DBRoleConsumeRebate cr = syncConsumeRebateState(cfg);
		if (cr == null)
			return null;
		return cr.kdClone();
	}
	
	public boolean canTakeConsumeRebateReward(GameActivities.ConsumeRebateConfig cfg, SBean.DBRoleConsumeRebate cr, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cr == null)
			return false;
		for (GameActivities.ConsumeLvlRebate clg : cfg.getConsumeLvlRebate())
		{
			if (cr.consume >= clg.consumeReq && !cr.reward.contains(clg.consumeReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllConsumeRebateReward(GameActivities.ConsumeRebateConfig cfg, SBean.DBRoleConsumeRebate cr)
	{
		if (cr == null)
			return false;
		for (GameActivities.ConsumeLvlRebate clg : cfg.getConsumeLvlRebate())
		{
			if (!cr.reward.contains(clg.consumeReq))
				return false;
		}
		return true;
	}
	
	public boolean takeConsumeRebateReward(GameActivities.ConsumeRebateConfig cfg, int id, int consume)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleConsumeRebate cr = syncConsumeRebateState(cfg);
			if (cr.id != id)
				return false;
			if (!canTakeConsumeRebateReward(cfg, cr, gs.getTime()))
				return false;
			GameActivities.ConsumeLvlRebate clr = cfg.getConsumeLvlRebate(consume);
			if (clr == null)
				return false;
			if (cr.consume < clr.consumeReq || cr.reward.contains(consume))
				return false;
			commonFlowRecord.addChange(this.addStonePresent(clr.rebate));
			cr.reward.add(consume);
		}
		commonFlowRecord.setEvent(TLog.AT_CONSUME_REBATE);
		commonFlowRecord.setArg(id, consume);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public void consumeRebateLogConsume(GameActivities.ConsumeRebateConfig cfg, int consume)
	{
		SBean.DBRoleConsumeRebate cr = syncConsumeRebateState(cfg);
		if (cr == null)
			return;
		cr.consume += consume;
	}
	
	public void consumeRebateLogConsume(int consume)
	{
		List<GameActivities.ConsumeRebateConfig> cfgs = gs.getGameActivities().getConsumeRebateActivity().getOpenedConfigs();
		for (GameActivities.ConsumeRebateConfig cfg : cfgs)
		{
			consumeRebateLogConsume(cfg, consume);
		}
	}
	
	SBean.DBRoleUpgradeGift syncUpgradeGiftState(GameActivities.UpgradeGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleUpgradeGift ug = this.upgradeGift.get(id);
		if (ug == null)
		{
			ug = new SBean.DBRoleUpgradeGift();
			ug.id = id;
			ug.reward = new ArrayList<Integer>();
			this.upgradeGift.put(id, ug);
		}
		return ug;
	}
	
	public synchronized SBean.DBRoleUpgradeGift upgradeGiftSync(GameActivities.UpgradeGiftConfig cfg)
	{
		SBean.DBRoleUpgradeGift ug = syncUpgradeGiftState(cfg);
		if (ug == null)
			return null;
		return ug.kdClone();
	}
	
	public boolean canTakeUpgradeGiftReward(GameActivities.UpgradeGiftConfig cfg, SBean.DBRoleUpgradeGift ug, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (ug == null)
			return false;
		for (GameActivities.UpgradeLvlGift ulg : cfg.getUpgradeLvlGift())
		{
			if (this.lvl >= ulg.lvlReq && !ug.reward.contains(ulg.lvlReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllUpgradeGiftReward(GameActivities.UpgradeGiftConfig cfg, SBean.DBRoleUpgradeGift ug)
	{
		if (ug == null)
			return false;
		for (GameActivities.UpgradeLvlGift ulg : cfg.getUpgradeLvlGift())
		{
			if (!ug.reward.contains(ulg.lvlReq))
				return false;
		}
		return true;
	}
	
	public boolean takeUpgradeGiftReward(GameActivities.UpgradeGiftConfig cfg, int id, int level)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleUpgradeGift ug = syncUpgradeGiftState(cfg);
			if (ug.id != id)
				return false;
			if (!canTakeUpgradeGiftReward(cfg, ug, gs.getTime()))
				return false;
			GameActivities.UpgradeLvlGift ulg = cfg.getUpgradeLvlGift(level);
			if (ulg == null)
				return false;
			if (this.lvl < ulg.lvlReq || ug.reward.contains(level))
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(ulg.gift));
			if (cfg.isInLimitedTime(gs.getTime()))
			{
				commonFlowRecord.addChanges(this.addDropsNew(ulg.giftEx));
			}
			ug.reward.add(level);
		}
		commonFlowRecord.setEvent(TLog.AT_UPGRADE_GIFT);
		commonFlowRecord.setArg(id, level);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean canTakeExchangeGiftReward(GameActivities.ExchangeGiftConfig cfg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		for (GameActivities.PropsGift pg : cfg.getExchangeGift())
		{
			if (haveDropsNew(pg.props))
				return true;
		}
		return false;
	}
	
	public boolean takeExchangeGiftReward(GameActivities.ExchangeGiftConfig cfg, int id, int seq)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			if (!canTakeExchangeGiftReward(cfg, gs.getTime()))
				return false;
			GameActivities.PropsGift pg = cfg.getPropsGift(seq-1);
			if (pg == null)
				return false;
			if (!haveDropsNew(pg.props))
				return false;
			commonFlowRecord.addChanges(this.delDropsNew(pg.props));
			commonFlowRecord.addChange(this.addDropNew(pg.gift));
		}
		commonFlowRecord.setEvent(TLog.AT_EXCHANGE_GIFT);
		commonFlowRecord.setArg(id, seq);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	
	SBean.DBRoleGatherGift syncGatherGiftState(GameActivities.GatherGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleGatherGift gg = this.gatherGift.get(id);
		if (gg == null)
		{
			gg = new SBean.DBRoleGatherGift();
			gg.id = id;
			gg.reward = new ArrayList<Integer>();
			this.gatherGift.put(id, gg);
		}
		return gg;
	}
	
	public synchronized SBean.DBRoleGatherGift gatherGiftSync(GameActivities.GatherGiftConfig cfg)
	{
		SBean.DBRoleGatherGift gg = syncGatherGiftState(cfg);
		if (gg == null)
			return null;
		return gg.kdClone();
	}
	
	public boolean canTakeGatherGiftReward(GameActivities.GatherGiftConfig cfg, SBean.DBRoleGatherGift gg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (gg == null)
			return false;
		int seq = 1;
		for (GameActivities.GeneralGift ulg : cfg.getGatherGift())
		{
			if (haveGenerals(ulg.generals) && !gg.reward.contains(seq))
			{
				return true;
			}
			++seq;
		}
		return false;
	}
	
	public boolean alreadyTakeAllGatherGiftReward(GameActivities.GatherGiftConfig cfg, SBean.DBRoleGatherGift gg)
	{
		if (gg == null)
			return false;
		int seq = 1;
		for (Iterator<GeneralGift> iterator = cfg.getGatherGift().iterator(); iterator
				.hasNext();) {
			iterator.next();
			if (!gg.reward.contains(seq))
			{
				return false;
			}
			++seq;
		}
		return true;
	}
	
	public boolean takeGatherGiftReward(GameActivities.GatherGiftConfig cfg, int id, int seq)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleGatherGift gg = syncGatherGiftState(cfg);
			if (gg.id != id)
				return false;
			if (!canTakeGatherGiftReward(cfg, gg, gs.getTime()))
				return false;
			GameActivities.GeneralGift ulg = cfg.getGeneralGift(seq-1);
			if (ulg == null)
				return false;
			if (!haveGenerals(ulg.generals) || gg.reward.contains(seq))
				return false;
			commonFlowRecord.addChange(this.addDropNew(ulg.gift));
			gg.reward.add(seq);
		}
		commonFlowRecord.setEvent(TLog.AT_GATHER_GIFT);
		commonFlowRecord.setArg(id, seq);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	SBean.DBRoleLimitedShop syncLimitedShopState(GameActivities.LimitedShopConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleLimitedShop ls = this.limitedShop.get(id);
		if (ls == null)
		{
			ls = new SBean.DBRoleLimitedShop();
			ls.id = id;
			ls.buglogs = new ArrayList<SBean.DBGiftBuyLog>();
			this.limitedShop.put(id, ls);
		}
		return ls;
	}
	
	public synchronized SBean.DBRoleLimitedShop limitedShopSync(GameActivities.LimitedShopConfig cfg)
	{
		SBean.DBRoleLimitedShop ls = syncLimitedShopState(cfg);
		if (ls == null)
			return null;
		return ls.kdClone();
	}
	
	static int getLimitedShopGoodsBuyCount(SBean.DBRoleLimitedShop ls, int id)
	{
		for (SBean.DBGiftBuyLog log : ls.buglogs)
		{
			if (log.id == id)
			{
				return log.count;
			}
		}
		return 0;
	}
	
	static void addLimitedShopGoodsBuyCount(SBean.DBRoleLimitedShop ls, int id,int count)
	{
		for (SBean.DBGiftBuyLog log : ls.buglogs)
		{
			if (log.id == id)
			{
				log.count+=count;
				return;
			}
		}
		ls.buglogs.add(new SBean.DBGiftBuyLog(id, count));
	}
	
	public boolean canTakeLimitedShopReward(GameActivities.LimitedShopConfig cfg, SBean.DBRoleLimitedShop ls, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (ls == null)
			return false;
		int seq = 1;
		for (GameActivities.LimitedGift lg : cfg.getLimitedGift())
		{
			if (this.lvlVIP >= lg.vipReq && this.stone >= lg.cost && (lg.maxBuyTimes < 0 || getLimitedShopGoodsBuyCount(ls, seq) < lg.maxBuyTimes))
			{
				return true;
			}
			++seq;
		}
		return false;
	}
	
	public boolean alreadyTakeAllLimitedShopReward(GameActivities.LimitedShopConfig cfg, SBean.DBRoleLimitedShop ls)
	{
		if (ls == null)
			return false;
		int seq = 1;
		for (GameActivities.LimitedGift lg : cfg.getLimitedGift())
		{
			if (lg.maxBuyTimes < 0 || getLimitedShopGoodsBuyCount(ls, seq) < lg.maxBuyTimes)
			{
				return false;
			}
			++seq;
		}
		return true;
	}

	public boolean takeLimitedShopReward(GameActivities.LimitedShopConfig cfg, int id, int seq, int count)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleLimitedShop ls = syncLimitedShopState(cfg);
			if (ls.id != id)
				return false;
			if (!canTakeLimitedShopReward(cfg, ls, gs.getTime()))
				return false;
			GameActivities.LimitedGift lg = cfg.getLimitedGift(seq-1);
			if (lg == null)
				return false;
			if (this.lvlVIP < lg.vipReq || this.stone < lg.cost*count || (lg.maxBuyTimes > 0 && (getLimitedShopGoodsBuyCount(ls, seq)+count-1) >= lg.maxBuyTimes))
				return false;
			commonFlowRecord.addChange(this.useStone(lg.cost*count));
			for(int i =0;i<count;i++){
				commonFlowRecord.addChanges(this.addDropsNew(lg.gift));
			}			
			addLimitedShopGoodsBuyCount(ls, seq,count);
		}
		commonFlowRecord.setEvent(TLog.AT_LIMITED_GIFT);
		commonFlowRecord.setArg(id, seq);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	
	SBean.DBRoleLoginGift syncLoginGiftState(GameActivities.LoginGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleLoginGift cg = this.loginGift.get(id);
		if (cg == null)
		{
			cg = new SBean.DBRoleLoginGift();
			cg.id = id;
			cg.loginDays = 0;
			cg.lastLoginTime = 0;
			cg.reward = new ArrayList<Integer>();
			this.loginGift.put(id, cg);
		}
		return cg;
	}
	
	public synchronized SBean.DBRoleLoginGift loginGiftSync(GameActivities.LoginGiftConfig cfg)
	{
		SBean.DBRoleLoginGift cg = syncLoginGiftState(cfg);
		if (cg == null)
			return null;
		return cg.kdClone();
	}
	
	public boolean canTakeLoginGiftReward(GameActivities.LoginGiftConfig cfg, SBean.DBRoleLoginGift cg, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (cg == null)
			return false;
		for (GameActivities.LoginLvlGift clg : cfg.getLoginLvlGift())
		{
			if (cg.loginDays >= clg.loginDaysReq && !cg.reward.contains(clg.loginDaysReq))
				return true;
		}
		return false;
	}
	
	public boolean alreadyTakeAllLoginGiftReward(GameActivities.LoginGiftConfig cfg, SBean.DBRoleLoginGift cg)
	{
		if (cg == null)
			return false;
		for (GameActivities.LoginLvlGift clg : cfg.getLoginLvlGift())
		{
			if (!cg.reward.contains(clg.loginDaysReq))
				return false;
		}
		return true;
	}
	
	public boolean takeLoginGiftReward(GameActivities.LoginGiftConfig cfg, int id, int days)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleLoginGift cg = syncLoginGiftState(cfg);
			if (cg.id != id)
				return false;
			if (!canTakeLoginGiftReward(cfg, cg, gs.getTime()))
				return false;
			GameActivities.LoginLvlGift clg = cfg.getLoginLvlGift(days);
			if (clg == null)
				return false;
			if (cg.loginDays < clg.loginDaysReq || cg.reward.contains(days))
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(clg.gift));
			cg.reward.add(days);
		}
		commonFlowRecord.setEvent(TLog.AT_LOGIN_GIFT);
		commonFlowRecord.setArg(id, days);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public void loginGiftLogLogin(GameActivities.LoginGiftConfig cfg, int curTime)
	{
		SBean.DBRoleLoginGift cg = syncLoginGiftState(cfg);
		if (cg == null)
			return;
		int curLogininDayIndex = cfg.getTimeDayIndex(curTime);
		int lastLoginDayIndex = cfg.getTimeDayIndex(cg.lastLoginTime);
		if (curLogininDayIndex > lastLoginDayIndex)
		{
			++cg.loginDays;
			cg.lastLoginTime = curTime;
		}
	}
	
	public void loginGiftLogLogin(int curTime)
	{
		List<GameActivities.LoginGiftConfig> cfgs = gs.getGameActivities().getLoginGiftActivity().getOpenedConfigs();
		for (GameActivities.LoginGiftConfig cfg : cfgs)
		{
			loginGiftLogLogin(cfg, curTime);
		}
	}
	
	SBean.DBRoleTradingCenter syncTradingCenterState(GameActivities.TradingCenterConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		SBean.DBRoleTradingCenter tc = this.tradingCenter.get(id);
		if (tc == null)
		{
			tc = new SBean.DBRoleTradingCenter();
			tc.id = id;
			tc.goods = new ArrayList<SBean.DBRoleWarGoods>();
			this.tradingCenter.put(id, tc);
		}
		return tc;
	}
	
	public synchronized SBean.DBRoleTradingCenter tradingCenterSync(GameActivities.TradingCenterConfig cfg)
	{
		SBean.DBRoleTradingCenter tc = syncTradingCenterState(cfg);
		if (tc == null)
			return null;
		return tc.kdClone();
	}
	
	SBean.DBRoleWarGoods getBuyGoods(SBean.DBRoleTradingCenter tc, int type)
	{
		for (SBean.DBRoleWarGoods g : tc.goods)
		{
			if (g.type == type)
				return g;
		}
		return null;
	}
	
	int getAllBuyGoodsCount(SBean.DBRoleTradingCenter tc)
	{
		int count = 0;
		for (SBean.DBRoleWarGoods g : tc.goods)
		{
			count += g.buyCnt;
		}
		return count;
	}
	
	public boolean canTakeTradingCenterReward(GameActivities.TradingCenterConfig cfg, SBean.DBRoleTradingCenter tc, int curTime)
	{
		if (!cfg.isOpened(curTime))
			return false;
		if (curTime < cfg.getBuyEndTime())
			return false;
		if (tc == null)
			return false;
		for (SBean.DBRoleWarGoods g : tc.goods)
		{
			if (g.buyCnt > 0)
			{
				for (int j = 0; j < cfg.getReturnTimeCount(); ++j)
				{
					if (curTime > cfg.getReturnTime(j) && !g.reward.contains(j+1))
						return true;
				}
			}
		}
		return false;
	}
	
	public boolean alreadyTakeAllTradingCenterReward(GameActivities.TradingCenterConfig cfg, SBean.DBRoleTradingCenter tc)
	{
		if (tc == null)
			return false;
		int curTime = gs.getTime();
		if (curTime < cfg.getBuyEndTime())
			return false;
		for (SBean.DBRoleWarGoods g : tc.goods)
		{
			if (g.buyCnt > 0)
			{
				for (int j = 0; j < cfg.getReturnTimeCount(); ++j)
				{
					if (!g.reward.contains(j+1))
						return false;
				}
			}
		}
		return true;
	}
	
	
	public boolean takeTradingCenterReward(GameActivities.TradingCenterConfig cfg, int id, int type)
	{
		if (cfg == null)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			SBean.DBRoleTradingCenter tc = syncTradingCenterState(cfg);
			if (tc.id != id)
				return false;
			int curTime = gs.getTime();
			if (!canTakeTradingCenterReward(cfg, tc, curTime))
				return false;
			GameActivities.WarGoods gcfg = cfg.getGoods(type);
			if (gcfg == null)
				return false;
			SBean.DBRoleWarGoods g = getBuyGoods(tc, type);
			if (g == null || g.buyCnt <= 0)
				return false;
			int canReturnVal = 0;
			for (int t = 0; t < cfg.getReturnTimeCount(); ++t)
			{
				if (curTime > cfg.getReturnTime(t) && !g.reward.contains(t+1))
					canReturnVal += gcfg.getReturnValue(t);
			}
			if (canReturnVal <= 0)
				return false;
			commonFlowRecord.addChange(this.addStonePresent(canReturnVal*g.buyCnt));
			for (int t = 0; t < cfg.getReturnTimeCount(); ++t)
			{
				if (curTime > cfg.getReturnTime(t) && !g.reward.contains(t+1))
					g.reward.add(t+1);
			}
		}
		commonFlowRecord.setEvent(TLog.AT_TRADING_CENTER);
		commonFlowRecord.setArg(id, type);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean buyTradingCenterGoods(GameActivities.TradingCenterConfig cfg, int id, int type, int count)
	{
		if (cfg == null)
			return false;
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this)
		{
			int curTime = gs.getTime();
			if (curTime > cfg.getBuyEndTime())
				return false;
			SBean.DBRoleTradingCenter tc = syncTradingCenterState(cfg);
			if (tc.id != id)
				return false;
			GameActivities.WarGoods gcfg = cfg.getGoods(type);
			if (gcfg == null)
				return false;
			if (this.lvlVIP < gcfg.getVipLvlReq())
				return false;
			SBean.DBRoleWarGoods g = getBuyGoods(tc, type);
//			int buyCnt = g == null ? 0 : g.buyCnt;
//			if (count <= 0 || buyCnt + count > gcfg.getMaxBuyCount())
		    int buyCnt = getAllBuyGoodsCount(tc);
			if (count <= 0 || buyCnt + count > cfg.getMaxBuyCount())
				return false;
			int cost = count * gcfg.getPrice();
			if (this.stone < cost)
				return false;
			commonFlowRecord.addChange(this.useStone(cost));
			if (g == null)
			{
				g = new SBean.DBRoleWarGoods(type, 0, new ArrayList<Integer>());
				tc.goods.add(g);
			}
			g.buyCnt += count;
		}
		commonFlowRecord.setEvent(TLog.AT_BUY_WARGOODS);
		commonFlowRecord.setArg(id, type);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
//TODO
/////	
	void syncInvitationState(SBean.InvitationCFGS cfgs)
	{
		if (cfgs == null)
			return;
		if (invitation == null)
		{
			SBean.DBInvitation data = new SBean.DBInvitation(new ArrayList<SBean.DBInvitationTasks>(), new ArrayList<SBean.DBInvitationFriend>(), new SBean.DBInvitationSelf());
			for (SBean.InvitationTypeTasksCFG cfg : cfgs.tasks)
			{
				SBean.DBInvitationTasks itasks = new SBean.DBInvitationTasks(cfg.type, new ArrayList<SBean.DBInvitationGroupTask>(), 0, new ArrayList<Integer>());
				for (SBean.InvitationGroupTasksCFG e : cfg.tasks)
				{
					SBean.DBInvitationGroupTask gtask = new SBean.DBInvitationGroupTask(e.groupId, 0, 0);
					itasks.tasks.add(gtask);
				}
				data.tasks.add(itasks);
			}
			invitation = data;
		}
	}
	
	public synchronized List<SBean.DBInvitationTasks> invitationTaskSync()
	{
		SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
		syncInvitationState(cfgs);
		List<SBean.DBInvitationTasks> lst = new ArrayList<SBean.DBInvitationTasks>();
		for (SBean.DBInvitationTasks e : invitation.tasks)
		{
			SBean.DBInvitationTasks clone = e.kdClone();
			if (e.type == 1)//锟斤拷锟轿碉拷一锟斤拷锟斤拷锟斤拷1锟斤拷锟斤拷锟窖斤拷锟斤拷锟街顾拷锟斤拷锟�
			{
				for (SBean.DBInvitationGroupTask gt : clone.tasks)
				{
					if (gt.groupId == 1)
					{
						SBean.InvitationTypeTasksCFG tcfg = cfgs.tasks.get(e.type-1);
						SBean.InvitationGroupTasksCFG gcfg = tcfg.tasks.get(gt.groupId-1);
						gt.seq = gcfg.tasks.size();
					}
				}
			}
			lst.add(clone);
		}
		return lst;
	}
	
	public synchronized List<SBean.DBInvitationFriend> invitationFriendsSync()
	{
		SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
		syncInvitationState(cfgs);
		List<SBean.DBInvitationFriend> lst = new ArrayList<SBean.DBInvitationFriend>();
		for (SBean.DBInvitationFriend e : invitation.friends)
		{
			lst.add(e.kdClone());
		}
		return lst;
	}
	
	public boolean takeInvitationTaskReward(int type, int groupID, int seq)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
			if (type <= 0 || type > cfgs.tasks.size())
				return false;
			SBean.InvitationTypeTasksCFG cfg = cfgs.tasks.get(type-1);
			if (groupID <= 0 || groupID > cfg.tasks.size())
				return false;
			SBean.InvitationGroupTasksCFG gcfg = cfg.tasks.get(groupID-1);
			if (seq <= 0 || seq > gcfg.tasks.size())
				return false;
			
			SBean.InvitationTaskCFG tcfg = gcfg.tasks.get(seq-1);
			syncInvitationState(cfgs);
			if (type <= 0 || type > invitation.tasks.size())
				return false;
			SBean.DBInvitationTasks itasks = invitation.tasks.get(type-1);
			if (groupID <= 0 || groupID > itasks.tasks.size())
				return false;
			SBean.DBInvitationGroupTask gtask = itasks.tasks.get(groupID-1);
			if (gtask.seq + 1 != seq)
				return false;
			if (gtask.reqCurVal < tcfg.reqArg1)
				return false;
			if (tcfg.rewardMoney > 0)
				commonFlowRecord.addChange(this.addMoney(tcfg.rewardMoney));
			if (tcfg.rewardStone > 0)
				commonFlowRecord.addChange(this.addStonePresent(tcfg.rewardStone));
			
			if (tcfg.rewardVip > 0)
			{
				TLogger.PayRecord record = new TLogger.PayRecord(this.lvl, GameData.getInstance().getVIPLevel(this.getStonePayTotal()), this.getStonePayTotal());
				
				TLogger.CommonItemChange change = this.addPayVipExp(tcfg.rewardVip);
				if (change != null)
				{
					commonFlowRecord.addChange(change);
					record.setPayInfo(TLog.PAY_LEVEL_PRESENT, (change.changeCount)/10, 0);
					tlogEvent.addRecord(record);
				}	
			}
			
			itasks.points += tcfg.rewardPoint;
			gtask.seq++;
		}
		commonFlowRecord.setEvent(TLog.AT_INVITATION_TASK);
		commonFlowRecord.setArg(type, groupID, seq);
		tlogEvent.addCommonFlow(commonFlowRecord);
		
		gs.getTLogger().logEvent(this, tlogEvent);
		
		return true;
	}
	
	void logInvitationTask()
	{
		SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
		for (SBean.DBInvitationTasks itasks : invitation.tasks)
		{
			SBean.InvitationTypeTasksCFG cfg = cfgs.tasks.get(itasks.type-1);
			for (SBean.DBInvitationGroupTask gtask : itasks.tasks)
			{
				int curVal = 0;
				SBean.InvitationGroupTasksCFG gcfg = cfg.tasks.get(gtask.groupId-1);
				for (SBean.DBInvitationFriend e : invitation.friends)
				{
					int nextSeq = gtask.seq + 1;
					if (nextSeq > gcfg.tasks.size())
						nextSeq = gcfg.tasks.size();
					SBean.InvitationTaskCFG tcfg = gcfg.tasks.get(nextSeq-1);
					switch (itasks.type)
					{
					case 1:
						{
							if (e.level >= tcfg.reqArg2)
								++curVal;	
						}
						break;
					case 2:
						{
							if (e.vip >= tcfg.reqArg2)
								++curVal;	
						}
						break;
					default:
						break;
					}
					
				}
				gtask.reqCurVal = curVal;
			}
		}
	}
	
	public boolean takeInvitationPointsReward(int type, int rid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
			if (type <= 0 || type > cfgs.tasks.size())
				return false;
			SBean.InvitationTypeTasksCFG cfg = cfgs.tasks.get(type-1);
			
			syncInvitationState(cfgs);
			if (type <= 0 || type > invitation.tasks.size())
				return false;
			SBean.DBInvitationTasks itasks = invitation.tasks.get(type-1);
			
			if (rid <= 0 || rid > cfg.rewards.size())
				return false;
			SBean.InvitationPointsRewardCFG rcfg = cfg.rewards.get(rid-1);
			if (itasks.points < rcfg.pointsLvl)
				return false;
			if (itasks.pointsReward.contains(rid))
				return false;
			commonFlowRecord.addChanges(this.addDropsNew(rcfg.reward));
			itasks.pointsReward.add(rid);
		}
		commonFlowRecord.setEvent(TLog.AT_INVITATION_POINTS);
		commonFlowRecord.setArg(type, rid);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	

	void updateAllInvitationFriendsInfo()
	{
		int selfgsid = gs.getConfig().id;
		int curTime = gs.getTime();
		List<SBean.GlobalRoleID> ugrid = new ArrayList<SBean.GlobalRoleID>();
		synchronized (this)
		{
			SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
			syncInvitationState(cfgs);
			for (SBean.DBInvitationFriend e : invitation.friends)
			{
				if (gs.getConfig().godMode == 1 || ExchangeManager.needUpdate(e.gsid == selfgsid, e.lastSyncTime, e.lastLoginTime, curTime))
					ugrid.add(new SBean.GlobalRoleID(e.gsid, e.roleid));
			}
		}
		for (SBean.GlobalRoleID e : ugrid)
		{
			updateInvitationFriendInfo(e.serverID, e.roleID, new LoginManager.UpdateInvitationFriendInfoCallback()
					{
						@Override
						public void onCallback(DBInvitationFriend info) 
						{
							onUpdateInvitationFriendInfo(info);
						}
					});
		}
	}
	
	public void updateInvitationFriendInfo(int gsid, int roleid, LoginManager.UpdateInvitationFriendInfoCallback callback)
	{
		if (gsid == gs.getConfig().id)
		{
			gs.getLoginManager().updateInvitationFriendInfo(roleid, callback);

		}
		else
		{
			gs.getRPCManager().sendInvitationFriendUpdateInfoRequest(gsid, roleid, callback);
		}
	}
	
	public void onUpdateInvitationFriendInfo(DBInvitationFriend info)
	{
		if (info == null)
			return;
		if (invitation == null)
			return;
		synchronized (this)
		{
			for (SBean.DBInvitationFriend e : invitation.friends)
			{
				if (e.gsid == info.gsid && e.roleid == info.roleid)
				{
					e.rolename = info.rolename;
					e.level = info.level;
					e.vip = info.vip;
					e.icon = info.icon;
					e.lastSyncTime = info.lastSyncTime;
					e.lastLoginTime = info.lastLoginTime;
					break;
				}
			}
			logInvitationTask();
		}
	}
	
	
	public String getInvitationSelfCode()
	{		
		return gs.getLoginManager().getGlobalRoleIDEncoder().getGlobalRoleIDCode(gs.getConfig().id, this.id);
	}
	
	private boolean isSelfBeInvitated()
	{
		return (this.invitation.selfInvitated.gsid != 0 && this.invitation.selfInvitated.roleid != 0);
	}
	
	private boolean isSelfInvitateFriend(int gsid, int roleid)
	{
		for (SBean.DBInvitationFriend e : invitation.friends)
		{
			if (e.gsid == gsid && e.roleid == roleid)
			{
				return true;
			}
		}
		return false;
	}
	
	private synchronized boolean canInvitateFriend(SBean.InvitationCFGS cfgs, int gsid, int roleid)
	{
		syncInvitationState(cfgs);
		return !isSelfBeInvitated() && !isSelfInvitateFriend(gsid, roleid);
	}
	
	private void addSelfInvitateFriend(int gsid, int roleid, String rolename, int level, int vip, short headicon, int syncTime, int lastLogin)
	{
		invitation.friends.add(new SBean.DBInvitationFriend(gsid, roleid, rolename, level, vip, headicon, syncTime, lastLogin));
	}
	//-1:unknown 锟斤拷锟斤拷
	//-2:锟斤拷时 锟斤拷锟斤拷
	//-3:锟斤拷询失锟斤拷 锟斤拷锟斤拷
	//-4:同一锟斤拷色锟斤拷锟斤拷
	//-5:锟截革拷锟斤拷锟矫达拷锟斤拷
	//-6:锟斤拷锟斤拷锟斤拷锟斤拷锟�
	//-7:锟斤拷锟斤拷燃锟斤拷锟斤拷诜锟轿э拷锟�
	public void inputInvitationFriendCode(String code)
	{
		final SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
		if (this.lvl < cfgs.inputKeyLvlMin || this.lvl > cfgs.inputKeyLvlMax)
		{
			gs.getRPCManager().notifyInvitationSetFriendResult(netsid, -7);
			return;
		}
		code = code.toUpperCase();
		if (!gs.getLoginManager().getGlobalRoleIDEncoder().checkCodeValid(code))
		{
			gs.getRPCManager().notifyInvitationSetFriendResult(netsid, -6);
			return;
		}
		{
			long ugrid = gs.getLoginManager().getGlobalRoleIDEncoder().getGlobalRoleID(code);
			final int dstgs = GlobalRoleIDEncoder.getGameServerID(ugrid);
			final int dstrid = GlobalRoleIDEncoder.getRoleID(ugrid);
			if (dstgs <= 0 || dstrid <= 0)
			{
				gs.getRPCManager().notifyInvitationSetFriendResult(netsid, -6);
				return;
			}
			if (!canInvitateFriend(cfgs, dstgs, dstrid))
			{
				gs.getRPCManager().notifyInvitationSetFriendResult(netsid, -5);
				return;
			}
			setInvitationFriendSelf(dstgs, dstrid, new LoginManager.SetInvitationFriendCallback()
					{
						public void onCallback(int errorCode)
						{
							if (errorCode == 0 || (errorCode == -5 && !isSelfBeInvitated()))
							{
								TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
								TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
								synchronized (Role.this)
								{
									commonFlowRecord.addChanges(Role.this.addDropsNew(cfgs.beInvitedRewards));
									Role.this.invitation.selfInvitated.gsid = dstgs;
									Role.this.invitation.selfInvitated.roleid = dstrid;
								}
								commonFlowRecord.setEvent(TLog.AT_INVITATION_REWARD);
								commonFlowRecord.setArg(dstgs, dstrid);
								tlogEvent.addCommonFlow(commonFlowRecord);
								gs.getTLogger().logEvent(Role.this, tlogEvent);
							}
							gs.getRPCManager().notifyInvitationSetFriendResult(netsid, errorCode);
						}
					});
		}
	}
	
	void setInvitationFriendSelf(int gsid, int roleid, LoginManager.SetInvitationFriendCallback callback)
	{
		if (gsid == gs.getConfig().id)
		{
			gs.getLoginManager().setInvitationFriend(roleid, gsid, this.id, this.name, this.lvl, this.lvlVIP, this.headIconID, gs.getTime(), this.lastLoginTime, callback);
		}
		else
		{
			gs.getRPCManager().sendInvitationSetFriendRequest(gsid, roleid, gs.getConfig().id, this.id, this.name, this.lvl, this.lvlVIP, this.headIconID, gs.getTime(), this.lastLoginTime, callback);
		}
	}
	
	public int onSetInvitationFriend(int gsid, int roleid, String rolename, int level, int vip, short headicon, int syncTime, int lastLogin)
	{
		synchronized (this)
		{
			SBean.InvitationCFGS cfgs = GameData.getInstance().getInvitationCFG();
			syncInvitationState(cfgs);
			if (isSelfInvitateFriend(gsid, roleid))
			{
				return -5;
			}
			addSelfInvitateFriend(gsid, roleid, rolename, level, vip, headicon, syncTime, lastLogin);
			logInvitationTask();
		}
		return 0;
	}
	
	public SBean.DBInvitationFriend getSelfInvitationInfo(int gsid, int curTime)
	{
		SBean.DBInvitationFriend info = new SBean.DBInvitationFriend(gsid, this.id, this.name, this.lvl, this.lvlVIP, this.headIconID, curTime, this.lastLoginTime);
		return info;
	}
	
	public interface ExchangeCDKeyCallback
	{
		public void onCallback(int bid, int gid, int result, String giftTitle, String giftContent, List<i3k.SBean.DropEntryNew> gift);
	}
	
	public void exchangeCDKeyGift(final GameActivities.GiftPackageConfig cfg, final String cdkey)
	{
		gs.getLoginManager().exchangeCDKey(this, cdkey, cfg, new ExchangeCDKeyCallback()
				{
					public void onCallback(int bid, int gid, int result, String giftTitle, String giftContent, List<i3k.SBean.DropEntryNew> gift)
					{
						if (result == CDKeyExchange.CDKEY_EXCHANGE_SUCCESS)
						{
							TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
							TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
							synchronized (this)
							{
								commonFlowRecord.addChanges(addDropsNew(gift));
							}
							commonFlowRecord.setEvent(TLog.AT_EXCHANGE_GIFT_PACKAGE);
							commonFlowRecord.setArg(bid, gid, result);
							tlogEvent.addCommonFlow(commonFlowRecord);
							gs.getTLogger().logEvent(Role.this, tlogEvent);
						}
						gs.getRPCManager().sendLuaPacket2(netsid, LuaPacket.encodeGiftPackageTakeRes(cfg.getID(), result, giftTitle, giftContent, gift));
						gs.getLogger().info("role " + Role.this.id + " exchange cdkey (" + cdkey + "), batchID=" + bid + ", genID=" + gid + ", result=" + result);
					}
				});
	}
	
	DBDiskBetInfo syncDiskBetState(GameActivities.DiskBetConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		
		if(diskBet==null||diskBet.id!=id){
			diskBet = new SBean.DBDiskBetInfo();
			diskBet.id = id;
			DBDiskBet diskBet2 = gs.getDiskManager().diskBet;
			if(diskBet2!=null&&(cfg.start!=diskBet2.statime||diskBet2.endtime != cfg.end)){
				diskBet2.statime = cfg.start;
				diskBet2.endtime = cfg.end;
				diskBet2.minscore = cfg.MIN_SCORE;
				diskBet2.minRmb = cfg.MIN_RMB;
				gs.getDiskManager().mailatt3=cfg.mailatt3;
				gs.getDiskManager().mailatt4=cfg.mailatt4;
				gs.getDiskManager().mailatt5=cfg.mailatt5;
				if(diskBet2.ranksHis!=null){
					diskBet2.ranksHis.clear();	
				}				
			}
			
		}
		int day = gs.getDayByOffset((short) 0);
		if (diskBet.bonusday < day) {
			diskBet.bonusday = day;
			diskBet.times1=0;
			diskBet.times2=0;
			diskBet.times3=0;
			diskBet.times4=0;
		}
		
		return diskBet;
	}
	
	DBDiskBetInfo syncRechargeRankState(GameActivities.RechargeRankConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		
		if(diskBet2==null||diskBet2.id!=id){
			diskBet2 = new SBean.DBDiskBetInfo();
			diskBet2.id = id;
			DBDiskBet diskBet2 = gs.getDiskManager2().diskBet;
			if(diskBet2!=null&&(cfg.start!=diskBet2.statime||diskBet2.endtime != cfg.end)){
				diskBet2.statime = cfg.start;
				diskBet2.endtime = cfg.end;
				diskBet2.minscore = cfg.MIN_SCORE;
				diskBet2.minRmb = cfg.MIN_RMB;
				gs.getDiskManager2().mailatt5=cfg.mailatt5;
				if(diskBet2.ranksHis!=null){
					diskBet2.ranksHis.clear();	
				}				
			}
			
		}
		int day = gs.getDayByOffset((short) 0);
		if (diskBet2.bonusday < day) {
			diskBet2.bonusday = day;
			diskBet2.times1=0;
			diskBet2.times2=0;
			diskBet2.times3=0;
			diskBet2.times4=0;
		}
		
		return diskBet2;
	}
	
	public synchronized short diskBetSyncSelf()
	{
		if(diskBet==null||gs.getDiskManager().diskBet==null){
			return (short)1;
		}
		return diskBet.score>=gs.getDiskManager().diskBet.minscore?(short)3:(short)1;
	}
	
	public synchronized List<DBDiskBetRank> diskrankHis()
	{
		
		return gs.getDiskManager().getHisRanks();
	}
	
	public synchronized List<DBDiskBetRank> diskrank(GameActivities.DiskBetConfig cfg,short tid)
	{
		if (cfg == null)
			return null;
		if(tid==3){
			return gs.getDiskManager().getAllScoreRanks2((short)1);
		}
		
		return gs.getDiskManager().getAllScoreRanks(tid);
	}
	
	public synchronized List<DBDiskBetRank> diskrank2(GameActivities.RechargeRankConfig cfg,short tid)
	{
		if (cfg == null)
			return null;
		if(tid==3){
			return gs.getDiskManager2().getAllScoreRanks2((short)1);
		}
		
		return gs.getDiskManager2().getAllScoreRanks(tid);
	}
	
	public synchronized List<DBDiskBetRank> diskrank2(GameActivities.DiskBetConfig cfg,short tid)
	{
		if (cfg == null)
			return null;
		if(tid==3){
			return gs.getDiskManager().getAllScoreRanks2((short)1);
		}
		
		return gs.getDiskManager().getAllScoreRanks(tid);
	}
	
	public synchronized List<DropEntryNew> diskBet(GameActivities.DiskBetConfig cfg,short tid)
	{
		if (cfg == null)
			return null;
		
		if(diskBet==null){
		   diskBet = new SBean.DBDiskBetInfo();
		}
		
		SBean.MercCFGS cfgfree = gs.gameData.getMercCFG();
		
		short bCount = 0;
		List<DropTableEntryNew> list = null;
		if(tid==1||tid==2){
			
			list  = cfg.mailatt1;
			bCount = diskBet.times1;
		}else if (tid==3||tid==4){
			list  = cfg.mailatt2;
			bCount = diskBet.times2;
		}
		
		if(list ==null){
			return null;
		}
		
		short  cost = gs.getGameData().getDiskBetCostCFG(bCount, tid);

		
        if(tid==1){
			
        	if(diskBet.times3<cfgfree.timedisk1){
        		cost =0;
        	}
		}else if (tid==3){
			if(diskBet.times4<cfgfree.timedisk2){
				cost =0;
	    	}
		}
    	
		
		
		if(cost<0){
			return null;
		}
		
		if(stone<cost){
			return null;
		}
		
		List<DropEntryNew> dropl = new ArrayList<DropEntryNew>();
		
		Random random = new Random();
		
		int count  = 1;
		if(tid ==2||tid==4){
			count  = 10;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.addChange(this.useStone(cost));
		int index1 =0;
		boolean poolf = false;
		for(int i = 0 ;i<count ;i++){
			DropEntryNew drop = null;
			
			
			float total = 0.0f;
			for(DropTableEntryNew e : list)
			{
				total += e.pro;
			}
			
			int r = random.nextInt((int)total);
			
			
			int pro =0;
			int index =0;
			for(DropTableEntryNew e : list)
			{
				pro+=e.pro;
				if( r < pro ){
					break;
				}
				index++;
			}
			drop = list.get(index).item;
			
			if(drop==null){
				return null;
			}
			index1 =index;
			if(drop.type==GameData.COMMON_TYPE_STONE){
				
				if(poolf){
					for(int j =0 ;j<50;j++){
						pro =0;
						index =0;
						r = random.nextInt((int)total);
						for(DropTableEntryNew e : list)
						{
							pro+=e.pro;
							if( r < pro ){
								break;
							}
							index++;
						}
						drop = list.get(index).item;
						
						if(drop==null){
							return null;
						}
						index1 =index;
						if(drop.type!=GameData.COMMON_TYPE_STONE){
							addDropNew(drop);
							break;
						}
					}
					
				}else{
					int pool =gs.getDiskManager().getDiskBetPool();
					this.addStonePresent(pool/2);
					drop.arg = pool/2;
					gs.getDiskManager().addDiskBetPool(-(pool/2));
					poolf= true;
				}
		
			}else{
				addDropNew(drop);
			}
			if(drop!=null)
			dropl.add(drop);
			
			gs.getDiskManager().addDiskBetPool(1);
		}

		
        if(tid==1||tid==2){
        	
        	if(tid==1&&diskBet.times3<cfgfree.timedisk1){//gs.getGameData().getDiskBetCostCFG(tid)
        		diskBet.times3++;
        	}else{
        		diskBet.times1+=count;	
        	}			
			diskBet.score+=10*count;
		}else if (tid==3||tid==4){
			if(tid==3&&diskBet.times4<cfgfree.timedisk2){
        		diskBet.times4++;
        	}else{
        		diskBet.times2+=count;	
        	}			
			diskBet.score+=100*count;
		}
		
        if(diskBet.score>0){
        	gs.getDiskManager().setDamage((short) 1,diskBet.score, id, name,"12", headIconID, lvl);
        }
        
        gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeDiskbetRes(id,tid,cfg, dropl,(index1+1)));
        
        commonFlowRecord.setEvent(TLog.AT_DISKBET);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return dropl;
	}
	
	public synchronized DBDiskBetInfo diskBetSync(GameActivities.DiskBetConfig cfg)
	{
		DBDiskBetInfo tc = syncDiskBetState(cfg);
		if (tc == null)
			return null;
		return tc.kdClone();
	}
	
	public synchronized DBDiskBetInfo diskBetSync2(GameActivities.RechargeRankConfig cfg)
	{
		DBDiskBetInfo tc = syncRechargeRankState(cfg);
		if (tc == null)
			return null;
		return tc.kdClone();
	}
	
	public boolean forceMobai(int fid, int rid, int type)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WORSHIP);
		synchronized (this)
		{
			dayRefresh();
			if( fid <= 0 || fid != forceInfo.id || type < 0 || type > 2 )
				return false;
			if( rid <= 0 || rid == id )
				return false;
			if( forceInfo.mobai.size() >= getMaxMobaiTimes() )
				return false;
			for(int e : forceInfo.mobai)
			{
				if( rid == e )
					return false;
			}
			if (type == 2 && forceInfo.superMobai >= gs.getGameData().getSuperMobaiTimesMax(lvlVIP))
				return false;
			SBean.ForceCFGS cfgs = gs.getGameData().getForceCFG();
			int mobaiReward = 0;
			switch (type)
			{
			case 0:
				commonFlowRecord.addChange(addVit2(cfgs.mobai.freeVit));
				mobaiReward = cfgs.mobai.freeMoney;
				break;
			case 1:
				if (money < cfgs.mobai.moneyCost)
					return false;
				//money -= cfgs.mobai.moneyCost;
				commonFlowRecord.addChange(useMoney(cfgs.mobai.moneyCost));
				commonFlowRecord.addChange(addVit2(cfgs.mobai.moneyVit));
				mobaiReward = cfgs.mobai.moneyMoney;
				break;
			case 2:
				if (stone < cfgs.mobai.stoneCost)
					return false;
				//stone -= cfgs.mobai.stoneCost;
				commonFlowRecord.addChange(useStone(cfgs.mobai.stoneCost));
				commonFlowRecord.addChange(addVit2(cfgs.mobai.stoneVit));
				mobaiReward = cfgs.mobai.stoneMoney;
				forceInfo.superMobai ++;
				break;
			}
			forceInfo.mobai.add(rid);
			final int reward = mobaiReward;
			gs.getLoginManager().execCommonRoleVisitor(rid, new CommonRoleVisitor()
			{
				@Override
				public byte visit(DBRole dbrole)
				{
					SBean.ForceCFGS cfgs = gs.getGameData().getForceCFG();
					dbrole.forceMobaiReward += reward;
					if (dbrole.forceMobaiReward > cfgs.mobai.maxRewardMoney)
						dbrole.forceMobaiReward = cfgs.mobai.maxRewardMoney;
					return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
				}
				
				@Override
				public boolean visit(Role role)
				{
					SBean.ForceCFGS cfgs = gs.getGameData().getForceCFG();
					synchronized (role) {
						role.forceInfo.mobaiReward += reward;
						if (role.forceInfo.mobaiReward > cfgs.mobai.maxRewardMoney)
							role.forceInfo.mobaiReward = cfgs.mobai.maxRewardMoney;					
					}
					return true;
				}

				@Override
				public void onCallback(boolean bDB, byte errcode, String rname)
				{
				}
			});
		}
		commonFlowRecord.setEvent(TLog.AT_FORCE_WORSHIP);
		commonFlowRecord.setArg(fid, rid, type);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public int forceMobaiTakeReward(int fid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WORSHIP_REWARD);
		int reward = -1;
		synchronized (this)
		{
			dayRefresh();
			if( fid <= 0 || fid != forceInfo.id )
				return -1;
			if( forceInfo.mobaiReward <= 0 )
				return -1;
			reward = forceInfo.mobaiReward;
			commonFlowRecord.addChange(addMoney(forceInfo.mobaiReward));
			forceInfo.mobaiReward = 0;
		}
		commonFlowRecord.setEvent(TLog.AT_FORCE_WORSHIP_REWARD);
		commonFlowRecord.setArg(fid);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
		return reward;
	}
	
	private int getMaxMobaiTimes()
	{
		return GameData.getInstance().getMobaiTimesMax(lvlVIP);
	}
	
	public void announceOpen(boolean b, int stampnow)
	{
		bRecvWorldChat = b;
		if( b )
		{
			if( stampnow > 0)
			{
				wChatSyncStamp = stampnow;
			}
		}
	}
	
	public void forceChatOpen(boolean b, int stampnow)
	{
		bRecvForceChat = b;
		if( b )
		{
			if( stampnow > 0)
				fChatSyncStamp = stampnow;
		}
	}
	
	public void privateChatOpen(boolean b, int stampnow)
	{
		bRecvPrivateChat = b;
		if( b )
		{
			if( stampnow > 0)
				pChatSyncStamp = stampnow;
		}
	}
	
	public int isRecvWorldChat(int stamp)
	{
		if( isNull() )
			return -1;
		if( netsid <= 0 )
			return -1;
		if( ! bRecvWorldChat )
			return -1;
		wChatSyncStamp = stamp;
		return netsid;
	}
	
	//public boolean isIOSGuest()
	//{
	//	return msdkInfo != null && msdkInfo.bGuest;
	//}
	
	public int testChat(int[] stampNew)
	{		
		int wcStamp = gs.getLoginManager().getWorldChatStamp();
		if( wcStamp > wChatSyncStamp )
		{
			stampNew[0] = wcStamp;
			return 1;
		}
		if( forceInfo != null && forceInfo.id > 0 )
		{
			int fcStamp = gs.getLoginManager().getForceChatStamp(forceInfo.id);
			if( fcStamp > fChatSyncStamp )
			{
				stampNew[0] = fcStamp;
				return 2;
			}
		}
		return 0;
	}
	
	public int isRecvForceChat(int stamp)
	{
		if( isNull() )
			return -1;
		if( netsid <= 0 )
			return -1;
		if( ! bRecvForceChat )
			return -1;
		fChatSyncStamp = stamp;
		return netsid;
	}
	
	public int isRecvPrivateChat(int stamp)
	{
		if( isNull() )
			return -1;
		if( netsid <= 0 )
			return -1;
		if( ! bRecvPrivateChat )
			return -1;
		pChatSyncStamp = stamp;
		return netsid;
	}
	
	public List<LoginManager.WorldChatCache.Msg> getPrivateMsg(int time)
	{	
		return rolePcCache.getMsg(time);
	}
	
	public void sendPrivateChatMsg(Role recvRole, String msg)
	{
		int curTime = gs.getTime();
		{
			LoginManager.WorldChatCache.Msg m = new LoginManager.WorldChatCache.Msg();
			m.msg = msg;
			m.roleid = this.id;
			m.lvlVIP = this.lvlVIP;
			m.rolename = this.name;
			m.forcename = this.forceInfo.name;
			m.lvl = this.lvl;
			m.headIconID = this.headIconID;
			m.time = curTime;
			
			recvRole.rolePcCache.addMsg(m);
			recvRole.pChatSyncStamp = m.time;
			String data = LuaPacket.encodePrivateChat(m);
			int recvSid = recvRole.isRecvPrivateChat(m.time);
			if (recvSid >= 0)
				gs.getRPCManager().sendLuaPacket(recvSid, data);	
		}
		{
			LoginManager.WorldChatCache.Msg m = new LoginManager.WorldChatCache.Msg();
			m.msg = msg;
			m.roleid = -recvRole.id;//锟斤拷锟斤拷rid锟斤拷示锟斤拷锟斤拷息锟斤拷锟酵斤拷锟斤拷锟斤拷
			m.lvlVIP = recvRole.lvlVIP;
			m.rolename = recvRole.name;
			m.forcename = recvRole.forceInfo.name;
			m.lvl = recvRole.lvl;
			m.headIconID = recvRole.headIconID;
			m.time = curTime;
			
			this.rolePcCache.addMsg(m);
			this.pChatSyncStamp = m.time;
			String data = LuaPacket.encodePrivateChat(m);
			int mySid = this.isRecvPrivateChat(m.time);
			if (mySid >= 0)
				gs.getRPCManager().sendLuaPacket(mySid, data);	
		}
		
	}
	
	public int forceAnnounceReplay(String image, String name1, String name2, int recId, int ftime)
	{
		if (isBanChat())
			return -1;
		
		if( forceInfo.id <= 0 )
			return -2;
		
		int max = gs.getGameData().getUserInputCFG().worldChat;
		name1 = gs.getLoginManager().checkUserInput(name1, max, false, true);
		if( name1 == null )
			return -3;
		name2 = gs.getLoginManager().checkUserInput(name2, max, false, true);
		if( name2 == null )
			return -3;
		
		String msg = "Rep!ayDat:" + recId + "#" + ftime + "#" + image + "#" + name1 + "#" + name2;
		return forceSendAnnounceMsg(msg, MsgType.Replay);
	}
	
	public int forceAnnounceNormal(String msg)
	{
		if (isBanChat())
			return -1;
		if( forceInfo.id <= 0 )
			return -2;
		
		int max = gs.getGameData().getUserInputCFG().worldChat;
		msg = gs.getLoginManager().checkUserInput(msg, max, false, true);
		if( msg == null )
			return -3;
		
		return forceSendAnnounceMsg(msg, MsgType.Normal);
	}
	
	int forceSendAnnounceMsg(String msg, MsgType type)
	{
	
		synchronized( this )
		{
			if( type != MsgType.Replay && ! bRecvForceChat )
				return -4;			
		}

		Msg m = new Msg();
		m.msg = msg;
		m.roleid = id;
		m.lvlVIP = lvlVIP;
		m.rolename = name;
		m.forcename = forceInfo.name;
		m.lvl = lvl;
		m.headIconID = headIconID;
		m.time = gs.getTime();
		gs.getLoginManager().addForceChatMsg(forceInfo.id, m);
		fChatSyncStamp = m.time;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.SecTalkRecord secRecord = new TLogger.SecTalkRecord(TLog.SEC_TALK_CHAT, msg);
		TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(getForceID(), TLog.FORCE_CHAT);
		tlogEvent.addRecord(secRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
		return 0;
	}
	
	public boolean announceIsReplay(String msg)
	{
		boolean replay = false;
		if (msg.length() > 10) {
			String replayDat = msg.substring(0, 10);
			if (replayDat.equals("Rep!ayDat:"))
				replay = true;
		}
		return replay;
	}
	
	public boolean announceIsRecruit(String msg)
	{
		boolean recruit = false;
		final String tag = "Recru!tDat:";
		if (msg.length() > tag.length()) {
			String recruitDat = msg.substring(0, tag.length());
			if (recruitDat.equals(tag))
				recruit = true;
		}
		return recruit;
	}
	
	public boolean announceIsNormal(String msg)
	{
		return !announceIsReplay(msg) && !announceIsRecruit(msg);
	}
	
	public enum MsgType
	{
		Normal,
		Replay,
		Recruit,
		ZanOffer,
	}
	
	public int announceReplay(String image, String name1, String name2, int recId, int ftime)
	{
		if (isBanChat())
			return -1;
		int max = gs.getGameData().getUserInputCFG().worldChat;
		name1 = gs.getLoginManager().checkUserInput(name1, max, false, true);
		if( name1 == null )
			return -3;
		name2 = gs.getLoginManager().checkUserInput(name2, max, false, true);
		if( name2 == null )
			return -3;
		
		String msg = "Rep!ayDat:" + recId + "#" + ftime + "#" + image + "#" + name1 + "#" + name2;
		return sendAnnounceMsg(msg, MsgType.Replay);
	}
	
	public int announceRecruit(int forceID, String forceName, short forceIcon, int forceMemCnt, int forceMaxMemCnt, int forceJoinLvlReq, String m)
	{
		if (isBanChat())
			return -1;
		int max = gs.getGameData().getUserInputCFG().worldChat;
		forceName = gs.getLoginManager().checkUserInput(forceName, max, false, true);
		if( forceName == null )
			return -3;
		m = gs.getLoginManager().checkUserInput(m, max, false, true);
		if( m == null )
			return -3;
		
		String msg = "Recru!tDat:" + forceID + "#" + forceName + "#" + forceIcon + "#" + forceMemCnt + "#" + forceMaxMemCnt + "#" + forceJoinLvlReq + "#" + m;
		return sendAnnounceMsg(msg, MsgType.Recruit);
	}
	
	public int announceZanOffer(int rid, short pid, short reward, short count, String message)
	{
		if (isBanChat())
			return -1;
		
		int max = gs.getGameData().getUserInputCFG().worldChat;
		message = gs.getLoginManager().checkUserInput(message, max, false, true);
		if( message == null )
			return -2;
		
		String msg = "Z@nOfferDat:" + rid + "#" + pid + "#" + reward + "#" + count + "#" + message;
		return sendAnnounceMsg(msg, MsgType.ZanOffer);
	}
	
	public int announceNormal(String msg)
	{
		boolean bGMCommand = gs.testGMCommand() && msg.startsWith("!") && processGMCommand(msg.substring(1, msg.length()));
		
		if (bGMCommand) {
			
		}
		
		if (isBanChat())
			return -1;
		
		int max = gs.getGameData().getUserInputCFG().worldChat;
		msg = gs.getLoginManager().checkUserInput(msg, max, false, true);
		if( msg == null )
			return -3;
		return sendAnnounceMsg(msg, MsgType.Normal);
	}
	
	int sendAnnounceMsg(String msg, MsgType type)
	{
		
		short lvlReq = GameData.getInstance().getRoleCmnCFG().chatLvlReq;
		short iid = GameData.getInstance().getRoleCmnCFG().chatItemID;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( type == MsgType.Normal && ! bRecvWorldChat )
				return -4;
			if( gs.getConfig().godMode == 0 )
			{
				if( lvl < lvlReq )
					return -5;
				if (type != MsgType.Replay && type != MsgType.ZanOffer) 
				{
					Integer s = items.get(iid);
					if( s == null || s.shortValue() == 0 )
					{
						return -6;
					}
					commonFlowRecord.addChange(delItem(iid, (short)1));
				}
			}
		}

		Msg m = new Msg();
		m.msg = msg;
		m.roleid = id;
		m.lvlVIP = lvlVIP;
		m.rolename = name;
		m.forcename = forceInfo.name;
		m.lvl = lvl;
		m.headIconID = headIconID;
		m.time = gs.getTime();
		gs.getLoginManager().addWorldChatMsg(m);
		wChatSyncStamp = m.time;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.SecTalkRecord secRecord = new TLogger.SecTalkRecord(TLog.SEC_TALK_CHAT, msg);
		tlogEvent.addRecord(secRecord);
		commonFlowRecord.setEvent(TLog.AT_CHAT);
		commonFlowRecord.setArg(0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return 0;
	}
	
	public int privateAnnounce(int rid, String msg)
	{
		if (isBanChat())
			return -1;
		Role recvRole = gs.getLoginManager().getRole(rid);
		if( recvRole == null )
			return -2;
		
		int max = gs.getGameData().getUserInputCFG().worldChat;
		msg = gs.getLoginManager().checkUserInput(msg, max, false, true);
		if( msg == null )
			return -3;
		
		synchronized( this )
		{
			if( !bRecvPrivateChat )
				return -4;			
		}

		this.sendPrivateChatMsg(recvRole, msg);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.SecTalkRecord secRecord = new TLogger.SecTalkRecord(TLog.SEC_TALK_CHAT, msg);
		tlogEvent.addRecord(secRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return 0;
	}
	
	public void testPetEggAndUse()
	{
		short iid = 0;
		synchronized( this )
		{
			for(Entry<Short, Integer> e : items.entrySet())
			{
				if( e.getValue().shortValue() > 0 )
				{
					SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(e.getKey().shortValue());
					if( icfg != null && icfg.type == GameData.ITEM_TYPE_PET_EGG )
					{
						iid = e.getKey().shortValue();
						break;
					}
				}
			}
		}
		
		if( iid > 0 )
		{
			int[] ret = new int[1];
			ret[0] = 100;
			itemsUse(iid, (short)1, (short)0, ret);
		}
	}
	
	void changeName(String newName)
	{
		this.name = newName;
		citys.changeBaseCitysName(newName);
	}
	
	private TLogger.CommonItemChange addItem(short itemID, int count)
	{
		if( count <= 0 )
			return null;
		
		Integer c = items.get(itemID);
		int nOld = c == null ? 0 : c;//.shortValue()
		int iNew = nOld + count;
		int iOverflow = 0;
		int nNew = iNew;
		if( iNew > GameData.MAX_ITEM_COUNT )
		{
			iOverflow = iNew - GameData.MAX_ITEM_COUNT;
			money += GameData.getInstance().getItemPrice(itemID) * iOverflow; // TODO
			nNew = GameData.MAX_ITEM_COUNT;
		}
		items.put(itemID, nNew);
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_ITEM, itemID, (nNew - nOld), nNew);
	}

	private TLogger.CommonItemChange delEquip(short equipID, int count)
	{
		short delCount = 0;
		short afterCount = 0;
		Short c = equips.get(equipID);
		if( c == null )
			return null;
		short oldCount = c.shortValue();
		if( oldCount < count )
			return null;
		short newCount = (short)(oldCount - count);
		if( newCount <= 0 )
		{
			equips.remove(equipID);
			delCount = oldCount;
			afterCount = 0;
		}
		else
		{
			equips.put(equipID, newCount);
			delCount = (short)-(newCount-oldCount);
			afterCount = newCount;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_EQUIP, equipID, -delCount, afterCount);
	}
	
	public TLogger.CommonItemChange delItem(short itemID, int count)
	{
		int delCount = 0;
		int afterCount = 0;
		Integer c = items.get(itemID);
		if( c == null )
			return null;
		int oldCount = c;//.shortValue()
		if( oldCount < count )
			return null;
		int newCount = (oldCount - count);
		if( newCount <= 0 )
		{
			items.remove(itemID);
			delCount = oldCount;
			afterCount = 0;
		}
		else
		{
			items.put(itemID, newCount);
			delCount = (short)-(newCount-oldCount);
			afterCount = newCount;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_ITEM, itemID, -delCount, afterCount);
	}
	
	private TLogger.CommonItemChange addEquip(short equipID, int count)
	{
		if( count <= 0 )
			return null;
		
		Short c = equips.get(equipID);
		short nOld = c == null ? 0 : c.shortValue();
		int iNew = nOld + count;
		int iOverflow = 0;
		short nNew = (short)iNew;
		if( iNew > GameData.MAX_EQUIP_COUNT )
		{
			iOverflow = iNew - GameData.MAX_EQUIP_COUNT;
			money += GameData.getInstance().getEquipPrice(equipID) * iOverflow; // TODO
			nNew = GameData.MAX_EQUIP_COUNT;
		}
		equips.put(equipID, nNew);
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_EQUIP, equipID, (short)(nNew - nOld), nNew);
	}
	
	// TODO
	public TLogger.CommonItemChange addGeneral(short gid, byte evoLvl)
	{
		TLogger.CommonItemChange record = null;
		General g = generals.get(gid);
		if( g == null )
		{
			g = new General(gid, evoLvl);
			
			int seyen =0;
			for(General general : generals.values()){
				if(general!=null&&general.generalSeyen!=null&&general.generalSeyen.size()>0
						&&general.generalSeyen.get(0)!=null){
					if(general.generalSeyen.get(0).seyenTotal>seyen){
						seyen = general.generalSeyen.get(0).seyenTotal;
						break;
					}
				}
			}
			
			if(seyen>0&&g.generalSeyen.size()>0){
				g.generalSeyen.get(0).seyenTotal=seyen;
			}
			
			List<DBBestowLevel> seyen1 = new ArrayList<DBBestowLevel>();
			for(General general : generals.values()){
				if(general!=null&&general.bestow!=null&&general.bestow.size()>0
						&&general.bestow.get(0)!=null){
					if(general.bestow.get(0).bestowLevel.size()>0){
						seyen1 = general.bestow.get(0).bestowLevel;
						break;
					}
				}
			}
			
			if(seyen1.size()>0&&g.bestow.size()>=0){
				g.bestow.get(0).bestowLevel.clear();
				for(DBBestowLevel lvl :seyen1){
					g.bestow.get(0).bestowLevel.add(lvl);
				}
			}
			
			byte head =(byte)0;
			for(General general : generals.values()){
				if(general!=null&&general.headicon>0){
					head = general.headicon;
					break;
				}
			}
			
			if(head>0){
				g.headicon = head;
			}
			
			generals.put(gid, g);
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_GENERAL, gid, evoLvl, evoLvl);
		}
		else
		{
			SBean.GeneralCFGS gcfg = GameData.getInstance().getGeneralCFG(gid);
			short iid = gcfg.stoneID;
			short cnt = GameData.getInstance().getGeneralCmnCFG().evoLevels.get(evoLvl-1).stoneBack;
			record = addItem(iid, cnt);
		}
		return record;
	}
	
	public TLogger.CommonItemChange addPet(short pid, short growthRate)
	{
		final byte evoLvl = (byte)1;
		TLogger.CommonItemChange record = null;
		Pet p = pets.get(pid);
		if( p == null )
		{
			p = new Pet(pid, evoLvl, growthRate);
			pets.put(pid, p);
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_PET, pid, evoLvl, evoLvl);
			if (!petDeforms.containsKey(p.id)) {
				SBean.DBPetDeform dbp = new SBean.DBPetDeform(p.id, (byte)0, 0, (byte)0, 0, (byte)0, (byte)0, 0, (byte)0, new ArrayList<DropEntry>(), (byte)0, (byte)0, 0, 0, 0);
				checkDeformHappiness(dbp, gs);
				
				petDeforms.put(p.id, dbp);
			}
		}
		else
		{
			if( growthRate > p.growthRate )
				p.growthRate = (byte)growthRate;
			SBean.PetCFGS cfg = GameData.getInstance().getPetCFG(pid, false);
			short iid = cfg.pieceID;
			short cnt = (short)(cfg.evo.get(0).pieceCountDec);
			record = addItem(iid, cnt);
		}
		return record;
	}
	
	public synchronized TLogger.CommonItemChange useMoneyOrStoneWithLock(byte mtype, int m)
	{
		if( m < 0 )
			//return false;
			return null;
		switch( mtype )
		{
		case GameData.CURRENCY_UNIT_MONEY:
			{
				if( money < m )
					//return false;
					return null;
				//money -= m;
				return useMoney(m);
				//return true;
			}
		case GameData.CURRENCY_UNIT_STONE:
			{
				if( stone < m )
					//return false;
					return null;
				return useStone(m);
				//return true;
			}
		default:
			break;
		}
		//return false;
		return null;
	}
	
	public synchronized void addCombatBonus(SBean.CombatBonus cb)
	{
		addMoney(cb.money);
		addStonePresent(cb.stone*cb.stoneTimes);
		addExp(cb.exp, null);
		addDropsNew(cb.entryIDs);
	}
	
	public TLogger.CommonItemChange addMoney(int moneyAdd)
	{
		if( moneyAdd <= 0 )
			return null;
		
		long moneyAll = money + moneyAdd;
		if( moneyAll > GameData.MAX_MONEY )
			moneyAll = GameData.MAX_MONEY;
		
		money = (int)moneyAll;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MONEY, 0, moneyAdd, money);
	}
	
	public TLogger.CommonItemChange useMoney(int moneyAdd)
	{
		if (moneyAdd <= 0)
			return null;
		money -= moneyAdd;
		consumeGiftLogConsume(moneyAdd, true);
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MONEY, 0, -moneyAdd, money);
	}
	
	public TLogger.CommonItemChange addStonePay(int stoneAdd)
	{
		if (stoneAdd <= 0)
			return null;
		stone += stoneAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 0, stoneAdd, stone);
	}
	
	public TLogger.CommonItemChange addStonePresent(int stoneAdd)
	{
		if (stoneAdd <= 0)
			return null;
		//
		if( gs.getConfig().payState == 1 )
			stoneMonitorList.add(stoneAdd);
		//
		stone += stoneAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 1, stoneAdd, stone);
	}
	
	public TLogger.CommonItemChange useStone(int yb)
	{
		return useStone(yb, true);
	}
	
	public TLogger.CommonItemChange useStone(int yb, boolean log)
	{
		if (yb <= 0)
			return null;
		if( gs.getConfig().payState == 1 )
			stoneMonitorList.add(-yb);
		stone -= yb;
		stoneUsedTotal += yb;
		if (log) {
			consumeGiftLogConsume(yb, false);
			consumeRebateLogConsume(yb);
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 0, -yb, stone);
	}
	
	public synchronized TLogger.CommonItemChange useStoneWithLock(int yb)
	{
		if( stone >= yb && yb >= 0 )
		{
			return useStone(yb);
			//return true;
		}
		//return false;
		return null;
	}
	
	public synchronized TLogger.CommonItemChange addPayVipExp(int val)
	{
		if (val <= 0)
			return null;
		this.stonePayAlpha += val;
		this.updateVipLevel();
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIP_EXP, 0, val, this.getStonePayTotal());
	}
	
	public synchronized void payCancel(String uuid)
	{
		Iterator<DBRole.PayLog> iter = payLogs.iterator();
		while( iter.hasNext() )
		{
			DBRole.PayLog e = iter.next();
			if( e.uuid.equals(uuid) )
			{
				iter.remove();
			}
		}
	}
	
	public synchronized void payConfirm(String uuid)
	{
		DBRole.PayLog r = null;
		Iterator<DBRole.PayLog> iter = payLogs.iterator();
		while( iter.hasNext() )
		{
			DBRole.PayLog e = iter.next();
			if( e.uuid.equals(uuid) )
			{
				iter.remove();
				r = e;
				break;
			}
		}
		if( r != null )
			payLogs.add(r);
	}
	
	public interface PaySyncCallback
	{
		public void onCallback(boolean bOK, int errCode, int ret);
	}
	public void paySync(final PaySyncCallback paySyncCallback)
	{
		final Midas.UserInfo uinfo = new Midas.UserInfo(msdkInfo.openID, msdkInfo.openKey, 
				msdkInfo.payToken, msdkInfo.pf, msdkInfo.pfKey,
				msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSIDGuest:gs.getConfig().offerCookieSID,
				msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSTypeGuest:gs.getConfig().offerCookieSType);
		gs.getMidas().getBalance(uinfo, new Midas.GetBalanceCallback()
				{										
					@Override
					public void onCallback(UserInfo uinfo, BalanceResult res)
					{
						// TODO
						boolean bOK = false;
						if( res.errCode == 0 && res.ret == 0 )
						{							
							int payTotal = res.save_amt;
							int balance = res.balance;
							//int gen_balance = res.gen_balance; // TODO
							// TODO
							payFinish(payTotal);
							if( stone > balance )
							{
								gs.getLogger().debug("auto present on paysync pay ok:[" + userName +"(" + id + ")],+"+(stone-balance));
								gs.getMidas().present(uinfo, stone - balance);
							}
							else if( stone < balance )
							{
								gs.getLogger().debug("auto pay on paysync pay ok:[" + userName +"(" + id + ")],-"+(balance-stone));
								gs.getMidas().pay(uinfo, balance - stone);
							}
							bOK = true;
						}
						else
							gs.getLogger().debug("auto pay on paysync pay err:[" + userName +"(" + id + ")], err="+res.errCode+",retcode="+res.ret);
						if( paySyncCallback != null )
							paySyncCallback.onCallback(bOK, res.errCode, res.ret);
					}
				}, null);
	}
	
	public void paySyncPayRes()
	{
		gs.getMidas().getBalance(new Midas.UserInfo(msdkInfo.openID, msdkInfo.openKey, 
				msdkInfo.payToken, msdkInfo.pf, msdkInfo.pfKey,
				msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSIDGuest:gs.getConfig().offerCookieSID,
				msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSTypeGuest:gs.getConfig().offerCookieSType), new Midas.GetBalanceCallback()
				{										
					@Override
					public void onCallback(UserInfo uinfo, BalanceResult res)
					{
						// TODO
						if( res.errCode == 0 && res.ret == 0 )
						{
							int payTotal = res.save_amt;
							// TODO
							payFinish(payTotal);
							Role.this.testDTask2Notice();
						}
						else
							gs.getLogger().warn("sync payres err:[" + userName +"(" + id + ")], err="+res.errCode+",retcode="+res.ret);
					}
				}, null);
	}
	
	public void payDiskBetLogPay(int pay)
	{
		List<RechargeRankConfig> cfgs = gs.getGameActivities().getRechargeRankActivity().getOpenedConfigs();
		for (RechargeRankConfig cfg : cfgs)
		{
			payDiskBet(cfg, pay);
		}
		
		List<RechargeGiftConfig> cfgs1 = gs.getGameActivities().getRechargeGiftActivity().getOpenedConfigs();
		for (RechargeGiftConfig cfg : cfgs1)
		{
			payDiskBet1(cfg, pay);
		}
	}
	
	public void payDiskBet(RechargeRankConfig cfg, int pay)
	{
		DBDiskBetInfo cg = syncPayDiskBetState(cfg);
		if (cg == null)
			return;
		cg.rmb += pay;
		
		if(cg.rmb>=cfg.MIN_RMB){
			gs.getDiskManager2().setDamage((short) 2,diskBet2.rmb, id, name,"12", headIconID, lvl);
		}
	}
	
	public void payDiskBet1(RechargeGiftConfig cfg, int pay)
	{
		DBDiskBetInfo cg = syncPayDiskBetState1(cfg);
		if (cg == null)
			return;
		cg.rmb += pay;
		
//		if(cg.rmb>=cfg.MIN_RMB){
//			gs.getDiskManager2().setDamage((short) 2,diskBet.rmb, id, name,"12", headIconID, lvl);
//		}
	}
	
	DBDiskBetInfo syncPayDiskBetState1(RechargeGiftConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		if(diskBet3==null||diskBet3.id!=id){
			diskBet3 = new SBean.DBDiskBetInfo();
			diskBet3.id = id;
		}
		return diskBet3;
	}
	
	DBDiskBetInfo syncPayDiskBetState(RechargeRankConfig cfg)
	{
		if (cfg == null)
			return null;
		int id = cfg.getID();
		
		if(diskBet2==null||diskBet2.id!=id){
			diskBet2 = new SBean.DBDiskBetInfo();
			diskBet2.id = id;
			gs.getDiskManager2().diskBet.statime = cfg.start;
			gs.getDiskManager2().diskBet.endtime = cfg.end;
			gs.getDiskManager2().diskBet.minscore = cfg.MIN_SCORE;
			gs.getDiskManager2().diskBet.minRmb = cfg.MIN_RMB;
			gs.getDiskManager2().mailatt5=cfg.mailatt5;
		}
		
		return diskBet2;
	}
	
	public void payFinish(int payTotal)
	{		//
		byte lvlVIPNotice = 0;
		int stoneCal = 0;
		int rmbCal = 0;
		boolean bSetMCard = false;
		String uuid = "";
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( payTotal <= stonePayMidas )
				return;
			int stoneOld = stone;
			int rmbLeft = (payTotal - stonePayMidas) / 10;
			rmbCal = rmbLeft;
			//
			firstPayGiftLogPay(rmbLeft);
			payGiftLogPay(rmbLeft);
			payDiskBetLogPay(rmbLeft);
			
			//
			if( rmbLeft > 0 )
			{
				byte matchLvl = -1;
				{
					if( rmbLeft == GameData.getInstance().getMonthlyCardCFG().price && ! bSetMCard)
					{
						matchLvl = 0;
					}
					else
					{
						SBean.PayCFGS cfg = GameData.getInstance().getPayCFG(userName);
						for(int i = cfg.levels.size()-1; i>=0; --i)
						{
							SBean.PayCFGLevelS e = cfg.levels.get(i);
							if( rmbLeft == e.rmb )
							{
								matchLvl = (byte)(i+1);
								break;
							}
						}
					}
				}
				
				if( matchLvl != -1 )
				{
					if( payLogs.size() == 1 && gs.getPayLvlOfUUID(payLogs.get(0).uuid) == matchLvl )
					{
						
					}
					else
						payLogs.clear();
				}
			}
			//
			
			ListIterator<DBRole.PayLog> rIter = payLogs.listIterator(payLogs.size());
			while( rIter.hasPrevious() )
			{
				DBRole.PayLog e = rIter.previous();				
				byte payLvl = gs.getPayLvlOfUUID(e.uuid);				
				int rmb = 0;
				if( payLvl == 0 )
				{
					if( bSetMCard )
					{
						rIter.remove();
						continue;
					}
					rmb = GameData.getInstance().getMonthlyCardCFG().price;
				}
				else
				{
					rmb = GameData.getInstance().getPayPrice(userName, payLvl);
				}
				if( rmb > rmbLeft )
				{
					continue;
				}
				rIter.remove();
				if( payLvl == 0 )
					bSetMCard = true;
				TLogger.PayRecord record = new TLogger.PayRecord(lvl, GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
				commonFlowRecord.addChanges(payAdd(payLvl, rmb, record));
				record.setAfterPayState(GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
				tlogEvent.addRecord(record);
				uuid += e.uuid + ",";
				rmbLeft -= rmb;
				if( rmbLeft == 0 )
					break;
			}
			if( rmbLeft > 0 )
			{
				//
				if( rmbLeft == GameData.getInstance().getMonthlyCardCFG().price && ! bSetMCard)
				{
					bSetMCard = true;
					TLogger.PayRecord record = new TLogger.PayRecord(lvl, GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
					commonFlowRecord.addChanges(payAdd((byte)0, rmbLeft, record));
					record.setAfterPayState(GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
					uuid += "pay_sync_guess_0,";
					tlogEvent.addRecord(record);
				}
				else
				{
					SBean.PayCFGS cfg = GameData.getInstance().getPayCFG(userName);
					
					byte lvlGuess = -1;
					for(int i = cfg.levels.size()-1; i>=0; --i)
					{
						SBean.PayCFGLevelS e = cfg.levels.get(i);
						if( rmbLeft == e.rmb )
						{
							lvlGuess = (byte)(i+1);
							break;
						}
					}
					if( lvlGuess > 0 )
					{
						TLogger.PayRecord record = new TLogger.PayRecord(lvl, GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
						commonFlowRecord.addChanges(payAdd(lvlGuess, rmbLeft, record));
						record.setAfterPayState(GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
						uuid += "pay_sync_guess_" + lvlGuess + ",";
						tlogEvent.addRecord(record);
					}
					else
					{
						TLogger.PayRecord record = new TLogger.PayRecord(lvl, GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
						commonFlowRecord.addChanges(payAdd((byte)1, rmbLeft, record));
						record.setAfterPayState(GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
						uuid += "pay_sync_left,";
						tlogEvent.addRecord(record);
					}
				}
			}
			
			stonePayMidas = payTotal;
			
//			byte vipTest = gs.getGameData().testVIPUPgrade(lvlVIP, getStonePayTotal());
//			if( vipTest > 0 )
//			{
//				lvlVIP = vipTest;
//				if( vitRecoverStart == 0 && vit < getVitMax() )
//					vitRecoverStart = gs.getTime();
//				if( skillPointRecoverStart == 0 && skillPoint < getSkillPointMax() )
//					skillPointRecoverStart = gs.getTime();
//				lvlVIPNotice = lvlVIP;
//			}
			lvlVIPNotice = updateVipLevel();
			lastSaveTime = -1;
			stoneCal = stone - stoneOld;
		}
		if( lvlVIPNotice > 0 )
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeVIPUpdate(this.lvlVIP, getStonePayTotal(), true));
		if( stoneCal > 0 )
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodePaySync(stoneCal, bSetMCard, getStonePayTotal()));
		gs.getLogger().info("user finish pay ok:[" + userName +"(" + id + ")], id=["
				+ uuid + "],rmb="+rmbCal+",stone="+stoneCal+",mcard="+bSetMCard);
		commonFlowRecord.setEvent(TLog.AT_USER_PAY);
		commonFlowRecord.setArg(rmbCal, stoneCal, bSetMCard ? 1 : 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void paySyncTable(final int sessionid)
	{
		SBean.PayCFGS cfg = gs.getGameData().getPayCFG(userName);
		SBean.MonthlyCardCFGS mcard = null;
		int mcardStart = getMonthlyCardStartTime();
		if( cfg.flag != 0 ) // monthly card
		{
			mcard = gs.getGameData().getMonthlyCardCFG();
		}
		List<Byte> payLvlState = copyPayLvlStateWithLock();
		Set<Byte> payDouble = new TreeSet<Byte>();
		for(byte i = 1; i <= cfg.levels.size(); ++i)
		{
			if( GameData.getInstance().isDoublePayLevel(i) )
				payDouble.add(i);
		}
		for(byte e : payLvlState)
		{
			payDouble.remove(new Byte(e));
		}
		gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodePayTable(cfg, mcard, mcardStart, payDouble));
	}
	
	public void payInit(byte payLvl, int rmb)
	{
		String uuid = "";
		synchronized( this )
		{
			uuid = gs.getUUIDWithGSID(payLvl);
			while( true )
			{
				boolean bFound = false;
				for(DBRole.PayLog e : payLogs)
				{
					if( e.uuid.equals(uuid) )
					{
						bFound = true;
						break;
					}
				}
				if( ! bFound )
					break;
				uuid = gs.getUUIDWithGSID(payLvl);
			}
			// remove old
			int now = gs.getTime();
			Iterator<DBRole.PayLog> iter = payLogs.iterator();
			while( iter.hasNext() )
			{
				DBRole.PayLog e = iter.next();
				if( e.timeStamp + 43200 < now )
				{
					iter.remove();
				}
			}
			if( payLogs.size() > 64 )
			{
				int nRemove = 0;
				iter = payLogs.iterator();
				while( iter.hasNext() && nRemove < 32)
				{
					//DBRole.PayLog e = 
					iter.next();
					iter.remove();
					++nRemove;
				}
			}
			//
			DBRole.PayLog e = new DBRole.PayLog();
			e.uuid = uuid;
			e.timeStamp = now;
			payLogs.add(e);
		}
		
		//
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodePayInit(uuid, rmb, gs.getConfig().zoneID));
		gs.getLogger().info("user init pay:[" + userName +"(" + id + ")], id=["
				+ uuid +"],rmb=" + rmb + ",payLvl=" + payLvl + ",zoneID=" + gs.getConfig().zoneID);
		//
	}
	
	public void payAddGod(byte payLvl, int rmb)
	{				
		byte lvlVIPNotice = -1;		
		//
		boolean bSetMCard = false;
		int stoneCal = 0;
							
		if( payLvl == 0 && rmb >= GameData.getInstance().getMonthlyCardCFG().price )
		{
			bSetMCard = true;
		}
		stoneCal = GameData.getInstance().getStoneByRMB(userName, rmb, testPayLvlDoubleWithLock(payLvl));
		int stonePay = rmb * 10;
		int stonePresent = stoneCal - stonePay;
		if( stonePresent < 0 )
			stonePresent = 0;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.PayRecord record = new TLogger.PayRecord(lvl, GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
		record.setPayInfo(testPayLvlDoubleWithLock(payLvl) > 0 ? -payLvl : payLvl, rmb, stonePay + stonePresent);
		synchronized( this )
		{
			firstPayGiftLogPay(rmb);
			payGiftLogPay(rmb);
			payDiskBetLogPay(rmb);
			stonePayMidas += stonePay;
			commonFlowRecord.addChanges(addPay(payLvl, stonePay, stonePresent));
			record.setAfterPayState(GameData.getInstance().getVIPLevel(getStonePayTotal()), getStonePayTotal());
			if( bSetMCard )
				setMonthlyCard();
//			byte vipTest = gs.getGameData().testVIPUPgrade(lvlVIP, getStonePayTotal());
//			if( vipTest > 0 )
//			{
//				lvlVIP = vipTest;
//				if( vitRecoverStart == 0 && vit < getVitMax() )
//					vitRecoverStart = gs.getTime();
//				if( skillPointRecoverStart == 0 && skillPoint < getSkillPointMax() )
//					skillPointRecoverStart = gs.getTime();
//				lvlVIPNotice = lvlVIP;
//			}
			lvlVIPNotice = updateVipLevel();
		}
		
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodePaySync(stoneCal, bSetMCard, getStonePayTotal()));
		
		gs.getLogger().warn("user finish pay ok:[" + userName +"(" + id + ")], id=[god],rmb="+rmb+",stone="+stoneCal+",mcard="+bSetMCard);
		
		if( lvlVIPNotice > 0 )
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeVIPUpdate(this.lvlVIP, getStonePayTotal(), true));
		commonFlowRecord.setEvent(TLog.AT_GOD_PAY);
		commonFlowRecord.setArg(rmb, stoneCal, bSetMCard ? 1 : 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		tlogEvent.addRecord(record);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	byte updateVipLevel()
	{
		byte vipLvl = GameData.getInstance().getVIPLevel(getStonePayTotal());
		byte oldlvlVIP = this.lvlVIP;
		this.lvlVIP = vipLvl;
		if (vipLvl > oldlvlVIP)
		{
			if( vitRecoverStart == 0 && vit < getVitMax() )
				vitRecoverStart = gs.getTime();
			if( skillPointRecoverStart == 0 && skillPoint < getSkillPointMax() )
				skillPointRecoverStart = gs.getTime();
		}
		return (byte)(vipLvl-oldlvlVIP);
	}
	
	public byte getQQVIP()
	{
		if( gs.getConfig().iosExamineServer == 1 )
			return (byte)0;
		if( testFlag(Role.ROLE_FLAG_QQ_VIP_SUPER) )
			return (byte)2;
		if( testFlag(Role.ROLE_FLAG_QQ_VIP_NORMAL) )
			return (byte)1;
		return (byte)0;
	}
	
	public void queryQQVIP()
	{
		// TODO when ?
		final boolean bQQ = GameData.getInstance().isQQGameServerID(gs.getConfig().id);
		final boolean bGuest = GameData.getInstance().isGuestOpenID(msdkInfo.openID);
		if( bQQ && ! bGuest)
		{
			gs.getMidas().queryQQVIP(msdkInfo.openID, id, new Midas.QueryQQVIPCallback()
			{
				@Override
				public void onCallback(int rid, byte res)
				{
					gs.getLogger().debug("Mias.QueryQQVIPCallback, rid= " + rid + ", res=" + res);
					if( res < 0 )
						return;
					Role role = gs.getLoginManager().getRole(rid);
					if( role != null )
					{
						role.setQQVIP(res);
						if( gs.getConfig().iosExamineServer == 0 )
						{
							gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeQQVIPSync(res, isAddQQVip(1), isAddQQVip(2)));
							testIOSSendQQVipRewardsMail();
						}
					}
				}
			});
		}
		//
	}
	
	public void setQQVIP(byte qqVIP)
	{
		if( qqVIP >= 2 )
		{
			setFlag(Role.ROLE_FLAG_QQ_VIP_NORMAL);
			setFlag(Role.ROLE_FLAG_QQ_VIP_SUPER);
		}
		else if( qqVIP == 1 )
		{
			setFlag(Role.ROLE_FLAG_QQ_VIP_NORMAL);
			clearFlag(Role.ROLE_FLAG_QQ_VIP_SUPER);
		}
		else
		{
			clearFlag(Role.ROLE_FLAG_QQ_VIP_NORMAL);
			clearFlag(Role.ROLE_FLAG_QQ_VIP_SUPER);
		}
	}
		
	public boolean isAddQQVip(int qqVIP)
	{
		if (qqVIP == 1)
		{
			return (this.qqVipRewards & ROLE_FLAG_QQ_VIP_NORMAL_ADD) != 0;
		}
		else if (qqVIP == 2)
		{
			return (this.qqVipRewards & ROLE_FLAG_QQ_VIP_SUPER_ADD) != 0;
		}
		return false;
	}
	
	public void addQQVip(int qqVIP)
	{
		if (qqVIP == 1)
		{
			this.qqVipRewards |= ROLE_FLAG_QQ_VIP_NORMAL_ADD;
		}
		else if (qqVIP == 2)
		{
			this.qqVipRewards |= ROLE_FLAG_QQ_VIP_NORMAL_ADD;
			this.qqVipRewards |= ROLE_FLAG_QQ_VIP_SUPER_ADD;
		}
	}
	
	public void addQQVipFinish(int openType, boolean godMod)
	{
		if (openType == 1)
		{
			if (getQQVIP() == 1 || getQQVIP() == 2)
			{
				addQQVip(1);
			}
			if (godMod)
			{
				if (getQQVIP() != 2)
					setQQVIP((byte)1);
			}
		}
		else if (openType == 2)
		{
			if (godMod)
			{
				setQQVIP((byte)2);
			}
		}
		else if (openType == 3)
		{
			if (getQQVIP() == 2)
			{
				addQQVip(2);
			}
			if (godMod)
			{
				setQQVIP((byte)2);
			}
		}
	}
	
	public byte getQQVIPRewards()
	{
		return qqVipRewards;
	}
	
	public boolean takeQQVIPReward(int vipRewardType)
	{
		List<SBean.QQVIPRewardsCFGS> cfgs = GameData.getInstance().getQQVIPRewardsCFG();
		if (vipRewardType <= 0 || vipRewardType > cfgs.size())
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			SBean.QQVIPRewardsCFGS cfg = cfgs.get(vipRewardType-1);
			if (vipRewardType == 1 || vipRewardType == 2)
			{
				if (getQQVIP() != 1 && getQQVIP() != 2)
					return false;
			} 
			else if (vipRewardType == 3 || vipRewardType == 4)
			{
				if (getQQVIP() != 2)
					return false;
			}
			int mask = 1 << (vipRewardType-1);
			if ((this.qqVipRewards & mask) != 0)
				return false;
			commonFlowRecord.addChanges(addDropsNew(cfg.rewards));
			this.qqVipRewards |= mask;
		}
		commonFlowRecord.setEvent(TLog.AT_QQ_VIP_REWARD);
		commonFlowRecord.setArg(vipRewardType);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	void testIOSSendQQVipRewardsMail()
	{
		if (gs.getConfig().platID == 0 && gs.getConfig().areaID == 2)
		{
			List<SBean.QQVIPRewardsCFGS> cfgs = GameData.getInstance().getQQVIPRewardsCFG();
			synchronized( this )
			{
				int qqvip = getQQVIP();
				if (qqvip == 1)
				{
					if ((this.qqVipRewards & ROLE_FLAG_QQ_VIP_REWARD_NORMAL_NEW) == 0)
					{
						SBean.QQVIPRewardsCFGS cfg = cfgs.get(0);
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						attLst.addAll(cfg.rewards);
						gs.getLoginManager().sysSendMessage(0, this.id, DBMail.Message.SUB_TYPE_QQ_VIP_REWARD, "", "" + qqvip, 0, true, attLst);	
						this.qqVipRewards |= ROLE_FLAG_QQ_VIP_REWARD_NORMAL_NEW;
					}	
				}
				else if (qqvip == 2)
				{
					if ((this.qqVipRewards & ROLE_FLAG_QQ_VIP_REWARD_NORMAL_NEW) == 0)
					{
						SBean.QQVIPRewardsCFGS cfg = cfgs.get(0);
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						attLst.addAll(cfg.rewards);
						gs.getLoginManager().sysSendMessage(0, this.id, DBMail.Message.SUB_TYPE_QQ_VIP_REWARD, "", "" + qqvip, 0, true, attLst);
						this.qqVipRewards |= ROLE_FLAG_QQ_VIP_REWARD_NORMAL_NEW;
					}
					if ((this.qqVipRewards & ROLE_FLAG_QQ_VIP_REWARD_SUPER_NEW) == 0)
					{
						SBean.QQVIPRewardsCFGS cfg = cfgs.get(2);
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						attLst.addAll(cfg.rewards);
						gs.getLoginManager().sysSendMessage(0, this.id, DBMail.Message.SUB_TYPE_QQ_VIP_REWARD, "", "" + qqvip, 0, true, attLst);
						this.qqVipRewards |= ROLE_FLAG_QQ_VIP_REWARD_SUPER_NEW;
					}
				}
			}
		}
	}
	
	private List<TLogger.CommonItemChange> payAdd(byte payLvl, int rmb, TLogger.PayRecord record)
	{				
		//
		List<TLogger.CommonItemChange> tlogRecords = null;
		boolean bSetMCard = false;
		int stoneCal = 0;
		int stonePay = 0;					
		if( payLvl == 0 && rmb >= GameData.getInstance().getMonthlyCardCFG().price )
		{
			bSetMCard = true;
		}
		stonePay = rmb * 10;
		stoneCal = GameData.getInstance().getStoneByRMB(userName, rmb, testPayLvlDoubleWithLock(payLvl));
		int stonePresent = stoneCal - stonePay;
		if( stonePresent < 0 )
			stonePresent = 0;
		
		record.setPayInfo(testPayLvlDoubleWithLock(payLvl) > 0 ? -payLvl : payLvl, rmb, stonePay + stonePresent);
		stonePayMidas += stonePay;
		tlogRecords = addPay(payLvl, stonePay, stonePresent);
		if( bSetMCard )
			setMonthlyCard();
		return tlogRecords;
	}
	
	private TLogger.CommonItemChange subVit(int vitSub)
	{
		if( vitSub <= 0 )
			return null;
		int beforeVit = vit;
		vit -= vitSub;
		if( vit < 0 )
			vit = 0;
		if( vitRecoverStart == 0 )
			vitRecoverStart = gs.getTime();
		int add = beforeVit-vit;
		activity += add;
		if (forceInfo.id > 0)
			gs.getForceManager().addActivity(forceInfo.id, add);
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 0, vit-beforeVit, vit);
	}
	
	private TLogger.CommonItemChange addVit1(int vitAdd)
	{
		if( vitAdd <= 0 )
			return null;
		short max = getVitMax();
		if( vit >= max )
			return null;
		int beforeVit = vit;
		vit = (short)(vit + vitAdd);
		if( vit >= max  || vit <= 0 )
		{
			vit = max;
			vitRecoverStart = 0;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 0, vit-beforeVit, vit);
	}
	private TLogger.CommonItemChange addVit2(int vitAdd)
	{
		if( vitAdd <= 0 )
			return null;
		short max1 = getVitMax();
		short max2 = gs.getGameData().getRoleCmnCFG().vitMax;
		if( vit >= max2 )
			return null;
		int beforeVit = vit;
		vit = (short)(vit + vitAdd);
		if( vit >= max2  || vit <= 0 )
		{
			vit = max2;
		}
		if( vit >= max1 )
		{
			vitRecoverStart = 0;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 0, vit-beforeVit, vit);
	}
	
	public class GMCommand
	{
		public String data = "";
		public String command = "";
		public int arg1 = 0;
		public int arg2 = 0;
		public int arg3 = 0;
		public int res = -1;
	}
	
	private GMCommand parseGMCommand(String cmd)
	{
		String[] v = cmd.split(" ");
		if( v == null || v.length <= 0 )
			return null;
		try
		{
			GMCommand c = new GMCommand();
			c.data = v[0].trim();
			switch( c.data)
			{
			case "money":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "stone":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "item":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				c.arg2 = Integer.parseInt(v[3]);
				return c;
			case "equip":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				c.arg2 = Integer.parseInt(v[3]);
				return c;
			case "general":
				c.command = v[1].trim();
				if( v.length > 2 )
					c.arg1 = Integer.parseInt(v[2]);
				if( v.length > 3 )
					c.arg2 = Integer.parseInt(v[3]);
				return c;
			case "role_level":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "vit":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "combat":
				c.command = v[1].trim();
				return c;
			case "arena_point":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "arena_rank":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "superarena_point":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "march_point":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "country_point":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "country_contrib":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "rich_point":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "forcewar_memcnt":
				c.command = "set";
				c.arg1 = Integer.parseInt(v[2]);
				return c;
			case "time":
				c.command = v[1].trim();
				if( c.command.equals("clear") )
					c.arg1 = 0;
				else if( c.command.equals("add") )
				{
					c.arg1 = 1;
					c.arg2 = Integer.parseInt(v[2]);
				}
				else if( c.command.equals("set") )
				{
					c.arg1 = 2;
					String[] v2 = v[2].trim().split("_");
					c.arg2 = StringConverter.parseDayOfYear(v2[0]) + StringConverter.parseMinuteOfDay(v2[1]) * 60
							+ GameData.getTimeZone() * 3600;
				}
				else
					return null;
				return c;
			case "duel":
				c.command = v[1].trim();
				if( c.command.equals("set_defaultlevel") )
					c.arg1 = Integer.parseInt(v[2]);
				else if (c.command.equals("set_defaultscore"))
					c.arg1 = Integer.parseInt(v[2]);
				return c;
			default:
				return null;
			}
		}
		catch(Exception ex)
		{
			gs.getLogger().warn("GMCommand parse err", ex);
		}
		return null;
	}
	
	private boolean processGMCommand(String cmd)
	{
		gs.getLogger().warn("GMCommand (" + id + ") cmd=[" + cmd + "]");
		GMCommand command = parseGMCommand(cmd);
		if( command == null )
			return false;
		switch( command.data )
		{
		case "money":	// only set
			{
				int money = command.arg1;
				if( money < 0 || money > GameData.MAX_MONEY )
				{
					command.res = -2;
					break;
				}
				this.money = money;
				command.res = 0;
			}
			break;
		case "stone":	// only set
			{
				int stone = command.arg1;
				if( stone < 0 || stone > GameData.MAX_STONE )
				{
					command.res = -2;
					break;
				}
				this.stone = stone;
				command.res = 0;
			}
			break;
		case "vit":	// only set
			{
				short vit = (short)command.arg1;
				if( vit < 0 || vit > 30000 )
				{
					command.res = -2;
					break;
				}
				this.vit = vit;
				command.res = 0;
			}
			break;
		case "combat":
			{
				switch( command.command )
				{
				case "unlock":
					{
						GameData.getInstance().updateMaxCombatPos((byte)0, combatPos[0]);
						GameData.getInstance().updateMaxCombatPos((byte)1, combatPos[1]);
						for (int i = 1; i < this.combatPos[0].catIndex+1; ++i)
							if (GameData.getInstance().getCityCfg(i) != null)
								this.citys.unlockBaseCity(i);
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeCombatDataSync(
									new ArrayList<SBean.DBCombatLog>(),
									new ArrayList<SBean.DBCombatLog>(),
									copyCombatPosWithoutLock()
								));
						// set all star 3
						{
							combatScores.clear();
							for(byte ctype = 0; ctype < 2; ++ctype)
							{
								SBean.DBCombatScore cs = new SBean.DBCombatScore();
								cs.combatType = ctype;
								cs.scores = new ArrayList<SBean.DBCombatTypeScore>();
								combatScores.add(cs);
								List<SBean.DBCombatTypeScore> slst = cs.scores;
								for(byte cindex = 0; cindex <= combatPos[0].catIndex; ++cindex)
								{
									SBean.DBCombatTypeScore cts = new SBean.DBCombatTypeScore();
									cts.catID = cindex;
									cts.flag = SBean.DBCombatTypeScore.eAllComplete;
									cts.score = new ArrayList<Byte>();
									for(byte bindex = 0; bindex < 18; ++bindex)
										cts.score.add((byte)3);
									slst.add(cts);
								}
							}
						}
						//
						command.res = 0;
					}
					break;
				default:
					break;
				}
			}
			break;
		case "item":	// only set
			{
				short iid = (short)command.arg1;
				int count = command.arg2;//(short)
				if( count < 0 || count > GameData.MAX_ITEM_COUNT )
				{
					command.res = -2;
					break;
				}
				SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(iid);
				if( icfg == null )
				{
					if (iid == 0) {
						List<Short> ids = new ArrayList<Short>();
						for (Entry<Short, Integer> e : items.entrySet()) {
							if (count > 0)
								e.setValue(count);
							else
								ids.add(e.getKey());
						}
						
						for (short id : ids)
							items.remove(id);
						command.res = 0;
					}
					else
						command.res = -3;
					break;
				}
				if( count > 0 )
					items.put(iid, count);
				else
					items.remove(iid);
				command.res = 0;
			}
			break;
		case "equip":	// only set
			{
				short eid = (short)command.arg1;
				short count = (short)command.arg2;
				if( count < 0 || count > GameData.MAX_EQUIP_COUNT )
				{
					command.res = -2;
					break;
				}
				SBean.EquipCFGS ecfg = GameData.getInstance().getEquipCFG(eid);
				if( ecfg == null )
				{
					if (eid == 0) {
						List<Short> ids = new ArrayList<Short>();
						for (Map.Entry<Short, Short> e : equips.entrySet()) {
							if (count > 0)
								e.setValue(count);
							else
								ids.add(e.getKey());
						}
						
						for (short id : ids)
							equips.remove(id);
						command.res = 0;
					}
					else
						command.res = -3;
					break;
				}
				if( count > 0 )
					equips.put(eid, count);
				else
					equips.remove(eid);
				command.res = 0;
			}
			break;
		case "general":
			{
				switch( command.command )
				{
				case "set_level":
					{
						short gid = (short)command.arg1;
						short glvl = (short)command.arg2;
						if( glvl < 0 || gid < 0 )
						{
							command.res = -2;
							break;
						}
						
						SBean.GeneralCFGS gcfg = null;
						
						if( gid != 0 )
						{
							gcfg = GameData.getInstance().getGeneralCFG(gid);
							if( gcfg == null )
							{
								command.res = -3;
								break;
							}
						}
						
						if( glvl == 0 || glvl > lvl )
							glvl = lvl;
						
						// TODO
						for(General g : generals.values())
						{
							if( g.id == gid || gid == 0 )
							{
								g.lvl = glvl;
								g.exp = 0;
							}
						}
						
						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				case "set_skill":
					{
						short gid = (short)command.arg1;
						short slvl = (short)command.arg2;
						if( slvl < 0 || gid < 0 )
						{
							command.res = -2;
							break;
						}
					
						SBean.GeneralCFGS gcfg = null;
					
						SBean.GeneralCmnCFGS cmncfg = GameData.getInstance().getGeneralCmnCFG();
						if( gid != 0 )
						{
							gcfg = GameData.getInstance().getGeneralCFG(gid);
							if( gcfg == null  || cmncfg == null )
							{
								command.res = -3;
								break;
							}
						}
					
						if( slvl == 0 || slvl > lvl )
							slvl = lvl;
					
						
						// TODO
						for(General g : generals.values())
						{
							if( g.id == gid || gid == 0 )
							{
								short slvl2 = slvl;
								if( slvl2 > g.lvl )
									slvl2 = g.lvl;
								// TODO set skill level slvl2
								for(int pos = 1; pos <= GameData.GENERAL_SKILL_COUNT; ++pos)
								{
									SBean.GeneralSkillCFGS scfg = cmncfg.skills.get(pos-1);
									if( g.advLvl < scfg.reqAdvLvl )
										continue;
									short lvlMax = (short)(g.lvl - scfg.maxLvl);
									if( lvlMax < 1 )
										lvlMax = 1;
									if( slvl2 > lvlMax )
										slvl2 = lvlMax;
									g.skills.set(pos-1, slvl2);
								}
							}
						}
					
						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				case "set_star":
					{
						short gid = (short)command.arg1;
						byte gstar = (byte)command.arg2;
						if( gstar < 1 || gstar > GameData.MAX_GENERAL_EVO_LEVEL || gid < 0 )
						{
							command.res = -2;
							break;
						}

						SBean.GeneralCFGS gcfg = null;

						if( gid != 0 )
						{
							gcfg = GameData.getInstance().getGeneralCFG(gid);
							if( gcfg == null )
							{
								command.res = -3;
								break;
							}
						}

						// TODO
						for(General g : generals.values())
						{
							if( g.id == gid || gid == 0 )
							{
								g.evoLvl = gstar;
							}
						}

						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				case "set_seyen":
					{
						short gid = (short)command.arg1;
						byte gseyen = (byte)command.arg2;
						if( gseyen < 0 || gid < 0 )
						{
							command.res = -2;
							break;
						}
	
						SBean.GeneralCFGS gcfg = null;
	
						if( gid != 0 )
						{
							gcfg = GameData.getInstance().getGeneralCFG(gid);
							if( gcfg == null )
							{
								command.res = -3;
								break;
							}
						}
	
						// TODO
						int tempSeyen =0;
						for(General g : generals.values())
						{
							if( g.id == gid || gid == 0 )
							{
								if(g.generalSeyen==null||g.generalSeyen.size()==0){
									g.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
									g.generalSeyen.add(new SBean.DBGeneralSeyen());
								}
								
								SBean.DBGeneralSeyen seyen1 = g.generalSeyen.get(0);
								if(gid==0){
									tempSeyen+=gseyen-seyen1.lvl;
								}else{
									tempSeyen=gseyen-seyen1.lvl;
								}
								seyen1.lvl=gseyen;
							}
						}
						if(tempSeyen!=0){
							for(General general : generals.values()){
								if(general!=null){
									if(general.generalSeyen==null||general.generalSeyen.size()==0){
										general.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
										general.generalSeyen.add(new SBean.DBGeneralSeyen());
									}
									general.generalSeyen.get(0).seyenTotal+=tempSeyen;
									
								}

							}
						}
							
						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				case "set_color":
					{
						short gid = (short)command.arg1;
						byte gstar = (byte)command.arg2;
						if( gstar < 1 || gstar > GameData.MAX_GENERAL_ADV_LEVEL || gid < 0 )
						{
							command.res = -2;
							break;
						}

						SBean.GeneralCFGS gcfg = null;

						if( gid != 0 )
						{
							gcfg = GameData.getInstance().getGeneralCFG(gid);
							if( gcfg == null )
							{
								command.res = -3;
								break;
							}
						}

						// TODO
						for(General g : generals.values())
						{
							if( g.id == gid || gid == 0 )
							{
								g.advLvl = gstar;
							}
						}

						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				case "add":
					{
						short gid1 = (short)command.arg1;
						short gid2 = (short)command.arg2;
						
						if( gid2 == 0 )
						{
							SBean.GeneralCFGS gcfg = null;

							if( gid1 != 0 )
							{
								gcfg = GameData.getInstance().getGeneralCFG(gid1);
								if( gcfg == null )
								{
									command.res = -3;
									break;
								}
							}
							if( ! generals.containsKey(gid1) )
							{
								addGeneral(gid1, (byte)3);
							}
						}
						else
						{
							if( gid1 < 1 || gid1 > 100 || gid2 < 1 || gid2 > 100 || gid2 < gid1 )
							{
								command.res = -3;
								break;
							}
							for(short gid = gid1; gid <= gid2; ++gid)
							{
								if( GameData.getInstance().getGeneralCFG(gid) == null )
									continue;
								
								if( ! generals.containsKey(gid) )
								{
									addGeneral(gid, (byte)3);
								}	
							}
						}

						command.res = 0;
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGeneralsSync(copyGeneralsWithoutLock()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "role_level":	// only set
			{
				short rlvl = (short)command.arg1;
				
				if( rlvl < 1 || rlvl > gs.getRoleLevelLimit() )
				{
					command.res = -2;
					break;
				}
				this.setLvl(rlvl);
				command.res = 0;
			}
			break;
		case "arena_point":
			{
				int point = command.arg1;
				if( point < 0 || point > 99999999 )
				{
					command.res = -2;
					break;
				}
				if( arenaState == null )
				{
					command.res = -4;
					break;
				}
				arenaState.point = point;
				command.res = 0;
			}
			break;
		case "arena_rank":
			{
				int rank = command.arg1;
				gs.getArenaManager().gmSetRank(id, rank);
				arenaCurrentEnemies = null;
				command.res = 0;
			}
			break;
		case "superarena_point":
			{
				int point = command.arg1;
				if( point < 0 || point > 99999999 )
				{
					command.res = -2;
					break;
				}
				if( superArena == null )
				{
					command.res = -4;
					break;
				}
				superArena.point = point;
				command.res = 0;
			}
			break;
		case "march_point":
			{
				int point = command.arg1;
				if( point < 0 || point > 99999999 )
				{
					command.res = -2;
					break;
				}
				if( marchState == null )
				{
					command.res = -4;
					break;
				}
				marchState.point = point;
				command.res = 0;
			}
			break;
		case "country_point":
			{
				int point = command.arg1;
				if( point < 0 || point > 99999999 )
				{
					command.res = -2;
					break;
				}
				if( forceInfo == null )
				{
					command.res = -4;
					break;
				}
				forceInfo.point = point;
				command.res = 0;
			}
			break;
		case "country_contrib":
			{
				int point = command.arg1;
				if( point < 0 || point > 9999999 )
				{
					command.res = -2;
					break;
				}
				if( forceInfo == null )
				{
					command.res = -4;
					break;
				}
				activity += point;
				if (forceInfo.id > 0)
					gs.getForceManager().addActivity(forceInfo.id, point);
				command.res = 0;
			}
			break;
		case "rich_point":
			{
				int point = command.arg1;
				if( point < 0 || point > 99999999 )
				{
					command.res = -2;
					break;
				}
				if( rich == null )
				{
					command.res = -4;
					break;
				}
				rich.point = point;
				command.res = 0;
			}
			break;
		case "time":
			{
				int setType = command.arg1;
				int t = command.arg2;
				GameData.getInstance().setTime(setType, t);
				command.res = 0;
				gs.getLoginManager().luaAnnounce(LuaPacket.encodeRoleTimeSync(gs.getTime()));
			}
			break;
		case "forcewar_memcnt":
			{
				int memcnt = command.arg1;
				if( memcnt < 1 || memcnt > 60 )
				{
					command.res = -2;
					break;
				}
				GameData.getInstance().setForceWarMemCntReq(memcnt);
				command.res = 0;
			}
			break;
		case "duel":
			{
				switch( command.command )
				{
				case "set_defaultlevel":
					DuelManager.defaultLevel = (short)command.arg1;
					SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
					int lvl = 10 - command.arg1;
					if (lvl >= 0 && lvl < cfg.upgrade.size())
						DuelManager.defaultScore = cfg.upgrade.get(lvl);
					command.res = 0;
					break;
				case "set_defaultscore":
					DuelManager.defaultScore = (short)command.arg1;
					command.res = 0;
					break;
				}
			}
			break;
		default:
			break;
		}
		gs.getLogger().warn("GMCommand (" + id + ") cmd=[" + cmd + "] res=" + command.res);
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGMCommandRes(command));
		return true;
	}
	
	public TLogger.CommonItemChange addArena(int arenaAdd)
	{
		if (arenaAdd <= 0)
			return null;
		arenaState.point += arenaAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_ARENA_POINT, 0, arenaAdd, arenaState.point);
	}
	
	public TLogger.CommonItemChange useArena(int arenaUse)
	{
		if (arenaUse <= 0)
			return null;
		arenaState.point -= arenaUse;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_ARENA_POINT, 0, -arenaUse, arenaState.point);
	}
	
	public TLogger.CommonItemChange addSuperArena(int arenaAdd)
	{
		if (arenaAdd <= 0 || superArena == null)
			return null;
		int beforePoint = superArena.point;
		if (superArena.point + arenaAdd >= GameData.MAX_POINT)
			superArena.point = GameData.MAX_POINT;
		else
			superArena.point += arenaAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_SUPER_ARENA_POINT, 0, superArena.point-beforePoint, superArena.point);
	}
	
	public TLogger.CommonItemChange useSuperArena(int arenaUse)
	{
		if (arenaUse <= 0 || superArena == null)
			return null;
		superArena.point -= arenaUse;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_SUPER_ARENA_POINT, 0, -arenaUse, superArena.point);
	}
	
	public TLogger.CommonItemChange addMarch(int marchAdd)
	{
		if (marchAdd <= 0)
			return null;
		marchState.point += marchAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MARCH_POINT, 0, marchAdd, marchState.point);
	}
	
	public TLogger.CommonItemChange useMarch(int marchUse)
	{
		if (marchUse <= 0)
			return null;
		marchState.point -= marchUse;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MARCH_POINT, 0, -marchUse, marchState.point);
	}
	
	public TLogger.CommonItemChange addForce(int forceAdd)
	{
		if (forceAdd <= 0)
			return null;
		forceInfo.point += forceAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_FORCE_POINT, 0, forceAdd, forceInfo.point);
	}
	
	public TLogger.CommonItemChange useForce(int forceUse)
	{
		if (forceUse <= 0)
			return null;
		forceInfo.point -= forceUse;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_FORCE_POINT, 0, -forceUse, forceInfo.point);
	}
	
	public void setMonthlyCard()
	{
		if( monthlyCardStartTime + gs.getGameData().getMonthlyCardCFG().days * 86400 < gs.getTime() )
			monthlyCardStartTime = gs.getTimeH0();
		else
			monthlyCardStartTime += gs.getGameData().getMonthlyCardCFG().days * 86400;
	}
	
	public int getMonthlyCardStartTime()
	{
		return monthlyCardStartTime;
	}
	
	public synchronized byte testPayLvlDoubleWithLock(byte payLvl)
	{
		return testPayLvlDoubleWithoutLock(payLvl);
	}
	
	public byte testPayLvlDoubleWithoutLock(byte payLvl)
	{
		if( ! GameData.getInstance().isDoublePayLevel(payLvl) )
			return 0;
		if( testPayLvl(payLvl) )
			return 0;
		return payLvl;
	}
	
	public boolean testPayLvl(byte payLvl)
	{
		
		for(byte l : payLvlState)
		{
			if( l == payLvl )
			{
				return true;
			}
		}
		return false;
	}
	
	public List<TLogger.CommonItemChange> addPay(byte payLvl, int stonePay, int stonePresent)
	{
		List<TLogger.CommonItemChange> tlogRecords = new ArrayList<TLogger.CommonItemChange>();
		if( stonePay > 0 )
		{
			if( GameData.getInstance().isDoublePayLevel(payLvl) )
			{
				if( ! testPayLvl(payLvl) )
				{
					payLvlState.add(payLvl);
				}
			}
		}		
		TLogger.CommonItemChange record = null;
		record = addStonePay(stonePay);
		if (record != null)
			tlogRecords.add(record);
		record = addStonePresent(stonePresent);
		if (record != null)
			tlogRecords.add(record);
//		tlogRecords.add(addStonePay(stonePay));
//		tlogRecords.add(addStonePresent(stonePresent));
		return tlogRecords;
	}
	
	private List<TLogger.CommonItemChange> addExp(int expAdd, TLogger.TLogEvent logEvent)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		//if( lvl >= gs.getRoleLevelLimit() )
		int levellimit = gs.getRoleLevelLimit();
		if ( lvl >= levellimit&&lvl >= 100 ){
			int seyenAdd = (int) (expAdd*GameData.getInstance().generalSeyen.commonValue.expRate);
			records.add(addSeyenExp(seyenAdd));
			//records.add(new TLogger.CommonItemChange(GameData.COMMON_TYPE_EXP, lvl, expAdd, exp));
			return records;
		}
		
		if ( lvl >= levellimit ){
			TLogger.CommonItemChange change = addMarch(expAdd/2);
			if (change != null)
				records.add(change);
			return records;
		}
				
		exp += expAdd;
		while( exp >= getUpgradeExp() )
		{
			exp -= getUpgradeExp();
			levelUp(logEvent);
			//if( lvl >= gs.getRoleLevelLimit() )
			if ( lvl >= levellimit )
			{
				if ( lvl >= 100 ){
					int seyenAdd = (int) (exp*GameData.getInstance().generalSeyen.commonValue.expRate);
					records.add(addSeyenExp(seyenAdd));
				}else{
					records.add(addMarch(exp/2));
				}
				
				exp = 0;
				break;
			}
		}
		
		if(lvl>=90){
			HeadIconCFGS cfg =gs.gameData.getHeadiconCFG((short)1);
			if(cfg!=null){
				if(getItemCount((short)cfg.type) < 1 ){
					boolean add = false;
					if(this.headicon!=null&&headicon.headicon!=null){
						
						if(!headicon.headicon.contains(cfg.sid)){
							SBean.DBHeadIconList list = new SBean.DBHeadIconList(cfg.sid,gs.getTime());
							add =true;//headicon.headicon.add(list);
						}
					}else{
						if(headicon==null){
							headicon = new DBHeadIcon((short)0,new  ArrayList<DBHeadIconList>(),0,(short)0,0,0);
						}
						if(!headicon.headicon.contains(cfg.sid)){
							SBean.DBHeadIconList list = new SBean.DBHeadIconList(cfg.sid,gs.getTime());
							add =true;//headicon.headicon.add(list);
						}
					}
					if(add&&headicon.mail==(short)0){
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						attLst.add(new SBean.DropEntryNew((byte) 0 ,(short)cfg.type, 1));
						gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_HEAD_ICON, "", ""+(short)cfg.type, 0, true, attLst);
						headicon.mail=(short)1;
					}
					
				}
			}		
		}
		records.add(new TLogger.CommonItemChange(GameData.COMMON_TYPE_EXP, lvl, expAdd, exp));
		return records;
	}

	private TLogger.CommonItemChange addSeyenExp(int seyenAdd) {
		if(generalStoneInfo==null){
			generalStoneInfo = new SBean.DBGeneralStoneInfo();
			generalStoneInfo.generalSeyen = new ArrayList<SBean.DBRoleSeyen>();
		}
		if(generalStoneInfo.generalSeyen==null){
			generalStoneInfo.generalSeyen = new ArrayList<SBean.DBRoleSeyen>();
		}
		if(generalStoneInfo.generalSeyen.size()==0){
			DBRoleSeyen seyenTemp = new DBRoleSeyen();
			generalStoneInfo.generalSeyen.add(seyenTemp);
		}
		DBRoleSeyen m =generalStoneInfo.generalSeyen.get(0);
		m.seyen+=seyenAdd;
		
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_SEYEN, 0, seyenAdd, m.seyen);
	}
	
	private void levelUp(TLogger.TLogEvent logEvent)
	{
		//List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		++lvl;
		gs.getLoginManager().updateLevelRoleCache(this.id, lvl-1, lvl);
		TLogger.LevelUpRecord lur = new TLogger.LevelUpRecord(lvl);
		lur.setLastLevelUpTime(upgradeTime);
		upgradeTime = gs.getTime();
		lur.setCurLevelUpTime(upgradeTime);
		if (logEvent != null)
			logEvent.addRecord(lur);
		TLogger.CommonItemChange vitRecord = addVit2(GameData.getInstance().getRoleCmnCFG().vitAddLvlup.get(lvl-2));
		if (vitRecord != null)
		{
			TLogger.CommonFlowRecord record = new TLogger.CommonFlowRecord();
			record.addChange(vitRecord);
			record.setEvent(TLog.AT_LEVEL_UP);
			if (logEvent != null)
				logEvent.addCommonFlow(record);
		}
		//return records;
	}
	
	public synchronized void subVitWithLock(int vitSub)
	{
		subVit(vitSub);
	}
	
	private TLogger.CommonItemChange addSkillPoint(int add)
	{
		if( add <= 0 )
			return null;
		short max = getSkillPointMax();
		if( skillPoint >= max )
			return null;
		skillPoint = (short)(skillPoint + add);
		if( skillPoint >= max  || skillPoint <= 0 )
		{
			skillPoint = max;
			skillPointRecoverStart = 0;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_SKILLPOINT, 0, add, skillPoint);
	}
	
	private void useSkillPoint()
	{
		skillPoint--;
		if( skillPoint < 0 )
			skillPoint = 0;
		if( skillPointRecoverStart == 0 )
			skillPointRecoverStart = gs.getTime();
	}
	
	public boolean testFlag(int mask)
	{
		return (flag & mask) != 0;
	}
	
	private void setFlag(int mask)
	{
		flag |= mask;
	}
	
	public synchronized void setFlagWithLock(int mask)
	{
		setFlag(mask);
	}
	
	public void clearFlag(int mask)
	{
		flag &= ~mask;
	}
	
	public SBean.DBRoleBrief getDBRoleBriefWithoutLock()
	{
		return new SBean.DBRoleBrief(id, lvl, headIconID, name, forceInfo.name, 0);
	}
	
	public synchronized SBean.DBRoleBrief getDBRoleBriefWithLock()
	{
		return getDBRoleBriefWithoutLock();
	}
	
	public synchronized SBean.DBCombatTypeScore copyCombatScoreWithLock(byte type, byte catIndex)
	{
		if( type >= 0 && type < combatScores.size() )
		{
			List<SBean.DBCombatTypeScore> lst = combatScores.get(type).scores;
			if( catIndex >= 0 && catIndex < lst.size() )
			{
				return lst.get(catIndex);
			}
		}
		return null;
	}
	
	public boolean onTimer(long timeNow, int timeTick)
	{
		if( gs.getHotChecker().checkRoleBeforeTimer(gs, this) != 0 )
		{
			gs.getLogger().warn("checkRoleBeforeTimer(role) failed");
		}
		boolean bSave = false;
		List<MedalNotice> lst = null;
		List<Byte> lstVIP = null;
		List<Integer> lstStoneMonitor = null;
		List<Byte> lstEvents = null;
		boolean bNewWorldMail = false;
		boolean bNotifyBroadcast = false;
		boolean bCheckDTask = false;
		boolean bCheckRichTask = false;
		boolean bCheckLevelLimit = false;
		synchronized( this )
		{
			if( isNull () )
			{
				// TODO
			}
			else if( netsid == 0 ) // standby
			{
				if( removeTime < timeNow )
					return true;
			}
			else // online
			{
				tryRefresh(timeTick);
				if( lastSaveTime == 0 )
				{
					lastSaveTime = timeNow + gs.getGameData().getRandom().nextInt(SAVE_ROLE_INTERVAL_RAND);
				}
				else if( lastSaveTime < 0 || lastSaveTime + SAVE_ROLE_INTERVAL_MIN < timeNow )
					bSave = true;
				if( ! medalNoticeList.isEmpty() )
				{
					lst = medalNoticeList;
					medalNoticeList = new ArrayList<MedalNotice>();
				}
				if( ! vipNoticeList.isEmpty() )
				{
					lstVIP = vipNoticeList;
					vipNoticeList = new ArrayList<Byte>();
				}
				if( ! eventNoticeList.isEmpty() )
				{
					lstEvents = eventNoticeList;
					eventNoticeList = new ArrayList<Byte>();
				}
				if( ! stoneMonitorList.isEmpty() )
				{
					lstStoneMonitor = stoneMonitorList;
					stoneMonitorList = new ArrayList<Integer>();
				}				
				if( timeTick % GameData.ROLE_RAND_INTERVAL_MAX == randTick )
				{
					bNewWorldMail = testWorldMail();
				}
				if (timeTick % GameData.ROLE_RAND_INTERVAL_MAX == randTick)
				{
					bNotifyBroadcast = testBroadcasts();
				}
				if (timeTick % GameData.ROLE_RAND_INTERVAL_MAX == randTick)
				{
					bCheckDTask = true;
					bCheckRichTask = true;
					bCheckLevelLimit = true;
				}
			}
		}
		if( bNewWorldMail )
			gs.getRPCManager().notifyMailNew(netsid);
		if (bNotifyBroadcast)
			gs.getRPCManager().notifyBroadcasts(netsid, broadcasts);
		if( bSave )
			doSave();
		if( lst != null )
		{
			for(MedalNotice e : lst)
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeMedalNotice(e.id, e.seq));
			}
		}
		if( lstVIP != null )
		{
			for(byte e : lstVIP)
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeVIPUpdate(e, getStonePayTotal(), true));
			}
		}
		if( lstStoneMonitor != null )
		{
			Midas.UserInfo uinfo = new Midas.UserInfo(msdkInfo.openID, msdkInfo.openKey, msdkInfo.payToken, msdkInfo.pf, msdkInfo.pfKey,
					msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSIDGuest:gs.getConfig().offerCookieSID,
							msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSTypeGuest:gs.getConfig().offerCookieSType);
			for(int d : lstStoneMonitor)
			{
				if( d > 0 )
					gs.getMidas().present(uinfo, d);
				else if( d < 0 )
					gs.getMidas().pay(uinfo, -d);
			}
		}
		if( bCheckDTask || lstEvents != null && ! lstEvents.isEmpty() )
		{
			testDTask2Notice();
		}
		if (bCheckRichTask || needTestRichDailyTaskNotice())
		{
			testRichDailyTaskNotice();
		}
		if (bCheckLevelLimit)
		{
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeGlobalState(gs.getLoginManager().getGlobalState()));
		}
		int[] newStamp = {0};
		if (forceInfo.id > 0 && gs.getWarManager().thiefStampNeedRefresh(forceInfo.id, forceThiefStamp, newStamp)) {
			int[] startTime = {0};
			List<ForceWarManager.ForceThiefCombat> combats = gs.getWarManager().getCurrentThief(forceInfo.id, startTime);
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeForceThiefCombats(startTime[0], combats));				
			forceThiefStamp = newStamp[0];
		}
		return false;
	}
	
	public boolean isIOSGuest()
	{
		return msdkInfo != null && msdkInfo.isIOSGuest();
	}
	
	public int changeSID(int sid)
	{
		int oldsid;
		synchronized( this )
		{
			oldsid = netsid;
			netsid = sid;
			if( oldsid == 0 )
				tryRefresh(gs.getTime());
		}
		return oldsid;
	}
	
	public void setStandby()
	{
		synchronized( this )
		{
			if( isNull() )
				return;
			if( netsid == 0 )
				return;
			netsid = 0;
			removeTime = System.currentTimeMillis() + MAX_STANDBY_TIME;
		}
		gs.getLogger().debug("role " + id + " leave");
	}
	
	public int getFriendCountWithoutLock()
	{
		return friend.getFriendCount();
	}
	
	public void tryRefresh(int timeTick)
	{
		if( vitRecoverStart > 0 )
		{
			final int d = timeTick - vitRecoverStart;
			final int interval = GameData.getInstance().getRoleCmnCFG().vitRecoverInterval;
			if( d >= interval )
			{
				final int n = d / interval;
				final int r = d % interval;
				vitRecoverStart = timeTick - r;
				addVit1(n);
			}
		}
		if( skillPointRecoverStart > 0 )
		{
			final int d = timeTick - skillPointRecoverStart;
			final int interval = GameData.getInstance().getRoleCmnCFG().skillPointRecoverInterval;
			if( d >= interval )
			{
				final int n = d / interval;
				final int r = d % interval;
				skillPointRecoverStart = timeTick - r;
				addSkillPoint(n);
			}
		}
		/*
		if( richMovementRecoverStart > 0 )
		{
			final int d = timeTick - richMovementRecoverStart;
			final int interval = 300; // TODO: cfg
			if( d >= interval )
			{
				final int n = d / interval;
				final int r = d % interval;
				richMovementRecoverStart = timeTick - r;
				richAddMovement(n);
			}
		}
		*/
	}
	
	public void updateLoginTime(int now)
	{
		lastLoginTime = now;
	}
	
	public int updateLogoutTime(int now)
	{
		int onlineTime = now - lastLoginTime;
		lastLogoutTime = now;
		return onlineTime;
	}
	
	//gm锟斤拷锟斤拷count锟斤拷uint锟斤拷锟斤拷锟�
	public TLogger.CommonItemChange gmDelItem(int itemID, int count)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			Integer c = items.get((short)itemID);
			if( c != null )
			{
				short nowCount = c.shortValue();
				count = count < 0 ? nowCount : (count > nowCount ? nowCount : count);
				record = delItem((short)itemID, count);
			}
		}
		return record;
	}
	
	//gm锟斤拷锟斤拷count锟斤拷uint锟斤拷锟斤拷锟�
	public TLogger.CommonItemChange gmAddItem(int itemID, int count)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			short realCount = count < 0 ? Short.MAX_VALUE : (count > Short.MAX_VALUE ? Short.MAX_VALUE : (short)count);
			record = addItem((short)itemID, realCount);
		}
		return record;
	}
	
	//gm锟斤拷锟斤拷count锟斤拷uint锟斤拷锟斤拷锟�
	public TLogger.CommonItemChange gmDelEquip(int equipID, int count)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			Short c = equips.get((short)equipID);
			if( c != null )
			{
				short nowCount = c.shortValue();
				count = count < 0 ? nowCount : (count > nowCount ? nowCount : count);
				record = delEquip((short)equipID, count);
			}
		}
		return record;
	}
	
	//gm锟斤拷锟斤拷count锟斤拷uint锟斤拷锟斤拷锟�
	public TLogger.CommonItemChange gmAddEquip(int equipID, int count)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			short realCount = count < 0 ? Short.MAX_VALUE : (count > Short.MAX_VALUE ? Short.MAX_VALUE : (short)count);
			record = addEquip((short)equipID, realCount);
		}
		return record;
	}
	
	public List<TLogger.CommonItemChange> gmModifyExp(int changeExp, TLogger.TLogEvent tlogEvent)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		synchronized( this )
		{
			if (changeExp > 0)
			{
				records.addAll(addExp(changeExp, tlogEvent));
			}
			else
			{
				if (exp < -changeExp)
					changeExp = -exp;
				exp = exp - changeExp;
				records.add(new TLogger.CommonItemChange(GameData.COMMON_TYPE_EXP, lvl, changeExp, exp));
			}
		}
		return records;
	}
	
	public TLogger.CommonItemChange gmModifyVit(int changeVit)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			if (changeVit > 0)
			{
				record = addVit2(changeVit);
			}
			else
			{
				record = subVit(-changeVit);
			}
		}
		return record;
	}
	
	public TLogger.CommonItemChange gmClearVit()
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			record = subVit(this.vit);
		}
		return record;
	}
	
	public TLogger.CommonItemChange gmModifyStone(int changeStone)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			int beforeStone = stone;
			long endStone = stone + changeStone;
			if (endStone > Integer.MAX_VALUE)
				stone = Integer.MAX_VALUE;
			else if (endStone < 0)
				stone = 0;
			else
				stone = (int)endStone;
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 2, stone-beforeStone, stone);
		}
		return record;
	}
	
	public TLogger.CommonItemChange gmClearStone()
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			int beforeStone = stone;
			stone = 0;
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 2, stone-beforeStone, stone);
		}
		return record;
	}
	
	public TLogger.CommonItemChange gmModifyMoney(int changeMoney)
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			int beforeMoney = money;
			long endMoney = money + changeMoney;
			if (endMoney > Integer.MAX_VALUE)
				money = Integer.MAX_VALUE;
			else if (endMoney < 0)
				money = 0;
			else
				money = (int)endMoney;
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_MONEY, 2, money-beforeMoney, money);
		}
		return record;
	}
	
	public TLogger.CommonItemChange gmClearMoney()
	{
		TLogger.CommonItemChange record = null;
		synchronized( this )
		{
			int beforeMoney = money;
			money = 0;
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 2, stone-beforeMoney, stone);
		}
		return record;
	}
	
	public int gmSetGeneralLevel(int gid, int level)
	{
		synchronized( this )
		{
			General g = generals.get((short)gid);
			if( g == null )
				return -1;
			if (g.lvl > level)
				return -2;
			else if (g.lvl == level)
				return 0;
			if (level > this.lvl)
				level = this.lvl;
			g.lvl = (short)level;
			g.exp = 0;
		}
		return 0;
	}
	
	public int gmSetPetLevel(int pid, int level)
	{
		synchronized( this )
		{
			Pet p = pets.get((short)pid);
			if( p == null )
				return -1;
			if (p.lvl > level)
				return -2;
			else if (p.lvl == level)
				return 0;
			if (level > this.lvl)
				level = this.lvl;
			p.lvl = (short)level;
			p.exp = 0;
		}
		return 0;
	}
	
	public int getBanLoginEndTime()
	{
		return banData.getBanLoginEndTime();
	}
	
	public boolean isBanLogin()
	{
		return banData.isBanLogin(gs.getTime());
	}
	
	public int getBanLoginLeftTime()
	{
		return banData.getBanLoginLeftTime(gs.getTime());
	}
	
	public String getBanLoginReason()
	{
		return banData.getBanLoginReason();
	}
	
	
	
	public boolean isBanBattle()
	{
		return banData.isBanBattle(gs.getTime());
	}
	
	
	public boolean isBanMarch()
	{
		return banData.isBanMarch(gs.getTime());
	}
	
	
	
	public boolean isBanChibi()
	{
		return banData.isBanChibi(gs.getTime());
	}

	
	public boolean isBanArena()
	{
		return banData.isBanArena(gs.getTime());
	}
	
	
	public boolean isBanChat()
	{
		return banData.isBanChat(gs.getTime());
	}
	
	public int getBanChatLeftTime()
	{
		return banData.getBanChatLeftTime(gs.getTime());
	}
	
	public String getBanChatReason()
	{
		return banData.getBanChatReason();
	}
	
	public boolean isBanPLay()
	{
		return banData.isBanPlay(gs.getTime());
	}

	
	public List<LoginManager.BanInfo> getBanPlayInfo()
	{
		return banData.getBanPlayInfo(gs.getTime());
	}
	
	public void forceActivityReward(int fid, int reward, int forceActivity)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_CONTRIBUTION_REWARD);
		boolean canRewards = false;
		synchronized (this)
		{
			for (Byte b : activityRewards) {
				if (b == (byte)reward) {
					gs.getRPCManager().notifyForceActivityReward(netsid, id, false, 0, null);											
					return;
				}
			}
			
			List<ForceActivityRewardCFGS> rewards = gs.getGameData().getForceCFG().activityRewards;
			if (rewards.size() >= reward && reward > 0) 
			{
				ForceActivityRewardCFGS rcfg = rewards.get(reward-1);
				if (forceActivity >= rcfg.forceActivity && activity >= rcfg.activity) 
				{
					int money = rcfg.money1*lvl + rcfg.money2;
					short drop = -1;
					for (SBean.ForceActivityDropCFGS d : rcfg.drops) {
						if (lvl < d.maxLvl) {
							drop = d.drop;
							break;
						}
					}
					if (drop == -1 && rcfg.drops.size() > 0)
						drop = rcfg.drops.get(rcfg.drops.size() - 1).drop;
					SBean.DropEntry dropEntry = gs.getGameData().getDropTableRandomEntry(drop);
					if (dropEntry != null)
						commonFlowRecord.addChange(addDrop(dropEntry));
					commonFlowRecord.addChange(addMoney(money));
					activityRewards.add((byte)reward);
					gs.getRPCManager().notifyForceActivityReward(netsid, id, true, money, dropEntry);
					canRewards = true;
				}
				else 
					gs.getRPCManager().notifyForceActivityReward(netsid, id, false, 0, null);											
			}
		}
		if (canRewards)
		{
			commonFlowRecord.setEvent(TLog.AT_FORCE_CONTRIBUTION_REWARD);
			commonFlowRecord.setArg(fid);
			tlogEvent.addCommonFlow(commonFlowRecord);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
	}
	
	public void forceWarCalc(List<DBRoleGeneral> generals1, List<DBPetBrief> pets1, List<DBRoleGeneral> generals2, List<DBPetBrief> pets2, 
			List<DBForceWarGeneralStatus> status1, List<DBForceWarGeneralStatus> status2, List<SBean.DBRelation> relation1, List<SBean.DBRelation> relation2, 
			int seed, int fid1, int fid2, byte rIndex1, byte rIndex2, byte[] eff1, byte[] eff2)
	{
		ForceWarManager.CalcReq req = new ForceWarManager.CalcReq();
		req.seed = seed;
		req.fid1 = fid1;
		req.fid2 = fid2;
		req.rIndex1 = rIndex1;
		req.rIndex2 = rIndex2;
		req.eff1 = eff1;
		req.eff2 = eff2;
		req.generals1 = generals1;
		req.generals2 = generals2;
		req.pets1 = pets1;
		req.pets2 = pets2;
		req.status1 = status1;
		req.status2 = status2;
		req.relation1 = relation1;
		req.relation2 = relation2;
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeForceWarCalcReq(req));
	}
	
	public void richInitValues(SBean.DBRichMap map, List<SBean.DBRichMapExt> mapExt)
	{
		if (rich == null)
			return;
		
		SBean.RichCFGS cfgs = gs.getGameData().getRichCFG();
		
		rich.mapType = 0;
		rich.pos = 0;
		rich.minipos = 0;
		//rich.gold = cfgs.basic.initgold;
		rich.goldTotal = 0;
		rich.movement = cfgs.basic.initmovement;
		rich.angelLeft = 0;
		rich.devilLeft = 0;
		rich.turtleLeft = 0;
		rich.boomLeft = 0;
		rich.building = 0;
		rich.bossTimes = 0;
		rich.bossDamage = 0;
		rich.status = 0;
		rich.lotery = new ArrayList<Byte>();
		rich.buyMovementTimes = 0;
		rich.gender = 0;
		
		if (map == null) {
			rich.map = new SBean.DBRichMap();
			rich.map.buildings = new ArrayList<SBean.DBRichMapBuilding>();
			rich.map.objects = new ArrayList<SBean.DBRichMapObject>();			
		}
		else {
			rich.map = map;
		}
		rich.mapExt = new ArrayList<SBean.DBRichMapExt>();
		if (mapExt != null && mapExt.size() > 0) 
			rich.mapExt.add(mapExt.get(0).kdClone());
		
		for (SBean.DBRichMapBuilding b : rich.map.buildings) {
			if (b.type == RichManager.MAP_TYPE_MINE) {
				int id = b.params.get(0);
				if (id < 0)
					id = 0;
				int combatId = 0;
				int reward = 0;
				for (SBean.RichMineCombatCFGS c : cfgs.mineCombats)
					if (lvl >= c.lvlMax) {
						if (id >= c.combatIds.size())
							id = c.combatIds.size() - 1;
						combatId = c.combatIds.get(id);
						reward = c.rewards.get(id);
					}
				b.params.set(0, id+1);
				b.params.add(combatId);
				b.params.add(reward);
				b.params.add(cfgs.basic.mine.get(0).tax);
			}
			else if (b.type == RichManager.MAP_TYPE_RICH_MINE) {
				int id = b.params.get(0);
				if (id < 0)
					id = 0;
				int combatId = 0;
				int reward = 0;
				for (SBean.RichMineCombatCFGS c : cfgs.richMineCombats)
					if (lvl >= c.lvlMax) {
						if (id >= c.combatIds.size())
							id = c.combatIds.size() - 1;
						combatId = c.combatIds.get(id);
						reward = c.rewards.get(id);
					}
				b.params.set(0, id+1);
				b.params.add(combatId);
				b.params.add(reward);
				b.params.add(cfgs.basic.mine.get(1).tax);
			}
			else if (b.type == RichManager.MAP_TYPE_JUBAO) {
				int id = b.params.get(0);
				if (id < 0)
					id = 0;
				int combatId = 0;
				for (SBean.RichBossCombatCFGS c : cfgs.bossCombats)
					if (lvl >= c.lvlMax) {
						if (id >= c.combatIds.size())
							id = c.combatIds.size() - 1;
						combatId = c.combatIds.get(id);
					}
				b.params.set(0, id+1);
				b.params.add(combatId);
			}
			else if (b.type == RichManager.MAP_TYPE_DIVINE) {
				b.params.add(0);
				b.params.add(0);
				b.params.add(0);
			}
		}
		rich.dailyTask = new ArrayList<SBean.DBRichDailyTask>();
		for (SBean.RichDailyTaskCFG cfg : cfgs.dailyTask)
		{
			SBean.DBRichDailyTask task = new SBean.DBRichDailyTask(cfg.id, 0, (byte)0);
			rich.dailyTask.add(task);
		}
		rich.thiefDefenseDownEndRate = 0;
		rich.thiefDefenseDownRate = 0;
		rich.thiefHpDownEndRate = 0;
		rich.thiefHpDownRate = 0;
		
		if (lvlVIP >= 15 && rich.goldHistory > 0)
			gs.getRichManager().setGold(rich.goldHistory, this.id, name, headIconID, lvl, (int)getRichTechPlusEffect(GameData.RICH_TECH_TYPE_RICH_REWARD));
	}
	
	public void richSync()
	{
		SBean.DBRich r = gs.getRichManager().getRich();
		if (r == null) {
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichSync(null, null, (byte)0));		
			return;
		}
		
		SBean.RichCFGS cfgs = gs.getGameData().getRichCFG();
		
		SBean.DBRichRole rr = null;
		String objectStr = null;
		synchronized (this) {
			if (lvl < cfgs.basic.reqLevel) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichSync(null, null, (byte)0));		
				return;
			}
			
			if (rich == null)
				rich = RichManager.createDefaultRichRole();
			
			if (rich.day != r.day) {
				rich.day = r.day;
				richInitValues(r.map, r.mapExt);
			}
			
			// Restore error pos
			while (rich.pos >= RichManager.MAP_LENGTH)
				rich.pos -= RichManager.MAP_LENGTH;
			
			objectStr = richReachObject();
			rr = rich.kdClone();
		}
		if (rr != null)
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichSync(rr, objectStr, (byte)(rich.building+1)));		
	}
	
	private boolean richCanPass()
	{
		SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
		switch (building.type) {
		case RichManager.MAP_TYPE_BLACK_SHOP:
		case RichManager.MAP_TYPE_DIVINE:
		case RichManager.MAP_TYPE_LOTERY:
		case RichManager.MAP_TYPE_NONE:
		case RichManager.MAP_TYPE_JUBAO:
			return true;
		case RichManager.MAP_TYPE_MINE:
		case RichManager.MAP_TYPE_RICH_MINE:
			if (building.status >= 2)
				return true;
			break;
		}
		return false;
	}
	
	public void richSetGender(byte gender)
	{	
		synchronized (this) {
			if (rich == null)
				return;

			if (rich.gender != 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichSetGender(gender, -1));
				return;
			}
			
			rich.gender = gender;
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichSetGender(gender, 0));
	}
	
	public void richDice(int cheat)
	{	
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();

		int oricheat = cheat;
		SBean.DBRichRole rr = null;
		String objectStr = null;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			if (rich == null)
				return;
			
			if ((rich.mapType == 0 && !richCanPass()) || (rich.status & RichManager.STATUS_OBJECT_HANDLED) == 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDice(oricheat, null, null, (byte)0));
				return;
			}
			
			if (rich.movement < cfg.basic.movecost) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDice(oricheat, null, null, (byte)0));
				return;
			}
			
			richDelMovement(cfg.basic.movecost);
			
			List<Short> posses = null;
			if (rich.mapType == 0)
				posses = cfg.basic.diceposs;
			else if (rich.mapType == 1)
				posses = cfg.basic.minidiceposs;
			
			if (posses == null) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDice(oricheat, null, null, (byte)0));
				return;
			}
			
			boolean remoteDice = false;
			boolean moveMode = false;
			if (cheat == 0) {
				int total = 0;
				for (short poss : posses)
					total += poss;
				
				int r = gs.getGameData().getRandInt(0, total);
				int add = 0;
				int index = 0;
				for (short poss : posses) {
					add += poss;
					if (r < add)
						break;
					index ++;
				}
				
				cheat = index + 1;
				if ((rich.status & RichManager.STATUS_GO_WEST) != 0 && rich.mapType == 0) {
					cheat += 9;
					moveMode = true;
					rich.status &= ~RichManager.STATUS_GO_WEST;
				}
				if ((rich.status & RichManager.STATUS_FLY) != 0 && rich.mapType == 0) {
					int fly = gs.getGameData().getRandInt(1, RichManager.MAP_LENGTH);
					cheat = fly;
					moveMode = true;
					rich.status &= ~RichManager.STATUS_FLY;
				}
			}
			else {
				if (!useRemoteDice(cheat, cfg, commonFlowRecord)) {
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDice(oricheat, null, null, (byte)0));
					return;					
				}
				remoteDice = true;
			}
			//if (cheat > 6)
			//	cheat = 6;
			
			// round update
			float gainScale = 1;
			if (rich.angelLeft > 0) {
				gainScale += cfg.basic.angelScale;
				rich.angelLeft --;
			}
			if (rich.devilLeft > 0) {
				gainScale -= cfg.basic.devilScale;
				rich.devilLeft --;
			}
			if (rich.boomLeft > 0) {
				gainScale += rich.boomUpRate;
				rich.boomLeft --;
			}
			if (rich.turtleLeft > 0 && !moveMode && !remoteDice && rich.mapType == 0) {
				cheat = 1;
				rich.turtleLeft --;
			}
			int goldGain = 0;
			for (SBean.DBRichMapBuilding b : rich.map.buildings) {
				if (b.type == RichManager.MAP_TYPE_MINE || b.type == RichManager.MAP_TYPE_RICH_MINE) {
					if (b.status > 10) {
						if (b.type == RichManager.MAP_TYPE_MINE)
							goldGain += cfg.basic.mine.get(0).gain;
						else if (b.type == RichManager.MAP_TYPE_RICH_MINE)
							goldGain += cfg.basic.mine.get(1).gain;
					}
				}
			}
			gainScale += getRichTechPlusEffect(GameData.RICH_TECH_TYPE_MINE);
			if (goldGain > 0) {
				int gain = (int)(gainScale * goldGain);
				commonFlowRecord.addChange(richAddGold(gain));
			}
			
			if (rich.mapType == 0) {
				rich.pos += cheat;
				
				while (rich.pos >= RichManager.MAP_LENGTH)
					rich.pos -= RichManager.MAP_LENGTH;
			}
			else if (rich.mapType == 1) {
				rich.minipos += cheat;
				if (rich.minipos >= RichManager.MAP_MINI_LENGTH)
					rich.mapType = 0;
			}
			
			rich.status &= ~RichManager.STATUS_OBJECT_HANDLED;
			objectStr = richReachObject();
			richReachBuilding();
			
			rr = rich.kdClone();
		}
		
		if (rr != null)
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDice(oricheat, rr, objectStr, (byte)(rich.building+1)));
		commonFlowRecord.setEvent(TLog.AT_RICH_DICE);
		commonFlowRecord.setArg(cheat, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	/*
	public boolean richFly(short pos)
	{	
		synchronized (this) {
			if (rich == null)
				return false;
			if (pos < 0)
				pos = 0;
			else if (pos >= RichManager.MAP_LENGTH)
				pos = RichManager.MAP_LENGTH - 1;

			if (rich.mapType == 0) {
				rich.pos = pos;
			}
			richReachObject();
			richReachBuilding();
		}
		return true;
	}
	*/
	
	private String richReachObject()
	{
		StringBuilder sb = new StringBuilder();
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();

		boolean objectExist = false;
		if (rich.mapType == 0) {
			SBean.DBRichMapObject object = null;
			for (SBean.DBRichMapObject o : rich.map.objects) {
				if (o.pos == rich.pos) {
					object = o;
					break;
				}
			}
			
			if (object != null) {
				objectExist = true;
				sb.append(object.type).append(';');
				switch (object.type) {
				case RichManager.OBJECT_TYPE_MONEY_GOD:
					{
						float scale = 1.0f + getRichTechPlusEffect(GameData.RICH_TECH_TYPE_GOD);
						sb.append((int)(cfg.basic.moneygodgold * scale)).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BIG_MONEY_GOD:
					{
						float scale = 1.0f + getRichTechPlusEffect(GameData.RICH_TECH_TYPE_GOD);
						sb.append((int)(cfg.basic.bigmoneygodgold * scale)).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_QUESTION_MARK:
					break;
				case RichManager.OBJECT_TYPE_TRANSFER_DOOR:
					break;
				case RichManager.OBJECT_TYPE_ANGEL:
					{
						sb.append(cfg.basic.angelround).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_DEVIL:
					{
						sb.append(cfg.basic.devilround).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_TURTLE:
					{
						sb.append(cfg.basic.turtleround).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BEGGAR:
					{
						sb.append(cfg.basic.beggargold).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BMW:
					{
						sb.append(cfg.basic.bmwmove).append(';');
					}
					break;
				}
			}
			
			if (object == null && rich.mapExt.size() > 0) {
				SBean.DBRichMapObjectExt objectExt = null;
				SBean.DBRichMapExt ext = rich.mapExt.get(0);
				for (SBean.DBRichMapObjectExt o : ext.objects) {
					if (o.pos == rich.pos) {
						objectExt = o;
						break;
					}
				}
				
				if (objectExt != null) {
					objectExist = true;
					sb.append(objectExt.type).append(';');
					switch (objectExt.type) {
					case RichManager.OBJECT_TYPE_MONEY:
						{
							sb.append(objectExt.params.get(0)).append(';'); // type
							sb.append(objectExt.params.get(1)).append(';'); // count
							sb.append(objectExt.params.get(2)).append(';'); // dice
						}
						break;
					case RichManager.OBJECT_TYPE_BOX:
						{
							int open = objectExt.params.get(0);
							sb.append(open).append(';'); // open times
							sb.append(objectExt.params.size() - 1).append(';'); // total level
							int cost = 0;
							if (objectExt.params.size() > open + 1)
								cost = objectExt.params.get(open + 1);
							sb.append(cost).append(';'); // cost
						}
						break;
					}
				}
			}
		}
		else if (rich.mapType == 1) {
			SBean.DBRichMapObject object = null;
			for (SBean.DBRichMapObject o : rich.map.miniobjects) {
				if (o.pos == rich.minipos) {
					object = o;
					break;
				}
			}
			
			if (object != null) {
				objectExist = true;
				sb.append(object.type).append(';');
				int gold = cfg.basic.minigold.get(rich.minipos -1);
				sb.append(gold).append(';');
			}
		}
		
		if (!objectExist) {
			sb.append(RichManager.OBJECT_TYPE_NONE).append(';');
			rich.status |= RichManager.STATUS_OBJECT_HANDLED;
		}
		
		return sb.toString();
	}
	
	public void richHandleObj(boolean action)
	{
		SBean.DBRichRole rr = null;
		String objectStr = null;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.setEvent(TLog.AT_RICH_ROAD);
		commonFlowRecord.setArg(0, 0);
		synchronized (this) {
			if (rich == null)
				return;
			
			if ((rich.status & RichManager.STATUS_OBJECT_HANDLED) != 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichHandleObj(null, null));
				return;
			}

			objectStr = richHandleObject(action, commonFlowRecord);

			rr = rich.kdClone();
		}

		if (rr != null)
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichHandleObj(rr, objectStr));
		
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	private String richHandleObject(boolean action, TLogger.CommonFlowRecord commonFlowRecord)
	{
		StringBuilder sb = new StringBuilder();
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		boolean handled = true;

		boolean objectExist = false;
		if (rich.mapType == 0) {
			SBean.DBRichMapObject object = null;
			int index = 0;
			for (SBean.DBRichMapObject o : rich.map.objects) {
				if (o.pos == rich.pos) {
					object = o;
					break;
				}
				index ++;
			}
			
			if (object != null) {
				objectExist = true;
				sb.append(object.type).append(';');
				switch (object.type) {
				case RichManager.OBJECT_TYPE_MONEY_GOD:
					{
						float scale = 1.0f + getRichTechPlusEffect(GameData.RICH_TECH_TYPE_GOD);
						int gold = (int)(cfg.basic.moneygodgold * scale);
						commonFlowRecord.addChange(richAddGold(gold));
						rich.map.objects.remove(index);
						sb.append(gold).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BIG_MONEY_GOD:
					{
						float scale = 1.0f + getRichTechPlusEffect(GameData.RICH_TECH_TYPE_GOD);
						int gold = (int)(cfg.basic.bigmoneygodgold * scale);
						commonFlowRecord.addChange(richAddGold(gold));
						rich.map.objects.remove(index);
						sb.append(gold).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_QUESTION_MARK:
					{
						SBean.DropEntryNew drop = createRandomRoadItem();
						commonFlowRecord.addChange(addDropNew(drop));
						rich.map.objects.remove(index);
						sb.append(drop.type).append(';').append(drop.id).append(';').append(drop.arg).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_TRANSFER_DOOR:
					{
						rich.mapType = 1;
						rich.minipos = 0;
						rich.map.objects.remove(index);
					}
					break;
				case RichManager.OBJECT_TYPE_ANGEL:
					{
						rich.angelLeft = cfg.basic.angelround;
						rich.map.objects.remove(index);
						sb.append(cfg.basic.angelround).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_DEVIL:
					{
						byte round = cfg.basic.devilround;
						if (action) {
							if (useOneObjectCard(object.type, cfg, new int[]{0}, commonFlowRecord))
								round = 0;
						}
						rich.devilLeft = round;
						rich.map.objects.remove(index);
						sb.append(round).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_TURTLE:
					{
						byte round = cfg.basic.turtleround;
						if (action) {
							if (useOneObjectCard(object.type, cfg, new int[]{0}, commonFlowRecord))
								round = 0;
						}
						rich.turtleLeft = round;
						rich.map.objects.remove(index);
						sb.append(round).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BEGGAR:
					{
						int gold = cfg.basic.beggargold;
						if (action) {
							if (useOneObjectCard(object.type, cfg, new int[]{0}, commonFlowRecord))
								gold = 0;
						}
						if (gold > 0) {
							//TLogger.CommonItemChange change = 
							commonFlowRecord.addChange(richDelGold(gold));
							/*
							if (change != null)
							{
								rich.beggarCost = -change.changeCount;
							}
							else
							{
								rich.beggarCost = 0;
							}							
							*/
						}
						rich.map.objects.remove(index);
						sb.append(gold).append(';');
					}
					break;
				case RichManager.OBJECT_TYPE_BMW:
					{
						int movement = 0;
						if (action) {
							int [] arg = {0};
							if (useOneObjectCard(object.type, cfg, arg, commonFlowRecord))
								movement = arg[0];
						}
						richAddMovement(cfg.basic.bmwmove + movement);
						rich.map.objects.remove(index);
						sb.append(cfg.basic.bmwmove).append(';').append(movement).append(';');
					}
					break;
				}
			}
			
			if (object == null && rich.mapExt.size() > 0) {
				SBean.DBRichMapObjectExt objectExt = null;
				SBean.DBRichMapExt ext = rich.mapExt.get(0);
				for (SBean.DBRichMapObjectExt o : ext.objects) {
					if (o.pos == rich.pos) {
						objectExt = o;
						break;
					}
				}
				
				if (objectExt != null) {
					objectExist = true;
					sb.append(objectExt.type).append(';');
					switch (objectExt.type) {
					case RichManager.OBJECT_TYPE_MONEY:
						{
							byte dice = objectExt.params.get(2).byteValue();
							int ratio = 1;
							if (dice > 0) {
								int total = 0;
								for (short poss : cfg.basic.multiplydiceposs)
									total += poss;
								
								int r = gs.getGameData().getRandInt(0, total);
								int add = 0;
								int indexposs = 0;
								for (short poss : cfg.basic.multiplydiceposs) {
									add += poss;
									if (r < add)
										break;
									indexposs ++;
								}
								dice = (byte)(indexposs + 1);
								ratio = dice;
							}
							byte type = objectExt.params.get(0).byteValue();
							int count = objectExt.params.get(1).intValue() * ratio;
							objectExt.params.set(2, 0);
							commonFlowRecord.addChange(addDropNew(new SBean.DropEntryNew(type, (short)0, count)));
							sb.append(type).append(';');
							sb.append(count).append(';');
							sb.append(dice).append(';');
						}
						break;
					case RichManager.OBJECT_TYPE_BOX:
						{
							int open = objectExt.params.get(0).byteValue();
							open ++;
							objectExt.params.set(0, open);
							sb.append(open).append(';');
							sb.append(objectExt.params.size() - 1).append(';');
							if (action) {
								sb.append(1).append(';');
								int cost = 0;
								if (objectExt.params.size() > open) // open - 1 + 1
									cost = objectExt.params.get(open);
								sb.append(cost).append(';'); // cost
								if (cost > 0)
									commonFlowRecord.addChange(useStone(cost));
								cost = 0;
								if (objectExt.params.size() > open + 1) // open - 1 + 2
									cost = objectExt.params.get(open + 1);
								sb.append(cost).append(';'); // next cost
								
								boolean exist = false;
								if (open < cfg.box.size())
									handled = false;
								
								short dropId = 0;
								if (open > 0 && open <= cfg.box.size()) 
									dropId = cfg.box.get(open-1).dropId;
								if (dropId > 0) {
									SBean.DropEntry drop = gs.gameData.getDropTableRandomEntry(dropId);
									if (drop != null) {
										drop = drop.kdClone();
										commonFlowRecord.addChange(addDrop(drop));
										sb.append(drop.arg).append(';');
										sb.append(drop.type).append(';');
										sb.append(drop.id).append(';');
										exist = true;
									}
								}

								if (!exist)
									sb.append(0).append(';');
								
								commonFlowRecord.setArg(1);
							}
							else {
								sb.append(0).append(';');
								commonFlowRecord.setArg(0);
							}
							
							commonFlowRecord.setEvent(TLog.AT_RICH_OPENBOX);
							if (handled)
								ext.objects.remove(objectExt);
						}
						break;
					}
				}
			}
		}
		else if (rich.mapType == 1) {
			SBean.DBRichMapObject object = null;
			int index = 0;
			for (SBean.DBRichMapObject o : rich.map.miniobjects) {
				if (o.pos == rich.minipos) {
					object = o;
					break;
				}
				index ++;
			}
			
			if (object != null) {
				objectExist = true;
				sb.append(object.type).append(';');
				rich.map.miniobjects.remove(index);
				int gold = cfg.basic.minigold.get(rich.minipos -1);
				commonFlowRecord.addChange(richAddGold(gold));
				sb.append(gold).append(';');
			}
		}
		
		if (!objectExist)
			sb.append(RichManager.OBJECT_TYPE_NONE).append(';');
		if (handled)
			rich.status |= RichManager.STATUS_OBJECT_HANDLED;
		
		return sb.toString();
	}
	
	private void richReachBuilding()
	{
		if (rich.mapType == 0) {
			byte bindex[] = {0};
			SBean.DBRichMapBuilding b = gs.getRichManager().getBuildingByPos(rich.pos, rich.map.buildings, bindex);
			if (b == null)
				return;
			
			rich.building = bindex[0];
			if (b.status <= 10)
				b.status = 0;
			else
				b.status = 100;
		}
	}
	
	public void richBlackshop()
	{
		synchronized (this) {
			if (rich == null)
				return;
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_BLACK_SHOP) {
				return;
			}
		}
	}
	
	public void richLotery(List<Byte> numbers)
	{
		Set<Byte> n = new HashSet<Byte>();
		for (byte b : numbers)
			n.add(b);
		if (n.size() < numbers.size()) {
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
			return;
		}
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		synchronized (this) {
			if (rich == null)
				return;
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_LOTERY) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
				return;
			}
			
			if (building.status > 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
				return;				
			}
			
			if (rich.lotery.size() >= 3) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
				return;
			}
			
			if (numbers.size() < rich.lotery.size()) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
				return;
			}
			
			if (stone < cfg.basic.loterycost) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(null));
				return;
			}
			commonFlowRecord.addChange(useStone(cfg.basic.loterycost));
			
			rich.lotery.clear();
			for (Byte b : numbers)
				rich.lotery.add(b);
			gs.getRichManager().setLotery(id, numbers);
			building.status = 1;
		}
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichLotery(numbers));
		commonFlowRecord.setEvent(TLog.AT_RICH_LOTERY);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void richDivine(byte type)
	{
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		SBean.DropEntry entry = null;
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			if (rich == null)
				return;
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_DIVINE) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDivine(null));
				return;
			}	
			
			int index = 0;
			int costGold = 0;
			int costStone = 0;
			switch (type) {
			case RichManager.DIVINE_TYPE_COMMON:
				index = building.params.get(0);
				if (index < cfg.basic.divine1cost.size()) {
					SBean.RichDivineCostCFGS cost = cfg.basic.divine1cost.get(index);
					costGold = cost.gold;
					costStone = cost.stone;
				}
				break;
			case RichManager.DIVINE_TYPE_ADVANCED:
				index = building.params.get(1);
				if (index < cfg.basic.divine2cost.size()) {
					SBean.RichDivineCostCFGS cost = cfg.basic.divine2cost.get(index);
					costGold = cost.gold;
					costStone = cost.stone;
				}
				break;
			case RichManager.DIVINE_TYPE_LUCKY:
				index = building.params.get(2);
				if (index < cfg.basic.divine3cost.size()) {
					SBean.RichDivineCostCFGS cost = cfg.basic.divine3cost.get(index);
					costGold = cost.gold;
					costStone = cost.stone;
				}
				break;
			default:
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDivine(null));
				return;
			}
			
			if (costGold == 0 && costStone == 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDivine(null));
				return;				
			}
			
			if (stone < costStone || rich.gold < costGold) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDivine(null));
				return;
			}
			
			switch (type) {
			case RichManager.DIVINE_TYPE_COMMON:
				entry = GameData.getInstance().getDropTableRandomEntry(cfg.basic.divineDropTbl.get(0));
				break;
			case RichManager.DIVINE_TYPE_ADVANCED:
				entry = GameData.getInstance().getDropTableRandomEntry(cfg.basic.divineDropTbl.get(1));
				break;
			case RichManager.DIVINE_TYPE_LUCKY:
				entry = GameData.getInstance().getDropTableRandomEntry(cfg.basic.divineDropTbl.get(2));
				break;
			}
			
			if (entry != null) {
				if (costGold > 0)
					commonFlowRecord.addChange(richDelGold(costGold));
				if (costStone > 0)
					useStone(costStone);
				addDrop(entry);
				index ++;
				switch (type) {
				case RichManager.DIVINE_TYPE_COMMON:
					building.params.set(0, index);
					break;
				case RichManager.DIVINE_TYPE_ADVANCED:
					building.params.set(1, index);
					break;
				case RichManager.DIVINE_TYPE_LUCKY:
					building.params.set(2, index);
					break;
				}
			}
		}
		
		if (entry != null)
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichDivine(entry));
		
		commonFlowRecord.setEvent(TLog.AT_RICH_DIVINE);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public TLogger.CommonItemChange addRich(int richAdd)
	{
		if (rich == null)
			return null;
		if (richAdd <= 0)
			return null;
		rich.point += richAdd;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_RICH_POINT, 0, richAdd, rich.point);
	}
	
	public TLogger.CommonItemChange useRich(int richUse)
	{
		if (rich == null)
			return null;
		if (richUse <= 0)
			return null;
		rich.point -= richUse;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_RICH_POINT, 0, -richUse, rich.point);
	}
	
	private TLogger.CommonItemChange richAddGold(int add)
	{
		if (rich == null)
			return null;
		if (add <= 0)
			return null;
		rich.gold += add;
		rich.goldTotal += add;
		if (rich.goldHistory < rich.goldTotal)
			rich.goldHistory = rich.goldTotal;
		int gold = rich.goldTotal;
		if (lvlVIP >= 15 && gold < rich.goldHistory)
			gold = rich.goldHistory;
		logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_GOLD, add);
		gs.getRichManager().setGold(gold, id, name, headIconID, lvl, (int)getRichTechPlusEffect(GameData.RICH_TECH_TYPE_RICH_REWARD));
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_RICH_GOLD, 0, add, rich.gold);
	}
	
	private TLogger.CommonItemChange richDelGold(int del)
	{
		if (rich == null)
			return null;
		if (del <= 0)
			return null;
		rich.gold -= del;
		if (rich.gold < 0)
			rich.gold = 0;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_RICH_GOLD, 0, -del, rich.gold);
	}
	
	private void richAddMovement(int add)
	{
		//SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		if (rich == null)
			return;
		if (add <= 0)
			return;
		rich.movement += add;
		//if (rich.movement > cfg.basic.maxmovement)
		//	rich.movement = cfg.basic.maxmovement;
	}
	
	private void richDelMovement(int del)
	{
		if (rich == null)
			return;
		if (del <= 0)
			return;
		rich.movement -= del;
		if (rich.movement < 0)
			rich.movement = 0;
		/*
		if (richMovementRecoverStart == 0)
			richMovementRecoverStart = gs.getTime();
		*/
	}
	
	// Boss status: 0 -> not entered, 1 -> attacking, 2 -> entered
	public void richBossStartAttackRes()
	{
		synchronized (this) {
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_JUBAO) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossStartAttack(-1));
				return;
			}
		
			if (building.status == 1)
				building.status = 0;
			
			if (building.status != 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossStartAttack(-2));
				return;				
			}
			
			if (rich.bossTimes >= 2) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossStartAttack(-3));
				return;
			}
			building.status = 1;
			rich.bossTimes ++;
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossStartAttack(rich.bossTimes));
	}
	
	public void richBossFinishAttackRes(int damage, List<Short> gids)
	{
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		
		int all = 0;
		int newDamage = 0;
		short combatId = 0;
		List<DBRoleGeneral> generals = null;
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			int base = 0;
			for (SBean.RichBossBaseCFGS b : cfg.basic.bossBase) {
				if (lvl < b.lvl) {
					base = b.base;
					break;
				}
			}
			if (damage > cfg.basic.bossMaxDamage)
				damage = cfg.basic.bossMaxDamage;
			all = base + damage / cfg.basic.bossDamage;
			if (all > cfg.basic.bossMaxReward)
				all = cfg.basic.bossMaxReward;
			
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_JUBAO || gids == null) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossFinishAttack(-1));
				return;
			}
			
			generals = copyGeneralsWithoutLock(gids);
			if (generals == null || generals.isEmpty() || generals.size() > 5) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossFinishAttack(-2));
				return;
			}
		
			if (building.status != 1) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossFinishAttack(-3));
				return;				
			}
			building.status = 2;
			
			if (damage > rich.bossDamage) {
				rich.bossDamage = damage;
				newDamage = damage;
			}
			combatId = (short)building.params.get(1).intValue();
			
			if (all > 0)
				commonFlowRecord.addChange(addMoney(all));
			
			commonFlowRecord.setEvent(TLog.AT_RICH_BOSS);
			commonFlowRecord.setArg(combatId, damage);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		
		if (newDamage > 0)
			gs.getRichManager().setDamage(newDamage, id, name, headIconID, lvl, combatId, generals);
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichBossFinishAttack(all));
		
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public List<DBExpiratBossTimes>  getExpiratBossTimesInfo(Role role)
	{
		List<DBExpiratBossTimes> list = new ArrayList<DBExpiratBossTimes>();
		
		
//		dayRefresh();
		
		if(expiratBossInfo!=null){
			updateExpiratBossInfo();
			for(DBExpiratBossTimes i:expiratBossInfo.times){
				list.add(i.kdClone());
			}
		}
		
		return list;
	}
	
	public synchronized DBHeroesBossInfo  getHeroesBossTimesInfo(Role role, String serverName)
	{
		gs.getHeroesBossManager().syncHeroesInfo();
		dayRefresh();
		gs.getHeroesBossManager().setHeroesBossId(id);
				
		if(heroesBossInfo!=null&&heroesBossInfo.ranks!=null&&heroesBossInfo.ranks.size()>0){			
			return heroesBossInfo;	
		}
	
		byte type1 = (byte) gs.getGameData().getHeroesBossType(lvl);
		HeroesBossCFGS info = gs.getGameData().getHeroesBossCFG();
		
		HeroesRank rank = new  HeroesRank(id,type1, new GlobalRoleID(gs.cfg.id,id), name, serverName,
				headIconID, (short)lvl,0, 0, 0, -1,(short)0,(short)0,0, 0);
		
		gs.getHeroesBossManager().syncHeroes(
				new SBean.HeroesBossSyncReq(0,new GlobalRoleID(gs.cfg.id,id),type1, info.gids,info.gcount,rank));
		
		return null;
	}
	
	public int  getExpiratBossLevel()
	{
		if(expiratBossInfo!=null){
			updateExpiratBossInfo();
			return expiratBossInfo.level;
		}
		
		return 0;
	}

	public void updateExpiratBossInfo() {
		int day = gs.getDayByOffset((short) 1366);
		synchronized (this) {
			if (expiratBossInfo.bonusday < day) {
				expiratBossInfo.bonusday = day;
				setExpirateBossInfo();
			}
		}
		
	}
	
	
	public boolean ExpiratBossBuyTimes(short combatId,Role role)
	{
		//VIPCFGS vip = gs.getGameData().getVIPCFG(this.lvlVIP);
		DBExpiratBossTimes dbExpiratBossTimes = null;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			if(expiratBossInfo==null){
				return false;
			}
			
			if(expiratBossInfo!=null){
				updateExpiratBossInfo();
			}
//			dayRefresh();

			
			for(DBExpiratBossTimes i:expiratBossInfo.times){
				if(combatId==i.id){
					dbExpiratBossTimes=i;
				}
			}
			
			if(dbExpiratBossTimes==null){
				return false;//expiratBossInfo.times.add(new DBExpiratBossTimes(combatId,(byte)0,(byte) 0));
			}
			
			if(dbExpiratBossTimes.buytimes>=2){
				return false;
			}
			
			int stone1 = 0;
			
			if(dbExpiratBossTimes.buytimes==0){
				stone1=gs.getGameData().expiratBoss.cost1;
			}else{
				stone1=gs.getGameData().expiratBoss.cost2;
			}
			
			if(this.stone<stone1){
				return false;
			}
			
			
			commonFlowRecord.addChange(this.useStone(stone1));
			dbExpiratBossTimes.buytimes++;
		}
		
		commonFlowRecord.setEvent(TLog.AT_EXPIRAT_BOSS);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized int getExpiratBossTimes(int combatId){
		DBExpiratBossTimes dbExpiratBossTimes = null;
		for(DBExpiratBossTimes i:expiratBossInfo.times){
			if(combatId==i.id){
				dbExpiratBossTimes=i;
			}
		}
		if(dbExpiratBossTimes==null){
			return 0;
		}
		
		return dbExpiratBossTimes.buytimes;
		
	}
	
	public void ExpiratBossStartAttackRes(short combatId)
	{
		ExpiratBossBasicCFGS cfg = gs.getGameData().getExpiratBossBasicCFG(combatId);
		
		if(cfg==null){
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeExpiratBossStartAttackRes(combatId,-1,null));
			return;
		}
		
		synchronized (this) {
			Bossdrops.clear();
			SBean.DropEntry drop = null;
			for(int dropIdTemp : cfg.dropId){
				drop = gs.gameData.getDropTableRandomEntry((short) dropIdTemp);
				if (drop != null){
					Bossdrops.add(drop);
				}
			}		
		}			
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeExpiratBossStartAttackRes(combatId,1,Bossdrops));	
	}
	
	public void ExpiratBossFinishAttackRes(short combatId,int damage, String serverName,List<Short> gids)
	{
		ExpiratBossBasicCFGS cfg = gs.getGameData().getExpiratBossBasicCFG(combatId);
		
		if(cfg==null){
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeExpiratBossFinishAttackRes(damage,combatId,-1,null));
			return;
		}
		int newDamage = 0;

		List<SBean.DropEntry> dropsList = new ArrayList<SBean.DropEntry>();
		synchronized (this) {
			DBExpiratBossTimes dbExpiratBossTimes = null;
			
			if(expiratBossInfo==null){
			   expiratBossInfo = new SBean.DBExpiratBossInfo();
			   expiratBossInfo.times= new ArrayList<SBean.DBExpiratBossTimes>();
			   expiratBossInfo.damageHis= new ArrayList<SBean.DBExpiratBossDamage>();
			   expiratBossInfo.bonusday = gs.getDayByOffset((short) 1366);
			   expiratBossInfo.level = this.lvl;
			}
			
			for(DBExpiratBossTimes i:expiratBossInfo.times){
				if(combatId==i.id){
					dbExpiratBossTimes=i;
				}
			}
			if(dbExpiratBossTimes!=null){
				if (dbExpiratBossTimes.times >= gs.getGameData().expiratBoss.count+dbExpiratBossTimes.buytimes) {
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeExpiratBossFinishAttackRes(damage,combatId,-3,null));
					return;
				}
			}
						
//			if (damage > expiratBossInfo.damage) {
//				expiratBossInfo.damage = damage;
//				newDamage = damage;
//			}
			if(dbExpiratBossTimes==null){
				dbExpiratBossTimes = new DBExpiratBossTimes(combatId,(byte) 1,(byte) 0,0,0);
				expiratBossInfo.level= this.lvl;
				expiratBossInfo.times.add(dbExpiratBossTimes);				
			}else{
				if(dbExpiratBossTimes.times==0){
					expiratBossInfo.level= this.lvl;
				}
				dbExpiratBossTimes.times++;
			}
			
			if(expiratBossInfo.times.size()>1){
				return;
			}
			
			if(damage>Integer.MAX_VALUE-dbExpiratBossTimes.damage){
				damage=Integer.MAX_VALUE-dbExpiratBossTimes.damage;
			}
			dbExpiratBossTimes.damage += damage;
			newDamage = dbExpiratBossTimes.damage;
			expiratBossInfo.damage=newDamage;
		
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			
			for(DropEntry dropi : Bossdrops){			
				if (dropi != null){
					dropsList.add(dropi);
					commonFlowRecord.addChange(addDropNew(new SBean.DropEntryNew(dropi.type, dropi.id, dropi.arg)));
				}
			}
			
			commonFlowRecord.setEvent(TLog.AT_EXPIRATBOSS_FINISH);
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
		
		if (newDamage > 0)
			gs.getExpiratBossManager().setDamage(combatId,newDamage, id, name,serverName, headIconID, lvl);
		
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeExpiratBossFinishAttackRes(damage,combatId,1,dropsList));
		
	}
	
	public int  getExpiratBossDamage(){
		if(expiratBossInfo==null){
			return 0;
		}
		updateExpiratBossInfo();
		return expiratBossInfo.damage;
	}
	
	public short useBestowItem2Weopen(short id2, short cnt)
	{
//		List<ItemCFGS> bestow = gs.getGameData().getBestowItemCFG();
//		
//		if(bestow.size()<=0){
//			return -1;
//		}
//		int random = gs.getGameData().getRandom().nextInt(bestow.size());
//		
//		if(random<0||random>=bestow.size()){
//			return -1;
//		}
//		
//		ItemCFGS  ranitem = bestow.get(random);
//		if(ranitem==null){
//			return -1;
//		}
		
		ItemCFGS bestow = gs.getGameData().getItemCFG(id2);
		if(bestow==null){
			return -1;
		}
		this.addItem((short)bestow.arg1, cnt);
		return 1;
	}
	
	public short useBestowItem()
	{
		List<ItemCFGS> bestow = gs.getGameData().getBestowItemCFG();
		
		if(bestow.size()<=0){
			return -1;
		}
		int random = gs.getGameData().getRandom().nextInt(bestow.size());
		
		if(random<0||random>=bestow.size()){
			return -1;
		}
		
		ItemCFGS  ranitem = bestow.get(random);
		if(ranitem==null){
			return -1;
		}
		this.addItem(ranitem.id, 1);
		return 1;
	}
	
	public short addheadicon(short type)
	{
		if(headicon==null){
			headicon = new DBHeadIcon((short)0,new  ArrayList<DBHeadIconList>(),0,(short)0,0,0);
		}
		
		for(DBHeadIconList icon :headicon.headicon){
			HeadIconCFGS cfg2 =gs.gameData.getHeadiconCFG(icon.headid);
			if(cfg2==null){
				continue;
			}
			if(icon.headid==type){
			   icon.headtime=gs.getTime();
			   return 1;
			}
		}

		SBean.DBHeadIconList list = new SBean.DBHeadIconList(type,gs.getTime());
		headicon.headicon.add(list);
		return 1;
	}
	
	
	public short bestowItem(short iid)
	{
		SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(iid);
		if( icfg == null  )
			return 0;
		
		if(icfg.type!= GameData.ITEM_TYPE_BESTOW_WEOPEN){
			return -1;
		}
		
		BestowItemCFGS cfg2 = GameData.getInstance().getBestowItemsCFG();
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if(this.stone<GameData.bestowCost){
				return -2;
			}
			
			if(getItemCount(iid) < 1 )
				return -3;
			
			
			commonFlowRecord.addChange(this.useStone(GameData.bestowCost));
			delItem(iid, 1);
//			this.addItem((short)GameData.bestowId, 1);
			if(cfg2.item1.contains((short)icfg.id)){
				this.addItem((short)cfg2.iid1, 1);
			}else if(cfg2.item2.contains((short)icfg.id)){
				this.addItem((short)cfg2.iid2, 1);
			}
		}
		
		commonFlowRecord.setEvent(TLog.AT_BESTOW_ITEM);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return 1;
	}
	
	public boolean upbestow(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			General g = generals.get(gid);
			if( g == null )
				return false;
				
			if(g.bestow==null||g.bestow.size()==0){
				g.bestow = new ArrayList<SBean.DBGeneralBestow>();
				g.bestow.add(new SBean.DBGeneralBestow((short) 0, new ArrayList<DBBestowLevel> (), 0, 0));
			}
			
			DBGeneralBestow bestow = g.bestow.get(0);
			
			if(bestow==null){
				return false;
			}
			
			BestowCFGS cfg = gs.getGameData().getBestowCFG(gid, bestow.lvl);
			
			if(cfg==null){
				return false;
			}
			
			
			Set<Short> itemIDs = new TreeSet<Short>();
			Set<Short> equipIDs = new TreeSet<Short>();
			for(SBean.DropEntryNew e : cfg.eItems)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					if( itemIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getItemCount(e.id) < e.arg )
						return false;
					SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(e.id);
					if( icfg == null  )
						return false;
					itemIDs.add(e.id);
				}
				else
				{
					if( equipIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getEquipCount(e.id) < e.arg )
						return false;
					SBean.EquipCFGS ecfg2 = GameData.getInstance().getEquipCFG(e.id);
					if( ecfg2 == null  )
						return false;
					equipIDs.add(e.id);
				}
			}
			
			
			for(SBean.DropEntryNew e : cfg.eItems)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					delItem(e.id, e.arg);
				}
				else
				{
					delEquip(e.id, e.arg);
				}
			}
			
			bestow.lvl++;
			
			if(true){
				for(General general : generals.values()){
					if(general!=null){
						if(general.bestow==null||general.bestow.size()==0){
							general.bestow = new ArrayList<SBean.DBGeneralBestow>();
							general.bestow.add(new SBean.DBGeneralBestow((short) 0, new ArrayList<DBBestowLevel> (), 0, 0));
						}
						List<DBBestowLevel> level = general.bestow.get(0).bestowLevel;
							
						boolean has = false;
						for(DBBestowLevel lvl :level){
							
							if(lvl.gid ==gid){
								lvl.lvl=bestow.lvl;
								has = true;
							}
						}
						
						if(!has){
//							general.bestow.get(0).bestowLevel = new ArrayList<SBean.DBBestowLevel>();
							general.bestow.get(0).bestowLevel.add(new SBean.DBBestowLevel(bestow.lvl,gid));
						}
						}
						
					}

				}
			}
					
			commonFlowRecord.setEvent(TLog.AT_SEYEN_ADD);
			commonFlowRecord.setArg(0, gid, 0, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
//		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	
	public boolean upbless(List<Short> gids,short type)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		CommomConstantCFGS cfg2 = gs.getGameData().getCommonCFG();
		synchronized( this )
		{
			int refresh1 = gs.getDayByOffset((short)cfg2.blessTime1) ;
			int refresh2 = gs.getDayByOffset((short)cfg2.blessTime2) ;
			
			if(this.bless!=null&&this.bless.refresh1<refresh1){
				this.bless.refresh1=refresh1;			
				for(General g : generals.values()) {
					if(g.bless!=null&&g.bless.size()>0&&g.bless.get(0)!=null){
						g.bless.get(0).state=0;
					}
				}				
			}
			
			if(this.bless!=null&&this.bless.refresh2<refresh2){
				this.bless.refresh2=refresh2;
				if(this.bless.gid!=null){
					this.bless.gid.clear();
				}
			}
			
			this.dayRefresh();
			
			if(gids==null||gids.size()<=0){
				return false;
			}
			
			int size = gids.size();
			if(type<1||type>3){
				return false;
			}
			
			BlessCFGS cfg = gs.getGameData().getBlessCFG(type);
		
			if(cfg==null){
				return false;
			}
			

			if(this.bless!=null&&this.bless.gid!=null&&this.bless.gid.size()>=cfg2.blessMaxNum){
				return false;
			}
			
			for(short gid :gids ){
				General g = generals.get(gid);
				if( g == null )
					return false;
					
				if(g.bless==null||g.bless.size()==0){
					g.bless = new ArrayList<SBean.DBGeneralBless>();
					g.bless.add(new SBean.DBGeneralBless());
				}
				
				DBGeneralBless bless = g.bless.get(0);
				
				if(bless==null||bless.state >cfg2.blessCnt){
					return false;
				}
				
				if(bless.p1>cfg.p1Max&&bless.p2>cfg.p2Max&&bless.p3>cfg.p3Max){
					return false;
				}
				
			}
			
			Set<Short> itemIDs = new TreeSet<Short>();
			Set<Short> equipIDs = new TreeSet<Short>();
			for(SBean.DropEntryNew e : cfg.eItems)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					if( itemIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getItemCount(e.id) < e.arg*size )
						return false;
					SBean.ItemCFGS icfg = GameData.getInstance().getItemCFG(e.id);
					if( icfg == null  )
						return false;
					itemIDs.add(e.id);
				}
				else
				{
					if( equipIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getEquipCount(e.id) < e.arg*size )
						return false;
					SBean.EquipCFGS ecfg2 = GameData.getInstance().getEquipCFG(e.id);
					if( ecfg2 == null  )
						return false;
					equipIDs.add(e.id);
				}
			}
			
			for(SBean.DropEntryNew e : cfg.eItems)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					delItem(e.id, e.arg*size);
				}
				else
				{
					delEquip(e.id, e.arg*size);
				}
			}
			
			for(short gid :gids ){
				General g = generals.get(gid);
				if( g == null )
					return false;
					
				if(g.bless==null||g.bless.size()==0){
					g.bless = new ArrayList<SBean.DBGeneralBless>();
					g.bless.add(new SBean.DBGeneralBless());
				}
				
				DBGeneralBless bless = g.bless.get(0);
				
				if(bless==null){
					return false;
				}
					
				bless.p1+=cfg.p1;
				bless.p2+=cfg.p2;
				bless.p3+=cfg.p3;
				bless.state ++;
				
				if(bless.p1>cfg.p1Max){
					bless.p1=cfg.p1Max;
				}
				
				if(bless.p2>cfg.p2Max){
					bless.p2=cfg.p2Max;
				}
				
				if(bless.p3>cfg.p3Max){
					bless.p3=cfg.p3Max;
				}
				
				if(this.bless==null){
					
					this.bless = new SBean.DBBlessInfo();
					this.bless.gid = new ArrayList<Short>();
					this.bless.refresh1 = refresh1;
					this.bless.refresh2 = refresh2;
				}
				
				this.bless.gid.add(g.id);
			}
			
		}			
			commonFlowRecord.setEvent(TLog.AT_SEYEN_ADD);
			commonFlowRecord.setArg(0, 0, 0, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
//		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized short activeheadicon(short type)
	{
		if(headicon==null||headicon.headicon==null){
			return -1;
		}
//		if(!headicon.headicon.contains((Short)type)){
//			return -1;
//		}
		HeadIconCFGS cfg =gs.gameData.getHeadiconCFG(type);
		if(cfg==null){
			return -1;
		}
		
		this.headIconID = (short)(this.headIconID+(type-headicon.headcur)*100);
		headicon.headcur = type;
		
		for(General general : generals.values()){
			if(general!=null){
				
				general.headicon=(byte)type;
				
			}
		}
		return headIconID;
	}
	
	public DBHeadIcon headiconSync()
	{
		checkHeadTime();
		return headicon;
	}

	public void checkHeadTime() {
		try {
			if(this.headicon!=null&&headicon.headicon!=null){
				List<DBHeadIconList> rev = new ArrayList<DBHeadIconList>();
				for(DBHeadIconList icon :headicon.headicon){
					HeadIconCFGS cfg =gs.gameData.getHeadiconCFG(icon.headid);
					if(cfg==null){
						continue;
					}
					if(cfg.etime!=-1&& icon.headtime+cfg.etime*24*3600<=gs.getTime()){
						rev.add(icon);
					}
				}
				
				if(rev.size()>0){
					for(DBHeadIconList icon1 : rev){
						if(icon1.headid==headicon.headcur){
							headIconID = (short)(headIconID - icon1.headid*100);
							headicon.headcur =0;
							for(General general : generals.values()){
								if(general!=null){								
									general.headicon=(byte)0;	
								}

							}
						}
						headicon.headicon.remove(icon1);
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_HEAD_ICON_DELETE, "", ""+(short)icon1.headid, 0, true, attLst);
						
					}
					DBHeadIcon ok=headiconSync();
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeHeadiconSyncRes(ok));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean heroesBossBuffTimes()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			if(heroesBossInfo==null){
				return false;
			}

			dayRefresh();
				
			HeroesBossBuffCFGS  buff = gs.getGameData().getHeroesBossBuffCFG(heroesBossInfo.buffCount);
			
			if(buff==null){
				return false;
			}
			
			if(this.stone<buff.cost){
				return false;
			}
			
			commonFlowRecord.addChange(this.useStone(buff.cost));
			heroesBossInfo.buffCount++;
			
			if(this.heroesBossInfo.ranks!=null){
				byte tId = (byte) gs.getGameData().getHeroesBossType(lvl);
				HeroesRank d = null;
				for(HeroesRank rank :this.heroesBossInfo.ranks){
					if(rank.rid==id){
						d = rank;
					}
				}
				
				if(d==null){
					if(type==1){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,"", headIconID,lvl,0 ,0,0,0,(short)0,(short)0,0, 0);
					}else if(type==2){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,"",  headIconID,lvl,0 ,0,0,0,(short)0,(short)0,0, 0);
					}else if(type==3){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,"",  headIconID,lvl,0 ,0,0,0,(short)0,(short)0,0, 0);
					}
					if(d!=null){
						heroesBossInfo.ranks.add(d);	
					}
									
				}else{
					
					d.buffLvl = heroesBossInfo.buffCount;
					
				}
				List<GlobalRoleID> serverIds = new ArrayList<GlobalRoleID> ();
				for(HeroesRank rank :heroesBossInfo.ranks){
					serverIds.add(rank.roleID);
				}
				
				gs.getHeroesBossManager().syncHeroesFinishAttack(
						new SBean.HeroesBossFinishAttReq(0,new GlobalRoleID(gs.cfg.id,id), (short)4,id, 
								type, 0,d.kdClone(), serverIds, heroesBossInfo.ranks));
			}
			
		}
		commonFlowRecord.setEvent(TLog.AT_HERO_BOSS);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized List<HeroesRank> getDamageRanks()
	{	
		
		return heroesBossInfo.ranks;
	}
	
	public synchronized List<HeroesRank> getHeroesBonusRanks()
	{	
		Map<Integer,Integer> rid2score = new HashMap<Integer,Integer>();
		List<HeroesRank> rankst = heroesBossInfo.ranks;
	
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				return rank2.score1 -rank1.score1;
			}
		});
		
		short index = 0;
		for(HeroesRank s1:rankst){
			
			HeroesBossScoreCFGS  sc1 = gs.getGameData().getHeroesBossScoreCFG(index,(short)1);
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
		}
		
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				return rank2.score2 -rank1.score2;
			}
		});
		
		index = 0;
		for(HeroesRank s1:rankst){
			
			HeroesBossScoreCFGS  sc1 = gs.getGameData().getHeroesBossScoreCFG(index,(short)2);
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
		}
		
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				return rank2.score3 -rank1.score3;
			}
		});
		
		index = 0;
		for(HeroesRank s1:rankst){
			
			HeroesBossScoreCFGS  sc1 = gs.getGameData().getHeroesBossScoreCFG(index,(short)3);
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
		}
		
		List<Map.Entry<Integer, Integer>> list_Data = new ArrayList<Map.Entry<Integer,Integer>>(rid2score.entrySet());  
                Collections.sort(list_Data,new Comparator<Map.Entry<Integer, Integer>>() {  
  
	            @Override  
	            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {  
	                return (o2.getValue() - o1.getValue());  
	            }  
        });
		
		return heroesBossInfo.ranks;
	}
	
	
	public void heroesBossFinishAttackRes(short type ,int damage, String serverName, int power)
	{
		byte tId = (byte) gs.getGameData().getHeroesBossType(lvl);
		
		if(tId==-1){
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeHeroesBossFinishAttackRes(type,damage,-1));
			return;
		}
		int newDamage = 0;

		synchronized (this) {
			DBHeroesBossTimes dbHeroesBossTimes = null;
			
			if(heroesBossInfo==null){
//				heroesBossInfo = new SBean.DBHeroesBossInfo();
				heroesBossInfo=new DBHeroesBossInfo(new ArrayList<HeroesRank>(), 
						new ArrayList<DBHeroesBossTimes> (), (short) 0, 0, 
                        0, 0, 0, 0, 0);
				heroesBossInfo.times= new ArrayList<SBean.DBHeroesBossTimes>();
			}
			
			for(DBHeroesBossTimes i:heroesBossInfo.times){
				if(type==i.id){
					dbHeroesBossTimes=i;
				}
			}
			if(dbHeroesBossTimes!=null){
				if (dbHeroesBossTimes.times >= gs.getGameData().expiratBoss.count) {
					gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeHeroesBossFinishAttackRes(type,damage,-3));
					return;
				}
			}
						
			if(dbHeroesBossTimes==null){
				dbHeroesBossTimes = new DBHeroesBossTimes(type,(byte) 1,(byte) 0,0,0);
				heroesBossInfo.times.add(dbHeroesBossTimes);				
			}else{
				dbHeroesBossTimes.times++;
			}
			
			if(heroesBossInfo.times.size()>3){
				return;
			}
			
//			if(damage>Integer.MAX_VALUE-dbHeroesBossTimes.damage){
//				damage=Integer.MAX_VALUE-dbHeroesBossTimes.damage;
//			}
//			dbHeroesBossTimes.damage += damage;
//			newDamage = dbHeroesBossTimes.damage;
			
			if(damage>dbHeroesBossTimes.damage){
				dbHeroesBossTimes.damage = damage;
				newDamage = damage;
			}
			
//			if(type==3&&dbHeroesBossTimes.damage>newDamage){
//				newDamage = dbHeroesBossTimes.damage;
//			}
		
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					
			commonFlowRecord.setEvent(TLog.AT_EXPIRATBOSS_FINISH);
			tlogEvent.addCommonFlow(commonFlowRecord);
//			gs.getTLogger().logEvent(this, tlogEvent);
			if (newDamage > 0||(type==3&&newDamage >= dbHeroesBossTimes.damage)){
				
				if(type==1){
					heroesBossInfo.score1=newDamage;
				}else if(type==2){
					heroesBossInfo.score2=newDamage;
				}else if(type==3){
					heroesBossInfo.score3=newDamage;
				}
				
				HeroesRank d = null;
				for(HeroesRank rank :this.heroesBossInfo.ranks){
					if(rank.rid==id){
						d = rank;
					}
				}
				
				if(d==null){
					if(type==1){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,serverName, headIconID,lvl,power ,newDamage,0,0,(short)0,(short)1,0, 0);
					}else if(type==2){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,serverName,  headIconID,lvl,power ,0,newDamage,0,(short)0,(short)1,0, 0);
					}else if(type==3){
						d = new HeroesRank(id,tId, new GlobalRoleID(gs.cfg.id,id), name,serverName,  headIconID,lvl,power ,0,0,newDamage,(short)0,(short)1,0, 0);
					}
					if(d!=null){
						heroesBossInfo.ranks.add(d);	
					}				
				}else{
					if(type==1){
						d.lvl = lvl;
						d.power = power;
						d.headIconId=headIconID;
						d.score1=newDamage;
					}else if(type==2){
						d.lvl = lvl;
						d.power = power;
						d.headIconId=headIconID;
						d.score2=newDamage;
					}else if(type==3){
						d.lvl = lvl;
						d.power = power;
						d.headIconId=headIconID;
						d.score3=newDamage;
					}
					d.buffLvl=heroesBossInfo.buffCount;
					short ti = 0;
					for(DBHeroesBossTimes time :heroesBossInfo.times){
						ti+=time.times;
					}
					d.attc = ti;
				}
	 
				gs.getHeroesBossManager().setHeroesBossId(id);
				
				List<GlobalRoleID> serverIds = new ArrayList<GlobalRoleID> ();
				for(HeroesRank rank :heroesBossInfo.ranks){
					serverIds.add(rank.roleID);
				}
				
				gs.getHeroesBossManager().syncHeroesFinishAttack(
						new SBean.HeroesBossFinishAttReq(0,new GlobalRoleID(gs.cfg.id,id), tId,id, 
								type, newDamage,d.kdClone(), serverIds, heroesBossInfo.ranks));
		   }
		
		
		}
			
		
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeHeroesBossFinishAttackRes(type,damage,1));
		
	}
	
	
	// Mine status: 0 -> not entered, 1 -> attacking, 2 -> payed, 100 -> occupied
	public void richMineBuyPassRes()
	{
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_MINE && building.type != RichManager.MAP_TYPE_RICH_MINE) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineBuyPass(-1));
				return;
			}
		
			if (building.status == 1)
				building.status = 0;
			
			if (building.status != 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineBuyPass(-2));
				return;				
			}
			
			/*
			if ((building.type == RichManager.MAP_TYPE_MINE && rich.gold < cfg.basic.buyPassGold) ||
					(building.type == RichManager.MAP_TYPE_RICH_MINE && rich.gold < cfg.basic.richBuyPassGold)) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineBuyPass(-3));
				return;				
			}
			*/
			if (building.type == RichManager.MAP_TYPE_MINE)
				commonFlowRecord.addChange(richDelGold(cfg.basic.buyPassGold));
			else if (building.type == RichManager.MAP_TYPE_RICH_MINE)
				commonFlowRecord.addChange(richDelGold(cfg.basic.richBuyPassGold));
			building.status = 2;
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineBuyPass(0));
		
		commonFlowRecord.setEvent(TLog.AT_RICH_BYPASS);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void richMineTaxRes()
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_MINE && building.type != RichManager.MAP_TYPE_RICH_MINE) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineTax(-1));
				return;
			}
		
			if (building.status != 100) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineTax(-2));
				return;				
			}
			
			commonFlowRecord.addChange(richAddGold(building.params.get(3)));
			building.status = 101;
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineTax(0));
		
		commonFlowRecord.setEvent(TLog.AT_RICH_MINE);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void richMineStartAttackRes()
	{
		synchronized (this) {
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_MINE && building.type != RichManager.MAP_TYPE_RICH_MINE) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineStartAttack(-1, null, 0, 0));
				return;
			}
		
			if (building.params.size() <= 1) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineStartAttack(-2, null, 0, 0));
				return;
			}
			int combatId = building.params.get(1);
			SBean.CombatCFGS cfg = gs.gameData.getCombatCFG((short)combatId);
			if (cfg == null) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineStartAttack(-3, null, 0, 0));
				return;
			}
			
			if (building.status == 1)
				building.status = 0;
			
			if (building.status != 0) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineStartAttack(-4, null, 0, 0));
				return;				
			}
			building.status = 1;
			GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, (byte)1, (byte)1, (byte)1, (byte)1, null, null, null, (byte)0); 
			richCombatBonus = bonusInfo.cb;
			int reward = building.params.get(2);
			if (richCombatBonus != null && reward > 0)
				richCombatBonus.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_RICH_POINT, (short)0, reward));
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineStartAttack(0, richCombatBonus, rich.thiefHpDownEndRate, rich.thiefDefenseDownEndRate));
	}
	
	public void richMineFinishAttackRes(boolean win)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			SBean.DBRichMapBuilding building = rich.map.buildings.get(rich.building);
			if (building.type != RichManager.MAP_TYPE_MINE && building.type != RichManager.MAP_TYPE_RICH_MINE) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineFinishAttack(-1));
				return;
			}
		
			if (building.status != 1) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineFinishAttack(-2));
				return;				
			}
			
			if (win) {
				building.status = 101; // gs.getTime();
				if (richCombatBonus != null) {
					for (SBean.DropEntryNew d : richCombatBonus.entryIDs)
						commonFlowRecord.addChange(addDropNew(d));
				}
				logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_SHANZEI, 1);
			}
			else
			{
				building.status = 0;
			}
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeRichMineFinishAttack(0));
		
		commonFlowRecord.setEvent(TLog.AT_RICH_OCCUPY_MINE);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public boolean richBuyMovement()
	{
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			int index = rich.buyMovementTimes;
			if( index >= cfg.basic.buyMovementCost.size() )
				return false;
			int stoneNeed = cfg.basic.buyMovementCost.get(index);
			if( stone < stoneNeed )
				return false;
			commonFlowRecord.addChange(useStone(stoneNeed));
			richAddMovement(cfg.basic.buyMovementVal);
			++rich.buyMovementTimes;
			
			commonFlowRecord.setEvent(TLog.AT_RICH_BUYMOVEMENT);
			commonFlowRecord.setArg(cfg.basic.buyMovementVal, rich.buyMovementTimes);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public String richUseItem(short itemID, short cnt)
	{
		StringBuilder sb = new StringBuilder();
		SBean.ItemCFGS cfg = gs.getGameData().getItemCFG(itemID);
		if( cfg == null )
			return sb.append(0).append(";").toString();
		short rcnt = 0;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			if( lvl < cfg.lvlReq )
				return sb.append(0).append(";").toString();
			if( getItemCount(itemID) < cnt || cnt <= 0 )
				return sb.append(0).append(";").toString();
//			if( cnt != 1 )
//				return sb.append(0).append(";").toString();
			switch( cfg.type )
			{
			case GameData.ITEM_TYPE_RICH_MINING_BOOM:
				{
					int round = cfg.arg1;
					float prouctRate = cfg.arg2/100.0f;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichMiningBoom(round, prouctRate));
				}
				break;
			case GameData.ITEM_TYPE_RICH_MUNIU:
				{
					float rate = cfg.arg1/100.0f;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichMuniu(rate, commonFlowRecord,cnt));
				}
				break;
			case GameData.ITEM_TYPE_RICH_WUZHONGSHENGYOU:
				{
					int count = cfg.arg1;
					short dropTblID = (short)cfg.arg2;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichWozhongshengyou(count*cnt, dropTblID, commonFlowRecord));
				}
				break;
			case GameData.ITEM_TYPE_RICH_CASH:
				{
					int minVal = cfg.arg1;
					int maxVal = cfg.arg2;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichCash(minVal, maxVal, commonFlowRecord,cnt));
				}
				break;
			case GameData.ITEM_TYPE_RICH_MOBILITY:
				{
					int minVal = cfg.arg1;
					int maxVal = cfg.arg2;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichMobility(minVal, maxVal,cnt));
				}
				break;
			case GameData.ITEM_TYPE_RICH_FLY:
				{
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichFly());
				}
				break;
			case GameData.ITEM_TYPE_RICH_GO_WEST:
				{
					int minVal = cfg.arg1;
					int maxVal = cfg.arg2;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichGoWest(minVal, maxVal));
				}
				break;
			case GameData.ITEM_TYPE_RICH_WANJIANQIFA:
				{
					float reduceRate = cfg.arg1/100.0f;
					float endRate = cfg.arg2/100.0f;
					int bIndex = getRichMineBuildingIndex();
					if (bIndex < 0)
						return sb.append(0).append(";").toString();
					//gs.getLogger().debug("thiefHpDownEndRate="+rich.thiefHpDownEndRate + ", endRate="+endRate);
					if (rich.thiefHpDownEndRate >= 1 - endRate)
						return sb.append(-1).append(";").append("wanjianqifa;").toString();
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichWanjianqifa(reduceRate, endRate));
				}
				break;
			case GameData.ITEM_TYPE_RICH_TIANLEIGUNGUN:
				{
					float reduceRate = cfg.arg1/100.0f;
					float endRate = cfg.arg2/100.0f;
					int bIndex = getRichMineBuildingIndex();
					if (bIndex < 0)
						return sb.append(0).append(";").toString();
					//gs.getLogger().debug("thiefDefenseDownEndRate="+rich.thiefDefenseDownEndRate + ", endRate="+endRate);
					if (rich.thiefDefenseDownEndRate >= 1 - endRate)
						return sb.append(-1).append(";").append("tianleigungun;").toString();
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichTianleigungun(reduceRate, endRate));
				}
				break;
			case GameData.ITEM_TYPE_RICH_KILLYOU:
				{
					int bIndex = getRichMineBuildingIndex();
					if (bIndex < 0)
						return sb.append(0).append(";").toString();
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichKillYou(bIndex, commonFlowRecord));
				}
				break;
			case GameData.ITEM_TYPE_RICH_TIANNVSANHUA:
				{
					short dropTblID = (short)cfg.arg1;
					rcnt = cnt;
					sb.append(rcnt).append(";");
					sb.append(fireRichTiannvsanhua(dropTblID));
				}
				break;
			default:
				return sb.append(0).append(";").toString();
			}
			if( rcnt > 0 )
			{
				commonFlowRecord.addChange(delItem(itemID, rcnt));
				logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_USE_CARD_ANY, rcnt);
			}
			commonFlowRecord.setEvent(TLog.AT_ITEM_USE);
			commonFlowRecord.setArg(itemID, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return sb.toString();
	}
	
	String fireRichMiningBoom(int round, float rate)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("miningboom;");
		rich.boomLeft = (byte)round;
		rich.boomUpRate = rate;
		sb.append(1).append(";");
		sb.append(round).append(";");
		sb.append(rate).append(";");
		return sb.toString();
	}
	
	String fireRichMuniu(float rate, TLogger.CommonFlowRecord commonFlowRecord, short cnt)
	{
		SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
		StringBuilder sb = new StringBuilder();
		sb.append("muniu;");
		int goldGain = 0;
		for (SBean.DBRichMapBuilding b : rich.map.buildings) {
			if (b.type == RichManager.MAP_TYPE_MINE || b.type == RichManager.MAP_TYPE_RICH_MINE) {
				if (b.status > 10) {
					if (b.type == RichManager.MAP_TYPE_MINE)
						goldGain += (int)(cfg.basic.mine.get(0).gain * rate);
					else if (b.type == RichManager.MAP_TYPE_RICH_MINE)
						goldGain += (int)(cfg.basic.mine.get(1).gain * rate);
				}
			}
		}
		if (goldGain > 0)
			commonFlowRecord.addChange(richAddGold(goldGain*cnt));
		sb.append(1).append(";");
		sb.append(goldGain*cnt).append(";");
		return sb.toString();
	}
	
	String fireRichWozhongshengyou(int count, short dropTblID, TLogger.CommonFlowRecord commonFlowRecord)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("wuzhongshengyou;");
		List<SBean.DropEntryNew> drops = new ArrayList<SBean.DropEntryNew>();
		for (int i = 0; i < count; ++i)
		{
			SBean.DropEntryNew drop = createRandomRichItem(dropTblID);
			if (drop != null)
				drops.add(drop);
		}
		sb.append(1).append(";");
		sb.append(drops.size()).append(";");
		for (SBean.DropEntryNew e : drops)
		{
			commonFlowRecord.addChange(addDropNew(e));
			sb.append(e.type).append(";");
			sb.append(e.id).append(";");
			sb.append(e.arg).append(";");
		}
		return sb.toString();
	}
	
	SBean.DropEntryNew createRandomRichItem(short dropTbl)
	{
		SBean.DropEntry d = GameData.getInstance().getDropTableRandomEntry(dropTbl);
		if (d == null)
			return null;
		return new SBean.DropEntryNew(d.type, d.id, d.arg);
	}
	
	SBean.DropEntryNew createRandomRoadItem()
	{
		short dropTblID = GameData.getInstance().getRichCFG().basic.roadEnventDropTbl;
		return createRandomRichItem(dropTblID);
	}
	
	String fireRichCash(int minVal, int maxVal, TLogger.CommonFlowRecord commonFlowRecord, short cnt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("cashcard;");
		int gold = gs.getGameData().getRandInt(minVal, maxVal + 1);
		if (gold > 0)
			commonFlowRecord.addChange(richAddGold(gold*cnt));
		sb.append(1).append(";");
		sb.append(gold*cnt).append(";");
		return sb.toString();
	}
	
	String fireRichMobility(int minVal, int maxVal, short cnt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("mobility;");
		int move = gs.getGameData().getRandInt(minVal, maxVal + 1);
		if (move > 0)
			richAddMovement(move*cnt);
		sb.append(1).append(";");
		sb.append(move*cnt).append(";");
		return sb.toString();
	}
	
	String fireRichFly()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("fly;");
		//int fly = gs.getGameData().getRandInt(0, RichManager.MAP_LENGTH);
		rich.status |= RichManager.STATUS_FLY;
		rich.status &= ~RichManager.STATUS_GO_WEST;
		sb.append(1);
		//boolean bOK = richFly((short)fly);
		//sb.append(bOK ? 1 : 0);
		return sb.toString();
	}
	
	String fireRichGoWest(int minVal, int maxVal)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("gowest;");
		rich.status |= RichManager.STATUS_GO_WEST;
		rich.status &= ~RichManager.STATUS_FLY;
		sb.append(1);
		return sb.toString();
	}
	
	String fireRichWanjianqifa(float reduceRate, float endRate)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("wanjianqifa;");
		rich.thiefHpDownRate = reduceRate;
		rich.thiefHpDownEndRate += reduceRate;
		if (rich.thiefHpDownEndRate > 1 - endRate)
			rich.thiefHpDownEndRate = 1 - endRate;
		sb.append(1).append(";");
		return sb.toString();
	}
	
	String fireRichTianleigungun(float reduceRate, float endRate)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("tianleigungun;");
		rich.thiefDefenseDownRate = reduceRate;
		rich.thiefDefenseDownEndRate += reduceRate;
		if (rich.thiefDefenseDownEndRate > 1 - endRate)
			rich.thiefDefenseDownEndRate = 1 - endRate;
		sb.append(1).append(";");
		return sb.toString();
	}
	
	String fireRichKillYou(int index, TLogger.CommonFlowRecord commonFlowRecord)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("killyou;");
		sb.append(1).append(";");
		StringBuilder dropsSB = new StringBuilder();  
		if (index >= 0 && index < rich.map.buildings.size())
		{
			SBean.DBRichMapBuilding b = rich.map.buildings.get(index);
			if (b.type == RichManager.MAP_TYPE_MINE || b.type == RichManager.MAP_TYPE_RICH_MINE)
			{
				if (b.status < 10)
				{
					int combatId = b.params.get(1);
					SBean.CombatCFGS cfg = gs.gameData.getCombatCFG((short)combatId);
					if (cfg != null)
					{
						GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, (byte)1, (byte)1, (byte)1, (byte)1, null, null, null, (byte)0); 
						richCombatBonus = bonusInfo.cb;
						int reward = b.params.get(2);
						if (richCombatBonus != null && reward > 0)
							richCombatBonus.entryIDs.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_RICH_POINT, (short)0, reward));
						if (richCombatBonus != null)
						{
							dropsSB.append(richCombatBonus.entryIDs.size()).append(";");
							for (SBean.DropEntryNew d : richCombatBonus.entryIDs)
							{
								commonFlowRecord.addChange(addDropNew(d));
								dropsSB.append(d.type).append(";");
								dropsSB.append(d.id).append(";");
								dropsSB.append(d.arg).append(";");
							}
						}
					}
					b.status = 101;
					logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_SHANZEI, 1);
				}
			}
		}
		sb.append(index+1).append(";");
		if (dropsSB.toString().isEmpty())
			dropsSB.append(0).append(";");
		sb.append(dropsSB.toString());
		return sb.toString();
	}
	
	int getRichMineBuildingIndex()
	{
		int index = 0;
		for (SBean.DBRichMapBuilding b : rich.map.buildings) 
		{
			if (b.type == RichManager.MAP_TYPE_MINE || b.type == RichManager.MAP_TYPE_RICH_MINE) 
			{
				if (b.status < 10) 
				{
					return index;
				}
			}
			index++;
		}
		return -1;
	}
	
	String fireRichTiannvsanhua(short dropTblID)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("tiannvsanhua;");
		final short tiannvsanhuaDropTblID = dropTblID;
		if (forceInfo.id > 0)
		{
			gs.getForceManager().getForceMembersRoleID(forceInfo.id, new ForceManager.QueryForceMembersCallback() 
			{
				@Override
				public void onCallback(boolean success, int headID, List<Integer> forceMembers) 
				{
					if (success)
					{
						for (int rid : forceMembers)
						{
							List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
							SBean.DropEntryNew drop = createRandomRichItem(tiannvsanhuaDropTblID);
							if (drop != null)
							{
								attLst.add(drop);
								gs.getLoginManager().sysSendMessage(0, rid, DBMail.Message.SUB_TYPE_RICH_TIANNVSANHUA_GIFT, "", "" + Role.this.name, 0, true, attLst);	
							}
							
						}	
					}
				}
			}, 0);
			
		}
		else
		{
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			SBean.DropEntryNew drop = createRandomRichItem(tiannvsanhuaDropTblID);
			if (drop != null)
			{
				attLst.add(drop);
				gs.getLoginManager().sysSendMessage(0, Role.this.id, DBMail.Message.SUB_TYPE_RICH_TIANNVSANHUA_GIFT, "", "" + Role.this.name, 0, true, attLst);	
			}
		}
		sb.append(1).append(";");
		return sb.toString();
	}
	
	public boolean useOneObjectCard(int objectType, SBean.RichCFGS cfg, int[] arg, TLogger.CommonFlowRecord commonFlowRecord)
	{
		if (objectType <= 0 && objectType > cfg.basic.objects.size())
			return false;
		SBean.RichObjectCFGS objectcfg = cfg.basic.objects.get(objectType-1);
		if (objectcfg.itemID <= 0)
			return false;
		SBean.ItemCFGS itemcfg = gs.getGameData().getItemCFG(objectcfg.itemID);
		if (itemcfg == null)
			return false;
		arg[0] = itemcfg.arg1;
		int cardCnt = getItemCount(objectcfg.itemID);
		if (cardCnt <= 0)
			return false;
		commonFlowRecord.addChange(this.delItem(objectcfg.itemID, 1));
		logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_USE_CARD_ANY, 1);
		return true;
	}
	
	public boolean useRemoteDice(int point, SBean.RichCFGS cfg, TLogger.CommonFlowRecord commonFlowRecord)
	{
		int diceTypeCnt = cfg.basic.remoteDiceMaxItemID - cfg.basic.remoteDiceMinItemID + 1;
		if (point <= 0 || point > diceTypeCnt)
			return false;
		short cardID = (short)(cfg.basic.remoteDiceMinItemID + point - 1);
		int cardCnt = getItemCount(cardID);
		if (cardCnt <= 0)
			return false;
		commonFlowRecord.addChange(this.delItem(cardID, 1));
		logRichDailyTask(GameData.RICH_DAILYTASK_TYPE_USE_CARD_ANY, 1);
		return true;
	}
	
	public synchronized List<SBean.DBRichTech> getRichTechTree()
	{
		List<SBean.DBRichTech> techtree = new ArrayList<SBean.DBRichTech>();
		if (rich == null)
			return techtree;
		for (SBean.DBRichTech tech : rich.techTree)
		{
			techtree.add(tech.kdClone());
		}
		return techtree;
	}
	
	public boolean upgradeRichTechTree(int id)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			if (rich == null)
				return false;
			if (id <= 0 || id > rich.techTree.size())
				return false;
			SBean.DBRichTech tech = rich.techTree.get(id-1);
			if (tech.id != id)
				return false;
			if (id != 1)
			{
				SBean.DBRichTech cityTech = rich.techTree.get(0);
				if (tech.level >= cityTech.level)
					return false;
			}
			SBean.RichCFGS richcfgs = GameData.getInstance().getRichCFG();
			SBean.RichTechCFG cfg = richcfgs.techTree.get(id-1);
			if (tech.level >= cfg.lvlMax)
				return false;
			if (tech.level < 0 || tech.level >= cfg.lvlupReq.size())
				return false;
			int cost = cfg.lvlupReq.get(tech.level);
			if (rich.gold < cost)
				return false;
			commonFlowRecord.addChange(richDelGold(cost));
			tech.level++;
			if (id == GameData.RICH_TECH_TYPE_RICH_REWARD) {
				int gold = rich.goldTotal;
				if (lvlVIP >= 15 && gold < rich.goldHistory)
					gold = rich.goldHistory;
				gs.getRichManager().setGold(gold, this.id, name, headIconID, lvl, (int)getRichTechPlusEffect(GameData.RICH_TECH_TYPE_RICH_REWARD));
			}
			
			commonFlowRecord.setEvent(TLog.AT_RICH_TECHTREE);
			commonFlowRecord.setArg(id, tech.level);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	float getRichTechPlusEffect(int type)
	{
		SBean.RichCFGS richcfgs = GameData.getInstance().getRichCFG();
		SBean.RichTechCFG cfg = richcfgs.techTree.get(type-1);
		SBean.DBRichTech tech = rich.techTree.get(type-1);
		return cfg.initEffect + cfg.increaseEffect*(tech.level-cfg.lvlInit);
	}
	
	public synchronized List<SBean.DBRichDailyTask> getRichDailyTask()
	{
		List<SBean.DBRichDailyTask> tasks = new ArrayList<SBean.DBRichDailyTask>();
		if (rich == null)
			return tasks;
		for (SBean.DBRichDailyTask e : rich.dailyTask)
		{
			tasks.add(e.kdClone());
		}
		return tasks;
	}
	
	void logRichDailyTask(int type, int add)
	{
		if (rich == null)
			return;
		SBean.RichCFGS richcfgs = GameData.getInstance().getRichCFG();
		for (SBean.DBRichDailyTask e : rich.dailyTask)
		{
			SBean.RichDailyTaskCFG cfg = richcfgs.dailyTask.get(e.id-1);
			if (cfg.type == type)
			{
				e.reqCurVal += add;
			}
		}
		rich.dailyTaskNotice = 1;
	}
	
	public List<SBean.DropEntry> takeRichDailyTaskReward(int id)
	{
		List<SBean.DropEntry> drops = new ArrayList<SBean.DropEntry>();
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this)
		{
			if (rich == null)
				return null;
			if (id <= 0 || id > rich.dailyTask.size())
				return null;
			SBean.DBRichDailyTask task = rich.dailyTask.get(id-1);
			if (task.id != id || task.reward != 0)
				return null;
			SBean.RichCFGS richcfgs = GameData.getInstance().getRichCFG();
			SBean.RichDailyTaskCFG cfg = richcfgs.dailyTask.get(id-1);
			if (task.reqCurVal < cfg.reqArg)
				return null;
			for (int i = 0; i < cfg.rewardDropTimes; ++i)
			{
				drops.add(GameData.getInstance().getDropTableRandomEntry(cfg.rewardDropTblID));
			}
			commonFlowRecord.addChanges(addDrops(drops));
			task.reward = 1;
		}
		commonFlowRecord.setEvent(TLog.AT_RICH_DAILYTASK);
		commonFlowRecord.setArg(id);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return drops;
	}
	
	public synchronized boolean canTakeRichDailyTaskReward()
	{
		if (rich == null)
			return false;
		rich.dailyTaskNotice = 0;
		SBean.RichCFGS richcfgs = GameData.getInstance().getRichCFG();
		List<DBRichDailyTask> tasks = rich.dailyTask;
		for (SBean.DBRichDailyTask task : tasks)
		{
			SBean.RichDailyTaskCFG cfg = richcfgs.dailyTask.get(task.id-1);
			if (task.reqCurVal >= cfg.reqArg && task.reward == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean needTestRichDailyTaskNotice()
	{
		return rich != null && rich.dailyTaskNotice != 0;
	}
	
	public void testRichDailyTaskNotice()
	{
		boolean bOK = canTakeRichDailyTaskReward();
		gs.getRPCManager().notifyRichTaskCanReward(netsid, bOK);
	}
	
	public void setForceThiefDamage(int index, List<Short> gids, int damage)
	{
		List<DBRoleGeneral> generals = null;
		
		synchronized (this) {
			generals = copyGeneralsWithoutLock(gids);
			if (generals == null || generals.isEmpty())
				return;
		}
			
		if (damage > 0)
			gs.getWarManager().setDamage(index, damage, forceInfo.id, id, name, headIconID, lvl, generals);
	}
	
	public void setForceChapterDamage(byte chapter, List<Short> gids, int damage)
	{
		List<DBRoleGeneral> generals = null;
		
		synchronized (this) {
			generals = copyGeneralsWithoutLock(gids);
			if (generals == null || generals.isEmpty())
				return;
		}
			
		if (damage > 0)
			if (gs.getForceManager().setDungeonDamage(chapter, damage, forceInfo.id, id, forceInfo.name, name, headIconID, lvl, generals)) {
				int stone = 0;
				for (SBean.DungeonBeyondRewardCFGS rcfg : gs.gameData.dungeon.beyondRewards)
					if (rcfg.chapter == chapter+1)
						stone = rcfg.stone;
				if (stone > 0) {
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					SBean.DropEntryNew drop = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, stone);
					attLst.add(drop);
					gs.getLoginManager().sysSendMessage(0, id, DBMail.Message.SUB_TYPE_DUNGEON_BEYOND_REWARD, "", "" + (chapter+1), 0, true, attLst);
				}
			}
	}
	
	public static boolean isPetDeformed(byte deformStage)
	{
		return deformStage >= 50;
	}
	
	private static SBean.PetDeformCFGS getDeformStage(SBean.DBPetDeform deform, GameServer gs)
	{
		SBean.PetCFGS cfg = gs.getGameData().getPetCFG(deform.id, isPetDeformed(deform.deformStage));
		if (cfg == null)
			return null;
		
		int stage = deform.deformStage - 10;
		if (stage < 0 || stage >= cfg.deform.size())
			return null;
		
		SBean.PetDeformCFGS dcfg = cfg.deform.get(stage);
		if (dcfg == null)
			return null;
		
		return dcfg;
	}
	
	private static void refreshDeformFeedItems(SBean.DBPetDeform deform, GameServer gs)
	{
		SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
		if (dcfg == null || dcfg.type != 1) {
			return;
		}
		
		deform.drops = new ArrayList<SBean.DropEntry>();
		for (short drop : dcfg.drops) {
			SBean.DropEntry entry = gs.getGameData().getDropTableRandomEntry(drop);
			if (entry != null)
				deform.drops.add(entry.kdClone());
		}
	}
	
	private static void initDeformStage(SBean.DBPetDeform deform, GameServer gs)
	{
		SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
		if (dcfg == null) {
			deform.deformStage = 0;
			return;
		}
		
		switch (dcfg.type) {
		case 1:
			refreshDeformFeedItems(deform, gs);
			break;
		}
	}
	
	private static void checkDeformHappiness(SBean.DBPetDeform deform, GameServer gs)
	{
		SBean.PetCmnCFGS pcfg = gs.getGameData().getPetCmnCFG();
		
		if (pcfg != null && deform.deformStage == 0 && deform.happiness >= pcfg.deformHappiness) {
			deform.deformStage = 10;
			initDeformStage(deform, gs);
		}
	}
	
	private static void checkDeformGrowth(SBean.DBPet pet, SBean.DBPetDeform deform, GameServer gs)
	{
		SBean.PetCFGS cfg = gs.getGameData().getPetCFG(deform.id, isPetDeformed(deform.deformStage));
		if (cfg == null)
			return;
		
		SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
		if (dcfg == null)
			return;
		
		if (deform.growth >= dcfg.growth) {
			deform.growth = 0;
			deform.feedTime = 0;
			deform.zanTimes = 0;
			deform.tryTimes = 0;
			deform.feedTimes = 0;
			deform.deformStage ++;
			if (deform.deformStage - 10 >= cfg.deform.size()) {
				deform.deformStage = 50;
				pet.growthRate = 120;
			}
			else
				initDeformStage(deform, gs);
		}
	}
	
	public void petsDeformSync(int sid)
	{
		List<SBean.DBPetDeform> deforms = null;
		Map<Short, Byte> awakeLvlMap = new HashMap<Short, Byte>();
		List<SBean.DBPet> pets = new ArrayList<SBean.DBPet>();
		byte[] takeTimes = {0};
		synchronized (this) {
			deforms = copyPetDeformsWithoutLock(false, takeTimes);
			pets = copyPetsWithoutLock();
		}
		
		for (SBean.DBPet p : pets) {
            awakeLvlMap.put(p.id, p.awakeLvl);
        }
		if (deforms != null)
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformSyncRes(deforms, activity, takeTimes[0], awakeLvlMap));			
	}
	
	public void petsDeformFeed(int sid, short pid, byte itemIndex)
	{
		SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-1, (short)0, null));
			return;
		}
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		int index = itemIndex - 1;
		synchronized (this) {
			Pet pet = pets.get(pid);
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if (pet == null || deform == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-2, (short)0, null));
				return;
			}
		
			SBean.PetCFGS cfg = gs.getGameData().getPetCFG(pid, isPetDeformed(deform.deformStage));
			if (cfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-1, (short)0, null));
				return;
			}
			
			SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
			if (dcfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-3, (short)0, null));
				return;
			}
			
			if (dcfg.type != 1) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-4, (short)0, null));
				return;				
			}
			
			dayRefresh();
			if (deform.feedTime + 300 > gs.getTime()) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-5, (short)0, null));
				return;				
			}
			
			if (deform.feedTimes > ccfg.deformFeedTimes) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-6, (short)0, null));
				return;				
			}
			
			if (index < 0 || index >= deform.drops.size() || index >= ccfg.deformFeedGrowth.size()) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-7, (short)0, null));
				return;
			}
			
			SBean.DropEntry drop = deform.drops.get(index);
			if (!haveDrop(drop)) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, (byte)-8, (short)0, null));
				return;
			}
			
			commonFlowRecord.addChange(delDrop(drop));
			deform.growth += ccfg.deformFeedGrowth.get(index);
			deform.feedTime = gs.getTime();
			deform.feedTimes ++;
			checkDeformGrowth(pet, deform, gs);
			refreshDeformFeedItems(deform, gs);
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformFeedRes(pid, itemIndex, deform.deformStage, deform.growth, deform.drops));
			
			commonFlowRecord.setEvent(TLog.AT_DEFORM_PET_FEED);
			commonFlowRecord.setArg(pid, itemIndex);
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
		}
	}
	
	public void petsDeformZan(int sid, short pid)
	{
		SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-1, (short)0));
			return;
		}
		
		synchronized (this) {
			Pet pet = pets.get(pid);
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if (pet == null || deform == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-2, (short)0));
				return;
			}
		
			SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
			if (dcfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-3, (short)0));
				return;
			}
			
			if (dcfg.type != 2) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-4, (short)0));
				return;				
			}
			
			dayRefresh();
			if (deform.zanTimes >= ccfg.deformZanTimes) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-5, (short)0));
				return;
			}
			int zanVit = 0;
			if (deform.zanTimes >= ccfg.deformZanVit.size())
				zanVit = ccfg.deformZanVit.get(ccfg.deformZanVit.size()-1);
			else
				zanVit = ccfg.deformZanVit.get(deform.zanTimes);
			if (zanVit > activity) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, (byte)-5, (short)0));
				return;
			}
			deform.zanTimes ++;
			
			deform.growth += ccfg.deformZanGrowth;
			checkDeformGrowth(pet, deform, gs);
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanRes(pid, deform.deformStage, deform.growth));
		}
	}
	
	public void petsDeformZanOffer(int sid, short pid, short reward, short count, String message)
	{
		SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-1));
			return;
		}
		
		if (message == null)
			message = "";
		int cost = reward * count;
		SBean.DBPetDeform deform = null;
		
		//TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			deform = petDeforms.get(pid);
			if (deform == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-2));
				return;
			}
		
			SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
			if (dcfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-3));
				return;
			}
			
			if (dcfg.type != 2) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-4));
				return;				
			}
			
			if (deform.zanOfferTime + 300 > gs.getTime()) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-5));
				return;				
			}
			
			if (stone < cost) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-6));
				return;
			}
		}
			
		if (!gs.getZanManager().zanOffer(this, pid, reward, count, message)) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)-7));
			return;
		}
			
		synchronized (this) {
			deform.zanOfferTime = gs.getTime();
			useStone(cost, false);
			// DO NOT RECORD COST HERE!!!
			//commonFlowRecord.addChange(useStone(cost));
		}
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformZanOfferRes(pid, reward, count, (byte)0));
		
		/*
		commonFlowRecord.setEvent(TLog.AT_DEFORM_PET_BEG_LIKE);
		commonFlowRecord.setArg(pid, reward, count);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		*/
	}
	
	public void petsDeformTakeOffer(int sid, int rid, short pid, short reward)
	{
		final int sessionId = sid;
		final int roleId = rid;
		final short petId = pid;
		final SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTakeOfferRes(rid, "", pid, (short)0, 0, (short)0, (short)0, 0, 0));
			return;
		}
		
		SBean.DBPetDeform deform = null;
		int zanVit = 0;
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			deform = petDeforms.get(VIRT_PET_ID);
			if (deform == null) {
				deform = new SBean.DBPetDeform(VIRT_PET_ID, (byte)0, 0, (byte)0, 0, (byte)0, (byte)0, 0, (byte)0, new ArrayList<DropEntry>(), (byte)0, (byte)0, 0, 0, 0);
				petDeforms.put(VIRT_PET_ID, deform);
			}
		
			dayRefresh();
			if (deform.takeTimes >= ccfg.deformTakeOfferTimes) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTakeOfferRes(rid, "", pid, (short)0, 0, (short)0, (short)0, 0, 0));
				return;
			}
			if (deform.takeTimes >= ccfg.deformTakeOfferVit.size())
				zanVit = ccfg.deformTakeOfferVit.get(ccfg.deformTakeOfferVit.size()-1);
			else
				zanVit = ccfg.deformTakeOfferVit.get(deform.takeTimes);
			/*
			if (zanVit > activity) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTakeOfferRes(rid, "", pid, (short)-4, 0, (short)0, (short)0));
				return;
			}
			*/
		}
		
		final int vipTimes[] = {0, 0};
		final short take = gs.getZanManager().takeOffer(id, rid, pid, reward, zanVit, activity, vipTimes);
		
		synchronized (this) {
			if (take > 0) {
				deform.totalReward += take;
				final int totalReward = deform.totalReward;
				deform.takeTimes ++;
				commonFlowRecord.addChange(addStonePresent(take));
				gs.getLoginManager().execCommonRoleVisitor(rid,
						new LoginManager.CommonRoleVisitor() {

							@Override
							public byte visit(DBRole dbrole) {
								SBean.DBPet pet = null;
								SBean.DBPetDeform deform = null;
								for (SBean.DBPet p : dbrole.pets)
									if (p.id == petId)
										pet = p;
								for (SBean.DBPetDeform p : dbrole.petDeforms)
									if (p.id == petId)
										deform = p;
								if (pet == null || deform == null)
									return LoginManager.CommonRoleVisitor.ERR_DB_FAILED;
							
								deform.growth += ccfg.deformTakeOfferGrowth;
								checkDeformGrowth(pet, deform, gs);
								
								SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
								if (dcfg != null) 
									gs.getRPCManager().sendLuaPacket(sessionId, LuaPacket.encodePetsDeformTakeOfferRes(roleId, dbrole.name, petId, take, totalReward, deform.growth, dcfg.growth, vipTimes[0], vipTimes[1]));
								return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
							}

							@Override
							public boolean visit(Role role) {
								Pet pet = role.pets.get(petId);
								SBean.DBPetDeform deform = role.petDeforms.get(petId);
								if (pet == null || deform == null)
									return false;
							
								deform.growth += ccfg.deformTakeOfferGrowth;
								checkDeformGrowth(pet, deform, gs);
								SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
								if (dcfg != null) 
									gs.getRPCManager().sendLuaPacket(sessionId, LuaPacket.encodePetsDeformTakeOfferRes(roleId, role.name, petId, take, totalReward, deform.growth, dcfg.growth, vipTimes[0], vipTimes[1]));
								return true;
							}

							@Override
							public void onCallback(boolean bDB, byte errcode,
									String rname) {
							}
						});
			}
			else
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTakeOfferRes(rid, "", pid, take, 0, (short)0, (short)0, vipTimes[0], vipTimes[1]));
		}
		
		commonFlowRecord.setEvent(TLog.AT_DEFORM_PET_LIKE);
		commonFlowRecord.setArg(rid, pid, reward);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void petsDeformTry(int sid, short pid)
	{
		SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-1, (short)0));
			return;
		}
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		int trytime = 0;
		synchronized (this) {
			Pet pet = pets.get(pid);
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if (pet == null || deform == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-2, (short)0));
				return;
			}
		
			SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
			if (dcfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-3, (short)0));
				return;
			}
			
			if (dcfg.type != 3) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-4, (short)0));
				return;				
			}
			
			if (getItemCount(ccfg.deformTryItemId) <= 0) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-5, (short)0));
				return;
			}
			
			dayRefresh();
			if (deform.tryTimes >= ccfg.deformFreeTryTimes * (1 + deform.buyTryTimes)) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)-6, (short)0));
				return;
			}
			commonFlowRecord.addChange(delItem(ccfg.deformTryItemId, 1));
			deform.tryTimes ++;
			trytime = deform.tryTimes;
			if (gs.getGameData().getRandInt(1, 101) > ccfg.deformTryPoss) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, (byte)0, (short)0));
				return;
			}
			
			deform.growth += dcfg.growth;
			checkDeformGrowth(pet, deform, gs);
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformTryRes(pid, deform.deformStage, deform.growth));
		}
		
		commonFlowRecord.setEvent(TLog.AT_DEFORM_PET_TRY_UPGRADE);
		commonFlowRecord.setArg(pid, trytime);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}
	
	public void petsDeformBuyTry(int sid, short pid)
	{
		SBean.PetCmnCFGS ccfg = gs.getGameData().getPetCmnCFG();
		if (ccfg == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-1));
			return;
		}
		
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		int buytrytimes = 0;
		synchronized (this) {
			SBean.DBPetDeform deform = petDeforms.get(pid);
			if (deform == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-2));
				return;
			}
		
			SBean.PetDeformCFGS dcfg = getDeformStage(deform, gs);
			if (dcfg == null) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-3));
				return;
			}
			
			if (dcfg.type != 3) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-4));
				return;				
			}
			
			dayRefresh();
			if (deform.buyTryTimes >= ccfg.deformBuyTryTimes) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-5));
				return;
			}
			
			if (stone < ccfg.deformBuyTryStone) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, (byte)-6));
				return;
			}
			deform.buyTryTimes ++;
			commonFlowRecord.addChange(useStone(ccfg.deformBuyTryStone));
			buytrytimes = deform.buyTryTimes;
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodePetsDeformBuyTryRes(pid, deform.buyTryTimes));
		}
		
		commonFlowRecord.setEvent(TLog.AT_DEFORM_PET_BUY_TRY_TIMES);
		commonFlowRecord.setArg(pid, buytrytimes);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
	}

	public byte petsAwake(short pid) 
	{
		byte result = 0;
		SBean.PetAwakeCFGS petAwakeCFGS = gs.gameData.getPetCFG(pid, false).awake;
		if (petAwakeCFGS == null) {
			return result;
		}
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized (this) {
			byte nowAwakeLevel = 0;
			Pet p = pets.get(pid);
			if (p != null) {
				nowAwakeLevel = p.awakeLvl;
			} else {
			    return 0;
			}
			byte nextAwakeLevel = (byte)(nowAwakeLevel + 1);
			int awakeSkill = -1;
			switch(nextAwakeLevel)
			{
				case 1:
					awakeSkill = petAwakeCFGS.awakeSkill1;
					break;
				case 2:
					awakeSkill = petAwakeCFGS.awakeSkill2;
					break;
				case 3:
					awakeSkill = petAwakeCFGS.awakeSkill3;
					break;
				case 4:
					awakeSkill = petAwakeCFGS.awakeSkill4;
					break;
				case 5:
					awakeSkill = petAwakeCFGS.awakeSkill5;
					break;
				default:
					break;
			}
			if (awakeSkill <= -1) 
				return 2;
			if (nextAwakeLevel == (byte)1) 
			{
			    for (DropEntryNew den : petAwakeCFGS.needItem1) {
			        int countNow = getItemCount(den.id);
			        if (den.arg > countNow)
			            return 3;
                }
			}
			if (nextAwakeLevel == (byte)2) 
			{
			    
			    for (DropEntryNew den : petAwakeCFGS.needItem2) {
                    int countNow = getItemCount(den.id);
                    if (den.arg > countNow)
                        return 3;
                }
			}
			if (nextAwakeLevel == (byte)3) 
			{
			    
			    for (DropEntryNew den : petAwakeCFGS.needItem3) {
			        int countNow = getItemCount(den.id);
			        if (den.arg > countNow)
			            return 3;
			    }
			}
			if (nextAwakeLevel == (byte)4) 
			{
			    
			    for (DropEntryNew den : petAwakeCFGS.needItem4) {
			        int countNow = getItemCount(den.id);
			        if (den.arg > countNow)
			            return 3;
			    }
			}
			if (nextAwakeLevel == (byte)5) 
			{
			    
			    for (DropEntryNew den : petAwakeCFGS.needItem5) {
			        int countNow = getItemCount(den.id);
			        if (den.arg > countNow)
			            return 3;
			    }
			}
			
			if (nextAwakeLevel == (byte)1) {
			    for (DropEntryNew den : petAwakeCFGS.needItem1) {
			        commonFlowRecord.addChange(delItem(den.id, den.arg));
                }
			}
			if (nextAwakeLevel == (byte)2) {
			    for (DropEntryNew den : petAwakeCFGS.needItem2) {
			        commonFlowRecord.addChange(delItem(den.id, den.arg));
			    }
			}
			if (nextAwakeLevel == (byte)3) {
			    for (DropEntryNew den : petAwakeCFGS.needItem3) {
			        commonFlowRecord.addChange(delItem(den.id, den.arg));
			    }
			}
			if (nextAwakeLevel == (byte)4) {
			    for (DropEntryNew den : petAwakeCFGS.needItem4) {
			        commonFlowRecord.addChange(delItem(den.id, den.arg));
			    }
			}
			if (nextAwakeLevel == (byte)5) {
			    for (DropEntryNew den : petAwakeCFGS.needItem5) {
			        commonFlowRecord.addChange(delItem(den.id, den.arg));
			    }
			}
			p.awakeLvl = nextAwakeLevel;
			result = 1;
		}
		commonFlowRecord.setEvent(TLog.AT_PET_AWAKE);
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return result;
	}
	
	public List<SBean.DBRelation> getRelations()
	{
		List<SBean.DBRelation> rels = new ArrayList<SBean.DBRelation>();
		for (SBean.DBRelation r : relations.values())
			rels.add(r.kdClone());
		return rels;
	}
	
	public List<SBean.DBGeneralStone> getGeneralStones()
	{
		List<SBean.DBGeneralStone> rels = new ArrayList<SBean.DBGeneralStone>();
		for (SBean.DBGeneralStone r : generalStone.values())
			rels.add(r.kdClone());
		return rels;
	}
	
	public List<SBean.DBGeneralStone> getGeneralStones(int gid)
	{
		List<SBean.DBGeneralStone> rels = new ArrayList<SBean.DBGeneralStone>();
		for (SBean.DBGeneralStone s : generalStone.values()){
			if(s.gid!=gid){
				continue;
			}
			rels.add(s.kdClone());
		}
		return rels;
	}
	
	public List<SBean.DBGeneralStone> getActiveGeneralStones()
	{
		List<SBean.DBGeneralStone> rels = new ArrayList<SBean.DBGeneralStone>();
		for (SBean.DBGeneralStone s : generalStone.values()){
			if(s.gid==0){
				continue;
			}
			rels.add(s.kdClone());
		}
			
		return rels;
	}
	
	public synchronized void giveBonus(short sid,short combatId,short rank,Role role)
	{
		ExpiratBossItemsCFGS bonus =gs.gameData.getExpiratBossBonusCFG(sid,rank,combatId);
		
		if(bonus==null){
			bonus =gs.gameData.getExpiratBossBonusCFG(sid,rank,combatId);
		}
		
		DBExpiratBossTimes dbExpiratBossTimes = null;
		if(role.expiratBossInfo==null){
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,null));
			return ;
		}
			
		for(DBExpiratBossTimes i:role.expiratBossInfo.times){
			if(combatId==i.id){
				dbExpiratBossTimes=i;
			}
		}
		
		if(bonus!=null){
			int day = gs.getDayByOffset((short) 1);
			dbExpiratBossTimes.bonusday=day;
			
			for(DropEntryNew item : bonus.eItems){			
				addDropNew(item);			
			}			
		}
	}
	
	
	
	public boolean canCityNotice(){
		
		if(this.citys!=null){
			syncCitys();
			List<CityBrief> cityInfo = getBaseCitysBrief();
			for (CityManager.CityBrief e : cityInfo)
			{
				if(e.baseRoleID!=e.ownerRoleID){
					return true;
				}
			}
			
			if(this.canTakeOccupyCitysReward()){
				return true;
			}
		}	
			
		return false;
	}
	
	
	public  boolean generalsActiveStone(int itemId,short gid,byte pos, DBGeneralStone gstoneTemp)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		
		if(!haveGeneral(gid)){
			return false;
		}
		
		if(pos<0||pos>4){
			return false;
		}
		
		GeneralStonePosCFGS rcfgPos = GameData.getInstance().getGeneralStonePosCFG(gid);
		
		if(rcfgPos==null){
			return false;
		}
				
		synchronized(this){
			
			DBGeneralStone stoneInfo = generalStone.get(itemId);
			
			if(stoneInfo==null){
				
				if(this.getItemCount((short) itemId)<=0){
					return false;
				}
								
				if(!checkStateAndPos(itemId, gid, pos, rcfgPos)){
					return false;
				}
				
				commonFlowRecord.addChange(delItem((short) itemId, 1));
				
				if(generalStoneInfo==null){
					generalStoneInfo = new SBean.DBGeneralStoneInfo();
				}
				if(generalStoneInfo.stoneId<=0){
					generalStoneInfo.stoneId=800000;
				}
				generalStoneInfo.stoneId+=1;
				DBGeneralStone stone = new  DBGeneralStone();
				stone.id=generalStoneInfo.stoneId;
				stone.itemId=itemId;
				stone.gid=gid;
				stone.pos=pos;
				stone.passes= new ArrayList<SBean.DBGeneralStoneProp>();
				stone.resetPasses= new ArrayList<SBean.DBGeneralStoneProp>();
				generalStone.put(stone.id,stone);
				
				gstoneTemp.itemId=stone.id;
				
			}else{
				if(!checkStateAndPos(stoneInfo.itemId, gid, pos, rcfgPos)){
					return false;
				}
				stoneInfo.gid=gid;
				stoneInfo.pos=pos;
			}
//			commonFlowRecord.setEvent(TLog.AT_GENERAL_STONE_ACTIVE_STONE);
			commonFlowRecord.setArg(itemId, pos, generalStoneInfo.stoneId,gid );			
			tlogEvent.addCommonFlow(commonFlowRecord);
		}

//		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}

	private boolean checkStateAndPos(int itemId, short gid, byte pos,
			GeneralStonePosCFGS rcfgPos) {
		GeneralStoneBasicCFGS rcfgBasic = GameData.getInstance().getGeneralStoneBasicCFG((short) itemId);
		if(rcfgBasic==null||(rcfgBasic.specialgid!=-1&&rcfgBasic.specialgid!=gid)){
			return false;
		}
		
		switch(pos){
		  
			case 1:
				if(rcfgPos.state1==0||rcfgPos.posType1!=rcfgBasic.stype){
					return false;
				}
				break;
			case 2:
				if(rcfgPos.state2==0||rcfgPos.posType2!=rcfgBasic.stype){
					return false;
				}
				break;
			case 3:
				if(rcfgPos.state3==0||rcfgPos.posType3!=rcfgBasic.stype){
					return false;
				}
				break;
			case 4:
				if(rcfgPos.state4==0||rcfgPos.posType4!=rcfgBasic.stype){
					return false;
				}
				break;
			default:
				return false;
		}
		return true;
	}
	
	public boolean generalsRemoveStone(int itemId)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized(this){
			DBGeneralStone stoneInfo = generalStone.get(itemId);
			if(stoneInfo==null){
				return false;
			}
			
			if(stoneInfo.passes.size()>0||stoneInfo.exp>0){
				stoneInfo.gid=0;
				stoneInfo.pos=0;
			}else{
				
				commonFlowRecord.addChange(addItem((short) stoneInfo.itemId, 1));
				generalStone.remove(itemId);
//				commonFlowRecord.setEvent(TLog.AT_GENERAL_STONE_REMOVE_STONE);
				commonFlowRecord.setArg(itemId, stoneInfo.itemId);			
				tlogEvent.addCommonFlow(commonFlowRecord);
			}
			
		}
//		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean generalsStoneAddExp(int itemId, List<SBean.DropEntryNew> matList,List<Integer> itemList )
	{
	
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		
		synchronized(this){
			DBGeneralStone stoneInfo = generalStone.get(itemId);
			if(stoneInfo==null){
				return false;
			}
			
			GeneralStoneBasicCFGS rcfg2 = GameData.getInstance().getGeneralStoneBasicCFG((short) (stoneInfo.itemId));
			if(rcfg2==null||rcfg2.upIid<=0){
				return false;
			}
			
			Set<Short> itemIDs = new TreeSet<Short>();
			Set<Short> equipIDs = new TreeSet<Short>();
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					if( itemIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getItemCount(e.id) < e.arg )
						return false;
					GeneralStoneBasicCFGS icfg = GameData.getInstance().getGeneralStoneBasicCFG(e.id);
					if( icfg == null || icfg.costExp < 0 )
						return false;

					if(rcfg2.specialgid>0&&rcfg2.specialgid!=icfg.specialgid){
						return false;
					}
					if(rcfg2.specialgid<0&&icfg.specialgid>0){
						return false;
					}
					if(rcfg2.group!=icfg.group){
						return false;
					}
					itemIDs.add(e.id);
				}
				else
				{
					if( equipIDs.contains(e.id) )
						return false;
					if( e.arg <= 0 || getEquipCount(e.id) < e.arg )
						return false;
					GeneralStoneBasicCFGS ecfg2 = GameData.getInstance().getGeneralStoneBasicCFG(e.id);
					if( ecfg2 == null || ecfg2.costExp < 0 )
						return false;
					
					if(rcfg2.specialgid>0&&rcfg2.specialgid!=ecfg2.specialgid){
						return false;
					}
					if(rcfg2.specialgid<0&&ecfg2.specialgid>0){
						return false;
					}
					if(rcfg2.group!=ecfg2.group){
						return false;
					}
					equipIDs.add(e.id);
				}
			}
			
			for(int iid:itemList){
				DBGeneralStone stoneInfol = generalStone.get(iid);
				if(stoneInfol==null||stoneInfol.gid>0){
					return false;
				}
				
				GeneralStoneBasicCFGS rcfgl = GameData.getInstance().getGeneralStoneBasicCFG((short) (stoneInfol.itemId));
				if(rcfgl==null){
					return false;
				}
				if(rcfg2.specialgid>0&&rcfg2.specialgid!=rcfgl.specialgid){
					return false;
				}
				if(rcfg2.specialgid<0&&rcfgl.specialgid>0){
					return false;
				}
				if(rcfg2.group!=rcfgl.group){
					return false;
				}
			}
			
			int sum = 0;
			int moneyNeed = 0;
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					GeneralStoneBasicCFGS icfg = GameData.getInstance().getGeneralStoneBasicCFG(e.id);				
					sum += icfg.costExp * e.arg;
					moneyNeed += icfg.upCost* e.arg;
				}
				else
				{
					GeneralStoneBasicCFGS icfg = GameData.getInstance().getGeneralStoneBasicCFG(e.id);
					sum += icfg.costExp * e.arg;
					moneyNeed += icfg.upCost* e.arg;
				}
			}
			
			for(int iid:itemList){
				DBGeneralStone stoneInfol = generalStone.get(iid);
				GeneralStoneBasicCFGS icfg = GameData.getInstance().getGeneralStoneBasicCFG((short) stoneInfol.itemId);
				sum += icfg.costExp;
				moneyNeed += icfg.upCost;
			}
			
			
			
			boolean bUP = false;
			
			short itemidTemp = (short)stoneInfo.itemId;
			int expTemp = stoneInfo.exp;
			
			while( sum > 0 )
			{
				GeneralStoneBasicCFGS rcfg = GameData.getInstance().getGeneralStoneBasicCFG(itemidTemp);
				if(rcfg==null){
					break;
				}
				int expUp = rcfg.exp;
				int expNeed = expUp - expTemp;//stoneInfo.exp;
				if( expNeed < 0 )
					expNeed = 0;
				if( sum < expNeed )
				{
					expTemp += sum;//stoneInfo.exp += sum;
//					moneyNeed += 0;
					break;
				}
				
				itemidTemp=rcfg.upIid; //stoneInfo.itemId=rcfg.upIid;
				bUP = true;
				expTemp = 0; //stoneInfo.exp = 0;
				sum -= expNeed;				
				GeneralStoneBasicCFGS rcfg1 = GameData.getInstance().getGeneralStoneBasicCFG(itemidTemp);
				if(rcfg1==null||rcfg.upIid<=0){
					break;
				}
			}
			if( moneyNeed > money )
				return false;
			if(moneyNeed >0)
			commonFlowRecord.addChange(useMoney(moneyNeed));
			
			for(SBean.DropEntryNew e : matList)
			{
				if( e.type == GameData.COMMON_TYPE_ITEM )
				{
					commonFlowRecord.addChange(delItem(e.id, e.arg));
				}
				else
				{
					commonFlowRecord.addChange(delEquip(e.id, e.arg));
				}
			}
			
			for(int iid:itemList){
				generalStone.remove(iid);
			}
			
			stoneInfo.itemId=itemidTemp;
			stoneInfo.exp = expTemp;
			
//			commonFlowRecord.setEvent(TLog.AT_GENERAL_STONE_ENCHANCE);
			commonFlowRecord.setArg(itemId, stoneInfo.itemId, moneyNeed, bUP?1:0);			
			tlogEvent.addCommonFlow(commonFlowRecord);
			
		}
		
//		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	
	public boolean generalsResetGeneralStone(int itemId, Set<Integer> lock, Set<Integer> lock2, List<SBean.DBGeneralStoneProp> props)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		List<GeneralStoneEvoCostCFGS> cfg = gs.getGameData().getGeneralStoneEvoCostsCFG();
		if (cfg == null)
			return false;
		
		synchronized (this)
		{
			DBGeneralStone stoneInfo = generalStone.get(itemId);
			if(stoneInfo==null){
				return false;
			}
			
			if(lvlVIP<15&&lock2.size()>0){
				return false;
			}
										
			int moneyCost = 0;
			int stoneCost = 0;
			
			for(GeneralStoneEvoCostCFGS cost : cfg){
				if(cost.totalLock==lock.size()){
					moneyCost += cost.costMoneyLock1;
					stoneCost += cost.costStoneLock1;
				}
			}
			
			for(GeneralStoneEvoCostCFGS cost : cfg){
				if(cost.totalLock==lock2.size()){
					moneyCost += cost.costMoneyLock2;
					stoneCost += cost.costStoneLock2;
				}
			}
			
			if (money < moneyCost || stone < stoneCost)
				return false;
			
			int count = stoneInfo.passes.size();
			if (lock.size()+lock2.size() > count)
				return false;
			List<GeneralStonePropCFGS> newProp = gs.getGameData().getGeneralStoneSpropCFG();
			
			if (newProp == null)
				return false;
						
			GeneralStoneBasicCFGS rcfg = GameData.getInstance().getGeneralStoneBasicCFG((short) stoneInfo.itemId);
			if(rcfg==null){
				return false;
			}
			count = rcfg.maxSkillCount;
			
			if (count >= newProp.size())
				return false;
			int totalprop = 0;
			for (GeneralStonePropCFGS p : newProp)
				totalprop += p.value;
			
			List<Short> newPropsLock = new ArrayList<Short>();
			for (int i = 0; i < count; i ++) {
				if (lock.contains(i + 1)){
					newPropsLock.add(stoneInfo.passes.get(i).propId);
				}
				if (lock2.contains(i + 1)){
					newPropsLock.add(stoneInfo.passes.get(i).propId);
				}
			}
			
			List<DBGeneralStoneProp> newProps = new ArrayList<DBGeneralStoneProp>();
			for (int i = 0; i < count; i ++) {
				if (lock.contains(i + 1))
					newProps.add(stoneInfo.passes.get(i));
				else {
					SBean.GeneralStonePropCFGS prop = null;
					
					
					if (!lock2.contains(i+1)){
						GeneralStonePropCFGS propCfg1 =null;						
						
						for(int j=0;j<50;j++){
							int rand = gs.getGameData().getRandom().nextInt(totalprop);
							int totalp=0;
							int randProp = 0;
							for (GeneralStonePropCFGS p : newProp) {
								totalp += p.value;
								if (totalp > rand)
									break;
								randProp ++;
							}
							propCfg1 = newProp.get(randProp);
							
							boolean ishas =true;
							for(DBGeneralStoneProp propHas: newProps){
								if(propHas.propId==propCfg1.propId){
									ishas =false;
								}
							}
							if(newPropsLock.contains(propCfg1.propId)){
								ishas =false;
							}
							
							if(ishas){
								break;
							}
							
						}
										
						prop = propCfg1;
					}
						
					
					List<GeneralStonePropValueCFGS> newPropValue = gs.getGameData().getGeneralStoneSpropValueCFG();
					
					if (newPropValue == null)
						return false;
					int totalValue = 0;
					for (GeneralStonePropValueCFGS p : newPropValue)
						totalValue += p.weight;
					int rand = gs.getGameData().getRandom().nextInt(totalValue);
					
					int index = 0;
					int totalv = 0;
					for (GeneralStonePropValueCFGS p : newPropValue) {
						totalv += p.weight;
						if (totalv > rand)
							break;
						index ++;
					}
					GeneralStonePropValueCFGS propValue = newPropValue.get(index);
					
					if(propValue==null){
						return false;
					}
					
					int span = (int)((propValue.maxvalue - propValue.minvalue)*100);
					
					int randProp1 = gs.getGameData().getRandom().nextInt(span)+1;
					int value = randProp1+ (int)(propValue.minvalue*100);
					if(prop==null){
						DBGeneralStoneProp dbGeneralStoneProp = stoneInfo.passes.get(i);
						float propvalue= (dbGeneralStoneProp.value>=((float)value)/100?dbGeneralStoneProp.value:((float)value)/100);//rank没锟斤拷
						byte  proprank = (byte) (dbGeneralStoneProp.value>=((float)value)/100?dbGeneralStoneProp.rank:index);
						newProps.add(new SBean.DBGeneralStoneProp(dbGeneralStoneProp.propId, (byte) dbGeneralStoneProp.type, (byte)proprank, propvalue));
					}else{
						newProps.add(new SBean.DBGeneralStoneProp(prop.propId, (byte)prop.type, (byte)index, ((float)value)/100));
					}
					
				}
			}
			commonFlowRecord.addChange(useMoney(moneyCost));
			commonFlowRecord.addChange(useStone(stoneCost));
			stoneInfo.resetPasses = newProps;
			for (DBGeneralStoneProp p : newProps)
				props.add(p.kdClone());
			
			commonFlowRecord.setEvent(TLog.AT_GENERAL_STONE_RETPROP);
			commonFlowRecord.setArg(itemId, 0);
			tlogEvent.addCommonFlow(commonFlowRecord);
		}
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean generalsSaveResetGeneralStone(int itemId, List<SBean.DBGeneralStoneProp> props)
	{
		DBGeneralStone stoneInfo = generalStone.get(itemId);
		if(stoneInfo==null){
			return false;
		}
		
		if (stoneInfo.resetPasses.size() == 0)
			return false;
		
		stoneInfo.passes.clear();
		for (DBGeneralStoneProp p :stoneInfo.resetPasses)
			stoneInfo.passes.add(p.kdClone());
		stoneInfo.resetPasses.clear();
		for (DBGeneralStoneProp p : stoneInfo.passes)
			props.add(p.kdClone());
		return true;
	}
	
	public boolean activateRelation(short id, short iid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		SBean.RelationCFGS rcfg = gs.gameData.getRelationCFG(id);
		if (rcfg == null)
			return false;
		
		boolean found = false;
		int index = 0;
		for (short i : rcfg.activateItems) {
			if (i == iid) {
				found = true;
				break;
			}
			index ++;
		}
		
		if (!found)
			return false;
		
		synchronized (this) {
			if (getItemCount(iid) <= 0)
				return false;
			
			SBean.DBRelation re = relations.get(id);
			if (re == null) {
				re = new SBean.DBRelation(id, new ArrayList<Short>(), 0, 0);
				for (int i = 0; i < rcfg.activateItems.size(); i++)
					re.lvls.add((short)0);
				relations.put(id, re);
			}
			
			if (index >= re.lvls.size())
				return false;
			
			short lvl = re.lvls.get(index);
			if (lvl > 0)
				return false;
			
			lvl = 1;
			re.lvls.set(index, lvl);
			commonFlowRecord.addChange(delItem(iid, 1));
			commonFlowRecord.setArg(id, iid);
		}
		commonFlowRecord.setEvent(TLog.AT_RELATION_ACTIVATE);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean upgradeRelation(short id, short gid, int[] ret)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		SBean.RelationCFGS rcfg = gs.gameData.getRelationCFG(id);
		if (rcfg == null)
			return false;
		
		boolean found = false;
		int index = 0;
		for (short i : rcfg.activateItems) {
			Short g = gs.gameData.getRelationItemCFG(i);
			if (g != null && g == gid) {
				found = true;
				break;
			}
			index ++;
		}
		
		if (!found) {
			ret[0] = -1;
			return false;
		}
		
		SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(gid);
		if( gcfg == null )
			return false;
		short iid = gcfg.stoneID;
		
		short commonId = gs.gameData.generalsCmn.commonSoulStoneId;
		
		synchronized (this) {
			SBean.DBRelation re = relations.get(id);
			if (re == null) {
				ret[0] = -2;
				return false;
			}
			
			if (index >= re.lvls.size()) {
				ret[0] = -3;
				return false;
			}
			
			short lvl = re.lvls.get(index);
			if (lvl <= 0) {
				ret[0] = -4;
				return false;
			}
			
			Integer consume = gs.gameData.getRelationConsumeCFG(gid, lvl);
			if (consume == null) {
				ret[0] = -5;
				return false;
			}
			
			int itemCount = getItemCount(iid);
			int commonItemCount = getItemCount(commonId);
			if (itemCount + commonItemCount < consume) {
				ret[0] = -6;
				return false;
			}
			
			lvl ++;
			re.lvls.set(index, lvl);
			
			if (relationStone == 0) {
				int commonConsume = consume - itemCount;
				if (commonConsume < 0)
					commonConsume = 0;
				if (commonConsume > 0) {
					commonFlowRecord.addChange(delItem(commonId, commonConsume));
					commonFlowRecord.addChange(delItem(iid, itemCount));
				}
				else
					commonFlowRecord.addChange(delItem(iid, consume));
			}
			else {
				int itemConsume = consume - commonItemCount;
				if (itemConsume < 0)
					itemConsume = 0;
				if (itemConsume > 0) {
					commonFlowRecord.addChange(delItem(commonId, commonItemCount));
					commonFlowRecord.addChange(delItem(iid, itemConsume));
				}
				else
					commonFlowRecord.addChange(delItem(commonId, consume));
			}
			commonFlowRecord.setArg(id, gid, lvl);
		}
		commonFlowRecord.setEvent(TLog.AT_RELATION_UPGRADE);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public boolean setRelationStone(byte stone)
	{
		if (stone != 0 && stone != 1)
			return false;
		
		relationStone = stone;
		return true;
	}
	
	public List<SBean.DBRelation> getActiveRelationsWithoutLock()
	{
		List<SBean.DBRelation> rel = new ArrayList<SBean.DBRelation>();
		for (SBean.DBRelation r : relations.values()) {
			boolean activated = true;
			for (short lvl : r.lvls)
				if (lvl <= 0)
					activated = false;
			
			if (activated)
				rel.add(r.kdClone());
		}
		return rel;
	}
	
	synchronized public List<SBean.DBRelation> getActiveRelations()
	{
		return getActiveRelationsWithoutLock();
	}
	
	synchronized public void hireMarchGeneral(SBean.DBMarchMercGeneral general)
	{
		if (general.type == 0 && mercGeneral == null)
			mercGeneral = general;
		else if (general.type == 1 && mercGeneralSura == null)
			mercGeneralSura = general;
	}
	
	synchronized public SBean.DBMarchMercGeneral getMarchGeneral()
	{
		if (mercGeneral == null)
			return null;
		return mercGeneral.kdClone();
	}
	
	synchronized public SBean.DBMarchMercGeneral getSuraGeneral()
	{
		if (mercGeneralSura == null)
			return null;
		return mercGeneralSura.kdClone();
	}
	
	private void duelCreate()
	{
		if (duel == null)
			duel = new SBean.DBRoleDuel(0, (byte)0, (byte)0, (byte)0, (byte)0, 0);
	}
	synchronized public void addDuelPoint(int add)
	{
		if (duel == null)
			duelCreate();
		
		if (add > 0)
			duel.point += add;
	}
	
	synchronized public void useDuelPoint(int use)
	{
		if (duel != null && use > 0) {
			duel.point -= use;
			if (duel.point < 0)
				duel.point = 0;
		}
	}
	
	synchronized public int duelFinishReward(short duelLvl)
	{
		if (duel == null) 
			duelCreate();
		
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		int time = gs.getTime();
		boolean found = false;
		
		for (SBean.DuelTimeCFGS tcfg : cfg.specialTimes) {
			int begin = gs.getTimeByMinuteOffset(tcfg.begin);
			int end = gs.getTimeByMinuteOffset(tcfg.end);
			if (time > begin && time < end) {
				found = true;
				break;
			}
		}
		
		int rewardPoints = 0;
		if (found) {
			if (duelLvl < 100)
				rewardPoints = cfg.specialRewardPoints;
			else
				rewardPoints = cfg.specialLegendRewardPoints;
		}
		else
			rewardPoints = cfg.rewardPoints;
		
		if (duel.rewardTimes < cfg.rewardTimes) {
			addDuelPoint(rewardPoints);
			duel.rewardTimes ++;
		}
		else
			rewardPoints = 0;
		
		return rewardPoints;
	}
	
	synchronized public byte getDuelFightTimes()
	{
		if (duel == null)
			return 0;
		dayRefresh();
		
		return duel.fightTimes;
	}
	
	synchronized public void duelJoin(short duelLvl)
	{
		if (duel == null) 
			duelCreate();
		
		dayRefresh();
		if (duelLvl < 100)
			return;
		
		duel.fightTimes ++;
	}
	
	synchronized public byte getDuelBuyTimes()
	{
		if (duel == null)
			return 0;
		dayRefresh();
		
		return duel.buyTimes;
	}
	
	synchronized public boolean duelBuyTime()
	{
		if (duel == null) 
			duelCreate();
		
		dayRefresh();
		
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		if (duel.buyTimes >= cfg.buyTimeCosts.size())
			return false;
		
		int cost = cfg.buyTimeCosts.get(duel.buyTimes);
		if (stone < cost)
			return false;
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.addChange(useStone(cost));
		duel.buyTimes ++;
		
		commonFlowRecord.setEvent(TLog.AT_DUELBUYTIME);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	synchronized public SBean.DBRoleSura getSura()
	{
		if (sura == null)
			sura = new SBean.DBRoleSura((byte)0, new ArrayList<Short>(), new ArrayList<Short>(), (byte)0, (byte)0, (byte)0, 0);
		
		return sura.kdClone();
	}
	
	public void suraSync()
	{		
		SBean.DBRoleSura roleSura = null;
		boolean mercExist = false;
		synchronized( this )
		{
			dayRefresh();
			if (sura == null)
				sura = new SBean.DBRoleSura((byte)0, new ArrayList<Short>(), new ArrayList<Short>(), (byte)0, (byte)0, (byte)0, 0);

			roleSura = sura.kdClone();
			mercExist = (mercGeneralSura != null);
		}
		
		if (roleSura == null) {
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraSyncRes(null, null, null, null, false));
			return;
		}
		
		List<Short> gids = new ArrayList<Short>();
		byte[] stageInfo = {0, 0};
		List<SBean.DBSuraEnemy> enemies = gs.getSuraManager().getStageEnemies(roleSura.stage, gids, stageInfo);
		if (enemies == null || gids.size() == 0) {
			gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraSyncRes(null, null, null, null, false));
			return;
		}
		
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraSyncRes(roleSura, enemies, gids, stageInfo, mercExist));
	}
	
	public void suraFinishAttack(byte stage, byte star, List<Short> deadGids, boolean mercDead)
	{		
		SuraManager.AttackFinishRes	res = new SuraManager.AttackFinishRes();
		synchronized(this) 
		{
			if (sura == null)
				return;
			
			if( stage != sura.stage )
				return;
			
			if (mercDead && mercGeneralSura != null)
				mercGeneralSura.status = new SBean.DBRoleMarchGeneralState(mercGeneralSura.general.id, 0, 0, 0);
			
			for (short g : deadGids) {
				boolean found = false;
				for (short o : sura.deadGids) {
					if (o == g) {
						found = true;
						break;
					}
				}
				if (!found)
					sura.deadGids.add(g);
			}

			res.bWin = star > 0;

			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			commonFlowRecord.setEvent(TLog.AT_SURA_FINISH);
			
			if (res.bWin) {
				res.drops = new ArrayList<SBean.DropEntryNew>();
				SBean.SuraCFGS cfg = gs.gameData.getSuraCFG();
				if (sura.stage < cfg.stages.size()) {
					SBean.SuraStageCFGS scfg = cfg.stages.get(sura.stage);
					SBean.DropEntryNew d = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, scfg.money);
					res.drops.add(d);
					SBean.DropEntry entry = gs.gameData.getDropTableRandomEntry(scfg.dropId);
					if (entry != null) {
						d = new SBean.DropEntryNew(entry.type, entry.id, entry.arg);
						res.drops.add(d);
					}
					for (SBean.DropEntryNew drop : res.drops)
						commonFlowRecord.addChange(addDropNew(drop));
				}
				
				sura.stage ++;
			}
			res.stage = stage;

			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(this, tlogEvent);
		}

		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraFinishAttackRes(res));
	}
	
	public void suraStartAttack(byte stage, List<Short> gids, SBean.DBPetBrief pet, boolean useMerc)
	{	
		SuraManager.AttackStartRes res = null;
		short mercGid = 0;
		int mercRid = -1;
		synchronized (this) 
		{
			if (sura == null)
				return;
			
			for(short gid : gids)
			{
				if (gid > 0)
				{
					General g = generals.get(gid);
					if (useMerc && mercGeneralSura != null && mercGeneralSura.general.id == gid) {
						mercGid = gid;
						mercRid = mercGeneralSura.rid;
					}
					else if( g == null )
					{
						gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, null, mercGid, mercRid));
						return;
					}
					else {
						for (short dead : sura.deadGids) {
							if (dead == gid) {
								gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, null, mercGid, mercRid));
								return;
							}
						}
					}
				}
			}
			if (useMerc && mercGid > 0) 
				gids.remove(new Short(mercGid));
			SBean.MarchCFGS cfg = GameData.getInstance().getMarchCFG();
			if (stage < 0 && stage >= cfg.stages.size())
			{
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, null, mercGid, mercRid));
				return;
			}
			dayRefresh();
			if (sura.stage != stage) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, null, mercGid, mercRid));
				return;
			}
			List<SBean.DBSuraEnemy> enemies = gs.getSuraManager().getStageEnemies(stage, null, null);
			if (enemies == null || enemies.size() != 3) {
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, null, mercGid, mercRid));
				return;
			}
			res = new SuraManager.AttackStartRes();
			res.sgenerals = copyGeneralsWithoutLock(gids);
			if (useMerc && mercGeneralSura != null)
				res.sgenerals.add(mercGeneralSura.general.kdClone());
			res.pets = new ArrayList<SBean.DBPetBrief>();
			res.srelation = getActiveRelationsWithoutLock();
			res.sgStones = this.getActiveGeneralStones();
			if (pet != null)
				res.pets.add(pet);
			res.enemies = new ArrayList<SuraManager.AttackStartEnemy>();
			for (SBean.DBSuraEnemy enemy : enemies) {
				SuraManager.AttackStartEnemy e = new SuraManager.AttackStartEnemy();
				e.erid = enemy.id;
				e.egenerals = enemy.generals;
				e.epets = enemy.pets;
				e.erelation = new ArrayList<SBean.DBRelation>();
				e.egStones = new ArrayList<SBean.DBGeneralStone>();
				res.enemies.add(e);
			}
		}
		gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeSuraStartAttackRes(stage, res, mercGid, mercRid));
	}
	
	public boolean suraActivate(short gid)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		synchronized( this )
		{
			dayRefresh();
			if (sura == null)
				return false;
			
			boolean found = false;
			for (short agid : sura.activatedGids)
				if (gid == agid) {
					found = true;
					break;
				}
					
			if (found)
				return false;
			
			if (stone < 100)
				return false;
			commonFlowRecord.addChange(useStone(100));
			
			sura.activatedGids.add(gid);
		}
		
		commonFlowRecord.setEvent(TLog.AT_SURA_ACTIVE);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized SBean.DBGeneralOfficial getGeneralOfficial(short gid)
	{
		if (lvl < gs.gameData.official.basic.openLvl)
			return null;
		
		General g = generals.get(gid);
		if (g == null || g.official == null || g.official.size() == 0)
			return null;
		
		return g.official.get(0).kdClone();
	}
	
	public synchronized boolean addOfficialExp(short gid, int itemCount, int[] itemUsed)
	{
		if (lvl < gs.gameData.official.basic.openLvl)
			return false;
		
		General g = generals.get(gid);
		if (g == null || g.official == null || g.official.size() == 0)
			return false;
		SBean.DBGeneralOfficial official = g.official.get(0);

		if (official.lvl >= gs.gameData.official.levels.size())
			return false;
		SBean.OfficialLevelCFGS levelCfg = gs.gameData.official.levels.get(official.lvl);
		
		if (itemCount < 0)
			return false;
		
		boolean full = false;
		if (itemCount == 0)
			full = true;
		
		short iid = gs.gameData.official.basic.levelItemId;
		boolean result = false;
		while (full || itemCount > 0) {
			result = true;
			if (getItemCount(iid) > 0)
				delItem(iid, 1);
			else
				break;
			itemUsed[0] ++;
				
			if (itemCount > 0)
				itemCount --;
			
			official.exp ++;
			if (official.exp >= levelCfg.itemCount) {
				official.lvl ++;
				official.exp -= levelCfg.itemCount;
				if (full)
					break;
			}
		}
		
		return result;
	}
	
	public synchronized int upgradeOfficialSkill(short gid, short targetLvl)
	{
		if (lvl < gs.gameData.official.basic.openLvl)
			return 0;
		
		General g = generals.get(gid);
		if (g == null || g.official == null || g.official.size() == 0)
			return 0;
		SBean.DBGeneralOfficial official = g.official.get(0);

		if (official.skillLvl >= gs.gameData.official.skillLevels.size() || official.skillLvl >= targetLvl)
			return 0;
		
		int lvlDiff = targetLvl - official.skillLvl;
		int levelUp = 0;
		for (int i = 0; i < lvlDiff; i ++) {
			SBean.OfficialSkillLevelCFGS levelCfg = gs.gameData.official.skillLevels.get(official.skillLvl);
			
			short iid = gs.gameData.official.basic.skillLevelItemId;
			if (getItemCount(iid) >= levelCfg.itemCount)
				delItem(iid, levelCfg.itemCount);
			else
				break;
			
			official.skillLvl ++;
			levelUp ++;
		}
		if (levelUp == 0)
			return 0;
		
		return official.skillLvl;
	}
	
	public synchronized boolean resetOfficial(short gid, int[] count)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		if (lvl < gs.gameData.official.basic.openLvl)
			return false;
		
		General g = generals.get(gid);
		if (g == null || g.official == null || g.official.size() == 0)
			return false;
		SBean.DBGeneralOfficial official = g.official.get(0);

		if (stone < gs.gameData.official.basic.officialResetStone)
			return false;
		
		int itemCount = 0;
		short lvl = 0;
		while (official.lvl > lvl) {
			if (lvl < gs.gameData.official.levels.size())
				itemCount += gs.gameData.official.levels.get(lvl).itemCount;
			lvl ++;
		}
		itemCount += official.exp;
		itemCount = (int)(itemCount * gs.gameData.official.basic.officialResetRate);
		
		short iid = gs.gameData.official.basic.levelItemId;
		
		official.lvl = 0; 
		official.exp = 0;
		commonFlowRecord.addChange(useStone(gs.gameData.official.basic.officialResetStone));
		addItem(iid, itemCount);
		count[0] = itemCount;
		
		commonFlowRecord.setEvent(TLog.AT_RESET_OFFICIAL);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized boolean resetOfficialSkill(short gid, int[] count)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		
		if (lvl < gs.gameData.official.basic.openLvl)
			return false;
		
		General g = generals.get(gid);
		if (g == null || g.official == null || g.official.size() == 0)
			return false;
		SBean.DBGeneralOfficial official = g.official.get(0);

		if (stone < gs.gameData.official.basic.skillResetStone)
			return false;
		
		int itemCount = 0;
		short lvl = 0;
		while (official.skillLvl > lvl) {
			if (lvl < gs.gameData.official.skillLevels.size())
				itemCount += gs.gameData.official.skillLevels.get(lvl).itemCount;
			lvl ++;
		}
		itemCount = (int)(itemCount * gs.gameData.official.basic.skillResetRate);
		
		short iid = gs.gameData.official.basic.skillLevelItemId;
		
		official.skillLvl = 0; 
		commonFlowRecord.addChange(useStone(gs.gameData.official.basic.skillResetStone));
		addItem(iid, itemCount);
		count[0] = itemCount;
		
		commonFlowRecord.setEvent(TLog.AT_RESET_OFFCIAL_SKILL);
		commonFlowRecord.setArg(0, 0);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(this, tlogEvent);
		return true;
	}
	
	public synchronized void setVipShow(boolean show)
	{
		vipShow = (byte)(show?1:0);
	}
	
	public void refreshTreasureLimiCnt() {
	    dayRefresh();
	    if (treasureLimitCnt == null) {
	        treasureLimitCnt = new DBTreasureLimitCnt((byte)0, (byte)0, (byte)0, 0);
	        return;
	    }
	    SBean.TreasureMapBaseCFGS cfgs = gs.gameData.treasureMap;
	    int now = gs.getTime();
	    if (treasureLimitCnt.lastAddTime > 0) {
	        while ((now - treasureLimitCnt.lastAddTime) > cfgs.addCountTime * 60) {
	            treasureLimitCnt.digOtherCnt = (byte)(treasureLimitCnt.digOtherCnt - cfgs.addCount);
	            if (treasureLimitCnt.digOtherCnt <= 0) {
	                treasureLimitCnt.lastAddTime = 0;
	                treasureLimitCnt.digOtherCnt = 0;
	                treasureLimitCnt.lastAddTime = now;
	                break;
	            } else {
	                if (treasureLimitCnt.lastAddTime + cfgs.addCountTime * 60 < now) {
	                    treasureLimitCnt.lastAddTime = treasureLimitCnt.lastAddTime + cfgs.addCountTime * 60;
	                } else {
	                    break;
	                }
	            }
	        }
	    }
	    
	    if (treasureLimitCnt.isTimeCntAdd == 0) {
	        Date d = new Date((long)(now * 1000l));
	        Calendar c = Calendar.getInstance();
	        c.setTime(d);
	        if (c.get(Calendar.HOUR_OF_DAY) > cfgs.refreshTime) {
	            treasureLimitCnt.digOtherCnt = (byte)(treasureLimitCnt.digOtherCnt - cfgs.refreshTimeAddCount);
	            if (treasureLimitCnt.digOtherCnt < 0) {
	                treasureLimitCnt.digOtherCnt = 0;
	            }
	            treasureLimitCnt.isTimeCntAdd = 1;
	        }
	    }
	}
	
	public byte useTreasureMap(byte treasureMapId)
	{
	    byte result = 0;
	    SBean.TreasureMapBaseCFGS cfgs = gs.gameData.treasureMap;
	    SBean.TreasureMapCFGS mapCfgs = null;
	    for (SBean.TreasureMapCFGS c : cfgs.treasureMaps) {
            if (c.id == treasureMapId) {
                mapCfgs = c;
                break;
            }
        }
	    if (mapCfgs == null) 
	        return 0;
	    if (cfgs.dayUseTreasureMapCount <= treasureLimitCnt.useTreasureCnt)
	        return 2;
	    if (gs.getTreasureMapManager().getRoleTreasureMap(this.id) != null) {
	        return 4;
	    }
	    if (gs.getTreasureMapManager().getTotalCount() >= cfgs.maxCount) {
	        return 5;
	    }
	    TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
        synchronized( this )
        {
            int countNow = getItemCount(mapCfgs.itemId);
            if (countNow < 1)
                return 3;
            commonFlowRecord.addChange(delItem(mapCfgs.itemId, 1));
            SBean.DBTreasureMap map = new DBTreasureMap();
            map.id = treasureMapId;
            map.rid = this.id;
            map.rname = this.name;
            map.diggers = new ArrayList<Integer>();
            map.rewards = new ArrayList<SBean.DBTreaureReward>();
            map.openedCells = new ArrayList<Byte>();
            map.createTime = gs.getTime();
            Random r = new Random();
            map.treasureCell = (byte)(r.nextInt(mapCfgs.rewardsMaxCnt) + 1);
            for (int i = 1; i <= mapCfgs.rewardsMaxCnt; i++) {
                SBean.DBTreaureReward reward = new SBean.DBTreaureReward();
                if (i == map.treasureCell) {
                    SBean.DropEntry de = gs.gameData.getDropTableRandomEntry(mapCfgs.treasureRewards);
                    if (de != null) {
                        reward.dropEntry = de;
                    }
                } else {
                    SBean.DropEntry de = gs.gameData.getDropTableRandomEntry(mapCfgs.commonRewards);
                    if (de != null) {
                        reward.dropEntry = de;
                    }
                }
                reward.num = (byte)i;
                reward.isOpen = 0;
                reward.diggerName = "";
                map.rewards.add(reward);
            }
            gs.getTreasureMapManager().addTreasureMap(map);
            treasureLimitCnt.useTreasureCnt++;
            result = 1;
        }
        commonFlowRecord.setEvent(TLog.AT_TREASURE_MAP_USE);
        commonFlowRecord.setArg(mapCfgs.itemId, 0);
        TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
        tlogEvent.addCommonFlow(commonFlowRecord);
        gs.getTLogger().logEvent(this, tlogEvent);
	    return result;
	}
	
	public String digTreasureMap(byte id, byte num, int rid) {
	    SBean.DBTreasureMap map = gs.getTreasureMapManager().getTreasureMap(id, rid);
	    if (map == null) {
	        return "0;0";
	    }
	    SBean.TreasureMapBaseCFGS cfgs = gs.gameData.treasureMap;
        SBean.TreasureMapCFGS mapCfgs = null;
        for (SBean.TreasureMapCFGS c : cfgs.treasureMaps) {
            if (c.id == id) {
                mapCfgs = c;
                break;
            }
        }
        if (mapCfgs == null) 
            return "0;0";
	    TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
        synchronized (this)
        {  
            if (rid == this.id) {
                if (map.diggers.contains(this.id) && this.stone < mapCfgs.digCost) {
                    return "2;0";
                }
            } else {
                if (this.treasureLimitCnt.digOtherCnt >= cfgs.digOtherMaxCount) {
                    return "3;0";
                }
                if (map.diggers.contains(this.id)) {
                    return "4;0";
                } 
            }
            if (gs.getTreasureMapManager().digTreasureMap(rid, id, num, this.name, this.id)) {
                List<Integer> list = new ArrayList<Integer>();
                list.addAll(map.diggers);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == this.id) {
                        list.remove(i);
                        break;
                    }
                }
                if (rid == this.id && list.contains(this.id)) {
                    commonFlowRecord.addChange(useStone(mapCfgs.digCost));
                    TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
                    tlogEvent.addCommonFlow(commonFlowRecord);
                    gs.getTLogger().logEvent(this, tlogEvent);
                }
                commonFlowRecord.setEvent(TLog.AT_TREASURE_MAP_DIG);
                SBean.DBTreaureReward reward = null;
                for (DBTreaureReward dbTreaureReward : map.rewards) {
                    if (dbTreaureReward.num == num) {
                        reward = dbTreaureReward;
                        break;
                    }
                }
                List<SBean.DropEntryNew> attList = new ArrayList<SBean.DropEntryNew>();
                SBean.DropEntryNew drop = new SBean.DropEntryNew();
                drop.type = reward.dropEntry.type;
                drop.id = reward.dropEntry.id;
                drop.arg = reward.dropEntry.arg;
                attList.add(drop);
                addDropNew(drop);
                if (rid != this.id) {
                    map.otherDigCnt++;
                    this.treasureLimitCnt.digOtherCnt++;
                    if (this.treasureLimitCnt.lastAddTime == 0) {
                        this.treasureLimitCnt.lastAddTime = gs.getTime();
                    }
                }
                if (num == map.treasureCell || map.otherDigCnt >= cfgs.mapDigMaxCount) {
                    gs.getTreasureMapManager().removeTreasureMap(rid, id);
                    attList = new ArrayList<SBean.DropEntryNew>();
                    if (mapCfgs.userRewards1Id > 0) {
                        SBean.DropEntryNew drop1 = new SBean.DropEntryNew();
                        drop1.type = GameData.COMMON_TYPE_ITEM;
                        drop1.id = mapCfgs.userRewards1Id;
                        drop1.arg = mapCfgs.userRewards1Cnt;
                        attList.add(drop1);
                    }
                    if (mapCfgs.userRewards2Id > 0) {
                        SBean.DropEntryNew drop2 = new SBean.DropEntryNew();
                        drop2.type = GameData.COMMON_TYPE_ITEM;
                        drop2.id = mapCfgs.userRewards2Id;
                        drop2.arg = mapCfgs.userRewards2Cnt;
                        attList.add(drop2);
                    }
                    if (mapCfgs.userRewards3Id > 0) {
                        SBean.DropEntryNew drop3 = new SBean.DropEntryNew();
                        drop3.type = GameData.COMMON_TYPE_ITEM;
                        drop3.id = mapCfgs.userRewards3Id;
                        drop3.arg = mapCfgs.userRewards3Cnt;
                        attList.add(drop3);
                    }
                    gs.getLoginManager().sysSendMessage(0, rid, DBMail.Message.SUB_TYPE_TREASURE_MAP, cfgs.mailTitle, cfgs.mailContextUser, 0, true, attList);
                }
            } else {
                return "5;0";
            }
        }
        if (num == map.treasureCell) {
            return "1;1";
        } else {
            return "1;0";
        }
	}
	
	public byte beMonsterRegister(int zuanshi)
	{
	    DBBeMonsterBoss boss = gs.getBeMonsterManager().getBossByRid(this.id);
	    BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	    if (zuanshi < cfg.RegisterCostMin) {
	        zuanshi = cfg.RegisterCostMin;
	    }
	    if (zuanshi > cfg.RegisterCostMax) {
	        zuanshi = cfg.RegisterCostMax;
	    }
	    int stoneNeed = zuanshi + cfg.RegisterPoundage;
	    TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
        TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
        synchronized( this )
        {
            if( stone < stoneNeed )
                return 0;
            commonFlowRecord.addChange(useStone(stoneNeed));
            if (boss == null) {
                boss = new DBBeMonsterBoss();
            }
            boss.rid = this.id;
            boss.rname = this.name;
            boss.lineup = new ArrayList<DBBeMonsterLineup>();
            boss.bidPirce += zuanshi;
            boss.killCnt = 0;
            boss.bidTime = gs.getTime();
            gs.getBeMonsterManager().addBoss(boss);
        }
        commonFlowRecord.setEvent(TLog.AT_BEMONSTER_REGISTER);
        tlogEvent.addCommonFlow(commonFlowRecord);
        gs.getTLogger().logEvent(this, tlogEvent);
        return 1;
	}
	
	public byte beMonsterSetLineup(DBBeMonsterLineup lineup, short pitid)
    {
	    BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	    DBBeMonsterBoss boss = gs.getBeMonsterManager().getBossByRid(this.id);
	    if (boss == null) {
	        return 0;
	    }
	    List<Short> gids = new ArrayList<Short>();
        for (DBBeMonsterGeneral g : lineup.generals) {
           gids.add(g.gid);
        }
	    DBBeMonsterLineup sameIdLineup = null;
	    for (int i = 0; i < boss.lineup.size(); i++) {
            DBBeMonsterLineup ml = boss.lineup.get(i);
            if (lineup.id == ml.id) {
                sameIdLineup = ml;
            } else {
               if (ml.pet == lineup.pet) {
                   return 0;
               }
               for (DBBeMonsterGeneral g : ml.generals) {
                   if (gids.contains(g.gid)) {
                       return 0;
                   }
               }
            }
        }
	    List<Short> petGid = new ArrayList<Short>();
        petGid.add(pitid);
        List<SBean.DBPetBrief> pet = new ArrayList<SBean.DBPetBrief>();
        List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
        synchronized( this )
        {
            pet = copyPetsWithoutLock(petGid);
            generals = copyGeneralsWithoutLock(gids);
        }
        if (pet == null || pet.size() <= 0 || generals == null || generals.size() != gids.size()) {
            return 0;
        }
        if (sameIdLineup != null) {
            lineup.hpPercent = sameIdLineup.hpPercent;
            lineup.upHpCnt = sameIdLineup.upHpCnt;
        } else {
            lineup.hpPercent = cfg.defaultUpHpPercent;
            lineup.upHpCnt = 0;
        }
        for (int i = 0; i < boss.lineup.size(); i++) {
            DBBeMonsterLineup ml = boss.lineup.get(i);
            if (lineup.id == ml.id) {
                boss.lineup.remove(i);
                break;
            }
        }
        for (DBBeMonsterGeneral g : lineup.generals) {
            g.hp = g.baseHp + (g.baseHp * lineup.hpPercent / 100);
            for (DBRoleGeneral dbRoleGeneral : generals) {
                if (g.gid == dbRoleGeneral.id) {
                    g.general = dbRoleGeneral;
                }
            }
        }
        lineup.pet = pet.get(0);
        boss.lineup.add(lineup);
        return 1;
    }
	
	public byte beMonsterUpHp(byte lineupId)
    {
	    DBBeMonsterBoss nowBoss = gs.getBeMonsterManager().getNowBoss();
	    if (nowBoss == null || nowBoss.rid != this.id) {
	        return 0;
	    }
	    DBBeMonsterLineup lineup = null;
	    for (DBBeMonsterLineup ml : nowBoss.lineup) {
            if (ml.id == lineupId) {
                lineup = ml;
                break;
            }
        }
	    if (lineup == null) {
	        return 0;
	    }
	    BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	    BeMonsterUpHpCFG upHpCfg = null;
	    for (BeMonsterUpHpCFG up : cfg.upHpCfgs) {
            if (up.cnt == (lineup.upHpCnt + 1)) {
                upHpCfg = up;
                break;
            }
        }
	    if (upHpCfg == null) {
	        return 0;
	    }
	    int stoneNeed = upHpCfg.cost;
        TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
        TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
        synchronized( this )
        {
            if( stone < stoneNeed )
                return 0;
            commonFlowRecord.addChange(useStone(stoneNeed));
            for (DBBeMonsterLineup ml : nowBoss.lineup) {
                if (ml.id == lineupId) {
                    ml.upHpCnt++;
                    ml.hpPercent += upHpCfg.percent;
                    for (DBBeMonsterGeneral g : lineup.generals) {
                        g.hp = g.baseHp + (g.baseHp * lineup.hpPercent / 100);
                    }
                    break;
                }
            }
        }
        commonFlowRecord.setEvent(TLog.AT_BEMONSTER_UPHP);
        tlogEvent.addCommonFlow(commonFlowRecord);
        gs.getTLogger().logEvent(this, tlogEvent);
        return 1;
    }
	
	public DBBeMonsterLineup beMonsterStartAttack(byte lineupId)
    {
	    BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	    DBBeMonsterBoss nowBoss = gs.getBeMonsterManager().getNowBoss();
        if (nowBoss == null || nowBoss.rid != this.id) {
            return null;
        }
        DBBeMonsterLineup lineup = null;
        for (DBBeMonsterLineup ml : nowBoss.lineup) {
            if (ml.id == lineupId) {
                lineup = ml;
                break;
            }
        }
        if (lineup == null) {
            return null;
        }
        DBBeMonsterAttacker att = gs.getBeMonsterManager().getAttackByRid(this.id);
        if (att == null) {
            att = new DBBeMonsterAttacker();
            att.rid = this.id;
            att.rname = this.name;
            att.attackCnt = 0;
            att.attackValue = 0;
            gs.getBeMonsterManager().addAttacker(att);
        }
        att.attackCnt++;
        if (att.attackCnt > cfg.attackMacCnt) {
            return null;
        }
        return lineup;
    }
	

    public synchronized byte beMonsterFinishAttack(byte lineupId, int totalAttackValue, List<DBBeMonsterGeneral> generals)
    {
        DBBeMonsterBoss nowBoss = gs.getBeMonsterManager().getNowBoss();
        if (nowBoss == null || nowBoss.rid != this.id) {
            return 0;
        }
        DBBeMonsterLineup lineup = null;
        for (DBBeMonsterLineup ml : nowBoss.lineup) {
            if (ml.id == lineupId) {
                lineup = ml;
                break;
            }
        }
        if (lineup == null) {
            return 0;
        }
        DBBeMonsterAttacker att = gs.getBeMonsterManager().getAttackByRid(this.id);
        if (att == null) {
            return 0;
        }
        att.attackValue += totalAttackValue;
        if (gs.getBeMonsterManager().updateLineupHp(lineupId, generals)) {
            return 1;
        }
        return 0;
    }
	
	final GameServer gs;
	final int id;
	int flag;
	int dayCommon; // 每锟斤拷刷锟铰憋拷签
	int dayFlag;
	int helpFlag1;
	int helpFlag2;
	short lvlDay;
	String name;
	String userName;
	String openID;
	int money;
	int stone;
	int exp;
	short vitUsedToday;
	short lvl;
	byte type;
	short headIconID;
	public short vit;
	int vitRecoverStart;
	short skillPoint;
	int skillPointRecoverStart;
	int lastLoginTime;
	int lastLogoutTime;
	SBean.DBCheckinLog checkinLog = new SBean.DBCheckinLog();
	int upgradeTime;
	boolean bDBLock = false; // TODO
	ForceInfo forceInfo;
	List<SBean.DBTaskPos> taskPos;
	Map<Short, Byte> combatFightLogs = new HashMap<Short, Byte>();
	List<SBean.DBEggLog> eggLogs = new ArrayList<SBean.DBEggLog>();
	List<SBean.DBCombatEventLog> combatEventLogs = new ArrayList<SBean.DBCombatEventLog>();
	SBean.DBCombatPos[] combatPos;
	Map<Short, Integer> items = new HashMap<Short, Integer>();
	Map<Short, Short> itemDropLogs = new HashMap<Short, Short>();
	Map<Short, Short> equips = new HashMap<Short, Short>();
	Map<Short, General> generals = new HashMap<Short, General>();
	Map<Short, Pet> pets = new HashMap<Short, Pet>();
	Map<Short, SBean.DBPetDeform> petDeforms = new HashMap<Short, SBean.DBPetDeform>();
	short petsTop = 0;
	List<SBean.DBFeedingPet> petsFeeding;
	List<SBean.DBCombatScore> combatScores = null;
	Shop shopNormal;
	Shop shopArena;
	Shop shopMarch;
	Shop shopRand1;
	Shop shopRand2;
	Shop shopForce;
	Shop[] shopRich = new Shop[SHOP_RICH_COUNT];
	Shop shopSuperArena;
	Shop shopDuel;
	Shop shopBless;
	Friend friend;
	Mail mail;
	int stonePayMidas;
	int stonePayAlpha;
	int stoneUsedTotal;
	byte lvlVIP;
	Set<Byte> vipRewards;
	Map<Short, Byte> combatResetLogs = new HashMap<Short, Byte>();
	
	Map<Byte, SBean.DBDailyTask2Log> dailyTask2Logs;
	int dailyActivity;
	List<Byte> dailyActivityRewards;
	int monthlyCardStartTime;
	List<Byte> payLvlState;
	
	SBean.DBRoleArenaState arenaState;
	int arenaBestRank;
	int arenaCurrentAttackRank;
	byte arenaCurrentAttackRes;
	List<SBean.DBRoleArenaLog> arenaLogs;
	int[] arenaCurrentEnemies;
	List<Short> arenaGenerals;
	List<Short> arenaPets;
	
	SBean.DBRoleSuperArena superArena;
	SBean.SuperArenaRecord curSuperArenaAttack;
	
	int marchStartTime;
	SBean.DBRoleMarchState marchState;
	List<SBean.DBRoleMarchGeneralState> marchGeneralStates;
	List<SBean.DBRoleMarchGeneralState> marchEnemyGeneralStates;
	List<SBean.DBRoleMarchEnemy> marchCurrentEnemies;
	byte marchPointReward;
	int marchPointDay;

	short buyMoneyTimes = 0;
	short buyVitTimes = 0;
	short buySkillPointTimes = 0;
	
	int netsid;
	long removeTime;
	List<MedalNotice> medalNoticeList = new ArrayList<MedalNotice>();
	List<Byte> vipNoticeList = new ArrayList<Byte>();
	List<Integer> stoneMonitorList = new ArrayList<Integer>();
	List<Byte> eventNoticeList = new ArrayList<Byte>();
	
	List<DBRole.PayLog> payLogs;
	
	public boolean bRecvWorldChat = false;
	public boolean bRecvForceChat = false;
	public boolean bRecvPrivateChat = false;
	
	CurrentCombat curCombat = null;
	CurrentCombatEvent curEventCombat = null;
	int  loginTime; //login time, liping
	long lastSaveTime = 0;
	int randTick;
	
	public int createTime;
	public byte lvlForceSeek = 0;
	
	public MSDKInfo msdkInfo = new MSDKInfo();
	
	public int wChatSyncStamp = 0;
	public int fChatSyncStamp = 0;
	public int pChatSyncStamp = 0;
	
	//public long banEndTime = 0;//-1锟斤拷锟斤拷锟斤拷,0:未锟斤拷牛锟�0锟斤拷沤锟斤拷锟绞憋拷锟�
	public LoginManager.RoleBanData banData;
	
	List<SBean.DropEntry> Bossdrops = new ArrayList<SBean.DropEntry>();
	
	public Set<Integer> loginGiftMail = new TreeSet<Integer>();
	public Map<Integer, SBean.DBRoleCheckinGift> checkinGift = new TreeMap<Integer, SBean.DBRoleCheckinGift>();
	public Map<Integer, SBean.DBRoleFirstPayGift> firstPayGift = new TreeMap<Integer, SBean.DBRoleFirstPayGift>();
	public Map<Integer, SBean.DBRoleConsumeGift> consumeGift = new TreeMap<Integer, SBean.DBRoleConsumeGift>();
	public Map<Integer, SBean.DBRoleConsumeRebate> consumeRebate = new TreeMap<Integer, SBean.DBRoleConsumeRebate>();
	public Map<Integer, SBean.DBRoleUpgradeGift> upgradeGift = new TreeMap<Integer, SBean.DBRoleUpgradeGift>();
	public Map<Integer, SBean.DBRoleGatherGift> gatherGift = new TreeMap<Integer, SBean.DBRoleGatherGift>();
	public Map<Integer, SBean.DBRolePayGift> payGift = new TreeMap<Integer, SBean.DBRolePayGift>();
	public Map<Integer, SBean.DBRoleLimitedShop> limitedShop = new TreeMap<Integer, SBean.DBRoleLimitedShop>();
	public Map<Integer, SBean.DBRoleLoginGift> loginGift = new TreeMap<Integer, SBean.DBRoleLoginGift>();
	public Map<Integer, SBean.DBRoleTradingCenter> tradingCenter = new TreeMap<Integer, SBean.DBRoleTradingCenter>();
	public Map<Integer, SBean.DBRoleDiskGift> diskGift = new TreeMap<Integer, SBean.DBRoleDiskGift>();
	
	public Map<Integer, SBean.DBRoleRechargeGift> rechargeGift = new TreeMap<Integer, SBean.DBRoleRechargeGift>();
	
	public SBean.DBGeneralStoneInfo generalStoneInfo ;
	public SBean.DBExpiratBossInfo expiratBossInfo ;
	public SBean.DBHeroesBossInfo  heroesBossInfo ;
	public DBDiskBetInfo diskBet;
	public DBDiskBetInfo diskBet2;
	public DBDiskBetInfo diskBet3;
	public DBBlessInfo bless;
	public DBHeadIcon headicon;
	public Map<Integer, SBean.DBGeneralStone> generalStone = new TreeMap<Integer, SBean.DBGeneralStone>();
	
	private LoginManager.WorldChatCache rolePcCache = new LoginManager.WorldChatCache();
	
	public byte boughtMoney;
	public Map<Short, SBean.DBSoulBoxLog> soulBoxLogs = new TreeMap<Short, SBean.DBSoulBoxLog>();
	
	public int broadcastsLastModifyTime;
	public List<Integer> broadcasts = new ArrayList<Integer>();
	public int feedTotal;
	
	public CityManager.RoleCitys citys;
	
	public int activity;
	public List<Byte> activityRewards;
	
	SBean.DBRichRole rich;
	//int richMovementRecoverStart;
	SBean.CombatBonus richCombatBonus;
	
	public int lastForceWarID;
	
	public SBean.DBInvitation invitation;
	
	public byte qqVipRewards;
	
	public volatile int userState = 0;
	
	public int forceThiefStamp = 0;
	public static short VIRT_PET_ID = 14444;
	public Map<Short, SBean.DBRelation> relations = new TreeMap<Short, SBean.DBRelation>();
	public SBean.DBMarchMercGeneral mercGeneral;
	public SBean.DBMarchMercGeneral mercGeneralSura;
	public SBean.DBRoleDuel duel;
	public SBean.DBRoleSura sura;
	public SBean.DBOptionalHotPoint optionalHotPoint;
	public byte relationStone;
	
	public byte vipShow;
	public SBean.DBTreasureLimitCnt treasureLimitCnt;
	
	
	public static void main(String[] args)
	{
		// for test
		List<Integer> lst = new ArrayList<Integer>();
		for(int i = 0; i < 10; ++i)
			lst.add(i);
		ListIterator<Integer> iter = lst.listIterator(lst.size());
		while( iter.hasPrevious() )
		{
			Integer e = iter.previous();
			System.out.println(e.intValue());
			if( e%2 == 0 )
				iter.remove();
		}
		iter = lst.listIterator(lst.size());
		while( iter.hasPrevious() )
		{
			Integer e = iter.previous();
			System.out.println(e.intValue());
			if( e%3 == 0 )
				continue;
			iter.remove();
		}
		iter = lst.listIterator(lst.size());
		while( iter.hasPrevious() )
		{
			Integer e = iter.previous();
			System.out.println(e.intValue());
			if( e%4 == 0 )
				continue;
		}
		lst.clear();
		iter = lst.listIterator(lst.size());
		while( iter.hasPrevious() )
		{
			Integer e = iter.previous();
			System.out.println(e.intValue());
			if( e%2 == 0 )
				iter.remove();
		}
	}

}


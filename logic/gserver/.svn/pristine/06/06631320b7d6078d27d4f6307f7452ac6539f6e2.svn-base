package i3k.gs;

import i3k.DBFriend;
import i3k.DBMail;
import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.DBUser;
import i3k.IDIP;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.ChangServerItemsCFGS;
import i3k.SBean.DBGeneralSeyen;
import i3k.SBean.DBGeneralStone;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBHeroesBossTimes;
import i3k.SBean.DBPetBrief;
import i3k.SBean.ExpatriateTransRoleReq;
import i3k.SBean.HeroesBossFinishAttRes;
import i3k.SBean.HeroesBossItemsCFGS;
import i3k.SBean.HeroesBossSyncRes;
import i3k.SBean.HeroesRank;
import i3k.TLog;
import i3k.gs.LoginManager.WorldChatCache.Msg;
import i3k.gs.Role.General;
import i3k.gtool.CDKeyGen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.management.ObjectName;

import ket.kdb.Table;
import ket.kdb.TableEntry;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.kdb.Transaction.ErrorCode;
import ket.util.Stream;

public class LoginManager
{

	public interface QueryRoleExistsByOtherGSCallBack
	{
		public void onCallBack(String openID, Integer roleID);
	}

	public interface GSStatMBean
	{
		int getOnlineCount();

		int getSessionCount();
	}

	public class GSStat implements GSStatMBean
	{
		@Override
		public int getOnlineCount()
		{
			return mapRoles.size();
		}

		@Override
		public int getSessionCount()
		{
			return maps2r.size();
		}
	}

	public static class GlobalState
	{
		public int	jubaoDay;
		public int	tssAnti;
		public short	roleLevelLimit;
	}

	public GlobalState getGlobalState()
	{
		GlobalState s = new GlobalState();
		s.jubaoDay = jubaoDay;
		s.tssAnti = gs.getConfig().tssAntiState;
		// s.roleLevelLimit = gs.getRoleLevelLimit();
		s.roleLevelLimit = (short) gs.getRoleLevelLimit();
		return s;
	}

	public static class RoleBriefCache
	{
		public RoleBriefCache(short lvl, short headIconID, int score,
				String name, String fname, int lastLoginTime,
				int activity)
		{
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.score = score;
			this.name = name;
			this.fname = fname;
			this.lastLoginTime = lastLoginTime;
			this.activity = activity;
		}

		public void update(RoleBriefCache data)
		{
			this.lvl = data.lvl;
			this.headIconID = data.headIconID;
			this.score = data.score;
			this.name = data.name;
			this.fname = data.fname;
			this.lastLoginTime = data.lastLoginTime;
			this.activity = data.activity;
		}

		public short	lvl;
		public short	headIconID;
		public int	score;
		public String	name;
		public String	fname;
		public int	lastLoginTime;
		public int	activity;
	}

	public static class RoleBriefCachePair
	{
		public RoleBriefCachePair(int id, RoleBriefCache cache)
		{
			this.id = id;
			this.cache = cache;
		}

		public int		id;
		public RoleBriefCache	cache;
	}

	public static class RoleBriefCacheData extends RoleBriefCache
	{
		public static final int	CLEAN_INTERVAL	= 60;
		public static final int	MAX_IDLE_TIME	= 3600;
		public static final int	REFRESH_TIME	= 300;

		public RoleBriefCacheData(byte lvl, byte gender, int score,
				String name, String fname, int lastLoginTime,
				int activity)
		{
			super(lvl, gender, score, name, fname, lastLoginTime,
					activity);
		}

		public RoleBriefCacheData(RoleBriefCache data)
		{
			super(data.lvl, data.headIconID, data.score, data.name,
					data.fname, data.lastLoginTime,
					data.activity);
		}

		public boolean isTooOld(int now)
		{
			return updateTime + MAX_IDLE_TIME < now;
		}

		public boolean isNeedRefresh(int now)
		{
			return updateTime + REFRESH_TIME < now;
		}

		public int	updateTime;
	}

	public static class PVPCache
	{
		public static final int	CLEAN_INTERVAL	= 60;

		public PVPCache(int id, int flag)
		{
			this.id = id;
			this.flag = flag;
		}

		public int	id;
		public int	flag;
	}

	public static class PVPCacheData extends PVPCache
	{
		public PVPCacheData(int id, int flag)
		{
			super(id, flag);
		}

		public void update(int flag)
		{
			this.flag = flag;
		}

		public boolean isTooOld(int now)
		{
			// TODO
			if (flag == 0)
				return updateTime + 300 < now;
			return updateTime + 60 < now;
		}

		public int	updateTime;
	}

	public static class UserLoginStub
	{
		public UserLoginStub(SBean.UserLoginRequest req,
				Role.MSDKInfo msdkInfo, int sid, String rname,
				short headIconID)
		{
			this.sid = sid;
			this.req = req;
			this.msdkInfo = msdkInfo;
			this.rname = rname;
			this.headIconID = headIconID;
			time = GameData.getTime();
		}

		public int			sid;
		public int			time;
		public SBean.UserLoginRequest	req;
		public Role.MSDKInfo		msdkInfo;
		public String			rname;
		public short			headIconID;
	}

	public static class ActiveRole
	{
		static final int	MAX_COUNT	= 100;
		static final int	DEL_COUNT	= 50;

		public ActiveRole(short lvl)
		{
			this.lvl = lvl;
		}

		public void put(SBean.DBRoleBrief e,
				Map<Integer, ActiveRole> idmap)
		{
			if (roles.size() >= MAX_COUNT)
			{
				SBean.DBRoleBrief re = roles.removeFirst();
				idmap.remove(re.id);
			}
			roles.add(e);
			idmap.put(e.id, this);
		}

		public void update(SBean.DBRoleBrief e)
		{
			for (SBean.DBRoleBrief r : roles)
			{
				if (r.id == e.id)
				{
					r.lvl = e.lvl;
					r.name = e.name;
					r.fname = e.fname;
					break;
				}
			}
		}

		public void remove(SBean.DBRoleBrief e)
		{
			Iterator<SBean.DBRoleBrief> iter = roles.iterator();
			while (iter.hasNext())
			{
				SBean.DBRoleBrief r = iter.next();
				if (r.id == e.id)
				{
					iter.remove();
					break;
				}
			}
		}

		public List<SBean.DBRoleBrief> get(int maxGetCount,
				Map<Integer, ActiveRole> idmap,
				Set<Integer> excludes)
		{
			List<SBean.DBRoleBrief> lst = new ArrayList<SBean.DBRoleBrief>();
			Iterator<SBean.DBRoleBrief> iter = roles.iterator();
			while (iter.hasNext())
			// while( lst.size() < maxGetCount && ! roles.isEmpty()
			// )
			{
				SBean.DBRoleBrief e = iter.next();
				if (!excludes.contains(e.id))
				{
					lst.add(e);
					iter.remove();
					if (lst.size() >= maxGetCount)
						break;
				}
			}
			if (roles.size() < DEL_COUNT)
				roles.addAll(lst);
			else
			{
				for (SBean.DBRoleBrief e : lst)
					idmap.remove(e.id);
			}
			return lst;
		}

		short				lvl;
		LinkedList<SBean.DBRoleBrief>	roles	= new LinkedList<SBean.DBRoleBrief>();
	}

	public static class ActiveRoles
	{
		public static final int	MAX_GET_COUNT	= 6;

		private ActiveRole getActiveRole(short lvl)
		{
			ActiveRole ar = array[lvl - 1];
			if (ar == null)
			{
				ar = new ActiveRole(lvl);
				array[lvl - 1] = ar;
			}
			return ar;
		}

		public synchronized void put(SBean.DBRoleBrief e)
		{
			ActiveRole arOld = idmap.get(e.id);
			if (arOld == null)
			{
				ActiveRole ar = getActiveRole(e.lvl);
				ar.put(e, idmap);
			} else if (arOld.lvl == e.lvl)
			{
				arOld.update(e);
			} else
			{
				arOld.remove(e);
				ActiveRole ar = getActiveRole(e.lvl);
				ar.put(e, idmap);
			}
		}

		public synchronized void get(List<SBean.DBRoleBrief> lst,
				short lvl, short lvlMin, short lvlMax,
				Set<Integer> excludes)
		{
			ActiveRole ar = getActiveRole(lvl);
			lst.addAll(ar.get(MAX_GET_COUNT - lst.size(), idmap,
					excludes));
			if (lst.size() >= MAX_GET_COUNT)
				return;
			short ll = (short) (lvl - 1);
			short lh = (short) (lvl + 1);
			while (ll >= lvlMin || lh <= lvlMax)
			{
				if (ll >= lvlMin)
				{
					ar = getActiveRole(ll);
					lst.addAll(ar.get(
							MAX_GET_COUNT
									- lst.size(),
							idmap, excludes));
					if (lst.size() >= MAX_GET_COUNT)
						return;
					--ll;
				}
				if (lh <= lvlMax)
				{
					ar = getActiveRole(lh);
					lst.addAll(ar.get(
							MAX_GET_COUNT
									- lst.size(),
							idmap, excludes));
					if (lst.size() >= MAX_GET_COUNT)
						return;
					++lh;
				}
			}
		}

		public synchronized void getRand(List<SBean.DBRoleBrief> lst,
				short lvl, short lvlMin, short lvlMax,
				Set<Integer> excludes)
		{
			Set<Integer> tset = new TreeSet<Integer>();
			for (int l = lvl - 2; l <= lvl + 2; ++l)
			{
				int i = l;
				if (i < lvlMin)
					i = lvlMin;
				if (i > lvlMax)
					i = lvlMax;
				tset.add(i);
			}
			while (lst.size() < MAX_GET_COUNT)
			{
				if (tset.isEmpty())
					break;
				Iterator<Integer> iter = tset.iterator();
				while (iter.hasNext())
				{
					short l = iter.next().shortValue();
					//
					ActiveRole ar = getActiveRole(l);
					List<SBean.DBRoleBrief> r = ar.get(1,
							idmap, excludes);
					if (r.isEmpty())
					{
						iter.remove();
					} else
					{
						lst.addAll(r);
						for (SBean.DBRoleBrief e : r)
							excludes.add(e.id);
						if (lst.size() >= MAX_GET_COUNT)
							return;
					}
					//
				}
			}
			ActiveRole ar = null;
			short ll = (short) (lvl - 3);
			short lh = (short) (lvl + 3);
			while (ll >= lvlMin || lh <= lvlMax)
			{
				if (ll >= lvlMin)
				{
					ar = getActiveRole(ll);
					lst.addAll(ar.get(
							MAX_GET_COUNT
									- lst.size(),
							idmap, excludes));
					if (lst.size() >= MAX_GET_COUNT)
						return;
					--ll;
				}
				if (lh <= lvlMax)
				{
					ar = getActiveRole(lh);
					lst.addAll(ar.get(
							MAX_GET_COUNT
									- lst.size(),
							idmap, excludes));
					if (lst.size() >= MAX_GET_COUNT)
						return;
					++lh;
				}
			}
		}

		Map<Integer, ActiveRole>	idmap	= new TreeMap<Integer, ActiveRole>();
		ActiveRole[]			array	= new ActiveRole[GameData.MAX_ROLE_LEVEL_CONFIG];
	}

	public static class WorldChatCache
	{
		public final static int	MAX_SIZE	= 10;

		public static class Msg
		{
			public static String encodeForceJoin(String rname)
			{
				return "#join#" + rname + "#";
			}

			public static String encodeEgg(byte eggID, short gid)
			{
				return "#egg#" + eggID + "#" + gid + "#";
			}

			public int	roleid;
			public byte	lvlVIP;
			public String	rolename;
			public String	forcename;
			public short	lvl;
			public short	headIconID;
			public int	time;
			public String	msg;
		}

		public synchronized List<Msg> getMsg(int time)
		{
			List<Msg> r = new ArrayList<Msg>();
			for (Msg m : msgs)
			{
				if (m.time > time)
					r.add(m);
			}
			return r;
		}

		public void addMsg(Msg m)
		{
			synchronized (this)
			{
				msgs.add(m);
				if (msgs.size() > MAX_SIZE)
					msgs.removeFirst();
			}
		}

		public int getStamp()
		{
			synchronized (this)
			{
				if (msgs.isEmpty())
					return 0;
				return msgs.get(msgs.size() - 1).time;
			}
		}

		LinkedList<Msg>	msgs	= new LinkedList<Msg>();
	}

	public interface VisitAllRolesCallback
	{
		public void onCallback(DBRole dbRole);
	}

	public class VisitAllRolesTrans implements Transaction
	{

		public VisitAllRolesTrans(VisitAllRolesCallback cb)
		{
			this.cb = cb;
		}

		@Override
		public boolean doTransaction()
		{
			Iterator<TableEntry<Integer, DBRole>> it = role.iterator();
			while(it.hasNext()){
				TableEntry<Integer, DBRole> entry = (TableEntry<Integer, DBRole>)it.next();
				if (cb != null)
					cb.onCallback(entry.getValue());
			}

			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;

		public final VisitAllRolesCallback	cb;
	}

	public class GenUserIDTrans implements Transaction
	{
		public GenUserIDTrans(int sid)
		{
			this.sid = sid;
		}

		@Override
		public boolean doTransaction()
		{
			Integer maxid = maxids.get("autouserid");
			if (maxid == null)
				userID = 1;
			else
				userID = maxid + 1;
			maxids.put("autouserid", userID);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			gs.getRPCManager().notifyAutoUserID(sid, userID);
		}

		@AutoInit
		public Table<String, Integer>	maxids;

		public final int		sid;
		public int			userID	= -1;
	}

	public class ExpatriateUserTrans implements Transaction
	{

		public ExpatriateUserTrans(int sid, int srcgs, int tag, int id,
				DBRole srcDBRole)
		{
			this.sid = sid;
			this.srcgs = srcgs;
			this.resTag = tag;
			this.resId = id;
			this.newRole = srcDBRole;
			this.openID = srcDBRole.openID;
			this.newname = new String(newRole.userName.getBytes());
			this.headIconID = newRole.headIconID;
		}

		@Override
		public boolean doTransaction()
		{
			final int now = gs.getTime();
			DBUser userinfo = user.get(newname);
			DBRole oldRole = null;
			if (userinfo != null)
			{
				oldRoleID = userinfo.roleID;
				roleID = userinfo.roleID;
				bUserExist = true;
				// userinfo.lastLoginKey = newRole. req.key;
				userinfo.lastLoginTime = now; // this time may
								// be not
								// necessary
				oldRole = role.get(roleID);
				userinfo.roleID = newRole.id;
			} else
			{
				userinfo = new DBUser();
				userinfo.lastLoginTime = now;
				// here the key will implements later
				// userinfo.lastLoginKey = "unknowKey";
				userinfo.createTime = now;
				userinfo.lastLoginTime = now;
			}

			user.put(newRole.userName, userinfo);

			// 锟斤拷锟矫匡拷锟阶拷锟斤拷锟�
			final int dayStartTime = now - now % 86400;
			Integer regToday = dayreg.get(dayStartTime);
			int iRegToday = regToday == null ? 0 : regToday
					.intValue();
			int iRegTodayLimit = LoginManager.this
					.getCurDayRegisterLimit();

			if (iRegTodayLimit > 0 && iRegToday >= iRegTodayLimit)
			{
//				bRegLimit = true;
//				errCreate = GameData.EXPATRIATE_REGLIMIT;
//				return true;
			}

			Integer maxid = maxids.get("roleid");
			if (maxid == null)
			{
				newRoleID = 1;
			} else
			{
				newRoleID = maxid + 1;
			}
			Integer rid = rolename.get(newname);
			if (rid != null && rid != 0)
			{
//				bRoleNameHasExist = true;
//				errCreate = GameData.EXPATRIATE_ROLENAME_CONFLICT;
				oldRoleID = rid;
			}
			userinfo.roleID = newRoleID;
			newRole.id = newRoleID;

			dayreg.put(dayStartTime, iRegToday + 1);
			maxids.put("roleid", newRoleID);
			
			int midasDiff = 0;
			if (oldRole != null)
				midasDiff = newRole.stonePayMidas - oldRole.stonePayMidas;
			else
				midasDiff = newRole.stonePayMidas;
			newRole.stonePayMidas -= midasDiff;
			newRole.stonePayAlpha += midasDiff;

			newRole.clearFriendFlag(Role.ROLE_FLAG_CREATE_EXCHANGE);
			
			newRole.forceID=0;
			newRole.forceContrib=0;
			newRole.forceJoinTime=0;
			newRole.forceIconId = 0;
			if(newRole.forceMobai!=null){
			   newRole.forceMobai.clear();
			}			
			newRole.forceMobaiReward=0;
			newRole.forceName="";
//			newRole.forcePoint=0;
			newRole.forceSuperMobai=0;
			
			if(newRole.arenaLogs!=null){
				newRole.arenaLogs.clear();
			}
			
//			if(newRole.arenaState!=null){
//				newRole.arenaState = new SBean.DBRoleArenaState();
//			}
			
			
			if(newRole.superArena!=null&&newRole.superArena.size()>0
					&&newRole.superArena.get(0)!=null
					&&newRole.superArena.get(0).logs!=null){
				newRole.superArena.get(0).logs.clear();
			}
			
			if(newRole.heroesBossInfo!=null){
				newRole.heroesBossInfo.clear();
			}
			
			newRole.stone-=GameData.expirateCost;
			
			for(int i =1 ;i<10;i++){
				Integer ridq = rolename.get(newRole.name);
				if (ridq != null && ridq != 0)
				{
					newRole.name=newRole.name+i;
				}else{
					break;
				}
			}
			
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			int days = GameData.getInstance().getchangeServerDay(srcgs, gs.cfg.id);
			if(days>0){
				for(ChangServerItemsCFGS item: GameData.getInstance().changeServer.changServerItems){
					if(item.itemType==1){
//						newRole.gmAddItem(item.iid, item.icount*days);
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)item.iid, item.icount*days));
					}else if(item.itemType==2){
//						newRole.gmAddEquip(item.iid, item.icount*days);
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)item.iid, item.icount*days));
					}
				}
			}
			
			user.put(newname, userinfo);
			role.put(newRoleID, newRole);
			mail.put(newRoleID, new DBMail());
			rolename.put(newRole.name, newRoleID);
						
			sendExpatriatetWorldMsg();
            if (days>0&&attLst.size()>0) {
				
				gs.getLoginManager().sysSendMessage(
						0, newRoleID, DBMail.Message.SUB_TYPE_EXPATRIATE_REWARD, "", "", 0, true, attLst);				
			}
            
            gs.getLogger().warn("ExpatriateUserTrans  newname:"+newname+" newRoleID:"+newRoleID+" srcgsId:"+srcgs+" togsId:"+gs.cfg.id+
			" oldRoleID:"+oldRoleID+" time:"+gs.getTime());
			return true;
		}

		private void sendExpatriatetWorldMsg() {
			Msg m = new Msg();
			m.msg =  "#expatriate#";//"锟斤拷喜"+newRole.name+",锟斤拷越锟斤拷锟斤拷锟斤拷锟界。";
			m.roleid = newRole.id;
			m.lvlVIP = newRole.lvlVIP;
			m.rolename = newRole.name;
			m.forcename = "";
			m.lvl = newRole.lvl;
			m.headIconID = headIconID;
			m.time = gs.getTime();
			gs.getLoginManager().addWorldChatMsg(m);
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK)
			{
				if (errCreate == GameData.EXPATRIATE_OK)
				{
					gs.getLogger()
							.warn("expatriate role success! "
									+ newname
									+ " ok");
					gs.getRPCManager()
							.exchangeSendExpatriateTransRoleResponse(
									errCreate,
									openID,
									srcgs,
									resTag,
									resId,
									"Expatriate Successed");
					return;
				}

				if (errCreate == GameData.EXPATRIATE_REGLIMIT)
				{
					gs.getLogger()
							.warn("expatriate role  "
									+ newname
									+ " failed, expatriate_reglimit");
					gs.getRPCManager()
							.exchangeSendExpatriateTransRoleResponse(
									errCreate,
									openID,
									srcgs,
									resTag,
									resId,
									"day register num limit");
					return;
				}

				if (errCreate == GameData.EXPATRIATE_ROLENAME_CONFLICT)
				{
					gs.getLogger()
							.warn("expatriate role  "
									+ newname
									+ " failed,  expatriate_rolename_conflict ");
					gs.getRPCManager()
							.exchangeSendExpatriateTransRoleResponse(
									errCreate,
									openID,
									srcgs,
									resTag,
									resId,
									"has a role name conflict error");
					return;
				}
			} else
			{
				gs.getLogger().warn("do transaction error");
				gs.getRPCManager()
						.exchangeSendExpatriateTransRoleResponse(
								GameData.EXPATRIATE_TRANSACTIONERROR,
								openID, srcgs,
								resTag, resId,
								"expriate transaction error");
			}
		}

		@AutoInit
		public Table<String, Integer>	maxids;
		@AutoInit
		public Table<Integer, Integer>	dayreg;
		@AutoInit
		public Table<String, DBUser>	user;
		@AutoInit
		public Table<Integer, DBRole>	role;
		@AutoInit
		public Table<Integer, DBMail>	mail;
		@AutoInit
		public Table<String, Integer>	rolename;

		public final int		sid;
		public final int		srcgs;
		private int			resTag;
		private int			resId;
		public  String		newname;
		public final String		openID;
		public final short		headIconID;
		public boolean			bRegLimit		= false;
		public boolean			bRoleNameHasExist	= false;
		public byte			errCreate;
		public boolean			bUserExist		= false;
		public int			oldRoleID		= 0;
		public int			roleID;
		public DBRole			newRole;
		public int			newRoleID;
	}

	public class LoadUserTrans implements Transaction
	{
		public LoadUserTrans(int sid, SBean.UserLoginRequest req,
				Role.MSDKInfo msdkInfo, String newname,
				short headIconID)
		{
			this.sid = sid;
			this.req = req;
			this.msdkInfo = msdkInfo;
			this.newname = newname;
			this.headIconID = headIconID;
		}

		@Override
		public boolean doTransaction()
		{
			final int now = gs.getTime();
			DBUser userinfo = user.get(req.userName);
			if (userinfo != null)
			{
				if (req.loginType == SBean.UserLoginRequest.eLoginNormal
						|| req.loginType == SBean.UserLoginRequest.eLoginGod)
				{
					roleID = userinfo.roleID;

					// Temp: Remove THIS!!!!
					/*
					 * if (roleID == 34199) roleID = 227;
					 * else if (roleID == 34200) roleID =
					 * 18098;
					 */

					bUserExist = true;
					userinfo.lastLoginKey = req.key;
					userinfo.lastLoginTime = now;
					user.put(req.userName, userinfo);
					return true;
				} else if (req.loginType == SBean.UserLoginRequest.eLoginReconnect)
				{
					roleID = userinfo.roleID;

					// Temp: Remove THIS!!!!
					/*
					 * if (roleID == 34199) roleID = 227;
					 * else if (roleID == 34200) roleID =
					 * 18098;
					 */

					bUserExist = true;
					if (!userinfo.lastLoginKey.equals(""))
					{
						if (!userinfo.lastLoginKey
								.equals(req.key)
								|| userinfo.lastLoginTime + 86400/* TODO */< now)
							return false;
					}
					return true;
				}
				return false;
			}
			// 锟斤拷锟矫匡拷锟阶拷锟斤拷锟�
			final int dayStartTime = now - now % 86400;
			Integer regToday = dayreg.get(dayStartTime);
			int iRegToday = regToday == null ? 0 : regToday
					.intValue();

			// int iRegTodayLimit = gs.getDayRegLimit(dayStartTime);
			int iRegTodayLimit = LoginManager.this
					.getCurDayRegisterLimit();
			// System.out.println("day: " + dayStartTime);

			if (iRegTodayLimit > 0 && iRegToday >= iRegTodayLimit)
			{
				bRegLimit = true;
				return true;
			}
			//
			if (newname == null)
			{
				bNeedCreate = true;
				errCreate = GameData.CREATEROLE_NOTFOUND;
				return true;
			}
			Integer rid = rolename.get(newname);
			if (rid != null && rid != 0)
			{
				bNeedCreate = true;
				errCreate = GameData.CREATEROLE_EXIST;
				return true;
			}
			if (req.loginType == SBean.UserLoginRequest.eLoginReconnect)
				return false;
			userinfo = new DBUser();
			userinfo.lastLoginTime = now;
			userinfo.lastLoginKey = req.key;
			Integer maxid = maxids.get("roleid");
			if (maxid == null)
				userinfo.roleID = 1;
			else
				userinfo.roleID = maxid + 1;
			dayreg.put(dayStartTime, iRegToday + 1);
			userinfo.createTime = now;
			userinfo.lastLoginTime = now;
			maxids.put("roleid", userinfo.roleID);
			user.put(req.userName, userinfo);
			roleinfo = new Role(userinfo.roleID, gs, sid)
					.createRole(newname, headIconID,
							req.userName,
							msdkInfo.openID);
			role.put(roleinfo.id, roleinfo.copyDBRoleWithoutLock());
			mail.put(roleinfo.id, new DBMail());
			rolename.put(roleinfo.name, roleinfo.id);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK)
			{
				if (bUserExist)
				{
					Role role = mapRoles.putIfAbsent(
							roleID, Role.NULL_ROLE);
					if (role != null) // lock fail
					{
						if (!role.isNull())
						{
							if (role.isBanLogin())
							{
								int banLeftTime = role
										.getBanLoginLeftTime();
								String banReason = role
										.getBanLoginReason();
								gs.getLogger()
										.warn("load username "
												+ req.userName
												+ " failed! user is banned leftTime "
												+ banLeftTime
												+ " for reason : "
												+ banReason);
								gs.getRPCManager()
										.sendLuaPacket(sid,
												LuaPacket.encodeUserLogin(
														GameData.USERLOGIN_BAN,
														banLeftTime,
														banReason));
							} else
							{
								int oldsid = role
										.changeSID(sid);
								if (oldsid != sid)
								{
									if (oldsid != 0) // kick
												// another
									{
										maps2r.remove(oldsid);
										gs.getRPCManager()
												.forceClose(oldsid,
														GameData.FORCE_CLOSE_REPLACE);
										// gs.getRPCManager().closeSession(oldsid);
									}
									maps2r.put(sid,
											role.id);
									role.onLoadOK(req.userName,
											Role.ROLE_LOAD_FROM_MEMORY,
											msdkInfo);
								}
							}
						} else
						{
							gs.getLogger()
									.debug("load username "
											+ req.userName
											+ " failed");
							gs.getRPCManager()
									.sendLuaPacket(sid,
											LuaPacket.encodeUserLogin(
													GameData.USERLOGIN_BUSY,
													(byte) 0,
													""));
						}
					} else
					// lock ok, load from db
					{
						gs.getDB()
								.execute(new LoadRoleTrans(
										sid,
										req.userName,
										roleID,
										msdkInfo));
					}
				} else
				{
					if (bRegLimit)
					{
						gs.getRPCManager()
								.sendLuaPacket(sid,
										LuaPacket.encodeUserLogin(
												GameData.USERLOGIN_REG_FULL,
												(byte) 0,
												""));
					} else if (bNeedCreate)
					{
						gs.getRPCManager()
								.sendLuaPacket(sid,
										LuaPacket.encodeCreateRole(errCreate));
					} else
					// create ok
					{
						mapRoles.put(roleinfo.id,
								roleinfo);
						setRegNum(roleinfo.id);
						maps2r.put(sid, roleinfo.id);
						roleinfo.onLoadOK(req.userName,
								Role.ROLE_NEW,
								msdkInfo);
					}
				}
			} else
			{
				gs.getLogger().debug(
						"load username " + req.userName
								+ " failed");
				gs.getRPCManager()
						.sendLuaPacket(sid,
								LuaPacket.encodeUserLogin(
										GameData.USERLOGIN_FAILED,
										(byte) 0,
										""));
			}
		}

		@AutoInit
		public Table<String, Integer>		maxids;
		@AutoInit
		public Table<Integer, Integer>		dayreg;
		@AutoInit
		public Table<String, DBUser>		user;
		@AutoInit
		public Table<Integer, DBRole>		role;
		@AutoInit
		public Table<Integer, DBMail>		mail;
		@AutoInit
		public Table<String, Integer>		rolename;

		public final int			sid;
		public final SBean.UserLoginRequest	req;
		public final Role.MSDKInfo		msdkInfo;
		public final String			newname;
		public final short			headIconID;
		public Role				roleinfo;
		public boolean				bRegLimit	= false;
		public boolean				bNeedCreate	= false;
		public byte				errCreate;
		public boolean				bUserExist	= false;
		public int				roleID;
	}

	public interface QueryNameCallback
	{
		public void onCallback(String name, Integer val);
	}

	public class QueryNameTrans implements Transaction
	{
		public QueryNameTrans(String name, QueryNameCallback callback)
		{
			this.name = name;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			val = rolename.get(name);
			if (val != null && val == 0)
				val = null;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(name, val);
		}

		@AutoInit
		public TableReadonly<String, Integer>	rolename;

		public final String			name;
		public Integer				val;
		public final QueryNameCallback		callback;
	}

	public class GetRoleIDByRoleNameTrans implements Transaction
	{
		public GetRoleIDByRoleNameTrans(String roleName,
				GetRoleIDByRoleNameCallback callback)
		{
			this.roleName = roleName;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			roleID = rolename.get(roleName);
			if (roleID != null && roleID == 0)
				roleID = null;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(roleName, roleID);
		}

		@AutoInit
		public TableReadonly<String, Integer>		rolename;

		public final String				roleName;
		public Integer					roleID;
		public final GetRoleIDByRoleNameCallback	callback;
	}

	public class GetRoleIDByUserNameTrans implements Transaction
	{
		public GetRoleIDByUserNameTrans(String userName,
				GetRoleIDByUserNameCallback callback)
		{
			this.userName = userName;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBUser dbuser = user.get(userName);
			if (dbuser != null)
				roleID = dbuser.roleID;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(userName, roleID);
		}

		@AutoInit
		public TableReadonly<String, DBUser>		user;

		public final String				userName;
		public Integer					roleID;
		public final GetRoleIDByUserNameCallback	callback;
	}

	public class ChangeRoleNameByRoleIDTrans implements Transaction
	{
		public ChangeRoleNameByRoleIDTrans(int rid, String oldName,
				String newName,
				ChangeRoleNameByRoleIDCallback callback)
		{
			this.roleID = rid;
			this.oldName = oldName;
			this.newName = newName;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			Integer rid = rolename.get(oldName);
			if (rid == null || rid != roleID)
			{
				newName = null;
				return true;
			}
			rid = rolename.get(newName);
			if (rid == null || rid == 0)
			{
				rolename.put(newName, roleID);
				rolename.put(oldName, 0);
			} else
				newName = null;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(newName);
		}

		@AutoInit
		public Table<String, Integer>			rolename;

		public final String				oldName;
		public String					newName;
		public final int				roleID;
		public final ChangeRoleNameByRoleIDCallback	callback;
	}

	interface PutRoleNameIfAbsentCallback
	{
		public void onCallback(boolean bOK);
	}

	public class PutRoleNameIfAbsentTrans implements Transaction
	{
		public PutRoleNameIfAbsentTrans(int rid, String newName,
				PutRoleNameIfAbsentCallback callback)
		{
			this.rid = rid;
			this.newName = newName;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			Integer roleID = rolename.get(newName);
			if (roleID != null && roleID != 0 && roleID != rid)
				return false;

			rolename.put(newName, rid);

			return true;
		}

		@AutoInit
		public Table<String, Integer>			rolename;
		public final int				rid;
		public final String				newName;
		public final PutRoleNameIfAbsentCallback	callback;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK);
		}
	}

	public class GetRoleBriefCacheDataTrans implements Transaction
	{
		public GetRoleBriefCacheDataTrans(List<Integer> rids,
				GetDataBriefCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			data = new ArrayList<RoleBriefCachePair>();
			for (int rid : rids)
			{
				RoleBriefCachePair e = new RoleBriefCachePair(
						rid, null);
				DBRole dbrole = role.get(rid);
				if (dbrole != null){					
					e.cache = dbrole.getCacheData(gs
							.getGameData());
					if(dbrole.activityRefresh(gs.getDayCommon())&&e.cache!=null){
						e.cache.activity=0;
					}
				}
				else {
					e.cache = new RoleBriefCache((short)1, (short)15013, 0, "", "", 0, 0);
				}
					
				data.add(e);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(data);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;

		public final List<Integer>		rids;
		public List<RoleBriefCachePair>		data;
		public final GetDataBriefCallback	callback;
	}
	
	public void doHeroesBossTran(List<Integer> rids,HeroesBossFinishAttRes res){
		gs.getDB().execute(new HeroesBossFinishTrans(rids, res,new LoginManager.HeroesBossFinishTransCallback(
				) {
			
				@Override
				public void onCallback(ErrorCode bOK) {
					gs.getLogger().info("HeroesBossFinishTrans "+bOK) ;
				}
		}));
	}
	
	public void doHeroesBossSyncTran(List<Integer> rids,HeroesBossSyncRes res){
		gs.getDB().execute(new HeroesBossSyncTrans(rids, res,new LoginManager.HeroesBossSyncTransCallback(
				) {
			
				@Override
				public void onCallback(ErrorCode bOK) {
					gs.getLogger().info("HeroesBossFinishTrans "+bOK) ;
				}
		}));
	}
	
	public void doHeroesBossBonusTran(List<Integer> rids){
		gs.getDB().execute(new HeroesBossBonusTrans(rids,new LoginManager.HeroesBossSyncTransCallback(
				) {
			
				@Override
				public void onCallback(ErrorCode bOK) {
					gs.getLogger().info("doHeroesBossBonusTran "+bOK) ;
				}
		}));
	}
	
	public class HeroesBossSyncTrans implements Transaction
	{
		public HeroesBossSyncTrans(List<Integer> rids,HeroesBossSyncRes res, HeroesBossSyncTransCallback callback)
		{
			this.rids = rids;
			this.res = res;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			for(int rid : rids)
			{
				DBRole dbrole = role.get(rid);
				if( dbrole != null ){
					if(dbrole.heroesBossInfo!=null&&
							dbrole.heroesBossInfo.size()>0&&dbrole.heroesBossInfo.get(0)!=null&&
							res.ranks!=null&&
							res.ranks.size()>0){
						if(dbrole.id==res.roleID.roleID&&dbrole.heroesBossInfo==null){
							dbrole.heroesBossInfo= new ArrayList< DBHeroesBossInfo>();
							dbrole.heroesBossInfo.add(new DBHeroesBossInfo(new ArrayList<HeroesRank>(), 
									new ArrayList<DBHeroesBossTimes> (), (short) 0, 0, 
			                        0, 0, 0, 0, 0))
									;
						}

						if(dbrole.heroesBossInfo!=null&&res.ranks!=null&&
								res.ranks.size()>0){
							dbrole.heroesBossInfo.get(0).ranks.clear();
							for(HeroesRank tant:res.ranks){
								if(tant!=null){
									dbrole.heroesBossInfo.get(0).ranks.add(tant.kdClone());
								}								
							}
//							dbrole.heroesBossInfo.get(0).ranks = res.ranks;
							role.put(rid, dbrole);
						}
					}
					   
				}			
				else
					gs.getLogger().warn("HHeroesBossSyncTrans rid=" + rid + "doesn't exist!!!");		
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode);
		}
		
		@AutoInit
		public Table<Integer, DBRole> role;
		
		public final List<Integer> rids;
	
		public HeroesBossSyncRes res;
		public final HeroesBossSyncTransCallback callback;
	}
	
	
	public class HeroesBossBonusTrans implements Transaction
	{
		public HeroesBossBonusTrans(List<Integer> rids, HeroesBossSyncTransCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			for(int rid : rids)
			{
				DBRole dbrole = role.get(rid);
				if( dbrole != null ){
					if(dbrole.heroesBossInfo!=null&&
							dbrole.heroesBossInfo.size()>0&&dbrole.heroesBossInfo.get(0)!=null){
						List<HeroesRank> r2heroes = dbrole.heroesBossInfo.get(0).ranks;	
						short rank = HeroesBossManager.getHeroesBonusRanks(dbrole.id,r2heroes);
						HeroesBossItemsCFGS bonus = gs.getGameData().getHeroesBossBonusCFG(rank);
						if(bonus!=null&&bonus.eItems.size()>0){
		   		   			gs.getLoginManager().sysSendMessage(0, dbrole.id, DBMail.Message.SUB_TYPE_HEROESBOSS_REWARD, "", ""+(short)rank, 0, true,bonus.eItems);	    
		   		   	    } 
						dbrole.setHeroesBossInfo();
   		   			    role.put(rid, dbrole);
					}
					   
				}			
				else
					gs.getLogger().warn("HHeroesBossSyncTrans rid=" + rid + "doesn't exist!!!");		
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
//			callback.onCallback(errcode);
		}
		
		@AutoInit
		public Table<Integer, DBRole> role;
		
		public final List<Integer> rids;
	
		public final HeroesBossSyncTransCallback callback;
	}
	
	interface HeroesBossSyncTransCallback
	{
		public void onCallback(ErrorCode bOK);
	}
	
	
	interface HeroesBossFinishTransCallback
	{
		public void onCallback(ErrorCode bOK);
	}
	
	public class HeroesBossFinishTrans implements Transaction
	{
		public HeroesBossFinishTrans(List<Integer> rids,HeroesBossFinishAttRes res, HeroesBossFinishTransCallback callback)
		{
			this.rids = rids;
			this.res = res;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			for(int rid : rids)
			{
				DBRole dbrole = role.get(rid);
				if( dbrole != null ){
					if(dbrole.heroesBossInfo!=null&&
							dbrole.heroesBossInfo.size()>0&&dbrole.heroesBossInfo.get(0)!=null){
						List<HeroesRank> r2heroes = dbrole.heroesBossInfo.get(0).ranks;
						HeroesRank rankt = null;
						for(HeroesRank rank :  r2heroes){
							
							if(rank.rid==res.roleID.roleID){
								rankt = rank;
							}
						}
						
						if(rankt!=null){
							if(res.rank!=null){
								if(res.type2==1){
									rankt.score1=res.rank.score1;
								}else if(res.type2==2){
									rankt.score2=res.rank.score2;
								}else if(res.type2==3){
									rankt.score3=res.rank.score3;
								}
								rankt.headIconId = res.rank.headIconId;
								rankt.power = res.rank.power;
								rankt.lvl = res.rank.lvl;
								rankt.buffLvl = res.rank.buffLvl;
								rankt.attc = res.rank.attc;
							}else{
								if(res.type2==1){
									rankt.score1=res.score;
								}else if(res.type2==2){
									rankt.score2=res.score;
								}else if(res.type2==3){
									rankt.score3=res.score;
								}
							}
							
							role.put(rid, dbrole);
						}
					}
					   
				}			
				else
					gs.getLogger().warn("HeroesBossFinishTrans rid=" + rid + "doesn't exist!!!");		
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode);
		}
		
		@AutoInit
		public Table<Integer, DBRole> role;
		
		public final List<Integer> rids;
	
		public HeroesBossFinishAttRes res;
		public final HeroesBossFinishTransCallback callback;
	}
	

	public class GetPVPCacheDataTrans implements Transaction
	{
		public GetPVPCacheDataTrans(List<Integer> rids,
				GetPVPStateCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			data = new ArrayList<PVPCache>();
			for (int rid : rids)
			{
				PVPCache e = new PVPCache(rid, 0);
				DBRole dbrole = role.get(rid);
				if (dbrole != null)
					e.flag = 0;
				data.add(e);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(data);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;

		public final List<Integer>		rids;
		public List<PVPCache>			data;
		public final GetPVPStateCallback	callback;
	}

	public class GetRolesDataTrans implements Transaction
	{
		public GetRolesDataTrans(int qtype, List<Integer> rids,
				GetRolesDataCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
			this.qtype = qtype;
		}

		@Override
		public boolean doTransaction()
		{
			data = new ArrayList<SBean.RoleDataRes>();
			for (int rid : rids)
			{
				SBean.RoleDataRes res = new SBean.RoleDataRes();
				res.res = SBean.RoleDataRes.eFailed;
				res.req = new SBean.RoleDataReq(qtype, rid);
				DBRole dbrole = role.get(rid);
				if (dbrole != null)
					dbrole.getData(res);
				data.add(res);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(data);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;

		public final List<Integer>		rids;
		public List<SBean.RoleDataRes>		data;
		public final GetRolesDataCallback	callback;
		public int				qtype;
	}

	public class GetMarchEnemiesTrans implements Transaction
	{
		public GetMarchEnemiesTrans(List<Integer> rids,
				GetMarchEnemiesCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			data = new ArrayList<SBean.DBRoleMarchEnemy>();
			for (int rid : rids)
			{
				SBean.DBRoleMarchEnemy res = new SBean.DBRoleMarchEnemy();
				DBRole dbrole = role.get(rid);
				if (dbrole != null)
					dbrole.getMarchEnemy(
							gs.getMarchManager(),
							res);
				else
					gs.getLogger()
							.warn("GetMarchEnemiesTrans rid="
									+ rid
									+ "doesn't exist!!!");

				data.add(res);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(data);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;

		public final List<Integer>		rids;
		public List<SBean.DBRoleMarchEnemy>	data;
		public final GetMarchEnemiesCallback	callback;
	}

	public class GetSuraEnemiesTrans implements Transaction
	{
		public GetSuraEnemiesTrans(List<Integer> rids, GetSuraEnemiesCallback callback)
		{
			this.rids = rids;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			data = new ArrayList<SBean.DBSuraEnemy>();
			for(int rid : rids)
			{
				SBean.DBSuraEnemy res = new SBean.DBSuraEnemy();
				DBRole dbrole = role.get(rid);
				if( dbrole != null )
					dbrole.getSuraEnemy(res);
				else
					gs.getLogger().warn("GetMarchEnemiesTrans rid=" + rid + "doesn't exist!!!");

				data.add(res);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(data);
		}
		
		@AutoInit
		public TableReadonly<Integer, DBRole> role;
		
		public final List<Integer> rids;
		public List<SBean.DBSuraEnemy> data;
		public final GetSuraEnemiesCallback callback;
	}

	public class ModRoleTrans implements Transaction
	{

		public ModRoleTrans(GameServer gs, int sid,
				SBean.DataModifyReq req)
		{
			this.req = req;
			this.gs = gs;
			this.sid = sid;
			res = new SBean.DataModifyRes();
			res.res = SBean.DataModifyRes.eFailed;
			res.req = req;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbRole = role.get(req.rid);
			if (dbRole == null)
				return false;
			res.res = SBean.DataModifyRes.eOK;
			switch (req.dtype) {
			case SBean.DataModifyReq.eStone:
			{
				res.oldVal = dbRole.stone;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					dbRole.stone = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (dbRole.stone < 0)
					dbRole.stone = 0;
				res.newVal = dbRole.stone;
			}
				break;
			case SBean.DataModifyReq.eMoney:
			{
				res.oldVal = dbRole.money;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					dbRole.money = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (dbRole.money < 0)
					dbRole.money = 0;
				res.newVal = dbRole.money;
			}
				break;
			case SBean.DataModifyReq.eForceScore:
			{
				res.oldVal = dbRole.forcePoint;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					dbRole.forcePoint = req.val;
					break;
				case SBean.DataModifyReq.eAdd:
					dbRole.forcePoint += req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (dbRole.forcePoint < 0)
					dbRole.forcePoint = 0;
				res.newVal = dbRole.forcePoint;
			}
				break;
			case SBean.DataModifyReq.eForceID:
			{
				res.oldVal = dbRole.forceID;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					if (req.id == 0
							&& dbRole.forceID == req.val)
					{
						dbRole.forceID = 0;
						dbRole.forceName = "";
					}
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				res.newVal = dbRole.forceID;
			}
				break;
			case SBean.DataModifyReq.eLevel:
			{
				res.oldVal = dbRole.lvl;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					dbRole.lvl = (byte) req.val;
					break;
				case SBean.DataModifyReq.eAdd:
					dbRole.lvl += req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (dbRole.lvl < 1)
					dbRole.lvl = 1;
				if (dbRole.lvl > gs.getRoleLevelLimit())
					dbRole.lvl = gs.getRoleLevelLimit();
				dbRole.vit = gs.getGameData().getVitMax(
						dbRole.lvl);
				res.newVal = dbRole.lvl;
			}
				break;
			case SBean.DataModifyReq.eItem:
			{
				SBean.ItemCFGS cfg = gs.getGameData()
						.getItemCFG(req.id);
				if (cfg == null)
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				SBean.DBRoleItem dbitem = null;
				for (SBean.DBRoleItem e : dbRole.items)
				{
					if (e.id == req.id)
					{
						dbitem = e;
						break;
					}
				}
				if (dbitem == null)
					res.oldVal = 0;
				else
					res.oldVal = dbitem.count;
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (newcnt < 0)
					newcnt = 0;
				if (newcnt > GameData.MAX_ITEM_COUNT)
					newcnt = GameData.MAX_ITEM_COUNT;
				res.newVal = newcnt;
				if (newcnt == 0)
				{
					if (res.oldVal != 0)
						dbRole.items.remove(dbitem);
				} else
				{
					if (dbitem != null)
						dbitem.count = (short) newcnt;
					else
						dbRole.items.add(new SBean.DBRoleItem(
								req.id,
								(short) newcnt));
				}
			}
				break;
			case SBean.DataModifyReq.eEquip:
			{
				SBean.EquipCFGS cfg = gs.getGameData()
						.getEquipCFG(req.id);
				if (cfg == null)
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				SBean.DBRoleEquip dbequip = null;
				for (SBean.DBRoleEquip e : dbRole.equips)
				{
					if (e.id == req.id)
					{
						dbequip = e;
						break;
					}
				}
				if (dbequip == null)
					res.oldVal = 0;
				else
					res.oldVal = dbequip.count;
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (newcnt < 0)
					newcnt = 0;
				if (newcnt > GameData.MAX_EQUIP_COUNT)
					newcnt = GameData.MAX_EQUIP_COUNT;
				res.newVal = newcnt;
				if (newcnt == 0)
				{
					if (res.oldVal != 0)
						dbRole.equips.remove(dbequip);
				} else
				{
					if (dbequip != null)
						dbequip.count = (short) newcnt;
					else
						dbRole.equips.add(new SBean.DBRoleEquip(
								req.id,
								(short) newcnt));
				}
			}
				break;
			case SBean.DataModifyReq.eSeyen:
			{
				short gid = req.id;
				if( req.val < 0 || gid < 0 )
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}

				SBean.GeneralCFGS gcfg = null;
				if( gid != 0 )
				{
					gcfg = GameData.getInstance().getGeneralCFG(gid);
					if( gcfg == null )
					{
						res.res = SBean.DataModifyRes.eFailed;
						break;
					}
				}

				res.oldVal = 0;
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				
				int tempSeyen =0;
				for(DBRoleGeneral g : dbRole.generals)
				{
					if( g.id == gid || gid == 0 )
					{
						if(g.generalSeyen==null||g.generalSeyen.size()==0){
							g.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
							g.generalSeyen.add(new SBean.DBGeneralSeyen());
						}
						
						SBean.DBGeneralSeyen seyen1 = g.generalSeyen.get(0);
						if(gid==0){
							tempSeyen+=newcnt-seyen1.lvl;
						}else{
							tempSeyen=newcnt-seyen1.lvl;
						}
						if (g.id == gid)
							res.oldVal = seyen1.lvl;
						seyen1.lvl=(short)newcnt;
					}
				}
				if(tempSeyen!=0){
					for(DBRoleGeneral general : dbRole.generals){
						if(general!=null){
							if(general.generalSeyen==null||general.generalSeyen.size()==0){
								general.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
								general.generalSeyen.add(new SBean.DBGeneralSeyen());
							}
							general.generalSeyen.get(0).seyenTotal+=tempSeyen;
						}
					}
				}
				res.newVal = newcnt;
			}
				break;
			default:
				res.res = SBean.DataModifyRes.eFailed;
				break;
			}
			if (res.res == SBean.DataModifyRes.eOK)
				role.put(req.rid, dbRole);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
				res.res = SBean.DataModifyRes.eFailed;
			if (sid > 0)
				gs.getLoginManager().modifyDataCallback(sid,
						res);
			mapRoles.remove(req.rid);
		}

		@AutoInit
		public Table<Integer, DBRole>		role;
		@AutoInit
		public Table<Integer, DBMail>		mail;

		public final GameServer			gs;
		public final int			sid;
		public final SBean.DataModifyReq	req;
		public SBean.DataModifyRes		res;
	}

	public class AddWorldMailTrans implements Transaction
	{
		public AddWorldMailTrans(SBean.SysMail mail,
				AddWorldMailCallback callback)
		{
			this.mail = mail;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("worldMails");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.SysMail> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.SysMail>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.SysMail.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("add world mail server fail 3");
						return false;
					}
				}
				int now = gs.getTime();
				// mail.timeStart = now;

				//
				int newid = 1000;
				Integer maxid = maxids.get("worldmail");
				if (maxid != null)
					newid = maxid.intValue() + 1;
				maxids.put("worldmail", newid);
				//
				mail.id = newid;
				lst.add(mail);
				//
				{
					Iterator<SBean.SysMail> iter = lst
							.iterator();
					while (iter.hasNext())
					{
						SBean.SysMail e = iter.next();
						if (e.timeStart + e.lifeTime < now)
						{
							iter.remove();
							gs.getLogger()
									.debug("test world mail time expired remove, id="
											+ e.id
											+ ", send time="
											+ GameServer.getTimeStampStr(e.timeStart)
											+ ", expiration time="
											+ GameServer.getTimeStampStr(e.timeStart
													+ e.lifeTime)
											+ ",title="
											+ e.title);
						}
					}
				}
				//
				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				reslst = lst;
				gs.getLogger()
						.debug("add world mail server ok, id="
								+ newid
								+ ",expiration time="
								+ GameServer.getTimeStampStr(mail.timeStart
										+ mail.lifeTime)
								+ ",title="
								+ mail.title);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK && reslst != null)
			{
				setWorldMails(reslst);
				callback.onCallback(true);
			} else
			{
				System.out.println("add world mail server fail 4");
				callback.onCallback(false);
			}
		}

		@AutoInit
		public Table<byte[], byte[]>		world;
		@AutoInit
		public Table<String, Integer>		maxids;

		public final SBean.SysMail		mail;
		public final AddWorldMailCallback	callback;
		public List<SBean.SysMail>		reslst;

	}

	public interface AddDelayMailCallback
	{
		public void onCallback(int errcode);
	}

	public class AddDelayMailTrans implements Transaction
	{
		public AddDelayMailTrans(SBean.SysMail delayMail,
				AddDelayMailCallback callback)
		{
			this.delayMail = delayMail;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("delayMails");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.SysMail> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.SysMail>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.SysMail.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("add delay mail server fail 3");
						return false;
					}
				}
				lst.add(delayMail);
				//
				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				reslst = lst;
				gs.getLogger()
						.info("add delay mail server ok, firetime="
								+ GameServer.getTimeStampStr(delayMail.timeStart)
								+ ",title="
								+ delayMail.title);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK && reslst != null)
			{
				// setDelayMails(reslst);
				callback.onCallback(errcode.ordinal());
			} else
			{
				gs.getLogger().warn(
						"add delay mail server fail 4");
				callback.onCallback(errcode.ordinal());
			}
		}

		@AutoInit
		public Table<byte[], byte[]>		world;

		public final SBean.SysMail		delayMail;
		public final AddDelayMailCallback	callback;
		public List<SBean.SysMail>		reslst;

	}

	public class PopDelayMailTrans implements Transaction
	{

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("delayMails");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.SysMail> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.SysMail>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.SysMail.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("pop delay mail server fail 3");
						return false;
					}
				}
				int now = gs.getTime();
				List<SBean.SysMail> timeoutMails = new ArrayList<SBean.SysMail>();
				Iterator<SBean.SysMail> iter = lst.iterator();
				while (iter.hasNext())
				{
					SBean.SysMail e = iter.next();
					if (now >= e.timeStart)
					{
						iter.remove();
						timeoutMails.add(e);
					}
				}

				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				reslst = timeoutMails;
				if (!timeoutMails.isEmpty())
					gs.getLogger()
							.debug("pop delay mail server ok, pop "
									+ timeoutMails.size()
									+ " world mails");
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK && reslst != null)
			{
				sendDelayMails(reslst);
			} else
			{
				gs.getLogger().warn(
						"pop delay mail server fail 4");
			}
		}

		@AutoInit
		public Table<byte[], byte[]>	world;

		public List<SBean.SysMail>	reslst;

	}

	public interface AddBroadcastCallback
	{
		public void onCallback(int errcode);
	}

	public class AddBroadcastTrans implements Transaction
	{
		public AddBroadcastTrans(SBean.Broadcast broadcast,
				AddBroadcastCallback callback)
		{
			this.broadcast = broadcast;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("broadcasts");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.Broadcast> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.Broadcast>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.Broadcast.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("add broadcast server fail 3");
						return false;
					}
				}
				int now = gs.getTime();

				int newid = 1000;
				Integer maxid = maxids.get("broadcast");
				if (maxid != null)
					newid = maxid.intValue() + 1;
				maxids.put("broadcast", newid);
				broadcast.id = newid;

				lst.add(broadcast);
				{
					Iterator<SBean.Broadcast> iter = lst
							.iterator();
					while (iter.hasNext())
					{
						SBean.Broadcast e = iter.next();
						if (e.timeStart + e.lifeTime < now)
							iter.remove();
					}
				}

				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				reslst = lst;
				gs.getLogger()
						.warn("add broadcast server ok, id="
								+ newid
								+ ",content="
								+ broadcast.content);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK && reslst != null)
			{
				setBroadcasts(reslst);
				callback.onCallback(errcode.ordinal());
			} else
			{
				gs.getLogger().warn(
						"add broadcast server fail 4");
				callback.onCallback(errcode.ordinal());
			}
		}

		@AutoInit
		public Table<byte[], byte[]>		world;
		@AutoInit
		public Table<String, Integer>		maxids;

		public final SBean.Broadcast		broadcast;
		public final AddBroadcastCallback	callback;
		public List<SBean.Broadcast>		reslst;
	}

	public interface DelBroadcastCallback
	{
		public void onCallback(int errcode);
	}

	public class DelBroadcastTrans implements Transaction
	{
		public DelBroadcastTrans(int id, DelBroadcastCallback callback)
		{
			this.id = id;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("broadcasts");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.Broadcast> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.Broadcast>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.Broadcast.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("del broadcast server fail 3");
						return false;
					}
				}
				int now = gs.getTime();
				{
					Iterator<SBean.Broadcast> iter = lst
							.iterator();
					while (iter.hasNext())
					{
						SBean.Broadcast e = iter.next();
						if (e.id == id)
							iter.remove();
						if (e.timeStart + e.lifeTime < now)
							iter.remove();
					}
				}

				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				reslst = lst;
				gs.getLogger().debug(
						"del broadcast server ok, id="
								+ id);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK && reslst != null)
			{
				setBroadcasts(reslst);
				callback.onCallback(errcode.ordinal());
			} else
			{
				gs.getLogger().warn(
						"del broadcast server fail 4");
				callback.onCallback(errcode.ordinal());
			}
		}

		@AutoInit
		public Table<byte[], byte[]>		world;
		@AutoInit
		public Table<String, Integer>		maxids;

		public final int			id;
		public final DelBroadcastCallback	callback;
		public List<SBean.Broadcast>		reslst;
	}

	public interface UseCDKeyCallback
	{
		public void onCallback(int errcode);
	}

	public class UseCDKeyTrans implements Transaction
	{
		public UseCDKeyTrans(int rid, int bid, int gid, String key,
				UseCDKeyCallback callback)
		{
			this.cdkeyLog = new SBean.CDKeyLog(rid, bid, gid, key);
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			{
				Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
				Stream.OStreamLE os = new Stream.OStreamLE(bos);
				os.pushString("cdkeys");
				byte[] key = bos.toByteArray();
				byte[] data = world.get(key);
				List<SBean.CDKeyLog> lst;
				if (data == null)
				{
					lst = new ArrayList<SBean.CDKeyLog>();
				} else
				{
					try
					{
						Stream.BytesInputStream bis = new Stream.BytesInputStream(
								data);
						Stream.IStreamLE is = new Stream.IStreamLE(
								bis);
						lst = is.popList(SBean.CDKeyLog.class);
					} catch (Exception ex)
					{
						gs.getLogger()
								.warn("use cdkey server fail 3");
						useErrorCode = CDKeyExchange.DATA_BASE_ERROR;
						return false;
					}
				}
				lst.add(cdkeyLog);
				//
				Stream.BytesOutputStream bos2 = new Stream.BytesOutputStream();
				Stream.OStreamLE os2 = new Stream.OStreamLE(
						bos2);
				os2.pushList(lst);
				world.put(key, bos2.toByteArray());
				gs.getLogger().debug("use cdkey server ok");
				useErrorCode = CDKeyExchange.CDKEY_EXCHANGE_SUCCESS;
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK)
			{
				callback.onCallback(useErrorCode);
			} else
			{
				callback.onCallback(useErrorCode);
			}
		}

		@AutoInit
		public Table<byte[], byte[]>	world;

		public final SBean.CDKeyLog	cdkeyLog;
		public final UseCDKeyCallback	callback;
		int				useErrorCode;

	}

	public class VisitRoleTrans implements Transaction
	{

		public VisitRoleTrans(int rid, DBRoleVisitor visitor)
		{
			this.rid = rid;
			this.visitor = visitor;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbRole = role.get(rid);
			if (dbRole == null)
				return false;
			if (visitor.visit(dbRole))
				role.put(rid, dbRole);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			mapRoles.remove(rid);
		}

		@AutoInit
		public Table<Integer, DBRole>	role;

		public final int		rid;
		public final DBRoleVisitor	visitor;
	}

	public class GetRoleBriefTrans implements Transaction
	{
		public GetRoleBriefTrans(int sid, int roleID)
		{
			this.sid = sid;
			this.roleID = roleID;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbrole = role.get(roleID);
			if (dbrole == null)
				return false;
			e = dbrole.getBriefWithoutFlag();
			return true;
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;
		public final int			sid;
		public final int			roleID;
		public SBean.DBRoleBrief		e;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			gs.getRPCManager().notifyGetRoleByIDResult(sid, e);
		}
	}

	public class GetRoleDataTrans implements Transaction
	{
		public GetRoleDataTrans(SBean.RoleDataRes res,
				GetRoleDataCallback callback)
		{
			this.res = res;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbrole = role.get(res.req.id);
			if (dbrole == null)
				return false;
			dbrole.getData(res);
			return true;
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;
		public final SBean.RoleDataRes		res;
		public final GetRoleDataCallback	callback;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
			{
				res.res = SBean.RoleDataRes.eFailed;
			}
			callback.onCallback(res);
		}
	}

	public class LoadRoleTrans implements Transaction
	{
		public LoadRoleTrans(int sid, String username, int roleID,
				Role.MSDKInfo msdkInfo)
		{
			this.sid = sid;
			this.username = username;
			this.roleID = roleID;
			this.msdkInfo = msdkInfo;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbRole = role.get(roleID);
			if (dbRole == null)
				return false;
			DBRoleBanData banData = new DBRoleBanData(dbRole);
			if (banData.isBanLogin(gs.getTime()))
			{
				ban = true;
				banLeftTime = banData.getBanLoginLeftTime(gs
						.getTime());
				banReason = banData.getBanLoginReason();
				return false;
			}
			roleinfo = new Role(roleID, gs, sid).fromDBRole(dbRole,
					mail.get(roleID), friend.get(roleID));
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK)
			{
				mapRoles.put(roleinfo.id, roleinfo);
				maps2r.put(sid, roleinfo.id);
				roleinfo.onLoadOK(username,
						Role.ROLE_LOAD_FROM_DB,
						msdkInfo);
			} else
			{
				mapRoles.remove(roleID);
				if (ban)
				{
					gs.getLogger()
							.warn("load username "
									+ username
									+ " failed, user is banned leftTime "
									+ banLeftTime
									+ " for reason : "
									+ banReason);
					gs.getRPCManager()
							.sendLuaPacket(sid,
									LuaPacket.encodeUserLogin(
											GameData.USERLOGIN_BAN,
											banLeftTime,
											banReason));
				} else
				{
					gs.getLogger()
							.debug("load username "
									+ username
									+ " failed");
					gs.getRPCManager()
							.sendLuaPacket(sid,
									LuaPacket.encodeUserLogin(
											GameData.USERLOGIN_FAILED,
											0,
											""));
				}
			}
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;
		@AutoInit
		public TableReadonly<Integer, DBMail>	mail;
		@AutoInit
		public TableReadonly<Integer, DBFriend>	friend;

		public final int			sid;
		public final String			username;
		public Role				roleinfo;
		public final int			roleID;
		public final Role.MSDKInfo		msdkInfo;
		public boolean				ban;
		public int				banLeftTime;
		public String				banReason;
	}

	public class GetPVPInfoTrans implements Transaction
	{
		public GetPVPInfoTrans(int rid, final byte pvpType,
				final boolean bAttack,
				GetPVPInfoCallback callback)
		{
			this.rid = rid;
			this.pvpType = pvpType;
			this.bAttack = bAttack;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbrole = role.get(rid);
			if (dbrole == null)
				return false;
			lst = new ArrayList<DBRoleGeneral>();
			for (DBRoleGeneral g : dbrole.generals)
			{
				boolean found = false;
				for (Short e : dbrole.arenaGenerals)
				{
					if (g.id == e)
					{
						found = true;
						break;
					}
				}
				if (found){
					DBRoleGeneral kdClone = g.kdClone();
					if(kdClone.generalSeyen!=null&&kdClone.generalSeyen.size()>0){
						DBGeneralSeyen seyen = kdClone.generalSeyen.get(0);
						if(seyen!=null){
							seyen.seyenTotal=dbrole.getRoleGeneralSeyenTotal();
						}
					}
					
					lst.add(kdClone);
				}
					
			}
			plst = new ArrayList<DBPetBrief>();
			for (SBean.DBPet p : dbrole.pets)
			{
				boolean found = false;
				for (Short e : dbrole.arenaPets)
				{
					if (p.id == e)
					{
						found = true;
						break;
					}
				}
				if (found)
				{
					byte deformStage = 0;
					for (SBean.DBPetDeform d : dbrole.petDeforms)
						if (d.id == p.id)
							deformStage = d.deformStage;
					plst.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl,
							p.lvl, p.evoLvl,
							(byte) p.growthRate,
							deformStage, p.name));
				}
			}
			relation = dbrole.getActiveRelations();
			gStone = dbrole.getGStones();
			rname = dbrole.name;
			lvl = dbrole.lvl;
			headIconID = dbrole.headIconID;
			return true;
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;
		public final int			rid;
		public final byte			pvpType;
		public final boolean			bAttack;
		public final GetPVPInfoCallback		callback;
		public String				rname;
		public short				lvl;
		public short				headIconID;
		public List<DBRoleGeneral>		lst;
		public List<DBPetBrief>			plst;
		public List<SBean.DBRelation>		relation;
		public List<SBean.DBGeneralStone>	gStone;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
			{
				callback.onCallback(null, (short) 0, (short) 0,
						null, null, null, null);
			} else
				callback.onCallback(rname, lvl, headIconID,
						lst, plst, relation, gStone);
		}
	}

	List<DBRole> cheatedRoles = new ArrayList<DBRole>();
	List<Integer> cheatedItems = new ArrayList<Integer>();
	
	public LoginManager(GameServer gs)
	{
		this.gs = gs;
		loginWhiteList.setCfgFile(gs.getConfig().whitelistFileName);
		registerLimitList
				.setCfgFile(gs.getConfig().registerlimitFileName);
	}

	public void visitAllRoles(VisitAllRolesCallback cb)
	{
		gs.getDB().execute(new VisitAllRolesTrans(cb));
	}

	public void genAutoUserID(int sid)
	{
		gs.getDB().execute(new GenUserIDTrans(sid));
	}

	public void startLogin(int sid, SBean.UserLoginRequest req,
			Role.MSDKInfo msdkInfo)
	{
		if (maps2r.containsKey(sid))
		{
			gs.getRPCManager()
					.sendLuaPacket(sid,
							LuaPacket.encodeUserLogin(
									GameData.USERLOGIN_BUSY,
									0, ""));
		}
		gs.getDB().execute(
				new LoadUserTrans(sid, req, msdkInfo, null,
						(short) 0));
	}

	public static int getStringCount(String s)
	{
		int n = 0;
		for (int i = 0; i < s.length(); ++i)
		{
			char c = s.charAt(i);
			if (c < (char) 128)
				n++;
			else
				n += 2;
		}
		return n;
	}

	public String checkUserInput(String name, int max, boolean isName,
			boolean checkDirty)
	{
		String rname = name.trim();
		//
		if (isName)
			rname = rname.toLowerCase();
		//
		int n = getStringCount(rname);
		if (n <= 0 || n > max)
			return null;
		if (rname.indexOf("[") != -1)
			return null;
		if (rname.indexOf(";") != -1)
			return null;
		if (rname.indexOf("#") != -1)
			return null;
		if (rname.indexOf("%") != -1)
			return null;
		if (rname.indexOf("\\") != -1)
			return null;
		if (rname.indexOf("|") != -1)
			return null;
		if (gs.getGameData().isBadName2(rname))
			return null;
		if (checkDirty && gs.getGameData().isQQDirtyWords(rname))
			return null;
		if (isName)
		{
			if (rname.indexOf(";") != -1)
				return null;
			if (rname.indexOf("'") != -1)
				return null;
			if (rname.indexOf("\"") != -1)
				return null;
		}
		return rname;
	}

	public void start()
	{
		try
		{
			ManagementFactory
					.getPlatformMBeanServer()
					.registerMBean(gsStat,
							new ObjectName(
									"i3k.gs:type=GSStat"));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void startCreateRole(int sid, SBean.UserLoginRequest reql,
			Role.MSDKInfo msdkInfo, String rname, short headIconID)
	{
//		visitAllRoles(new VisitAllRolesCallback() {
//
//			@Override
//			public void onCallback(DBRole dbRole) {
//				/*
//				boolean found = false;
//				for (DBRoleGeneral g : dbRole.generals) {
//					for (SBean.DBGeneralSeyen s : g.generalSeyen) {
//						if (s.lvl == 20)
//							found = true;
//					}
//				}
//				*/
//				
//				boolean found = false;
//				for (DBRoleGeneral g : dbRole.generals) {
//					if (g.evoLvl >= 5)
//						found = true;
//				}
//				
//				if (found) {
//					cheatedRoles.add(dbRole);
//				
//					/*
//					int count = 0;
//					for (SBean.DBRoleItem i : dbRole.items) {
//						if (i.id == 16 && i.count > count)
//							count = i.count;
//					}
//					cheatedItems.add(count);
//					*/
//				}
//			}
//			
//		});
		
		if (maps2r.containsKey(sid))
		{
			gs.getRPCManager()
					.sendLuaPacket(sid,
							LuaPacket.encodeUserLogin(
									GameData.USERLOGIN_BUSY,
									0, ""));
			return;
		}
		rname = checkUserInput(rname, gs.getGameData()
				.getUserInputCFG().roleName, true, true);
		if (rname == null)
		{
			gs.getRPCManager()
					.sendLuaPacket(sid,
							LuaPacket.encodeCreateRole(GameData.CREATEROLE_INVALID));
			return;
		}
		if (gs.getGameData().isBadName(rname))
		{
			gs.getRPCManager()
					.sendLuaPacket(sid,
							LuaPacket.encodeCreateRole(GameData.CREATEROLE_INVALID));
			return;
		}
		gs.getDB().execute(
				new LoadUserTrans(sid, reql, msdkInfo, rname,
						headIconID));
	}

	public boolean allowOpenID(String openID)
	{
		if (openID == null || openID.isEmpty())
			return false;
		return loginWhiteList.canLogin(openID);
	}

	public int getCurDayRegisterLimit()
	{
		return registerLimitList.getCurRegisterLimit();
	}

	public void startLogout(int sid)
	{
		Integer rid = maps2r.remove(sid);
		if (rid != null)
		{
			Role role = mapRoles.get(rid);
			if (role != null)
			{
				role.setStandby();
			}
		}
	}

	public void setJubaoDay(int jubaoDay)
	{
		this.jubaoDay = jubaoDay;
	}

	public int getLastWorldMailID()
	{
		return lastWorldMailID;
	}

	private void checkWorldMail(SBean.SysMail m)
	{
		List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
		for (SBean.DropEntryNew e : m.att)
		{
			switch (e.type) {
			case GameData.COMMON_TYPE_ITEM:
			{
				short iid = e.id;
				if (GameData.getInstance().getItemCFG(iid) != null
						&& e.arg > 0)
				{
					attLst.add(e);
				}
			}
				break;
			case GameData.COMMON_TYPE_EQUIP:
			{
				short eid = e.id;
				if (GameData.getInstance().getEquipCFG(eid) != null
						&& e.arg > 0)
				{
					attLst.add(e);
				}
			}
				break;
			case GameData.COMMON_TYPE_GENERAL:
			{
				short gid = e.id;
				if (GameData.getInstance().getGeneralCFG(gid) != null
						&& e.arg > 0
						&& e.arg <= GameData.MAX_GENERAL_EVO_LEVEL)
				{
					attLst.add(e);
				}
			}
				break;
			case GameData.COMMON_TYPE_MONEY:
			case GameData.COMMON_TYPE_STONE:
			case GameData.COMMON_TYPE_ARENA_POINT:
			case GameData.COMMON_TYPE_MARCH_POINT:
			case GameData.COMMON_TYPE_FORCE_POINT:
			{
				if (e.arg > 0)
				{
					attLst.add(e);
				}
			}
				break;
			default:
				break;
			}
		}
		m.att = attLst;
	}

	public void setWorldMails(List<SBean.SysMail> lst)
	{
		synchronized (worldMails)
		{
			worldMails = new ArrayList<SBean.SysMail>();
			for (SBean.SysMail e : lst)
			{
				checkWorldMail(e);
				worldMails.add(e);
				if (e.id > lastWorldMailID)
					lastWorldMailID = e.id;
			}
		}
	}

	public boolean getWorldMailContent(int id, List<String> contents,
			DBMail.MailAttachments att)
	{
		int now = gs.getTime();
		synchronized (worldMails)
		{
			for (SBean.SysMail e : worldMails)
			{
				if (e.id == id
						&& e.timeStart + e.lifeTime > now)
				{
					contents.add(e.content);
					att.attLst.addAll(e.att);
					att.mailSubType = 0;
					return true;
				}
			}
		}
		return false;
	}

	public List<SBean.SysMail> testWorldMails(int createTime, int lastID)
	{
		List<SBean.SysMail> lst = new ArrayList<SBean.SysMail>();
		int now = gs.getTime();
		synchronized (worldMails)
		{
			for (SBean.SysMail e : worldMails)
			{
				if ((e.timeStart > createTime || e.att
						.isEmpty())
						&& e.timeStart + e.lifeTime > now
						&& lastID < e.id)
				{
					lst.add(e);
				}
			}
		}
		return lst;
	}

	public interface AddWorldMailCallback
	{
		public void onCallback(boolean bOK);
	}

	public void addWorldMail(SBean.SysMail mail,
			AddWorldMailCallback callback)
	{
		gs.getDB().execute(new AddWorldMailTrans(mail, callback));
	}

	public void addDelayMail(SBean.SysMail mail,
			AddDelayMailCallback callback)
	{
		gs.getDB().execute(new AddDelayMailTrans(mail, callback));
	}

	// public void setDelayMails(List<SBean.SysMail> lst)
	// {
	// synchronized( delayMails )
	// {
	// delayMails = new ArrayList<SBean.SysMail>();
	// for(SBean.SysMail e : lst)
	// {
	// delayMails.add(e);
	// }
	// }
	// }

	public void testDelayMails()
	{
		gs.getDB().execute(new PopDelayMailTrans());
	}

	public void sendDelayMails(List<SBean.SysMail> lst)
	{
		for (SBean.SysMail e : lst)
		{
			final String title = e.title;
			final int fireTime = e.timeStart;
			this.addWorldMail(e, new AddWorldMailCallback()
			{

				@Override
				public void onCallback(boolean bOK)
				{
					gs.getLogger()
							.warn("send delay mail to world "
									+ String.valueOf(bOK)
									+ ", firetime "
									+ GameServer.getTimeStampStr(fireTime)
									+ ", title="
									+ title);
				}
			});
		}
	}

	public void addBroadcast(SBean.Broadcast broadcast,
			AddBroadcastCallback callback)
	{
		gs.getDB().execute(new AddBroadcastTrans(broadcast, callback));
	}

	public void delBroadcast(int id, DelBroadcastCallback callback)
	{
		gs.getDB().execute(new DelBroadcastTrans(id, callback));
	}

	public List<SBean.Broadcast> queryBroadcasts(int beginTime, int endTime)
	{
		List<SBean.Broadcast> curlst = broadcasts;
		List<SBean.Broadcast> lst = new ArrayList<SBean.Broadcast>();
		for (SBean.Broadcast e : curlst)
		{
			if (e.timeStart >= beginTime
					&& e.timeStart + e.lifeTime <= endTime)
				lst.add(e);
		}
		return lst;
	}

	public void setBroadcasts(List<SBean.Broadcast> lst)
	{
		synchronized (broadcasts)
		{
			broadcasts = new ArrayList<SBean.Broadcast>();
			for (SBean.Broadcast e : lst)
			{
				broadcasts.add(e);
			}
			lastBroadcastsModifyTime = gs.getTime();
		}
	}

	public int getBroadcastLastModifyTime()
	{
		return lastBroadcastsModifyTime;
	}

	public List<SBean.Broadcast> getCurrentBroadcasts()
	{
		return broadcasts;
	}

	public void onTimer(long timeNow, int timeTick)
	{
		// dll.updateCurLevelLimit();
		if (timeTick % RoleBriefCacheData.CLEAN_INTERVAL == 0) // TODO
		{
			Iterator<Map.Entry<Integer, RoleBriefCacheData>> it = mapCache
					.entrySet().iterator();
			while (it.hasNext())
			{
				RoleBriefCacheData r = it.next().getValue();
				if (r.isTooOld(timeTick))
				{
					it.remove();
				}
			}
		}

		if (timeTick % PVPCacheData.CLEAN_INTERVAL == 0) // TODO
		{
			Iterator<Map.Entry<Integer, PVPCacheData>> it = pvpCache
					.entrySet().iterator();
			while (it.hasNext())
			{
				PVPCacheData r = it.next().getValue();
				if (r.isTooOld(timeTick))
				{
					it.remove();
				}
			}
		}

		if (gs.getConfig().godMode == 0 && timeTick % 60 == 0)
		{
			final int time = gs.getTimeGMT();
			final int guestOnlineUsers = this.getGuestOnline();
			final int otherOnlineUsers = this.getOnlineNum()
					- guestOnlineUsers;
			final int registerUsers = this.getRegNum();
			gs.getGameService()
					.reportRegisterInfo(
							time,
							registerUsers,
							new GameService.ReportRegisterInfoCallback()
							{
								public void onReportRegisterInfoCallback(
										int result)
								{
									if (result != GameService.INVOKE_SUCCESS)
										gs.getLogger()
												.info("report register info time="
														+ time
														+ " registerCount="
														+ registerUsers
														+ " failed, return: "
														+ result);
								}
							});
			gs.getGameService()
					.reportOnlineInfo(
							time,
							otherOnlineUsers,
							false,
							new GameService.ReportOnlineInfoCallback()
							{
								public void onReportOnlineInfoCallback(
										int result)
								{
									if (result != GameService.INVOKE_SUCCESS)
										gs.getLogger()
												.info("report online info time="
														+ time
														+ " onlineCount="
														+ otherOnlineUsers
														+ " isGuest="
														+ false
														+ " failed, return: "
														+ result);
								}
							});
			gs.getGameService()
					.reportOnlineInfo(
							time,
							guestOnlineUsers,
							true,
							new GameService.ReportOnlineInfoCallback()
							{
								public void onReportOnlineInfoCallback(
										int result)
								{
									if (result != GameService.INVOKE_SUCCESS)
										gs.getLogger()
												.info("report online info time="
														+ time
														+ " onlineCount="
														+ guestOnlineUsers
														+ " isGuest="
														+ true
														+ " failed, return: "
														+ result);
								}
							});
		}

		if (timeTick % 60 == 0)
		{
			testDelayMails();
		}

		Iterator<Map.Entry<Integer, UserLoginStub>> itl = loginMap
				.entrySet().iterator();
		while (itl.hasNext())
		{
			UserLoginStub v = itl.next().getValue();
			if (v.time + 30 < timeTick) // TODO
			{
				itl.remove();
				// TODO
				gs.getRPCManager().notifyUserLoginFailed(v.sid,
						GameData.USERLOGIN_FAILED);
			}
		}

		int nGuest = 0;
		Iterator<Map.Entry<Integer, Role>> it = mapRoles.entrySet()
				.iterator();
		while (it.hasNext())
		{
			Role r = it.next().getValue();
			if (!r.isNull())
			{
				if (r.isIOSGuest())
					++nGuest;
				if (r.onTimer(timeNow, timeTick))
				{
					it.remove();
					gs.getLogger()
							.debug("role "
									+ r.id
									+ " remove");
					int onlineTime = r.updateLogoutTime(gs
							.getTime());
					gs.getTLogger()
							.logLogout(r,
									onlineTime,
									r.getFriendCountWithoutLock());
					gs.getTSSAntiManager().delUser(r);
					r.doSave();
				}
			}
		}
		setGuestOnline(nGuest);

		onlineIdChangeTime++;
		synchronized (this)
		{
			if (onlineIdChangeTime > 60 || onlineIds == null
					|| onlineIds.isEmpty())
			{
				onlineIds = new ArrayList<Integer>(
						mapRoles.keySet());
				Collections.shuffle(onlineIds);
				curOnlineId = 0;
				onlineIdChangeTime = 0;
			}
		}

		// resend sysmails
		{
			List<ResendMessage> messagesResend = new ArrayList<ResendMessage>();
			synchronized (resendMessages)
			{
				Iterator<ResendMessage> iter = resendMessages
						.iterator();
				while (iter.hasNext())
				{
					ResendMessage m = iter.next();
					if (m.timeStart
							+ GameData.getInstance()
									.getRandInt(15,
											30) < timeTick)
					{
						messagesResend.add(m);
						iter.remove();
					}
				}
			}
			for (ResendMessage m : messagesResend)
			{
				sysResendMessage(m);
			}
		}
	}

	public int getOnlineNum()
	{
		return mapRoles.size();
	}

	public boolean checkUserLogin(SBean.UserLoginRequest req)
	{
		if (req.loginType == SBean.UserLoginRequest.eLoginGod)
		{
			// TODO if( gs.getConfig().godMode > 0 )
			return true;
		} else if (req.loginType == SBean.UserLoginRequest.eLoginReconnect)
		{
			// TODO
			return true;
		}
		return false;
	}

	public boolean addUserLogin(int sid, SBean.UserLoginRequest req,
			Role.MSDKInfo msdkInfo, String rname, short headIconID)
	{
		UserLoginStub stub = new UserLoginStub(req, msdkInfo, sid,
				rname, headIconID);
		if (null != loginMap.putIfAbsent(sid, stub))
			return false;
		return true;
	}

	public void setUserLoginRes(int sid, byte err, String userName,
			String id)
	{
		// gs.getLogger().info("check stub, userName=" + userName +
		// ",key=" + id + ",sid=" + sid);
		UserLoginStub stub = loginMap.remove(sid);
		if (stub == null)
			return;
		// gs.getLogger().info("check stub, userName=" +
		// stub.req.userName + ",key=" + stub.req.key);
		if (stub.rname == null)
		{
			if (err == 0 && userName.equals(stub.req.userName)
					&& id.equals(stub.req.key))
			{
				startLogin(sid, stub.req, stub.msdkInfo);
			} else
			{
				gs.getRPCManager().notifyUserLoginFailed(sid,
						GameData.USERLOGIN_FAILED);
			}
		} else
		{
			if (err == 0 && userName.equals(stub.req.userName)
					&& id.equals(stub.req.key))
			{
				startCreateRole(sid, stub.req, stub.msdkInfo,
						stub.rname, stub.headIconID);
			} else
			{
				gs.getRPCManager().notifyUserLoginFailed(sid,
						GameData.USERLOGIN_FAILED);
			}
		}
	}

	public int getRoleCount()
	{
		return mapRoles.size();
	}

	public int getSessionCount()
	{
		return maps2r.size();
	}

	public Role getLoginRole(int sid)
	{
		Integer rid = maps2r.get(sid);
		if (rid == null)
			return null;
		Role role = mapRoles.get(rid);
		if( role == null || role.isNull() || ! role.checkSessionID(sid) )
			return null;
		return role;
	}

	public int getLoginRoleSessionID(int rid)
	{
		Role r = getRole(rid);
		if (r == null)
			return 0;
		return r.netsid;
	}

	public Role getRole(int rid)
	{
		Role role = mapRoles.get(rid);
		if (role == null || role.isNull())
			return null;
		return role;
	}

	public void luaAnnounce(String data)
	{
		for (int sid : maps2r.keySet())
		{
			gs.getRPCManager().sendLuaPacket(sid, data);
		}
	}

	public boolean announceGMMessage(String msg, byte times)
	{
		msg = checkUserInput(msg,
				gs.getGameData().getUserInputCFG().gmAnnounce,
				false, false);
		if (msg == null)
			return false;
		luaAnnounce(LuaPacket.encodeGMAnnounce(msg, times));
		return true;
	}

	public void modifyDataCallback(int sid, SBean.DataModifyRes res)
	{
		gs.getRPCManager().notifyDataModify(sid, res);
	}

	class ResendMessage
	{
		public int			roleID;
		public byte			subType;
		public String			title;
		public String			content;
		public int			lifeTime;
		public boolean			bNotifyOnline;
		public List<SBean.DropEntryNew>	attLst;

		public int			resendTimes;
		public int			timeStart;

	}

	List<ResendMessage>	resendMessages	= new LinkedList<ResendMessage>();

	public void sysSendMessage(final int gmsid, final int roleID,
			final byte subType, final String title,
			final String content, final int lifeTime,
			final boolean bNotifyOnline,
			final List<SBean.DropEntryNew> attLst)
	{
		this.execCommonMailVisitor(roleID, new CommonMailVisitor()
		{

			@Override
			public boolean visit(Role role)
			{
				role.addSysMessage(subType, title, content,
						attLst, lifeTime, bNotifyOnline);
				return true;
			}

			@Override
			public byte visit(DBMail dbmail)
			{
				dbmail.addSysMessage(subType, title, content,
						attLst, gs, lifeTime);
				return CommonMailVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode)
			{
				if (gmsid > 0)
				{
					gs.getRPCManager()
							.notifySysMessage(
									gmsid,
									errcode == 0 ? (byte) 0
											: (byte) 1);
				}
				if (errcode != 0)
				{
					gs.getLogger()
							.warn("Mail not sent!!! rid="
									+ roleID
									+ ", errcode="
									+ errcode
									+ ", subType="
									+ subType);
					if (errcode == 1 && gmsid <= 0)
					{
						ResendMessage resend = new ResendMessage();

						resend.roleID = roleID;
						resend.subType = subType;
						resend.title = title;
						resend.content = content;
						resend.lifeTime = lifeTime;
						resend.bNotifyOnline = bNotifyOnline;
						resend.attLst = attLst;

						resend.resendTimes = 0;
						resend.timeStart = gs.getTime();

						synchronized (resendMessages)
						{
							resendMessages.add(resend);
						}
					}
				}
			}

		});
	}

	public void sysResendMessage(final ResendMessage resend)
	{
		this.execCommonMailVisitor(resend.roleID,
				new CommonMailVisitor()
				{

					@Override
					public boolean visit(Role role)
					{
						role.addSysMessage(
								resend.subType,
								resend.title,
								resend.content,
								resend.attLst,
								resend.lifeTime,
								resend.bNotifyOnline);
						return true;
					}

					@Override
					public byte visit(DBMail dbmail)
					{
						dbmail.addSysMessage(
								resend.subType,
								resend.title,
								resend.content,
								resend.attLst,
								gs,
								resend.lifeTime);
						return CommonMailVisitor.ERR_DB_OK_SAVE;
					}

					@Override
					public void onCallback(boolean bDB,
							byte errcode)
					{
						if (errcode != 0)
						{
							gs.getLogger()
									.warn("Mail not resent!!! rid="
											+ resend.roleID
											+ ", errcode="
											+ errcode
											+ ", subType="
											+ resend.subType);
							if (errcode == 1)
							{

								resend.resendTimes++;
								if (resend.resendTimes <= 16)
								{
									resend.timeStart = gs
											.getTime();
									synchronized (resendMessages)
									{
										resendMessages.add(resend);
									}
								}
							}
						} else
						{
							gs.getLogger()
									.warn("Mail Resend OK!!! rid="
											+ resend.roleID
											+ ", errcode="
											+ errcode
											+ ", subType="
											+ resend.subType);
						}
					}

				});
	}

	public void userSendMessage(final Role sendRole,
			final int ReceiveRoleID, final String title,
			final String content, final int lifeTime,
			final boolean bNotifyOnline,
			final List<SBean.DropEntryNew> attLst)
	{
		this.execCommonRoleMailVisitor(ReceiveRoleID,
				new CommonRoleMailVisitor()
				{
					// String receiveRoleOpenID = "";
					// int receiveRoleLvl = 0;
					@Override
					public boolean visit(Role role)
					{
						role.addUserMessage(
								sendRole.name,
								title, content,
								attLst,
								lifeTime,
								bNotifyOnline);
						// receiveRoleOpenID =
						// role.msdkInfo.openID;
						// receiveRoleLvl = role.lvl;
						return true;
					}

					@Override
					public byte visit(DBRole dbrole,
							DBMail dbmail)
					{
						dbmail.addUserMessage(
								dbrole.name,
								title, content,
								attLst, gs,
								lifeTime);
						// receiveRoleOpenID =
						// dbrole.openID;
						// receiveRoleLvl = dbrole.lvl;
						return CommonRoleMailVisitor.ERR_DB_OK_SAVE;
					}

					@Override
					public void onCallback(boolean bDB,
							byte errcode)
					{
						if (errcode == CommonRoleMailVisitor.ERR_OK)
						{
							// TLogger.TLogEvent
							// tlogEvent = new
							// TLogger.TLogEvent();
							// TLogger.SecTalkRecord
							// secRecord = new
							// TLogger.SecTalkRecord(receiveRoleOpenID,
							// receiveRoleLvl,
							// title, content);
							// tlogEvent.addRecord(secRecord);
							// gs.getTLogger().logEvent(sendRole,
							// tlogEvent);
						}
					}

				});
	}

	public int getStoneByRMB(String username, float rmb)
	{
		SBean.PayCFGS cfg = gs.getGameData().getPayCFG(username);
		int irmb = (int) rmb;
		int stoneRes = 0;
		float f = rmb - irmb;
		while (true)
		{
			SBean.PayCFGLevelS lcfg = null;
			for (int i = cfg.levels.size() - 1; i >= 0; --i)
			{
				SBean.PayCFGLevelS e = cfg.levels.get(i);
				if (irmb >= e.rmb)
				{
					lcfg = e;
					break;
				}
			}
			if (lcfg == null)
				break;
			int n = irmb / lcfg.rmb;
			stoneRes += n * lcfg.stone;
			irmb -= n * lcfg.rmb;
		}
		stoneRes += (irmb + f) * 10;
		return stoneRes;
	}

	public class CommonMailVisitTrans implements Transaction
	{
		public CommonMailVisitTrans(int rid, CommonMailVisitor visitor)
		{
			this.rid = rid;
			this.visitor = visitor;
		}

		@Override
		public boolean doTransaction()
		{
			DBMail dbmail = mail.get(rid);
			if (dbmail == null)
			{
				dbmail = new DBMail();
			}
			byte r = visitor.visit(dbmail);
			if (r == CommonMailVisitor.ERR_DB_OK_SAVE)
				mail.put(rid, dbmail);
			return r != CommonRoleVisitor.ERR_DB_FAILED;
		}

		@AutoInit
		public Table<Integer, DBMail>	mail;
		public final int		rid;
		public final CommonMailVisitor	visitor;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			mapRoles.remove(rid);
			if (errcode == ErrorCode.eOK)
			{
				visitor.onCallback(true,
						CommonMailVisitor.ERR_OK);
			} else
			{
				visitor.onCallback(true,
						CommonMailVisitor.ERR_FAILED);
			}
		}
	}

	public static interface CommonMailVisitor
	{
		public static byte	ERR_OK			= 0;
		public static byte	ERR_NULL		= 1;
		public static byte	ERR_FAILED		= 2;

		public static byte	ERR_DB_OK_SAVE		= 0;
		public static byte	ERR_DB_OK_NOSAVE	= 1;
		public static byte	ERR_DB_FAILED		= 2;

		public boolean visit(Role role);

		public byte visit(DBMail dbmail);

		public void onCallback(boolean bDB, byte errcode);
	}

	public void execCommonMailVisitor(final int rid,
			final CommonMailVisitor visitor)
	{
		Role role = mapRoles.putIfAbsent(rid, Role.NULL_ROLE);
		if (role == null) //
		{
			gs.getDB().execute(
					new CommonMailVisitTrans(rid, visitor));
			return;
		}
		if (role.isNull())
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_NULL);
			return;
		}
		if (!visitor.visit(role))
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_FAILED);
		} else
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_OK);
		}
	}

	public static interface CommonRoleMailVisitor
	{
		public static byte	ERR_OK			= 0;
		public static byte	ERR_NULL		= 1;
		public static byte	ERR_FAILED		= 2;

		public static byte	ERR_DB_OK_SAVE		= 0;
		public static byte	ERR_DB_OK_NOSAVE	= 1;
		public static byte	ERR_DB_FAILED		= 2;

		public boolean visit(Role role);

		public byte visit(DBRole dbrole, DBMail dbmail);

		public void onCallback(boolean bDB, byte errcode);
	}

	public class CommonRoleMailVisitTrans implements Transaction
	{
		public CommonRoleMailVisitTrans(int rid,
				CommonRoleMailVisitor visitor)
		{
			this.rid = rid;
			this.visitor = visitor;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbrole = role.get(rid);
			if (dbrole == null)
				return false;
			DBMail dbmail = mail.get(rid);
			if (dbmail == null)
			{
				dbmail = new DBMail();
			}
			byte r = visitor.visit(dbrole, dbmail);
			if (r == CommonMailVisitor.ERR_DB_OK_SAVE)
				mail.put(rid, dbmail);
			return r != CommonRoleVisitor.ERR_DB_FAILED;
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>	role;
		@AutoInit
		public Table<Integer, DBMail>		mail;
		public final int			rid;
		public final CommonRoleMailVisitor	visitor;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			mapRoles.remove(rid);
			if (errcode == ErrorCode.eOK)
			{
				visitor.onCallback(true,
						CommonMailVisitor.ERR_OK);
			} else
			{
				visitor.onCallback(true,
						CommonMailVisitor.ERR_FAILED);
			}
		}
	}

	public void execCommonRoleMailVisitor(final int rid,
			final CommonRoleMailVisitor visitor)
	{
		Role role = mapRoles.putIfAbsent(rid, Role.NULL_ROLE);
		if (role == null) //
		{
			gs.getDB().execute(
					new CommonRoleMailVisitTrans(rid,
							visitor));
			return;
		}
		if (role.isNull())
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_NULL);
			return;
		}
		if (!visitor.visit(role))
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_FAILED);
		} else
		{
			visitor.onCallback(false, CommonMailVisitor.ERR_OK);
		}
	}

	public void execCommonDBRoleVisitor(final int rid,
			final CommonRoleVisitor visitor)
	{
		gs.getDB().execute(new CommonRoleVisitTrans(rid, visitor));
	}

	public class CommonRoleVisitTrans implements Transaction
	{
		public CommonRoleVisitTrans(int rid, CommonRoleVisitor visitor)
		{
			this.rid = rid;
			this.visitor = visitor;
		}

		@Override
		public boolean doTransaction()
		{
			DBRole dbrole = role.get(rid);
			if (dbrole == null)
				return false;
			rname = dbrole.name;
			byte r = visitor.visit(dbrole);
			if (r == CommonRoleVisitor.ERR_DB_OK_SAVE)
				role.put(rid, dbrole);
			return r != CommonRoleVisitor.ERR_DB_FAILED;
		}

		@AutoInit
		public Table<Integer, DBRole>	role;
		public final int		rid;
		public final CommonRoleVisitor	visitor;
		public String			rname	= "";

		@Override
		public void onCallback(ErrorCode errcode)
		{
			mapRoles.remove(rid);
			if (errcode == ErrorCode.eOK)
			{
				visitor.onCallback(true,
						CommonRoleVisitor.ERR_OK, rname);
			} else
			{
				visitor.onCallback(true,
						CommonRoleVisitor.ERR_FAILED,
						rname);
			}
		}
	}

	public class CommonFriendVisitTrans implements Transaction
	{
		public CommonFriendVisitTrans(int rid,
				CommonFriendVisitor visitor)
		{
			this.rid = rid;
			this.visitor = visitor;
		}

		@Override
		public boolean doTransaction()
		{
			DBFriend dbfriend = friend.get(rid);
			if (dbfriend == null)
				return false;
			byte r = visitor.visit(dbfriend);
			if (r == CommonFriendVisitor.ERR_DB_OK_SAVE)
				friend.put(rid, dbfriend);
			return r != CommonFriendVisitor.ERR_DB_FAILED;
		}

		@AutoInit
		public Table<Integer, DBFriend>		friend;
		public final int			rid;
		public final CommonFriendVisitor	visitor;

		@Override
		public void onCallback(ErrorCode errcode)
		{
			mapRoles.remove(rid);
			if (errcode == ErrorCode.eOK)
			{
				visitor.onCallback(true,
						CommonFriendVisitor.ERR_OK);
			} else
			{
				visitor.onCallback(true,
						CommonFriendVisitor.ERR_FAILED);
			}
		}
	}

	public static interface CommonRoleVisitor
	{
		public static byte	ERR_OK			= 0;
		public static byte	ERR_NULL		= 1;
		public static byte	ERR_FAILED		= 2;

		public static byte	ERR_DB_OK_SAVE		= 0;
		public static byte	ERR_DB_OK_NOSAVE	= 1;
		public static byte	ERR_DB_FAILED		= 2;

		public boolean visit(Role role);

		public byte visit(DBRole dbrole);

		public void onCallback(boolean bDB, byte errcode, String rname);
	}

	public static interface CommonFriendVisitor
	{
		public static byte	ERR_OK			= 0;
		public static byte	ERR_NULL		= 1;
		public static byte	ERR_FAILED		= 2;

		public static byte	ERR_DB_OK_SAVE		= 0;
		public static byte	ERR_DB_OK_NOSAVE	= 1;
		public static byte	ERR_DB_FAILED		= 2;

		public boolean visit(Role role);

		public byte visit(DBFriend dbfriend);

		public void onCallback(boolean bDB, byte errcode);
	}

	public void execCommonRoleVisitor(final int rid,
			final CommonRoleVisitor visitor)
	{
		Role role = mapRoles.putIfAbsent(rid, Role.NULL_ROLE);
		if (role == null) //
		{
			gs.getDB().execute(
					new CommonRoleVisitTrans(rid, visitor));
			return;
		}
		byte errcode = 0;
		String rname = "";
		if (role.isNull())
		{
			errcode = CommonRoleVisitor.ERR_NULL;
		} else if (!visitor.visit(role))
		{
			errcode = CommonRoleVisitor.ERR_FAILED;
		} else
		{
			errcode = CommonRoleVisitor.ERR_OK;
			rname = role.name;
		}

		visitor.onCallback(false, errcode, rname);
	}

	public void execCommonRoleVisitorAsync(final int rid,
			final CommonRoleVisitor visitor)
	{
		Role role = mapRoles.putIfAbsent(rid, Role.NULL_ROLE);
		if (role == null) //
		{
			gs.getDB().execute(
					new CommonRoleVisitTrans(rid, visitor));
			return;
		}
		byte errcode = 0;
		String rname = "";
		if (role.isNull())
		{
			errcode = CommonRoleVisitor.ERR_NULL;
		} else if (!visitor.visit(role))
		{
			errcode = CommonRoleVisitor.ERR_FAILED;
		} else
		{
			errcode = CommonRoleVisitor.ERR_OK;
			rname = role.name;
		}
		final byte ecode = errcode;
		final String name = rname;
		gs.execute(new Runnable()
		{
			@Override
			public void run()
			{
				visitor.onCallback(false, ecode, name);
			}

		});
	}

	public void execCommonFriendVisitor(final int rid,
			final CommonFriendVisitor visitor)
	{
		Role role = mapRoles.putIfAbsent(rid, Role.NULL_ROLE);
		if (role == null) //
		{
			gs.getDB()
					.execute(new CommonFriendVisitTrans(
							rid, visitor));
			return;
		}
		if (role.isNull())
		{
			visitor.onCallback(false, CommonRoleVisitor.ERR_NULL);
			return;
		}
		if (!visitor.visit(role))
		{
			visitor.onCallback(false, CommonRoleVisitor.ERR_FAILED);
		} else
		{
			visitor.onCallback(false, CommonRoleVisitor.ERR_OK);
		}
	}

	public interface PayCallback
	{
		public void onCallback(boolean bOK);
	}

	public void recvMessageFromFriend(final SBean.GlobalRoleID srcID,
			SBean.GlobalRoleID dstID, final byte msgType,
			final short arg1, final int arg2)
	{
		if (dstID.serverID != gs.getConfig().id)
			return;
		int rid = dstID.roleID;
		//
		execCommonFriendVisitor(rid, new CommonFriendVisitor()
		{
			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					role.recvMessageFromFriend(srcID,
							msgType, arg1, arg2);
				}
				return true;
			}

			@Override
			public byte visit(DBFriend dbfriend)
			{
				dbfriend.recvMessageFromFriend(srcID,
						gs.getTime(), msgType, arg1,
						arg2);
				return CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode)
			{
			}

		});
	}

	public void queryFriendProp(final SBean.QueryFriendPropReq req)
	{
		if (req.recordTarget.roleID.serverID != gs.getConfig().id)
			return;
		int rid = req.recordTarget.roleID.roleID;
		//
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<SBean.FriendProp>	prop	= new ArrayList<SBean.FriendProp>();

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					prop.add(role.getFriendPropWithoutLock());
				}
				return true;
			}

			@Override
			public byte visit(DBRole dbrole)
			{
				prop.add(dbrole.getFriendProp());
				return CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				if (!prop.isEmpty())
					gs.getRPCManager()
							.friendSetFriendPropRes(
									req,
									prop.get(0));
			}

		});
	}

	public static interface DBRoleVisitor
	{
		public boolean visit(DBRole dbrole);
	}

	public void modifyData(final int sid, SBean.DataModifyReq req,
			String mailTitle, String mailContent)
	{
		// TODO
		if (sid > 0
				&& req.rid > 0
				&& req.mtype == SBean.DataModifyReq.eAdd
				&& (req.dtype == SBean.DataModifyReq.eMoney
						|| req.dtype == SBean.DataModifyReq.eStone
						|| req.dtype == SBean.DataModifyReq.eItem
						|| req.dtype == SBean.DataModifyReq.eEquip || req.dtype == SBean.DataModifyReq.eGeneral))
		{
			/*
			 * if( mailTitle != null ) { mailTitle =
			 * checkUserInput(mailTitle, 32, false); if( mailTitle
			 * == null ) { gs.getRPCManager().notifyDataModify(sid,
			 * new SBean.DataModifyRes(SBean.DataModifyRes.eFailed,
			 * req, 0, 0)); } } else mailTitle = ""; if( mailContent
			 * != null ) { mailContent = checkUserInput(mailContent,
			 * GameData.getInstance().getUserInputCFG().sysMail,
			 * false); if( mailContent == null ) {
			 * gs.getRPCManager().notifyDataModify(sid, new
			 * SBean.DataModifyRes(SBean.DataModifyRes.eFailed, req,
			 * 0, 0)); } } else mailContent = "";
			 * List<SBean.DropEntry> attItemLst = new
			 * ArrayList<SBean.DropEntry>();
			 * List<SBean.CurrencyEntry> attCurrencyLst = new
			 * ArrayList<SBean.CurrencyEntry>(); switch( req.dtype )
			 * { case SBean.DataModifyReq.eMoney:
			 * attCurrencyLst.add(new
			 * SBean.CurrencyEntry(GameData.CURRENCY_UNIT_MONEY,
			 * req.val)); break; case SBean.DataModifyReq.eStone:
			 * attCurrencyLst.add(new
			 * SBean.CurrencyEntry(GameData.CURRENCY_UNIT_STONE,
			 * req.val)); break; case SBean.DataModifyReq.eItem: if(
			 * req.val > 0 && req.val <= 100 ) attItemLst.add(new
			 * SBean.DropEntry(GameData.COMMON_TYPE_ITEM,
			 * (byte)req.val, (short)req.id)); break; case
			 * SBean.DataModifyReq.eEquip: if( req.val > 0 &&
			 * req.val <= 100 ) attItemLst.add(new
			 * SBean.DropEntry(GameData.COMMON_TYPE_EQUIP,
			 * (byte)req.val, (short)req.id)); break; case
			 * SBean.DataModifyReq.eGeneral: if( req.val > 0 &&
			 * req.val <= GameData.MAX_GENERAL_EVO_LEVEL )
			 * attItemLst.add(new
			 * SBean.DropEntry(GameData.COMMON_TYPE_GENERAL,
			 * (byte)req.val, (short)req.id)); break; default:
			 * break; } sysSendMessage(sid, req.rid,
			 * DBMail.Message.SUB_TYPE_ADD_DATA, mailTitle,
			 * mailContent, 0, true, attItemLst, attCurrencyLst);
			 * return;
			 */
		}
		Role role = mapRoles.putIfAbsent(req.rid, Role.NULL_ROLE);
		if (role == null)
		{
			gs.getDB().execute(new ModRoleTrans(gs, sid, req));
			return;
		}
		if (role.isNull())
		{
			if (sid > 0)
				gs.getRPCManager()
						.notifyDataModify(
								sid,
								new SBean.DataModifyRes(
										SBean.DataModifyRes.eBusy,
										req,
										0,
										0));
			return;
		}
		// modify role
		SBean.DataModifyRes res = new SBean.DataModifyRes(
				SBean.DataModifyRes.eOK, req, 0, 0);
		// int newStamp = -1;
		synchronized (role)
		{
			switch (req.dtype) {
			case SBean.DataModifyReq.eStone:
			{
				res.oldVal = role.stone;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					role.stone = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (role.stone < 0)
					role.stone = 0;
				res.newVal = role.stone;
			}
				break;
			case SBean.DataModifyReq.eMoney:
			{
				res.oldVal = role.money;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					role.money = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (role.money < 0)
					role.money = 0;
				res.newVal = role.money;
			}
				break;
			case SBean.DataModifyReq.eForceScore:
			{
				res.oldVal = role.forceInfo.point;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					role.forceInfo.point = req.val;
					break;
				case SBean.DataModifyReq.eAdd:
					role.forceInfo.point += req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (role.forceInfo.point < 0)
					role.forceInfo.point = 0;
				res.newVal = role.forceInfo.point;
			}
				break;
			case SBean.DataModifyReq.eForceID:
			{
				res.oldVal = role.forceInfo.id;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					if (req.id == 0
							&& role.forceInfo.id == req.val)
					{
						role.forceInfo.id = 0;
						role.forceInfo.name = "";
					}
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				res.newVal = role.forceInfo.id;
			}
				break;
			case SBean.DataModifyReq.eLevel:
			{
				res.oldVal = role.lvl;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					role.lvl = (byte) req.val;
					break;
				case SBean.DataModifyReq.eAdd:
					role.lvl += req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (role.lvl < 1)
					role.lvl = 1;
				if (role.lvl > gs.getRoleLevelLimit())
					role.lvl = gs.getRoleLevelLimit();
				role.setLvl(role.lvl);
				res.newVal = role.lvl;
			}
				break;
			case SBean.DataModifyReq.eItem:
			{
				SBean.ItemCFGS cfg = gs.getGameData()
						.getItemCFG(req.id);
				if (cfg == null)
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				Integer oldCount = role.items.get(req.id);
				if (oldCount == null)
					res.oldVal = 0;
				else
					res.oldVal = oldCount.intValue();
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (newcnt < 0)
					newcnt = 0;
				if (newcnt > GameData.MAX_ITEM_COUNT)
					newcnt = GameData.MAX_ITEM_COUNT;
				res.newVal = newcnt;
				if (newcnt == 0)
				{
					if (res.oldVal != 0)
						role.items.remove(req.id);
				} else
					role.items.put(req.id, newcnt);
			}
				break;
			case SBean.DataModifyReq.eEquip:
			{
				SBean.EquipCFGS cfg = gs.getGameData()
						.getEquipCFG(req.id);
				if (cfg == null)
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				Short oldCount = role.equips.get(req.id);
				if (oldCount == null)
					res.oldVal = 0;
				else
					res.oldVal = oldCount.intValue();
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				if (newcnt < 0)
					newcnt = 0;
				if (newcnt > GameData.MAX_EQUIP_COUNT)
					newcnt = GameData.MAX_EQUIP_COUNT;
				res.newVal = newcnt;
				if (newcnt == 0)
				{
					if (res.oldVal != 0)
						role.equips.remove(req.id);
				} else
					role.equips.put(req.id, (short) newcnt);
			}
				break;
			case SBean.DataModifyReq.eSeyen:
			{
				short gid = req.id;
				if( req.val < 0 || gid < 0 )
				{
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}

				SBean.GeneralCFGS gcfg = null;
				if( gid != 0 )
				{
					gcfg = GameData.getInstance().getGeneralCFG(gid);
					if( gcfg == null )
					{
						res.res = SBean.DataModifyRes.eFailed;
						break;
					}
				}

				res.oldVal = 0;
				int newcnt = 0;
				switch (req.mtype) {
				case SBean.DataModifyReq.eSet:
					newcnt = req.val;
					break;
				default:
					res.res = SBean.DataModifyRes.eFailed;
					break;
				}
				
				int tempSeyen =0;
				for(General g : role.generals.values())
				{
					if( g.id == gid || gid == 0 )
					{
						if(g.generalSeyen==null||g.generalSeyen.size()==0){
							g.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
							g.generalSeyen.add(new SBean.DBGeneralSeyen());
						}
						
						SBean.DBGeneralSeyen seyen1 = g.generalSeyen.get(0);
						if(gid==0){
							tempSeyen+=newcnt-seyen1.lvl;
						}else{
							tempSeyen=newcnt-seyen1.lvl;
						}
						if (g.id == gid)
							res.oldVal = seyen1.lvl;
						seyen1.lvl=(short)newcnt;
					}
				}
				if(tempSeyen!=0){
					for(General general : role.generals.values()){
						if(general!=null){
							if(general.generalSeyen==null||general.generalSeyen.size()==0){
								general.generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
								general.generalSeyen.add(new SBean.DBGeneralSeyen());
							}
							general.generalSeyen.get(0).seyenTotal+=tempSeyen;
						}
					}
				}
				res.newVal = newcnt;
			}
				break;
			default:
				res.res = SBean.DataModifyRes.eFailed;
				break;
			}
		}
		if (sid > 0)
			gs.getRPCManager().notifyDataModify(sid, res);
	}

	public List<LoginManager.WorldChatCache.Msg> getWorldMsg(int time)
	{
		return wcCache.getMsg(time);
	}

	public List<LoginManager.WorldChatCache.Msg> getForceMsg(int fid,
			int time)
	{
		WorldChatCache wc = fcCache.get(fid);
		if (wc != null)
			return wc.getMsg(time);
		return new ArrayList<WorldChatCache.Msg>();
	}

	public void addWorldChatMsg(WorldChatCache.Msg m)
	{
		wcCache.addMsg(m);
		String data = LuaPacket.encodeWorldChat(m);
		for (Role role : mapRoles.values())
		{
			int sid = role.isRecvWorldChat(m.time);
			if (sid < 0)
				continue;
			gs.getRPCManager().sendLuaPacket(sid, data);
		}
	}

	public void addForceChatMsg(int fid, WorldChatCache.Msg m)
	{
		WorldChatCache wc = fcCache.get(fid);
		if (wc == null)
		{
			wc = new WorldChatCache();
			fcCache.put(fid, wc);
		}
		wc.addMsg(m);
		String data = LuaPacket.encodeForceChat(m);
		for (Role role : mapRoles.values())
		{
			int sid = role.isRecvForceChat(m.time);
			if (sid < 0)
				continue;
			if (role.getForceID() != fid)
				continue;
			gs.getRPCManager().sendLuaPacket(sid, data);
		}
	}

	public int getWorldChatStamp()
	{
		return wcCache.getStamp();
	}

	public int getForceChatStamp(int fid)
	{
		WorldChatCache wc = fcCache.get(fid);
		if (wc == null)
		{
			return 0;
		}
		return wc.getStamp();
	}

	public void queryName(String name, QueryNameCallback callback)
	{
		gs.getDB().execute(new QueryNameTrans(name, callback));
	}

	public void getRoleByID(int sid, int roleID)
	{
		Role r = mapRoles.get(roleID);
		if (r != null && !r.isNull())
		{
			gs.getRPCManager().notifyGetRoleByIDResult(sid,
					r.getDBRoleBriefWithLock());
			return;
		}
		gs.getDB().execute(new GetRoleBriefTrans(sid, roleID));
	}

	public void saveRoles()
	{
		int now = gs.getTime();
		for (Role role : mapRoles.values())
		{
			int onlineTime = role.updateLogoutTime(now);
			gs.getTLogger().logLogout(role, onlineTime,
					role.getFriendCountWithoutLock());
			role.doSave();
		}
		mapRoles.clear();
	}

	public void updateRoleBriefCache(int rid, RoleBriefCache data)
	{
		RoleBriefCacheData d = new RoleBriefCacheData(data);
		RoleBriefCacheData old = mapCache.putIfAbsent(rid, d);
		if (old == null)
			d.updateTime = gs.getTime();
		else
		{
			old.update(data);
			old.updateTime = gs.getTime();
		}
	}

	public void updatePVPCache(int rid, int flag)
	{
		PVPCacheData d = new PVPCacheData(rid, flag);
		PVPCacheData old = pvpCache.putIfAbsent(rid, d);
		if (old == null)
			d.updateTime = gs.getTime();
		else
		{
			old.update(flag);
			old.updateTime = gs.getTime();
		}
	}

	static class LevelRoleCache
	{
		public static final int	MAX_LEVEL_ROLE_CACHE_LENGTH	= 200;
		public int		level;
		public List<Integer>	roles				= new LinkedList<Integer>();

		public LevelRoleCache(int lvl)
		{
			this.level = lvl;
		}

		public synchronized void updateRole(int rid)
		{
			roles.remove(new Integer(rid));
			if (roles.size() >= MAX_LEVEL_ROLE_CACHE_LENGTH)
				roles.remove(0);
			roles.add(rid);
		}

		public synchronized void removeRole(int rid)
		{
			roles.remove(new Integer(rid));
		}

		public synchronized Integer getRandomRole()
		{
			if (roles.isEmpty())
				return null;
			int index = GameData.getInstance().getRandom()
					.nextInt(roles.size());
			return roles.get(index);
		}
	}

	synchronized LevelRoleCache getCreateLevelRoleCache(int level)
	{
		LevelRoleCache cache = levelCache.get(level);
		if (cache == null)
			cache = new LevelRoleCache(level);
		levelCache.put(level, cache);
		return cache;
	}

	synchronized void removeCacheLevelRole(int level, int rid)
	{
		LevelRoleCache cache = levelCache.get(level);
		if (cache != null)
		{
			cache.removeRole(rid);
		}
	}

	synchronized LevelRoleCache getRandomLevelRoleCache(int minxLevel,
			int MaxLevel)
	{
		NavigableMap<Integer, LevelRoleCache> subMap = levelCache
				.subMap(minxLevel, true, MaxLevel, true);
		if (subMap == null || subMap.isEmpty())
			return null;
		Object[] keys = subMap.keySet().toArray();
		int index = GameData.getInstance().getRandom()
				.nextInt(keys.length);
		int level = (int) keys[index];
		return subMap.get(level);
	}

	void updateLevelRoleCache(int rid, int level)
	{
		LevelRoleCache cache = getCreateLevelRoleCache(level);
		cache.updateRole(rid);
	}

	void updateLevelRoleCache(int rid, int oldlevel, int curlevel)
	{
		removeCacheLevelRole(oldlevel, rid);
		updateLevelRoleCache(rid, curlevel);
	}

	Integer getRandomLevelRole(int minxLevel, int MaxLevel)
	{
		LevelRoleCache cache = getRandomLevelRoleCache(minxLevel,
				MaxLevel);
		if (cache == null)
			return null;
		return cache.getRandomRole();
	}

	public interface GetRoleIDByRoleNameCallback
	{
		public void onCallback(String roleName, Integer roleID);
	}

	public interface GetRoleIDByUserNameCallback
	{
		public void onCallback(String userName, Integer roleID);
	}

	public interface GetForceIDByForceNameCallback
	{
		public void onCallback(String forceName, Integer forceID);
	}

	public interface ChangeRoleNameByRoleIDCallback
	{
		public void onCallback(String newName);
	}

	public void getRoleIDByRoleName(final String roleName,
			final GetRoleIDByRoleNameCallback callback)
	{
		gs.getDB()
				.execute(new GetRoleIDByRoleNameTrans(
						roleName,
						new GetRoleIDByRoleNameCallback()
						{

							@Override
							public void onCallback(
									String rname,
									Integer rid)
							{
								if (rid == null)
								{
									callback.onCallback(
											roleName,
											rid);
									return;
								}
								if (rid.intValue() <= 0)
								{
									callback.onCallback(
											roleName,
											null);
									return;
								}
								callback.onCallback(
										roleName,
										rid);
							}
						}));
	}

	public void getRoleIDByUserName(final String userName,
			final GetRoleIDByUserNameCallback callback)
	{
		gs.getDB()
				.execute(new GetRoleIDByUserNameTrans(userName,
						callback));
	}

	public String getUserNameByOpenID(String openID)
	{
		return gs.getConfig().channelID + "_" + openID;
	}

	public void changeRoleNameByRoleID(int rid, String oldName,
			String newName, ChangeRoleNameByRoleIDCallback callback)
	{
		gs.getDB().execute(
				new ChangeRoleNameByRoleIDTrans(rid, oldName,
						newName, callback));
	}

	public interface GetRoleIDByOpenIDCallback
	{
		public void onCallback(String openID, Integer roleID);
	}

	public void getRoleIDByOpendID(final String openID,
			final GetRoleIDByOpenIDCallback callback)
	{
		final String userName = getUserNameByOpenID(openID)
				.toLowerCase();
		getRoleIDByUserName(userName, new GetRoleIDByUserNameCallback()
		{
			@Override
			public void onCallback(String userName, Integer roleID)
			{
				callback.onCallback(openID, roleID);
			}
		});
	}

	public void getForceIDByForceName(final String forceName,
			final GetForceIDByForceNameCallback callback)
	{
		gs.getDB()
				.execute(new GetRoleIDByRoleNameTrans(
						forceName,
						new GetRoleIDByRoleNameCallback()
						{

							@Override
							public void onCallback(
									String rname,
									Integer rid)
							{
								if (rid == null)
								{
									callback.onCallback(
											forceName,
											rid);
									return;
								}
								if (rid.intValue() >= 0)
								{
									callback.onCallback(
											forceName,
											null);
									return;
								}
								callback.onCallback(
										forceName,
										new Integer(
												-rid.intValue()));
							}
						}));
	}

	public interface GetRoleDataCallback
	{
		public void onCallback(SBean.RoleDataRes res);
	}

	public void getRoleData(SBean.RoleDataReq req,
			GetRoleDataCallback callback)
	{
		// TODO
		SBean.RoleDataRes res = new SBean.RoleDataRes();
		res.res = SBean.RoleDataRes.eFailed;
		res.req = req;
		Role role = mapRoles.get(req.id);
		if (role != null)
		{
			if (role.isNull())
			{
				res.res = SBean.RoleDataRes.eBusy;
			} else
			{
				role.getData(res);
			}
			callback.onCallback(res);
			return;
		}
		//
		gs.getDB().execute(new GetRoleDataTrans(res, callback));
	}

	public interface GetRolesDataCallback
	{
		public void onCallback(List<SBean.RoleDataRes> res);
	}

	public void getRolesData(int qtype, List<Integer> rids,
			final GetRolesDataCallback callback)
	{
		final List<SBean.RoleDataRes> res = new ArrayList<SBean.RoleDataRes>();
		List<Integer> lst = new ArrayList<Integer>();
		for (int rid : rids)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
				lst.add(rid);
			else
			{
				SBean.RoleDataRes result = new SBean.RoleDataRes();
				result.res = SBean.RoleDataRes.eFailed;
				result.req = new SBean.RoleDataReq(qtype, rid);
				r.getData(result);
				res.add(result);
			}
		}
		if (lst.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetRolesDataTrans(
						qtype,
						lst,
						new LoginManager.GetRolesDataCallback()
						{

							@Override
							public void onCallback(
									List<SBean.RoleDataRes> data)
							{
								for (SBean.RoleDataRes p : data)
								{
									if (p == null)
										continue;
									res.add(p);
								}
								callback.onCallback(res);
							}
						}));
	}

	public static class BanInfo
	{
		public int	banID;
		public int	banLeftTime;
		public String	reason;

		public BanInfo(int id, int leftTime, String reason)
		{
			this.banID = id;
			this.banLeftTime = leftTime;
			this.reason = reason;
		}
	}

	static abstract class BanData
	{
		// second: 0 锟斤拷猓�>0锟斤拷锟斤拷锟斤拷锟�0锟斤拷锟矫凤拷锟�
		// List<Integer> banEndTimes = new ArrayList<Integer>();
		Map<Integer, SBean.DBBanData>	allBanData	= new TreeMap<Integer, SBean.DBBanData>();

		BanData()
		{

		}

		BanData(List<SBean.DBBanData> allBanData)
		{
			for (SBean.DBBanData e : allBanData)
			{
				this.allBanData.put(e.banTypeID, e);
			}
		}

		List<SBean.DBBanData> getDBData()
		{
			return new ArrayList<SBean.DBBanData>(
					allBanData.values());
		}

		static int calcBanEndTime(int banSecond, int curTime)
		{
			int banEndTime = -1;
			if (banSecond > 0)
			{
				banEndTime = curTime + banSecond;
				if (banEndTime < 0)
					banEndTime = Integer.MAX_VALUE;
			}
			return banEndTime;
		}

		static boolean isInBan(int banEndTime, int curTime)
		{
			return banEndTime < 0 || curTime < banEndTime;
		}

		boolean isBan(int typeID, int curTime)
		{
			SBean.DBBanData banData = allBanData.get(typeID);
			if (banData == null)
				return false;
			return isInBan(banData.banEndTime, curTime);
		}

		String getBanReason(int typeID)
		{
			SBean.DBBanData banData = allBanData.get(typeID);
			if (banData == null)
				return "";
			return banData.banReason;
		}

		void ban(int typeID, int second, String reason, int curTime)
		{
			int banEndTime = calcBanEndTime(second, curTime);
			SBean.DBBanData banData = new SBean.DBBanData(typeID,
					banEndTime, reason);
			allBanData.put(banData.banTypeID, banData);
		}

		void unban(int typeID)
		{
			allBanData.remove(typeID);
		}

		int getBanEndTime(int typeID)
		{
			SBean.DBBanData banData = allBanData.get(typeID);
			if (banData == null)
				return 0;
			return banData.banEndTime;
		}

		int getBanLeftTime(int typeID, int curTime)
		{
			int endTime = getBanEndTime(typeID);
			int leftTime = endTime > curTime ? (endTime - curTime)
					: (endTime < 0 ? -1 : 0);
			return leftTime;
		}

		boolean isBanLogin(int curTime)
		{
			return isBan(BAN_LOGIN_INDEX, curTime);
		}

		int getBanLoginLeftTime(int curTime)
		{
			return getBanLeftTime(BAN_LOGIN_INDEX, curTime);
		}

		String getBanLoginReason()
		{
			return getBanReason(BAN_LOGIN_INDEX);
		}

		void banLogin(int second, String reason, int curTime)
		{
			ban(BAN_LOGIN_INDEX, second, reason, curTime);
		}

		void unbanLogin()
		{
			unban(BAN_LOGIN_INDEX);
		}

		int getBanLoginEndTime()
		{
			return getBanEndTime(BAN_LOGIN_INDEX);
		}

		boolean isBanBattle(int curTime)
		{
			return isBan(BAN_BATTLE_INDEX, curTime);
		}

		// int getBanBattleLeftTime(int curTime)
		// {
		// return getBanLeftTime(BAN_BATTLE_INDEX, curTime);
		// }
		//
		// String getBanBattleReason()
		// {
		// return getBanReason(BAN_BATTLE_INDEX);
		// }

		boolean isBanMarch(int curTime)
		{
			return isBan(BAN_MARCH_INDEX, curTime);
		}

		// int getBanMarchLeftTime(int curTime)
		// {
		// return getBanLeftTime(BAN_MARCH_INDEX, curTime);
		// }
		//
		// String getBanMarchReason()
		// {
		// return getBanReason(BAN_MARCH_INDEX);
		// }

		boolean isBanChibi(int curTime)
		{
			return isBan(BAN_CHIBI_INDEX, curTime);
		}

		// int getBanChibiLeftTime(int curTime)
		// {
		// return getBanLeftTime(BAN_CHIBI_INDEX, curTime);
		// }
		//
		// String getBanChibiReason()
		// {
		// return getBanReason(BAN_CHIBI_INDEX);
		// }

		boolean isBanArena(int curTime)
		{
			return isBan(BAN_ARENA_INDEX, curTime);
		}

		// int getBanArenaLeftTime(int curTime)
		// {
		// return getBanLeftTime(BAN_ARENA_INDEX, curTime);
		// }
		//
		// String getBanArenaReason()
		// {
		// return getBanReason(BAN_ARENA_INDEX);
		// }

		boolean isBanChat(int curTime)
		{
			return isBan(BAN_CHAT_INDEX, curTime);
		}

		int getBanChatLeftTime(int curTime)
		{
			return getBanLeftTime(BAN_CHAT_INDEX, curTime);
		}

		String getBanChatReason()
		{
			return getBanReason(BAN_CHAT_INDEX);
		}

		void multiBan(int types, int second, String reason, int curTime)
		{
			for (int i = 0; i < BAN_PLAY_TYPE_COUNT; ++i)
			{
				if ((types & (1 << i)) != 0)
				{
					ban(i, second, reason, curTime);
				}
			}
		}

		void multiUnban(int types)
		{
			for (int i = 0; i < BAN_PLAY_TYPE_COUNT; ++i)
			{
				if ((types & (1 << i)) != 0)
				{
					unban(i);
				}
			}
		}

		// Map<Integer, Integer> getBanPlayLeftTime(int curTime)
		// {
		// Map<Integer, Integer> leftTimes = new TreeMap<Integer,
		// Integer>();
		// for (int i = BAN_BATTLE_INDEX; i <= BAN_ARENA_INDEX; ++i)
		// {
		// int leftTime = getBanLeftTime(i, curTime);
		// if (leftTime != 0)
		// leftTimes.put(i, leftTime);
		// }
		// return leftTimes;
		// }

		List<BanInfo> getBanPlayInfo(int curTime)
		{
			List<BanInfo> info = new ArrayList<BanInfo>();
			for (int i = BAN_BATTLE_INDEX; i <= BAN_ARENA_INDEX; ++i)
			{
				int leftTime = getBanLeftTime(i, curTime);
				if (leftTime != 0)
					info.add(new BanInfo(i, leftTime,
							getBanReason(i)));
			}
			return info;
		}

		boolean isBanPlay(int curTime)
		{
			for (int i = BAN_BATTLE_INDEX; i <= BAN_ARENA_INDEX; ++i)
			{
				if (isBan(i, curTime))
					return true;
			}
			return false;
		}
	}

	static class RoleBanData extends BanData
	{
		RoleBanData()
		{
		}

		RoleBanData(List<SBean.DBBanData> banData)
		{
			super(banData);
		}
	}

	static class DBRoleBanData extends BanData
	{
		DBRoleBanData(DBRole dbrole)
		{
			super(dbrole.banData);
		}
	}

	public interface ModifyRoleDataCallback
	{
		public void onCallback(int errcode);
	}

	public int banRole(int rid, final int second, final String reason,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleBanData banData = new DBRoleBanData(
						dbrole);
				banData.banLogin(second, reason, gs.getTime());
				dbrole.banData = banData.getDBData();
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					role.banData.banLogin(second, reason,
							gs.getTime());
				}
				gs.getRPCManager().closeSession(role.netsid);
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int unbanRole(int rid, final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleBanData banData = new DBRoleBanData(
						dbrole);
				banData.unbanLogin();
				dbrole.banData = banData.getDBData();
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					role.banData.unbanLogin();
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	// second: 0 锟斤拷猓�>0锟斤拷锟斤拷锟斤拷锟�0锟斤拷锟矫凤拷锟�
	// public int setRoleBanEndTime(final int rid, final long second, final
	// ModifyRoleDataCallback callback)
	// {
	// execCommonRoleVisitor(rid, new CommonRoleVisitor()
	// {
	// @Override
	// public byte visit(DBRole dbrole)
	// {
	// long banEndTime = second;
	// if (second > 0)
	// banEndTime += gs.getTime();
	// dbrole.banEndTime = banEndTime;
	// return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
	// }
	//
	// @Override
	// public boolean visit(Role role)
	// {
	// synchronized( role )
	// {
	// long banEndTime = second;
	// if (second > 0)
	// banEndTime += gs.getTime();
	// role.banEndTime = banEndTime;
	// }
	// if (second != 0)
	// gs.getRPCManager().closeSession(role.netsid);
	// return true;
	// }
	//
	// @Override
	// public void onCallback(boolean bDB, byte errcode, String rname)
	// {
	// callback.onCallback(errcode);
	// }
	// });
	// return 0;
	// }

	public int modifyRolePay(final int rid, final int pay,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (pay != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.PayRecord record = new TLogger.PayRecord(
							dbrole.lvl,
							GameData.getInstance()
									.getVIPLevel(dbrole
											.getStonePayTotal()),
							dbrole.getStonePayTotal());
					int oldStonePayAlpha = dbrole.stonePayAlpha;
					dbrole.stonePayAlpha += pay;
					if (pay > Integer.MAX_VALUE / 2
							&& dbrole.stonePayAlpha < 0
							|| dbrole.stonePayAlpha > Integer.MAX_VALUE / 2)
						dbrole.stonePayAlpha = Integer.MAX_VALUE / 2;
					if (dbrole.stonePayAlpha < -dbrole.stonePayMidas)
						dbrole.stonePayAlpha = -dbrole.stonePayMidas;
					record.setPayInfo(
							TLog.PAY_LEVEL_RETURN,
							(dbrole.stonePayAlpha - oldStonePayAlpha) / 10,
							0);
					record.setAfterPayState(
							GameData.getInstance()
									.getVIPLevel(dbrole
											.getStonePayTotal()),
							dbrole.getStonePayTotal());
					tlogEvent.addRecord(record);
					dbrole.updateVipLevel(gs.getTime());
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (pay != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.PayRecord record = new TLogger.PayRecord(
							role.lvl,
							GameData.getInstance()
									.getVIPLevel(role
											.getStonePayTotal()),
							role.getStonePayTotal());
					int oldStonePayAlpha = role.stonePayAlpha;
					role.stonePayAlpha += pay;
					if (pay > Integer.MAX_VALUE / 2
							&& role.stonePayAlpha < 0
							|| role.stonePayAlpha > Integer.MAX_VALUE / 2)
						role.stonePayAlpha = Integer.MAX_VALUE / 2;
					if (role.stonePayAlpha < -role.stonePayMidas)
						role.stonePayAlpha = -role.stonePayMidas;
					record.setPayInfo(
							TLog.PAY_LEVEL_RETURN,
							(role.stonePayAlpha - oldStonePayAlpha) / 10,
							0);
					record.setAfterPayState(
							GameData.getInstance()
									.getVIPLevel(role
											.getStonePayTotal()),
							role.getStonePayTotal());
					tlogEvent.addRecord(record);
					role.updateVipLevel();
					gs.getRPCManager()
							.syncRolePaytoatalViplvl(
									role.netsid,
									role.getStonePayTotal(),
									role.lvlVIP);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int modifyRoleLevel(final int rid, final int level,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (level > 0)
				{
					dbrole.lvl += level;
					// if (dbrole.lvl >
					// gs.getRoleLevelLimit())
					// dbrole.lvl = gs.getRoleLevelLimit();
					int levellimit = gs.getRoleLevelLimit();
					if (dbrole.lvl > levellimit)
						dbrole.lvl = (short) levellimit;
					dbrole.exp = 0;
					TLogger.LevelUpRecord lur = new TLogger.LevelUpRecord(
							dbrole.lvl);
					lur.setLastLevelUpTime(dbrole.upgradeTime);
					dbrole.upgradeTime = gs.getTime();
					lur.setCurLevelUpTime(dbrole.upgradeTime);
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					tlogEvent.addRecord(lur);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (level > 0)
				{
					role.lvl += level;
					// if (role.lvl >
					// gs.getRoleLevelLimit())
					// role.lvl = gs.getRoleLevelLimit();
					int levellimit = gs.getRoleLevelLimit();
					if (role.lvl > levellimit)
						role.lvl = (short) levellimit;
					role.exp = 0;
					TLogger.LevelUpRecord lur = new TLogger.LevelUpRecord(
							role.lvl);
					lur.setLastLevelUpTime(role.upgradeTime);
					role.upgradeTime = gs.getTime();
					lur.setCurLevelUpTime(role.upgradeTime);
					gs.getRPCManager().syncRoleLvlExp(
							role.netsid, role.lvl,
							role.exp);
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					tlogEvent.addRecord(lur);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int delRoleItem(final int rid, final int iid, final int icount,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_DEL_ITEM);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(dbrole
							.gmDelItem(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_DEL_ITEM);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(role
							.gmDelItem(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int addRoleItem(final int rid, final int iid, final int icount,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_ADD_ITEM);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(dbrole
							.gmAddItem(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_ADD_ITEM);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(role
							.gmAddItem(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int delRoleEquip(final int rid, final int iid, final int icount,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_DEL_EQUIP);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(dbrole
							.gmDelEquip(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_DEL_EQUIP);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(role
							.gmDelEquip(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int addRoleEquip(final int rid, final int iid, final int icount,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_ADD_EQUIP);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(dbrole
							.gmAddEquip(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (icount != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_ADD_EQUIP);
					commonFlowRecord.setArg(iid, icount);
					commonFlowRecord.addChange(role
							.gmAddEquip(iid, icount));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int modifyRoleExp(final int rid, final int changeExp,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (changeExp != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_EXP);
					commonFlowRecord.setArg(changeExp, 0);
					tlogEvent.addCommonFlow(commonFlowRecord);
					commonFlowRecord.addChanges(dbrole
							.gmModifyExp(changeExp,
									gs.getTime(),
									tlogEvent,
									gs));
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (changeExp != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_EXP);
					commonFlowRecord.setArg(changeExp, 0);
					tlogEvent.addCommonFlow(commonFlowRecord);
					commonFlowRecord.addChanges(role
							.gmModifyExp(changeExp,
									tlogEvent));
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int modifyRoleVit(final int rid, final int changeVit,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (changeVit != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_VIT);
					commonFlowRecord.setArg(changeVit, 0);
					commonFlowRecord.addChange(dbrole
							.gmModifyVit(changeVit,
									gs.getTime()));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (changeVit != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_VIT);
					commonFlowRecord.setArg(changeVit, 0);
					commonFlowRecord.addChange(role
							.gmModifyVit(changeVit));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
					gs.getRPCManager().syncRoleVit(
							role.netsid, role.vit);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int modifyRoleStone(final int rid, final int changeStone,
			final boolean kick,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (changeStone != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_DIAMOND);
					commonFlowRecord.setArg(changeStone, 0);
					commonFlowRecord.addChange(dbrole
							.gmModifyStone(changeStone));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (changeStone != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_DIAMOND);
					commonFlowRecord.setArg(changeStone, 0);
					commonFlowRecord.addChange(role
							.gmModifyStone(changeStone));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
					gs.getRPCManager()
							.syncRoleStone(role.netsid,
									role.stone);
					if (kick)
						gs.getRPCManager()
								.forceClose(role.netsid,
										GameData.FORCE_CLOSE_KICK);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public int modifyRoleMoney(final int rid, final int changeMoney,
			final ModifyRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				if (changeMoney != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_MONEY);
					commonFlowRecord.setArg(changeMoney, 0);
					commonFlowRecord.addChange(dbrole
							.gmModifyMoney(changeMoney));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(dbrole,
									tlogEvent);
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				if (changeMoney != 0)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
					commonFlowRecord.setEvent(TLog.AT_GM_MODIFY_MONEY);
					commonFlowRecord.setArg(changeMoney, 0);
					commonFlowRecord.addChange(role
							.gmModifyMoney(changeMoney));
					tlogEvent.addCommonFlow(commonFlowRecord);
					tlogcontent = gs.getTLogger()
							.toTlogEventStr(role,
									tlogEvent);
					gs.getRPCManager()
							.syncRoleMoney(role.netsid,
									role.money);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public static class SRoleInfo
	{
		public int	lvl;
		public int	exp;
		public int	money;
		public int	stone;
		public int	vit;
		public int	maxVit;
		public int	itemTypeNum;
		public int	registerTime;
		public int	isOnLine;
		public int	banStatus;
		public long	banEndTime;
		public int	normalBigPassProgress;
		public int	normalSmallPassProgress;
		public int	heroBigPassProgress;
		public int	heroSmallPassProgress;
		public String	roleName	= "";
		public int	lastLoginTime;
	}

	public interface QueryRoleInfoCallback
	{
		public void onCallback(int errcode, SRoleInfo roleinfo);
	}

	public int queryRoleInfo(final int rid,
			final QueryRoleInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			SRoleInfo	info	= new SRoleInfo();

			@Override
			public byte visit(DBRole dbrole)
			{
				info.lvl = dbrole.lvl;
				info.exp = dbrole.exp;
				info.money = dbrole.money;
				info.stone = dbrole.stone;
				info.vit = dbrole.vit;
				info.maxVit = GameData.getInstance().getVitMax(
						dbrole.lvl);
				info.itemTypeNum = dbrole.items.size();
				info.registerTime = dbrole.createTime - 60 * 60 * 8;
				info.isOnLine = 1;
				DBRoleBanData banData = new DBRoleBanData(
						dbrole);
				info.banStatus = banData.isBanLogin(gs
						.getTime()) ? 1 : 0;
				int banEndTime = banData.getBanLoginEndTime();
				info.banEndTime = banEndTime < 0 ? 0
						: banEndTime - 60 * 60 * 8;
				info.normalBigPassProgress = dbrole.combatPos
						.get(0).catIndex + 1;
				info.normalSmallPassProgress = dbrole.combatPos
						.get(0).combatIndex + 1;
				info.heroBigPassProgress = dbrole.combatPos
						.get(1).catIndex + 1;
				info.heroSmallPassProgress = dbrole.combatPos
						.get(1).combatIndex + 1;
				info.roleName = dbrole.name;
				info.lastLoginTime = dbrole.lastLoginTime - 60 * 60 * 8;
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				info.lvl = role.lvl;
				info.exp = role.exp;
				info.money = role.money;
				info.stone = role.stone;
				info.vit = role.vit;
				info.maxVit = GameData.getInstance().getVitMax(
						role.lvl);
				info.itemTypeNum = role.items.size();
				info.registerTime = role.createTime;
				info.isOnLine = mapRoles.containsKey(role.id) ? 0
						: 1;
				info.banStatus = role.banData.isBanLogin(gs
						.getTime()) ? 1 : 0;
				int banEndTime = role.banData
						.getBanLoginEndTime();
				info.banEndTime = banEndTime < 0 ? 0
						: banEndTime - 60 * 60 * 8;
				info.normalBigPassProgress = role.combatPos[0].catIndex + 1;
				info.normalSmallPassProgress = role.combatPos[0].combatIndex + 1;
				info.heroBigPassProgress = role.combatPos[1].catIndex + 1;
				info.heroSmallPassProgress = role.combatPos[1].combatIndex + 1;
				info.roleName = role.name;
				info.lastLoginTime = role.lastLoginTime;
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode, info);
			}
		});
		return 0;
	}

	public interface QueryGeneralInfoCallback
	{
		public void onCallback(int errcode,
				List<IDIP.SGeneralInfo> ginfo);
	}

	public int queryGeneralInfo(final int rid, final int pageNo,
			final QueryGeneralInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<DBRoleGeneral>	generals	= new ArrayList<DBRoleGeneral>();
			Set<Short>		battleGnerals	= new TreeSet<Short>();

			@Override
			public byte visit(DBRole dbrole)
			{
				for (DBRoleGeneral g : dbrole.generals)
				{
					generals.add(new DBRoleGeneral(g.id,
							g.lvl, g.exp, g.advLvl,
							g.evoLvl, g.weapon,
							null, null,null,null,(short)g.headicon,null,null));
				}
				battleGnerals = new TreeSet<Short>(
						dbrole.arenaGenerals);

				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					for (DBRoleGeneral g : role.generals
							.values())
					{
						generals.add(new DBRoleGeneral(
								g.id, g.lvl,
								g.exp,
								g.advLvl,
								g.evoLvl,
								g.weapon, null,
								null,null,null,(short)g.headicon,null,null));
					}
					battleGnerals.addAll(role.arenaGenerals);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				Collections.sort(generals,
						new Comparator<DBRoleGeneral>()
						{
							public int compare(
									DBRoleGeneral l,
									DBRoleGeneral r)
							{
								return 10000
										* (l.lvl - r.lvl)
										+ 100
										* (l.advLvl - l.advLvl)
										+ (l.evoLvl - r.evoLvl);

							}
						});
				int allCount = generals.size();
				List<IDIP.SGeneralInfo> ginfos = new ArrayList<IDIP.SGeneralInfo>();
				int totalPage = (allCount + (IDIP.MAX_GENERALLIST_NUM - 1))
						/ IDIP.MAX_GENERALLIST_NUM;
				int fromIndex = IDIP.MAX_GENERALLIST_NUM
						* (pageNo - 1);
				int endIndex = IDIP.MAX_GENERALLIST_NUM
						* pageNo;
				if (endIndex > allCount)
					endIndex = allCount;
				int index = 0;
				for (DBRoleGeneral g : generals)
				{
					if (index >= fromIndex)
					{
						if (index < endIndex)
						{
							SBean.GeneralCFGS cfg = GameData
									.getInstance()
									.getGeneralCFG(g.id);
							if (cfg != null)
							{
								IDIP.SGeneralInfo ginfo = new IDIP.SGeneralInfo();
								ginfo.GeneralName = cfg.name;
								ginfo.Level = g.lvl;
								ginfo.Grade = g.advLvl;
								ginfo.Star = g.evoLvl;
								ginfo.IsBattle = (byte) (battleGnerals
										.contains(g.id) ? 1
										: 0);
								ginfo.TotalPageNo = totalPage;
								ginfos.add(ginfo);
							}
						} else
						{
							break;
						}
					}
					++index;
				}
				callback.onCallback(errcode, ginfos);
			}
		});
		return 0;
	}

	public interface QueryGeneralEquipInfoCallback
	{
		public void onCallback(int errcode,
				List<IDIP.SGeneralEquipInfo> geinfo);
	}

	public int queryGeneralEquipInfo(final int rid, final int pageNo,
			final QueryGeneralEquipInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<DBRoleGeneral>	generals	= new ArrayList<DBRoleGeneral>();
			Set<Short>		battleGnerals	= new TreeSet<Short>();

			@Override
			public byte visit(DBRole dbrole)
			{
				for (DBRoleGeneral g : dbrole.generals)
				{
					generals.add(g.kdClone());
				}
				battleGnerals.addAll(dbrole.arenaGenerals);
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					for (DBRoleGeneral g : role.generals
							.values())
					{
						generals.add(g.kdClone());
					}
					battleGnerals.addAll(role.arenaGenerals);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				Collections.sort(generals,
						new Comparator<DBRoleGeneral>()
						{
							public int compare(
									DBRoleGeneral l,
									DBRoleGeneral r)
							{
								return 10000
										* (l.lvl - r.lvl)
										+ 100
										* (l.advLvl - l.advLvl)
										+ (l.evoLvl - r.evoLvl);

							}
						});
				int allCount = 0;
				List<IDIP.SGeneralEquipInfo> geinfos = new ArrayList<IDIP.SGeneralEquipInfo>();
				for (DBRoleGeneral g : generals)
				{
					for (SBean.DBGeneralEquip ge : g.equips)
					{
						if (ge.lvl >= 0)
							++allCount;
					}
				}
				// gs.getLogger().info("equip count =" +
				// allCount);
				int totalPage = (allCount + (IDIP.MAX_GENERALLIST_NUM - 1))
						/ IDIP.MAX_GENERALLIST_NUM;
				int fromIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* (pageNo - 1);
				int endIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* pageNo;
				if (endIndex > allCount)
					endIndex = allCount;
				int index = 0;
				for (DBRoleGeneral g : generals)
				{
					String gname = GameData.getInstance()
							.getGeneralCFG(g.id).name;
					byte isBattle = (byte) (battleGnerals
							.contains(g.id) ? 1 : 0);
					int pos = 1;
					for (SBean.DBGeneralEquip ge : g.equips)
					{
						if (ge.lvl >= 0)
						{
							if (index >= fromIndex)
							{
								if (index < endIndex)
								{
									SBean.EquipCFGS ecfg = GameData
											.getInstance()
											.getEquipCFG(g,
													pos);
									if (ecfg != null)
									{
										IDIP.SGeneralEquipInfo geinfo = new IDIP.SGeneralEquipInfo();
										geinfo.GeneralName = gname;
										geinfo.EquipId = ecfg.id;
										geinfo.EquipName = ecfg.name;
										geinfo.IsBattle = isBattle;
										geinfo.TotalPageNo = totalPage;
										geinfos.add(geinfo);
										// gs.getLogger().info("pos="
										// +
										// pos
										// +
										// " ["
										// +
										// geinfo.GeneralName
										// +
										// "] eid="
										// +
										// geinfo.EquipId
										// +
										// ", ename="
										// +
										// geinfo.EquipName);
									}
								} else
								{
									break;
								}
							}
							++index;
						}
						++pos;
					}
				}
				callback.onCallback(errcode, geinfos);
			}
		});
		return 0;
	}

	public interface QueryItemInfoCallback
	{
		public void onCallback(int errcode, List<IDIP.SBagInfo> binfo);
	}

	public int queryItemInfo(final int rid, final int pageNo,
			final QueryItemInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<SBean.DBRoleItem2>	items	= new ArrayList<SBean.DBRoleItem2>();

			@Override
			public byte visit(DBRole dbrole)
			{
				for (SBean.DBRoleItem i : dbrole.items)
				{
					int count =i.count;
					
					if(count<=0){
						count=60000+count;
					}
					items.add(new SBean.DBRoleItem2(i.id,
							count));
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					items.addAll(role
							.copyItemsWithoutLock());
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				Collections.sort(
						items,
						new Comparator<SBean.DBRoleItem2>()
						{
							public int compare(
									SBean.DBRoleItem2 l,
									SBean.DBRoleItem2 r)
							{
								return l.id
										- r.id;

							}
						});
				int allCount = items.size();
				List<IDIP.SBagInfo> binfos = new ArrayList<IDIP.SBagInfo>();
				int totalPage = (allCount + (IDIP.MAX_GENERALLIST_NUM - 1))
						/ IDIP.MAX_GENERALLIST_NUM;
				int fromIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* (pageNo - 1);
				int endIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* pageNo;
				if (endIndex > allCount)
					endIndex = allCount;
				int index = 0;
				for (SBean.DBRoleItem2 i : items)
				{
					if (index >= fromIndex)
					{
						if (index < endIndex)
						{
							SBean.ItemCFGS cfg = GameData
									.getInstance()
									.getItemCFG(i.id);
							if (cfg != null)
							{
								IDIP.SBagInfo binfo = new IDIP.SBagInfo();
								String iname = cfg.name;
								binfo.ItemName = iname;
								binfo.ItemId = i.id;
								binfo.ItemNum = i.count;
								binfo.TotalPageNo = totalPage;
								binfos.add(binfo);
							}
						} else
						{
							break;
						}
					}
					++index;

				}
				callback.onCallback(errcode, binfos);
			}
		});
		return 0;
	}

	public interface QueryEquipInfoCallback
	{
		public void onCallback(int errcode,
				List<IDIP.BackPackListInfo> binfo);
	}

	public int queryEquipInfo(final int rid, final int pageNo,
			final QueryEquipInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<SBean.DBRoleEquip>	equips	= new ArrayList<SBean.DBRoleEquip>();

			@Override
			public byte visit(DBRole dbrole)
			{
				for (SBean.DBRoleEquip i : dbrole.equips)
				{
					equips.add(new SBean.DBRoleEquip(i.id,
							i.count));
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					equips.addAll(role
							.copyEquipsWithoutLock());
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				Collections.sort(
						equips,
						new Comparator<SBean.DBRoleEquip>()
						{
							public int compare(
									SBean.DBRoleEquip l,
									SBean.DBRoleEquip r)
							{
								return l.id
										- r.id;

							}
						});
				int allCount = equips.size();
				List<IDIP.BackPackListInfo> binfos = new ArrayList<IDIP.BackPackListInfo>();
				int totalPage = (allCount + (IDIP.MAX_GENERALLIST_NUM - 1))
						/ IDIP.MAX_GENERALLIST_NUM;
				int fromIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* (pageNo - 1);
				int endIndex = IDIP.MAX_GENERALEQUIPLIST_NUM
						* pageNo;
				if (endIndex > allCount)
					endIndex = allCount;
				int index = 0;
				for (SBean.DBRoleEquip i : equips)
				{
					if (index >= fromIndex)
					{
						if (index < endIndex)
						{
							SBean.EquipCFGS cfg = GameData
									.getInstance()
									.getEquipCFG(i.id);
							if (cfg != null)
							{
								String iname = cfg.name;
								IDIP.BackPackListInfo binfo = new IDIP.BackPackListInfo();
								binfo.EquipName = iname;
								binfo.EquipId = i.id;
								binfo.EquipNum = i.count;
								binfo.TotalPageNo = totalPage;
								binfos.add(binfo);
							}
						} else
						{
							break;
						}
					}
					++index;

				}
				callback.onCallback(errcode, binfos);
			}
		});
		return 0;
	}

	public interface QueryUserFightPowerCallback
	{
		public void onCallback(int errcode, int power);
	}

	public int queryUserFightPowerInfo(final int rid,
			final QueryUserFightPowerCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			int	power	= -1;

			@Override
			public byte visit(DBRole dbrole)
			{
				power = dbrole.getArenaGeneralsPower()
						+ dbrole.getArenaPetsPower();
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_NOSAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					power = role.getArenaGeneralsPowerWithoutLock()
							+ role.getArenaPetsPowerWithoutLock();
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode, power);
			}
		});
		return 0;
	}

	public static final int	MAX_MAIL_ATTACHMENT_ITEM_COUNT	= 1000;
	public static final int	MAX_MAIL_ATTACHMENT_MONEY_COUNT	= 10000000;

	public static String checkMailAttachment(int type, long id, int count)
	{
		String errMsg = "";
		if (count != 0)
		{
			switch (type) {
			case GameData.COMMON_TYPE_ITEM:
			{
				if (GameData.getInstance().getItemCFG(
						(short) id) == null)
				{
					errMsg = "item id invalid";
				} else
				{
					if (count < 0
							&& count > MAX_MAIL_ATTACHMENT_ITEM_COUNT)
					{
						errMsg = "item count greater than 1k";
					}
				}
			}
				break;
			case GameData.COMMON_TYPE_EQUIP:
			{
				if (GameData.getInstance().getEquipCFG(
						(short) id) == null)
				{
					errMsg = "equip id invalid";
				} else
				{
					if (count < 0
							&& count > MAX_MAIL_ATTACHMENT_ITEM_COUNT)
					{
						errMsg = "equip count greater than 1k";
					}
				}
			}
				break;
			case GameData.COMMON_TYPE_MONEY:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "money count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_STONE:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "diamond count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_ARENA_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "arena point count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_MARCH_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "march point count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_FORCE_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "force point count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_RICH_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "rich point count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_RICH_GOLD:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "rich gold count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_SUPER_ARENA_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "super arena point count greater than 10M";
				}
			}
				break;
			case GameData.COMMON_TYPE_DUEL_POINT:
			{
				if (count < 0
						&& count > MAX_MAIL_ATTACHMENT_MONEY_COUNT)
				{
					errMsg = "duel point count greater than 10M";
				}
			}
				break;
			default:
				errMsg = "attachment type id invalid";
				break;
			}
		}
		return errMsg;
	}

	public interface SendRoleMailCallback
	{
		public void onCallback(int errcode);
	}

	public void sendRoleMail(final int roleID, final String title,
			final String content, final int lifeTime,
			final List<SBean.DropEntryNew> attLst,
			final SendRoleMailCallback callback)
	{
		this.execCommonMailVisitor(roleID, new CommonMailVisitor()
		{

			@Override
			public boolean visit(Role role)
			{
				role.addSysMessage(
						DBMail.Message.SUB_TYPE_GM_MAIL,
						title, content, attLst,
						lifeTime, true);
				return true;
			}

			@Override
			public byte visit(DBMail dbmail)
			{
				dbmail.addSysMessage(
						DBMail.Message.SUB_TYPE_GM_MAIL,
						title, content, attLst, gs,
						lifeTime);
				return CommonMailVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode)
			{
				callback.onCallback(errcode);
			}
		});
	}

	public int getRoleArenaRank(int rid)
	{
		int rank = gs.getArenaManager().getRoleRank(rid);
		if (rank < 0)
			rank = GameData.getInstance().getArenaCFG().rankMax;
		return rank;
	}

	public interface ClearRoleDataCallback
	{
		public void onCallback(int errcode);
	}

	public int clearRoleData(final int rid, final byte clearStone,
			final byte clearMoney, final byte clearVit,
			final ClearRoleDataCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			List<String>	tlogcontent	= null;

			@Override
			public byte visit(DBRole dbrole)
			{
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				commonFlowRecord.setEvent(TLog.AT_GM_CLEAR_DATA);
				commonFlowRecord.setArg(clearStone, clearMoney,
						clearVit);
				if (clearStone == 1)
					commonFlowRecord.addChange(dbrole
							.gmClearStone());
				if (clearMoney == 1)
					commonFlowRecord.addChange(dbrole
							.gmClearMoney());
				if (clearVit == 1)
					commonFlowRecord.addChange(dbrole.gmClearVit(gs
							.getTime()));
				tlogEvent.addCommonFlow(commonFlowRecord);
				tlogcontent = gs.getTLogger().toTlogEventStr(
						dbrole, tlogEvent);
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				commonFlowRecord.setEvent(TLog.AT_GM_CLEAR_DATA);
				commonFlowRecord.setArg(clearStone, clearMoney,
						clearVit);
				if (clearStone == 1)
					commonFlowRecord.addChange(role
							.gmClearStone());
				if (clearMoney == 1)
					commonFlowRecord.addChange(role
							.gmClearMoney());
				if (clearVit == 1)
					commonFlowRecord.addChange(role
							.gmClearVit());
				tlogEvent.addCommonFlow(commonFlowRecord);
				tlogcontent = gs.getTLogger().toTlogEventStr(
						role, tlogEvent);
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getTLogger().log(tlogcontent);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public interface SetGeneralLevelCallback
	{
		public void onCallback(int errcode);
	}

	public int setGeneralLevel(final int rid, final int gid, final int lvl,
			final SetGeneralLevelCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			int	ret	= -100;

			@Override
			public byte visit(DBRole dbrole)
			{
				ret = dbrole.gmSetGeneralLevel(gid, lvl);
				return 0 == ret ? LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE
						: LoginManager.CommonRoleVisitor.ERR_FAILED;
			}

			@Override
			public boolean visit(Role role)
			{
				ret = role.gmSetGeneralLevel(gid, lvl);
				return 0 == ret;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getLogger().info(
						"setGeneralLevel ret=" + ret);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public interface SetPetLevelCallback
	{
		public void onCallback(int errcode);
	}

	public int setPetLevel(final int rid, final int pid, final int lvl,
			final SetGeneralLevelCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			int	ret	= -100;

			@Override
			public byte visit(DBRole dbrole)
			{
				ret = dbrole.gmSetPetLevel(pid, lvl);
				return 0 == ret ? LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE
						: LoginManager.CommonRoleVisitor.ERR_FAILED;
			}

			@Override
			public boolean visit(Role role)
			{
				ret = role.gmSetPetLevel(pid, lvl);
				return 0 == ret;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				gs.getLogger().info("setPetLevel ret=" + ret);
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public interface QueryUserBaseInfoCallback
	{
		public void onCallback(int errcode, String roleName, int lvl,
				int money, int diamond, int status, int lays);
	}

	public int queryUserBaseInfo(final int rid,
			final QueryUserBaseInfoCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			String	roleName	= "";
			int	level		= 0;
			int	money		= 0;
			int	diamond		= 0;
			int	status		= 0;
			int	lays		= 0;

			@Override
			public byte visit(DBRole dbrole)
			{
				roleName = dbrole.name;
				level = dbrole.lvl;
				money = dbrole.money;
				diamond = dbrole.stone;
				status = dbrole.combatPos.get(0).catIndex + 1;
				lays = dbrole.marchState.stage;
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				roleName = role.name;
				level = role.lvl;
				money = role.money;
				diamond = role.stone;
				status = role.combatPos[0].catIndex + 1;
				lays = role.marchState.stage;
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{

				callback.onCallback(errcode, roleName, level,
						money, diamond, status, lays);
			}
		});
		return 0;
	}

	public interface UserMultiBanCallback
	{
		public void onCallback(int errcode);
	}

	public int userMultiBan(final int rid, final int playtypes,
			final int second, final String reason,
			final UserMultiBanCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{

			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleBanData banData = new DBRoleBanData(
						dbrole);
				banData.multiBan(playtypes, second, reason,
						gs.getTime());
				dbrole.banData = banData.getDBData();
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					role.banData.multiBan(playtypes,
							second, reason,
							gs.getTime());
				}
				if (role.isBanPLay())
					gs.getRPCManager().notifyBanPlayInfo(
							role.netsid,
							role.getBanPlayInfo());
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public interface UserMultiUnbanCallback
	{
		public void onCallback(int errcode);
	}

	public int userMultiUnban(final int rid, final int playtypes,
			final UserMultiUnbanCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{

			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleBanData banData = new DBRoleBanData(
						dbrole);
				banData.multiUnban(playtypes);
				dbrole.banData = banData.getDBData();
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				synchronized (role)
				{
					role.banData.multiUnban(playtypes);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode);
			}
		});
		return 0;
	}

	public interface QueryUsrSignGuildIDCallback
	{
		public void onCallback(int errcode, String roleName, int lvl,
				String usrSign, int guildId);
	}

	public int queryUsrSignGuildID(final int rid,
			final QueryUsrSignGuildIDCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			String	roleName	= "";
			int	level		= 0;
			String	usrSign		= "";
			int	guildId		= 0;

			@Override
			public byte visit(DBRole dbrole)
			{
				roleName = dbrole.name;
				level = dbrole.lvl;
				usrSign = dbrole.name;
				guildId = dbrole.forceID;
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				roleName = role.name;
				level = role.lvl;
				usrSign = role.name;
				guildId = role.forceInfo.id;
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{

				callback.onCallback(errcode, roleName, level,
						usrSign, guildId);
			}
		});
		return 0;
	}

	public interface ModifyRoleNameCallback
	{
		public void onCallback(int errcode);
	}

	public int modifyRoleName(final int rid, final String newName,
			final ModifyRoleNameCallback callback)
	{
		gs.getDB()
				.execute(new PutRoleNameIfAbsentTrans(
						rid,
						newName,
						new PutRoleNameIfAbsentCallback()
						{
							public void onCallback(
									boolean bOK)
							{
								if (bOK)
								{
									execCommonRoleVisitor(
											rid,
											new CommonRoleVisitor()
											{

												@Override
												public byte visit(
														DBRole dbrole)
												{
													dbrole.name = newName;
													CityManager.DBRoleCitys citys = new CityManager.DBRoleCitys(
															gs,
															dbrole,
															dbrole.citys);
													citys.changeBaseCitysName(newName);
													return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
												}

												@Override
												public boolean visit(
														Role role)
												{
													synchronized (role)
													{
														role.changeName(newName);
													}
													gs.getRPCManager()
															.syncRoleName(role.netsid,
																	role.name);
													return true;
												}

												@Override
												public void onCallback(
														boolean bDB,
														byte errcode,
														String rname)
												{
													callback.onCallback(errcode);
												}
											});
								} else
								{
									callback.onCallback(CommonRoleVisitor.ERR_FAILED);
								}
							}
						}));
		return 0;
	}

	public interface QueryUsrVipLevelCallback
	{
		public void onCallback(int errcode, int vip, int vipExp,
				int lackVipExp);
	}

	public int queryUsrVipLevel(final int rid,
			final QueryUsrVipLevelCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			int	vip		= 0;
			int	vipExp		= 0;
			int	lackVipExp	= 0;

			@Override
			public byte visit(DBRole dbrole)
			{
				vip = dbrole.lvlVIP;
				vipExp = dbrole.getStonePayTotal();
				lackVipExp = GameData.getInstance()
						.getNextVipNeedPay((byte) vip,
								vipExp);
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				vip = role.lvlVIP;
				;
				vipExp = role.getStonePayTotal();
				lackVipExp = GameData.getInstance()
						.getNextVipNeedPay((byte) vip,
								vipExp);
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{

				callback.onCallback(errcode, vip, vipExp,
						lackVipExp);
			}
		});
		return 0;
	}

	public interface QueryUsrCheckinCallback
	{
		public void onCallback(int errcode, int day, String reward);
	}

	public int queryUsrCheckin(final int rid,
			final QueryUsrCheckinCallback callback)
	{
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			int	day		= 0;
			String	rewardStr	= "";

			@Override
			public byte visit(DBRole dbrole)
			{
				byte[] now = new byte[2];
				SBean.CheckinCFGS cfg = GameData.getInstance()
						.getCheckinNow(gs.getTimeGMT(),
								now);
				if (cfg != null)
				{
					if (now[0] == dbrole.checkinLog.lastID)
					{
						day = dbrole.checkinLog.nFinished;
						if (dbrole.checkinLog.nFinished > 0
								&& dbrole.checkinLog.nFinished <= cfg.rewards
										.size())
						{
							SBean.CheckinRewardCFGS r = cfg.rewards
									.get(dbrole.checkinLog.nFinished - 1);
							SBean.DropEntryNew dropReal = r.reward
									.kdClone();
							if (r.lvlVIP > 0
									&& dbrole.lvlVIP >= r.lvlVIP)
								dropReal.arg *= 2;
							if (dbrole.getQQVIP() != 0
									&& dropReal.type == GameData.COMMON_TYPE_STONE)
								dropReal.arg = (int) (dropReal.arg * 1.2f);
							rewardStr = dropReal.type
									+ ","
									+ dropReal.id
									+ ","
									+ dropReal.arg;
						}
					}
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				byte[] now = new byte[2];
				SBean.CheckinCFGS cfg = GameData.getInstance()
						.getCheckinNow(gs.getTimeGMT(),
								now);
				if (cfg != null)
				{
					synchronized (role)
					{
						if (now[0] == role.checkinLog.lastID)
						{
							day = role.checkinLog.nFinished;
							if (role.checkinLog.nFinished > 0
									&& role.checkinLog.nFinished <= cfg.rewards
											.size())
							{
								SBean.CheckinRewardCFGS r = cfg.rewards
										.get(role.checkinLog.nFinished - 1);
								SBean.DropEntryNew dropReal = r.reward
										.kdClone();
								if (r.lvlVIP > 0
										&& role.lvlVIP >= r.lvlVIP)
									dropReal.arg *= 2;
								if (role.getQQVIP() != 0
										&& dropReal.type == GameData.COMMON_TYPE_STONE)
									dropReal.arg = (int) (dropReal.arg * 1.2f);
								rewardStr = dropReal.type
										+ ","
										+ dropReal.id
										+ ","
										+ dropReal.arg;
							}
						}
					}
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{

				callback.onCallback(errcode, day, rewardStr);
			}
		});
		return 0;
	}

	public interface SetInvitationFriendCallback
	{
		public void onCallback(int errorCode);
	}

	public int setInvitationFriend(final int targetrid, final int gsid,
			final int rid, final String rolename, final int level,
			final int vip, final short headicon,
			final int syncTime, final int lastLogout,
			final SetInvitationFriendCallback callback)
	{
		if (gsid == gs.getConfig().id && rid == targetrid)
		{
			callback.onCallback(-4);
			return 0;
		}
		execCommonRoleVisitor(targetrid, new CommonRoleVisitor()
		{
			int	errorCode	= -1;

			@Override
			public byte visit(DBRole dbrole)
			{
				errorCode = dbrole.onSetInvitationFriend(gsid,
						rid, rolename, level, vip,
						headicon, syncTime, lastLogout);
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				errorCode = role.onSetInvitationFriend(gsid,
						rid, rolename, level, vip,
						headicon, syncTime, lastLogout);
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(errcode == LoginManager.CommonRoleVisitor.ERR_OK ? errorCode
						: -3);
			}
		});
		return 0;
	}

	public interface UpdateInvitationFriendInfoCallback
	{
		public void onCallback(SBean.DBInvitationFriend info);
	}

	public int updateInvitationFriendInfo(final int rid,
			final UpdateInvitationFriendInfoCallback callback)
	{
		final int gsid = gs.getConfig().id;
		execCommonRoleVisitor(rid, new CommonRoleVisitor()
		{
			SBean.DBInvitationFriend	info;

			@Override
			public byte visit(DBRole dbrole)
			{
				info = dbrole.getSelfInvitationInfo(gsid,
						gs.getTime());
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role)
			{
				info = role.getSelfInvitationInfo(gsid,
						gs.getTime());
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode,
					String rname)
			{
				callback.onCallback(info);
			}
		});
		return 0;
	}

	public class LoginWhiteList
	{
		boolean		whileListOn	= false;
		Set<String>	openIDWhiteList	= new TreeSet<String>();

		LoginWhiteList()
		{

		}

		synchronized void setWhileList(boolean whileliston,
				Set<String> lst)
		{
			whileListOn = whileliston;
			openIDWhiteList = lst;
		}

		synchronized boolean canLogin(String openID)
		{
			return !whileListOn
					|| openIDWhiteList.contains(openID
							.toLowerCase());
		}

		void setCfgFile(final String filePath)
		{
			gs.mointorFileChanged(filePath, new Runnable()
			{
				@Override
				public void run()
				{
					reloadFile(filePath);
				}
			});
		}

		void reloadFile(String filePath)
		{
			try
			{
				boolean whitelistOn = false;
				Set<String> whiteList = new TreeSet<String>();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(
										filePath),
								"utf-8"));
				for (String line = in.readLine(); line != null; line = in
						.readLine())
				{
					// System.out.println(line);
					String linetrim = line.trim();
					if (!linetrim.startsWith("#"))
					{
						if (linetrim.startsWith("WhiteList"))
						{
							String[] strs = linetrim
									.split("\\s+",
											2);
							if (strs.length == 2
									&& strs[0].equals("WhiteList")
									&& strs[1].toLowerCase()
											.equals("on"))
							{
								whitelistOn = true;
							}
						} else if (!linetrim.isEmpty())
						{
							whiteList.add(linetrim
									.toLowerCase());
						}
					}
				}
				in.close();
				gs.getLogger()
						.info(this.getClass()
								.getSimpleName()
								+ " : reloadFile whilelist "
								+ (whitelistOn ? "on"
										: "off"));
				setWhileList(whitelistOn, whiteList);
				// TODO !!!
				if (whiteList.contains("close server"))
				{
					gs.shutdownBG();
				}
				//
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public class RegisterLimitList
	{
		boolean			registerLimitOn		= false;
		Map<Integer, Integer>	dayRegisterLimitList	= new TreeMap<Integer, Integer>();

		RegisterLimitList()
		{

		}

		synchronized void setRegisterLimitList(boolean registerLimitOn,
				Map<Integer, Integer> dayRegisterLimitList)
		{
			this.registerLimitOn = registerLimitOn;
			this.dayRegisterLimitList = dayRegisterLimitList;
			for (Map.Entry<Integer, Integer> e : dayRegisterLimitList
					.entrySet())
			{
				gs.getLogger()
						.info("day register limit : ("
								+ GameServer.getTimeStampStr(e
										.getKey() * 86400)
								+ ","
								+ e.getValue()
								+ ")");
			}
			// gs.getLogger().info("cur day " + gs.getDay() +
			// " limit : " + getCurRegisterLimit() + " " +
			// gs.getTime());
		}

		synchronized int getCurRegisterLimit()
		{
			int limit = Integer.MAX_VALUE;
			if (registerLimitOn)
			{
				Integer regesterLimit = dayRegisterLimitList
						.get(gs.getDay());
				if (regesterLimit != null)
					limit = regesterLimit;

			}
			return limit;
		}

		void setCfgFile(final String filePath)
		{
			gs.mointorFileChanged(filePath, new Runnable()
			{
				@Override
				public void run()
				{
					reloadFile(filePath);
				}
			});
		}

		void reloadFile(String filePath)
		{
			try
			{
				boolean registerLimitOn = false;
				Map<Integer, Integer> dayRegisterLimitList = new TreeMap<Integer, Integer>();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(
										filePath),
								"utf-8"));
				int lineNo = 1;
				for (String line = in.readLine(); line != null; line = in
						.readLine())
				{
					// System.out.println(line);
					String linetrim = line.trim();
					if (!linetrim.startsWith("#"))
					{
						if (linetrim.startsWith("RegisterLimit"))
						{
							String[] strs = linetrim
									.split("\\s+",
											2);
							if (strs.length == 2
									&& strs[0].equals("RegisterLimit")
									&& strs[1].toLowerCase()
											.equals("on"))
							{
								registerLimitOn = true;
							}
						} else if (!linetrim.isEmpty())
						{
							String[] strs = linetrim
									.split("\\s+",
											2);
							if (strs.length == 2)
							{
								try
								{
									Date date = new SimpleDateFormat(
											"yyyy-MM-dd")
											.parse(strs[0]);
									int limit = Integer
											.parseInt(strs[1]);
									if (date != null)
									{
										int dayTime = (int) (date
												.getTime() / 1000) + 8 * 3600;
										int key = gs.getDay(dayTime);
										dayRegisterLimitList
												.put(key,
														limit);
									}
								} catch (Exception e)
								{
									gs.getLogger()
											.warn("file line "
													+ lineNo
													+ ":"
													+ e.getMessage());
								}
							}
						}
					}
					lineNo++;
				}
				in.close();
				gs.getLogger()
						.info(this.getClass()
								.getSimpleName()
								+ " : reloadFile register Limit list "
								+ (registerLimitOn ? "on"
										: "off"));
				setRegisterLimitList(registerLimitOn,
						dayRegisterLimitList);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	static class GlobalRoleIDEncoder
	{
		private static final char[]		VALID_CHARS		= {
												'0',
												'1',
												'2',
												'3',
												'4',
												'5',
												'6',
												'7',
												'8',
												'9',
												'A',
												'B',
												'C',
												'D',
												'E',
												'F',
												'G',
												'H',
												'I',
												'J',
												'K',
												'L',
												'M',
												'N',
												'O',
												'P',
												'Q',
												'R',
												'S',
												'T',
												'U',
												'V',
												'W',
												'X',
												'Y',
												'Z' };
		private static final int		GLOBAL_ROLE_ID_LENGTH	= 16;
		private String				key			= "ksladslfghfkjhk";
		private MessageDigest			md;

		private static final Set<Character>	VALID_CHAR_SET		= new HashSet<Character>();
		static
		{
			for (int i = 0; i < VALID_CHARS.length; ++i)
			{
				VALID_CHAR_SET.add(VALID_CHARS[i]);
			}
		}

		GlobalRoleIDEncoder()
		{
			try
			{
				md = java.security.MessageDigest
						.getInstance("MD5");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private byte[] genEncryptCode(StringBuilder rawCode)
		{
			StringBuilder keyCode = new StringBuilder(key);
			keyCode.append(rawCode);
			return md.digest(keyCode.toString().getBytes());
		}

		public StringBuilder genEncryptCodeDigest(
				StringBuilder rawCode, int digestLength)
		{
			byte[] encryptCode = genEncryptCode(rawCode);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digestLength; ++i)
			{
				sb.append(getByteChar(encryptCode[i]));
			}
			return sb;
		}

		public static char getByteChar(byte b)
		{
			int val = 0xff & b;
			return VALID_CHARS[val % VALID_CHARS.length];
		}

		static long getGlobalRoleID(int gsid, int rid)
		{
			return ((long) rid << 32) | ((long) gsid & 0xffffffffL);
		}

		static int getGameServerID(long grid)
		{
			return (int) (grid & 0xffffffffL);
		}

		static int getRoleID(long grid)
		{
			return (int) ((grid >> 32) & 0xffffffffL);
		}

		static StringBuilder genGlobalRoleIDRawCode(int gsid, int roleid)
		{
			StringBuilder sb = new StringBuilder();
			long ugrid = getGlobalRoleID(gsid, roleid);
			while (ugrid > 0)
			{
				int index = (int) (ugrid % VALID_CHARS.length);
				ugrid /= VALID_CHARS.length;
				sb.append(VALID_CHARS[index]);
			}
			return sb.reverse();
		}

		static long genGlobalRoleIDRawCodeRevert(String rawCode)
		{
			long val = 0;
			for (int i = 0; i < rawCode.length(); ++i)
			{
				int bitVal = Character.getNumericValue(rawCode
						.charAt(i));
				val = (val * VALID_CHARS.length) + bitVal;
			}
			return val;
		}

		StringBuilder genGlobalRoleIDEncryptCode(StringBuilder rawCode)
		{
			StringBuilder code = genEncryptCodeDigest(
					rawCode,
					GLOBAL_ROLE_ID_LENGTH - 1
							- rawCode.length());
			return code;
		}

		StringBuilder genGlobalRoleIDCode(StringBuilder rawCode)
		{
			StringBuilder code = genGlobalRoleIDEncryptCode(rawCode);
			code.append(rawCode).append(
					getByteChar((byte) rawCode.length()));
			return code;
		}

		String getGlobalRoleIDCode(int gsid, int roleid)
		{
			return genGlobalRoleIDCode(
					genGlobalRoleIDRawCode(gsid, roleid))
					.toString();
		}

		long getGlobalRoleID(String code)
		{
			int len = Character.getNumericValue(code.charAt(code
					.length() - 1));
			int digestLen = code.length() - 1 - len;
			String rawCode = code.substring(digestLen,
					code.length() - 1);
			long ugrid = genGlobalRoleIDRawCodeRevert(rawCode);
			return ugrid;
		}

		boolean checkCodeValid(String code)
		{
			if (code.length() != GLOBAL_ROLE_ID_LENGTH)
				return false;
			for (int i = 0; i < code.length(); ++i)
			{
				char c = code.charAt(i);
				if (!VALID_CHAR_SET.contains(c))
					return false;
			}
			int len = Character.getNumericValue(code.charAt(code
					.length() - 1));
			int digestLen = code.length() - 1 - len;
			if (digestLen <= 0)
				return false;
			StringBuilder rawCode = new StringBuilder(
					code.substring(digestLen,
							code.length() - 1));
			String encryptCode = code.substring(0, digestLen);
			String calcEncryptCode = genGlobalRoleIDEncryptCode(
					rawCode).toString();
			return encryptCode.equals(calcEncryptCode);
		}

		static void testPrint(int gsid, int roleid)
		{
			GlobalRoleIDEncoder encoder = new GlobalRoleIDEncoder();
			String code = encoder.getGlobalRoleIDCode(gsid, roleid);
			long gurid = encoder.getGlobalRoleID(code);
			System.out.println("test gsid="
					+ gsid
					+ ", roleid="
					+ roleid
					+ " ...... "
					+ code
					+ ", "
					+ GlobalRoleIDEncoder
							.getGameServerID(gurid)
					+ ", "
					+ GlobalRoleIDEncoder.getRoleID(gurid));
		}
	}

	GlobalRoleIDEncoder getGlobalRoleIDEncoder()
	{
		return globalRoleIDEncoder;
	}

	public interface GetMarchEnemiesCallback
	{
		public void onCallback(List<SBean.DBRoleMarchEnemy> res);
	}

	public void getMarchEnemies(List<Integer> rids,
			final GetMarchEnemiesCallback callback)
	{
		final List<SBean.DBRoleMarchEnemy> res = new ArrayList<SBean.DBRoleMarchEnemy>();
		List<Integer> lst = new ArrayList<Integer>();
		for (int rid : rids)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
				lst.add(rid);
			else
			{
				SBean.DBRoleMarchEnemy result = new SBean.DBRoleMarchEnemy();
				r.getMarchEnemy(result);
				res.add(result);
			}
		}
		if (lst.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetMarchEnemiesTrans(
						lst,
						new LoginManager.GetMarchEnemiesCallback()
						{

							@Override
							public void onCallback(
									List<SBean.DBRoleMarchEnemy> data)
							{
								for (SBean.DBRoleMarchEnemy p : data)
								{
									if (p == null)
										continue;
									res.add(p);
								}
								callback.onCallback(res);
							}
						}));
	}

	public interface GetSuraEnemiesCallback
	{
		public void onCallback(List<SBean.DBSuraEnemy> res);
	}
	
	public void getSuraEnemies(List<Integer> rids, final GetSuraEnemiesCallback callback)
	{
		final List<SBean.DBSuraEnemy> res = new ArrayList<SBean.DBSuraEnemy>();
		List<Integer> lst = new ArrayList<Integer>();
		for(int rid : rids)
		{
			Role r = mapRoles.get(rid);
			if( r == null || r.isNull() )
				lst.add(rid);
			else
			{
				SBean.DBSuraEnemy result = new SBean.DBSuraEnemy();
				r.getSuraEnemy(result);
				res.add(result);
			}
		}
		if( lst.isEmpty() )
		{
			callback.onCallback(res);
			return;
		}
		
		gs.getDB().execute(new GetSuraEnemiesTrans(lst, new LoginManager.GetSuraEnemiesCallback(
				) {
			
			@Override
			public void onCallback(List<SBean.DBSuraEnemy> data)
			{
				for(SBean.DBSuraEnemy p : data)
				{
					if( p == null )
						continue;
					res.add(p);
				}
				callback.onCallback(res);
			}
		}));
	}
	
	public interface GetPVPStateCallback
	{
		public void onCallback(List<PVPCache> lst);
	}

	public int getJubaoStartDay()
	{
		return jubaoDay;
	}

	public void getPVPState(List<Integer> rids,
			final GetPVPStateCallback callback)
	{
		List<Integer> qlst1 = new ArrayList<Integer>();
		final List<PVPCache> res = new ArrayList<PVPCache>();
		for (int rid : rids)
		{
			PVPCacheData c = pvpCache.get(rid);
			if (c == null)
				qlst1.add(rid);
			else
				res.add(c);
		}
		if (qlst1.isEmpty())
		{
			callback.onCallback(res);
			return;
		}
		List<Integer> qlst2 = new ArrayList<Integer>();
		for (int rid : qlst1)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
				qlst2.add(rid);
			else
			{
				int pvpState = r.getPVPStateWithLock();
				updatePVPCache(rid, pvpState);
				res.add(new PVPCache(rid, pvpState));
			}
		}
		if (qlst2.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetPVPCacheDataTrans(
						qlst2,
						new LoginManager.GetPVPStateCallback()
						{

							@Override
							public void onCallback(
									List<PVPCache> lst)
							{
								// TODO
								// Auto-generated
								// method stub
								for (PVPCache p : lst)
								{
									if (p == null)
										continue;
									updatePVPCache(p.id,
											p.flag);
									res.add(p);
								}
								callback.onCallback(res);
							}
						}));
	}

	public interface GetDataBriefCallback
	{
		public void onCallback(List<RoleBriefCachePair> lst);
	}

	public void getDataBrief(List<Integer> rids, boolean ignoreCache,
			final GetDataBriefCallback callback)
	{
		int now = gs.getTime();
		List<Integer> qlst1 = new ArrayList<Integer>();
		final List<RoleBriefCachePair> res = new ArrayList<RoleBriefCachePair>();
		for (int rid : rids)
		{
			RoleBriefCacheData c = mapCache.get(rid);
			if (ignoreCache)
				qlst1.add(rid);
			else if (c == null)
				qlst1.add(rid);
			else if (c.isNeedRefresh(now))
			{
				qlst1.add(rid);
			} else
				res.add(new RoleBriefCachePair(rid, c));
		}
		if (qlst1.isEmpty())
		{
			callback.onCallback(res);
			return;
		}
		List<Integer> qlst2 = new ArrayList<Integer>();
		for (int rid : qlst1)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
				qlst2.add(rid);
			else
			{				
				RoleBriefCache c = r.getCacheDataWithLock();				
				if(r.activityRefresh()){
					c.activity=0;
				}
				updateRoleBriefCache(rid, c);
				res.add(new RoleBriefCachePair(rid, c));
			}
		}
		if (qlst2.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetRoleBriefCacheDataTrans(
						qlst2,
						new LoginManager.GetDataBriefCallback()
						{

							@Override
							public void onCallback(
									List<RoleBriefCachePair> lst)
							{
								// TODO
								// Auto-generated
								// method stub
								for (RoleBriefCachePair p : lst)
								{
									if (p == null)
										continue;
									updateRoleBriefCache(
											p.id,
											p.cache);
									res.add(p);
								}
								callback.onCallback(res);
							}
						}));
	}
	
	public void getDataBrief(int roleID,List<Integer> rids, boolean ignoreCache,
			final GetDataBriefCallback callback)
	{
		int now = gs.getTime();
		List<Integer> qlst1 = new ArrayList<Integer>();
		final List<RoleBriefCachePair> res = new ArrayList<RoleBriefCachePair>();
		for (int rid : rids)
		{
			RoleBriefCacheData c = mapCache.get(rid);
			if (ignoreCache)
				qlst1.add(rid);
			else if (c == null)
				qlst1.add(rid);
			else if (c.isNeedRefresh(now))
			{
				qlst1.add(rid);
			}else if (rid==roleID)
			{
				qlst1.add(rid);
			} else
				res.add(new RoleBriefCachePair(rid, c));
		}
		if (qlst1.isEmpty())
		{
			callback.onCallback(res);
			return;
		}
		List<Integer> qlst2 = new ArrayList<Integer>();
		for (int rid : qlst1)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
				qlst2.add(rid);
			else
			{				
				RoleBriefCache c = r.getCacheDataWithLock();				
				if(r.activityRefresh()){
					c.activity=0;
				}
				updateRoleBriefCache(rid, c);
				res.add(new RoleBriefCachePair(rid, c));
			}
		}
		if (qlst2.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetRoleBriefCacheDataTrans(
						qlst2,
						new LoginManager.GetDataBriefCallback()
						{

							@Override
							public void onCallback(
									List<RoleBriefCachePair> lst)
							{
								// TODO
								// Auto-generated
								// method stub
								for (RoleBriefCachePair p : lst)
								{
									if (p == null)
										continue;
									updateRoleBriefCache(
											p.id,
											p.cache);
									res.add(p);
								}
								callback.onCallback(res);
							}
						}));
	}

	public static class RoleLevelPair
	{
		public int	rid;
		public int	level;

		public RoleLevelPair(int rid, int lvl)
		{
			this.rid = rid;
			this.level = lvl;
		}
	}

	public interface GetRoleLevelCallback
	{
		public void onCallback(List<RoleLevelPair> lst);
	}

	public void getRoleLevel(List<Integer> rids,
			final GetRoleLevelCallback callback)
	{
		int now = gs.getTime();
		List<Integer> qlst1 = new ArrayList<Integer>();
		final List<RoleLevelPair> res = new ArrayList<RoleLevelPair>();
		for (int rid : rids)
		{
			RoleBriefCacheData c = mapCache.get(rid);
			if (c == null || c.isNeedRefresh(now))
				qlst1.add(rid);
			else
				res.add(new RoleLevelPair(rid, c.lvl));
		}
		if (qlst1.isEmpty())
		{
			callback.onCallback(res);
			return;
		}
		List<Integer> qlst2 = new ArrayList<Integer>();
		for (int rid : qlst1)
		{
			Role r = mapRoles.get(rid);
			if (r == null || r.isNull())
			{
				qlst2.add(rid);
			} else
			{
				RoleBriefCache c = r.getCacheDataWithLock();
				updateRoleBriefCache(rid, c);
				res.add(new RoleLevelPair(rid, c.lvl));
			}
		}
		if (qlst2.isEmpty())
		{
			callback.onCallback(res);
			return;
		}

		gs.getDB()
				.execute(new GetRoleBriefCacheDataTrans(
						qlst2,
						new LoginManager.GetDataBriefCallback()
						{

							@Override
							public void onCallback(
									List<RoleBriefCachePair> lst)
							{
								// TODO
								// Auto-generated
								// method stub
								for (RoleBriefCachePair p : lst)
								{
									if (p == null)
										continue;
									updateRoleBriefCache(
											p.id,
											p.cache);
									res.add(new RoleLevelPair(
											p.id,
											p.cache.lvl));
								}
								callback.onCallback(res);
							}
						}));
	}

	public static class RoleLevelExpRank
	{
		public int	rid;
		public int	level;
		public int	exp;
		public int	rank	= 200000;
		public String	name	= "";

		public RoleLevelExpRank()
		{

		}

		public boolean isTopBetterTo(int lvl, int exp, int rank)
		{
			if (this.level > lvl)
				return true;
			else if (this.level < lvl)
				return false;
			else if (this.exp > exp)
				return true;
			else if (this.exp < exp)
				return false;
			else if (this.rank < rank)
				return true;
			return false;
		}
	}

	public static class RoleRankPair
	{
		public int	rid;
		public int	rank;

		public RoleRankPair(int rid, int rank)
		{
			this.rid = rid;
			this.rank = rank;
		}
	}

	interface GetTopRoleLevelExpRankCallback
	{
		void onCallback(RoleLevelExpRank topler);
	}

	public void queryTopRoleLevelExpRank(List<Integer> lst,
			GetTopRoleLevelExpRankCallback callback)
	{
		List<RoleRankPair> dbqlst = new ArrayList<RoleRankPair>();
		RoleLevelExpRank topler = new RoleLevelExpRank();
		int rank = 1;
		for (int rid : lst)
		{
			Role r = mapRoles.get(rid);
			if (r != null && !r.isNull())
			{
				if (!topler.isTopBetterTo(r.lvl, r.exp, rank))
				{
					topler.rid = rid;
					topler.level = r.lvl;
					topler.exp = r.exp;
					topler.rank = rank;
					topler.name = r.name;
				}
			} else
			{
				dbqlst.add(new RoleRankPair(rid, rank));
			}
			++rank;
		}
		gs.getDB().execute(
				new GetTopRoleLevelExpRankTrans(topler, dbqlst,
						callback));
	}

	public class GetTopRoleLevelExpRankTrans implements Transaction
	{
		public GetTopRoleLevelExpRankTrans(RoleLevelExpRank topler,
				List<RoleRankPair> roleranks,
				GetTopRoleLevelExpRankCallback callback)
		{
			this.topler = topler;
			this.roleranks = roleranks;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			for (RoleRankPair rr : roleranks)
			{
				DBRole dbrole = role.get(rr.rid);
				if (dbrole != null)
				{
					if (!topler.isTopBetterTo(dbrole.lvl,
							dbrole.exp, rr.rank))
					{
						topler.rid = rr.rid;
						topler.level = dbrole.lvl;
						topler.exp = dbrole.exp;
						topler.rank = rr.rank;
						topler.name = dbrole.name;
					}
				}
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(topler);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole>		role;

		public RoleLevelExpRank				topler;
		public final List<RoleRankPair>			roleranks;
		public final GetTopRoleLevelExpRankCallback	callback;
	}

	public void setRegNum(int regNum)
	{
		this.regNum = regNum;
	}

	public int getRegNum()
	{
		return regNum;
	}

	public int getGuestOnline()
	{
		return guestNum;
	}

	public void setGuestOnline(int guest)
	{
		this.guestNum = guest;
	}

	public interface GetPVPInfoCallback
	{
		public void onCallback(String rname, short lvl,
				short headIconID, List<DBRoleGeneral> generals,
				List<DBPetBrief> epets,
				List<SBean.DBRelation> erelation,
				List<SBean.DBGeneralStone> egStone);
	}

	public void getPVPInfo(int rid, final byte pvpType,
			final boolean bAttack, GetPVPInfoCallback callback)
	{
		Role role = mapRoles.get(rid);
		if (role != null)
		{
			if (role.isNull())
			{
				callback.onCallback(null, (short) 0, (short) 0,
						null, null, null, null);
			} else
			{
				List<DBRoleGeneral> lst = null;
				List<DBPetBrief> plst = null;
				List<SBean.DBRelation> relation = null;
				List<DBGeneralStone> gStones = null;
				synchronized (role)
				{
					lst = role.copyArenaGeneralsWithoutLock();
					plst = role.copyArenaPetsWithoutLock();
					relation = role.getActiveRelationsWithoutLock();
					gStones = role.copyGeneralStoneWithoutLock();
				}
				callback.onCallback(role.name, role.lvl,
						role.headIconID, lst, plst,
						relation, gStones);
			}
			return;
		}
		//
		gs.getDB().execute(
				new GetPVPInfoTrans(rid, pvpType, bAttack,
						callback));
	}

	public Role getOnlineRole()
	{
		synchronized (this)
		{
			if (onlineIds.isEmpty())
				return null;

			int size = onlineIds.size();
			int index = 0;
			while (index < size)
			{
				index++;
				int id = onlineIds.get(curOnlineId);
				Role r = mapRoles.get(id);

				curOnlineId++;
				if (curOnlineId >= onlineIds.size())
					curOnlineId = 0;

				if (r != null && !r.isNull() && r.netsid > 0)
					return r;
			}
		}
		return null;
	}

	public void useCDKey(int rid, int batchID, int genID, String key,
			UseCDKeyCallback callback)
	{
		gs.getDB().execute(
				new UseCDKeyTrans(rid, batchID, genID, key,
						callback));
	}

	static class CDKeyRoleLog
	{
		int					roleID;
		Map<Integer, Map<String, Integer>>	logs	= new TreeMap<Integer, Map<String, Integer>>();

		public CDKeyRoleLog(int rid)
		{
			roleID = rid;
		}

		public void addUsedInfo(int batchId, int genId, String key)
		{
			Map<String, Integer> batchKeyinfo = logs.get(batchId);
			if (batchKeyinfo == null)
			{
				batchKeyinfo = new TreeMap<String, Integer>();
				logs.put(batchId, batchKeyinfo);
			}
			batchKeyinfo.put(key, genId);
		}

		public void removeUsedInfo(int batchId, String key)
		{
			Map<String, Integer> batchKeyinfo = logs.get(batchId);
			if (batchKeyinfo != null)
			{
				batchKeyinfo.remove(key);
			}
		}

		boolean isUseBatchKey(int batchId)
		{
			Map<String, Integer> batchKeyinfo = logs.get(batchId);
			if (batchKeyinfo != null)
			{
				return !batchKeyinfo.isEmpty();
			}
			return false;
		}
	}

	public class CDKeyExchange
	{
		Map<String, Integer>		keyUsed			= new TreeMap<String, Integer>();
		Map<Integer, CDKeyRoleLog>	roleUsed		= new TreeMap<Integer, CDKeyRoleLog>();
		CDKeyGen			cdkeyGen		= new CDKeyGen();
		public static final int		CDKEY_EXCHANGE_SUCCESS	= 0;
		public static final int		CDKEY_INVALID		= -1;
		public static final int		CDKEY_USED		= -2;
		public static final int		BATCH_KEY_USED		= -3;
		public static final int		KEY_NOT_GO_INTO_EFFECT	= -4;
		public static final int		DATA_BASE_ERROR		= -5;

		public CDKeyExchange()
		{

		}

		public void addCDKeyRoleUsedInfo(int rid, int batchId,
				int genId, String key)
		{
			keyUsed.put(key, rid);
			CDKeyRoleLog cdkeyRoleLog = roleUsed.get(rid);
			if (cdkeyRoleLog == null)
			{
				cdkeyRoleLog = new CDKeyRoleLog(rid);
				roleUsed.put(rid, cdkeyRoleLog);
			}
			cdkeyRoleLog.addUsedInfo(batchId, genId, key);
		}

		public void cancelAddCDKeyRoleUsedInfo(int rid, int batchId,
				int genId, String key)
		{
			keyUsed.remove(key);
			CDKeyRoleLog cdkeyRoleLog = roleUsed.get(rid);
			if (cdkeyRoleLog != null)
			{
				cdkeyRoleLog.removeUsedInfo(batchId, key);
			}
		}

		public boolean isKeyUsed(String key)
		{
			return keyUsed.containsKey(key);
		}

		private boolean isRoleUseBatchKey(int roleID, int batchID)
		{
			CDKeyRoleLog cdkeyRoleLog = roleUsed.get(roleID);
			if (cdkeyRoleLog != null)
			{
				return cdkeyRoleLog.isUseBatchKey(batchID);
			}
			return false;
		}

		public boolean isRoleCanUseBatchKey(int roleID, int batchID)
		{
			return batchID == 0
					|| !isRoleUseBatchKey(roleID, batchID);
		}

		public void setUsedCDKeyInfo(List<SBean.CDKeyLog> cdkeylogs)
		{
			keyUsed.clear();
			roleUsed.clear();
			for (SBean.CDKeyLog keylog : cdkeylogs)
			{
				addCDKeyRoleUsedInfo(keylog.roleID,
						keylog.batchID, keylog.genID,
						keylog.key);
			}
		}

		// public synchronized boolean testUseCDKey(int rid, int
		// batchID, int genID, String key)
		// {
		// if (!isKeyUsed(key) && (batchID == 0 ||
		// !isRoleUseBatchKey(rid, batchID)))
		// {
		// addCDKeyRoleUsedInfo(rid, batchID, genID, key);
		// return true;
		// }
		// return false;
		// }

		public void exchangeCDKey(final int roleID, String key,
				GameActivities.GiftPackageConfig cfg,
				final Role.ExchangeCDKeyCallback callback)
		{
			final String upperkey = key.toUpperCase();
			final CDKeyGen.CDKeyIDInfo keyInfo = cdkeyGen
					.getCodeIDInfo(upperkey);
			if (keyInfo.batchID < 0 || keyInfo.genID < 0)
			{
				callback.onCallback(keyInfo.batchID,
						keyInfo.genID, CDKEY_INVALID,
						null, null, null);
				return;
			}
			GameActivities.GenPackageGift gpg = cfg.getGift(
					keyInfo.batchID, keyInfo.genID);
			if (gpg == null)
			{
				callback.onCallback(keyInfo.batchID,
						keyInfo.genID, CDKEY_INVALID,
						null, null, null);
				return;
			}
			if (!gpg.isValid() || !gpg.isInValidTime(gs.getTime()))
			{
				callback.onCallback(keyInfo.batchID,
						keyInfo.genID,
						KEY_NOT_GO_INTO_EFFECT, null,
						null, null);
				return;
			}
			synchronized (this)
			{
				if (isKeyUsed(upperkey))
				{
					callback.onCallback(keyInfo.batchID,
							keyInfo.genID,
							CDKEY_USED, null, null,
							null);
					return;
				}
				if (!isRoleCanUseBatchKey(roleID,
						keyInfo.batchID))
				{
					callback.onCallback(keyInfo.batchID,
							keyInfo.genID,
							BATCH_KEY_USED, null,
							null, null);
					return;
				}
				addCDKeyRoleUsedInfo(roleID, keyInfo.batchID,
						keyInfo.genID, upperkey);
				final List<SBean.DropEntryNew> gift = gpg.genGift;
				final String title = gpg.getTitle();
				final String content = gpg.getContent();
				useCDKey(roleID, keyInfo.batchID,
						keyInfo.genID, upperkey,
						new UseCDKeyCallback()
						{
							public void onCallback(
									int errcode)
							{
								if (errcode != CDKEY_EXCHANGE_SUCCESS)
								{
									synchronized (this)
									{
										cancelAddCDKeyRoleUsedInfo(
												roleID,
												keyInfo.batchID,
												keyInfo.genID,
												upperkey);
									}
								}
								callback.onCallback(
										keyInfo.batchID,
										keyInfo.genID,
										errcode,
										title,
										content,
										gift);
							}
						});
			}
		}

	}

	void setCDKeyUsedInfo(List<SBean.CDKeyLog> cdkeylogs)
	{
		cdkeyExchange.setUsedCDKeyInfo(cdkeylogs);
	}

	void exchangeCDKey(Role role, String key,
			GameActivities.GiftPackageConfig cfg,
			Role.ExchangeCDKeyCallback callback)
	{
		cdkeyExchange.exchangeCDKey(role.id, key, cfg, callback);
	}

	// public class SaveLevelLimitDataTrans implements Transaction
	// {
	// public SaveLevelLimitDataTrans(List<SBean.LevelLimit> levelLimits)
	// {
	// this.levelLimits = levelLimits;
	// }
	//
	// @Override
	// public boolean doTransaction()
	// {
	// gs.getLogger().info("save level limit data to DB...");
	// byte[] key = Stream.encodeStringLE("levellimit");
	// byte[] data = Stream.encodeListLE(levelLimits);
	// world.put(key, data);
	// return true;
	// }
	//
	//
	// @Override
	// public void onCallback(ErrorCode errcode)
	// {
	// if( errcode != ErrorCode.eOK )
	// {
	// gs.getLogger().warn("level limit save failed errcode="+errcode);
	// }
	// }
	//
	// @AutoInit
	// public Table<byte[], byte[]> world;
	//
	// public final List<SBean.LevelLimit> levelLimits;
	// }
	//
	// public DynamicLevelLimit getDynamicLevelLimit()
	// {
	// return dll;
	// }
	//
	// public class DynamicLevelLimit
	// {
	// List<SBean.LevelLimit> levelLimits = new
	// ArrayList<SBean.LevelLimit>();
	// short curLevelLimit = 0;
	// DynamicLevelLimit()
	// {
	//
	// }
	//
	// void init(List<SBean.LevelLimit> lst)
	// {
	// if (lst != null)
	// {
	// levelLimits.clear();
	// levelLimits.addAll(lst);
	// for (SBean.LevelLimit ll : lst)
	// {
	// gs.getLogger().info("Dynamic Level Limit : level=" + ll.level +
	// ", openTime=" +
	// GameServer.getTimeStampStr(ll.openTime*86400+GameData.getInstance().getRoleCmnCFG().timeDayRefresh
	// * 60));
	// }
	// }
	// else
	// {
	// update();
	// }
	// updateCurLevelLimit();
	// }
	//
	// void save()
	// {
	// List<SBean.LevelLimit> oldll = levelLimits;
	// List<SBean.LevelLimit> dupll = new ArrayList<SBean.LevelLimit>();
	// dupll.addAll(oldll);
	// gs.getDB().execute(new SaveLevelLimitDataTrans(dupll));
	// }
	//
	// void update()
	// {
	// // if (true)
	// // {
	// // return;
	// // }
	// gs.getLogger().info("DynamicLevelLimit update");
	// if (!levelLimits.isEmpty())
	// {
	// int nowDay = gs.getDayCommon();
	// SBean.LevelLimit ll = levelLimits.get(0);
	// if (ll.openTime > nowDay)
	// {
	// return;
	// }
	// }
	// SBean.LevelLimitCFGS cfgs =
	// GameData.getInstance().getLevelLimitCFG();
	// List<Integer> roles =
	// gs.getArenaManager().dupTopRankRoles(cfgs.sampleCount);
	// List<Integer> validRoles = new ArrayList<Integer>();
	// for (int rid : roles)
	// {
	// if (rid > 0)
	// validRoles.add(rid);
	// }
	// if (validRoles.isEmpty())
	// return;
	// getRoleLevel(validRoles, new GetRoleLevelCallback()
	// {
	// public void onCallback(List<RoleLevelPair> lst)
	// {
	// List<SBean.LevelLimit> lllst = updateLevelLimit(lst);
	// if (lllst != null)
	// {
	// levelLimits = lllst;
	// save();
	// updateCurLevelLimit();
	// }
	// }
	// });
	// }
	//
	// public void updateCurLevelLimit()
	// {
	// curLevelLimit = (short)calcCurLevelLimit();
	// }
	//
	// public short getCurLevelLimit()
	// {
	// return curLevelLimit;
	// }
	//
	// int calcCurLevelLimit()
	// {
	// //gs.getLogger().info("calcCurLevelLimit curDynamicLevelLimit="+getCurDynamicLevelLimit()+", RoleLevelLimit="+gs.getRoleLevelLimit()+", resLevelLimit="+GameData.getInstance().getLevelLimitCFG().resorceLevelLimit);
	// int curDynamicLevelLimit = getCurDynamicLevelLimit();
	// if (curDynamicLevelLimit <= 0)
	// return gs.getRoleLevelLimit();
	// int resLevelLimit =
	// GameData.getInstance().getLevelLimitCFG().resorceLevelLimit;
	// return curDynamicLevelLimit > resLevelLimit ? resLevelLimit :
	// curDynamicLevelLimit;
	// }
	//
	// int getCurDynamicLevelLimit()
	// {
	// List<SBean.LevelLimit> oldll = levelLimits;
	// int nowDay = gs.getDayCommon();
	// for (SBean.LevelLimit e : oldll)
	// {
	// if (e.openTime <= nowDay)
	// {
	// return e.level;
	// }
	// }
	// return 0;
	// }
	//
	// SBean.LevelLimit getNextDynamicLevelLimit()
	// {
	// List<SBean.LevelLimit> oldll = levelLimits;
	// if (!oldll.isEmpty())
	// {
	// SBean.LevelLimit ll = oldll.get(0);
	// int nowDay = gs.getDayCommon();
	// if (ll.openTime > nowDay)
	// {
	// return ll;
	// }
	// }
	// return null;
	// }
	//
	// float getCurLevelLimitExpAdd(int level)
	// {
	// // if (true)
	// // {
	// // return 1.0f;
	// // }
	// if (level < 10)
	// return 1.0f;
	// int ll = getCurDynamicLevelLimit();
	// if (ll <= 0)
	// return 1.0f;
	// SBean.LevelLimitItemCFGS cfg = getLevelLimitCfg(ll);
	// if (cfg == null)
	// return 1.0f;
	// if (level <= cfg.level-40)
	// return 1.0f + cfg.lessThan40ExpAdd/100.0f;
	// if (level <= cfg.level-20)
	// return 1.0f + cfg.lessThan20ExpAdd/100.0f;
	// return 1.0f;
	// }
	//
	// SBean.LevelLimitItemCFGS getLevelLimitCfg(int levellimit)
	// {
	// SBean.LevelLimitCFGS cfgs =
	// GameData.getInstance().getLevelLimitCFG();
	// for (SBean.LevelLimitItemCFGS cfg : cfgs.levels)
	// {
	// if (cfg.level == levellimit)
	// return cfg;
	// }
	// return null;
	// }
	//
	// SBean.LevelLimitItemCFGS getNextLevelLimitCfg(int curLevelLimit)
	// {
	// SBean.LevelLimitCFGS cfgs =
	// GameData.getInstance().getLevelLimitCFG();
	// for (SBean.LevelLimitItemCFGS cfg : cfgs.levels)
	// {
	// if (cfg.level > curLevelLimit)
	// return cfg;
	// }
	// return null;
	// }
	//
	//
	// List<SBean.LevelLimit> initCreateLevelLimit(List<RoleLevelPair>
	// roleslevel)
	// {
	// List<SBean.LevelLimit> oldll = levelLimits;
	// if (!oldll.isEmpty())
	// return null;
	// int maxLvl = 0;
	// for (RoleLevelPair rl : roleslevel)
	// {
	// if (rl.level > maxLvl)
	// maxLvl = rl.level;
	// }
	//
	// SBean.LevelLimitItemCFGS levelLimitcfg = null;
	// SBean.LevelLimitCFGS cfgs =
	// GameData.getInstance().getLevelLimitCFG();
	// for (SBean.LevelLimitItemCFGS cfg : cfgs.levels)
	// {
	// if (cfg.level >= maxLvl)
	// {
	// levelLimitcfg = cfg;
	// break;
	// }
	// }
	// if (levelLimitcfg == null)
	// {
	// levelLimitcfg = cfgs.levels.get(cfgs.levels.size()-1);
	// }
	// int nowDay = gs.getDayCommon();
	// int openDay = nowDay-levelLimitcfg.minDuration/2;
	// List<SBean.LevelLimit> duplevelLimits = new
	// ArrayList<SBean.LevelLimit>();
	// duplevelLimits.add(new SBean.LevelLimit(levelLimitcfg.level,
	// openDay));
	// gs.getLogger().info("DynamicLevelLimit initCreateLevelLimit level=" +
	// levelLimitcfg.level + ", openTime=" +
	// GameServer.getTimeStampStr(openDay*86400+GameData.getInstance().getRoleCmnCFG().timeDayRefresh
	// * 60));
	// return duplevelLimits;
	// }
	//
	// List<SBean.LevelLimit> updateLevelLimit(List<RoleLevelPair>
	// roleslevel)
	// {
	// gs.getLogger().info("DynamicLevelLimit updateLevelLimit...");
	// List<SBean.LevelLimit> oldll = levelLimits;
	// if (oldll.isEmpty())
	// {
	// return initCreateLevelLimit(roleslevel);
	// }
	// int nowDay = gs.getDayCommon();
	// SBean.LevelLimit curll = oldll.get(0);
	// if (curll.openTime < nowDay)
	// {
	// SBean.LevelLimitItemCFGS curcfg = getLevelLimitCfg(curll.level);
	// SBean.LevelLimitItemCFGS nextcfg = getNextLevelLimitCfg(curll.level);
	// if (curcfg != null && nextcfg != null)
	// {
	// int openDay = 0;
	// if (testNextCanOpen(roleslevel, curcfg))
	// {
	// openDay = nowDay + nextcfg.openSpan;
	// if (openDay < curll.openTime + curcfg.minDuration)
	// openDay = curll.openTime + curcfg.minDuration;
	// if (openDay > curll.openTime + curcfg.maxDuration)
	// openDay = curll.openTime + curcfg.maxDuration;
	// }
	// else
	// {
	// if (curll.openTime + curcfg.maxDuration < nowDay + nextcfg.openSpan)
	// openDay = curll.openTime + curcfg.maxDuration;
	// }
	// if (openDay != 0)
	// {
	// List<SBean.LevelLimit> duplevelLimits = new
	// ArrayList<SBean.LevelLimit>();
	// duplevelLimits.addAll(oldll);
	// duplevelLimits.add(0, new SBean.LevelLimit(nextcfg.level, openDay));
	// gs.getLogger().info("DynamicLevelLimit updateLevelLimit level=" +
	// nextcfg.level + ", openTime=" +
	// GameServer.getTimeStampStr(openDay*86400+GameData.getInstance().getRoleCmnCFG().timeDayRefresh
	// * 60));
	// return duplevelLimits;
	// }
	// }
	//
	// }
	// return null;
	// }
	//
	// boolean testNextCanOpen(List<RoleLevelPair> roleslevel,
	// SBean.LevelLimitItemCFGS curLevelLimitcfg)
	// {
	// SBean.LevelLimitCFGS cfgs =
	// GameData.getInstance().getLevelLimitCFG();
	// int gelCount = 0;
	// for (RoleLevelPair rl : roleslevel)
	// {
	// int l = rl.level;
	// if (l >= curLevelLimitcfg.level)
	// ++gelCount;
	// }
	// return gelCount >= cfgs.openCountReq;
	// }
	// }
	//
	// public static class LevelLimitInfo
	// {
	// public int levelLimit;
	// public int lessThan20ExpAdd;
	// public int lessThan40ExpAdd;
	// public int nextLevelLimit;
	// public int nextLevelLimitOpenTime;
	// public int nextLevelShowDay;
	// public int resLevelLimit;
	// }
	//
	// public LevelLimitInfo getLevelLimitInfo()
	// {
	// int levellimit = getDynamicLevelLimit().getCurDynamicLevelLimit();
	// if (levellimit == 0)
	// return null;
	// SBean.LevelLimitItemCFGS cfg =
	// getDynamicLevelLimit().getLevelLimitCfg(levellimit);
	// if (cfg == null)
	// return null;
	// LevelLimitInfo info = new LevelLimitInfo();
	// info.levelLimit = levellimit;
	// info.lessThan20ExpAdd = cfg.lessThan20ExpAdd;
	// info.lessThan40ExpAdd = cfg.lessThan40ExpAdd;
	// SBean.LevelLimit ll =
	// getDynamicLevelLimit().getNextDynamicLevelLimit();
	// if (ll != null)
	// {
	// info.nextLevelLimit = ll.level;
	// info.nextLevelLimitOpenTime = ll.openTime;
	// SBean.LevelLimitItemCFGS nextcfg =
	// getDynamicLevelLimit().getLevelLimitCfg(ll.level);
	// info.nextLevelShowDay = nextcfg != null ? nextcfg.showSpan : 0;
	// }
	// info.resLevelLimit =
	// GameData.getInstance().getLevelLimitCFG().resorceLevelLimit;
	// return info;
	// }

	public class SaveTopLevelataTrans implements Transaction
	{
		public SaveTopLevelataTrans(SBean.TopLevelRoleInfo toplevelInfo)
		{
			this.toplevelInfo = toplevelInfo;
		}

		@Override
		public boolean doTransaction()
		{
			gs.getLogger().info("save top level data to DB...");
			byte[] key = Stream.encodeStringLE("toplevel");
			byte[] data = Stream.encodeLE(toplevelInfo);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
			{
				gs.getLogger().warn(
						"top level save failed errcode="
								+ errcode);
			}
		}

		@AutoInit
		public Table<byte[], byte[]>		world;

		public final SBean.TopLevelRoleInfo	toplevelInfo;
	}

	public TopLevel getTopLevel()
	{
		return tl;
	}

	public static class TopLevelHaloInfo
	{
		public int	toplevel;
		public int	rid;
		public String	rname;
		public int	lessThan20ExpAdd;
		public int	lessThan40ExpAdd;

		public TopLevelHaloInfo(int toplevel, int rid, String rname,
				int lessThan20ExpAdd, int lessThan40ExpAdd)
		{
			this.toplevel = toplevel;
			this.rid = rid;
			this.rname = rname;
			this.lessThan20ExpAdd = lessThan20ExpAdd;
			this.lessThan40ExpAdd = lessThan40ExpAdd;
		}
	}

	public void queryRoleExistsByOtherGS(String vopenID,
			final QueryRoleExistsByOtherGSCallBack callBack)
	{
		gs.getLoginManager().getRoleIDByOpendID(vopenID,
				new GetRoleIDByOpenIDCallback()
				{
					@Override
					public void onCallback(String openID,
							Integer roleID)
					{
						callBack.onCallBack(openID,
								roleID);
					}
				});

	}

	public TopLevelHaloInfo getTopLevelHaloInfo()
	{
		SBean.TopLevelRoleInfo info = this.getTopLevel()
				.getTopLevelRoleInfo();
		if (info.level <= 0)
			return null;
		SBean.TopLevelAuraCFGS cfg = GameData.getInstance()
				.getTopLevelAuraCFG();
		return new TopLevelHaloInfo(info.level, info.roleID,
				info.roleName, cfg.lessThan20ExpAdd,
				cfg.lessThan40ExpAdd);
	}

	public void expatriateDBRole(ExpatriateTransRoleReq req)
	{
		gs.getDB().execute(
				new ExpatriateUserTrans(req.srcgs, req.srcgs,
						req.tag, req.id, req.data));
	}

	public class TopLevel
	{
		SBean.TopLevelRoleInfo	info	= new SBean.TopLevelRoleInfo(0,
								0, "");

		public TopLevel()
		{

		}

		void init(SBean.TopLevelRoleInfo info)
		{
			if (info != null)
			{
				this.info = info;
			}
		}

		void save()
		{
			gs.getDB().execute(new SaveTopLevelataTrans(info));
		}

		int getTopLeve()
		{
			return info.level;
		}

		public void update()
		{
			gs.getLogger().info("TopLevel update");
			List<Integer> roles = gs
					.getArenaManager()
					.dupTopRankRoles(
							GameData.getInstance()
									.getTopLevelAuraCFG().sampleCount);
			List<Integer> validRoles = new ArrayList<Integer>();
			for (int rid : roles)
			{
				if (rid > 0)
					validRoles.add(rid);
			}
			if (validRoles.isEmpty())
				return;
			calcTopLevelExpRankRole(validRoles);
		}

		void calcTopLevelExpRankRole(List<Integer> rids)
		{
			gs.getLogger().info("TopLevel calcTopLevelRole...");
			queryTopRoleLevelExpRank(rids,
					new GetTopRoleLevelExpRankCallback()
					{
						public void onCallback(
								RoleLevelExpRank topler)
						{
							if (topler.rid != 0)
							{
								if (info.level <= topler.level)
								{
									info = new SBean.TopLevelRoleInfo(
											topler.level,
											topler.rid,
											topler.name);
									save();
								}
							}
						}
					});
		}

		float getCurLevelExpAdd(int level)
		{
			if (level < 10)
				return 1.0f;
			if (info.level <= 0)
				return 1.0f;
			SBean.TopLevelAuraCFGS cfg = GameData.getInstance()
					.getTopLevelAuraCFG();
			if (cfg == null)
				return 1.0f;
			if (level <= info.level - 40)
				return 1.0f + 100 / 100.0f;
			if (level <= info.level - 20)
				return 1.0f + 50 / 100.0f;
			return 1.0f;
		}

		SBean.TopLevelRoleInfo getTopLevelRoleInfo()
		{
			return this.info;
		}
	}

	GameServer					gs;
	// sid -> roleid
	Map<Integer, Integer>				maps2r				= new ConcurrentHashMap<Integer, Integer>();
	// roleid -> role
	ConcurrentMap<Integer, Role>			mapRoles			= new ConcurrentHashMap<Integer, Role>();
	// forceid -> force
	ConcurrentMap<Integer, Force>			mapForces			= new ConcurrentHashMap<Integer, Force>();
	// roleid -> role brief cache data
	ConcurrentMap<Integer, RoleBriefCacheData>	mapCache			= new ConcurrentHashMap<Integer, RoleBriefCacheData>();
	// roleid -> pvp flag cache data
	ConcurrentMap<Integer, PVPCacheData>		pvpCache			= new ConcurrentHashMap<Integer, PVPCacheData>();
	ConcurrentMap<Integer, UserLoginStub>		loginMap			= new ConcurrentHashMap<Integer, UserLoginStub>();
	NavigableMap<Integer, LevelRoleCache>		levelCache			= new TreeMap<Integer, LevelRoleCache>();
	ActiveRoles					actives				= new ActiveRoles();
	private WorldChatCache				wcCache				= new WorldChatCache();
	private ConcurrentMap<Integer, WorldChatCache>	fcCache				= new ConcurrentHashMap<Integer, WorldChatCache>();

	private List<Integer>				onlineIds			= null;
	private int					onlineIdChangeTime		= 0;
	private int					curOnlineId			= 0;

	private List<SBean.SysMail>			worldMails			= new ArrayList<SBean.SysMail>();
	private int					lastWorldMailID			= 0;

	// private List<SBean.SysMail> delayMails = new
	// ArrayList<SBean.SysMail>();

	private List<SBean.Broadcast>			broadcasts			= new ArrayList<SBean.Broadcast>();
	private int					lastBroadcastsModifyTime	= 0;

	int						jubaoDay			= -1;

	private volatile int				regNum				= 0;
	private volatile int				guestNum			= 0;

	private CDKeyExchange				cdkeyExchange			= new CDKeyExchange();

	LoginWhiteList					loginWhiteList			= new LoginWhiteList();
	RegisterLimitList				registerLimitList		= new RegisterLimitList();
	GlobalRoleIDEncoder				globalRoleIDEncoder		= new GlobalRoleIDEncoder();

	GSStat						gsStat				= new GSStat();

	// DynamicLevelLimit dll = new DynamicLevelLimit();
	TopLevel					tl				= new TopLevel();

	public static final int				BAN_PLAY_TYPE_COUNT		= 6;
	public static final int				BAN_LOGIN_INDEX			= 0;
	public static final int				BAN_CHAT_INDEX			= 1;
	public static final int				BAN_BATTLE_INDEX		= 2;
	public static final int				BAN_MARCH_INDEX			= 3;
	public static final int				BAN_CHIBI_INDEX			= 4;
	public static final int				BAN_ARENA_INDEX			= 5;

}

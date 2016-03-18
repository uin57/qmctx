
package i3k.gs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import i3k.DBForce;
import i3k.DBMail;
import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.TLog;
import i3k.DBForce.Manager;
import i3k.SBean.DBForceChapterBox;
import i3k.SBean.DBForceChapterGeneralStatus;
import i3k.SBean.DBForceChapterTreasure;
import i3k.SBean.DBForceTreasureApply;
import i3k.SBean.DBForceTreasureList;
import i3k.SBean.DBForceTreasureLog;
import i3k.gs.LoginManager.CommonRoleVisitor;
import i3k.gs.LoginManager.RoleBriefCachePair;
import ket.kdb.Table;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.util.Stream;

public class ForceManager
{
	
	public static class RewardMembers
	{
		public RewardMembers(byte chapter, List<Integer> rids)
		{
			this.chapter = chapter;
			this.rids = rids;
		}
		byte chapter;
		List<Integer> rids;
	}
	
	public static class Reward
	{
		public Reward(int roleId, byte chapter, int rank, List<SBean.DropEntryNew> attLst)
		{
			this.roleId = roleId;
			this.chapter = chapter;
			this.rank = rank;
			this.attLst = attLst;
		}
		int roleId;
		byte chapter;
		int rank;
		List<SBean.DropEntryNew> attLst;
	}
	
	public static class ApplyCache
	{
		public ApplyCache() {
			this.rids = new TreeSet<Integer>();
		}
		
		Set<Integer> rids;
		boolean exist;
	}
	
	public static class ForceLevel
	{
		public ForceLevel() { }
		public ForceLevel(int id, byte lvl) { this.id = id; this.lvl = lvl; }
		int id;
		byte lvl;
	}
	
	public static class BattleAttackRes
	{
		public int scoreBase;
		public int scoreRand;
		public List<SBean.PVPGeneral> fighters;
		public int scoreAdd;
	}
	
	public static class ActiveForce
	{
		static final int MAX_COUNT = 100;
		static final int DEL_COUNT = 50;
		
		public ActiveForce(byte lvl)
		{
			this.lvl = lvl;
		}
		
		public void put(ForceLevel e, Map<Integer, ActiveForce> idmap)
		{
			if( forces.size() >= MAX_COUNT )
			{
				ForceLevel re = forces.removeFirst();
				idmap.remove(re.id);
			}
			forces.add(e);
			idmap.put(e.id, this);
		}
		
		public void remove(ForceLevel e)
		{
			Iterator<ForceLevel> iter = forces.iterator();
			while( iter.hasNext() )
			{
				ForceLevel r = iter.next();
				if( r.id == e.id )
				{
					iter.remove();
					break;
				}
			}
		}
		public List<ForceLevel> get(int maxGetCount, Map<Integer, ActiveForce> idmap, Set<Integer> excludes)
		{
			List<ForceLevel> lst = new ArrayList<ForceLevel>();
			Iterator<ForceLevel> iter = forces.iterator();
			while( iter.hasNext() )
			{
				ForceLevel e = iter.next();
				if( ! excludes.contains(e.id) )
				{
					lst.add(e);
					iter.remove();
					if( lst.size() >= maxGetCount )
						break;
				}
			}
			if( forces.size() < DEL_COUNT )
				forces.addAll(lst);
			else
			{
				for(ForceLevel e : lst)
					idmap.remove(e.id);
			}
			return lst;
		}
		
		byte lvl;
		LinkedList<ForceLevel> forces = new LinkedList<ForceLevel>();
	}
	
	public static class ActiveForces
	{
		public static final int MAX_GET_COUNT = 6;
		private ActiveForce getActiveForce(byte lvl)
		{
			ActiveForce ar = array[lvl-1];
			if( ar == null )
			{
				ar = new ActiveForce(lvl);
				array[lvl-1] = ar;
			}
			return ar;
		}
		public synchronized void put(ForceLevel e)
		{
			ActiveForce arOld = idmap.get(e.id);
			if( arOld == null )
			{
				ActiveForce ar = getActiveForce(e.lvl);
				ar.put(e, idmap);
			}
			else if( arOld.lvl == e.lvl )
			{
			}
			else
			{
				arOld.remove(e);				
				ActiveForce ar = getActiveForce(e.lvl);
				ar.put(e, idmap);
			}
		}
		public synchronized void get(List<ForceLevel> lst, byte lvl, byte lvlMin, byte lvlMax, Set<Integer> excludes)
		{
			ActiveForce ar = getActiveForce(lvl);
			lst.addAll(ar.get(MAX_GET_COUNT - lst.size(), idmap, excludes));
			if( lst.size() >= MAX_GET_COUNT )
				return;
			byte ll = (byte)(lvl-1);
			byte lh = (byte)(lvl+1);
			while( ll >= lvlMin || lh <= lvlMax )
			{
				if( ll >= lvlMin )
				{
					ar = getActiveForce(ll);
					lst.addAll(ar.get(MAX_GET_COUNT - lst.size(), idmap, excludes));
					if( lst.size() >= MAX_GET_COUNT )
						return;
					--ll;
				}
				if( lh <= lvlMax )
				{
					ar = getActiveForce(lh);
					lst.addAll(ar.get(MAX_GET_COUNT - lst.size(), idmap, excludes));
					if( lst.size() >= MAX_GET_COUNT )
						return;
					++lh;
				}
			}
		}

		Map<Integer, ActiveForce> idmap = new TreeMap<Integer, ActiveForce>();
		ActiveForce[] array = new ActiveForce[GameData.MAX_FORCE_LEVEL];
	}
	
	public static class ForceBrief
	{
		public int fid;
		public String fname;
		public short iconID;
		public byte lvl;
		public byte members;
		public String msg;
		public int headID;
		public String headName;
	}
	
	public static class ForceInfo extends ForceBrief
	{
		public int point;
		public int contrib;
		public String qq;
		public byte joinMode;
		public short joinLvlReq;
		public byte sendMailCnt;
		public int activity;
		public int cupCount;
	}
	
	public static class BattleInfo
	{
		public static class Door
		{
			public int id;
			public int winner;
			public String name;
		}
		
		public int forceID;
		public int startTime;
		public String forceName;
		public List<Door> doors;
	}
	
	public static class MemberInfo extends DBForce.Member
	{
		public MemberInfo(DBForce.Member a, LoginManager.RoleBriefCache c)
		{
			super(a.id, a.joinTime, a.point, a.contrib);
			cache = c;
		}
		public LoginManager.RoleBriefCache cache;
	}
	
	public static class ApplyInfo extends DBForce.Apply
	{
		public ApplyInfo(DBForce.Apply a, LoginManager.RoleBriefCache c)
		{
			super(a.id, a.applyTime);
			cache = c;
		}
		public LoginManager.RoleBriefCache cache;
	}
	
	public class CreateForceTrans implements Transaction
	{
		public CreateForceTrans(final Role role, String fname, short iconID)
		{
			this.role = role;
			this.fname = fname;
			this.iconID = iconID;
		}

		@Override
		public boolean doTransaction()
		{			
			if( rolename.get(fname) != null )
			{
				fid = -1;
				return true;
			}
			int now = gs.getTime();
			Integer maxid = maxids.get("forceid");
			if( maxid == null )
				fid = 1;
			else
				fid = maxid + 1;
			DBForce dbforce = new DBForce();
			dbforce.id = fid;
			dbforce.dayCommon = -1;
			dbforce.iconID = iconID;
			dbforce.lvl = 1;
			dbforce.msg = "";
			dbforce.qq = "";
			dbforce.name = fname;
			dbforce.headName = role.name;
			dbforce.point = 0;
			dbforce.contrib = 0;
			dbforce.upgradeTime = now;
			dbforce.lastModifyTime = now;
			dbforce.members = new ArrayList<DBForce.Member>();
			dbforce.queue = new ArrayList<DBForce.Apply>();
			dbforce.logs = new ArrayList<DBForce.History>();
			dbforce.managers = new ArrayList<DBForce.Manager>();
			dbforce.activities = new ArrayList<DBForce.Activity>();
			DBForce.Member member = new DBForce.Member();
			member.id = role.id;
			member.joinTime = gs.getTime();
			member.point = 0;
			member.contrib = 0;
			dbforce.members.add(member);
			DBForce.Manager manager = new DBForce.Manager();
			manager.id = role.id;
			manager.pos = DBForce.POS_KING;
			dbforce.managers.add(manager);
			
			dbforce.beasts = new ArrayList<SBean.DBForceBeast>();
			dbforce.defaultBeast = -1;
			dbforce.dungeon = new ArrayList<SBean.DBForceDungeon>();
			dbforce.merc = new ArrayList<SBean.DBMerc>();
			maxids.put("forceid", fid);
			force.put(fid, dbforce);
			rolename.put(fname, -fid);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				role.onCreateForceRes(fid, iconID, fname);
			}
			else
			{
				role.onCreateForceRes(0, iconID, fname);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		@AutoInit
		public Table<String, Integer> maxids;
		@AutoInit
		public Table<String, Integer> rolename;
		
		public final Role role;
		public final short iconID;
		public final String fname;
		public int fid;
	}
	
	public class ForceInfoTrans implements Transaction
	{
		public static final byte TYPE_STAMP = 2;
		public static final byte TYPE_MEMBER = 3;
		public static final byte TYPE_APPLY = 4;
		public static final byte TYPE_LIST = 5;
		public static final byte TYPE_LOGS = 6;
		public static final byte TYPE_GETPOS = 7;
		public static final byte TYPE_EXPATRIATE = 8;
		
		public ForceInfoTrans(byte type, int fid, int sid, int roleID)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
		}
		
		public ForceInfoTrans(byte type, int fid, int sid, int roleID, int iArg)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.iArg = iArg;
		}
		
		public ForceInfoTrans(byte type, int fid, int sid, int roleID, String sArg1, String sArg2)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.sArg1 = sArg1;
			this.sArg2 = sArg2;
		}

		@Override
		public boolean doTransaction()
		{			
			DBForce dbforce = null;
			if( type != TYPE_LIST )
			{
				dbforce = force.get(fid);
				if( dbforce == null )
					return false;
			}
			{
				switch( type )
				{
				case TYPE_STAMP:
					{
						stamp = dbforce.lastModifyTime;
					}
					break;
				case TYPE_LIST:
					{
						lst = new ArrayList<ForceBrief>();
						List<Integer> ids = getRandomForceID((byte)iArg);
						for(int fid : ids)
						{
							DBForce dbforceE = force.get(fid);
							if( dbforceE != null )
							{
								ForceBrief brief = new ForceBrief();
								brief.fid = fid;
								brief.fname = dbforceE.name;
								brief.iconID = dbforceE.iconID;
								brief.lvl = dbforceE.lvl;
								brief.members = (byte)dbforceE.members.size();
								brief.msg = dbforceE.msg;
								DBForce.Manager m1 = dbforceE.managers.get(0);
								brief.headID = m1.id;
								brief.headName = dbforceE.headName;
								lst.add(brief);
							}
							//else
							//	break;
						}
					}
					break;
				case TYPE_MEMBER:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						members = dbforce.members;
						managers = dbforce.managers;
					}
					break;
				case TYPE_GETPOS:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						byte pos = 0;
						DBForce.Manager m = dbforce.getManager(roleID);
						if( m != null )
							pos = m.pos;
						iArg = pos;
					}
					break;
					
				case TYPE_EXPATRIATE:
				    {
				    	if(dbforce.isKing(roleID) )
							return false;
				    }
					break;
				case TYPE_APPLY:
					{
						if( ! dbforce.canAccept(roleID) )
							return false;
						applies = dbforce.queue;
					}
					break;
				case TYPE_LOGS:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						logs = dbforce.copyLogs(iArg);
					}
					break;
				default:
					break;
				}
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				switch( type )
				{
				case TYPE_STAMP:
					gs.getRPCManager().notifyForceStamp(sid, fid, stamp);
					break;
				case TYPE_GETPOS:
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceGetPosRes(fid, (byte)iArg));
					break;
				case TYPE_MEMBER:
					{
						final List<Integer> rids = new ArrayList<Integer>();
						for(DBForce.Member e : members)
							rids.add(e.id);
						gs.getLoginManager().getDataBrief(roleID,rids, false, new LoginManager.GetDataBriefCallback() {
						
							@Override
							public void onCallback(List<RoleBriefCachePair> lst)
							{
								List<MemberInfo> infos = new ArrayList<MemberInfo>();
								Map<Integer, LoginManager.RoleBriefCache> map = new TreeMap<Integer, LoginManager.RoleBriefCache>();
								for(RoleBriefCachePair p : lst)
									map.put(p.id, p.cache);
								for(DBForce.Member e : members)
								{
									LoginManager.RoleBriefCache c = map.get(e.id);
									if( c != null )
									{
										infos.add(new MemberInfo(e, c));
									}
								}
								Collections.sort(infos,new Comparator<MemberInfo>(){
									public int compare(MemberInfo o1, MemberInfo o2) {
										return o2.cache.activity-o1.cache.activity;
									}
								});
								gs.getRPCManager().notifyForceMembers(sid, fid, infos, managers);
							}
						});
					}
					break;
				case TYPE_APPLY:
					{
						final List<Integer> rids = new ArrayList<Integer>();
						for(DBForce.Apply e : applies)
							rids.add(e.id);
						gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
							
							@Override
							public void onCallback(List<RoleBriefCachePair> lst)
							{
								List<ApplyInfo> infos = new ArrayList<ApplyInfo>();
								Map<Integer, LoginManager.RoleBriefCache> map = new TreeMap<Integer, LoginManager.RoleBriefCache>();
								for(RoleBriefCachePair p : lst)
									map.put(p.id, p.cache);
								for(DBForce.Apply e : applies)
								{
									LoginManager.RoleBriefCache c = map.get(e.id);
									if( c != null )
									{
										infos.add(new ApplyInfo(e, c));
									}
								}
								gs.getRPCManager().notifyForceApply(sid, fid, infos);
							}
						});
					}
					break;
				case TYPE_LIST:
					gs.getRPCManager().notifyForceList(sid, lst);
					break;
				case TYPE_LOGS:
					gs.getRPCManager().notifyForceLogs(sid, fid, logs);
					break;
				case TYPE_EXPATRIATE:
					 String tarGameServer = sArg1;
                     String openId = sArg2;
                     gs.getRPCManager().sendConfirmTargetRequest(sid , tarGameServer, openId);
					break;
				default:
					break;
				}
			}
			else
			{
				switch( type )
				{
				case TYPE_STAMP:
					gs.getRPCManager().notifyForceStamp(sid, fid, 0);
					break;
				case TYPE_MEMBER:
					gs.getRPCManager().notifyForceMembers(sid, fid, null, null);
					break;
				case TYPE_GETPOS:
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceGetPosRes(fid, (byte)-1));
					break;
				case TYPE_APPLY:
					gs.getRPCManager().notifyForceApply(sid, fid, null);
					break;
				case TYPE_LOGS:
					gs.getRPCManager().notifyForceLogs(sid, fid, null);
					break;
				case TYPE_LIST:
					gs.getRPCManager().notifyForceList(sid, lst);
					break;
				case TYPE_EXPATRIATE:
					List<String> res = new ArrayList<String>();
                    res.add("4");
                    gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
					break;
				default:
					break;
				}
			}
		}
		
		@AutoInit
		public TableReadonly<Integer, DBForce> force;
		
		public final byte type;
		public final int fid;
		public final int sid;
		public final int roleID;
		public int iArg;
		public String sArg1;
		public String sArg2;
		public int stamp;
		public List<DBForce.Member> members;
		public List<DBForce.Apply> applies;
		public List<DBForce.Manager> managers;
		public List<DBForce.Activity> activities;
		public List<ForceBrief> lst;
		public List<DBForce.History> logs;
	}
	
	public class ForceOPTrans implements Transaction
	{
		public static final byte TYPE_ANNOUNCE = 1;
		public static final byte TYPE_ACCEPT = 2;
		public static final byte TYPE_ACCEPT_DB = 3;
		public static final byte TYPE_INFO = 4;
		public static final byte TYPE_QQ = 8;
		public static final byte TYPE_REFUSE = 9;
		public static final byte TYPE_SETPOS = 11;
		public static final byte TYPE_QUIT = 13;
		public static final byte TYPE_KICK = 14;
		public static final byte TYPE_CHUANWEI = 15;
		public static final byte TYPE_SETICON = 16;
		public static final byte TYPE_ACTIVITYREWARD = 17;
		public static final byte TYPE_GETACTIVITY = 18;
		public static final byte TYPE_BATTLE = 19;
		public static final byte TYPE_OPEN_BATTLE = 20;
		public static final byte TYPE_JOIN_BATTLE = 21;
		public static final byte TYPE_QUIT_BATTLE = 22;
		public static final byte TYPE_BATTLE_CHANGE_SEQ = 23;
		public static final byte TYPE_BATTLE_CONTRIBUTITON = 24;
		public static final byte TYPE_CONTRIBUTE_BATTLE = 25;
		public static final byte TYPE_DISMISS = 26;
		public static final byte TYPE_SETJOIN = 27;
		public static final byte TYPE_UPGRADE = 28;
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, String sArg, int iArg, Role roleOnline)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.sArg = sArg;
			this.iArg = iArg;
			this.roleOnline = roleOnline;
		}
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, Object varArg, int iArg, Role roleOnline)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.varArg = varArg;
			this.iArg = iArg;
			this.roleOnline = roleOnline;
		}
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, String roleName, String sArg, int iArg, Role roleOnline)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.roleName = roleName;
			this.sArg = sArg;
			this.iArg = iArg;
			this.roleOnline = roleOnline;
		}
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, String sArg, int iArg, int iArg2, Role roleOnline)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.sArg = sArg;
			this.iArg = iArg;
			this.iArg2 = iArg2;
			this.roleOnline = roleOnline;
		}
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, int[] ilArg, Role roleOnline)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.ilArg = ilArg;
			this.roleOnline = roleOnline;
		}
		
		public ForceOPTrans(byte type, int fid, int sid, int roleID, String sArg, int iArg, Role roleOnline, List<Short> gids, List<Short> pids)
		{
			this.type = type;
			this.fid = fid;
			this.sid = sid;
			this.roleID = roleID;
			this.sArg = sArg;
			this.iArg = iArg;
			this.roleOnline = roleOnline;
			this.gids = gids;
			this.pids = pids;
		}

		@Override
		public boolean doTransaction()
		{			
			DBForce dbforce = force.get(fid);
			if( dbforce == null )
			{
				if( type == TYPE_INFO )
				{
					if( roleOnline != null )
						roleOnline.clearForce(fid, false);
				}
				return false;
			}
			else {
				boolean mod = false;
				int ret = retrieveActivity(fid);
				if (ret > 0) {
					dbforce.activity += ret;
					mod = true;
				}
				
				ret = retrieveCup(fid);
				if (ret > 0) {
					dbforce.cupCount += ret;
					mod = true;
				}
				
				if (dbforce.beasts.size() == 0)
					gs.getWarManager().beastGet(fid);
				
				if (dbforce.dungeon.size() == 0) {
					SBean.DBForceDungeon dungeon = createDefaultDungeon(fid);
					dbforce.dungeon.add(dungeon);
					mod = true;
				}
				else {
					SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
					SBean.DungeonCFGS cfg = gs.gameData.getDungeonCFG();
					if (dungeon != null && dungeon.chapters.size() < cfg.chapters.size()) {
						for (int i = dungeon.chapters.size(); i < cfg.chapters.size(); i ++)
							dungeon.chapters.add(new SBean.DBForceChapter((byte)-1, new ArrayList<DBForceChapterGeneralStatus>(), new ArrayList<DBForceChapterBox>(), 
				                    new ArrayList<Integer>(), (byte)0, 0, 0));
						mod = true;
					}
				}
				
				if (dbforce.merc.size() == 0) {
					SBean.DBMerc merc = new SBean.DBMerc(new ArrayList<SBean.DBRoleMerc>(), 0, 0);
					dbforce.merc.add(merc);
					mod = true;
				}
				
				if (roleOnline != null) {
					for (DBForce.Manager manager : dbforce.managers) {
						if (manager.id == roleOnline.id) {
							if (manager.pos == DBForce.POS_KING) {
								dbforce.headName = roleOnline.name;
								mod = true;
							}
							break;
						}
					}
				}
				
				if (mod)
					force.put(fid, dbforce);
				Integer maxid = maxids.get("forceid");
				if( maxid == null )
					maxid = 0;
				addForceCache(fid, maxid);
			}
			{
				switch( type )
				{
				case TYPE_INFO:
					{
						if( ! dbforce.isMember(roleID) )
						{
							if( roleOnline != null )
								roleOnline.clearForce(fid, true);
							return false;
						}
						updateForceLevel(fid, dbforce.lvl);
						int now = gs.getTime();
						info = new ForceInfo();
						info.fid = fid;
						info.fname = dbforce.name;
						info.iconID = dbforce.iconID;
						info.lvl = dbforce.lvl;
						info.msg = dbforce.msg;
						info.point = dbforce.point;
						info.contrib = dbforce.contrib;
						info.qq = dbforce.qq;
						info.joinMode = dbforce.joinMode;
						SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
						if (dbforce.joinLvlReq < cfg.lvlCreate)
							dbforce.joinLvlReq = cfg.lvlCreate;
						info.joinLvlReq = dbforce.joinLvlReq;
						info.sendMailCnt = dbforce.sendMailCnt;
						info.members = (byte)dbforce.members.size();
						info.cupCount = dbforce.cupCount;
						//SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
						if (dbforce.dayCommon != gs.getDayCommon())
						{
							int actTotal = 0;
							int actMem = 0;
							for (DBForce.Activity act : dbforce.activities)
							{
								actTotal += act.activity;
								if (act.activity > 0)
									actMem++;
							}
							iArg = actTotal;
							iArg2 = actMem;
						}
						else
						{
							iArg = -1;
							iArg2 = 0;
						}
						boolean bNewDay = dbforce.dayRefresh(gs.getDayCommon());
						if( bNewDay )
						{
							for (SBean.DBForceBeast b : dbforce.beasts)
								b.resetPropTimes = 0;
							
							final int timeout = 7 * 86400;
							DBForce.Manager king = dbforce.managers.get(0);
							DBRole dbking = role.get(king.id);
							if( dbking != null )
							{
								if( dbking.lastLoginTime + timeout < now )
								{
									int newKingID = -1;
									String newKingName = null;
									// do change king here
									Manager queen = null;
									for(Manager e : dbforce.managers)
									{
										if( e.pos == DBForce.POS_QUEEN )
										{
											queen = e;
											break;
										}
									}
									if( queen != null )
									{
										DBRole dbqueen = role.get(queen.id);
										if( dbqueen != null )
										{
											if( dbqueen.lastLoginTime + timeout >= now )
											{
												newKingID = queen.id;
												newKingName = dbqueen.name;
												queen.id = king.id;
												king.id = newKingID;
											}
										}
									}
									if( newKingID == -1 )
									{
										DBForce.Manager m = dbforce.getManager(roleID);
										if( m != null )
											m.id = king.id;
										newKingID = roleID;
										newKingName = roleOnline.name;
										king.id = newKingID;
									}
									if( newKingID > 0 )
									{
										dbforce.headName = newKingName;
									}
								}
							}
							
							SBean.DBForceTreasureDeploy trd = dungeonTreasureDeploy.get(fid);
							if (trd != null)
								trd.deployTimes = 0;
						}
						DBForce.Manager m1 = dbforce.managers.get(0);
						info.headID = m1.id;
						info.headName = dbforce.headName; 
						info.activity = dbforce.activity;
						boolean bSave = bNewDay;
						if( bSave )
						{
							dbforce.lastModifyTime = now;
							force.put(fid, dbforce);
						}
					}
					break;
				case TYPE_DISMISS:
					{
						if( fid != roleOnline.getForceID() )
							return false;
						DBForce.Member m = dbforce.getMember(roleID);
						if( m == null )
						{
							if( roleOnline != null )
								roleOnline.clearForce();
							return true;
						}
						if( !dbforce.isKing(roleID) )
							return false;
						if( dbforce.members.size() != 1 )
							return false;
						SBean.ForceCFGS cfg = gs.gameData.getForceCFG();
						SBean.DropEntryNew drop = null;
						for (SBean.ForceDismissRewardCFGS r : cfg.dismissRewards)
							if (r.lvl <= dbforce.lvl)
								drop = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, r.itemId, r.count);
						if (drop != null) {
							iArg = drop.id;
							iArg2 = drop.arg;
						}
						force.del(fid);
						roleOnline.clearForce();
						int now = gs.getTime();
						roleOnline.forceInfo.joinTime = now; // 解散之后计算重新加入时间
						newStamp = now;
					}
					break;
				case TYPE_QUIT:
					{
						if( fid != roleOnline.getForceID() )
							return false;
						DBForce.Member m = dbforce.getMember(roleID);
						if( m == null )
						{
							if( roleOnline != null )
								roleOnline.clearForce();
							return true;
						}
						if( dbforce.isKing(roleID) )
							return false;
						dbforce.members.remove(m);
						DBForce.Manager ma = dbforce.getManager(roleID);
						if( ma != null )
							dbforce.managers.remove(ma);
						int now = gs.getTime();
						dbforce.addLog(new DBForce.History().logQuit(now, roleOnline.name));
						dbforce.lastModifyTime = now;
						
						// Clear merc
						if (!dbforce.merc.isEmpty()) {
							SBean.DBMerc merc = dbforce.merc.get(0);
							List<SBean.DBRoleMerc> dels = new ArrayList<SBean.DBRoleMerc>();
							for (SBean.DBRoleMerc rm : merc.roleMercs)
								if (rm.rid == roleID)
									dels.add(rm);
							
							for (SBean.DBRoleMerc rm : dels)
								merc.roleMercs.remove(rm);
						}
						
						force.put(fid, dbforce);
						roleOnline.clearForce();
						roleOnline.forceInfo.joinTime = now; // 踢出之后计算重新加入时间
						newStamp = now;
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEQUIT, fid, Role.formatForceQuitRecord(fid, roleID),  
                        //                                 -1, -1, -1, -1, 
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_ANNOUNCE:
					{
						if( ! dbforce.canModAnnounce(roleID) )
							return false;
						int now = gs.getTime();
						dbforce.msg = sArg;
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEANNOUNCE, fid, "", 
		                //                                 -1, -1, -1, -1, 
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_QQ:
					{
						if( ! dbforce.canModQQ(roleID) )
							return false;
						int now = gs.getTime();
						dbforce.qq = sArg;
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEQQ, fid, Role.formatForceQQRecord(fid, dbforce.qq), 
		                //                                  -1, -1, -1, -1,  
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_ACCEPT:
				case TYPE_ACCEPT_DB:
					{
						if( ! dbforce.canAccept(roleID) )
							return false;
						DBRole dbrole = null;
						if( type == TYPE_ACCEPT_DB )
						{
							dbrole = role.get(iArg);
							if( dbrole == null )
								return false;
						}
						int now = gs.getTime();
						DBForce.Apply ma = null;
						for(DBForce.Apply m : dbforce.queue)
						{
							if( m.id == iArg )
							{
								ma = m;
								break;
							}
						}
						if( ma == null )
							return true;
						boolean bFound = false;
						for(DBForce.Member m : dbforce.members)
						{
							if( m.id == iArg )
							{
								bFound = true;
								break;
							}
						}
						if( bFound )
						{
							dbforce.queue.remove(ma);
						}
						else
						{
							if( dbforce.members.size() >= getMaxMemberCount(dbforce.lvl) )
								return true;
							dbforce.queue.remove(ma);
							if( dbrole != null )
							{
								if( dbrole.forceID > 0 )
									ret = (byte)1;
								else if( dbrole.forceJoinTime + 86400 > now )
									ret = (byte)2;
								else
								{
									ret = 0;
									dbrole.forceID = fid;
									dbrole.forceName = dbforce.name;
									dbrole.forceJoinTime = now;
									dbrole.forceIconId = dbforce.iconID;
									DBForce.Member newm = new DBForce.Member(dbrole.id, now, 0, 0);
									dbforce.members.add(newm);
									dbforce.addLog(new DBForce.History().logJoin(now, dbrole.name, roleName));
									role.put(dbrole.id, dbrole);
									sArg = dbrole.name;
								}
							}
							else
							{
								if( roleOnline == null )
									ret = (byte)1;
								else if( roleOnline.forceInfo.joinTime + 86400 > now )
									ret = (byte)2;
								else
								{
									ret = 0;
									fname = dbforce.name;
									ficonID = dbforce.iconID;
									DBForce.Member newm = new DBForce.Member(roleOnline.id, now, 0, 0);
									dbforce.members.add(newm);
									dbforce.addLog(new DBForce.History().logJoin(now, roleOnline.name, roleName));
									sArg = roleOnline.name;
								}
							}
						}
						refreshApplyCache(dbforce);
						newStamp = now;
						dbforce.lastModifyTime = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEACCEPT, fid, Role.formatForceAcceptRecord(fid, iArg), 
		                //                                  -1, -1, -1, -1,  
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_REFUSE:
					{
						if( ! dbforce.canAccept(roleID) )
							return false;
						int now = gs.getTime();
						DBForce.Apply ma = null;
						for(DBForce.Apply m : dbforce.queue)
						{
							if( m.id == iArg )
							{
								ma = m;
								break;
							}
						}
						if( ma == null )
							return true;
						dbforce.queue.remove(ma);
						refreshApplyCache(dbforce);
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEREFUSE, fid, Role.formatForceRefuseRecord(fid, iArg), 
						//		                          -1, -1, -1, -1,  
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_UPGRADE:
					{
						if( ! dbforce.canUpgrade(roleID) )
							return false;
						byte lvlNow = dbforce.lvl;
						if( lvlNow >= GameData.MAX_FORCE_LEVEL )
							return false;
						SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
						int now = gs.getTime();
						
						int cupNeed = cfg.levels.get(lvlNow).cupNeed;
						if( dbforce.cupCount < cupNeed || cupNeed <= 0 )
							return false;
						dbforce.cupCount -= cupNeed;
						
						dbforce.lvl++;
//						dbforce.addLog(new DBForce.History().logUpgrade(now, dbforce.lvl, roleOnline.name));
						dbforce.lastModifyTime = now;
						dbforce.upgradeTime = now;
						newStamp = now;
						force.put(fid, dbforce);
					}
					break;
				case TYPE_CHUANWEI:
					{
						if( ! dbforce.isKing(roleID) )
							return false;
						Manager ma = null;
						int targetID = iArg;
						for(Manager e : dbforce.managers)
						{
							if( e.pos == 2 && targetID == e.id )
							{
								ma = e;
								break;
							}
						}
						if( ma == null )
							return false;
						DBRole dbrole = role.get(ma.id);
						if( dbrole == null )
							return false;
						if( ! dbforce.chuanwei(dbrole.name, targetID) )
							return false;
						int now = gs.getTime();
//						dbforce.addLog(new DBForce.History().logChuanwei(now, dbrole.name, roleOnline.name));
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCECHUANWEI, fid, Role.formatForceChuanweiRecord(fid, dbrole.id), 
						//		                          -1, -1, -1, -1,  
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_SETPOS:
					{
						int targetID = iArg;
						byte newpos = (byte)iArg2;
						SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
						SBean.ForceLevelCFGS lcfg = cfg.levels.get(dbforce.lvl-1);
						if( newpos < 0 || newpos > lcfg.posCount.size() )
							return false;
						if( newpos == 1 )
							return false; // TODO
						if( ! dbforce.isMember(targetID) )
							return false;
						if( roleID == targetID || dbforce.isKing(targetID) )
							return false; // TODO
						if( ! dbforce.canSetPos(roleID) )
							return false;
						DBForce.Manager m = dbforce.getManager(targetID);
						byte oldPos = 0;
						if( m != null )
							oldPos = m.pos;
						if( oldPos == newpos )
							return false;
						
						if( newpos == 0 ) // 免职
						{
							dbforce.managers.remove(m);
						}
						else
						{
							byte maxCnt = lcfg.posCount.get(newpos-1);
							if( dbforce.getManagerCount(newpos) >= maxCnt )
								return false;
							if( m != null )
								dbforce.managers.remove(m);
							dbforce.managers.add(new DBForce.Manager(targetID, newpos));
						}
						// TODO
						int now = gs.getTime();
						DBRole dbrole = role.get(targetID);
						if( dbrole != null )
//							dbforce.addLog(new DBForce.History().logSetpos(now, dbrole.name, roleOnline.name, oldPos, newpos));
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCESETPOS, fid, Role.formatForceSetPosRecord(fid, targetID, newpos), 
						//		                          -1, -1, -1, -1, 
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_KICK:
					{
						int targetID = iArg;
						if( roleID == targetID || dbforce.isKing(targetID) )
							return false; // TODO
						if( ! dbforce.canKick(roleID) )
							return false;
						DBForce.Member m = dbforce.getMember(targetID);
						if( m == null )
							return false;
						fname = dbforce.name;
						roleName = roleOnline.name;
						DBRole dbrole = role.get(targetID);
						dbforce.members.remove(m);
						DBForce.Manager ma = dbforce.getManager(targetID);
						if( ma != null )
							dbforce.managers.remove(ma);
						int now = gs.getTime();
						if( dbrole != null )
							dbforce.addLog(new DBForce.History().logKick(now, dbrole.name, roleOnline.name,dbforce.getPos(roleID)));
						// TODO
						dbforce.lastModifyTime = now;
						dbrole.forceJoinTime = now; // 踢出之后计算重新加入时间
						Role role = gs.getLoginManager().getRole(targetID);
						if (role != null) {
							role.clearForce();
							role.forceInfo.joinTime = now; // 踢出之后计算重新加入时间
						}
						newStamp = now;
						
						// Clear merc
						if (!dbforce.merc.isEmpty()) {
							SBean.DBMerc merc = dbforce.merc.get(0);
							List<SBean.DBRoleMerc> dels = new ArrayList<SBean.DBRoleMerc>();
							for (SBean.DBRoleMerc rm : merc.roleMercs)
								if (rm.rid == targetID)
									dels.add(rm);
							
							for (SBean.DBRoleMerc rm : dels)
								merc.roleMercs.remove(rm);
						}
						
						force.put(fid, dbforce);
						//gs.getGameStatLogger().roleAction(roleID, GameStateLogger.Actions.FORCEKICK, fid, Role.formatForceKickRecord(fid, targetID), 
						//		                          -1, -1, -1, -1,  
                        //                                  0, 0, 0, 0, 0, "", "", "");
					}
					break;
				case TYPE_SETICON:
					{
						if (!dbforce.canModIcon(roleID))
							return false;
						int icon = iArg;
						if (icon <= 0)
							return false;
						int now = gs.getTime();
						dbforce.iconID = (short)icon;
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
					}
					break;
				case TYPE_SETJOIN:
					{
						if (!dbforce.canModJoin(roleID))
							return false;
						byte mode = (byte)iArg;
						short lvlReq = (short)iArg2;
						if (mode != DBForce.JOIN_MODE_VERIFY && mode != DBForce.JOIN_MODE_ANYONE && mode != DBForce.JOIN_MODE_NOONE)
							return false;
						SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
						if (lvlReq < cfg.lvlCreate)
							return false;
						int now = gs.getTime();
						dbforce.joinMode = mode;
						dbforce.joinLvlReq = lvlReq;
						dbforce.lastModifyTime = now;
						newStamp = now;
						force.put(fid, dbforce);
					}
					break;
				case TYPE_GETACTIVITY:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						members = dbforce.members;
						activities = dbforce.activities;
						forceActivity = dbforce.activity;
					}
					break;
				case TYPE_ACTIVITYREWARD:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						activities = dbforce.activities;
					}
					break;
				case TYPE_BATTLE:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
					}
					break;
				case TYPE_OPEN_BATTLE:
					{
						if( ! dbforce.canOpenBattle(roleID) )
							return false;
						sArg = dbforce.name;
						sArg2 = dbforce.headName;
						ilArg = new int[] {dbforce.iconID, dbforce.lvl, dbforce.getKing(), dbforce.members.size()};
					}
					break;
				case TYPE_JOIN_BATTLE:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						ilArg = new int[] {dbforce.members.size()};
						if (dbforce.beasts.size() > 0)
							beast = dbforce.beasts.get(dbforce.beasts.size()-1);
					}
					break;
				case TYPE_BATTLE_CHANGE_SEQ:
					{
						if( ! dbforce.canChangeBattleSeq(roleID) )
							return false;
						ilArg[3] = dbforce.members.size();
					}
					break;
				case TYPE_BATTLE_CONTRIBUTITON:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
					}
					break;
				case TYPE_CONTRIBUTE_BATTLE:
					{
						if( ! dbforce.isMember(roleID) )
							return false;
						ilArg[3] = dbforce.members.size();
					}
					break;
				default:
					break;
				}
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				switch( type )
				{
				case TYPE_INFO:
					{
						gs.getRPCManager().notifyForceInfo(sid, fid, info);
						if (iArg >= 0)
							gs.getTLogger().logForce(fid, info.fname, info.lvl, info.members, iArg, iArg2);
					}
					break;
				case TYPE_ANNOUNCE:
					{
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.SecTalkRecord secRecord = new TLogger.SecTalkRecord(TLog.SEC_TALK_ANNOUNCE, sArg);
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_SET_ANNOUNCEMENT);
						tlogEvent.addRecord(secRecord);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						gs.getRPCManager().notifyForceSetAnnounce(sid, fid, newStamp);
					}
					break;
				case TYPE_QQ:
					gs.getRPCManager().notifyForceSetQQ(sid, fid, newStamp);
					break;
				case TYPE_UPGRADE:
					gs.getRPCManager().notifyForceCmnRes(sid, fid, newStamp);
					break;
				case TYPE_CHUANWEI:
				case TYPE_SETPOS:
					{
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, type == TYPE_CHUANWEI ? TLog.FORCE_PASS_POSITION : TLog.FORCE_SET_POSITION);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						gs.getRPCManager().notifyForceCmnRes(sid, fid, newStamp);
					}
					break;
				case TYPE_DISMISS:
					{
						if( roleOnline != null )
						{
							if (iArg > 0 && iArg2 > 0)
								roleOnline.addDropNew(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)iArg, iArg2));
							roleOnline.doSave();
						}
						gs.getRPCManager().notifyForceDismissRes(sid, fid, newStamp, (short)iArg, iArg2);
						gs.getWarManager().handleForceDisMiss(fid);
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_DISMISS);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
					}
					break;
				case TYPE_QUIT:
					{
						if( roleOnline != null )
						{
							roleOnline.doSave();
						}
						gs.getRPCManager().notifyForceCmnRes(sid, fid, newStamp);
						gs.getWarManager().handleForceMemberQuit(fid, roleOnline.id);
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_QUIT);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						
						dungeonRemoveApply(fid, roleOnline.id);
					}
					break;
				case TYPE_KICK:
					{
						//String m = DBMail.encodeSysMessageForceKick(fname, roleName);
						List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
						gs.getLoginManager().sysSendMessage(0, iArg, DBMail.Message.SUB_TYPE_FORCE_KICKED, "", roleName, 0, true, attLst);
						gs.getLoginManager().modifyData(0, new SBean.DataModifyReq(0, iArg,
								SBean.DataModifyReq.eSet, SBean.DataModifyReq.eForceID, (byte)0, (short)0, fid), null, null);
						gs.getRPCManager().notifyForceCmnRes(sid, fid, newStamp); // TODO!!!
						gs.getWarManager().handleForceMemberQuit(fid, iArg);
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_KICK);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						
						dungeonRemoveApply(fid, iArg);
					}
					break;
				case TYPE_ACCEPT:
				case TYPE_ACCEPT_DB:
					{
						gs.getRPCManager().notifyForceAccept(sid, (byte)1, newStamp, (byte)ret);
						if( sArg != null )
						{
							LoginManager.WorldChatCache.Msg m = new LoginManager.WorldChatCache.Msg();
							m.roleid = roleID;
							m.lvlVIP = 0;
							{
								Role roleOP = gs.getLoginManager().getRole(roleID);
								if( roleOP != null )
								{
									m.lvl = roleOP.lvl;
									m.headIconID = roleOP.headIconID;
								}
							}
							m.rolename = roleName;
							m.time = gs.getTime();
							m.msg = LoginManager.WorldChatCache.Msg.encodeForceJoin(sArg);
							gs.getLoginManager().addForceChatMsg(fid, m);	
						}
						if( roleOnline != null )
						{
							if( newStamp != 0 && ret == 0 )
							{
								roleOnline.setAcceptForceRes(new Role.ForceInfo(fid, 0, 0, fname, ficonID, 0, (short)0));
								roleOnline.doSave();
							}
							else
								roleOnline.setAcceptForceRes(null);
						}
					}
					break;
				case TYPE_REFUSE:
					gs.getRPCManager().notifyForceAccept(sid, (byte)0, newStamp, (byte)0);
					break;
				case TYPE_SETICON:
					{
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_SET_ICON);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						gs.getRPCManager().notifyForceSetIcon(sid, newStamp, iArg);
					}
					break;
				case TYPE_SETJOIN:
					{
						TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
						TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_SET_JOIN);
						tlogEvent.addRecord(record);
						gs.getTLogger().logEvent(roleOnline, tlogEvent);
						gs.getRPCManager().notifyForceSetJoin(sid, fid, newStamp, (byte)iArg, (short)iArg2);
					}
					break;
				case TYPE_GETACTIVITY:
					{
						final List<Integer> rids = new ArrayList<Integer>();
						for(DBForce.Member e : members)
							rids.add(e.id);
						gs.getLoginManager().getDataBrief(rids, true, new LoginManager.GetDataBriefCallback() {
						
							@Override
							public void onCallback(List<RoleBriefCachePair> lst)
							{
								Map<Integer, LoginManager.RoleBriefCache> map = new TreeMap<Integer, LoginManager.RoleBriefCache>();
								for(RoleBriefCachePair p : lst)
									map.put(p.id, p.cache);
								Map<Integer, Integer> mapActivities = new TreeMap<Integer, Integer>();
								int activity = 0;
								int selfActivity = 0;
								for(DBForce.Member e : members)
								{
									LoginManager.RoleBriefCache c = map.get(e.id);
									if( c != null )
									{
										activity += c.activity;
										if (e.id == roleID)
											selfActivity = c.activity;
										mapActivities.put(e.id, c.activity);
									}
								}
								for (DBForce.Activity a : activities) {
									if (mapActivities.get(a.id) == null) {
										activity += a.activity;
										mapActivities.put(a.id, a.activity);
									}
								}
								if (!mapActivities.isEmpty())
									gs.getDB().execute(new ModifyForceActivitiesTrans(fid, mapActivities));
								List<Byte> rewards = new ArrayList<Byte>();
								synchronized (roleOnline) {
									for (byte r : roleOnline.activityRewards) {
										rewards.add(r);
									}
								}
								gs.getRPCManager().notifyForceActivity(sid, fid, roleID, selfActivity, activity, forceActivity, rewards);
							}
						});
					}
					break;
				case TYPE_ACTIVITYREWARD:
					{
						int activity = 0;
						for (DBForce.Activity a : activities) 
							activity += a.activity;
						if (roleOnline != null)
							roleOnline.forceActivityReward(fid, iArg, activity);
					}
					break;
				case TYPE_BATTLE:
					{
						int curTime = gs.getTime();
						SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(curTime);
						if (cfg == null)
						{
							cfg = GameData.getInstance().getForceBattleNext(curTime);
							gs.getRPCManager().notifyForceBattleSync(sid, fid, cfg, null, null);
						}
						else
						{
							gs.getRPCManager().notifyForceBattleSync(sid, fid, cfg, gs.getWarManager().getForcebattlePlayerRoles(cfg.id, fid), gs.getWarManager().getForcebattlePlayerBeast(cfg.id, fid));
						}
					}
					break;
				case TYPE_OPEN_BATTLE:
					{
						boolean ret = gs.getWarManager().openForceWar(iArg, fid, sArg, (short)ilArg[0], (short)ilArg[1], ilArg[2], sArg2, ilArg[3]);
						if (ret)
						{
							TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
							TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WAR_OPEN);
							record.setArgs(iArg, ilArg[2]);
							tlogEvent.addRecord(record);
							gs.getTLogger().logEvent(roleOnline, tlogEvent);
							
							gs.getDB().execute(new ModifyForceLogTrans(fid, sArg2,null));
											
						}
						gs.getRPCManager().notifyForceBattleOpen(sid, fid, iArg, ret);
					}
					break;
				case TYPE_JOIN_BATTLE:
					{
						boolean ret = gs.getWarManager().joinForceWar(iArg, fid, roleOnline, gids, pids, beast);
						if (ret)
						{
							TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
							TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WAR_JOIN);
							record.setArgs(iArg, ilArg[0]);
							tlogEvent.addRecord(record);
							gs.getTLogger().logEvent(roleOnline, tlogEvent);
						}
						gs.getRPCManager().notifyForceBattlePlayerJoin(sid, fid, iArg, ret);
					}
					break;
				case TYPE_BATTLE_CHANGE_SEQ:
					{
						boolean ret = gs.getWarManager().changeRolePlayerSeq(ilArg[0], fid, ilArg[1]-1, ilArg[2]);
						if (ret)
						{
							TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
							TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WAR_CHANGE_SEQ);
							record.setArgs(iArg, ilArg[3]);
							tlogEvent.addRecord(record);
							gs.getTLogger().logEvent(roleOnline, tlogEvent);
						}
						gs.getRPCManager().notifyForceBattlePlayerPosChange(sid, fid, ilArg[0], ret);
					}
					break;
				case TYPE_BATTLE_CONTRIBUTITON:
					{
						int curTime = gs.getTime();
						SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(curTime);
						if (cfg == null)
						{
							cfg = GameData.getInstance().getForceBattleNext(curTime);
							if (cfg == null)
							{
								gs.getRPCManager().notifyForceBattleSyncContribution(sid, fid, 0, 0, 0, 0, null);
							}
							else
							{
								gs.getRPCManager().notifyForceBattleSyncContribution(sid, fid, cfg.id, cfg.startTime, cfg.endTime, cfg.selectionTimeStart, null);
							}
						}
						else
						{
							gs.getRPCManager().notifyForceBattleSyncContribution(sid, fid, cfg.id, cfg.startTime, cfg.endTime, cfg.selectionTimeStart, gs.getWarManager().getForceWarContribution(cfg.id, fid));
						}
					}
					break;
				case TYPE_CONTRIBUTE_BATTLE:
					{
						boolean ret = gs.getWarManager().contributeForceWar(ilArg[0], fid, roleOnline, ilArg[1], ilArg[2]);
						if (ret)
						{
							TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
							TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WAR_CONTRIBUTE);
							record.setArgs(iArg, ilArg[3]);
							tlogEvent.addRecord(record);
							gs.getTLogger().logEvent(roleOnline, tlogEvent);
						}
						gs.getRPCManager().notifyForceBattlePlayerContribute(sid, fid, ilArg[0], ret);
					}
					break;
				default:
					break;
				}
			}
			else
			{
				switch( type )
				{
				case TYPE_INFO:
					gs.getRPCManager().notifyForceInfo(sid, fid, null);
					break;
				case TYPE_ANNOUNCE:
					gs.getRPCManager().notifyForceSetAnnounce(sid, fid, 0);
					break;
				case TYPE_QQ:
					gs.getRPCManager().notifyForceSetQQ(sid, fid, 0);
					break;
				case TYPE_UPGRADE:
				case TYPE_CHUANWEI:
				case TYPE_SETPOS:
				case TYPE_DISMISS:
				case TYPE_QUIT:
				case TYPE_KICK:
					gs.getRPCManager().notifyForceCmnRes(sid, fid, 0);
					break;
				case TYPE_ACCEPT:
				case TYPE_ACCEPT_DB:
					gs.getRPCManager().notifyForceAccept(sid, (byte)1, 0, (byte)0);
					break;
				case TYPE_REFUSE:
					gs.getRPCManager().notifyForceAccept(sid, (byte)0, 0, (byte)0);
					break;
				case TYPE_SETICON:
					gs.getRPCManager().notifyForceSetIcon(sid, 0, 0);
					break;
				case TYPE_SETJOIN:
					gs.getRPCManager().notifyForceSetJoin(sid, fid, 0,(byte)0, (short)0);
					break;
				case TYPE_GETACTIVITY:
					gs.getRPCManager().notifyForceActivity(sid, fid, roleID, -1, -1, -1, null);
					break;
				case TYPE_BATTLE:
					gs.getRPCManager().notifyForceBattleSync(sid, fid, null, null, null);
					break;
				case TYPE_OPEN_BATTLE:
					gs.getRPCManager().notifyForceBattleOpen(sid, fid, iArg, false);
					break;
				case TYPE_JOIN_BATTLE:
					gs.getRPCManager().notifyForceBattlePlayerJoin(sid, fid, iArg, false);
					break;
				case TYPE_BATTLE_CHANGE_SEQ:
					gs.getRPCManager().notifyForceBattlePlayerPosChange(sid, fid, ilArg[0], false);
					break;
				case TYPE_BATTLE_CONTRIBUTITON:
					gs.getRPCManager().notifyForceBattleSyncContribution(sid, fid, 0, 0, 0, 0, null);
					break;
				case TYPE_CONTRIBUTE_BATTLE:
					gs.getRPCManager().notifyForceBattlePlayerContribute(sid, fid, ilArg[0], false);
					break;
				default:
					break;
				}
			}
			if( type == TYPE_ACCEPT_DB )
			{
				gs.getLoginManager().mapRoles.remove(iArg);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		@AutoInit
		public TableReadonly<String, Integer> maxids;
		@AutoInit
		public Table<Integer, DBRole> role;
		
		public final byte type;
		public final int fid;
		public int sid;
		public final int roleID;
		public String roleName;
		public String sArg;
		public String sArg2;
		public Object varArg;
		public int iArg;
		public int iArg2;
		public int[] ilArg;
		public Role roleOnline;
		int newStamp;
		public String fname;
		public short ficonID;
		public int ret;
		public ForceInfo info;
		public List<Integer> ilRes;
		public List<DBForce.Activity> activities;
		public List<DBForce.Member> members;
		public List<Short> gids;
		public List<Short> pids;
		public int forceActivity;
		public SBean.DBForceBeast beast;
	}
	
	int getMaxMemberCount(byte lvl)
	{
		return gs.getGameData().getForceCFG().maxMembers + lvl*2 - 2;
		//return lvl + 19;
	}
	
	public class ForceJoinTrans implements Transaction
	{
		public ForceJoinTrans(int fid, int sid, Role role)
		{
			this.fid = fid;
			this.sid = sid;
			this.role = role;
		}

		@Override
		public boolean doTransaction()
		{			
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if( dbforce.members.size() >= getMaxMemberCount(dbforce.lvl) )
				{
					res = 3;
					return true;
				}
				boolean bFound = false;
				for(DBForce.Member m : dbforce.members)
				{
					if( m.id == role.id )
					{
						bFound = true;
						break;
					}
				}
				if( bFound )
				{
					res = 4;
					return true;
				}
				SBean.ForceCFGS cfg = gs.getGameData().getForceCFG();
				if (dbforce.joinLvlReq < cfg.lvlCreate)
					dbforce.joinLvlReq = cfg.lvlCreate;
				if (role.lvl < dbforce.joinLvlReq)
				{
					res = 6;
					return true;
				}
				int now = gs.getTime();
				if (dbforce.joinMode == DBForce.JOIN_MODE_NOONE)
				{
					res = 7;
					return true;
				}
				else if (dbforce.joinMode == DBForce.JOIN_MODE_ANYONE)
				{
					int aret = role.acceptForce();
					if( aret != 0 )
					{
						res = 8;
						return true;
					}
					DBForce.Member newm = new DBForce.Member(role.id, now, 0, 0);
					dbforce.members.add(newm);
					dbforce.addLog(new DBForce.History().logJoin(now, role.name, ""));
					
					LoginManager.WorldChatCache.Msg m = new LoginManager.WorldChatCache.Msg();
					m.roleid = role.id;
					m.lvlVIP = 0;
					m.lvl = role.lvl;
					m.headIconID = role.headIconID;
					m.rolename = role.name;
					m.time = now;
					m.msg = LoginManager.WorldChatCache.Msg.encodeForceJoin(role.name);
					gs.getLoginManager().addForceChatMsg(fid, m);

					role.setAcceptForceRes(new Role.ForceInfo(fid, 0, 0, dbforce.name, dbforce.iconID, 0, (short)0));
					role.doSave();
					
					dbforce.lastModifyTime = now;
					force.put(fid, dbforce);
					res = -1;
				}
				else if (dbforce.joinMode == DBForce.JOIN_MODE_VERIFY)
				{
					bFound = false;
					for(DBForce.Apply m : dbforce.queue)
					{
						if( m.id == role.id )
						{
							bFound = true;
							break;
						}
					}
					if( bFound )
					{
						res = 5;
						return true;
					}
					dbforce.queue.add(new DBForce.Apply(role.id, gs.getTime()));
					//TODO
					if( dbforce.queue.size() > 50 )
					{
						dbforce.queue.remove(0);
					}
					refreshApplyCache(dbforce);
					
					dbforce.lastModifyTime = now;
					force.put(fid, dbforce);
					res = 0;
				}
				else
				{
					res = 9;
				}
			}
			else
				res = 2;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				gs.getRPCManager().notifyForceJoin(sid, fid, res);
			}
			else
			{
				gs.getRPCManager().notifyForceJoin(sid, fid, (byte)1);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public int fid;
		public int sid;
		public Role role;
		public byte res;
	}
	
	public interface QueryForceCallback
	{
		public void onCallback(ForceBrief brief);
	}
	
	public class QueryForceTrans implements Transaction
	{
		public QueryForceTrans(int fid, QueryForceCallback callback)
		{
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				ForceBrief brief = new ForceBrief();
				brief.fid = fid;
				brief.fname = dbforce.name;
				brief.iconID = dbforce.iconID;
				brief.lvl = dbforce.lvl;
				brief.members = (byte)dbforce.members.size();
				brief.msg = dbforce.msg;
				DBForce.Manager m1 = dbforce.managers.get(0);
				brief.headID = m1.id;
				brief.headName = dbforce.headName;
				this.brief = brief;
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(brief);
		}
		
		@AutoInit
		public TableReadonly<Integer, DBForce> force;
		
		public final int fid;
		public final QueryForceCallback callback;
		public ForceBrief brief;
	}
	
	public interface ModifyForceAnnounceCallback
	{
		public void onCallback(int errcode);
	}
	
	public class ModifyForceAnnounceTrans implements Transaction
	{
		public ModifyForceAnnounceTrans(int fid, String annouce, ModifyForceAnnounceCallback callback)
		{
			this.fid = fid;
			this.annouce = annouce;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				int now = gs.getTime();
				dbforce.msg = annouce;
				dbforce.lastModifyTime = now;
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK ? CommonRoleVisitor.ERR_OK : CommonRoleVisitor.ERR_DB_FAILED);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public final int fid;
		public final String annouce;
		public final ModifyForceAnnounceCallback callback;
	}
		
	public class ModifyForceLogTrans implements Transaction
	{
		public ModifyForceLogTrans(int fid, String log, DBForce.History history)
		{
			this.fid = fid;
			this.log = log;
			this.history = history;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				int now = gs.getTime();
				
				if(history!=null){
					dbforce.addLog(history);
				}else{
					dbforce.addLog(new DBForce.History().logOpenBattle(gs.getTime(),log));
				}
				
				force.put(fid, dbforce);
				
				dbforce.lastModifyTime = now;
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public final int fid;
		public final String log;
		public final DBForce.History history;
	}
	
	
	
	public class ModifyForceActivitiesTrans implements Transaction
	{
		public ModifyForceActivitiesTrans(int fid, Map<Integer, Integer> mapActivities)
		{
			this.fid = fid;
			this.mapActivities = mapActivities;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				int now = gs.getTime();
				dbforce.activities.clear();
				for (Map.Entry<Integer, Integer> e : mapActivities.entrySet())
					dbforce.activities.add(new DBForce.Activity(e.getKey(), e.getValue()));
				dbforce.lastModifyTime = now;
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public final int fid;
		public final Map<Integer, Integer> mapActivities;
	}
	
	
	public interface QueryForceMembersCallback
	{
		public void onCallback(boolean success, int headID, List<Integer> forceMembers);
	}
	
	public class QueryForceMembersTrans implements Transaction
	{
		public QueryForceMembersTrans(int fid, QueryForceMembersCallback callback, int afterJoinTime)
		{
			this.fid = fid;
			this.callback = callback;
			this.afterJoinTime = afterJoinTime;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				headID = dbforce.getKing();
				for (DBForce.Member m : dbforce.members)
				{
					if (m.joinTime + afterJoinTime < gs.getTime())
						members.add(m.id);
				}
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, headID, members);
		}
		
		@AutoInit
		public TableReadonly<Integer, DBForce> force;
		
		public final int fid;
		public final QueryForceMembersCallback callback;
		public int headID;
		public int afterJoinTime;
		public List<Integer> members = new ArrayList<Integer>();
	}
	
	public void getForceMembersRoleID(final int fid, QueryForceMembersCallback callback, int afterJoinTime)
	{
		gs.getDB().execute(new QueryForceMembersTrans(fid, callback, afterJoinTime));
	}
	
	public interface SendForceMailCallback
	{
		public void onCallback(int errorCode);
	}
	
	public class SendForceMailCallbackTrans implements Transaction
	{
		public SendForceMailCallbackTrans(int fid, Role role, String title, String content, SendForceMailCallback callback)
		{
			this.fid = fid;
			this.role = role;
			this.title = title;
			this.content = content;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			String rtitle = gs.getLoginManager().checkUserInput(title, gs.getGameData().getUserInputCFG().mail, false, true);
			if (rtitle == null)
			{
				this.errorCode = -2;
				return true;
			}
			String rcontent = gs.getLoginManager().checkUserInput(content, gs.getGameData().getUserInputCFG().sysMail, false, true);
			if (rcontent == null)
			{
				this.errorCode = -2;
				return true;
			}
			DBForce dbforce = force.get(fid);
			if( dbforce == null )
			{
				this.errorCode = -3;
				return true;
			}
			
			int king = dbforce.getKing();
			if (king != role.id)
			{
				this.errorCode = -4;
				return true;
			}
			SBean.ForceCFGS forcecfg = GameData.getInstance().getForceCFG();
			if (dbforce.sendMailCnt >= forcecfg.sendMailMaxCnt)
			{
				this.errorCode = -5;
				return true;
			}
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			for (DBForce.Member m : dbforce.members)
			{
				gs.getLoginManager().userSendMessage(role, m.id, rtitle, rcontent, 0, true, attLst);
			}
			dbforce.sendMailCnt++;
			dbforce.lastModifyTime = gs.getTime();
			force.put(fid, dbforce);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
				this.errorCode = -1;
			callback.onCallback(this.errorCode);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public final int fid;
		public final Role role;
		public final String title;
		public final String content;
		public final SendForceMailCallback callback;
		public int errorCode;

	}
	
	public void sendForceMail(final int fid, Role role, String title, String content, SendForceMailCallback callback)
	{
		gs.getDB().execute(new SendForceMailCallbackTrans(fid, role, title, content, callback));
	}
	
	public ForceManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void createForce(Role role, final String fname, final short iconID)
	{
		gs.getDB().execute(new CreateForceTrans(role, fname, iconID));
	}
	
	public void getBrief(int fid, int sid, Role roleOnline)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_INFO, fid, sid, roleOnline.id, null, 0, roleOnline));
	}
	
	public void dismiss(int fid, int sid, Role roleOnline)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_DISMISS, fid, sid, roleOnline.id, null, 0, roleOnline));
	}
	
	public void quit(int fid, int sid, Role roleOnline)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_QUIT, fid, sid, roleOnline.id, null, 0, roleOnline));
	}
	
	public void getStamp(int fid, int sid, int roleID)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_STAMP, fid, sid, roleID));
	}
	
	public void getMembers(int fid, int sid, int roleID)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_MEMBER, fid, sid, roleID));
	}
	
	public void getPos(int fid, int sid, int roleID)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_GETPOS, fid, sid, roleID));
	}
	
	public void sendExpirate(int fid, int sid, int roleID,String tarGameServer,String openId)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_EXPATRIATE, fid, sid, roleID,tarGameServer,openId));	
	}
	
	public void getApply(int fid, int sid, int roleID)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_APPLY, fid, sid, roleID));
	}
	
	public void getLogs(int fid, int sid, int roleID, int ltime)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_LOGS, fid, sid, roleID, ltime));
	}
	
	public void setAnnounce(int fid, int sid, int roleID, String msg, Role roleOnline)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_ANNOUNCE, fid, sid, roleID, msg, 0, roleOnline));
	}
	
	public void setQQ(int fid, int sid, int roleID, String msg)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_QQ, fid, sid, roleID, msg, 0, null));
	}
	
	public void accept(int fid, int sid, Role forceOwner, int roleID, String roleName, int aid, byte acc)
	{
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		if( acc == 1 )
		{
			Role role = gs.getLoginManager().mapRoles.putIfAbsent(aid, Role.NULL_ROLE);
			if( role == null ) // 
			{
				gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_ACCEPT_DB, fid, sid, roleID, roleName, null, aid, null));
				TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_ACCEPT);
				tlogEvent.addRecord(record);
				gs.getTLogger().logEvent(forceOwner, tlogEvent);
				return;
			}
			if( role.isNull() )
			{
				gs.getRPCManager().notifyForceAccept(sid, acc, 0, (byte)0);
				return;
			}		
			int aret = role.acceptForce();
			if( aret < 0 )
			{
				gs.getRPCManager().notifyForceAccept(sid, acc, 0, (byte)0);
				return;
			}
			gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_ACCEPT, fid, sid, roleID, roleName, null, aid, aret==0?role:null));
			TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_ACCEPT);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(forceOwner, tlogEvent);
		}
		else
		{
			gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_REFUSE, fid, sid, roleID, null, aid, null));
			TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_REFUSE);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(forceOwner, tlogEvent);
		}
	}
	
	public void kick(int fid, int sid, Role roleOnline, int aid)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_KICK, fid, sid, roleOnline.id, null, aid, roleOnline));
	}
	
	public void setPos(int fid, int sid, Role role, int aid, byte newpos)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_SETPOS, fid, sid, role.id, null, aid, newpos, role));
	}

	public void chuanwei(int fid, int sid, Role roleOnline, int rid)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_CHUANWEI, fid, sid, roleOnline.id, null, rid, roleOnline));
	}
	
	public void join(int fid, int sid, Role role)
	{
		gs.getDB().execute(new ForceJoinTrans(fid, sid, role));
	}
	
	public void listForce(int fid, int sid, int roleID, byte lvlSeek)
	{
		gs.getDB().execute(new ForceInfoTrans(ForceInfoTrans.TYPE_LIST, fid, sid, roleID, lvlSeek));
	}
	
	public void getActivity(int fid, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_GETACTIVITY, fid, sid, role.id, null, 0, role));
	}
	
	public void activityReward(int fid, int sid, Role role, int reward)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_ACTIVITYREWARD, fid, sid, role.id, null, reward, role));
	}
	
	public void getBattleInfo(int fid, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_BATTLE, fid, sid, role.id, null, 0, role));
	}
	
	public void openBattle(int fid, int bid, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_OPEN_BATTLE, fid, sid, role.id, null, bid, role));
	}
	
	public void joinBattle(int fid, int bid, List<Short> gids, List<Short> pids, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_JOIN_BATTLE, fid, sid, role.id, null, bid, role, gids, pids));
	}
	
	public void changeBattlePlayerSeq(int fid, int bid, int seq, int change, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_BATTLE_CHANGE_SEQ, fid, sid, role.id, new int[]{bid, seq, change, 0}, role));
	}
	
	public void getBattleContributionInfo(int fid, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_BATTLE_CONTRIBUTITON, fid, sid, role.id, null, 0, role));
	}
	
	public void battleContribute(int fid, int bid, int id, int count, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_CONTRIBUTE_BATTLE, fid, sid, role.id, new int[]{bid, id, count, 0}, role));
	}
	
	public void updateForceLevel(int fid, byte lvl)
	{
		actives.put(new ForceLevel(fid, lvl));
	}
	
	public List<Integer> getRandomForceID(byte lvlSeek)
	{
		List<Integer> r = new ArrayList<Integer>();
		List<ForceLevel> lst = new ArrayList<ForceLevel>();
		actives.get(lst, lvlSeek, (byte)1, GameData.MAX_FORCE_LEVEL, new TreeSet<Integer>());
		for(ForceLevel e : lst)
			r.add(e.id);
		int i = 0;
		while( r.size() < ActiveForces.MAX_GET_COUNT )
		{
			i++;
			if( ! r.contains(new Integer(i)) )
				r.add(i);
		}
		return r;
	}
	
	public void queryForce(int fid, QueryForceCallback callback)
	{
		gs.getDB().execute(new QueryForceTrans(fid, callback));
	}
	
	public void setIcon(int fid, int sid, int roleID, int icon, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_SETICON, fid, sid, roleID, null, icon, role));
	}
	
	public void setJoin(int fid, int sid, int roleID, byte mode, short lvlReq, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_SETJOIN, fid, sid, roleID, null, mode, lvlReq, role));
	}
	
	public void setForceAnnounce(int fid, String announce, ModifyForceAnnounceCallback callback)
	{
		gs.getDB().execute(new ModifyForceAnnounceTrans(fid, announce, callback));
	}

	private void refreshApplyCache(DBForce dbforce)
	{
		boolean exist = dbforce.queue.size() > 0;
		if (exist) {
			ApplyCache a = new ApplyCache();
			for (DBForce.Manager m : dbforce.managers)
				if (dbforce.canAccept(m.id))
					a.rids.add(m.id);
			a.exist = true;
			synchronized (applyCache) {
				applyCache.put(dbforce.id, a);
			}
		}
		else {
			synchronized (applyCache) {
				applyCache.remove(dbforce.id);
			}
		}
		
		for (DBForce.Manager m : dbforce.managers) {
			if (dbforce.canAccept(m.id)) {
				int sid = gs.getLoginManager().getLoginRoleSessionID(m.id);
				if (sid > 0)
					gs.getRPCManager().notifyForceApplyExist(sid, exist);
			}
		}
	}
	
	public boolean isApplyExist(int fid, int rid)
	{
		synchronized (applyCache) {
			ApplyCache a = applyCache.get(fid);
			if (a != null && a.rids.contains(rid) && a.exist)
				return true;
		}
		return false;
	}
	
	public void addActivity(int fid, int add)
	{
		if (fid <= 0 || add <= 0)
			return;
		
		synchronized (activityCache) {
			Integer activity = activityCache.get(fid);
			if (activity != null)
				activity += add;
			else
				activity = add;
			activityCache.put(fid, activity);
		}
	}
	
	public void addCup(int fid, int add)
	{
		if (fid <= 0 || add <= 0)
			return;
		
		synchronized (cupCache) {
			Integer cup = cupCache.get(fid);
			if (cup != null)
				cup += add;
			else
				cup = add;
			cupCache.put(fid, cup);
		}
	}
	
	public void sendForceActivityLog(int fid, String name , short itemID)
	{
		gs.getDB().execute(new ModifyForceLogTrans(
				fid, "",new DBForce.History().logForceActivity(gs.getTime(), name, itemID)));
	}
	
	public int retrieveActivity(int fid)
	{
		int ret = 0;
		synchronized (activityCache) {
			Integer activity = activityCache.get(fid);
			if (activity != null) {
				ret = activity;
				activityCache.remove(fid);
			}
		}
		return ret;
	}
	
	public int retrieveCup(int fid)
	{
		int ret = 0;
		synchronized (cupCache) {
			Integer cup = cupCache.get(fid);
			if (cup != null) {
				ret = cup;
				cupCache.remove(fid);
			}
		}
		return ret;
	}
	
	public void initDungeon(SBean.DBForceDungeonGlobal dungeon)
	{
		dungeonGlobal = dungeon;
		if (dungeonGlobal == null) {
			dungeonGlobal = new SBean.DBForceDungeonGlobal(new ArrayList<SBean.DBForceTreasure>(), new ArrayList<SBean.DBForceChapterDamage>(), new ArrayList<SBean.DBForceChapterDamage>(), new ArrayList<SBean.DBForceTreasureDeploy>(), (byte)0, (byte)0, (byte)0, 0);
		}
			
		if (dungeonDamage == null) {
			dungeonDamage = new HashMap<Byte, DungeonChapterDamage>();
		}
		else
			dungeonDamage.clear();
		
		if (dungeonTreasure == null) {
			dungeonTreasure = new HashMap<Integer, SBean.DBForceTreasure>();
		}
		else
			dungeonTreasure.clear();
		
		if (dungeonTreasureDeploy == null) {
			dungeonTreasureDeploy = new HashMap<Integer, SBean.DBForceTreasureDeploy>();
		}
		else
			dungeonTreasureDeploy.clear();
		
		SBean.DungeonCFGS cfg = gs.gameData.getDungeonCFG();
		for (byte i = 0; i < cfg.chapters.size(); i ++)
			dungeonDamage.put(i, new DungeonChapterDamage());
		
		for (SBean.DBForceChapterDamage d : dungeonGlobal.damages)
			setDamage(d.chapter, d.damage, d.fid, d.rid, d.fname, d.name, d.icon, d.lvl, d.generals, d);
		for (SBean.DBForceChapterDamage d : dungeonGlobal.forceDamages)
			setForceDamage(d.chapter, d.damage, d.fid, d.rid, d.fname, d.name, d.icon, d.lvl, d.generals, d);
		
		for (SBean.DBForceTreasure t : dungeonGlobal.treasure)
			dungeonTreasure.put(t.fid, t);
		
		for (SBean.DBForceTreasureDeploy d : dungeonGlobal.deploys)
			dungeonTreasureDeploy.put(d.fid, d);
	}
	
	public SBean.DBForceDungeon createDefaultDungeon(int fid)
	{
		List<SBean.DBForceChapter> chapters = new ArrayList<SBean.DBForceChapter>();
		SBean.DungeonCFGS cfg = gs.gameData.getDungeonCFG();
		for (int i = 0; i < cfg.chapters.size(); i ++)
			chapters.add(new SBean.DBForceChapter((byte)-1, new ArrayList<DBForceChapterGeneralStatus>(), new ArrayList<DBForceChapterBox>(), 
                    new ArrayList<Integer>(), (byte)0, 0, 0));
		return new SBean.DBForceDungeon(fid, gs.getDayCommon(), chapters, 0, 0);
	}
	
	public interface SyncDungeonCallback
	{
		public void onCallback(SBean.DBForceDungeon dungeon);
	}
	
	public class SyncDungeonTrans implements Transaction
	{
		public SyncDungeonTrans(int fid, SyncDungeonCallback callback)
		{
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.dungeon.isEmpty())
					return false;
				
				dungeon = dbforce.dungeon.get(0);
				
				if (dungeon.day < gs.getDayCommon()) {
					for (SBean.DBForceChapter c : dungeon.chapters)
						c.rids.clear();
					dungeon.day = gs.getDayCommon();
					force.put(fid, dbforce);
				}
				
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				if (dcfg == null)
					return false;
				
				synchronized (ForceManager.this) {
					ForceDungeonRTStatus rts = dungeonRTStatusMap.get(fid);
					if (rts == null) {
						rts = new ForceDungeonRTStatus();
						dungeonRTStatusMap.put(fid, rts);
					}
					
					SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
					if (tr == null) {
						tr = new SBean.DBForceTreasure(fid, new ArrayList<DBForceTreasureList>(), new ArrayList<SBean.DBForceTotalTreasure>(), new ArrayList<DBForceTreasureLog>());
						for (int i = 0; i < dcfg.chapters.size(); i ++) {
							List<DBForceChapterTreasure> tre = new ArrayList<DBForceChapterTreasure>();
							SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(i);
							if (ccfg != null) {
								for (SBean.DungeonApplyCFGS acfg : ccfg.applies) {
									tre.add(new DBForceChapterTreasure(acfg.type, acfg.id));
									
									boolean found = false;
									for (SBean.DBForceTotalTreasure tot : tr.totalTreasure) 
										if (tot.type == acfg.type && tot.id == acfg.id) {
											found = true;
											break;
										}
									
									if (!found)
										tr.totalTreasure.add(new SBean.DBForceTotalTreasure(acfg.type, acfg.id, 0, 0, new ArrayList<SBean.DBForceTreasureApply>()));
								}
							}
							tr.treasure.add(new SBean.DBForceTreasureList(tre));
						}
						
						dungeonTreasure.put(fid, tr);
						dungeonGlobal.treasure.add(tr);
					}
					else {
						for (int i = 0; i < tr.treasure.size(); i ++) {
							List<DBForceChapterTreasure> tre = new ArrayList<DBForceChapterTreasure>();
							List<DBForceChapterTreasure> treOld = tr.treasure.get(i).list;
							SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(i);
							if (ccfg != null) {
								for (SBean.DungeonApplyCFGS acfg : ccfg.applies) {
									SBean.DBForceChapterTreasure old = null;
									for (SBean.DBForceChapterTreasure ct : treOld)
										if (ct.type == acfg.type && ct.id == acfg.id) {
											old = ct;
											break;
										}
									
									if (old == null)
										tre.add(new DBForceChapterTreasure(acfg.type, acfg.id));
									else
										tre.add(old);
									
									boolean found = false;
									for (SBean.DBForceTotalTreasure tot : tr.totalTreasure) 
										if (tot.type == acfg.type && tot.id == acfg.id) {
											found = true;
											break;
										}
									
									if (!found)
										tr.totalTreasure.add(new SBean.DBForceTotalTreasure(acfg.type, acfg.id, 0, 0, new ArrayList<SBean.DBForceTreasureApply>()));
								}
								if (tre.size() > treOld.size())
									tr.treasure.get(i).list = tre;
							}
						}						
						for (int i = tr.treasure.size(); i < dcfg.chapters.size(); i ++) {
							List<DBForceChapterTreasure> tre = new ArrayList<DBForceChapterTreasure>();
							SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(i);
							if (ccfg != null) {
								for (SBean.DungeonApplyCFGS acfg : ccfg.applies) {
									tre.add(new DBForceChapterTreasure(acfg.type, acfg.id));
									
									boolean found = false;
									for (SBean.DBForceTotalTreasure tot : tr.totalTreasure) 
										if (tot.type == acfg.type && tot.id == acfg.id) {
											found = true;
											break;
										}
									
									if (!found)
										tr.totalTreasure.add(new SBean.DBForceTotalTreasure(acfg.type, acfg.id, 0, 0, new ArrayList<SBean.DBForceTreasureApply>()));
								}
							}
							tr.treasure.add(new SBean.DBForceTreasureList(tre));
						}						
					}
					
					SBean.DBForceTreasureDeploy trd = dungeonTreasureDeploy.get(fid);
					if (trd == null) {
						trd = new SBean.DBForceTreasureDeploy(fid, (short)0, 0);
						dungeonTreasureDeploy.put(fid, trd);
						dungeonGlobal.deploys.add(trd);
					}
				}
				
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback((errcode == ErrorCode.eOK)?dungeon:null);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int fid;
		SBean.DBForceDungeon dungeon;
		final SyncDungeonCallback callback;
	}
	
	public void dungeonSyncChapter(final int sid, final int fid, final byte chapter, final int rid)
	{
		gs.getDB().execute(new SyncDungeonTrans(fid, new SyncDungeonCallback() {
			@Override
			public void onCallback(SBean.DBForceDungeon dungeon) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonSyncChapterRes(fid, chapter, rid, dungeon));
			}
		}));
	}
	
	public void dungeonSync(final int sid, final int fid)
	{
		gs.getDB().execute(new SyncDungeonTrans(fid, new SyncDungeonCallback() {
			@Override
			public void onCallback(SBean.DBForceDungeon dungeon) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonSyncRes(fid, dungeon));
			}
		}));
	}
	
	public interface CommonResultCallback
	{
		public void onCallback(boolean success, int[] values);
	}
	
	public class FightingDungeonTrans implements Transaction
	{
		public FightingDungeonTrans(int fid, byte chapter, String[] name, List<SBean.DBForceChapterGeneralStatus> status, CommonResultCallback callback)
		{
			this.fid = fid;
			this.chapter = chapter;
			this.status = status;
			this.callback = callback;
			this.name = name;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				
				status.clear();
				for (SBean.DBForceChapterGeneralStatus s : ch.enemyStatus)
					status.add(s);
				
				name[0] = "";
				int now = gs.getTime();
				synchronized (ForceManager.this) {
					ForceDungeonRTStatus rts = dungeonRTStatusMap.get(fid);
					if (rts != null) {
						ForceDungeonFighting fighting = rts.fighting.get(chapter);
						if (fighting != null && fighting.time + 60 * 2 > now) {
							name[0] = fighting.name;
						}
					}
				}
				
				if (name[0].isEmpty())
					return false;

				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, null);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int fid;
		final byte chapter;
		final String[] name;
		final CommonResultCallback callback;
		final List<SBean.DBForceChapterGeneralStatus> status;
	}
	
	public void dungeonFighting(final int sid, final int fid, final byte chapter)
	{
		final List<SBean.DBForceChapterGeneralStatus> status = new ArrayList<SBean.DBForceChapterGeneralStatus>();
		final String[] name = {""};
		gs.getDB().execute(new FightingDungeonTrans(fid, chapter, name, status, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonFightingRes(fid, chapter, name[0], status));
			}
		}));
	}
	
	public class ActivateDungeonTrans implements Transaction
	{
		public ActivateDungeonTrans(int rid, int fid, byte chapter, CommonResultCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.chapter = chapter;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModDungeon(rid))
					return false;
				
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				if (ch.stage >= 0)
					return false;
				
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				if (chapter >= dcfg.chapters.size())
					return false;
				SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(chapter);
				if (dbforce.activity < ccfg.resetActivity)
					return false;
				
				dbforce.activity -= ccfg.resetActivity;
				ch.stage = 0;
//				dbforce.addLog(new DBForce.History().logResetChapter(gs.getTime(), rname,dbforce.getPos(rid), chapter));
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, null);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		public final int fid;
		public final int rid;
		public final byte chapter;
		public final CommonResultCallback callback;
	}
	
	public void dungeonActivateChapter(final int sid, final int fid, final byte chapter, Role role)
	{
		SBean.DungeonCFGS cfg = gs.gameData.getDungeonCFG();
		if (chapter < 0 || chapter >= cfg.chapters.size()) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonActivateChapterRes(fid, chapter, false));
			return;
		}
		
		if (role.lvl < cfg.chapters.get(chapter).openLvl) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonActivateChapterRes(fid, chapter, false));
			return;
		}
			
		gs.getDB().execute(new ActivateDungeonTrans(role.id, fid, chapter, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonActivateChapterRes(fid, chapter, success));
			}
		}));
	}
	
	class ForceDungeonFighting
	{
		ForceDungeonFighting(int time, int rid, String name) {
			this.time = time;
			this.rid = rid;
			this.name = name;
		}
		int time;
		int rid;
		String name;
	}
	
	class ForceDungeonRTStatus
	{
		Map<Byte, ForceDungeonFighting> fighting = new HashMap<Byte, ForceDungeonFighting>();
		Map<Integer, SBean.CombatBonus> combatBonus = new HashMap<Integer, SBean.CombatBonus>();
	}
	
	public class StartAttackDungeonTrans implements Transaction
	{
		public StartAttackDungeonTrans(int fid, int rid, String rname, byte chapter, byte stage, List<SBean.DBForceChapterGeneralStatus> status, List<SBean.CombatBonus> bonus, CommonResultCallback callback)
		{
			this.fid = fid;
			this.rid = rid;
			this.rname = rname;
			this.chapter = chapter;
			this.stage = stage;
			this.status = status;
			this.bonus = bonus;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				if (ch.stage < 0 || ch.stage != stage) {
					ret[0] = -1;
					return false;
				}
				
				int count = 0;
				for (int r : ch.rids)
					if (r == rid)
						count ++;
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				if (count >= dcfg.basic.dailyTimes)
					return false;
				
				SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(chapter);
				if (stage < 0 || stage >= ccfg.stages.size())
					return false;
				SBean.DungeonStageCFGS scfg = ccfg.stages.get(stage);
				
				synchronized (ForceManager.this) {
					ForceDungeonRTStatus rts = dungeonRTStatusMap.get(fid);
					if (rts == null)
						return false;
					
					final int now = gs.getTime();
					ForceDungeonFighting fighting = rts.fighting.get(chapter);
					if (fighting != null) {
						if (fighting.time + 60 * 2 > now) {
							ret[0] = -2;
							return false;
						}
						else
							rts.fighting.remove(chapter);
					}
				
					SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(scfg.combatId);
					if (cfg == null)
						return false;

					GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, (byte)1, (byte)1, (byte)1, (byte)1, null, null, null, (byte)0);
					rts.combatBonus.put(rid, bonusInfo.cb);
					if (bonusInfo.cb != null)
						bonus.add(bonusInfo.cb);
					
					rts.fighting.put(chapter, new ForceDungeonFighting(now, rid, rname));
				}

				status.clear();
				for (SBean.DBForceChapterGeneralStatus s : ch.enemyStatus)
					status.add(s);
				force.put(fid, dbforce);
				
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, ret);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int fid;
		final int rid;
		final String rname;
		final byte chapter;
		final byte stage;
		final CommonResultCallback callback;
		final List<SBean.DBForceChapterGeneralStatus> status;
		final List<SBean.CombatBonus> bonus;
		final int[] ret = {0};
	}
	
	public void dungeonStartAttack(final int sid, final Role role, final int fid, final byte chapter, final byte stage)
	{
		final List<SBean.DBForceChapterGeneralStatus> status = new ArrayList<SBean.DBForceChapterGeneralStatus>();
		final List<SBean.CombatBonus> bonus = new ArrayList<SBean.CombatBonus>();
		gs.getDB().execute(new StartAttackDungeonTrans(fid, role.id, role.name, chapter, stage, status, bonus, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonStartAttackRes(fid, chapter, stage, status, bonus, success, values[0]));
			}
		}));
	}
	
	public class FinishAttackDungeonTrans implements Transaction
	{
		public FinishAttackDungeonTrans(int fid, int rid, byte chapter, byte stage, boolean win, List<SBean.DBForceChapterGeneralStatus> status, int damage, int total, 
				List<SBean.CombatBonus> bonus, List<SBean.DropEntry> treasure, CommonResultCallback callback)
		{
			this.fid = fid;
			this.rid = rid;
			this.chapter = chapter;
			this.stage = stage;
			this.status = status;
			this.damage = damage;
			this.total = total;
			this.win = win;
			this.bonus = bonus;
			this.treasure = treasure;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				if (ch.stage < 0 || ch.stage != stage)
					return false;
				
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(chapter);
				if (stage < 0 || stage >= ccfg.stages.size())
					return false;
				SBean.DungeonStageCFGS scfg = ccfg.stages.get(stage);
				
				int totalHpLeft1 = 0;
				Map<Short, SBean.DBForceChapterGeneralStatus> statusMap = new HashMap<Short, SBean.DBForceChapterGeneralStatus>();
				for (SBean.DBForceChapterGeneralStatus s : ch.enemyStatus) {
					statusMap.put(s.id, s);
					totalHpLeft1 += s.hp;
				}
				for (SBean.DBForceChapterGeneralStatus s : status)
					statusMap.put(s.id, s);
				int totalHpLeft2 = 0;
				for (SBean.DBForceChapterGeneralStatus s : statusMap.values())
					totalHpLeft2 += s.hp;
				
				synchronized (ForceManager.this) {
					ForceDungeonRTStatus rts = dungeonRTStatusMap.get(fid);
					if (rts == null)
						return false;
					
					ForceDungeonFighting fighting = rts.fighting.get(chapter);
					if (fighting != null) {
						if (fighting.rid == rid) {
							rts.fighting.remove(chapter);
						}
						else
							return false;
					}
					else
						return false;
					
					SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
					if (tr == null)
						return false;
					
					SBean.CombatBonus bo = rts.combatBonus.get(rid);
					if (bo != null) {
						bonus.add(bo);
					}
					rts.combatBonus.remove(rid);
					
					int percent1 = 0, percent2 = 0;
					if (total >= totalHpLeft1 && total >= totalHpLeft2) {
						if (totalHpLeft1 == 0)
							totalHpLeft1 = total;
						if (total > 0) {
							percent1 = (total - totalHpLeft1) * 100 / total;
							percent2 = (total - totalHpLeft2) * 100 / total;
						}
					}
					if (win)
						percent2 = 100;
					SBean.DungeonTreasureCFGS drop = null;
					for (SBean.DungeonTreasureCFGS d : scfg.treasureDrops) {
						if (d.percent > percent1 && d.percent <= percent2)
							drop = d;
					}
					if (drop != null) {
						for (short dropId : drop.drops) {
							SBean.DropEntry d = gs.gameData.getDropTableRandomEntry(dropId);
							if (d != null)
								treasure.add(d.kdClone());
						}
					}
					
					for (SBean.DropEntry d : treasure) {
						boolean found = false;

						SBean.DBForceTotalTreasure tre = null;
						for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
							if (t.type == d.type && t.id == d.id) {
								t.count += d.arg;
								found = true;
								tre = t;
								break;
							}
						}

						if (!found) {
							SBean.DBForceTotalTreasure t = new SBean.DBForceTotalTreasure(d.type, d.id, d.arg, 0, new ArrayList<SBean.DBForceTreasureApply>());
							tr.totalTreasure.add(t);
							tre = t;
						}

						if (tre != null && tre.count > 0 && tre.apply.size() > 0)
							tre.time = gs.getTime();
					}
					if (gs.gameData.getRandInt(0, 100) < scfg.cupPoss) {
						cupCount = scfg.cupCount;
						dbforce.cupCount += cupCount;
					}
				}
				
				ch.enemyStatus.clear();
				for (SBean.DBForceChapterGeneralStatus s : statusMap.values())
					ch.enemyStatus.add(s);
				
				ch.rids.add(rid);
				if (win) {
					ch.stage ++;
					synchronized (ForceManager.this) {
						if (ch.stage >= GameData.MAX_DUNGEON_STAGES && ch.reward == 0){
							damageRewards.add(new RewardMembers(chapter, collectDamageRewards(fid, chapter)));
							dbforce.addLog(new DBForce.History().logFinishChapter(gs.getTime(), chapter));
						}
							
					}

					ch.enemyStatus.clear();
				}
				force.put(fid, dbforce);

				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, new int[]{cupCount});
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int fid;
		final int rid;
		final byte chapter;
		final byte stage;
		short cupCount;
		final boolean win;
		final CommonResultCallback callback;
		final List<SBean.DBForceChapterGeneralStatus> status;
		final int damage;
		final int total;
		final List<SBean.CombatBonus> bonus;
		final List<SBean.DropEntry> treasure;
	}
	
	public void dungeonFinishAttack(final int sid, final Role role, final int fid, final byte chapter, final byte stage, final boolean win, final List<SBean.DBForceChapterGeneralStatus> status, 
			final int damage, final int total, final List<Short> gids)
	{
		final List<SBean.CombatBonus> bonus = new ArrayList<SBean.CombatBonus>();
		final List<SBean.DropEntry> treasure = new ArrayList<SBean.DropEntry>();
		gs.getDB().execute(new FinishAttackDungeonTrans(fid, role.id, chapter, stage, win, status, damage, total, bonus, treasure, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				if (success) {
					for (SBean.CombatBonus cb : bonus)
						role.addCombatBonus(cb);
					role.setForceChapterDamage(chapter, gids, damage);
				}
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonFinishAttackRes(fid, chapter, stage, win, bonus, treasure, values[0], success));
			}
		}));
	}
	
	public class ResetDungeonChapterTrans implements Transaction
	{
		public ResetDungeonChapterTrans(int rid,String rname,int fid, byte chapter, CommonResultCallback callback)
		{
			this.rid = rid;
			this.rname = rname;
			this.fid = fid;
			this.chapter = chapter;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModDungeon(rid))
					return false;
				
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				if (ch == null)
					return false;
				/*
				if (ch.stage <= 0)
					return false;
				*/
				
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				if (chapter >= dcfg.chapters.size())
					return false;
				SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(chapter);
				if (dbforce.activity < ccfg.resetActivity)
					return false;
				
				ForceDungeonRTStatus rts = dungeonRTStatusMap.get(fid);
				if (rts != null) {
					final int now = gs.getTime();
					ForceDungeonFighting fighting = rts.fighting.get(chapter);
					if (fighting != null) {
						if (fighting.time + 60 * 2 > now) {
							ret[0] = -1;
							return false;
						}
						else
							rts.fighting.remove(chapter);
					}
					
					rts.combatBonus.clear();
					rts.fighting.clear();
				}
				
				dbforce.activity -= ccfg.resetActivity;
				ch.stage = 0;
				ch.enemyStatus.clear();
				ch.boxTaken.clear();
				synchronized (ForceManager.this) {
					if (ch.reward == 0)
						damageRewards.add(new RewardMembers(chapter, collectDamageRewards(fid, chapter)));
					else
						ch.reward = 0;
				}
				dbforce.addLog(new DBForce.History().logResetChapter(gs.getTime(), rname,dbforce.getPos(rid), chapter));
				force.put(fid, dbforce);		
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, ret);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int rid;
		final String rname;
		final int fid;
		final byte chapter;
		final int[] ret = {0};
		final CommonResultCallback callback;
	}
	
	public void dungeonResetChapter(final int sid, final int rid, final String rname, final int fid, final byte chapter)
	{
		gs.getDB().execute(new ResetDungeonChapterTrans(rid, rname,fid, chapter, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonResetChapterRes(fid, chapter, success, values[0]));
			}
		}));
	}
	
	public class OpenDungeonChapterBoxTrans implements Transaction
	{
		public OpenDungeonChapterBoxTrans(Role role, int fid, byte chapter, byte stage, List<SBean.DropEntry> drops, CommonResultCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.chapter = chapter;
			this.stage = stage;
			this.drops = drops;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.dungeon.isEmpty())
					return false;
				
				SBean.DBForceDungeon dungeon = dbforce.dungeon.get(0);
				if (chapter < 0 || chapter >= dungeon.chapters.size())
					return false;
				
				SBean.DBForceChapter ch = dungeon.chapters.get(chapter);
				if (ch.stage <= 0)
					return false;
				
				SBean.DungeonCFGS dcfg = gs.gameData.getDungeonCFG();
				SBean.DungeonChapterCFGS ccfg = dcfg.chapters.get(chapter);
				if (stage < 0 || stage >= ccfg.stages.size())
					return false;
				SBean.DungeonStageCFGS scfg = ccfg.stages.get(stage);
				
				SBean.DBForceChapterBox box = null;
				for (SBean.DBForceChapterBox b : ch.boxTaken) {
					if (b.stage == stage)
						box = b;
				}
				
				if (box == null) {
					box = new SBean.DBForceChapterBox(stage, new ArrayList<Integer>());
					ch.boxTaken.add(box);
				}
				
				for (int r : box.rids)
					if (r == role.id)
						return false;
				
				synchronized (role) {
					if (role.stone < dcfg.basic.boxStone)
						return false;
					role.useStone(dcfg.basic.boxStone);
				}
				
				box.rids.add(role.id);
				
				for (short dropId : scfg.boxDropIds) {
					SBean.DropEntry drop = gs.gameData.getDropTableRandomEntry(dropId);
					if (drop != null)
						drops.add(drop.kdClone());
				}
				
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null) {
				callback.onCallback(errcode == ErrorCode.eOK, null);
				synchronized (role) {
					for (SBean.DropEntry d : drops)
						role.addDropNew(new SBean.DropEntryNew(d.type, d.id, d.arg));
				}
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		final byte chapter;
		final byte stage;
		final List<SBean.DropEntry> drops;
		final CommonResultCallback callback;
	}
	
	public void dungeonOpenBox(final int sid, final Role role, final int fid, final byte chapter, final byte stage)
	{
		final List<SBean.DropEntry> drops = new ArrayList<SBean.DropEntry>();
		gs.getDB().execute(new OpenDungeonChapterBoxTrans(role, fid, chapter, stage, drops, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonOpenBoxRes(fid, chapter, stage, drops, success));
			}
		}));
	}
	
	public void dungeonApplyTreasure(final int sid, final Role role, final int fid, final byte chapter, final byte type, final short id)
	{
		boolean success = false;
		int applyCount = 0;
		int time = 0;
		
		synchronized (role) {
			if (role.forceInfo.id != fid) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonApplyTreasureRes(fid, chapter, type, id, success, applyCount, time));
				return;
			}
		}
		
		synchronized (this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					if (t.type != type || t.id != id) {
						List<SBean.DBForceTreasureApply> delApply = new ArrayList<SBean.DBForceTreasureApply>();
						for (SBean.DBForceTreasureApply apply : t.apply) {
							if (apply.rid == role.id)
								delApply.add(apply);
						}
						for (SBean.DBForceTreasureApply a : delApply)
							t.apply.remove(a);
						
						if (t.apply.size() == 0)
							t.time = 0;
					}
				}
				
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					if (t.type == type && t.id == id) {
						for (SBean.DBForceTreasureApply apply : t.apply) {
							if (apply.rid == role.id)
								break;
						}
						t.apply.add(new SBean.DBForceTreasureApply(role.id, role.name, role.headIconID, role.lvl));
						if (t.count > 0 && t.time == 0)
							t.time = gs.getTime();
						applyCount = t.apply.size();
						time = t.time;
						success = true;
						break;
					}
				}
			}
		}
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonApplyTreasureRes(fid, chapter, type, id, success, applyCount, time));
	}
	
	class MemberBrief
	{
		MemberBrief(int rid, String name, short headIcon, short lvl) {
			this.rid = rid;
			this.name = name;
			this.headIcon = headIcon;
			this.lvl = lvl;
		}
		
		public int rid;
		public String name;
		public short headIcon;
		public short lvl;
	}
	
	public interface GetMembersCallback
	{
		public void onCallback(List<MemberBrief> members);
	}
	
	public class GetMembersTrans implements Transaction
	{
		public GetMembersTrans(int fid, GetMembersCallback callback)
		{
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				final List<Integer> rids = new ArrayList<Integer>();
				for(DBForce.Member e : dbforce.members)
					rids.add(e.id);
				gs.getLoginManager().getDataBrief(rids, false, new LoginManager.GetDataBriefCallback() {
				
					@Override
					public void onCallback(List<RoleBriefCachePair> lst)
					{
						List<MemberBrief> members = new ArrayList<MemberBrief>();
						Map<Integer, LoginManager.RoleBriefCache> map = new TreeMap<Integer, LoginManager.RoleBriefCache>();
						for(RoleBriefCachePair p : lst)
							map.put(p.id, p.cache);
						for(int rid : rids) {
							LoginManager.RoleBriefCache c = map.get(rid);
							if( c != null )
								members.add(new MemberBrief(rid, c.name, c.headIconID, c.lvl));
						}
						if (callback != null)
							callback.onCallback(members);
					}
					
				});
				
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
		}
		
		@AutoInit
		public TableReadonly<Integer, DBForce> force;
		
		final int fid;
		final GetMembersCallback callback;
	}
	
	public void dungeonKingDeploy(final int sid, final int fid, final int rid, final byte chapter, final byte type, final short id)
	{
		synchronized (this) {
			final SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			final SBean.DBForceTreasureDeploy trd = dungeonTreasureDeploy.get(fid);
			if (tr != null && trd != null && trd.deployTimes < 2) {
				SBean.DBForceTotalTreasure tre = null;
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					if (t.type == type && t.id == id) {
						tre = t;
						break;
					}
				}
				
				if (tre != null) {
					SBean.DBForceTreasureApply apply = null;
					for (SBean.DBForceTreasureApply a : tre.apply) {
						if (rid == a.rid) {
							apply = a;
							break;
						}
					}

					final SBean.DBForceTotalTreasure trea = tre;
					
					GetMembersCallback cb = new GetMembersCallback() {

						@Override
						public void onCallback(List<MemberBrief> members) {
							MemberBrief brief = null;
							for (MemberBrief m : members)
								if (m.rid == rid)
									brief = m;
							List<SBean.DropEntryNew> rewards = null;
							
							if (brief != null && trea.count > 0) {

								List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
								attLst.add(new SBean.DropEntryNew(trea.type, trea.id, 1));
								rewards = attLst;

								trea.count --;

								if (trea.apply.size() == 0)
									trea.time = 0;

								tr.logs.add(new SBean.DBForceTreasureLog(rid, brief.name, brief.headIcon, (short)-brief.lvl, trea.type, trea.id, gs.getTime()));
								if (tr.logs.size() > 20)
									tr.logs.remove(0);

								trd.deployTimes ++;
							}
							if (rewards != null) {
								gs.getLoginManager().sysSendMessage(0, rid, DBMail.Message.SUB_TYPE_GET_TREASURE, "", "", 0, true, rewards);
								gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonKingDeployRes(fid, rid, chapter, type, id, true));
							}
							else
								gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonKingDeployRes(fid, rid, chapter, type, id, false));
						}
						
					};
					
					if (apply == null) {
						gs.getDB().execute(new GetMembersTrans(fid, cb));
					}
					else {
						trea.apply.remove(apply);
						List<MemberBrief> members = new ArrayList<MemberBrief>();
						members.add(new MemberBrief(apply.rid, apply.name, apply.headIconId, apply.lvl));
						cb.onCallback(members);
					}
				}
			}
		}
	}
	
	public void dungeonKingListApply(final int sid, final int fid, final byte chapter, final byte type, final short id)
	{
		synchronized (this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				SBean.DBForceTotalTreasure tre = null;
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					if (t.type == type && t.id == id) {
						tre = t;
						break;
					}
				}
				
				if (tre != null) {
					final SBean.DBForceTotalTreasure trea = tre;
					
					gs.getDB().execute(new GetMembersTrans(fid, new GetMembersCallback() {

						@Override
						public void onCallback(List<MemberBrief> members) {
							List<DamageRank> noApplyList = new ArrayList<DamageRank>();
							List<DamageRank> applyList = new ArrayList<DamageRank>();
							DungeonChapterDamage cdamage = dungeonDamage.get(chapter);
							Map<Integer, SBean.DBForceChapterDamage> rid2DamageMap = null;
							if (cdamage != null) 
								rid2DamageMap = cdamage.forceRid2DamageMap;
							
							Map<Integer, MemberBrief> mapMembers = new HashMap<Integer, MemberBrief>();
							for (MemberBrief m : members) 
								mapMembers.put(m.rid, m);
							
							for (SortDamage d : cdamage.sortDamageList) {
								SBean.DBForceChapterDamage da = rid2DamageMap.get(d.rid);
								MemberBrief m = mapMembers.get(d.rid);
								if (da == null || m == null)
									continue;
								
								boolean found = false;
								for (SBean.DBForceTreasureApply a : trea.apply) {
									if (m.rid == a.rid) {
										found = true;
										break;
									}
								}
								
								if (found)
									applyList.add(new DamageRank(0, m.rid, d.damage, m.headIcon, "", m.name, m.lvl, new ArrayList<SBean.DBForceChapterGeneralBrief>()));
								else
									noApplyList.add(new DamageRank(0, m.rid, d.damage, m.headIcon, "", m.name, m.lvl, new ArrayList<SBean.DBForceChapterGeneralBrief>()));
								
								mapMembers.remove(m.rid);
							}
							
							for (MemberBrief m : members) {
								if (mapMembers.get(m.rid) == null)
									continue;
								
								boolean found = false;
								for (SBean.DBForceTreasureApply a : trea.apply) {
									if (m.rid == a.rid) {
										found = true;
										break;
									}
								}
								
								if (found)
									applyList.add(new DamageRank(0, m.rid, 0, m.headIcon, "", m.name, m.lvl, new ArrayList<SBean.DBForceChapterGeneralBrief>()));
								else
									noApplyList.add(new DamageRank(0, m.rid, 0, m.headIcon, "", m.name, m.lvl, new ArrayList<SBean.DBForceChapterGeneralBrief>()));
							}
							
							gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonKingListApplyRes(fid, chapter, type, id, noApplyList, applyList));
						}
						
					}));
				}
			}
		}
	}
	
	public void dungeonListApply(final int sid, final int fid, final byte chapter, final byte type, final short id)
	{
		final List<SBean.DBForceTreasureApply> apply = new ArrayList<SBean.DBForceTreasureApply>();
		synchronized (this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					if (t.type == type && t.id == id) {
						for (SBean.DBForceTreasureApply a : t.apply) {
							apply.add(a.kdClone());
						}
						break;
					}
				}
			}
		}
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonListApplyRes(fid, chapter, type, id, apply, true));
	}
	
	private void dungeonRemoveApply(final int fid, final int rid)
	{
		synchronized (this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				for (SBean.DBForceTotalTreasure t : tr.totalTreasure) {
					SBean.DBForceTreasureApply removeApply = null;
					for (SBean.DBForceTreasureApply a : t.apply) {
						if (a.rid == rid) {
							removeApply = a;
							break;
						}
					}

					if (removeApply != null)
						t.apply.remove(removeApply);
				}
			}
		}
	}
	
	public void dungeonTreasureLog(final int sid, final int fid)
	{
		final List<SBean.DBForceTreasureLog> logs = new ArrayList<SBean.DBForceTreasureLog>();
		boolean success = false;
		synchronized (this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				for (SBean.DBForceTreasureLog log : tr.logs) {
					logs.add(log.kdClone());
				}
				success = true;
			}
		}
		
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonTreasureLogRes(fid, logs, success));
	}
	
	public void dungeonListTreasure(final int sid, final int fid, final int rid, final byte chapter)
	{
		final List<SBean.DBForceTotalTreasure> treasure = new ArrayList<SBean.DBForceTotalTreasure>();
		byte applyType = 0;
		short applyId = 0;
		int applyIndex = 0;
		synchronized (ForceManager.this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			if (tr != null) {
				SBean.DBForceTreasureList list = tr.treasure.get(chapter);
				
				for (SBean.DBForceTotalTreasure tot : tr.totalTreasure) {
					int index = 0;
					for (SBean.DBForceTreasureApply r : tot.apply) {
						if (r.rid == rid) {
							applyType = tot.type;
							applyId = tot.id;
							applyIndex = index;
							break;
						}
						index ++;
					}
					
					if (applyId > 0)
						break;
				}
				
				for (SBean.DBForceChapterTreasure t : list.list) {
					boolean found = false;
					for (SBean.DBForceTotalTreasure tot : tr.totalTreasure)
						if (t.type == tot.type && t.id == tot.id) {
							treasure.add(tot.kdClone());
							found = true;
							break;
						}
					
					if (!found)
						treasure.add(new SBean.DBForceTotalTreasure(t.type, t.id, 0, 0, new ArrayList<DBForceTreasureApply>()));
				}
			}
		}
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonListTreasureRes(fid, rid, chapter, treasure, applyType, applyId, applyIndex, true));
	}
	
	public void dungeonKingListTreasure(final int sid, final int fid, final int rid)
	{
		final Map<Byte, List<SBean.DBForceTotalTreasure>> treasures = new HashMap<Byte, List<SBean.DBForceTotalTreasure>>();
		int deployTimes = 0;
		synchronized (ForceManager.this) {
			SBean.DBForceTreasure tr = dungeonTreasure.get(fid);
			SBean.DBForceTreasureDeploy trd = dungeonTreasureDeploy.get(fid);
			if (tr != null && trd != null) {
				deployTimes = trd.deployTimes;
				byte chapter = 1;
				for (SBean.DBForceTreasureList list : tr.treasure) {
					List<SBean.DBForceTotalTreasure> treasure = new ArrayList<SBean.DBForceTotalTreasure>();
					for (SBean.DBForceChapterTreasure t : list.list) {
						for (SBean.DBForceTotalTreasure tot : tr.totalTreasure)
							if (t.type == tot.type && t.id == tot.id && tot.count > 0) {
								treasure.add(tot.kdClone());
								break;
							}
					}
					
//					List<DamageRank> ranks = getDamageRanks((byte)(chapter-1), fid, 0);
//					Set<Integer> ridSet = new HashSet<Integer>();
//					for (DamageRank r : ranks)
//						ridSet.add(r.rid);
//					for (SBean.DBForceTotalTreasure t : treasure) {
//						List<SBean.DBForceTreasureApply> apply = new ArrayList<SBean.DBForceTreasureApply>();
//						for (SBean.DBForceTreasureApply a : t.apply) {
//							if (ridSet.contains(a.rid))
//								apply.add(a);
//						}
//						t.apply = apply;
//					}
					if (treasure.size() > 0) {
						treasures.put(chapter, treasure);
					}
					chapter ++;
				}
			}
		}
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceDungeonKingListTreasureRes(fid, rid, treasures, deployTimes));
	}
	
	class DungeonChapterDamage
	{
		List<SortDamage> sortDamageList = new ArrayList<SortDamage>();
		Map<Integer, List<SortDamage>> forceSortDamageList = new HashMap<Integer, List<SortDamage>>();
		Map<Integer, SBean.DBForceChapterDamage> rid2DamageMap = new HashMap<Integer, SBean.DBForceChapterDamage>();
		Map<Integer, SBean.DBForceChapterDamage> forceRid2DamageMap = new HashMap<Integer, SBean.DBForceChapterDamage>();
	}
	
	public static class SortDamage
	{
		SortDamage(int damage, int rid)
		{
			this.rid = rid;
			this.damage = damage;
		}
		int rid;
		int damage;
	}
	
	public static class DamageRank
	{
		DamageRank(int rank, int rid, int damage, short icon, String fname, String name, short lvl, List<SBean.DBForceChapterGeneralBrief> generals)
		{
			this.rank = rank;
			this.rid = rid;
			this.damage = damage;
			this.icon = icon;
			this.fname = fname;
			this.name = name;
			this.lvl = lvl;
			this.generals = generals;
		}
		public int rank;
		public int rid;
		public int damage;
		public short icon;
		public String fname;
		public String name;
		public short lvl;
		public List<SBean.DBForceChapterGeneralBrief> generals;
	}
	
	synchronized boolean setDungeonDamage(byte chapter, int damage, int fid, int id, String fname, String name, short icon, short lvl, List<DBRoleGeneral> generals)
	{
		List<SBean.DBForceChapterGeneralBrief> briefs = new ArrayList<SBean.DBForceChapterGeneralBrief>();
		for (DBRoleGeneral g : generals) 
			briefs.add(new SBean.DBForceChapterGeneralBrief(g.id, g.lvl, g.advLvl, g.evoLvl));
		
		setForceDamage(chapter, damage, fid, id, fname, name, icon, lvl, briefs, null);
		return setDamage(chapter, damage, fid, id, fname, name, icon, lvl, briefs, null);
	}
			
	private boolean setDamage(byte chapter, int damage, int fid, int id, String fname, String name, short icon, short lvl, List<SBean.DBForceChapterGeneralBrief> generals, SBean.DBForceChapterDamage initDamage)
	{
		DungeonChapterDamage cdamage = dungeonDamage.get(chapter);
		if (cdamage == null)
			return false;
		
		List<SortDamage> sortDamageList = cdamage.sortDamageList;
		Map<Integer, SBean.DBForceChapterDamage> rid2DamageMap = cdamage.rid2DamageMap;
		
		SBean.DBForceChapterDamage d = rid2DamageMap.get(id);
		if (d != null && d.damage >= damage)
			return false;
		
		int oldSize = sortDamageList.size();
		removeDamageSortList(id, sortDamageList, rid2DamageMap);
		boolean first = insertDamageSortList(damage, id, sortDamageList, oldSize);
		
		d = rid2DamageMap.get(id);
		if (d == null) {
			if (initDamage != null) {
				rid2DamageMap.put(id, initDamage);
			}
			else {
				d = new SBean.DBForceChapterDamage(fid, id, chapter, damage, fname, name, icon, lvl, generals);
				rid2DamageMap.put(id, d);
				dungeonGlobal.damages.add(d);
			}
		}
		else {
			d.damage = damage;
			d.name = name;
			d.fid = fid;
			d.fname = fname;
			d.icon = icon;
			d.lvl = lvl;
			d.generals = generals;
		}
		
		return first;
	}
	
	private void setForceDamage(byte chapter, int damage, int fid, int id, String fname, String name, short icon, short lvl, List<SBean.DBForceChapterGeneralBrief> generals, SBean.DBForceChapterDamage initDamage)
	{
		DungeonChapterDamage cdamage = dungeonDamage.get(chapter);
		if (cdamage == null)
			return;
		
		Map<Integer, List<SortDamage>> forceSortDamageList = cdamage.forceSortDamageList;
		Map<Integer, SBean.DBForceChapterDamage> forceRid2DamageMap = cdamage.forceRid2DamageMap;
		
		SBean.DBForceChapterDamage d = forceRid2DamageMap.get(id);
		if (d != null && d.damage >= damage)
			return;
		
		List<SortDamage> fSortDamage = forceSortDamageList.get(fid);
		if (fSortDamage == null) {
			fSortDamage = new ArrayList<SortDamage>();
			forceSortDamageList.put(fid, fSortDamage);
		}
		
		removeDamageSortList(id, fSortDamage, forceRid2DamageMap);
		insertDamageSortList(damage, id, fSortDamage, 0);
		
		d = forceRid2DamageMap.get(id);
		if (d == null) {
			if (initDamage != null) {
				forceRid2DamageMap.put(id, initDamage);
			}
			else {
				d = new SBean.DBForceChapterDamage(fid, id, chapter, damage, fname, name, icon, lvl, generals);
				forceRid2DamageMap.put(id, d);
				dungeonGlobal.forceDamages.add(d);
			}
		}
		else {
			d.damage = damage;
			d.name = name;
			d.fid = fid;
			d.fname = fname;
			d.icon = icon;
			d.lvl = lvl;
			d.generals = generals;
		}
	}
	
	private boolean insertDamageSortList(int damage, int id, List<SortDamage> sortDamageList, int oldSize)
	{
		int size = sortDamageList.size();
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (damage > sortDamageList.get(mid).damage) {
				end = mid - 1;
			} else if (damage < sortDamageList.get(mid).damage) {
				begin = mid + 1;
				if (begin > end)
					mid = begin;
			} else {
				break;
			}
		}
		sortDamageList.add(mid, new SortDamage(damage, id));
		
		if (mid == 0 && oldSize > 0)
			return true;
		
		return false;
	}
	
	private void removeDamageSortList(int id, List<SortDamage> sortDamageList, Map<Integer, SBean.DBForceChapterDamage> rid2DamageMap)
	{
		SBean.DBForceChapterDamage d = rid2DamageMap.get(id);
		if (d == null)
			return;
		
		int index = getDamageSortList(d.damage, id, sortDamageList);
		if (index >= 0)
			sortDamageList.remove(index);
	}
	
	private int getDamageSortList(int damage, int id, List<SortDamage> sortDamageList)
	{
		int size = sortDamageList.size();
		if (size == 0)
			return -1;
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (damage >= sortDamageList.get(mid).damage) {
				end = mid - 1;
			} else if (damage < sortDamageList.get(mid).damage) {
				begin = mid + 1;
			}
		}
		int tmp = mid;
		if (sortDamageList.get(tmp).damage != damage)
			tmp ++;
		SortDamage p = null;
		while (tmp < size && (p = sortDamageList.get(tmp)).damage == damage) {
			if (p.rid == id) {
				return tmp;
			}
			tmp ++;
		}
		return -1;
	}
	
	synchronized List<DamageRank> getDamageRanks(byte chapter, int fid, int rid)
	{			
		List<DamageRank> ranks = new ArrayList<DamageRank>();
		DungeonChapterDamage cdamage = dungeonDamage.get(chapter);
		if (cdamage == null)
			return ranks;
		
		List<SortDamage> sdList = cdamage.sortDamageList;
		Map<Integer, List<SortDamage>> forceSortDamageList = cdamage.forceSortDamageList;
		Map<Integer, SBean.DBForceChapterDamage> rid2DamageMap = cdamage.forceRid2DamageMap;
		
		List<SortDamage> sortDamageList = forceSortDamageList.get(fid);
		if (sortDamageList == null) {
			if (fid <= 0) {
				sortDamageList = sdList;
				rid2DamageMap = cdamage.rid2DamageMap;
			}
			else
				return ranks;
		}
		
		SBean.DBForceChapterDamage d = rid2DamageMap.get(rid);
		int index = -1;
		if (d != null) {
			index = getDamageSortList(d.damage, rid, sortDamageList);
		}
		
		for (byte i = 0; i < 20; i ++) {
			if (i >= sortDamageList.size())
				break;
			SortDamage damage = sortDamageList.get(i);
			SBean.DBForceChapterDamage rrd = rid2DamageMap.get(damage.rid);
			if (rrd == null)
				continue;
			List<SBean.DBForceChapterGeneralBrief> generals = new ArrayList<SBean.DBForceChapterGeneralBrief>();
			for (SBean.DBForceChapterGeneralBrief g : rrd.generals)
				generals.add(g.kdClone());
			DamageRank rank = new DamageRank(i+1, rrd.rid, rrd.damage, rrd.icon, rrd.fname, rrd.name, rrd.lvl, generals);
			ranks.add(rank);
		}
		if (index >= 20) {
			if (d != null) {
				List<SBean.DBForceChapterGeneralBrief> generals = new ArrayList<SBean.DBForceChapterGeneralBrief>();
				for (SBean.DBForceChapterGeneralBrief g : d.generals)
					generals.add(g.kdClone());
				DamageRank rank = new DamageRank(index+1, d.rid, d.damage, d.icon, d.fname, d.name, d.lvl, generals);
				ranks.add(rank);
			}
		}
		return ranks;
	}
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBForceDungeonGlobal global)
		{
			this.global = global;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("dungeon");
			byte[] data = Stream.encodeLE(global);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("force dungeon save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBForceDungeonGlobal global;
	}
	
	synchronized public void save()
	{
		gs.getDB().execute(new SaveTrans(dungeonGlobal.kdClone()));
	}
	
	private void deployTreasure()
	{
		int now = gs.getTime();
		
		SBean.DungeonCFGS cfg = gs.gameData.getDungeonCFG();
		Map<Integer, List<SBean.DropEntryNew>> rewards = new HashMap<Integer, List<SBean.DropEntryNew>>();
		
		synchronized (this) {
			for (SBean.DBForceTreasure t : dungeonTreasure.values()) {

				List<SBean.DBForceTotalTreasure> deployObjs = new ArrayList<SBean.DBForceTotalTreasure>();
				for (SBean.DBForceTotalTreasure tr : t.totalTreasure) {
					if (tr.time > 0) {
						if (now > tr.time + cfg.basic.deployMinutes*60) {
							deployObjs.add(tr);
						}
					}
					else if (tr.count > 0 && tr.apply.size() > 0) {
						tr.time = now;
					}
				}

				for (SBean.DBForceTotalTreasure tr : deployObjs) {
					List<SBean.DBForceTreasureApply> delApply = new ArrayList<SBean.DBForceTreasureApply>();
					for (SBean.DBForceTreasureApply a : tr.apply) {
						if (tr.count > 0) {
							List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
							attLst.add(new SBean.DropEntryNew(tr.type, tr.id, 1));
							rewards.put(a.rid, attLst);

							tr.count --;
							delApply.add(a);

							t.logs.add(new SBean.DBForceTreasureLog(a.rid, a.name, a.headIconId, a.lvl, tr.type, tr.id, now));
							if (t.logs.size() > 20)
								t.logs.remove(0);
						}
						else
							break;
					}

					for (SBean.DBForceTreasureApply a : delApply)
						tr.apply.remove(a);

					if (tr.apply.size() == 0 || tr.count <= 0)
						tr.time = 0;
				}
			}
		}

		for (Map.Entry<Integer, List<SBean.DropEntryNew>> e : rewards.entrySet())
			gs.getLoginManager().sysSendMessage(0, e.getKey(), DBMail.Message.SUB_TYPE_GET_TREASURE, "", "", 0, true, e.getValue());
	}
	
	public void onTimer()
	{
		int timeTick = gs.getTime();
		if (timeTick % 10 == 4) {
			deployTreasure();
		}
		else if (timeTick % 480 == 10) {
			save();
		}
		
		List<Reward> damageRewardMails = new ArrayList<Reward>();
		
		synchronized (this) {
			if (!damageRewards.isEmpty())
				sendDamageRewards(damageRewardMails);
		}
		
		for (Reward r : damageRewardMails)
			gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_DUNGEON_DAMAGE_REWARD, "", "#" + (r.chapter+1) + "#" + r.rank + "#", 0, true, r.attLst);
	}
	
	private List<Integer> collectDamageRewards(int fid, byte chapter)
	{
		List<Integer> rewards = new ArrayList<Integer>();
		DungeonChapterDamage cdamage = dungeonDamage.get(chapter);
		if (cdamage == null)
			return rewards;
		
		Map<Integer, List<SortDamage>> forceSortDamageList = cdamage.forceSortDamageList;
		Map<Integer, SBean.DBForceChapterDamage> forceRid2DamageMap = cdamage.forceRid2DamageMap;
		if (forceSortDamageList == null || forceRid2DamageMap == null)
			return rewards;
		
		List<SortDamage> list = forceSortDamageList.get(fid);
		if (list == null)
			return rewards;
		
		for (SortDamage d : list) {
			rewards.add(d.rid);
			forceRid2DamageMap.remove(d.rid);
		}
		
		forceSortDamageList.remove(fid);
		
		Iterator<SBean.DBForceChapterDamage> it = dungeonGlobal.forceDamages.iterator();
		while (it.hasNext()) {
			SBean.DBForceChapterDamage d = it.next();
			if (d.fid == fid && d.chapter == chapter) {
				it.remove();
			}
		}
		
		return rewards;
	}
	
	private void sendDamageRewards(List<Reward> rewardMails) 
	{
		List<SBean.DungeonRewardCFGS> cfgs = gs.getGameData().getDungeonCFG().rewards;
		int i = 0;
		while (!damageRewards.isEmpty() && i < 1000) {
			RewardMembers rm = damageRewards.get(0);
			List<Integer> rids = rm.rids;
			int index = 0;
			for (int rid : rids) {
				int rank = index + 1;
				index ++;
				SBean.DungeonRewardCFGS reward = null;
				for(SBean.DungeonRewardCFGS e : cfgs)
				{
					if( rank <= e.rankFloor )
					{
						reward = e;
						break;
					}
				}
				if( reward == null )
					continue;
				if (rid < 0)
					continue;
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_FORCE_POINT, (short)0, reward.point));
				attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, reward.money));
				rewardMails.add(new Reward(rid, rm.chapter, rank, attLst));
				i ++;
			}
			damageRewards.remove(0);
		}
	}
	
	public void upgrade(int fid, int sid, Role role)
	{
		gs.getDB().execute(new ForceOPTrans(ForceOPTrans.TYPE_UPGRADE, fid, sid, role.id, null, 0, role));
	}
	
	private int calcMercTimeIncome(int time, int power)
	{
		SBean.MercCFGS cfg = gs.gameData.getMercCFG();
		int minutes = gs.getTime() - time;
		if (minutes < 0)
			minutes = 0;
		else
			minutes = minutes / 60;
		if (minutes > cfg.maxHours * 60)
			minutes = cfg.maxHours * 60;
		int partA = 0;
		if (cfg.timeGainA > 0)
			partA = power / cfg.timeGainA;
		return minutes * (partA + cfg.timeGainB);
	}
	
	public interface MercSyncSelfCallback
	{
		public void onCallback(SBean.DBRoleMerc merc);
	}
	
	public class MercSyncSelfTrans implements Transaction
	{
		public MercSyncSelfTrans(Role role, int fid, MercSyncSelfCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.merc.isEmpty())
					return false;
				
				SBean.DBMerc merc = dbforce.merc.get(0);
				for (SBean.DBRoleMerc rm : merc.roleMercs)
					if (role.id == rm.rid) {
						roleMerc = rm;
						break;
					}
				
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null) {
				callback.onCallback(roleMerc);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		SBean.DBRoleMerc roleMerc;
		final MercSyncSelfCallback callback;
	}
	
	public void mercSyncSelf(final int sid, final int fid, final Role role)
	{
		gs.getDB().execute(new MercSyncSelfTrans(role, fid, new MercSyncSelfCallback() {
			@Override
			public void onCallback(SBean.DBRoleMerc merc) {
				Map<Short, Integer> baseIncomes = new HashMap<Short, Integer>();
				if (merc != null)
					for (SBean.DBMercGeneral g : merc.generals)
						baseIncomes.put(g.general.id, calcMercTimeIncome(g.time, DBRole.calcGeneralPower(g.general)));
				List<Short> gids = role.getCitysGids();
						
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercSyncSelfRes(fid, merc, baseIncomes, gids));
			}
		}));
	}
	
	public void mercInitSync(final int sid, final int fid, Role role)
	{
		gs.getDB().execute(new MercSyncSelfTrans(role, fid, new MercSyncSelfCallback() {
			@Override
			public void onCallback(SBean.DBRoleMerc merc) {
				Map<Short, Integer> baseIncomes = new HashMap<Short, Integer>();
				if (merc != null)
					for (SBean.DBMercGeneral g : merc.generals)
						baseIncomes.put(g.general.id, calcMercTimeIncome(g.time, DBRole.calcGeneralPower(g.general)));
				
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercInitSyncRes(fid, merc, baseIncomes));
			}
		}));
	}
	
	public interface MercSyncCallback
	{
		public void onCallback(List<SBean.DBRoleMerc> merc);
	}
	
	public class MercSyncTrans implements Transaction
	{
		public MercSyncTrans(Role role, int fid, MercSyncCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.merc.isEmpty())
					return false;
				
				roleMerc = new ArrayList<SBean.DBRoleMerc>();
				SBean.DBMerc merc = dbforce.merc.get(0);
				for (SBean.DBRoleMerc rm : merc.roleMercs)
					if (role.id != rm.rid) 
						roleMerc.add(rm);
				
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null) {
				callback.onCallback(roleMerc);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		List<SBean.DBRoleMerc> roleMerc;
		final MercSyncCallback callback;
	}
	
	public void mercSync(final int sid, final int fid, final Role role)
	{
		gs.getDB().execute(new MercSyncTrans(role, fid, new MercSyncCallback() {
			@Override
			public void onCallback(List<SBean.DBRoleMerc> merc) {
				SBean.DBMarchMercGeneral g = role.getMarchGeneral();
				SBean.DBMarchMercGeneral sg = role.getSuraGeneral();
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercSyncRes(fid, role.id, gs.getDayCommon(), merc, g, sg));
			}
		}));
	}
	
	public class MercShareTrans implements Transaction
	{
		public MercShareTrans(Role role, int fid, short gid, CommonResultCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.merc.isEmpty())
					return false;
				
				SBean.DBMerc merc = dbforce.merc.get(0);
				SBean.DBRoleMerc roleMerc = null;
				for (SBean.DBRoleMerc rm : merc.roleMercs)
					if (role.id == rm.rid) {
						roleMerc = rm;
						for (SBean.DBMercGeneral g : rm.generals) {
							if (g.general.id == gid)
								return false;
						}
						break;
					}
				
				DBRoleGeneral general = null;
				synchronized (role) {
					general = role.getGeneral(gid);
				}
				if (general == null || general.lvl < gs.gameData.getMercCFG().minGeneralLevel)
					return false;
				
				SBean.DBMercGeneral mg = new SBean.DBMercGeneral(general, gs.getTime(), 0, new ArrayList<SBean.DBMercUsed>(), 0);
				if (roleMerc == null) {
					roleMerc = new SBean.DBRoleMerc(role.id, role.name, new ArrayList<SBean.DBMercGeneral>(), 0, 0);
					merc.roleMercs.add(roleMerc);
				}
				if (roleMerc.generals.size() >= gs.gameData.getMercCountMax(role.lvlVIP))
					return false;
				roleMerc.generals.add(mg);
				
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null) {
				callback.onCallback(errcode == ErrorCode.eOK, new int[]{});
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		final short gid;
		final CommonResultCallback callback;
	}
	
	public void mercShare(final int sid, final int fid, final short gid, Role role)
	{
		gs.getDB().execute(new MercShareTrans(role, fid, gid, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercShareRes(fid, gid, success));
			}
		}));
	}
	
	public class MercWithdrawTrans implements Transaction
	{
		public MercWithdrawTrans(Role role, int fid, short gid, CommonResultCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
			baseIncome = 0;
			mercIncome = 0;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.merc.isEmpty())
					return false;
				
				SBean.DBMerc merc = dbforce.merc.get(0);
				SBean.DBRoleMerc roleMerc = null;
				for (SBean.DBRoleMerc rm : merc.roleMercs)
					if (role.id == rm.rid) {
						roleMerc = rm;
						boolean found = false;
						for (SBean.DBMercGeneral g : rm.generals) {
							if (g.general.id == gid)
								found = true;
						}
						if (!found)
							return false;
						break;
					}
				
				if (roleMerc == null)
					return false;
				
				for (SBean.DBMercGeneral g : roleMerc.generals) {
					if (g.general.id == gid) {
						baseIncome = calcMercTimeIncome(g.time, DBRole.calcGeneralPower(g.general));
						mercIncome = g.mercIncome;
						g.mercIncome = 0;
						
						roleMerc.generals.remove(g);
						break;
					}
				}
				
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode == ErrorCode.eOK) {
				synchronized (role) {
					role.addMoney(baseIncome + mercIncome);
				}
			}
				
			if (callback != null) {
				int [] vals = {baseIncome, mercIncome};
				callback.onCallback(errcode == ErrorCode.eOK, vals);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		final short gid;
		int baseIncome;
		int mercIncome;
		final CommonResultCallback callback;
	}
	
	public void mercWithdraw(final int sid, final int fid, final short gid, Role role)
	{
		gs.getDB().execute(new MercWithdrawTrans(role, fid, gid, new CommonResultCallback() {
			@Override
			public void onCallback(boolean success, int[] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercWithdrawRes(fid, gid, success, values[0], values[1]));
			}
		}));
	}
	
	public interface MercHireCallback
	{
		public void onCallback(boolean success, DBRoleGeneral general, String rname);
	}
	
	public class MercHireTrans implements Transaction
	{
		public MercHireTrans(Role role, int fid, int rid, short gid, MercHireCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.rid = rid;
			this.gid = gid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.merc.isEmpty())
					return false;
				
				SBean.DBMerc merc = dbforce.merc.get(0);
				SBean.DBRoleMerc roleMerc = null;
				for (SBean.DBRoleMerc rm : merc.roleMercs)
					if (rid == rm.rid) {
						roleMerc = rm;
						boolean found = false;
						for (SBean.DBMercGeneral g : rm.generals) {
							if (g.general.id == gid) {
								found = true;
								
								for (SBean.DBMercUsed u : g.used) {
									if (role.id == u.rid) {
										if (u.day < gs.getDayCommon()) {
											g.used.remove(u);
											break;
										}
										else
											return false;
									}
								}
							}
						}
						if (!found)
							return false;
						break;
					}
				
				if (roleMerc == null)
					return false;
				
				for (SBean.DBMercGeneral g : roleMerc.generals) {
					if (g.general.id == gid) {
						SBean.MercCFGS cfg = gs.gameData.getMercCFG();
						int power = DBRole.calcGeneralPower(g.general);
						int cost = power * 3;
						synchronized (role) {
							if (role.money < cost)
								return false;
							role.useMoney(cost);
						}
						g.used.add(new SBean.DBMercUsed(gs.getDayCommon(), role.id));
						g.mercIncome += (int)(cost * cfg.hireGainA);
						general = g.general.kdClone();
						rname = roleMerc.rname;
						break;
					}
				}
				
				force.put(fid, dbforce);
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null) {
				callback.onCallback(errcode == ErrorCode.eOK, general, rname);
			}
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Role role;
		final int fid;
		final int rid;
		final short gid;
		String rname = "";
		DBRoleGeneral general;
		final MercHireCallback callback;
	}
	
	public void mercHire(final int sid, final int fid, final int rid, final short gid, Role role)
	{
		gs.getDB().execute(new MercHireTrans(role, fid, rid, gid, new MercHireCallback() {
			@Override
			public void onCallback(boolean success, DBRoleGeneral general, String rname) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercHireRes(fid, rid, gid, success));
			}
		}));
	}
	
	public void mercMarchHire(final int sid, final int fid, final int rid, final short gid, final Role role)
	{
		SBean.DBMarchMercGeneral g = role.getMarchGeneral();
		if (g != null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercMarchHireRes(fid, rid, gid, true));
			return;
		}
			
		gs.getDB().execute(new MercHireTrans(role, fid, rid, gid, new MercHireCallback() {
			@Override
			public void onCallback(boolean success, DBRoleGeneral general, String rname) {
				if (success) {
					SBean.DBMarchMercGeneral g = new SBean.DBMarchMercGeneral(rid, rname, general, new SBean.DBRoleMarchGeneralState(), (byte)0, (byte)0, (byte)0, (byte)0);
					role.hireMarchGeneral(g);
				}
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercMarchHireRes(fid, rid, gid, success));
			}
		}));
	}
	
	public void mercSuraHire(final int sid, final int fid, final int rid, final short gid, final Role role)
	{
		SBean.DBMarchMercGeneral g = role.getSuraGeneral();
		if (g != null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercMarchHireRes(fid, rid, gid, true));
			return;
		}
			
		gs.getDB().execute(new MercHireTrans(role, fid, rid, gid, new MercHireCallback() {
			@Override
			public void onCallback(boolean success, DBRoleGeneral general, String rname) {
				if (success) {
					SBean.DBMarchMercGeneral g = new SBean.DBMarchMercGeneral(rid, rname, general, new SBean.DBRoleMarchGeneralState(), (byte)1, (byte)0, (byte)0, (byte)0);
					role.hireMarchGeneral(g);
				}
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercSuraHireRes(fid, rid, gid, success));
			}
		}));
	}
	
	public void mercRemoveCityGeneral(final int sid, final int fid, final short gid, Role role)
	{
		boolean succ = role.removeCitysGid(gid);
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceMercRemoveCityGeneralRes(fid, gid, succ));
	}
	
	synchronized void addForceCache(int fid, int maxid)
	{
		if (!forceCacheSet.contains(fid)) {
			forceCacheSet.add(fid);
			forceCacheList.add(fid);
		}
		if (forceCacheSet.size() > 100) {
			int delFid = forceCacheList.get(0);
			forceCacheSet.remove(delFid);
			forceCacheList.remove(0);
		}
		if (maxid > 0)
			maxForceId = maxid;
		else
			maxForceId = fid;
	}

	synchronized Integer getRandomForce()
	{
		if (forceCacheList.size() == 0)
			return null;
		
		int index = GameData.getInstance().getRandom()
				.nextInt(forceCacheList.size());
		int fid = forceCacheList.get(index);
		return fid;
	}

	synchronized Integer getRandomForceEx()
	{
		if (maxForceId > 0)
			return gs.gameData.getRandInt(1, maxForceId+1);
		
		return null;
	}

	GameServer gs;
	
	ActiveForces actives = new ActiveForces();
	Map<Integer, ApplyCache> applyCache = new TreeMap<Integer, ApplyCache>();
	Map<Integer, Integer> activityCache = new TreeMap<Integer, Integer>();
	Map<Integer, Integer> cupCache = new TreeMap<Integer, Integer>();
	Map<Integer, ForceDungeonRTStatus> dungeonRTStatusMap = new HashMap<Integer, ForceDungeonRTStatus>();
	SBean.DBForceDungeonGlobal dungeonGlobal = null;
	Map<Byte, DungeonChapterDamage> dungeonDamage = null;
	Map<Integer, SBean.DBForceTreasure> dungeonTreasure = null;
	Map<Integer, SBean.DBForceTreasureDeploy> dungeonTreasureDeploy = null;
	List<RewardMembers> damageRewards = new ArrayList<RewardMembers>();
	Set<Integer> forceCacheSet = new TreeSet<Integer>();
	List<Integer> forceCacheList = new ArrayList<Integer>();
	int maxForceId;
	
}

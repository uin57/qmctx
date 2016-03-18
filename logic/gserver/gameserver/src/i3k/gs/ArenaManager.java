
package i3k.gs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Iterator;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;
import i3k.DBArena;
import i3k.DBMail;
import i3k.DBRole;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.ArenaRewardCFGS;
import i3k.SBean.DBGeneralEquip;
import i3k.SBean.DBPetBrief;
import i3k.DBRoleGeneral;
import i3k.gs.LoginManager.CommonRoleVisitor;
import i3k.gs.LoginManager.RoleBriefCachePair;

public class ArenaManager
{

	public static int SAVE_INTERVAL = 60 * 10;
	
	public static class ActiveAttackFinishRes
	{
		public int roleID;
		public String roleName;
		public boolean bWin;
		public int rankUp;
	}
	
	public static class ActiveAttackStartRes
	{
		public int roleID;
		public String roleName;
		public short lvl;
		public short headIconID;
		public int seed;
		public List<DBRoleGeneral> sgenerals;
		public List<DBRoleGeneral> egenerals;
		public List<DBPetBrief> pets;
		public List<DBPetBrief> epets;
		public List<SBean.DBRelation> srelation;
		public List<SBean.DBRelation> erelation;
		public List<SBean.DBGeneralStone> sgStone;
		public List<SBean.DBGeneralStone> egStone;
	}
	
	public static class CalcAttackStartReq
	{
		public int seed;
		public int rid1;
		public int rid2;
		public List<DBRoleGeneral> generals1;
		public List<DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<SBean.DBRelation> relation1;
		public List<SBean.DBRelation> relation2;
		public List<SBean.DBGeneralStone> gStone1;
		public List<SBean.DBGeneralStone> gStone2;
	}
	
	public static class Reward
	{
		public Reward(int roleId, int rank, int rankSuper, List<SBean.DropEntryNew> attLst)
		{
			this.roleId = roleId;
			this.rank = rank;
			this.rankSuper = rankSuper;
			this.attLst = attLst;
		}
		int roleId;
		int rank;
		int rankSuper;
		List<SBean.DropEntryNew> attLst;
	}
	
	public static class Fighting
	{
		public Fighting(int rid, int time)
		{
			this.rid = rid;
			this.time = time;
		}
		int rid;
		int time;
	}
	
	public class SaveTrans implements Transaction
	{	
		public SaveTrans(DBArena arena)
		{
			this.arena = arena;
		}

		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("arena");
			byte[] data = Stream.encodeLE(arena);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("arena save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		public final DBArena arena;

	}
	
	static class Ranks
	{
		public int arena;
		public int superArena = -1;
		Ranks(int arena)
		{
			this.arena = arena;
		}
		Ranks(int arena, int superArena)
		{
			this.arena = arena;
			this.superArena = superArena;
		}
	}
	public class Arena
	{
		public Arena(short stamp)
		{
			this.stamp = stamp;
			ranks = new TreeMap<Integer, Integer>();
			roles = new TreeMap<Integer, Integer>();
		}
		
		public Arena(SBean.DBArenaRank dbarena)
		{
			stamp = dbarena.stamp;
			ranks = new TreeMap<Integer, Integer>();
			roles = new TreeMap<Integer, Integer>();
			for(SBean.DBArenaRankEntry e : dbarena.ranks)
			{
				if (roles.get(e.roleID) == null)
				{
					ranks.put(e.rank, e.roleID);
					roles.put(e.roleID, e.rank);					
				}
			}
		}
		
		public Arena copy()
		{
			Arena n = new Arena(stamp);
			n.ranks.putAll(ranks);
			n.roles.putAll(roles);
			return n;
		}
		
		public List<Integer> dupTopRankRoles(int tops)
		{
			List<Integer> roles = new ArrayList<Integer>();
			int count = 0;
			for (Map.Entry<Integer, Integer> e :  ranks.entrySet())
			{
				roles.add(e.getValue());
				if (++count >= tops)
					break;
			}
			return roles;
		}
		
		public int getMaxLevel(int lvl)
		{
			if (lvl > maxLvl)
				maxLvl = lvl;
			return (maxLvl > gs.getGameData().getArenaCFG().lvlReq) ? maxLvl : gs.getGameData().getArenaCFG().lvlReq;
		}
		
		public int getMaxWinTimes(int winTimes)
		{
			if (winTimes > maxWinTimes)
				maxWinTimes = winTimes;
			return maxWinTimes;
		}
		
		public int getRoleRank(int rid)
		{
			Integer r = roles.get(rid);
			if( r == null )
				return -1;
			return r.intValue();
		}
		
		public void gmSetRank(int rid, int rank)
		{
			if( rank < 1 || rank > GameData.getInstance().getArenaCFG().rankMax )
				rank = GameData.getInstance().getArenaCFG().rankMax;
			//TODO
			int oldRID = -1;
			if( ranks.containsKey(rank) )
			{
				oldRID = ranks.get(rank).intValue();
			}
			if( rid == oldRID )
				return;
			int oldRank = -1;
			if( roles.containsKey(rid) )
			{
				oldRank = roles.get(rid).intValue();
			}
			ranks.put(rank, rid);
			roles.put(rid, rank);
			
			if( oldRank != -1 )
			{
				if( oldRID != -1 )
					ranks.put(oldRank, oldRID);
				else
					ranks.remove(oldRank);
			}
			if( oldRID != -1 )
			{
				if( oldRank != -1 )
					roles.put(oldRID, oldRank);
				else
					roles.remove(oldRID);
			}
		}
		
		public int getRankRole(int rank)
		{
			Integer id = ranks.get(rank);
			if( id == null )
				return -rank;
			return id.intValue();
		}
		
		public boolean rewardSending() {
			//return rewards != null;
			return role2rankRewards != null;
		}
		
		public void sendRewards(List<Reward> rewardMails) {
			if (rewardMails == null)
				return;
			
//			if (rewards == null) {
//				rewards = new ArrayList<Integer>(ranks.keySet());
//			}
			if (role2rankRewards == null)
			{
				role2rankRewards = new TreeMap<Integer, Ranks>();
				for (Map.Entry<Integer, Integer> e : roles.entrySet())
				{
					int rid = e.getKey();
					if (rid > 0)
					{
						role2rankRewards.put(rid, new Ranks(e.getValue()));
					}
				}
				int rankMax = gs.getGameData().getArenaCFG().rankMax;
				//role2rankRewards.putAll(roles);
				Map<Integer, Integer> superArenaRole2Rank = superArena.copyRole2Rank();
				for (Map.Entry<Integer, Integer> e : superArenaRole2Rank.entrySet())
				{
					int rid = e.getKey();
					int superarenaRank = e.getValue();
					Ranks ranks = role2rankRewards.get(rid);
					if (ranks == null)
					{
						role2rankRewards.put(rid, new Ranks(rankMax, superarenaRank));
					}
					else
					{
						ranks.superArena = superarenaRank;
					}
				}
			}
			
			List<ArenaRewardCFGS> cfgs = gs.getGameData().getArenaCFG().rewards;
			int i = 0;
			Iterator<Map.Entry<Integer, Ranks>> it = role2rankRewards.entrySet().iterator();
			while (it.hasNext() && i++ < 1000)
			{
				Map.Entry<Integer, Ranks> role2ranks = it.next();
				int rid = role2ranks.getKey();
				Ranks ranks = role2ranks.getValue();
				it.remove();
				if (rid < 0)
					continue;
				int rank = ranks.arena;
				if (ranks.superArena > 0 && ranks.superArena < rank)
					rank = ranks.superArena;
				SBean.ArenaRewardCFGS reward = null;
				for(SBean.ArenaRewardCFGS e : cfgs)
				{
					if( rank <= e.rankFloor )
					{
						reward = e;
						break;
					}
				}
				if( reward == null )
					continue;
				
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				if (reward.item1 > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, reward.item1, reward.item1Count));
				if (reward.item2 > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, reward.item2, reward.item2Count));				
				if (reward.money > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, reward.money));
				if (reward.stone > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, reward.stone));
				if (reward.point > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ARENA_POINT, (short)0, reward.point));
				
				rewardMails.add(new Reward(rid, ranks.arena, ranks.superArena, attLst));
			}
			if (role2rankRewards.isEmpty())
				role2rankRewards = null;	
			
//			while (!rewards.isEmpty() && i ++ < 1000) {
//				int rank = rewards.get(0).intValue();
//				rewards.remove(0);
//				SBean.ArenaRewardCFGS reward = null;
//				for(SBean.ArenaRewardCFGS e : cfgs)
//				{
//					if( rank <= e.rankFloor )
//					{
//						reward = e;
//						break;
//					}
//				}
//				if( reward == null )
//					continue;
//				int roleId = getRankRole(rank);
//				if (roleId < 0)
//					continue;
//				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
//				if (reward.item1 > 0)
//					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, reward.item1, reward.item1Count));
//				if (reward.item2 > 0)
//					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, reward.item2, reward.item2Count));				
//				if (reward.money > 0)
//					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, reward.money));
//				if (reward.stone > 0)
//					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, reward.stone));
//				if (reward.point > 0)
//					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ARENA_POINT, (short)0, reward.point));
//				
//				rewardMails.add(new Reward(roleId, rank, attLst));
//			}
//			
//			if (rewards.isEmpty())
//				rewards = null;				
		}
		
		SBean.DBArenaRank getDBArena()
		{
			SBean.DBArenaRank e = new SBean.DBArenaRank();
			e.stamp = stamp;
			e.ranks = new ArrayList<SBean.DBArenaRankEntry>();
			for(Map.Entry<Integer, Integer> e2 : ranks.entrySet())
			{
				e.ranks.add(new SBean.DBArenaRankEntry(e2.getKey(), e2.getValue()));
			}
			return e;
		}
		
		short stamp;
		int maxLvl = 0;
		int maxWinTimes = 0;
		Map<Integer, Integer> ranks;
		Map<Integer, Integer> roles;
		//List<Integer> rewards;
		Map<Integer, Ranks> role2rankRewards;
	}
	
	public ArenaManager(GameServer gs)
	{
		this.gs = gs;
		this.superArena = new SuperArena(gs);
	}
	
	public void init(DBArena arena)
	{
		saveTime = gs.getTime() + gs.getGameData().getRandom().nextInt(SAVE_INTERVAL);
		if( arena == null )
		{
			history = new Arena((short)0);
			current = new Arena(gs.getDayByOffset(gs.getGameData().getArenaCFG().refreshTime));
			return;
		}
		history = new Arena(arena.history);
		current = new Arena(arena.current);
		//randSecs = gs.getGameData().getRandom().nextInt(120/*TODO*/);
	}
	
	public boolean doVerify()
	{
		return gs.getConfig().arenaVerifyFlag == 1;
	}
	
	public void onTimer()
	{
		boolean bRewards = false;
		boolean bSave = false;
		short newstamp = gs.getDayByOffset(gs.getGameData().getArenaCFG().refreshTime);
		//short newstamp = gs.getDayByOffsetRandAdjust(gs.getGameData().getArenaCFG().refreshTime, randSecs);
		List<Reward> rewards = new ArrayList<Reward>();
		synchronized( this )
		{
			//System.out.println("new stamp is " + newstamp + ", old is " + current.stamp + ", cfg = " + gs.getGameData().getArenaCFG().refreshTime);
			if( newstamp > current.stamp ) // do change
			{
				gs.getLogger().info("arena ranks change");	
				history = current.copy();
				current.stamp = newstamp;
				//System.out.println("aftger copy his size =" + history.ranks.size() + ", cur size =" + current.ranks.size());
				bSave = true;
				//randSecs = gs.getGameData().getRandom().nextInt(120/*TODO*/);
				
				// Send rewards
				history.sendRewards(rewards);
				bRewards = true;
			}
			else if (history.rewardSending()) {
				history.sendRewards(rewards);
			}
		}
		
		for (Reward r : rewards)
		{
			int lastRolePowerUpdateTime = gs.getMarchManager().getPowerRoleUpdateTime(r.roleId);
			int now = gs.getTime();
			if( lastRolePowerUpdateTime < 0 || lastRolePowerUpdateTime + 5 * 86400 > now )	// whf
			{
				if(r.rank==1&&gs.isSunday()){
//					r.attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)6002, 1));
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					attLst.add(new SBean.DropEntryNew((byte) 0 ,(short)6002, 1));
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_HEAD_ICON, "", ""+(short)6002, 0, true, attLst);
				}
				if(r.rankSuper==1&&gs.isSunday()){
//					r.attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)6003, 1));
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					attLst.add(new SBean.DropEntryNew((byte) 0 ,(short)6003, 1));
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_HEAD_ICON, "", ""+(short)6003, 0, true, attLst);
				}
				gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_ARENA_REWARD, "", "#" + r.rank + "#" + r.rankSuper + "#", 0, true, r.attLst);
			}
				
		}
		
		if( ! bSave )
		{
			if( gs.getTime() > saveTime + SAVE_INTERVAL )
				bSave = true;
		}
				
		if( bSave )
		{
			saveArena();
			saveTime = gs.getTime();
		}
		
		superArena.onTimer(gs.getTime());
		
		if (bRewards)
		{
			gs.getLoginManager().getTopLevel().update();
		}
	}
	
	void saveArena()
	{
		DBArena dbarena = new DBArena();
		synchronized( this )
		{
			dbarena.current = current.getDBArena();
			dbarena.history = history.getDBArena();
		}
		gs.getDB().execute(new SaveTrans(dbarena));
	}
	
	public void save()
	{
		// TODO
		saveArena();
		superArena.save();
	}
	
	private int[] selectEnemy(int rankNow)
	{
		int rankMax = gs.getGameData().getArenaCFG().rankMax;
		Random rand = gs.getGameData().getRandom();
		if( rankNow <= 0 || rankNow > rankMax )
			rankNow = rankMax;
		List<SBean.ArenaTargetCFGS> lst = gs.getGameData().getArenaCFG().targets;
		SBean.ArenaTargetCFGS t = null;
		for(SBean.ArenaTargetCFGS e : lst)
		{
			if( rankNow <= e.rankFloor )
			{
				t = e;
				break;
			}
		}
		int[] res = null;
		if (t.rankFloor <= 10 || t.seeMax <= 0) {
			res = new int[t.deltaMin.size()];
			for(int i = 0; i < res.length; ++i)
			{
				int mind = t.deltaMin.get(i);
				int d = t.deltaMax.get(i) - mind;
				if( d <= 0)
					res[i] = rankNow+mind;
				else
					res[i] = rankNow+mind+rand.nextInt(d);
				if( res[i] > rankMax )
					res[i] = rankMax;
			}
		}
		else {
			int seeMax = t.seeMax;
			res = new int[3];
			int max = rankNow - 1;
			if (seeMax > max)
				seeMax = max;
			int min = (int)(max - (max - seeMax) * 0.2);
			int d = max - min;
			if (d == 0) d = 1;
			res[2] = min + rand.nextInt(d);
			max = min;
			min = seeMax;
			if (min > max)
				min = max;
			d = max - min;
			if (d == 0) d = 1;
			int rand1 = min + rand.nextInt(d);
			int rand2 = min + rand.nextInt(d);
			res[0] = (rand1 > rand2) ? rand2 : rand1;
			res[1] = (rand1 > rand2) ? rand1 : rand2;
		}
		
		int arenaDebugRank = gs.getConfig().arenaDebugRank;
		if (arenaDebugRank > 0)
			res[0] = arenaDebugRank;
		return res;
	}
	
	public synchronized List<Integer> getRoleInfo(int rid, Role.ArenaStateInfo info)
	{
		List<Integer> rids = new ArrayList<Integer>();

		info.rankNow = current.getRoleRank(rid);
		info.rankReward = history.getRoleRank(rid);
		info.stampReward = history.stamp;
		if( info.ranks == null )
			info.ranks = selectEnemy(info.rankNow);
		info.enemies = new ArrayList<SBean.DBRoleBrief>();
		for(int e : info.ranks)
		{
			int eid = current.getRankRole(e);
			info.enemies.add(new SBean.DBRoleBrief(eid, (byte)0, (byte)1, "", "", 0));
			if( eid > 0 )
				rids.add(eid);
		}
		attacked.remove(rid);
		return rids;
	}
	
	public synchronized void getRoleRewardInfo(int rid, Role.ArenaStateInfo info)
	{
		info.rankReward = history.getRoleRank(rid);
		info.stampReward = history.stamp;
	}
	
	public synchronized List<Integer> dupTopRankRoles(int tops)
	{
		return current.dupTopRankRoles(tops);
	}
	
	public synchronized int getRoleRank(int rid)
	{
		return current.getRoleRank(rid);
	}
	
	public synchronized int getRankRole(int rank)
	{
		return current.getRankRole(rank);
	}
	
	public synchronized void gmSetRank(int rid, int rank)
	{
		current.gmSetRank(rid, rank);
	}
	
	public synchronized int getMaxLevel(int lvl)
	{
		return current.getMaxLevel(lvl);
	}
	
	public synchronized int getMaxWinTimes(int winTimes)
	{
		return current.getMaxWinTimes(winTimes);
	}
	
	public void listRanks(final int netsid)
	{
		final List<Integer> roles = new ArrayList<Integer>();
		final List<Integer> rids = new ArrayList<Integer>();
		synchronized( this )
		{
			for(short i = 1; i <= 50; ++i)
			{
				int rid = current.getRankRole(i);
				roles.add(rid);
				if( rid > 0 )
				{
					rids.add(rid);
				}
			}
		}
		//TODO
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
				List<Role.ArenaRank> ranks = new ArrayList<Role.ArenaRank>();
				for(int rid : roles)
				{
					Role.ArenaRank ar = new Role.ArenaRank();
					ar.id = rid;
					if( rid > 0 )
					{
						SBean.DBRoleBrief b = rmap.get(rid);
						if( b == null )
						{
							ar.id = -1; // TODO
							ar.name = "";
							ar.lvl = 1;
							ar.headIconID = 1;
						}
						else
						{
							ar.lvl = b.lvl;
							ar.name = b.name;
							ar.headIconID = b.headIconID;
						}
					}
					else
					{
						ar.name = "";
						ar.lvl = 1;
						ar.headIconID = 1;
					}
					ranks.add(ar);
				}
				
				gs.getRPCManager().sendLuaPacket(netsid, LuaPacket.encodeArenaSyncRanks(ranks));
			}
		});
	}
	
	public void startAttack(final Role role, final int rank, final int erid, List<Short> generals, List<Short> pets)
	{
		int rid = 0;
		boolean bFailed = false;
		ActiveAttackStartRes res = null;
		final int now = gs.getTime();
		Fighting fi = fighting.get(erid);
		if (fi != null) {
			if (fi.time + 60 * 2 > now) {
				role.arenaStartAttackRes(rank, 2, null);
				return;
			}
			else
				fighting.remove(erid);
		}
		synchronized( this )
		{
			int rankSelf = current.getRoleRank(role.id);
			rid = current.getRankRole(rank);
			if( rank <= 0 || rid == 0 || rid == role.id || rankSelf == rank || rid != erid )
			{
				bFailed = true;
			}
			else if( rid < 0 ) // attack robot
			{
				res = new ActiveAttackStartRes();
				
				res.roleID = -rank;
				res.roleName = "Robot";
				res.lvl = 1;
				res.headIconID = 0;
				res.seed = gs.getGameData().getRandom().nextInt();
				synchronized (role) {
					res.sgenerals = role.copyGeneralsWithoutLock(generals);
					res.pets = role.copyPetsWithoutLock(pets);
					res.srelation = role.getActiveRelationsWithoutLock();
					res.sgStone = role.copyGeneralStoneWithoutLock();
				}
				fighting.put(res.roleID, new Fighting(role.id, now));
			}
		}
		if( bFailed )
		{
			role.arenaStartAttackRes(rank, (rid != erid)?1:0, null);
			return;
		}
		if( res != null ) // attack robot ok
		{
			role.arenaStartAttackRes(rank, 0, res);
			return;
		}
		if( rid <= 0 )
			return;
		final int orid = rid;
		final List<Short> sgenerals = generals;
		final List<Short> spets = pets;
		gs.getLoginManager().getPVPInfo(rid, (byte)0, false, new LoginManager.GetPVPInfoCallback() {
			
			@Override
			public void onCallback(String rname, short lvl, short headIconID, List<DBRoleGeneral> egenerals, 
					List<DBPetBrief> epets, List<SBean.DBRelation> erelation, List<SBean.DBGeneralStone> egStone)
			{
				boolean bFailed2 = false;
				ActiveAttackStartRes res2 = null;
				int nr = 0;
				synchronized( ArenaManager.this )
				{
					int rankSelf = current.getRoleRank(role.id);
					final int nrid = current.getRankRole(rank);
					nr = nrid;
					if( rank <= 0 || nrid == 0 || nrid == role.id || rankSelf == rank || nrid != orid || nrid != erid )
					{
						bFailed2 = true;
					}
					else
					{
						res2 = new ActiveAttackStartRes();
						res2.roleID = nrid;
						res2.roleName = rname;
						res2.lvl = lvl;
						res2.headIconID = headIconID;
						res2.seed = gs.getGameData().getRandom().nextInt();
						res2.egenerals = egenerals;
						res2.epets = epets;
						res2.erelation = erelation;
						res2.egStone = egStone;
						calcResults.remove(role.id);
					}
				}
				if( bFailed2 )
				{
					role.arenaStartAttackRes(rank, (nr != erid)?1:0, null);
					return;
				}
				synchronized (role) {
					res2.sgenerals = role.copyGeneralsWithoutLock(sgenerals);
					res2.pets = role.copyPetsWithoutLock(spets);
					res2.srelation = role.getActiveRelations();
					res2.sgStone = role.copyGeneralStoneWithoutLock();
				}
				if( res2 != null && res2.sgenerals != null )
				{
					fighting.put(res2.roleID, new Fighting(role.id, now));
					role.arenaStartAttackRes(rank, 0, res2);
					
					// find other player to calc result
					Role r1 = gs.getLoginManager().getOnlineRole();
					Role r2 = gs.getLoginManager().getOnlineRole();
					if (r1 != null && r2 != null) {
						r1.arenaCalcAttackResult(res2.sgenerals, res2.pets, res2.srelation,res2.sgStone, res2.egenerals, res2.epets, res2.erelation,res2.egStone, res2.seed, role.id, res2.roleID);
						r2.arenaCalcAttackResult(res2.sgenerals, res2.pets, res2.srelation,res2.sgStone, res2.egenerals, res2.epets, res2.erelation,res2.egStone, res2.seed, role.id, res2.roleID);
					}
					return;
				}
			}
		});
	}
	
	public void finishAttack(final Role role, final boolean bWin, SBean.ArenaRecord record)
	{
		int rankSelf = 0;
		int rankOppo = 0;
		int rid = 0;
		boolean bFailed = false;
		ActiveAttackFinishRes res = null;
		int recIndex = -1;
		int recTime = 0;
		SBean.DBRoleArenaLog plog = null;
		synchronized( this )
		{
			rankSelf = current.getRoleRank(role.id);
			rid = record.rid2; //current.getRankRole(rank);
			if (rid > 0)
				rankOppo = current.getRoleRank(rid);
			else {
				rankOppo = -rid;
				if (current.getRankRole(rankOppo) != rid) {
					role.arenaFinishAttackRes(rankSelf, rankOppo, null, recIndex,
							recTime);
					return;
				}
			}
			Fighting fi = fighting.get(rid);
			if (fi == null || fi.rid != role.id || fi.time + 60 * 2 < gs.getTime()) {
				role.arenaFinishAttackRes(rankSelf, rankOppo, null, recIndex,
						recTime);
				return;
			}
			fighting.remove(rid);
			if( rid == 0 || rid == role.id )
			{
				bFailed = true;
			}
			else 
			{
				if (record != null) {
					recIndex = ++recordIndex;
					recTime = record.time;
					record.index = recIndex;
					record.result = (byte)(bWin?1:0);
					records.put(recIndex, record);
					if (rankOppo == 1 && record.generals1.size() >= 5 && record.generals2.size() >= 5) {
						highRecords.addFirst(recIndex);
						while (highRecords.size() > 50)
							highRecords.removeLast();
					}
				}
				
				if( rid < 0 ) // attack robot
				{
					res = new ActiveAttackFinishRes();
					res.bWin = bWin;
					res.roleID = rid;
					res.rankUp = 0;
					if( res.bWin )
					{
						if( rankSelf <= 0 )
							res.rankUp = gs.getGameData().getArenaCFG().rankMax - rankOppo;
						else if( rankOppo < rankSelf )
							res.rankUp = rankSelf - rankOppo;
						if( res.rankUp > 0 )
						{
							current.roles.put(role.id, rankOppo);
							current.ranks.remove(new Integer(rankSelf));
							current.ranks.put(rankOppo, role.id);
						}
					}
				}
				else
				{
					res = new ActiveAttackFinishRes();
					int seed = 0;
					if (record != null)
						seed = record.seed;
					boolean bWin2 = bWin;
					if (doVerify())
						bWin2 = getCalcResult(seed, role.id, rid, bWin);
					if (bWin != bWin2) {
						if (bWin)
							res.bWin = bWin2;
						else
							res.bWin = bWin;
						try {
							File file = new File("arena_diff");
							file.mkdirs();
							String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
							ket.util.Stream.storeObjLE(record, new File("arena_diff/arena_diff_record_" + (bWin?1:0) + "_" + stamp + "_" + role.id + "_" + rid));
						} catch (IOException e) {
							e.printStackTrace();
						}
						gs.getLogger().warn("arena attack result differs!!! roleid=" + role.id + ", enemyid=" + rid + ", expect=" + bWin + ", got=" + res.bWin);
					}
					else
						res.bWin = bWin;
					res.roleID = rid;
					res.roleName = record.name2;
					res.rankUp = 0;
					if( res.bWin )
					{
						if( rankSelf <= 0 )
							res.rankUp = gs.getGameData().getArenaCFG().rankMax - rankOppo;
						else if( rankOppo < rankSelf )
							res.rankUp = rankSelf - rankOppo;
						if( res.rankUp > 0 )
						{
							current.roles.put(role.id, rankOppo);
							current.roles.put(rid, rankSelf);
							current.ranks.put(rankOppo, role.id);
							current.ranks.put(rankSelf, rid);
							attacked.add(rid);
						}
					}
					plog = new SBean.DBRoleArenaLog((byte) (res.bWin ? 0 : 1),
							-res.rankUp, recTime, role.id, recIndex);
				}
			}
		}
		if (bFailed) {
			role.arenaFinishAttackRes(rankSelf, rankOppo, null, recIndex,
					recTime);
			return;
		}
		if (rid > 0 && plog != null) {
			final SBean.DBRoleArenaLog log = plog;
			gs.getLoginManager().execCommonRoleVisitor(rid,
					new LoginManager.CommonRoleVisitor() {

						@Override
						public byte visit(DBRole dbrole) {
							if (log != null) {
								dbrole.arenaAddLog(log);
								if (log.rankUp < 0) {
									synchronized (ArenaManager.this) {
										attacked.add(dbrole.id);
									}
								}
							}
							return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
						}

						@Override
						public boolean visit(Role role) {
							if (log != null) {
								role.arenaSetPassiveAttackLog(log);
								if (log.rankUp < 0) {
									role.arenaRefresh();
									synchronized (ArenaManager.this) {
										attacked.add(role.id);
									}
									gs.getRPCManager().notifyArenaAttacked(gs.getLoginManager().getLoginRoleSessionID(role.id), true);
								}
							}
							return true;
						}

						@Override
						public void onCallback(boolean bDB, byte errcode,
								String rname) {
						}
					});
		}
		if (res != null)
		{
			record.result = (byte)(res.bWin?1:0);
			role.arenaFinishAttackRes(rankSelf, rankOppo, res, recIndex,
					recTime);
			return;
		}
		else
		{
			role.arenaFinishAttackRes(rankSelf, rankOppo, null, recIndex,
					recTime);
			return;
		}
	}
	
	public synchronized void setCalcResult(int seed, int rid1, int rid2, int result)
	{
		CalcResult res = calcResults.get(rid1);
		if (res == null || res.rid1 != rid1 || res.rid2 != rid2 || (seed != 0 && res.seed != seed))
		{
			res = new CalcResult();
			res.seed = seed;
			res.rid1 = rid1;
			res.rid2 = rid2;
			res.result1 = result;
			res.result2 = -1;
			calcResults.put(rid1, res);
		}
		else
		{
			if (res.result1 == -1)
				res.result1 = result;
			else if (res.result2 == -1)
				res.result2 = result;
		}
	}
	
	public synchronized boolean getCalcResult(int seed, int rid1, int rid2, boolean defaultResult) {
		CalcResult res = calcResults.get(rid1);
		calcResults.remove(rid1);
		if (res != null && (res.seed == 0 || res.seed == seed) && res.rid1 == rid1 && res.rid2 == rid2 && res.result1 >= 0 && res.result1 == res.result2) {
			return res.result1 > 0;
		}
		gs.getLogger().warn("arena attack use default result, may be modified!!! roleid=" + rid1 + ", enemyid=" + rid2 + ", result=" + defaultResult);
		return defaultResult;
	}
	
	public synchronized SBean.ArenaRecord getArenaRecord(int recId, int recTime) {
		SBean.ArenaRecord record = records.get(recId);
		if (record != null && record.time == recTime) 
			return record.kdClone();
		return null;
	}
	
	public synchronized boolean isAttacked(int rid)
	{
		return attacked.contains(rid);
	}
	
	public synchronized List<SBean.ArenaRecord> getHighRecords(int begin, int count, int[] total)
	{
		List<SBean.ArenaRecord> hrecords = new ArrayList<SBean.ArenaRecord>();
		
		for (int i = 0; i < count; i ++) {
			if (begin + i < highRecords.size()) {
				int index = highRecords.get(begin + i);
				SBean.ArenaRecord rec = records.get(index);
				if (rec != null)
					hrecords.add(rec);
			}
			else
				break;
		}
		total[0] = highRecords.size();
		
		return hrecords;
	}
	
	public static void main(String[] args)
	{
		if (args.length == 0)
			return;
		
		SBean.ArenaRecord record = new SBean.ArenaRecord();
		ket.util.Stream.loadObjLE(record, new File(args[0]));
		String lua = "arenaRecord={seed=" + record.seed + ",generals1={";
		for (DBRoleGeneral g : record.generals1) {
			lua += "{_id=" + g.id + ",";
			lua += "_lvl=" + g.lvl + ",";
			lua += "_advLvl=" + g.advLvl + ",";
			lua += "_evoLvl=" + g.evoLvl + ",";
			lua += "_skills={";
			for (short s : g.skills)
				lua += s + ",";
			lua += "},_equips={";
			for (DBGeneralEquip e : g.equips)
				lua += "{lvl=" + e.lvl + ", exp=" + e.exp + "},";
			lua += "},_weapon={id=" + g.id + ",lvl=" + g.weapon.lvl + ",evo=" + g.weapon.evo + ",props={";
			for (SBean.DBWeaponProp p : g.weapon.passes)
				lua += "{id=" + p.propId + ",type=" + p.type + ",value=" + p.value + "},";
			lua += "}}},";
		}
		lua += "},pet1={";
		for (DBPetBrief p : record.pets1) {
			lua += "id=" + p.id + ",";
			lua += "lvl=" + p.lvl + ",";
			lua += "evoLvl=" + p.evoLvl + ",";
			lua += "growthRate=" + p.growthRate + ",";
			lua += "deformStage=" + p.deformStage + ",";
			break;
		}
		lua += "},generals2={";
		for (DBRoleGeneral g : record.generals2) {
			lua += "{_id=" + g.id + ",";
			lua += "_lvl=" + g.lvl + ",";
			lua += "_advLvl=" + g.advLvl + ",";
			lua += "_evoLvl=" + g.evoLvl + ",";
			lua += "_skills={";
			for (short s : g.skills)
				lua += s + ",";
			lua += "}, _equips={";
			for (DBGeneralEquip e : g.equips)
				lua += "{lvl=" + e.lvl + ", exp=" + e.exp + "},";
			lua += "}, _weapon={id=" + g.id + ",lvl=" + g.weapon.lvl + ",evo=" + g.weapon.evo + ",props={";
			for (SBean.DBWeaponProp p : g.weapon.passes)
				lua += "{id=" + p.propId + ",type=" + p.type + ",value=" + p.value + "},";
			lua += "}}},";
		}
		lua += "},pet2={";
		for (DBPetBrief p : record.pets1) {
			lua += "id=" + p.id + ",";
			lua += "lvl=" + p.lvl + ",";
			lua += "evoLvl=" + p.evoLvl + ",";
			lua += "growthRate=" + p.growthRate + ",";
			lua += "deformStage=" + p.deformStage + ",";
			break;
		}
		lua += "},relation1={";
		for (SBean.DBRelation r : record.relation1) {
			lua += "{id=" + r.id + ", lvls={";
			for (short lvl : r.lvls)
				lua += lvl + ",";
			lua += "},";
		}
		lua += "},relation2={";
		for (SBean.DBRelation r : record.relation2) {
			lua += "{id=" + r.id + ", lvls={";
			for (short lvl : r.lvls)
				lua += lvl + ",";
			lua += "}},";
		}
		lua += "}};";
		try {
			FileWriter fw;
			fw = new FileWriter("arena_diff.lua");
			fw.write(lua); 
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TODO 
	static class ArenaRankData
	{
		short stamp;
		Map<Integer, Integer> rank2role = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> role2rank = new TreeMap<Integer, Integer>();
		ArenaRankData()
		{
			
		}
		
		public void setDBData(SBean.DBArenaRank dbarena)
		{
			stamp = dbarena.stamp;
			for(SBean.DBArenaRankEntry e : dbarena.ranks)
			{
				if (role2rank.get(e.roleID) == null)
				{
					rank2role.put(e.rank, e.roleID);
					role2rank.put(e.roleID, e.rank);					
				}
			}
		}
		
		public void setStamp( short stamp )
		{
			this.stamp = stamp;
		}
		
		public SBean.DBArenaRank getDBData()
		{
			SBean.DBArenaRank e = new SBean.DBArenaRank();
			e.stamp = this.stamp;
			e.ranks = new ArrayList<SBean.DBArenaRankEntry>();
			for(Map.Entry<Integer, Integer> e2 : rank2role.entrySet())
			{
				e.ranks.add(new SBean.DBArenaRankEntry(e2.getKey(), e2.getValue()));
			}
			return e;
		}
		
		public ArenaRankData copy()
		{
			ArenaRankData n = new ArenaRankData();
			n.stamp = this.stamp;
			n.rank2role.putAll(rank2role);
			n.role2rank.putAll(role2rank);
			return n;
		}
		
		public int getRoleRank(int rid)
		{
			Integer r = role2rank.get(rid);
			if( r == null )
				return -1;
			return r.intValue();
		}
		
		public int getRankRole(int rank)
		{
			Integer id = rank2role.get(rank);
			if( id == null )
				return -1;
			return id.intValue();
		}
		
		public List<Integer> getTopRanks(int rankCount)
		{
			List<Integer> roles = new ArrayList<Integer>();
			int count = rankCount > rank2role.size() ? rank2role.size() : rankCount;
			for(short i = 1; i <= count; ++i)
			{
				int rid = getRankRole(i);
				roles.add(rid);
			}
			return roles;
		}
		
		public Map<Integer, Integer> dumpRoleRanks()
		{
			Map<Integer, Integer> rs = new TreeMap<Integer, Integer>();
			rs.putAll(role2rank);
			return rs;
		}
		
		public void newRoleJoin(int rid)
		{
			if (!role2rank.containsKey(rid))
			{
				rank2role.put(rank2role.size()+1, rid);
				role2rank.put(rid, rank2role.size());	
			}
		}
		
		public RankRole[] searchEnemy(int rid)
		{
			int rankNow = getRoleRank(rid);
			if (rankNow <= 0)
				return null;
			List<SBean.SuperArenaTargetCFGS> lst = GameData.getInstance().getSuperArenaCFG().targets;
			SBean.SuperArenaTargetCFGS t = null;
			for(SBean.SuperArenaTargetCFGS e : lst)
			{
				if( rankNow <= e.rankFloor )
				{
					t = e;
					break;
				}
			}
			if (t == null)
				return null;
			
			RankRole[] res = new RankRole[t.deltaRanks.size()];
			int index = 0;
			for (SBean.SuperArenaRankCFGS rankcfg : t.deltaRanks)
			{
				int min = rankcfg.deltaMin + rankNow;
				int max = rankcfg.deltaMax + rankNow;
				if (min <= 0)
					min = 1;
				if (max <= 0)
					max = 1;
				int rand = GameData.getInstance().getRandInt(min, max+1);
				for (int i = 0; i < index; ++i)
				{
					if (res[i] != null)
					{
						
					}
				}
				RankRole rr = new RankRole();
				rr.rank = rand;
				rr.rid = getRankRole(rand);
				res[index++] = rr;
			}
			return res;
		}
	}
	
	//TODO
	public static class RankRole
	{
		public int rank;
		public int rid;
	}
	public static class AttackInfo
	{
		public int rid;
		public int attackTime;
		public AttackInfo(int rid, int time)
		{
			this.rid = rid;
			this.attackTime = time;
		}
	}
	public static class SuperArena
	{
		public class SaveSuperArenaDataTrans implements Transaction
		{
			public SaveSuperArenaDataTrans(SBean.DBSuperArena data)
			{
				this.dbdata = data;
			}

			@Override
			public boolean doTransaction()
			{
				gs.getLogger().info("save super arena data to DB...");
				byte[] key = Stream.encodeStringLE("superarena");
				byte[] data = Stream.encodeLE(dbdata);
				world.put(key, data);
				return true;
			}
		

			@Override
			public void onCallback(ErrorCode errcode)
			{
				if( errcode != ErrorCode.eOK )
				{
					gs.getLogger().warn("super arena save failed errcode="+errcode);
				}
			}
			
			@AutoInit
			public Table<byte[], byte[]> world;
			
			public final SBean.DBSuperArena dbdata;
		}
		
		
		public final static int SUPER_ARENA_SAVE_INTERVAL = 60*15;
		public final static int[][] SUPER_ARENA_FIGHT_ORDERS = 
			{
				{0, 1, 2},
				{0, 2, 1},
				{1, 0, 2},
				{1, 2, 0},
				{2, 0, 1},
				{2, 1, 0},
			};
		
		public static int getMaxTeamOrderCount()
		{
			return SUPER_ARENA_FIGHT_ORDERS.length;
		}
		
		public static boolean isTeamOrderIndexInvalid(int index)
		{
			return index >= 0 && index < SUPER_ARENA_FIGHT_ORDERS.length;
		}
		
		public static List<SBean.DBRoleArenaFightTeam> getOrderedFightTeams(List<SBean.DBRoleArenaFightTeam> teams, int order)
		{
			int orderIndex = (order) < 0 ? 0 : (order) % SUPER_ARENA_FIGHT_ORDERS.length;
			int[] orders = SUPER_ARENA_FIGHT_ORDERS[orderIndex];
			List<SBean.DBRoleArenaFightTeam> orderedTeams = new ArrayList<SBean.DBRoleArenaFightTeam>();
			for (int i = 0; i < orders.length; ++i)
			{
				SBean.DBRoleArenaFightTeam t = teams.get(orders[i]);
				orderedTeams.add(t);
			}
			return orderedTeams;
		}

		GameServer gs;
		Map<Integer, Integer> rank2role = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> role2rank = new TreeMap<Integer, Integer>();
		int lastRewardTime;
		int nextRewardTime;
		int nextRecordID;
		ConcurrentMap<Integer, SBean.SuperArenaRecord> records = new ConcurrentHashMap<Integer, SBean.SuperArenaRecord>();
		Set<Integer> attacked = new HashSet<Integer>();	
		
		ConcurrentMap<Integer, AttackInfo> fighting = new ConcurrentHashMap<Integer, AttackInfo>();
		public SuperArena(GameServer gs)
		{
			this.gs = gs; 
		}
		
		
		public synchronized void setDBData(SBean.DBSuperArena dbsuperArena)
		{
			if (dbsuperArena == null)
				return;
			for(SBean.DBArenaRankEntry e : dbsuperArena.ranks)
			{
				if (role2rank.get(e.roleID) == null)
				{
					rank2role.put(e.rank, e.roleID);
					role2rank.put(e.roleID, e.rank);					
				}
			}
			this.lastRewardTime = dbsuperArena.lastRewardTime;
		}
	
		public synchronized SBean.DBSuperArena getDBData()
		{
			SBean.DBSuperArena e = new SBean.DBSuperArena();
			e.ranks = new ArrayList<SBean.DBArenaRankEntry>();
			for(Map.Entry<Integer, Integer> e2 : rank2role.entrySet())
			{
				e.ranks.add(new SBean.DBArenaRankEntry(e2.getKey(), e2.getValue()));
			}
			e.lastRewardTime = this.lastRewardTime;
			return e;
		}
		
		public void onTimer(int timeTick)
		{
			boolean needSave = false;
			if (nextRewardTime == 0)
			{
				SBean.SuperArenaCFGS cfg = gs.getGameData().getSuperArenaCFG();
				int startTime = gs.getTimeByMinuteOffset(cfg.fightStartTime);
				int endTime = gs.getTimeByMinuteOffset(cfg.fightEndTime);
				int curOffset = (timeTick - startTime)/3600;
				int lastRoffset = (lastRewardTime - startTime)/3600;
				if (curOffset >= 0  && timeTick < endTime && curOffset > lastRoffset)
				{
					nextRewardTime = startTime + curOffset*3600 + GameData.getInstance().getRandInt(600, 3000);
					gs.getLogger().info("super arena create next reward time :" + GameServer.getTimeStampStr(nextRewardTime));
				}
			}
			else
			{
				if (timeTick > nextRewardTime)
				{
					gs.getLogger().info("super arena send reward for current time > " + GameServer.getTimeStampStr(nextRewardTime));
					this.sendRankRewards();
					nextRewardTime = 0;
					lastRewardTime = timeTick;
					needSave = true;
				}
			}
			if (needSave || timeTick % SUPER_ARENA_SAVE_INTERVAL == SUPER_ARENA_SAVE_INTERVAL/4)
			{
				save();
			}
		}
		
		public void save()
		{
			SBean.DBSuperArena data = getDBData();
			gs.getDB().execute(new SaveSuperArenaDataTrans(data));
		}
		
		public synchronized int getRoleRank(int rid)
		{
			Integer r = role2rank.get(rid);
			if( r == null )
				return -1;
			return r.intValue();
		}
		
		public synchronized int getRankRole(int rank)
		{
			Integer id = rank2role.get(rank);
			if( id == null )
				return -1;
			return id.intValue();
		}
		
		public synchronized List<Integer> getTopRanks(int rankCount)
		{
			List<Integer> roles = new ArrayList<Integer>();
			int count = rankCount > rank2role.size() ? rank2role.size() : rankCount;
			for(short i = 1; i <= count; ++i)
			{
				int rid = getRankRole(i);
				roles.add(rid);
			}
			return roles;
		}
		
		public synchronized Map<Integer, Integer> dumpRoleRanks()
		{
			Map<Integer, Integer> rs = new TreeMap<Integer, Integer>();
			rs.putAll(role2rank);
			return rs;
		}
		
		public void newRoleJoin(int rid)
		{
			boolean join = false;
			synchronized (this)
			{
				if (!role2rank.containsKey(rid))
				{
					rank2role.put(rank2role.size()+1, rid);
					role2rank.put(rid, rank2role.size());
					join = true;
				}
			}
			if (join)
				this.save();
		}
		
		public synchronized RankRole[] searchEnemy(int rid)
		{
			int rankNow = getRoleRank(rid);
			if (rankNow <= 0)
				return null;
			List<SBean.SuperArenaTargetCFGS> lst = GameData.getInstance().getSuperArenaCFG().targets;
			SBean.SuperArenaTargetCFGS t = null;
			for(SBean.SuperArenaTargetCFGS e : lst)
			{
				if( rankNow <= e.rankFloor )
				{
					t = e;
					break;
				}
			}
			if (t == null)
				return null;
			
			RankRole[] targets = new RankRole[t.deltaRanks.size()];
			int index = 0;
			for (SBean.SuperArenaRankCFGS rankcfg : t.deltaRanks)
			{
				int min = rankcfg.deltaMin + rankNow;
				int max = rankcfg.deltaMax + rankNow;
				if (min <= 0)
					min = 1;
				if (max <= 0)
					max = 1;
				int rand = GameData.getInstance().getRandInt(min, max+1);
				boolean foundEqual = false;
				for (int i = 0; i < index; ++i)
				{
					if (targets[i] != null)
					{
						if (targets[i].rank == rand)
						{	
							foundEqual = true;
							break;
						}
					}
				}
				int randrid = getRankRole(rand);
				if (!foundEqual && randrid != rid)
				{
					RankRole rr = new RankRole();
					rr.rank = rand;
					rr.rid = randrid;
					targets[index++] = rr;
				}
			}
			return targets;
		}
		
		public interface StartAttackCallback
		{
			public void onCallback(int errCode, SBean.SuperArenaFightInfo info);
		}
		public synchronized void startAttack(final int srid, final int srank, final int trid, final int trank, final StartAttackCallback callback)
		{
			final int now = gs.getTime();
			AttackInfo ainfo = fighting.get(trid);
			if (ainfo != null)
			{
				if (ainfo.attackTime + 300 <= now || ainfo.rid == srid)
				{
					fighting.remove(trid);
				}
				else
				{
					callback.onCallback(-100, null);
					return;
				}
			}
			int rankSelf = getRoleRank(srid);
			int rankTarget = getRoleRank(trid);
			if (rankTarget != trank || rankSelf != srank)
			{
				callback.onCallback(-101, null);
				return;
			}
			if (rankTarget <= 0 || rankSelf <= 0 || rankSelf == rankTarget || trid == srid)
			{
				callback.onCallback(-102, null);
				return;
			}
			gs.getLoginManager().execCommonRoleVisitor(trid, new LoginManager.CommonRoleVisitor()
				{
					SBean.SuperArenaFightInfo info;
					@Override
					public byte visit(DBRole dbrole)
					{
						info = dbrole.superArenaGetFightInfo();
						dbrole.superArenaChangeFightTeamOrder();
						return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
					}
					
					@Override
					public boolean visit(Role role) 
					{
						synchronized (role)
						{
							info = role.superArenaGetFightInfo();
							role.superArenaChangeFightTeamOrder();
						}
						return true;
					}
					
					@Override
					public void onCallback(boolean bDB, byte errcode, String rname) 
					{
						int error = errcode != 0 ? -101 : 0;
						if (info == null)
							error = -101;
						if (error == 0)
							fighting.put(trid, new AttackInfo(srid, now));
						callback.onCallback(error, info);
					}
				});
		}
		
		public interface FinishAttackCallback
		{
			public void onCallback(int errCode, byte win, int rankUp);
		}
		public synchronized void finishAttack(final SBean.SuperArenaRecord record, final FinishAttackCallback callback)
		{
			AttackInfo ainfo = fighting.get(record.fteam2.rid);
			if (ainfo == null || ainfo.rid != record.fteam1.rid || ainfo.attackTime + 300 < gs.getTime())
			{
				callback.onCallback(-100, (byte)0, 0);
				return;
			}
			fighting.remove(record.fteam2.rid);
			
			int curSelfRank = getRoleRank(record.fteam1.rid);
			int curTargetRank = getRoleRank(record.fteam2.rid);
			
			int winOverRound = 0;
			for (int e : record.result)
			{
				winOverRound += e;
			}
			int rankUp = winOverRound > 0 ? curSelfRank - curTargetRank : 0;
			if (rankUp < 0)
				rankUp = 0;
			if (rankUp > 0)
			{
				role2rank.put(record.fteam1.rid, curTargetRank);
				rank2role.put(curTargetRank, record.fteam1.rid);
				
				role2rank.put(record.fteam2.rid, curSelfRank);
				rank2role.put(curSelfRank, record.fteam2.rid);
			}
			record.id = ++nextRecordID;
			records.put(record.id, record);
			final SBean.DBRoleArenaLog log = new SBean.DBRoleArenaLog((byte)(winOverRound > 0 ? 0 : 1), -rankUp, record.time, record.fteam1.rid, record.id);
			callback.onCallback(0, (byte)(winOverRound > 0 ? 1 : 0), rankUp);
			gs.getLoginManager().execCommonRoleVisitor(record.fteam2.rid, new LoginManager.CommonRoleVisitor() 
				{

						@Override
						public byte visit(DBRole dbrole) 
						{
							if (log != null) 
							{
								dbrole.superArenaAddLog(log);
								//dbrole.superArenaChangeFightTeamOrder();
								if (log.rankUp < 0) 
								{
									synchronized (SuperArena.this) 
									{
										attacked.add(dbrole.id);
									}
								}
							}
							return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
						}

						@Override
						public boolean visit(Role role)
						{
							if (log != null) 
							{
								role.superArenaAddLog(log);
								//role.superArenaChangeFightTeamOrder();
								if (log.rankUp < 0) 
								{
									synchronized (SuperArena.this) 
									{
										attacked.add(role.id);
									}
									gs.getRPCManager().notifySuperArenaAttacked(gs.getLoginManager().getLoginRoleSessionID(role.id), true);
								}
							}
							return true;
						}

						@Override
						public void onCallback(boolean bDB, byte errcode,String rname) 
						{
							if (errcode != CommonRoleVisitor.ERR_OK)
							{
								gs.getLogger().warn("add role " + record.fteam2.rid + " super arena log failed ! errcode="+errcode);
							}
						}
					});
		}
		
		public synchronized boolean isAttacked(int rid)
		{
			return attacked.contains(rid);
		}
		
		public synchronized void removeAttacked(int rid)
		{
			attacked.remove(rid);
		}
		
		public synchronized SBean.SuperArenaRecord getRecord(int recId, int recTime) 
		{
			SBean.SuperArenaRecord record = records.get(recId);
			if (record != null && record.time == recTime) 
				return record.kdClone();
			return null;
		}
		
		public synchronized Map<Integer, Integer> copyRole2Rank()
		{
			Map<Integer, Integer> c = new TreeMap<Integer, Integer>();
			c.putAll(role2rank);
			return c;
		}
		
		public synchronized void sendRankRewards()
		{
			SBean.SuperArenaCFGS cfg = GameData.getInstance().getSuperArenaCFG();
			Iterator<SBean.SuperArenaRewardCFGS> it = cfg.rewards.iterator();
			SBean.SuperArenaRewardCFGS rcfg = it.hasNext() ? it.next() : null;
			if (rcfg == null)
				return;
			for (Map.Entry<Integer, Integer> e : rank2role.entrySet())
			{
				final int rank = e.getKey();
				final int rid = e.getValue();
				int reward = 0;
				if (rank <= rcfg.rankFloor)
				{
					reward = rcfg.point;
				}
				else
				{
					while (it.hasNext())
					{
						rcfg = it.next();
						if (rank <= rcfg.rankFloor)
						{
							reward = rcfg.point;
							break;
						}
					}
					if (reward == 0 && !it.hasNext())
					{
						break;
					}
				}
				if (reward > 0)
				{
					final int rewardPoint = reward;
					gs.getLoginManager().execCommonRoleVisitor(rid, new LoginManager.CommonRoleVisitor() 
						{
							
							@Override
							public byte visit(DBRole dbrole) 
							{
								dbrole.superArenaAddRankReward(rewardPoint);
								return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
							}
							
							@Override
							public boolean visit(Role role) 
							{
								role.superArenaAddRankReward(rewardPoint);
								return true;
							}
							
							@Override
							public void onCallback(boolean bDB, byte errcode, String rname) 
							{
								if (errcode != CommonRoleVisitor.ERR_OK)
								{
									gs.getLogger().warn("send role " + rid + " super arena reward " + rewardPoint + " point failed ! errcode="+errcode);
								}
							}
						});
				}
			}
		}
	}
	

	
	SuperArena getSuperArena()
	{
		return superArena;
	}
	
	//int randSecs;
	int saveTime;
	Arena current;
	Arena history;
	GameServer gs;
	
	SuperArena superArena;
	
	class CalcResult
	{
		public int seed;
		public int rid1;
		public int rid2;
		public int result1;
		public int result2;
	}
	ConcurrentMap<Integer, CalcResult> calcResults = new ConcurrentHashMap<Integer, CalcResult>();
	int recordIndex = 0;
	ConcurrentMap<Integer, SBean.ArenaRecord> records = new ConcurrentHashMap<Integer, SBean.ArenaRecord>();	
	Set<Integer> attacked = new HashSet<Integer>();	
	ConcurrentMap<Integer, Fighting> fighting = new ConcurrentHashMap<Integer, Fighting>();	
	LinkedList<Integer> highRecords = new LinkedList<Integer>();
	
}

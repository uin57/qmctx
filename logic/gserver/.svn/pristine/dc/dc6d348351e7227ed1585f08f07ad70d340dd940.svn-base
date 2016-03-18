package i3k.gs;

import i3k.DBMail;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.DuelTopRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class DuelManager {
	
	public static short defaultLevel = 10;
	public static short defaultScore = 0;
	
	public static int SAVE_INTERVAL = 29 * 60;
	private final short RESET_TIME = 1290;
	
	public static final byte DUEL_TYPE_2_WINS = 1;
	public static final byte DUEL_TYPE_3_WINS = 2;
	public static final byte DUEL_TYPE_4_WINS = 3;
	
	public static class RTMatchData
	{
		RTMatchData(int rid, SBean.GlobalRoleID oppoRid, String selfName, String selfServerName, short selfHeadIconId, short selfLevel, String oppoName, String oppoServerName, short oppoHeadIconId, short oppoLevel, int matchId, byte type, boolean first, int time) {
			this.rid = rid;
			this.oppoRid = oppoRid;
			this.matchId = matchId;
			this.combatType = type;
			this.first = first;
			this.selfServerName = selfServerName;
			this.oppoServerName = oppoServerName;
			this.record = new SBean.DBDuelRecord(rid, selfName, oppoName, selfHeadIconId, oppoHeadIconId, selfLevel, oppoLevel, (byte)(first?1:0), (byte)0, 0, new ArrayList<SBean.DBDuelMatchRecord>(), 0);
			this.joinTime = time;
			this.lastMySelection = !first;
			this.quit = (byte)0;
		}
		public int rid;
		public SBean.GlobalRoleID oppoRid;
		public int matchId;
		public boolean first;
		public byte combatType = 0;
		public byte combatIndex = 0;
		public List<DBRoleGeneral> selfGenerals = new ArrayList<DBRoleGeneral>();
		public List<DBRoleGeneral> oppoGenerals = new ArrayList<DBRoleGeneral>();
		public SBean.DBPetBrief selfPet = null;
		public SBean.DBPetBrief oppoPet = null;
		public List<SBean.DBRelation> selfRelations = new ArrayList<SBean.DBRelation>();
		public List<SBean.DBRelation> oppoRelations = new ArrayList<SBean.DBRelation>();
		public Set<Short> selfGids = new HashSet<Short>();
		public Set<Short> selfPids = new HashSet<Short>();
		public List<Boolean> results = new ArrayList<Boolean>();
		public SBean.DBDuelRecord record = null;
		public String selfServerName = "";
		public String oppoServerName = "";
		public int joinTime = 0;
		public int countDownTime = 0;
		public int expectTime = 0;
		public boolean lastMySelection = false;
		public boolean afterFinishAttack = true;
		public int lastSelectionTime = 0;
		public byte quit = 0;
	}
	
	public static class SortScore
	{
		SortScore(int score, int rid)
		{
			this.rid = rid;
			this.score = score;
		}
		int rid;
		int score;
	}
	
	public static class ScoreRank
	{
		ScoreRank(int rank, int rid, int score, short icon, String name, short lvl)
		{
			this.rank = rank;
			this.rid = rid;
			this.score = score;
			this.icon = icon;
			this.name = name;
			this.lvl = lvl;
		}
		public int rank;
		public int rid;
		public int score;
		public short icon;
		public String name;
		public short lvl;
	}
	
	public static class Reward
	{
		public Reward(int roleId, int rank, List<SBean.DropEntryNew> attLst)
		{
			this.roleId = roleId;
			this.rank = rank;
			this.attLst = attLst;
		}
		int roleId;
		int rank;
		List<SBean.DropEntryNew> attLst;
	}
	
	static class EventService 
	{
		public EventService(GameServer gs) 
		{
			this.gs = gs;
		}

		public int getNextID() 
		{
			return eventID.incrementAndGet();
		}

		public void onTaskTimer() 
		{
			List<EventTask> timeoutTasks = removeAndGetTimeoutTasks(System.currentTimeMillis());
			for (EventTask task : timeoutTasks) 
			{
				task.onCallback(task.makeDefaultResponse(INVOKE_TIMEOUT));
				gs.getLogger().debug("duel event task timeout remove callback");
			}
		}

		private synchronized void addEventTask(EventTask task) 
		{
			tasks.put(task.getID(), task);
		}

		private synchronized EventTask removeAndGetTask(int id) 
		{
			return tasks.remove(id);
		}

		private synchronized List<EventTask> removeAndGetTimeoutTasks(long now) 
		{
			List<EventTask> timeoutTasks = new ArrayList<EventTask>();
			Iterator<Map.Entry<Integer, EventTask>> it = tasks.entrySet().iterator();
			while (it.hasNext()) 
			{
				EventTask si = it.next().getValue();
				if (si.isTooOld(now)) 
				{
					timeoutTasks.add(si);
					it.remove();
				}
			}
			return timeoutTasks;
		}

		public void excuteEventTaskWaitResponse(EventTask task) 
		{
			addEventTask(task);
			task.doTask();
		}
		
		public void excuteEventTask(EventTask task) 
		{
			task.doTask();
		}

		private void onCallback(int id, Object obj) 
		{
			EventTask task = removeAndGetTask(id);
			if (task != null) 
			{
				task.onCallback(obj);
			} 
			else 
			{
				gs.getLogger().warn("duel event task callback id=" + id + "==>but timeout");
			}
		}

		GameServer gs;
		AtomicInteger eventID = new AtomicInteger();
		Map<Integer, EventTask> tasks = new HashMap<Integer, EventTask>();
	}

	abstract class EventTask
	{
		long callTime;

		public EventTask() 
		{
			this.callTime = System.currentTimeMillis();
		}

		public boolean isTooOld(long now) 
		{
			return callTime + MAX_WAIT_TIME <= now;
		}

		abstract public int getID();
		abstract public void setID(int id);
		abstract public void doTask();
		abstract public void onCallback(Object res);
		abstract public Object makeDefaultResponse(int error);
	}

	public void excuteEventTask(EventTask task) 
	{
		service.excuteEventTask(task);
	}
	
	public void excuteEventTaskWaitResponse(EventTask task)
	{
		service.excuteEventTaskWaitResponse(task);
	}

	public void handleEventTaskResult(int id, Object obj) 
	{
		service.onCallback(id, obj);
	}

	public SBean.DuelSelectGeneralRes handleSelectGeneralForward(SBean.DuelSelectGeneralReq req)
	{
		RTMatchData matchData = matchMap.get(req.oppoRoleID.roleID);
		if (matchData == null) 
			return null;
		
		if (matchData.oppoRid.serverID != req.roleID.serverID || matchData.oppoRid.roleID != req.roleID.roleID)
			return null;
		
		if (matchData.oppoPet == null && req.pet.size() != 1) 
			return null;
		
		if (matchData.oppoPet == null) {
			matchData.oppoPet = req.pet.get(0);
			
			for (SBean.DBRelation r : req.relation)
				matchData.oppoRelations.add(r);
		}
		
		if (matchData.oppoGenerals.size() + req.generals.size() > 5)
			return null;

		for (DBRoleGeneral general : req.generals) {
			for (DBRoleGeneral g : matchData.oppoGenerals)
				if (g.id == general.id)
					return null;
		}

		for (DBRoleGeneral g : req.generals)
			matchData.oppoGenerals.add(g);
		
		int roleId = req.oppoRoleID.roleID;
		Role role = gs.getLoginManager().getRole(roleId);
		if (role != null && !role.isNull()) 
		{
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelSelectGeneralForward(matchData));
		}
		else
			return null;
		matchData.countDownTime = gs.getTime();
		matchData.expectTime = 0;
		matchData.lastMySelection = false;
		matchData.lastSelectionTime = gs.getTime();
		RPCManager.SessionInfo sinfo = gs.getRPCManager().getSessionInfo(role.netsid);
		if (sinfo == null && matchData.selfGenerals.size() == 5 && matchData.selfPet != null && matchData.oppoGenerals.size() == 5 && matchData.oppoPet != null) {
            startAttack(new SBean.DuelStartAttackReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, matchData.combatIndex, matchData.matchId));
        }
		return new SBean.DuelSelectGeneralRes(req.id, req.roleID, INVOKE_SUCCESS, req.matchId);
	}
	
	public SBean.DuelStartAttackRes handleStartAttackForward(SBean.DuelStartAttackReq req)
	{
		RTMatchData matchData = matchMap.get(req.oppoRoleID.roleID);
		if (matchData == null) 
			return null;
		
		if (matchData.oppoRid.serverID != req.roleID.serverID || matchData.oppoRid.roleID != req.roleID.roleID)
			return null;
		
		if (matchData.selfGenerals.size() < 5 || matchData.selfPet == null ||
				matchData.oppoGenerals.size() < 5 || matchData.oppoPet == null)
			return null;
		
		if (req.index != matchData.combatIndex)
			return null;
		
		int roleId = req.oppoRoleID.roleID;
		Role role = gs.getLoginManager().getRole(roleId);
		if (role != null && !role.isNull()) 
		{
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelStartAttackRes(new SBean.DuelStartAttackRes(0, req.oppoRoleID, req.index, INVOKE_SUCCESS, req.matchId), matchData));
		}
		else
			return null;

		matchData.lastMySelection = true;
		matchData.afterFinishAttack = false;
		matchData.countDownTime = gs.getTime();
		matchData.expectTime = 120;
		return new SBean.DuelStartAttackRes(req.id, req.roleID, req.index, INVOKE_SUCCESS, req.matchId);
	}
	
	public SBean.DuelRanksRes handleRanksRequest()
	{
		List<SBean.DuelRank> ranks = getAllScoreRanks();
		return new SBean.DuelRanksRes(gs.getConfig().id, ranks);
	}
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBDuel duel)
		{
			this.duel = duel;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("duel");
			byte[] data = Stream.encodeLE(duel);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("duel save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBDuel duel;
	}
	
	public DuelManager(GameServer gs)
	{
		this.gs = gs;
		this.service = new EventService(gs);
	}
	
	public void init(SBean.DBDuel duel)
	{
		this.duel = duel;
		rolesMap.clear();
		if (duel != null) {
			for (SBean.DBDuelRole r : duel.roles)
				rolesMap.put(r.rid, r);
			
			for (SBean.DBDuelRank r : duel.ranks) {
				rid2ScoreMap.put(r.rid, r);
				setScore(r.score, r.rid, r.rname, r.serverName, r.headIconId, r.lvl);
			}
			
			//initRecords();
		}
		if (duel == null)
			this.duel = new SBean.DBDuel(new ArrayList<SBean.DBDuelRole>(), new ArrayList<SBean.DBDuelRank>(), new ArrayList<SBean.DBDuelRecord>(), gs.getDayCommon(), 0, 0);
		saveTime = gs.getTime();
	}
	
	private synchronized void collectRewards()
	{
		rewardRoles = new LinkedList<SBean.DBDuelRole>();
		for (SBean.DBDuelRole r : duel.roles) {
			rewardRoles.add(r);
		}
	}
	
	private synchronized boolean rewardSending()
	{
		return rewardRoles != null;
	}
	
	private void resetDuelRole(SBean.DBDuelRole dr)
	{
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		if (dr.lvl > 0 && dr.lvl <= 10) {
			int index = 10 - dr.lvl;
			short newLvl = cfg.resetLevels.get(index);
			dr.lvl = newLvl;
		}
		else if (dr.lvl >= 100) {
			short newLvl = cfg.resetLevels.get(10);
			dr.lvl = newLvl;
		}
		
		if (dr.lvl >= 100)
			dr.score = 10000;
		else {
			int newScore = 0;
			short lvl = 10;
			for (short needStar : cfg.upgrade) {
				if (lvl > 0 && lvl >= dr.lvl)
					newScore = needStar;
				else
					break;
				lvl --;
			}
			dr.score = newScore;
		}
		dr.rank = 0;
	}
	
	public synchronized void sendRewards(List<Reward> rewardMails) {
		if (rewardMails == null)
			return;
		
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		
		int i = 0;
		while (!rewardRoles.isEmpty() && i ++ < 1000) {
			SBean.DBDuelRole dr = rewardRoles.get(0);
			rewardRoles.remove(0);
			
			SBean.DuelRewardCFGS reward = null;
			int rank = 0;
			if (dr.lvl > 0 && dr.lvl <= 10) {
				int index = 10 - dr.lvl;
				reward = cfg.rewards.get(index);
				rank = -dr.lvl;
			}
			else if (dr.lvl >= 100) {
				if (dr.rank <= 0)
					reward = cfg.rewards.get(10);
				else if (dr.rank > 50)
					reward = cfg.rewards.get(10);
				else if (dr.rank > 10)
					reward = cfg.rewards.get(11);
				else if (dr.rank > 1)
					reward = cfg.rewards.get(12);
				else 
					reward = cfg.rewards.get(13);
				rank = dr.rank;
			}
			if( reward == null )
				continue;
			int roleId = dr.rid;
			if (roleId < 0)
				continue;
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			for (SBean.DropEntryNew drop : reward.drops)
				attLst.add(drop.kdClone());
			if (reward.point > 0)
				attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_DUEL_POINT, (short)0, reward.point));
			
			rewardMails.add(new Reward(roleId, rank, attLst));
			
			resetDuelRole(dr);
		}
		
		if (rewardRoles.isEmpty())
			rewardRoles = null;				
	}
	
	public void onTimer()
	{
		int day = gs.getDayByOffset(gs.gameData.getDuelCFG().resetTime);
		List<Reward> rewards = new ArrayList<Reward>();
		if (duel.day < day && gs.getWeekday(day) == 0) {
			duel.day = day;
			
			duel.ranks.clear();
			sortScoreList.clear();
			rid2ScoreMap.clear();
			collectRewards();
		}
		else if (rewardSending()) {
			sendRewards(rewards);
		}

		for (Reward r : rewards){
			if(r.rank==1&&gs.getWeekday(day) == 0){//gs.isSunday()
//				r.attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)6001, 1));
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				attLst.add(new SBean.DropEntryNew((byte) 0 ,(short)6001, 1));
				gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_HEAD_ICON, "", ""+(short)6001, 0, true, attLst);
			}
			gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_DUEL_REWARD, "", "" + r.rank, 0, true, r.attLst);
		}
		
		for (RTMatchData match : matchMap.values()) {
            autoSelectGeneral(match);
        }
		
		int time = gs.getTime();
		if( time > saveTime + SAVE_INTERVAL ) {
			save();
			saveTime = time;
		}
		service.onTaskTimer();
	}
	
	public synchronized void save()
	{
		gs.getDB().execute(new SaveTrans(duel.kdClone()));
	}
	
	public synchronized SBean.DBDuelRole sync(int rid)
	{
		SBean.DBDuelRole dr = rolesMap.get(rid);
		if (dr == null) {
			dr = new SBean.DBDuelRole(rid, defaultLevel, defaultScore, 0, (short)0, (short)0, 0);
			rolesMap.put(rid, dr);
			duel.roles.add(dr);
		}
		
		return dr.kdClone();
	}
	
	public synchronized void join(int sid, byte type, String serverName, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 0));
			return;
		}
		
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		short lvlReq = 0;
		if (dr.lvl < 100)
			lvlReq = cfg.winStars.get(type-1).lvlMin;
		else
			lvlReq = cfg.legendWinStars.get(type-1).lvlMin;
		synchronized (role) {
			switch (type) {
			case DUEL_TYPE_2_WINS:
				if (role.lvl < lvlReq || role.generals.size() < 3*5 || role.pets.size() < 3) {
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 0));
					return;
				}
				break;
			case DUEL_TYPE_3_WINS:
				if (role.lvl < lvlReq || role.generals.size() < 5*5 || role.pets.size() < 5) {
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 0));
					return;
				}
				break;
			case DUEL_TYPE_4_WINS:
				if (role.lvl < lvlReq || role.generals.size() < 7*5 || role.pets.size() < 7) {
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 0));
					return;
				}
				break;
			}
		}
		
		RTMatchData matchData = matchMap.get(role.id);
		if (matchData != null && matchData.quit == (byte)1) {
		    matchData = null;
		}
		if (matchData != null && gs.getTime() < matchData.countDownTime + 120) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 1));
			return;
		}
		
		if (role.getDuelFightTimes() >= cfg.playTimes + role.getDuelBuyTimes()) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelJoinRes(null, 0));
			return;
		}
		
		byte winTimes = 0;
		short lastResults = dr.lastResults;
		for (int i = 0; i < 10; i ++) {
			if ((lastResults & 1) == 1)
				winTimes ++;
			lastResults >>= 1;
		}
		join(new SBean.DuelJoinReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), role.name, serverName, role.headIconID, type, dr.lvl, winTimes, gs.getTime()));
	}
	
	public synchronized void cancelJoin(byte type, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null)
			return;
		
		cancelJoin(new SBean.DuelCancelJoinReq(new SBean.GlobalRoleID(gs.getConfig().id, role.id), type));
	}
	
	public synchronized void selectGeneral(int sid, Role role, List<Short> gids)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
			return;
		}
		
		RTMatchData matchData = matchMap.get(role.id);
		if (matchData == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 1, null));
			return;
		}
		
		if (gids.size() == 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
			return;
		}
		
		List<SBean.DBPetBrief> pet = new ArrayList<SBean.DBPetBrief>();
		List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
		List<SBean.DBRelation> relations = new ArrayList<SBean.DBRelation>();
		List<Short> petGid = new ArrayList<Short>();
		petGid.add(gids.get(0));
		if (matchData.selfPet == null) {
			synchronized (role) {
				pet = role.copyPetsWithoutLock(petGid);
				relations = role.getActiveRelations();
			}
			
			if (pet == null || pet.size() == 0) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
				return;
			}
			
			matchData.selfPet = pet.get(0);
			matchData.selfPet.lvl = 100;
			matchData.selfPids.add(Short.valueOf(matchData.selfPet.id));
			for (SBean.DBRelation r : relations)
				matchData.selfRelations.add(r);
		}
		gids.remove(0);
		
		if (gids.size() > 0)
		{
			List<Short> newGids = new ArrayList<Short>();
			for (short gid : gids) {
				if (matchData.selfGids.contains(gid)) {
					gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
					return;
				}
				
				boolean found = false;
				for (DBRoleGeneral g : matchData.selfGenerals)
					if (g.id == gid) {
						found = true;
					}
				
				if (!found)
					newGids.add(gid);
			}
			gids = newGids;
			
			if (matchData.selfGenerals.size() + gids.size() > 5) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
				return;
			}

			synchronized (role) {
				generals = role.copyGeneralsWithoutLock(gids);
			}

			if (generals == null || generals.size() != gids.size()) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
				return;
			}
			
			for (DBRoleGeneral g : generals) {
				g.lvl = 100;
				g.advLvl = 14;
				for (int i = 0; i < g.skills.size(); i ++) {
					if (i < gs.gameData.generalsCmn.skills.size()) {
						SBean.GeneralSkillCFGS scfg = gs.gameData.generalsCmn.skills.get(i);
						short lvlMax = (short)(g.lvl - scfg.maxLvl);
						if( lvlMax < 1 )
							lvlMax = 1;
						g.skills.set(i, lvlMax);
					}
				}
				matchData.selfGenerals.add(g);
			}
		}
		matchData.lastSelectionTime = gs.getTime();
		selectGeneral(new SBean.DuelSelectGeneralReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, generals, pet, relations, matchData.matchId));
	}
	
	public synchronized void startAttack(int sid, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelStartAttackRes(null, null));
			return;
		}
		
		RTMatchData matchData = matchMap.get(role.id);
		if (matchData == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelStartAttackRes(null, null));
			return;
		}
		
		if (matchData.selfGenerals.size() < 5 || matchData.selfPet == null ||
				matchData.oppoGenerals.size() < 5 || matchData.oppoPet == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelStartAttackRes(null, null));
			return;
		}
		
		startAttack(new SBean.DuelStartAttackReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, matchData.combatIndex, matchData.matchId));
	}
	
	public synchronized void finishAttack(int sid, Role role, boolean win, int timeSpan)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
			return;
		}
		
		RTMatchData matchData = matchMap.get(role.id);
		if (matchData == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
			return;
		}
		
		int timeJoin = gs.getTime() - matchData.joinTime;
		if (timeJoin < timeSpan - 3) {
		    finishAttack(new SBean.DuelFinishAttackReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, matchData.combatIndex, (byte)-1, matchData.matchId));
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
			return;
		}
		
		matchData.afterFinishAttack = true;
		finishAttack(new SBean.DuelFinishAttackReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, matchData.combatIndex, (byte)(win?1:0), matchData.matchId));
	}
	
	public synchronized void rank(int sid, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelRankRes(null));
			return;
		}
		
		rank(new SBean.DuelRankReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id)));
	}
	
	public synchronized void topRanks(int sid, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelTopRanksRes(null));
			return;
		}
		
		topRanks(new SBean.DuelTopRanksReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id)));
	}
	
	public synchronized void oppoGiveup(int sid, int timeSpan, Role role)
	{
		SBean.DBDuelRole dr = rolesMap.get(role.id);
		if (dr == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
			return;
		}
		
		RTMatchData matchData = matchMap.get(role.id);
		if (matchData == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
			return;
		}
		
		if (!matchData.lastMySelection || !matchData.afterFinishAttack) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
			return;
		}
		
		int timeCountDown = gs.getTime() - matchData.countDownTime;
		int timeJoin = gs.getTime() - matchData.joinTime;
		if (timeCountDown > matchData.expectTime || timeJoin < timeSpan - 3) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
			return;
		}
		
		oppoGiveup(new SBean.DuelOppoGiveupReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), matchData.oppoRid, matchData.matchId));
	}
	
	public synchronized void finishAttackOnOppoDownLine(int sid, Role role, Byte attackResult) 
	{
	    SBean.DBDuelRole dr = rolesMap.get(role.id);
        if (dr == null) {
            gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
            return;
        }
        
        RTMatchData matchData = matchMap.get(role.id);
        if (matchData == null) {
            gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
            return;
        }
        
        if (!matchData.lastMySelection || !matchData.afterFinishAttack) {
            gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
            return;
        }
        
	    finishAttackOnOppoDownLine(new SBean.DuelFinishAttackOnOppoDownLineReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id) , matchData.oppoRid, matchData.matchId, attackResult));
	}
	
	public synchronized byte quit(Role role) 
	{
	    SBean.DBDuelRole dr = rolesMap.get(role.id);
        if (dr == null) {
            return (byte) 1;
        }
        RTMatchData matchData = matchMap.get(role.id);
        if (matchData != null) {
            matchData.quit = (byte)1;
        }
        return 0;
	}
	
	class JoinTask extends EventTask 
	{
		SBean.DuelJoinReq req;

		JoinTask(SBean.DuelJoinReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelJoinReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelJoinRes res = (SBean.DuelJoinRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				SBean.DBDuelRole dr = rolesMap.get(role.id);
				if (dr == null) {
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelJoinRes(null, 0));
					return;
				}
				
				if (res.error == INVOKE_TIMEOUT)
					cancelJoin(res.type, role);
				
				if (res.error != INVOKE_SUCCESS) {
					if (matchMap.get(roleId) != null)
						return;
				}
				else {
					RTMatchData matchData = new RTMatchData(roleId, res.oppoRoleID, role.name, req.serverName, role.headIconID, dr.lvl, res.oppoRoleName, res.oppoServerName, res.oppoHeadIconId, res.oppoLevel, res.matchId, res.type, res.first>0, gs.getTime());
					matchData.countDownTime = gs.getTime();
					matchData.expectTime = 45;
					matchData.lastSelectionTime = gs.getTime();
					matchMap.put(roleId, matchData);
					gs.getLogger().warn("duel match created, rid=" + roleId + ", opposerver=" + matchData.oppoRid.serverID + ", opporid=" + matchData.oppoRid.roleID);
					
					role.duelJoin(dr.lvl);
				}
				
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelJoinRes(res, 0));
				
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelJoinRes(req.id, req.roleID, req.type, error, 0, (byte)0, new SBean.GlobalRoleID(), "", "", (short)0, (short)0);
		}
		
		public boolean isTooOld(long now) 
		{
			return callTime + 15000 <= now;
		}
	}

	public void join(SBean.DuelJoinReq req) 
	{
		excuteEventTaskWaitResponse(new JoinTask(req));
	}

	public void cancelJoin(SBean.DuelCancelJoinReq req) 
	{
		gs.getRPCManager().exchangeSendDuelCancelJoinReq(req);
	}

	class SelectGeneralTask extends EventTask 
	{
		SBean.DuelSelectGeneralReq req;

		SelectGeneralTask(SBean.DuelSelectGeneralReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelSelectGeneralReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelSelectGeneralRes res = (SBean.DuelSelectGeneralRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				RTMatchData matchData = matchMap.get(roleId);
				if (matchData == null) {
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelSelectGeneralRes(null, 0, null));
					return;
				}
				
				matchData.countDownTime = gs.getTime();
				matchData.expectTime = 40;
				matchData.lastMySelection = true;
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelSelectGeneralRes(res, 0, matchData));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelSelectGeneralRes(req.id, req.roleID, error, req.matchId);
		}
	}

	public void selectGeneral(SBean.DuelSelectGeneralReq req) 
	{
		excuteEventTaskWaitResponse(new SelectGeneralTask(req));
	}

	class StartAttackTask extends EventTask 
	{
		SBean.DuelStartAttackReq req;

		StartAttackTask(SBean.DuelStartAttackReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelStartAttackReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelStartAttackRes res = (SBean.DuelStartAttackRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				RTMatchData matchData = matchMap.get(roleId);
				if (matchData == null) {
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelStartAttackRes(null, null));
					return;
				}
				
				matchData.countDownTime = gs.getTime();
				matchData.expectTime = 120;
				matchData.lastMySelection = true;
				matchData.afterFinishAttack = false;
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelStartAttackRes(res, matchData));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelStartAttackRes(req.id, req.roleID, req.index, error, req.matchId);
		}
	}

	public void startAttack(SBean.DuelStartAttackReq req) 
	{
		excuteEventTaskWaitResponse(new StartAttackTask(req));
	}
	
	private void upgradeByStars(byte combatType, SBean.DuelStarsCFGS scfg, SBean.DBDuelRole dr, String name, String serverName, short headIconId, short rlvl)
	{
		SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
		
		short star = 0;
		switch (combatType) {
		case DUEL_TYPE_2_WINS:
			star = scfg.win2Stars;
			break;
		case DUEL_TYPE_3_WINS:
			star = scfg.win3Stars;
			break;
		case DUEL_TYPE_4_WINS:
			star = scfg.win4Stars;
			break;
		}

		if (star == 0 || (star < 0 && dr.lvl <= 10 && dr.lvl >= 5)) {
			// No star reduction under level 5
		}
		else if (dr.lvl > 0 && dr.lvl <= 10) {
			dr.score += star;
			if (dr.score < 0)
				dr.score = 0;
			short lvl = 10;
			for (short needStar : cfg.upgrade) {
				if (dr.score >= needStar && needStar > 0)
					lvl --;
			}
			dr.lvl = lvl;
			
			if (dr.lvl <= 0) {
				dr.lvl = 100;
				dr.score = 10000;
				setScore(dr.score, dr.rid, name, serverName, headIconId, rlvl);
			}
		}
		else if (dr.lvl >= 100)	{
			dr.score += star;
			setScore(dr.score, dr.rid, name, serverName, headIconId, rlvl);
		}
	}

	public static boolean isReverse(boolean first, byte index)
	{
		switch (index)
		{
		case 0:
		case 3:
		case 4:
			return first;
		}
		
		switch (index)
		{
		case 1:
		case 2:
		case 5:
		case 6:
			return !first;
		}
		
		return first;
	}
	
	class FinishAttackTask extends EventTask 
	{
		SBean.DuelFinishAttackReq req;

		FinishAttackTask(SBean.DuelFinishAttackReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelFinishAttackReq(req);
		}
		
		public boolean isTooOld(long now) 
		{
			return callTime + 10000 <= now;
		}

		public void onCallback(Object obj) 
		{
			SBean.DuelFinishAttackRes res = (SBean.DuelFinishAttackRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			int rewardPoints = 0;
			if (role != null && !role.isNull()) 
			{
				RTMatchData matchData = matchMap.get(roleId);
				if (matchData == null) {
					// Could be cancelled by OppoGiveup
					//gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null));
					return;
				}
				
				if (matchData.oppoRid.serverID != res.oppoRoleID.serverID || matchData.oppoRid.roleID != res.oppoRoleID.roleID) {
					return;
				}
				if (res.error == INVOKE_TIMEOUT) {
				    return;
				}
				if (res.error == INVOKE_OPPO_SPEEDUP) {
				    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(res, (byte)0, null, 0));
                    return;
				}
				
				SBean.DBDuelRole dr = null;
				if (res.error == INVOKE_SUCCESS) {
					int winCount = 0;
					switch (matchData.combatType) {
					case DUEL_TYPE_2_WINS:
						winCount = 2;
						break;
					case DUEL_TYPE_3_WINS:
						winCount = 3;
						break;
					case DUEL_TYPE_4_WINS:
						winCount = 4;
						break;
					}
					
					if (winCount == 0) {
						gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
						return;
					}
					
					if (matchData.selfGenerals.size() < 5 || matchData.oppoGenerals.size() < 5 || matchData.selfPet == null || matchData.oppoPet == null) {
						gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
						return;
					}
					
					matchData.results.add(res.result>0);
					matchData.combatIndex ++;
					boolean first = isReverse(matchData.first, matchData.combatIndex);
					matchData.lastMySelection = !first;
					matchData.afterFinishAttack = true;
					matchData.lastSelectionTime = gs.getTime();
					
					List<SBean.DBPetBrief> pets1 = new ArrayList<SBean.DBPetBrief>();
					pets1.add(matchData.selfPet);
					List<SBean.DBPetBrief> pets2 = new ArrayList<SBean.DBPetBrief>();
					pets2.add(matchData.oppoPet);
					SBean.DBDuelMatchRecord mr = new SBean.DBDuelMatchRecord(matchData.selfGenerals, matchData.oppoGenerals, pets1, pets2, matchData.selfRelations, matchData.oppoRelations, 0, res.result, 0);
					matchData.record.matches.add(mr);
					matchData.record.time = gs.getTime();
					matchData.countDownTime = gs.getTime();
					matchData.expectTime = 45;
					
					for (DBRoleGeneral g : matchData.selfGenerals)
						matchData.selfGids.add(g.id);
					
					matchData.selfPet = null;
					matchData.selfGenerals = new ArrayList<DBRoleGeneral>();
					matchData.selfRelations = new ArrayList<SBean.DBRelation>();
					matchData.oppoPet = null;
					matchData.oppoGenerals = new ArrayList<DBRoleGeneral>();
					matchData.oppoRelations = new ArrayList<SBean.DBRelation>();
					
					int win = 0, lose = 0;
					for (boolean r : matchData.results) {
						if (r)
							win ++;
						else
							lose ++;
					}
					
					// TODO: Temp
					//winCount = 1;
					
					if (win >= winCount || lose >= winCount) {
						synchronized (DuelManager.this) {
							SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
							dr = rolesMap.get(role.id);
							if (dr == null) {
								gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
								return;
							}

							List<SBean.DuelStarsCFGS> starsCfg = null;
							if (dr.lvl < 100) {
								if (win >= winCount)
									starsCfg = cfg.winStars;
								else
									starsCfg = cfg.loseStars;
							}
							else {
								if (win >= winCount)
									starsCfg = cfg.legendWinStars;
								else
									starsCfg = cfg.legendLoseStars;
							}

							if (starsCfg == null) {
								gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
								return;
							}

							SBean.DuelStarsCFGS scfg = null;
							for (SBean.DuelStarsCFGS s : starsCfg) {
								if (role.lvl >= s.lvlMin)
									scfg = s;
							}
							if (scfg == null) {
								gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
								return;
							}

							upgradeByStars(matchData.combatType, scfg, dr, role.name, matchData.selfServerName, role.headIconID, role.lvl);
							
							if (win >= winCount) {
								matchData.record.result = (byte)1;
								dr.lastResults <<= 1;
								dr.lastResults |= 1;
							}
							else {
								matchData.record.result = (byte)0;
								dr.lastResults <<= 1;
							}
							
							gs.getLogger().warn("duel finish match, rid=" + roleId + ", opposerver=" + matchData.oppoRid.serverID + ", opporid=" + matchData.oppoRid.roleID + ", result=" + matchData.record.result);
							addRecord(matchData.record);
							matchMap.remove(roleId);
						}
						
						rewardPoints = role.duelFinishReward(dr.lvl);
					}
				}
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(res, matchData.combatType, dr, rewardPoints));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, error);
		}
	}

	public void finishAttack(SBean.DuelFinishAttackReq req) 
	{
		excuteEventTaskWaitResponse(new FinishAttackTask(req));
	}

	class OppoGiveupTask extends EventTask 
	{
		SBean.DuelOppoGiveupReq req;

		OppoGiveupTask(SBean.DuelOppoGiveupReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelOppoGiveupReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelOppoGiveupRes res = (SBean.DuelOppoGiveupRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			int rewardPoints = 0;
			if (role != null && !role.isNull()) 
			{
				RTMatchData matchData = matchMap.get(roleId);
				if (matchData == null) {
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
					return;
				}
				
				SBean.DBDuelRole dr = null;
				if (res.error == INVOKE_JOIN_LEAVE) {
					matchMap.remove(roleId);
					res.error = INVOKE_SUCCESS;
					gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(res, matchData.combatType, null, 0));
				}
				else if (res.error == INVOKE_SUCCESS) {
					synchronized (DuelManager.this) {
						SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
						dr = rolesMap.get(role.id);
						if (dr == null) {
							gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
							return;
						}

						List<SBean.DuelStarsCFGS> starsCfg = null;
						if (dr.lvl < 100)
							starsCfg = cfg.winStars;
						else
							starsCfg = cfg.legendWinStars;

						if (starsCfg == null) {
							gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
							return;
						}

						SBean.DuelStarsCFGS scfg = null;
						for (SBean.DuelStarsCFGS s : starsCfg) {
							if (role.lvl >= s.lvlMin)
								scfg = s;
						}
						if (scfg == null) {
							gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(null, (byte)0, null, 0));
							return;
						}

						upgradeByStars(matchData.combatType, scfg, dr, role.name, matchData.selfServerName, role.headIconID, role.lvl);

						dr.lastResults <<= 1;
						dr.lastResults |= 1;
						
						gs.getLogger().warn("duel oppo giveup, rid=" + roleId + ", opposerver=" + matchData.oppoRid.serverID + ", opporid=" + matchData.oppoRid.roleID);
						if (true) { //matchData.record.matches.size() > 0) {
							matchData.record.result = (byte)1;
							addRecord(matchData.record);
						}
						matchMap.remove(roleId);
					}
					
					rewardPoints = role.duelFinishReward(dr.lvl);
				}
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelOppoGiveupRes(res, matchData.combatType, dr, rewardPoints));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelOppoGiveupRes(req.id, req.roleID, req.oppoRoleID, error);
		}
	}

	public void oppoGiveup(SBean.DuelOppoGiveupReq req) 
	{
		excuteEventTaskWaitResponse(new OppoGiveupTask(req));
	}
	
	public void handleOppoGiveupForward(SBean.DuelOppoGiveupRes res)
	{
		int roleId = res.oppoRoleID.roleID;
		Role role = gs.getLoginManager().getRole(roleId);
		if (role != null && !role.isNull()) 
		{
			RTMatchData matchData = matchMap.get(roleId);
			if (matchData == null) 
				return;

			if (matchData.oppoRid.serverID != res.roleID.serverID || matchData.oppoRid.roleID != res.roleID.roleID)
				return;
			
			SBean.DBDuelRole dr = null;
			if (res.error == INVOKE_SUCCESS) {
				synchronized (this) {
					SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
					dr = rolesMap.get(roleId);
					if (dr == null) 
						return;

					List<SBean.DuelStarsCFGS> starsCfg = null;
					if (dr.lvl < 100)
						starsCfg = cfg.loseStars;
					else
						starsCfg = cfg.legendLoseStars;

					if (starsCfg == null)
						return;

					SBean.DuelStarsCFGS scfg = null;
					for (SBean.DuelStarsCFGS s : starsCfg) {
						if (role.lvl >= s.lvlMin)
							scfg = s;
					}
					if (scfg == null) 
						return;

					upgradeByStars(matchData.combatType, scfg, dr, role.name, matchData.selfServerName, role.headIconID, role.lvl);
					
					dr.lastResults <<= 1;

					gs.getLogger().warn("duel self giveup, rid=" + roleId + ", opposerver=" + matchData.oppoRid.serverID + ", opporid=" + matchData.oppoRid.roleID);
					if (true) { //matchData.record.matches.size() > 0) {
						matchData.record.result = (byte)0;
						addRecord(matchData.record);
					}
					matchMap.remove(roleId);
				}
			}
		}
	}

	class FinishAttackOnOppoDownLineTask extends EventTask 
    {
        SBean.DuelFinishAttackOnOppoDownLineReq req;

        FinishAttackOnOppoDownLineTask(SBean.DuelFinishAttackOnOppoDownLineReq req) 
        {
            this.req = req;
            setID(service.getNextID());
        }
        
        public int getID()
        {
            return req.id;
        }
        
        public void setID(int id)
        {
            req.id = id;
        }
        
        public void doTask() 
        {
            gs.getRPCManager().exchangeSendDuelFinishAttackOnOppoDownLineReq(req);
        }
        
        public void onCallback(Object obj) 
        {
            SBean.DuelFinishAttackOnOppoDownLineRes res = (SBean.DuelFinishAttackOnOppoDownLineRes) obj;
            int roleId = res.roleID.roleID;
            Role role = gs.getLoginManager().getRole(roleId);
            int rewardPoints = 0;
            if (role != null && !role.isNull()) 
            {
                RTMatchData matchData = matchMap.get(roleId);
                if (matchData == null) {
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                    return;
                }
                
                if (matchData.oppoRid.serverID != res.oppoRoleID.serverID || matchData.oppoRid.roleID != res.oppoRoleID.roleID) {
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                    return;
                }
                
                SBean.DBDuelRole dr = null;
                if (res.error != INVOKE_SUCCESS) {
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(new SBean.DuelFinishAttackRes(1, res.roleID, res.oppoRoleID, (byte)(matchData.combatIndex + 1), (byte)1, res.error), matchData.combatType, dr, rewardPoints));
                    return;
                }
                
                int winCount = 0;
                switch (matchData.combatType) {
                case DUEL_TYPE_2_WINS:
                    winCount = 2;
                    break;
                case DUEL_TYPE_3_WINS:
                    winCount = 3;
                    break;
                case DUEL_TYPE_4_WINS:
                    winCount = 4;
                    break;
                }
                
                if (winCount == 0) {
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                    return;
                }
                
                if (matchData.selfGenerals.size() < 5 || matchData.oppoGenerals.size() < 5 || matchData.selfPet == null || matchData.oppoPet == null) {
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                    gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                    return;
                }
                matchData.results.add(res.attackResult > 0);
                matchData.combatIndex ++;
                boolean first = isReverse(matchData.first, matchData.combatIndex);
                matchData.lastMySelection = !first;
                matchData.afterFinishAttack = true;
                matchData.lastSelectionTime = gs.getTime();
                
                List<SBean.DBPetBrief> pets1 = new ArrayList<SBean.DBPetBrief>();
                pets1.add(matchData.selfPet);
                List<SBean.DBPetBrief> pets2 = new ArrayList<SBean.DBPetBrief>();
                pets2.add(matchData.oppoPet);
                SBean.DBDuelMatchRecord mr = new SBean.DBDuelMatchRecord(matchData.selfGenerals, matchData.oppoGenerals, pets1, pets2, matchData.selfRelations, matchData.oppoRelations, 0, res.attackResult, 0);
                matchData.record.matches.add(mr);
                matchData.record.time = gs.getTime();
                matchData.countDownTime = gs.getTime();
                matchData.expectTime = 45;
                
                for (DBRoleGeneral g : matchData.selfGenerals)
                    matchData.selfGids.add(g.id);
                
                matchData.selfPet = null;
                matchData.selfGenerals = new ArrayList<DBRoleGeneral>();
                matchData.selfRelations = new ArrayList<SBean.DBRelation>();
                matchData.oppoPet = null;
                matchData.oppoGenerals = new ArrayList<DBRoleGeneral>();
                matchData.oppoRelations = new ArrayList<SBean.DBRelation>();
                
                int win = 0, lose = 0;
                for (boolean r : matchData.results) {
                    if (r)
                        win ++;
                    else
                        lose ++;
                }
                
                if (win >= winCount || lose >= winCount) {
                    synchronized (DuelManager.this) {
                        SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
                        dr = rolesMap.get(role.id);
                        if (dr == null) {
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                            return;
                        }
                        
                        List<SBean.DuelStarsCFGS> starsCfg = null;
                        if (dr.lvl < 100) {
                            if (win >= winCount)
                                starsCfg = cfg.winStars;
                            else
                                starsCfg = cfg.loseStars;
                        }
                        else {
                            if (win >= winCount)
                                starsCfg = cfg.legendWinStars;
                            else
                                starsCfg = cfg.legendLoseStars;
                        }
                        
                        if (starsCfg == null) {
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                            return;
                        }
                        
                        SBean.DuelStarsCFGS scfg = null;
                        for (SBean.DuelStarsCFGS s : starsCfg) {
                            if (role.lvl >= s.lvlMin)
                                scfg = s;
                        }
                        if (scfg == null) {
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
                            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                            return;
                        }
                        
                        upgradeByStars(matchData.combatType, scfg, dr, role.name, matchData.selfServerName, role.headIconID, role.lvl);
                        
                        if (win >= winCount) {
                            matchData.record.result = (byte)1;
                            dr.lastResults <<= 1;
                            dr.lastResults |= 1;
                        }
                        else {
                            matchData.record.result = (byte)0;
                            dr.lastResults <<= 1;
                        }
                        addRecord(matchData.record);
                        matchMap.remove(roleId);
                    }
                    rewardPoints = role.duelFinishReward(dr.lvl);
                }
                gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(1));
                gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(new SBean.DuelFinishAttackRes(1, res.roleID, res.oppoRoleID, (byte)(matchData.combatIndex + 1), res.attackResult, res.error), matchData.combatType, dr, rewardPoints));
                return;
            }
            gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackOnOppoDownLine(0));
        }
        
        public Object makeDefaultResponse(int error)
        {
            return new SBean.DuelFinishAttackOnOppoDownLineRes(req.id, req.roleID, req.oppoRoleID, (byte)0, error);
        }
    }

    public void finishAttackOnOppoDownLine(SBean.DuelFinishAttackOnOppoDownLineReq req) 
    {
        excuteEventTaskWaitResponse(new FinishAttackOnOppoDownLineTask(req));
    }
    
    public void handleFinishAttackOnOppoDownLineForward(SBean.DuelFinishAttackOnOppoDownLineRes res)
    {
        int roleId = res.oppoRoleID.roleID;
        Role role = gs.getLoginManager().getRole(roleId);
        if (role != null && !role.isNull()) 
        {
            RTMatchData matchData = matchMap.get(roleId);
            if (matchData == null) 
                return;

            if (matchData.oppoRid.serverID != res.roleID.serverID || matchData.oppoRid.roleID != res.roleID.roleID)
                return;
            if (res.error != INVOKE_SUCCESS) {
                return;
            }
            SBean.DBDuelRole dr = null;
            int winCount = 0;
            switch (matchData.combatType) {
            case DUEL_TYPE_2_WINS:
                winCount = 2;
                break;
            case DUEL_TYPE_3_WINS:
                winCount = 3;
                break;
            case DUEL_TYPE_4_WINS:
                winCount = 4;
                break;
            }
            
            if (winCount == 0) {
                gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                return;
            }
            
            if (matchData.selfGenerals.size() < 5 || matchData.oppoGenerals.size() < 5 || matchData.selfPet == null || matchData.oppoPet == null) {
                gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                return;
            }
            matchData.results.add(res.attackResult > 0);
            matchData.combatIndex ++;
            boolean first = isReverse(matchData.first, matchData.combatIndex);
            matchData.lastMySelection = !first;
            matchData.afterFinishAttack = true;
            matchData.lastSelectionTime = gs.getTime();
            
            List<SBean.DBPetBrief> pets1 = new ArrayList<SBean.DBPetBrief>();
            pets1.add(matchData.selfPet);
            List<SBean.DBPetBrief> pets2 = new ArrayList<SBean.DBPetBrief>();
            pets2.add(matchData.oppoPet);
            SBean.DBDuelMatchRecord mr = new SBean.DBDuelMatchRecord(matchData.selfGenerals, matchData.oppoGenerals, pets1, pets2, matchData.selfRelations, matchData.oppoRelations, 0, res.attackResult, 0);
            matchData.record.matches.add(mr);
            matchData.record.time = gs.getTime();
            matchData.countDownTime = gs.getTime();
            matchData.expectTime = 45;
            
            for (DBRoleGeneral g : matchData.selfGenerals)
                matchData.selfGids.add(g.id);
            
            matchData.selfPet = null;
            matchData.selfGenerals = new ArrayList<DBRoleGeneral>();
            matchData.selfRelations = new ArrayList<SBean.DBRelation>();
            matchData.oppoPet = null;
            matchData.oppoGenerals = new ArrayList<DBRoleGeneral>();
            matchData.oppoRelations = new ArrayList<SBean.DBRelation>();
            
            int win = 0, lose = 0;
            for (boolean r : matchData.results) {
                if (r)
                    win ++;
                else
                    lose ++;
            }
            
            if (win >= winCount || lose >= winCount) {
                synchronized (DuelManager.this) {
                    SBean.DuelCFGS cfg = gs.gameData.getDuelCFG();
                    dr = rolesMap.get(role.id);
                    if (dr == null) {
                        gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                        return;
                    }
                    
                    List<SBean.DuelStarsCFGS> starsCfg = null;
                    if (dr.lvl < 100) {
                        if (win >= winCount)
                            starsCfg = cfg.winStars;
                        else
                            starsCfg = cfg.loseStars;
                    }
                    else {
                        if (win >= winCount)
                            starsCfg = cfg.legendWinStars;
                        else
                            starsCfg = cfg.legendLoseStars;
                    }
                    
                    if (starsCfg == null) {
                        gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                        return;
                    }
                    
                    SBean.DuelStarsCFGS scfg = null;
                    for (SBean.DuelStarsCFGS s : starsCfg) {
                        if (role.lvl >= s.lvlMin)
                            scfg = s;
                    }
                    if (scfg == null) {
                        gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelFinishAttackRes(null, (byte)0, null, 0));
                        return;
                    }
                    
                    upgradeByStars(matchData.combatType, scfg, dr, role.name, matchData.selfServerName, role.headIconID, role.lvl);
                    
                    if (win >= winCount) {
                        matchData.record.result = (byte)1;
                        dr.lastResults <<= 1;
                        dr.lastResults |= 1;
                    }
                    else {
                        matchData.record.result = (byte)0;
                        dr.lastResults <<= 1;
                    }
                    addRecord(matchData.record);
                    matchMap.remove(roleId);
                }
                role.duelFinishReward(dr.lvl);
            }
        }
    }
	
	
	class RankTask extends EventTask 
	{
		SBean.DuelRankReq req;

		RankTask(SBean.DuelRankReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelRankReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelRankRes res = (SBean.DuelRankRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				synchronized (DuelManager.this) {
					SBean.DBDuelRole dr = rolesMap.get(role.id);
					if (dr != null) 
						dr.rank = res.rank;
				}
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelRankRes(res));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelRankRes(req.id, req.roleID, 0, error);
		}
	}

	public void rank(SBean.DuelRankReq req) 
	{
		excuteEventTaskWaitResponse(new RankTask(req));
	}

	class TopRanksTask extends EventTask 
	{
		SBean.DuelTopRanksReq req;

		TopRanksTask(SBean.DuelTopRanksReq req) 
		{
			this.req = req;
			setID(service.getNextID());
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendDuelTopRanksReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.DuelTopRanksRes res = (SBean.DuelTopRanksRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeDuelTopRanksRes(res));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.DuelTopRanksRes(req.id, req.roleID, new ArrayList<DuelTopRank>(), error);
		}
	}

	public void topRanks(SBean.DuelTopRanksReq req) 
	{
		excuteEventTaskWaitResponse(new TopRanksTask(req));
	}

	synchronized void setScore(int score, int id, String name, String serverName, short icon, short lvl)
	{
		removeScoreSortList(id);
		insertScoreSortList(score, id);
		
		SBean.DBDuelRank d = rid2ScoreMap.get(id);
		if (d == null) {
			d = new SBean.DBDuelRank(serverName, id, name, icon, lvl, score);
			rid2ScoreMap.put(id, d);
			duel.ranks.add(d);
		}
		else {
			d.serverName = serverName;
			d.score = score;
			d.rname = name;
			d.headIconId = icon;
			d.lvl = lvl;
		}
	}
	
	private void insertScoreSortList(int score, int id)
	{
		int size = sortScoreList.size();
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (score > sortScoreList.get(mid).score) {
				end = mid - 1;
			} else if (score < sortScoreList.get(mid).score) {
				begin = mid + 1;
				if (begin > end)
					mid = begin;
			} else {
				break;
			}
		}
		sortScoreList.add(mid, new SortScore(score, id));
	}
	
	private void removeScoreSortList(int id)
	{
		SBean.DBDuelRank d = rid2ScoreMap.get(id);
		if (d == null)
			return;
		int index = getScoreSortList(d.score, id);
		if (index >= 0)
			sortScoreList.remove(index);
	}
	
	private int getScoreSortList(int score, int id)
	{
		int size = sortScoreList.size();
		if (size == 0)
			return -1;
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (score >= sortScoreList.get(mid).score) {
				end = mid - 1;
			} else if (score < sortScoreList.get(mid).score) {
				begin = mid + 1;
			}
		}
		int tmp = mid;
		if (sortScoreList.get(tmp).score != score)
			tmp ++;
		SortScore p = null;
		while (tmp < size && (p = sortScoreList.get(tmp)).score == score) {
			if (p.rid == id) {
				return tmp;
			}
			tmp ++;
		}
		return -1;
	}
	
	synchronized List<SBean.DuelRank> getAllScoreRanks()
	{			
		List<SBean.DuelRank> ranks = new ArrayList<SBean.DuelRank>();
		for (SortScore s : sortScoreList) {
			SBean.DBDuelRank dr = rid2ScoreMap.get(s.rid);
			if (dr == null)
				continue;
			
			ranks.add(new SBean.DuelRank(0, dr.serverName, new SBean.GlobalRoleID(gs.getConfig().id, dr.rid), dr.rname, dr.headIconId, dr.score));
		}
		
		return ranks;
	}
	
	synchronized List<ScoreRank> getScoreRanks(int rid)
	{			
		SBean.DBDuelRank d = rid2ScoreMap.get(rid);
		int index = -1;
		if (d != null) {
			index = getScoreSortList(d.score, rid);
		}
		
		List<ScoreRank> ranks = new ArrayList<ScoreRank>();
		for (byte i = 0; i < 32; i ++) {
			if (i >= sortScoreList.size())
				break;
			SortScore score = sortScoreList.get(i);
			SBean.DBDuelRank dr = rid2ScoreMap.get(score.rid);
			if (dr == null)
				continue;
			ScoreRank rank = new ScoreRank(i+1, dr.rid, dr.score, dr.headIconId, dr.rname, dr.lvl);
			ranks.add(rank);
		}
		if (index >= 32) {
			if (d != null) {
				ScoreRank rank = new ScoreRank(index+1, d.rid, d.score, d.headIconId, d.rname, d.lvl);
				ranks.add(rank);
			}
		}
		return ranks;
	}
	
	/*
	private void initRecords()
	{
		for (SBean.DBDuelRecord r : duel.records) {
			List<SBean.DBDuelRecord> records = rid2RecordMap.get(r.rid);
			if (records == null) {
				records = new LinkedList<SBean.DBDuelRecord>();
				rid2RecordMap.put(r.rid, records);
			}
			
			records.add(r);
		}
	}
	*/
	
	private void addRecord(SBean.DBDuelRecord record)
	{
		List<SBean.DBDuelRecord> records = rid2RecordMap.get(record.rid);
		if (records == null) {
			records = new LinkedList<SBean.DBDuelRecord>();
			rid2RecordMap.put(record.rid, records);
		}
		
		records.add(record);
		while (records.size() > 30)
			records.remove(0);
		duel.records.add(record);
	}
	
	public synchronized List<SBean.DBDuelRecord> getRecordList(int rid)
	{
		List<SBean.DBDuelRecord> records = new ArrayList<SBean.DBDuelRecord>();
		List<SBean.DBDuelRecord> recs = rid2RecordMap.get(rid);
		if (recs != null) {
			for (SBean.DBDuelRecord r : recs)
				records.add(r.kdClone());
		}
		
		return records;
	}
	
	public synchronized SBean.DBDuelMatchRecord getMatchRecord(int rid, int rindex, int mindex, String[] names, short[] headIconIds, short[] levels, boolean[] first)
	{
		List<SBean.DBDuelRecord> records = rid2RecordMap.get(rid);
		if (records == null || rindex < 0 || records.size() <= rindex)
			return null;
		
		SBean.DBDuelRecord rec = records.get(rindex);
		if (rec == null || mindex < 0 || rec.matches.size() <= mindex)
			return null;
		
		names[0] = rec.rname1;
		names[1] = rec.rname2;
		headIconIds[0] = rec.headIconId1;
		headIconIds[1] = rec.headIconId2;
		levels[0] = rec.level1;
		levels[1] = rec.level1;
		first[0] = rec.first>0;
		
		return rec.matches.get(mindex);
	}
	
	public void autoSelectGeneral(RTMatchData match) {
	    if (match.lastSelectionTime == 0 || match.lastMySelection || match.quit == (byte)1) {
	        return;
	    }
	    Role role = gs.getLoginManager().getRole(match.rid);
	    if (role == null) {
	        return;
	    }
	    int selectTime = 21;
	    int curType2 = 0;
        if (match.combatType == 1) {
            curType2 = 3;
        } else if (match.combatType == 2) {
            curType2 = 5;
        } else if (match.combatType == 3) {
            curType2 = 7;
        }
        if (curType2 == 0) {
            return;
        }
        if (curType2 == match.combatIndex + 1) {
            selectTime = 31;
        }
	    if (gs.getTime() - match.lastSelectionTime >= selectTime) {
	        boolean seletPet = false;
	        int seletGeneralCnt = 0;
	        if (match.selfPet == null) {
	            seletPet = true;
	        }
	        boolean seletG = false;
	        if (!seletPet || curType2 == match.combatIndex) {
	            seletG = true;
	        }
	        if (match.first) {
	            if (match.combatIndex == 0 || match.combatIndex == 3 || match.combatIndex == 4) {
	                if (seletG) {
	                    if (match.selfGenerals.size() == 0) {
	                        seletGeneralCnt = 1;
	                    } else if (match.selfGenerals.size() == 1) {
	                        seletGeneralCnt = 2;
	                    } else if (match.selfGenerals.size() == 3) {
	                        seletGeneralCnt = 2;
	                    }
	                }
	            } else {
	                if (seletG) {
	                    if (match.selfGenerals.size() == 0) {
	                        seletGeneralCnt = 2;
	                    } else if (match.selfGenerals.size() == 2) {
	                        seletGeneralCnt = 2;
	                    } else if (match.selfGenerals.size() == 4) {
	                        seletGeneralCnt = 1;
	                    }
	              
	                }
	            }
	        } else {
	            if (match.combatIndex == 0 || match.combatIndex == 3 || match.combatIndex == 4) {
	                if (seletG) {
                        if (match.selfGenerals.size() == 0) {
                            seletGeneralCnt = 2;
                        } else if (match.selfGenerals.size() == 2) {
                            seletGeneralCnt = 2;
                        } else if (match.selfGenerals.size() == 4) {
                            seletGeneralCnt = 1;
                        }
                  
                    }
                } else {
                    if (seletG) {
                        if (match.selfGenerals.size() < 1) {
                            seletGeneralCnt = 1;
                        } else if (match.selfGenerals.size() >= 1 && match.selfGenerals.size() < 3) {
                            seletGeneralCnt = 2;
                        } else if (match.selfGenerals.size() >= 3 && match.selfGenerals.size() < 5) {
                            seletGeneralCnt = 2;
                        }
                    }
                }
	        }
	        if (curType2 == match.combatIndex+1) {
	            seletGeneralCnt = 5;
	        }
	        if (seletPet) {
	            List<SBean.DBPet> allPets = new ArrayList<SBean.DBPet>();
	            synchronized (role) {
                    allPets.addAll(role.copyPetsWithoutLock());
                }
	            for (int i = 0; i < allPets.size(); i++) {
                    if (match.selfPids.contains(allPets.get(i).id)) {
                        allPets.remove(i);
                        i--;
                    }
                }
	            Random r = new Random();
	            int index = r.nextInt(allPets.size());
	            List<Short> selectPets = new ArrayList<Short>();
	            List<SBean.DBRelation> relations = new ArrayList<SBean.DBRelation>();
	            selectPets.add(allPets.get(index).id);
	            synchronized (role) {
	                match.selfPet = role.copyPetsWithoutLock(selectPets).get(0); 
	                relations = role.getActiveRelations();
                }
	            match.selfPet.lvl = 100;
	            match.selfPids.add(Short.valueOf(match.selfPet.id));
	            for (SBean.DBRelation j : relations)
	                match.selfRelations.add(j);
	        }
	        List<DBRoleGeneral> SelectedGs = new ArrayList<DBRoleGeneral>();
	        if (seletGeneralCnt > 0) {
	            List<DBRoleGeneral> allGenerals = new ArrayList<DBRoleGeneral>();
                synchronized (role) {
                    allGenerals.addAll(role.copyGeneralsWithoutLock());
                }
                Set<Short> selectedGids = new HashSet<Short>();
                for (DBRoleGeneral dbRoleGeneral : match.selfGenerals) {
                    selectedGids.add(dbRoleGeneral.id);
                }
                for (int i = 0; i < allGenerals.size(); i++) {
                    if (match.selfGids.contains(allGenerals.get(i).id) || selectedGids.contains(allGenerals.get(i).id)) {
                        allGenerals.remove(i);
                        i--;
                    }
                }
                for (int i = 0; i < seletGeneralCnt; i++) {
                    Random r = new Random();
                    int index = r.nextInt(allGenerals.size());
                    DBRoleGeneral g = allGenerals.remove(index);
                    g.lvl = 100;
                    g.advLvl = 14;
                    for (int j = 0; j < g.skills.size(); j ++) {
                        if (j < gs.gameData.generalsCmn.skills.size()) {
                            SBean.GeneralSkillCFGS scfg = gs.gameData.generalsCmn.skills.get(j);
                            short lvlMax = (short)(g.lvl - scfg.maxLvl);
                            if( lvlMax < 1 )
                                lvlMax = 1;
                            g.skills.set(j, lvlMax);
                        }
                    }
                    match.selfGenerals.add(g);
                    SelectedGs.add(g);
                }
	        }
	        match.lastSelectionTime = gs.getTime();
	        List<SBean.DBPetBrief> pet = new ArrayList<SBean.DBPetBrief>();
	        pet.add(match.selfPet);
	        selectGeneral(new SBean.DuelSelectGeneralReq(0, new SBean.GlobalRoleID(gs.getConfig().id, match.rid), match.oppoRid, SelectedGs, pet, match.selfRelations, match.matchId));
	        if (match.selfGenerals.size() == 5 && match.selfPet != null && match.oppoGenerals.size() == 5 && match.oppoPet != null) {
	            startAttack(new SBean.DuelStartAttackReq(0, new SBean.GlobalRoleID(gs.getConfig().id, role.id), match.oppoRid, match.combatIndex, match.matchId));
	        }
	    }
	}
	
	GameServer gs;
	SBean.DBDuel duel;
	Map<Integer, SBean.DBDuelRole> rolesMap = new HashMap<Integer, SBean.DBDuelRole>();
	Map<Integer, RTMatchData> matchMap = new HashMap<Integer, RTMatchData>();
	List<SortScore> sortScoreList = new ArrayList<SortScore>();
	Map<Integer, SBean.DBDuelRank> rid2ScoreMap = new HashMap<Integer, SBean.DBDuelRank>();
	Map<Integer, List<SBean.DBDuelRecord>> rid2RecordMap = new HashMap<Integer, List<SBean.DBDuelRecord>>();
	int saveTime;
	private EventService service;
	private List<SBean.DBDuelRole> rewardRoles;

	public static final int INVOKE_SUCCESS = 0;
	public static final int INVOKE_UNKNOWN_ERROR = -1;
	public static final int INVOKE_TIMEOUT = -2;
	public static final int INVOKE_JOIN_LEAVE = -3;
	public static final int INVOKE_OPPO_SPEEDUP = -4;
	
	public static final int INVOKE_VIST_ROLE_ERROR = -100;
	public static final int INVOKE_VIST_DBROLE_ERROR = -101;
	
	public static final long MAX_WAIT_TIME = 2000;
}

package i3k.gs;

import i3k.DBForce;
import i3k.DBMail;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.TLog;
import i3k.SBean.DBForceSWarBrief;
import i3k.gs.LoginManager.RoleLevelPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class ForceSWarManager {
	
	public static int SAVE_INTERVAL = 13 * 60;
	
	public static class Reward
	{
		List<SBean.DropEntryNew> rewards = new ArrayList<SBean.DropEntryNew>();
		Set<Integer> rids = new HashSet<Integer>();
		boolean win;
	}
	
	public static class SortScore
	{
		SortScore(int score, int fid)
		{
			this.fid = fid;
			this.score = score;
		}
		int fid;
		int score;
	}
	
	public static class ScoreRank
	{
		ScoreRank(int rank, int fid, int score, short icon, String fname, short lvl)
		{
			this.rank = rank;
			this.fid = fid;
			this.score = score;
			this.icon = icon;
			this.fname = fname;
			this.lvl = lvl;
		}
		public int rank;
		public int fid;
		public int score;
		public short icon;
		public String fname;
		public short lvl;
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
				gs.getLogger().debug("swar event task timeout remove callback");
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
				gs.getLogger().warn("swar event task callback id=" + id + "==>but timeout");
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

	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBForceSWarGlobal swar)
		{
			this.swar = swar;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("swar2");
			byte[] data = Stream.encodeLE(swar);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("swar save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBForceSWarGlobal swar;
	}
	
	public ForceSWarManager(GameServer gs)
	{
		this.gs = gs;
		this.service = new EventService(gs);
	}
	
	public void init(SBean.DBForceSWarGlobal swar)
	{
		globalProtectDay = gs.getDay();
		
		swarGlobal = swar;
		if (swarGlobal == null)
			swarGlobal = new SBean.DBForceSWarGlobal(new ArrayList<SBean.DBForceSWar>(), new ArrayList<SBean.DBForceSWarRank>(), gs.getDay(), 0, 0);
		saveTime = gs.getTime();
		
		swarMap.clear();
		for (SBean.DBForceSWar sw : swarGlobal.swar) {
			swarMap.put(sw.brief.fid, sw);
		}
		
		fid2ScoreMap.clear();
		for (SBean.DBForceSWarRank r : swarGlobal.ranks) {
			fid2ScoreMap.put(r.fid, r);
			setScore(r.score, r.fid, r.fname, r.serverName, r.iconId, r.lvl);
		}
	}
	
	public void onTimer()
	{
		int time = gs.getTime();
		if( time > saveTime + SAVE_INTERVAL ) {
			save();
			saveTime = time;
		}
		service.onTaskTimer();
		
		synchronized (this) {
			int day = gs.getDay();
			if (swarGlobal.day < day) {
				for (SBean.DBForceSWar swar : swarGlobal.swar)
					updateSWarByDay(swar, day);
				swarGlobal.day = day;
			}
		}
		
		List<Reward> re = new ArrayList<Reward>();
		List<Integer> votes = new ArrayList<Integer>();
		List<Integer> attacks = new ArrayList<Integer>();
		synchronized (this) {
			if (rewards.size() > 0) {
				re.addAll(rewards);
				rewards.clear();
			}
			
			if (voteNotices.size() > 0) {
				votes.addAll(voteNotices);
				voteNotices.clear();
			}
			
			if (attackNotices.size() > 0) {
				attacks.addAll(attackNotices);
				attackNotices.clear();
			}
		}
		
		for (Reward r : re)
			for (int rid : r.rids)
				gs.getLoginManager().sysSendMessage(0, rid, DBMail.Message.SUB_TYPE_FORCE_SWAR_REWARD, "", "" + (r.win?1:0), 0, true, r.rewards);
		
		for (int r : votes)
			gs.getLoginManager().sysSendMessage(0, r, DBMail.Message.SUB_TYPE_FORCE_SWAR_VOTE, "", "", 0, true, new ArrayList<SBean.DropEntryNew>());
		
		for (int r : attacks)
			gs.getLoginManager().sysSendMessage(0, r, DBMail.Message.SUB_TYPE_FORCE_SWAR_ATTACK, "", "", 0, true, new ArrayList<SBean.DropEntryNew>());
	}
	
	public synchronized void save()
	{
		gs.getDB().execute(new SaveTrans(swarGlobal.kdClone()));
	}
	
	private SBean.DBForceSWar getSWar(int fid, short iconId, String fname)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null) {
			swar = new SBean.DBForceSWar(new SBean.DBForceSWarBrief(fid, iconId, fname, (short)1, 0, 0, gs.getConfig().id, serverName, new ArrayList<SBean.DBForceSWarBuilding>(), 0), 
					new ArrayList<SBean.DBForceSWarOppo>(), new ArrayList<SBean.DBForceSWarOppo>(), 0, 0, 0, new ArrayList<SBean.DBForceSWarLog>(), new ArrayList<SBean.DBForceSWarRole>(), 0, 0, (short)0, 
					new ArrayList<SBean.DBForceSWarDonateRank>(), (byte)0, (byte)0, (byte)0, 0);
			
			//swar.attackBadges = 1000000;
			
			swarMap.put(fid, swar);
			swarGlobal.swar.add(swar);
		}
		else
			swar.brief.fname = fname;
		return swar;
	}
	
	// winTimes 0 selfWin 1 oppoWin
	// scores 0 totalScore 1 getScore
	public static boolean getOppoScores(GameServer gs, SBean.DBForceSWarOppo oppo, short[] winTimes, int[] scores)
	{
		if (oppo == null || winTimes == null || scores == null)
			return false;
		
		List<SBean.DBForceSWarBuilding> buildings = oppo.oppoBriefs.buildings;
		short oppoWin = 0;
		short selfWin = 0;
		int totalScore = 0;
		int getScore = 0;
		
		SBean.ForceSWarBasicCFGS cfg = gs.gameData.getSWarCFG().basic;
		for (byte bid = 1; bid <= cfg.occupyScore.size(); bid ++) {
			boolean found = false;
			for (SBean.DBForceSWarBuilding b : buildings) {
				if (b.bid == bid) {
					found = true;
					break;
				}
			}
			
			if (found)
				continue;
			
			buildings.add(new SBean.DBForceSWarBuilding(bid, new ArrayList<SBean.DBForceSWarTeam>(), 0));
		}
		
		for (SBean.DBForceSWarBuilding b : buildings) {
			int winTeams = 0;
			
			int buildingScore = 0;
			if (b.bid > 0 && b.bid <= cfg.occupyScore.size())
				buildingScore = cfg.occupyScore.get(b.bid-1);
			totalScore += buildingScore;

			for (SBean.DBForceSWarTeam t : b.teams) {
				boolean winTeam = false;
				totalScore += cfg.winScore; 
				for (SBean.DBForceSWarMatchRecord r : t.records) {
					if (r.result == 1) {
						selfWin ++;
						winTeam = true;
						getScore += cfg.winScore;
					}
					else
						oppoWin ++;
				}

				if (winTeam)
					winTeams ++;
			}

			if (winTeams >= b.teams.size() || b.teams.size() == 0)
				getScore += buildingScore;
		}

		winTimes[0] = selfWin;
		winTimes[1] = oppoWin;
		scores[0] = totalScore;
		scores[1] = getScore;
		return true;
	}
	
	// winTimes 0 selfWin 1 oppoWin
	// scores 0 totalScore 1 getScore
	public static boolean getSelfScores(GameServer gs, SBean.DBForceSWar swar, short[] winTimes, int[] scores)
	{
		if (swar == null || winTimes == null || scores == null)
			return false;
		
		List<SBean.DBForceSWarBuilding> buildings = swar.brief.buildings;
		short selfWin = 0;
		short oppoWin = 0;
		int totalScore = 0;
		int getScore = 0;
		
		SBean.ForceSWarBasicCFGS cfg = gs.gameData.getSWarCFG().basic;
		for (byte bid = 1; bid <= cfg.occupyScore.size(); bid ++) {
			boolean found = false;
			for (SBean.DBForceSWarBuilding b : buildings) {
				if (b.bid == bid) {
					found = true;
					break;
				}
			}
			
			if (found)
				continue;
			
			buildings.add(new SBean.DBForceSWarBuilding(bid, new ArrayList<SBean.DBForceSWarTeam>(), 0));
		}
		
		for (SBean.DBForceSWarBuilding b : buildings) {
			int winTeams = 0;
			
			int buildingScore = 0;
			if (b.bid > 0 && b.bid <= cfg.occupyScore.size())
				buildingScore = cfg.occupyScore.get(b.bid-1);
			totalScore += buildingScore;

			for (SBean.DBForceSWarTeam t : b.teams) {
				boolean winTeam = false;
				totalScore += cfg.winScore; 
				for (SBean.DBForceSWarMatchRecord r : t.records) {
					if (r.result == 0) {
						selfWin ++;
						winTeam = true;
						getScore += cfg.winScore;
					}
					else
						oppoWin ++;
				}

				if (winTeam)
					winTeams ++;
			}

			if (winTeams >= b.teams.size() || b.teams.size() == 0)
				getScore += buildingScore;
		}

		winTimes[0] = selfWin;
		winTimes[1] = oppoWin;
		scores[0] = totalScore;
		scores[1] = getScore;
		return true;
	}
	
	private void attackEnd(SBean.DBForceSWar swar)
	{
		short winTimes[] = {0, 0};
		int scores[] = {0, 0};
		for (SBean.DBForceSWarDonateRank r : swar.donateRanks)
			r.donate = 0;
		if (swar.opponents.size() > 0) {
			SBean.DBForceSWarOppo oppo = swar.opponents.get(0);
			if (getOppoScores(gs, oppo, winTimes, scores)) {

				SBean.ForceSWarCFGS swarCfg = gs.gameData.getSWarCFG();
				float rate = 1;
				int lvl = swar.brief.lvl - 1;
				if (lvl >= 0 && lvl < swarCfg.level.size()) {
					SBean.ForceSWarLevelCFGS cfg = swarCfg.level.get(lvl);
					rate = cfg.winRate;
				}
				
				boolean win = false;
				if (scores[1] >= scores[0] * rate)
					win = true;
				
				int exp = 0;
				if (lvl >= 0 && lvl < swarCfg.exp.size()) {
					SBean.ForceSWarExpCFGS cfg = swarCfg.exp.get(lvl);
					if (win)
						exp = cfg.attackWinExp;
					else
						exp = cfg.attackLoseExp;
				}
				
				SBean.DBForceSWarLog log = new SBean.DBForceSWarLog((byte)1, gs.getTime(), win?1:0, oppo.oppoBriefs.serverName, oppo.oppoBriefs.fname, winTimes[1], winTimes[0], 0);
				swar.logs.add(log);
				swar.roles.clear();
				swar.opponents.clear();
				swar.loseTimes = 0;
				swar.weakBufId = 0;
				addExp(swar, exp);
				if (win) {
					swar.brief.winTimes ++;
					setScore(swar.brief.winTimes, swar.brief.fid, swar.brief.fname, swar.brief.serverName, swar.brief.iconId, swar.brief.lvl);
				}
				
				final Reward reward = new Reward();
				reward.win = win;
				if (lvl >= 0 && lvl < swarCfg.reward.size()) {
					SBean.ForceSWarRewardCFGS cfg = swarCfg.reward.get(lvl);
					if (win)
						reward.rewards.addAll(cfg.winReward);
					else
						reward.rewards.addAll(cfg.loseReward);
				}
				
				gs.getForceManager().getForceMembersRoleID(swar.brief.fid, new ForceManager.QueryForceMembersCallback() {
					
					@Override
					public void onCallback(boolean success, int headID,
							List<Integer> forceMembers) {
						reward.rids.addAll(forceMembers);
						synchronized (ForceSWarManager.this) {
							rewards.add(reward);
						}
					}
				}, 3 * 86400);
			}
		}
	}
	
	private void addExp(SBean.DBForceSWar swar, int exp)
	{
		SBean.ForceSWarCFGS swarCfg = gs.gameData.getSWarCFG();
		int upgradeExp = 0;
		int lvl = swar.brief.lvl - 1;
		if (lvl >= 0 && lvl < swarCfg.level.size()) {
			SBean.ForceSWarLevelCFGS cfg = swarCfg.level.get(lvl);
			upgradeExp = cfg.expNeed;
		}
		
		if (exp > 0)
			swar.brief.exp += exp;
		else {
			swar.brief.exp += exp;
			if (swar.brief.exp < 0)
				swar.brief.exp = 0;
			upgradeExp = 0;
		}

		while (upgradeExp > 0 && swar.brief.exp >= upgradeExp) {
			swar.brief.exp -= upgradeExp;
			swar.brief.lvl ++;

			lvl = swar.brief.lvl - 1;
			if (lvl >= 0 && lvl < swarCfg.level.size()) {
				SBean.ForceSWarLevelCFGS cfg = swarCfg.level.get(lvl);
				upgradeExp = cfg.expNeed;
			}
		}
	}
	
	private void defendEnd(SBean.DBForceSWar swar)
	{
		short winTimes[] = {0, 0};
		int scores[] = {0, 0};
		if (swar.attacked.size() > 0) {
			SBean.DBForceSWarOppo oppo = swar.attacked.get(0);
			if (getSelfScores(gs, swar, winTimes, scores)) {

				SBean.ForceSWarCFGS swarCfg = gs.gameData.getSWarCFG();
				float rate = 1;
				int lvl = oppo.oppoBriefs.lvl - 1;
				if (lvl >= 0 && lvl < swarCfg.level.size()) {
					SBean.ForceSWarLevelCFGS cfg = swarCfg.level.get(lvl);
					rate = cfg.winRate;
				}
				
				boolean win = false;
				if (scores[1] < scores[0] * rate)
					win = true;
				
				int exp = 0;
				if (lvl >= 0 && lvl < swarCfg.exp.size()) {
					SBean.ForceSWarExpCFGS cfg = swarCfg.exp.get(lvl);
					if (win)
						exp = cfg.defendWinExp;
					else 
						exp = -cfg.defendLoseExp;
				}
				
				SBean.DBForceSWarLog log = new SBean.DBForceSWarLog((byte)0, gs.getTime(), win?1:0, oppo.oppoBriefs.serverName, oppo.oppoBriefs.fname, winTimes[1], winTimes[0], 0);
				swar.logs.add(log);
				swar.attacked.clear();
				addExp(swar, exp);
				if (win) {
					swar.brief.winTimes ++;
					swar.loseTimes = 0;
					swar.weakBufId = 0;
					setScore(swar.brief.winTimes, swar.brief.fid, swar.brief.fname, swar.brief.serverName, swar.brief.iconId, swar.brief.lvl);
				}
				else {
					swar.loseTimes ++;
					SBean.ForceSWarWeakBufCFGS cfg = null;
					for (SBean.ForceSWarWeakBufCFGS wcfg : swarCfg.weakBuf)
						if (swar.loseTimes >= wcfg.loseTimes)
							cfg = wcfg;
					
					if (cfg != null)
						swar.weakBufId = cfg.bufId;
				}
				for (SBean.DBForceSWarBuilding b : swar.brief.buildings)
					for (SBean.DBForceSWarTeam t : b.teams) {
						t.status = 0;
						t.records.clear();
					}
			}
		}
	}
	
	private void updateSWarByDay(SBean.DBForceSWar swar, int day)
	{
		int dayDiff = day - swar.defendDay;
		if (swar.defendDay == 0)
			dayDiff = 0;
		
		if (swar.protectDays > 0 && dayDiff != 4) // 正常保护的当天不减保护时间
			swar.protectDays --;
		
		if (dayDiff > 3) {
			swar.defendDay = 0;
			defendEnd(swar);
		}
		
		if (swar.defendDay == 0) {
			final SBean.DBForceSWar sw = swar;
			gs.getForceManager().getForceMembersRoleID(swar.brief.fid, new ForceManager.QueryForceMembersCallback() {
				
				@Override
				public void onCallback(boolean success, int headID,
						List<Integer> forceMembers) {
					Set<Integer> membersMap = new HashSet<Integer>();
					for (int rid : forceMembers)
						membersMap.add(rid);
					
					synchronized (ForceSWarManager.this) {
						for (SBean.DBForceSWarBuilding b : sw.brief.buildings) {
							List<SBean.DBForceSWarTeam> removeTeams = new ArrayList<SBean.DBForceSWarTeam>();
							for (SBean.DBForceSWarTeam t : b.teams)
								if (!membersMap.contains(t.id))
									removeTeams.add(t);
							
							for (SBean.DBForceSWarTeam t : removeTeams)
								b.teams.remove(t);
						}
					}
				}
			}, 0);
		}
		
		dayDiff = day - swar.attackDay;
		if (swar.attackDay == 0)
			dayDiff = 0;
		if (dayDiff > 2) {
			if (dayDiff > 3)
				swar.attackDay = 0;
			attackEnd(swar);
		}
		else if (dayDiff == 2) {
			gs.getForceManager().getForceMembersRoleID(swar.brief.fid, new ForceManager.QueryForceMembersCallback() {
				
				@Override
				public void onCallback(boolean success, int headID,
						List<Integer> forceMembers) {
					synchronized (ForceSWarManager.this) {
						attackNotices.addAll(forceMembers);
					}
				}
			}, 0);
			
			if (swar.opponents.size() > 1) {
				SBean.DBForceSWarOppo oppo = null;
				SBean.DBForceSWarOppo release = null;
				if (swar.opponents.get(0).votes.size() > swar.opponents.get(1).votes.size()) {
					oppo = swar.opponents.get(0);
					release = swar.opponents.get(1);
				}
				else {
					oppo = swar.opponents.get(1);
					release = swar.opponents.get(0);
				}
				if (oppo != null) {
					swar.opponents.clear();
					swar.opponents.add(oppo);
				}
				gs.getRPCManager().exchangeSendSWarVoteReq(new SBean.SWarVoteReq(new SBean.GlobalForceID(gs.getConfig().id, swar.brief.fid), 
						new SBean.GlobalForceID(oppo.oppoBriefs.serverId, oppo.oppoBriefs.fid), 
						new SBean.GlobalForceID(release.oppoBriefs.serverId, release.oppoBriefs.fid), swar.brief.serverName, swar.brief.fname, swar.brief.lvl));
			}
		}
		else if (dayDiff == 1) {
			gs.getForceManager().getForceMembersRoleID(swar.brief.fid, new ForceManager.QueryForceMembersCallback() {
				
				@Override
				public void onCallback(boolean success, int headID,
						List<Integer> forceMembers) {
					synchronized (ForceSWarManager.this) {
						voteNotices.addAll(forceMembers);
					}
				}
			}, 0);
		}
	}
	
	public synchronized SBean.DBForceSWar sync(int fid, short iconId, String fname, String serverName, int [] status)
	{
		if (this.serverName.equals("Unknown") && !serverName.isEmpty())
			this.serverName = serverName;
		SBean.DBForceSWar swar = getSWar(fid, iconId, fname);
		
		int day = gs.getDay();
		status[0] = 0;
		if (swar.defendDay > 0)
			status[0] = day - swar.defendDay + 1;
		if (swar.protectDays > 0)
			status[0] = 4;
		status[1] = 0;
		if (swar.attackDay > 0)
			status[1] = day - swar.attackDay + 1;

		return swar.kdClone();
	}
	
	public synchronized List<SBean.DBForceSWarLog> getLogs(int fid)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		List<SBean.DBForceSWarLog> logs = new ArrayList<SBean.DBForceSWarLog>();
		for (SBean.DBForceSWarLog l : swar.logs)
			logs.add(l.kdClone());
		
		return logs;
	}
	
	public synchronized void search(Role role)
	{
		if (role == null)
			return;
		
		if (role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -1));
			return;
		}
		
		boolean globalProtect = (gs.getDay() - globalProtectDay < gs.gameData.getSWarCFG().basic.globalProtect);
		if (globalProtect) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -2));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -3));
			return;
		}
		
		if (swar.attackDay > 0) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -4));
			return;
		}
		
		List<SBean.ForceSWarLevelCFGS> lvlCfg = gs.gameData.getSWarCFG().level;
		int lvl = swar.brief.lvl - 1;
		if (lvl < 0 || lvl >= lvlCfg.size()) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -5));
			return;
		}
		SBean.ForceSWarLevelCFGS cfg = lvlCfg.get(lvl);
		
		if (swar.attackBadges < cfg.badgeNeed) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -6));
			return;
		}
		
		search(new SBean.SWarSearchReq(0, new SBean.GlobalForceID(gs.getConfig().id, role.forceInfo.id), new SBean.GlobalForceID(0, 0), swar.brief.lvl), role);
	}
	
	private synchronized void onSearch(Role role, SBean.SWarSearchRes res)
	{
		if (role == null || res == null) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -7));
			return;
		}

		if (role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -8));
			return;
		}

		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -9));
			return;
		}

		if (res.oppos.size() != 2) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -10));
			return;
		}

		List<SBean.ForceSWarLevelCFGS> lvlCfg = gs.gameData.getSWarCFG().level;
		int lvl = swar.brief.lvl - 1;
		if (lvl < 0 || lvl >= lvlCfg.size()) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -11));
			return;
		}
		SBean.ForceSWarLevelCFGS cfg = lvlCfg.get(lvl);
		
		if (res.error != 0) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(false, -12));
			return;
		}
		
		swar.opponents.clear();
		for (SBean.DBForceSWarBrief b : res.oppos) {
			SBean.DBForceSWarOppo oppo = new SBean.DBForceSWarOppo(b, new ArrayList<Integer>(), 0);
			swar.opponents.add(oppo);
		}
		swar.attackDay = gs.getDay();
		if (swar.attackBadges >= cfg.badgeNeed)
			swar.attackBadges -= cfg.badgeNeed;
		else
			swar.attackBadges = 0;

		gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarSearch(true, 0));
	}
	
	public interface GetForceInfoCallback
	{
		public void onCallback(boolean success, int fid, short iconId, String fname);
	}
	
	public class GetForceInfoTrans implements Transaction
	{
		public GetForceInfoTrans(int fid, GetForceInfoCallback cb)
		{
			this.fid = fid;
			this.cb = cb;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				fname = dbforce.name;
				iconId = dbforce.iconID;
				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (cb != null)
				cb.onCallback(errcode == ErrorCode.eOK, fid, iconId, fname);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final int fid;
		short iconId;
		String fname;
		GetForceInfoCallback cb;
	}
	
	public void getForceInfo(int fid, GetForceInfoCallback cb)
	{
		gs.getDB().execute(new GetForceInfoTrans(fid, cb));
	}
	
	public static class ForceInfo
	{
		int fid;
		short iconId;
		String fname;
		int averageLvl;
		List<Integer> rids = new ArrayList<Integer>();
		boolean calculated = false;
	}
	
	public interface GetForcesInfoCallback
	{
		public void onCallback(boolean success, List<ForceInfo> info);
	}
	
	public class GetForcesInfoTrans implements Transaction
	{
		public GetForcesInfoTrans(Set<Integer> fids, GetForcesInfoCallback cb)
		{
			this.fids = fids;
			this.cb = cb;
		}

		@Override
		public boolean doTransaction()
		{
			for (int fid : fids) {
				DBForce dbforce = force.get(fid);
				if( dbforce != null )
				{
					ForceInfo info = new ForceInfo();
					info.fid = fid;
					info.fname = dbforce.name;
					info.iconId = dbforce.iconID;
					for (DBForce.Member m : dbforce.members)
						info.rids.add(m.id);
					infos.add(info);
				}
			}
			if (infos.size() != fids.size())
				return false;
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (cb != null)
				cb.onCallback(errcode == ErrorCode.eOK, infos);
		}
		
		@AutoInit
		public Table<Integer, DBForce> force;
		
		final Set<Integer> fids;
		List<ForceInfo> infos = new ArrayList<ForceInfo>();
		GetForcesInfoCallback cb;
	}
	
	public void getForcesInfo(Set<Integer> fids, GetForcesInfoCallback cb)
	{
		gs.getDB().execute(new GetForcesInfoTrans(fids, cb));
	}
	
	private synchronized void onLvlCalculated(final SBean.SWarSearchReq req, short lvl, List<ForceInfo> infos)
	{
		final List<DBForceSWarBrief> lst = new ArrayList<DBForceSWarBrief>();
		final boolean globalProtect = (gs.getDay() - globalProtectDay < gs.gameData.getSWarCFG().basic.globalProtect);
		
		boolean allDone = true;
		for (ForceInfo info : infos)
			if (!info.calculated) {
				allDone = false;
				break;
			}
		
		if (allDone) {
			int absDiff = 10000;
			ForceInfo absInfo = null;
			SBean.DBForceSWar swar = null;
			for (ForceInfo info : infos) {
				swar = getSWar(info.fid, info.iconId, info.fname);
				if (swar.defendDay > 0 || swar.protectDays > 0)
					continue;
				
				int diff = Math.abs(lvl - info.averageLvl);
				if (diff < absDiff) {
					absDiff = diff;
					absInfo = info;
				}
			}
			
			if (absInfo == null || swar == null) {
				gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -3));
				return;
			}
			
			if (swar.defendDay == 0 && swar.protectDays == 0 && !globalProtect) {
				swar.defendDay = gs.getDay();
				lst.add(swar.brief.kdClone());
				gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, 0));
				return;
			}
			
			gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -4));
		}
	}
	
//	public synchronized void handleSearchForward(final SBean.SWarSearchReq req)
//	{
//		final List<DBForceSWarBrief> lst = new ArrayList<DBForceSWarBrief>();
//		final boolean globalProtect = (gs.getDay() - globalProtectDay < gs.gameData.getSWarCFG().basic.globalProtect);
//		int count = swarGlobal.swar.size();
//		if (count > 0) {
//			Set<Integer> skipIndexes = new HashSet<Integer>();
//			while (skipIndexes.size() < count) {
//				int index = 0;
//				do {
//					index = gs.gameData.getRandInt(0, count);
//				} while (skipIndexes.contains(index));
//				skipIndexes.add(index);
//				SBean.DBForceSWar swar = swarGlobal.swar.get(index);
//				if (swar.brief.fid == req.forceID.forceID && gs.getConfig().id == req.forceID.serverID)
//					continue;
//				if (swar.brief.fid == req.skipForceID.forceID && gs.getConfig().id == req.skipForceID.serverID)
//					continue;
//				
//				if (swar.defendDay == 0 && swar.protectDays == 0 && !globalProtect) {
//					swar.defendDay = gs.getDay();
//					lst.add(swar.brief.kdClone());
//					gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, 0));
//					return;
//				}
//			}
//		}
//		
//		Integer fid = null;
//		int retry = 0;
//		while (retry < 5) {
//			fid = gs.getForceManager().getRandomForce();
//			if (fid == null)
//				break;
//			if ((gs.getConfig().id != req.skipForceID.serverID || fid != req.skipForceID.forceID) &&
//					(gs.getConfig().id != req.forceID.serverID || fid != req.forceID.forceID))
//				break;
//			fid = null;
//			retry ++;
//		}
//		if (fid == null) {
//			gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -1));
//			return;
//		}
//			
//		getForceInfo(fid, new GetForceInfoCallback() {
//			
//			@Override
//			public void onCallback(boolean success, int fid, short iconId, String fname) {
//				if (success) {
//					synchronized (ForceSWarManager.this) {
//						SBean.DBForceSWar swar = getSWar(fid, iconId, fname);
//
//						if (swar.defendDay == 0 && swar.protectDays == 0 && !globalProtect) {
//							swar.defendDay = gs.getDay();
//							lst.add(swar.brief.kdClone());
//							gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, 0));
//							return;
//						}
//					}
//				}
//				
//				gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -2));
//			}
//			
//		});
//	}
	
	public synchronized void handleSearchForward(final SBean.SWarSearchReq req)
	{
		final List<DBForceSWarBrief> lst = new ArrayList<DBForceSWarBrief>();
		/*
		int count = swarGlobal.swar.size();
		if (count > 0) {
			Set<Integer> skipIndexes = new HashSet<Integer>();
			while (skipIndexes.size() < count) {
				int index = 0;
				do {
					index = gs.gameData.getRandInt(0, count);
				} while (skipIndexes.contains(index));
				skipIndexes.add(index);
				SBean.DBForceSWar swar = swarGlobal.swar.get(index);
				if (swar.brief.fid == req.forceID.forceID && gs.getConfig().id == req.forceID.serverID)
					continue;
				if (swar.brief.fid == req.skipForceID.forceID && gs.getConfig().id == req.skipForceID.serverID)
					continue;
				
				if (swar.defendDay == 0 && swar.protectDays == 0 && !globalProtect) {
					swar.defendDay = gs.getDay();
					lst.add(swar.brief.kdClone());
					gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, 0));
					return;
				}
			}
		}
		*/
		
		Set<Integer> fids = new HashSet<Integer>();
		while (fids.size() < 10) {
			Integer fid = null;
			int retry = 0;
			while (retry < 5) {
				fid = gs.getForceManager().getRandomForceEx();
				if (fid == null)
					break;
				if ((gs.getConfig().id != req.skipForceID.serverID || fid != req.skipForceID.forceID) &&
						(gs.getConfig().id != req.forceID.serverID || fid != req.forceID.forceID) && !fids.contains(fid))
					break;
				fid = null;
				retry ++;
			}
			if (fid != null)
				fids.add(fid);
			else
				break;
		}
		if (fids.size() == 0) {
			gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -1));
			return;
		}
			
		getForcesInfo(fids, new GetForcesInfoCallback() {
			
			@Override
			public void onCallback(boolean success, final List<ForceInfo> infos) {
				if (success) {
					for (final ForceInfo info : infos) {

						gs.getLoginManager().getRoleLevel(info.rids, new LoginManager.GetRoleLevelCallback() {

							@Override
							public void onCallback(List<RoleLevelPair> lst) {
								short lvl = 0;
								for (RoleLevelPair r : lst)
									lvl += r.level;
								if (lst.size() > 0)
									lvl /= lst.size();
								info.averageLvl = lvl;
								info.calculated = true;
								onLvlCalculated(req, lvl, infos);
							}

						});
					}
				}
				else
					gs.getRPCManager().exchangeSendSWarSearchForwardRes(new SBean.SWarSearchRes(req.id, req.forceID, lst, -2));
			}
			
		});
	}
	
	public synchronized List<SBean.DBForceSWarOppo> getOppos(int fid)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		List<SBean.DBForceSWarOppo> oppos = new ArrayList<SBean.DBForceSWarOppo>();
		for (SBean.DBForceSWarOppo o : swar.opponents)
			oppos.add(o.kdClone());
		
		return oppos;
	}
	
	public synchronized SBean.DBForceSWarBuilding getOppoBuilding(int fid, int oppoFid, int oppoSid, byte bid)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		List<SBean.DBForceSWarBuilding> buildings = null;
		for (SBean.DBForceSWarOppo o : swar.opponents)
			if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
				buildings = o.oppoBriefs.buildings;
		
		if (buildings == null)
			return null;
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (bid == b.bid)
				building = b;
		
		return building;
	}
	
	public boolean vote(Role role, byte oindex)
	{
		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null)
			return false;
		
		int day = gs.getDay();
		if (day - swar.attackDay != 1)
			return false;

		if (swar.opponents.size() < 2)
			return false;
		
		if (oindex < 0 || oindex > 1)
			return false;
		
		SBean.DBForceSWarOppo oppo = swar.opponents.get(oindex);
		oppo.votes.add(role.id);
		return true;
	}
	
	public synchronized void handleVoteForward(SBean.SWarVoteReq req)
	{
		if (req.oppoForceID.serverID == gs.getConfig().id) {
			SBean.DBForceSWar swar = swarMap.get(req.oppoForceID.forceID);
			if (swar != null && swar.attacked.size() == 0) {
				SBean.DBForceSWarOppo oppo = new SBean.DBForceSWarOppo(new SBean.DBForceSWarBrief(req.forceID.forceID, (short)0, req.fname, (short)req.lvl, 0, 0, req.forceID.serverID, req.serverName, new ArrayList<SBean.DBForceSWarBuilding>(), 0), new ArrayList<Integer>(), 0);
				swar.attacked.add(oppo);
			}
		}
	}
	
	public synchronized void handleRelease(SBean.SWarReleaseReq req)
	{
		if (req.forceID.serverID == gs.getConfig().id) {
			SBean.DBForceSWar swar = swarMap.get(req.forceID.forceID);
			if (swar != null)
				swar.defendDay = 0;
		}
	}
	
	public boolean setTeam(Role role, byte bid, short bindex, List<Short> gids, List<Short> pids)
	{
		List<DBRoleGeneral> generals = null;
		List<SBean.DBPetBrief> pets = null;
		List<SBean.DBRelation> relations = null;
		List<SBean.DBGeneralStone> gStones = null;
		synchronized (role) {
			generals = role.copyGeneralsWithoutLock(gids);
			pets = role.copyPetsWithoutLock(pids);
			relations = role.getActiveRelations();
			gStones = role.copyGeneralStoneWithoutLock();
		}
		
		if (gids.size() != generals.size() || pets.size() != pids.size())
			return false;
		
		synchronized (this) {
			SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
			if (swar == null)
				return false;
			
			//int day = gs.getDay();
			if (swar.defendDay != 0/* && swar.defendDay != day + 4*/)
				return false;

			SBean.DBForceSWarBuilding building = null;
			for (SBean.DBForceSWarBuilding b : swar.brief.buildings)
				if (bid == b.bid)
					building = b;

			if (building == null) {
				building = new SBean.DBForceSWarBuilding(bid, new ArrayList<SBean.DBForceSWarTeam>(), 0);
				swar.brief.buildings.add(building);
			}

			if (bindex > building.teams.size())
				return false;
			else if (bindex == building.teams.size())
				bindex = -1;

			if (bindex < 0) {
				if (gids.size() > 0) {
					bindex = (short)building.teams.size();
					building.teams.add(new SBean.DBForceSWarTeam(role.id, role.name, role.headIconID, role.lvl, 0, 0, (short)0, generals, pets, relations, gStones, new ArrayList<SBean.DBForceSWarMatchRecord>(), 0));
				}
			}
			else {
				SBean.DBForceSWarTeam team = building.teams.get(bindex);
				if (team.id != role.id)
					return false;
				
				if (gids.size() > 0) {
					team.generals = generals;
					team.pets = pets;
					team.relation = relations;
					team.gStones = gStones;
				}
				else
					building.teams.remove(bindex);
			}
		}
		
		return true;
	}
	
	public void startAttack(int sessionid, Role role, int oppoFid, int oppoSid, byte bid, short bindex, List<Short> gids, List<Short> pids)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -1, null));
			return;
		}
		int fid = role.forceInfo.id;
		
		List<DBRoleGeneral> generals = null;
		List<SBean.DBPetBrief> pets = null;
		List<SBean.DBRelation> relations = null;
		List<SBean.DBGeneralStone> gStones = null;
		synchronized (role) {
			generals = role.copyGeneralsWithoutLock(gids);
			pets = role.copyPetsWithoutLock(pids);
			relations = role.getActiveRelations();
			gStones = role.copyGeneralStoneWithoutLock();
		}
		
		if (generals.size() == 0) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -2, null));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -3, null));
			return;
		}
		
		int day = gs.getDay();
		if (day - swar.attackDay != 2) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -4, null));
			return;
		}
		
		List<SBean.DBForceSWarBuilding> buildings = null;
		for (SBean.DBForceSWarOppo o : swar.opponents)
			if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
				buildings = o.oppoBriefs.buildings;
		
		if (buildings == null) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -5, null));
			return;
		}
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size()) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -6, null));
			return;
		}
		
		SBean.DBForceSWarTeam team = building.teams.get(bindex);
		if (team.status != 0) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -7, null));
			return;
		}
		
		SBean.DBForceSWarRole swarRole = null;
		for (SBean.DBForceSWarRole r : swar.roles) {
			if (r.rid == role.id) {
				swarRole = r;
				boolean found = false;
				for (short usedGid : r.usedGIds)
					for (short gid : gids)
						if (gid == usedGid) {
							found = true;
							break;
						}
				
				if (!found) {
					for (short usedPid : r.usedPIds)
						for (short pid : pids)
							if (pid == usedPid) {
								found = true;
								break;
							}
				}
				
				if (found) {
					gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, -8, null));
					return;
				}
			}
		}
		
		if (swarRole == null) {
			swarRole = new SBean.DBForceSWarRole(role.id, new ArrayList<Short>(), new ArrayList<Short>(), 0);
			swar.roles.add(swarRole);
		}
		
		for (short gid : gids)
			swarRole.usedGIds.add(gid);
		for (short pid : pids)
			swarRole.usedPIds.add(pid);

		List<DBRoleGeneral> oppoGenerals = new ArrayList<DBRoleGeneral>();
		for (DBRoleGeneral g : team.generals)
			oppoGenerals.add(g.kdClone());
		
		List<SBean.DBPetBrief> oppoPets = new ArrayList<SBean.DBPetBrief>();
		for (SBean.DBPetBrief p : team.pets)
			oppoPets.add(p.kdClone());
		
		List<SBean.DBRelation> oppoRelations = new ArrayList<SBean.DBRelation>();
		for (SBean.DBRelation r : team.relation)
			oppoRelations.add(r.kdClone());
		
		List<SBean.DBGeneralStone> oppoGStones = new ArrayList<SBean.DBGeneralStone>();
		for (SBean.DBGeneralStone s : team.gStones)
			oppoGStones.add(s.kdClone());
		
		SBean.DBForceSWarMatchRecord record = new SBean.DBForceSWarMatchRecord(gs.getTime(), role.name, team.name, role.lvl, team.level, role.headIconID, team.icon, (byte)0, generals, oppoGenerals, pets, oppoPets, relations, oppoRelations, gStones, oppoGStones, 0, new ArrayList<SBean.DBForceSWarBuf>(), new ArrayList<SBean.DBForceSWarBuf>(), 0);
		recordsMap.put(role.id, record);
		
		gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarStartAttack(oppoFid, oppoSid, bid, bindex, 0, record));
	}
	
	public void finishAttack(int sessionid, Role role, int oppoFid, int oppoSid, byte bid, short bindex, byte win, List<SBean.DBForceSWarBuf> attackBufs, List<SBean.DBForceSWarBuf> defendBufs)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -1));
			return;
		}
		int fid = role.forceInfo.id;
		
		SBean.DBForceSWarMatchRecord record = recordsMap.get(role.id);
		if (record == null) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -2));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -3));
			return;
		}
		
		int day = gs.getDay();
		if (day - swar.attackDay != 2) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -4));
			return;
		}
		
		List<SBean.DBForceSWarBuilding> buildings = null;
		for (SBean.DBForceSWarOppo o : swar.opponents)
			if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
				buildings = o.oppoBriefs.buildings;
		
		if (buildings == null) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -5));
			return;
		}
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size()) {
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -6));
			return;
		}
		
		SBean.DBForceSWarMatchRecord rec = record;
		rec.result = win;
		rec.bufs1.addAll(attackBufs);
		rec.bufs2.addAll(defendBufs);
		attack(new SBean.SWarAttackReq(0, new SBean.GlobalForceID(gs.getConfig().id, role.forceInfo.id), new SBean.GlobalForceID(oppoSid, oppoFid), bid, bindex, rec), role);
		
		TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
		TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
		commonFlowRecord.setEvent(TLog.AT_FORCE_SWAR_FINISH);
		tlogEvent.addCommonFlow(commonFlowRecord);
		gs.getTLogger().logEvent(role, tlogEvent);
	}
	
	private synchronized void onFinishAttack(int sessionid, int rid, int fid, int oppoFid, int oppoSid, byte bid, short bindex, byte win, boolean ok)
	{
		if (ok) {
			SBean.DBForceSWarMatchRecord record = recordsMap.get(rid);
			if (record == null) {
				gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -7));
				return;
			}
			
			SBean.DBForceSWar swar = swarMap.get(fid);
			if (swar == null) {
				gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -8));
				return;
			}
			
			List<SBean.DBForceSWarBuilding> buildings = null;
			for (SBean.DBForceSWarOppo o : swar.opponents)
				if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
					buildings = o.oppoBriefs.buildings;
			
			if (buildings == null) {
				gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -9));
				return;
			}
			
			SBean.DBForceSWarBuilding building = null;
			for (SBean.DBForceSWarBuilding b : buildings)
				if (bid == b.bid)
					building = b;
			
			if (bindex < 0 || bindex >= building.teams.size()) {
				gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -10));
				return;
			}
			
			SBean.DBForceSWarTeam team = building.teams.get(bindex);
			record.result = win;
			if (win > 0)
				team.status = 1;
			else
				team.winTimes ++;
			team.records.add(record);
			recordsMap.remove(rid);
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, 0));
		}
		else
			gs.getRPCManager().sendLuaPacket(sessionid, LuaPacket.encodeForceSWarFinishAttack(oppoFid, oppoSid, bid, bindex, win, -11));
	}
	
	public synchronized void handleAttackForward(SBean.SWarAttackReq req)
	{
		SBean.DBForceSWar swar = swarMap.get(req.oppoForceID.forceID);
		if (swar == null) {
			gs.getRPCManager().exchangeSendSWarAttackForwardRes(new SBean.SWarAttackRes(req.id, req.forceID, req.oppoForceID, req.bid, req.bindex, req.record.result, -1));
			return;
		}
		
		int day = gs.getDay();
		if (day - swar.defendDay != 2) {
			gs.getRPCManager().exchangeSendSWarAttackForwardRes(new SBean.SWarAttackRes(req.id, req.forceID, req.oppoForceID, req.bid, req.bindex, req.record.result, -2));
			return;
		}
		
		List<SBean.DBForceSWarBuilding> buildings = swar.brief.buildings;
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (req.bid == b.bid)
				building = b;
		
		if (req.bindex < 0 || req.bindex >= building.teams.size()) {
			gs.getRPCManager().exchangeSendSWarAttackForwardRes(new SBean.SWarAttackRes(req.id, req.forceID, req.oppoForceID, req.bid, req.bindex, req.record.result, -3));
			return;
		}
		
		SBean.DBForceSWarTeam team = building.teams.get(req.bindex);
		team.records.add(req.record);
		
		if (req.record.result == 1)
			team.status = 1;
		else
			team.winTimes ++;
		
		gs.getRPCManager().exchangeSendSWarAttackForwardRes(new SBean.SWarAttackRes(req.id, req.forceID, req.oppoForceID, req.bid, req.bindex, req.record.result, 0));
	}
	
	public synchronized List<SBean.DBForceSWarMatchRecord> getRecordList(int fid, byte bid, short bindex)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : swar.brief.buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size())
			return null;
		
		SBean.DBForceSWarTeam team = building.teams.get(bindex);
		
		List<SBean.DBForceSWarMatchRecord> records = new ArrayList<SBean.DBForceSWarMatchRecord>();
		for (SBean.DBForceSWarMatchRecord r : team.records)
			records.add(r.kdClone());
		
		return records;
	}
	
	public synchronized SBean.DBForceSWarMatchRecord getRecord(int fid, byte bid, short bindex, short mindex)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : swar.brief.buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size())
			return null;
		
		SBean.DBForceSWarTeam team = building.teams.get(bindex);
		
		if (mindex < 0 || mindex >= team.records.size())
			return null;
		
		SBean.DBForceSWarMatchRecord record = team.records.get(mindex);
		return record;
	}
	
	public synchronized List<SBean.DBForceSWarMatchRecord> getOppoRecordList(int fid, int oppoFid, int oppoSid, byte bid, short bindex)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		List<SBean.DBForceSWarBuilding> buildings = null;
		for (SBean.DBForceSWarOppo o : swar.opponents)
			if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
				buildings = o.oppoBriefs.buildings;
		
		if (buildings == null)
			return null;
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size())
			return null;
		
		SBean.DBForceSWarTeam team = building.teams.get(bindex);
		
		List<SBean.DBForceSWarMatchRecord> records = new ArrayList<SBean.DBForceSWarMatchRecord>();
		for (SBean.DBForceSWarMatchRecord r : team.records)
			records.add(r.kdClone());
		
		return records;
	}
	
	public synchronized SBean.DBForceSWarMatchRecord getOppoRecord(int fid, int oppoFid, int oppoSid, byte bid, short bindex, short mindex)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return null;
		
		List<SBean.DBForceSWarBuilding> buildings = null;
		for (SBean.DBForceSWarOppo o : swar.opponents)
			if (o.oppoBriefs.fid == oppoFid && o.oppoBriefs.serverId == oppoSid)
				buildings = o.oppoBriefs.buildings;
		
		if (buildings == null)
			return null;
		
		SBean.DBForceSWarBuilding building = null;
		for (SBean.DBForceSWarBuilding b : buildings)
			if (bid == b.bid)
				building = b;
		
		if (bindex < 0 || bindex >= building.teams.size())
			return null;
		
		SBean.DBForceSWarTeam team = building.teams.get(bindex);
		
		if (mindex < 0 || mindex >= team.records.size())
			return null;
		
		SBean.DBForceSWarMatchRecord record = team.records.get(mindex);
		return record;
	}
	
	class SearchTask extends EventTask 
	{
		SBean.SWarSearchReq req;
		Role role;

		SearchTask(SBean.SWarSearchReq req, Role role) 
		{
			this.req = req;
			this.role = role;
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
		
		public boolean isTooOld(long now) 
		{
			return callTime + 60000 <= now;
		}

		public void doTask() 
		{
			gs.getRPCManager().exchangeSendSWarSearchReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.SWarSearchRes res = (SBean.SWarSearchRes) obj;
			onSearch(role, res);
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.SWarSearchRes(req.id, req.forceID, new ArrayList<SBean.DBForceSWarBrief>(), error);
		}
	}

	public void search(SBean.SWarSearchReq req, Role role) 
	{
		excuteEventTaskWaitResponse(new SearchTask(req, role));
	}

	class AttackTask extends EventTask 
	{
		SBean.SWarAttackReq req;
		Role role;

		AttackTask(SBean.SWarAttackReq req, Role role) 
		{
			this.req = req;
			this.role = role;
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
			gs.getRPCManager().exchangeSendSWarAttackReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.SWarAttackRes res = (SBean.SWarAttackRes) obj;
			onFinishAttack(role.netsid, role.id, req.forceID.forceID, req.oppoForceID.forceID, req.oppoForceID.serverID, req.bid, req.bindex, req.record.result, res.error == 0);
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.SWarAttackRes(req.id, req.forceID, req.oppoForceID, req.bid, req.bindex, req.record.result, error);
		}
	}

	public void attack(SBean.SWarAttackReq req, Role role) 
	{
		excuteEventTaskWaitResponse(new AttackTask(req, role));
	}

	class RankTask extends EventTask 
	{
		SBean.SWarRankReq req;
		Role role;

		RankTask(SBean.SWarRankReq req, Role role) 
		{
			this.req = req;
			this.role = role;
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
			gs.getRPCManager().exchangeSendSWarRankReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.SWarRankRes res = (SBean.SWarRankRes) obj;
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarRankRes(res));
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.SWarRankRes(req.id, req.forceID, 0, error);
		}
	}

	public void rank(SBean.SWarRankReq req, Role role) 
	{
		excuteEventTaskWaitResponse(new RankTask(req, role));
	}

	class TopRanksTask extends EventTask 
	{
		SBean.SWarTopRanksReq req;
		Role role;

		TopRanksTask(SBean.SWarTopRanksReq req, Role role) 
		{
			this.req = req;
			this.role = role;
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
			gs.getRPCManager().exchangeSendSWarTopRanksReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.SWarTopRanksRes res = (SBean.SWarTopRanksRes) obj;
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeForceSWarTopRanksRes(res));
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.SWarTopRanksRes(req.id, req.forceID, new ArrayList<SBean.SWarTopRank>(), error);
		}
	}

	public void topRanks(SBean.SWarTopRanksReq req, Role role) 
	{
		excuteEventTaskWaitResponse(new TopRanksTask(req, role));
	}

	synchronized void setScore(int score, int fid, String fname, String serverName, short icon, short lvl)
	{
		removeScoreSortList(fid);
		insertScoreSortList(score, fid);
		
		SBean.DBForceSWarRank d = fid2ScoreMap.get(fid);
		if (d == null) {
			d = new SBean.DBForceSWarRank(serverName, fid, fname, icon, lvl, score);
			fid2ScoreMap.put(fid, d);
			swarGlobal.ranks.add(d);
		}
		else {
			d.serverName = serverName;
			d.score = score;
			d.fname = fname;
			d.iconId = icon;
			d.lvl = lvl;
		}
	}
	
	private void insertScoreSortList(int score, int fid)
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
		sortScoreList.add(mid, new SortScore(score, fid));
	}
	
	private void removeScoreSortList(int fid)
	{
		SBean.DBForceSWarRank d = fid2ScoreMap.get(fid);
		if (d == null)
			return;
		int index = getScoreSortList(d.score, fid);
		if (index >= 0)
			sortScoreList.remove(index);
	}
	
	private int getScoreSortList(int score, int fid)
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
			if (p.fid == fid) {
				return tmp;
			}
			tmp ++;
		}
		return -1;
	}
	
	synchronized List<SBean.SWarRank> getAllScoreRanks()
	{			
		List<SBean.SWarRank> ranks = new ArrayList<SBean.SWarRank>();
		for (SortScore s : sortScoreList) {
			SBean.DBForceSWarRank dr = fid2ScoreMap.get(s.fid);
			if (dr == null)
				continue;
			
			ranks.add(new SBean.SWarRank(0, dr.lvl, dr.serverName, new SBean.GlobalForceID(gs.getConfig().id, dr.fid), dr.fname, dr.iconId, dr.score));
		}
		
		return ranks;
	}
	
	synchronized List<ScoreRank> getScoreRanks(int fid)
	{			
		SBean.DBForceSWarRank d = fid2ScoreMap.get(fid);
		int index = -1;
		if (d != null) {
			index = getScoreSortList(d.score, fid);
		}
		
		List<ScoreRank> ranks = new ArrayList<ScoreRank>();
		for (byte i = 0; i < 50; i ++) {
			if (i >= sortScoreList.size())
				break;
			SortScore score = sortScoreList.get(i);
			SBean.DBForceSWarRank dr = fid2ScoreMap.get(score.fid);
			if (dr == null)
				continue;
			ScoreRank rank = new ScoreRank(i+1, dr.fid, dr.score, dr.iconId, dr.fname, dr.lvl);
			ranks.add(rank);
		}
		if (index >= 50) {
			if (d != null) {
				ScoreRank rank = new ScoreRank(index+1, d.fid, d.score, d.iconId, d.fname, d.lvl);
				ranks.add(rank);
			}
		}
		return ranks;
	}
	
	public synchronized void rank(int sid, Role role)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarRankRes(null));
			return;
		}
		
		rank(new SBean.SWarRankReq(0, new SBean.GlobalForceID(gs.getConfig().id, role.forceInfo.id)), role);
	}
	
	public synchronized void topRanks(int sid, Role role)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarTopRanksRes(null));
			return;
		}
		
		topRanks(new SBean.SWarTopRanksReq(0, new SBean.GlobalForceID(gs.getConfig().id, role.forceInfo.id)), role);
	}
	
	public SBean.SWarRanksRes handleRanksRequest()
	{
		List<SBean.SWarRank> ranks = getAllScoreRanks();
		return new SBean.SWarRanksRes(gs.getConfig().id, ranks);
	}
	
	public void donateMoney(int sid, Role role, int count)
	{
		if (role == null || role.forceInfo.id <= 0 || count <= 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateMoneyRes(0));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateMoneyRes(0));
			return;
		}
		
		SBean.ForceSWarBasicCFGS cfg = gs.gameData.getSWarCFG().basic;
		
		synchronized (role) {
			if (role.money < cfg.badgePrice * count) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateMoneyRes(0));
				return;
			}
			role.useMoney(cfg.badgePrice * count);
		}
		
		synchronized (this) {
			swar.attackBadges += count;
			
			SBean.DBForceSWarDonateRank rank = null;
			for (SBean.DBForceSWarDonateRank r : swar.donateRanks)
				if (r.rid == role.id) {
					rank = r;
					break;
				}
			if (rank == null) {
				rank = new SBean.DBForceSWarDonateRank(role.id, role.name, role.headIconID, role.lvl, 0, 0, 0);
				swar.donateRanks.add(rank);
			}
			rank.donate += count;
			rank.totalDonate += count;
			
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateMoneyRes(swar.attackBadges));
		}
	}
	
	public void donateItems(int sid, Role role)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateItemsRes(0));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateItemsRes(0));
			return;
		}
		
		List<SBean.ForceSWarBadgeItemCFGS> itemCfg = gs.gameData.getSWarCFG().badgeItem;
		int count = 0;
		synchronized (role) {
			for (SBean.ForceSWarBadgeItemCFGS cfg : itemCfg) {
				int icount = role.getItemCount(cfg.itemId);
				count += icount * cfg.badgeCount;
				role.delItem(cfg.itemId, icount);
			}
		}
		
		synchronized (this) {
			swar.attackBadges += count;
			
			SBean.DBForceSWarDonateRank rank = null;
			for (SBean.DBForceSWarDonateRank r : swar.donateRanks)
				if (r.rid == role.id) {
					rank = r;
					break;
				}
			if (rank == null) {
				rank = new SBean.DBForceSWarDonateRank(role.id, role.name, role.headIconID, role.lvl, 0, 0, 0);
				swar.donateRanks.add(rank);
			}
			rank.donate += count;
			rank.totalDonate += count;
			
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateItemsRes(swar.attackBadges));
		}
	}
	
	private class CompareDonate implements Comparator<SBean.DBForceSWarDonateRank>
	{

		@Override
		public int compare(SBean.DBForceSWarDonateRank rank1, SBean.DBForceSWarDonateRank rank2) {
			if (rank1 == null || rank2 == null)
				return 0;
			
			return rank1.donate - rank2.donate;
		}
		
	}
	
	private class CompareHistoryDonate implements Comparator<SBean.DBForceSWarDonateRank>
	{

		@Override
		public int compare(SBean.DBForceSWarDonateRank rank1, SBean.DBForceSWarDonateRank rank2) {
			if (rank1 == null || rank2 == null)
				return 0;
			
			return rank1.totalDonate - rank2.totalDonate;
		}
		
	}
	
	public synchronized void donateRanks(int sid, Role role, boolean history)
	{
		if (role == null || role.forceInfo.id <= 0) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateRanksRes(history, null));
			return;
		}
		
		SBean.DBForceSWar swar = swarMap.get(role.forceInfo.id);
		if (swar == null) {
			gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateRanksRes(history, null));
			return;
		}
		
		List<SBean.DBForceSWarDonateRank> ranks = new ArrayList<SBean.DBForceSWarDonateRank>();
		for (SBean.DBForceSWarDonateRank r : swar.donateRanks)
			if (history || r.donate > 0)
				ranks.add(r.kdClone());
		if (history)
			Collections.sort(ranks, new CompareHistoryDonate());
		else
			Collections.sort(ranks, new CompareDonate());
			
		gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceSWarDonateRanksRes(history, ranks));
	}
	
	public boolean addProtectDays(int fid, int add)
	{
		SBean.DBForceSWar swar = swarMap.get(fid);
		if (swar == null)
			return false;
		
		int dayDiff = gs.getDay() - swar.defendDay;
		if (swar.defendDay != 0 && dayDiff != 3)
			return false;
		
		if (add <= 0)
			return false;
		
		swar.protectDays += add;
		return true;
	}
	
	GameServer gs;
	SBean.DBForceSWarGlobal swarGlobal;
	int saveTime;
	private EventService service;
	String serverName = "Unknown";
	
	Map<Integer, SBean.DBForceSWar> swarMap = new HashMap<Integer, SBean.DBForceSWar>();
	Map<Integer, SBean.DBForceSWarMatchRecord> recordsMap = new HashMap<Integer, SBean.DBForceSWarMatchRecord>();
	List<SortScore> sortScoreList = new ArrayList<SortScore>();
	Map<Integer, SBean.DBForceSWarRank> fid2ScoreMap = new HashMap<Integer, SBean.DBForceSWarRank>();
	List<Reward> rewards = new ArrayList<Reward>();
	List<Integer> voteNotices = new ArrayList<Integer>();
	List<Integer> attackNotices = new ArrayList<Integer>();
	int globalProtectDay = 0;

	public static final int INVOKE_SUCCESS = 0;
	public static final int INVOKE_UNKNOWN_ERROR = -1;
	public static final int INVOKE_TIMEOUT = -2;
	
	public static final int INVOKE_VIST_ROLE_ERROR = -100;
	public static final int INVOKE_VIST_DBROLE_ERROR = -101;
	
	public static final long MAX_WAIT_TIME = 2000;
}

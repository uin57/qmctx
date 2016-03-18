
package i3k.exchange;


import i3k.SBean;
import i3k.SBean.DuelTopRank;
import i3k.gs.DuelManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DuelMatchManager 
{
	
	private final short REFRESH_RANK_TIME = 1390;
	private final short REFRESH_RANK_TIME_SUNDAY = 1420;
	
	class MatchSession
	{
		MatchSession(int matchId, SBean.DuelJoinReq req1, SBean.DuelJoinReq req2, int time)
		{
			this.matchId = matchId;
			this.req1 = req1;
			this.req2 = req2;
			this.time = time;
		}
		int matchId;
		SBean.DuelJoinReq req1;
		SBean.DuelJoinReq req2;
		byte result1 = 0;
		byte result2 = 0;
		int time = 0;
		int lastResponseTime1 = 0;
		int lastResponseTime2 = 0;
		boolean req1SpeedUp = false;
		boolean req2SpeedUp = false;
	}
	
	class LastOppo
	{
		LastOppo(long oppo, int time) {
			this.oppo = oppo;
			this.time = time;
		}
		
		long oppo;
		int time;
	}
	
	public DuelMatchManager(ExchangeServer es)
	{
		this.es = es;
		day = es.getDayByOffset(REFRESH_RANK_TIME);
		day2 = es.getDayByOffset(REFRESH_RANK_TIME_SUNDAY);
	}

	public synchronized void onTimer()
	{
		int newDay = es.getDayByOffset(REFRESH_RANK_TIME);
		if (newDay > day) {
			day = newDay;
			
			rankList.clear();
			mapRoleRanks.clear();
			sendRanksReq();
			
			sendGlobalRanksTime = es.getTime();
		}
		
		int newDay2 = es.getDayByOffset(REFRESH_RANK_TIME_SUNDAY);
		if (newDay2 > day2 && es.getWeekday(newDay2) == 0) {
			day2 = newDay2;
			
			rankList.clear();
			mapRoleRanks.clear();
			sendRanksReq();
			
			sendGlobalRanksTime = es.getTime();
		}
		
		int time = es.getTime();
		if (time % 600 == 0) {
			List<MatchSession> delSessions = new ArrayList<MatchSession>();
			for (MatchSession session : sessions.values()) {
				if (session.time < time - 1200)
					delSessions.add(session);
			}
			
			for (MatchSession s : delSessions)
				sessions.remove(s.matchId);
		}
		
		if (sendGlobalRanksTime > 0 && time - sendGlobalRanksTime > 10) {
			sendGlobalRanksTime = 0;
			
			for (Map.Entry<Integer, SBean.DuelGlobalRanksRes> e : mapGlobalRanks.entrySet()) {
				int serverId = e.getKey();
				List<SBean.DuelGlobalRank> ranks = e.getValue().ranks;
				for (SBean.DuelGlobalRank r : ranks) {
					long key = ((long)serverId << 32) | r.roleId;
					SBean.DuelRank rank = mapRoleRanks.get(key);
					if (rank != null)
						r.rank = rank.rank;
				}
				
				sendGlobalRanksRes(serverId, e.getValue());
			}
		}
	}
	
	public synchronized void join(SBean.DuelJoinReq req)
	{
		SBean.DuelJoinReq jr = null;
		int matchId = 0;
		List<SBean.DuelJoinReq> reqs = joinReqs.get(req.type);
		if (reqs == null) {
			reqs = new LinkedList<SBean.DuelJoinReq>();
			joinReqs.put(req.type, reqs);
		}

		SBean.DuelJoinReq selfReq = null;
		long key1 = ((long)req.roleID.serverID << 32) | req.roleID.roleID;
		Map<Long, LastOppo> lastOppo = mapLastOppo.get(key1);
		int time = es.getTime();
		int var = 1;
		
		while (jr == null) {
			int reqLevel = req.level;
			if (reqLevel == 100)
				reqLevel = 0;
			int levelMin = reqLevel - var;
			int levelMax = reqLevel + var;
			if (levelMin < 0)
				levelMin = 0;
			if (levelMax > 10)
				levelMax = 10;
			
			for (SBean.DuelJoinReq r : reqs) {
				if (r.roleID.roleID != req.roleID.roleID || r.roleID.serverID != req.roleID.serverID) {
					int curLevel = r.level;
					if (curLevel == 100)
						curLevel = 0;
					
					if (curLevel >= levelMin && curLevel <= levelMax) {
						long key2 = ((long)r.roleID.serverID << 32) | r.roleID.roleID;
						boolean found = false;
						if (lastOppo != null) {
							List<LastOppo> delOppos = new ArrayList<LastOppo>();
							for (LastOppo oppo : lastOppo.values())
								if (oppo.time + 86400 > time) {
									if (oppo.oppo == key2) {
										found = true;
										break;
									}
								}
								else
									delOppos.add(oppo);
							for (LastOppo oppo : delOppos)
								lastOppo.remove(oppo);
						}
						if (!found) {
							if (jr != null) {
								int abs1 = Math.abs(jr.winTimes - req.winTimes);
								int abs2 = Math.abs(r.winTimes - req.winTimes);
								if (abs2 < abs1)
									jr = r;
							}
							else
								jr = r;
						}
					}
				}
				else
					selfReq = r;
			}
			if (levelMin == 0 && levelMax == 10)
				break;
			var ++;
		}
		
		if (jr == null) {
			if (selfReq != null) {
				selfReq.id = req.id;
				selfReq.type = req.type;
				selfReq.level = req.level;
				selfReq.time = req.time;
			}
			else
				reqs.add(req);
		}
		else {
			matchIndex ++;
			MatchSession session = new MatchSession(matchIndex, jr, req, es.getTime());
			sessions.put(session.matchId, session);
			reqs.remove(jr);
			matchId = session.matchId;
			es.getLogger().warn("duel match created, rid1=" + req.roleID.roleID + ", server1=" + req.roleID.serverID + ", rid2=" + jr.roleID.roleID + ", server2=" + jr.roleID.serverID);
		}
		
		if (jr != null) {
			sendJoinRes(new SBean.DuelJoinRes(req.id, req.roleID, req.type, DuelManager.INVOKE_SUCCESS, matchId, (byte)1, jr.roleID, jr.roleName, jr.serverName, jr.headIconId, jr.level));
			sendJoinRes(new SBean.DuelJoinRes(jr.id, jr.roleID, jr.type, DuelManager.INVOKE_SUCCESS, matchId, (byte)0, req.roleID, req.roleName, req.serverName, req.headIconId, req.level));
		}
	}
	
	public synchronized void cancelJoin(SBean.DuelCancelJoinReq req)
	{
		SBean.DuelJoinReq jr = null;
		List<SBean.DuelJoinReq> reqs = joinReqs.get(req.type);
		if (reqs == null) 
			return;

		for (SBean.DuelJoinReq r : reqs) {
			if (r.roleID.roleID == req.roleID.roleID && r.roleID.serverID == req.roleID.serverID) {
				jr = r;
				break;
			}
		}

		if (jr != null) 
			reqs.remove(jr);
	}
	
	private SBean.DuelJoinReq getSessionRole(MatchSession session, SBean.GlobalRoleID roleID, int newId)
	{
		SBean.DuelJoinReq req = null;
		
		if (roleID.roleID == session.req1.roleID.roleID && roleID.serverID == session.req1.roleID.serverID) {
			req = session.req1;
			if (newId > 0)
				session.lastResponseTime1 = es.getTime();
		}
		else if (roleID.roleID == session.req2.roleID.roleID && roleID.serverID == session.req2.roleID.serverID) {
			req = session.req2;
			if (newId > 0)
				session.lastResponseTime2 = es.getTime();
		}
		
		if (req != null && newId >= 0)
			req.id = newId;
		
		return req;
	}
	
	private boolean checkSessionRoles(MatchSession session, SBean.GlobalRoleID roleID, SBean.GlobalRoleID oppoRoleID, int newId)
	{
		SBean.DuelJoinReq selfReq = getSessionRole(session, roleID, newId);
		SBean.DuelJoinReq oppoReq = getSessionRole(session, oppoRoleID, -1);
		
		if (selfReq == null || oppoReq == null || selfReq == oppoReq)
			return false;
		
		return true;
	}
	
	public synchronized void selectGeneral(SBean.DuelSelectGeneralReq req)
	{
		MatchSession session = sessions.get(req.matchId);
		if (session == null) {
			sendSelectGeneralRes(new SBean.DuelSelectGeneralRes(req.id, req.roleID, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId));
			return;
		}

		if (!checkSessionRoles(session, req.roleID, req.oppoRoleID, req.id)) {
			sendSelectGeneralRes(new SBean.DuelSelectGeneralRes(req.id, req.roleID, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId));
			return;
		}
		
		sendSelectGeneralForwardReq(req);
	}
	
	public synchronized void selectGeneralForward(SBean.DuelSelectGeneralRes res)
	{
		MatchSession session = sessions.get(res.matchId);
		if (session == null)
			return;

		if (getSessionRole(session, res.roleID, -1) == null)
			return;

		sendSelectGeneralRes(res);
	}
	
	public synchronized void startAttack(SBean.DuelStartAttackReq req)
	{
		MatchSession session = sessions.get(req.matchId);
		if (session == null) {
			sendStartAttackRes(new SBean.DuelStartAttackRes(req.id, req.roleID, req.index, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId));
			return;
		}

		if (!checkSessionRoles(session, req.roleID, req.oppoRoleID, req.id)) {
			sendStartAttackRes(new SBean.DuelStartAttackRes(req.id, req.roleID, req.index, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId));
			return;
		}

		sendStartAttackForwardReq(req);
	}
	
	public synchronized void startAttackForward(SBean.DuelStartAttackRes res)
	{
		MatchSession session = sessions.get(res.matchId);
		if (session == null)
			return;

		if (getSessionRole(session, res.roleID, -1) == null)
			return;

		session.result1 = session.result2 = 0;
		
		sendStartAttackRes(res);
	}
	
	public synchronized void finishAttack(SBean.DuelFinishAttackReq req)
	{
		boolean finished = false;
		byte result = 0;
		byte reqOwner = 0;
		SBean.DuelJoinReq oppoReq = null;
		
		MatchSession session = sessions.get(req.matchId);
		if (session == null) {
			sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}

		if (!checkSessionRoles(session, req.roleID, req.oppoRoleID, req.id)) {
			sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}

		if (session.req1 == getSessionRole(session, req.roleID, -1)) {
			session.result1 = (byte)((req.result > 0)?1:2);
			reqOwner = 1;
			oppoReq = session.req2;
			if (req.result == -1) {
			    session.req1SpeedUp = true;
			}
		}
		else {
			session.result2 = (byte)((req.result > 0)?2:1);
			reqOwner = 2;
			oppoReq = session.req1;
			if (req.result == -1) {
			    session.req2SpeedUp = true;
			}
		}

		if (session.req1 == getSessionRole(session, req.roleID, -1) && session.req2SpeedUp) {
		    sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, DuelManager.INVOKE_OPPO_SPEEDUP));
            return;
		}
		if (session.req2 == getSessionRole(session, req.roleID, -1) && session.req1SpeedUp) {
		    sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, DuelManager.INVOKE_OPPO_SPEEDUP));
            return;
		}
		
		if (session.result1 > 0 && session.result2 > 0) {
			finished = true;
			if (session.result1 == session.result2)
				result = session.result1;
		}
		
		if (oppoReq == null) {
			sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, req.result, DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}
		
		if (finished) {
			if (result > 0 && reqOwner > 0) {
				sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, (byte)((result == reqOwner)?1:0), DuelManager.INVOKE_SUCCESS));
				sendFinishAttackRes(new SBean.DuelFinishAttackRes(oppoReq.id, oppoReq.roleID, req.roleID, req.index, (byte)((result == reqOwner)?0:1), DuelManager.INVOKE_SUCCESS));
			}
			else {
				sendFinishAttackRes(new SBean.DuelFinishAttackRes(req.id, req.roleID, req.oppoRoleID, req.index, (byte)0, DuelManager.INVOKE_UNKNOWN_ERROR));
				sendFinishAttackRes(new SBean.DuelFinishAttackRes(oppoReq.id, oppoReq.roleID, req.roleID, req.index, (byte)0, DuelManager.INVOKE_UNKNOWN_ERROR));
			}
			int time = es.getTime();
			long key1 = ((long)req.roleID.serverID << 32) | req.roleID.roleID;
			long key2 = ((long)oppoReq.roleID.serverID << 32) | oppoReq.roleID.roleID;
			Map<Long, LastOppo> lastOppos = mapLastOppo.get(key1);
			if (lastOppos == null) {
				lastOppos = new HashMap<Long, LastOppo>();
				mapLastOppo.put(key1, lastOppos);
			}
			lastOppos.put(key2, new LastOppo(key2, time));
			lastOppos = mapLastOppo.get(key2);
			if (lastOppos == null) {
				lastOppos = new HashMap<Long, LastOppo>();
				mapLastOppo.put(key2, lastOppos);
			}
			lastOppos.put(key1, new LastOppo(key1, time));
		}
	}
	
	public synchronized void oppoGiveup(SBean.DuelOppoGiveupReq req)
	{
		MatchSession session = sessions.get(req.matchId);
		if (session == null) {
			sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}

		if (!checkSessionRoles(session, req.roleID, req.oppoRoleID, req.id)) {
			sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}

		/*
		if (session.req1 == getSessionRole(session, req.roleID, -1)) {
			if (session.lastResponseTime2 == 0) {
				sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_JOIN_LEAVE));
				return;
			}
		}
		else {
			if (session.lastResponseTime1 == 0) {
				sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_JOIN_LEAVE));
				return;
			}
		}
		*/
		
		sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID, req.oppoRoleID, DuelManager.INVOKE_SUCCESS));
		sendOppoGiveupForward(new SBean.DuelOppoGiveupRes(req.id, req.roleID, req.oppoRoleID, DuelManager.INVOKE_SUCCESS));
		int time = es.getTime();
		long key1 = ((long)req.roleID.serverID << 32) | req.roleID.roleID;
		long key2 = ((long)req.oppoRoleID.serverID << 32) | req.oppoRoleID.roleID;
		Map<Long, LastOppo> lastOppos = mapLastOppo.get(key1);
		if (lastOppos == null) {
			lastOppos = new HashMap<Long, LastOppo>();
			mapLastOppo.put(key1, lastOppos);
		}
		lastOppos.put(key2, new LastOppo(key2, time));
		lastOppos = mapLastOppo.get(key2);
		if (lastOppos == null) {
			lastOppos = new HashMap<Long, LastOppo>();
			mapLastOppo.put(key2, lastOppos);
		}
		lastOppos.put(key1, new LastOppo(key1, time));
	}
	
	public synchronized void finishAttackOnOppoDownLine(SBean.DuelFinishAttackOnOppoDownLineReq req)
    {
        MatchSession session = sessions.get(req.matchId);
        if (session == null) {
            sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_UNKNOWN_ERROR));
            return;
        }

        if (!checkSessionRoles(session, req.roleID, req.oppoRoleID, req.id)) {
            sendOppoGiveupRes(new SBean.DuelOppoGiveupRes(req.id, req.roleID,req.oppoRoleID,  DuelManager.INVOKE_UNKNOWN_ERROR));
            return;
        }
        sendFinishAttackOnOppoDownLineRes(new SBean.DuelFinishAttackOnOppoDownLineRes(req.id, req.roleID, req.oppoRoleID, (byte)(req.attackResult > 0 ? 1 : 0), DuelManager.INVOKE_SUCCESS));
        sendFinishAttackOnOppoDownLineForward(new SBean.DuelFinishAttackOnOppoDownLineRes(req.id, req.roleID, req.oppoRoleID, (byte)(req.attackResult > 0 ? 0 : 1), DuelManager.INVOKE_SUCCESS));
    }
	
	public synchronized void ranks(SBean.DuelRanksRes res)
	{
		int index = 0;
		
		SBean.DuelGlobalRanksRes globalRanks = new SBean.DuelGlobalRanksRes(new ArrayList<SBean.DuelGlobalRank>());
		mapGlobalRanks.put(res.serverId, globalRanks);
		
		for (SBean.DuelRank r : res.ranks) {
			globalRanks.ranks.add(new SBean.DuelGlobalRank(r.roleID.roleID, 0));
			
			long key = ((long)r.roleID.serverID << 32) | r.roleID.roleID;
			mapRoleRanks.put(key, r);
			
			boolean insert = false;
			while (index < rankList.size()) {
				SBean.DuelRank rank = rankList.get(index);
				if (rank.score < r.score) {
					rankList.add(index, r);
					insert = true;
					r.rank = index + 1;
					index ++;
					break;
				}
				rank.rank = index + 1;
				index ++;
			}
			
			if (!insert) {
				rankList.add(r);
				r.rank = index + 1;
				index ++;
			}
		}
		
		while (index < rankList.size()) {
			SBean.DuelRank rank = rankList.get(index);
			rank.rank = index + 1;
			index ++;
		}
	}
	
	public synchronized void rank(SBean.DuelRankReq req)
	{
		long key = ((long)req.roleID.serverID << 32) | req.roleID.roleID;
		SBean.DuelRank r = mapRoleRanks.get(key);
		if (r != null)
			sendRankRes(new SBean.DuelRankRes(req.id, req.roleID, r.rank, DuelManager.INVOKE_SUCCESS));
		else
			sendRankRes(new SBean.DuelRankRes(req.id, req.roleID, 0, DuelManager.INVOKE_UNKNOWN_ERROR));
	}
	
	public synchronized void topRanks(SBean.DuelTopRanksReq req)
	{
		int index = 0;
		List<DuelTopRank> top = new ArrayList<DuelTopRank>();
		for (SBean.DuelRank r : rankList) {
			top.add(new SBean.DuelTopRank(r.roleID, r.rname, r.serverName, r.headIconId, r.score, r.rank));
			index ++;
			if (index >= 50)
				break;
		}
		sendTopRanksRes(new SBean.DuelTopRanksRes(req.id, req.roleID, top, DuelManager.INVOKE_SUCCESS));
	}
	
	public synchronized void onServerArrived(int serverId)
	{
		List<SBean.DuelRank> delRanks = new ArrayList<SBean.DuelRank>();
		for (SBean.DuelRank r : rankList) {
			if (r.roleID.serverID == serverId)
				delRanks.add(r);
		}
		
		for (SBean.DuelRank r : delRanks) {
			rankList.remove(r);
			
			long key = ((long)r.roleID.serverID << 32) | r.roleID.roleID;
			mapRoleRanks.remove(key);
		}
		
		sendRanksReq(serverId);
	}
	
	public void sendJoinRes(SBean.DuelJoinRes res)
	{
		es.getRPCManager().sendDuelJoinRes(res.roleID.serverID, res);
	}
	
	public void sendSelectGeneralRes(SBean.DuelSelectGeneralRes res)
	{
		es.getRPCManager().sendDuelSelectGeneralRes(res.roleID.serverID, res);
	}
	
	public void sendSelectGeneralForwardReq(SBean.DuelSelectGeneralReq req)
	{
		es.getRPCManager().sendDuelSelectGeneralForwardReq(req.oppoRoleID.serverID, req);
	}
	
	public void sendStartAttackRes(SBean.DuelStartAttackRes res)
	{
		es.getRPCManager().sendDuelStartAttackRes(res.roleID.serverID, res);
	}
	
	public void sendStartAttackForwardReq(SBean.DuelStartAttackReq req)
	{
		es.getRPCManager().sendDuelStartAttackForwardReq(req.oppoRoleID.serverID, req);
	}
	
	public void sendFinishAttackRes(SBean.DuelFinishAttackRes res)
	{
		es.getRPCManager().sendDuelFinishAttackRes(res.roleID.serverID, res);
	}
	
	public void sendOppoGiveupRes(SBean.DuelOppoGiveupRes res)
	{
		es.getRPCManager().sendDuelOppoGiveupRes(res.roleID.serverID, res);
	}
	
	public void sendFinishAttackOnOppoDownLineRes(SBean.DuelFinishAttackOnOppoDownLineRes res)
    {
        es.getRPCManager().sendDuelFinishAttackOnOppoDownLineRes(res.roleID.serverID, res);
    }
	
	public void sendOppoGiveupForward(SBean.DuelOppoGiveupRes res)
	{
		es.getRPCManager().sendDuelOppoGiveupForward(res.oppoRoleID.serverID, res);
	}
	
	public void sendFinishAttackOnOppoDownLineForward(SBean.DuelFinishAttackOnOppoDownLineRes res)
    {
        es.getRPCManager().sendDuelFinishAttackOnOppoDownLineForward(res.oppoRoleID.serverID, res);
    }
	
	public void sendRankRes(SBean.DuelRankRes res)
	{
		es.getRPCManager().sendDuelRankRes(res.roleID.serverID, res);
	}
	
	public void sendTopRanksRes(SBean.DuelTopRanksRes res)
	{
		es.getRPCManager().sendDuelTopRanksRes(res.roleID.serverID, res);
	}
	
	public void sendRanksReq(int serverId)
	{
		es.getRPCManager().sendDuelRanksReq(serverId);
	}
	
	public void sendRanksReq()
	{
		es.getRPCManager().sendDuelRanksReqToAllServers();
	}
	
	public void sendGlobalRanksRes(int serverId, SBean.DuelGlobalRanksRes res)
	{
		es.getRPCManager().sendDuelGlobalRanksRes(serverId, res);
	}
	
	ExchangeServer es;
	Map<Byte, List<SBean.DuelJoinReq>> joinReqs = new TreeMap<Byte, List<SBean.DuelJoinReq>>();
	Map<Integer, MatchSession> sessions = new HashMap<Integer, MatchSession>();
	int matchIndex = 0;
	LinkedList<SBean.DuelRank> rankList = new LinkedList<SBean.DuelRank>();
	Map<Long, SBean.DuelRank> mapRoleRanks = new HashMap<Long, SBean.DuelRank>();
	int day = 0;
	int day2 = 0;
	Map<Integer, SBean.DuelGlobalRanksRes> mapGlobalRanks = new HashMap<Integer, SBean.DuelGlobalRanksRes>();
	int sendGlobalRanksTime = 0;
	Map<Long, Map<Long, LastOppo>> mapLastOppo = new HashMap<Long, Map<Long, LastOppo>>();
	
}

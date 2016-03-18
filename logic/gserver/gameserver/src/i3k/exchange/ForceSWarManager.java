
package i3k.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import i3k.SBean;

public class ForceSWarManager 
{
	
	private final short REFRESH_RANK_TIME = 600;
	
	class Search
	{
		SBean.SWarSearchReq req;
		SBean.SWarSearchRes res;
		int retry = 0;
	}
	
	public ForceSWarManager(ExchangeServer es)
	{
		this.es = es;
		day = es.getDayByOffset(REFRESH_RANK_TIME);
	}

	public synchronized void onTimer()
	{
		int newDay = es.getDayByOffset(REFRESH_RANK_TIME);
		if (newDay > day) {
			day = newDay;
			
			rankList.clear();
			mapForceRanks.clear();
			sendRanksReq();
			
			sendGlobalRanksTime = es.getTime();
		}
		
		int time = es.getTime();
		if (sendGlobalRanksTime > 0 && time - sendGlobalRanksTime > 10) {
			sendGlobalRanksTime = 0;
			
			for (Map.Entry<Integer, SBean.SWarGlobalRanksRes> e : mapGlobalRanks.entrySet()) {
				int serverId = e.getKey();
				List<SBean.SWarGlobalRank> ranks = e.getValue().ranks;
				for (SBean.SWarGlobalRank r : ranks) {
					long key = ((long)serverId << 32) | r.forceId;
					SBean.SWarRank rank = mapForceRanks.get(key);
					if (rank != null)
						r.rank = rank.rank;
				}
				
				sendGlobalRanksRes(serverId, e.getValue());
			}
		}
	}
	
	public synchronized void search(int serverID1, int serverID2, SBean.SWarSearchReq req)
	{
		long key = ((long)req.forceID.serverID << 32) | req.forceID.forceID;
		Search s = new Search();
		s.req = req;
		s.retry = 0;
		searches.put(key, s);
		
		es.getRPCManager().sendSWarSearchForwardReq(serverID1, req);
		es.getRPCManager().sendSWarSearchForwardReq(serverID2, req);
	}
	
	public synchronized void searchForward(int serverID, SBean.SWarSearchRes res)
	{
		long key = ((long)res.forceID.serverID << 32) | res.forceID.forceID;
		Search s = searches.get(key);
		if (s == null)
			return;
		
		boolean retry = false;
		
		if (res.error == 0 && res.oppos.size() > 0) {
			SBean.DBForceSWarBrief brief = res.oppos.get(0);
			if (brief.serverId == res.forceID.serverID && brief.fid == res.forceID.forceID) {
				retry = true;
			}
			else if (s.res == null) {
				s.res = res;
			}
			else if (s.res.oppos.size() > 0) {
				SBean.DBForceSWarBrief sbrief = s.res.oppos.get(0);
				if (brief.serverId == sbrief.serverId && brief.fid == sbrief.fid)
					retry = true;
				else {
					s.res.oppos.add(brief);
					searches.remove(key);
					es.getRPCManager().sendSWarSearchRes(res.forceID.serverID, s.res);
				}
			}
			else 
				s.res.oppos.add(brief);
		}
		else
			retry = true;
		
		if (retry) {
			for (SBean.DBForceSWarBrief oppo : res.oppos)
				es.getRPCManager().sendSWarReleaseReq(oppo.serverId, new SBean.SWarReleaseReq(new SBean.GlobalForceID(oppo.serverId, oppo.fid)));
			s.retry ++;
			if (s.retry <= 5) {
				if (s.res != null && s.res.oppos.size() > 0) {
					SBean.DBForceSWarBrief brief = s.res.oppos.get(0);
					s.req.skipForceID = new SBean.GlobalForceID(brief.serverId, brief.fid);
				}
				es.getRPCManager().sendSWarSearchForwardReq(serverID, s.req);
			}
			else {
				if (s.res != null)
					for (SBean.DBForceSWarBrief oppo : s.res.oppos)
						es.getRPCManager().sendSWarReleaseReq(oppo.serverId, new SBean.SWarReleaseReq(new SBean.GlobalForceID(oppo.serverId, oppo.fid)));
				searches.remove(key);

				es.getRPCManager().sendSWarSearchRes(res.forceID.serverID, new SBean.SWarSearchRes(res.id, res.forceID, new ArrayList<SBean.DBForceSWarBrief>(), i3k.gs.ForceSWarManager.INVOKE_TIMEOUT));
			}
		}
	}
	
	public synchronized void attack(SBean.SWarAttackReq req)
	{
		es.getRPCManager().sendSWarAttackForwardReq(req.oppoForceID.serverID, req);
	}
	
	public synchronized void attackForward(SBean.SWarAttackRes res)
	{
		es.getRPCManager().sendSWarAttackRes(res.forceID.serverID, res);
	}
	
	public synchronized void ranks(SBean.SWarRanksRes res)
	{
		int index = 0;
		
		SBean.SWarGlobalRanksRes globalRanks = new SBean.SWarGlobalRanksRes(new ArrayList<SBean.SWarGlobalRank>());
		mapGlobalRanks.put(res.serverId, globalRanks);
		
		for (SBean.SWarRank r : res.ranks) {
			globalRanks.ranks.add(new SBean.SWarGlobalRank(r.forceID.forceID, 0));
			
			long key = ((long)r.forceID.serverID << 32) | r.forceID.forceID;
			mapForceRanks.put(key, r);
			
			boolean insert = false;
			while (index < rankList.size()) {
				SBean.SWarRank rank = rankList.get(index);
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
			SBean.SWarRank rank = rankList.get(index);
			rank.rank = index + 1;
			index ++;
		}
	}
	
	public synchronized void rank(SBean.SWarRankReq req)
	{
		long key = ((long)req.forceID.serverID << 32) | req.forceID.forceID;
		SBean.SWarRank r = mapForceRanks.get(key);
		if (r != null)
			sendRankRes(new SBean.SWarRankRes(req.id, req.forceID, r.rank, i3k.gs.ForceSWarManager.INVOKE_SUCCESS));
		else
			sendRankRes(new SBean.SWarRankRes(req.id, req.forceID, 0, i3k.gs.ForceSWarManager.INVOKE_UNKNOWN_ERROR));
	}
	
	public synchronized void topRanks(SBean.SWarTopRanksReq req)
	{
		int index = 0;
		List<SBean.SWarTopRank> top = new ArrayList<SBean.SWarTopRank>();
		for (SBean.SWarRank r : rankList) {
			top.add(new SBean.SWarTopRank(r.forceID, r.fname, r.serverName, r.iconId, r.score, r.rank, r.lvl));
			index ++;
			if (index >= 50)
				break;
		}
		sendTopRanksRes(new SBean.SWarTopRanksRes(req.id, req.forceID, top, i3k.gs.ForceSWarManager.INVOKE_SUCCESS));
	}
	
	public synchronized void voteForward(SBean.SWarVoteReq req)
	{
		es.getRPCManager().sendSWarVoteReq(req.oppoForceID.serverID, req);
		es.getRPCManager().sendSWarReleaseReq(req.releaseForceID.serverID, new SBean.SWarReleaseReq(req.releaseForceID));
	}
	
	public void sendRankRes(SBean.SWarRankRes res)
	{
		es.getRPCManager().sendSWarRankRes(res.forceID.serverID, res);
	}
	
	public void sendTopRanksRes(SBean.SWarTopRanksRes res)
	{
		es.getRPCManager().sendSWarTopRanksRes(res.forceID.serverID, res);
	}
	
	public void sendRanksReq(int serverId)
	{
		es.getRPCManager().sendSWarRanksReq(serverId);
	}
	
	public void sendRanksReq()
	{
		es.getRPCManager().sendSWarRanksReqToAllServers();
	}
	
	public void sendGlobalRanksRes(int serverId, SBean.SWarGlobalRanksRes res)
	{
		es.getRPCManager().sendSWarGlobalRanksRes(serverId, res);
	}
	
	ExchangeServer es;
	Map<Long, Search> searches = new HashMap<Long, Search>();
	LinkedList<SBean.SWarRank> rankList = new LinkedList<SBean.SWarRank>();
	Map<Long, SBean.SWarRank> mapForceRanks = new HashMap<Long, SBean.SWarRank>();
	Map<Integer, SBean.SWarGlobalRanksRes> mapGlobalRanks = new HashMap<Integer, SBean.SWarGlobalRanksRes>();
	int day = 0;
	int sendGlobalRanksTime = 0;
	
}


package i3k.exchange;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import i3k.SBean;
import i3k.SBean.ExpiratBossGlobalRank;
import i3k.SBean.ExpiratBossGlobalRanksRes;
import i3k.SBean.ExpiratBossGlobalRanksRes2;
import i3k.SBean.ExpiratBossRank;
import i3k.SBean.ExpiratBossRankReq;
import i3k.SBean.ExpiratBossRankRes;
import i3k.SBean.ExpiratBossRanks;
import i3k.SBean.ExpiratBossTopRank;
import i3k.gs.DuelManager;

public class ExpiratBossManager
{
	
	private final short REFRESH_RANK_TIME = 1364;//1438
	public static int SAVE_INTERVAL = 10* 60;
			
	public ExpiratBossManager(ExchangeServer es)
	{
		this.es = es;
		day = es.getDayByOffset(REFRESH_RANK_TIME);
		saveTime=es.getTime();
	}

	public synchronized void onTimer()
	{
		int newDay = es.getDayByOffset(REFRESH_RANK_TIME);
		
		if (newDay > day) {
			day = newDay;
			
			rankList.clear();
			mapRoleRanks.clear();
			mapGlobalRanks.clear();
			sendRanksReq();
			
			sendGlobalRanksTime = es.getTime();
			bonus = true;
		}
				
		int time = es.getTime();
		if (time >= saveTime + SAVE_INTERVAL) {//time % 600 == 0||
			rankList.clear();
			mapRoleRanks.clear();
			sendRanksReq();
			sendGlobalRanksTime = es.getTime();
			saveTime = time;
			es.getLogger().warn("ExpiratBossManager onTimer" );
		}
		
		if (sendGlobalRanksTime > 0 && time - sendGlobalRanksTime > 20) {
			sendGlobalRanksTime = 0;
			
			
			Map<Short,List<ExpiratBossTopRank>> topMap = new HashMap<Short,List<ExpiratBossTopRank>>();
			for (Entry<Short, LinkedList<ExpiratBossRank>> rl : rankList.entrySet()) {
				List<ExpiratBossTopRank> top = new ArrayList<ExpiratBossTopRank>();
				LinkedList<ExpiratBossRank> rankList1 =rl.getValue();
				int index = 0;
				for (ExpiratBossRank r : rankList1) {
					
					top.add(new SBean.ExpiratBossTopRank(r.roleID, r.rname, r.serverName, r.headIconId, r.damage, r.rank));	
					index ++;
		
					if (index >= 100)
						break;
				}
				es.getLogger().warn("ExpiratBossManager onTimer count" +top.size());
				topMap.put(rl.getKey(), top);
			}
			
			
//			for (Entry<Short, Map<Integer, ExpiratBossGlobalRanksRes>> e1 : mapGlobalRanks.entrySet()) {
//				Map<Integer, ExpiratBossGlobalRanksRes>  el =e1.getValue();
////				Map<Long, ExpiratBossRank> mapRoleRanksi =mapRoleRanks.get(e1.getKey());
//				for(Entry<Integer, ExpiratBossGlobalRanksRes> e : el.entrySet()){
//					int serverId = e.getKey();
//					SBean.ExpiratBossTopRanksRes restop = new SBean.ExpiratBossTopRanksRes();
//					restop.combatId=e1.getKey();
//					restop.ranks=topMap.get(e1.getKey());
//					sendGlobalRanksRes(serverId,restop);
//			    }
//			
//		    }
			
			for(Entry<Short, List<ExpiratBossTopRank>> rankTop : topMap.entrySet()){
				SBean.ExpiratBossTopRanksRes restop = new SBean.ExpiratBossTopRanksRes();
				restop.combatId=rankTop.getKey();
				restop.ranks=topMap.get(rankTop.getKey());
				sendGlobalRanksRes(0,restop);
			}
			
			if(bonus){
				
				for (Entry<Integer, Map<Short, ExpiratBossGlobalRanksRes>> e1 : mapGlobalRanks.entrySet()) {
					Map<Short, ExpiratBossGlobalRanksRes>  el =e1.getValue();
					int serverId =e1.getKey();
					ExpiratBossGlobalRanksRes2 expiratBossGlobalRanksResList = new ExpiratBossGlobalRanksRes2(
							new ArrayList<ExpiratBossGlobalRanksRes>());
					for(Entry<Short, ExpiratBossGlobalRanksRes> e  : el.entrySet()){
						short combatId = e.getKey();
						
						if(e.getValue()!=null){
							e.getValue().rankType=combatId;
							List<ExpiratBossGlobalRank> ranks = e.getValue().ranks;
							for (ExpiratBossGlobalRank r : ranks) {
								long key = ((long)serverId << 32) | r.roleId;
								Map<Long, ExpiratBossRank> rankl = mapRoleRanks.get(combatId);
								
								if(rankl!=null){
									ExpiratBossRank rank = rankl.get(key);
									if (rank != null)
										r.rank = rank.rank;
								}							
							}
							
							expiratBossGlobalRanksResList.ranks2.add(e.getValue());
//							sendGlobalRanksResBonus(serverId, e.getValue());
						}
						
					}
					
					sendGlobalRanksResBonus2(serverId, expiratBossGlobalRanksResList);
				}
				bonus=false;
			}

		}
		
		
		
	}
	
	public synchronized void ranks(SBean.ExpiratBossRanksRes res)
	{
	
		//Map<Integer, LinkedList<ExpiratBossRank>> mapGlobalRanksit = new HashMap<Integer, LinkedList<ExpiratBossRank>>();
		Map<Short, SBean.ExpiratBossGlobalRanksRes> mapGlobalRanksTemp = new HashMap<Short, SBean.ExpiratBossGlobalRanksRes>();
		mapGlobalRanks.put(res.serverId, mapGlobalRanksTemp);
			
		List<ExpiratBossRanks> exi = res.ranksList;
		for(ExpiratBossRanks ranksl: exi){
			
			SBean.ExpiratBossGlobalRanksRes globalRanks = new SBean.ExpiratBossGlobalRanksRes(ranksl.type,new ArrayList<SBean.ExpiratBossGlobalRank>());
			mapGlobalRanksTemp.put(ranksl.type, globalRanks);
			
			for (ExpiratBossRank r : ranksl.ranks) {
				int index = 0;
				globalRanks.ranks.add(new SBean.ExpiratBossGlobalRank(r.roleID.roleID, 0));
				
				long key = ((long)r.roleID.serverID << 32) | r.roleID.roleID;
				
				if(mapRoleRanks.get(ranksl.type)==null){
					mapRoleRanks.put(ranksl.type, new HashMap<Long, ExpiratBossRank>());
				}
				mapRoleRanks.get(ranksl.type).put(key, r);
				
				boolean insert = false;
				
				if(rankList.get(ranksl.type)==null){
					rankList.put(ranksl.type, new LinkedList<ExpiratBossRank>());
				}
				
				LinkedList<ExpiratBossRank> rankList1 =rankList.get(ranksl.type);
					
					while (index < rankList1.size()) {
						ExpiratBossRank rank = rankList1.get(index);
						if (rank.damage < r.damage) {
							rankList1.add(index, r);
							insert = true;
							r.rank = index + 1;
							index ++;
							break;
						}
						rank.rank = index + 1;
						index ++;
					}
					
					if (!insert) {
						rankList1.add(r);
						r.rank = index + 1;
						index ++;
					}
					while (index < rankList1.size()) {
						ExpiratBossRank rank = rankList1.get(index);
						rank.rank = index + 1;
						index ++;
					}
				}			
		}
		
		
	}
	
	public synchronized void rank(ExpiratBossRankReq req)
	{
		
		Map<Long, ExpiratBossRank> mapRoleRanksi = mapRoleRanks.get(req.type);
		
		if(mapRoleRanksi==null){
			sendRankRes(new ExpiratBossRankRes(req.id, req.roleID, 0, DuelManager.INVOKE_UNKNOWN_ERROR));
			return;
		}
		long key = ((long)req.roleID.serverID << 32) | req.roleID.roleID;
		ExpiratBossRank r = mapRoleRanksi.get(key);
		if (r != null)
			sendRankRes(new ExpiratBossRankRes(req.id, req.roleID, r.rank, DuelManager.INVOKE_SUCCESS));
		else
			sendRankRes(new ExpiratBossRankRes(req.id, req.roleID, 0, DuelManager.INVOKE_UNKNOWN_ERROR));
	}
	
	public synchronized void topRanks(SBean.DuelTopRanksReq req)
	{
//		int index = 0;
//		List<ExpiratBossTopRank> top = new ArrayList<ExpiratBossTopRank>();
//		
//		for (ExpiratBossRank r : rankList) {
//			top.add(new SBean.ExpiratBossTopRank(r.roleID, r.rname, r.serverName, r.headIconId, r.score, r.rank));
//			index ++;
//			if (index >= 100)
//				break;
//		}
//		sendTopRanksRes(new SBean.DuelTopRanksRes(req.id, req.roleID, top, DuelManager.INVOKE_SUCCESS));
	}
	
	public synchronized void onServerArrived(int serverId)
	{
		List<ExpiratBossRank> delRanks = new ArrayList<ExpiratBossRank>();
		for (Entry<Short, LinkedList<ExpiratBossRank>> rl : rankList.entrySet()) {
			LinkedList<ExpiratBossRank> rankList =rl.getValue();
			for (ExpiratBossRank r : rankList) {
				if (r.roleID.serverID == serverId)
					delRanks.add(r);
			}
			
			for (ExpiratBossRank r : delRanks) {
				rankList.remove(r);
			
			long key = ((long)r.roleID.serverID << 32) | r.roleID.roleID;
			mapRoleRanks.remove(key);
		}		
		}
		
		sendRanksReq(serverId);
	}
	
	public void sendRankRes(ExpiratBossRankRes res)
	{
		es.getRPCManager().sendExpiratBossRankRes(res.roleID.serverID, res);
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
		es.getRPCManager().sendExpiratBossRanksReqToAllServers();
	}
		
	public void sendGlobalRanksRes(int serverId, SBean.ExpiratBossTopRanksRes res)
	{
		es.getRPCManager().sendExpiratBossGlobalRanksRes(serverId, res);
	}
	
	public void sendGlobalRanksResBonus(int serverId, ExpiratBossGlobalRanksRes expiratBossGlobalRanksRes)
	{
		es.getRPCManager().sendExpiratBossGlobalRanksRes(serverId, expiratBossGlobalRanksRes);
	}
	
	public void sendGlobalRanksResBonus2(int serverId, ExpiratBossGlobalRanksRes2 expiratBossGlobalRanksRes2)
	{
		es.getRPCManager().sendExpiratBossGlobalRanksRes2(serverId, expiratBossGlobalRanksRes2);
	}
	
	ExchangeServer es;
	Map<Short,LinkedList<ExpiratBossRank>> rankList = new HashMap<Short,LinkedList<ExpiratBossRank>>();
	Map<Short,Map<Long, ExpiratBossRank>> mapRoleRanks =  new HashMap<Short, Map<Long, ExpiratBossRank>>();
	int day = 0;
	Map<Integer,Map<Short, SBean.ExpiratBossGlobalRanksRes>> mapGlobalRanks = new HashMap<Integer, Map<Short, SBean.ExpiratBossGlobalRanksRes>>();
	int sendGlobalRanksTime = 0;
	boolean bonus=false;
	int saveTime;
	
}

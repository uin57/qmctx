
package i3k.exchange;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import i3k.SBean;
import i3k.SBean.GlobalRoleID;
import i3k.SBean.HeroesBossFinishAttReq;
import i3k.SBean.HeroesBossInfoReq;
import i3k.SBean.HeroesBossSyncReq;
import i3k.SBean.HeroesRank;

public class HeroesBossManager
{
	
	private final short REFRESH_RANK_TIME = 300;//
			
	public HeroesBossManager(ExchangeServer es)
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
			rankl.clear();
			gIds.clear();
		}
	
	}

	ExchangeServer es;
	Map<Short,LinkedList<HeroesRank>> rankl = new HashMap<Short,LinkedList<HeroesRank>>();
	Map<Short, List<Short>> gIds = new HashMap<Short, List<Short>>();
	
	Map<Short,LinkedList<HeroesRank>> rankl2 = new HashMap<Short,LinkedList<HeroesRank>>();
	int day = 0;
	int saveTime;

	public synchronized void syncHeroes(HeroesBossSyncReq req) {
		
		if(gIds.size()<=0){
			
			getGidsByNewDay(req);
		
		}
		
		
		LinkedList<HeroesRank> r2heroes = rankl.get(req.type);
		
		if(r2heroes==null){
			rankl.put(req.type, new LinkedList<HeroesRank>());
		}
		r2heroes = rankl.get(req.type);
		
		if(r2heroes.size()>=5){
			r2heroes.clear();
			 LinkedList<HeroesRank> r2heroes2 = rankl2.get(req.type);
		 		
		 		if(r2heroes2!=null&&r2heroes2.size()<=4){
		 			r2heroes.addAll(r2heroes2);
		 			r2heroes2.clear();
		 		}	 				
		}
				
		r2heroes.add(req.ranks);
		
		List <Integer> server = new ArrayList<Integer>();
		
		for(HeroesRank r2r :r2heroes){
			if(server.contains(r2r.roleID.serverID)){
				continue;
			}
			server.add(r2r.roleID.serverID);
			sendSyncHeroesRes(r2r.roleID.serverID,new SBean.HeroesBossSyncRes(req.id, req.roleID, req.type, gIds.get((short)1), 
					gIds.get((short)2),gIds.get((short)3),r2heroes));
		}
		
	}

	public synchronized void getGidsByNewDay(HeroesBossSyncReq req) {
		for(short j =1; j<4;j++){
			List<Short> gid = gIds.get(j);
			List<Short> gidall = req.gids;
			Set<Short> gset = new HashSet<Short>();
			if(gid==null||gid.size()<=0){
				gIds.put((short)j, new ArrayList<Short>());
				gid = gIds.get(j);
				for(int i =0 ;i<70;i++){
					Random ran = new Random();
					short r = (short)(ran.nextInt(gidall.size()));
					short r1 = gidall.get(r);
					if(gidall.contains(r1)){
						gset.add(r1);
					}
					
					if(gset.size()>=req.gcnt){
						break;
					}
				}
				
				for(short id :gset){
					gid.add(id);
				}
			}
			
			
		}
	}
	
	public synchronized void sendSyncHeroesInfoRes(int serverid)
	{
		SBean.HeroesBossInfoRes res = new SBean.HeroesBossInfoRes();
		res.ranks = new ArrayList<HeroesRank>();
		
		for(Entry<Short, LinkedList<HeroesRank>> rankle :rankl.entrySet()){
			for(HeroesRank ra :rankle.getValue()){
				res.ranks.add(ra);		
			}			
		}
		es.getRPCManager().sendSyncHeroesInfoRes(serverid, res);
		es.getLogger().info("sendSyncHeroesInfoRes start" );
	}
	
	public synchronized void sendSyncHeroesRes(int serverid,SBean.HeroesBossSyncRes res)
	{
		es.getRPCManager().sendSyncHeroesRes(serverid, res);
	}

	public synchronized void heroesBossFinishAtt(HeroesBossFinishAttReq req) {
		
		if(req.serverIds.size()<5){
			LinkedList<HeroesRank> r2heroes = rankl.get(req.type);
			HeroesRank rankt = null;
			
			if(r2heroes!=null){
				for(HeroesRank rank :  r2heroes){
					
					if(rank.rid==req.roleID.roleID){
						rankt = rank;
					}
				}
			}else{
				rankl.put(req.type, new LinkedList<HeroesRank>());
			}
			
			
			if(rankt!=null){
				
				if(req.rank!=null){
					if(req.type2==1){
						rankt.score1=req.rank.score1;
					}else if(req.type2==2){
						rankt.score2=req.rank.score2;
					}else if(req.type2==3){
						rankt.score3=req.rank.score3;
					}
					rankt.headIconId = req.rank.headIconId;
					rankt.power = req.rank.power;
					rankt.lvl = req.rank.lvl;
					rankt.buffLvl = req.rank.buffLvl;
					rankt.attc = req.rank.attc;
				}else{
					if(req.type2==1){
						rankt.score1=req.score;
					}else if(req.type2==2){
						rankt.score2=req.score;
					}else if(req.type2==3){
						rankt.score3=req.score;
					}
				}
				
			}
			
		}
		
		List <Integer> server = new ArrayList<Integer>();
		for(GlobalRoleID serverids :req.serverIds){
			if(server.contains(serverids.serverID)){
				continue;
			}
			server.add(serverids.serverID);
			es.getRPCManager().sendSyncHeroesRes(serverids.serverID, 
					new SBean.HeroesBossFinishAttRes(req.id, req.roleID, req.type, req.type2, 
							req.score,req.rank, req.serverIds, req.ranks));
		}
		
	}

	public synchronized void HeroesBossInfo(HeroesBossInfoReq req) {

		if(req.ranks!=null&&req.ranks.size()>0){
			for(HeroesRank rank :req.ranks){
		    	 LinkedList<HeroesRank> r2heroes = rankl2.get(rank.type);
		 		
		 		if(r2heroes==null){
		 			rankl2.put((short)rank.type, new LinkedList<HeroesRank>());
		 		}
		 		r2heroes = rankl2.get((short)rank.type);
		 		
		 		r2heroes.add(rank);
		     }
		}
	     
		
	}
	
}

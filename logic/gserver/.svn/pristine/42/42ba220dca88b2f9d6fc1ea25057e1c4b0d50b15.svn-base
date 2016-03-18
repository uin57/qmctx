package i3k.gs;

import i3k.DBMail;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.DBHeroesBoss;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBHeroesBossTimes;
import i3k.SBean.DropEntryNew;
import i3k.SBean.GlobalRoleID;
import i3k.SBean.HeroesBossFinishAttReq;
import i3k.SBean.HeroesBossFinishAttRes;
import i3k.SBean.HeroesBossInfoReq;
import i3k.SBean.HeroesBossInfoRes;
import i3k.SBean.HeroesBossItemsCFGS;
import i3k.SBean.HeroesBossScoreCFGS;
import i3k.SBean.HeroesBossSyncReq;
import i3k.SBean.HeroesBossSyncRes;
import i3k.SBean.HeroesRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class HeroesBossManager {
	
	
	public static int SAVE_INTERVAL = 27 * 60;
	private final short RESET_TIME = 300;//1439
				
	
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
				gs.getLogger().debug("HeroesBoss event task timeout remove callback");
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
				gs.getLogger().warn("HeroesBoss event task callback id=" + id + "==>but timeout");
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
		SaveTrans(SBean.DBHeroesBoss expiratBoss)
		{
			this.heroesBoss = expiratBoss;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("heroesBoss");
			byte[] data = Stream.encodeLE(heroesBoss);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("heroesBoss save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBHeroesBoss heroesBoss;
	}
	
	public HeroesBossManager(GameServer gs)
	{
		this.gs = gs;
		this.service = new EventService(gs);
	}
	
	public void init(SBean.DBHeroesBoss heroesBoss)
	{
		this.heroesBoss = heroesBoss;
		if (heroesBoss != null) {
			
		}
		if (heroesBoss == null)
			this.heroesBoss = new SBean.DBHeroesBoss(new ArrayList<HeroesRank>(),new ArrayList<Integer>()
					, new ArrayList<Short> (), new ArrayList<Short> (), 
					new ArrayList<Short> (),(short) 0, 0, 0);
		saveTime = gs.getTime();
	}
	
	public void onTimer()
	{
		int day = gs.getDayByOffset(RESET_TIME);
		if (heroesBoss.day < day) {//
			heroesBoss.day = day;
			
			
			for(int roleId :heroesBoss.roles){
				Role role = gs.getLoginManager().getRole(roleId);
				if (role != null && !role.isNull()) 
				{
					synchronized (HeroesBossManager.this) {					
						if(role.heroesBossInfo!=null){
							List<HeroesRank> r2heroes = role.heroesBossInfo.ranks;
							short rank = getHeroesBonusRanks(roleId,r2heroes);
							HeroesBossItemsCFGS bonus = gs.getGameData().getHeroesBossBonusCFG(rank);
							if(bonus!=null&&bonus.eItems.size()>0){
   		   		   				gs.getLoginManager().sysSendMessage(0, roleId, DBMail.Message.SUB_TYPE_HEROESBOSS_REWARD, "", ""+(short)rank, 0, true,bonus.eItems);  		   		   		       
   		   		   			 } 
							 role.setHeroesBossInfo();
						}
					}
				}else{
					List<Integer> ridsl2 = new ArrayList <Integer>();
					ridsl2.add(roleId);
					gs.getLoginManager().doHeroesBossBonusTran(ridsl2);
				}
			}
			heroesBoss.roles.clear();
			heroesBoss.gids1.clear();
			heroesBoss.gids2.clear();
			heroesBoss.gids3.clear();
			
			gIds.clear();
			
           
           bonus = false ;
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
		gs.getDB().execute(new SaveTrans(heroesBoss.kdClone()));
	}
	
	public synchronized void setHeroesBossId(int rId){
		
		if(!heroesBoss.roles.contains(rId))
			heroesBoss.roles.add(rId);
	}
	
	
	class HeroesBossSync extends EventTask 
	{
		HeroesBossSyncReq req;

		HeroesBossSync(HeroesBossSyncReq req) 
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
			gs.getRPCManager().exchangeSendHeroesBossSync(req);
		}
		
		public void onCallback(Object obj) 
		{
			HeroesBossSyncRes res = (SBean.HeroesBossSyncRes) obj;
			execHeroesSync(res);
			
		}

		
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.HeroesBossSyncRes(req.id, req.roleID, (short)0, new ArrayList<Short>()
					,new ArrayList<Short>(),new ArrayList<Short>(), null);
		}
	}
	
	public void execHeroesSync(HeroesBossSyncRes  res) {
		
		
		List<Integer> ridsl = new ArrayList <Integer>();
		
		if(res.type==0&&res.roleID!=null&&res.roleID.serverID==gs.cfg.id){
			Role role = gs.getLoginManager().getRole(res.roleID.roleID);
			if(role!=null){
				if(role.heroesBossInfo==null){
					role.heroesBossInfo=new DBHeroesBossInfo(new ArrayList<HeroesRank>(), 
							new ArrayList<DBHeroesBossTimes> (), (short) 0, 0, 
	                        0, 0, 0, 0, 0);
				}
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeHeroesBossSyncRes(heroesBoss,role.heroesBossInfo));
			}
			
		}
		
		if(res==null||res.ranks==null){
			return;
		}
		for(HeroesRank rank : res.ranks){
			GlobalRoleID ids = rank.roleID;
			if(ids.serverID==gs.cfg.id){//&&ids.roleID!=res.roleID.roleID
				ridsl.add(ids.roleID);
			}
		}
		
		for(int roleId :ridsl){
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				synchronized (HeroesBossManager.this) {					
			
						if(res.gids1!=null&&heroesBoss.gids1.size()<=res.gids1.size()){
							heroesBoss.gids1=res.gids1;
						}
                        if(res.gids2!=null&&heroesBoss.gids2.size()<=res.gids2.size()){
							heroesBoss.gids2=res.gids2;
						}
                        if(res.gids3!=null&&heroesBoss.gids3.size()<=res.gids3.size()){
							heroesBoss.gids3=res.gids3;
						}
						
						if(roleId==res.roleID.roleID&&role.heroesBossInfo==null){
							role.heroesBossInfo=new DBHeroesBossInfo(new ArrayList<HeroesRank>(), 
									new ArrayList<DBHeroesBossTimes> (), (short) 0, 0, 
			                        0, 0, 0, 0, 0);
						}

						if(role.heroesBossInfo!=null&&res.ranks!=null&&
								res.ranks.size()>0){
							role.heroesBossInfo.ranks.clear();
							for(HeroesRank tant:res.ranks){
								if(tant!=null){
									role.heroesBossInfo.ranks.add(tant.kdClone());
								}
								
							}
						}					
				}
				
				if(roleId==res.roleID.roleID)
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeHeroesBossSyncRes(heroesBoss,role.heroesBossInfo));
			}else{
				List<Integer> ridsl2 = new ArrayList <Integer>();
				ridsl2.add(roleId);
				gs.getLoginManager().doHeroesBossSyncTran(ridsl2,res);
			}
		}
	}
	
	public void syncHeroes(SBean.HeroesBossSyncReq req) 
	{
		excuteEventTaskWaitResponse(new HeroesBossSync(req));
	}
	
	public void syncHeroesInfo() 
	{
		if(heroesBoss!=null&&heroesBoss.ranks!=null&&heroesBoss.ranks.size()>0){
			SBean.HeroesBossInfoReq req = new SBean.HeroesBossInfoReq();
			req.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank rank :heroesBoss.ranks){
				req.ranks.add(rank.kdClone());
			}
			excuteEventTaskWaitResponse(new HeroesBossInfo(req));
			heroesBoss.ranks.clear();
		}
		
		
	}
	
	class HeroesBossInfo extends EventTask 
	{
		HeroesBossInfoReq req;

		HeroesBossInfo(HeroesBossInfoReq req) 
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
			gs.getRPCManager().exchangeSendHeroesBossInfo(req);
		}
		
		public void onCallback(Object obj) 
		{
			
		}
		
		public Object makeDefaultResponse(int error)
		{
			return error;
		
		}
	}
	
	
	class HeroesBossFinishAtt extends EventTask 
	{
		HeroesBossFinishAttReq req;

		HeroesBossFinishAtt(HeroesBossFinishAttReq req) 
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
			gs.getRPCManager().exchangeSendHeroesBossFinishAtt(req);
		}
		
		public void onCallback(Object obj) 
		{
			HeroesBossFinishAttRes res = (SBean.HeroesBossFinishAttRes) obj;
			execFinishAtt(res);
			
		}
	
		public Object makeDefaultResponse(int error)
		{
			return new SBean.HeroesBossFinishAttRes(req.id, req.roleID, (short)0,(short)0,0,new HeroesRank(0,(byte)0, new GlobalRoleID(gs.cfg.id,0), "1", "1",
					(short)0, (short)1,0, 0, 0, 0,(short)0,(short)0,0, 0), new ArrayList<GlobalRoleID>(),
					new ArrayList<HeroesRank>());
		}
	}

	public void execFinishAtt(HeroesBossFinishAttRes res) {
		
		List<Integer> ridsl = new ArrayList <Integer>();
		for(GlobalRoleID ids : res.serverIds){
			if(ids.serverID==gs.cfg.id&&ids.roleID!=res.roleID.roleID){
				ridsl.add(ids.roleID);
			}
		}
		
		for(int roleId :ridsl){
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				synchronized (HeroesBossManager.this) {					
					if(role.heroesBossInfo!=null){
						List<HeroesRank> r2heroes = role.heroesBossInfo.ranks;
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
							
							
						}
					}
				}
	//			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeHeroesBossFinishAttackRes(res.type2,res.score,1));
			}else{
				List<Integer> ridsl2 = new ArrayList <Integer>();
				ridsl2.add(roleId);
				gs.getLoginManager().doHeroesBossTran(ridsl2,res);
			}
		}
	}
	
	public void syncHeroesFinishAttack(
			HeroesBossFinishAttReq req) {
		excuteEventTaskWaitResponse(new HeroesBossFinishAtt(req));
		
	}
	
	
	
	public  DBHeroesBoss getHeroesBossInfo(){
		return heroesBoss;
	}

			
	GameServer gs;
	SBean.DBHeroesBoss heroesBoss;
	
	List<Integer> roleIds = new ArrayList<Integer>();
	static Map<Short, List<Short>> gIds = new HashMap<Short, List<Short>>();
	
	int saveTime;
	private EventService service;
	

	public static final int INVOKE_SUCCESS = 0;
	public static final int INVOKE_UNKNOWN_ERROR = -1;
	public static final int INVOKE_TIMEOUT = -2;
	
	public static final int INVOKE_VIST_ROLE_ERROR = -100;
	public static final int INVOKE_VIST_DBROLE_ERROR = -101;
	
	public static final long MAX_WAIT_TIME = 3000;
	
	public boolean bonus  = false;

	public synchronized List<SBean.DropEntryNew> giveBonus(short rank)
	{
		List<SBean.DropEntryNew>  dropList= new ArrayList<SBean.DropEntryNew>();
		HeroesBossItemsCFGS bonus =gs.gameData.getHeroesBossBonusCFG(rank);			
		if(bonus!=null){		
			for(DropEntryNew item : bonus.eItems){			
				dropList.add(item);			
			}			
		}
		return dropList;
	}

	public void handleHeroesBossSyncRequest(HeroesBossSyncRes res) {
		
		
	}

	public static short getHeroesBonusRanks(int roleid,List<HeroesRank> rankst1 )
	{	
		Map<Integer,Integer> rid2score = new HashMap<Integer,Integer>();
		List<HeroesRank> rankst = new ArrayList<HeroesRank>();//rankst1;
		
		for(HeroesRank ran:rankst1){
			rankst.add(ran.kdClone());
		}
	
		if(rankst==null||rankst.size()<=0){
			return 6;
		}
		
//		if(rankst.size()==1){
//			return 1;
//		}
		
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				if(rank2.score1 ==rank1.score1){
					 return rank2.power - rank1.power;
				}
				return rank2.score1 -rank1.score1;
			}
		});
		
		short index = 0;
		int temp = 0;
		for(HeroesRank s1:rankst){
			
			if(s1.score1<=0){
				continue;
			}
			short num =0;
			if(temp==s1.score1){
				
				short in =1;
				for( HeroesRank  rant3 : rankst){
					if(in>index){
						break;
					}
					if(rant3.score1==temp){
						num++;
					}

					in++;
				}
			}
			HeroesBossScoreCFGS  sc1 = GameData.getInstance().getHeroesBossScoreCFG((short) (index-num<0?0:(index-num)),(short)1);
			if(sc1==null){
				continue;
			}
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
			
			
			temp =s1.score1;
			
		}
		
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				if(rank2.score2 ==rank1.score2){
					 return rank2.power - rank1.power;
				}
				return rank2.score2 -rank1.score2;
			}
		});
		
		index = 0;
		temp = 0;
		for(HeroesRank s1:rankst){
			if(s1.score2<=0){
				continue;
			}
			
			short num =0;
			if(temp==s1.score2){
				
				short in =1;
				for( HeroesRank  rant3 : rankst){
					if(in>index){
						break;
					}
					if(rant3.score2==temp){
						num++;
					}

					in++;
				}
			}
			HeroesBossScoreCFGS  sc1 = GameData.getInstance().getHeroesBossScoreCFG((short) (index-num<0?0:(index-num)),(short)2);
			if(sc1==null){
				continue;
			}
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
			
			temp =s1.score2;
			
		}
		
		Collections.sort(rankst,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				if(rank2.score3 ==rank1.score3){
					 return rank2.power - rank1.power;
				}
				return rank2.score3 -rank1.score3;
			}
		});
		
		index = 0;
		temp = 0;
		for(HeroesRank s1:rankst){
			if(s1.score3<0){
				continue;
			}
			
			short num =0;
			if(temp==s1.score3){
				
				short in =1;
				for( HeroesRank  rant3 : rankst){
					if(in>index){
						break;
					}
					if(rant3.score3==temp){
						num++;
					}

					in++;
				}
			}
			
			HeroesBossScoreCFGS  sc1 = GameData.getInstance().getHeroesBossScoreCFG((short) (index-num<0?0:(index-num)),(short)3);
			
			if(sc1==null){
				continue;
			}
			Integer sco = rid2score.get(s1.rid);
			rid2score.put(s1.rid, sco==null?sc1.score:(sc1.score+sco));
			index++;
			
			temp =s1.score3;
		}
		
		List<Map.Entry<Integer, Integer>> list_Data = new ArrayList<Map.Entry<Integer,Integer>>(rid2score.entrySet());  
                Collections.sort(list_Data,new Comparator<Map.Entry<Integer, Integer>>() {  
  
	            @Override  
	            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
	            	
	                return (o2.getValue() - o1.getValue());  
	            }  
        });  
                
//        short rank = 1;
//        
//        for(Entry<Integer, Integer> data :list_Data){
//        	if(roleid==data.getKey()){
//        		return rank;
//        	}
//        	rank++;
//        }
           
                
        for(Entry<Integer, Integer> data :list_Data){
        	for( HeroesRank  rant : rankst1){
        		if(data.getKey()==rant.rid){
        			rant.score3=data.getValue();
        		}
        	}
        } 
        
        Collections.sort(rankst1,new Comparator<HeroesRank>(){
			public int compare(HeroesRank rank1,HeroesRank rank2) {
				return rank2.score3 -rank1.score3;
			}
		});
        
        short rank1 = 1;
        temp = 0;
        for( HeroesRank  rant : rankst1){
       	
        	
    		if(roleid==rant.rid){
    			short num =0;
    			if(rant.score1==0&&rant.score2==0&&rant.score3==-1){
    				return -1;
    			}
    			if(temp==rant.score3){
    				
    				short in =1;
    				for( HeroesRank  rant3 : rankst1){
    					if(in>=rank1){
    						break;
    					}
    					if(rant3.score3==temp){
    						num++;
    					}
	
    					in++;
    				}
    			}
    			return (short) ((rank1-num)<=0?1:(rank1-num));
    		}
    		rank1++;
    				
    		temp =rant.score3;
    	}
                   
        
//        List<HeroesRank> rankst2 = new ArrayList<HeroesRank>();
//        for(Entry<Integer, Integer> data :list_Data){
//        	for( HeroesRank  rant : rankst){
//        		if(data.getKey()==rant.rid){
//        			rant.score3=data.getValue();
//        			rankst2.add(rant);
//        		}
//        	}
//        }
//        
//        Collections.sort(rankst2,new Comparator<HeroesRank>(){
//			public int compare(HeroesRank rank1,HeroesRank rank2) {
//				if(rank2.score3 ==rank1.score3){
//					 return rank2.power - rank1.power;
//				}
//				return rank2.score3 -rank1.score3;
//			}
//		});
//        
//        short rank1 = 1;
//        
//        for( HeroesRank  rant : rankst2){
//    		if(roleid==rant.rid){
//    			return rank1;
//    		}
//    		rank1++;
//    	}
        
		return 6;
		
	}

	public void handleHeroesBossInfo(HeroesBossInfoRes res) {
		heroesBoss.ranks = res.ranks;
	}
	
}

package i3k.gs;

import i3k.DBMail;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.DBExpiratBossDamage;
import i3k.SBean.DBExpiratBossRank;
import i3k.SBean.DBExpiratBossRankList;
import i3k.SBean.DBExpiratBossRankList2;
import i3k.SBean.DBExpiratBossRole;
import i3k.SBean.DBExpiratBossTimes;
import i3k.SBean.DropEntryNew;
import i3k.SBean.DuelTopRank;
import i3k.SBean.ExpiratBossBasicCFGS;
import i3k.SBean.ExpiratBossGlobalRank;
import i3k.SBean.ExpiratBossGlobalRanksRes;
import i3k.SBean.ExpiratBossGlobalRanksRes2;
import i3k.SBean.ExpiratBossItemsCFGS;
import i3k.SBean.ExpiratBossRank;
import i3k.SBean.ExpiratBossRankReq;
import i3k.SBean.ExpiratBossRankRes;
import i3k.SBean.ExpiratBossRanks;
import i3k.SBean.ExpiratBossRanksRes;
import i3k.SBean.ExpiratBossTopRank;
import i3k.SBean.ExpiratBossTopRanksRes;
import i3k.SBean.GlobalRoleID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class ExpiratBossManager {
	
	
	public static int SAVE_INTERVAL = 29 * 60;
	private final short RESET_TIME = 1366;//1439
				
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
		DamageRank(short combatId,int rank, int rid, int damage, short icon, String name, short lvl,String serverName)
		{
			this.rank = rank;
			this.rid = rid;
			this.damage = damage;
			this.icon = icon;
			this.name = name;
			this.lvl = lvl;
			this.combatId = combatId;
			this.serverName = serverName;
		}
		public short combatId;
		public int rank;
		public int rid;
		public int damage;
		public short icon;
		public String name;
		public short lvl;
		public String serverName;
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
				gs.getLogger().debug("expiratBoss event task timeout remove callback");
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
				gs.getLogger().warn("expiratBoss event task callback id=" + id + "==>but timeout");
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
		SaveTrans(SBean.DBExpiratBoss expiratBoss)
		{
			this.expiratBoss = expiratBoss;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("expiratBoss");
			byte[] data = Stream.encodeLE(expiratBoss);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("expiratBoss save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBExpiratBoss expiratBoss;
	}
	
	public ExpiratBossManager(GameServer gs)
	{
		this.gs = gs;
		this.service = new EventService(gs);
	}
	
	public void init(SBean.DBExpiratBoss expiratBoss)
	{
		this.expiratBoss = expiratBoss;
		rolesMap.clear();
		if (expiratBoss != null) {
				
			for (DBExpiratBossRole r : expiratBoss.roles)
				rolesMap.put(r.rid, r);
			
			for(DBExpiratBossRankList rankList : expiratBoss.ranks){
				
				Map<Integer, DBExpiratBossRank> i =rid2DamageMap.get(rankList.combatId);
				if(i==null){
					rid2DamageMap.put(rankList.combatId, new HashMap<Integer, DBExpiratBossRank>());
				}
				i =rid2DamageMap.get(rankList.combatId);
				for (SBean.DBExpiratBossRank r : rankList.expiratBossRank) {							
					i.put(r.rid, r);
					setDamage(r.combatId,r.damage, r.rid, r.rname, r.serverName, r.headIconId, r.lvl);
				}
			}
			
            for(DBExpiratBossRankList2 rankList : expiratBoss.ranksHis){

				List<ExpiratBossTopRank> i =sortDamageListsHis.get(rankList.combatId);
				if(i==null){
					sortDamageListsHis.put(rankList.combatId, new ArrayList<ExpiratBossTopRank>());
				}
				i =sortDamageListsHis.get(rankList.combatId);
				for (ExpiratBossTopRank r : rankList.expiratBossRank2) {							
					i.add(new ExpiratBossTopRank( new GlobalRoleID (r.roleID.serverID,r.roleID.roleID), r.roleName, r.serverName, r.headIconId, 
	                          r.damage,r.rank));
//					setDamage(r.combatId,r.damage, r.rid, r.rname, r.serverName, r.headIconId, r.lvl);
				}
			}
			
		}
		if (expiratBoss == null)
			this.expiratBoss = new SBean.DBExpiratBoss(new ArrayList<SBean.DBExpiratBossRankList>(),new ArrayList<SBean.DBExpiratBossRankList2>(),
					new ArrayList<SBean.DBExpiratBossRole>(),gs.getDayByOffset(RESET_TIME),(short) 0, 0, 0);
		saveTime = gs.getTime();
	}
	
	public void onTimer()
	{
		int day = gs.getDayByOffset(RESET_TIME);
		if (expiratBoss.day < day&& bonus ) {//
			expiratBoss.day = day;
			
			expiratBoss.ranks.clear();
			expiratBoss.ranksHis.clear();
			
			sortDamageLists.clear();
			rid2DamageMap.clear();
			
			
           for(DBExpiratBossRole roleBonus:  rolesMap.values()){
        	
   			 
   			 boolean hasBonus = false;
   			 
   			 if(sortDamageListsHis.size()>0){
   				 for(Entry<Short, List<ExpiratBossTopRank>>  top : sortDamageListsHis.entrySet()){
   	   				 
   	   				 if(top.getKey()!=roleBonus.combat){
   	   					 continue;
   	   				 }
   	   				 for(ExpiratBossTopRank boss :top.getValue()){
   	   					 
   	   					 if(boss.roleID.roleID==roleBonus.rid&&boss.roleID.serverID==gs.cfg.id){
   	   						List<DropEntryNew> giveBonus1 = giveBonus(roleBonus.combat,(short) boss.rank);
   		   		   			 if(giveBonus1.size()>0){
   		   		   				gs.getLoginManager().sysSendMessage(0, roleBonus.rid, DBMail.Message.SUB_TYPE_EXPATRIATEBOSS_REWARD, "", ""+(short) boss.rank, 0, true, giveBonus1);
   		   		   			    hasBonus = true;
   		   		   			 } 
   	   					 }
   	   				 }
   	   			 } 
   			 }
   			
   			 
   			 if(!hasBonus){
   				 List<DropEntryNew> giveBonus = giveBonus(roleBonus.combat);
   	   			 if(giveBonus.size()>0){
   	   				gs.getLoginManager().sysSendMessage(0, roleBonus.rid, DBMail.Message.SUB_TYPE_EXPATRIATEBOSS_REWARD, "", "", 0, true, giveBonus);
   	   			 }  
   			 }
           }
           sortDamageListsHis.clear();
           rolesMap.clear();
           expiratBoss.roles.clear();
           
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
		gs.getDB().execute(new SaveTrans(expiratBoss.kdClone()));
	}
	
	public synchronized void rank(int sid, Short combatId,Role role)
	{
		
		if(role.expiratBossInfo==null||role.expiratBossInfo.damageHis==null
				||sortDamageListsHis.get(combatId)==null){
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,null));
			return;
		}
		
		DBExpiratBossTimes dbExpiratBossTimes = null;
		if(role.expiratBossInfo==null){
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,null));
			return ;
		}
		
		role.updateExpiratBossInfo();
	
		for(DBExpiratBossTimes i:role.expiratBossInfo.times){
			if(combatId==i.id){
				dbExpiratBossTimes=i;
			}
		}
		
		DBExpiratBossDamage dbExpiratBossDamage = null;
		for(DBExpiratBossDamage di:role.expiratBossInfo.damageHis){
			if(combatId==di.id){
				dbExpiratBossDamage=di;
			}
		}
		
		if(dbExpiratBossTimes==null||dbExpiratBossDamage==null||dbExpiratBossDamage.damage<=0){
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,null));
			return ;
		}
		
		int day = gs.getDayByOffset((short) 1);
		if (dbExpiratBossTimes.bonusday >= day) {
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,null));
//			role.expiratBossInfo.bonusday=day;
			return;
		}
		
		short rank =0 ;
		for(ExpiratBossTopRank i :sortDamageListsHis.get(combatId)){
			if(i.roleID.roleID==role.id){
				rank=(short) i.rank;
			}
		}
		
		if(rank>0){
			
			SBean.ExpiratBossRankRes res = new SBean.ExpiratBossRankRes();
			res.rank=rank;
			res.error=0;
			role.giveBonus(expiratBoss.bossId,combatId,rank,role);
			gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(combatId,res));
			return;
		}
		
		rank(new SBean.ExpiratBossRankReq(0,combatId, new SBean.GlobalRoleID(gs.getConfig().id, role.id)));
	}
	

	class RankTask extends EventTask 
	{
		ExpiratBossRankReq req;

		RankTask(ExpiratBossRankReq req) 
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
			gs.getRPCManager().exchangeSendExpiratBossRank(req);
		}
		
		public void onCallback(Object obj) 
		{
			ExpiratBossRankRes res = (SBean.ExpiratBossRankRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null && !role.isNull()) 
			{
				synchronized (ExpiratBossManager.this) {
					DBExpiratBossRole dr = rolesMap.get(role.id);
					if (dr != null) 
						dr.rank = res.rank;
					role.giveBonus(expiratBoss.bossId,req.type,(short) res.rank,role);
				}
				
				gs.getRPCManager().sendLuaPacket(role.netsid, LuaPacket.encodeExpiratBossRankRes(req.type,res));
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return new SBean.ExpiratBossRankRes(req.id, req.roleID, 0, error);
		}
	}

	public void rank(SBean.ExpiratBossRankReq req) 
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

//	public void topRanks(SBean.DuelTopRanksReq req) 
//	{
//		excuteEventTaskWaitResponse(new TopRanksTask(req));
//	}

	synchronized void setDamage(short combatId,int damage, int id, String name, String serverName, short icon, short lvl)
	{
		removeScoreSortList(id,combatId);
		insertScoreSortList(damage, id, combatId);
		Map<Integer, DBExpiratBossRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
		if(rid2ScoreMapi==null){
			rid2DamageMap.put(combatId, new HashMap<Integer, DBExpiratBossRank>());
		}
		rid2ScoreMapi =rid2DamageMap.get(combatId);
		SBean.DBExpiratBossRank d = rid2ScoreMapi.get(id);
		if (d == null) {
			d = new SBean.DBExpiratBossRank(combatId,serverName, id, name, icon, lvl, damage);
			rid2ScoreMapi.put(id, d);
			
			List<DBExpiratBossRank> ranksBossList =null;
			
            for(DBExpiratBossRankList  ranksl :expiratBoss.ranks){
				if(ranksl.combatId==combatId){
					ranksBossList=ranksl.expiratBossRank;
				}
			}
                     
            if(ranksBossList==null){
            	DBExpiratBossRankList  rankslst = new DBExpiratBossRankList();
            	rankslst.combatId=combatId;
            	rankslst.expiratBossRank = new  ArrayList<DBExpiratBossRank>();
				expiratBoss.ranks.add(rankslst);
				for(DBExpiratBossRankList  ranksl :expiratBoss.ranks){
					if(ranksl.combatId==combatId){
						ranksBossList=ranksl.expiratBossRank;
					}
				}
			}
			
            ranksBossList.add(d);
		}
		else {
			d.combatId=combatId;
			d.serverName = serverName;
			d.damage = damage;
			d.rname = name;
			d.headIconId = icon;
			d.lvl = lvl;
		}

		SBean.DBExpiratBossRole dr = rolesMap.get(id);
		if (dr == null) {
			dr = new SBean.DBExpiratBossRole(id, combatId, (short)0, (short)0, 0);
			rolesMap.put(id, dr);
			expiratBoss.roles.add(dr);
		}
		else
			dr.combat = combatId;
	}
	
	private void insertScoreSortList(int damage, int id,short combatId)
	{
		List<SortDamage> sortScoreList = sortDamageLists.get(combatId);
		if (sortScoreList == null) {
			sortScoreList = new ArrayList<SortDamage>();
			sortDamageLists.put(combatId, sortScoreList);
		}
		
		int size = sortScoreList.size();
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (damage > sortScoreList.get(mid).damage) {
				end = mid - 1;
			} else if (damage < sortScoreList.get(mid).damage) {
				begin = mid + 1;
				if (begin > end)
					mid = begin;
			} else {
				break;
			}
		}
		sortScoreList.add(mid, new SortDamage(damage, id));
	}
	
	private void removeScoreSortList(int id, short combatId)
	{
		List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
		if (sortDamageList == null)
			return;
		Map<Integer, DBExpiratBossRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
		if(rid2ScoreMapi==null){
			rid2DamageMap.put(combatId, new HashMap<Integer, DBExpiratBossRank>());
		}
		rid2ScoreMapi =rid2DamageMap.get(combatId);
		SBean.DBExpiratBossRank d = rid2ScoreMapi.get(id);
		if (d == null)
			return;
		int index = getScoreSortList(d.damage, id,combatId);
		if (index >= 0)
			sortDamageList.remove(index);
	}
	
	private int getScoreSortList(int damage, int id, short combatId)
	{
		List<SortDamage> sortScoreList = sortDamageLists.get(combatId);
		if (sortScoreList == null)
			return -1;
		int size = sortScoreList.size();
		if (size == 0)
			return -1;
		int mid = 0;
		int begin = 0;
		int end = size - 1;
		while (begin <= end) {
			mid = (end - begin) / 2 + begin;
			if (damage >= sortScoreList.get(mid).damage) {
				end = mid - 1;
			} else if (damage < sortScoreList.get(mid).damage) {
				begin = mid + 1;
			}
		}
		int tmp = mid;
		if (sortScoreList.get(tmp).damage != damage)
			tmp ++;
		SortDamage p = null;
		while (tmp < size && (p = sortScoreList.get(tmp)).damage == damage) {
			if (p.rid == id) {
				return tmp;
			}
			tmp ++;
		}
		return -1;
	}
	
	synchronized List<SBean.ExpiratBossRank> getAllDamageRanks(short combatId)
	{	
		List<ExpiratBossRank> ranks = new ArrayList<ExpiratBossRank>();
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
	    if (sortDamageList == null)
		return ranks;
		for (SortDamage s : sortDamageList) {
			Map<Integer, DBExpiratBossRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
			if(rid2ScoreMapi==null){
				rid2DamageMap.put(combatId, new HashMap<Integer, DBExpiratBossRank>());
			}
			rid2ScoreMapi =rid2DamageMap.get(combatId);
			SBean.DBExpiratBossRank dr = rid2ScoreMapi.get(s.rid);
			if (dr == null)
				continue;
			
			ranks.add(new SBean.ExpiratBossRank(combatId,0,dr.serverName, new SBean.GlobalRoleID(gs.getConfig().id, dr.rid), dr.rname, dr.headIconId, dr.damage));
			if(ranks.size()>=60){
				break;
			}
		}
		
		return ranks;
	}
	
	public ExpiratBossRanksRes handleRanksRequest()
	{
		List<ExpiratBossRanks> rankslist =new ArrayList<ExpiratBossRanks>();
		for(short i : sortDamageLists.keySet()){
			ExpiratBossRanks ranksboss = new ExpiratBossRanks();
			List<SBean.ExpiratBossRank> ranksi = getAllDamageRanks(i);
			ranksboss.ranks=ranksi;
			ranksboss.type=i;
			rankslist.add(ranksboss);
			
		}
		
		return new SBean.ExpiratBossRanksRes(gs.getConfig().id, rankslist);
	}
	
	synchronized List<DamageRank> getDamageRanks(int rid,short combatId)
	{	
		List<DamageRank> ranks = new ArrayList<DamageRank>();
		List<ExpiratBossTopRank> sortDamageList = sortDamageListsHis.get(combatId);
		if (sortDamageList == null)
			return ranks;
		
		for (byte i = 0; i < 100; i ++) {
			if (i >= sortDamageList.size())
				break;
			ExpiratBossTopRank damage = sortDamageList.get(i);
			DamageRank rank = new DamageRank(combatId,i+1, damage.roleID.roleID, 
					damage.damage, damage.headIconId, damage.roleName, (short) 0,damage.serverName);
			ranks.add(rank);
		}
		
		return ranks;
	}
	
	public void setExpiratBoss(short bossId){
		expiratBoss.bossId=bossId;
	}
			
	GameServer gs;
	SBean.DBExpiratBoss expiratBoss;
	Map<Integer, SBean.DBExpiratBossRole> rolesMap = new HashMap<Integer, SBean.DBExpiratBossRole>();
	Map<Short, List<SortDamage>> sortDamageLists = new HashMap<Short,List<SortDamage>>();
	Map<Short,Map<Integer, SBean.DBExpiratBossRank>> rid2DamageMap = new HashMap<Short,Map<Integer, SBean.DBExpiratBossRank>>();
	
	Map<Short, List<ExpiratBossTopRank>> sortDamageListsHis = new HashMap<Short,List<ExpiratBossTopRank>>();
	
	int saveTime;
	private EventService service;
	
	public static short bossId=0;

	public static final int INVOKE_SUCCESS = 0;
	public static final int INVOKE_UNKNOWN_ERROR = -1;
	public static final int INVOKE_TIMEOUT = -2;
	
	public static final int INVOKE_VIST_ROLE_ERROR = -100;
	public static final int INVOKE_VIST_DBROLE_ERROR = -101;
	
	public static final long MAX_WAIT_TIME = 3000;
	
	public boolean bonus  = false;

	public void handleTopRanksRequest(ExpiratBossTopRanksRes res) {
		Short combat = res.combatId;
		int day = gs.getDayByOffset(RESET_TIME);
		int day2 = gs.getDayByOffset((short) (RESET_TIME+30));
		if (day>day2) {
			return;
		}
		sortDamageListsHis.put(combat, res.ranks);	
	}
	
	public synchronized List<SBean.DropEntryNew> giveBonus(short combatId,short rank)
	{
		List<SBean.DropEntryNew>  dropList= new ArrayList<SBean.DropEntryNew>();
		ExpiratBossBasicCFGS cfg = gs.getGameData().getExpiratBossBasicCFG(combatId);
		if(cfg==null){
			return dropList;
		}
		ExpiratBossItemsCFGS bonus =gs.gameData.getExpiratBossBonusCFG(expiratBoss.bossId,rank,cfg.tid);
		
		if(bonus==null){
			bonus =gs.gameData.getExpiratBossBonusCFG(expiratBoss.bossId,rank,cfg.tid);
		}
				
		if(bonus!=null){		
			for(DropEntryNew item : bonus.eItems){			
				dropList.add(item);			
			}			
		}
		return dropList;
	}
	
	public synchronized List<SBean.DropEntryNew> giveBonus(short combatId)
	{
		List<SBean.DropEntryNew>  dropList= new ArrayList<SBean.DropEntryNew>();
		ExpiratBossBasicCFGS cfg = gs.getGameData().getExpiratBossBasicCFG(combatId);
		if(cfg==null){
			return dropList;
		}
		ExpiratBossItemsCFGS bonus =gs.gameData.getExpiratBossBonusMinCFG(expiratBoss.bossId,cfg.tid);
					
		if(bonus!=null){		
			for(DropEntryNew item : bonus.eItems){			
				dropList.add(item);			
			}			
		}
		return dropList;
	}

	public synchronized  void handleGlobalRanksRequest(ExpiratBossGlobalRanksRes res) {
		for(ExpiratBossGlobalRank i :res.ranks){
			List<DropEntryNew> giveBonus = giveBonus(res.rankType,(short) i.rank);
			if(giveBonus.size()>0){
				gs.getLoginManager().sysSendMessage(0, i.roleId, DBMail.Message.SUB_TYPE_EXPATRIATEBOSS_REWARD, "", "" + i.rank, 0, true, giveBonus);
			}			
			rolesMap.remove(i.roleId);
		}
		bonus = true;
	}
	
	public synchronized  void handleGlobalRanksRequest2(ExpiratBossGlobalRanksRes2 res) {
		
		if(res.ranks2!=null){
			for(ExpiratBossGlobalRanksRes top :res.ranks2){
				for(ExpiratBossGlobalRank i :top.ranks){
					List<DropEntryNew> giveBonus = giveBonus(top.rankType,(short) i.rank);
					if(giveBonus.size()>0){
						gs.getLoginManager().sysSendMessage(0, i.roleId, DBMail.Message.SUB_TYPE_EXPATRIATEBOSS_REWARD, "", "" + i.rank, 0, true, giveBonus);
					}			
					rolesMap.remove(i.roleId);
				}
			}
		}
			
		bonus = true;
	}
	
}

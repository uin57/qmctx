package i3k.gs;

import i3k.DBMail;
import i3k.SBean;
import i3k.SBean.DBDiskBetRank;
import i3k.SBean.DBDiskBetRankList;
import i3k.SBean.DBDiskBetRole;
import i3k.SBean.DiskBetItemsCFGS;
import i3k.SBean.DropEntryNew;
import i3k.gs.GameActivities.DiskBetConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class DiskBetManager {
	
	
	public static int SAVE_INTERVAL = 26 * 60;
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
	

	
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBDiskBet DiskBet)
		{
			this.diskBet = DiskBet;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("diskBet");
			byte[] data = Stream.encodeLE(diskBet);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("diskBet save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBDiskBet diskBet;
	}
	
	public DiskBetManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBDiskBet diskBet)
	{
		this.diskBet = diskBet;
		rolesMap.clear();
		if (diskBet != null) {
				
			for (DBDiskBetRole r : diskBet.roles)
				rolesMap.put(r.rid, r);
			
			for(DBDiskBetRankList rankList : diskBet.ranks){
				
				Map<Integer, DBDiskBetRank> i =rid2DamageMap.get(rankList.combatId);
				if(i==null){
					rid2DamageMap.put(rankList.combatId, new HashMap<Integer, DBDiskBetRank>());
				}
				i =rid2DamageMap.get(rankList.combatId);
				for (SBean.DBDiskBetRank r : rankList.DiskBetRank) {							
					i.put(r.rid, r);
					setDamage(r.tId,r.score, r.rid, r.rname, r.serverName, r.headIconId, r.lvl);
				}
			}
			
		}
		if (diskBet == null)
			this.diskBet = new SBean.DBDiskBet(new ArrayList<SBean.DBDiskBetRankList>(),
					new ArrayList<SBean.DBDiskBetRole>(),new ArrayList<SBean.DBDiskBetRankList>(),new ArrayList<SBean.DBDiskBetRank>(),gs.getDayByOffset(RESET_TIME)
					,0, 0,0, 0,0, 0,0);
		saveTime = gs.getTime();
		
		
		
	}
	
	public void onTimer()
	{
		int day = gs.getDayByOffset(RESET_TIME);
		int curTime = gs.getTime();
		if (diskBet.day < day && sortDamageLists.size()>0&&curTime>diskBet.statime&&curTime>diskBet.endtime) {//
			diskBet.day = day;
			
			diskBet.ranksHis.clear();
			short index =1;
			List<DBDiskBetRank> allScoreRanks = getAllScoreRanks1((short) 1);
			for(DBDiskBetRank slist : allScoreRanks){
				if(slist.score>=diskBet.minscore){//DiskBetConfig.MIN_SCORE
					continue;
				}
				List<DropEntryNew> giveBonus = giveBonus((short) 1,(short)index);
  	   			 if(giveBonus.size()>0){
  	   			    if(diskBet.ranksHis.size()<4){
	   					 diskBet.ranksHis.add(slist);
	   				 }
  	   				gs.getLoginManager().sysSendMessage(0, slist.rid, DBMail.Message.SUB_TYPE_DISK_BET, "", ""+index, 0, true, giveBonus);
  	   				index++;
  	   			 } 
			}
			
			index =1; 
			for(DBDiskBetRank slist : allScoreRanks){
				
				if(slist.score<diskBet.minscore){
					break;
				}
				List<DropEntryNew> giveBonus = giveBonus((short) 2,(short)index);
  	   			 if(giveBonus.size()>0){  	   			   
  	   				gs.getLoginManager().sysSendMessage(0, slist.rid, DBMail.Message.SUB_TYPE_DISK_BET_HAOHUA, "", ""+index, 0, true, giveBonus);
  	   				index++;
  	   			 } 
			}
			
			index =1;
//			List<DBDiskBetRank> allScoreRanks2 = getAllScoreRanks1((short) 2);
//			for(DBDiskBetRank slist : allScoreRanks2){
//				if(slist.score<diskBet.minRmb){
//					continue;
//				}
//				List<DropEntryNew> giveBonus = giveBonus((short) 3,(short)index);
//  	   			 if(giveBonus.size()>0){
//  	   				gs.getLoginManager().sysSendMessage(0, slist.rid, DBMail.Message.SUB_TYPE_DISK_BET_RMB, "", ""+index, 0, true, giveBonus);
//  	   				index++;
//  	   			 } 
//			}
			
           
            diskBet.ranks.clear();
            diskBet.pool=0;
			
			sortDamageLists.clear();
			rid2DamageMap.clear();
			
            sortDamageListsHis.clear();
            rolesMap.clear();
            diskBet.roles.clear();
           
           bonus = false ;
		}
			
		int time = gs.getTime();
		if( time > saveTime + SAVE_INTERVAL ) {
			save();
			saveTime = time;
		}
	}
	
	public synchronized void save()
	{
		gs.getDB().execute(new SaveTrans(diskBet.kdClone()));
		if(diskBet.statime>0&&gs.getGameActivities()!=null&&gs.getGameActivities().getDiskBetActivity()!=null){
			DiskBetConfig cfg = gs.getGameActivities().getDiskBetActivity().getOpenedConfigById(diskBet.statime);
			if(cfg!=null&&cfg.mailatt3!=null&&cfg.mailatt4!=null&&cfg.mailatt5!=null){
				mailatt3=cfg.mailatt3;
				mailatt4=cfg.mailatt4;
				mailatt5=cfg.mailatt5;
			}
		}
	}

	synchronized void setDamage(short combatId,int damage, int id, String name, String serverName, short icon, short lvl)
	{
		removeScoreSortList(id,combatId);
		insertScoreSortList(damage, id, combatId);
		Map<Integer, DBDiskBetRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
		if(rid2ScoreMapi==null){
			rid2DamageMap.put(combatId, new HashMap<Integer, DBDiskBetRank>());
		}
		rid2ScoreMapi =rid2DamageMap.get(combatId);
		SBean.DBDiskBetRank d = rid2ScoreMapi.get(id);
		if (d == null) {
			d = new SBean.DBDiskBetRank(combatId,serverName, id, name, icon, lvl, damage,0);
			rid2ScoreMapi.put(id, d);
			
			List<DBDiskBetRank> ranksBossList =null;
			
            for(DBDiskBetRankList  ranksl :diskBet.ranks){
				if(ranksl.combatId==combatId){
					ranksBossList=ranksl.DiskBetRank;
				}
			}
                     
            if(ranksBossList==null){
            	DBDiskBetRankList  rankslst = new DBDiskBetRankList();
            	rankslst.combatId=combatId;
            	rankslst.DiskBetRank = new  ArrayList<DBDiskBetRank>();
				diskBet.ranks.add(rankslst);
				for(DBDiskBetRankList  ranksl :diskBet.ranks){
					if(ranksl.combatId==combatId){
						ranksBossList=ranksl.DiskBetRank;
					}
				}
			}
			
            ranksBossList.add(d);
		}
		else {
			d.tId=combatId;
			d.serverName = serverName;
			d.score = damage;
			d.rname = name;
			d.headIconId = icon;
			d.lvl = lvl;
		}

		SBean.DBDiskBetRole dr = rolesMap.get(id);
		if (dr == null) {
			dr = new SBean.DBDiskBetRole(id, combatId, (short)0, (short)0, 0);
			rolesMap.put(id, dr);
			diskBet.roles.add(dr);
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
		int count = 0;
		for(int i = mid; i<size;i++){
			if(sortScoreList.get(i).damage==damage){
				count++;
			}else{
				break;
			}
			
		}
		sortScoreList.add(mid+count, new SortDamage(damage, id));
	}
	
	private void removeScoreSortList(int id, short combatId)
	{
		List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
		if (sortDamageList == null)
			return;
		Map<Integer, DBDiskBetRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
		if(rid2ScoreMapi==null){
			rid2DamageMap.put(combatId, new HashMap<Integer, DBDiskBetRank>());
		}
		rid2ScoreMapi =rid2DamageMap.get(combatId);
		SBean.DBDiskBetRank d = rid2ScoreMapi.get(id);
		if (d == null)
			return;
		int index = getScoreSortList(d.score, id,combatId);
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
	
	public synchronized int  getDiskBetPool(){
		return diskBet.pool;
	}
	
	public synchronized int  addDiskBetPool(int po){
		diskBet.pool+=po;
		return diskBet.pool;
	}
				
	GameServer gs;
	SBean.DBDiskBet diskBet;
	Map<Integer, SBean.DBDiskBetRole> rolesMap = new ConcurrentHashMap<Integer, SBean.DBDiskBetRole>();
	Map<Short, List<SortDamage>> sortDamageLists = new ConcurrentHashMap<Short,List<SortDamage>>();
	Map<Short,Map<Integer, SBean.DBDiskBetRank>> rid2DamageMap = new ConcurrentHashMap<Short,Map<Integer, SBean.DBDiskBetRank>>();
	
	Map<Short, List<DBDiskBetRank>> sortDamageListsHis = new ConcurrentHashMap<Short,List<DBDiskBetRank>>();
	
	
	public List<SBean.DiskBetItemsCFGS> mailatt3 = new ArrayList<SBean.DiskBetItemsCFGS>();
	public List<SBean.DiskBetItemsCFGS> mailatt4 = new ArrayList<SBean.DiskBetItemsCFGS>();
	public List<SBean.DiskBetItemsCFGS> mailatt5 = new ArrayList<SBean.DiskBetItemsCFGS>();
	
	int saveTime;
	
	public static final long MAX_WAIT_TIME = 3000;
	
	public boolean bonus  = false;

	synchronized List<SBean.DBDiskBetRank> getHisRanks()
	{
		return diskBet.ranksHis;
	}
	
	synchronized List<SBean.DBDiskBetRank> getAllScoreRanks(short combatId)
	{	
		List<DBDiskBetRank> ranks = new ArrayList<DBDiskBetRank>();
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
	    if (sortDamageList == null)
		return ranks;
		for (SortDamage s : sortDamageList) {
			Map<Integer, DBDiskBetRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
			if(rid2ScoreMapi==null){
				rid2DamageMap.put(combatId, new HashMap<Integer, DBDiskBetRank>());
			}
			rid2ScoreMapi =rid2DamageMap.get(combatId);
			SBean.DBDiskBetRank dr = rid2ScoreMapi.get(s.rid);
			if (dr == null)
				continue;
			if(combatId==2||dr.score<diskBet.minscore){
				ranks.add(new SBean.DBDiskBetRank(combatId,dr.serverName,dr.rid, dr.rname, dr.headIconId,dr.lvl, dr.score,dr.rmb));
			}		
			if(ranks.size()>=100){
				break;
			}
		}
		
		return ranks;
	}
	
	synchronized List<SBean.DBDiskBetRank> getAllScoreRanks2(short combatId)
	{	
		List<DBDiskBetRank> ranks = new ArrayList<DBDiskBetRank>();
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
	    if (sortDamageList == null)
		return ranks;
		for (SortDamage s : sortDamageList) {
			Map<Integer, DBDiskBetRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
			if(rid2ScoreMapi==null){
				rid2DamageMap.put(combatId, new HashMap<Integer, DBDiskBetRank>());
			}
			rid2ScoreMapi =rid2DamageMap.get(combatId);
			SBean.DBDiskBetRank dr = rid2ScoreMapi.get(s.rid);
			if (dr == null)
				continue;
			if(dr.score>=diskBet.minscore){
				ranks.add(new SBean.DBDiskBetRank(combatId,dr.serverName,dr.rid, dr.rname, dr.headIconId,dr.lvl, dr.score,dr.rmb));
			}	
			if(ranks.size()>=100){
				break;
			}
		}
		
		return ranks;
	}
	
	synchronized List<SBean.DBDiskBetRank> getAllScoreRanks1(short combatId)
	{	
		List<DBDiskBetRank> ranks = new ArrayList<DBDiskBetRank>();
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
	    if (sortDamageList == null)
		return ranks;
		for (SortDamage s : sortDamageList) {
			Map<Integer, DBDiskBetRank> rid2ScoreMapi =rid2DamageMap.get(combatId);
			if(rid2ScoreMapi==null){
				rid2DamageMap.put(combatId, new HashMap<Integer, DBDiskBetRank>());
			}
			rid2ScoreMapi =rid2DamageMap.get(combatId);
			SBean.DBDiskBetRank dr = rid2ScoreMapi.get(s.rid);
			if (dr == null)
				continue;
			
			ranks.add(new SBean.DBDiskBetRank(combatId,dr.serverName,dr.rid, dr.rname, dr.headIconId,dr.lvl, dr.score,dr.rmb));
//			if(ranks.size()>=10000){
//				break;
//			}
		}
		
		return ranks;
	}
	
	synchronized int getselfRank(short combatId,int rid)
	{	
		short combatId2=combatId;
		if(combatId==3){
			combatId2=1;
		}
		
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId2);
	    if (sortDamageList == null)
		return -1;
	    int index =1;
		for (SortDamage s : sortDamageList) {
			if(rid==s.rid){
				
				if(combatId==3&&s.damage<diskBet.minscore){
					return -1;
				}
				
				if(combatId==1&&s.damage>=diskBet.minscore){
					return -1;
				}
				
				if(combatId==1&&s.damage<diskBet.minscore){
					int i = index-getspeRank(combatId);
					if(i<=0){
						i=1;
					}
					return i;
				}
				
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	
	synchronized int getspeRank(short combatId)
	{	
	    List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
	    if (sortDamageList == null)
		return 0;
	    int index =0;
		for (SortDamage s : sortDamageList) {
				
			if(s.damage<diskBet.minscore){
					break;
			}
				
			index++;
		}
		
		return index;
	}
	
	
	public synchronized List<SBean.DropEntryNew> giveBonus(short tId,short rank)
	{
		List<SBean.DropEntryNew>  dropList= new ArrayList<SBean.DropEntryNew>();
		
		List<SBean.DiskBetItemsCFGS> mailatt = null;
		if(tId ==1){
			mailatt =mailatt3;
		}else if(tId ==2){
			mailatt =mailatt4;
		}else if(tId ==3){
			mailatt =mailatt5;
		}
		
		if(mailatt==null){
			if(diskBet.statime>0&&gs.getGameActivities()!=null&&gs.getGameActivities().getDiskBetActivity()!=null){
				DiskBetConfig cfg = gs.getGameActivities().getDiskBetActivity().getConfigById(diskBet.statime);
				if(cfg!=null&&cfg.mailatt3!=null&&cfg.mailatt4!=null&&cfg.mailatt5!=null){
					mailatt3=cfg.mailatt3;
					mailatt4=cfg.mailatt4;
					mailatt5=cfg.mailatt5;
				}
			}
			
			if(tId ==1){
				mailatt =mailatt3;
			}else if(tId ==2){
				mailatt =mailatt4;
			}else if(tId ==3){
				mailatt =mailatt5;
			}
			
			if(mailatt==null){
				return dropList;
			}
			
		}
		
		List<DropEntryNew> bonus = null;
		for(DiskBetItemsCFGS bon :mailatt){
			if(bon.rankmin<=rank&&bon.rankmax>=rank){
				bonus =bon.eItems;
			}
		}
		
		if(bonus==null){
			for(DiskBetItemsCFGS bon :mailatt){
				if(bon.rankmax==-1){
					bonus =bon.eItems;
				}
			}
		}
				
		if(bonus!=null){		
			for(DropEntryNew item : bonus){			
				dropList.add(item);			
			}			
		}
		
		
		return dropList;
	}
	
}

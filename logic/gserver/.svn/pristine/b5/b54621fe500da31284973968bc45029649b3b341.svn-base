package i3k.gs;

import i3k.SBean;
import i3k.SBean.DBPetBrief;
import i3k.DBRoleGeneral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class MarchManager {
	
	public static int SAVE_INTERVAL = 60 * 60;
	
	public class SortPower
	{
		SortPower(int power, int id)
		{
			this.listId = id;
			this.power = power;
		}
		int listId;
		int power;
	}
	
	public class PowerRankList 
	{
		PowerRankList(SBean.DBPowerRankList list)
		{
			if (list == null) {
				this.list = new SBean.DBPowerRankList();
				this.list.ranks = new ArrayList<SBean.DBPowerRank>();
			}
			else
				this.list = list;
			
			rid2ListId = new HashMap<Integer, Integer>();
			sortList = new ArrayList<SortPower>();
			int index = 0;
			for (SBean.DBPowerRank r : this.list.ranks) {
				rid2ListId.put(r.roleid, index);
				insertSortList(r.power, index ++);
			}
		}
		
		void setRank(SBean.DBPowerRank rank)
		{
			Integer id = rid2ListId.get(rank.roleid);
			if (id == null) {
				list.ranks.add(rank);
				int listId = list.ranks.size() - 1;
				rid2ListId.put(rank.roleid, listId);
				insertSortList(rank.power, listId);
			}
			else {
				removeSortList(list.ranks.get(id).power, id);
				list.ranks.set(id, rank);
				insertSortList(rank.power, id);
			}
		}
		
		void insertSortList(int power, int id)
		{
			int size = sortList.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (power > sortList.get(mid).power) {
					end = mid - 1;
				} else if (power < sortList.get(mid).power) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			sortList.add(mid, new SortPower(power, id));
		}
		
		void removeSortList(int power, int id)
		{
			int index = getSortList(power, id);
			if (index >= 0)
				sortList.remove(index);
		}
		
		int getSortList(int power, int id)
		{
			int size = sortList.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (power >= sortList.get(mid).power) {
					end = mid - 1;
				} else if (power < sortList.get(mid).power) {
					begin = mid + 1;
				}
			}
			int tmp = mid;
			if (sortList.get(tmp).power != power)
				tmp ++;
			SortPower p = null;
			while (tmp < size && (p = sortList.get(tmp)).power == power) {
				if (p.listId == id) {
					return tmp;
				}
				tmp ++;
			}
			return -1;
		}
		
		int getRoleRank(int rid)
		{
			Integer r = rid2ListId.get(rid);
			if( r == null )
				return -1;
			SBean.DBPowerRank rank = list.ranks.get(r);
			if (rank == null)
				return -1;
			
			int index = getSortList(rank.power, r);
			if (index >= 0)
				return index + 1;
			return -1;
		}
		
		SBean.DBPowerRank copyRoleRankData(int rid)
		{
			Integer r = rid2ListId.get(rid);
			if( r == null )
				return null;
			SBean.DBPowerRank rank = list.ranks.get(r);
			if (rank == null)
				return null;
			
			return rank.kdClone();
		}
		
		int getRoleUpdateTime(int rid)
		{
			Integer r = rid2ListId.get(rid);
			if( r == null )
				return -1;
			SBean.DBPowerRank rank = list.ranks.get(r);
			if (rank == null)
				return -1;
			
			return rank.updateTime;
		}
		
		int getRankRole(int rank)
		{
			if (rank < 1)
				rank = 1;
			if (rank > sortList.size())
				return -rank;
			SortPower power = sortList.get(rank - 1);
			if( power == null )
				return -rank;
			SBean.DBPowerRank r = list.ranks.get(power.listId);
			if (r == null)
				return -rank;
			return r.roleid;
		}
		
		SBean.DBPowerRankList getDBPowerRankList()
		{
			return list.kdClone();
		}
		
		SBean.DBPowerRankList list;
		Map<Integer, Integer> rid2ListId;
		List<SortPower> sortList;
	}
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBPowerRankList list)
		{
			rankList = list;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("powerRanks");
			byte[] data = Stream.encodeLE(rankList);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("power rank save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBPowerRankList rankList;
	}
	
	public static class AttackFinishRes
	{
		public boolean bWin;
		public short stage;
	}
	
	public static class AttackStartRes
	{
		public int roleID;
		public String roleName;
		public List<DBRoleGeneral> sgenerals;
		public List<DBRoleGeneral> egenerals;
		public List<SBean.DBRoleMarchGeneralState> sgeneralStates;
		public List<SBean.DBRoleMarchGeneralState> egeneralStates;
		public List<DBPetBrief> pets;
		public List<DBPetBrief> epets;
		public List<SBean.DBRelation> srelation;
		public List<SBean.DBRelation> erelation;
		public List<SBean.DBGeneralStone> sgStones;
		public List<SBean.DBGeneralStone> egStones;
	}
	
	public MarchManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBPowerRankList rankList)
	{
		this.rankList = new PowerRankList(rankList);
		saveTime = gs.getTime();
	}
	
	public synchronized List<Integer> getRoleInfo(int rid, Role.MarchStateInfo info)
	{
		List<Integer> rids = new ArrayList<Integer>();
		if( info.enemies == null ) {
			int[] ranks = selectEnemy(getPowerRoleRank(rid));
			info.enemies = new ArrayList<SBean.DBRoleMarchEnemy>();
			for(int e : ranks)
			{
				int eid = getPowerRankRole(e);
				info.enemies.add(new SBean.DBRoleMarchEnemy(eid, (byte)0, (byte)1, "", "", null, null));
				if( eid > 0 )
					rids.add(eid);
			}
		}
		return rids;
	}
	
	public synchronized boolean checkPowerRoleRank(int rid)
	{
		int rank = getPowerRoleRank(rid);
		if (rank < 0)
			return true;
		return false;
	}
	
	private int[] selectEnemy(int rankNow)
	{
		int rankMax = gs.getGameData().getArenaCFG().rankMax;
		Random rand = gs.getGameData().getRandom();
		if( rankNow <= 0 || rankNow > rankMax )
			rankNow = rankMax;
		List<SBean.MarchTargetCFGS> lst = gs.getGameData().getMarchCFG().targets;
		SBean.MarchTargetCFGS t = null;
		for(SBean.MarchTargetCFGS e : lst)
		{
			if( rankNow <= e.rankFloor )
			{
				t = e;
				break;
			}
		}
		int[] res = new int[t.deltaMin.size()];
		for(int i = 0; i < res.length; ++i)
		{
			boolean collision = false;
			do {
				int mind = t.deltaMin.get(i);
				int d = t.deltaMax.get(i) - mind;
				if( d <= 0)
					res[i] = rankNow+mind;
				else
					res[i] = rankNow+mind+rand.nextInt(d);
				if( res[i] > rankMax )
					res[i] = rankMax;
				collision = false;
				for (int j = 0; j < i; j ++) {
					if (res[i] == res[j]) {
						collision = true;
						break;
					}
				}
			} while (collision);
		}
		return res;
	}
	
	public synchronized int getPowerRoleRank(int rid)
	{
		return rankList.getRoleRank(rid);
	}
	
	public synchronized int getPowerRoleUpdateTime(int rid)
	{
		return rankList.getRoleUpdateTime(rid);
	}
	
	public synchronized int getPowerRankRole(int rank)
	{
		return rankList.getRankRole(rank);
	}
	
	public synchronized SBean.DBPowerRank copyPowerRoleRankData(int rid)
	{
		return rankList.copyRoleRankData(rid);
	}
	
	public void onTimer()
	{
		if( gs.getTime() > saveTime + SAVE_INTERVAL ) {
			save();
			saveTime = gs.getTime();
		}
	}
	
	public synchronized void save()
	{
		gs.getDB().execute(new SaveTrans(rankList.getDBPowerRankList()));
	}
	
	public synchronized void setRank(SBean.DBPowerRank rank)
	{
		this.rankList.setRank(rank);
	}
	
	GameServer gs;
	PowerRankList rankList;
	int saveTime;

}

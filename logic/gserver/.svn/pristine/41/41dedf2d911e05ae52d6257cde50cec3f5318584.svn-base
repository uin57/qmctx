package i3k.gs;

import i3k.SBean;
import i3k.SBean.DBTreasureMap;
import i3k.SBean.DBTreaureReward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class TreasureMapManager {
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBTreasure treasure)
		{
			this.treasure = treasure;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("treasure");
			byte[] data = Stream.encodeLE(treasure);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("treasure save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBTreasure treasure;
	}
	
	public TreasureMapManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBTreasure treasure)
	{
		this.treasure = treasure;
		if (treasure == null) {
			this.treasure = new SBean.DBTreasure();
			this.treasure.treasureMaps = new ArrayList<SBean.DBTreasureMap>();
		} else {
			treasureMaps.clear();
			for (SBean.DBTreasureMap item : this.treasure.treasureMaps) {
				if (treasureMaps.get(item.id) != null) {
					treasureMaps.get(item.id).add(item);
				} else {
					List<SBean.DBTreasureMap> list = new ArrayList<SBean.DBTreasureMap>();
					list.add(item);
					treasureMaps.put(item.id, list);
				}
			}
		}
	}
	
	public synchronized void onTimer()
	{
		save();
	}
	
	public synchronized void save()
	{
		List<SBean.DBTreasureMap> list = new ArrayList<SBean.DBTreasureMap>();
		for (List<SBean.DBTreasureMap> value : treasureMaps.values()) {
			list.addAll(value);
		}
		treasure.treasureMaps = list;
		gs.getDB().execute(new SaveTrans(treasure.kdClone()));
	}
	
	public void addTreasureMap(SBean.DBTreasureMap item) {
		if (treasureMaps.get(item.id) != null) {
			treasureMaps.get(item.id).add(item);
		} else {
			List<SBean.DBTreasureMap> list = new ArrayList<SBean.DBTreasureMap>();
			list.add(item);
			treasureMaps.put(item.id, list);
		}
	}

	public List<SBean.DBTreasureMap> getTreasureMapList(byte id, int rid) {
	    SBean.TreasureMapBaseCFGS cfgs = gs.gameData.treasureMap;
		List<SBean.DBTreasureMap> resultList = new ArrayList<SBean.DBTreasureMap>();
		List<SBean.DBTreasureMap> list = treasureMaps.get(id);
		if (list != null) {
		    for (int i = 0; i < list.size(); i++) {
		        DBTreasureMap DBTreasureMapItem = list.get(i);
		        if (DBTreasureMapItem.rid != rid && DBTreasureMapItem.otherDigCnt < cfgs.mapDigMaxCount) {
		            resultList.add(DBTreasureMapItem);
		        }
                if (DBTreasureMapItem.otherDigCnt >= cfgs.mapDigMaxCount) {
                    list.remove(DBTreasureMapItem);
                    i--;
                }
            }
		} 
		Collections.sort(resultList, new Comparator<SBean.DBTreasureMap>() {

			@Override
			public int compare(DBTreasureMap o1, DBTreasureMap o2) {
				return (int) (o1.createTime - o2.createTime);
			}
		});
		return resultList;
	}
	
	public SBean.DBTreasureMap getRoleTreasureMap(int rid) {
		SBean.DBTreasureMap result = null;
		for (List<SBean.DBTreasureMap> value : treasureMaps.values()) {
			for (DBTreasureMap DBTreasureMapItem : value) {
				if (DBTreasureMapItem.rid == rid) {
					return DBTreasureMapItem;
				}
			}
		}
		return result;
	}
	
	public DBTreasureMap getTreasureMap(byte id, int rid) {
	    List<SBean.DBTreasureMap> list = getTreasureMapList(id, 0);
	    if (list != null) {
            for (DBTreasureMap DBTreasureMapItem : list) {
                if (DBTreasureMapItem.rid == rid && DBTreasureMapItem.id == id) {
                    return DBTreasureMapItem;
                }
            }
        }
        return null;
    }
	
	public synchronized boolean removeTreasureMap(int rid, byte id) {
		List<SBean.DBTreasureMap> list = getTreasureMapList(id, 0);
		if (list != null) {
			for (DBTreasureMap DBTreasureMapItem : list) {
				if (DBTreasureMapItem.rid == rid && DBTreasureMapItem.id == id) {
					return treasureMaps.get(id).remove(DBTreasureMapItem);
				}
			}
		}
		return false;
	}
	
	public int getTotalCount() {
		int count = 0;
		for (List<SBean.DBTreasureMap> value : treasureMaps.values()) {
			count += value.size();
		}
		return count;
	}
	
	public synchronized boolean digTreasureMap(int rid, byte id, byte num, String name, int digger) {
	    boolean result = false;
	    List<SBean.DBTreasureMap> list = getTreasureMapList(id, 0);
	    if (list == null) {
	        return false;
	    }
        if (list != null) {
            for (DBTreasureMap map : list) {
                if (map.rid == rid && map.id == id) {
                    SBean.DBTreaureReward reward = null;
                    for (DBTreaureReward dbTreaureReward : map.rewards) {
                        if (dbTreaureReward.num == num) {
                            reward = dbTreaureReward;
                            break;
                        }
                    }
                    if (reward == null || reward.isOpen == 1) {
                        return false;
                    }
                    reward.isOpen = 1;
                    reward.diggerName = name;
                    map.diggers.add(digger);
                    map.openedCells.add(num);
                    result = true;
                    break;
                }
            }
        }
	    return result;
	}
	
	GameServer gs;
	public static Map<Byte,List<SBean.DBTreasureMap>> treasureMaps = new ConcurrentHashMap<Byte,List<SBean.DBTreasureMap>>();
	SBean.DBTreasure treasure;
    
}

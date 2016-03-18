package i3k.gs;

import i3k.DBMail;
import i3k.SBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class PetZanManager {
	
	public static int SAVE_INTERVAL = 90 * 60;
	
	public static class ZanBack
	{
		int rid;
		short pid;
		int back;
	}
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(List<SBean.DBPetZan> list)
		{
			zanList = list;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("petzan");
			byte[] data = Stream.encodeListLE(zanList);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("pet zan save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		List<SBean.DBPetZan> zanList;
	}
	
	public PetZanManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(List<SBean.DBPetZan> zanList)
	{
		zanMap.clear();
		if (zanList != null) {
			for (SBean.DBPetZan z : zanList)
				zanMap.put(z.rid, z);
		}
		saveTime = gs.getTime();
	}
	
	public void onTimer()
	{
		int day = gs.getDayCommon();
		Map<Integer, ZanBack> back = new HashMap<Integer, ZanBack>();
		synchronized (this) {
			for (SBean.DBPetZan z : zanMap.values()) {
				if (z.day < day) {
					z.totalTimes = 0;
					z.day = (short)day;
				}
		        Iterator<SBean.DBPetZanPet> itr = z.pets.iterator();
		        while (itr.hasNext()) {
		        	SBean.DBPetZanPet p = itr.next();
		        	int zanDay = gs.getDayCommon(p.time);
					if (day > zanDay) {
						ZanBack zb = back.get(z.rid);
						if (zb == null) {
							ZanBack b = new ZanBack();
							b.rid = z.rid;
							b.pid = p.pid;
							b.back = p.reward * p.count;
							back.put(z.rid, b);
						}
						else {
							zb.back += p.reward * p.count;
						}
		                itr.remove();
		            }
		        }
			}
		}
		
		for (ZanBack b : back.values()) {
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			if (b.back > 0) {
				attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, b.back));
				gs.getLoginManager().sysSendMessage(0, b.rid, DBMail.Message.SUB_TYPE_ZAN_BACK, "", "" + b.pid, 0, true, attLst);				
			}
		}
		
		int time = gs.getTime();
		if( time > saveTime + SAVE_INTERVAL ) {
			save();
			saveTime = time;
		}
	}
	
	public synchronized void save()
	{
		List<SBean.DBPetZan> list = new ArrayList<SBean.DBPetZan>();
		for (SBean.DBPetZan z : zanMap.values())
			list.add(z.kdClone());
		gs.getDB().execute(new SaveTrans(list));
	}
	
	public synchronized boolean zanOffer(Role role, short pid, short reward, short count, String message)
	{
		SBean.DBPetZan zan = zanMap.get(role.id);
		if (zan == null) {
			zan = new SBean.DBPetZan();
			zan.rid = role.id;
			zan.totalTimes = 0;
			zan.day = (short)gs.getDayCommon();
			zan.pets = new ArrayList<SBean.DBPetZanPet>();
		}
		
		SBean.DBPetZanPet p = new SBean.DBPetZanPet();
		p.pid = pid;
		p.reward = reward;
		p.count = count;
		p.time = gs.getTime();
		p.rids = new ArrayList<Integer>();
		zan.pets.add(p);
		zan.maxTimes = gs.getGameData().getZanOfferTimesMax(role.lvlVIP);
		zan.vip = role.lvlVIP;
		zanMap.put(role.id, zan);
		role.announceZanOffer(role.id, pid, reward, count, message);
		return true;
	}
	
	public synchronized short takeOffer(int trid, int orid, short pid, short reward, int zanVit, int activity, int[] vipTimes)
	{
		SBean.DBPetZan zan = zanMap.get(orid);
		if (zan == null) 
			return 0;
		
		vipTimes[0] = zan.vip;
		vipTimes[1] = zan.maxTimes;
		
		SBean.DBPetZanPet pet = null;
		Set<Integer> takenRids = new HashSet<Integer>();
		for (SBean.DBPetZanPet p : zan.pets) {
			if (p.pid == pid) {
				for (int rid : p.rids)
					takenRids.add(rid);
			}
				
			if (p.pid == pid && p.reward == reward && p.count > 0) {
				if (pet == null)
					pet = p;
			}
		}

		if (takenRids.contains(trid))
			return -1;

		if (pet == null || pet.reward <= 0)
			return -3;

		if (zan.totalTimes >= zan.maxTimes)
			return -2;
		
		if (zanVit > activity)
			return -4;
		
		zan.totalTimes ++;
		pet.count --;
		pet.rids.add(trid);
		return pet.reward;
	}
	
	GameServer gs;
	Map<Integer, SBean.DBPetZan> zanMap = new HashMap<Integer, SBean.DBPetZan>();
	int saveTime;

}

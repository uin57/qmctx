package i3k.gs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import i3k.DBMail;
import i3k.SBean;
import i3k.DBRoleGeneral;
import i3k.SBean.DBRichMapBuilding;
import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class RichManager {
	
	static final int MAP_LENGTH = 36;
	static final int MAP_INNER_LENGTH = 28;
	static final int MAP_MINI_LENGTH = 7;
	
	static final byte MAP_TYPE_NONE = 0;
	static final byte MAP_TYPE_MINE = 1;
	static final byte MAP_TYPE_RICH_MINE = 2;
	static final byte MAP_TYPE_DIVINE = 3;
	static final byte MAP_TYPE_BLACK_SHOP = 4;
	static final byte MAP_TYPE_LOTERY = 5;
	static final byte MAP_TYPE_JUBAO = 6;
	
	static final byte MAP_SIZE_NONE = 1;
	static final byte MAP_SIZE_MINE = 1;
	static final byte MAP_SIZE_RICH_MINE = 3;
	static final byte MAP_SIZE_BLACK_SHOP = 2;
	static final byte MAP_SIZE_LOTERY = 2;
	static final byte MAP_SIZE_JUBAO = 3;
	static final byte MAP_SIZE_DIVINE = 2;
	
	static final byte OBJECT_TYPE_NONE = 0;
	static final byte OBJECT_TYPE_ANGEL = 1;
	static final byte OBJECT_TYPE_DEVIL = 2;
	static final byte OBJECT_TYPE_TURTLE = 3;
	static final byte OBJECT_TYPE_BEGGAR = 4;
	static final byte OBJECT_TYPE_BMW = 5;
	static final byte OBJECT_TYPE_MONEY_GOD = 6;
	static final byte OBJECT_TYPE_BIG_MONEY_GOD = 7;
	static final byte OBJECT_TYPE_QUESTION_MARK = 8;
	static final byte OBJECT_TYPE_TRANSFER_DOOR = 9;
	static final byte OBJECT_TYPE_MINI_GOLD = 10;
	static final byte OBJECT_TYPE_MONEY = 11;
	static final byte OBJECT_TYPE_BOX = 12;
	
	static final byte DIVINE_TYPE_COMMON = 1;
	static final byte DIVINE_TYPE_ADVANCED = 2;
	static final byte DIVINE_TYPE_LUCKY = 3;
	
	static final byte DATA_CHANGE_GOLD = 1;
	static final byte DATA_CHANGE_ANGEL = 2;
	static final byte DATA_CHANGE_DEVIL = 3;
	static final byte DATA_CHANGE_TURTLE = 4;
	static final byte DATA_CHANGE_MOVEMENT = 5;
	
	static final int STATUS_GO_WEST = 1;
	static final int STATUS_OBJECT_HANDLED = 2;
	static final int STATUS_FLY = 4;
	
	public static List<Integer> getObjectSyncParam(SBean.DBRichMapObjectExt obj) 
	{
		List<Integer> params = new ArrayList<Integer>();
		if (obj != null && obj.type == OBJECT_TYPE_MONEY) {
			params.add(obj.params.get(2));
			params.add(obj.params.get(3));
		}
		
		return params;
	}
	
	public static SBean.DBRichRole createDefaultRichRole()
	{
		SBean.RichCFGS cfgs = GameData.getInstance().getRichCFG();
		SBean.DBRichRole rich = new SBean.DBRichRole();
		rich.gold = cfgs.basic.initgold;
		rich.techTree = new ArrayList<SBean.DBRichTech>();
		for (SBean.RichTechCFG cfg : cfgs.techTree)
		{
			SBean.DBRichTech tech = new SBean.DBRichTech(cfg.id, cfg.lvlInit);
			rich.techTree.add(tech);
		}
		return rich;
	}
	
	public static class SortGold
	{
		SortGold(int gold, int rid)
		{
			this.rid = rid;
			this.gold = gold;
		}
		int rid;
		int gold;
	}
	
	public static class GoldRank
	{
		GoldRank(int rank, int rid, int gold, short icon, String name, short lvl)
		{
			this.rank = rank;
			this.rid = rid;
			this.gold = gold;
			this.icon = icon;
			this.name = name;
			this.lvl = lvl;
		}
		public int rank;
		public int rid;
		public int gold;
		public short icon;
		public String name;
		public short lvl;
	}
	
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
		DamageRank(int rank, int rid, int damage, short icon, String name, short lvl, List<SBean.DBRichRoleGeneralBrief> generals)
		{
			this.rank = rank;
			this.rid = rid;
			this.damage = damage;
			this.icon = icon;
			this.name = name;
			this.lvl = lvl;
			this.generals = generals;
		}
		public int rank;
		public int rid;
		public int damage;
		public short icon;
		public String name;
		public short lvl;
		public List<SBean.DBRichRoleGeneralBrief> generals;
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
	
	public static class LoteryReward
	{
		public LoteryReward(int roleId, byte right, boolean win, List<Byte> guess, List<SBean.DropEntryNew> attLst)
		{
			this.roleId = roleId;
			this.right = right;
			this.win = win;
			this.guess = guess;
			this.attLst = attLst;
		}
		int roleId;
		byte right;
		List<Byte> guess;
		boolean win;
		List<SBean.DropEntryNew> attLst;
	}
	
	public static class Rich
	{
		Rich(GameServer gs, SBean.DBRich rich) {
			this.gs = gs;
			if (rich == null) {
				this.rich = new SBean.DBRich();
				this.rich.map = new SBean.DBRichMap();
				this.rich.map.buildings = new ArrayList<SBean.DBRichMapBuilding>();
				this.rich.map.objects = new ArrayList<SBean.DBRichMapObject>();
				this.rich.gold = new ArrayList<SBean.DBRichRoleGold>();
				this.rich.bossDamage = new ArrayList<SBean.DBRichRoleBossDamage>();
				this.rich.lotery = new ArrayList<SBean.DBRichRoleLotery>();
				this.rich.mapExt = new ArrayList<SBean.DBRichMapExt>();
			}
			else
				this.rich = rich;
			
			for (SBean.DBRichRoleGold g : this.rich.gold) {
				rid2GoldMap.put(g.rid, g);
				setGold(g.gold, g.rid, g.name, g.icon, g.lvl, g.minReward);
			}
			
			for (SBean.DBRichRoleBossDamage d : this.rich.bossDamage) {
				rid2DamageMap.put(d.rid, d);
				setDamage(d.damage, d.rid, d.name, d.icon, d.lvl, d.combatId, d.generals);
			}

			for (SBean.DBRichRoleLotery l : this.rich.lotery)
				rid2LoteryMap.put(l.rid, l);
		}
		
		boolean onTimer()
		{
			boolean bSave = false;
			int newDay = gs.getDayCommon();
			List<Reward> rewardMails = new ArrayList<Reward>();
			List<LoteryReward> loteryRewardMails = new ArrayList<LoteryReward>();
			synchronized( this )
			{
				if (newDay > rich.day) // do clean up
				{
					gs.getLogger().info("rich reset");	
					rich.day = newDay;
					// Generate buildings
					rich.map.buildings = new ArrayList<SBean.DBRichMapBuilding>();
					rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_BLACK_SHOP, 0, new ArrayList<Integer>()));
					rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_JUBAO, 0, new ArrayList<Integer>()));
					rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_LOTERY, 0, new ArrayList<Integer>()));
					rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_DIVINE, 0, new ArrayList<Integer>()));
					rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_RICH_MINE, 0, new ArrayList<Integer>()));
					int noneCount = 0;
					int totalSize = 0;
					for (SBean.DBRichMapBuilding b : rich.map.buildings)
						totalSize += getMapTypeSize(b.type);
					for (int i = 5; i < 15; i ++) {
						byte type = MAP_TYPE_NONE;
						boolean retry = false;
						do {
							retry = false;
							type = (byte)gs.getGameData().getRandInt(MAP_TYPE_NONE, MAP_TYPE_MINE + 1);
							if (type == MAP_TYPE_NONE)
								noneCount ++;
							
							if (totalSize + getMapTypeSize(type) > MAP_INNER_LENGTH)
								retry = true;
							
							if (noneCount > 7 && type == MAP_TYPE_NONE)
								retry = true;
						}
						while (retry);
						totalSize += getMapTypeSize(type);
						rich.map.buildings.add(new SBean.DBRichMapBuilding(type, 0, new ArrayList<Integer>()));
					}
					while (totalSize < MAP_INNER_LENGTH) {
						totalSize += getMapTypeSize(MAP_TYPE_NONE);
						rich.map.buildings.add(new SBean.DBRichMapBuilding(MAP_TYPE_NONE, 0, new ArrayList<Integer>()));
					}
					boolean retry = false;
					do {
						retry = false;
						Collections.shuffle(rich.map.buildings);
						int pos = 0;
						for (SBean.DBRichMapBuilding b : rich.map.buildings) {
							int size = getMapTypeSize(b.type);
							if (pos == 0 && b.type != RichManager.MAP_TYPE_NONE) {
								retry = true;
								break;
							}
							if (size == 3 && ((pos <= 1) || (pos >= 4 && pos <= 8) || (pos >= 11 && pos <= 15) || (pos >= 18 && pos <= 22) || (pos >= 25))) {
								retry = true;
								break;
							}
							if (size == 2 && ((pos <= 1) || (pos >= 6 && pos <= 8) || (pos >= 13 && pos <= 15) || (pos >= 20 && pos <= 22) || (pos >= 27))) {
								retry = true;
								break;
							}
							pos += size;
						}
					}
					while (retry);
					SBean.RichCFGS cfg = gs.getGameData().getRichCFG();
					for (SBean.DBRichMapBuilding b : rich.map.buildings) {
						if (b.type == MAP_TYPE_MINE) {
							int count = cfg.mineCombats.get(0).combatIds.size();
							int id = gs.getGameData().getRandInt(0, count);
							b.params.add(id);
						}
						else if (b.type == MAP_TYPE_RICH_MINE) {
							int count = cfg.richMineCombats.get(0).combatIds.size();
							int id = gs.getGameData().getRandInt(0, count);
							b.params.add(id);
						}
						else if (b.type == MAP_TYPE_JUBAO) {
							int count = cfg.bossCombats.get(0).combatIds.size();
							int id = gs.getGameData().getRandInt(0, count);
							b.params.add(id);
						}
					}
					// Generate objects
					rich.map.objects = new ArrayList<SBean.DBRichMapObject>();
					/*
					rich.map.objects.add(new SBean.DBRichMapObject(OBJECT_TYPE_MONEY_GOD, (short)0));
					rich.map.objects.add(new SBean.DBRichMapObject(OBJECT_TYPE_BIG_MONEY_GOD, (short)0));
					rich.map.objects.add(new SBean.DBRichMapObject(OBJECT_TYPE_QUESTION_MARK, (short)0));
					rich.map.objects.add(new SBean.DBRichMapObject(OBJECT_TYPE_TRANSFER_DOOR, (short)0));
					*/
					noneCount = 0;
					for (int i = 0; i < 6; i ++) {
						byte type = OBJECT_TYPE_NONE;
						do {
							type = (byte)gs.getGameData().getRandInt(OBJECT_TYPE_NONE, OBJECT_TYPE_TRANSFER_DOOR + 1);
							if (type == OBJECT_TYPE_NONE)
								noneCount ++;
						}
						while (noneCount > 2 && type == OBJECT_TYPE_NONE);
						if (type != OBJECT_TYPE_NONE)
							rich.map.objects.add(new SBean.DBRichMapObject(type, (short)0));
					}
					Collections.shuffle(rich.map.objects);
					Set<Short> existPos = new HashSet<Short>();
					for (SBean.DBRichMapObject o : rich.map.objects) {
						short pos = 1;
						do {
							pos = (short)gs.getGameData().getRandInt(1, MAP_LENGTH);
						}
						while (existPos.contains(pos));
						existPos.add(pos);
						o.pos = pos;
					}
					if (rich.mapExt.size() == 0)
						rich.mapExt.add(new SBean.DBRichMapExt(new ArrayList<SBean.DBRichMapObjectExt>(), 0));
					else {
						SBean.DBRichMapExt ext = rich.mapExt.get(0);
						ext.objects.clear();
					}
					for (short i = 1; i < MAP_LENGTH; i ++) {
						byte[] bindex = {0};
						SBean.DBRichMapBuilding b = gs.getRichManager().getBuildingByPos(i, rich.map.buildings, bindex);
						if (b != null && b.type == MAP_TYPE_NONE && !existPos.contains(i)) {
							SBean.DBRichMapExt ext = rich.mapExt.get(0);
							SBean.DBRichMapObjectExt obj = new SBean.DBRichMapObjectExt(OBJECT_TYPE_MONEY, i, new ArrayList<Integer>());
							int total = 0;
							for (SBean.RichMoneyCFGS m : cfg.money) {
								total += m.poss;
							}
							int rand = gs.getGameData().getRandom().nextInt(total);
							total = 0;
							int randMoney = 0;
							for (SBean.RichMoneyCFGS p : cfg.money) {
								total += p.poss;
								if (total > rand)
									break;
								randMoney ++;
							}
							SBean.RichMoneyCFGS mcfg = cfg.money.get(randMoney);
							obj.params.add((int)mcfg.type);
							obj.params.add(mcfg.count);
							if (gs.gameData.getRandInt(0, 100) * 0.01f < cfg.basic.multiplyposs)
								obj.params.add(1);
							else
								obj.params.add(0);
							obj.params.add((int)mcfg.modelId);
							ext.objects.add(obj);
						}
					}
					if (rich.mapExt.size() > 0) {
						SBean.DBRichMapExt ext = rich.mapExt.get(0);						
						if (ext.objects.size() > 0) {
							int boxIndex = gs.getGameData().getRandInt(0, ext.objects.size());
							SBean.DBRichMapObjectExt obj = ext.objects.get(boxIndex);
							obj.type = OBJECT_TYPE_BOX;
							obj.params.clear();
							List<Integer> fee = new ArrayList<Integer>();
							for (SBean.RichBoxCFGS b : cfg.box) {
								if (gs.gameData.getRandInt(0, 100) * 0.01f < b.poss) {
									int f = 0;
									if (gs.gameData.getRandInt(0, 100) * 0.01f < b.freePoss)
										f = 0;
									else
										f = gs.gameData.getRandInt(b.feeMin, b.feeMax);
									fee.add(f);
								}
								else
									break;
							}
							obj.params.add(0);
							for (int f : fee)
								obj.params.add(f);
						}
					}
					
					rich.map.miniobjects = new ArrayList<SBean.DBRichMapObject>();
					for (int i = 1; i <= 6; i ++)
						rich.map.miniobjects.add(new SBean.DBRichMapObject(OBJECT_TYPE_MINI_GOLD, (short)i));
					bSave = true;
					
					sortDamageLists.clear();
					rid2DamageMap.clear();
					rich.bossDamage.clear();
					
					rewards = collectRewards();
					loteryRewards = collectLoteryRewards();
				}
				else if (rewards != null)
					sendRewards(rewardMails);
				else if (loteryRewards != null)
					sendLoteryRewards(loteryRewardMails);
			}
			
			for (Reward r : rewardMails)
				gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_RICH_REWARD, "", "" + r.rank, 0, true, r.attLst);
			for (LoteryReward r : loteryRewardMails) {
				String guess = "";
				for (byte b : r.guess)
					guess += b + "#";
				gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_RICH_LOTERY_REWARD, "", "#" + r.right + "#" + (r.win?1:0) + "#" + guess , 0, true, r.attLst);
			}
			
			if (dirty)
				bSave = true;
			dirty = false;
			
			return bSave;
		}
		
		synchronized void setGold(int gold, int id, String name, short icon, short lvl, int minReward)
		{
			removeGoldSortList(id);
			insertGoldSortList(gold, id);
			
			SBean.DBRichRoleGold g = rid2GoldMap.get(id);
			if (g == null) {
				g = new SBean.DBRichRoleGold(id, gold, name, icon, lvl, minReward);
				rid2GoldMap.put(id, g);
				rich.gold.add(g);
			}
			else {
				g.gold = gold;
				g.name = name;
				g.icon = icon;
				g.lvl = lvl;
				g.minReward = minReward;
			}
			
			dirty = true;
		}
		
		private void insertGoldSortList(int gold, int id)
		{
			int size = sortGoldList.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (gold > sortGoldList.get(mid).gold) {
					end = mid - 1;
				} else if (gold < sortGoldList.get(mid).gold) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			sortGoldList.add(mid, new SortGold(gold, id));
		}
		
		private void removeGoldSortList(int id)
		{
			SBean.DBRichRoleGold g = rid2GoldMap.get(id);
			if (g == null)
				return;
			int index = getGoldSortList(g.gold, id);
			if (index >= 0)
				sortGoldList.remove(index);
		}
		
		private int getGoldSortList(int gold, int id)
		{
			int size = sortGoldList.size();
			if (size == 0)
				return -1;
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (gold >= sortGoldList.get(mid).gold) {
					end = mid - 1;
				} else if (gold < sortGoldList.get(mid).gold) {
					begin = mid + 1;
				}
			}
			int tmp = mid;
			if (sortGoldList.get(tmp).gold != gold)
				tmp ++;
			SortGold p = null;
			while (tmp < size && (p = sortGoldList.get(tmp)).gold == gold) {
				if (p.rid == id) {
					return tmp;
				}
				tmp ++;
			}
			return -1;
		}
		
		synchronized void setDamage(int damage, int id, String name, short icon, short lvl, short combatId, List<SBean.DBRichRoleGeneralBrief> generals)
		{
			removeDamageSortList(id, combatId);
			insertDamageSortList(damage, id, combatId);
			
			SBean.DBRichRoleBossDamage d = rid2DamageMap.get(id);
			if (d == null) {
				d = new SBean.DBRichRoleBossDamage(id, damage, name, icon, lvl, combatId, generals);
				rid2DamageMap.put(id, d);
				rich.bossDamage.add(d);
			}
			else {
				d.damage = damage;
				d.name = name;
				d.icon = icon;
				d.lvl = lvl;
				d.combatId = combatId;
				d.generals = generals;
			}
			
			dirty = true;
		}
		
		private void insertDamageSortList(int damage, int id, short combatId)
		{
			List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
			if (sortDamageList == null) {
				sortDamageList = new ArrayList<SortDamage>();
				sortDamageLists.put(combatId, sortDamageList);
			}
			
			int size = sortDamageList.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (damage > sortDamageList.get(mid).damage) {
					end = mid - 1;
				} else if (damage < sortDamageList.get(mid).damage) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			sortDamageList.add(mid, new SortDamage(damage, id));
		}
		
		private void removeDamageSortList(int id, short combatId)
		{
			List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
			if (sortDamageList == null)
				return;
			
			SBean.DBRichRoleBossDamage d = rid2DamageMap.get(id);
			if (d == null)
				return;
			
			int index = getDamageSortList(d.damage, id, combatId);
			if (index >= 0)
				sortDamageList.remove(index);
		}
		
		private int getDamageSortList(int damage, int id, short combatId)
		{
			List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
			if (sortDamageList == null)
				return -1;
			
			int size = sortDamageList.size();
			if (size == 0)
				return -1;
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (damage >= sortDamageList.get(mid).damage) {
					end = mid - 1;
				} else if (damage < sortDamageList.get(mid).damage) {
					begin = mid + 1;
				}
			}
			int tmp = mid;
			if (sortDamageList.get(tmp).damage != damage)
				tmp ++;
			SortDamage p = null;
			while (tmp < size && (p = sortDamageList.get(tmp)).damage == damage) {
				if (p.rid == id) {
					return tmp;
				}
				tmp ++;
			}
			return -1;
		}
		
		synchronized void setLotery(int id, List<Byte> lotery)
		{
			SBean.DBRichRoleLotery l = rid2LoteryMap.get(id);
			if (l == null) {
				l = new SBean.DBRichRoleLotery(id, lotery);
				rid2LoteryMap.put(id, l);
				rich.lotery.add(l);
			}
			else 
				l.lotery = lotery;
			
			dirty = true;
		}
		
		public byte getMapTypeSize(byte type)
		{
			switch (type)
			{
			case MAP_TYPE_NONE:
				return MAP_SIZE_NONE;
			case MAP_TYPE_MINE:
				return MAP_SIZE_MINE;
			case MAP_TYPE_RICH_MINE:
				return MAP_SIZE_RICH_MINE;
			case MAP_TYPE_BLACK_SHOP:
				return MAP_SIZE_BLACK_SHOP;
			case MAP_TYPE_LOTERY:
				return MAP_SIZE_LOTERY;
			case MAP_TYPE_JUBAO:
				return MAP_SIZE_JUBAO;
			case MAP_TYPE_DIVINE:
				return MAP_SIZE_DIVINE;
			}
			
			return MAP_SIZE_NONE;
		}
		
		synchronized List<GoldRank> getGoldRanks(int rid)
		{			
			SBean.DBRichRoleGold g = rid2GoldMap.get(rid);
			int index = -1;
			if (g != null) {
				index = getGoldSortList(g.gold, rid);
			}
			
			//Set<Integer> ids = new HashSet<Integer>();
			List<GoldRank> ranks = new ArrayList<GoldRank>();
			for (byte i = 0; i < 32; i ++) {
				if (i >= sortGoldList.size())
					break;
				SortGold gold = sortGoldList.get(i);
				//if (ids.contains(gold.rid))
				//	continue;
				SBean.DBRichRoleGold rrg = rid2GoldMap.get(gold.rid);
				if (rrg == null)
					continue;
				GoldRank rank = new GoldRank(i+1, rrg.rid, rrg.gold, rrg.icon, rrg.name, rrg.lvl);
				ranks.add(rank);
				//ids.add(gold.rid);
			}
			if (index >= 32) {
				if (g != null) {
					GoldRank rank = new GoldRank(index+1, g.rid, g.gold, g.icon, g.name, g.lvl);
					ranks.add(rank);
				}
			}
			return ranks;
		}
		
		synchronized List<DamageRank> getDamageRanks(int rid, short combatId)
		{			
			List<DamageRank> ranks = new ArrayList<DamageRank>();
			List<SortDamage> sortDamageList = sortDamageLists.get(combatId);
			if (sortDamageList == null)
				return ranks;
			
			SBean.DBRichRoleBossDamage d = rid2DamageMap.get(rid);
			int index = -1;
			if (d != null) {
				index = getDamageSortList(d.damage, rid, combatId);
			}
			
			for (byte i = 0; i < 32; i ++) {
				if (i >= sortDamageList.size())
					break;
				SortDamage damage = sortDamageList.get(i);
				SBean.DBRichRoleBossDamage rrd = rid2DamageMap.get(damage.rid);
				if (rrd == null)
					continue;
				List<SBean.DBRichRoleGeneralBrief> generals = new ArrayList<SBean.DBRichRoleGeneralBrief>();
				for (SBean.DBRichRoleGeneralBrief g : rrd.generals)
					generals.add(g.kdClone());
				DamageRank rank = new DamageRank(i+1, rrd.rid, rrd.damage, rrd.icon, rrd.name, rrd.lvl, generals);
				ranks.add(rank);
			}
			if (index >= 32) {
				if (d != null) {
					List<SBean.DBRichRoleGeneralBrief> generals = new ArrayList<SBean.DBRichRoleGeneralBrief>();
					for (SBean.DBRichRoleGeneralBrief g : d.generals)
						generals.add(g.kdClone());
					DamageRank rank = new DamageRank(index+1, d.rid, d.damage, d.icon, d.name, d.lvl, generals);
					ranks.add(rank);
				}
			}
			return ranks;
		}
		
		private List<Integer> collectRewards()
		{
			List<Integer> rewards = new ArrayList<Integer>();
			for (int i = 0; i < sortGoldList.size(); i ++)
				rewards.add(i);
			return rewards;
		}
		
		private void sendRewards(List<Reward> rewardMails) 
		{
			if (rewards == null)
				return;
			
			List<SBean.RichRewardCFGS> cfgs = gs.getGameData().getRichCFG().rewards;
			int i = 0;
			while (!rewards.isEmpty() && i ++ < 1000) {
				int rank = rewards.get(0).intValue();
				rewards.remove(0);
				SBean.RichRewardCFGS reward = null;
				for(SBean.RichRewardCFGS e : cfgs)
				{
					if( rank < e.rankFloor )
					{
						reward = e;
						break;
					}
				}
				if( reward == null )
					continue;
				if (rank < 0 || rank >= sortGoldList.size())
					continue;
				int roleId = sortGoldList.get(rank).rid;
				if (roleId < 0)
					continue;
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				if (reward.money > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, reward.money));
				int rewardPoint = reward.point;
				SBean.DBRichRoleGold g = rid2GoldMap.get(roleId);
				if (g != null)
				{
					if (rewardPoint < g.minReward)
					{
						rewardPoint = g.minReward;
					}
				}
				if (rewardPoint > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_RICH_POINT, (short)0, rewardPoint));
				
				rewardMails.add(new Reward(roleId, rank, attLst));
			}
			
			if (rewards.isEmpty()) {
				rewards = null;
				sortGoldList.clear();
				rid2GoldMap.clear();
				rich.gold.clear();
			}
		}
		
		private List<Integer> collectLoteryRewards()
		{
			loteryResult = (byte)gs.getGameData().getRandInt(1, 19);
			List<Integer> rewards = new ArrayList<Integer>();
			for (int rid : rid2LoteryMap.keySet())
				rewards.add(rid);
			return rewards;
		}
		
		private void sendLoteryRewards(List<LoteryReward> rewardMails) 
		{
			if (loteryRewards == null)
				return;
			
			SBean.RichCFGS cfgs = gs.getGameData().getRichCFG();
			int i = 0;
			while (!loteryRewards.isEmpty() && i ++ < 1000) {
				int rid = loteryRewards.get(0).intValue();
				loteryRewards.remove(0);
				if (rid < 0)
					continue;
				SBean.DBRichRoleLotery l = rid2LoteryMap.get(rid);
				if (l == null)
					continue;
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				boolean win = false;
				List<Byte> guess = new ArrayList<Byte>();
				for (byte lo : l.lotery) {
					if (lo == loteryResult) {
						win = true;
					}
					guess.add(lo);
				}
				if (win)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_RICH_POINT, (short)0, cfgs.basic.loteryWinReward));
				else
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_RICH_POINT, (short)0, cfgs.basic.loteryLoseReward));
				
				rewardMails.add(new LoteryReward(rid, loteryResult, win, guess, attLst));
			}
			
			if (loteryRewards.isEmpty()) {
				loteryRewards = null;
				rid2LoteryMap.clear();
				rich.lotery.clear();
			}
		}
		
		synchronized SBean.DBRich getDBRich()
		{
			return rich.kdClone();
		}
		
		GameServer gs;
		SBean.DBRich rich;
		List<SortGold> sortGoldList = new ArrayList<SortGold>();
		Map<Short, List<SortDamage>> sortDamageLists = new HashMap<Short, List<SortDamage>>();
		Map<Integer, SBean.DBRichRoleGold> rid2GoldMap = new HashMap<Integer, SBean.DBRichRoleGold>();
		Map<Integer, SBean.DBRichRoleBossDamage> rid2DamageMap = new HashMap<Integer, SBean.DBRichRoleBossDamage>();
		Map<Integer, SBean.DBRichRoleLotery> rid2LoteryMap = new HashMap<Integer, SBean.DBRichRoleLotery>();
		boolean dirty = false;
		byte loteryResult;
		List<Integer> rewards;
		List<Integer> loteryRewards;
	}
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBRich rich)
		{
			this.rich = rich;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("rich2");
			byte[] data = Stream.encodeLE(rich);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("rich save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBRich rich;
	}
	
	public RichManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBRich rich)
	{
		this.rich = new Rich(gs, rich);
	}
	
	public void save()
	{
		gs.getDB().execute(new SaveTrans(rich.getDBRich()));
	}
	
	public void onTimer()
	{
		boolean save = rich.onTimer();
		if (save)
			save();
	}
	
	public SBean.DBRich getRich()
	{
		return rich.getDBRich();
	}
	
	public byte getMapTypeSize(byte type)
	{
		return rich.getMapTypeSize(type);
	}
	
	public void setGold(int newGold, int rid, String name, short icon, short lvl, int minReward)
	{
		rich.setGold(newGold, rid, name, icon, lvl, minReward);
	}
	
	public void setDamage(int newDamage, int rid, String name, short icon, short lvl, short combatId, List<DBRoleGeneral> generals)
	{
		List<SBean.DBRichRoleGeneralBrief> briefs = new ArrayList<SBean.DBRichRoleGeneralBrief>();
		for (DBRoleGeneral g : generals) 
			briefs.add(new SBean.DBRichRoleGeneralBrief(g.id, g.lvl, g.advLvl, g.evoLvl));

		rich.setDamage(newDamage, rid, name, icon, lvl, combatId, briefs);
	}
	
	public void setLotery(int id, List<Byte> lotery)
	{
		rich.setLotery(id, lotery);
	}
	
	public List<GoldRank> getGoldRanks(int rid)
	{
		return rich.getGoldRanks(rid);
	}
	
	public List<DamageRank> getDamageRanks(int rid, short combatId)
	{
		return rich.getDamageRanks(rid, combatId);
	}
	
	public SBean.DBRichMapBuilding getBuildingByPos(short pos, List<DBRichMapBuilding> buildings, byte[] bindex)
	{
		int innerpos = 0;
		if (pos >= 1 && pos <= 8)
			innerpos = pos;
		else if (pos >= 10 && pos <= 17)
			innerpos = pos - 2;
		else if (pos >= 19 && pos <= 26)
			innerpos = pos - 4;
		else if (pos >= 28 && pos <= 34)
			innerpos = pos - 6;
		else if (pos == 35)
			innerpos = 1;
		
		if (innerpos > 0) {
			int size = 0;
			bindex[0] = 0;
			for (SBean.DBRichMapBuilding b : buildings) {
				size += getMapTypeSize(b.type);
				if (size >= innerpos) {
					return b;
				}
				bindex[0] ++;
			}	
		}
	
		if (buildings.size() > 0)
			return buildings.get(0);
		
		return null;
	}
	
	GameServer gs;
	Rich rich;

}

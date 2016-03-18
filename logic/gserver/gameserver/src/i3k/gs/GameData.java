
package i3k.gs;

import i3k.DBRoleGeneral;
import i3k.SBean;
import i3k.SBean.BestowCFGS;
import i3k.SBean.BestowItemCFGS;
import i3k.SBean.BlessCFGS;
import i3k.SBean.ChangeServerListCFGS;
import i3k.SBean.CommomConstantCFGS;
import i3k.SBean.DiskBetCFGS;
import i3k.SBean.DiskBetCostCFGS;
import i3k.SBean.ExpiratBossBasicCFGS;
import i3k.SBean.ExpiratBossItemsCFGS;
import i3k.SBean.GeneralSeyenExpCFGS;
import i3k.SBean.GeneralStoneBasicCFGS;
import i3k.SBean.GeneralStoneEvoCostCFGS;
import i3k.SBean.GeneralStonePosCFGS;
import i3k.SBean.GeneralStonePropCFGS;
import i3k.SBean.GeneralStonePropValueCFGS;
import i3k.SBean.HeadIconCFGS;
import i3k.SBean.HeroesBossBuffCFGS;
import i3k.SBean.HeroesBossCFGS;
import i3k.SBean.HeroesBossItemsCFGS;
import i3k.SBean.HeroesBossScoreCFGS;
import i3k.SBean.HeroesBossTypeCFGS;
import i3k.SBean.ItemCFGS;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import ket.util.FileSys;

public class GameData
{
	public static final int MAX_MONEY = 999999999;
	public static final int MAX_STONE = 999999999;
	public static final int MAX_POINT = 999999999;
	public static final int MAX_ITEM_COUNT = 60000;
	public static final short MAX_EQUIP_COUNT = 30000;
	public static final byte GENERAL_TEAM_SIZE = 5;
	public static final int MIN_COMBAT_TIME = 15;
	
	public static final byte FORCE_DOOR_COUNT = 5;
	public static final byte FORCE_MAX_ATTACK = 3;
	public static final byte MAX_FORCE_LEVEL = 5;
	public static final short MAX_ROLE_LEVEL_CONFIG = 100;
	public static final byte MAX_GENERAL_ADV_LEVEL = 14;
	public static final byte MAX_GENERAL_EVO_LEVEL = 5;
	public static final byte MAX_PET_EVO_LEVEL = 5;
	public static final byte MAX_GENERAL_INT_ORG = 100;
	public static final byte MAX_GENERAL_STR_ORG = 100;
	public static final byte MAX_EQUIP_EFEECT_COUNT = 2;
	public static final short ITEM_TYPE_EXP = 1;
	public static final short ITEM_TYPE_GENERAL_STONE = 2;
	public static final short ITEM_TYPE_SCROLL = 3;
	public static final short ITEM_TYPE_SCROLL_PIECE = 4;
	public static final short ITEM_TYPE_GENERAL_EXP = 5;
	public static final short ITEM_TYPE_MONEY = 6;
	public static final short ITEM_TYPE_EQUIP_PIECE = 7;
	public static final short ITEM_TYPE_GILD = 8;
	public static final short ITEM_TYPE_SKIP_COMBAT = 9;
	public static final short ITEM_TYPE_PET_EGG = 10;
	public static final short ITEM_TYPE_PET_PIECE = 11;
	public static final short ITEM_TYPE_PET_EXP = 12;
	public static final short ITEM_TYPE_GAMBLE = 13;
	public static final short ITEM_TYPE_CHAT = 14;
	public static final short ITEM_TYPE_STONE = 15;
	public static final short ITEM_TYPE_VIT = 16;
	public static final short ITEM_TYPE_RICH_DICE = 17;
	public static final short ITEM_TYPE_RICH_MINING_BOOM = 18;
	public static final short ITEM_TYPE_RICH_MUNIU = 19;
	public static final short ITEM_TYPE_RICH_WUZHONGSHENGYOU = 20;
	public static final short ITEM_TYPE_RICH_CASH = 21;
	public static final short ITEM_TYPE_RICH_MOBILITY = 22;
	public static final short ITEM_TYPE_RICH_FLY = 23;
	public static final short ITEM_TYPE_RICH_GO_WEST = 24;
	public static final short ITEM_TYPE_RICH_WANJIANQIFA = 25;
	public static final short ITEM_TYPE_RICH_TIANLEIGUNGUN = 26;
	public static final short ITEM_TYPE_RICH_KILLYOU = 27;
	public static final short ITEM_TYPE_RICH_TIANNVSANHUA = 28;
	public static final short ITEM_TYPE_RICH_SHIMOTIANSHI = 29;
	public static final short ITEM_TYPE_RICH_LINGBOWEIBO = 30;
	public static final short ITEM_TYPE_RICH_BOMACAOLIAO = 31;
	public static final short ITEM_TYPE_RICH_TIEGONGJI = 32;
	public static final short ITEM_TYPE_UPGRADE_LEVEL = 33;
	public static final short ITEM_TYPE_UPGRADE_GENERAL_LEVEL = 34;
	public static final short ITEM_TYPE_FORCE_ACTIVITY = 35;
	public static final short ITEM_TYPE_GENERAL_SEYEN = 36;
	public static final short ITEM_TYPE_FORCE_CUP = 37;
	public static final short ITEM_TYPE_VIP_EXP = 38;
	public static final short ITEM_TYPE_SWAR_PROTECT = 39;
	public static final short ITEM_TYPE_ARENA_POINT = 40;
	public static final short ITEM_TYPE_HEADICON = 41;
	public static final short ITEM_TYPE_TREASURE_MAP = 42;
	public static final short ITEM_TYPE_BESTOW = 43;
	public static final short ITEM_TYPE_BESTOW_ALL = 44;
	public static final short ITEM_TYPE_BESTOW_WEOPEN = 45;
	
	
	public static final byte EQUIP_RANK_COUNT = 5;
	public static final byte TASK_TYPE_LEVEL = 1;
	public static final byte TASK_TYPE_FINISH_COMBAT = 2;
	public static final byte TASK_TYPE_GENERAL_COUNT = 3;
	public static final byte TASK_TYPE_GENERAL_ANY_ADV = 4;
	public static final byte TASK_TYPE_GENERAL_ADV = 5;
	public static final byte FUNC_FLAG_CHECKIN = 3;
	public static final byte FUNC_FLAG_EGG = 4;
	public static final byte FUNC_FLAG_EQUIP = 5;
	public static final byte FUNC_FLAG_AUTO_FIGHTING = 14;
	public static final byte FUNC_FLAG_COUNT = 15;
	
	public static final int GENERAL_SKILL_COUNT =  4;
	public static final int GENERAL_EQUIP_COUNT =  6;
	
	public static final byte RANDOMROLES_FIND = -1;
	public static final byte RANDOMROLES_FRIEND = 0;
	public static final byte RANDOMROLES_PVP = 1;
	
	public static final byte ROLE_GENDER_MAX = 5;
	
	public static final byte CHAT_SHOW_VIP_LEVEL = 6;
	
	public static final byte COMMON_TYPE_NULL = -100;
	public static final byte COMMON_TYPE_ACTIVITY_POOL = -16;
	public static final byte COMMON_TYPE_SEYEN = -15;
	public static final byte COMMON_TYPE_FORCE_CUP = -14;
	public static final byte COMMON_TYPE_DUEL_POINT = -13;
	public static final byte COMMON_TYPE_SUPER_ARENA_POINT = -12;
	public static final byte COMMON_TYPE_SKILLPOINT = -11;
	public static final byte COMMON_TYPE_EXP = -10;
	public static final byte COMMON_TYPE_VIP_EXP = -9;
	public static final byte COMMON_TYPE_VIT = -8;
	public static final byte COMMON_TYPE_RICH_GOLD = -7;
	public static final byte COMMON_TYPE_RICH_POINT = -6;
	public static final byte COMMON_TYPE_FORCE_POINT = -5;
	public static final byte COMMON_TYPE_MARCH_POINT = -4;
	public static final byte COMMON_TYPE_ARENA_POINT = -3;
	public static final byte COMMON_TYPE_STONE = -2;
	public static final byte COMMON_TYPE_MONEY = -1;
	public static final byte COMMON_TYPE_ITEM = 0;
	public static final byte COMMON_TYPE_EQUIP = 1;
	public static final byte COMMON_TYPE_GENERAL = 2;
	public static final byte COMMON_TYPE_PET = 3;
	
	public static final byte COMBAT_EVENT_TYPE_CAOCHUANJIEJIAN = 1;
	public static final byte COMBAT_EVENT_TYPE_HUOSHAOCHIBI = 2;
	public static final byte COMBAT_EVENT_TYPE_BAGUAZHENWULI = 3;
	public static final byte COMBAT_EVENT_TYPE_BAGUAZHENFASHU = 4;
	public static final byte COMBAT_EVENT_TYPE_BAGUAZHENNVJIANG = 5;
	
	public static final byte COUNTRY_NONE = 0;
	public static final byte COUNTRY_WEI = 1;
	public static final byte COUNTRY_SHU = 2;
	public static final byte COUNTRY_WU = 3;
	public static final byte COUNTRY_COUNT = 4;
	public static final byte EGG_MONEY = 1;
	public static final byte EGG_STONE = 2;
	public static final int CHECKIN_CYCLE = 7;
	public static final byte MIN_ANNOUNCE_GENERAL_RANK = 1;	
	
	public static final byte CHECK_FUNC_CHECKIN = 10;
	
	public static final byte CURRENCY_UNIT_MONEY = 0;
	public static final byte CURRENCY_UNIT_STONE = 1;
	public static final byte CURRENCY_UNIT_ARENA = 2;
	public static final byte CURRENCY_UNIT_MARCH = 3;
	public static final byte CURRENCY_UNIT_FORCE = 4;
	public static final byte CURRENCY_UNIT_RICH_POINT = 5;
	public static final byte CURRENCY_UNIT_RICH_GOLD = 6;
	public static final byte CURRENCY_UNIT_SUPER_ARENA = 7;
	public static final byte CURRENCY_UNIT_DUEL = 8;
	
	public static final byte ANNOUNCE_FMT_BOSS_DIE = 10;
	
	public static final byte FORCE_CLOSE_KICK = 1;
	public static final byte FORCE_CLOSE_REPLACE = 2;
	public static final byte FORCE_CLOSE_VERSION_CODE = 3;
	public static final byte FORCE_CLOSE_VERSION_RES = 4;
	
	public static final byte CREATEROLE_NOTFOUND = 0;
	public static final byte CREATEROLE_EXIST = 1;
	public static final byte CREATEROLE_INVALID = 2;
	
	public static final byte EXPATRIATE_OK = 0;
	public static final byte EXPATRIATE_ROLENAME_CONFLICT = 1;
	public static final byte EXPATRIATE_REGLIMIT = 2;
	public static final byte EXPATRIATE_TRANSACTIONERROR = 4;
    
	public static final byte USERLOGIN_OK = 0;
	public static final byte USERLOGIN_FAILED = 1;
	public static final byte USERLOGIN_BUSY = 2;
	public static final byte USERLOGIN_FULL = 3;
	public static final byte USERLOGIN_RESTART = 4;
	public static final byte USERLOGIN_BAN = 5;
	public static final byte USERLOGIN_AUTH_FAILED = 6;
	public static final byte USERLOGIN_CLOSED = 7;
	public static final byte USERLOGIN_REG_FULL = 8;
	
	public static final byte CHANGENAME_OK = 0;
	public static final byte CHANGENAME_INVALID = 1;
	public static final byte CHANGENAME_EXIST = 2;
	public static final byte CHANGENAME_NOMONEY = 3;
	
	public static final byte SHOP_TYPE_NORMAL = 1;
	public static final byte SHOP_TYPE_ARENA = 2;
	public static final byte SHOP_TYPE_MARCH = 3;
	public static final byte SHOP_TYPE_RAND1 = 4;
	public static final byte SHOP_TYPE_RAND2 = 5;
	public static final byte SHOP_TYPE_FORCE = 6;
	public static final byte SHOP_TYPE_RICH_STONE = 7;
	public static final byte SHOP_TYPE_RICH_POINT = 8;
	public static final byte SHOP_TYPE_RICH_GOLD = 9;
	public static final byte SHOP_TYPE_SUPER_ARENA = 10;
	public static final byte SHOP_TYPE_DUEL = 11;
	public static final byte SHOP_TYPE_BLESS = 12;
	
	public static final byte VIP_RIGHT_FRIEND = 4;
	public static final byte VIP_RIGHT_SHOPITEM = 5;
	
	public static final byte VIP_SPEEDUP_COMBAT_X2 = 1;
	public static final byte VIP_SPEEDUP_COMBAT_X3 = 2;
	public static final byte VIP_SPEEDUP_FORCEPVE_X2 = 9;
	public static final byte VIP_SPEEDUP_FORCEPVE_X3 = 10;
	public static final byte VIP_SPEEDUP_FORCEPVE_SKIP = 11;
	
	public static final byte EVENTS_NOTICE_DTASK2 = 1;
	
	public static final byte DAILYTASK2_TYPE_COMBAT_ANY = 1;
	public static final byte DAILYTASK2_TYPE_COMBAT_HERO = 2;
	public static final byte DAILYTASK2_TYPE_MONTHLY_CARD = 3;
	public static final byte DAILYTASK2_TYPE_UPGRADE_GENERAL_SKILL = 4;
	public static final byte DAILYTASK2_TYPE_EGG = 5;
	public static final byte DAILYTASK2_TYPE_GILD = 6;
	public static final byte DAILYTASK2_TYPE_ARENA = 7;
	public static final byte DAILYTASK2_TYPE_MARCH = 8;
	public static final byte DAILYTASK2_TYPE_COMBAT_EVENT12 = 9;
	public static final byte DAILYTASK2_TYPE_COMBAT_EVENT345 = 10;
	public static final byte DAILYTASK2_TYPE_FREE_VIT1 = 11;
	public static final byte DAILYTASK2_TYPE_FREE_VIT2 = 12;
	public static final byte DAILYTASK2_TYPE_FREE_VIT3 = 13;
	public static final byte DAILYTASK2_TYPE_BUY_MONEY = 14;
	public static final byte DAILYTASK2_TYPE_MAIL_BAOZI = 15;
	public static final byte DAILYTASK2_TYPE_SHARE_PIC = 16;
	
	public static final byte DAILYTASK2_REWARD_EXP = 1;
	public static final byte DAILYTASK2_REWARD_MONEY = 2;
	public static final byte DAILYTASK2_REWARD_STONE = 3;
	public static final byte DAILYTASK2_REWARD_ITEM = 4;
	public static final byte DAILYTASK2_REWARD_VIT = 5;
	
	public static final byte RICH_DAILYTASK_TYPE_GOLD = 1;
	public static final byte RICH_DAILYTASK_TYPE_SHANZEI = 2;
	public static final byte RICH_DAILYTASK_TYPE_USE_CARD_ANY = 3;
	
	public static final byte RICH_TECH_TYPE_CITY = 1;
	public static final byte RICH_TECH_TYPE_MINE = 2;
	public static final byte RICH_TECH_TYPE_GOD = 3;
	public static final byte RICH_TECH_TYPE_BLACK_SHOP = 4;
	public static final byte RICH_TECH_TYPE_RICH_REWARD = 5;
	
	public static final byte MAX_DUNGEON_STAGES = 6;
	
	private static GameData instance;
	
	public static final int ROLE_RAND_INTERVAL_MAX = 60;
	
	public static GameData getInstance()
	{
		return instance;
	}
	
	public GameData(SBean.GameDataCFGS cfg) throws Exception
	{
		for(SBean.DropTableCFGS c : cfg.droptables)
			dropTables.put(c.id, c);
		for(SBean.EquipCFGS c : cfg.equips)
			equips.put(c.id, c);
		for(SBean.ItemCFGS c : cfg.items)
			items.put(c.id, c);
		for(SBean.CombatCFGS c : cfg.combats)
			combats.put(c.id, c);
		for(SBean.CombatEventCFGS c : cfg.combatEvents)
			combatEvents.put(c.id, c);
		for(SBean.GeneralCFGS g : cfg.generals)
			generals.put(g.id, g);
		for(SBean.PetCFGS g : cfg.pets)
			pets.put(g.id, g);
		for(SBean.PetCFGS g : cfg.deformPets)
			deformPets.put(g.id, g);
		for(SBean.TaskGroupCFGS t : cfg.taskGroups)
			taskGroups.put(t.id, t);
		for(SBean.EggCFGS e : cfg.eggs)
			eggs.put(e.id, e);
		soulboxs = cfg.soulboxs;
		capturecity = cfg.capturecity;
		forceBattles = cfg.forcebattles;
		rich = cfg.rich;
		invitation = cfg.invitation;
		for(SBean.PackageCFGS s : cfg.packages)
			packages.put(s.id, s);
		for(SBean.CmnRewardCFGS s : cfg.cmnrewards)
			cmnRewards.put(s.id, s);
		generalsCmn = cfg.generalsCmn;
		petsCmn = cfg.petsCmn;
		petFarm = cfg.petFarm;
		roleCmn = cfg.roleCmn;
		combatCmn = cfg.combatCmn;
		equipCmn = cfg.equipCmn;
		vip = cfg.vip;
		shopNormal = cfg.shopNormal;
		shopArena = cfg.shopArena;
		shopMarch = cfg.shopMarch;
		shopForce = cfg.shopForce;
		shopRichStone = cfg.shopRichStone;
		shopRichPoint = cfg.shopRichPoint;
		shopRichGold = cfg.shopRichGold;
		shopRand1 = cfg.shopRand1;
		shopRand2 = cfg.shopRand2;
		shopSuperArena = cfg.shopSuperArena;
		shopDuel = cfg.shopDuel;
		shopBless = cfg.shopBless;
		// TODO
		shopRand1.lvlVIPReq = 100;
		for(SBean.VIPCFGS e : vip)
		{
			if( e.canSummonShopRand1 == 1 )
			{
				shopRand1.lvlVIPReq = e.lvl;
				break;
			}
		}
		shopRand2.lvlVIPReq = 100;
		for(SBean.VIPCFGS e : vip)
		{
			if( e.canSummonShopRand2 == 1 )
			{
				shopRand2.lvlVIPReq = e.lvl;
				break;
			}
		}
		//
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopNormal.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_NORMAL, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopArena.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_ARENA, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopMarch.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_MARCH, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopForce.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_FORCE, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopRichStone.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_RICH_STONE, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopRichPoint.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_RICH_POINT, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopRichGold.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_RICH_GOLD, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopRand1.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_RAND1, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopRand2.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_RAND2, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopSuperArena.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_SUPER_ARENA, m);
		}
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopDuel.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_DUEL, m);
		}
		
		{
			Map<Short, SBean.ShopGoods> m = new TreeMap<Short, SBean.ShopGoods>();
			for(SBean.ShopGoodsLevelCFGS l : shopBless.levels)
			{
				for(SBean.ShopGoodsGroup g : l.groups)
				{
					for(SBean.ShopGoods e : g.goods)
					{
						m.put(e.id, e);
					}
				}
			}
			shopMap.put(SHOP_TYPE_BLESS, m);
		}
		
		badnames = cfg.badnames;
		badnames2 = cfg.badnames2;
		qqDirtyWords = cfg.qqDirtyWords;
		event = cfg.event;
		ipad = cfg.ipad;
		arena = cfg.arena;
		superArena = cfg.superArena;
		{
			arenaBestRankMoney = new int[arena.rankMax+2];
			arenaBestRankStone = new int[arena.rankMax+2];
			SBean.ArenaBestRankRewardCFGS lastLevel = null;
			for(SBean.ArenaBestRankRewardCFGS e : arena.bestRankRewards)
			{
				if( lastLevel == null )
				{
					if( e.rankFloor != 1 )
						throw new Error("impossible");
					arenaBestRankMoney[1] = e.money;
					arenaBestRankStone[1] = e.stone;
				}
				else
				{
					if( e.rankFloor <= 0 || e.rankFloor > arena.rankMax )
						e.rankFloor = (short)(arena.rankMax + 1);
					if( e.rankFloor <= lastLevel.rankFloor )
						throw new Error("impossible");
					int d = e.rankFloor - lastLevel.rankFloor;
					int moneyAllD = lastLevel.money - e.money;
					int stoneAllD = lastLevel.stone - e.stone;
					for(int i = lastLevel.rankFloor+1; i <= e.rankFloor; ++i)
					{
						arenaBestRankMoney[i] = (int)((e.rankFloor-i)*moneyAllD/(float)d) + e.money;
						arenaBestRankStone[i] = (int)((e.rankFloor-i)*stoneAllD/(float)d) + e.stone;
					}
				}
				lastLevel = e;
			}
		}
		march = cfg.march;
		power = cfg.power;
		force = cfg.force;
		checkin = cfg.checkin;
		dtask2 = cfg.dtask2;
		dtask2Map = new TreeMap<Byte, SBean.DailyTask2CFGS>();
		for(SBean.DailyTask2CFGS e : dtask2)
		{
			dtask2Map.put(e.id, e);
		}
		dailyActivity = cfg.dailyActivity;
		
		qqvipRewards = cfg.qqvipRewards;
		//levelLimit = cfg.levelLimit;
		topLevelAura = cfg.topLevelAura;
		
		friend = cfg.friend;
		userInput = cfg.userInput;
		combatCats = cfg.combatcats;
		
		vitMaxTable = new short[MAX_ROLE_LEVEL_CONFIG];
		for(int i = 0; i < MAX_ROLE_LEVEL_CONFIG; ++i)
			vitMaxTable[i] = roleCmn.vitMaxTable.get(i);
		
		combat = cfg.combat;
		Map<Short, SBean.PayCFGS> pmap = new TreeMap<Short, SBean.PayCFGS>();
		for(SBean.PayCFGS e : cfg.pay)
		{
			pmap.put(e.cid, e);
			if( e.cid == 1 )
				payDefault = e;
		}
		mcard = cfg.mcard;
		for(SBean.ChannelCFGS e : cfg.channels)
		{
			SBean.PayCFGS p = pmap.get(e.payID);
			if( p == null )
				p = payDefault;
			pay.put(e.name, p);
		}
		//
		{
			SBean.DropTableCFGS cfgsItem = dropTables.get(eggs.get(EGG_MONEY).itemDropTableID);
			List<SBean.DropTableEntry> lstItem = new ArrayList<SBean.DropTableEntry>();
			float pAllOld = 0.f;
			float pSum = 0.f;
			for(SBean.DropTableEntry e : cfgsItem.entryList)
			{
				float pAll = e.pro;
				float p = pAll - pAllOld;
				pAllOld = pAll;
				if( e.item.type == COMMON_TYPE_ITEM )
				{
					SBean.ItemCFGS icfg = getItemCFG(e.item.id);
					if( icfg.rank <= 2 )
					{
						SBean.DropTableEntry eNew = new SBean.DropTableEntry(e.item, p);
						lstItem.add(eNew);
						pSum += p;
					}
				}
			}
			if( ! lstItem.isEmpty() )
			{
				// TODO
				eggItemRank2 = new SBean.DropTableCFGS();
				eggItemRank2.id = 0;
				eggItemRank2.entryList = new ArrayList<SBean.DropTableEntry>();
				float pSum2 = 0;
				for(SBean.DropTableEntry e : lstItem)
				{
					float pNew = e.pro / pSum;
					pSum2 += pNew;
					e.pro = pSum2;
					eggItemRank2.entryList.add(e);
				}
			}
		}
		for(SBean.WeaponBasicCFGS b : cfg.weapon.basic)
			weaponBasic.put(b.gid, b);
		for(SBean.WeaponEnhanceCFGS e : cfg.weapon.enhance)
			weaponEnhance.put(e.gid, e);
		weaponReset = cfg.weapon.reset;
		weaponEvoCosts = cfg.weapon.evoCosts;
		dungeon = cfg.dungeon;
		relations = new HashMap<Short, SBean.RelationCFGS>();
		for (SBean.RelationCFGS r : cfg.relation.relations)
			relations.put(r.id, r);
		relationItems = new HashMap<Short, Short>();
		for (SBean.RelationItemCFGS i : cfg.relation.items)
			relationItems.put(i.iid, i.gid);
		relationConsumes = new HashMap<Short, List<Integer>>();
		for (SBean.RelationConsumeCFGS c : cfg.relation.consumes)
			relationConsumes.put(c.gid, c.consume);
	
		expirateCost = cfg.expatriateCost;
		bestowCost = cfg.bestowcost;
		bestowId = cfg.bestowId;
		forceBattleCount = cfg.forceBattleCount;
		generalStone = cfg.generalStone;
		generalSeyen = cfg.generalSeyen;
		changeServer = cfg.changeServer;
		expiratBoss = cfg.expiratBoss;
		heroesBoss = cfg.heroesBoss;
		for(GeneralSeyenExpCFGS b : cfg.generalSeyen.sexp)
			generalSeyenExp.put(b.level, b.exp);
		for(GeneralSeyenExpCFGS b : cfg.generalSeyen.sexp)
			generalSeyenLimit.put(b.level, b.limitLvl);
		for(GeneralStoneBasicCFGS b : cfg.generalStone.basic)
			generalStoneBasic.put(b.sid, b);
		for(GeneralStonePosCFGS e : cfg.generalStone.pos)
			generalStonePos.put(e.gid, e);
		
		headicon = cfg.headicon;
		diskBet = cfg.diskBet;
		bestow = cfg.bestow;
		common = cfg.common;
		bless = cfg.bless;
		bestowItem = cfg.bestowItem;
		merc = cfg.merc;
		duel = cfg.duel;
		sura = cfg.sura;
		swar = cfg.swar;
		official = cfg.official;
		treasureMap = cfg.treasureMap;
		beMonster = cfg.beMonster;

		//
		instance = this;
	}
	
	public boolean reload(SBean.GameDataCFGS cfg) throws Exception
	{
		//
		Map<Short, SBean.DropTableCFGS> dropTablesReload = new TreeMap<Short, SBean.DropTableCFGS>();
		for(SBean.DropTableCFGS c : cfg.droptables)
			dropTablesReload.put(c.id, c);
		dropTables = dropTablesReload;
		//
		soulboxs = cfg.soulboxs;
		ipad = cfg.ipad;
		//
		return true;
	}
	
	public SBean.IPadCFGS getIPadCFG()
	{
		return ipad;
	}
	
	public SBean.MonthlyCardCFGS getMonthlyCardCFG()
	{
		return mcard;
	}
	
	public SBean.CombatServerCFG getCombatCFG()
	{
		return combat;
	}
	
	public SBean.FriendCFGS getFriendCFG()
	{
		return friend;
	}
	
	public SBean.ForceCFGS getForceCFG()
	{
		return force;
	}
	
	public int getRoleRandInterval()
	{
		return random.nextInt(ROLE_RAND_INTERVAL_MAX);
	}
	
	public SBean.ShopGoods shopRandSelect(SBean.ShopGoodsGroup group)
	{
		if( group == null || group.goods.isEmpty() )
			return null;
		float r = random.nextFloat();
		for(SBean.ShopGoods e : group.goods)
		{
			if( r < e.pro )
				return e;
		}
		return group.goods.get(group.goods.size()-1);
	}
	
	public List<String> getBadNames()
	{
		return badnames;
	}
	
	public List<String> getBadNames2()
	{
		return badnames2;
	}
	
	public List<SBean.DailyTask2CFGS> listDailyTask2CFG()
	{
		return dtask2;
	}
	
	public SBean.DailyTask2CFGS getDailyTask2CFG(byte id)
	{
		return dtask2Map.get(id);
	}
	
	public List<SBean.DailyActivityRewardCFGS> getDailyActivityRewardsCFG()
	{
		return dailyActivity;
	}
	
	public SBean.DailyActivityRewardCFGS getDailyActivityReward(int id)
	{
		if (id < 0 || id >= dailyActivity.size())
			return null;
		return dailyActivity.get(id);
	}
	
	public List<SBean.QQVIPRewardsCFGS> getQQVIPRewardsCFG()
	{
		return qqvipRewards;
	}
	
//	public SBean.LevelLimitCFGS getLevelLimitCFG()
//	{
//		return levelLimit;
//	}
	
	public SBean.TopLevelAuraCFGS getTopLevelAuraCFG()
	{
		return topLevelAura;
	}
	
	public static int getTime()
	{
		return FileSys.getLocalTime() + timeAddGM;
	}
	
	public static int getTimeZone()
	{
		return 8;
	}
	
	public void setTime(int setType, int t)
	{
		switch( setType )
		{
		case 0:	// clear
			timeAddGM = 0;
			break;
		case 1: // rel
			timeAddGM += t;
			break;
		case 2: // abs
			timeAddGM = t - FileSys.getLocalTime();
			break;
		default:
			break;
		}
	}
	
	public short getMaxGeneralLevel(short lvlRole)
	{
		return lvlRole;
	}
	
	public boolean isBadName(String name)
	{
		for(String e : badnames)
		{
			if( name.indexOf(e) >= 0 )
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isBadName2(String name)
	{
		for(String e : badnames2)
		{
			if( name.indexOf(e) >= 0 )
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isQQDirtyWords(String name)
	{
		for(String e : qqDirtyWords.wordsICase)
		{
			if( name.toLowerCase().indexOf(e.toLowerCase()) >= 0 )
			{
				return true;
			}
		}
		for(String e : qqDirtyWords.words)
		{
			if( name.indexOf(e) >= 0 )
			{
				return true;
			}
		}
		for(SBean.QQDirtyWordCatCFGS c : qqDirtyWords.cats)
		{
			if( name.indexOf(c.key) >= 0 )
			{
				for(String e : c.words)
				{
					if( name.indexOf(e) >= 0 )
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public SBean.VIPCFGS getVIPCFG(byte lvl)
	{
		if( lvl < 1 || lvl > vip.size() )
			return null;
		return vip.get(lvl-1);
	}
	
	public SBean.TaskGroupCFGS getTaskGroupCFG(short gid)
	{
		return taskGroups.get(gid);
	}
	
	// ע�ⲻҪ�޸ķ��ض���
	public List<SBean.DropEntry> getDropTableList(short tableid)
	{
		SBean.DropTableCFGS cfg = dropTables.get(tableid);
		if( cfg == null || cfg.srcList.isEmpty() )
			return null;
		return cfg.srcList;
	}
	
	// ע�ⲻҪ�޸ķ��ض���
	public SBean.DropEntry getDropTableRandomEntry(short tableid)
	{
		SBean.DropTableCFGS cfg = dropTables.get(tableid);
		if( cfg == null || cfg.entryList.isEmpty() )
			return null;
		float r = random.nextFloat();
		for(SBean.DropTableEntry e : cfg.entryList)
		{
			if( r < e.pro )
				return e.item;
		}
		return cfg.entryList.get(cfg.entryList.size()-1).item;
	}
	
	// TODO !!!
	public SBean.DropEntry getDropTableRandomEntryItemRank2()
	{
		SBean.DropTableCFGS cfg = eggItemRank2;
		if( cfg == null || cfg.entryList.isEmpty() )
			return null;
		float r = random.nextFloat();
		for(SBean.DropTableEntry e : cfg.entryList)
		{
			if( r < e.pro )
				return e.item;
		}
		return cfg.entryList.get(cfg.entryList.size()-1).item;
	}
	
	public short getEggRandomID(List<SBean.DropTableEntry> lst)
	{
		if( lst.isEmpty() )
			return -1;
		float r = random.nextFloat();
		for(SBean.DropTableEntry e : lst)
		{
			if( r < e.pro )
				return e.item.id;
		}
		return lst.get(lst.size()-1).item.id;
	}
	
	public int getUpgradeExp(short lvlNow)
	{
		return roleCmn.upgradeExp.get(lvlNow-1);
	}	
	
	public short getVitMax(short lvlNow)
	{
		short t = vitMaxTable[lvlNow-1];
		return t;
	}
	
	public short getSkillPointMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).skillPointMax;
	}
	
	public short getBuySkillPointMaxTimes(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).buySkillPointTimes;
	}
	
	public byte getMobaiTimesMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).mobaiTimes;
	}
	
	public short getBuyVitMaxTimes(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).buyVitTimes;
	}
	
	public boolean canSaodang(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).canSaodang1 != 0;
	}
	
	public boolean canSaodang10(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).canSaodang10 != 0;
	}
	
	public boolean canBuySoulBox(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).canBuySoulBox != 0;
	}
	
	public short getBuyMoneyMaxTimes(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).buyMoneyTimes;
	}
	
	public short getArenaBuyTimesMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).buyArenaTimes;
	}
	
	public short getMarchResetTimesMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).resetMarchTimes;
	}
	
	public byte getSuperMobaiTimesMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).superMobaiTimes;
	}
	
	public short getZanOfferTimesMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).petZanOfferCount;
	}
	
	public byte getMercCountMax(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).mercCountMax;
	}
	
	public byte getMarchRewardUp(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).marchRewardUp;
	}

	public short getOccupyCityMaxCount(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).canOccupyCityCount;
	}
	
	public int getGeneralUpgradeExp(short lvlNow)
	{
		return generalsCmn.upgradeExp.get(lvlNow-1);
	}
	
	public int getPetUpgradeExp(short lvlNow)
	{
		return petsCmn.upgradeExp.get(lvlNow-1);
	}
	
	public SBean.ArenaCFGS getArenaCFG()
	{
		return arena;
	}
	
	public SBean.SuperArenaCFGS getSuperArenaCFG()
	{
		return superArena;
	}
	
	public SBean.MarchCFGS getMarchCFG()
	{
		return march;
	}
	
	public SBean.DungeonCFGS getDungeonCFG()
	{
		return dungeon;
	}
	
	public SBean.MercCFGS getMercCFG()
	{
		return merc;
	}
	
	public SBean.DuelCFGS getDuelCFG()
	{
		return duel;
	}
	
	public SBean.SuraCFGS getSuraCFG()
	{
		return sura;
	}
	
	public SBean.ForceSWarCFGS getSWarCFG()
	{
		return swar;
	}
	
	public SBean.OfficialCFGS getOfficialCFG()
	{
		return official;
	}
	
	public SBean.TreasureMapBaseCFGS getTreasureMapCFG()
	{
		return treasureMap;
	}
	
	public SBean.BeMonsterCFGS getBeMonsterCFG()
	{
	    return beMonster;
	}
	
	public Collection<SBean.RelationCFGS> getAllRelationCFG()
	{
		return relations.values();
	}
	
	public SBean.RelationCFGS getRelationCFG(short id)
	{
		return relations.get(id);
	}
	
	public Short getRelationItemCFG(short iid)
	{
		return relationItems.get(iid);
	}
	
	public Integer getRelationLevelMaxCFG(short gid)
	{
		List<Integer> con = relationConsumes.get(gid);
		if (con == null)
			return null;
		return con.size();
	}
	
	public Integer getRelationConsumeCFG(short gid, short lvl)
	{
		lvl --;
		List<Integer> con = relationConsumes.get(gid);
		if (con == null)
			return null;
		if (lvl < 0 || lvl >= con.size()-1)
			return null;
		return con.get(lvl);
	}
	
	public List<SBean.PowerCFGS> getPowerCFG()
	{
		return power;
	}
	
	public SBean.GeneralCmnCFGS getGeneralCmnCFG()
	{
		return generalsCmn;
	}
	
	public SBean.PetCmnCFGS getPetCmnCFG()
	{
		return petsCmn;
	}
	
	public SBean.PetFarmCFGS getPetFarmCFG()
	{
		return petFarm;
	}
	
	public SBean.RoleCmnCFGS getRoleCmnCFG()
	{
		return roleCmn;
	}
	
	public SBean.CombatCmnCFGS getCombatCmnCFG()
	{
		return combatCmn;
	}
	
	public SBean.EquipCmnCFGS getEquipCmnCFG()
	{
		return equipCmn;
	}
	
	public SBean.CombatCFGS getCombatCFG(short combatid)
	{
		return combats.get(combatid);
	}
	
	public SBean.CombatEventCFGS getCombatEventCFG(byte ceid)
	{
		return combatEvents.get(ceid);
	}
	
	public String getChannelName(String username)
	{
		try
		{
			String[] l = username.split("_");
			if( l.length < 2 )
				return "";
			if( pay.containsKey(l[0]) )
				return l[0];
		}
		catch(Exception ex)
		{			
		}
		return "";
	}
	
	public String getOpenIDByUserName(String username)
	{
		return getChannelUserName(username).toUpperCase();
	}
	
	public String getChannelUserName(String username)
	{
		String cname = getChannelName(username);
		if( cname.equals("") )
			return username;
		return username.substring(cname.length()+1);
	}
	
	public boolean isGuestOpenID(String openID)
	{
		return openID != null && openID.startsWith("G_");
	}
	
	public boolean isQQGameServerID(int gsID)
	{
		// TODO
		return gsID > 20000;
	}
	
	public SBean.PayCFGS getPayCFG(String username)
	{
		SBean.PayCFGS r = null;
		try
		{
			String[] l = username.split("_");
			if( l.length > 1 )
				r = pay.get(l[0]);
		}
		catch(Exception ex)
		{			
		}
		if( r == null )
			r = payDefault;
		return r;
	}
	
	public SBean.PackageCFGS getPackageCFG(short pid)
	{
		return packages.get(pid);
	}
	
	public SBean.CmnRewardCFGS getCmnReward(short rid)
	{
		return cmnRewards.get(rid);
	}
	
	public byte getCombatMaxResetTimes(byte lvlVIP)
	{
		if( lvlVIP < 0 )
			lvlVIP = 0;
		else if( lvlVIP > vip.size() )
			lvlVIP = (byte)(vip.size() - 1);
		
		return vip.get(lvlVIP).resetCombatTimes;
	}
	
	public int getCombatResetPrice(byte timesNow)
	{
		int index = timesNow;
		if( index < 0 || index >= combatCmn.priceReset.size() )
		{
			index = combatCmn.priceReset.size() - 1;
		}
		return combatCmn.priceReset.get(index);		
	}
	
	public SBean.CheckinCFGS getCheckinNow(int now, byte[] ret)
	{
		for(SBean.CheckinCFGS e : checkin)
		{
			int timeStart = e.timeStart + roleCmn.timeDayRefresh * 60;
			int timeEnd = timeStart + 86400 * e.rewards.size();
			if( now >= timeStart && now < timeEnd )
			{
				ret[0] = e.id;
				ret[1] = (byte)(1+(now - timeStart)/86400);
				return e;
			}
		}
		return null;
	}
	
	public boolean isDoublePayLevel(byte payLvl)
	{
		return payLvl == 1 || payLvl == 2 || payLvl == 4 || payLvl == 6;
	}
	
	public byte checkCombatID(short cid, SBean.DBCombatPos pos)
	{
		pos.catIndex = 0;
		for(SBean.CombatCatCFG cccfg : combatCats)
		{
			byte type = 0;
			for(SBean.CombatCatEntryListCFG ccfg : cccfg.combats)
			{
				pos.combatIndex = 0;
				for(SBean.CombatCatEntryCFG cfg : ccfg.combats)
				{
					if( cfg.cid == cid )
					{
						return type;
					}
					++pos.combatIndex;
				}
				++type;
			}
			++pos.catIndex;
		}
		return -1;
	}
	
	public short getCombatID(byte type, SBean.DBCombatPos pos)
	{
		if( pos.catIndex < 0 || pos.catIndex >= combatCats.size() )
			return -1;
		SBean.CombatCatCFG ccfg = combatCats.get(pos.catIndex);
		if( type < 0 || type >= ccfg.combats.size() )
			return -1;
		SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(type);
		if( pos.combatIndex < 0 || pos.combatIndex >= lcfg.combats.size() )
			return -1;
		SBean.CombatCatEntryCFG ecfg = lcfg.combats.get(pos.combatIndex);
		return ecfg.cid;
	}
	
	public short getCombatID(byte ctype, byte cindex, byte bindex)
	{
		if( cindex < 0 || cindex >= combatCats.size() )
			return -1;
		SBean.CombatCatCFG ccfg = combatCats.get(cindex);
		if( ctype < 0 || ctype >= ccfg.combats.size() )
			return -1;
		SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(ctype);
		if( bindex < 0 || bindex >= lcfg.combats.size() )
			return -1;
		SBean.CombatCatEntryCFG ecfg = lcfg.combats.get(bindex);
		return ecfg.cid;
	}
	
	public int getCombatCatSize(byte type, byte catIndex)
	{
		if( catIndex < 0 || catIndex >= combatCats.size() )
			return 0;
		SBean.CombatCatCFG ccfg = combatCats.get(catIndex);
		if( type < 0 || type >= ccfg.combats.size() )
			return 0;
		SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(type);
		return lcfg.combats.size();
	}
	
	public boolean advCombatPos(byte type, SBean.DBCombatPos pos)
	{
		if( pos.catIndex < 0 || pos.catIndex >= combatCats.size() )
			return false;
		SBean.CombatCatCFG ccfg = combatCats.get(pos.catIndex);
		if( type < 0 || type >= ccfg.combats.size() )
			return false;
		SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(type);
		if( pos.combatIndex < 0 || pos.combatIndex >= lcfg.combats.size() )
			return false;
		if( pos.combatIndex + 1 < lcfg.combats.size() )
			pos.combatIndex++;
		else if( pos.catIndex + 1 < combatCats.size() )
		{
			pos.catIndex++;
			pos.combatIndex = 0;
		}
		return true;
	}
	
	public boolean verifyCombatPos(byte ctype, SBean.DBCombatPos pos)
	{
		if( ctype == 0 )
			return true;
		if( pos.catIndex < 0 || pos.catIndex >= combatCats.size() )
			return true;
		short cid = getCombatID(ctype, pos.catIndex, pos.combatIndex);
		if( cid <= 0 )
			return true;
		SBean.CombatCFGS cfg = getCombatCFG(cid);
		if( cfg == null )
			return true;
		if( cfg.type == 1 )
			return true;
		return false;
	}
	
	public short getRandomGrowthRate(SBean.PetCFGS cfg)
	{
		short min = cfg.minGrowthRate;
		short max = cfg.maxGrowthRate;
		if( cfg.propGrowthRate == null || cfg.propGrowthRate.size() <= 1 )
			return (short)getRandInt(min, max);
		int n = cfg.propGrowthRate.size();
		int index = 0;
		float p = random.nextFloat();
		for(int i = 0; i < n; ++i)
		{
			if( p < cfg.propGrowthRate.get(i) )
			{
				index = i;
				break;
			}
		}
		float d = (max-min)/(float)n;
		short from = (short)(min + index * d);
		if( index == 0 || from < min )
			from = min;
		short to = (short)(min + index * d + d);
		if( index == n-1 || to > max )
			to = max;
		return (short)getRandInt(from, to);
	}
	
	public void updateMaxCombatPos(byte type, SBean.DBCombatPos pos)
	{
		pos.catIndex = (byte)(combatCats.size()-1);
		SBean.CombatCatCFG ccfg = combatCats.get(pos.catIndex);
		SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(type);
		pos.combatIndex = (byte)(lcfg.combats.size()-1);
	}
	
	public void updateGodCombatPos(byte type, SBean.DBCombatPos pos, byte cindex, byte bindex)
	{		
		//if( cindex >= combatCats.size() )
		//	cindex = (byte)(combatCats.size()-1);
		pos.catIndex = cindex;
		//SBean.CombatCatCFG ccfg = combatCats.get(pos.catIndex);
		//SBean.CombatCatEntryListCFG lcfg = ccfg.combats.get(type);
		//if( bindex >= lcfg.combats.size() )
		//	bindex = (byte)(lcfg.combats.size()-1);
		pos.combatIndex = bindex;
	}
	
	public SBean.EquipCFGS getEquipCFG(DBRoleGeneral g, int pos)
	{
		SBean.EquipCFGS ecfg = null;
		SBean.GeneralCFGS gcfg = getGeneralCFG(g.id);
		if (gcfg == null)
			return ecfg;
		int index = (g.advLvl-1) * GameData.GENERAL_EQUIP_COUNT + (pos-1);
		if( index < 0 || index >= gcfg.equips.size() )
			return ecfg;
		short eid = gcfg.equips.get(index);
		ecfg = getEquipCFG(eid);
		return ecfg;
	}
	
	public SBean.EquipCFGS getEquipCFG(short equipid)
	{
		return equips.get(equipid);
	}
	
	public SBean.ShopCFGS getShopCFG(byte type)
	{
		switch( type )
		{
		case SHOP_TYPE_NORMAL:
			return shopNormal;
		case SHOP_TYPE_ARENA:
			return shopArena;
		case SHOP_TYPE_MARCH:
			return shopMarch;
		case SHOP_TYPE_RAND1:
			return shopRand1;
		case SHOP_TYPE_RAND2:
			return shopRand2;
		case SHOP_TYPE_FORCE:
			return shopForce;
		case SHOP_TYPE_RICH_STONE:
			return shopRichStone;
		case SHOP_TYPE_RICH_POINT:
			return shopRichPoint;
		case SHOP_TYPE_RICH_GOLD:
			return shopRichGold;
		case SHOP_TYPE_SUPER_ARENA:
			return shopSuperArena;
		case SHOP_TYPE_DUEL:
			return shopDuel;
		case SHOP_TYPE_BLESS:
			return shopBless;	
			
		default:
			break;
		}
		return null;
	}
	
	public Map<Short, SBean.ShopGoods> getShopCFGMap(byte type)
	{
		return shopMap.get(type);
	}
	
	private void mergeDrop(List<SBean.DropEntryNew> lst, SBean.DropEntryNew d)
	{
		if( d.type == GameData.COMMON_TYPE_GENERAL || d.type == GameData.COMMON_TYPE_PET )
		{
			lst.add(d);
			return;
		}
		for(SBean.DropEntryNew e : lst)
		{
			if( e.type == d.type && e.id == d.id )
			{
				e.arg += d.arg;
				return;
			}
		}
		lst.add(d);
	}
	
	public static class ItemDropLogTrans
	{
		public ItemDropLogTrans(Map<Short, Short> oldLog, boolean bAutoCommit)
		{
			this.bAutoCommit = bAutoCommit;
			this.oldLog = oldLog;
			
			if( ! bAutoCommit )
				newLog = new HashMap<Short, Short>();
		}
		
		public void commit()
		{
			if( bAutoCommit )
				return;
			
			for(Map.Entry<Short, Short> e : newLog.entrySet())
			{
				oldLog.put(e.getKey(), e.getValue());
			}
		}
		
		public Map<Short, Short> getOldLog()
		{
			return oldLog;
		}
		
		public Map<Short, Short> getNewLog()
		{
			if( bAutoCommit )
				return oldLog;
			return newLog;
		}
		
		private final boolean bAutoCommit;
		private Map<Short, Short> oldLog;
		private Map<Short, Short> newLog;
	}
	
	static class BonusInfo
	{
		SBean.CombatBonus cb = new SBean.CombatBonus();
		int nornamlDropCount = 0;
	}
	public BonusInfo getCombatBonus(SBean.CombatCFGS cfg, ItemDropLogTrans itemDropLogTrans, float levellimitExpAdd,
			                                byte nTimesExp, byte nTimesStone, byte nTimesMoney, byte ntimeItemOrEquip, 
			                                SBean.DropEntry extraDrop, List<Short> forbattleExtraDropTblIDs, List<Short> forceMemExtraDropTblIDs, byte qqVIP)
	{
		BonusInfo info = new BonusInfo();
		//SBean.CombatBonus cb = new SBean.CombatBonus();
		info.cb.entryIDs = new ArrayList<SBean.DropEntryNew>();
		info.cb.exp = (int)(cfg.dropExp * nTimesExp * levellimitExpAdd);
		info.cb.generalExp = cfg.dropGeneralExp * nTimesExp;
		info.cb.money = cfg.dropMoney * nTimesMoney;
		if( qqVIP > 1 )
		{
			info.cb.money = (int)(info.cb.money * 1.3f);
		}
		else if( qqVIP == 1 )
		{
			info.cb.money = (int)(info.cb.money * 1.2f);
		}
		info.cb.stone = cfg.dropStone;
		info.cb.stoneTimes = (byte)(cfg.dropStoneTimes * nTimesStone);
		
		if( cfg.dropTableID > 0 )
		{
			for(int i = 0; i < cfg.dropTimes; ++i)
			{
				SBean.DropEntry d = getDropTableRandomEntry(cfg.dropTableID);
				if( d != null && d.type != GameData.COMMON_TYPE_NULL )
				{
					int count = d.type == GameData.COMMON_TYPE_GENERAL ? d.arg : (d.arg * ntimeItemOrEquip);
					mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(d.type, d.id, count));
				}
			}
		}
		else
		{
			for(SBean.DropEntry d : cfg.dropFixed)
			{
				int count = d.type == GameData.COMMON_TYPE_GENERAL ? d.arg : (d.arg * ntimeItemOrEquip);
				mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(d.type, d.id, count));
			}
		}
		if( cfg.dropStoneID > 0 && itemDropLogTrans != null )
		{
			short iid = cfg.dropStoneID; 
			SBean.ItemCFGS icfg = getItemCFG(iid);
			if( icfg != null )
			{
				{
					short timesNow = 0;
					if( itemDropLogTrans.getOldLog().get(iid) != null )
						timesNow = itemDropLogTrans.getOldLog().get(iid).shortValue();
					if( timesNow < 0 )
					{
						itemDropLogTrans.getNewLog().put(iid, (short)(timesNow+1));
					}
					else
					{
						float p = (icfg.extDropProp + timesNow * icfg.extDropPropInc)/100.f;
						boolean bDrop = getRandom().nextFloat() < p;
						if( bDrop )
						{
							mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(COMMON_TYPE_ITEM, cfg.dropStoneID, ntimeItemOrEquip));
							itemDropLogTrans.getNewLog().put(iid, (short)-icfg.extDropCool);
						}
						else
						{
							itemDropLogTrans.getNewLog().put(iid, (short)(timesNow+1));
						}
					}
				}
			}
		}
		info.nornamlDropCount = info.cb.entryIDs.size();
		if (extraDrop != null)
		{
			mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(extraDrop.type, extraDrop.id, extraDrop.arg));
		}
		if (forbattleExtraDropTblIDs != null)
		{
			for (short e : forbattleExtraDropTblIDs)
			{
				if (e > 0)
				{
					SBean.DropEntry d = getDropTableRandomEntry(e);
					if( d != null && d.type != GameData.COMMON_TYPE_NULL )
					{
						mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(d.type, d.id, d.arg));
					}
				}
			}
		}
		if (forceMemExtraDropTblIDs != null)
		{
			for (short e : forceMemExtraDropTblIDs)
			{
				if (e > 0)
				{
					SBean.DropEntry d = getDropTableRandomEntry(e);
					if( d != null && d.type != GameData.COMMON_TYPE_NULL )
					{
						mergeDrop(info.cb.entryIDs, new SBean.DropEntryNew(d.type, d.id, d.arg));
					}
				}
			}
		}
		return info;
	}
	
	public boolean testPVP(int s1, int s2)
	{
		return true; // TODO
	}
	
	public SBean.UserInputCFGS getUserInputCFG()
	{
		return userInput;
	}
		
	public SBean.GeneralCFGS getGeneralCFG(short gid)
	{
		return generals.get(gid);
	}
	
	public SBean.PetCFGS getPetCFG(short pid, boolean deformed)
	{
		if (deformed)
			return deformPets.get(pid);
		else
			return pets.get(pid);
	}
	
	public SBean.ItemCFGS getItemCFG(short id)
	{
		return items.get(id);
	}
	
	public List<ItemCFGS> getBestowItemCFG()
	{
		List<SBean.ItemCFGS> itemList = new ArrayList<SBean.ItemCFGS>();
		for(ItemCFGS item :items.values()){
			if(item.type==ITEM_TYPE_BESTOW){
				itemList.add(item);
			}		
		}
		return itemList;
	}
	
	public BestowItemCFGS getBestowItemsCFG()
	{
		return bestowItem;
	}
	
	public int getItemPrice(short id)
	{
		return items.get(id).price;
	}
	
	public int getEquipPrice(short id)
	{
		return equips.get(id).price;
	}
	
	public SBean.EggCFGS getEggCFG(byte eid)
	{
		return eggs.get(eid);
	}

	public int getNextVipNeedPay(byte vipNow, int stonePayTotal)
	{
		int needPayStone = 0; 
		if (vipNow + 1 < vip.size())
		{
			SBean.VIPCFGS cfg = vip.get(vipNow + 1);
			needPayStone = cfg.stoneReq - stonePayTotal;
		}
		if (needPayStone < 0)
			needPayStone = 0;
		
		return needPayStone;
	}
	
	public byte testVIPUPgrade(byte vipNow, int stonePayTotal)
	{
		byte r = -1;
		for(byte i = (byte)(vipNow+1); i < vip.size(); ++i)
		{
			SBean.VIPCFGS cfg = vip.get(i);
			if( stonePayTotal >= cfg.stoneReq )
				r = i;
		}
		return r;
	}
	
	public byte getVIPLevel(int stonePayTotal)
	{
		byte lvl = 0;
		for(byte i = 0; i < vip.size(); ++i)
		{
			SBean.VIPCFGS cfg = vip.get(i);
			if( stonePayTotal >= cfg.stoneReq )
				lvl = i;
		}
		return lvl;
	}
	
	public int getVIPStoneMax()
	{
		return vip.get(vip.size()-1).stoneReq;
	}
	
	// GMCommand
	public void setForceWarMemCntReq(int memcnt)
	{
		// TODO
		for(SBean.ForceBattleCFGS e : forceBattles.battles)
		{
			e.forceMemCntReq = memcnt;
		}
	}
	
	public int getPayPrice(String username, byte payLvl)
	{
		SBean.PayCFGS cfg = getPayCFG(username);
		if( payLvl < 1 || payLvl > cfg.levels.size() )
			return -1;
		return cfg.levels.get(payLvl-1).rmb;
	}
	
	public int getStoneByRMB(String username, final float rmb, byte lvlDouble)
	{
		SBean.PayCFGS cfg = getPayCFG(username);
		int irmb = (int)rmb;
		int stoneRes = 0;
		float f = rmb - irmb;
		while( true )
		{
			SBean.PayCFGLevelS lcfg = null;
			for(int i = cfg.levels.size()-1; i>=0; --i)
			{
				SBean.PayCFGLevelS e = cfg.levels.get(i);
				if( irmb >= e.rmb )
				{
					lcfg = e;
					break;
				}
			}
			if( lcfg == null )
				break;
			int n = irmb / lcfg.rmb;
			stoneRes += n * lcfg.stone;
			irmb -= n * lcfg.rmb;
		}
		stoneRes += (irmb + f) * 10;
		if( lvlDouble > 0 && lvlDouble <= cfg.levels.size() )
		{
			SBean.PayCFGLevelS lcfg = cfg.levels.get(lvlDouble-1);
			if( rmb >= lcfg.rmb )
			{
				stoneRes += lcfg.stone;
			}
		}
		return stoneRes;
	}
	
	public SBean.EventCFGS getEventCFG()
	{
		return event;
	}
	
	public boolean testGeneral(short id)
	{
		return generals.containsKey(id);
	}
	
	public boolean testEquip(short id)
	{
		return equips.containsKey(id);
	}
	
	public boolean testItem(short id)
	{
		return items.containsKey(id);
	}
	
	public Random getRandom()
	{
		return random;
	}
	
	public int getRandInt(int min, int max)
	{
		if( min == max )
			return min;
		if( min > max )
		{
			int t = max;
			max = min;
			min = t;
		}
		return random.nextInt(max-min) + min;
	}
	
	public byte[] secureRandBytes(int len)
	{
		byte[] r = new byte[len];
		srandom.nextBytes(r);
		return r;
	}
	
	public List<SBean.DropEntryNew> getArenaBestRankRewards(int rankNow, int bestRank)
	{
		if( rankNow <= 0 )
			return null;
		if( bestRank <= 0 )
			bestRank = (short)(arena.rankMax + 1);
		if( rankNow >= bestRank )
			return null;
		List<SBean.DropEntryNew> lst = new ArrayList<SBean.DropEntryNew>();
		int money = arenaBestRankMoney[rankNow] - arenaBestRankMoney[bestRank]; 
		int stone = arenaBestRankStone[rankNow] - arenaBestRankStone[bestRank];
		if( money > 0 )
			lst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, money));
		if( stone > 0 )
			lst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, stone));
		// TODO
		return lst;
	}
	
	public List<SBean.SoulBoxCFGS> getSoulBoxCfgs()
	{
		return soulboxs;
	}
	
	public SBean.ForceBattlesCFGS getForceBattleCfgs()
	{
		return forceBattles;
	}
	
	public SBean.ForceBattleCFGS getForceBattleNow(int now)
	{
		for(SBean.ForceBattleCFGS e : forceBattles.battles)
		{
			if( now >= e.startTime && now <= e.endTime )
			{
				return e;
			}
		}
		return null;
	}
	
	public SBean.ForceBattleCFGS getForceBattleNext(int now)
	{
		SBean.ForceBattleCFGS fb = null;
		for(SBean.ForceBattleCFGS e : forceBattles.battles)
		{
			if( now < e.startTime )
			{
				if (fb == null)
				{
					fb = e;
				}
				else
				{
					if (e.startTime < fb.startTime)
						fb = e;
				}
			}
		}
		return fb;
	}
	
	public SBean.ItemPlusEffectCFGS getForceBattlePluseEffectItemCFG(int id)
	{
		for(SBean.ItemPlusEffectCFGS e : forceBattles.itemPlusEffects)
		{
			if( e.id == id )
			{
				return e;
			}
		}
		return null;
	}
	
	public SBean.ForceBattleCFGS getForceBattleByID(int bid)
	{
		for(SBean.ForceBattleCFGS e : forceBattles.battles)
		{
			if (e.id == bid)
				return e;
		}
		return null;
	}
	
	public SBean.InvitationCFGS getInvitationCFG()
	{
		return invitation;
	}
	
	public SBean.InvitationTypeTasksCFG getInvitationTypeTasksCFG(int index)
	{
		if (index < 0 || index >= invitation.tasks.size())
			return null;
		return invitation.tasks.get(index);
	}
	
	public SBean.RichCFGS getRichCFG()
	{
		return rich;
	}
	
	public SBean.PetEatCFGS getPetEatCFG(short pid, short lvlNow, boolean deformed)
	{
		SBean.PetCFGS p = null;
		if (deformed)
			p = deformPets.get(pid);
		else
			p = pets.get(pid);
		if (p == null)
			return null;
		
		SBean.PetEatCFGS eat = null;
		for (SBean.PetEatCFGS e : p.eat) {
			if (lvlNow < e.lvlMax) {
				eat = e;
				break;
			}
		}
		if (eat == null && p.eat.size() > 0)
			eat = p.eat.get(p.eat.size() - 1);
		
		if (eat == null)
			return null;
		
		return eat;
	}
	
	public SBean.CityCFGS getUnlockCity(int cid)
	{
		for (SBean.CityCFGS cfg : capturecity.citys)
		{
			if (cfg.combatIDReq == cid)
				return cfg;
		}
		return null;
	}
	
	public SBean.CityCFGS getCityCfg(int id)
	{
		SBean.CityCFGS cfg = null;
		if (id > 0 && id <= capturecity.citys.size())
		{
			cfg = capturecity.citys.get(id-1);
			if (cfg.id != id)
				cfg = null;
		}
		return cfg;
	}
	
	public int getDiscountPrice(int price, float discount)
	{
		int r = (int)(price * (1 - discount));
		if( r > price )
			r = price;
		if( r < 1 )
			r = 1;
		return r;
	}
	
	public SBean.CaptureCityCFGS getCaptureCityCfg()
	{
		return capturecity;
	}

	public SBean.WeaponBasicCFGS getWeaponBasicCFG(short gid)
	{
		return weaponBasic.get(gid);
	}
	
	public SBean.WeaponEnhanceCFGS getWeaponEnhanceCFG(short gid)
	{
		return weaponEnhance.get(gid);
	}
	
	public SBean.WeaponResetCFGS getWeaponResetCFG()
	{
		return weaponReset;
	}
	
	public List<SBean.WeaponEvoCostCFGS> getWeaponEvoCostsCFG()
	{
		return weaponEvoCosts;
	}
	
	public SBean.GeneralStoneCFGS getGeneralStoneCFG()
	{
		return generalStone;
	}
	public SBean.ExpiratBossCFGS getExpiratBossCFG()
	{
		return expiratBoss;
	}
	
	public HeroesBossCFGS getHeroesBossCFG()
	{
		return heroesBoss;
	}
	
	public int getHeroesBossType(short level)
	{
		for(HeroesBossTypeCFGS type :heroesBoss.heroesBossType){
			if(type.type==1&&type.levelmin<=level&&type.levelmax>=level){
				return type.sid;
			}
		}
		return -1;
	}
	
	public HeroesBossItemsCFGS getHeroesBossBonusCFG(short rank)
	{
		for(HeroesBossItemsCFGS item :heroesBoss.heroesBossItems){
			if((rank)==item.rank){
				return item;
			}
		}
		
		return null;
	}
	
	public HeroesBossBuffCFGS getHeroesBossBuffCFG(short rank)
	{
		for(HeroesBossBuffCFGS item :heroesBoss.heroesBossBuff){
			if(rank+1==item.sid){
				return item;
			}
		}
		
		return null;
	}
	
	public HeroesBossScoreCFGS getHeroesBossScoreCFG(short rank,short type)
	{
		for(HeroesBossScoreCFGS item :heroesBoss.heroesBossScore){
			if((rank+1)==item.rank&&item.type==type){
				return item;
			}
		}
		
		return null;
	}
	
	
	public List<BestowCFGS> getBestowCFG()
	{
		return bestow;
	}
	
	public List<BlessCFGS> getBlessCFG()
	{
		return bless;
	}
	
	public BlessCFGS getBlessCFG(short level)
	{
		for(BlessCFGS icon :bless){
			if(icon.sid==level){
				return icon;
			}
		}
		return null;
	}
	
	public CommomConstantCFGS getCommonCFG()
	{
		return common;
	}
	
	public BestowCFGS getBestowCFG(short gid,short level)
	{
		for(BestowCFGS icon :bestow){
			if(icon.gid==gid&&icon.level==level+1){
				return icon;
			}
		}
		return null;
	}
	
	public List<HeadIconCFGS> getHeadiconCFG()
	{
		return headicon;
	}
	
	public HeadIconCFGS getHeadiconCFG(short id)
	{
		for(HeadIconCFGS icon :headicon){
			if(icon.sid==id){
				return icon;
			}
		}
		return null;
	}
	
	public DiskBetCFGS getDiskBetCFG()
	{
		return diskBet;
	}
	
	public Short getDiskBetCostCFG(short count ,short index)
	{
		if(diskBet==null||diskBet.cost==null){
			return -1;
		}
		if(diskBet.cost.size()<1){
			return -1;
		}
		for(DiskBetCostCFGS  cost : diskBet.cost){
			
			if(cost.sid==(count+1)){
				return cost.cost.get(index-1);
			}
		}
		
		return diskBet.cost.get(diskBet.cost.size()-1).cost.get(index-1);
	}
	
	public Short getDiskBetCostCFG(short index)
	{
		if(diskBet==null||diskBet.cost==null){
			return -1;
		}
		if(diskBet.cost.size()<1){
			return -1;
		}
		short free =0;
		for(DiskBetCostCFGS  cost : diskBet.cost){
			
			if(cost.cost.get(index-1)==0){
				free++;
			}
		}
		
		return free;
	}
	
	public ExpiratBossBasicCFGS getExpiratBossBasicCFG(short iid)
	{
		return expiratBoss.expiratBossBasic.get(iid-1);
	}
	
	public ExpiratBossItemsCFGS getExpiratBossBonusCFG(short rank)
	{
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(rank>=item.rankmin&&(rank<=item.rankmax)){
				return item;
			}
		}
		
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(item.rankmax==-1){
				return item;
			}
		}
		return null;
	}
	
	public ExpiratBossItemsCFGS getExpiratBossBonusCFG(short sid,short rank,short type)
	{
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(item.sid==sid&&item.type==type&&rank>=item.rankmin&&(rank<=item.rankmax)){
				return item;
			}
		}
		
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(item.sid==sid&&item.type==type&&item.rankmax==-1){
				return item;
			}
		}
		return null;
	}
	
	public ExpiratBossItemsCFGS getExpiratBossBonusMinCFG(short sid,short type)
	{
				
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(item.sid==sid&&item.type==type&&item.rankmax==-1){
				return item;
			}
		}
		return null;
	}
	
	public ExpiratBossItemsCFGS getExpiratBossBonusMinCFG(short rank)
	{
				
		for(ExpiratBossItemsCFGS item :expiratBoss.expiratBossItems){
			if(item.rankmax==-1){
				return item;
			}
		}
		return null;
	}
	
	public GeneralStoneBasicCFGS getGeneralStoneBasicTypeCFG(short iid,short k)
	{
		GeneralStoneBasicCFGS st =null;
		for(GeneralStoneBasicCFGS kn :generalStoneBasic.values()){
			if(kn.stype==iid){
				st = kn;
				if(k==1){
					break;
				}
			}
		}
		return st;
	}
	
	public GeneralStoneBasicCFGS getGeneralStoneBasicCFG(short iid)
	{
		return generalStoneBasic.get(iid);
	}
	public int getchangeServerDay(int sid,int toid)
	{
		if(changeServer.serverList==null){
			return 0;
		}
		for(ChangeServerListCFGS server:changeServer.serverList){
			if(server.sid==sid&&server.toid==toid){
				return server.days;
			}
		}
		return 0;
	}
	
	
	public List<GeneralStonePropCFGS> getGeneralStoneSpropCFG()
	{
		return generalStone.sprop;
	}
	
	public List<GeneralStonePropValueCFGS> getGeneralStoneSpropValueCFG()
	{
		return generalStone.spropValue;
	}
	
	public List<GeneralStoneEvoCostCFGS> getGeneralStoneEvoCostsCFG()
	{
		return generalStone.evoCosts;
	}
	
	public GeneralStonePosCFGS getGeneralStonePosCFG(short gid)
	{
		return generalStonePos.get(gid);
	}
	
	Map<Short, SBean.DropTableCFGS> dropTables = new TreeMap<Short, SBean.DropTableCFGS>();
	Map<Short, SBean.CombatCFGS> combats = new TreeMap<Short, SBean.CombatCFGS>();
	Map<Byte, SBean.CombatEventCFGS> combatEvents = new TreeMap<Byte, SBean.CombatEventCFGS>();
	Map<Short, SBean.EquipCFGS> equips = new TreeMap<Short, SBean.EquipCFGS>();
	Map<Short, SBean.ItemCFGS> items = new TreeMap<Short, SBean.ItemCFGS>();
	SBean.ShopCFGS shopNormal;
	SBean.ShopCFGS shopArena;
	SBean.ShopCFGS shopMarch;
	SBean.ShopCFGS shopForce;
	SBean.ShopCFGS shopRand1;
	SBean.ShopCFGS shopRand2;
	SBean.ShopCFGS shopRichStone;
	SBean.ShopCFGS shopRichPoint;
	SBean.ShopCFGS shopRichGold;
	SBean.ShopCFGS shopSuperArena;
	SBean.ShopCFGS shopDuel;
	SBean.ShopCFGS shopBless;
	Map<Byte, Map<Short, SBean.ShopGoods>> shopMap = new TreeMap<Byte, Map<Short, SBean.ShopGoods>>();
	Map<Short, SBean.GeneralCFGS> generals = new TreeMap<Short, SBean.GeneralCFGS>();
	Map<Short, SBean.PetCFGS> pets = new TreeMap<Short, SBean.PetCFGS>();
	Map<Short, SBean.PetCFGS> deformPets = new TreeMap<Short, SBean.PetCFGS>();
	Map<Short, SBean.TaskGroupCFGS> taskGroups = new TreeMap<Short, SBean.TaskGroupCFGS>();
	Map<Short, SBean.PackageCFGS> packages = new TreeMap<Short, SBean.PackageCFGS>();
	Map<Short, SBean.CmnRewardCFGS> cmnRewards = new TreeMap<Short, SBean.CmnRewardCFGS>();
	Map<Byte, SBean.EggCFGS> eggs = new TreeMap<Byte, SBean.EggCFGS>();
	List<SBean.SoulBoxCFGS> soulboxs;
	SBean.CaptureCityCFGS capturecity;
	SBean.ForceBattlesCFGS forceBattles;
	SBean.RichCFGS rich;
	SBean.InvitationCFGS invitation;
	SBean.PayCFGS payDefault;
	SBean.MonthlyCardCFGS mcard;
	Map<String, SBean.PayCFGS> pay = new HashMap<String, SBean.PayCFGS>();
	SBean.ForceCFGS force;
	SBean.CombatServerCFG combat;
	SBean.EventCFGS event;
	SBean.ArenaCFGS arena;
	SBean.SuperArenaCFGS superArena;
	int[] arenaBestRankMoney;
	int[] arenaBestRankStone;
	SBean.MarchCFGS march;
	List<SBean.PowerCFGS> power;
	SBean.UserInputCFGS userInput;
	SBean.GeneralCmnCFGS generalsCmn;
	SBean.PetCmnCFGS petsCmn;
	SBean.PetFarmCFGS petFarm;
	SBean.RoleCmnCFGS roleCmn;
	SBean.CombatCmnCFGS combatCmn;
	SBean.EquipCmnCFGS equipCmn;
	List<SBean.CheckinCFGS> checkin;
	List<SBean.CombatCatCFG> combatCats;
	List<SBean.VIPCFGS> vip;
	List<SBean.DailyTask2CFGS> dtask2;
	Map<Byte, SBean.DailyTask2CFGS> dtask2Map;
	List<SBean.DailyActivityRewardCFGS> dailyActivity;
	List<SBean.QQVIPRewardsCFGS> qqvipRewards;
	//SBean.LevelLimitCFGS levelLimit;
	SBean.TopLevelAuraCFGS topLevelAura;
	List<String> badnames;
	List<String> badnames2;
	SBean.QQDirtyWordCFGS qqDirtyWords;
	short[] vitMaxTable;
	SBean.FriendCFGS friend;
	SBean.DropTableCFGS eggItemRank2;
	Random random = new Random();
	SecureRandom srandom = new SecureRandom();
	Map<Short, SBean.WeaponBasicCFGS> weaponBasic = new TreeMap<Short, SBean.WeaponBasicCFGS>();
	Map<Short, SBean.WeaponEnhanceCFGS> weaponEnhance = new TreeMap<Short, SBean.WeaponEnhanceCFGS>();
	SBean.GeneralStoneCFGS generalStone = new SBean.GeneralStoneCFGS();
	SBean.GeneralSeyenCFGS generalSeyen = new SBean.GeneralSeyenCFGS();
	SBean.ChangeServerCFGS changeServer = new SBean.ChangeServerCFGS();
	SBean.ExpiratBossCFGS expiratBoss = new SBean.ExpiratBossCFGS();
	SBean.HeroesBossCFGS heroesBoss = new HeroesBossCFGS();
	Map<Integer, Integer> generalSeyenExp = new TreeMap<Integer, Integer>();
	Map<Integer, Integer> generalSeyenLimit = new TreeMap<Integer, Integer>();
	Map<Short, SBean.GeneralStoneBasicCFGS> generalStoneBasic = new TreeMap<Short, SBean.GeneralStoneBasicCFGS>();
	Map<Short, SBean.GeneralStonePosCFGS> generalStonePos = new TreeMap<Short, SBean.GeneralStonePosCFGS>();
	SBean.WeaponResetCFGS weaponReset;
	List<SBean.WeaponEvoCostCFGS> weaponEvoCosts;
	SBean.DungeonCFGS dungeon;
	Map<Short, SBean.RelationCFGS> relations;
	Map<Short, Short> relationItems;
	Map<Short, List<Integer>> relationConsumes;
	SBean.MercCFGS merc;
	SBean.DuelCFGS duel;
	SBean.SuraCFGS sura;
	SBean.ForceSWarCFGS swar;
	SBean.OfficialCFGS official;
	SBean.DiskBetCFGS diskBet = new SBean.DiskBetCFGS();
	List<BestowCFGS> bestow;
	List<SBean.BlessCFGS> bless;
	SBean.CommomConstantCFGS common = new SBean.CommomConstantCFGS();
	SBean.BestowItemCFGS bestowItem;
	List<HeadIconCFGS> headicon;
	
	SBean.IPadCFGS ipad;
	
	SBean.TreasureMapBaseCFGS treasureMap;
	SBean.BeMonsterCFGS beMonster;
	
	public static int expirateCost = 0;
	public static short bestowCost = 0;
	public static short bestowId = 0;
	public static int forceBattleCount = 0;
	private static int timeAddGM = 0;
}


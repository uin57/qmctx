package i3k;

import i3k.SBean.DBBlessInfo;
import i3k.SBean.DBCombatEventLog;
import i3k.SBean.DBCombatLog;
import i3k.SBean.DBCombatPos;
import i3k.SBean.DBCombatScore;
import i3k.SBean.DBEggLog;
import i3k.SBean.DBGeneralEquip;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBItemDropLog;
import i3k.SBean.DBPet;
import i3k.SBean.DBRoleEquip;
import i3k.SBean.DBRoleItem;
import i3k.SBean.DBRoleSeyen;
import i3k.SBean.DBShop;
import i3k.SBean.DBTaskPos;
import i3k.gs.ArenaManager;
import i3k.gs.GameData;
import i3k.gs.GameServer;
import i3k.gs.LoginManager;
import i3k.gs.MarchManager;
import i3k.gs.Role;
import i3k.gs.TLogger;

import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;

public class DBRole implements Stream.IStreamable
{
	public static int VERSION_NOW = 2;
	
	public static class PayLog implements Stream.IStreamable
	{		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			uuid = is.popString();
			state = is.popByte();
			timeStamp = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(uuid);
			os.pushByte(state);
			os.pushInteger(timeStamp);
		}
		
		public PayLog ksClone()
		{
			PayLog c = new PayLog();
			c.uuid = uuid;
			c.state = state;
			c.timeStamp = timeStamp;
			return c;
		}
		
		public String uuid;
		public byte state;
		public int timeStamp;
	}
	
	public DBRole() { }

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		id = is.popInteger();
		dayCommon = is.popInteger();
		dayFlag = is.popInteger();
		helpFlag1 = is.popInteger();
		name = is.popString();
		userName = is.popString();
		money = is.popInteger();
		stone = is.popInteger();
		lvlDay = is.popShort();
		exp = is.popInteger();
		lvl = is.popShort();
		headIconID = is.popShort();
		flag = is.popInteger();
		vit = is.popShort();
		vitRecoverStart = is.popInteger();
		skillPoint = is.popShort();
		skillPointRecoverStart = is.popInteger();
		createTime = is.popInteger();
		lastLoginTime = is.popInteger();
		lastLogoutTime = is.popInteger();
		is.pop(checkinLog);
		upgradeTime = is.popInteger();		
		taskPos = is.popList(DBTaskPos.class);
		generals = is.popList(DBRoleGeneral.class);
		pets = is.popList(DBPet.class);
		petsTop = is.popShortList();
		petsFeeding = is.popList(SBean.DBFeedingPet.class);
		items = is.popList(DBRoleItem.class);
		itemDropLogs = is.popList(DBItemDropLog.class);
		equips = is.popList(DBRoleEquip.class);
		combatFightLogs = is.popList(DBCombatLog.class);
		combatScores = is.popList(DBCombatScore.class);
		combatPos = is.popList(DBCombatPos.class);
		eggLogs = is.popList(DBEggLog.class);
		combatEventLogs = is.popList(DBCombatEventLog.class);
		if( shopNormal == null )
			shopNormal = new DBShop();
		is.pop(shopNormal);
		if( shopArena == null )
			shopArena = new DBShop();
		is.pop(shopArena);
		if( shopMarch == null )
			shopMarch = new DBShop();
		is.pop(shopMarch);
		if( shopRand1 == null )
			shopRand1 = new DBShop();
		is.pop(shopRand1);
		if( shopRand2 == null )
			shopRand2 = new DBShop();
		is.pop(shopRand2);
		
		forceID = is.popInteger();
		forcePoint = is.popInteger();
		forceContrib = is.popInteger();
		forceJoinTime = is.popInteger();
		forceName = is.popString();
		forceMobai = is.popIntegerList();
		forceMobaiReward = is.popInteger();
		forceSuperMobai = is.popShort();
		forceIconId = is.popShort();
		
		stonePayMidas = is.popInteger();
		stoneUsedTotal = is.popInteger();
		lvlVIP = is.popByte();
		vipRewards = is.popByteList();
			
		combatResetLogs = is.popList(DBCombatLog.class);
			
		vitUsedToday = is.popShort();
		
		cdkeys = is.popShortList();
		worldGifts = is.popShortList();
		
		dailyTask2Logs = is.popList(SBean.DBDailyTask2Log.class);
		dailyActivity = is.popInteger();
		dailyActivityRewards = is.popByteList();
		monthlyCardStartTime = is.popInteger();
		payLvlState = is.popByteList();
		
		buyMoneyTimes = is.popShort();
		buyVitTimes = is.popShort();
		buySkillPointTimes = is.popShort();
		
		
		if( arenaState == null ) 
			arenaState = new SBean.DBRoleArenaState();
		is.pop(arenaState);
		
		arenaLogs = is.popList(SBean.DBRoleArenaLog.class);
		
		arenaGenerals = is.popShortList();
		arenaPets = is.popShortList();

		if( marchState == null ) 
			marchState = new SBean.DBRoleMarchState();

		is.pop(marchState);
		marchGeneralStates = is.popList(SBean.DBRoleMarchGeneralState.class);
		marchEnemyGeneralStates = is.popList(SBean.DBRoleMarchGeneralState.class);
		marchCurrentEnemies = is.popList(SBean.DBRoleMarchEnemy.class);
		
		//deprecated Data 原封禁数据
		is.popIntegerList();
		
		payLogs = is.popList(PayLog.class);
		loginGiftMail = is.popIntegerList();
		checkinGift = is.popList(SBean.DBRoleCheckinGift.class);
		firstPayGift = is.popList(SBean.DBRoleFirstPayGift.class);
		boughtMoney = is.popByte();
		soulBoxLogs = is.popList(SBean.DBSoulBoxLog.class);
		
		banData = is.popList(SBean.DBBanData.class);
		
		arenaBestRank = is.popInteger();
		feedTotal = is.popInteger();
		stonePayAlpha = is.popInteger();
		
		if( citys == null ) 
			citys = new SBean.DBCitys();
		is.pop(citys);
		activity = is.popInteger();
		activityRewards = is.popByteList();
		
		consumeGift = is.popList(SBean.DBRoleConsumeGift.class);
		consumeRebate = is.popList(SBean.DBRoleConsumeRebate.class);
		upgradeGift = is.popList(SBean.DBRoleUpgradeGift.class);
		gatherGift = is.popList(SBean.DBRoleGatherGift.class);
		
		helpFlag2 = is.popInteger();
		openID = is.popString();
		if( openID.equals("") )
			openID = GameData.getInstance().getOpenIDByUserName(userName); // TODO
		shopForce = is.popList(DBShop.class);
		payGift = is.popList(SBean.DBRolePayGift.class);
		limitedShop = is.popList(SBean.DBRoleLimitedShop.class);
		loginGift = is.popList(SBean.DBRoleLoginGift.class);
		
		rich = is.popList(SBean.DBRichRole.class);
		
		marchPointReward = is.popByte();
		shopRich = is.popList(DBShop.class);
		
		lastForceWarID = is.popInteger();
		marchPointDay = is.popInteger();
		invitations = is.popList(SBean.DBInvitation.class);
		qqVipRewards = is.popByte();
		
		tradingCenter = is.popList(SBean.DBRoleTradingCenter.class);
		
		superArena = is.popList(SBean.DBRoleSuperArena.class);
		//richMovementRecoverStart = is.popInteger();
		petDeforms = is.popList(SBean.DBPetDeform.class);

		duel = is.popList(SBean.DBRoleDuel.class);
		sura = is.popList(SBean.DBRoleSura.class);
		shopSuperArena = is.popList(SBean.DBShop.class);
		
		relations = is.popList(SBean.DBRelation.class);
		mercGenerals = is.popList(SBean.DBMarchMercGeneral.class);
		
		generalStoneInfos = is.popList(SBean.DBGeneralStoneInfo.class);
		shopDuel = is.popList(SBean.DBShop.class);
		expiratBossInfo = is.popList(SBean.DBExpiratBossInfo.class);
		
		optionalHotPoint = is.popList(SBean.DBOptionalHotPoint.class);
		
		relationStone = is.popByte();
		heroesBossInfo = is.popList(SBean.DBHeroesBossInfo.class);
		
		if (dbVersion == 1) {
			vipShow = 1;
		}
		else if (dbVersion == 2) {
			vipShow = is.popByte();
			

			diskBet = is.popList(SBean.DBDiskBetInfo.class);
			treasureLimitCnt = is.popList(SBean.DBTreasureLimitCnt.class);
			rechargeGift = is.popList(SBean.DBRoleRechargeGift.class);
			bless = is.popList(SBean.DBBlessInfo.class);

			headicon = is.popList(SBean.DBHeadIcon.class);
			diskGift = is.popList(SBean.DBRoleDiskGift.class);
			diskBet2 = is.popList(SBean.DBDiskBetInfo.class);
			diskBet3 = is.popList(SBean.DBDiskBetInfo.class);

			shopBless = is.popList(SBean.DBShop.class);
	    	padding32= is.popByte();
	    	padding33= is.popByte();
	    	padding34= is.popByte();
			
			padding4 = is.popInteger();
			padding5 = is.popInteger();
			padding6 = is.popInteger();
			padding7 = is.popInteger();
			padding8 = is.popInteger();
			padding9 = is.popInteger();
		}
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);
		os.pushInteger(id);
		os.pushInteger(dayCommon);
		os.pushInteger(dayFlag);
		os.pushInteger(helpFlag1);
		os.pushString(name);
		os.pushString(userName);
		os.pushInteger(money);
		os.pushInteger(stone);
		os.pushShort(lvlDay);
		os.pushInteger(exp);
		os.pushShort(lvl);
		os.pushShort(headIconID);
		os.pushInteger(flag);
		os.pushShort(vit);
		os.pushInteger(vitRecoverStart);
		os.pushShort(skillPoint);
		os.pushInteger(skillPointRecoverStart);
		os.pushInteger(createTime);
		os.pushInteger(lastLoginTime);
		os.pushInteger(lastLogoutTime);
		os.push(checkinLog);
		os.pushInteger(upgradeTime);
		//System.out.println("save generals size = " + generals.size());
		os.pushList(taskPos);
		os.pushList(generals);
		os.pushList(pets);
		os.pushShortList(petsTop);
		os.pushList(petsFeeding);
		os.pushList(items);
		os.pushList(itemDropLogs);
		os.pushList(equips);
		os.pushList(combatFightLogs);
		os.pushList(combatScores);
		os.pushList(combatPos);
		os.pushList(eggLogs);
		os.pushList(combatEventLogs);
		os.push(shopNormal);
		os.push(shopArena);
		os.push(shopMarch);
		os.push(shopRand1);
		os.push(shopRand2);
		
		os.pushInteger(forceID);
		os.pushInteger(forcePoint);
		os.pushInteger(forceContrib);
		os.pushInteger(forceJoinTime);
		os.pushString(forceName);
		os.pushIntegerList(forceMobai);
		os.pushInteger(forceMobaiReward);
		os.pushShort(forceSuperMobai);
		os.pushShort(forceIconId);
		
		os.pushInteger(stonePayMidas);
		os.pushInteger(stoneUsedTotal);
		os.pushByte(lvlVIP);
		if( vipRewards == null )
			vipRewards = new ArrayList<Byte>();
		os.pushByteList(vipRewards);
		
		os.pushList(combatResetLogs);
		
		os.pushShort(vitUsedToday);
		
		os.pushShortList(cdkeys);
		os.pushShortList(worldGifts);
		
		os.pushList(dailyTask2Logs);
		os.pushInteger(dailyActivity);
		os.pushByteList(dailyActivityRewards);
		os.pushInteger(monthlyCardStartTime);
		os.pushByteList(payLvlState);
		
		os.pushShort(buyMoneyTimes);
		os.pushShort(buyVitTimes);
		os.pushShort(buySkillPointTimes);

		os.push(arenaState);
		os.pushList(arenaLogs);
		os.pushShortList(arenaGenerals);
		os.pushShortList(arenaPets);

		os.push(marchState);
		os.pushList(marchGeneralStates);
		os.pushList(marchEnemyGeneralStates);
		os.pushList(marchCurrentEnemies);
		
		//deprecated Data 原封禁数据
		os.pushIntegerList(new ArrayList<Integer>());
		
		os.pushList(payLogs);
		os.pushIntegerList(loginGiftMail);
		os.pushList(checkinGift);
		os.pushList(firstPayGift);
		os.pushByte(boughtMoney);
		os.pushList(soulBoxLogs);
		
		os.pushList(banData);
		
		os.pushInteger(arenaBestRank);
		os.pushInteger(feedTotal);
		os.pushInteger(stonePayAlpha);
		
		os.push(citys);
		os.pushInteger(activity);
		os.pushByteList(activityRewards);
		
		os.pushList(consumeGift);
		os.pushList(consumeRebate);
		os.pushList(upgradeGift);
		os.pushList(gatherGift);
		
		os.pushInteger(helpFlag2);
		os.pushString(openID);
		os.pushList(shopForce);
		os.pushList(payGift);
		os.pushList(limitedShop);
		os.pushList(loginGift);
		
		os.pushList(rich);
		
		os.pushByte(marchPointReward);
		os.pushList(shopRich);
		
		os.pushInteger(lastForceWarID);
		os.pushInteger(marchPointDay);
		os.pushList(invitations);
		os.pushByte(qqVipRewards);
		
		os.pushList(tradingCenter);
		
		os.pushList(superArena);
		//os.pushInteger(richMovementRecoverStart);
		os.pushList(petDeforms);

		os.pushList(duel);
		os.pushList(sura);
		os.pushList(shopSuperArena);
		
		os.pushList(relations);
		os.pushList(mercGenerals);
		
		os.pushList(generalStoneInfos);
		os.pushList(shopDuel);
		os.pushList(expiratBossInfo);
		os.pushList(optionalHotPoint);
		os.pushByte(relationStone);
		os.pushList(heroesBossInfo);
		
		// Version 2
		os.pushByte(vipShow);
		
		os.pushList(diskBet);
		os.pushList(treasureLimitCnt);
		os.pushList(rechargeGift);
		os.pushList(bless);
		os.pushList(headicon);
    	os.pushList(diskGift);
    	os.pushList(diskBet2);
    	os.pushList(diskBet3);
    	
    	os.pushList(shopBless);
    	os.pushByte(padding32);
    	os.pushByte(padding33);
    	os.pushByte(padding34);
		os.pushInteger(padding4);
		os.pushInteger(padding5);
		os.pushInteger(padding6);
		os.pushInteger(padding7);
		os.pushInteger(padding8);
		os.pushInteger(padding9);
	}
	
	public LoginManager.RoleBriefCache getCacheData(GameData gameData)
	{
		return new LoginManager.RoleBriefCache(lvl, headIconID, getArenaGeneralsPower() + getArenaPetsPower(), name, forceName, lastLoginTime, activity);
	}
	
	public SBean.DBRoleBrief getBriefWithoutFlag()
	{
		return new SBean.DBRoleBrief(id, lvl, headIconID, name, forceName, 0);
	}
	
	public SBean.DBRoleBrief getBriefWithFlag()
	{
		return new SBean.DBRoleBrief(id, lvl, headIconID, name, forceName, getFlag());
	}
	
	public int getFlag()
	{
		// TODO
		return 0;
	}
	
	public List<DBRoleGeneral> getDBArenaGenerals()
	{
		List<DBRoleGeneral> lst = new ArrayList<DBRoleGeneral>();
		for(short gid : arenaGenerals)
		{
			for(DBRoleGeneral g : generals)
			{
				if( g.id == gid )
				{
					lst.add(g);
					break;
				}
			}
		}
		return lst;
	}
	
	public List<SBean.DBPetBrief> getDBArenaPets()
	{
		List<SBean.DBPetBrief> lst = new ArrayList<SBean.DBPetBrief>();
		for(short pid : arenaPets)
		{
			for(SBean.DBPet p : pets)
			{
				if( p.id == pid )
				{
					byte deformStage = 0;
					for(SBean.DBPetDeform d : petDeforms)
						if (d.id == p.id)
							deformStage = d.deformStage;
					lst.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
					break;
				}
			}
		}
		return lst;
	}
	
	public DBRoleGeneral getGeneral(short gid)
	{
		for (DBRoleGeneral g : generals)
		{
			if (g.id == gid)
				return g;
		}
		return null;
	}
	
	public SBean.DBPet getPet(short pid)
	{
		for (SBean.DBPet p : pets)
		{
			if (p.id == pid)
				return p;
		}
		return null;
	}
	
	public SBean.DBPetBrief getPetBrief(short pid)
	{
		for (SBean.DBPet p : pets)
		{
			if (p.id == pid) {
				byte deformStage = 0;
				for(SBean.DBPetDeform d : petDeforms)
					if (d.id == p.id)
						deformStage = d.deformStage;
				return new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name);
			}
		}
		return null;
	}
	
	public SBean.RoleDataGeneral getGeneralData(short gid)
	{
		for (DBRoleGeneral g : generals)
		{
			if (g.id == gid)
				return new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo);
		}
		return null;
	}
	
	public SBean.RoleDataPet getPetData(short pid)
	{
		for (SBean.DBPet p : pets)
		{
			if (p.id == pid)
				return new SBean.RoleDataPet(p.id, p.lvl, p.evoLvl, p.growthRate, p.name);
		}
		return null;
	}
	
	public SBean.FriendPets copyFriendPets()
	{
		SBean.FriendPets fpets = new SBean.FriendPets();
		fpets.petsG1 = new ArrayList<SBean.DBPetBrief>();
		fpets.petsTop = new ArrayList<SBean.DBPetBrief>();
		short petsTopID = 0;
		if( ! petsTop.isEmpty() )
			petsTopID = petsTop.get(0).shortValue();
		for(SBean.DBPet p : pets)
		{
			byte deformStage = 0;
			for(SBean.DBPetDeform d : petDeforms)
				if (d.id == p.id)
					deformStage = d.deformStage;
			if( GameData.getInstance().getPetCFG(p.id, false).gender == 1 && p.lvl >= GameData.getInstance().getPetCmnCFG().breedReqLvl )
				fpets.petsG1.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
			if( p.id == petsTopID )
				fpets.petsTop.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
		}
		return fpets;
	}
	
	/*
	public List<SBean.FriendPetDeform> copyFriendPetDeforms(SBean.FriendPets fpets)
	{
		List<SBean.FriendPetDeform> petDeformsTop = new ArrayList<SBean.FriendPetDeform>();
		for(SBean.DBPetDeform p : petDeforms)
		{
			for (SBean.DBPetBrief fpb : fpets.petsTop)
				if (fpb.id == p.id)
					petDeformsTop.add(new SBean.FriendPetDeform(p.id, p.deformStage, 0, 0));
		}
		return petDeformsTop;
	}
	*/
	
	public boolean testFlag(int mask)
	{
		return (flag & mask) != 0;
	}
	
	public void clearFriendFlag(int mask)
	{
		flag &= ~mask;
	}
	
	public byte getQQVIP()
	{
		if( testFlag(Role.ROLE_FLAG_QQ_VIP_SUPER) )
			return (byte)2;
		if( testFlag(Role.ROLE_FLAG_QQ_VIP_NORMAL) )
			return (byte)1;
		return (byte)0;
	}
	
	public SBean.FriendProp getFriendProp()
	{
		return new SBean.FriendProp(lvl, lastLoginTime, name, getDBArenaGenerals(), getDBArenaPets(), copyFriendPets()
				, forceName, headIconID, getQQVIP(), new ArrayList<SBean.FriendPetDeform>(), getActiveRelations(), getActiveGeneralStones(), (byte)0);
	}
	
	public int getUpgradeExp(GameData gameData)
	{
		return gameData.getUpgradeExp(lvl);
	}

	private TLogger.CommonItemChange addVit1(int vitAdd)
	{
		if( vitAdd <= 0 )
			return null;
		short max = GameData.getInstance().getVitMax(lvl);
		if( vit >= max )
			return null;
		int beforeVit = vit;
		vit = (short)(vit + vitAdd);
		if( vit >= max || vit <= 0)
		{
			vit = max;
			vitRecoverStart = 0;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 0, vit-beforeVit, vit);
	}
	
	private TLogger.CommonItemChange addVit2(int vitAdd)
	{
		if( vitAdd <= 0 )
			return null;
		short max1 = GameData.getInstance().getVitMax(lvl);
		short max2 = GameData.getInstance().getRoleCmnCFG().vitMax;
		if( vit >= max2 )
			return null;
		int beforeVit = vit;
		vit = (short)(vit + vitAdd);
		if( vit >= max2  || vit <= 0 )
		{
			vit = max2;
		}
		if( vit >= max1 )
		{
			vitRecoverStart = 0;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 0, vit-beforeVit, vit);
	}
	
	private void addSkillPoint(int add)
	{
		if( add <= 0 )
			return;
		short max = GameData.getInstance().getSkillPointMax(lvlVIP);
		if( skillPoint >= max )
			return;
		skillPoint = (short)(skillPoint + add);
		if( skillPoint >= max || skillPoint <= 0)
		{
			skillPoint = max;
			skillPointRecoverStart = 0;
		}
	}	
	
	public void levelUp(GameData gameData)
	{
		++lvl;
		upgradeTime = GameData.getInstance().getTime();
		addVit2(GameData.getInstance().getRoleCmnCFG().vitAddLvlup.get(lvl-2));
	}
	
	public void addExp(int expAdd, GameData gameData, GameServer gs)
	{
		if( expAdd <= 0 )
			return;
		//if( lvl >= gs.getRoleLevelLimit() )
		int levellimit = gs.getRoleLevelLimit();
		if (lvl >= levellimit)
			return;
		exp += expAdd;
		while( exp >= getUpgradeExp(gameData) )
		{
			exp -= getUpgradeExp(gameData);
			levelUp(gameData);
			//if( lvl >= gs.getRoleLevelLimit() )
			if ( lvl >= levellimit)
				return;
		}
	}

	public static class PVPGeneral
	{
		public PVPGeneral(short id, byte lvl3)
		{
			this.id = id;
			this.lvl3 = lvl3;
		}
		public short id;
		public byte lvl3;
		public short horseID = -1;
		public int score;
	}
	
	public void arenaAddLog(SBean.DBRoleArenaLog log)
	{
		arenaLogs.add(log);
		if( arenaLogs.size() > 10 )
		{
			arenaLogs.remove(0);
		}
	}

	public synchronized void getData(SBean.RoleDataRes res)
	{
		if( ( res.req.qtype & SBean.RoleDataReq.eBrief) != 0 )
		{
			res.brief = new SBean.RoleDataBrief();
			res.brief.id = id;
			res.brief.name = name;
			res.brief.uname = userName;
			res.brief.lvl = lvl;
			res.brief.money = money;
			res.brief.stone = stone;
			res.brief.gcount = generals.size();
			res.brief.icount = items.size();
			res.brief.ecount = equips.size();
			res.brief.headIconID = headIconID;
			res.brief.fname = forceName;
			res.brief.arenaPower = 0;
			res.brief.arenaWinTimes = arenaState.winTimes;
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eGeneral) != 0 )
		{
			res.generals = new ArrayList<SBean.RoleDataGeneral>();
			for(DBRoleGeneral g : generals)
			{
				res.generals.add(new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo));
			}
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eItem) != 0 )
		{
			res.items = items;
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eEquip) != 0 )
		{
			res.equips = equips;
		}
		if( ( res.req.qtype & SBean.RoleDataReq.eArenaGeneral) != 0 )
		{
			res.arenagenerals = new ArrayList<SBean.RoleDataGeneral>();
			for(DBRoleGeneral g : generals)
			{
				for (short gid : arenaGenerals)
				{
					if (g.id == gid) {
						res.arenagenerals.add(new SBean.RoleDataGeneral(g.id, g.lvl, g.advLvl, g.evoLvl, g.weapon.lvl, g.weapon.evo));
						break;
					}
				}
			}
			res.arenapets = new ArrayList<SBean.RoleDataPet>();
			for(SBean.DBPet p : pets)
			{
				for (short pid : arenaPets)
				{
					if (p.id == pid) {
						res.arenapets.add(new SBean.RoleDataPet(p.id, p.lvl, p.evoLvl, p.growthRate, p.name));
						break;
					}
				}
			}
			res.brief.arenaPower = getArenaGeneralsPower();
			res.brief.arenaPower += getArenaPetsPower();
		}
		if ( (res.req.qtype & SBean.RoleDataReq.eSuperArenaTeam) != 0 )
		{
			if (!superArena.isEmpty())
			{
				SBean.DBRoleSuperArena sarena = superArena.get(0);
				int curOrder = getSuperArenaCurOrder(sarena);
				List<SBean.DBRoleArenaFightTeam> teams = ArenaManager.SuperArena.getOrderedFightTeams(sarena.team.teams, curOrder);
				res.superArena = new SBean.RoleDataSuperArena(new ArrayList<SBean.RoleDataFightTeam>(), sarena.winTimes);
				int hiddenTeams = getSuperArenaTeamHideCount(sarena);
				for (int index = 0; index < teams.size(); ++index)
				{
					if (index < hiddenTeams)
					{
						res.superArena.teams.add(null);
					}
					else
					{
						SBean.DBRoleArenaFightTeam t = teams.get(index);
						res.superArena.teams.add(superArenaFillTeam(t));
					}
				}
			}
		}
		res.res = SBean.RoleDataRes.eOK;
	}
	
	public static int getSuperArenaCurOrder(SBean.DBRoleSuperArena superArena)
	{
		int orderIndex = superArena.team.curOrderIndex < 0 ? 0 : (superArena.team.curOrderIndex % superArena.team.orders.size());
		return superArena.team.orders.get(orderIndex);
	}
	
	public static int getSuperArenaTeamHideCount(SBean.DBRoleSuperArena superArena)
	{
		SBean.SuperArenaCFGS cfgs = GameData.getInstance().getSuperArenaCFG();
		int dayNow = getDayCommon();
		int hideTeams = 0;
		if (superArena.team.hideTime != 0)
		{
			if (superArena.team.hideTime + cfgs.hideTeamDaySpan > dayNow)
			{
				hideTeams = superArena.team.hiddenTeams;
			}
		}
		return hideTeams;
	}
	
	SBean.RoleDataFightTeam superArenaFillTeam(SBean.DBRoleArenaFightTeam t)
	{
		SBean.RoleDataFightTeam team = new SBean.RoleDataFightTeam(new ArrayList<SBean.RoleDataGeneral>(), new ArrayList<SBean.RoleDataPet>());
		for (short gid : t.generals)
		{
			SBean.RoleDataGeneral g = getGeneralData(gid);
			team.generals.add(g);
		}
		for (short pid : t.pets)
		{
			SBean.RoleDataPet p = getPetData(pid);
			team.pets.add(p);
		}
		return team;
	}
	
	SBean.SuperArenaTeamDetail superArenaFillTeamDetail(SBean.DBRoleArenaFightTeam t)
	{
		SBean.SuperArenaTeamDetail team = new SBean.SuperArenaTeamDetail(new ArrayList<DBRoleGeneral>(), new ArrayList<SBean.DBPetBrief>(), new ArrayList<SBean.DBRelation>(), new ArrayList<SBean.DBGeneralStone>());
		for (short gid : t.generals)
		{
			DBRoleGeneral g = getGeneral(gid);
			team.generals.add(g);
		}
		for (short pid : t.pets)
		{
			SBean.DBPetBrief p = getPetBrief(pid);
			team.pets.add(p);
		}
		team.relation = getActiveRelations();
		team.gStones = getActiveGeneralStones();
		return team;
	}
	
	public List<SBean.SuperArenaTeamDetail> superArenaGetFightTeamDetail(SBean.DBRoleSuperArena superArena)
	{
		List<SBean.SuperArenaTeamDetail> teamsdetail = new ArrayList<SBean.SuperArenaTeamDetail>();
		int curOrder = getSuperArenaCurOrder(superArena);
		List<SBean.DBRoleArenaFightTeam> teams = ArenaManager.SuperArena.getOrderedFightTeams(superArena.team.teams, curOrder);
		for (int index = 0; index < teams.size(); ++index)
		{
			SBean.DBRoleArenaFightTeam t = teams.get(index);
			SBean.SuperArenaTeamDetail teamDetail = superArenaFillTeamDetail(t);
			teamsdetail.add(teamDetail);
		}
		return teamsdetail;
	}
	
	public SBean.SuperArenaFightInfo superArenaGetFightInfo()
	{
		if (superArena.isEmpty())
			return null;
		SBean.DBRoleSuperArena sarena = superArena.get(0);
		SBean.SuperArenaFightInfo info = new SBean.SuperArenaFightInfo(this.id, this.headIconID, this.name, this.lvl, this.superArenaGetFightTeamDetail(sarena));
		return info;
	}
	
	public void superArenaChangeFightTeamOrder()
	{
		if (superArena.isEmpty())
			return;
		SBean.DBRoleSuperArena sarena = superArena.get(0);
		sarena.team.curOrderIndex++;
		sarena.team.curOrderIndex = sarena.team.curOrderIndex % sarena.team.orders.size();
	}
	
	public void superArenaAddLog(SBean.DBRoleArenaLog log)
	{
		if (superArena.isEmpty())
			return;
		SBean.DBRoleSuperArena sarena = superArena.get(0);
		sarena.logs.add(log);
		if( sarena.logs.size() > 10 )
		{
			sarena.logs.remove(0);
		}
	}
	
	public void superArenaAddRankReward(int reward)
	{
		if (superArena.isEmpty())
			return;
		SBean.DBRoleSuperArena sarena = superArena.get(0);
		if (sarena.rankRewards + reward >= GameData.MAX_POINT)
			sarena.rankRewards = GameData.MAX_POINT;
		else
			sarena.rankRewards += reward;
	}
	
	static int getDayCommon()
	{
		return getDayByOffset(GameData.getInstance().getRoleCmnCFG().timeDayRefresh);
	}
	
	static short getDayByOffset(short o)
	{
		return (short)((GameData.getInstance().getTime() - o * 60) / 86400);
	}
	
	
	public synchronized void getMarchEnemy(MarchManager marchManager, SBean.DBRoleMarchEnemy enemy)
	{
		enemy.id = id;
		enemy.name = name;
		enemy.headIconID = headIconID;
		enemy.fname = forceName;
		enemy.lvl = lvl;
		enemy.generals = new ArrayList<DBRoleGeneral>();
		enemy.pets = new ArrayList<SBean.DBPetBrief>();
		SBean.DBPowerRank rank = marchManager.copyPowerRoleRankData(id);
		if (rank == null)
			return;
		for(DBRoleGeneral g : generals)
		{
			for (short gid : rank.generals)
			{
				if (g.id == gid) {
					enemy.generals.add(g.kdClone());
					break;
				}
			}
		}
		for(SBean.DBPet p : pets)
		{
			if (p.id == rank.pets) {
				byte deformStage = 0;
				for(SBean.DBPetDeform d : petDeforms)
					if (d.id == p.id)
						deformStage = d.deformStage;
				enemy.pets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
				break;
			}
		}
	}
	
	public synchronized void getSuraEnemy(SBean.DBSuraEnemy enemy)
	{
		enemy.id = id;
		enemy.name = name;
		enemy.headIconID = headIconID;
		enemy.fname = forceName;
		enemy.lvl = lvl;
		enemy.generals = getDBArenaGenerals();
		enemy.pets = getDBArenaPets();
	}
	
	public int calcPetPower(short pid) 
	{
		float appraise = 0;

		SBean.DBPet pet = null;
		boolean deformed = false;
		synchronized(this) {
			for (DBPet dbpet : pets) {
				if (dbpet.id == pid) {
					pet = dbpet.kdClone();
					break;
				}
			}
			for (SBean.DBPetDeform dbpet : petDeforms) {
				if (dbpet.id == pid) {
					deformed = Role.isPetDeformed(dbpet.deformStage);
					break;
				}
			}
		}
		
		if (pet == null)
			return 0;
		
		SBean.PetCFGS pcfg = GameData.getInstance().getPetCFG(pid, deformed);
		if (pcfg != null) {
			appraise = pcfg.initPower + pcfg.upPower * (pet.growthRate/100.0f) * (pet.lvl - 1);
			
			SBean.PetEvoCFGS evoCfg = pcfg.evo.get(pet.evoLvl - 1);
			if (evoCfg != null) {
				appraise += evoCfg.power;
			}
		}

		return (int)(appraise + 0.5f);
	}
	
	public int calcGeneralPower(short gid) 
	{
		DBRoleGeneral general = null;
		synchronized(this) {
			for(DBRoleGeneral g : generals) {
				if (g.id == gid) {
					general = g.kdClone();
				}
			}
		}
		
		return calcGeneralPower(general);
	}
		
	public static int calcGeneralPower(DBRoleGeneral general)
	{
		if (general == null)
			return 0;
		
		List<SBean.PowerCFGS> cfgs = GameData.getInstance().getPowerCFG();
		SBean.PowerCFGS cfg = null;
		for (SBean.PowerCFGS e : cfgs) {
			if (e.id == general.id) {
				cfg = e;
				break;
			}
		}
		
		if (cfg == null)
			return 0;
		
		int advLvl = general.advLvl - 1;
		int evoLvl = general.evoLvl - 1;
		
		int lvlApp = cfg.initPower + (general.lvl - 1) * cfg.upPower;
		
		int equApp = 0;
		SBean.GeneralCFGS gcfg = GameData.getInstance().getGeneralCFG(general.id);
		for (int i = 0; i < GameData.GENERAL_EQUIP_COUNT; i ++) {
			DBGeneralEquip e = general.equips.get(i);
			if (e != null && e.lvl >= 0) {
				if (gcfg.equips.size() <= advLvl * GameData.GENERAL_EQUIP_COUNT + i)
					return 0;
				short eid = gcfg.equips.get(advLvl * GameData.GENERAL_EQUIP_COUNT + i).shortValue();
				SBean.EquipCFGS ecfg = GameData.getInstance().getEquipCFG(eid);
				if (ecfg != null) {
					equApp += ecfg.powers.get(0);
					for (int j = 0; j < e.lvl; j ++)
						equApp += ecfg.powers.get(j + 1);
				}
			}
		}
		
		int sklApp = 0;
		SBean.GeneralCmnCFGS generalCmnCfg = GameData.getInstance().getGeneralCmnCFG();
		for (int i = 0; i < GameData.GENERAL_SKILL_COUNT; i ++) {
			SBean.GeneralSkillCFGS skillcfg = generalCmnCfg.skills.get(i);
			if (general.advLvl >= skillcfg.reqAdvLvl) {
				short sklLvl = general.skills.get(i).shortValue();
				if (sklLvl >= (short)1) {
					int skillInitPower = 0;
					int size = generalCmnCfg.skillInitPower.size();
					if (i < size)
						skillInitPower = generalCmnCfg.skillInitPower.get(i);
					sklApp += skillInitPower + (sklLvl - 1) * generalCmnCfg.skillUpPower;
				}
			}
		}
		
		int advApp = 0;
		for (int i = 0; i < advLvl; i ++)
			advApp += gcfg.advPowerUps.get(i);
		
		return (int)((lvlApp + equApp + advApp) * (1 + generalCmnCfg.evoPowerScales.get(evoLvl).floatValue()) + 0.5f) + sklApp; 
	}
	
	public int getStonePayTotal()
	{
		return stonePayMidas + stonePayAlpha;
	}
	
	public synchronized int getArenaGeneralsPower()
	{
		int power = 0;
		for (int i = 0; i < 5; i ++)
		{
			short gid = arenaGenerals.get(i);
			power += calcGeneralPower(gid);
		}
		return power;
	}
	
	public synchronized int getArenaPetsPower()
	{
		int power = 0;
		short gid = arenaPets.get(0);
		power += calcPetPower(gid);
		return power;
	}
	
	//gm传入count做uint来理解
	public TLogger.CommonItemChange gmDelItem(int itemID, int count)
	{
		TLogger.CommonItemChange record = null;
		short delCount = 0;
		short afterCount = 0;
		SBean.DBRoleItem t = null;
		int index = 0;
		for(SBean.DBRoleItem e : items)
		{
			if( e.id == itemID )
			{
				t = e;
				break;
			}
			index++;
		}
		if( t != null )
		{
			if (count < 0 || count >= t.count)
			{
				items.remove(index);
				delCount = t.count;
				afterCount = 0;
			}
			else
			{
				t.count = (short)(t.count - count);
				delCount = (short)count;
				afterCount = t.count;
			}
			record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_ITEM, itemID, -delCount, afterCount);
		}
		return record;
	}
	
	//gm传入count做uint来理解
	public TLogger.CommonItemChange gmAddItem(int itemID, int count)
	{
		SBean.DBRoleItem t = null;
		for(SBean.DBRoleItem e : items)
		{
			if( e.id == itemID )
			{
				t = e;
				break;
			}
		}
		int nowCount = 0;
		if (t != null)
			nowCount = t.count;
		long newCount = nowCount + count;
		if (count < 0 || newCount >= GameData.MAX_ITEM_COUNT)
			newCount = GameData.MAX_ITEM_COUNT;
		if (t != null)
			t.count = (short)newCount;
		else
			items.add(new SBean.DBRoleItem((short)itemID, (short)newCount));
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_ITEM, itemID, (int)(newCount-nowCount), (int)newCount);
	}
	
	//gm传入count做uint来理解
		public TLogger.CommonItemChange gmDelEquip(int equipID, int count)
		{
			TLogger.CommonItemChange record = null;
			short delCount = 0;
			short afterCount = 0;
			SBean.DBRoleEquip t = null;
			int index = 0;
			for(SBean.DBRoleEquip e : equips)
			{
				if( e.id == equipID )
				{
					t = e;
					break;
				}
				index++;
			}
			if( t != null )
			{
				if (count < 0 || count >= t.count)
				{
					equips.remove(index);
					delCount = t.count;
					afterCount = 0;
				}
				else
				{
					t.count = (short)(t.count - count);
					delCount = (short)count;
					afterCount = t.count;
				}
				record = new TLogger.CommonItemChange(GameData.COMMON_TYPE_EQUIP, equipID, -delCount, afterCount);
			}
			return record;
		}
		
	//gm传入count做uint来理解
	public TLogger.CommonItemChange gmAddEquip(int equipID, int count)
	{
		if( count == 0 )
			return null;
		SBean.DBRoleEquip t = null;
		for(SBean.DBRoleEquip e : equips)
		{
			if( e.id == equipID )
			{
				t = e;
				break;
			}
		}
		int nowCount = 0;
		if (t != null)
			nowCount = t.count;
		long newCount = nowCount + count;
		if (count < 0 || newCount >= GameData.MAX_EQUIP_COUNT)
			newCount = GameData.MAX_EQUIP_COUNT;
		if (t != null)
			t.count = (short)newCount;
		else
			equips.add(new SBean.DBRoleEquip((short)equipID, (short)newCount));
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_EQUIP, equipID, (int)(newCount-nowCount), (int)newCount);
	}
	
	public List<TLogger.CommonItemChange> gmModifyExp(int changeExp, int nowTime, TLogger.TLogEvent logEvent, GameServer gs)
	{
		List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		if (changeExp > 0)
		{
			//if( lvl < gs.getRoleLevelLimit() )
			int levellimit = gs.getRoleLevelLimit();
			if ( lvl < levellimit )
			{
				exp += changeExp;
				while( exp >= GameData.getInstance().getUpgradeExp(lvl) )
				{
					exp -= GameData.getInstance().getUpgradeExp(lvl);
					levelUp(nowTime, logEvent);
					//if( lvl >= gs.getRoleLevelLimit() )
					if ( lvl >= levellimit )
					{
						exp = 0;
						break;
					}
				}
				records.add(new TLogger.CommonItemChange(GameData.COMMON_TYPE_EXP, lvl, changeExp, exp));
			}
		}
		else
		{
			if (exp < -changeExp)
				changeExp = -exp;
			exp = exp - changeExp;
			records.add(new TLogger.CommonItemChange(GameData.COMMON_TYPE_EXP, lvl, changeExp, exp));
		}
		return records;
	}
	
	private void levelUp(int nowTime, TLogger.TLogEvent logEvent)
	{
		//List<TLogger.CommonItemChange> records = new ArrayList<TLogger.CommonItemChange>();
		++lvl;
		TLogger.LevelUpRecord lur = new TLogger.LevelUpRecord(lvl);
		lur.setLastLevelUpTime(upgradeTime);
		upgradeTime = nowTime;
		lur.setCurLevelUpTime(upgradeTime);
		if (logEvent != null)
			logEvent.addRecord(lur);
		TLogger.CommonItemChange vitRecord = addVit2(GameData.getInstance().getRoleCmnCFG().vitAddLvlup.get(lvl-2));
		if (vitRecord != null)
		{
			TLogger.CommonFlowRecord record = new TLogger.CommonFlowRecord();
			record.addChange(vitRecord);
			record.setEvent(TLog.AT_LEVEL_UP);
			if (logEvent != null)
				logEvent.addCommonFlow(record);
		}
		//return records;
	}
	
	public TLogger.CommonItemChange gmModifyVit(int changeVit, int nowTime)
	{
		short max1 = GameData.getInstance().getVitMax(lvl);
		short max2 = GameData.getInstance().getRoleCmnCFG().vitMax;
		int beforeVit = vit;
		int endvit = vit + changeVit;
		if( endvit >= max2 )
		{
			vit = max2;
		}
		else if( endvit < 0 )
		{
			vit = 0;
		}
		else
		{
			vit = (short)endvit;
		}
		if( vit >= max1 )
		{
			vitRecoverStart = 0;
		}
		else
		{
			vitRecoverStart = nowTime;
		}
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 2, vit-beforeVit, vit);
	}
	
	public TLogger.CommonItemChange gmClearVit(int nowTime)
	{
		int beforeVit = vit;
		vit = 0;
		vitRecoverStart = nowTime;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_VIT, 2, vit-beforeVit, vit);
	}
	
	public TLogger.CommonItemChange gmModifyStone(int changeStone)
	{
		int beforeStone = stone;
		long endStone = stone + changeStone;
		if (endStone > GameData.MAX_STONE)
			stone = GameData.MAX_STONE;
		else if (endStone < 0)
			stone = 0;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 2, stone-beforeStone, stone);
	}
	
	public TLogger.CommonItemChange gmClearStone()
	{
		int beforeStone = stone;
		stone = 0;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_STONE, 2, stone-beforeStone, stone);
	}
	
	public TLogger.CommonItemChange gmModifyMoney(int changeMoney)
	{
		int beforeMoney = money;
		long endMoney = money + changeMoney;
		if (endMoney > GameData.MAX_MONEY)
			money = GameData.MAX_MONEY;
		else if (money < 0)
			money = 0;
		else
			money = (int)endMoney;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MONEY, 2, money-beforeMoney, money);
	}
	
	public TLogger.CommonItemChange gmClearMoney()
	{
		int beforeMoney = money;
		money = 0;
		return new TLogger.CommonItemChange(GameData.COMMON_TYPE_MONEY, 2, money-beforeMoney, money);
	}
	
	public int gmSetGeneralLevel(int gid, int level)
	{
		for (DBRoleGeneral g : generals)
		{
			if (g.id == gid)
			{
				if (g.lvl > level)
					return -2;
				else if (g.lvl == level)
					return 0;
				if (level > this.lvl)
					level = this.lvl;
				g.lvl = (short)level;
				g.exp = 0;
				return 0;
			}
		}
		return -1;
	}
	
	public int gmSetPetLevel(int pid, int level)
	{
		for (DBPet p : pets)
		{
			if (p.id == pid)
			{
				if (p.lvl > level)
					return -2;
				else if (p.lvl == level)
					return 0;
				if (level > this.lvl)
					level = this.lvl;
				p.lvl = (short)level;
				p.exp = 0;
				return 0;
			}
		}
		return -1;
	}

	public byte updateVipLevel(int curTime)
	{
		byte vipLvl = GameData.getInstance().getVIPLevel(getStonePayTotal());
		byte oldlvlVIP = this.lvlVIP;
		this.lvlVIP = vipLvl;
		if (vipLvl > oldlvlVIP)
		{
			if( vitRecoverStart == 0 && vit < GameData.getInstance().getVitMax(this.lvl) )
				vitRecoverStart = curTime;
			if( skillPointRecoverStart == 0 && skillPoint < GameData.getInstance().getSkillPointMax(this.lvlVIP) )
				skillPointRecoverStart = curTime;
		}
		return (byte)(vipLvl-oldlvlVIP);
	}
	
	public int onSetInvitationFriend(int gsid, int roleid, String rolename, int level, int vip, short headicon, int syncTime, int lastLogin)
	{
		if (invitations.isEmpty())
			return -3;
		SBean.DBInvitation invitation = invitations.get(0);
		for (SBean.DBInvitationFriend e : invitation.friends)
		{
			if (e.gsid == gsid && e.roleid == e.roleid)
			{
				if (e.level < level)
					e.level = level;
				if (e.vip < vip)
					e.vip = vip;
				e.icon = headicon;
				e.lastSyncTime = syncTime;
				e.lastLoginTime = lastLogin;
				return -5;
			}
		}
		invitation.friends.add(new SBean.DBInvitationFriend(gsid, roleid, rolename, level, vip, headicon, syncTime, lastLogin));
		return 0;
	}
	
	public List<SBean.DBGeneralStone> getActiveGeneralStones()
	{
		List<SBean.DBGeneralStone> rel = new ArrayList<SBean.DBGeneralStone>();
		if(generalStoneInfos==null||generalStoneInfos.size()==0){
			return rel;
		}
		if(generalStoneInfos.get(0)==null){
			return rel;
		}
		for (SBean.DBGeneralStone r : generalStoneInfos.get(0).generalStone) {
			if(r.gid<=0){
				continue;
			}
			rel.add(r.kdClone());
		}
		return rel;
	}
	
	public boolean activityRefresh(int newDay)
	{
		if( newDay != dayCommon )
		{
			return true;
		}
		return false;
	}
	
	public List<SBean.DBGeneralStone> getActiveGeneralStones(int gid)
	{
		List<SBean.DBGeneralStone> rel = new ArrayList<SBean.DBGeneralStone>();
		if(generalStoneInfos==null||generalStoneInfos.size()==0){
			return rel;
		}
		if(generalStoneInfos.get(0)==null){
			return rel;
		}
		for (SBean.DBGeneralStone r : generalStoneInfos.get(0).generalStone) {
			if(r.gid!=gid){
				continue;
			}
			rel.add(r.kdClone());
		}
		return rel;
	}
	
	public void setHeroesBossInfo(){
		
		if(heroesBossInfo!=null&&heroesBossInfo.size()>0
				){
			DBHeroesBossInfo dbHeroesBossInfotemp = heroesBossInfo.get(0);
			if(dbHeroesBossInfotemp!=null){
				if(dbHeroesBossInfotemp.times!=null)
					dbHeroesBossInfotemp.times.clear();
					dbHeroesBossInfotemp.buffCount=0;
					if(dbHeroesBossInfotemp.ranks!=null)
					dbHeroesBossInfotemp.ranks.clear();
					dbHeroesBossInfotemp.score1=0;
					dbHeroesBossInfotemp.score2=0;
					dbHeroesBossInfotemp.score3=0;
			}
			
		}
	}
		
	public List<SBean.DBGeneralStone> getGStones()
	{
		List<SBean.DBGeneralStone> rel = new ArrayList<SBean.DBGeneralStone>();
		if(generalStoneInfos==null||generalStoneInfos.size()==0){
			return rel;
		}
		if(generalStoneInfos.get(0)==null){
			return rel;
		}
		for (SBean.DBGeneralStone r : generalStoneInfos.get(0).generalStone) {
			rel.add(r.kdClone());
		}
		return rel;
	}
	
	public int getRoleGeneralSeyenTotal()
	{
		if(generalStoneInfos==null||generalStoneInfos.size()==0){
			return 0;
		}
		if(generalStoneInfos.get(0)==null){
			return 0;
		}
		List<DBRoleSeyen> generalSeyen = generalStoneInfos.get(0).generalSeyen;
		if(generalSeyen==null||generalSeyen.size()==0){
			return 0;
		}
		DBRoleSeyen m =generalSeyen.get(0);
		if(m==null){
			return 0;
		}
		return m.seyenTotal;
	}
	
	public SBean.DBInvitationFriend getSelfInvitationInfo(int gsid, int curTime)
	{
		SBean.DBInvitationFriend info = new SBean.DBInvitationFriend(gsid, this.id, this.name, this.lvl, this.lvlVIP, this.headIconID, curTime, this.lastLogoutTime);
		return info;
	}
/////////////////////////////////////////////////////////
	public boolean isOpenBaseCity()
	{
		return !citys.baseCitys.isEmpty();
	}
	
	public List<SBean.DBRelation> getActiveRelations()
	{
		List<SBean.DBRelation> rel = new ArrayList<SBean.DBRelation>();
		for (SBean.DBRelation r : relations) {
			boolean activated = true;
			for (short lvl : r.lvls)
				if (lvl <= 0)
					activated = false;
			
			if (activated)
				rel.add(r.kdClone());
		}
		return rel;
	}
	
	
	public int id;
	public int dayCommon;
	public int dayFlag;
	public int helpFlag1;
	public String name;
	public String userName;
	public int money;
	public int stone;
	public short lvlDay;
	public int exp;
	public short lvl;
	public short headIconID;
	public int flag;
	public short vit;
	public int vitRecoverStart;
	public short skillPoint;
	public int skillPointRecoverStart;
	public int createTime;
	public int lastLoginTime;
	public int lastLogoutTime;	
	public SBean.DBCheckinLog checkinLog = new SBean.DBCheckinLog();
	public int upgradeTime;
	public List<DBTaskPos> taskPos = new ArrayList<DBTaskPos>();
	public List<DBRoleGeneral> generals;
	public List<DBPet> pets;
	public List<Short> petsTop;
	public List<SBean.DBFeedingPet> petsFeeding;
	public List<DBRoleItem> items;
	public List<DBItemDropLog> itemDropLogs;
	public List<DBRoleEquip> equips;
	public List<DBCombatLog> combatFightLogs;
	public List<DBCombatScore> combatScores;
	public List<DBCombatPos> combatPos;
	public List<DBEggLog> eggLogs;
	public List<DBCombatEventLog> combatEventLogs;
	public DBShop shopNormal;
	public DBShop shopArena;
	public DBShop shopMarch;
	public DBShop shopRand1;
	public DBShop shopRand2;
	
	public int forceID;
	public int forcePoint;
	public int forceContrib;
	public int forceJoinTime;
	public String forceName;
	public List<Integer> forceMobai = new ArrayList<Integer>();
	public int forceMobaiReward;
	public short forceSuperMobai;
	public short forceIconId;
	
	public int stonePayMidas;
	public int stoneUsedTotal;
	public byte lvlVIP;
	public List<Byte> vipRewards;
	
	public List<DBCombatLog> combatResetLogs;
	
	public short vitUsedToday;
	
	public SBean.DBRoleArenaState arenaState;
	public List<SBean.DBRoleArenaLog> arenaLogs;
	public List<Short> arenaGenerals;
	public List<Short> arenaPets;

	public SBean.DBRoleMarchState marchState;
	public List<SBean.DBRoleMarchGeneralState> marchGeneralStates;
	public List<SBean.DBRoleMarchGeneralState> marchEnemyGeneralStates;
	public List<SBean.DBRoleMarchEnemy> marchCurrentEnemies;

	public List<Short> cdkeys = new ArrayList<Short>();
	public List<Short> worldGifts = new ArrayList<Short>();
	
	public List<SBean.DBDailyTask2Log> dailyTask2Logs;
	public int dailyActivity;
	public List<Byte> dailyActivityRewards;
	public int monthlyCardStartTime;
	public List<Byte> payLvlState = new ArrayList<Byte>();
	
	public short buyMoneyTimes;
	public short buyVitTimes;
	public short buySkillPointTimes;
	
	
	public List<PayLog> payLogs;
	public List<Integer> loginGiftMail = new ArrayList<Integer>();
	public List<SBean.DBRoleCheckinGift> checkinGift = new ArrayList<SBean.DBRoleCheckinGift>();
	public List<SBean.DBRoleFirstPayGift> firstPayGift = new ArrayList<SBean.DBRoleFirstPayGift>();
	public byte boughtMoney;
	public List<SBean.DBSoulBoxLog> soulBoxLogs;
	
	public List<SBean.DBBanData> banData;
	
	public int arenaBestRank;
	public int feedTotal;
	public int stonePayAlpha;
	
	public SBean.DBCitys citys;
	
	public int activity;
	public List<Byte> activityRewards;

	public List<SBean.DBRoleConsumeGift> consumeGift = new ArrayList<SBean.DBRoleConsumeGift>();
	public List<SBean.DBRoleConsumeRebate> consumeRebate = new ArrayList<SBean.DBRoleConsumeRebate>();
	public List<SBean.DBRoleUpgradeGift> upgradeGift = new ArrayList<SBean.DBRoleUpgradeGift>();
	public List<SBean.DBRoleGatherGift> gatherGift = new ArrayList<SBean.DBRoleGatherGift>();
	
	public int helpFlag2;
	public String openID;
	public List<DBShop> shopForce = new ArrayList<DBShop>();
	public List<SBean.DBRolePayGift> payGift = new ArrayList<SBean.DBRolePayGift>();
	public List<SBean.DBRoleDiskGift> diskGift = new ArrayList<SBean.DBRoleDiskGift>();
	public List<SBean.DBRoleRechargeGift> rechargeGift = new ArrayList<SBean.DBRoleRechargeGift>();
	public List<SBean.DBRoleLimitedShop> limitedShop = new ArrayList<SBean.DBRoleLimitedShop>();
	public List<SBean.DBRoleLoginGift> loginGift = new ArrayList<SBean.DBRoleLoginGift>();
	
	public List<SBean.DBRichRole> rich;
	
	public byte marchPointReward;
	public List<DBShop> shopRich = new ArrayList<DBShop>();
	
	public int lastForceWarID;
	public int marchPointDay;
	public List<SBean.DBInvitation> invitations = new ArrayList<SBean.DBInvitation>();
	public byte qqVipRewards;
	
	public List<SBean.DBRoleTradingCenter> tradingCenter = new ArrayList<SBean.DBRoleTradingCenter>();
	
	public List<SBean.DBRoleSuperArena> superArena = new ArrayList<SBean.DBRoleSuperArena>();
	//public int richMovementRecoverStart;
	public List<SBean.DBPetDeform> petDeforms;

	public List<SBean.DBRoleDuel> duel = new ArrayList<SBean.DBRoleDuel>();
	public List<SBean.DBRoleSura> sura = new ArrayList<SBean.DBRoleSura>();
	public List<DBShop> shopSuperArena = new ArrayList<DBShop>();

	public List<SBean.DBRelation> relations = new ArrayList<SBean.DBRelation>();
	public List<SBean.DBMarchMercGeneral> mercGenerals = new ArrayList<SBean.DBMarchMercGeneral>();
	
	public List<SBean.DBGeneralStoneInfo> generalStoneInfos = new ArrayList<SBean.DBGeneralStoneInfo>();
	public List<DBShop> shopDuel = new ArrayList<DBShop>();
	public List<SBean.DBExpiratBossInfo> expiratBossInfo = new ArrayList<SBean.DBExpiratBossInfo>();
	
	public List<SBean.DBOptionalHotPoint> optionalHotPoint = new ArrayList<SBean.DBOptionalHotPoint>();
	public byte relationStone;
	public List<SBean.DBHeroesBossInfo> heroesBossInfo = new ArrayList<SBean.DBHeroesBossInfo>();//public byte padding104;
	
	// Version 2
	public byte vipShow;

	public List<SBean.DBDiskBetInfo> diskBet = new ArrayList<SBean.DBDiskBetInfo>();//public DBDiskBetInfo diskBet1 = new DBDiskBetInfo();
	public List<SBean.DBDiskBetInfo> diskBet2 = new ArrayList<SBean.DBDiskBetInfo>();
	public List<SBean.DBDiskBetInfo> diskBet3 = new ArrayList<SBean.DBDiskBetInfo>();
	
	public List<DBBlessInfo> bless = new ArrayList<DBBlessInfo>();

	public List<SBean.DBTreasureLimitCnt> treasureLimitCnt = new ArrayList<SBean.DBTreasureLimitCnt>();
//	public byte padding26;

	public List<SBean.DBHeadIcon> headicon = new ArrayList<SBean.DBHeadIcon>();
	public List<DBShop> shopBless = new ArrayList<DBShop>();
	
	public byte padding32;
	public byte padding33;
	public byte padding34;
	public int padding4;
	public int padding5;
	public int padding6;
	public int padding7;
	public int padding8;
	public int padding9;
	
}

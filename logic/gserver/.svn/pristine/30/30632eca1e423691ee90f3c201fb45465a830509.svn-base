
package i3k;

import i3k.SBean.DBBeMonsterAttacker;
import i3k.SBean.DBBeMonsterBoss;
import i3k.SBean.DBBeMonsterLineup;
import i3k.SBean.DBBestowLevel;
import i3k.SBean.DBDiskBet;
import i3k.SBean.DBDiskBetInfo;
import i3k.SBean.DBDiskBetRank;
import i3k.SBean.DBExpiratBossTimes;
import i3k.SBean.DBGeneralBestow;
import i3k.SBean.DBGeneralBless;
import i3k.SBean.DBGeneralSeyen;
import i3k.SBean.DBGeneralStone;
import i3k.SBean.DBGeneralStoneProp;
import i3k.SBean.DBHeadIcon;
import i3k.SBean.DBHeadIconList;
import i3k.SBean.DBHeroesBoss;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBHeroesBossTimes;
import i3k.SBean.DBPetBrief;
import i3k.SBean.DBRoleDiskGift;
import i3k.SBean.DBRoleItem2;
import i3k.SBean.DBRoleRechargeGift;
import i3k.SBean.DBTreasureMap;
import i3k.SBean.DBTreaureReward;
import i3k.SBean.DiskBetItemsCFGS;
import i3k.SBean.DropEntry;
import i3k.SBean.DropEntryNew;
import i3k.SBean.DropTableEntryNew;
import i3k.SBean.HeroesRank;
import i3k.global.SocialManager;
import i3k.gs.ArenaManager;
import i3k.gs.BeMonsterManager;
import i3k.gs.CityManager;
import i3k.gs.DuelManager;
import i3k.gs.DuelManager.RTMatchData;
import i3k.gs.ExpiratBossManager;
import i3k.gs.ForceManager;
import i3k.gs.ForceSWarManager;
import i3k.gs.ForceWarManager;
import i3k.gs.GameActivities;
import i3k.gs.GameActivities.ActivityDisplayConfig;
import i3k.gs.GameActivities.DiskBetConfig;
import i3k.gs.GameActivities.DiskGiftConfig;
import i3k.gs.GameActivities.DiskLvlGift;
import i3k.gs.GameActivities.RechargeGiftConfig;
import i3k.gs.GameActivities.RechargeLvlGift;
import i3k.gs.GameActivities.RechargeRankConfig;
import i3k.gs.GameData;
import i3k.gs.GameServer;
import i3k.gs.LoginManager;
import i3k.gs.MarchManager;
import i3k.gs.RichManager;
import i3k.gs.Role;
import i3k.gs.SuraManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LuaPacket
{
		
	public static String encodeRoleBriefSync(SBean.RoleBrief b)
	{
		StringBuilder sb = new StringBuilder(";role;syncbrief;");
		
		sb.append(b.id).append(';');
		sb.append(b.day).append(';');
		sb.append(b.name).append(';');
		sb.append(b.money).append(';');
		sb.append(b.yuanbao).append(';');
		sb.append(b.exp).append(';');
		sb.append(b.vit).append(';');
		sb.append(b.lvl).append(';');
		sb.append(b.allscore).append(';');
		sb.append(b.headIconID).append(';');
		sb.append(b.buyMoneyTimes).append(';');
		sb.append(b.buyVitTimes).append(';');
		sb.append(b.buySkillPointTimes).append(';');
		sb.append(b.vitRecoverStart).append(';');
		sb.append(b.skillPoint).append(';');
		sb.append(b.skillPointRecoverStart).append(';');
		sb.append(b.timeSync).append(';');
		for(int i = 0; i < 32; ++i)
			sb.append(((b.flag&(1<<i))!=0)?1:0).append(';');
		for(int i = 0; i < 32; ++i)
			sb.append(((b.helpFlag1&(1<<i))!=0)?1:0).append(';');
		for(int i = 0; i < 32; ++i)
			sb.append(((b.helpFlag2&(1<<i))!=0)?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleTimeSync(int now)
	{
		StringBuilder sb = new StringBuilder(";role;synctime;");
		sb.append(now).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleLevelLimit(short limit,int seyen)
	{
		StringBuilder sb = new StringBuilder(";role;levellimit;");
		sb.append(limit).append(';');
		sb.append(seyen).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleSeyen(int seyen)
	{
		StringBuilder sb = new StringBuilder(";generals;syncSeyen;");
		sb.append(seyen).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleBuyMoneyRes(List<Integer> critMultipliers)
	{
		StringBuilder sb = new StringBuilder(";role;buymoney;");
		if (critMultipliers == null || critMultipliers.isEmpty())
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(critMultipliers.size()).append(';');
			for (int cm : critMultipliers)
			{
				sb.append(cm).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeRoleBuyVitRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";role;buyvit;");
		
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleBuySkillPointRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";role;buyskillpoint;");
		
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleSetHeadIconRes(short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";role;setheadicon;");
		
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRoleBriefUpdate(String type, int val)
	{
		StringBuilder sb = new StringBuilder(";role;updatebrief;");
		
		sb.append(type).append(';');
		sb.append(val).append(';');
		
		return sb.toString();
	}
	
	public static String encodeRandomRoles(byte reason, List<SBean.DBRoleBrief> lst)
	{
		StringBuilder sb = new StringBuilder(";randomroles;sync;");		
		sb.append(reason).append(';');
		sb.append(lst.size()).append(';');
		for(SBean.DBRoleBrief e : lst)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.name).append(';');
			sb.append(e.fname).append(';');
			if( reason == GameData.RANDOMROLES_PVP )
			{
				for(int i = 0; i < 2; ++i)
					sb.append(((e.flag&(1<<i))!=0)?1:0).append(';');
			}
			else
			{
				sb.append(e.flag).append(';');	
			}
		}
		return sb.toString();
	}
	
	public static String encodeCombatDataSync(List<SBean.DBCombatLog> combatFightLogs, List<SBean.DBCombatLog> combatResetLogs, List<SBean.DBCombatPos> combatPos)
	{
		StringBuilder sb = new StringBuilder(";combats;sync;");
		
		sb.append(combatFightLogs.size()).append(';');
		for(SBean.DBCombatLog e : combatFightLogs)
		{
			sb.append(e.id).append(';');
			sb.append(e.times).append(';');
		}
		
		sb.append(combatResetLogs.size()).append(';');
		for(SBean.DBCombatLog e : combatResetLogs)
		{
			sb.append(e.id).append(';');
			sb.append(e.times).append(';');
		}
		
		sb.append(combatPos.size()).append(';');
		for(SBean.DBCombatPos e : combatPos)
		{
			sb.append(e.catIndex).append(';');
			sb.append(e.combatIndex).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsSync(List<DBRoleGeneral> generals)
	{
		StringBuilder sb = new StringBuilder(";generals;sync;");		
		sb.append(generals.size()).append(';');
		for(DBRoleGeneral e : generals)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.exp).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
			{
				sb.append(e.skills.get(i)).append(';');
			}
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
			{
				sb.append(e.equips.get(i).lvl).append(';');
				sb.append(e.equips.get(i).exp).append(';');
			}
			sb.append(e.weapon.lvl).append(';');
			if (e.weapon.lvl >= 0) {
				sb.append(e.weapon.evo).append(';');
				sb.append(e.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : e.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.rank).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, e);
			encodeGeneralOfficial(sb, e);
//			encodeGStones(sb, e.generalStone);
		}
		return sb.toString();
	}

	private static void encodeGeneralSeyen(StringBuilder sb, DBRoleGeneral e) {
		if(e.generalSeyen!=null&&e.generalSeyen.size()>0){
			DBGeneralSeyen sey =e.generalSeyen.get(0);
			sb.append(sey.lvl).append(';');
			sb.append(sey.exp).append(';');
			sb.append(sey.seyenTotal).append(';');
		}else{
			sb.append(0).append(';');
			sb.append(0).append(';');
			sb.append(0).append(';');
		}
		
	}
		
	private static void encodeGeneralOfficial(StringBuilder sb, DBRoleGeneral e) {
		sb.append(e.headicon).append(';');
		if(e.bless!=null&&e.bless.size()>0){
			DBGeneralBless sey =e.bless.get(0);
			sb.append(sey.p1).append(';');
			sb.append(sey.p2).append(';');
			sb.append(sey.p3).append(';');
			sb.append(sey.state).append(';');
			
		}else{
			sb.append(0).append(';');
			sb.append(0).append(';');
			sb.append(0).append(';');
			sb.append(0).append(';');
		}
		if(e.bestow!=null&&e.bestow.size()>0){
			DBGeneralBestow sey =e.bestow.get(0);
			sb.append(sey.lvl).append(';');
			sb.append(sey.bestowLevel.size()).append(';');
			for(DBBestowLevel lvl :sey.bestowLevel){
				sb.append(lvl.gid).append(';');
				sb.append(lvl.lvl).append(';');
			}
			
		}else{
			sb.append(0).append(';');
			sb.append(0).append(';');
//			sb.append(0).append(';');
		}
		if(e.official!=null&&e.official.size()>0){
			SBean.DBGeneralOfficial off =e.official.get(0);
			sb.append(off.lvl+1).append(';');
			sb.append(off.exp).append(';');
			sb.append(off.skillLvl).append(';');
		}else{
			sb.append(1).append(';');
			sb.append(0).append(';');
			sb.append(0).append(';');
		}
	}
		
	public static String encodePetsSync(List<SBean.DBPet> pets, List<SBean.DBFeedingPet> petsFeeding, short petsTop, int feedTotal, List<SBean.DBPetDeform> deforms, int activity, byte takeZanTimes)
	{
		StringBuilder sb = new StringBuilder(";pets;sync;");
		sb.append(petsTop).append(';');
		sb.append(feedTotal).append(';');
		sb.append(pets.size()).append(';');
		for(SBean.DBPet e : pets)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.exp).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.name).append(';');
			sb.append(e.lastBreedTime).append(';');
			sb.append(e.hungryDay).append(';');
			sb.append(e.hungryTimes).append(';');
			sb.append(e.feedTime).append(';');
			sb.append(e.feedGain).append(';');
			sb.append(e.feedMode).append(';');
			sb.append(e.awakeLvl).append(";");
			if (e.feedMode == 1) {
				sb.append(e.feedNeed.type).append(';');
				sb.append(e.feedNeed.id).append(';');
				sb.append(e.feedNeed.arg).append(';');
			}
		}
		sb.append(petsFeeding.size()).append(';');
		for(SBean.DBFeedingPet e : petsFeeding)
		{
			sb.append(e.pid).append(';');
			sb.append(e.lvlStart).append(';');
			sb.append(e.timeStart).append(';');
		}
		sb.append(activity).append(';');
		sb.append(takeZanTimes).append(';');
		sb.append(deforms.size()).append(';');
		for(SBean.DBPetDeform e : deforms)
		{
			sb.append(e.id).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.happiness).append(';');
			sb.append(e.growth).append(';');
			sb.append(e.feedTime).append(';');
			sb.append(e.feedTimes).append(';');
			sb.append(e.zanTimes).append(';');
			sb.append(e.zanOfferTime).append(';');
			sb.append(e.tryTimes).append(';');
			sb.append(e.buyTryTimes).append(';');
			sb.append(e.drops.size()).append(';');
			for (SBean.DropEntry d : e.drops) {
				sb.append(d.type).append(';');
				sb.append(d.id).append(';');
				sb.append(d.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeTaskPosSync(List<SBean.DBTaskPos> taskPos)
	{
		StringBuilder sb = new StringBuilder(";task;sync;");		
		sb.append(taskPos.size()).append(';');
		for(SBean.DBTaskPos e : taskPos)
		{
			sb.append(e.groupID).append(';');
			sb.append(e.seqComp).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeTaskFinishRes(short gid, short seq, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";task;finish;");		
		sb.append(gid).append(';');
		sb.append(seq).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeClearFunctionCool(byte fid, short s, boolean res)
	{
		StringBuilder sb = new StringBuilder(";function;clearcool;");		
		sb.append(fid).append(';');
		sb.append(s).append(';');
		sb.append(res?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeCityUpgradeRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";city;upgrade;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodePalaceNeizhengSync(List<Short> lst)
	{
		StringBuilder sb = new StringBuilder(";palace;syncneizheng;");
		sb.append(lst.size()).append(';');
		for(short e : lst)
		{
			sb.append(e).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeMarketGoodsSellRes(byte slot, byte id, byte cnt, i3k.gs.Role.SellMarketGoodsRes res)
	{
		StringBuilder sb = new StringBuilder(";market;sell;");
		sb.append(slot).append(';');
		sb.append(id).append(';');
		sb.append(cnt).append(';');
		sb.append(res.bOK?1:0).append(';');
		sb.append(res.price).append(';');
		sb.append(res.taxAll).append(';');
		sb.append(res.bLost?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeMarketGoodsBuyRes(byte id, byte cnt, i3k.gs.Role.BuyMarketGoodsRes res)
	{
		StringBuilder sb = new StringBuilder(";market;buy;");
		sb.append(id).append(';');
		sb.append(cnt).append(';');
		sb.append(res.bOK?1:0).append(';');
		sb.append(res.price).append(';');
		return sb.toString();
	}
	
	public static String encodePrisonOPRes(byte fid, short gid, i3k.gs.Role.PrisonOPRes res)
	{
		StringBuilder sb = new StringBuilder(";prison;opres;");		
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(res.res).append(';');
		sb.append(res.sur?1:0).append(';');
		sb.append(res.jiecao).append(';');
		sb.append(res.jiecao2).append(';');
		sb.append(res.jiecao3).append(';');
		return sb.toString();
	}
	
	public static String encodeFighterMoveRes(String op, byte curSel, boolean res)
	{
		StringBuilder sb = new StringBuilder(";fighters;" + op + ";");		
		sb.append(curSel).append(';');
		sb.append(res?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFighterSetRes(String op, byte curSel, short gid, boolean res)
	{
		StringBuilder sb = new StringBuilder(";fighters;" + op + ";");		
		sb.append(curSel).append(';');
		sb.append(gid).append(';');
		sb.append(res?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeCombatScore(byte ctype, byte cindex, SBean.DBCombatTypeScore ts)
	{
		StringBuilder sb = new StringBuilder(";combats;syncscore;");
		sb.append(ctype).append(';');
		sb.append(cindex).append(';');
		sb.append(ts.catID).append(';');
		sb.append(ts.flag).append(';');
		sb.append(ts.score.size()).append(';');
		for(byte e : ts.score)
		{
			sb.append(e).append(';');
		}
		return sb.toString();
	}
	
	private static void append(StringBuilder sb, SBean.DBFriendData e)
	{
	    sb.append(e.openID).append(';');
        sb.append(e.roleID.serverID).append(';');
        sb.append(e.roleID.roleID).append(';');
        //TODO
        sb.append(e.prop.lvl).append(';');
        sb.append(e.prop.flag).append(';'); // qq vip now
        sb.append(((e.flag&DBFriend.FLAG_LOCAL)!=0)?1:0).append(';');
        sb.append(e.prop.name).append(';');
        sb.append(e.prop.fname).append(';');
        sb.append(e.prop.headIconID).append(';');
        sb.append(e.prop.lastLoginTime).append(';');
        
        sb.append(e.prop.arenaGenerals.size()).append(';');
        for(DBRoleGeneral g : e.prop.arenaGenerals)
        {
            sb.append(g.id).append(';');
            sb.append(g.lvl).append(';');
            sb.append(g.exp).append(';');
            sb.append(g.advLvl).append(';');
            sb.append(g.evoLvl).append(';');
            for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
            {
                sb.append(g.skills.get(i)).append(';');
            }
            for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
            {
                sb.append(g.equips.get(i).lvl).append(';');
                sb.append(g.equips.get(i).exp).append(';');
            }
            sb.append(g.weapon.lvl).append(';');
            if (g.weapon.lvl >= 0) {
                sb.append(g.weapon.evo).append(';');
                sb.append(g.weapon.passes.size()).append(';');
                for (SBean.DBWeaponProp prop : g.weapon.passes) {
                    sb.append(prop.propId).append(';');                 
                    sb.append(prop.type).append(';');                   
                    sb.append(prop.value).append(';');                  
                    sb.append(prop.rank).append(';');                   
                }
            }
            encodeGeneralSeyen(sb, g);
            encodeGeneralOfficial(sb, g);
        }
        sb.append(e.prop.arenaPets.size()).append(';');
        for(SBean.DBPetBrief g : e.prop.arenaPets)
        {
            sb.append(g.id).append(';');
            sb.append(g.lvl).append(';');
            sb.append(g.evoLvl).append(';');
            sb.append(g.growthRate).append(';');
            sb.append(g.name).append(';');
            sb.append(g.deformStage).append(';');
            sb.append(g.awakLvl).append(";");
        }
        
        sb.append(e.prop.pets.petsTop.size()).append(';');
        for(SBean.DBPetBrief p : e.prop.pets.petsTop)
        {
            sb.append(p.id).append(';');
            sb.append(p.lvl).append(';');
            sb.append(p.evoLvl).append(';');
            sb.append(p.growthRate).append(';');
            sb.append(p.name).append(';');
            sb.append(p.deformStage).append(';');
            sb.append(p.awakLvl).append(";");
        }
        encodeRelation(sb, e.prop.relation);
        encodeGStones(sb, e.prop.gStones);
	}
	
	public static String encodeFriendSync(List<SBean.DBFriendData> lst)
	{
		StringBuilder sb = new StringBuilder(";friend;");
		sb.append("sync;");
		sb.append(lst.size()).append(';');
		for(SBean.DBFriendData e : lst)
		{
            sb.append(e.openID).append(';');
            sb.append(e.roleID.serverID).append(';');
            sb.append(e.roleID.roleID).append(';');
            // TODO
            sb.append(e.prop.lvl).append(';');
            sb.append(e.prop.flag).append(';'); // qq vip now
            sb.append(((e.flag & DBFriend.FLAG_LOCAL) != 0) ? 1 : 0).append(';');
            sb.append(e.prop.name).append(';');
            sb.append(e.prop.fname).append(';');
            sb.append(e.prop.headIconID).append(';');
            sb.append(e.prop.lastLoginTime).append(';');

            int totalPower = 0;
            for (DBRoleGeneral g : e.prop.arenaGenerals) {
                totalPower += DBRole.calcGeneralPower(g);
            }
            for (SBean.DBPetBrief g : e.prop.arenaPets) {
                float appraise = 0;
                SBean.PetCFGS pcfg = GameData.getInstance().getPetCFG(g.id, g.deformStage >= 50);
                if ( pcfg != null ) {
                    appraise = pcfg.initPower + pcfg.upPower * (g.growthRate / 100.0f) * (g.lvl - 1);

                    SBean.PetEvoCFGS evoCfg = pcfg.evo.get(g.evoLvl - 1);
                    if ( evoCfg != null ) {
                        appraise += evoCfg.power;
                    }
                }

                totalPower += (int) (appraise + 0.5f);
            }
            sb.append(totalPower).append(";");

            sb.append(e.prop.pets.petsTop.size()).append(';');
            for (SBean.DBPetBrief p : e.prop.pets.petsTop) {
                sb.append(p.id).append(';');
                sb.append(p.lvl).append(';');
                sb.append(p.evoLvl).append(';');
                sb.append(p.growthRate).append(';');
                sb.append(p.name).append(';');
                sb.append(p.deformStage).append(';');
                sb.append(p.awakLvl).append(";");
            }
		}
		return sb.toString();
	}
	
	public static String encodeFriendNoSearchSync(Set<String> set)
	{
		StringBuilder sb = new StringBuilder(";friend;");
		sb.append("syncnosearch;");
		sb.append(set.size()).append(';');
		for(String e: set)
		{
			sb.append(e).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeFriendSyncStart(List<SBean.DBFriendData> lst)
	{
		StringBuilder sb = new StringBuilder(";friend;");
		sb.append("syncstart;");
		if( lst == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(lst.size()).append(';');
			for(SBean.DBFriendData e : lst)
			{
				append(sb, e);
			}
		}
		return sb.toString();
	}
	
	public static String encodeFriendSyncEnd()
	{
		StringBuilder sb = new StringBuilder(";friend;");
		sb.append("syncend;");
		sb.append(1).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendSyncUpdate(List<SBean.DBFriendData> lst)
	{
		StringBuilder sb = new StringBuilder(";friend;");
		sb.append("syncupdate;");
		sb.append(lst.size()).append(';');
		for(SBean.DBFriendData e : lst)
		{
			append(sb, e);
		}
		return sb.toString();
	}
	
	public static String encodeFriendSyncState(DBFriend dbfriend)
	{
		StringBuilder sb = new StringBuilder(";friend;syncstate;");
		sb.append(dbfriend.point).append(';');
		sb.append(dbfriend.pointToday).append(';');
		sb.append(GameData.getInstance().getFriendCFG().pointMaxPerDay).append(';');
		sb.append(dbfriend.baoziVitToday).append(';');
		sb.append(GameData.getInstance().getFriendCFG().vitBaoziMaxPerDay).append(';');
		sb.append(GameData.getInstance().getFriendCFG().pointPerBaozi).append(';');
		sb.append(GameData.getInstance().getFriendCFG().vitPerBaozi).append(';');
		sb.append(dbfriend.recallCount).append(';');
		sb.append(dbfriend.friends.size()).append(';');
		for(SBean.DBFriendData e : dbfriend.friends)
		{
			sb.append(e.openID).append(';');
			sb.append(e.roleID.serverID).append(';');
			sb.append(e.roleID.roleID).append(';');
			sb.append(e.haogan).append(';');
			//TODO
			sb.append(((e.flag&DBFriend.DAY_FLAG_BAOZI)!=0)?1:0).append(';');
			sb.append(((e.flag&DBFriend.DAY_FLAG_RECALL)!=0)?1:0).append(';');
			sb.append(((e.flag&DBFriend.DAY_FLAG_PET_EXP)!=0)?1:0).append(';');
			sb.append(((e.flag&DBFriend.DAY_FLAG_PET_BREED)!=0)?1:0).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeFriendSyncMessage(DBFriend dbfriend)
	{
		StringBuilder sb = new StringBuilder(";friend;syncmessage;");
		sb.append(dbfriend.msgs.size()).append(';');
		for(SBean.DBFriendMessage e : dbfriend.msgs)
		{
			sb.append(e.openID).append(';');
			sb.append(e.roleID.serverID).append(';');
			sb.append(e.roleID.roleID).append(';');
			sb.append(e.time).append(';');
			sb.append(e.type).append(';');
			sb.append(e.flag).append(';');
			sb.append(e.arg1).append(';');
			sb.append(e.arg2).append(';');
		}
		sb.append(dbfriend.applyLst.size()).append(';');
		for(SBean.DBFriendApply e : dbfriend.applyLst)
		{
			sb.append(e.roleID).append(';');
			sb.append(e.name).append(';');
			sb.append(e.fname).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.headIconID).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeFriendSyncHeart(short heart)
	{
		StringBuilder sb = new StringBuilder(";friend;syncheart;");
		sb.append(heart).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendMailBaoziRes(String openID, int serverID, int roleID, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;mailbaozi;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendMailAllBaoziRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;mailallbaozi;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendMailPetExpRes(String openID, int serverID, int roleID, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;mailpetexp;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendBatchMailPetExpRes(String openID, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;batchmailpetexp;");
		sb.append(openID).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendRecallRes(String openID, int serverID, int roleID, int money)
	{
		StringBuilder sb = new StringBuilder(";friend;recall;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(money).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendRecallAllRes(int money, List<String> openIDs)
	{
		StringBuilder sb = new StringBuilder(";friend;recallall;");
		sb.append(money).append(';');
		sb.append(openIDs.size()).append(';');
		for(String s : openIDs)
		{
			sb.append(s).append(';');	
		}
		return sb.toString();
	}
	
	public static String encodeFriendEatBaoziRes(String openID, int serverID, int roleID, int time, int vit)
	{
		StringBuilder sb = new StringBuilder(";friend;eatbaozi;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(time).append(';');
		sb.append(vit).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendTakeBreedRewardRes(String openID, int serverID, int roleID, int time, int money)
	{
		StringBuilder sb = new StringBuilder(";friend;takebreedreward;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(time).append(';');
		sb.append(money).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendTakePetExpRes(String openID, int serverID, int roleID, int time, int ret)
	{
		StringBuilder sb = new StringBuilder(";friend;takepetexp;");
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(time).append(';');
		sb.append(ret).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendApplyRes(int targetID, int ret)
	{
		StringBuilder sb = new StringBuilder(";friend;apply;");
		sb.append(targetID).append(';');
		sb.append(ret).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendAcceptRes(int applyID, int ret)
	{
		StringBuilder sb = new StringBuilder(";friend;accept;");
		sb.append(applyID).append(';');
		sb.append(ret).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendEatAllBaoziRes(boolean bOK, int vit, int money, List<SBean.DropEntry> items)
	{
		StringBuilder sb = new StringBuilder(";friend;eatallbaozi;");
		sb.append(bOK?1:0).append(';');
		sb.append(vit).append(';');
		sb.append(money).append(';');
		sb.append(items.size()).append(';');
		for(SBean.DropEntry e : items)
		{
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeFriendRefuseRes(int roleID, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;refuse;");
		sb.append(roleID).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendDelRes(int serverID, int roleID, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";friend;del;");
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendUndelAllRes(int count)
	{
		StringBuilder sb = new StringBuilder(";friend;undelall;");
		sb.append(count).append(';');
		return sb.toString();
	}
	
	public static String encodeFriendSyncDel(int serverID, int roleID)
	{
		StringBuilder sb = new StringBuilder(";friend;syncdel;");
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		return sb.toString();
	}	
	
	public static String encodeItemsSync(List<DBRoleItem2> items)
	{
		StringBuilder sb = new StringBuilder(";items;sync;");
		sb.append(items.size()).append(';');
		for(DBRoleItem2 e : items)
		{
			sb.append(e.id).append(';');
			sb.append(e.count).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeEquipsSync(List<SBean.DBRoleEquip> equips)
	{
		StringBuilder sb = new StringBuilder(";equips;sync;");
		sb.append(equips.size()).append(';');
		for(SBean.DBRoleEquip e : equips)
		{
			sb.append(e.id).append(';');
			sb.append(e.count).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeGlobalState(LoginManager.GlobalState s)
	{
		StringBuilder sb = new StringBuilder(";gstate;sync;");
		sb.append(s.jubaoDay).append(';');
		sb.append(s.tssAnti).append(';');
		sb.append(s.roleLevelLimit).append(';');
		return sb.toString();
	}
	
	public static String encodeItemsSellRes(short iid, int cnt, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";items;sell;");
		sb.append(iid).append(';');
		sb.append(cnt).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeItemsSellAllRes(int money)
	{
		StringBuilder sb = new StringBuilder(";items;sellall;");
		sb.append(money).append(';');
		return sb.toString();
	}
	
	public static String encodeItemsGambleRes(short iid, int icount, List<List<DropEntry>> drops1)
	{
		StringBuilder sb = new StringBuilder(";items;gamble;");
		sb.append(iid).append(';');
		sb.append(icount).append(';');
		if( drops1 == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(drops1.size()).append(';');
			int index =1;
			for(List<DropEntry> drops : drops1)
			{
				
				sb.append(index).append(';');
				sb.append(drops.size()).append(';');
				for(SBean.DropEntry d : drops)
				{
					sb.append(d.type).append(';');
					sb.append(d.id).append(';');
					sb.append(d.arg).append(';');
				}
				index++;
			}
			
		}
		return sb.toString();
	}
	
	public static String encodeItemsUseRes(short iid, short cnt, short gid, short rcnt, int[] dataRes)
	{
		StringBuilder sb = new StringBuilder(";items;use;");
		sb.append(iid).append(';');
		sb.append(cnt).append(';');
		sb.append(gid).append(';');
		sb.append(rcnt).append(';');
		sb.append(dataRes[0]).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsSetEquipRes(short gid, short eid, byte pos, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;setequip;");
		sb.append(gid).append(';');
		sb.append(eid).append(';');
		sb.append(pos).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsGildEquipRes(short gid, short eid, byte pos, List<SBean.DropEntryNew> matList, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;gildequip;");
		sb.append(gid).append(';');
		sb.append(eid).append(';');
		sb.append(pos).append(';');
		sb.append(matList.size()).append(';');
		for(SBean.DropEntryNew e : matList)
		{
			sb.append(e.type).append(';');
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsUpgradeAdvRes(short gid, boolean bOK, List<SBean.DropEntryNew> drops, int err)
	{
		StringBuilder sb = new StringBuilder(";generals;upgradeadv;");
		sb.append(gid).append(';');
		sb.append(bOK?1:err).append(';');
		if (bOK) {
			sb.append(drops.size()).append(';');
			for (SBean.DropEntryNew d : drops) {
				sb.append(d.type).append(';');
				sb.append(d.id).append(';');
				sb.append(d.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsUpgradeEvoRes(short gid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;upgradeevo;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodePetsUpgradeEvoRes(short pid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";pets;upgradeevo;");
		sb.append(pid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodePetsListBreed(short pid, List<SBean.FriendBreedPet> lst)
	{
		StringBuilder sb = new StringBuilder(";pets;listbreed;");
		sb.append(pid).append(';');
		if( lst == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(lst.size()).append(';');
			for(SBean.FriendBreedPet e : lst)
			{
				sb.append(e.openID).append(';');
				sb.append(e.roleID.serverID).append(';');
				sb.append(e.roleID.roleID).append(';');
				sb.append(e.pet.id).append(';');
				sb.append(e.pet.lvl).append(';');
				sb.append(e.pet.evoLvl).append(';');
				sb.append(e.pet.name).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodePetsBreedRes(short pid, String openID, int serverID, int roleID, short eggItemID)
	{
		StringBuilder sb = new StringBuilder(";pets;breed;");
		sb.append(pid).append(';');
		sb.append(openID).append(';');
		sb.append(serverID).append(';');
		sb.append(roleID).append(';');
		sb.append(eggItemID).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsSetTopRes(short pid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";pets;settop;");
		sb.append(pid).append(';');
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundUpgradeRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";pets;poundupgrade;");
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundPutRes(short pid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";pets;poundput;");
		sb.append(pid).append(';');
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundGetRes(short pid, int exp)
	{
		StringBuilder sb = new StringBuilder(";pets;poundget;");
		sb.append(pid).append(';');
		sb.append(exp>=0?1:0).append(';');
		sb.append(exp).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundHungryRes(short pid, SBean.DropEntry need)
	{
		StringBuilder sb = new StringBuilder(";pets;hungry;");
		sb.append(pid).append(';');
		if (need != null) {
			sb.append(1).append(';');
			sb.append(need.type).append(';');
			sb.append(need.id).append(';');
			sb.append(need.arg).append(';');
		}
		else
			sb.append(0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundFeedRes(short pid, int feedGain)
	{
		StringBuilder sb = new StringBuilder(";pets;feed;");
		sb.append(pid).append(';');
		sb.append(feedGain).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundProduceRes(short pid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";pets;produce;");
		sb.append(pid).append(';');
		sb.append(bOK?1:0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsPoundGetProductRes(short pid, SBean.DropEntry product)
	{
		StringBuilder sb = new StringBuilder(";pets;getproduct;");
		sb.append(pid).append(';');
		if (product != null) {
			sb.append(1).append(';');
			sb.append(product.type).append(';');
			sb.append(product.id).append(';');
			sb.append(product.arg).append(';');
		}
		else
			sb.append(0).append(';');
		
		return sb.toString();
	}
	
	public static String encodePetsChangeNameRes(short pid, String newName, byte result)
	{
		StringBuilder sb = new StringBuilder(";pets;changename;");
		sb.append(pid).append(';');
		sb.append(result).append(';');
		if (result == GameData.CHANGENAME_OK) {
			sb.append(newName).append(';');
		}
		
		return sb.toString();
	}
	
	public static String encodePetsDeformSyncRes(List<SBean.DBPetDeform> deforms, int activity, byte takeZanTimes, Map<Short, Byte> awakeLvlMap)
	{
		StringBuilder sb = new StringBuilder(";pets;deformsync;");
		sb.append(activity).append(';');
		sb.append(takeZanTimes).append(';');
		sb.append(deforms.size()).append(';');
		for(SBean.DBPetDeform e : deforms)
		{ 
			sb.append(e.id).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.happiness).append(';');
			sb.append(e.growth).append(';');
			sb.append(e.feedTime).append(';');
			sb.append(e.feedTimes).append(';');
			sb.append(e.zanTimes).append(';');
			sb.append(e.zanOfferTime).append(';');
			sb.append(e.tryTimes).append(';');
			sb.append(e.buyTryTimes).append(';');
			sb.append(awakeLvlMap.get(e.id) == null ? 0 : awakeLvlMap.get(e.id)).append(";");
			sb.append(e.drops.size()).append(';');
			for (SBean.DropEntry d : e.drops) {
				sb.append(d.type).append(';');
				sb.append(d.id).append(';');
				sb.append(d.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodePetsDeformFeedRes(short pid, byte itemIndex, byte deformStage, short deformGrowth, List<SBean.DropEntry> drops)
	{
		StringBuilder sb = new StringBuilder(";pets;deformfeed;");
		sb.append(pid).append(';');
		sb.append(itemIndex).append(';');
		sb.append(deformStage).append(';');
		sb.append(deformGrowth).append(';');
		if (drops == null)
			sb.append(0).append(';');
		else {
			sb.append(drops.size()).append(';');
			for (SBean.DropEntry d : drops) {
				sb.append(d.type).append(';');
				sb.append(d.id).append(';');
				sb.append(d.arg).append(';');
			}			
		}
		
		return sb.toString();
	}
		
	public static String encodePetsDeformZanRes(short pid, byte deformStage, short deformGrowth)
	{
		StringBuilder sb = new StringBuilder(";pets;deformzan;");
		sb.append(pid).append(';');
		sb.append(deformStage).append(';');
		sb.append(deformGrowth).append(';');
		
		return sb.toString();
	}
		
	public static String encodePetsDeformZanOfferRes(short pid, short reward, short count, byte result)
	{
		StringBuilder sb = new StringBuilder(";pets;deformzanoffer;");
		sb.append(pid).append(';');
		sb.append(reward).append(';');
		sb.append(count).append(';');
		sb.append(result).append(';');
		
		return sb.toString();
	}
		
	public static String encodePetsDeformTakeOfferRes(int rid, String rname, short pid, short take, int totalReward, short growth, short totalGrowth, int vip, int maxTimes)
	{
		StringBuilder sb = new StringBuilder(";pets;deformtakeoffer;");
		sb.append(rid).append(';');
		sb.append(rname).append(';');
		sb.append(pid).append(';');
		sb.append(take).append(';');
		sb.append(totalReward).append(';');
		sb.append(growth).append(';');
		sb.append(totalGrowth).append(';');
		sb.append(vip).append(';');
		sb.append(maxTimes).append(';');
		
		return sb.toString();
	}
		
	public static String encodePetsDeformTryRes(short pid, byte deformStage, short deformGrowth)
	{
		StringBuilder sb = new StringBuilder(";pets;deformtry;");
		sb.append(pid).append(';');
		sb.append(deformStage).append(';');
		sb.append(deformGrowth).append(';');
		
		return sb.toString();
	}
		
	public static String encodePetsDeformBuyTryRes(short pid, byte buyTryTimes)
	{
		StringBuilder sb = new StringBuilder(";pets;deformbuytry;");
		sb.append(pid).append(';');
		sb.append(buyTryTimes).append(';');
		
		return sb.toString();
	}
		
	public static String encodeGeneralsFirstEvoRes(short gid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;firstevo;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsUpgradeSkillRes(short gid, byte pos, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;upgradeskill;");
		sb.append(gid).append(';');
		sb.append(pos).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsActiveWeaponRes(short gid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;activeweapon;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsEnhanceWeaponRes(short gid, short stage)
	{
		StringBuilder sb = new StringBuilder(";generals;enhanceweapon;");
		sb.append(gid).append(';');
		sb.append(stage).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsEvoWeaponRes(short gid, byte evo)
	{
		StringBuilder sb = new StringBuilder(";generals;evoweapon;");
		sb.append(gid).append(';');
		sb.append(evo).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsResetWeaponRes(short gid, boolean bOK, List<SBean.DBWeaponProp> props)
	{
		StringBuilder sb = new StringBuilder(";generals;resetweapon;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		if (bOK) {
			sb.append(props.size()).append(';');
			for (SBean.DBWeaponProp prop : props) {
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsSaveResetWeaponRes(short gid, boolean bOK, List<SBean.DBWeaponProp> props)
	{
		StringBuilder sb = new StringBuilder(";generals;saveweapon;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		if (bOK) {
			sb.append(props.size()).append(';');
			for (SBean.DBWeaponProp prop : props) {
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsAddSeyenExpRes(short gid,byte type, boolean bOK, List<Short> props)
	{
		StringBuilder sb = new StringBuilder(";generals;addSeyenExp;");
		sb.append(gid).append(';');
		sb.append(type).append(';');
		sb.append(bOK?1:0).append(';');
		if (bOK) {
			sb.append(props.size()).append(';');
			for (Short prop : props) {
				sb.append(prop).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeRevertSeyenRes(short gid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generals;revertSeyen;");
		sb.append(gid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeEquipsSellRes(short eid, short cnt, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";equips;sell;");
		sb.append(eid).append(';');
		sb.append(cnt).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeEquipsCombineRes(short eid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";equips;combine;");
		sb.append(eid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeItemCombineRes(short iid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";items;combine;");
		sb.append(iid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeItemUseRes(short iid, String where, byte res, SBean.DropEntry[] ritems)
	{
		StringBuilder sb = new StringBuilder(";items;use;");
		sb.append(iid).append(';');
		sb.append(where).append(';');
		sb.append(res).append(';');
		if( ritems[0] == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(ritems[0].type).append(';');
			sb.append(ritems[0].arg).append(';');
			sb.append(ritems[0].id).append(';');
		}
		return sb.toString();
	}
	
	public static String encodePaySync(int stone, boolean bMCard, int payTotal)
	{
		StringBuilder sb = new StringBuilder(";pay;sync;");
		sb.append(stone).append(';');
		sb.append(bMCard?1:0).append(';');
		sb.append(payTotal).append(';');
		return sb.toString();
	}
	
	public static String encodePayInit(String id, int rmb, int zoneID)
	{
		StringBuilder sb = new StringBuilder(";pay;init;");
		if( id == null )
		{
			sb.append(0).append(';');
			sb.append(1).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(id).append(';');
		}
		sb.append(rmb).append(';');
		sb.append(zoneID).append(';');
		return sb.toString();
	}
	
//	public static String encodeLevelLimitInfo(LoginManager.LevelLimitInfo info)
//	{
//		StringBuilder sb = new StringBuilder(";levellimit;");
//		if (info == null)
//		{
//			sb.append(0).append(';');
//		}
//		else
//		{
//			sb.append(1).append(';');
//			sb.append(info.levelLimit).append(";");
//			sb.append(info.lessThan20ExpAdd).append(";");
//			sb.append(info.lessThan40ExpAdd).append(";");
//			sb.append(info.nextLevelLimit).append(";");
//			sb.append(info.nextLevelLimitOpenTime).append(";");
//			sb.append(info.nextLevelShowDay).append(";");
//			sb.append(info.resLevelLimit).append(";");
//		}
//		return sb.toString();
//	}
	
	public static String encodeLevelLimitInfo(LoginManager.TopLevelHaloInfo info)
	{
		StringBuilder sb = new StringBuilder(";toplevel;");
		if (info == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(info.toplevel).append(";");
			sb.append(info.rid).append(";");
			sb.append(info.rname).append(";");
			sb.append(info.lessThan20ExpAdd).append(";");
			sb.append(info.lessThan40ExpAdd).append(";");
		}
		return sb.toString();
	}
	
	public static String encodeEggsInfo(List<i3k.gs.Role.EggInfo> lst, boolean bOpen)
	{
		StringBuilder sb = new StringBuilder(";egg;sync;");
		sb.append(lst.size()).append(';');
		for(i3k.gs.Role.EggInfo e : lst)
		{
			sb.append(e.id).append(';');
			sb.append(e.timesToday).append(';');
			sb.append(e.timesAll).append(';');
			sb.append(e.freeTime).append(';');
		}
		sb.append(bOpen?1:0).append(';');
		if( lst.size() > 1 )
		{
			sb.append(lst.get(1).times10).append(';');
			sb.append(lst.get(1).firstEgg).append(';');
		}
		return sb.toString();
	}
	
//	public static String encodeEggsInfo(List<i3k.gs.Role.EggInfo> lst, boolean bOpen, i3k.gs.Role.SoulBoxInfo soulBoxInfo)
//	{
//		StringBuilder sb = new StringBuilder(";egg;sync;");
//		sb.append(lst.size()).append(';');
//		for(i3k.gs.Role.EggInfo e : lst)
//		{
//			sb.append(e.id).append(';');
//			sb.append(e.timesToday).append(';');
//			sb.append(e.timesAll).append(';');
//			sb.append(e.freeTime).append(';');
//		}
//		sb.append(bOpen?1:0).append(';');
//		if( lst.size() > 1 )
//			sb.append(lst.get(1).times10).append(';');
//		if (soulBoxInfo == null)
//		{
//			sb.append(0).append(';');
//		}
//		else
//		{
//			sb.append(1).append(';');
//			sb.append(soulBoxInfo.cfg.cost).append(';');
//			sb.append(soulBoxInfo.cfg.weekHotPointGeneralID).append(';');
//			SBean.DayHotPoint dhp = soulBoxInfo.cfg.dayHotPoint.get(soulBoxInfo.dayIndex);
//			sb.append(dhp.dayHotPoint.size()).append(';');
//			for (SBean.HotPoint hp : dhp.dayHotPoint)
//			{
//				sb.append(hp.generialID).append(';');
//			}
//		}
//		return sb.toString();
//	}
	
	public static String encodeSoulBoxInfo(i3k.gs.Role.SoulBoxInfo soulBoxInfo, int logtimes)
	{
		StringBuilder sb = new StringBuilder(";soulbox;sync;");
		if (soulBoxInfo == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(soulBoxInfo.cfg.id).append(';');
			sb.append(soulBoxInfo.cfg.cost).append(';');
			sb.append(soulBoxInfo.cfg.weekHotPointGeneralID).append(';');
			SBean.DayHotPoint dhp = soulBoxInfo.cfg.dayHotPoint.get(soulBoxInfo.dayIndex);
			sb.append(dhp.dayHotPoint.size()).append(';');
			for (SBean.HotPoint hp : dhp.dayHotPoint)
			{
				sb.append(hp.generialID).append(';');
			}
			sb.append(logtimes).append(';');
			sb.append(soulBoxInfo.cfg.optionalHotPoint.size()).append(';');
			for (short gid : soulBoxInfo.cfg.optionalHotPoint)
			{
				sb.append(gid).append(';');
			}
			sb.append(soulBoxInfo.optionalGid).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeOptionalSoulBoxRes(short gid, boolean res)
	{
		StringBuilder sb = new StringBuilder(";soulbox;optional;");
		sb.append(gid).append(';');
		sb.append(res?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopNormalSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";shopnormal;sync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopBlessSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";bless;sync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopRandSync(byte shopid, Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";shoprand;sync;");
		sb.append(shopid).append(';');
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopRandBuyRes(byte shopid, byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";shoprand;buy;");
		sb.append(shopid).append(';');
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopRandBrief(List<Role.ShopRandBrief> lst)
	{
		StringBuilder sb = new StringBuilder(";shoprand;brief;");
		sb.append(lst.size()).append(';');
		for(Role.ShopRandBrief e : lst)
		{
			sb.append(e.flag1).append(';');
			sb.append(e.timeout).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeShopRandNotice(byte shopid)
	{
		StringBuilder sb = new StringBuilder(";shoprand;notice;");
		sb.append(shopid).append(';');
		return sb.toString();
	}
	
	public static String encodeShopRandRefreshRes(byte shopid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";shoprand;refresh;");
		sb.append(shopid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopRandSummonRes(byte shopid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";shoprand;summon;");
		sb.append(shopid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopArenaSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";arena;shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopSuperArenaSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";superarena;shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopDuelSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";duel;shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopRichSync(int index, Role.ShopInfo info)
	{
		String[] tag = { "stone", "point", "gold" };
		StringBuilder sb = new StringBuilder(";rich;" + tag[index] + "shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(GameData.getInstance().getDiscountPrice(e.price, info.discount)).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopMarchSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";march;shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeShopForceSync(Role.ShopInfo info)
	{
		StringBuilder sb = new StringBuilder(";force;shopsync;");
		if( info == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(info.timeLeft).append(';');
			sb.append(info.refreshTimes).append(';');
			int n = info.goods.size();
			sb.append(n).append(';');
			for(int i = 0; i < n; ++i)
			{
				SBean.ShopGoods e = info.goods.get(i);
				sb.append(e.item.type).append(';');
				sb.append(e.item.id).append(';');
				sb.append(e.item.arg).append(';');
				sb.append(e.curUnit).append(';');
				sb.append(e.price).append(';');
				sb.append(info.logs.get(i).byteValue()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceThiefCombats(int startTime, List<ForceWarManager.ForceThiefCombat> combats)
	{
		StringBuilder sb = new StringBuilder(";force;thiefcombats;");
		sb.append(startTime).append(';');
		if (combats == null)
			sb.append(0).append(';');
		else {
			sb.append(combats.size()).append(';');
			for (ForceWarManager.ForceThiefCombat combat : combats) {
				sb.append(combat.groupIndex).append(';');
				sb.append(combat.generalType).append(';');
				sb.append(combat.combatId).append(';');
				sb.append(combat.fighting?1:0).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceThiefDamageRanksRes(int rid, List<ForceWarManager.DamageRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;thiefdamageranks;");
		sb.append(rid).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (ForceWarManager.DamageRank r : ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.rid).append(';');
				sb.append(r.damage).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.generals.size()).append(';');
				for (SBean.DBForceThiefGeneralBrief g : r.generals) {
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
				}
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceThiefStartAttackRes(int fid, int index, int res, List<SBean.DBForceWarGeneralStatus> status, SBean.CombatBonus bonus)
	{
		StringBuilder sb = new StringBuilder(";force;thiefstartattack;");
		sb.append(fid).append(';');
		sb.append(index).append(';');
		sb.append(res).append(';');
		if (status == null)
			sb.append(0).append(';');
		else {
			sb.append(status.size()).append(';');
			for (SBean.DBForceWarGeneralStatus s : status) {
				sb.append(s.id).append(';');
				sb.append(s.hp).append(';');
				sb.append(s.mp).append(';');
				sb.append(s.state).append(';');
			}
		}
		if (bonus == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			SBean.CombatBonus cb = bonus;
			sb.append(cb.money).append(';');
			sb.append(cb.exp).append(';');
			sb.append(cb.entryIDs.size()).append(';');
			for(SBean.DropEntryNew e : cb.entryIDs)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceThiefFinishAttackRes(int fid, int index, int win, int res)
	{
		StringBuilder sb = new StringBuilder(";force;thieffinishattack;");
		sb.append(fid).append(';');
		sb.append(index).append(';');
		sb.append(win).append(';');
		sb.append(res).append(';');
		return sb.toString();	
	}
	
	public static String encodeForceBeastSyncRes(int fid, List<SBean.DBForceBeast> beasts)
	{
		StringBuilder sb = new StringBuilder(";force;beastsync;");
		sb.append(fid).append(';');
		
		if (beasts != null) {
			sb.append(beasts.size()).append(';');
			for (SBean.DBForceBeast beast : beasts) {
				sb.append(beast.general.id).append(';');
				sb.append(beast.general.lvl).append(';');
				sb.append(beast.general.exp).append(';');
				sb.append(beast.general.evoLvl).append(';');
				for (int i = 0; i < 5; i ++)
					sb.append(beast.general.skills.get(i)).append(';');
				sb.append(beast.attackProp).append(';');
				sb.append(beast.mainProp).append(';');
				for (int i = 0; i < 5; i ++)
					sb.append(beast.props.get(i)).append(';');
				sb.append(beast.resetPropTimes).append(';');
			}
		}
		else 
			sb.append(0).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastResetAttackPropRes(int fid, short gid, boolean ok, int newProp)
	{
		StringBuilder sb = new StringBuilder(";force;beastresetattackprop;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(newProp).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastSetDefaultRes(int fid, short gid, byte def, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;beastsetdefault;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(def).append(';');
		sb.append(ok?1:0).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastUpgradeSkillRes(int fid, short gid, byte skill, short lvl)
	{
		StringBuilder sb = new StringBuilder(";force;beastupgradeskill;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(skill).append(';');
		sb.append(lvl).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastUpgradePropRes(int fid, short gid, byte prop, short lvl)
	{
		StringBuilder sb = new StringBuilder(";force;beastupgradeprop;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(prop).append(';');
		sb.append(lvl).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastResetSkillRes(int fid, short gid, boolean ok, int giveBack)
	{
		StringBuilder sb = new StringBuilder(";force;beastresetskill;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(giveBack).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastResetPropRes(int fid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;beastresetprop;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastUpgradeEvoRes(int fid, short gid, byte lvl)
	{
		StringBuilder sb = new StringBuilder(";force;beastupgradeevo;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(lvl).append(';');
		
		return sb.toString();	
	}
	
	public static String encodeForceBeastFeedRes(int fid, short gid, boolean ok, int[] values)
	{
		StringBuilder sb = new StringBuilder(";force;beastfeed;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(values[0]).append(';'); // exp
		sb.append(values[1]).append(';'); // lvl
		
		return sb.toString();	
	}
	
	public static String encodeShopNormalBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";shopnormal;buy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopBlessBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";bless;buy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopArenaBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;shopbuy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopSuperArenaBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;shopbuy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopDuelBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";duel;shopbuy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopRichBuyRes(int index, byte seq, byte type, short id, boolean bOK)
	{
		String[] tag = { "stone", "point", "gold" };
		StringBuilder sb = new StringBuilder(";rich;" + tag[index] + "shopbuy;");
		
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopMarchBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";march;shopbuy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopForceBuyRes(byte seq, byte type, short id, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";force;shopbuy;");
		sb.append(seq).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopNormalRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";shopnormal;refresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopBlessRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";bless;refresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopArenaRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;shoprefresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopSuperArenaRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;shoprefresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopDuelRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";duel;shoprefresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeDuelBuyTimeRes(boolean bOK, byte buyTimes)
	{
		StringBuilder sb = new StringBuilder(";duel;buytime;");
		sb.append(bOK?1:0).append(';');
		sb.append(buyTimes).append(';');
		return sb.toString();
	}
	
	public static String encodeShopRichRefreshRes(int index, boolean bOK)
	{
		String[] tag = { "stone", "point", "gold" };
		StringBuilder sb = new StringBuilder(";rich;" + tag[index] + "shoprefresh;");
		
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopMarchRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";march;shoprefresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeShopForceRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";force;shoprefresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeCombatEventSync(byte ceid, SBean.DBCombatEventLog log)
	{
		StringBuilder sb = new StringBuilder(";combatevent;sync;");
		sb.append(ceid).append(';');
		if( log != null )
		{
			sb.append(1).append(';');
			sb.append(log.timesToday).append(';');
			sb.append(log.lastTime).append(';');
		}
		else
			sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeCombatEventStartRes(byte ceid, byte celvl, short cid, boolean bOK, SBean.CombatBonus cb)
	{
		StringBuilder sb = new StringBuilder(";combatevent;start;");
		sb.append(ceid).append(';');
		sb.append(celvl).append(';');
		sb.append(cid).append(';');
		sb.append(bOK?1:0).append(';');
		if( bOK )
		{
			if( cb != null )
			{
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.generalExp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeCombatEventFinishRes(byte ceid, byte celvl, short cid, byte star, List<Short> generalIDs, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";combatevent;finish;");
		sb.append(ceid).append(';');
		sb.append(celvl).append(';');
		sb.append(cid).append(';');
		sb.append(star).append(';');
		sb.append(generalIDs.size()).append(';');
		for(short gid : generalIDs)
		{
			sb.append(gid).append(';');
		}
		sb.append(bOK?1:0).append(';');
		if( bOK )
		{
			
		}
		return sb.toString();
	}
	
	public static String encodeCombatEventSkipRes(byte ceid, byte celvl, short cid, byte nTimes, boolean bOK, SBean.CombatBonus[] cbs)
	{
		StringBuilder sb = new StringBuilder(";combatevent;skip;");
		sb.append(ceid).append(';');
		sb.append(celvl).append(';');
		sb.append(cid).append(';');
		sb.append(nTimes).append(';');
		sb.append(bOK?1:0).append(';');
		if( bOK && cbs != null )
		{
			for(byte i = 0; i <= nTimes && i < cbs.length; ++i )
			{
				SBean.CombatBonus cb = cbs[i];
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.generalExp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeCombatsStartRes(byte ctype, byte cindex, byte bindex, short cid, boolean bOK, SBean.CombatBonus cb, int normalDropCnt)
	{
		StringBuilder sb = new StringBuilder(";combats;start;");
		sb.append(ctype).append(';');
		sb.append(cindex).append(';');
		sb.append(bindex).append(';');
		sb.append(cid).append(';');
		sb.append(bOK?1:0).append(';');
		if( bOK )
		{
			if( cb != null )
			{
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.generalExp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
				sb.append(cb.stoneTimes).append(';');
				sb.append(cb.stone).append(';');
				sb.append(normalDropCnt).append(";");
			}
		}
		return sb.toString();
	}
	
	public static String encodeCombatsFinishRes(byte ctype, byte cindex, byte bindex, short cid, byte star, List<Short> generalIDs
			, boolean bOK, byte qqVIP)
	{
		StringBuilder sb = new StringBuilder(";combats;finish;");
		sb.append(ctype).append(';');
		sb.append(cindex).append(';');
		sb.append(bindex).append(';');
		sb.append(cid).append(';');
		sb.append(star).append(';');
		sb.append(generalIDs.size()).append(';');
		for(short gid : generalIDs)
		{
			sb.append(gid).append(';');
		}
		sb.append(bOK?1:0).append(';');
		sb.append(qqVIP).append(';');
		if( bOK )
		{
			
		}
		return sb.toString();
	}
	
	public static String encodeCombatsSkipRes(byte ctype, byte cindex, byte bindex, short cid, byte nTimes
			, boolean bOK, byte qqVIP, SBean.CombatBonus[] cbs)
	{
		StringBuilder sb = new StringBuilder(";combats;skip;");
		sb.append(ctype).append(';');
		sb.append(cindex).append(';');
		sb.append(bindex).append(';');
		sb.append(cid).append(';');
		sb.append(nTimes).append(';');
		sb.append(bOK?1:0).append(';');
		sb.append(qqVIP).append(';');
		if( bOK && cbs != null )
		{
			for(byte i = 0; i <= nTimes && i < cbs.length; ++i )
			{
				SBean.CombatBonus cb = cbs[i];
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.generalExp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeCombatsResetRes(short cid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";combats;reset;");
		sb.append(cid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static  String encodeRoleSkillOPRes(short id, String op,boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";roleskill;");
		sb.append(op).append(';');
		sb.append(id).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeMedalInfo(List<i3k.gs.Role.MedalInfo> lst)
	{
		StringBuilder sb = new StringBuilder(";medal;sync;");
		sb.append(lst.size()).append(';');
		for(i3k.gs.Role.MedalInfo e : lst)
		{
			sb.append(e.id).append(';');
			sb.append(e.seq).append(';');
			sb.append(e.state).append(';');
			sb.append(e.max).append(';');
			sb.append(e.cur).append(';');
		}
		return sb.toString();
	}
	
	public static String encodePayTable(SBean.PayCFGS cfg, SBean.MonthlyCardCFGS mcard, int mcardStart, Set<Byte> payDouble)
	{
		StringBuilder sb = new StringBuilder(";pay;table;");
		if( mcard != null )
		{
			sb.append(mcard.price).append(';');
			sb.append(mcard.days).append(';');
			sb.append(mcard.reward).append(';');
		}
		else
		{
			sb.append(0).append(';');
		}
		sb.append(mcardStart).append(';');
		sb.append(cfg.floor).append(';');
		sb.append(cfg.ceil).append(';');
		sb.append(cfg.levels.size()).append(';');
		byte l = 0;
		for(SBean.PayCFGLevelS e : cfg.levels)
		{
			++l;
			sb.append(e.rmb).append(';');
			sb.append(e.stone).append(';');
			if( payDouble.contains(l) )
				sb.append(1).append(';');
			else
				sb.append(0).append(';');
		}
		return sb.toString();
	}
	
	public static String encodePayTableErr(int errcode, int ret)
	{
		StringBuilder sb = new StringBuilder(";pay;tableerr;");
		sb.append(errcode).append(';');
		sb.append(ret).append(';');
		return sb.toString();
	}
	
	public static String encodeFirstPayCheck(int payTotal)
	{
		StringBuilder sb = new StringBuilder(";firstpay;check;");
		sb.append(payTotal).append(';');
		return sb.toString();
	}
	
	public static String encodeFirstPayTakeRes(int res)
	{
		StringBuilder sb = new StringBuilder(";firstpay;take;");
		sb.append(res).append(';');
		return sb.toString();
	}
	
	public static String encodeVIPInfo(i3k.gs.Role.VIPInfo info)
	{
		StringBuilder sb = new StringBuilder(";vip;sync;");
		sb.append(info.payTotal).append(';');
		sb.append(info.lvl).append(';');
		sb.append(info.vipRewards.size()).append(';');
		for(byte e : info.vipRewards)
			sb.append(e).append(';');
		return sb.toString();
	}
	
	public static String encodeQQVIPSync(byte qqVIP, boolean addNormal, boolean addSuper)
	{
		StringBuilder sb = new StringBuilder(";qqvip;sync;");
		sb.append(qqVIP).append(';');
		sb.append(addNormal ? 1 : 0).append(';');
		sb.append(addSuper ? 1 : 0).append(';');
		return sb.toString();
	}
	
	public static String encodeQQVIPRewards(byte qqVIPRewards)
	{
		StringBuilder sb = new StringBuilder(";qqvip;rewards;");
		sb.append(qqVIPRewards).append(';');
		return sb.toString();
	}
	
	public static String encodeTakeQQVIPReward(int rType, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";qqvip;takereward;");
		sb.append(rType).append(';');
		sb.append(bOk ? 1 : 0).append(';');
		return sb.toString();
	}
	
	public static String encodeQQVIPOpenInit(int zoneID)
	{
		StringBuilder sb = new StringBuilder(";qqvip;openinit;");
		sb.append(zoneID).append(';');
		return sb.toString();
	}
	
	public static String encodeJubaoTakeRes(int res)
	{
		StringBuilder sb = new StringBuilder(";jubao;take;");
		sb.append(res).append(';');
		return sb.toString();
	}
	
	public static String encodeVIPUpdate(byte lvl, int payTotal, boolean bNotice)
	{
		StringBuilder sb = new StringBuilder(";vip;update;");
		sb.append(lvl).append(';');
		sb.append(payTotal).append(';');
		sb.append(bNotice?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodePrisonBreakNotice(short gid)
	{
		StringBuilder sb = new StringBuilder(";prison;break;");
		sb.append(gid).append(';');
		return sb.toString();
	}
	
	public static String encodeCheckinSyncInfo(i3k.gs.Role.CheckinSyncInfo info, byte qqVIP)
	{
		StringBuilder sb = new StringBuilder(";checkin;sync;");
		sb.append(info.id).append(';');
		sb.append(info.n).append(';');
		sb.append(info.bOK?1:0).append(';');
		sb.append(qqVIP).append(';');
		return sb.toString();
	}
	
	public static String encodeCheckinTakeRes(boolean bOK, byte qqVIP)
	{
		StringBuilder sb = new StringBuilder(";checkin;take;");
		sb.append(bOK?1:0).append(';');
		sb.append(qqVIP).append(';');
		return sb.toString();
	}
	
	public static List<String> encodeLuaPatch(String p)
	{
		List<String> info = new ArrayList<String>();
		info.add("patch");
		info.add("code");
		info.add(p);
		return info;
	}
	
	public static List<String> encodeLuaPatchTip()
	{
		List<String> info = new ArrayList<String>();
		info.add("patch");
		info.add("tip");
		return info;
	}
	
	public static List<String> encodeActivitiesCenterInfo(List<ActivityDisplayConfig> openSoonCfgs, List<ActivityDisplayConfig> openedCfgs, List<Boolean> canRewards, List<Boolean> alreadyTakeAllRewards)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("table");
		info.add(String.valueOf(openSoonCfgs.size()));
		for (ActivityDisplayConfig cfg : openSoonCfgs)
		{
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getID()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
		}
		info.add(String.valueOf(openedCfgs.size()));
		for (int i = 0; i < openedCfgs.size(); ++i)
		{
			ActivityDisplayConfig cfg = openedCfgs.get(i);
			boolean canReward = canRewards.get(i);
			boolean alreadyTakeAllReward = alreadyTakeAllRewards.get(i);
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getID()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(String.valueOf(canReward ? 1 : 0));
			info.add(String.valueOf(alreadyTakeAllReward ? 1 : 0));
		}
		return info;
	}
	
	public static List<String> encodeDoubleDropSyncInfo(GameActivities.DoubleDropConfig cfg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("doubledropsync");
		if (cfg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
		}
		return info;
	}
	
	public static List<String> encodeExtraDropSyncInfo(GameActivities.ExtraDropConfig cfg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("extradropsync");
		if (cfg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
		}
		return info;
	}
	
	public static List<String> encodeCheckinGiftSyncInfo(GameActivities.CheckinGiftConfig cfg, SBean.DBRoleCheckinGift cg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("checkingiftsync");
		if (cfg == null || cg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int days = cfg.getDays();
			info.add(String.valueOf(days));
			for (int i = 0; i < days; ++i)
			{
				int lvlReq = cfg.getDayGiftLevelReq(i);
				info.add(String.valueOf(lvlReq));
				int count = cfg.getDayGiftCount(i);
				info.add(String.valueOf(count));
				for (GameActivities.GiftItem gi : cfg.getDayGift(i))
				{
					info.add(String.valueOf(gi.item.type));
					info.add(String.valueOf(gi.item.id));
					info.add(String.valueOf(gi.item.arg));
					info.add(String.valueOf(gi.doubleCountLvl));
				}
			}
			info.add(String.valueOf(cg.id));
			info.add(String.valueOf(cg.checkinDay));
			info.add(String.valueOf(cg.lastCheckinTime));
		}
		return info;
	}
	
	public static List<String> encodeCheckinGiftTakeRes(int id, int seq, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("checkingifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(seq));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	
	public static List<String> encodeFirstPayGiftSyncInfo(GameActivities.FirstPayGiftConfig cfg, SBean.DBRoleFirstPayGift pg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("firstpaygiftsync");
		if (cfg == null || pg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int payLevels = 1;
			info.add(String.valueOf(payLevels));
			for (int i = 0; i < payLevels; ++i)
			{
				int count = cfg.getGiftCount();
				info.add(String.valueOf(count));
				for (SBean.DropEntryNew e : cfg.getGift())
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			
			info.add(String.valueOf(pg.id));
			info.add(String.valueOf(pg.pay));
			info.add(String.valueOf(pg.reward));
		}
		return info;
	}
	
	public static List<String> encodeFirstPayGiftTakeRes(int id, int seq, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("firstpaygifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(seq));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodePayGiftSyncInfo(GameActivities.PayGiftConfig cfg, SBean.DBRolePayGift cg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("paygiftsync");
		if (cfg == null || cg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int lvlCnt = cfg.getPayLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (GameActivities.PayLvlGift clg : cfg.getPayLvlGift())
			{
				info.add(String.valueOf(clg.payReq));
				info.add(String.valueOf(clg.gift.size()));
				for (SBean.DropEntryNew e : clg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(cg.id));
			info.add(String.valueOf(cg.pay));
			info.add(String.valueOf(cg.reward.size()));
			for (int c : cg.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodePayGiftTakeRes(int id, int pay, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("paygifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(pay));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
    public static List<String> encodeDiskGiftTakeRes(int id, int pay, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskgifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(pay));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
    
    public static List<String> encodeRechargeGiftTakeRes(int id, int pay, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("rechargegifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(pay));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeDiskGiftSyncInfo(DiskGiftConfig cfg, DBRoleDiskGift dbRoleDiskGift)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskgiftsync");
		if (cfg == null || dbRoleDiskGift == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int lvlCnt = cfg.getDiskLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (DiskLvlGift clg : cfg.getDiskLvlGift())
			{
				info.add(String.valueOf(clg.payReq));
				info.add(String.valueOf(clg.gift.size()));
				for (SBean.DropEntryNew e : clg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(dbRoleDiskGift.id));
			info.add(String.valueOf(dbRoleDiskGift.pay));
			info.add(String.valueOf(dbRoleDiskGift.reward.size()));
			for (int c : dbRoleDiskGift.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	
	public static List<String> encodeRechargeGiftSyncInfo(RechargeGiftConfig cfg, DBRoleRechargeGift dbRoleRechargeGift)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("rechargegiftsync");
		if (cfg == null || dbRoleRechargeGift == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int lvlCnt = cfg.getDiskLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (RechargeLvlGift clg : cfg.getRechargeLvlGift())
			{
				info.add(String.valueOf(clg.payReq));
				info.add(String.valueOf(clg.gift.size()));
				for (SBean.DropEntryNew e : clg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(dbRoleRechargeGift.id));
			info.add(String.valueOf(dbRoleRechargeGift.pay));
			info.add(String.valueOf(dbRoleRechargeGift.reward.size()));
			for (int c : dbRoleRechargeGift.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeConsumeGiftSyncInfo(GameActivities.ConsumeGiftConfig cfg, SBean.DBRoleConsumeGift cg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("consumegiftsync");
		if (cfg == null || cg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			info.add(String.valueOf(cfg.isConsumeMoney() ? 1 : 0));
			int lvlCnt = cfg.getConsumeLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (GameActivities.ConsumeLvlGift clg : cfg.getConsumeLvlGift())
			{
				info.add(String.valueOf(clg.consumeReq));
				info.add(String.valueOf(clg.gift.size()));
				for (SBean.DropEntryNew e : clg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(cg.id));
			info.add(String.valueOf(cg.consume));
			info.add(String.valueOf(cg.reward.size()));
			for (int c : cg.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeConsumeGiftTakeRes(int id, int consume, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("consumegifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(consume));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeConsumeRebateSyncInfo(GameActivities.ConsumeRebateConfig cfg, SBean.DBRoleConsumeRebate cr)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("consumerebatesync");
		if (cfg == null || cr == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int lvlCnt = cfg.getConsumeLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (GameActivities.ConsumeLvlRebate clr : cfg.getConsumeLvlRebate())
			{
				info.add(String.valueOf(clr.consumeReq));
				info.add(String.valueOf(clr.rebate));
			}
			info.add(String.valueOf(cr.id));
			info.add(String.valueOf(cr.consume));
			info.add(String.valueOf(cr.reward.size()));
			for (int c : cr.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeConsumeRebateTakeRes(int id, int consume, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("consumerebatetake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(consume));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	
	public static List<String> encodeUpgradeGiftSyncInfo(GameActivities.UpgradeGiftConfig cfg, SBean.DBRoleUpgradeGift ug)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("upgradegiftsync");
		if (cfg == null || ug == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			info.add(String.valueOf(cfg.getLimitedEndTime()));
			int lvlCnt = cfg.getUpgradeLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (GameActivities.UpgradeLvlGift ulg : cfg.getUpgradeLvlGift())
			{
				info.add(String.valueOf(ulg.lvlReq));
				info.add(String.valueOf(ulg.gift.size()));
				for (SBean.DropEntryNew e : ulg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
				info.add(String.valueOf(ulg.giftEx.size()));
				for (SBean.DropEntryNew e : ulg.giftEx)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(ug.id));
			info.add(String.valueOf(ug.reward.size()));
			for (int c : ug.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeUpgradeGiftTakeRes(int id, int level, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("upgradegifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(level));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeExchangeGiftSyncInfo(GameActivities.ExchangeGiftConfig cfg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("exchangegiftsync");
		if (cfg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int giftCnt = cfg.getGiftCount();
			info.add(String.valueOf(giftCnt));
			for (GameActivities.PropsGift pg : cfg.getExchangeGift())
			{
				info.add(String.valueOf(pg.props.size()));
				for (SBean.DropEntryNew e : pg.props)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
				info.add(String.valueOf(pg.gift.type));
				info.add(String.valueOf(pg.gift.id));
				info.add(String.valueOf(pg.gift.arg));
			}
			info.add(String.valueOf(cfg.getID()));
		}
		return info;
	}
	
	public static List<String> encodeExchangeGiftTakeRes(int id, int seq, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("exchangegifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(seq));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	
	public static List<String> encodeGatherGiftSyncInfo(GameActivities.GatherGiftConfig cfg, SBean.DBRoleGatherGift gg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("gathergiftsync");
		if (cfg == null || gg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int cnt = cfg.getGiftCount();
			info.add(String.valueOf(cnt));
			for (GameActivities.GeneralGift glg : cfg.getGatherGift())
			{
				info.add(String.valueOf(glg.generals.size()));
				for (int gid : glg.generals)
				{
					info.add(String.valueOf(gid));
				}
				info.add(String.valueOf(glg.gift.type));
				info.add(String.valueOf(glg.gift.id));
				info.add(String.valueOf(glg.gift.arg));
			}
			info.add(String.valueOf(gg.id));
			info.add(String.valueOf(gg.reward.size()));
			for (int c : gg.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeGatherGiftTakeRes(int id, int seq, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("gathergifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(seq));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	
	public static List<String> encodeLimitedShopSyncInfo(GameActivities.LimitedShopConfig cfg, SBean.DBRoleLimitedShop ls)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("limitedshopsync");
		if (cfg == null || ls == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int cnt = cfg.getGiftCount();
			info.add(String.valueOf(cnt));
			for (GameActivities.LimitedGift lg : cfg.getLimitedGift())
			{
				info.add(String.valueOf(lg.vipReq));
				info.add(String.valueOf(lg.maxBuyTimes));
				info.add(String.valueOf(lg.cost));
				info.add(String.valueOf(lg.gift.size()));
				for (SBean.DropEntryNew e : lg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(ls.id));
			info.add(String.valueOf(ls.buglogs.size()));
			for (SBean.DBGiftBuyLog log : ls.buglogs)
			{
				info.add(String.valueOf(log.id));
				info.add(String.valueOf(log.count));
			}
		}
		return info;
	}
	
	public static List<String> encodeLimitedShopTakeRes(int id, int seq, int count, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("limitedshoptake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(seq));
		info.add(String.valueOf(count));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeLoginGiftSyncInfo(GameActivities.LoginGiftConfig cfg, SBean.DBRoleLoginGift cg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("logingiftsync");
		if (cfg == null || cg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			int lvlCnt = cfg.getLoginLvlCount();
			info.add(String.valueOf(lvlCnt));
			for (GameActivities.LoginLvlGift clg : cfg.getLoginLvlGift())
			{
				info.add(String.valueOf(clg.loginDaysReq));
				info.add(String.valueOf(clg.gift.size()));
				for (SBean.DropEntryNew e : clg.gift)
				{
					info.add(String.valueOf(e.type));
					info.add(String.valueOf(e.id));
					info.add(String.valueOf(e.arg));
				}
			}
			info.add(String.valueOf(cg.id));
			info.add(String.valueOf(cg.loginDays));
			info.add(String.valueOf(cg.reward.size()));
			for (int c : cg.reward)
			{
				info.add(String.valueOf(c));
			}
		}
		return info;
	}
	
	public static List<String> encodeLoginGiftTakeRes(int id, int days, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("logingifttake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(days));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeTradingCenterSyncInfo(GameActivities.TradingCenterConfig cfg, SBean.DBRoleTradingCenter tc)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("tradingcentersync");
		if (cfg == null || tc == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(cfg.getBriefTitle());
			info.add(cfg.getDetailContent());
			info.add(String.valueOf(cfg.getBuyEndTime()));
			int returnCnt = cfg.getReturnTimeCount();
			info.add(String.valueOf(returnCnt));
			for (int t = 0; t < returnCnt; ++t)
			{
				info.add(String.valueOf(cfg.getReturnTime(t)));
			}
			info.add(String.valueOf(cfg.getMaxBuyCount()));
			int goodsCnt = cfg.getGoodsCount();
			info.add(String.valueOf(goodsCnt));
			for (int i = 0; i < goodsCnt; ++i)
			{
				GameActivities.WarGoods gcfg = cfg.getGoodsByIndex(i);
				info.add(String.valueOf(gcfg.getType()));
				info.add(String.valueOf(gcfg.getVipLvlReq()));
				info.add(String.valueOf(gcfg.getPrice()));
//				info.add(String.valueOf(gcfg.getMaxBuyCount()));
				int returnTimes = gcfg.getReturnTimes();
				info.add(String.valueOf(returnTimes));
				for (int r = 0; r < returnCnt; ++r)
				{
					info.add(String.valueOf(gcfg.getReturnValue(r)));
				}
			}
			info.add(String.valueOf(tc.id));
			info.add(String.valueOf(tc.goods.size()));
			for (SBean.DBRoleWarGoods g : tc.goods)
			{
				info.add(String.valueOf(g.type));
				info.add(String.valueOf(g.buyCnt));
				info.add(String.valueOf(g.reward.size()));
				for (int r : g.reward)
				{
					info.add(String.valueOf(r));
				}
			}
		}
		return info;
	}
	
	
	public static List<String> encodeTradingCenterTakeRes(int id, int type, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("tradingcentertake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(type));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeTradingCenterBuyRes(int id, int type, int count, boolean bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("tradingcenterbuy");
		info.add(String.valueOf(id));
		info.add(String.valueOf(type));
		info.add(String.valueOf(count));
		info.add(String.valueOf(bOK?1:0));
		return info;
	}
	
	public static List<String> encodeGiftPackageSyncInfo(GameActivities.GiftPackageConfig cfg)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("giftpackagesync");
		if (cfg == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			
		}
		return info;
	}
	
	public static List<String> encodeGiftPackageTakeRes(int id, int result, String giftTitle, String giftContent, List<SBean.DropEntryNew> gift)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("giftpackagetake");
		info.add(String.valueOf(id));
		info.add(String.valueOf(result));
		info.add(giftTitle == null ? "" : giftTitle);
		info.add(giftContent == null ? "" : giftContent);
		if (gift == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(gift.size()));
			for (SBean.DropEntryNew den : gift)
			{
				info.add(String.valueOf(den.type));
				info.add(String.valueOf(den.id));
				info.add(String.valueOf(den.arg));
			}	
		}
		return info;
	}
	
	
	
//	public static String encodeActivitiesCenterInfo(List<ActivityDisplayConfig> openSoonCfgs, List<ActivityDisplayConfig> openedCfgs)
//	{
//		StringBuilder sb = new StringBuilder(";actcenter;table;");
//		sb.append(openSoonCfgs.size()).append(';');
//		for (ActivityDisplayConfig cfg : openSoonCfgs)
//		{
//			sb.append(cfg.getID()).append(';');
//			sb.append(cfg.getStartTime()).append(';');
//			sb.append(cfg.getEndTime()).append(';');
//			sb.append(cfg.getBriefTitle()).append(';');
//		}
//		sb.append(openedCfgs.size()).append(';');
//		for (ActivityDisplayConfig cfg : openedCfgs)
//		{
//			sb.append(cfg.getID()).append(';');
//			sb.append(cfg.getStartTime()).append(';');
//			sb.append(cfg.getEndTime()).append(';');
//			sb.append(cfg.getBriefTitle()).append(';');
//		}
//		return sb.toString();
//	}
	
//	public static String encodeCheckinGiftSyncInfo(GameActivities.CheckinGiftConfig cfg, SBean.DBRoleCheckinGift cg)
//	{
//		StringBuilder sb = new StringBuilder(";checkingift;sync;");
//		if (cfg == null || cg == null)
//		{
//			sb.append(0).append(';');
//		}
//		else
//		{
//			sb.append(1).append(';');
//			sb.append(cfg.getStartTime()).append(';');
//			sb.append(cfg.getEndTime()).append(';');
//			int days = cfg.getDays();
//			sb.append(days).append(';');
//			for (int i = 0; i < days; ++i)
//			{
//				int count = cfg.getDayGiftCount(i);
//				sb.append(count).append(';');
//				for (GameActivities.GiftItem gi : cfg.getDayGift(i))
//				{
//					sb.append(gi.item.type).append(';');
//					sb.append(gi.item.id).append(';');
//					sb.append(gi.item.arg).append(';');
//					sb.append(gi.doubleCountLvl).append(";");
//				}
//			}
//			sb.append(cg.id).append(';');
//			sb.append(cg.rewards.size()).append(';');
//			for(byte e : cg.rewards)
//			{
//				sb.append(e).append(';');
//			}
//		}
//		return sb.toString();
//	}
	
//	public static String encodeCheckinGiftTakeRes(int id, int seq, boolean bOK)
//	{
//		StringBuilder sb = new StringBuilder(";checkingift;take;");
//		sb.append(id).append(';');
//		sb.append(seq).append(';');
//		sb.append(bOK?1:0).append(';');
//		return sb.toString();
//	}
	
	public static String encodeDayGiftRes(i3k.gs.Role.DayGiftRes info)
	{
		StringBuilder sb = new StringBuilder(";daygift;res;");
		sb.append(info.type).append(';');
		sb.append(info.res).append(';');
		sb.append(info.r).append(';');
		return sb.toString();
	}
	
	public static String encodeMarchSyncInfo(Role.MarchStateInfo info)
	{
		StringBuilder sb = new StringBuilder(";march;sync;");
		sb.append(info.maxLvl).append(';');
		sb.append(info.stage).append(';');
		sb.append(info.rewardTaken).append(';');
		sb.append(info.point).append(';');
		sb.append(info.timesUsed).append(';');
		if (info.generalStates != null) {
			sb.append(info.generalStates.size()).append(';');
			for (SBean.DBRoleMarchGeneralState e : info.generalStates) {
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}
		}
		else
			sb.append(0).append(';');
		sb.append(info.enemies.size()).append(';');
		for(SBean.DBRoleMarchEnemy e : info.enemies)
		{
			sb.append(e.id).append(';'); 
			sb.append(e.name).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.headIconID).append(';');
			sb.append(e.fname).append(';');
			if (e.generals != null) {
				sb.append(e.generals.size()).append(';');
				for (DBRoleGeneral g : e.generals) {
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
					for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
					{
						sb.append(g.skills.get(i)).append(';');
					}
					for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
					{
						sb.append(g.equips.get(i).lvl).append(';');
						sb.append(g.equips.get(i).exp).append(';');
					}
					sb.append(g.weapon.lvl).append(';');
					if (g.weapon.lvl >= 0) {
						sb.append(g.weapon.evo).append(';');
						sb.append(g.weapon.passes.size()).append(';');
						for (SBean.DBWeaponProp prop : g.weapon.passes) {
							sb.append(prop.propId).append(';');					
							sb.append(prop.type).append(';');					
							sb.append(prop.value).append(';');					
							sb.append(prop.rank).append(';');					
						}
					}
					encodeGeneralSeyen(sb, g);
					encodeGeneralOfficial(sb, g);
				}
			}
			else {
				sb.append(0).append(';');
			}
			if (e.pets != null) {
				sb.append(e.pets.size()).append(';');
				for (SBean.DBPetBrief p : e.pets) {
					sb.append(p.id).append(';');
					sb.append(p.lvl).append(';');
					sb.append(p.evoLvl).append(';');
					sb.append(p.growthRate).append(';');
					sb.append(p.name).append(';');
					sb.append(p.deformStage).append(';');
					sb.append(p.awakLvl).append(";");
				}				
			}
			else {
				sb.append(0).append(';');				
			}
		}
		if (info.enemyGeneralStates != null) {
			sb.append(info.enemyGeneralStates.size()).append(';');
			for (SBean.DBRoleMarchGeneralState e : info.enemyGeneralStates) {
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}
		}
		else
			sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeMarchReward(boolean bOK, int money, int point, SBean.DropEntry drop)
	{
		StringBuilder sb = new StringBuilder(";march;reward;");
		sb.append(bOK?1:0).append(';');
		sb.append(money).append(';');
		sb.append(point).append(';');
		sb.append((drop == null)?0:1).append(';');
		if (drop != null) {
			sb.append(drop.type).append(';');
			sb.append(drop.id).append(';');
			sb.append(drop.arg).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeMarchReset(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";march;reset;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeMarchWipeRes(boolean succ, List<Role.MarchWipeDrop> drops)
	{
		StringBuilder sb = new StringBuilder(";march;wipe;");
		sb.append(succ?1:0).append(';');
		if (succ) {
			if( drops == null )
				sb.append(0).append(';');
			else
			{
				sb.append(drops.size()).append(';');
				for (Role.MarchWipeDrop d : drops) {
					sb.append(d.money).append(';');
					sb.append(d.point).append(';');
					if (d.drop == null)
						sb.append(0).append(';');
					else {
						sb.append(1).append(';');
						sb.append(d.drop.type).append(';');
						sb.append(d.drop.id).append(';');
						sb.append(d.drop.arg).append(';');
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeMarchFinishAttackRes(byte stage, MarchManager.AttackFinishRes res)
	{
		StringBuilder sb = new StringBuilder(";march;finishattack;");
		sb.append(stage).append(';');
		if( res == null )
			sb.append(0).append(';');
		else
		{
			sb.append(res.bWin?2:1).append(';');
			sb.append(res.stage).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeMarchStartAttackRes(byte stage, MarchManager.AttackStartRes res, short mercGid, int mercRid)
	{
		StringBuilder sb = new StringBuilder(";march;startattack;");
		sb.append(stage).append(';');
		if( res == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(res.roleID).append(';');
			sb.append(res.sgenerals.size()).append(';');
			for(DBRoleGeneral e : res.sgenerals)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				if (e.id == mercGid)
					sb.append(mercRid).append(';');					
				else
					sb.append(-1).append(';');
			}
			sb.append(res.sgeneralStates.size()).append(';');			
			for (SBean.DBRoleMarchGeneralState e : res.sgeneralStates)
			{
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}
			sb.append(res.pets.size()).append(';');			
			for (SBean.DBPetBrief e : res.pets)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, res.srelation);
			if (res.egenerals == null) {
				sb.append(0).append(';');				
			}
			else {
				sb.append(res.egenerals.size()).append(';');
				for(DBRoleGeneral e : res.egenerals)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.advLvl).append(';');
					sb.append(e.evoLvl).append(';');
					for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
					{
						sb.append(e.skills.get(i)).append(';');
					}
					for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
					{
						sb.append(e.equips.get(i).lvl).append(';');
						sb.append(e.equips.get(i).exp).append(';');
					}
					sb.append(e.weapon.lvl).append(';');
					if (e.weapon.lvl >= 0) {
						sb.append(e.weapon.evo).append(';');
						sb.append(e.weapon.passes.size()).append(';');
						for (SBean.DBWeaponProp prop : e.weapon.passes) {
							sb.append(prop.propId).append(';');					
							sb.append(prop.type).append(';');					
							sb.append(prop.value).append(';');					
							sb.append(prop.rank).append(';');					
						}
					}
					encodeGeneralSeyen(sb, e);
					encodeGeneralOfficial(sb, e);
				}
			}
			sb.append(res.egeneralStates.size()).append(';');			
			for (SBean.DBRoleMarchGeneralState e : res.egeneralStates)
			{
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}		
			if (res.epets == null) {
				sb.append(0).append(';');				
			}
			else {
				sb.append(res.epets.size()).append(';');			
				for (SBean.DBPetBrief e : res.epets)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.evoLvl).append(';');
					sb.append(e.growthRate).append(';');
					sb.append(e.name).append(';');
					sb.append(e.deformStage).append(';');
					sb.append(e.awakLvl).append(";");
				}				
			}
			encodeRelation(sb, res.erelation);
			encodeGStones(sb, res.sgStones);
			encodeGStones(sb, res.egStones);
		}
		return sb.toString();
	}
	
	public static String encodeSuraSyncRes(SBean.DBRoleSura sura, List<SBean.DBSuraEnemy> enemies, List<Short> gids, byte[] stageInfo, boolean mercExist)
	{
		StringBuilder sb = new StringBuilder(";sura;sync;");
		if( sura == null || enemies == null )
			sb.append(0).append(';');
		else
		{
			sb.append(stageInfo[0]>0?2:1).append(';');
			sb.append(stageInfo[1]+1).append(';');
			sb.append(mercExist?1:0).append(';');
			sb.append(sura.deadGids.size()).append(';');
			for (short gid : sura.deadGids)
				sb.append(gid).append(';');
			sb.append(sura.activatedGids.size()).append(';');
			for (short gid : sura.activatedGids)
				sb.append(gid).append(';');
			sb.append(gids.size()).append(';');
			for (short gid : gids)
				sb.append(gid).append(';');
			sb.append(enemies.size()).append(';');
			for (SBean.DBSuraEnemy e : enemies) {
				sb.append(e.id).append(';'); 
				sb.append(e.name).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.headIconID).append(';');
				sb.append(e.fname).append(';');
				if (e.generals != null) {
					sb.append(e.generals.size()).append(';');
					for (DBRoleGeneral g : e.generals) {
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						sb.append(g.weapon.lvl).append(';');
					}
				}
				else {
					sb.append(0).append(';');
				}
				if (e.pets != null) {
					sb.append(e.pets.size()).append(';');
					for (SBean.DBPetBrief p : e.pets) {
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
						sb.append(p.deformStage).append(';');
						sb.append(p.awakLvl).append(";");
					}				
				}
				else {
					sb.append(0).append(';');				
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuraFinishAttackRes(SuraManager.AttackFinishRes res)
	{
		StringBuilder sb = new StringBuilder(";sura;finishattack;");
		if( res == null )
			sb.append(0).append(';');
		else
		{
			sb.append(res.bWin?2:1).append(';');
			sb.append(res.stage+1).append(';');
			if (res.drops == null)
				sb.append(0).append(';');
			else {
				sb.append(res.drops.size()).append(';');
				for(SBean.DropEntryNew e : res.drops)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}				
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuraStartAttackRes(byte stage, SuraManager.AttackStartRes res, short mercGid, int mercRid)
	{
		StringBuilder sb = new StringBuilder(";sura;startattack;");
		sb.append(stage+1).append(';');
		if( res == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(res.sgenerals.size()).append(';');
			for(DBRoleGeneral e : res.sgenerals)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				if (e.id == mercGid)
					sb.append(mercRid).append(';');					
				else
					sb.append(-1).append(';');
			}
			sb.append(res.pets.size()).append(';');			
			for (SBean.DBPetBrief e : res.pets)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, res.srelation);
			encodeGStones(sb, res.sgStones);
			sb.append(res.enemies.size()).append(';');
			for (SuraManager.AttackStartEnemy enemy : res.enemies) {
				sb.append(enemy.erid).append(';');
				sb.append(enemy.egenerals.size()).append(';');
				for(DBRoleGeneral e : enemy.egenerals)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.advLvl).append(';');
					sb.append(e.evoLvl).append(';');
					for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
					{
						sb.append(e.skills.get(i)).append(';');
					}
					for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
					{
						sb.append(e.equips.get(i).lvl).append(';');
						sb.append(e.equips.get(i).exp).append(';');
					}
					sb.append(e.weapon.lvl).append(';');
					if (e.weapon.lvl >= 0) {
						sb.append(e.weapon.evo).append(';');
						sb.append(e.weapon.passes.size()).append(';');
						for (SBean.DBWeaponProp prop : e.weapon.passes) {
							sb.append(prop.propId).append(';');					
							sb.append(prop.type).append(';');					
							sb.append(prop.value).append(';');					
							sb.append(prop.rank).append(';');					
						}
					}
					encodeGeneralSeyen(sb, e);
					encodeGeneralOfficial(sb, e);
				}
				if (enemy.epets == null) {
					sb.append(0).append(';');				
				}
				else {
					sb.append(enemy.epets.size()).append(';');			
					for (SBean.DBPetBrief e : enemy.epets)
					{
						sb.append(e.id).append(';');
						sb.append(e.lvl).append(';');
						sb.append(e.evoLvl).append(';');
						sb.append(e.growthRate).append(';');
						sb.append(e.name).append(';');
						sb.append(e.deformStage).append(';');
						sb.append(e.awakLvl).append(";");
					}				
				}
				encodeRelation(sb, enemy.erelation);
				encodeGStones(sb, enemy.egStones);
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuraActivateRes(short gid, boolean succ)
	{
		StringBuilder sb = new StringBuilder(";sura;activate;");
		sb.append(gid).append(';');
		sb.append(succ?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaAttacked(boolean attacked)
	{
		StringBuilder sb = new StringBuilder(";arena;attacked;");
		sb.append(attacked?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaSyncInfo(Role.ArenaStateInfo info)
	{
		StringBuilder sb = new StringBuilder(";arena;sync;");
		sb.append(info.maxLvl).append(';');
		sb.append(info.maxWinTimes).append(';');
		sb.append(info.point).append(';');
		sb.append(info.rankBest).append(';');
		sb.append(info.rankNow).append(';');
		sb.append(info.rankReward).append(';');
		sb.append(info.timesUsed).append(';');
		sb.append(info.timesBuy).append(';');
		sb.append(info.power).append(';');
		sb.append(info.lastTime).append(';');
		for (int i = 0; i < 5; i ++)
			sb.append(info.generals[i]).append(';');
		sb.append(info.pets[0]).append(';');
		sb.append(info.enemies.size()).append(';');
		int index = 0;
		for(SBean.DBRoleBrief e : info.enemies)
		{
			sb.append(info.ranks[index]).append(';'); // rank
			sb.append(e.id).append(';'); 
			if( e.id > 0 )
			{
				sb.append(e.name).append(';');
				sb.append(e.flag).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.headIconID).append(';');
			}
			++index;
		}
		return sb.toString();
	}
	
	public static String encodeArenaSyncBrief(Role.ArenaStateInfo info)
	{
		StringBuilder sb = new StringBuilder(";arena;brief;");
		sb.append(info.rankNow).append(';');
		sb.append(info.power).append(';');
		for (int i = 0; i < 5; i ++)
			sb.append(info.generals[i]).append(';');
		sb.append(info.pets[0]).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaSyncRanks(List<Role.ArenaRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";arena;ranks;");
		sb.append(ranks.size()).append(';');
		for(Role.ArenaRank e : ranks)
		{
			sb.append(e.id).append(';'); 
			if( e.id > 0 )
			{
				sb.append(e.lvl).append(';');
				sb.append(e.name).append(';');
				sb.append(e.headIconID).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeArenaSyncLogs(List<Role.ArenaLog> logs)
	{
		StringBuilder sb = new StringBuilder(";arena;logs;");
		sb.append(logs.size()).append(';');
		for(Role.ArenaLog e : logs)
		{
			sb.append(e.win).append(';');
			sb.append(e.rankUp).append(';');
			sb.append(e.time).append(';');
			sb.append(e.recordId).append(';');
			sb.append(e.id).append(';'); 
			if( e.id > 0 )
			{
				sb.append(e.name).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.headIconID).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeArenaFinishAttackRes(int rank, ArenaManager.ActiveAttackFinishRes res, int bestRankUp, List<SBean.DropEntryNew> bestRankRewards)
	{
		StringBuilder sb = new StringBuilder(";arena;finishattack;");
		sb.append(rank).append(';');
		if( res == null )
			sb.append(0).append(';');
		else
		{
			sb.append(res.bWin?2:1).append(';');
			sb.append(res.rankUp).append(';');
			sb.append(bestRankUp).append(';');
			int money = 0;
			int stone = 0;
			if( bestRankRewards != null )
			{
				for(SBean.DropEntryNew e : bestRankRewards)
				{
					if( e.type == GameData.COMMON_TYPE_MONEY )
						money = e.arg;
					else if( e.type == GameData.COMMON_TYPE_STONE )
						stone = e.arg;
				}
			}
			sb.append(money).append(';');
			sb.append(stone).append(';');
		}
		return sb.toString();
	}
	
	private static void encodeRelation(StringBuilder sb, List<SBean.DBRelation> relation)
	{
		if (relation == null)
			sb.append(0).append(';');
		else {
			sb.append(relation.size()).append(';');
			for (SBean.DBRelation r : relation) {
				sb.append(r.id).append(';');
				sb.append(r.lvls.size()).append(';');
				for (short lvl : r.lvls)
					sb.append(lvl).append(';');
			}
		}
	}
	
	private static void encodeGStones(StringBuilder sb, List<SBean.DBGeneralStone> gStones)
	{
		if (gStones == null)
			sb.append(0).append(';');
		else {
			sb.append(gStones.size()).append(';');
			for (DBGeneralStone s : gStones) {
				sb.append(s.id).append(';');
				sb.append(s.itemId).append(';');
				sb.append(s.exp).append(';');
				sb.append(s.gid).append(';');
				sb.append(s.pos).append(';');
				sb.append(s.passes.size()).append(';');
				for (DBGeneralStoneProp prop : s.passes){
					sb.append(prop.propId).append(';');
					sb.append(prop.type).append(';');
					sb.append(prop.rank).append(';');
					sb.append(prop.value).append(';');
				}
//				sb.append(s.resetPasses.size()).append(';');
//				for (DBGeneralStoneProp prop : s.resetPasses){
//					sb.append(prop.propId).append(';');
//					sb.append(prop.type).append(';');
//					sb.append(prop.rank).append(';');
//					sb.append(prop.value).append(';');
//				}
			}
		}
	}
	
	public static String encodeArenaStartAttackRes(int rank, int err, ArenaManager.ActiveAttackStartRes res)
	{
		StringBuilder sb = new StringBuilder(";arena;startattack;");
		sb.append(rank).append(';');
		if( res == null ) {
			sb.append(0).append(';');
			sb.append(err).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(res.roleID).append(';');
			sb.append(res.roleName).append(';');
			sb.append(res.lvl).append(';');
			sb.append(res.headIconID).append(';');
			sb.append(res.seed).append(';');
			sb.append(res.sgenerals.size()).append(';');
			for(DBRoleGeneral e : res.sgenerals)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}	
			sb.append(res.pets.size()).append(';');
			for(DBPetBrief e : res.pets)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, res.srelation);
			encodeGStones(sb, res.sgStone);
			if( res.roleID > 0 )
			{
				sb.append(res.egenerals.size()).append(';');
				for(DBRoleGeneral e : res.egenerals)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.advLvl).append(';');
					sb.append(e.evoLvl).append(';');
					for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
					{
						sb.append(e.skills.get(i)).append(';');
					}
					for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
					{
						sb.append(e.equips.get(i).lvl).append(';');
						sb.append(e.equips.get(i).exp).append(';');
					}
					sb.append(e.weapon.lvl).append(';');
					if (e.weapon.lvl >= 0) {
						sb.append(e.weapon.evo).append(';');
						sb.append(e.weapon.passes.size()).append(';');
						for (SBean.DBWeaponProp prop : e.weapon.passes) {
							sb.append(prop.propId).append(';');					
							sb.append(prop.type).append(';');					
							sb.append(prop.value).append(';');					
							sb.append(prop.rank).append(';');					
						}
					}
					encodeGeneralSeyen(sb, e);
					encodeGeneralOfficial(sb, e);
				}
				sb.append(res.epets.size()).append(';');
				for(DBPetBrief e : res.epets)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.evoLvl).append(';');
					sb.append(e.growthRate).append(';');
					sb.append(e.name).append(';');
					sb.append(e.deformStage).append(';');
					sb.append(e.awakLvl).append(";");
				}
				encodeRelation(sb, res.erelation);		
				encodeGStones(sb, res.egStone);
			}
		}
		return sb.toString();
	}
	
	public static String encodeArenaCalcAttackResultReq(ArenaManager.CalcAttackStartReq req)
	{
		StringBuilder sb = new StringBuilder(";arena;calcattack;");
		sb.append(req.seed).append(';');
		sb.append(req.rid1).append(';');
		sb.append(req.rid2).append(';');
		sb.append(req.generals1.size()).append(';');
		for(DBRoleGeneral e : req.generals1)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
			{
				sb.append(e.skills.get(i)).append(';');
			}
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
			{
				sb.append(e.equips.get(i).lvl).append(';');
				sb.append(e.equips.get(i).exp).append(';');
			}
			sb.append(e.weapon.lvl).append(';');
			if (e.weapon.lvl >= 0) {
				sb.append(e.weapon.evo).append(';');
				sb.append(e.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : e.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, e);
			encodeGeneralOfficial(sb, e);
		}	
		sb.append(req.pets1.size()).append(';');
		for(DBPetBrief e : req.pets1)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.awakLvl).append(";");
		}
		encodeRelation(sb, req.relation1);
		sb.append(req.generals2.size()).append(';');
		for(DBRoleGeneral e : req.generals2)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
			{
				sb.append(e.skills.get(i)).append(';');
			}
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
			{
				sb.append(e.equips.get(i).lvl).append(';');
				sb.append(e.equips.get(i).exp).append(';');
			}
			sb.append(e.weapon.lvl).append(';');
			if (e.weapon.lvl >= 0) {
				sb.append(e.weapon.evo).append(';');
				sb.append(e.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : e.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, e);
			encodeGeneralOfficial(sb, e);
		}
		sb.append(req.pets2.size()).append(';');
		for(DBPetBrief e : req.pets2)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.awakLvl).append(";");
		}
		encodeRelation(sb, req.relation2);
		
		encodeGStones(sb, req.gStone1);
		encodeGStones(sb, req.gStone2);
		return sb.toString();
	}
	
	public static String encodeArenaClearCoolRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;cool;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaBuyTimesRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;buytimes;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaTakeRewardRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;reward;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeRankRes(byte rtype)
	{
		StringBuilder sb = new StringBuilder(";rank;sync;");
		sb.append(rtype).append(';');
		sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaViewRes(int rid, int rankNow, SBean.RoleDataRes res)
	{
		StringBuilder sb = new StringBuilder(";arena;view;");
		sb.append(rid).append(';');
		if( res.res == SBean.RoleDataRes.eOK && res.arenagenerals != null && res.brief != null )
		{
			sb.append(1).append(';');
			sb.append(res.brief.lvl).append(';');
			sb.append(res.brief.name).append(';');
			sb.append(rankNow).append(';');
			sb.append(res.brief.headIconID).append(';');
			sb.append(res.brief.fname).append(';');
			sb.append(res.brief.arenaPower).append(';');
			sb.append(res.brief.arenaWinTimes).append(';');
			sb.append(res.arenagenerals.size()).append(';');
			for(SBean.RoleDataGeneral g : res.arenagenerals)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');				
				sb.append(g.advLvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.weaponLvl).append(';');
				sb.append(g.weaponEvo).append(';');
			}
			sb.append(res.arenapets.size()).append(';');
			for(SBean.RoleDataPet p : res.arenapets)
			{
				sb.append(p.id).append(';');
				sb.append(p.lvl).append(';');				
				sb.append(p.evoLvl).append(';');
				sb.append(p.growthRate).append(';');
				sb.append(p.name).append(';');
			}
		}
		else
		{
			sb.append(0).append(';');	
		}
		return sb.toString();
	}
	
	public static String encodeArenaRefreshRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";arena;refresh;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaSetGeneralsRes(int err)
	{
		StringBuilder sb = new StringBuilder(";arena;setgenerals;");
		sb.append(err).append(';');
		return sb.toString();
	}
	
	public static String encodeArenaGetRecordRes(int recId, SBean.ArenaRecord record)
	{
		StringBuilder sb = new StringBuilder(";arena;getrecord;");
		if (record != null) {
			sb.append(1).append(';');
			sb.append(recId).append(';');
			sb.append(record.headIconId1).append(';');
			sb.append(record.headIconId2).append(';');
			sb.append(record.name1).append(';');
			sb.append(record.name2).append(';');
			sb.append(record.lvl1).append(';');
			sb.append(record.lvl2).append(';');
			sb.append(record.time).append(';');
			sb.append(record.seed).append(';');
			sb.append(record.generals1.size()).append(';');
			for(DBRoleGeneral e : record.generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(record.generals2.size()).append(';');
			for(DBRoleGeneral e : record.generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(record.pets1.size()).append(';');
			for(SBean.DBPetBrief g : record.pets1)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			sb.append(record.pets2.size()).append(';');
			for(SBean.DBPetBrief g : record.pets2)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			encodeRelation(sb, record.relation1);
			encodeRelation(sb, record.relation2);
			encodeGStones(sb, record.gStones1);
			encodeGStones(sb, record.gStones2);
		}
		else
			sb.append(0).append(';');
		return sb.toString();		
	}
	
	public static String encodeArenaGetHighRecordsRes(List<SBean.ArenaRecord> records, int total)
	{
		StringBuilder sb = new StringBuilder(";arena;gethighrecords;");
		sb.append(total).append(';');
		if (records != null) {
			sb.append(records.size()).append(';');
			for (SBean.ArenaRecord rec : records) {
				sb.append(rec.index).append(';');
				sb.append(rec.time).append(';');
				sb.append(rec.result).append(';');
				sb.append(rec.name1).append(';');
				sb.append(rec.name2).append(';');
				sb.append(rec.lvl1).append(';');
				sb.append(rec.lvl2).append(';');
				sb.append(rec.headIconId1).append(';');
				sb.append(rec.headIconId2).append(';');
				sb.append(rec.generals1.size()).append(';');
				for(DBRoleGeneral e : rec.generals1)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.advLvl).append(';');
					sb.append(e.evoLvl).append(';');
				}
				sb.append(rec.generals2.size()).append(';');
				for(DBRoleGeneral e : rec.generals2)
				{
					sb.append(e.id).append(';');
					sb.append(e.lvl).append(';');
					sb.append(e.advLvl).append(';');
					sb.append(e.evoLvl).append(';');
				}
				sb.append(rec.pets1.size()).append(';');
				for(SBean.DBPetBrief g : rec.pets1)
				{
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.evoLvl).append(';');
					sb.append(g.growthRate).append(';');
					sb.append(g.name).append(';');
					sb.append(g.deformStage).append(';');
					sb.append(g.awakLvl).append(";");
				}
				sb.append(rec.pets2.size()).append(';');
				for(SBean.DBPetBrief g : rec.pets2)
				{
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.evoLvl).append(';');
					sb.append(g.growthRate).append(';');
					sb.append(g.name).append(';');
					sb.append(g.deformStage).append(';');
					sb.append(g.awakLvl).append(";");
				}
				/*
				encodeRelation(sb, rec.relation1);
				encodeRelation(sb, rec.relation2);
				encodeGStones(sb, rec.gStones1);
				encodeGStones(sb, rec.gStones2);
				*/
			}
		}
		else
			sb.append(0).append(';');
			
		return sb.toString();
		
	}
	
	public static String encodeSuperArenaSyncInfo(Role.SuperArenaSyncInfo syncinfo)
	{
		StringBuilder sb = new StringBuilder(";superarena;sync;");
		sb.append(syncinfo.arenaRank).append(';');
		if (syncinfo.info == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(syncinfo.info.superarenaRank).append(';');
			
			sb.append(syncinfo.info.teams.size()).append(';');
			for (SBean.DBRoleArenaFightTeam t : syncinfo.info.teams)
			{
				sb.append(t.generals.size()).append(';');
				for (short gid : t.generals)
				{
					sb.append(gid).append(';');
				}
				sb.append(t.pets.size()).append(';');
				for (short pid : t.pets)
				{
					sb.append(pid).append(';');
				}
			}
			sb.append(syncinfo.info.timesUsed).append(';');
			sb.append(syncinfo.info.timesBuy).append(';');
			sb.append(syncinfo.info.lastFightTime).append(';');
			sb.append(syncinfo.info.point).append(';');
			sb.append(syncinfo.info.rankRewards).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaSyncRanks(List<Role.ArenaRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";superarena;ranks;");
		sb.append(ranks.size()).append(';');
		for(Role.ArenaRank e : ranks)
		{
			sb.append(e.id).append(';'); 
			sb.append(e.lvl).append(';');
			sb.append(e.name).append(';');
			sb.append(e.headIconID).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaTakeRankRewardRes(int point)
	{
		StringBuilder sb = new StringBuilder(";superarena;takereward;");
		sb.append(point).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaSyncLogs(List<Role.ArenaLog> logs)
	{
		StringBuilder sb = new StringBuilder(";superarena;logs;");
		sb.append(logs.size()).append(';');
		for(Role.ArenaLog e : logs)
		{
			sb.append(e.win).append(';');
			sb.append(e.rankUp).append(';');
			sb.append(e.time).append(';');
			sb.append(e.recordId).append(';');
			sb.append(e.id).append(';'); 
			sb.append(e.name).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.headIconID).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaClearCoolRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;cool;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaBuyTimesRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;buytimes;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaViewRes(int rid, int rankNow, SBean.RoleDataBrief brief, SBean.RoleDataSuperArena superArena)
	{
		StringBuilder sb = new StringBuilder(";superarena;view;");
		sb.append(rid).append(';');
		if( brief != null && superArena != null )
		{
			sb.append(1).append(';');
			sb.append(rankNow).append(';');
			sb.append(brief.lvl).append(';');
			sb.append(brief.name).append(';');
			sb.append(brief.headIconID).append(';');
			sb.append(brief.fname).append(';');
			sb.append(superArena.winTimes).append(';');
			sb.append(superArena.teams.size()).append(';');
			for (SBean.RoleDataFightTeam t : superArena.teams)
			{
				if (t != null)
				{
					sb.append(1).append(';');
					sb.append(t.generals.size()).append(';');
					for(SBean.RoleDataGeneral g : t.generals)
					{
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');				
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						sb.append(g.weaponLvl).append(';');
						sb.append(g.weaponEvo).append(';');
					}
					sb.append(t.pets.size()).append(';');
					for(SBean.RoleDataPet p : t.pets)
					{
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');				
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
					}
				}
				else
				{
					sb.append(0).append(';');
				}
			}
		}
		else
		{
			sb.append(0).append(';');	
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaSetGeneralsRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;setgenerals;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaGetHideRes(Role.SuperArenaTeamHideInfo info)
	{
		StringBuilder sb = new StringBuilder(";superarena;gethide;");
		if (info == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(info.hideTime).append(';');
			sb.append(info.hideTeams).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaSetHideRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;sethide;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaGetOrderRes(Role.SuperArenaOrderInfo info)
	{
		StringBuilder sb = new StringBuilder(";superarena;getorder;");
		if (info == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(info.curOrderSeq).append(';');
			sb.append(info.orders.size()).append(';');
			for (int o : info.orders)
			{
				sb.append(o).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaSetOrderRes(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";superarena;setorder;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeSuperArenaTargetsInfo(List<Role.SuperArenaTarget> targets)
	{
		StringBuilder sb = new StringBuilder(";superarena;search;");
		sb.append(targets.size()).append(';');
		for (Role.SuperArenaTarget t : targets)
		{
			if (t == null)
			{
				sb.append(0).append(';');
			}
			else
			{
				sb.append(1).append(';');
				sb.append(t.rank).append(';');
				sb.append(t.brief.id).append(';');
				sb.append(t.brief.lvl).append(';');
				sb.append(t.brief.name).append(';');
				sb.append(t.brief.headIconID).append(';');
				sb.append(t.brief.fname).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaStartAttackRes(int targetRid, int errCode, int seed, SBean.SuperArenaFightInfo[] fightInfo)
	{
		StringBuilder sb = new StringBuilder(";superarena;startattack;");
		sb.append(targetRid).append(';');
		sb.append(errCode).append(";");
		if (errCode == 0)
		{
			sb.append(seed).append(';');
			for (int idx = 0; idx < fightInfo.length; ++idx)
			{
				SBean.SuperArenaFightInfo info = fightInfo[idx];
				sb.append(info.rid).append(';');
				sb.append(info.name).append(';');
				sb.append(info.lvl).append(';');
				sb.append(info.headIconId).append(';');
				sb.append(info.teams.size()).append(';');
				for (SBean.SuperArenaTeamDetail t : info.teams)
				{
					sb.append(t.generals.size()).append(';');
					for (DBRoleGeneral g : t.generals)
					{
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
						{
							sb.append(g.skills.get(i)).append(';');
						}
						for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
						{
							sb.append(g.equips.get(i).lvl).append(';');
							sb.append(g.equips.get(i).exp).append(';');
						}
						sb.append(g.weapon.lvl).append(';');
						if (g.weapon.lvl >= 0) 
						{
							sb.append(g.weapon.evo).append(';');
							sb.append(g.weapon.passes.size()).append(';');
							for (SBean.DBWeaponProp prop : g.weapon.passes) 
							{
								sb.append(prop.propId).append(';');					
								sb.append(prop.type).append(';');					
								sb.append(prop.value).append(';');					
								sb.append(prop.rank).append(';');					
							}
						}
						encodeGeneralSeyen(sb, g);
						encodeGeneralOfficial(sb, g);
					}
					sb.append(t.pets.size()).append(';');
					for (DBPetBrief p : t.pets)
					{
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
						sb.append(p.deformStage).append(';');
						sb.append(p.awakLvl).append(";");
					}
					encodeRelation(sb, t.relation);
					encodeGStones(sb, t.gStones);
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaFinishAttackRes(int targetRid, int errorCode, int rankUp, int rewardPoint)
	{
		StringBuilder sb = new StringBuilder(";superarena;finishattack;");
		sb.append(targetRid).append(';');
		sb.append(errorCode).append(';');
		if (errorCode == 0)
		{
		    sb.append(rankUp).append(';');
		    sb.append(rewardPoint).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeSuperArenaGetRecordRes(int recId, SBean.SuperArenaRecord record)
	{
		StringBuilder sb = new StringBuilder(";superarena;getrecord;");
		if (record != null) 
		{
			sb.append(1).append(';');
			sb.append(record.id).append(';');
			sb.append(record.time).append(';');
			sb.append(record.result.size()).append(';');
			for (int ret : record.result)
			{
				sb.append(ret).append(';');
			}
			sb.append(record.seed).append(';');
			for (int idx = 0; idx < 2; ++idx)
			{
				SBean.SuperArenaFightInfo fteam = (idx == 0) ? record.fteam1 : record.fteam2;
				sb.append(fteam.rid).append(';');
				sb.append(fteam.name).append(';');
				sb.append(fteam.lvl).append(';');
				sb.append(fteam.headIconId).append(';');
				sb.append(fteam.teams.size()).append(';');
				for (SBean.SuperArenaTeamDetail t : fteam.teams)
				{
					sb.append(t.generals.size()).append(';');
					for (DBRoleGeneral g : t.generals)
					{
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
						{
							sb.append(g.skills.get(i)).append(';');
						}
						for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
						{
							sb.append(g.equips.get(i).lvl).append(';');
							sb.append(g.equips.get(i).exp).append(';');
						}
						sb.append(g.weapon.lvl).append(';');
						if (g.weapon.lvl >= 0) 
						{
							sb.append(g.weapon.evo).append(';');
							sb.append(g.weapon.passes.size()).append(';');
							for (SBean.DBWeaponProp prop : g.weapon.passes) 
							{
								sb.append(prop.propId).append(';');					
								sb.append(prop.type).append(';');					
								sb.append(prop.value).append(';');					
								sb.append(prop.rank).append(';');					
							}
						}
						encodeGeneralSeyen(sb, g);
						encodeGeneralOfficial(sb, g);
					}
					sb.append(t.pets.size()).append(';');
					for (DBPetBrief p : t.pets)
					{
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
						sb.append(p.deformStage).append(';');
						sb.append(p.awakLvl).append(";");
					}
					encodeRelation(sb, t.relation);
					encodeGStones(sb, t.gStones);
				}
			}
		}
		else
		{
			sb.append(0).append(';');
		}
		return sb.toString();		
	}
	
	public static String encodeSuperArenaAttacked(boolean attacked)
	{
		StringBuilder sb = new StringBuilder(";superarena;attacked;");
		sb.append(attacked?1:0).append(';');
		return sb.toString();
	}
	
	
	public static String encodeGMAnnounce(String msg, byte times)
	{
		StringBuilder sb = new StringBuilder(";gbt;anc");
		sb.append(';').append(msg);
		sb.append(';').append(times);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeWorldChat(LoginManager.WorldChatCache.Msg m)
	{
		StringBuilder sb = new StringBuilder(";worldchat;msg");
		sb.append(';').append(m.roleid);
		sb.append(';').append(m.lvlVIP);
		sb.append(';').append(m.rolename);
		sb.append(';').append(m.forcename);
		sb.append(';').append(m.lvl);
		sb.append(';').append(m.headIconID);
		sb.append(';').append(m.time);
		sb.append(';').append(m.msg);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeForceClose(byte err)
	{
		StringBuilder sb = new StringBuilder(";forceclose;");
		sb.append(err).append(';');
		return sb.toString();
	}
	
	public static String encodeCreateRole(byte err)
	{
		StringBuilder sb = new StringBuilder(";createrole;");
		sb.append(err).append(';');
		return sb.toString();
	}
	
	public static List<String> encodeUserLogin(byte err, int state, String reason)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("userlogin");
		lst.add(String.valueOf(err));
		lst.add(String.valueOf(state));
		lst.add(reason);
		return lst;
	}
	
	public static String encodeChangeName(byte err, String newName)
	{
		StringBuilder sb = new StringBuilder(";changename;");
		sb.append(err).append(';');
		sb.append(newName).append(';');
		return sb.toString();
	}
	
	public static String encodeGMCommandRes(Role.GMCommand cmd)
	{
		StringBuilder sb = new StringBuilder(";gmcmd;res;");
		sb.append(cmd.data).append(';');
		sb.append(cmd.command).append(';');
		sb.append(cmd.arg1).append(';');
		sb.append(cmd.arg2).append(';');
		sb.append(cmd.arg3).append(';');
		sb.append(cmd.res).append(';');
		return sb.toString();
	}
	
	public static String encodeAutoUserID(int uid)
	{
		StringBuilder sb = new StringBuilder(";autouid;");
		sb.append(uid).append(';');
		return sb.toString();
	}
	
	public static String encodeForceChat(LoginManager.WorldChatCache.Msg m)
	{
		StringBuilder sb = new StringBuilder(";forcechat;msg");
		sb.append(';').append(m.roleid);
		sb.append(';').append(m.lvlVIP);
		sb.append(';').append(m.rolename);
		sb.append(';').append(m.forcename);
		sb.append(';').append(m.lvl);
		sb.append(';').append(m.headIconID);
		sb.append(';').append(m.time);
		sb.append(';').append(m.msg);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodePrivateChat(LoginManager.WorldChatCache.Msg m)
	{
		StringBuilder sb = new StringBuilder(";privatechat;msg");
		sb.append(';').append(m.roleid);
		sb.append(';').append(m.lvlVIP);
		sb.append(';').append(m.rolename);
		sb.append(';').append(m.forcename);
		sb.append(';').append(m.lvl);
		sb.append(';').append(m.headIconID);
		sb.append(';').append(m.time);
		sb.append(';').append(m.msg);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeOpenWorldChat(List<LoginManager.WorldChatCache.Msg> msgs)
	{
		StringBuilder sb = new StringBuilder(";worldchat;open");
		sb.append(';').append(msgs.size());
		for(LoginManager.WorldChatCache.Msg m : msgs)
		{
			sb.append(';').append(m.roleid);
			sb.append(';').append(m.lvlVIP);
			sb.append(';').append(m.rolename);
			sb.append(';').append(m.forcename);
			sb.append(';').append(m.lvl);
			sb.append(';').append(m.headIconID);
			sb.append(';').append(m.time);
			sb.append(';').append(m.msg);
		}
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeWorldChatTestRes(boolean bOpen, int stamp)
	{
		StringBuilder sb = new StringBuilder(";worldchat;test;");
		sb.append(bOpen?stamp:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceChatTestRes(boolean bOpen, int stamp)
	{
		StringBuilder sb = new StringBuilder(";forcechat;test;");
		sb.append(bOpen?stamp:0).append(';');
		return sb.toString();
	}
	
	public static String encodeOpenForceChat(List<LoginManager.WorldChatCache.Msg> msgs)
	{
		StringBuilder sb = new StringBuilder(";forcechat;open");
		sb.append(';').append(msgs.size());
		for(LoginManager.WorldChatCache.Msg m : msgs)
		{
			sb.append(';').append(m.roleid);
			sb.append(';').append(m.lvlVIP);
			sb.append(';').append(m.rolename);
			sb.append(';').append(m.forcename);
			sb.append(';').append(m.lvl);
			sb.append(';').append(m.headIconID);
			sb.append(';').append(m.time);
			sb.append(';').append(m.msg);
		}
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeOpenPrivateChat(List<LoginManager.WorldChatCache.Msg> msgs)
	{
		StringBuilder sb = new StringBuilder(";privatechat;open");
		sb.append(';').append(msgs.size());
		for(LoginManager.WorldChatCache.Msg m : msgs)
		{
			sb.append(';').append(m.roleid);
			sb.append(';').append(m.lvlVIP);
			sb.append(';').append(m.rolename);
			sb.append(';').append(m.forcename);
			sb.append(';').append(m.lvl);
			sb.append(';').append(m.headIconID);
			sb.append(';').append(m.time);
			sb.append(';').append(m.msg);
		}
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeTestNewMail(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";mail;test");
		sb.append(';').append(bOK?1:0);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeMailSetRead(short mid, int res)
	{
		StringBuilder sb = new StringBuilder(";mail;setread");
		sb.append(';').append(mid);
		sb.append(';').append(res);
		sb.append(';');
		return sb.toString();
	}
	
	/*
	public static String encodeMailReadContent(short mid, String content, List<SBean.DropEntryNew> attLst)
	{
		StringBuilder sb = new StringBuilder(";mail;readcontent;");
		sb.append(mid).append(';');
		sb.append(content).append(';');
		sb.append(attLst.size()).append(';');
		for(SBean.DropEntryNew e : attLst)
		{
			sb.append(e.type).append(';');
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		
		return sb.toString();
	}
	*/
	
	public static List<String> encodeMailReadContent(short mid, String content, List<SBean.DropEntryNew> attLst)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("mail");
		lst.add("readcontent");
		
		lst.add(String.valueOf(mid));
		lst.add(content);
		lst.add(String.valueOf(attLst.size()));
		for(SBean.DropEntryNew e : attLst)
		{
			lst.add(String.valueOf(e.type));
			lst.add(String.valueOf(e.id));
			lst.add(String.valueOf(e.arg));
		}
		
		return lst;
	}
	
	public static String encodeMailAcceptFriend(int modTime, int res, byte acc)
	{
		StringBuilder sb = new StringBuilder(";mail;accfriend");
		sb.append(';').append(modTime);
		sb.append(';').append(res);
		sb.append(';').append(acc);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeMailNew()
	{
		StringBuilder sb = new StringBuilder(";mail;new");
		//sb.append(';').append(modTime);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeBroadcastIDs(List<Integer> ids)
	{
		StringBuilder sb = new StringBuilder(";gbt;new");
		sb.append(';').append(ids.size());
		for (int id : ids)
		{
			sb.append(';').append(id);
		}
		sb.append(';');
		return sb.toString();
	}
	
	public static List<String> encodeBroadcast(SBean.Broadcast broadcast)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("gbt");
		lst.add("sync");
		lst.add(String.valueOf(broadcast.id));
		lst.add(String.valueOf(broadcast.timeStart));
		lst.add(String.valueOf(broadcast.lifeTime));
		lst.add(String.valueOf(broadcast.freq));
		lst.add(String.valueOf(broadcast.times));
		lst.add(String.valueOf(broadcast.content));
		return lst;
	}
	
	public static List<String> encodeBanPlayInfo(List<LoginManager.BanInfo> banInfo)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("ban");
		lst.add("play");
		lst.add(String.valueOf(banInfo.size()));
		for (LoginManager.BanInfo e : banInfo)
		{
			lst.add(String.valueOf(e.banID));
			lst.add(String.valueOf(e.banLeftTime));
			lst.add(String.valueOf(e.reason));
		}
		return lst;
	}
	
	public static String encodeCheckinNotice(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";checkin;notice;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeActTipNotice(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";act;notice;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeMailSendRes(byte bOK)
	{
		StringBuilder sb = new StringBuilder(";mail;send");
		sb.append(';').append(bOK);
		sb.append(';');
		return sb.toString();
	}
	
	/*
	public static String encodeSyncMails(List<DBMail.Message> mails)
	{
		StringBuilder sb = new StringBuilder(";mail;sync;");
		sb.append(mails.size()).append(';');
		for(DBMail.Message m : mails)
		{
			sb.append(m.id).append(';');
			sb.append(m.type).append(';');
			sb.append(m.subType).append(';');
			sb.append(m.state).append(';');
			sb.append(m.sendTime).append(';');
			sb.append(m.srcID).append(';');
			sb.append(m.srcName).append(';');
			sb.append(m.title).append(';');
		}
		return sb.toString();
	}
	*/
	
	public static List<String> encodeSyncMails(List<DBMail.Message> mails)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("mail");
		lst.add("sync");
		lst.add(String.valueOf(mails.size()));
		for(DBMail.Message m : mails)
		{
			lst.add(String.valueOf(m.id));
			lst.add(String.valueOf(m.type));
			lst.add(String.valueOf(m.subType));
			lst.add(String.valueOf(m.state));
			lst.add(String.valueOf(m.sendTime));
			lst.add(String.valueOf(m.srcID));
			lst.add(String.valueOf(m.srcName));
			lst.add(String.valueOf(m.title));
		}
		return lst;
	}
	
	public static List<String> encodeWorldChatRet(int errCode, int banLeftTime, String reason, boolean normalMsg)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("worldchat");
		lst.add("msgret");
		lst.add(String.valueOf(errCode));
		lst.add(String.valueOf(banLeftTime));
		lst.add(reason);
		lst.add(String.valueOf(normalMsg?1:0));
		return lst;
	}
	
	public static List<String> encodeForceChatRet(int errCode, int banLeftTime, String reason)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("forcechat");
		lst.add("msgret");
		lst.add(String.valueOf(errCode));
		lst.add(String.valueOf(banLeftTime));
		lst.add(reason);
		return lst;
	}
	
	public static List<String> encodePrivateChatRet(int errCode, int banLeftTime, String reason)
	{
		List<String> lst = new ArrayList<String>();
		lst.add("privatechat");
		lst.add("msgret");
		lst.add(String.valueOf(errCode));
		lst.add(String.valueOf(banLeftTime));
		lst.add(reason);
		return lst;
	}
	
	public static String encodeMedalNotice(byte mid, byte seq)
	{
		StringBuilder sb = new StringBuilder(";medal;notice;");
		sb.append(mid).append(';');
		sb.append(seq).append(';');		
		return sb.toString();
	}
	
	public static String encodeVIPReward(byte lvl, SBean.CmnRewardCFGS r)
	{
		StringBuilder sb = new StringBuilder(";vip;reward;");
		sb.append(lvl).append(';');
		if( r == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(r.exp).append(';');
			sb.append(r.money).append(';');
			sb.append(r.stone).append(';');
			sb.append(r.rewards.size()).append(';');
			for(SBean.DropEntry e : r.rewards)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeMedalReward(byte mid, byte seq, SBean.CmnRewardCFGS r, boolean bCheck)
	{
		StringBuilder sb = new StringBuilder(";medal;reward;");
		sb.append(mid).append(';');
		sb.append(seq).append(';');
		sb.append(bCheck?1:0).append(';');
		if( r == null )
			sb.append(0).append(';');
		else
		{
			sb.append(1).append(';');
			sb.append(r.exp).append(';');
			sb.append(r.money).append(';');
			sb.append(r.stone).append(';');
			sb.append(r.rewards.size()).append(';');
			for(SBean.DropEntry e : r.rewards)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeZhenfaSetRes(short gid, short zid, byte eid, int res)
	{
		StringBuilder sb = new StringBuilder(";zhenfa;set;");
		sb.append(gid).append(';');
		sb.append(zid).append(';');
		sb.append(eid).append(';');
		sb.append(res).append(';');
		return sb.toString();
	}
	
	public static String encodeDailyTask2Info(int mcard, SBean.FreeVit[] fvs, List<SBean.DBDailyTask2Log> lst, int activity, List<Byte> actRewards)
	{
		StringBuilder sb = new StringBuilder(";dtask2;sync;");
		sb.append(mcard).append(';');
		if (fvs == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(fvs.length).append(';');
			for (int i = 0; i < fvs.length; ++i)
			{
				SBean.FreeVit fv = fvs[i];
				if (fv == null)
				{
					sb.append(0).append(';');
				}
				else
				{
					sb.append(i+1).append(';');
					sb.append(fv.startTime).append(';');
					sb.append(fv.endTime).append(';');
					sb.append(fv.vit).append(';');	
				}
			}
		}
		sb.append(lst.size()).append(';');
		for(SBean.DBDailyTask2Log e : lst)
		{
			sb.append(e.id).append(';');
			sb.append(e.times).append(';');
			sb.append(e.reward).append(';');
		}
		sb.append(activity).append(';');
		if (actRewards != null) {
			sb.append(actRewards.size()).append(';');
			for (byte b : actRewards)
				sb.append(b).append(';');
		}
		else
			sb.append(0).append(';');
			
		return sb.toString();
	}
	
	public static String encodeDailyTask2RewardRes(byte id, boolean bok, short lvlDay)
	{
		StringBuilder sb = new StringBuilder(";dtask2;reward;");
		sb.append(id).append(';');
		sb.append(bok?1:0).append(';');
		sb.append(lvlDay).append(';');
		return sb.toString();
	}
	
	public static String encodeDailyTask2Notice(boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";dtask2;notice;");
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeDailyActivityReward(int reward, boolean ok, List<SBean.DropEntryNew> drops)
	{
		StringBuilder sb = new StringBuilder(";dtask2;activityreward;");
		sb.append(reward).append(';');
		sb.append(ok?1:0).append(';');
		if (ok) {
			if (drops == null)
				sb.append(0).append(';');
			else {
				sb.append(drops.size()).append(';');
				for (SBean.DropEntryNew e : drops) {
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeDailyTaskRefreshStar(byte newStar)
	{
		StringBuilder sb = new StringBuilder(";dtask;refreshstar;");
		sb.append(newStar).append(';');
		return sb.toString();
	}
	
	public static String encodeSelfForceInfo(i3k.gs.Role.ForceInfo res)
	{
		StringBuilder sb = new StringBuilder(";force;syncself;");
		sb.append(res.id).append(';');
		sb.append(res.point).append(';');
		sb.append(res.contrib).append(';');
		sb.append(res.mobaiReward).append(';');
		sb.append(res.superMobai).append(';');
		sb.append(res.mobai.size()).append(';');
		for(int rid : res.mobai)
			sb.append(rid).append(';');
		sb.append(res.name).append(';');
		sb.append(res.joinTime).append(';');
		return sb.toString();
	}
	
	public static String encodeCreateForceRes(int fid)
	{
		StringBuilder sb = new StringBuilder(";force;create;");
		sb.append(fid).append(';');		
		return sb.toString();
	}
	
	public static String encodeQueryForceIDRes(int fid)
	{
		StringBuilder sb = new StringBuilder(";force;queryid;");
		sb.append(fid).append(';');		
		return sb.toString();
	}
	
	public static String encodeForceAnnounceRes(int fid, int ret)
	{
		StringBuilder sb = new StringBuilder(";force;announce");
		sb.append(';').append(fid);
		sb.append(';').append(ret);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeForceQQRes(int fid, int ret)
	{
		StringBuilder sb = new StringBuilder(";force;qq");
		sb.append(';').append(fid);
		sb.append(';').append(ret);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeForceMobaiRes(int fid, int rid, int type, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";force;mobai");
		sb.append(';').append(fid);
		sb.append(';').append(rid);
		sb.append(';').append(type);
		sb.append(';').append(bOK?1:0);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeForceMobaiRewardRes(int fid, int reward)
	{
		StringBuilder sb = new StringBuilder(";force;mobaireward");
		sb.append(';').append(fid);
		sb.append(';').append(reward);
		sb.append(';');
		return sb.toString();	
	}
	
	public static String encodeForceCmnRes(int fid, int ret)
	{
		StringBuilder sb = new StringBuilder(";force;cmnres");
		sb.append(';').append(fid);
		sb.append(';').append(ret);
		sb.append(';');
		return sb.toString();
	}
	
	public static String encodeRoleStoneSync(int stone)
	{
		StringBuilder sb = new StringBuilder(";rsync;stone;");
		sb.append(stone).append(';');
		return sb.toString();
	}
	
	public static String encodeRoleMoneySync(int money)
	{
		StringBuilder sb = new StringBuilder(";rsync;money;");
		sb.append(money).append(';');
		return sb.toString();
	}
	
	public static String encodeRoleVitSync(int vit)
	{
		StringBuilder sb = new StringBuilder(";rsync;vit;");
		sb.append(vit).append(';');
		return sb.toString();
	}
	
	public static String encodeRoleNameSync(String name)
	{
		StringBuilder sb = new StringBuilder(";rsync;name;");
		sb.append(name).append(';');
		return sb.toString();
	}
	
	public static String encodeRoleLvlExpSync(int lvl, int exp)
	{
		StringBuilder sb = new StringBuilder(";rsync;lvlexp;");
		sb.append(lvl).append(';');
		sb.append(exp).append(';');
		return sb.toString();
	}
	
	public static String encodeRolePayVipSync(int paytoatal, int viplvl)
	{
		StringBuilder sb = new StringBuilder(";rsync;payvip;");
		sb.append(paytoatal).append(';');
		sb.append(viplvl).append(';');
		return sb.toString();
	}
	
	public static String encodeForceInfo(int fid, ForceManager.ForceInfo info)
	{
		StringBuilder sb = new StringBuilder(";force;brief;");
		sb.append(fid).append(';');
		if( info == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(info.fname).append(';');
			sb.append(info.iconID).append(';');
			sb.append(info.lvl).append(';');
			sb.append(info.msg).append(';');
			sb.append(info.point).append(';');
			sb.append(info.contrib).append(';');
			sb.append(info.headID).append(';');
			sb.append(info.headName).append(';');
			sb.append(info.qq).append(';');
			sb.append(info.joinMode).append(';');
			sb.append(info.joinLvlReq).append(';');
			sb.append(info.sendMailCnt).append(';');
			sb.append(info.activity).append(';');
			sb.append(info.cupCount).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceStamp(int fid, int stamp)
	{
		StringBuilder sb = new StringBuilder(";force;stamp;");
		sb.append(fid).append(';');
		sb.append(stamp).append(';');
		return sb.toString();
	}
	
	public static String encodeForceJoin(int fid, byte res)
	{
		StringBuilder sb = new StringBuilder(";force;join;");
		sb.append(fid).append(';');
		sb.append(res).append(';');
		return sb.toString();
	}
	
	public static String encodeForceAccept(byte acc, int modTime, byte res)
	{
		StringBuilder sb = new StringBuilder(";force;accept;");
		sb.append(acc).append(';');
		sb.append(modTime).append(';');
		sb.append(res).append(';');
		return sb.toString();
	}
	
	public static String encodeEggTakeRes(byte eid, List<SBean.DropEntry> res)
	{
		StringBuilder sb = new StringBuilder(";egg;take;");
		sb.append(eid).append(';');
		if( res == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(res.size()).append(';');
			for(SBean.DropEntry e : res)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeEggTake10Res(byte eid, List<SBean.DropEntry> res)
	{
		StringBuilder sb = new StringBuilder(";egg;take10;");
		sb.append(eid).append(';');
		if( res == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(res.size()).append(';');
			for(SBean.DropEntry e : res)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeEggTake100Res(byte eid, List<SBean.DropEntry> res)
	{
		StringBuilder sb = new StringBuilder(";egg;take100;");
		sb.append(eid).append(';');
		if( res == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(res.size()).append(';');
			for(SBean.DropEntry e : res)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeTakeSoulBoxRes(byte sbid, List<SBean.DropEntry> res)
	{
		StringBuilder sb = new StringBuilder(";soulbox;take;");
		sb.append(sbid).append(';');
		if( res == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(res.size()).append(';');
			for(SBean.DropEntry e : res)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceGetPosRes(int fid, byte pos)
	{
		StringBuilder sb = new StringBuilder(";force;getpos;");
		sb.append(fid).append(';');
		sb.append(pos).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMembers(int fid, List<ForceManager.MemberInfo> members, List<DBForce.Manager> managers)
	{
		StringBuilder sb = new StringBuilder(";force;member;");
		sb.append(fid).append(';');
		if( members == null || managers == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(members.size()).append(';');
			for(ForceManager.MemberInfo e : members)
			{
				sb.append(e.id).append(';');
				sb.append(e.cache.lvl).append(';');
				sb.append(e.cache.headIconID).append(';');
				sb.append(e.cache.score).append(';');
				sb.append(e.cache.name).append(';');
				sb.append(e.cache.lastLoginTime).append(';');
				sb.append(e.joinTime).append(';');
				sb.append(e.point).append(';');
				sb.append(e.contrib).append(';');
				sb.append(e.cache.activity).append(';');
			}
			sb.append(managers.size()).append(';');
			for(DBForce.Manager e : managers)
			{
				sb.append(e.id).append(';');
				sb.append(e.pos).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceApply(int fid, List<ForceManager.ApplyInfo> members)
	{
		StringBuilder sb = new StringBuilder(";force;apply;");
		sb.append(fid).append(';');
		if( members == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(members.size()).append(';');
			for(ForceManager.ApplyInfo e : members)
			{
				sb.append(e.id).append(';');
				sb.append(e.cache.lvl).append(';');
				sb.append(e.cache.headIconID).append(';');
				sb.append(e.cache.name).append(';');
				sb.append(e.cache.score).append(';');
				sb.append(e.applyTime).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceLogs(int fid, List<DBForce.History> logs)
	{
		StringBuilder sb = new StringBuilder(";force;logs;");
		sb.append(fid).append(';');
		if( logs == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(logs.size()).append(';');
			for(DBForce.History e : logs)
			{
				sb.append(e.time).append(';');
				sb.append(e.msg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceActivity(int fid, int rid, int selfActivity, int activity, int forceActivity, List<Byte> activityRewards)
	{
		StringBuilder sb = new StringBuilder(";force;getactivity;");
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		sb.append(selfActivity).append(';');
		sb.append(activity).append(';');
		sb.append(forceActivity).append(';');
		if (activityRewards != null) {
			sb.append(activityRewards.size()).append(';');
			for (byte b : activityRewards)
				sb.append(b).append(';');
		}
		else
			sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceActivityReward(int rid, boolean ok, int money, SBean.DropEntry drop)
	{
		StringBuilder sb = new StringBuilder(";force;activityreward;");
		sb.append(rid).append(';');
		sb.append(ok?1:0).append(';');
		if (ok) {
			sb.append(money).append(';');
			sb.append((drop == null)?0:1).append(';');
			if (drop != null) {
				sb.append(drop.type).append(';');
				sb.append(drop.id).append(';');
				sb.append(drop.arg).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceList(List<ForceManager.ForceBrief> lst)
	{
		StringBuilder sb = new StringBuilder(";force;list;");
		if( lst == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(lst.size()).append(';');
			for(ForceManager.ForceBrief brief : lst)
			{
				sb.append(brief.fid).append(';');
				sb.append(brief.fname).append(';');
				sb.append(brief.iconID).append(';');
				sb.append(brief.lvl).append(';');
				sb.append(brief.members).append(';');
				sb.append(brief.msg).append(';');
				sb.append(brief.headID).append(';');
				sb.append(brief.headName).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeQueryForceRes(ForceManager.ForceBrief brief)
	{
		StringBuilder sb = new StringBuilder(";force;query;");
		if( brief == null )
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(brief.fid).append(';');
			sb.append(brief.fname).append(';');
			sb.append(brief.iconID).append(';');
			sb.append(brief.lvl).append(';');
			sb.append(brief.members).append(';');
			sb.append(brief.msg).append(';');
			sb.append(brief.headID).append(';');
			sb.append(brief.headName).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceSetJoin(int fid, int modTime, byte joinMode, short joinLvlReq)
	{
		StringBuilder sb = new StringBuilder(";force;setjoin;");
		sb.append(fid).append(';');
		sb.append(modTime).append(';');
		sb.append(joinMode).append(';');
		sb.append(joinLvlReq).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDismiss(int fid, int modTime, short itemId, int itemCount)
	{
		StringBuilder sb = new StringBuilder(";force;dismiss;");
		sb.append(fid).append(';');
		sb.append(modTime).append(';');
		sb.append(itemId).append(';');
		sb.append(itemCount).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSendMail(int fid, int errorCode)
	{
		StringBuilder sb = new StringBuilder(";force;mail;");
		sb.append(fid).append(';');
		sb.append(errorCode).append(';');
		return sb.toString();
	}
	
	public static String encodeForceInfoInit(int fid, String fname)
	{
		StringBuilder sb = new StringBuilder(";force;syncinit;");
		sb.append(fid).append(';');
		sb.append(fname).append(';');
		return sb.toString();
	}
	
	public static String encodeForceApplyExist(boolean applyExist)
	{
		StringBuilder sb = new StringBuilder(";force;applyexist;");
		sb.append(applyExist?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSetIcon(int modTime, int newicon)
	{
		StringBuilder sb = new StringBuilder(";force;seticon;");
		sb.append(modTime).append(';');
		sb.append(newicon).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSyncBattle(int fid, SBean.ForceBattleCFGS cfg, List<ForceWarManager.RolePlayerInfo> rps, SBean.DBForceBeast beast)
	{
		StringBuilder sb = new StringBuilder(";force;syncbattle;");
		sb.append(fid).append(';');
		if (cfg == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(cfg.id).append(';');
			sb.append(cfg.startTime).append(';');
			sb.append(cfg.endTime).append(';');
			sb.append(cfg.selectionTimeStart).append(';');
			sb.append(cfg.knockoutTimeStart).append(';');
			sb.append(cfg.knockout.size()).append(';');
			for (SBean.ForceBattleGameCFGS c : cfg.knockout)
			{
				sb.append(c.startTime).append(';');
				sb.append(c.endTime).append(';');
			}
			if (rps == null)
			{
				sb.append(0).append(';');
			}
			else
			{
				sb.append(1).append(';');
				sb.append(rps.size()).append(';');
				for (ForceWarManager.RolePlayerInfo player : rps)
				{
					sb.append(player.id).append(';');
					sb.append(player.name).append(';');
					sb.append(player.icon).append(';');
					sb.append(player.level).append(';');
					sb.append(player.power).append(';');
					sb.append(player.generals.size()).append(';');
					for(DBRoleGeneral g : player.generals)
					{
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.evoLvl).append(';');
						sb.append(g.advLvl).append(';');
					}
					sb.append(player.pets.size()).append(';');		
					for (SBean.DBPetBrief e : player.pets)
					{
						sb.append(e.id).append(';');
						sb.append(e.lvl).append(';');
						sb.append(e.evoLvl).append(';');
					}
				}
			}
		}
		if (beast == null)
			sb.append(0).append(';');
		else {
			sb.append(beast.general.id).append(';');
			sb.append(beast.general.lvl).append(';');
			sb.append(beast.general.evoLvl).append(';');
			for (int i = 0; i < 5; i ++)
				sb.append(beast.general.skills.get(i)).append(';');
			sb.append(beast.attackProp).append(';');
			for (int i = 0; i < 5; i ++)
				sb.append(beast.props.get(i)).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceBattleOpen(int fid, int bid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;openbattle;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceBattlePlayerJoin(int fid, int bid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;joinbattle;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceBattlePlayerQuit(int fid, int bid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;quitbattle;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceBattlePlayerPosChange(int fid, int bid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;battlechangeseq;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceBattleSyncContribution(int fid, int bid, int startTime, int endTime, int selectionTimeStart, List<ForceWarManager.PlusEffectItem> items)
	{
		StringBuilder sb = new StringBuilder(";force;synccontribution;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(startTime).append(';');
		sb.append(endTime).append(';');
		sb.append(selectionTimeStart).append(';');
		if (items == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(items.size()).append(';');
			for (ForceWarManager.PlusEffectItem e : items)
			{
				sb.append(e.id).append(';');
				sb.append(e.count).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceBattlePlayerContribute(int fid, int bid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;battlecontribute;");
		sb.append(fid).append(';');
		sb.append(bid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceBattleQuiz(int bid, int qid, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";force;battlequiz;");
		sb.append(bid).append(';');
		sb.append(qid).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static List<String> encodeSocialUserSyncInfo(int protocolVersion, SocialManager.SocialUserInfo info)
	{
		List<String> lst = new ArrayList<String>();
		lst.add(String.valueOf(protocolVersion));
		lst.add("sync");
		lst.add(String.valueOf(info.gsid));
		lst.add(String.valueOf(info.rid));
		lst.add(String.valueOf(info.exp));
		lst.add(String.valueOf(info.dayPostCount));
		lst.add(String.valueOf(info.dayLikeCount));
		return lst;
	}
	
	public static List<String> encodeSocialPageMsgInfo(int protocolVersion, int gsid, int rid, SocialManager.PageMsgInfo info)
	{
		final int Max_MSG_LENGTH = 80;
		List<String> lst = new ArrayList<String>();
		lst.add(String.valueOf(protocolVersion));
		lst.add("page");
		lst.add(String.valueOf(gsid));
		lst.add(String.valueOf(rid));
		lst.add(String.valueOf(info.cid));
		lst.add(String.valueOf(info.pageNo));
		lst.add(String.valueOf(info.pageCount));
		lst.add(String.valueOf(info.msgCountPerPage));
		lst.add(String.valueOf(info.msgs.size()));
		for (SocialManager.SocialMsgInfo e : info.msgs)
		{
			lst.add(String.valueOf(e.gsid));
			lst.add(String.valueOf(e.rid));
			lst.add(String.valueOf(e.gsname));
			lst.add(String.valueOf(e.name));
			lst.add(String.valueOf(e.exp));
			lst.add(String.valueOf(e.msgid));
			lst.add(e.msg.substring(0, e.msg.length() > Max_MSG_LENGTH ? Max_MSG_LENGTH : e.msg.length()));
			lst.add(String.valueOf(e.likeCount));
			lst.add(String.valueOf(e.selflike ? 1 : 0));
		}
		return lst;
	}
	
	public static List<String> encodeSendSocialMsg(int protocolVersion, SocialManager.PostMsgInfo info)
	{
		List<String> lst = new ArrayList<String>();
		lst.add(String.valueOf(protocolVersion));
		lst.add("send");
		lst.add(info.selfPost ? String.valueOf(1) : String.valueOf(0));
		lst.add(String.valueOf(info.exp));
		lst.add(String.valueOf(info.daySelfPostCount));
		return lst;
	}
	
	public static List<String> encodeLikeSocialMsg(int protocolVersion, SocialManager.LikeMsgInfo info)
	{
		List<String> lst = new ArrayList<String>();
		lst.add(String.valueOf(protocolVersion));
		lst.add("like");
		lst.add(String.valueOf(info.msgid));
		lst.add(info.selflike ? String.valueOf(1) : String.valueOf(0));
		lst.add(String.valueOf(info.exp));
		lst.add(String.valueOf(info.daySelfLikeCount));
		lst.add(String.valueOf(info.likeCount));
		return lst;
	}
	
//	public static String encodeNewCityOpen()
//	{
//		StringBuilder sb = new StringBuilder(";capturecity;new;");
//		sb.append(';');
//		return sb.toString();
//	}
	
	public static String encodeCitysSync(List<CityManager.CityBrief> baseCitys, List<CityManager.CityBrief> occupiedCitys, boolean canTakeOccupyReward)
	{
		StringBuilder sb = new StringBuilder(";capturecity;sync;");
		encodeCityBrief(sb, baseCitys, occupiedCitys);
		sb.append(canTakeOccupyReward ? 1 : 0).append(';');
		return sb.toString();
	}
	
	public static String encodeCitysNotice(boolean canNotice)
	{
		StringBuilder sb = new StringBuilder(";capturecity;notice;");
		sb.append(canNotice ? 1 : 0).append(';');
		return sb.toString();
	}
	
	public static String encodeCitysBrief(List<CityManager.CityBrief> baseCitys, List<CityManager.CityBrief> occupiedCitys)
	{
		StringBuilder sb = new StringBuilder(";capturecity;brief;");
		encodeCityBrief(sb, baseCitys, occupiedCitys);
		return sb.toString();
	}
	
	public static StringBuilder encodeCityBrief(StringBuilder sb, List<CityManager.CityBrief> baseCitys, List<CityManager.CityBrief> occupiedCitys)
	{
		encodeCityBrief(sb, baseCitys);
		encodeCityBrief(sb, occupiedCitys);
		return sb;
	}
	
	public static StringBuilder encodeCityBrief(StringBuilder sb, List<CityManager.CityBrief> citys)
	{
		sb.append(citys.size()).append(';');
		for (CityManager.CityBrief e : citys)
		{
			sb.append(e.baseGSID).append(';');
			sb.append(e.baseRoleID).append(';');
			sb.append(e.baseRoleName).append(';');
			sb.append(e.cityID).append(';');
			sb.append(e.ownerGSID).append(';');
			sb.append(e.ownerRoleID).append(';');
			sb.append(e.ownerRoleName).append(';');
			sb.append(e.guardStartTime).append(';');
			sb.append(e.guardLifeTime).append(';');
			sb.append(e.gids.size()).append(';');
			for (short ee : e.gids)
			{
				sb.append(ee).append(';');
			}
			sb.append(e.pids.size()).append(';');
			for (short ee : e.pids)
			{
				sb.append(ee).append(';');
			}
			sb.append(e.hasRewards ? 1 : 0).append(';');
		}
		return sb;
	}
	
	
	public static StringBuilder encodeCityInfo(StringBuilder sb, SBean.CityInfo info)
	{
		sb.append(info.baseRoleID.serverID).append(';');
		sb.append(info.baseRoleID.roleID).append(';');
		sb.append(info.cityID).append(';');
		sb.append(info.ownerRoleID.serverID).append(';');
		sb.append(info.ownerRoleID.roleID).append(';');
		sb.append(info.ownerRoleName).append(';');
		sb.append(info.guardStartTime).append(';');
		sb.append(info.guardLifeTime).append(';');
		sb.append(info.guardGenerals.size()).append(';');
		for(DBRoleGeneral g : info.guardGenerals)
		{
			sb.append(g.id).append(';');
			sb.append(g.lvl).append(';');
			sb.append(g.exp).append(';');
			sb.append(g.advLvl).append(';');
			sb.append(g.evoLvl).append(';');
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
			{
				sb.append(g.skills.get(i)).append(';');
			}
			for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
			{
				sb.append(g.equips.get(i).lvl).append(';');
				sb.append(g.equips.get(i).exp).append(';');
			}
			sb.append(g.weapon.lvl).append(';');
			if (g.weapon.lvl >= 0) {
				sb.append(g.weapon.evo).append(';');
				sb.append(g.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : g.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, g);
			encodeGeneralOfficial(sb, g);
			encodeGStones(sb, g.generalStone);
		}
		sb.append(info.guardPets.size()).append(';');			
		for (SBean.DBPetBrief e : info.guardPets)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.name).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.awakLvl).append(";");
		}
		return sb;
	}
	
	public static String encodeCitySelfSync(CityManager.InvokeRes<SBean.CityInfo> res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;cityself;");
		encodeCityDetail(sb, res);
		return sb.toString();
	}
	
	public static String encodeCityOtherSync(CityManager.InvokeRes<SBean.CityInfo> res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;cityother;");
		encodeCityDetail(sb, res);
		return sb.toString();
	}
	
	public static StringBuilder encodeCityDetail(StringBuilder sb, CityManager.InvokeRes<SBean.CityInfo> res)
	{
		sb.append(res.error).append(';');
		if (res.error == 0)
		{
			encodeCityInfo(sb, res.info);
		}
		return sb;
	}
	
	public static String encodeSearchCity(SBean.SearchCityRes res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;search;");
		sb.append(res.error).append(';');
		if (res.error == 0)
		{
			encodeCityInfo(sb, res.cityInfo);
		}
		return sb.toString();
	}
	
	public static String encodeGuardSelfCity(int error)
	{
		StringBuilder sb = new StringBuilder(";capturecity;guardself;");
		sb.append(error).append(';');
		return sb.toString();
	}
	
	public static String encodeGuardOtherCity(int error)
	{
		StringBuilder sb = new StringBuilder(";capturecity;guardother;");
		sb.append(error).append(';');
		return sb.toString();
	}
	
	public static String encodeGuardRobotCity(int error)
	{
		StringBuilder sb = new StringBuilder(";capturecity;guardrobot;");
		sb.append(error).append(';');
		return sb.toString();
	}
	
	public static String encodeAttackCityStart(SBean.AttackCityStartRes res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;attackstart;");
		sb.append(res.error).append(';');
		if (res.error == 0)
		{
			encodeCityInfo(sb, res.cityInfo);
		}
		return sb.toString();
	}
	
	public static String encodeAttackCityEnd(SBean.AttackCityEndRes res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;attackend;");
		sb.append(res.error).append(';');
		if (res.error == 0)
		{
			encodeCityInfo(sb, res.cityInfo);
		}
		return sb.toString();
	}
	
	public static String encodeQueryCityOwner(SBean.QueryCityOwnerRes res)
	{
		StringBuilder sb = new StringBuilder(";capturecity;cityowner;");
		sb.append(res.error).append(';');
		if (res.error == 0)
		{
			encodeCityInfo(sb, res.cityInfo);	
		}
		return sb.toString();
	}
	
	public static String encodeTakeCitySelfRewards(CityManager.CityReward creward)
	{
		StringBuilder sb = new StringBuilder(";capturecity;rewardself;");
		sb.append(creward.rewards.size()).append(';');
		for (SBean.DropEntryNew e : creward.rewards)
		{
			sb.append(e.type).append(';');
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		sb.append(creward.nTimesStone).append(';');
		sb.append(creward.nTimesMoney).append(';');
		sb.append(creward.nTimesItem).append(';');
		return sb.toString();
	}
	
	public static String encodeTakeCityOtherRewards(CityManager.CityReward creward)
	{
		StringBuilder sb = new StringBuilder(";capturecity;rewardother;");
		sb.append(creward.rewards.size()).append(';');
		for (SBean.DropEntryNew e : creward.rewards)
		{
			sb.append(e.type).append(';');
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		sb.append(creward.nTimesStone).append(';');
		sb.append(creward.nTimesMoney).append(';');
		sb.append(creward.nTimesItem).append(';');
		return sb.toString();
	}
	
	public static String encodeSyncCityRewardLog(CityManager.CityRewardLog log)
	{
		StringBuilder sb = new StringBuilder(";capturecity;rewardlog;");
		sb.append(log.rewards.size()).append(';');
		for (SBean.DBCityReward r : log.rewards)
		{
			sb.append(r.baseGSID).append(';');
			sb.append(r.baseRoleID).append(';');
			sb.append(r.baseRoleName).append(';');
			sb.append(r.cityID).append(';');
			sb.append(r.guardStartTime).append(';');
			sb.append(r.guardLifeTime).append(';');
			sb.append(r.totalIncome.size()).append(';');
			for (SBean.DropEntryNew e : r.totalIncome)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');	
			}
		}
		sb.append(log.nTimesStone).append(';');
		sb.append(log.nTimesMoney).append(';');
		sb.append(log.nTimesItem).append(';');
		return sb.toString();
	}
	
	public static String encodeForceWarCalcReq(ForceWarManager.CalcReq req)
	{
		StringBuilder sb = new StringBuilder(";force;warcalc;");
		sb.append(req.seed).append(';');
		sb.append(req.fid1).append(';');
		sb.append(req.fid2).append(';');
		sb.append(req.rIndex1).append(';');
		sb.append(req.rIndex2).append(';');
		for(int i = 0; i < 3; i ++)
			sb.append(req.eff1[i]).append(';');
		for(int i = 0; i < 3; i ++)
			sb.append(req.eff2[i]).append(';');
		sb.append(req.generals1.size()).append(';');
		for(DBRoleGeneral e : req.generals1)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.skills.size()).append(';');
			for(short skill : e.skills)
			{
				sb.append(skill).append(';');
			}
			sb.append(e.equips.size()).append(';');
			for(SBean.DBGeneralEquip equip : e.equips)
			{
				sb.append(equip.lvl).append(';');
				sb.append(equip.exp).append(';');
			}
			sb.append(e.weapon.lvl).append(';');
			if (e.weapon.lvl >= 0) {
				sb.append(e.weapon.evo).append(';');
				sb.append(e.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : e.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, e);
			encodeGeneralOfficial(sb, e);
			encodeGStones(sb, e.generalStone);
		}
		sb.append(req.status1.size()).append(';');
		for (SBean.DBForceWarGeneralStatus e : req.status1) {
			sb.append(e.id).append(';');
			sb.append(e.hp).append(';');
			sb.append(e.mp).append(';');
			sb.append(e.state).append(';');
		}
		sb.append(req.pets1.size()).append(';');
		for(DBPetBrief e : req.pets1)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.awakLvl).append(";");
		}
		encodeRelation(sb, req.relation1);
		sb.append(req.generals2.size()).append(';');
		for(DBRoleGeneral e : req.generals2)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.skills.size()).append(';');
			for(short skill : e.skills)
			{
				sb.append(skill).append(';');
			}
			sb.append(e.equips.size()).append(';');
			for(SBean.DBGeneralEquip equip : e.equips)
			{
				sb.append(equip.lvl).append(';');
				sb.append(equip.exp).append(';');
			}
			sb.append(e.weapon.lvl).append(';');
			if (e.weapon.lvl >= 0) {
				sb.append(e.weapon.evo).append(';');
				sb.append(e.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : e.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
				}
			}
			encodeGeneralSeyen(sb, e);
			encodeGeneralOfficial(sb, e);
			encodeGStones(sb, e.generalStone);
		}
		sb.append(req.status2.size()).append(';');
		for (SBean.DBForceWarGeneralStatus e : req.status2) {
			sb.append(e.id).append(';');
			sb.append(e.hp).append(';');
			sb.append(e.mp).append(';');
			sb.append(e.state).append(';');
		}
		sb.append(req.pets2.size()).append(';');
		for(DBPetBrief e : req.pets2)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.evoLvl).append(';');
			sb.append(e.growthRate).append(';');
			sb.append(e.deformStage).append(';');
			sb.append(e.awakLvl).append(";");
		}
		encodeRelation(sb, req.relation2);
		
		return sb.toString();
	}
	
	private static void encodeForceWarData(StringBuilder sb, ForceWarManager.WarData d)
	{
		sb.append(d.fid1).append(';');
		sb.append(d.fid2).append(';');
		sb.append(d.name1).append(';');
		sb.append(d.name2).append(';');
		sb.append(d.headName1).append(';');
		sb.append(d.headName2).append(';');
		sb.append(d.icon1).append(';');
		sb.append(d.icon2).append(';');
		sb.append(d.win?1:0).append(';');
		sb.append(d.quizfid).append(';');
		sb.append(d.matchData.size()).append(';');
		for (ForceWarManager.WarMatchData m : d.matchData) {
			sb.append(m.roleName1).append(';');
			sb.append(m.roleName2).append(';');
			sb.append(m.roleIndex1).append(';');
			sb.append(m.roleIndex2).append(';');
			sb.append(m.win?1:0).append(';');
		}
	}
	
	public static String encodeForceWarLeagueDataStartRes()
	{
		StringBuilder sb = new StringBuilder(";force;warleaguedatastart;");
		return sb.toString();
	}
	
	public static String encodeForceWarLeagueDataEndRes()
	{
		StringBuilder sb = new StringBuilder(";force;warleaguedataend;");
		return sb.toString();
	}
	
	public static String encodeForceWarLeagueDataRes(ForceWarManager.WarData data)
	{
		StringBuilder sb = new StringBuilder(";force;warleaguedata;");
		encodeForceWarData(sb, data);
		return sb.toString();
	}
	
	public static String encodeForceWarCupDataStartRes()
	{
		StringBuilder sb = new StringBuilder(";force;warcupdatastart;");
		return sb.toString();
	}
	
	public static String encodeForceWarCupDataEndRes()
	{
		StringBuilder sb = new StringBuilder(";force;warcupdataend;");
		return sb.toString();
	}
	
	public static String encodeForceWarCupDataRes(ForceWarManager.WarData data)
	{
		StringBuilder sb = new StringBuilder(";force;warcupdata;");
		encodeForceWarData(sb, data);
		return sb.toString();
	}
	
	public static String encodeForceWarGetRecordRes(int fid1, int fid2, int index1, int index2, SBean.DBForceWarMatchRecord record, boolean reverse)
	{
		StringBuilder sb = new StringBuilder(";force;wargetrecord;");
		if (record != null) {
			sb.append(1).append(';');
			sb.append(fid1).append(';');
			sb.append(fid2).append(';');
			sb.append(index1).append(';');
			sb.append(index2).append(';');
			sb.append(record.name1).append(';');
			sb.append(record.name2).append(';');
			sb.append(record.seed).append(';');
			for (int i = 0; i < 3; i ++)
				sb.append(record.eff1.get(i)).append(';');
			for (int i = 0; i < 3; i ++)
				sb.append(record.eff2.get(i)).append(';');
			sb.append(record.generals1.size()).append(';');
			for(DBRoleGeneral e : record.generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.skills.size()).append(';');
				for(short skill : e.skills)
				{
					sb.append(skill).append(';');
				}
				sb.append(e.equips.size()).append(';');
				for(SBean.DBGeneralEquip equip : e.equips)
				{
					sb.append(equip.lvl).append(';');
					sb.append(equip.exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			sb.append(record.generals2.size()).append(';');
			for(DBRoleGeneral e : record.generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.skills.size()).append(';');
				for(short skill : e.skills)
				{
					sb.append(skill).append(';');
				}
				sb.append(e.equips.size()).append(';');
				for(SBean.DBGeneralEquip equip : e.equips)
				{
					sb.append(equip.lvl).append(';');
					sb.append(equip.exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			sb.append(record.pets1.size()).append(';');
			for(SBean.DBPetBrief g : record.pets1)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			sb.append(record.pets2.size()).append(';');
			for(SBean.DBPetBrief g : record.pets2)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			sb.append(record.status1.size()).append(';');
			for (SBean.DBForceWarGeneralStatus e : record.status1) {
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}
			sb.append(record.status2.size()).append(';');
			for (SBean.DBForceWarGeneralStatus e : record.status2) {
				sb.append(e.id).append(';');
				sb.append(e.hp).append(';');
				sb.append(e.mp).append(';');
				sb.append(e.state).append(';');
			}
			encodeRelation(sb, record.relation1);
			encodeRelation(sb, record.relation2);
//			encodeGStones(sb, record.gStones1);
//			encodeGStones(sb, record.gStones2);
		}
		else
			sb.append(0).append(';');
		sb.append(reverse?1:0).append(';');		
		return sb.toString();		
	}
	
	public static String encodeForceWarScoreRanksRes(int fid, List<ForceWarManager.ScoreRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;warscoreranks;");
		sb.append(fid).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (ForceWarManager.ScoreRank r : ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.fid).append(';');
				sb.append(r.score).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceWarPowerRanksRes(int fid, List<ForceWarManager.PowerRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;warpowerranks;");
		sb.append(fid).append(';');
		if (ranks == null)
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			if (size > 32)
				size = 32;
			int i = 0;
			sb.append(size).append(';');
			for (ForceWarManager.PowerRank r : ranks) {
				sb.append(r.rid).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.power).append(';');
				i ++;
				if (i >= size)
					break;
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceWarRoleScoreRanksRes(int fid, List<ForceWarManager.RoleScoreRank> ranks)
	{
//		if(ranks!=null&&ranks.size()>1){
//			Collections.sort(ranks,new Comparator<ForceWarManager.RoleScoreRank>(){
//				public int compare(ForceWarManager.RoleScoreRank o1, ForceWarManager.RoleScoreRank o2) {
//					
//					if(o2.score==o1.score){
//						return o2.power-o1.power;
//					}
//					return o2.score-o1.score;
//				}
//			});
//		}
		
		StringBuilder sb = new StringBuilder(";force;warrolescoreranks;");
		sb.append(fid).append(';');
		if (ranks == null)
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			if (size > 32)
				size = 32;
			int i = 0;
			sb.append(size).append(';');
			for (ForceWarManager.RoleScoreRank r : ranks) {
				sb.append(r.rid).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.score).append(';');
				sb.append(0).append(';');
				i ++;
				if (i >= size)
					break;
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceWarDonateRanksRes(int fid, List<ForceWarManager.DonateRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;wardonateranks;");
		sb.append(fid).append(';');
		if (ranks == null)
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			if (size > 32)
				size = 32;
			sb.append(size).append(';');
			int i = 0;
			for (ForceWarManager.DonateRank r : ranks) {
				sb.append(r.rid).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.donate).append(';');
				i ++;
				if (i >= size)
					break;
			}
		}
		return sb.toString();	
	}
	
	public static String encodeForceWarCupWinRanksRes(int fid, List<ForceWarManager.CupWinRank> ranks)
	{
		
//		if(ranks!=null&&ranks.size()>1){
//			Collections.sort(ranks,new Comparator<ForceWarManager.CupWinRank>(){
//				public int compare(ForceWarManager.CupWinRank o1, ForceWarManager.CupWinRank o2) {
//					
//					if(o2.cupWin==o1.cupWin){
//						return o2.power-o1.power;
//					}
//					return o2.cupWin-o1.cupWin;
//				}
//			});
//		}
		
		StringBuilder sb = new StringBuilder(";force;warcupwinranks;");
		sb.append(fid).append(';');
		if (ranks == null)
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			if (size > 32)
				size = 32;
			int i = 0;
			sb.append(size).append(';');
			for (ForceWarManager.CupWinRank r : ranks) {
				sb.append(r.rid).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.cupWin).append(';');
				sb.append(0).append(';');
				i ++;
				if (i >= size)
					break;
			}
		}
		return sb.toString();	
	}
	
	public static String encodeInvitationTaskInfo(List<SBean.DBInvitationTasks> info)
	{
		StringBuilder sb = new StringBuilder(";invitation;sync;");
		sb.append(info.size()).append(';');
		for (SBean.DBInvitationTasks e : info)
		{
			sb.append(e.type).append(';');
			sb.append(e.points).append(';');
			sb.append(e.tasks.size()).append(';');
			for (SBean.DBInvitationGroupTask t : e.tasks)
			{
				sb.append(t.groupId).append(';');
				sb.append(t.reqCurVal).append(';');
				sb.append(t.seq).append(';');
			}
			sb.append(e.pointsReward.size()).append(';');
			for (int t : e.pointsReward)
			{
				sb.append(t).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeInvitationTakeTaskReward(int type, int gid, int seq, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";invitation;taketaskreward;");
		sb.append(type).append(";");
		sb.append(gid).append(";");
		sb.append(seq).append(";");
		sb.append(bOK?1:0).append(";");
		return sb.toString();
	}
	
	public static String encodeInvitationTakePointsReward(int type, int rid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";invitation;takepointsreward;");
		sb.append(type).append(";");
		sb.append(rid).append(";");
		sb.append(bOK?1:0).append(";");
		return sb.toString();
	}
	
	public static String encodeInvitationFriendsInfo(List<SBean.DBInvitationFriend> info)
	{
		StringBuilder sb = new StringBuilder(";invitation;friends;");
		sb.append(info.size()).append(';');
		for (SBean.DBInvitationFriend e : info)
		{
			sb.append(e.gsid).append(';');
			sb.append(e.roleid).append(';');
			sb.append(e.rolename).append(';');
			sb.append(e.level).append(';');
			sb.append(e.vip).append(';');
			sb.append(e.icon).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeInvitationInputFriendCode(int errorCode)
	{
		StringBuilder sb = new StringBuilder(";invitation;code;");
		sb.append(errorCode).append(";");
		return sb.toString();
	}
	
	public static String encodeInvitationSelfCode(String code)
	{
		StringBuilder sb = new StringBuilder(";invitation;self;");
		sb.append(code).append(";");
		return sb.toString();
	}
	
	public static void encodeRichRoleInfo(StringBuilder sb, SBean.DBRichRole role)
	{
		sb.append(role.gender).append(';');
		sb.append(role.mapType).append(';');
		sb.append(role.pos).append(';');
		sb.append(role.minipos).append(';');
		sb.append(role.gold).append(';');
		sb.append(role.goldTotal).append(';');
		sb.append(role.movement).append(';');
		sb.append(role.angelLeft).append(';');
		sb.append(role.devilLeft).append(';');
		sb.append(role.turtleLeft).append(';');
		sb.append(role.boomLeft).append(';');
		sb.append(role.buyMovementTimes).append(';');
		int bits = 8;
		sb.append(bits).append(';');
		for (int i = 0; i < bits; i ++)
			sb.append((role.status >> i) & 1).append(';');
		sb.append(role.bossTimes).append(';');
		sb.append(role.lotery.size()).append(';');
		for (byte l : role.lotery)
			sb.append(l).append(';');		
	}
	
	public static String encodeRichSync(SBean.DBRichRole role, String objectStr, byte building)
	{
		StringBuilder sb = new StringBuilder(";rich;sync;");
		if (role == null || role.map == null || objectStr == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			encodeRichRoleInfo(sb, role);
			sb.append(role.map.buildings.size()).append(';');
			for (SBean.DBRichMapBuilding b : role.map.buildings) {
				sb.append(b.type).append(';');
				sb.append(b.status).append(';');
				sb.append(b.params.size()).append(';');
				for (int param : b.params)
					sb.append(param).append(';');
			}
			sb.append(role.map.objects.size()).append(';');
			for (SBean.DBRichMapObject o : role.map.objects) {
				sb.append(o.type).append(';');
				sb.append(o.pos).append(';');
			}
			sb.append(role.mapExt.size()).append(';');
			if (role.mapExt.size() > 0) {
				SBean.DBRichMapExt ext = role.mapExt.get(0);
				sb.append(ext.objects.size()).append(';');
				for (SBean.DBRichMapObjectExt o : ext.objects) {
					sb.append(o.type).append(';');
					sb.append(o.pos).append(';');
					List<Integer> params = RichManager.getObjectSyncParam(o);
					sb.append(params.size()).append(';');
					for (int param : params)
						sb.append(param).append(';');
				}
			}
			sb.append(role.map.miniobjects.size()).append(';');
			for (SBean.DBRichMapObject o : role.map.miniobjects) {
				sb.append(o.type).append(';');
				sb.append(o.pos).append(';');
			}
			sb.append(role.lotery.size()).append(';');
			for (byte l : role.lotery)
				sb.append(l).append(';');
			sb.append(building).append(';');
			sb.append(objectStr).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRichDice(int cheat, SBean.DBRichRole role, String objectStr, byte building)
	{
		StringBuilder sb = new StringBuilder(";rich;dice;");
		sb.append(cheat).append(';');
		if (role == null || objectStr == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			encodeRichRoleInfo(sb, role);
			sb.append(building).append(';');
			sb.append(objectStr).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRichHandleObj(SBean.DBRichRole role, String objectStr)
	{
		StringBuilder sb = new StringBuilder(";rich;handleobj;");
		if (role == null || objectStr == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			encodeRichRoleInfo(sb, role);
			sb.append(objectStr).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRichSetGender(byte gender, int result)
	{
		StringBuilder sb = new StringBuilder(";rich;setgender;");
		sb.append(gender).append(';');
		sb.append(result).append(';');
		return sb.toString();
	}
	
	public static String encodeRichLotery(List<Byte> numbers)
	{
		StringBuilder sb = new StringBuilder(";rich;lotery;");
		if (numbers == null)
			sb.append(0).append(';');
		else {
			sb.append(numbers.size()).append(';');
			for (byte b : numbers)
				sb.append(b).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRichDivine(SBean.DropEntry entry)
	{
		StringBuilder sb = new StringBuilder(";rich;divine;");
		if (entry == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			sb.append(entry.type).append(';');
			sb.append(entry.id).append(';');
			sb.append(entry.arg).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRichBossStartAttack(int times)
	{
		StringBuilder sb = new StringBuilder(";rich;bossstartattack;");
		sb.append(times).append(';');
		return sb.toString();
	}
	
	public static String encodeRichBossFinishAttack(int addgold)
	{
		StringBuilder sb = new StringBuilder(";rich;bossfinishattack;");
		sb.append(addgold).append(';');
		return sb.toString();
	}
	
	public static String encodeRichMineBuyPass(int result)
	{
		StringBuilder sb = new StringBuilder(";rich;minebuypass;");
		sb.append(result).append(';');
		return sb.toString();
	}
	
	public static String encodeRichMineTax(int result)
	{
		StringBuilder sb = new StringBuilder(";rich;minetax;");
		sb.append(result).append(';');
		return sb.toString();
	}
	
	public static String encodeRichMineStartAttack(int result, SBean.CombatBonus bonus, float hpDownRate, float defenceDownRate)
	{
		StringBuilder sb = new StringBuilder(";rich;minestartattack;");
		sb.append(result).append(';');
		sb.append(hpDownRate).append(';');
		sb.append(defenceDownRate).append(';');
		if (bonus != null) {
			sb.append(bonus.entryIDs.size()).append(';');
			for (SBean.DropEntryNew d : bonus.entryIDs) {
				sb.append(d.type).append(';');
				sb.append(d.id).append(';');
				sb.append(d.arg).append(';');
			}
		}
		else
			sb.append(0).append(';');
			
		return sb.toString();
	}
	
	public static String encodeRichMineFinishAttack(int result)
	{
		StringBuilder sb = new StringBuilder(";rich;minefinishattack;");
		sb.append(result).append(';');
		return sb.toString();
	}
	
	public static String encodeRichGoldRanksRes(int rid, List<RichManager.GoldRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";rich;goldranks;");
		sb.append(rid).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (RichManager.GoldRank r : ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.rid).append(';');
				sb.append(r.gold).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.lvl).append(';');
			}
		}
		return sb.toString();	
	}
	
	public static String encodeRichDamageRanksRes(int rid, List<RichManager.DamageRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";rich;damageranks;");
		sb.append(rid).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (RichManager.DamageRank r : ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.rid).append(';');
				sb.append(r.damage).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.generals.size()).append(';');
				for (SBean.DBRichRoleGeneralBrief g : r.generals) {
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
				}
			}
		}
		return sb.toString();	
	}
	
	public static String encodeRichBuyMovementRes(boolean succ)
	{
		StringBuilder sb = new StringBuilder(";rich;buymovement;");
		sb.append(succ?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeRichUseItem(short iid, short cnt, String result)
	{
		StringBuilder sb = new StringBuilder(";rich;useitem;");
		sb.append(iid).append(';');
		sb.append(cnt).append(';');
		sb.append(result);
		return sb.toString();
	}
	
	public static String encodeRichTechTreeSync(List<SBean.DBRichTech> techtree)
	{
		StringBuilder sb = new StringBuilder(";rich;techtreesync;");
		sb.append(techtree.size()).append(';');
		for (SBean.DBRichTech e : techtree)
		{
			sb.append(e.id).append(";");
			sb.append(e.level).append(";");
		}
		return sb.toString();
	}
	
	public static String encodeRichTechTreeUpgrade(int id, boolean bOk)
	{
		StringBuilder sb = new StringBuilder(";rich;techtreeupgrade;");
		sb.append(id).append(';');
		sb.append(bOk?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeRichDailyTaskSync(List<SBean.DBRichDailyTask> dailyTask)
	{
		StringBuilder sb = new StringBuilder(";rich;dailytasksync;");
		sb.append(dailyTask.size()).append(';');
		for (SBean.DBRichDailyTask e : dailyTask)
		{
			sb.append(e.id).append(";");
			sb.append(e.reqCurVal).append(";");
			sb.append(e.reward).append(";");
		}
		return sb.toString();
	}
	
	public static String encodeRichDailyTaskRewardTake(int id, List<SBean.DropEntry> drops)
	{
		StringBuilder sb = new StringBuilder(";rich;dailytaskrewardtake;");
		sb.append(id).append(';');
		if (drops == null)
		{
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(drops.size()).append(';');
			for (SBean.DropEntry e : drops)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');	
			}
		}
		return sb.toString();
	}
	
	public static String encodeRichCanReward(boolean canReward)
	{
		StringBuilder sb = new StringBuilder(";rich;dailytaskcanreward;");
		sb.append(canReward ? 1 : 0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonSyncChapterRes(int fid, byte chapter, int rid, SBean.DBForceDungeon dungeon)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonsyncchapter;");
		sb.append(fid).append(';');
		if (dungeon != null && chapter >= 0 && chapter < dungeon.chapters.size()) {
			sb.append(1).append(';');
			SBean.DBForceChapter c = dungeon.chapters.get(chapter);
			sb.append(c.stage+1).append(';');
			sb.append(c.boxTaken.size()).append(';');
			for (SBean.DBForceChapterBox b : c.boxTaken) {
				sb.append(b.stage+1).append(';');
				boolean found = false;
				for (int r : b.rids) 
					if (r == rid) {
						found = true;
						break;
					}
				sb.append(found?1:0).append(';');
			}
			int times = 0;
			for (int r : c.rids) 
				if (r == rid)
					times ++;
			sb.append(times).append(';');
		}
		else
			sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonSyncRes(int fid, SBean.DBForceDungeon dungeon)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonsync;");
		sb.append(fid).append(';');
		if (dungeon != null) {
			sb.append(dungeon.chapters.size()).append(';');
			for (SBean.DBForceChapter c : dungeon.chapters) {
				sb.append(c.stage+1).append(';');
			}
		}
		else
			sb.append(0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonFightingRes(int fid, byte chapter, String name, List<SBean.DBForceChapterGeneralStatus> status)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonfighting;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(name).append(';');
		sb.append(status.size()).append(';');
		for (SBean.DBForceChapterGeneralStatus s : status) {
			sb.append(s.id).append(';');
			sb.append(s.hp).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceDungeonActivateChapterRes(int fid, byte chapter, boolean succ)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonactivatechapter;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(succ?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonStartAttackRes(int fid, byte chapter, byte stage, List<SBean.DBForceChapterGeneralStatus> status, List<SBean.CombatBonus> bonus, boolean success, int ret)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonstartattack;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(stage+1).append(';');
		sb.append(success?1:ret).append(';');
		if (success) {
			sb.append(status.size()).append(';');
			for (SBean.DBForceChapterGeneralStatus s : status) {
				sb.append(s.id).append(';');
				sb.append(s.hp).append(';');
				sb.append(s.mp).append(';');
				sb.append(s.state).append(';');
			}
			sb.append(bonus.size()).append(';');
			for (SBean.CombatBonus cb : bonus) {
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceDungeonFinishAttackRes(int fid, byte chapter, byte stage, boolean win, List<SBean.CombatBonus> bonus, List<SBean.DropEntry> treasure, int cupCount, boolean success)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonfinishattack;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(stage+1).append(';');
		sb.append(win?1:0).append(';');
		sb.append(success?1:0).append(';');
		if (success) {
			sb.append(bonus.size()).append(';');
			for (SBean.CombatBonus cb : bonus) {
				sb.append(cb.money).append(';');
				sb.append(cb.exp).append(';');
				sb.append(cb.entryIDs.size()).append(';');
				for(SBean.DropEntryNew e : cb.entryIDs)
				{
					sb.append(e.type).append(';');
					sb.append(e.id).append(';');
					sb.append(e.arg).append(';');
				}
			}
			sb.append(treasure.size()).append(';');
			for(SBean.DropEntry e : treasure)
			{
				sb.append(e.type).append(';');
				sb.append(e.id).append(';');
				sb.append(e.arg).append(';');
			}
			sb.append(cupCount).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceDungeonResetChapterRes(int fid, byte chapter, boolean succ, int ret)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonresetchapter;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(succ?1:ret).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonOpenBoxRes(int fid, byte chapter, byte stage, List<SBean.DropEntry> drops, boolean succ)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonopenbox;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(stage+1).append(';');
		sb.append(drops.size()).append(';');
		for (SBean.DropEntry d : drops) {
			sb.append(d.type).append(';');
			sb.append(d.id).append(';');
			sb.append(d.arg).append(';');
		}
		sb.append(succ?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonApplyTreasureRes(int fid, byte chapter, byte type, short id, boolean success, int applyCount, int time)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonapplytreasure;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(success?1:0).append(';');
		sb.append(applyCount).append(';');
		sb.append(time).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonListApplyRes(int fid, byte chapter, byte type, short id, List<SBean.DBForceTreasureApply> apply, boolean success)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonlistapply;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(apply.size()).append(';');
		for (SBean.DBForceTreasureApply a : apply) {
			sb.append(a.name).append(';');
			sb.append(a.headIconId).append(';');
			sb.append(a.lvl).append(';');
		}
		sb.append(success?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonKingListApplyRes(int fid, byte chapter, byte type, short id, List<ForceManager.DamageRank> noApplyList, List<ForceManager.DamageRank> applyList)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonkinglistapply;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(noApplyList.size()).append(';');
		for (ForceManager.DamageRank r : noApplyList) {
			sb.append(r.rid).append(';');
			sb.append(r.damage).append(';');
			sb.append(r.icon).append(';');
			sb.append(r.name).append(';');
			sb.append(r.lvl).append(';');
		}
		sb.append(applyList.size()).append(';');
		for (ForceManager.DamageRank r : applyList) {
			sb.append(r.rid).append(';');
			sb.append(r.damage).append(';');
			sb.append(r.icon).append(';');
			sb.append(r.name).append(';');
			sb.append(r.lvl).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceDungeonKingDeployRes(int fid, int rid, byte chapter, byte type, short id, boolean success)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonkingdeploy;");
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(type).append(';');
		sb.append(id).append(';');
		sb.append(success?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonTreasureLogRes(int fid, List<SBean.DBForceTreasureLog> logs, boolean success)
	{
		StringBuilder sb = new StringBuilder(";force;dungeontreasurelog;");
		sb.append(fid).append(';');
		sb.append(logs.size()).append(';');
		for (SBean.DBForceTreasureLog log : logs) {
			sb.append(log.name).append(';');
			sb.append(log.headIconId).append(';');
			sb.append(log.lvl).append(';');
			sb.append(log.type).append(';');
			sb.append(log.id).append(';');
			sb.append(log.time).append(';');
		}
		sb.append(success?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonListTreasureRes(int fid, int rid, byte chapter, List<SBean.DBForceTotalTreasure> treasure, byte applyType, short applyId, int applyIndex, boolean success)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonlisttreasure;");
		sb.append(fid).append(';');
		sb.append(chapter+1).append(';');
		sb.append(treasure.size()).append(';');
		for (SBean.DBForceTotalTreasure t : treasure) {
			sb.append(t.type).append(';');
			sb.append(t.id).append(';');
			sb.append(t.count).append(';');
			sb.append(t.time).append(';');
			sb.append(t.apply.size()).append(';');
		}
		sb.append(applyType).append(';');
		sb.append(applyId).append(';');
		sb.append(applyIndex+1).append(';');
		sb.append(success?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceDungeonKingListTreasureRes(int fid, int rid, Map<Byte, List<SBean.DBForceTotalTreasure>> treasures, int deployTimes)
	{
		StringBuilder sb = new StringBuilder(";force;dungeonkinglisttreasure;");
		sb.append(fid).append(';');
		sb.append(deployTimes).append(';');
		sb.append(treasures.size()).append(';');
		for (Map.Entry<Byte, List<SBean.DBForceTotalTreasure>> e : treasures.entrySet()) {
			sb.append(e.getKey()).append(';');
			sb.append(e.getValue().size()).append(';');
			for (SBean.DBForceTotalTreasure t : e.getValue()) {
				sb.append(t.type).append(';');
				sb.append(t.id).append(';');
				sb.append(t.count).append(';');
				sb.append(t.time).append(';');
				sb.append(t.apply.size()).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceDungeonDamageRanksRes(byte chapter, int fid, int rid, List<ForceManager.DamageRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;dungeondamageranks;");
		sb.append(chapter+1).append(';');
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (ForceManager.DamageRank r : ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.rid).append(';');
				sb.append(r.damage).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.fname).append(';');
				sb.append(r.name).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.generals.size()).append(';');
				for (SBean.DBForceChapterGeneralBrief g : r.generals) {
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
				}
			}
		}
		return sb.toString();	
	}
	
	public static String encodeRelationSyncRes(List<SBean.DBRelation> relations, byte stone)
	{
		StringBuilder sb = new StringBuilder(";relation;sync;");
		sb.append(relations.size()).append(';');
		for (SBean.DBRelation r : relations) {
			sb.append(r.id).append(';');
			sb.append(r.lvls.size()).append(';');
			for (short lvl : r.lvls)
				sb.append(lvl).append(';');
		}
		sb.append(stone).append(';');
		return sb.toString();	
	}
	
	public static String encodeRelationSync(List<SBean.DBRelation> relations)
	{
		StringBuilder sb = new StringBuilder(";relation;initsync;");
		sb.append(relations.size()).append(';');
		for (SBean.DBRelation r : relations) {
			sb.append(r.id).append(';');
			sb.append(r.lvls.size()).append(';');
			for (short lvl : r.lvls)
				sb.append(lvl).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeRelationActivateRes(short id, short gid, boolean succ)
	{
		StringBuilder sb = new StringBuilder(";relation;activate;");
		sb.append(id).append(';');
		sb.append(gid).append(';');
		sb.append(succ?1:0).append(';');
		return sb.toString();	
	}
	
	public static String encodeRelationUpgradeRes(short id, short gid, boolean succ, int ret)
	{
		StringBuilder sb = new StringBuilder(";relation;upgrade;");
		sb.append(id).append(';');
		sb.append(gid).append(';');
		sb.append(succ?1:ret).append(';');
		return sb.toString();
	}
	
	public static String encodeRelationStoneRes(byte stone, boolean succ)
	{
		StringBuilder sb = new StringBuilder(";relation;stone;");
		sb.append(stone).append(';');
		sb.append(succ?1:0).append(';');
		return sb.toString();
	}
	

	public static String encodeStonesSyncRes(List<SBean.DBGeneralStone> stones)
	{
		StringBuilder sb = new StringBuilder(";generalstone;sync;");
		sb.append(stones.size()).append(';');
		for (DBGeneralStone s : stones) {
			sb.append(s.id).append(';');
			sb.append(s.itemId).append(';');
			sb.append(s.exp).append(';');
			sb.append(s.gid).append(';');
			sb.append(s.pos).append(';');
			sb.append(s.passes.size()).append(';');
			for (DBGeneralStoneProp prop : s.passes){
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
			sb.append(s.resetPasses.size()).append(';');
			for (DBGeneralStoneProp prop : s.resetPasses){
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
				
		}
		return sb.toString();	
	}
	
	public static String encodeGeneralRemoveStoneRes(int iid, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generalstone;removestone;");
		sb.append(iid).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralStoneActiveRes(int iid,short gid,byte pos, DBGeneralStone gstone, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generalstone;activestone;");
		sb.append(iid).append(';');
		sb.append(gid).append(';');
		sb.append(pos).append(';');
		sb.append(gstone.itemId).append(';');
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsStoneRes(int iid,List<SBean.DropEntryNew> matList,List<Integer> itemId, boolean bOK)
	{
		StringBuilder sb = new StringBuilder(";generalstone;addstoneexp;");
		sb.append(iid).append(';');
		sb.append(matList.size()).append(';');
		for(SBean.DropEntryNew e : matList)
		{
			sb.append(e.type).append(';');
			sb.append(e.id).append(';');
			sb.append(e.arg).append(';');
		}
		sb.append(itemId.size()).append(';');
		for(Integer i : itemId)
		{
			sb.append(i).append(';');
		}
		sb.append(bOK?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsResetStoneRes(int iid, boolean bOK, List<DBGeneralStoneProp> props)
	{
		StringBuilder sb = new StringBuilder(";generalstone;resetstone;");
		sb.append(iid).append(';');
		sb.append(bOK?1:0).append(';');
		if (bOK) {
			sb.append(props.size()).append(';');
			for (DBGeneralStoneProp prop : props) {
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsSaveResetStoneRes(int iid, boolean bOK, List<DBGeneralStoneProp> props)
	{
		StringBuilder sb = new StringBuilder(";generalstone;savestone;");
		sb.append(iid).append(';');
		sb.append(bOK?1:0).append(';');
		if (bOK) {
			sb.append(props.size()).append(';');
			for (DBGeneralStoneProp prop : props) {
				sb.append(prop.propId).append(';');
				sb.append(prop.type).append(';');
				sb.append(prop.rank).append(';');
				sb.append(prop.value).append(';');
			}
		}
		return sb.toString();
	}
	

	private static int translateDuelScore(short lvl, int score)
	{
		SBean.DuelCFGS cfg = GameData.getInstance().getDuelCFG();
		
		if (lvl == 100)
			return score;
		
		short l = 10;
		short star = 0;
		for (short needStar : cfg.upgrade) {
			if (l >= lvl) {
				star = needStar;
				l --;
			}
		}
		score = score - star;
		
		if (score < 0)
			score = 0;
		
		return score;
	}
	
	public static String encodeDuelSyncRes(SBean.DBDuelRole dr, byte fightTimes, byte buyTimes)
	{
		StringBuilder sb = new StringBuilder(";duel;sync;");
		sb.append(dr.rid).append(';');
		sb.append(dr.lvl).append(';');
		sb.append(translateDuelScore(dr.lvl, dr.score)).append(';');
		sb.append(fightTimes).append(';');
		sb.append(buyTimes).append(';');
		return sb.toString();
	}
	
	public static String encodeDuelJoinRes(SBean.DuelJoinRes res, int err)
	{
		StringBuilder sb = new StringBuilder(";duel;join;");
		if (res == null)
			sb.append(-1000-err).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(res.type).append(';');
			sb.append(res.first).append(';');
			sb.append(res.oppoRoleName).append(';');
			sb.append(res.oppoServerName).append(';');
			sb.append(res.oppoHeadIconId).append(';');
			sb.append(res.oppoLevel).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeDuelSelectGeneralRes(SBean.DuelSelectGeneralRes res, int err, RTMatchData matchData)
	{
		StringBuilder sb = new StringBuilder(";duel;selectgeneral;");
		if (res == null)
			sb.append(-1000-err).append(';');
		else {
			sb.append(res.error).append(';');
			if (res.error == 0 && matchData != null) {
			    sb.append(matchData.selfGenerals.size()).append(';');
		        for(DBRoleGeneral e : matchData.selfGenerals)
		        {
		            sb.append(e.id).append(';');
		            sb.append(e.lvl).append(';');
		            sb.append(e.advLvl).append(';');
		            sb.append(e.evoLvl).append(';');
		        }
		        if (matchData.selfPet == null)
		            sb.append(0).append(';');
		        else {
		            sb.append(matchData.selfPet.id).append(';');
		            sb.append(matchData.selfPet.lvl).append(';');
		            sb.append(matchData.selfPet.evoLvl).append(';');
		            sb.append(matchData.selfPet.growthRate).append(';');
		            sb.append(matchData.selfPet.name).append(';');
		            sb.append(matchData.selfPet.deformStage).append(';');
		        }
		        sb.append(matchData.selfGids.size()).append(";");
		        for (Short i : matchData.selfGids) {
		            sb.append(i).append(";");
		        }
		        sb.append(matchData.selfPids.size()).append(";");
		        for (Short i : matchData.selfPids) {
		            sb.append(i).append(";");
		        }
			}
		}
		return sb.toString();
	}
	
	public static String encodeDuelSelectGeneralForward(DuelManager.RTMatchData matchData)
	{
		StringBuilder sb = new StringBuilder(";duel;selectgeneralforward;");
		sb.append(matchData.oppoGenerals.size()).append(';');
		for(DBRoleGeneral e : matchData.oppoGenerals)
		{
			sb.append(e.id).append(';');
			sb.append(e.lvl).append(';');
			sb.append(e.advLvl).append(';');
			sb.append(e.evoLvl).append(';');
		}
		if (matchData.oppoPet == null)
			sb.append(0).append(';');
		else {
			sb.append(matchData.oppoPet.id).append(';');
			sb.append(matchData.oppoPet.lvl).append(';');
			sb.append(matchData.oppoPet.evoLvl).append(';');
			sb.append(matchData.oppoPet.growthRate).append(';');
			sb.append(matchData.oppoPet.name).append(';');
			sb.append(matchData.oppoPet.deformStage).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeDuelStartAttackRes(SBean.DuelStartAttackRes res, DuelManager.RTMatchData matchData)
	{
		StringBuilder sb = new StringBuilder(";duel;startattack;");
		if (res == null || matchData == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			boolean first = DuelManager.isReverse(matchData.first, matchData.combatIndex);
			sb.append(first?1:0).append(';');
			List<DBRoleGeneral> generals1 = null;
			List<DBRoleGeneral> generals2 = null;
			SBean.DBPetBrief pet1 = null;
			SBean.DBPetBrief pet2 = null;
			List<SBean.DBRelation> relation1 = null;
			List<SBean.DBRelation> relation2 = null;
			if (first) {
				generals1 = matchData.selfGenerals;
				pet1 = matchData.selfPet;
				relation1 = matchData.selfRelations;
				generals2 = matchData.oppoGenerals;
				pet2 = matchData.oppoPet;
				relation2 = matchData.oppoRelations;
			}
			else {
				generals2 = matchData.selfGenerals;
				pet2 = matchData.selfPet;
				relation2 = matchData.selfRelations;
				generals1 = matchData.oppoGenerals;
				pet1 = matchData.oppoPet;
				relation1 = matchData.oppoRelations;
			}
			sb.append(generals1.size()).append(';');
			for(DBRoleGeneral e : generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.skills.size()).append(';');
				for(short skill : e.skills)
				{
					sb.append(skill).append(';');
				}
				sb.append(e.equips.size()).append(';');
				for(SBean.DBGeneralEquip equip : e.equips)
				{
					sb.append(equip.lvl).append(';');
					sb.append(equip.exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			sb.append(generals2.size()).append(';');
			for(DBRoleGeneral e : generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.skills.size()).append(';');
				for(short skill : e.skills)
				{
					sb.append(skill).append(';');
				}
				sb.append(e.equips.size()).append(';');
				for(SBean.DBGeneralEquip equip : e.equips)
				{
					sb.append(equip.lvl).append(';');
					sb.append(equip.exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			if (pet1 == null)
				sb.append(0).append(';');
			else {
				sb.append(pet1.id).append(';');
				sb.append(pet1.lvl).append(';');
				sb.append(pet1.evoLvl).append(';');
				sb.append(pet1.growthRate).append(';');
				sb.append(pet1.name).append(';');
				sb.append(pet1.deformStage).append(';');
				sb.append(pet1.awakLvl).append(";");
			}
			if (pet2 == null)
				sb.append(0).append(';');
			else {
				sb.append(pet2.id).append(';');
				sb.append(pet2.lvl).append(';');
				sb.append(pet2.evoLvl).append(';');
				sb.append(pet2.growthRate).append(';');
				sb.append(pet2.name).append(';');
				sb.append(pet2.deformStage).append(';');
				sb.append(pet2.awakLvl).append(";");
			}
			encodeRelation(sb, relation1);
			encodeRelation(sb, relation2);
		}
		return sb.toString();
	}
	
	public static String encodeDuelReconnect(RTMatchData matchData) {
	    StringBuilder sb = new StringBuilder(";duel;reconnect;");
	    sb.append(1).append(";");
	    sb.append(matchData.oppoRid.serverID).append(";");
	    sb.append(matchData.oppoRid.roleID).append(";");
	    sb.append(matchData.record.rname2).append(";");
	    sb.append(matchData.record.headIconId2).append(";");
	    sb.append(matchData.record.level2).append(";");
	    sb.append(matchData.oppoServerName).append(";");
	    sb.append(matchData.combatType).append(";");
	    sb.append(matchData.combatIndex).append(";");
	    sb.append(matchData.first ? 1 : 0).append(";");
	    sb.append(matchData.joinTime).append(";");
	    sb.append(matchData.lastMySelection ? 1 : 0).append(";");
	    sb.append(matchData.lastSelectionTime).append(";");
	    sb.append(matchData.results.size()).append(";");
	    for (boolean r : matchData.results) {
	        if (r) {
	            sb.append("1").append(";");
	        } else {
	            sb.append("0").append(";");
	        }
        }
	    sb.append(matchData.selfGenerals.size()).append(';');
        for(DBRoleGeneral e : matchData.selfGenerals)
        {
            sb.append(e.id).append(';');
            sb.append(e.lvl).append(';');
            sb.append(e.advLvl).append(';');
            sb.append(e.evoLvl).append(';');
        }
        if (matchData.selfPet == null)
            sb.append(0).append(';');
        else {
            sb.append(matchData.selfPet.id).append(';');
            sb.append(matchData.selfPet.lvl).append(';');
            sb.append(matchData.selfPet.evoLvl).append(';');
            sb.append(matchData.selfPet.growthRate).append(';');
            sb.append(matchData.selfPet.name).append(';');
            sb.append(matchData.selfPet.deformStage).append(';');
        }
	    sb.append(matchData.oppoGenerals.size()).append(';');
        for(DBRoleGeneral e : matchData.oppoGenerals)
        {
            sb.append(e.id).append(';');
            sb.append(e.lvl).append(';');
            sb.append(e.advLvl).append(';');
            sb.append(e.evoLvl).append(';');
        }
        if (matchData.oppoPet == null)
            sb.append(0).append(';');
        else {
            sb.append(matchData.oppoPet.id).append(';');
            sb.append(matchData.oppoPet.lvl).append(';');
            sb.append(matchData.oppoPet.evoLvl).append(';');
            sb.append(matchData.oppoPet.growthRate).append(';');
            sb.append(matchData.oppoPet.name).append(';');
            sb.append(matchData.oppoPet.deformStage).append(';');
        }
        sb.append(matchData.selfGids.size()).append(";");
        for (Short i : matchData.selfGids) {
            sb.append(i).append(";");
        }
        sb.append(matchData.selfPids.size()).append(";");
        for (Short i : matchData.selfPids) {
            sb.append(i).append(";");
        }
	    return sb.toString();
	}
	
	public static String encodeDuelFinishAttackRes(SBean.DuelFinishAttackRes res, byte type, SBean.DBDuelRole dr, int rewardPoints)
	{
		StringBuilder sb = new StringBuilder(";duel;finishattack;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(type).append(';');
			sb.append(res.result).append(';');
			if (dr == null)
				sb.append(0).append(';');
			else {
				sb.append(dr.lvl).append(';');
				sb.append(translateDuelScore(dr.lvl, dr.score)).append(';');
			}
			sb.append(rewardPoints).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeDuelOppoGiveupRes(SBean.DuelOppoGiveupRes res, byte type, SBean.DBDuelRole dr, int rewardPoints)
	{
		StringBuilder sb = new StringBuilder(";duel;oppogiveup;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(type).append(';');
			if (dr == null)
				sb.append(0).append(';');
			else {
				sb.append(dr.lvl).append(';');
				sb.append(translateDuelScore(dr.lvl, dr.score)).append(';');
			}
			sb.append(rewardPoints).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeDuelFinishAttackOnOppoDownLine(int result)
	{
	    StringBuilder sb = new StringBuilder(";duel;finishattackonoppodownline;");
	    sb.append(result).append(";");
        return sb.toString();
	}
	
	public static String encodeDuelRankRes(SBean.DuelRankRes res)
	{
		StringBuilder sb = new StringBuilder(";duel;rank;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(res.rank).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeDuelTopRanksRes(SBean.DuelTopRanksRes res)
	{
		StringBuilder sb = new StringBuilder(";duel;topranks;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(res.ranks.size()).append(';');
			for (SBean.DuelTopRank r : res.ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.roleName).append(';');
				sb.append(r.serverName).append(';');
				sb.append(r.headIconId).append(';');
				sb.append(r.score).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeDuelRecordListRes(List<SBean.DBDuelRecord> records)
	{
		StringBuilder sb = new StringBuilder(";duel;recordlist;");
		if (records == null)
			sb.append(0).append(';');
		else {
			sb.append(records.size()).append(';');
			for (SBean.DBDuelRecord r : records) {
				sb.append(r.rname1).append(';');
				sb.append(r.rname2).append(';');
				sb.append(r.headIconId1).append(';');
				sb.append(r.headIconId2).append(';');
				sb.append(r.level1).append(';');
				sb.append(r.level2).append(';');
				sb.append(r.result).append(';');
				sb.append(r.matches.size()).append(';');
				for (SBean.DBDuelMatchRecord m : r.matches) {
					sb.append(m.result).append(';');
					sb.append(m.generals1.size()).append(';');
					for (DBRoleGeneral g : m.generals1) {
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						sb.append(g.weapon.lvl).append(';');
					}
					sb.append(m.generals2.size()).append(';');
					for (DBRoleGeneral g : m.generals2) {
						sb.append(g.id).append(';');
						sb.append(g.lvl).append(';');
						sb.append(g.advLvl).append(';');
						sb.append(g.evoLvl).append(';');
						sb.append(g.weapon.lvl).append(';');
					}
					sb.append(m.pets1.size()).append(';');
					for (SBean.DBPetBrief p : m.pets1) {
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
						sb.append(p.deformStage).append(';');
						sb.append(p.awakLvl).append(";");
					}
					sb.append(m.pets2.size()).append(';');
					for (SBean.DBPetBrief p : m.pets2) {
						sb.append(p.id).append(';');
						sb.append(p.lvl).append(';');
						sb.append(p.evoLvl).append(';');
						sb.append(p.growthRate).append(';');
						sb.append(p.name).append(';');
						sb.append(p.deformStage).append(';');
						sb.append(p.awakLvl).append(";");
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String encodeDuelMatchRecordRes(SBean.DBDuelMatchRecord record, String[] names, short[] headIconIds, short[] levels, boolean first, byte mindex)
	{
		StringBuilder sb = new StringBuilder(";duel;getmatchrecord;");
		if (record == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			first = DuelManager.isReverse(first, mindex);
			sb.append(first?1:0).append(';');
			List<DBRoleGeneral> generals1 = null;
			List<DBRoleGeneral> generals2 = null;
			List<SBean.DBPetBrief> pets1 = null;
			List<SBean.DBPetBrief> pets2 = null;
			List<SBean.DBRelation> relation1 = null;
			List<SBean.DBRelation> relation2 = null;
			String name1 = null;
			String name2 = null;
			short headIconId1 = 0;
			short headIconId2 = 0;
			short level1 = 0;
			short level2 = 0;
			int result = 0;
			if (first) {
				generals1 = record.generals1;
				pets1 = record.pets1;
				relation1 = record.relation1;
				name1 = names[0];
				headIconId1 = headIconIds[0];
				level1 = levels[0];
				generals2 = record.generals2;
				pets2 = record.pets2;
				relation2 = record.relation2;
				name2 = names[1];
				headIconId2 = headIconIds[1];
				level2 = levels[1];
				result = record.result;
			}
			else {
				generals2 = record.generals1;
				pets2 = record.pets1;
				relation2 = record.relation1;
				name2 = names[0];
				headIconId2 = headIconIds[0];
				level2 = levels[0];
				generals1 = record.generals2;
				pets1 = record.pets2;
				relation1 = record.relation2;
				name1 = names[1];
				headIconId1 = headIconIds[1];
				level1 = levels[1];
				result = (record.result > 0)?0:1;
			}
			sb.append(result).append(';');
			sb.append(name1).append(';');
			sb.append(name2).append(';');
			sb.append(headIconId1).append(';');
			sb.append(headIconId2).append(';');
			sb.append(level1).append(';');
			sb.append(level2).append(';');
			sb.append(generals1.size()).append(';');
			for(DBRoleGeneral e : generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			sb.append(generals2.size()).append(';');
			for(DBRoleGeneral e : generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
				encodeGStones(sb, e.generalStone);
			}
			sb.append(pets1.size()).append(';');
			for(SBean.DBPetBrief g : pets1)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			sb.append(pets2.size()).append(';');
			for(SBean.DBPetBrief g : pets2)
			{
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.growthRate).append(';');
				sb.append(g.name).append(';');
				sb.append(g.deformStage).append(';');
				sb.append(g.awakLvl).append(";");
			}
			encodeRelation(sb, relation1);
			encodeRelation(sb, relation2);
		}
		return sb.toString();
		
	}
	
	public static String encodeForceMercInitSyncRes(int fid, SBean.DBRoleMerc merc, Map<Short, Integer> baseIncomes)
	{
		StringBuilder sb = new StringBuilder(";force;mercinitsync;");
		sb.append(fid).append(';');
		if (merc == null)
			sb.append(0).append(';');
		else {
			sb.append(merc.generals.size()).append(';');
			for (SBean.DBMercGeneral e : merc.generals) {
				sb.append(e.general.id).append(';');
				sb.append(e.general.lvl).append(';');
				sb.append(e.general.advLvl).append(';');
				sb.append(e.general.evoLvl).append(';');
				sb.append(e.time).append(';');
				Integer baseIncome = baseIncomes.get(e.general.id);
				if (baseIncome == null)
					baseIncome = 0;
				sb.append(baseIncome).append(';');
				sb.append(e.mercIncome).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceMercSyncSelfRes(int fid, SBean.DBRoleMerc merc, Map<Short, Integer> baseIncomes, List<Short> gids)
	{
		StringBuilder sb = new StringBuilder(";force;mercsyncself;");
		sb.append(fid).append(';');
		if (merc == null)
			sb.append(0).append(';');
		else {
			sb.append(merc.generals.size()).append(';');
			for (SBean.DBMercGeneral e : merc.generals) {
				sb.append(e.general.id).append(';');
				sb.append(e.general.lvl).append(';');
				sb.append(e.general.advLvl).append(';');
				sb.append(e.general.evoLvl).append(';');
				sb.append(e.time).append(';');
				Integer baseIncome = baseIncomes.get(e.general.id);
				if (baseIncome == null)
					baseIncome = 0;
				sb.append(baseIncome).append(';');
				sb.append(e.mercIncome).append(';');
			}
		}
		if (gids == null)
			sb.append(0).append(';');
		else {
			sb.append(gids.size()).append(';');
			for (short gid : gids)
				sb.append(gid).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceMercSyncRes(int fid, int rid, int day, List<SBean.DBRoleMerc> merc, SBean.DBMarchMercGeneral general, SBean.DBMarchMercGeneral suraGeneral)
	{
		StringBuilder sb = new StringBuilder(";force;mercsync;");
		sb.append(fid).append(';');
		if (merc == null)
			sb.append(0).append(';');
		else {
			sb.append(merc.size()).append(';');
			for (SBean.DBRoleMerc m : merc) {
				sb.append(m.rid).append(';');
				sb.append(m.rname).append(';');
				int available = 0;
				for (SBean.DBMercGeneral e : m.generals) {
					boolean ignore = false;
					for (SBean.DBMercUsed u : e.used)
						if (u.day == day && u.rid == rid) {
							ignore = true;
							break;
						}
					
					if (!ignore)
						available ++;
				}
				sb.append(available).append(';');
				for (SBean.DBMercGeneral e : m.generals) {
					boolean ignore = false;
					for (SBean.DBMercUsed u : e.used)
						if (u.day == day && u.rid == rid) {
							ignore = true;
							break;
						}
					
					if (ignore)
						continue;
					
					DBRoleGeneral g = e.general;
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
					sb.append(g.skills.size()).append(';');
					for(short skill : g.skills)
					{
						sb.append(skill).append(';');
					}
					sb.append(g.equips.size()).append(';');
					for(SBean.DBGeneralEquip equip : g.equips)
					{
						sb.append(equip.lvl).append(';');
						sb.append(equip.exp).append(';');
					}
					sb.append(g.weapon.lvl).append(';');
					if (g.weapon.lvl >= 0) {
						sb.append(g.weapon.evo).append(';');
						sb.append(g.weapon.passes.size()).append(';');
						for (SBean.DBWeaponProp prop : g.weapon.passes) {
							sb.append(prop.propId).append(';');					
							sb.append(prop.type).append(';');					
							sb.append(prop.value).append(';');					
							sb.append(prop.rank).append(';');					
						}
					}
					encodeGeneralSeyen(sb, g);
					encodeGeneralOfficial(sb, g);
					encodeGStones(sb, g.generalStone);
				}
			}
		}
		if (general == null)
			sb.append(0).append(';');
		else {
			sb.append(general.rid).append(';');
			sb.append(general.rname).append(';');
			DBRoleGeneral g = general.general;
			sb.append(g.id).append(';');
			sb.append(g.lvl).append(';');
			sb.append(g.advLvl).append(';');
			sb.append(g.evoLvl).append(';');
			sb.append(g.skills.size()).append(';');
			for(short skill : g.skills)
			{
				sb.append(skill).append(';');
			}
			sb.append(g.equips.size()).append(';');
			for(SBean.DBGeneralEquip equip : g.equips)
			{
				sb.append(equip.lvl).append(';');
				sb.append(equip.exp).append(';');
			}
			sb.append(g.weapon.lvl).append(';');
			if (g.weapon.lvl >= 0) {
				sb.append(g.weapon.evo).append(';');
				sb.append(g.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : g.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
					sb.append(prop.rank).append(';');					
				}
			}
			encodeGeneralSeyen(sb, g);
			encodeGeneralOfficial(sb, g);
			encodeGStones(sb, g.generalStone);
			if (general.status.id == g.id) {
				sb.append(general.status.id).append(';');
				sb.append(general.status.hp).append(';');
				sb.append(general.status.mp).append(';');
				sb.append(general.status.state).append(';');
			}
			else {
				sb.append(0).append(';');
			}
		}
		
		if (suraGeneral == null)
			sb.append(0).append(';');
		else {
			sb.append(suraGeneral.rid).append(';');
			sb.append(suraGeneral.rname).append(';');
			DBRoleGeneral g = suraGeneral.general;
			sb.append(g.id).append(';');
			sb.append(g.lvl).append(';');
			sb.append(g.advLvl).append(';');
			sb.append(g.evoLvl).append(';');
			sb.append(g.skills.size()).append(';');
			for(short skill : g.skills)
			{
				sb.append(skill).append(';');
			}
			sb.append(g.equips.size()).append(';');
			for(SBean.DBGeneralEquip equip : g.equips)
			{
				sb.append(equip.lvl).append(';');
				sb.append(equip.exp).append(';');
			}
			sb.append(g.weapon.lvl).append(';');
			if (g.weapon.lvl >= 0) {
				sb.append(g.weapon.evo).append(';');
				sb.append(g.weapon.passes.size()).append(';');
				for (SBean.DBWeaponProp prop : g.weapon.passes) {
					sb.append(prop.propId).append(';');					
					sb.append(prop.type).append(';');					
					sb.append(prop.value).append(';');					
					sb.append(prop.rank).append(';');					
				}
			}
			encodeGeneralSeyen(sb, g);
			encodeGeneralOfficial(sb, g);
			encodeGStones(sb, g.generalStone);
			if (suraGeneral.status.id == g.id) {
				sb.append((suraGeneral.status.hp>0)?0:1).append(';');
			}
			else {
				sb.append(0).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceMercShareRes(int fid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;mercshare;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMercWithdrawRes(int fid, short gid, boolean ok, int baseIncome, int mercIncome)
	{
		StringBuilder sb = new StringBuilder(";force;mercwithdraw;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(baseIncome).append(';');
		sb.append(mercIncome).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMercHireRes(int fid, int rid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;merchire;");
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMercMarchHireRes(int fid, int rid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;mercmarchhire;");
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMercSuraHireRes(int fid, int rid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;mercsurahire;");
		sb.append(fid).append(';');
		sb.append(rid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceMercRemoveCityGeneralRes(int fid, short gid, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;mercremovecitygeneral;");
		sb.append(fid).append(';');
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSWarSyncBegin(int rid, SBean.DBForceSWar swar, int [] status)
	{
		StringBuilder sb = new StringBuilder(";force;swarsyncbegin;");
		if (swar == null)
			sb.append(0).append(';');
		else {
			sb.append(swar.brief.fid).append(';');
			sb.append(swar.brief.fname).append(';');
			sb.append(swar.brief.lvl).append(';');
			sb.append(swar.brief.exp).append(';');
			sb.append(swar.brief.winTimes).append(';');
			Set<Short> usedGIds = new HashSet<Short>();
			Set<Short> usedPIds = new HashSet<Short>();
			Map<Integer, String> nameMap = new HashMap<Integer, String>();
			for (SBean.DBForceSWarBuilding b : swar.brief.buildings) {
				for (SBean.DBForceSWarTeam t : b.teams)
					if (nameMap.get(t.id) == null)
						nameMap.put(t.id, t.name);
			}
			sb.append(nameMap.size()).append(';');
			for (Map.Entry<Integer, String> e : nameMap.entrySet()) {
				sb.append(e.getKey()).append(';');
				sb.append(e.getValue()).append(';');
			}
			sb.append(swar.brief.buildings.size()).append(';');
			for (SBean.DBForceSWarBuilding b : swar.brief.buildings) {
				sb.append(b.bid).append(';');
				sb.append(b.teams.size()).append(';');
				int alive = 0;
				for (SBean.DBForceSWarTeam t : b.teams) {
					if (t.status == 0)
						alive ++;
					if (t.id == rid) {
						for (DBRoleGeneral g : t.generals)
							usedGIds.add(g.id);
						for (SBean.DBPetBrief p : t.pets)
							usedPIds.add((short)p.id);
					}
				}
				sb.append(alive).append(';');
			}
			sb.append(usedGIds.size()).append(';');
			for (short gid : usedGIds)
				sb.append(gid).append(';');
			sb.append(usedPIds.size()).append(';');
			for (short pid : usedPIds)
				sb.append(pid).append(';');
			List<Short> gids = new ArrayList<Short>();
			List<Short> pids = new ArrayList<Short>();
			for (SBean.DBForceSWarRole r : swar.roles) {
				if (r.rid == rid) {
					gids.addAll(r.usedGIds);
					pids.addAll(r.usedPIds);
				}
			}
			sb.append(gids.size()).append(';');
			for (short gid : gids)
				sb.append(gid).append(';');
			sb.append(pids.size()).append(';');
			for (short pid : pids)
				sb.append(pid).append(';');
				
			sb.append(status[0]).append(';');
			sb.append(status[1]).append(';');
			sb.append(swar.attackBadges).append(';');
			sb.append(swar.protectDays).append(';');
			sb.append(swar.weakBufId).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceSWarSyncTeams(byte bid, List<SBean.DBForceSWarTeam> teams)
	{
		StringBuilder sb = new StringBuilder(";force;swarsyncteams;");
		sb.append(bid).append(';');
		sb.append(teams.size()).append(';');
		for (SBean.DBForceSWarTeam t : teams) {
			sb.append(t.id).append(';');
			sb.append(t.level).append(';');
			sb.append(t.icon).append(';');
			sb.append(t.status).append(';');
			sb.append(t.winTimes).append(';');
			sb.append(t.generals.size()).append(';');
			for (DBRoleGeneral g : t.generals) {
				sb.append(g.id).append(';');
				sb.append(g.lvl).append(';');
				sb.append(g.advLvl).append(';');
				sb.append(g.evoLvl).append(';');
				sb.append(g.weapon.lvl).append(';');
			}
			sb.append(t.pets.size()).append(';');
			for (SBean.DBPetBrief p : t.pets) {
				sb.append(p.id).append(';');
				sb.append(p.lvl).append(';');
				sb.append(p.evoLvl).append(';');
				//sb.append(p.growthRate).append(';');
				//sb.append(p.name).append(';');
				//sb.append(p.deformStage).append(';');
			}				
		}				
		return sb.toString();
	}
	
	public static String encodeForceSWarSyncEnd()
	{
		StringBuilder sb = new StringBuilder(";force;swarsyncend;");
		return sb.toString();
	}

	public static String encodeForceSWarLogs(List<SBean.DBForceSWarLog> logs)
	{
		StringBuilder sb = new StringBuilder(";force;swarlogs;");
		if (logs == null)
			sb.append(0).append(';');
		else {
			sb.append(logs.size()).append(';');
			for (SBean.DBForceSWarLog l : logs) {
				sb.append(l.type).append(';');
				sb.append(l.time).append(';');
				sb.append(l.oppoServerName).append(';');
				sb.append(l.oppoName).append(';');
				sb.append(l.result).append(';');
				sb.append(l.dead).append(';');
				sb.append(l.oppoDead).append(';');
			}
		}
		return sb.toString();
	}

	public static String encodeForceSWarSearch(boolean ok, int err)
	{
		StringBuilder sb = new StringBuilder(";force;swarsearch;");
		sb.append(ok?1:0).append(';');
		sb.append(err).append(';');
		return sb.toString();
	}

	public static String encodeForceSWarViewOppos(GameServer gs, int rid, List<SBean.DBForceSWarOppo> opponents)
	{
		StringBuilder sb = new StringBuilder(";force;swarviewoppos;");
		if (opponents == null)
			sb.append(0).append(';');
		else {
			sb.append(opponents.size()).append(';');
			for (SBean.DBForceSWarOppo o : opponents) {
				sb.append(o.oppoBriefs.fid).append(';');
				sb.append(o.oppoBriefs.serverId).append(';');
				sb.append(o.oppoBriefs.fname).append(';');
				sb.append(o.oppoBriefs.serverName).append(';');
				sb.append(o.oppoBriefs.lvl).append(';');
				sb.append(o.oppoBriefs.buildings.size()).append(';');
				for (SBean.DBForceSWarBuilding b : o.oppoBriefs.buildings) {
					sb.append(b.bid).append(';');
					sb.append(b.teams.size()).append(';');
					int alive = 0;
					for (SBean.DBForceSWarTeam t : b.teams)
						if (t.status == 0)
							alive ++;
					sb.append(alive).append(';');
				}
				sb.append(o.votes.size()).append(';');
				boolean found = false;
				for (int v : o.votes)
					if (rid == v) {
						found = true;
						break;
					}
				
				sb.append(found?1:0).append(';');
				short winTimes[] = {0, 0};
				int scores[] = {0, 0};
				ForceSWarManager.getOppoScores(gs, o, winTimes, scores);
				sb.append(scores[0]).append(';');
				sb.append(scores[1]).append(';');
			}
		}
		return sb.toString();
	}

	public static String encodeForceSWarViewOppoBuilding(int fid, int sid, byte bid, SBean.DBForceSWarBuilding building)
	{
		StringBuilder sb = new StringBuilder(";force;swarviewoppobuilding;");
		sb.append(fid).append(';');
		sb.append(sid).append(';');
		sb.append(bid).append(';');
		if (building == null || building.teams == null)
			sb.append(0).append(';');
		else {
			sb.append(building.teams.size()).append(';');
			for (SBean.DBForceSWarTeam t : building.teams) {
				sb.append(t.name).append(';');
				sb.append(t.level).append(';');
				sb.append(t.icon).append(';');
				sb.append(t.status).append(';');
				sb.append(t.winTimes).append(';');
				sb.append(t.generals.size()).append(';');
				for (DBRoleGeneral g : t.generals) {
					sb.append(g.id).append(';');
					sb.append(g.lvl).append(';');
					sb.append(g.advLvl).append(';');
					sb.append(g.evoLvl).append(';');
					sb.append(g.weapon.lvl).append(';');
				}
				sb.append(t.pets.size()).append(';');
				for (SBean.DBPetBrief p : t.pets) {
					sb.append(p.id).append(';');
					sb.append(p.lvl).append(';');
					sb.append(p.evoLvl).append(';');
					sb.append(p.growthRate).append(';');
					sb.append(p.name).append(';');
					sb.append(p.deformStage).append(';');
					sb.append(p.awakLvl).append(";");
				}				
			}
		}
		return sb.toString();
	}

	public static String encodeForceSWarVote(byte oindex, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;swarvote;");
		sb.append(oindex).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}

	public static String encodeForceSWarSetTeam(byte bid, short bindex, boolean ok)
	{
		StringBuilder sb = new StringBuilder(";force;swarsetteam;");
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}

	public static String encodeForceSWarStartAttack(int fid, int sid, byte bid, short bindex, int err, SBean.DBForceSWarMatchRecord ar)
	{
		StringBuilder sb = new StringBuilder(";force;swarstartattack;");
		sb.append(fid).append(';');
		sb.append(sid).append(';');
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		//sb.append(err).append(';');
		if (err != 0 || ar == null) {
			sb.append(0).append(';');
		}
		else
		{
			sb.append(1).append(';');
			sb.append(ar.seed).append(';');
			sb.append(ar.generals1.size()).append(';');
			for(DBRoleGeneral e : ar.generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(ar.pets1.size()).append(';');
			for(DBPetBrief e : ar.pets1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, ar.relation1);
			encodeGStones(sb, ar.gStones1);
			sb.append(ar.generals2.size()).append(';');
			for(DBRoleGeneral e : ar.generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(ar.pets2.size()).append(';');
			for(DBPetBrief e : ar.pets2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, ar.relation2);		
			encodeGStones(sb, ar.gStones2);
		}
		return sb.toString();
	}

	public static String encodeForceSWarFinishAttack(int fid, int sid, byte bid, short bindex, byte win, int err)
	{
		StringBuilder sb = new StringBuilder(";force;swarfinishattack;");
		sb.append(fid).append(';');
		sb.append(sid).append(';');
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		sb.append(win).append(';');
		sb.append(err).append(';');
		return sb.toString();
	}

	private static void encodeForceSWarRecord(StringBuilder sb, SBean.DBForceSWarMatchRecord record)
	{
		if (record == null)
			sb.append(0).append(';');
		else {
			sb.append(1).append(';');
			sb.append(record.name1).append(';');
			sb.append(record.name2).append(';');
			sb.append(record.lvl1).append(';');
			sb.append(record.lvl2).append(';');
			sb.append(record.icon1).append(';');
			sb.append(record.icon2).append(';');
			sb.append(record.seed).append(';');
			sb.append(record.generals1.size()).append(';');
			for(DBRoleGeneral e : record.generals1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(record.pets1.size()).append(';');
			for(DBPetBrief e : record.pets1)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, record.relation1);
			encodeGStones(sb, record.gStones1);
			sb.append(record.generals2.size()).append(';');
			for(DBRoleGeneral e : record.generals2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.advLvl).append(';');
				sb.append(e.evoLvl).append(';');
				for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
				{
					sb.append(e.skills.get(i)).append(';');
				}
				for(int i = 0; i < GameData.GENERAL_EQUIP_COUNT; ++i)
				{
					sb.append(e.equips.get(i).lvl).append(';');
					sb.append(e.equips.get(i).exp).append(';');
				}
				sb.append(e.weapon.lvl).append(';');
				if (e.weapon.lvl >= 0) {
					sb.append(e.weapon.evo).append(';');
					sb.append(e.weapon.passes.size()).append(';');
					for (SBean.DBWeaponProp prop : e.weapon.passes) {
						sb.append(prop.propId).append(';');					
						sb.append(prop.type).append(';');					
						sb.append(prop.value).append(';');					
						sb.append(prop.rank).append(';');					
					}
				}
				encodeGeneralSeyen(sb, e);
				encodeGeneralOfficial(sb, e);
			}
			sb.append(record.pets2.size()).append(';');
			for(DBPetBrief e : record.pets2)
			{
				sb.append(e.id).append(';');
				sb.append(e.lvl).append(';');
				sb.append(e.evoLvl).append(';');
				sb.append(e.growthRate).append(';');
				sb.append(e.name).append(';');
				sb.append(e.deformStage).append(';');
				sb.append(e.awakLvl).append(";");
			}
			encodeRelation(sb, record.relation2);		
			encodeGStones(sb, record.gStones2);
			sb.append(record.bufs1.size()).append(';');
			for(SBean.DBForceSWarBuf buf : record.bufs1) {
				sb.append(buf.id).append(';');
				sb.append(buf.type).append(';');
				sb.append(buf.value).append(';');
			}
			sb.append(record.bufs2.size()).append(';');
			for(SBean.DBForceSWarBuf buf : record.bufs2) {
				sb.append(buf.id).append(';');
				sb.append(buf.type).append(';');
				sb.append(buf.value).append(';');
			}
		}
	}

	public static String encodeForceSWarRecordList(byte bid, short bindex, List<SBean.DBForceSWarMatchRecord> records)
	{
		StringBuilder sb = new StringBuilder(";force;swarrecordlist;");
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		if (records == null)
			sb.append(0).append(';');
		else {
			sb.append(records.size()).append(';');
			for (SBean.DBForceSWarMatchRecord r : records) {
				sb.append(r.time).append(';');
				sb.append(r.name1).append(';');
				sb.append(r.lvl1).append(';');
				sb.append(r.icon1).append(';');
				sb.append(r.result).append(';');
			}
		}
		return sb.toString();
	}

	public static String encodeForceSWarRecord(byte bid, short bindex, short mindex, SBean.DBForceSWarMatchRecord record)
	{
		StringBuilder sb = new StringBuilder(";force;swarrecord;");
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		sb.append(mindex+1).append(';');
		encodeForceSWarRecord(sb, record);
		return sb.toString();
	}

	public static String encodeForceSWarOppoRecordList(int fid, int sid, byte bid, short bindex, List<SBean.DBForceSWarMatchRecord> records)
	{
		StringBuilder sb = new StringBuilder(";force;swaropporecordlist;");
		sb.append(fid).append(';');
		sb.append(sid).append(';');
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		if (records == null)
			sb.append(0).append(';');
		else {
			sb.append(records.size()).append(';');
			for (SBean.DBForceSWarMatchRecord r : records) {
				sb.append(r.time).append(';');
				sb.append(r.name1).append(';');
				sb.append(r.lvl1).append(';');
				sb.append(r.icon1).append(';');
				sb.append(r.result).append(';');
			}
		}
		return sb.toString();
	}

	public static String encodeForceSWarOppoRecord(int fid, int sid, byte bid, short bindex, short mindex, SBean.DBForceSWarMatchRecord record)
	{
		StringBuilder sb = new StringBuilder(";force;swaropporecord;");
		sb.append(fid).append(';');
		sb.append(sid).append(';');
		sb.append(bid).append(';');
		sb.append(bindex+1).append(';');
		sb.append(mindex+1).append(';');
		encodeForceSWarRecord(sb, record);
		return sb.toString();
	}

	public static String encodeForceSWarRankRes(SBean.SWarRankRes res)
	{
		StringBuilder sb = new StringBuilder(";force;swarrank;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(res.rank).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeForceSWarTopRanksRes(SBean.SWarTopRanksRes res)
	{
		StringBuilder sb = new StringBuilder(";force;swartopranks;");
		if (res == null)
			sb.append(-1000).append(';');
		else {
			sb.append(res.error).append(';');
			sb.append(res.ranks.size()).append(';');
			for (SBean.SWarTopRank r : res.ranks) {
				sb.append(r.rank).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.fname).append(';');
				sb.append(r.serverName).append(';');
				sb.append(r.iconId).append(';');
				sb.append(r.score).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeForceSWarDonateMoneyRes(int newBadges)
	{
		StringBuilder sb = new StringBuilder(";force;swardonatemoney;");
		sb.append(newBadges).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSWarDonateItemsRes(int newBadges)
	{
		StringBuilder sb = new StringBuilder(";force;swardonateitems;");
		sb.append(newBadges).append(';');
		return sb.toString();
	}
	
	public static String encodeForceSWarDonateRanksRes(boolean history, List<SBean.DBForceSWarDonateRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";force;swardonateranks;");
		if (ranks == null)
			sb.append(0).append(';');
		else {
			sb.append(ranks.size()).append(';');
			for (SBean.DBForceSWarDonateRank r : ranks) {
				sb.append(r.rid).append(';');
				sb.append(r.rname).append(';');
				sb.append(r.iconId).append(';');
				sb.append(r.lvl).append(';');
				if (history)
					sb.append(r.totalDonate).append(';');
				else
					sb.append(r.donate).append(';');
			}
		}
		return sb.toString();
	}
	
	public static String encodeExpatriateQueryRoleResponse(List<String> res)
    {
          StringBuilder sb = new StringBuilder(";expatriate;queryrole");
          for (String s : res)
          {
             sb.append(";").append(s);
          }
          sb.append(";");
          return sb.toString();
    }
	public static String encodeExpiratBossSyncRes(List<DBExpiratBossTimes> times,int day,int level)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;sync;");
		sb.append(level).append(';');
		sb.append(times.size()).append(';');
		for (DBExpiratBossTimes r : times) {
			sb.append(r.id).append(';');
			sb.append(r.times).append(';');
			sb.append(r.buytimes).append(';');	
			sb.append(r.bonusday==day?0:1).append(';');
		}
		return sb.toString();	
	}
	
	public static List<String> encodeDiskBetSyncInfo2(RechargeRankConfig cfg, DBDiskBetInfo dbDiskBetInfo, DBDiskBet diskBet, int i, int j, List<DBDiskBetRank> bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("rechargeRankSync");
		if (cfg == null || dbDiskBetInfo == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(String.valueOf(cfg.getDetailContent()));
			info.add(""+cfg.MIN_RMB);
			info.add(String.valueOf(dbDiskBetInfo.rmb));
			info.add(String.valueOf(j));
			
			info.add(""+cfg.mailatt5.size());
			for(DiskBetItemsCFGS drop :cfg.mailatt5){
				info.add(""+drop.type);
				info.add(""+drop.rankmin);
				info.add(""+drop.rankmax);
				info.add(""+drop.eItems.size());
				for(DropEntryNew drop1 :drop.eItems){
					info.add(""+drop1.type);
					info.add(""+drop1.id);
					info.add(""+drop1.arg);
				}
			}
			
			info.add(""+bOK.size());
			int index =1;
			for(DBDiskBetRank r :bOK){
				info.add(""+r.tId);
				info.add(""+index);
				info.add(""+r.rid);
				info.add(""+r.score);
//				info.add(""+r.rmb);
				info.add(""+r.headIconId);
				info.add(""+r.rname);
				info.add(""+r.lvl);
				index++;
			}
		}
		return info;
	}
	
	public static List<String> encodeDiskBetSyncInfo(DiskBetConfig cfg, DBDiskBetInfo dbDiskBetInfo, DBDiskBet diskBet, int i, int j)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskBetSync");
		if (cfg == null || dbDiskBetInfo == null)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(String.valueOf(1));
			info.add(String.valueOf(cfg.getType()));
			info.add(String.valueOf(cfg.getStartTime()));
			info.add(String.valueOf(cfg.getEndTime()));
			info.add(""+cfg.MIN_SCORE);
			info.add(""+cfg.MIN_RMB);
			
			info.add(String.valueOf(dbDiskBetInfo.score));
			info.add(String.valueOf(dbDiskBetInfo.rmb));
			info.add(String.valueOf(dbDiskBetInfo.times1));
			info.add(String.valueOf(dbDiskBetInfo.times2));
			info.add(String.valueOf(dbDiskBetInfo.times3));
			info.add(String.valueOf(dbDiskBetInfo.times4));
			info.add(String.valueOf(i));
			info.add(String.valueOf(j));
			
			info.add(String.valueOf(diskBet.pool));
			
			info.add(""+cfg.mailatt1.size());
			for(DropTableEntryNew drop1 :cfg.mailatt1){
				DropEntryNew drop = drop1.item;				
				info.add(""+drop.type);
				info.add(""+drop.id);
				info.add(""+drop.arg);
				
			}
			
			info.add(""+cfg.mailatt2.size());
			for(DropTableEntryNew drop1 :cfg.mailatt2){
				DropEntryNew drop = drop1.item;	
				info.add(""+drop.type);
				info.add(""+drop.id);
				info.add(""+drop.arg);				
			}
			
			info.add(""+cfg.mailatt3.size());
			for(DiskBetItemsCFGS drop :cfg.mailatt3){
				info.add(""+drop.type);
				info.add(""+drop.rankmin);
				info.add(""+drop.rankmax);
				info.add(""+drop.eItems.size());
				for(DropEntryNew drop1 :drop.eItems){
					info.add(""+drop1.type);
					info.add(""+drop1.id);
					info.add(""+drop1.arg);
				}
			}
			info.add(""+cfg.mailatt4.size());
			for(DiskBetItemsCFGS drop :cfg.mailatt4){
				info.add(""+drop.type);
				info.add(""+drop.rankmin);
				info.add(""+drop.rankmax);
				info.add(""+drop.eItems.size());
				for(DropEntryNew drop1 :drop.eItems){
					info.add(""+drop1.type);
					info.add(""+drop1.id);
					info.add(""+drop1.arg);
				}
			}
			info.add(""+cfg.mailatt5.size());
			for(DiskBetItemsCFGS drop :cfg.mailatt5){
				info.add(""+drop.type);
				info.add(""+drop.rankmin);
				info.add(""+drop.rankmax);
				info.add(""+drop.eItems.size());
				for(DropEntryNew drop1 :drop.eItems){
					info.add(""+drop1.type);
					info.add(""+drop1.id);
					info.add(""+drop1.arg);
				}
			}
		}
		return info;
	}
	
	public static List<String> encodeDiskrankRes2(int id, short tid, RechargeRankConfig cfg, List<DBDiskBetRank> bOK, int i)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("rechargerank");
		info.add(String.valueOf(id));
		info.add(String.valueOf(tid));
		info.add(String.valueOf(i));
		if (cfg == null ||bOK==null ||bOK.size()<=0)
		{
			info.add(String.valueOf(0));
		}
		else
		{

			info.add(""+bOK.size());
			int index =1;
			for(DBDiskBetRank r :bOK){
				info.add(""+r.tId);
				info.add(""+index);
				info.add(""+r.rid);
				info.add(""+r.score);
//				info.add(""+r.rmb);
				info.add(""+r.headIconId);
				info.add(""+r.rname);
				info.add(""+r.lvl);
			}
			
		}
		return info;
	}
	
	public static List<String> encodeDiskrankRes(int id, short tid, DiskBetConfig cfg, List<DBDiskBetRank> bOK, int i)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskrank");
		info.add(String.valueOf(id));
		info.add(String.valueOf(tid));
		info.add(String.valueOf(i));
		if (cfg == null ||bOK==null ||bOK.size()<=0)
		{
			info.add(String.valueOf(0));
		}
		else
		{
//			if(bOK==null ||bOK.size()<=0){
//				info.add(String.valueOf(0));
//			}

			info.add(""+bOK.size());
			int index =1;
			for(DBDiskBetRank r :bOK){
				info.add(""+r.tId);
				info.add(""+index);
				info.add(""+r.rid);
				info.add(""+r.score);
//				info.add(""+r.rmb);
				info.add(""+r.headIconId);
				info.add(""+r.rname);
				info.add(""+r.lvl);
			}
			
		}
		return info;
	}
	
	public static List<String> encodeDiskrankHisRes(List<DBDiskBetRank> bOK)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskrankHis");
		if (bOK==null ||bOK.size()<=0)
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(""+bOK.size());
			int index =1;
			for(DBDiskBetRank r :bOK){
				info.add(""+r.tId);
				info.add(""+index);
				info.add(""+r.rid);
				info.add(""+r.score);
//				info.add(""+r.rmb);
				info.add(""+r.headIconId);
				info.add(""+r.rname);
				info.add(""+r.lvl);
			}
			
		}
		return info;
	}
	
	public static List<String> encodeDiskbetRes(int id, short tid, DiskBetConfig cfg, List<DropEntryNew> bOK, int i)
	{
		List<String> info = new ArrayList<String>();
		info.add("act");
		info.add("diskbet");
		info.add(String.valueOf(id));
		info.add(String.valueOf(tid));
		info.add(""+i);
		if (cfg == null ||bOK ==null ||bOK.size()<=0 )
		{
			info.add(String.valueOf(0));
		}
		else
		{
			info.add(""+bOK.size());
			for(DropEntryNew r :bOK){
				info.add(""+r.type);
				info.add(""+r.id);
				info.add(""+r.arg);
			}
		}
		return info;
	}
	
	public static String encodeHeroesBossSyncRes(DBHeroesBoss dbHeroesBoss, DBHeroesBossInfo bossInfo)
	{
		StringBuilder sb = new StringBuilder(";heroesBoss;sync;");
		sb.append(bossInfo.buffCount).append(';');
		
		sb.append(dbHeroesBoss.gids1.size()).append(';');
		for (Short r : dbHeroesBoss.gids1) {
			sb.append(r).append(';');
		}
		sb.append(dbHeroesBoss.gids2.size()).append(';');
		for (Short r : dbHeroesBoss.gids2) {
			sb.append(r).append(';');
		}
		sb.append(dbHeroesBoss.gids3.size()).append(';');
		for (Short r : dbHeroesBoss.gids3) {
			sb.append(r).append(';');
		}

		sb.append(bossInfo.times.size()).append(';');
		for (DBHeroesBossTimes r : bossInfo.times) {
			sb.append(r.id).append(';');
			sb.append(r.times).append(';');
		}
		
		sb.append(bossInfo.ranks.size()).append(';');
		for (HeroesRank r : bossInfo.ranks) {
			sb.append(r.type).append(';');
			
			sb.append(r.roleID.roleID).append(';');
			sb.append(r.roleID.serverID).append(';');
			sb.append(r.lvl).append(';');
			sb.append(r.headIconId).append(';');
			sb.append(r.roleName).append(';');
			sb.append(r.serverName).append(';');
			sb.append(r.score1).append(';');
			sb.append(r.score2).append(';');
			sb.append(r.score3).append(';');
			sb.append(r.buffLvl).append(';');
			sb.append(r.attc).append(';');
		}
		
		return sb.toString();	
	}
	
	public static String encodeExpiratBossStartAttackRes(short combatId,int ok, List<SBean.DropEntry> drop)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;startBattle;");
		sb.append(combatId).append(';');
		sb.append(ok).append(';');
		if(drop==null||drop.size()==0){
			sb.append(0).append(';');	
		}else{
			sb.append(drop.size()).append(';');
			for(DropEntry i:drop){
				sb.append(i.type).append(';');
				sb.append(i.id).append(';');
				sb.append(i.arg).append(';');
			}
		}
		return sb.toString();				
	}
	
	public static String encodeHeroesBossFinishAttackRes(short type ,int damage,int ok)
	{
		StringBuilder sb = new StringBuilder(";heroesBoss;finishBattle;");
		sb.append(type).append(';');
		sb.append(damage).append(';');
		sb.append(ok).append(';');	
		return sb.toString();	
	}
	
	public static String encodeBestowUp(short gid, short ok)
	{
		StringBuilder sb = new StringBuilder(";bestow;upbestow;");
		
		sb.append(gid).append(';');
		sb.append(ok).append(';');
		return sb.toString();
	}
	
	public static String encodeBlessUp(short type, List<Short> gids, short ok)
	{
		StringBuilder sb = new StringBuilder(";bless;upbless;");
		
		sb.append(type).append(';');
		sb.append(ok).append(';');
		sb.append(gids.size()).append(';');
		for(short gid : gids)
		{
			sb.append(gid).append(';');
		}
		return sb.toString();
	}
	
	
	public static String encodeBestowItem(short iid, short ok)
	{
		StringBuilder sb = new StringBuilder(";bestow;bestowItem;");
		
		sb.append(iid).append(';');
		sb.append(ok).append(';');
		return sb.toString();
	}
	
	public static String encodeActiveheadiconRes(short type, short ok)
	{
		StringBuilder sb = new StringBuilder(";headicon;activeheadicon;");
		
		sb.append(type).append(';');
		sb.append(ok).append(';');
		return sb.toString();
	}
	
	public static String encodeHeadiconSyncRes(DBHeadIcon ok)
	{
		StringBuilder sb = new StringBuilder(";headicon;headiconSync;");
		if(ok==null){
			sb.append(0).append(';');
		}else{
			sb.append(1).append(';');
			sb.append(ok.headcur).append(';');
			sb.append(ok.headicon.size()).append(';');
			for(DBHeadIconList icon : ok.headicon){
				sb.append(icon.headid).append(';');
				sb.append(icon.headtime).append(';');
			}
		}	
		
		return sb.toString();
	}
	
	public static String encodeHeroesBuyTimesRes(boolean ok)
	{
		StringBuilder sb = new StringBuilder(";heroesBoss;buffTimes;");
		sb.append(ok?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeHeroesBossDamageRanksRes(List<HeroesRank> ranks)
	{
		StringBuilder sb = new StringBuilder(";heroesBoss;rank;");
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (HeroesRank r : ranks) {
				sb.append(r.type).append(';');
				
				sb.append(r.roleID.roleID).append(';');
				sb.append(r.roleID.serverID).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.headIconId).append(';');
				sb.append(r.roleName).append(';');
				sb.append(r.serverName).append(';');
				sb.append(r.score1).append(';');
				sb.append(r.score2).append(';');
				sb.append(r.score3).append(';');
				sb.append(r.buffLvl).append(';');
				sb.append(r.attc).append(';');
			}
		}
		return sb.toString();	
	}
		
	
	public static String encodeExpiratBossFinishAttackRes(int damage,short combatId,int ok,List<SBean.DropEntry> drop)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;finishBattle;");
		sb.append(damage).append(';');
		sb.append(combatId).append(';');
		sb.append(ok).append(';');
		if(drop==null||drop.size()==0){
			sb.append(0).append(';');	
		}else{
			sb.append(drop.size()).append(';');
			for(DropEntry i:drop){
				sb.append(i.type).append(';');
				sb.append(i.id).append(';');
				sb.append(i.arg).append(';');
			}
		}
		
		return sb.toString();	
	}
	public static String encodeBuyTimesRes(short combatId,boolean ok, int times)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;buyTimes;");
		sb.append(combatId).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(times).append(';');
		return sb.toString();
	}
	
	public static String encodeExpiratBossDamageRanksRes(int rid, short combat,List<ExpiratBossManager.DamageRank> ranks, int damage)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;rank;");
		sb.append(rid).append(';');
		sb.append(combat).append(';');
		sb.append(damage).append(';');
		if (ranks == null) 
			sb.append(0).append(';');
		else {
			int size = ranks.size();
			sb.append(size).append(';');
			for (ExpiratBossManager.DamageRank r : ranks) {
				sb.append(r.combatId).append(';');
				sb.append(r.rank).append(';');
				sb.append(r.rid).append(';');
				sb.append(r.damage).append(';');
				sb.append(r.icon).append(';');
				sb.append(r.name).append(';');
				sb.append(r.lvl).append(';');
				sb.append(r.serverName).append(';');
			}
		}
		return sb.toString();	
	}
	
	public static String encodeExpiratBossRankRes(short combatId,SBean.ExpiratBossRankRes res)
	{
		StringBuilder sb = new StringBuilder(";expiratBoss;bonus;");
		sb.append(combatId).append(';');
		if (res == null){
			sb.append(-1000).append(';');
		    sb.append(-1).append(';');	
		}	
		else {
			sb.append(res.error).append(';');
			sb.append(res.rank).append(';');
		}
		return sb.toString();
	}
	
	public static String encodeGeneralsSyncOfficialRes(short gid, SBean.DBGeneralOfficial official)
	{
		StringBuilder sb = new StringBuilder(";generals;syncofficial;");
		sb.append(gid).append(';');
		if (official == null){
			sb.append(1).append(';');
			sb.append(0).append(';');
			sb.append(0).append(';');
		}	
		else {
			sb.append(official.lvl+1).append(';');
			sb.append(official.exp).append(';');
			sb.append(official.skillLvl).append(';');
		}
		return sb.toString();
	}

	public static String encodeGeneralsAddOfficialExpRes(short gid, int itemCount, boolean ok, int itemUsed)
	{
		StringBuilder sb = new StringBuilder(";generals;addofficialexp;");
		sb.append(gid).append(';');
		sb.append(itemCount).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(itemUsed).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsUpgradeOfficialSkillRes(short gid, int skillLvl)
	{
		StringBuilder sb = new StringBuilder(";generals;upgradeofficialskill;");
		sb.append(gid).append(';');
		sb.append(skillLvl).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsResetOfficialRes(short gid, boolean ok, int itemCount)
	{
		StringBuilder sb = new StringBuilder(";generals;resetofficial;");
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(itemCount).append(';');
		return sb.toString();
	}
	
	public static String encodeGeneralsResetOfficialSkillRes(short gid, boolean ok, int itemCount)
	{
		StringBuilder sb = new StringBuilder(";generals;resetofficialskill;");
		sb.append(gid).append(';');
		sb.append(ok?1:0).append(';');
		sb.append(itemCount).append(';');
		return sb.toString();
	}
	
	public static String encodeSyncVipShow(boolean show)
	{
		StringBuilder sb = new StringBuilder(";gbt;syncvipshow;");
		sb.append(show?1:0).append(';');
		return sb.toString();
	}
	
	public static String encodeVipShowRes(boolean show)
	{
		StringBuilder sb = new StringBuilder(";gbt;vipshow;");
		sb.append(show?1:0).append(';');
		return sb.toString();
	}
	
	 public static String encodeOpenMyTreasureMap(DBTreasureMap map)
	 {
	     StringBuilder sb = new StringBuilder(";treasure;openMyTreasureMap;");
	     if (map == null) {
	         sb.append(0).append(";");
	     } else {
	         sb.append(1).append(";");
	         sb.append(map.id).append(";");
	         sb.append(map.diggers.size()).append(";");
	         for (int i : map.diggers) {
                sb.append(i).append(";");
	         }
	         sb.append(map.rewards.size()).append(";");
	         for (DBTreaureReward reward : map.rewards)
            {
	             sb.append(reward.num).append(";");
	             sb.append(reward.dropEntry.id).append(";");
	             sb.append(reward.dropEntry.type).append(";");
	             sb.append(reward.isOpen).append(";");
	             sb.append(reward.diggerName == null ? "" : reward.diggerName).append(";");
            }
	     }
	     return sb.toString();
	 }
	 
	 public static String encodeAllTreasureMap(int rid, int digCount, int lastRefreshTime, int pageCount, List<DBTreasureMap> list) 
	 {
	     StringBuilder sb = new StringBuilder(";treasure;allTreasure;");
	     sb.append(digCount).append(";");
	     sb.append(lastRefreshTime).append(";");
	     sb.append(pageCount).append(";");
	     sb.append(list.size()).append(";");
	     for (DBTreasureMap dbTreasureMap : list) {
	        sb.append(dbTreasureMap.rid).append(";");
            sb.append(dbTreasureMap.rname).append(";");
            sb.append(dbTreasureMap.otherDigCnt).append(";");
            sb.append(dbTreasureMap.diggers.contains(rid) ? 1 : 0).append(";");
	     }
	     return sb.toString();
	 }

	 public static String encodeEnterTreasureMap(DBTreasureMap map, int rid)
     {
         StringBuilder sb = new StringBuilder(";treasure;enterTreasureMap;");
         if (map == null) {
             sb.append(0).append(";");
         } else {
             if (map.diggers.contains(rid)) {
                 sb.append(2).append(";");
             } else {
                 sb.append(1).append(";");
                 sb.append(map.otherDigCnt).append(";");
                 sb.append(map.rewards.size()).append(";");
                 for (DBTreaureReward reward : map.rewards)
                 {
                     sb.append(reward.num).append(";");
                     sb.append(reward.dropEntry.id).append(";");
                     sb.append(reward.dropEntry.type).append(";");
                     sb.append(reward.isOpen).append(";");
                     sb.append(reward.diggerName == null ? "" : reward.diggerName).append(";");
                 }
             }
         }
         return sb.toString();
     }
	
	public static String[] decode(String data)
	{
		return data.split(";");
	}

    public static String encodeBeMonsterStartAttack(DBBeMonsterLineup lineup)
    {
        StringBuilder sb = new StringBuilder(";bemonster;attack;");
        if (lineup == null) {
            sb.append("0").append(";");
        } else {
            sb.append("1").append(";");
            sb.append(lineup.id).append(";");
            sb.append(lineup.upHpCnt).append(";");
            sb.append(lineup.hpPercent).append(";");
            
            sb.append(lineup.pet.id).append(';');
            sb.append(lineup.pet.lvl).append(';');
            sb.append(lineup.pet.evoLvl).append(';');
            sb.append(lineup.pet.growthRate).append(';');
            sb.append(lineup.pet.name).append(';');
            sb.append(lineup.pet.deformStage).append(';');
            
            sb.append(lineup.generals.size()).append(";");
            for (SBean.DBBeMonsterGeneral g : lineup.generals) {
                sb.append(g.hp).append(";");
                sb.append(g.general.id).append(';');
                sb.append(g.general.lvl).append(';');
                sb.append(g.general.advLvl).append(';');
                sb.append(g.general.evoLvl).append(';');
            }
        }
        return sb.toString();
    }

    public static String encodeBeMonsterSync(int state, DBBeMonsterAttacker attacker, DBBeMonsterBoss boss,
            DBBeMonsterBoss nowBoss)
    {
        StringBuilder sb = new StringBuilder(";bemonster;sync;");
        sb.append(state).append(";");
        if (state == BeMonsterManager.STATE_REGISTER) {
            if (boss == null) {
                sb.append("0").append(";");
            } else {
                sb.append("1").append(";");
                sb.append(boss.lineup.size()).append(";");
                for (SBean.DBBeMonsterLineup lineup : boss.lineup) {
                    sb.append(lineup.id).append(";");
                    sb.append(lineup.pet.id).append(";");
                    sb.append(lineup.upHpCnt).append(";");
                    sb.append(lineup.hpPercent).append(";");
                    sb.append(lineup.generals.size()).append(";");
                    for (SBean.DBBeMonsterGeneral g : lineup.generals) {
                        sb.append(g.gid).append(";");
                        sb.append(g.hp).append(";");
                    }
                }
            }
        } else if (state == BeMonsterManager.STATE_REGISTER_END) {
            if (boss != null && nowBoss != null && boss.rid == nowBoss.rid) {
                sb.append("1").append(";");
                sb.append(boss.lineup.size()).append(";");
                for (SBean.DBBeMonsterLineup lineup : boss.lineup) {
                    sb.append(lineup.id).append(";");
                    sb.append(lineup.pet.id).append(";");
                    sb.append(lineup.upHpCnt).append(";");
                    sb.append(lineup.hpPercent).append(";");
                    sb.append(lineup.generals.size()).append(";");
                    for (SBean.DBBeMonsterGeneral g : lineup.generals) {
                        sb.append(g.gid).append(";");
                        sb.append(g.hp).append(";");
                    }
                }
            } else {
                sb.append("0").append(";");
            }
        } else if (state == BeMonsterManager.STATE_FIGHT_START) {
            if (boss != null && nowBoss != null && boss.rid == nowBoss.rid) {
                sb.append("1").append(";");
            } else {
                sb.append("0").append(";");
            }
            if (attacker == null) {
                sb.append("0").append(";");
            } else {
                sb.append(attacker.attackCnt).append(";");
            }
            sb.append(nowBoss.lineup.size()).append(";");
            for (SBean.DBBeMonsterLineup lineup : nowBoss.lineup) {
                sb.append(lineup.id).append(";");
                sb.append(lineup.pet.id).append(";");
                sb.append(lineup.upHpCnt).append(";");
                sb.append(lineup.hpPercent).append(";");
                sb.append(lineup.generals.size()).append(";");
                for (SBean.DBBeMonsterGeneral g : lineup.generals) {
                    sb.append(g.gid).append(";");
                    sb.append(g.hp).append(";");
                }
            }
        }
        return sb.toString();
    }

}

package i3k.gs;

import org.apache.log4j.Logger;

import i3k.SBean;
import i3k.TLog;
import i3k.DBRole;
import i3k.gs.Role.MSDKInfo;

import java.util.List;
import java.util.ArrayList;


public class TLogger 
{
	private GameServer gs;
	private Logger filelogger = Logger.getLogger("tLogger");
	private int seq = 0;
	public TLogger(GameServer gs)
	{
		this.gs = gs;
	}
	
	int getNextSeq()
	{
		int curSeq = 0;
		synchronized (this)
		{
			curSeq = ++seq;
		}
		return curSeq;
	}
	
	public void log(String log)
	{
		send(gs, log);
		filelogger.info(log);
	}
	
	public void log(List<String> logs)
	{
		if (logs != null)
		{
			for (String log : logs)
			{
				log(log);
			}	
		}
	}
	
	public void logRegister(Role role)
	{
		log(toTlogRegisterStr(getNextSeq(), role));
	}
	
	public void logLogin(Role role)
	{
		log(toTlogLoginStr(getNextSeq(), role));
	}
	
	public void logLogout(Role role, int onlineTime, int friendCount)
	{
		log(toTlogLogoutStr(getNextSeq(), role, onlineTime, friendCount));
	}
	
	public void logBattleGeneralsState(Role role, int power, int g1id, int g1lvl, int g1star, int g1adv, int g2id, int g2lvl, int g2star, int g2adv, int g3id, int g3lvl, int g3star, int g3adv, int g4id, int g4lvl, int g4star, int g4adv, int g5id, int g5lvl, int g5star, int g5adv, int pid, int plvl, int pstar, int battleType)
	{
		log(toTlogBattleGeneralsStateStr(getNextSeq(), role, power, g1id, g1lvl, g1star, g1adv, g2id, g2lvl, g2star, g2adv, g3id, g3lvl, g3star, g3adv, g4id, g4lvl, g4star, g4adv, g5id, g5lvl, g5star, g5adv, pid, plvl, pstar, battleType));
	}
	
	public void logForce(int fid, String fname, int flvl, int fmcnt, int fvitality, int fvitalitymemcnt)
	{
		log(toTlogForceStateStr(getNextSeq(), fid, fname, flvl, fmcnt, fvitality, fvitalitymemcnt));
	}
	
	public void logSns(Role role, int recNum, int count, int type, int subType)
	{
		log(toTlogSnsStr(getNextSeq(), role, recNum, count, type, subType));
	}

	public void logIDIPCMD(String openID, int itemtype, int itemid, int itemnum, String serialnum, int sourceid, String cmd)
	{
		log(toTlogIDIPCMDStr(openID, itemtype, itemid, itemnum, serialnum, sourceid, cmd));
	}
	
	public void logEvent(Role role, TLogEvent record)
	{
		log(toTlogEventStr(role, record));
	}
	
	public void logGameServerState()
	{
		log(toTlogGameServerStateStr());
	}

	
	private static void send(GameServer gs, String log)
	{
		gs.getRPCManager().sendTLog(log);
	}	
	
	public String toTlogRegisterStr(int sequence, Role role)
	{
		return toTlogRegisterStrImpl(sequence, gs, role.msdkInfo, role.id);
	}
	
	public String toTlogLoginStr(int sequence, Role role)
	{
		return toTlogLoginStrImpl(sequence, gs, role.msdkInfo, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), role.forceInfo.id);
	}
	
	public String toTlogLogoutStr(int sequence, Role role, int onlineTime, int friendCount)
	{
		return toTlogLogoutStrImpl(sequence, gs, role.msdkInfo, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), onlineTime, friendCount, role.forceInfo.id);
	}
	
	public String toTlogItemFlowStr(int sequence, Role role, int eventID, int ctype, int cid, int changeCount, int afterCount,  int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCommonFlowStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), eventID, ctype, cid, changeCount, afterCount, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogItemFlowStr(int sequence, DBRole role, int eventID, int ctype, int cid, int changeCount, int afterCount,  int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCommonFlowStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), eventID, ctype, cid, changeCount, afterCount, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogItemFlow2Str(int sequence, Role role, int eventID, String itemsChange,  int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCommonFlow2StrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), eventID, itemsChange, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogItemFlow2Str(int sequence, DBRole role, int eventID, String itemsChange,  int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCommonFlow2StrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), eventID, itemsChange, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogLevelUpStr(int sequence, Role role, int level, int costTime)
	{
		return toTlogLevelUpStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), costTime);
	}
	
	public String toTlogLevelUpStr(int sequence, DBRole role, int level, int costTime)
	{
		return toTlogLevelUpStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), costTime);
	}
	
	public String toTlogBattleStr(int sequence, Role role, int level, int battleType, int battleID, int checkPointID, int score, int costTime, int combatType)
	{
		return toTlogBattleStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleType, battleID, checkPointID, score, costTime, combatType);
	}
	
	public String toTlogBattleStr(int sequence, DBRole role, int level, int battleType, int battleID, int checkPointID, int score, int costTime, int combatType)
	{
		return toTlogBattleStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleType, battleID, checkPointID, score, costTime, combatType);
	}
	
	public String toTlogChibiBattleStr(int sequence, Role role, int level, int battleID, int dlvl, int score, int costTime)
	{
		return toTlogChibiBattleStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleID, dlvl, score, costTime);
	}
	
	public String toTlogChibiBattleStr(int sequence, DBRole role, int level, int battleID, int dlvl, int score, int costTime)
	{
		return toTlogChibiBattleStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleID, dlvl, score, costTime);
	}
	
	public String toTlogBaguazhengBattleStr(int sequence, Role role, int level, int battleID, int dlvl, int score, int costTime)
	{
		return toTlogBaguazhengBattleStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleID, dlvl, score, costTime);
	}
	
	public String toTlogBaguazhengBattleStr(int sequence, DBRole role, int level, int battleID, int dlvl, int score, int costTime)
	{
		return toTlogBaguazhengBattleStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), battleID, dlvl, score, costTime);
	}
	
	public String toTlogArenaStr(int sequence, Role role, int level, int rank, int orank, int result)
	{
		return toTlogArenaStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), rank, orank, result);
	}
	
	public String toTlogArenaStr(int sequence, DBRole role, int level, int rank, int orank, int result)
	{
		return toTlogArenaStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), rank, orank, result);
	}
	
	public String toTlogDailyTaskStr(int sequence, Role role, int level, int dtID)
	{
		return toTlogDailyTaskStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), dtID);
	}
	
	public String toTlogDailyTaskStr(int sequence, DBRole role, int level, int dtID)
	{
		return toTlogDailyTaskStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), dtID);
	}
	
	public String toTlogDailyTaskRewardStr(int sequence, Role role, int level, int dtID)
	{
		return toTlogDailyTaskRewardStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), dtID);
	}
	
	public String toTlogDailyTaskRewardStr(int sequence, DBRole role, int level, int dtID)
	{
		return toTlogDailyTaskRewardStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), dtID);
	}
	
	public String toTlogChestStr(int sequence, Role role, int level, int chestType, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return toTlogChestStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), chestType, buyCount, buyPrice, buyPriceUnit);
	}
	
	public String toTlogChestStr(int sequence, DBRole role, int level, int chestType, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return toTlogChestStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), chestType, buyCount, buyPrice, buyPriceUnit);
	}
	
	public String toTlogShopStr(int sequence, Role role, int level, int shopType, int goodsType, int goodsID, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return toTlogShopStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), shopType, goodsType, goodsID, buyCount, buyPrice, buyPriceUnit);
	}
	
	public String toTlogShopStr(int sequence, DBRole role, int level, int shopType, int goodsType, int goodsID, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return toTlogShopStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), shopType, goodsType, goodsID, buyCount, buyPrice, buyPriceUnit);
	}
	
	public String toTlogGuideStr(int sequence, Role role, int level, int guideID)
	{
		return toTlogGuideStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), guideID);
	}
	
	public String toTlogGuideStr(int sequence, DBRole role, int level, int guideID)
	{
		return toTlogGuideStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), guideID);
	}
	
	public String toTlogTaskStr(int sequence, Role role, int level, int taskID, int taskSeq)
	{
		return toTlogTaskStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), taskID, taskSeq);
	}
	
	public String toTlogTaskStr(int sequence, DBRole role, int level, int taskID, int taskSeq)
	{
		return toTlogTaskStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, role.getStonePayTotal(), taskID, taskSeq);
	}
	
	public String toTlogCheckinStr(int sequence, Role role, int level, int checkinMonthID, int checkinDaySeq)
	{
		return toTlogCheckinStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), checkinMonthID, checkinDaySeq);
	}
	
	public String toTlogCheckinStr(int sequence, DBRole role, int level, int checkinMonthID, int checkinDaySeq)
	{
		return toTlogCheckinStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), checkinMonthID, checkinDaySeq);
	}
	
	public String toTlogEquipGildStr(int sequence, Role role, int level, int gID, int eID, int posID, int equipLvl)
	{
		return toTlogEquipGildStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), gID, eID, posID, equipLvl);
	}
	
	public String toTlogEquipGildStr(int sequence, DBRole role, int level, int gID, int eID, int posID, int equipLvl)
	{
		return toTlogEquipGildStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), gID, eID, posID, equipLvl);
	}
	
	public String toTlogMarchStr(int sequence, Role role, int level, int stageID, int win, int costTime)
	{
		return toTlogMarchStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), stageID, win, costTime);
	}
	
	public String toTlogMarchStr(int sequence, DBRole role, int level, int stageID, int win, int costTime)
	{
		return toTlogMarchStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), stageID, win, costTime);
	}
	
	public String toTlogMarchRewardStr(int sequence, Role role, int level, int stageID, int points)
	{
		return toTlogMarchRewardStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), stageID, points);
	}
	
	public String toTlogMarchRewardStr(int sequence, DBRole role, int level, int stageID, int points)
	{
		return toTlogMarchRewardStrImpl(sequence, gs, role.openID, role.id, level, role.lvlVIP, (int)role.getStonePayTotal(), stageID, points);
	}
	
	public String toTlogBattleGeneralsStateStr(int sequence, Role role, int power, int g1id, int g1lvl, int g1star, int g1adv, int g2id, int g2lvl, int g2star, int g2adv, int g3id, int g3lvl, int g3star, int g3adv, int g4id, int g4lvl, int g4star, int g4adv, int g5id, int g5lvl, int g5star, int g5adv, int pid, int plvl, int pstar, int battleType)
	{
		return toTlogBattleGeneralsStateStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, role.getStonePayTotal(), power, g1id, g1lvl, g1star, g1adv, g2id, g2lvl, g2star, g2adv, g3id, g3lvl, g3star, g3adv, g4id, g4lvl, g4star, g4adv, g5id, g5lvl, g5star, g5adv, pid, plvl, pstar, battleType);
	}
	
	public String toTlogGeneralEvolutionStr(int sequence, Role role, int gid, int glvl, int gstar, int gadv, int gEvoType, int glvlBefore, int gstarBefore, int gadvBefore)
	{
		return toTlogGeneralEvolutionStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), gid, glvl, gstar, gadv, gEvoType, glvlBefore, gstarBefore, gadvBefore);
	}
	
	public String toTlogGeneralEvolutionStr(int sequence, DBRole role, int gid, int glvl, int gstar, int gadv, int gEvoType, int glvlBefore, int gstarBefore, int gadvBefore)
	{
		return toTlogGeneralEvolutionStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), gid, glvl, gstar, gadv, gEvoType, glvlBefore, gstarBefore, gadvBefore);
	}
	
	public String toTlogForceEventStr(int sequence, Role role, int fid, int fevent, int arg1, int arg2)
	{
		return toTlogForceEventStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), fid, fevent, arg1, arg2);
	}
	
	public String toTlogForceEventStr(int sequence, DBRole role, int fid, int fevent, int arg1, int arg2)
	{
		return toTlogForceEventStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), fid, fevent, arg1, arg2);
	}
	
	public String toTlogForceStateStr(int sequence, int fid, String fname, int flvl, int fmcnt, int fvitality, int fvitalitymemcnt)
	{
		return toTlogForceStateStrImpl(sequence, gs, fid, fname, flvl, fmcnt, fvitality, fvitalitymemcnt);
	}
	
	public String toTlogPayStr(int sequence, Role role, int level, int viplvl, int paytotal, int rviplvlBefore, int rpayBefore, int paylvl, int payamount, int stone)
	{
		return toTlogPayStrImpl(sequence, gs, role.msdkInfo.openID, role.id, level, viplvl, paytotal, rviplvlBefore, rpayBefore, paylvl, payamount, stone);
	}
	
	public String toTlogPayStr(int sequence, DBRole role, int level, int viplvl, int paytotal, int rviplvlBefore, int rpayBefore, int paylvl, int payamount, int stone)
	{
		return toTlogPayStrImpl(sequence, gs, role.openID, role.id, level, viplvl, paytotal, rviplvlBefore, rpayBefore, paylvl, payamount, stone);
	}
	
	public String toTlogSnsStr(int sequence, Role role, int recNum, int count, int type, int subType)
	{
		return toTlogSnsStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), recNum, count, type, subType);
	}
	
	public String toTlogSnsStr(int sequence, DBRole role, int recNum, int count, int type, int subType)
	{
		return toTlogSnsStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), recNum, count, type, subType);
	}
	
	public String toTlogPetPoundStr(int sequence, Role role, int iEventType, int iEventArg)
	{
		return toTlogPetPoundStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), iEventType, iEventArg);
	}
	
	public String toTlogPetPoundStr(int sequence, DBRole role, int iEventType, int iEventArg)
	{
		return toTlogPetPoundStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), iEventType, iEventArg);
	}
	
	public String toTlogCityWarStr(int sequence, Role role, int type, int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCityWarStrImpl(sequence, gs, role.msdkInfo.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), type, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogCityWarStr(int sequence, DBRole role, int type, int arg1, int arg2, int arg3, int arg4)
	{
		return toTlogCityWarStrImpl(sequence, gs, role.openID, role.id, role.lvl, role.lvlVIP, (int)role.getStonePayTotal(), type, arg1, arg2, arg3, arg4);
	}
	
	public String toTlogSecRoundStartStr(Role role, int startTime, int clientTime, int vit, int combatID, int combatType, int diffculty, int chibiType, int baguaType, int lvlReq, int marchState, int costVit, int confFightScore, int realFightScore, int waveCount, int monsterCount, int bossCount, int immune)
	{
		return toTlogSecRoundStartStrImpl(gs, startTime, role.msdkInfo.openID, clientTime, role.name, role.money, role.stone, vit, combatID, combatType, diffculty, chibiType, baguaType, lvlReq, marchState, costVit, confFightScore, realFightScore, waveCount, monsterCount, bossCount, immune);
	}
	
	public String toTlogSecRoundStartStr(DBRole role, int startTime, int clientTime, int vit, int combatID, int combatType, int diffculty, int chibiType, int baguaType, int lvlReq, int marchState, int costVit, int confFightScore, int realFightScore, int waveCount, int monsterCount, int bossCount, int immune)
	{
		return toTlogSecRoundStartStrImpl(gs, startTime, role.openID, clientTime, role.name, role.money, role.stone, vit, combatID, combatType, diffculty, chibiType, baguaType, lvlReq, marchState, costVit, confFightScore, realFightScore, waveCount, monsterCount, bossCount, immune);
	}
	
	public String toTlogSecRoundEndStr(Role role, int EndTime, int clientTime, int cheat, int endLost, int star, int cexp, int cmoney, int waveCount, int dpsTotal, String cgeneral, String citem, String cequip, int serverTimeCost, int clientTimeCost, int cilentSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		return toTlogSecRoundEndStrImpl(gs, EndTime, role.msdkInfo.openID, clientTime, role.msdkInfo.clientVer, role.msdkInfo.ip, cheat, endLost, star, cexp, cmoney, waveCount, dpsTotal, cgeneral, citem, cequip, serverTimeCost, clientTimeCost, cilentSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
	}
	
	public String toTlogSecRoundEndStr(DBRole role, int EndTime, int clientTime, int cheat, int endLost, int star, int cexp, int cmoney, int waveCount, int dpsTotal, String cgeneral, String citem, String cequip, int serverTimeCost, int clientTimeCost, int cilentSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		return toTlogSecRoundEndStrImpl(gs, EndTime, role.openID, clientTime, "", "", cheat, endLost, star, cexp, cmoney, waveCount, dpsTotal, cgeneral, citem, cequip, serverTimeCost, clientTimeCost, cilentSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
	}
	
	public String toTlogSecTalkStr(Role role, String receiveRoleOpenID, int receiveRoleLvl, int chatType, String charTitle, String chatContent)
	{
		return toTlogSecTalkStrImpl(gs, role.msdkInfo.openID, role.lvl, role.msdkInfo.ip, receiveRoleOpenID, receiveRoleLvl, chatType, charTitle, chatContent);
	}
	
	public String toTlogSecTalkStr(DBRole role, String receiveRoleOpenID, int receiveRoleLvl, int chatType, String charTitle, String chatContent)
	{
		return toTlogSecTalkStrImpl(gs, role.openID, role.lvl, "", receiveRoleOpenID, receiveRoleLvl, chatType, charTitle, chatContent);
	}
	
	public String toTlogIDIPCMDStr(String openID, int itemtype, int itemid, int itemnum, String serialnum, int sourceid, String cmd)
	{
		return toTlogIDIPCMDStrImpl(gs, openID, itemtype, itemid, itemnum, serialnum, sourceid, cmd);
	}
	
	
	public String toTlogGameServerStateStr()
	{
		return toTlogGameServerStateStrImpl(gs);
	}
	private static String toTlogRegisterStrImpl(int sequence, GameServer gs, MSDKInfo msdkInfo, int rid)
	{
		return new TLog.PlayerRegister(
				  new Integer(gs.getConfig().id).toString()
				, gs.getTimeStampStr()
				, sequence
				, msdkInfo.gameappID
				, msdkInfo.platID
				, msdkInfo.openID
				, rid
				, msdkInfo.clientVer.replaceAll("\\|", "")
				, msdkInfo.systemSoftware.replaceAll("\\|", "")
				, msdkInfo.systemHardware.replaceAll("\\|", "")
				, msdkInfo.telecomOper.replaceAll("\\|", "")
				, msdkInfo.network.replaceAll("\\|", "")
				, msdkInfo.screenWidth
				, msdkInfo.screenHeight
				, msdkInfo.density
				, msdkInfo.loginChannel
				, msdkInfo.cpuHardware.replaceAll("\\|", "")
				, msdkInfo.memory
				, msdkInfo.gLRender.replaceAll("\\|", "")
				, msdkInfo.gLVersion.replaceAll("\\|", "")
				, msdkInfo.deviceID.replaceAll("\\|", "")
				).toString();
	}
	
	private static String toTlogLoginStrImpl(int sequence, GameServer gs, MSDKInfo msdkInfo, int rid, int rlvl, int rviplvl, int rpay, int forceID)
	{
		return new TLog.PlayerLogin(
				  new Integer(gs.getConfig().id).toString()
				, gs.getTimeStampStr()
				, sequence
				, msdkInfo.gameappID
				, msdkInfo.platID
				, msdkInfo.openID
				, rid
				, rlvl
				, rviplvl
				, rpay
				, msdkInfo.clientVer.replaceAll("\\|", "")
				, msdkInfo.systemHardware.replaceAll("\\|", "")
				, msdkInfo.telecomOper.replaceAll("\\|", "")
				, msdkInfo.network.replaceAll("\\|", "")
				, msdkInfo.loginChannel
				, forceID).toString();
		
	}
	
	private static String toTlogLogoutStrImpl(int sequence, GameServer gs, MSDKInfo msdkInfo, int rid, int rlvl, int rviplvl, int rpay, int onlineTime, int friendCount, int forceID)
	{		
		return new TLog.PlayerLogout(
				  new Integer(gs.getConfig().id).toString()
				, gs.getTimeStampStr()
				, sequence
				, msdkInfo.gameappID
				, msdkInfo.platID
				, msdkInfo.openID
				, rid
				, onlineTime
				, rlvl
				, rviplvl
				, rpay
				, friendCount
				, msdkInfo.clientVer.replaceAll("\\|", "")
				, msdkInfo.systemHardware.replaceAll("\\|", "")
				, msdkInfo.telecomOper.replaceAll("\\|", "")
				, msdkInfo.network.replaceAll("\\|", "")
				, forceID).toString();
	}

	
	public static String toTlogCommonFlowStrImpl(int sequence, GameServer gs, String openID, int rid, int rlvl, int rviplvl, int rpay, int eventID, int ctype, int cid, int changeCount, int afterCount,  int arg1, int arg2, int arg3, int arg4)
	{
		return new TLog.PlayerCommonChangeFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence, 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				rlvl,
				rviplvl,
				rpay,
				eventID,
				ctype, 
				cid, 
				changeCount,
				afterCount,
				arg1, 
				arg2,
				arg3,
				arg4).toString();
	}
	
	public static String toTlogCommonFlow2StrImpl(int sequence, GameServer gs, String openID, int rid, int rlvl, int rviplvl, int rpay, int eventID, String itemsChange, int arg1, int arg2, int arg3, int arg4)
	{
		return new TLog.PlayerCommonChangeFlow2(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence, 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				rlvl,
				rviplvl,
				rpay,
				eventID,
				itemsChange, 
				arg1, 
				arg2,
				arg3,
				arg4).toString();
	}

	
	public static String toTlogLevelUpStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int costTime)
	{
		return new TLog.PlayerLevelUpFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid, 
				level,
				rviplvl,
				rpay,
				costTime).toString();
	}
	
	public static String toTlogBattleStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int battleType, int battleID, int checkPiontID, int score, int costTime, int combatType)
	{
		return new TLog.PlayerBattleFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				battleType,
				battleID,
				checkPiontID,
				score,
				costTime,
				combatType).toString();
	}
	
	public static String toTlogChibiBattleStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int battleID, int dlvl, int score, int costTime)
	{
		return new TLog.PlayerChibiBattleFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				battleID,
				dlvl,
				score,
				costTime).toString();
	}
	
	public static String toTlogBaguazhengBattleStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int battleID, int dlvl, int score, int costTime)
	{
		return new TLog.PlayerBaguazhengBattleFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				battleID,
				dlvl,
				score,
				costTime).toString();
	}
	
	public static String toTlogArenaStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int rank, int orank, int result)
	{
		return new TLog.PlayerArenaFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				rank,
				orank,
				result).toString();
	}
	
	public static String toTlogDailyTaskStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int dtID)
	{
		return new TLog.PlayerDailyTaskFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				dtID).toString();
	}
	
	public static String toTlogDailyTaskRewardStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int dtID)
	{
		return new TLog.PlayerDailyTaskRewardFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				dtID).toString();
	}
	
	public static String toTlogChestStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int chestType, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return new TLog.PlayerChestFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				chestType,
				buyCount,
				buyPrice,
				buyPriceUnit).toString();
	}
	
	public static String toTlogShopStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int shopType, int goodsType, int goodsID, int buyCount, int buyPrice, int buyPriceUnit)
	{
		return new TLog.PlayerShopFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				shopType,
				goodsType,
				goodsID,
				buyCount,
				buyPrice,
				buyPriceUnit).toString();
	}
	
	public static String toTlogGuideStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int guideID)
	{
		return new TLog.NewPlayerGuideRecord(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				guideID).toString();
	}
	
	public static String toTlogTaskStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int taskID, int taskSeq)
	{
		return new TLog.PlayerTaskRecord(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				taskID,
				taskSeq).toString();
	}
	
	public static String toTlogCheckinStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int checkinMonthID, int checkinDaySeq)
	{
		return new TLog.PlayerCheckinRecord(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				checkinMonthID,
				checkinDaySeq).toString();
	}
	
	public static String toTlogEquipGildStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int gID, int eID, int posID, int equipLvl)
	{
		return new TLog.PlayerEquipGildFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)), 
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				gID,
				eID,
				posID,
				equipLvl).toString();
	}
	
	public static String toTlogMarchStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int stageID, int win, int costTime)
	{
		return new TLog.PlayerMarchFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				stageID,
				win,
				costTime).toString();
	}
	
	public static String toTlogMarchRewardStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int stageID, int points)
	{
		return new TLog.PlayerMarchRewardFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				stageID,
				points).toString();
	}
	
	public static String toTlogBattleGeneralsStateStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int power, int g1id, int g1lvl, int g1star, int g1adv, int g2id, int g2lvl, int g2star, int g2adv, int g3id, int g3lvl, int g3star, int g3adv, int g4id, int g4lvl, int g4star, int g4adv, int g5id, int g5lvl, int g5star, int g5adv, int pid, int plvl, int pstar, int battleType)
	{
		return new TLog.PlayerBattleGeneralsState(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				power,
				g1id,
				g1lvl,
				g1star,
				g1adv,
				g2id,
				g2lvl,
				g2star,
				g2adv,
				g3id,
				g3lvl,
				g3star,
				g3adv,
				g4id,
				g4lvl,
				g4star,
				g4adv,
				g5id,
				g5lvl,
				g5star,
				g5adv,
				pid,
				plvl,
				pstar,
				battleType).toString();
	}
	
	public static String toTlogGeneralEvolutionStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int viplvl, int pay, int gid, int glvl, int gstar, int gadv, int gEvoType, int glvlBefore, int gstarBefore, int gadvBefore)
	{
		return new TLog.GeneralEvolutionFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				viplvl,
				pay,
				gid,
				glvl,
				gstar,
				gadv,
				gEvoType,
				glvlBefore,
				gstarBefore,
				gadvBefore).toString();
	}
	
	public static String toTlogForceEventStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int fid, int fevent, int arg1, int arg2)
	{
		return new TLog.PlayerForceEventFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				fid,
				fevent,
				arg1,
				arg2).toString();
	}
	
	public static String toTlogForceStateStrImpl(int sequence, GameServer gs, int fid, String fname, int flvl, int fmcnt, int fvitality, int fvitalitymemcnt)
	{
		return new TLog.ForceState(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getConfig().gameappID,
				gs.getConfig().platID, 
				fid,
				fname.replaceAll("\\|", ""),
				flvl,
				fmcnt,
				fvitality,
				fvitalitymemcnt).toString();
	}
	
	public static String toTlogPayStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int rviplvlBefore, int rpayBefore, int paylvl, int payamount, int stone)
	{
		return new TLog.PlayerPayFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				rviplvlBefore,
				rpayBefore,
				paylvl,
				payamount,
				stone).toString();
	}
	
	public static String toTlogSnsStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int viplvl, int pay, int recNum, int count, int type, int subType)
	{
		return new TLog.SnsFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				gs.getConfig().areaID,
				openID, 
				rid,
				level,
				viplvl,
				pay,
				recNum,
				count,
				type,
				subType).toString();
	}
	
	public static String toTlogPetPoundStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int iEventType, int iEventArg)
	{
		return new TLog.PetPoundFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				iEventType,
				iEventArg).toString();
	}
	
	public static String toTlogCityWarStrImpl(int sequence, GameServer gs, String openID, int rid, int level, int rviplvl, int rpay, int iEventType, int arg1, int arg2, int arg3, int arg4)
	{
		return new TLog.CityWarFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				sequence,
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				gs.getConfig().platID, 
				openID, 
				rid,
				level,
				rviplvl,
				rpay,
				iEventType,
				arg1,
				arg2,
				arg3,
				arg4).toString();
	}
	
	public static String toTlogGameServerStateStrImpl(GameServer gs)
	{
		return new TLog.GameSvrState(
					gs.getTimeStampStr(), 
					gs.getConfig().addrIDIP.host).toString();
	}
	
	public static String toTlogSecRoundStartStrImpl(GameServer gs, int time, String openID, int clientTime, String roleName, int moeny, int stone, int vit, int combatID, int combatType, int diffculty, int chibiType, int baguaType, int lvlReq, int marchState, int costVit, int confFightScore, int realFightScore, int waveCount, int monsterCount, int bossCount, int immune)
	{
		return new TLog.SecRoundStartFlow(
				new Integer(gs.getConfig().id).toString(), 
				GameServer.getTimeStampStr(time), 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				openID,
				gs.getConfig().platID, 
				gs.getConfig().areaID-1,
				time,
				GameServer.getTimeStampStr(clientTime),
				roleName.replaceAll("\\|", ""),
				moeny,
				stone,
				vit,
				combatID,
				combatType,
				diffculty,
				chibiType,
				baguaType,
				0,
				lvlReq,
				marchState,
				costVit,
				confFightScore,
				realFightScore,
				waveCount,
				monsterCount,
				bossCount,
				immune).toString();
	}
	
	public static String toTlogSecRoundEndStrImpl(GameServer gs, int time, String openID, int clientEndTime, String cilentVersion, String cilentIp, int cheat, int endLost, int star, int cexp, int cmoney, int waveCount, int dpsTotal, String cgeneral, String citem, String cequip, int serverTimeCost, int clientTimeCost, int cilentSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
	{
		return new TLog.SecRoundEndFlow(
				new Integer(gs.getConfig().id).toString(), 
				GameServer.getTimeStampStr(time), 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				openID,
				gs.getConfig().platID, 
				gs.getConfig().areaID-1,
				time-serverTimeCost,
				GameServer.getTimeStampStr(clientEndTime),
				cilentVersion,
				cilentIp,
				cheat,
				endLost,
				star,
				cexp,
				cmoney,
				waveCount,
				dpsTotal,
				cgeneral,
				citem,
				cequip,
				serverTimeCost,
				clientTimeCost,
				cilentSpeed,
				clientMonsterCount,
				clientKillMonsterCount,
				pauseTimeTotal).toString();
	}
	
	public static String toTlogSecTalkStrImpl(GameServer gs, String openID, int roleLvl, String userIp, String receiveOpenID, int receiveRoleLvl, int chatType, String chatTitle, String chatContent)
	{
		return new TLog.SecTalkFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				openID,
				gs.getConfig().platID, 
				gs.getConfig().areaID-1,
				roleLvl,
				userIp,
				receiveOpenID,
				receiveRoleLvl,
				chatType,
				chatTitle.replaceAll("\\|", ""),
				chatContent.replaceAll("\\|", "")).toString();
	}
	
	public static String toTlogIDIPCMDStrImpl(GameServer gs, String openID, int itemtype, int itemid, int itemnum, String serialnum, int sourceid, String cmd)
	{
		return new TLog.IDIPCmdFlow(
				new Integer(gs.getConfig().id).toString(), 
				gs.getTimeStampStr(), 
				gs.getGameAppID(GameData.getInstance().isGuestOpenID(openID)),
				openID,
				gs.getConfig().platID, 
				gs.getConfig().areaID,
				itemtype,
				itemid,
				itemnum,
				serialnum,
				sourceid,
				cmd).toString();
	}
	
	public List<String> toTlogEventStr(Role role, TLogEvent event)
	{
		List<String> logs = new ArrayList<String>();
		if (!event.records.isEmpty()  || !event.commonFlowRecords.isEmpty())
		{
			event.setSeq(getNextSeq());
			for (TLogRecord e : event.records)
			{
				if (e != null)
					logs.add(e.toLogStr(this, role, event));
			}
			for (CommonFlowRecord e : event.commonFlowRecords)
			{
				if (e != null && e.hasChangeRecord())
				{
					//logs.addAll(e.toLogStrs(this, role, event));
					logs.add(e.toLogStr(this, role, event));
				}
			}
		}
		return logs;
	}
	
	public List<String> toTlogEventStr(DBRole role, TLogEvent event)
	{
		List<String> logs = new ArrayList<String>();
		if (!event.records.isEmpty() || !event.commonFlowRecords.isEmpty())
		{
			event.setSeq(getNextSeq());
			for (TLogRecord item : event.records)
			{	
				if (item != null)
					logs.add(item.toLogStr(this, role, event));
			}
			for (CommonFlowRecord e : event.commonFlowRecords)
			{
				if (e != null && e.hasChangeRecord())
				{
					//logs.addAll(e.toLogStrs(this, role, event));
					logs.add(e.toLogStr(this, role, event));
				}
			}
		}
		return logs;
	}
	
	
	public static class CommonItemChange
	{
		public int type;
		public int id;
		public int changeCount;
		public int afterCount;
		
		public CommonItemChange(int type, int id, int ccount, int acount)
		{
			this.type = type;
			this.id = id;
			this.changeCount = ccount;
			this.afterCount = acount;
		}
		
		public CommonItemChange()
		{
			
		}
	}
	
	public interface TLogRecord
	{
		public String toLogStr(TLogger logger, Role role, TLogEvent event);
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event);
	}
	
//	public abstract class TLogCommonChangeRecord
//	{
//		List<String> to
//	}
	
	static public class CommonFlowRecord// implements  TLogRecord
	{
		List<CommonItemChange> changeRecords = new ArrayList<CommonItemChange>();
		int event = 0;
		int arg1 = 0;
		int arg2 = 0;
		int arg3 = 0;
		int arg4 = 0;
		
		public CommonFlowRecord()
		{
		}
		
		public void addChange(CommonItemChange change)
		{
			if (change != null)
				changeRecords.add(change);
		}
		
		public void addChanges(List<CommonItemChange> changes)
		{
			if (changes != null)
				changeRecords.addAll(changes);
		}
		
		public void addChange(int type, int id, int ccount, int acount)
		{
			addChange(new CommonItemChange(type, id, ccount, acount));
		}
		
		public void setEvent(int event)
		{
			this.event = event;
		}
		
		public void setArg(int arg1)
		{
			this.arg1 = arg1;
		}
		
		public void setArg(int arg1, int arg2)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
		}
		
		public void setArg(int arg1, int arg2, int arg3)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
		}
		
		public void setArg(int arg1, int arg2, int arg3, int arg4)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
		}
		
		public boolean hasChangeRecord()
		{
			return !changeRecords.isEmpty();
		}
		
		public String getItemsChangeStr()
		{
			StringBuilder sb = new StringBuilder();
			for (CommonItemChange change : changeRecords)
			{
				if (sb.length() != 0)
					sb.append(";");
				if (change != null)
					sb.append(change.type).append(",").append(change.id).append(",").append(change.changeCount).append(",").append(change.afterCount);
			}
			return sb.toString();
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogItemFlow2Str(event.seq, role, this.event, getItemsChangeStr(), this.arg1, this.arg2, this.arg3, this.arg4);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogItemFlow2Str(event.seq, role, this.event, getItemsChangeStr(), this.arg1, this.arg2, this.arg3, this.arg4);
		}
		
//		public List<String> toLogStrs(TLogger logger, Role role, TLogEvent event)
//		{
//			List<String> lst = new ArrayList<String>();
//			for (CommonItemChange change : changeRecords)
//			{
//				lst.add(logger.toTlogItemFlowStr(event.seq, role, this.event, change.type, change.id, change.changeCount, change.afterCount, arg1, arg2, arg3, arg4));
//			}
//			return lst;
//		}
//		
//		public List<String> toLogStrs(TLogger logger, DBRole role, TLogEvent event)
//		{
//			List<String> lst = new ArrayList<String>();
//			for (CommonItemChange change : changeRecords)
//			{
//				lst.add(logger.toTlogItemFlowStr(event.seq, role, this.event, change.type, change.id, change.changeCount, change.afterCount, arg1, arg2, arg3, arg4));
//			}
//			return lst;
//		}
	}
	
//	static public class SecondaryCommonFlowRecord implements TLogRecord
//	{
//		CommonItemChange changeRecord;
//		int eventID = 0;
//		int arg1 = 0;
//		int arg2 = 0;
//		int arg3 = 0;
//		int arg4 = 0;
//		
//		public SecondaryCommonFlowRecord(CommonItemChange change)
//		{
//			changeRecord = new CommonItemChange(change.type, change.id, change.changeCount, change.afterCount);
//		}
//		
//		public void setSecondaryEventID(int id)
//		{
//			eventID = id;
//		}
//		
//		public void setArg(int arg1)
//		{
//			this.arg1 = arg1;
//		}
//		
//		public void setArg(int arg1, int arg2)
//		{
//			this.arg1 = arg1;
//			this.arg2 = arg2;
//		}
//		
//		public void setArg(int arg1, int arg2, int arg3)
//		{
//			this.arg1 = arg1;
//			this.arg2 = arg2;
//			this.arg3 = arg3;
//		}
//		
//		public void setArg(int arg1, int arg2, int arg3, int arg4)
//		{
//			this.arg1 = arg1;
//			this.arg2 = arg2;
//			this.arg3 = arg3;
//			this.arg4 = arg4;
//		}
//		
//		public String toLogStr(TLogger logger, Role role, TLogEvent event)
//		{
//			return logger.toTlogItemFlowStr(event.seq, role, eventID, changeRecord.type, changeRecord.id, changeRecord.changeCount, changeRecord.afterCount, arg1, arg2, arg3, arg4);
//		}
//		
//		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
//		{
//			return logger.toTlogItemFlowStr(event.seq, role, eventID, changeRecord.type, changeRecord.id, changeRecord.changeCount, changeRecord.afterCount, arg1, arg2, arg3, arg4);
//		}
//	}
	
	static public class LevelUpRecord implements TLogRecord
	{
		int level;
		int lastLevelUpTime;
		int curLevelUpTime;
		
		public LevelUpRecord(int level)
		{
			this.level = level;
		}
		
		public void setLastLevelUpTime(int time)
		{
			lastLevelUpTime = time;
		}
		
		public void setCurLevelUpTime(int time)
		{
			curLevelUpTime = time;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogLevelUpStr(event.seq, role, level, curLevelUpTime-lastLevelUpTime);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogLevelUpStr(event.seq, role, level, curLevelUpTime-lastLevelUpTime);
		}
	}
	
	static public class BattleRecord implements TLogRecord
	{
		int battleType;
		int battleID;
		int checkPointID;
		int score;
		int costTime;
		int combatType;
		int level;
		
		public BattleRecord(int level)
		{
			this.level = level;
		}
		
		public void setBattle(int type, int id, int checkPoint)
		{
			this.battleType = type;
			this.battleID = id;
			this.checkPointID = checkPoint;
		}
		
		public void setScore(int score)
		{
			this.score = score;
		}
		
		public void setCostTime(int costTime)
		{
			this.costTime = costTime;
		}
		
		public void setCombatType(int type)
		{
			this.combatType = type;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogBattleStr(event.seq, role, level, battleType, battleID, checkPointID, score, costTime, combatType);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogBattleStr(event.seq, role, level, battleType, battleID, checkPointID, score, costTime, combatType);
		}
	}
	
	static public abstract class EventBattleRecord implements TLogRecord
	{
		int battleID;
		int dLevel;
		int score;
		int costTime;
		int level;
		
		public EventBattleRecord(int level)
		{
			this.level = level;
		}
		
		public void setBattle(int id, int dlvl)
		{
			this.battleID = id;
			this.dLevel = dlvl;
		}
		
		public void setScore(int score)
		{
			this.score = score;
		}
		
		public void setCostTime(int costTime)
		{
			this.costTime = costTime;
		}
	}
	
	static public class ChibiBattleRecord extends EventBattleRecord
	{
		
		public ChibiBattleRecord(int level)
		{
			super(level);
		}	
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogChibiBattleStr(event.seq, role, level, battleID, dLevel, score, costTime);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogChibiBattleStr(event.seq, role, level, battleID, dLevel, score, costTime);
		}
	}
	
	static public class BaguazhengBattleRecord extends EventBattleRecord
	{	
		public BaguazhengBattleRecord(int level)
		{
			super(level);
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogBaguazhengBattleStr(event.seq, role, level, battleID, dLevel, score, costTime);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogBaguazhengBattleStr(event.seq, role, level, battleID, dLevel, score, costTime);
		}
	}
	
	static public class ArenaRecord implements TLogRecord
	{
		int rank;
		int orank;
		int result;
		int level;
		
		public ArenaRecord(int level)
		{
			this.level = level;
		}
		
		public void setArenaTwoSidesRank(int rank, int orank)
		{
			this.rank = rank;
			this.orank = orank;
		}
		
		public void setResult(int result)
		{
			this.result = result;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogArenaStr(event.seq, role, level, rank, orank, result);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogArenaStr(event.seq, role, level, rank, orank, result);
		}
	}
	
	
	static public class DailyTaskRecord implements TLogRecord
	{
		int id;
		int level;
		
		public DailyTaskRecord(int level)
		{
			this.level = level;
		}
		
		public void setId(int id)
		{
			this.id = id;
		}
		
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogDailyTaskStr(event.seq, role, level, id);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogDailyTaskStr(event.seq, role, level, id);
		}
	}
	
	
	static public class DailyTaskRewardRecord implements TLogRecord
	{
		int id;
		int level;
		
		public DailyTaskRewardRecord(int level)
		{
			this.level = level;
		}
		
		public void setId(int id)
		{
			this.id = id;
		}
		
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogDailyTaskRewardStr(event.seq, role, level, id);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogDailyTaskRewardStr(event.seq, role, level, id);
		}
	}
	
	static public class ChestRecord implements TLogRecord
	{
		int level;
		int chestType;
		int buyCount;
		int buyPrice;
		int buyPriceUnit;
		
		public ChestRecord(int level)
		{
			this.level = level;
		}
		
		public void setBuyInfo(int chestType, int buyCount, int buyPrice, int buyPriceUnit)
		{
			this.chestType = chestType;
			this.buyCount = buyCount;
			this.buyPrice = buyPrice;
			this.buyPriceUnit = buyPriceUnit;
		}
		
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogChestStr(event.seq, role, level, chestType, buyCount, buyPrice, buyPriceUnit);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogChestStr(event.seq, role, level, chestType, buyCount, buyPrice, buyPriceUnit);
		}
	}
	
	static public class ShopRecord implements TLogRecord
	{
		int level;
		int shopType;
		int goodsType;
		int goodsID;
		int buyCount;
		int buyPrice;
		int buyPriceUnit;
		
		public ShopRecord(int level)
		{
			this.level = level;
		}
		
		public void setShopType(int type)
		{
			this.shopType = type;
		}
		
		public void setBuyInfo(int goodsType, int goodsID, int buyCount, int buyPrice, int buyPriceUnit)
		{
			this.goodsType = goodsType;
			this.goodsID = goodsID;
			this.buyCount = buyCount;
			this.buyPrice = buyPrice;
			this.buyPriceUnit = buyPriceUnit;
		}
		
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogShopStr(event.seq, role, level, shopType, goodsType, goodsID, buyCount, buyPrice, buyPriceUnit);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogShopStr(event.seq, role, level, shopType, goodsType, goodsID, buyCount, buyPrice, buyPriceUnit);
		}
	}
	
	static public class GuideRecord implements TLogRecord
	{
		int level;
		int guideID;
		
		public GuideRecord(int level, int guideID)
		{
			this.level = level;
			this.guideID = guideID;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogGuideStr(event.seq, role, level, guideID);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogGuideStr(event.seq, role, level, guideID);
		}
	}
	
	static public class TaskRecord implements TLogRecord
	{
		int level;
		int taskID;
		int taskSeq;
		
		public TaskRecord(int level, int taskID, int taskSeq)
		{
			this.level = level;
			this.taskID = taskID;
			this.taskSeq = taskSeq;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogTaskStr(event.seq, role, level, taskID, taskSeq);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogTaskStr(event.seq, role, level, taskID, taskSeq);
		}
	}
	
	static public class CheckinRecord implements TLogRecord
	{
		int level;
		int checkinMonthID;
		int checkinDaySeq;
		
		public CheckinRecord(int level, int checkinMonthID, int checkinDaySeq)
		{
			this.level = level;
			this.checkinMonthID = checkinMonthID;
			this.checkinDaySeq = checkinDaySeq;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogCheckinStr(event.seq, role, level, checkinMonthID, checkinDaySeq);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogCheckinStr(event.seq, role, level, checkinMonthID, checkinDaySeq);
		}
	}
	
	static public class EquipGildRecord implements TLogRecord
	{
		int generalID;
		int equipID;
		int posID;
		int equipLvl;
		int level;
		
		public EquipGildRecord(int level)
		{
			this.level = level;
		}
		
		public void setEquipGildInfo(int gid, int eid, int pos, int equipLvl)
		{
			this.generalID = gid;
			this.equipID = eid;
			this.posID = pos;
			this.equipLvl = equipLvl;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogEquipGildStr(event.seq, role, level, generalID, equipID, posID, equipLvl);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogEquipGildStr(event.seq, role, level, generalID, equipID, posID, equipLvl);
		}
	}
	
	static public class MarchRecord implements TLogRecord
	{
		int marchID;
		int win;
		int level;
		int costTime;
		
		public MarchRecord(int level)
		{
			this.level = level;
		}
		
		public void setMarchInfo(int marchID, int win)
		{
			this.marchID = marchID;
			this.win = win;
		}
		
		public void setCostTime(int costTime)
		{
			this.costTime = costTime;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogMarchStr(event.seq, role, level, marchID, win, costTime);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogMarchStr(event.seq, role, level, marchID, win, costTime);
		}
	}
	
	static public class MarchRewardRecord implements TLogRecord
	{
		int marchID;
		int points;
		int level;
		
		public MarchRewardRecord(int level)
		{
			this.level = level;
		}
		
		public void setMarchReward(int marchID, int points)
		{
			this.marchID = marchID;
			this.points = points;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogMarchRewardStr(event.seq, role, level, marchID, points);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogMarchRewardStr(event.seq, role, level, marchID, points);
		}
	}
	
	static public class GeneralEvolutionRecord implements TLogRecord
	{
		int generalID;
		int gLvl;
		int gStar;
		int gAdv;
		int gLvlBefore;
		int gStarBeforer;
		int gAdvBefore;
		int EvoType;
		
		public GeneralEvolutionRecord(int gid, int gLvl, int gStar, int gAdv)
		{
			this.generalID = gid;
			this.gLvlBefore = gLvl;
			this.gStarBeforer = gStar;
			this.gAdvBefore = gAdv;
		}
		
		public void setGeneralEvoInfo(int type, int gLvl, int gStar, int gAdv)
		{
			this.EvoType = type;
			this.gLvl = gLvl;
			this.gStar = gStar;
			this.gAdv = gAdv;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogGeneralEvolutionStr(event.seq, role, generalID, gLvl, gStar, gAdv, EvoType, gLvlBefore, gStarBeforer, gAdvBefore);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogGeneralEvolutionStr(event.seq, role, generalID, gLvl, gStar, gAdv, EvoType, gLvlBefore, gStarBeforer, gAdvBefore);
		}
	}
	
	static public class ForceEventRecord implements TLogRecord
	{
		int forceID;
		int forceEvent;
		int arg1;
		int arg2;
		
		public ForceEventRecord(int fid, int forceEvent)
		{
			this.forceID = fid;
			this.forceEvent = forceEvent;
		}
		
		public void setArgs(int arg1, int arg2)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogForceEventStr(event.seq, role, forceID, forceEvent, arg1, arg2);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogForceEventStr(event.seq, role, forceID, forceEvent, arg1, arg2);
		}
	}
	
	static public class PayRecord implements TLogRecord
	{
		int level;
		int viplvl;
		int payTotal;
		int viplvlBefore;
		int payTotalBefore;
		int paylvl;
		int payamount;
		int stone;
		
		public PayRecord(int level, int viplvl, int payTotal)
		{
			this.level = level;
			this.viplvlBefore = viplvl;
			this.payTotalBefore = payTotal;
		}
		
		public void setPayInfo(int paylvl, int payamount, int stone)
		{
			this.paylvl = paylvl;
			this.payamount = payamount;
			this.stone = stone;
		}
		
		public void setAfterPayState(int viplvl, int payTotal)
		{
			this.viplvl = viplvl;
			this.payTotal = payTotal;
		}
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogPayStr(event.seq, role, level, viplvl, payTotal, viplvlBefore, payTotalBefore, paylvl, payamount, stone);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogPayStr(event.seq, role, level, viplvl, payTotal, viplvlBefore, payTotalBefore, paylvl, payamount, stone);
		}
	}
	
//	static public class SnsRecord implements TLogRecord
//	{
//		int type;
//		int subType;
//		int recNum;
//		int count;
//		
//		public SnsRecord(int type, int subType, int recNum, int count)
//		{
//			this.type = type;
//			this.subType = subType;
//			this.recNum = recNum;
//			this.count = count;
//		}
//		
//		public String toLogStr(TLogger logger, Role role, TLogEvent event)
//		{
//			return logger.toTlogSnsStr(event.seq, role, recNum, count, type, subType);
//		}
//		
//		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
//		{
//			return logger.toTlogSnsStr(event.seq, role, recNum, count, type, subType);
//		}
//	}
	
	
	static public class PetPoundRecord implements TLogRecord
	{
		int type;
		int arg;
		
		public PetPoundRecord(int type)
		{
			this.type = type;
		}
		
		public PetPoundRecord(int type, int arg)
		{
			this.type = type;
			this.arg = arg;
		}
		
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogPetPoundStr(event.seq, role, type, arg);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogPetPoundStr(event.seq, role, type, arg);
		}
	}
	
	static public class CityWarRecord implements TLogRecord
	{
		int type;
		int arg1;
		int arg2;
		int arg3;
		int arg4;
		
		public CityWarRecord(int type)
		{
			this.type = type;
		}
		
		public CityWarRecord(int type, int arg1)
		{
			this.type = type;
			this.arg1 = arg1;
		}
		
		public void setArgs(int arg1, int arg2, int arg3, int arg4)
		{
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
		}
		
				
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogCityWarStr(event.seq, role, type, arg1, arg2, arg3, arg4);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogCityWarStr(event.seq, role, type, arg1, arg2, arg3, arg4);
		}
	}
	
	static public class SecRoundStartRecord implements TLogRecord
	{
		int serverStartTime;
		int clientStartTime;
		int beforeVit;
		int combatID;
		int combatType;
		int diffculty;
		int chibiType;
		int baguaType;
		int lvlReq;
		int marchState;
		int costVit;
		int confFightScore;
		int realFightScore;
		int waveCount;
		int monsterCount;
		int bossCount;
		int immune;
		public SecRoundStartRecord(int vit)
		{
			this.beforeVit = vit;
		}
		
		public void setStartTime(int svrStartTime, int clientStartTime)
		{
			this.serverStartTime = svrStartTime;
			this.clientStartTime = clientStartTime;
		}
		
		public void setBattle(int id, int diffculty)
		{
			this.combatID = id;
			this.combatType = TLog.SEC_ROUND_BATTLE;
			this.diffculty = diffculty;
			this.chibiType = 0;
			this.baguaType = 0;
			this.marchState = 0;
		}
		
		public void setChibi(int id, int chibiType)
		{
			this.combatID = id;
			this.combatType = TLog.SEC_ROUND_CHIBI;
			this.diffculty = 0;
			this.chibiType = chibiType;
			this.baguaType = 0;
			this.marchState = 0;
		}
		
		public void setBagua(int id, int baguaType)
		{
			this.combatID = id;
			this.combatType = TLog.SEC_ROUND_BAGUA;
			this.diffculty = 0;
			this.chibiType = 0;
			this.baguaType = baguaType;
			this.marchState = 0;
		}
		
		public void setMarch(int id, int marchState)
		{
			this.combatID = id;
			this.combatType = TLog.SEC_ROUND_MARCH;
			this.diffculty = 0;
			this.chibiType = 0;
			this.baguaType = 0;
			this.marchState = marchState;
		}
		
		public void setConfig(int lvlReq, int costVit, int confFightScore, int realFightScore, int waveCount, int monsterCount, int bossCount, int immune)
		{
			this.lvlReq = lvlReq;
			this.costVit = costVit;
			this.confFightScore = confFightScore;
			this.realFightScore = realFightScore;
			this.waveCount = waveCount;
			this.monsterCount = monsterCount;
			this.bossCount = bossCount;
			this.immune = immune;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogSecRoundStartStr(role, serverStartTime, clientStartTime, beforeVit, combatID, combatType, diffculty, chibiType, baguaType, lvlReq, marchState, costVit, confFightScore, realFightScore, waveCount, monsterCount, bossCount, immune);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogSecRoundStartStr(role, serverStartTime, clientStartTime, beforeVit, combatID, combatType, diffculty, chibiType, baguaType, lvlReq, marchState, costVit, confFightScore, realFightScore, waveCount, monsterCount, bossCount, immune);
		}
	}
	
	static public class SecRoundEndRecord implements TLogRecord
	{
		int svrEndTime;
		int clientEndTime;
		int cheat;
		int endLost;
		int star;
		int cexp;
		int cmoney;
		int waveCount;
		int dpsTotal;
		String cgeneral = "";
		String citem = "";
		String cequip = "";
		int serverTimeCost;
		int clientTimeCost;
		int clientSpeed;
		int clientMonsterCount;
		int clientKillMonsterCount;
		int pauseTimeTotal;
		
		public SecRoundEndRecord()
		{
			
		}
		
		public void setSvrEndCostTime(int endTime, int costTime)
		{
			this.svrEndTime = endTime;
			this.serverTimeCost = costTime;
		}
		
		public void setClientEndCostTime(int endTime, int costTime)
		{
			this.clientEndTime = endTime;
			this.clientTimeCost = costTime;
		}

		
		public void setResult(int cheat, int star)
		{
			this.cheat = cheat;
			this.endLost = star > 0 ? 0 : 1;
			this.star = star;
		}
		
		public void setClientStats(int waveCount, int dpsTotal, int clientSpeed, int clientMonsterCount, int clientKillMonsterCount, int pauseTimeTotal)
		{
			this.waveCount = waveCount;
			this.dpsTotal = dpsTotal;
			this.clientSpeed = clientSpeed;
			this.clientMonsterCount = clientMonsterCount;
			this.clientKillMonsterCount = clientKillMonsterCount;
			this.pauseTimeTotal = pauseTimeTotal;
		}
		
		public void addItem(int id)
		{
			if (!citem.isEmpty())
				citem += ",";
			citem += id;
		}
		
		public void addEquip(int id)
		{
			if (!cequip.isEmpty())
				cequip += ",";
			cequip += id;
		}
		
		public void addGeneral(int id)
		{
			if (!cgeneral.isEmpty())
				cgeneral += ",";
			cgeneral += id;
		}
		
		public void addMoney(int money)
		{
			this.cmoney += money;
		}
		
		public void addExp(int exp)
		{
			this.cexp += exp;
		}
		
		public void addDrop(SBean.DropEntryNew e)
		{
			switch( e.type )
			{
			case GameData.COMMON_TYPE_ITEM:
				{
					for (int i = 0; i < e.arg; ++i)
						addItem(e.id);
				}
				break;
			case GameData.COMMON_TYPE_EQUIP:
				{
					for (int i = 0; i < e.arg; ++i)
						addEquip(e.id);
				}
				break;
			case GameData.COMMON_TYPE_GENERAL:
				{
					for (int i = 0; i < e.arg; ++i)
						addGeneral(e.id);
				}
				break;
			case GameData.COMMON_TYPE_MONEY:
				{
					addMoney(e.arg);
				}
				break;
			case GameData.COMMON_TYPE_STONE:
				{
					
				}
				break;
			case GameData.COMMON_TYPE_ARENA_POINT:
				{
					
				}
				break;
			case GameData.COMMON_TYPE_MARCH_POINT:
				{
					
				}
				break;
			case GameData.COMMON_TYPE_FORCE_POINT:
				{
					
				}
				break;
			default:
				break;
			}
		}
		
		public void addDrops(List<SBean.DropEntryNew> drops)
		{
			for (SBean.DropEntryNew e : drops)
			{
				addDrop(e);
			}
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogSecRoundEndStr(role, svrEndTime, clientEndTime, cheat, endLost, star, cexp, cmoney, waveCount, dpsTotal, cgeneral, citem, cequip, serverTimeCost, clientTimeCost, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogSecRoundEndStr(role, svrEndTime, clientEndTime, cheat, endLost, star, cexp, cmoney, waveCount, dpsTotal, cgeneral, citem, cequip, serverTimeCost, clientTimeCost, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
		}
	}
	
	static public class SecTalkRecord implements TLogRecord
	{
		String receiveRoleOpenID = "";
		int receiveRoleLvl = 0;
		int chatType;
		String chatTitle = "";
		String chatContent = "";
		public SecTalkRecord(int chatType, String charContent)
		{
			this.chatType = chatType;
			this.chatContent = charContent;
		}
		
		public SecTalkRecord(String receiveRoleOpenID, int receiveRoleLvl, String chatTitle, String charContent)
		{
			this.receiveRoleOpenID = receiveRoleOpenID;
			this.receiveRoleLvl = receiveRoleLvl;
			this.chatType = TLog.SEC_TALK_MAIL;
			this.chatTitle = chatTitle;
			this.chatContent = charContent;
		}
		
		public String toLogStr(TLogger logger, Role role, TLogEvent event)
		{
			return logger.toTlogSecTalkStr(role, receiveRoleOpenID, receiveRoleLvl, chatType, chatTitle, chatContent);
		}
		
		public String toLogStr(TLogger logger, DBRole role, TLogEvent event)
		{
			return logger.toTlogSecTalkStr(role, receiveRoleOpenID, receiveRoleLvl, chatType, chatTitle, chatContent);
		}
	}
	
//	public static interface ToTLogStrCallback
//	{
//		void handleTLogStr(String str);
//	}
	
	public static class TLogEvent
	{
		int seq = 0;
//		int event = 0;
//		int arg1 = 0;
//		int arg2 = 0;
//		int arg3 = 0;
//		int arg4 = 0;
		List<TLogRecord> records = new ArrayList<TLogRecord>();
		List<CommonFlowRecord> commonFlowRecords = new ArrayList<CommonFlowRecord>();
		//CommonFlowRecord commonChangeRecord = new  CommonFlowRecord();
		
		public TLogEvent()
		{
			
		}
		
//		public TLogEvent(int event)
//		{
//			this.commonChangeRecord.event = event;
//		}
		
		void setSeq(int seq)
		{
			this.seq = seq;
		}
		
		public void addRecord(TLogRecord record)
		{
			if (record != null)
				records.add(record);
		}
		
		public void addCommonFlow(CommonFlowRecord record)
		{
			if (record != null)
				commonFlowRecords.add(record);
		}
		
//		public void addRecords(List<TLogRecord> records)
//		{
//			records.addAll(records);
//		}
		
//		public void addCommonChange(CommonItemChange change)
//		{
//			if (change != null)
//				commonChangeRecord.addChange(change);
//		}
//		
//		public void addCommonChanges(List<CommonItemChange> changes)
//		{
//			commonChangeRecord.addChanges(changes);
//		}
//		
//		public void setEvent(int event)
//		{
//			this.commonChangeRecord.event = event;
//		}
//		
//		public void setArg(int arg1)
//		{
//			this.commonChangeRecord.arg1 = arg1;
//		}
//		
//		public void setArg(int arg1, int arg2)
//		{
//			this.commonChangeRecord.arg1 = arg1;
//			this.commonChangeRecord.arg2 = arg2;
//		}
//		
//		public void setArg(int arg1, int arg2, int arg3)
//		{
//			this.commonChangeRecord.arg1 = arg1;
//			this.commonChangeRecord.arg2 = arg2;
//			this.commonChangeRecord.arg3 = arg3;
//		}
//		
//		public void setArg(int arg1, int arg2, int arg3, int arg4)
//		{
//			this.commonChangeRecord.arg1 = arg1;
//			this.commonChangeRecord.arg2 = arg2;
//			this.commonChangeRecord.arg3 = arg3;
//			this.commonChangeRecord.arg4 = arg4;
//		}
	}
	
}

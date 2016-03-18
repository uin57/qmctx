package i3k.gs;

import i3k.DBForce;
import i3k.DBMail;
import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.DBForceBeast;
import i3k.SBean.DBForceWarGeneralStatus;
import i3k.SBean.DBForceWarRecord;
import i3k.SBean.DBPetBrief;
import i3k.SBean.RoleDataRes;
import i3k.TLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ket.kdb.Table;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.util.Stream;

public class ForceWarManager {

	static final int OPPO_MAX = 5;

	static final int THIEF_STAND_POS = 2;

	public static class CalcReq
	{
		public int seed;
		public int fid1;
		public int fid2;
		public byte rIndex1;
		public byte rIndex2;
		public byte [] eff1;
		public byte [] eff2;
		public List<DBRoleGeneral> generals1;
		public List<DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<DBForceWarGeneralStatus> status1;
		public List<DBForceWarGeneralStatus> status2;
		public List<SBean.DBRelation> relation1;
		public List<SBean.DBRelation> relation2;
//		public List<SBean.DBGeneralStone> gStones1;
//		public List<SBean.DBGeneralStone> gStones2;
	}

	public static class RuntimeForceWarStatus
	{
		public byte roleIndex = 0;
		public byte oppoRoleIndex = 0;
		public boolean matchBegin = false;
		public int matchTimeout = 0;
		public List<Byte> calcResults = new ArrayList<Byte>();
		public SBean.DBForceWarRecord record;
		public int score = 0;
		public Map<Short, SBean.DBForceWarGeneralStatus> generalStatus = null;
		public Map<Short, SBean.DBForceWarGeneralStatus> oppoGeneralStatus = null;
		public Map<Short, SBean.DBForceWarGeneralStatus> nextGeneralStatus = null;
		public Map<Short, SBean.DBForceWarGeneralStatus> nextOppoGeneralStatus = null;
	}

	public static class WarMatchData
	{
		public String roleName1;
		public String roleName2;
		public byte roleIndex1;
		public byte roleIndex2;
		public boolean win;
	}

	public static class WarData
	{
		public int fid1;
		public int fid2;
		public String name1;
		public String name2;
		public String headName1;
		public String headName2;
		public short icon1;
		public short icon2;
		public boolean win;
		public int quizfid;
		public List<WarMatchData> matchData = new ArrayList<WarMatchData>();
	}

	public static class SortScore
	{
		SortScore(int score, int fid, int power)
		{
			this.fid = fid;
			this.score = score;
			this.power = power;
		}
		int fid;
		int score;
		int power;
	}

	public static class ScoreRank
	{
		ScoreRank(int rank, int fid, int score, short icon, String name)
		{
			this.rank = rank;
			this.fid = fid;
			this.score = score;
			this.icon = icon;
			this.name = name;
		}
		public int rank;
		public int fid;
		public int score;
		public short icon;
		public String name;
	}

	public static class PowerRank
	{
		PowerRank(int rid, short lvl, short icon, String name, int power)
		{
			this.rid = rid;
			this.lvl = lvl;
			this.icon = icon;
			this.name = name;
			this.power = power;
		}
		public int rid;
		public int lvl;
		public short icon;
		public String name;
		public int power;
	}

	public static class RoleScoreRank
	{
		RoleScoreRank(int rid, short lvl, short icon, String name, int score)
		{
			this.rid = rid;
			this.lvl = lvl;
			this.icon = icon;
			this.name = name;
			this.score = score;
		}
		public int rid;
		public int lvl;
		public short icon;
		public String name;
		public int score;
	}

	public static class DonateRank
	{
		DonateRank(int rid, short lvl, short icon, String name, int donate)
		{
			this.rid = rid;
			this.lvl = lvl;
			this.icon = icon;
			this.name = name;
			this.donate = donate;
		}
		public int rid;
		public int lvl;
		public short icon;
		public String name;
		public int donate;
	}

	public static class CupWinRank
	{
		CupWinRank(int rid, short lvl, short icon, String name, int cupWin)
		{
			this.rid = rid;
			this.lvl = lvl;
			this.icon = icon;
			this.name = name;
			this.cupWin = cupWin;
		}
		public int rid;
		public int lvl;
		public short icon;
		public String name;
		public int cupWin;
	}

	public static class ForceWarStatus
	{
		ForceWarStatus(SBean.DBForceWarStatus status, int listIndex)
		{
			this.status = status;
			this.listIndex = listIndex;
		}
		SBean.DBForceWarStatus status = null;
		int listIndex = 0;
	}

	public static class ForceWarRecord
	{
		ForceWarRecord(SBean.DBForceWarRecord record, int listIndex)
		{
			this.record = record;
			this.listIndex = listIndex;
		}
		SBean.DBForceWarRecord record = null;
		int listIndex = 0;
	}

	public static class ForceWarRoleScore
	{
		ForceWarRoleScore(SBean.DBForceWarRoleScore score, int listIndex)
		{
			this.score = score;
			this.listIndex = listIndex;
		}
		SBean.DBForceWarRoleScore score = null;
		int listIndex = 0;
	}

	public static class ForceWarCupWin
	{
		ForceWarCupWin(SBean.DBForceWarCupWin cupWin, int listIndex)
		{
			this.cupWin = cupWin;
			this.listIndex = listIndex;
		}
		SBean.DBForceWarCupWin cupWin = null;
		int listIndex = 0;
	}

	public static class QuizReward
	{
		QuizReward(int rid, int money, int point, boolean right)
		{
			this.rid = rid;
			this.money = money;
			this.point = point;
			this.right = right;
		}
		int rid;
		int money;
		int point;
		boolean right;
	}

	public static class DamageRank
	{
		DamageRank(int rank, int rid, int damage, short icon, String name, short lvl, List<SBean.DBForceThiefGeneralBrief> generals)
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
		public List<SBean.DBForceThiefGeneralBrief> generals;
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

	public static class ForceThiefCombat
	{
		public byte groupIndex;
		public byte generalType;
		public short combatId;
		public boolean fighting;
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

	public static class ForceWar
	{
		ForceWar(GameServer gs, SBean.DBForceWar war, SBean.DBForceThief thief, SBean.DBForceWarBeast beast) {
			this.gs = gs;
			if (war == null) {
				this.war = new SBean.DBForceWar();
				this.war.players = new ArrayList<SBean.DBForceWarForcePlayer>();
				this.war.stage = 0;
				this.war.status = new ArrayList<SBean.DBForceWarStatus>();
				this.war.roleScores = new ArrayList<SBean.DBForceWarRoleScore>();
				this.war.cupWins = new ArrayList<SBean.DBForceWarCupWin>();
				this.war.cupResults = new ArrayList<Integer>();
				this.war.leagueSequence = new ArrayList<Integer>();
				this.war.leagueRecords = new ArrayList<SBean.DBForceWarRecord>();
				this.war.cupRecords = new ArrayList<SBean.DBForceWarRecord>();
				this.war.cupQuizs = new ArrayList<SBean.DBForceWarQuiz>();
			}
			else
				this.war = war;

			/*
			if (this.war.bid == 24 && this.war.stage > 2) {
				this.war.stage = 1;
				this.war.status = new ArrayList<SBean.DBForceWarStatus>();
				this.war.roleScores = new ArrayList<SBean.DBForceWarRoleScore>();
				this.war.cupWins = new ArrayList<SBean.DBForceWarCupWin>();
				this.war.cupResults = new ArrayList<Integer>();
				this.war.leagueSequence = new ArrayList<Integer>();
				this.war.leagueRecords = new ArrayList<SBean.DBForceWarRecord>();
				this.war.cupRecords = new ArrayList<SBean.DBForceWarRecord>();
				this.war.cupQuizs = new ArrayList<SBean.DBForceWarQuiz>();
			}
			*/

			if (thief == null) {
				this.thief = new SBean.DBForceThief();
				//this.thief.thiefGroups = new ArrayList<SBean.DBForceThiefGroup>();
				this.thief.thiefStatus = new ArrayList<SBean.DBForceThiefStatus>();
				this.thief.bossDamage = new ArrayList<SBean.DBForceThiefBossDamage>();
				this.thief.thiefStartTime = 0;
				/*
				for (int i = 0; i < 5; i ++) {
					SBean.DBForceThiefGroup group = new SBean.DBForceThiefGroup();
					group.thiefIndex = (byte)i;
					group.bossType = (byte)gs.getGameData().getRandInt(0, 2);
					group.combats = new ArrayList<SBean.DBForceThiefCombat>();
					for (int j = 0; j < 10; j ++) {
						SBean.DBForceThiefCombat combat = new SBean.DBForceThiefCombat();
						combat.groupIndex = (byte)j;
						combat.generalType = (byte)gs.getGameData().getRandInt(0, 5);
						combat.combatId = (short)(j + 1);
						group.combats.add(combat);
					}
					this.thief.thiefGroups.add(group);
				}
				*/
			}
			else
				this.thief = thief;

			if (beast == null) {
				this.beast = new SBean.DBForceWarBeast();
				this.beast.beasts = new ArrayList<SBean.DBForceBeast>();
			}
			else
				this.beast = beast;

			warStatusMap = new HashMap<Integer, ForceWarStatus>();
			warRTStatusMap = new HashMap<Integer, RuntimeForceWarStatus>();
			warRoleScoreMap = new HashMap<Integer, ForceWarRoleScore>();
			warCupWinMap = new HashMap<Integer, ForceWarCupWin>();
			warLeagueRecordMap = new HashMap<Integer, Map<Integer, ForceWarRecord>>();
			warCupRecordMap = new HashMap<Integer, Map<Integer, ForceWarRecord>>();
			sortLists = new ArrayList<List<SortScore>>();
			powerRanks = new ArrayList<PowerRank>();
			roleScoreRanks = new ArrayList<RoleScoreRank>();
			donateRanks = new ArrayList<DonateRank>();
			cupWinRanks = new ArrayList<CupWinRank>();
			forcePowerRanks = new HashMap<Integer, List<PowerRank>>();
			forceRoleScoreRanks = new HashMap<Integer, List<RoleScoreRank>>();
			forceDonateRanks = new HashMap<Integer, List<DonateRank>>();
			forceCupWinRanks = new HashMap<Integer, List<CupWinRank>>();
			powers = new HashMap<Integer, Integer>();

			sortDamageList = new ArrayList<SortDamage>();
			forceSortDamageList = new HashMap<Integer, List<SortDamage>>();
			thiefStatusMap = new HashMap<Integer, SBean.DBForceThiefStatus>();
			thiefRTStatusMap = new HashMap<Integer, ForceThiefRTStatus>();
			for (SBean.DBForceThiefBossDamage d : this.thief.bossDamage) {
				rid2DamageMap.put(d.rid, d);
				setDamage(100, d.damage, d.fid, d.rid, d.name, d.icon, d.lvl, d.generals);
			}

			int i = 0;
			for (SBean.DBForceWarStatus s : this.war.status) {
				initWarStatus(s, i ++);
				int j = 0;
				for (int score : s.scores) {
					setScore(0, score, s.fid, j);
					j ++;
				}
			}
			i = 0;
			for (SBean.DBForceWarRecord r : this.war.leagueRecords)
				initWarRecord(true, r, i ++);
			i = 0;
			for (SBean.DBForceWarRecord r : this.war.cupRecords)
				initWarRecord(false, r, i ++);
			i = 0;
			for (SBean.DBForceWarRoleScore s : this.war.roleScores)
				initRoleScore(s, i ++);
			i = 0;
			for (SBean.DBForceWarCupWin c : this.war.cupWins)
				initCupWin(c, i ++);
			i = 0;
			for (SBean.DBForceThiefStatus s : this.thief.thiefStatus)
				initThiefStatus(s);

			if (this.war.stage >= 3)
				collectRankList();
			else if (this.war.stage >= 2)
				collectCupForces();
		}

		private int getLeagueSequence(int index) {
			int size = war.leagueSequence.size();
			if (size == 0)
				return -1;
			while (index < 0)
				index += size;
			while (index >= size)
				index -= size;
			return war.leagueSequence.get(index);
		}

		/*
		synchronized SBean.DBForceWarStatus getWarStatus(int fid) {
			SBean.DBForceWarStatus status = warStatusMap.get(fid);
			if (status != null)
				return status.kdClone();
			return null;
		}
		*/

		private void initWarStatus(SBean.DBForceWarStatus status, int index) {
			ForceWarStatus s = new ForceWarStatus(status, index);
			warStatusMap.put(status.fid, s);
		}

		private void setWarStatus(SBean.DBForceWarStatus status) {
			ForceWarStatus s = warStatusMap.get(status.fid);
			if (s == null) {
				s = new ForceWarStatus(status, war.status.size());
				war.status.add(status);
			}
			else {
				s.status = status;
				war.status.set(s.listIndex, status);
			}

			warStatusMap.put(status.fid, s);
		}

		synchronized SBean.DBForceWarRecord getWarRecord(boolean league, int fid1, int fid2) {
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null)
				return null;
			if (cfg.recordEndTime < now)
				return null;

			Map<Integer, Map<Integer, ForceWarRecord>> warRecordMap;
			if (league)
				warRecordMap = warLeagueRecordMap;
			else
				warRecordMap = warCupRecordMap;

			Map<Integer, ForceWarRecord> records = warRecordMap.get(fid1);
			if (records != null) {
				ForceWarRecord record = records.get(fid2);
				if (record != null)
					return record.record.kdClone();
			}
			return null;
		}

		private void initWarRecord(boolean league, SBean.DBForceWarRecord record, int index) {
			Map<Integer, Map<Integer, ForceWarRecord>> warRecordMap;
			if (league)
				warRecordMap = warLeagueRecordMap;
			else
				warRecordMap = warCupRecordMap;

			ForceWarRecord r = new ForceWarRecord(record, index);
			// fid1
			Map<Integer, ForceWarRecord> records = warRecordMap.get(record.fid1);
			if (records == null)
				records = new HashMap<Integer, ForceWarRecord>();
			records.put(record.fid2, r);
			warRecordMap.put(record.fid1, records);
			// fid2
			records = warRecordMap.get(record.fid2);
			if (records == null)
				records = new HashMap<Integer, ForceWarRecord>();
			records.put(record.fid1, r);
			warRecordMap.put(record.fid2, records);
		}

		private void addWarRecord(boolean league, SBean.DBForceWarRecord record) {
			Map<Integer, Map<Integer, ForceWarRecord>> warRecordMap;
			List<DBForceWarRecord> warRecords;
			if (league) {
				warRecordMap = warLeagueRecordMap;
				warRecords = war.leagueRecords;
			}
			else {
				warRecordMap = warCupRecordMap;
				warRecords = war.cupRecords;
			}

			// fid1
			Map<Integer, ForceWarRecord> records = warRecordMap.get(record.fid1);
			if (records == null)
				records = new HashMap<Integer, ForceWarRecord>();
			ForceWarRecord r = records.get(record.fid2);
			if (r == null) {
				r = new ForceWarRecord(record, warRecords.size());
				warRecords.add(record);
			}
			else {
				// Use first record
				//r.record = record;
				//warRecords.set(r.listIndex, record);
			}
			records.put(record.fid2, r);
			warRecordMap.put(record.fid1, records);
			// fid2
			records = warRecordMap.get(record.fid2);
			if (records == null)
				records = new HashMap<Integer, ForceWarRecord>();
			records.put(record.fid1, r);
			warRecordMap.put(record.fid2, records);
		}

		synchronized SBean.DBForceWar getDBWarData()
		{
			return war.kdClone();
		}

		synchronized SBean.DBForceThief getDBThiefData()
		{
			return thief.kdClone();
		}

		synchronized SBean.DBForceWarBeast getDBBeastData()
		{
			return beast.kdClone();
		}

		private void initRoleScore(SBean.DBForceWarRoleScore score, int index) {
			ForceWarRoleScore s = new ForceWarRoleScore(score, index);
			warRoleScoreMap.put(score.rid, s);
		}

		private void setRoleScore(SBean.DBForceWarRoleScore score) {
			ForceWarRoleScore s = warRoleScoreMap.get(score.rid);
			if (s == null) {
				s = new ForceWarRoleScore(score, war.roleScores.size());
				war.roleScores.add(score);
			}
			else {
				s.score = score;
				war.roleScores.set(s.listIndex, score);
			}

			warRoleScoreMap.put(score.rid, s);
		}

		private void initCupWin(SBean.DBForceWarCupWin cupWin, int index) {
			ForceWarCupWin c = new ForceWarCupWin(cupWin, index);
			warCupWinMap.put(cupWin.rid, c);
		}

		private void setCupWin(SBean.DBForceWarCupWin cupWin) {
			ForceWarCupWin c = warCupWinMap.get(cupWin.rid);
			if (c == null) {
				c = new ForceWarCupWin(cupWin, war.cupWins.size());
				war.cupWins.add(cupWin);
			}
			else {
				c.cupWin = cupWin;
				war.cupWins.set(c.listIndex, cupWin);
			}

			warCupWinMap.put(cupWin.rid, c);
		}

		synchronized void setCalcResult(int fid1, int fid2, byte roleIndex1, byte roleIndex2, byte result,
				List<SBean.DBForceWarGeneralStatus> status1, List<SBean.DBForceWarGeneralStatus> status2) {
			ForceWarStatus s = warStatusMap.get(fid1);
			if (s == null)
				return;
			RuntimeForceWarStatus rts = warRTStatusMap.get(fid1);
			if (rts == null)
				return;
			if (s.status.oppoFid == fid2 && rts.roleIndex == roleIndex1 && rts.oppoRoleIndex == roleIndex2) {
				rts.calcResults.add(result);
				int win = 0;
				int lose = 0;
				for (byte r : rts.calcResults) {
					if (r > 0)
						win ++;
					else
						lose ++;
				}
				if ((win > lose && result > 0) || (win < lose && result <= 0)) {
					rts.nextGeneralStatus = new HashMap<Short, SBean.DBForceWarGeneralStatus>();
					if (rts.generalStatus != null) {
						for (SBean.DBForceWarGeneralStatus st : rts.generalStatus.values())
							rts.nextGeneralStatus.put(st.id, st);
					}
					for (SBean.DBForceWarGeneralStatus st : status1) {
						SBean.DBForceWarGeneralStatus oldst = rts.nextGeneralStatus.get(st.id);
						if (oldst == null || oldst.hp > 0)
							rts.nextGeneralStatus.put(st.id, st);
					}
					rts.nextOppoGeneralStatus = new HashMap<Short, SBean.DBForceWarGeneralStatus>();
					if (rts.oppoGeneralStatus != null) {
						for (SBean.DBForceWarGeneralStatus st : rts.oppoGeneralStatus.values())
							rts.nextOppoGeneralStatus.put(st.id, st);
					}
					for (SBean.DBForceWarGeneralStatus st : status2) {
						SBean.DBForceWarGeneralStatus oldst = rts.nextOppoGeneralStatus.get(st.id);
						if (oldst == null || oldst.hp > 0)
							rts.nextOppoGeneralStatus.put(st.id, st);
					}
				}
			}
		}

		List<Reward> rewardMails = new ArrayList<Reward>();
		List<Reward> headRewardMails = new ArrayList<Reward>();
		List<Reward> quizRewardMails = new ArrayList<Reward>();

		boolean onTimer()
		{
			int time = gs.getTime();

			List<Reward> thiefRewardMails = new ArrayList<Reward>();

			synchronized (this) {
				if (thiefRewards != null)
					sendThiefRewards(thiefRewardMails);
			}

			if (!thiefRewardMails.isEmpty()) {
				for (Reward r : thiefRewardMails)
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_FORCE_THIEF_REWARD, "", "" + r.rank, 0, true, r.attLst);
			}

			synchronized (this) {
				SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
				if (cfg == null)
					return false;
				if (time < cfg.startTime || time > cfg.endTime)
					return false;

				// TODO
				if( gs.getConfig().godMode == 0 )
				{
					if (time < cfg.selectionTimeStart - 600)
					{
						if (time % 420 == 0)
						{
							updateForceWarForceInfo();
						}
						return (time % 600 == 300);
					}
					else if (time > cfg.selectionTimeStart)
					{
						if (war.stage == 1)
						{
							updateForceWarForceMembers();
							war.stage = 2;
						}
					}
				}
				else
				{
					if (time < cfg.selectionTimeStart - 30)
					{
						if (time % 20 == 0)
						{
							updateForceWarForceInfo();
						}
						return (time % 30 == 15);
					}
					else if (time > cfg.selectionTimeStart)
					{
						if (war.stage == 1)
						{
							updateForceWarForceMembers();
							war.stage = 2;
						}
					}
				}

				SBean.ForceBattleGameCFGS finalGame = null;
				if (cfg.knockout.size() > 0)
					finalGame = cfg.knockout.get(cfg.knockout.size() - 1);

				//if (cfg.id != war.bid)
				//	war.stage = 0;

				if (time - lastStatusTime > 30 && war.stage < 5) {
					gs.getLogger().warn("force war status: stage=" + war.stage + ", league count=" + war.leagueSequence.size() + ", league record=" + war.leagueRecords.size() + ", cup record=" + war.cupRecords.size());
					lastStatusTime = time;
				}

				if (time > cfg.selectionTimeStart && time < finalGame.endTime && war.stage < 5) {
					boolean save = false;
					switch (war.stage)
					{
					case 2:
						collectLeagueForces();
						if (war.leagueSequence.size() > 0) {
							gs.getLogger().warn("force war collect league forces end!");
							war.stage = 3;
							save = true;
						}
						else
							return false;
					case 3:
						return updateLeague() || save;
					case 4:
						return updateCup();
					}
				}
				else if (time > finalGame.endTime && time < cfg.endTime && war.stage == 5) {
					gs.getLogger().warn("force war send rewards!");
					war.stage = 6;
					sendRewards(rewardMails);
					return true;
				}
				else if (time > finalGame.endTime + 30 && time < cfg.endTime + 30 && war.stage == 6) {
					gs.getLogger().warn("force war send head rewards!");
					war.stage = 7;
					sendHeadRewards(headRewardMails);
					quizRewards = collectQuizRewards();
					return true;
				}

				if (quizRewards != null)
					sendQuizRewards(quizRewardMails);
			}

			if (!thiefRewardMails.isEmpty()) {

			}
			else if (!quizRewardMails.isEmpty()) {
				for (Reward r : quizRewardMails)
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_FORCE_BATTLE_QUIZ_REWARD, "", "" + r.rank, 0, true, r.attLst);
				quizRewardMails.clear();
			}
			else if (!rewardMails.isEmpty()) {
				for (Reward r : rewardMails)
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_FORCE_BATTLE_REWARD, "", "" + r.rank, 0, true, r.attLst);
				rewardMails.clear();
			}
			else if (!headRewardMails.isEmpty()) {
				for (Reward r : headRewardMails)
					gs.getLoginManager().sysSendMessage(0, r.roleId, DBMail.Message.SUB_TYPE_FORCE_BATTLE_HEAD_REWARD, "", "" + r.rank, 0, true, r.attLst);
				headRewardMails.clear();
			}

			return false;
		}

		private byte[] getPlusEffect(SBean.DBForceWarForcePlayer player)
		{
			int k = 0;
			byte plus[] = {0,0,0};
			for (int item : player.plusItems) {
				if (item > 0 && gs.getGameData().getForceBattleCfgs().itemPlusEffects.size() > k) {
					SBean.ItemPlusEffectCFGS cfg = gs.getGameData().getForceBattleCfgs().itemPlusEffects.get(k);
					for (SBean.PlusEffectCFGS eff : cfg.plusEffect)
						if (item >= eff.count) {
							plus[k] = eff.plus;
						}
				}
				k ++;
				if (k >= 3)
					break;
			}
			return plus;
		}

		synchronized boolean updateCup()
		{
			boolean save = false;
			boolean end = false;
			boolean levelEnd = true;

			int i = 16;
			int base = 0;
			boolean third = false;
			while (i > 0) {
				levelEnd = true;
				for (int j = 0; j < i; j ++) {
					int winner = war.cupResults.get(base + i*2 + j);
					int fid1 = war.cupResults.get(base + j*2);
					int fid2 = war.cupResults.get(base + j*2+1);

					if (i == 1 && third) {
						// 3 - 4
						int f1 = 0;
						int f2 = 0;
						for (int k = 1; k <= 4; k ++) {
							int fid = war.cupResults.get(base - k);
							if (fid != fid1 && fid != fid2) {
								if (f1 == 0)
									f1 = fid;
								else
									f2 = fid;
							}
						}

						fid1 = f1;
						fid2 = f2;
						j = 1;
						winner = war.cupResults.get(base + i*2 + j);
					}

					if (fid1 <= 0 && fid2 <= 0) {
						if (i == 1 && third)
							end = true;
						continue;
					}
					else if (winner > 0) {
						if (i == 1)
							end = true;
						continue;
					}
					else if (fid1 > 0 && fid2 <= 0) {
						war.cupResults.set(base + i*2 + j, fid1);
						if (i == 1)
							end = true;
						continue;
					}
					else if (fid1 <= 0 && fid2 > 0) {
						war.cupResults.set(base + i*2 + j, fid2);
						if (i == 1)
							end = true;
						continue;
					}

					SBean.DBForceWarForcePlayer player1 = getForcePlayer(fid1);
					SBean.DBForceWarForcePlayer player2 = getForcePlayer(fid2);
					ForceWarStatus st = warStatusMap.get(fid1);
					ForceWarStatus st2 = warStatusMap.get(fid2);
					if (player1 == null || player2 == null || st == null || st2 == null)
						continue;
					SBean.DBForceWarStatus s = st.status;
					SBean.DBForceWarStatus s2 = st2.status;
					s.oppoFid = fid2;
					s2.oppoFid = fid1;

					RuntimeForceWarStatus rts = warRTStatusMap.get(fid1);
					if (rts == null) {
						rts = new RuntimeForceWarStatus();
						warRTStatusMap.put(fid1, rts);
					}

					RuntimeForceWarStatus rts2 = warRTStatusMap.get(fid2);
					if (rts2 == null) {
						rts2 = new RuntimeForceWarStatus();
						warRTStatusMap.put(fid2, rts2);
					}

					levelEnd = false;
					int flag = gs.getWarManager().getVerifyFlag();
					boolean retry = false;
					if (rts.matchBegin) {
						if (rts.calcResults.size() >= flag) {
							int win = 0;
							int lose = 0;
							for (byte r : rts.calcResults) {
								if (r > 0)
									win ++;
								else
									lose ++;
							}

							if ((win > lose && win >= flag) || (win < lose && lose >= flag)) {
								boolean w = win > lose;
								if (rts.record == null) {
									rts.record = new SBean.DBForceWarRecord();
									rts.record.fid1 = s.fid;
									rts.record.fid2 = s.oppoFid;
									rts.record.fname1 = player1.name;
									rts.record.fname2 = player2.name;
									rts.record.matches = new ArrayList<SBean.DBForceWarMatchRecordBrief>();
								}

								SBean.DBForceWarRolePlayer p1 = null;
								SBean.DBForceWarRolePlayer p2 = null;
								SBean.DBForceBeast b1 = null;
								SBean.DBForceBeast b2 = null;
								String name1 = "";
								String name2 = "";

								if (rts.roleIndex < player1.players.size()) {
									p1 = player1.players.get(rts.roleIndex);
									name1 = p1.name;
								}
								else {
									// beast
									b1 = getForceBeast(player1.id);
									name1 = "beast";
								}
								if (rts.oppoRoleIndex < player2.players.size()) {
									p2 = player2.players.get(rts.oppoRoleIndex);
									name2 = p2.name;
								}
								else {
									// beast
									b2 = getForceBeast(player2.id);
									name2 = "beast";
								}

								if ((p1 != null || b1 != null) && (p2 != null || b2 != null)) {
									SBean.DBForceWarMatchRecordBrief mr = new SBean.DBForceWarMatchRecordBrief();
									mr.index1 = rts.roleIndex;
									mr.index2 = rts.oppoRoleIndex;
									mr.result = (byte)(w?1:0);
									mr.seed = 0;
									mr.status1 = new ArrayList<SBean.DBForceWarGeneralStatus>();
									if (rts.generalStatus != null) {
										for (SBean.DBForceWarGeneralStatus status : rts.generalStatus.values())
											mr.status1.add(status.kdClone());
									}
									mr.status2 = new ArrayList<SBean.DBForceWarGeneralStatus>();
									if (rts.oppoGeneralStatus != null) {
										for (SBean.DBForceWarGeneralStatus status : rts.oppoGeneralStatus.values())
											mr.status2.add(status.kdClone());
									}
									rts.record.matches.add(mr);
								}

								if (i == 1)
									end = true;

								gs.getLogger().warn("force cup match over: (" + s.fid + ")" + name1 + " vs (" + s.oppoFid + ")" + name2 + "!");
								if (w) {
									rts.calcResults.clear();
									rts.oppoRoleIndex ++;
									rts.generalStatus = rts.nextGeneralStatus;
									rts.oppoGeneralStatus = null;
									rts.nextGeneralStatus = null;
									rts.nextOppoGeneralStatus = null;
									//rts.score += 5;
									if (p1 != null) {
										ForceWarCupWin cupWin = warCupWinMap.get(p1.id);
										if (cupWin == null)
											setCupWin(new SBean.DBForceWarCupWin(p1.id, 1));
										else {
											cupWin.cupWin.cupWin ++;
											setCupWin(cupWin.cupWin);
										}
									}
									if (p2 != null) {
										ForceWarCupWin cupWin = warCupWinMap.get(p2.id);
										if (cupWin == null)
											setCupWin(new SBean.DBForceWarCupWin(p2.id, 0));
									}
									rts.matchBegin = false;
									if (rts.oppoRoleIndex > player2.players.size()) {
										//rts.score += 1000;
										//int oldscore = s.score;
										//s.score += rts.score;
										//setScore(oldscore, s.score, fid1);
										//oldscore = s2.score;
										//s2.score += rts.score;
										//setScore(oldscore, s2.score, fid);
										rts.record.result = 1;
										addWarRecord(false, rts.record);
										warRTStatusMap.remove(fid1);
										warRTStatusMap.remove(fid2);
										war.cupResults.set(base + i*2 + j, fid1);
										gs.getLogger().warn("force cup game over: (" + s.fid + ") vs (" + s.oppoFid + "), (" + s.fid + ") wins!");
										save = true;
									}
								}
								else {
									rts.calcResults.clear();
									rts.roleIndex ++;
									rts.generalStatus = null;
									rts.oppoGeneralStatus = rts.nextOppoGeneralStatus;
									rts.nextGeneralStatus = null;
									rts.nextOppoGeneralStatus = null;
									//rts2.score += 5;
									if (p2 != null) {
										ForceWarCupWin cupWin = warCupWinMap.get(p2.id);
										if (cupWin == null)
											setCupWin(new SBean.DBForceWarCupWin(p2.id, 1));
										else {
											cupWin.cupWin.cupWin ++;
											setCupWin(cupWin.cupWin);
										}
									}
									if (p1 != null) {
										ForceWarCupWin cupWin = warCupWinMap.get(p1.id);
										if (cupWin == null)
											setCupWin(new SBean.DBForceWarCupWin(p1.id, 0));
									}
									rts.matchBegin = false;
									if (rts.roleIndex > player1.players.size()) {
										//rts2.score += 1000;
										//int oldscore = s2.score;
										//s2.score += rts2.score;
										//setScore(oldscore, s2.score, fid2);
										//rts.record.result = 0;
										//oldscore = s.score;
										//s.score += rts.score;
										//setScore(oldscore, s.score, fid);
										addWarRecord(false, rts.record);
										warRTStatusMap.remove(fid1);
										warRTStatusMap.remove(fid2);
										war.cupResults.set(base + i*2 + j, fid2);
										gs.getLogger().warn("force cup game over: (" + s.fid + ") vs (" + s.oppoFid + "), (" + s.oppoFid + ") wins!");
										save = true;
									}
								}
							}
							else
								retry = true;
						}
						else
							retry = true;

						if (retry && rts.matchTimeout < gs.getTime())
							rts.matchBegin = false;
					}
					else {
						// Send calc request
						rts.matchBegin = true;
						rts.matchTimeout = gs.getTime() + 20;
						Role r1 = gs.getLoginManager().getOnlineRole();
						Role r2 = gs.getLoginManager().getOnlineRole();
						if (player1.players.size() == 0 || player2.players.size() == 0)
							continue;

						SBean.DBForceWarRolePlayer p1 = null;
						SBean.DBForceWarRolePlayer p2 = null;
						SBean.DBForceBeast b1 = null;
						SBean.DBForceBeast b2 = null;

						if (rts.roleIndex < player1.players.size()) {
							p1 = player1.players.get(rts.roleIndex);
						}
						else {
							// beast
							b1 = getForceBeast(player1.id);

							if (b1 == null) {
								gs.getDB().execute(new UpdateForceWarBeastTrans(gs, this, player1.id));
							}
						}
						if (rts.oppoRoleIndex < player2.players.size()) {
							p2 = player2.players.get(rts.oppoRoleIndex);
						}
						else {
							// beast
							b2 = getForceBeast(player2.id);

							if (b2 == null) {
								gs.getDB().execute(new UpdateForceWarBeastTrans(gs, this, player2.id));
							}
						}

						if (r1 != null && r2 != null) {
							List<SBean.DBForceWarGeneralStatus> status1 = new ArrayList<SBean.DBForceWarGeneralStatus>();
							List<SBean.DBForceWarGeneralStatus> status2 = new ArrayList<SBean.DBForceWarGeneralStatus>();
							if (rts.generalStatus != null) {
								for (SBean.DBForceWarGeneralStatus status : rts.generalStatus.values())
									status1.add(status.kdClone());
							}
							if (rts.oppoGeneralStatus != null) {
								for (SBean.DBForceWarGeneralStatus status : rts.oppoGeneralStatus.values()) {
									status2.add(status.kdClone());
								}
							}
							byte[] eff1 = getPlusEffect(player1);
							byte[] eff2 = getPlusEffect(player2);

							List<DBRoleGeneral> generals1 = null;
							List<DBRoleGeneral> generals2 = null;
							List<DBPetBrief> pets1 = null;
							List<DBPetBrief> pets2 = null;
							List<SBean.DBRelation> relation1 = null;
							List<SBean.DBRelation> relation2 = null;
//							List<SBean.DBGeneralStone> gStone1 = null;
//							List<SBean.DBGeneralStone> gStone2 = null;
							String name1 = null;
							String name2 = null;

							if (p1 != null) {
								generals1 = p1.generals;
								pets1 = p1.pets;
								name1 = p1.name;
								relation1 = p1.relation;
//								gStone1 = p1.generals;
							}
							else if (b1 != null) {
								generals1 = new ArrayList<DBRoleGeneral>();
								DBRoleGeneral role = new DBRoleGeneral(b1.general.id, b1.general.lvl, b1.general.exp, (byte)100,
					                     b1.general.evoLvl, new SBean.DBWeapon(b1.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
					                     new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)b1.general.headicon,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
								for (short skill : b1.general.skills)
									role.skills.add(skill);
								for (short prop : b1.props)
									role.skills.add(prop);
								role.skills.add((short)b1.attackProp);
								role.skills.add((short)b1.mainProp);
								generals1.add(role);
								pets1 = new ArrayList<DBPetBrief>();
								name1 = "beast";
							}

							if (p2 != null) {
								generals2 = p2.generals;
								pets2 = p2.pets;
								name2 = p2.name;
								relation2 = p2.relation;
//								gStone2 = p2.gStones;
							}
							else if (b2 != null) {
								generals2 = new ArrayList<DBRoleGeneral>();
								DBRoleGeneral role = new DBRoleGeneral(b2.general.id, b2.general.lvl, b2.general.exp, (byte)100,
					                     b2.general.evoLvl, new SBean.DBWeapon(b2.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
					                     new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)0,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
								for (short skill : b2.general.skills)
									role.skills.add(skill);
								for (short prop : b2.props)
									role.skills.add(prop);
								role.skills.add((short)b2.attackProp);
								role.skills.add((short)b2.mainProp);
								generals2.add(role);
								pets2 = new ArrayList<DBPetBrief>();
								name2 = "beast";
							}

							if (generals1 != null && generals2 != null && pets1 != null && pets2 != null && name1 != null && name2 != null) {
								gs.getLogger().warn("force cup match ask for calc: (" + s.fid + ")" + name1 + " vs (" + s.oppoFid + ")" + name2 + ", role1=" + r1.id + ", role2=" + r2.id);
								r1.forceWarCalc(generals1, pets1, generals2, pets2, status1, status2, relation1, relation2, 0, s.fid, s.oppoFid, rts.roleIndex, rts.oppoRoleIndex, eff1, eff2);
								r2.forceWarCalc(generals1, pets1, generals2, pets2, status1, status2, relation1, relation2, 0, s.fid, s.oppoFid, rts.roleIndex, rts.oppoRoleIndex, eff1, eff2);
							}
						}
					}
				}

				if (end && i == 1 && !third) {
					third = true;
					end = false;
				}
				else {
					base += i * 2;
					i /= 2;
				}

				if (!levelEnd) {
					end = false;
					break;
				}
			}
			if (end) {
				collectRankList();
				gs.getLogger().warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!force war cup over!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				war.stage = 5;
				save = true;
			}
			return save;
		}

		//boolean collected = false;
		synchronized boolean updateLeague()
		{
			boolean save = false;
			boolean end = true;

			//test
			/*
			if (!collected && battle.forcePlayers.size() >= 2) {
				boolean ok = true;
				for (SBean.DBFBForcePlayer p : battle.forcePlayers.values())
					if (p.players.size() == 0)
						ok = false;
				if (ok) {
					collected = true;
					collectLeagueForces();
				}
				return false;
			}
			*/

			for (int i = 0; i < war.leagueSequence.size(); i ++) {
				int fid = getLeagueSequence(i);
				if (fid < 0)
					continue;
				SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
				ForceWarStatus st = warStatusMap.get(fid);
				if (st != null) {
					SBean.DBForceWarStatus s = st.status;
					int oppoCount = ForceWarManager.OPPO_MAX;
					if (oppoCount > war.leagueSequence.size() - 1)
						oppoCount = war.leagueSequence.size() - 1;
					while (s.oppoFid == 0 && s.warIndex < oppoCount) { // 閹广垹顕幍瀣禇鐎癸拷
						s.warIndex ++;
						int oppoFid = getLeagueSequence(i + s.warIndex);
						if (oppoFid <= 0 || oppoFid == fid)
							continue;
						s.oppoFid = oppoFid;
					}

					if (s.oppoFid > 0) {
						end = false;
						SBean.DBForceWarForcePlayer oppoPlayer = getForcePlayer(s.oppoFid);
						if (oppoPlayer == null) {
							s.oppoFid = 0;
							continue;
						}

						RuntimeForceWarStatus rts = warRTStatusMap.get(fid);
						if (rts == null) {
							rts = new RuntimeForceWarStatus();
							warRTStatusMap.put(fid, rts);
						}

						if (rts.matchBegin) {
							SBean.DBForceWarRecord record = getWarRecord(true, fid, s.oppoFid);
							if (record != null)
							{
								if (record.fid1 == fid) {
									for (SBean.DBForceWarMatchRecordBrief r : record.matches)
										if (r.index1 == rts.roleIndex && r.index2 == rts.oppoRoleIndex) {
											rts.calcResults.clear();
											rts.calcResults.add(r.result);
											rts.calcResults.add(r.result);
											break;
										}
								}
								else {
									for (SBean.DBForceWarMatchRecordBrief r : record.matches)
										if (r.index2 == rts.roleIndex && r.index1 == rts.oppoRoleIndex) {
											byte result = 0;
											if (r.result == 0)
												result = 1;
											rts.calcResults.clear();
											rts.calcResults.add(result);
											rts.calcResults.add(result);
											break;
										}
								}
							}

							if (player.players.size() == 0) {
								rts.calcResults.clear();
								rts.calcResults.add((byte)0);
								rts.calcResults.add((byte)0);
							}
							else if (oppoPlayer.players.size() == 0) {
								rts.calcResults.clear();
								rts.calcResults.add((byte)1);
								rts.calcResults.add((byte)1);
							}

							int flag = gs.getWarManager().getVerifyFlag();
							boolean retry = false;
							if (rts.calcResults.size() >= flag) {
								int win = 0;
								int lose = 0;
								for (byte r : rts.calcResults) {
									if (r > 0)
										win ++;
									else
										lose ++;
								}

								if ((win > lose && win >= flag) || (win < lose && lose >= flag)) {
									boolean w = win > lose;
									if (rts.record == null) {
										rts.record = new SBean.DBForceWarRecord();
										rts.record.fid1 = s.fid;
										rts.record.fid2 = s.oppoFid;
										rts.record.fname1 = player.name;
										rts.record.fname2 = oppoPlayer.name;
										rts.record.matches = new ArrayList<SBean.DBForceWarMatchRecordBrief>();
									}

									SBean.DBForceWarRolePlayer player1 = null;
									SBean.DBForceWarRolePlayer player2 = null;
									SBean.DBForceBeast b1 = null;
									SBean.DBForceBeast b2 = null;
									String name1 = "";
									String name2 = "";

									if (rts.roleIndex < player.players.size()) {
										player1 = player.players.get(rts.roleIndex);
										name1 = player1.name;
									}
									else {
										// beast
										b1 = getForceBeast(player.id);
										name1 = "beast";
									}
									if (rts.oppoRoleIndex < oppoPlayer.players.size()) {
										player2 = oppoPlayer.players.get(rts.oppoRoleIndex);
										name2 = player2.name;
									}
									else {
										// beast
										b2 = getForceBeast(oppoPlayer.id);
										name2 = "beast";
									}

									if ((player1 != null || b1 != null) && (player2 != null || b2 != null)) {
										SBean.DBForceWarMatchRecordBrief mr = new SBean.DBForceWarMatchRecordBrief();
										mr.index1 = rts.roleIndex;
										mr.index2 = rts.oppoRoleIndex;
										mr.result = (byte)(w?1:0);
										mr.seed = 0;
										mr.status1 = new ArrayList<SBean.DBForceWarGeneralStatus>();
										if (rts.generalStatus != null) {
											for (SBean.DBForceWarGeneralStatus status : rts.generalStatus.values())
												mr.status1.add(status.kdClone());
										}
										mr.status2 = new ArrayList<SBean.DBForceWarGeneralStatus>();
										if (rts.oppoGeneralStatus != null) {
											for (SBean.DBForceWarGeneralStatus status : rts.oppoGeneralStatus.values())
												mr.status2.add(status.kdClone());
										}
										rts.record.matches.add(mr);
									}

									gs.getLogger().warn("force league match over: (" + s.fid + ")" + name1 + " vs (" + s.oppoFid + ")" + name2 + ", (" + (w?s.fid:s.oppoFid) + ") wins!");
									boolean gameOver = false;
									if (w) {
										rts.calcResults.clear();
										rts.oppoRoleIndex ++;
										rts.generalStatus = rts.nextGeneralStatus;
										rts.oppoGeneralStatus = null;
										rts.nextGeneralStatus = null;
										rts.nextOppoGeneralStatus = null;
										//rts.score += 5;
										if (player1 != null) {
											ForceWarRoleScore score = warRoleScoreMap.get(player1.id);
											if (score == null)
												setRoleScore(new SBean.DBForceWarRoleScore(player1.id, 1));
											else {
												score.score.score += 1;
												setRoleScore(score.score);
											}
										}
										//gs.getLogger().warn("force league role score: (" + player1.id + ")" + player1.name + " score (" + sc + ")!");
										rts.matchBegin = false;
										if (rts.oppoRoleIndex > oppoPlayer.players.size()) {
											gs.getLogger().warn("force league game over: (" + s.fid + ") vs (" + s.oppoFid + "), (" + s.fid + ") wins!");
											rts.score += 1000;
											int oldscore = 0;
											if (s.scores.size() > 0)
												oldscore = s.scores.get(s.scores.size() - 1);
											int newscore = oldscore + rts.score;
											setScore(oldscore, newscore, fid, s.scores.size());
											s.scores.add(newscore);
											s.oppoFid = 0;
											rts.record.result = 1;
											addWarRecord(true, rts.record);
											warRTStatusMap.remove(fid);
											save = true;
											gameOver = true;
										}
									}
									else {
										rts.calcResults.clear();
										rts.roleIndex ++;
										rts.generalStatus = null;
										rts.oppoGeneralStatus = rts.nextOppoGeneralStatus;
										rts.nextGeneralStatus = null;
										rts.nextOppoGeneralStatus = null;
										if (player1 != null) {
											ForceWarRoleScore score = warRoleScoreMap.get(player1.id);
											if (score == null)
												setRoleScore(new SBean.DBForceWarRoleScore(player1.id, 0));
										}
										rts.matchBegin = false;
										if (rts.roleIndex > player.players.size()) {
											gs.getLogger().warn("force league game over: (" + s.fid + ") vs (" + s.oppoFid + "), (" + s.oppoFid + ") wins!");
											int oldscore = 0;
											if (s.scores.size() > 0)
												oldscore = s.scores.get(s.scores.size() - 1);
											int newscore = oldscore + rts.score;
											setScore(oldscore, newscore, fid, s.scores.size());
											s.scores.add(newscore);
											s.oppoFid = 0;
											rts.record.result = 0;
											addWarRecord(true, rts.record);
											warRTStatusMap.remove(fid);
											save = true;
											gameOver = true;
										}
									}

									if (gameOver) {
										int total = war.players.size() * 5;
										progLeague ++;
										gs.getLogger().warn("force war league progress " + progLeague + "/" + total);
									}
								}
								else
									retry = true;
							}
							else
								retry = true;

							if (retry && rts.matchTimeout < gs.getTime())
								rts.matchBegin = false;
						}
						else {
							// Send calc request
							rts.matchBegin = true;
							rts.matchTimeout = gs.getTime() + 20;
							Role r1 = gs.getLoginManager().getOnlineRole();
							Role r2 = gs.getLoginManager().getOnlineRole();

							SBean.DBForceWarRolePlayer p1 = null;
							SBean.DBForceWarRolePlayer p2 = null;
							SBean.DBForceBeast b1 = null;
							SBean.DBForceBeast b2 = null;

							if (rts.roleIndex < player.players.size()) {
								p1 = player.players.get(rts.roleIndex);
							}
							else {
								// beast
								b1 = getForceBeast(player.id);

								if (b1 == null) {
									gs.getDB().execute(new UpdateForceWarBeastTrans(gs, this, player.id));
								}
							}
							if (rts.oppoRoleIndex < oppoPlayer.players.size()) {
								p2 = oppoPlayer.players.get(rts.oppoRoleIndex);
							}
							else {
								// beast
								b2 = getForceBeast(oppoPlayer.id);

								if (b2 == null) {
									gs.getDB().execute(new UpdateForceWarBeastTrans(gs, this, oppoPlayer.id));
								}
							}

							if (r1 != null && r2 != null) {
								List<SBean.DBForceWarGeneralStatus> status1 = new ArrayList<SBean.DBForceWarGeneralStatus>();
								List<SBean.DBForceWarGeneralStatus> status2 = new ArrayList<SBean.DBForceWarGeneralStatus>();
								if (rts.generalStatus != null) {
									for (SBean.DBForceWarGeneralStatus status : rts.generalStatus.values())
										status1.add(status.kdClone());
								}
								if (rts.oppoGeneralStatus != null) {
									for (SBean.DBForceWarGeneralStatus status : rts.oppoGeneralStatus.values()) {
										status2.add(status.kdClone());
									}
								}
								byte[] eff1 = getPlusEffect(player);
								byte[] eff2 = getPlusEffect(oppoPlayer);

								List<DBRoleGeneral> generals1 = null;
								List<DBRoleGeneral> generals2 = null;
								List<DBPetBrief> pets1 = null;
								List<DBPetBrief> pets2 = null;
								List<SBean.DBRelation> relation1 = null;
								List<SBean.DBRelation> relation2 = null;
								String name1 = null;
								String name2 = null;

								if (p1 != null) {
									generals1 = p1.generals;
									pets1 = p1.pets;
									name1 = p1.name;
									relation1 = p1.relation;
								}
								else if (b1 != null) {
									generals1 = new ArrayList<DBRoleGeneral>();
									DBRoleGeneral role = new DBRoleGeneral(b1.general.id, b1.general.lvl, b1.general.exp, (byte)100, b1.general.evoLvl,
											new SBean.DBWeapon(b1.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
											new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)b1.general.headicon,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
									for (short skill : b1.general.skills)
										role.skills.add(skill);
									for (short prop : b1.props)
										role.skills.add(prop);
									role.skills.add((short)b1.attackProp);
									role.skills.add((short)b1.mainProp);
									generals1.add(role);
									pets1 = new ArrayList<DBPetBrief>();
									name1 = "beast";
								}

								if (p2 != null) {
									generals2 = p2.generals;
									pets2 = p2.pets;
									name2 = p2.name;
									relation2 = p2.relation;
								}
								else if (b2 != null) {
									generals2 = new ArrayList<DBRoleGeneral>();
									DBRoleGeneral role = new DBRoleGeneral(b2.general.id, b2.general.lvl, b2.general.exp, (byte)100, b2.general.evoLvl,
											new SBean.DBWeapon(b2.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
											new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)0,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
									for (short skill : b2.general.skills)
										role.skills.add(skill);
									for (short prop : b2.props)
										role.skills.add(prop);
									role.skills.add((short)b2.attackProp);
									role.skills.add((short)b2.mainProp);
									generals2.add(role);
									pets2 = new ArrayList<DBPetBrief>();
									name2 = "beast";
								}

								if (generals1 != null && generals2 != null && pets1 != null && pets2 != null && name1 != null && name2 != null) {
									gs.getLogger().warn("force league match ask for calc: (" + s.fid + ")" + name1 + " vs (" + s.oppoFid + ")" + name2 + ", role1=" + r1.id + ", role2=" + r2.id);
									r1.forceWarCalc(generals1, pets1, generals2, pets2, status1, status2, relation1, relation2, 0, s.fid, s.oppoFid, rts.roleIndex, rts.oppoRoleIndex, eff1, eff2);
									r2.forceWarCalc(generals1, pets1, generals2, pets2, status1, status2, relation1, relation2, 0, s.fid, s.oppoFid, rts.roleIndex, rts.oppoRoleIndex, eff1, eff2);
								}
							}
						}
					}
				}
			}
			if (end && war.leagueSequence.size() > 0 && war.cupResults.size() == 0) {
				collectCupForces();
				gs.getLogger().warn("force war league over!");
				war.stage = 4;
				save = true;
			}
			return save;
		}

		synchronized void setScore(int oldscore, int score, int id, int round)
		{
			removeSortList(oldscore, id, round);
			insertSortList(score, id, round);
		}

		synchronized void insertSortList(int score, int id, int round)
		{
			while (sortLists.size() <= round)
				sortLists.add(new ArrayList<SortScore>());
			List<SortScore> sortList = sortLists.get(round);

			int power = 0;
			SBean.DBForceWarForcePlayer player = getForcePlayer(id);
			if (player != null) {
				for (SBean.DBForceWarRolePlayer p : player.players)
					power += p.power;
			}

			int size = sortList.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (score > sortList.get(mid).score) {
					end = mid - 1;
				} else if (score < sortList.get(mid).score) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else if (power > sortList.get(mid).power){
					end = mid - 1;
				} else if (power < sortList.get(mid).power) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}

			sortList.add(mid, new SortScore(score, id, power));
		}

		synchronized void removeSortList(int score, int id, int round)
		{
			if (sortLists.size() <= round)
				return;
			List<SortScore> sortList = sortLists.get(round);

			int index = getSortList(score, id, round);
			if (index >= 0)
				sortList.remove(index);
		}

		private int getSortList(int score, int id, int round)
		{
			if (sortLists.size() <= round)
				return -1;
			List<SortScore> sortList = sortLists.get(round);

			int size = sortList.size();
			if (size == 0)
				return -1;
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (score >= sortList.get(mid).score) {
					end = mid - 1;
				} else if (score < sortList.get(mid).score) {
					begin = mid + 1;
				}
			}
			int tmp = mid;
			if (sortList.get(tmp).score != score)
				tmp ++;
			SortScore p = null;
			while (tmp < size && (p = sortList.get(tmp)).score == score) {
				if (p.fid == id) {
					return tmp;
				}
				tmp ++;
			}
			return -1;
		}

		synchronized List<ScoreRank> getScoreRanks(int fid)
		{
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null)
				return null;

			int round = -1;
			for (int t : cfg.leagueEndTimes) {
				if (t < now) {
					round ++;
				}
				else
					break;
			}
			if (round < 0)
				return null;

			if (sortLists.size() == 0)
				return null;

			if (sortLists.size() <= round)
				round = sortLists.size() - 1;
			List<SortScore> sortList = sortLists.get(round);

			ForceWarStatus st = warStatusMap.get(fid);
			if (st == null)
				return null;
			SBean.DBForceWarStatus s = st.status;
			if (s.scores.size() <= round)
				return null;
			int index = getSortList(s.scores.get(round), s.fid, round);
			if (index < 0)
				return null;

			SortScore ss = sortList.get(index);
			List<ScoreRank> ranks = new ArrayList<ScoreRank>();
			for (byte i = 0; i < 32; i ++) {
				if (i >= sortList.size())
					break;
				SortScore score = sortList.get(i);
				SBean.DBForceWarForcePlayer player = getForcePlayer(score.fid);
				if (player == null)
					continue;
				ScoreRank rank = new ScoreRank(i, score.fid, score.score, player.icon, player.name);
				ranks.add(rank);
			}
			if (index >= 32) {
				SBean.DBForceWarForcePlayer player = getForcePlayer(ss.fid);
				if (player != null) {
					ScoreRank rank = new ScoreRank(index, ss.fid, ss.score, player.icon, player.name);
					ranks.add(rank);
				}
			}
			return ranks;
		}

		public List<PowerRank> getPowerRanks(int fid)
		{
			if (fid > 0)
				return forcePowerRanks.get(fid);
			return powerRanks;
		}

		public List<RoleScoreRank> getRoleScoreRanks(int fid)
		{
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null || cfg.leagueEndTimes.size() == 0)
				return null;

			int t = cfg.leagueEndTimes.get(cfg.leagueEndTimes.size() - 1);
			if (t > now)
				return null;

			if (fid > 0)
				return forceRoleScoreRanks.get(fid);
			return roleScoreRanks;
		}

		public List<DonateRank> getDonateRanks(int fid)
		{
			if (fid > 0)
				return forceDonateRanks.get(fid);
			return donateRanks;
		}

		public List<CupWinRank> getCupWinRanks(int fid)
		{
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null || cfg.knockout.size() == 0)
				return null;

			int t = cfg.knockout.get(cfg.knockout.size() - 1).endTime;
			if (t > now)
				return null;

			if (fid > 0)
				return forceCupWinRanks.get(fid);
			return cupWinRanks;
		}

		synchronized void collectLeagueForces() {

			war.leagueSequence.clear();
			for (SBean.DBForceWarForcePlayer player : war.players) {
				if (player.players.size() > 0) {
					SBean.DBForceWarStatus s = new SBean.DBForceWarStatus(player.id, 0, (byte)0, new ArrayList<Integer>(), (short)0, new ArrayList<SBean.DBForceWarRoleStatus>());
					setWarStatus(s);
					war.leagueSequence.add(player.id);
				}
				for (SBean.DBForceWarRolePlayer p : player.players) {
					ForceWarRoleScore score = warRoleScoreMap.get(p.id);
					if (score == null)
						setRoleScore(new SBean.DBForceWarRoleScore(p.id, 0));
				}
			}
			Collections.sort(war.leagueSequence, new Comparator<Integer>() {
				@Override
				public int compare(Integer arg0, Integer arg1) {
					SBean.DBForceWarForcePlayer player1 = getForcePlayer((Integer)arg0);
					SBean.DBForceWarForcePlayer player2 = getForcePlayer((Integer)arg1);

					if (player1 == null || player2 == null)
						return 0;

					return player2.players.size() - player1.players.size();
				}
			});
			int group = war.leagueSequence.size() / 6;
			List<Integer> topList = new ArrayList<Integer>();
			List<Integer> otherList = new ArrayList<Integer>();
			List<Integer> newList = new ArrayList<Integer>();
			for (int i = 0; i < group; i ++)
				topList.add(war.leagueSequence.get(i));
			for (int i = group; i < war.leagueSequence.size(); i ++)
				otherList.add(war.leagueSequence.get(i));
			Collections.shuffle(otherList);
			int index1 = 0;
			int index2 = 0;
			for (int i = 0; i < war.leagueSequence.size(); i ++) {
				if (index1 < topList.size() && i % 6 == 0)
					newList.add(topList.get(index1 ++));
				else
					newList.add(otherList.get(index2 ++));
			}
			war.leagueSequence = newList;
			short index = 0;
			for (int fid : war.leagueSequence) {
				ForceWarStatus s = warStatusMap.get(fid);
				if (s != null && s.status != null)
					s.status.leagueSeqIndex = index;
				index ++;
			}

			progLeague = 0;
		}

		private void collectCupForces() {
			if (war.leagueSequence.size() == 1) {
				setScore(0, 0, war.leagueSequence.get(0), 0);
			}

			if (sortLists.size() == 0)
				return;

			war.cupResults.clear();
			while (war.cupResults.size() < 32)
				war.cupResults.add(0);

			List<SortScore> sortList = sortLists.get(sortLists.size() - 1);
			List<SortScore> newSort = new ArrayList<SortScore>();

			for (int j = 0; j < 4; j ++) {
				for (int i = 0; i < 4; i ++) {
					int index = j*4 + i;
					if (index >= sortList.size())
						newSort.add(null);
					else
						newSort.add(sortList.get(index));
				}

				for (int i = 0; i < 4; i ++) {
					int index = 31 - j*4 - i;
					if (index >= sortList.size())
						newSort.add(null);
					else
						newSort.add(sortList.get(index));
				}
			}

			int i = 0;
			for (SortScore s : newSort) {
				int group = 0;
				switch (i % 4)
				{
				case 0:
					group = 0;
					break;
				case 1:
					group = 2;
					break;
				case 2:
					group = 1;
					break;
				case 3:
					group = 3;
					break;
				}
				int pos = group * 8 + i / 4;
				if (s != null)
					war.cupResults.set(pos, s.fid);
				i ++;
				if (i >= 32)
					break;
			}

			for (int fid : war.cupResults) {
				if (fid > 0) {
					SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
					if (player == null)
						continue;

					for (SBean.DBForceWarRolePlayer p : player.players) {
						ForceWarCupWin cupWin = warCupWinMap.get(p.id);
						if (cupWin== null)
							setCupWin(new SBean.DBForceWarCupWin(p.id, 0));
					}
				}
			}

			i = 16;
			while (i > 0) {
				for (int j = 0; j < i; j ++) {
					war.cupResults.add(0);
					war.cupQuizs.add(new SBean.DBForceWarQuiz(new ArrayList<Integer>(), new ArrayList<Integer>()));
				}
				i /= 2;
			}
			war.cupResults.add(0);
			war.cupQuizs.add(new SBean.DBForceWarQuiz(new ArrayList<Integer>(), new ArrayList<Integer>()));
		}

		private void collectRankList() {

			powerRanks.clear();
			roleScoreRanks.clear();
			donateRanks.clear();
			forcePowerRanks.clear();
			forceRoleScoreRanks.clear();
			forceDonateRanks.clear();

			for (SBean.DBForceWarStatus s : war.status) {
				SBean.DBForceWarForcePlayer player = getForcePlayer(s.fid);
				if (player == null)
					continue;
				for (SBean.DBForceWarRolePlayer p : player.players) {
					// Power rank
					PowerRank powerRank = new PowerRank(p.id, p.level, p.icon, p.name, p.power);
					insertPowerRank(powerRank, powerRanks);
					List<PowerRank> pRanks = forcePowerRanks.get(s.fid);
					if (pRanks == null) {
						pRanks = new ArrayList<PowerRank>();
						forcePowerRanks.put(s.fid, pRanks);
					}
					insertPowerRank(powerRank, pRanks);
					// Donate rank
					int plus = 0;
					for (int pi : p.plusItems)
						plus += pi;
					DonateRank donateRank = new DonateRank(p.id, p.level, p.icon, p.name, plus);
					insertDonateRank(donateRank, donateRanks);
					List<DonateRank> dRanks = forceDonateRanks.get(s.fid);
					if (dRanks == null) {
						dRanks = new ArrayList<DonateRank>();
						forceDonateRanks.put(s.fid, dRanks);
					}
					insertDonateRank(donateRank, dRanks);
					// Role score rank
					ForceWarRoleScore score = warRoleScoreMap.get(p.id);
					if (score == null)
						continue;
					RoleScoreRank roleScoreRank = new RoleScoreRank(p.id, p.level, p.icon, p.name, score.score.score);
					insertRoleScoreRank(roleScoreRank, roleScoreRanks);
					List<RoleScoreRank> rRanks = forceRoleScoreRanks.get(s.fid);
					if (rRanks == null) {
						rRanks = new ArrayList<RoleScoreRank>();
						forceRoleScoreRanks.put(s.fid, rRanks);
					}
					insertRoleScoreRank(roleScoreRank, rRanks);
					// Cup win rank
					ForceWarCupWin cupWin = warCupWinMap.get(p.id);
					if (cupWin == null)
						continue;
					CupWinRank cupWinRank = new CupWinRank(p.id, p.level, p.icon, p.name, cupWin.cupWin.cupWin);
					insertCupWinRank(cupWinRank, cupWinRanks);
					List<CupWinRank> cRanks = forceCupWinRanks.get(s.fid);
					if (cRanks == null) {
						cRanks = new ArrayList<CupWinRank>();
						forceCupWinRanks.put(s.fid, cRanks);
					}
					insertCupWinRank(cupWinRank, cRanks);
				}
			}
		}

		synchronized void insertPowerRank(PowerRank r, List<PowerRank> powerRanks)
		{
			int size = powerRanks.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (r.power > powerRanks.get(mid).power) {
					end = mid - 1;
				} else if (r.power < powerRanks.get(mid).power) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			powerRanks.add(mid, r);
		}

		synchronized void insertRoleScoreRank(RoleScoreRank r, List<RoleScoreRank> roleScoreRanks)
		{
			int size = roleScoreRanks.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (r.score > roleScoreRanks.get(mid).score) {
					end = mid - 1;
				} else if (r.score < roleScoreRanks.get(mid).score) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			roleScoreRanks.add(mid, r);
		}

		synchronized void insertDonateRank(DonateRank r, List<DonateRank> donateRanks)
		{
			int size = donateRanks.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (r.donate > donateRanks.get(mid).donate) {
					end = mid - 1;
				} else if (r.donate < donateRanks.get(mid).donate) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			donateRanks.add(mid, r);
		}

		synchronized void insertCupWinRank(CupWinRank r, List<CupWinRank> cupWinRanks)
		{
			int size = cupWinRanks.size();
			int mid = 0;
			int begin = 0;
			int end = size - 1;
			while (begin <= end) {
				mid = (end - begin) / 2 + begin;
				if (r.cupWin > cupWinRanks.get(mid).cupWin) {
					end = mid - 1;
				} else if (r.cupWin < cupWinRanks.get(mid).cupWin) {
					begin = mid + 1;
					if (begin > end)
						mid = begin;
				} else {
					break;
				}
			}
			cupWinRanks.add(mid, r);
		}

		synchronized List<WarData> getLeagueData(int fid)
		{
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null)
				return null;

			SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
			if (player == null)
				return null;

			Map<Integer, ForceWarRecord> records = warLeagueRecordMap.get(fid);
			if (records == null)
				return null;

			ForceWarStatus st = warStatusMap.get(fid);
			if (st == null || st.status == null)
				return null;

			int oppoCount = ForceWarManager.OPPO_MAX;
			if (oppoCount > war.leagueSequence.size() - 1)
				oppoCount = war.leagueSequence.size() - 1;
			List<Integer> oppoFids = new ArrayList<Integer>();
			int warIndex = 0;
			while (warIndex < oppoCount) {
				warIndex ++;
				int oppoFid = getLeagueSequence(st.status.leagueSeqIndex + warIndex);
				if (oppoFid <= 0 || oppoFid == fid)
					continue;
				oppoFids.add(oppoFid);
			}

			int round = 0;
			List<WarData> league = new ArrayList<WarData>();
			for (int fid2 : oppoFids) {
			//for (ForceWarRecord f : records.values()) {
				boolean timeNotReach = false;
				if (cfg.leagueEndTimes.size() > round) {
					if (cfg.leagueEndTimes.get(round) > now)
						timeNotReach = true;
				}

				round ++;
				ForceWarRecord f = records.get(fid2);
				if (f == null)
					continue;

				WarData data = new WarData();
				boolean win;
				boolean revert = (f.record.fid1 != fid);
				if (!revert) {
					fid2 = f.record.fid2;
					win = f.record.result > 0;
				}
				else {
					fid2 = f.record.fid1;
					win = f.record.result <= 0;
				}

				SBean.DBForceWarForcePlayer oppo = getForcePlayer(fid2);
				if (oppo == null)
					continue;

				if (timeNotReach)
					continue;

				SBean.DBForceBeast beast1 = getForceBeast(fid);
				SBean.DBForceBeast beast2 = getForceBeast(fid2);

				data.fid1 = fid;
				data.fid2 = fid2;
				data.name1 = player.name;
				data.name2 = oppo.name;
				data.headName1 = player.headName;
				data.headName2 = oppo.headName;
				data.icon1 = player.icon;
				data.icon2 = oppo.icon;
				data.win = win;
				for (SBean.DBForceWarMatchRecordBrief r : f.record.matches) {
					WarMatchData match = new WarMatchData();
					if (!revert) {
						if (r.index1 >= player.players.size()) {
							match.roleName1 = player.name + " ";
							boolean found = false;
							if (beast1 != null) {
								SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast1.general.id);
								if (gcfg != null) {
									found = true;
									match.roleName1 += gcfg.name;
								}
							}
							if (!found)
								match.roleName1 += "Beast";
						}
						else
							match.roleName1 = player.players.get(r.index1).name;
						if (r.index2 >= oppo.players.size()) {
							match.roleName2= oppo.name + " ";
							boolean found = false;
							if (beast2 != null) {
								SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast2.general.id);
								if (gcfg != null) {
									found = true;
									match.roleName2 += gcfg.name;
								}
							}
							if (!found)
								match.roleName2 += "Beast";
						}
						else
							match.roleName2 = oppo.players.get(r.index2).name;
						match.roleIndex1 = r.index1;
						match.roleIndex2 = r.index2;
						match.win = r.result > 0;
					}
					else {
						if (r.index2 >= player.players.size()) {
							match.roleName1 = player.name + " ";
							boolean found = false;
							if (beast1 != null) {
								SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast1.general.id);
								if (gcfg != null) {
									found = true;
									match.roleName1 += gcfg.name;
								}
							}
							if (!found)
								match.roleName1 += "Beast";
						}
						else
							match.roleName1 = player.players.get(r.index2).name;
						if (r.index1 >= oppo.players.size()){
							match.roleName2= oppo.name + " ";
							boolean found = false;
							if (beast2 != null) {
								SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast2.general.id);
								if (gcfg != null) {
									found = true;
									match.roleName2 += gcfg.name;
								}
							}
							if (!found)
								match.roleName2 += "Beast";
						}
						else
							match.roleName2 = oppo.players.get(r.index1).name;
						match.roleIndex1 = r.index2;
						match.roleIndex2 = r.index1;
						match.win = r.result <= 0;
					}
					data.matchData.add(match);
				}
				league.add(data);
			}
			return league;
		}

		synchronized List<WarData> getCupData(int rid)
		{
			int now = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null)
				return null;
			if (war.cupResults.size() == 0)
				return null;

			List<WarData> cup = new ArrayList<WarData>();
			int i = 16;
			int base = 0;
			int round = 0;
			boolean third = false;
			while (i > 0) {
				boolean timeNotReach = false;
				if (cfg.knockout.size() > round) {
					SBean.ForceBattleGameCFGS game = cfg.knockout.get(round);
					if (game != null && game.startTime > now)
						timeNotReach = true;
				}

				if (round == 0 && cfg.leagueEndTimes.get(cfg.leagueEndTimes.size() - 1) < now)
					timeNotReach = false;

				for (int j = 0; j < i; j ++) {
					int fid1 = war.cupResults.get(base + j*2);
					int fid2 = war.cupResults.get(base + j*2+1);

					if (i == 1 && third) {
						// 3 - 4
						int f1 = 0;
						int f2 = 0;
						for (int k = 1; k <= 4; k ++) {
							int fid = war.cupResults.get(base - k);
							if (fid != fid1 && fid != fid2) {
								if (f1 == 0)
									f1 = fid;
								else
									f2 = fid;
							}
						}

						fid1 = f1;
						fid2 = f2;
					}

					WarData data = new WarData();
					data.fid1 = fid1;
					data.fid2 = fid2;
					data.name1 = "";
					data.name2 = "";
					data.headName1 = "";
					data.headName2 = "";

					if (timeNotReach) {
						data.fid1 = 0;
						data.fid2 = 0;
						cup.add(data);
						continue;
					}

					SBean.DBForceWarForcePlayer player1 = null;
					SBean.DBForceWarForcePlayer player2 = null;
					SBean.DBForceBeast beast1 = null;
					SBean.DBForceBeast beast2 = null;

					if (fid1 > 0) {
						player1 = getForcePlayer(fid1);
						if (player1 == null)
							return null;
						beast1 = getForceBeast(fid1);
						data.name1 = player1.name;
						data.headName1 = player1.headName;
						data.icon1 = player1.icon;
						if (fid2 <= 0)
							data.win = true;
					}
					if (fid2 > 0) {
						player2 = getForcePlayer(fid2);
						if (player2 == null)
							return null;
						beast2 = getForceBeast(fid2);
						data.name2 = player2.name;
						data.headName2 = player2.headName;
						data.icon2 = player2.icon;
						if (fid1 <= 0)
							data.win = false;
					}

					if (player1 == null || player2 == null) {
						cup.add(data);
						continue;
					}

					Map<Integer, ForceWarRecord> records = warCupRecordMap.get(fid1);
					if (records == null)
						return null;
					ForceWarRecord f = records.get(fid2);
					if (f == null)
						return null;

					boolean win;
					boolean revert = (f.record.fid1 != fid1);
					if (!revert) {
						fid2 = f.record.fid2;
						win = f.record.result > 0;
					}
					else {
						fid2 = f.record.fid1;
						win = f.record.result <= 0;
					}

					data.win = win;
					int index = third ? base/2 + j + 1 : base/2 + j;
					SBean.DBForceWarQuiz quiz = war.cupQuizs.get(index);
					if (quiz.f1WinRoles.contains(rid))
					{
						data.quizfid = fid1;
					}
					else if (quiz.f2WinRoles.contains(rid))
					{
						data.quizfid = fid2;
					}
					else
					{
						data.quizfid = 0;
					}
					for (SBean.DBForceWarMatchRecordBrief r : f.record.matches) {
						WarMatchData match = new WarMatchData();
						if (!revert) {
							if (r.index1 >= player1.players.size()) {
								match.roleName1 = player1.name + " ";
								boolean found = false;
								if (beast1 != null) {
									SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast1.general.id);
									if (gcfg != null) {
										found = true;
										match.roleName1 += gcfg.name;
									}
								}
								if (!found)
									match.roleName1 += "Beast";
							}
							else
								match.roleName1 = player1.players.get(r.index1).name;
							if (r.index2 >= player2.players.size()) {
								match.roleName2= player2.name + " ";
								boolean found = false;
								if (beast2 != null) {
									SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast2.general.id);
									if (gcfg != null) {
										found = true;
										match.roleName2 += gcfg.name;
									}
								}
								if (!found)
									match.roleName2 += "Beast";
							}
							else
								match.roleName2 = player2.players.get(r.index2).name;
							match.roleIndex1 = r.index1;
							match.roleIndex2 = r.index2;
							match.win = r.result > 0;
						}
						else {
							if (r.index2 >= player1.players.size()) {
								match.roleName1 = player1.name + " ";
								boolean found = false;
								if (beast1 != null) {
									SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast1.general.id);
									if (gcfg != null) {
										found = true;
										match.roleName1 += gcfg.name;
									}
								}
								if (!found)
									match.roleName1 += "Beast";
							}
							else
								match.roleName1 = player1.players.get(r.index2).name;
							if (r.index1 >= player2.players.size()) {
								match.roleName2= player2.name + " ";
								boolean found = false;
								if (beast2 != null) {
									SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(beast2.general.id);
									if (gcfg != null) {
										found = true;
										match.roleName2 += gcfg.name;
									}
								}
								if (!found)
									match.roleName2 += "Beast";
							}
							else
								match.roleName2 = player2.players.get(r.index1).name;
							match.roleIndex1 = r.index2;
							match.roleIndex2 = r.index1;
							match.win = r.result <= 0;
						}
						data.matchData.add(match);
					}
					cup.add(data);
				}

				if (i == 1 && !third) {
					third = true;
				}
				else {
					base += i * 2;
					i /= 2;
					round ++;
				}
			}

			return cup;
		}

		private List<List<Integer>> collectRewards()
		{
			Set<Integer> rewardsset = new HashSet<Integer>();
			List<List<Integer>> rewards = new ArrayList<List<Integer>>();
			int base = 62;
			int i = 1;
			while (base > 0) {
				List<Integer> re = new ArrayList<Integer>();
				for (int j = 0; j < i; j ++) {
					int fid = war.cupResults.get(base - j);
					if (fid > 0 && !rewardsset.contains(fid)) {
						re.add(fid);
						rewardsset.add(fid);
					}
				}
				rewards.add(re);

				base -= i;
				i *= 2;
			}
			return rewards;
		}

		private void sendRewards(List<Reward> rewardMails)
		{
			List<List<Integer>> rewards = collectRewards();
			if (rewards == null)
				return;

			List<SBean.ForceBattleRewardCFGS> cfgs = gs.getGameData().getForceBattleCfgs().rewards;
			int i = 0;
			for (List<Integer> l : rewards) {
				List<Integer> r = new ArrayList<Integer>();
				if (l != null) {
					for (int fid : l) {
						SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
						for (int id : player.froles)
							r.add(id);
					}
				}

				if (i >= cfgs.size())
					return;
				SBean.ForceBattleRewardCFGS cfg = cfgs.get(i);
				if (cfg == null)
					continue;

				for (int rid : r) {
					if (rid <= 0)
						continue;
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					if (cfg.item1 > 0)
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, cfg.item1, cfg.item1Count));
					if (cfg.item2 > 0)
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, cfg.item2, cfg.item2Count));
					if (cfg.money > 0)
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, cfg.money));
					if (cfg.stone > 0)
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, cfg.stone));
					if (cfg.point > 0)
						attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_FORCE_POINT, (short)0, cfg.point));
					rewardMails.add(new Reward(rid, i, attLst));
				}

				i ++;
			}
		}

		private List<Integer> collectHeadRewards()
		{
			List<Integer> rewards = new ArrayList<Integer>();
			if (war.cupResults.size() >= 64) {
				int fid1 = war.cupResults.get(62);
				int fid3 = war.cupResults.get(63);
				int fid2 = war.cupResults.get(61);
				if (fid2 == fid1)
					fid2 = war.cupResults.get(60);
				rewards.add(fid1);
				rewards.add(fid2);
				rewards.add(fid3);
			}
			return rewards;
		}

		private void sendHeadRewards(List<Reward> rewardMails)
		{
			List<Integer> rewards = collectHeadRewards();
			if (rewards == null)
				return;

			List<SBean.ForceBattleRewardCFGS> cfgs = gs.getGameData().getForceBattleCfgs().headRewards;
			int i = 0;
			for (int fid : rewards) {
				SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
				if (player == null)
					continue;

				if (i >= cfgs.size())
					return;
				SBean.ForceBattleRewardCFGS cfg = cfgs.get(i);
				if (cfg == null)
					continue;

				int rid = player.headId;
				if (rid <= 0)
					continue;
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				if (cfg.item1 > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, cfg.item1, cfg.item1Count));
				if (cfg.item2 > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, cfg.item2, cfg.item2Count));
				if (cfg.money > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, cfg.money));
				if (cfg.stone > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, cfg.stone));
				if (cfg.point > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_FORCE_POINT, (short)0, cfg.point));
				rewardMails.add(new Reward(rid, i, attLst));

				i ++;
			}
		}

		private List<QuizReward> collectQuizRewards()
		{
			List<QuizReward> rewards = new ArrayList<QuizReward>();
			int base = 0;
			int i = 16;
			int k = 0;
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null)
				return rewards;
			while (i > 0) {
				if (cfg.knockout.size() <= k)
					return rewards;
				SBean.ForceBattleGameCFGS game = cfg.knockout.get(k);
				for (int j = 0; j < i; j ++) {
					int fid1 = war.cupResults.get(base + j*2);
					int fid2 = war.cupResults.get(base + j*2 + 1);
					int winFid = war.cupResults.get(base + i*2 + j);
					SBean.DBForceWarQuiz quiz = war.cupQuizs.get(base + i*2 + j - 32);
					if (winFid == fid1 && fid1 > 0 && fid2 > 0) {
						for (int q : quiz.f1WinRoles)
							rewards.add(new QuizReward(q, game.winMoney, game.winPoints, true));
						for (int q : quiz.f2WinRoles)
							rewards.add(new QuizReward(q, game.lostMoney, game.lostPoints, false));
					}
					else if (winFid == fid2 && fid2 > 0 && fid1 > 0) {
						for (int q : quiz.f2WinRoles)
							rewards.add(new QuizReward(q, game.winMoney, game.winPoints, true));
						for (int q : quiz.f1WinRoles)
							rewards.add(new QuizReward(q, game.lostMoney, game.lostPoints, false));
					}
				}
				base += i * 2;
				i /= 2;
				k ++;
			}
			return rewards;
		}

		private void sendQuizRewards(List<Reward> rewardMails) {
			if (quizRewards == null)
				return;

			int i = 0;
			while (!quizRewards.isEmpty() && i ++ < 1000) {
				QuizReward reward = quizRewards.get(0);
				quizRewards.remove(0);
				int roleId = reward.rid;
				if (roleId < 0)
					continue;
				List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
				if (reward.money > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, reward.money));
				if (reward.point > 0)
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_FORCE_POINT, (short)0, reward.point));
				rewardMails.add(new Reward(roleId, reward.right?1:0, attLst));
			}

			if (quizRewards.isEmpty()) {
				quizRewards = null;
				gs.getLogger().warn("force war send quiz rewards over!");
			}
		}

		private List<List<Integer>> collectThiefRewards()
		{
			List<List<Integer>> rewardList = new ArrayList<List<Integer>>();
			for (List<SortDamage> list : forceSortDamageList.values()) {
				List<Integer> rewards = new ArrayList<Integer>();
				for (SortDamage d : list)
					rewards.add(d.rid);
				rewardList.add(rewards);
			}
			return rewardList;
		}

		private void sendThiefRewards(List<Reward> rewardMails)
		{
			if (thiefRewards == null)
				return;

			List<SBean.ForceThiefRewardCFGS> cfgs = gs.getGameData().getForceBattleCfgs().thiefRewards;
			int i = 0;
			while (!thiefRewards.isEmpty() && i < 1000) {
				List<Integer> rids = thiefRewards.get(0);
				int index = 0;
				for (int rid : rids) {
					int rank = index + 1;
					index ++;
					SBean.ForceThiefRewardCFGS reward = null;
					for(SBean.ForceThiefRewardCFGS e : cfgs)
					{
						if( rank <= e.rankFloor )
						{
							reward = e;
							break;
						}
					}
					if( reward == null )
						continue;
					if (rid < 0)
						continue;
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					attLst.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, reward.itemId, reward.itemCount));
					rewardMails.add(new Reward(rid, rank, attLst));
					i ++;
				}
				thiefRewards.remove(0);
			}

			if (thiefRewards.isEmpty()) {
				thiefRewards = null;
				sortDamageList.clear();
				forceSortDamageList.clear();
				rid2DamageMap.clear();
				thief.bossDamage.clear();
			}
		}

		SBean.DBForceWarForcePlayer getForcePlayer(int fid)
		{
			SBean.DBForceWarForcePlayer dbfp = null;
			for (SBean.DBForceWarForcePlayer e : war.players)
			{
				if (e.id == fid)
				{
					dbfp = e;
					break;
				}
			}
			return dbfp;
		}

		SBean.DBForceWarRolePlayer getForceRolePlayer(int fid, int idx)
		{
			return getForcePlayer(fid).players.get(idx);
		}

		synchronized SBean.DBForceBeast getForceBeast(int fid)
		{
			SBean.DBForceBeast fb = null;
			for (SBean.DBForceBeast b : beast.beasts)
			{
				if (b.fid == fid)
				{
					fb = b;
					break;
				}
			}
			return fb;
		}

		void updateForceBeast(SBean.DBForceBeast be)
		{
			boolean found = false;
			for (int i = 0; i < beast.beasts.size(); i ++)
			{
				SBean.DBForceBeast b = beast.beasts.get(i);
				if (b.fid == be.fid) {
					beast.beasts.set(i, be);
					found = true;
					break;
				}
			}
			if (!found)
				beast.beasts.add(be);
		}

		synchronized boolean quizCup(int rid, int qid, int fid)
		{
			if (qid <= 0 || qid > war.cupQuizs.size() || qid > war.cupResults.size())
			{
				gs.getLogger().debug("quizCup : qid=" + qid + ", cupQuiuzs.size()=" + war.cupQuizs.size() + ", cupResults.size()=" + war.cupResults.size());
				return false;
			}
			int start = 0;
			int end = 0;
			if (qid <= 16)
			{
				start = 0;
				end = 16;
			}
			else if (qid <= 16 + 8)
			{
				start = 16;
				end = 16 + 8;
			}
			else if (qid <= 16 + 8 + 4)
			{
				start = 16 + 8;
				end = 16 + 8 + 4;
			}
			else if (qid <= 16 + 8 + 4 + 2)
			{
				start = 16 + 8 + 4;
				end = 16 + 8 + 4 + 2;
			}
			else if (qid <= 16 + 8 + 4 + 2 +2)
			{
				start = 16 + 8 + 4 + 2;
				end = 16 + 8 + 4 + 2 + 2;
			}
			else
			{
				gs.getLogger().debug("quizCup: qid > " + 16 + 8 + 4 + 2 +2);
				return false;
			}
			for (int i = start; i < end; ++i)
			{
				SBean.DBForceWarQuiz quiz = war.cupQuizs.get(i);
				if (quiz.f1WinRoles.contains(rid))
				{
					gs.getLogger().debug("quizCup: quiz.f1WinRoles contains " + rid);
					return false;
				}
				if (quiz.f2WinRoles.contains(rid))
				{
					gs.getLogger().debug("quizCup: quiz.f2WinRoles contains " + rid);
					return false;
				}
			}
			int fid1 = war.cupResults.get((qid-1)*2);
			int fid2 = war.cupResults.get((qid-1)*2+1);
			SBean.DBForceWarQuiz quiz = war.cupQuizs.get(qid-1);
			if (fid == fid1)
			{
				quiz.f1WinRoles.add(rid);
			}
			else if (fid == fid2)
			{
				quiz.f2WinRoles.add(rid);
			}
			else
			{
				gs.getLogger().debug("quizCup: fid = " + fid + ", fid1="+fid1+", fid2=" + fid2);
				return false;
			}
			return true;
		}

		synchronized boolean openForceWar(int bid, int fid, String fname, short ficon, short flvl, int headId, String headName, int memcnt)
		{
			if (war.bid != bid)
				return false;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime >= cfg.selectionTimeStart)
				return false;
			if (memcnt < cfg.forceMemCntReq)
				return false;
			SBean.DBForceWarForcePlayer fp = getForcePlayer(fid);
			if (fp == null)
			{
				fp = new SBean.DBForceWarForcePlayer();
				fp.id = fid;
				fp.name = fname;
				fp.icon = ficon;
				fp.level = flvl;
				fp.headId = headId;
				fp.headName = headName;
				fp.froles = new ArrayList<Integer>();
				fp.players = new ArrayList<SBean.DBForceWarRolePlayer>();
				fp.plusItems = new ArrayList<Integer>();
				SBean.ForceBattlesCFGS fbscfg = GameData.getInstance().getForceBattleCfgs();
				for (int i = 0; i < fbscfg.itemPlusEffects.size(); ++i)
				{
					fp.plusItems.add(0);
				}
				war.players.add(fp);
			}
			return true;
		}

		synchronized boolean joinForceWar(int bid, int fid, SBean.DBForceWarRolePlayer player, SBean.DBForceBeast beast)
		{
			if (player == null)
				return false;
			if (war.bid != bid)
				return false;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
				return false;
			SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
			if (dbfp == null)
				return false;
			//int index = -1;
			for (int i = 0; i < dbfp.players.size(); ++i)
			{
				SBean.DBForceWarRolePlayer rplayer = dbfp.players.get(i);
				if (rplayer.id == player.id)
				{
					//index = i;
					rplayer.name = player.name;
					rplayer.icon = player.icon;
					rplayer.level = player.level;
					rplayer.power = player.power;
					rplayer.generals = player.generals;
					rplayer.pets = player.pets;
					return true;
				}
			}
			dbfp.players.add(player);
//			if (index >= 0)
//			{
//				dbfp.players.set(index, player);
//			}
//			else
//			{
//				dbfp.players.add(player);
//			}
			if (beast != null)
				updateForceBeast(beast);
			return true;
		}

		synchronized boolean changeRolePlayerSeq(int bid, int fid, int index, int change)
		{
			if (war.bid != bid)
				return false;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
				return false;
			SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
			if (dbfp == null)
				return false;
			if (index < 0 || index >= dbfp.players.size())
				return false;
			if (change > 0 && index + 1 < dbfp.players.size())
			{
				SBean.DBForceWarRolePlayer tmp = dbfp.players.get(index);
				dbfp.players.set(index, dbfp.players.get(index+1));
				dbfp.players.set(index+1, tmp);
			}
			else if (change < 0 && index - 1 >= 0)
			{
				SBean.DBForceWarRolePlayer tmp = dbfp.players.get(index);
				dbfp.players.set(index, dbfp.players.get(index-1));
				dbfp.players.set(index-1, tmp);
			}
			return true;
		}

		boolean contributeForceWar(int bid, int fid, Role role, int cid, int count)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			synchronized (this)
			{
				if (war.bid != bid)
					return false;
				int curTime = gs.getTime();
				SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
				if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
					return false;
				SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
				if (dbfp == null)
					return false;
				SBean.ItemPlusEffectCFGS itemcfg = GameData.getInstance().getForceBattlePluseEffectItemCFG(cid);
				if (itemcfg == null || cid < 0 || cid > dbfp.plusItems.size())
					return false;
				synchronized (role)
				{
					if (role.getItemCount(itemcfg.itemID) < count)
						return false;
					commonFlowRecord.addChange(role.delItem(itemcfg.itemID, count));
					commonFlowRecord.addChange(role.addForce(itemcfg.rewardPointPerItem*count));
				}
				int newCount = dbfp.plusItems.get(cid-1) + count;
				dbfp.plusItems.set(cid-1, newCount);
				for (SBean.DBForceWarRolePlayer rplayer : dbfp.players)
				{
					if (rplayer.id == role.id)
					{
						int roleNewCount = rplayer.plusItems.get(cid-1) +  count;
						rplayer.plusItems.set(cid-1, roleNewCount);
						break;
					}
				}
			}
			commonFlowRecord.setEvent(TLog.AT_FORCE_WAR_CONTRIBUTE);
			commonFlowRecord.setArg(bid, fid, role.id, cid);
			tlogEvent.addCommonFlow(commonFlowRecord);
			gs.getTLogger().logEvent(role, tlogEvent);
			return true;
		}

		synchronized void handleForceMemberQuit(int fid, int rid)
		{
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(war.bid);
			if (cfg == null)
				return;
			int curTime = gs.getTime();
			if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
				return;
			SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
			if (dbfp == null)
				return;
			int index = -1;
			for (int i = 0; i < dbfp.players.size(); ++i)
			{
				SBean.DBForceWarRolePlayer rplayer = dbfp.players.get(i);
				if (rplayer.id == rid)
				{
					index = i;
					break;
				}
			}
			if (index >= 0)
			{
				dbfp.players.remove(index);
			}
			return;
		}

		synchronized void handleForceDisMiss(int fid)
		{
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(war.bid);
			if (cfg == null)
				return;
			int curTime = gs.getTime();
			if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
				return;
			int index = -1;
			for (int i = 0; i < war.players.size(); ++i)
			{
				SBean.DBForceWarForcePlayer player = war.players.get(i);
				if (player.id == fid)
				{
					index = i;
					break;
				}
			}
			if (index >= 0)
			{
				war.players.remove(index);
			}
		}


		synchronized List<RolePlayerInfo> getForcebattlePlayerRoles(int bid, int fid)
		{
			if (war.bid != bid)
				return null;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime > cfg.endTime)
				return null;
			SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
			if (dbfp == null)
				return null;
			List<RolePlayerInfo> lst = new ArrayList<RolePlayerInfo>();
			for (SBean.DBForceWarRolePlayer player : dbfp.players)
			{
				lst.add(RolePlayerInfo.fromDBRolePlayer(player));
			}
			return lst;
		}

		synchronized SBean.DBForceBeast getForcebattlePlayerBeast(int bid, int fid)
		{
			if (war.bid != bid)
				return null;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime > cfg.endTime)
				return null;
			SBean.DBForceBeast dbfb = getForceBeast(fid);
			if (dbfb == null)
				return null;
			return dbfb.kdClone();
		}

		synchronized List<PlusEffectItem> getForceWarContribution(int bid, int fid)
		{
			if (war.bid != bid)
				return null;
			int curTime = gs.getTime();
			SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleByID(bid);
			if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
				return null;
			SBean.DBForceWarForcePlayer dbfp = getForcePlayer(fid);
			if (dbfp == null)
				return null;
			List<PlusEffectItem> items = new ArrayList<PlusEffectItem>();
			for (int i = 0; i < dbfp.plusItems.size(); ++i)
			{
				int c = dbfp.plusItems.get(i);
				items.add(new PlusEffectItem(i+1, c));
			}
			return items;
		}

		void updateForceWarForceInfo()
		{
			gs.getLogger().debug("updateForceWarForceInfo ...");
			for (SBean.DBForceWarForcePlayer fp : war.players)
			{
				final int fid = fp.id;
				gs.getDB().execute(new updateForceWarPlayerTrans(gs, this, fp.id, new updateForceWarPlayerCallback()
				{
					public void onCallback(boolean success, List<Integer> forceMembers)
					{
						if (success)
						{
							final SBean.DBForceWarForcePlayer fp = ForceWar.this.getForcePlayer(fid);
							if (fp != null)
							{
								gs.getLoginManager().getRolesData(SBean.RoleDataReq.eBrief, forceMembers, new LoginManager.GetRolesDataCallback() {
									@Override
									public void onCallback(List<RoleDataRes> res) {
										int lvl = 0;
										int count = 0;
										for (RoleDataRes r : res) {
											if (r.brief != null) {
												lvl += r.brief.lvl;
												count ++;
											}
										}
										if (count > 0) {
											lvl /= count;
											forceLevels.put(fp.id, (short)lvl);
										}
									}
								});
							}
						}
					}
				}));
			}
		}

		void updateForceWarForceMembers()
		{
			gs.getLogger().debug("updateForceWarForceMembers ...");
			for (SBean.DBForceWarForcePlayer fp : war.players)
			{
				final int fid = fp.id;
				gs.getForceManager().getForceMembersRoleID(fid, new ForceManager.QueryForceMembersCallback()
				{
					@Override
					public void onCallback(boolean success, int headID, List<Integer> forceMembers)
					{
						if (success)
						{
							final SBean.DBForceWarForcePlayer fp = ForceWar.this.getForcePlayer(fid);
							if (fp != null)
								fp.froles = forceMembers;
						}
					}
				}, 0);
			}
		}

		class ForceThiefRTStatus
		{
			Map<Integer, Integer> fighting = new HashMap<Integer, Integer>();
			Map<Integer, List<ForceThiefCombat>> combats = new HashMap<Integer, List<ForceThiefCombat>>();
			Map<Integer, SBean.CombatBonus> combatBonus = new HashMap<Integer, SBean.CombatBonus>();
		}

		private SBean.ForceThiefSequenceCFGS getThiefSequenceByTime()
		{
			int minute = gs.getMinuteOfDay();
			List<SBean.ForceThiefSequenceCFGS> sequences = gs.getGameData().getForceBattleCfgs().thiefSequences;

			SBean.ForceThiefSequenceCFGS s = null;
			boolean periodEnd = false;
			for (SBean.ForceThiefSequenceCFGS seq : sequences)
				if (seq.startTime <= minute && seq.startTime + 30 > minute) {
					s = seq;
					if (thief.thiefStartTime < seq.startTime) {
						periodEnd = true;
					}
				}

			int time = gs.getTime();
			SBean.ForceBattleCFGS cfg = gs.getGameData().getForceBattleByID(war.bid);
			if (cfg == null || time < cfg.startTime || time > cfg.selectionTimeStart)
				s = null;

			if (s == null)
				periodEnd = true;

			if (periodEnd) {
				if (thief.thiefStartTime > 0) {
					/*
					for (SBean.DBForceThiefStatus st : thief.thiefStatus) {
						if ((st.killed & (1 << 30)) == 0) {
							// boss not killed
							SBean.DBForceWarForcePlayer player = getForcePlayer(st.fid);
							if (player != null) {
								for (int i = 0; i < player.plusItems.size(); i ++) {
									int item = player.plusItems.get(i);
									item = (int)(item * 0.95);
									player.plusItems.set(i, item);
								}
							}
						}
					}
					*/
					thiefRewards = collectThiefRewards();
				}
				if (s == null)
					thief.thiefStartTime = 0;
				else
					thief.thiefStartTime = s.startTime;
			}

			return s;
		}

		private List<ForceThiefCombat> getThiefCombat(SBean.ForceThiefSequenceCFGS sequence, int fid)
		{
			SBean.DBForceThiefStatus status = thiefStatusMap.get(fid);
			if (status == null)
				return null;

			ForceThiefRTStatus fts = thiefRTStatusMap.get(fid);
			if (fts == null)
				return null;

			Short lvl = forceLevels.get(fid);
			if (lvl == null)
				return null;
				//lvl = 40;

			List<ForceThiefCombat> res = fts.combats.get(fid);
			if (res == null) {
				res = new ArrayList<ForceThiefCombat>();
				byte index = 0;
				for (byte type : sequence.combatTypes) {
					type --;
					List<SBean.ForceThiefCombatCFGS> combats = null;
					if (index == sequence.combatTypes.size() - 1)
						combats = gs.getGameData().getForceBattleCfgs().thiefBossCombats;
					else
						combats = gs.getGameData().getForceBattleCfgs().thiefCombats;
					SBean.ForceThiefCombatCFGS combat = null;
					for (SBean.ForceThiefCombatCFGS c : combats)
						if (c.lvlMin <= lvl)
							combat = c;
					if (combat != null) {
						ForceThiefCombat ftc = new ForceThiefCombat();
						ftc.groupIndex = index;
						ftc.combatId = combat.combatIds.get(type);
						ftc.generalType = type;
						ftc.fighting = false;
						res.add(ftc);
					}
					index ++;
				}
				if (res.size() > 0)
					fts.combats.put(fid, res);
			}

			int index = 0;
			for (ForceThiefCombat ftc : res) {
				ftc.fighting = false;
				Integer time = fts.fighting.get(index);
				int now = gs.getTime();
				if (time != null && time + 60 * 2 > now)
					ftc.fighting = true;
				index ++;
			}

			return res;
		}

		synchronized List<ForceThiefCombat> getCurrentThief(int fid, int[] startTime)
		{
			SBean.ForceThiefSequenceCFGS sequence = getThiefSequenceByTime();
			if (sequence == null)
				return null;

			SBean.DBForceWarForcePlayer player = getForcePlayer(fid);
			if (player == null)
				return null;

			SBean.DBForceThiefStatus s = thiefStatusMap.get(fid);
			if (s == null) {
				s = new SBean.DBForceThiefStatus();
				s.fid = fid;
				s.killed = 0;
				s.startTime = sequence.startTime;
				s.bossStatus = new ArrayList<SBean.DBForceWarGeneralStatus>();
				s.stamp = gs.getTime();
				s.roles = new ArrayList<Integer>();
				s.bossRoles = new ArrayList<Integer>();

				setThiefStatus(s);
			}
			else if (s.startTime != sequence.startTime) {
				s.killed = 0;
				s.startTime = sequence.startTime;
				s.stamp = gs.getTime();
				s.bossStatus = new ArrayList<SBean.DBForceWarGeneralStatus>();
				s.roles = new ArrayList<Integer>();
				s.bossRoles = new ArrayList<Integer>();
			}
			startTime[0] = gs.getTimeByMinuteOffset((short)s.startTime);

			List<ForceThiefCombat> combats = getThiefCombat(sequence, fid);
			if (combats == null)
				return null;

			List<ForceThiefCombat> cs = new ArrayList<ForceThiefCombat>();
			for (int i = 0; i < combats.size(); i ++) {
				if (((1 << i) & s.killed) > 0)
					continue;

				ForceThiefCombat c = combats.get(i);
				if (c == null)
					continue;

				cs.add(c);
				if (cs.size() >= THIEF_STAND_POS)
					break;
			}

			return cs;
		}

		synchronized int startAttackThief(int fid, int rid, int index, List<SBean.DBForceWarGeneralStatus> bossStatus, List<SBean.CombatBonus> bonus)
		{
			SBean.ForceThiefSequenceCFGS sequence = getThiefSequenceByTime();
			if (sequence == null)
				return -1;

			SBean.DBForceThiefStatus s = thiefStatusMap.get(fid);
			if (s == null)
				return -2;

			ForceThiefRTStatus rts = thiefRTStatusMap.get(fid);
			if (rts == null)
				return -3;

			final int now = gs.getTime();
			Integer time = rts.fighting.get(index);
			if (time != null) {
				if (time + 60 * 2 > now) {
					return -4;
				}
				else
					rts.fighting.remove(index);
			}

			List<ForceThiefCombat> combats = getThiefCombat(sequence, fid);
			if (combats == null || index >= combats.size())
				return -5;

			if (((1 << index) & s.killed) > 0)
				return -6;

			if (index == combats.size()-1) {
				for (int id : s.bossRoles)
					if (id == rid)
						return -7;
			}
			else {
				for (int id : s.roles)
					if (id == rid)
						return -8;
			}

			ForceThiefCombat combat = combats.get(index);
			SBean.CombatCFGS cfg = gs.gameData.getCombatCFG(combat.combatId);
			if (cfg == null)
				return -9;

			GameData.BonusInfo bonusInfo = gs.gameData.getCombatBonus(cfg, null, 1.0f, (byte)1, (byte)1, (byte)1, (byte)1, null, null, null, (byte)0);
			rts.combatBonus.put(rid, bonusInfo.cb);
			if (bonusInfo.cb != null)
				bonus.add(bonusInfo.cb);

			rts.fighting.put(index, now);
			s.stamp = gs.getTime();

			bossStatus.clear();
			if (index == combats.size() - 1) {
				// boss
				for (SBean.DBForceWarGeneralStatus status : s.bossStatus)
					bossStatus.add(status);
			}

			return 0;
		}

		synchronized int finishAttackThief(int fid, Role role, int index, boolean win, int damage, List<SBean.DBForceWarGeneralStatus> bossStatus, List<SBean.CombatBonus> bonus)
		{
			SBean.ForceThiefSequenceCFGS sequence = getThiefSequenceByTime();
			if (sequence == null)
				return -1;

			SBean.DBForceThiefStatus s = thiefStatusMap.get(fid);
			if (s == null)
				return -2;

			ForceThiefRTStatus rts = thiefRTStatusMap.get(fid);
			if (rts == null)
				return -3;

			if (!rts.fighting.containsKey(index))
				return -4;

			List<ForceThiefCombat> combats = getThiefCombat(sequence, fid);
			if (combats == null)
				return -5;

			if (index == combats.size()-1) {
				for (int id : s.bossRoles)
					if (id == role.id)
						return -6;
			}
			else {
				for (int id : s.roles)
					if (id == role.id)
						return -7;
			}

			rts.fighting.remove(index);
			if (win) {
				s.killed |= (1 << index);
				if (index == combats.size() - 1) {
					s.bossRoles.add(role.id);
					s.killed |= (1 << 30);
				}
				else
					s.roles.add(role.id);
			}
			else {
				if (index == combats.size() - 1) {
					// boss
					Map<Short, SBean.DBForceWarGeneralStatus> statusMap = new HashMap<Short, SBean.DBForceWarGeneralStatus>();
					for (SBean.DBForceWarGeneralStatus status : s.bossStatus)
						statusMap.put(status.id, status);
					for (SBean.DBForceWarGeneralStatus status : bossStatus)
						statusMap.put(status.id, status);
					s.bossStatus.clear();
					for (SBean.DBForceWarGeneralStatus status : statusMap.values())
						s.bossStatus.add(status);
					s.bossRoles.add(role.id);
				}
				else
					s.roles.add(role.id);
			}
			s.stamp = gs.getTime();

			SBean.CombatBonus bo = rts.combatBonus.get(role.id);
			if (bo != null) {
				bonus.add(bo);
			}
			rts.combatBonus.remove(role.id);

			return 0;
		}

		private void initThiefStatus(SBean.DBForceThiefStatus status) {
			thiefStatusMap.put(status.fid, status);
			thiefRTStatusMap.put(status.fid, new ForceThiefRTStatus());
		}

		private void setThiefStatus(SBean.DBForceThiefStatus status) {
			SBean.DBForceThiefStatus s = thiefStatusMap.get(status.fid);
			if (s == null) {
				s = status.kdClone();
				thief.thiefStatus.add(status);
			}
			else {
				SBean.DBForceThiefStatus st = status.kdClone();
				s.killed = st.killed;
				s.bossStatus = st.bossStatus;
				s.roles = st.roles;
				s.bossRoles = st.bossRoles;
			}

			thiefStatusMap.put(status.fid, s);

			ForceThiefRTStatus rts = thiefRTStatusMap.get(status.fid);
			if (rts == null) {
				rts = new ForceThiefRTStatus();
				thiefRTStatusMap.put(status.fid, rts);
			}
		}

		synchronized void setDamage(int index, int damage, int fid, int id, String name, short icon, short lvl, List<SBean.DBForceThiefGeneralBrief> generals)
		{
			SBean.ForceThiefSequenceCFGS sequence = getThiefSequenceByTime();
			if (sequence == null)
				return;

			List<ForceThiefCombat> combats = getThiefCombat(sequence, fid);
			if (combats == null || index < combats.size() - 1)
				return;

			List<SortDamage> fSortDamage = forceSortDamageList.get(fid);
			if (fSortDamage == null) {
				fSortDamage = new ArrayList<SortDamage>();
				forceSortDamageList.put(fid, fSortDamage);
			}
			removeDamageSortList(id, fSortDamage);
			insertDamageSortList(damage, id, fSortDamage);

			removeDamageSortList(id, sortDamageList);
			insertDamageSortList(damage, id, sortDamageList);

			SBean.DBForceThiefBossDamage d = rid2DamageMap.get(id);
			if (d == null) {
				d = new SBean.DBForceThiefBossDamage(fid, id, damage, name, icon, lvl, generals, 0, 0);
				rid2DamageMap.put(id, d);
				thief.bossDamage.add(d);
			}
			else {
				d.damage = damage;
				d.name = name;
				d.icon = icon;
				d.lvl = lvl;
				d.generals = generals;
			}
		}

		private void insertDamageSortList(int damage, int id, List<SortDamage> sortDamageList)
		{
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

		private void removeDamageSortList(int id, List<SortDamage> sortDamageList)
		{
			SBean.DBForceThiefBossDamage d = rid2DamageMap.get(id);
			if (d == null)
				return;

			int index = getDamageSortList(d.damage, id, sortDamageList);
			if (index >= 0)
				sortDamageList.remove(index);
		}

		private int getDamageSortList(int damage, int id, List<SortDamage> sortDamageList)
		{
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

		synchronized List<DamageRank> getDamageRanks(int fid, int rid)
		{
			List<SortDamage> sortDamageList = forceSortDamageList.get(fid);
			if (sortDamageList == null)
				sortDamageList = this.sortDamageList;

			List<DamageRank> ranks = new ArrayList<DamageRank>();
			SBean.DBForceThiefBossDamage d = rid2DamageMap.get(rid);
			int index = -1;
			if (d != null) {
				index = getDamageSortList(d.damage, rid, sortDamageList);
			}

			for (byte i = 0; i < 32; i ++) {
				if (i >= sortDamageList.size())
					break;
				SortDamage damage = sortDamageList.get(i);
				SBean.DBForceThiefBossDamage rrd = rid2DamageMap.get(damage.rid);
				if (rrd == null)
					continue;
				List<SBean.DBForceThiefGeneralBrief> generals = new ArrayList<SBean.DBForceThiefGeneralBrief>();
				for (SBean.DBForceThiefGeneralBrief g : rrd.generals)
					generals.add(g.kdClone());
				DamageRank rank = new DamageRank(i+1, rrd.rid, rrd.damage, rrd.icon, rrd.name, rrd.lvl, generals);
				ranks.add(rank);
			}
			if (index >= 32) {
				if (d != null) {
					List<SBean.DBForceThiefGeneralBrief> generals = new ArrayList<SBean.DBForceThiefGeneralBrief>();
					for (SBean.DBForceThiefGeneralBrief g : d.generals)
						generals.add(g.kdClone());
					DamageRank rank = new DamageRank(index+1, d.rid, d.damage, d.icon, d.name, d.lvl, generals);
					ranks.add(rank);
				}
			}
			return ranks;
		}

		synchronized boolean thiefStampNeedRefresh(int fid, int stamp, int[] newStamp)
		{
			SBean.DBForceThiefStatus s = thiefStatusMap.get(fid);
			if (s == null) {
				int[] startTime = {0};
				getCurrentThief(fid, startTime);
				s = thiefStatusMap.get(fid);
				if (s == null)
					return false;
			}
			else {
				int minute = gs.getMinuteOfDay();
				if (s.startTime > minute || s.startTime + 30 <= minute) {
					int[] startTime = {0};
					if (getCurrentThief(fid, startTime) == null)
						s.stamp = 0;
				}
			}

			newStamp[0] = s.stamp;
			return s.stamp != stamp;
		}

		GameServer gs;
		SBean.DBForceWar war;
		SBean.DBForceThief thief;
		SBean.DBForceWarBeast beast;
		Map<Integer, ForceWarStatus> warStatusMap;
		Map<Integer, RuntimeForceWarStatus> warRTStatusMap;
		Map<Integer, ForceWarRoleScore> warRoleScoreMap;
		Map<Integer, ForceWarCupWin> warCupWinMap;
		Map<Integer, Map<Integer, ForceWarRecord>> warLeagueRecordMap;
		Map<Integer, Map<Integer, ForceWarRecord>> warCupRecordMap;
		//List<Integer> leagueSequence;
		List<List<SortScore>> sortLists;
		List<PowerRank> powerRanks;
		Map<Integer, List<PowerRank>> forcePowerRanks;
		List<RoleScoreRank> roleScoreRanks;
		Map<Integer, List<RoleScoreRank>> forceRoleScoreRanks;
		List<DonateRank> donateRanks;
		Map<Integer, List<DonateRank>> forceDonateRanks;
		List<CupWinRank> cupWinRanks;
		Map<Integer, List<CupWinRank>> forceCupWinRanks;
		List<QuizReward> quizRewards;
		int lastStatusTime = 0;
		int progLeague = 0;
		Map<Integer, Integer> powers;
		Map<Integer, SBean.DBForceThiefStatus> thiefStatusMap;
		Map<Integer, ForceThiefRTStatus> thiefRTStatusMap;
		List<SortDamage> sortDamageList;
		Map<Integer, List<SortDamage>> forceSortDamageList;
		Map<Integer, SBean.DBForceThiefBossDamage> rid2DamageMap = new HashMap<Integer, SBean.DBForceThiefBossDamage>();
		Map<Integer, Short> forceLevels = new HashMap<Integer, Short>();
		List<List<Integer>> thiefRewards;
	}

	public class SaveTrans implements Transaction
	{
		SaveTrans(SBean.DBForceWar war, SBean.DBForceThief thief, SBean.DBForceWarBeast beast)
		{
			this.war = war;
			this.thief = thief;
			this.beast = beast;
		}

		@Override
		public boolean doTransaction()
		{
			byte[] key = Stream.encodeStringLE("forceWar2");
			byte[] data = Stream.encodeLE(war);
			world.put(key, data);
			byte[] key2 = Stream.encodeStringLE("forceThief");
			byte[] data2 = Stream.encodeLE(thief);
			world.put(key2, data2);
			byte[] key3 = Stream.encodeStringLE("forceBeast2");
			byte[] data3 = Stream.encodeLE(beast);
			world.put(key3, data3);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("force war save failed");
			}
		}

		@AutoInit
		public Table<byte[], byte[]> world;

		SBean.DBForceWar war;
		SBean.DBForceThief thief;
		SBean.DBForceWarBeast beast;
	}

	public ForceWarManager(GameServer gs)
	{
		this.gs = gs;
	}

	public void init(SBean.DBForceWar war, SBean.DBForceThief thief, SBean.DBForceWarBeast beast)
	{
		this.war = new ForceWar(gs, war, thief, beast);
		getCurForceWar();
	}

	public void save()
	{
		gs.getDB().execute(new SaveTrans(war.getDBWarData(), war.getDBThiefData(), war.getDBBeastData()));
	}

	public void onTimer()
	{
		int timeTick = gs.getTime();
		if ( timeTick % 600 == 100)
		{
			getCurForceWar();
		}
		boolean save = war.onTimer();
		if (save)
			save();
	}

	public void setCalcResult(int fid1, int fid2, byte rIndex1, byte rIndex2, byte result,
			List<SBean.DBForceWarGeneralStatus> status1, List<SBean.DBForceWarGeneralStatus> status2)
	{
		war.setCalcResult(fid1, fid2, rIndex1, rIndex2, result, status1, status2);
	}

	public List<WarData> getLeagueData(int fid)
	{
		return war.getLeagueData(fid);
	}

	public List<WarData> getCupData(int rid)
	{
		return war.getCupData(rid);
	}

	SBean.DBForceWarMatchRecord getRecord(boolean league, int fid1, int fid2, int index1, int index2, int selffid, boolean[] reverse)
	{
		SBean.DBForceWarRecord record = war.getWarRecord(league, fid1, fid2);
		if (record != null) {
			for (SBean.DBForceWarMatchRecordBrief m : record.matches)
				if ((record.fid1 == fid1 && m.index1 == index1 && record.fid2 == fid2 && m.index2 == index2) ||
						(record.fid2 == fid1 && m.index2 == index1 && record.fid1 == fid2 && m.index1 == index2)) {
					if (record.fid2 == selffid)
					{
						reverse[0] = true;
					}
					else
					{
						reverse[0] = false;
					}
					//
					SBean.DBForceWarForcePlayer f1 = war.getForcePlayer(record.fid1);
					SBean.DBForceWarForcePlayer f2 = war.getForcePlayer(record.fid2);
					SBean.DBForceWarRolePlayer p1 = null;
					SBean.DBForceWarRolePlayer p2 = null;
					SBean.DBForceBeast b1 = null;
					SBean.DBForceBeast b2 = null;

					if (m.index1 < f1.players.size()) {
						p1 = f1.players.get(m.index1);
					}
					else {
						// beast
						b1 = war.getForceBeast(record.fid1);
					}
					if (m.index2 < f2.players.size()) {
						p2 = f2.players.get(m.index2);
					}
					else {
						// beast
						b2 = war.getForceBeast(record.fid2);
					}

					List<DBRoleGeneral> generals1 = null;
					List<DBRoleGeneral> generals2 = null;
					List<DBPetBrief> pets1 = null;
					List<DBPetBrief> pets2 = null;
					List<SBean.DBRelation> relation1 = null;
					List<SBean.DBRelation> relation2 = null;
					//List<SBean.DBGeneralStone> gStone1 = null;
					//List<SBean.DBGeneralStone> gStone2 = null;
					String name1 = null;
					String name2 = null;

					if (p1 != null) {
						generals1 = p1.generals;
						pets1 = p1.pets;
						name1 = p1.name;
						relation1 = p1.relation;
//						gStone1 = p1.gStones;
					}
					else if (b1 != null) {
						generals1 = new ArrayList<DBRoleGeneral>();
						DBRoleGeneral role = new DBRoleGeneral(b1.general.id, b1.general.lvl, b1.general.exp, (byte)100,
								b1.general.evoLvl, new SBean.DBWeapon(b1.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
								new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)b1.general.headicon,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
						for (short skill : b1.general.skills)
							role.skills.add(skill);
						for (short prop : b1.props)
							role.skills.add(prop);
						role.skills.add((short)b1.attackProp);
						role.skills.add((short)b1.mainProp);
						generals1.add(role);
						pets1 = new ArrayList<DBPetBrief>();
						name1 = f1.name + " ";
						boolean found = false;
						if (b1 != null) {
							SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(b1.general.id);
							if (gcfg != null) {
								found = true;
								name1 += gcfg.name;
							}
						}
						if (!found)
							name1 += "Beast";
					}

					if (p2 != null) {
						generals2 = p2.generals;
						pets2 = p2.pets;
						name2 = p2.name;
						relation2 = p2.relation;
//						gStone2 = p2.gStones;
					}
					else if (b2 != null) {
						generals2 = new ArrayList<DBRoleGeneral>();
						DBRoleGeneral role = new DBRoleGeneral(b2.general.id, b2.general.lvl, b2.general.exp, (byte)100,
								b2.general.evoLvl, new SBean.DBWeapon(b2.general.id, (short)-1, (byte)0, new ArrayList<SBean.DBWeaponProp>(), new ArrayList<SBean.DBWeaponProp>()),
								new ArrayList<Short>(), new ArrayList<SBean.DBGeneralEquip>(),new ArrayList<SBean.DBGeneralSeyen>(), new ArrayList<SBean.DBGeneralOfficial>(),(short)0,new ArrayList<SBean.DBGeneralBestow>(),new ArrayList<SBean.DBGeneralBless>());
						for (short skill : b2.general.skills)
							role.skills.add(skill);
						for (short prop : b2.props)
							role.skills.add(prop);
						role.skills.add((short)b2.attackProp);
						role.skills.add((short)b2.mainProp);
						generals2.add(role);
						pets2 = new ArrayList<DBPetBrief>();
						name2 = f2.name + " ";
						boolean found = false;
						if (b2 != null) {
							SBean.GeneralCFGS gcfg = gs.getGameData().getGeneralCFG(b2.general.id);
							if (gcfg != null) {
								found = true;
								name2 += gcfg.name;
							}
						}
						if (!found)
							name2 += "Beast";
					}

					if (generals1 == null || generals2 == null || pets1 == null || pets2 == null || name1 == null || name2 == null)
						return null;

					SBean.DBForceWarMatchRecord r = new SBean.DBForceWarMatchRecord();
					//
					r.index1 = m.index1;
					r.index2 = m.index2;
					r.status1 = m.status1;
					r.status2 = m.status2;
					r.eff1 = new ArrayList<Byte>();
					r.eff2 = new ArrayList<Byte>();

					byte[] eff1 = war.getPlusEffect(f1);
					for (int k = 0; k < 3; k ++)
						r.eff1.add(eff1[k]);
					byte[] eff2 = war.getPlusEffect(f2);
					for (int k = 0; k < 3; k ++)
						r.eff2.add(eff2[k]);

					r.generals1 = generals1;
					r.generals2 = generals2;
					r.pets1 = pets1;
					r.pets2 = pets2;
					r.relation1 = relation1;
					r.relation2 = relation2;
					r.name1 = name1;
					r.name2 = name2;
					r.result = m.result;
					r.seed = m.seed;
					//
					return r;
				}
		}
		return null;
	}

	public List<ScoreRank> getScoreRanks(int fid)
	{
		return war.getScoreRanks(fid);
	}

	public List<PowerRank> getPowerRanks(int fid)
	{
		return war.getPowerRanks(fid);
	}

	public List<RoleScoreRank> getRoleScoreRanks(int fid)
	{
		return war.getRoleScoreRanks(fid);
	}

	public List<DonateRank> getDonateRanks(int fid)
	{
		return war.getDonateRanks(fid);
	}

	public List<CupWinRank> getCupWinRanks(int fid)
	{
		return war.getCupWinRanks(fid);
	}

	boolean quizCup(int bid, Role role, int qid, int fid)
	{
		int curTime = gs.getTime();
		SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(curTime);
		if (cfg.id != bid)
		{
			gs.getLogger().debug("quizCup bid="+bid + ", cfg.id="+cfg.id);
			return false;
		}
		if (curTime < cfg.knockoutTimeStart || curTime > cfg.endTime)
		{
			gs.getLogger().debug("quizCup curTime="+curTime + ", knockoutTimeStart="+cfg.knockoutTimeStart+ ", endTime="+cfg.endTime);
			return false;
		}
		boolean ret = war.quizCup(role.id, qid, fid);
		if (ret)
		{
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.ForceEventRecord record = new TLogger.ForceEventRecord(fid, TLog.FORCE_WAR_QUIZ);
			record.setArgs(fid, 0);
			tlogEvent.addRecord(record);
			gs.getTLogger().logEvent(role, tlogEvent);
		}
		return ret;
	}

	public interface updateForceWarPlayerCallback
	{
		public void onCallback(boolean success, List<Integer> forceMembers);
	}
	public static class updateForceWarPlayerTrans implements Transaction
	{
		public updateForceWarPlayerTrans(final GameServer gs, final ForceWar fw, final int fid, updateForceWarPlayerCallback callback)
		{
			this.gs = gs;
			this.fw = fw;
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			//int curTime = gs.getTime();
			synchronized (fw)
			{
				SBean.DBForceWarForcePlayer fp = fw.getForcePlayer(fid);
				if (fp != null)
				{
					for(SBean.DBForceWarRolePlayer player : fp.players)
					{
						//閸欘亝娲块弬鏉挎躬缁捐法甯虹�锟�						
						Role rolePlayer = gs.getLoginManager().getRole(player.id);
						if (rolePlayer != null)
						{
							updateRolePlayer(rolePlayer, player);
						}
//						else
//						{
//							DBRole dbrole = role.get(player.id);
//							if( dbrole != null )
//							{
//								updateRolePlayer(dbrole, player);
//							}
//						}
					}
				}
				SBean.DBForceBeast beast = null;
				DBForce dbforce = force.get(fid);
				if (dbforce != null && dbforce.beasts.size() > 0) {
					for (DBForce.Member m : dbforce.members)
						members.add(m.id);
					beast = dbforce.beasts.get(dbforce.beasts.size()-1);
					fw.updateForceBeast(beast);
				}
				gs.getLogger().debug("updateForceWarPlayer fid=" + fid);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, members);
		}

		@AutoInit
		public TableReadonly<Integer, DBRole> role;
		@AutoInit
		public TableReadonly<Integer, DBForce> force;

		public GameServer gs;
		public ForceWar fw;
		public int fid;
		public final updateForceWarPlayerCallback callback;
		public List<Integer> members = new ArrayList<Integer>();
	}

	public static class UpdateForceWarBeastTrans implements Transaction
	{
		public UpdateForceWarBeastTrans(final GameServer gs, final ForceWar fw, final int fid)
		{
			this.gs = gs;
			this.fw = fw;
			this.fid = fid;
		}

		@Override
		public boolean doTransaction()
		{
			synchronized (fw)
			{
				SBean.DBForceBeast beast = null;
				DBForce dbforce = force.get(fid);
				if (dbforce != null) {
					if (dbforce.beasts.size() > 0) {
						beast = dbforce.beasts.get(dbforce.beasts.size()-1);
						fw.updateForceBeast(beast);
					}
					else
						return false;
				}
				else {
					SBean.DBForceBeast b = createDefaultBeast(gs, fid);
					fw.updateForceBeast(b);
				}
				gs.getLogger().debug("UpdateForceWarBeastTrans fid=" + fid);
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (errcode != ErrorCode.eOK)
				gs.getDB().execute(new GetBeastsTrans(gs, fid, null));
		}

		@AutoInit
		public TableReadonly<Integer, DBForce> force;

		public GameServer gs;
		public ForceWar fw;
		public int fid;
	}

	public static void updateRolePlayer(Role role, SBean.DBForceWarRolePlayer player)
	{
		player.name = role.name;
		player.icon = role.headIconID;
		player.level = role.lvl;
		player.power = 0;
		List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();

		for (DBRoleGeneral og : player.generals)
		{
			DBRoleGeneral g = role.generals.get(og.id);
			g.generalStone=role.getGeneralStones(og.id);
			generals.add(g.kdClone());
			player.power += role.calcGeneralPower(g.id);
		}
		player.generals = generals;
		List<SBean.DBPetBrief> pets = new ArrayList<SBean.DBPetBrief>();
		for (SBean.DBPetBrief op : player.pets)
		{
			SBean.DBPet p = role.pets.get(Short.valueOf(op.id));
			byte deformStage = 0;
			SBean.DBPetDeform deform = role.petDeforms.get(op.id);
			if (deform != null)
				deformStage = deform.deformStage;
			pets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
			player.power += role.calcPetPower(p.id);
		}
		player.pets = pets;
		player.relation = role.getActiveRelations();
	}

	public static void updateRolePlayer(DBRole role, SBean.DBForceWarRolePlayer player)
	{
		player.name = role.name;
		player.icon = role.headIconID;
		player.level = role.lvl;
		player.power = 0;
		List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
		for (DBRoleGeneral og : player.generals)
		{
			DBRoleGeneral g = role.getGeneral(og.id);
			g.generalStone = role.getActiveGeneralStones(og.id);
			generals.add(g.kdClone());
			player.power += role.calcGeneralPower(g.id);
		}
		player.generals = generals;
		List<SBean.DBPetBrief> pets = new ArrayList<SBean.DBPetBrief>();
		for (SBean.DBPetBrief op : player.pets)
		{
			SBean.DBPet p = role.getPet(op.id);
			byte deformStage = 0;
			for (SBean.DBPetDeform d : role.petDeforms)
				if (d.id == op.id)
					deformStage = d.deformStage;
			pets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
			player.power += role.calcPetPower(p.id);
		}
		player.pets = pets;
		player.relation = role.getActiveRelations();
	}

	ForceWar syncForceWar(SBean.ForceBattleCFGS cfg)
	{
		if (cfg == null)
			return null;
		synchronized (this)
		{
			if (war == null || war.war.bid !=  cfg.id)
			{
				war = new ForceWar(gs, null, null, null);
				war.war.bid = cfg.id;
				war.war.stage = 1;
			}
		}
		return war;
	}

	public ForceWar getCurForceWar()
	{
		SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(gs.getTime());
		return syncForceWar(cfg);
	}

	public boolean openForceWar(int bid, int fid, String fname, short ficon, short flvl, int headId, String headName, int memcnt)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return false;
		return war.openForceWar(bid, fid, fname, ficon, flvl, headId, headName, memcnt);
	}

	public boolean joinForceWar(int bid, int fid, Role role, List<Short> gids, List<Short> pids, SBean.DBForceBeast beast)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return false;
		SBean.DBForceWarRolePlayer player = checkRolePlayerGeneralsPets(role, gids, pids);
		return war.joinForceWar(bid, fid, player, beast);
	}

	public boolean changeRolePlayerSeq(int bid, int fid, int index, int change)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return false;
		return war.changeRolePlayerSeq(bid, fid, index, change);
	}

	public boolean contributeForceWar(int bid, int fid, Role role, int cid, int count)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return false;
		return war.contributeForceWar(bid, fid, role, cid, count);
	}

	public void handleForceMemberQuit(int fid, int rid)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return;
		war.handleForceMemberQuit(fid, rid);
	}

	public void handleForceDisMiss(int fid)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return;
		war.handleForceDisMiss(fid);
	}

	public List<RolePlayerInfo> getForcebattlePlayerRoles(int bid, int fid)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return null;
		return war.getForcebattlePlayerRoles(bid, fid);
	}

	public SBean.DBForceBeast getForcebattlePlayerBeast(int bid, int fid)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return null;
		return war.getForcebattlePlayerBeast(bid, fid);
	}

	public int getVerifyFlag()
	{
		return gs.getConfig().forceWarVerifyFlag;
	}

	public List<PlusEffectItem> getForceWarContribution(int bid, int fid)
	{
		ForceWar war = getCurForceWar();
		if (war == null)
			return null;
		return war.getForceWarContribution(bid, fid);
	}

	public static class RolePlayerInfo
	{
		public int id;
		public String name = "";
		public short icon;
		public int level;
		public int power;
		public List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
		public List<DBPetBrief> pets = new ArrayList<DBPetBrief>();

		public RolePlayerInfo(int id)
		{
			this.id = id;
		}

		public static RolePlayerInfo fromDBRolePlayer(SBean.DBForceWarRolePlayer dbplayer)
		{
			RolePlayerInfo player = new RolePlayerInfo(dbplayer.id);
			player.name = dbplayer.name;
			player.icon = dbplayer.icon;
			player.level = dbplayer.level;
			player.power = dbplayer.power;
			player.generals.addAll(dbplayer.generals);
			player.pets.addAll(dbplayer.pets);
			return player;
		}
	}

	public static class PlusEffectItem
	{
		public int id;
		public int count;
		public PlusEffectItem(int id, int count)
		{
			this.id = id;
			this.count = count;
		}
	}

	public static SBean.DBForceWarRolePlayer checkRolePlayerGeneralsPets(Role role, List<Short> gids, List<Short> pids)
	{
		if (gids == null || pids == null)
			return null;
		if (gids.isEmpty() || gids.size() > 5 || pids.size() > 1)
			return null;
		Set<Short> checkSet = new TreeSet<Short>(gids);
		if (checkSet.size() != gids.size())
			return null;
		SBean.DBForceWarRolePlayer player = null;
		synchronized (role)
		{
			for (short gid : gids)
			{
				if (!role.generals.containsKey(gid))
					return null;
			}
			for (short pid : pids)
			{
				if (!role.pets.containsKey(pid))
					return null;
			}

			player = new SBean.DBForceWarRolePlayer(role.id, role.name, role.headIconID, role.lvl, 0, new ArrayList<DBRoleGeneral>(), new ArrayList<SBean.DBPetBrief>(), new ArrayList<SBean.DBRelation>(), new ArrayList<Integer>());

			for (short gid : gids)
			{
				DBRoleGeneral g = role.generals.get(gid);
				g.generalStone=role.getGeneralStones(gid);
				player.generals.add(g.kdClone());
				player.power += role.calcGeneralPower(gid);
			}
			for (short pid : pids)
			{
				SBean.DBPet p = role.pets.get(pid);
				byte deformStage = 0;
				SBean.DBPetDeform deform = role.petDeforms.get(pid);
				if (deform != null)
					deformStage = deform.deformStage;
				player.pets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage, p.name));
				player.power += role.calcPetPower(pid);
			}
			SBean.ForceBattlesCFGS fbscfg = GameData.getInstance().getForceBattleCfgs();
			for (int i = 0; i < fbscfg.itemPlusEffects.size(); ++i)
			{
				player.plusItems.add(0);
			}
			player.relation = role.getActiveRelationsWithoutLock();
		}
		return player;
	}

	public List<Short> getCurForceBattleExtraDropTblID(Role role, boolean heroBattle, short combatId)
	{
		int curTime = gs.getTime();
		SBean.ForceBattleCFGS cfg = GameData.getInstance().getForceBattleNow(curTime);
		if (cfg == null)
			return null;
		if (curTime < cfg.startTime || curTime > cfg.selectionTimeStart)
			return null;
		if (role.lvl < cfg.playerLvlReq)
			return null;
		ForceWar fw = syncForceWar(cfg);
		if (fw == null)
			return null;
		SBean.DBForceWarForcePlayer dbfp = fw.getForcePlayer(role.forceInfo.id);
		if (dbfp == null)
			return null;
		List<Short> lst = new ArrayList<Short>();
		lst.add(heroBattle ? cfg.heroDropTblID : cfg.normalDropTblID);
		for (SBean.CombatExtraDropCFGS e : cfg.combatsDropTblIDs)
		{
			if (e.combatID == combatId)
			{
				lst.add(e.dropTblID);
			}
		}
		return lst;
	}

	public List<Short> getForceMemberExtraDropTblID(Role role, boolean heroBattle, short combatId)
	{
		if (role.getForceID() <= 0)
			return null;
		SBean.ForceBattlesCFGS cfg = GameData.getInstance().getForceBattleCfgs();
		if (cfg == null)
			return null;
		List<Short> lst = new ArrayList<Short>();
		lst.add(heroBattle ? cfg.heroDropTblID : cfg.normalDropTblID);
		for (SBean.CombatExtraDropCFGS e : cfg.combatsDropTblIDs)
		{
			if (e.combatID == combatId)
			{
				lst.add(e.dropTblID);
			}
		}
		return lst;
	}

	public boolean thiefStampNeedRefresh(int fid, int stamp, int[] newStamp)
	{
		return war.thiefStampNeedRefresh(fid, stamp, newStamp);
	}

	public List<ForceThiefCombat> getCurrentThief(int fid, int[] startTime)
	{
		return war.getCurrentThief(fid, startTime);
	}

	public int startAttackThief(int fid, int rid, int index, List<SBean.DBForceWarGeneralStatus> bossStatus, List<SBean.CombatBonus> bonus)
	{
		return war.startAttackThief(fid, rid, index, bossStatus, bonus);
	}

	public int finishAttackThief(int fid, Role role, int index, boolean win, int damage, List<SBean.DBForceWarGeneralStatus> bossStatus, List<SBean.CombatBonus> bonus)
	{
		return war.finishAttackThief(fid, role, index, win, damage, bossStatus, bonus);
	}

	public void setDamage(int index, int damage, int fid, int id, String name, short icon, short lvl, List<DBRoleGeneral> generals)
	{
		List<SBean.DBForceThiefGeneralBrief> briefs = new ArrayList<SBean.DBForceThiefGeneralBrief>();
		for (DBRoleGeneral g : generals)
			briefs.add(new SBean.DBForceThiefGeneralBrief(g.id, g.lvl, g.advLvl, g.evoLvl, 0, 0));

		war.setDamage(index, damage, fid, id, name, icon, lvl, briefs);
	}

	public List<DamageRank> getDamageRanks(int fid, int rid)
	{
		return war.getDamageRanks(fid, rid);
	}

	public interface GetBeastsCallback
	{
		public void onCallback(boolean success, List<SBean.DBForceBeast> beasts);
	}

	private static SBean.DBForceBeast createDefaultBeast(GameServer gs, int fid)
	{
		SBean.ForceBeastEvoCFGS cfg = gs.getGameData().getForceBattleCfgs().beastEvo.get(0);
		SBean.DBForceBeast b = new SBean.DBForceBeast();
		b.fid = fid;
		b.general = new DBRoleGeneral();
		b.general.id = cfg.gid;
		b.general.lvl = 1;
		b.general.exp = 0;
		b.general.evoLvl = 1;
		b.general.equips = new ArrayList<SBean.DBGeneralEquip>();
		b.general.skills = new ArrayList<Short>();
		b.general.weapon = new SBean.DBWeapon();
		b.general.weapon.passes = new ArrayList<SBean.DBWeaponProp>();
		b.general.weapon.resetPasses = new ArrayList<SBean.DBWeaponProp>();
		b.attackProp = (byte)gs.getGameData().getRandInt(1, 6);
		b.mainProp = -1;
		b.props = new ArrayList<Short>();
		for (int i = 0; i < 5; i ++) {
			b.general.skills.add((short)0);
			b.props.add((short)0);
		}
		b.general.skills.set(0, (short)1);
		return b;
	}

	public static class GetBeastsTrans implements Transaction
	{
		public GetBeastsTrans(GameServer gs, int fid, GetBeastsCallback callback)
		{
			this.gs = gs;
			this.fid = fid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (dbforce.beasts.isEmpty()) {
					SBean.DBForceBeast b = createDefaultBeast(gs, fid);
					dbforce.beasts.add(b);
					force.put(fid, dbforce);
				}

				for (SBean.DBForceBeast b : dbforce.beasts)
					beasts.add(b);

				return true;
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if (callback != null)
				callback.onCallback(errcode == ErrorCode.eOK, beasts);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public GameServer gs;
		public final int fid;
		List<SBean.DBForceBeast> beasts = new ArrayList<SBean.DBForceBeast>();
		public final GetBeastsCallback callback;
	}

	public void beastSync(final int sid, final int fid)
	{
		gs.getDB().execute(new GetBeastsTrans(gs, fid, new GetBeastsCallback() {
			@Override
			public void onCallback(boolean success, List<DBForceBeast> beasts) {
				if (beasts.size() > 0) {
					synchronized (war) {
						war.updateForceBeast(beasts.get(beasts.size()-1));
					}
				}
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastSyncRes(fid, beasts));
			}
		}));
	}

	public void beastGet(int fid)
	{
		gs.getDB().execute(new GetBeastsTrans(gs, fid, null));
	}

	public interface GeneralCallback
	{
		public void onCallback(boolean success);
	}

	public interface GeneralReturnValueCallback
	{
		public void onCallback(boolean success, int value);
	}

	public interface GeneralReturnValuesCallback
	{
		public void onCallback(boolean success, int[] values);
	}

	public class ResetBeastAttackPropTrans implements Transaction
	{
		public ResetBeastAttackPropTrans(Role role, int fid, short gid, GeneralReturnValueCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
			this.newProp = -1;
		}

		@Override
		public boolean doTransaction()
		{
			if (role == null)
				return false;

			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(role.id))
					return false;
				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				for (SBean.DBForceBeast b : dbforce.beasts)
					if (b.general.id == gid) {
						SBean.ForceBeastBasicCFGS cfg = gs.getGameData().getForceBattleCfgs().beastBasic;

						synchronized (role) {
							if (role.stone < cfg.resetAttackPropCost)
								return false;

							commonFlowRecord.addChange(role.useStone(cfg.resetAttackPropCost));
						}

						b.attackProp = (byte)gs.getGameData().getRandInt(1, 6);
						newProp = b.attackProp;
						force.put(fid, dbforce);
						return true;
					}
				commonFlowRecord.setEvent(TLog.AT_FORCE_RESET_BEAST_ATTACK);
				commonFlowRecord.setArg(fid);
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				tlogEvent.addCommonFlow(commonFlowRecord);
				gs.getTLogger().logEvent(role, tlogEvent);
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, newProp);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final Role role;
		public final int fid;
		public final short gid;
		public byte newProp;
		public final GeneralReturnValueCallback callback;
	}

	public void beastResetAttackProp(final Role role, final int sid, final int fid, final short gid)
	{
		gs.getDB().execute(new ResetBeastAttackPropTrans(role, fid, gid, new GeneralReturnValueCallback() {
			@Override
			public void onCallback(boolean success, int value) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastResetAttackPropRes(fid, gid, success, value));
			}
		}));
	}

	public class SetBeastDefaultTrans implements Transaction
	{
		public SetBeastDefaultTrans(int rid, int fid, short gid, byte def, GeneralCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.gid = gid;
			this.def = def;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(rid))
					return false;

				if (def >= 0 && def < dbforce.beasts.size() && dbforce.beasts.get(def).general.id == gid) {
					if (dbforce.defaultBeast != def) {
						dbforce.defaultBeast = def;
						force.put(fid, dbforce);
					}
					return true;
				}
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final int rid;
		public final int fid;
		public final short gid;
		public final byte def;
		public final GeneralCallback callback;
	}

	public void beastSetDefault(final int rid, final int sid, final int fid, final short gid, final byte def)
	{
		gs.getDB().execute(new SetBeastDefaultTrans(rid, fid, gid, def, new GeneralCallback() {
			@Override
			public void onCallback(boolean success) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastSetDefaultRes(fid, gid, def, success));
			}
		}));
	}

	public class UpgradeBeastSkillTrans implements Transaction
	{
		public UpgradeBeastSkillTrans(int rid, int fid, short gid, byte skill, GeneralReturnValueCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.gid = gid;
			this.skill = (byte)(skill-1);
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(rid))
					return false;

				for (SBean.DBForceBeast b : dbforce.beasts)
					if (skill >= 0 && skill < b.general.skills.size() && b.general.id == gid) {
						short s = (short)(b.general.skills.get(skill)-1);

						List<SBean.ForceBeastSkillUpCostCFGS> cfg = gs.getGameData().getForceBattleCfgs().beastSkillUpCost;
						if (s < 0 || s >= cfg.size() - 1) // 閺堬拷鎮楁稉锟介嚋闁板秶鐤嗘稉宥堝厴閸愬秴绶氭稉瀣磳缁撅拷
							return false;
						SBean.ForceBeastSkillUpCostCFGS ccfg = cfg.get(s);
						if (skill >= ccfg.costs.size())
							return false;
						int cost = ccfg.costs.get(skill);

						if (dbforce.activity < cost)
							return false;
						dbforce.activity -= cost;

						s ++;
						short newskill = (short)(s + 1);
						b.general.skills.set(skill, newskill);
						ret = newskill;
						force.put(fid, dbforce);
						return true;
					}
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, ret);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final int rid;
		public final int fid;
		public final short gid;
		public final byte skill;
		public int ret;
		public final GeneralReturnValueCallback callback;
	}

	public void beastUpgradeSkill(final int rid, final int sid, final int fid, final short gid, final byte skill)
	{
		gs.getDB().execute(new UpgradeBeastSkillTrans(rid, fid, gid, skill, new GeneralReturnValueCallback() {
			@Override
			public void onCallback(boolean success, int value) {
				short lvl = 0;
				if (success)
					lvl = (short)value;
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastUpgradeSkillRes(fid, gid, skill, lvl));
			}
		}));
	}

	public class UpgradeBeastPropTrans implements Transaction
	{
		public UpgradeBeastPropTrans(int rid, int fid, short gid, byte prop, GeneralReturnValueCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.gid = gid;
			this.prop = prop;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(rid))
					return false;

				for (SBean.DBForceBeast b : dbforce.beasts)
					if (prop > 0 && prop <= b.props.size() && b.general.id == gid) {

						SBean.ForceBeastBasicCFGS cfg = gs.getGameData().getForceBattleCfgs().beastBasic;
						int cost = cfg.auxPropUpCost;
						if (prop == b.mainProp || b.mainProp <= 0)
							cost = cfg.mainPropUpCost;

						int total = 0;
						for (short p : b.props)
							total += p;
						int left = b.general.lvl - total;
						if (left < cost)
							return false;

						if (b.mainProp <= 0)
							b.mainProp = prop;

						short p = b.props.get(prop-1);
						p ++;
						b.props.set(prop-1, p);
						ret = p;
						force.put(fid, dbforce);
						return true;
					}
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, ret);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final int rid;
		public final int fid;
		public final short gid;
		public final byte prop;
		public int ret;
		public final GeneralReturnValueCallback callback;
	}

	public void beastUpgradeProp(final int rid, final int sid, final int fid, final short gid, final byte prop)
	{
		gs.getDB().execute(new UpgradeBeastPropTrans(rid, fid, gid, prop, new GeneralReturnValueCallback() {
			@Override
			public void onCallback(boolean success, int value) {
				short lvl = 0;
				if (success)
					lvl = (short)value;
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastUpgradePropRes(fid, gid, prop, lvl));
			}
		}));
	}

	public class ResetBeastSkillTrans implements Transaction
	{
		public ResetBeastSkillTrans(int rid, int fid, short gid, GeneralReturnValueCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
			this.back = 0;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(rid))
					return false;

				for (SBean.DBForceBeast b : dbforce.beasts)
					if (b.general.id == gid) {
						SBean.ForceBeastBasicCFGS cfg = gs.getGameData().getForceBattleCfgs().beastBasic;
						if (dbforce.activity < cfg.resetSkillCost)
							return false;
						dbforce.activity -= cfg.resetSkillCost;

						List<SBean.ForceBeastSkillUpCostCFGS> scfg = gs.getGameData().getForceBattleCfgs().beastSkillUpCost;
						int giveBack = 0;

						for (int i = 0; i < 5; i ++) {
							short slvl = b.general.skills.get(i);
							for (int j = 0; j < slvl-1; j ++) {
								SBean.ForceBeastSkillUpCostCFGS ccfg = scfg.get(j);
								if (ccfg.costs.size() > i)
									giveBack += ccfg.costs.get(i);
							}
							if (b.general.skills.get(i) > 0)
								b.general.skills.set(i, (short)1);
						}
						if (giveBack > 0)
							dbforce.activity += giveBack;
						back = giveBack;
						force.put(fid, dbforce);
						return true;
					}
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, back);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final int rid;
		public final int fid;
		public final short gid;
		public int back;
		public final GeneralReturnValueCallback callback;
	}

	public void beastResetSkill(final int rid, final int sid, final int fid, final short gid)
	{
		gs.getDB().execute(new ResetBeastSkillTrans(rid, fid, gid, new GeneralReturnValueCallback() {
			@Override
			public void onCallback(boolean success, int value) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastResetSkillRes(fid, gid, success, value));
			}
		}));
	}

	public class ResetBeastPropTrans implements Transaction
	{
		public ResetBeastPropTrans(Role role, int fid, short gid, GeneralCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			if (role == null)
				return false;

			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(role.id))
					return false;

				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				for (SBean.DBForceBeast b : dbforce.beasts)
					if (b.general.id == gid) {
						SBean.ForceBeastBasicCFGS cfg = gs.getGameData().getForceBattleCfgs().beastBasic;
						int index = b.resetPropTimes;
						if (index >= cfg.resetPropCost.size())
							index = cfg.resetPropCost.size() - 1;
						int cost = cfg.resetPropCost.get(index);

						synchronized (role) {
							if (role.stone < cost)
								return false;

							commonFlowRecord.addChange(role.useStone(cost));
						}

						for (int i = 0; i < 5; i ++)
							b.props.set(i, (short)0);
						b.mainProp = -1;
						b.resetPropTimes ++;
						force.put(fid, dbforce);
						return true;
					}
				commonFlowRecord.setEvent(TLog.AT_FORCE_RESET_BEAST_DEFENSE);
				commonFlowRecord.setArg(fid);
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				tlogEvent.addCommonFlow(commonFlowRecord);
				gs.getTLogger().logEvent(role, tlogEvent);
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final Role role;
		public final int fid;
		public final short gid;
		public final GeneralCallback callback;
	}

	public void beastResetProp(final Role role, final int sid, final int fid, final short gid)
	{
		gs.getDB().execute(new ResetBeastPropTrans(role, fid, gid, new GeneralCallback() {
			@Override
			public void onCallback(boolean success) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastResetPropRes(fid, gid, success));
			}
		}));
	}

	public class UpgradeBeastEvoTrans implements Transaction
	{
		public UpgradeBeastEvoTrans(int rid, int fid, short gid, GeneralReturnValuesCallback callback)
		{
			this.rid = rid;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
		}

		@Override
		public boolean doTransaction()
		{
			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				if (!dbforce.canModBeast(rid))
					return false;

				for (SBean.DBForceBeast b : dbforce.beasts)
					if (b.general.id == gid) {
						List<SBean.ForceBeastEvoCFGS> cfg = gs.getGameData().getForceBattleCfgs().beastEvo;
						SBean.ForceBeastEvoCFGS ecfg = null;
						int index = 0;
						for (SBean.ForceBeastEvoCFGS e : cfg) {
							index ++;
							if (e.gid == gid) {
								ecfg = e;
								break;
							}
						}
						if (ecfg == null)
							return false;

						if (index >= cfg.size() - 1)
							return false;

						if (b.general.lvl < ecfg.nextLvl)
							return false;

						if (dbforce.activity < ecfg.nextCost)
							return false;
						dbforce.activity -= ecfg.nextCost;

						b.general.id = cfg.get(index).gid;
						b.general.evoLvl ++;
						ret[0] = b.general.id;
						ret[1] = b.general.evoLvl;
						force.put(fid, dbforce);
						return true;
					}
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, ret);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final int rid;
		public final int fid;
		public final short gid;
		public int[] ret = {0,0};
		public final GeneralReturnValuesCallback callback;
	}

	public void beastUpgradeEvo(final int rid, final int sid, final int fid, final short gid)
	{
		gs.getDB().execute(new UpgradeBeastEvoTrans(rid, fid, gid, new GeneralReturnValuesCallback() {
			@Override
			public void onCallback(boolean success, int[] value) {
				short gid = 0;
				byte lvl = 0;
				if (success) {
					gid = (short)value[0];
					lvl = (byte)value[1];
				}
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastUpgradeEvoRes(fid, gid, lvl));
			}
		}));
	}

	public class FeedBeastTrans implements Transaction
	{
		public FeedBeastTrans(Role role, int fid, short gid, GeneralReturnValuesCallback callback)
		{
			this.role = role;
			this.fid = fid;
			this.gid = gid;
			this.callback = callback;
			this.values = new int[]{0, 0};
		}

		@Override
		public boolean doTransaction()
		{
			if (role == null)
				return false;

			DBForce dbforce = force.get(fid);
			if( dbforce != null )
			{
				TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
				for (SBean.DBForceBeast b : dbforce.beasts)
					if (b.general.id == gid) {
						SBean.ForceBeastBasicCFGS cfg = gs.getGameData().getForceBattleCfgs().beastBasic;
						int count = 0;
						synchronized (role) {
							count = role.getItemCount(cfg.feedItemId);
							if (count <= 0)
								return false;
							commonFlowRecord.addChange(role.delItem(cfg.feedItemId, count));
						}

						b.general.exp += count * cfg.feedGain;

						List<Integer> lcfg = gs.getGameData().getForceBattleCfgs().beastExp;
						while (b.general.lvl < lcfg.size()) {
							int exp = lcfg.get(b.general.lvl - 1);
							if (exp <= b.general.exp) {
								b.general.exp -= exp;
								b.general.lvl ++;

								int[] skillOpen = {1, 20, 40, 60, 70};
								for (int i = 0; i < 5; i ++)
									if (b.general.lvl >= skillOpen[i] && b.general.skills.get(i) <= 0)
										b.general.skills.set(i, (short)1);
							}
							else
								break;
						}

						values[0] = b.general.exp;
						values[1] = b.general.lvl;
						force.put(fid, dbforce);
						return true;
					}
				commonFlowRecord.setEvent(TLog.AT_FORCE_FEED_BEAST);
				commonFlowRecord.setArg(fid);
				TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
				tlogEvent.addCommonFlow(commonFlowRecord);
				gs.getTLogger().logEvent(role, tlogEvent);
			}
			return false;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			callback.onCallback(errcode == ErrorCode.eOK, values);
		}

		@AutoInit
		public Table<Integer, DBForce> force;

		public final Role role;
		public final int fid;
		public final short gid;
		public int [] values;
		public final GeneralReturnValuesCallback callback;
	}

	public void beastFeed(final Role role, final int sid, final int fid, final short gid)
	{
		gs.getDB().execute(new FeedBeastTrans(role, fid, gid, new GeneralReturnValuesCallback() {
			@Override
			public void onCallback(boolean success, int [] values) {
				gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeForceBeastFeedRes(fid, gid, success, values));
			}
		}));
	}


	public static SBean.DBForceWar convertNewData(SBean.DBForceWarOld old)
	{
		if (old == null)
			return null;

		List<SBean.DBForceWarForcePlayer> players = new ArrayList<SBean.DBForceWarForcePlayer>();
		for (SBean.DBForceWarForcePlayerOld o : old.players) {
			List<SBean.DBForceWarRolePlayer> rplayers = new ArrayList<SBean.DBForceWarRolePlayer>();
			for (SBean.DBForceWarRolePlayerOld ro : o.players)
				rplayers.add(new SBean.DBForceWarRolePlayer(ro.id, ro.name, ro.icon, ro.level, ro.power, ro.generals, ro.pets, new ArrayList<SBean.DBRelation>(), ro.plusItems));
			players.add(new SBean.DBForceWarForcePlayer(o.id, o.name, o.icon, o.level, o.headId, o.headName, o.froles, rplayers, o.plusItems));
		}

		return new SBean.DBForceWar(old.bid, players, old.stage, old.status, old.roleScores, old.cupWins, old.cupResults, old.leagueRecords, old.leagueSequence, old.cupRecords, old.cupQuizs);
	}

	GameServer gs;
	ForceWar war;

}

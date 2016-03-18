// modified by ket.kio.RPCGen at Thu Mar 17 16:00:52 CST 2016.

package i3k;

import java.util.List;
import java.util.ArrayList;
import java.nio.ByteBuffer;

import ket.util.Stream;

public final class SBean
{

	public static class Float3 implements Stream.IStreamable
	{

		public Float3() { }

		public Float3(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Float3 ksClone()
		{
			return new Float3(x, y, z);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			x = is.popFloat();
			y = is.popFloat();
			z = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(x);
			os.pushFloat(y);
			os.pushFloat(z);
		}

		public float x;
		public float y;
		public float z;
	}

	public static class CmnRewardCFGS implements Stream.IStreamable
	{

		public CmnRewardCFGS() { }

		public CmnRewardCFGS(short id, byte generalLevel, int exp, int stone, 
		                     int money, List<DropEntry> rewards)
		{
			this.id = id;
			this.generalLevel = generalLevel;
			this.exp = exp;
			this.stone = stone;
			this.money = money;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			generalLevel = is.popByte();
			exp = is.popInteger();
			stone = is.popInteger();
			money = is.popInteger();
			rewards = is.popList(DropEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(generalLevel);
			os.pushInteger(exp);
			os.pushInteger(stone);
			os.pushInteger(money);
			os.pushList(rewards);
		}

		public short id;
		public byte generalLevel;
		public int exp;
		public int stone;
		public int money;
		public List<DropEntry> rewards;
	}

	public static class UIEffectCFG implements Stream.IStreamable
	{

		public UIEffectCFG() { }

		public UIEffectCFG(short id, String path, float scale)
		{
			this.id = id;
			this.path = path;
			this.scale = scale;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			path = is.popString();
			scale = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(path);
			os.pushFloat(scale);
		}

		public short id;
		public String path;
		public float scale;
	}

	public static class ArtEffectCFG implements Stream.IStreamable
	{

		public ArtEffectCFG() { }

		public ArtEffectCFG(short id, String path, float radius, String hsName)
		{
			this.id = id;
			this.path = path;
			this.radius = radius;
			this.hsName = hsName;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			path = is.popString();
			radius = is.popFloat();
			hsName = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(path);
			os.pushFloat(radius);
			os.pushString(hsName);
		}

		public short id;
		public String path;
		public float radius;
		public String hsName;
	}

	public static class FightEffectCFG implements Stream.IStreamable
	{

		public FightEffectCFG() { }

		public FightEffectCFG(byte id, String name, short artEffectID)
		{
			this.id = id;
			this.name = name;
			this.artEffectID = artEffectID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			name = is.popString();
			artEffectID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushString(name);
			os.pushShort(artEffectID);
		}

		public byte id;
		public String name;
		public short artEffectID;
	}

	public static class CombatMapCFG implements Stream.IStreamable
	{

		public CombatMapCFG() { }

		public CombatMapCFG(short id, String name, String path)
		{
			this.id = id;
			this.name = name;
			this.path = path;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			path = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushString(path);
		}

		public short id;
		public String name;
		public String path;
	}

	public static class CombatGeneral implements Stream.IStreamable
	{

		public CombatGeneral() { }

		public CombatGeneral(short generalID, byte generalLvl)
		{
			this.generalID = generalID;
			this.generalLvl = generalLvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generalID = is.popShort();
			generalLvl = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(generalID);
			os.pushByte(generalLvl);
		}

		public short generalID;
		public byte generalLvl;
	}

	public static class CombatCFGS implements Stream.IStreamable
	{

		public CombatCFGS() { }

		public CombatCFGS(short id, byte monsterWaveCnt, short lvlReq, byte monsterCnt, 
		                  byte immune, int fightPower, byte maxTimes, byte type, 
		                  short vitCostStart, short vitCostWin, int dropExp, int dropGeneralExp, 
		                  int dropMoney, short dropStone, byte dropStoneTimes, byte dropTimes, 
		                  short dropTableID, short dropStoneID, List<DropEntry> dropFixed)
		{
			this.id = id;
			this.monsterWaveCnt = monsterWaveCnt;
			this.lvlReq = lvlReq;
			this.monsterCnt = monsterCnt;
			this.immune = immune;
			this.fightPower = fightPower;
			this.maxTimes = maxTimes;
			this.type = type;
			this.vitCostStart = vitCostStart;
			this.vitCostWin = vitCostWin;
			this.dropExp = dropExp;
			this.dropGeneralExp = dropGeneralExp;
			this.dropMoney = dropMoney;
			this.dropStone = dropStone;
			this.dropStoneTimes = dropStoneTimes;
			this.dropTimes = dropTimes;
			this.dropTableID = dropTableID;
			this.dropStoneID = dropStoneID;
			this.dropFixed = dropFixed;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			monsterWaveCnt = is.popByte();
			lvlReq = is.popShort();
			monsterCnt = is.popByte();
			immune = is.popByte();
			fightPower = is.popInteger();
			maxTimes = is.popByte();
			type = is.popByte();
			vitCostStart = is.popShort();
			vitCostWin = is.popShort();
			dropExp = is.popInteger();
			dropGeneralExp = is.popInteger();
			dropMoney = is.popInteger();
			dropStone = is.popShort();
			dropStoneTimes = is.popByte();
			dropTimes = is.popByte();
			dropTableID = is.popShort();
			dropStoneID = is.popShort();
			dropFixed = is.popList(DropEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(monsterWaveCnt);
			os.pushShort(lvlReq);
			os.pushByte(monsterCnt);
			os.pushByte(immune);
			os.pushInteger(fightPower);
			os.pushByte(maxTimes);
			os.pushByte(type);
			os.pushShort(vitCostStart);
			os.pushShort(vitCostWin);
			os.pushInteger(dropExp);
			os.pushInteger(dropGeneralExp);
			os.pushInteger(dropMoney);
			os.pushShort(dropStone);
			os.pushByte(dropStoneTimes);
			os.pushByte(dropTimes);
			os.pushShort(dropTableID);
			os.pushShort(dropStoneID);
			os.pushList(dropFixed);
		}

		public short id;
		public byte monsterWaveCnt;
		public short lvlReq;
		public byte monsterCnt;
		public byte immune;
		public int fightPower;
		public byte maxTimes;
		public byte type;
		public short vitCostStart;
		public short vitCostWin;
		public int dropExp;
		public int dropGeneralExp;
		public int dropMoney;
		public short dropStone;
		public byte dropStoneTimes;
		public byte dropTimes;
		public short dropTableID;
		public short dropStoneID;
		public List<DropEntry> dropFixed;
	}

	public static class CombatEventCFGS implements Stream.IStreamable
	{

		public CombatEventCFGS() { }

		public CombatEventCFGS(byte id, short lvlReq, byte maxTimes, short cool, 
		                       List<Byte> weekdays, List<Short> combatIDs)
		{
			this.id = id;
			this.lvlReq = lvlReq;
			this.maxTimes = maxTimes;
			this.cool = cool;
			this.weekdays = weekdays;
			this.combatIDs = combatIDs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			lvlReq = is.popShort();
			maxTimes = is.popByte();
			cool = is.popShort();
			weekdays = is.popByteList();
			combatIDs = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushShort(lvlReq);
			os.pushByte(maxTimes);
			os.pushShort(cool);
			os.pushByteList(weekdays);
			os.pushShortList(combatIDs);
		}

		public byte id;
		public short lvlReq;
		public byte maxTimes;
		public short cool;
		public List<Byte> weekdays;
		public List<Short> combatIDs;
	}

	public static class DayReward implements Stream.IStreamable
	{

		public static final byte eTypeItem = 0;
		public static final byte eTypeEquip = 1;
		public static final byte eTypeGeneral = 2;

		public DayReward() { }

		public DayReward(byte type, byte count, short id)
		{
			this.type = type;
			this.count = count;
			this.id = id;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			count = is.popByte();
			id = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushByte(count);
			os.pushShort(id);
		}

		public byte type;
		public byte count;
		public short id;
	}

	public static class ForceCostCFGS implements Stream.IStreamable
	{

		public ForceCostCFGS() { }

		public ForceCostCFGS(byte moneyType, int money)
		{
			this.moneyType = moneyType;
			this.money = money;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			moneyType = is.popByte();
			money = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(moneyType);
			os.pushInteger(money);
		}

		public byte moneyType;
		public int money;
	}

	public static class ForceLevelCFGS implements Stream.IStreamable
	{

		public ForceLevelCFGS() { }

		public ForceLevelCFGS(int cupNeed, List<Byte> posCount)
		{
			this.cupNeed = cupNeed;
			this.posCount = posCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cupNeed = is.popInteger();
			posCount = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(cupNeed);
			os.pushByteList(posCount);
		}

		public int cupNeed;
		public List<Byte> posCount;
	}

	public static class ForceMobaiCFGS implements Stream.IStreamable
	{

		public ForceMobaiCFGS() { }

		public ForceMobaiCFGS(int maxRewardMoney, short freeVit, short moneyVit, short stoneVit, 
		                      int moneyCost, int stoneCost, int freeMoney, int moneyMoney, 
		                      int stoneMoney)
		{
			this.maxRewardMoney = maxRewardMoney;
			this.freeVit = freeVit;
			this.moneyVit = moneyVit;
			this.stoneVit = stoneVit;
			this.moneyCost = moneyCost;
			this.stoneCost = stoneCost;
			this.freeMoney = freeMoney;
			this.moneyMoney = moneyMoney;
			this.stoneMoney = stoneMoney;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			maxRewardMoney = is.popInteger();
			freeVit = is.popShort();
			moneyVit = is.popShort();
			stoneVit = is.popShort();
			moneyCost = is.popInteger();
			stoneCost = is.popInteger();
			freeMoney = is.popInteger();
			moneyMoney = is.popInteger();
			stoneMoney = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(maxRewardMoney);
			os.pushShort(freeVit);
			os.pushShort(moneyVit);
			os.pushShort(stoneVit);
			os.pushInteger(moneyCost);
			os.pushInteger(stoneCost);
			os.pushInteger(freeMoney);
			os.pushInteger(moneyMoney);
			os.pushInteger(stoneMoney);
		}

		public int maxRewardMoney;
		public short freeVit;
		public short moneyVit;
		public short stoneVit;
		public int moneyCost;
		public int stoneCost;
		public int freeMoney;
		public int moneyMoney;
		public int stoneMoney;
	}

	public static class ForceActivityDropCFGS implements Stream.IStreamable
	{

		public ForceActivityDropCFGS() { }

		public ForceActivityDropCFGS(byte maxLvl, short drop)
		{
			this.maxLvl = maxLvl;
			this.drop = drop;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			maxLvl = is.popByte();
			drop = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(maxLvl);
			os.pushShort(drop);
		}

		public byte maxLvl;
		public short drop;
	}

	public static class ForceActivityRewardCFGS implements Stream.IStreamable
	{

		public ForceActivityRewardCFGS() { }

		public ForceActivityRewardCFGS(String name, String tips, int forceActivity, int activity, 
		                               int money1, int money2, List<ForceActivityDropCFGS> drops)
		{
			this.name = name;
			this.tips = tips;
			this.forceActivity = forceActivity;
			this.activity = activity;
			this.money1 = money1;
			this.money2 = money2;
			this.drops = drops;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			name = is.popString();
			tips = is.popString();
			forceActivity = is.popInteger();
			activity = is.popInteger();
			money1 = is.popInteger();
			money2 = is.popInteger();
			drops = is.popList(ForceActivityDropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(name);
			os.pushString(tips);
			os.pushInteger(forceActivity);
			os.pushInteger(activity);
			os.pushInteger(money1);
			os.pushInteger(money2);
			os.pushList(drops);
		}

		public String name;
		public String tips;
		public int forceActivity;
		public int activity;
		public int money1;
		public int money2;
		public List<ForceActivityDropCFGS> drops;
	}

	public static class ForceDismissRewardCFGS implements Stream.IStreamable
	{

		public ForceDismissRewardCFGS() { }

		public ForceDismissRewardCFGS(short lvl, short itemId, int count)
		{
			this.lvl = lvl;
			this.itemId = itemId;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			itemId = is.popShort();
			count = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushShort(itemId);
			os.pushInteger(count);
		}

		public short lvl;
		public short itemId;
		public int count;
	}

	public static class ForceCFGS implements Stream.IStreamable
	{

		public ForceCFGS() { }

		public ForceCFGS(short lvlCreate, int stoneCreate, short maxMembers, List<ForceLevelCFGS> levels, 
		                 ForceMobaiCFGS mobai, List<ForceActivityRewardCFGS> activityRewards, int sendMailMaxCnt, List<ForceDismissRewardCFGS> dismissRewards)
		{
			this.lvlCreate = lvlCreate;
			this.stoneCreate = stoneCreate;
			this.maxMembers = maxMembers;
			this.levels = levels;
			this.mobai = mobai;
			this.activityRewards = activityRewards;
			this.sendMailMaxCnt = sendMailMaxCnt;
			this.dismissRewards = dismissRewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlCreate = is.popShort();
			stoneCreate = is.popInteger();
			maxMembers = is.popShort();
			levels = is.popList(ForceLevelCFGS.class);
			if( mobai == null )
				mobai = new ForceMobaiCFGS();
			is.pop(mobai);
			activityRewards = is.popList(ForceActivityRewardCFGS.class);
			sendMailMaxCnt = is.popInteger();
			dismissRewards = is.popList(ForceDismissRewardCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlCreate);
			os.pushInteger(stoneCreate);
			os.pushShort(maxMembers);
			os.pushList(levels);
			os.push(mobai);
			os.pushList(activityRewards);
			os.pushInteger(sendMailMaxCnt);
			os.pushList(dismissRewards);
		}

		public short lvlCreate;
		public int stoneCreate;
		public short maxMembers;
		public List<ForceLevelCFGS> levels;
		public ForceMobaiCFGS mobai;
		public List<ForceActivityRewardCFGS> activityRewards;
		public int sendMailMaxCnt;
		public List<ForceDismissRewardCFGS> dismissRewards;
	}

	public static class CombatExtraDropCFGS implements Stream.IStreamable
	{

		public CombatExtraDropCFGS() { }

		public CombatExtraDropCFGS(short combatID, short dropTblID)
		{
			this.combatID = combatID;
			this.dropTblID = dropTblID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatID = is.popShort();
			dropTblID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatID);
			os.pushShort(dropTblID);
		}

		public short combatID;
		public short dropTblID;
	}

	public static class PlusEffectCFGS implements Stream.IStreamable
	{

		public PlusEffectCFGS() { }

		public PlusEffectCFGS(int count, byte plus)
		{
			this.count = count;
			this.plus = plus;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			count = is.popInteger();
			plus = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(count);
			os.pushByte(plus);
		}

		public int count;
		public byte plus;
	}

	public static class ItemPlusEffectCFGS implements Stream.IStreamable
	{

		public ItemPlusEffectCFGS() { }

		public ItemPlusEffectCFGS(short id, short itemID, short rewardPointPerItem, List<PlusEffectCFGS> plusEffect)
		{
			this.id = id;
			this.itemID = itemID;
			this.rewardPointPerItem = rewardPointPerItem;
			this.plusEffect = plusEffect;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			itemID = is.popShort();
			rewardPointPerItem = is.popShort();
			plusEffect = is.popList(PlusEffectCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(itemID);
			os.pushShort(rewardPointPerItem);
			os.pushList(plusEffect);
		}

		public short id;
		public short itemID;
		public short rewardPointPerItem;
		public List<PlusEffectCFGS> plusEffect;
	}

	public static class ForceBattleGameCFGS implements Stream.IStreamable
	{

		public ForceBattleGameCFGS() { }

		public ForceBattleGameCFGS(int startTime, int endTime, int winPoints, int winMoney, 
		                           int lostPoints, int lostMoney)
		{
			this.startTime = startTime;
			this.endTime = endTime;
			this.winPoints = winPoints;
			this.winMoney = winMoney;
			this.lostPoints = lostPoints;
			this.lostMoney = lostMoney;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			startTime = is.popInteger();
			endTime = is.popInteger();
			winPoints = is.popInteger();
			winMoney = is.popInteger();
			lostPoints = is.popInteger();
			lostMoney = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(startTime);
			os.pushInteger(endTime);
			os.pushInteger(winPoints);
			os.pushInteger(winMoney);
			os.pushInteger(lostPoints);
			os.pushInteger(lostMoney);
		}

		public int startTime;
		public int endTime;
		public int winPoints;
		public int winMoney;
		public int lostPoints;
		public int lostMoney;
	}

	public static class ForceBattleCFGS implements Stream.IStreamable
	{

		public ForceBattleCFGS() { }

		public ForceBattleCFGS(int id, int startTime, int endTime, int selectionTimeStart, 
		                       int knockoutTimeStart, int forceMemCntReq, int playerLvlReq, int generalLvlReq, 
		                       short normalDropTblID, short heroDropTblID, List<CombatExtraDropCFGS> combatsDropTblIDs, List<ForceBattleGameCFGS> knockout, 
		                       List<Integer> leagueEndTimes, int recordEndTime)
		{
			this.id = id;
			this.startTime = startTime;
			this.endTime = endTime;
			this.selectionTimeStart = selectionTimeStart;
			this.knockoutTimeStart = knockoutTimeStart;
			this.forceMemCntReq = forceMemCntReq;
			this.playerLvlReq = playerLvlReq;
			this.generalLvlReq = generalLvlReq;
			this.normalDropTblID = normalDropTblID;
			this.heroDropTblID = heroDropTblID;
			this.combatsDropTblIDs = combatsDropTblIDs;
			this.knockout = knockout;
			this.leagueEndTimes = leagueEndTimes;
			this.recordEndTime = recordEndTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			startTime = is.popInteger();
			endTime = is.popInteger();
			selectionTimeStart = is.popInteger();
			knockoutTimeStart = is.popInteger();
			forceMemCntReq = is.popInteger();
			playerLvlReq = is.popInteger();
			generalLvlReq = is.popInteger();
			normalDropTblID = is.popShort();
			heroDropTblID = is.popShort();
			combatsDropTblIDs = is.popList(CombatExtraDropCFGS.class);
			knockout = is.popList(ForceBattleGameCFGS.class);
			leagueEndTimes = is.popIntegerList();
			recordEndTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(startTime);
			os.pushInteger(endTime);
			os.pushInteger(selectionTimeStart);
			os.pushInteger(knockoutTimeStart);
			os.pushInteger(forceMemCntReq);
			os.pushInteger(playerLvlReq);
			os.pushInteger(generalLvlReq);
			os.pushShort(normalDropTblID);
			os.pushShort(heroDropTblID);
			os.pushList(combatsDropTblIDs);
			os.pushList(knockout);
			os.pushIntegerList(leagueEndTimes);
			os.pushInteger(recordEndTime);
		}

		public int id;
		public int startTime;
		public int endTime;
		public int selectionTimeStart;
		public int knockoutTimeStart;
		public int forceMemCntReq;
		public int playerLvlReq;
		public int generalLvlReq;
		public short normalDropTblID;
		public short heroDropTblID;
		public List<CombatExtraDropCFGS> combatsDropTblIDs;
		public List<ForceBattleGameCFGS> knockout;
		public List<Integer> leagueEndTimes;
		public int recordEndTime;
	}

	public static class ForceBattleRewardCFGS implements Stream.IStreamable
	{

		public ForceBattleRewardCFGS() { }

		public ForceBattleRewardCFGS(int money, int stone, short point, short item1, 
		                             byte item1Count, short item2, byte item2Count)
		{
			this.money = money;
			this.stone = stone;
			this.point = point;
			this.item1 = item1;
			this.item1Count = item1Count;
			this.item2 = item2;
			this.item2Count = item2Count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			money = is.popInteger();
			stone = is.popInteger();
			point = is.popShort();
			item1 = is.popShort();
			item1Count = is.popByte();
			item2 = is.popShort();
			item2Count = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(money);
			os.pushInteger(stone);
			os.pushShort(point);
			os.pushShort(item1);
			os.pushByte(item1Count);
			os.pushShort(item2);
			os.pushByte(item2Count);
		}

		public int money;
		public int stone;
		public short point;
		public short item1;
		public byte item1Count;
		public short item2;
		public byte item2Count;
	}

	public static class ForceThiefCombatCFGS implements Stream.IStreamable
	{

		public ForceThiefCombatCFGS() { }

		public ForceThiefCombatCFGS(short lvlMin, List<Short> combatIds)
		{
			this.lvlMin = lvlMin;
			this.combatIds = combatIds;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlMin = is.popShort();
			combatIds = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlMin);
			os.pushShortList(combatIds);
		}

		public short lvlMin;
		public List<Short> combatIds;
	}

	public static class ForceThiefSequenceCFGS implements Stream.IStreamable
	{

		public ForceThiefSequenceCFGS() { }

		public ForceThiefSequenceCFGS(int startTime, List<Byte> combatTypes)
		{
			this.startTime = startTime;
			this.combatTypes = combatTypes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			startTime = is.popInteger();
			combatTypes = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(startTime);
			os.pushByteList(combatTypes);
		}

		public int startTime;
		public List<Byte> combatTypes;
	}

	public static class ForceThiefRewardCFGS implements Stream.IStreamable
	{

		public ForceThiefRewardCFGS() { }

		public ForceThiefRewardCFGS(int rankFloor, short itemId, int itemCount)
		{
			this.rankFloor = rankFloor;
			this.itemId = itemId;
			this.itemCount = itemCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			itemId = is.popShort();
			itemCount = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushShort(itemId);
			os.pushInteger(itemCount);
		}

		public int rankFloor;
		public short itemId;
		public int itemCount;
	}

	public static class ForceBeastBasicCFGS implements Stream.IStreamable
	{

		public ForceBeastBasicCFGS() { }

		public ForceBeastBasicCFGS(int resetAttackPropCost, int resetSkillCost, List<Integer> resetPropCost, int mainPropUpCost, 
		                           int auxPropUpCost, short feedItemId, short feedGain)
		{
			this.resetAttackPropCost = resetAttackPropCost;
			this.resetSkillCost = resetSkillCost;
			this.resetPropCost = resetPropCost;
			this.mainPropUpCost = mainPropUpCost;
			this.auxPropUpCost = auxPropUpCost;
			this.feedItemId = feedItemId;
			this.feedGain = feedGain;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			resetAttackPropCost = is.popInteger();
			resetSkillCost = is.popInteger();
			resetPropCost = is.popIntegerList();
			mainPropUpCost = is.popInteger();
			auxPropUpCost = is.popInteger();
			feedItemId = is.popShort();
			feedGain = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(resetAttackPropCost);
			os.pushInteger(resetSkillCost);
			os.pushIntegerList(resetPropCost);
			os.pushInteger(mainPropUpCost);
			os.pushInteger(auxPropUpCost);
			os.pushShort(feedItemId);
			os.pushShort(feedGain);
		}

		public int resetAttackPropCost;
		public int resetSkillCost;
		public List<Integer> resetPropCost;
		public int mainPropUpCost;
		public int auxPropUpCost;
		public short feedItemId;
		public short feedGain;
	}

	public static class ForceBeastSkillUpCostCFGS implements Stream.IStreamable
	{

		public ForceBeastSkillUpCostCFGS() { }

		public ForceBeastSkillUpCostCFGS(List<Integer> costs)
		{
			this.costs = costs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			costs = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushIntegerList(costs);
		}

		public List<Integer> costs;
	}

	public static class ForceBeastEvoCFGS implements Stream.IStreamable
	{

		public ForceBeastEvoCFGS() { }

		public ForceBeastEvoCFGS(short gid, int nextCost, short nextLvl)
		{
			this.gid = gid;
			this.nextCost = nextCost;
			this.nextLvl = nextLvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			nextCost = is.popInteger();
			nextLvl = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.pushInteger(nextCost);
			os.pushShort(nextLvl);
		}

		public short gid;
		public int nextCost;
		public short nextLvl;
	}

	public static class ForceBattlesCFGS implements Stream.IStreamable
	{

		public ForceBattlesCFGS() { }

		public ForceBattlesCFGS(List<ForceBattleCFGS> battles, List<ItemPlusEffectCFGS> itemPlusEffects, List<ForceBattleRewardCFGS> rewards, List<ForceBattleRewardCFGS> headRewards, 
		                        List<ForceThiefCombatCFGS> thiefCombats, List<ForceThiefCombatCFGS> thiefBossCombats, List<ForceThiefSequenceCFGS> thiefSequences, List<ForceThiefRewardCFGS> thiefRewards, 
		                        ForceBeastBasicCFGS beastBasic, List<ForceBeastSkillUpCostCFGS> beastSkillUpCost, List<ForceBeastEvoCFGS> beastEvo, List<Integer> beastExp, 
		                        short normalDropTblID, short heroDropTblID, List<CombatExtraDropCFGS> combatsDropTblIDs)
		{
			this.battles = battles;
			this.itemPlusEffects = itemPlusEffects;
			this.rewards = rewards;
			this.headRewards = headRewards;
			this.thiefCombats = thiefCombats;
			this.thiefBossCombats = thiefBossCombats;
			this.thiefSequences = thiefSequences;
			this.thiefRewards = thiefRewards;
			this.beastBasic = beastBasic;
			this.beastSkillUpCost = beastSkillUpCost;
			this.beastEvo = beastEvo;
			this.beastExp = beastExp;
			this.normalDropTblID = normalDropTblID;
			this.heroDropTblID = heroDropTblID;
			this.combatsDropTblIDs = combatsDropTblIDs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			battles = is.popList(ForceBattleCFGS.class);
			itemPlusEffects = is.popList(ItemPlusEffectCFGS.class);
			rewards = is.popList(ForceBattleRewardCFGS.class);
			headRewards = is.popList(ForceBattleRewardCFGS.class);
			thiefCombats = is.popList(ForceThiefCombatCFGS.class);
			thiefBossCombats = is.popList(ForceThiefCombatCFGS.class);
			thiefSequences = is.popList(ForceThiefSequenceCFGS.class);
			thiefRewards = is.popList(ForceThiefRewardCFGS.class);
			if( beastBasic == null )
				beastBasic = new ForceBeastBasicCFGS();
			is.pop(beastBasic);
			beastSkillUpCost = is.popList(ForceBeastSkillUpCostCFGS.class);
			beastEvo = is.popList(ForceBeastEvoCFGS.class);
			beastExp = is.popIntegerList();
			normalDropTblID = is.popShort();
			heroDropTblID = is.popShort();
			combatsDropTblIDs = is.popList(CombatExtraDropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(battles);
			os.pushList(itemPlusEffects);
			os.pushList(rewards);
			os.pushList(headRewards);
			os.pushList(thiefCombats);
			os.pushList(thiefBossCombats);
			os.pushList(thiefSequences);
			os.pushList(thiefRewards);
			os.push(beastBasic);
			os.pushList(beastSkillUpCost);
			os.pushList(beastEvo);
			os.pushIntegerList(beastExp);
			os.pushShort(normalDropTblID);
			os.pushShort(heroDropTblID);
			os.pushList(combatsDropTblIDs);
		}

		public List<ForceBattleCFGS> battles;
		public List<ItemPlusEffectCFGS> itemPlusEffects;
		public List<ForceBattleRewardCFGS> rewards;
		public List<ForceBattleRewardCFGS> headRewards;
		public List<ForceThiefCombatCFGS> thiefCombats;
		public List<ForceThiefCombatCFGS> thiefBossCombats;
		public List<ForceThiefSequenceCFGS> thiefSequences;
		public List<ForceThiefRewardCFGS> thiefRewards;
		public ForceBeastBasicCFGS beastBasic;
		public List<ForceBeastSkillUpCostCFGS> beastSkillUpCost;
		public List<ForceBeastEvoCFGS> beastEvo;
		public List<Integer> beastExp;
		public short normalDropTblID;
		public short heroDropTblID;
		public List<CombatExtraDropCFGS> combatsDropTblIDs;
	}

	public static class InvitationTaskCFG implements Stream.IStreamable
	{

		public InvitationTaskCFG() { }

		public InvitationTaskCFG(int id, int reqArg1, int reqArg2, int rewardMoney, 
		                         int rewardStone, int rewardVip, int rewardPoint)
		{
			this.id = id;
			this.reqArg1 = reqArg1;
			this.reqArg2 = reqArg2;
			this.rewardMoney = rewardMoney;
			this.rewardStone = rewardStone;
			this.rewardVip = rewardVip;
			this.rewardPoint = rewardPoint;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			reqArg1 = is.popInteger();
			reqArg2 = is.popInteger();
			rewardMoney = is.popInteger();
			rewardStone = is.popInteger();
			rewardVip = is.popInteger();
			rewardPoint = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(reqArg1);
			os.pushInteger(reqArg2);
			os.pushInteger(rewardMoney);
			os.pushInteger(rewardStone);
			os.pushInteger(rewardVip);
			os.pushInteger(rewardPoint);
		}

		public int id;
		public int reqArg1;
		public int reqArg2;
		public int rewardMoney;
		public int rewardStone;
		public int rewardVip;
		public int rewardPoint;
	}

	public static class InvitationGroupTasksCFG implements Stream.IStreamable
	{

		public InvitationGroupTasksCFG() { }

		public InvitationGroupTasksCFG(byte groupId, List<InvitationTaskCFG> tasks)
		{
			this.groupId = groupId;
			this.tasks = tasks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			groupId = is.popByte();
			tasks = is.popList(InvitationTaskCFG.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(groupId);
			os.pushList(tasks);
		}

		public byte groupId;
		public List<InvitationTaskCFG> tasks;
	}

	public static class InvitationPointsRewardCFG implements Stream.IStreamable
	{

		public InvitationPointsRewardCFG() { }

		public InvitationPointsRewardCFG(int id, int pointsLvl, List<DropEntryNew> reward)
		{
			this.id = id;
			this.pointsLvl = pointsLvl;
			this.reward = reward;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pointsLvl = is.popInteger();
			reward = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(pointsLvl);
			os.pushList(reward);
		}

		public int id;
		public int pointsLvl;
		public List<DropEntryNew> reward;
	}

	public static class InvitationTypeTasksCFG implements Stream.IStreamable
	{

		public InvitationTypeTasksCFG() { }

		public InvitationTypeTasksCFG(byte type, List<InvitationGroupTasksCFG> tasks, List<InvitationPointsRewardCFG> rewards)
		{
			this.type = type;
			this.tasks = tasks;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			tasks = is.popList(InvitationGroupTasksCFG.class);
			rewards = is.popList(InvitationPointsRewardCFG.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushList(tasks);
			os.pushList(rewards);
		}

		public byte type;
		public List<InvitationGroupTasksCFG> tasks;
		public List<InvitationPointsRewardCFG> rewards;
	}

	public static class InvitationCFGS implements Stream.IStreamable
	{

		public InvitationCFGS() { }

		public InvitationCFGS(List<InvitationTypeTasksCFG> tasks, List<DropEntryNew> beInvitedRewards, int inputKeyLvlMin, int inputKeyLvlMax, 
		                      int invatationLvlReq)
		{
			this.tasks = tasks;
			this.beInvitedRewards = beInvitedRewards;
			this.inputKeyLvlMin = inputKeyLvlMin;
			this.inputKeyLvlMax = inputKeyLvlMax;
			this.invatationLvlReq = invatationLvlReq;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tasks = is.popList(InvitationTypeTasksCFG.class);
			beInvitedRewards = is.popList(DropEntryNew.class);
			inputKeyLvlMin = is.popInteger();
			inputKeyLvlMax = is.popInteger();
			invatationLvlReq = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(tasks);
			os.pushList(beInvitedRewards);
			os.pushInteger(inputKeyLvlMin);
			os.pushInteger(inputKeyLvlMax);
			os.pushInteger(invatationLvlReq);
		}

		public List<InvitationTypeTasksCFG> tasks;
		public List<DropEntryNew> beInvitedRewards;
		public int inputKeyLvlMin;
		public int inputKeyLvlMax;
		public int invatationLvlReq;
	}

	public static class RichBossBaseCFGS implements Stream.IStreamable
	{

		public RichBossBaseCFGS() { }

		public RichBossBaseCFGS(byte lvl, int base)
		{
			this.lvl = lvl;
			this.base = base;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popByte();
			base = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lvl);
			os.pushInteger(base);
		}

		public byte lvl;
		public int base;
	}

	public static class RichMineCFGS implements Stream.IStreamable
	{

		public RichMineCFGS() { }

		public RichMineCFGS(int gain, int tax)
		{
			this.gain = gain;
			this.tax = tax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gain = is.popInteger();
			tax = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gain);
			os.pushInteger(tax);
		}

		public int gain;
		public int tax;
	}

	public static class RichObjectCFGS implements Stream.IStreamable
	{

		public RichObjectCFGS() { }

		public RichObjectCFGS(int id, short itemID)
		{
			this.id = id;
			this.itemID = itemID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			itemID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(itemID);
		}

		public int id;
		public short itemID;
	}

	public static class RichDivineCostCFGS implements Stream.IStreamable
	{

		public RichDivineCostCFGS() { }

		public RichDivineCostCFGS(int gold, int stone)
		{
			this.gold = gold;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gold = is.popInteger();
			stone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gold);
			os.pushInteger(stone);
		}

		public int gold;
		public int stone;
	}

	public static class RichBasicCFGS implements Stream.IStreamable
	{

		public RichBasicCFGS() { }

		public RichBasicCFGS(int initgold, short initmovement, short movecost, List<Short> diceposs, 
		                     List<Short> minidiceposs, List<Short> multiplydiceposs, float multiplyposs, List<Integer> minigold, 
		                     int moneygodgold, int bigmoneygodgold, byte angelround, byte devilround, 
		                     byte turtleround, int beggargold, short bmwmove, short loterycost, 
		                     List<RichDivineCostCFGS> divine1cost, List<RichDivineCostCFGS> divine2cost, List<RichDivineCostCFGS> divine3cost, byte bossTimes, 
		                     List<RichBossBaseCFGS> bossBase, int bossDamage, float angelScale, float devilScale, 
		                     short roadEnventDropTbl, short maxmovement, List<Short> divineDropTbl, List<RichMineCFGS> mine, 
		                     int buyPassGold, int richBuyPassGold, int loteryWinReward, int loteryLoseReward, 
		                     List<RichObjectCFGS> objects, short remoteDiceMinItemID, short remoteDiceMaxItemID, List<Integer> buyMovementCost, 
		                     int buyMovementVal, int bossMaxDamage, int bossMaxReward, short reqLevel)
		{
			this.initgold = initgold;
			this.initmovement = initmovement;
			this.movecost = movecost;
			this.diceposs = diceposs;
			this.minidiceposs = minidiceposs;
			this.multiplydiceposs = multiplydiceposs;
			this.multiplyposs = multiplyposs;
			this.minigold = minigold;
			this.moneygodgold = moneygodgold;
			this.bigmoneygodgold = bigmoneygodgold;
			this.angelround = angelround;
			this.devilround = devilround;
			this.turtleround = turtleround;
			this.beggargold = beggargold;
			this.bmwmove = bmwmove;
			this.loterycost = loterycost;
			this.divine1cost = divine1cost;
			this.divine2cost = divine2cost;
			this.divine3cost = divine3cost;
			this.bossTimes = bossTimes;
			this.bossBase = bossBase;
			this.bossDamage = bossDamage;
			this.angelScale = angelScale;
			this.devilScale = devilScale;
			this.roadEnventDropTbl = roadEnventDropTbl;
			this.maxmovement = maxmovement;
			this.divineDropTbl = divineDropTbl;
			this.mine = mine;
			this.buyPassGold = buyPassGold;
			this.richBuyPassGold = richBuyPassGold;
			this.loteryWinReward = loteryWinReward;
			this.loteryLoseReward = loteryLoseReward;
			this.objects = objects;
			this.remoteDiceMinItemID = remoteDiceMinItemID;
			this.remoteDiceMaxItemID = remoteDiceMaxItemID;
			this.buyMovementCost = buyMovementCost;
			this.buyMovementVal = buyMovementVal;
			this.bossMaxDamage = bossMaxDamage;
			this.bossMaxReward = bossMaxReward;
			this.reqLevel = reqLevel;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			initgold = is.popInteger();
			initmovement = is.popShort();
			movecost = is.popShort();
			diceposs = is.popShortList();
			minidiceposs = is.popShortList();
			multiplydiceposs = is.popShortList();
			multiplyposs = is.popFloat();
			minigold = is.popIntegerList();
			moneygodgold = is.popInteger();
			bigmoneygodgold = is.popInteger();
			angelround = is.popByte();
			devilround = is.popByte();
			turtleround = is.popByte();
			beggargold = is.popInteger();
			bmwmove = is.popShort();
			loterycost = is.popShort();
			divine1cost = is.popList(RichDivineCostCFGS.class);
			divine2cost = is.popList(RichDivineCostCFGS.class);
			divine3cost = is.popList(RichDivineCostCFGS.class);
			bossTimes = is.popByte();
			bossBase = is.popList(RichBossBaseCFGS.class);
			bossDamage = is.popInteger();
			angelScale = is.popFloat();
			devilScale = is.popFloat();
			roadEnventDropTbl = is.popShort();
			maxmovement = is.popShort();
			divineDropTbl = is.popShortList();
			mine = is.popList(RichMineCFGS.class);
			buyPassGold = is.popInteger();
			richBuyPassGold = is.popInteger();
			loteryWinReward = is.popInteger();
			loteryLoseReward = is.popInteger();
			objects = is.popList(RichObjectCFGS.class);
			remoteDiceMinItemID = is.popShort();
			remoteDiceMaxItemID = is.popShort();
			buyMovementCost = is.popIntegerList();
			buyMovementVal = is.popInteger();
			bossMaxDamage = is.popInteger();
			bossMaxReward = is.popInteger();
			reqLevel = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(initgold);
			os.pushShort(initmovement);
			os.pushShort(movecost);
			os.pushShortList(diceposs);
			os.pushShortList(minidiceposs);
			os.pushShortList(multiplydiceposs);
			os.pushFloat(multiplyposs);
			os.pushIntegerList(minigold);
			os.pushInteger(moneygodgold);
			os.pushInteger(bigmoneygodgold);
			os.pushByte(angelround);
			os.pushByte(devilround);
			os.pushByte(turtleround);
			os.pushInteger(beggargold);
			os.pushShort(bmwmove);
			os.pushShort(loterycost);
			os.pushList(divine1cost);
			os.pushList(divine2cost);
			os.pushList(divine3cost);
			os.pushByte(bossTimes);
			os.pushList(bossBase);
			os.pushInteger(bossDamage);
			os.pushFloat(angelScale);
			os.pushFloat(devilScale);
			os.pushShort(roadEnventDropTbl);
			os.pushShort(maxmovement);
			os.pushShortList(divineDropTbl);
			os.pushList(mine);
			os.pushInteger(buyPassGold);
			os.pushInteger(richBuyPassGold);
			os.pushInteger(loteryWinReward);
			os.pushInteger(loteryLoseReward);
			os.pushList(objects);
			os.pushShort(remoteDiceMinItemID);
			os.pushShort(remoteDiceMaxItemID);
			os.pushIntegerList(buyMovementCost);
			os.pushInteger(buyMovementVal);
			os.pushInteger(bossMaxDamage);
			os.pushInteger(bossMaxReward);
			os.pushShort(reqLevel);
		}

		public int initgold;
		public short initmovement;
		public short movecost;
		public List<Short> diceposs;
		public List<Short> minidiceposs;
		public List<Short> multiplydiceposs;
		public float multiplyposs;
		public List<Integer> minigold;
		public int moneygodgold;
		public int bigmoneygodgold;
		public byte angelround;
		public byte devilround;
		public byte turtleround;
		public int beggargold;
		public short bmwmove;
		public short loterycost;
		public List<RichDivineCostCFGS> divine1cost;
		public List<RichDivineCostCFGS> divine2cost;
		public List<RichDivineCostCFGS> divine3cost;
		public byte bossTimes;
		public List<RichBossBaseCFGS> bossBase;
		public int bossDamage;
		public float angelScale;
		public float devilScale;
		public short roadEnventDropTbl;
		public short maxmovement;
		public List<Short> divineDropTbl;
		public List<RichMineCFGS> mine;
		public int buyPassGold;
		public int richBuyPassGold;
		public int loteryWinReward;
		public int loteryLoseReward;
		public List<RichObjectCFGS> objects;
		public short remoteDiceMinItemID;
		public short remoteDiceMaxItemID;
		public List<Integer> buyMovementCost;
		public int buyMovementVal;
		public int bossMaxDamage;
		public int bossMaxReward;
		public short reqLevel;
	}

	public static class RichTechCFG implements Stream.IStreamable
	{

		public RichTechCFG() { }

		public RichTechCFG(int id, int lvlInit, int lvlMax, float initEffect, 
		                   float increaseEffect, List<Integer> lvlupReq)
		{
			this.id = id;
			this.lvlInit = lvlInit;
			this.lvlMax = lvlMax;
			this.initEffect = initEffect;
			this.increaseEffect = increaseEffect;
			this.lvlupReq = lvlupReq;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			lvlInit = is.popInteger();
			lvlMax = is.popInteger();
			initEffect = is.popFloat();
			increaseEffect = is.popFloat();
			lvlupReq = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(lvlInit);
			os.pushInteger(lvlMax);
			os.pushFloat(initEffect);
			os.pushFloat(increaseEffect);
			os.pushIntegerList(lvlupReq);
		}

		public int id;
		public int lvlInit;
		public int lvlMax;
		public float initEffect;
		public float increaseEffect;
		public List<Integer> lvlupReq;
	}

	public static class RichDailyTaskCFG implements Stream.IStreamable
	{

		public RichDailyTaskCFG() { }

		public RichDailyTaskCFG(int id, int type, int reqArg, short rewardDropTblID, 
		                        short rewardDropTimes)
		{
			this.id = id;
			this.type = type;
			this.reqArg = reqArg;
			this.rewardDropTblID = rewardDropTblID;
			this.rewardDropTimes = rewardDropTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			type = is.popInteger();
			reqArg = is.popInteger();
			rewardDropTblID = is.popShort();
			rewardDropTimes = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(type);
			os.pushInteger(reqArg);
			os.pushShort(rewardDropTblID);
			os.pushShort(rewardDropTimes);
		}

		public int id;
		public int type;
		public int reqArg;
		public short rewardDropTblID;
		public short rewardDropTimes;
	}

	public static class RichBossCombatCFGS implements Stream.IStreamable
	{

		public RichBossCombatCFGS() { }

		public RichBossCombatCFGS(short lvlMax, List<Short> combatIds)
		{
			this.lvlMax = lvlMax;
			this.combatIds = combatIds;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlMax = is.popShort();
			combatIds = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlMax);
			os.pushShortList(combatIds);
		}

		public short lvlMax;
		public List<Short> combatIds;
	}

	public static class RichMineCombatCFGS implements Stream.IStreamable
	{

		public RichMineCombatCFGS() { }

		public RichMineCombatCFGS(short lvlMax, List<Short> combatIds, List<Integer> rewards)
		{
			this.lvlMax = lvlMax;
			this.combatIds = combatIds;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlMax = is.popShort();
			combatIds = is.popShortList();
			rewards = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlMax);
			os.pushShortList(combatIds);
			os.pushIntegerList(rewards);
		}

		public short lvlMax;
		public List<Short> combatIds;
		public List<Integer> rewards;
	}

	public static class RichRewardCFGS implements Stream.IStreamable
	{

		public RichRewardCFGS() { }

		public RichRewardCFGS(int rankFloor, int money, int point)
		{
			this.rankFloor = rankFloor;
			this.money = money;
			this.point = point;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			money = is.popInteger();
			point = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushInteger(money);
			os.pushInteger(point);
		}

		public int rankFloor;
		public int money;
		public int point;
	}

	public static class RichMoneyCFGS implements Stream.IStreamable
	{

		public RichMoneyCFGS() { }

		public RichMoneyCFGS(byte type, int count, short poss, short modelId)
		{
			this.type = type;
			this.count = count;
			this.poss = poss;
			this.modelId = modelId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			count = is.popInteger();
			poss = is.popShort();
			modelId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushInteger(count);
			os.pushShort(poss);
			os.pushShort(modelId);
		}

		public byte type;
		public int count;
		public short poss;
		public short modelId;
	}

	public static class RichBoxCFGS implements Stream.IStreamable
	{

		public RichBoxCFGS() { }

		public RichBoxCFGS(float poss, float freePoss, int feeMin, int feeMax, 
		                   short dropId)
		{
			this.poss = poss;
			this.freePoss = freePoss;
			this.feeMin = feeMin;
			this.feeMax = feeMax;
			this.dropId = dropId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			poss = is.popFloat();
			freePoss = is.popFloat();
			feeMin = is.popInteger();
			feeMax = is.popInteger();
			dropId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(poss);
			os.pushFloat(freePoss);
			os.pushInteger(feeMin);
			os.pushInteger(feeMax);
			os.pushShort(dropId);
		}

		public float poss;
		public float freePoss;
		public int feeMin;
		public int feeMax;
		public short dropId;
	}

	public static class RichCFGS implements Stream.IStreamable
	{

		public RichCFGS() { }

		public RichCFGS(RichBasicCFGS basic, List<RichTechCFG> techTree, List<RichDailyTaskCFG> dailyTask, List<RichBossCombatCFGS> bossCombats, 
		                List<RichMineCombatCFGS> mineCombats, List<RichMineCombatCFGS> richMineCombats, List<RichRewardCFGS> rewards, List<RichMoneyCFGS> money, 
		                List<RichBoxCFGS> box)
		{
			this.basic = basic;
			this.techTree = techTree;
			this.dailyTask = dailyTask;
			this.bossCombats = bossCombats;
			this.mineCombats = mineCombats;
			this.richMineCombats = richMineCombats;
			this.rewards = rewards;
			this.money = money;
			this.box = box;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( basic == null )
				basic = new RichBasicCFGS();
			is.pop(basic);
			techTree = is.popList(RichTechCFG.class);
			dailyTask = is.popList(RichDailyTaskCFG.class);
			bossCombats = is.popList(RichBossCombatCFGS.class);
			mineCombats = is.popList(RichMineCombatCFGS.class);
			richMineCombats = is.popList(RichMineCombatCFGS.class);
			rewards = is.popList(RichRewardCFGS.class);
			money = is.popList(RichMoneyCFGS.class);
			box = is.popList(RichBoxCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(basic);
			os.pushList(techTree);
			os.pushList(dailyTask);
			os.pushList(bossCombats);
			os.pushList(mineCombats);
			os.pushList(richMineCombats);
			os.pushList(rewards);
			os.pushList(money);
			os.pushList(box);
		}

		public RichBasicCFGS basic;
		public List<RichTechCFG> techTree;
		public List<RichDailyTaskCFG> dailyTask;
		public List<RichBossCombatCFGS> bossCombats;
		public List<RichMineCombatCFGS> mineCombats;
		public List<RichMineCombatCFGS> richMineCombats;
		public List<RichRewardCFGS> rewards;
		public List<RichMoneyCFGS> money;
		public List<RichBoxCFGS> box;
	}

	public static class CheckinRewardCFGS implements Stream.IStreamable
	{

		public CheckinRewardCFGS() { }

		public CheckinRewardCFGS(byte lvlVIP, DropEntryNew reward)
		{
			this.lvlVIP = lvlVIP;
			this.reward = reward;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlVIP = is.popByte();
			if( reward == null )
				reward = new DropEntryNew();
			is.pop(reward);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lvlVIP);
			os.push(reward);
		}

		public byte lvlVIP;
		public DropEntryNew reward;
	}

	public static class CheckinCFGS implements Stream.IStreamable
	{

		public CheckinCFGS() { }

		public CheckinCFGS(byte id, int timeStart, List<CheckinRewardCFGS> rewards)
		{
			this.id = id;
			this.timeStart = timeStart;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			timeStart = is.popInteger();
			rewards = is.popList(CheckinRewardCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushInteger(timeStart);
			os.pushList(rewards);
		}

		public byte id;
		public int timeStart;
		public List<CheckinRewardCFGS> rewards;
	}

	public static class PackageCFGS implements Stream.IStreamable
	{

		public PackageCFGS() { }

		public PackageCFGS(short id, byte generalLevel, byte lvlReq, int money, 
		                   int stone, List<DayReward> dayRewards)
		{
			this.id = id;
			this.generalLevel = generalLevel;
			this.lvlReq = lvlReq;
			this.money = money;
			this.stone = stone;
			this.dayRewards = dayRewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			generalLevel = is.popByte();
			lvlReq = is.popByte();
			money = is.popInteger();
			stone = is.popInteger();
			dayRewards = is.popList(DayReward.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(generalLevel);
			os.pushByte(lvlReq);
			os.pushInteger(money);
			os.pushInteger(stone);
			os.pushList(dayRewards);
		}

		public short id;
		public byte generalLevel;
		public byte lvlReq;
		public int money;
		public int stone;
		public List<DayReward> dayRewards;
	}

	public static class PackageCFGC implements Stream.IStreamable
	{

		public PackageCFGC() { }

		public PackageCFGC(PackageCFGS cfgs, String name, String des)
		{
			this.cfgs = cfgs;
			this.name = name;
			this.des = des;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( cfgs == null )
				cfgs = new PackageCFGS();
			is.pop(cfgs);
			name = is.popString();
			des = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(cfgs);
			os.pushString(name);
			os.pushString(des);
		}

		public PackageCFGS cfgs;
		public String name;
		public String des;
	}

	public static class LoadingCFG implements Stream.IStreamable
	{

		public LoadingCFG() { }

		public LoadingCFG(String msg, float globalProp)
		{
			this.msg = msg;
			this.globalProp = globalProp;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			msg = is.popString();
			globalProp = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(msg);
			os.pushFloat(globalProp);
		}

		public String msg;
		public float globalProp;
	}

	public static class CombatCatEntryCFG implements Stream.IStreamable
	{

		public CombatCatEntryCFG() { }

		public CombatCatEntryCFG(short cid, byte seq)
		{
			this.cid = cid;
			this.seq = seq;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cid = is.popShort();
			seq = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cid);
			os.pushByte(seq);
		}

		public short cid;
		public byte seq;
	}

	public static class CombatCatEntryListCFG implements Stream.IStreamable
	{

		public CombatCatEntryListCFG() { }

		public CombatCatEntryListCFG(List<CombatCatEntryCFG> combats)
		{
			this.combats = combats;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combats = is.popList(CombatCatEntryCFG.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(combats);
		}

		public List<CombatCatEntryCFG> combats;
	}

	public static class CombatCatCFG implements Stream.IStreamable
	{

		public CombatCatCFG() { }

		public CombatCatCFG(byte id, String name, short mapIconID, short mapCityIconID, 
		                    short mapCityDisableIconID, List<CombatCatEntryListCFG> combats)
		{
			this.id = id;
			this.name = name;
			this.mapIconID = mapIconID;
			this.mapCityIconID = mapCityIconID;
			this.mapCityDisableIconID = mapCityDisableIconID;
			this.combats = combats;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			name = is.popString();
			mapIconID = is.popShort();
			mapCityIconID = is.popShort();
			mapCityDisableIconID = is.popShort();
			combats = is.popList(CombatCatEntryListCFG.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushString(name);
			os.pushShort(mapIconID);
			os.pushShort(mapCityIconID);
			os.pushShort(mapCityDisableIconID);
			os.pushList(combats);
		}

		public byte id;
		public String name;
		public short mapIconID;
		public short mapCityIconID;
		public short mapCityDisableIconID;
		public List<CombatCatEntryListCFG> combats;
	}

	public static class GeneralSkill implements Stream.IStreamable
	{

		public GeneralSkill() { }

		public GeneralSkill(short id, List<Byte> learnLvl)
		{
			this.id = id;
			this.learnLvl = learnLvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			learnLvl = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByteList(learnLvl);
		}

		public short id;
		public List<Byte> learnLvl;
	}

	public static class GeneralCFGS implements Stream.IStreamable
	{

		public GeneralCFGS() { }

		public GeneralCFGS(short id, String name, byte type, byte stance, 
		                   short stoneID, byte initEvoLevel, List<Short> equips, List<Integer> advPowerUps)
		{
			this.id = id;
			this.name = name;
			this.type = type;
			this.stance = stance;
			this.stoneID = stoneID;
			this.initEvoLevel = initEvoLevel;
			this.equips = equips;
			this.advPowerUps = advPowerUps;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			type = is.popByte();
			stance = is.popByte();
			stoneID = is.popShort();
			initEvoLevel = is.popByte();
			equips = is.popShortList();
			advPowerUps = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushByte(type);
			os.pushByte(stance);
			os.pushShort(stoneID);
			os.pushByte(initEvoLevel);
			os.pushShortList(equips);
			os.pushIntegerList(advPowerUps);
		}

		public short id;
		public String name;
		public byte type;
		public byte stance;
		public short stoneID;
		public byte initEvoLevel;
		public List<Short> equips;
		public List<Integer> advPowerUps;
	}

	public static class PetEvoCFGS implements Stream.IStreamable
	{

		public PetEvoCFGS() { }

		public PetEvoCFGS(int pieceCountDec, int pieceCountEvo, int power)
		{
			this.pieceCountDec = pieceCountDec;
			this.pieceCountEvo = pieceCountEvo;
			this.power = power;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pieceCountDec = is.popInteger();
			pieceCountEvo = is.popInteger();
			power = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(pieceCountDec);
			os.pushInteger(pieceCountEvo);
			os.pushInteger(power);
		}

		public int pieceCountDec;
		public int pieceCountEvo;
		public int power;
	}

	public static class PetEatCFGS implements Stream.IStreamable
	{

		public PetEatCFGS() { }

		public PetEatCFGS(short lvlMax, short eatId, short productId)
		{
			this.lvlMax = lvlMax;
			this.eatId = eatId;
			this.productId = productId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlMax = is.popShort();
			eatId = is.popShort();
			productId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlMax);
			os.pushShort(eatId);
			os.pushShort(productId);
		}

		public short lvlMax;
		public short eatId;
		public short productId;
	}

	public static class PetDeformCFGS implements Stream.IStreamable
	{

		public PetDeformCFGS() { }

		public PetDeformCFGS(byte type, short growth, List<Short> drops)
		{
			this.type = type;
			this.growth = growth;
			this.drops = drops;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			growth = is.popShort();
			drops = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(growth);
			os.pushShortList(drops);
		}

		public byte type;
		public short growth;
		public List<Short> drops;
	}

	public static class PetAwakeCFGS implements Stream.IStreamable
	{

		public PetAwakeCFGS() { }

		public PetAwakeCFGS(byte canAwake, short awakeSkill1, short awakeSkill2, short awakeSkill3, 
		                    short awakeSkill4, short awakeSkill5, List<DropEntryNew> needItem1, List<DropEntryNew> needItem2, 
		                    List<DropEntryNew> needItem3, List<DropEntryNew> needItem4, List<DropEntryNew> needItem5)
		{
			this.canAwake = canAwake;
			this.awakeSkill1 = awakeSkill1;
			this.awakeSkill2 = awakeSkill2;
			this.awakeSkill3 = awakeSkill3;
			this.awakeSkill4 = awakeSkill4;
			this.awakeSkill5 = awakeSkill5;
			this.needItem1 = needItem1;
			this.needItem2 = needItem2;
			this.needItem3 = needItem3;
			this.needItem4 = needItem4;
			this.needItem5 = needItem5;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			canAwake = is.popByte();
			awakeSkill1 = is.popShort();
			awakeSkill2 = is.popShort();
			awakeSkill3 = is.popShort();
			awakeSkill4 = is.popShort();
			awakeSkill5 = is.popShort();
			needItem1 = is.popList(DropEntryNew.class);
			needItem2 = is.popList(DropEntryNew.class);
			needItem3 = is.popList(DropEntryNew.class);
			needItem4 = is.popList(DropEntryNew.class);
			needItem5 = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(canAwake);
			os.pushShort(awakeSkill1);
			os.pushShort(awakeSkill2);
			os.pushShort(awakeSkill3);
			os.pushShort(awakeSkill4);
			os.pushShort(awakeSkill5);
			os.pushList(needItem1);
			os.pushList(needItem2);
			os.pushList(needItem3);
			os.pushList(needItem4);
			os.pushList(needItem5);
		}

		public byte canAwake;
		public short awakeSkill1;
		public short awakeSkill2;
		public short awakeSkill3;
		public short awakeSkill4;
		public short awakeSkill5;
		public List<DropEntryNew> needItem1;
		public List<DropEntryNew> needItem2;
		public List<DropEntryNew> needItem3;
		public List<DropEntryNew> needItem4;
		public List<DropEntryNew> needItem5;
	}

	public static class PetCFGS implements Stream.IStreamable
	{

		public PetCFGS() { }

		public PetCFGS(short id, byte type, byte gender, short pieceID, 
		               int pieceMoney, short eggID1, short eggID2, float proEgg2, 
		               int breedCool, int breedMoney, int initPower, int upPower, 
		               List<PetEvoCFGS> evo, short minGrowthRate, short maxGrowthRate, List<Float> propGrowthRate, 
		               List<PetEatCFGS> eat, short produceTime, short productDesc, List<PetDeformCFGS> deform, 
		               PetAwakeCFGS awake)
		{
			this.id = id;
			this.type = type;
			this.gender = gender;
			this.pieceID = pieceID;
			this.pieceMoney = pieceMoney;
			this.eggID1 = eggID1;
			this.eggID2 = eggID2;
			this.proEgg2 = proEgg2;
			this.breedCool = breedCool;
			this.breedMoney = breedMoney;
			this.initPower = initPower;
			this.upPower = upPower;
			this.evo = evo;
			this.minGrowthRate = minGrowthRate;
			this.maxGrowthRate = maxGrowthRate;
			this.propGrowthRate = propGrowthRate;
			this.eat = eat;
			this.produceTime = produceTime;
			this.productDesc = productDesc;
			this.deform = deform;
			this.awake = awake;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			type = is.popByte();
			gender = is.popByte();
			pieceID = is.popShort();
			pieceMoney = is.popInteger();
			eggID1 = is.popShort();
			eggID2 = is.popShort();
			proEgg2 = is.popFloat();
			breedCool = is.popInteger();
			breedMoney = is.popInteger();
			initPower = is.popInteger();
			upPower = is.popInteger();
			evo = is.popList(PetEvoCFGS.class);
			minGrowthRate = is.popShort();
			maxGrowthRate = is.popShort();
			propGrowthRate = is.popFloatList();
			eat = is.popList(PetEatCFGS.class);
			produceTime = is.popShort();
			productDesc = is.popShort();
			deform = is.popList(PetDeformCFGS.class);
			if( awake == null )
				awake = new PetAwakeCFGS();
			is.pop(awake);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(type);
			os.pushByte(gender);
			os.pushShort(pieceID);
			os.pushInteger(pieceMoney);
			os.pushShort(eggID1);
			os.pushShort(eggID2);
			os.pushFloat(proEgg2);
			os.pushInteger(breedCool);
			os.pushInteger(breedMoney);
			os.pushInteger(initPower);
			os.pushInteger(upPower);
			os.pushList(evo);
			os.pushShort(minGrowthRate);
			os.pushShort(maxGrowthRate);
			os.pushFloatList(propGrowthRate);
			os.pushList(eat);
			os.pushShort(produceTime);
			os.pushShort(productDesc);
			os.pushList(deform);
			os.push(awake);
		}

		public short id;
		public byte type;
		public byte gender;
		public short pieceID;
		public int pieceMoney;
		public short eggID1;
		public short eggID2;
		public float proEgg2;
		public int breedCool;
		public int breedMoney;
		public int initPower;
		public int upPower;
		public List<PetEvoCFGS> evo;
		public short minGrowthRate;
		public short maxGrowthRate;
		public List<Float> propGrowthRate;
		public List<PetEatCFGS> eat;
		public short produceTime;
		public short productDesc;
		public List<PetDeformCFGS> deform;
		public PetAwakeCFGS awake;
	}

	public static class GeneralSkillCFGS implements Stream.IStreamable
	{

		public GeneralSkillCFGS() { }

		public GeneralSkillCFGS(byte reqAdvLvl, short maxLvl, List<Integer> money)
		{
			this.reqAdvLvl = reqAdvLvl;
			this.maxLvl = maxLvl;
			this.money = money;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			reqAdvLvl = is.popByte();
			maxLvl = is.popShort();
			money = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(reqAdvLvl);
			os.pushShort(maxLvl);
			os.pushIntegerList(money);
		}

		public byte reqAdvLvl;
		public short maxLvl;
		public List<Integer> money;
	}

	public static class GeneralEvoCFGS implements Stream.IStreamable
	{

		public GeneralEvoCFGS() { }

		public GeneralEvoCFGS(int money, short stone, short stoneBack)
		{
			this.money = money;
			this.stone = stone;
			this.stoneBack = stoneBack;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			money = is.popInteger();
			stone = is.popShort();
			stoneBack = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(money);
			os.pushShort(stone);
			os.pushShort(stoneBack);
		}

		public int money;
		public short stone;
		public short stoneBack;
	}

	public static class GeneralCmnCFGS implements Stream.IStreamable
	{

		public GeneralCmnCFGS() { }

		public GeneralCmnCFGS(List<Integer> skillInitPower, int skillUpPower, List<Integer> upgradeExp, List<GeneralEvoCFGS> evoLevels, 
		                      List<GeneralSkillCFGS> skills, List<Float> evoPowerScales, short commonSoulStoneId)
		{
			this.skillInitPower = skillInitPower;
			this.skillUpPower = skillUpPower;
			this.upgradeExp = upgradeExp;
			this.evoLevels = evoLevels;
			this.skills = skills;
			this.evoPowerScales = evoPowerScales;
			this.commonSoulStoneId = commonSoulStoneId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			skillInitPower = is.popIntegerList();
			skillUpPower = is.popInteger();
			upgradeExp = is.popIntegerList();
			evoLevels = is.popList(GeneralEvoCFGS.class);
			skills = is.popList(GeneralSkillCFGS.class);
			evoPowerScales = is.popFloatList();
			commonSoulStoneId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushIntegerList(skillInitPower);
			os.pushInteger(skillUpPower);
			os.pushIntegerList(upgradeExp);
			os.pushList(evoLevels);
			os.pushList(skills);
			os.pushFloatList(evoPowerScales);
			os.pushShort(commonSoulStoneId);
		}

		public List<Integer> skillInitPower;
		public int skillUpPower;
		public List<Integer> upgradeExp;
		public List<GeneralEvoCFGS> evoLevels;
		public List<GeneralSkillCFGS> skills;
		public List<Float> evoPowerScales;
		public short commonSoulStoneId;
	}

	public static class PetFriendExpItemCFGS implements Stream.IStreamable
	{

		public PetFriendExpItemCFGS() { }

		public PetFriendExpItemCFGS(short lvlCeil, short itemID, int itemCnt)
		{
			this.lvlCeil = lvlCeil;
			this.itemID = itemID;
			this.itemCnt = itemCnt;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlCeil = is.popShort();
			itemID = is.popShort();
			itemCnt = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlCeil);
			os.pushShort(itemID);
			os.pushInteger(itemCnt);
		}

		public short lvlCeil;
		public short itemID;
		public int itemCnt;
	}

	public static class PetCmnCFGS implements Stream.IStreamable
	{

		public PetCmnCFGS() { }

		public PetCmnCFGS(List<Integer> upgradeExp, List<Integer> feedingExp, short breedReqLvl, byte poundCapInit, 
		                  byte poundCapMax, short feedingFriendMax, int feedingFriendInterval, short feedingInterval, 
		                  int feedingMaxTime, List<Integer> poundUpgradeCost, List<PetFriendExpItemCFGS> friendExpItems, List<Short> deformFeedGrowth, 
		                  byte deformZanTimes, short deformZanGrowth, List<Short> deformZanVit, byte deformTakeOfferTimes, 
		                  short deformTakeOfferGrowth, List<Short> deformTakeOfferVit, short deformHappiness, byte deformFreeTryTimes, 
		                  byte deformBuyTryTimes, short deformBuyTryStone, short deformTryItemId, byte deformTryPoss, 
		                  byte deformFeedTimes)
		{
			this.upgradeExp = upgradeExp;
			this.feedingExp = feedingExp;
			this.breedReqLvl = breedReqLvl;
			this.poundCapInit = poundCapInit;
			this.poundCapMax = poundCapMax;
			this.feedingFriendMax = feedingFriendMax;
			this.feedingFriendInterval = feedingFriendInterval;
			this.feedingInterval = feedingInterval;
			this.feedingMaxTime = feedingMaxTime;
			this.poundUpgradeCost = poundUpgradeCost;
			this.friendExpItems = friendExpItems;
			this.deformFeedGrowth = deformFeedGrowth;
			this.deformZanTimes = deformZanTimes;
			this.deformZanGrowth = deformZanGrowth;
			this.deformZanVit = deformZanVit;
			this.deformTakeOfferTimes = deformTakeOfferTimes;
			this.deformTakeOfferGrowth = deformTakeOfferGrowth;
			this.deformTakeOfferVit = deformTakeOfferVit;
			this.deformHappiness = deformHappiness;
			this.deformFreeTryTimes = deformFreeTryTimes;
			this.deformBuyTryTimes = deformBuyTryTimes;
			this.deformBuyTryStone = deformBuyTryStone;
			this.deformTryItemId = deformTryItemId;
			this.deformTryPoss = deformTryPoss;
			this.deformFeedTimes = deformFeedTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			upgradeExp = is.popIntegerList();
			feedingExp = is.popIntegerList();
			breedReqLvl = is.popShort();
			poundCapInit = is.popByte();
			poundCapMax = is.popByte();
			feedingFriendMax = is.popShort();
			feedingFriendInterval = is.popInteger();
			feedingInterval = is.popShort();
			feedingMaxTime = is.popInteger();
			poundUpgradeCost = is.popIntegerList();
			friendExpItems = is.popList(PetFriendExpItemCFGS.class);
			deformFeedGrowth = is.popShortList();
			deformZanTimes = is.popByte();
			deformZanGrowth = is.popShort();
			deformZanVit = is.popShortList();
			deformTakeOfferTimes = is.popByte();
			deformTakeOfferGrowth = is.popShort();
			deformTakeOfferVit = is.popShortList();
			deformHappiness = is.popShort();
			deformFreeTryTimes = is.popByte();
			deformBuyTryTimes = is.popByte();
			deformBuyTryStone = is.popShort();
			deformTryItemId = is.popShort();
			deformTryPoss = is.popByte();
			deformFeedTimes = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushIntegerList(upgradeExp);
			os.pushIntegerList(feedingExp);
			os.pushShort(breedReqLvl);
			os.pushByte(poundCapInit);
			os.pushByte(poundCapMax);
			os.pushShort(feedingFriendMax);
			os.pushInteger(feedingFriendInterval);
			os.pushShort(feedingInterval);
			os.pushInteger(feedingMaxTime);
			os.pushIntegerList(poundUpgradeCost);
			os.pushList(friendExpItems);
			os.pushShortList(deformFeedGrowth);
			os.pushByte(deformZanTimes);
			os.pushShort(deformZanGrowth);
			os.pushShortList(deformZanVit);
			os.pushByte(deformTakeOfferTimes);
			os.pushShort(deformTakeOfferGrowth);
			os.pushShortList(deformTakeOfferVit);
			os.pushShort(deformHappiness);
			os.pushByte(deformFreeTryTimes);
			os.pushByte(deformBuyTryTimes);
			os.pushShort(deformBuyTryStone);
			os.pushShort(deformTryItemId);
			os.pushByte(deformTryPoss);
			os.pushByte(deformFeedTimes);
		}

		public List<Integer> upgradeExp;
		public List<Integer> feedingExp;
		public short breedReqLvl;
		public byte poundCapInit;
		public byte poundCapMax;
		public short feedingFriendMax;
		public int feedingFriendInterval;
		public short feedingInterval;
		public int feedingMaxTime;
		public List<Integer> poundUpgradeCost;
		public List<PetFriendExpItemCFGS> friendExpItems;
		public List<Short> deformFeedGrowth;
		public byte deformZanTimes;
		public short deformZanGrowth;
		public List<Short> deformZanVit;
		public byte deformTakeOfferTimes;
		public short deformTakeOfferGrowth;
		public List<Short> deformTakeOfferVit;
		public short deformHappiness;
		public byte deformFreeTryTimes;
		public byte deformBuyTryTimes;
		public short deformBuyTryStone;
		public short deformTryItemId;
		public byte deformTryPoss;
		public byte deformFeedTimes;
	}

	public static class CritCFGS implements Stream.IStreamable
	{

		public CritCFGS() { }

		public CritCFGS(float CritRate, byte CritMultiplier)
		{
			this.CritRate = CritRate;
			this.CritMultiplier = CritMultiplier;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			CritRate = is.popFloat();
			CritMultiplier = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(CritRate);
			os.pushByte(CritMultiplier);
		}

		public float CritRate;
		public byte CritMultiplier;
	}

	public static class RoleCmnCFGS implements Stream.IStreamable
	{

		public RoleCmnCFGS() { }

		public RoleCmnCFGS(List<Integer> upgradeExp, List<Short> vitMaxTable, List<Short> vitAddLvlup, int vitRecoverInterval, 
		                   int skillPointRecoverInterval, short skillPointMax00, short timeDayRefresh, short vitMax, 
		                   short buyMoneyMaxTimes00, int buyMoneyBaseVal, int buyMoneylvlVal, int buyMoneyTimesVal, 
		                   List<CritCFGS> buyMoneyCrit, List<Integer> buyMoneyPrice, short buyVitMaxTimes00, int buyVitVal, 
		                   List<Integer> buyVitPrice, short buySkillPointMaxTimes00, int buySkillPointVal00, List<Integer> buySkillPointPrice, 
		                   short chatLvlReq, short chatItemID, int changeNameCost, int changePetNameCost)
		{
			this.upgradeExp = upgradeExp;
			this.vitMaxTable = vitMaxTable;
			this.vitAddLvlup = vitAddLvlup;
			this.vitRecoverInterval = vitRecoverInterval;
			this.skillPointRecoverInterval = skillPointRecoverInterval;
			this.skillPointMax00 = skillPointMax00;
			this.timeDayRefresh = timeDayRefresh;
			this.vitMax = vitMax;
			this.buyMoneyMaxTimes00 = buyMoneyMaxTimes00;
			this.buyMoneyBaseVal = buyMoneyBaseVal;
			this.buyMoneylvlVal = buyMoneylvlVal;
			this.buyMoneyTimesVal = buyMoneyTimesVal;
			this.buyMoneyCrit = buyMoneyCrit;
			this.buyMoneyPrice = buyMoneyPrice;
			this.buyVitMaxTimes00 = buyVitMaxTimes00;
			this.buyVitVal = buyVitVal;
			this.buyVitPrice = buyVitPrice;
			this.buySkillPointMaxTimes00 = buySkillPointMaxTimes00;
			this.buySkillPointVal00 = buySkillPointVal00;
			this.buySkillPointPrice = buySkillPointPrice;
			this.chatLvlReq = chatLvlReq;
			this.chatItemID = chatItemID;
			this.changeNameCost = changeNameCost;
			this.changePetNameCost = changePetNameCost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			upgradeExp = is.popIntegerList();
			vitMaxTable = is.popShortList();
			vitAddLvlup = is.popShortList();
			vitRecoverInterval = is.popInteger();
			skillPointRecoverInterval = is.popInteger();
			skillPointMax00 = is.popShort();
			timeDayRefresh = is.popShort();
			vitMax = is.popShort();
			buyMoneyMaxTimes00 = is.popShort();
			buyMoneyBaseVal = is.popInteger();
			buyMoneylvlVal = is.popInteger();
			buyMoneyTimesVal = is.popInteger();
			buyMoneyCrit = is.popList(CritCFGS.class);
			buyMoneyPrice = is.popIntegerList();
			buyVitMaxTimes00 = is.popShort();
			buyVitVal = is.popInteger();
			buyVitPrice = is.popIntegerList();
			buySkillPointMaxTimes00 = is.popShort();
			buySkillPointVal00 = is.popInteger();
			buySkillPointPrice = is.popIntegerList();
			chatLvlReq = is.popShort();
			chatItemID = is.popShort();
			changeNameCost = is.popInteger();
			changePetNameCost = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushIntegerList(upgradeExp);
			os.pushShortList(vitMaxTable);
			os.pushShortList(vitAddLvlup);
			os.pushInteger(vitRecoverInterval);
			os.pushInteger(skillPointRecoverInterval);
			os.pushShort(skillPointMax00);
			os.pushShort(timeDayRefresh);
			os.pushShort(vitMax);
			os.pushShort(buyMoneyMaxTimes00);
			os.pushInteger(buyMoneyBaseVal);
			os.pushInteger(buyMoneylvlVal);
			os.pushInteger(buyMoneyTimesVal);
			os.pushList(buyMoneyCrit);
			os.pushIntegerList(buyMoneyPrice);
			os.pushShort(buyVitMaxTimes00);
			os.pushInteger(buyVitVal);
			os.pushIntegerList(buyVitPrice);
			os.pushShort(buySkillPointMaxTimes00);
			os.pushInteger(buySkillPointVal00);
			os.pushIntegerList(buySkillPointPrice);
			os.pushShort(chatLvlReq);
			os.pushShort(chatItemID);
			os.pushInteger(changeNameCost);
			os.pushInteger(changePetNameCost);
		}

		public List<Integer> upgradeExp;
		public List<Short> vitMaxTable;
		public List<Short> vitAddLvlup;
		public int vitRecoverInterval;
		public int skillPointRecoverInterval;
		public short skillPointMax00;
		public short timeDayRefresh;
		public short vitMax;
		public short buyMoneyMaxTimes00;
		public int buyMoneyBaseVal;
		public int buyMoneylvlVal;
		public int buyMoneyTimesVal;
		public List<CritCFGS> buyMoneyCrit;
		public List<Integer> buyMoneyPrice;
		public short buyVitMaxTimes00;
		public int buyVitVal;
		public List<Integer> buyVitPrice;
		public short buySkillPointMaxTimes00;
		public int buySkillPointVal00;
		public List<Integer> buySkillPointPrice;
		public short chatLvlReq;
		public short chatItemID;
		public int changeNameCost;
		public int changePetNameCost;
	}

	public static class CombatCmnCFGS implements Stream.IStreamable
	{

		public CombatCmnCFGS() { }

		public CombatCmnCFGS(short combatEventSkipItem, short marchSkipItem, List<Short> generalExpItemIDs, List<Integer> priceReset)
		{
			this.combatEventSkipItem = combatEventSkipItem;
			this.marchSkipItem = marchSkipItem;
			this.generalExpItemIDs = generalExpItemIDs;
			this.priceReset = priceReset;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatEventSkipItem = is.popShort();
			marchSkipItem = is.popShort();
			generalExpItemIDs = is.popShortList();
			priceReset = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatEventSkipItem);
			os.pushShort(marchSkipItem);
			os.pushShortList(generalExpItemIDs);
			os.pushIntegerList(priceReset);
		}

		public short combatEventSkipItem;
		public short marchSkipItem;
		public List<Short> generalExpItemIDs;
		public List<Integer> priceReset;
	}

	public static class EquipRankCFGS implements Stream.IStreamable
	{

		public EquipRankCFGS() { }

		public EquipRankCFGS(List<Short> gild)
		{
			this.gild = gild;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gild = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShortList(gild);
		}

		public List<Short> gild;
	}

	public static class EquipGildBackCFGS implements Stream.IStreamable
	{

		public EquipGildBackCFGS() { }

		public EquipGildBackCFGS(List<DropEntryNew> drops)
		{
			this.drops = drops;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			drops = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(drops);
		}

		public List<DropEntryNew> drops;
	}

	public static class EquipCmnCFGS implements Stream.IStreamable
	{

		public EquipCmnCFGS() { }

		public EquipCmnCFGS(List<EquipRankCFGS> rank, List<EquipGildBackCFGS> gildBack)
		{
			this.rank = rank;
			this.gildBack = gildBack;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rank = is.popList(EquipRankCFGS.class);
			gildBack = is.popList(EquipGildBackCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(rank);
			os.pushList(gildBack);
		}

		public List<EquipRankCFGS> rank;
		public List<EquipGildBackCFGS> gildBack;
	}

	public static class IconCFG implements Stream.IStreamable
	{

		public IconCFG() { }

		public IconCFG(short id, String path)
		{
			this.id = id;
			this.path = path;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			path = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(path);
		}

		public short id;
		public String path;
	}

	public static class ItemCFGS implements Stream.IStreamable
	{

		public ItemCFGS() { }

		public ItemCFGS(short id, String name, byte rank, short type, 
		                short lvlReq, int arg1, int arg2, int arg3, 
		                int price, int gildVal, byte extDropProp, byte extDropPropInc, 
		                byte extDropCool)
		{
			this.id = id;
			this.name = name;
			this.rank = rank;
			this.type = type;
			this.lvlReq = lvlReq;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.price = price;
			this.gildVal = gildVal;
			this.extDropProp = extDropProp;
			this.extDropPropInc = extDropPropInc;
			this.extDropCool = extDropCool;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			rank = is.popByte();
			type = is.popShort();
			lvlReq = is.popShort();
			arg1 = is.popInteger();
			arg2 = is.popInteger();
			arg3 = is.popInteger();
			price = is.popInteger();
			gildVal = is.popInteger();
			extDropProp = is.popByte();
			extDropPropInc = is.popByte();
			extDropCool = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushByte(rank);
			os.pushShort(type);
			os.pushShort(lvlReq);
			os.pushInteger(arg1);
			os.pushInteger(arg2);
			os.pushInteger(arg3);
			os.pushInteger(price);
			os.pushInteger(gildVal);
			os.pushByte(extDropProp);
			os.pushByte(extDropPropInc);
			os.pushByte(extDropCool);
		}

		public short id;
		public String name;
		public byte rank;
		public short type;
		public short lvlReq;
		public int arg1;
		public int arg2;
		public int arg3;
		public int price;
		public int gildVal;
		public byte extDropProp;
		public byte extDropPropInc;
		public byte extDropCool;
	}

	public static class EquipEffect implements Stream.IStreamable
	{

		public EquipEffect() { }

		public EquipEffect(byte effectID, float arg)
		{
			this.effectID = effectID;
			this.arg = arg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			effectID = is.popByte();
			arg = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(effectID);
			os.pushFloat(arg);
		}

		public byte effectID;
		public float arg;
	}

	public static class EquipCFGS implements Stream.IStreamable
	{

		public EquipCFGS() { }

		public EquipCFGS(short id, String name, byte rank, byte type, 
		                 short lvlReq, int price, int gildPrice, int gildVal, 
		                 int combineMoney, List<DropEntry> combinePieces, List<Integer> powers)
		{
			this.id = id;
			this.name = name;
			this.rank = rank;
			this.type = type;
			this.lvlReq = lvlReq;
			this.price = price;
			this.gildPrice = gildPrice;
			this.gildVal = gildVal;
			this.combineMoney = combineMoney;
			this.combinePieces = combinePieces;
			this.powers = powers;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			rank = is.popByte();
			type = is.popByte();
			lvlReq = is.popShort();
			price = is.popInteger();
			gildPrice = is.popInteger();
			gildVal = is.popInteger();
			combineMoney = is.popInteger();
			combinePieces = is.popList(DropEntry.class);
			powers = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushByte(rank);
			os.pushByte(type);
			os.pushShort(lvlReq);
			os.pushInteger(price);
			os.pushInteger(gildPrice);
			os.pushInteger(gildVal);
			os.pushInteger(combineMoney);
			os.pushList(combinePieces);
			os.pushIntegerList(powers);
		}

		public short id;
		public String name;
		public byte rank;
		public byte type;
		public short lvlReq;
		public int price;
		public int gildPrice;
		public int gildVal;
		public int combineMoney;
		public List<DropEntry> combinePieces;
		public List<Integer> powers;
	}

	public static class ShopGoods implements Stream.IStreamable
	{

		public ShopGoods() { }

		public ShopGoods(short id, byte curUnit, int price, float pro, 
		                 DropEntry item)
		{
			this.id = id;
			this.curUnit = curUnit;
			this.price = price;
			this.pro = pro;
			this.item = item;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			curUnit = is.popByte();
			price = is.popInteger();
			pro = is.popFloat();
			if( item == null )
				item = new DropEntry();
			is.pop(item);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(curUnit);
			os.pushInteger(price);
			os.pushFloat(pro);
			os.push(item);
		}

		public short id;
		public byte curUnit;
		public int price;
		public float pro;
		public DropEntry item;
	}

	public static class TSSAntiData implements Stream.IStreamable
	{

		public TSSAntiData() { }

		public TSSAntiData(ByteBuffer anti_data)
		{
			this.anti_data = anti_data;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			anti_data = is.popByteBuffer();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByteBuffer(anti_data);
		}

		public ByteBuffer anti_data;
	}

	public static class SysMail implements Stream.IStreamable
	{

		public SysMail() { }

		public SysMail(int id, short lvlReq, short vipReq, int timeStart, 
		               int lifeTime, int flag, String title, String content, 
		               List<DropEntryNew> att)
		{
			this.id = id;
			this.lvlReq = lvlReq;
			this.vipReq = vipReq;
			this.timeStart = timeStart;
			this.lifeTime = lifeTime;
			this.flag = flag;
			this.title = title;
			this.content = content;
			this.att = att;
		}

		public SysMail ksClone()
		{
			return new SysMail(id, lvlReq, vipReq, timeStart, 
			                   lifeTime, flag, title, content, 
			                   att);
		}

		public SysMail kdClone()
		{
			SysMail _kio_clobj = ksClone();
			_kio_clobj.att = new ArrayList<DropEntryNew>();
			for(DropEntryNew _kio_iter : att)
			{
				_kio_clobj.att.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			lvlReq = is.popShort();
			vipReq = is.popShort();
			timeStart = is.popInteger();
			lifeTime = is.popInteger();
			flag = is.popInteger();
			title = is.popString();
			content = is.popString();
			att = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(lvlReq);
			os.pushShort(vipReq);
			os.pushInteger(timeStart);
			os.pushInteger(lifeTime);
			os.pushInteger(flag);
			os.pushString(title);
			os.pushString(content);
			os.pushList(att);
		}

		public int id;
		public short lvlReq;
		public short vipReq;
		public int timeStart;
		public int lifeTime;
		public int flag;
		public String title;
		public String content;
		public List<DropEntryNew> att;
	}

	public static class Broadcast implements Stream.IStreamable
	{

		public Broadcast() { }

		public Broadcast(int id, int sendTime, int times, int freq, 
		                 int timeStart, int lifeTime, String content)
		{
			this.id = id;
			this.sendTime = sendTime;
			this.times = times;
			this.freq = freq;
			this.timeStart = timeStart;
			this.lifeTime = lifeTime;
			this.content = content;
		}

		public Broadcast ksClone()
		{
			return new Broadcast(id, sendTime, times, freq, 
			                     timeStart, lifeTime, content);
		}

		public Broadcast kdClone()
		{
			Broadcast _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			sendTime = is.popInteger();
			times = is.popInteger();
			freq = is.popInteger();
			timeStart = is.popInteger();
			lifeTime = is.popInteger();
			content = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(sendTime);
			os.pushInteger(times);
			os.pushInteger(freq);
			os.pushInteger(timeStart);
			os.pushInteger(lifeTime);
			os.pushString(content);
		}

		public int id;
		public int sendTime;
		public int times;
		public int freq;
		public int timeStart;
		public int lifeTime;
		public String content;
	}

	public static class CDKeyLog implements Stream.IStreamable
	{

		public CDKeyLog() { }

		public CDKeyLog(int roleID, int batchID, int genID, String key)
		{
			this.roleID = roleID;
			this.batchID = batchID;
			this.genID = genID;
			this.key = key;
		}

		public CDKeyLog ksClone()
		{
			return new CDKeyLog(roleID, batchID, genID, key);
		}

		public CDKeyLog kdClone()
		{
			CDKeyLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleID = is.popInteger();
			batchID = is.popInteger();
			genID = is.popInteger();
			key = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(roleID);
			os.pushInteger(batchID);
			os.pushInteger(genID);
			os.pushString(key);
		}

		public int roleID;
		public int batchID;
		public int genID;
		public String key;
	}

	public static class TopLevelRoleInfo implements Stream.IStreamable
	{

		public TopLevelRoleInfo() { }

		public TopLevelRoleInfo(int level, int roleID, String roleName)
		{
			this.level = level;
			this.roleID = roleID;
			this.roleName = roleName;
		}

		public TopLevelRoleInfo ksClone()
		{
			return new TopLevelRoleInfo(level, roleID, roleName);
		}

		public TopLevelRoleInfo kdClone()
		{
			TopLevelRoleInfo _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			level = is.popInteger();
			roleID = is.popInteger();
			roleName = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(level);
			os.pushInteger(roleID);
			os.pushString(roleName);
		}

		public int level;
		public int roleID;
		public String roleName;
	}

	public static class CityProduceCFGS implements Stream.IStreamable
	{

		public CityProduceCFGS() { }

		public CityProduceCFGS(byte type, short id, List<Integer> produce)
		{
			this.type = type;
			this.id = id;
			this.produce = produce;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			id = is.popShort();
			produce = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(id);
			os.pushIntegerList(produce);
		}

		public byte type;
		public short id;
		public List<Integer> produce;
	}

	public static class CityCFGS implements Stream.IStreamable
	{

		public CityCFGS() { }

		public CityCFGS(short id, String name, int combatIDReq, int searchLvlReq, 
		                int searchedLvlMin, int searchedLvlMax, List<CityProduceCFGS> produce)
		{
			this.id = id;
			this.name = name;
			this.combatIDReq = combatIDReq;
			this.searchLvlReq = searchLvlReq;
			this.searchedLvlMin = searchedLvlMin;
			this.searchedLvlMax = searchedLvlMax;
			this.produce = produce;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			combatIDReq = is.popInteger();
			searchLvlReq = is.popInteger();
			searchedLvlMin = is.popInteger();
			searchedLvlMax = is.popInteger();
			produce = is.popList(CityProduceCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushInteger(combatIDReq);
			os.pushInteger(searchLvlReq);
			os.pushInteger(searchedLvlMin);
			os.pushInteger(searchedLvlMax);
			os.pushList(produce);
		}

		public short id;
		public String name;
		public int combatIDReq;
		public int searchLvlReq;
		public int searchedLvlMin;
		public int searchedLvlMax;
		public List<CityProduceCFGS> produce;
	}

	public static class CitySearchLevelCFGS implements Stream.IStreamable
	{

		public CitySearchLevelCFGS() { }

		public CitySearchLevelCFGS(int lvlDiffMin, int lvlDiffMax)
		{
			this.lvlDiffMin = lvlDiffMin;
			this.lvlDiffMax = lvlDiffMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlDiffMin = is.popInteger();
			lvlDiffMax = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(lvlDiffMin);
			os.pushInteger(lvlDiffMax);
		}

		public int lvlDiffMin;
		public int lvlDiffMax;
	}

	public static class CaptureCityCFGS implements Stream.IStreamable
	{

		public CaptureCityCFGS() { }

		public CaptureCityCFGS(List<CityCFGS> citys, int searchOpenLevel, int searchCostMoney, List<CitySearchLevelCFGS> searchTypeLvlRanges, 
		                       List<Integer> searchTypeSequence, List<Integer> guardTimes, float captureProduceRatio, float lostProduceRatio, 
		                       float counterattackProduceRatio, short baseRewardTime)
		{
			this.citys = citys;
			this.searchOpenLevel = searchOpenLevel;
			this.searchCostMoney = searchCostMoney;
			this.searchTypeLvlRanges = searchTypeLvlRanges;
			this.searchTypeSequence = searchTypeSequence;
			this.guardTimes = guardTimes;
			this.captureProduceRatio = captureProduceRatio;
			this.lostProduceRatio = lostProduceRatio;
			this.counterattackProduceRatio = counterattackProduceRatio;
			this.baseRewardTime = baseRewardTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			citys = is.popList(CityCFGS.class);
			searchOpenLevel = is.popInteger();
			searchCostMoney = is.popInteger();
			searchTypeLvlRanges = is.popList(CitySearchLevelCFGS.class);
			searchTypeSequence = is.popIntegerList();
			guardTimes = is.popIntegerList();
			captureProduceRatio = is.popFloat();
			lostProduceRatio = is.popFloat();
			counterattackProduceRatio = is.popFloat();
			baseRewardTime = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(citys);
			os.pushInteger(searchOpenLevel);
			os.pushInteger(searchCostMoney);
			os.pushList(searchTypeLvlRanges);
			os.pushIntegerList(searchTypeSequence);
			os.pushIntegerList(guardTimes);
			os.pushFloat(captureProduceRatio);
			os.pushFloat(lostProduceRatio);
			os.pushFloat(counterattackProduceRatio);
			os.pushShort(baseRewardTime);
		}

		public List<CityCFGS> citys;
		public int searchOpenLevel;
		public int searchCostMoney;
		public List<CitySearchLevelCFGS> searchTypeLvlRanges;
		public List<Integer> searchTypeSequence;
		public List<Integer> guardTimes;
		public float captureProduceRatio;
		public float lostProduceRatio;
		public float counterattackProduceRatio;
		public short baseRewardTime;
	}

	public static class DBCityGuardTimeLog implements Stream.IStreamable
	{

		public DBCityGuardTimeLog() { }

		public DBCityGuardTimeLog(int normalGuardTime, int lostGuardTime)
		{
			this.normalGuardTime = normalGuardTime;
			this.lostGuardTime = lostGuardTime;
		}

		public DBCityGuardTimeLog ksClone()
		{
			return new DBCityGuardTimeLog(normalGuardTime, lostGuardTime);
		}

		public DBCityGuardTimeLog kdClone()
		{
			DBCityGuardTimeLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			normalGuardTime = is.popInteger();
			lostGuardTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(normalGuardTime);
			os.pushInteger(lostGuardTime);
		}

		public int normalGuardTime;
		public int lostGuardTime;
	}

	public static class DBBaseCity implements Stream.IStreamable
	{

		public DBBaseCity() { }

		public DBBaseCity(int baseGSID, int baseRoleID, String baseRoleName, int cityID, 
		                  int openTime, int ownerGSID, int ownerRoleID, String ownerRoleName, 
		                  int state, int guardStartTime, int guardLifeTime, int lastBalanceTime, 
		                  List<Short> guardGenerals, List<Short> guardPets, List<Integer> guardTimeLog, int attackGSID, 
		                  int attackRoleID, int attackTime, List<DropEntryNew> totalIncome)
		{
			this.baseGSID = baseGSID;
			this.baseRoleID = baseRoleID;
			this.baseRoleName = baseRoleName;
			this.cityID = cityID;
			this.openTime = openTime;
			this.ownerGSID = ownerGSID;
			this.ownerRoleID = ownerRoleID;
			this.ownerRoleName = ownerRoleName;
			this.state = state;
			this.guardStartTime = guardStartTime;
			this.guardLifeTime = guardLifeTime;
			this.lastBalanceTime = lastBalanceTime;
			this.guardGenerals = guardGenerals;
			this.guardPets = guardPets;
			this.guardTimeLog = guardTimeLog;
			this.attackGSID = attackGSID;
			this.attackRoleID = attackRoleID;
			this.attackTime = attackTime;
			this.totalIncome = totalIncome;
		}

		public DBBaseCity ksClone()
		{
			return new DBBaseCity(baseGSID, baseRoleID, baseRoleName, cityID, 
			                      openTime, ownerGSID, ownerRoleID, ownerRoleName, 
			                      state, guardStartTime, guardLifeTime, lastBalanceTime, 
			                      guardGenerals, guardPets, guardTimeLog, attackGSID, 
			                      attackRoleID, attackTime, totalIncome);
		}

		public DBBaseCity kdClone()
		{
			DBBaseCity _kio_clobj = ksClone();
			_kio_clobj.guardGenerals = new ArrayList<Short>();
			for(Short _kio_iter : guardGenerals)
			{
				_kio_clobj.guardGenerals.add(_kio_iter);
			}
			_kio_clobj.guardPets = new ArrayList<Short>();
			for(Short _kio_iter : guardPets)
			{
				_kio_clobj.guardPets.add(_kio_iter);
			}
			_kio_clobj.guardTimeLog = new ArrayList<Integer>();
			for(Integer _kio_iter : guardTimeLog)
			{
				_kio_clobj.guardTimeLog.add(_kio_iter);
			}
			_kio_clobj.totalIncome = new ArrayList<DropEntryNew>();
			for(DropEntryNew _kio_iter : totalIncome)
			{
				_kio_clobj.totalIncome.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			baseGSID = is.popInteger();
			baseRoleID = is.popInteger();
			baseRoleName = is.popString();
			cityID = is.popInteger();
			openTime = is.popInteger();
			ownerGSID = is.popInteger();
			ownerRoleID = is.popInteger();
			ownerRoleName = is.popString();
			state = is.popInteger();
			guardStartTime = is.popInteger();
			guardLifeTime = is.popInteger();
			lastBalanceTime = is.popInteger();
			guardGenerals = is.popShortList();
			guardPets = is.popShortList();
			guardTimeLog = is.popIntegerList();
			attackGSID = is.popInteger();
			attackRoleID = is.popInteger();
			attackTime = is.popInteger();
			totalIncome = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(baseGSID);
			os.pushInteger(baseRoleID);
			os.pushString(baseRoleName);
			os.pushInteger(cityID);
			os.pushInteger(openTime);
			os.pushInteger(ownerGSID);
			os.pushInteger(ownerRoleID);
			os.pushString(ownerRoleName);
			os.pushInteger(state);
			os.pushInteger(guardStartTime);
			os.pushInteger(guardLifeTime);
			os.pushInteger(lastBalanceTime);
			os.pushShortList(guardGenerals);
			os.pushShortList(guardPets);
			os.pushIntegerList(guardTimeLog);
			os.pushInteger(attackGSID);
			os.pushInteger(attackRoleID);
			os.pushInteger(attackTime);
			os.pushList(totalIncome);
		}

		public int baseGSID;
		public int baseRoleID;
		public String baseRoleName;
		public int cityID;
		public int openTime;
		public int ownerGSID;
		public int ownerRoleID;
		public String ownerRoleName;
		public int state;
		public int guardStartTime;
		public int guardLifeTime;
		public int lastBalanceTime;
		public List<Short> guardGenerals;
		public List<Short> guardPets;
		public List<Integer> guardTimeLog;
		public int attackGSID;
		public int attackRoleID;
		public int attackTime;
		public List<DropEntryNew> totalIncome;
	}

	public static class DBCityReward implements Stream.IStreamable
	{

		public DBCityReward() { }

		public DBCityReward(int baseGSID, int baseRoleID, String baseRoleName, int cityID, 
		                    int guardStartTime, int guardLifeTime, List<DropEntryNew> totalIncome)
		{
			this.baseGSID = baseGSID;
			this.baseRoleID = baseRoleID;
			this.baseRoleName = baseRoleName;
			this.cityID = cityID;
			this.guardStartTime = guardStartTime;
			this.guardLifeTime = guardLifeTime;
			this.totalIncome = totalIncome;
		}

		public DBCityReward ksClone()
		{
			return new DBCityReward(baseGSID, baseRoleID, baseRoleName, cityID, 
			                        guardStartTime, guardLifeTime, totalIncome);
		}

		public DBCityReward kdClone()
		{
			DBCityReward _kio_clobj = ksClone();
			_kio_clobj.totalIncome = new ArrayList<DropEntryNew>();
			for(DropEntryNew _kio_iter : totalIncome)
			{
				_kio_clobj.totalIncome.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			baseGSID = is.popInteger();
			baseRoleID = is.popInteger();
			baseRoleName = is.popString();
			cityID = is.popInteger();
			guardStartTime = is.popInteger();
			guardLifeTime = is.popInteger();
			totalIncome = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(baseGSID);
			os.pushInteger(baseRoleID);
			os.pushString(baseRoleName);
			os.pushInteger(cityID);
			os.pushInteger(guardStartTime);
			os.pushInteger(guardLifeTime);
			os.pushList(totalIncome);
		}

		public int baseGSID;
		public int baseRoleID;
		public String baseRoleName;
		public int cityID;
		public int guardStartTime;
		public int guardLifeTime;
		public List<DropEntryNew> totalIncome;
	}

	public static class DBCitys implements Stream.IStreamable
	{

		public DBCitys() { }

		public DBCitys(List<DBBaseCity> baseCitys, List<DBBaseCity> occupiedCitys, List<DBCityReward> occupiedCitysRewardLog, int lastBaseRewardTime)
		{
			this.baseCitys = baseCitys;
			this.occupiedCitys = occupiedCitys;
			this.occupiedCitysRewardLog = occupiedCitysRewardLog;
			this.lastBaseRewardTime = lastBaseRewardTime;
		}

		public DBCitys ksClone()
		{
			return new DBCitys(baseCitys, occupiedCitys, occupiedCitysRewardLog, lastBaseRewardTime);
		}

		public DBCitys kdClone()
		{
			DBCitys _kio_clobj = ksClone();
			_kio_clobj.baseCitys = new ArrayList<DBBaseCity>();
			for(DBBaseCity _kio_iter : baseCitys)
			{
				_kio_clobj.baseCitys.add(_kio_iter.kdClone());
			}
			_kio_clobj.occupiedCitys = new ArrayList<DBBaseCity>();
			for(DBBaseCity _kio_iter : occupiedCitys)
			{
				_kio_clobj.occupiedCitys.add(_kio_iter.kdClone());
			}
			_kio_clobj.occupiedCitysRewardLog = new ArrayList<DBCityReward>();
			for(DBCityReward _kio_iter : occupiedCitysRewardLog)
			{
				_kio_clobj.occupiedCitysRewardLog.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			baseCitys = is.popList(DBBaseCity.class);
			occupiedCitys = is.popList(DBBaseCity.class);
			occupiedCitysRewardLog = is.popList(DBCityReward.class);
			lastBaseRewardTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(baseCitys);
			os.pushList(occupiedCitys);
			os.pushList(occupiedCitysRewardLog);
			os.pushInteger(lastBaseRewardTime);
		}

		public List<DBBaseCity> baseCitys;
		public List<DBBaseCity> occupiedCitys;
		public List<DBCityReward> occupiedCitysRewardLog;
		public int lastBaseRewardTime;
	}

	public static class CityInfo implements Stream.IStreamable
	{

		public CityInfo() { }

		public CityInfo(GlobalRoleID baseRoleID, String baseRoleName, int cityID, GlobalRoleID ownerRoleID, 
		                String ownerRoleName, int guardStartTime, int guardLifeTime, List<i3k.DBRoleGeneral> guardGenerals, 
		                List<DBPetBrief> guardPets)
		{
			this.baseRoleID = baseRoleID;
			this.baseRoleName = baseRoleName;
			this.cityID = cityID;
			this.ownerRoleID = ownerRoleID;
			this.ownerRoleName = ownerRoleName;
			this.guardStartTime = guardStartTime;
			this.guardLifeTime = guardLifeTime;
			this.guardGenerals = guardGenerals;
			this.guardPets = guardPets;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( baseRoleID == null )
				baseRoleID = new GlobalRoleID();
			is.pop(baseRoleID);
			baseRoleName = is.popString();
			cityID = is.popInteger();
			if( ownerRoleID == null )
				ownerRoleID = new GlobalRoleID();
			is.pop(ownerRoleID);
			ownerRoleName = is.popString();
			guardStartTime = is.popInteger();
			guardLifeTime = is.popInteger();
			guardGenerals = is.popList(i3k.DBRoleGeneral.class);
			guardPets = is.popList(DBPetBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(baseRoleID);
			os.pushString(baseRoleName);
			os.pushInteger(cityID);
			os.push(ownerRoleID);
			os.pushString(ownerRoleName);
			os.pushInteger(guardStartTime);
			os.pushInteger(guardLifeTime);
			os.pushList(guardGenerals);
			os.pushList(guardPets);
		}

		public GlobalRoleID baseRoleID;
		public String baseRoleName;
		public int cityID;
		public GlobalRoleID ownerRoleID;
		public String ownerRoleName;
		public int guardStartTime;
		public int guardLifeTime;
		public List<i3k.DBRoleGeneral> guardGenerals;
		public List<DBPetBrief> guardPets;
	}

	public static class DBBanData implements Stream.IStreamable
	{

		public DBBanData() { }

		public DBBanData(int banTypeID, int banEndTime, String banReason)
		{
			this.banTypeID = banTypeID;
			this.banEndTime = banEndTime;
			this.banReason = banReason;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			banTypeID = is.popInteger();
			banEndTime = is.popInteger();
			banReason = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(banTypeID);
			os.pushInteger(banEndTime);
			os.pushString(banReason);
		}

		public int banTypeID;
		public int banEndTime;
		public String banReason;
	}

	public static class ShopGoodsGroup implements Stream.IStreamable
	{

		public ShopGoodsGroup() { }

		public ShopGoodsGroup(List<ShopGoods> goods)
		{
			this.goods = goods;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			goods = is.popList(ShopGoods.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(goods);
		}

		public List<ShopGoods> goods;
	}

	public static class ShopGoodsLevelCFGS implements Stream.IStreamable
	{

		public ShopGoodsLevelCFGS() { }

		public ShopGoodsLevelCFGS(short lvlCeil, List<ShopGoodsGroup> groups)
		{
			this.lvlCeil = lvlCeil;
			this.groups = groups;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlCeil = is.popShort();
			groups = is.popList(ShopGoodsGroup.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlCeil);
			os.pushList(groups);
		}

		public short lvlCeil;
		public List<ShopGoodsGroup> groups;
	}

	public static class ShopCFGS implements Stream.IStreamable
	{

		public ShopCFGS() { }

		public ShopCFGS(byte type, byte refreshCount, short lvlReq, byte lvlVIPReq, 
		                byte refreshCurUnit, List<Integer> refreshPrice, List<Short> refreshTime, List<ShopGoodsLevelCFGS> levels, 
		                float randSummonPro, int randSummonTime, int randSummonStone, short randSummonVit, 
		                short randSummonCID)
		{
			this.type = type;
			this.refreshCount = refreshCount;
			this.lvlReq = lvlReq;
			this.lvlVIPReq = lvlVIPReq;
			this.refreshCurUnit = refreshCurUnit;
			this.refreshPrice = refreshPrice;
			this.refreshTime = refreshTime;
			this.levels = levels;
			this.randSummonPro = randSummonPro;
			this.randSummonTime = randSummonTime;
			this.randSummonStone = randSummonStone;
			this.randSummonVit = randSummonVit;
			this.randSummonCID = randSummonCID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			refreshCount = is.popByte();
			lvlReq = is.popShort();
			lvlVIPReq = is.popByte();
			refreshCurUnit = is.popByte();
			refreshPrice = is.popIntegerList();
			refreshTime = is.popShortList();
			levels = is.popList(ShopGoodsLevelCFGS.class);
			randSummonPro = is.popFloat();
			randSummonTime = is.popInteger();
			randSummonStone = is.popInteger();
			randSummonVit = is.popShort();
			randSummonCID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushByte(refreshCount);
			os.pushShort(lvlReq);
			os.pushByte(lvlVIPReq);
			os.pushByte(refreshCurUnit);
			os.pushIntegerList(refreshPrice);
			os.pushShortList(refreshTime);
			os.pushList(levels);
			os.pushFloat(randSummonPro);
			os.pushInteger(randSummonTime);
			os.pushInteger(randSummonStone);
			os.pushShort(randSummonVit);
			os.pushShort(randSummonCID);
		}

		public byte type;
		public byte refreshCount;
		public short lvlReq;
		public byte lvlVIPReq;
		public byte refreshCurUnit;
		public List<Integer> refreshPrice;
		public List<Short> refreshTime;
		public List<ShopGoodsLevelCFGS> levels;
		public float randSummonPro;
		public int randSummonTime;
		public int randSummonStone;
		public short randSummonVit;
		public short randSummonCID;
	}

	public static class DlgTableEntry implements Stream.IStreamable
	{

		public DlgTableEntry() { }

		public DlgTableEntry(String dlg, float pro)
		{
			this.dlg = dlg;
			this.pro = pro;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			dlg = is.popString();
			pro = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(dlg);
			os.pushFloat(pro);
		}

		public String dlg;
		public float pro;
	}

	public static class DlgTableCFG implements Stream.IStreamable
	{

		public DlgTableCFG() { }

		public DlgTableCFG(short id, short iconID, List<DlgTableEntry> entryList)
		{
			this.id = id;
			this.iconID = iconID;
			this.entryList = entryList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			iconID = is.popShort();
			entryList = is.popList(DlgTableEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(iconID);
			os.pushList(entryList);
		}

		public short id;
		public short iconID;
		public List<DlgTableEntry> entryList;
	}

	public static class TipsCFG implements Stream.IStreamable
	{

		public TipsCFG() { }

		public TipsCFG(short id, short effID, byte seq, byte count, 
		               String title, String content)
		{
			this.id = id;
			this.effID = effID;
			this.seq = seq;
			this.count = count;
			this.title = title;
			this.content = content;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			effID = is.popShort();
			seq = is.popByte();
			count = is.popByte();
			title = is.popString();
			content = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(effID);
			os.pushByte(seq);
			os.pushByte(count);
			os.pushString(title);
			os.pushString(content);
		}

		public short id;
		public short effID;
		public byte seq;
		public byte count;
		public String title;
		public String content;
	}

	public static class DropTableEntry implements Stream.IStreamable
	{

		public DropTableEntry() { }

		public DropTableEntry(DropEntry item, float pro)
		{
			this.item = item;
			this.pro = pro;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( item == null )
				item = new DropEntry();
			is.pop(item);
			pro = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(item);
			os.pushFloat(pro);
		}

		public DropEntry item;
		public float pro;
	}

	public static class DropTableCFGS implements Stream.IStreamable
	{

		public DropTableCFGS() { }

		public DropTableCFGS(short id, List<DropEntry> srcList, List<DropTableEntry> entryList)
		{
			this.id = id;
			this.srcList = srcList;
			this.entryList = entryList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			srcList = is.popList(DropEntry.class);
			entryList = is.popList(DropTableEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushList(srcList);
			os.pushList(entryList);
		}

		public short id;
		public List<DropEntry> srcList;
		public List<DropTableEntry> entryList;
	}

	public static class DailyTask2CFGS implements Stream.IStreamable
	{

		public DailyTask2CFGS() { }

		public DailyTask2CFGS(byte id, short lvlReq, byte times, byte reward1Type, 
		                      short reward1ID, int reward1Arg1, int reward1Arg2, byte reward2Type, 
		                      short reward2ID, int reward2Arg1, int reward2Arg2, int activity)
		{
			this.id = id;
			this.lvlReq = lvlReq;
			this.times = times;
			this.reward1Type = reward1Type;
			this.reward1ID = reward1ID;
			this.reward1Arg1 = reward1Arg1;
			this.reward1Arg2 = reward1Arg2;
			this.reward2Type = reward2Type;
			this.reward2ID = reward2ID;
			this.reward2Arg1 = reward2Arg1;
			this.reward2Arg2 = reward2Arg2;
			this.activity = activity;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			lvlReq = is.popShort();
			times = is.popByte();
			reward1Type = is.popByte();
			reward1ID = is.popShort();
			reward1Arg1 = is.popInteger();
			reward1Arg2 = is.popInteger();
			reward2Type = is.popByte();
			reward2ID = is.popShort();
			reward2Arg1 = is.popInteger();
			reward2Arg2 = is.popInteger();
			activity = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushShort(lvlReq);
			os.pushByte(times);
			os.pushByte(reward1Type);
			os.pushShort(reward1ID);
			os.pushInteger(reward1Arg1);
			os.pushInteger(reward1Arg2);
			os.pushByte(reward2Type);
			os.pushShort(reward2ID);
			os.pushInteger(reward2Arg1);
			os.pushInteger(reward2Arg2);
			os.pushInteger(activity);
		}

		public byte id;
		public short lvlReq;
		public byte times;
		public byte reward1Type;
		public short reward1ID;
		public int reward1Arg1;
		public int reward1Arg2;
		public byte reward2Type;
		public short reward2ID;
		public int reward2Arg1;
		public int reward2Arg2;
		public int activity;
	}

	public static class DailyTask2CFGC implements Stream.IStreamable
	{

		public DailyTask2CFGC() { }

		public DailyTask2CFGC(DailyTask2CFGS cfgs, short iconID, String name, String des)
		{
			this.cfgs = cfgs;
			this.iconID = iconID;
			this.name = name;
			this.des = des;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( cfgs == null )
				cfgs = new DailyTask2CFGS();
			is.pop(cfgs);
			iconID = is.popShort();
			name = is.popString();
			des = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(cfgs);
			os.pushShort(iconID);
			os.pushString(name);
			os.pushString(des);
		}

		public DailyTask2CFGS cfgs;
		public short iconID;
		public String name;
		public String des;
	}

	public static class DailyActivityRewardCFGS implements Stream.IStreamable
	{

		public DailyActivityRewardCFGS() { }

		public DailyActivityRewardCFGS(int activity, String desc, short icon, List<DropEntryNew> drops)
		{
			this.activity = activity;
			this.desc = desc;
			this.icon = icon;
			this.drops = drops;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			activity = is.popInteger();
			desc = is.popString();
			icon = is.popShort();
			drops = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(activity);
			os.pushString(desc);
			os.pushShort(icon);
			os.pushList(drops);
		}

		public int activity;
		public String desc;
		public short icon;
		public List<DropEntryNew> drops;
	}

	public static class QQVIPRewardsCFGS implements Stream.IStreamable
	{

		public QQVIPRewardsCFGS() { }

		public QQVIPRewardsCFGS(List<DropEntryNew> rewards)
		{
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rewards = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(rewards);
		}

		public List<DropEntryNew> rewards;
	}

	public static class TopLevelAuraCFGS implements Stream.IStreamable
	{

		public TopLevelAuraCFGS() { }

		public TopLevelAuraCFGS(int sampleCount, int lessThan20ExpAdd, int lessThan40ExpAdd)
		{
			this.sampleCount = sampleCount;
			this.lessThan20ExpAdd = lessThan20ExpAdd;
			this.lessThan40ExpAdd = lessThan40ExpAdd;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sampleCount = is.popInteger();
			lessThan20ExpAdd = is.popInteger();
			lessThan40ExpAdd = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(sampleCount);
			os.pushInteger(lessThan20ExpAdd);
			os.pushInteger(lessThan40ExpAdd);
		}

		public int sampleCount;
		public int lessThan20ExpAdd;
		public int lessThan40ExpAdd;
	}

	public static class EggRankCFGS implements Stream.IStreamable
	{

		public EggRankCFGS() { }

		public EggRankCFGS(int porg, List<DropTableEntry> entryList)
		{
			this.porg = porg;
			this.entryList = entryList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			porg = is.popInteger();
			entryList = is.popList(DropTableEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(porg);
			os.pushList(entryList);
		}

		public int porg;
		public List<DropTableEntry> entryList;
	}

	public static class EggCFGS implements Stream.IStreamable
	{

		public EggCFGS() { }

		public EggCFGS(byte id, int cost, byte freeTimes, short freeCool, 
		               short firstDropTableID, short firstFreeDropTableID, short itemDropTableID, short generalDropTableID, 
		               int poolLow, int poolHigh, int generalUp, int itemDown, 
		               float propGeneral, int cost10, byte minGeneral10, byte maxGeneral10, 
		               byte proectTimes10, DropEntry itemFixed, List<Short> dropTableIDsFirst10, List<Integer> cost10FirstBuy, 
		               short dropTableIDs100)
		{
			this.id = id;
			this.cost = cost;
			this.freeTimes = freeTimes;
			this.freeCool = freeCool;
			this.firstDropTableID = firstDropTableID;
			this.firstFreeDropTableID = firstFreeDropTableID;
			this.itemDropTableID = itemDropTableID;
			this.generalDropTableID = generalDropTableID;
			this.poolLow = poolLow;
			this.poolHigh = poolHigh;
			this.generalUp = generalUp;
			this.itemDown = itemDown;
			this.propGeneral = propGeneral;
			this.cost10 = cost10;
			this.minGeneral10 = minGeneral10;
			this.maxGeneral10 = maxGeneral10;
			this.proectTimes10 = proectTimes10;
			this.itemFixed = itemFixed;
			this.dropTableIDsFirst10 = dropTableIDsFirst10;
			this.cost10FirstBuy = cost10FirstBuy;
			this.dropTableIDs100 = dropTableIDs100;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			cost = is.popInteger();
			freeTimes = is.popByte();
			freeCool = is.popShort();
			firstDropTableID = is.popShort();
			firstFreeDropTableID = is.popShort();
			itemDropTableID = is.popShort();
			generalDropTableID = is.popShort();
			poolLow = is.popInteger();
			poolHigh = is.popInteger();
			generalUp = is.popInteger();
			itemDown = is.popInteger();
			propGeneral = is.popFloat();
			cost10 = is.popInteger();
			minGeneral10 = is.popByte();
			maxGeneral10 = is.popByte();
			proectTimes10 = is.popByte();
			if( itemFixed == null )
				itemFixed = new DropEntry();
			is.pop(itemFixed);
			dropTableIDsFirst10 = is.popShortList();
			cost10FirstBuy = is.popIntegerList();
			dropTableIDs100 = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushInteger(cost);
			os.pushByte(freeTimes);
			os.pushShort(freeCool);
			os.pushShort(firstDropTableID);
			os.pushShort(firstFreeDropTableID);
			os.pushShort(itemDropTableID);
			os.pushShort(generalDropTableID);
			os.pushInteger(poolLow);
			os.pushInteger(poolHigh);
			os.pushInteger(generalUp);
			os.pushInteger(itemDown);
			os.pushFloat(propGeneral);
			os.pushInteger(cost10);
			os.pushByte(minGeneral10);
			os.pushByte(maxGeneral10);
			os.pushByte(proectTimes10);
			os.push(itemFixed);
			os.pushShortList(dropTableIDsFirst10);
			os.pushIntegerList(cost10FirstBuy);
			os.pushShort(dropTableIDs100);
		}

		public byte id;
		public int cost;
		public byte freeTimes;
		public short freeCool;
		public short firstDropTableID;
		public short firstFreeDropTableID;
		public short itemDropTableID;
		public short generalDropTableID;
		public int poolLow;
		public int poolHigh;
		public int generalUp;
		public int itemDown;
		public float propGeneral;
		public int cost10;
		public byte minGeneral10;
		public byte maxGeneral10;
		public byte proectTimes10;
		public DropEntry itemFixed;
		public List<Short> dropTableIDsFirst10;
		public List<Integer> cost10FirstBuy;
		public short dropTableIDs100;
	}

	public static class HotPoint implements Stream.IStreamable
	{

		public HotPoint() { }

		public HotPoint(short generialID, short soulItemID)
		{
			this.generialID = generialID;
			this.soulItemID = soulItemID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generialID = is.popShort();
			soulItemID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(generialID);
			os.pushShort(soulItemID);
		}

		public short generialID;
		public short soulItemID;
	}

	public static class DayHotPoint implements Stream.IStreamable
	{

		public DayHotPoint() { }

		public DayHotPoint(List<HotPoint> dayHotPoint)
		{
			this.dayHotPoint = dayHotPoint;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			dayHotPoint = is.popList(HotPoint.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(dayHotPoint);
		}

		public List<HotPoint> dayHotPoint;
	}

	public static class DropTimes implements Stream.IStreamable
	{

		public DropTimes() { }

		public DropTimes(float rate, byte times)
		{
			this.rate = rate;
			this.times = times;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rate = is.popFloat();
			times = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(rate);
			os.pushByte(times);
		}

		public float rate;
		public byte times;
	}

	public static class DropCount implements Stream.IStreamable
	{

		public DropCount() { }

		public DropCount(float rate, byte count)
		{
			this.rate = rate;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rate = is.popFloat();
			count = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(rate);
			os.pushByte(count);
		}

		public float rate;
		public byte count;
	}

	public static class SoulBoxCFGS implements Stream.IStreamable
	{

		public SoulBoxCFGS() { }

		public SoulBoxCFGS(short id, int startTime, int cost, short normalDropTableID, 
		                   byte weekHotPointFirstInterval, byte weekHotPointOtherInterval, byte weekHotPointMaxInterval, short weekHotPointGeneralID, 
		                   byte weekHotPointWeight, List<DayHotPoint> dayHotPoint, List<DropTimes> dayHotPointDropTimes, List<DropCount> dayHotPointDropCount, 
		                   List<Short> optionalHotPoint)
		{
			this.id = id;
			this.startTime = startTime;
			this.cost = cost;
			this.normalDropTableID = normalDropTableID;
			this.weekHotPointFirstInterval = weekHotPointFirstInterval;
			this.weekHotPointOtherInterval = weekHotPointOtherInterval;
			this.weekHotPointMaxInterval = weekHotPointMaxInterval;
			this.weekHotPointGeneralID = weekHotPointGeneralID;
			this.weekHotPointWeight = weekHotPointWeight;
			this.dayHotPoint = dayHotPoint;
			this.dayHotPointDropTimes = dayHotPointDropTimes;
			this.dayHotPointDropCount = dayHotPointDropCount;
			this.optionalHotPoint = optionalHotPoint;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			startTime = is.popInteger();
			cost = is.popInteger();
			normalDropTableID = is.popShort();
			weekHotPointFirstInterval = is.popByte();
			weekHotPointOtherInterval = is.popByte();
			weekHotPointMaxInterval = is.popByte();
			weekHotPointGeneralID = is.popShort();
			weekHotPointWeight = is.popByte();
			dayHotPoint = is.popList(DayHotPoint.class);
			dayHotPointDropTimes = is.popList(DropTimes.class);
			dayHotPointDropCount = is.popList(DropCount.class);
			optionalHotPoint = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(startTime);
			os.pushInteger(cost);
			os.pushShort(normalDropTableID);
			os.pushByte(weekHotPointFirstInterval);
			os.pushByte(weekHotPointOtherInterval);
			os.pushByte(weekHotPointMaxInterval);
			os.pushShort(weekHotPointGeneralID);
			os.pushByte(weekHotPointWeight);
			os.pushList(dayHotPoint);
			os.pushList(dayHotPointDropTimes);
			os.pushList(dayHotPointDropCount);
			os.pushShortList(optionalHotPoint);
		}

		public short id;
		public int startTime;
		public int cost;
		public short normalDropTableID;
		public byte weekHotPointFirstInterval;
		public byte weekHotPointOtherInterval;
		public byte weekHotPointMaxInterval;
		public short weekHotPointGeneralID;
		public byte weekHotPointWeight;
		public List<DayHotPoint> dayHotPoint;
		public List<DropTimes> dayHotPointDropTimes;
		public List<DropCount> dayHotPointDropCount;
		public List<Short> optionalHotPoint;
	}

	public static class OfficialBasicCFGS implements Stream.IStreamable
	{

		public OfficialBasicCFGS() { }

		public OfficialBasicCFGS(float officialResetRate, int officialResetStone, float skillResetRate, int skillResetStone, 
		                         short openLvl, short levelItemId, short skillLevelItemId)
		{
			this.officialResetRate = officialResetRate;
			this.officialResetStone = officialResetStone;
			this.skillResetRate = skillResetRate;
			this.skillResetStone = skillResetStone;
			this.openLvl = openLvl;
			this.levelItemId = levelItemId;
			this.skillLevelItemId = skillLevelItemId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			officialResetRate = is.popFloat();
			officialResetStone = is.popInteger();
			skillResetRate = is.popFloat();
			skillResetStone = is.popInteger();
			openLvl = is.popShort();
			levelItemId = is.popShort();
			skillLevelItemId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushFloat(officialResetRate);
			os.pushInteger(officialResetStone);
			os.pushFloat(skillResetRate);
			os.pushInteger(skillResetStone);
			os.pushShort(openLvl);
			os.pushShort(levelItemId);
			os.pushShort(skillLevelItemId);
		}

		public float officialResetRate;
		public int officialResetStone;
		public float skillResetRate;
		public int skillResetStone;
		public short openLvl;
		public short levelItemId;
		public short skillLevelItemId;
	}

	public static class OfficialLevelCFGS implements Stream.IStreamable
	{

		public OfficialLevelCFGS() { }

		public OfficialLevelCFGS(String name, int itemCount)
		{
			this.name = name;
			this.itemCount = itemCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			name = is.popString();
			itemCount = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(name);
			os.pushInteger(itemCount);
		}

		public String name;
		public int itemCount;
	}

	public static class OfficialSkillLevelCFGS implements Stream.IStreamable
	{

		public OfficialSkillLevelCFGS() { }

		public OfficialSkillLevelCFGS(short skillLvl, int itemCount)
		{
			this.skillLvl = skillLvl;
			this.itemCount = itemCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			skillLvl = is.popShort();
			itemCount = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(skillLvl);
			os.pushInteger(itemCount);
		}

		public short skillLvl;
		public int itemCount;
	}

	public static class OfficialCFGS implements Stream.IStreamable
	{

		public OfficialCFGS() { }

		public OfficialCFGS(OfficialBasicCFGS basic, List<OfficialLevelCFGS> levels, List<OfficialSkillLevelCFGS> skillLevels)
		{
			this.basic = basic;
			this.levels = levels;
			this.skillLevels = skillLevels;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( basic == null )
				basic = new OfficialBasicCFGS();
			is.pop(basic);
			levels = is.popList(OfficialLevelCFGS.class);
			skillLevels = is.popList(OfficialSkillLevelCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(basic);
			os.pushList(levels);
			os.pushList(skillLevels);
		}

		public OfficialBasicCFGS basic;
		public List<OfficialLevelCFGS> levels;
		public List<OfficialSkillLevelCFGS> skillLevels;
	}

	public static class ChannelCFGS implements Stream.IStreamable
	{

		public ChannelCFGS() { }

		public ChannelCFGS(short payID, String name)
		{
			this.payID = payID;
			this.name = name;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			payID = is.popShort();
			name = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(payID);
			os.pushString(name);
		}

		public short payID;
		public String name;
	}

	public static class TreasureMapBaseCFGS implements Stream.IStreamable
	{

		public TreasureMapBaseCFGS() { }

		public TreasureMapBaseCFGS(short maxCount, byte refreshTime, byte refreshTimeAddCount, byte addCountTime, 
		                           byte addCount, byte dayUseTreasureMapCount, byte mapDigMaxCount, byte digOtherMaxCount, 
		                           String mailTitle, String mailContextDigger, String mailContextUser, List<TreasureMapCFGS> treasureMaps)
		{
			this.maxCount = maxCount;
			this.refreshTime = refreshTime;
			this.refreshTimeAddCount = refreshTimeAddCount;
			this.addCountTime = addCountTime;
			this.addCount = addCount;
			this.dayUseTreasureMapCount = dayUseTreasureMapCount;
			this.mapDigMaxCount = mapDigMaxCount;
			this.digOtherMaxCount = digOtherMaxCount;
			this.mailTitle = mailTitle;
			this.mailContextDigger = mailContextDigger;
			this.mailContextUser = mailContextUser;
			this.treasureMaps = treasureMaps;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			maxCount = is.popShort();
			refreshTime = is.popByte();
			refreshTimeAddCount = is.popByte();
			addCountTime = is.popByte();
			addCount = is.popByte();
			dayUseTreasureMapCount = is.popByte();
			mapDigMaxCount = is.popByte();
			digOtherMaxCount = is.popByte();
			mailTitle = is.popString();
			mailContextDigger = is.popString();
			mailContextUser = is.popString();
			treasureMaps = is.popList(TreasureMapCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(maxCount);
			os.pushByte(refreshTime);
			os.pushByte(refreshTimeAddCount);
			os.pushByte(addCountTime);
			os.pushByte(addCount);
			os.pushByte(dayUseTreasureMapCount);
			os.pushByte(mapDigMaxCount);
			os.pushByte(digOtherMaxCount);
			os.pushString(mailTitle);
			os.pushString(mailContextDigger);
			os.pushString(mailContextUser);
			os.pushList(treasureMaps);
		}

		public short maxCount;
		public byte refreshTime;
		public byte refreshTimeAddCount;
		public byte addCountTime;
		public byte addCount;
		public byte dayUseTreasureMapCount;
		public byte mapDigMaxCount;
		public byte digOtherMaxCount;
		public String mailTitle;
		public String mailContextDigger;
		public String mailContextUser;
		public List<TreasureMapCFGS> treasureMaps;
	}

	public static class TreasureMapCFGS implements Stream.IStreamable
	{

		public TreasureMapCFGS() { }

		public TreasureMapCFGS(byte id, String name, short itemId, short commonRewards, 
		                       short treasureRewards, short userRewards1Id, short userRewards1Cnt, short userRewards2Id, 
		                       short userRewards2Cnt, short userRewards3Id, short userRewards3Cnt, byte rewardsMaxCnt, 
		                       short digCost)
		{
			this.id = id;
			this.name = name;
			this.itemId = itemId;
			this.commonRewards = commonRewards;
			this.treasureRewards = treasureRewards;
			this.userRewards1Id = userRewards1Id;
			this.userRewards1Cnt = userRewards1Cnt;
			this.userRewards2Id = userRewards2Id;
			this.userRewards2Cnt = userRewards2Cnt;
			this.userRewards3Id = userRewards3Id;
			this.userRewards3Cnt = userRewards3Cnt;
			this.rewardsMaxCnt = rewardsMaxCnt;
			this.digCost = digCost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			name = is.popString();
			itemId = is.popShort();
			commonRewards = is.popShort();
			treasureRewards = is.popShort();
			userRewards1Id = is.popShort();
			userRewards1Cnt = is.popShort();
			userRewards2Id = is.popShort();
			userRewards2Cnt = is.popShort();
			userRewards3Id = is.popShort();
			userRewards3Cnt = is.popShort();
			rewardsMaxCnt = is.popByte();
			digCost = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushString(name);
			os.pushShort(itemId);
			os.pushShort(commonRewards);
			os.pushShort(treasureRewards);
			os.pushShort(userRewards1Id);
			os.pushShort(userRewards1Cnt);
			os.pushShort(userRewards2Id);
			os.pushShort(userRewards2Cnt);
			os.pushShort(userRewards3Id);
			os.pushShort(userRewards3Cnt);
			os.pushByte(rewardsMaxCnt);
			os.pushShort(digCost);
		}

		public byte id;
		public String name;
		public short itemId;
		public short commonRewards;
		public short treasureRewards;
		public short userRewards1Id;
		public short userRewards1Cnt;
		public short userRewards2Id;
		public short userRewards2Cnt;
		public short userRewards3Id;
		public short userRewards3Cnt;
		public byte rewardsMaxCnt;
		public short digCost;
	}

	public static class BeMonsterReward implements Stream.IStreamable
	{

		public BeMonsterReward() { }

		public BeMonsterReward(int numMin, int numMax, List<DropEntryNew> rewrad)
		{
			this.numMin = numMin;
			this.numMax = numMax;
			this.rewrad = rewrad;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			numMin = is.popInteger();
			numMax = is.popInteger();
			rewrad = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(numMin);
			os.pushInteger(numMax);
			os.pushList(rewrad);
		}

		public int numMin;
		public int numMax;
		public List<DropEntryNew> rewrad;
	}

	public static class BeMonsterUpHpCFG implements Stream.IStreamable
	{

		public BeMonsterUpHpCFG() { }

		public BeMonsterUpHpCFG(short cnt, int percent, int cost)
		{
			this.cnt = cnt;
			this.percent = percent;
			this.cost = cost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cnt = is.popShort();
			percent = is.popInteger();
			cost = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cnt);
			os.pushInteger(percent);
			os.pushInteger(cost);
		}

		public short cnt;
		public int percent;
		public int cost;
	}

	public static class BeMonsterCFGS implements Stream.IStreamable
	{

		public BeMonsterCFGS() { }

		public BeMonsterCFGS(List<Short> RegisterTime, List<Short> RegisterEndTime, List<Short> fightStartTime, List<Short> fightEndTime, 
		                     int RegisterCostMin, int RegisterCostMax, int RegisterPoundage, byte attackMacCnt, 
		                     byte defaultUpHpPercent, List<BeMonsterUpHpCFG> upHpCfgs, List<DropEntryNew> monsterAliveRewards, List<BeMonsterReward> monsterKillRoleRewrads, 
		                     List<DropEntryNew> perFightRewards, List<DropEntryNew> killedMonsterRewards, List<BeMonsterReward> rankRewards)
		{
			this.RegisterTime = RegisterTime;
			this.RegisterEndTime = RegisterEndTime;
			this.fightStartTime = fightStartTime;
			this.fightEndTime = fightEndTime;
			this.RegisterCostMin = RegisterCostMin;
			this.RegisterCostMax = RegisterCostMax;
			this.RegisterPoundage = RegisterPoundage;
			this.attackMacCnt = attackMacCnt;
			this.defaultUpHpPercent = defaultUpHpPercent;
			this.upHpCfgs = upHpCfgs;
			this.monsterAliveRewards = monsterAliveRewards;
			this.monsterKillRoleRewrads = monsterKillRoleRewrads;
			this.perFightRewards = perFightRewards;
			this.killedMonsterRewards = killedMonsterRewards;
			this.rankRewards = rankRewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			RegisterTime = is.popShortList();
			RegisterEndTime = is.popShortList();
			fightStartTime = is.popShortList();
			fightEndTime = is.popShortList();
			RegisterCostMin = is.popInteger();
			RegisterCostMax = is.popInteger();
			RegisterPoundage = is.popInteger();
			attackMacCnt = is.popByte();
			defaultUpHpPercent = is.popByte();
			upHpCfgs = is.popList(BeMonsterUpHpCFG.class);
			monsterAliveRewards = is.popList(DropEntryNew.class);
			monsterKillRoleRewrads = is.popList(BeMonsterReward.class);
			perFightRewards = is.popList(DropEntryNew.class);
			killedMonsterRewards = is.popList(DropEntryNew.class);
			rankRewards = is.popList(BeMonsterReward.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShortList(RegisterTime);
			os.pushShortList(RegisterEndTime);
			os.pushShortList(fightStartTime);
			os.pushShortList(fightEndTime);
			os.pushInteger(RegisterCostMin);
			os.pushInteger(RegisterCostMax);
			os.pushInteger(RegisterPoundage);
			os.pushByte(attackMacCnt);
			os.pushByte(defaultUpHpPercent);
			os.pushList(upHpCfgs);
			os.pushList(monsterAliveRewards);
			os.pushList(monsterKillRoleRewrads);
			os.pushList(perFightRewards);
			os.pushList(killedMonsterRewards);
			os.pushList(rankRewards);
		}

		public List<Short> RegisterTime;
		public List<Short> RegisterEndTime;
		public List<Short> fightStartTime;
		public List<Short> fightEndTime;
		public int RegisterCostMin;
		public int RegisterCostMax;
		public int RegisterPoundage;
		public byte attackMacCnt;
		public byte defaultUpHpPercent;
		public List<BeMonsterUpHpCFG> upHpCfgs;
		public List<DropEntryNew> monsterAliveRewards;
		public List<BeMonsterReward> monsterKillRoleRewrads;
		public List<DropEntryNew> perFightRewards;
		public List<DropEntryNew> killedMonsterRewards;
		public List<BeMonsterReward> rankRewards;
	}

	public static class PayCFGLevelS implements Stream.IStreamable
	{

		public PayCFGLevelS() { }

		public PayCFGLevelS(int rmb, int stone)
		{
			this.rmb = rmb;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rmb = is.popInteger();
			stone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rmb);
			os.pushInteger(stone);
		}

		public int rmb;
		public int stone;
	}

	public static class PayCFGS implements Stream.IStreamable
	{

		public PayCFGS() { }

		public PayCFGS(short cid, byte flag, int ceil, int floor, 
		               List<PayCFGLevelS> levels)
		{
			this.cid = cid;
			this.flag = flag;
			this.ceil = ceil;
			this.floor = floor;
			this.levels = levels;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cid = is.popShort();
			flag = is.popByte();
			ceil = is.popInteger();
			floor = is.popInteger();
			levels = is.popList(PayCFGLevelS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cid);
			os.pushByte(flag);
			os.pushInteger(ceil);
			os.pushInteger(floor);
			os.pushList(levels);
		}

		public short cid;
		public byte flag;
		public int ceil;
		public int floor;
		public List<PayCFGLevelS> levels;
	}

	public static class MonthlyCardCFGS implements Stream.IStreamable
	{

		public MonthlyCardCFGS() { }

		public MonthlyCardCFGS(byte price, byte days, short reward)
		{
			this.price = price;
			this.days = days;
			this.reward = reward;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			price = is.popByte();
			days = is.popByte();
			reward = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(price);
			os.pushByte(days);
			os.pushShort(reward);
		}

		public byte price;
		public byte days;
		public short reward;
	}

	public static class GlobalRoleID implements Stream.IStreamable
	{

		public GlobalRoleID() { }

		public GlobalRoleID(int serverID, int roleID)
		{
			this.serverID = serverID;
			this.roleID = roleID;
		}

		public GlobalRoleID ksClone()
		{
			return new GlobalRoleID(serverID, roleID);
		}

		public GlobalRoleID kdClone()
		{
			GlobalRoleID _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverID = is.popInteger();
			roleID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(serverID);
			os.pushInteger(roleID);
		}

		public int serverID;
		public int roleID;
	}

	public static class GlobalForceID implements Stream.IStreamable
	{

		public GlobalForceID() { }

		public GlobalForceID(int serverID, int forceID)
		{
			this.serverID = serverID;
			this.forceID = forceID;
		}

		public GlobalForceID ksClone()
		{
			return new GlobalForceID(serverID, forceID);
		}

		public GlobalForceID kdClone()
		{
			GlobalForceID _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverID = is.popInteger();
			forceID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(serverID);
			os.pushInteger(forceID);
		}

		public int serverID;
		public int forceID;
	}

	public static class OpenIDRecord implements Stream.IStreamable
	{

		public OpenIDRecord() { }

		public OpenIDRecord(String openID, GlobalRoleID roleID)
		{
			this.openID = openID;
			this.roleID = roleID;
		}

		public OpenIDRecord ksClone()
		{
			return new OpenIDRecord(openID, roleID);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openID = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(openID);
			os.push(roleID);
		}

		public String openID;
		public GlobalRoleID roleID;
	}

	public static class SearchFriendsReq implements Stream.IStreamable
	{

		public SearchFriendsReq() { }

		public SearchFriendsReq(GlobalRoleID roleID, List<String> openIDs)
		{
			this.roleID = roleID;
			this.openIDs = openIDs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			openIDs = is.popStringList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleID);
			os.pushStringList(openIDs);
		}

		public GlobalRoleID roleID;
		public List<String> openIDs;
	}

	public static class SearchFriendsRes implements Stream.IStreamable
	{

		public SearchFriendsRes() { }

		public SearchFriendsRes(GlobalRoleID roleID, OpenIDRecord record, FriendProp prop)
		{
			this.roleID = roleID;
			this.record = record;
			this.prop = prop;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( record == null )
				record = new OpenIDRecord();
			is.pop(record);
			if( prop == null )
				prop = new FriendProp();
			is.pop(prop);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleID);
			os.push(record);
			os.push(prop);
		}

		public GlobalRoleID roleID;
		public OpenIDRecord record;
		public FriendProp prop;
	}

	public static class QueryFriendPropReq implements Stream.IStreamable
	{

		public QueryFriendPropReq() { }

		public QueryFriendPropReq(GlobalRoleID roleIDSrc, OpenIDRecord recordTarget)
		{
			this.roleIDSrc = roleIDSrc;
			this.recordTarget = recordTarget;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleIDSrc == null )
				roleIDSrc = new GlobalRoleID();
			is.pop(roleIDSrc);
			if( recordTarget == null )
				recordTarget = new OpenIDRecord();
			is.pop(recordTarget);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleIDSrc);
			os.push(recordTarget);
		}

		public GlobalRoleID roleIDSrc;
		public OpenIDRecord recordTarget;
	}

	public static class UpdateFriendPropReq implements Stream.IStreamable
	{

		public UpdateFriendPropReq() { }

		public UpdateFriendPropReq(GlobalRoleID roleIDSrc, List<OpenIDRecord> recordTargets)
		{
			this.roleIDSrc = roleIDSrc;
			this.recordTargets = recordTargets;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleIDSrc == null )
				roleIDSrc = new GlobalRoleID();
			is.pop(roleIDSrc);
			recordTargets = is.popList(OpenIDRecord.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleIDSrc);
			os.pushList(recordTargets);
		}

		public GlobalRoleID roleIDSrc;
		public List<OpenIDRecord> recordTargets;
	}

	public static class QueryFriendPropRes implements Stream.IStreamable
	{

		public QueryFriendPropRes() { }

		public QueryFriendPropRes(QueryFriendPropReq req, FriendProp prop)
		{
			this.req = req;
			this.prop = prop;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( req == null )
				req = new QueryFriendPropReq();
			is.pop(req);
			if( prop == null )
				prop = new FriendProp();
			is.pop(prop);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(req);
			os.push(prop);
		}

		public QueryFriendPropReq req;
		public FriendProp prop;
	}

	public static class SearchCityReq implements Stream.IStreamable
	{

		public SearchCityReq() { }

		public SearchCityReq(int tag, int id, GlobalRoleID roleID, int level, 
		                     int cityIDMax, int searchType)
		{
			this.tag = tag;
			this.id = id;
			this.roleID = roleID;
			this.level = level;
			this.cityIDMax = cityIDMax;
			this.searchType = searchType;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			level = is.popInteger();
			cityIDMax = is.popInteger();
			searchType = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.push(roleID);
			os.pushInteger(level);
			os.pushInteger(cityIDMax);
			os.pushInteger(searchType);
		}

		public int tag;
		public int id;
		public GlobalRoleID roleID;
		public int level;
		public int cityIDMax;
		public int searchType;
	}

	public static class SearchCityRes implements Stream.IStreamable
	{

		public SearchCityRes() { }

		public SearchCityRes(int tag, int id, int error, GlobalRoleID roleID, 
		                     CityInfo cityInfo)
		{
			this.tag = tag;
			this.id = id;
			this.error = error;
			this.roleID = roleID;
			this.cityInfo = cityInfo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			error = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( cityInfo == null )
				cityInfo = new CityInfo();
			is.pop(cityInfo);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(error);
			os.push(roleID);
			os.push(cityInfo);
		}

		public int tag;
		public int id;
		public int error;
		public GlobalRoleID roleID;
		public CityInfo cityInfo;
	}

	public static class AttackCityStartReq implements Stream.IStreamable
	{

		public AttackCityStartReq() { }

		public AttackCityStartReq(int tag, int id, GlobalRoleID roleID, GlobalRoleID targetRoleID, 
		                          GlobalRoleID baseRoleID, int cityID)
		{
			this.tag = tag;
			this.id = id;
			this.roleID = roleID;
			this.targetRoleID = targetRoleID;
			this.baseRoleID = baseRoleID;
			this.cityID = cityID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( targetRoleID == null )
				targetRoleID = new GlobalRoleID();
			is.pop(targetRoleID);
			if( baseRoleID == null )
				baseRoleID = new GlobalRoleID();
			is.pop(baseRoleID);
			cityID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.push(roleID);
			os.push(targetRoleID);
			os.push(baseRoleID);
			os.pushInteger(cityID);
		}

		public int tag;
		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID targetRoleID;
		public GlobalRoleID baseRoleID;
		public int cityID;
	}

	public static class AttackCityStartRes implements Stream.IStreamable
	{

		public AttackCityStartRes() { }

		public AttackCityStartRes(int tag, int id, int error, GlobalRoleID roleID, 
		                          CityInfo cityInfo)
		{
			this.tag = tag;
			this.id = id;
			this.error = error;
			this.roleID = roleID;
			this.cityInfo = cityInfo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			error = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( cityInfo == null )
				cityInfo = new CityInfo();
			is.pop(cityInfo);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(error);
			os.push(roleID);
			os.push(cityInfo);
		}

		public int tag;
		public int id;
		public int error;
		public GlobalRoleID roleID;
		public CityInfo cityInfo;
	}

	public static class AttackCityEndReq implements Stream.IStreamable
	{

		public AttackCityEndReq() { }

		public AttackCityEndReq(int tag, int id, GlobalRoleID roleID, String roleName, 
		                        GlobalRoleID targetRoleID, GlobalRoleID baseRoleID, int cityID, byte win)
		{
			this.tag = tag;
			this.id = id;
			this.roleID = roleID;
			this.roleName = roleName;
			this.targetRoleID = targetRoleID;
			this.baseRoleID = baseRoleID;
			this.cityID = cityID;
			this.win = win;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			if( targetRoleID == null )
				targetRoleID = new GlobalRoleID();
			is.pop(targetRoleID);
			if( baseRoleID == null )
				baseRoleID = new GlobalRoleID();
			is.pop(baseRoleID);
			cityID = is.popInteger();
			win = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.push(roleID);
			os.pushString(roleName);
			os.push(targetRoleID);
			os.push(baseRoleID);
			os.pushInteger(cityID);
			os.pushByte(win);
		}

		public int tag;
		public int id;
		public GlobalRoleID roleID;
		public String roleName;
		public GlobalRoleID targetRoleID;
		public GlobalRoleID baseRoleID;
		public int cityID;
		public byte win;
	}

	public static class AttackCityEndRes implements Stream.IStreamable
	{

		public AttackCityEndRes() { }

		public AttackCityEndRes(int tag, int id, int error, GlobalRoleID roleID, 
		                        CityInfo cityInfo)
		{
			this.tag = tag;
			this.id = id;
			this.error = error;
			this.roleID = roleID;
			this.cityInfo = cityInfo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			error = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( cityInfo == null )
				cityInfo = new CityInfo();
			is.pop(cityInfo);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(error);
			os.push(roleID);
			os.push(cityInfo);
		}

		public int tag;
		public int id;
		public int error;
		public GlobalRoleID roleID;
		public CityInfo cityInfo;
	}

	public static class NotifyCityOwnerChangedReq implements Stream.IStreamable
	{

		public NotifyCityOwnerChangedReq() { }

		public NotifyCityOwnerChangedReq(int tag, int id, GlobalRoleID roleID, String roleName, 
		                                 GlobalRoleID baseRoleID, int cityID)
		{
			this.tag = tag;
			this.id = id;
			this.roleID = roleID;
			this.roleName = roleName;
			this.baseRoleID = baseRoleID;
			this.cityID = cityID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			if( baseRoleID == null )
				baseRoleID = new GlobalRoleID();
			is.pop(baseRoleID);
			cityID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.push(roleID);
			os.pushString(roleName);
			os.push(baseRoleID);
			os.pushInteger(cityID);
		}

		public int tag;
		public int id;
		public GlobalRoleID roleID;
		public String roleName;
		public GlobalRoleID baseRoleID;
		public int cityID;
	}

	public static class QueryCityOwnerReq implements Stream.IStreamable
	{

		public QueryCityOwnerReq() { }

		public QueryCityOwnerReq(int tag, int id, GlobalRoleID roleID, GlobalRoleID targetRoleID, 
		                         GlobalRoleID baseRoleID, int cityID)
		{
			this.tag = tag;
			this.id = id;
			this.roleID = roleID;
			this.targetRoleID = targetRoleID;
			this.baseRoleID = baseRoleID;
			this.cityID = cityID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( targetRoleID == null )
				targetRoleID = new GlobalRoleID();
			is.pop(targetRoleID);
			if( baseRoleID == null )
				baseRoleID = new GlobalRoleID();
			is.pop(baseRoleID);
			cityID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.push(roleID);
			os.push(targetRoleID);
			os.push(baseRoleID);
			os.pushInteger(cityID);
		}

		public int tag;
		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID targetRoleID;
		public GlobalRoleID baseRoleID;
		public int cityID;
	}

	public static class QueryCityOwnerRes implements Stream.IStreamable
	{

		public QueryCityOwnerRes() { }

		public QueryCityOwnerRes(int tag, int id, int error, GlobalRoleID roleID, 
		                         CityInfo cityInfo)
		{
			this.tag = tag;
			this.id = id;
			this.error = error;
			this.roleID = roleID;
			this.cityInfo = cityInfo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			error = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( cityInfo == null )
				cityInfo = new CityInfo();
			is.pop(cityInfo);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(error);
			os.push(roleID);
			os.push(cityInfo);
		}

		public int tag;
		public int id;
		public int error;
		public GlobalRoleID roleID;
		public CityInfo cityInfo;
	}

	public static class DuelJoinReq implements Stream.IStreamable
	{

		public DuelJoinReq() { }

		public DuelJoinReq(int id, GlobalRoleID roleID, String roleName, String serverName, 
		                   short headIconId, byte type, short level, byte winTimes, 
		                   int time)
		{
			this.id = id;
			this.roleID = roleID;
			this.roleName = roleName;
			this.serverName = serverName;
			this.headIconId = headIconId;
			this.type = type;
			this.level = level;
			this.winTimes = winTimes;
			this.time = time;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			serverName = is.popString();
			headIconId = is.popShort();
			type = is.popByte();
			level = is.popShort();
			winTimes = is.popByte();
			time = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushString(roleName);
			os.pushString(serverName);
			os.pushShort(headIconId);
			os.pushByte(type);
			os.pushShort(level);
			os.pushByte(winTimes);
			os.pushInteger(time);
		}

		public int id;
		public GlobalRoleID roleID;
		public String roleName;
		public String serverName;
		public short headIconId;
		public byte type;
		public short level;
		public byte winTimes;
		public int time;
	}

	public static class DuelJoinRes implements Stream.IStreamable
	{

		public DuelJoinRes() { }

		public DuelJoinRes(int id, GlobalRoleID roleID, byte type, int error, 
		                   int matchId, byte first, GlobalRoleID oppoRoleID, String oppoRoleName, 
		                   String oppoServerName, short oppoHeadIconId, short oppoLevel)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.error = error;
			this.matchId = matchId;
			this.first = first;
			this.oppoRoleID = oppoRoleID;
			this.oppoRoleName = oppoRoleName;
			this.oppoServerName = oppoServerName;
			this.oppoHeadIconId = oppoHeadIconId;
			this.oppoLevel = oppoLevel;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popByte();
			error = is.popInteger();
			matchId = is.popInteger();
			first = is.popByte();
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			oppoRoleName = is.popString();
			oppoServerName = is.popString();
			oppoHeadIconId = is.popShort();
			oppoLevel = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushByte(type);
			os.pushInteger(error);
			os.pushInteger(matchId);
			os.pushByte(first);
			os.push(oppoRoleID);
			os.pushString(oppoRoleName);
			os.pushString(oppoServerName);
			os.pushShort(oppoHeadIconId);
			os.pushShort(oppoLevel);
		}

		public int id;
		public GlobalRoleID roleID;
		public byte type;
		public int error;
		public int matchId;
		public byte first;
		public GlobalRoleID oppoRoleID;
		public String oppoRoleName;
		public String oppoServerName;
		public short oppoHeadIconId;
		public short oppoLevel;
	}

	public static class DuelCancelJoinReq implements Stream.IStreamable
	{

		public DuelCancelJoinReq() { }

		public DuelCancelJoinReq(GlobalRoleID roleID, byte type)
		{
			this.roleID = roleID;
			this.type = type;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleID);
			os.pushByte(type);
		}

		public GlobalRoleID roleID;
		public byte type;
	}

	public static class DuelSelectGeneralReq implements Stream.IStreamable
	{

		public DuelSelectGeneralReq() { }

		public DuelSelectGeneralReq(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, List<i3k.DBRoleGeneral> generals, 
		                            List<DBPetBrief> pet, List<DBRelation> relation, int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.generals = generals;
			this.pet = pet;
			this.relation = relation;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			generals = is.popList(i3k.DBRoleGeneral.class);
			pet = is.popList(DBPetBrief.class);
			relation = is.popList(DBRelation.class);
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushList(generals);
			os.pushList(pet);
			os.pushList(relation);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pet;
		public List<DBRelation> relation;
		public int matchId;
	}

	public static class DuelSelectGeneralRes implements Stream.IStreamable
	{

		public DuelSelectGeneralRes() { }

		public DuelSelectGeneralRes(int id, GlobalRoleID roleID, int error, int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.error = error;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			error = is.popInteger();
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushInteger(error);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public int error;
		public int matchId;
	}

	public static class DuelStartAttackReq implements Stream.IStreamable
	{

		public DuelStartAttackReq() { }

		public DuelStartAttackReq(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, byte index, 
		                          int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.index = index;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			index = is.popByte();
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushByte(index);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public byte index;
		public int matchId;
	}

	public static class DuelStartAttackRes implements Stream.IStreamable
	{

		public DuelStartAttackRes() { }

		public DuelStartAttackRes(int id, GlobalRoleID roleID, byte index, int error, 
		                          int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.index = index;
			this.error = error;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			index = is.popByte();
			error = is.popInteger();
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushByte(index);
			os.pushInteger(error);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public byte index;
		public int error;
		public int matchId;
	}

	public static class DuelFinishAttackReq implements Stream.IStreamable
	{

		public DuelFinishAttackReq() { }

		public DuelFinishAttackReq(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, byte index, 
		                           byte result, int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.index = index;
			this.result = result;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			index = is.popByte();
			result = is.popByte();
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushByte(index);
			os.pushByte(result);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public byte index;
		public byte result;
		public int matchId;
	}

	public static class DuelFinishAttackRes implements Stream.IStreamable
	{

		public DuelFinishAttackRes() { }

		public DuelFinishAttackRes(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, byte index, 
		                           byte result, int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.index = index;
			this.result = result;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			index = is.popByte();
			result = is.popByte();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushByte(index);
			os.pushByte(result);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public byte index;
		public byte result;
		public int error;
	}

	public static class DuelOppoGiveupReq implements Stream.IStreamable
	{

		public DuelOppoGiveupReq() { }

		public DuelOppoGiveupReq(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, int matchId)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.matchId = matchId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			matchId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushInteger(matchId);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public int matchId;
	}

	public static class DuelOppoGiveupRes implements Stream.IStreamable
	{

		public DuelOppoGiveupRes() { }

		public DuelOppoGiveupRes(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public int error;
	}

	public static class DuelFinishAttackOnOppoDownLineReq implements Stream.IStreamable
	{

		public DuelFinishAttackOnOppoDownLineReq() { }

		public DuelFinishAttackOnOppoDownLineReq(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, int matchId, 
		                                         byte attackResult)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.matchId = matchId;
			this.attackResult = attackResult;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			matchId = is.popInteger();
			attackResult = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushInteger(matchId);
			os.pushByte(attackResult);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public int matchId;
		public byte attackResult;
	}

	public static class DuelFinishAttackOnOppoDownLineRes implements Stream.IStreamable
	{

		public DuelFinishAttackOnOppoDownLineRes() { }

		public DuelFinishAttackOnOppoDownLineRes(int id, GlobalRoleID roleID, GlobalRoleID oppoRoleID, byte attackResult, 
		                                         int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.oppoRoleID = oppoRoleID;
			this.attackResult = attackResult;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( oppoRoleID == null )
				oppoRoleID = new GlobalRoleID();
			is.pop(oppoRoleID);
			attackResult = is.popByte();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.push(oppoRoleID);
			os.pushByte(attackResult);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public GlobalRoleID oppoRoleID;
		public byte attackResult;
		public int error;
	}

	public static class DuelRank implements Stream.IStreamable
	{

		public DuelRank() { }

		public DuelRank(int rank, String serverName, GlobalRoleID roleID, String rname, 
		                short headIconId, int score)
		{
			this.rank = rank;
			this.serverName = serverName;
			this.roleID = roleID;
			this.rname = rname;
			this.headIconId = headIconId;
			this.score = score;
		}

		public DuelRank ksClone()
		{
			return new DuelRank(rank, serverName, roleID, rname, 
			                    headIconId, score);
		}

		public DuelRank kdClone()
		{
			DuelRank _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rank = is.popInteger();
			serverName = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			rname = is.popString();
			headIconId = is.popShort();
			score = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rank);
			os.pushString(serverName);
			os.push(roleID);
			os.pushString(rname);
			os.pushShort(headIconId);
			os.pushInteger(score);
		}

		public int rank;
		public String serverName;
		public GlobalRoleID roleID;
		public String rname;
		public short headIconId;
		public int score;
	}

	public static class DuelRanksRes implements Stream.IStreamable
	{

		public DuelRanksRes() { }

		public DuelRanksRes(int serverId, List<DuelRank> ranks)
		{
			this.serverId = serverId;
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverId = is.popInteger();
			ranks = is.popList(DuelRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(serverId);
			os.pushList(ranks);
		}

		public int serverId;
		public List<DuelRank> ranks;
	}

	public static class DuelGlobalRank implements Stream.IStreamable
	{

		public DuelGlobalRank() { }

		public DuelGlobalRank(int roleId, int rank)
		{
			this.roleId = roleId;
			this.rank = rank;
		}

		public DuelGlobalRank ksClone()
		{
			return new DuelGlobalRank(roleId, rank);
		}

		public DuelGlobalRank kdClone()
		{
			DuelGlobalRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleId = is.popInteger();
			rank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(roleId);
			os.pushInteger(rank);
		}

		public int roleId;
		public int rank;
	}

	public static class ExpiratBossGlobalRank implements Stream.IStreamable
	{

		public ExpiratBossGlobalRank() { }

		public ExpiratBossGlobalRank(int roleId, int rank)
		{
			this.roleId = roleId;
			this.rank = rank;
		}

		public ExpiratBossGlobalRank ksClone()
		{
			return new ExpiratBossGlobalRank(roleId, rank);
		}

		public ExpiratBossGlobalRank kdClone()
		{
			ExpiratBossGlobalRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleId = is.popInteger();
			rank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(roleId);
			os.pushInteger(rank);
		}

		public int roleId;
		public int rank;
	}

	public static class DuelGlobalRanksRes implements Stream.IStreamable
	{

		public DuelGlobalRanksRes() { }

		public DuelGlobalRanksRes(List<DuelGlobalRank> ranks)
		{
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(DuelGlobalRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
		}

		public List<DuelGlobalRank> ranks;
	}

	public static class ExpiratBossGlobalRanksRes implements Stream.IStreamable
	{

		public ExpiratBossGlobalRanksRes() { }

		public ExpiratBossGlobalRanksRes(short rankType, List<ExpiratBossGlobalRank> ranks)
		{
			this.rankType = rankType;
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankType = is.popShort();
			ranks = is.popList(ExpiratBossGlobalRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(rankType);
			os.pushList(ranks);
		}

		public short rankType;
		public List<ExpiratBossGlobalRank> ranks;
	}

	public static class ExpiratBossGlobalRanksRes2 implements Stream.IStreamable
	{

		public ExpiratBossGlobalRanksRes2() { }

		public ExpiratBossGlobalRanksRes2(List<ExpiratBossGlobalRanksRes> ranks2)
		{
			this.ranks2 = ranks2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks2 = is.popList(ExpiratBossGlobalRanksRes.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks2);
		}

		public List<ExpiratBossGlobalRanksRes> ranks2;
	}

	public static class DuelRankReq implements Stream.IStreamable
	{

		public DuelRankReq() { }

		public DuelRankReq(int id, GlobalRoleID roleID)
		{
			this.id = id;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
		}

		public int id;
		public GlobalRoleID roleID;
	}

	public static class DuelRankRes implements Stream.IStreamable
	{

		public DuelRankRes() { }

		public DuelRankRes(int id, GlobalRoleID roleID, int rank, int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.rank = rank;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			rank = is.popInteger();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushInteger(rank);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public int rank;
		public int error;
	}

	public static class DuelTopRanksReq implements Stream.IStreamable
	{

		public DuelTopRanksReq() { }

		public DuelTopRanksReq(int id, GlobalRoleID roleID)
		{
			this.id = id;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
		}

		public int id;
		public GlobalRoleID roleID;
	}

	public static class DuelTopRank implements Stream.IStreamable
	{

		public DuelTopRank() { }

		public DuelTopRank(GlobalRoleID roleID, String roleName, String serverName, short headIconId, 
		                   int score, int rank)
		{
			this.roleID = roleID;
			this.roleName = roleName;
			this.serverName = serverName;
			this.headIconId = headIconId;
			this.score = score;
			this.rank = rank;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			serverName = is.popString();
			headIconId = is.popShort();
			score = is.popInteger();
			rank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleID);
			os.pushString(roleName);
			os.pushString(serverName);
			os.pushShort(headIconId);
			os.pushInteger(score);
			os.pushInteger(rank);
		}

		public GlobalRoleID roleID;
		public String roleName;
		public String serverName;
		public short headIconId;
		public int score;
		public int rank;
	}

	public static class DuelTopRanksRes implements Stream.IStreamable
	{

		public DuelTopRanksRes() { }

		public DuelTopRanksRes(int id, GlobalRoleID roleID, List<DuelTopRank> ranks, int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.ranks = ranks;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			ranks = is.popList(DuelTopRank.class);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushList(ranks);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public List<DuelTopRank> ranks;
		public int error;
	}

	public static class SWarSearchReq implements Stream.IStreamable
	{

		public SWarSearchReq() { }

		public SWarSearchReq(int id, GlobalForceID forceID, GlobalForceID skipForceID, short level)
		{
			this.id = id;
			this.forceID = forceID;
			this.skipForceID = skipForceID;
			this.level = level;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			if( skipForceID == null )
				skipForceID = new GlobalForceID();
			is.pop(skipForceID);
			level = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.push(skipForceID);
			os.pushShort(level);
		}

		public int id;
		public GlobalForceID forceID;
		public GlobalForceID skipForceID;
		public short level;
	}

	public static class SWarSearchRes implements Stream.IStreamable
	{

		public SWarSearchRes() { }

		public SWarSearchRes(int id, GlobalForceID forceID, List<DBForceSWarBrief> oppos, int error)
		{
			this.id = id;
			this.forceID = forceID;
			this.oppos = oppos;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			oppos = is.popList(DBForceSWarBrief.class);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.pushList(oppos);
			os.pushInteger(error);
		}

		public int id;
		public GlobalForceID forceID;
		public List<DBForceSWarBrief> oppos;
		public int error;
	}

	public static class SWarAttackReq implements Stream.IStreamable
	{

		public SWarAttackReq() { }

		public SWarAttackReq(int id, GlobalForceID forceID, GlobalForceID oppoForceID, byte bid, 
		                     short bindex, DBForceSWarMatchRecord record)
		{
			this.id = id;
			this.forceID = forceID;
			this.oppoForceID = oppoForceID;
			this.bid = bid;
			this.bindex = bindex;
			this.record = record;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			if( oppoForceID == null )
				oppoForceID = new GlobalForceID();
			is.pop(oppoForceID);
			bid = is.popByte();
			bindex = is.popShort();
			if( record == null )
				record = new DBForceSWarMatchRecord();
			is.pop(record);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.push(oppoForceID);
			os.pushByte(bid);
			os.pushShort(bindex);
			os.push(record);
		}

		public int id;
		public GlobalForceID forceID;
		public GlobalForceID oppoForceID;
		public byte bid;
		public short bindex;
		public DBForceSWarMatchRecord record;
	}

	public static class SWarAttackRes implements Stream.IStreamable
	{

		public SWarAttackRes() { }

		public SWarAttackRes(int id, GlobalForceID forceID, GlobalForceID oppoForceID, byte bid, 
		                     short bindex, byte result, int error)
		{
			this.id = id;
			this.forceID = forceID;
			this.oppoForceID = oppoForceID;
			this.bid = bid;
			this.bindex = bindex;
			this.result = result;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			if( oppoForceID == null )
				oppoForceID = new GlobalForceID();
			is.pop(oppoForceID);
			bid = is.popByte();
			bindex = is.popShort();
			result = is.popByte();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.push(oppoForceID);
			os.pushByte(bid);
			os.pushShort(bindex);
			os.pushByte(result);
			os.pushInteger(error);
		}

		public int id;
		public GlobalForceID forceID;
		public GlobalForceID oppoForceID;
		public byte bid;
		public short bindex;
		public byte result;
		public int error;
	}

	public static class SWarRank implements Stream.IStreamable
	{

		public SWarRank() { }

		public SWarRank(int rank, short lvl, String serverName, GlobalForceID forceID, 
		                String fname, short iconId, int score)
		{
			this.rank = rank;
			this.lvl = lvl;
			this.serverName = serverName;
			this.forceID = forceID;
			this.fname = fname;
			this.iconId = iconId;
			this.score = score;
		}

		public SWarRank ksClone()
		{
			return new SWarRank(rank, lvl, serverName, forceID, 
			                    fname, iconId, score);
		}

		public SWarRank kdClone()
		{
			SWarRank _kio_clobj = ksClone();
			_kio_clobj.forceID = forceID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rank = is.popInteger();
			lvl = is.popShort();
			serverName = is.popString();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			fname = is.popString();
			iconId = is.popShort();
			score = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rank);
			os.pushShort(lvl);
			os.pushString(serverName);
			os.push(forceID);
			os.pushString(fname);
			os.pushShort(iconId);
			os.pushInteger(score);
		}

		public int rank;
		public short lvl;
		public String serverName;
		public GlobalForceID forceID;
		public String fname;
		public short iconId;
		public int score;
	}

	public static class SWarRanksRes implements Stream.IStreamable
	{

		public SWarRanksRes() { }

		public SWarRanksRes(int serverId, List<SWarRank> ranks)
		{
			this.serverId = serverId;
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverId = is.popInteger();
			ranks = is.popList(SWarRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(serverId);
			os.pushList(ranks);
		}

		public int serverId;
		public List<SWarRank> ranks;
	}

	public static class SWarGlobalRank implements Stream.IStreamable
	{

		public SWarGlobalRank() { }

		public SWarGlobalRank(int forceId, int rank)
		{
			this.forceId = forceId;
			this.rank = rank;
		}

		public SWarGlobalRank ksClone()
		{
			return new SWarGlobalRank(forceId, rank);
		}

		public SWarGlobalRank kdClone()
		{
			SWarGlobalRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			forceId = is.popInteger();
			rank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(forceId);
			os.pushInteger(rank);
		}

		public int forceId;
		public int rank;
	}

	public static class SWarGlobalRanksRes implements Stream.IStreamable
	{

		public SWarGlobalRanksRes() { }

		public SWarGlobalRanksRes(List<SWarGlobalRank> ranks)
		{
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(SWarGlobalRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
		}

		public List<SWarGlobalRank> ranks;
	}

	public static class SWarRankReq implements Stream.IStreamable
	{

		public SWarRankReq() { }

		public SWarRankReq(int id, GlobalForceID forceID)
		{
			this.id = id;
			this.forceID = forceID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
		}

		public int id;
		public GlobalForceID forceID;
	}

	public static class SWarRankRes implements Stream.IStreamable
	{

		public SWarRankRes() { }

		public SWarRankRes(int id, GlobalForceID forceID, int rank, int error)
		{
			this.id = id;
			this.forceID = forceID;
			this.rank = rank;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			rank = is.popInteger();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.pushInteger(rank);
			os.pushInteger(error);
		}

		public int id;
		public GlobalForceID forceID;
		public int rank;
		public int error;
	}

	public static class SWarTopRanksReq implements Stream.IStreamable
	{

		public SWarTopRanksReq() { }

		public SWarTopRanksReq(int id, GlobalForceID forceID)
		{
			this.id = id;
			this.forceID = forceID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
		}

		public int id;
		public GlobalForceID forceID;
	}

	public static class SWarTopRank implements Stream.IStreamable
	{

		public SWarTopRank() { }

		public SWarTopRank(GlobalForceID forceID, String fname, String serverName, short iconId, 
		                   int score, int rank, short lvl)
		{
			this.forceID = forceID;
			this.fname = fname;
			this.serverName = serverName;
			this.iconId = iconId;
			this.score = score;
			this.rank = rank;
			this.lvl = lvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			fname = is.popString();
			serverName = is.popString();
			iconId = is.popShort();
			score = is.popInteger();
			rank = is.popInteger();
			lvl = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(forceID);
			os.pushString(fname);
			os.pushString(serverName);
			os.pushShort(iconId);
			os.pushInteger(score);
			os.pushInteger(rank);
			os.pushShort(lvl);
		}

		public GlobalForceID forceID;
		public String fname;
		public String serverName;
		public short iconId;
		public int score;
		public int rank;
		public short lvl;
	}

	public static class SWarTopRanksRes implements Stream.IStreamable
	{

		public SWarTopRanksRes() { }

		public SWarTopRanksRes(int id, GlobalForceID forceID, List<SWarTopRank> ranks, int error)
		{
			this.id = id;
			this.forceID = forceID;
			this.ranks = ranks;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			ranks = is.popList(SWarTopRank.class);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(forceID);
			os.pushList(ranks);
			os.pushInteger(error);
		}

		public int id;
		public GlobalForceID forceID;
		public List<SWarTopRank> ranks;
		public int error;
	}

	public static class SWarVoteReq implements Stream.IStreamable
	{

		public SWarVoteReq() { }

		public SWarVoteReq(GlobalForceID forceID, GlobalForceID oppoForceID, GlobalForceID releaseForceID, String serverName, 
		                   String fname, short lvl)
		{
			this.forceID = forceID;
			this.oppoForceID = oppoForceID;
			this.releaseForceID = releaseForceID;
			this.serverName = serverName;
			this.fname = fname;
			this.lvl = lvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
			if( oppoForceID == null )
				oppoForceID = new GlobalForceID();
			is.pop(oppoForceID);
			if( releaseForceID == null )
				releaseForceID = new GlobalForceID();
			is.pop(releaseForceID);
			serverName = is.popString();
			fname = is.popString();
			lvl = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(forceID);
			os.push(oppoForceID);
			os.push(releaseForceID);
			os.pushString(serverName);
			os.pushString(fname);
			os.pushShort(lvl);
		}

		public GlobalForceID forceID;
		public GlobalForceID oppoForceID;
		public GlobalForceID releaseForceID;
		public String serverName;
		public String fname;
		public short lvl;
	}

	public static class SWarReleaseReq implements Stream.IStreamable
	{

		public SWarReleaseReq() { }

		public SWarReleaseReq(GlobalForceID forceID)
		{
			this.forceID = forceID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( forceID == null )
				forceID = new GlobalForceID();
			is.pop(forceID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(forceID);
		}

		public GlobalForceID forceID;
	}

	public static class ExpiratBossTopRanksRes implements Stream.IStreamable
	{

		public ExpiratBossTopRanksRes() { }

		public ExpiratBossTopRanksRes(int id, short combatId, List<ExpiratBossTopRank> ranks, int error)
		{
			this.id = id;
			this.combatId = combatId;
			this.ranks = ranks;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			combatId = is.popShort();
			ranks = is.popList(ExpiratBossTopRank.class);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(combatId);
			os.pushList(ranks);
			os.pushInteger(error);
		}

		public int id;
		public short combatId;
		public List<ExpiratBossTopRank> ranks;
		public int error;
	}

	public static class ExchangeChannelMessage implements Stream.IStreamable
	{

		public ExchangeChannelMessage() { }

		public ExchangeChannelMessage(int tag, int id, int srcgs, int dstgs, 
		                              List<String> msg)
		{
			this.tag = tag;
			this.id = id;
			this.srcgs = srcgs;
			this.dstgs = dstgs;
			this.msg = msg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			srcgs = is.popInteger();
			dstgs = is.popInteger();
			msg = is.popStringList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(srcgs);
			os.pushInteger(dstgs);
			os.pushStringList(msg);
		}

		public int tag;
		public int id;
		public int srcgs;
		public int dstgs;
		public List<String> msg;
	}

	public static class ExpiratBossRankReq implements Stream.IStreamable
	{

		public ExpiratBossRankReq() { }

		public ExpiratBossRankReq(int id, short type, GlobalRoleID roleID)
		{
			this.id = id;
			this.type = type;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			type = is.popShort();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(type);
			os.push(roleID);
		}

		public int id;
		public short type;
		public GlobalRoleID roleID;
	}

	public static class ExpiratBossRankRes implements Stream.IStreamable
	{

		public ExpiratBossRankRes() { }

		public ExpiratBossRankRes(int id, GlobalRoleID roleID, int rank, int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.rank = rank;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			rank = is.popInteger();
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushInteger(rank);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public int rank;
		public int error;
	}

	public static class HeroesBossSyncReq implements Stream.IStreamable
	{

		public HeroesBossSyncReq() { }

		public HeroesBossSyncReq(int id, GlobalRoleID roleID, short type, List<Short> gids, 
		                         short gcnt, HeroesRank ranks)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.gids = gids;
			this.gcnt = gcnt;
			this.ranks = ranks;
		}

		public HeroesBossSyncReq ksClone()
		{
			return new HeroesBossSyncReq(id, roleID, type, gids, 
			                             gcnt, ranks);
		}

		public HeroesBossSyncReq kdClone()
		{
			HeroesBossSyncReq _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.gids = new ArrayList<Short>();
			for(Short _kio_iter : gids)
			{
				_kio_clobj.gids.add(_kio_iter);
			}
			_kio_clobj.ranks = ranks.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popShort();
			gids = is.popShortList();
			gcnt = is.popShort();
			if( ranks == null )
				ranks = new HeroesRank();
			is.pop(ranks);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushShort(type);
			os.pushShortList(gids);
			os.pushShort(gcnt);
			os.push(ranks);
		}

		public int id;
		public GlobalRoleID roleID;
		public short type;
		public List<Short> gids;
		public short gcnt;
		public HeroesRank ranks;
	}

	public static class HeroesBossFinishAttReq implements Stream.IStreamable
	{

		public HeroesBossFinishAttReq() { }

		public HeroesBossFinishAttReq(int id, GlobalRoleID roleID, short type, int rid, 
		                              short type2, int score, HeroesRank rank, List<GlobalRoleID> serverIds, 
		                              List<HeroesRank> ranks)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.rid = rid;
			this.type2 = type2;
			this.score = score;
			this.rank = rank;
			this.serverIds = serverIds;
			this.ranks = ranks;
		}

		public HeroesBossFinishAttReq ksClone()
		{
			return new HeroesBossFinishAttReq(id, roleID, type, rid, 
			                                  type2, score, rank, serverIds, 
			                                  ranks);
		}

		public HeroesBossFinishAttReq kdClone()
		{
			HeroesBossFinishAttReq _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.rank = rank.kdClone();
			_kio_clobj.serverIds = new ArrayList<GlobalRoleID>();
			for(GlobalRoleID _kio_iter : serverIds)
			{
				_kio_clobj.serverIds.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popShort();
			rid = is.popInteger();
			type2 = is.popShort();
			score = is.popInteger();
			if( rank == null )
				rank = new HeroesRank();
			is.pop(rank);
			serverIds = is.popList(GlobalRoleID.class);
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushShort(type);
			os.pushInteger(rid);
			os.pushShort(type2);
			os.pushInteger(score);
			os.push(rank);
			os.pushList(serverIds);
			os.pushList(ranks);
		}

		public int id;
		public GlobalRoleID roleID;
		public short type;
		public int rid;
		public short type2;
		public int score;
		public HeroesRank rank;
		public List<GlobalRoleID> serverIds;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossInfoRes implements Stream.IStreamable
	{

		public HeroesBossInfoRes() { }

		public HeroesBossInfoRes(int id, List<HeroesRank> ranks)
		{
			this.id = id;
			this.ranks = ranks;
		}

		public HeroesBossInfoRes ksClone()
		{
			return new HeroesBossInfoRes(id, ranks);
		}

		public HeroesBossInfoRes kdClone()
		{
			HeroesBossInfoRes _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushList(ranks);
		}

		public int id;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossInfoReq implements Stream.IStreamable
	{

		public HeroesBossInfoReq() { }

		public HeroesBossInfoReq(int id, List<HeroesRank> ranks)
		{
			this.id = id;
			this.ranks = ranks;
		}

		public HeroesBossInfoReq ksClone()
		{
			return new HeroesBossInfoReq(id, ranks);
		}

		public HeroesBossInfoReq kdClone()
		{
			HeroesBossInfoReq _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushList(ranks);
		}

		public int id;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossSyncRes implements Stream.IStreamable
	{

		public HeroesBossSyncRes() { }

		public HeroesBossSyncRes(int id, GlobalRoleID roleID, short type, List<Short> gids1, 
		                         List<Short> gids2, List<Short> gids3, List<HeroesRank> ranks)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.gids1 = gids1;
			this.gids2 = gids2;
			this.gids3 = gids3;
			this.ranks = ranks;
		}

		public HeroesBossSyncRes ksClone()
		{
			return new HeroesBossSyncRes(id, roleID, type, gids1, 
			                             gids2, gids3, ranks);
		}

		public HeroesBossSyncRes kdClone()
		{
			HeroesBossSyncRes _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.gids1 = new ArrayList<Short>();
			for(Short _kio_iter : gids1)
			{
				_kio_clobj.gids1.add(_kio_iter);
			}
			_kio_clobj.gids2 = new ArrayList<Short>();
			for(Short _kio_iter : gids2)
			{
				_kio_clobj.gids2.add(_kio_iter);
			}
			_kio_clobj.gids3 = new ArrayList<Short>();
			for(Short _kio_iter : gids3)
			{
				_kio_clobj.gids3.add(_kio_iter);
			}
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popShort();
			gids1 = is.popShortList();
			gids2 = is.popShortList();
			gids3 = is.popShortList();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushShort(type);
			os.pushShortList(gids1);
			os.pushShortList(gids2);
			os.pushShortList(gids3);
			os.pushList(ranks);
		}

		public int id;
		public GlobalRoleID roleID;
		public short type;
		public List<Short> gids1;
		public List<Short> gids2;
		public List<Short> gids3;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossFinishAttRes implements Stream.IStreamable
	{

		public HeroesBossFinishAttRes() { }

		public HeroesBossFinishAttRes(int id, GlobalRoleID roleID, short type, short type2, 
		                              int score, HeroesRank rank, List<GlobalRoleID> serverIds, List<HeroesRank> ranks)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.type2 = type2;
			this.score = score;
			this.rank = rank;
			this.serverIds = serverIds;
			this.ranks = ranks;
		}

		public HeroesBossFinishAttRes ksClone()
		{
			return new HeroesBossFinishAttRes(id, roleID, type, type2, 
			                                  score, rank, serverIds, ranks);
		}

		public HeroesBossFinishAttRes kdClone()
		{
			HeroesBossFinishAttRes _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.rank = rank.kdClone();
			_kio_clobj.serverIds = new ArrayList<GlobalRoleID>();
			for(GlobalRoleID _kio_iter : serverIds)
			{
				_kio_clobj.serverIds.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popShort();
			type2 = is.popShort();
			score = is.popInteger();
			if( rank == null )
				rank = new HeroesRank();
			is.pop(rank);
			serverIds = is.popList(GlobalRoleID.class);
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushShort(type);
			os.pushShort(type2);
			os.pushInteger(score);
			os.push(rank);
			os.pushList(serverIds);
			os.pushList(ranks);
		}

		public int id;
		public GlobalRoleID roleID;
		public short type;
		public short type2;
		public int score;
		public HeroesRank rank;
		public List<GlobalRoleID> serverIds;
		public List<HeroesRank> ranks;
	}

	public static class ExpiratBossTopRanksReq implements Stream.IStreamable
	{

		public ExpiratBossTopRanksReq() { }

		public ExpiratBossTopRanksReq(int id, GlobalRoleID roleID)
		{
			this.id = id;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
		}

		public int id;
		public GlobalRoleID roleID;
	}

	public static class ExpiratBossGlobalRanksReq implements Stream.IStreamable
	{

		public ExpiratBossGlobalRanksReq() { }

		public ExpiratBossGlobalRanksReq(int id, GlobalRoleID roleID)
		{
			this.id = id;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
		}

		public int id;
		public GlobalRoleID roleID;
	}

	public static class ExpiratBossGlobalRanksReq2 implements Stream.IStreamable
	{

		public ExpiratBossGlobalRanksReq2() { }

		public ExpiratBossGlobalRanksReq2(int id, GlobalRoleID roleID)
		{
			this.id = id;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
		}

		public int id;
		public GlobalRoleID roleID;
	}

	public static class ExpiratBossTopRank implements Stream.IStreamable
	{

		public ExpiratBossTopRank() { }

		public ExpiratBossTopRank(GlobalRoleID roleID, String roleName, String serverName, short headIconId, 
		                          int damage, int rank)
		{
			this.roleID = roleID;
			this.roleName = roleName;
			this.serverName = serverName;
			this.headIconId = headIconId;
			this.damage = damage;
			this.rank = rank;
		}

		public ExpiratBossTopRank ksClone()
		{
			return new ExpiratBossTopRank(roleID, roleName, serverName, headIconId, 
			                              damage, rank);
		}

		public ExpiratBossTopRank kdClone()
		{
			ExpiratBossTopRank _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			serverName = is.popString();
			headIconId = is.popShort();
			damage = is.popInteger();
			rank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(roleID);
			os.pushString(roleName);
			os.pushString(serverName);
			os.pushShort(headIconId);
			os.pushInteger(damage);
			os.pushInteger(rank);
		}

		public GlobalRoleID roleID;
		public String roleName;
		public String serverName;
		public short headIconId;
		public int damage;
		public int rank;
	}

	public static class ExpiratBossTopRanksRes2 implements Stream.IStreamable
	{

		public ExpiratBossTopRanksRes2() { }

		public ExpiratBossTopRanksRes2(int id, GlobalRoleID roleID, short type, List<ExpiratBossTopRank> ranks, 
		                               int error)
		{
			this.id = id;
			this.roleID = roleID;
			this.type = type;
			this.ranks = ranks;
			this.error = error;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popShort();
			ranks = is.popList(ExpiratBossTopRank.class);
			error = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.push(roleID);
			os.pushShort(type);
			os.pushList(ranks);
			os.pushInteger(error);
		}

		public int id;
		public GlobalRoleID roleID;
		public short type;
		public List<ExpiratBossTopRank> ranks;
		public int error;
	}

	public static class ExpiratBossRank implements Stream.IStreamable
	{

		public ExpiratBossRank() { }

		public ExpiratBossRank(short combatId, int rank, String serverName, GlobalRoleID roleID, 
		                       String rname, short headIconId, int damage)
		{
			this.combatId = combatId;
			this.rank = rank;
			this.serverName = serverName;
			this.roleID = roleID;
			this.rname = rname;
			this.headIconId = headIconId;
			this.damage = damage;
		}

		public ExpiratBossRank ksClone()
		{
			return new ExpiratBossRank(combatId, rank, serverName, roleID, 
			                           rname, headIconId, damage);
		}

		public ExpiratBossRank kdClone()
		{
			ExpiratBossRank _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			rank = is.popInteger();
			serverName = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			rname = is.popString();
			headIconId = is.popShort();
			damage = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushInteger(rank);
			os.pushString(serverName);
			os.push(roleID);
			os.pushString(rname);
			os.pushShort(headIconId);
			os.pushInteger(damage);
		}

		public short combatId;
		public int rank;
		public String serverName;
		public GlobalRoleID roleID;
		public String rname;
		public short headIconId;
		public int damage;
	}

	public static class ExpiratBossRanks implements Stream.IStreamable
	{

		public ExpiratBossRanks() { }

		public ExpiratBossRanks(short type, List<ExpiratBossRank> ranks)
		{
			this.type = type;
			this.ranks = ranks;
		}

		public ExpiratBossRanks ksClone()
		{
			return new ExpiratBossRanks(type, ranks);
		}

		public ExpiratBossRanks kdClone()
		{
			ExpiratBossRanks _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<ExpiratBossRank>();
			for(ExpiratBossRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popShort();
			ranks = is.popList(ExpiratBossRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(type);
			os.pushList(ranks);
		}

		public short type;
		public List<ExpiratBossRank> ranks;
	}

	public static class ExpiratBossRanksRes implements Stream.IStreamable
	{

		public ExpiratBossRanksRes() { }

		public ExpiratBossRanksRes(int serverId, List<ExpiratBossRanks> ranksList)
		{
			this.serverId = serverId;
			this.ranksList = ranksList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverId = is.popInteger();
			ranksList = is.popList(ExpiratBossRanks.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(serverId);
			os.pushList(ranksList);
		}

		public int serverId;
		public List<ExpiratBossRanks> ranksList;
	}

	public static class HeroesBossSync implements Stream.IStreamable
	{

		public HeroesBossSync() { }

		public HeroesBossSync(short type, List<Short> gids, short gcnt, List<HeroesRank> ranks)
		{
			this.type = type;
			this.gids = gids;
			this.gcnt = gcnt;
			this.ranks = ranks;
		}

		public HeroesBossSync ksClone()
		{
			return new HeroesBossSync(type, gids, gcnt, ranks);
		}

		public HeroesBossSync kdClone()
		{
			HeroesBossSync _kio_clobj = ksClone();
			_kio_clobj.gids = new ArrayList<Short>();
			for(Short _kio_iter : gids)
			{
				_kio_clobj.gids.add(_kio_iter);
			}
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popShort();
			gids = is.popShortList();
			gcnt = is.popShort();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(type);
			os.pushShortList(gids);
			os.pushShort(gcnt);
			os.pushList(ranks);
		}

		public short type;
		public List<Short> gids;
		public short gcnt;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossFinishAtt implements Stream.IStreamable
	{

		public HeroesBossFinishAtt() { }

		public HeroesBossFinishAtt(short type, short rid, short type2, short score, 
		                           List<GlobalRoleID> serverIds, List<HeroesRank> ranks)
		{
			this.type = type;
			this.rid = rid;
			this.type2 = type2;
			this.score = score;
			this.serverIds = serverIds;
			this.ranks = ranks;
		}

		public HeroesBossFinishAtt ksClone()
		{
			return new HeroesBossFinishAtt(type, rid, type2, score, 
			                               serverIds, ranks);
		}

		public HeroesBossFinishAtt kdClone()
		{
			HeroesBossFinishAtt _kio_clobj = ksClone();
			_kio_clobj.serverIds = new ArrayList<GlobalRoleID>();
			for(GlobalRoleID _kio_iter : serverIds)
			{
				_kio_clobj.serverIds.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popShort();
			rid = is.popShort();
			type2 = is.popShort();
			score = is.popShort();
			serverIds = is.popList(GlobalRoleID.class);
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(type);
			os.pushShort(rid);
			os.pushShort(type2);
			os.pushShort(score);
			os.pushList(serverIds);
			os.pushList(ranks);
		}

		public short type;
		public short rid;
		public short type2;
		public short score;
		public List<GlobalRoleID> serverIds;
		public List<HeroesRank> ranks;
	}

	public static class HeroesBossInfo implements Stream.IStreamable
	{

		public HeroesBossInfo() { }

		public HeroesBossInfo(short rid, List<HeroesRank> ranks)
		{
			this.rid = rid;
			this.ranks = ranks;
		}

		public HeroesBossInfo ksClone()
		{
			return new HeroesBossInfo(rid, ranks);
		}

		public HeroesBossInfo kdClone()
		{
			HeroesBossInfo _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popShort();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(rid);
			os.pushList(ranks);
		}

		public short rid;
		public List<HeroesRank> ranks;
	}

	public static class ExpatriateTransRoleReq implements Stream.IStreamable
	{

		public ExpatriateTransRoleReq() { }

		public ExpatriateTransRoleReq(int tag, int id, int srcgs, int targs, 
		                              String openid, i3k.DBRole data)
		{
			this.tag = tag;
			this.id = id;
			this.srcgs = srcgs;
			this.targs = targs;
			this.openid = openid;
			this.data = data;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			srcgs = is.popInteger();
			targs = is.popInteger();
			openid = is.popString();
			if( data == null )
				data = new i3k.DBRole();
			is.pop(data);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(srcgs);
			os.pushInteger(targs);
			os.pushString(openid);
			os.push(data);
		}

		public int tag;
		public int id;
		public int srcgs;
		public int targs;
		public String openid;
		public i3k.DBRole data;
	}

	public static class ExpatriateTransRoleRes implements Stream.IStreamable
	{

		public ExpatriateTransRoleRes() { }

		public ExpatriateTransRoleRes(int tag, int id, int srcgs, int targs, 
		                              int operror, String errordes)
		{
			this.tag = tag;
			this.id = id;
			this.srcgs = srcgs;
			this.targs = targs;
			this.operror = operror;
			this.errordes = errordes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tag = is.popInteger();
			id = is.popInteger();
			srcgs = is.popInteger();
			targs = is.popInteger();
			operror = is.popInteger();
			errordes = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(tag);
			os.pushInteger(id);
			os.pushInteger(srcgs);
			os.pushInteger(targs);
			os.pushInteger(operror);
			os.pushString(errordes);
		}

		public int tag;
		public int id;
		public int srcgs;
		public int targs;
		public int operror;
		public String errordes;
	}

	public static class VIPCFGS implements Stream.IStreamable
	{

		public VIPCFGS() { }

		public VIPCFGS(byte lvl, int stoneReq, short buyMoneyTimes, short buyVitTimes, 
		               short buySkillPointTimes, byte skillPointMax, short buyArenaTimes, short resetMarchTimes, 
		               byte mobaiTimes, byte superMobaiTimes, byte canSaodang1, byte canSaodang10, 
		               byte resetCombatTimes, byte marchRewardUp, byte canBuySoulBox, byte canOccupyCityCount, 
		               byte canSummonShopRand1, byte canSummonShopRand2, short petZanOfferCount, byte mercCountMax)
		{
			this.lvl = lvl;
			this.stoneReq = stoneReq;
			this.buyMoneyTimes = buyMoneyTimes;
			this.buyVitTimes = buyVitTimes;
			this.buySkillPointTimes = buySkillPointTimes;
			this.skillPointMax = skillPointMax;
			this.buyArenaTimes = buyArenaTimes;
			this.resetMarchTimes = resetMarchTimes;
			this.mobaiTimes = mobaiTimes;
			this.superMobaiTimes = superMobaiTimes;
			this.canSaodang1 = canSaodang1;
			this.canSaodang10 = canSaodang10;
			this.resetCombatTimes = resetCombatTimes;
			this.marchRewardUp = marchRewardUp;
			this.canBuySoulBox = canBuySoulBox;
			this.canOccupyCityCount = canOccupyCityCount;
			this.canSummonShopRand1 = canSummonShopRand1;
			this.canSummonShopRand2 = canSummonShopRand2;
			this.petZanOfferCount = petZanOfferCount;
			this.mercCountMax = mercCountMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popByte();
			stoneReq = is.popInteger();
			buyMoneyTimes = is.popShort();
			buyVitTimes = is.popShort();
			buySkillPointTimes = is.popShort();
			skillPointMax = is.popByte();
			buyArenaTimes = is.popShort();
			resetMarchTimes = is.popShort();
			mobaiTimes = is.popByte();
			superMobaiTimes = is.popByte();
			canSaodang1 = is.popByte();
			canSaodang10 = is.popByte();
			resetCombatTimes = is.popByte();
			marchRewardUp = is.popByte();
			canBuySoulBox = is.popByte();
			canOccupyCityCount = is.popByte();
			canSummonShopRand1 = is.popByte();
			canSummonShopRand2 = is.popByte();
			petZanOfferCount = is.popShort();
			mercCountMax = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lvl);
			os.pushInteger(stoneReq);
			os.pushShort(buyMoneyTimes);
			os.pushShort(buyVitTimes);
			os.pushShort(buySkillPointTimes);
			os.pushByte(skillPointMax);
			os.pushShort(buyArenaTimes);
			os.pushShort(resetMarchTimes);
			os.pushByte(mobaiTimes);
			os.pushByte(superMobaiTimes);
			os.pushByte(canSaodang1);
			os.pushByte(canSaodang10);
			os.pushByte(resetCombatTimes);
			os.pushByte(marchRewardUp);
			os.pushByte(canBuySoulBox);
			os.pushByte(canOccupyCityCount);
			os.pushByte(canSummonShopRand1);
			os.pushByte(canSummonShopRand2);
			os.pushShort(petZanOfferCount);
			os.pushByte(mercCountMax);
		}

		public byte lvl;
		public int stoneReq;
		public short buyMoneyTimes;
		public short buyVitTimes;
		public short buySkillPointTimes;
		public byte skillPointMax;
		public short buyArenaTimes;
		public short resetMarchTimes;
		public byte mobaiTimes;
		public byte superMobaiTimes;
		public byte canSaodang1;
		public byte canSaodang10;
		public byte resetCombatTimes;
		public byte marchRewardUp;
		public byte canBuySoulBox;
		public byte canOccupyCityCount;
		public byte canSummonShopRand1;
		public byte canSummonShopRand2;
		public short petZanOfferCount;
		public byte mercCountMax;
	}

	public static class FriendRecallLevelCFGS implements Stream.IStreamable
	{

		public FriendRecallLevelCFGS() { }

		public FriendRecallLevelCFGS(short lvlCeil, int reward)
		{
			this.lvlCeil = lvlCeil;
			this.reward = reward;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlCeil = is.popShort();
			reward = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlCeil);
			os.pushInteger(reward);
		}

		public short lvlCeil;
		public int reward;
	}

	public static class FriendCFGS implements Stream.IStreamable
	{

		public FriendCFGS() { }

		public FriendCFGS(byte cap, short pointMaxPerDay, short vitBaoziMaxPerDay, short vitPerBaozi, 
		                  short pointPerBaozi, byte minRecallDay, byte maxRecall, List<FriendRecallLevelCFGS> recallLevels, 
		                  float breedRewardProp, int breedRewardMaxPerDay, short petExpItemMaxPerDay)
		{
			this.cap = cap;
			this.pointMaxPerDay = pointMaxPerDay;
			this.vitBaoziMaxPerDay = vitBaoziMaxPerDay;
			this.vitPerBaozi = vitPerBaozi;
			this.pointPerBaozi = pointPerBaozi;
			this.minRecallDay = minRecallDay;
			this.maxRecall = maxRecall;
			this.recallLevels = recallLevels;
			this.breedRewardProp = breedRewardProp;
			this.breedRewardMaxPerDay = breedRewardMaxPerDay;
			this.petExpItemMaxPerDay = petExpItemMaxPerDay;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cap = is.popByte();
			pointMaxPerDay = is.popShort();
			vitBaoziMaxPerDay = is.popShort();
			vitPerBaozi = is.popShort();
			pointPerBaozi = is.popShort();
			minRecallDay = is.popByte();
			maxRecall = is.popByte();
			recallLevels = is.popList(FriendRecallLevelCFGS.class);
			breedRewardProp = is.popFloat();
			breedRewardMaxPerDay = is.popInteger();
			petExpItemMaxPerDay = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(cap);
			os.pushShort(pointMaxPerDay);
			os.pushShort(vitBaoziMaxPerDay);
			os.pushShort(vitPerBaozi);
			os.pushShort(pointPerBaozi);
			os.pushByte(minRecallDay);
			os.pushByte(maxRecall);
			os.pushList(recallLevels);
			os.pushFloat(breedRewardProp);
			os.pushInteger(breedRewardMaxPerDay);
			os.pushShort(petExpItemMaxPerDay);
		}

		public byte cap;
		public short pointMaxPerDay;
		public short vitBaoziMaxPerDay;
		public short vitPerBaozi;
		public short pointPerBaozi;
		public byte minRecallDay;
		public byte maxRecall;
		public List<FriendRecallLevelCFGS> recallLevels;
		public float breedRewardProp;
		public int breedRewardMaxPerDay;
		public short petExpItemMaxPerDay;
	}

	public static class EventEggLevelCFGS implements Stream.IStreamable
	{

		public EventEggLevelCFGS() { }

		public EventEggLevelCFGS(short dropID, int stone)
		{
			this.dropID = dropID;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			dropID = is.popShort();
			stone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(dropID);
			os.pushInteger(stone);
		}

		public short dropID;
		public int stone;
	}

	public static class EventCFGS implements Stream.IStreamable
	{

		public EventCFGS() { }

		public EventCFGS(short packIDFirstPay, List<EventEggLevelCFGS> stoneAllGeneralEggDropIDs)
		{
			this.packIDFirstPay = packIDFirstPay;
			this.stoneAllGeneralEggDropIDs = stoneAllGeneralEggDropIDs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			packIDFirstPay = is.popShort();
			stoneAllGeneralEggDropIDs = is.popList(EventEggLevelCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(packIDFirstPay);
			os.pushList(stoneAllGeneralEggDropIDs);
		}

		public short packIDFirstPay;
		public List<EventEggLevelCFGS> stoneAllGeneralEggDropIDs;
	}

	public static class TaskReward implements Stream.IStreamable
	{

		public TaskReward() { }

		public TaskReward(byte type, float arg1, float arg2, float arg3)
		{
			this.type = type;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			arg1 = is.popFloat();
			arg2 = is.popFloat();
			arg3 = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushFloat(arg1);
			os.pushFloat(arg2);
			os.pushFloat(arg3);
		}

		public byte type;
		public float arg1;
		public float arg2;
		public float arg3;
	}

	public static class TaskCFGS implements Stream.IStreamable
	{

		public TaskCFGS() { }

		public TaskCFGS(short lvlReq, short gidReq, byte type, int arg1, 
		                int arg2, int arg3, int arg4, int rewardExp, 
		                int rewardMoney, int rewardStone, List<DropEntry> rewards)
		{
			this.lvlReq = lvlReq;
			this.gidReq = gidReq;
			this.type = type;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
			this.rewardExp = rewardExp;
			this.rewardMoney = rewardMoney;
			this.rewardStone = rewardStone;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlReq = is.popShort();
			gidReq = is.popShort();
			type = is.popByte();
			arg1 = is.popInteger();
			arg2 = is.popInteger();
			arg3 = is.popInteger();
			arg4 = is.popInteger();
			rewardExp = is.popInteger();
			rewardMoney = is.popInteger();
			rewardStone = is.popInteger();
			rewards = is.popList(DropEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlReq);
			os.pushShort(gidReq);
			os.pushByte(type);
			os.pushInteger(arg1);
			os.pushInteger(arg2);
			os.pushInteger(arg3);
			os.pushInteger(arg4);
			os.pushInteger(rewardExp);
			os.pushInteger(rewardMoney);
			os.pushInteger(rewardStone);
			os.pushList(rewards);
		}

		public short lvlReq;
		public short gidReq;
		public byte type;
		public int arg1;
		public int arg2;
		public int arg3;
		public int arg4;
		public int rewardExp;
		public int rewardMoney;
		public int rewardStone;
		public List<DropEntry> rewards;
	}

	public static class TaskGroupCFGS implements Stream.IStreamable
	{

		public TaskGroupCFGS() { }

		public TaskGroupCFGS(short id, List<TaskCFGS> tasks)
		{
			this.id = id;
			this.tasks = tasks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			tasks = is.popList(TaskCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushList(tasks);
		}

		public short id;
		public List<TaskCFGS> tasks;
	}

	public static class ArenaRewardCFGS implements Stream.IStreamable
	{

		public ArenaRewardCFGS() { }

		public ArenaRewardCFGS(int rankFloor, int money, int stone, short point, 
		                       short item1, byte item1Count, short item2, byte item2Count)
		{
			this.rankFloor = rankFloor;
			this.money = money;
			this.stone = stone;
			this.point = point;
			this.item1 = item1;
			this.item1Count = item1Count;
			this.item2 = item2;
			this.item2Count = item2Count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			money = is.popInteger();
			stone = is.popInteger();
			point = is.popShort();
			item1 = is.popShort();
			item1Count = is.popByte();
			item2 = is.popShort();
			item2Count = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushInteger(money);
			os.pushInteger(stone);
			os.pushShort(point);
			os.pushShort(item1);
			os.pushByte(item1Count);
			os.pushShort(item2);
			os.pushByte(item2Count);
		}

		public int rankFloor;
		public int money;
		public int stone;
		public short point;
		public short item1;
		public byte item1Count;
		public short item2;
		public byte item2Count;
	}

	public static class ArenaBestRankRewardCFGS implements Stream.IStreamable
	{

		public ArenaBestRankRewardCFGS() { }

		public ArenaBestRankRewardCFGS(int rankFloor, int money, int stone)
		{
			this.rankFloor = rankFloor;
			this.money = money;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			money = is.popInteger();
			stone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushInteger(money);
			os.pushInteger(stone);
		}

		public int rankFloor;
		public int money;
		public int stone;
	}

	public static class ArenaTargetCFGS implements Stream.IStreamable
	{

		public ArenaTargetCFGS() { }

		public ArenaTargetCFGS(int rankFloor, List<Integer> deltaMin, List<Integer> deltaMax, int seeMax)
		{
			this.rankFloor = rankFloor;
			this.deltaMin = deltaMin;
			this.deltaMax = deltaMax;
			this.seeMax = seeMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			deltaMin = is.popIntegerList();
			deltaMax = is.popIntegerList();
			seeMax = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushIntegerList(deltaMin);
			os.pushIntegerList(deltaMax);
			os.pushInteger(seeMax);
		}

		public int rankFloor;
		public List<Integer> deltaMin;
		public List<Integer> deltaMax;
		public int seeMax;
	}

	public static class ArenaCFGS implements Stream.IStreamable
	{

		public ArenaCFGS() { }

		public ArenaCFGS(short refreshTime, byte lvlReq, byte freeTimes, byte stoneRefresh, 
		                 byte coolTime, short combatId, List<Short> timesPrice, int rankMax, 
		                 List<ArenaRewardCFGS> rewards, List<ArenaBestRankRewardCFGS> bestRankRewards, List<ArenaTargetCFGS> targets)
		{
			this.refreshTime = refreshTime;
			this.lvlReq = lvlReq;
			this.freeTimes = freeTimes;
			this.stoneRefresh = stoneRefresh;
			this.coolTime = coolTime;
			this.combatId = combatId;
			this.timesPrice = timesPrice;
			this.rankMax = rankMax;
			this.rewards = rewards;
			this.bestRankRewards = bestRankRewards;
			this.targets = targets;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			refreshTime = is.popShort();
			lvlReq = is.popByte();
			freeTimes = is.popByte();
			stoneRefresh = is.popByte();
			coolTime = is.popByte();
			combatId = is.popShort();
			timesPrice = is.popShortList();
			rankMax = is.popInteger();
			rewards = is.popList(ArenaRewardCFGS.class);
			bestRankRewards = is.popList(ArenaBestRankRewardCFGS.class);
			targets = is.popList(ArenaTargetCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(refreshTime);
			os.pushByte(lvlReq);
			os.pushByte(freeTimes);
			os.pushByte(stoneRefresh);
			os.pushByte(coolTime);
			os.pushShort(combatId);
			os.pushShortList(timesPrice);
			os.pushInteger(rankMax);
			os.pushList(rewards);
			os.pushList(bestRankRewards);
			os.pushList(targets);
		}

		public short refreshTime;
		public byte lvlReq;
		public byte freeTimes;
		public byte stoneRefresh;
		public byte coolTime;
		public short combatId;
		public List<Short> timesPrice;
		public int rankMax;
		public List<ArenaRewardCFGS> rewards;
		public List<ArenaBestRankRewardCFGS> bestRankRewards;
		public List<ArenaTargetCFGS> targets;
	}

	public static class SuperArenaRewardCFGS implements Stream.IStreamable
	{

		public SuperArenaRewardCFGS() { }

		public SuperArenaRewardCFGS(int rankFloor, int point)
		{
			this.rankFloor = rankFloor;
			this.point = point;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			point = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushInteger(point);
		}

		public int rankFloor;
		public int point;
	}

	public static class SuperArenaRankCFGS implements Stream.IStreamable
	{

		public SuperArenaRankCFGS() { }

		public SuperArenaRankCFGS(int deltaMin, int deltaMax)
		{
			this.deltaMin = deltaMin;
			this.deltaMax = deltaMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			deltaMin = is.popInteger();
			deltaMax = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(deltaMin);
			os.pushInteger(deltaMax);
		}

		public int deltaMin;
		public int deltaMax;
	}

	public static class SuperArenaTargetCFGS implements Stream.IStreamable
	{

		public SuperArenaTargetCFGS() { }

		public SuperArenaTargetCFGS(int rankFloor, List<SuperArenaRankCFGS> deltaRanks)
		{
			this.rankFloor = rankFloor;
			this.deltaRanks = deltaRanks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			deltaRanks = is.popList(SuperArenaRankCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushList(deltaRanks);
		}

		public int rankFloor;
		public List<SuperArenaRankCFGS> deltaRanks;
	}

	public static class SuperArenaCFGS implements Stream.IStreamable
	{

		public SuperArenaCFGS() { }

		public SuperArenaCFGS(byte lvlReq, byte freeTimes, byte stoneRefresh, byte coolTime, 
		                      short combatId, List<Short> timesPrice, int hideTeamDaySpan, List<Integer> hideTeamCost, 
		                      int hideOrderVipReq, short fightStartTime, short fightEndTime, int fightWinReward, 
		                      int fightLoseReward, List<SuperArenaTargetCFGS> targets, List<SuperArenaRewardCFGS> rewards)
		{
			this.lvlReq = lvlReq;
			this.freeTimes = freeTimes;
			this.stoneRefresh = stoneRefresh;
			this.coolTime = coolTime;
			this.combatId = combatId;
			this.timesPrice = timesPrice;
			this.hideTeamDaySpan = hideTeamDaySpan;
			this.hideTeamCost = hideTeamCost;
			this.hideOrderVipReq = hideOrderVipReq;
			this.fightStartTime = fightStartTime;
			this.fightEndTime = fightEndTime;
			this.fightWinReward = fightWinReward;
			this.fightLoseReward = fightLoseReward;
			this.targets = targets;
			this.rewards = rewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlReq = is.popByte();
			freeTimes = is.popByte();
			stoneRefresh = is.popByte();
			coolTime = is.popByte();
			combatId = is.popShort();
			timesPrice = is.popShortList();
			hideTeamDaySpan = is.popInteger();
			hideTeamCost = is.popIntegerList();
			hideOrderVipReq = is.popInteger();
			fightStartTime = is.popShort();
			fightEndTime = is.popShort();
			fightWinReward = is.popInteger();
			fightLoseReward = is.popInteger();
			targets = is.popList(SuperArenaTargetCFGS.class);
			rewards = is.popList(SuperArenaRewardCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lvlReq);
			os.pushByte(freeTimes);
			os.pushByte(stoneRefresh);
			os.pushByte(coolTime);
			os.pushShort(combatId);
			os.pushShortList(timesPrice);
			os.pushInteger(hideTeamDaySpan);
			os.pushIntegerList(hideTeamCost);
			os.pushInteger(hideOrderVipReq);
			os.pushShort(fightStartTime);
			os.pushShort(fightEndTime);
			os.pushInteger(fightWinReward);
			os.pushInteger(fightLoseReward);
			os.pushList(targets);
			os.pushList(rewards);
		}

		public byte lvlReq;
		public byte freeTimes;
		public byte stoneRefresh;
		public byte coolTime;
		public short combatId;
		public List<Short> timesPrice;
		public int hideTeamDaySpan;
		public List<Integer> hideTeamCost;
		public int hideOrderVipReq;
		public short fightStartTime;
		public short fightEndTime;
		public int fightWinReward;
		public int fightLoseReward;
		public List<SuperArenaTargetCFGS> targets;
		public List<SuperArenaRewardCFGS> rewards;
	}

	public static class ArenaRecord implements Stream.IStreamable
	{

		public ArenaRecord() { }

		public ArenaRecord(int rid1, int rid2, int headIconId1, int headIconId2, 
		                   String name1, String name2, short lvl1, short lvl2, 
		                   List<i3k.DBRoleGeneral> generals1, List<i3k.DBRoleGeneral> generals2, List<DBPetBrief> pets1, List<DBPetBrief> pets2, 
		                   List<DBRelation> relation1, List<DBRelation> relation2, int time, int seed, 
		                   byte result, int index, List<DBGeneralStone> gStones1, List<DBGeneralStone> gStones2)
		{
			this.rid1 = rid1;
			this.rid2 = rid2;
			this.headIconId1 = headIconId1;
			this.headIconId2 = headIconId2;
			this.name1 = name1;
			this.name2 = name2;
			this.lvl1 = lvl1;
			this.lvl2 = lvl2;
			this.generals1 = generals1;
			this.generals2 = generals2;
			this.pets1 = pets1;
			this.pets2 = pets2;
			this.relation1 = relation1;
			this.relation2 = relation2;
			this.time = time;
			this.seed = seed;
			this.result = result;
			this.index = index;
			this.gStones1 = gStones1;
			this.gStones2 = gStones2;
		}

		public ArenaRecord ksClone()
		{
			return new ArenaRecord(rid1, rid2, headIconId1, headIconId2, 
			                       name1, name2, lvl1, lvl2, 
			                       generals1, generals2, pets1, pets2, 
			                       relation1, relation2, time, seed, 
			                       result, index, gStones1, gStones2);
		}

		public ArenaRecord kdClone()
		{
			ArenaRecord _kio_clobj = ksClone();
			_kio_clobj.generals1 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals1)
			{
				_kio_clobj.generals1.add(_kio_iter.kdClone());
			}
			_kio_clobj.generals2 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals2)
			{
				_kio_clobj.generals2.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets1 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets1)
			{
				_kio_clobj.pets1.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets2 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets2)
			{
				_kio_clobj.pets2.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation1 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation1)
			{
				_kio_clobj.relation1.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation2 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation2)
			{
				_kio_clobj.relation2.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones1 = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones1)
			{
				_kio_clobj.gStones1.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones2 = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones2)
			{
				_kio_clobj.gStones2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid1 = is.popInteger();
			rid2 = is.popInteger();
			headIconId1 = is.popInteger();
			headIconId2 = is.popInteger();
			name1 = is.popString();
			name2 = is.popString();
			lvl1 = is.popShort();
			lvl2 = is.popShort();
			generals1 = is.popList(i3k.DBRoleGeneral.class);
			generals2 = is.popList(i3k.DBRoleGeneral.class);
			pets1 = is.popList(DBPetBrief.class);
			pets2 = is.popList(DBPetBrief.class);
			relation1 = is.popList(DBRelation.class);
			relation2 = is.popList(DBRelation.class);
			time = is.popInteger();
			seed = is.popInteger();
			result = is.popByte();
			index = is.popInteger();
			gStones1 = is.popList(DBGeneralStone.class);
			gStones2 = is.popList(DBGeneralStone.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid1);
			os.pushInteger(rid2);
			os.pushInteger(headIconId1);
			os.pushInteger(headIconId2);
			os.pushString(name1);
			os.pushString(name2);
			os.pushShort(lvl1);
			os.pushShort(lvl2);
			os.pushList(generals1);
			os.pushList(generals2);
			os.pushList(pets1);
			os.pushList(pets2);
			os.pushList(relation1);
			os.pushList(relation2);
			os.pushInteger(time);
			os.pushInteger(seed);
			os.pushByte(result);
			os.pushInteger(index);
			os.pushList(gStones1);
			os.pushList(gStones2);
		}

		public int rid1;
		public int rid2;
		public int headIconId1;
		public int headIconId2;
		public String name1;
		public String name2;
		public short lvl1;
		public short lvl2;
		public List<i3k.DBRoleGeneral> generals1;
		public List<i3k.DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<DBRelation> relation1;
		public List<DBRelation> relation2;
		public int time;
		public int seed;
		public byte result;
		public int index;
		public List<DBGeneralStone> gStones1;
		public List<DBGeneralStone> gStones2;
	}

	public static class SuperArenaTeamDetail implements Stream.IStreamable
	{

		public SuperArenaTeamDetail() { }

		public SuperArenaTeamDetail(List<i3k.DBRoleGeneral> generals, List<DBPetBrief> pets, List<DBRelation> relation, List<DBGeneralStone> gStones)
		{
			this.generals = generals;
			this.pets = pets;
			this.relation = relation;
			this.gStones = gStones;
		}

		public SuperArenaTeamDetail ksClone()
		{
			return new SuperArenaTeamDetail(generals, pets, relation, gStones);
		}

		public SuperArenaTeamDetail kdClone()
		{
			SuperArenaTeamDetail _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation)
			{
				_kio_clobj.relation.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones)
			{
				_kio_clobj.gStones.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
			relation = is.popList(DBRelation.class);
			gStones = is.popList(DBGeneralStone.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(generals);
			os.pushList(pets);
			os.pushList(relation);
			os.pushList(gStones);
		}

		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
		public List<DBRelation> relation;
		public List<DBGeneralStone> gStones;
	}

	public static class SuperArenaFightInfo implements Stream.IStreamable
	{

		public SuperArenaFightInfo() { }

		public SuperArenaFightInfo(int rid, int headIconId, String name, short lvl, 
		                           List<SuperArenaTeamDetail> teams)
		{
			this.rid = rid;
			this.headIconId = headIconId;
			this.name = name;
			this.lvl = lvl;
			this.teams = teams;
		}

		public SuperArenaFightInfo ksClone()
		{
			return new SuperArenaFightInfo(rid, headIconId, name, lvl, 
			                               teams);
		}

		public SuperArenaFightInfo kdClone()
		{
			SuperArenaFightInfo _kio_clobj = ksClone();
			_kio_clobj.teams = new ArrayList<SuperArenaTeamDetail>();
			for(SuperArenaTeamDetail _kio_iter : teams)
			{
				_kio_clobj.teams.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			headIconId = is.popInteger();
			name = is.popString();
			lvl = is.popShort();
			teams = is.popList(SuperArenaTeamDetail.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushInteger(headIconId);
			os.pushString(name);
			os.pushShort(lvl);
			os.pushList(teams);
		}

		public int rid;
		public int headIconId;
		public String name;
		public short lvl;
		public List<SuperArenaTeamDetail> teams;
	}

	public static class SuperArenaRecord implements Stream.IStreamable
	{

		public SuperArenaRecord() { }

		public SuperArenaRecord(SuperArenaFightInfo fteam1, SuperArenaFightInfo fteam2, int seed, int time, 
		                        List<Integer> result, int id)
		{
			this.fteam1 = fteam1;
			this.fteam2 = fteam2;
			this.seed = seed;
			this.time = time;
			this.result = result;
			this.id = id;
		}

		public SuperArenaRecord ksClone()
		{
			return new SuperArenaRecord(fteam1, fteam2, seed, time, 
			                            result, id);
		}

		public SuperArenaRecord kdClone()
		{
			SuperArenaRecord _kio_clobj = ksClone();
			_kio_clobj.fteam1 = fteam1.kdClone();
			_kio_clobj.fteam2 = fteam2.kdClone();
			_kio_clobj.result = new ArrayList<Integer>();
			for(Integer _kio_iter : result)
			{
				_kio_clobj.result.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( fteam1 == null )
				fteam1 = new SuperArenaFightInfo();
			is.pop(fteam1);
			if( fteam2 == null )
				fteam2 = new SuperArenaFightInfo();
			is.pop(fteam2);
			seed = is.popInteger();
			time = is.popInteger();
			result = is.popIntegerList();
			id = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(fteam1);
			os.push(fteam2);
			os.pushInteger(seed);
			os.pushInteger(time);
			os.pushIntegerList(result);
			os.pushInteger(id);
		}

		public SuperArenaFightInfo fteam1;
		public SuperArenaFightInfo fteam2;
		public int seed;
		public int time;
		public List<Integer> result;
		public int id;
	}

	public static class MarchDropCFGS implements Stream.IStreamable
	{

		public MarchDropCFGS() { }

		public MarchDropCFGS(byte maxLvl, short drop)
		{
			this.maxLvl = maxLvl;
			this.drop = drop;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			maxLvl = is.popByte();
			drop = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(maxLvl);
			os.pushShort(drop);
		}

		public byte maxLvl;
		public short drop;
	}

	public static class MarchStageCFGS implements Stream.IStreamable
	{

		public MarchStageCFGS() { }

		public MarchStageCFGS(short combatId, int money1, int money2, short point, 
		                      List<MarchDropCFGS> drops, float fightPowerScale)
		{
			this.combatId = combatId;
			this.money1 = money1;
			this.money2 = money2;
			this.point = point;
			this.drops = drops;
			this.fightPowerScale = fightPowerScale;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			money1 = is.popInteger();
			money2 = is.popInteger();
			point = is.popShort();
			drops = is.popList(MarchDropCFGS.class);
			fightPowerScale = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushInteger(money1);
			os.pushInteger(money2);
			os.pushShort(point);
			os.pushList(drops);
			os.pushFloat(fightPowerScale);
		}

		public short combatId;
		public int money1;
		public int money2;
		public short point;
		public List<MarchDropCFGS> drops;
		public float fightPowerScale;
	}

	public static class MarchTargetCFGS implements Stream.IStreamable
	{

		public MarchTargetCFGS() { }

		public MarchTargetCFGS(int rankFloor, List<Integer> deltaMin, List<Integer> deltaMax)
		{
			this.rankFloor = rankFloor;
			this.deltaMin = deltaMin;
			this.deltaMax = deltaMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			deltaMin = is.popIntegerList();
			deltaMax = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushIntegerList(deltaMin);
			os.pushIntegerList(deltaMax);
		}

		public int rankFloor;
		public List<Integer> deltaMin;
		public List<Integer> deltaMax;
	}

	public static class MarchPowerUpCFGS implements Stream.IStreamable
	{

		public MarchPowerUpCFGS() { }

		public MarchPowerUpCFGS(byte stage, short powerUpByDay)
		{
			this.stage = stage;
			this.powerUpByDay = powerUpByDay;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stage = is.popByte();
			powerUpByDay = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(stage);
			os.pushShort(powerUpByDay);
		}

		public byte stage;
		public short powerUpByDay;
	}

	public static class MarchOpponentAdjustCFGS implements Stream.IStreamable
	{

		public MarchOpponentAdjustCFGS() { }

		public MarchOpponentAdjustCFGS(short roleLevelMax, List<Float> hpScale, List<Float> attackScale)
		{
			this.roleLevelMax = roleLevelMax;
			this.hpScale = hpScale;
			this.attackScale = attackScale;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleLevelMax = is.popShort();
			hpScale = is.popFloatList();
			attackScale = is.popFloatList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(roleLevelMax);
			os.pushFloatList(hpScale);
			os.pushFloatList(attackScale);
		}

		public short roleLevelMax;
		public List<Float> hpScale;
		public List<Float> attackScale;
	}

	public static class MarchCFGS implements Stream.IStreamable
	{

		public MarchCFGS() { }

		public MarchCFGS(short refreshTime, byte lvlReq, short wipeItemId, byte powerUpDays, 
		                 byte powerDownDays, byte powerUpStage, byte powerDownStage, List<MarchStageCFGS> stages, 
		                 List<MarchTargetCFGS> targets, List<MarchPowerUpCFGS> powerUps, List<MarchPowerUpCFGS> powerDowns, List<MarchOpponentAdjustCFGS> opponentAdjust)
		{
			this.refreshTime = refreshTime;
			this.lvlReq = lvlReq;
			this.wipeItemId = wipeItemId;
			this.powerUpDays = powerUpDays;
			this.powerDownDays = powerDownDays;
			this.powerUpStage = powerUpStage;
			this.powerDownStage = powerDownStage;
			this.stages = stages;
			this.targets = targets;
			this.powerUps = powerUps;
			this.powerDowns = powerDowns;
			this.opponentAdjust = opponentAdjust;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			refreshTime = is.popShort();
			lvlReq = is.popByte();
			wipeItemId = is.popShort();
			powerUpDays = is.popByte();
			powerDownDays = is.popByte();
			powerUpStage = is.popByte();
			powerDownStage = is.popByte();
			stages = is.popList(MarchStageCFGS.class);
			targets = is.popList(MarchTargetCFGS.class);
			powerUps = is.popList(MarchPowerUpCFGS.class);
			powerDowns = is.popList(MarchPowerUpCFGS.class);
			opponentAdjust = is.popList(MarchOpponentAdjustCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(refreshTime);
			os.pushByte(lvlReq);
			os.pushShort(wipeItemId);
			os.pushByte(powerUpDays);
			os.pushByte(powerDownDays);
			os.pushByte(powerUpStage);
			os.pushByte(powerDownStage);
			os.pushList(stages);
			os.pushList(targets);
			os.pushList(powerUps);
			os.pushList(powerDowns);
			os.pushList(opponentAdjust);
		}

		public short refreshTime;
		public byte lvlReq;
		public short wipeItemId;
		public byte powerUpDays;
		public byte powerDownDays;
		public byte powerUpStage;
		public byte powerDownStage;
		public List<MarchStageCFGS> stages;
		public List<MarchTargetCFGS> targets;
		public List<MarchPowerUpCFGS> powerUps;
		public List<MarchPowerUpCFGS> powerDowns;
		public List<MarchOpponentAdjustCFGS> opponentAdjust;
	}

	public static class PowerCFGS implements Stream.IStreamable
	{

		public PowerCFGS() { }

		public PowerCFGS(short id, int initPower, int upPower)
		{
			this.id = id;
			this.initPower = initPower;
			this.upPower = upPower;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			initPower = is.popInteger();
			upPower = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(initPower);
			os.pushInteger(upPower);
		}

		public short id;
		public int initPower;
		public int upPower;
	}

	public static class RandomNameCFG implements Stream.IStreamable
	{

		public RandomNameCFG() { }

		public RandomNameCFG(List<String> lastNames, List<String> mnames, List<String> fnames, List<String> robotNames)
		{
			this.lastNames = lastNames;
			this.mnames = mnames;
			this.fnames = fnames;
			this.robotNames = robotNames;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lastNames = is.popStringList();
			mnames = is.popStringList();
			fnames = is.popStringList();
			robotNames = is.popStringList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushStringList(lastNames);
			os.pushStringList(mnames);
			os.pushStringList(fnames);
			os.pushStringList(robotNames);
		}

		public List<String> lastNames;
		public List<String> mnames;
		public List<String> fnames;
		public List<String> robotNames;
	}

	public static class CombatServerCFG implements Stream.IStreamable
	{

		public CombatServerCFG() { }

		public CombatServerCFG(short skipCombatItemID)
		{
			this.skipCombatItemID = skipCombatItemID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			skipCombatItemID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(skipCombatItemID);
		}

		public short skipCombatItemID;
	}

	public static class CombatClientCFG implements Stream.IStreamable
	{

		public CombatClientCFG() { }

		public CombatClientCFG(short skipCombatItemID, float randomDamageRadius, byte showGeneralName, short shadowEffectID, 
		                       short moveTargetEffectID, short enemySelectEffectID, short allySelectEffectID, short attackRangeEffectID, 
		                       short healEffectID, short damageEffectID, short healCriticalEffectID, short damageCriticalEffectID, 
		                       short hseTime, short generalRefreshInterval, byte deathDelTime, short commonSkillCool, 
		                       short winEffectID, short loseEffectID, short ydbEffectID, short winEffectTime, 
		                       short loseEffectTime, short ydbEffectTime, short maxPersistantAttackTime, float escapeHP, 
		                       float continueFightHP, float aoeEffectRange, float healHP, short aoeInterval, 
		                       byte aoeEnemyCount, short firstCombatID, byte firstCombatGeneralLevel, List<Short> firstCombatGenerals, 
		                       short dlgIDGeneralStart, short dlgIDAssistantGeneralStart, short dlgIDEnemyGeneralStart, short dlgIDDie, 
		                       short dlgIDIdle, short dlgIDKill, short dlgIDCry, float dlgStartHeroProp, 
		                       float dlgStartAssistantProp, float dlgStartEnemyProp, float dlgShoutProp, float dlgShoutUseProp, 
		                       float dlgDieProp, float dlgIdleProp, float dlgKillProp)
		{
			this.skipCombatItemID = skipCombatItemID;
			this.randomDamageRadius = randomDamageRadius;
			this.showGeneralName = showGeneralName;
			this.shadowEffectID = shadowEffectID;
			this.moveTargetEffectID = moveTargetEffectID;
			this.enemySelectEffectID = enemySelectEffectID;
			this.allySelectEffectID = allySelectEffectID;
			this.attackRangeEffectID = attackRangeEffectID;
			this.healEffectID = healEffectID;
			this.damageEffectID = damageEffectID;
			this.healCriticalEffectID = healCriticalEffectID;
			this.damageCriticalEffectID = damageCriticalEffectID;
			this.hseTime = hseTime;
			this.generalRefreshInterval = generalRefreshInterval;
			this.deathDelTime = deathDelTime;
			this.commonSkillCool = commonSkillCool;
			this.winEffectID = winEffectID;
			this.loseEffectID = loseEffectID;
			this.ydbEffectID = ydbEffectID;
			this.winEffectTime = winEffectTime;
			this.loseEffectTime = loseEffectTime;
			this.ydbEffectTime = ydbEffectTime;
			this.maxPersistantAttackTime = maxPersistantAttackTime;
			this.escapeHP = escapeHP;
			this.continueFightHP = continueFightHP;
			this.aoeEffectRange = aoeEffectRange;
			this.healHP = healHP;
			this.aoeInterval = aoeInterval;
			this.aoeEnemyCount = aoeEnemyCount;
			this.firstCombatID = firstCombatID;
			this.firstCombatGeneralLevel = firstCombatGeneralLevel;
			this.firstCombatGenerals = firstCombatGenerals;
			this.dlgIDGeneralStart = dlgIDGeneralStart;
			this.dlgIDAssistantGeneralStart = dlgIDAssistantGeneralStart;
			this.dlgIDEnemyGeneralStart = dlgIDEnemyGeneralStart;
			this.dlgIDDie = dlgIDDie;
			this.dlgIDIdle = dlgIDIdle;
			this.dlgIDKill = dlgIDKill;
			this.dlgIDCry = dlgIDCry;
			this.dlgStartHeroProp = dlgStartHeroProp;
			this.dlgStartAssistantProp = dlgStartAssistantProp;
			this.dlgStartEnemyProp = dlgStartEnemyProp;
			this.dlgShoutProp = dlgShoutProp;
			this.dlgShoutUseProp = dlgShoutUseProp;
			this.dlgDieProp = dlgDieProp;
			this.dlgIdleProp = dlgIdleProp;
			this.dlgKillProp = dlgKillProp;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			skipCombatItemID = is.popShort();
			randomDamageRadius = is.popFloat();
			showGeneralName = is.popByte();
			shadowEffectID = is.popShort();
			moveTargetEffectID = is.popShort();
			enemySelectEffectID = is.popShort();
			allySelectEffectID = is.popShort();
			attackRangeEffectID = is.popShort();
			healEffectID = is.popShort();
			damageEffectID = is.popShort();
			healCriticalEffectID = is.popShort();
			damageCriticalEffectID = is.popShort();
			hseTime = is.popShort();
			generalRefreshInterval = is.popShort();
			deathDelTime = is.popByte();
			commonSkillCool = is.popShort();
			winEffectID = is.popShort();
			loseEffectID = is.popShort();
			ydbEffectID = is.popShort();
			winEffectTime = is.popShort();
			loseEffectTime = is.popShort();
			ydbEffectTime = is.popShort();
			maxPersistantAttackTime = is.popShort();
			escapeHP = is.popFloat();
			continueFightHP = is.popFloat();
			aoeEffectRange = is.popFloat();
			healHP = is.popFloat();
			aoeInterval = is.popShort();
			aoeEnemyCount = is.popByte();
			firstCombatID = is.popShort();
			firstCombatGeneralLevel = is.popByte();
			firstCombatGenerals = is.popShortList();
			dlgIDGeneralStart = is.popShort();
			dlgIDAssistantGeneralStart = is.popShort();
			dlgIDEnemyGeneralStart = is.popShort();
			dlgIDDie = is.popShort();
			dlgIDIdle = is.popShort();
			dlgIDKill = is.popShort();
			dlgIDCry = is.popShort();
			dlgStartHeroProp = is.popFloat();
			dlgStartAssistantProp = is.popFloat();
			dlgStartEnemyProp = is.popFloat();
			dlgShoutProp = is.popFloat();
			dlgShoutUseProp = is.popFloat();
			dlgDieProp = is.popFloat();
			dlgIdleProp = is.popFloat();
			dlgKillProp = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(skipCombatItemID);
			os.pushFloat(randomDamageRadius);
			os.pushByte(showGeneralName);
			os.pushShort(shadowEffectID);
			os.pushShort(moveTargetEffectID);
			os.pushShort(enemySelectEffectID);
			os.pushShort(allySelectEffectID);
			os.pushShort(attackRangeEffectID);
			os.pushShort(healEffectID);
			os.pushShort(damageEffectID);
			os.pushShort(healCriticalEffectID);
			os.pushShort(damageCriticalEffectID);
			os.pushShort(hseTime);
			os.pushShort(generalRefreshInterval);
			os.pushByte(deathDelTime);
			os.pushShort(commonSkillCool);
			os.pushShort(winEffectID);
			os.pushShort(loseEffectID);
			os.pushShort(ydbEffectID);
			os.pushShort(winEffectTime);
			os.pushShort(loseEffectTime);
			os.pushShort(ydbEffectTime);
			os.pushShort(maxPersistantAttackTime);
			os.pushFloat(escapeHP);
			os.pushFloat(continueFightHP);
			os.pushFloat(aoeEffectRange);
			os.pushFloat(healHP);
			os.pushShort(aoeInterval);
			os.pushByte(aoeEnemyCount);
			os.pushShort(firstCombatID);
			os.pushByte(firstCombatGeneralLevel);
			os.pushShortList(firstCombatGenerals);
			os.pushShort(dlgIDGeneralStart);
			os.pushShort(dlgIDAssistantGeneralStart);
			os.pushShort(dlgIDEnemyGeneralStart);
			os.pushShort(dlgIDDie);
			os.pushShort(dlgIDIdle);
			os.pushShort(dlgIDKill);
			os.pushShort(dlgIDCry);
			os.pushFloat(dlgStartHeroProp);
			os.pushFloat(dlgStartAssistantProp);
			os.pushFloat(dlgStartEnemyProp);
			os.pushFloat(dlgShoutProp);
			os.pushFloat(dlgShoutUseProp);
			os.pushFloat(dlgDieProp);
			os.pushFloat(dlgIdleProp);
			os.pushFloat(dlgKillProp);
		}

		public short skipCombatItemID;
		public float randomDamageRadius;
		public byte showGeneralName;
		public short shadowEffectID;
		public short moveTargetEffectID;
		public short enemySelectEffectID;
		public short allySelectEffectID;
		public short attackRangeEffectID;
		public short healEffectID;
		public short damageEffectID;
		public short healCriticalEffectID;
		public short damageCriticalEffectID;
		public short hseTime;
		public short generalRefreshInterval;
		public byte deathDelTime;
		public short commonSkillCool;
		public short winEffectID;
		public short loseEffectID;
		public short ydbEffectID;
		public short winEffectTime;
		public short loseEffectTime;
		public short ydbEffectTime;
		public short maxPersistantAttackTime;
		public float escapeHP;
		public float continueFightHP;
		public float aoeEffectRange;
		public float healHP;
		public short aoeInterval;
		public byte aoeEnemyCount;
		public short firstCombatID;
		public byte firstCombatGeneralLevel;
		public List<Short> firstCombatGenerals;
		public short dlgIDGeneralStart;
		public short dlgIDAssistantGeneralStart;
		public short dlgIDEnemyGeneralStart;
		public short dlgIDDie;
		public short dlgIDIdle;
		public short dlgIDKill;
		public short dlgIDCry;
		public float dlgStartHeroProp;
		public float dlgStartAssistantProp;
		public float dlgStartEnemyProp;
		public float dlgShoutProp;
		public float dlgShoutUseProp;
		public float dlgDieProp;
		public float dlgIdleProp;
		public float dlgKillProp;
	}

	public static class MusicCFG implements Stream.IStreamable
	{

		public MusicCFG() { }

		public MusicCFG(String cityMusic, String combatMusic, String createRoleMusic, String pvpMusic)
		{
			this.cityMusic = cityMusic;
			this.combatMusic = combatMusic;
			this.createRoleMusic = createRoleMusic;
			this.pvpMusic = pvpMusic;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cityMusic = is.popString();
			combatMusic = is.popString();
			createRoleMusic = is.popString();
			pvpMusic = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(cityMusic);
			os.pushString(combatMusic);
			os.pushString(createRoleMusic);
			os.pushString(pvpMusic);
		}

		public String cityMusic;
		public String combatMusic;
		public String createRoleMusic;
		public String pvpMusic;
	}

	public static class PVPMapCFG implements Stream.IStreamable
	{

		public PVPMapCFG() { }

		public PVPMapCFG(String map, List<Float3> pos)
		{
			this.map = map;
			this.pos = pos;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			map = is.popString();
			pos = is.popList(Float3.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(map);
			os.pushList(pos);
		}

		public String map;
		public List<Float3> pos;
	}

	public static class MapCFG implements Stream.IStreamable
	{

		public MapCFG() { }

		public MapCFG(String cityMap, String createRoleMap, String createRoleScript, float createRoleMapSpeed, 
		              byte createRoleMapEnable, List<PVPMapCFG> pvpMaps, String heroBoxName, String heroBoxName2)
		{
			this.cityMap = cityMap;
			this.createRoleMap = createRoleMap;
			this.createRoleScript = createRoleScript;
			this.createRoleMapSpeed = createRoleMapSpeed;
			this.createRoleMapEnable = createRoleMapEnable;
			this.pvpMaps = pvpMaps;
			this.heroBoxName = heroBoxName;
			this.heroBoxName2 = heroBoxName2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cityMap = is.popString();
			createRoleMap = is.popString();
			createRoleScript = is.popString();
			createRoleMapSpeed = is.popFloat();
			createRoleMapEnable = is.popByte();
			pvpMaps = is.popList(PVPMapCFG.class);
			heroBoxName = is.popString();
			heroBoxName2 = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(cityMap);
			os.pushString(createRoleMap);
			os.pushString(createRoleScript);
			os.pushFloat(createRoleMapSpeed);
			os.pushByte(createRoleMapEnable);
			os.pushList(pvpMaps);
			os.pushString(heroBoxName);
			os.pushString(heroBoxName2);
		}

		public String cityMap;
		public String createRoleMap;
		public String createRoleScript;
		public float createRoleMapSpeed;
		public byte createRoleMapEnable;
		public List<PVPMapCFG> pvpMaps;
		public String heroBoxName;
		public String heroBoxName2;
	}

	public static class AlarmCFG implements Stream.IStreamable
	{

		public AlarmCFG() { }

		public AlarmCFG(short time, String content)
		{
			this.time = time;
			this.content = content;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			time = is.popShort();
			content = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(time);
			os.pushString(content);
		}

		public short time;
		public String content;
	}

	public static class PetFarmGrowthRateCFG implements Stream.IStreamable
	{

		public PetFarmGrowthRateCFG() { }

		public PetFarmGrowthRateCFG(short growthRateMax, byte addCount)
		{
			this.growthRateMax = growthRateMax;
			this.addCount = addCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			growthRateMax = is.popShort();
			addCount = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(growthRateMax);
			os.pushByte(addCount);
		}

		public short growthRateMax;
		public byte addCount;
	}

	public static class PetFarmCFGS implements Stream.IStreamable
	{

		public PetFarmCFGS() { }

		public PetFarmCFGS(short lvlOpen, short lvlPet, short firstHungry, short hungryTimeBase, 
		                   short hungryTimeFloat, byte maxEatTimes, byte petMaxEatTimes, short noHungryTimeBase, 
		                   int noHungryTimeSpan, byte eatGainBase, byte eatGainFloat, byte produceCost, 
		                   short hungryEffect, short produceEffect, short getProductEffect, short loyaltyEffect, 
		                   List<PetFarmGrowthRateCFG> growthRateAffect)
		{
			this.lvlOpen = lvlOpen;
			this.lvlPet = lvlPet;
			this.firstHungry = firstHungry;
			this.hungryTimeBase = hungryTimeBase;
			this.hungryTimeFloat = hungryTimeFloat;
			this.maxEatTimes = maxEatTimes;
			this.petMaxEatTimes = petMaxEatTimes;
			this.noHungryTimeBase = noHungryTimeBase;
			this.noHungryTimeSpan = noHungryTimeSpan;
			this.eatGainBase = eatGainBase;
			this.eatGainFloat = eatGainFloat;
			this.produceCost = produceCost;
			this.hungryEffect = hungryEffect;
			this.produceEffect = produceEffect;
			this.getProductEffect = getProductEffect;
			this.loyaltyEffect = loyaltyEffect;
			this.growthRateAffect = growthRateAffect;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlOpen = is.popShort();
			lvlPet = is.popShort();
			firstHungry = is.popShort();
			hungryTimeBase = is.popShort();
			hungryTimeFloat = is.popShort();
			maxEatTimes = is.popByte();
			petMaxEatTimes = is.popByte();
			noHungryTimeBase = is.popShort();
			noHungryTimeSpan = is.popInteger();
			eatGainBase = is.popByte();
			eatGainFloat = is.popByte();
			produceCost = is.popByte();
			hungryEffect = is.popShort();
			produceEffect = is.popShort();
			getProductEffect = is.popShort();
			loyaltyEffect = is.popShort();
			growthRateAffect = is.popList(PetFarmGrowthRateCFG.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlOpen);
			os.pushShort(lvlPet);
			os.pushShort(firstHungry);
			os.pushShort(hungryTimeBase);
			os.pushShort(hungryTimeFloat);
			os.pushByte(maxEatTimes);
			os.pushByte(petMaxEatTimes);
			os.pushShort(noHungryTimeBase);
			os.pushInteger(noHungryTimeSpan);
			os.pushByte(eatGainBase);
			os.pushByte(eatGainFloat);
			os.pushByte(produceCost);
			os.pushShort(hungryEffect);
			os.pushShort(produceEffect);
			os.pushShort(getProductEffect);
			os.pushShort(loyaltyEffect);
			os.pushList(growthRateAffect);
		}

		public short lvlOpen;
		public short lvlPet;
		public short firstHungry;
		public short hungryTimeBase;
		public short hungryTimeFloat;
		public byte maxEatTimes;
		public byte petMaxEatTimes;
		public short noHungryTimeBase;
		public int noHungryTimeSpan;
		public byte eatGainBase;
		public byte eatGainFloat;
		public byte produceCost;
		public short hungryEffect;
		public short produceEffect;
		public short getProductEffect;
		public short loyaltyEffect;
		public List<PetFarmGrowthRateCFG> growthRateAffect;
	}

	public static class UserInputCFGS implements Stream.IStreamable
	{

		public UserInputCFGS() { }

		public UserInputCFGS(byte roleName, byte userName, byte forceName, byte forceAnnounce, 
		                     byte forceQQ, byte worldChat, byte mail, short sysMail, 
		                     byte gmAnnounce)
		{
			this.roleName = roleName;
			this.userName = userName;
			this.forceName = forceName;
			this.forceAnnounce = forceAnnounce;
			this.forceQQ = forceQQ;
			this.worldChat = worldChat;
			this.mail = mail;
			this.sysMail = sysMail;
			this.gmAnnounce = gmAnnounce;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleName = is.popByte();
			userName = is.popByte();
			forceName = is.popByte();
			forceAnnounce = is.popByte();
			forceQQ = is.popByte();
			worldChat = is.popByte();
			mail = is.popByte();
			sysMail = is.popShort();
			gmAnnounce = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(roleName);
			os.pushByte(userName);
			os.pushByte(forceName);
			os.pushByte(forceAnnounce);
			os.pushByte(forceQQ);
			os.pushByte(worldChat);
			os.pushByte(mail);
			os.pushShort(sysMail);
			os.pushByte(gmAnnounce);
		}

		public byte roleName;
		public byte userName;
		public byte forceName;
		public byte forceAnnounce;
		public byte forceQQ;
		public byte worldChat;
		public byte mail;
		public short sysMail;
		public byte gmAnnounce;
	}

	public static class EngineCFG implements Stream.IStreamable
	{

		public EngineCFG() { }

		public EngineCFG(String horseHS, String horseCS, float horseTitleOffset, String texBloodGreen, 
		                 String texBloodRed, String texNumberEffectGreen, String texNumberEffectRed, short durNumberEffect, 
		                 float gravity, byte useCollision, byte useKPack, byte cameraFollowHero, 
		                 byte showFPSOnHead, byte showErrorOnHead, String defaultFont, String defaultStandAction, 
		                 String defaultRunAction, String defaultAttackAction, List<String> defaultHurtAction, List<String> defaultDeathAction, 
		                 List<String> defaultDeathLoopAction, String defaultWinAction, String defaultLoseAction, short loadingInitMaxCool, 
		                 short dragMin, float dragSpeed, float moveStopRange, float rotateStopRange, 
		                 float attackGridSize, float roundBoxScale, float X2Speed, float X3Speed, 
		                 float horseBaseSpeed)
		{
			this.horseHS = horseHS;
			this.horseCS = horseCS;
			this.horseTitleOffset = horseTitleOffset;
			this.texBloodGreen = texBloodGreen;
			this.texBloodRed = texBloodRed;
			this.texNumberEffectGreen = texNumberEffectGreen;
			this.texNumberEffectRed = texNumberEffectRed;
			this.durNumberEffect = durNumberEffect;
			this.gravity = gravity;
			this.useCollision = useCollision;
			this.useKPack = useKPack;
			this.cameraFollowHero = cameraFollowHero;
			this.showFPSOnHead = showFPSOnHead;
			this.showErrorOnHead = showErrorOnHead;
			this.defaultFont = defaultFont;
			this.defaultStandAction = defaultStandAction;
			this.defaultRunAction = defaultRunAction;
			this.defaultAttackAction = defaultAttackAction;
			this.defaultHurtAction = defaultHurtAction;
			this.defaultDeathAction = defaultDeathAction;
			this.defaultDeathLoopAction = defaultDeathLoopAction;
			this.defaultWinAction = defaultWinAction;
			this.defaultLoseAction = defaultLoseAction;
			this.loadingInitMaxCool = loadingInitMaxCool;
			this.dragMin = dragMin;
			this.dragSpeed = dragSpeed;
			this.moveStopRange = moveStopRange;
			this.rotateStopRange = rotateStopRange;
			this.attackGridSize = attackGridSize;
			this.roundBoxScale = roundBoxScale;
			this.X2Speed = X2Speed;
			this.X3Speed = X3Speed;
			this.horseBaseSpeed = horseBaseSpeed;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			horseHS = is.popString();
			horseCS = is.popString();
			horseTitleOffset = is.popFloat();
			texBloodGreen = is.popString();
			texBloodRed = is.popString();
			texNumberEffectGreen = is.popString();
			texNumberEffectRed = is.popString();
			durNumberEffect = is.popShort();
			gravity = is.popFloat();
			useCollision = is.popByte();
			useKPack = is.popByte();
			cameraFollowHero = is.popByte();
			showFPSOnHead = is.popByte();
			showErrorOnHead = is.popByte();
			defaultFont = is.popString();
			defaultStandAction = is.popString();
			defaultRunAction = is.popString();
			defaultAttackAction = is.popString();
			defaultHurtAction = is.popStringList();
			defaultDeathAction = is.popStringList();
			defaultDeathLoopAction = is.popStringList();
			defaultWinAction = is.popString();
			defaultLoseAction = is.popString();
			loadingInitMaxCool = is.popShort();
			dragMin = is.popShort();
			dragSpeed = is.popFloat();
			moveStopRange = is.popFloat();
			rotateStopRange = is.popFloat();
			attackGridSize = is.popFloat();
			roundBoxScale = is.popFloat();
			X2Speed = is.popFloat();
			X3Speed = is.popFloat();
			horseBaseSpeed = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(horseHS);
			os.pushString(horseCS);
			os.pushFloat(horseTitleOffset);
			os.pushString(texBloodGreen);
			os.pushString(texBloodRed);
			os.pushString(texNumberEffectGreen);
			os.pushString(texNumberEffectRed);
			os.pushShort(durNumberEffect);
			os.pushFloat(gravity);
			os.pushByte(useCollision);
			os.pushByte(useKPack);
			os.pushByte(cameraFollowHero);
			os.pushByte(showFPSOnHead);
			os.pushByte(showErrorOnHead);
			os.pushString(defaultFont);
			os.pushString(defaultStandAction);
			os.pushString(defaultRunAction);
			os.pushString(defaultAttackAction);
			os.pushStringList(defaultHurtAction);
			os.pushStringList(defaultDeathAction);
			os.pushStringList(defaultDeathLoopAction);
			os.pushString(defaultWinAction);
			os.pushString(defaultLoseAction);
			os.pushShort(loadingInitMaxCool);
			os.pushShort(dragMin);
			os.pushFloat(dragSpeed);
			os.pushFloat(moveStopRange);
			os.pushFloat(rotateStopRange);
			os.pushFloat(attackGridSize);
			os.pushFloat(roundBoxScale);
			os.pushFloat(X2Speed);
			os.pushFloat(X3Speed);
			os.pushFloat(horseBaseSpeed);
		}

		public String horseHS;
		public String horseCS;
		public float horseTitleOffset;
		public String texBloodGreen;
		public String texBloodRed;
		public String texNumberEffectGreen;
		public String texNumberEffectRed;
		public short durNumberEffect;
		public float gravity;
		public byte useCollision;
		public byte useKPack;
		public byte cameraFollowHero;
		public byte showFPSOnHead;
		public byte showErrorOnHead;
		public String defaultFont;
		public String defaultStandAction;
		public String defaultRunAction;
		public String defaultAttackAction;
		public List<String> defaultHurtAction;
		public List<String> defaultDeathAction;
		public List<String> defaultDeathLoopAction;
		public String defaultWinAction;
		public String defaultLoseAction;
		public short loadingInitMaxCool;
		public short dragMin;
		public float dragSpeed;
		public float moveStopRange;
		public float rotateStopRange;
		public float attackGridSize;
		public float roundBoxScale;
		public float X2Speed;
		public float X3Speed;
		public float horseBaseSpeed;
	}

	public static class GameDataCFGC implements Stream.IStreamable
	{

		public GameDataCFGC() { }

		public GameDataCFGC(int version, List<DlgTableCFG> dlgtables, List<DlgTableCFG> dlgtablesNew, List<TipsCFG> tips, 
		                    List<ArtEffectCFG> artEffects, List<UIEffectCFG> uiEffects, List<FightEffectCFG> fightEffects, List<CombatMapCFG> combatmaps, 
		                    List<CombatCatCFG> combatcats, List<LoadingCFG> loadingTips, List<IconCFG> icons, List<PackageCFGC> packages, 
		                    List<AlarmCFG> alarms, RandomNameCFG randomNames, CombatClientCFG combat, MusicCFG music, 
		                    MapCFG map, EngineCFG engine, String luaCfg)
		{
			this.version = version;
			this.dlgtables = dlgtables;
			this.dlgtablesNew = dlgtablesNew;
			this.tips = tips;
			this.artEffects = artEffects;
			this.uiEffects = uiEffects;
			this.fightEffects = fightEffects;
			this.combatmaps = combatmaps;
			this.combatcats = combatcats;
			this.loadingTips = loadingTips;
			this.icons = icons;
			this.packages = packages;
			this.alarms = alarms;
			this.randomNames = randomNames;
			this.combat = combat;
			this.music = music;
			this.map = map;
			this.engine = engine;
			this.luaCfg = luaCfg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			version = is.popInteger();
			dlgtables = is.popList(DlgTableCFG.class);
			dlgtablesNew = is.popList(DlgTableCFG.class);
			tips = is.popList(TipsCFG.class);
			artEffects = is.popList(ArtEffectCFG.class);
			uiEffects = is.popList(UIEffectCFG.class);
			fightEffects = is.popList(FightEffectCFG.class);
			combatmaps = is.popList(CombatMapCFG.class);
			combatcats = is.popList(CombatCatCFG.class);
			loadingTips = is.popList(LoadingCFG.class);
			icons = is.popList(IconCFG.class);
			packages = is.popList(PackageCFGC.class);
			alarms = is.popList(AlarmCFG.class);
			if( randomNames == null )
				randomNames = new RandomNameCFG();
			is.pop(randomNames);
			if( combat == null )
				combat = new CombatClientCFG();
			is.pop(combat);
			if( music == null )
				music = new MusicCFG();
			is.pop(music);
			if( map == null )
				map = new MapCFG();
			is.pop(map);
			if( engine == null )
				engine = new EngineCFG();
			is.pop(engine);
			luaCfg = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(version);
			os.pushList(dlgtables);
			os.pushList(dlgtablesNew);
			os.pushList(tips);
			os.pushList(artEffects);
			os.pushList(uiEffects);
			os.pushList(fightEffects);
			os.pushList(combatmaps);
			os.pushList(combatcats);
			os.pushList(loadingTips);
			os.pushList(icons);
			os.pushList(packages);
			os.pushList(alarms);
			os.push(randomNames);
			os.push(combat);
			os.push(music);
			os.push(map);
			os.push(engine);
			os.pushString(luaCfg);
		}

		public int version = 1;
		public List<DlgTableCFG> dlgtables;
		public List<DlgTableCFG> dlgtablesNew;
		public List<TipsCFG> tips;
		public List<ArtEffectCFG> artEffects;
		public List<UIEffectCFG> uiEffects;
		public List<FightEffectCFG> fightEffects;
		public List<CombatMapCFG> combatmaps;
		public List<CombatCatCFG> combatcats;
		public List<LoadingCFG> loadingTips;
		public List<IconCFG> icons;
		public List<PackageCFGC> packages;
		public List<AlarmCFG> alarms;
		public RandomNameCFG randomNames;
		public CombatClientCFG combat;
		public MusicCFG music;
		public MapCFG map;
		public EngineCFG engine;
		public String luaCfg;
	}

	public static class GeneralName implements Stream.IStreamable
	{

		public GeneralName() { }

		public GeneralName(short id, String name)
		{
			this.id = id;
			this.name = name;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
		}

		public short id;
		public String name;
	}

	public static class GameDataCFGT implements Stream.IStreamable
	{

		public GameDataCFGT() { }

		public GameDataCFGT(List<GeneralName> generals, List<GeneralName> equips, List<GeneralName> items)
		{
			this.generals = generals;
			this.equips = equips;
			this.items = items;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generals = is.popList(GeneralName.class);
			equips = is.popList(GeneralName.class);
			items = is.popList(GeneralName.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(generals);
			os.pushList(equips);
			os.pushList(items);
		}

		public List<GeneralName> generals;
		public List<GeneralName> equips;
		public List<GeneralName> items;
	}

	public static class QQDirtyWordCatCFGS implements Stream.IStreamable
	{

		public QQDirtyWordCatCFGS() { }

		public QQDirtyWordCatCFGS(String key, List<String> words)
		{
			this.key = key;
			this.words = words;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			key = is.popString();
			words = is.popStringList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(key);
			os.pushStringList(words);
		}

		public String key;
		public List<String> words;
	}

	public static class QQDirtyWordCFGS implements Stream.IStreamable
	{

		public QQDirtyWordCFGS() { }

		public QQDirtyWordCFGS(List<String> words, List<String> wordsICase, List<QQDirtyWordCatCFGS> cats)
		{
			this.words = words;
			this.wordsICase = wordsICase;
			this.cats = cats;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			words = is.popStringList();
			wordsICase = is.popStringList();
			cats = is.popList(QQDirtyWordCatCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushStringList(words);
			os.pushStringList(wordsICase);
			os.pushList(cats);
		}

		public List<String> words;
		public List<String> wordsICase;
		public List<QQDirtyWordCatCFGS> cats;
	}

	public static class IPadAreaCFGS implements Stream.IStreamable
	{

		public IPadAreaCFGS() { }

		public IPadAreaCFGS(int gsID, short itemID, int timeCheat)
		{
			this.gsID = gsID;
			this.itemID = itemID;
			this.timeCheat = timeCheat;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gsID = is.popInteger();
			itemID = is.popShort();
			timeCheat = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gsID);
			os.pushShort(itemID);
			os.pushInteger(timeCheat);
		}

		public int gsID;
		public short itemID;
		public int timeCheat;
	}

	public static class IPadCFGS implements Stream.IStreamable
	{

		public IPadCFGS() { }

		public IPadCFGS(int state, int timeStart, int timeEnd, short itemIDRand, 
		                float prop, List<IPadAreaCFGS> areas)
		{
			this.state = state;
			this.timeStart = timeStart;
			this.timeEnd = timeEnd;
			this.itemIDRand = itemIDRand;
			this.prop = prop;
			this.areas = areas;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			state = is.popInteger();
			timeStart = is.popInteger();
			timeEnd = is.popInteger();
			itemIDRand = is.popShort();
			prop = is.popFloat();
			areas = is.popList(IPadAreaCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(state);
			os.pushInteger(timeStart);
			os.pushInteger(timeEnd);
			os.pushShort(itemIDRand);
			os.pushFloat(prop);
			os.pushList(areas);
		}

		public int state;
		public int timeStart;
		public int timeEnd;
		public short itemIDRand;
		public float prop;
		public List<IPadAreaCFGS> areas;
	}

	public static class GameDataCFGS implements Stream.IStreamable
	{

		public GameDataCFGS() { }

		public GameDataCFGS(int version, UserInputCFGS userInput, List<GeneralCFGS> generals, List<PetCFGS> pets, 
		                    List<PetCFGS> deformPets, GeneralCmnCFGS generalsCmn, PetCmnCFGS petsCmn, RoleCmnCFGS roleCmn, 
		                    CombatCmnCFGS combatCmn, EquipCmnCFGS equipCmn, List<CombatCFGS> combats, List<CombatEventCFGS> combatEvents, 
		                    List<CombatCatCFG> combatcats, List<DropTableCFGS> droptables, List<EggCFGS> eggs, List<ItemCFGS> items, 
		                    List<PackageCFGS> packages, List<EquipCFGS> equips, ShopCFGS shopNormal, ShopCFGS shopArena, 
		                    ShopCFGS shopMarch, ShopCFGS shopRand1, ShopCFGS shopRand2, ShopCFGS shopForce, 
		                    ShopCFGS shopRichStone, ShopCFGS shopRichPoint, ShopCFGS shopRichGold, ShopCFGS shopSuperArena, 
		                    ShopCFGS shopDuel, ShopCFGS shopBless, MonthlyCardCFGS mcard, List<CmnRewardCFGS> cmnrewards, 
		                    List<CheckinCFGS> checkin, ForceCFGS force, List<DailyTask2CFGS> dtask2, List<DailyActivityRewardCFGS> dailyActivity, 
		                    List<QQVIPRewardsCFGS> qqvipRewards, TopLevelAuraCFGS topLevelAura, FriendCFGS friend, EventCFGS event, 
		                    List<TaskGroupCFGS> taskGroups, List<VIPCFGS> vip, List<ChannelCFGS> channels, List<PayCFGS> pay, 
		                    List<String> badnames, List<String> badnames2, QQDirtyWordCFGS qqDirtyWords, CombatServerCFG combat, 
		                    ArenaCFGS arena, SuperArenaCFGS superArena, MarchCFGS march, List<PowerCFGS> power, 
		                    List<SoulBoxCFGS> soulboxs, PetFarmCFGS petFarm, CaptureCityCFGS capturecity, WeaponCFGS weapon, 
		                    ForceBattlesCFGS forcebattles, RichCFGS rich, InvitationCFGS invitation, DungeonCFGS dungeon, 
		                    RelationsCFGS relation, GeneralStoneCFGS generalStone, GeneralSeyenCFGS generalSeyen, ChangeServerCFGS changeServer, 
		                    ExpiratBossCFGS expiratBoss, HeroesBossCFGS heroesBoss, List<BestowCFGS> bestow, List<BlessCFGS> bless, 
		                    CommomConstantCFGS common, BestowItemCFGS bestowItem, List<HeadIconCFGS> headicon, DiskBetCFGS diskBet, 
		                    short expatriateCost, short bestowcost, short bestowId, short forceBattleCount, 
		                    MercCFGS merc, DuelCFGS duel, SuraCFGS sura, ForceSWarCFGS swar, 
		                    OfficialCFGS official, IPadCFGS ipad, TreasureMapBaseCFGS treasureMap, BeMonsterCFGS beMonster)
		{
			this.version = version;
			this.userInput = userInput;
			this.generals = generals;
			this.pets = pets;
			this.deformPets = deformPets;
			this.generalsCmn = generalsCmn;
			this.petsCmn = petsCmn;
			this.roleCmn = roleCmn;
			this.combatCmn = combatCmn;
			this.equipCmn = equipCmn;
			this.combats = combats;
			this.combatEvents = combatEvents;
			this.combatcats = combatcats;
			this.droptables = droptables;
			this.eggs = eggs;
			this.items = items;
			this.packages = packages;
			this.equips = equips;
			this.shopNormal = shopNormal;
			this.shopArena = shopArena;
			this.shopMarch = shopMarch;
			this.shopRand1 = shopRand1;
			this.shopRand2 = shopRand2;
			this.shopForce = shopForce;
			this.shopRichStone = shopRichStone;
			this.shopRichPoint = shopRichPoint;
			this.shopRichGold = shopRichGold;
			this.shopSuperArena = shopSuperArena;
			this.shopDuel = shopDuel;
			this.shopBless = shopBless;
			this.mcard = mcard;
			this.cmnrewards = cmnrewards;
			this.checkin = checkin;
			this.force = force;
			this.dtask2 = dtask2;
			this.dailyActivity = dailyActivity;
			this.qqvipRewards = qqvipRewards;
			this.topLevelAura = topLevelAura;
			this.friend = friend;
			this.event = event;
			this.taskGroups = taskGroups;
			this.vip = vip;
			this.channels = channels;
			this.pay = pay;
			this.badnames = badnames;
			this.badnames2 = badnames2;
			this.qqDirtyWords = qqDirtyWords;
			this.combat = combat;
			this.arena = arena;
			this.superArena = superArena;
			this.march = march;
			this.power = power;
			this.soulboxs = soulboxs;
			this.petFarm = petFarm;
			this.capturecity = capturecity;
			this.weapon = weapon;
			this.forcebattles = forcebattles;
			this.rich = rich;
			this.invitation = invitation;
			this.dungeon = dungeon;
			this.relation = relation;
			this.generalStone = generalStone;
			this.generalSeyen = generalSeyen;
			this.changeServer = changeServer;
			this.expiratBoss = expiratBoss;
			this.heroesBoss = heroesBoss;
			this.bestow = bestow;
			this.bless = bless;
			this.common = common;
			this.bestowItem = bestowItem;
			this.headicon = headicon;
			this.diskBet = diskBet;
			this.expatriateCost = expatriateCost;
			this.bestowcost = bestowcost;
			this.bestowId = bestowId;
			this.forceBattleCount = forceBattleCount;
			this.merc = merc;
			this.duel = duel;
			this.sura = sura;
			this.swar = swar;
			this.official = official;
			this.ipad = ipad;
			this.treasureMap = treasureMap;
			this.beMonster = beMonster;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			version = is.popInteger();
			if( userInput == null )
				userInput = new UserInputCFGS();
			is.pop(userInput);
			generals = is.popList(GeneralCFGS.class);
			pets = is.popList(PetCFGS.class);
			deformPets = is.popList(PetCFGS.class);
			if( generalsCmn == null )
				generalsCmn = new GeneralCmnCFGS();
			is.pop(generalsCmn);
			if( petsCmn == null )
				petsCmn = new PetCmnCFGS();
			is.pop(petsCmn);
			if( roleCmn == null )
				roleCmn = new RoleCmnCFGS();
			is.pop(roleCmn);
			if( combatCmn == null )
				combatCmn = new CombatCmnCFGS();
			is.pop(combatCmn);
			if( equipCmn == null )
				equipCmn = new EquipCmnCFGS();
			is.pop(equipCmn);
			combats = is.popList(CombatCFGS.class);
			combatEvents = is.popList(CombatEventCFGS.class);
			combatcats = is.popList(CombatCatCFG.class);
			droptables = is.popList(DropTableCFGS.class);
			eggs = is.popList(EggCFGS.class);
			items = is.popList(ItemCFGS.class);
			packages = is.popList(PackageCFGS.class);
			equips = is.popList(EquipCFGS.class);
			if( shopNormal == null )
				shopNormal = new ShopCFGS();
			is.pop(shopNormal);
			if( shopArena == null )
				shopArena = new ShopCFGS();
			is.pop(shopArena);
			if( shopMarch == null )
				shopMarch = new ShopCFGS();
			is.pop(shopMarch);
			if( shopRand1 == null )
				shopRand1 = new ShopCFGS();
			is.pop(shopRand1);
			if( shopRand2 == null )
				shopRand2 = new ShopCFGS();
			is.pop(shopRand2);
			if( shopForce == null )
				shopForce = new ShopCFGS();
			is.pop(shopForce);
			if( shopRichStone == null )
				shopRichStone = new ShopCFGS();
			is.pop(shopRichStone);
			if( shopRichPoint == null )
				shopRichPoint = new ShopCFGS();
			is.pop(shopRichPoint);
			if( shopRichGold == null )
				shopRichGold = new ShopCFGS();
			is.pop(shopRichGold);
			if( shopSuperArena == null )
				shopSuperArena = new ShopCFGS();
			is.pop(shopSuperArena);
			if( shopDuel == null )
				shopDuel = new ShopCFGS();
			is.pop(shopDuel);
			if( shopBless == null )
				shopBless = new ShopCFGS();
			is.pop(shopBless);
			if( mcard == null )
				mcard = new MonthlyCardCFGS();
			is.pop(mcard);
			cmnrewards = is.popList(CmnRewardCFGS.class);
			checkin = is.popList(CheckinCFGS.class);
			if( force == null )
				force = new ForceCFGS();
			is.pop(force);
			dtask2 = is.popList(DailyTask2CFGS.class);
			dailyActivity = is.popList(DailyActivityRewardCFGS.class);
			qqvipRewards = is.popList(QQVIPRewardsCFGS.class);
			if( topLevelAura == null )
				topLevelAura = new TopLevelAuraCFGS();
			is.pop(topLevelAura);
			if( friend == null )
				friend = new FriendCFGS();
			is.pop(friend);
			if( event == null )
				event = new EventCFGS();
			is.pop(event);
			taskGroups = is.popList(TaskGroupCFGS.class);
			vip = is.popList(VIPCFGS.class);
			channels = is.popList(ChannelCFGS.class);
			pay = is.popList(PayCFGS.class);
			badnames = is.popStringList();
			badnames2 = is.popStringList();
			if( qqDirtyWords == null )
				qqDirtyWords = new QQDirtyWordCFGS();
			is.pop(qqDirtyWords);
			if( combat == null )
				combat = new CombatServerCFG();
			is.pop(combat);
			if( arena == null )
				arena = new ArenaCFGS();
			is.pop(arena);
			if( superArena == null )
				superArena = new SuperArenaCFGS();
			is.pop(superArena);
			if( march == null )
				march = new MarchCFGS();
			is.pop(march);
			power = is.popList(PowerCFGS.class);
			soulboxs = is.popList(SoulBoxCFGS.class);
			if( petFarm == null )
				petFarm = new PetFarmCFGS();
			is.pop(petFarm);
			if( capturecity == null )
				capturecity = new CaptureCityCFGS();
			is.pop(capturecity);
			if( weapon == null )
				weapon = new WeaponCFGS();
			is.pop(weapon);
			if( forcebattles == null )
				forcebattles = new ForceBattlesCFGS();
			is.pop(forcebattles);
			if( rich == null )
				rich = new RichCFGS();
			is.pop(rich);
			if( invitation == null )
				invitation = new InvitationCFGS();
			is.pop(invitation);
			if( dungeon == null )
				dungeon = new DungeonCFGS();
			is.pop(dungeon);
			if( relation == null )
				relation = new RelationsCFGS();
			is.pop(relation);
			if( generalStone == null )
				generalStone = new GeneralStoneCFGS();
			is.pop(generalStone);
			if( generalSeyen == null )
				generalSeyen = new GeneralSeyenCFGS();
			is.pop(generalSeyen);
			if( changeServer == null )
				changeServer = new ChangeServerCFGS();
			is.pop(changeServer);
			if( expiratBoss == null )
				expiratBoss = new ExpiratBossCFGS();
			is.pop(expiratBoss);
			if( heroesBoss == null )
				heroesBoss = new HeroesBossCFGS();
			is.pop(heroesBoss);
			bestow = is.popList(BestowCFGS.class);
			bless = is.popList(BlessCFGS.class);
			if( common == null )
				common = new CommomConstantCFGS();
			is.pop(common);
			if( bestowItem == null )
				bestowItem = new BestowItemCFGS();
			is.pop(bestowItem);
			headicon = is.popList(HeadIconCFGS.class);
			if( diskBet == null )
				diskBet = new DiskBetCFGS();
			is.pop(diskBet);
			expatriateCost = is.popShort();
			bestowcost = is.popShort();
			bestowId = is.popShort();
			forceBattleCount = is.popShort();
			if( merc == null )
				merc = new MercCFGS();
			is.pop(merc);
			if( duel == null )
				duel = new DuelCFGS();
			is.pop(duel);
			if( sura == null )
				sura = new SuraCFGS();
			is.pop(sura);
			if( swar == null )
				swar = new ForceSWarCFGS();
			is.pop(swar);
			if( official == null )
				official = new OfficialCFGS();
			is.pop(official);
			if( ipad == null )
				ipad = new IPadCFGS();
			is.pop(ipad);
			if( treasureMap == null )
				treasureMap = new TreasureMapBaseCFGS();
			is.pop(treasureMap);
			if( beMonster == null )
				beMonster = new BeMonsterCFGS();
			is.pop(beMonster);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(version);
			os.push(userInput);
			os.pushList(generals);
			os.pushList(pets);
			os.pushList(deformPets);
			os.push(generalsCmn);
			os.push(petsCmn);
			os.push(roleCmn);
			os.push(combatCmn);
			os.push(equipCmn);
			os.pushList(combats);
			os.pushList(combatEvents);
			os.pushList(combatcats);
			os.pushList(droptables);
			os.pushList(eggs);
			os.pushList(items);
			os.pushList(packages);
			os.pushList(equips);
			os.push(shopNormal);
			os.push(shopArena);
			os.push(shopMarch);
			os.push(shopRand1);
			os.push(shopRand2);
			os.push(shopForce);
			os.push(shopRichStone);
			os.push(shopRichPoint);
			os.push(shopRichGold);
			os.push(shopSuperArena);
			os.push(shopDuel);
			os.push(shopBless);
			os.push(mcard);
			os.pushList(cmnrewards);
			os.pushList(checkin);
			os.push(force);
			os.pushList(dtask2);
			os.pushList(dailyActivity);
			os.pushList(qqvipRewards);
			os.push(topLevelAura);
			os.push(friend);
			os.push(event);
			os.pushList(taskGroups);
			os.pushList(vip);
			os.pushList(channels);
			os.pushList(pay);
			os.pushStringList(badnames);
			os.pushStringList(badnames2);
			os.push(qqDirtyWords);
			os.push(combat);
			os.push(arena);
			os.push(superArena);
			os.push(march);
			os.pushList(power);
			os.pushList(soulboxs);
			os.push(petFarm);
			os.push(capturecity);
			os.push(weapon);
			os.push(forcebattles);
			os.push(rich);
			os.push(invitation);
			os.push(dungeon);
			os.push(relation);
			os.push(generalStone);
			os.push(generalSeyen);
			os.push(changeServer);
			os.push(expiratBoss);
			os.push(heroesBoss);
			os.pushList(bestow);
			os.pushList(bless);
			os.push(common);
			os.push(bestowItem);
			os.pushList(headicon);
			os.push(diskBet);
			os.pushShort(expatriateCost);
			os.pushShort(bestowcost);
			os.pushShort(bestowId);
			os.pushShort(forceBattleCount);
			os.push(merc);
			os.push(duel);
			os.push(sura);
			os.push(swar);
			os.push(official);
			os.push(ipad);
			os.push(treasureMap);
			os.push(beMonster);
		}

		public int version = 1;
		public UserInputCFGS userInput;
		public List<GeneralCFGS> generals;
		public List<PetCFGS> pets;
		public List<PetCFGS> deformPets;
		public GeneralCmnCFGS generalsCmn;
		public PetCmnCFGS petsCmn;
		public RoleCmnCFGS roleCmn;
		public CombatCmnCFGS combatCmn;
		public EquipCmnCFGS equipCmn;
		public List<CombatCFGS> combats;
		public List<CombatEventCFGS> combatEvents;
		public List<CombatCatCFG> combatcats;
		public List<DropTableCFGS> droptables;
		public List<EggCFGS> eggs;
		public List<ItemCFGS> items;
		public List<PackageCFGS> packages;
		public List<EquipCFGS> equips;
		public ShopCFGS shopNormal;
		public ShopCFGS shopArena;
		public ShopCFGS shopMarch;
		public ShopCFGS shopRand1;
		public ShopCFGS shopRand2;
		public ShopCFGS shopForce;
		public ShopCFGS shopRichStone;
		public ShopCFGS shopRichPoint;
		public ShopCFGS shopRichGold;
		public ShopCFGS shopSuperArena;
		public ShopCFGS shopDuel;
		public ShopCFGS shopBless;
		public MonthlyCardCFGS mcard;
		public List<CmnRewardCFGS> cmnrewards;
		public List<CheckinCFGS> checkin;
		public ForceCFGS force;
		public List<DailyTask2CFGS> dtask2;
		public List<DailyActivityRewardCFGS> dailyActivity;
		public List<QQVIPRewardsCFGS> qqvipRewards;
		public TopLevelAuraCFGS topLevelAura;
		public FriendCFGS friend;
		public EventCFGS event;
		public List<TaskGroupCFGS> taskGroups;
		public List<VIPCFGS> vip;
		public List<ChannelCFGS> channels;
		public List<PayCFGS> pay;
		public List<String> badnames;
		public List<String> badnames2;
		public QQDirtyWordCFGS qqDirtyWords;
		public CombatServerCFG combat;
		public ArenaCFGS arena;
		public SuperArenaCFGS superArena;
		public MarchCFGS march;
		public List<PowerCFGS> power;
		public List<SoulBoxCFGS> soulboxs;
		public PetFarmCFGS petFarm;
		public CaptureCityCFGS capturecity;
		public WeaponCFGS weapon;
		public ForceBattlesCFGS forcebattles;
		public RichCFGS rich;
		public InvitationCFGS invitation;
		public DungeonCFGS dungeon;
		public RelationsCFGS relation;
		public GeneralStoneCFGS generalStone;
		public GeneralSeyenCFGS generalSeyen;
		public ChangeServerCFGS changeServer;
		public ExpiratBossCFGS expiratBoss;
		public HeroesBossCFGS heroesBoss;
		public List<BestowCFGS> bestow;
		public List<BlessCFGS> bless;
		public CommomConstantCFGS common;
		public BestowItemCFGS bestowItem;
		public List<HeadIconCFGS> headicon;
		public DiskBetCFGS diskBet;
		public short expatriateCost;
		public short bestowcost;
		public short bestowId;
		public short forceBattleCount;
		public MercCFGS merc;
		public DuelCFGS duel;
		public SuraCFGS sura;
		public ForceSWarCFGS swar;
		public OfficialCFGS official;
		public IPadCFGS ipad;
		public TreasureMapBaseCFGS treasureMap;
		public BeMonsterCFGS beMonster;
	}

	public static class WeaponPropCFGS implements Stream.IStreamable
	{

		public WeaponPropCFGS() { }

		public WeaponPropCFGS(short propId, byte type, float value)
		{
			this.propId = propId;
			this.type = type;
			this.value = value;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			propId = is.popShort();
			type = is.popByte();
			value = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(propId);
			os.pushByte(type);
			os.pushFloat(value);
		}

		public short propId;
		public byte type;
		public float value;
	}

	public static class WeaponBasicCFGS implements Stream.IStreamable
	{

		public WeaponBasicCFGS() { }

		public WeaponBasicCFGS(short gid, String name, byte reqAdvLvl, short iconId, 
		                       String desc, String recommend, List<WeaponPropCFGS> props)
		{
			this.gid = gid;
			this.name = name;
			this.reqAdvLvl = reqAdvLvl;
			this.iconId = iconId;
			this.desc = desc;
			this.recommend = recommend;
			this.props = props;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			name = is.popString();
			reqAdvLvl = is.popByte();
			iconId = is.popShort();
			desc = is.popString();
			recommend = is.popString();
			props = is.popList(WeaponPropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.pushString(name);
			os.pushByte(reqAdvLvl);
			os.pushShort(iconId);
			os.pushString(desc);
			os.pushString(recommend);
			os.pushList(props);
		}

		public short gid;
		public String name;
		public byte reqAdvLvl;
		public short iconId;
		public String desc;
		public String recommend;
		public List<WeaponPropCFGS> props;
	}

	public static class WeaponEnhanceStageCFGS implements Stream.IStreamable
	{

		public WeaponEnhanceStageCFGS() { }

		public WeaponEnhanceStageCFGS(short imageId, short effectId, byte enhanceCount)
		{
			this.imageId = imageId;
			this.effectId = effectId;
			this.enhanceCount = enhanceCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			imageId = is.popShort();
			effectId = is.popShort();
			enhanceCount = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(imageId);
			os.pushShort(effectId);
			os.pushByte(enhanceCount);
		}

		public short imageId;
		public short effectId;
		public byte enhanceCount;
	}

	public static class WeaponCostCFGS implements Stream.IStreamable
	{

		public WeaponCostCFGS() { }

		public WeaponCostCFGS(int costMoney, int costStone)
		{
			this.costMoney = costMoney;
			this.costStone = costStone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			costMoney = is.popInteger();
			costStone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(costMoney);
			os.pushInteger(costStone);
		}

		public int costMoney;
		public int costStone;
	}

	public static class WeaponEvoCostCFGS implements Stream.IStreamable
	{

		public WeaponEvoCostCFGS() { }

		public WeaponEvoCostCFGS(int costMoney, short costEquipID0, short costEquipID1)
		{
			this.costMoney = costMoney;
			this.costEquipID0 = costEquipID0;
			this.costEquipID1 = costEquipID1;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			costMoney = is.popInteger();
			costEquipID0 = is.popShort();
			costEquipID1 = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(costMoney);
			os.pushShort(costEquipID0);
			os.pushShort(costEquipID1);
		}

		public int costMoney;
		public short costEquipID0;
		public short costEquipID1;
	}

	public static class WeaponEnhanceCFGS implements Stream.IStreamable
	{

		public WeaponEnhanceCFGS() { }

		public WeaponEnhanceCFGS(short gid, List<WeaponEnhanceStageCFGS> enhanceStages, List<DropEntryNew> enhanceCosts, List<WeaponPropCFGS> enhanceProps)
		{
			this.gid = gid;
			this.enhanceStages = enhanceStages;
			this.enhanceCosts = enhanceCosts;
			this.enhanceProps = enhanceProps;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			enhanceStages = is.popList(WeaponEnhanceStageCFGS.class);
			enhanceCosts = is.popList(DropEntryNew.class);
			enhanceProps = is.popList(WeaponPropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.pushList(enhanceStages);
			os.pushList(enhanceCosts);
			os.pushList(enhanceProps);
		}

		public short gid;
		public List<WeaponEnhanceStageCFGS> enhanceStages;
		public List<DropEntryNew> enhanceCosts;
		public List<WeaponPropCFGS> enhanceProps;
	}

	public static class WeaponResetPropCFGS implements Stream.IStreamable
	{

		public WeaponResetPropCFGS() { }

		public WeaponResetPropCFGS(short propId, byte type, float valueMin, float valueMax, 
		                           byte rule, short poss)
		{
			this.propId = propId;
			this.type = type;
			this.valueMin = valueMin;
			this.valueMax = valueMax;
			this.rule = rule;
			this.poss = poss;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			propId = is.popShort();
			type = is.popByte();
			valueMin = is.popFloat();
			valueMax = is.popFloat();
			rule = is.popByte();
			poss = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(propId);
			os.pushByte(type);
			os.pushFloat(valueMin);
			os.pushFloat(valueMax);
			os.pushByte(rule);
			os.pushShort(poss);
		}

		public short propId;
		public byte type;
		public float valueMin;
		public float valueMax;
		public byte rule;
		public short poss;
	}

	public static class WeaponResetPropPassCFGS implements Stream.IStreamable
	{

		public WeaponResetPropPassCFGS() { }

		public WeaponResetPropPassCFGS(List<WeaponResetPropCFGS> pass)
		{
			this.pass = pass;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pass = is.popList(WeaponResetPropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(pass);
		}

		public List<WeaponResetPropCFGS> pass;
	}

	public static class WeaponResetRuleCFGS implements Stream.IStreamable
	{

		public WeaponResetRuleCFGS() { }

		public WeaponResetRuleCFGS(byte id, List<Byte> percent)
		{
			this.id = id;
			this.percent = percent;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			percent = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushByteList(percent);
		}

		public byte id;
		public List<Byte> percent;
	}

	public static class WeaponResetNewPropCFGS implements Stream.IStreamable
	{

		public WeaponResetNewPropCFGS() { }

		public WeaponResetNewPropCFGS(List<Byte> percent)
		{
			this.percent = percent;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			percent = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByteList(percent);
		}

		public List<Byte> percent;
	}

	public static class WeaponResetCFGS implements Stream.IStreamable
	{

		public WeaponResetCFGS() { }

		public WeaponResetCFGS(WeaponCostCFGS noLock, WeaponCostCFGS lock1, WeaponCostCFGS lock2, WeaponCostCFGS lock3, 
		                       List<WeaponResetPropPassCFGS> propPasses, List<WeaponResetRuleCFGS> rules, List<WeaponResetNewPropCFGS> newPropPurple, List<WeaponResetNewPropCFGS> newPropOrange, 
		                       WeaponCostCFGS nlock1, WeaponCostCFGS nlock2, WeaponCostCFGS nlock3, WeaponCostCFGS nlock4, 
		                       WeaponCostCFGS nlock5)
		{
			this.noLock = noLock;
			this.lock1 = lock1;
			this.lock2 = lock2;
			this.lock3 = lock3;
			this.propPasses = propPasses;
			this.rules = rules;
			this.newPropPurple = newPropPurple;
			this.newPropOrange = newPropOrange;
			this.nlock1 = nlock1;
			this.nlock2 = nlock2;
			this.nlock3 = nlock3;
			this.nlock4 = nlock4;
			this.nlock5 = nlock5;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( noLock == null )
				noLock = new WeaponCostCFGS();
			is.pop(noLock);
			if( lock1 == null )
				lock1 = new WeaponCostCFGS();
			is.pop(lock1);
			if( lock2 == null )
				lock2 = new WeaponCostCFGS();
			is.pop(lock2);
			if( lock3 == null )
				lock3 = new WeaponCostCFGS();
			is.pop(lock3);
			propPasses = is.popList(WeaponResetPropPassCFGS.class);
			rules = is.popList(WeaponResetRuleCFGS.class);
			newPropPurple = is.popList(WeaponResetNewPropCFGS.class);
			newPropOrange = is.popList(WeaponResetNewPropCFGS.class);
			if( nlock1 == null )
				nlock1 = new WeaponCostCFGS();
			is.pop(nlock1);
			if( nlock2 == null )
				nlock2 = new WeaponCostCFGS();
			is.pop(nlock2);
			if( nlock3 == null )
				nlock3 = new WeaponCostCFGS();
			is.pop(nlock3);
			if( nlock4 == null )
				nlock4 = new WeaponCostCFGS();
			is.pop(nlock4);
			if( nlock5 == null )
				nlock5 = new WeaponCostCFGS();
			is.pop(nlock5);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(noLock);
			os.push(lock1);
			os.push(lock2);
			os.push(lock3);
			os.pushList(propPasses);
			os.pushList(rules);
			os.pushList(newPropPurple);
			os.pushList(newPropOrange);
			os.push(nlock1);
			os.push(nlock2);
			os.push(nlock3);
			os.push(nlock4);
			os.push(nlock5);
		}

		public WeaponCostCFGS noLock;
		public WeaponCostCFGS lock1;
		public WeaponCostCFGS lock2;
		public WeaponCostCFGS lock3;
		public List<WeaponResetPropPassCFGS> propPasses;
		public List<WeaponResetRuleCFGS> rules;
		public List<WeaponResetNewPropCFGS> newPropPurple;
		public List<WeaponResetNewPropCFGS> newPropOrange;
		public WeaponCostCFGS nlock1;
		public WeaponCostCFGS nlock2;
		public WeaponCostCFGS nlock3;
		public WeaponCostCFGS nlock4;
		public WeaponCostCFGS nlock5;
	}

	public static class WeaponCFGS implements Stream.IStreamable
	{

		public WeaponCFGS() { }

		public WeaponCFGS(List<WeaponBasicCFGS> basic, List<WeaponEnhanceCFGS> enhance, List<WeaponEvoCostCFGS> evoCosts, WeaponResetCFGS reset)
		{
			this.basic = basic;
			this.enhance = enhance;
			this.evoCosts = evoCosts;
			this.reset = reset;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			basic = is.popList(WeaponBasicCFGS.class);
			enhance = is.popList(WeaponEnhanceCFGS.class);
			evoCosts = is.popList(WeaponEvoCostCFGS.class);
			if( reset == null )
				reset = new WeaponResetCFGS();
			is.pop(reset);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(basic);
			os.pushList(enhance);
			os.pushList(evoCosts);
			os.push(reset);
		}

		public List<WeaponBasicCFGS> basic;
		public List<WeaponEnhanceCFGS> enhance;
		public List<WeaponEvoCostCFGS> evoCosts;
		public WeaponResetCFGS reset;
	}

	public static class GeneralStonePropCFGS implements Stream.IStreamable
	{

		public GeneralStonePropCFGS() { }

		public GeneralStonePropCFGS(short pId, short propId, short type, short value)
		{
			this.pId = pId;
			this.propId = propId;
			this.type = type;
			this.value = value;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pId = is.popShort();
			propId = is.popShort();
			type = is.popShort();
			value = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(pId);
			os.pushShort(propId);
			os.pushShort(type);
			os.pushShort(value);
		}

		public short pId;
		public short propId;
		public short type;
		public short value;
	}

	public static class GeneralStonePropValueCFGS implements Stream.IStreamable
	{

		public GeneralStonePropValueCFGS() { }

		public GeneralStonePropValueCFGS(short pId, short weight, float minvalue, float maxvalue)
		{
			this.pId = pId;
			this.weight = weight;
			this.minvalue = minvalue;
			this.maxvalue = maxvalue;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pId = is.popShort();
			weight = is.popShort();
			minvalue = is.popFloat();
			maxvalue = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(pId);
			os.pushShort(weight);
			os.pushFloat(minvalue);
			os.pushFloat(maxvalue);
		}

		public short pId;
		public short weight;
		public float minvalue;
		public float maxvalue;
	}

	public static class GeneralStoneSkillPropCFGS implements Stream.IStreamable
	{

		public GeneralStoneSkillPropCFGS() { }

		public GeneralStoneSkillPropCFGS(short beskillId, short afskillId)
		{
			this.beskillId = beskillId;
			this.afskillId = afskillId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			beskillId = is.popShort();
			afskillId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(beskillId);
			os.pushShort(afskillId);
		}

		public short beskillId;
		public short afskillId;
	}

	public static class GeneralStonePosCFGS implements Stream.IStreamable
	{

		public GeneralStonePosCFGS() { }

		public GeneralStonePosCFGS(short gid, byte state1, byte state2, byte state3, 
		                           byte state4, byte posType1, byte posType2, byte posType3, 
		                           byte posType4, GeneralStoneSkillPropCFGS posSkill1, GeneralStoneSkillPropCFGS posSkill2, GeneralStoneSkillPropCFGS posSkill3, 
		                           GeneralStoneSkillPropCFGS posSkill4)
		{
			this.gid = gid;
			this.state1 = state1;
			this.state2 = state2;
			this.state3 = state3;
			this.state4 = state4;
			this.posType1 = posType1;
			this.posType2 = posType2;
			this.posType3 = posType3;
			this.posType4 = posType4;
			this.posSkill1 = posSkill1;
			this.posSkill2 = posSkill2;
			this.posSkill3 = posSkill3;
			this.posSkill4 = posSkill4;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			state1 = is.popByte();
			state2 = is.popByte();
			state3 = is.popByte();
			state4 = is.popByte();
			posType1 = is.popByte();
			posType2 = is.popByte();
			posType3 = is.popByte();
			posType4 = is.popByte();
			if( posSkill1 == null )
				posSkill1 = new GeneralStoneSkillPropCFGS();
			is.pop(posSkill1);
			if( posSkill2 == null )
				posSkill2 = new GeneralStoneSkillPropCFGS();
			is.pop(posSkill2);
			if( posSkill3 == null )
				posSkill3 = new GeneralStoneSkillPropCFGS();
			is.pop(posSkill3);
			if( posSkill4 == null )
				posSkill4 = new GeneralStoneSkillPropCFGS();
			is.pop(posSkill4);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.pushByte(state1);
			os.pushByte(state2);
			os.pushByte(state3);
			os.pushByte(state4);
			os.pushByte(posType1);
			os.pushByte(posType2);
			os.pushByte(posType3);
			os.pushByte(posType4);
			os.push(posSkill1);
			os.push(posSkill2);
			os.push(posSkill3);
			os.push(posSkill4);
		}

		public short gid;
		public byte state1;
		public byte state2;
		public byte state3;
		public byte state4;
		public byte posType1;
		public byte posType2;
		public byte posType3;
		public byte posType4;
		public GeneralStoneSkillPropCFGS posSkill1;
		public GeneralStoneSkillPropCFGS posSkill2;
		public GeneralStoneSkillPropCFGS posSkill3;
		public GeneralStoneSkillPropCFGS posSkill4;
	}

	public static class GeneralStoneEvoCostCFGS implements Stream.IStreamable
	{

		public GeneralStoneEvoCostCFGS() { }

		public GeneralStoneEvoCostCFGS(short cid, byte totalLock, int costStoneLock1, int costMoneyLock1, 
		                               int costStoneLock2, int costMoneyLock2)
		{
			this.cid = cid;
			this.totalLock = totalLock;
			this.costStoneLock1 = costStoneLock1;
			this.costMoneyLock1 = costMoneyLock1;
			this.costStoneLock2 = costStoneLock2;
			this.costMoneyLock2 = costMoneyLock2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cid = is.popShort();
			totalLock = is.popByte();
			costStoneLock1 = is.popInteger();
			costMoneyLock1 = is.popInteger();
			costStoneLock2 = is.popInteger();
			costMoneyLock2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cid);
			os.pushByte(totalLock);
			os.pushInteger(costStoneLock1);
			os.pushInteger(costMoneyLock1);
			os.pushInteger(costStoneLock2);
			os.pushInteger(costMoneyLock2);
		}

		public short cid;
		public byte totalLock;
		public int costStoneLock1;
		public int costMoneyLock1;
		public int costStoneLock2;
		public int costMoneyLock2;
	}

	public static class GeneralStoneBasicCFGS implements Stream.IStreamable
	{

		public GeneralStoneBasicCFGS() { }

		public GeneralStoneBasicCFGS(short sid, String name, byte stype, short maxSkillCount, 
		                             byte skillAdd, int exp, int costExp, int upCost, 
		                             short upIid, short specialgid, short group)
		{
			this.sid = sid;
			this.name = name;
			this.stype = stype;
			this.maxSkillCount = maxSkillCount;
			this.skillAdd = skillAdd;
			this.exp = exp;
			this.costExp = costExp;
			this.upCost = upCost;
			this.upIid = upIid;
			this.specialgid = specialgid;
			this.group = group;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			name = is.popString();
			stype = is.popByte();
			maxSkillCount = is.popShort();
			skillAdd = is.popByte();
			exp = is.popInteger();
			costExp = is.popInteger();
			upCost = is.popInteger();
			upIid = is.popShort();
			specialgid = is.popShort();
			group = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushString(name);
			os.pushByte(stype);
			os.pushShort(maxSkillCount);
			os.pushByte(skillAdd);
			os.pushInteger(exp);
			os.pushInteger(costExp);
			os.pushInteger(upCost);
			os.pushShort(upIid);
			os.pushShort(specialgid);
			os.pushShort(group);
		}

		public short sid;
		public String name;
		public byte stype;
		public short maxSkillCount;
		public byte skillAdd;
		public int exp;
		public int costExp;
		public int upCost;
		public short upIid;
		public short specialgid;
		public short group;
	}

	public static class GeneralStoneCFGS implements Stream.IStreamable
	{

		public GeneralStoneCFGS() { }

		public GeneralStoneCFGS(List<GeneralStoneBasicCFGS> basic, List<GeneralStonePosCFGS> pos, List<GeneralStoneEvoCostCFGS> evoCosts, List<GeneralStonePropCFGS> sprop, 
		                        List<GeneralStonePropValueCFGS> spropValue)
		{
			this.basic = basic;
			this.pos = pos;
			this.evoCosts = evoCosts;
			this.sprop = sprop;
			this.spropValue = spropValue;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			basic = is.popList(GeneralStoneBasicCFGS.class);
			pos = is.popList(GeneralStonePosCFGS.class);
			evoCosts = is.popList(GeneralStoneEvoCostCFGS.class);
			sprop = is.popList(GeneralStonePropCFGS.class);
			spropValue = is.popList(GeneralStonePropValueCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(basic);
			os.pushList(pos);
			os.pushList(evoCosts);
			os.pushList(sprop);
			os.pushList(spropValue);
		}

		public List<GeneralStoneBasicCFGS> basic;
		public List<GeneralStonePosCFGS> pos;
		public List<GeneralStoneEvoCostCFGS> evoCosts;
		public List<GeneralStonePropCFGS> sprop;
		public List<GeneralStonePropValueCFGS> spropValue;
	}

	public static class GeneralSeyenCFGS implements Stream.IStreamable
	{

		public GeneralSeyenCFGS() { }

		public GeneralSeyenCFGS(List<GeneralSeyenExpAddCFGS> expAdd, List<GeneralSeyenExpCFGS> sexp, GeneralSeyenCommonCFGS commonValue)
		{
			this.expAdd = expAdd;
			this.sexp = sexp;
			this.commonValue = commonValue;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			expAdd = is.popList(GeneralSeyenExpAddCFGS.class);
			sexp = is.popList(GeneralSeyenExpCFGS.class);
			if( commonValue == null )
				commonValue = new GeneralSeyenCommonCFGS();
			is.pop(commonValue);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(expAdd);
			os.pushList(sexp);
			os.push(commonValue);
		}

		public List<GeneralSeyenExpAddCFGS> expAdd;
		public List<GeneralSeyenExpCFGS> sexp;
		public GeneralSeyenCommonCFGS commonValue;
	}

	public static class HeadIconCFGS implements Stream.IStreamable
	{

		public HeadIconCFGS() { }

		public HeadIconCFGS(short sid, short type, short itemId, int stime, 
		                    int etime)
		{
			this.sid = sid;
			this.type = type;
			this.itemId = itemId;
			this.stime = stime;
			this.etime = etime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			type = is.popShort();
			itemId = is.popShort();
			stime = is.popInteger();
			etime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(type);
			os.pushShort(itemId);
			os.pushInteger(stime);
			os.pushInteger(etime);
		}

		public short sid;
		public short type;
		public short itemId;
		public int stime;
		public int etime;
	}

	public static class DBDiskBetInfo implements Stream.IStreamable
	{

		public DBDiskBetInfo() { }

		public DBDiskBetInfo(int id, int score, int rmb, short times1, 
		                     short times2, short times3, short times4, int bonusday, 
		                     int padding2, int padding3, byte padding5, byte padding6, 
		                     byte padding7, byte padding8)
		{
			this.id = id;
			this.score = score;
			this.rmb = rmb;
			this.times1 = times1;
			this.times2 = times2;
			this.times3 = times3;
			this.times4 = times4;
			this.bonusday = bonusday;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding5 = padding5;
			this.padding6 = padding6;
			this.padding7 = padding7;
			this.padding8 = padding8;
		}

		public DBDiskBetInfo ksClone()
		{
			return new DBDiskBetInfo(id, score, rmb, times1, 
			                         times2, times3, times4, bonusday, 
			                         padding2, padding3, padding5, padding6, 
			                         padding7, padding8);
		}

		public DBDiskBetInfo kdClone()
		{
			DBDiskBetInfo _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			score = is.popInteger();
			rmb = is.popInteger();
			times1 = is.popShort();
			times2 = is.popShort();
			times3 = is.popShort();
			times4 = is.popShort();
			bonusday = is.popInteger();
			padding2 = is.popInteger();
			padding3 = is.popInteger();
			padding5 = is.popByte();
			padding6 = is.popByte();
			padding7 = is.popByte();
			padding8 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(score);
			os.pushInteger(rmb);
			os.pushShort(times1);
			os.pushShort(times2);
			os.pushShort(times3);
			os.pushShort(times4);
			os.pushInteger(bonusday);
			os.pushInteger(padding2);
			os.pushInteger(padding3);
			os.pushByte(padding5);
			os.pushByte(padding6);
			os.pushByte(padding7);
			os.pushByte(padding8);
		}

		public int id;
		public int score;
		public int rmb;
		public short times1;
		public short times2;
		public short times3;
		public short times4;
		public int bonusday;
		public int padding2;
		public int padding3;
		public byte padding5;
		public byte padding6;
		public byte padding7;
		public byte padding8;
	}

	public static class DBBlessInfo implements Stream.IStreamable
	{

		public DBBlessInfo() { }

		public DBBlessInfo(List<Short> gid, int refresh1, int refresh2, byte padding7, 
		                   byte padding8)
		{
			this.gid = gid;
			this.refresh1 = refresh1;
			this.refresh2 = refresh2;
			this.padding7 = padding7;
			this.padding8 = padding8;
		}

		public DBBlessInfo ksClone()
		{
			return new DBBlessInfo(gid, refresh1, refresh2, padding7, 
			                       padding8);
		}

		public DBBlessInfo kdClone()
		{
			DBBlessInfo _kio_clobj = ksClone();
			_kio_clobj.gid = new ArrayList<Short>();
			for(Short _kio_iter : gid)
			{
				_kio_clobj.gid.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShortList();
			refresh1 = is.popInteger();
			refresh2 = is.popInteger();
			padding7 = is.popByte();
			padding8 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShortList(gid);
			os.pushInteger(refresh1);
			os.pushInteger(refresh2);
			os.pushByte(padding7);
			os.pushByte(padding8);
		}

		public List<Short> gid;
		public int refresh1;
		public int refresh2;
		public byte padding7;
		public byte padding8;
	}

	public static class DBDiskBet implements Stream.IStreamable
	{

		public DBDiskBet() { }

		public DBDiskBet(List<DBDiskBetRankList> ranks, List<DBDiskBetRole> roles, List<DBDiskBetRankList> rmbranks, List<DBDiskBetRank> ranksHis, 
		                 int day, int pool, int statime, int endtime, 
		                 int minscore, int minRmb, int r1, int r2)
		{
			this.ranks = ranks;
			this.roles = roles;
			this.rmbranks = rmbranks;
			this.ranksHis = ranksHis;
			this.day = day;
			this.pool = pool;
			this.statime = statime;
			this.endtime = endtime;
			this.minscore = minscore;
			this.minRmb = minRmb;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBDiskBet ksClone()
		{
			return new DBDiskBet(ranks, roles, rmbranks, ranksHis, 
			                     day, pool, statime, endtime, 
			                     minscore, minRmb, r1, r2);
		}

		public DBDiskBet kdClone()
		{
			DBDiskBet _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<DBDiskBetRankList>();
			for(DBDiskBetRankList _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.roles = new ArrayList<DBDiskBetRole>();
			for(DBDiskBetRole _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter.kdClone());
			}
			_kio_clobj.rmbranks = new ArrayList<DBDiskBetRankList>();
			for(DBDiskBetRankList _kio_iter : rmbranks)
			{
				_kio_clobj.rmbranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranksHis = new ArrayList<DBDiskBetRank>();
			for(DBDiskBetRank _kio_iter : ranksHis)
			{
				_kio_clobj.ranksHis.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(DBDiskBetRankList.class);
			roles = is.popList(DBDiskBetRole.class);
			rmbranks = is.popList(DBDiskBetRankList.class);
			ranksHis = is.popList(DBDiskBetRank.class);
			day = is.popInteger();
			pool = is.popInteger();
			statime = is.popInteger();
			endtime = is.popInteger();
			minscore = is.popInteger();
			minRmb = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
			os.pushList(roles);
			os.pushList(rmbranks);
			os.pushList(ranksHis);
			os.pushInteger(day);
			os.pushInteger(pool);
			os.pushInteger(statime);
			os.pushInteger(endtime);
			os.pushInteger(minscore);
			os.pushInteger(minRmb);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBDiskBetRankList> ranks;
		public List<DBDiskBetRole> roles;
		public List<DBDiskBetRankList> rmbranks;
		public List<DBDiskBetRank> ranksHis;
		public int day;
		public int pool;
		public int statime;
		public int endtime;
		public int minscore;
		public int minRmb;
		public int r1;
		public int r2;
	}

	public static class DBDiskBetRankList implements Stream.IStreamable
	{

		public DBDiskBetRankList() { }

		public DBDiskBetRankList(short combatId, List<DBDiskBetRank> DiskBetRank)
		{
			this.combatId = combatId;
			this.DiskBetRank = DiskBetRank;
		}

		public DBDiskBetRankList ksClone()
		{
			return new DBDiskBetRankList(combatId, DiskBetRank);
		}

		public DBDiskBetRankList kdClone()
		{
			DBDiskBetRankList _kio_clobj = ksClone();
			_kio_clobj.DiskBetRank = new ArrayList<DBDiskBetRank>();
			for(DBDiskBetRank _kio_iter : DiskBetRank)
			{
				_kio_clobj.DiskBetRank.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			DiskBetRank = is.popList(DBDiskBetRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushList(DiskBetRank);
		}

		public short combatId;
		public List<DBDiskBetRank> DiskBetRank;
	}

	public static class DBDiskBetRole implements Stream.IStreamable
	{

		public DBDiskBetRole() { }

		public DBDiskBetRole(int rid, short combat, int rank, short r12, 
		                     int r2)
		{
			this.rid = rid;
			this.combat = combat;
			this.rank = rank;
			this.r12 = r12;
			this.r2 = r2;
		}

		public DBDiskBetRole ksClone()
		{
			return new DBDiskBetRole(rid, combat, rank, r12, 
			                         r2);
		}

		public DBDiskBetRole kdClone()
		{
			DBDiskBetRole _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			combat = is.popShort();
			rank = is.popInteger();
			r12 = is.popShort();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushShort(combat);
			os.pushInteger(rank);
			os.pushShort(r12);
			os.pushInteger(r2);
		}

		public int rid;
		public short combat;
		public int rank;
		public short r12;
		public int r2;
	}

	public static class DBDiskBetRank implements Stream.IStreamable
	{

		public DBDiskBetRank() { }

		public DBDiskBetRank(short tId, String serverName, int rid, String rname, 
		                     short headIconId, short lvl, int score, int rmb)
		{
			this.tId = tId;
			this.serverName = serverName;
			this.rid = rid;
			this.rname = rname;
			this.headIconId = headIconId;
			this.lvl = lvl;
			this.score = score;
			this.rmb = rmb;
		}

		public DBDiskBetRank ksClone()
		{
			return new DBDiskBetRank(tId, serverName, rid, rname, 
			                         headIconId, lvl, score, rmb);
		}

		public DBDiskBetRank kdClone()
		{
			DBDiskBetRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tId = is.popShort();
			serverName = is.popString();
			rid = is.popInteger();
			rname = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
			score = is.popInteger();
			rmb = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(tId);
			os.pushString(serverName);
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushShort(headIconId);
			os.pushShort(lvl);
			os.pushInteger(score);
			os.pushInteger(rmb);
		}

		public short tId;
		public String serverName;
		public int rid;
		public String rname;
		public short headIconId;
		public short lvl;
		public int score;
		public int rmb;
	}

	public static class DiskBetCFGS implements Stream.IStreamable
	{

		public DiskBetCFGS() { }

		public DiskBetCFGS(short cLevel, short rate, short drop1, short drop2, 
		                   int minScore, List<DiskBetCostCFGS> cost)
		{
			this.cLevel = cLevel;
			this.rate = rate;
			this.drop1 = drop1;
			this.drop2 = drop2;
			this.minScore = minScore;
			this.cost = cost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cLevel = is.popShort();
			rate = is.popShort();
			drop1 = is.popShort();
			drop2 = is.popShort();
			minScore = is.popInteger();
			cost = is.popList(DiskBetCostCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cLevel);
			os.pushShort(rate);
			os.pushShort(drop1);
			os.pushShort(drop2);
			os.pushInteger(minScore);
			os.pushList(cost);
		}

		public short cLevel;
		public short rate;
		public short drop1;
		public short drop2;
		public int minScore;
		public List<DiskBetCostCFGS> cost;
	}

	public static class DiskBetItemsCFGS implements Stream.IStreamable
	{

		public DiskBetItemsCFGS() { }

		public DiskBetItemsCFGS(short type, short rankmin, short rankmax, List<DropEntryNew> eItems)
		{
			this.type = type;
			this.rankmin = rankmin;
			this.rankmax = rankmax;
			this.eItems = eItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popShort();
			rankmin = is.popShort();
			rankmax = is.popShort();
			eItems = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(type);
			os.pushShort(rankmin);
			os.pushShort(rankmax);
			os.pushList(eItems);
		}

		public short type;
		public short rankmin;
		public short rankmax;
		public List<DropEntryNew> eItems;
	}

	public static class DiskBetCostCFGS implements Stream.IStreamable
	{

		public DiskBetCostCFGS() { }

		public DiskBetCostCFGS(short sid, List<Short> cost)
		{
			this.sid = sid;
			this.cost = cost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			cost = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShortList(cost);
		}

		public short sid;
		public List<Short> cost;
	}

	public static class BestowCFGS implements Stream.IStreamable
	{

		public BestowCFGS() { }

		public BestowCFGS(short sid, short level, short gid, List<DropEntryNew> eItems)
		{
			this.sid = sid;
			this.level = level;
			this.gid = gid;
			this.eItems = eItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			level = is.popShort();
			gid = is.popShort();
			eItems = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(level);
			os.pushShort(gid);
			os.pushList(eItems);
		}

		public short sid;
		public short level;
		public short gid;
		public List<DropEntryNew> eItems;
	}

	public static class BestowItemCFGS implements Stream.IStreamable
	{

		public BestowItemCFGS() { }

		public BestowItemCFGS(short iid1, short iid2, List<Short> item1, List<Short> item2)
		{
			this.iid1 = iid1;
			this.iid2 = iid2;
			this.item1 = item1;
			this.item2 = item2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			iid1 = is.popShort();
			iid2 = is.popShort();
			item1 = is.popShortList();
			item2 = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(iid1);
			os.pushShort(iid2);
			os.pushShortList(item1);
			os.pushShortList(item2);
		}

		public short iid1;
		public short iid2;
		public List<Short> item1;
		public List<Short> item2;
	}

	public static class BlessCFGS implements Stream.IStreamable
	{

		public BlessCFGS() { }

		public BlessCFGS(short sid, float p1, float p2, float p3, 
		                 float p1Max, float p2Max, float p3Max, List<DropEntryNew> eItems)
		{
			this.sid = sid;
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.p1Max = p1Max;
			this.p2Max = p2Max;
			this.p3Max = p3Max;
			this.eItems = eItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			p1 = is.popFloat();
			p2 = is.popFloat();
			p3 = is.popFloat();
			p1Max = is.popFloat();
			p2Max = is.popFloat();
			p3Max = is.popFloat();
			eItems = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushFloat(p1);
			os.pushFloat(p2);
			os.pushFloat(p3);
			os.pushFloat(p1Max);
			os.pushFloat(p2Max);
			os.pushFloat(p3Max);
			os.pushList(eItems);
		}

		public short sid;
		public float p1;
		public float p2;
		public float p3;
		public float p1Max;
		public float p2Max;
		public float p3Max;
		public List<DropEntryNew> eItems;
	}

	public static class CommomConstantCFGS implements Stream.IStreamable
	{

		public CommomConstantCFGS() { }

		public CommomConstantCFGS(short blessMaxNum, short blessCnt, int blessTime1, int blessTime2, 
		                          float blessMaxP1, float blessMaxP2, float blessMaxP3)
		{
			this.blessMaxNum = blessMaxNum;
			this.blessCnt = blessCnt;
			this.blessTime1 = blessTime1;
			this.blessTime2 = blessTime2;
			this.blessMaxP1 = blessMaxP1;
			this.blessMaxP2 = blessMaxP2;
			this.blessMaxP3 = blessMaxP3;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			blessMaxNum = is.popShort();
			blessCnt = is.popShort();
			blessTime1 = is.popInteger();
			blessTime2 = is.popInteger();
			blessMaxP1 = is.popFloat();
			blessMaxP2 = is.popFloat();
			blessMaxP3 = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(blessMaxNum);
			os.pushShort(blessCnt);
			os.pushInteger(blessTime1);
			os.pushInteger(blessTime2);
			os.pushFloat(blessMaxP1);
			os.pushFloat(blessMaxP2);
			os.pushFloat(blessMaxP3);
		}

		public short blessMaxNum;
		public short blessCnt;
		public int blessTime1;
		public int blessTime2;
		public float blessMaxP1;
		public float blessMaxP2;
		public float blessMaxP3;
	}

	public static class HeroesBossCFGS implements Stream.IStreamable
	{

		public HeroesBossCFGS() { }

		public HeroesBossCFGS(short cLevel, short battlecount, short cost1, List<Short> gids, 
		                      short gcount, List<HeroesBossScoreCFGS> heroesBossScore, List<HeroesBossItemsCFGS> heroesBossItems, List<HeroesBossTypeCFGS> heroesBossType, 
		                      List<HeroesBossBuffCFGS> heroesBossBuff)
		{
			this.cLevel = cLevel;
			this.battlecount = battlecount;
			this.cost1 = cost1;
			this.gids = gids;
			this.gcount = gcount;
			this.heroesBossScore = heroesBossScore;
			this.heroesBossItems = heroesBossItems;
			this.heroesBossType = heroesBossType;
			this.heroesBossBuff = heroesBossBuff;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cLevel = is.popShort();
			battlecount = is.popShort();
			cost1 = is.popShort();
			gids = is.popShortList();
			gcount = is.popShort();
			heroesBossScore = is.popList(HeroesBossScoreCFGS.class);
			heroesBossItems = is.popList(HeroesBossItemsCFGS.class);
			heroesBossType = is.popList(HeroesBossTypeCFGS.class);
			heroesBossBuff = is.popList(HeroesBossBuffCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cLevel);
			os.pushShort(battlecount);
			os.pushShort(cost1);
			os.pushShortList(gids);
			os.pushShort(gcount);
			os.pushList(heroesBossScore);
			os.pushList(heroesBossItems);
			os.pushList(heroesBossType);
			os.pushList(heroesBossBuff);
		}

		public short cLevel;
		public short battlecount;
		public short cost1;
		public List<Short> gids;
		public short gcount;
		public List<HeroesBossScoreCFGS> heroesBossScore;
		public List<HeroesBossItemsCFGS> heroesBossItems;
		public List<HeroesBossTypeCFGS> heroesBossType;
		public List<HeroesBossBuffCFGS> heroesBossBuff;
	}

	public static class HeroesBossTypeCFGS implements Stream.IStreamable
	{

		public HeroesBossTypeCFGS() { }

		public HeroesBossTypeCFGS(short sid, short type, short levelmin, short levelmax)
		{
			this.sid = sid;
			this.type = type;
			this.levelmin = levelmin;
			this.levelmax = levelmax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			type = is.popShort();
			levelmin = is.popShort();
			levelmax = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(type);
			os.pushShort(levelmin);
			os.pushShort(levelmax);
		}

		public short sid;
		public short type;
		public short levelmin;
		public short levelmax;
	}

	public static class HeroesBossItemsCFGS implements Stream.IStreamable
	{

		public HeroesBossItemsCFGS() { }

		public HeroesBossItemsCFGS(short rank, List<DropEntryNew> eItems)
		{
			this.rank = rank;
			this.eItems = eItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rank = is.popShort();
			eItems = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(rank);
			os.pushList(eItems);
		}

		public short rank;
		public List<DropEntryNew> eItems;
	}

	public static class HeroesBossBuffCFGS implements Stream.IStreamable
	{

		public HeroesBossBuffCFGS() { }

		public HeroesBossBuffCFGS(short sid, short cost)
		{
			this.sid = sid;
			this.cost = cost;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			cost = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(cost);
		}

		public short sid;
		public short cost;
	}

	public static class HeroesBossScoreCFGS implements Stream.IStreamable
	{

		public HeroesBossScoreCFGS() { }

		public HeroesBossScoreCFGS(short sid, short rank, short type, short score)
		{
			this.sid = sid;
			this.rank = rank;
			this.type = type;
			this.score = score;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			rank = is.popShort();
			type = is.popShort();
			score = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(rank);
			os.pushShort(type);
			os.pushShort(score);
		}

		public short sid;
		public short rank;
		public short type;
		public short score;
	}

	public static class ExpiratBossCFGS implements Stream.IStreamable
	{

		public ExpiratBossCFGS() { }

		public ExpiratBossCFGS(short cLevel, short count, short cost1, short cost2, 
		                       List<ExpiratBossBasicCFGS> expiratBossBasic, List<ExpiratBossItemsCFGS> expiratBossItems)
		{
			this.cLevel = cLevel;
			this.count = count;
			this.cost1 = cost1;
			this.cost2 = cost2;
			this.expiratBossBasic = expiratBossBasic;
			this.expiratBossItems = expiratBossItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cLevel = is.popShort();
			count = is.popShort();
			cost1 = is.popShort();
			cost2 = is.popShort();
			expiratBossBasic = is.popList(ExpiratBossBasicCFGS.class);
			expiratBossItems = is.popList(ExpiratBossItemsCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cLevel);
			os.pushShort(count);
			os.pushShort(cost1);
			os.pushShort(cost2);
			os.pushList(expiratBossBasic);
			os.pushList(expiratBossItems);
		}

		public short cLevel;
		public short count;
		public short cost1;
		public short cost2;
		public List<ExpiratBossBasicCFGS> expiratBossBasic;
		public List<ExpiratBossItemsCFGS> expiratBossItems;
	}

	public static class ExpiratBossBasicCFGS implements Stream.IStreamable
	{

		public ExpiratBossBasicCFGS() { }

		public ExpiratBossBasicCFGS(short sid, short tid, List<Integer> dropId, short cmax)
		{
			this.sid = sid;
			this.tid = tid;
			this.dropId = dropId;
			this.cmax = cmax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			tid = is.popShort();
			dropId = is.popIntegerList();
			cmax = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(tid);
			os.pushIntegerList(dropId);
			os.pushShort(cmax);
		}

		public short sid;
		public short tid;
		public List<Integer> dropId;
		public short cmax;
	}

	public static class ExpiratBossItemsCFGS implements Stream.IStreamable
	{

		public ExpiratBossItemsCFGS() { }

		public ExpiratBossItemsCFGS(short sid, short type, short rankmin, short rankmax, 
		                            List<DropEntryNew> eItems)
		{
			this.sid = sid;
			this.type = type;
			this.rankmin = rankmin;
			this.rankmax = rankmax;
			this.eItems = eItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			type = is.popShort();
			rankmin = is.popShort();
			rankmax = is.popShort();
			eItems = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(type);
			os.pushShort(rankmin);
			os.pushShort(rankmax);
			os.pushList(eItems);
		}

		public short sid;
		public short type;
		public short rankmin;
		public short rankmax;
		public List<DropEntryNew> eItems;
	}

	public static class ChangeServerCFGS implements Stream.IStreamable
	{

		public ChangeServerCFGS() { }

		public ChangeServerCFGS(short cLevel, List<ChangeServerListCFGS> serverList, List<ChangServerItemsCFGS> changServerItems)
		{
			this.cLevel = cLevel;
			this.serverList = serverList;
			this.changServerItems = changServerItems;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			cLevel = is.popShort();
			serverList = is.popList(ChangeServerListCFGS.class);
			changServerItems = is.popList(ChangServerItemsCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(cLevel);
			os.pushList(serverList);
			os.pushList(changServerItems);
		}

		public short cLevel;
		public List<ChangeServerListCFGS> serverList;
		public List<ChangServerItemsCFGS> changServerItems;
	}

	public static class ChangeServerListCFGS implements Stream.IStreamable
	{

		public ChangeServerListCFGS() { }

		public ChangeServerListCFGS(int sid, int toid, short days)
		{
			this.sid = sid;
			this.toid = toid;
			this.days = days;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popInteger();
			toid = is.popInteger();
			days = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(sid);
			os.pushInteger(toid);
			os.pushShort(days);
		}

		public int sid;
		public int toid;
		public short days;
	}

	public static class ChangServerItemsCFGS implements Stream.IStreamable
	{

		public ChangServerItemsCFGS() { }

		public ChangServerItemsCFGS(short sid, short iid, byte itemType, short icount)
		{
			this.sid = sid;
			this.iid = iid;
			this.itemType = itemType;
			this.icount = icount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			iid = is.popShort();
			itemType = is.popByte();
			icount = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(iid);
			os.pushByte(itemType);
			os.pushShort(icount);
		}

		public short sid;
		public short iid;
		public byte itemType;
		public short icount;
	}

	public static class GeneralSeyenExpAddCFGS implements Stream.IStreamable
	{

		public GeneralSeyenExpAddCFGS() { }

		public GeneralSeyenExpAddCFGS(short sid, short weight, int exp)
		{
			this.sid = sid;
			this.weight = weight;
			this.exp = exp;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sid = is.popShort();
			weight = is.popShort();
			exp = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(sid);
			os.pushShort(weight);
			os.pushInteger(exp);
		}

		public short sid;
		public short weight;
		public int exp;
	}

	public static class GeneralSeyenExpCFGS implements Stream.IStreamable
	{

		public GeneralSeyenExpCFGS() { }

		public GeneralSeyenExpCFGS(int level, int exp, int limitLvl)
		{
			this.level = level;
			this.exp = exp;
			this.limitLvl = limitLvl;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			level = is.popInteger();
			exp = is.popInteger();
			limitLvl = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(level);
			os.pushInteger(exp);
			os.pushInteger(limitLvl);
		}

		public int level;
		public int exp;
		public int limitLvl;
	}

	public static class GeneralSeyenCommonCFGS implements Stream.IStreamable
	{

		public GeneralSeyenCommonCFGS() { }

		public GeneralSeyenCommonCFGS(int reversCost, float expRate, short sItemId, float reversRate)
		{
			this.reversCost = reversCost;
			this.expRate = expRate;
			this.sItemId = sItemId;
			this.reversRate = reversRate;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			reversCost = is.popInteger();
			expRate = is.popFloat();
			sItemId = is.popShort();
			reversRate = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(reversCost);
			os.pushFloat(expRate);
			os.pushShort(sItemId);
			os.pushFloat(reversRate);
		}

		public int reversCost;
		public float expRate;
		public short sItemId;
		public float reversRate;
	}

	public static class DungeonBasicCFGS implements Stream.IStreamable
	{

		public DungeonBasicCFGS() { }

		public DungeonBasicCFGS(byte dailyTimes, short boxStone, short deployMinutes)
		{
			this.dailyTimes = dailyTimes;
			this.boxStone = boxStone;
			this.deployMinutes = deployMinutes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			dailyTimes = is.popByte();
			boxStone = is.popShort();
			deployMinutes = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(dailyTimes);
			os.pushShort(boxStone);
			os.pushShort(deployMinutes);
		}

		public byte dailyTimes;
		public short boxStone;
		public short deployMinutes;
	}

	public static class DungeonTreasureCFGS implements Stream.IStreamable
	{

		public DungeonTreasureCFGS() { }

		public DungeonTreasureCFGS(byte percent, List<Short> drops)
		{
			this.percent = percent;
			this.drops = drops;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			percent = is.popByte();
			drops = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(percent);
			os.pushShortList(drops);
		}

		public byte percent;
		public List<Short> drops;
	}

	public static class DungeonStageCFGS implements Stream.IStreamable
	{

		public DungeonStageCFGS() { }

		public DungeonStageCFGS(short combatId, List<Short> boxDropIds, List<DungeonTreasureCFGS> treasureDrops, short cupCount, 
		                        byte cupPoss)
		{
			this.combatId = combatId;
			this.boxDropIds = boxDropIds;
			this.treasureDrops = treasureDrops;
			this.cupCount = cupCount;
			this.cupPoss = cupPoss;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			boxDropIds = is.popShortList();
			treasureDrops = is.popList(DungeonTreasureCFGS.class);
			cupCount = is.popShort();
			cupPoss = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushShortList(boxDropIds);
			os.pushList(treasureDrops);
			os.pushShort(cupCount);
			os.pushByte(cupPoss);
		}

		public short combatId;
		public List<Short> boxDropIds;
		public List<DungeonTreasureCFGS> treasureDrops;
		public short cupCount;
		public byte cupPoss;
	}

	public static class DungeonApplyCFGS implements Stream.IStreamable
	{

		public DungeonApplyCFGS() { }

		public DungeonApplyCFGS(byte type, short id)
		{
			this.type = type;
			this.id = id;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			id = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(id);
		}

		public byte type;
		public short id;
	}

	public static class DungeonChapterCFGS implements Stream.IStreamable
	{

		public DungeonChapterCFGS() { }

		public DungeonChapterCFGS(short openLvl, int resetActivity, List<DungeonStageCFGS> stages, List<DungeonApplyCFGS> applies)
		{
			this.openLvl = openLvl;
			this.resetActivity = resetActivity;
			this.stages = stages;
			this.applies = applies;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openLvl = is.popShort();
			resetActivity = is.popInteger();
			stages = is.popList(DungeonStageCFGS.class);
			applies = is.popList(DungeonApplyCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(openLvl);
			os.pushInteger(resetActivity);
			os.pushList(stages);
			os.pushList(applies);
		}

		public short openLvl;
		public int resetActivity;
		public List<DungeonStageCFGS> stages;
		public List<DungeonApplyCFGS> applies;
	}

	public static class DungeonRewardCFGS implements Stream.IStreamable
	{

		public DungeonRewardCFGS() { }

		public DungeonRewardCFGS(int rankFloor, int point, int money)
		{
			this.rankFloor = rankFloor;
			this.point = point;
			this.money = money;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankFloor = is.popInteger();
			point = is.popInteger();
			money = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankFloor);
			os.pushInteger(point);
			os.pushInteger(money);
		}

		public int rankFloor;
		public int point;
		public int money;
	}

	public static class DungeonBeyondRewardCFGS implements Stream.IStreamable
	{

		public DungeonBeyondRewardCFGS() { }

		public DungeonBeyondRewardCFGS(byte chapter, int stone)
		{
			this.chapter = chapter;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			chapter = is.popByte();
			stone = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(chapter);
			os.pushInteger(stone);
		}

		public byte chapter;
		public int stone;
	}

	public static class DungeonCFGS implements Stream.IStreamable
	{

		public DungeonCFGS() { }

		public DungeonCFGS(DungeonBasicCFGS basic, List<DungeonChapterCFGS> chapters, List<DungeonRewardCFGS> rewards, List<DungeonBeyondRewardCFGS> beyondRewards)
		{
			this.basic = basic;
			this.chapters = chapters;
			this.rewards = rewards;
			this.beyondRewards = beyondRewards;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( basic == null )
				basic = new DungeonBasicCFGS();
			is.pop(basic);
			chapters = is.popList(DungeonChapterCFGS.class);
			rewards = is.popList(DungeonRewardCFGS.class);
			beyondRewards = is.popList(DungeonBeyondRewardCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(basic);
			os.pushList(chapters);
			os.pushList(rewards);
			os.pushList(beyondRewards);
		}

		public DungeonBasicCFGS basic;
		public List<DungeonChapterCFGS> chapters;
		public List<DungeonRewardCFGS> rewards;
		public List<DungeonBeyondRewardCFGS> beyondRewards;
	}

	public static class RelationPropCFGS implements Stream.IStreamable
	{

		public RelationPropCFGS() { }

		public RelationPropCFGS(short id, byte type, float value)
		{
			this.id = id;
			this.type = type;
			this.value = value;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			type = is.popByte();
			value = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(type);
			os.pushFloat(value);
		}

		public short id;
		public byte type;
		public float value;
	}

	public static class RelationCFGS implements Stream.IStreamable
	{

		public RelationCFGS() { }

		public RelationCFGS(short id, String name, List<Short> activateItems, List<RelationPropCFGS> props)
		{
			this.id = id;
			this.name = name;
			this.activateItems = activateItems;
			this.props = props;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			name = is.popString();
			activateItems = is.popShortList();
			props = is.popList(RelationPropCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushString(name);
			os.pushShortList(activateItems);
			os.pushList(props);
		}

		public short id;
		public String name;
		public List<Short> activateItems;
		public List<RelationPropCFGS> props;
	}

	public static class RelationItemCFGS implements Stream.IStreamable
	{

		public RelationItemCFGS() { }

		public RelationItemCFGS(short iid, short gid)
		{
			this.iid = iid;
			this.gid = gid;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			iid = is.popShort();
			gid = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(iid);
			os.pushShort(gid);
		}

		public short iid;
		public short gid;
	}

	public static class RelationConsumeCFGS implements Stream.IStreamable
	{

		public RelationConsumeCFGS() { }

		public RelationConsumeCFGS(short gid, List<Integer> consume)
		{
			this.gid = gid;
			this.consume = consume;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			consume = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.pushIntegerList(consume);
		}

		public short gid;
		public List<Integer> consume;
	}

	public static class RelationsCFGS implements Stream.IStreamable
	{

		public RelationsCFGS() { }

		public RelationsCFGS(List<RelationCFGS> relations, List<RelationItemCFGS> items, List<RelationConsumeCFGS> consumes)
		{
			this.relations = relations;
			this.items = items;
			this.consumes = consumes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			relations = is.popList(RelationCFGS.class);
			items = is.popList(RelationItemCFGS.class);
			consumes = is.popList(RelationConsumeCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(relations);
			os.pushList(items);
			os.pushList(consumes);
		}

		public List<RelationCFGS> relations;
		public List<RelationItemCFGS> items;
		public List<RelationConsumeCFGS> consumes;
	}

	public static class MercCFGS implements Stream.IStreamable
	{

		public MercCFGS() { }

		public MercCFGS(int maxHours, short minGeneralLevel, int timeGainA, int timeGainB, 
		                float hireGainA, short timedisk1, short timedisk2)
		{
			this.maxHours = maxHours;
			this.minGeneralLevel = minGeneralLevel;
			this.timeGainA = timeGainA;
			this.timeGainB = timeGainB;
			this.hireGainA = hireGainA;
			this.timedisk1 = timedisk1;
			this.timedisk2 = timedisk2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			maxHours = is.popInteger();
			minGeneralLevel = is.popShort();
			timeGainA = is.popInteger();
			timeGainB = is.popInteger();
			hireGainA = is.popFloat();
			timedisk1 = is.popShort();
			timedisk2 = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(maxHours);
			os.pushShort(minGeneralLevel);
			os.pushInteger(timeGainA);
			os.pushInteger(timeGainB);
			os.pushFloat(hireGainA);
			os.pushShort(timedisk1);
			os.pushShort(timedisk2);
		}

		public int maxHours;
		public short minGeneralLevel;
		public int timeGainA;
		public int timeGainB;
		public float hireGainA;
		public short timedisk1;
		public short timedisk2;
	}

	public static class DuelStarsCFGS implements Stream.IStreamable
	{

		public DuelStarsCFGS() { }

		public DuelStarsCFGS(short lvlMin, short win2Stars, short win3Stars, short win4Stars)
		{
			this.lvlMin = lvlMin;
			this.win2Stars = win2Stars;
			this.win3Stars = win3Stars;
			this.win4Stars = win4Stars;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvlMin = is.popShort();
			win2Stars = is.popShort();
			win3Stars = is.popShort();
			win4Stars = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvlMin);
			os.pushShort(win2Stars);
			os.pushShort(win3Stars);
			os.pushShort(win4Stars);
		}

		public short lvlMin;
		public short win2Stars;
		public short win3Stars;
		public short win4Stars;
	}

	public static class DuelRewardCFGS implements Stream.IStreamable
	{

		public DuelRewardCFGS() { }

		public DuelRewardCFGS(List<DropEntryNew> drops, int point)
		{
			this.drops = drops;
			this.point = point;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			drops = is.popList(DropEntryNew.class);
			point = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(drops);
			os.pushInteger(point);
		}

		public List<DropEntryNew> drops;
		public int point;
	}

	public static class DuelTimeCFGS implements Stream.IStreamable
	{

		public DuelTimeCFGS() { }

		public DuelTimeCFGS(short begin, short end)
		{
			this.begin = begin;
			this.end = end;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			begin = is.popShort();
			end = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(begin);
			os.pushShort(end);
		}

		public short begin;
		public short end;
	}

	public static class DuelCFGS implements Stream.IStreamable
	{

		public DuelCFGS() { }

		public DuelCFGS(List<DuelTimeCFGS> specialTimes, byte playTimes, byte rewardTimes, int specialLegendRewardPoints, 
		                int specialRewardPoints, int rewardPoints, List<Short> upgrade, List<DuelStarsCFGS> winStars, 
		                List<DuelStarsCFGS> loseStars, List<DuelStarsCFGS> legendWinStars, List<DuelStarsCFGS> legendLoseStars, List<Short> resetLevels, 
		                List<DuelRewardCFGS> rewards, List<Integer> buyTimeCosts, short resetTime)
		{
			this.specialTimes = specialTimes;
			this.playTimes = playTimes;
			this.rewardTimes = rewardTimes;
			this.specialLegendRewardPoints = specialLegendRewardPoints;
			this.specialRewardPoints = specialRewardPoints;
			this.rewardPoints = rewardPoints;
			this.upgrade = upgrade;
			this.winStars = winStars;
			this.loseStars = loseStars;
			this.legendWinStars = legendWinStars;
			this.legendLoseStars = legendLoseStars;
			this.resetLevels = resetLevels;
			this.rewards = rewards;
			this.buyTimeCosts = buyTimeCosts;
			this.resetTime = resetTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			specialTimes = is.popList(DuelTimeCFGS.class);
			playTimes = is.popByte();
			rewardTimes = is.popByte();
			specialLegendRewardPoints = is.popInteger();
			specialRewardPoints = is.popInteger();
			rewardPoints = is.popInteger();
			upgrade = is.popShortList();
			winStars = is.popList(DuelStarsCFGS.class);
			loseStars = is.popList(DuelStarsCFGS.class);
			legendWinStars = is.popList(DuelStarsCFGS.class);
			legendLoseStars = is.popList(DuelStarsCFGS.class);
			resetLevels = is.popShortList();
			rewards = is.popList(DuelRewardCFGS.class);
			buyTimeCosts = is.popIntegerList();
			resetTime = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(specialTimes);
			os.pushByte(playTimes);
			os.pushByte(rewardTimes);
			os.pushInteger(specialLegendRewardPoints);
			os.pushInteger(specialRewardPoints);
			os.pushInteger(rewardPoints);
			os.pushShortList(upgrade);
			os.pushList(winStars);
			os.pushList(loseStars);
			os.pushList(legendWinStars);
			os.pushList(legendLoseStars);
			os.pushShortList(resetLevels);
			os.pushList(rewards);
			os.pushIntegerList(buyTimeCosts);
			os.pushShort(resetTime);
		}

		public List<DuelTimeCFGS> specialTimes;
		public byte playTimes;
		public byte rewardTimes;
		public int specialLegendRewardPoints;
		public int specialRewardPoints;
		public int rewardPoints;
		public List<Short> upgrade;
		public List<DuelStarsCFGS> winStars;
		public List<DuelStarsCFGS> loseStars;
		public List<DuelStarsCFGS> legendWinStars;
		public List<DuelStarsCFGS> legendLoseStars;
		public List<Short> resetLevels;
		public List<DuelRewardCFGS> rewards;
		public List<Integer> buyTimeCosts;
		public short resetTime;
	}

	public static class SuraRandomGroupCFGS implements Stream.IStreamable
	{

		public SuraRandomGroupCFGS() { }

		public SuraRandomGroupCFGS(byte count, List<Short> gids)
		{
			this.count = count;
			this.gids = gids;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			count = is.popByte();
			gids = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(count);
			os.pushShortList(gids);
		}

		public byte count;
		public List<Short> gids;
	}

	public static class SuraMiniStageCFGS implements Stream.IStreamable
	{

		public SuraMiniStageCFGS() { }

		public SuraMiniStageCFGS(int rankMin, int rankMax)
		{
			this.rankMin = rankMin;
			this.rankMax = rankMax;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rankMin = is.popInteger();
			rankMax = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rankMin);
			os.pushInteger(rankMax);
		}

		public int rankMin;
		public int rankMax;
	}

	public static class SuraStageCFGS implements Stream.IStreamable
	{

		public SuraStageCFGS() { }

		public SuraStageCFGS(int money, short dropId)
		{
			this.money = money;
			this.dropId = dropId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			money = is.popInteger();
			dropId = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(money);
			os.pushShort(dropId);
		}

		public int money;
		public short dropId;
	}

	public static class SuraCFGS implements Stream.IStreamable
	{

		public SuraCFGS() { }

		public SuraCFGS(short reqLvl, List<Short> fixedGids, List<SuraRandomGroupCFGS> randomGroups, List<SuraMiniStageCFGS> miniStages, 
		                List<SuraStageCFGS> stages)
		{
			this.reqLvl = reqLvl;
			this.fixedGids = fixedGids;
			this.randomGroups = randomGroups;
			this.miniStages = miniStages;
			this.stages = stages;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			reqLvl = is.popShort();
			fixedGids = is.popShortList();
			randomGroups = is.popList(SuraRandomGroupCFGS.class);
			miniStages = is.popList(SuraMiniStageCFGS.class);
			stages = is.popList(SuraStageCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(reqLvl);
			os.pushShortList(fixedGids);
			os.pushList(randomGroups);
			os.pushList(miniStages);
			os.pushList(stages);
		}

		public short reqLvl;
		public List<Short> fixedGids;
		public List<SuraRandomGroupCFGS> randomGroups;
		public List<SuraMiniStageCFGS> miniStages;
		public List<SuraStageCFGS> stages;
	}

	public static class ForceSWarBasicCFGS implements Stream.IStreamable
	{

		public ForceSWarBasicCFGS() { }

		public ForceSWarBasicCFGS(int winScore, List<Integer> occupyScore, int badgePrice, int globalProtect)
		{
			this.winScore = winScore;
			this.occupyScore = occupyScore;
			this.badgePrice = badgePrice;
			this.globalProtect = globalProtect;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			winScore = is.popInteger();
			occupyScore = is.popIntegerList();
			badgePrice = is.popInteger();
			globalProtect = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(winScore);
			os.pushIntegerList(occupyScore);
			os.pushInteger(badgePrice);
			os.pushInteger(globalProtect);
		}

		public int winScore;
		public List<Integer> occupyScore;
		public int badgePrice;
		public int globalProtect;
	}

	public static class ForceSWarExpCFGS implements Stream.IStreamable
	{

		public ForceSWarExpCFGS() { }

		public ForceSWarExpCFGS(int attackWinExp, int attackLoseExp, int defendWinExp, int defendLoseExp)
		{
			this.attackWinExp = attackWinExp;
			this.attackLoseExp = attackLoseExp;
			this.defendWinExp = defendWinExp;
			this.defendLoseExp = defendLoseExp;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			attackWinExp = is.popInteger();
			attackLoseExp = is.popInteger();
			defendWinExp = is.popInteger();
			defendLoseExp = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(attackWinExp);
			os.pushInteger(attackLoseExp);
			os.pushInteger(defendWinExp);
			os.pushInteger(defendLoseExp);
		}

		public int attackWinExp;
		public int attackLoseExp;
		public int defendWinExp;
		public int defendLoseExp;
	}

	public static class ForceSWarLevelCFGS implements Stream.IStreamable
	{

		public ForceSWarLevelCFGS() { }

		public ForceSWarLevelCFGS(int expNeed, int badgeNeed, float winRate)
		{
			this.expNeed = expNeed;
			this.badgeNeed = badgeNeed;
			this.winRate = winRate;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			expNeed = is.popInteger();
			badgeNeed = is.popInteger();
			winRate = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(expNeed);
			os.pushInteger(badgeNeed);
			os.pushFloat(winRate);
		}

		public int expNeed;
		public int badgeNeed;
		public float winRate;
	}

	public static class ForceSWarRewardCFGS implements Stream.IStreamable
	{

		public ForceSWarRewardCFGS() { }

		public ForceSWarRewardCFGS(List<DropEntryNew> winReward, List<DropEntryNew> loseReward)
		{
			this.winReward = winReward;
			this.loseReward = loseReward;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			winReward = is.popList(DropEntryNew.class);
			loseReward = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(winReward);
			os.pushList(loseReward);
		}

		public List<DropEntryNew> winReward;
		public List<DropEntryNew> loseReward;
	}

	public static class ForceSWarBadgeItemCFGS implements Stream.IStreamable
	{

		public ForceSWarBadgeItemCFGS() { }

		public ForceSWarBadgeItemCFGS(short itemId, int badgeCount)
		{
			this.itemId = itemId;
			this.badgeCount = badgeCount;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			itemId = is.popShort();
			badgeCount = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(itemId);
			os.pushInteger(badgeCount);
		}

		public short itemId;
		public int badgeCount;
	}

	public static class ForceSWarWeakBufCFGS implements Stream.IStreamable
	{

		public ForceSWarWeakBufCFGS() { }

		public ForceSWarWeakBufCFGS(short bufId, int loseTimes)
		{
			this.bufId = bufId;
			this.loseTimes = loseTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			bufId = is.popShort();
			loseTimes = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(bufId);
			os.pushInteger(loseTimes);
		}

		public short bufId;
		public int loseTimes;
	}

	public static class ForceSWarCFGS implements Stream.IStreamable
	{

		public ForceSWarCFGS() { }

		public ForceSWarCFGS(ForceSWarBasicCFGS basic, List<ForceSWarExpCFGS> exp, List<ForceSWarLevelCFGS> level, List<ForceSWarRewardCFGS> reward, 
		                     List<ForceSWarBadgeItemCFGS> badgeItem, List<ForceSWarWeakBufCFGS> weakBuf)
		{
			this.basic = basic;
			this.exp = exp;
			this.level = level;
			this.reward = reward;
			this.badgeItem = badgeItem;
			this.weakBuf = weakBuf;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( basic == null )
				basic = new ForceSWarBasicCFGS();
			is.pop(basic);
			exp = is.popList(ForceSWarExpCFGS.class);
			level = is.popList(ForceSWarLevelCFGS.class);
			reward = is.popList(ForceSWarRewardCFGS.class);
			badgeItem = is.popList(ForceSWarBadgeItemCFGS.class);
			weakBuf = is.popList(ForceSWarWeakBufCFGS.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(basic);
			os.pushList(exp);
			os.pushList(level);
			os.pushList(reward);
			os.pushList(badgeItem);
			os.pushList(weakBuf);
		}

		public ForceSWarBasicCFGS basic;
		public List<ForceSWarExpCFGS> exp;
		public List<ForceSWarLevelCFGS> level;
		public List<ForceSWarRewardCFGS> reward;
		public List<ForceSWarBadgeItemCFGS> badgeItem;
		public List<ForceSWarWeakBufCFGS> weakBuf;
	}

	public static class DropEntry implements Stream.IStreamable
	{

		public DropEntry() { }

		public DropEntry(byte type, byte arg, short id)
		{
			this.type = type;
			this.arg = arg;
			this.id = id;
		}

		public DropEntry ksClone()
		{
			return new DropEntry(type, arg, id);
		}

		public DropEntry kdClone()
		{
			DropEntry _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			arg = is.popByte();
			id = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushByte(arg);
			os.pushShort(id);
		}

		public byte type;
		public byte arg;
		public short id;
	}

	public static class DropEntryNew implements Stream.IStreamable
	{

		public DropEntryNew() { }

		public DropEntryNew(byte type, short id, int arg)
		{
			this.type = type;
			this.id = id;
			this.arg = arg;
		}

		public DropEntryNew ksClone()
		{
			return new DropEntryNew(type, id, arg);
		}

		public DropEntryNew kdClone()
		{
			DropEntryNew _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			id = is.popShort();
			arg = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(id);
			os.pushInteger(arg);
		}

		public byte type;
		public short id;
		public int arg;
	}

	public static class CombatBonus implements Stream.IStreamable
	{

		public CombatBonus() { }

		public CombatBonus(int money, int exp, int generalExp, byte stoneTimes, 
		                   short stone, List<DropEntryNew> entryIDs)
		{
			this.money = money;
			this.exp = exp;
			this.generalExp = generalExp;
			this.stoneTimes = stoneTimes;
			this.stone = stone;
			this.entryIDs = entryIDs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			money = is.popInteger();
			exp = is.popInteger();
			generalExp = is.popInteger();
			stoneTimes = is.popByte();
			stone = is.popShort();
			entryIDs = is.popList(DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(money);
			os.pushInteger(exp);
			os.pushInteger(generalExp);
			os.pushByte(stoneTimes);
			os.pushShort(stone);
			os.pushList(entryIDs);
		}

		public int money;
		public int exp;
		public int generalExp;
		public byte stoneTimes;
		public short stone;
		public List<DropEntryNew> entryIDs;
	}

	public static class UserLoginRequest implements Stream.IStreamable
	{

		public static final byte eLoginNormal = 0;
		public static final byte eLoginReconnect = 1;
		public static final byte eLoginGod = 2;

		public UserLoginRequest() { }

		public UserLoginRequest(String userName, String deviceID, int verMajor, int verMinor, 
		                        byte loginType, String key)
		{
			this.userName = userName;
			this.deviceID = deviceID;
			this.verMajor = verMajor;
			this.verMinor = verMinor;
			this.loginType = loginType;
			this.key = key;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			userName = is.popString();
			deviceID = is.popString();
			verMajor = is.popInteger();
			verMinor = is.popInteger();
			loginType = is.popByte();
			key = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(userName);
			os.pushString(deviceID);
			os.pushInteger(verMajor);
			os.pushInteger(verMinor);
			os.pushByte(loginType);
			os.pushString(key);
		}

		public String userName;
		public String deviceID;
		public int verMajor;
		public int verMinor;
		public byte loginType;
		public String key;
	}

	public static class SetPayResult implements Stream.IStreamable
	{

		public SetPayResult() { }

		public SetPayResult(String userName, String id, float pay, byte res)
		{
			this.userName = userName;
			this.id = id;
			this.pay = pay;
			this.res = res;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			userName = is.popString();
			id = is.popString();
			pay = is.popFloat();
			res = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(userName);
			os.pushString(id);
			os.pushFloat(pay);
			os.pushByte(res);
		}

		public String userName;
		public String id;
		public float pay;
		public byte res;
	}

	public static class DBGeneralEquip implements Stream.IStreamable
	{

		public DBGeneralEquip() { }

		public DBGeneralEquip(byte lvl, short exp)
		{
			this.lvl = lvl;
			this.exp = exp;
		}

		public DBGeneralEquip ksClone()
		{
			return new DBGeneralEquip(lvl, exp);
		}

		public DBGeneralEquip kdClone()
		{
			DBGeneralEquip _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popByte();
			exp = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lvl);
			os.pushShort(exp);
		}

		public byte lvl;
		public short exp;
	}

	public static class DBGeneralSeyen implements Stream.IStreamable
	{

		public DBGeneralSeyen() { }

		public DBGeneralSeyen(short lvl, int exp, int seyenCount, int seyenTotal, 
		                      int r1, int r2)
		{
			this.lvl = lvl;
			this.exp = exp;
			this.seyenCount = seyenCount;
			this.seyenTotal = seyenTotal;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBGeneralSeyen ksClone()
		{
			return new DBGeneralSeyen(lvl, exp, seyenCount, seyenTotal, 
			                          r1, r2);
		}

		public DBGeneralSeyen kdClone()
		{
			DBGeneralSeyen _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			exp = is.popInteger();
			seyenCount = is.popInteger();
			seyenTotal = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushInteger(exp);
			os.pushInteger(seyenCount);
			os.pushInteger(seyenTotal);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short lvl;
		public int exp;
		public int seyenCount;
		public int seyenTotal;
		public int r1;
		public int r2;
	}

	public static class DBBestowLevel implements Stream.IStreamable
	{

		public DBBestowLevel() { }

		public DBBestowLevel(short lvl, short gid)
		{
			this.lvl = lvl;
			this.gid = gid;
		}

		public DBBestowLevel ksClone()
		{
			return new DBBestowLevel(lvl, gid);
		}

		public DBBestowLevel kdClone()
		{
			DBBestowLevel _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			gid = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushShort(gid);
		}

		public short lvl;
		public short gid;
	}

	public static class DBGeneralBestow implements Stream.IStreamable
	{

		public DBGeneralBestow() { }

		public DBGeneralBestow(short lvl, List<DBBestowLevel> bestowLevel, int r1, int r2)
		{
			this.lvl = lvl;
			this.bestowLevel = bestowLevel;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBGeneralBestow ksClone()
		{
			return new DBGeneralBestow(lvl, bestowLevel, r1, r2);
		}

		public DBGeneralBestow kdClone()
		{
			DBGeneralBestow _kio_clobj = ksClone();
			_kio_clobj.bestowLevel = new ArrayList<DBBestowLevel>();
			for(DBBestowLevel _kio_iter : bestowLevel)
			{
				_kio_clobj.bestowLevel.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			bestowLevel = is.popList(DBBestowLevel.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushList(bestowLevel);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short lvl;
		public List<DBBestowLevel> bestowLevel;
		public int r1;
		public int r2;
	}

	public static class DBGeneralBless implements Stream.IStreamable
	{

		public DBGeneralBless() { }

		public DBGeneralBless(short state, float p1, float p2, float p3, 
		                      int r1, int r2)
		{
			this.state = state;
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBGeneralBless ksClone()
		{
			return new DBGeneralBless(state, p1, p2, p3, 
			                          r1, r2);
		}

		public DBGeneralBless kdClone()
		{
			DBGeneralBless _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			state = is.popShort();
			p1 = is.popFloat();
			p2 = is.popFloat();
			p3 = is.popFloat();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(state);
			os.pushFloat(p1);
			os.pushFloat(p2);
			os.pushFloat(p3);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short state;
		public float p1;
		public float p2;
		public float p3;
		public int r1;
		public int r2;
	}

	public static class DBGeneralOfficial implements Stream.IStreamable
	{

		public DBGeneralOfficial() { }

		public DBGeneralOfficial(short lvl, int exp, short skillLvl, int r1, 
		                         int r2)
		{
			this.lvl = lvl;
			this.exp = exp;
			this.skillLvl = skillLvl;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBGeneralOfficial ksClone()
		{
			return new DBGeneralOfficial(lvl, exp, skillLvl, r1, 
			                             r2);
		}

		public DBGeneralOfficial kdClone()
		{
			DBGeneralOfficial _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			exp = is.popInteger();
			skillLvl = is.popShort();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushInteger(exp);
			os.pushShort(skillLvl);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short lvl;
		public int exp;
		public short skillLvl;
		public int r1;
		public int r2;
	}

	public static class DBPet implements Stream.IStreamable
	{

		public DBPet() { }

		public DBPet(short id, short lvl, int exp, byte evoLvl, 
		             int lastBreedTime, byte growthRate, byte awakeLvl, String name, 
		             byte feedGain, byte feedMode, int feedTime, DropEntry feedNeed, 
		             byte hungryTimes, int hungryDay)
		{
			this.id = id;
			this.lvl = lvl;
			this.exp = exp;
			this.evoLvl = evoLvl;
			this.lastBreedTime = lastBreedTime;
			this.growthRate = growthRate;
			this.awakeLvl = awakeLvl;
			this.name = name;
			this.feedGain = feedGain;
			this.feedMode = feedMode;
			this.feedTime = feedTime;
			this.feedNeed = feedNeed;
			this.hungryTimes = hungryTimes;
			this.hungryDay = hungryDay;
		}

		public DBPet ksClone()
		{
			return new DBPet(id, lvl, exp, evoLvl, 
			                 lastBreedTime, growthRate, awakeLvl, name, 
			                 feedGain, feedMode, feedTime, feedNeed, 
			                 hungryTimes, hungryDay);
		}

		public DBPet kdClone()
		{
			DBPet _kio_clobj = ksClone();
			_kio_clobj.feedNeed = feedNeed.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			exp = is.popInteger();
			evoLvl = is.popByte();
			lastBreedTime = is.popInteger();
			growthRate = is.popByte();
			awakeLvl = is.popByte();
			name = is.popString();
			feedGain = is.popByte();
			feedMode = is.popByte();
			feedTime = is.popInteger();
			if( feedNeed == null )
				feedNeed = new DropEntry();
			is.pop(feedNeed);
			hungryTimes = is.popByte();
			hungryDay = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushInteger(exp);
			os.pushByte(evoLvl);
			os.pushInteger(lastBreedTime);
			os.pushByte(growthRate);
			os.pushByte(awakeLvl);
			os.pushString(name);
			os.pushByte(feedGain);
			os.pushByte(feedMode);
			os.pushInteger(feedTime);
			os.push(feedNeed);
			os.pushByte(hungryTimes);
			os.pushInteger(hungryDay);
		}

		public short id;
		public short lvl;
		public int exp;
		public byte evoLvl;
		public int lastBreedTime;
		public byte growthRate;
		public byte awakeLvl;
		public String name;
		public byte feedGain;
		public byte feedMode;
		public int feedTime;
		public DropEntry feedNeed;
		public byte hungryTimes;
		public int hungryDay;
	}

	public static class DBPetDeform implements Stream.IStreamable
	{

		public DBPetDeform() { }

		public DBPetDeform(short id, byte deformStage, int happiness, short growth, 
		                   int feedTime, byte feedTimes, byte zanTimes, int zanOfferTime, 
		                   byte takeTimes, List<DropEntry> drops, byte tryTimes, byte buyTryTimes, 
		                   int totalReward, int r1, int r2)
		{
			this.id = id;
			this.deformStage = deformStage;
			this.happiness = happiness;
			this.growth = growth;
			this.feedTime = feedTime;
			this.feedTimes = feedTimes;
			this.zanTimes = zanTimes;
			this.zanOfferTime = zanOfferTime;
			this.takeTimes = takeTimes;
			this.drops = drops;
			this.tryTimes = tryTimes;
			this.buyTryTimes = buyTryTimes;
			this.totalReward = totalReward;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBPetDeform ksClone()
		{
			return new DBPetDeform(id, deformStage, happiness, growth, 
			                       feedTime, feedTimes, zanTimes, zanOfferTime, 
			                       takeTimes, drops, tryTimes, buyTryTimes, 
			                       totalReward, r1, r2);
		}

		public DBPetDeform kdClone()
		{
			DBPetDeform _kio_clobj = ksClone();
			_kio_clobj.drops = new ArrayList<DropEntry>();
			for(DropEntry _kio_iter : drops)
			{
				_kio_clobj.drops.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			deformStage = is.popByte();
			happiness = is.popInteger();
			growth = is.popShort();
			feedTime = is.popInteger();
			feedTimes = is.popByte();
			zanTimes = is.popByte();
			zanOfferTime = is.popInteger();
			takeTimes = is.popByte();
			drops = is.popList(DropEntry.class);
			tryTimes = is.popByte();
			buyTryTimes = is.popByte();
			totalReward = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(deformStage);
			os.pushInteger(happiness);
			os.pushShort(growth);
			os.pushInteger(feedTime);
			os.pushByte(feedTimes);
			os.pushByte(zanTimes);
			os.pushInteger(zanOfferTime);
			os.pushByte(takeTimes);
			os.pushList(drops);
			os.pushByte(tryTimes);
			os.pushByte(buyTryTimes);
			os.pushInteger(totalReward);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short id;
		public byte deformStage;
		public int happiness;
		public short growth;
		public int feedTime;
		public byte feedTimes;
		public byte zanTimes;
		public int zanOfferTime;
		public byte takeTimes;
		public List<DropEntry> drops;
		public byte tryTimes;
		public byte buyTryTimes;
		public int totalReward;
		public int r1;
		public int r2;
	}

	public static class DBPetZanPet implements Stream.IStreamable
	{

		public DBPetZanPet() { }

		public DBPetZanPet(short pid, short reward, short count, int time, 
		                   List<Integer> rids, int r1, int r2)
		{
			this.pid = pid;
			this.reward = reward;
			this.count = count;
			this.time = time;
			this.rids = rids;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBPetZanPet ksClone()
		{
			return new DBPetZanPet(pid, reward, count, time, 
			                       rids, r1, r2);
		}

		public DBPetZanPet kdClone()
		{
			DBPetZanPet _kio_clobj = ksClone();
			_kio_clobj.rids = new ArrayList<Integer>();
			for(Integer _kio_iter : rids)
			{
				_kio_clobj.rids.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pid = is.popShort();
			reward = is.popShort();
			count = is.popShort();
			time = is.popInteger();
			rids = is.popIntegerList();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(pid);
			os.pushShort(reward);
			os.pushShort(count);
			os.pushInteger(time);
			os.pushIntegerList(rids);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short pid;
		public short reward;
		public short count;
		public int time;
		public List<Integer> rids;
		public int r1;
		public int r2;
	}

	public static class DBPetZan implements Stream.IStreamable
	{

		public DBPetZan() { }

		public DBPetZan(int rid, short totalTimes, short maxTimes, List<DBPetZanPet> pets, 
		                byte vip, short day, byte r14, int r2)
		{
			this.rid = rid;
			this.totalTimes = totalTimes;
			this.maxTimes = maxTimes;
			this.pets = pets;
			this.vip = vip;
			this.day = day;
			this.r14 = r14;
			this.r2 = r2;
		}

		public DBPetZan ksClone()
		{
			return new DBPetZan(rid, totalTimes, maxTimes, pets, 
			                    vip, day, r14, r2);
		}

		public DBPetZan kdClone()
		{
			DBPetZan _kio_clobj = ksClone();
			_kio_clobj.pets = new ArrayList<DBPetZanPet>();
			for(DBPetZanPet _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			totalTimes = is.popShort();
			maxTimes = is.popShort();
			pets = is.popList(DBPetZanPet.class);
			vip = is.popByte();
			day = is.popShort();
			r14 = is.popByte();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushShort(totalTimes);
			os.pushShort(maxTimes);
			os.pushList(pets);
			os.pushByte(vip);
			os.pushShort(day);
			os.pushByte(r14);
			os.pushInteger(r2);
		}

		public int rid;
		public short totalTimes;
		public short maxTimes;
		public List<DBPetZanPet> pets;
		public byte vip;
		public short day;
		public byte r14;
		public int r2;
	}

	public static class DBPetBrief implements Stream.IStreamable
	{

		public DBPetBrief() { }

		public DBPetBrief(byte id, byte awakLvl, short lvl, byte evoLvl, 
		                  byte growthRate, byte deformStage, String name)
		{
			this.id = id;
			this.awakLvl = awakLvl;
			this.lvl = lvl;
			this.evoLvl = evoLvl;
			this.growthRate = growthRate;
			this.deformStage = deformStage;
			this.name = name;
		}

		public DBPetBrief ksClone()
		{
			return new DBPetBrief(id, awakLvl, lvl, evoLvl, 
			                      growthRate, deformStage, name);
		}

		public DBPetBrief kdClone()
		{
			DBPetBrief _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			awakLvl = is.popByte();
			lvl = is.popShort();
			evoLvl = is.popByte();
			growthRate = is.popByte();
			deformStage = is.popByte();
			name = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushByte(awakLvl);
			os.pushShort(lvl);
			os.pushByte(evoLvl);
			os.pushByte(growthRate);
			os.pushByte(deformStage);
			os.pushString(name);
		}

		public byte id;
		public byte awakLvl;
		public short lvl;
		public byte evoLvl;
		public byte growthRate;
		public byte deformStage;
		public String name;
	}

	public static class DBFeedingPetOld implements Stream.IStreamable
	{

		public DBFeedingPetOld() { }

		public DBFeedingPetOld(short pid, short lvlStart, int timeStart)
		{
			this.pid = pid;
			this.lvlStart = lvlStart;
			this.timeStart = timeStart;
		}

		public DBFeedingPetOld ksClone()
		{
			return new DBFeedingPetOld(pid, lvlStart, timeStart);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pid = is.popShort();
			lvlStart = is.popShort();
			timeStart = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(pid);
			os.pushShort(lvlStart);
			os.pushInteger(timeStart);
		}

		public short pid;
		public short lvlStart;
		public int timeStart;
	}

	public static class DBFeedingPet implements Stream.IStreamable
	{

		public DBFeedingPet() { }

		public DBFeedingPet(short pid, short lvlStart, int timeStart)
		{
			this.pid = pid;
			this.lvlStart = lvlStart;
			this.timeStart = timeStart;
		}

		public DBFeedingPet ksClone()
		{
			return new DBFeedingPet(pid, lvlStart, timeStart);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pid = is.popShort();
			lvlStart = is.popShort();
			timeStart = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(pid);
			os.pushShort(lvlStart);
			os.pushInteger(timeStart);
		}

		public short pid;
		public short lvlStart;
		public int timeStart;
	}

	public static class DBRoleItem implements Stream.IStreamable
	{

		public DBRoleItem() { }

		public DBRoleItem(short id, short count)
		{
			this.id = id;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			count = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(count);
		}

		public short id;
		public short count;
	}

	public static class DBRoleItem2 implements Stream.IStreamable
	{

		public DBRoleItem2() { }

		public DBRoleItem2(short id, int count)
		{
			this.id = id;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			count = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(count);
		}

		public short id;
		public int count;
	}

	public static class DBCheckinLog implements Stream.IStreamable
	{

		public DBCheckinLog() { }

		public DBCheckinLog(byte lastID, byte lastDay, byte nFinished, short reserved)
		{
			this.lastID = lastID;
			this.lastDay = lastDay;
			this.nFinished = nFinished;
			this.reserved = reserved;
		}

		public DBCheckinLog ksClone()
		{
			return new DBCheckinLog(lastID, lastDay, nFinished, reserved);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lastID = is.popByte();
			lastDay = is.popByte();
			nFinished = is.popByte();
			reserved = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(lastID);
			os.pushByte(lastDay);
			os.pushByte(nFinished);
			os.pushShort(reserved);
		}

		public byte lastID;
		public byte lastDay;
		public byte nFinished;
		public short reserved;
	}

	public static class DBItemDropLog implements Stream.IStreamable
	{

		public DBItemDropLog() { }

		public DBItemDropLog(short id, short times)
		{
			this.id = id;
			this.times = times;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			times = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(times);
		}

		public short id;
		public short times;
	}

	public static class DBTaskPos implements Stream.IStreamable
	{

		public DBTaskPos() { }

		public DBTaskPos(short groupID, short seqComp)
		{
			this.groupID = groupID;
			this.seqComp = seqComp;
		}

		public DBTaskPos ksClone()
		{
			return new DBTaskPos(groupID, seqComp);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			groupID = is.popShort();
			seqComp = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(groupID);
			os.pushShort(seqComp);
		}

		public short groupID;
		public short seqComp;
	}

	public static class DBRoleEquip implements Stream.IStreamable
	{

		public DBRoleEquip() { }

		public DBRoleEquip(short id, short count)
		{
			this.id = id;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			count = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(count);
		}

		public short id;
		public short count;
	}

	public static class DBCombatTypeScore implements Stream.IStreamable
	{

		public static final byte eNull = 0;
		public static final byte eAllComplete = 1;

		public DBCombatTypeScore() { }

		public DBCombatTypeScore(short catID, byte flag, List<Byte> score)
		{
			this.catID = catID;
			this.flag = flag;
			this.score = score;
		}

		public DBCombatTypeScore ksClone()
		{
			return new DBCombatTypeScore(catID, flag, score);
		}

		public DBCombatTypeScore kdClone()
		{
			DBCombatTypeScore _kio_clobj = ksClone();
			_kio_clobj.score = new ArrayList<Byte>();
			for(Byte _kio_iter : score)
			{
				_kio_clobj.score.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			catID = is.popShort();
			flag = is.popByte();
			score = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(catID);
			os.pushByte(flag);
			os.pushByteList(score);
		}

		public short catID;
		public byte flag;
		public List<Byte> score;
	}

	public static class DBCombatScore implements Stream.IStreamable
	{

		public DBCombatScore() { }

		public DBCombatScore(byte combatType, List<DBCombatTypeScore> scores)
		{
			this.combatType = combatType;
			this.scores = scores;
		}

		public DBCombatScore ksClone()
		{
			return new DBCombatScore(combatType, scores);
		}

		public DBCombatScore kdClone()
		{
			DBCombatScore _kio_clobj = ksClone();
			_kio_clobj.scores = new ArrayList<DBCombatTypeScore>();
			for(DBCombatTypeScore _kio_iter : scores)
			{
				_kio_clobj.scores.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatType = is.popByte();
			scores = is.popList(DBCombatTypeScore.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(combatType);
			os.pushList(scores);
		}

		public byte combatType;
		public List<DBCombatTypeScore> scores;
	}

	public static class DBCombatLog implements Stream.IStreamable
	{

		public DBCombatLog() { }

		public DBCombatLog(short id, byte times)
		{
			this.id = id;
			this.times = times;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			times = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(times);
		}

		public short id;
		public byte times;
	}

	public static class DBDailyTask2Log implements Stream.IStreamable
	{

		public DBDailyTask2Log() { }

		public DBDailyTask2Log(byte id, byte times, short reward)
		{
			this.id = id;
			this.times = times;
			this.reward = reward;
		}

		public DBDailyTask2Log ksClone()
		{
			return new DBDailyTask2Log(id, times, reward);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			times = is.popByte();
			reward = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushByte(times);
			os.pushShort(reward);
		}

		public byte id;
		public byte times;
		public short reward;
	}

	public static class DBCombatPos implements Stream.IStreamable
	{

		public DBCombatPos() { }

		public DBCombatPos(byte catIndex, byte combatIndex)
		{
			this.catIndex = catIndex;
			this.combatIndex = combatIndex;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			catIndex = is.popByte();
			combatIndex = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(catIndex);
			os.pushByte(combatIndex);
		}

		public byte catIndex;
		public byte combatIndex;
	}

	public static class DBEggLog implements Stream.IStreamable
	{

		public DBEggLog() { }

		public DBEggLog(byte eggID, byte firstEgg, byte timesToday, int lastTime, 
		                byte timesAll10, int poolPos)
		{
			this.eggID = eggID;
			this.firstEgg = firstEgg;
			this.timesToday = timesToday;
			this.lastTime = lastTime;
			this.timesAll10 = timesAll10;
			this.poolPos = poolPos;
		}

		public DBEggLog ksClone()
		{
			return new DBEggLog(eggID, firstEgg, timesToday, lastTime, 
			                    timesAll10, poolPos);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			eggID = is.popByte();
			firstEgg = is.popByte();
			timesToday = is.popByte();
			lastTime = is.popInteger();
			timesAll10 = is.popByte();
			poolPos = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(eggID);
			os.pushByte(firstEgg);
			os.pushByte(timesToday);
			os.pushInteger(lastTime);
			os.pushByte(timesAll10);
			os.pushInteger(poolPos);
		}

		public byte eggID;
		public byte firstEgg;
		public byte timesToday;
		public int lastTime;
		public byte timesAll10;
		public int poolPos;
	}

	public static class DBSoulBoxLog implements Stream.IStreamable
	{

		public DBSoulBoxLog() { }

		public DBSoulBoxLog(short boxID, short times, short lastWHPTime)
		{
			this.boxID = boxID;
			this.times = times;
			this.lastWHPTime = lastWHPTime;
		}

		public DBSoulBoxLog ksClone()
		{
			return new DBSoulBoxLog(boxID, times, lastWHPTime);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			boxID = is.popShort();
			times = is.popShort();
			lastWHPTime = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(boxID);
			os.pushShort(times);
			os.pushShort(lastWHPTime);
		}

		public short boxID;
		public short times;
		public short lastWHPTime;
	}

	public static class DBCombatEventLog implements Stream.IStreamable
	{

		public DBCombatEventLog() { }

		public DBCombatEventLog(byte id, byte timesToday, int lastTime)
		{
			this.id = id;
			this.timesToday = timesToday;
			this.lastTime = lastTime;
		}

		public DBCombatEventLog ksClone()
		{
			return new DBCombatEventLog(id, timesToday, lastTime);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			timesToday = is.popByte();
			lastTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushByte(timesToday);
			os.pushInteger(lastTime);
		}

		public byte id;
		public byte timesToday;
		public int lastTime;
	}

	public static class DBForceItemLog implements Stream.IStreamable
	{

		public DBForceItemLog() { }

		public DBForceItemLog(short id, int lastTime)
		{
			this.id = id;
			this.lastTime = lastTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lastTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(lastTime);
		}

		public short id;
		public int lastTime;
	}

	public static class DBShop implements Stream.IStreamable
	{

		public DBShop() { }

		public DBShop(byte refreshTimes, byte flag1, byte flag2, int stamp, 
		              int timeout, List<Short> ids, List<Byte> logs)
		{
			this.refreshTimes = refreshTimes;
			this.flag1 = flag1;
			this.flag2 = flag2;
			this.stamp = stamp;
			this.timeout = timeout;
			this.ids = ids;
			this.logs = logs;
		}

		public DBShop ksClone()
		{
			return new DBShop(refreshTimes, flag1, flag2, stamp, 
			                  timeout, ids, logs);
		}

		public DBShop kdClone()
		{
			DBShop _kio_clobj = ksClone();
			_kio_clobj.ids = new ArrayList<Short>();
			for(Short _kio_iter : ids)
			{
				_kio_clobj.ids.add(_kio_iter);
			}
			_kio_clobj.logs = new ArrayList<Byte>();
			for(Byte _kio_iter : logs)
			{
				_kio_clobj.logs.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			refreshTimes = is.popByte();
			flag1 = is.popByte();
			flag2 = is.popByte();
			stamp = is.popInteger();
			timeout = is.popInteger();
			ids = is.popShortList();
			logs = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(refreshTimes);
			os.pushByte(flag1);
			os.pushByte(flag2);
			os.pushInteger(stamp);
			os.pushInteger(timeout);
			os.pushShortList(ids);
			os.pushByteList(logs);
		}

		public byte refreshTimes;
		public byte flag1;
		public byte flag2;
		public int stamp;
		public int timeout;
		public List<Short> ids;
		public List<Byte> logs;
	}

	public static class DBRoleBrief implements Stream.IStreamable
	{

		public DBRoleBrief() { }

		public DBRoleBrief(int id, short lvl, short headIconID, String name, 
		                   String fname, int flag)
		{
			this.id = id;
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.name = name;
			this.fname = fname;
			this.flag = flag;
		}

		public DBRoleBrief ksClone()
		{
			return new DBRoleBrief(id, lvl, headIconID, name, 
			                       fname, flag);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			lvl = is.popShort();
			headIconID = is.popShort();
			name = is.popString();
			fname = is.popString();
			flag = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(lvl);
			os.pushShort(headIconID);
			os.pushString(name);
			os.pushString(fname);
			os.pushInteger(flag);
		}

		public int id;
		public short lvl;
		public short headIconID;
		public String name;
		public String fname;
		public int flag;
	}

	public static class DBArenaRankEntry implements Stream.IStreamable
	{

		public DBArenaRankEntry() { }

		public DBArenaRankEntry(int rank, int roleID)
		{
			this.rank = rank;
			this.roleID = roleID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rank = is.popInteger();
			roleID = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rank);
			os.pushInteger(roleID);
		}

		public int rank;
		public int roleID;
	}

	public static class DBArenaRank implements Stream.IStreamable
	{

		public DBArenaRank() { }

		public DBArenaRank(short stamp, List<DBArenaRankEntry> ranks)
		{
			this.stamp = stamp;
			this.ranks = ranks;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stamp = is.popShort();
			ranks = is.popList(DBArenaRankEntry.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(stamp);
			os.pushList(ranks);
		}

		public short stamp;
		public List<DBArenaRankEntry> ranks;
	}

	public static class DBSuperArena implements Stream.IStreamable
	{

		public DBSuperArena() { }

		public DBSuperArena(List<DBArenaRankEntry> ranks, int lastRewardTime)
		{
			this.ranks = ranks;
			this.lastRewardTime = lastRewardTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(DBArenaRankEntry.class);
			lastRewardTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
			os.pushInteger(lastRewardTime);
		}

		public List<DBArenaRankEntry> ranks;
		public int lastRewardTime;
	}

	public static class DBRoleArenaState implements Stream.IStreamable
	{

		public DBRoleArenaState() { }

		public DBRoleArenaState(int winTimes, int point, byte timesUsed, byte timesBuy, 
		                        short rewardStamp, int lastTime)
		{
			this.winTimes = winTimes;
			this.point = point;
			this.timesUsed = timesUsed;
			this.timesBuy = timesBuy;
			this.rewardStamp = rewardStamp;
			this.lastTime = lastTime;
		}

		public DBRoleArenaState ksClone()
		{
			return new DBRoleArenaState(winTimes, point, timesUsed, timesBuy, 
			                            rewardStamp, lastTime);
		}

		public DBRoleArenaState kdClone()
		{
			DBRoleArenaState _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			winTimes = is.popInteger();
			point = is.popInteger();
			timesUsed = is.popByte();
			timesBuy = is.popByte();
			rewardStamp = is.popShort();
			lastTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(winTimes);
			os.pushInteger(point);
			os.pushByte(timesUsed);
			os.pushByte(timesBuy);
			os.pushShort(rewardStamp);
			os.pushInteger(lastTime);
		}

		public int winTimes;
		public int point;
		public byte timesUsed;
		public byte timesBuy;
		public short rewardStamp;
		public int lastTime;
	}

	public static class DBRoleArenaLogOld implements Stream.IStreamable
	{

		public DBRoleArenaLogOld() { }

		public DBRoleArenaLogOld(byte win, int rankUp, int time, int id)
		{
			this.win = win;
			this.rankUp = rankUp;
			this.time = time;
			this.id = id;
		}

		public DBRoleArenaLogOld ksClone()
		{
			return new DBRoleArenaLogOld(win, rankUp, time, id);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			win = is.popByte();
			rankUp = is.popInteger();
			time = is.popInteger();
			id = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(win);
			os.pushInteger(rankUp);
			os.pushInteger(time);
			os.pushInteger(id);
		}

		public byte win;
		public int rankUp;
		public int time;
		public int id;
	}

	public static class DBRoleArenaLog implements Stream.IStreamable
	{

		public DBRoleArenaLog() { }

		public DBRoleArenaLog(byte win, int rankUp, int time, int id, 
		                      int recordId)
		{
			this.win = win;
			this.rankUp = rankUp;
			this.time = time;
			this.id = id;
			this.recordId = recordId;
		}

		public DBRoleArenaLog ksClone()
		{
			return new DBRoleArenaLog(win, rankUp, time, id, 
			                          recordId);
		}

		public DBRoleArenaLog kdClone()
		{
			DBRoleArenaLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			win = is.popByte();
			rankUp = is.popInteger();
			time = is.popInteger();
			id = is.popInteger();
			recordId = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(win);
			os.pushInteger(rankUp);
			os.pushInteger(time);
			os.pushInteger(id);
			os.pushInteger(recordId);
		}

		public byte win;
		public int rankUp;
		public int time;
		public int id;
		public int recordId;
	}

	public static class DBRoleArenaFightTeam implements Stream.IStreamable
	{

		public DBRoleArenaFightTeam() { }

		public DBRoleArenaFightTeam(List<Short> generals, List<Short> pets)
		{
			this.generals = generals;
			this.pets = pets;
		}

		public DBRoleArenaFightTeam ksClone()
		{
			return new DBRoleArenaFightTeam(generals, pets);
		}

		public DBRoleArenaFightTeam kdClone()
		{
			DBRoleArenaFightTeam _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<Short>();
			for(Short _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter);
			}
			_kio_clobj.pets = new ArrayList<Short>();
			for(Short _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generals = is.popShortList();
			pets = is.popShortList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShortList(generals);
			os.pushShortList(pets);
		}

		public List<Short> generals;
		public List<Short> pets;
	}

	public static class DBRoleSuperArenaTeam implements Stream.IStreamable
	{

		public DBRoleSuperArenaTeam() { }

		public DBRoleSuperArenaTeam(List<DBRoleArenaFightTeam> teams, int hiddenTeams, int hideTime, List<Integer> orders, 
		                            int curOrderIndex)
		{
			this.teams = teams;
			this.hiddenTeams = hiddenTeams;
			this.hideTime = hideTime;
			this.orders = orders;
			this.curOrderIndex = curOrderIndex;
		}

		public DBRoleSuperArenaTeam ksClone()
		{
			return new DBRoleSuperArenaTeam(teams, hiddenTeams, hideTime, orders, 
			                                curOrderIndex);
		}

		public DBRoleSuperArenaTeam kdClone()
		{
			DBRoleSuperArenaTeam _kio_clobj = ksClone();
			_kio_clobj.teams = new ArrayList<DBRoleArenaFightTeam>();
			for(DBRoleArenaFightTeam _kio_iter : teams)
			{
				_kio_clobj.teams.add(_kio_iter.kdClone());
			}
			_kio_clobj.orders = new ArrayList<Integer>();
			for(Integer _kio_iter : orders)
			{
				_kio_clobj.orders.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			teams = is.popList(DBRoleArenaFightTeam.class);
			hiddenTeams = is.popInteger();
			hideTime = is.popInteger();
			orders = is.popIntegerList();
			curOrderIndex = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(teams);
			os.pushInteger(hiddenTeams);
			os.pushInteger(hideTime);
			os.pushIntegerList(orders);
			os.pushInteger(curOrderIndex);
		}

		public List<DBRoleArenaFightTeam> teams;
		public int hiddenTeams;
		public int hideTime;
		public List<Integer> orders;
		public int curOrderIndex;
	}

	public static class DBRoleSuperArena implements Stream.IStreamable
	{

		public DBRoleSuperArena() { }

		public DBRoleSuperArena(DBRoleSuperArenaTeam team, List<DBRoleArenaLog> logs, int winTimes, byte timesUsed, 
		                        byte timesBuy, int lastFightTime, int point, int rankRewards, 
		                        int padding1, int padding2, int padding3, int padding4)
		{
			this.team = team;
			this.logs = logs;
			this.winTimes = winTimes;
			this.timesUsed = timesUsed;
			this.timesBuy = timesBuy;
			this.lastFightTime = lastFightTime;
			this.point = point;
			this.rankRewards = rankRewards;
			this.padding1 = padding1;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
		}

		public DBRoleSuperArena ksClone()
		{
			return new DBRoleSuperArena(team, logs, winTimes, timesUsed, 
			                            timesBuy, lastFightTime, point, rankRewards, 
			                            padding1, padding2, padding3, padding4);
		}

		public DBRoleSuperArena kdClone()
		{
			DBRoleSuperArena _kio_clobj = ksClone();
			_kio_clobj.team = team.kdClone();
			_kio_clobj.logs = new ArrayList<DBRoleArenaLog>();
			for(DBRoleArenaLog _kio_iter : logs)
			{
				_kio_clobj.logs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( team == null )
				team = new DBRoleSuperArenaTeam();
			is.pop(team);
			logs = is.popList(DBRoleArenaLog.class);
			winTimes = is.popInteger();
			timesUsed = is.popByte();
			timesBuy = is.popByte();
			lastFightTime = is.popInteger();
			point = is.popInteger();
			rankRewards = is.popInteger();
			padding1 = is.popInteger();
			padding2 = is.popInteger();
			padding3 = is.popInteger();
			padding4 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(team);
			os.pushList(logs);
			os.pushInteger(winTimes);
			os.pushByte(timesUsed);
			os.pushByte(timesBuy);
			os.pushInteger(lastFightTime);
			os.pushInteger(point);
			os.pushInteger(rankRewards);
			os.pushInteger(padding1);
			os.pushInteger(padding2);
			os.pushInteger(padding3);
			os.pushInteger(padding4);
		}

		public DBRoleSuperArenaTeam team;
		public List<DBRoleArenaLog> logs;
		public int winTimes;
		public byte timesUsed;
		public byte timesBuy;
		public int lastFightTime;
		public int point;
		public int rankRewards;
		public int padding1;
		public int padding2;
		public int padding3;
		public int padding4;
	}

	public static class DBRoleCheckinGift implements Stream.IStreamable
	{

		public DBRoleCheckinGift() { }

		public DBRoleCheckinGift(int id, int checkinDay, int lastCheckinTime)
		{
			this.id = id;
			this.checkinDay = checkinDay;
			this.lastCheckinTime = lastCheckinTime;
		}

		public DBRoleCheckinGift ksClone()
		{
			return new DBRoleCheckinGift(id, checkinDay, lastCheckinTime);
		}

		public DBRoleCheckinGift kdClone()
		{
			DBRoleCheckinGift _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			checkinDay = is.popInteger();
			lastCheckinTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(checkinDay);
			os.pushInteger(lastCheckinTime);
		}

		public int id;
		public int checkinDay;
		public int lastCheckinTime;
	}

	public static class DBRoleFirstPayGift implements Stream.IStreamable
	{

		public DBRoleFirstPayGift() { }

		public DBRoleFirstPayGift(int id, int pay, int reward)
		{
			this.id = id;
			this.pay = pay;
			this.reward = reward;
		}

		public DBRoleFirstPayGift ksClone()
		{
			return new DBRoleFirstPayGift(id, pay, reward);
		}

		public DBRoleFirstPayGift kdClone()
		{
			DBRoleFirstPayGift _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pay = is.popInteger();
			reward = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(pay);
			os.pushInteger(reward);
		}

		public int id;
		public int pay;
		public int reward;
	}

	public static class DBRolePayGift implements Stream.IStreamable
	{

		public DBRolePayGift() { }

		public DBRolePayGift(int id, int pay, List<Integer> reward)
		{
			this.id = id;
			this.pay = pay;
			this.reward = reward;
		}

		public DBRolePayGift ksClone()
		{
			return new DBRolePayGift(id, pay, reward);
		}

		public DBRolePayGift kdClone()
		{
			DBRolePayGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pay = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(pay);
			os.pushIntegerList(reward);
		}

		public int id;
		public int pay;
		public List<Integer> reward;
	}

	public static class DBRoleDiskGift implements Stream.IStreamable
	{

		public DBRoleDiskGift() { }

		public DBRoleDiskGift(int id, int pay, List<Integer> reward)
		{
			this.id = id;
			this.pay = pay;
			this.reward = reward;
		}

		public DBRoleDiskGift ksClone()
		{
			return new DBRoleDiskGift(id, pay, reward);
		}

		public DBRoleDiskGift kdClone()
		{
			DBRoleDiskGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pay = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(pay);
			os.pushIntegerList(reward);
		}

		public int id;
		public int pay;
		public List<Integer> reward;
	}

	public static class DBRoleRechargeGift implements Stream.IStreamable
	{

		public DBRoleRechargeGift() { }

		public DBRoleRechargeGift(int id, int pay, List<Integer> reward)
		{
			this.id = id;
			this.pay = pay;
			this.reward = reward;
		}

		public DBRoleRechargeGift ksClone()
		{
			return new DBRoleRechargeGift(id, pay, reward);
		}

		public DBRoleRechargeGift kdClone()
		{
			DBRoleRechargeGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pay = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(pay);
			os.pushIntegerList(reward);
		}

		public int id;
		public int pay;
		public List<Integer> reward;
	}

	public static class DBRoleConsumeGift implements Stream.IStreamable
	{

		public DBRoleConsumeGift() { }

		public DBRoleConsumeGift(int id, int consume, List<Integer> reward)
		{
			this.id = id;
			this.consume = consume;
			this.reward = reward;
		}

		public DBRoleConsumeGift ksClone()
		{
			return new DBRoleConsumeGift(id, consume, reward);
		}

		public DBRoleConsumeGift kdClone()
		{
			DBRoleConsumeGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			consume = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(consume);
			os.pushIntegerList(reward);
		}

		public int id;
		public int consume;
		public List<Integer> reward;
	}

	public static class DBRoleConsumeRebate implements Stream.IStreamable
	{

		public DBRoleConsumeRebate() { }

		public DBRoleConsumeRebate(int id, int consume, List<Integer> reward)
		{
			this.id = id;
			this.consume = consume;
			this.reward = reward;
		}

		public DBRoleConsumeRebate ksClone()
		{
			return new DBRoleConsumeRebate(id, consume, reward);
		}

		public DBRoleConsumeRebate kdClone()
		{
			DBRoleConsumeRebate _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			consume = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(consume);
			os.pushIntegerList(reward);
		}

		public int id;
		public int consume;
		public List<Integer> reward;
	}

	public static class DBRoleUpgradeGift implements Stream.IStreamable
	{

		public DBRoleUpgradeGift() { }

		public DBRoleUpgradeGift(int id, List<Integer> reward)
		{
			this.id = id;
			this.reward = reward;
		}

		public DBRoleUpgradeGift ksClone()
		{
			return new DBRoleUpgradeGift(id, reward);
		}

		public DBRoleUpgradeGift kdClone()
		{
			DBRoleUpgradeGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushIntegerList(reward);
		}

		public int id;
		public List<Integer> reward;
	}

	public static class DBRoleGatherGift implements Stream.IStreamable
	{

		public DBRoleGatherGift() { }

		public DBRoleGatherGift(int id, List<Integer> reward)
		{
			this.id = id;
			this.reward = reward;
		}

		public DBRoleGatherGift ksClone()
		{
			return new DBRoleGatherGift(id, reward);
		}

		public DBRoleGatherGift kdClone()
		{
			DBRoleGatherGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushIntegerList(reward);
		}

		public int id;
		public List<Integer> reward;
	}

	public static class DBGiftBuyLog implements Stream.IStreamable
	{

		public DBGiftBuyLog() { }

		public DBGiftBuyLog(int id, int count)
		{
			this.id = id;
			this.count = count;
		}

		public DBGiftBuyLog ksClone()
		{
			return new DBGiftBuyLog(id, count);
		}

		public DBGiftBuyLog kdClone()
		{
			DBGiftBuyLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			count = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(count);
		}

		public int id;
		public int count;
	}

	public static class DBRoleLimitedShop implements Stream.IStreamable
	{

		public DBRoleLimitedShop() { }

		public DBRoleLimitedShop(int id, List<DBGiftBuyLog> buglogs)
		{
			this.id = id;
			this.buglogs = buglogs;
		}

		public DBRoleLimitedShop ksClone()
		{
			return new DBRoleLimitedShop(id, buglogs);
		}

		public DBRoleLimitedShop kdClone()
		{
			DBRoleLimitedShop _kio_clobj = ksClone();
			_kio_clobj.buglogs = new ArrayList<DBGiftBuyLog>();
			for(DBGiftBuyLog _kio_iter : buglogs)
			{
				_kio_clobj.buglogs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			buglogs = is.popList(DBGiftBuyLog.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushList(buglogs);
		}

		public int id;
		public List<DBGiftBuyLog> buglogs;
	}

	public static class DBRoleLoginGift implements Stream.IStreamable
	{

		public DBRoleLoginGift() { }

		public DBRoleLoginGift(int id, int loginDays, int lastLoginTime, List<Integer> reward)
		{
			this.id = id;
			this.loginDays = loginDays;
			this.lastLoginTime = lastLoginTime;
			this.reward = reward;
		}

		public DBRoleLoginGift ksClone()
		{
			return new DBRoleLoginGift(id, loginDays, lastLoginTime, reward);
		}

		public DBRoleLoginGift kdClone()
		{
			DBRoleLoginGift _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			loginDays = is.popInteger();
			lastLoginTime = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(loginDays);
			os.pushInteger(lastLoginTime);
			os.pushIntegerList(reward);
		}

		public int id;
		public int loginDays;
		public int lastLoginTime;
		public List<Integer> reward;
	}

	public static class DBRoleWarGoods implements Stream.IStreamable
	{

		public DBRoleWarGoods() { }

		public DBRoleWarGoods(int type, int buyCnt, List<Integer> reward)
		{
			this.type = type;
			this.buyCnt = buyCnt;
			this.reward = reward;
		}

		public DBRoleWarGoods ksClone()
		{
			return new DBRoleWarGoods(type, buyCnt, reward);
		}

		public DBRoleWarGoods kdClone()
		{
			DBRoleWarGoods _kio_clobj = ksClone();
			_kio_clobj.reward = new ArrayList<Integer>();
			for(Integer _kio_iter : reward)
			{
				_kio_clobj.reward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popInteger();
			buyCnt = is.popInteger();
			reward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(type);
			os.pushInteger(buyCnt);
			os.pushIntegerList(reward);
		}

		public int type;
		public int buyCnt;
		public List<Integer> reward;
	}

	public static class DBRoleTradingCenter implements Stream.IStreamable
	{

		public DBRoleTradingCenter() { }

		public DBRoleTradingCenter(int id, List<DBRoleWarGoods> goods)
		{
			this.id = id;
			this.goods = goods;
		}

		public DBRoleTradingCenter ksClone()
		{
			return new DBRoleTradingCenter(id, goods);
		}

		public DBRoleTradingCenter kdClone()
		{
			DBRoleTradingCenter _kio_clobj = ksClone();
			_kio_clobj.goods = new ArrayList<DBRoleWarGoods>();
			for(DBRoleWarGoods _kio_iter : goods)
			{
				_kio_clobj.goods.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			goods = is.popList(DBRoleWarGoods.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushList(goods);
		}

		public int id;
		public List<DBRoleWarGoods> goods;
	}

	public static class FreeVit implements Stream.IStreamable
	{

		public FreeVit() { }

		public FreeVit(int startTime, int endTime, int vit)
		{
			this.startTime = startTime;
			this.endTime = endTime;
			this.vit = vit;
		}

		public FreeVit ksClone()
		{
			return new FreeVit(startTime, endTime, vit);
		}

		public FreeVit kdClone()
		{
			FreeVit _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			startTime = is.popInteger();
			endTime = is.popInteger();
			vit = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(startTime);
			os.pushInteger(endTime);
			os.pushInteger(vit);
		}

		public int startTime;
		public int endTime;
		public int vit;
	}

	public static class FriendPets implements Stream.IStreamable
	{

		public FriendPets() { }

		public FriendPets(List<DBPetBrief> petsG1, List<DBPetBrief> petsTop)
		{
			this.petsG1 = petsG1;
			this.petsTop = petsTop;
		}

		public FriendPets ksClone()
		{
			return new FriendPets(petsG1, petsTop);
		}

		public FriendPets kdClone()
		{
			FriendPets _kio_clobj = ksClone();
			_kio_clobj.petsG1 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : petsG1)
			{
				_kio_clobj.petsG1.add(_kio_iter.kdClone());
			}
			_kio_clobj.petsTop = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : petsTop)
			{
				_kio_clobj.petsTop.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			petsG1 = is.popList(DBPetBrief.class);
			petsTop = is.popList(DBPetBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(petsG1);
			os.pushList(petsTop);
		}

		public List<DBPetBrief> petsG1;
		public List<DBPetBrief> petsTop;
	}

	public static class FriendPetDeform implements Stream.IStreamable
	{

		public FriendPetDeform() { }

		public FriendPetDeform(short id, byte deformStage, int r1, int r2)
		{
			this.id = id;
			this.deformStage = deformStage;
			this.r1 = r1;
			this.r2 = r2;
		}

		public FriendPetDeform ksClone()
		{
			return new FriendPetDeform(id, deformStage, r1, r2);
		}

		public FriendPetDeform kdClone()
		{
			FriendPetDeform _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			deformStage = is.popByte();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(deformStage);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short id;
		public byte deformStage;
		public int r1;
		public int r2;
	}

	public static class FriendProp implements Stream.IStreamable
	{

		public FriendProp() { }

		public FriendProp(short lvl, int lastLoginTime, String name, List<i3k.DBRoleGeneral> arenaGenerals, 
		                  List<DBPetBrief> arenaPets, FriendPets pets, String fname, short headIconID, 
		                  byte flag, List<FriendPetDeform> petDeformsTop, List<DBRelation> relation, List<DBGeneralStone> gStones, 
		                  byte r24)
		{
			this.lvl = lvl;
			this.lastLoginTime = lastLoginTime;
			this.name = name;
			this.arenaGenerals = arenaGenerals;
			this.arenaPets = arenaPets;
			this.pets = pets;
			this.fname = fname;
			this.headIconID = headIconID;
			this.flag = flag;
			this.petDeformsTop = petDeformsTop;
			this.relation = relation;
			this.gStones = gStones;
			this.r24 = r24;
		}

		public FriendProp ksClone()
		{
			return new FriendProp(lvl, lastLoginTime, name, arenaGenerals, 
			                      arenaPets, pets, fname, headIconID, 
			                      flag, petDeformsTop, relation, gStones, 
			                      r24);
		}

		public FriendProp kdClone()
		{
			FriendProp _kio_clobj = ksClone();
			_kio_clobj.arenaGenerals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : arenaGenerals)
			{
				_kio_clobj.arenaGenerals.add(_kio_iter.kdClone());
			}
			_kio_clobj.arenaPets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : arenaPets)
			{
				_kio_clobj.arenaPets.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = pets.kdClone();
			_kio_clobj.petDeformsTop = new ArrayList<FriendPetDeform>();
			for(FriendPetDeform _kio_iter : petDeformsTop)
			{
				_kio_clobj.petDeformsTop.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation)
			{
				_kio_clobj.relation.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones)
			{
				_kio_clobj.gStones.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			lvl = is.popShort();
			lastLoginTime = is.popInteger();
			name = is.popString();
			arenaGenerals = is.popList(i3k.DBRoleGeneral.class);
			arenaPets = is.popList(DBPetBrief.class);
			if( pets == null )
				pets = new FriendPets();
			is.pop(pets);
			fname = is.popString();
			headIconID = is.popShort();
			flag = is.popByte();
			petDeformsTop = is.popList(FriendPetDeform.class);
			relation = is.popList(DBRelation.class);
			gStones = is.popList(DBGeneralStone.class);
			r24 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(lvl);
			os.pushInteger(lastLoginTime);
			os.pushString(name);
			os.pushList(arenaGenerals);
			os.pushList(arenaPets);
			os.push(pets);
			os.pushString(fname);
			os.pushShort(headIconID);
			os.pushByte(flag);
			os.pushList(petDeformsTop);
			os.pushList(relation);
			os.pushList(gStones);
			os.pushByte(r24);
		}

		public short lvl;
		public int lastLoginTime;
		public String name;
		public List<i3k.DBRoleGeneral> arenaGenerals;
		public List<DBPetBrief> arenaPets;
		public FriendPets pets;
		public String fname;
		public short headIconID;
		public byte flag;
		public List<FriendPetDeform> petDeformsTop;
		public List<DBRelation> relation;
		public List<DBGeneralStone> gStones;
		public byte r24;
	}

	public static class DBFriendData implements Stream.IStreamable
	{

		public DBFriendData() { }

		public DBFriendData(String openID, GlobalRoleID roleID, byte flag, int lastPropSyncTime, 
		                    int cool, int haogan, FriendProp prop)
		{
			this.openID = openID;
			this.roleID = roleID;
			this.flag = flag;
			this.lastPropSyncTime = lastPropSyncTime;
			this.cool = cool;
			this.haogan = haogan;
			this.prop = prop;
		}

		public DBFriendData ksClone()
		{
			return new DBFriendData(openID, roleID, flag, lastPropSyncTime, 
			                        cool, haogan, prop);
		}

		public DBFriendData kdClone()
		{
			DBFriendData _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.prop = prop.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openID = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			flag = is.popByte();
			lastPropSyncTime = is.popInteger();
			cool = is.popInteger();
			haogan = is.popInteger();
			if( prop == null )
				prop = new FriendProp();
			is.pop(prop);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(openID);
			os.push(roleID);
			os.pushByte(flag);
			os.pushInteger(lastPropSyncTime);
			os.pushInteger(cool);
			os.pushInteger(haogan);
			os.push(prop);
		}

		public String openID;
		public GlobalRoleID roleID;
		public byte flag;
		public int lastPropSyncTime;
		public int cool;
		public int haogan;
		public FriendProp prop;
	}

	public static class DBFriendSearching implements Stream.IStreamable
	{

		public DBFriendSearching() { }

		public DBFriendSearching(String openID, int lastSearchTime, int cool)
		{
			this.openID = openID;
			this.lastSearchTime = lastSearchTime;
			this.cool = cool;
		}

		public DBFriendSearching ksClone()
		{
			return new DBFriendSearching(openID, lastSearchTime, cool);
		}

		public DBFriendSearching kdClone()
		{
			DBFriendSearching _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openID = is.popString();
			lastSearchTime = is.popInteger();
			cool = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(openID);
			os.pushInteger(lastSearchTime);
			os.pushInteger(cool);
		}

		public String openID;
		public int lastSearchTime;
		public int cool;
	}

	public static class FriendBreedPet implements Stream.IStreamable
	{

		public FriendBreedPet() { }

		public FriendBreedPet(String openID, GlobalRoleID roleID, DBPetBrief pet)
		{
			this.openID = openID;
			this.roleID = roleID;
			this.pet = pet;
		}

		public FriendBreedPet ksClone()
		{
			return new FriendBreedPet(openID, roleID, pet);
		}

		public FriendBreedPet kdClone()
		{
			FriendBreedPet _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			_kio_clobj.pet = pet.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openID = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			if( pet == null )
				pet = new DBPetBrief();
			is.pop(pet);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(openID);
			os.push(roleID);
			os.push(pet);
		}

		public String openID;
		public GlobalRoleID roleID;
		public DBPetBrief pet;
	}

	public static class DBFriendMessage implements Stream.IStreamable
	{

		public DBFriendMessage() { }

		public DBFriendMessage(String openID, GlobalRoleID roleID, byte type, byte flag, 
		                       short arg1, int arg2, int time)
		{
			this.openID = openID;
			this.roleID = roleID;
			this.type = type;
			this.flag = flag;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.time = time;
		}

		public DBFriendMessage ksClone()
		{
			return new DBFriendMessage(openID, roleID, type, flag, 
			                           arg1, arg2, time);
		}

		public DBFriendMessage kdClone()
		{
			DBFriendMessage _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			openID = is.popString();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			type = is.popByte();
			flag = is.popByte();
			arg1 = is.popShort();
			arg2 = is.popInteger();
			time = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(openID);
			os.push(roleID);
			os.pushByte(type);
			os.pushByte(flag);
			os.pushShort(arg1);
			os.pushInteger(arg2);
			os.pushInteger(time);
		}

		public String openID;
		public GlobalRoleID roleID;
		public byte type;
		public byte flag;
		public short arg1;
		public int arg2;
		public int time;
	}

	public static class DBFriendApply implements Stream.IStreamable
	{

		public DBFriendApply() { }

		public DBFriendApply(int roleID, short lvl, short headIconID, String name, 
		                     String fname, int time, int r1)
		{
			this.roleID = roleID;
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.name = name;
			this.fname = fname;
			this.time = time;
			this.r1 = r1;
		}

		public DBFriendApply ksClone()
		{
			return new DBFriendApply(roleID, lvl, headIconID, name, 
			                         fname, time, r1);
		}

		public DBFriendApply kdClone()
		{
			DBFriendApply _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleID = is.popInteger();
			lvl = is.popShort();
			headIconID = is.popShort();
			name = is.popString();
			fname = is.popString();
			time = is.popInteger();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(roleID);
			os.pushShort(lvl);
			os.pushShort(headIconID);
			os.pushString(name);
			os.pushString(fname);
			os.pushInteger(time);
			os.pushInteger(r1);
		}

		public int roleID;
		public short lvl;
		public short headIconID;
		public String name;
		public String fname;
		public int time;
		public int r1;
	}

	public static class DBRoleMarchGeneralState implements Stream.IStreamable
	{

		public DBRoleMarchGeneralState() { }

		public DBRoleMarchGeneralState(short id, int hp, int mp, int state)
		{
			this.id = id;
			this.hp = hp;
			this.mp = mp;
			this.state = state;
		}

		public DBRoleMarchGeneralState ksClone()
		{
			return new DBRoleMarchGeneralState(id, hp, mp, state);
		}

		public DBRoleMarchGeneralState kdClone()
		{
			DBRoleMarchGeneralState _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			hp = is.popInteger();
			mp = is.popInteger();
			state = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(hp);
			os.pushInteger(mp);
			os.pushInteger(state);
		}

		public short id;
		public int hp;
		public int mp;
		public int state;
	}

	public static class DBRoleMarchState implements Stream.IStreamable
	{

		public DBRoleMarchState() { }

		public DBRoleMarchState(byte stage, int point, byte timesUsed, byte rewardTaken)
		{
			this.stage = stage;
			this.point = point;
			this.timesUsed = timesUsed;
			this.rewardTaken = rewardTaken;
		}

		public DBRoleMarchState ksClone()
		{
			return new DBRoleMarchState(stage, point, timesUsed, rewardTaken);
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stage = is.popByte();
			point = is.popInteger();
			timesUsed = is.popByte();
			rewardTaken = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(stage);
			os.pushInteger(point);
			os.pushByte(timesUsed);
			os.pushByte(rewardTaken);
		}

		public byte stage;
		public int point;
		public byte timesUsed;
		public byte rewardTaken;
	}

	public static class DBRoleMarchEnemy implements Stream.IStreamable
	{

		public DBRoleMarchEnemy() { }

		public DBRoleMarchEnemy(int id, short lvl, short headIconID, String name, 
		                        String fname, List<i3k.DBRoleGeneral> generals, List<DBPetBrief> pets)
		{
			this.id = id;
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.name = name;
			this.fname = fname;
			this.generals = generals;
			this.pets = pets;
		}

		public DBRoleMarchEnemy ksClone()
		{
			return new DBRoleMarchEnemy(id, lvl, headIconID, name, 
			                            fname, generals, pets);
		}

		public DBRoleMarchEnemy kdClone()
		{
			DBRoleMarchEnemy _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			lvl = is.popShort();
			headIconID = is.popShort();
			name = is.popString();
			fname = is.popString();
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(lvl);
			os.pushShort(headIconID);
			os.pushString(name);
			os.pushString(fname);
			os.pushList(generals);
			os.pushList(pets);
		}

		public int id;
		public short lvl;
		public short headIconID;
		public String name;
		public String fname;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
	}

	public static class DBWeaponProp implements Stream.IStreamable
	{

		public DBWeaponProp() { }

		public DBWeaponProp(short propId, byte type, byte rank, float value)
		{
			this.propId = propId;
			this.type = type;
			this.rank = rank;
			this.value = value;
		}

		public DBWeaponProp ksClone()
		{
			return new DBWeaponProp(propId, type, rank, value);
		}

		public DBWeaponProp kdClone()
		{
			DBWeaponProp _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			propId = is.popShort();
			type = is.popByte();
			rank = is.popByte();
			value = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(propId);
			os.pushByte(type);
			os.pushByte(rank);
			os.pushFloat(value);
		}

		public short propId;
		public byte type;
		public byte rank;
		public float value;
	}

	public static class DBWeapon implements Stream.IStreamable
	{

		public DBWeapon() { }

		public DBWeapon(short id, short lvl, byte evo, List<DBWeaponProp> passes, 
		                List<DBWeaponProp> resetPasses)
		{
			this.id = id;
			this.lvl = lvl;
			this.evo = evo;
			this.passes = passes;
			this.resetPasses = resetPasses;
		}

		public DBWeapon ksClone()
		{
			return new DBWeapon(id, lvl, evo, passes, 
			                    resetPasses);
		}

		public DBWeapon kdClone()
		{
			DBWeapon _kio_clobj = ksClone();
			_kio_clobj.passes = new ArrayList<DBWeaponProp>();
			for(DBWeaponProp _kio_iter : passes)
			{
				_kio_clobj.passes.add(_kio_iter.kdClone());
			}
			_kio_clobj.resetPasses = new ArrayList<DBWeaponProp>();
			for(DBWeaponProp _kio_iter : resetPasses)
			{
				_kio_clobj.resetPasses.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			evo = is.popByte();
			passes = is.popList(DBWeaponProp.class);
			resetPasses = is.popList(DBWeaponProp.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(evo);
			os.pushList(passes);
			os.pushList(resetPasses);
		}

		public short id;
		public short lvl;
		public byte evo;
		public List<DBWeaponProp> passes;
		public List<DBWeaponProp> resetPasses;
	}

	public static class RoleBrief implements Stream.IStreamable
	{

		public RoleBrief() { }

		public RoleBrief(int id, int day, String name, int flag, 
		                 int helpFlag1, int helpFlag2, int money, int yuanbao, 
		                 int exp, short vit, short lvl, short headIconID, 
		                 int vitRecoverStart, short skillPoint, int skillPointRecoverStart, int timeSync, 
		                 int allscore, short buyMoneyTimes, short buyVitTimes, short buySkillPointTimes)
		{
			this.id = id;
			this.day = day;
			this.name = name;
			this.flag = flag;
			this.helpFlag1 = helpFlag1;
			this.helpFlag2 = helpFlag2;
			this.money = money;
			this.yuanbao = yuanbao;
			this.exp = exp;
			this.vit = vit;
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.vitRecoverStart = vitRecoverStart;
			this.skillPoint = skillPoint;
			this.skillPointRecoverStart = skillPointRecoverStart;
			this.timeSync = timeSync;
			this.allscore = allscore;
			this.buyMoneyTimes = buyMoneyTimes;
			this.buyVitTimes = buyVitTimes;
			this.buySkillPointTimes = buySkillPointTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			day = is.popInteger();
			name = is.popString();
			flag = is.popInteger();
			helpFlag1 = is.popInteger();
			helpFlag2 = is.popInteger();
			money = is.popInteger();
			yuanbao = is.popInteger();
			exp = is.popInteger();
			vit = is.popShort();
			lvl = is.popShort();
			headIconID = is.popShort();
			vitRecoverStart = is.popInteger();
			skillPoint = is.popShort();
			skillPointRecoverStart = is.popInteger();
			timeSync = is.popInteger();
			allscore = is.popInteger();
			buyMoneyTimes = is.popShort();
			buyVitTimes = is.popShort();
			buySkillPointTimes = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(day);
			os.pushString(name);
			os.pushInteger(flag);
			os.pushInteger(helpFlag1);
			os.pushInteger(helpFlag2);
			os.pushInteger(money);
			os.pushInteger(yuanbao);
			os.pushInteger(exp);
			os.pushShort(vit);
			os.pushShort(lvl);
			os.pushShort(headIconID);
			os.pushInteger(vitRecoverStart);
			os.pushShort(skillPoint);
			os.pushInteger(skillPointRecoverStart);
			os.pushInteger(timeSync);
			os.pushInteger(allscore);
			os.pushShort(buyMoneyTimes);
			os.pushShort(buyVitTimes);
			os.pushShort(buySkillPointTimes);
		}

		public int id;
		public int day;
		public String name;
		public int flag;
		public int helpFlag1;
		public int helpFlag2;
		public int money;
		public int yuanbao;
		public int exp;
		public short vit;
		public short lvl;
		public short headIconID;
		public int vitRecoverStart;
		public short skillPoint;
		public int skillPointRecoverStart;
		public int timeSync;
		public int allscore;
		public short buyMoneyTimes;
		public short buyVitTimes;
		public short buySkillPointTimes;
	}

	public static class SkipCombatReq implements Stream.IStreamable
	{

		public SkipCombatReq() { }

		public SkipCombatReq(byte type, byte times, byte catIndex, byte combatIndex)
		{
			this.type = type;
			this.times = times;
			this.catIndex = catIndex;
			this.combatIndex = combatIndex;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			times = is.popByte();
			catIndex = is.popByte();
			combatIndex = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushByte(times);
			os.pushByte(catIndex);
			os.pushByte(combatIndex);
		}

		public byte type;
		public byte times;
		public byte catIndex;
		public byte combatIndex;
	}

	public static class PVPGeneral implements Stream.IStreamable
	{

		public PVPGeneral() { }

		public PVPGeneral(short id, byte lvl3, short horseID)
		{
			this.id = id;
			this.lvl3 = lvl3;
			this.horseID = horseID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl3 = is.popByte();
			horseID = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(lvl3);
			os.pushShort(horseID);
		}

		public short id;
		public byte lvl3;
		public short horseID;
	}

	public static class CheckFunctionStateReq implements Stream.IStreamable
	{

		public CheckFunctionStateReq() { }

		public CheckFunctionStateReq(byte countNeed, List<Byte> funcs)
		{
			this.countNeed = countNeed;
			this.funcs = funcs;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			countNeed = is.popByte();
			funcs = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(countNeed);
			os.pushByteList(funcs);
		}

		public byte countNeed;
		public List<Byte> funcs;
	}

	public static class ClearFunctionCoolReq implements Stream.IStreamable
	{

		public ClearFunctionCoolReq() { }

		public ClearFunctionCoolReq(byte funcID, short stone)
		{
			this.funcID = funcID;
			this.stone = stone;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			funcID = is.popByte();
			stone = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(funcID);
			os.pushShort(stone);
		}

		public byte funcID;
		public short stone;
	}

	public static class CheckFunctionStateRes implements Stream.IStreamable
	{

		public CheckFunctionStateRes() { }

		public CheckFunctionStateRes(List<Byte> funcs, List<Byte> states)
		{
			this.funcs = funcs;
			this.states = states;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			funcs = is.popByteList();
			states = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByteList(funcs);
			os.pushByteList(states);
		}

		public List<Byte> funcs;
		public List<Byte> states;
	}

	public static class RPCRes implements Stream.IStreamable
	{

		public static final byte eOK = 0;
		public static final byte eFailed = 1;
		public static final byte eCool = 2;
		public static final byte eVit = 4;
		public static final byte eYuanbao = 5;
		public static final byte eMoney = 6;
		public static final byte eMaxTimes = 7;
		public static final byte eLevel = 8;
		public static final byte eRoleType = 11;
		public static final byte eNotFound = 30;

		public RPCRes() { }

		public RPCRes(byte ret)
		{
			this.ret = ret;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ret = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(ret);
		}

		public byte ret;
	}

	public static class DataModifyReq implements Stream.IStreamable
	{

		public static final byte eAdd = 1;
		public static final byte eSet = 2;
		public static final byte eStone = 1;
		public static final byte eMoney = 2;
		public static final byte eItem = 3;
		public static final byte eEquip = 4;
		public static final byte eGeneral = 5;
		public static final byte eLevel = 6;
		public static final byte eForceScore = 8;
		public static final byte eForceID = 9;
		public static final byte eSeyen = 10;

		public DataModifyReq() { }

		public DataModifyReq(int gm, int rid, byte mtype, byte dtype, 
		                     byte mail, short id, int val)
		{
			this.gm = gm;
			this.rid = rid;
			this.mtype = mtype;
			this.dtype = dtype;
			this.mail = mail;
			this.id = id;
			this.val = val;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gm = is.popInteger();
			rid = is.popInteger();
			mtype = is.popByte();
			dtype = is.popByte();
			mail = is.popByte();
			id = is.popShort();
			val = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gm);
			os.pushInteger(rid);
			os.pushByte(mtype);
			os.pushByte(dtype);
			os.pushByte(mail);
			os.pushShort(id);
			os.pushInteger(val);
		}

		public int gm;
		public int rid;
		public byte mtype;
		public byte dtype;
		public byte mail;
		public short id;
		public int val;
	}

	public static class DataModifyRes implements Stream.IStreamable
	{

		public static final byte eOK = 0;
		public static final byte eFailed = 1;
		public static final byte eBusy = 2;

		public DataModifyRes() { }

		public DataModifyRes(byte res, DataModifyReq req, int oldVal, int newVal)
		{
			this.res = res;
			this.req = req;
			this.oldVal = oldVal;
			this.newVal = newVal;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			res = is.popByte();
			if( req == null )
				req = new DataModifyReq();
			is.pop(req);
			oldVal = is.popInteger();
			newVal = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(res);
			os.push(req);
			os.pushInteger(oldVal);
			os.pushInteger(newVal);
		}

		public byte res;
		public DataModifyReq req;
		public int oldVal;
		public int newVal;
	}

	public static class DataQueryReq implements Stream.IStreamable
	{

		public static final byte eOnlineUser = 1;
		public static final byte eRoleBrief = 2;
		public static final byte eRoleID = 3;
		public static final byte eSendMail = 4;

		public DataQueryReq() { }

		public DataQueryReq(byte qtype, int iArg1, int iArg2, String sArg1, 
		                    String sArg2)
		{
			this.qtype = qtype;
			this.iArg1 = iArg1;
			this.iArg2 = iArg2;
			this.sArg1 = sArg1;
			this.sArg2 = sArg2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			qtype = is.popByte();
			iArg1 = is.popInteger();
			iArg2 = is.popInteger();
			sArg1 = is.popString();
			sArg2 = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(qtype);
			os.pushInteger(iArg1);
			os.pushInteger(iArg2);
			os.pushString(sArg1);
			os.pushString(sArg2);
		}

		public byte qtype;
		public int iArg1;
		public int iArg2;
		public String sArg1;
		public String sArg2;
	}

	public static class DataQueryRes implements Stream.IStreamable
	{

		public static final byte eOK = 0;
		public static final byte eFailed = 1;
		public static final byte eBusy = 2;
		public static final byte eNotFound = 3;

		public DataQueryRes() { }

		public DataQueryRes(byte res, DataQueryReq req, int iRes1, int iRes2, 
		                    String sRes1, String sRes2)
		{
			this.res = res;
			this.req = req;
			this.iRes1 = iRes1;
			this.iRes2 = iRes2;
			this.sRes1 = sRes1;
			this.sRes2 = sRes2;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			res = is.popByte();
			if( req == null )
				req = new DataQueryReq();
			is.pop(req);
			iRes1 = is.popInteger();
			iRes2 = is.popInteger();
			sRes1 = is.popString();
			sRes2 = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(res);
			os.push(req);
			os.pushInteger(iRes1);
			os.pushInteger(iRes2);
			os.pushString(sRes1);
			os.pushString(sRes2);
		}

		public byte res;
		public DataQueryReq req;
		public int iRes1;
		public int iRes2;
		public String sRes1;
		public String sRes2;
	}

	public static class RoleDataReq implements Stream.IStreamable
	{

		public static final int eBrief = 1;
		public static final int eGeneral = 2;
		public static final int eEquip = 4;
		public static final int eItem = 8;
		public static final int eArenaGeneral = 16;
		public static final int eSuperArenaTeam = 32;

		public RoleDataReq() { }

		public RoleDataReq(int qtype, int id)
		{
			this.qtype = qtype;
			this.id = id;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			qtype = is.popInteger();
			id = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(qtype);
			os.pushInteger(id);
		}

		public int qtype;
		public int id;
	}

	public static class RoleDataBrief implements Stream.IStreamable
	{

		public RoleDataBrief() { }

		public RoleDataBrief(int id, String name, String uname, String fname, 
		                     short lvl, int money, int stone, int gcount, 
		                     int icount, int ecount, short headIconID, int arenaPower, 
		                     int arenaWinTimes)
		{
			this.id = id;
			this.name = name;
			this.uname = uname;
			this.fname = fname;
			this.lvl = lvl;
			this.money = money;
			this.stone = stone;
			this.gcount = gcount;
			this.icount = icount;
			this.ecount = ecount;
			this.headIconID = headIconID;
			this.arenaPower = arenaPower;
			this.arenaWinTimes = arenaWinTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			uname = is.popString();
			fname = is.popString();
			lvl = is.popShort();
			money = is.popInteger();
			stone = is.popInteger();
			gcount = is.popInteger();
			icount = is.popInteger();
			ecount = is.popInteger();
			headIconID = is.popShort();
			arenaPower = is.popInteger();
			arenaWinTimes = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushString(uname);
			os.pushString(fname);
			os.pushShort(lvl);
			os.pushInteger(money);
			os.pushInteger(stone);
			os.pushInteger(gcount);
			os.pushInteger(icount);
			os.pushInteger(ecount);
			os.pushShort(headIconID);
			os.pushInteger(arenaPower);
			os.pushInteger(arenaWinTimes);
		}

		public int id;
		public String name;
		public String uname;
		public String fname;
		public short lvl;
		public int money;
		public int stone;
		public int gcount;
		public int icount;
		public int ecount;
		public short headIconID;
		public int arenaPower;
		public int arenaWinTimes;
	}

	public static class RoleDataGeneral implements Stream.IStreamable
	{

		public RoleDataGeneral() { }

		public RoleDataGeneral(short id, short lvl, byte advLvl, byte evoLvl, 
		                       short weaponLvl, byte weaponEvo)
		{
			this.id = id;
			this.lvl = lvl;
			this.advLvl = advLvl;
			this.evoLvl = evoLvl;
			this.weaponLvl = weaponLvl;
			this.weaponEvo = weaponEvo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			advLvl = is.popByte();
			evoLvl = is.popByte();
			weaponLvl = is.popShort();
			weaponEvo = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(advLvl);
			os.pushByte(evoLvl);
			os.pushShort(weaponLvl);
			os.pushByte(weaponEvo);
		}

		public short id;
		public short lvl;
		public byte advLvl;
		public byte evoLvl;
		public short weaponLvl;
		public byte weaponEvo;
	}

	public static class RoleDataPet implements Stream.IStreamable
	{

		public RoleDataPet() { }

		public RoleDataPet(short id, short lvl, byte evoLvl, short growthRate, 
		                   String name)
		{
			this.id = id;
			this.lvl = lvl;
			this.evoLvl = evoLvl;
			this.growthRate = growthRate;
			this.name = name;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			evoLvl = is.popByte();
			growthRate = is.popShort();
			name = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(evoLvl);
			os.pushShort(growthRate);
			os.pushString(name);
		}

		public short id;
		public short lvl;
		public byte evoLvl;
		public short growthRate;
		public String name;
	}

	public static class RoleDataFightTeam implements Stream.IStreamable
	{

		public RoleDataFightTeam() { }

		public RoleDataFightTeam(List<RoleDataGeneral> generals, List<RoleDataPet> pets)
		{
			this.generals = generals;
			this.pets = pets;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generals = is.popList(RoleDataGeneral.class);
			pets = is.popList(RoleDataPet.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(generals);
			os.pushList(pets);
		}

		public List<RoleDataGeneral> generals;
		public List<RoleDataPet> pets;
	}

	public static class RoleDataSuperArena implements Stream.IStreamable
	{

		public RoleDataSuperArena() { }

		public RoleDataSuperArena(List<RoleDataFightTeam> teams, int winTimes)
		{
			this.teams = teams;
			this.winTimes = winTimes;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			teams = is.popList(RoleDataFightTeam.class);
			winTimes = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(teams);
			os.pushInteger(winTimes);
		}

		public List<RoleDataFightTeam> teams;
		public int winTimes;
	}

	public static class RoleDataRes implements Stream.IStreamable
	{

		public static final byte eOK = 0;
		public static final byte eFailed = 1;
		public static final byte eBusy = 2;

		public RoleDataRes() { }

		public RoleDataRes(RoleDataReq req, byte res, RoleDataBrief brief, List<RoleDataGeneral> generals, 
		                   List<DBRoleEquip> equips, List<DBRoleItem> items, List<RoleDataGeneral> arenagenerals, List<RoleDataPet> arenapets, 
		                   RoleDataSuperArena superArena)
		{
			this.req = req;
			this.res = res;
			this.brief = brief;
			this.generals = generals;
			this.equips = equips;
			this.items = items;
			this.arenagenerals = arenagenerals;
			this.arenapets = arenapets;
			this.superArena = superArena;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( req == null )
				req = new RoleDataReq();
			is.pop(req);
			res = is.popByte();
			if( brief == null )
				brief = new RoleDataBrief();
			is.pop(brief);
			generals = is.popList(RoleDataGeneral.class);
			equips = is.popList(DBRoleEquip.class);
			items = is.popList(DBRoleItem.class);
			arenagenerals = is.popList(RoleDataGeneral.class);
			arenapets = is.popList(RoleDataPet.class);
			if( superArena == null )
				superArena = new RoleDataSuperArena();
			is.pop(superArena);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(req);
			os.pushByte(res);
			os.push(brief);
			os.pushList(generals);
			os.pushList(equips);
			os.pushList(items);
			os.pushList(arenagenerals);
			os.pushList(arenapets);
			os.push(superArena);
		}

		public RoleDataReq req;
		public byte res;
		public RoleDataBrief brief;
		public List<RoleDataGeneral> generals;
		public List<DBRoleEquip> equips;
		public List<DBRoleItem> items;
		public List<RoleDataGeneral> arenagenerals;
		public List<RoleDataPet> arenapets;
		public RoleDataSuperArena superArena;
	}

	public static class DBPowerRank implements Stream.IStreamable
	{

		public DBPowerRank() { }

		public DBPowerRank(int roleid, int power, List<Short> generals, short pets, 
		                   int updateTime)
		{
			this.roleid = roleid;
			this.power = power;
			this.generals = generals;
			this.pets = pets;
			this.updateTime = updateTime;
		}

		public DBPowerRank ksClone()
		{
			return new DBPowerRank(roleid, power, generals, pets, 
			                       updateTime);
		}

		public DBPowerRank kdClone()
		{
			DBPowerRank _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<Short>();
			for(Short _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleid = is.popInteger();
			power = is.popInteger();
			generals = is.popShortList();
			pets = is.popShort();
			updateTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(roleid);
			os.pushInteger(power);
			os.pushShortList(generals);
			os.pushShort(pets);
			os.pushInteger(updateTime);
		}

		public int roleid;
		public int power;
		public List<Short> generals;
		public short pets;
		public int updateTime;
	}

	public static class DBPowerRankList implements Stream.IStreamable
	{

		public DBPowerRankList() { }

		public DBPowerRankList(List<DBPowerRank> ranks)
		{
			this.ranks = ranks;
		}

		public DBPowerRankList ksClone()
		{
			return new DBPowerRankList(ranks);
		}

		public DBPowerRankList kdClone()
		{
			DBPowerRankList _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<DBPowerRank>();
			for(DBPowerRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(DBPowerRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
		}

		public List<DBPowerRank> ranks;
	}

	public static class DBForceWarRolePlayerOld implements Stream.IStreamable
	{

		public DBForceWarRolePlayerOld() { }

		public DBForceWarRolePlayerOld(int id, String name, short icon, short level, 
		                               int power, List<i3k.DBRoleGeneral> generals, List<DBPetBrief> pets, List<Integer> plusItems)
		{
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.level = level;
			this.power = power;
			this.generals = generals;
			this.pets = pets;
			this.plusItems = plusItems;
		}

		public DBForceWarRolePlayerOld ksClone()
		{
			return new DBForceWarRolePlayerOld(id, name, icon, level, 
			                                   power, generals, pets, plusItems);
		}

		public DBForceWarRolePlayerOld kdClone()
		{
			DBForceWarRolePlayerOld _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			_kio_clobj.plusItems = new ArrayList<Integer>();
			for(Integer _kio_iter : plusItems)
			{
				_kio_clobj.plusItems.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			level = is.popShort();
			power = is.popInteger();
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
			plusItems = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(level);
			os.pushInteger(power);
			os.pushList(generals);
			os.pushList(pets);
			os.pushIntegerList(plusItems);
		}

		public int id;
		public String name;
		public short icon;
		public short level;
		public int power;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
		public List<Integer> plusItems;
	}

	public static class DBForceWarRolePlayer implements Stream.IStreamable
	{

		public DBForceWarRolePlayer() { }

		public DBForceWarRolePlayer(int id, String name, short icon, short level, 
		                            int power, List<i3k.DBRoleGeneral> generals, List<DBPetBrief> pets, List<DBRelation> relation, 
		                            List<Integer> plusItems)
		{
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.level = level;
			this.power = power;
			this.generals = generals;
			this.pets = pets;
			this.relation = relation;
			this.plusItems = plusItems;
		}

		public DBForceWarRolePlayer ksClone()
		{
			return new DBForceWarRolePlayer(id, name, icon, level, 
			                                power, generals, pets, relation, 
			                                plusItems);
		}

		public DBForceWarRolePlayer kdClone()
		{
			DBForceWarRolePlayer _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation)
			{
				_kio_clobj.relation.add(_kio_iter.kdClone());
			}
			_kio_clobj.plusItems = new ArrayList<Integer>();
			for(Integer _kio_iter : plusItems)
			{
				_kio_clobj.plusItems.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			level = is.popShort();
			power = is.popInteger();
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
			relation = is.popList(DBRelation.class);
			plusItems = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(level);
			os.pushInteger(power);
			os.pushList(generals);
			os.pushList(pets);
			os.pushList(relation);
			os.pushIntegerList(plusItems);
		}

		public int id;
		public String name;
		public short icon;
		public short level;
		public int power;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
		public List<DBRelation> relation;
		public List<Integer> plusItems;
	}

	public static class DBForceWarForcePlayerOld implements Stream.IStreamable
	{

		public DBForceWarForcePlayerOld() { }

		public DBForceWarForcePlayerOld(int id, String name, short icon, short level, 
		                                int headId, String headName, List<Integer> froles, List<DBForceWarRolePlayerOld> players, 
		                                List<Integer> plusItems)
		{
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.level = level;
			this.headId = headId;
			this.headName = headName;
			this.froles = froles;
			this.players = players;
			this.plusItems = plusItems;
		}

		public DBForceWarForcePlayerOld ksClone()
		{
			return new DBForceWarForcePlayerOld(id, name, icon, level, 
			                                    headId, headName, froles, players, 
			                                    plusItems);
		}

		public DBForceWarForcePlayerOld kdClone()
		{
			DBForceWarForcePlayerOld _kio_clobj = ksClone();
			_kio_clobj.froles = new ArrayList<Integer>();
			for(Integer _kio_iter : froles)
			{
				_kio_clobj.froles.add(_kio_iter);
			}
			_kio_clobj.players = new ArrayList<DBForceWarRolePlayerOld>();
			for(DBForceWarRolePlayerOld _kio_iter : players)
			{
				_kio_clobj.players.add(_kio_iter.kdClone());
			}
			_kio_clobj.plusItems = new ArrayList<Integer>();
			for(Integer _kio_iter : plusItems)
			{
				_kio_clobj.plusItems.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			level = is.popShort();
			headId = is.popInteger();
			headName = is.popString();
			froles = is.popIntegerList();
			players = is.popList(DBForceWarRolePlayerOld.class);
			plusItems = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(level);
			os.pushInteger(headId);
			os.pushString(headName);
			os.pushIntegerList(froles);
			os.pushList(players);
			os.pushIntegerList(plusItems);
		}

		public int id;
		public String name;
		public short icon;
		public short level;
		public int headId;
		public String headName;
		public List<Integer> froles;
		public List<DBForceWarRolePlayerOld> players;
		public List<Integer> plusItems;
	}

	public static class DBForceWarForcePlayer implements Stream.IStreamable
	{

		public DBForceWarForcePlayer() { }

		public DBForceWarForcePlayer(int id, String name, short icon, short level, 
		                             int headId, String headName, List<Integer> froles, List<DBForceWarRolePlayer> players, 
		                             List<Integer> plusItems)
		{
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.level = level;
			this.headId = headId;
			this.headName = headName;
			this.froles = froles;
			this.players = players;
			this.plusItems = plusItems;
		}

		public DBForceWarForcePlayer ksClone()
		{
			return new DBForceWarForcePlayer(id, name, icon, level, 
			                                 headId, headName, froles, players, 
			                                 plusItems);
		}

		public DBForceWarForcePlayer kdClone()
		{
			DBForceWarForcePlayer _kio_clobj = ksClone();
			_kio_clobj.froles = new ArrayList<Integer>();
			for(Integer _kio_iter : froles)
			{
				_kio_clobj.froles.add(_kio_iter);
			}
			_kio_clobj.players = new ArrayList<DBForceWarRolePlayer>();
			for(DBForceWarRolePlayer _kio_iter : players)
			{
				_kio_clobj.players.add(_kio_iter.kdClone());
			}
			_kio_clobj.plusItems = new ArrayList<Integer>();
			for(Integer _kio_iter : plusItems)
			{
				_kio_clobj.plusItems.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			level = is.popShort();
			headId = is.popInteger();
			headName = is.popString();
			froles = is.popIntegerList();
			players = is.popList(DBForceWarRolePlayer.class);
			plusItems = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(level);
			os.pushInteger(headId);
			os.pushString(headName);
			os.pushIntegerList(froles);
			os.pushList(players);
			os.pushIntegerList(plusItems);
		}

		public int id;
		public String name;
		public short icon;
		public short level;
		public int headId;
		public String headName;
		public List<Integer> froles;
		public List<DBForceWarRolePlayer> players;
		public List<Integer> plusItems;
	}

	public static class DBForceWarGeneralStatus implements Stream.IStreamable
	{

		public DBForceWarGeneralStatus() { }

		public DBForceWarGeneralStatus(short id, int hp, int mp, int state)
		{
			this.id = id;
			this.hp = hp;
			this.mp = mp;
			this.state = state;
		}

		public DBForceWarGeneralStatus ksClone()
		{
			return new DBForceWarGeneralStatus(id, hp, mp, state);
		}

		public DBForceWarGeneralStatus kdClone()
		{
			DBForceWarGeneralStatus _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			hp = is.popInteger();
			mp = is.popInteger();
			state = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(hp);
			os.pushInteger(mp);
			os.pushInteger(state);
		}

		public short id;
		public int hp;
		public int mp;
		public int state;
	}

	public static class DBForceWarMatchRecord implements Stream.IStreamable
	{

		public DBForceWarMatchRecord() { }

		public DBForceWarMatchRecord(String name1, String name2, byte index1, byte index2, 
		                             byte result, List<i3k.DBRoleGeneral> generals1, List<i3k.DBRoleGeneral> generals2, List<DBPetBrief> pets1, 
		                             List<DBPetBrief> pets2, List<DBForceWarGeneralStatus> status1, List<DBForceWarGeneralStatus> status2, List<Byte> eff1, 
		                             List<Byte> eff2, List<DBRelation> relation1, List<DBRelation> relation2, int seed)
		{
			this.name1 = name1;
			this.name2 = name2;
			this.index1 = index1;
			this.index2 = index2;
			this.result = result;
			this.generals1 = generals1;
			this.generals2 = generals2;
			this.pets1 = pets1;
			this.pets2 = pets2;
			this.status1 = status1;
			this.status2 = status2;
			this.eff1 = eff1;
			this.eff2 = eff2;
			this.relation1 = relation1;
			this.relation2 = relation2;
			this.seed = seed;
		}

		public DBForceWarMatchRecord ksClone()
		{
			return new DBForceWarMatchRecord(name1, name2, index1, index2, 
			                                 result, generals1, generals2, pets1, 
			                                 pets2, status1, status2, eff1, 
			                                 eff2, relation1, relation2, seed);
		}

		public DBForceWarMatchRecord kdClone()
		{
			DBForceWarMatchRecord _kio_clobj = ksClone();
			_kio_clobj.generals1 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals1)
			{
				_kio_clobj.generals1.add(_kio_iter.kdClone());
			}
			_kio_clobj.generals2 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals2)
			{
				_kio_clobj.generals2.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets1 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets1)
			{
				_kio_clobj.pets1.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets2 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets2)
			{
				_kio_clobj.pets2.add(_kio_iter.kdClone());
			}
			_kio_clobj.status1 = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : status1)
			{
				_kio_clobj.status1.add(_kio_iter.kdClone());
			}
			_kio_clobj.status2 = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : status2)
			{
				_kio_clobj.status2.add(_kio_iter.kdClone());
			}
			_kio_clobj.eff1 = new ArrayList<Byte>();
			for(Byte _kio_iter : eff1)
			{
				_kio_clobj.eff1.add(_kio_iter);
			}
			_kio_clobj.eff2 = new ArrayList<Byte>();
			for(Byte _kio_iter : eff2)
			{
				_kio_clobj.eff2.add(_kio_iter);
			}
			_kio_clobj.relation1 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation1)
			{
				_kio_clobj.relation1.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation2 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation2)
			{
				_kio_clobj.relation2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			name1 = is.popString();
			name2 = is.popString();
			index1 = is.popByte();
			index2 = is.popByte();
			result = is.popByte();
			generals1 = is.popList(i3k.DBRoleGeneral.class);
			generals2 = is.popList(i3k.DBRoleGeneral.class);
			pets1 = is.popList(DBPetBrief.class);
			pets2 = is.popList(DBPetBrief.class);
			status1 = is.popList(DBForceWarGeneralStatus.class);
			status2 = is.popList(DBForceWarGeneralStatus.class);
			eff1 = is.popByteList();
			eff2 = is.popByteList();
			relation1 = is.popList(DBRelation.class);
			relation2 = is.popList(DBRelation.class);
			seed = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(name1);
			os.pushString(name2);
			os.pushByte(index1);
			os.pushByte(index2);
			os.pushByte(result);
			os.pushList(generals1);
			os.pushList(generals2);
			os.pushList(pets1);
			os.pushList(pets2);
			os.pushList(status1);
			os.pushList(status2);
			os.pushByteList(eff1);
			os.pushByteList(eff2);
			os.pushList(relation1);
			os.pushList(relation2);
			os.pushInteger(seed);
		}

		public String name1;
		public String name2;
		public byte index1;
		public byte index2;
		public byte result;
		public List<i3k.DBRoleGeneral> generals1;
		public List<i3k.DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<DBForceWarGeneralStatus> status1;
		public List<DBForceWarGeneralStatus> status2;
		public List<Byte> eff1;
		public List<Byte> eff2;
		public List<DBRelation> relation1;
		public List<DBRelation> relation2;
		public int seed;
	}

	public static class DBForceWarMatchRecordBrief implements Stream.IStreamable
	{

		public DBForceWarMatchRecordBrief() { }

		public DBForceWarMatchRecordBrief(byte index1, byte index2, byte result, List<DBForceWarGeneralStatus> status1, 
		                                  List<DBForceWarGeneralStatus> status2, int seed)
		{
			this.index1 = index1;
			this.index2 = index2;
			this.result = result;
			this.status1 = status1;
			this.status2 = status2;
			this.seed = seed;
		}

		public DBForceWarMatchRecordBrief ksClone()
		{
			return new DBForceWarMatchRecordBrief(index1, index2, result, status1, 
			                                      status2, seed);
		}

		public DBForceWarMatchRecordBrief kdClone()
		{
			DBForceWarMatchRecordBrief _kio_clobj = ksClone();
			_kio_clobj.status1 = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : status1)
			{
				_kio_clobj.status1.add(_kio_iter.kdClone());
			}
			_kio_clobj.status2 = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : status2)
			{
				_kio_clobj.status2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			index1 = is.popByte();
			index2 = is.popByte();
			result = is.popByte();
			status1 = is.popList(DBForceWarGeneralStatus.class);
			status2 = is.popList(DBForceWarGeneralStatus.class);
			seed = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(index1);
			os.pushByte(index2);
			os.pushByte(result);
			os.pushList(status1);
			os.pushList(status2);
			os.pushInteger(seed);
		}

		public byte index1;
		public byte index2;
		public byte result;
		public List<DBForceWarGeneralStatus> status1;
		public List<DBForceWarGeneralStatus> status2;
		public int seed;
	}

	public static class DBForceWarRecord implements Stream.IStreamable
	{

		public DBForceWarRecord() { }

		public DBForceWarRecord(int fid1, int fid2, String fname1, String fname2, 
		                        byte result, List<DBForceWarMatchRecordBrief> matches)
		{
			this.fid1 = fid1;
			this.fid2 = fid2;
			this.fname1 = fname1;
			this.fname2 = fname2;
			this.result = result;
			this.matches = matches;
		}

		public DBForceWarRecord ksClone()
		{
			return new DBForceWarRecord(fid1, fid2, fname1, fname2, 
			                            result, matches);
		}

		public DBForceWarRecord kdClone()
		{
			DBForceWarRecord _kio_clobj = ksClone();
			_kio_clobj.matches = new ArrayList<DBForceWarMatchRecordBrief>();
			for(DBForceWarMatchRecordBrief _kio_iter : matches)
			{
				_kio_clobj.matches.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid1 = is.popInteger();
			fid2 = is.popInteger();
			fname1 = is.popString();
			fname2 = is.popString();
			result = is.popByte();
			matches = is.popList(DBForceWarMatchRecordBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid1);
			os.pushInteger(fid2);
			os.pushString(fname1);
			os.pushString(fname2);
			os.pushByte(result);
			os.pushList(matches);
		}

		public int fid1;
		public int fid2;
		public String fname1;
		public String fname2;
		public byte result;
		public List<DBForceWarMatchRecordBrief> matches;
	}

	public static class DBForceWarRoleStatus implements Stream.IStreamable
	{

		public DBForceWarRoleStatus() { }

		public DBForceWarRoleStatus(int rid, List<DBForceWarGeneralStatus> generalStatus)
		{
			this.rid = rid;
			this.generalStatus = generalStatus;
		}

		public DBForceWarRoleStatus ksClone()
		{
			return new DBForceWarRoleStatus(rid, generalStatus);
		}

		public DBForceWarRoleStatus kdClone()
		{
			DBForceWarRoleStatus _kio_clobj = ksClone();
			_kio_clobj.generalStatus = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : generalStatus)
			{
				_kio_clobj.generalStatus.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			generalStatus = is.popList(DBForceWarGeneralStatus.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushList(generalStatus);
		}

		public int rid;
		public List<DBForceWarGeneralStatus> generalStatus;
	}

	public static class DBForceWarStatus implements Stream.IStreamable
	{

		public DBForceWarStatus() { }

		public DBForceWarStatus(int fid, int oppoFid, byte warIndex, List<Integer> scores, 
		                        short leagueSeqIndex, List<DBForceWarRoleStatus> roleStatus)
		{
			this.fid = fid;
			this.oppoFid = oppoFid;
			this.warIndex = warIndex;
			this.scores = scores;
			this.leagueSeqIndex = leagueSeqIndex;
			this.roleStatus = roleStatus;
		}

		public DBForceWarStatus ksClone()
		{
			return new DBForceWarStatus(fid, oppoFid, warIndex, scores, 
			                            leagueSeqIndex, roleStatus);
		}

		public DBForceWarStatus kdClone()
		{
			DBForceWarStatus _kio_clobj = ksClone();
			_kio_clobj.scores = new ArrayList<Integer>();
			for(Integer _kio_iter : scores)
			{
				_kio_clobj.scores.add(_kio_iter);
			}
			_kio_clobj.roleStatus = new ArrayList<DBForceWarRoleStatus>();
			for(DBForceWarRoleStatus _kio_iter : roleStatus)
			{
				_kio_clobj.roleStatus.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			oppoFid = is.popInteger();
			warIndex = is.popByte();
			scores = is.popIntegerList();
			leagueSeqIndex = is.popShort();
			roleStatus = is.popList(DBForceWarRoleStatus.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushInteger(oppoFid);
			os.pushByte(warIndex);
			os.pushIntegerList(scores);
			os.pushShort(leagueSeqIndex);
			os.pushList(roleStatus);
		}

		public int fid;
		public int oppoFid;
		public byte warIndex;
		public List<Integer> scores;
		public short leagueSeqIndex;
		public List<DBForceWarRoleStatus> roleStatus;
	}

	public static class DBForceWarRoleScore implements Stream.IStreamable
	{

		public DBForceWarRoleScore() { }

		public DBForceWarRoleScore(int rid, int score)
		{
			this.rid = rid;
			this.score = score;
		}

		public DBForceWarRoleScore ksClone()
		{
			return new DBForceWarRoleScore(rid, score);
		}

		public DBForceWarRoleScore kdClone()
		{
			DBForceWarRoleScore _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			score = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushInteger(score);
		}

		public int rid;
		public int score;
	}

	public static class DBForceWarCupWin implements Stream.IStreamable
	{

		public DBForceWarCupWin() { }

		public DBForceWarCupWin(int rid, int cupWin)
		{
			this.rid = rid;
			this.cupWin = cupWin;
		}

		public DBForceWarCupWin ksClone()
		{
			return new DBForceWarCupWin(rid, cupWin);
		}

		public DBForceWarCupWin kdClone()
		{
			DBForceWarCupWin _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			cupWin = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushInteger(cupWin);
		}

		public int rid;
		public int cupWin;
	}

	public static class DBForceWarQuiz implements Stream.IStreamable
	{

		public DBForceWarQuiz() { }

		public DBForceWarQuiz(List<Integer> f1WinRoles, List<Integer> f2WinRoles)
		{
			this.f1WinRoles = f1WinRoles;
			this.f2WinRoles = f2WinRoles;
		}

		public DBForceWarQuiz ksClone()
		{
			return new DBForceWarQuiz(f1WinRoles, f2WinRoles);
		}

		public DBForceWarQuiz kdClone()
		{
			DBForceWarQuiz _kio_clobj = ksClone();
			_kio_clobj.f1WinRoles = new ArrayList<Integer>();
			for(Integer _kio_iter : f1WinRoles)
			{
				_kio_clobj.f1WinRoles.add(_kio_iter);
			}
			_kio_clobj.f2WinRoles = new ArrayList<Integer>();
			for(Integer _kio_iter : f2WinRoles)
			{
				_kio_clobj.f2WinRoles.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			f1WinRoles = is.popIntegerList();
			f2WinRoles = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushIntegerList(f1WinRoles);
			os.pushIntegerList(f2WinRoles);
		}

		public List<Integer> f1WinRoles;
		public List<Integer> f2WinRoles;
	}

	public static class DBForceWarOld implements Stream.IStreamable
	{

		public DBForceWarOld() { }

		public DBForceWarOld(int bid, List<DBForceWarForcePlayerOld> players, byte stage, List<DBForceWarStatus> status, 
		                     List<DBForceWarRoleScore> roleScores, List<DBForceWarCupWin> cupWins, List<Integer> cupResults, List<DBForceWarRecord> leagueRecords, 
		                     List<Integer> leagueSequence, List<DBForceWarRecord> cupRecords, List<DBForceWarQuiz> cupQuizs)
		{
			this.bid = bid;
			this.players = players;
			this.stage = stage;
			this.status = status;
			this.roleScores = roleScores;
			this.cupWins = cupWins;
			this.cupResults = cupResults;
			this.leagueRecords = leagueRecords;
			this.leagueSequence = leagueSequence;
			this.cupRecords = cupRecords;
			this.cupQuizs = cupQuizs;
		}

		public DBForceWarOld ksClone()
		{
			return new DBForceWarOld(bid, players, stage, status, 
			                         roleScores, cupWins, cupResults, leagueRecords, 
			                         leagueSequence, cupRecords, cupQuizs);
		}

		public DBForceWarOld kdClone()
		{
			DBForceWarOld _kio_clobj = ksClone();
			_kio_clobj.players = new ArrayList<DBForceWarForcePlayerOld>();
			for(DBForceWarForcePlayerOld _kio_iter : players)
			{
				_kio_clobj.players.add(_kio_iter.kdClone());
			}
			_kio_clobj.status = new ArrayList<DBForceWarStatus>();
			for(DBForceWarStatus _kio_iter : status)
			{
				_kio_clobj.status.add(_kio_iter.kdClone());
			}
			_kio_clobj.roleScores = new ArrayList<DBForceWarRoleScore>();
			for(DBForceWarRoleScore _kio_iter : roleScores)
			{
				_kio_clobj.roleScores.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupWins = new ArrayList<DBForceWarCupWin>();
			for(DBForceWarCupWin _kio_iter : cupWins)
			{
				_kio_clobj.cupWins.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupResults = new ArrayList<Integer>();
			for(Integer _kio_iter : cupResults)
			{
				_kio_clobj.cupResults.add(_kio_iter);
			}
			_kio_clobj.leagueRecords = new ArrayList<DBForceWarRecord>();
			for(DBForceWarRecord _kio_iter : leagueRecords)
			{
				_kio_clobj.leagueRecords.add(_kio_iter.kdClone());
			}
			_kio_clobj.leagueSequence = new ArrayList<Integer>();
			for(Integer _kio_iter : leagueSequence)
			{
				_kio_clobj.leagueSequence.add(_kio_iter);
			}
			_kio_clobj.cupRecords = new ArrayList<DBForceWarRecord>();
			for(DBForceWarRecord _kio_iter : cupRecords)
			{
				_kio_clobj.cupRecords.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupQuizs = new ArrayList<DBForceWarQuiz>();
			for(DBForceWarQuiz _kio_iter : cupQuizs)
			{
				_kio_clobj.cupQuizs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			bid = is.popInteger();
			players = is.popList(DBForceWarForcePlayerOld.class);
			stage = is.popByte();
			status = is.popList(DBForceWarStatus.class);
			roleScores = is.popList(DBForceWarRoleScore.class);
			cupWins = is.popList(DBForceWarCupWin.class);
			cupResults = is.popIntegerList();
			leagueRecords = is.popList(DBForceWarRecord.class);
			leagueSequence = is.popIntegerList();
			cupRecords = is.popList(DBForceWarRecord.class);
			cupQuizs = is.popList(DBForceWarQuiz.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(bid);
			os.pushList(players);
			os.pushByte(stage);
			os.pushList(status);
			os.pushList(roleScores);
			os.pushList(cupWins);
			os.pushIntegerList(cupResults);
			os.pushList(leagueRecords);
			os.pushIntegerList(leagueSequence);
			os.pushList(cupRecords);
			os.pushList(cupQuizs);
		}

		public int bid;
		public List<DBForceWarForcePlayerOld> players;
		public byte stage;
		public List<DBForceWarStatus> status;
		public List<DBForceWarRoleScore> roleScores;
		public List<DBForceWarCupWin> cupWins;
		public List<Integer> cupResults;
		public List<DBForceWarRecord> leagueRecords;
		public List<Integer> leagueSequence;
		public List<DBForceWarRecord> cupRecords;
		public List<DBForceWarQuiz> cupQuizs;
	}

	public static class DBForceWar implements Stream.IStreamable
	{

		public DBForceWar() { }

		public DBForceWar(int bid, List<DBForceWarForcePlayer> players, byte stage, List<DBForceWarStatus> status, 
		                  List<DBForceWarRoleScore> roleScores, List<DBForceWarCupWin> cupWins, List<Integer> cupResults, List<DBForceWarRecord> leagueRecords, 
		                  List<Integer> leagueSequence, List<DBForceWarRecord> cupRecords, List<DBForceWarQuiz> cupQuizs)
		{
			this.bid = bid;
			this.players = players;
			this.stage = stage;
			this.status = status;
			this.roleScores = roleScores;
			this.cupWins = cupWins;
			this.cupResults = cupResults;
			this.leagueRecords = leagueRecords;
			this.leagueSequence = leagueSequence;
			this.cupRecords = cupRecords;
			this.cupQuizs = cupQuizs;
		}

		public DBForceWar ksClone()
		{
			return new DBForceWar(bid, players, stage, status, 
			                      roleScores, cupWins, cupResults, leagueRecords, 
			                      leagueSequence, cupRecords, cupQuizs);
		}

		public DBForceWar kdClone()
		{
			DBForceWar _kio_clobj = ksClone();
			_kio_clobj.players = new ArrayList<DBForceWarForcePlayer>();
			for(DBForceWarForcePlayer _kio_iter : players)
			{
				_kio_clobj.players.add(_kio_iter.kdClone());
			}
			_kio_clobj.status = new ArrayList<DBForceWarStatus>();
			for(DBForceWarStatus _kio_iter : status)
			{
				_kio_clobj.status.add(_kio_iter.kdClone());
			}
			_kio_clobj.roleScores = new ArrayList<DBForceWarRoleScore>();
			for(DBForceWarRoleScore _kio_iter : roleScores)
			{
				_kio_clobj.roleScores.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupWins = new ArrayList<DBForceWarCupWin>();
			for(DBForceWarCupWin _kio_iter : cupWins)
			{
				_kio_clobj.cupWins.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupResults = new ArrayList<Integer>();
			for(Integer _kio_iter : cupResults)
			{
				_kio_clobj.cupResults.add(_kio_iter);
			}
			_kio_clobj.leagueRecords = new ArrayList<DBForceWarRecord>();
			for(DBForceWarRecord _kio_iter : leagueRecords)
			{
				_kio_clobj.leagueRecords.add(_kio_iter.kdClone());
			}
			_kio_clobj.leagueSequence = new ArrayList<Integer>();
			for(Integer _kio_iter : leagueSequence)
			{
				_kio_clobj.leagueSequence.add(_kio_iter);
			}
			_kio_clobj.cupRecords = new ArrayList<DBForceWarRecord>();
			for(DBForceWarRecord _kio_iter : cupRecords)
			{
				_kio_clobj.cupRecords.add(_kio_iter.kdClone());
			}
			_kio_clobj.cupQuizs = new ArrayList<DBForceWarQuiz>();
			for(DBForceWarQuiz _kio_iter : cupQuizs)
			{
				_kio_clobj.cupQuizs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			bid = is.popInteger();
			players = is.popList(DBForceWarForcePlayer.class);
			stage = is.popByte();
			status = is.popList(DBForceWarStatus.class);
			roleScores = is.popList(DBForceWarRoleScore.class);
			cupWins = is.popList(DBForceWarCupWin.class);
			cupResults = is.popIntegerList();
			leagueRecords = is.popList(DBForceWarRecord.class);
			leagueSequence = is.popIntegerList();
			cupRecords = is.popList(DBForceWarRecord.class);
			cupQuizs = is.popList(DBForceWarQuiz.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(bid);
			os.pushList(players);
			os.pushByte(stage);
			os.pushList(status);
			os.pushList(roleScores);
			os.pushList(cupWins);
			os.pushIntegerList(cupResults);
			os.pushList(leagueRecords);
			os.pushIntegerList(leagueSequence);
			os.pushList(cupRecords);
			os.pushList(cupQuizs);
		}

		public int bid;
		public List<DBForceWarForcePlayer> players;
		public byte stage;
		public List<DBForceWarStatus> status;
		public List<DBForceWarRoleScore> roleScores;
		public List<DBForceWarCupWin> cupWins;
		public List<Integer> cupResults;
		public List<DBForceWarRecord> leagueRecords;
		public List<Integer> leagueSequence;
		public List<DBForceWarRecord> cupRecords;
		public List<DBForceWarQuiz> cupQuizs;
	}

	public static class DBForceThiefStatus implements Stream.IStreamable
	{

		public DBForceThiefStatus() { }

		public DBForceThiefStatus(int fid, int startTime, int killed, int stamp, 
		                          List<Integer> roles, List<Integer> bossRoles, List<DBForceWarGeneralStatus> bossStatus, int r1, 
		                          int r2)
		{
			this.fid = fid;
			this.startTime = startTime;
			this.killed = killed;
			this.stamp = stamp;
			this.roles = roles;
			this.bossRoles = bossRoles;
			this.bossStatus = bossStatus;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceThiefStatus ksClone()
		{
			return new DBForceThiefStatus(fid, startTime, killed, stamp, 
			                              roles, bossRoles, bossStatus, r1, 
			                              r2);
		}

		public DBForceThiefStatus kdClone()
		{
			DBForceThiefStatus _kio_clobj = ksClone();
			_kio_clobj.roles = new ArrayList<Integer>();
			for(Integer _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter);
			}
			_kio_clobj.bossRoles = new ArrayList<Integer>();
			for(Integer _kio_iter : bossRoles)
			{
				_kio_clobj.bossRoles.add(_kio_iter);
			}
			_kio_clobj.bossStatus = new ArrayList<DBForceWarGeneralStatus>();
			for(DBForceWarGeneralStatus _kio_iter : bossStatus)
			{
				_kio_clobj.bossStatus.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			startTime = is.popInteger();
			killed = is.popInteger();
			stamp = is.popInteger();
			roles = is.popIntegerList();
			bossRoles = is.popIntegerList();
			bossStatus = is.popList(DBForceWarGeneralStatus.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushInteger(startTime);
			os.pushInteger(killed);
			os.pushInteger(stamp);
			os.pushIntegerList(roles);
			os.pushIntegerList(bossRoles);
			os.pushList(bossStatus);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int fid;
		public int startTime;
		public int killed;
		public int stamp;
		public List<Integer> roles;
		public List<Integer> bossRoles;
		public List<DBForceWarGeneralStatus> bossStatus;
		public int r1;
		public int r2;
	}

	public static class DBForceThiefGeneralBrief implements Stream.IStreamable
	{

		public DBForceThiefGeneralBrief() { }

		public DBForceThiefGeneralBrief(short id, short lvl, byte advLvl, byte evoLvl, 
		                                int r1, int r2)
		{
			this.id = id;
			this.lvl = lvl;
			this.advLvl = advLvl;
			this.evoLvl = evoLvl;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceThiefGeneralBrief ksClone()
		{
			return new DBForceThiefGeneralBrief(id, lvl, advLvl, evoLvl, 
			                                    r1, r2);
		}

		public DBForceThiefGeneralBrief kdClone()
		{
			DBForceThiefGeneralBrief _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			advLvl = is.popByte();
			evoLvl = is.popByte();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(advLvl);
			os.pushByte(evoLvl);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short id;
		public short lvl;
		public byte advLvl;
		public byte evoLvl;
		public int r1;
		public int r2;
	}

	public static class DBForceThiefBossDamage implements Stream.IStreamable
	{

		public DBForceThiefBossDamage() { }

		public DBForceThiefBossDamage(int fid, int rid, int damage, String name, 
		                              short icon, short lvl, List<DBForceThiefGeneralBrief> generals, int r1, 
		                              int r2)
		{
			this.fid = fid;
			this.rid = rid;
			this.damage = damage;
			this.name = name;
			this.icon = icon;
			this.lvl = lvl;
			this.generals = generals;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceThiefBossDamage ksClone()
		{
			return new DBForceThiefBossDamage(fid, rid, damage, name, 
			                                  icon, lvl, generals, r1, 
			                                  r2);
		}

		public DBForceThiefBossDamage kdClone()
		{
			DBForceThiefBossDamage _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<DBForceThiefGeneralBrief>();
			for(DBForceThiefGeneralBrief _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			rid = is.popInteger();
			damage = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			lvl = is.popShort();
			generals = is.popList(DBForceThiefGeneralBrief.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushInteger(rid);
			os.pushInteger(damage);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(lvl);
			os.pushList(generals);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int fid;
		public int rid;
		public int damage;
		public String name;
		public short icon;
		public short lvl;
		public List<DBForceThiefGeneralBrief> generals;
		public int r1;
		public int r2;
	}

	public static class DBForceThief implements Stream.IStreamable
	{

		public DBForceThief() { }

		public DBForceThief(int bid, List<DBForceThiefStatus> thiefStatus, List<DBForceThiefBossDamage> bossDamage, int thiefStartTime, 
		                    int r1, int r2)
		{
			this.bid = bid;
			this.thiefStatus = thiefStatus;
			this.bossDamage = bossDamage;
			this.thiefStartTime = thiefStartTime;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceThief ksClone()
		{
			return new DBForceThief(bid, thiefStatus, bossDamage, thiefStartTime, 
			                        r1, r2);
		}

		public DBForceThief kdClone()
		{
			DBForceThief _kio_clobj = ksClone();
			_kio_clobj.thiefStatus = new ArrayList<DBForceThiefStatus>();
			for(DBForceThiefStatus _kio_iter : thiefStatus)
			{
				_kio_clobj.thiefStatus.add(_kio_iter.kdClone());
			}
			_kio_clobj.bossDamage = new ArrayList<DBForceThiefBossDamage>();
			for(DBForceThiefBossDamage _kio_iter : bossDamage)
			{
				_kio_clobj.bossDamage.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			bid = is.popInteger();
			thiefStatus = is.popList(DBForceThiefStatus.class);
			bossDamage = is.popList(DBForceThiefBossDamage.class);
			thiefStartTime = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(bid);
			os.pushList(thiefStatus);
			os.pushList(bossDamage);
			os.pushInteger(thiefStartTime);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int bid;
		public List<DBForceThiefStatus> thiefStatus;
		public List<DBForceThiefBossDamage> bossDamage;
		public int thiefStartTime;
		public int r1;
		public int r2;
	}

	public static class DBForceBeast implements Stream.IStreamable
	{

		public DBForceBeast() { }

		public DBForceBeast(int fid, i3k.DBRoleGeneral general, byte attackProp, byte mainProp, 
		                    List<Short> props, short resetPropTimes, int r1, int r2)
		{
			this.fid = fid;
			this.general = general;
			this.attackProp = attackProp;
			this.mainProp = mainProp;
			this.props = props;
			this.resetPropTimes = resetPropTimes;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceBeast ksClone()
		{
			return new DBForceBeast(fid, general, attackProp, mainProp, 
			                        props, resetPropTimes, r1, r2);
		}

		public DBForceBeast kdClone()
		{
			DBForceBeast _kio_clobj = ksClone();
			_kio_clobj.general = general.kdClone();
			_kio_clobj.props = new ArrayList<Short>();
			for(Short _kio_iter : props)
			{
				_kio_clobj.props.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			if( general == null )
				general = new i3k.DBRoleGeneral();
			is.pop(general);
			attackProp = is.popByte();
			mainProp = is.popByte();
			props = is.popShortList();
			resetPropTimes = is.popShort();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.push(general);
			os.pushByte(attackProp);
			os.pushByte(mainProp);
			os.pushShortList(props);
			os.pushShort(resetPropTimes);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int fid;
		public i3k.DBRoleGeneral general;
		public byte attackProp;
		public byte mainProp;
		public List<Short> props;
		public short resetPropTimes;
		public int r1;
		public int r2;
	}

	public static class DBForceWarBeast implements Stream.IStreamable
	{

		public DBForceWarBeast() { }

		public DBForceWarBeast(List<DBForceBeast> beasts, int r1, int r2)
		{
			this.beasts = beasts;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceWarBeast ksClone()
		{
			return new DBForceWarBeast(beasts, r1, r2);
		}

		public DBForceWarBeast kdClone()
		{
			DBForceWarBeast _kio_clobj = ksClone();
			_kio_clobj.beasts = new ArrayList<DBForceBeast>();
			for(DBForceBeast _kio_iter : beasts)
			{
				_kio_clobj.beasts.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			beasts = is.popList(DBForceBeast.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(beasts);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBForceBeast> beasts;
		public int r1;
		public int r2;
	}

	public static class DBInvitationGroupTask implements Stream.IStreamable
	{

		public DBInvitationGroupTask() { }

		public DBInvitationGroupTask(int groupId, int seq, int reqCurVal)
		{
			this.groupId = groupId;
			this.seq = seq;
			this.reqCurVal = reqCurVal;
		}

		public DBInvitationGroupTask ksClone()
		{
			return new DBInvitationGroupTask(groupId, seq, reqCurVal);
		}

		public DBInvitationGroupTask kdClone()
		{
			DBInvitationGroupTask _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			groupId = is.popInteger();
			seq = is.popInteger();
			reqCurVal = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(groupId);
			os.pushInteger(seq);
			os.pushInteger(reqCurVal);
		}

		public int groupId;
		public int seq;
		public int reqCurVal;
	}

	public static class DBInvitationTasks implements Stream.IStreamable
	{

		public DBInvitationTasks() { }

		public DBInvitationTasks(int type, List<DBInvitationGroupTask> tasks, int points, List<Integer> pointsReward)
		{
			this.type = type;
			this.tasks = tasks;
			this.points = points;
			this.pointsReward = pointsReward;
		}

		public DBInvitationTasks ksClone()
		{
			return new DBInvitationTasks(type, tasks, points, pointsReward);
		}

		public DBInvitationTasks kdClone()
		{
			DBInvitationTasks _kio_clobj = ksClone();
			_kio_clobj.tasks = new ArrayList<DBInvitationGroupTask>();
			for(DBInvitationGroupTask _kio_iter : tasks)
			{
				_kio_clobj.tasks.add(_kio_iter.kdClone());
			}
			_kio_clobj.pointsReward = new ArrayList<Integer>();
			for(Integer _kio_iter : pointsReward)
			{
				_kio_clobj.pointsReward.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popInteger();
			tasks = is.popList(DBInvitationGroupTask.class);
			points = is.popInteger();
			pointsReward = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(type);
			os.pushList(tasks);
			os.pushInteger(points);
			os.pushIntegerList(pointsReward);
		}

		public int type;
		public List<DBInvitationGroupTask> tasks;
		public int points;
		public List<Integer> pointsReward;
	}

	public static class DBInvitationFriend implements Stream.IStreamable
	{

		public DBInvitationFriend() { }

		public DBInvitationFriend(int gsid, int roleid, String rolename, int level, 
		                          int vip, short icon, int lastSyncTime, int lastLoginTime)
		{
			this.gsid = gsid;
			this.roleid = roleid;
			this.rolename = rolename;
			this.level = level;
			this.vip = vip;
			this.icon = icon;
			this.lastSyncTime = lastSyncTime;
			this.lastLoginTime = lastLoginTime;
		}

		public DBInvitationFriend ksClone()
		{
			return new DBInvitationFriend(gsid, roleid, rolename, level, 
			                              vip, icon, lastSyncTime, lastLoginTime);
		}

		public DBInvitationFriend kdClone()
		{
			DBInvitationFriend _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gsid = is.popInteger();
			roleid = is.popInteger();
			rolename = is.popString();
			level = is.popInteger();
			vip = is.popInteger();
			icon = is.popShort();
			lastSyncTime = is.popInteger();
			lastLoginTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gsid);
			os.pushInteger(roleid);
			os.pushString(rolename);
			os.pushInteger(level);
			os.pushInteger(vip);
			os.pushShort(icon);
			os.pushInteger(lastSyncTime);
			os.pushInteger(lastLoginTime);
		}

		public int gsid;
		public int roleid;
		public String rolename;
		public int level;
		public int vip;
		public short icon;
		public int lastSyncTime;
		public int lastLoginTime;
	}

	public static class DBInvitationSelf implements Stream.IStreamable
	{

		public DBInvitationSelf() { }

		public DBInvitationSelf(int gsid, int roleid)
		{
			this.gsid = gsid;
			this.roleid = roleid;
		}

		public DBInvitationSelf ksClone()
		{
			return new DBInvitationSelf(gsid, roleid);
		}

		public DBInvitationSelf kdClone()
		{
			DBInvitationSelf _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gsid = is.popInteger();
			roleid = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(gsid);
			os.pushInteger(roleid);
		}

		public int gsid;
		public int roleid;
	}

	public static class DBInvitation implements Stream.IStreamable
	{

		public DBInvitation() { }

		public DBInvitation(List<DBInvitationTasks> tasks, List<DBInvitationFriend> friends, DBInvitationSelf selfInvitated)
		{
			this.tasks = tasks;
			this.friends = friends;
			this.selfInvitated = selfInvitated;
		}

		public DBInvitation ksClone()
		{
			return new DBInvitation(tasks, friends, selfInvitated);
		}

		public DBInvitation kdClone()
		{
			DBInvitation _kio_clobj = ksClone();
			_kio_clobj.tasks = new ArrayList<DBInvitationTasks>();
			for(DBInvitationTasks _kio_iter : tasks)
			{
				_kio_clobj.tasks.add(_kio_iter.kdClone());
			}
			_kio_clobj.friends = new ArrayList<DBInvitationFriend>();
			for(DBInvitationFriend _kio_iter : friends)
			{
				_kio_clobj.friends.add(_kio_iter.kdClone());
			}
			_kio_clobj.selfInvitated = selfInvitated.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tasks = is.popList(DBInvitationTasks.class);
			friends = is.popList(DBInvitationFriend.class);
			if( selfInvitated == null )
				selfInvitated = new DBInvitationSelf();
			is.pop(selfInvitated);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(tasks);
			os.pushList(friends);
			os.push(selfInvitated);
		}

		public List<DBInvitationTasks> tasks;
		public List<DBInvitationFriend> friends;
		public DBInvitationSelf selfInvitated;
	}

	public static class DBRichTech implements Stream.IStreamable
	{

		public DBRichTech() { }

		public DBRichTech(int id, int level)
		{
			this.id = id;
			this.level = level;
		}

		public DBRichTech ksClone()
		{
			return new DBRichTech(id, level);
		}

		public DBRichTech kdClone()
		{
			DBRichTech _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			level = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(level);
		}

		public int id;
		public int level;
	}

	public static class DBRichDailyTask implements Stream.IStreamable
	{

		public DBRichDailyTask() { }

		public DBRichDailyTask(int id, int reqCurVal, byte reward)
		{
			this.id = id;
			this.reqCurVal = reqCurVal;
			this.reward = reward;
		}

		public DBRichDailyTask ksClone()
		{
			return new DBRichDailyTask(id, reqCurVal, reward);
		}

		public DBRichDailyTask kdClone()
		{
			DBRichDailyTask _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			reqCurVal = is.popInteger();
			reward = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(reqCurVal);
			os.pushByte(reward);
		}

		public int id;
		public int reqCurVal;
		public byte reward;
	}

	public static class DBRichMapBuilding implements Stream.IStreamable
	{

		public DBRichMapBuilding() { }

		public DBRichMapBuilding(byte type, int status, List<Integer> params)
		{
			this.type = type;
			this.status = status;
			this.params = params;
		}

		public DBRichMapBuilding ksClone()
		{
			return new DBRichMapBuilding(type, status, params);
		}

		public DBRichMapBuilding kdClone()
		{
			DBRichMapBuilding _kio_clobj = ksClone();
			_kio_clobj.params = new ArrayList<Integer>();
			for(Integer _kio_iter : params)
			{
				_kio_clobj.params.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			status = is.popInteger();
			params = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushInteger(status);
			os.pushIntegerList(params);
		}

		public byte type;
		public int status;
		public List<Integer> params;
	}

	public static class DBRichMapObject implements Stream.IStreamable
	{

		public DBRichMapObject() { }

		public DBRichMapObject(byte type, short pos)
		{
			this.type = type;
			this.pos = pos;
		}

		public DBRichMapObject ksClone()
		{
			return new DBRichMapObject(type, pos);
		}

		public DBRichMapObject kdClone()
		{
			DBRichMapObject _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			pos = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(pos);
		}

		public byte type;
		public short pos;
	}

	public static class DBRichMapObjectExt implements Stream.IStreamable
	{

		public DBRichMapObjectExt() { }

		public DBRichMapObjectExt(byte type, short pos, List<Integer> params)
		{
			this.type = type;
			this.pos = pos;
			this.params = params;
		}

		public DBRichMapObjectExt ksClone()
		{
			return new DBRichMapObjectExt(type, pos, params);
		}

		public DBRichMapObjectExt kdClone()
		{
			DBRichMapObjectExt _kio_clobj = ksClone();
			_kio_clobj.params = new ArrayList<Integer>();
			for(Integer _kio_iter : params)
			{
				_kio_clobj.params.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			pos = is.popShort();
			params = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(pos);
			os.pushIntegerList(params);
		}

		public byte type;
		public short pos;
		public List<Integer> params;
	}

	public static class DBRichMap implements Stream.IStreamable
	{

		public DBRichMap() { }

		public DBRichMap(List<DBRichMapBuilding> buildings, List<DBRichMapObject> objects, List<DBRichMapObject> miniobjects)
		{
			this.buildings = buildings;
			this.objects = objects;
			this.miniobjects = miniobjects;
		}

		public DBRichMap ksClone()
		{
			return new DBRichMap(buildings, objects, miniobjects);
		}

		public DBRichMap kdClone()
		{
			DBRichMap _kio_clobj = ksClone();
			_kio_clobj.buildings = new ArrayList<DBRichMapBuilding>();
			for(DBRichMapBuilding _kio_iter : buildings)
			{
				_kio_clobj.buildings.add(_kio_iter.kdClone());
			}
			_kio_clobj.objects = new ArrayList<DBRichMapObject>();
			for(DBRichMapObject _kio_iter : objects)
			{
				_kio_clobj.objects.add(_kio_iter.kdClone());
			}
			_kio_clobj.miniobjects = new ArrayList<DBRichMapObject>();
			for(DBRichMapObject _kio_iter : miniobjects)
			{
				_kio_clobj.miniobjects.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			buildings = is.popList(DBRichMapBuilding.class);
			objects = is.popList(DBRichMapObject.class);
			miniobjects = is.popList(DBRichMapObject.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(buildings);
			os.pushList(objects);
			os.pushList(miniobjects);
		}

		public List<DBRichMapBuilding> buildings;
		public List<DBRichMapObject> objects;
		public List<DBRichMapObject> miniobjects;
	}

	public static class DBRichMapExt implements Stream.IStreamable
	{

		public DBRichMapExt() { }

		public DBRichMapExt(List<DBRichMapObjectExt> objects, int r1)
		{
			this.objects = objects;
			this.r1 = r1;
		}

		public DBRichMapExt ksClone()
		{
			return new DBRichMapExt(objects, r1);
		}

		public DBRichMapExt kdClone()
		{
			DBRichMapExt _kio_clobj = ksClone();
			_kio_clobj.objects = new ArrayList<DBRichMapObjectExt>();
			for(DBRichMapObjectExt _kio_iter : objects)
			{
				_kio_clobj.objects.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			objects = is.popList(DBRichMapObjectExt.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(objects);
			os.pushInteger(r1);
		}

		public List<DBRichMapObjectExt> objects;
		public int r1;
	}

	public static class DBRichRoleGold implements Stream.IStreamable
	{

		public DBRichRoleGold() { }

		public DBRichRoleGold(int rid, int gold, String name, short icon, 
		                      short lvl, int minReward)
		{
			this.rid = rid;
			this.gold = gold;
			this.name = name;
			this.icon = icon;
			this.lvl = lvl;
			this.minReward = minReward;
		}

		public DBRichRoleGold ksClone()
		{
			return new DBRichRoleGold(rid, gold, name, icon, 
			                          lvl, minReward);
		}

		public DBRichRoleGold kdClone()
		{
			DBRichRoleGold _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			gold = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			lvl = is.popShort();
			minReward = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushInteger(gold);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(lvl);
			os.pushInteger(minReward);
		}

		public int rid;
		public int gold;
		public String name;
		public short icon;
		public short lvl;
		public int minReward;
	}

	public static class DBRichRoleGeneralBrief implements Stream.IStreamable
	{

		public DBRichRoleGeneralBrief() { }

		public DBRichRoleGeneralBrief(short id, short lvl, byte advLvl, byte evoLvl)
		{
			this.id = id;
			this.lvl = lvl;
			this.advLvl = advLvl;
			this.evoLvl = evoLvl;
		}

		public DBRichRoleGeneralBrief ksClone()
		{
			return new DBRichRoleGeneralBrief(id, lvl, advLvl, evoLvl);
		}

		public DBRichRoleGeneralBrief kdClone()
		{
			DBRichRoleGeneralBrief _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			advLvl = is.popByte();
			evoLvl = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(advLvl);
			os.pushByte(evoLvl);
		}

		public short id;
		public short lvl;
		public byte advLvl;
		public byte evoLvl;
	}

	public static class DBRichRoleBossDamage implements Stream.IStreamable
	{

		public DBRichRoleBossDamage() { }

		public DBRichRoleBossDamage(int rid, int damage, String name, short icon, 
		                            short lvl, short combatId, List<DBRichRoleGeneralBrief> generals)
		{
			this.rid = rid;
			this.damage = damage;
			this.name = name;
			this.icon = icon;
			this.lvl = lvl;
			this.combatId = combatId;
			this.generals = generals;
		}

		public DBRichRoleBossDamage ksClone()
		{
			return new DBRichRoleBossDamage(rid, damage, name, icon, 
			                                lvl, combatId, generals);
		}

		public DBRichRoleBossDamage kdClone()
		{
			DBRichRoleBossDamage _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<DBRichRoleGeneralBrief>();
			for(DBRichRoleGeneralBrief _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			damage = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			lvl = is.popShort();
			combatId = is.popShort();
			generals = is.popList(DBRichRoleGeneralBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushInteger(damage);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(lvl);
			os.pushShort(combatId);
			os.pushList(generals);
		}

		public int rid;
		public int damage;
		public String name;
		public short icon;
		public short lvl;
		public short combatId;
		public List<DBRichRoleGeneralBrief> generals;
	}

	public static class DBRichRoleLotery implements Stream.IStreamable
	{

		public DBRichRoleLotery() { }

		public DBRichRoleLotery(int rid, List<Byte> lotery)
		{
			this.rid = rid;
			this.lotery = lotery;
		}

		public DBRichRoleLotery ksClone()
		{
			return new DBRichRoleLotery(rid, lotery);
		}

		public DBRichRoleLotery kdClone()
		{
			DBRichRoleLotery _kio_clobj = ksClone();
			_kio_clobj.lotery = new ArrayList<Byte>();
			for(Byte _kio_iter : lotery)
			{
				_kio_clobj.lotery.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			lotery = is.popByteList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushByteList(lotery);
		}

		public int rid;
		public List<Byte> lotery;
	}

	public static class DBRich implements Stream.IStreamable
	{

		public DBRich() { }

		public DBRich(int day, DBRichMap map, List<DBRichRoleGold> gold, List<DBRichRoleBossDamage> bossDamage, 
		              List<DBRichRoleLotery> lotery, List<DBRichMapExt> mapExt, byte padding12, byte padding13, 
		              byte padding14, int padding2, int padding3, int padding4, 
		              int padding5)
		{
			this.day = day;
			this.map = map;
			this.gold = gold;
			this.bossDamage = bossDamage;
			this.lotery = lotery;
			this.mapExt = mapExt;
			this.padding12 = padding12;
			this.padding13 = padding13;
			this.padding14 = padding14;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
			this.padding5 = padding5;
		}

		public DBRich ksClone()
		{
			return new DBRich(day, map, gold, bossDamage, 
			                  lotery, mapExt, padding12, padding13, 
			                  padding14, padding2, padding3, padding4, 
			                  padding5);
		}

		public DBRich kdClone()
		{
			DBRich _kio_clobj = ksClone();
			_kio_clobj.map = map.kdClone();
			_kio_clobj.gold = new ArrayList<DBRichRoleGold>();
			for(DBRichRoleGold _kio_iter : gold)
			{
				_kio_clobj.gold.add(_kio_iter.kdClone());
			}
			_kio_clobj.bossDamage = new ArrayList<DBRichRoleBossDamage>();
			for(DBRichRoleBossDamage _kio_iter : bossDamage)
			{
				_kio_clobj.bossDamage.add(_kio_iter.kdClone());
			}
			_kio_clobj.lotery = new ArrayList<DBRichRoleLotery>();
			for(DBRichRoleLotery _kio_iter : lotery)
			{
				_kio_clobj.lotery.add(_kio_iter.kdClone());
			}
			_kio_clobj.mapExt = new ArrayList<DBRichMapExt>();
			for(DBRichMapExt _kio_iter : mapExt)
			{
				_kio_clobj.mapExt.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			day = is.popInteger();
			if( map == null )
				map = new DBRichMap();
			is.pop(map);
			gold = is.popList(DBRichRoleGold.class);
			bossDamage = is.popList(DBRichRoleBossDamage.class);
			lotery = is.popList(DBRichRoleLotery.class);
			mapExt = is.popList(DBRichMapExt.class);
			padding12 = is.popByte();
			padding13 = is.popByte();
			padding14 = is.popByte();
			padding2 = is.popInteger();
			padding3 = is.popInteger();
			padding4 = is.popInteger();
			padding5 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(day);
			os.push(map);
			os.pushList(gold);
			os.pushList(bossDamage);
			os.pushList(lotery);
			os.pushList(mapExt);
			os.pushByte(padding12);
			os.pushByte(padding13);
			os.pushByte(padding14);
			os.pushInteger(padding2);
			os.pushInteger(padding3);
			os.pushInteger(padding4);
			os.pushInteger(padding5);
		}

		public int day;
		public DBRichMap map;
		public List<DBRichRoleGold> gold;
		public List<DBRichRoleBossDamage> bossDamage;
		public List<DBRichRoleLotery> lotery;
		public List<DBRichMapExt> mapExt;
		public byte padding12;
		public byte padding13;
		public byte padding14;
		public int padding2;
		public int padding3;
		public int padding4;
		public int padding5;
	}

	public static class DBRichRole implements Stream.IStreamable
	{

		public DBRichRole() { }

		public DBRichRole(int day, DBRichMap map, byte mapType, short pos, 
		                  byte minipos, int gold, int goldTotal, short movement, 
		                  byte angelLeft, byte devilLeft, byte turtleLeft, byte boomLeft, 
		                  float boomUpRate, float thiefHpDownRate, float thiefHpDownEndRate, float thiefDefenseDownRate, 
		                  float thiefDefenseDownEndRate, byte building, byte bossTimes, int bossDamage, 
		                  List<Byte> lotery, List<DBRichTech> techTree, List<DBRichDailyTask> dailyTask, byte dailyTaskNotice, 
		                  int point, int status, byte buyMovementTimes, byte gender, 
		                  List<DBRichMapExt> mapExt, byte padding12, byte padding13, byte padding14, 
		                  int goldHistory, int padding3, int padding4, int padding5)
		{
			this.day = day;
			this.map = map;
			this.mapType = mapType;
			this.pos = pos;
			this.minipos = minipos;
			this.gold = gold;
			this.goldTotal = goldTotal;
			this.movement = movement;
			this.angelLeft = angelLeft;
			this.devilLeft = devilLeft;
			this.turtleLeft = turtleLeft;
			this.boomLeft = boomLeft;
			this.boomUpRate = boomUpRate;
			this.thiefHpDownRate = thiefHpDownRate;
			this.thiefHpDownEndRate = thiefHpDownEndRate;
			this.thiefDefenseDownRate = thiefDefenseDownRate;
			this.thiefDefenseDownEndRate = thiefDefenseDownEndRate;
			this.building = building;
			this.bossTimes = bossTimes;
			this.bossDamage = bossDamage;
			this.lotery = lotery;
			this.techTree = techTree;
			this.dailyTask = dailyTask;
			this.dailyTaskNotice = dailyTaskNotice;
			this.point = point;
			this.status = status;
			this.buyMovementTimes = buyMovementTimes;
			this.gender = gender;
			this.mapExt = mapExt;
			this.padding12 = padding12;
			this.padding13 = padding13;
			this.padding14 = padding14;
			this.goldHistory = goldHistory;
			this.padding3 = padding3;
			this.padding4 = padding4;
			this.padding5 = padding5;
		}

		public DBRichRole ksClone()
		{
			return new DBRichRole(day, map, mapType, pos, 
			                      minipos, gold, goldTotal, movement, 
			                      angelLeft, devilLeft, turtleLeft, boomLeft, 
			                      boomUpRate, thiefHpDownRate, thiefHpDownEndRate, thiefDefenseDownRate, 
			                      thiefDefenseDownEndRate, building, bossTimes, bossDamage, 
			                      lotery, techTree, dailyTask, dailyTaskNotice, 
			                      point, status, buyMovementTimes, gender, 
			                      mapExt, padding12, padding13, padding14, 
			                      goldHistory, padding3, padding4, padding5);
		}

		public DBRichRole kdClone()
		{
			DBRichRole _kio_clobj = ksClone();
			_kio_clobj.map = map.kdClone();
			_kio_clobj.lotery = new ArrayList<Byte>();
			for(Byte _kio_iter : lotery)
			{
				_kio_clobj.lotery.add(_kio_iter);
			}
			_kio_clobj.techTree = new ArrayList<DBRichTech>();
			for(DBRichTech _kio_iter : techTree)
			{
				_kio_clobj.techTree.add(_kio_iter.kdClone());
			}
			_kio_clobj.dailyTask = new ArrayList<DBRichDailyTask>();
			for(DBRichDailyTask _kio_iter : dailyTask)
			{
				_kio_clobj.dailyTask.add(_kio_iter.kdClone());
			}
			_kio_clobj.mapExt = new ArrayList<DBRichMapExt>();
			for(DBRichMapExt _kio_iter : mapExt)
			{
				_kio_clobj.mapExt.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			day = is.popInteger();
			if( map == null )
				map = new DBRichMap();
			is.pop(map);
			mapType = is.popByte();
			pos = is.popShort();
			minipos = is.popByte();
			gold = is.popInteger();
			goldTotal = is.popInteger();
			movement = is.popShort();
			angelLeft = is.popByte();
			devilLeft = is.popByte();
			turtleLeft = is.popByte();
			boomLeft = is.popByte();
			boomUpRate = is.popFloat();
			thiefHpDownRate = is.popFloat();
			thiefHpDownEndRate = is.popFloat();
			thiefDefenseDownRate = is.popFloat();
			thiefDefenseDownEndRate = is.popFloat();
			building = is.popByte();
			bossTimes = is.popByte();
			bossDamage = is.popInteger();
			lotery = is.popByteList();
			techTree = is.popList(DBRichTech.class);
			dailyTask = is.popList(DBRichDailyTask.class);
			dailyTaskNotice = is.popByte();
			point = is.popInteger();
			status = is.popInteger();
			buyMovementTimes = is.popByte();
			gender = is.popByte();
			mapExt = is.popList(DBRichMapExt.class);
			padding12 = is.popByte();
			padding13 = is.popByte();
			padding14 = is.popByte();
			goldHistory = is.popInteger();
			padding3 = is.popInteger();
			padding4 = is.popInteger();
			padding5 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(day);
			os.push(map);
			os.pushByte(mapType);
			os.pushShort(pos);
			os.pushByte(minipos);
			os.pushInteger(gold);
			os.pushInteger(goldTotal);
			os.pushShort(movement);
			os.pushByte(angelLeft);
			os.pushByte(devilLeft);
			os.pushByte(turtleLeft);
			os.pushByte(boomLeft);
			os.pushFloat(boomUpRate);
			os.pushFloat(thiefHpDownRate);
			os.pushFloat(thiefHpDownEndRate);
			os.pushFloat(thiefDefenseDownRate);
			os.pushFloat(thiefDefenseDownEndRate);
			os.pushByte(building);
			os.pushByte(bossTimes);
			os.pushInteger(bossDamage);
			os.pushByteList(lotery);
			os.pushList(techTree);
			os.pushList(dailyTask);
			os.pushByte(dailyTaskNotice);
			os.pushInteger(point);
			os.pushInteger(status);
			os.pushByte(buyMovementTimes);
			os.pushByte(gender);
			os.pushList(mapExt);
			os.pushByte(padding12);
			os.pushByte(padding13);
			os.pushByte(padding14);
			os.pushInteger(goldHistory);
			os.pushInteger(padding3);
			os.pushInteger(padding4);
			os.pushInteger(padding5);
		}

		public int day;
		public DBRichMap map;
		public byte mapType;
		public short pos;
		public byte minipos;
		public int gold;
		public int goldTotal;
		public short movement;
		public byte angelLeft;
		public byte devilLeft;
		public byte turtleLeft;
		public byte boomLeft;
		public float boomUpRate;
		public float thiefHpDownRate;
		public float thiefHpDownEndRate;
		public float thiefDefenseDownRate;
		public float thiefDefenseDownEndRate;
		public byte building;
		public byte bossTimes;
		public int bossDamage;
		public List<Byte> lotery;
		public List<DBRichTech> techTree;
		public List<DBRichDailyTask> dailyTask;
		public byte dailyTaskNotice;
		public int point;
		public int status;
		public byte buyMovementTimes;
		public byte gender;
		public List<DBRichMapExt> mapExt;
		public byte padding12;
		public byte padding13;
		public byte padding14;
		public int goldHistory;
		public int padding3;
		public int padding4;
		public int padding5;
	}

	public static class DBForceChapterGeneralStatus implements Stream.IStreamable
	{

		public DBForceChapterGeneralStatus() { }

		public DBForceChapterGeneralStatus(short id, int hp, int mp, int state)
		{
			this.id = id;
			this.hp = hp;
			this.mp = mp;
			this.state = state;
		}

		public DBForceChapterGeneralStatus ksClone()
		{
			return new DBForceChapterGeneralStatus(id, hp, mp, state);
		}

		public DBForceChapterGeneralStatus kdClone()
		{
			DBForceChapterGeneralStatus _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			hp = is.popInteger();
			mp = is.popInteger();
			state = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(hp);
			os.pushInteger(mp);
			os.pushInteger(state);
		}

		public short id;
		public int hp;
		public int mp;
		public int state;
	}

	public static class DBForceChapterBox implements Stream.IStreamable
	{

		public DBForceChapterBox() { }

		public DBForceChapterBox(byte stage, List<Integer> rids)
		{
			this.stage = stage;
			this.rids = rids;
		}

		public DBForceChapterBox ksClone()
		{
			return new DBForceChapterBox(stage, rids);
		}

		public DBForceChapterBox kdClone()
		{
			DBForceChapterBox _kio_clobj = ksClone();
			_kio_clobj.rids = new ArrayList<Integer>();
			for(Integer _kio_iter : rids)
			{
				_kio_clobj.rids.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stage = is.popByte();
			rids = is.popIntegerList();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(stage);
			os.pushIntegerList(rids);
		}

		public byte stage;
		public List<Integer> rids;
	}

	public static class DBForceTreasureApply implements Stream.IStreamable
	{

		public DBForceTreasureApply() { }

		public DBForceTreasureApply(int rid, String name, short headIconId, short lvl)
		{
			this.rid = rid;
			this.name = name;
			this.headIconId = headIconId;
			this.lvl = lvl;
		}

		public DBForceTreasureApply ksClone()
		{
			return new DBForceTreasureApply(rid, name, headIconId, lvl);
		}

		public DBForceTreasureApply kdClone()
		{
			DBForceTreasureApply _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			name = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(name);
			os.pushShort(headIconId);
			os.pushShort(lvl);
		}

		public int rid;
		public String name;
		public short headIconId;
		public short lvl;
	}

	public static class DBForceChapterTreasure implements Stream.IStreamable
	{

		public DBForceChapterTreasure() { }

		public DBForceChapterTreasure(byte type, short id)
		{
			this.type = type;
			this.id = id;
		}

		public DBForceChapterTreasure ksClone()
		{
			return new DBForceChapterTreasure(type, id);
		}

		public DBForceChapterTreasure kdClone()
		{
			DBForceChapterTreasure _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			id = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(id);
		}

		public byte type;
		public short id;
	}

	public static class DBForceTotalTreasure implements Stream.IStreamable
	{

		public DBForceTotalTreasure() { }

		public DBForceTotalTreasure(byte type, short id, int count, int time, 
		                            List<DBForceTreasureApply> apply)
		{
			this.type = type;
			this.id = id;
			this.count = count;
			this.time = time;
			this.apply = apply;
		}

		public DBForceTotalTreasure ksClone()
		{
			return new DBForceTotalTreasure(type, id, count, time, 
			                                apply);
		}

		public DBForceTotalTreasure kdClone()
		{
			DBForceTotalTreasure _kio_clobj = ksClone();
			_kio_clobj.apply = new ArrayList<DBForceTreasureApply>();
			for(DBForceTreasureApply _kio_iter : apply)
			{
				_kio_clobj.apply.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			id = is.popShort();
			count = is.popInteger();
			time = is.popInteger();
			apply = is.popList(DBForceTreasureApply.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushShort(id);
			os.pushInteger(count);
			os.pushInteger(time);
			os.pushList(apply);
		}

		public byte type;
		public short id;
		public int count;
		public int time;
		public List<DBForceTreasureApply> apply;
	}

	public static class DBForceTreasureLog implements Stream.IStreamable
	{

		public DBForceTreasureLog() { }

		public DBForceTreasureLog(int rid, String name, short headIconId, short lvl, 
		                          byte type, short id, int time)
		{
			this.rid = rid;
			this.name = name;
			this.headIconId = headIconId;
			this.lvl = lvl;
			this.type = type;
			this.id = id;
			this.time = time;
		}

		public DBForceTreasureLog ksClone()
		{
			return new DBForceTreasureLog(rid, name, headIconId, lvl, 
			                              type, id, time);
		}

		public DBForceTreasureLog kdClone()
		{
			DBForceTreasureLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			name = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
			type = is.popByte();
			id = is.popShort();
			time = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(name);
			os.pushShort(headIconId);
			os.pushShort(lvl);
			os.pushByte(type);
			os.pushShort(id);
			os.pushInteger(time);
		}

		public int rid;
		public String name;
		public short headIconId;
		public short lvl;
		public byte type;
		public short id;
		public int time;
	}

	public static class DBForceChapterGeneralBrief implements Stream.IStreamable
	{

		public DBForceChapterGeneralBrief() { }

		public DBForceChapterGeneralBrief(short id, short lvl, byte advLvl, byte evoLvl)
		{
			this.id = id;
			this.lvl = lvl;
			this.advLvl = advLvl;
			this.evoLvl = evoLvl;
		}

		public DBForceChapterGeneralBrief ksClone()
		{
			return new DBForceChapterGeneralBrief(id, lvl, advLvl, evoLvl);
		}

		public DBForceChapterGeneralBrief kdClone()
		{
			DBForceChapterGeneralBrief _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvl = is.popShort();
			advLvl = is.popByte();
			evoLvl = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShort(lvl);
			os.pushByte(advLvl);
			os.pushByte(evoLvl);
		}

		public short id;
		public short lvl;
		public byte advLvl;
		public byte evoLvl;
	}

	public static class DBForceChapterDamage implements Stream.IStreamable
	{

		public DBForceChapterDamage() { }

		public DBForceChapterDamage(int fid, int rid, byte chapter, int damage, 
		                            String fname, String name, short icon, short lvl, 
		                            List<DBForceChapterGeneralBrief> generals)
		{
			this.fid = fid;
			this.rid = rid;
			this.chapter = chapter;
			this.damage = damage;
			this.fname = fname;
			this.name = name;
			this.icon = icon;
			this.lvl = lvl;
			this.generals = generals;
		}

		public DBForceChapterDamage ksClone()
		{
			return new DBForceChapterDamage(fid, rid, chapter, damage, 
			                                fname, name, icon, lvl, 
			                                generals);
		}

		public DBForceChapterDamage kdClone()
		{
			DBForceChapterDamage _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<DBForceChapterGeneralBrief>();
			for(DBForceChapterGeneralBrief _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			rid = is.popInteger();
			chapter = is.popByte();
			damage = is.popInteger();
			fname = is.popString();
			name = is.popString();
			icon = is.popShort();
			lvl = is.popShort();
			generals = is.popList(DBForceChapterGeneralBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushInteger(rid);
			os.pushByte(chapter);
			os.pushInteger(damage);
			os.pushString(fname);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(lvl);
			os.pushList(generals);
		}

		public int fid;
		public int rid;
		public byte chapter;
		public int damage;
		public String fname;
		public String name;
		public short icon;
		public short lvl;
		public List<DBForceChapterGeneralBrief> generals;
	}

	public static class DBForceChapter implements Stream.IStreamable
	{

		public DBForceChapter() { }

		public DBForceChapter(byte stage, List<DBForceChapterGeneralStatus> enemyStatus, List<DBForceChapterBox> boxTaken, List<Integer> rids, 
		                      byte reward, int r1, int r2)
		{
			this.stage = stage;
			this.enemyStatus = enemyStatus;
			this.boxTaken = boxTaken;
			this.rids = rids;
			this.reward = reward;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceChapter ksClone()
		{
			return new DBForceChapter(stage, enemyStatus, boxTaken, rids, 
			                          reward, r1, r2);
		}

		public DBForceChapter kdClone()
		{
			DBForceChapter _kio_clobj = ksClone();
			_kio_clobj.enemyStatus = new ArrayList<DBForceChapterGeneralStatus>();
			for(DBForceChapterGeneralStatus _kio_iter : enemyStatus)
			{
				_kio_clobj.enemyStatus.add(_kio_iter.kdClone());
			}
			_kio_clobj.boxTaken = new ArrayList<DBForceChapterBox>();
			for(DBForceChapterBox _kio_iter : boxTaken)
			{
				_kio_clobj.boxTaken.add(_kio_iter.kdClone());
			}
			_kio_clobj.rids = new ArrayList<Integer>();
			for(Integer _kio_iter : rids)
			{
				_kio_clobj.rids.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stage = is.popByte();
			enemyStatus = is.popList(DBForceChapterGeneralStatus.class);
			boxTaken = is.popList(DBForceChapterBox.class);
			rids = is.popIntegerList();
			reward = is.popByte();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(stage);
			os.pushList(enemyStatus);
			os.pushList(boxTaken);
			os.pushIntegerList(rids);
			os.pushByte(reward);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public byte stage;
		public List<DBForceChapterGeneralStatus> enemyStatus;
		public List<DBForceChapterBox> boxTaken;
		public List<Integer> rids;
		public byte reward;
		public int r1;
		public int r2;
	}

	public static class DBForceDungeon implements Stream.IStreamable
	{

		public DBForceDungeon() { }

		public DBForceDungeon(int fid, int day, List<DBForceChapter> chapters, int r1, 
		                      int r2)
		{
			this.fid = fid;
			this.day = day;
			this.chapters = chapters;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceDungeon ksClone()
		{
			return new DBForceDungeon(fid, day, chapters, r1, 
			                          r2);
		}

		public DBForceDungeon kdClone()
		{
			DBForceDungeon _kio_clobj = ksClone();
			_kio_clobj.chapters = new ArrayList<DBForceChapter>();
			for(DBForceChapter _kio_iter : chapters)
			{
				_kio_clobj.chapters.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			day = is.popInteger();
			chapters = is.popList(DBForceChapter.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushInteger(day);
			os.pushList(chapters);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int fid;
		public int day;
		public List<DBForceChapter> chapters;
		public int r1;
		public int r2;
	}

	public static class DBForceTreasureList implements Stream.IStreamable
	{

		public DBForceTreasureList() { }

		public DBForceTreasureList(List<DBForceChapterTreasure> list)
		{
			this.list = list;
		}

		public DBForceTreasureList ksClone()
		{
			return new DBForceTreasureList(list);
		}

		public DBForceTreasureList kdClone()
		{
			DBForceTreasureList _kio_clobj = ksClone();
			_kio_clobj.list = new ArrayList<DBForceChapterTreasure>();
			for(DBForceChapterTreasure _kio_iter : list)
			{
				_kio_clobj.list.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			list = is.popList(DBForceChapterTreasure.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(list);
		}

		public List<DBForceChapterTreasure> list;
	}

	public static class DBForceTreasure implements Stream.IStreamable
	{

		public DBForceTreasure() { }

		public DBForceTreasure(int fid, List<DBForceTreasureList> treasure, List<DBForceTotalTreasure> totalTreasure, List<DBForceTreasureLog> logs)
		{
			this.fid = fid;
			this.treasure = treasure;
			this.totalTreasure = totalTreasure;
			this.logs = logs;
		}

		public DBForceTreasure ksClone()
		{
			return new DBForceTreasure(fid, treasure, totalTreasure, logs);
		}

		public DBForceTreasure kdClone()
		{
			DBForceTreasure _kio_clobj = ksClone();
			_kio_clobj.treasure = new ArrayList<DBForceTreasureList>();
			for(DBForceTreasureList _kio_iter : treasure)
			{
				_kio_clobj.treasure.add(_kio_iter.kdClone());
			}
			_kio_clobj.totalTreasure = new ArrayList<DBForceTotalTreasure>();
			for(DBForceTotalTreasure _kio_iter : totalTreasure)
			{
				_kio_clobj.totalTreasure.add(_kio_iter.kdClone());
			}
			_kio_clobj.logs = new ArrayList<DBForceTreasureLog>();
			for(DBForceTreasureLog _kio_iter : logs)
			{
				_kio_clobj.logs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			treasure = is.popList(DBForceTreasureList.class);
			totalTreasure = is.popList(DBForceTotalTreasure.class);
			logs = is.popList(DBForceTreasureLog.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushList(treasure);
			os.pushList(totalTreasure);
			os.pushList(logs);
		}

		public int fid;
		public List<DBForceTreasureList> treasure;
		public List<DBForceTotalTreasure> totalTreasure;
		public List<DBForceTreasureLog> logs;
	}

	public static class DBForceTreasureDeploy implements Stream.IStreamable
	{

		public DBForceTreasureDeploy() { }

		public DBForceTreasureDeploy(int fid, short deployTimes, int r1)
		{
			this.fid = fid;
			this.deployTimes = deployTimes;
			this.r1 = r1;
		}

		public DBForceTreasureDeploy ksClone()
		{
			return new DBForceTreasureDeploy(fid, deployTimes, r1);
		}

		public DBForceTreasureDeploy kdClone()
		{
			DBForceTreasureDeploy _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			deployTimes = is.popShort();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushShort(deployTimes);
			os.pushInteger(r1);
		}

		public int fid;
		public short deployTimes;
		public int r1;
	}

	public static class DBForceDungeonGlobal implements Stream.IStreamable
	{

		public DBForceDungeonGlobal() { }

		public DBForceDungeonGlobal(List<DBForceTreasure> treasure, List<DBForceChapterDamage> damages, List<DBForceChapterDamage> forceDamages, List<DBForceTreasureDeploy> deploys, 
		                            byte r12, byte r13, byte r14, int r2)
		{
			this.treasure = treasure;
			this.damages = damages;
			this.forceDamages = forceDamages;
			this.deploys = deploys;
			this.r12 = r12;
			this.r13 = r13;
			this.r14 = r14;
			this.r2 = r2;
		}

		public DBForceDungeonGlobal ksClone()
		{
			return new DBForceDungeonGlobal(treasure, damages, forceDamages, deploys, 
			                                r12, r13, r14, r2);
		}

		public DBForceDungeonGlobal kdClone()
		{
			DBForceDungeonGlobal _kio_clobj = ksClone();
			_kio_clobj.treasure = new ArrayList<DBForceTreasure>();
			for(DBForceTreasure _kio_iter : treasure)
			{
				_kio_clobj.treasure.add(_kio_iter.kdClone());
			}
			_kio_clobj.damages = new ArrayList<DBForceChapterDamage>();
			for(DBForceChapterDamage _kio_iter : damages)
			{
				_kio_clobj.damages.add(_kio_iter.kdClone());
			}
			_kio_clobj.forceDamages = new ArrayList<DBForceChapterDamage>();
			for(DBForceChapterDamage _kio_iter : forceDamages)
			{
				_kio_clobj.forceDamages.add(_kio_iter.kdClone());
			}
			_kio_clobj.deploys = new ArrayList<DBForceTreasureDeploy>();
			for(DBForceTreasureDeploy _kio_iter : deploys)
			{
				_kio_clobj.deploys.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			treasure = is.popList(DBForceTreasure.class);
			damages = is.popList(DBForceChapterDamage.class);
			forceDamages = is.popList(DBForceChapterDamage.class);
			deploys = is.popList(DBForceTreasureDeploy.class);
			r12 = is.popByte();
			r13 = is.popByte();
			r14 = is.popByte();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(treasure);
			os.pushList(damages);
			os.pushList(forceDamages);
			os.pushList(deploys);
			os.pushByte(r12);
			os.pushByte(r13);
			os.pushByte(r14);
			os.pushInteger(r2);
		}

		public List<DBForceTreasure> treasure;
		public List<DBForceChapterDamage> damages;
		public List<DBForceChapterDamage> forceDamages;
		public List<DBForceTreasureDeploy> deploys;
		public byte r12;
		public byte r13;
		public byte r14;
		public int r2;
	}

	public static class DBRelation implements Stream.IStreamable
	{

		public DBRelation() { }

		public DBRelation(short id, List<Short> lvls, int r1, int r2)
		{
			this.id = id;
			this.lvls = lvls;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBRelation ksClone()
		{
			return new DBRelation(id, lvls, r1, r2);
		}

		public DBRelation kdClone()
		{
			DBRelation _kio_clobj = ksClone();
			_kio_clobj.lvls = new ArrayList<Short>();
			for(Short _kio_iter : lvls)
			{
				_kio_clobj.lvls.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			lvls = is.popShortList();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushShortList(lvls);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short id;
		public List<Short> lvls;
		public int r1;
		public int r2;
	}

	public static class DBGeneralStoneProp implements Stream.IStreamable
	{

		public DBGeneralStoneProp() { }

		public DBGeneralStoneProp(short propId, byte type, byte rank, float value)
		{
			this.propId = propId;
			this.type = type;
			this.rank = rank;
			this.value = value;
		}

		public DBGeneralStoneProp ksClone()
		{
			return new DBGeneralStoneProp(propId, type, rank, value);
		}

		public DBGeneralStoneProp kdClone()
		{
			DBGeneralStoneProp _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			propId = is.popShort();
			type = is.popByte();
			rank = is.popByte();
			value = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(propId);
			os.pushByte(type);
			os.pushByte(rank);
			os.pushFloat(value);
		}

		public short propId;
		public byte type;
		public byte rank;
		public float value;
	}

	public static class DBGeneralStone implements Stream.IStreamable
	{

		public DBGeneralStone() { }

		public DBGeneralStone(int id, int itemId, int exp, short gid, 
		                      byte pos, List<DBGeneralStoneProp> passes, List<DBGeneralStoneProp> resetPasses, byte padding1, 
		                      byte padding2, byte padding3, byte padding4)
		{
			this.id = id;
			this.itemId = itemId;
			this.exp = exp;
			this.gid = gid;
			this.pos = pos;
			this.passes = passes;
			this.resetPasses = resetPasses;
			this.padding1 = padding1;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
		}

		public DBGeneralStone ksClone()
		{
			return new DBGeneralStone(id, itemId, exp, gid, 
			                          pos, passes, resetPasses, padding1, 
			                          padding2, padding3, padding4);
		}

		public DBGeneralStone kdClone()
		{
			DBGeneralStone _kio_clobj = ksClone();
			_kio_clobj.passes = new ArrayList<DBGeneralStoneProp>();
			for(DBGeneralStoneProp _kio_iter : passes)
			{
				_kio_clobj.passes.add(_kio_iter.kdClone());
			}
			_kio_clobj.resetPasses = new ArrayList<DBGeneralStoneProp>();
			for(DBGeneralStoneProp _kio_iter : resetPasses)
			{
				_kio_clobj.resetPasses.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			itemId = is.popInteger();
			exp = is.popInteger();
			gid = is.popShort();
			pos = is.popByte();
			passes = is.popList(DBGeneralStoneProp.class);
			resetPasses = is.popList(DBGeneralStoneProp.class);
			padding1 = is.popByte();
			padding2 = is.popByte();
			padding3 = is.popByte();
			padding4 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(itemId);
			os.pushInteger(exp);
			os.pushShort(gid);
			os.pushByte(pos);
			os.pushList(passes);
			os.pushList(resetPasses);
			os.pushByte(padding1);
			os.pushByte(padding2);
			os.pushByte(padding3);
			os.pushByte(padding4);
		}

		public int id;
		public int itemId;
		public int exp;
		public short gid;
		public byte pos;
		public List<DBGeneralStoneProp> passes;
		public List<DBGeneralStoneProp> resetPasses;
		public byte padding1;
		public byte padding2;
		public byte padding3;
		public byte padding4;
	}

	public static class DBRoleSeyen implements Stream.IStreamable
	{

		public DBRoleSeyen() { }

		public DBRoleSeyen(int seyen, int seyenTotal, int padding1, int padding2, 
		                   byte padding3, byte padding4)
		{
			this.seyen = seyen;
			this.seyenTotal = seyenTotal;
			this.padding1 = padding1;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
		}

		public DBRoleSeyen ksClone()
		{
			return new DBRoleSeyen(seyen, seyenTotal, padding1, padding2, 
			                       padding3, padding4);
		}

		public DBRoleSeyen kdClone()
		{
			DBRoleSeyen _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			seyen = is.popInteger();
			seyenTotal = is.popInteger();
			padding1 = is.popInteger();
			padding2 = is.popInteger();
			padding3 = is.popByte();
			padding4 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(seyen);
			os.pushInteger(seyenTotal);
			os.pushInteger(padding1);
			os.pushInteger(padding2);
			os.pushByte(padding3);
			os.pushByte(padding4);
		}

		public int seyen;
		public int seyenTotal;
		public int padding1;
		public int padding2;
		public byte padding3;
		public byte padding4;
	}

	public static class DBGeneralStoneInfo implements Stream.IStreamable
	{

		public DBGeneralStoneInfo() { }

		public DBGeneralStoneInfo(int stoneId, List<DBGeneralStone> generalStone, List<DBRoleSeyen> generalSeyen, byte padding2, 
		                          byte padding3, byte padding4, byte padding5, byte padding6, 
		                          byte padding7, byte padding8)
		{
			this.stoneId = stoneId;
			this.generalStone = generalStone;
			this.generalSeyen = generalSeyen;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
			this.padding5 = padding5;
			this.padding6 = padding6;
			this.padding7 = padding7;
			this.padding8 = padding8;
		}

		public DBGeneralStoneInfo ksClone()
		{
			return new DBGeneralStoneInfo(stoneId, generalStone, generalSeyen, padding2, 
			                              padding3, padding4, padding5, padding6, 
			                              padding7, padding8);
		}

		public DBGeneralStoneInfo kdClone()
		{
			DBGeneralStoneInfo _kio_clobj = ksClone();
			_kio_clobj.generalStone = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : generalStone)
			{
				_kio_clobj.generalStone.add(_kio_iter.kdClone());
			}
			_kio_clobj.generalSeyen = new ArrayList<DBRoleSeyen>();
			for(DBRoleSeyen _kio_iter : generalSeyen)
			{
				_kio_clobj.generalSeyen.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stoneId = is.popInteger();
			generalStone = is.popList(DBGeneralStone.class);
			generalSeyen = is.popList(DBRoleSeyen.class);
			padding2 = is.popByte();
			padding3 = is.popByte();
			padding4 = is.popByte();
			padding5 = is.popByte();
			padding6 = is.popByte();
			padding7 = is.popByte();
			padding8 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(stoneId);
			os.pushList(generalStone);
			os.pushList(generalSeyen);
			os.pushByte(padding2);
			os.pushByte(padding3);
			os.pushByte(padding4);
			os.pushByte(padding5);
			os.pushByte(padding6);
			os.pushByte(padding7);
			os.pushByte(padding8);
		}

		public int stoneId;
		public List<DBGeneralStone> generalStone;
		public List<DBRoleSeyen> generalSeyen;
		public byte padding2;
		public byte padding3;
		public byte padding4;
		public byte padding5;
		public byte padding6;
		public byte padding7;
		public byte padding8;
	}

	public static class DBExpiratBossTimes implements Stream.IStreamable
	{

		public DBExpiratBossTimes() { }

		public DBExpiratBossTimes(short id, byte times, byte buytimes, int bonusday, 
		                          int damage)
		{
			this.id = id;
			this.times = times;
			this.buytimes = buytimes;
			this.bonusday = bonusday;
			this.damage = damage;
		}

		public DBExpiratBossTimes ksClone()
		{
			return new DBExpiratBossTimes(id, times, buytimes, bonusday, 
			                              damage);
		}

		public DBExpiratBossTimes kdClone()
		{
			DBExpiratBossTimes _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			times = is.popByte();
			buytimes = is.popByte();
			bonusday = is.popInteger();
			damage = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(times);
			os.pushByte(buytimes);
			os.pushInteger(bonusday);
			os.pushInteger(damage);
		}

		public short id;
		public byte times;
		public byte buytimes;
		public int bonusday;
		public int damage;
	}

	public static class DBExpiratBossDamage implements Stream.IStreamable
	{

		public DBExpiratBossDamage() { }

		public DBExpiratBossDamage(short id, int damage)
		{
			this.id = id;
			this.damage = damage;
		}

		public DBExpiratBossDamage ksClone()
		{
			return new DBExpiratBossDamage(id, damage);
		}

		public DBExpiratBossDamage kdClone()
		{
			DBExpiratBossDamage _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			damage = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushInteger(damage);
		}

		public short id;
		public int damage;
	}

	public static class DBExpiratBossInfo implements Stream.IStreamable
	{

		public DBExpiratBossInfo() { }

		public DBExpiratBossInfo(int damage, List<DBExpiratBossTimes> times, List<DBExpiratBossDamage> damageHis, int bonusday, 
		                         short level, int padding2, int padding3, int padding4, 
		                         byte padding5, byte padding6, byte padding7, byte padding8)
		{
			this.damage = damage;
			this.times = times;
			this.damageHis = damageHis;
			this.bonusday = bonusday;
			this.level = level;
			this.padding2 = padding2;
			this.padding3 = padding3;
			this.padding4 = padding4;
			this.padding5 = padding5;
			this.padding6 = padding6;
			this.padding7 = padding7;
			this.padding8 = padding8;
		}

		public DBExpiratBossInfo ksClone()
		{
			return new DBExpiratBossInfo(damage, times, damageHis, bonusday, 
			                             level, padding2, padding3, padding4, 
			                             padding5, padding6, padding7, padding8);
		}

		public DBExpiratBossInfo kdClone()
		{
			DBExpiratBossInfo _kio_clobj = ksClone();
			_kio_clobj.times = new ArrayList<DBExpiratBossTimes>();
			for(DBExpiratBossTimes _kio_iter : times)
			{
				_kio_clobj.times.add(_kio_iter.kdClone());
			}
			_kio_clobj.damageHis = new ArrayList<DBExpiratBossDamage>();
			for(DBExpiratBossDamage _kio_iter : damageHis)
			{
				_kio_clobj.damageHis.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			damage = is.popInteger();
			times = is.popList(DBExpiratBossTimes.class);
			damageHis = is.popList(DBExpiratBossDamage.class);
			bonusday = is.popInteger();
			level = is.popShort();
			padding2 = is.popInteger();
			padding3 = is.popInteger();
			padding4 = is.popInteger();
			padding5 = is.popByte();
			padding6 = is.popByte();
			padding7 = is.popByte();
			padding8 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(damage);
			os.pushList(times);
			os.pushList(damageHis);
			os.pushInteger(bonusday);
			os.pushShort(level);
			os.pushInteger(padding2);
			os.pushInteger(padding3);
			os.pushInteger(padding4);
			os.pushByte(padding5);
			os.pushByte(padding6);
			os.pushByte(padding7);
			os.pushByte(padding8);
		}

		public int damage;
		public List<DBExpiratBossTimes> times;
		public List<DBExpiratBossDamage> damageHis;
		public int bonusday;
		public short level;
		public int padding2;
		public int padding3;
		public int padding4;
		public byte padding5;
		public byte padding6;
		public byte padding7;
		public byte padding8;
	}

	public static class DBRoleDuel implements Stream.IStreamable
	{

		public DBRoleDuel() { }

		public DBRoleDuel(int point, byte rewardTimes, byte fightTimes, byte buyTimes, 
		                  byte r14, int r2)
		{
			this.point = point;
			this.rewardTimes = rewardTimes;
			this.fightTimes = fightTimes;
			this.buyTimes = buyTimes;
			this.r14 = r14;
			this.r2 = r2;
		}

		public DBRoleDuel ksClone()
		{
			return new DBRoleDuel(point, rewardTimes, fightTimes, buyTimes, 
			                      r14, r2);
		}

		public DBRoleDuel kdClone()
		{
			DBRoleDuel _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			point = is.popInteger();
			rewardTimes = is.popByte();
			fightTimes = is.popByte();
			buyTimes = is.popByte();
			r14 = is.popByte();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(point);
			os.pushByte(rewardTimes);
			os.pushByte(fightTimes);
			os.pushByte(buyTimes);
			os.pushByte(r14);
			os.pushInteger(r2);
		}

		public int point;
		public byte rewardTimes;
		public byte fightTimes;
		public byte buyTimes;
		public byte r14;
		public int r2;
	}

	public static class DBDuelRole implements Stream.IStreamable
	{

		public DBDuelRole() { }

		public DBDuelRole(int rid, short lvl, int score, int rank, 
		                  short lastResults, short r12, int r2)
		{
			this.rid = rid;
			this.lvl = lvl;
			this.score = score;
			this.rank = rank;
			this.lastResults = lastResults;
			this.r12 = r12;
			this.r2 = r2;
		}

		public DBDuelRole ksClone()
		{
			return new DBDuelRole(rid, lvl, score, rank, 
			                      lastResults, r12, r2);
		}

		public DBDuelRole kdClone()
		{
			DBDuelRole _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			lvl = is.popShort();
			score = is.popInteger();
			rank = is.popInteger();
			lastResults = is.popShort();
			r12 = is.popShort();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushShort(lvl);
			os.pushInteger(score);
			os.pushInteger(rank);
			os.pushShort(lastResults);
			os.pushShort(r12);
			os.pushInteger(r2);
		}

		public int rid;
		public short lvl;
		public int score;
		public int rank;
		public short lastResults;
		public short r12;
		public int r2;
	}

	public static class DBDuelRank implements Stream.IStreamable
	{

		public DBDuelRank() { }

		public DBDuelRank(String serverName, int rid, String rname, short headIconId, 
		                  short lvl, int score)
		{
			this.serverName = serverName;
			this.rid = rid;
			this.rname = rname;
			this.headIconId = headIconId;
			this.lvl = lvl;
			this.score = score;
		}

		public DBDuelRank ksClone()
		{
			return new DBDuelRank(serverName, rid, rname, headIconId, 
			                      lvl, score);
		}

		public DBDuelRank kdClone()
		{
			DBDuelRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverName = is.popString();
			rid = is.popInteger();
			rname = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
			score = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(serverName);
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushShort(headIconId);
			os.pushShort(lvl);
			os.pushInteger(score);
		}

		public String serverName;
		public int rid;
		public String rname;
		public short headIconId;
		public short lvl;
		public int score;
	}

	public static class DBExpiratBossRole implements Stream.IStreamable
	{

		public DBExpiratBossRole() { }

		public DBExpiratBossRole(int rid, short combat, int rank, short r12, 
		                         int r2)
		{
			this.rid = rid;
			this.combat = combat;
			this.rank = rank;
			this.r12 = r12;
			this.r2 = r2;
		}

		public DBExpiratBossRole ksClone()
		{
			return new DBExpiratBossRole(rid, combat, rank, r12, 
			                             r2);
		}

		public DBExpiratBossRole kdClone()
		{
			DBExpiratBossRole _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			combat = is.popShort();
			rank = is.popInteger();
			r12 = is.popShort();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushShort(combat);
			os.pushInteger(rank);
			os.pushShort(r12);
			os.pushInteger(r2);
		}

		public int rid;
		public short combat;
		public int rank;
		public short r12;
		public int r2;
	}

	public static class DBExpiratBossRank implements Stream.IStreamable
	{

		public DBExpiratBossRank() { }

		public DBExpiratBossRank(short combatId, String serverName, int rid, String rname, 
		                         short headIconId, short lvl, int damage)
		{
			this.combatId = combatId;
			this.serverName = serverName;
			this.rid = rid;
			this.rname = rname;
			this.headIconId = headIconId;
			this.lvl = lvl;
			this.damage = damage;
		}

		public DBExpiratBossRank ksClone()
		{
			return new DBExpiratBossRank(combatId, serverName, rid, rname, 
			                             headIconId, lvl, damage);
		}

		public DBExpiratBossRank kdClone()
		{
			DBExpiratBossRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			serverName = is.popString();
			rid = is.popInteger();
			rname = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
			damage = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushString(serverName);
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushShort(headIconId);
			os.pushShort(lvl);
			os.pushInteger(damage);
		}

		public short combatId;
		public String serverName;
		public int rid;
		public String rname;
		public short headIconId;
		public short lvl;
		public int damage;
	}

	public static class DBExpiratBossRankList implements Stream.IStreamable
	{

		public DBExpiratBossRankList() { }

		public DBExpiratBossRankList(short combatId, List<DBExpiratBossRank> expiratBossRank)
		{
			this.combatId = combatId;
			this.expiratBossRank = expiratBossRank;
		}

		public DBExpiratBossRankList ksClone()
		{
			return new DBExpiratBossRankList(combatId, expiratBossRank);
		}

		public DBExpiratBossRankList kdClone()
		{
			DBExpiratBossRankList _kio_clobj = ksClone();
			_kio_clobj.expiratBossRank = new ArrayList<DBExpiratBossRank>();
			for(DBExpiratBossRank _kio_iter : expiratBossRank)
			{
				_kio_clobj.expiratBossRank.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			expiratBossRank = is.popList(DBExpiratBossRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushList(expiratBossRank);
		}

		public short combatId;
		public List<DBExpiratBossRank> expiratBossRank;
	}

	public static class DBExpiratBossRankList2 implements Stream.IStreamable
	{

		public DBExpiratBossRankList2() { }

		public DBExpiratBossRankList2(short combatId, List<ExpiratBossTopRank> expiratBossRank2)
		{
			this.combatId = combatId;
			this.expiratBossRank2 = expiratBossRank2;
		}

		public DBExpiratBossRankList2 ksClone()
		{
			return new DBExpiratBossRankList2(combatId, expiratBossRank2);
		}

		public DBExpiratBossRankList2 kdClone()
		{
			DBExpiratBossRankList2 _kio_clobj = ksClone();
			_kio_clobj.expiratBossRank2 = new ArrayList<ExpiratBossTopRank>();
			for(ExpiratBossTopRank _kio_iter : expiratBossRank2)
			{
				_kio_clobj.expiratBossRank2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			combatId = is.popShort();
			expiratBossRank2 = is.popList(ExpiratBossTopRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(combatId);
			os.pushList(expiratBossRank2);
		}

		public short combatId;
		public List<ExpiratBossTopRank> expiratBossRank2;
	}

	public static class DBDuelMatchRecord implements Stream.IStreamable
	{

		public DBDuelMatchRecord() { }

		public DBDuelMatchRecord(List<i3k.DBRoleGeneral> generals1, List<i3k.DBRoleGeneral> generals2, List<DBPetBrief> pets1, List<DBPetBrief> pets2, 
		                         List<DBRelation> relation1, List<DBRelation> relation2, int seed, byte result, 
		                         int r1)
		{
			this.generals1 = generals1;
			this.generals2 = generals2;
			this.pets1 = pets1;
			this.pets2 = pets2;
			this.relation1 = relation1;
			this.relation2 = relation2;
			this.seed = seed;
			this.result = result;
			this.r1 = r1;
		}

		public DBDuelMatchRecord ksClone()
		{
			return new DBDuelMatchRecord(generals1, generals2, pets1, pets2, 
			                             relation1, relation2, seed, result, 
			                             r1);
		}

		public DBDuelMatchRecord kdClone()
		{
			DBDuelMatchRecord _kio_clobj = ksClone();
			_kio_clobj.generals1 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals1)
			{
				_kio_clobj.generals1.add(_kio_iter.kdClone());
			}
			_kio_clobj.generals2 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals2)
			{
				_kio_clobj.generals2.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets1 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets1)
			{
				_kio_clobj.pets1.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets2 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets2)
			{
				_kio_clobj.pets2.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation1 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation1)
			{
				_kio_clobj.relation1.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation2 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation2)
			{
				_kio_clobj.relation2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			generals1 = is.popList(i3k.DBRoleGeneral.class);
			generals2 = is.popList(i3k.DBRoleGeneral.class);
			pets1 = is.popList(DBPetBrief.class);
			pets2 = is.popList(DBPetBrief.class);
			relation1 = is.popList(DBRelation.class);
			relation2 = is.popList(DBRelation.class);
			seed = is.popInteger();
			result = is.popByte();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(generals1);
			os.pushList(generals2);
			os.pushList(pets1);
			os.pushList(pets2);
			os.pushList(relation1);
			os.pushList(relation2);
			os.pushInteger(seed);
			os.pushByte(result);
			os.pushInteger(r1);
		}

		public List<i3k.DBRoleGeneral> generals1;
		public List<i3k.DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<DBRelation> relation1;
		public List<DBRelation> relation2;
		public int seed;
		public byte result;
		public int r1;
	}

	public static class DBDuelRecord implements Stream.IStreamable
	{

		public DBDuelRecord() { }

		public DBDuelRecord(int rid, String rname1, String rname2, short headIconId1, 
		                    short headIconId2, short level1, short level2, byte first, 
		                    byte result, int time, List<DBDuelMatchRecord> matches, int r1)
		{
			this.rid = rid;
			this.rname1 = rname1;
			this.rname2 = rname2;
			this.headIconId1 = headIconId1;
			this.headIconId2 = headIconId2;
			this.level1 = level1;
			this.level2 = level2;
			this.first = first;
			this.result = result;
			this.time = time;
			this.matches = matches;
			this.r1 = r1;
		}

		public DBDuelRecord ksClone()
		{
			return new DBDuelRecord(rid, rname1, rname2, headIconId1, 
			                        headIconId2, level1, level2, first, 
			                        result, time, matches, r1);
		}

		public DBDuelRecord kdClone()
		{
			DBDuelRecord _kio_clobj = ksClone();
			_kio_clobj.matches = new ArrayList<DBDuelMatchRecord>();
			for(DBDuelMatchRecord _kio_iter : matches)
			{
				_kio_clobj.matches.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname1 = is.popString();
			rname2 = is.popString();
			headIconId1 = is.popShort();
			headIconId2 = is.popShort();
			level1 = is.popShort();
			level2 = is.popShort();
			first = is.popByte();
			result = is.popByte();
			time = is.popInteger();
			matches = is.popList(DBDuelMatchRecord.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname1);
			os.pushString(rname2);
			os.pushShort(headIconId1);
			os.pushShort(headIconId2);
			os.pushShort(level1);
			os.pushShort(level2);
			os.pushByte(first);
			os.pushByte(result);
			os.pushInteger(time);
			os.pushList(matches);
			os.pushInteger(r1);
		}

		public int rid;
		public String rname1;
		public String rname2;
		public short headIconId1;
		public short headIconId2;
		public short level1;
		public short level2;
		public byte first;
		public byte result;
		public int time;
		public List<DBDuelMatchRecord> matches;
		public int r1;
	}

	public static class DBDuel implements Stream.IStreamable
	{

		public DBDuel() { }

		public DBDuel(List<DBDuelRole> roles, List<DBDuelRank> ranks, List<DBDuelRecord> records, int day, 
		              int r1, int r2)
		{
			this.roles = roles;
			this.ranks = ranks;
			this.records = records;
			this.day = day;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBDuel ksClone()
		{
			return new DBDuel(roles, ranks, records, day, 
			                  r1, r2);
		}

		public DBDuel kdClone()
		{
			DBDuel _kio_clobj = ksClone();
			_kio_clobj.roles = new ArrayList<DBDuelRole>();
			for(DBDuelRole _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranks = new ArrayList<DBDuelRank>();
			for(DBDuelRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.records = new ArrayList<DBDuelRecord>();
			for(DBDuelRecord _kio_iter : records)
			{
				_kio_clobj.records.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roles = is.popList(DBDuelRole.class);
			ranks = is.popList(DBDuelRank.class);
			records = is.popList(DBDuelRecord.class);
			day = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(roles);
			os.pushList(ranks);
			os.pushList(records);
			os.pushInteger(day);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBDuelRole> roles;
		public List<DBDuelRank> ranks;
		public List<DBDuelRecord> records;
		public int day;
		public int r1;
		public int r2;
	}

	public static class DBExpiratBoss implements Stream.IStreamable
	{

		public DBExpiratBoss() { }

		public DBExpiratBoss(List<DBExpiratBossRankList> ranks, List<DBExpiratBossRankList2> ranksHis, List<DBExpiratBossRole> roles, int day, 
		                     short bossId, int r1, int r2)
		{
			this.ranks = ranks;
			this.ranksHis = ranksHis;
			this.roles = roles;
			this.day = day;
			this.bossId = bossId;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBExpiratBoss ksClone()
		{
			return new DBExpiratBoss(ranks, ranksHis, roles, day, 
			                         bossId, r1, r2);
		}

		public DBExpiratBoss kdClone()
		{
			DBExpiratBoss _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<DBExpiratBossRankList>();
			for(DBExpiratBossRankList _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranksHis = new ArrayList<DBExpiratBossRankList2>();
			for(DBExpiratBossRankList2 _kio_iter : ranksHis)
			{
				_kio_clobj.ranksHis.add(_kio_iter.kdClone());
			}
			_kio_clobj.roles = new ArrayList<DBExpiratBossRole>();
			for(DBExpiratBossRole _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(DBExpiratBossRankList.class);
			ranksHis = is.popList(DBExpiratBossRankList2.class);
			roles = is.popList(DBExpiratBossRole.class);
			day = is.popInteger();
			bossId = is.popShort();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
			os.pushList(ranksHis);
			os.pushList(roles);
			os.pushInteger(day);
			os.pushShort(bossId);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBExpiratBossRankList> ranks;
		public List<DBExpiratBossRankList2> ranksHis;
		public List<DBExpiratBossRole> roles;
		public int day;
		public short bossId;
		public int r1;
		public int r2;
	}

	public static class DropTableEntryNew implements Stream.IStreamable
	{

		public DropTableEntryNew() { }

		public DropTableEntryNew(DropEntryNew item, float pro)
		{
			this.item = item;
			this.pro = pro;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( item == null )
				item = new DropEntryNew();
			is.pop(item);
			pro = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(item);
			os.pushFloat(pro);
		}

		public DropEntryNew item;
		public float pro;
	}

	public static class HeroesRanks implements Stream.IStreamable
	{

		public HeroesRanks() { }

		public HeroesRanks(int type, List<HeroesRank> ranks)
		{
			this.type = type;
			this.ranks = ranks;
		}

		public HeroesRanks ksClone()
		{
			return new HeroesRanks(type, ranks);
		}

		public HeroesRanks kdClone()
		{
			HeroesRanks _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popInteger();
			ranks = is.popList(HeroesRank.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(type);
			os.pushList(ranks);
		}

		public int type;
		public List<HeroesRank> ranks;
	}

	public static class HeroesRank implements Stream.IStreamable
	{

		public HeroesRank() { }

		public HeroesRank(int rid, byte type, GlobalRoleID roleID, String roleName, 
		                  String serverName, short headIconId, short lvl, int power, 
		                  int score1, int score2, int score3, short buffLvl, 
		                  short attc, int padid1, int padid2)
		{
			this.rid = rid;
			this.type = type;
			this.roleID = roleID;
			this.roleName = roleName;
			this.serverName = serverName;
			this.headIconId = headIconId;
			this.lvl = lvl;
			this.power = power;
			this.score1 = score1;
			this.score2 = score2;
			this.score3 = score3;
			this.buffLvl = buffLvl;
			this.attc = attc;
			this.padid1 = padid1;
			this.padid2 = padid2;
		}

		public HeroesRank ksClone()
		{
			return new HeroesRank(rid, type, roleID, roleName, 
			                      serverName, headIconId, lvl, power, 
			                      score1, score2, score3, buffLvl, 
			                      attc, padid1, padid2);
		}

		public HeroesRank kdClone()
		{
			HeroesRank _kio_clobj = ksClone();
			_kio_clobj.roleID = roleID.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			type = is.popByte();
			if( roleID == null )
				roleID = new GlobalRoleID();
			is.pop(roleID);
			roleName = is.popString();
			serverName = is.popString();
			headIconId = is.popShort();
			lvl = is.popShort();
			power = is.popInteger();
			score1 = is.popInteger();
			score2 = is.popInteger();
			score3 = is.popInteger();
			buffLvl = is.popShort();
			attc = is.popShort();
			padid1 = is.popInteger();
			padid2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushByte(type);
			os.push(roleID);
			os.pushString(roleName);
			os.pushString(serverName);
			os.pushShort(headIconId);
			os.pushShort(lvl);
			os.pushInteger(power);
			os.pushInteger(score1);
			os.pushInteger(score2);
			os.pushInteger(score3);
			os.pushShort(buffLvl);
			os.pushShort(attc);
			os.pushInteger(padid1);
			os.pushInteger(padid2);
		}

		public int rid;
		public byte type;
		public GlobalRoleID roleID;
		public String roleName;
		public String serverName;
		public short headIconId;
		public short lvl;
		public int power;
		public int score1;
		public int score2;
		public int score3;
		public short buffLvl;
		public short attc;
		public int padid1;
		public int padid2;
	}

	public static class DBHeadIcon implements Stream.IStreamable
	{

		public DBHeadIcon() { }

		public DBHeadIcon(short headcur, List<DBHeadIconList> headicon, int day, short mail, 
		                  int r1, int r2)
		{
			this.headcur = headcur;
			this.headicon = headicon;
			this.day = day;
			this.mail = mail;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBHeadIcon ksClone()
		{
			return new DBHeadIcon(headcur, headicon, day, mail, 
			                      r1, r2);
		}

		public DBHeadIcon kdClone()
		{
			DBHeadIcon _kio_clobj = ksClone();
			_kio_clobj.headicon = new ArrayList<DBHeadIconList>();
			for(DBHeadIconList _kio_iter : headicon)
			{
				_kio_clobj.headicon.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			headcur = is.popShort();
			headicon = is.popList(DBHeadIconList.class);
			day = is.popInteger();
			mail = is.popShort();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(headcur);
			os.pushList(headicon);
			os.pushInteger(day);
			os.pushShort(mail);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public short headcur;
		public List<DBHeadIconList> headicon;
		public int day;
		public short mail;
		public int r1;
		public int r2;
	}

	public static class DBHeadIconList implements Stream.IStreamable
	{

		public DBHeadIconList() { }

		public DBHeadIconList(short headid, int headtime)
		{
			this.headid = headid;
			this.headtime = headtime;
		}

		public DBHeadIconList ksClone()
		{
			return new DBHeadIconList(headid, headtime);
		}

		public DBHeadIconList kdClone()
		{
			DBHeadIconList _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			headid = is.popShort();
			headtime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(headid);
			os.pushInteger(headtime);
		}

		public short headid;
		public int headtime;
	}

	public static class DBHeroesBoss implements Stream.IStreamable
	{

		public DBHeroesBoss() { }

		public DBHeroesBoss(List<HeroesRank> ranks, List<Integer> roles, List<Short> gids1, List<Short> gids2, 
		                    List<Short> gids3, int day, int r1, int r2)
		{
			this.ranks = ranks;
			this.roles = roles;
			this.gids1 = gids1;
			this.gids2 = gids2;
			this.gids3 = gids3;
			this.day = day;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBHeroesBoss ksClone()
		{
			return new DBHeroesBoss(ranks, roles, gids1, gids2, 
			                        gids3, day, r1, r2);
		}

		public DBHeroesBoss kdClone()
		{
			DBHeroesBoss _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.roles = new ArrayList<Integer>();
			for(Integer _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter);
			}
			_kio_clobj.gids1 = new ArrayList<Short>();
			for(Short _kio_iter : gids1)
			{
				_kio_clobj.gids1.add(_kio_iter);
			}
			_kio_clobj.gids2 = new ArrayList<Short>();
			for(Short _kio_iter : gids2)
			{
				_kio_clobj.gids2.add(_kio_iter);
			}
			_kio_clobj.gids3 = new ArrayList<Short>();
			for(Short _kio_iter : gids3)
			{
				_kio_clobj.gids3.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(HeroesRank.class);
			roles = is.popIntegerList();
			gids1 = is.popShortList();
			gids2 = is.popShortList();
			gids3 = is.popShortList();
			day = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
			os.pushIntegerList(roles);
			os.pushShortList(gids1);
			os.pushShortList(gids2);
			os.pushShortList(gids3);
			os.pushInteger(day);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<HeroesRank> ranks;
		public List<Integer> roles;
		public List<Short> gids1;
		public List<Short> gids2;
		public List<Short> gids3;
		public int day;
		public int r1;
		public int r2;
	}

	public static class DBHeroesBossTimes implements Stream.IStreamable
	{

		public DBHeroesBossTimes() { }

		public DBHeroesBossTimes(short id, byte times, byte buytimes, int bonusday, 
		                         int damage)
		{
			this.id = id;
			this.times = times;
			this.buytimes = buytimes;
			this.bonusday = bonusday;
			this.damage = damage;
		}

		public DBHeroesBossTimes ksClone()
		{
			return new DBHeroesBossTimes(id, times, buytimes, bonusday, 
			                             damage);
		}

		public DBHeroesBossTimes kdClone()
		{
			DBHeroesBossTimes _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			times = is.popByte();
			buytimes = is.popByte();
			bonusday = is.popInteger();
			damage = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(times);
			os.pushByte(buytimes);
			os.pushInteger(bonusday);
			os.pushInteger(damage);
		}

		public short id;
		public byte times;
		public byte buytimes;
		public int bonusday;
		public int damage;
	}

	public static class DBHeroesBossInfo implements Stream.IStreamable
	{

		public DBHeroesBossInfo() { }

		public DBHeroesBossInfo(List<HeroesRank> ranks, List<DBHeroesBossTimes> times, short buffCount, int score1, 
		                        int score2, int score3, int day, int r1, 
		                        int r2)
		{
			this.ranks = ranks;
			this.times = times;
			this.buffCount = buffCount;
			this.score1 = score1;
			this.score2 = score2;
			this.score3 = score3;
			this.day = day;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBHeroesBossInfo ksClone()
		{
			return new DBHeroesBossInfo(ranks, times, buffCount, score1, 
			                            score2, score3, day, r1, 
			                            r2);
		}

		public DBHeroesBossInfo kdClone()
		{
			DBHeroesBossInfo _kio_clobj = ksClone();
			_kio_clobj.ranks = new ArrayList<HeroesRank>();
			for(HeroesRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			_kio_clobj.times = new ArrayList<DBHeroesBossTimes>();
			for(DBHeroesBossTimes _kio_iter : times)
			{
				_kio_clobj.times.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ranks = is.popList(HeroesRank.class);
			times = is.popList(DBHeroesBossTimes.class);
			buffCount = is.popShort();
			score1 = is.popInteger();
			score2 = is.popInteger();
			score3 = is.popInteger();
			day = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(ranks);
			os.pushList(times);
			os.pushShort(buffCount);
			os.pushInteger(score1);
			os.pushInteger(score2);
			os.pushInteger(score3);
			os.pushInteger(day);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<HeroesRank> ranks;
		public List<DBHeroesBossTimes> times;
		public short buffCount;
		public int score1;
		public int score2;
		public int score3;
		public int day;
		public int r1;
		public int r2;
	}

	public static class DBMercUsed implements Stream.IStreamable
	{

		public DBMercUsed() { }

		public DBMercUsed(int day, int rid)
		{
			this.day = day;
			this.rid = rid;
		}

		public DBMercUsed ksClone()
		{
			return new DBMercUsed(day, rid);
		}

		public DBMercUsed kdClone()
		{
			DBMercUsed _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			day = is.popInteger();
			rid = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(day);
			os.pushInteger(rid);
		}

		public int day;
		public int rid;
	}

	public static class DBMercGeneral implements Stream.IStreamable
	{

		public DBMercGeneral() { }

		public DBMercGeneral(i3k.DBRoleGeneral general, int time, int mercIncome, List<DBMercUsed> used, 
		                     int r1)
		{
			this.general = general;
			this.time = time;
			this.mercIncome = mercIncome;
			this.used = used;
			this.r1 = r1;
		}

		public DBMercGeneral ksClone()
		{
			return new DBMercGeneral(general, time, mercIncome, used, 
			                         r1);
		}

		public DBMercGeneral kdClone()
		{
			DBMercGeneral _kio_clobj = ksClone();
			_kio_clobj.general = general.kdClone();
			_kio_clobj.used = new ArrayList<DBMercUsed>();
			for(DBMercUsed _kio_iter : used)
			{
				_kio_clobj.used.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( general == null )
				general = new i3k.DBRoleGeneral();
			is.pop(general);
			time = is.popInteger();
			mercIncome = is.popInteger();
			used = is.popList(DBMercUsed.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(general);
			os.pushInteger(time);
			os.pushInteger(mercIncome);
			os.pushList(used);
			os.pushInteger(r1);
		}

		public i3k.DBRoleGeneral general;
		public int time;
		public int mercIncome;
		public List<DBMercUsed> used;
		public int r1;
	}

	public static class DBMarchMercGeneral implements Stream.IStreamable
	{

		public DBMarchMercGeneral() { }

		public DBMarchMercGeneral(int rid, String rname, i3k.DBRoleGeneral general, DBRoleMarchGeneralState status, 
		                          byte type, byte r12, byte r13, byte r14)
		{
			this.rid = rid;
			this.rname = rname;
			this.general = general;
			this.status = status;
			this.type = type;
			this.r12 = r12;
			this.r13 = r13;
			this.r14 = r14;
		}

		public DBMarchMercGeneral ksClone()
		{
			return new DBMarchMercGeneral(rid, rname, general, status, 
			                              type, r12, r13, r14);
		}

		public DBMarchMercGeneral kdClone()
		{
			DBMarchMercGeneral _kio_clobj = ksClone();
			_kio_clobj.general = general.kdClone();
			_kio_clobj.status = status.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname = is.popString();
			if( general == null )
				general = new i3k.DBRoleGeneral();
			is.pop(general);
			if( status == null )
				status = new DBRoleMarchGeneralState();
			is.pop(status);
			type = is.popByte();
			r12 = is.popByte();
			r13 = is.popByte();
			r14 = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname);
			os.push(general);
			os.push(status);
			os.pushByte(type);
			os.pushByte(r12);
			os.pushByte(r13);
			os.pushByte(r14);
		}

		public int rid;
		public String rname;
		public i3k.DBRoleGeneral general;
		public DBRoleMarchGeneralState status;
		public byte type;
		public byte r12;
		public byte r13;
		public byte r14;
	}

	public static class DBRoleMerc implements Stream.IStreamable
	{

		public DBRoleMerc() { }

		public DBRoleMerc(int rid, String rname, List<DBMercGeneral> generals, int r1, 
		                  int r2)
		{
			this.rid = rid;
			this.rname = rname;
			this.generals = generals;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBRoleMerc ksClone()
		{
			return new DBRoleMerc(rid, rname, generals, r1, 
			                      r2);
		}

		public DBRoleMerc kdClone()
		{
			DBRoleMerc _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<DBMercGeneral>();
			for(DBMercGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname = is.popString();
			generals = is.popList(DBMercGeneral.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushList(generals);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int rid;
		public String rname;
		public List<DBMercGeneral> generals;
		public int r1;
		public int r2;
	}

	public static class DBMerc implements Stream.IStreamable
	{

		public DBMerc() { }

		public DBMerc(List<DBRoleMerc> roleMercs, int r1, int r2)
		{
			this.roleMercs = roleMercs;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBMerc ksClone()
		{
			return new DBMerc(roleMercs, r1, r2);
		}

		public DBMerc kdClone()
		{
			DBMerc _kio_clobj = ksClone();
			_kio_clobj.roleMercs = new ArrayList<DBRoleMerc>();
			for(DBRoleMerc _kio_iter : roleMercs)
			{
				_kio_clobj.roleMercs.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			roleMercs = is.popList(DBRoleMerc.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(roleMercs);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBRoleMerc> roleMercs;
		public int r1;
		public int r2;
	}

	public static class DBRoleSura implements Stream.IStreamable
	{

		public DBRoleSura() { }

		public DBRoleSura(byte stage, List<Short> deadGids, List<Short> activatedGids, byte r12, 
		                  byte r13, byte r14, int r2)
		{
			this.stage = stage;
			this.deadGids = deadGids;
			this.activatedGids = activatedGids;
			this.r12 = r12;
			this.r13 = r13;
			this.r14 = r14;
			this.r2 = r2;
		}

		public DBRoleSura ksClone()
		{
			return new DBRoleSura(stage, deadGids, activatedGids, r12, 
			                      r13, r14, r2);
		}

		public DBRoleSura kdClone()
		{
			DBRoleSura _kio_clobj = ksClone();
			_kio_clobj.deadGids = new ArrayList<Short>();
			for(Short _kio_iter : deadGids)
			{
				_kio_clobj.deadGids.add(_kio_iter);
			}
			_kio_clobj.activatedGids = new ArrayList<Short>();
			for(Short _kio_iter : activatedGids)
			{
				_kio_clobj.activatedGids.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			stage = is.popByte();
			deadGids = is.popShortList();
			activatedGids = is.popShortList();
			r12 = is.popByte();
			r13 = is.popByte();
			r14 = is.popByte();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(stage);
			os.pushShortList(deadGids);
			os.pushShortList(activatedGids);
			os.pushByte(r12);
			os.pushByte(r13);
			os.pushByte(r14);
			os.pushInteger(r2);
		}

		public byte stage;
		public List<Short> deadGids;
		public List<Short> activatedGids;
		public byte r12;
		public byte r13;
		public byte r14;
		public int r2;
	}

	public static class DBSuraEnemy implements Stream.IStreamable
	{

		public DBSuraEnemy() { }

		public DBSuraEnemy(int id, short lvl, short headIconID, String name, 
		                   String fname, List<i3k.DBRoleGeneral> generals, List<DBPetBrief> pets)
		{
			this.id = id;
			this.lvl = lvl;
			this.headIconID = headIconID;
			this.name = name;
			this.fname = fname;
			this.generals = generals;
			this.pets = pets;
		}

		public DBSuraEnemy ksClone()
		{
			return new DBSuraEnemy(id, lvl, headIconID, name, 
			                       fname, generals, pets);
		}

		public DBSuraEnemy kdClone()
		{
			DBSuraEnemy _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			lvl = is.popShort();
			headIconID = is.popShort();
			name = is.popString();
			fname = is.popString();
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushShort(lvl);
			os.pushShort(headIconID);
			os.pushString(name);
			os.pushString(fname);
			os.pushList(generals);
			os.pushList(pets);
		}

		public int id;
		public short lvl;
		public short headIconID;
		public String name;
		public String fname;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
	}

	public static class DBSura implements Stream.IStreamable
	{

		public DBSura() { }

		public DBSura(int day, List<Short> gids, List<DBSuraEnemy> enemies, int r1, 
		              int r2)
		{
			this.day = day;
			this.gids = gids;
			this.enemies = enemies;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBSura ksClone()
		{
			return new DBSura(day, gids, enemies, r1, 
			                  r2);
		}

		public DBSura kdClone()
		{
			DBSura _kio_clobj = ksClone();
			_kio_clobj.gids = new ArrayList<Short>();
			for(Short _kio_iter : gids)
			{
				_kio_clobj.gids.add(_kio_iter);
			}
			_kio_clobj.enemies = new ArrayList<DBSuraEnemy>();
			for(DBSuraEnemy _kio_iter : enemies)
			{
				_kio_clobj.enemies.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			day = is.popInteger();
			gids = is.popShortList();
			enemies = is.popList(DBSuraEnemy.class);
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(day);
			os.pushShortList(gids);
			os.pushList(enemies);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public int day;
		public List<Short> gids;
		public List<DBSuraEnemy> enemies;
		public int r1;
		public int r2;
	}

	public static class DBOptionalHotPoint implements Stream.IStreamable
	{

		public DBOptionalHotPoint() { }

		public DBOptionalHotPoint(int sbid, short gid)
		{
			this.sbid = sbid;
			this.gid = gid;
		}

		public DBOptionalHotPoint ksClone()
		{
			return new DBOptionalHotPoint(sbid, gid);
		}

		public DBOptionalHotPoint kdClone()
		{
			DBOptionalHotPoint _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			sbid = is.popInteger();
			gid = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(sbid);
			os.pushShort(gid);
		}

		public int sbid;
		public short gid;
	}

	public static class DBForceSWarBuf implements Stream.IStreamable
	{

		public DBForceSWarBuf() { }

		public DBForceSWarBuf(short id, byte type, float value)
		{
			this.id = id;
			this.type = type;
			this.value = value;
		}

		public DBForceSWarBuf ksClone()
		{
			return new DBForceSWarBuf(id, type, value);
		}

		public DBForceSWarBuf kdClone()
		{
			DBForceSWarBuf _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			type = is.popByte();
			value = is.popFloat();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(type);
			os.pushFloat(value);
		}

		public short id;
		public byte type;
		public float value;
	}

	public static class DBForceSWarMatchRecord implements Stream.IStreamable
	{

		public DBForceSWarMatchRecord() { }

		public DBForceSWarMatchRecord(int time, String name1, String name2, short lvl1, 
		                              short lvl2, short icon1, short icon2, byte result, 
		                              List<i3k.DBRoleGeneral> generals1, List<i3k.DBRoleGeneral> generals2, List<DBPetBrief> pets1, List<DBPetBrief> pets2, 
		                              List<DBRelation> relation1, List<DBRelation> relation2, List<DBGeneralStone> gStones1, List<DBGeneralStone> gStones2, 
		                              int seed, List<DBForceSWarBuf> bufs1, List<DBForceSWarBuf> bufs2, int r1)
		{
			this.time = time;
			this.name1 = name1;
			this.name2 = name2;
			this.lvl1 = lvl1;
			this.lvl2 = lvl2;
			this.icon1 = icon1;
			this.icon2 = icon2;
			this.result = result;
			this.generals1 = generals1;
			this.generals2 = generals2;
			this.pets1 = pets1;
			this.pets2 = pets2;
			this.relation1 = relation1;
			this.relation2 = relation2;
			this.gStones1 = gStones1;
			this.gStones2 = gStones2;
			this.seed = seed;
			this.bufs1 = bufs1;
			this.bufs2 = bufs2;
			this.r1 = r1;
		}

		public DBForceSWarMatchRecord ksClone()
		{
			return new DBForceSWarMatchRecord(time, name1, name2, lvl1, 
			                                  lvl2, icon1, icon2, result, 
			                                  generals1, generals2, pets1, pets2, 
			                                  relation1, relation2, gStones1, gStones2, 
			                                  seed, bufs1, bufs2, r1);
		}

		public DBForceSWarMatchRecord kdClone()
		{
			DBForceSWarMatchRecord _kio_clobj = ksClone();
			_kio_clobj.generals1 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals1)
			{
				_kio_clobj.generals1.add(_kio_iter.kdClone());
			}
			_kio_clobj.generals2 = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals2)
			{
				_kio_clobj.generals2.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets1 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets1)
			{
				_kio_clobj.pets1.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets2 = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets2)
			{
				_kio_clobj.pets2.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation1 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation1)
			{
				_kio_clobj.relation1.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation2 = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation2)
			{
				_kio_clobj.relation2.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones1 = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones1)
			{
				_kio_clobj.gStones1.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones2 = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones2)
			{
				_kio_clobj.gStones2.add(_kio_iter.kdClone());
			}
			_kio_clobj.bufs1 = new ArrayList<DBForceSWarBuf>();
			for(DBForceSWarBuf _kio_iter : bufs1)
			{
				_kio_clobj.bufs1.add(_kio_iter.kdClone());
			}
			_kio_clobj.bufs2 = new ArrayList<DBForceSWarBuf>();
			for(DBForceSWarBuf _kio_iter : bufs2)
			{
				_kio_clobj.bufs2.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			time = is.popInteger();
			name1 = is.popString();
			name2 = is.popString();
			lvl1 = is.popShort();
			lvl2 = is.popShort();
			icon1 = is.popShort();
			icon2 = is.popShort();
			result = is.popByte();
			generals1 = is.popList(i3k.DBRoleGeneral.class);
			generals2 = is.popList(i3k.DBRoleGeneral.class);
			pets1 = is.popList(DBPetBrief.class);
			pets2 = is.popList(DBPetBrief.class);
			relation1 = is.popList(DBRelation.class);
			relation2 = is.popList(DBRelation.class);
			gStones1 = is.popList(DBGeneralStone.class);
			gStones2 = is.popList(DBGeneralStone.class);
			seed = is.popInteger();
			bufs1 = is.popList(DBForceSWarBuf.class);
			bufs2 = is.popList(DBForceSWarBuf.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(time);
			os.pushString(name1);
			os.pushString(name2);
			os.pushShort(lvl1);
			os.pushShort(lvl2);
			os.pushShort(icon1);
			os.pushShort(icon2);
			os.pushByte(result);
			os.pushList(generals1);
			os.pushList(generals2);
			os.pushList(pets1);
			os.pushList(pets2);
			os.pushList(relation1);
			os.pushList(relation2);
			os.pushList(gStones1);
			os.pushList(gStones2);
			os.pushInteger(seed);
			os.pushList(bufs1);
			os.pushList(bufs2);
			os.pushInteger(r1);
		}

		public int time;
		public String name1;
		public String name2;
		public short lvl1;
		public short lvl2;
		public short icon1;
		public short icon2;
		public byte result;
		public List<i3k.DBRoleGeneral> generals1;
		public List<i3k.DBRoleGeneral> generals2;
		public List<DBPetBrief> pets1;
		public List<DBPetBrief> pets2;
		public List<DBRelation> relation1;
		public List<DBRelation> relation2;
		public List<DBGeneralStone> gStones1;
		public List<DBGeneralStone> gStones2;
		public int seed;
		public List<DBForceSWarBuf> bufs1;
		public List<DBForceSWarBuf> bufs2;
		public int r1;
	}

	public static class DBForceSWarTeam implements Stream.IStreamable
	{

		public DBForceSWarTeam() { }

		public DBForceSWarTeam(int id, String name, short icon, short level, 
		                       int power, int status, short winTimes, List<i3k.DBRoleGeneral> generals, 
		                       List<DBPetBrief> pets, List<DBRelation> relation, List<DBGeneralStone> gStones, List<DBForceSWarMatchRecord> records, 
		                       int r1)
		{
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.level = level;
			this.power = power;
			this.status = status;
			this.winTimes = winTimes;
			this.generals = generals;
			this.pets = pets;
			this.relation = relation;
			this.gStones = gStones;
			this.records = records;
			this.r1 = r1;
		}

		public DBForceSWarTeam ksClone()
		{
			return new DBForceSWarTeam(id, name, icon, level, 
			                           power, status, winTimes, generals, 
			                           pets, relation, gStones, records, 
			                           r1);
		}

		public DBForceSWarTeam kdClone()
		{
			DBForceSWarTeam _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<i3k.DBRoleGeneral>();
			for(i3k.DBRoleGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pets = new ArrayList<DBPetBrief>();
			for(DBPetBrief _kio_iter : pets)
			{
				_kio_clobj.pets.add(_kio_iter.kdClone());
			}
			_kio_clobj.relation = new ArrayList<DBRelation>();
			for(DBRelation _kio_iter : relation)
			{
				_kio_clobj.relation.add(_kio_iter.kdClone());
			}
			_kio_clobj.gStones = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : gStones)
			{
				_kio_clobj.gStones.add(_kio_iter.kdClone());
			}
			_kio_clobj.records = new ArrayList<DBForceSWarMatchRecord>();
			for(DBForceSWarMatchRecord _kio_iter : records)
			{
				_kio_clobj.records.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			name = is.popString();
			icon = is.popShort();
			level = is.popShort();
			power = is.popInteger();
			status = is.popInteger();
			winTimes = is.popShort();
			generals = is.popList(i3k.DBRoleGeneral.class);
			pets = is.popList(DBPetBrief.class);
			relation = is.popList(DBRelation.class);
			gStones = is.popList(DBGeneralStone.class);
			records = is.popList(DBForceSWarMatchRecord.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushString(name);
			os.pushShort(icon);
			os.pushShort(level);
			os.pushInteger(power);
			os.pushInteger(status);
			os.pushShort(winTimes);
			os.pushList(generals);
			os.pushList(pets);
			os.pushList(relation);
			os.pushList(gStones);
			os.pushList(records);
			os.pushInteger(r1);
		}

		public int id;
		public String name;
		public short icon;
		public short level;
		public int power;
		public int status;
		public short winTimes;
		public List<i3k.DBRoleGeneral> generals;
		public List<DBPetBrief> pets;
		public List<DBRelation> relation;
		public List<DBGeneralStone> gStones;
		public List<DBForceSWarMatchRecord> records;
		public int r1;
	}

	public static class DBForceSWarBuilding implements Stream.IStreamable
	{

		public DBForceSWarBuilding() { }

		public DBForceSWarBuilding(byte bid, List<DBForceSWarTeam> teams, int r1)
		{
			this.bid = bid;
			this.teams = teams;
			this.r1 = r1;
		}

		public DBForceSWarBuilding ksClone()
		{
			return new DBForceSWarBuilding(bid, teams, r1);
		}

		public DBForceSWarBuilding kdClone()
		{
			DBForceSWarBuilding _kio_clobj = ksClone();
			_kio_clobj.teams = new ArrayList<DBForceSWarTeam>();
			for(DBForceSWarTeam _kio_iter : teams)
			{
				_kio_clobj.teams.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			bid = is.popByte();
			teams = is.popList(DBForceSWarTeam.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(bid);
			os.pushList(teams);
			os.pushInteger(r1);
		}

		public byte bid;
		public List<DBForceSWarTeam> teams;
		public int r1;
	}

	public static class DBForceSWarLog implements Stream.IStreamable
	{

		public DBForceSWarLog() { }

		public DBForceSWarLog(byte type, int time, int result, String oppoServerName, 
		                      String oppoName, short dead, short oppoDead, int r1)
		{
			this.type = type;
			this.time = time;
			this.result = result;
			this.oppoServerName = oppoServerName;
			this.oppoName = oppoName;
			this.dead = dead;
			this.oppoDead = oppoDead;
			this.r1 = r1;
		}

		public DBForceSWarLog ksClone()
		{
			return new DBForceSWarLog(type, time, result, oppoServerName, 
			                          oppoName, dead, oppoDead, r1);
		}

		public DBForceSWarLog kdClone()
		{
			DBForceSWarLog _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			type = is.popByte();
			time = is.popInteger();
			result = is.popInteger();
			oppoServerName = is.popString();
			oppoName = is.popString();
			dead = is.popShort();
			oppoDead = is.popShort();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(type);
			os.pushInteger(time);
			os.pushInteger(result);
			os.pushString(oppoServerName);
			os.pushString(oppoName);
			os.pushShort(dead);
			os.pushShort(oppoDead);
			os.pushInteger(r1);
		}

		public byte type;
		public int time;
		public int result;
		public String oppoServerName;
		public String oppoName;
		public short dead;
		public short oppoDead;
		public int r1;
	}

	public static class DBForceSWarBrief implements Stream.IStreamable
	{

		public DBForceSWarBrief() { }

		public DBForceSWarBrief(int fid, short iconId, String fname, short lvl, 
		                        int exp, int winTimes, int serverId, String serverName, 
		                        List<DBForceSWarBuilding> buildings, int r1)
		{
			this.fid = fid;
			this.iconId = iconId;
			this.fname = fname;
			this.lvl = lvl;
			this.exp = exp;
			this.winTimes = winTimes;
			this.serverId = serverId;
			this.serverName = serverName;
			this.buildings = buildings;
			this.r1 = r1;
		}

		public DBForceSWarBrief ksClone()
		{
			return new DBForceSWarBrief(fid, iconId, fname, lvl, 
			                            exp, winTimes, serverId, serverName, 
			                            buildings, r1);
		}

		public DBForceSWarBrief kdClone()
		{
			DBForceSWarBrief _kio_clobj = ksClone();
			_kio_clobj.buildings = new ArrayList<DBForceSWarBuilding>();
			for(DBForceSWarBuilding _kio_iter : buildings)
			{
				_kio_clobj.buildings.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fid = is.popInteger();
			iconId = is.popShort();
			fname = is.popString();
			lvl = is.popShort();
			exp = is.popInteger();
			winTimes = is.popInteger();
			serverId = is.popInteger();
			serverName = is.popString();
			buildings = is.popList(DBForceSWarBuilding.class);
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(fid);
			os.pushShort(iconId);
			os.pushString(fname);
			os.pushShort(lvl);
			os.pushInteger(exp);
			os.pushInteger(winTimes);
			os.pushInteger(serverId);
			os.pushString(serverName);
			os.pushList(buildings);
			os.pushInteger(r1);
		}

		public int fid;
		public short iconId;
		public String fname;
		public short lvl;
		public int exp;
		public int winTimes;
		public int serverId;
		public String serverName;
		public List<DBForceSWarBuilding> buildings;
		public int r1;
	}

	public static class DBForceSWarOppo implements Stream.IStreamable
	{

		public DBForceSWarOppo() { }

		public DBForceSWarOppo(DBForceSWarBrief oppoBriefs, List<Integer> votes, int r1)
		{
			this.oppoBriefs = oppoBriefs;
			this.votes = votes;
			this.r1 = r1;
		}

		public DBForceSWarOppo ksClone()
		{
			return new DBForceSWarOppo(oppoBriefs, votes, r1);
		}

		public DBForceSWarOppo kdClone()
		{
			DBForceSWarOppo _kio_clobj = ksClone();
			_kio_clobj.oppoBriefs = oppoBriefs.kdClone();
			_kio_clobj.votes = new ArrayList<Integer>();
			for(Integer _kio_iter : votes)
			{
				_kio_clobj.votes.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( oppoBriefs == null )
				oppoBriefs = new DBForceSWarBrief();
			is.pop(oppoBriefs);
			votes = is.popIntegerList();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(oppoBriefs);
			os.pushIntegerList(votes);
			os.pushInteger(r1);
		}

		public DBForceSWarBrief oppoBriefs;
		public List<Integer> votes;
		public int r1;
	}

	public static class DBForceSWarRole implements Stream.IStreamable
	{

		public DBForceSWarRole() { }

		public DBForceSWarRole(int rid, List<Short> usedGIds, List<Short> usedPIds, int r1)
		{
			this.rid = rid;
			this.usedGIds = usedGIds;
			this.usedPIds = usedPIds;
			this.r1 = r1;
		}

		public DBForceSWarRole ksClone()
		{
			return new DBForceSWarRole(rid, usedGIds, usedPIds, r1);
		}

		public DBForceSWarRole kdClone()
		{
			DBForceSWarRole _kio_clobj = ksClone();
			_kio_clobj.usedGIds = new ArrayList<Short>();
			for(Short _kio_iter : usedGIds)
			{
				_kio_clobj.usedGIds.add(_kio_iter);
			}
			_kio_clobj.usedPIds = new ArrayList<Short>();
			for(Short _kio_iter : usedPIds)
			{
				_kio_clobj.usedPIds.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			usedGIds = is.popShortList();
			usedPIds = is.popShortList();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushShortList(usedGIds);
			os.pushShortList(usedPIds);
			os.pushInteger(r1);
		}

		public int rid;
		public List<Short> usedGIds;
		public List<Short> usedPIds;
		public int r1;
	}

	public static class DBForceSWarDonateRank implements Stream.IStreamable
	{

		public DBForceSWarDonateRank() { }

		public DBForceSWarDonateRank(int rid, String rname, short iconId, short lvl, 
		                             int donate, int totalDonate, int r1)
		{
			this.rid = rid;
			this.rname = rname;
			this.iconId = iconId;
			this.lvl = lvl;
			this.donate = donate;
			this.totalDonate = totalDonate;
			this.r1 = r1;
		}

		public DBForceSWarDonateRank ksClone()
		{
			return new DBForceSWarDonateRank(rid, rname, iconId, lvl, 
			                                 donate, totalDonate, r1);
		}

		public DBForceSWarDonateRank kdClone()
		{
			DBForceSWarDonateRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname = is.popString();
			iconId = is.popShort();
			lvl = is.popShort();
			donate = is.popInteger();
			totalDonate = is.popInteger();
			r1 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushShort(iconId);
			os.pushShort(lvl);
			os.pushInteger(donate);
			os.pushInteger(totalDonate);
			os.pushInteger(r1);
		}

		public int rid;
		public String rname;
		public short iconId;
		public short lvl;
		public int donate;
		public int totalDonate;
		public int r1;
	}

	public static class DBForceSWar implements Stream.IStreamable
	{

		public DBForceSWar() { }

		public DBForceSWar(DBForceSWarBrief brief, List<DBForceSWarOppo> opponents, List<DBForceSWarOppo> attacked, int attackDay, 
		                   int defendDay, int attackBadges, List<DBForceSWarLog> logs, List<DBForceSWarRole> roles, 
		                   int protectDays, int loseTimes, short weakBufId, List<DBForceSWarDonateRank> donateRanks, 
		                   byte r12, byte r13, byte r14, int r2)
		{
			this.brief = brief;
			this.opponents = opponents;
			this.attacked = attacked;
			this.attackDay = attackDay;
			this.defendDay = defendDay;
			this.attackBadges = attackBadges;
			this.logs = logs;
			this.roles = roles;
			this.protectDays = protectDays;
			this.loseTimes = loseTimes;
			this.weakBufId = weakBufId;
			this.donateRanks = donateRanks;
			this.r12 = r12;
			this.r13 = r13;
			this.r14 = r14;
			this.r2 = r2;
		}

		public DBForceSWar ksClone()
		{
			return new DBForceSWar(brief, opponents, attacked, attackDay, 
			                       defendDay, attackBadges, logs, roles, 
			                       protectDays, loseTimes, weakBufId, donateRanks, 
			                       r12, r13, r14, r2);
		}

		public DBForceSWar kdClone()
		{
			DBForceSWar _kio_clobj = ksClone();
			_kio_clobj.brief = brief.kdClone();
			_kio_clobj.opponents = new ArrayList<DBForceSWarOppo>();
			for(DBForceSWarOppo _kio_iter : opponents)
			{
				_kio_clobj.opponents.add(_kio_iter.kdClone());
			}
			_kio_clobj.attacked = new ArrayList<DBForceSWarOppo>();
			for(DBForceSWarOppo _kio_iter : attacked)
			{
				_kio_clobj.attacked.add(_kio_iter.kdClone());
			}
			_kio_clobj.logs = new ArrayList<DBForceSWarLog>();
			for(DBForceSWarLog _kio_iter : logs)
			{
				_kio_clobj.logs.add(_kio_iter.kdClone());
			}
			_kio_clobj.roles = new ArrayList<DBForceSWarRole>();
			for(DBForceSWarRole _kio_iter : roles)
			{
				_kio_clobj.roles.add(_kio_iter.kdClone());
			}
			_kio_clobj.donateRanks = new ArrayList<DBForceSWarDonateRank>();
			for(DBForceSWarDonateRank _kio_iter : donateRanks)
			{
				_kio_clobj.donateRanks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( brief == null )
				brief = new DBForceSWarBrief();
			is.pop(brief);
			opponents = is.popList(DBForceSWarOppo.class);
			attacked = is.popList(DBForceSWarOppo.class);
			attackDay = is.popInteger();
			defendDay = is.popInteger();
			attackBadges = is.popInteger();
			logs = is.popList(DBForceSWarLog.class);
			roles = is.popList(DBForceSWarRole.class);
			protectDays = is.popInteger();
			loseTimes = is.popInteger();
			weakBufId = is.popShort();
			donateRanks = is.popList(DBForceSWarDonateRank.class);
			r12 = is.popByte();
			r13 = is.popByte();
			r14 = is.popByte();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(brief);
			os.pushList(opponents);
			os.pushList(attacked);
			os.pushInteger(attackDay);
			os.pushInteger(defendDay);
			os.pushInteger(attackBadges);
			os.pushList(logs);
			os.pushList(roles);
			os.pushInteger(protectDays);
			os.pushInteger(loseTimes);
			os.pushShort(weakBufId);
			os.pushList(donateRanks);
			os.pushByte(r12);
			os.pushByte(r13);
			os.pushByte(r14);
			os.pushInteger(r2);
		}

		public DBForceSWarBrief brief;
		public List<DBForceSWarOppo> opponents;
		public List<DBForceSWarOppo> attacked;
		public int attackDay;
		public int defendDay;
		public int attackBadges;
		public List<DBForceSWarLog> logs;
		public List<DBForceSWarRole> roles;
		public int protectDays;
		public int loseTimes;
		public short weakBufId;
		public List<DBForceSWarDonateRank> donateRanks;
		public byte r12;
		public byte r13;
		public byte r14;
		public int r2;
	}

	public static class DBForceSWarRank implements Stream.IStreamable
	{

		public DBForceSWarRank() { }

		public DBForceSWarRank(String serverName, int fid, String fname, short iconId, 
		                       short lvl, int score)
		{
			this.serverName = serverName;
			this.fid = fid;
			this.fname = fname;
			this.iconId = iconId;
			this.lvl = lvl;
			this.score = score;
		}

		public DBForceSWarRank ksClone()
		{
			return new DBForceSWarRank(serverName, fid, fname, iconId, 
			                           lvl, score);
		}

		public DBForceSWarRank kdClone()
		{
			DBForceSWarRank _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			serverName = is.popString();
			fid = is.popInteger();
			fname = is.popString();
			iconId = is.popShort();
			lvl = is.popShort();
			score = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(serverName);
			os.pushInteger(fid);
			os.pushString(fname);
			os.pushShort(iconId);
			os.pushShort(lvl);
			os.pushInteger(score);
		}

		public String serverName;
		public int fid;
		public String fname;
		public short iconId;
		public short lvl;
		public int score;
	}

	public static class DBForceSWarGlobal implements Stream.IStreamable
	{

		public DBForceSWarGlobal() { }

		public DBForceSWarGlobal(List<DBForceSWar> swar, List<DBForceSWarRank> ranks, int day, int r1, 
		                         int r2)
		{
			this.swar = swar;
			this.ranks = ranks;
			this.day = day;
			this.r1 = r1;
			this.r2 = r2;
		}

		public DBForceSWarGlobal ksClone()
		{
			return new DBForceSWarGlobal(swar, ranks, day, r1, 
			                             r2);
		}

		public DBForceSWarGlobal kdClone()
		{
			DBForceSWarGlobal _kio_clobj = ksClone();
			_kio_clobj.swar = new ArrayList<DBForceSWar>();
			for(DBForceSWar _kio_iter : swar)
			{
				_kio_clobj.swar.add(_kio_iter.kdClone());
			}
			_kio_clobj.ranks = new ArrayList<DBForceSWarRank>();
			for(DBForceSWarRank _kio_iter : ranks)
			{
				_kio_clobj.ranks.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			swar = is.popList(DBForceSWar.class);
			ranks = is.popList(DBForceSWarRank.class);
			day = is.popInteger();
			r1 = is.popInteger();
			r2 = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(swar);
			os.pushList(ranks);
			os.pushInteger(day);
			os.pushInteger(r1);
			os.pushInteger(r2);
		}

		public List<DBForceSWar> swar;
		public List<DBForceSWarRank> ranks;
		public int day;
		public int r1;
		public int r2;
	}

	public static class DBTreasureMap implements Stream.IStreamable
	{

		public DBTreasureMap() { }

		public DBTreasureMap(byte id, int rid, String rname, List<Integer> diggers, 
		                     List<DBTreaureReward> rewards, List<Byte> openedCells, byte treasureCell, byte otherDigCnt, 
		                     int createTime)
		{
			this.id = id;
			this.rid = rid;
			this.rname = rname;
			this.diggers = diggers;
			this.rewards = rewards;
			this.openedCells = openedCells;
			this.treasureCell = treasureCell;
			this.otherDigCnt = otherDigCnt;
			this.createTime = createTime;
		}

		public DBTreasureMap ksClone()
		{
			return new DBTreasureMap(id, rid, rname, diggers, 
			                         rewards, openedCells, treasureCell, otherDigCnt, 
			                         createTime);
		}

		public DBTreasureMap kdClone()
		{
			DBTreasureMap _kio_clobj = ksClone();
			_kio_clobj.diggers = new ArrayList<Integer>();
			for(Integer _kio_iter : diggers)
			{
				_kio_clobj.diggers.add(_kio_iter);
			}
			_kio_clobj.rewards = new ArrayList<DBTreaureReward>();
			for(DBTreaureReward _kio_iter : rewards)
			{
				_kio_clobj.rewards.add(_kio_iter.kdClone());
			}
			_kio_clobj.openedCells = new ArrayList<Byte>();
			for(Byte _kio_iter : openedCells)
			{
				_kio_clobj.openedCells.add(_kio_iter);
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			rid = is.popInteger();
			rname = is.popString();
			diggers = is.popIntegerList();
			rewards = is.popList(DBTreaureReward.class);
			openedCells = is.popByteList();
			treasureCell = is.popByte();
			otherDigCnt = is.popByte();
			createTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushIntegerList(diggers);
			os.pushList(rewards);
			os.pushByteList(openedCells);
			os.pushByte(treasureCell);
			os.pushByte(otherDigCnt);
			os.pushInteger(createTime);
		}

		public byte id;
		public int rid;
		public String rname;
		public List<Integer> diggers;
		public List<DBTreaureReward> rewards;
		public List<Byte> openedCells;
		public byte treasureCell;
		public byte otherDigCnt;
		public int createTime;
	}

	public static class DBTreasure implements Stream.IStreamable
	{

		public DBTreasure() { }

		public DBTreasure(List<DBTreasureMap> treasureMaps)
		{
			this.treasureMaps = treasureMaps;
		}

		public DBTreasure ksClone()
		{
			return new DBTreasure(treasureMaps);
		}

		public DBTreasure kdClone()
		{
			DBTreasure _kio_clobj = ksClone();
			_kio_clobj.treasureMaps = new ArrayList<DBTreasureMap>();
			for(DBTreasureMap _kio_iter : treasureMaps)
			{
				_kio_clobj.treasureMaps.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			treasureMaps = is.popList(DBTreasureMap.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(treasureMaps);
		}

		public List<DBTreasureMap> treasureMaps;
	}

	public static class DBTreaureReward implements Stream.IStreamable
	{

		public DBTreaureReward() { }

		public DBTreaureReward(byte num, DropEntry dropEntry, byte isOpen, String diggerName)
		{
			this.num = num;
			this.dropEntry = dropEntry;
			this.isOpen = isOpen;
			this.diggerName = diggerName;
		}

		public DBTreaureReward ksClone()
		{
			return new DBTreaureReward(num, dropEntry, isOpen, diggerName);
		}

		public DBTreaureReward kdClone()
		{
			DBTreaureReward _kio_clobj = ksClone();
			_kio_clobj.dropEntry = dropEntry.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			num = is.popByte();
			if( dropEntry == null )
				dropEntry = new DropEntry();
			is.pop(dropEntry);
			isOpen = is.popByte();
			diggerName = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(num);
			os.push(dropEntry);
			os.pushByte(isOpen);
			os.pushString(diggerName);
		}

		public byte num;
		public DropEntry dropEntry;
		public byte isOpen;
		public String diggerName;
	}

	public static class DBTreasureLimitCnt implements Stream.IStreamable
	{

		public DBTreasureLimitCnt() { }

		public DBTreasureLimitCnt(byte useTreasureCnt, byte digOtherCnt, byte isTimeCntAdd, int lastAddTime)
		{
			this.useTreasureCnt = useTreasureCnt;
			this.digOtherCnt = digOtherCnt;
			this.isTimeCntAdd = isTimeCntAdd;
			this.lastAddTime = lastAddTime;
		}

		public DBTreasureLimitCnt ksClone()
		{
			return new DBTreasureLimitCnt(useTreasureCnt, digOtherCnt, isTimeCntAdd, lastAddTime);
		}

		public DBTreasureLimitCnt kdClone()
		{
			DBTreasureLimitCnt _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			useTreasureCnt = is.popByte();
			digOtherCnt = is.popByte();
			isTimeCntAdd = is.popByte();
			lastAddTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(useTreasureCnt);
			os.pushByte(digOtherCnt);
			os.pushByte(isTimeCntAdd);
			os.pushInteger(lastAddTime);
		}

		public byte useTreasureCnt;
		public byte digOtherCnt;
		public byte isTimeCntAdd;
		public int lastAddTime;
	}

	public static class DBBeMonsterAttacker implements Stream.IStreamable
	{

		public DBBeMonsterAttacker() { }

		public DBBeMonsterAttacker(int rid, String rname, int attackValue, byte attackCnt)
		{
			this.rid = rid;
			this.rname = rname;
			this.attackValue = attackValue;
			this.attackCnt = attackCnt;
		}

		public DBBeMonsterAttacker ksClone()
		{
			return new DBBeMonsterAttacker(rid, rname, attackValue, attackCnt);
		}

		public DBBeMonsterAttacker kdClone()
		{
			DBBeMonsterAttacker _kio_clobj = ksClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname = is.popString();
			attackValue = is.popInteger();
			attackCnt = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushInteger(attackValue);
			os.pushByte(attackCnt);
		}

		public int rid;
		public String rname;
		public int attackValue;
		public byte attackCnt;
	}

	public static class DBBeMonsterGeneral implements Stream.IStreamable
	{

		public DBBeMonsterGeneral() { }

		public DBBeMonsterGeneral(short gid, i3k.DBRoleGeneral general, int baseHp, int hp)
		{
			this.gid = gid;
			this.general = general;
			this.baseHp = baseHp;
			this.hp = hp;
		}

		public DBBeMonsterGeneral ksClone()
		{
			return new DBBeMonsterGeneral(gid, general, baseHp, hp);
		}

		public DBBeMonsterGeneral kdClone()
		{
			DBBeMonsterGeneral _kio_clobj = ksClone();
			_kio_clobj.general = general.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			gid = is.popShort();
			if( general == null )
				general = new i3k.DBRoleGeneral();
			is.pop(general);
			baseHp = is.popInteger();
			hp = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(gid);
			os.push(general);
			os.pushInteger(baseHp);
			os.pushInteger(hp);
		}

		public short gid;
		public i3k.DBRoleGeneral general;
		public int baseHp;
		public int hp;
	}

	public static class DBBeMonsterLineup implements Stream.IStreamable
	{

		public DBBeMonsterLineup() { }

		public DBBeMonsterLineup(byte id, List<DBBeMonsterGeneral> generals, DBPetBrief pet, short hpPercent, 
		                         short upHpCnt)
		{
			this.id = id;
			this.generals = generals;
			this.pet = pet;
			this.hpPercent = hpPercent;
			this.upHpCnt = upHpCnt;
		}

		public DBBeMonsterLineup ksClone()
		{
			return new DBBeMonsterLineup(id, generals, pet, hpPercent, 
			                             upHpCnt);
		}

		public DBBeMonsterLineup kdClone()
		{
			DBBeMonsterLineup _kio_clobj = ksClone();
			_kio_clobj.generals = new ArrayList<DBBeMonsterGeneral>();
			for(DBBeMonsterGeneral _kio_iter : generals)
			{
				_kio_clobj.generals.add(_kio_iter.kdClone());
			}
			_kio_clobj.pet = pet.kdClone();
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popByte();
			generals = is.popList(DBBeMonsterGeneral.class);
			if( pet == null )
				pet = new DBPetBrief();
			is.pop(pet);
			hpPercent = is.popShort();
			upHpCnt = is.popShort();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushByte(id);
			os.pushList(generals);
			os.push(pet);
			os.pushShort(hpPercent);
			os.pushShort(upHpCnt);
		}

		public byte id;
		public List<DBBeMonsterGeneral> generals;
		public DBPetBrief pet;
		public short hpPercent;
		public short upHpCnt;
	}

	public static class DBBeMonsterBoss implements Stream.IStreamable
	{

		public DBBeMonsterBoss() { }

		public DBBeMonsterBoss(int rid, String rname, List<DBBeMonsterLineup> lineup, int bidPirce, 
		                       int killCnt, int bidTime)
		{
			this.rid = rid;
			this.rname = rname;
			this.lineup = lineup;
			this.bidPirce = bidPirce;
			this.killCnt = killCnt;
			this.bidTime = bidTime;
		}

		public DBBeMonsterBoss ksClone()
		{
			return new DBBeMonsterBoss(rid, rname, lineup, bidPirce, 
			                           killCnt, bidTime);
		}

		public DBBeMonsterBoss kdClone()
		{
			DBBeMonsterBoss _kio_clobj = ksClone();
			_kio_clobj.lineup = new ArrayList<DBBeMonsterLineup>();
			for(DBBeMonsterLineup _kio_iter : lineup)
			{
				_kio_clobj.lineup.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			rid = is.popInteger();
			rname = is.popString();
			lineup = is.popList(DBBeMonsterLineup.class);
			bidPirce = is.popInteger();
			killCnt = is.popInteger();
			bidTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(rid);
			os.pushString(rname);
			os.pushList(lineup);
			os.pushInteger(bidPirce);
			os.pushInteger(killCnt);
			os.pushInteger(bidTime);
		}

		public int rid;
		public String rname;
		public List<DBBeMonsterLineup> lineup;
		public int bidPirce;
		public int killCnt;
		public int bidTime;
	}

	public static class DBBeMonster implements Stream.IStreamable
	{

		public DBBeMonster() { }

		public DBBeMonster(List<DBBeMonsterBoss> boss, List<DBBeMonsterAttacker> attacker)
		{
			this.boss = boss;
			this.attacker = attacker;
		}

		public DBBeMonster ksClone()
		{
			return new DBBeMonster(boss, attacker);
		}

		public DBBeMonster kdClone()
		{
			DBBeMonster _kio_clobj = ksClone();
			_kio_clobj.boss = new ArrayList<DBBeMonsterBoss>();
			for(DBBeMonsterBoss _kio_iter : boss)
			{
				_kio_clobj.boss.add(_kio_iter.kdClone());
			}
			_kio_clobj.attacker = new ArrayList<DBBeMonsterAttacker>();
			for(DBBeMonsterAttacker _kio_iter : attacker)
			{
				_kio_clobj.attacker.add(_kio_iter.kdClone());
			}
			return _kio_clobj;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			boss = is.popList(DBBeMonsterBoss.class);
			attacker = is.popList(DBBeMonsterAttacker.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(boss);
			os.pushList(attacker);
		}

		public List<DBBeMonsterBoss> boss;
		public List<DBBeMonsterAttacker> attacker;
	}

}

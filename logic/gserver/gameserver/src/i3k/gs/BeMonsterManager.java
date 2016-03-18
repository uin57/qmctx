package i3k.gs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import i3k.DBMail;
import i3k.DBRole;
import i3k.SBean;
import i3k.SBean.BeMonsterCFGS;
import i3k.SBean.BeMonsterReward;
import i3k.SBean.DBBeMonsterAttacker;
import i3k.SBean.DBBeMonsterBoss;
import i3k.SBean.DBBeMonsterGeneral;
import i3k.SBean.DBBeMonsterLineup;
import ket.kdb.Table;
import ket.kdb.TableEntry;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.util.Stream;

public class BeMonsterManager {
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBBeMonster beMonster)
		{
			this.beMonster = beMonster;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("beMonster");
			byte[] data = Stream.encodeLE(beMonster);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("beMonster save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBBeMonster beMonster;
	}
	
	public BeMonsterManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBBeMonster beMonster)
	{
		this.beMonster = beMonster;
		if (beMonster == null) {
			this.beMonster = new SBean.DBBeMonster();
			this.beMonster.boss = new ArrayList<SBean.DBBeMonsterBoss>();
			this.beMonster.attacker = new ArrayList<SBean.DBBeMonsterAttacker>();
		} else {
			bossMap.clear();
			attackerMap.clear();
			for (SBean.DBBeMonsterBoss boss : this.beMonster.boss) {
				bossMap.put(boss.rid, boss);
			}
			for (SBean.DBBeMonsterAttacker attacker : this.beMonster.attacker) {
			    attackerMap.put(attacker.rid, attacker);
            }
		}
	}
	
	public void onTimer()
	{
	    if (this.state == (byte)0) {
	        this.state = refreshState();
	    }
	    byte newState = refreshState();
	    if (state == STATE_NORMAL && newState == STATE_REGISTER) { //娲诲姩寮�锛屾竻绌轰箣鍓嶆暟鎹�
	        state = newState;
	        bossMap.clear();
	        attackerMap.clear();
	        nowBoss = null;
	    } else if (state == STATE_REGISTER && newState == STATE_REGISTER_END) { //绔炴媿缁撴潫锛岄�鍑篵oss锛岄�杩橀捇鐭�
	        state = newState;
	        if (bossMap.size() > 0) {
	            nowBoss = getTopOneBoss();
	            for (DBBeMonsterBoss boss : bossMap.values()) {
	                if (nowBoss != null && nowBoss.rid != boss.rid) {
	                    List<SBean.DropEntryNew> addList = new ArrayList<SBean.DropEntryNew>();
	                    addList.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, boss.bidPirce));
	                    gs.getLoginManager().sysSendMessage(0, boss.rid, DBMail.Message.SUB_TYPE_BE_MONSTER_SEND_BACK, "", "", 0, true, addList);
	                }
                }
	        }
	    } else if (state == STATE_REGISTER_END && newState == STATE_FIGHT_START) { //鎴樻枟寮�
	        state = newState;
	    } else if (state == STATE_FIGHT_START && newState == STATE_FIGHT_END) { //鎴樻枟缁撴潫
	        state = newState;
	    }
	    boolean bossDie = false;
	    if (state == STATE_FIGHT_START) {
	        if (nowBoss == null) {
	            state = STATE_FIGHT_END;
	            bossDie = true;
	        } else {
	            boolean isAllGeneralDie = false;
	            if (nowBoss.lineup.size() <= 0) {
	                state = STATE_FIGHT_END;
	                bossDie = true;
	            }
                for (DBBeMonsterLineup lineup : nowBoss.lineup) {
                    for (DBBeMonsterGeneral general : lineup.generals) {
                        if (general.hp <= 0) {
                            isAllGeneralDie = true;
                        } else {
                            isAllGeneralDie = false;
                        }
                        if (!isAllGeneralDie) {
                            break;
                        }
                    }
                    if (!isAllGeneralDie) {
                        break;
                    }
                }
                if (isAllGeneralDie) {
                    state = STATE_FIGHT_END;
                    bossDie = true;
                }
	        }
	    }
	    if (state == STATE_FIGHT_END) {
	        state = STATE_NORMAL;
	        BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	        if (bossDie) {
	            gs.getDB().execute(new sendRewardToAllTransaction(cfg.killedMonsterRewards));
	        } else {
	            gs.getLoginManager().sysSendMessage(0, nowBoss.rid, DBMail.Message.SUB_TYPE_BE_MONSTER_BOSS_NOT_DIE, "", "", 0, true, cfg.monsterAliveRewards);
	        }
	        if (nowBoss != null) {
	            BeMonsterReward monsterKillRoleRewrads = null;
	            for (BeMonsterReward beMonsterReward : cfg.monsterKillRoleRewrads) {
	                if (nowBoss.killCnt >= beMonsterReward.numMin && nowBoss.killCnt <= beMonsterReward.numMax) {
	                    monsterKillRoleRewrads = beMonsterReward;
	                    break;
	                }
	            }
	            if (monsterKillRoleRewrads != null) {
	                gs.getLoginManager().sysSendMessage(0, nowBoss.rid, DBMail.Message.SUB_TYPE_BE_MONSTER_BOSS_KILL_ROLE_REWARD, "", "", 0, true, monsterKillRoleRewrads.rewrad);
	            }
	        }
	        List<SBean.DBBeMonsterAttacker> rankList = getAttackValueRank();
	        for (int i = 0; i < rankList.size(); i++) {
	            SBean.DBBeMonsterAttacker attacker = rankList.get(i);
	            BeMonsterReward rankReward = null;
	            int rank = i + 1;
	            for (BeMonsterReward beMonsterReward : cfg.rankRewards) {
                    if (rank >= beMonsterReward.numMin && rank <= beMonsterReward.numMax) {
                        rankReward = beMonsterReward;
                        break;
                    }
                }
	            if (rankReward != null) {
	                gs.getLoginManager().sysSendMessage(0, attacker.rid, DBMail.Message.SUB_TYPE_BE_MONSTER_RANK_REWARD, "", "", 0, true, rankReward.rewrad);
	            }
            }
	    }
	    if (state != STATE_NORMAL) {
	        save();
	    }
	}
	
	public DBBeMonsterBoss getTopOneBoss()
	{
	    List<SBean.DBBeMonsterBoss> list = new ArrayList<SBean.DBBeMonsterBoss>(); 
	    list.addAll(bossMap.values());
	    Collections.sort(list, new Comparator<DBBeMonsterBoss>() {

            @Override
            public int compare(DBBeMonsterBoss o1, DBBeMonsterBoss o2)
            {
                if (o1.bidPirce == o2.bidPirce) {
                    return o2.bidTime - o1.bidTime;
                } else {
                    return o2.bidPirce - o1.bidPirce;
                }
            }
	    });
	    if (list.size() <= 0) {
	        return null;
	    }
	    return list.get(0);
	}
	
	public synchronized void save()
	{
		List<SBean.DBBeMonsterBoss> bossList = new ArrayList<SBean.DBBeMonsterBoss>();
		for (SBean.DBBeMonsterBoss boss : bossMap.values()) {
			bossList.add(boss);
		}
		List<SBean.DBBeMonsterAttacker> attackerList = new ArrayList<SBean.DBBeMonsterAttacker>();
		for (SBean.DBBeMonsterAttacker attacker : attackerMap.values()) {
		    attackerList.add(attacker);
        }
		beMonster.boss = bossList;
		beMonster.attacker = attackerList;
		gs.getDB().execute(new SaveTrans(beMonster.kdClone()));
	}
	
	public synchronized void addBoss(SBean.DBBeMonsterBoss boss)
	{
	    if (this.state == STATE_REGISTER) {
	        bossMap.put(boss.rid, boss);
	    }
	}
	
	public synchronized void updateNowBoss(SBean.DBBeMonsterBoss boss)
	{
	    if (this.state == STATE_REGISTER_END || this.state == STATE_FIGHT_START) {
	        this.nowBoss = boss;
	    }
	}
	
	public synchronized boolean updateLineupHp(byte lineupId, List<DBBeMonsterGeneral> generals)
    {
	    DBBeMonsterLineup lineup = null;
        for (DBBeMonsterLineup ml : nowBoss.lineup) {
            if (ml.id == lineupId) {
                lineup = ml;
                break;
            }
        }
        if (lineup == null) {
            return false; 
        }
        if (generals.size() != lineup.generals.size()) {
            return false;
        }
        for (DBBeMonsterGeneral dbBeMonsterGeneral : lineup.generals) {
            for (DBBeMonsterGeneral g : generals) {
                if (dbBeMonsterGeneral.gid == g.gid) {
                   dbBeMonsterGeneral.hp = g.hp; 
                }
            }
        }
        return true;
    }
	
	public byte refreshState()
	{
	    int now = gs.getMinuteOfDay();
	    SBean.BeMonsterCFGS cfg = gs.gameData.getBeMonsterCFG();
	    for(int i = 0; i < cfg.RegisterTime.size(); i++) {
	        short registerTime = cfg.RegisterTime.get(i);
	        short registerEndTime = cfg.RegisterEndTime.get(i);
	        short fightStartTime = cfg.fightStartTime.get(i);
	        short fightEndTime = cfg.fightEndTime.get(i);
	        if (now >= registerTime && now < registerEndTime) {
	            return STATE_REGISTER;
	        } else if (now >= registerEndTime && now < fightStartTime) {
	            return STATE_REGISTER_END;
	        } else if (now >= fightStartTime && now < fightEndTime) {
	            return STATE_FIGHT_START;
	        } 
	    }
	    return STATE_FIGHT_END;
	}
	
	public List<SBean.DBBeMonsterAttacker> getAttackValueRank()
	{
	    List<SBean.DBBeMonsterAttacker> list = new ArrayList<SBean.DBBeMonsterAttacker>();
	    if (attackerMap != null) {
	        list.addAll(attackerMap.values());
	    }
	    Collections.sort(list, new Comparator<DBBeMonsterAttacker>() {

            @Override
            public int compare(DBBeMonsterAttacker o1, DBBeMonsterAttacker o2)
            {
                return o2.attackValue - o1.attackValue;
            }
        });
	    return list;
	}
	
	public synchronized DBBeMonsterBoss getNowBoss()
	{
	    return nowBoss;
	}
	
	public synchronized DBBeMonsterBoss getBossByRid(int rid)
	{
	    System.out.println(rid);
	    return bossMap.get(rid);
	}
	
	public synchronized DBBeMonsterAttacker getAttackByRid(int id)
    {
        return attackerMap.get(id);
    }
	
	public synchronized void addAttacker(DBBeMonsterAttacker att)
	{
	    attackerMap.put(att.rid, att);
	}
	
	public class sendRewardToAllTransaction implements Transaction
    {

	    public sendRewardToAllTransaction(List<SBean.DropEntryNew> addList) {
	        this.addList = addList;
	    }
	    
        @Override
        public boolean doTransaction()
        {
            if (this.addList == null) {
                return false;
            }
            Iterator<TableEntry<Integer, DBRole>> it = role.iterator();
            while(it.hasNext()){
                TableEntry<Integer, DBRole> entry = (TableEntry<Integer, DBRole>)it.next();
                gs.getLoginManager().sysSendMessage(0, entry.getKey(), DBMail.Message.SUB_TYPE_BE_MONSTER_ATTACKER_REWARD, "", "", 0, true, addList);
            }
            return true;
        }

        @Override
        public void onCallback(ErrorCode arg0)
        {
            
        }
        
        @AutoInit
        public TableReadonly<Integer, DBRole>  role;
        private List<SBean.DropEntryNew> addList;
    }
	
	GameServer gs;
	public static Map<Integer,SBean.DBBeMonsterBoss> bossMap = new ConcurrentHashMap<Integer,SBean.DBBeMonsterBoss>();
	public static Map<Integer,SBean.DBBeMonsterAttacker> attackerMap = new ConcurrentHashMap<Integer,SBean.DBBeMonsterAttacker>();
	SBean.DBBeMonster beMonster;
	private byte state = 0;
	private DBBeMonsterBoss nowBoss;
	
	public static final byte STATE_REGISTER = 1;
	public static final byte STATE_REGISTER_END = 2;
	public static final byte STATE_FIGHT_START = 3;
	public static final byte STATE_FIGHT_END = 4;
	public static final byte STATE_NORMAL = 5;
}

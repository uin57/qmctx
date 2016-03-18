package i3k.gs;

import i3k.SBean;
import i3k.SBean.DBPetBrief;
import i3k.DBRoleGeneral;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ket.kdb.Table;
import ket.kdb.Transaction;
import ket.util.Stream;

public class SuraManager {
	
	public class SaveTrans implements Transaction
	{	
		SaveTrans(SBean.DBSura sura)
		{
			this.sura = sura;
		}
		
		@Override
		public boolean doTransaction()
		{	
			byte[] key = Stream.encodeStringLE("sura");
			byte[] data = Stream.encodeLE(sura);
			world.put(key, data);
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				gs.getLogger().warn("sura save failed");
			}
		}
		
		@AutoInit
		public Table<byte[], byte[]> world;
		
		SBean.DBSura sura;
	}
	
	public static class AttackFinishRes
	{
		public boolean bWin;
		public short stage;
		public List<SBean.DropEntryNew> drops;
	}
	
	public static class AttackStartEnemy
	{
		public int erid;
		public List<DBRoleGeneral> egenerals;
		public List<DBPetBrief> epets;
		public List<SBean.DBRelation> erelation;
		public List<SBean.DBGeneralStone> egStones;
	}
	
	public static class AttackStartRes
	{
		public List<DBRoleGeneral> sgenerals;
		public List<DBPetBrief> pets;
		public List<SBean.DBRelation> srelation;
		public List<SBean.DBGeneralStone> sgStones;
		public List<AttackStartEnemy> enemies;
	}
	
	public SuraManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(SBean.DBSura sura)
	{
		this.sura = sura;
		if (sura == null) {
			this.sura = new SBean.DBSura(gs.getDayCommon(), new ArrayList<Short>(), new ArrayList<SBean.DBSuraEnemy>(), 0, 0);
			generateNewDay();
		}
	}
	
	private int[] selectEnemy()
	{
		SBean.SuraCFGS cfg = gs.gameData.getSuraCFG();
		
		int[] res = new int[cfg.miniStages.size()];
		for(int i = 0; i < res.length; ++i)
		{
			SBean.SuraMiniStageCFGS scfg = cfg.miniStages.get(i);
			res[i] = gs.gameData.getRandInt(scfg.rankMin, scfg.rankMax+1);
		}
		return res;
	}
	
	public synchronized void onTimer()
	{
		int day = gs.getDayCommon();
		if (day > sura.day) {
			sura.day = day;
			generateNewDay();
			save();
		}
	}
	
	private void generateNewDay()
	{
		Set<Short> gids = new TreeSet<Short>();
		SBean.SuraCFGS cfg = gs.gameData.getSuraCFG();
		for (short gid : cfg.fixedGids)
			gids.add(gid);
		
		for (SBean.SuraRandomGroupCFGS rcfg : cfg.randomGroups) {
			for (int i = 0; i < rcfg.count; i ++) {
				short gid = 0;
				do {
					int index = gs.gameData.getRandInt(0, rcfg.gids.size());
					gid = rcfg.gids.get(index);
				}
				while (gids.contains(gid));
				gids.add(gid);
			}
		}
		
		List<Short> newGids = new ArrayList<Short>();
		for (short gid : gids)
			newGids.add(gid);
		sura.gids = newGids;
		
		sura.enemies.clear();
		int[] enemies = selectEnemy();
		final List<Integer> rids = new ArrayList<Integer>();
		for (int r : enemies) {
			int rid = gs.getArenaManager().getRankRole(r);
			if (rid > 0)
				rids.add(rid);
			sura.enemies.add(new SBean.DBSuraEnemy(rid, (short)0, (short)0, "", "", new ArrayList<DBRoleGeneral>(), new ArrayList<SBean.DBPetBrief>()));
		}
		gs.getLoginManager().getSuraEnemies(rids, new LoginManager.GetSuraEnemiesCallback() {
			
			@Override
			public void onCallback(List<SBean.DBSuraEnemy> lst)
			{
				if (lst.size() != rids.size())
					gs.getLogger().warn("Sura enemies count doesn't match!!! lst=" + lst.size() + ", rids=" + rids.size());
				for (SBean.DBSuraEnemy e : lst) {
					int index = 0;
					for (SBean.DBSuraEnemy se : sura.enemies) {
						if (se.id == e.id) {
							sura.enemies.set(index, e);
							break;
						}
						index ++;
					}
				}
			}
		});
	}
	
	public synchronized void save()
	{
		gs.getDB().execute(new SaveTrans(sura.kdClone()));
	}
	
	public synchronized List<SBean.DBSuraEnemy> getStageEnemies(byte stage, List<Short> gids, byte[] stageInfo)
	{
		if (stage < 0)
			return null;
		
		if (gids != null) {
			for (short gid : sura.gids)
				gids.add(gid);
		}
		
		List<SBean.DBSuraEnemy> res = new ArrayList<SBean.DBSuraEnemy>();
		if (stage * 3 + 2 >= sura.enemies.size()) {
			stage = (byte)((sura.enemies.size()-2)/3);
			if (stage < 0)
				return null;
			if (stageInfo != null)
				stageInfo[0] = 1;
		}
		
		for (int i = stage * 3; i <= stage * 3 + 2; i ++)
			res.add(sura.enemies.get(i).kdClone());
		
		if (stageInfo != null)
			stageInfo[1] = stage;

		return res;
	}
	
	GameServer gs;
	SBean.DBSura sura;

}

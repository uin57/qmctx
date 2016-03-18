
package i3k.gtool;

import i3k.SBean;
import i3k.SBean.DropEntryNew;
import i3k.SBean.GeneralStoneBasicCFGS;
import i3k.SBean.GeneralStoneEvoCostCFGS;
import i3k.SBean.GeneralStonePosCFGS;
import i3k.SBean.InvitationTaskCFG;
import i3k.gs.GameData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ket.moi.ExcelSheet;
import ket.util.ArgsMap;
import ket.util.Stream;
import ket.util.StringConverter;

public class DataTool
{

	static class QEntry implements Comparable<QEntry>
	{
		public QEntry(short id, float p)
		{
			this.id = id;
			this.p = p;
		}
		short id;
		float p;
		@Override
		public int compareTo(QEntry oe)
		{
			if( p > oe.p ) return -1;
			if( p < oe.p ) return 1;
			return 0;
		}
	}
	
	short checkIDRange(int id, String name) throws Exception
	{
		final int MIN_ID = 1;
		final int MAX_ID = 32000;
		if( id < MIN_ID || id > MAX_ID )
			throw new Exception(name + " ID " + id + " 超出范围 (" + MIN_ID + "," + MAX_ID + ")");
		return (short)id;
	}

	static class Formula
	{
		public short id;
		public float arg1;
		public float arg2;
	}

	public Map<Integer, Formula> loadFormulas(String fileName) throws Exception
	{
		Map<Integer, Formula> formulas = new TreeMap<Integer, Formula>();
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		final int colStart = 0; // start col
		final int rowStart = 3; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, colStart) )
		{
			int sid = sheet.getIntValue(row, colStart);
			Formula f = new Formula();
			f.arg1 = sheet.getFloatValue(row, colStart + 1);
			f.id = sheet.getShortValue(row, colStart + 2);
			f.arg2 = sheet.getFloatValue(row, colStart + 3);
			if( null != formulas.put(sid, f) )
				throw new Exception("公式ID重复， id = " + f.id);
			++row;
		}
		// TODO dump
		System.out.println("formula count: " + formulas.size());
		/*
		for(Map.Entry<Integer, Formula> e : formulas.entrySet())
		{
			System.out.println("formula id=" + e.getKey() + ", pid=" 
					+ e.getValue().id + ", arg1=" + e.getValue().arg1 + ", arg2=" + e.getValue().arg2);
		}
		*/
		return formulas;
	}
	
	public List<SBean.UIEffectCFG> loadUIEffects(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.UIEffectCFG> lst = new ArrayList<SBean.UIEffectCFG>();
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.UIEffectCFG cfg = new SBean.UIEffectCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfg.path = sheet.getStringValue(row, 1);
			cfg.scale = sheet.getFloatValue(row, 2);
			lst.add(cfg);
			if( ! uiEffectIDs.add(cfg.id) )
				throw new Exception(tname + "ID重复， id = " + cfg.id);
			++row;
		}
		// TODO dump
		for(SBean.UIEffectCFG cfg : lst)
		{
			//System.out.println("ui effect id=" + cfg.id + ", path=" + cfg.path + ", scale= " + cfg.scale);
			if( checkPS != null )
				checkPS.println("ui effect : [" + cfg.path + "]");
		}
		//
		return lst;
	}

	public List<SBean.ArtEffectCFG> loadArtEffects(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.ArtEffectCFG> lst = new ArrayList<SBean.ArtEffectCFG>();
		final int colStart = 0; // start col
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, colStart) )
		{
			SBean.ArtEffectCFG cfg = new SBean.ArtEffectCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, colStart), tname);
			cfg.path = sheet.getStringValue(row, colStart + 1);
			cfg.hsName = sheet.getStringValue(row, colStart + 2);
			cfg.radius = sheet.getFloatValue(row, colStart + 3);
			lst.add(cfg);
			if( ! artEffectIDs.add(cfg.id) )
				throw new Exception("特效ID重复， id = " + cfg.id);
			++row;
		}
		// TODO dump
		for(SBean.ArtEffectCFG cfg : lst)
		{
			//System.out.println("art effect id=" + cfg.id + ", path=" + cfg.path + ", hsName= " + cfg.hsName);
			if( checkPS != null )
				checkPS.println("3d effect : [" + cfg.path + ", hsName=" + cfg.hsName + "]");
		}
		//
		return lst;
	}
	
	public List<SBean.FightEffectCFG> loadFightEffects(String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.FightEffectCFG> lst = new ArrayList<SBean.FightEffectCFG>();
		return lst;
	}
	
	public List<SBean.IconCFG> loadIcons(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.IconCFG> lst = new ArrayList<SBean.IconCFG>();
		final int colStart = 0; // start col
		final int rowStart = 1; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, colStart) )
		{
			SBean.IconCFG cfg = new SBean.IconCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, colStart), tname);
			cfg.path = sheet.getStringValue(row, colStart + 1);
			lst.add(cfg);
			if( ! iconIDs.add(cfg.id) )
				throw new Exception("图标ID重复， id = " + cfg.id);
			++row;
		}
		// TODO dump
		System.out.println("icons count: " + lst.size());
		
		for(SBean.IconCFG cfg : lst)
		{
			//System.out.println("icon id=" + cfg.id + ", path=" + cfg.path);
			if( checkPS != null )
				checkPS.println("image : [" + cfg.path + "]");
		}
		return lst;
	}
	
	public List<String> loadGG(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<String> lst = new ArrayList<String>();
		final int colStart = 0; // start col
		final int rowStart = 1; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, colStart) )
		{
			lst.add(sheet.getStringValue(row, 1));
			lst.add(sheet.getStringValue(row, 2));
			++row;
		}		
		return lst;
	}
	
	public void loadItems(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs, SBean.GameDataCFGT gcfgt) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.ItemCFGS> lsts = new ArrayList<SBean.ItemCFGS>();
		gcfgt.items = new ArrayList<SBean.GeneralName>();
		gcfgs.items = lsts;
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.ItemCFGS cfgs = new SBean.ItemCFGS();
			cfgs.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			String name = sheet.getStringValue(row, 1);
			gcfgt.items.add(new SBean.GeneralName(cfgs.id, name));
			cfgs.name = name;
			cfgs.type = sheet.getShortValue(row, 4);
			cfgs.arg1 = sheet.getIntValue(row, 5);
			cfgs.arg2 = sheet.getIntValue(row, 6);
			cfgs.arg3 = sheet.getIntValue(row, 7);
			switch( cfgs.type )
			{
			case GameData.ITEM_TYPE_GAMBLE:
				{
					if( cfgs.arg2 != 1 && cfgs.arg2 != 2 )
							throw new Exception(tname + "道具类型错误， id = " + cfgs.id);
				}
			default:
				break;
			}
			cfgs.rank = sheet.getByteValue(row, 8);
			cfgs.price = sheet.getIntValue(row, 10);
			cfgs.extDropProp = sheet.getByteValue(row, 12);
			cfgs.extDropPropInc = sheet.getByteValue(row, 13);
			cfgs.extDropCool = sheet.getByteValue(row, 14);
			cfgs.lvlReq = sheet.getShortValue(row, 15);
			cfgs.gildVal = sheet.getIntValue(row, 16);
			lsts.add(cfgs);
			if( itemIDs.put(cfgs.id, cfgs) != null )
				throw new Exception("道具ID重复， id = " + cfgs.id);
			++row;
		}
	}
	
	public List<SBean.DropTableCFGS> loadDropTables(String tname, String fileName, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.DropTableCFGS> lst = new ArrayList<SBean.DropTableCFGS>();
		int row = 2;
		while( sheet.isNotEmpty(row, 2) )
		{
			SBean.DropTableCFGS cfg = new SBean.DropTableCFGS();
			cfg.id = checkIDRange(sheet.getIntValue(row, 2), tname);
			cfg.entryList = new ArrayList<SBean.DropTableEntry>();
			cfg.srcList = new ArrayList<SBean.DropEntry>();
			int col = 3;
			float sum = 0;
			while( sheet.isNotEmpty(row, col) )
			{
				short id = sheet.getShortValue(row, col);
				byte type = sheet.getByteValue(row+1, col);
				byte arg = sheet.getByteValue(row+2, col);
				float w = sheet.getFloatValue(row+3, col);
				switch( type )
				{
				case GameData.COMMON_TYPE_ITEM:
					{
						if( arg <= 0 || arg > 100 )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的掉落参数 ");
						if( ! itemIDs.containsKey(id) )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的道具ID ");
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{
						if( arg <= 0 || arg > 100 )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的掉落参数 ");
						if( ! equipIDs.containsKey(id) )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的装备ID ");
					}
					break;
				case GameData.COMMON_TYPE_GENERAL:
					{
						if( ! generalIDs.containsKey(id) )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的武将ID ");
						if( arg <= 0 || arg > GameData.MAX_GENERAL_EVO_LEVEL )
								throw new Exception(tname + " " + cfg.id + ", id=" + id + " 错误的掉落参数 ");
					}
					break;
				case GameData.COMMON_TYPE_NULL:
					break;
				default:
						throw new Exception(tname + " " + cfg.id + " 错误的掉落类型 " + type);
				}
				cfg.srcList.add(new SBean.DropEntry(type, arg, id));
				if( w > 0 )
				{
					SBean.DropTableEntry e = new SBean.DropTableEntry(new SBean.DropEntry(type, arg, id), w);
					cfg.entryList.add(e);
					sum += w;
				}
				++col;
			}
			if( cfg.entryList.isEmpty() || sum <= 0 )
				throw new Exception(tname + " " + cfg.id + " 为空");
			
			class QEntry implements Comparable<QEntry>
			{
				public QEntry(SBean.DropEntry e, float p)
				{
					this.e = e;
					this.p = p;
				}
				SBean.DropEntry e;
				float p;
				@Override
				public int compareTo(QEntry oe)
				{
					if( p > oe.p ) return -1;
					if( p < oe.p ) return 1;
					return 0;
				}
			}
			PriorityQueue<QEntry> queue = new PriorityQueue<QEntry>();
			for(SBean.DropTableEntry e : cfg.entryList)
			{
				queue.add(new QEntry(e.item, e.pro/sum));
			}
			List<SBean.DropTableEntry> lst2 = new ArrayList<SBean.DropTableEntry>();
			float sp = 0;
			for(QEntry e : queue)
			{
				lst2.add(new SBean.DropTableEntry(e.e, sp + e.p));
				sp += e.p;
			}
			cfg.entryList = lst2;
			lst.add(cfg);
			if( ! dropTableIDs.add(cfg.id) )
				throw new Exception("掉落表ID重复， id = " + cfg.id);
			row += 4;
		}
		// TODO dump
		System.out.println("drop tables count: " + lst.size());
		return lst;
	}
	
	public List<SBean.TipsCFG> loadTips(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.TipsCFG> lst = new ArrayList<SBean.TipsCFG>();
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.TipsCFG cfg = new SBean.TipsCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfg.effID = sheet.getShortValue(row, 1);
			if( ! uiEffectIDs.contains(cfg.effID) )
				throw new Exception(tname + " " + cfg.id + " 引用界面特效ID " + cfg.effID + " 不存在");
			cfg.seq = sheet.getByteValue(row, 2);
			cfg.count = sheet.getByteValue(row, 3);
			cfg.title = sheet.getStringValue(row, 4);
			cfg.content = sheet.getStringValue(row, 5);
						
			lst.add(cfg);
			if( ! tipsIDs.add(cfg.id) )
				throw new Exception(tname + " ID重复， id = " + cfg.id);
			++row;
		}
		//
		return lst;
	}
	
	public List<SBean.DlgTableCFG> loadDlgTables(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.DlgTableCFG> lst = new ArrayList<SBean.DlgTableCFG>();
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.DlgTableCFG cfg = new SBean.DlgTableCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfg.iconID = sheet.getShortValue(row, 22);
			if( cfg.iconID > 0 )
			{
				if( ! iconIDs.contains(cfg.iconID) )
					throw new Exception(tname + " " + cfg.id + " 引用ICONID " + cfg.iconID + " 不存在");
			}
			cfg.entryList = new ArrayList<SBean.DlgTableEntry>();
			final int nDrop = 10;
			final int colDropStart = 2;
			Map<String, Integer> pmap = new TreeMap<String, Integer>();
			int sum = 0;
			for(int i = 0; i < nDrop; ++i)
			{
				int p = sheet.getIntValue(row, colDropStart + i + nDrop);
				if( p <= 0 ) continue;
				String dlg = sheet.getStringValue(row, colDropStart + i);
				sum += p;
				Integer mp = pmap.get(dlg);
				if( mp == null )
					pmap.put(dlg, p);
				else
					pmap.put(dlg, mp.intValue() + p);
			}
			class QEntry implements Comparable<QEntry>
			{
				public QEntry(String id, float p)
				{
					this.id = id;
					this.p = p;
				}
				String id;
				float p;
				@Override
				public int compareTo(QEntry oe)
				{
					if( p > oe.p ) return -1;
					if( p < oe.p ) return 1;
					return 0;
				}
			}
			PriorityQueue<QEntry> queue = new PriorityQueue<QEntry>();
			for(Map.Entry<String, Integer> e : pmap.entrySet())
			{
				queue.add(new QEntry(e.getKey(), e.getValue()/(float)sum));
			}
			float sp = 0;
			for(QEntry e : queue)
			{
				cfg.entryList.add(new SBean.DlgTableEntry(e.id, sp + e.p));
				sp += e.p;
			}
			lst.add(cfg);
			if( ! dlgTableIDs.add(cfg.id) )
				throw new Exception("对白库ID重复， id = " + cfg.id);
			++row;
		}
		//
		return lst;
	}
	
	public List<SBean.DlgTableCFG> loadDlgTablesNew(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.DlgTableCFG> lst = new ArrayList<SBean.DlgTableCFG>();
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.DlgTableCFG cfg = new SBean.DlgTableCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfg.iconID = sheet.getShortValue(row, 2);
			if( cfg.iconID > 0 )
			{
				if( ! iconIDs.contains(cfg.iconID) )
					throw new Exception(tname + " " + cfg.id + " 引用ICONID " + cfg.iconID + " 不存在");
			}
			cfg.entryList = new ArrayList<SBean.DlgTableEntry>();
			
			final int colDropStart = 3;
			Map<String, Integer> pmap = new TreeMap<String, Integer>();
			int sum = 0;
			int i = 0;
			while( sheet.isNotEmpty(row, colDropStart + i) )
			{
				String dlg = sheet.getStringValue(row, colDropStart + i);
				int p = sheet.getIntValue(row+1, colDropStart + i);
				if( p <= 0 ) continue;
				sum += p;
				Integer mp = pmap.get(dlg);
				if( mp == null )
					pmap.put(dlg, p);
				else
					pmap.put(dlg, mp.intValue() + p);
				++i;
			}
			class QEntry implements Comparable<QEntry>
			{
				public QEntry(String id, float p)
				{
					this.id = id;
					this.p = p;
				}
				String id;
				float p;
				@Override
				public int compareTo(QEntry oe)
				{
					if( p > oe.p ) return -1;
					if( p < oe.p ) return 1;
					return 0;
				}
			}
			PriorityQueue<QEntry> queue = new PriorityQueue<QEntry>();
			for(Map.Entry<String, Integer> e : pmap.entrySet())
			{
				queue.add(new QEntry(e.getKey(), e.getValue()/(float)sum));
			}
			float sp = 0;
			for(QEntry e : queue)
			{
				cfg.entryList.add(new SBean.DlgTableEntry(e.id, sp + e.p));
				sp += e.p;
			}
			lst.add(cfg);
			if( ! dlgTableIDsNew.add(cfg.id) )
				throw new Exception("对白库ID重复， id = " + cfg.id);
			//
			if( cfg.iconID == -2 || cfg.iconID == -3 ) // general dlgs
			{
				String name = sheet.getStringValue(row, 1);
				short[] dlgs = generalDlgs.get(name);
				if( dlgs == null )
				{
					dlgs = new short[8];
					for(int j = 0; j < 8; ++j)
						dlgs[j] = -1;
					generalDlgs.put(name, dlgs);
				}
				int type = cfg.id / 1000;
				if( type > 0 && type < 8 )
				{
					dlgs[type-1] = cfg.id;
				}
				else if( type == 8 )
				{
					dlgs[0] = cfg.id;
					dlgs[2] = cfg.id;
				}
				if( cfg.iconID == -3 )
					dlgs[7] = 1;
			}
			//
			row+=2;
		}
		//
		return lst;
	}
	
	private int locateColTag(String tname, ExcelSheet sheet, int col, String tagName) throws Exception
	{
		final int maxBlank = 1024;
		int nBlank = 0;
		int row = 0;
		while( true )
		{
			if( sheet.isNotEmpty(row, col) )
			{
				nBlank = 0;
				String str = sheet.getStringValue(row, col);
				if( tagName.equals(str) )
					return row;
			}
			else
			{	if( ++nBlank >= maxBlank )
					break;
			}
			++row;
		}
		throw new Exception(tname + " 寻找标签 " + tagName + " 失败！");
	}
	
	public void loadSaleTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);	
		
		gcfgc.alarms = new ArrayList<SBean.AlarmCFG>();
		final int rowAlarm = locateColTag(tname, sheet, 0, "定时提醒");
		int row = rowAlarm + 1;
		while( sheet.isNotEmpty(row, 1) )
		{
			short s = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(row, 2));
			String c = sheet.getStringValue(row, 3);
			gcfgc.alarms.add(new SBean.AlarmCFG(s, c));
			++row;
		}
		
		gcfgs.mcard = new SBean.MonthlyCardCFGS();
		final int rowMCard = locateColTag(tname, sheet, 0, "月卡");
		gcfgs.mcard.price = sheet.getByteValue(rowMCard + 1, 2);
		gcfgs.mcard.reward = sheet.getShortValue(rowMCard + 2, 2);
		gcfgs.mcard.days = sheet.getByteValue(rowMCard + 3, 2);
		
		if( gcfgs.dtask2 != null )
		{
			for(SBean.DailyTask2CFGS e : gcfgs.dtask2)
			{
				if( e.id == GameData.DAILYTASK2_TYPE_MONTHLY_CARD )
				{
					e.reward1Type = GameData.DAILYTASK2_REWARD_STONE;
					e.reward1Arg1 = gcfgs.mcard.reward;
					e.reward1Arg2 = 0;
					e.reward1ID = 0;
					e.reward2Type = -1;
					e.reward2Arg1 = 0;
					e.reward2Arg2 = 0;
					e.reward2ID = 0;
				}
			}
		}
	}
	
	public void loadMainTableNew(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		
		gcfgs.equipCmn = new SBean.EquipCmnCFGS();
		{
			final int row = locateColTag(tname, sheet, 0, "装备相关");
			gcfgs.equipCmn.rank = new ArrayList<SBean.EquipRankCFGS>();
			for(byte i = 0; i < GameData.EQUIP_RANK_COUNT; ++i)
			{
				byte max = sheet.getByteValue(row+2, 2+i);
				String str = sheet.getStringValue(row+3, 2+i);
				SBean.EquipRankCFGS rcfg = new SBean.EquipRankCFGS();
				rcfg.gild = new ArrayList<Short>();
				String[] v = str.split(",");
				for(byte j = 0; j < max; ++j)
				{
					short e = (short)Float.parseFloat(v[j].trim());
					rcfg.gild.add(e);
				}
				gcfgs.equipCmn.rank.add(rcfg);
			}
			gcfgs.equipCmn.gildBack = new ArrayList<SBean.EquipGildBackCFGS>();
			for(byte i = 0; i < 5; ++i)
			{
				String str = sheet.getStringValue(row+6, 3+i);
				SBean.EquipGildBackCFGS bcfg = new SBean.EquipGildBackCFGS();
				bcfg.drops = new ArrayList<SBean.DropEntryNew>();
				String[] strs = str.split(";");
				for (String s : strs) {
					String[] v = s.split(",");
					SBean.DropEntryNew drop = new SBean.DropEntryNew();
					drop.type = GameData.COMMON_TYPE_ITEM;
					drop.id = Short.parseShort(v[0].trim());
					drop.arg = Integer.parseInt(v[1].trim());
					bcfg.drops.add(drop);
				}
				gcfgs.equipCmn.gildBack.add(bcfg);
			}
		}
		
		gcfgs.friend = new SBean.FriendCFGS();
		{
			final int row = locateColTag(tname, sheet, 0, "好友相关");
			gcfgs.friend.cap = sheet.getByteValue(row, 2);
			gcfgs.friend.pointMaxPerDay = sheet.getShortValue(row+1, 2);
			gcfgs.friend.vitBaoziMaxPerDay = sheet.getShortValue(row+2, 2);
			gcfgs.friend.vitPerBaozi = sheet.getShortValue(row+2, 4);
			gcfgs.friend.pointPerBaozi = sheet.getShortValue(row+2, 6);
			
			gcfgs.friend.maxRecall = sheet.getByteValue(row+4, 2);
			gcfgs.friend.minRecallDay = sheet.getByteValue(row+4, 4);
			gcfgs.friend.recallLevels = new ArrayList<SBean.FriendRecallLevelCFGS>();
			{
				int col = 2;
				while( sheet.isNotEmpty(row+5, col) )
				{
					short lvlCeil = sheet.getShortValue(row+5, col);
					int reward = sheet.getIntValue(row+6, col);
					gcfgs.friend.recallLevels.add(new SBean.FriendRecallLevelCFGS(lvlCeil, reward));
					++col;
				}
			}
			
			gcfgs.friend.breedRewardProp = sheet.getFloatValue(row+8, 3);
			gcfgs.friend.breedRewardMaxPerDay = sheet.getIntValue(row+9, 3);
			
			gcfgs.friend.petExpItemMaxPerDay = sheet.getShortValue(row+11, 3);
			
			String cfgFriend = "{cap=" + gcfgs.friend.cap + "}";
			luaCfg.put("friend", cfgFriend);
		}
		
		{
			final int row = locateColTag(tname, sheet, 0, "版本");
			int resVersion = sheet.getIntValue(row + 1, 2);
			
			String cfgVersion = "{res=" + resVersion + "}";
			luaCfg.put("version", cfgVersion);
		}
		
		gcfgs.petsCmn = new SBean.PetCmnCFGS();
		{
			final int row = locateColTag(tname, sheet, 0, "宠物相关");
			gcfgs.petsCmn.breedReqLvl = sheet.getShortValue(row+1, 2);
			gcfgs.petsCmn.poundCapInit = sheet.getByteValue(row+2, 2);
			gcfgs.petsCmn.poundCapMax = sheet.getByteValue(row+2, 4);
			gcfgs.petsCmn.feedingFriendMax = sheet.getShortValue(row+6, 6);
			gcfgs.petsCmn.feedingFriendInterval = sheet.getIntValue(row+6, 8) * 60;
			gcfgs.petsCmn.feedingInterval = sheet.getShortValue(row+2, 6);
			gcfgs.petsCmn.feedingMaxTime = sheet.getShortValue(row+2, 9) * 60;
			gcfgs.petsCmn.poundUpgradeCost = new ArrayList<Integer>();
			for(int i = 0; i < gcfgs.petsCmn.poundCapMax - 1; ++i)
			{
				gcfgs.petsCmn.poundUpgradeCost.add(sheet.getIntValue(row+4, 2+i));
			}
			{
				gcfgs.petsCmn.friendExpItems = new ArrayList<SBean.PetFriendExpItemCFGS>();
				int col = 2;
				while( sheet.isNotEmpty(row+5, col) )
				{
					String s = sheet.getStringValue(row+5, col);
					String[] v = s.split(";");
					SBean.PetFriendExpItemCFGS ei = new SBean.PetFriendExpItemCFGS();
					ei.lvlCeil = (short)Float.parseFloat(v[0].trim());
					ei.itemID = (short)Float.parseFloat(v[1].trim());
					ei.itemCnt = (int)Float.parseFloat(v[2].trim());
					gcfgs.petsCmn.friendExpItems.add(ei);
					++col;
				}
			}
			gcfgs.petsCmn.deformFeedGrowth = new ArrayList<Short>();
			String str = sheet.getStringValue(row+7, 4);
			String[] v = str.split(";");
			for(String s : v)
			{
				short growth = (short)Float.parseFloat(s.trim());
				gcfgs.petsCmn.deformFeedGrowth.add(growth);
			}
			gcfgs.petsCmn.deformHappiness = sheet.getShortValue(row+8, 4);
			gcfgs.petsCmn.deformZanTimes = sheet.getByteValue(row+7, 6);
			gcfgs.petsCmn.deformTakeOfferTimes = sheet.getByteValue(row+8, 6);
			gcfgs.petsCmn.deformZanGrowth = sheet.getShortValue(row+7, 8);
			gcfgs.petsCmn.deformTakeOfferGrowth = sheet.getShortValue(row+8, 8);
			gcfgs.petsCmn.deformZanVit = new ArrayList<Short>();
			str = sheet.getStringValue(row+7, 10);
			v = str.split(";");
			for(String s : v)
			{
				short vit = (short)Float.parseFloat(s.trim());
				gcfgs.petsCmn.deformZanVit.add(vit);
			}
			gcfgs.petsCmn.deformTakeOfferVit = new ArrayList<Short>();
			str = sheet.getStringValue(row+8, 10);
			v = str.split(";");
			for(String s : v)
			{
				short vit = (short)Float.parseFloat(s.trim());
				gcfgs.petsCmn.deformTakeOfferVit.add(vit);
			}
			gcfgs.petsCmn.deformFreeTryTimes = sheet.getByteValue(row+9, 4);
			gcfgs.petsCmn.deformBuyTryTimes = sheet.getByteValue(row+9, 6);
			gcfgs.petsCmn.deformBuyTryStone = sheet.getShortValue(row+9, 8);
			gcfgs.petsCmn.deformTryItemId = sheet.getShortValue(row+9, 10);
			gcfgs.petsCmn.deformTryPoss = sheet.getByteValue(row+9, 12);
			gcfgs.petsCmn.deformFeedTimes = sheet.getByteValue(row+9, 14);
		}
		
		gcfgs.roleCmn = new SBean.RoleCmnCFGS();		
		{
			final int row = locateColTag(tname, sheet, 0, "战队相关");
			gcfgs.roleCmn.skillPointRecoverInterval = sheet.getIntValue(row + 1, 4);
			gcfgs.roleCmn.vitRecoverInterval = sheet.getIntValue(row + 2, 4);
			//gcfgs.roleCmn.skillPointMax = sheet.getShortValue(row + 1, 2);
			gcfgs.roleCmn.timeDayRefresh = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(row + 2, 2));
			gcfgs.roleCmn.vitMax = sheet.getShortValue(row + 3, 2);
			//gcfgs.roleCmn.buyMoneyMaxTimes = sheet.getShortValue(row + 4, 3);
			gcfgs.roleCmn.buyMoneyBaseVal = sheet.getIntValue(row + 4, 5);
			gcfgs.roleCmn.buyMoneylvlVal = sheet.getIntValue(row + 4, 7);
			gcfgs.roleCmn.buyMoneyTimesVal = sheet.getIntValue(row + 4, 9);
			gcfgs.roleCmn.buyMoneyCrit = new ArrayList<SBean.CritCFGS>();
			{
				String str = sheet.getStringValue(row + 4,  11);
				String[] critv = str.split(";");
				int sum = 0;
				for (String e: critv)
				{
					String[] v = e.split(",");
					SBean.CritCFGS critcfg = new SBean.CritCFGS();
					critcfg.CritMultiplier = Byte.parseByte(v[0]);
					critcfg.CritRate = Byte.parseByte(v[1]);
					gcfgs.roleCmn.buyMoneyCrit.add(critcfg);
					sum += critcfg.CritRate;
				}
				Collections.sort(gcfgs.roleCmn.buyMoneyCrit, new java.util.Comparator<SBean.CritCFGS>()
							{
								public int compare(SBean.CritCFGS l, SBean.CritCFGS r)
								{
									return (int)(l.CritRate-r.CritRate);
								}
							});
				float sp = 0;
				for (SBean.CritCFGS e : gcfgs.roleCmn.buyMoneyCrit)
				{
					sp += e.CritRate/sum;
					e.CritRate = sp;
				}
			}
			{
				String str = sheet.getStringValue(row + 4, 13);
				String[] v = str.split(";");
				gcfgs.roleCmn.buyMoneyPrice = new ArrayList<Integer>();
				for(String e : v )
				{
					gcfgs.roleCmn.buyMoneyPrice.add((int)Float.parseFloat(e.trim()));
				}
			}
			gcfgs.roleCmn.vitMaxTable = new ArrayList<Short>();
			gcfgs.roleCmn.vitAddLvlup = new ArrayList<Short>();
			//gcfgs.roleCmn.buyVitMaxTimes = sheet.getShortValue(row + 5, 3);
			gcfgs.roleCmn.buyVitVal = sheet.getIntValue(row + 5, 5);
			{
				String str = sheet.getStringValue(row + 5, 7);
				String[] v = str.split(";");
				gcfgs.roleCmn.buyVitPrice = new ArrayList<Integer>();
				for(String e : v )
				{
					gcfgs.roleCmn.buyVitPrice.add((int)Float.parseFloat(e.trim()));
				}
			}
			gcfgs.roleCmn.chatLvlReq = sheet.getShortValue(row + 7, 2);
			gcfgs.roleCmn.chatItemID = sheet.getShortValue(row + 7, 4);
			gcfgs.roleCmn.changeNameCost = sheet.getShortValue(row + 1, 8);
			gcfgs.roleCmn.changePetNameCost = sheet.getShortValue(row + 1, 10);
			luaCfg.put("timeDayRefresh", new Short(gcfgs.roleCmn.timeDayRefresh).toString());
			luaCfg.put("role", "{vitMax=" + gcfgs.roleCmn.vitMax
					+ ",chatLvlReq=" + gcfgs.roleCmn.chatLvlReq
					+ ",chatItemID=" + gcfgs.roleCmn.chatItemID
					+ ",vitRecoverInterval=" + gcfgs.roleCmn.vitRecoverInterval
					+ ",changeNameCost=" + gcfgs.roleCmn.changeNameCost
					+ ",changePetNameCost=" + gcfgs.roleCmn.changePetNameCost
					+ "}");
		}		
		
		gcfgs.generalsCmn = new SBean.GeneralCmnCFGS();
		gcfgs.generalsCmn.evoLevels = new ArrayList<SBean.GeneralEvoCFGS>();
		{
			final int row = locateColTag(tname, sheet, 0, "武将相关");
			for(int i = 0; i < GameData.MAX_GENERAL_EVO_LEVEL; ++i)
			{
				int money = sheet.getIntValue(row+3, 2+i);
				short stone = sheet.getShortValue(row+2, 2+i);
				short stoneBack = sheet.getShortValue(row+4, 2+i);
				gcfgs.generalsCmn.evoLevels.add(new SBean.GeneralEvoCFGS(money, stone, stoneBack));
			}
			String str = sheet.getStringValue(row+8, 2);
			String[] powerScales = str.split(",");
			gcfgs.generalsCmn.evoPowerScales = new ArrayList<Float>();
			for (String scale : powerScales) {
				gcfgs.generalsCmn.evoPowerScales.add(Float.parseFloat(scale.trim()));
			}
			gcfgs.generalsCmn.commonSoulStoneId = sheet.getShortValue(row+11, 4);
		}
		
		gcfgs.generalsCmn.skills = new ArrayList<SBean.GeneralSkillCFGS>();
		{
			final int row = locateColTag(tname, sheet, 0, "技能相关");
			for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
			{
				SBean.GeneralSkillCFGS scfg = new SBean.GeneralSkillCFGS();
				scfg.reqAdvLvl = sheet.getByteValue(row+1, i+2);
				scfg.maxLvl = sheet.getByteValue(row+2, i+2);
				gcfgs.generalsCmn.skills.add(scfg);
			}
			{
				String str = sheet.getStringValue(row+3, 2);
				String[] powers = str.split(",");
				gcfgs.generalsCmn.skillInitPower = new ArrayList<Integer>();
				for (String p : powers) {
					gcfgs.generalsCmn.skillInitPower.add(Integer.parseInt(p.trim()));
				}				
			}
			gcfgs.generalsCmn.skillUpPower = sheet.getIntValue(row+3, 4);
			//gcfgs.roleCmn.buySkillPointMaxTimes = sheet.getShortValue(row+4, 3);
			//gcfgs.roleCmn.buySkillPointVal = sheet.getIntValue(row + 4, 5);
			{
				String str = sheet.getStringValue(row + 4, 7);
				String[] v = str.split(";");
				gcfgs.roleCmn.buySkillPointPrice = new ArrayList<Integer>();
				for(String e : v )
				{
					gcfgs.roleCmn.buySkillPointPrice.add((int)Float.parseFloat(e.trim()));
				}
			}
		}
		
		gcfgs.combatCmn = new SBean.CombatCmnCFGS();
		gcfgs.combatCmn.generalExpItemIDs = new ArrayList<Short>();
		{
			final int row = locateColTag(tname, sheet, 0, "战役相关");
			int col = 5;
			while( sheet.isNotEmpty(row+2, col) )
			{
				short iid = sheet.getShortValue(row+2, col);
				if( ! itemIDs.containsKey(iid) )
					throw new Exception(tname + " 引用武将经验道具ID " + iid + "不存在");
				gcfgs.combatCmn.generalExpItemIDs.add(iid);
				++col;
			}
			if( gcfgs.combatCmn.generalExpItemIDs.size() != 2 )
				throw new Exception(tname + " 引用武将经验道具ID队列不存在");
			
			gcfgs.combatCmn.priceReset = new ArrayList<Integer>();
			col = 2;
			while( sheet.isNotEmpty(row + 4, col) )
			{
				gcfgs.combatCmn.priceReset.add(sheet.getIntValue(row + 4, col));
				++col;
			}
			{
				col = 2;
				short iid = sheet.getShortValue(row+7, col);
				if( ! itemIDs.containsKey(iid) )
					throw new Exception(tname + " 引用八卦阵赤壁扫荡道具ID " + iid + "不存在");
				gcfgs.combatCmn.combatEventSkipItem = iid;
			}
			{
				col = 2;
				short iid = sheet.getShortValue(row+8, col);
				if( ! itemIDs.containsKey(iid) )
					throw new Exception(tname + " 引用过关斩将扫荡道具ID " + iid + "不存在");
				gcfgs.combatCmn.marchSkipItem = iid;
			}
		}
		
		gcfgs.merc = new SBean.MercCFGS();
		{
			final int row = locateColTag(tname, sheet, 0, "雇佣兵");
			gcfgs.merc.maxHours = sheet.getIntValue(row+1, 2);
			gcfgs.merc.minGeneralLevel = sheet.getShortValue(row+1, 4);
			gcfgs.merc.timeGainA = sheet.getIntValue(row+2, 2);
			gcfgs.merc.timeGainB = sheet.getIntValue(row+2, 4);
			gcfgs.merc.hireGainA = sheet.getFloatValue(row+2, 6);
		}
		{
			final int row = locateColTag(tname, sheet, 0, "转盘免费次数");
			gcfgs.merc.timedisk1 = sheet.getShortValue(row+1, 2);
			gcfgs.merc.timedisk2 = sheet.getShortValue(row+2, 2);
		}
		
	}
	
	public void loadGeneralSkillUpgradeTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		for(int i = 0; i < GameData.GENERAL_SKILL_COUNT; ++i)
		{
			SBean.GeneralSkillCFGS scfg = gcfgs.generalsCmn.skills.get(i);
			int n = GameData.MAX_ROLE_LEVEL_CONFIG - scfg.maxLvl;
			scfg.money = new ArrayList<Integer>();
			for(int j = 0; j < n; ++j)
			{
				scfg.money.add(sheet.getIntValue(2 + j, i + 1));
			}
		}
	}
	
	public void loadGeneralUpgradeTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.generalsCmn.upgradeExp = new ArrayList<Integer>();
		for(int i = 0; i < GameData.MAX_ROLE_LEVEL_CONFIG; ++i)
		{
			gcfgs.generalsCmn.upgradeExp.add(sheet.getIntValue(i+1, 1));
		}
	}
	
	public void loadPetUpgradeTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.petsCmn.upgradeExp = new ArrayList<Integer>();
		gcfgs.petsCmn.feedingExp = new ArrayList<Integer>();
		for(int i = 0; i < GameData.MAX_ROLE_LEVEL_CONFIG; ++i)
		{
			gcfgs.petsCmn.upgradeExp.add(sheet.getIntValue(i+1, 1));
			gcfgs.petsCmn.feedingExp.add(sheet.getIntValue(i+1, 2));
		}
	}
	
	public void loadUpgradeTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.roleCmn.upgradeExp = new ArrayList<Integer>();
		for(int i = 0; i < GameData.MAX_ROLE_LEVEL_CONFIG; ++i)
		{
			gcfgs.roleCmn.upgradeExp.add(sheet.getIntValue(i+2, 1));
			gcfgs.roleCmn.vitMaxTable.add(sheet.getShortValue(i+2, 2));
			gcfgs.roleCmn.vitAddLvlup.add(sheet.getShortValue(i+2, 3));
		}
	}
	
	public void loadArena(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.arena = new SBean.ArenaCFGS();
				
		final int rowMain = locateColTag(tname, sheet, 0, "竞技场");
		gcfgs.arena.refreshTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowMain + 1, 2));
		gcfgs.arena.lvlReq = sheet.getByteValue(rowMain + 1, 5);
		gcfgs.arena.freeTimes = sheet.getByteValue(rowMain + 1, 8);
		gcfgs.arena.stoneRefresh = sheet.getByteValue(rowMain + 1, 11);
		gcfgs.arena.combatId = sheet.getShortValue(rowMain + 3, 2);
		gcfgs.arena.timesPrice = new ArrayList<Short>();
		String sprice = sheet.getStringValue(rowMain + 1, 15);
		{
			String[] slst = sprice.split(";");
			for(String e : slst)
			{
				short p = (short)(Float.parseFloat(e));
				gcfgs.arena.timesPrice.add(p);
			}
		}
		gcfgs.arena.coolTime = sheet.getByteValue(rowMain + 3, 11);
		gcfgs.arena.rankMax = sheet.getIntValue(rowMain + 3, 8);
		
		int robotLvlCeiling = sheet.getIntValue(rowMain+5, 2);
		int robotLvlFloor = sheet.getIntValue(rowMain+5, 5);
		List<Short> robotSpecialGenerals = new ArrayList<Short>();
		String str = sheet.getStringValue(rowMain+5, 8);
		String[] v = str.split(",");
		for (String e : v) {
			robotSpecialGenerals.add(Short.parseShort(e.trim()));
		}
		
		gcfgs.arena.rewards = new ArrayList<SBean.ArenaRewardCFGS>();
		final int rowRewards = locateColTag(tname, sheet, 0, "奖励");
		int col = 2;
		while( sheet.isNotEmpty(rowRewards+1, col) )
		{
			int rankFloor = sheet.getIntValue(rowRewards+1, col);
			if( rankFloor <= 0 )
				rankFloor = Short.MAX_VALUE;
			int money = sheet.getIntValue(rowRewards+2, col);
			int stone = sheet.getIntValue(rowRewards+4, col);
			short point = sheet.getShortValue(rowRewards+3, col);
			short item1 = sheet.getShortValue(rowRewards+5, col);
			byte item1Count = sheet.getByteValue(rowRewards+6, col);
			short item2 = sheet.getShortValue(rowRewards+7, col);
			byte item2Count = sheet.getByteValue(rowRewards+8, col);
			
			gcfgs.arena.rewards.add(new SBean.ArenaRewardCFGS(rankFloor, money, stone, point, item1, item1Count, item2, item2Count));
			++col;
		}
		
		gcfgs.arena.bestRankRewards = new ArrayList<SBean.ArenaBestRankRewardCFGS>();
		{
			final int row = locateColTag(tname, sheet, 0, "历史最高奖励");
			col = 2;
			while( sheet.isNotEmpty(row+1, col) )
			{
				short rankFloor = sheet.getShortValue(row+1, col);
				if( rankFloor <= 0 )
					rankFloor = Short.MAX_VALUE;
				int money = sheet.getIntValue(row+2, col);
				int stone = sheet.getShortValue(row+3, col);
				gcfgs.arena.bestRankRewards.add(new SBean.ArenaBestRankRewardCFGS(rankFloor, money, stone));
				++col;
			}
		}
		
		gcfgs.arena.targets = new ArrayList<SBean.ArenaTargetCFGS>();
		{
			final int rowTargets = locateColTag(tname, sheet, 0, "可见对手");
			col = 2;
			int rankFloor = 0;
			while( sheet.isNotEmpty(rowTargets+1, col) )
			{
				rankFloor = sheet.getIntValue(rowTargets+1, col);
				SBean.ArenaTargetCFGS t = new SBean.ArenaTargetCFGS();
				t.rankFloor = rankFloor;
				t.deltaMin = new ArrayList<Integer>();
				t.deltaMax = new ArrayList<Integer>();
				t.deltaMin.add(sheet.getIntValue(rowTargets+2, col));
				t.deltaMax.add(sheet.getIntValue(rowTargets+3, col));
				t.deltaMin.add(sheet.getIntValue(rowTargets+4, col));
				t.deltaMax.add(sheet.getIntValue(rowTargets+5, col));
				t.deltaMin.add(sheet.getIntValue(rowTargets+6, col));
				t.deltaMax.add(sheet.getIntValue(rowTargets+7, col));
				t.seeMax = sheet.getIntValue(rowTargets+8, col);
				gcfgs.arena.targets.add(t);
				++col;
			}
			if( rankFloor != gcfgs.arena.rankMax )
				throw new Exception(tname + "排名数量必须和可见对手最后一列一致 ");
		}

		class RobotGeneral {
			public short lvlLimit;
			public List<Short> vals = new ArrayList<Short>();
			public List<Short> poss = new ArrayList<Short>();
		}
		List<RobotGeneral> robotGenerals = new ArrayList<RobotGeneral>();
		{
			final int rowGenerals = locateColTag(tname, sheet, 0, "机器人武将");
			col = 2;
			while( sheet.isNotEmpty(rowGenerals+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowGenerals+1, col);
				str = sheet.getStringValue(rowGenerals+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add(Short.parseShort(e.trim()));
				}
				str = sheet.getStringValue(rowGenerals+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add(Short.parseShort(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "id数量和权重数量不一致");
				robotGenerals.add(g);
				++col;
			}
		}
		
		List<RobotGeneral> robotGeneralAdvs = new ArrayList<RobotGeneral>();
		{
			final int rowGenerals = locateColTag(tname, sheet, 0, "机器人武将品级");
			col = 2;
			while( sheet.isNotEmpty(rowGenerals+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowGenerals+1, col);
				str = sheet.getStringValue(rowGenerals+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add(Short.parseShort(e.trim()));
				}
				str = sheet.getStringValue(rowGenerals+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add(Short.parseShort(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "品级数量和权重数量不一致");
				robotGeneralAdvs.add(g);
				++col;
			}
		}
		
		List<RobotGeneral> robotGeneralEvos = new ArrayList<RobotGeneral>();
		List<Short> specialEvos = new ArrayList<Short>();
		{
			final int rowGenerals = locateColTag(tname, sheet, 0, "机器人武将星级");
			col = 2;
			while( sheet.isNotEmpty(rowGenerals+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowGenerals+1, col);
				str = sheet.getStringValue(rowGenerals+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add(Short.parseShort(e.trim()));
				}
				str = sheet.getStringValue(rowGenerals+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add(Short.parseShort(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "星级数量和权重数量不一致");
				robotGeneralEvos.add(g);
				specialEvos.add(sheet.getShortValue(rowGenerals+4, col));
				++col;
			}
			if( robotGeneralEvos.size() != specialEvos.size() )
				throw new Exception(tname + "星级数量和特殊星级数量不一致");
		}
		
		List<RobotGeneral> robotGeneralCounts = new ArrayList<RobotGeneral>();
		{
			final int rowGenerals = locateColTag(tname, sheet, 0, "机器人武将人数");
			col = 2;
			while( sheet.isNotEmpty(rowGenerals+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowGenerals+1, col);
				str = sheet.getStringValue(rowGenerals+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add((short)Float.parseFloat(e.trim()));
				}
				str = sheet.getStringValue(rowGenerals+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add((short)Float.parseFloat(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "人数数量和权重数量不一致");
				robotGeneralCounts.add(g);
				++col;
			}
		}
		
		List<RobotGeneral> robotPets = new ArrayList<RobotGeneral>();
		{
			final int rowPets = locateColTag(tname, sheet, 0, "机器人宠物");
			col = 2;
			while( sheet.isNotEmpty(rowPets+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowPets+1, col);
				str = sheet.getStringValue(rowPets+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add(Short.parseShort(e.trim()));
				}
				str = sheet.getStringValue(rowPets+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add(Short.parseShort(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "宠物id数量和权重数量不一致");
				robotPets.add(g);
				++col;
			}
		}
		
		List<RobotGeneral> robotPetEvos = new ArrayList<RobotGeneral>();
		{
			final int rowPets = locateColTag(tname, sheet, 0, "机器人宠物星级");
			col = 2;
			while( sheet.isNotEmpty(rowPets+1, col) )
			{
				RobotGeneral g = new RobotGeneral();
				g.lvlLimit = sheet.getShortValue(rowPets+1, col);
				str = sheet.getStringValue(rowPets+2, col);
				v = str.split(",");
				for (String e : v) {
					g.vals.add(Short.parseShort(e.trim()));
				}
				str = sheet.getStringValue(rowPets+3, col);
				v = str.split(",");
				for (String e : v) {
					g.poss.add(Short.parseShort(e.trim()));
				}
				if( g.poss.size() != g.vals.size() )
					throw new Exception(tname + "宠物星级数量和权重数量不一致");
				robotPetEvos.add(g);
				++col;
			}
		}
		
		gcfgs.superArena = new SBean.SuperArenaCFGS();
		{
			int rowStart = locateColTag(tname, sheet, 0, "炼狱场");
			gcfgs.superArena.combatId = sheet.getShortValue(rowStart + 1, 2);
			gcfgs.superArena.lvlReq = sheet.getByteValue(rowStart + 1, 5);
			gcfgs.superArena.freeTimes = sheet.getByteValue(rowStart + 1, 8);
			gcfgs.superArena.stoneRefresh = sheet.getByteValue(rowStart + 1, 11);
			gcfgs.superArena.coolTime = sheet.getByteValue(rowStart + 1, 14);
			String priceStr = sheet.getStringValue(rowStart + 1, 17);
			{
				gcfgs.superArena.timesPrice = new ArrayList<Short>();
				String[] slst = priceStr.split(";");
				for(String e : slst)
				{
					short p = (short)(Float.parseFloat(e));
					gcfgs.superArena.timesPrice.add(p);
				}
			}
			gcfgs.superArena.hideTeamDaySpan = sheet.getIntValue(rowStart + 2, 2);
			gcfgs.superArena.hideTeamCost = new ArrayList<Integer>();
			int offsetRow = 3;
			while ( sheet.isNotEmpty(rowStart+offsetRow, 2) )
			{
				gcfgs.superArena.hideTeamCost.add(sheet.getIntValue(rowStart + offsetRow++, 2));
			}
			gcfgs.superArena.fightStartTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowStart + 2, 5));
			gcfgs.superArena.fightEndTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowStart + 3, 5));
			gcfgs.superArena.hideOrderVipReq = sheet.getIntValue(rowStart + 2, 8);
			gcfgs.superArena.fightWinReward = sheet.getIntValue(rowStart + 4, 5);
			gcfgs.superArena.fightLoseReward = sheet.getIntValue(rowStart + 5, 5);
		}
		
		gcfgs.superArena.targets = new ArrayList<SBean.SuperArenaTargetCFGS>();
		{
			int rowStart = locateColTag(tname, sheet, 0, "炼狱场搜索对手");
			col = 2;
			int lastRankFloor = 0;
			while( sheet.isNotEmpty(rowStart+1, col) )
			{
				int rankFloor = sheet.getIntValue(rowStart+1, col);
				if (rankFloor < 0)
					rankFloor = Integer.MAX_VALUE;
				if (lastRankFloor >= rankFloor)
					throw new Exception("炼狱场搜索对手  : row=" + rowStart + 1 + ", col=" + col + " 当前名次小于前面列的名次!");
				SBean.SuperArenaTargetCFGS t = new SBean.SuperArenaTargetCFGS();
				t.rankFloor = rankFloor;
				t.deltaRanks = new ArrayList<SBean.SuperArenaRankCFGS>();
				int rowOffset = 2;
				while (sheet.isNotEmpty(rowStart+rowOffset, col))
				{
					SBean.SuperArenaRankCFGS rankcfg = new SBean.SuperArenaRankCFGS();
					rankcfg.deltaMin = sheet.getIntValue(rowStart+rowOffset++, col);
					rankcfg.deltaMax = sheet.getIntValue(rowStart+rowOffset++, col);
					t.deltaRanks.add(rankcfg);
				}
				gcfgs.superArena.targets.add(t);
				lastRankFloor = rankFloor;
				++col;
			}
		}
		
		gcfgs.superArena.rewards = new ArrayList<SBean.SuperArenaRewardCFGS>();
		{
			final int rowStart = locateColTag(tname, sheet, 0, "炼狱场排名奖励");
			int colLocal = 2;
			while( sheet.isNotEmpty(rowStart+1, colLocal) )
			{
				int rankFloor = sheet.getIntValue(rowStart+1, colLocal);
				if( rankFloor <= 0 )
					rankFloor = Integer.MAX_VALUE;
				int point = sheet.getIntValue(rowStart+2, colLocal);
				
				gcfgs.superArena.rewards.add(new SBean.SuperArenaRewardCFGS(rankFloor, point));
				++colLocal;
			}
		}
		
		
		String arenaCfg = "{ lvlReq= " + gcfgs.arena.lvlReq;
		arenaCfg += ",rankMax=" + gcfgs.arena.rankMax;
		arenaCfg += ",refreshTime=" + gcfgs.arena.refreshTime;
		arenaCfg += ",freeTimes=" + gcfgs.arena.freeTimes;
		arenaCfg += ",stoneRefresh=" + gcfgs.arena.stoneRefresh;
		arenaCfg += ",coolTime=" + gcfgs.arena.coolTime;
		arenaCfg += ",combatId=" + gcfgs.arena.combatId;
		arenaCfg += ",robotLvlCeiling=" + robotLvlCeiling;
		arenaCfg += ",robotLvlFloor=" + robotLvlFloor;
		arenaCfg += ",robotSpecialGenerals={";
		for(short e : robotSpecialGenerals)
			arenaCfg += new Short(e).toString() + ",";
		arenaCfg += "}, timesPrice={";
		for(short e : gcfgs.arena.timesPrice)
			arenaCfg += new Short(e).toString() + ",";
		arenaCfg += "}, rewards={";
		for(SBean.ArenaRewardCFGS e : gcfgs.arena.rewards)
		{
			arenaCfg += "{rankFloor=" + e.rankFloor + ", money=" + e.money + ",point=" + e.point + ",stone=" + e.stone + 
					",item1=" + e.item1 + ",item1Count=" + e.item1Count + ",item2=" + e.item2 + ",item2Count=" + e.item2Count + "},";
		}
		arenaCfg += "}, robotGenerals={";
		for (RobotGeneral g : robotGenerals) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit + ", ids={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
		}
		arenaCfg += "}, robotGeneralAdvs={";
		for (RobotGeneral g : robotGeneralAdvs) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit + ", adv={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
		}
		arenaCfg += "}, robotGeneralEvos={";
		int i = 0;
		for (RobotGeneral g : robotGeneralEvos) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit;
			arenaCfg += ",specialEvo=" + specialEvos.get(i) + ", evo={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
			i ++;
		}
		arenaCfg += "}, robotGeneralCounts={";
		for (RobotGeneral g : robotGeneralCounts) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit + ", counts={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
		}
		arenaCfg += "}, robotPets={";
		for (RobotGeneral g : robotPets) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit + ", ids={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
		}
		arenaCfg += "}, robotPetEvos={";
		for (RobotGeneral g : robotPetEvos) {
			arenaCfg += "{lvlLimit=" + g.lvlLimit + ", evo={";
			for (Short e : g.vals) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}, poss={";
			for (Short e : g.poss) {
				arenaCfg += e.shortValue() + ",";
			}
			arenaCfg += "}},";
		}
		
		arenaCfg += "}, \nsuperArena={";
		arenaCfg += " lvlReq= " + gcfgs.superArena.lvlReq;
		arenaCfg += ",freeTimes=" + gcfgs.superArena.freeTimes;
		arenaCfg += ",stoneRefresh=" + gcfgs.superArena.stoneRefresh;
		arenaCfg += ",coolTime=" + gcfgs.superArena.coolTime;
		arenaCfg += ",combatId=" + gcfgs.superArena.combatId;
		arenaCfg += ", \ntimesPrice={";
		for(short e : gcfgs.superArena.timesPrice)
			arenaCfg += new Short(e).toString() + ",";
		arenaCfg += "}, \nhideDaySpan=" + gcfgs.superArena.hideTeamDaySpan;
		arenaCfg += ", \nhideCost={";
		for (int c : gcfgs.superArena.hideTeamCost)
			arenaCfg += String.valueOf(c) + ",";
		arenaCfg += "}, \nfightStartTime=" + gcfgs.superArena.fightStartTime;
		arenaCfg += ", fightEndTime=" + gcfgs.superArena.fightEndTime;
		arenaCfg += ", hideOrderVipReq=" + gcfgs.superArena.hideOrderVipReq;
		arenaCfg += ", fightWinReward=" + gcfgs.superArena.fightWinReward;
		arenaCfg += ", fightLoseReward=" + gcfgs.superArena.fightLoseReward;
		arenaCfg += ", \nrewards={";
		for(SBean.SuperArenaRewardCFGS e : gcfgs.superArena.rewards)
			arenaCfg += "{" + e.rankFloor + "," + e.point + "},";
		arenaCfg += "}\n}\n}";
		luaCfg.put("arena", arenaCfg);
	}
	
	public void loadMarch(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.march = new SBean.MarchCFGS();

		final int rowMain = locateColTag(tname, sheet, 0, "远征");
		gcfgs.march.refreshTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowMain + 1, 2));
		gcfgs.march.lvlReq = sheet.getByteValue(rowMain + 1, 5);
		gcfgs.march.wipeItemId = sheet.getShortValue(rowMain + 1, 8);
		gcfgs.march.powerUpDays = sheet.getByteValue(rowMain + 3, 2);
		gcfgs.march.powerDownDays = sheet.getByteValue(rowMain + 3, 5);
		gcfgs.march.powerUpStage = sheet.getByteValue(rowMain + 5, 2);
		gcfgs.march.powerDownStage = sheet.getByteValue(rowMain + 5, 5);
		
		gcfgs.march.stages = new ArrayList<SBean.MarchStageCFGS>();
		final int rowStages = locateColTag(tname, sheet, 0, "关卡");
		int col = 2;
		while( sheet.isNotEmpty(rowStages+1, col) )
		{
			short combatId = sheet.getShortValue(rowStages+2, col);
			int money1 = sheet.getIntValue(rowStages+3, col);
			int money2 = sheet.getIntValue(rowStages+4, col);
			short point = sheet.getShortValue(rowStages+5, col);
			String str = sheet.getStringValue(rowStages+6, col);
			List<SBean.MarchDropCFGS> drops = new ArrayList<SBean.MarchDropCFGS>();
			String[] dstrs = str.split(";");
			for (String d : dstrs) {
				String[] dstrs2 = d.split(",");
				if (dstrs2.length >= 2) {
					byte maxLvl = Byte.parseByte(dstrs2[0].trim());
					short drop = Short.parseShort(dstrs2[1].trim());
					drops.add(new SBean.MarchDropCFGS(maxLvl, drop));
				}
			}
			float fightPowerScale = sheet.getFloatValue(rowStages+7, col);
			gcfgs.march.stages.add(new SBean.MarchStageCFGS(combatId, money1, money2, point, drops, fightPowerScale));
			++col;
		}
		
		gcfgs.march.targets = new ArrayList<SBean.MarchTargetCFGS>();
		{
			final int rowTargets = locateColTag(tname, sheet, 0, "可见对手");
			col = 2;
			int rankFloor = 0;
			while( sheet.isNotEmpty(rowTargets+1, col) )
			{
				rankFloor = sheet.getIntValue(rowTargets+1, col);
				SBean.MarchTargetCFGS t = new SBean.MarchTargetCFGS();
				t.rankFloor = rankFloor;
				t.deltaMin = new ArrayList<Integer>();
				t.deltaMax = new ArrayList<Integer>();
				for (int i = 0; i < 15; i ++) {
					t.deltaMin.add(sheet.getIntValue(rowTargets+2+i*2, col));
					t.deltaMax.add(sheet.getIntValue(rowTargets+3+i*2, col));
				}
				gcfgs.march.targets.add(t);
				++col;
			}
			if( rankFloor != gcfgs.arena.rankMax )
				throw new Exception(tname + "排名数量必须和可见对手最后一列一致 ");
		}
		
		gcfgs.march.powerUps = new ArrayList<SBean.MarchPowerUpCFGS>();
		final int rowPowerUps = locateColTag(tname, sheet, 0, "能量上涨");
		col = 2;
		while( sheet.isNotEmpty(rowPowerUps+1, col) )
		{
			byte stage = sheet.getByteValue(rowPowerUps+1, col);
			short powerUpByDay = sheet.getShortValue(rowPowerUps+2, col);
			gcfgs.march.powerUps.add(new SBean.MarchPowerUpCFGS(stage, powerUpByDay));
			++col;
		}

		gcfgs.march.powerDowns = new ArrayList<SBean.MarchPowerUpCFGS>();
		final int rowPowerDowns = locateColTag(tname, sheet, 0, "能量下降");
		col = 2;
		while( sheet.isNotEmpty(rowPowerDowns+1, col) )
		{
			byte stage = sheet.getByteValue(rowPowerDowns+1, col);
			short powerDownByDay = sheet.getShortValue(rowPowerDowns+2, col);
			gcfgs.march.powerDowns.add(new SBean.MarchPowerUpCFGS(stage, powerDownByDay));
			++col;
		}

		gcfgs.march.opponentAdjust = new ArrayList<SBean.MarchOpponentAdjustCFGS>();
		final int rowOpponentAdjust = locateColTag(tname, sheet, 0, "对手调整");
		col = 2;
		while( sheet.isNotEmpty(rowOpponentAdjust+1, col) )
		{
			short roleLevelMax = sheet.getShortValue(rowOpponentAdjust+1, col);
			List<Float> hpScales = new ArrayList<Float>();
			List<Float> attackScales = new ArrayList<Float>();
			int row = rowOpponentAdjust+2;
			while ( sheet.isNotEmpty(row, col) ) {
				hpScales.add(sheet.getFloatValue(row, col));
				row ++;
				attackScales.add(sheet.getFloatValue(row, col));
				row ++;
			}
			gcfgs.march.opponentAdjust.add(new SBean.MarchOpponentAdjustCFGS(roleLevelMax, hpScales, attackScales));
			++col;
		}

		List<Byte> robotLvlLow = new ArrayList<Byte>();
		List<Byte> robotLvlHigh = new ArrayList<Byte>();
		final int rowRobotLvl = locateColTag(tname, sheet, 0, "机器人等级差");
		col = 2;
		int row = rowRobotLvl+1;
		while ( sheet.isNotEmpty(row, col) ) {
			byte lvlHigh = sheet.getByteValue(row++, col);
			byte lvlLow = sheet.getByteValue(row++, col);
			robotLvlHigh.add(lvlHigh);
			robotLvlLow.add(lvlLow);
		}

		String marchCfg = "{ lvlReq= " + gcfgs.march.lvlReq;
		marchCfg += ",refreshTime=" + gcfgs.march.refreshTime;
		marchCfg += ",wipeItemId=" + gcfgs.march.wipeItemId;
		marchCfg += ",powerUpDays=" + gcfgs.march.powerUpDays;
		marchCfg += ",powerDownDays=" + gcfgs.march.powerDownDays;
		marchCfg += ",powerUpStage=" + gcfgs.march.powerUpStage;
		marchCfg += ",powerDownStage=" + gcfgs.march.powerDownStage;
		marchCfg += ", stages={";
		for(SBean.MarchStageCFGS e : gcfgs.march.stages)
		{
			marchCfg += "{combatId=" + e.combatId + ", money1=" + e.money1 + ", money2=" + e.money2 + ",point=" + e.point + "},";
		}
		marchCfg += "},oppoAdjust={";
		for(SBean.MarchOpponentAdjustCFGS e : gcfgs.march.opponentAdjust)
		{
			marchCfg += "{roleLevelMax=" + e.roleLevelMax + ", hpScales={";
			for (Float f : e.hpScale)
				marchCfg += f + ", ";
			marchCfg += "}, attackScales={";
			for (Float f : e.attackScale)
				marchCfg += f + ", ";
			marchCfg += "}},";
		}
		marchCfg += "},robotLvl={";
		for(int i = 0; i < robotLvlHigh.size(); i ++) {
			marchCfg += "{high=" + robotLvlHigh.get(i) + ", low=" + robotLvlLow.get(i) + "},";
		}
		marchCfg += "}}";
		luaCfg.put("march", marchCfg);
	}
	
	public void loadGeneralPower(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.power = new ArrayList<SBean.PowerCFGS>();

		int row = 2;
		int col = 0;
		while( sheet.isNotEmpty(row, col) )
		{
			short generalId = sheet.getShortValue(row, col);
			int initPower = sheet.getIntValue(row, col+1);
			int upPower = sheet.getIntValue(row, col+2);
			gcfgs.power.add(new SBean.PowerCFGS(generalId, initPower, upPower));
			++row;
		}
	}
	
	public void loadWeapon(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.weapon = new SBean.WeaponCFGS();

		gcfgs.weapon.basic = new ArrayList<SBean.WeaponBasicCFGS>();
		int row = 1;
		int col = 0;
		Set<Short> ids = new HashSet<Short>();
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.WeaponBasicCFGS basic = new SBean.WeaponBasicCFGS();
			basic.gid = sheetBasic.getShortValue(row, col ++);
			ids.add(basic.gid);
			basic.name = sheetBasic.getStringValue(row, col ++);
			basic.props = new ArrayList<SBean.WeaponPropCFGS>();
			for (int i = 0; i < 3; i ++)
			{
				SBean.WeaponPropCFGS prop = new SBean.WeaponPropCFGS();
				prop.propId = sheetBasic.getShortValue(row, col ++);
				prop.type = sheetBasic.getByteValue(row, col ++);
				prop.value = sheetBasic.getFloatValue(row, col ++);
				basic.props.add(prop);
			}
			basic.reqAdvLvl = sheetBasic.getByteValue(row, col ++);
			basic.iconId = sheetBasic.getShortValue(row, col ++);
			basic.desc = sheetBasic.getStringValue(row, col ++);
			basic.recommend = sheetBasic.getStringValue(row, col ++);
			gcfgs.weapon.basic.add(basic);
			++row;
			col = 0;
		}
		
		for (SBean.GeneralCFGS g : gcfgs.generals)
			if (g.id < 1000 && !ids.contains(g.id))
				throw new Exception(tname + "存在没有配置神兵的武将，id=" + g.id);
		
		ExcelSheet sheetEnhanceStage = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		gcfgs.weapon.enhance = new ArrayList<SBean.WeaponEnhanceCFGS>();
		row = 1;
		col = 0;
		while (sheetEnhanceStage.isNotEmpty(row, col))
		{
			SBean.WeaponEnhanceCFGS enhance = new SBean.WeaponEnhanceCFGS();
			enhance.gid = sheetEnhanceStage.getShortValue(row, col ++);
			enhance.enhanceStages = new ArrayList<SBean.WeaponEnhanceStageCFGS>();
			for (int i = 0; i < 5; i ++)
			{
				SBean.WeaponEnhanceStageCFGS stage = new SBean.WeaponEnhanceStageCFGS();
				stage.imageId = sheetEnhanceStage.getShortValue(row, col ++);
				stage.enhanceCount = sheetEnhanceStage.getByteValue(row, col ++);
				stage.effectId = sheetEnhanceStage.getShortValue(row, col ++);
				enhance.enhanceStages.add(stage);
			}
			gcfgs.weapon.enhance.add(enhance);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetEnhanceCost = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		row = 0;
		col = 1;
		while (sheetEnhanceCost.isNotEmpty(row, col))
		{
			short gid = sheetEnhanceCost.getShortValue(row ++, col);
			List<SBean.DropEntryNew> costs = new ArrayList<SBean.DropEntryNew>();
			while (sheetEnhanceCost.isNotEmpty(row, col))
			{
				for (int i = 0; i < 3; i ++)
				{
					SBean.DropEntryNew cost = new SBean.DropEntryNew();
					cost.type = sheetEnhanceCost.getByteValue(row ++, col);
					cost.id = sheetEnhanceCost.getShortValue(row ++, col);
					cost.arg = sheetEnhanceCost.getIntValue(row ++, col);
					costs.add(cost);
				}				
			}
			boolean found = false;
			for (SBean.WeaponEnhanceCFGS enhance : gcfgs.weapon.enhance)
			{
				if (enhance.gid == gid)
				{
					found = true;
					enhance.enhanceCosts = costs;
				}
			}
			if (!found)
				throw new Exception(tname + "强化消耗表中的id" + gid + "在强化上线表中找不到");
			col ++;
			row = 0;
		}
		
		ExcelSheet sheetEnhanceProp = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
		row = 0;
		col = 1;
		while (sheetEnhanceProp.isNotEmpty(row, col))
		{
			short gid = sheetEnhanceProp.getShortValue(row ++, col);
			List<SBean.WeaponPropCFGS> props = new ArrayList<SBean.WeaponPropCFGS>();
			while (sheetEnhanceProp.isNotEmpty(row, col))
			{
				for (int i = 0; i < 5; i ++)
				{
					SBean.WeaponPropCFGS prop = new SBean.WeaponPropCFGS();
					prop.propId = sheetEnhanceProp.getShortValue(row ++, col);
					prop.type = sheetEnhanceProp.getByteValue(row ++, col);
					prop.value = sheetEnhanceProp.getFloatValue(row ++, col);
					props.add(prop);
				}
			}
			boolean found = false;
			for (SBean.WeaponEnhanceCFGS enhance : gcfgs.weapon.enhance)
			{
				if (enhance.gid == gid)
				{
					found = true;
					enhance.enhanceProps = props;
				}
			}
			if (!found)
				throw new Exception(tname + "强化属性表中的id" + gid + "在强化上线表中找不到");
			col ++;
			row = 0;
		}
		
		for (SBean.WeaponEnhanceCFGS enhance : gcfgs.weapon.enhance)
		{
			if (enhance.enhanceCosts == null || enhance.enhanceProps == null)
				throw new Exception(tname + "强化上线表中的id" + enhance.gid + "缺强化属性或消耗数据");
		}

		ExcelSheet sheetResetCost = ket.moi.Factory.newExcelReader(fileName).getSheet(4);
		gcfgs.weapon.reset = new SBean.WeaponResetCFGS();
		SBean.WeaponCostCFGS cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(1, 1);
		cost.costStone = sheetResetCost.getIntValue(1, 2);
		gcfgs.weapon.reset.noLock = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(2, 1);
		cost.costStone = sheetResetCost.getIntValue(2, 2);
		gcfgs.weapon.reset.lock1 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(3, 1);
		cost.costStone = sheetResetCost.getIntValue(3, 2);
		gcfgs.weapon.reset.lock2 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(4, 1);
		cost.costStone = sheetResetCost.getIntValue(4, 2);
		gcfgs.weapon.reset.lock3 = cost;
		
		gcfgs.weapon.evoCosts = new ArrayList<SBean.WeaponEvoCostCFGS>();
		for (int i = 0; i < 4; i ++)
		{
			SBean.WeaponEvoCostCFGS evocost = new SBean.WeaponEvoCostCFGS();
			evocost.costMoney = sheetResetCost.getIntValue(i + 1, 5);
			evocost.costEquipID0 = sheetResetCost.getShortValue(i + 1, 7);
			if( evocost.costEquipID0 > 0 && ! equipIDs.containsKey(evocost.costEquipID0) )
				throw new Exception(tname + "进化所需装备id" + evocost.costEquipID0 + "不存在");
			evocost.costEquipID1 = sheetResetCost.getShortValue(i + 1, 8);
			if( evocost.costEquipID1 > 0 && ! equipIDs.containsKey(evocost.costEquipID1) )
				throw new Exception(tname + "进化所需装备id" + evocost.costEquipID1 + "不存在");
			gcfgs.weapon.evoCosts.add(evocost);
		}
		
		
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(10, 1);
		cost.costStone = sheetResetCost.getIntValue(10, 2);
		gcfgs.weapon.reset.nlock1 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(11, 1);
		cost.costStone = sheetResetCost.getIntValue(11, 2);
		gcfgs.weapon.reset.nlock2 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(12, 1);
		cost.costStone = sheetResetCost.getIntValue(12, 2);
		gcfgs.weapon.reset.nlock3 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(13, 1);
		cost.costStone = sheetResetCost.getIntValue(13, 2);
		gcfgs.weapon.reset.nlock4 = cost;
		cost = new SBean.WeaponCostCFGS();
		cost.costMoney = sheetResetCost.getIntValue(14, 1);
		cost.costStone = sheetResetCost.getIntValue(14, 2);
		gcfgs.weapon.reset.nlock5 = cost;
		
		ExcelSheet sheetResetProp = ket.moi.Factory.newExcelReader(fileName).getSheet(5);
		gcfgs.weapon.reset.propPasses = new ArrayList<SBean.WeaponResetPropPassCFGS>();
		for (int i = 0; i < 5; i ++)
		{
			SBean.WeaponResetPropPassCFGS pass = new SBean.WeaponResetPropPassCFGS();
			pass.pass = new ArrayList<SBean.WeaponResetPropCFGS>(); 
			row = 2;
			col = 1 + i * 7;
			while (sheetResetProp.isNotEmpty(row, col))
			{
				SBean.WeaponResetPropCFGS prop = new SBean.WeaponResetPropCFGS();
				prop.propId = sheetResetProp.getShortValue(row, col ++);
				prop.type = sheetResetProp.getByteValue(row, col ++);
				prop.valueMin = sheetResetProp.getFloatValue(row, col ++);
				prop.valueMax = sheetResetProp.getFloatValue(row, col ++);
				prop.rule = sheetResetProp.getByteValue(row, col ++);
				prop.poss = sheetResetProp.getShortValue(row, col ++);
				pass.pass.add(prop);
				++row;
				col = 1 + i * 7;
			}
			gcfgs.weapon.reset.propPasses.add(pass);			
		}
		
		ExcelSheet sheetPossibility = ket.moi.Factory.newExcelReader(fileName).getSheet(6);
		gcfgs.weapon.reset.rules = new ArrayList<SBean.WeaponResetRuleCFGS>();
		row = 2;
		col = 0;
		while (sheetPossibility.isNotEmpty(row, col))
		{
			SBean.WeaponResetRuleCFGS rule = new SBean.WeaponResetRuleCFGS();
			rule.id = sheetPossibility.getByteValue(row, col ++);
			rule.percent = new ArrayList<Byte>();
			for (int i = 0; i < 5; i ++)
				rule.percent.add(sheetPossibility.getByteValue(row, col ++));
			gcfgs.weapon.reset.rules.add(rule);
			++row;
			col = 0;
		}
		
		gcfgs.weapon.reset.newPropPurple = new ArrayList<SBean.WeaponResetNewPropCFGS>();
		row = 2;
		col = 8;
		while (sheetPossibility.isNotEmpty(row, col))
		{
			SBean.WeaponResetNewPropCFGS newProp = new SBean.WeaponResetNewPropCFGS();
			newProp.percent = new ArrayList<Byte>();
			for (int i = 0; i < 4; i ++)
				newProp.percent.add(sheetPossibility.getByteValue(row, col ++));
			gcfgs.weapon.reset.newPropPurple.add(newProp);
			++row;
			col = 8;
		}
		
		gcfgs.weapon.reset.newPropOrange = new ArrayList<SBean.WeaponResetNewPropCFGS>();
		row = 2;
		col = 14;
		while (sheetPossibility.isNotEmpty(row, col))
		{
			SBean.WeaponResetNewPropCFGS newProp = new SBean.WeaponResetNewPropCFGS();
			newProp.percent = new ArrayList<Byte>();
			for (int i = 0; i < 4; i ++)
				newProp.percent.add(sheetPossibility.getByteValue(row, col ++));
			gcfgs.weapon.reset.newPropOrange.add(newProp);
			++row;
			col = 14;
		}
		
		String weaponCfg = "{ basic={";
		for (SBean.WeaponBasicCFGS b : gcfgs.weapon.basic)
		{
			weaponCfg += "[" + b.gid + "]={gid=" + b.gid;
			weaponCfg += ", name=\"" + b.name;
			weaponCfg += "\", reqAdvLvl=" + b.reqAdvLvl;
			weaponCfg += ", iconId=" + b.iconId;
			weaponCfg += ", desc=\"" + b.desc;
			weaponCfg += "\", recommend=\"" + b.recommend;
			weaponCfg += "\", props={";
			for (int i = 0; i < 3; i ++) {
				SBean.WeaponPropCFGS p = b.props.get(i);
				weaponCfg += "{id=" + p.propId + ", type=" + p.type + ", value=" + p.value + "},";
			}
			weaponCfg += "}},";
		}
		weaponCfg += "}, enhance={";
		for (SBean.WeaponEnhanceCFGS enhance : gcfgs.weapon.enhance)
		{
			weaponCfg += "[" + enhance.gid + "]={gid=" + enhance.gid;
			weaponCfg += ", stages={";
			for (SBean.WeaponEnhanceStageCFGS stage : enhance.enhanceStages) {
				weaponCfg += "{imageId=" + stage.imageId + ", enhanceCount=" + stage.enhanceCount + ", effectId=" + stage.effectId + "},";
			}
			weaponCfg += "}, costs={";
			for (SBean.DropEntryNew c: enhance.enhanceCosts) {
				weaponCfg += "{type=" + c.type + ", id=" + c.id + ", count=" + c.arg + "},";
			}
			weaponCfg += "}, props={";
			for (SBean.WeaponPropCFGS p : enhance.enhanceProps) {
				weaponCfg += "{id=" + p.propId + ", type=" + p.type + ", value=" + p.value + "},";
			}
			weaponCfg += "}},";
		}
		weaponCfg += "}, evoCosts={";
		for (SBean.WeaponEvoCostCFGS c : gcfgs.weapon.evoCosts) {
			weaponCfg += "{costMoney=" + c.costMoney + ", costStone=" + 0 
					+ ", costEquipID0=" + c.costEquipID0 
					+ ", costEquipID1=" + c.costEquipID1
					+ "},";
		}
		weaponCfg += "}, reset={";
		weaponCfg += "noLock={costMoney=" + gcfgs.weapon.reset.noLock.costMoney + ", costStone=" + gcfgs.weapon.reset.noLock.costStone + "},";
		weaponCfg += "lock1={costMoney=" + gcfgs.weapon.reset.lock1.costMoney + ", costStone=" + gcfgs.weapon.reset.lock1.costStone + "},";
		weaponCfg += "lock2={costMoney=" + gcfgs.weapon.reset.lock2.costMoney + ", costStone=" + gcfgs.weapon.reset.lock2.costStone + "},";
		weaponCfg += "lock3={costMoney=" + gcfgs.weapon.reset.lock3.costMoney + ", costStone=" + gcfgs.weapon.reset.lock3.costStone + "},";
		weaponCfg += "}, reset2={";
		weaponCfg += "lock1={costMoney=" + gcfgs.weapon.reset.nlock1.costMoney + ", costStone=" + gcfgs.weapon.reset.nlock1.costStone + "},";
		weaponCfg += "lock2={costMoney=" + gcfgs.weapon.reset.nlock2.costMoney + ", costStone=" + gcfgs.weapon.reset.nlock2.costStone + "},";
		weaponCfg += "lock3={costMoney=" + gcfgs.weapon.reset.nlock3.costMoney + ", costStone=" + gcfgs.weapon.reset.nlock3.costStone + "},";
		weaponCfg += "lock4={costMoney=" + gcfgs.weapon.reset.nlock4.costMoney + ", costStone=" + gcfgs.weapon.reset.nlock4.costStone + "},";
		weaponCfg += "lock5={costMoney=" + gcfgs.weapon.reset.nlock5.costMoney + ", costStone=" + gcfgs.weapon.reset.nlock5.costStone + "},";
		weaponCfg += "}}";
		luaCfg.put("weapon", weaponCfg);
	}
	
	public void loadDungeon(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.dungeon = new SBean.DungeonCFGS();

		final int rowMain = locateColTag(tname, sheet, 0, "基本配置");
		gcfgs.dungeon.basic = new SBean.DungeonBasicCFGS();
		gcfgs.dungeon.basic.dailyTimes = sheet.getByteValue(rowMain+1, 2);
		gcfgs.dungeon.basic.boxStone = sheet.getShortValue(rowMain+1, 4);
		gcfgs.dungeon.basic.deployMinutes = sheet.getShortValue(rowMain+1, 8);
		
		gcfgs.dungeon.chapters = new ArrayList<SBean.DungeonChapterCFGS>();
		final int rowChapter = locateColTag(tname, sheet, 0, "章节");
		int row = rowChapter + 1;
		int col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DungeonChapterCFGS ccfg = new SBean.DungeonChapterCFGS();
			ccfg.openLvl = sheet.getShortValue(row+5, 2);
			ccfg.resetActivity = sheet.getIntValue(row+5, 4);
			ccfg.stages = new ArrayList<SBean.DungeonStageCFGS>();
			while (sheet.isNotEmpty(row, col))
			{
				SBean.DungeonStageCFGS scfg = new SBean.DungeonStageCFGS();
				scfg.combatId = sheet.getShortValue(row+1, col);
				scfg.boxDropIds = new ArrayList<Short>();
				String str = sheet.getStringValue(row+2, col);
				String[] strs = str.split(";");
				for (String s : strs) {
					short id = (short)Float.parseFloat(s);
					if (id > 0)
						scfg.boxDropIds.add(id);
				}
				scfg.treasureDrops = new ArrayList<SBean.DungeonTreasureCFGS>();
				str = sheet.getStringValue(row+3, col);
				strs = str.split(";");
				for (String s : strs) {
					String[] ss = s.split(",");
					SBean.DungeonTreasureCFGS t = new SBean.DungeonTreasureCFGS();
					t.percent = (byte)Float.parseFloat(ss[0]);
					t.drops = new ArrayList<Short>();
					for (int i = 1; i < ss.length; i ++) {
						short id = (short)Float.parseFloat(ss[i]);
						if (id > 0)
							t.drops.add(id);
					}
					scfg.treasureDrops.add(t);
				}
				str = sheet.getStringValue(row+4, col);
				strs = str.split(";");
				scfg.cupCount = (short)Float.parseFloat(strs[0]);
				scfg.cupPoss = (byte)Float.parseFloat(strs[1]);
				ccfg.stages.add(scfg);
				col ++;
			}
			ccfg.applies = new ArrayList<SBean.DungeonApplyCFGS>();
			String str = sheet.getStringValue(row+5, 10);
			String[] strs = str.split(";");
			for (String s : strs) {
				String[] ss = s.split(",");
				SBean.DungeonApplyCFGS acfg = new SBean.DungeonApplyCFGS();
				acfg.type = (byte)Float.parseFloat(ss[0]);
				acfg.id = (short)Float.parseFloat(ss[1]);
				ccfg.applies.add(acfg);
			}
			gcfgs.dungeon.chapters.add(ccfg);
			row += 6;
			col = 2;
		}
		
		gcfgs.dungeon.rewards = new ArrayList<SBean.DungeonRewardCFGS>();
		final int rowReward = locateColTag(tname, sheet, 0, "伤害奖励");
		col = 2;
		while( sheet.isNotEmpty(rowReward+1, col) )
		{
			int rankFloor = sheet.getIntValue(rowReward+1, col);
			if( rankFloor <= 0 )
				rankFloor = Short.MAX_VALUE;
			int point = sheet.getIntValue(rowReward+2, col);
			int money = sheet.getIntValue(rowReward+3, col);
			
			gcfgs.dungeon.rewards.add(new SBean.DungeonRewardCFGS(rankFloor, point, money));
			++col;
		}
		
		gcfgs.dungeon.beyondRewards = new ArrayList<SBean.DungeonBeyondRewardCFGS>();
		final int rowBeyondReward = locateColTag(tname, sheet, 0, "超越奖励");
		col = 2;
		while( sheet.isNotEmpty(rowBeyondReward+1, col) )
		{
			byte chapter = sheet.getByteValue(rowBeyondReward+1, col);
			int stone = sheet.getIntValue(rowBeyondReward+2, col);
			
			gcfgs.dungeon.beyondRewards.add(new SBean.DungeonBeyondRewardCFGS(chapter, stone));
			++col;
		}
	}
	
	public void loadRelation(String tname, String fileName, SBean.GameDataCFGC gcfgc
			, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetA = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		ExcelSheet sheetB = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		ExcelSheet sheetC = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.relation = new SBean.RelationsCFGS();
		gcfgs.relation.relations = new ArrayList<SBean.RelationCFGS>();
		gcfgs.relation.items = new ArrayList<SBean.RelationItemCFGS>();
		gcfgs.relation.consumes = new ArrayList<SBean.RelationConsumeCFGS>();
		final int rowStart = 1; // start row
		int row = rowStart;
		int col = 0;
		while( sheetA.isNotEmpty(row, 0) )
		{
			SBean.RelationCFGS cfgs = new SBean.RelationCFGS();
			cfgs.id = sheetA.getShortValue(row, col++);
			cfgs.name = sheetA.getStringValue(row, col++);
			cfgs.activateItems = new ArrayList<Short>();
			for (int i = 0; i < 5; i ++) {
				short iid = sheetA.getShortValue(row, col++);
				if (iid > 0)
					cfgs.activateItems.add(iid);
			}
			cfgs.props = new ArrayList<SBean.RelationPropCFGS>();
			for (int i = 0; i < 5; i ++) {
				SBean.RelationPropCFGS prop = new SBean.RelationPropCFGS();
				prop.id = sheetA.getShortValue(row, col++);
				prop.type = sheetA.getByteValue(row, col++);
				prop.value = sheetA.getFloatValue(row, col++);
				cfgs.props.add(prop);
			}
			gcfgs.relation.relations.add(cfgs);
			row++;
			col = 0;
		}
		
		row = rowStart;
		col = 0;
		while( sheetB.isNotEmpty(row, 0) )
		{
			SBean.RelationItemCFGS cfgs = new SBean.RelationItemCFGS();
			cfgs.iid = sheetB.getShortValue(row, col++);
			cfgs.gid = sheetB.getShortValue(row, col++);
			gcfgs.relation.items.add(cfgs);
			row++;
			col = 0;
		}
		
		row = 0;
		col = 1;
		while (sheetC.isNotEmpty(row, col)) {
			short gid = sheetC.getShortValue(row++, col);
			SBean.RelationConsumeCFGS con = new SBean.RelationConsumeCFGS(gid, new ArrayList<Integer>());
			while( sheetC.isNotEmpty(row, col) )
			{
				int consume = sheetC.getIntValue(row, col);
				con.consume.add(consume);
				row++;
			}
			gcfgs.relation.consumes.add(con);
			row = 0;
			col ++;
		}
	}
	

	public void loadHeadicon(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.headicon = new ArrayList<SBean.HeadIconCFGS>();
		
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.HeadIconCFGS basic1 = new SBean.HeadIconCFGS();
			
			basic1.sid =  sheetBasic.getShortValue(row, col ++);
			col ++;
			basic1.type =  sheetBasic.getShortValue(row, col ++);
			col ++;
			col ++;
//			basic1.stime = StringConverter.parseDayOfYear(sheetBasic.getStringValue(row, col++)) + 8*3600 + StringConverter.parseMinuteOfDay("00:00")*60;
			basic1.etime = sheetBasic.getShortValue(row, col ++);
			gcfgs.headicon.add(basic1);			
			row++;
			col = 0;		
		}
	}

	
	public void loadBestow(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);

		gcfgs.bestow = new ArrayList<SBean.BestowCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.BestowCFGS basic = new SBean.BestowCFGS();
			basic.sid = sheetBasic.getShortValue(row, col ++);
			basic.gid = sheetBasic.getShortValue(row, col ++);
			basic.level = sheetBasic.getShortValue(row, col ++);
			basic.eItems = new ArrayList<SBean.DropEntryNew>();
			for(int i =0 ;i<1;i++){
				SBean.DropEntryNew basic1 = new SBean.DropEntryNew();
				basic1.type = (byte)0;
				if(basic1.type==-1){
					continue;
				}
				basic1.id = sheetBasic.getShortValue(row, col ++);			
				basic1.arg= sheetBasic.getShortValue(row, col ++);
				basic.eItems.add(basic1);
			}
			gcfgs.bestow.add(basic);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetBasic1 = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		row = 0;
		col = 1;
		gcfgs.bestowcost = sheetBasic1.getShortValue(row++, col );
		gcfgs.bestowId = sheetBasic1.getShortValue(row, col );
		
		
		
		gcfgs.bestowItem = new SBean.BestowItemCFGS();
		gcfgs.bestowItem.iid1 = sheetBasic1.getShortValue(row, 1 );
		String gisd = sheetBasic1.getStringValue(row, 2);
		String gi [] = gisd.split(",");
		gcfgs.bestowItem.item1 = new ArrayList<Short>();
		if(gi!=null){
			for( String g :gi){
				gcfgs.bestowItem.item1.add(Short.parseShort(g.trim()));
			}
		}
		row++;
		gcfgs.bestowItem.iid2 = sheetBasic1.getShortValue(row, 1 );
		gisd = sheetBasic1.getStringValue(row, 2);
		gi  = gisd.split(",");
		gcfgs.bestowItem.item2 = new ArrayList<Short>();
		if(gi!=null){
			for( String g :gi){
				gcfgs.bestowItem.item2.add(Short.parseShort(g.trim()));
			}
		}
		row++;
	}
	
	public void loadBless(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);

		gcfgs.bless = new ArrayList<SBean.BlessCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.BlessCFGS basic = new SBean.BlessCFGS();
			basic.sid = sheetBasic.getShortValue(row, col ++);
			col ++;
			col ++;
			col ++;
			basic.eItems = new ArrayList<SBean.DropEntryNew>();
			for(int i =0 ;i<1;i++){
				SBean.DropEntryNew basic1 = new SBean.DropEntryNew();
				basic1.type = (byte)0;
				if(basic1.type==-1){
					continue;
				}
				basic1.id = sheetBasic.getShortValue(row, col ++);			
				basic1.arg= sheetBasic.getShortValue(row, col ++);
				basic.eItems.add(basic1);
			}
			col ++;
			col ++;
			basic.p1 = sheetBasic.getFloatValue(row, col ++);
			basic.p1Max = sheetBasic.getFloatValue(row, col ++);
			col ++;
			col ++;
			basic.p2 = sheetBasic.getFloatValue(row, col ++);
			basic.p2Max = sheetBasic.getFloatValue(row, col ++);
			col ++;
			col ++;
			basic.p3 = sheetBasic.getFloatValue(row, col ++);
			basic.p3Max = sheetBasic.getFloatValue(row, col ++);
			gcfgs.bless.add(basic);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetBasic1 = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		row = 0;
		col = 1;
		gcfgs.common = new SBean.CommomConstantCFGS();
		gcfgs.common.blessMaxNum = sheetBasic1.getShortValue(row++, col );
		gcfgs.common.blessCnt = sheetBasic1.getShortValue(row++, col );
		gcfgs.common.blessTime1 = StringConverter.parseMinuteOfDay(sheetBasic1.getStringValue(row++, col))*60;
		gcfgs.common.blessTime2 = StringConverter.parseMinuteOfDay(sheetBasic1.getStringValue(row++, col))*60;
		
		row++;
	}
	
	public void loadDiskBet(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.diskBet = new SBean.DiskBetCFGS();

		gcfgs.diskBet.cost = new ArrayList<SBean.DiskBetCostCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.DiskBetCostCFGS basic = new SBean.DiskBetCostCFGS();
			basic.sid = sheetBasic.getShortValue(row, col ++);
			basic.cost = new ArrayList<Short>();
			for(int i =0 ;i<4;i++){
				basic.cost.add(sheetBasic.getShortValue(row, col ++));
			}
			gcfgs.diskBet.cost.add(basic);
			++row;
			col = 0;
		}
	}
	
		
	
	
	public void loadHeroes(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.heroesBoss = new SBean.HeroesBossCFGS();
		
		gcfgs.heroesBoss.heroesBossType = new ArrayList<SBean.HeroesBossTypeCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.HeroesBossTypeCFGS basic1 = new SBean.HeroesBossTypeCFGS();
			
			basic1.sid =  sheetBasic.getShortValue(row, col ++);
			basic1.type =  sheetBasic.getShortValue(row, col ++);
	
		    basic1.levelmin = sheetBasic.getShortValue(row, col ++);
		    basic1.levelmax = sheetBasic.getShortValue(row, col ++);
			gcfgs.heroesBoss.heroesBossType.add(basic1);			
			row++;
			col = 0;		
		}
		
		ExcelSheet sheetItem = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.heroesBoss.heroesBossItems = new ArrayList<SBean.HeroesBossItemsCFGS>();
		row = 1;
		col = 0;
		while (sheetItem.isNotEmpty(row, col))
		{
			SBean.HeroesBossItemsCFGS basic1 = new SBean.HeroesBossItemsCFGS();
			
			basic1.rank = sheetItem.getShortValue(row, col ++);
			basic1.eItems = new ArrayList<DropEntryNew>();
			for(int i =0 ;i<3;i++){
				SBean.DropEntryNew basic = new SBean.DropEntryNew();
				basic.type = sheetItem.getByteValue(row, col ++);
				if(basic.type==-1){
					continue;
				}
				basic.id = sheetItem.getShortValue(row, col ++);			
				basic.arg= sheetItem.getShortValue(row, col ++);
				basic1.eItems.add(basic);				
			}
			gcfgs.heroesBoss.heroesBossItems.add(basic1);			
			row++;
			col = 0;		
		}
		
		ExcelSheet sheetScore = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
		gcfgs.heroesBoss.heroesBossScore = new ArrayList<SBean.HeroesBossScoreCFGS>();
		row = 1;
		col = 0;
		while (sheetScore.isNotEmpty(row, col))
		{
			SBean.HeroesBossScoreCFGS basic1 = new SBean.HeroesBossScoreCFGS();
			
			basic1.sid =  sheetScore.getShortValue(row, col ++);
			basic1.type =  sheetScore.getShortValue(row, col ++);
			basic1.rank = sheetScore.getShortValue(row, col ++);
			basic1.score = sheetScore.getShortValue(row, col ++);
			gcfgs.heroesBoss.heroesBossScore.add(basic1);			
			row++;
			col = 0;		
		}
		
		ExcelSheet sheetBuff = ket.moi.Factory.newExcelReader(fileName).getSheet(4);
		gcfgs.heroesBoss.heroesBossBuff = new ArrayList<SBean.HeroesBossBuffCFGS>();
		row = 1;
		col = 0;
		while (sheetBuff.isNotEmpty(row, col))
		{
			SBean.HeroesBossBuffCFGS basic1 = new SBean.HeroesBossBuffCFGS();
			
			basic1.sid =  sheetBuff.getShortValue(row, col ++);
			col ++;
			basic1.cost = sheetBuff.getShortValue(row, col ++);
			gcfgs.heroesBoss.heroesBossBuff.add(basic1);			
			row++;
			col = 0;		
		}
		
		ExcelSheet sheetBasic3 = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		row = 1;
		col = 1;
		 
		String gisd = sheetBasic3.getStringValue(row++, col);
		String gi [] = gisd.split(",");
		gcfgs.heroesBoss.gids = new ArrayList<Short>();
		if(gi!=null){
			for( String g :gi){
				gcfgs.heroesBoss.gids.add(Short.parseShort(g));
			}
		}
		
		
		gcfgs.heroesBoss.battlecount = sheetBasic3.getShortValue(row++, col);		
		gcfgs.heroesBoss.gcount = sheetBasic3.getShortValue(row++, col);
	}
	
	public void loadTreasureMap(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.treasureMap = new SBean.TreasureMapBaseCFGS();
		
		gcfgs.treasureMap.maxCount = sheetBasic.getShortValue(2, 2);
		gcfgs.treasureMap.refreshTime = sheetBasic.getByteValue(3, 2);
		gcfgs.treasureMap.refreshTimeAddCount = sheetBasic.getByteValue(4, 2);
		gcfgs.treasureMap.addCountTime = sheetBasic.getByteValue(5, 2);
		gcfgs.treasureMap.addCount = sheetBasic.getByteValue(6, 2);
		gcfgs.treasureMap.dayUseTreasureMapCount = sheetBasic.getByteValue(7, 2);
		gcfgs.treasureMap.mapDigMaxCount = sheetBasic.getByteValue(8, 2);
		gcfgs.treasureMap.digOtherMaxCount = sheetBasic.getByteValue(9, 2);
		gcfgs.treasureMap.mailTitle = sheetBasic.getStringValue(10, 2);
		gcfgs.treasureMap.mailContextDigger = sheetBasic.getStringValue(11, 2);
		gcfgs.treasureMap.mailContextUser = sheetBasic.getStringValue(12, 2);
		gcfgs.treasureMap.treasureMaps = new ArrayList<SBean.TreasureMapCFGS>();
		
		final int rowTreasureMap = locateColTag(tname, sheetBasic, 0, "藏宝图");
		
		int row = rowTreasureMap + 2;
		int col = 0;
		
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.TreasureMapCFGS tm = new SBean.TreasureMapCFGS();
			tm.id = sheetBasic.getByteValue(row, col++);
			tm.name = sheetBasic.getStringValue(row, col++);
			tm.itemId = sheetBasic.getShortValue(row, col++);
			tm.commonRewards = sheetBasic.getShortValue(row, col++);
			tm.treasureRewards = sheetBasic.getShortValue(row, col++);
			tm.userRewards1Id = sheetBasic.getShortValue(row, col++);
			tm.userRewards1Cnt = sheetBasic.getByteValue(row, col++);
			tm.userRewards2Id = sheetBasic.getShortValue(row, col++);
			tm.userRewards2Cnt = sheetBasic.getByteValue(row, col++);
			tm.userRewards3Id = sheetBasic.getShortValue(row, col++);
            tm.userRewards3Cnt = sheetBasic.getByteValue(row, col++);
			tm.rewardsMaxCnt = sheetBasic.getByteValue(row, col++);
			tm.digCost = sheetBasic.getShortValue(row, col++);
			
			gcfgs.treasureMap.treasureMaps.add(tm);
			
			row++;
			col = 0;
		}
	}
	
	public void loadBeMonster(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
    {
	    ExcelSheet sheet0 = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
	    gcfgs.beMonster = new SBean.BeMonsterCFGS();
	    
	    final int rowbase = locateColTag(tname, sheet0, 0, "基本配置");
	    int row = rowbase + 1;
	    String[] registerTime = sheet0.getStringValue(row++, 2).split(";");
	    String[] registerEndTime = sheet0.getStringValue(row++, 2).split(";");
	    String[] fightStartTime = sheet0.getStringValue(row++, 2).split(";");
	    String[] fightEndTime = sheet0.getStringValue(row++, 2).split(";");
	    if (registerTime.length != registerEndTime.length || registerTime.length != fightStartTime.length || registerTime.length != fightEndTime.length) {
	        throw new Exception("我是大魔王 时间配置错误");
	    }
	    gcfgs.beMonster.RegisterTime = new ArrayList<Short>();
	    gcfgs.beMonster.RegisterEndTime = new ArrayList<Short>();
	    gcfgs.beMonster.fightStartTime = new ArrayList<Short>();
	    gcfgs.beMonster.fightEndTime = new ArrayList<Short>();
	    for (int i = 0; i < registerTime.length; i++) {
	        gcfgs.beMonster.RegisterTime.add(i, (short)StringConverter.parseMinuteOfDay(registerTime[i]));
	        gcfgs.beMonster.RegisterEndTime.add(i, (short)StringConverter.parseMinuteOfDay(registerEndTime[i]));
	        gcfgs.beMonster.fightStartTime.add(i, (short)StringConverter.parseMinuteOfDay(fightStartTime[i]));
	        gcfgs.beMonster.fightEndTime.add(i, (short)StringConverter.parseMinuteOfDay(fightEndTime[i]));
        }
	    
	    gcfgs.beMonster.RegisterCostMin = sheet0.getIntValue(row++, 2);
	    gcfgs.beMonster.RegisterCostMax = sheet0.getIntValue(row++, 2);
	    gcfgs.beMonster.RegisterPoundage = sheet0.getIntValue(row++, 2);
	    gcfgs.beMonster.attackMacCnt = sheet0.getByteValue(row++, 2);
	    
	    
	    ExcelSheet sheet1 = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
	    final int rowhp = locateColTag(tname, sheet1, 0, "血量提升配置");
	    row = rowhp+1;
	    gcfgs.beMonster.defaultUpHpPercent = sheet1.getByteValue(row++, 2);
	    row = row + 3;
	    int col = 0;
	    gcfgs.beMonster.upHpCfgs = new ArrayList<SBean.BeMonsterUpHpCFG>();
	    while (sheet1.isNotEmpty(row, col))
        {
	        SBean.BeMonsterUpHpCFG bmuh = new SBean.BeMonsterUpHpCFG();
	        bmuh.cnt = sheet1.getShortValue(row, col++);
	        bmuh.percent = sheet1.getIntValue(row, col++);
	        bmuh.cost = sheet1.getIntValue(row, col++);
	        gcfgs.beMonster.upHpCfgs.add(bmuh);
	        
	        row++;
	        col = 0;
        }
	    
	    
	    ExcelSheet sheet2 = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
	    final int rowbossreward = locateColTag(tname, sheet2, 0, "boss奖励配置");
	    gcfgs.beMonster.monsterAliveRewards = new ArrayList<SBean.DropEntryNew>();
	    
	    row = rowbossreward + 1;
	    String[] aliveRewards = sheet2.getStringValue(row, 2).split(";");
	    for (String string : aliveRewards) {
            SBean.DropEntryNew entry = new SBean.DropEntryNew();
            entry.type = Byte.parseByte(string.split(",")[0]);
            entry.id = Short.parseShort(string.split(",")[1]);
            entry.arg = Integer.parseInt(string.split(",")[2]);
            gcfgs.beMonster.monsterAliveRewards.add(entry);
        }
	    row = row + 3;
	    col = 0;
	    gcfgs.beMonster.monsterKillRoleRewrads = new ArrayList<SBean.BeMonsterReward>();
	    while (sheet2.isNotEmpty(row, col))
        {
	        SBean.BeMonsterReward br = new SBean.BeMonsterReward();
	        br.numMin = sheet2.getIntValue(row, col++);
	        br.numMax = sheet2.getIntValue(row, col++);
	        br.rewrad = new ArrayList<SBean.DropEntryNew>();
	        String[] re = sheet2.getStringValue(row, col++).split(";");
	        for (String string : re) {
	            SBean.DropEntryNew entry = new SBean.DropEntryNew();
	            entry.type = Byte.parseByte(string.split(",")[0]);
	            entry.id = Short.parseShort(string.split(",")[1]);
	            entry.arg = Integer.parseInt(string.split(",")[2]);
	            br.rewrad.add(entry);
	        }
	        gcfgs.beMonster.monsterKillRoleRewrads.add(br);
        }
	    
	    
	    ExcelSheet sheet3 = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
	    final int rowattackreward = locateColTag(tname, sheet3, 0, "攻击者奖励配置");
	    gcfgs.beMonster.perFightRewards = new ArrayList<SBean.DropEntryNew>();
	    
	    row = rowattackreward + 1;
	    String[] perRewards = sheet3.getStringValue(row, 2).split(";");
        for (String string : perRewards) {
            SBean.DropEntryNew entry = new SBean.DropEntryNew();
            entry.type = Byte.parseByte(string.split(",")[0]);
            entry.id = Short.parseShort(string.split(",")[1]);
            entry.arg = Integer.parseInt(string.split(",")[2]);
            gcfgs.beMonster.perFightRewards.add(entry);
        }
        row++;
        gcfgs.beMonster.killedMonsterRewards = new ArrayList<SBean.DropEntryNew>();
        String[] killRewards = sheet3.getStringValue(row, 2).split(";");
        for (String string : killRewards) {
            SBean.DropEntryNew entry = new SBean.DropEntryNew();
            entry.type = Byte.parseByte(string.split(",")[0]);
            entry.id = Short.parseShort(string.split(",")[1]);
            entry.arg = Integer.parseInt(string.split(",")[2]);
            gcfgs.beMonster.killedMonsterRewards.add(entry);
        }
        
        row = row + 3;
        col = 0;
        gcfgs.beMonster.rankRewards = new ArrayList<SBean.BeMonsterReward>();
        while (sheet3.isNotEmpty(row, col))
        {
            SBean.BeMonsterReward br = new SBean.BeMonsterReward();
            br.numMin = sheet3.getIntValue(row, col++);
            br.numMax = sheet3.getIntValue(row, col++);
            br.rewrad = new ArrayList<SBean.DropEntryNew>();
            String[] re = sheet3.getStringValue(row, col++).split(";");
            for (String string : re) {
                SBean.DropEntryNew entry = new SBean.DropEntryNew();
                entry.type = Byte.parseByte(string.split(",")[0]);
                entry.id = Short.parseShort(string.split(",")[1]);
                entry.arg = Integer.parseInt(string.split(",")[2]);
                br.rewrad.add(entry);
            }
            gcfgs.beMonster.rankRewards.add(br);
        }
        
    }
	
	public void loadExpiratBoss(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
		gcfgs.expiratBoss = new SBean.ExpiratBossCFGS();
		
		gcfgs.expiratBoss.expiratBossBasic = new ArrayList<SBean.ExpiratBossBasicCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.ExpiratBossBasicCFGS basic1 = new SBean.ExpiratBossBasicCFGS();
			
			basic1.sid =  sheetBasic.getShortValue(row, col ++);
			col ++;
			col ++;
			basic1.tid =  sheetBasic.getShortValue(row, col ++);
			 col +=4;		
//			basic1.cmax = sheetBasic.getShortValue(row, col ++);
//			col ++;
			basic1.dropId = new ArrayList <Integer> ();
			String  servers= sheetBasic.getStringValue(row, col ++);
			
			if(servers.contains(",")){
				
				String [] stemp =servers.split(",");
				
				for(String s:stemp){
					if(s==null||s.length()<=0){
						continue;
					}
					
					int sid=(int)Float.parseFloat(s);
					basic1.dropId.add(sid);
				}
			}else{
				int sid=(int)Float.parseFloat(servers);
				basic1.dropId.add(sid);
			}
			
			gcfgs.expiratBoss.expiratBossBasic.add(basic1);			
			row++;
			col = 0;		
		}
		
		ExcelSheet sheetItem = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		gcfgs.expiratBoss.expiratBossItems = new ArrayList<SBean.ExpiratBossItemsCFGS>();
		row = 1;
		col = 0;
		while (sheetItem.isNotEmpty(row, col))
		{
			SBean.ExpiratBossItemsCFGS basic1 = new SBean.ExpiratBossItemsCFGS();
			
			basic1.sid = sheetItem.getShortValue(row, col ++);
			basic1.type = sheetItem.getShortValue(row, col ++);
			basic1.rankmin = sheetItem.getShortValue(row, col ++);
			basic1.rankmax = sheetItem.getShortValue(row, col ++);
			basic1.eItems = new ArrayList<DropEntryNew>();
			for(int i =0 ;i<5;i++){
				SBean.DropEntryNew basic = new SBean.DropEntryNew();
				basic.type = sheetItem.getByteValue(row, col ++);
				if(basic.type==-1){
					continue;
				}
				basic.id = sheetItem.getShortValue(row, col ++);			
				basic.arg= sheetItem.getShortValue(row, col ++);
				basic1.eItems.add(basic);				
			}
			gcfgs.expiratBoss.expiratBossItems.add(basic1);			
			row++;
			col = 0;		
		}
		
		
		ExcelSheet sheetBasic3 = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		row = 1;
		col = 1;
		gcfgs.expiratBoss.count = sheetBasic3.getShortValue(row++, col);
		gcfgs.expiratBoss.cost1 = sheetBasic3.getShortValue(row++, col);
		gcfgs.expiratBoss.cost2 = sheetBasic3.getShortValue(row++, col);
	}
	
	public void loadExpiration(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetServer = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.changeServer = new SBean.ChangeServerCFGS();
		gcfgs.changeServer.serverList = new ArrayList<SBean.ChangeServerListCFGS>();
		int row = 1;
		int col = 0;
		while (sheetServer.isNotEmpty(row, col))
		{
			SBean.ChangeServerListCFGS basic = new SBean.ChangeServerListCFGS();
			
			String servers = sheetServer.getStringValue(row, col ++);
			int toid = sheetServer.getIntValue(row, col++);
			col++;
			String days = sheetServer.getStringValue(row, col++);
			if(servers==null||servers.length()<=0){
				continue;
			}
			if(days==null||days.length()<=0){
				continue;
			}
			
			String [] dtemp = null;
			if(days.contains(",")){
				dtemp =days.split(",");
			}
			if(servers.contains(",")){
				if(dtemp == null){
					continue;
				}
				String [] stemp =servers.split(",");
				if(stemp.length!=dtemp.length){
					continue;
				}
				int index =0;
				for(String s:stemp){
					if(s==null||s.length()<=0){
						continue;
					}
					SBean.ChangeServerListCFGS basic1 = new SBean.ChangeServerListCFGS();
					basic1.sid=(int)Float.parseFloat(s);
					basic1.toid=toid;
					
					basic1.days=(short)Float.parseFloat(dtemp[index]);
					
					gcfgs.changeServer.serverList.add(basic1);
					index++;
				}
			}else{
				basic.sid=(int)Float.parseFloat(servers);
				basic.toid=toid;
				basic.days=(short)Float.parseFloat(days);
				
				gcfgs.changeServer.serverList.add(basic);
			}
			
			row++;
			col = 0;
		}
		
		
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		row = 0;
		col = 1;
		gcfgs.expatriateCost = sheetBasic.getShortValue(row++, col);
		gcfgs.changeServer.cLevel = sheetBasic.getShortValue(row++, col);
		
		
		ExcelSheet sheetItem = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.changeServer.changServerItems = new ArrayList<SBean.ChangServerItemsCFGS>();
		row = 1;
		col = 0;
		while (sheetItem.isNotEmpty(row, col))
		{
			SBean.ChangServerItemsCFGS basic = new SBean.ChangServerItemsCFGS();
			
			basic.sid = sheetItem.getShortValue(row, col ++);
			col++;
			basic.itemType = sheetItem.getByteValue(row, col ++);
			basic.iid = sheetItem.getShortValue(row, col ++);			
			basic.icount= sheetItem.getShortValue(row, col ++);				
			gcfgs.changeServer.changServerItems.add(basic);
			
			row++;
			col = 0;		
		}
	}
	
	public void loadGeneralStone(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		gcfgs.generalStone = new SBean.GeneralStoneCFGS();

		gcfgs.generalStone.basic = new ArrayList<SBean.GeneralStoneBasicCFGS>();
		int row = 1;
		int col = 0;
		Set<Short> ids = new HashSet<Short>();
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.GeneralStoneBasicCFGS basic = new SBean.GeneralStoneBasicCFGS();
			basic.sid = sheetBasic.getShortValue(row, col ++);
			basic.name = sheetBasic.getStringValue(row, col ++);
			basic.stype = sheetBasic.getByteValue(row, col ++);
			basic.maxSkillCount = sheetBasic.getShortValue(row, col ++);
			basic.skillAdd = sheetBasic.getByteValue(row, col ++);
			basic.exp = sheetBasic.getIntValue(row, col ++);
			basic.costExp = sheetBasic.getIntValue(row, col ++);
			basic.upCost = sheetBasic.getIntValue(row, col ++);
			basic.upIid = sheetBasic.getShortValue(row, col ++);
			basic.specialgid = sheetBasic.getShortValue(row, col ++);
			basic.group = sheetBasic.getShortValue(row, col ++);
			gcfgs.generalStone.basic.add(basic);
			++row;
			col = 0;
		}
				
		ExcelSheet sheetpos = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.generalStone.pos = new ArrayList<SBean.GeneralStonePosCFGS>();
		row = 1;
		col = 0;
		while (sheetpos.isNotEmpty(row, col))
		{
			SBean.GeneralStonePosCFGS pos = new SBean.GeneralStonePosCFGS();
			pos.gid = sheetpos.getShortValue(row, col ++);
//			ids.add(pos.gid);
			col ++;
			pos.state1 = sheetpos.getByteValue(row, col ++);
			pos.state2 = sheetpos.getByteValue(row, col ++);
			pos.state3 = sheetpos.getByteValue(row, col ++);
			pos.state4 = sheetpos.getByteValue(row, col ++);
			String skill1 = sheetpos.getStringValue(row, col ++);
			String skill2 = sheetpos.getStringValue(row, col ++);
			String skill3 = sheetpos.getStringValue(row, col ++);
			String skill4 = sheetpos.getStringValue(row, col ++);
			pos.posType1 = sheetpos.getByteValue(row, col ++);
			pos.posType2 = sheetpos.getByteValue(row, col ++);
			pos.posType3 = sheetpos.getByteValue(row, col ++);
			pos.posType4 = sheetpos.getByteValue(row, col ++);
			
			if( pos.state1 > 0 &&skill1!=null&&skill1.contains(";"))
			{
				String[] skillList = skill1.split(";");
				pos.posSkill1 = new SBean.GeneralStoneSkillPropCFGS(
					Short.parseShort(skillList[0]),Short.parseShort(skillList[1]));
			}
			else
			{
				pos.posSkill1 = new SBean.GeneralStoneSkillPropCFGS((short)-1, (short)-1);
			}
			if( pos.state2 > 0 &&skill2!=null&&skill2.contains(";"))
			{
				String[] skillList = skill2.split(";");
				pos.posSkill2 = new SBean.GeneralStoneSkillPropCFGS(
					Short.parseShort(skillList[0]),Short.parseShort(skillList[1]));
			}
			else
			{
				pos.posSkill2 = new SBean.GeneralStoneSkillPropCFGS((short)-1, (short)-1);
			}
			if( pos.state3 > 0 &&skill3!=null&&skill3.contains(";"))
			{
				String[] skillList = skill3.split(";");
				pos.posSkill3 = new SBean.GeneralStoneSkillPropCFGS(
					Short.parseShort(skillList[0]),Short.parseShort(skillList[1]));
			}
			else
			{
				pos.posSkill3 = new SBean.GeneralStoneSkillPropCFGS((short)-1, (short)-1);
			}
			if( pos.state4 > 0 &&skill4!=null&&skill4.contains(";"))
			{
				String[] skillList = skill4.split(";");
				pos.posSkill4 = new SBean.GeneralStoneSkillPropCFGS(
					Short.parseShort(skillList[0]),Short.parseShort(skillList[1]));
			}
			else
			{
				pos.posSkill4 = new SBean.GeneralStoneSkillPropCFGS((short)-1, (short)-1);
			}
			
			
			gcfgs.generalStone.pos.add(pos);
			++row;
			col = 0;
		}
		
//		for (SBean.GeneralCFGS g : gcfgs.generals)
//		if (g.id < 1000 && !ids.contains(g.id))
		// throw new Exception(tname + "存在没有配置神兵的武将，id=" + g.id);
		
		ExcelSheet sheetEnhanceCost = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.generalStone.evoCosts = new ArrayList<SBean.GeneralStoneEvoCostCFGS>();
		row = 1;
		col = 0;
		while (sheetEnhanceCost.isNotEmpty(row, col))
		{
			SBean.GeneralStoneEvoCostCFGS cost = new SBean.GeneralStoneEvoCostCFGS();
			
			cost.cid = sheetEnhanceCost.getShortValue(row, col++);
			cost.totalLock = sheetEnhanceCost.getByteValue(row, col ++);
			cost.costStoneLock1 = sheetEnhanceCost.getIntValue(row, col ++);
			cost.costMoneyLock1 = sheetEnhanceCost.getIntValue(row, col ++);
			cost.costStoneLock2 = sheetEnhanceCost.getIntValue(row, col ++);
			cost.costMoneyLock2 = sheetEnhanceCost.getIntValue(row, col ++);
			
			gcfgs.generalStone.evoCosts.add(cost);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetEnhanceProp = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
		gcfgs.generalStone.sprop = new ArrayList<SBean.GeneralStonePropCFGS>();
		row = 1;
		col = 0;
		while (sheetEnhanceProp.isNotEmpty(row, col))
		{
			SBean.GeneralStonePropCFGS prop = new SBean.GeneralStonePropCFGS();
			prop.pId = sheetEnhanceProp.getShortValue(row, col++);
			prop.propId = sheetEnhanceProp.getShortValue(row, col++);
			prop.value = sheetEnhanceProp.getShortValue(row, col++);
			prop.type = sheetEnhanceProp.getShortValue(row, col++);
			gcfgs.generalStone.sprop.add(prop);						
			++row;
			col = 0;
		}
		
		ExcelSheet sheetPropValue = ket.moi.Factory.newExcelReader(fileName).getSheet(4);
		gcfgs.generalStone.spropValue = new ArrayList<SBean.GeneralStonePropValueCFGS>();
		row = 1;
		col = 0;
		while (sheetPropValue.isNotEmpty(row, col))
		{
			SBean.GeneralStonePropValueCFGS propValue = new SBean.GeneralStonePropValueCFGS();
			propValue.pId = sheetPropValue.getShortValue(row, col++);
			propValue.minvalue = sheetPropValue.getFloatValue(row, col++);
			propValue.maxvalue = sheetPropValue.getFloatValue(row, col++);
			propValue.weight = sheetPropValue.getShortValue(row, col++);
			gcfgs.generalStone.spropValue.add(propValue);						
			++row;
			col = 0;
		}
		
		
		String weaponCfg = "{ \n basic={";
		for (GeneralStoneBasicCFGS b : gcfgs.generalStone.basic)
		{
			weaponCfg += "[" + b.sid + "]={sid=" + b.sid;
			weaponCfg += ", name=\"" + b.name;
			weaponCfg += "\", stype=" + b.stype;
			weaponCfg += ", skillAdd=" + b.skillAdd;
			weaponCfg += ", exp=" + b.exp;
			weaponCfg += ", costExp=" + b.costExp;
			weaponCfg += ", upCost=" + b.upCost;
			weaponCfg += ", upIid=" + b.upIid;
			weaponCfg += ", specialgid=" + b.specialgid;
			weaponCfg += "},\n";
		}
		weaponCfg += "},\n stonePosInfo={";
		for (GeneralStonePosCFGS pos : gcfgs.generalStone.pos)
		{
			weaponCfg += "[" + pos.gid + "]={gid=" + pos.gid;
			weaponCfg += ", state1=" + pos.state1;
			weaponCfg += ", state2=" + pos.state2;
			weaponCfg += ", state3=" + pos.state3;
			weaponCfg += ", state4=" + pos.state4;
			weaponCfg += ", posType1=" + pos.posType1;
			weaponCfg += ", posType2=" + pos.posType2;
			weaponCfg += ", posType3=" + pos.posType3;
			weaponCfg += ", posType4=" + pos.posType4;
			weaponCfg += ", posSid1be=" + pos.posSkill1.beskillId;
			weaponCfg += ", posSid1af=" + pos.posSkill1.afskillId;
			weaponCfg += ", posSid2be=" + pos.posSkill2.beskillId;
			weaponCfg += ", posSid2af=" + pos.posSkill2.afskillId;
			weaponCfg += ", posSid3be=" + pos.posSkill3.beskillId;
			weaponCfg += ", posSid3af=" + pos.posSkill3.afskillId;
			weaponCfg += ", posSid4be=" + pos.posSkill4.beskillId;
			weaponCfg += ", posSid4af=" + pos.posSkill4.afskillId;
			weaponCfg += "},\n ";
		}
		weaponCfg += "},\n lockCost={";
		for (GeneralStoneEvoCostCFGS cost : gcfgs.generalStone.evoCosts)
		{
			weaponCfg += "[" + cost.totalLock + "]={totalLock=" + cost.totalLock;
			weaponCfg += ", costMoneyLock1=" + cost.costMoneyLock1;
			weaponCfg += ", costStoneLock1=" + cost.costStoneLock1;
			weaponCfg += ", costMoneyLock2=" + cost.costMoneyLock2;
			weaponCfg += ", costStoneLock2=" + cost.costStoneLock2;
			weaponCfg += "},\n";
		}
		weaponCfg += "}}";
		
//		luaCfg.put("generalStone", weaponCfg);
	}
	
	public void loadGeneralSeyen(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		
		ExcelSheet sheetBasic = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.generalSeyen = new SBean.GeneralSeyenCFGS();

		gcfgs.generalSeyen.expAdd = new ArrayList<SBean.GeneralSeyenExpAddCFGS>();
		int row = 1;
		int col = 0;
		while (sheetBasic.isNotEmpty(row, col))
		{
			SBean.GeneralSeyenExpAddCFGS basic = new SBean.GeneralSeyenExpAddCFGS();
			basic.sid = sheetBasic.getShortValue(row, col ++);
			basic.weight = sheetBasic.getShortValue(row, col ++);
			basic.exp = sheetBasic.getIntValue(row, col ++);
			gcfgs.generalSeyen.expAdd.add(basic);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetExp = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
	
		gcfgs.generalSeyen.sexp = new ArrayList<SBean.GeneralSeyenExpCFGS>();
		row = 1;
		col = 0;
		while (sheetExp.isNotEmpty(row, col))
		{
			SBean.GeneralSeyenExpCFGS basic = new SBean.GeneralSeyenExpCFGS();
			basic.level = sheetExp.getIntValue(row, col ++);
			basic.exp = sheetExp.getIntValue(row, col ++);
			basic.limitLvl = sheetExp.getIntValue(row, col ++);
			gcfgs.generalSeyen.sexp.add(basic);
			++row;
			col = 0;
		}
		
		ExcelSheet sheetCommon = ket.moi.Factory.newExcelReader(fileName).getSheet(4);
		gcfgs.generalSeyen.commonValue = new SBean.GeneralSeyenCommonCFGS();
		row = 0;
		col = 1;
		gcfgs.generalSeyen.commonValue.expRate = sheetCommon.getFloatValue(row++, col);
		gcfgs.generalSeyen.commonValue.reversCost = sheetCommon.getIntValue(row++, col);
		gcfgs.generalSeyen.commonValue.sItemId = sheetCommon.getShortValue(row++, col);
		gcfgs.generalSeyen.commonValue.reversRate = sheetCommon.getFloatValue(row++, col);
	}
	
	
	public void loadDuel(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.duel = new SBean.DuelCFGS();
		
		gcfgs.duel.specialTimes = new ArrayList<SBean.DuelTimeCFGS>();
		String str = sheet.getStringValue(2, 1);
		String[] strs = str.split(";");
		for (String s : strs) {
			String[] ss = s.split(",");
			short begin = (short)StringConverter.parseMinuteOfDay(ss[0]);
			short end = (short)StringConverter.parseMinuteOfDay(ss[1]);
			gcfgs.duel.specialTimes.add(new SBean.DuelTimeCFGS(begin, end));
		}
		gcfgs.duel.playTimes = sheet.getByteValue(4, 1);
		gcfgs.duel.rewardTimes = sheet.getByteValue(4, 3);
		gcfgs.duel.specialLegendRewardPoints = sheet.getIntValue(2, 3);
		gcfgs.duel.specialRewardPoints = sheet.getIntValue(2, 5);
		gcfgs.duel.rewardPoints = sheet.getIntValue(4, 5);

		gcfgs.duel.upgrade = new ArrayList<Short>();
		final int rowUpgrade = locateColTag(tname, sheet, 0, "决斗场等级");
		int row = rowUpgrade + 2;
		int col = 2;
		short total = 0;
		while (sheet.isNotEmpty(row, col))
		{
			short stars = sheet.getShortValue(row, col);
			total += stars;
			gcfgs.duel.upgrade.add(total);
			++row;
		}
				
		gcfgs.duel.winStars = new ArrayList<SBean.DuelStarsCFGS>();
		final int rowWinStars = locateColTag(tname, sheet, 0, "非传说赢星");
		row = rowWinStars + 1;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DuelStarsCFGS stars = new SBean.DuelStarsCFGS();
			stars.lvlMin = sheet.getShortValue(row++, col);
			stars.win2Stars = sheet.getShortValue(row++, col);
			stars.win3Stars = sheet.getShortValue(row++, col);
			stars.win4Stars = sheet.getShortValue(row++, col);
			gcfgs.duel.winStars.add(stars);
			col ++;
			row = rowWinStars + 1;
		}
		
		gcfgs.duel.loseStars = new ArrayList<SBean.DuelStarsCFGS>();
		final int rowLoseStars = locateColTag(tname, sheet, 0, "非传说输星");
		row = rowLoseStars + 1;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DuelStarsCFGS stars = new SBean.DuelStarsCFGS();
			stars.lvlMin = sheet.getShortValue(row++, col);
			stars.win2Stars = sheet.getShortValue(row++, col);
			stars.win3Stars = sheet.getShortValue(row++, col);
			stars.win4Stars = sheet.getShortValue(row++, col);
			gcfgs.duel.loseStars.add(stars);
			col ++;
			row = rowLoseStars + 1;
		}
		
		gcfgs.duel.legendWinStars = new ArrayList<SBean.DuelStarsCFGS>();
		final int rowLWinStars = locateColTag(tname, sheet, 0, "传说赢星");
		row = rowLWinStars + 1;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DuelStarsCFGS stars = new SBean.DuelStarsCFGS();
			stars.lvlMin = sheet.getShortValue(row++, col);
			stars.win2Stars = sheet.getShortValue(row++, col);
			stars.win3Stars = sheet.getShortValue(row++, col);
			stars.win4Stars = sheet.getShortValue(row++, col);
			gcfgs.duel.legendWinStars.add(stars);
			col ++;
			row = rowLWinStars + 1;
		}
		
		gcfgs.duel.legendLoseStars = new ArrayList<SBean.DuelStarsCFGS>();
		final int rowLLoseStars = locateColTag(tname, sheet, 0, "传说输星");
		row = rowLLoseStars + 1;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DuelStarsCFGS stars = new SBean.DuelStarsCFGS();
			stars.lvlMin = sheet.getShortValue(row++, col);
			stars.win2Stars = sheet.getShortValue(row++, col);
			stars.win3Stars = sheet.getShortValue(row++, col);
			stars.win4Stars = sheet.getShortValue(row++, col);
			gcfgs.duel.legendLoseStars.add(stars);
			col ++;
			row = rowLLoseStars + 1;
		}
		
		gcfgs.duel.resetLevels = new ArrayList<Short>();
		final int rowReset = locateColTag(tname, sheet, 0, "重置等级");
		row = rowReset + 2;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			short level = sheet.getShortValue(row, col);
			gcfgs.duel.resetLevels.add(level);
			++row;
		}
				
		gcfgs.duel.rewards = new ArrayList<SBean.DuelRewardCFGS>();
		final int rowRewards = locateColTag(tname, sheet, 0, "奖励");
		row = rowRewards + 2;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.DuelRewardCFGS reward = new SBean.DuelRewardCFGS();
			reward.drops = new ArrayList<SBean.DropEntryNew>();
			str = sheet.getStringValue(row, col++);
			strs = str.split(";");
			for (String s : strs) {
				String[] ss = s.split(",");
				short id = (short)Float.parseFloat(ss[0]);
				int count = (int)Float.parseFloat(ss[1]);
				SBean.DropEntryNew drop = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, id, count);
				reward.drops.add(drop);
			}
			reward.point = sheet.getIntValue(row, col++);
			gcfgs.duel.rewards.add(reward);
			row ++;
			col = 2;
		}
		
		gcfgs.duel.buyTimeCosts = new ArrayList<Integer>();
		final int rowBuyTimes = locateColTag(tname, sheet, 0, "购买次数需要的钻石");
		row = rowBuyTimes + 2;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			int cost = sheet.getIntValue(row, col);
			gcfgs.duel.buyTimeCosts.add(cost);
			++col;
		}
		final int rowResetTime = locateColTag(tname, sheet, 0, "发奖时间");
		gcfgs.duel.resetTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowResetTime, 1));
	}
	
	public void loadSura(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.sura = new SBean.SuraCFGS();
		
		gcfgs.sura.reqLvl = sheet.getShortValue(1, 2);
		gcfgs.sura.fixedGids = new ArrayList<Short>();
		String str = sheet.getStringValue(1, 4);
		String[] strs = str.split(",");
		for (String s : strs)
			gcfgs.sura.fixedGids.add(Short.parseShort(s));

		gcfgs.sura.randomGroups = new ArrayList<SBean.SuraRandomGroupCFGS>();
		final int rowRandomGroup = locateColTag(tname, sheet, 0, "随机组");
		int row = rowRandomGroup + 1;
		int col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.SuraRandomGroupCFGS g = new SBean.SuraRandomGroupCFGS();
			g.count = sheet.getByteValue(row, col);
			g.gids = new ArrayList<Short>();
			str = sheet.getStringValue(row+1, col);
			strs = str.split(",");
			for (String s : strs)
				g.gids.add(Short.parseShort(s));
			if (g.count > g.gids.size())
				throw new Exception(tname + "随机数量大于id总数");
			gcfgs.sura.randomGroups.add(g);
			++col;
		}
				
		gcfgs.sura.miniStages = new ArrayList<SBean.SuraMiniStageCFGS>();
		final int rowMiniStage = locateColTag(tname, sheet, 0, "层");
		row = rowMiniStage + 2;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			int rankMin = sheet.getIntValue(row, col);
			int rankMax = sheet.getIntValue(row+1, col);
			gcfgs.sura.miniStages.add(new SBean.SuraMiniStageCFGS(rankMin, rankMax));
			++col;
		}
				
		gcfgs.sura.stages = new ArrayList<SBean.SuraStageCFGS>();
		final int rowStage = locateColTag(tname, sheet, 0, "关");
		row = rowStage + 2;
		col = 2;
		while (sheet.isNotEmpty(row, col))
		{
			SBean.SuraStageCFGS s = new SBean.SuraStageCFGS();
			s.money = sheet.getIntValue(row, col);
			s.dropId = sheet.getShortValue(row+1, col);
			gcfgs.sura.stages.add(s);
			++col;
		}
		
		if (gcfgs.sura.miniStages.size() != 15 || gcfgs.sura.stages.size() != 5)
			throw new Exception(tname + "关卡数量不正确");
	}
	
	public void loadSWar(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		gcfgs.swar = new SBean.ForceSWarCFGS();
		
		ExcelSheet sheet0 = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.swar.basic = new SBean.ForceSWarBasicCFGS();
		gcfgs.swar.basic.winScore = sheet0.getIntValue(0, 1);
		gcfgs.swar.basic.occupyScore = new ArrayList<Integer>();
		int col = 3;
		while (sheet0.isNotEmpty(0, col)) {
			int score = sheet0.getIntValue(0, col++);
			gcfgs.swar.basic.occupyScore.add(score);
		}
		gcfgs.swar.basic.badgePrice = sheet0.getIntValue(2, 1);
		gcfgs.swar.basic.globalProtect = 0; // sheet0.getIntValue(2, 3);
		
		ExcelSheet sheet1 = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		gcfgs.swar.exp = new ArrayList<SBean.ForceSWarExpCFGS>();
		int row = 1;
		while (sheet1.isNotEmpty(row, 1)) {
			SBean.ForceSWarExpCFGS exp = new SBean.ForceSWarExpCFGS();
			exp.attackWinExp = sheet1.getIntValue(row, 1);
			exp.attackLoseExp = sheet1.getIntValue(row, 2);
			exp.defendWinExp = sheet1.getIntValue(row, 3);
			exp.defendLoseExp = sheet1.getIntValue(row, 4);
			gcfgs.swar.exp.add(exp);
			row ++;
		}
		
		ExcelSheet sheet2 = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.swar.level = new ArrayList<SBean.ForceSWarLevelCFGS>();
		row = 1;
		while (sheet2.isNotEmpty(row, 1)) {
			SBean.ForceSWarLevelCFGS level = new SBean.ForceSWarLevelCFGS();
			level.expNeed = sheet2.getIntValue(row, 1);
			level.badgeNeed = sheet2.getIntValue(row, 2);
			level.winRate = sheet2.getFloatValue(row, 3);
			gcfgs.swar.level.add(level);
			row ++;
		}
		
		ExcelSheet sheet3 = ket.moi.Factory.newExcelReader(fileName).getSheet(3);
		gcfgs.swar.reward = new ArrayList<SBean.ForceSWarRewardCFGS>();
		row = 2;
		while (sheet3.isNotEmpty(row, 1)) {
			SBean.ForceSWarRewardCFGS reward = new SBean.ForceSWarRewardCFGS(new ArrayList<SBean.DropEntryNew>(), new ArrayList<SBean.DropEntryNew>());
			for (int i = 0; i < 5; i ++) {
				short itemId = sheet3.getShortValue(row, i*2 + 1);
				if (itemId > 0) {
					int count = sheet3.getShortValue(row, i*2 + 2);
					reward.winReward.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, itemId, count));
				}
			}
			for (int i = 0; i < 5; i ++) {
				short itemId = sheet3.getShortValue(row, i*2 + 11);
				if (itemId > 0) {
					int count = sheet3.getShortValue(row, i*2 + 12);
					reward.loseReward.add(new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, itemId, count));
				}
			}
			gcfgs.swar.reward.add(reward);
			row ++;
		}
		
		ExcelSheet sheet4 = ket.moi.Factory.newExcelReader(fileName).getSheet(4);
		gcfgs.swar.badgeItem = new ArrayList<SBean.ForceSWarBadgeItemCFGS>();
		row = 1;
		while (sheet4.isNotEmpty(row, 1)) {
			SBean.ForceSWarBadgeItemCFGS item = new SBean.ForceSWarBadgeItemCFGS();
			item.itemId = sheet4.getShortValue(row, 1);
			item.badgeCount = sheet4.getIntValue(row, 2);
			gcfgs.swar.badgeItem.add(item);
			row ++;
		}
		
		ExcelSheet sheet5 = ket.moi.Factory.newExcelReader(fileName).getSheet(5);
		gcfgs.swar.weakBuf = new ArrayList<SBean.ForceSWarWeakBufCFGS>();
		row = 1;
		while (sheet5.isNotEmpty(row, 1)) {
			int loseTimes = sheet5.getIntValue(row, 8);
			if (loseTimes < 0) {
				row ++;
				continue;
			}
			short bufId = sheet5.getShortValue(row, 0);
			SBean.ForceSWarWeakBufCFGS weakBuf = new SBean.ForceSWarWeakBufCFGS(bufId, loseTimes);
			gcfgs.swar.weakBuf.add(weakBuf);
			row ++;
		}
	}
	
	public void loadOfficial(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		gcfgs.official = new SBean.OfficialCFGS();
		
		ExcelSheet sheet0 = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.official.basic = new SBean.OfficialBasicCFGS();
		gcfgs.official.basic.officialResetRate = sheet0.getFloatValue(0, 1);
		gcfgs.official.basic.officialResetStone = sheet0.getIntValue(0, 3);
		gcfgs.official.basic.levelItemId = sheet0.getShortValue(0, 5);
		gcfgs.official.basic.skillResetRate = sheet0.getFloatValue(2, 1);
		gcfgs.official.basic.skillResetStone = sheet0.getIntValue(2, 3);
		gcfgs.official.basic.skillLevelItemId = sheet0.getShortValue(2, 5);
		gcfgs.official.basic.openLvl = sheet0.getShortValue(4, 1);
		
		ExcelSheet sheet1 = ket.moi.Factory.newExcelReader(fileName).getSheet(1);
		gcfgs.official.levels = new ArrayList<SBean.OfficialLevelCFGS>();
		int row = 1;
		while (sheet1.isNotEmpty(row, 0)) {
			SBean.OfficialLevelCFGS level = new SBean.OfficialLevelCFGS();
			level.name = sheet1.getStringValue(row, 0);
			level.itemCount = sheet1.getIntValue(row, 1);
			gcfgs.official.levels.add(level);
			row ++;
		}
		
		ExcelSheet sheet2 = ket.moi.Factory.newExcelReader(fileName).getSheet(2);
		gcfgs.official.skillLevels = new ArrayList<SBean.OfficialSkillLevelCFGS>();
		row = 1;
		while (sheet2.isNotEmpty(row, 0)) {
			SBean.OfficialSkillLevelCFGS level = new SBean.OfficialSkillLevelCFGS();
			level.skillLvl = sheet2.getShortValue(row, 0);
			level.itemCount = sheet2.getIntValue(row, 1);
			gcfgs.official.skillLevels.add(level);
			row ++;
		}
	}
	
	public void loadMainTable(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		
		final int rowRole = locateColTag(tname, sheet, 0, "君主");
		String iconstr = sheet.getStringValue(rowRole + 7, 12);
		String[] icons = iconstr.split(",");
		List<Integer> iconList = new ArrayList<Integer>();
		for (String icon : icons) {
			int v = Integer.parseInt(icon.trim());
			iconList.add(v);
		}
		String roleIconCfg = "{";
		for (Integer i : iconList) {
			roleIconCfg += i + ",";			
		}
		roleIconCfg += "}";
		luaCfg.put("roleicons", roleIconCfg);

		String emostr = sheet.getStringValue(rowRole + 9, 12);
		String[] emos = emostr.split(",");
		List<Integer> emoList = new ArrayList<Integer>();
		for (String emo : emos) {
			int v = Integer.parseInt(emo.trim());
			emoList.add(v);
		}
		String emodstr = sheet.getStringValue(rowRole + 10, 12);
		String[] emods = emodstr.split(",");
		List<String> emodList = new ArrayList<String>();
		for (String emod : emods) {
			emodList.add(emod.trim());
		}
		if (emoList.size() != emodList.size())
			throw new Exception(tname + "表情id和描述数量不一致");
		String emotionCfg = "{";
		int index = 0;
		for (Integer i : emoList) {
			emotionCfg += "{image=" + i + ",";			
			String desc = emodList.get(index);
			if (desc != null)
				emotionCfg += "desc=\"" + desc + "\",";
			emotionCfg += "},";
			index ++;
		}
		emotionCfg += "}";
		luaCfg.put("emotions", emotionCfg);
		
		final int rowEvent = locateColTag(tname, sheet, 0, "活动");
		
		gcfgs.event = new SBean.EventCFGS();
		gcfgs.event.stoneAllGeneralEggDropIDs = new ArrayList<SBean.EventEggLevelCFGS>();
		final int STONE_LEVEL = 19;
		for(int i = 0; i < STONE_LEVEL; ++i)
		{
			int s = sheet.getIntValue(rowEvent + 2, 2 + i);
			if( i == 0 )
				s = 0;
			short lid = sheet.getShortValue(rowEvent + 3, 2 + i);
			if( ! dropTableIDs.contains(lid) )
				throw new Exception("活动引用武将掉落表ID  " + lid + "不存在或类型不符");
			gcfgs.event.stoneAllGeneralEggDropIDs.add(new SBean.EventEggLevelCFGS(lid, s));
		}
		
		{
			final int row = locateColTag(tname, sheet, 0, "IPAD活动");
			
			gcfgs.ipad = new SBean.IPadCFGS();
			gcfgs.ipad.areas = new ArrayList<SBean.IPadAreaCFGS>();
			
			gcfgs.ipad.state = sheet.getIntValue(row + 1, 3);
			String d1 = sheet.getStringValue(row+2, 3);
			String d2 = sheet.getStringValue(row+3, 3);
			String m1 = sheet.getStringValue(row+2, 4);
			String m2 = sheet.getStringValue(row+3, 4);
			gcfgs.ipad.timeStart = StringConverter.parseDayOfYear(d1) + 8*3600 + StringConverter.parseMinuteOfDay(m1)*60;
			gcfgs.ipad.timeEnd = StringConverter.parseDayOfYear(d2) + 8*3600 + StringConverter.parseMinuteOfDay(m2)*60;
			gcfgs.ipad.itemIDRand = sheet.getShortValue(row + 4, 3);
			if( gcfgs.ipad.itemIDRand <= 0 || ! itemIDs.containsKey(gcfgs.ipad.itemIDRand)
				|| itemIDs.get(gcfgs.ipad.itemIDRand).type != GameData.ITEM_TYPE_GAMBLE
				|| itemIDs.get(gcfgs.ipad.itemIDRand).arg2 != 1
					)
			{
				throw new Exception("ipad活动引用道具ID  " + gcfgs.ipad.itemIDRand + "不存在或类型不符");
			}
			int c = 3;
			while( sheet.isNotEmpty(row + 5, c) )
			{
				int gsid = sheet.getIntValue(row + 5, c);
				short iid = sheet.getShortValue(row + 6, c);
				int cheatTime = sheet.getIntValue(row + 7, c);
				if( iid <= 0 || ! itemIDs.containsKey(iid) )
				{
					throw new Exception("ipad活动引用道具ID  " + gcfgs.ipad.itemIDRand + "不存在或类型不符");
				}
				gcfgs.ipad.areas.add(new SBean.IPadAreaCFGS(gsid, iid, cheatTime));
				++c;
			}
			gcfgs.ipad.prop = sheet.getFloatValue(row + 8, 3);
			System.out.println("ipad cfg, start=" + gcfgs.ipad.timeStart + ",end=" + gcfgs.ipad.timeEnd);
		}
		
		final int rowInput = locateColTag(tname, sheet, 0, "输入");
		gcfgs.userInput = new SBean.UserInputCFGS();
		gcfgs.userInput.roleName = sheet.getByteValue(rowInput + 1, 2);
		gcfgs.userInput.userName = sheet.getByteValue(rowInput + 2, 2);
		gcfgs.userInput.forceName = sheet.getByteValue(rowInput + 3, 2);
		gcfgs.userInput.forceAnnounce = sheet.getByteValue(rowInput + 4, 2);
		gcfgs.userInput.forceQQ = sheet.getByteValue(rowInput + 5, 2);
		gcfgs.userInput.worldChat = sheet.getByteValue(rowInput + 6, 2);
		gcfgs.userInput.mail = sheet.getByteValue(rowInput + 7, 2);
		gcfgs.userInput.gmAnnounce = sheet.getByteValue(rowInput + 8, 2);
		gcfgs.userInput.sysMail = sheet.getShortValue(rowInput + 9, 2); // TODO
		
		String inputCFG = "{";
		inputCFG = inputCFG + "roleName = " + gcfgs.userInput.roleName + ",";
		inputCFG = inputCFG + "userName = " + gcfgs.userInput.userName + ",";
		inputCFG = inputCFG + "forceName = " + gcfgs.userInput.forceName + ",";
		inputCFG = inputCFG + "forceAnnounce = " + gcfgs.userInput.forceAnnounce + ",";
		inputCFG = inputCFG + "forceQQ = " + gcfgs.userInput.forceQQ + ",";
		inputCFG = inputCFG + "worldChat = " + gcfgs.userInput.worldChat + ",";
		inputCFG = inputCFG + "mail = " + gcfgs.userInput.mail + ",";
		inputCFG = inputCFG + "}";
		luaCfg.put("inputSize", inputCFG);
		
		final int rowDailyTask2 = locateColTag(tname, sheet, 0, "每日活动");
		{
			List<SBean.DailyTask2CFGS> lsts = new ArrayList<SBean.DailyTask2CFGS>();
			List<SBean.DailyTask2CFGC> lstc = new ArrayList<SBean.DailyTask2CFGC>();
			gcfgs.dtask2 = lsts;
			int colType = 2;
			while( sheet.isNotEmpty(rowDailyTask2 + 1, colType))
			{
				byte id = sheet.getByteValue(rowDailyTask2 + 1, colType);
				switch( id )
				{
				case GameData.DAILYTASK2_TYPE_COMBAT_ANY:
				case GameData.DAILYTASK2_TYPE_COMBAT_HERO:
				case GameData.DAILYTASK2_TYPE_UPGRADE_GENERAL_SKILL:
				case GameData.DAILYTASK2_TYPE_EGG:
				case GameData.DAILYTASK2_TYPE_GILD:
				case GameData.DAILYTASK2_TYPE_ARENA:
				case GameData.DAILYTASK2_TYPE_MARCH:
				case GameData.DAILYTASK2_TYPE_COMBAT_EVENT12:
				case GameData.DAILYTASK2_TYPE_COMBAT_EVENT345:
				case GameData.DAILYTASK2_TYPE_MONTHLY_CARD:
				case GameData.DAILYTASK2_TYPE_FREE_VIT1:
				case GameData.DAILYTASK2_TYPE_FREE_VIT2:
				case GameData.DAILYTASK2_TYPE_FREE_VIT3:
				case GameData.DAILYTASK2_TYPE_BUY_MONEY:
				case GameData.DAILYTASK2_TYPE_MAIL_BAOZI:
				case GameData.DAILYTASK2_TYPE_SHARE_PIC:
					break;
				default:
						throw new Exception(tname + " 无效的每日活动类型 " + id);
				}
				SBean.DailyTask2CFGS cfgs = new SBean.DailyTask2CFGS();
				SBean.DailyTask2CFGC cfgc = new SBean.DailyTask2CFGC();
				cfgc.cfgs = cfgs;
				cfgs.id = id;
				cfgs.lvlReq = sheet.getShortValue(rowDailyTask2 + 2, colType);
				cfgc.name = sheet.getStringValue(rowDailyTask2 + 3, colType);
				cfgc.des = sheet.getStringValue(rowDailyTask2 + 4, colType);
				cfgc.iconID = sheet.getShortValue(rowDailyTask2 + 5, colType);
				cfgs.times = sheet.getByteValue(rowDailyTask2 + 6, colType);
				cfgs.reward1Type = sheet.getByteValue(rowDailyTask2 + 7, colType);
				cfgs.reward1ID = sheet.getShortValue(rowDailyTask2 + 8, colType);
				cfgs.reward1Arg1 = sheet.getIntValue(rowDailyTask2 + 9, colType);
				cfgs.reward1Arg2 = sheet.getIntValue(rowDailyTask2 + 10, colType);
				cfgs.reward2Type = sheet.getByteValue(rowDailyTask2 + 11, colType);
				cfgs.reward2ID = sheet.getShortValue(rowDailyTask2 + 12, colType);
				cfgs.reward2Arg1 = sheet.getIntValue(rowDailyTask2 + 13, colType);
				cfgs.reward2Arg2 = sheet.getIntValue(rowDailyTask2 + 14, colType);
				cfgs.activity = sheet.getIntValue(rowDailyTask2 + 15, colType);
				lsts.add(cfgs);
				lstc.add(cfgc);
				++colType;
			}
			
			String cfg = "{";
			for(SBean.DailyTask2CFGC e : lstc)
			{
				cfg += "[" + e.cfgs.id + "]={lvlReq=" + e.cfgs.lvlReq + ",name=\"" + e.name + "\",des=\"" + e.des + "\",iconID="
						+ e.iconID +",times=" + e.cfgs.times +  ",reward1Type=" + e.cfgs.reward1Type + ",reward1Arg1=" + e.cfgs.reward1Arg1
						+ ",reward1Arg2=" + e.cfgs.reward1Arg2+ ",reward2Type=" + e.cfgs.reward2Type + ",reward2Arg1=" + e.cfgs.reward2Arg1
						+ ",reward1ID=" + e.cfgs.reward1ID + ",reward2ID=" +e.cfgs.reward2ID
						+ ",reward2Arg2=" + e.cfgs.reward2Arg2 + ",activity=" + e.cfgs.activity + "},";
			}
			cfg += "}";
			luaCfg.put("dailytask2", cfg);
			
			gcfgs.dailyActivity = new ArrayList<SBean.DailyActivityRewardCFGS>();
			final int rowRewards = locateColTag(tname, sheet, 0, "每日活跃度奖励");
			int col = 2;
			while( sheet.isNotEmpty(rowRewards+1, col) )
			{
				int activity = sheet.getIntValue(rowRewards+1, col);
				String desc = sheet.getStringValue(rowRewards+2, col);
				short iconId = sheet.getShortValue(rowRewards+3, col);
				List<SBean.DropEntryNew> drops = new ArrayList<SBean.DropEntryNew>();
				int idx = rowRewards+4;
				for (int i = 0; i < 3; i ++) {
					SBean.DropEntryNew drop = new SBean.DropEntryNew();
					drop.type = sheet.getByteValue(idx ++, col);
					drop.id = sheet.getShortValue(idx ++, col);
					drop.arg = sheet.getIntValue(idx ++, col);
					drops.add(drop);
				}
				gcfgs.dailyActivity.add(new SBean.DailyActivityRewardCFGS(activity, desc, iconId, drops));
				++col;
			}
			
			cfg = "{";
			for(SBean.DailyActivityRewardCFGS e : gcfgs.dailyActivity)
			{
				cfg += "{activity=" + e.activity + ",desc=\"" + e.desc + "\",iconId=" + e.icon + "},";
			}
			cfg += "}";
			luaCfg.put("dailyActivity", cfg);
		}
		final int rowForce = locateColTag(tname, sheet, 0, "国家");
		gcfgs.force = new SBean.ForceCFGS();
		gcfgs.force.lvlCreate = sheet.getShortValue(rowForce + 1, 2);
		gcfgs.force.stoneCreate = sheet.getIntValue(rowForce + 1, 5);
		gcfgs.force.maxMembers = sheet.getShortValue(rowForce + 2, 2);
		
		gcfgs.force.levels = new ArrayList<SBean.ForceLevelCFGS>();
		final int MAX_POS = 5;
		int rowForceLevel = rowForce + 3;
		{
			for(int i = 0; i < GameData.MAX_FORCE_LEVEL; ++i)				
			{
				int cupNeed = sheet.getIntValue(rowForceLevel + 6, 2 + i);
				SBean.ForceLevelCFGS s = new SBean.ForceLevelCFGS(cupNeed, null);
				s.posCount = new ArrayList<Byte>();
				for(int j = 0; j < MAX_POS; ++j)
				{
					s.posCount.add(sheet.getByteValue(rowForceLevel + 1 + j, 2 + i));
				}
				gcfgs.force.levels.add(s);
			}
		}
		
		iconstr = sheet.getStringValue(rowForce + 1, 13);
		icons = iconstr.split(",");
		iconList = new ArrayList<Integer>();
		for (String icon : icons) {
			int v = Integer.parseInt(icon.trim());
			iconList.add(v);
		}
		String forceIconCfg = "{";
		for (Integer i : iconList) {
			forceIconCfg += i + ",";			
		}
		forceIconCfg += "}";
		
		gcfgs.force.mobai = new SBean.ForceMobaiCFGS();
		gcfgs.force.mobai.maxRewardMoney = sheet.getIntValue(rowForce+11, 2);
		gcfgs.force.mobai.freeVit = sheet.getShortValue(rowForce+12, 2);
		gcfgs.force.mobai.moneyVit = sheet.getShortValue(rowForce+12, 5);
		gcfgs.force.mobai.stoneVit = sheet.getShortValue(rowForce+12, 8);
		gcfgs.force.mobai.moneyCost = sheet.getIntValue(rowForce+13, 5);
		gcfgs.force.mobai.stoneCost = sheet.getIntValue(rowForce+13, 8);
		gcfgs.force.mobai.freeMoney = sheet.getShortValue(rowForce+14, 2);
		gcfgs.force.mobai.moneyMoney = sheet.getShortValue(rowForce+14, 5);
		gcfgs.force.mobai.stoneMoney = sheet.getShortValue(rowForce+14, 8);
		gcfgs.force.sendMailMaxCnt = sheet.getIntValue(rowForce+15, 2);
		String mobaiCfg = "{"
				+ "maxRewardMoney=" + gcfgs.force.mobai.maxRewardMoney
				+ ", freeVit=" + gcfgs.force.mobai.freeVit
				+ ", moneyVit=" + gcfgs.force.mobai.moneyVit
				+ ", stoneVit=" + gcfgs.force.mobai.stoneVit
				+ ", moneyCost=" + gcfgs.force.mobai.moneyCost
				+ ", stoneCost=" + gcfgs.force.mobai.stoneCost
				+ ", freeMoney=" + gcfgs.force.mobai.freeMoney
				+ ", moneyMoney=" + gcfgs.force.mobai.moneyMoney
				+ ", stoneMoney=" + gcfgs.force.mobai.stoneMoney
				+ "}";
		
		String levelcfg = "{ n = " + gcfgs.force.levels.size();
		for(SBean.ForceLevelCFGS e : gcfgs.force.levels)
		{
			levelcfg = levelcfg  + ",{expNeed=" + e.cupNeed + ",posCount={n=" + e.posCount.size() + ",";
			for(byte m : e.posCount)
				levelcfg = levelcfg + m + ",";
			levelcfg = levelcfg + "}}";
		}
		levelcfg = levelcfg + "}";
		
		gcfgs.force.dismissRewards = new ArrayList<SBean.ForceDismissRewardCFGS>();
		int rowForceDismiss = rowForce + 17;
		int col = 3;
		while( sheet.isNotEmpty(rowForceDismiss, col) )
		{
			short lvl = sheet.getShortValue(rowForceDismiss, col);
			short itemId = sheet.getShortValue(rowForceDismiss+1, col);
			int count = sheet.getIntValue(rowForceDismiss+2, col);
			gcfgs.force.dismissRewards.add(new SBean.ForceDismissRewardCFGS(lvl, itemId, count));
			++col;
		}
		
		gcfgs.force.activityRewards = new ArrayList<SBean.ForceActivityRewardCFGS>();
		final int rowRewards = locateColTag(tname, sheet, 0, "活跃度奖励");
		col = 2;
		while( sheet.isNotEmpty(rowRewards+1, col) )
		{
			String name = sheet.getStringValue(rowRewards+1, col);
			int forceActivity = sheet.getIntValue(rowRewards+2, col);
			int activity = sheet.getIntValue(rowRewards+3, col);
			int money1 = sheet.getIntValue(rowRewards+4, col);
			int money2 = sheet.getIntValue(rowRewards+5, col);
			String str = sheet.getStringValue(rowRewards+6, col);
			String tips = sheet.getStringValue(rowRewards+7, col);
			List<SBean.ForceActivityDropCFGS> drops = new ArrayList<SBean.ForceActivityDropCFGS>();
			String[] dstrs = str.split(";");
			for (String d : dstrs) {
				String[] dstrs2 = d.split(",");
				if (dstrs2.length >= 2) {
					byte maxLvl = Byte.parseByte(dstrs2[0].trim());
					short drop = Short.parseShort(dstrs2[1].trim());
					drops.add(new SBean.ForceActivityDropCFGS(maxLvl, drop));
				}
			}
			gcfgs.force.activityRewards.add(new SBean.ForceActivityRewardCFGS(name, tips, forceActivity, activity, money1, money2, drops));
			++col;
		}
		
		String rewardcfg = "{";
		for(SBean.ForceActivityRewardCFGS e : gcfgs.force.activityRewards)
		{
			rewardcfg += "{name='" + e.name + "',";
			rewardcfg += "tips='" + e.tips + "',";
			rewardcfg += "forceActivity=" + e.forceActivity + ",";
			rewardcfg += "activity=" + e.activity + "},";
		}
		rewardcfg += "}";
		
		String forcecfg = "{lvlCreate=" + gcfgs.force.lvlCreate
				+ ",stoneCreate=" + gcfgs.force.stoneCreate
				+ ",maxMembers=" + gcfgs.force.maxMembers
				+ ",levels=" + levelcfg
				+ ",icons=" + forceIconCfg
				+ ",mobai=" + mobaiCfg 
				+ ",activityRewards=" + rewardcfg
				+ ",sendMailMaxCnt=" + gcfgs.force.sendMailMaxCnt 
				+ "}";
		luaCfg.put("force", forcecfg);
		
		final int rowMusic = locateColTag(tname, sheet, 0, "声音");
		
		gcfgc.music = new SBean.MusicCFG();
		gcfgc.music.cityMusic = sheet.getStringValue(rowMusic + 1, 2);
		gcfgc.music.combatMusic = sheet.getStringValue(rowMusic + 2, 2);
		gcfgc.music.createRoleMusic = sheet.getStringValue(rowMusic + 3, 2);
		gcfgc.music.pvpMusic = sheet.getStringValue(rowMusic + 4, 2);
		
		final int rowMap = locateColTag(tname, sheet, 0, "地图");
		
		gcfgc.map = new SBean.MapCFG();
		gcfgc.map.cityMap = sheet.getStringValue(rowMap + 1, 2);
		gcfgc.map.createRoleMap = sheet.getStringValue(rowMap + 2, 2);
		gcfgc.map.createRoleScript = sheet.getStringValue(rowMap + 4, 2);
		gcfgc.map.createRoleMapSpeed = sheet.getFloatValue(rowMap + 2, 6);
		gcfgc.map.createRoleMapEnable = sheet.getByteValue(rowMap + 2, 9);
		gcfgc.map.pvpMaps = new ArrayList<SBean.PVPMapCFG>();
		SBean.PVPMapCFG pvpMapCFG = new SBean.PVPMapCFG();
		pvpMapCFG.map = sheet.getStringValue(rowMap + 3, 2);
		pvpMapCFG.pos = new ArrayList<SBean.Float3>();
		pvpMapCFG.pos.add(getFloat3(sheet.getStringValue(rowMap + 3, 4)));
		pvpMapCFG.pos.add(getFloat3(sheet.getStringValue(rowMap + 3, 5)));
		gcfgc.map.pvpMaps.add(pvpMapCFG);
		gcfgc.map.heroBoxName = sheet.getStringValue(rowMap + 5, 2);
		gcfgc.map.heroBoxName2 = sheet.getStringValue(rowMap + 5, 3);
		
		final int rowEngine = locateColTag(tname, sheet, 0, "引擎");
		
		gcfgc.engine = new SBean.EngineCFG();
		gcfgc.engine.horseHS = sheet.getStringValue(rowEngine + 1, 2);
		gcfgc.engine.horseCS = sheet.getStringValue(rowEngine + 2, 2);
		gcfgc.engine.horseTitleOffset = sheet.getFloatValue(rowEngine + 3, 2);
		gcfgc.engine.texBloodRed = sheet.getStringValue(rowEngine + 1, 7);
		gcfgc.engine.texBloodGreen = sheet.getStringValue(rowEngine + 2, 7);
		gcfgc.engine.texNumberEffectRed = sheet.getStringValue(rowEngine + 1, 12);
		gcfgc.engine.texNumberEffectGreen = sheet.getStringValue(rowEngine + 2, 12);
		gcfgc.engine.durNumberEffect = sheet.getShortValue(rowEngine + 3, 12);
		
		gcfgc.engine.loadingInitMaxCool = sheet.getShortValue(rowEngine + 8, 13);
		gcfgc.engine.dragMin = sheet.getShortValue(rowEngine + 11, 4);
		gcfgc.engine.dragSpeed = sheet.getFloatValue(rowEngine + 12, 4);
		
		gcfgc.engine.moveStopRange = sheet.getFloatValue(rowEngine + 14, 2);
		gcfgc.engine.rotateStopRange = sheet.getFloatValue(rowEngine + 15, 2);
		gcfgc.engine.attackGridSize = sheet.getFloatValue(rowEngine + 16, 2);
		gcfgc.engine.roundBoxScale = sheet.getFloatValue(rowEngine + 17, 2);
		
		gcfgc.engine.X2Speed = sheet.getFloatValue(rowEngine + 19, 2);
		gcfgc.engine.X3Speed = sheet.getFloatValue(rowEngine + 20, 2);
		
		gcfgc.engine.defaultFont = sheet.getStringValue(rowEngine + 4, 7);
		gcfgc.engine.defaultStandAction = sheet.getStringValue(rowEngine + 1, 17);
		gcfgc.engine.defaultRunAction = sheet.getStringValue(rowEngine + 2, 17);
		gcfgc.engine.defaultAttackAction = sheet.getStringValue(rowEngine + 3, 17);
		{
			gcfgc.engine.defaultHurtAction = new ArrayList<String>();
			col = 17;
			while( sheet.isNotEmpty(rowEngine + 8, col) )
			{
				gcfgc.engine.defaultHurtAction.add(sheet.getStringValue(rowEngine + 8, col));
				++col;
			}
		}
		{
			gcfgc.engine.defaultDeathAction = new ArrayList<String>();
			col = 17;
			while( sheet.isNotEmpty(rowEngine + 4, col) )
			{
				gcfgc.engine.defaultDeathAction.add(sheet.getStringValue(rowEngine + 4, col));
				++col;
			}
		}
		{
			gcfgc.engine.defaultDeathLoopAction = new ArrayList<String>();
			col = 17;
			while( sheet.isNotEmpty(rowEngine + 5, col) )
			{
				gcfgc.engine.defaultDeathLoopAction.add(sheet.getStringValue(rowEngine + 5, col));
				++col;
			}
		}
		
		gcfgc.engine.defaultWinAction = sheet.getStringValue(rowEngine + 6, 17);
		gcfgc.engine.defaultLoseAction = sheet.getStringValue(rowEngine + 7, 17);
		
		gcfgc.engine.gravity = sheet.getFloatValue(rowEngine + 6, 2);
		gcfgc.engine.useCollision = sheet.getByteValue(rowEngine + 9, 2);
		gcfgc.engine.useKPack = sheet.getByteValue(rowEngine + 6, 9);
		gcfgc.engine.cameraFollowHero = sheet.getByteValue(rowEngine + 7, 9);
		gcfgc.engine.showFPSOnHead = sheet.getByteValue(rowEngine + 8, 9);
		gcfgc.engine.showErrorOnHead = sheet.getByteValue(rowEngine + 9, 9);
		
		gcfgc.engine.horseBaseSpeed = sheet.getFloatValue(rowEngine + 9, 13);
		
		final int rowSkill = locateColTag(tname, sheet, 0, "君主");
		
		String roleCfg = "{";
		int row = rowSkill+1;
		while( sheet.isNotEmpty(row, 15) )
		{
			short iid1 = sheet.getShortValue(row, 17);
			if( ! iconIDs.contains(iid1) )
				throw new Exception("君主引用头像 " + iid1 + "不存在");
			short iid2 = sheet.getShortValue(row, 18);
			if( ! iconIDs.contains(iid2) )
				throw new Exception("君主引用头像 " + iid2 + "不存在");
			roleCfg += "{headIcon=" + iid1 + ",bodyIcon=" + iid2 + "},";
			++row;
		}
		roleCfg += "}";
		luaCfg.put("roleGender", roleCfg);
		
		final int rowCombat = locateColTag(tname, sheet, 0, "战役");
		gcfgc.combat = new SBean.CombatClientCFG();
		gcfgs.combat = new SBean.CombatServerCFG();
		
		gcfgs.combat.skipCombatItemID = sheet.getShortValue(rowCombat + 20, 2);
		if( ! itemIDs.containsKey(gcfgs.combat.skipCombatItemID) )
			throw new Exception("扫荡引用道具表ID  " + gcfgs.combat.skipCombatItemID + "不存在或类型不符");
		gcfgc.combat.skipCombatItemID = gcfgs.combat.skipCombatItemID;
		luaCfg.put("funcCombatCoe", sheet.getStringValue(rowCombat + 21, 2));
		
		gcfgc.combat.randomDamageRadius = sheet.getFloatValue(rowCombat + 2, 3);
		gcfgc.combat.showGeneralName = sheet.getByteValue(rowCombat + 5, 3);
		
		gcfgc.combat.deathDelTime = sheet.getByteValue(rowCombat + 8, 2);
		gcfgc.combat.generalRefreshInterval = sheet.getShortValue(rowCombat + 9, 2);
		gcfgc.combat.commonSkillCool = sheet.getShortValue(rowCombat + 12, 2);
		
		gcfgc.combat.dlgIDGeneralStart = sheet.getShortValue(rowCombat + 8, 19);
		gcfgc.combat.dlgIDAssistantGeneralStart = sheet.getShortValue(rowCombat + 9, 19);
		gcfgc.combat.dlgIDEnemyGeneralStart = sheet.getShortValue(rowCombat + 10, 19);
		gcfgc.combat.dlgIDDie = sheet.getShortValue(rowCombat + 11, 19);
		gcfgc.combat.dlgIDIdle = sheet.getShortValue(rowCombat + 12, 19);
		gcfgc.combat.dlgIDKill = sheet.getShortValue(rowCombat + 13, 19);
		gcfgc.combat.dlgIDCry = sheet.getShortValue(rowCombat + 14, 19);
		
		gcfgc.combat.dlgStartHeroProp = sheet.getFloatValue(rowCombat + 15, 15);
		gcfgc.combat.dlgStartAssistantProp = sheet.getFloatValue(rowCombat + 16, 15);
		gcfgc.combat.dlgStartEnemyProp = sheet.getFloatValue(rowCombat + 17, 15);
		gcfgc.combat.dlgShoutProp = sheet.getFloatValue(rowCombat + 18, 15);
		gcfgc.combat.dlgShoutUseProp = sheet.getFloatValue(rowCombat + 19, 15);
		gcfgc.combat.dlgDieProp = sheet.getFloatValue(rowCombat + 20, 15);
		gcfgc.combat.dlgIdleProp = sheet.getFloatValue(rowCombat + 21, 15);
		gcfgc.combat.dlgKillProp = sheet.getFloatValue(rowCombat + 22, 15);
		
		gcfgc.combat.aoeEffectRange = sheet.getFloatValue(rowCombat + 12, 15);
		
		gcfgc.combat.maxPersistantAttackTime = sheet.getShortValue(rowCombat + 15, 2);
		gcfgc.combat.escapeHP = sheet.getFloatValue(rowCombat + 16, 2);
		gcfgc.combat.continueFightHP = sheet.getFloatValue(rowCombat + 17, 2);
		gcfgc.combat.healHP = sheet.getFloatValue(rowCombat + 18, 2);
		gcfgc.combat.aoeInterval = sheet.getShortValue(rowCombat + 15, 5);
		gcfgc.combat.aoeEnemyCount = sheet.getByteValue(rowCombat + 16, 5);
		
		gcfgc.combat.firstCombatID = sheet.getShortValue(rowCombat + 15, 9);
		if( ! combatIDs.contains(gcfgc.combat.firstCombatID) )
			throw new Exception("新手战役引用战役ID  " + gcfgc.combat.firstCombatID + "不存在");
		gcfgc.combat.firstCombatGeneralLevel = sheet.getByteValue(rowCombat + 16, 9);
		gcfgc.combat.firstCombatGenerals = new ArrayList<Short>();
		
		gcfgc.combat.winEffectID = sheet.getShortValue(rowCombat + 4, 11);
		gcfgc.combat.winEffectTime = sheet.getShortValue(rowCombat + 4, 13);
		if( ! uiEffectIDs.contains(gcfgc.combat.winEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.winEffectID + "不存在或类型不符");
		gcfgc.combat.loseEffectID = sheet.getShortValue(rowCombat + 5, 11);
		gcfgc.combat.loseEffectTime = sheet.getShortValue(rowCombat + 5, 13);
		if( ! uiEffectIDs.contains(gcfgc.combat.loseEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.winEffectID + "不存在或类型不符");
		gcfgc.combat.ydbEffectID = sheet.getShortValue(rowCombat + 6, 11);
		gcfgc.combat.ydbEffectTime = sheet.getShortValue(rowCombat + 6, 13);
		if( ! uiEffectIDs.contains(gcfgc.combat.ydbEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.ydbEffectID + "不存在或类型不符");
		
		gcfgc.combat.shadowEffectID = sheet.getShortValue(rowCombat + 1, 16);
		if( ! artEffectIDs.contains(gcfgc.combat.shadowEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.shadowEffectID + "不存在或类型不符");
		gcfgc.combat.moveTargetEffectID = sheet.getShortValue(rowCombat + 2, 16);
		if( ! artEffectIDs.contains(gcfgc.combat.moveTargetEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.moveTargetEffectID + "不存在或类型不符");
		gcfgc.combat.enemySelectEffectID = sheet.getShortValue(rowCombat + 3, 16);
		if( ! artEffectIDs.contains(gcfgc.combat.enemySelectEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.enemySelectEffectID + "不存在或类型不符");
		gcfgc.combat.allySelectEffectID = sheet.getShortValue(rowCombat + 4, 16);
		if( ! artEffectIDs.contains(gcfgc.combat.allySelectEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.allySelectEffectID + "不存在或类型不符");
		gcfgc.combat.attackRangeEffectID = sheet.getShortValue(rowCombat + 5, 16);
		if( ! artEffectIDs.contains(gcfgc.combat.attackRangeEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.attackRangeEffectID + "不存在或类型不符");
		
		gcfgc.combat.healEffectID = sheet.getShortValue(rowCombat + 1, 20);
		if( ! artEffectIDs.contains(gcfgc.combat.healEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.healEffectID + "不存在或类型不符");
		gcfgc.combat.damageEffectID = sheet.getShortValue(rowCombat + 2, 20);
		if( ! artEffectIDs.contains(gcfgc.combat.damageEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.damageEffectID + "不存在或类型不符");
		gcfgc.combat.healCriticalEffectID = sheet.getShortValue(rowCombat + 3, 20);
		if( ! artEffectIDs.contains(gcfgc.combat.healCriticalEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.healCriticalEffectID + "不存在或类型不符");
		gcfgc.combat.damageCriticalEffectID = sheet.getShortValue(rowCombat + 4, 20);
		if( ! artEffectIDs.contains(gcfgc.combat.damageCriticalEffectID) )
			throw new Exception("战役引用特效ID  " + gcfgc.combat.damageCriticalEffectID + "不存在或类型不符");
		gcfgc.combat.hseTime = sheet.getShortValue(rowCombat + 5, 20);
		
		final int rowPetFarm = locateColTag(tname, sheet, 0, "宠物经营");
		gcfgs.petFarm = new SBean.PetFarmCFGS();
		gcfgs.petFarm.produceCost = sheet.getByteValue(rowPetFarm + 1, 2);
		gcfgs.petFarm.lvlOpen = sheet.getShortValue(rowPetFarm + 1, 5);
		gcfgs.petFarm.lvlPet = sheet.getShortValue(rowPetFarm + 1, 8);
		gcfgs.petFarm.firstHungry = sheet.getShortValue(rowPetFarm + 1, 11);
		gcfgs.petFarm.hungryTimeBase = sheet.getShortValue(rowPetFarm + 2, 2);
		gcfgs.petFarm.hungryTimeFloat = sheet.getShortValue(rowPetFarm + 2, 5);
		gcfgs.petFarm.maxEatTimes = sheet.getByteValue(rowPetFarm + 2, 8);
		gcfgs.petFarm.petMaxEatTimes = sheet.getByteValue(rowPetFarm + 2, 11);
		gcfgs.petFarm.noHungryTimeBase = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(rowPetFarm + 3, 2));
		gcfgs.petFarm.noHungryTimeSpan = sheet.getIntValue(rowPetFarm + 3, 5);
		gcfgs.petFarm.eatGainBase = sheet.getByteValue(rowPetFarm + 3, 8);
		gcfgs.petFarm.eatGainFloat = sheet.getByteValue(rowPetFarm + 3, 11);
		gcfgs.petFarm.hungryEffect = sheet.getByteValue(rowPetFarm + 4, 2);
		gcfgs.petFarm.getProductEffect = sheet.getByteValue(rowPetFarm + 4, 5);
		gcfgs.petFarm.loyaltyEffect = sheet.getByteValue(rowPetFarm + 4, 8);
		gcfgs.petFarm.growthRateAffect = new ArrayList<SBean.PetFarmGrowthRateCFG>();
		int colGrowth = 2;
		while( sheet.isNotEmpty(rowPetFarm + 5, colGrowth))		
		{
			SBean.PetFarmGrowthRateCFG g = new SBean.PetFarmGrowthRateCFG();
			g.growthRateMax = sheet.getShortValue(rowPetFarm + 5, colGrowth);
			g.addCount = sheet.getByteValue(rowPetFarm + 6, colGrowth);			
			gcfgs.petFarm.growthRateAffect.add(g);
			colGrowth ++;
		}
		
		String petFarmCFG = "{";
		petFarmCFG += "produceCost = " + gcfgs.petFarm.produceCost + ",";
		petFarmCFG += "lvlOpen = " + gcfgs.petFarm.lvlOpen + ",";
		petFarmCFG += "lvlPet = " + gcfgs.petFarm.lvlPet + ",";
		petFarmCFG += "firstHungry = " + gcfgs.petFarm.firstHungry + ",";
		petFarmCFG += "hungryTimeBase = " + gcfgs.petFarm.hungryTimeBase + ",";
		petFarmCFG += "hungryTimeFloat = " + gcfgs.petFarm.hungryTimeFloat + ",";
		petFarmCFG += "maxEatTimes = " + gcfgs.petFarm.maxEatTimes + ",";
		petFarmCFG += "petMaxEatTimes = " + gcfgs.petFarm.petMaxEatTimes + ",";
		petFarmCFG += "noHungryTimeBase = " + gcfgs.petFarm.noHungryTimeBase + ",";
		petFarmCFG += "noHungryTimeSpan = " + gcfgs.petFarm.noHungryTimeSpan + ",";
		petFarmCFG += "hungryEffect = " + gcfgs.petFarm.hungryEffect + ",";
		petFarmCFG += "produceEffect = " + gcfgs.petFarm.produceEffect + ",";
		petFarmCFG += "getProductEffect = " + gcfgs.petFarm.getProductEffect + ",";
		petFarmCFG += "loyaltyEffect = " + gcfgs.petFarm.loyaltyEffect + ",";
		petFarmCFG += "}";
		luaCfg.put("petFarm", petFarmCFG);
		
		{
			gcfgs.qqvipRewards = new ArrayList<SBean.QQVIPRewardsCFGS>();
			final int rowStart = locateColTag(tname, sheet, 0, "qqvip奖励") + 2;
			int colStart = 2;
			row = rowStart;
			col = colStart;
			while( sheet.isNotEmpty(row, col) )
			{
				SBean.QQVIPRewardsCFGS qqvipRewardscfg = new SBean.QQVIPRewardsCFGS();
				qqvipRewardscfg.rewards = new ArrayList<SBean.DropEntryNew>();
				for (int i = 0; i < 3; ++i)
				{
					byte dtype = sheet.getByteValue(row++, col);
					short did = sheet.getShortValue(row++, col);
					int dcnt = sheet.getIntValue(row++, col);
					if (dtype != -100)
					{
						switch( dtype )
						{
						case GameData.COMMON_TYPE_ITEM:
							{
								if( ! itemIDs.containsKey(did) )
										throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的道具ID ");
							}
							break;
						case GameData.COMMON_TYPE_EQUIP:
							{
								if( ! equipIDs.containsKey(did) )
										throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的装备ID ");
							}
							break;
						case GameData.COMMON_TYPE_GENERAL:
							{
								if( ! generalIDs.containsKey(did) )
										throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的武将ID ");
							}
							break;
						case GameData.COMMON_TYPE_PET:
							{
								if( ! petIDs.containsKey(did) )
										throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的宠物ID ");
							}
							break;
						case GameData.COMMON_TYPE_STONE:
						case GameData.COMMON_TYPE_MONEY:
							{
							}
							break;
						case GameData.COMMON_TYPE_NULL:
							break;
						default:
								throw new Exception(tname + " row=" + row + " 错误的掉落类型 " + dtype);
						}
						qqvipRewardscfg.rewards.add(new SBean.DropEntryNew(dtype, did, dcnt));
					}
				}
				gcfgs.qqvipRewards.add(qqvipRewardscfg);
				row = rowStart;
				++col;
			}
			
			String cfg = "{";
			for(SBean.QQVIPRewardsCFGS e : gcfgs.qqvipRewards)
			{
				cfg += "{";
				for (SBean.DropEntryNew ee : e.rewards)
				{
					cfg += "{type=" + ee.type + ", id=" + ee.id + ", count=" + ee.arg + "},";
				}
				cfg += "},";
			}
			cfg += "}";
			luaCfg.put("qqvipRewards", cfg);
		}
		
//		{
//			gcfgs.levelLimit = new SBean.LevelLimitCFGS();
		// final int rowStart = locateColTag(tname, sheet, 0, "动态等级限");
//			int colStart = 1;
//			row = rowStart+2;
//			col = colStart;
//			gcfgs.levelLimit.resorceLevelLimit = sheet.getIntValue(row, col++);
//			gcfgs.levelLimit.sampleCount = sheet.getIntValue(row, col++);
//			gcfgs.levelLimit.openCountReq = (int)(gcfgs.levelLimit.sampleCount * (sheet.getIntValue(row, col++)/100.0f));
//			gcfgs.levelLimit.levels = new ArrayList<SBean.LevelLimitItemCFGS>();
//			row = rowStart + 4;
//			col = colStart;
//			while( sheet.isNotEmpty(row, col) )
//			{
//				SBean.LevelLimitItemCFGS llcfg = new SBean.LevelLimitItemCFGS();
//				llcfg.level = sheet.getIntValue(row, col++);
//				llcfg.minDuration = sheet.getIntValue(row, col++);
//				llcfg.maxDuration = sheet.getIntValue(row, col++);
//				llcfg.openSpan = sheet.getIntValue(row, col++);
//				llcfg.showSpan = sheet.getIntValue(row, col++);
//				llcfg.lessThan20ExpAdd = sheet.getIntValue(row, col++);
//				llcfg.lessThan40ExpAdd = sheet.getIntValue(row, col++);
//				gcfgs.levelLimit.levels.add(llcfg);
//				row++;
//				col = colStart;
//			}
//		}
		{
			gcfgs.topLevelAura = new SBean.TopLevelAuraCFGS();
			final int rowStart = locateColTag(tname, sheet, 0, "最高等级光环");
			int colStart = 2;
			row = rowStart+2;
			col = colStart;
			gcfgs.topLevelAura.sampleCount = sheet.getIntValue(row, col++);
			gcfgs.topLevelAura.lessThan20ExpAdd = sheet.getIntValue(row, col++);
			gcfgs.topLevelAura.lessThan40ExpAdd = sheet.getIntValue(row, col++);
		}
	}
	
	public void loadEquips(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs, SBean.GameDataCFGT gcfgt) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.equips = new ArrayList<SBean.EquipCFGS>();
		gcfgt.equips = new ArrayList<SBean.GeneralName>();
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.EquipCFGS cfgs = new SBean.EquipCFGS();
			cfgs.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			String name = sheet.getStringValue(row, 1);
			gcfgt.equips.add(new SBean.GeneralName(cfgs.id, name));
			cfgs.name = name;
			cfgs.rank = sheet.getByteValue(row,  20);
			if( cfgs.rank < 0 || cfgs.rank >= GameData.EQUIP_RANK_COUNT )
				throw new Exception("装备品级错误,  id = " + cfgs.id);
			cfgs.lvlReq = sheet.getByteValue(row,  21);
			cfgs.type = sheet.getByteValue(row, 4);
			cfgs.gildPrice = sheet.getIntValue(row, 5);
			cfgs.gildVal = sheet.getIntValue(row, 29);
			cfgs.price = sheet.getIntValue(row, 17);
			String p = sheet.getStringValue(row, 30);
			String[] powers = p.split(",");
			cfgs.powers = new ArrayList<Integer>();
			for (String power : powers) {
				int v = Integer.parseInt(power.trim());
				cfgs.powers.add(v);
			}
			
			cfgs.combineMoney = -1;
			cfgs.combinePieces = new ArrayList<SBean.DropEntry>();
			if( sheet.getByteValue(row, 22) == 1 )
			{
				cfgs.combineMoney = sheet.getIntValue(row, 23);
				for(int i = 0; i < 4; ++i)
				{
					String str = sheet.getStringValue(row, 24 + i);
					String[] v = str.split(",");
					byte t = Byte.parseByte(v[0].trim());
					short id = Short.parseShort(v[1].trim());
					byte cnt = Byte.parseByte(v[2].trim());
					if( (t == GameData.COMMON_TYPE_ITEM || t == GameData.COMMON_TYPE_EQUIP) && cnt > 0 )
					{
						SBean.DropEntry ex = null;
						for(SBean.DropEntry e : cfgs.combinePieces)
						{
							if( e.type == t && e.id == id )
							{
								ex = e;
								break;
							}
						}
						if( ex == null )
							cfgs.combinePieces.add(new SBean.DropEntry(t, cnt, id));
						else
							ex.arg += cnt;
					}
				}
			}
			
			gcfgs.equips.add(cfgs);
			if( null != equipIDs.put(cfgs.id, cfgs) )
				throw new Exception("装备ID重复， id = " + cfgs.id);
			++row;
		}
		
		for(SBean.EquipCFGS e : gcfgs.equips)
		{
			for(SBean.DropEntry d : e.combinePieces)
			{
				switch( d.type )
				{
				case GameData.COMMON_TYPE_ITEM:
					{
						SBean.ItemCFGS icfg = itemIDs.get(d.id);
						if( icfg == null )
								throw new Error(tname + " 错误的装备组件 , 装备id=" + e.id + ", 组件ID=" + d.id);
						if( icfg.type == GameData.ITEM_TYPE_SCROLL_PIECE && icfg.arg1 != e.id )
								throw new Error(tname + " 错误的装备组件 , 装备id=" + e.id + ", 组件ID=" + d.id);
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{
						if( d.id == e.id || ! equipIDs.containsKey(d.id) )
								throw new Error(tname + " 错误的装备组件 , 装备id=" + e.id + ", 组件ID=" + d.id);
					}
					break;
				default:
						throw new Error(tname + " 错误的装备组件 , 装备id=" + e.id + ", type=" + d.type);
				}
			}
		}
		for(SBean.ItemCFGS e : gcfgs.items)
		{
			switch( e.type )
			{
			case GameData.ITEM_TYPE_EQUIP_PIECE:
				{
					SBean.EquipCFGS ecfg = equipIDs.get((short)e.arg1);
					if( ecfg == null )
							throw new Error(tname + " 错误的道具碎片 , 道具id=" + e.id + ", target=" + e.arg1);
					boolean bFound = false;
					for(SBean.DropEntry d : ecfg.combinePieces)
					{
						if( d.type == GameData.COMMON_TYPE_ITEM && d.id == e.id && d.arg == e.arg2 
								&& ecfg.combinePieces.size() == 1 && ecfg.combineMoney == e.arg3 )
						{
							bFound = true;
							break;
						}
					}
					if( ! bFound )
							throw new Error(tname + " 错误的道具碎片 , 道具id=" + e.id + ", target=" + e.arg1);
				}
				break;
			case GameData.ITEM_TYPE_SCROLL_PIECE:
				{
					if( e.arg1 == e.id || ! itemIDs.containsKey((short)e.arg1) )
							throw new Error(tname + " 错误的道具碎片 , 道具id=" + e.id + ", target=" + e.arg1);
				}
				break;
			default:
				break;
			}
		}
	}
	
	public void loadTasks(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.taskGroups = new ArrayList<SBean.TaskGroupCFGS>();
		
		final int rowStart = 2; // start row
		int row = rowStart;
		Set<Short> groupIDs = new TreeSet<Short>();
		SBean.TaskGroupCFGS group = null;
		while( sheet.isNotEmpty(row, 0) )
		{
			short gid = sheet.getShortValue(row, 1);
			short seq = sheet.getShortValue(row, 2);
			if( group == null || gid != group.id )
			{
				if( groupIDs.contains(gid) )
					throw new Exception(tname + " 链ID重复， " + gid);
				groupIDs.add(gid);
				group = new SBean.TaskGroupCFGS();
				group.id = gid;
				group.tasks = new ArrayList<SBean.TaskCFGS>();
				gcfgs.taskGroups.add(group);
			}
			if( seq != group.tasks.size() + 1 )
				throw new Exception(tname + " 链内 seq 必须严格递增");
			SBean.TaskCFGS cfgs = new SBean.TaskCFGS();
			cfgs.lvlReq = sheet.getShortValue(row, 7);
			cfgs.gidReq = sheet.getShortValue(row, 8);
			cfgs.type = sheet.getByteValue(row, 9);
			cfgs.arg1 = sheet.getIntValue(row, 10);
			cfgs.arg2 = sheet.getIntValue(row, 11);
			cfgs.arg3 = sheet.getIntValue(row, 12);
			cfgs.arg4 = sheet.getIntValue(row, 13);
			cfgs.rewardExp = sheet.getIntValue(row, 14);
			cfgs.rewardMoney = sheet.getIntValue(row, 15);
			cfgs.rewardStone = sheet.getIntValue(row, 16);
			cfgs.rewards = new ArrayList<SBean.DropEntry>();
			for(int i = 0; i < 3; ++i)
			{
				byte dtype = sheet.getByteValue(row, 17 + 3*i);
				if( dtype < 0 || dtype > 2 )
					break;
				SBean.DropEntry d = new SBean.DropEntry();
				d.type = dtype;
				d.id = sheet.getShortValue(row, 17 + 3*i + 1);
				d.arg = sheet.getByteValue(row, 17 + 3*i + 2);
				cfgs.rewards.add(d);
			}
			group.tasks.add(cfgs);
			++row;
		}
	}
	
	public void loadShop(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		// 普通
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);

			gcfgs.shopNormal = new SBean.ShopCFGS();
			gcfgs.shopNormal.type = GameData.SHOP_TYPE_NORMAL;
			{
				final int row = locateColTag(tname, sheet, 0, "普通商城");

				gcfgs.shopNormal.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopNormal.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopNormal.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopNormal.refreshCurUnit = GameData.CURRENCY_UNIT_STONE;

				gcfgs.shopNormal.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopNormal.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopNormal.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				//Map<Byte, List<SBean.ShopGoods>> map = new TreeMap<Byte, List<SBean.ShopGoods>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);
							
					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);
					
					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopNormal.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopNormal.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		// 竞技场
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(1);

			gcfgs.shopArena = new SBean.ShopCFGS();
			gcfgs.shopArena.type = GameData.SHOP_TYPE_ARENA;
			{
				final int row = locateColTag(tname, sheet, 0, "竞技场商城");

				gcfgs.shopArena.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopArena.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopArena.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopArena.refreshCurUnit = GameData.CURRENCY_UNIT_ARENA;

				gcfgs.shopArena.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopArena.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopArena.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);
							
					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}
					
					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopArena.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopArena.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		
		// 远征
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(2);

			gcfgs.shopMarch = new SBean.ShopCFGS();
			gcfgs.shopMarch.type = GameData.SHOP_TYPE_MARCH;
			{
				final int row = locateColTag(tname, sheet, 0, "远征商城");

				gcfgs.shopMarch.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopMarch.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopMarch.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopMarch.refreshCurUnit = GameData.CURRENCY_UNIT_MARCH;

				gcfgs.shopMarch.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopMarch.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopMarch.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);
							
					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopMarch.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopMarch.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		
		// 大富翁钻石
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(6);

			gcfgs.shopRichStone = new SBean.ShopCFGS();
			gcfgs.shopRichStone.type = GameData.SHOP_TYPE_RICH_STONE;
			{
				final int row = locateColTag(tname, sheet, 0, "大富翁钻石商城");

				gcfgs.shopRichStone.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopRichStone.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichStone.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopRichStone.refreshCurUnit = GameData.CURRENCY_UNIT_STONE;

				gcfgs.shopRichStone.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopRichStone.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichStone.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				//Map<Byte, List<SBean.ShopGoods>> map = new TreeMap<Byte, List<SBean.ShopGoods>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopRichStone.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopRichStone.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		
		// 大富翁币
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(7);

			gcfgs.shopRichPoint = new SBean.ShopCFGS();
			gcfgs.shopRichPoint.type = GameData.SHOP_TYPE_RICH_POINT;
			{
				final int row = locateColTag(tname, sheet, 0, "大富翁币商城");

				gcfgs.shopRichPoint.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopRichPoint.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichPoint.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopRichPoint.refreshCurUnit = GameData.CURRENCY_UNIT_RICH_POINT;

				gcfgs.shopRichPoint.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopRichPoint.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichPoint.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				//Map<Byte, List<SBean.ShopGoods>> map = new TreeMap<Byte, List<SBean.ShopGoods>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopRichPoint.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopRichPoint.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		
		// 大富翁赤金（黑店）
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(8);

			gcfgs.shopRichGold = new SBean.ShopCFGS();
			gcfgs.shopRichGold.type = GameData.SHOP_TYPE_RICH_GOLD;
			{
				final int row = locateColTag(tname, sheet, 0, "大富翁赤金商城");

				gcfgs.shopRichGold.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopRichGold.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichGold.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopRichGold.refreshCurUnit = GameData.CURRENCY_UNIT_RICH_GOLD;

				gcfgs.shopRichGold.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopRichGold.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRichGold.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				//Map<Byte, List<SBean.ShopGoods>> map = new TreeMap<Byte, List<SBean.ShopGoods>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopRichGold.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopRichGold.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}

		// 国家
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(5);

			gcfgs.shopForce = new SBean.ShopCFGS();
			gcfgs.shopForce.type = GameData.SHOP_TYPE_FORCE;
			{
				final int row = locateColTag(tname, sheet, 0, "国家商城");

				gcfgs.shopForce.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopForce.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopForce.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopForce.refreshCurUnit = GameData.CURRENCY_UNIT_FORCE;

				gcfgs.shopForce.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopForce.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopForce.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopForce.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopForce.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}

		// 随机商人1
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(3);

			gcfgs.shopRand1 = new SBean.ShopCFGS();
			gcfgs.shopRand1.type = GameData.SHOP_TYPE_RAND1;
			{
				final int row = locateColTag(tname, sheet, 0, "随机商人1");

				gcfgs.shopRand1.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopRand1.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRand1.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopRand1.refreshCurUnit = GameData.CURRENCY_UNIT_STONE;

				gcfgs.shopRand1.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopRand1.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRand1.refreshPrice.add(Integer.parseInt(s));
					}
				}
				gcfgs.shopRand1.randSummonStone = sheet.getIntValue(row+6, 2);
				gcfgs.shopRand1.randSummonPro = sheet.getFloatValue(row+7, 2);
				gcfgs.shopRand1.randSummonTime = sheet.getIntValue(row+8, 2) * 60;
				gcfgs.shopRand1.randSummonVit = sheet.getShortValue(row+9, 2);
				gcfgs.shopRand1.randSummonCID = sheet.getShortValue(row+10, 2);
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopRand1.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopRand1.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}

		// 随机商人2
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(4);

			gcfgs.shopRand2 = new SBean.ShopCFGS();
			gcfgs.shopRand2.type = GameData.SHOP_TYPE_RAND2;
			{
				final int row = locateColTag(tname, sheet, 0, "随机商人2");

				gcfgs.shopRand2.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopRand2.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRand2.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopRand2.refreshCurUnit = GameData.CURRENCY_UNIT_STONE;

				gcfgs.shopRand2.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopRand2.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopRand2.refreshPrice.add(Integer.parseInt(s));
					}
				}
				gcfgs.shopRand2.randSummonStone = sheet.getIntValue(row+6, 2);
				gcfgs.shopRand2.randSummonPro = sheet.getFloatValue(row+7, 2);
				gcfgs.shopRand2.randSummonTime = sheet.getIntValue(row+8, 2) * 60;
				gcfgs.shopRand2.randSummonVit = sheet.getShortValue(row+9, 2);
				gcfgs.shopRand2.randSummonCID = sheet.getShortValue(row+10, 2);
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopRand2.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopRand2.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}
		
		// 炼狱场
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(9);

			gcfgs.shopSuperArena = new SBean.ShopCFGS();
			gcfgs.shopSuperArena.type = GameData.SHOP_TYPE_SUPER_ARENA;
			{
				final int row = locateColTag(tname, sheet, 0, "炼狱场商城");

				gcfgs.shopSuperArena.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopSuperArena.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopSuperArena.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopSuperArena.refreshCurUnit = GameData.CURRENCY_UNIT_SUPER_ARENA;

				gcfgs.shopSuperArena.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopSuperArena.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopSuperArena.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopSuperArena.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopSuperArena.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}

		}		
		
		// 决斗场
		{
			ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(10);

			gcfgs.shopDuel = new SBean.ShopCFGS();
			gcfgs.shopDuel.type = GameData.SHOP_TYPE_DUEL;
			{
				final int row = locateColTag(tname, sheet, 0, "决斗场商城");

				gcfgs.shopDuel.lvlReq = sheet.getShortValue(row+2, 2);
				gcfgs.shopDuel.refreshTime = new ArrayList<Short>();
				{
					String str = sheet.getStringValue(row+3, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopDuel.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
					}
				}

				gcfgs.shopDuel.refreshCurUnit = GameData.CURRENCY_UNIT_DUEL;

				gcfgs.shopDuel.refreshCount = sheet.getByteValue(row+4, 2);
				gcfgs.shopDuel.refreshPrice = new ArrayList<Integer>();
				{
					String str = sheet.getStringValue(row+5, 2);
					String[] v = str.split(";");
					for(String s : v)
					{
						gcfgs.shopDuel.refreshPrice.add(Integer.parseInt(s));
					}
				}
			}

			{
				int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
				Set<Short> ids = new TreeSet<Short>();
				Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
				while( sheet.isNotEmpty(row, 1) )
				{
					short id = sheet.getShortValue(row, 1);
					if( ids.contains(id) )
						throw new Exception(tname + " id = " + id + ", 重复的货物ID");
					ids.add(id);
					byte type = sheet.getByteValue(row, 2);
					SBean.ShopGoods goods = new SBean.ShopGoods();
					goods.id = id;
					goods.pro = sheet.getFloatValue(row, 3);
					goods.item = new SBean.DropEntry();
					goods.item.type = sheet.getByteValue(row, 4);
					goods.item.id = sheet.getShortValue(row, 5);
					{
						if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
								|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
							throw new Exception(tname + " 错误的货物ID id = " + id);

					}
					goods.item.arg = sheet.getByteValue(row, 7);
					goods.curUnit = sheet.getByteValue(row, 8);
					goods.price = sheet.getIntValue(row, 9);
					short lc = sheet.getShortValue(row, 10);

					Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
					if( map1 == null )
					{
						map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
						map2.put(lc, map1);
					}

					List<SBean.ShopGoods> lst = map1.get(type);
					if( lst == null )
					{
						lst = new ArrayList<SBean.ShopGoods>();
						map1.put(type, lst);
					}
					lst.add(goods);				
					++row;
				}
				// TODO load from map
				gcfgs.shopDuel.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
				for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
				{
					SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
					sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
					sl.lvlCeil = e1.getKey();
					gcfgs.shopDuel.levels.add(sl);
					Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
					for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
					{
						List<SBean.ShopGoods> l = e.getValue();
						if( ! l.isEmpty() )
						{
							float sum = 0;
							for(SBean.ShopGoods g : l)
							{
								sum += g.pro;
							}
							float psum = 0;
							for(SBean.ShopGoods g : l)
							{
								float p = g.pro / sum;
								g.pro = p + psum;
								psum += p;
							}
							SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
							if( e.getKey().byteValue() == 0 )
								sl.groups.add(0, g);
							else
								sl.groups.add(g);
						}
					}
				}
			}
		}
		
		// 祈福商城
			{
				ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(11);

				gcfgs.shopBless = new SBean.ShopCFGS();
				gcfgs.shopBless.type = GameData.SHOP_TYPE_BLESS;
				{
					final int row = locateColTag(tname, sheet, 0, "祈福商城");

					gcfgs.shopBless.lvlReq = sheet.getShortValue(row+2, 2);
					gcfgs.shopBless.refreshTime = new ArrayList<Short>();
					{
						String str = sheet.getStringValue(row+3, 2);
						String[] v = str.split(";");
						for(String s : v)
						{
							gcfgs.shopBless.refreshTime.add((short)StringConverter.parseMinuteOfDay(s));		
						}
					}

					gcfgs.shopBless.refreshCurUnit = GameData.CURRENCY_UNIT_STONE;

					gcfgs.shopBless.refreshCount = sheet.getByteValue(row+4, 2);
					gcfgs.shopBless.refreshPrice = new ArrayList<Integer>();
					{
						String str = sheet.getStringValue(row+5, 2);
						String[] v = str.split(";");
						for(String s : v)
						{
							gcfgs.shopBless.refreshPrice.add(Integer.parseInt(s));
						}
					}
				}

				{
					int row = locateColTag(tname, sheet, 0, "货物列表") + 2;
					Set<Short> ids = new TreeSet<Short>();
					Map<Short, Map<Byte, List<SBean.ShopGoods>>> map2 = new TreeMap<Short, Map<Byte, List<SBean.ShopGoods>>>();
					//Map<Byte, List<SBean.ShopGoods>> map = new TreeMap<Byte, List<SBean.ShopGoods>>();
					while( sheet.isNotEmpty(row, 1) )
					{
						short id = sheet.getShortValue(row, 1);
						if( ids.contains(id) )
							throw new Exception(tname + " id = " + id + ", 重复的货物ID");
						ids.add(id);
						byte type = sheet.getByteValue(row, 2);
						SBean.ShopGoods goods = new SBean.ShopGoods();
						goods.id = id;
						goods.pro = sheet.getFloatValue(row, 3);
						goods.item = new SBean.DropEntry();
						goods.item.type = sheet.getByteValue(row, 4);
						goods.item.id = sheet.getShortValue(row, 5);
						{
							if( goods.item.type == GameData.COMMON_TYPE_ITEM && ! itemIDs.containsKey(goods.item.id)
									|| goods.item.type == GameData.COMMON_TYPE_EQUIP && ! equipIDs.containsKey(goods.item.id) )
								throw new Exception(tname + " 错误的货物ID id = " + id);
								
						}
						goods.item.arg = sheet.getByteValue(row, 7);
						goods.curUnit = sheet.getByteValue(row, 8);
						goods.price = sheet.getIntValue(row, 9);
						short lc = sheet.getShortValue(row, 10);
						
						Map<Byte, List<SBean.ShopGoods>> map1 = map2.get(lc);
						if( map1 == null )
						{
							map1 = new TreeMap<Byte, List<SBean.ShopGoods>>();
							map2.put(lc, map1);
						}

						List<SBean.ShopGoods> lst = map1.get(type);
						if( lst == null )
						{
							lst = new ArrayList<SBean.ShopGoods>();
							map1.put(type, lst);
						}
						lst.add(goods);				
						++row;
					}
					// TODO load from map
					gcfgs.shopBless.levels = new ArrayList<SBean.ShopGoodsLevelCFGS>();
					for(Map.Entry<Short, Map<Byte, List<SBean.ShopGoods>>> e1 : map2.entrySet())
					{
						SBean.ShopGoodsLevelCFGS sl = new SBean.ShopGoodsLevelCFGS();
						sl.groups = new ArrayList<SBean.ShopGoodsGroup>();
						sl.lvlCeil = e1.getKey();
						gcfgs.shopBless.levels.add(sl);
						Map<Byte, List<SBean.ShopGoods>> map1 = e1.getValue();
						for(Map.Entry<Byte, List<SBean.ShopGoods>> e : map1.entrySet())
						{
							List<SBean.ShopGoods> l = e.getValue();
							if( ! l.isEmpty() )
							{
								float sum = 0;
								for(SBean.ShopGoods g : l)
								{
									sum += g.pro;
								}
								float psum = 0;
								for(SBean.ShopGoods g : l)
								{
									float p = g.pro / sum;
									g.pro = p + psum;
									psum += p;
								}
								SBean.ShopGoodsGroup g = new SBean.ShopGoodsGroup(l);
								if( e.getKey().byteValue() == 0 )
									sl.groups.add(0, g);
								else
									sl.groups.add(g);
							}
						}
					}
				}

			}
	}
	
	static class GeneralWithP
	{
		public GeneralWithP(short id, int p)
		{
			this.id = id;
			this.p = p;
		}
		public short id;
		public int p;
	}
	public void loadEggs(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.eggs = new ArrayList<SBean.EggCFGS>();
		
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.EggCFGS egg = new SBean.EggCFGS();
			egg.id = sheet.getByteValue(row, 0);
			String eggName = sheet.getStringValue(row, 1);
			String eggDes = sheet.getStringValue(row, 2);
			egg.cost = sheet.getIntValue(row, 4);
			egg.cost10 = sheet.getIntValue(row, 5);
			egg.freeTimes = sheet.getByteValue(row, 6);
			egg.freeCool = sheet.getShortValue(row, 7);
			egg.firstFreeDropTableID = sheet.getShortValue(row, 8);
			if( ! dropTableIDs.contains(egg.firstFreeDropTableID) )
				throw new Exception(tname + " 掉落表ID " + egg.firstFreeDropTableID + " 不存在");
			egg.firstDropTableID = sheet.getShortValue(row, 21);
			if( egg.firstDropTableID > 0 && ! dropTableIDs.contains(egg.firstDropTableID) )
				throw new Exception(tname + " 掉落表ID " + egg.firstDropTableID + " 不存在");
			egg.generalDropTableID = sheet.getShortValue(row, 9);
			if( ! dropTableIDs.contains(egg.generalDropTableID) )
				throw new Exception(tname + " 掉落表ID " + egg.generalDropTableID + " 不存在");
			egg.itemDropTableID = sheet.getShortValue(row, 10);
			if( ! dropTableIDs.contains(egg.itemDropTableID) )
				throw new Exception(tname + " 掉落表ID " + egg.itemDropTableID + " 不存在");
			egg.propGeneral = sheet.getFloatValue(row, 11);
			egg.minGeneral10 = 1;
			egg.maxGeneral10 = sheet.getByteValue(row, 12);
			egg.proectTimes10 = sheet.getByteValue(row, 13);
			egg.poolHigh = sheet.getIntValue(row, 14);
			egg.poolLow = sheet.getIntValue(row, 15);
			egg.generalUp = sheet.getIntValue(row, 16);
			egg.itemDown = sheet.getIntValue(row, 17);
			egg.itemFixed = new SBean.DropEntry(
					sheet.getByteValue(row, 18), 
					sheet.getByteValue(row, 20), 
					sheet.getShortValue(row, 19)
					);
			egg.dropTableIDs100 = sheet.getShortValue(row, 22);
			//TODO
			egg.dropTableIDsFirst10 = new ArrayList<Short>();
			egg.cost10FirstBuy = new ArrayList<Integer>();
			if( egg.id == GameData.EGG_STONE )
			{
				for(int i = 0; i < 4; ++i)
				{
					short did = sheet.getShortValue(7+i, 2);
					if( ! dropTableIDs.contains(did) )
						throw new Exception(tname + " 掉落表ID " + did + " 不存在");
					egg.dropTableIDsFirst10.add(did);
				}
				int nFirstBuy = sheet.getByteValue(12, 2);
				if( nFirstBuy > egg.proectTimes10 )
					throw new Exception(tname + " 保护次数过少");
				for(int i = 0; i < nFirstBuy; ++i)
				{
					int cost10 = sheet.getIntValue(14+i, 2);
					egg.cost10FirstBuy.add(cost10);
				}
			}
			//
			gcfgs.eggs.add(egg);			
			++row;
			System.out.println("egg " + egg.id + ": " + eggName + ", " + eggDes);
		}
	}
	
	public void loadSoulBoxs(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.soulboxs = new ArrayList<SBean.SoulBoxCFGS>();
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			int col = 0;
			SBean.SoulBoxCFGS cfgs = new SBean.SoulBoxCFGS();
			cfgs.id = sheet.getByteValue(row, col++);
			cfgs.startTime = StringConverter.parseDayOfYear(sheet.getStringValue(row, col++));
			cfgs.cost = sheet.getIntValue(row, col++);
			cfgs.normalDropTableID = sheet.getShortValue(row, col++);
			cfgs.weekHotPointFirstInterval = sheet.getByteValue(row, col++);
			cfgs.weekHotPointOtherInterval = sheet.getByteValue(row, col++);
			cfgs.weekHotPointMaxInterval = sheet.getByteValue(row, col++);
			cfgs.weekHotPointWeight = sheet.getByteValue(row, col++);
			cfgs.weekHotPointGeneralID = sheet.getShortValue(row, col++);
			cfgs.dayHotPointDropTimes = new ArrayList<SBean.DropTimes>();
			{
				String str = sheet.getStringValue(row, col++);
				String[] strs = str.split(",");
				float sum = 0;
				for (String e: strs)
				{
					String[] v = e.split(":");
					SBean.DropTimes droptimes = new SBean.DropTimes();
					droptimes.times = Byte.parseByte(v[0]);
					droptimes.rate = Byte.parseByte(v[1]);
					cfgs.dayHotPointDropTimes.add(droptimes);
					sum += droptimes.rate;
				}
				Collections.sort(cfgs.dayHotPointDropTimes, new java.util.Comparator<SBean.DropTimes>()
						{
							public int compare(SBean.DropTimes l, SBean.DropTimes r)
							{
								return (int)(l.rate-r.rate);
							}
						});
				float sp = 0;
				for (SBean.DropTimes e : cfgs.dayHotPointDropTimes)
				{
					sp += e.rate/sum;
					e.rate = sp;
				}
			}
			cfgs.dayHotPointDropCount = new ArrayList<SBean.DropCount>();
			{
				String str = sheet.getStringValue(row, col++);
				String[] strs = str.split(",");
				float sum = 0;
				for (String e: strs)
				{
					String[] v = e.split(":");
					SBean.DropCount dropcount = new SBean.DropCount();
					dropcount.count = Byte.parseByte(v[0]);
					dropcount.rate = Byte.parseByte(v[1]);
					cfgs.dayHotPointDropCount.add(dropcount);
					sum += dropcount.rate;
				}
				Collections.sort(cfgs.dayHotPointDropCount, new java.util.Comparator<SBean.DropCount>()
						{
							public int compare(SBean.DropCount l, SBean.DropCount r)
							{
								return (int)(l.rate-r.rate);
							}
						});
				float sp = 0;
				for (SBean.DropCount e : cfgs.dayHotPointDropCount)
				{
					sp += e.rate/sum;
					e.rate = sp;
				}
			}
			cfgs.dayHotPoint = new ArrayList<SBean.DayHotPoint>();
			int i = 0;
			while( i < 7 )
			{
				SBean.DayHotPoint dhp = new SBean.DayHotPoint(new ArrayList<SBean.HotPoint>());
				String str = sheet.getStringValue(row, col++);
				String[] strs = str.split(",");
				for (String e : strs)
				{
					String[] v = e.split(":");
					SBean.HotPoint hp = new SBean.HotPoint();
					hp.generialID = Short.parseShort(v[0]);
					hp.soulItemID = Short.parseShort(v[1]);
					dhp.dayHotPoint.add(hp);
				}
				cfgs.dayHotPoint.add(dhp);
				i ++;
			}			
			cfgs.optionalHotPoint = new ArrayList<Short>();
			{
				String str = sheet.getStringValue(row, col++);
				String[] strs = str.split(",");
				for (String e : strs)
				{
					short generalID = Short.parseShort(e);
					cfgs.optionalHotPoint.add(generalID);
				}
			}			
			gcfgs.soulboxs.add(cfgs);
			++row;
		}
		System.out.println("soul box week count: " + gcfgs.checkin.size());
	}
	
	public void loadCaptureCity(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		if (gcfgs.capturecity == null)
			gcfgs.capturecity = new SBean.CaptureCityCFGS();
		gcfgs.capturecity.citys = new ArrayList<SBean.CityCFGS>();
		int rowStart = 1; // start row
		int row = rowStart;
		int colStart = 1;
		int col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.CityCFGS cfgs = new SBean.CityCFGS();
			cfgs.id = sheet.getShortValue(row++, col);
			cfgs.name = sheet.getStringValue(row++, col);
			cfgs.combatIDReq = sheet.getIntValue(row++, col);
			cfgs.produce = new ArrayList<SBean.CityProduceCFGS>();
			for (int k = 0; k < 3; ++k)
			{
				byte type = sheet.getByteValue(row++, col);
				short id = sheet.getShortValue(row++, col);
				boolean valid = false;
				switch( type )
				{
				case GameData.COMMON_TYPE_ITEM:
					{
						if( ! itemIDs.containsKey(id) )
								throw new Exception(tname + " " + cfgs.id + ", id=" + id + " 错误的道具ID ");
						valid = true;
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{
						if( ! equipIDs.containsKey(id) )
								throw new Exception(tname + " " + cfgs.id + ", id=" + id + " 错误的装备ID ");
						valid = true;
					}
					break;
				case GameData.COMMON_TYPE_GENERAL:
					{
						if( ! generalIDs.containsKey(id) )
								throw new Exception(tname + " " + cfgs.id + ", id=" + id + " 错误的武将ID ");
						valid = true;
					}
					break;
				case GameData.COMMON_TYPE_STONE:
				case GameData.COMMON_TYPE_MONEY:
					{
						valid = true;
					}
					break;
				case GameData.COMMON_TYPE_NULL:
					break;
				default:
						throw new Exception(tname + " " + cfgs.id + " 错误的掉落类型 " + type);
				}
				if (valid)
				{
					SBean.CityProduceCFGS cpcfg = new SBean.CityProduceCFGS(type, id, new ArrayList<Integer>());
					for (int i = 0; i < 6; ++i)
					{
						cpcfg.produce.add(sheet.getIntValue(row++, col));
					}
					cfgs.produce.add(cpcfg);	
				}
				else
				{
					row += 6;
				}
			}
			cfgs.searchLvlReq = sheet.getIntValue(row++, col);
			String str = sheet.getStringValue(row++, col);
			String[] strs = str.split(";");
			cfgs.searchedLvlMin = Integer.parseInt(strs[0]);
			cfgs.searchedLvlMax = Integer.parseInt(strs[1]);
			
			if (cfgs.id != gcfgs.capturecity.citys.size()+1)
			{
				throw new Exception("城池ID " + cfgs.id + " 不连续或不是从1开始！");
			}
			if (cfgs.produce.isEmpty())
			{
				throw new Exception("城池ID " + cfgs.id + " 没有设置任何奖励！");
			}
			gcfgs.capturecity.citys.add(cfgs);
			row = rowStart;
			++col;
		}
		rowStart = locateColTag(tname, sheet, 0, "搜索相关");
		row = rowStart + 1;
		col = 1;
		gcfgs.capturecity.searchOpenLevel = sheet.getIntValue(row++, col);
		gcfgs.capturecity.searchCostMoney = sheet.getIntValue(row++, col);
		rowStart = row + 1;
		row = rowStart;
		gcfgs.capturecity.searchTypeLvlRanges = new ArrayList<SBean.CitySearchLevelCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			int lvldiffMin = sheet.getIntValue(row++, col);
			int lvldiffMax = sheet.getIntValue(row++, col);
			SBean.CitySearchLevelCFGS cslcfgs = new SBean.CitySearchLevelCFGS(lvldiffMin, lvldiffMax);
			gcfgs.capturecity.searchTypeLvlRanges.add(cslcfgs);
			++col;
			row = rowStart;
		}
		rowStart = row + 2;
		row = rowStart;
		col = 1;
		gcfgs.capturecity.searchTypeSequence = new ArrayList<Integer>();
		while( sheet.isNotEmpty(row, col) )
		{
			int type = sheet.getIntValue(row, col++);
			if (type <= 0 || type > gcfgs.capturecity.searchTypeLvlRanges.size())
			{
				throw new Exception("城池 搜索 方式序列ID " + type + " 超出范围(1-" + gcfgs.capturecity.searchTypeLvlRanges.size()
						+ ")!");
			}
			gcfgs.capturecity.searchTypeSequence.add(type);
		}
		System.out.println("capture searchTypeSequence count: " + gcfgs.capturecity.searchTypeSequence.size());
		row = locateColTag(tname, sheet, 0, "驻守时长相关");
		row += 2;
		col = 1;
		gcfgs.capturecity.guardTimes = new ArrayList<Integer>();
		while( sheet.isNotEmpty(row, col) )
		{
			int hour = sheet.getIntValue(row, col);
			gcfgs.capturecity.guardTimes.add(hour*3600);
			++col;
		}
		row = locateColTag(tname, sheet, 0, "收益系数相关");
		row += 1;
		col = 1;
		gcfgs.capturecity.captureProduceRatio = sheet.getFloatValue(row++, col);
		gcfgs.capturecity.lostProduceRatio = sheet.getFloatValue(row++, col);
		gcfgs.capturecity.counterattackProduceRatio = sheet.getFloatValue(row++, col);
		row = locateColTag(tname, sheet, 0, "结算时间相关");
		row += 1;
		col = 1;
		gcfgs.capturecity.baseRewardTime = (short)StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col));
		System.out.println("capture city count: " + gcfgs.capturecity.citys.size());
	}
	
	public void loadForceBattles(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		if (gcfgs.forcebattles == null)
			gcfgs.forcebattles = new SBean.ForceBattlesCFGS();
		gcfgs.forcebattles.battles = new ArrayList<SBean.ForceBattleCFGS>();
		gcfgs.forcebattles.itemPlusEffects = new ArrayList<SBean.ItemPlusEffectCFGS>();
		int rowStart = 2; // start row
		int colStart = 0;
		int row = rowStart;
		int col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.ForceBattleCFGS cfgs = new SBean.ForceBattleCFGS();
			cfgs.id = sheet.getShortValue(row, col++);
			cfgs.startTime = StringConverter.parseDayOfYear(sheet.getStringValue(row, col++)) + 8*3600 + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
			cfgs.endTime = StringConverter.parseDayOfYear(sheet.getStringValue(row, col++)) + 8*3600 + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
			if (cfgs.startTime >= cfgs.endTime)
				throw new Exception("force battle id=" + cfgs.id + ", satrtTime >= endTime");
			int selectionDayTimeStart = StringConverter.parseDayOfYear(sheet.getStringValue(row, col++)) + 8*3600;
			cfgs.selectionTimeStart = selectionDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
			if (cfgs.selectionTimeStart <= cfgs.startTime || cfgs.selectionTimeStart >= cfgs.endTime)
				throw new Exception("force battle id=" + cfgs.id + ", selectionTimeStart invalid!");
			int knockoutDayTimeStart = StringConverter.parseDayOfYear(sheet.getStringValue(row, col++)) + 8*3600;
			cfgs.knockoutTimeStart = knockoutDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;;
			if (cfgs.knockoutTimeStart <= cfgs.selectionTimeStart || cfgs.knockoutTimeStart >= cfgs.endTime)
				throw new Exception("force battle id=" + cfgs.id + ", knockoutTimeStart invalid!");
			cfgs.forceMemCntReq = sheet.getIntValue(row, col++);
			cfgs.playerLvlReq = sheet.getIntValue(row, col++);
			cfgs.generalLvlReq = sheet.getIntValue(row, col++);
			cfgs.normalDropTblID = sheet.getShortValue(row, col++);
			cfgs.heroDropTblID = sheet.getShortValue(row, col++);
			cfgs.combatsDropTblIDs = new ArrayList<SBean.CombatExtraDropCFGS>();
			{
				String str = sheet.getStringValue(row, col++);
				if (!str.isEmpty())
				{
					String[] strs = str.split(",");
					for (String e: strs)
					{
						String[] v = e.split(":");
						SBean.CombatExtraDropCFGS combatdrop = new SBean.CombatExtraDropCFGS();
						combatdrop.combatID = Short.parseShort(v[0]);
						combatdrop.dropTblID = Short.parseShort(v[1]);
						cfgs.combatsDropTblIDs.add(combatdrop);
					}	
				}
			}
			cfgs.knockout = new ArrayList<SBean.ForceBattleGameCFGS>();
			int gameEndTime = cfgs.knockoutTimeStart;
			for (int i = 0; i < 5; ++i)
			{
				SBean.ForceBattleGameCFGS game = new SBean.ForceBattleGameCFGS();
				game.startTime = knockoutDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
				game.endTime = knockoutDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
				if (game.startTime >= game.endTime || game.startTime < gameEndTime || game.endTime > cfgs.endTime)
					throw new Exception("force battle id=" + cfgs.id + ", force knockout game time invalid!");
				gameEndTime = game.endTime;
				{
					String str = sheet.getStringValue(row, col++);
					String[] strs = str.split(",");
					game.winPoints = Integer.parseInt(strs[0]);
					game.lostPoints = Integer.parseInt(strs[1]);	
				}
				{
					String str = sheet.getStringValue(row, col++);
					String[] strs = str.split(",");
					game.winMoney = Integer.parseInt(strs[0]);
					game.lostMoney = Integer.parseInt(strs[1]);
				}
				cfgs.knockout.add(game);
			}
			cfgs.leagueEndTimes = new ArrayList<Integer>();
			int leagueEndTime = cfgs.selectionTimeStart;
			for (int i = 0; i < 5; ++i)
			{
				int endTime = selectionDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
				cfgs.leagueEndTimes.add(endTime);
				if (endTime < leagueEndTime || endTime > cfgs.knockoutTimeStart)
					throw new Exception("force battle id=" + cfgs.id + ", force league End Time time invalid!");
				leagueEndTime = endTime;
			}
			cfgs.recordEndTime = knockoutDayTimeStart + StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++))*60;
			gcfgs.forcebattles.battles.add(cfgs);
			row++;
			col = 0;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "道具增益相关");
		colStart = 0;
		row = rowStart+2;
		col = colStart+1;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.ItemPlusEffectCFGS effect = new SBean.ItemPlusEffectCFGS();
			effect.id = sheet.getShortValue(row++, col);
			effect.itemID = sheet.getShortValue(row++, col);
			row++;
			effect.rewardPointPerItem = sheet.getShortValue(row++, col);
			effect.plusEffect = new ArrayList<SBean.PlusEffectCFGS>();
			row += 2;
			while( sheet.isNotEmpty(row, 0) )
			{
				SBean.PlusEffectCFGS pe = new SBean.PlusEffectCFGS();
				pe.plus = sheet.getByteValue(row, 0);
				pe.count = sheet.getIntValue(row, col);
				effect.plusEffect.add(pe);
				row++;
			}
			gcfgs.forcebattles.itemPlusEffects.add(effect);
			row = rowStart+2;
			col++;
		}
		
		gcfgs.forcebattles.rewards = new ArrayList<SBean.ForceBattleRewardCFGS>();
		final int rowRewards = locateColTag(tname, sheet, 0, "奖励");
		col = 2;
		while( sheet.isNotEmpty(rowRewards+1, col) )
		{
			int money = sheet.getIntValue(rowRewards+2, col);
			int stone = sheet.getIntValue(rowRewards+4, col);
			short point = sheet.getShortValue(rowRewards+3, col);
			short item1 = sheet.getShortValue(rowRewards+5, col);
			byte item1Count = sheet.getByteValue(rowRewards+6, col);
			short item2 = sheet.getShortValue(rowRewards+7, col);
			byte item2Count = sheet.getByteValue(rowRewards+8, col);
			
			gcfgs.forcebattles.rewards.add(new SBean.ForceBattleRewardCFGS(money, stone, point, item1, item1Count, item2, item2Count));
			++col;
		}
		
		gcfgs.forcebattles.headRewards = new ArrayList<SBean.ForceBattleRewardCFGS>();
		final int rowHeadRewards = locateColTag(tname, sheet, 0, "国主额外奖励");
		col = 2;
		while( sheet.isNotEmpty(rowHeadRewards+1, col) )
		{
			int money = sheet.getIntValue(rowHeadRewards+2, col);
			int stone = sheet.getIntValue(rowHeadRewards+4, col);
			short point = sheet.getShortValue(rowHeadRewards+3, col);
			short item1 = sheet.getShortValue(rowHeadRewards+5, col);
			byte item1Count = sheet.getByteValue(rowHeadRewards+6, col);
			short item2 = sheet.getShortValue(rowHeadRewards+7, col);
			byte item2Count = sheet.getByteValue(rowHeadRewards+8, col);
			
			gcfgs.forcebattles.headRewards.add(new SBean.ForceBattleRewardCFGS(money, stone, point, item1, item1Count, item2, item2Count));
			++col;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "盗宝贼");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.forcebattles.thiefCombats = new ArrayList<SBean.ForceThiefCombatCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.ForceThiefCombatCFGS combat = new SBean.ForceThiefCombatCFGS();
			combat.lvlMin = sheet.getShortValue(row, col++);
			combat.combatIds = new ArrayList<Short>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				short combatId = sheet.getShortValue(row, col++);
				combat.combatIds.add(combatId);
			}
			gcfgs.forcebattles.thiefCombats.add(combat);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "盗宝贼BOSS");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.forcebattles.thiefBossCombats = new ArrayList<SBean.ForceThiefCombatCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.ForceThiefCombatCFGS combat = new SBean.ForceThiefCombatCFGS();
			combat.lvlMin = sheet.getShortValue(row, col++);
			combat.combatIds = new ArrayList<Short>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				short combatId = sheet.getShortValue(row, col++);
				combat.combatIds.add(combatId);
			}
			gcfgs.forcebattles.thiefBossCombats.add(combat);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "盗宝贼序列");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.forcebattles.thiefSequences = new ArrayList<SBean.ForceThiefSequenceCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.ForceThiefSequenceCFGS seq = new SBean.ForceThiefSequenceCFGS();
			seq.startTime = StringConverter.parseMinuteOfDay(sheet.getStringValue(row, col++));
			seq.combatTypes = new ArrayList<Byte>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				byte combatType = sheet.getByteValue(row, col++);
				seq.combatTypes.add(combatType);
			}
			gcfgs.forcebattles.thiefSequences.add(seq);
			row++;
			col = colStart;
		}
		
		gcfgs.forcebattles.thiefRewards = new ArrayList<SBean.ForceThiefRewardCFGS>();
		rowStart = locateColTag(tname, sheet, 0, "盗宝贼奖励");
		col = 2;
		while( sheet.isNotEmpty(rowStart+1, col) )
		{
			int rankFloor = sheet.getIntValue(rowStart+1, col);
			if( rankFloor <= 0 )
				rankFloor = Short.MAX_VALUE;
			short itemId = sheet.getShortValue(rowStart+2, col);
			int itemCount = sheet.getIntValue(rowStart+3, col);
			
			gcfgs.forcebattles.thiefRewards.add(new SBean.ForceThiefRewardCFGS(rankFloor, itemId, itemCount));
			++col;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "神兽");
		gcfgs.forcebattles.beastBasic = new SBean.ForceBeastBasicCFGS();
		gcfgs.forcebattles.beastBasic.resetAttackPropCost = sheet.getIntValue(rowStart+1, 2);
		gcfgs.forcebattles.beastBasic.resetSkillCost = sheet.getIntValue(rowStart+1, 4);
		gcfgs.forcebattles.beastBasic.resetPropCost = new ArrayList<Integer>();
		String str = sheet.getStringValue(rowStart+1, 6);
		if (!str.isEmpty()) {
			String[] strs = str.split(",");
			for (String e: strs) {
				int v = (int)Float.parseFloat(e);
				gcfgs.forcebattles.beastBasic.resetPropCost.add(v);
			}
		}
		gcfgs.forcebattles.beastBasic.mainPropUpCost = sheet.getIntValue(rowStart+1, 8);
		gcfgs.forcebattles.beastBasic.auxPropUpCost = sheet.getIntValue(rowStart+1, 10);
		str = sheet.getStringValue(rowStart+1, 12);
		String[] strs = str.split(";");
		gcfgs.forcebattles.beastBasic.feedItemId = (short)Float.parseFloat(strs[0]);
		gcfgs.forcebattles.beastBasic.feedGain = (short)Float.parseFloat(strs[1]);
		
		rowStart = locateColTag(tname, sheet, 0, "神兽技能升级消耗");
		gcfgs.forcebattles.beastSkillUpCost = new ArrayList<SBean.ForceBeastSkillUpCostCFGS>();
		row = rowStart + 2;
		while( sheet.isNotEmpty(row, 2) ) {
			SBean.ForceBeastSkillUpCostCFGS cfg = new SBean.ForceBeastSkillUpCostCFGS();
			cfg.costs = new ArrayList<Integer>();
			for (int i = 0; i < 5; i ++) {
				int cost = sheet.getIntValue(row, 2 + i);
				cfg.costs.add(cost);
			}
			gcfgs.forcebattles.beastSkillUpCost.add(cfg);
			row ++;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "神兽进阶");
		gcfgs.forcebattles.beastEvo = new ArrayList<SBean.ForceBeastEvoCFGS>();
		row = rowStart + 2;
		while( sheet.isNotEmpty(row, 1) ) {
			SBean.ForceBeastEvoCFGS cfg = new SBean.ForceBeastEvoCFGS();
			cfg.gid = sheet.getShortValue(row, 1);
			cfg.nextCost = sheet.getShortValue(row, 3);
			cfg.nextLvl = sheet.getShortValue(row, 4);
			gcfgs.forcebattles.beastEvo.add(cfg);
			row ++;
		}
	
		rowStart = locateColTag(tname, sheet, 0, "神兽升级经验");
		gcfgs.forcebattles.beastExp = new ArrayList<Integer>();
		row = rowStart + 2;
		col = 2;
		while( sheet.isNotEmpty(row, col) ) {
			int exp = sheet.getIntValue(row, col);
			gcfgs.forcebattles.beastExp.add(exp);
			col ++;
		}
	
		rowStart = locateColTag(tname, sheet, 0, "国家成员额外掉落");
		{
			gcfgs.forcebattles.normalDropTblID = sheet.getShortValue(rowStart+2, 1);
			gcfgs.forcebattles.heroDropTblID = sheet.getShortValue(rowStart+2, 2);
			gcfgs.forcebattles.combatsDropTblIDs = new ArrayList<SBean.CombatExtraDropCFGS>();
			{
				String sstr = sheet.getStringValue(rowStart+2, 3);
				if (!sstr.isEmpty())
				{
					strs = sstr.split(",");
					for (String e: strs)
					{
						String[] v = e.split(":");
						SBean.CombatExtraDropCFGS combatdrop = new SBean.CombatExtraDropCFGS();
						combatdrop.combatID = Short.parseShort(v[0]);
						combatdrop.dropTblID = Short.parseShort(v[1]);
						gcfgs.forcebattles.combatsDropTblIDs.add(combatdrop);
					}	
				}
			}
		}
		
		rowStart = locateColTag(tname, sheet, 0, "限制国战人数");
		gcfgs.forceBattleCount = sheet.getShortValue(rowStart+1, 1);
		
		System.out.println("force battle count: " + gcfgs.forcebattles.battles.size() + ", plus effect count: " + gcfgs.forcebattles.itemPlusEffects.size());
	}
	
	public void loadRich(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		if (gcfgs.rich == null)
			gcfgs.rich= new SBean.RichCFGS();
		
		gcfgs.rich.basic = new SBean.RichBasicCFGS();
		gcfgs.rich.basic.initgold = sheet.getIntValue(1, 1);
		gcfgs.rich.basic.initmovement = sheet.getShortValue(1, 3);
		gcfgs.rich.basic.movecost = sheet.getShortValue(1, 5);
		gcfgs.rich.basic.diceposs = new ArrayList<Short>();
		gcfgs.rich.basic.objects = new ArrayList<SBean.RichObjectCFGS>();
		String str = sheet.getStringValue(1, 7);
		String[] strs = str.split(",");
		if( strs == null || strs.length != 6 )
			throw new Exception();
		for (int i = 0; i < 6; i ++) {
			short poss = Short.parseShort(strs[i].trim());
			gcfgs.rich.basic.diceposs.add(poss);
		}
		gcfgs.rich.basic.minidiceposs = new ArrayList<Short>();
		str = sheet.getStringValue(1, 9);
		strs = str.split(",");
		if( strs == null || strs.length != 6 )
			throw new Exception();
		for (int i = 0; i < 6; i ++) {
			short poss = Short.parseShort(strs[i].trim());
			gcfgs.rich.basic.minidiceposs.add(poss);
		}
		gcfgs.rich.basic.multiplydiceposs = new ArrayList<Short>();
		str = sheet.getStringValue(3, 11);
		strs = str.split(",");
		if( strs == null || strs.length != 6 )
			throw new Exception();
		for (int i = 0; i < 6; i ++) {
			short poss = Short.parseShort(strs[i].trim());
			gcfgs.rich.basic.multiplydiceposs.add(poss);
		}
		gcfgs.rich.basic.multiplyposs = sheet.getFloatValue(3, 13);
		gcfgs.rich.basic.minigold = new ArrayList<Integer>();
		str = sheet.getStringValue(1, 11);
		strs = str.split(",");
		if( strs == null || strs.length != 6 )
			throw new Exception();
		for (int i = 0; i < 6; i ++) {
			int gold = Integer.parseInt(strs[i].trim());
			gcfgs.rich.basic.minigold.add(gold);
		}
		gcfgs.rich.basic.moneygodgold = sheet.getIntValue(3, 1);
		gcfgs.rich.basic.bigmoneygodgold = sheet.getIntValue(3, 3);
		gcfgs.rich.basic.angelround = sheet.getByteValue(3, 5);
		gcfgs.rich.basic.devilround = sheet.getByteValue(3, 7);
		gcfgs.rich.basic.turtleround = sheet.getByteValue(3, 9);
		gcfgs.rich.basic.beggargold = sheet.getIntValue(5, 1);
		gcfgs.rich.basic.bmwmove = sheet.getShortValue(5, 3);
		gcfgs.rich.basic.loterycost = sheet.getShortValue(5, 5);
		gcfgs.rich.basic.divine1cost = new ArrayList<SBean.RichDivineCostCFGS>();
		str = sheet.getStringValue(5, 7);
		strs = str.split(";");
		for (String s : strs) {
			String [] ss = s.split(",");
			if( ss == null || ss.length != 2 )
				throw new Exception();
			SBean.RichDivineCostCFGS cost = new SBean.RichDivineCostCFGS();
			cost.gold = Integer.parseInt(ss[0]);
			cost.stone = Integer.parseInt(ss[1]);
			gcfgs.rich.basic.divine1cost.add(cost);
		}
		gcfgs.rich.basic.divine2cost = new ArrayList<SBean.RichDivineCostCFGS>();
		str = sheet.getStringValue(5, 9);
		strs = str.split(";");
		for (String s : strs) {
			String [] ss = s.split(",");
			if( ss == null || ss.length != 2 )
				throw new Exception();
			SBean.RichDivineCostCFGS cost = new SBean.RichDivineCostCFGS();
			cost.gold = Integer.parseInt(ss[0]);
			cost.stone = Integer.parseInt(ss[1]);
			gcfgs.rich.basic.divine2cost.add(cost);
		}
		gcfgs.rich.basic.divine3cost = new ArrayList<SBean.RichDivineCostCFGS>();
		str = sheet.getStringValue(5, 11);
		strs = str.split(";");
		for (String s : strs) {
			String [] ss = s.split(",");
			if( ss == null || ss.length != 2 )
				throw new Exception();
			SBean.RichDivineCostCFGS cost = new SBean.RichDivineCostCFGS();
			cost.gold = Integer.parseInt(ss[0]);
			cost.stone = Integer.parseInt(ss[1]);
			gcfgs.rich.basic.divine3cost.add(cost);
		}
		gcfgs.rich.basic.bossTimes = sheet.getByteValue(7, 1);
		gcfgs.rich.basic.bossBase = new ArrayList<SBean.RichBossBaseCFGS>();
		str = sheet.getStringValue(7, 3);
		strs = str.split(";");
		for (String s : strs) {
			String [] ss = s.split(",");
			if( ss == null || ss.length != 2 )
				throw new Exception();
			SBean.RichBossBaseCFGS base = new SBean.RichBossBaseCFGS();
			base.lvl = Byte.parseByte(ss[0]);
			base.base = Integer.parseInt(ss[1]);
			gcfgs.rich.basic.bossBase.add(base);
		}
		gcfgs.rich.basic.bossDamage = sheet.getIntValue(7, 5);
		gcfgs.rich.basic.angelScale = sheet.getFloatValue(7, 7) / 100;
		gcfgs.rich.basic.devilScale = sheet.getFloatValue(7, 9) / 100;
		gcfgs.rich.basic.buyPassGold = sheet.getShortValue(7, 11);
		gcfgs.rich.basic.richBuyPassGold = sheet.getShortValue(7, 13);
		gcfgs.rich.basic.maxmovement = sheet.getShortValue(9, 1);
		gcfgs.rich.basic.divineDropTbl = new ArrayList<Short>();
		gcfgs.rich.basic.divineDropTbl.add(sheet.getShortValue(9, 3));
		gcfgs.rich.basic.divineDropTbl.add(sheet.getShortValue(9, 5));
		gcfgs.rich.basic.divineDropTbl.add(sheet.getShortValue(9, 7));
		gcfgs.rich.basic.loteryWinReward = sheet.getShortValue(9, 11);
		gcfgs.rich.basic.loteryLoseReward = sheet.getShortValue(9, 13);
		gcfgs.rich.basic.buyMovementCost = new ArrayList<Integer>();
		str = sheet.getStringValue(10, 1);
		strs = str.split(";");
		if( strs == null )
			throw new Exception();
		for (String s : strs) {
			int cost = (int)Float.parseFloat(s);
			gcfgs.rich.basic.buyMovementCost.add(cost);
		}
		gcfgs.rich.basic.buyMovementVal = sheet.getIntValue(10, 3);
		gcfgs.rich.basic.bossMaxDamage = sheet.getIntValue(10, 5);
		gcfgs.rich.basic.bossMaxReward = sheet.getIntValue(10, 9);
		gcfgs.rich.basic.reqLevel = sheet.getShortValue(10, 11);
		
		gcfgs.rich.basic.mine = new ArrayList<SBean.RichMineCFGS>();
		int rowMine = locateColTag(tname, sheet, 0, "金矿产量和税收");
		int gain = sheet.getIntValue(rowMine+2, 3);
		int tax = sheet.getIntValue(rowMine+2, 4);
		gcfgs.rich.basic.mine.add(new SBean.RichMineCFGS(gain, tax));
		gain = sheet.getIntValue(rowMine+3, 3);
		tax = sheet.getIntValue(rowMine+3, 4);
		gcfgs.rich.basic.mine.add(new SBean.RichMineCFGS(gain, tax));
		
		gcfgs.rich.techTree = new ArrayList<SBean.RichTechCFG>();
		gcfgs.rich.dailyTask = new ArrayList<SBean.RichDailyTaskCFG>();
		int rowStart = 2; // start row
		int colStart = 0;
		int row = rowStart;
		int col = colStart;
		
		
		rowStart = locateColTag(tname, sheet, 0, "大富翁道具类型相关");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.rich.basic.roadEnventDropTbl = sheet.getShortValue(row++, col);
		gcfgs.rich.basic.remoteDiceMinItemID = sheet.getShortValue(row++, col);
		gcfgs.rich.basic.remoteDiceMaxItemID = sheet.getShortValue(row++, col);
		if( itemIDs.containsKey(gcfgs.rich.basic.remoteDiceMinItemID) && ! itemIDs.containsKey(gcfgs.rich.basic.remoteDiceMaxItemID) )
			throw new Exception(tname + " 遥控骰子道具ID　" + gcfgs.rich.basic.remoteDiceMinItemID + ", "
					+ gcfgs.rich.basic.remoteDiceMaxItemID + " 错误的道具ID ");
		
		rowStart = locateColTag(tname, sheet, 0, "科技树");
		colStart = 0;
		row = rowStart+2;
		col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichTechCFG techTree = new SBean.RichTechCFG();
			techTree.id = sheet.getIntValue(row, col++);
			col += 3;
			techTree.lvlInit = sheet.getIntValue(row, col++);
			techTree.lvlMax = sheet.getIntValue(row, col++);
			techTree.initEffect = sheet.getFloatValue(row, col++);
			techTree.increaseEffect = sheet.getFloatValue(row, col++);
			techTree.lvlupReq = new ArrayList<Integer>();
			for (int i = 0; i < techTree.lvlMax; ++i)
			{
				int req = sheet.getIntValue(row, col++);
				techTree.lvlupReq.add(req);
			}
			gcfgs.rich.techTree.add(techTree);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "天天向上");
		colStart = 0;
		row = rowStart+2;
		col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichDailyTaskCFG dailyTask = new SBean.RichDailyTaskCFG();
			dailyTask.id = sheet.getIntValue(row, col++);
			col += 3;
			dailyTask.type = sheet.getIntValue(row, col++);
			dailyTask.reqArg = sheet.getIntValue(row, col++);
			dailyTask.rewardDropTblID = sheet.getShortValue(row, col++);
			dailyTask.rewardDropTimes = sheet.getShortValue(row, col++);
			if( ! dropTableIDs.contains(dailyTask.rewardDropTblID) )
				throw new Exception(tname + " 天天向上 row " + row + ", dropTblId=" + dailyTask.rewardDropTblID
						+ " 错误的掉落表ID ");
			gcfgs.rich.dailyTask.add(dailyTask);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "道路事件");
		colStart = 0;
		row = rowStart+2;
		col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichObjectCFGS objectcfgs = new SBean.RichObjectCFGS();
			objectcfgs.id = sheet.getIntValue(row, col++);
			col += 4;
			objectcfgs.itemID = sheet.getShortValue(row, col++);
			if( objectcfgs.itemID > 0 && ! itemIDs.containsKey(objectcfgs.itemID) )
				throw new Exception(tname + " 道路事件  row " + row + ", id=" + objectcfgs.id + ", itemID="
						+ objectcfgs.itemID + " 错误的道具ID ");
			gcfgs.rich.basic.objects.add(objectcfgs);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "聚宝盆BOSS");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.rich.bossCombats = new ArrayList<SBean.RichBossCombatCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichBossCombatCFGS combat = new SBean.RichBossCombatCFGS();
			combat.lvlMax = sheet.getShortValue(row, col++);
			combat.combatIds = new ArrayList<Short>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				short combatId = sheet.getShortValue(row, col++);
				combat.combatIds.add(combatId);
			}
			gcfgs.rich.bossCombats.add(combat);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "贫矿金矿山贼");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.rich.mineCombats = new ArrayList<SBean.RichMineCombatCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichMineCombatCFGS combat = new SBean.RichMineCombatCFGS();
			combat.lvlMax = sheet.getShortValue(row, col++);
			combat.combatIds = new ArrayList<Short>();
			combat.rewards = new ArrayList<Integer>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				str = sheet.getStringValue(row, col++);
				strs = str.split(";");
				short combatId = Short.parseShort(strs[0]);
				int reward = Integer.parseInt(strs[1]);
				combat.combatIds.add(combatId);
				combat.rewards.add(reward);
			}
			gcfgs.rich.mineCombats.add(combat);
			row++;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "富矿金矿山贼");
		colStart = 1;
		row = rowStart+3;
		col = colStart;
		gcfgs.rich.richMineCombats = new ArrayList<SBean.RichMineCombatCFGS>();
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichMineCombatCFGS combat = new SBean.RichMineCombatCFGS();
			combat.lvlMax = sheet.getShortValue(row, col++);
			combat.combatIds = new ArrayList<Short>();
			combat.rewards = new ArrayList<Integer>();
			col ++;
			while (sheet.isNotEmpty(row, col))
			{
				str = sheet.getStringValue(row, col++);
				strs = str.split(";");
				short combatId = Short.parseShort(strs[0]);
				int reward = Integer.parseInt(strs[1]);
				combat.combatIds.add(combatId);
				combat.rewards.add(reward);
			}
			gcfgs.rich.richMineCombats.add(combat);
			row++;
			col = colStart;
		}
		
		gcfgs.rich.rewards = new ArrayList<SBean.RichRewardCFGS>();
		final int rowRewards = locateColTag(tname, sheet, 0, "奖励");
		col = 2;
		while( sheet.isNotEmpty(rowRewards+1, col) )
		{
			int rankFloor = sheet.getIntValue(rowRewards+1, col);
			if( rankFloor <= 0 )
				rankFloor = Short.MAX_VALUE;
			int money = sheet.getIntValue(rowRewards+2, col);
			int point = sheet.getIntValue(rowRewards+3, col);
			
			gcfgs.rich.rewards.add(new SBean.RichRewardCFGS(rankFloor, money, point));
			++col;
		}
		
		gcfgs.rich.money = new ArrayList<SBean.RichMoneyCFGS>();
		rowStart = locateColTag(tname, sheet, 0, "直接奖励概率");
		colStart = 1;
		row = rowStart+2;
		col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichMoneyCFGS moneycfgs = new SBean.RichMoneyCFGS();
			moneycfgs.type = sheet.getByteValue(row, col++);
			moneycfgs.count = sheet.getIntValue(row, col++);
			moneycfgs.poss = sheet.getShortValue(row, col++);
			moneycfgs.modelId = sheet.getShortValue(row, col++);
			gcfgs.rich.money.add(moneycfgs);
			row++;
			col = colStart;
		}
		
		gcfgs.rich.box = new ArrayList<SBean.RichBoxCFGS>();
		rowStart = locateColTag(tname, sheet, 0, "套娃宝箱");
		colStart = 2;
		row = rowStart+2;
		col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.RichBoxCFGS boxcfgs = new SBean.RichBoxCFGS();
			boxcfgs.poss = sheet.getFloatValue(row, col++);
			boxcfgs.freePoss = sheet.getFloatValue(row, col++);
			str = sheet.getStringValue(row, col++);
			strs = str.split(",");
			int feeMin = Integer.parseInt(strs[0]);
			int feeMax = Integer.parseInt(strs[1]);
			boxcfgs.feeMin = feeMin;
			boxcfgs.feeMax = feeMax;
			boxcfgs.dropId = sheet.getShortValue(row, col++);
			gcfgs.rich.box.add(boxcfgs);
			row++;
			col = colStart;
		}
		
		System.out.println("load rich end. ");
	}
	
	public void loadInvitation(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		if (gcfgs.invitation == null)
			gcfgs.invitation= new SBean.InvitationCFGS();
		gcfgs.invitation.tasks = new ArrayList<SBean.InvitationTypeTasksCFG>();
		gcfgs.invitation.beInvitedRewards = new ArrayList<SBean.DropEntryNew>();

		int rowStart = 1; // start row
		int colStart = 0;
		int row = rowStart;
		int col = colStart;
		while( sheet.isNotEmpty(row, col) )
		{
			SBean.InvitationTaskCFG task = new SBean.InvitationTaskCFG(); 
			task.id = sheet.getIntValue(row, col++);
			byte taskType = sheet.getByteValue(row, col++);
			byte groupID =  sheet.getByteValue(row, col++);
			col++;
			col++;
			task.reqArg1 = sheet.getIntValue(row, col++);
			task.reqArg2 = sheet.getIntValue(row, col++);
			task.rewardMoney = sheet.getIntValue(row, col++);
			task.rewardStone = sheet.getIntValue(row, col++);
			task.rewardVip = sheet.getIntValue(row, col++);
			task.rewardPoint = sheet.getIntValue(row, col++);
			SBean.InvitationTypeTasksCFG typeTasks = null;
			for (SBean.InvitationTypeTasksCFG e : gcfgs.invitation.tasks)
			{
				if (e.type == taskType)
				{
					typeTasks = e;
					break;
				}
			}
			if (typeTasks == null)
			{
				typeTasks = new SBean.InvitationTypeTasksCFG(taskType, new ArrayList<SBean.InvitationGroupTasksCFG>(), new ArrayList<SBean.InvitationPointsRewardCFG>());
				gcfgs.invitation.tasks.add(typeTasks);
			}
			SBean.InvitationGroupTasksCFG groupTasks = null;
			for (SBean.InvitationGroupTasksCFG e : typeTasks.tasks)
			{
				if (e.groupId == groupID)
				{
					groupTasks = e;
					break;
				}
			}
			if (groupTasks == null)
			{
				groupTasks = new SBean.InvitationGroupTasksCFG(groupID, new ArrayList<InvitationTaskCFG>());
				typeTasks.tasks.add(groupTasks);
			}
			groupTasks.tasks.add(task);
			++row;
			col = colStart;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "积分奖励表");
		colStart = 0;
		row = rowStart+2;
		col = colStart+1;
		while( sheet.isNotEmpty(row, col) )
		{
			byte taskType = sheet.getByteValue(row, col++);
			int rid = sheet.getIntValue(row, col++);
			int pointLvl = sheet.getIntValue(row, col++);
			SBean.InvitationPointsRewardCFG pReward = new SBean.InvitationPointsRewardCFG(rid, pointLvl, new ArrayList<SBean.DropEntryNew>());
			col++;
			for (int i = 0; i < 5; ++i)
			{
				byte dtype = sheet.getByteValue(row, col++);
				if (dtype != -100)
				{
					short did = sheet.getShortValue(row, col++);
					int dcnt = sheet.getIntValue(row, col++);
					switch( dtype )
					{
					case GameData.COMMON_TYPE_ITEM:
						{
							if( ! itemIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的道具ID ");
						}
						break;
					case GameData.COMMON_TYPE_EQUIP:
						{
							if( ! equipIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的装备ID ");
						}
						break;
					case GameData.COMMON_TYPE_GENERAL:
						{
							if( ! generalIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的武将ID ");
						}
						break;
					case GameData.COMMON_TYPE_PET:
						{
							if( ! petIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的宠物ID ");
						}
						break;
					case GameData.COMMON_TYPE_STONE:
					case GameData.COMMON_TYPE_MONEY:
						{
						}
						break;
					case GameData.COMMON_TYPE_NULL:
						break;
					default:
							throw new Exception(tname + " row=" + row + " 错误的掉落类型 " + dtype);
					}
					pReward.reward.add(new SBean.DropEntryNew(dtype, did, dcnt));
				}
				else
				{
					col++;
					col++;
				}
			}
			SBean.InvitationTypeTasksCFG typeTasks = null;
			for (SBean.InvitationTypeTasksCFG e : gcfgs.invitation.tasks)
			{
				if (e.type == taskType)
				{
					typeTasks = e;
					break;
				}
			}
			if (typeTasks == null)
			{
				typeTasks = new SBean.InvitationTypeTasksCFG(taskType, new ArrayList<SBean.InvitationGroupTasksCFG>(), new ArrayList<SBean.InvitationPointsRewardCFG>());
				gcfgs.invitation.tasks.add(typeTasks);
			}
			typeTasks.rewards.add(pReward);
			row++;
			col = colStart+1;
		}
		
		rowStart = locateColTag(tname, sheet, 0, "被邀请相关");
		colStart = 0;
		row = rowStart+2;
		col = colStart+2;
		while( sheet.isNotEmpty(row, col) )
		{
			gcfgs.invitation.inputKeyLvlMin = sheet.getIntValue(row, col++);
			gcfgs.invitation.inputKeyLvlMax = sheet.getIntValue(row, col++);
			gcfgs.invitation.invatationLvlReq = sheet.getIntValue(row, col++);
			for (int i = 0; i < 3; ++i)
			{
				byte dtype = sheet.getByteValue(row, col++);
				if (dtype != -100)
				{
					short did = sheet.getShortValue(row, col++);
					int dcnt = sheet.getIntValue(row, col++);
					switch( dtype )
					{
					case GameData.COMMON_TYPE_ITEM:
						{
							if( ! itemIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的道具ID ");
						}
						break;
					case GameData.COMMON_TYPE_EQUIP:
						{
							if( ! equipIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的装备ID ");
						}
						break;
					case GameData.COMMON_TYPE_GENERAL:
						{
							if( ! generalIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的武将ID ");
						}
						break;
					case GameData.COMMON_TYPE_PET:
						{
							if( ! petIDs.containsKey(did) )
									throw new Exception(tname + " row=" + row + ", id=" + did + " 错误的宠物ID ");
						}
						break;
					case GameData.COMMON_TYPE_STONE:
					case GameData.COMMON_TYPE_MONEY:
						{
						}
						break;
					case GameData.COMMON_TYPE_NULL:
						break;
					default:
							throw new Exception(tname + " row=" + row + " 错误的掉落类型 " + dtype);
					}
					gcfgs.invitation.beInvitedRewards.add(new SBean.DropEntryNew(dtype, did, dcnt));
				}
				else
				{
					col++;
					col++;
				}
			}
			row++;
			col = colStart+2;
		}
		
		System.out.println("load invitation type task count=" + gcfgs.invitation.tasks.size() + ", points level rewards items count=" + gcfgs.invitation.beInvitedRewards.size());
	}

	public void loadGenerals(String tname, String fileNameA, String fileNameB, String fileNameC, SBean.GameDataCFGC gcfgc
			, SBean.GameDataCFGS gcfgs, SBean.GameDataCFGT gcfgt) throws Exception
	{
		ExcelSheet sheetA = ket.moi.Factory.newExcelReader(fileNameA).getSheet(0);
		ExcelSheet sheetB = ket.moi.Factory.newExcelReader(fileNameB).getSheet(0);
		ExcelSheet sheetC = ket.moi.Factory.newExcelReader(fileNameC).getSheet(0);
		List<SBean.GeneralCFGS> lsts = new ArrayList<SBean.GeneralCFGS>();
		gcfgt.generals = new ArrayList<SBean.GeneralName>();
		gcfgs.generals = lsts;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheetA.isNotEmpty(row, 0) )
		{
			SBean.GeneralCFGS cfgs = new SBean.GeneralCFGS();
			cfgs.id = checkIDRange(sheetA.getIntValue(row, 0), tname);
			String name = sheetA.getStringValue(row, 1);
			gcfgt.generals.add(new SBean.GeneralName(cfgs.id, name));
			cfgs.name = name;
			cfgs.type = sheetA.getByteValue(row, 5);
			cfgs.stance = sheetA.getByteValue(row, 51);
			cfgs.equips = new ArrayList<Short>();
			cfgs.initEvoLevel = sheetA.getByteValue(row, 55);
			cfgs.stoneID = sheetA.getShortValue(row, 54);
			cfgs.advPowerUps = new ArrayList<Integer>();
			
			if( cfgs.stoneID > 0 && ! itemIDs.containsKey(cfgs.stoneID) )
				throw new Exception("武将， id = " + cfgs.id + ", 灵魂石ID错误,itemID=" + cfgs.stoneID);
			
			lsts.add(cfgs);
			if( null != generalIDs.put(cfgs.id, cfgs) )
				throw new Exception("武将ID重复， id = " + cfgs.id);
			++row;
		}
		
		row = 2;
		while( sheetB.isNotEmpty(row, 0) )
		{
			short id = checkIDRange(sheetB.getIntValue(row, 0), tname);
			SBean.GeneralCFGS cfgs = generalIDs.get(id);
			if( cfgs == null )
				throw new Exception("武将ID错误， id = " + id);
			final int count = GameData.GENERAL_EQUIP_COUNT * GameData.MAX_GENERAL_ADV_LEVEL;
			for(int i = 0; i < count; ++i)
			{
				cfgs.equips.add(sheetB.getShortValue(row, i+1));
			}
			++row;
		}
		
		row = 2;
		while( sheetC.isNotEmpty(row, 0) )
		{
			short id = checkIDRange(sheetC.getIntValue(row, 1), tname);
			SBean.GeneralCFGS cfgs = generalIDs.get(id);
			if( cfgs == null )
				throw new Exception("武将ID错误， id = " + id);
			cfgs.advPowerUps.add(sheetC.getIntValue(row, 25));
			++row;
		}
	}
	
	public void loadPets(String tname, String fileNameA, String fileNameB, SBean.GameDataCFGC gcfgc
			, SBean.GameDataCFGS gcfgs, SBean.GameDataCFGT gcfgt) throws Exception
	{
		ExcelSheet sheetA = ket.moi.Factory.newExcelReader(fileNameA).getSheet(0);
		ExcelSheet sheetB = ket.moi.Factory.newExcelReader(fileNameB).getSheet(0);
		List<SBean.PetCFGS> lsts = new ArrayList<SBean.PetCFGS>();
		gcfgs.pets = lsts;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheetA.isNotEmpty(row, 0) )
		{
			SBean.PetCFGS cfgs = new SBean.PetCFGS();
			cfgs.id = checkIDRange(sheetA.getIntValue(row, 0), tname);
			cfgs.type = sheetA.getByteValue(row, 1);
			cfgs.gender = sheetA.getByteValue(row, 2);
			cfgs.pieceID = sheetA.getShortValue(row, 44);
			cfgs.pieceMoney = sheetA.getIntValue(row, 45);
			if( sheetA.isNotEmpty(row, 51) )
			{
				String str = sheetA.getStringValue(row, 51);
				String[] v = str.split(";");
				String[] v1 = v[0].split(",");
				cfgs.eggID1 = (short)Float.parseFloat(v1[0]);
				String[] v2 = v[1].split(",");
				cfgs.eggID2 = (short)Float.parseFloat(v2[0]);
				cfgs.proEgg2 = Float.parseFloat(v2[1]);
			}
			cfgs.evo = new ArrayList<SBean.PetEvoCFGS>();
			cfgs.breedMoney = sheetA.getIntValue(row, 52);
			cfgs.breedCool = sheetA.getIntValue(row, 53) * 60;
			cfgs.minGrowthRate = sheetA.getShortValue(row, 54);
			cfgs.maxGrowthRate = sheetA.getShortValue(row, 55);
			if (cfgs.maxGrowthRate > 127)
				throw new Exception("宠物最大成长率太大， id = " + cfgs.id);
			cfgs.propGrowthRate = new ArrayList<Float>();
			{
				List<Integer> wlst = new ArrayList<Integer>();
				int sum = 0;
				for(int i = 0; i < 5; ++i)
				{
					int weight = sheetA.getIntValue(row, 56 + i);
					wlst.add(weight);
					sum += weight;
				}
				float fsum = 0.f;
				for(int w : wlst)
				{
					float p = w/(float)sum;
					fsum += p;
					cfgs.propGrowthRate.add(fsum);
				}
			}
			cfgs.initPower = sheetA.getIntValue(row, 49);
			cfgs.upPower = sheetA.getIntValue(row, 50);
			cfgs.eat = new ArrayList<SBean.PetEatCFGS>();
			{
				int col = 61;
				for (int i = 0; i < 5; i ++)
				{
					SBean.PetEatCFGS e = new SBean.PetEatCFGS();
					e.lvlMax = sheetA.getShortValue(row, col ++);
					e.eatId = sheetA.getShortValue(row, col ++);
					e.productId = sheetA.getShortValue(row, col ++);
					cfgs.eat.add(e);
				}
			}
			cfgs.produceTime = sheetA.getShortValue(row, 76);
			cfgs.productDesc = sheetA.getShortValue(row, 77);
			
			cfgs.deform = new ArrayList<SBean.PetDeformCFGS>();
			int col = 79;
			while( sheetA.isNotEmpty(row, col) )
			{
				SBean.PetDeformCFGS deform = new SBean.PetDeformCFGS();
				deform.type = sheetA.getByteValue(row, col);
				++col;
				deform.growth = sheetA.getShortValue(row, col);
				++col;
				deform.drops = new ArrayList<Short>();
				String str = sheetA.getStringValue(row, col);
				String[] strs = str.split(";");
				for (String s : strs) {
					short drop = (short)Float.parseFloat(s.trim());
					deform.drops.add(drop);
				}
				++col;
				cfgs.deform.add(deform);
				if ( col >= 100 )
					break;
			}
			
			SBean.PetAwakeCFGS awake = new SBean.PetAwakeCFGS();
			col = 100;
			awake.canAwake = sheetA.getByteValue(row, col++);
			awake.awakeSkill1 = sheetA.getShortValue(row, col++);
			awake.awakeSkill2 = sheetA.getShortValue(row, col++);
			awake.awakeSkill3 = sheetA.getShortValue(row, col++);
			awake.awakeSkill4 = sheetA.getShortValue(row, col++);
			awake.awakeSkill5 = sheetA.getShortValue(row, col++);
			awake.needItem1 = new ArrayList<DropEntryNew>();
			awake.needItem2 = new ArrayList<DropEntryNew>();
			awake.needItem3 = new ArrayList<DropEntryNew>();
			awake.needItem4 = new ArrayList<DropEntryNew>();
			awake.needItem5 = new ArrayList<DropEntryNew>();
			String needItem1Str = sheetA.getStringValue(row, col++);
			if (needItem1Str != null && !needItem1Str.equals("")) {
			    String[] strs = needItem1Str.split(";");
			    for (String string : strs) {
			        String[] s = string.split(",");
                    DropEntryNew den = new DropEntryNew();
                    den.id = Short.parseShort(s[0].trim());
                    den.arg = Integer.parseInt(s[1].trim());
                    den.type = GameData.COMMON_TYPE_ITEM;
                    awake.needItem1.add(den);
                }
			}
			
			String needItem2Str = sheetA.getStringValue(row, col++);
			if (needItem2Str != null && !needItem2Str.equals("")) {
			    String[] strs = needItem2Str.split(";");
			    for (String string : strs) {
			        String[] s = string.split(",");
                    DropEntryNew den = new DropEntryNew();
                    den.id = Short.parseShort(s[0].trim());
                    den.arg = Integer.parseInt(s[1].trim());
			        den.type = GameData.COMMON_TYPE_ITEM;
			        awake.needItem2.add(den);
			    }
			}
			
			String needItem3Str = sheetA.getStringValue(row, col++);
			if (needItem3Str != null && !needItem3Str.equals("")) {
			    String[] strs = needItem3Str.split(";");
			    for (String string : strs) {
			        String[] s = string.split(",");
                    DropEntryNew den = new DropEntryNew();
                    den.id = Short.parseShort(s[0].trim());
                    den.arg = Integer.parseInt(s[1].trim());
			        den.type = GameData.COMMON_TYPE_ITEM;
			        awake.needItem3.add(den);
			    }
			}
			
			String needItem4Str = sheetA.getStringValue(row, col++);
			if (needItem4Str != null && !needItem4Str.equals("")) {
			    String[] strs = needItem4Str.split(";");
			    for (String string : strs) {
			        String[] s = string.split(",");
                    DropEntryNew den = new DropEntryNew();
                    den.id = Short.parseShort(s[0].trim());
                    den.arg = Integer.parseInt(s[1].trim());
			        den.type = GameData.COMMON_TYPE_ITEM;
			        awake.needItem4.add(den);
			    }
			}
			
			String needItem5Str = sheetA.getStringValue(row, col++);
			if (needItem5Str != null && !needItem5Str.equals("")) {
			    String[] strs = needItem5Str.split(";");
			    for (String string : strs) {
			        String[] s = string.split(",");
                    DropEntryNew den = new DropEntryNew();
                    den.id = Short.parseShort(s[0].trim());
                    den.arg = Integer.parseInt(s[1].trim());
			        den.type = GameData.COMMON_TYPE_ITEM;
			        awake.needItem5.add(den);
			    }
			}
			cfgs.awake = awake;
			lsts.add(cfgs);
			if( null != petIDs.put(cfgs.id, cfgs) )
				throw new Exception("宠物ID重复， id = " + cfgs.id);
			++row;
		}
		
		String eatCfg = "{";
		for(SBean.PetCFGS cfg : lsts)
		{
			eatCfg += "[" + cfg.id + "]={";
			eatCfg += "produceTime" + cfg.produceTime + ",";
			eatCfg += "productDesc" + cfg.productDesc + ",";
			eatCfg += "},";
		}
		eatCfg += "}";
		luaCfg.put("petEat", eatCfg);
		
		row = 2;
		while( sheetB.isNotEmpty(row, 1) )
		{
			short id = checkIDRange(sheetB.getIntValue(row, 1), tname);
			SBean.PetCFGS cfgs = petIDs.get(id);
			if( cfgs == null )
				throw new Exception("宠物ID错误， id = " + id);
			SBean.PetEvoCFGS c = new SBean.PetEvoCFGS();
			c.pieceCountDec = sheetB.getIntValue(row, 21);
			c.pieceCountEvo = sheetB.getIntValue(row, 22);
			c.power = sheetB.getIntValue(row, 20);
			cfgs.evo.add(c);
			++row;
		}
		
	}
	
	public void loadDeformPets(String tname, String fileNameA, String fileNameB, SBean.GameDataCFGC gcfgc
			, SBean.GameDataCFGS gcfgs, SBean.GameDataCFGT gcfgt) throws Exception
	{
		ExcelSheet sheetA = ket.moi.Factory.newExcelReader(fileNameA).getSheet(0);
		ExcelSheet sheetB = ket.moi.Factory.newExcelReader(fileNameB).getSheet(0);
		List<SBean.PetCFGS> lsts = new ArrayList<SBean.PetCFGS>();
		gcfgs.deformPets = lsts;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheetA.isNotEmpty(row, 0) )
		{
			SBean.PetCFGS cfgs = new SBean.PetCFGS();
			cfgs.id = checkIDRange(sheetA.getIntValue(row, 0), tname);
			cfgs.type = sheetA.getByteValue(row, 1);
			cfgs.gender = sheetA.getByteValue(row, 2);
			cfgs.pieceID = sheetA.getShortValue(row, 44);
			cfgs.pieceMoney = sheetA.getIntValue(row, 45);
			if( sheetA.isNotEmpty(row, 51) )
			{
				String str = sheetA.getStringValue(row, 51);
				String[] v = str.split(";");
				String[] v1 = v[0].split(",");
				cfgs.eggID1 = (short)Float.parseFloat(v1[0]);
				String[] v2 = v[1].split(",");
				cfgs.eggID2 = (short)Float.parseFloat(v2[0]);
				cfgs.proEgg2 = Float.parseFloat(v2[1]);
			}
			cfgs.evo = new ArrayList<SBean.PetEvoCFGS>();
			cfgs.breedMoney = sheetA.getIntValue(row, 52);
			cfgs.breedCool = sheetA.getIntValue(row, 53) * 60;
			cfgs.minGrowthRate = sheetA.getShortValue(row, 54);
			cfgs.maxGrowthRate = sheetA.getShortValue(row, 55);
			cfgs.propGrowthRate = new ArrayList<Float>();
			{
				List<Integer> wlst = new ArrayList<Integer>();
				int sum = 0;
				for(int i = 0; i < 5; ++i)
				{
					int weight = sheetA.getIntValue(row, 56 + i);
					wlst.add(weight);
					sum += weight;
				}
				float fsum = 0.f;
				for(int w : wlst)
				{
					float p = w/(float)sum;
					fsum += p;
					cfgs.propGrowthRate.add(fsum);
				}
			}
			cfgs.initPower = sheetA.getIntValue(row, 49);
			cfgs.upPower = sheetA.getIntValue(row, 50);
			cfgs.eat = new ArrayList<SBean.PetEatCFGS>();
			{
				int col = 61;
				for (int i = 0; i < 5; i ++)
				{
					SBean.PetEatCFGS e = new SBean.PetEatCFGS();
					e.lvlMax = sheetA.getShortValue(row, col ++);
					e.eatId = sheetA.getShortValue(row, col ++);
					e.productId = sheetA.getShortValue(row, col ++);
					cfgs.eat.add(e);
				}
			}
			cfgs.produceTime = sheetA.getShortValue(row, 76);
			cfgs.productDesc = sheetA.getShortValue(row, 77);
			
			cfgs.deform = new ArrayList<SBean.PetDeformCFGS>();
			cfgs.awake = new SBean.PetAwakeCFGS();
		    cfgs.awake.needItem1 = new ArrayList<DropEntryNew>();
            cfgs.awake.needItem2 = new ArrayList<DropEntryNew>();  
            cfgs.awake.needItem3 = new ArrayList<DropEntryNew>();  
            cfgs.awake.needItem4 = new ArrayList<DropEntryNew>();  
            cfgs.awake.needItem5 = new ArrayList<DropEntryNew>();  
			lsts.add(cfgs);
			if( null != deformPetIDs.put(cfgs.id, cfgs) )
				throw new Exception("宠物ID重复， id = " + cfgs.id);
			++row;
		}
		
//		String eatCfg = "{";
//		for(SBean.PetCFGS cfg : lsts)
//		{
//			eatCfg += "[" + cfg.id + "]={";
//			eatCfg += "produceTime" + cfg.produceTime + ",";
//			eatCfg += "productDesc" + cfg.productDesc + ",";
//			eatCfg += "},";
//		}
//		eatCfg += "}";
//		luaCfg.put("deformPetEat", eatCfg);
		
		row = 2;
		while( sheetB.isNotEmpty(row, 1) )
		{
			short id = checkIDRange(sheetB.getIntValue(row, 1), tname);
			SBean.PetCFGS cfgs = deformPetIDs.get(id);
			if( cfgs == null )
				throw new Exception("宠物ID错误， id = " + id);
			SBean.PetEvoCFGS c = new SBean.PetEvoCFGS();
			c.pieceCountDec = sheetB.getIntValue(row, 21);
			c.pieceCountEvo = sheetB.getIntValue(row, 22);
			c.power = sheetB.getIntValue(row, 20);
			cfgs.evo.add(c);
			++row;
		}
		
	}
	
	private SBean.Float3 getFloat3(String str) throws Exception
	{
		String[] strs = str.split(",");
		if( strs == null || strs.length != 3 )
			throw new Exception();
		SBean.Float3 ret = new SBean.Float3();
		ret.x = Float.parseFloat(strs[0].trim());
		ret.y = Float.parseFloat(strs[1].trim());
		ret.z = Float.parseFloat(strs[2].trim());
		return ret;
	}
	
	public Set<String> loadNames(String fileName, int sheetIndex) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(sheetIndex);
		Set<String> lst = new HashSet<String>();
		int row = 1;
		while( sheet.isNotEmpty(row, 1) )
		{
			String s = sheet.getStringValue(row, 1);
			if( sheetIndex == 3 || sheetIndex == 4 )
				s = s.toLowerCase();
			lst.add(s);
			++row;
		}
		return lst;
	}
	
	public SBean.QQDirtyWordCFGS loadQQDirtyWords(String fileName, int sheetIndex) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(sheetIndex);
		SBean.QQDirtyWordCFGS cfgs = new SBean.QQDirtyWordCFGS();
		cfgs.cats = new ArrayList<SBean.QQDirtyWordCatCFGS>();
		cfgs.words = new ArrayList<String>();
		cfgs.wordsICase = new ArrayList<String>();
		//
		Map<String, SBean.QQDirtyWordCatCFGS> cats = new HashMap<String, SBean.QQDirtyWordCatCFGS>();
		//
		int row = 1;
		while( sheet.isNotEmpty(row, 1) )
		{
			int l = sheet.getIntValue(row, 2);
			String s = sheet.getStringValue(row, 1).trim();
			//System.out.println(s);
			if( s.isEmpty() )
			{
				++row;
				continue;
			}
			// TODO
			if( l != 1 )
			{
				if( l == 2 )
					cfgs.wordsICase.add(s);
				else
					cfgs.words.add(s);
			}
			else
			{
				String[] v = s.split("\\|");
				if( v.length == 0 || v.length > 2 )
				{
					++row;
					continue;
				}
				if( v.length == 1 )
					cfgs.words.add(s);
				else // length = 2
				{
					String key = v[1].trim();
					s = v[0].trim();
					if( key.isEmpty() || s.isEmpty() )
					{
						++row;
						continue;
					}
					SBean.QQDirtyWordCatCFGS cat = cats.get(key);
					if( cat == null )
					{
						cat = new SBean.QQDirtyWordCatCFGS(key, new ArrayList<String>());
						cats.put(key, cat);
					}
					cat.words.add(s);
				}
			}
			//
			++row;
		}
		cfgs.cats.addAll(cats.values());
		System.out.println("load qq dirty cats " + cfgs.cats.size());
		System.out.println("load qq dirty words " + cfgs.words.size() + ", " + cfgs.wordsICase.size());
		return cfgs;
	}
	
	public SBean.RandomNameCFG loadRandomNames(String fileName) throws Exception
	{
		SBean.RandomNameCFG cfg = new SBean.RandomNameCFG();
		Set<String> l = loadNames(fileName, 0);
		Set<String> f = loadNames(fileName, 2);
		Set<String> m = loadNames(fileName, 1);
		cfg.robotNames = new ArrayList<String>();
		cfg.lastNames = new ArrayList<String>();
		cfg.fnames = new ArrayList<String>();
		cfg.mnames = new ArrayList<String>();
		for(String e : l)
		{
			cfg.lastNames.add(e);
		}
		for(String e : f)
		{
			cfg.fnames.add(e);
		}
		for(String e : m)
		{
			cfg.mnames.add(e);
		}
		final int ROBOT_NAME_COUNT = 2000;
		Random rand = new Random();
		rand.setSeed(0);
		while( cfg.robotNames.size() < ROBOT_NAME_COUNT )
		{
			String n = "";
			String el = cfg.lastNames.get(rand.nextInt(cfg.lastNames.size()));
			if( rand.nextBoolean() )
				n = el + cfg.fnames.get(rand.nextInt(cfg.fnames.size()));
			else
				n = el + cfg.mnames.get(rand.nextInt(cfg.mnames.size()));
			cfg.robotNames.add(n);
		}
		//Collections.shuffle(cfg.robotNames);
		System.out.println("load random names " + cfg.lastNames.size() + ", " + cfg.mnames.size() 
				+ ", " + cfg.fnames.size() + ", " + cfg.robotNames.size());
		return cfg;
	}
	
	public List<SBean.LoadingCFG> loadLoadingTips(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.LoadingCFG> lst = new ArrayList<SBean.LoadingCFG>();
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.LoadingCFG cfg = new SBean.LoadingCFG();
			cfg.msg = sheet.getStringValue(row, 1);
			cfg.globalProp = sheet.getFloatValue(row, 2);
			lst.add(cfg);
			++row;
		}
		String loadingCfg = "{";
		float totalPoss = 0;
		for(SBean.LoadingCFG cfg : lst)
		{
			totalPoss += cfg.globalProp;
			loadingCfg += "{msg=\"" + cfg.msg + "\", poss=" + cfg.globalProp + "}, ";
		}
		loadingCfg += "totalPoss=" + totalPoss + "}";
		luaCfg.put("loadingtips", loadingCfg);
		return lst;
	}
	
	public List<SBean.CombatMapCFG> loadCombatMaps(String tname, String fileName) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.CombatMapCFG> lst = new ArrayList<SBean.CombatMapCFG>();
		final int colStart = 0; // start col
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, colStart) )
		{
			SBean.CombatMapCFG cfg = new SBean.CombatMapCFG();
			cfg.id = checkIDRange(sheet.getIntValue(row, colStart), tname);
			cfg.name = sheet.getStringValue(row, colStart + 1);
			cfg.path = sheet.getStringValue(row, colStart + 2);
			lst.add(cfg);
			if( ! combatMapIDs.add(cfg.id) )
				throw new Exception("战役地图ID重复， id = " + cfg.id);
			++row;
		}
		// TODO dump
		for(SBean.CombatMapCFG cfg : lst)
		{
			System.out.println("combatmap id=" + cfg.id + ", name=" + cfg.name + ", path=" + cfg.path);
			if( checkPS != null )
				checkPS.println("battle map : [" + cfg.name + "]");
		}
		//
		return lst;
	}
	
	public void loadCombatCats(String tname, String fileNameA, String fileNameB, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheetA = ket.moi.Factory.newExcelReader(fileNameA).getSheet(0);
		ExcelSheet sheetB = ket.moi.Factory.newExcelReader(fileNameB).getSheet(0);
		Map<Byte, SBean.CombatCatCFG> mapCat = new TreeMap<Byte, SBean.CombatCatCFG>();
		int row = 1;
		while( sheetA.isNotEmpty(row, 0) )
		{
			SBean.CombatCatCFG cfg = new SBean.CombatCatCFG();
			cfg.id = sheetA.getByteValue(row, 0);
			if( mapCat.containsKey(cfg.id) )
				throw new Exception("战役分区表ID重复， id = " + cfg.id);
			combatCatIDs.add(cfg.id);
			if( cfg.id != row )
				throw new Exception("战役分区表ID必须严格增序");
			cfg.name = sheetA.getStringValue(row, 1);
			cfg.mapIconID = sheetA.getShortValue(row, 2);
			if( ! iconIDs.contains(cfg.mapIconID) )
				throw new Exception("战役分区表引用图标ID不存在， icon id = " + cfg.mapIconID);
			cfg.mapCityIconID = sheetA.getShortValue(row, 3);
			if( ! iconIDs.contains(cfg.mapCityIconID) )
				throw new Exception("战役分区表引用图标ID不存在， icon id = " + cfg.mapCityIconID);
			cfg.mapCityDisableIconID = sheetA.getShortValue(row, 4);
			if( ! iconIDs.contains(cfg.mapCityDisableIconID) )
				throw new Exception("战役分区表引用图标ID不存在， icon id = " + cfg.mapCityDisableIconID);
			cfg.combats = new ArrayList<SBean.CombatCatEntryListCFG>();
			cfg.combats.add(new SBean.CombatCatEntryListCFG(new ArrayList<SBean.CombatCatEntryCFG>()));
			cfg.combats.add(new SBean.CombatCatEntryListCFG(new ArrayList<SBean.CombatCatEntryCFG>()));
			mapCat.put(cfg.id, cfg);
			++row;
		}
		row = 1;
		Set<Short> cids = new TreeSet<Short>();
		while( sheetB.isNotEmpty(row, 0) )
		{
			short id = sheetB.getShortValue(row, 0);
			byte catid = sheetB.getByteValue(row, 3);
			SBean.CombatCatCFG cfg = mapCat.get(catid);
			if( cfg == null )
				throw new Exception("战役分区表ID不存在， id = " + catid + ", 排列表 id = " + id);
			short cid = sheetB.getShortValue(row, 4);
			if( ! combatIDs.contains(cid) )
				throw new Exception("战役表ID不存在， id = " + cid + ", 排列表 id = " + id);
			if( cids.contains(cid) )
				throw new Exception("战役表ID重复， id = " + cid + ", 排列表 id = " + id);
			cids.add(cid);
			byte type = sheetB.getByteValue(row, 2);
			if( type < 0 || type >= cfg.combats.size() )
				throw new Exception("战役表类型错误， type = " + type + ", 排列表 id = " + id);
			SBean.CombatCatEntryListCFG lcfg = cfg.combats.get(type);
			byte seq = sheetB.getByteValue(row, 5);
			SBean.CombatCatEntryCFG ecfg = new SBean.CombatCatEntryCFG(cid, seq);
			lcfg.combats.add(ecfg);
			++row;
		}
		for(SBean.CombatCatCFG cfg : mapCat.values())
		{
			for(SBean.CombatCatEntryListCFG lcfg : cfg.combats)
			{
				List<SBean.CombatCatEntryCFG> lst = lcfg.combats;
				Map<Byte, SBean.CombatCatEntryCFG> seqMap = new TreeMap<Byte, SBean.CombatCatEntryCFG>();
				for(SBean.CombatCatEntryCFG ecfg : lst)
				{
					if( seqMap.containsKey(ecfg.seq ) )
						throw new Exception("战役分区表战役顺序重复， 战役区id = " + cfg.id + ", 战役id = " + ecfg.cid);
					seqMap.put(ecfg.seq, ecfg);
				}
				lcfg.combats = new ArrayList<SBean.CombatCatEntryCFG>(lst);
			}
		}
		// TODO
		gcfgc.combatcats = new ArrayList<SBean.CombatCatCFG>(mapCat.values());
		gcfgs.combatcats = gcfgc.combatcats;
	}
	
	public void loadCombats(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.CombatCFGS> server_lst = new ArrayList<SBean.CombatCFGS>();
		gcfgs.combats = server_lst;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.CombatCFGS cfgs = new SBean.CombatCFGS();
			cfgs.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfgs.monsterWaveCnt = sheet.getByteValue(row, 2);
			cfgs.lvlReq = sheet.getShortValue(row, 3);
			cfgs.fightPower = sheet.getIntValue(row, 4);
			cfgs.immune = sheet.getByteValue(row, 9);
			for (int col = 15; col < 30; ++col)
			{
				if (sheet.isNotEmpty(row, col))
				{
					String str = sheet.getStringValue(row, col);
					String[] gstr = str.split(",");
					short gid = Short.parseShort(gstr[0]);
					SBean.GeneralCFGS generalCfg = null;
					for (SBean.GeneralCFGS gcfg : gcfgs.generals)
					{
						if (gcfg.id == gid)
						{
							generalCfg = gcfg;
							break;
						}
					}
					if (gstr.length != 3 || generalCfg == null)
						throw new Exception(tname + " 战役 " + cfgs.id + ", (" + row + "," + col + ")武将配置错误 ");
					cfgs.monsterCnt++;
				}
			}
			cfgs.maxTimes = sheet.getByteValue(row, 30);
			cfgs.vitCostStart = sheet.getShortValue(row, 31);
			cfgs.vitCostWin = sheet.getShortValue(row, 32);
			cfgs.dropExp = sheet.getIntValue(row, 33);
			cfgs.dropGeneralExp = sheet.getIntValue(row, 34);
			cfgs.dropMoney = sheet.getIntValue(row, 35);
			cfgs.dropStoneTimes = sheet.getByteValue(row, 45);
			cfgs.dropStone = sheet.getShortValue(row, 46);
			cfgs.dropTimes = sheet.getByteValue(row, 42);
			cfgs.dropTableID = sheet.getShortValue(row, 43);
			cfgs.type = sheet.getByteValue(row, 44);
			{
				String s = sheet.getStringValue(row, 41);
				String[] v = s.split(",");
				if( v.length != 2 || Byte.parseByte(v[0].trim()) != 0 )
					throw new Exception(tname + " 战役 " + cfgs.id + ", 额外掉落错误 ");
				short stoneID = Short.parseShort(v[1].trim());
				if( stoneID > 0 && ! itemIDs.containsKey(stoneID) )
					throw new Exception(tname + " 战役 " + cfgs.id + ", 额外掉落错误 ");
				cfgs.dropStoneID = stoneID;
			}
			
			if( cfgs.dropTableID > 0 && ! dropTableIDs.contains(cfgs.dropTableID) )
				throw new Exception(tname + " 战役 " + cfgs.id + ", 掉落表ID " + cfgs.dropTableID + " 错误");
			cfgs.dropFixed = new ArrayList<SBean.DropEntry>();
			if( cfgs.dropTableID <= 0 )
			{
				for(int i = 0; i < 5; ++i)
				{
					String str = sheet.getStringValue(row, 36+i);
					String[] v = str.split(",");
					byte t = (byte)Float.parseFloat(v[0].trim());
					if( v.length < 2 )
					{
						throw new Exception("数组格式错误  " + (row + 1) + " 行");
					}
					short id = (short)Float.parseFloat(v[1].trim());
					byte cnt = 1;
					if( v.length > 2 )
					{
						cnt = (byte)Float.parseFloat(v[2].trim());
					}
					if( id > 0 && (t == GameData.COMMON_TYPE_ITEM || t == GameData.COMMON_TYPE_EQUIP) && cnt >=1 && cnt <= 100)
					{
						cfgs.dropFixed.add(new SBean.DropEntry(t, cnt, id));
					}
				}
			}
			server_lst.add(cfgs);
			if( ! combatIDs.add(cfgs.id) )
				throw new Exception("战役ID重复， id = " + cfgs.id);
			++row;
		}
		System.out.println("combats count: " + server_lst.size());
	}
	
	public void loadCombatEvents(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.CombatEventCFGS> lst = new ArrayList<SBean.CombatEventCFGS>();
		gcfgs.combatEvents = lst;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.CombatEventCFGS cfgs = new SBean.CombatEventCFGS();
			cfgs.id = sheet.getByteValue(row, 0);
			cfgs.lvlReq = sheet.getShortValue(row, 2);
			cfgs.maxTimes = sheet.getByteValue(row, 4);
			cfgs.cool = sheet.getShortValue(row, 5);
			cfgs.weekdays = new ArrayList<Byte>();
			String swd = sheet.getStringValue(row, 3);
			String[] v = swd.split(";");
			for(String s : v)
			{
				byte wd = (byte)(Byte.parseByte(s)%7);
				cfgs.weekdays.add(wd);
			}
			cfgs.combatIDs = new ArrayList<Short>();
			int n = sheet.getShortValue(row, 7);
			for(int i = 0; i < n; ++i)
			{
				short cid = sheet.getShortValue(row, 8 + 2*i);
				if( ! combatIDs.contains(cid) )
					throw new Exception(tname + " 战役ID不存在， id = " + cid);
				cfgs.combatIDs.add(cid);
			}
			
			lst.add(cfgs);
			++row;
		}
		System.out.println("combat events count: " + lst.size());
	}
	
	public void loadCheckin(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		gcfgs.checkin = new ArrayList<SBean.CheckinCFGS>();
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.CheckinCFGS cfgs = new SBean.CheckinCFGS();
			cfgs.id = sheet.getByteValue(row, 0);
			cfgs.timeStart = StringConverter.parseDayOfYear(sheet.getStringValue(row, 1));
			cfgs.rewards = new ArrayList<SBean.CheckinRewardCFGS>();
			int col = 4;
			while( sheet.isNotEmpty(row, col) )
			{
				String str = sheet.getStringValue(row, col);
				String[] v = str.split(",");
				SBean.DropEntryNew d = new SBean.DropEntryNew();
				d.type = (byte)Float.parseFloat(v[0]);
				d.id = (short)Float.parseFloat(v[1]);
				d.arg = (int)Float.parseFloat(v[2]);
				SBean.CheckinRewardCFGS r = new SBean.CheckinRewardCFGS();
				r.reward = d;
				r.lvlVIP = 0;
				if( v.length > 3 )
				{
					r.lvlVIP = (byte)Float.parseFloat(v[3]);
				}
				cfgs.rewards.add(r);
				++col;
			}			
			
			gcfgs.checkin.add(cfgs);
			++row;
		}
		System.out.println("checkin month count: " + gcfgs.checkin.size());
	}
	
	public void loadChannels(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.ChannelCFGS> lst = new ArrayList<SBean.ChannelCFGS>();
		gcfgs.channels = lst;
		final int rowStart = 1; // start row
		int row = rowStart;
		Set<String> codes = new HashSet<String>();
		while( sheet.isNotEmpty(row, 0) )
		{
			short pid = sheet.getShortValue(row, 3);
			String name = sheet.getStringValue(row, 1);
			if( codes.contains(name) )
				throw new Exception(tname + " name重复 " + name + " 不正确");
			codes.add(name);
			lst.add(new SBean.ChannelCFGS(pid, name));
			++row;
		}
	}
	
	public void loadPay(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.PayCFGS> lst = new ArrayList<SBean.PayCFGS>();
		gcfgs.pay = lst;
		final int rowStart = 2; // start row
		int row = rowStart;;
		while( sheet.isNotEmpty(row, 0) )
		{
			short cid = sheet.getShortValue(row, 0);
			SBean.PayCFGS cfg = new SBean.PayCFGS();
			cfg.levels = new ArrayList<SBean.PayCFGLevelS>();
			cfg.cid = cid;
			cfg.flag = sheet.getByteValue(row, 2);
			cfg.ceil = sheet.getIntValue(row, 3);
			cfg.floor = sheet.getIntValue(row, 4);
			final int MAX_LEVEL = 6;
			for(int i = 0; i < MAX_LEVEL; ++i)
			{
				int rmb = sheet.getIntValue(row, 5 + i*2);
				int stone = sheet.getIntValue(row, 5 + i*2 + 1);
				cfg.levels.add(new SBean.PayCFGLevelS(rmb, stone));
			}
			lst.add(cfg);
			++row;
		}
	}
	
	public void loadVIP(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.VIPCFGS> lst = new ArrayList<SBean.VIPCFGS>();
		gcfgs.vip = lst;
		int row = 2;
		while( sheet.isNotEmpty(row, 0) )
		{
			byte lvl = sheet.getByteValue(row, 0);
			if( lvl != row-2 )
				throw new Exception(tname + " vip 等级错误");
			SBean.VIPCFGS cfg = new SBean.VIPCFGS();
			cfg.lvl = lvl;
			cfg.canSaodang1 = sheet.getByteValue(row, 3);
			cfg.canSaodang10 = sheet.getByteValue(row, 5);
			cfg.resetCombatTimes = sheet.getByteValue(row, 6);
			cfg.canSummonShopRand1 = sheet.getByteValue(row, 9);
			cfg.canSummonShopRand2 = sheet.getByteValue(row, 10);
			cfg.buyMoneyTimes = sheet.getShortValue(row, 12);
			cfg.buySkillPointTimes = sheet.getShortValue(row, 7);
			cfg.buyVitTimes = sheet.getShortValue(row, 11);
			cfg.mobaiTimes = sheet.getByteValue(row, 16);
			cfg.skillPointMax = sheet.getByteValue(row, 17);
			cfg.stoneReq = sheet.getIntValue(row, 2);
			cfg.buyArenaTimes = sheet.getShortValue(row, 14);
			cfg.resetMarchTimes = sheet.getShortValue(row, 13);
			cfg.superMobaiTimes = sheet.getByteValue(row, 15);
			cfg.marchRewardUp = sheet.getByteValue(row, 18);
			cfg.canBuySoulBox = sheet.getByteValue(row, 20);
			cfg.canOccupyCityCount = sheet.getByteValue(row, 22);
			cfg.petZanOfferCount = sheet.getShortValue(row, 24);
			cfg.mercCountMax = sheet.getByteValue(row, 25);	
			lst.add(cfg);
			++row;
		}
		// TODO
		System.out.println("vip count: " + lst.size());
	}
	
	public void loadPackages(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.PackageCFGC> lst = new ArrayList<SBean.PackageCFGC>();
		List<SBean.PackageCFGS> server_lst = new ArrayList<SBean.PackageCFGS>();
		gcfgc.packages = lst;
		gcfgs.packages = server_lst;
		final int rowStart = 2; // start row
		int row = rowStart;
		Map<Short, SBean.PackageCFGS> sets = new TreeMap<Short, SBean.PackageCFGS>();
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.PackageCFGC cfgc = new SBean.PackageCFGC();
			SBean.PackageCFGS cfgs = new SBean.PackageCFGS();
			cfgs.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfgc.cfgs = cfgs;
			cfgc.name = sheet.getStringValue(row, 1);
			cfgc.des = sheet.getStringValue(row, 2);
			cfgs.money = sheet.getIntValue(row, 3);
			cfgs.stone = sheet.getIntValue(row, 4);
			cfgs.generalLevel = sheet.getByteValue(row, 5);
			cfgs.lvlReq = sheet.getByteValue(row, 6);
			
			cfgs.dayRewards = new ArrayList<SBean.DayReward>();
			final int REWARD_COUNT = 5;
			final int REWARD_WIDTH = 3;
			for(int i = 0; i < REWARD_COUNT; ++i)
			{
				byte rtype = sheet.getByteValue(row, 7 + i * REWARD_WIDTH);
				short rid = sheet.getShortValue(row, 7 + i * REWARD_WIDTH + 1);
				byte rcnt = sheet.getByteValue(row, 7 + i * REWARD_WIDTH + 2);
				if( rtype < 0 ) 
					continue;
				switch( rtype )
				{
				case SBean.DayReward.eTypeItem:
					{
						if( rcnt <= 0 || ! itemIDs.containsKey(rid) )
								throw new Exception(tname + " 道具ID  " + rid + " 不正确");
					}
					break;
				case SBean.DayReward.eTypeEquip:
					{
						if( rcnt <= 0 || ! equipIDs.containsKey(rid) )
								throw new Exception(tname + " 装备ID  " + rid + " 不正确");
					}
					break;
				case SBean.DayReward.eTypeGeneral:
					{
						if( rcnt <= 0 || ! generalIDs.containsKey(rid) )
								throw new Exception(tname + " 武将ID  " + rid + " 不正确");
					}
					break;
				default:
						throw new Exception(tname + " 类型 " + rtype + " 不正确");
				}
				cfgs.dayRewards.add(new SBean.DayReward(rtype, rcnt, rid));
			}
			
			lst.add(cfgc);
			server_lst.add(cfgs);
			if( sets.containsKey(cfgs.id) )
				throw new Exception(tname + " ID重复 " + cfgs.id + " 不正确");
			sets.put(cfgs.id, cfgs);
			packIDs.add(cfgs.id);
			++row;
		}
		// TODO dump
		System.out.println("packages count: " + lst.size());
	}
	
	public void loadCmnRewards(String tname, String fileName, SBean.GameDataCFGC gcfgc, SBean.GameDataCFGS gcfgs) throws Exception
	{
		ExcelSheet sheet = ket.moi.Factory.newExcelReader(fileName).getSheet(0);
		List<SBean.CmnRewardCFGS> lst = new ArrayList<SBean.CmnRewardCFGS>();
		gcfgs.cmnrewards = lst;
		final int rowStart = 2; // start row
		int row = rowStart;
		while( sheet.isNotEmpty(row, 0) )
		{
			SBean.CmnRewardCFGS cfgs = new SBean.CmnRewardCFGS();
			cfgs.id = checkIDRange(sheet.getIntValue(row, 0), tname);
			cfgs.exp = sheet.getIntValue(row, 2);
			cfgs.stone = sheet.getIntValue(row, 3);
			cfgs.money = sheet.getIntValue(row, 4);
			cfgs.generalLevel = sheet.getByteValue(row, 5);
			
			cfgs.rewards = new ArrayList<SBean.DropEntry>();
			final int REWARD_COUNT = 5;
			final int REWARD_WIDTH = 3;
			for(int i = 0; i < REWARD_COUNT; ++i)
			{
				byte rtype = sheet.getByteValue(row, 6 + i * REWARD_WIDTH);
				short rid = sheet.getShortValue(row, 6 + i * REWARD_WIDTH + 1);
				byte rcnt = sheet.getByteValue(row, 6 + i * REWARD_WIDTH + 2);
				if( rtype < 0 )
					continue;
				switch( rtype )
				{
				case GameData.COMMON_TYPE_ITEM:
					{
						if( rcnt <= 0 || ! itemIDs.containsKey(rid) )
								throw new Exception(tname + " 道具ID  " + rid + " 不正确");
					}
					break;
				case GameData.COMMON_TYPE_EQUIP:
					{
						if( rcnt <= 0 || ! equipIDs.containsKey(rid) )
								throw new Exception(tname + " 装备ID  " + rid + " 不正确");
					}
					break;
				case GameData.COMMON_TYPE_GENERAL:
					{
						if( rcnt <= 0 || ! generalIDs.containsKey(rid) )
								throw new Exception(tname + " 武将ID  " + rid + " 不正确");
					}
					break;
				default:
						throw new Exception(tname + " 类型 " + rtype + " 不正确");
				}
				cfgs.rewards.add(new SBean.DropEntry(rtype, rcnt, rid));
			}
			
			lst.add(cfgs);
			if( cmnRewardIDs.contains(cfgs.id) )
				throw new Exception(tname + " ID重复 " + cfgs.id + " 不正确");
			cmnRewardIDs.add(cfgs.id);
			++row;
		}
		// TODO dump
		System.out.println("cmnrewards count: " + lst.size());
	}

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		String srcdir = am.get("-srcdir", ".");
		String dstdir = am.get("-dstdir", ".");
		String lua_dstdir = am.get("-luadstdir", ".");
		String checkFileName = am.get("-ckFileOld", null);
		String checkFileNameNew = am.get("-ckFile", null);
		String server_dstdir = am.get("-server_dstdir", ".");
		DataTool tool = new DataTool();
		SBean.GameDataCFGC cfgc = new SBean.GameDataCFGC();
		SBean.GameDataCFGS cfgs = new SBean.GameDataCFGS();
		SBean.GameDataCFGT cfgt = new SBean.GameDataCFGT();
		SBean.QQDirtyWordCFGS cfgd = new SBean.QQDirtyWordCFGS();
		try
		{
			if( checkFileName != null )
				tool.checkPS = new PrintStream(checkFileName);
			cfgc.icons = tool.loadIcons("ICON表", srcdir + "/图片ICON资源表.xlsx");
			cfgc.dlgtables = tool.loadDlgTables("对白库", srcdir + "/对白库.xlsx");
			cfgc.dlgtablesNew = tool.loadDlgTablesNew("新对白库", srcdir + "/新对白库.xlsx");
			cfgc.uiEffects = tool.loadUIEffects("界面特效表", srcdir + "/界面特效表.xlsx");
			cfgc.tips = tool.loadTips("图文提示表", srcdir + "/图文提示表.xlsx");
			cfgc.artEffects = tool.loadArtEffects("特效表", srcdir + "/特效表.xlsx");
			cfgc.fightEffects = tool.loadFightEffects(srcdir + "/状态效果ID表.xlsx");
			tool.loadItems("道具表", srcdir + "/道具表.xlsx", cfgc, cfgs, cfgt);
			tool.loadGenerals("武将表", srcdir + "/武将表.xlsx", srcdir + "/武将进阶表.xlsx", srcdir + "/武将进阶属性表.xlsx", cfgc,
					cfgs, cfgt);
			tool.loadEquips("装备表", srcdir + "/装备表.xlsx", cfgc, cfgs, cfgt);
			tool.loadPets("宠物表", srcdir + "/宠物表.xlsx", srcdir + "/宠物进阶属性表.xlsx", cfgc, cfgs, cfgt);
			tool.loadDeformPets("宠物变异表", srcdir + "/宠物变异表.xlsx", srcdir + "/宠物变异进阶属性表.xlsx", cfgc, cfgs, cfgt);
			cfgs.droptables = tool.loadDropTables("掉落表", srcdir + "/掉落表.xlsx", cfgs);
			cfgc.combatmaps = tool.loadCombatMaps("战役地图表", srcdir + "/战役地图表.xlsx");
			tool.loadCombats("战役表", srcdir + "/战役表.xlsx", cfgc, cfgs);
			tool.loadCombatEvents("战役活动表", srcdir + "/战役活动表.xlsx", cfgc, cfgs);
			tool.loadCmnRewards("活动通用奖励表", srcdir + "/活动通用奖励表.xlsx", cfgc, cfgs);
			tool.loadVIP("VIP表", srcdir + "/VIP表.xlsx", cfgc, cfgs);
			tool.loadPackages("礼包表", srcdir + "/礼包表.xlsx", cfgc, cfgs);
			tool.loadCombatCats("战役目录表", srcdir + "/大战役分页表.xlsx", srcdir + "/战役表排列.xlsx", cfgc, cfgs);
			tool.loadMainTable("主配置表", srcdir + "/主配置表.xlsx", cfgc, cfgs);
			tool.loadMainTableNew("通用配置表", srcdir + "/通用配置表.xlsx", cfgc, cfgs);
			tool.loadGeneralSkillUpgradeTable("技能升级消耗", srcdir + "/技能升级消耗.xlsx", cfgc, cfgs);
			tool.loadGeneralUpgradeTable("武将升级经验表", srcdir + "/武将升级经验表.xlsx", cfgc, cfgs);
			tool.loadPetUpgradeTable("宠物升级经验表", srcdir + "/宠物升级经验表.xlsx", cfgc, cfgs);
			tool.loadUpgradeTable("君主等级经验", srcdir + "/君主等级经验.xlsx", cfgc, cfgs);
			tool.loadSaleTable("运营活动表", srcdir + "/运营活动表.xlsx", cfgc, cfgs);
			tool.loadCheckin("签到表", srcdir + "/签到表.xlsx", cfgc, cfgs);
			cfgc.randomNames = tool.loadRandomNames(srcdir + "/君主姓名随机.xlsx");
			cfgs.badnames = new ArrayList<String>();
			cfgs.badnames2 = new ArrayList<String>();
			cfgs.badnames.addAll(tool.loadNames(srcdir + "/君主姓名随机.xlsx", 3));
			cfgs.badnames2.addAll(tool.loadNames(srcdir + "/君主姓名随机.xlsx", 4));
			cfgs.qqDirtyWords = tool.loadQQDirtyWords(srcdir + "/君主姓名随机.xlsx", 5);
			cfgd = cfgs.qqDirtyWords;
			// TODO
			cfgs.badnames.clear();
			cfgs.badnames.addAll(cfgs.qqDirtyWords.words);
			for(SBean.QQDirtyWordCatCFGS e : cfgs.qqDirtyWords.cats)
			{
				cfgs.badnames.addAll(e.words);	
			}
			//
			tool.loadTasks("任务表", srcdir + "/任务表.xlsx", cfgc, cfgs);
			cfgc.loadingTips = tool.loadLoadingTips("加载提示表", srcdir + "/加载提示表.xlsx");
			tool.loadEggs("抽卡表", srcdir + "/抽卡表.xlsx", cfgc, cfgs);
			tool.loadSoulBoxs("魂匣表", srcdir + "/魂匣表.xlsx", cfgc, cfgs);
			tool.loadCaptureCity("夺城战", srcdir + "/夺城战.xlsx", cfgc, cfgs);
			tool.loadForceBattles("国战表", srcdir + "/国战.xlsx", cfgc, cfgs);
			tool.loadRich("大富翁", srcdir + "/大富翁.xlsx", cfgc, cfgs);
			tool.loadInvitation("推广系统", srcdir + "/推广系统.xlsx", cfgc, cfgs);
			tool.loadArena("竞技场", srcdir + "/竞技场.xlsx", cfgc, cfgs);
			tool.loadMarch("燃烧远征", srcdir + "/燃烧远征.xlsx", cfgc, cfgs);
			tool.loadGeneralPower("战力值配置表", srcdir + "/战力值配置表.xlsx", cfgc, cfgs);
			tool.loadWeapon("神兵表", srcdir + "/神兵表.xlsx", cfgc, cfgs);
			tool.loadShop("商城表", srcdir + "/商城表.xlsx", cfgc, cfgs);
			tool.loadPay("充值表", srcdir + "/充值表.xlsx", cfgc, cfgs);
			tool.loadChannels("渠道表", srcdir + "/渠道表.xlsx", cfgc, cfgs);
			;
			tool.loadDungeon("国家副本", srcdir + "/国家副本.xlsx", cfgc, cfgs);
			tool.loadRelation("武将缘分", srcdir + "/武将缘分.xlsx", cfgc, cfgs);
			tool.loadGeneralStone("天命魂石", srcdir + "/天命魂石.xlsx", cfgc, cfgs);
			tool.loadGeneralSeyen("武将精元", srcdir + "/武将精元.xlsx", cfgc, cfgs);
			tool.loadDuel("决斗场", srcdir + "/决斗场.xlsx", cfgc, cfgs);
			tool.loadSura("修罗", srcdir + "/修罗.xlsx", cfgc, cfgs);
			tool.loadExpiration("转服表", srcdir + "/转服表.xlsx", cfgc, cfgs);
			tool.loadExpiratBoss("跨服BOSS", srcdir + "/跨服BOSS.xlsx", cfgc, cfgs);
			tool.loadSWar("跨服国战", srcdir + "/跨服国战.xlsx", cfgc, cfgs);
			tool.loadOfficial("武将官职武技", srcdir + "/武将官职武技.xlsx", cfgc, cfgs);
			tool.loadHeroes("群英会", srcdir + "/群英会.xlsx", cfgc, cfgs);
			tool.loadHeadicon("头像边框属性表", srcdir + "/头像边框属性表.xlsx", cfgc, cfgs);
			tool.loadDiskBet("轮盘活动", srcdir + "/轮盘活动.xlsx", cfgc, cfgs);
			tool.loadBestow("封赏", srcdir + "/封赏.xlsx", cfgc, cfgs);
			tool.loadBless("祈福上香", srcdir + "/祈福上香.xlsx", cfgc, cfgs);
			tool.loadTreasureMap("藏宝图", srcdir + "/藏宝图.xlsx", cfgc, cfgs);
			tool.loadBeMonster("我是大魔王", srcdir + "/我是大魔王.xlsx", cfgc, cfgs);
			
			cfgc.luaCfg = "";			
			
			//
			if( checkFileNameNew != null )
			{
				
			}
			//
			if( am.containsKey("check") )
			{
				System.out.println("数据检查成功");
				return;
			}
			String luaCfg = "";
			for(Map.Entry<String, String> e : tool.luaCfg.entrySet())
			{
				String name = e.getKey();
				if( name.equals("arena") || name.equals("march") || name.equals("weapon")|| name.equals("dailytask2"))
				{
					String lua = "\n\ni3k_cfg_" + name +"=" + e.getValue() + ";";
					File lua_ofile = new File(lua_dstdir + "/i3k_db_" + name + ".lua");
					FileOutputStream luafos = new FileOutputStream(lua_ofile);
					luafos.write(lua.getBytes("UTF-8"));
					luafos.close();
				}
				else
					luaCfg = luaCfg + "\n\ni3k_cfg_" + e.getKey() + "=" + e.getValue() + ";";				
			}
			
			File lua_ofile = new File(lua_dstdir + "/i3k_db_cfg.lua");
			FileOutputStream luafos = new FileOutputStream(lua_ofile);
			luafos.write(luaCfg.getBytes("UTF-8"));
			luafos.close();
			
			File ofile = new File(dstdir + "/cfg.dat");
			File server_ofile = new File(server_dstdir + "/server_cfg.dat");
			File server_tfile = new File(server_dstdir + "/tool_cfg.dat");
			File server_dfile = new File(server_dstdir + "/dirty_words_cfg.dat");
			
			Stream.storeObjLE(cfgc, ofile);
			Stream.storeObjLE(cfgs, server_ofile);
			Stream.storeObjLE(cfgt, server_tfile);
			Stream.storeObjLE(cfgd, server_dfile);
			if( tool.checkPS != null )
				tool.checkPS.close();
			
			System.out.println("output data cfg to " + ofile.getAbsolutePath());
			System.out.println("output server data cfg to " + server_ofile.getAbsolutePath());
			System.out.println("output tool data cfg to " + server_tfile.getAbsolutePath());
			System.out.println("output dirty words data cfg to " + server_dfile.getAbsolutePath());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("数据异常.");
		}
		
	}
	
	static class FunctionBuilding
	{
		public FunctionBuilding(short type, short lvl)
		{
			this.type = type;
			this.lvl = lvl;
		}
		
		public short type;
		public short lvl;
	}

	PrintStream checkPS;
	Map<String, String> luaCfg = new HashMap<String, String>();
	Map<Short, SBean.ItemCFGS> itemIDs = new TreeMap<Short, SBean.ItemCFGS>();
	Set<Byte> combatCatIDs = new TreeSet<Byte>();
	Set<Short> packIDs = new TreeSet<Short>();
	Set<Short> cmnRewardIDs = new TreeSet<Short>();
	Map<Short, SBean.EquipCFGS> equipIDs = new TreeMap<Short, SBean.EquipCFGS>();
	Set<Byte> propIDs = new TreeSet<Byte>();
	Set<Short> dlgTableIDs = new TreeSet<Short>();
	Set<Short> dlgTableIDsNew = new TreeSet<Short>();
	Set<Short> tipsIDs = new TreeSet<Short>();
	Set<Short> dropTableIDs = new TreeSet<Short>();
	Set<Short> taskIDs = new TreeSet<Short>();
	Set<Short> scriptIDs = new TreeSet<Short>();
	Set<Short> iconIDs = new TreeSet<Short>();
	Set<Short> uiEffectIDs = new TreeSet<Short>();
	Set<Short> artEffectIDs = new TreeSet<Short>();
	Map<Byte, Short> fightEffectIDs = new TreeMap<Byte, Short>();
	Set<Short> skinIDs = new TreeSet<Short>();
	Set<Short> modelIDs = new TreeSet<Short>();
	Set<Short> buffIDs = new TreeSet<Short>();
	Set<Short> skillIDs = new TreeSet<Short>();
	Map<Short, Byte> battleSkillIDs = new TreeMap<Short, Byte>();
	Map<Short, SBean.GeneralCFGS> generalIDs = new TreeMap<Short, SBean.GeneralCFGS>();
	Map<Short, SBean.PetCFGS> petIDs = new TreeMap<Short, SBean.PetCFGS>();
	Map<Short, SBean.PetCFGS> deformPetIDs = new TreeMap<Short, SBean.PetCFGS>();
	Map<Short, Byte> generalRanks = new TreeMap<Short, Byte>();
	Set<Short> combatMapIDs = new TreeSet<Short>();
	Set<Short> combatIDs = new TreeSet<Short>();
	Set<Byte> buildingIDs = new TreeSet<Byte>();
	Map<Byte, Short> soldierItemIDs = new TreeMap<Byte, Short>();
	Map<String, short[]> generalDlgs = new HashMap<String, short[]>();
}


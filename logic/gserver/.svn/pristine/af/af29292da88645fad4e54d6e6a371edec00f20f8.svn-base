
package i3k.gs;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

import ket.util.Stream;
import i3k.SBean;

public class ToolData
{
	
	public static ToolData loadToolData(String dirCData)
	{
		try
		{
			SBean.GameDataCFGT gamedata = new SBean.GameDataCFGT();
			if( Stream.loadObjLE(gamedata, new File(dirCData + File.separator + "tool_cfg.dat")) )
				return new ToolData(gamedata);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public ToolData(SBean.GameDataCFGT cfg) throws Exception
	{
		this.cfg = cfg;
		for(SBean.GeneralName e : cfg.generals)
			generals.put(e.id, e.name);
		for(SBean.GeneralName e : cfg.equips)
			equips.put(e.id, e.name);
		for(SBean.GeneralName e : cfg.items)
			items.put(e.id, e.name);
	}
	
	public String getGeneralName(short gid)
	{
		return generals.get(gid);
	}
	
	public String getEquipName(short gid)
	{
		return equips.get(gid);
	}
	
	public String getItemName(short gid)
	{
		return items.get(gid);
	}
	
	Map<Short, String> generals = new HashMap<Short, String>();
	Map<Short, String> equips = new HashMap<Short, String>();
	Map<Short, String> items = new HashMap<Short, String>();
	SBean.GameDataCFGT cfg;
}


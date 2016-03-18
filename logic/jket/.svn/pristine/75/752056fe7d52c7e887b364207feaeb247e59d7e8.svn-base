
package ket.kdb;

import java.nio.file.Path;
import java.nio.file.Paths;

import ket.util.ArgsMap;

public class DBTool
{
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		if( am.containsKey("update") )
		{
			String cfgOld = am.get("-cfgold");
			String cfgNew = am.get("-cfgnew");
			String clsTrans = am.get("-translator");
			if( cfgOld == null || cfgNew == null || cfgOld.equals(cfgNew) || clsTrans == null )
			{
				System.out.println("usage: DBTool -update --cfgold=<oldcfgxmlfile> --cfgnew=<newcfgxmlfile> --translator=<translatorclassname>");
				return;
			}
			Path po = Paths.get(cfgOld);
			Path pn = Paths.get(cfgNew);
			tool.upgradeDB(po.getParent(), po, pn.getParent(), pn, clsTrans);
		}
	}
	
	static Tool tool = Factory.newTool();
}

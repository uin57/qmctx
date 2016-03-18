
package ket.kdb;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ket.util.FileTool;

class ToolImp implements Tool
{
	
	static class CopyTask
	{
		public CopyTask(String src, String dst)
		{
			this.src = src;
			this.dst = dst;
		}
		
		String src;
		String dst;
	}
	
	static class TransMap
	{
		public TransMap(String translator)
		{
			try
			{
				Class<?> cls = Class.forName(translator);
				transObj = cls.newInstance();
				for(Method m : cls.getMethods())
				{
					if( m.getName().equals("trans") && m.getParameterTypes().length == 1 )
					{
						String rname = m.getReturnType().getName();
						String pname = m.getParameterTypes()[0].getName();
						methods.put(rname + pname, m);
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		public Object trans(Class<?> classOld, Class<?> classNew, Object obj)
		{
			Method m = methods.get(classNew.getName() + classOld.getName());
			if( m == null )
				throw new Error("can't translate from " + classOld + " to " + classNew);
			try
			{
				return m.invoke(transObj, obj);
			}
			catch(Exception ex)
			{
				throw new Error("can't translate from " + classOld + " to " + classNew);
			}
		}
		
		public Object transObj;
		public Map<String, Method> methods = new HashMap<String, Method>();	
	}

	public void upgradeDB(Path pathDBDirOld, Path pathCfgFileOld, Path pathDBDirNew, Path pathCfgFileNew, String translator)
	{
		// load translator
		TransMap transMap = new TransMap(translator);
		
		// load db
		DBImp dbOld = new DBImp();
		dbOld.open(pathDBDirOld, pathCfgFileOld);
		DBImp dbNew = new DBImp();
		dbNew.open(pathDBDirNew, pathCfgFileNew);
		dbNew.clear();
		
		//
		List<CopyTask> copyTasks = new ArrayList<CopyTask>();
		// compare
		Map<String, AbstractTable> tablesOld = dbOld.getTableMap();
		Map<String, AbstractTable> tablesNew = dbNew.getTableMap();
		for(Map.Entry<String, AbstractTable> e : tablesOld.entrySet())
		{
			String name = e.getKey();
			AbstractTable tableOld = e.getValue();
			AbstractTable tableNew = tablesNew.get(name);
			if( tableNew == null )
				continue;
			System.out.println("start upgrading table " + name + "...");
			if( tableOld.getClassKey() == tableNew.getClassKey() && tableOld.getClassValue() == tableNew.getClassValue() )
			{
				System.out.println("    same table, copy it later");
				copyTasks.add(new CopyTask(tableOld.fileName, tableNew.fileName));
				// TODO
			}
			else if( tableOld.getClassKey() == null && tableNew.getClassKey() != null 
					|| tableOld.getClassKey() != null && tableNew.getClassKey() == null )
			{
				throw new RuntimeException("    can't update from " + tableOld.getClassKey() + " to " + tableNew.getClassKey());
			}
			else if( tableOld.getClassValue() == null && tableNew.getClassValue() != null
					|| tableOld.getClassValue() != null && tableNew.getClassValue() == null )
			{
				throw new RuntimeException("    can't update from " + tableOld.getClassValue() + " to " + tableNew.getClassValue());
			}
			else
			{
				System.out.println("    upgrade table from [" + tableOld.getClassKey() + "]=>[" + tableOld.getClassValue()+ "] to ["
						+ tableNew.getClassKey() + "]=>[" + tableNew.getClassValue() + "]");
				for(TableEntry<byte[], byte[]> re : tableOld)
				{
					byte[] keyOld = re.getKey();
					byte[] valOld = re.getValue();
					byte[] keyNew = keyOld;
					byte[] valNew = valOld;
					if( tableOld.getClassKey() != tableNew.getClassKey() )
					{
						Object objOld = TableHandles.loadBytes(keyOld, tableOld.getClassKey());
						Object objNew = transMap.trans(tableOld.getClassKey(), tableNew.getClassKey(), objOld);
						keyNew = TableHandles.getBytes(objNew, tableNew.getClassKey());
					}
					if( tableOld.getClassValue() != tableNew.getClassValue() )
					{
						Object objOld = TableHandles.loadBytes(valOld, tableOld.getClassValue());
						Object objNew = transMap.trans(tableOld.getClassValue(), tableNew.getClassValue(), objOld);
						valNew = TableHandles.getBytes(objNew, tableNew.getClassValue());
					}
					tableNew.put(keyNew, valNew);
				}
			}
		}
		
		
		// close
		dbOld.close();
		dbNew.close();
		
		// copy
		try
		{
			for(CopyTask ct : copyTasks)
			{
				System.out.println("    copy db file from " + ct.src + " to " + ct.dst);
				if( ! FileTool.copyFile(ct.src, ct.dst) )
					throw new RuntimeException("    copy failed from " + ct.src + " to " + ct.dst);
			}
		}
		catch(IOException ex)
		{
			throw new RuntimeException("    copy failed");
		}
		
		System.out.println("upgrade successfully completed.");
	}
}


package ket.kdb;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransImp
{
	
	public Transaction.ErrorCode begin(Transaction trans, DBImp db)
	{
		Map<String, Field> tablemap = new HashMap<String, Field>();
		Field[] fields = trans.getClass().getFields();
		for(Field field : fields)
		{
			if( (  field.getType() == TableReadonly.class || field.getType() == Table.class) 
					&& field.getModifiers() == Modifier.PUBLIC && field.isAnnotationPresent(Transaction.AutoInit.class) )
			{
				tablemap.put(field.getName(), field);
			}
		}
		for(Map.Entry<String, Field> e : tablemap.entrySet())
		{
			AbstractTable atable = db.getTable(e.getKey());
			if( atable == null )
				return Transaction.ErrorCode.eBadTable;
			AbstractTableHandle table = TableHandles.newTableHandle(atable
					, e.getValue().getType() == TableReadonly.class ); // TODO get null table
			tables.add(table);
			try
			{
				e.getValue().set(trans, table);
			}
			catch(IllegalAccessException ex)
			{
				throw new Error("DBErr: illegal reflection", ex);
			}
		}
		Collections.sort(tables);
		for(AbstractTableHandle table : tables)
			table.lock();
		return Transaction.ErrorCode.eOK;
	}
	
	public void rollback()
	{
		for(AbstractTableHandle table : tables)
		{
			table.unlock();
			table.clear();
		}
	}
	
	public void commit()
	{
		for(AbstractTableHandle table : tables)
			table.commit();
		for(AbstractTableHandle table : tables)
		{
			table.unlock();
			table.clear();
		}
	}		
	
	private List<AbstractTableHandle> tables = new ArrayList<AbstractTableHandle>();
}

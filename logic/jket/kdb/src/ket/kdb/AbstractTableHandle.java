
package ket.kdb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

class AbstractTableHandle implements Comparable<AbstractTableHandle>
{	
	
	public AbstractTableHandle(final AbstractTable table, final boolean readonly)
	{
		this.table = table;
		this.readonly = readonly;
	}
	
	public final void clear()
	{
		table = null;
	}
	
	static class DataCache
	{
		public DataCache(final byte[] val, boolean bDirty)
		{
			this.val = val;
			this.bDirty = bDirty;
		}
		
		public byte[] val;
		public boolean bDirty;
	}
	
	public final void lock()
	{
		tablelock = readonly ? table.getLock().readLock() : table.getLock().writeLock();
		tablelock.lock();
	}
	
	public final void unlock()
	{
		tablelock.unlock();
	}
	
	public final void commit()
	{
		for(Map.Entry<byte[], DataCache> e : cache.entrySet())
		{
			if( e.getValue().bDirty )
			{
				if( e.getValue().val == null )
					table.del(e.getKey());
				else
					table.put(e.getKey(), e.getValue().val);
			}
		}
	}
	
	@Override
	public final int compareTo(final AbstractTableHandle other)
	{
		return table.compareTo(other.table);
	}
	
	AbstractTable table;
	transient Lock tablelock;
	final boolean readonly;
	
	Map<byte[], DataCache> cache = new HashMap<byte[], DataCache>(); // never use when readonly
}
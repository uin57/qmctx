
package ket.kdb;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class AbstractTable implements Table<byte[], byte[]>, TableReadonly<byte[], byte[]>
								, Comparable<AbstractTable>, Iterable<TableEntry<byte[], byte[]>>
{
	protected AbstractTable(String fileName, boolean bStandAlone)
	{
		lock = new ReentrantReadWriteLock();
		this.fileName = fileName;
		bufferManager = new BufferManager(name, fileName, bStandAlone);
	}
	
	public ReadWriteLock getLock()
	{
		return lock;
	}
	
	public void load()
	{
		bufferManager.load();
		loadTable();
	}
	
	public abstract void loadTable();
	
	public void clear()
	{
		bufferManager.clear();
		loadTable();
	}
	
	public void close()
	{
		bufferManager.close();
	}
	
	public final void setName(final String name)
	{
		this.name = name;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final void setCacheLow(final int cacheLow)
	{
		this.cacheLow = cacheLow;
	}
	
	public final int getCacheLow()
	{
		return cacheLow;
	}
	
	public final void setCacheHigh(final int cacheHigh)
	{
		this.cacheHigh = cacheHigh;
	}
	
	public final int getCacheHigh()
	{
		return cacheHigh;
	}
	
	public boolean isCacheOverflow()
	{
		if( bufferManager.getCacheSize() > 1024 * 1024 * cacheHigh )
			return true;
		return false;
	}
	
	public Class<?> getClassKey()
	{
		return classKey;
	}
	
	public void setClassKey(Class<?> classKey)
	{
		this.classKey = classKey;
	}
	
	public Class<?> getClassValue()
	{
		return classValue;
	}
	
	public void setClassValue(Class<?> classValue)
	{
		this.classValue = classValue;
	}
	
	public final BufferManager getBufferManager()
	{
		return bufferManager;
	}
	
	public final void setSeq(final int seq)
	{
		this.seq = seq;
	}
	
	public final int getSeq()
	{
		return seq;
	}
	
	@Override
	public final int compareTo(AbstractTable other)
	{
		return seq - other.seq;
	}
	
	private int seq;
	private String name;
	private int cacheLow;
	private int cacheHigh;
	private Class<?> classKey;
	private Class<?> classValue;
	private ReadWriteLock lock;
	final String fileName;
	final BufferManager bufferManager;
}

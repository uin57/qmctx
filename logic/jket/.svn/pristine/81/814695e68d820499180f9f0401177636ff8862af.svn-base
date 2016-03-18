
package ket.kdb;

abstract class PageView implements RangeIndexed
{
	protected PageView(final Range range)
	{
		this.range = range;
	}
	
	@Override
	public final Range range()
	{
		return range;
	}
	
	public final int getPageCount()
	{
		return range.size();
	}
	
	public final int getDataSize()
	{
		return range.size() * Page.SIZE;
	}
	
	public abstract void setDirty(final int pageIndex);
	public abstract void setDirty(final Range range);	
	public abstract void setAllDirty();
	
	public final byte[] getBytes(final int offset, final int len)
	{
		byte[] bs = new byte[len];
		getBytes(offset, bs, 0, len);
		return bs;
	}
	public abstract void getBytes(final int offset, final byte[] dstBuf, final int dstOffset, final int len);
	public final void setBytes(final int offset, final byte[] data)
	{
		setBytes(offset, data, 0, data.length);
	}
	public abstract void setBytes(final int offset, final byte[] srcBuf, final int srcOffset, final int len);
	public abstract void copyRangeOf(final int offset, final PageView pvOther, final int srcOffset, final int len);
	public abstract int getInt(final int offset);
	public abstract void setInt(final int offset, final int val);
	public abstract short getShort(final int offset);
	public abstract void setShort(final int offset, final short val);
	public abstract byte getByte(final int offset);
	public abstract void setByte(final int offset, final byte val);
	
	public final void save(final DBFile dbFile)
	{
		save(dbFile, range.from * (long)Page.SIZE);
	}
	public final void save(final DBFile dbFile, final Range range)
	{
		save(dbFile, range.from * (long)Page.SIZE, range);
	}
	public final void load(final DBFile dbFile)
	{
		load(dbFile, range.from * (long)Page.SIZE);
	}
	public final void load(final DBFile dbFile, final Range range)
	{
		load(dbFile, range.from * (long)Page.SIZE, range);
	}
	public final boolean tryLoad(final DBFile dbFile)
	{
		return tryLoad(dbFile, range.from * (long)Page.SIZE);
	}
	public final boolean tryLoad(final DBFile dbFile, final Range range)
	{
		return tryLoad(dbFile, range.from * (long)Page.SIZE, range);
	}
	
	public final void save(final DBFile dbFile, final long offset)
	{
		save(dbFile, offset, range);
	}
	public abstract void save(final DBFile dbFile, final long offset, final Range range);
	public final void load(final DBFile dbFile, final long offset)
	{
		load(dbFile, offset, range);
	}
	public final void load(final DBFile dbFile, final long offset, final Range range)
	{
		if( ! tryLoad(dbFile, offset, range) )
			throw new Error("DBError: load failed");
	}
	public final boolean tryLoad(final DBFile dbFile, final long offset)
	{
		return tryLoad(dbFile, offset, range);
	}
	public abstract boolean tryLoad(final DBFile dbFile, final long offset, final Range range);
	
	protected final Range range;
}

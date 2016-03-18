
package ket.kdb;

class PageViewWithReader extends PageView
{

	public PageViewWithReader(CloneOnWrite cow)
	{
		super(cow.getRange());
		this.cow = cow;
	}
	
	@Override
	public final void save(final DBFile dbFile, final long offset, final Range range)
	{
		cow.save(dbFile, offset, range);
	}
	
	public void release()
	{
		cow.readerRelease();
	}

	@Override
	public void setDirty(int pageIndex)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setDirty(Range range)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setAllDirty()
	{
		throw new UnsupportedOperationException();		
	}

	@Override
	public void getBytes(int offset, byte[] dstBuf, int dstOffset, int len)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setBytes(int offset, byte[] srcBuf, int srcOffset, int len)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void copyRangeOf(int offset, PageView pvOther, int srcOffset, int len)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public int getInt(int offset)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setInt(int offset, int val)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public short getShort(int offset)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setShort(int offset, short val)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public byte getByte(int offset)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public void setByte(int offset, byte val)
	{
		throw new UnsupportedOperationException();	
	}

	@Override
	public boolean tryLoad(DBFile dbFile, long offset, Range range)
	{
		throw new UnsupportedOperationException();	
	}

	private final CloneOnWrite cow;
}

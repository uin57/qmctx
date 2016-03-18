
package ket.kdb;

class PageBuffer extends PageView
{
	public PageBuffer(final Range range)
	{
		this(range, new byte[Page.SIZE * range.size()]);
	}		
	
	private PageBuffer(final Range range, final byte[] data)
	{
		super(range);
		this.data = data;
	}
	
	public final PageBuffer clonePageBuffer()
	{
		return new PageBuffer(range, data.clone());
	}
	
	@Override
	public final void save(final DBFile dbFile, final long offset, final Range range)
	{
		dbFile.write(offset, data, (range.from - this.range.from) * Page.SIZE, range.size() * Page.SIZE);
	}
	
	@Override
	public final boolean tryLoad(final DBFile dbFile, final long offset, final Range range)
	{
		return dbFile.tryRead(offset, data, (range.from - this.range.from) * Page.SIZE, range.size() * Page.SIZE);
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
	public void getBytes(final int offset, final byte[] dstBuf, final int dstOffset, final int len)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setBytes(final int offset, final byte[] srcBuf, final int srcOffset, final int len)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void copyRangeOf(final int offset, final PageView pvOther, final int srcOffset, final int len)
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
	public byte getByte(final int offset)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setByte(final int offset, final byte val)
	{
		throw new UnsupportedOperationException();
	}
	
	protected final byte[] data; // data buffered
}

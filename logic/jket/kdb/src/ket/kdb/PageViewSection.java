
package ket.kdb;

class PageViewSection extends PageView
{
	public PageViewSection(final Range range, final PageView pv)
	{
		super(range);
		if( ! pv.range.contains(range) )
			throw new Error();
		this.pv = pv;
		offset = (range.from - pv.range.from) * Page.SIZE;
	}
	
	@Override
	public final void setDirty(final int pageIndex)
	{
		pv.setDirty(pageIndex);
	}
	
	@Override
	public final void setDirty(final Range range)
	{
		pv.setDirty(range);
	}
	
	@Override
	public final void setAllDirty()
	{
		pv.setDirty(range);
	}
	
	@Override
	public final void getBytes(final int offset, final byte[] dstBuf, final int dstOffset, final int len)
	{
		pv.getBytes(this.offset + offset, dstBuf, dstOffset, len);
	}
	
	@Override
	public final void setBytes(final int offset, final byte[] srcBuf, final int srcOffset, final int len)
	{
		pv.setBytes(this.offset + offset, srcBuf, srcOffset, len);
	}
	
	@Override
	public final void copyRangeOf(final int offset, final PageView pvOther, final int srcOffset, final int len)
	{
		pv.copyRangeOf(this.offset + offset, pvOther, srcOffset, len);
	}
	
	@Override
	public final int getInt(final int offset)
	{
		return pv.getInt(this.offset + offset);
	}
	
	@Override
	public final void setInt(final int offset, final int val)
	{
		pv.setInt(this.offset + offset, val);
	}
	
	@Override
	public final short getShort(final int offset)
	{
		return pv.getShort(this.offset + offset);
	}
	
	@Override
	public final void setShort(final int offset, final short val)
	{
		pv.setShort(this.offset + offset, val);
	}
	
	@Override
	public final byte getByte(final int offset)
	{
		return pv.getByte(this.offset + offset);
	}
	
	@Override
	public final void setByte(final int offset, final byte val)
	{
		pv.setByte(this.offset + offset, val);
	}
	
	@Override
	public void save(final DBFile dbFile, final long offset, final Range range)
	{
		pv.save(dbFile, offset, range);
	}
	
	@Override
	public final boolean tryLoad(final DBFile dbFile, final long offset, final Range range)
	{
		return pv.tryLoad(dbFile, offset, range);
	}
	
	public final PageView pv;
	public final int offset;
}

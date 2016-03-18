
package ket.kdb;

import java.nio.ByteBuffer;
import java.util.Arrays;

class PageBufferManaged extends PageBuffer
{
	public PageBufferManaged(final Range range)
	{
		super(range);
		bb = ByteBuffer.wrap(data);
		accessTime = new long[range.size()];
		dirty = new boolean[range.size()];
		touchAll();
	}
	
	public final void touch(final int pageIndex)
	{
		accessTime[pageIndex - range.from] = System.currentTimeMillis();
	}
	
	public final void touchAll()
	{
		Arrays.fill(accessTime, System.currentTimeMillis());
	}
	
	@Override
	public final void setDirty(final int pageIndex)
	{
		dirty[pageIndex - range.from] = true;
	}
	
	@Override
	public final void setDirty(final Range range)
	{
		for(int i : range)
			setDirty(i);
	}
	
	public final void setDirty(final Range range, final boolean dirty[])
	{
		for(int i : range)
			if( dirty[i - range.from] )
				setDirty(i);
	}	
	
	@Override
	public final void getBytes(final int offset, final byte[] dstBuf, final int dstOffset, final int len)
	{
		System.arraycopy(data, offset, dstBuf, dstOffset, len);
	}
	
	@Override
	public final void setBytes(final int offset, final byte[] srcBuf, final int srcOffset, final int len)
	{
		cow.write(this);
		System.arraycopy(srcBuf, srcOffset, this.data, offset, len);
	}
	
	@Override
	public final void copyRangeOf(final int offset, final PageView pvOther, final int srcOffset, final int len)
	{
		cow.write(this);
		pvOther.getBytes(srcOffset, data, offset, len);
	}
	
	public final void setBits(final Range range)
	{
		cow.write(this);
		for(int i : range)
			data[i/8] |= ( 1 << (i % 8) );
	}
	
	public final void clearBits(final Range range)
	{
		cow.write(this);
		for(int i : range)
			data[i/8] &= (~( 1 << (i % 8) ));
	}
	
	public final boolean isBitClear(final int pageIndex)
	{
		return ( (data[pageIndex/8] & ( 1 << (pageIndex % 8) )) == 0 );
	}
	
	public final boolean isBitSet(final int pageIndex)
	{
		return ( (data[pageIndex/8] & ( 1 << (pageIndex % 8) )) != 0 );
	}

	@Override
	public final byte getByte(final int offset)
	{
		return data[offset];
	}
	
	@Override
	public final void setByte(final int offset, final byte val)
	{
		cow.write(this);
		data[offset] = val;
	}
	
	@Override
	public final int getInt(final int offset)
	{
		return bb.getInt(offset);
	}
	
	@Override
	public final void setInt(final int offset, final int val)
	{
		cow.write(this);
		bb.putInt(offset, val);
	}
	
	@Override
	public final short getShort(final int offset)
	{
		return bb.getShort(offset);
	}
	
	@Override
	public final void setShort(final int offset, final short val)
	{
		cow.write(this);
		bb.putShort(offset, val);
	}
	
	@Override
	public final void setAllDirty()
	{
		Arrays.fill(dirty, true);
	}
	
	public final void clearDirty(final int pageIndex)
	{
		dirty[pageIndex - range.from] = false;
	}
	
	public final void clearAllDirty()
	{
		Arrays.fill(dirty, false);
	}
	
	public final long getAccessTime(final int pageIndex)
	{
		return accessTime[pageIndex - range.from];
	}
	
	public final long[] getAccessTime(final Range range)
	{
		return Arrays.copyOfRange(accessTime, range.from - this.range.from, range.to - this.range.from);
	}
	
	public final boolean isDirty(final int pageIndex)
	{
		return dirty[pageIndex - range.from];
	}
	
	public final boolean[] isDirty(final Range range)
	{
		return Arrays.copyOfRange(dirty, range.from - this.range.from, range.to - this.range.from);
	}
	
	public ByteBuffer clear()
	{
		bb.clear();
		return bb;
	}
	
	public CloneOnWrite getCOW()
	{
		return cow;
	}
	
	private final ByteBuffer bb;
	private final long[] accessTime; // last access time of every page
	private final boolean[] dirty;
	private final CloneOnWrite cow = new CloneOnWrite();
}

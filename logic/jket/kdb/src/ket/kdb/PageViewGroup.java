
package ket.kdb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

final class PageViewGroup extends PageView
{
	public PageViewGroup(final Range range, final List<PageView> pvLst)
	{
		super(range);
		this.pvLst = pvLst;
	}
	
	private PageView findPageView(final int pageIndex)
	{
		for(PageView pv : pvLst)
			if( pv.range().contains(pageIndex) )
				return pv;
		return null;
	}
	
	private List<PageView> findPageViewList(final Range range)
	{
		List<PageView> lst = new ArrayList<PageView>();
		if( range.size() == 0 ) return lst;
		int i = range.from;
		for(PageView pv : pvLst)
		{
			if( ! pv.range().contains(i) )
				continue;
			if( pv.range().to >= range.to )
			{
				lst.add(new PageViewSection(new Range(i, range.to), pv));
				break;
			}
			lst.add(new PageViewSection(new Range(i, pv.range().to), pv));
			i = pv.range().to;
		}
		return lst;
	}
	
	private static class PageSection
	{
		public PageSection(final int offset, final int pvOffset, final int pvLen, final PageView pv)
		{
			this.offset = offset;
			this.pvOffset = pvOffset;
			this.pvLen = pvLen;
			this.pv = pv;
		}
		public int offset;
		public int pvOffset;
		public int pvLen;
		public PageView pv;
	}
	
	private List<PageSection> findPageSections(final int offset, final int len)
	{
		List<PageSection> lst = new ArrayList<PageSection>();
		if( len == 0 ) return lst;
		int start = offset;
		final int base = range.from * Page.SIZE;
		final int end = offset + len;
		for(PageView pv : pvLst)
		{
			if( pv.range().to * Page.SIZE <= base + start )
				continue;
			final int pos = pv.range().to * Page.SIZE;
			if( pos >= base + end )
			{
				lst.add(new PageSection(start - offset, start + base - pv.range().from * Page.SIZE, end - start, pv));
				break;
			}
			lst.add(new PageSection(start - offset, start + base - pv.range().from * Page.SIZE, pos - base - start, pv));
			start = pos - base;
		}
		return lst;
	}
	
	
	@Override
	public final void setDirty(final int pageIndex)
	{
		findPageView(pageIndex).setDirty(pageIndex);
	}
	
	@Override
	public final void setDirty(final Range range)
	{
		List<PageView> lst = findPageViewList(range);
		for(PageView pv : lst)
			pv.setAllDirty();
	}
	
	@Override
	public final void setAllDirty()
	{
		for(PageView pv : pvLst)
			pv.setAllDirty();
	}
	
	@Override
	public final void getBytes(final int offset, final byte[] dstBuf, final int dstOffset, final int len)
	{
		List<PageSection> lst = findPageSections(offset, len);
		for(PageSection ps : lst)
		{
			ps.pv.getBytes(ps.pvOffset, dstBuf, dstOffset + ps.offset, ps.pvLen);
		}
	}
	
	@Override
	public final void setBytes(final int offset, final byte[] srcBuf, final int srcOffset, final int len)
	{
		List<PageSection> lst = findPageSections(offset, len);
		for(PageSection ps : lst)
		{
			ps.pv.setBytes(ps.pvOffset, srcBuf, srcOffset + ps.offset, ps.pvLen);
		}
	}
	
	@Override
	public final void copyRangeOf(final int offset, final PageView pvOther, final int srcOffset, final int len)
	{
		List<PageSection> lst = findPageSections(offset, len);
		for(PageSection ps : lst)
		{
			ps.pv.copyRangeOf(ps.pvOffset, pvOther, srcOffset + ps.offset, ps.pvLen);
		}
	}
	
	@Override
	public final int getInt(final int offset)
	{
		List<PageSection> lst = findPageSections(offset, 4);
		if( lst.size() == 1 )
		{
			PageSection ps = lst.get(0);
			return ps.pv.getInt(ps.pvOffset);
		}
		byte[] bs = new byte[4];
		for(PageSection ps : lst)
		{
			ps.pv.getBytes(ps.pvOffset, bs, ps.offset, ps.pvLen);
		}
		return ByteBuffer.wrap(bs).getInt();
	}
	
	@Override
	public final void setInt(final int offset, final int val)
	{
		List<PageSection> lst = findPageSections(offset, 4);
		if( lst.size() == 1 )
		{
			PageSection ps = lst.get(0);
			ps.pv.setInt(ps.pvOffset, val);
			return;
		}
		byte[] bs = new byte[4];
		ByteBuffer.wrap(bs).putInt(val);
		for(PageSection ps : lst)
		{
			ps.pv.setBytes(ps.pvOffset, bs, ps.offset, ps.pvLen);
		}
	}
	
	@Override
	public final short getShort(final int offset)
	{
		List<PageSection> lst = findPageSections(offset, 2);
		if( lst.size() == 1 )
		{
			PageSection ps = lst.get(0);
			return ps.pv.getShort(ps.pvOffset);
		}
		byte[] bs = new byte[2];
		for(PageSection ps : lst)
		{
			ps.pv.getBytes(ps.pvOffset, bs, ps.offset, ps.pvLen);
		}
		return ByteBuffer.wrap(bs).getShort();
	}
	
	@Override
	public final void setShort(final int offset, final short val)
	{
		List<PageSection> lst = findPageSections(offset, 2);
		if( lst.size() == 1 )
		{
			PageSection ps = lst.get(0);
			ps.pv.setShort(ps.pvOffset, val);
			return;
		}
		byte[] bs = new byte[2];
		ByteBuffer.wrap(bs).putShort(val);
		for(PageSection ps : lst)
		{
			ps.pv.setBytes(ps.pvOffset, bs, ps.offset, ps.pvLen);
		}
	}
	
	@Override
	public final byte getByte(final int offset)
	{
		List<PageSection> lst = findPageSections(offset, 1);
		PageSection ps = lst.get(0);
		return ps.pv.getByte(ps.pvOffset);
	}
	
	@Override
	public final void setByte(final int offset, final byte val)
	{
		List<PageSection> lst = findPageSections(offset, 1);
		PageSection ps = lst.get(0);
		ps.pv.setByte(ps.pvOffset, val);
	}
	
	@Override
	public final void save(final DBFile dbFile, final long offset, final Range range)
	{
		List<PageView> lst = findPageViewList(range);
		int start = 0;
		for(PageView pv : lst)
		{
			pv.save(dbFile, offset + start);
			start += pv.range().size() * (long)Page.SIZE;
		}
	}
	
	@Override
	public final boolean tryLoad(final DBFile dbFile, final long offset, final Range range)
	{
		List<PageView> lst = findPageViewList(range);
		int start = 0;
		for(PageView pv : lst)
		{
			if( ! pv.tryLoad(dbFile, offset + start) )
				return false;
			start += pv.range().size() * (long)Page.SIZE;
		}
		return true;
	}
	
	private final List<PageView> pvLst;
}

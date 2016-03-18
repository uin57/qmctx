
package ket.kdb;

class BlockEntry implements Cloneable
{		
	private static final int PAGEINDEX_OFFSET = 0;
	private static final int PAGECOUNT_OFFSET = 4;
	private static final int RECORDCOUNT_OFFSET = 6;
	private static final int FREESPACE_FROM_OFFSET = 8;
	private static final int FREESPACE_TO_OFFSET = 12;
	
	public static final int SIZE = 16;
	
	public BlockEntry()
	{		
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException ex)
		{			
		}
		return null;
	}
	
	public BlockEntry(final Range pageRange)
	{
		pageIndex = pageRange.from;
		pageCount = (short)pageRange.size();
		recordCount = 0;
		freespaceFrom = 0;
		freespaceTo = pageRange.size() * Page.SIZE;
	}
	
	public final void load(final PageView pv, final int offset)
	{
		pageIndex = pv.getInt(offset + PAGEINDEX_OFFSET);
		pageCount = pv.getShort(offset + PAGECOUNT_OFFSET);
		recordCount = pv.getShort(offset + RECORDCOUNT_OFFSET);
		freespaceFrom = pv.getInt(offset + FREESPACE_FROM_OFFSET);
		freespaceTo = pv.getInt(offset + FREESPACE_TO_OFFSET);
	}
	
	public final void save(final PageView pv, final int offset)
	{
		pv.setInt(offset + PAGEINDEX_OFFSET, pageIndex);
		pv.setShort(offset + PAGECOUNT_OFFSET, pageCount);
		pv.setShort(offset + RECORDCOUNT_OFFSET, recordCount);
		pv.setInt(offset + FREESPACE_FROM_OFFSET, freespaceFrom);
		pv.setInt(offset + FREESPACE_TO_OFFSET, freespaceTo);
	}
	
	public final int getFreespaceSize()
	{
		return freespaceTo - freespaceFrom;
	}
	
	public final boolean containsPage(int pageIndex)
	{
		return this.pageIndex <= pageIndex && pageIndex < this.pageIndex + pageCount;
	}
	
	public final Range getPageRange()
	{
		return new Range(pageIndex, pageIndex + pageCount);
	}
	
	public final BlockEntry pushRecord(int dataLength)
	{
		++recordCount;
		freespaceTo -= dataLength;
		return this;
	}
	
	public final BlockEntry coverRecord(int dataLength)
	{
		++recordCount;
		freespaceFrom = dataLength;
		return this;
	}
	
	public BlockEntry setFreespaceFrom(int freespaceFrom)
	{
		this.freespaceFrom = freespaceFrom;
		return this;
	}
	
	public int pageIndex;
	public short pageCount;
	public short recordCount;
	public int freespaceFrom;
	public int freespaceTo;
}	
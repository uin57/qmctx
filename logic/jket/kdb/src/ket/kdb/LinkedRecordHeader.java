
package ket.kdb;

class LinkedRecordHeader extends Address
{
	public static final int NEXT_PAGEINDEX_OFFSET = 0;
	public static final int NEXT_PAGEOFFSET_OFFSET = 4;
	public static final int NEXT_DATA_LENGTH_OFFSET = 6;
	public static final int KEY_LENGTH_OFFSET = 10;
			
	public LinkedRecordHeader()
	{
	}
	
	public LinkedRecordHeader(final Address addr)
	{
		super(addr);
	}
	
	public final void load(final PageView pv, final int offset)
	{
		pageIndex = pv.getInt(offset + NEXT_PAGEINDEX_OFFSET);
		pageOffset = pv.getShort(offset + NEXT_PAGEOFFSET_OFFSET);
		dataLength = pv.getInt(offset + NEXT_DATA_LENGTH_OFFSET);
		keyLength = pv.getInt(offset + KEY_LENGTH_OFFSET);
		key = pv.getBytes(offset + KEY_LENGTH_OFFSET + 4, keyLength);
	}
	
	public final void save(final PageView pv, final int offset)
	{
		pv.setInt(offset + NEXT_PAGEINDEX_OFFSET, pageIndex);
		pv.setShort(offset + NEXT_PAGEOFFSET_OFFSET, pageOffset);
		pv.setInt(offset + NEXT_DATA_LENGTH_OFFSET, dataLength);
		pv.setInt(offset + KEY_LENGTH_OFFSET, keyLength);
		pv.setBytes(offset + KEY_LENGTH_OFFSET + 4, key);
	}
	
	public int keyLength;
	public byte[] key;
}
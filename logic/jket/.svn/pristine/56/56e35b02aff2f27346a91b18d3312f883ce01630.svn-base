
package ket.kdb;

class Pointer
{
	public Pointer(final Address addr, final PageView pv)
	{
		dataOffset = (addr.pageIndex - pv.range().from) * Page.SIZE + addr.pageOffset;
		dataLength = addr.dataLength;
		this.pv = pv;
	}	
	
	public final byte[] getValue()
	{
		return pv.getBytes(dataOffset, dataLength);
	}
	
	public final void putData(final byte[] data)
	{
		pv.setBytes(dataOffset, data);
	}
	
	public final void getLinkedRecordHeader(final LinkedRecordHeader linkedRecordHeader)
	{
		linkedRecordHeader.load(pv, dataOffset);
	}
	
	public final byte[] getLinkedRecordValue(final byte[] key)
	{
		return pv.getBytes(dataOffset + LinkedRecordHeader.KEY_LENGTH_OFFSET + 4 + key.length
				, dataLength - (LinkedRecordHeader.KEY_LENGTH_OFFSET + 4 + key.length));
	}
	
	public final void putLinkedRecord(final LinkedRecordHeader linkedRecordHeader, final byte[] data)
	{
		linkedRecordHeader.save(pv, dataOffset);
		pv.setBytes(dataOffset + LinkedRecordHeader.KEY_LENGTH_OFFSET + 4 + linkedRecordHeader.keyLength, data);
	}
	
	public final void putValuePointer(final Address addr)
	{
		pv.setInt(dataOffset + LinkedRecordHeader.NEXT_PAGEINDEX_OFFSET, addr.pageIndex);
		pv.setShort(dataOffset + LinkedRecordHeader.NEXT_PAGEOFFSET_OFFSET, addr.pageOffset);
		pv.setInt(dataOffset + LinkedRecordHeader.NEXT_DATA_LENGTH_OFFSET, addr.dataLength);
	}
	
	public final void setDirty()
	{
		pv.setDirty(new Range(dataOffset, dataOffset + dataLength).div(Page.SIZE).add(pv.range().from));
	}
			
	private int dataOffset;
	private int dataLength;
	private final PageView pv;
}
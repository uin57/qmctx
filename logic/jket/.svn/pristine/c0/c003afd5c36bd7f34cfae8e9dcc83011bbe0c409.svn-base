
package ket.kdb;

import java.util.Arrays;

class RecordEntry extends Address
{
	private static final int KEYPREFIX_OFFSET = 0;
	private static final int KEYPREFIX_LENGTH_OFFSET = 9;
	private static final int PAGEOFFSET_OFFSET = 10;
	private static final int PAGEINDEX_OFFSET = 12;
	private static final int DATA_LENGTH_OFFSET = 16;
	
	public static final int SIZE = 20;
	
	private static final int MAX_KEYPREFIX_LENGTH = 9;	
	
	public static final boolean isShortKey(final int keyLength)
	{
		return keyLength < MAX_KEYPREFIX_LENGTH;
	}
	
	public RecordEntry()
	{
	}
	
	public RecordEntry(final Address addr)
	{
		super(addr);
	}
	
	public RecordEntry(final Address addr, final byte[] key)
	{
		super(addr);
		this.keyPrefixLength = (byte)key.length;
		keyPrefix =  key.clone();
	}
	
	public RecordEntry(final Address addr, byte keyPrefixLength, byte[] key)
	{
		super(addr);
		this.keyPrefixLength = keyPrefixLength;
		keyPrefix = Arrays.copyOfRange(key, 0, MAX_KEYPREFIX_LENGTH);
	}
	
	public final boolean isShortKey()
	{
		return isShortKey(keyPrefix.length);
	}
	
	public final boolean isInvalid()
	{
		return ( ( keyPrefixLength == -1 || keyPrefixLength == -2 ) && keyPrefix.length == MAX_KEYPREFIX_LENGTH
				|| keyPrefixLength > 0 && keyPrefixLength < MAX_KEYPREFIX_LENGTH && keyPrefixLength == keyPrefix.length);
	}
	
	public final boolean match(final byte[] key)
	{
		return (( keyPrefixLength > 0 && key.length == keyPrefixLength
						&& Arrays.equals(key, keyPrefix)
					|| keyPrefixLength < 0 && key.length >= MAX_KEYPREFIX_LENGTH 
						&& Arrays.equals(Arrays.copyOf(key, MAX_KEYPREFIX_LENGTH), keyPrefix)));
	}
	
	public final void load(final PageView pv, final int offset)
	{			
		keyPrefixLength = pv.getByte(offset + KEYPREFIX_LENGTH_OFFSET);
		keyPrefix = pv.getBytes(offset + KEYPREFIX_OFFSET, (keyPrefixLength < 0 ? MAX_KEYPREFIX_LENGTH : keyPrefixLength));
		pageOffset = pv.getShort(offset + PAGEOFFSET_OFFSET);
		pageIndex = pv.getInt(offset + PAGEINDEX_OFFSET);
		dataLength = pv.getInt(offset + DATA_LENGTH_OFFSET);
	}
	
	public final void save(final PageView pv, final int offset)
	{			
		pv.setBytes(offset + KEYPREFIX_OFFSET, keyPrefix);
		pv.setByte(offset + KEYPREFIX_LENGTH_OFFSET, keyPrefixLength);
		pv.setShort(offset + PAGEOFFSET_OFFSET, pageOffset);
		pv.setInt(offset + PAGEINDEX_OFFSET, pageIndex);
		pv.setInt(offset + DATA_LENGTH_OFFSET, dataLength);
	}
	
	public byte[] keyPrefix;
	public byte keyPrefixLength;
}
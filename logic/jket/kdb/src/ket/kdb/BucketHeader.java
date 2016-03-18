
package ket.kdb;

class BucketHeader
{
	private static final int HASHVALUE_BITUSED_OFFSET = 0;
	private static final int BLOCK_COUNT_OFFSET = 4;
	private static final int PAGE_COUNT_OFFSET = 6;
	private static final int RECORD_COUNT_OFFSET = 8;
	private static final int RECORDENTRY_COUNT_OFFSET = 10;
	private static final int FREESPACE_SIZE_OFFSET = 12;
	private static final int BLOCK_TABLE_SIZE_OFFSET = 16;
	private static final int RECORD_TABLE_SIZE_OFFSET = 18;
	
	private static final short INIT_BLOCK_TABLE_SIZE = 2;
	private static final short BLOCK_TABLE_EXPAND_STEP = 8;
	private static final short INIT_RECORD_TABLE_SIZE = 16;
	private static final short RECORD_TABLE_EXPAND_STEP = 8;		
	
	public static final int SIZE = 20;
	
	public BucketHeader(int hashValueBitUsed)
	{			
		this.hashValueBitUsed = hashValueBitUsed;
		blockCount = (short)1;
		pageCount = (short)1;
		recordCount = (short)0;
		recordEntryCount = (short)0;
		freespaceSize = Page.SIZE - getInitFreespaceSize();
		blockTableSize = INIT_BLOCK_TABLE_SIZE;
		recordTableSize = INIT_RECORD_TABLE_SIZE;
	}
	
	public BucketHeader(final PageView pv)
	{
		load(pv);
	}
	
	public static int getInitFreespaceSize()
	{
		return SIZE + INIT_BLOCK_TABLE_SIZE * BlockEntry.SIZE + INIT_RECORD_TABLE_SIZE * RecordEntry.SIZE;
	}
	
	public final int getBlockTableExpandStep()
	{
		if( blockCount != blockTableSize )
			return 0;
		return blockTableSize < BLOCK_TABLE_EXPAND_STEP ? blockTableSize : BLOCK_TABLE_EXPAND_STEP;
	}
	
	public final int getRecordTableExpandStep()
	{
		if( recordEntryCount != recordTableSize )
			return 0;
		return recordTableSize < RECORD_TABLE_EXPAND_STEP ? recordTableSize : RECORD_TABLE_EXPAND_STEP;
	}
	
	public void load(final PageView pv)
	{
		hashValueBitUsed = pv.getInt(HASHVALUE_BITUSED_OFFSET);
		blockCount = pv.getShort(BLOCK_COUNT_OFFSET);
		pageCount = pv.getShort(PAGE_COUNT_OFFSET);
		recordCount = pv.getShort(RECORD_COUNT_OFFSET);
		recordEntryCount = pv.getShort(RECORDENTRY_COUNT_OFFSET);
		freespaceSize = pv.getInt(FREESPACE_SIZE_OFFSET);
		blockTableSize = pv.getShort(BLOCK_TABLE_SIZE_OFFSET);
		recordTableSize = pv.getShort(RECORD_TABLE_SIZE_OFFSET);
	}
	
	public void save(final PageView pv)
	{
		pv.setInt(HASHVALUE_BITUSED_OFFSET, hashValueBitUsed);
		pv.setShort(BLOCK_COUNT_OFFSET, blockCount);
		pv.setShort(PAGE_COUNT_OFFSET, pageCount);
		pv.setShort(RECORD_COUNT_OFFSET, recordCount);
		pv.setShort(RECORDENTRY_COUNT_OFFSET, recordEntryCount);
		pv.setInt(FREESPACE_SIZE_OFFSET, freespaceSize);
		pv.setShort(BLOCK_TABLE_SIZE_OFFSET, blockTableSize);
		pv.setShort(RECORD_TABLE_SIZE_OFFSET, recordTableSize);
	}
	
	public int hashValueBitUsed;
	public short blockCount;
	public short pageCount;
	public short recordCount;
	public short recordEntryCount;
	public int freespaceSize;
	public short blockTableSize;
	public short recordTableSize;				
}

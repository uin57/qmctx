
package ket.kdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

class HashTableImp extends AbstractTable
{
	private static final int HASH_ENTRY_SIZE = 4;
	private static final int INIT_HASHVALUE_BITUSED = 10;
	private static final int INVALID_BUCKET_PAGEINDEX = BufferManager.META_PAGEINDEX;
		
	private class TableMeta
	{
		private static final int MAX_HASHVALUE_BITUSED = 30;
		private static final int HASHVALUE_BITUSED_OFFSET = 0;
		private static final int HASHTABLE_PAGEINDEX_OFFSET = 4;
		private static final int RECORD_COUNT_OFFSET = 8;
		
		public TableMeta(PageView pv)
		{
			this.pv = pv;
		}		
		
		public int getHashValueBitUsed()
		{
			return pv.getInt(HASHVALUE_BITUSED_OFFSET);
		}
		
		public boolean isFullHashed()
		{
			return getHashValueBitUsed() >= MAX_HASHVALUE_BITUSED;
		}
		public void setHashValueBitUsed(int hvBitUsed)
		{
			pv.setInt(HASHVALUE_BITUSED_OFFSET, hvBitUsed);
			pv.setDirty(BufferManager.TABLE_META_PAGEINDEX);
		}
		
		public int getHashTablePageIndex()
		{
			return pv.getInt(HASHTABLE_PAGEINDEX_OFFSET);
		}
		public void setHashTablePageIndex(int hashTablePageIndex)
		{
			pv.setInt(HASHTABLE_PAGEINDEX_OFFSET, hashTablePageIndex);
			pv.setDirty(BufferManager.TABLE_META_PAGEINDEX);
		}
		
		public int getRecordCount()
		{
			return pv.getInt(RECORD_COUNT_OFFSET);
		}
		
		public void incRecordCount()
		{
			pv.setInt(RECORD_COUNT_OFFSET, pv.getInt(RECORD_COUNT_OFFSET) + 1);
			pv.setDirty(BufferManager.TABLE_META_PAGEINDEX);
		}
		
		public void decRecordCount()
		{
			pv.setInt(RECORD_COUNT_OFFSET, pv.getInt(RECORD_COUNT_OFFSET) - 1);
			pv.setDirty(BufferManager.TABLE_META_PAGEINDEX);
		}
		
		private final PageView pv;
	}
	
	private static class BucketStat
	{
		int recordCount;
		Map<Integer, Integer> pageMap;
	}	
	
	private class Bucket implements Iterable<TableEntry<byte[], byte[]>>
	{				
		public Bucket(PageView pv, int hashValueBase)
		{
			pvMain = pv;
			this.hashValueBase = hashValueBase;
		}
		
		public BucketHeader getHeader()
		{
			return new BucketHeader(pvMain);
		}
		
		public void setHeader(BucketHeader header)
		{			
			header.save(pvMain);
		}
		
		private void getBlockEntry(int i, BlockEntry blockIndexEntry)
		{
			blockIndexEntry.load(pvMain, BucketHeader.SIZE + i * BlockEntry.SIZE);
		}
		
		private void setBlockEntry(int i, BlockEntry blockIndexEntry)
		{
			blockIndexEntry.save(pvMain, BucketHeader.SIZE + i * BlockEntry.SIZE);
		}
		
		private int getRecordEntryOffset(int i)
		{
			return BucketHeader.SIZE + header.blockTableSize * BlockEntry.SIZE + i * RecordEntry.SIZE;
		}
		
		private void getRecordEntry(int i, RecordEntry recordIndexEntry)
		{
			recordIndexEntry.load(pvMain, getRecordEntryOffset(i));
		}
		
		private void setRecordEntry(int i, RecordEntry recordIndexEntry)
		{			
			recordIndexEntry.save(pvMain, getRecordEntryOffset(i));
		}	
				
		public Bucket init(int hvBitUsed)
		{
			header = new BucketHeader(hvBitUsed);
			setHeader(header);
			
			beMain = new BlockEntry(pvMain.range).setFreespaceFrom(BucketHeader.getInitFreespaceSize());
			setBlockEntry(0, beMain);
			
			pvMain.setDirty(pvMain.range.from);
			return this;
		}
		
		public Bucket load()
		{
			header = getHeader();
			beMain = new BlockEntry();
			getBlockEntry(0, beMain);
			if( beMain.pageCount != (short)1 )
			{
				pvMain = bufferManager.findPages(new Range(pvMain.range.from, pvMain.range.from + beMain.pageCount));
			}
			return this;
		}		
		
		public boolean isOverflow()
		{
			//return false;
			return header.blockCount > 1 && header.recordCount > 8; // TODO
		}
		
		public boolean testConsistency(BucketStat bucketStat)
		{
			if( header.hashValueBitUsed > meta.getHashValueBitUsed() )
				return false;
			int sumPageCount = 0;
			int sumFreespaceSize = 0;
			int sumRecordCount = 0;
			if( header.blockCount > header.blockTableSize )
				return false;
			bucketStat.pageMap = new TreeMap<Integer, Integer>();
			BlockEntry blockIndexEntry = new BlockEntry();
			for(int i = 0; i < header.blockCount; ++i )
			{
				getBlockEntry(i, blockIndexEntry);
				if( i == 0 )
				{
					if( blockIndexEntry.pageIndex != pvMain.range.from )
						return false;
					if( blockIndexEntry.pageCount != pvMain.range.size() )
						return false;
				}
				sumPageCount += blockIndexEntry.pageCount;
				sumFreespaceSize += blockIndexEntry.getFreespaceSize();
				sumRecordCount += blockIndexEntry.recordCount;
				for(int j = 0; j < blockIndexEntry.pageCount; ++j)
					bucketStat.pageMap.put(blockIndexEntry.pageIndex + j, blockIndexEntry.pageCount - j);
			}
			if( sumPageCount != header.pageCount )
				return false;
			if( sumFreespaceSize != header.freespaceSize )
				return false;
			if( sumRecordCount != header.recordCount )
				return false;
			
			if( header.recordEntryCount > header.recordTableSize )
				return false;
			if( header.recordEntryCount > header.recordCount )
				return false;
			RecordEntry recordIndexEntry = new RecordEntry();
			for(int i = 0; i < header.recordEntryCount; ++i)
			{
				getRecordEntry(i, recordIndexEntry);
				if( ! recordIndexEntry.isInvalid() )
					return false;
				Integer pageCount = bucketStat.pageMap.get(recordIndexEntry.pageIndex);
				if( pageCount == null || recordIndexEntry.pageOffset + recordIndexEntry.dataLength > pageCount * Page.SIZE )
					return false;
			}
			bucketStat.recordCount = header.recordCount;
			return true;
		}
		
		private EntryCursor<RecordEntry> searchRecordByKey(final byte[] key)
		{
			int index = 0;
			RecordEntry entry = new RecordEntry();
			while( index < header.recordEntryCount )
			{
				getRecordEntry(index, entry);
				if( entry.match(key) )
					return new EntryCursor<RecordEntry>(index, entry);
				++index;
			}
			return null;
		}
		
		private Address getDataAddress(final RecordEntry recordIndexEntry, final byte[] key)
		{
			if( RecordEntry.isShortKey(key.length) )
				return recordIndexEntry;
			
			Address addr = recordIndexEntry;
			while( true )
			{
				Pointer p = getPointer(addr);
				LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
				p.getLinkedRecordHeader(linkedRecordHeader);
				if( Arrays.equals(key, linkedRecordHeader.key) )
					return addr;
				if( linkedRecordHeader.isNull() )
					return null;
				addr = linkedRecordHeader;
			}
		}
		
		private Address getDataAddress(RecordEntry recordIndexEntry, byte[] key, int[] levelVP, Pointer[] lastVS)
		{
			if( RecordEntry.isShortKey(key.length) )
				return recordIndexEntry;
			
			Address addr = recordIndexEntry;
			while( true )
			{
				Pointer p = getPointer(addr);
				LinkedRecordHeader multipleRecordHeader = new LinkedRecordHeader();
				p.getLinkedRecordHeader(multipleRecordHeader);
				if( Arrays.equals(key, multipleRecordHeader.key) )
					return addr;
				if( multipleRecordHeader.isNull() )
					return null;
				addr = multipleRecordHeader;
				levelVP[0]++;
				lastVS[0] = p;
			}
		}
		
		private void detach(int expandStep, int expandSize, boolean bBlock)
		{
			int newPageCount = Page.getCount(beMain.freespaceFrom + expandSize);
			
			PageView newPageView = bufferManager.allocPages(newPageCount, false);			
			pvMain.setDirty(pvMain.range);
			newPageView.copyRangeOf(0, pvMain, 0, beMain.freespaceFrom);
			
			BlockEntry beNew = ((BlockEntry)(beMain.clone())).setFreespaceFrom(0);
			
			pvMain = newPageView;
			
			beMain = new BlockEntry(pvMain.range).setFreespaceFrom(beMain.freespaceFrom);
			
			if( bBlock )
				header.blockTableSize += expandStep;
			else
				header.recordTableSize += expandStep;
			if( bBlock )
			{	
				pvMain.copyRangeOf(getRecordEntryOffset(0), pvMain, getRecordEntryOffset(0) - expandSize, header.recordEntryCount * RecordEntry.SIZE);
			}
			setBlockEntry(header.blockCount, beNew);
			header.freespaceSize += newPageCount * Page.SIZE;
			header.blockCount++;
			header.pageCount += newPageCount;
				
			int hashValueBitDiff = meta.getHashValueBitUsed() - header.hashValueBitUsed;
			for(int i = 0; i < ( 1 << hashValueBitDiff ); ++i)
				hashTable.setBucketPageIndex((hashValueBase % ( 1 << header.hashValueBitUsed ))| (i << header.hashValueBitUsed), pvMain.range.from);						
		}
		
		private void assureBlockIndexTableNotFull()
		{
			int expandStep = header.getBlockTableExpandStep();
			if( expandStep == 0 )
				return;
			int expandSize = expandStep * BlockEntry.SIZE;
			if( beMain.getFreespaceSize() >= expandSize )
			{
				header.blockTableSize += expandStep;
				pvMain.copyRangeOf(getRecordEntryOffset(0), pvMain, getRecordEntryOffset(0) - expandSize, header.recordEntryCount * RecordEntry.SIZE);
			}	
			else
				detach(expandStep, expandSize, true);
			header.freespaceSize -= expandSize;
			beMain.freespaceFrom += expandSize;
		}
		
		private void assureRecordIndexTableNotFull()
		{
			int expandStep = header.getRecordTableExpandStep();
			if( expandStep == 0 )
				return;
			int expandSize = expandStep * RecordEntry.SIZE;
			if( beMain.getFreespaceSize() >= expandSize )
			{
				header.recordTableSize += expandStep;
			}
			else
				detach(expandStep, expandSize, false);
			header.freespaceSize -= expandSize;
			beMain.freespaceFrom += expandSize;
		}
		
		private void split()
		{
			hashValueBase &= (~( 1 << header.hashValueBitUsed));
			Bucket bucketNew =  new Bucket(bufferManager.allocPages(1, false), hashValueBase | ( 1 << header.hashValueBitUsed) );
			header.hashValueBitUsed++;
			bucketNew.init(header.hashValueBitUsed);
		
			int hashValueBitDiff = meta.getHashValueBitUsed() - bucketNew.header.hashValueBitUsed;
			for(int i = 0; i < ( 1 << hashValueBitDiff ); ++i)
			{
				int newPageIndex = (bucketNew.hashValueBase % ( 1 << bucketNew.header.hashValueBitUsed ))
					| (i << bucketNew.header.hashValueBitUsed);
				hashTable.setBucketPageIndex(newPageIndex, bucketNew.pvMain.range.from);
			}
			
			List<byte[]> keysTrans = enumKeys();			
			for(byte[] key : keysTrans)
			{
				if( getHashCode(key) % ( 1 << bucketNew.header.hashValueBitUsed) == bucketNew.hashValueBase % ( 1 << bucketNew.header.hashValueBitUsed) )
				{
					byte[] value = get(key);
					bucketNew.put(key, value);
					del(key);
				}
			}
		}
		
		private List<byte[]> enumKeys()
		{
			List<byte[]> keys = new ArrayList<byte[]>();
			int index = 0;
			RecordEntry entry = new RecordEntry();
			while( index < header.recordEntryCount )
			{
				getRecordEntry(index, entry);
				if( entry.keyPrefixLength > 0 )
				{
					keys.add(entry.keyPrefix);						
				}
				else
				{
					Address addr = entry;
					while( true )
					{
						Pointer p = getPointer(addr);
						LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
						p.getLinkedRecordHeader(linkedRecordHeader);						
						keys.add(linkedRecordHeader.key);
						if( linkedRecordHeader.isNull() )
							break;
						addr = linkedRecordHeader;
					}
				}
				++index;
			}
			return keys;
		}
		
		class EntryImp implements TableEntry<byte[], byte[]> // TODO
		{
			
			EntryImp(byte[] key, byte[] val)
			{
				this.key = key;
				this.val = val;
			}

			@Override
			public byte[] getKey()
			{
				return key;
			}

			@Override
			public byte[] getValue()
			{
				return val;
			}
			
			byte[] key;
			byte[] val;
		}
		
		private List<TableEntry<byte[], byte[]>> enumRecords()
		{
			List<TableEntry<byte[], byte[]>> records = new ArrayList<TableEntry<byte[], byte[]>>();
			int index = 0;
			RecordEntry entry = new RecordEntry();
			while( index < header.recordEntryCount )
			{
				getRecordEntry(index, entry);
				if( entry.keyPrefixLength > 0 ) // short key
				{
					byte[] key = entry.keyPrefix;
					if( entry.dataLength == 0 )
						records.add(new EntryImp(key, new byte[0]));
					else
					{
						byte[] val = getPointer(entry).getValue();
						records.add(new EntryImp(key, val));
					}						
				}
				else
				{
					Address addr = entry;
					while( true )
					{
						Pointer p = getPointer(addr);
						LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
						p.getLinkedRecordHeader(linkedRecordHeader);
						byte[] key = linkedRecordHeader.key;
						byte[] val = p.getLinkedRecordValue(key);
						records.add(new EntryImp(key, val));
						if( linkedRecordHeader.isNull() )
							break;
						addr = linkedRecordHeader;
					}
				}
				++index;
			}
			return records;
		}
		
		private EntryCursor<BlockEntry> searchBlockByFreespace(final int dataLength)
		{
			if( beMain.getFreespaceSize() >= dataLength )
				return new EntryCursor<BlockEntry>(0, beMain);
			
			BlockEntry entry = new BlockEntry();
			for(int i = 1; i < header.blockCount; ++i)
			{
				getBlockEntry(i, entry);
				if( entry.getFreespaceSize() >= dataLength )
					return new EntryCursor<BlockEntry>(i, entry);
			}
			return null;
		}
		
		private EntryCursor<BlockEntry> searchBlockByValuePointer(final Address addr)
		{
			if( beMain.containsPage(addr.pageIndex) )
				return new EntryCursor<BlockEntry>(0, beMain);
			
			BlockEntry entry = new BlockEntry();
			for(int i = 1; i < header.blockCount; ++i)
			{
				getBlockEntry(i, entry);
				if( entry.containsPage(addr.pageIndex) )
					return new EntryCursor<BlockEntry>(i, entry);
			}
			return null;
		}
		
		private void updateRecordOffsetByRange(int blockPageIndex, int offset, int valueLength)
		{
			RecordEntry re = new RecordEntry();
			Address addr;
			for(int i = 0; i < header.recordEntryCount; ++i)
			{				
				getRecordEntry(i, re);
				addr = re;
				
				if( addr.pageIndex >= blockPageIndex && (addr.pageIndex - blockPageIndex) * Page.SIZE + addr.pageOffset < offset 
						&& ! ( addr.pageIndex == blockPageIndex && addr.pageOffset == 0 ) )
				{
					re.advance(valueLength);
					setRecordEntry(i, re);
				}
				
				if( ! re.isShortKey() && re.keyPrefixLength != (byte)-1 )
				{
					LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
					while( true )
					{
						Pointer p = getPointer(addr);
						p.getLinkedRecordHeader(linkedRecordHeader);
						if( linkedRecordHeader.isNull() )
							break;
						addr = linkedRecordHeader;
						
						if( addr.pageIndex >= blockPageIndex && (addr.pageIndex - blockPageIndex) * Page.SIZE + addr.pageOffset < offset
							&& ! ( addr.pageIndex == blockPageIndex && addr.pageOffset == 0 ) )
						{
							addr.advance(valueLength);
							p.putValuePointer(addr);
							p.setDirty();
						}
					}
				}
			}
		}
		
		private void updateRecordOffsetByRange1(int blockPageIndex, int offset, int valueLength)
		{
			RecordEntry re = new RecordEntry();
			Address addr;
			for(int i = 0; i < header.recordEntryCount; ++i)
			{				
				getRecordEntry(i, re);
				addr = re;
				
				if( addr.pageIndex >= blockPageIndex && (addr.pageIndex - blockPageIndex) * Page.SIZE + addr.pageOffset < offset 
						&& ! ( addr.pageIndex == blockPageIndex && addr.pageOffset == 0 ) )
				{
					re.disadvance(valueLength);
					setRecordEntry(i, re);
				}
				
				if( ! re.isShortKey() && re.keyPrefixLength != (byte)-1 )
				{
					LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
					while( true )
					{
						Pointer p = getPointer(addr);
						p.getLinkedRecordHeader(linkedRecordHeader);
						if( linkedRecordHeader.isNull() )
							break;
						addr = linkedRecordHeader;
						
						if( addr.pageIndex >= blockPageIndex && (addr.pageIndex - blockPageIndex) * Page.SIZE + addr.pageOffset < offset
							&& ! ( addr.pageIndex == blockPageIndex && addr.pageOffset == 0 ) )
						{
							addr.disadvance(valueLength);
							p.putValuePointer(addr);
							p.setDirty();
						}
					}
				}
			}
		}
		
		private Pointer getPointer(final Address addr)
		{
			return new Pointer(addr, bufferManager.findPages(addr.getPageRange()));
		}
		

		@Override
		public Iterator<TableEntry<byte[], byte[]>> iterator()
		{
			return enumRecords().iterator();
		}
		
		public byte[] get(final byte[] key)
		{			
			EntryCursor<RecordEntry> recFound = searchRecordByKey(key);
			if( recFound == null )
				return null;
			Address addr = getDataAddress(recFound.entry, key);
			if( addr == null )
				return null;
			if( recFound.entry.dataLength == 0 )
				return new byte[0];
			Pointer p = getPointer(addr);
			return ( RecordEntry.isShortKey(key.length) ) ? p.getValue() : p.getLinkedRecordValue(key);
		}
		
		public void del(final byte[] key)
		{
			// 1. find record
			EntryCursor<RecordEntry> recFound = searchRecordByKey(key); 
			if( recFound == null )
				return;
			int[] levelVP = new int[1];
			Pointer[] vsLast = new Pointer[1];
			Address addr = getDataAddress(recFound.entry, key, levelVP, vsLast);
			if( addr == null )
				return;
						
			// 2. update record index table
			header.freespaceSize += addr.dataLength;
			header.recordCount--;
			if( RecordEntry.isShortKey(key.length) || recFound.entry.keyPrefixLength == (byte)-1 )
			{
				header.recordEntryCount--;
				if( header.recordEntryCount != 0 )
				{
					RecordEntry reMove = new RecordEntry();
					getRecordEntry(header.recordEntryCount, reMove);
					setRecordEntry(recFound.index, reMove);
				}
			}
			else
			{
				LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
				getPointer(addr).getLinkedRecordHeader(linkedRecordHeader);
				if( levelVP[0] > 1 ) // no need update recordIndexFound.entry
				{
					vsLast[0].putValuePointer(linkedRecordHeader);
					vsLast[0].setDirty();
				}
				else if( levelVP[0] == 1 ) // the second node, vs is the first node
				{
					vsLast[0].putValuePointer(linkedRecordHeader);
					vsLast[0].setDirty();
					if( linkedRecordHeader.isNull() )
					{
						addr = new Address(addr);
						recFound.entry.keyPrefixLength = (byte)-1;
						setRecordEntry(recFound.index, recFound.entry);
					}
				}
				else // levelVP[0] == 0 the first node
				{
					addr = new Address(addr);
					recFound.entry.set(linkedRecordHeader);
					getPointer(linkedRecordHeader).getLinkedRecordHeader(linkedRecordHeader);
					if( linkedRecordHeader.isNull() )
						recFound.entry.keyPrefixLength = (byte)-1;
					setRecordEntry(recFound.index, recFound.entry);
				}
			}
			
			// 3. find block contains value
			EntryCursor<BlockEntry> becFound = searchBlockByValuePointer(addr);
			if( becFound == null )
				throw new Error("DBError: impossible:(");
				
			// 4. move other record in the block, update block index
			int blockOffset = (addr.pageIndex - becFound.entry.pageIndex) * Page.SIZE + addr.pageOffset;
			if( addr.dataLength != 0 )
			{
				if( blockOffset == 0 )
					becFound.entry.freespaceFrom = 0;
				else if( blockOffset == becFound.entry.freespaceTo )
					becFound.entry.freespaceTo += addr.dataLength;
				else if( blockOffset < becFound.entry.freespaceTo )
					throw new Error("impossible");
				else
				{				
					PageView pvBlockFound = bufferManager.findPages(becFound.entry.getPageRange());
					pvBlockFound.copyRangeOf(becFound.entry.freespaceTo + addr.dataLength, pvBlockFound
						, becFound.entry.freespaceTo, blockOffset - becFound.entry.freespaceTo);
					pvBlockFound.setDirty(becFound.entry.getPageRange()); // TODO
					updateRecordOffsetByRange(becFound.entry.pageIndex, blockOffset , addr.dataLength);
					becFound.entry.freespaceTo += addr.dataLength;
				}
			}
				
			// 5. update block index table, remove empty block				
			becFound.entry.recordCount--;
			if( becFound.entry != beMain )
			{
				if( becFound.entry.recordCount == 0 )
				{
					header.pageCount -= becFound.entry.pageCount;
					header.blockCount--;
					bufferManager.freePages(becFound.entry.getPageRange());
					header.freespaceSize -= becFound.entry.pageCount * Page.SIZE;
					// TODO shrink
				
					if( header.blockCount > 1 )
					{
						BlockEntry beMove = new BlockEntry();
						getBlockEntry(header.blockCount, beMove);
						setBlockEntry(becFound.index, beMove);
					}
				}
				else
					setBlockEntry(becFound.index, becFound.entry);
			}			
						
			// 6. save change
			setBlockEntry(0, beMain);
			setHeader(header);		
			pvMain.setAllDirty(); // TODO
			meta.decRecordCount();
		}
		
		private void update(final byte[] key, final byte[] value, final EntryCursor<RecordEntry> recFound, final Address addr)
		{
			final int lengthDiff = value.length - addr.dataLength;
			
			EntryCursor<BlockEntry> becFound = searchBlockByValuePointer(addr);
			if( becFound == null )
				throw new Error("DBError: impossible:(");
			final PageView pvOld = bufferManager.findPages(becFound.entry.getPageRange());
			final Pointer pOld = new Pointer(addr, pvOld);
			final int blockOffset = ( addr.pageIndex - becFound.entry.pageIndex ) * Page.SIZE + addr.pageOffset;
			if( lengthDiff == 0 )
			{				
				if( RecordEntry.isShortKey(key.length) )
					pOld.putData(value);
				else
				{
					LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
					pOld.getLinkedRecordHeader(linkedRecordHeader);
					pOld.putLinkedRecord(linkedRecordHeader, value);
				}
				pvOld.setAllDirty(); // TODO
			}
			else if( lengthDiff < 0 )
			{
				if( RecordEntry.isShortKey(key.length) )
				{
					if( blockOffset == 0 ) // cover record
					{
						Address addrNew = new Address(addr);
						addrNew.dataLength = value.length;
						Pointer pNew = new Pointer(addrNew, pvOld);
						pNew.putData(value);
						pvOld.setAllDirty();
						becFound.entry.freespaceFrom = addrNew.dataLength;
						setBlockEntry(becFound.index, becFound.entry);
						recFound.entry.set(addrNew);
						setRecordEntry(recFound.index, recFound.entry);
					}
					else if( blockOffset == becFound.entry.freespaceTo )
					{
						Address addrNew = new Address(addr);
						addrNew.advance(-lengthDiff);
						addrNew.dataLength = value.length;
						Pointer pNew = new Pointer(addrNew, pvOld);
						pNew.putData(value);
						pvOld.setAllDirty();
						becFound.entry.freespaceTo -= lengthDiff;
						setBlockEntry(becFound.index, becFound.entry);
						recFound.entry.set(addrNew);
						setRecordEntry(recFound.index, recFound.entry);
					}
					else
					{
						Address addrNew = new Address(addr);
						addrNew.advance(-lengthDiff);
						addrNew.dataLength = value.length;
						Pointer pNew = new Pointer(addrNew, pvOld);
						pNew.putData(value);
						pvOld.setAllDirty();
						becFound.entry.freespaceTo -= lengthDiff;
						setBlockEntry(becFound.index, becFound.entry);
						recFound.entry.set(addrNew);
						setRecordEntry(recFound.index, recFound.entry);
						pvOld.copyRangeOf(becFound.entry.freespaceTo, pvOld
								, becFound.entry.freespaceTo + lengthDiff, blockOffset - becFound.entry.freespaceTo-  lengthDiff);
						updateRecordOffsetByRange(becFound.entry.pageIndex, blockOffset, -lengthDiff);
					}
				}
				else
				{
					del(key);
					put(key, value);
					return; // TODO
				}
			}
			else // lengthDiff > 0
			{
				if( RecordEntry.isShortKey(key.length) )
				{
					if( lengthDiff > becFound.entry.getFreespaceSize() )
					{
						del(key);
						put(key, value);
						return; // TODO
					}
					else
					{
						if( blockOffset == 0 ) // cover record
						{
							Address addrNew = new Address(addr);
							addrNew.dataLength = value.length;
							Pointer pNew = new Pointer(addrNew, pvOld);
							pNew.putData(value);
							pvOld.setAllDirty();
							becFound.entry.freespaceFrom = addrNew.dataLength;
							setBlockEntry(becFound.index, becFound.entry);
							recFound.entry.set(addrNew);
							setRecordEntry(recFound.index, recFound.entry);
						}
						else if( blockOffset == becFound.entry.freespaceTo )
						{
							Address addrNew = new Address(addr);
							addrNew.disadvance(lengthDiff);
							addrNew.dataLength = value.length;
							Pointer pNew = new Pointer(addrNew, pvOld);
							pNew.putData(value);
							pvOld.setAllDirty();
							becFound.entry.freespaceTo -= lengthDiff;
							setBlockEntry(becFound.index, becFound.entry);
							recFound.entry.set(addrNew);
							setRecordEntry(recFound.index, recFound.entry);
						}
						else
						{
							Address addrNew = new Address(addr);
							addrNew.disadvance(lengthDiff);
							addrNew.dataLength = value.length;
							Pointer pNew = new Pointer(addrNew, pvOld);
							pvOld.copyRangeOf(becFound.entry.freespaceTo - lengthDiff, pvOld
									, becFound.entry.freespaceTo, blockOffset - becFound.entry.freespaceTo);
							updateRecordOffsetByRange1(becFound.entry.pageIndex, blockOffset, lengthDiff);
							pNew.putData(value);
							pvOld.setAllDirty();
							becFound.entry.freespaceTo -= lengthDiff;
							setBlockEntry(becFound.index, becFound.entry);
							recFound.entry.set(addrNew);
							setRecordEntry(recFound.index, recFound.entry);							
						}
					}
				}
				else
				{
					del(key);
					put(key, value);
					return; // TODO
				}
			}
			
			setBlockEntry(0, beMain);
			header.freespaceSize -= lengthDiff;
			setHeader(header);
			pvMain.setAllDirty(); // TODO
		}
		
		public void put(final byte[] key, final byte[] value)
		{
			assureBlockIndexTableNotFull();
			assureRecordIndexTableNotFull();
			assureBlockIndexTableNotFull();
			EntryCursor<RecordEntry> recFound = searchRecordByKey(key);
			if( recFound != null )
			{
				Address addr = getDataAddress(recFound.entry, key);
				if( addr != null )
				{
					update(key, value, recFound, addr);
					return;
				}
			}			

			Address addrInsert = new Address();
			PageView pvInsert = pvMain;
			addrInsert.dataLength = RecordEntry.isShortKey(key.length)
									? value.length : LinkedRecordHeader.KEY_LENGTH_OFFSET + 4 + key.length + value.length;
			
			EntryCursor<BlockEntry> becFound = searchBlockByFreespace(addrInsert.dataLength);

			if( becFound == null )
			{
				int pageCountNew = Page.getCount(addrInsert.dataLength);
				pvInsert = bufferManager.allocPages(pageCountNew, false);
				
				BlockEntry beNew = new BlockEntry(pvInsert.range).pushRecord(addrInsert.dataLength);
				pvInsert.setAllDirty();
				
				addrInsert.pageIndex = pvInsert.range.from + beNew.freespaceTo / Page.SIZE;
				addrInsert.pageOffset = (short)(beNew.freespaceTo % Page.SIZE);
				setBlockEntry(header.blockCount, beNew);
				header.blockCount++;
				header.freespaceSize += beNew.pageCount * Page.SIZE;
				header.pageCount += beNew.pageCount;
			}
			else
			{
				if( becFound.entry == beMain )
				{
					becFound.entry.pushRecord(addrInsert.dataLength);
					addrInsert.pageIndex = pvMain.range.from + beMain.freespaceTo / Page.SIZE;
					addrInsert.pageOffset = (short)(beMain.freespaceTo % Page.SIZE);
					if( addrInsert.dataLength == 0 )
					{
						if( addrInsert.pageIndex >= pvMain.range.to ) // TODO
						{
							addrInsert.pageIndex--;
							addrInsert.pageOffset = Page.SIZE;
						}
					}
				}
				else
				{
					boolean bCover = true;
					if( becFound.entry.freespaceFrom == 0 )
						becFound.entry.coverRecord(addrInsert.dataLength);
					else
					{
						becFound.entry.pushRecord(addrInsert.dataLength);
						bCover = false;
					}
					pvInsert = bufferManager.findPages(becFound.entry.getPageRange());
					pvInsert.setAllDirty(); // TODO
					setBlockEntry(becFound.index, becFound.entry);
					
					if( bCover )
					{
						addrInsert.pageIndex = pvInsert.range.from;
						addrInsert.pageOffset = 0;
					}
					else
					{
						addrInsert.pageIndex = pvInsert.range.from + becFound.entry.freespaceTo / Page.SIZE;
						addrInsert.pageOffset = (short)(becFound.entry.freespaceTo % Page.SIZE);
						if( addrInsert.dataLength == 0 )
						{
							if( addrInsert.pageIndex >= pvInsert.range.to ) // TODO
							{
								addrInsert.pageIndex--;
								addrInsert.pageOffset = Page.SIZE;
							}
						}
					}
				}
			}		
			Pointer pInsert = new Pointer(addrInsert, pvInsert);
			
			if( RecordEntry.isShortKey(key.length) ) // insert short key
			{
				pInsert.putData(value);											
				setRecordEntry(header.recordEntryCount, new RecordEntry(addrInsert, key));				
				header.recordEntryCount++;
			}
			else if( recFound == null ) // insert long key
			{
				LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader();
				linkedRecordHeader.setNull();
				linkedRecordHeader.keyLength = key.length;
				linkedRecordHeader.key = key.clone();
				pInsert.putLinkedRecord(linkedRecordHeader, value);			
				
				setRecordEntry(header.recordEntryCount, new RecordEntry(addrInsert, (byte)-1, key));
				header.recordEntryCount++;
			}
			else // link long key
			{
				LinkedRecordHeader linkedRecordHeader = new LinkedRecordHeader(recFound.entry);
				linkedRecordHeader.keyLength = key.length;
				linkedRecordHeader.key = key.clone();
				pInsert.putLinkedRecord(linkedRecordHeader, value);			
					
				recFound.entry.keyPrefixLength = (byte)-2;
				//recordIndexFound.entry.keyPrefix = nochange;
				recFound.entry.set(addrInsert);
				setRecordEntry(recFound.index, recFound.entry);
			}
			setBlockEntry(0, beMain);
			header.freespaceSize -=  addrInsert.dataLength;
			header.recordCount++;
			setHeader(header);
			pvMain.setAllDirty(); // TODO
			meta.incRecordCount();
		}

		public PageView pvMain;
		public int hashValueBase;
		public BucketHeader header;
		public BlockEntry beMain;
	}
	
	private class HashTable
	{
		// new
		public HashTable(int hashValueBitUsed)
		{
			entryCount = 1 << hashValueBitUsed;
			int hashTableSize = entryCount * HASH_ENTRY_SIZE;
			int pageCount = 1;
			while( pageCount * Page.SIZE < hashTableSize) pageCount = pageCount * 2;
			pv = bufferManager.allocPages(pageCount, true);
			pv.setDirty(new Range(pv.range.from, pv.range.from + ( 4 * entryCount) / Page.SIZE));
			for(int i = 0; i < entryCount; ++i)
				pv.setInt(i * 4, INVALID_BUCKET_PAGEINDEX);
		}
		
		// load
		public HashTable(int hashValueBitUsed, int index)
		{
			entryCount = 1 << hashValueBitUsed;
			int hashTableSize = entryCount * HASH_ENTRY_SIZE;
			int pageCount = 1;
			while( pageCount * Page.SIZE < hashTableSize) pageCount = pageCount * 2;
			pv = bufferManager.findPages(new Range(index, index + pageCount));
			pv.setAllDirty(); // TODO
		}
		
		public void setBucketPageIndex(int hashIndex, int pageIndex)
		{
			pv.setInt(hashIndex * 4, pageIndex);
			pv.setDirty(pv.range.from + hashIndex * HASH_ENTRY_SIZE / Page.SIZE);
		}
		
		public byte[] get(byte[] key)
		{
			int hashIndex = getHashCode(key) % entryCount;
			int bucketIndex = pv.getInt(hashIndex * 4);
			if( bucketIndex == INVALID_BUCKET_PAGEINDEX )
				return null;
			return loadBucket(bucketIndex, hashIndex).get(key);
		}
		
		public void del(byte[] key)
		{
			int hashIndex = getHashCode(key) % entryCount;
			int bucketIndex = pv.getInt(hashIndex * 4);
			if( bucketIndex == INVALID_BUCKET_PAGEINDEX )
				return;
			loadBucket(bucketIndex, hashIndex).del(key);
		}
		
		public void put(byte[] key, byte[] value)
		{
			int hashIndex = getHashCode(key) % entryCount;
			int bucketIndex = pv.getInt(hashIndex * 4);
			Bucket bucket;
			if( bucketIndex == INVALID_BUCKET_PAGEINDEX )
				bucket = newBucket(hashIndex);
			else
				bucket = loadBucket(bucketIndex, hashIndex);
			
			if( ! bucket.isOverflow() || meta.isFullHashed() && bucket.header.hashValueBitUsed == meta.getHashValueBitUsed())
			{
				bucket.put(key, value);
				return;
			}			
			do
			{
				if( bucket.header.hashValueBitUsed < meta.getHashValueBitUsed() )
					bucket.split();
				else if( meta.isFullHashed() )
					break;
				else
					expand();
			} while( bucket.isOverflow() );
			
			put(key, value);
		}
		
		public void expand()
		{
			PageView pvNew = bufferManager.allocPages(pv.range.size() * 2, true);
			pvNew.setAllDirty();
			
			pvNew.copyRangeOf(0, pv, 0, pv.getDataSize());
			pvNew.copyRangeOf(pv.getDataSize(), pv, 0, pv.getDataSize());
			
			entryCount *= 2;
			bufferManager.freePages(pv.range);
			pv = pvNew;
			meta.setHashValueBitUsed(meta.getHashValueBitUsed() + 1);
			meta.setHashTablePageIndex(pv.range.from);
			bufferManager.setPinRange(pv.range);
		}
		
		private Bucket loadBucket(int pageIndex, int hashIndex)
		{
			return new Bucket(bufferManager.findPage(pageIndex), hashIndex).load();
		}
		
		private Bucket newBucket(int hashIndex)
		{
			Bucket bucket =  new Bucket(bufferManager.allocPages(1, false), hashIndex);
			pv.setInt(hashIndex * 4, bucket.pvMain.range.from);
			pv.setDirty(pv.range.from + hashIndex * HASH_ENTRY_SIZE / Page.SIZE);
			return bucket.init(meta.getHashValueBitUsed());
		}
		
		public int entryCount;
		public PageView pv;
	}
	
	public HashTableImp(String fileName, boolean bStandAlone)
	{
		super(fileName, bStandAlone);
	}

	@Override
	public byte[] get(byte[] key)
	{		
		return hashTable.get(key);
	}

	@Override
	public void put(byte[] key, byte[] val)
	{
		hashTable.put(key, val);
	}

	@Override
	public void del(byte[] key)
	{
		//System.out.println("TableImp(" + name + ").del");
		hashTable.del(key);
	}
	
	private static int getHashCode(final byte[] key) // temp
	{
		int seed = 131;
		int hash = 0;
		for(byte b : key)
		{
			hash = hash * seed + (int)b;
		}
		return hash & 0x7fFFffFF;
	}
	
	@Override
	public void loadTable()
	{
		meta = new TableMeta(bufferManager.findPage(BufferManager.TABLE_META_PAGEINDEX));
		if( meta.getHashValueBitUsed() == 0 ) // init table meta
		{
			meta.setHashValueBitUsed(INIT_HASHVALUE_BITUSED);
			hashTable = new HashTable(meta.getHashValueBitUsed());
			meta.setHashTablePageIndex(hashTable.pv.range.from);
		}
		else
		{
			hashTable = new HashTable(meta.getHashValueBitUsed(), meta.getHashTablePageIndex());
		}				
		bufferManager.setPinRange(hashTable.pv.range);
		//meta.dump();
	}
	
	class Iter implements Iterator<TableEntry<byte[], byte[]>>
	{
		
		Iter()
		{
			Set<Integer> bucketPageIndices = new HashSet<Integer>();
			for(int i = 0; i < hashTable.entryCount; ++i )
			{
				int pageIndex = hashTable.pv.getInt(i * 4);
				if( pageIndex == INVALID_BUCKET_PAGEINDEX )
					continue;
				bucketPageIndices.add(pageIndex);
			}
			iterBucketIndex = bucketPageIndices.iterator();
		}

		@Override
		public boolean hasNext()
		{
			if( iterCurBucket != null && iterCurBucket.hasNext() )
				return true;
			while( iterBucketIndex.hasNext() )
			{
				Bucket bucket = hashTable.loadBucket(iterBucketIndex.next(), 0);
				iterCurBucket = bucket.iterator();
				if( iterCurBucket.hasNext() )
					return true;
			}
			return false;
		}

		@Override
		public TableEntry<byte[], byte[]> next()
		{
			return iterCurBucket.next();
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
		Iterator<Integer> iterBucketIndex;
		Iterator<TableEntry<byte[], byte[]>> iterCurBucket;
	}

	@Override
	public Iterator<TableEntry<byte[], byte[]>> iterator()
	{
		return new Iter();
	}
	
	public boolean testConsistency()
	{
		if( hashTable.pv.range().from != meta.getHashTablePageIndex() )
			return false;
		if( ( 1 << meta.getHashValueBitUsed() ) != hashTable.pv.range().size() * Page.SIZE / HASH_ENTRY_SIZE )
			return false;
		int pageUsedCount = bufferManager.getMeta().pageCount - 2 - bufferManager.getMeta().freePageBitmapPageCount
									- bufferManager.getMeta().getFreePageCount() - hashTable.pv.range.size();
		Set<Integer> bucketPages = new HashSet<Integer>();
		Set<Integer> bucketPageIndices = new HashSet<Integer>();
		for(int i = 0; i < hashTable.entryCount; ++i )
		{
			int pageIndex = hashTable.pv.getInt(i * 4);
			if( pageIndex == INVALID_BUCKET_PAGEINDEX )
				continue;
			bucketPageIndices.add(pageIndex);
		}
		BucketStat bucketStat = new BucketStat();
		int sumRecordCount = 0;
		for(int i : bucketPageIndices)
		{
			Bucket bucket = new Bucket(bufferManager.findPage(i), 0); // TODO
			bucket.load();
			if( bucket.pvMain.range.from != i )
				return false;
			if( ! bucket.testConsistency(bucketStat) )
				return false;
			bucketPages.addAll(bucketStat.pageMap.keySet());
			sumRecordCount += bucketStat.recordCount;
		}
		if( pageUsedCount != bucketPages.size() )
			return false;
		if( sumRecordCount != meta.getRecordCount() )
			return false;
		return bufferManager.testConsistency();
	}
	
	private TableMeta meta;
	private HashTable hashTable;
}

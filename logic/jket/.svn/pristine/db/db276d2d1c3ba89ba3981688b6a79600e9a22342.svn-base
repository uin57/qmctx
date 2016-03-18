
package ket.kdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Comparator;

import org.apache.log4j.Logger;

class BufferManager
{
	public static final int META_PAGEINDEX = 0;
	public static final int TABLE_META_PAGEINDEX = 1;
	
	private static final int FREEPAGE_BITMAP_INIT_PAGEINDEX = 2;
	private static final int INVALID_FREEPAGE_SIZE_LEVEL = -1;
	private static final int INVALID_FREEPAGE_PAGEINDEX = META_PAGEINDEX;		
	
	private final class FreePageBitmap extends PageBufferManaged
	{
		public FreePageBitmap(final Range range)
		{
			super(range);
		}
	
		public void setFree(final Range range)
		{
			setBits(range);
			setDirty(new Range(this.range.from + range.from/(Page.SIZE*8), this.range.from + 1 + (range.to-1)/(Page.SIZE*8)));
		}
		
		public void setUsed(final Range range)
		{
			clearBits(range);
			setDirty(new Range(this.range.from + range.from/(Page.SIZE*8), this.range.from + 1 + (range.to-1)/(Page.SIZE*8)));
		}
		
		public boolean isFree(final Range range)
		{
			for(int i : range)
			{
				if( isBitClear(i) )
					return false;
			}
			return true;
		}		
		
		public int getFreePageCount()
		{
			int sum = 0;
			for(int i = 0; i < meta.pageCount; ++i)
			{
				if( isFree(new Range(i, i+1)) )
					sum++;
			}
			return sum;
		}
		
		public Range findMaxFreeRange(final Range range)
		{
			int from = range.from;
			int to = range.to;
			for( ;from > 0; --from)
			{
				if( isBitClear(from - 1) )
					break;
			}			
			for( ; to < meta.pageCount; ++to)
			{
				if( isBitClear(to) )
					break;
			}				
			return new Range(from, to);
		}
	}
	
	private static class FreePageHeadWrapper
	{		
		private static final int LEVEL_OFFSET = 0;
		private static final int PREV_PAGEINDEX_OFFSET = 4;
		private static final int NEXT_PAGEINDEX_OFFSET = 8;
		
		public FreePageHeadWrapper(PageView pv)
		{
			this.pv = pv;
		}
		
		public int getLevel()
		{			
			return pv.getInt(LEVEL_OFFSET);
		}
		public void setLevel(int level)
		{
			pv.setInt(LEVEL_OFFSET, level);
		}
		
		public int getPrev()
		{
			return pv.getInt(PREV_PAGEINDEX_OFFSET);
		}
		public void setPrev(int index)
		{
			pv.setInt(PREV_PAGEINDEX_OFFSET, index);
		}
		
		public int getNext()
		{
			return pv.getInt(NEXT_PAGEINDEX_OFFSET);
		}
		
		public void setNext(int index)
		{
			pv.setInt(NEXT_PAGEINDEX_OFFSET, index);
		}
		
		private final PageView pv;
	}	
	
	public BufferManager(String name, String fileName, boolean bStandAlone)
	{
		this.bStandAlone = bStandAlone;
		dbFile = DBFile.newInstance(new File(fileName));
	}
	
	public void load()
	{		
		pageBuffers = new RangeTree<PageBufferManaged>();
		pinRange = null;
		
		metaPage = new PageBufferManaged(new Range(META_PAGEINDEX, META_PAGEINDEX + 1));
		meta = new Meta();
		
		if( metaPage.tryLoad(dbFile) )
		{
			meta.load(metaPage.clear());
			loadPageBuffer(new PageBufferManaged(new Range(TABLE_META_PAGEINDEX, TABLE_META_PAGEINDEX + 1)));
			freePageBitmap = new FreePageBitmap(new Range(meta.freePageBitmapPageIndex, meta.freePageBitmapPageIndex + meta.freePageBitmapPageCount));
			loadPageBuffer(freePageBitmap);
		}		
		else
		{
			meta.pageCount = 3;
			meta.freePageBitmapPageIndex = FREEPAGE_BITMAP_INIT_PAGEINDEX;
			meta.freePageBitmapPageCount = 1;
			Arrays.fill(meta.freePagesListHeadPageIndex, INVALID_FREEPAGE_PAGEINDEX);
			Arrays.fill(meta.freePagesCount, 0);
			freePageBitmap = new FreePageBitmap(new Range(FREEPAGE_BITMAP_INIT_PAGEINDEX, FREEPAGE_BITMAP_INIT_PAGEINDEX + 1));
			pageBuffers.add(freePageBitmap);
			freePageBitmap.setAllDirty();
			PageBufferManaged pbTableMeta = new PageBufferManaged(new Range(TABLE_META_PAGEINDEX, TABLE_META_PAGEINDEX + 1));
			pageBuffers.add(pbTableMeta);
			pbTableMeta.setAllDirty();	
		}
		//meta.dump();
	}
	
	public void clear()
	{
		dbFile.clear();
		load();
	}
	
	private void loadPageBuffer(PageBufferManaged pbm)
	{
		pbm.load(dbFile);
		pageBuffers.add(pbm);
	}
	
	public List<PageView> gatherDirtyPages()
	{
		List<PageView> pvsDirty = new ArrayList<PageView>();
		//
		meta.save(metaPage.clear());
		pvsDirty.add(metaPage);		
		
		for(PageBufferManaged pbm : pageBuffers)
		{
			Range range = null;
			for(int i : pbm.range)
			{
				if( ! pbm.isDirty(i) )
				{
				}
				else if( range == null )
					range = new Range(i, i + 1);
				else if( i == range.to )
					range = new Range(range.from, range.to + 1);
				else
				{
					pvsDirty.add(new PageViewSection(range, pbm));
					range = new Range(i, i + 1);
				}
			}
			if( range != null )
			{
				pvsDirty.add(new PageViewSection(range, pbm));
			}
			pbm.clearAllDirty();
		}
		//
		return pvsDirty;
	}
	
	public List<PageViewWithReader> gatherDirtyPagesWithReader(final int cacheLow, Logger logger)
	{
		long now = System.currentTimeMillis();
		List<PageViewWithReader> pvsDirty = new ArrayList<PageViewWithReader>();
		//
		meta.save(metaPage.clear());
		CloneOnWrite cowmeta = metaPage.getCOW();
		cowmeta.reset(metaPage, 1);
		pvsDirty.add(new PageViewWithReader(cowmeta));
		
		class PageInBuffer
		{
			public PageInBuffer(final int pageIndex, final PageBufferManaged pbm)
			{
				this.pageIndex = pageIndex;
				this.pbm = pbm;
			}
			
			public final int pageIndex;
			public final PageBufferManaged pbm;
		}
		PriorityQueue<PageInBuffer> pqueue = null;
		int pageCountReleaseMax = 0;
		
		if( cacheLow > 0 && ! bStandAlone )
		{
			// 1. load target size from config file
			int cachePageCount = Page.getCount(cacheLow * 1024 * 1024);
		
			int pinPageCount = 1 + 1 + freePageBitmap.range.size() + (( pinRange == null ) ? 0 : pinRange.size()); // TODO
			if( cachePageCount < pinPageCount )
				cachePageCount = pinPageCount;
				
			// 2. calculate page count need to be released
			int pageCountNow = pageBuffers.getPageCount() + 1; // add meta page
			pageCountReleaseMax = pageCountNow - cachePageCount;
			if( pageCountReleaseMax >0 )
			{
				pqueue
					= new PriorityQueue<PageInBuffer>(pageCountNow - 1, new Comparator<PageInBuffer>()
					{
						@Override
						public int compare(final PageInBuffer p1, final PageInBuffer p2)
						{
							long ld = p1.pbm.getAccessTime(p1.pageIndex) - p2.pbm.getAccessTime(p2.pageIndex);
							if( ld == 0 ) return 0;
							return ld < 0 ? -1 : 1;
						}
					});
			}
		}
		
		for(PageBufferManaged pbm : pageBuffers)
		{
			CloneOnWrite cow = pbm.getCOW();
			cow.reset(pbm, 1); // TODO
			int rcount = 0;
			Range range = null;
			for(int i : pbm.range)
			{
				if( ! pbm.isDirty(i) )
				{
					if( pqueue != null )
						pqueue.offer(new PageInBuffer(i, pbm));
				}
				else if( range == null )
					range = new Range(i, i + 1);
				else if( i == range.to )
					range = new Range(range.from, range.to + 1);
				else
				{
					++rcount;
					pvsDirty.add(new PageViewWithReader(cow));
					range = new Range(i, i + 1);
				}
			}
			if( range != null )
			{
				++rcount;
				pvsDirty.add(new PageViewWithReader(cow));
			}
			cow.reset(pbm, rcount);
			pbm.clearAllDirty();
		}
		//
		if( pqueue != null && ! pqueue.isEmpty() )
		{
			List<PageInBuffer> releaseLst = new ArrayList<PageInBuffer>(Math.max(pageCountReleaseMax, pqueue.size()));
			for(int i = 0; i < pageCountReleaseMax;)
			{
				PageInBuffer p = pqueue.poll();
				if( p == null )
					break;
				if( p.pageIndex == TABLE_META_PAGEINDEX || freePageBitmap.range.contains(p.pageIndex)
					|| pinRange != null && pinRange.contains(p.pageIndex) )
					continue;
				releaseLst.add(p);
				++i;
			}
		
			Collections.sort(releaseLst, new Comparator<PageInBuffer>()
										{
											@Override
											public int compare(final PageInBuffer p1, final PageInBuffer p2)
											{
												return p1.pageIndex - p2.pageIndex;
											}
										});		

			///
			class ReleasePageBuffer
			{
				PageBuffer pageBuffer;
				List<Integer> pages = new ArrayList<Integer>();
			}
			List<ReleasePageBuffer> releasePageBuffers = new ArrayList<ReleasePageBuffer>();
			ReleasePageBuffer lastReleasePageBuffer = new ReleasePageBuffer();
			for(PageInBuffer p : releaseLst)
			{
				PageBuffer pageBuffer = p.pbm;			
				if( pageBuffer == lastReleasePageBuffer.pageBuffer )
				{
					lastReleasePageBuffer.pages.add(p.pageIndex);
				}
				else
				{
					if( lastReleasePageBuffer.pageBuffer != null )
					{
						releasePageBuffers.add(lastReleasePageBuffer);
						lastReleasePageBuffer = new ReleasePageBuffer();
					}
					lastReleasePageBuffer.pageBuffer = pageBuffer;
					lastReleasePageBuffer.pages.add(p.pageIndex);
				}
			}
			if( lastReleasePageBuffer.pageBuffer != null )
			{
				releasePageBuffers.add(lastReleasePageBuffer);
			}
			if( ! releasePageBuffers.isEmpty() )
			{
				int releaseTotal = 0;
				int releaseReal = 0;
				for(ReleasePageBuffer rpb : releasePageBuffers)
				{
					releaseTotal += rpb.pages.size();
					if( rpb.pages.size() == rpb.pageBuffer.range.size() )
					{
						releaseReal += rpb.pages.size();
						pageBuffers.remove(rpb.pageBuffer);
					}
					else
					{
						//System.out.print(", release " + rpb.pages.size() + " != " +rpb.pageBuffer.range.size());
					// TODO
						//System.out.println("need release pages " + rpb.pages.size() + " in " + rpb.pageBuffer.range);
					}
				}
				//logger.debug(" release %d pages %2.1f%%, ", releaseReal, releaseReal * 100/(float)releaseTotal);
			}
		}
		logger.debug(" gather " + (System.currentTimeMillis() - now) + "(ms)");
		//
		return pvsDirty;
	}
	
	public void saveDirtyPagesWithReader(List<PageViewWithReader> pvsDirty)
	{
		for(PageViewWithReader pv : pvsDirty)
		{
			pv.save(dbFile);
			pv.release();
		}
		dbFile.sync();
	}
	
	public void saveDirtyPages(List<PageView> pvsDirty)
	{
		for(PageView pv : pvsDirty)
		{
			pv.save(dbFile);
		}
		dbFile.sync();
	}
	
	private void updateFreePageBitmap(int pageCountIncrement)
	{
		if( pageCountIncrement <= 0 )
			return; // TODO
		int freePageBitMapPageCount = (meta.pageCount - 1 ) / ( Page.SIZE * 8) + 1;
		if( freePageBitMapPageCount <= meta.freePageBitmapPageCount)
			return;		
		 // TODO reuse free pages ? or not
		while( freePageBitMapPageCount < (meta.pageCount + (freePageBitMapPageCount - meta.freePageBitmapPageCount) -1) / ( Page.SIZE * 8) + 1)
			++freePageBitMapPageCount;
		FreePageBitmap oldFreePageBitmap = freePageBitmap;		
		meta.freePageBitmapPageIndex = meta.pageCount;
		meta.pageCount += freePageBitMapPageCount;
		meta.freePageBitmapPageCount = freePageBitMapPageCount;
		freePageBitmap = new FreePageBitmap(new Range(meta.freePageBitmapPageIndex, meta.freePageBitmapPageIndex + meta.freePageBitmapPageCount));
		freePageBitmap.copyRangeOf(0, oldFreePageBitmap, 0, oldFreePageBitmap.getDataSize());
		pageBuffers.add(freePageBitmap);
		freePageBitmap.setAllDirty();
		freePages(oldFreePageBitmap.range);
	}
	
	private void updateFreePage(int index, int oldLevel, int newLevel)
	{
		PageView pageView = findPage(index);
		FreePageHeadWrapper freePageHeadWrapper = new FreePageHeadWrapper(pageView);
		// detach old
		if( oldLevel != INVALID_FREEPAGE_SIZE_LEVEL )
		{
			if( freePageHeadWrapper.getLevel() != oldLevel )
				throw new Error("DBError: incorrect free page level " + freePageHeadWrapper.getLevel() + " != " + oldLevel);
			int prevIndex = freePageHeadWrapper.getPrev();
			int nextIndex = freePageHeadWrapper.getNext();
			if( prevIndex == META_PAGEINDEX )
				meta.freePagesListHeadPageIndex[oldLevel] = nextIndex;
			else
			{
				PageView pvPrev = findPage(prevIndex);
				FreePageHeadWrapper prevFreePageHeadWrapper = new FreePageHeadWrapper(pvPrev);
				pvPrev.setDirty(prevIndex);
				prevFreePageHeadWrapper.setNext(nextIndex);
			}
			if( nextIndex != INVALID_FREEPAGE_PAGEINDEX )
			{
				PageView pvNext = findPage(nextIndex);
				FreePageHeadWrapper nextFreePageHeadWrapper = new FreePageHeadWrapper(pvNext);
				pvNext.setDirty(nextIndex);
				nextFreePageHeadWrapper.setPrev(prevIndex);
			}
			meta.freePagesCount[oldLevel]--;
		}
		// attach new
		if( newLevel != INVALID_FREEPAGE_SIZE_LEVEL )
		{
			freePageHeadWrapper.setLevel(newLevel);
			int firstIndex = meta.freePagesListHeadPageIndex[newLevel]; 
			if( firstIndex == INVALID_FREEPAGE_PAGEINDEX )
			{
				freePageHeadWrapper.setNext(INVALID_FREEPAGE_PAGEINDEX);
			}
			else
			{
				PageView pvFirst = findPage(firstIndex);
				FreePageHeadWrapper firstFreePageHeadWrapper = new FreePageHeadWrapper(pvFirst);
				pvFirst.setDirty(firstIndex);
				firstFreePageHeadWrapper.setPrev(index);
				freePageHeadWrapper.setNext(firstIndex);
			}
			freePageHeadWrapper.setPrev(META_PAGEINDEX);
			meta.freePagesListHeadPageIndex[newLevel] = index;
			meta.freePagesCount[newLevel]++;
			pageView.setDirty(index);
		}
	}

	private void partitionRange(int[] result, int offset, int count)
	{
		while( count > 0 )
		{
			for(int level = Meta.FREEPAGE_SIZE_LEVEL_COUNT - 1; level >= 0; --level)
			{
				int i = 1 << level;
				if( count >= i )
				{
					result[offset] = level;
					offset = offset + i;
					count = count - i;
					break;
				}
			}
		}
	}
	
	public void setPinRange(final Range pinRange)
	{
		this.pinRange = new Range(pinRange.from, pinRange.to);
	}
	
	public void freePages(Range range)
	{
		if( range.from <= TABLE_META_PAGEINDEX )
			throw new Error("DBError: free meta page " + range);
		freePageBitmap.setFree(range);
		Range maxRange = freePageBitmap.findMaxFreeRange(range);
		int[] newPartition = new int[maxRange.size()];
		Arrays.fill(newPartition, INVALID_FREEPAGE_SIZE_LEVEL);
		partitionRange(newPartition, 0, newPartition.length);
		int[] oldPartition = new int[maxRange.size()];
		Arrays.fill(oldPartition, INVALID_FREEPAGE_SIZE_LEVEL);
		partitionRange(oldPartition, 0, range.from - maxRange.from);
		partitionRange(oldPartition, range.to - maxRange.from, maxRange.to - range.to);		
		for(int i = 0; i < newPartition.length; ++i)
		{
			if( newPartition[i] != oldPartition[i] )
			{
				updateFreePage(maxRange.from + i, oldPartition[i], newPartition[i]);
			}
		}
	}
	
	public synchronized PageView findPage(int index)
	{
		PageBufferManaged pbm = pageBuffers.get(index);
		if( pbm != null )
		{
			pbm.touch(index);
			return new PageViewSection(new Range(index, index + 1), pbm);
		}
		pbm = new PageBufferManaged(new Range(index, index + 1));
		loadPageBuffer(pbm);
		return new PageViewSection(new Range(index, index + 1), pbm);
	}
	
	public synchronized PageView findPages(final Range range)
	{
		class RangeStatus
		{
			public Range range;
			public PageBuffer pageBuffer;
		}
		
		List<RangeStatus> rangeStatusList = new ArrayList<RangeStatus>();
		int index = range.from;
		RangeStatus rangeStatus = new RangeStatus();
		while( index < range.to )
		{
			PageBuffer pageBuffer = pageBuffers.get(index);
			if( pageBuffer == null )
			{
				if( rangeStatus.range == null )
					rangeStatus.range = new Range(index, index + 1);
				else
					rangeStatus.range = new Range(rangeStatus.range.from, rangeStatus.range.to + 1);
				++index;
			}
			else
			{
				int count = Math.min(range.to, pageBuffer.range.to) - index;
				if( rangeStatus.range != null )
					rangeStatusList.add(rangeStatus);
				rangeStatus = new RangeStatus();
				rangeStatus.range = new Range(index, index + count);
				rangeStatus.pageBuffer = pageBuffer;
				rangeStatusList.add(rangeStatus);
				rangeStatus = new RangeStatus();
				index = index + count;
			}
		}
		if( rangeStatus.range != null )
			rangeStatusList.add(rangeStatus);
		List<PageView> pvLst = new ArrayList<PageView>();
		for(RangeStatus rs : rangeStatusList)
		{
			if( rs.pageBuffer == null )
			{
				PageBufferManaged pbm = new PageBufferManaged(rs.range);
				loadPageBuffer(pbm);
				pvLst.add(pbm);
			}
			else
				pvLst.add(new PageViewSection(rs.range, rs.pageBuffer));
		}
		if( pvLst.size() == 1 ) 
			return pvLst.get(0);
		else
			return new PageViewGroup(range, pvLst);
	}
	
	public PageView allocPages(int count, boolean bExclusive)
	{
		int level = INVALID_FREEPAGE_SIZE_LEVEL;
		int index = INVALID_FREEPAGE_PAGEINDEX;
		if( ! bExclusive )
		{
			for(int lvlFreePageSize  = 0; lvlFreePageSize < Meta.FREEPAGE_SIZE_LEVEL_COUNT ; ++ lvlFreePageSize)
			{
				if( ( 1 << lvlFreePageSize ) >= count && meta.freePagesListHeadPageIndex[lvlFreePageSize] != INVALID_FREEPAGE_PAGEINDEX )
				{
					index = meta.freePagesListHeadPageIndex[lvlFreePageSize];
					level = lvlFreePageSize;
					break;
				}
			}
		}
		if( index == INVALID_FREEPAGE_PAGEINDEX )
		{
			// allocate new pages
			index = meta.pageCount;
			meta.pageCount = meta.pageCount + count;
			PageBufferManaged pageBuffer = new PageBufferManaged(new Range(index, index + count));
			pageBuffers.add(pageBuffer);
			pageBuffer.setAllDirty();
			updateFreePageBitmap(count);
			return pageBuffer;
		}
		
		// reuse free pages
		int freeCount = 1 << level;
		freePageBitmap.setUsed(new Range(index, index + count));
		PageView pageView = findPages(new Range(index, index + count));
		updateFreePage(index, level, INVALID_FREEPAGE_SIZE_LEVEL);
		if( freeCount > count)
			freePages(new Range(index + count, index + freeCount));
		return pageView;
	}
	
	public Meta getMeta()
	{
		return meta;
	}
	
	public int getCacheSize()
	{
		// TODO
		return (pageBuffers.getPageCount() + 1) * (Page.SIZE/*data*/ + 8/*accessTime*/ + 1/*dirty*/)
			+ ( pageBuffers.size() * (32/*page buffer*/ + 20/*node*/));
	}
	
	public boolean testConsistency()
	{	
		if( ! pageBuffers.testConsistency() )
			return false;
		
		Set<PageBufferManaged> pbSet = new HashSet<PageBufferManaged>();
		for(PageBufferManaged pb : pageBuffers)
		{
			if( pb.range.size() <= 0 )
				return false;
			if( pb.range.to > meta.pageCount )
				return false;
			pbSet.add(pb);
		}
		Set<Integer> bufferedPageSet = new HashSet<Integer>();
		for(PageBufferManaged pb : pbSet)
		{
			for(int i : pb.range)
			{
				if( pb.getAccessTime(i) == 0 )
					return false;
				if( bufferedPageSet.contains(i) )
					return false;
				bufferedPageSet.add(i);
			}			
		}
		if( meta.getFreePageCount() + 2 + meta.freePageBitmapPageCount > meta.pageCount )
			return false;
		
		if( freePageBitmap.isFree(new Range(META_PAGEINDEX, META_PAGEINDEX + 1)) )			
			return false;
		if( freePageBitmap.isFree(new Range(TABLE_META_PAGEINDEX, TABLE_META_PAGEINDEX + 1)) )
			return false;
		if( freePageBitmap.isFree(new Range(meta.freePageBitmapPageIndex, meta.freePageBitmapPageIndex + meta.freePageBitmapPageCount)) )
			return false;
		
		if( ! pageBuffers.contains(TABLE_META_PAGEINDEX) )
			return false;
		if( ! pageBuffers.contains(meta.freePageBitmapPageIndex) )
			return false;
		if( freePageBitmap.getFreePageCount() != meta.getFreePageCount() )
			return false;
		
		for(int i = 0; i < Meta.FREEPAGE_SIZE_LEVEL_COUNT; ++i)
		{
			int nFreeCount = 0;
			int next = meta.freePagesListHeadPageIndex[i];
			int prev = META_PAGEINDEX;
			while( next != INVALID_FREEPAGE_PAGEINDEX )
			{
				++nFreeCount;
				if( ! freePageBitmap.isFree(new Range(next, next + (1 << i))) )
					return false;
				PageView pv = findPage(next);
				FreePageHeadWrapper fpHeadWrapper = new FreePageHeadWrapper(pv);
				if( fpHeadWrapper.getPrev() != prev )
					return false;
				if( fpHeadWrapper.getLevel() != i )
					return false;
				prev = next;
				next = fpHeadWrapper.getNext();
			}
			if( nFreeCount != meta.freePagesCount[i] )
				return false;
		}
		
		return true;
	}
	
	public void flush()
	{
		saveDirtyPages(gatherDirtyPages());
	}
	
	public void close()
	{
		if( bStandAlone )
			flush();
		dbFile.close();
	}	
	
	private final boolean bStandAlone;
	private DBFile dbFile;
	private PageBufferManaged metaPage;
	private Meta meta;
	private RangeTree<PageBufferManaged> pageBuffers;
	private Range pinRange; // TODO temporary
	private FreePageBitmap freePageBitmap;
}

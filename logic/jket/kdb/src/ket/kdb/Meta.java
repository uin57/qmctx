
package ket.kdb;

import java.nio.ByteBuffer;

class Meta
{
	
	public static final int FREEPAGE_SIZE_LEVEL_COUNT = 8; // 4k ~ 512k		
	
	public final void load(final ByteBuffer bb)
	{
		pageCount = bb.getInt();
		freePageBitmapPageIndex = bb.getInt();
		freePageBitmapPageCount = bb.getInt();
		for(int i = 0; i < FREEPAGE_SIZE_LEVEL_COUNT; ++i)
		{
			freePagesListHeadPageIndex[i] = bb.getInt();
			freePagesCount[i] = bb.getInt();
		}
	}
	
	public final void save(final ByteBuffer bb)
	{
		bb.putInt(pageCount);
		bb.putInt(freePageBitmapPageIndex);
		bb.putInt(freePageBitmapPageCount);
		for(int i = 0; i < FREEPAGE_SIZE_LEVEL_COUNT; ++i)
		{
			bb.putInt(freePagesListHeadPageIndex[i]);
			bb.putInt(freePagesCount[i]);
		}
	}
	
	public final int getFreePageCount()
	{
		int sum = 0;
		for(int i = 0; i < FREEPAGE_SIZE_LEVEL_COUNT; ++i)
		{
			sum += freePagesCount[i] * ( 1 << i);
		}
		return sum;
	}
	
	public final int getMaxFreePageCount()
	{
		return 1 << ( FREEPAGE_SIZE_LEVEL_COUNT - 1);
	}
	
	public final void dump(final String name)
	{
		System.out.println("\nbuffer " + name + " meta.dump begin");
		System.out.println("    page count is " + pageCount);
		System.out.println("    free page bitmap page index is " + freePageBitmapPageIndex);
		for(int i = 0; i < FREEPAGE_SIZE_LEVEL_COUNT; ++i)
			System.out.println("    free page of size " + ( 1 << i ) + " is ("
					+ freePagesListHeadPageIndex[i] + ", " + freePagesCount[i] + ")");
		System.out.println("buffer " + name + " meta.dump end\n");
	}

	public int pageCount;
	public int freePageBitmapPageIndex;
	public int freePageBitmapPageCount;
	public int freePagesListHeadPageIndex[] = new int[FREEPAGE_SIZE_LEVEL_COUNT];
	public int freePagesCount[] = new int[FREEPAGE_SIZE_LEVEL_COUNT];
}
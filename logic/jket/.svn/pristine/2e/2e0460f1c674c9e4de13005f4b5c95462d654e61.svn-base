
package ket.kdb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BufferManagerTest
{
	private BufferManager bufferManager;

	@Before
	public void setUp()
	{
		String name = getClass().getSimpleName();
		bufferManager = new BufferManager(name, name, true);
		bufferManager.clear();
		assertEquals(bufferManager.getMeta().pageCount, 3);
	}

	@After
	public void tearDown()
	{
		bufferManager.close();
	}	
	
	@Test
	public void testGetPages()
	{
		assertTrue(bufferManager.testConsistency());
		PageView pbs = bufferManager.allocPages(1, true);
		assertNotNull(pbs);
		int freePageCount = bufferManager.getMeta().getFreePageCount();
		bufferManager.freePages(pbs.range);
		assertEquals(pbs.range.size() + freePageCount, bufferManager.getMeta().getFreePageCount());
		assertTrue(bufferManager.testConsistency());
	}
	
	@Test
	public void testFreePage()
	{
		assertTrue(bufferManager.testConsistency());
		int n = bufferManager.getMeta().getMaxFreePageCount();
		PageView pbs = bufferManager.allocPages(n, false);
		assertEquals(pbs.range.size(), n);
		bufferManager.freePages(pbs.range);
		int freePageCount = bufferManager.getMeta().getFreePageCount();
		assertTrue(bufferManager.testConsistency());
		assertTrue(freePageCount >= n);
		int pageCount = bufferManager.getMeta().pageCount;
		assertTrue(pageCount > freePageCount);
		for(int i = n - 1; i > 0; --i)
		{
			pbs = bufferManager.allocPages(i, false);
			assertEquals(i + bufferManager.getMeta().getFreePageCount(), freePageCount);
			assertEquals(pageCount, bufferManager.getMeta().pageCount);
			bufferManager.freePages(pbs.range);
			assertEquals(bufferManager.getMeta().getFreePageCount(), freePageCount);
		}
		assertTrue(bufferManager.testConsistency());
	}
	
	@Test
	public void testReload()
	{
		int n = 300;
		PageView[] pvs = new PageView[n];
		for(int i = 1; i < n; ++i)
		{
			boolean bFull = ( (i % 2) != 0);
			pvs[i] = bufferManager.allocPages(i, bFull);
		}
		for(int i = 1; i < n; i = i + 3)
		{
			bufferManager.freePages(pvs[i].range);
		}
		assertTrue(bufferManager.testConsistency());
		int pageCount = bufferManager.getMeta().pageCount;
		int freePageCount = bufferManager.getMeta().getFreePageCount();
		//
		bufferManager.close();
		String name = getClass().getSimpleName();
		bufferManager = new BufferManager(name, name, true);
		bufferManager.load();
		assertEquals(bufferManager.getMeta().pageCount, pageCount);
		assertEquals(bufferManager.getMeta().getFreePageCount(), freePageCount);
		bufferManager.testConsistency();
		bufferManager.clear();
		assertTrue(bufferManager.getMeta().pageCount == 3);
	}

}

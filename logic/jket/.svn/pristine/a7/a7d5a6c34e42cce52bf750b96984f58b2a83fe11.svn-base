
package ket.kdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals; 

import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.Test;

public class HashTableImpTest
{
	
	private byte[] getKey(int i)
	{
		return ("orange11".substring(0, i % 9) + i).getBytes();
	}

	@Test
	public void testPutAndGet() throws Exception
	{
		String name = getClass().getSimpleName();
		HashTableImp table = new HashTableImp(name, true);
		table.load();
		assertTrue(table.testConsistency());
		int n = 200;
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i%512 + 1];
			val[0] = (byte)val.length;
			table.put(key, val);
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "](" + i + ")", val, valGet);
			//System.out.println(i);
		}
		//
		assertTrue(table.testConsistency());
		table.close();
		table = new HashTableImp(name, true);
		table.load();
		assertTrue(table.testConsistency());
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] valGet = table.get(key);
			assertEquals(valGet.length, i%512 + 1);
			assertEquals(valGet[0], (byte)valGet.length);
		}
		assertTrue(table.testConsistency());
		table.close();
	}
	
	@Test
	public void testEmptyValue()
	{
		String name = getClass().getSimpleName();
		HashTableImp table = new HashTableImp(name, true);
		table.clear();
		assertTrue(table.testConsistency());
		int n = 10000;
		for(int i = 0; i < n; ++i)
		{
			byte[] key = new byte[4];
			ByteBuffer bb = ByteBuffer.wrap(key);
			//bb.putInt(random.nextInt(10000));
			bb.putInt(i);
			byte[] val = new byte[0];
			table.put(key, val);
		}
		//
		assertTrue(table.testConsistency());
		table.close();
	}
	
	@Test
	public void testUpdate()
	{
		String name = getClass().getSimpleName();
		HashTableImp table = new HashTableImp(name, true);
		table.load();
		assertTrue(table.testConsistency());
		int n = 2000;
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i];
			val[0] = (byte)(i % 11);
			table.put(key, val);
			val = new byte[i*3];
			val[0] = (byte)val.length;
			table.put(key, val);
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i*3];
			val[0] = (byte)val.length;
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		//
		assertTrue(table.testConsistency());
		table.close();
		table = new HashTableImp(name, true);
		table.load();
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i*3];
			val[0] = (byte)val.length;
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		assertTrue(table.testConsistency());
		table.close();
	}
	
	@Test
	public void testDel()
	{
		String name = getClass().getSimpleName();
		HashTableImp table = new HashTableImp(name, true);
		table.clear();
		int n = 2000;
		for(int i = 1; i < n; ++i)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i*3];
			val[0] = (byte)val.length;
			table.put(key, val);
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		for(int i = 1; i < n; i+=2)
		{
			byte[] key = getKey(i);
			table.del(key);
			assertNull(table.get(key));
		}
		for(int i = 2; i < n; i+=2)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i*3];
			val[0] = (byte)val.length;
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		//
		assertTrue(table.testConsistency());
		table.close();
		table = new HashTableImp(name, true);
		table.load();
		for(int i = 1; i < n; i+=2)
		{
			assertNull(table.get(getKey(i)));
		}
		for(int i = 2; i < n; i+=2)
		{
			byte[] key = getKey(i);
			byte[] val = new byte[i*3];
			val[0] = (byte)val.length;
			byte[] valGet = table.get(key);
			assertArrayEquals("TestError: get != last put [" + new String(key) + "]", val, valGet);
			
		}
		assertTrue(table.testConsistency());
		table.close();
	}
	
	@Test
	public void testDBTable()
	{
		String name = "./dbtest1/data/table1";
		HashTableImp table = new HashTableImp(name, true);
		table.load();
		assertTrue(table.testConsistency());
		table.close();
	}
	
	//@Test
	public void testPut()
	{
		//String name = "./dbtest1/data/table2";
		String name = getClass().getSimpleName();
		HashTableImp table = new HashTableImp(name, true);
		table.load();
		assertTrue(table.testConsistency());
		byte[] key = new byte[10];
		int n = 1000;
		for(int i = 0; i < n; ++i)
		{
			ByteBuffer bb = ByteBuffer.wrap(key, 6, 4);
			bb.putInt(i);
			//table.put(key, new byte[5000]);
			//System.out.println(i);
			//assertTrue("at " + i, table.testConsistency());
			table.put(key, new byte[1000 + random.nextInt(9000)]);
		}
		table.close();
	}
	
	private Random random = new Random();
}

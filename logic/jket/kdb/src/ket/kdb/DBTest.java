
package ket.kdb;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;

//import ket.util.FileSys;

public class DBTest
{
	static abstract class AbstractTrans implements Transaction
	{
		@Override
		public void onCallback(ErrorCode errcode)
		{
			switch( errcode )
			{
			case eOK:
			case eUserCancel:
			case eRejected:
				break;
			default:
				System.out.println(getClass().getSimpleName() + " onCallBack, errcode is " + errcode);
				break;
			}
		}
	}
	static final class PutTestTransaction extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			byte[] key = new byte[4];
			byte[] val = table2.get(key);
			key = new String("hello").getBytes();
			val = new String("kdb").getBytes();
			table1.put(key, val);
			return Arrays.equals(table1.get(key), val);
		}
				
		@AutoInit
		public Table<byte[], byte[]> table1;
		@AutoInit
		public TableReadonly<byte[], byte[]> table2;
	}
	
	static final class PutTestTransaction2 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			byte[] key = new String("hello").getBytes();
			table1.put(key, new byte[16]);
			return false;
		}
				
		@AutoInit
		public Table<byte[], byte[]> table1;
	}
	
	static final class PutTestTransaction3 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			byte[] key = new byte[4];
			byte[] val = table1.get(key);
			key = new String("hello").getBytes();
			val = new String("kdb").getBytes();
			return Arrays.equals(table1.get(key), val);
		}
				
		@AutoInit
		public TableReadonly<byte[], byte[]> table1;
	}
	
	static final class PutTable1 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			table1.put(random.nextInt(10000), "hello");
			return true;
		}
				
		@AutoInit
		public Table<Integer, String> table1;
	}
	
	static final class GetTable1 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			table1.get(random.nextInt(10000));
			return true;
		}
				
		@AutoInit
		public TableReadonly<Integer, String> table1;
	}
	
	static final class PutTable2 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			//byte[] key = new byte[10];
			//ByteBuffer bb = ByteBuffer.wrap(key, 6, 4);
			byte[] key = new byte[4];
			ByteBuffer bb = ByteBuffer.wrap(key);
			bb.putInt(random.nextInt(10000));
			//table2.put(key, new byte[5000]);
			table2.put(key, new byte[1000 + random.nextInt(9000)]);
			return true;
		}
				
		@AutoInit
		public Table<byte[], byte[]> table2;
	}
	
	static final class GetTable2 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			//byte[] key = new byte[10];
			//ByteBuffer bb = ByteBuffer.wrap(key, 6, 4);
			byte[] key = new byte[4];
			ByteBuffer bb = ByteBuffer.wrap(key);
			bb.putInt(random.nextInt(10000));
			//table2.put(key, new byte[5000]);
			table2.get(key);
			return true;
		}
				
		@AutoInit
		public TableReadonly<byte[], byte[]> table2;
	}
	
	static final class DumpTable1 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			int i = 0;
			for(TableEntry<Integer, String> e : table1)
			{
				System.out.println("(" + (++i) + ") key is " + e.getKey() + ", val is " + e.getValue());
			}
			return true;
		}
				
		@AutoInit
		public TableReadonly<Integer, String> table1;
	}
	
	static final class DumpTable2 extends AbstractTrans
	{
		@Override
		public boolean doTransaction()
		{
			int i = 0;
			for(TableEntry<byte[], byte[]> e : table2)
			{
				System.out.println("(" + (++i) + ") key is " + e.getKey() + ", val is " + e.getValue());
			}
			return true;
		}
				
		@AutoInit
		public TableReadonly<byte[], byte[]> table2;
	}
	
	//@Before
	public void openDB()
	{
		db = Factory.newDB();
		db.open(Paths.get("./dbtest1"), Paths.get("./dbtest1/dbcfg.xml"));
	}
	
	//@After
	public void closeDB()
	{
		db.close();
	}
	
	//@Test
	public void testDB()
	{
		db.execute(new PutTable1());
		db.execute(new GetTable1());
		db.execute(new PutTable2());
		db.execute(new GetTable2());
	}
	
	public void dumpTables()
	{
		db.execute(new DumpTable2());
		//db.execute(new DumpTable1());
	}
	
	public static void main(String[] args)
	{
		DBTest dbtest = new DBTest();
		dbtest.openDB();
		//dbtest.dumpTables();
		while( true )
		{
			//for(int i = 0; i < 5000; ++i)
			//	dbtest.testDB();
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		//dbtest.closeDB();
	}
	
	DB db;
	static Random random = new Random();
}

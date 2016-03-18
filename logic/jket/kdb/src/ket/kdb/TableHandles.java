
package ket.kdb;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;

import ket.util.Stream;
import ket.util.Stream.AIStream;
import ket.util.Stream.AOStream;
import ket.util.Stream.BytesOutputStream;
import ket.util.Stream.OStreamLE;

class TableHandles
{		
	
	public static AbstractTableHandle newTableHandle(final AbstractTable table, final boolean readonly)
	{
		Class<?> classKey = table.getClassKey();
		Class<?> classValue = table.getClassValue();
		if( classKey == null || classValue == null )
			return new RawTableHandle(table, readonly);
		return new TableHandle(table, readonly, classKey, classValue);
	}
	
	public static byte[] getBytes(Object obj, Class<?> classObj)
	{
		if( obj instanceof Stream.IStreamable )
		{
			ByteBuffer bb = Stream.storeObjLE((Stream.IStreamable)obj);
			return Arrays.copyOf(bb.array(), bb.limit());
		}
		BytesOutputStream bsos = new BytesOutputStream();
		AOStream os = new OStreamLE(bsos);
		if( classObj == Integer.class )				
			os.pushInteger(((Integer)obj).intValue());
		else if( classObj == Long.class )				
			os.pushLong(((Long)obj).longValue());
		else if( classObj == Integer.class )				
			os.pushInteger(((Integer)obj).intValue());
		else if( classObj == String.class )
			os.pushString((String)obj);
		else
			throw new Error("Impossible");
		return Arrays.copyOf(bsos.array(), bsos.size());
	}
	
	public static Object loadBytes(byte[] bs, Class<?> classObj)
	{
		ByteArrayInputStream bbis = new ByteArrayInputStream(bs);
		AIStream ais = new Stream.IStreamLE(bbis);
		try
		{
			if( classObj == Integer.class )
				return ais.popInteger();
			else if( classObj == Long.class )
				return ais.popLong();
			else if( classObj == String.class )
				return ais.popString();
			else
			{
				Object obj = classObj.newInstance();
				ais.pop((Stream.IStreamable)obj);
				return obj;
			}
		}
		catch(Exception ex)
		{
			throw new RuntimeException("decode err", ex); // TODO
		}
	}
	
	private static class TableHandle extends AbstractTableHandle implements Table<Object, Object>, TableReadonly<Object, Object>
	{	
		
		public TableHandle(final AbstractTable table, final boolean readonly, Class<?> classKey, Class<?> classValue)
		{
			super(table, readonly);
			this.classKey = classKey;
			this.classValue = classValue;
		}
		
		class Iter implements Iterator<TableEntry<Object, Object>>
		{
			@Override
			public boolean hasNext()
			{
				return iter.hasNext();
			}
			
			class TableEntryImp implements TableEntry<Object, Object>
			{
				TableEntryImp(Object k, Object v)
				{
					this.k = k;
					this.v = v;
				}
				
				@Override
				public Object getKey()
				{
					return k;
				}
				
				@Override
				public Object getValue()
				{
					return v;
				}
				
				Object k;
				Object v;
			}

			@Override
			public TableEntry<Object, Object> next()
			{
				TableEntry<byte[], byte[]> e = iter.next();
				Object k = e.getKey() == null ? null : loadBytes(e.getKey(), classKey);
				Object v = e.getValue() == null ? null : loadBytes(e.getValue(), classValue);
				return new TableEntryImp(k, v);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
			
			Iterator<TableEntry<byte[], byte[]>> iter = table.iterator();
		}
		
		@Override
		public Iterator<TableEntry<Object, Object>> iterator()
		{
			return new Iter();
		}
		
		@Override
		public final Object get(final Object key)
		{
			byte[] bsKey = getBytes(key, classKey);
			byte[] bsVal;
			if( readonly )
			{
				bsVal = table.get(bsKey);
				return bsVal == null ? null : loadBytes(bsVal, classValue);
			}
			if( cache.containsKey(bsKey) )
				bsVal = cache.get(bsKey).val;
			else
				bsVal = table.get(bsKey);
			cache.put(bsKey, new DataCache(bsVal, false));
			return bsVal == null ? null : loadBytes(bsVal, classValue);
				
		}

		@Override
		public final void put(final Object key, final Object val)
		{
			cache.put(getBytes(key, classKey), new DataCache(getBytes(val, classValue), true));
		}

		@Override
		public final void del(final Object key)
		{	
			cache.put(getBytes(key, classKey), new DataCache(null, true));
		}
		
		private final Class<?> classKey;
		private final Class<?> classValue;
	}
	
	private static class RawTableHandle extends AbstractTableHandle implements Table<byte[], byte[]>, TableReadonly<byte[], byte[]>
	{	
		
		public RawTableHandle(final AbstractTable table, final boolean readonly)
		{
			super(table, readonly);
		}
		
		@Override
		public Iterator<TableEntry<byte[], byte[]>> iterator()
		{
			return table.iterator();
		}
		
		@Override
		public final byte[] get(final byte[] key)
		{
			if( readonly )
				return table.get(key);
			
			if( cache.containsKey(key) )
				return cache.get(key).val;
			byte[] val = table.get(key);
			cache.put(key, new DataCache(val, false));
			return val;
				
		}

		@Override
		public final void put(final byte[] key, final byte[] val)
		{
			cache.put(key, new DataCache(val, true));
		}

		@Override
		public final void del(final byte[] key)
		{	
			cache.put(key, new DataCache(null, true));
		}
	}
}

package ket.util;

import java.util.List;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Stream
{
	public static class EOFException extends java.lang.Exception
	{
		private static final long serialVersionUID = -4882139392430635223L;
		
	}
	
	public static class DecodeException extends java.lang.Exception
	{
		private static final long serialVersionUID = -4169839945261141485L;
	}
	
	public static void readAll(InputStream is, byte[] b) throws IOException
	{
		readAll(is, b, 0, b.length);
	}
	
	public static void readAll(InputStream is, byte[] b, int off, int len) throws IOException
	{
		while( len > 0 )
		{
			int rlen = is.read(b, off, len);
			if( rlen == -1 )
				throw new java.io.EOFException();
			len -= rlen;
			off += rlen;
		}
	}
	
	public static void skipAll(InputStream is, long n) throws IOException
	{
		while( n > 0 )
		{
			long slen = is.skip(n);
			if( n < 0 )
				throw new java.io.IOException();
			n -= slen;
		}
	}
	
	public static interface IStreamable
	{
		public void encode(AOStream os);
		public void decode(AIStream is) throws EOFException, DecodeException;
	}
	
	public static ByteBuffer storeObjLE(IStreamable obj)
	{
		BytesOutputStream bsos = new BytesOutputStream();
		AOStream os = new OStreamLE(bsos);
		obj.encode(os);
		return ByteBuffer.wrap(bsos.array(), 0, bsos.size());
	}
	
	public static boolean loadObjLE(IStreamable obj, ByteBuffer bb)
	{
		ByteArrayInputStream bbis = new ByteArrayInputStream(bb.array(), bb.arrayOffset(), bb.limit());
		AIStream ais = new IStreamLE(bbis);
		try
		{
			obj.decode(ais);
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static ByteBuffer storeObjBE(IStreamable obj)
	{
		BytesOutputStream bsos = new BytesOutputStream();
		AOStream os = new OStreamBE(bsos);
		obj.encode(os);
		return ByteBuffer.wrap(bsos.array(), 0, bsos.size());
	}
	
	public static boolean loadObjBE(IStreamable obj, ByteBuffer bb)
	{
		ByteArrayInputStream bbis = new ByteArrayInputStream(bb.array(), bb.arrayOffset(), bb.limit());
		AIStream ais = new IStreamBE(bbis);
		try
		{
			obj.decode(ais);
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static void storeObjLE(IStreamable obj, OutputStream os)
	{
		obj.encode(new OStreamLE(os));
	}
	
	public static void storeObjLE(IStreamable obj, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		storeObjLE(obj, fos);
		fos.close();
	}
	
	public static boolean loadObjLE(IStreamable obj, File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			AIStream ais = new IStreamLE(fis);
			obj.decode(ais);
			fis.close();
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static void storeZipObjLE(IStreamable obj, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		ByteBuffer bb = Stream.storeObjLE(obj);
		DeflaterOutputStream zos = new DeflaterOutputStream(fos);
		zos.write(bb.array(), 0, bb.limit());
		zos.finish();
		fos.close();
	}
	
	public static boolean loadZipObjLE(IStreamable obj, File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			InflaterInputStream zis = new InflaterInputStream(fis);
			boolean bOK = Stream.loadObjLE(obj, zis);
			fis.close();
			return bOK;
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	public static boolean loadObjLE(IStreamable obj, InputStream is)
	{
		try
		{
			AIStream ais = new IStreamLE(is);
			obj.decode(ais);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public static void decodeLE(IStreamable obj, byte[] bs) throws DecodeException, EOFException
	{
		obj.decode(new IStreamLE(new BytesInputStream(bs)));
	}
	
	public static <T extends IStreamable> List<T> decodeListLE(Class<T> c, byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamLE(new BytesInputStream(bs)).popList(c);
	}
	
	public static int decodeIntegerLE(byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamLE(new BytesInputStream(bs)).popInteger();
	}
	
	public static String decodeStringLE(byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamLE(new BytesInputStream(bs)).popString();
	}
	
	public static void decodeBE(IStreamable obj, byte[] bs) throws DecodeException, EOFException
	{
		obj.decode(new IStreamBE(new BytesInputStream(bs)));
	}
	
	public static <T extends IStreamable> List<T> decodeListBE(Class<T> c, byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamBE(new BytesInputStream(bs)).popList(c);
	}
	
	public static int decodeIntegerBE(byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamBE(new BytesInputStream(bs)).popInteger();
	}
	
	public static String decodeStringBE(byte[] bs) throws DecodeException, EOFException
	{
		return new IStreamBE(new BytesInputStream(bs)).popString();
	}
	
	public static byte[] encodeLE(IStreamable obj)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		obj.encode(new Stream.OStreamLE(bos));
		return bos.toByteArray();
	}
	
	public static byte[] encodeListLE(List<? extends IStreamable> lst)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamLE(bos).pushList(lst);
		return bos.toByteArray();
	}
	
	public static byte[] encodeIntegerLE(int i)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamLE(bos).pushInteger(i);
		return bos.toByteArray();
	}
	
	public static byte[] encodeStringLE(String s)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamLE(bos).pushString(s);
		return bos.toByteArray();
	}
	
	public static byte[] encodeBE(IStreamable obj)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		obj.encode(new Stream.OStreamBE(bos));
		return bos.toByteArray();
	}
	
	public static byte[] encodeListBE(List<? extends IStreamable> lst)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamBE(bos).pushList(lst);
		return bos.toByteArray();
	}
	
	public static byte[] encodeIntegerBE(int i)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamBE(bos).pushInteger(i);
		return bos.toByteArray();
	}
	
	public static byte[] encodeStringBE(String s)
	{
		Stream.BytesOutputStream bos = new Stream.BytesOutputStream();
		new Stream.OStreamBE(bos).pushString(s);
		return bos.toByteArray();
	}
	
	public static void storeObjBE(IStreamable obj, OutputStream os)
	{
		obj.encode(new OStreamBE(os));
	}
	
	public static void storeObjBE(IStreamable obj, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		storeObjBE(obj, fos);
		fos.close();
	}
	
	public static boolean loadObjBE(IStreamable obj, File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			AIStream ais = new IStreamBE(fis);
			obj.decode(ais);
			fis.close();
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static void storeZipObjBE(IStreamable obj, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		ByteBuffer bb = Stream.storeObjBE(obj);
		DeflaterOutputStream zos = new DeflaterOutputStream(fos);
		zos.write(bb.array(), 0, bb.limit());
		zos.finish();
		fos.close();
	}
	
	public static boolean loadZipObjBE(IStreamable obj, File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			InflaterInputStream zis = new InflaterInputStream(fis);
			boolean bOK = Stream.loadObjBE(obj, zis);
			fis.close();
			return bOK;
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	public static boolean loadObjBE(IStreamable obj, InputStream is)
	{
		try
		{
			AIStream ais = new IStreamBE(is);
			obj.decode(ais);
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static abstract class AIStream
	{	
		
		public AIStream(InputStream is)
		{
			this.is = is;
		}
		
		public InputStream getInputStream()
		{
			return is;
		}
		
		public boolean hasMoreData()
		{
			try
			{
				return is.available() > 0;
			}
			catch(IOException ex)
			{
				return false;
			}
		}
		
		public byte[] popBytes(int len) throws EOFException, DecodeException
		{
			if( len < 0 )
				throw new DecodeException();
			else if( len == 0)
				return new byte[0];
			try
			{
				byte[] bs = new byte[len];
				Stream.readAll(is, bs);
				return bs;
			}
			catch(Exception ex)
			{
				throw new EOFException();
			}
		}
		
		public void popBytes(int len, byte[] dst) throws EOFException, DecodeException
		{
			if( len < 0 )
				throw new DecodeException();
			else if( len == 0 )
				return;
			try
			{
				Stream.readAll(is, dst, 0, len);
			}
			catch(Exception ex)
			{
				throw new EOFException();
			}
		}
		
		public byte popByte() throws EOFException
		{
			try
			{
				int r = is.read();
				if( r == -1 )
					throw new EOFException();
				return (byte)r;
			}
			catch(Exception ex)
			{
				throw new EOFException();
			}
		}
		
		public float popFloat() throws EOFException
		{
			int i = popInteger();
			return Float.intBitsToFloat(i);
		}
		
		public boolean popBoolean() throws EOFException
		{
			return popByte() != 0;
		}
		
		public abstract short popShort() throws EOFException;
		public abstract int popInteger() throws EOFException;
		public abstract long popLong() throws EOFException;
		
		public int popSizeT() throws EOFException, DecodeException
		{
			int i1 = 0xff & (int)popByte();
			if( (i1 & 0x80) == 0 ) // 1byte
				return i1;
			if( (i1 & 0x40) == 0 )// 2bytes
			{
				i1 = (i1 & 0x7f) << 8;
				int i2 = 0xff & (int)popByte();
				return i1 | i2;
			}
			else // 4bytes
			{
				i1 = (i1 & 0x3f) << 24;
				int i2 = (0xff & (int)popByte()) << 16;
				int i3 = (0xff & (int)popByte()) << 8;
				int i4 = (0xff & (int)popByte());
				return i1 | i2 | i3 | i4;
			}
		}
		
		public String popString() throws EOFException, DecodeException
		{
			int len = popSizeT();
			byte[] bs = popBytes(len);
			try
			{
				return new String(bs, 0, len, "UTF-8");
			}
			catch(UnsupportedEncodingException ex)
			{
				throw new DecodeException();
			}
		}
		
		public <E extends Enum<E>> E popEnum(E[] values) throws EOFException, DecodeException
		{
			try
			{
				return values[popInteger()];
			}
			catch(IndexOutOfBoundsException ex)
			{
				throw new DecodeException();
			}
		}
		
		public ByteBuffer popByteBuffer() throws EOFException, DecodeException
		{
			int len = popSizeT();
			if( len < 0 )
				throw new DecodeException();
			byte[] bs = new byte[len];
			if( len > 0 )
				popBytes(len, bs);
			return ByteBuffer.wrap(bs);
		}
		
		public List<Byte> popByteList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<Byte> lst = new ArrayList<Byte>();
			for(int i = 0; i < size; ++i)
				lst.add(popByte());
			return lst;
		}
		
		public List<Short> popShortList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<Short> lst = new ArrayList<Short>();
			for(int i = 0; i < size; ++i)
				lst.add(popShort());
			return lst;
		}
		
		public List<Integer> popIntegerList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<Integer> lst = new ArrayList<Integer>();
			for(int i = 0; i < size; ++i)
				lst.add(popInteger());
			return lst;
		}
		
		public List<Float> popFloatList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<Float> lst = new ArrayList<Float>();
			for(int i = 0; i < size; ++i)
				lst.add(popFloat());
			return lst;	
		}
		
		public List<Long> popLongList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<Long> lst = new ArrayList<Long>();
			for(int i = 0; i < size; ++i)
				lst.add(popLong());
			return lst;
		}
		
		public List<String> popStringList() throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<String> lst = new ArrayList<String>();
			for(int i = 0; i < size; ++i)
				lst.add(popString());
			return lst;
		}
		
		public <T extends IStreamable> List<T> popList(Class<T> c) throws EOFException, DecodeException
		{
			int size = popSizeT();
			if( size < 0 )
				throw new DecodeException();
			List<T> lst = new ArrayList<T>(size);
			try
			{
				for(int i = 0; i < size; ++i)
				{
					T e = c.newInstance();
					pop(e);
					lst.add(e);
				}
				return lst;
			}
			catch(IllegalAccessException ex)
			{
				throw new DecodeException();
			}
			catch(InstantiationException ex)
			{
				throw new DecodeException();
			}
		}
		
		public IStreamable pop(IStreamable obj) throws EOFException, DecodeException
		{
			obj.decode(this);
			return obj;
		}		
		
		protected InputStream is;
	}
	
	public static class IStreamBE extends AIStream
	{	
		
		public IStreamBE(InputStream is)
		{
			super(is);
		}
		
		@Override		
		public short popShort() throws EOFException
		{
			byte[] bs = null;
			try
			{
				bs = popBytes(2);
			}
			catch(DecodeException ex)
			{			
			}
			return (short)(( ((int)bs[0]) <<8 ) | (0xff&(int)bs[1]));
		}
		
		@Override		
		public int popInteger() throws EOFException
		{
			byte[] bs = null;
			try
			{
				bs = popBytes(4);
			}
			catch(DecodeException ex)
			{			
			}
			return ( ((int)bs[0]) <<24 )|( ((int)bs[1]<<16)&0xffffff )|( ((int)bs[2]<<8)&0xffff )| ( ((int)(bs[3]))&0xff );
		}				
		
		@Override
		public long popLong() throws EOFException
		{
			long lh = 0xffffffffL&(long)popInteger();
			long ll = 0xffffffffL&(long)popInteger();
			return (lh<<32) | ll;
		}
	}
	
	public static class IStreamLE extends AIStream
	{	
		
		public IStreamLE(InputStream is)
		{
			super(is);
		}
		
		@Override		
		public short popShort() throws EOFException
		{
			byte[] bs = null;
			try
			{
				bs = popBytes(2);
			}
			catch(DecodeException ex)
			{			
			}
			return (short)(( ((int)bs[1]) <<8 ) | (0xff&(int)bs[0]));
		}
		
		@Override		
		public int popInteger() throws EOFException
		{
			byte[] bs = null;
			try
			{
				bs = popBytes(4);
			}
			catch(DecodeException ex)
			{			
			}
			return ( ((int)bs[3]) <<24 )|( ((int)bs[2]<<16)&0xffffff )|( ((int)bs[1]<<8)&0xffff )| ( ((int)(bs[0]))&0xff );
		}
		
		@Override
		public long popLong() throws EOFException
		{
			long ll = 0xffffffffL&(long)popInteger();
			long lh = 0xffffffffL&(long)popInteger();
			return (lh<<32) | ll;
		}
	}
	
	public static class BytesOutputStream extends ByteArrayOutputStream
	{
		public byte[] array()
		{
			return buf;
		}
		
		public int size()
		{
			return count;
		}
	}
	
	public static class BytesInputStream extends ByteArrayInputStream
	{
		public BytesInputStream(byte[] bs)
		{
			super(bs);
		}
		
		public BytesInputStream(byte[] bs, int offset, int len)
		{
			super(bs, offset, len);
		}
		
		public byte[] array()
		{
			return buf;
		}
		
		public int size()
		{
			return count;
		}
		
		public int pos()
		{
			return pos;
		}
	}
	
	public static abstract class AOStream
	{
		public AOStream(OutputStream os)
		{
			this.os = os;
		}
		
		public AOStream pushBytes(byte[] data, int offset, int len)
		{
			try
			{
				os.write(data, offset, len);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		public AOStream pushBytes(byte[] data, int offset)
		{
			try
			{
				os.write(data, offset, data.length - offset);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		public AOStream pushBytes(byte[] data)
		{
			try
			{
				os.write(data);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		public AOStream pushBoolean(boolean b)
		{
			return pushByte(b ? (byte)1 : 0);
		}
		
		public AOStream pushByte(byte b)
		{
			try
			{
				os.write((int)b);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		public AOStream pushFloat(float f)
		{
			return pushInteger(Float.floatToRawIntBits((f)));
		}
		
		public abstract AOStream pushShort(short s);		
		public abstract AOStream pushChar(char c);		
		public abstract AOStream pushInteger(int i);
		public abstract AOStream pushLong(long l);
		
		public static int getSizeTPackSize(int s)
		{
			if( s < ( 1 << 7 ) )
				return 1;
			else if( s < ( 1 << 14 ) )
			{
				return 2;
			}
			return 4;
		}
		
		public AOStream pushSizeT(int s)
		{
			//if( s < 0 || s > ( 1 << 30 ) )
			//	throw new Error("impossible");
			if( s < ( 1 << 7 ) )
				pushByte((byte)s);
			else if( s < ( 1 << 14 ) )
			{
				byte[] bs = new byte[2];
				bs[0] =  (byte)( (s + ( 1 << 15 ) )>>> 8 );
				bs[1] = (byte)s;
				pushBytes(bs);
			}
			else
			{
				byte[] bs = new byte[4];
				bs[0] = (byte)((s + ( 3 << 30 ) )>>>24);
				bs[1] = (byte)(s>>>16);
				bs[2] = (byte)(s>>>8);
				bs[3] = (byte)(s);
				pushBytes(bs);
			}
			return this;
		}
		
		public AOStream pushString(String str)
		{
			try
			{
				byte[] bs = str.getBytes("UTF-8");
				pushSizeT(bs.length);
				pushBytes(bs);
			}
			catch(UnsupportedEncodingException ex)
			{			
			}
			return this;
		}
		
		public <E extends Enum<E>> AOStream pushEnum(E e)
		{
			pushInteger(e.ordinal());
			return this;
		}
		
		public AOStream pushList(List<? extends IStreamable> lst)
		{
			pushSizeT(lst.size());
			for(IStreamable i : lst)
			{
				push(i);
			}
			return this;
		}
		
		public AOStream pushByteList(List<Byte> lst)
		{
			pushSizeT(lst.size());
			for(byte s : lst)
				pushByte(s);
			return this;
		}
		
		public AOStream pushShortList(List<Short> lst)
		{
			pushSizeT(lst.size());
			for(short s : lst)
				pushShort(s);
			return this;
		}
		
		public AOStream pushIntegerList(List<Integer> lst)
		{
			pushSizeT(lst.size());
			for(int i : lst)
				pushInteger(i);
			return this;
		}
		
		public AOStream pushFloatList(List<Float> lst)
		{
			pushSizeT(lst.size());
			for(float f : lst)
				pushFloat(f);
			return this;
		}
		
		public AOStream pushLongList(List<Long> lst)
		{
			pushSizeT(lst.size());
			for(long l : lst)
				pushLong(l);
			return this;
		}
		
		public AOStream pushStringList(List<String> lst)
		{
			pushSizeT(lst.size());
			for(String s : lst)
				pushString(s);
			return this;
		}
		
		public AOStream pushByteBuffer(ByteBuffer bb)
		{
			pushSizeT(bb.remaining());
			if( bb.hasRemaining() )
				pushBytes(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
			return this;
		}
		
		public AOStream push(IStreamable obj)
		{
			obj.encode(this);
			return this;
		}
		
		protected OutputStream os;
	}
	
	public static class OStreamBE extends AOStream
	{		
		
		public OStreamBE(OutputStream os)
		{
			super(os);
		}
		
		@Override
		public AOStream pushShort(short s)
		{
			byte[] bs = new byte[2];
			bs[0] = (byte)(s>>>8);
			bs[1] = (byte)(s);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		@Override
		public AOStream pushChar(char c)
		{
			byte[] bs = new byte[2];
			bs[0] = (byte)(c>>>8);
			bs[1] = (byte)(c);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		@Override
		public AOStream pushInteger(int i)
		{
			byte[] bs = new byte[4];
			bs[0] = (byte)(i>>>24);
			bs[1] = (byte)(i>>>16);
			bs[2] = (byte)(i>>>8);
			bs[3] = (byte)(i);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		@Override
		public AOStream pushLong(long l)
		{
			int ih = (int)((l>>>32)&0xffffffff);
			int il = (int)(l&0xffffffff);
			pushInteger(ih);
			pushInteger(il);
			return this;
		}
	}
	
	public static class OStreamLE extends AOStream
	{		
		
		public OStreamLE(OutputStream os)
		{
			super(os);
		}
				
		@Override
		public AOStream pushShort(short s)
		{
			byte[] bs = new byte[2];
			bs[1] = (byte)(s>>>8);
			bs[0] = (byte)(s);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		@Override
		public AOStream pushChar(char c)
		{
			byte[] bs = new byte[2];
			bs[1] = (byte)(c>>>8);
			bs[0] = (byte)(c);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}
		
		@Override
		public AOStream pushInteger(int i)
		{
			byte[] bs = new byte[4];
			bs[3] = (byte)(i>>>24);
			bs[2] = (byte)(i>>>16);
			bs[1] = (byte)(i>>>8);
			bs[0] = (byte)(i);
			try
			{
				os.write(bs);
			}
			catch(IOException ex)
			{				
			}
			return this;
		}	
		
		@Override
		public AOStream pushLong(long l)
		{
			int ih = (int)((l>>>32)&0xffffffff);
			int il = (int)(l&0xffffffff);
			pushInteger(il);
			pushInteger(ih);
			return this;
		}
	}
	
	public static void main(String[] args)
	{
		byte[] bs2 = encodeStringLE("sdfdf");
		System.out.println(bs2.length);
		for(byte e : bs2)
			System.out.println(e);
		try
		{
			System.out.println(decodeStringLE(bs2));
		}
		catch(Exception ex)
		{			
		}
	}
}

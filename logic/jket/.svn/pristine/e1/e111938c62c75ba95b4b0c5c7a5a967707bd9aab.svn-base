
package ket.util;

import java.util.Arrays;

public class Octets implements Cloneable
{	
	public Octets()
	{		
	}
	
	public Octets(int initsize)
	{
		int cap = calCap(initsize);
		if( cap > 0 )
		{
			buf = new byte[cap];
			size = initsize;
		}
	}
	
	public Octets(byte[] bytes)
	{
		replace(bytes);
	}
	
	public Octets(byte[] bytes, int offset, int len)
	{
		replace(bytes, offset, len);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Octets o = (Octets)obj;
		if( o == this )
			return true;
		if( o == null )
			return false;
		return size == o.size && ( buf == o.buf || buf.equals(o.buf) );
	}
	
	@Override
	public int hashCode()
	{
		int i = 0;
		int hash = 0;
		while( i < size )
		{
			byte b1 = buf[i];
			byte b2 = buf[i+1];
			byte b3 = buf[i+2];
			byte b4 = buf[i+3];
			int h = (((int)b4)<<24)|(((int)b3)<<16)|(((int)b2)<<8)|(((int)b1));
			hash ^= h;
			i += 4;
		}
		return hash;
	}
	
	public Octets clone()
	{
		Octets o = new Octets();
		if( size != 0 )
		{
			o.replace(buf, 0, size);
		}
		return o;
	}
	
	public int size()
	{
		return size;
	}
	
	public void clear()
	{
		size = 0;
		buf = null;
	}
	
	public Octets resize(int newsize)
	{
		if( newsize < 0 )
			newsize = 0;
		if( newsize == size )
			return this;
		int newcap = calCap(newsize);
		if( size == 0 )
		{
			buf = new byte[newcap];
		}
		else if( newcap != buf.length )
		{
			buf = Arrays.copyOf(buf, newcap);	
		}
		size = newsize;
		return this;
	}
	
	public byte[] getBytes()
	{
		if( size == 0 )
			return new byte[0];
		return Arrays.copyOf(buf, size);
	}
	
	public byte[] getBytes(int offset, int len) throws IndexOutOfBoundsException
	{
		if( offset < 0 || offset > size || len < 0 || offset + len > size )
			throw new IndexOutOfBoundsException();
		if( len == 0 )
			return new byte[0];
		return Arrays.copyOfRange(buf, offset, offset + len);
	}
	
	public byte[] getBytes(int offset, int len, byte[] dst) throws IndexOutOfBoundsException
	{
		if( offset < 0 || offset > size || len < 0 || offset + len > size || dst == null || dst.length < len)
			throw new IndexOutOfBoundsException();
		if( len != 0 )
			System.arraycopy(buf, offset, dst, 0, len);
		return dst;
	}
	
	public byte[] array()
	{
		if( size == 0 )
			return new byte[0];
		return buf;
	}
	
	private Octets doInsert(int offset, byte[] data, int srcoffset, int len)
	{
		if( size == 0 )
		{
			size = len;
			buf = Arrays.copyOfRange(data, srcoffset, srcoffset + len);
			return this;
		}
		int newsize = size + len;
		int newcap = calCap(newsize);
		if( newcap > buf.length )
			buf = Arrays.copyOf(buf, newcap);
		if( offset != size )
			System.arraycopy(buf, offset, buf, offset + len, size - offset);
		System.arraycopy(data, srcoffset, buf, offset, len);
		size = newsize;
		return this;
	}
	
	private Octets doErase(int offset, int len)
	{
		if( offset + len == size)
		{
			int newcap = calCap(offset);
			if( newcap < buf.length )
			{
				buf = Arrays.copyOfRange(buf, 0, newcap);
			}
			size = offset;
		}
		else
		{
			int newsize = size - len;
			int newcap = calCap(newsize);
			if( newcap < buf.length )
			{
				byte[] newbuf = Arrays.copyOfRange(buf, 0, newcap);
				System.arraycopy(buf, offset + len, newbuf, offset, size - offset - len);
				buf = newbuf;
			}
			else
			{
				System.arraycopy(buf, offset + len, buf, offset, size - offset - len);
			}
			size = newsize;
		}
		return this;
	}
	
	private Octets doInsert(int offset, byte b)
	{
		if( size == 0 )
		{
			size = 1;
			buf = new byte[calCap(size)];
			buf[0] = b;
			return this;
		}
		int newsize = size + 1;
		int newcap = calCap(newsize);
		if( newcap > buf.length )
			buf = Arrays.copyOf(buf, newcap);
		if( offset != size )
			System.arraycopy(buf, offset, buf, offset + 1, size - offset);
		buf[offset] = b;
		size = newsize;
		return this;
	}
	
	private Octets doReplace(byte[] data, int srcoffset, int len)
	{
		size = len;
		if( size == 0 )
		{
			size = 0;
			buf = null;
		}
		else
			buf = Arrays.copyOfRange(data, srcoffset, srcoffset + len);
		return this;
	}
	
	public Octets insert(int offset, byte[] data, int srcoffset, int len) throws NullPointerException, IndexOutOfBoundsException
	{
		if( data == null )
			throw new NullPointerException();
		if( data.length == 0 )
			return this;
		if( offset < 0 || offset > size
				|| srcoffset < 0 || srcoffset >= data.length || len <= 0 || len + srcoffset > data.length )
			throw new IndexOutOfBoundsException();
		return doInsert(offset, data, srcoffset, len);
	}
	
	public Octets insert(int offset, byte b) throws IndexOutOfBoundsException
	{
		if( offset < 0 || offset > size )
			throw new IndexOutOfBoundsException();
		return doInsert(offset, b);
	}
	
	public Octets insert(int offset, byte[] data) throws NullPointerException, IndexOutOfBoundsException
	{
		if( data == null )
			throw new NullPointerException();
		if( data.length == 0 )
			return this;
		if( offset < 0 || offset > size )
			throw new IndexOutOfBoundsException();
		return doInsert(offset, data, 0, data.length);
	}
	
	public Octets insert(int offset, byte[] data, int srcoffset) throws NullPointerException, IndexOutOfBoundsException
	{
		if( data == null )
			throw new NullPointerException();
		if( data.length == 0 )
			return this;
		if( offset < 0 || offset > size || srcoffset < 0 || srcoffset >= data.length )
			throw new IndexOutOfBoundsException();
		return doInsert(offset, data, srcoffset, data.length - srcoffset);
	}
	
	public Octets replace(byte[] data, int srcoffset, int len) throws NullPointerException, IndexOutOfBoundsException
	{
		if( data == null )
			throw new NullPointerException();
		if( srcoffset < 0 || srcoffset > data.length || len < 0 || len + srcoffset > data.length )
			throw new IndexOutOfBoundsException();
		return doReplace(data, srcoffset, len);
	}
	
	public Octets replace(byte[] data) throws NullPointerException
	{
		if( data == null )
			throw new NullPointerException();
		return doReplace(data, 0, data.length);
	}
	
	public Octets replace(byte[] data, int srcoffset) throws NullPointerException, IndexOutOfBoundsException
	{
		if( data == null )
			throw new NullPointerException();
		if( srcoffset < 0 || srcoffset > data.length )
			throw new IndexOutOfBoundsException();
		return doReplace(data, srcoffset, data.length - srcoffset);
	}
	
	public Octets append(byte b)
	{
		return insert(size, b);
	}
	
	public Octets append(byte[] data) throws NullPointerException
	{
		return insert(size, data);
	}
	
	public Octets append(byte[] data, int offset) throws NullPointerException, IndexOutOfBoundsException
	{
		return insert(size, data, offset);
	}
	
	public Octets append(byte[] data, int offset, int len) throws NullPointerException, IndexOutOfBoundsException
	{
		return insert(size, data, offset, len);
	}
	
	public Octets erase(int offset, int len) throws IndexOutOfBoundsException
	{
		if( len == 0 )
			return this;
		if( offset < 0 || offset >= size || len <0 || offset + len > size )
			throw new IndexOutOfBoundsException();
		return doErase(offset, len);
	}
	
	public Octets erase(int offset)
	{
		if( offset < 0 || offset >= size )
			return this;
		return doErase(offset, size - offset);
	}
	
	public byte getByte(int offset) throws IndexOutOfBoundsException
	{
		if( offset < 0 || offset >= size )
			throw new IndexOutOfBoundsException();
		return buf[offset];
	}	
	
	private static int calCap(int size)
	{
		if( size <= 0 )
			return 0;
		int ret = 8;
		if( ret < size ) ret = size;
		return ret;
	}
	private byte[] buf = null;
	private int size = 0;
}

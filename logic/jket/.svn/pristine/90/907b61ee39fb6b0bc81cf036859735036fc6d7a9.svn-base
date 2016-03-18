
package ket.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitArray implements Cloneable
{

	@Override
	public boolean equals(Object other)
	{
		BitArray otherBitArray = (BitArray)other;
		if( other == null )
			return false;
		if( other == this )
			return true;
		return Arrays.equals(ba, otherBitArray.ba);
	}
	
	@Override
	public Object clone()
	{
		try
		{
			BitArray newBitArray = (BitArray)super.clone();
			return newBitArray.setArray(ba);
		}
		catch(CloneNotSupportedException ex)
		{			
		}
		return null;
	}
	
	public BitArray()
	{		
	}
	
	public BitArray(int size, boolean b)
	{
		resize(size).setAll(b);
	}
	
	public BitArray(boolean[] ba)
	{
		setArray(ba);
	}
	
	public BitArray setArray(boolean[] ba)
	{
		this.ba = ba.clone();
		return this;
	}
	
	public BitArray resize(int size)
	{
		ba = Arrays.copyOf(ba, size);
		return this;
	}
	
	public int size()
	{
		return ba.length;
	}
	
	public boolean isEmpty()
	{
		return ba.length == 0;
	}
	
	public boolean isSet(int index)
	{
		return ba[index];
	}
	
	public boolean isClear(int index)
	{
		return !ba[index];
	}
	
	public BitArray setBit(int index)
	{
		ba[index] = true;
		return this;
	}
	
	public BitArray setBit(int index, boolean b)
	{
		ba[index] = b;
		return this;
	}
	
	public BitArray clearBit(int index)
	{
		ba[index] = false;
		return this;
	}
	
	public BitArray clear()
	{
		ba = new boolean[0];
		return this;
	}
	
	public BitArray setAll(boolean b)
	{
		Arrays.fill(ba, b);
		return this;
	}
	
	public boolean contains(boolean b)
	{
		for(boolean e : ba)
		{
			if( e == b )
				return true;
		}
		return false;
	}
	
	public int count(boolean b)
	{
		int c = 0;
		for(boolean e : ba)
		{
			if( e == b )
				++c;
		}
		return c;
	}
	
	public boolean[] getArray()
	{
		return ba;
	}
	
	public ByteBuffer compress(int offset, int count)
	{
		byte[] bs = new byte[(count + 7)/8];
		for(int i = 0; i < count; ++i)
		{
			if( ba[offset + i] )
				bs[i/8] |= ( (byte)1 << (i%8) );				
		}
		ByteBuffer bb = ByteBuffer.wrap(bs);
		byte bZip = 0;
		if( bb.remaining() > 64 )
		{
			ByteBuffer bbCom = new ZipCompressor().compress(bb);
			if( bbCom.remaining() < bb.remaining() )
			{
				bb = bbCom;
				bZip = 1;
			}
		}
		bs = new byte[5 + bb.remaining()];
		bs[0] = (byte)(count&0xff);
		bs[1] = (byte)((count>>8)&0xff);
		bs[2] = (byte)((count>>16)&0xff);
		bs[3] = (byte)((count>>24)&0xff);
		bs[4] = bZip;
		System.arraycopy(bs, 5, bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
		return ByteBuffer.wrap(bs);
	}
	
	public ByteBuffer compress()
	{
		return compress(0, ba.length);
	}
	
	public boolean uncompress(ByteBuffer bb)
	{
		return false; // todo
	}
	
	public boolean uncompress(int offset, int count, ByteBuffer bb)
	{
		return false; // todo
	}

	private boolean[] ba = new boolean[0];
}

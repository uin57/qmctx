
package ket.util;

import java.util.Arrays;

public class WrappedByteBuffer implements Cloneable
{
	
	public WrappedByteBuffer()
	{
	}
	
	public WrappedByteBuffer(byte[] bytes, int offset, int len)
	{
		if( bytes == null || offset < 0 || offset > bytes.length || len < 0 || offset + len > bytes.length )
			return;
		this.bytes = bytes;
		this.offset = offset;
		this.len = len;		
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		WrappedByteBuffer b = (WrappedByteBuffer)obj;
		if( b == this )
			return true;
		if( b == null )
			return false;
		if( len != b.len )
			return false;
		if( bytes == null )
			return b.bytes == null;
		if( b.bytes == null )
			return false;
		if( bytes == b.bytes )
		{
			if( offset == b.offset )
				return true;
		}
		return Arrays.equals(Arrays.copyOfRange(bytes, offset, offset + len), Arrays.copyOfRange(b.bytes, b.offset, b.offset + b.len));
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return (WrappedByteBuffer)super.clone();
		}
		catch(CloneNotSupportedException ex)
		{			
		}
		return null;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 0;
		if( bytes != null )
		{
			for(int i= offset; i< offset + len; i+=4)
			{
				byte b1 = bytes[i];
				byte b2 = bytes[i+1];
				byte b3 = bytes[i+2];
				byte b4 = bytes[i+3];
				int j = ( ((int)b4) <<24 )|( ((int)b3<<16)&0xffffff )|( ((int)b2<<8)&0xffff )| ( ((int)(b1))&0xff );
				hash ^= j;
			}
		}
		return hash;
	}
	
	protected byte[] bytes = null;
	protected int offset = 0;
	protected int len = 0;
}

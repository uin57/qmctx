
package ket.util;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class Bytes implements Cloneable
{
	
	public Bytes(byte[] bytes)
	{
		this.bytes = bytes;		
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Bytes b = (Bytes)obj;
		if( b == this )
			return true;
		if( b == null )
			return false;
		if( bytes == null )
			return b.bytes == null;
		if( b.bytes == null )
			return false;
		return Arrays.equals(bytes, b.bytes);
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return (Bytes)super.clone();
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
		try
		{
			if( bytes.length % 4 == 0 )
			{
				Stream.AIStream ais = new Stream.IStreamLE(new ByteArrayInputStream(bytes));
				for(int i= 0; i< bytes.length; i+=4)
				{
					hash ^= ais.popInteger();
				}
			}
		}
		catch(Exception ex)
		{
			return 0;
		}
		return hash;
	}
	
	protected byte[] bytes = null;
}

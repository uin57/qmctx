
package ket.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CopiedMemoryDataStorage implements Storage
{
	
	public CopiedMemoryDataStorage(byte[] data, int offset, int len)
	{
		try
		{
			this.data = Arrays.copyOfRange(data, offset, offset + len);
		}
		catch(Exception ex)
		{			
		}
	}
	
	@Override
	public boolean isOK()
	{
		return data != null;
	}

	@Override
	public long getLength()
	{
		return ! isOK() ? 0 : data.length;
	}
	
	@Override
	public ByteBuffer read(long offset, int len, byte[] dstbuf)
	{
		if( offset < 0 || offset > Integer.MAX_VALUE )
			return null;
		int iOffset = (int)offset;
		if( ! isOK() || iOffset < 0 || len < 0 || iOffset + len < 0
				|| iOffset + len > getLength() )
			return null;
		return ByteBuffer.wrap(data, iOffset, len);
	}

	@Override
	public boolean write(long offset, int len, byte[] srcbuf)
	{
		if( offset < 0 || offset > Integer.MAX_VALUE )
			return false;
		int iOffset = (int)offset;
		if( ! isOK() || iOffset < 0 || len < 0 || iOffset + len < 0
				|| iOffset + len > getLength() || srcbuf == null || len > srcbuf.length )
			return false;
		try
		{
			System.arraycopy(srcbuf, 0, data, iOffset, len);
			return true;
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	private byte[] data;
}

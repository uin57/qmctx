
package ket.util;

import java.nio.ByteBuffer;

import ket.util.WrappedByteBuffer;

public class WrappedMemoryDataStorage extends WrappedByteBuffer implements Storage
{
	
	public WrappedMemoryDataStorage(byte[] data, int offset, int len)
	{
		super(data, offset, len);
	}
	
	@Override
	public boolean isOK()
	{
		return bytes != null;
	}

	@Override
	public long getLength()
	{
		return ! isOK() ? 0 : len;
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
		return ByteBuffer.wrap(bytes, this.offset + iOffset, len);
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
			System.arraycopy(srcbuf, 0, bytes, this.offset + iOffset, len);
			return true;
		}
		catch(Exception ex)
		{
		}
		return false;
	}

}


package ket.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class FileDataSource implements Storage
{

	public FileDataSource(String fn)
	{
		fileName = fn;
		if( fn == null )
			return;
		try
		{
			File file = new File(fn);
			if( file.exists() && file.isFile() && file.canRead() )
			{
				length = file.length();
				isOK = true;
			}
		}
		catch(Exception ex)
		{			
		}
	}
	
	public FileDataSource(String fn, long offset, int len)
	{
		fileName = fn;
		if( offset < 0 || len < 0 || fn == null )
			return;
		fileOffset = offset;
		try
		{
			File file = new File(fn);
			if( file.exists() && file.isFile() && file.canRead() )
			{
				long end = offset + len;
				if( end < 0 || end > file.length() )
					return;
				length = len;
				isOK = true;
			}
		}
		catch(Exception ex)
		{			
		}
	}
	
	@Override
	public boolean isOK()
	{
		return isOK;
	}
	
	@Override
	public long getLength()
	{
		return length;
	}
	
	@Override
	public ByteBuffer read(long offset, int len, byte[] dstbuf)
	{
		if( ! isOK() || offset < 0 || len < 0 || offset + len < 0 || offset + len > getLength() ) 
			return null;
		try
		{
			ByteBuffer buffer;
			if( dstbuf == null )
				buffer = ByteBuffer.allocate(len);
			else if( dstbuf.length < len )
				return null;
			else
				buffer = ByteBuffer.wrap(dstbuf);
			FileInputStream is = new FileInputStream(fileName);
			Stream.skipAll(is, fileOffset + offset);
			Stream.readAll(is, buffer.array(), 0, len);
			is.close();
			return (ByteBuffer)buffer.position(0);
		}
		catch(Exception ex)
		{			
		}
		return null;
	}

	@Override
	public boolean write(long offset, int len, byte[] srcbuf)
	{
		return false;
	}
	
	public String getName()
	{
		return fileName;
	}

	private String fileName;
	private boolean isOK = false;
	private long length = 0;
	private long fileOffset = 0;
}


package ket.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;

import ket.util.Stream;

public class FileMappingDataStorage implements Storage
{

	public FileMappingDataStorage()
	{
		
	}
	
	public FileMappingDataStorage(String fn, long len, boolean bNewFile)
	{
		this.fileName = fn;
		if( fn == null )
			return;
		File file = new File(fn);
		boolean bFileOK = file.exists() && file.isFile();
		if( bNewFile )
		{
			boolean bCreateFileOK = false;
			try
			{
				bCreateFileOK = ! bFileOK && ! file.exists() && file.createNewFile();
				if( bFileOK || bCreateFileOK )
				{
					RandomAccessFile raf = new RandomAccessFile(file, "rw");
					raf.setLength(len);
					raf.close();
					length = len;
					isOK = true;
					return;
				}
			}
			catch(SecurityException ex)
			{			
			}
			catch(IOException ex)
			{	
				if( bCreateFileOK )
					file.delete();
			}
		}
		else
		{
			try
			{
				if( bFileOK && file.canRead() && file.canWrite() && file.length() == len )
				{
					length = len;
					isOK = true;
				}
			}
			catch(SecurityException ex)
			{			
			}
		}
	}
	
	public String getFileName()
	{
		return fileName;
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
			Stream.skipAll(is, offset);
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
		if( ! isOK() || offset < 0 || len < 0 || offset + len < 0 || offset + len > getLength() 
				|| srcbuf == null || len > srcbuf.length ) 
			return false;
		try
		{
			RandomAccessFile fileRA = new RandomAccessFile(fileName, "rw");
			fileRA.seek(offset);
			fileRA.write(srcbuf, 0, len);
			fileRA.close();
			return true;
		}
		catch(IOException ex)
		{			
		}
		return false;
	}

	private String fileName;
	private boolean isOK = false;
	private long length = 0;
}

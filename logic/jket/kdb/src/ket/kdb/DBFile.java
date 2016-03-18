
package ket.kdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;

class DBFile
{	
	
	private DBFile()
	{		
	}
	
	private DBFile open(File file) throws FileNotFoundException
	{
		raFile = new RandomAccessFile(file, "rw");
		return this;
	}
	
	public static DBFile newInstance(File file)
	{
		try
		{
			return new DBFile().open(file);
		}
		catch(FileNotFoundException ex)
		{			
			ex.printStackTrace();
		}
		return null;
	}		
	
	public synchronized boolean read(long offset, ByteBuffer bb)
	{
		try
		{
			raFile.seek(offset);
			return bb.remaining() == raFile.read(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file read failed.", ex);
		}
	}
	
	public synchronized void write(long offset, ByteBuffer bb)
	{
		try
		{
			raFile.seek(offset);
			raFile.write(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file write failed.", ex);
		}
	}
	
	public synchronized boolean tryRead(final long fileOffset, final byte[] data, final int offset, final int len)
	{
		try
		{
			raFile.seek(fileOffset);
			return len == raFile.read(data, offset, len);
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file read failed.", ex);
		}
	}
	
	public synchronized void read(final long fileOffset, final byte[] data, final int offset, final int len)
	{
		try
		{
			raFile.seek(fileOffset);
			if( len != raFile.read(data, offset, len) )
				throw new Error("DBErr: db file read failed.");
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file read failed.", ex);
		}
	}
	
	public synchronized void write(final long fileOffset, final byte[] data, final int offset, final int len)
	{
		try
		{
			raFile.seek(fileOffset);
			raFile.write(data, offset, len);
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file write failed.", ex);
		}
	}
	
	public synchronized void sync()
	{
		try
		{
			raFile.getFD().sync();
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file sync failed.", ex);
		}
	}
	
	public synchronized void clear()
	{
		try
		{
			raFile.setLength(0);
			raFile.getFD().sync();
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file clear failed.", ex);
		}
	}
	
	public synchronized void close()
	{
		try
		{
			raFile.getFD().sync();
			raFile.close();
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db file close failed.", ex);
		}
	}
	
	private RandomAccessFile raFile;
}

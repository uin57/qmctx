
/**
 * @date 2011.12.07
 */
package ket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;

/**
 * An utility class for file and directory.
 * <p>
 * Unlike {@link java.io.File}, all methods of this class make 
 * a difference between <em>normal file</em> and <em>directory</em>.
 * <p>
 * @see java.io.File
 */
public class FileTool 
{
	private FileTool()
	{		
	}
	/**
     * Tests whether the file exists.
     *
     * @return  <code>true</code> if the file exists; <code>false</code> otherwise
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>file</code> is <code>null</code>
     */
	public static boolean fileExist(File file)
	{
		return file.exists() && file.isFile();
	}
	
	/**
     * Tests whether the file exists.
     *
     * @return  <code>true</code> if the file exists; <code>false</code> otherwise
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>file</code> is <code>null</code>
     */
	public static boolean fileExist(String file)
	{
		return fileExist(new File(file));
	}
	
	/**
     * Tests whether the directory exists.
     *
     * @return  <code>true</code> if the dir exists; <code>false</code> otherwise
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>dir</code> is <code>null</code>
     */
	public static boolean directoryExist(File dir)
	{
		return dir.exists() && dir.isDirectory();
	}
	
	/**
     * Tests whether the directory exists.
     *
     * @return  <code>true</code> if the dir exists; <code>false</code> otherwise
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>dir</code> is <code>null</code>
     */
	public static boolean directoryExist(String dir)
	{
		return directoryExist(new File(dir));
	}
	
	/**
     * Ensure that the empty file will exist.
     *
     * @return  <code>true</code> if the empty file exists or was created; <code>false</code> otherwise
     * 
     * @throws  IOException
     *          If an I/O error occurred
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>file</code> is <code>null</code>
     */
	public static boolean ensureEmptyFileExist(File file) throws IOException
	{
		if( file.exists() )
		{
			if( ! file.delete() )
				return false;
		}
		else
		{
			File parent = file.getAbsoluteFile().getParentFile();
			if( parent != null && ! parent.exists() && ! parent.mkdirs() )
				return false;
		}
		return file.createNewFile();
	}
	
	/**
     * Ensure that the file will exist.
     *
     * @return  <code>true</code> if the file exists or was created; <code>false</code> otherwise
     * 
     * @throws  IOException
     *          If an I/O error occurred
     *
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>file</code> is <code>null</code>
     */
	public static boolean ensureEmptyFileExist(String file) throws IOException
	{
		return ensureEmptyFileExist(new File(file));
	}
	
	/**
     * Ensure that the directory will exist.
     *
     * @return  <code>true</code> if the dir exists or was created; <code>false</code> otherwise
     * 
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>dir</code> is <code>null</code>
     */
	public static boolean ensureDirectoryExist(File dir)
	{
		if( dir.exists() )
			return dir.isDirectory();
		return dir.mkdirs();
	}
	
	/**
     * Ensure that the directory will exist.
     *
     * @return  <code>true</code> if the dir exists or was created; <code>false</code> otherwise
     * 
     * @throws  SecurityException
     *          If a security manager exists and its denies this operation
     *          
     * @throws  NullPointerException  
     *          If parameter <code>dir</code> is <code>null</code>
     */
	public static boolean ensureDirectoryExist(String dir)
	{
		return ensureDirectoryExist(new File(dir));
	}
	
	public static boolean isAbsolute(String pathname)
	{
		return new File(pathname).isAbsolute();
	}
	
	public static byte[] readFile(File file)
	{
		try
		{
			InputStream is = new FileInputStream(file);
			byte[] buf = new byte[(int)file.length()];
			is.read(buf);
			return buf;
		}
		catch(Exception ex)
		{
		}
		return null;
	}
	
	public static boolean copyFile(File fSrc, File fDst) throws IOException
	{
		final int COPY_BUF_SIZE = 8192;
		InputStream is = new FileInputStream(fSrc);
		try
		{
			if( fDst.exists() )
			{
				if( ! fDst.isFile() )
					return false;
			}
			else
			{
				File parent = fDst.getAbsoluteFile().getParentFile();
				if( parent != null && ! parent.exists() && ! parent.mkdirs() )
					return false;
			}
			FileOutputStream os = new FileOutputStream(fDst);
			FileChannel fc = os.getChannel();
			if( fc.size() != 0 )
				fc.truncate(0L);
			try
			{
				byte[] buf = new byte[COPY_BUF_SIZE];
				while( true )
				{
					int n = is.read(buf);
					if( n == -1 )
						return true;
					os.write(buf, 0, n);
				}
			}
			finally
			{
				os.close();
			}
		}
		finally
		{
			is.close();
		}
	}
	
	public static boolean copyFile(String fnSrc, String fnDst) throws IOException
	{
		return copyFile(new File(fnSrc), new File(fnDst));
	}
}

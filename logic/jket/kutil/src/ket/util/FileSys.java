
package ket.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

public class FileSys
{
	
	public static boolean moveFile(String src, String dst)
	{
		File fileSrc = new File(src);
		if( ! fileSrc.exists() )
			return false;
		File fileDst = new File(dst);
		if( fileDst.exists() && ! fileDst.delete() )
			return false;		
		return fileSrc.renameTo(fileDst);
	}
	
	public static interface ITraversalVisitor
	{
		public static class BreakException extends Exception
		{
			private static final long serialVersionUID = 1L;
			
		}
		public void visitFile(String pathname, String name, int depth) throws BreakException;
		public boolean onEnterDirectory(String pathname, String name, int depth) throws BreakException;
		public void onLeaveDirectory(String pathname, String name, int depth) throws BreakException;
	}
	
	private static void traverse(File fDir, ITraversalVisitor visitor, int depthMax, int depth) throws ITraversalVisitor.BreakException
	{
		if( depthMax >= 0 && depth > depthMax )
			return;
		
		if( ! visitor.onEnterDirectory(fDir.getAbsolutePath(), fDir.getName(), depth) )
			return;
		
		if( depthMax < 0 || depth < depthMax )
		{		
			String[] lst = fDir.list();
			for(String name : lst)
			{
				File f = null;
				try
				{
				f = new File(fDir.getAbsolutePath() + File.separator + name).getCanonicalFile();
				}
				catch(Exception ex)
				{
					continue;
				}
				if( f.isFile() )
				{
					visitor.visitFile(f.getAbsolutePath(), f.getName(), depth + 1);
				}
				else if( f.isDirectory() )
				{
					traverse(f, visitor, depthMax, depth + 1);
				}
			}
		}
		visitor.onLeaveDirectory(fDir.getAbsolutePath(), fDir.getName(), depth);
	}
	
	public static boolean traverse(String pathname, ITraversalVisitor visitor, int depthMax)
	{
		try
		{
			File fRoot = new File(pathname).getCanonicalFile();
			if( ! fRoot.exists() )
				return false;
			if( fRoot.isFile() )
			{
				visitor.visitFile(fRoot.getAbsolutePath(), fRoot.getName(), 0);
			}
			else if( fRoot.isDirectory() )
			{
				traverse(fRoot, visitor, depthMax, 0);
			}
			else
				return false;
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static boolean deleteFile(String pathname)
	{
		return traverse(pathname,
				new ITraversalVisitor()
				{
					@Override
					public void visitFile(String pathname, String name, int depth) throws BreakException
					{
						if( ! new File(pathname).delete() )
							throw new BreakException();
					}

					@Override
					public boolean onEnterDirectory(String pathname, String name, int depth) throws BreakException
					{
						return true;
					}

					@Override
					public void onLeaveDirectory(String pathname, String name, int depth) throws BreakException
					{
						if( ! new File(pathname).delete() )
							throw new BreakException();
					}
			
				}, -1);
	}	
	
	public static boolean copyFile(String src, String dstdir)
	{
		try
		{
			if( src == null || dstdir == null )
				return false;
			File fDstdir = new File(dstdir).getCanonicalFile();
			if( ! fDstdir.exists() || ! fDstdir.isDirectory() )
				return false;
			File fSrc = new File(src).getCanonicalFile();
			if( ! fSrc.exists() )
				return false;
			if( fDstdir.getAbsolutePath().startsWith(fSrc.getAbsolutePath()) )
				return false;
			if( fSrc.getParentFile().getAbsolutePath().equals(fDstdir.getAbsolutePath()) )
				return false;
			File fDst = new File(fDstdir.getAbsolutePath() + File.separator + fSrc.getName());
			if( fSrc.isFile() )
			{				
				if( fDst.exists() )
					fDst.delete();
				if( ! fDst.createNewFile() )
					return false;
				FileInputStream is = new FileInputStream(fSrc);
				FileOutputStream os = new FileOutputStream(fDst);
				byte[] buf = new byte[8192];
				while( true )
				{
					int n = is.read(buf);
					if( n == -1 )
						break;
					if( n == 0 )
						continue;
					os.write(buf, 0, n);
				}
				is.close();
				os.close();				
			}
			else if( fSrc.isDirectory() )
			{
				if( !fDst.exists() && ! fDst.mkdir() )
					return false;
				String[] lst = fSrc.list();
				for(String fn : lst)
				{
					copyFile(fSrc.getAbsolutePath() + File.separator + fn, fDst.getAbsolutePath());
				}
			}
			else
				return false;
			return true;
		}
		catch(Exception ex)
		{			
		}
		return false;
	}
	
	public static void pause(boolean bBackGround)
	{
		if( ! bBackGround && System.console() != null )
			System.console().readLine();
		else
		{
			try
			{
				Thread.currentThread().join();
			}
			catch(Exception ex)
			{				
			}
		}
	}
	
	public static void pauseWaitInput()
	{
		if( System.console() != null )
			System.console().readLine();
		else
		{
			try
			{
				Thread.currentThread().join();
			}
			catch(Exception ex)
			{				
			}
		}
	}
	
	public static void pauseWaitSingal(CountDownLatch latch)
	{
		try
		{
			latch.await();
		}
		catch(Exception ex)
		{			
		}
	}
	
	public static int getLocalTime()
	{
		//return (int)(java.util.Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime().getTime()/1000);
		return (int)(new java.util.Date().getTime()/1000) + 8 * 3600;
	}
}

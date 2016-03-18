
package ket.kpc.common;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import ket.util.ArgsMap;

public class LogTool
{
	private static class DownloadStep
	{
		public String pieceDigest;
		public String sourceAnswer;
		public String requestRegister;
		public String downloadComplete;
		public int n = 0;
	}
	
	private static void parseLog(String fn) throws IOException
	{
		LineNumberReader reader = new LineNumberReader(new FileReader(fn));					
		String line = null;
		Map<String, DownloadStep> map = new HashMap<String, DownloadStep>();
		while( true )
		{
			line = reader.readLine();
			if( line == null )
				break;
			DownloadStep ds = null;
			String pieceDigestPrefix = "piece digest is["; 
			int i = line.indexOf(pieceDigestPrefix);
			if( i != -1 )
			{
				String digest = line.substring(i + pieceDigestPrefix.length(), i + pieceDigestPrefix.length() + 40);
				ds = map.get(digest);
				if( ds == null )
				{
					ds = new DownloadStep();
					ds.pieceDigest = line;
					ds.n = 1;
					map.put(digest, ds);
				}
				else
				{
					if( ds.pieceDigest == null )
					{
						ds.pieceDigest = line;
						ds.n++;
					}
				}
				continue;
			}
			String sourceAnswerPrefix = "on source answer, digest is ["; 
			i = line.indexOf(sourceAnswerPrefix);
			if( i != -1 )
			{
				String digest = line.substring(i + sourceAnswerPrefix.length(), i + sourceAnswerPrefix.length() + 40);
				ds = map.get(digest);
				if( ds == null )
				{
					ds = new DownloadStep();
					ds.sourceAnswer = line;
					ds.n = 1;
					map.put(digest, ds);
				}
				else
				{
					if( ds.sourceAnswer == null )
					{
						ds.sourceAnswer = line;
						ds.n++;
					}
				}
				continue;
			}
			String requestRegisterPrefix = "request register, digest is ["; 
			i = line.indexOf(requestRegisterPrefix);
			if( i != -1 )
			{
				String digest = line.substring(i + requestRegisterPrefix.length(), i + requestRegisterPrefix.length() + 40);
				ds = map.get(digest);
				if( ds == null )
				{
					ds = new DownloadStep();
					ds.requestRegister = line;
					ds.n = 1;
					map.put(digest, ds);
				}
				else
				{
					if( ds.requestRegister == null )
					{
						ds.requestRegister = line;
						ds.n++;
					}
				}
				continue;
			}
			String downloadCompletePrefix = "on download complete, file is ["; 
			i = line.indexOf(downloadCompletePrefix);
			if( i != -1 )
			{
				String digest = line.substring(i + downloadCompletePrefix.length(), i + downloadCompletePrefix.length() + 40);
				ds = map.get(digest);
				if( ds == null )
				{
					ds = new DownloadStep();
					ds.downloadComplete = line;
					ds.n = 1;
					map.put(digest, ds);
				}
				else
				{
					if( ds.downloadComplete == null )
					{
						ds.downloadComplete = line;
						ds.n++;
					}
				}
				continue;
			}
		}
		for(Map.Entry<String, DownloadStep> e : map.entrySet())
		{
			if( e.getValue().downloadComplete == null )
				System.out.println(e.getKey() + " " + e.getValue().n);
		}
		reader.close();
	}

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		try
		{
			String fn = am.get("-fn");
			parseLog(fn);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}


package ket.kdb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ket.util.FileTool;
import ket.util.Stream;
import ket.xml.ReaderException;
import ket.xml.Node;

class Config
{
	
	public static class ThreadPoolConfig
	{
		public int corePoolSize = 1;
		public int maxTaskCount = 80000;
	}
	
	public static class CheckPointConfig
	{
		public int intervalMin = 0; //s
		public int intervalMax = 0; //s		
	}
	
	public static class BackupConfig
	{
		public int interval = 0; //s		
	}
	
	public static class TableConfig
	{
		public TableConfig(String name)
		{
			this.name = name;
		}
		
		public final String name;
		public int cacheLow = 1; // M
		public int cacheHigh = 100; // M
		public Class<?> classKey = null;
		public Class<?> classValue = null;
	}

	public void load(Path pathDBDir, Path pathCfgFile)
	{
		try
		{
			Node root = ket.xml.Factory.newReader(pathCfgFile.toFile()).getRoot();
			
			parseDataDir(root.getChild("datadir"), pathDBDir);
			parseLogDir(root.getChild("logdir"), pathDBDir);
			parseBackupDir(root.getChild("backupdir"), pathDBDir);
			parseTables(root.getChildren("table"));
			root.getChild("threadpool", threadPool);
			root.getChild("checkpoint", checkPoint);
			root.getChild("backup", backup);
			this.pathCfgFile = pathCfgFile;
		}
		catch(ReaderException ex)
		{
			throw new Error("DBErr: db config load exception", ex);
		}
		catch(IOException ex)
		{
			throw new Error("DBErr: db config load exception", ex);
		}
	}
	
	public void dump(Logger logger)
	{
		logger.info("================db config dump=================");
		logger.info("data dir is: " + datadir);
		logger.info("log dir is: " + logdir);
		logger.info("backup dir is: " + backupdir);
		logger.info("tables count: " + tables.size());
		for(TableConfig table : tables)
		{
			logger.info("    " + table.name + ", cacheLow is " + table.cacheLow + "(M)" + ", cacheHigh is " + table.cacheHigh + "(M)");
		}
		logger.info("thread core pool size is " + threadPool.corePoolSize + ", maxTaskCount is " + threadPool.maxTaskCount);
		logger.info("check point intervalMin is " + checkPoint.intervalMin + "(s), intervalMax is " + checkPoint.intervalMax + "(s)");
		logger.info("backup interval is " + backup.interval + "(s)");
		logger.info("================db config dump=================");
	}
	
	private void parseDataDir(Node node, Path pathDBDir) throws ReaderException, IOException
	{
		if( node != null )
		{
			String name = node.getString("name");
			datadir = pathDBDir.resolve(Paths.get(name));
				
		}
		else
			datadir = pathDBDir.resolve("data");
		if( ! ( Files.exists(datadir) && Files.isDirectory(datadir) ) && null == Files.createDirectory(datadir) )
			throw new Error("DBErr: bad dir name: " + datadir);
	}
	
	private void parseLogDir(Node node, Path pathDBDir) throws ReaderException, IOException
	{
		if( node != null )
		{
			String name = node.getString("name");
			logdir = pathDBDir.resolve(Paths.get(name));
				
		}
		else
			logdir = pathDBDir.resolve("log");
		if( ! ( Files.exists(logdir) && Files.isDirectory(logdir) ) && null == Files.createDirectory(logdir) )
			throw new Error("DBErr: bad dir name: " + logdir);
	}	
	
	private void parseBackupDir(Node node, Path pathDBDir) throws ReaderException, IOException
	{
		if( node != null )
		{
			String name = node.getString("name");
			backupdir = pathDBDir.resolve(Paths.get(name));
				
		}
		else
			backupdir = pathDBDir.resolve("backup");
		if( ! ( Files.exists(backupdir) && Files.isDirectory(backupdir) ) && null == Files.createDirectory(backupdir) )
			throw new Error("DBErr: bad dir name: " + backupdir);
	}
	
	private Class<?> getClass(String type) throws ReaderException
	{
		try
		{
			Class<?> classRet = Class.forName(type);
			if( classRet != Integer.class && classRet != Long.class && classRet != String.class )
			{
				if( ! ( classRet.newInstance() instanceof Stream.IStreamable) )
					throw new ReaderException("type " + type + " should imp Stream.IStreamable or [Integer,Long,String]");
			}
			return classRet;
		}
		catch(Exception ex)
		{
			throw new ReaderException("type " + type + " is not found or deny access", ex);
		}
	}
	
	private void parseTables(List<Node> nodes) throws ReaderException
	{
		Set<String> names = new HashSet<String>();
		for(Node node : nodes)
		{
			String name = node.getString("name");
			if( names.contains(name) )
				throw new Error("DBErr: bad cfg file, dup table name " + name);
			names.add(name);
			TableConfig table = new TableConfig(name);
			table.cacheLow = node.getInteger("cacheLow", table.cacheLow);
			table.cacheHigh = node.getInteger("cacheHigh", table.cacheHigh);
			// TODO
			String keyType = node.getString("keyType", null);
			if( keyType != null )
				table.classKey = getClass(keyType);
			String valueType = node.getString("valueType", null);
			if( valueType != null )
				table.classValue = getClass(valueType);
			tables.add(table);
		}	
	}
	
	public Path getDataDirectory()
	{
		return datadir;
	}
	
	public Path getLogDirectory()
	{
		return logdir;
	}
	
	public Path getBackupDirectory()
	{
		return backupdir;
	}
	
	public Path getConfigFile()
	{
		return pathCfgFile;
	}
	
	public List<TableConfig> getTablesConfig()
	{
		return tables;
	}
	
	public final String getLogFileName()
	{
		return LOG_FILE_NAME;
	}
	
	public final ThreadPoolConfig getThreadPoolConfig()
	{
		return threadPool;
	}
	
	public CheckPointConfig getCheckPointConfig()
	{
		return checkPoint;
	}
	
	public BackupConfig getBackupConfig()
	{
		return backup;
	}
	
	private static String LOG_FILE_NAME = "db.log";
	
	private Path pathCfgFile;
	private Path datadir;
	private Path logdir;
	private Path backupdir;
	private List<TableConfig> tables = new ArrayList<TableConfig>();
	private ThreadPoolConfig threadPool = new ThreadPoolConfig();
	private CheckPointConfig checkPoint = new CheckPointConfig();
	private BackupConfig backup = new BackupConfig();
}

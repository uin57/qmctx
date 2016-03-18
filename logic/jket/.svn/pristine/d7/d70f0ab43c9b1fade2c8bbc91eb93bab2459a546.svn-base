
package ket.kdb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import ket.util.ExecutorTool;

class DBImp implements DB
{
	
	private static final class Statistics
	{
		public void decTaskCountQueued()
		{
			taskCountQueued.decrementAndGet();
		}
		
		public void incTaskCountQueued()
		{
			taskCountQueued.incrementAndGet();
		}
		
		public int getTaskCountQueued()
		{
			return taskCountQueued.intValue();
		}
		
		public int getTaskCountRejected()
		{
			return taskCountRejected.intValue();
		}
		
		public void logTransTime(final long transTime)
		{
			if( transTime > maxTransTime )
				maxTransTime = transTime;
		}
		
		public long getMaxTransTime()
		{
			return maxTransTime;
		}
		
		public void clear()
		{
			maxTransTime = 0;
			taskCountRejected.set(0);
		}
		
		private AtomicInteger taskCountQueued = new AtomicInteger();
		private AtomicInteger taskCountRejected = new AtomicInteger();
		private volatile long maxTransTime = 0;
	}
	
	private class CheckPointTask implements Runnable
	{
		@Override
		public void run()
		{
			long now = System.currentTimeMillis();
			if( now >= lastCheck + (long)(config.getCheckPointConfig().intervalMax) * 1000
					|| isCacheOverflow() )
			{
				try // for debug
				{
					checkPoint();
					lastCheck = now;
				}
				catch(Throwable e)
				{
					logger.error("db checkpoint exception[" + e.getMessage()
							+ "], throwed by db thread, abort", e);
					System.exit(0);
				}
			}
		}	
		
		public ScheduledFuture<?> future;
		private long lastCheck = System.currentTimeMillis();
	}
	
	private class BackupTask implements Runnable
	{
		@Override
		public void run()
		{
			try // for debug
			{
				backup();
			}
			catch(Throwable e)
			{
				logger.warn("db backup exception[" + e.getMessage()
						+ "], throwed by db thread", e);
				//System.exit(0);
			}
		}	
		
		public ScheduledFuture<?> future;
	}
	
	private class TransactionTask implements Runnable
	{
		public TransactionTask(Transaction transaction)
		{
			this.transaction = transaction;
		}

		@Override
		public void run()
		{	
			try // for debug
			{
				stat.decTaskCountQueued();
				long now = System.currentTimeMillis();
				TransImp transimp = new TransImp();	
			
				Transaction.ErrorCode errcode = transimp.begin(transaction, DBImp.this);
				if( errcode != Transaction.ErrorCode.eOK )
				{
					transaction.onCallback(errcode);
					return;
				}
			
				try
				{
					errcode = transaction.doTransaction() ? Transaction.ErrorCode.eOK : Transaction.ErrorCode.eUserCancel;
				}
				catch(RuntimeException ex)
				{
					transimp.rollback();
					throw ex;
				}		
				catch(Throwable e)
				{
					logger.warn("db transaction exception[" + e.getMessage()
							+ "], throwed by db thread", e);
				}
			
				if( errcode == Transaction.ErrorCode.eOK )
					transimp.commit();
				else
					transimp.rollback();
				stat.logTransTime(System.currentTimeMillis() - now);
				transaction.onCallback(errcode);
			}
			catch(Throwable e)
			{
				logger.error("db transaction exception[" + e.getMessage()
						+ "], throwed by db thread, abort", e);
				System.exit(0);
			}
		}	
	
		private Transaction transaction;
	}
	
	private boolean isCacheOverflow()
	{
		for(AbstractTable table : tables)
			if( table.isCacheOverflow() )
				return true;
		return false;
	}

	@Override
	public void open(Path pathDBDir, Path pathCfgFile)
	{	
		config = new Config();
		config.load(pathDBDir, pathCfgFile);
		config.dump(logger);
					
		int seq = 0;
		tables = new AbstractTable[config.getTablesConfig().size()];
		for(Config.TableConfig tablecfg : config.getTablesConfig())
		{
			AbstractTable table = new HashTableImp(config.getDataDirectory() + File.separator + tablecfg.name, false); // TODO B+TreeImp
			table.setSeq(seq);
			table.setName(tablecfg.name);
			table.setCacheLow(tablecfg.cacheLow);
			table.setCacheHigh(tablecfg.cacheHigh);
			table.setClassKey(tablecfg.classKey);
			table.setClassValue(tablecfg.classValue);
			tables[seq] = table;
			tableMap.put(tablecfg.name, table);
			++seq;
		}
		
		log = new Log(config.getLogDirectory() + File.separator + config.getLogFileName(), tableMap);
		log.recover();
		
		executor = ExecutorTool.newScheduledThreadPool(config.getThreadPoolConfig().corePoolSize, "DBThread");
		if( config.getCheckPointConfig().intervalMin > 0 )
			cpt.future = executor.scheduleWithFixedDelay(cpt, config.getCheckPointConfig().intervalMin, config.getCheckPointConfig().intervalMin, TimeUnit.SECONDS);
		// TODO
		long interval = config.getBackupConfig().interval;
		if( interval > 0 )
		{
			long now = new java.util.Date().getTime()/1000;
			long delay = now % interval;
			if( delay != 0 )
				delay = interval - delay;
			bkt.future = executor.scheduleAtFixedRate(bkt, delay, interval, TimeUnit.SECONDS);
		}
	}

	@Override
	public void close()
	{
		if( cpt.future != null )
			cpt.future.cancel(false);
		if( bkt.future != null )
			bkt.future.cancel(false);
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception e)
		{
			logger.warn("db shutdown exception[" + e.getMessage()
					+ "]", e);
		}
		executor = null;
		if( config.getCheckPointConfig().intervalMin > 0 )
			checkPoint();
		log.close();
	}	

	@Override
	public void execute(Transaction trans)
	{		
		if( stat.getTaskCountQueued() > config.getThreadPoolConfig().maxTaskCount )
		{
			trans.onCallback(Transaction.ErrorCode.eRejected);
			return;
		}
		
		try
		{
			stat.incTaskCountQueued();
			executor.execute(new TransactionTask(trans));
		}
		catch(NullPointerException e)
		{
			trans.onCallback(Transaction.ErrorCode.eRejected);
			//TODO
			logger.warn("db execute exception[" + e.getMessage()
					+ "]", e);
		}
		catch(RejectedExecutionException e)
		{
			trans.onCallback(Transaction.ErrorCode.eRejected);
			//TODO
			logger.warn("db execute exception[" + e.getMessage()
					+ "]", e);
		}
	}
	
	private void lockAllTables()
	{
		for(AbstractTable table : tables)
			table.getLock().writeLock().lock();	
	}
	
	private void unlockAllTables()
	{
		for(AbstractTable table : tables)
			table.getLock().writeLock().unlock();
	}
	
	public void clear()
	{
		lockAllTables();
		log.clear();
		unlockAllTables();
	}
	
	private synchronized void checkPoint()
	{
		lockAllTables();
		long now = System.currentTimeMillis();
		logger.info("checkpoint at " + new SimpleDateFormat("hh:mm:ss").format(new Date())
				+ ", " + stat.getTaskCountQueued() + " appending, " + stat.getTaskCountRejected() 
				+ " rejected, maxTransTime is " + stat.getMaxTransTime() + "(ms)");	
		Map<AbstractTable, List<PageViewWithReader>> pvsDirtyMap = new HashMap<AbstractTable, List<PageViewWithReader>>();
		int pvCount = 0;
		for(AbstractTable table : tableMap.values())
		{
			logger.debug("    table " + table.getName() + ":");
			List<PageViewWithReader> pvsDirty = table.getBufferManager().gatherDirtyPagesWithReader(table.getCacheLow(), logger);
			pvCount += pvsDirty.size();
			if( ! pvsDirty.isEmpty() )
				pvsDirtyMap.put(table, pvsDirty);
		}
		unlockAllTables();
		if( ! pvsDirtyMap.isEmpty() )
			log.checkPoint(pvsDirtyMap, pvCount);
		logger.info(", check " + (System.currentTimeMillis() - now) + "(ms)");
		stat.clear();
		//unlockAllTables();
	}
	
	private synchronized void backup() throws IOException
	{
		String stamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		logger.info("do backup at " + stamp);
		
		Path p = config.getBackupDirectory();
		Path tmpd = Files.createTempDirectory(p, "kdbtmp_");
		Path srcc = config.getConfigFile();
		Path dstc = tmpd.resolve(srcc.getFileName());
		Files.copy(srcc, dstc);
		Path srcd = config.getDataDirectory();
		Path dstd = tmpd.resolve(srcd.getFileName());
		Files.copy(srcd, dstd);
		for(AbstractTable table : tableMap.values())
		{
			logger.debug("    copy table " + table.getName() + ":");
			Path srcf = srcd.resolve(table.getName());
			Path dstf = dstd.resolve(table.getName());
			Files.copy(srcf, dstf); // TODO
		}
		Path movd = Paths.get(p.toString(), "kdbbackup_" + stamp);
		Files.move(tmpd, movd);
		
	}
	
	public AbstractTable getTable(String tablename)
	{
		return tableMap.get(tablename);
	}
	
	Map<String, AbstractTable> getTableMap()
	{
		return tableMap;
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	@Override
	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	private Config config;
	private ScheduledExecutorService executor;
	private CheckPointTask cpt = new CheckPointTask();
	private BackupTask bkt = new BackupTask();
	private Log log;
	private AbstractTable[] tables;
	private Map<String, AbstractTable> tableMap = new HashMap<String, AbstractTable>(); // TODO concurrent read
	private Statistics stat = new Statistics();
	private Logger logger = Logger.getRootLogger();

}

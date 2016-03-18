
package ket.kdb;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class Log
{
	public Log(String fileName, Map<String, AbstractTable> tableMap)
	{
		dbFile = DBFile.newInstance(new File(fileName));
		this.tableMap = tableMap;
	}	
	
	private static class LogData
	{
		public LogData()
		{		
		}
		
		public LogData(PageView pvDirty)
		{
			pvTable = pvDirty;
			pageOffset = 0;
		}
		
		public PageView pvTable;
		public int pageOffset;
	}
	
	private static class TableData
	{
		public static final int MAX_NAME_BYTES = 60;
		private static final String NAME_CHARSET = "utf-8";
				
		public TableData()
		{			
		}
		
		public TableData(final String name, final List<PageViewWithReader> pvsDirty)
		{
			this.name = name;
			try
			{
				bytesName = name.getBytes(NAME_CHARSET);
				if( bytesName.length > MAX_NAME_BYTES )
					throw new Error("DBErr: table name " + name + " is invalid ");
			}
			catch(UnsupportedEncodingException ex)
			{
				throw new Error("DBErr: table name " + name + " is invalid ", ex);
			}
			logs = new ArrayList<LogData>();
			for(PageView pvDirty : pvsDirty)
			{
				logs.add(new LogData(pvDirty));
				dirtyPageCount += pvDirty.range.size();
			}
		}
		
		public void saveName(ByteBuffer bb)
		{
			bb.putInt(bytesName.length);
			bb.put(Arrays.copyOf(bytesName, MAX_NAME_BYTES));
		}
		
		public void loadName(ByteBuffer bb)
		{
			int bytesNameLen = bb.getInt();
			byte[] bytes = new byte[MAX_NAME_BYTES];
			bb.get(bytes);
			bytesName = Arrays.copyOfRange(bytes, 0, bytesNameLen);
			try
			{
				name = new String(bytesName, NAME_CHARSET);
			}
			catch(UnsupportedEncodingException ex)
			{
				throw new Error("DBErr: table name " + name + " is invalid ", ex);
			}
		}
		
		public String name;
		public byte[] bytesName;
		public List<LogData> logs;
		public int dirtyPageCount;
	}
	
	private static class CheckPointData
	{
		public void construct(final Map<AbstractTable, List<PageViewWithReader>> pvsDirtyMap)
		{
			tables = new ArrayList<TableData>();
			int headerSize = 4 + 4; // headerPageCount & tableCount
			for(Map.Entry<AbstractTable, List<PageViewWithReader>> e : pvsDirtyMap.entrySet())
			{
				TableData tableData = new TableData(e.getKey().getName(), e.getValue());
				headerSize += 4 + TableData.MAX_NAME_BYTES; // bytesName.length & bytesName
				headerSize += 4;  // logDataCount
				headerSize += tableData.logs.size() * 12; // table from, log from, page count
				tables.add(tableData);
				dirtyPageCount += tableData.dirtyPageCount;
			}
			headerPageCount = Page.getCount(headerSize); // start page index of log data
			int pageIndexStart = headerPageCount;
			for(TableData tableData : tables)
			{
				for(LogData logData : tableData.logs)
				{
					logData.pageOffset = pageIndexStart;
					pageIndexStart += logData.pvTable.range.size();
				}
			}
		}
		
		public boolean readLog(DBFile dbFile)
		{
			ByteBuffer bbsig = ByteBuffer.wrap(new byte[4]);
			if( ! dbFile.read(0, bbsig) )
				return false;
			headerPageCount = bbsig.getInt();
			if( headerPageCount == 0 )
				return false;
			ByteBuffer pbHeader = ByteBuffer.allocate(headerPageCount * Page.SIZE);
			dbFile.read(0, pbHeader.array(), pbHeader.arrayOffset(), pbHeader.capacity());
			headerPageCount = pbHeader.getInt();
			int tableCount = pbHeader.getInt();
			tables = new ArrayList<TableData>();
			for(int i = 0; i < tableCount; ++i)
			{
				TableData tableData = new TableData();
				tableData.loadName(pbHeader);
				tables.add(tableData);
			}
			for(TableData tableData : tables)
			{
				int logCount = pbHeader.getInt();
				tableData.logs = new ArrayList<LogData>();
				for(int i = 0; i < logCount; ++i)
				{
					LogData logData = new LogData();
					//
					int pvLogFrom = pbHeader.getInt();
					int pvTableFrom = pbHeader.getInt();
					int pvLogSize = pbHeader.getInt();
					Range tableRange = new Range(pvTableFrom, pvTableFrom + pvLogSize);
					logData.pageOffset = pvLogFrom;					
					logData.pvTable = new PageBuffer(tableRange);
					tableData.logs.add(logData);
				}
			}
			for(TableData tableData : tables)
			{
				for(LogData logData : tableData.logs)
					logData.pvTable.load(dbFile, logData.pageOffset * (long)Page.SIZE);
			}
			return true;
		}
		
		public void writeLog(DBFile dbFile)
		{
			ByteBuffer pbHeader = ByteBuffer.allocate(headerPageCount * Page.SIZE);
			pbHeader.putInt(0); // disabled log
			pbHeader.putInt(tables.size());
			for(TableData tableData : tables)
				tableData.saveName(pbHeader);
			for(TableData tableData : tables)
			{
				pbHeader.putInt(tableData.logs.size());
				for(LogData logData : tableData.logs)
				{
					pbHeader.putInt(logData.pageOffset);
					pbHeader.putInt(logData.pvTable.range().from);
					pbHeader.putInt(logData.pvTable.range().size());
				}
			}
			dbFile.write(0, pbHeader.array(), pbHeader.arrayOffset(), pbHeader.capacity());
			for(TableData tableData : tables)
			{
				for(LogData logData : tableData.logs)					
					logData.pvTable.save(dbFile, logData.pageOffset * (long)Page.SIZE);
			}
		}
		
		public int headerPageCount;
		public List<TableData> tables;
		public int dirtyPageCount;
	}
	
	private void enableLog(int signature)
	{
		ByteBuffer bb = ByteBuffer.wrap(new byte[4]);
		bb.putInt(signature);
		bb.rewind();
		dbFile.write(0, bb);
		dbFile.sync();
	}
	
	private void disableLog()
	{
		ByteBuffer bb = ByteBuffer.wrap(new byte[4]);
		bb.putInt(0);
		bb.rewind();
		dbFile.write(0, bb);
		dbFile.sync();
	}
	
	private void clearLog()
	{
		dbFile.clear();
	}
	
	public void checkPoint(Map<AbstractTable, List<PageViewWithReader>> pvsDirtyMap, int pvCount)
	{			
		// 1. construct log
		CheckPointData cpd = new CheckPointData();
		cpd.construct(pvsDirtyMap);
		System.out.print("    " + cpd.dirtyPageCount + "(" + pvCount + ") dirty pages(buffers)");
		// 2. save log, set log
		long now = System.currentTimeMillis();
		disableLog();		
		cpd.writeLog(dbFile);
		dbFile.sync();
		enableLog(cpd.headerPageCount);
		System.out.print(", writelog " + (System.currentTimeMillis() - now) + "(ms)");
		// 3. save tables
		for(Map.Entry<AbstractTable, List<PageViewWithReader>> e : pvsDirtyMap.entrySet())
		{
			e.getKey().getBufferManager().saveDirtyPagesWithReader(e.getValue());
		}
		
		// 4. clear log
		pvsDirtyMap.clear();
		clearLog();
	}
	
	public void recover()
	{
		CheckPointData cpd = new CheckPointData();
		if( cpd.readLog(dbFile) )
		{
			System.out.println("start recovering");
			for(TableData tableData : cpd.tables)
			{
				List<PageView> pvsDirty = new ArrayList<PageView>();
				for(LogData logData : tableData.logs)
					pvsDirty.add(logData.pvTable);
				AbstractTable table = tableMap.get(tableData.name);
				if( table == null )
					System.err.println("DBWarning: table " + tableData.name + " is missing on db recover");
				table.getBufferManager().saveDirtyPages(pvsDirty);
			}
			clearLog();
			System.out.println("recover ok");
		}
		for(AbstractTable table : tableMap.values())
			table.load();
	}
	
	public void clear()
	{
		clearLog();
		for(AbstractTable table : tableMap.values())
			table.clear();
	}
	
	public void close()
	{
		dbFile.close();
		for(AbstractTable table : tableMap.values())
			table.close();		
	}
	
	private DBFile dbFile;
	private final Map<String, AbstractTable> tableMap;
}

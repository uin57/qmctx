package com.joypiegame.gameservice.db;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;




import javax.servlet.ServletContext;

import com.joypiegame.gameservice.util.FileWatchdog;
import com.joypiegame.gameservice.util.XmlParser;
import com.joypiegame.gameservice.util.TimerThread;
import com.joypiegame.gameservice.web.GSWebApp;

public class DBServers 
{
	static DBServers instance;
	static final String dbConfName = "DBConf";
	static final String gameInfoDBname = "GameInfo";
	Map<String, DBExecutor> dbExecutors = new TreeMap<String, DBExecutor>();
	FileMonitor dbConfFileMonitor = new FileMonitor();
	
	public static DBServers getInstance()
	{
		return instance;
	}
	
    public DBServers() {
    	instance = this;
    }
    
    public void init(GSWebApp gsapp) {
    	setConfFilePath(gsapp.getTimerThread(), gsapp.getGsConf().getDatabaseConfRealPath());
    }
    
    public void fini() {
    	
    }
    
    public static DBExecutor getGameInfoDBExecutor() {
    	DBExecutor dbExecutor = null;
    	DBServers dbServers = getInstance();
    	if (dbServers != null) {
    		dbExecutor = dbServers.getDBExecutor(gameInfoDBname);
    	}
    	return dbExecutor;
    }
    
    public void setConfFilePath(TimerThread timerThread, String filePath) {
    	dbConfFileMonitor.setFile(filePath);
    	timerThread.startTimeTask(dbConfName, dbConfFileMonitor, 10, 10, TimeUnit.SECONDS);
    }
    
    class FileMonitor implements Runnable {
		String filePath = "";
		FileWatchdog fileWathdog = new FileWatchdog();
		XmlParser xmlParser = new XmlParser();
		
		FileMonitor() {
			initXmlParser();
		}
		
		void setFile(String filepath) {
			this.filePath = filepath;
			fileWathdog.setFile(filepath, new Runnable()
					{
						public void run() {
							handleFileChanged();
						}
					});
		}
		
		private void initXmlParser()
		{
			xmlParser.getVerifier().setRootElementName("mysql");
			xmlParser.getVerifier().setStructElementName("database");
			xmlParser.getVerifier().addStructAttributeName(gameInfoDBname);
		}
		
		void update() {
			if (filePath != null) {
				File file = new File(filePath);
				 try {
					 if (file.exists()) {
						 XmlParser.XMLItem item = xmlParser.parse(filePath);
						 if (item != null) {
							 for (XmlParser.XMLItem citem : item.getChildernItems()) {
								 if (citem.getName().equals("database")) {
									 String aname = citem.getAttribute("name");
									 if (!aname.isEmpty()) {
										 String ahost = citem.getAttribute("host");
										 String aport = citem.getAttribute("port");
										 String ausername = citem.getAttribute("username");
										 String apassword = citem.getAttribute("password");
										 String adbname = citem.getAttribute("dbname");
										 if (!ahost.isEmpty() && !aport.isEmpty() && !ausername.isEmpty() && !adbname.isEmpty()) {
											 AddDBExecutor(aname, ahost, aport, ausername, apassword, adbname);
										 }
									 }
								 }
							 }
						 }
					 }
				} 
				catch(SecurityException  e) {
					e.printStackTrace();
				}
			}
		}
		
		void handleFileChanged() {
			System.out.println(GSWebApp.getTimeString()+ " : file("+filePath+") changed, needupdate!");
			update();
		}
		
		public void run() {
			fileWathdog.run();
		}
	}
    
    public DBExecutor getDBExecutor(String name)
    {
    	return dbExecutors.get(name);
    }
    
    public void AddDBExecutor(String confName, String host, String port, String username, String password, String dbname)
    {
    	DBExecutor dbExecutor = new DBExecutor();
    	dbExecutor.setDbUrl(host, port);
    	dbExecutor.setDbUser(username, password);
    	dbExecutor.setDbName(dbname);
    	dbExecutors.put(confName, dbExecutor);
    }
}





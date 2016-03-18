package com.joypiegame.gameservice.web.serverinfo;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;




import javax.servlet.ServletContext;

import com.joypiegame.gameservice.util.FileWatchdog;
import com.joypiegame.gameservice.util.XmlParser;
import com.joypiegame.gameservice.util.TimerThread;
import com.joypiegame.gameservice.web.GSWebApp;

public class GameVersion 
{
	static GameVersion instance;
	static final String gameVersionConfName = "GameVersionConf";
	static final String jumpTablename = "JumpEntry";
	Map<String, String> gameVersionJumpTable = new TreeMap<String, String>();
	FileMonitor gameVersionConfFileMonitor = new FileMonitor();
	
	public static GameVersion getInstance()
	{
		return instance;
	}
	
    public GameVersion() {
    	instance = this;
    }
    
    public void init(GSWebApp gsapp) {
    	setConfFilePath(gsapp.getTimerThread(), gsapp.getGsConf().getVersionJumpConfRealPath());
    }
    
    public void fini() {
    	
    }
    
    public void setConfFilePath(TimerThread timerThread, String filePath) {
    	gameVersionConfFileMonitor.setFile(filePath);
    	timerThread.startTimeTask(gameVersionConfName, gameVersionConfFileMonitor, 10, 10, TimeUnit.SECONDS);
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
			xmlParser.getVerifier().setRootElementName("game");
			xmlParser.getVerifier().setStructElementName("version");
			xmlParser.getVerifier().addStructAttributeName(jumpTablename);
		}
		
		void update() {
			if (filePath != null) {
				File file = new File(filePath);
				Map<String, String> versionJumpTbl = new TreeMap<String, String>();
				 try {
					 if (file.exists()) {
						 XmlParser.XMLItem item = xmlParser.parse(filePath);
						 if (item != null) {
							 for (XmlParser.XMLItem citem : item.getChildernItems()) {
								 if (citem.getName().equals("version")) {
									 String aname = citem.getAttribute("name");
									 if (!aname.isEmpty()) {
										 String requestVersion = citem.getAttribute("request");
										 String jumptoVersion = citem.getAttribute("jumpto");
										 versionJumpTbl.put(requestVersion, jumptoVersion);
										 System.out.println(GSWebApp.getTimeString()+ " : version request jump table entry [" + requestVersion + " --> " + jumptoVersion + "]");
									 }
								 }
							 }
						 }
					 }
				} 
				catch(SecurityException  e) {
					versionJumpTbl = null;
					e.printStackTrace();
					System.out.println(GSWebApp.getTimeString()+ " : file("+filePath+") update failed!");
				}
			  if (versionJumpTbl != null) {
				  gameVersionJumpTable = versionJumpTbl;
				  System.out.println(GSWebApp.getTimeString()+ " : file("+filePath+") update success!");
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
    
    public static String getGameJumpToVersion(String version)
    {
    	GameVersion gameversion = getInstance();
    	String v = gameversion.gameVersionJumpTable.get(version);
    	if (v == null)
    	{
    		v = version;
    	}
    	return v;
    }
   
}





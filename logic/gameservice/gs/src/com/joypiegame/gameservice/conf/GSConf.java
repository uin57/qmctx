package com.joypiegame.gameservice.conf;

import javax.servlet.ServletContext;

import java.io.File;

public class GSConf {
	private String writeOnlineData;
	private static boolean writeOnlineDataConf;
    
    private String gsConfDir;
    private String absoluteConfDir;
    
    private String databaseConfFile;
    private String versionjumpConfFile;
    
    private String urlRequestName;
    private String urlChannelPlatName;
    private String urlVersionName;
    
    private String channelPlatsName;
    
    private String gameServerConfFile;
    private String gameServerRequestName;
    private String versionConfFile;
    private String versionRequestName;
    private String announcementConfFile;
    private String announcementRequestName;
    private String updateConfFile;
    private String updateRequestName;
    
    //private String databaseConfRealPath;
//    private String gameServerConfRealPath;
//    private String versionConfRealPath;
//    private String announcementConfRealPath;
    public GSConf() {
    }

    public void initialize(ServletContext context){
        initializeConf(context);
    }

    private void initializeConf(ServletContext context) {
    	writeOnlineData = context.getInitParameter("writeOnlineData");
    	writeOnlineDataConf = writeOnlineData.toLowerCase().equals("false") ? false : true;
        
        gsConfDir = context.getInitParameter("gameServiceConfDir");
        absoluteConfDir = context.getInitParameter("absoluteConfDir");
        
        databaseConfFile = context.getInitParameter("databaseConf");
        if (databaseConfFile == null)
        	databaseConfFile = "database.xml";
        versionjumpConfFile = context.getInitParameter("versionjumpConf");
        if (versionjumpConfFile == null)
        	versionjumpConfFile = "versionjump.xml";
        
        urlRequestName = context.getInitParameter("urlRequestName");
        urlChannelPlatName = context.getInitParameter("urlChannelPlatName");
        urlVersionName = context.getInitParameter("urlVersionName");
        
        channelPlatsName = context.getInitParameter("channelPlats");
        
        gameServerConfFile = context.getInitParameter("gameServerConf");
        gameServerRequestName = context.getInitParameter("gameServerRequestName");
        versionConfFile = context.getInitParameter("versionConf");
        versionRequestName = context.getInitParameter("versionRequestName");
        announcementConfFile = context.getInitParameter("announcementConf");
        announcementRequestName = context.getInitParameter("announcementRequestName");
        updateConfFile = context.getInitParameter("updateConf");
        updateRequestName = context.getInitParameter("updateRequestName");
        
        
        if (!absoluteConfDir.toLowerCase().equals("true"))
        {
        	String catalinaHomePath = System.getProperty("catalina.home");
        	if (catalinaHomePath != null)
        	{
        		gsConfDir = catalinaHomePath + File.separator + gsConfDir;
        	}
        }
        System.out.println("gsconf path : ("+gsConfDir+")");
    }

    public static boolean getWriteOnlineDataConf() {
    	return writeOnlineDataConf;
    }
    
    public String getDatabaseConfRealPath() {
    	return getConfRealPath(databaseConfFile);
    }
    
    public String getVersionJumpConfRealPath() {
    	return getConfRealPath(versionjumpConfFile);
    }
    
    public String getUrlRequestName() {
    	return urlRequestName;
    }
    
    public String getUrlChannelPlatName() {
    	return urlChannelPlatName;
    }
    
    public String getUrlVersionName() {
    	return urlVersionName;
    }
    
    public String[] getChannelPlatsName() {
    	return channelPlatsName.trim().split(",");
    }
    
    public String getGameServerRequestName() {
    	return gameServerRequestName;
    }
    
    public String getVersionRequestName() {
    	return versionRequestName;
    }
    
    public String getAnnouncementRequestName() {
    	return announcementRequestName;
    }
    
    public String getUpdateRequestName() {
    	return updateRequestName;
    }
    
    public String getGameServerConfRealPath(String cp, String version) 
    {
    	return getCPVersionConfRealPath(cp, version, gameServerConfFile);
    }
    
    public String getVersionConfRealPath(String cp, String version) 
    {
    	return getCPVersionConfRealPath(cp, version, versionConfFile);
    }
    
    public String getAnnouncementConfRealPath(String cp, String version) 
    {
    	return getCPVersionConfRealPath(cp, version, announcementConfFile);
    }
    
    public String getUpdateConfRealPath(String cp, String version) 
    {
    	return getCPVersionConfRealPath(cp, version, updateConfFile);
    }
    
    public String getCPVersionConfRealPath(String cp, String version, String fileName)
    {
    	return getConfRealPath(getCPVersionPath(cp, version, fileName));
    }
    
    String getConfRealPath(String fileName)
    {
    	return gsConfDir + File.separator + fileName;
    }
    
    static String getCPVersionPath(String cp, String version, String fileName)
    {
    	String filePath = fileName;
    	if (!version.isEmpty())
    	{
    		StringBuilder sb = new StringBuilder(fileName);
    		int index = sb.lastIndexOf(".");
    		if (index >= 0)
    		{
    			sb.insert(index, "." + version);
    			filePath = sb.toString();
    		}	
    	}
    	if (!cp.isEmpty())
    		filePath = cp + File.separator + filePath;
    	return filePath;
    }
}




package com.joypiegame.gameservice.web;

import javax.servlet.ServletContext;

import java.text.SimpleDateFormat;

import com.joypiegame.gameservice.conf.GSConf;
import com.joypiegame.gameservice.db.DBServers;
import com.joypiegame.gameservice.web.serverinfo.GameVersion;
import com.joypiegame.gameservice.util.TimerThread;



public class GSWebApp  {
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	GSConf conf = new GSConf();
	TimerThread timerThread = new TimerThread();
	DBServers dbServers = new DBServers();
	GameVersion gameVersion = new GameVersion();
	
	public static void instance(ServletContext context) {
		GSWebApp gswa = new GSWebApp();
		gswa.init(context);
		context.setAttribute(GSWebApp.class.getName(), gswa);
	}
	
	public static void destory(ServletContext context) {
		GSWebApp gswa = getInstance(context);
		if (gswa != null)
			gswa.fini();
		context.removeAttribute(GSWebApp.class.getName());
	}
	
	public static GSWebApp getInstance(ServletContext context) {
		Object obj = context.getAttribute(GSWebApp.class.getName());
		return (GSWebApp)obj;
	}
	
	public static String getTimeString()
	{
		return dateformat.format(new java.util.Date());
	}
	
	public GSWebApp() {
		
	}
	
	public void init(ServletContext context) {
		conf.initialize(context);
		timerThread.start();
		dbServers.init(this);
		gameVersion.init(this);
		System.out.println("GSWebApp init!");
	}
	
	public void fini() {
		gameVersion.fini();
		dbServers.fini();
		timerThread.shutDown();
		System.out.println("GSWebApp fini!");
	}

	public GSConf getGsConf()
	{
		return conf;
	}
	
	public TimerThread getTimerThread() {
		return timerThread;
	}
	
	public DBServers getDBServers() {
		return dbServers;
	}
}



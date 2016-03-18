package com.joypiegame.gameservice.web.serverinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import com.joypiegame.gameservice.web.GSWebApp;
import com.joypiegame.gameservice.conf.GSConf;
import com.joypiegame.gameservice.util.FileWatchdog;




public class ServerInfoServlet extends HttpServlet {

	//private GSConf gsConf;
	private RequestConfPathGetter requestConfPathGetter;
//    private String urlRequestName;
//    private String urlChannelPlatName;
//    private String urlVersionName;
//    private Map<String, ConfPathGetter> requestHandlers = new TreeMap<String, ConfPathGetter>();
    private Map<String, FileCache> requestFileCaches = new TreeMap<String, FileCache>();
    private Task task = new Task();
	
	
	class FileCache implements Runnable {
		String filePath = "";
		FileWatchdog fileWathdog = new FileWatchdog();
		boolean needUpdate = true;
		List<String> lines = new ArrayList<String>();
		
		FileCache(String filepath) {
			this.filePath = filepath;
			fileWathdog.setFile(filepath, 
					new Runnable()
					{
						public void run() {
							setFileChanged();
						}
					},
					new Runnable()
					{
						public void run() {
							setFileNotExist();
						}
					});
		}
		
		void update() {
			if (needUpdate) {
				if (filePath != null) {
					File file = new File(filePath);
					 try {
						if (file.exists()) {
							try {
								BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));
								lines.clear();
								for (String line = in.readLine(); line != null; line = in.readLine()) {
									lines.add(line);
									//System.out.println(line);
								}
								in.close();
								System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet read content from file("+filePath+") !");
							} catch (Exception e) {
								e.printStackTrace();
							}
							needUpdate = false;
						}
					} 
					catch(SecurityException  e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		Iterator<String> getFileContent() {
			update();
			return lines.iterator();
		}
		
		void setFileChanged() {
			needUpdate = true;
			System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet file("+filePath+") changed, needupdate!");
		}
		
		void setFileNotExist() {
			task.addTask(filePath);
			System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet file("+filePath+") not exist, cancel monitor!");
		}
		
		public void run() {
			fileWathdog.run();
		}
	}
	
	class Task implements Runnable
	{
		private static final String taskName = "ServerInfoServletTask"; 
		private List<String> cancelMonitorFiles = new ArrayList<String>();
		public Task()
		{
			
		}
		
		public void init()
		{
			ServletContext context = getServletContext();
			GSWebApp gsapp = GSWebApp.getInstance(context);
			gsapp.getTimerThread().startTimeTask(taskName, this, 10, 10, TimeUnit.SECONDS);
		}
		
		public void fini()
		{
			ServletContext context = getServletContext();
			GSWebApp gsapp = GSWebApp.getInstance(context);
			gsapp.getTimerThread().cancelTimerTask(taskName);
		}
		
		public synchronized void run()
		{
			for (String cmf : cancelMonitorFiles)
			{
				tryCancelMonitorFile(cmf);
			}
			cancelMonitorFiles.clear();
		}
		
		public synchronized void addTask(String cancelFile)
		{
			cancelMonitorFiles.add(cancelFile);
		}
	}
	
//	public interface ConfPathGetter
//	{
//		String getConfRealPath(String cp, String version);
//	}
	interface ConfPathGenerator
	{
		String getConfRealPath(String cp, String version);
	}
	public class RequestConfPathGetter
	{
	    private String urlRequestName;
	    private String urlChannelPlatName;
	    private String urlVersionName;
		private Map<String, ConfPathGenerator> requestHandlers = new TreeMap<String, ConfPathGenerator>();
		private Set<String> channelPlats = new TreeSet<String>();
		RequestConfPathGetter(final GSWebApp gsapp)
		{
			urlRequestName = gsapp.getGsConf().getUrlRequestName();
			urlChannelPlatName = gsapp.getGsConf().getUrlChannelPlatName();
			urlVersionName = gsapp.getGsConf().getUrlVersionName();
			requestHandlers.put(gsapp.getGsConf().getGameServerRequestName(), new ConfPathGenerator()
					{
						public String getConfRealPath(String cp, String version)
						{
							return gsapp.getGsConf().getGameServerConfRealPath(cp, version);
						}
					});
			requestHandlers.put(gsapp.getGsConf().getVersionRequestName(), new ConfPathGenerator()
					{
						public String getConfRealPath(String cp, String version)
						{
							return gsapp.getGsConf().getVersionConfRealPath(cp, version);
						}
					});
			requestHandlers.put(gsapp.getGsConf().getAnnouncementRequestName(), new ConfPathGenerator()
					{
						public String getConfRealPath(String cp, String version)
						{
							return gsapp.getGsConf().getAnnouncementConfRealPath(cp, version);
						}
					});
			requestHandlers.put(gsapp.getGsConf().getUpdateRequestName(), new ConfPathGenerator()
					{
						public String getConfRealPath(String cp, String version)
						{
							return gsapp.getGsConf().getUpdateConfRealPath(cp, version);
						}
					});
			String[] channelPlatsName = gsapp.getGsConf().getChannelPlatsName();
			for (String cp : channelPlatsName)
			{
				channelPlats.add(cp);
			}
//			channelPlats.add("aqq");
//			channelPlats.add("iqq");
//			channelPlats.add("aweixin");
//			channelPlats.add("iweixin");
//			channelPlats.add("iguest");
		}
		
		String getConfRealPath(String request, String cp, String version)
		{
			String filePath = null;
			version = GameVersion.getGameJumpToVersion(version);
			ConfPathGenerator generator = requestHandlers.get(request);
			if (generator != null && (channelPlats.contains(cp) || cp.isEmpty()))
			{
				filePath = generator.getConfRealPath(cp, version);
			}
			return filePath;
		}
		
		String getConfRealPath(String request)
		{
			String[] requestStrs = request.split("&");
			String requestValue = "";
			String channelPlatValue = "";
			String versionValue = "";
			for (String requestStr : requestStrs)
			{
				String paramNameStr = requestStr;
				String paramValueStr = "";
				int index = requestStr.indexOf("=");
				if (index >= 0)
				{
					paramNameStr = requestStr.substring(0, index);
					if (index + 1 < requestStr.length())
						paramValueStr = requestStr.substring(index+1);
				}
				if (paramNameStr.equals(urlRequestName))
				{
					requestValue = paramValueStr;
				}
				else if (paramNameStr.equals(urlChannelPlatName))
				{
					channelPlatValue = paramValueStr;
				}
				else if (paramNameStr.equals(urlVersionName))
				{
					versionValue = paramValueStr;
				}
			}
			return getConfRealPath(requestValue.toLowerCase(), channelPlatValue.toLowerCase(), versionValue.toLowerCase());
		}
		
	}
	public void init() throws ServletException
	{
		ServletContext context = getServletContext();
		GSWebApp gsapp = GSWebApp.getInstance(context);
		requestConfPathGetter = new RequestConfPathGetter(gsapp);
		task.init();
		System.out.println("ServerInfoServlet init");
	}
	
	public void destory()
	{
		task.fini();
		tryCancelAllMonitorFiles();
		System.out.println("ServerInfoServlet destory");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String queryStr = request.getQueryString();
		String requestFilePath = requestConfPathGetter.getConfRealPath(queryStr);
		System.out.println(GSWebApp.getTimeString()+" : ServerInfoServlet queryStr:"+queryStr+" requestfile:"+requestFilePath);
		if (requestFilePath != null)
		{
			FileCache fc = tryGetMonitorFile(requestFilePath);
			if (fc != null)
			{
				Iterator<String> iterator = fc.getFileContent();
				PrintWriter out = response.getWriter();
				while (iterator.hasNext()) 
				{
					out.println(iterator.next());
				}
				out.close();
				System.out.println(GSWebApp.getTimeString()+" : ServerInfoServlet response: requestfile("+requestFilePath+") find filecache");
			}
			else
			{
				System.out.println(GSWebApp.getTimeString()+" : ServerInfoServlet response: requestfile("+requestFilePath+") can't find");
			}
		}
	}
	
	private synchronized FileCache tryGetMonitorFile(String filePath)
	{
		FileCache fc = requestFileCaches.get(filePath);
		if (fc == null)
		{
			fc = monitorFile(filePath);
			if (fc != null)
				requestFileCaches.put(filePath, fc);
		}
		return fc;
	}
	
	private synchronized void tryCancelMonitorFile(String filePath)
	{
		cancelMonitorFile(filePath);
		requestFileCaches.remove(filePath);
	}
	
	private synchronized void tryCancelAllMonitorFiles()
	{
		for (Map.Entry<String, FileCache> e : requestFileCaches.entrySet())
		{
			cancelMonitorFile(e.getKey());
		}
		requestFileCaches.clear();
	}
	
	private FileCache monitorFile(String filePath) {
		FileCache fc = null;
		File file = new File(filePath);
		try 
		{
			if (file.exists())
			{
				System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet monitor file("+filePath+") start");
				fc = new FileCache(filePath);
				ServletContext context = getServletContext();
				GSWebApp gsapp = GSWebApp.getInstance(context);
				gsapp.getTimerThread().startTimeTask(filePath, fc, 10, 10, TimeUnit.SECONDS);
				
			}
			else
				System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet monitor file("+filePath+") is not exist!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fc;
	}
	
	private void cancelMonitorFile(String filePath) {
		ServletContext context = getServletContext();
		GSWebApp gsapp = GSWebApp.getInstance(context);
		gsapp.getTimerThread().cancelTimerTask(filePath);
		System.out.println(GSWebApp.getTimeString()+ " : ServerInfoServlet monitor file("+filePath+") cancel");
	}

}




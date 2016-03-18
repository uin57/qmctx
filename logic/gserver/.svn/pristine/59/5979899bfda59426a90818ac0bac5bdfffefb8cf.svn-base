package i3k.gs;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import com.joypiegame.gameservice.webservice.client.GameWebService;
import com.joypiegame.gameservice.webservice.service.GWService;
//import com.joypiegame.gameservice.webservice.service.cdkey.CDKeyGen;;




public class GameService 
{
	public static final int INVOKE_SUCCESS = 0;
	static final int INVOKE_TIMEOUT = -1;
	static final int INVOKE_EXCEPTION = -2;
	static final int INVOKE_WEBSERVICE_CALL_FAILED = -100;
	static final int INVOKE_PARSE_ERROR = -101;
	static final long MAX_WAIT_TIME = 15000;
	ExecutorService executor = Executors.newSingleThreadExecutor();
	GameWebService gws = new GameWebService();
	String host;
	int port;
	GWService gwservice;
	AtomicInteger invokeId = new AtomicInteger();
	Map<Integer, ServiceInvoker> invokers = new HashMap<Integer, ServiceInvoker>();
	GameServer gs;
	
	public interface GameWebServiceTaskHandler
	{
		public int doTask(GWService gws);
		public void onCallback(int result);
	}
	
	public static class ExchangeItemCallback
	{
		public void onExchangeItemCallback(int result, List<i3k.SBean.DropEntry> gift)
		{
			
		}
	}
	
	public static class ReportOnlineInfoCallback
	{
		public void onReportOnlineInfoCallback(int result)
		{
			
		}
	}
	
	public static class ReportRegisterInfoCallback
	{
		public void onReportRegisterInfoCallback(int result)
		{
			
		}
	}
	
	class ServiceInvoker implements Runnable
	{
		int id;
		long callTime;
		GameWebServiceTaskHandler handler;
		String callLogStr;
		public ServiceInvoker(GameWebServiceTaskHandler handler)
		{
			this.id = invokeId.addAndGet(1);
			this.callTime = System.currentTimeMillis();
			this.handler = handler;
		}
		
		public int getId()
		{
			return id;
		}
		
		public boolean isTooOld(long now)
		{
			return callTime + MAX_WAIT_TIME < now;
		}
		
		public void run()
		{
			if (gwservice == null)
				initServiceImpl();
			int result = handler.doTask(gwservice);
			if (result == INVOKE_EXCEPTION)
				gwservice = null;
			onCallback(result);
		}
		
		private void onCallback(int result)
		{
			gs.getLogger().debug(callLogStr+", result=" + result + "==>call return invokeId=" + invokeId);
			ServiceInvoker si = GameService.this.removeAndGetTask(id);
			if (si != null)
			{
				si.handler.onCallback(result);
			}
			else
			{
				gs.getLogger().warn(callLogStr+", result=" + result + "==>but timeout invokeId=" + invokeId);
			}
		}
		
		void setCallLogStr(String logStr)
		{
			callLogStr = logStr;
		}
	}
	
	class ReportOnlineInfoInvoker extends ServiceInvoker
	{
		public ReportOnlineInfoInvoker(final int time, final int userCount, final boolean isGuest, final ReportOnlineInfoCallback callback)
		{
			super(new GameWebServiceTaskHandler()
					{
						public int doTask(GWService gws)
						{
							return reportOnlineInfoImpl(gwservice, time, gs.getConfig().areaID, gs.getConfig().platID, gs.getConfig().id, gs.getGameAppID(isGuest), userCount);
						}
						public void onCallback(int result)
						{
							callback.onReportOnlineInfoCallback(result);
						}
					});
			setCallLogStr("reportOnlineInfo: time=" + time + ", areaId=" + gs.getConfig().areaID + ", platId=" + gs.getConfig().platID + ", gsid=" + gs.getConfig().id + ", gameAppId=" + gs.getGameAppID(isGuest) + ", userCount=" + userCount);
		}
	}
	
	class ReportRegisterInfoInvoker extends ServiceInvoker
	{
		public ReportRegisterInfoInvoker(final int time, final int registerCount, final ReportRegisterInfoCallback callback)
		{
			super(new GameWebServiceTaskHandler()
					{
						public int doTask(GWService gws)
						{
							return reportRegisterInfoImpl(gwservice, time, gs.getConfig().areaID, gs.getConfig().platID, gs.getConfig().id, registerCount);
						}
						public void onCallback(int result)
						{
							callback.onReportRegisterInfoCallback(result);
						}
					});
			setCallLogStr("reportRegisterInfo: time=" + time + ", areaId=" + gs.getConfig().areaID + ", platId=" + gs.getConfig().platID + ", gsid=" + gs.getConfig().id + ", registerCount=" + registerCount);
		}
	}
	
	class ExchangeItemInvoker extends ServiceInvoker
	{
		public ExchangeItemInvoker(final int roleid, final String rolename, final String cdkey, final String channel, final ExchangeItemCallback callback)
		{
			super(new GameWebServiceTaskHandler()
					{
						List<i3k.SBean.DropEntry> gift = null;
						public int doTask(GWService gws)
						{
							int error = INVOKE_EXCEPTION;
							String result = exchangeItemImpl(gwservice, gs.getConfig().id, roleid, rolename, cdkey, channel);
							String[] strs = result.split("\\|");
							if (strs.length >= 1)
							{
								try
								{
									error = Integer.parseInt(strs[0]);
									if (error == 0)
									{
										if (strs.length == 2)
										{
											String[] giftstrs = strs[1].split(";");
											gift = new ArrayList<i3k.SBean.DropEntry>();
											for (String giftstr : giftstrs)
											{
												String[] gstrs = giftstr.split(",");
												if (gstrs.length != 3)
												{
													error = INVOKE_PARSE_ERROR;
													gift = null;
													break;
												}
												int type = Integer.parseInt(gstrs[0]);
												int id = Integer.parseInt(gstrs[1]);
												int count = Integer.parseInt(gstrs[2]);
												i3k.SBean.DropEntry den = new i3k.SBean.DropEntry((byte)type, (byte)id, (short)count);
												gift.add(den);
											}
										}
										else
										{
											error = INVOKE_PARSE_ERROR;
										}
									}
								}
								catch (Exception e)
								{
									
								}
							}
							return error;
						}
						public void onCallback(int result)
						{
							callback.onExchangeItemCallback(result, gift);
						}
					});
			setCallLogStr("exchangeCDKey: gsid=" + gs.getConfig().id + ", roleid=" + roleid + ", rolename=" + rolename + ", cdkey=" + cdkey + ", channel=" + channel);
		}
	}
	
	public GameService(GameServer gs)
	{
		this.gs = gs;
	}
	

	public void init(String host, int port)
	{
		this.host = host;
		this.port = port;
		gs.getLogger().info("init parse Web Service host=" + host + ", port=" + port);
		initServiceImpl();
	}
	
	public void shutDown()
	{
		try
		{
			executor.shutdown();
			if(!executor.awaitTermination(3, TimeUnit.SECONDS))
			{
				executor.shutdownNow();
			}
		}
		catch(Exception ex)
		{			
		}
	}
	
//	private void reInitService()
//	{
//		gs.getLogger().info("reInit parse webService host=" + host + ", port=" + port);
//		initServiceImpl();
//	}
	
	private void initServiceImpl()
	{
    	try 
    	{
    		gwservice = gws.getGameService(host, port);
    	} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
    		gs.getLogger().warn(e.getMessage());
    	}
	}
	
	public void onTimer()
	{
		List<ServiceInvoker> timeoutInvokers = removeAndGetTimeoutTasks(System.currentTimeMillis());
		for (ServiceInvoker si : timeoutInvokers)
		{
			si.handler.onCallback(INVOKE_TIMEOUT);
			gs.getLogger().debug("game web service remove timeout task callback");
		}
	}
	
	private synchronized void addMonitorTask(ServiceInvoker si)
	{
		invokers.put(si.getId(), si);
	}
	
	private synchronized ServiceInvoker removeAndGetTask(int id)
	{
		return invokers.remove(id);
	}
	
	private synchronized List<ServiceInvoker> removeAndGetTimeoutTasks(long now)
	{
		List<ServiceInvoker> timeoutInvokers = new ArrayList<ServiceInvoker>();
		Iterator<Map.Entry<Integer, ServiceInvoker>> it = invokers.entrySet().iterator();
		while( it.hasNext() )
		{
			ServiceInvoker si = it.next().getValue();
			if(si.isTooOld(now) )
			{
				timeoutInvokers.add(si);
				it.remove();
			}
		}
		return timeoutInvokers;
	}
	
	public void excuteGameWebService(ServiceInvoker invoker)
	{
		addMonitorTask(invoker);
		executor.execute(invoker);
	}
	
//	class ReportOnlineInfoTask implements Runnable
//	{
//		int invokeId;
//		int time;
//		int areaId;
//		int gsid;
//		int platId;
//		int userCount;
//		public ReportOnlineInfoTask(int invokeId, int time, int areaId, int gsid, int platId, int userCount)
//		{
//			this.invokeId = invokeId;
//			this.time = time;
//			this.areaId = areaId;
//			this.gsid = gsid;
//			this.platId = platId;
//			this.userCount = userCount;
//		}
//		
//		public void run()
//		{
//			if (gwservice == null)
//				initServiceImpl();
//			int result = reportOnlineInfoImpl(gwservice, time, areaId, gsid, platId, userCount);
//			if (result == INVOKE_EXCEPTION)
//				gwservice = null;
//			onCallback(time, areaId, gsid, platId, userCount, result);
//		}
//		
//		private void onCallback(int time, int areaId, int gsid, int platId, int userCount, int result)
//		{
//			gs.getLogger().debug("reportOnlineInfo: time=" + time + ", areaId=" + areaId + ", gsid=" + gsid + ", platId=" + platId + ", userCount=" + userCount + ", result=" + result + "==>call return invokeId=" + invokeId);
//			ServiceInvoker si = removeAndGetTask(invokeId);
//			if (si != null)
//			{
//				si.callback.onReportOnlineInfoCallback(result);
//			}
//			else
//			{
//				gs.getLogger().warn("reportOnlineInfo: time=" + time + ", areaId=" + areaId + ", gsid=" + gsid + ", platId=" + platId + ", userCount=" + userCount + ", result=" + result + "==>but timeout invokeId=" + invokeId);
//				//gs.getGameStatLogger().roleAction(roleid, GameStateLogger.Actions.DUIHUANTIMEOUT, error, cdkey, -1, -1, -1, -1, 0, 0, 0, 0, 0, "", "", "");
//			}
//		}
//	}
	
	public void reportOnlineInfo(int time, int userCount, boolean registerCount, ReportOnlineInfoCallback callback)
	{
		excuteGameWebService(new ReportOnlineInfoInvoker(time, userCount, registerCount, callback));
	}

	public void reportRegisterInfo(int time, int registerCount, ReportRegisterInfoCallback callback)
	{
		excuteGameWebService(new ReportRegisterInfoInvoker(time, registerCount, callback));
	}
	
	public void exchangeCDKey(int roleid, String rolename, String cdkey, String channel, ExchangeItemCallback callback)
	{
		if (checkCDKeyValid(cdkey))
		{
			excuteGameWebService(new ExchangeItemInvoker(roleid, rolename, cdkey, channel, callback));
		}
		else
		{
			callback.onExchangeItemCallback(-1, null);
			gs.getLogger().debug("exchangeCDKey roleid=" + roleid + " cdkey=" + cdkey + " invalid!");
		}
		
	}
	
	private static boolean checkCDKeyValid(String cdkey)
	{
		return GameWebService.checkCDKeyValid(cdkey);
	}
	
//	private static int getCDKeyLength()
//	{
//		return CDKeyGen.getCDKeyLength();
//	}
	
	
//	class ExchangeItemTask implements Runnable
//	{
//		int invokeId;
//		int gsid;
//		int roleid;
//		String rolename;
//		String cdkey;
//		String channel;
//		ExchangeItemTask(int invokeId, int gsid, int roleid, String rolename, String cdkey, String channel)
//		{
//			this.invokeId = invokeId;
//			this.gsid = gsid;
//			this.roleid = roleid;
//			this.rolename = rolename;
//			this.cdkey = cdkey;
//			this.channel = channel;
//		}
//		
//		public void run()
//		{
//			if (gwservice == null)
//				initServiceImpl();
//			int error = INVOKE_EXCEPTION;
//			List<i3k.SBean.DropEntry> gift = null;
//			String result = exchangeItemImpl(gwservice, gsid, roleid, rolename, cdkey, channel);
//			String[] strs = result.split("\\|");
//			if (strs.length >= 1)
//			{
//				try
//				{
//					error = Integer.parseInt(strs[0]);
//					if (error == 0)
//					{
//						if (strs.length == 2)
//						{
//							String[] giftstrs = strs[1].split(";");
//							gift = new ArrayList<i3k.SBean.DropEntry>();
//							for (String giftstr : giftstrs)
//							{
//								String[] gstrs = giftstr.split(",");
//								if (gstrs.length != 3)
//								{
//									error = INVOKE_PARSE_ERROR;
//									gift = null;
//									break;
//								}
//								int type = Integer.parseInt(gstrs[0]);
//								int id = Integer.parseInt(gstrs[1]);
//								int count = Integer.parseInt(gstrs[2]);
//								i3k.SBean.DropEntry den = new i3k.SBean.DropEntry((byte)type, (byte)id, (short)count);
//								gift.add(den);
//							}
//						}
//						else
//						{
//							error = INVOKE_PARSE_ERROR;
//						}
//					}
//				}
//				catch (Exception e)
//				{
//					
//				}
//			}
//			if (error == INVOKE_EXCEPTION)
//				gwservice = null;
//			onCallback(gsid, roleid, rolename, cdkey, channel, error, gift);
//		}
//		
//		private void onCallback(int gsid, int roleid, String rolename, String cdkey, String channel, int error, List<i3k.SBean.DropEntry> gift)
//		{
//			gs.getLogger().debug("exchangeCDKey: gsid=" + gsid + ", roleid=" + roleid + ", rolename=" + rolename + ", cdkey=" + cdkey + ", channel=" + channel + ", error=" + error + "==>call return invokeId=" + invokeId);
//			ServiceInvoker si = removeAndGetTask(invokeId);
//			if (si != null)
//			{
//				si.callback.onExchangeItemCallback(error, gift);
//			}
//			else
//			{
//				gs.getLogger().warn("exchangeCDKey: gsid=" + gsid + ", roleid=" + roleid + ", rolename=" + rolename + ", cdkey=" + cdkey + ", channel=" + channel + ", error=" + error + "==>but timeout invokeId=" + invokeId);
//				//gs.getGameStatLogger().roleAction(roleid, GameStateLogger.Actions.DUIHUANTIMEOUT, error, cdkey, -1, -1, -1, -1, 0, 0, 0, 0, 0, "", "", "");
//			}
//		}
//	}
	
	private int reportOnlineInfoImpl(GWService gwservice, int time, int areaId, int platId, int gsid, String gameAppId, int userCount)
	{
		int result = INVOKE_EXCEPTION;
		try 
		{
			if (gwservice != null)
			{
				int retVal = gwservice.reportOnlineInfo(time, areaId, platId, gsid, gameAppId, userCount);
				result = (retVal >= 1 ? INVOKE_SUCCESS : INVOKE_WEBSERVICE_CALL_FAILED);
			}
		}
		catch (Exception e)
		{
			gs.getLogger().warn(e.getMessage());
		}
		return result;
	}
	
	private int reportRegisterInfoImpl(GWService gwservice, int time, int areaId, int platId, int gsid, int registerCount)
	{
		int result = INVOKE_EXCEPTION;
		try 
		{
			if (gwservice != null)
			{
				int retVal = gwservice.reportRegisterInfo(time, areaId, platId, gsid, registerCount);
				result = (retVal >= 1 ? INVOKE_SUCCESS : INVOKE_WEBSERVICE_CALL_FAILED);
			}
		}
		catch (Exception e)
		{
			gs.getLogger().warn(e.getMessage());
		}
		return result;
	}
	
	private String exchangeItemImpl(GWService gwservice, int gsid, int roleid, String rolename, String cdkey, String channel)
	{
		String result = INVOKE_EXCEPTION + "|";
		try 
		{
			if (gwservice != null)
			{
				result = gwservice.exchangeItem(gsid, roleid, rolename, cdkey, channel);
			}
		}
		catch (Exception e)
		{
			gs.getLogger().warn(e.getMessage());
		}
		return result;
	}
	
	public static void main(String [] args)
	{
		if (args.length > 0)
		{
			WSTest t = new WSTest();
			t.test(args[0]);
		}
		else
		{
			System.out.println("please input cdkeys file path");
		}
		System.out.println("main exit");
	}
	
}


class WSTest
{
	//private ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	public WSTest()
	{
		
	}
	
	public void test(String cdkeyPath)
	{
		TestCDKey tck = new TestCDKey(cdkeyPath);
		for (int i = 0; i < 100; ++i)
		{
			//TestGameServer tgs = new TestGameServer(tck.getCDKeys());
			TestGameServer tgs = new TestGameServer(tck);
			tgs.test();
		}
	}
}


class TestCDKey
{
	private String propertiesFileName = "cdkeytest.txt";
	private String keyNamePrefix = "key_";
	private List<String> cdkeys = new ArrayList<String>();
	private boolean[] flags; 
		
	public TestCDKey(String path)
	{
		propertiesFileName = path;
		loadKeys();
	}
	private void loadKeys() 
	{
		try {
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFileName));
            Properties properties = new Properties();
            System.out.println("load properties file : " + propertiesFileName);
            properties.load(in);
            java.util.Enumeration<?> en = properties.propertyNames();
            while (en.hasMoreElements()) 
            {
                String key = (String)en.nextElement();
                String value = properties.getProperty(key);
                if (key.startsWith(keyNamePrefix))
                {
                	cdkeys.add(value);
                }
            }
            in.close();
        }
		catch (Exception e) 
        {
            e.printStackTrace();
        }
		flags = new boolean[cdkeys.size()];
	}
	
	public String[] getCDKeys()
	{
		return cdkeys.toArray(new String[cdkeys.size()]);
	}
	
	public boolean testSetCDKeyUsed(int index)
	{
		if (!flags[index])
		{
			flags[index] = true;
			return true;
		}
		return false;
	}
	
	public String getCDKey(int index)
	{
		return cdkeys.get(index);
	}
	
	public int getCDKeyCount()
	{
		//return cdkeys.size();
		return 5000;
	}
}


class TestGameServer extends GameServer
{
	private static int nextId;
	private GameService gservice;
	private ExecutorService es = Executors.newCachedThreadPool();
	private TestCDKey tck;
	private int taskCount;
	private int nextRoleId;
	private Map<String, Role> name2Roles = new HashMap<String, Role>();
	private Role[] roles;
	private Random random = new Random();
    private String roleNameXing = "’‘«ÆÀÔ¿Ó÷‹Œ‚÷£Õı∑Î≥¬Ò“Œ¿ΩØ…Ú∫´—Ó÷Ï«ÿ”»–Ì∫Œ¬¿ ©’≈ø◊≤‹—œª™ΩŒ∫Ã’Ω™";
    private String roleNameMing = "“ª∂˛»˝ÀƒŒÂ¡˘∆ﬂ∞Àæ≈ Æ";

	
	static class Role
	{
		int id;
		String name;
		Role(int id, String name)
		{
			this.id = id;
			this.name = name;
		}
	}
	
	public TestGameServer(TestCDKey tck)
	{
		super(null);
		this.getConfig().id = ++nextId;
		createRole();
		gservice = new GameService(this);
		gservice.init("localhost", 8080);
		this.tck = tck;
	}
	
	private void createRole()
	{
		for (int i = 0; i < 250; ++i)
			createRandomRole();
		roles = name2Roles.values().toArray(new Role[name2Roles.size()]);
	}
	
	private void createRandomRole()
	{
		String name = getRandomRoleName();
		while (name2Roles.containsKey(name) && name2Roles.size() < roleNameXing.length()*roleNameMing.length())
		{
			name = getRandomRoleName();
		}
		if (name2Roles.size() < roleNameXing.length()*roleNameMing.length())
		{
			int id = ++nextRoleId;
			name2Roles.put(name, new Role(id, name));
		}
	}
	
	private String getRandomRoleName() 
	{
        int xingIndex = random.nextInt(roleNameXing.length());
        int mingIndex = random.nextInt(roleNameMing.length());
        return roleNameXing.substring(xingIndex, xingIndex+1) + roleNameMing.substring(mingIndex, mingIndex+1);
    }
	

	
	public Role getRandomRole()
	{
		int index = random.nextInt(roles.length);
		return roles[index];
	}
	
	public TestCDKey getTestCDKey()
	{
		return tck;
	}
	
	public void test()
	{
		for (int i = 0; i < 20; ++i)
		{
			addTask();
		}
		es.shutdown();
	}
	
	public synchronized void addTask()
	{
		taskCount++;
		TestGameService tgs = new TestGameService(gservice, this);
		//tgs.setCDKeys(cdkeys);
		es.execute(tgs);
	}
	
	public synchronized void decTask()
	{
		taskCount--;
		if (taskCount == 0)
		{
			gservice.shutDown();
		}
	}
}

class TestGameService implements Runnable
{
	private GameService gservice;
	private TestGameServer gs;
	//private String[] cdkeys;
	//private boolean[] flags;
	private Random random = new Random();
    private int startIndex = -1;
    private int lastCDKeyIndex = -1;
	public TestGameService(GameService gservice, TestGameServer gs)
	{
		this.gservice = gservice;
		this.gs = gs;
	}
	
//	public void setCDKeys(String[] cdkeys)
//	{
//		this.cdkeys = cdkeys;
//		flags = new boolean[cdkeys.length];
//	}
	
	
	
	private String getNextCDKey()
	{
		TestCDKey tck = gs.getTestCDKey();
		if (startIndex < 0)
		{
			int loopCount = tck.getCDKeyCount()/2;
			while (loopCount > 0)
			{
				loopCount--;
				int index = random.nextInt(tck.getCDKeyCount());
				if (tck.testSetCDKeyUsed(index))
				{
					return tck.getCDKey(index);
				}
			}
			startIndex = random.nextInt(tck.getCDKeyCount());
			lastCDKeyIndex = startIndex;
		}
		
		if (lastCDKeyIndex >= startIndex)
		{
			for (int i = lastCDKeyIndex; i < tck.getCDKeyCount(); ++i)
			{
				if (tck.testSetCDKeyUsed(i))
				{
					lastCDKeyIndex = i;
					return tck.getCDKey(i);
				}
			}
		}
		for (int i = 0; i < startIndex; ++i)
		{
			if (tck.testSetCDKeyUsed(i))
			{
				lastCDKeyIndex = i;
				return tck.getCDKey(i);
			}
		}
		return "";
	}
	
	
	private boolean exchangeItem()
	{
		TestGameServer.Role role = gs.getRandomRole();
		String cdkey = getNextCDKey();
		if (cdkey.isEmpty())
			return false;
		System.out.println("exchangeCDKey: gsid=" + gs.getConfig().id + ", rolename=" + role.name + ", cdkey=" + cdkey + " start");
		gservice.exchangeCDKey(role.id, role.name, cdkey, "dummy", new TestCallback(role.name, cdkey));
		return true;
	}
	
	private class TestCallback extends GameService.ExchangeItemCallback
	{
		private String rolename;
		private String cdkey;
		
		public TestCallback(String rolename, String cdkey)
		{
			this.rolename = rolename;
			this.cdkey = cdkey;
		}
		
		public void onExchangeItemCallback(int result, List<i3k.SBean.DropEntry> gift)
		{
			System.out.println("exchangeCDKey: gsid=" + gs.getConfig().id + ", rolename=" + rolename + ", cdkey=" + cdkey + ", result=" + result + "==>user callback");
		}
	}
	
	public void run()
	{
		while (exchangeItem());
		gs.decTask();
		System.out.println("task exit");
	}
	
	
}




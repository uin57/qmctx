package com.joypiegame.gameservice.webservice.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;


import com.joypiegame.gameservice.db.DBExecutor;
import com.joypiegame.gameservice.webservice.service.GWService;
import com.joypiegame.gameservice.webservice.server.GWServiceImpl;
import com.joypiegame.gameservice.webservice.client.GameWebService;
import com.joypiegame.gameservice.webservice.service.cdkey.CDKeyGen;

public class TestWebService {
	public static void main(String[] args) {
		ExchangeItemTest.serviceTest();
    }
}

class CheckCDKey {
	public static void main(String[] args) {
		for (String arg : args) {
			int result = CDKeyGen.getCDKeyBatchId(arg);
			System.out.println("check cdkey : " + arg + ", result = " + result);
		}
	}
}

class ExchangeItemTest {
	private String propertiesFileName = "cdkeytest.txt";
	private String keyNamePrefix = "key_";
	private List<String> cdkyes = new ArrayList<String>();
	private GWServiceImpl gsimpl = new GWServiceImpl();
	private GameWebService gws = new GameWebService();
	private Random random = new Random();
	private int gsid = 1001;
    private String roleNameXing = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜";
    private String roleNameMing = "一二三四五六七八九十";
    private Map<String, Map<String, Integer>> roleOwnItems = new HashMap<String, Map<String, Integer>>();
	private DBExecutor dbExecutor = new DBExecutor();
	ExchangeItemTest() {
		dbExecutor.setDbUrl("localhost", "3306");
		dbExecutor.setDbUser("root", "zhangfeiwoyao");
	}
	
	private void loadKeys() {
		try {
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFileName));
            Properties properties = new Properties();
            System.out.println("load properties file : " + propertiesFileName);
            properties.load(in);
            java.util.Enumeration<?> en = properties.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String)en.nextElement();
                String value = properties.getProperty(key);
                if (key.startsWith(keyNamePrefix)) {
                	cdkyes.add(value);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private String getRandomRoleName() {
        int xingIndex = random.nextInt(roleNameXing.length());
        int mingIndex = random.nextInt(roleNameMing.length());
        return roleNameXing.substring(xingIndex, xingIndex+1) + roleNameMing.substring(mingIndex, mingIndex+1);
    }
	private String getUniqueRoleName() {
		String name = getRandomRoleName();
		while (roleOwnItems.containsKey(name)) {
			name = getRandomRoleName();
		}
		roleOwnItems.put(name, new HashMap<String, Integer>());
		return name;
	}
	

	private class TestDBInvoke implements Runnable {
		private String rolename;
		private Map<String, Integer> items;
		public TestDBInvoke(String rolename) {
			this.rolename = rolename;
			items = roleOwnItems.get(rolename);
		}
		public void run() {
			for (String cdkey : cdkyes) {
				String result = gsimpl.exchangeItem(gsid,  -1, rolename, cdkey, "dummy");
				System.out.println(String.format("rolename=%s, cdkey=%s, exchange result = (%s)", rolename, cdkey, result));
				String[] resultStr = result.split("\\|");
				int error = Integer.parseInt(resultStr[0]);
				items.put(cdkey, error);
			}
		}
	}
	
	private void dbTestImpl() {
		loadKeys();
		java.util.concurrent.ExecutorService es = java.util.concurrent.Executors.newCachedThreadPool();
		for (int i = 0; i < 100; ++i)
			es.execute(new TestDBInvoke(getUniqueRoleName()));
	}
	
	public static void dbTest() {
		ExchangeItemTest test = new ExchangeItemTest();
    	test.dbTestImpl();
	}
	
	private class TestServiceInvoke implements Runnable {
		private String rolename;
		private Map<String, Integer> items;
		private GWService gservice;
		private String wsdlUrl = "http://localhost:8080/gameservice/cdkey?wsdl";
		private String targetNS = "http://server.gameservice.joypiegame.com/";
		private String serviceName = "ExchangeCDKeyImplService";
		public TestServiceInvoke(String rolename) {
			this.rolename = rolename;
			items = roleOwnItems.get(rolename);
			try {
				gservice = gws.getGameService(wsdlUrl, targetNS, serviceName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void run() {
			for (String cdkey : cdkyes) {
				String result = gservice.exchangeItem(gsid, -1, rolename, cdkey, "dummy");
				System.out.println(String.format("rolename=%s, cdkey=%s, exchange result = (%s)", rolename, cdkey, result));
				String[] resultStr = result.split("\\|");
				int error = Integer.parseInt(resultStr[0]);
				items.put(cdkey, error);
			}
		}
	}
	
	
	private void serviceTestImpl() {
		loadKeys();
		java.util.concurrent.ExecutorService es = java.util.concurrent.Executors.newCachedThreadPool();
		for (int i = 0; i < 30; ++i)
			es.execute(new TestServiceInvoke(getUniqueRoleName()));
	}
	
	public static void serviceTest() {
		ExchangeItemTest test = new ExchangeItemTest();
    	test.serviceTestImpl();
	}
	
	public static void main(String[] args) {
		dbTest();
    }
}





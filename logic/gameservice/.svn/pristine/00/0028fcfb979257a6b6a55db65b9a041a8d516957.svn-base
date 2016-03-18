package com.joypiegame.gameservice.webservice.service.cdkey;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Random;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
//import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class CDKeyGen {
    private static final int CDKYE_CODE_LENGTH = 12;
    private static final int BATCH_CODE_LENGTH = 3;
    private static final int CHECKSUM_CODE_LENGTH = 1; 
    private static final int CDKEY_LENGTH = CDKYE_CODE_LENGTH + BATCH_CODE_LENGTH + CHECKSUM_CODE_LENGTH;
	private static final char[] VALID_CHARS = {
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	    };
	private static final String INIT_BATCH_CODE = "I3K";
	private static final int BATCH_ID_OFFSET = getInitBatchCodeOffset();
	
	private static final String KEY_NAME_PREFIX_IN_PROPERTIES = "key_";
	private static final String SEPARATOR_CHAR_IN_PROPERTIES = "_";
    private static final String PROPERTIES_FILE_NAME_PREFIX = "cdkey_";
    private static final String PROPERTIES_FILE_SEPARATOR_CHAR = "_";
    private static final String PROPERTIES_FILE_EXTENSION_NAME = ".properties";
    
    private static Random random = new Random();
    //private Map<String, Integer> batchCodes = new TreeMap<String, Integer>();
    //private int maxBatchId;
    //private Set<String> randomCDKeyCodes = new HashSet<String>();
    //private Map<String, Long> allBatchCDKeys = new TreeMap<String, Long>();
    //private Map<Integer, BatchGenInfoOldFormat> allBatchGenInfoOldFormat = new TreeMap<Integer, BatchGenInfoOldFormat>();
    //private Map<Integer, BatchGenInfo> allBatchGenInfo = new TreeMap<Integer, BatchGenInfo>();
    
    //private int batchId = 1;
    //private String batchIdCode = "I3K";
    //private int batchGenId = 1;
    //private int itemIdOldFormat = 77;
    //private String giftItems = "0,77,1";
    //private String channelName = "all";
    //private Date activeDate = Calendar.getInstance().getTime();
    //private int validDayOldFormat = 30;
    //private Date endDate = Calendar.getInstance().getTime();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //private String genCDKeyFileName;
    //private String cdKeyResultFileName;
    private static final String GEN_CDKEY_FILENAME_PREFIX = "gamecdkey-";
    private static final String GEN_CDKEY_FILENAME_SEPARATOR_CHAR = "-";
    private static final String SQL_FILE_EXTENSION_NAME = ".sql";
    private static final String CDKEY_FILENAME_PREFIX = "key-";
    private static final String CDKEY_FILENAME_SEPARATOR_CHAR = "_";
    private static final String CDKEY_FILE_EXTENSION_NAME = ".txt";
    
    private static final String BATCH_GEN_ID_PROPERTY_NAME = "batchGenId";
    private static final String BATCH_GEN_ITEM_ID_PROPERTY_NAME_PREFIX = "itemId_";
    private static final String BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_PREFIX = "channelName_";
    private static final String BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_PREFIX = "activeDate_";
    private static final String BATCH_GEN_VALID_DAY_PROPERTY_NAME_PREFIX = "validDay_";
    
    private static final String BATCH_ID_PROPERTY_NAME = "batchId";
    private static final String BATCH_GEN_ID_PROPERTY_NAME_PREFIX = "genInfo_";
    private static final String BATCH_GEN_MAX_ID_PROPERTY_NAME_PREFIX = "maxGenId";
    private static final String BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_SUFFIX = "_channelName";
    private static final String BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_SUFFIX = "_activeDate";
    private static final String BATCH_GEN_END_DATE_PROPERTY_NAME_SUFFIX = "_endDate";
    private static final String BATCH_GEN_ITEMS_PROPERTY_NAME_SUFFIX = "_items";
    
    private Properties properties = new Properties();
    
    static class BatchCDKeys {
    	int batchId;
    	Map<Integer, BatchGenInfo> genInfo = new TreeMap<Integer, BatchGenInfo>();
    	//Map<String, Long> allBatchCDKeys = new TreeMap<String, Long>();
    }
    
    static class BatchCDKeysOldFormat {
    	int batchId;
    	Map<Integer, BatchGenInfoOldFormat> genInfo = new TreeMap<Integer, BatchGenInfoOldFormat>();
    	//private Map<String, Long> allBatchCDKeys = new TreeMap<String, Long>();
    	BatchCDKeys toNewFormat() {
    		BatchCDKeys batchCdkeys = new BatchCDKeys();
    		batchCdkeys.batchId = batchId;
    		for (Entry<Integer, BatchGenInfoOldFormat> e : genInfo.entrySet()) {
    			BatchGenInfo newGenInfo = new BatchGenInfo();
    			batchCdkeys.genInfo.put(e.getKey(), e.getValue().toBatchGenInfo());
    		}
    		return batchCdkeys;
    	}
    }
    
    static class BatchGenInfoOldFormat {
    	int itemId = 77;
    	String channelName = "all";
    	Date activeDate = Calendar.getInstance().getTime();
    	int validDay = 30;
    	Map<Integer, String> allGenCDKeys = new TreeMap<Integer, String>();
    	
    	BatchGenInfo toBatchGenInfo() {
    		BatchGenInfo bgi = new BatchGenInfo(); 
    		bgi.items = "0," + itemId + ",1";
    		bgi.channelName = channelName;
    		bgi.activeDate = activeDate;
    		Calendar endCalendar = Calendar.getInstance();
    		endCalendar.setTime(activeDate);
    		endCalendar.add(Calendar.DAY_OF_MONTH, validDay-1);
    		bgi.endDate = endCalendar.getTime();
    		bgi.allGenCDKeys.putAll(allGenCDKeys);
    		return bgi;
    	}
    }
    
    static class BatchGenInfo {
    	String items = "0,77,1";
    	String channelName = "all";
    	Date activeDate = Calendar.getInstance().getTime();
    	Date endDate = Calendar.getInstance().getTime();
    	Map<Integer, String> allGenCDKeys = new TreeMap<Integer, String>();
    }
    
    static class Gift {
    	public static final byte COMMON_TYPE_NULL = -100;
    	public static final byte COMMON_TYPE_VIT = -8;
    	public static final byte COMMON_TYPE_SPR = -7;
    	public static final byte COMMON_TYPE_NOUSE = -3;
    	public static final byte COMMON_TYPE_STONE = -2;
    	public static final byte COMMON_TYPE_MONEY = -1;
    	public static final byte COMMON_TYPE_ITEM = 0;
    	public static final byte COMMON_TYPE_EQUIP = 1;
    	public static final byte COMMON_TYPE_GENERAL = 2;
    	
    	int type = -100;
    	int id = 0;
    	int count = 1;
    	Gift(int type, int id, int count) {
    		this.type = type;
    		this.id = id;
    		this.count = count;
    	}
    	
    	static Gift newItem(int id, int count) {
    		return new Gift(COMMON_TYPE_ITEM, id, count);
    	}
    	
    	static Gift newEquip(int id, int count) {
    		return new Gift(COMMON_TYPE_EQUIP, id, count);
    	}
    	
    	static Gift newGeneral(int id, int count) {
    		return new Gift(COMMON_TYPE_GENERAL, id, count);
    	}
    	
    	static Gift newMoney(int count) {
    		return new Gift(COMMON_TYPE_MONEY, 0, count);
    	}
    	
    	static Gift newStone(int count) {
    		return new Gift(COMMON_TYPE_STONE, 0, count);
    	}
    	
    	public String toString() {
    		return type + "," + id + "," + count;
    	}
    	
    	public static String toString(Collection<Gift> gift) {
    		StringBuilder giftItems = new StringBuilder();
    		for (Gift g : gift) {
    			if (giftItems.length() != 0)
    				giftItems.append(";");
    			giftItems.append(g.toString());
    		}
    		return giftItems.toString();
    	}
    	
    	public static Gift fromString(String gstr) {
    		Gift gift = null;
    		String[] strs = gstr.split("[iegms]");
    		if (strs.length == 1) {
    			int count = Integer.parseInt(strs[0]);
    			String typeStr = gstr.substring(strs[0].length());
    			if (typeStr.equals("m")) {
    				gift = newMoney(count);
    			}else if (typeStr.equals("s")) {
    				gift = newStone(count);
    			}else {
    				System.out.println("get gift from str("+gstr+") failed");
    			}
    		}else if (strs.length == 2) {
    			int count = Integer.parseInt(strs[0]);
    			int id = Integer.parseInt(strs[1]);
    			String typeStr = gstr.substring(strs[0].length(), strs[0].length()+1);
    			if (typeStr.equals("i")) {
    				gift = newItem(id, count);
    			}else if (typeStr.equals("e")) {
    				gift = newEquip(id, count);
    			}else if (typeStr.equals("g")) {
    				gift = newGeneral(id, count);
    			}else {
    				System.out.println("get gift from str("+gstr+") failed");
    			}
    		}else {
    			System.out.println("get gift from str("+gstr+") failed");
    		}
    		return gift;
    	}
    }
   
    public static int getCDKeyBatchId(String cdkey) {
    	int batchId = -1;
    	if (isValid(cdkey))
    		batchId = getBatchId(cdkey.substring(CDKYE_CODE_LENGTH, CDKYE_CODE_LENGTH + BATCH_CODE_LENGTH));
    	return batchId;
    }
    
    public static boolean isValid(String cdkey) {
    	if (cdkey == null || cdkey.length() != CDKEY_LENGTH)
    		return false;
    	int sum = 0;
    	cdkey = cdkey.toUpperCase();
    	String validStr = new String(VALID_CHARS);
    	for (int i = 0; i < CDKEY_LENGTH; ++i) {
    		if (validStr.indexOf(cdkey.charAt(i)) == -1)
    			return false;
    		sum += Character.getNumericValue(cdkey.charAt(i));
    	}
    	return (sum % VALID_CHARS.length == 0);
    }
    
    public static int getCDKeyLength() {
    	return CDKEY_LENGTH;
    }
    
    private static String genCDKey(String randomCode, String batchString) {
    	String code = randomCode + batchString;
        int sum = 0;
        for (int i = 0; i < code.length(); ++i) {
            sum += Character.getNumericValue(code.charAt(i));
        }
        int checksum = (VALID_CHARS.length - sum%VALID_CHARS.length)%VALID_CHARS.length;
        String cdKeyCode = code + VALID_CHARS[checksum];
        return cdKeyCode;
    }
    
    private static String newRandomCode(Random random, int codeLength) {
        char[] code = new char[codeLength];
        for (int i = 0; i < codeLength; ++i) {
            code[i] = VALID_CHARS[random.nextInt(VALID_CHARS.length)];
        }
        return new String(code);
    }
    
    public CDKeyGen() {
    }
    
    private static String getCDKeyFileName(int batchId, int genId) {
    	return GEN_CDKEY_FILENAME_PREFIX + batchId + GEN_CDKEY_FILENAME_SEPARATOR_CHAR + genId + SQL_FILE_EXTENSION_NAME;
    }
    
    private static String getResultFileName(int batchId, int genId, String channel) {
    	return CDKEY_FILENAME_PREFIX + channel + CDKEY_FILENAME_SEPARATOR_CHAR + batchId + CDKEY_FILENAME_SEPARATOR_CHAR + genId + CDKEY_FILE_EXTENSION_NAME;
    }
    
    private static int getInitBatchCodeOffset() {
    	int offset = 0;
    	for (int i = 0; i < INIT_BATCH_CODE.length(); ++i) {
    		offset = Character.getNumericValue(INIT_BATCH_CODE.charAt(i)) + offset*VALID_CHARS.length;
    	}
    	return offset;
    }
    
    private static int getBatchId(String batchCode) {
    	batchCode = batchCode.toUpperCase();
    	int batchId = -1;
    	if (batchCode.length() == BATCH_CODE_LENGTH) {
    		int batchIdOffset = 0;
    		for (int i = 0; i < batchCode.length(); ++i) {
    			batchIdOffset = Character.getNumericValue(batchCode.charAt(i)) + batchIdOffset*VALID_CHARS.length;
        	}
    		int batchCodeCount = 1;
        	for (int i = 0; i < BATCH_CODE_LENGTH; ++i)
        		batchCodeCount *= VALID_CHARS.length;
        	batchId = (batchCodeCount + batchIdOffset - BATCH_ID_OFFSET)%batchCodeCount;
    	}
    	return batchId;
    }
    
    private static String getBatchCode(int batchId) {
    	int offsetBatchId = BATCH_ID_OFFSET + batchId;
    	int batchCodeCount = 1;
    	for (int i = 0; i < BATCH_CODE_LENGTH; ++i)
    		batchCodeCount *= VALID_CHARS.length;
    	offsetBatchId %= batchCodeCount;
    	char[] code = new char[BATCH_CODE_LENGTH];
    	for (int i = 0; i < BATCH_CODE_LENGTH; ++i) {
    		int radix = 1;
    		for (int j = 0; j < BATCH_CODE_LENGTH-1-i; ++j)
    			radix *= VALID_CHARS.length;
    		int index = offsetBatchId/radix%VALID_CHARS.length;
    		code[i] = VALID_CHARS[index];
    	}
    	return new String(code);
    }
    
    private static String newRandomCDKeyCode() {
        return newRandomCode(random, CDKYE_CODE_LENGTH);
    }
    
    static interface TestKeyExist {
    	boolean isExist(String key);
    	
    	void addKey(String key);
    }
    
    private static String createRandomCDKeyCode(TestKeyExist testcdkeycode) {
        String code = null;
        do {
            code = newRandomCDKeyCode();
        } while(testcdkeycode.isExist(code));
        testcdkeycode.addKey(code);
        return code;
    }
    
    private static String genRandomCDKey(String batchIdCode, TestKeyExist testcdkeycode) {
    	return genCDKey(createRandomCDKeyCode(testcdkeycode), batchIdCode);
    }
    
    static class TestCDKeyCodeExist implements TestKeyExist {
    	Set<String> randomCDKeyCodes = new HashSet<String>();
    	
    	public boolean isExist(String key) {
    		return randomCDKeyCodes.contains(key);
    	}
    	
    	public void addKey(String key) {
    		randomCDKeyCodes.add(key);
    	}
    }
    
    private static String genUniqueCDKey(int batchId, TestKeyExist testcdkey) {
    	String code = getBatchCode(batchId);
    	TestCDKeyCodeExist testcdkeycode = new TestCDKeyCodeExist();
    	String cdkey = genRandomCDKey(code, testcdkeycode);
    	while (testcdkey.isExist(cdkey)) {
    		cdkey = genRandomCDKey(code, testcdkeycode);
    	}
    	return cdkey;
    }
    
    private static String genGenInfoInsertSql(int batchid, int batchGenid, String giftItems, String channelName, Date activeDate,  Date endDate) {
    	return String.format("INSERT INTO giftinfo(bid, gid, items, channel, adate, edate, ctime) VALUES(%d, %d, '%s', '%s', '%s', '%s', NOW());\n", 
    			batchid, batchGenid, giftItems, channelName, dateFormat.format(activeDate), dateFormat.format(endDate));
    }
    
    private static String getCDKeyInsertSql(int batchid, int batchGenid, String cdkey) {
        return String.format("INSERT INTO keyinfo(bid, gid, kvalue) VALUES(%d, %d, '%s');\n", batchid, batchGenid, cdkey);
    }
    
    private static BatchGenInfo genCDKey(int count, String giftItems, String channelName, Date activeDate, Date endDate, BatchCDKeys batchCdkeys) {
    	BatchGenInfo batchGenInfo = new BatchGenInfo();
    	batchGenInfo.items = giftItems;
    	batchGenInfo.channelName = channelName;
    	batchGenInfo.activeDate = activeDate;
    	batchGenInfo.endDate = endDate;
    	
    	
    	class TestCDKeyExist implements TestKeyExist {
    		Set<String> allBatchCDKeys = new HashSet<String>();
    		TestCDKeyExist(BatchCDKeys batchCdkeys) {
    			int count = 0;
    			for (Entry<Integer, BatchGenInfo> e : batchCdkeys.genInfo.entrySet()) {
    				BatchGenInfo geninfo = e.getValue();
    				allBatchCDKeys.addAll(geninfo.allGenCDKeys.values());
    				count += geninfo.allGenCDKeys.size();
    			}
    			if (count != allBatchCDKeys.size())
    			{
    				System.out.println("batch gen cdkey duplication !!!!!!!!!!!!!!!!!!!");
        			System.exit(-1);
    			}
    		}
    		
        	public boolean isExist(String key) {
        		return allBatchCDKeys.contains(key);
        	}
        	
        	public void addKey(String key) {
        		allBatchCDKeys.add(key);
        	}
        }
    	
        for (int i = 0; i < count; ++i) {
        	String cdkey = genUniqueCDKey(batchCdkeys.batchId, new TestCDKeyExist(batchCdkeys));
            System.out.println("create NO." + (i+1) + " cdkey: " + cdkey);
            batchGenInfo.allGenCDKeys.put(i, cdkey);
        }
        return batchGenInfo;
    }
    
    private static void createNextGenCDKey(int count, String giftItems, String channelName, Date activeDate, Date endDate, BatchCDKeys batchCdkeys) {
    	int newGenId = batchCdkeys.genInfo.size() + 1;
    	System.out.println("genInfo : batchId="+batchCdkeys.batchId+", batchGenId="+newGenId+", giftItems="+giftItems+", channel="+channelName+", activeDate="+activeDate+", endDate="+endDate);
    	BatchGenInfo batchGenInfo = genCDKey(count, giftItems, channelName, activeDate, endDate, batchCdkeys);
    	batchCdkeys.genInfo.put(newGenId, batchGenInfo);
    	saveProperties(batchCdkeys);
    	saveGenCDkeySqlResult(batchCdkeys.batchId, newGenId, batchGenInfo);
    }
    
    private static void saveProperties(BatchCDKeys batchCdkeys) {
    	try {
        	Properties properties = new Properties();
        	properties.setProperty(BATCH_ID_PROPERTY_NAME, String.valueOf(batchCdkeys.batchId));
        	properties.setProperty(BATCH_GEN_ID_PROPERTY_NAME_PREFIX + BATCH_GEN_MAX_ID_PROPERTY_NAME_PREFIX, String.valueOf(batchCdkeys.genInfo.size()));
        	for (Entry<Integer, BatchGenInfo> e : batchCdkeys.genInfo.entrySet()) {
        		int genId = e.getKey();
        		System.out.println("......save batch gen info properties: batchId=" + batchCdkeys.batchId + ", genId=" + genId);
        		BatchGenInfo genInfo = e.getValue();
        		properties.setProperty(BATCH_GEN_ID_PROPERTY_NAME_PREFIX + genId + BATCH_GEN_ITEMS_PROPERTY_NAME_SUFFIX, String.valueOf(genInfo.items));
                properties.setProperty(BATCH_GEN_ID_PROPERTY_NAME_PREFIX + genId + BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_SUFFIX, genInfo.channelName);
                properties.setProperty(BATCH_GEN_ID_PROPERTY_NAME_PREFIX + genId + BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_SUFFIX, String.valueOf(genInfo.activeDate));
                properties.setProperty(BATCH_GEN_ID_PROPERTY_NAME_PREFIX + genId + BATCH_GEN_END_DATE_PROPERTY_NAME_SUFFIX, String.valueOf(genInfo.endDate));
                System.out.println("......save batch gen cdkey properties: batchId=" + batchCdkeys.batchId + ", genId=" + genId + ", keyCount=" + genInfo.allGenCDKeys.size());
                for (Entry<Integer, String> keyentry : genInfo.allGenCDKeys.entrySet()) {
                	int seqNo = keyentry.getKey();
                	String cdkey = keyentry.getValue();
                	String propertyName = KEY_NAME_PREFIX_IN_PROPERTIES + genId + SEPARATOR_CHAR_IN_PROPERTIES + seqNo;
                	properties.setProperty(propertyName, cdkey);
                }
        	}
        	String propertiesFileName = getPropertiesFIleNameFromBatchIdCode(batchCdkeys.batchId, getBatchCode(batchCdkeys.batchId));
            FileOutputStream out = new FileOutputStream(propertiesFileName);
            properties.store(out, "");
            out.close();
            System.out.println("save properties file : " + propertiesFileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    
    private static void saveGenCDkeySqlResult(int batchId, int genId, BatchGenInfo genInfo) {
    	try {
    		String genCDKeySqlFileName =  getCDKeyFileName(batchId, genId);
    		String cdKeyResultFileName =  getResultFileName(batchId, genId, genInfo.channelName);
    		
        	Writer sqlwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(genCDKeySqlFileName), "UTF-8"));
        	Writer resultwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cdKeyResultFileName), "UTF-8"));
        	sqlwriter.write("SET collation_connection = utf8_bin;\n");
        	sqlwriter.write("SET character_set_client = utf8;\n");
        	sqlwriter.write("SET character_set_connection = utf8;\n");
        	sqlwriter.write("SET autocommit=0;\n");
        	
        	String genInfoSql = genGenInfoInsertSql(batchId, genId, genInfo.items, genInfo.channelName, genInfo.activeDate, genInfo.endDate);
        	sqlwriter.write(genInfoSql);
        	for (Entry<Integer, String> keyentry : genInfo.allGenCDKeys.entrySet()) {
        		String cdkey = keyentry.getValue();
        		String cdkeySql = getCDKeyInsertSql(batchId, genId, cdkey);
                sqlwriter.write(cdkeySql);
                resultwriter.write(cdkey+"\r\n");
        	}
            sqlwriter.write("SET autocommit=1;\n");
            sqlwriter.flush();
            sqlwriter.close();
            System.out.println("save sql file : " + genCDKeySqlFileName);
            resultwriter.flush();
            resultwriter.close();
            System.out.println("save result file : " + cdKeyResultFileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    

    
    private static String getPropertiesFIleNameFromBatchIdCode(int batchid, String batchidCode) {
    	return PROPERTIES_FILE_NAME_PREFIX + batchid + PROPERTIES_FILE_SEPARATOR_CHAR + batchidCode + PROPERTIES_FILE_EXTENSION_NAME;
    }
    
    private static class BatchCDKeyFileFilter implements FilenameFilter {
    	public BatchCDKeyFileFilter() {
    		
    	}
    	
    	public boolean accept(File dir, String name) {
    		if (name.startsWith(PROPERTIES_FILE_NAME_PREFIX) && name.endsWith(PROPERTIES_FILE_EXTENSION_NAME)) {
    			String batchId2BatchCode = name.substring(PROPERTIES_FILE_NAME_PREFIX.length(), name.length() - PROPERTIES_FILE_EXTENSION_NAME.length());
    			String[] childStrs = batchId2BatchCode.split(PROPERTIES_FILE_SEPARATOR_CHAR);
    			return childStrs.length == 2;
    		}
    		return false;
    	}
    }
    
    private static String[] getAllBatchCDKeyFileNames() {
    	File curfile = new File(".");
    	String[] files = curfile.list(new BatchCDKeyFileFilter());
    	return files;
    }
    
    
    static void convertOldFormatPropertiesFile() {
    	String[] allBatchCDKeyFileNames = getAllBatchCDKeyFileNames();
    	for (String name : allBatchCDKeyFileNames) {
    		String batchId2BatchCode = name.substring(PROPERTIES_FILE_NAME_PREFIX.length(), name.length() - PROPERTIES_FILE_EXTENSION_NAME.length());
			String[] childStrs = batchId2BatchCode.split(PROPERTIES_FILE_SEPARATOR_CHAR);
			int batchid = Integer.parseInt(childStrs[0]);
			String batchCode = childStrs[1];
			convertOldFormatPropertiesFile(batchid, batchCode);
    	}
    }
    
    private static void convertOldFormatPropertiesFile(int batchid, String batchCode) {
    	String propertiesFileName = getPropertiesFIleNameFromBatchIdCode(batchid, batchCode);
    	BatchCDKeysOldFormat batchCDKeysOld = loadKeysOldFormat(propertiesFileName);
    	System.out.println("...convert properties batchid :" + batchid);
    	BatchCDKeys batchCDKeys = batchCDKeysOld.toNewFormat();
    	saveProperties(batchCDKeys);
    }
    
    private static BatchCDKeysOldFormat loadKeysOldFormat(String propertiesFileName) {
    	BatchCDKeysOldFormat batchCDKeys = new BatchCDKeysOldFormat();
		try {
			int batchGenId = 0;
			Map<Integer, Integer> items = new TreeMap<Integer, Integer>();
			Map<Integer, String> channels = new TreeMap<Integer, String>();
			Map<Integer, Date> activeDates = new TreeMap<Integer, Date>();
			Map<Integer, Integer> validDays = new TreeMap<Integer, Integer>();
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFileName));
            Properties properties = new Properties();
            System.out.println("load properties file : " + propertiesFileName);
            properties.load(in);
            in.close();
            {
            	java.util.Enumeration<?> en = properties.propertyNames();
                while (en.hasMoreElements()) {
                    String key = (String)en.nextElement();
                    String value = properties.getProperty(key);
                    if (key.equals(BATCH_ID_PROPERTY_NAME)) {
                    	batchCDKeys.batchId = Integer.parseInt(value);
                    } else if (key.equals(BATCH_GEN_ID_PROPERTY_NAME)) {
                    	batchGenId = Integer.parseInt(value);
                    }
                    else if (key.startsWith(BATCH_GEN_ITEM_ID_PROPERTY_NAME_PREFIX)) {
                    	String genIdStr = key.substring(BATCH_GEN_ITEM_ID_PROPERTY_NAME_PREFIX.length());
                    	int genId = Integer.parseInt(genIdStr);
                    	int itemid = Integer.parseInt(value);
                    	items.put(genId, itemid);
                    }
                    else if (key.startsWith(BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_PREFIX)) {
                    	String genIdStr = key.substring(BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_PREFIX.length());
                    	int genId = Integer.parseInt(genIdStr);
                    	channels.put(genId, value);
                    }
                    else if (key.startsWith(BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_PREFIX)) {
                    	String genIdStr = key.substring(BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_PREFIX.length());
                    	int genId = Integer.parseInt(genIdStr);
                    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0:00"));
                    	cal.setTimeInMillis(Date.parse(value)+TimeZone.getTimeZone("GMT-14:00").getRawOffset());
                    	Date activedate = cal.getTime();
                    	activeDates.put(genId, activedate);
                    }
                    else if (key.startsWith(BATCH_GEN_VALID_DAY_PROPERTY_NAME_PREFIX)) {
                    	String genIdStr = key.substring(BATCH_GEN_VALID_DAY_PROPERTY_NAME_PREFIX.length());
                    	int genId = Integer.parseInt(genIdStr);
                    	int validday = Integer.parseInt(value);
                    	validDays.put(genId, validday);
                	}else if (!key.startsWith(KEY_NAME_PREFIX_IN_PROPERTIES)){
                    	System.out.println("find cdkey property, but key name invalid: key=" + key + ", value=" + value + "!!!!!!!!!!!!!!!!!!!");
                    }
                }
            }
            for (int i = 1; i <= batchGenId; ++i) {
            	BatchGenInfoOldFormat genInfo = new BatchGenInfoOldFormat();
            	if (items.containsKey(i)) 
            		genInfo.itemId = items.get(i);
            	else {
            		System.out.println("can't find itemId property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (channels.containsKey(i)) 
            		genInfo.channelName = channels.get(i);
            	else {
            		System.out.println("can't find channelName property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (activeDates.containsKey(i)) 
            		genInfo.activeDate = activeDates.get(i);
            	else {
            		System.out.println("can't find activeDate property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (validDays.containsKey(i)) 
            		genInfo.validDay = validDays.get(i);
            	else {
            		System.out.println("can't find validDay property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	batchCDKeys.genInfo.put(i, genInfo);
            }
            {
            	java.util.Enumeration<?> en = properties.propertyNames();
            	while (en.hasMoreElements()) {
                    String key = (String)en.nextElement();
                    String value = properties.getProperty(key);
                    if (key.startsWith(KEY_NAME_PREFIX_IN_PROPERTIES)) {
                    	int index = key.indexOf(SEPARATOR_CHAR_IN_PROPERTIES, KEY_NAME_PREFIX_IN_PROPERTIES.length());
                    	if (index > KEY_NAME_PREFIX_IN_PROPERTIES.length() && key.length() > index+1) {
                    		String genIdStr = key.substring(KEY_NAME_PREFIX_IN_PROPERTIES.length(), index);
                    		String keyIdStr = key.substring(index+1);
                    		int genId = Integer.parseInt(genIdStr);
                    		int keyId = Integer.parseInt(keyIdStr);
                    		if (batchCDKeys.genInfo.containsKey(genId)) {
                    			BatchGenInfoOldFormat genInfo = batchCDKeys.genInfo.get(genId);
                    			genInfo.allGenCDKeys.put(keyId, value);
                    		} else {
                    			System.out.println("can't find gen id info, genId=" + genId + "!!!!!!!!!!!!!!!!!!!");
                        		System.exit(-1);
                    		}
                    	} else {
                    		System.out.println("find cdkey property, but key name invalid: key=" + key + ", value=" + value + "!!!!!!!!!!!!!!!!!!!");
                    	}
                    } 
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return batchCDKeys;
	}

    private static BatchCDKeys loadKeys(String propertiesFileName) {
    	BatchCDKeys batchCDKeys = new BatchCDKeys();
		try {
			int maxGenId = 0;
			Map<Integer, String> giftitems = new TreeMap<Integer, String>();
			Map<Integer, String> channels = new TreeMap<Integer, String>();
			Map<Integer, Date> activeDates = new TreeMap<Integer, Date>();
			Map<Integer, Date> endDates = new TreeMap<Integer, Date>();
            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFileName));
            Properties properties = new Properties();
            System.out.println("load properties file : " + propertiesFileName);
            properties.load(in);
            in.close();
            {
            	java.util.Enumeration<?> en = properties.propertyNames();
                while (en.hasMoreElements()) {
                    String key = (String)en.nextElement();
                    String value = properties.getProperty(key);
                    if (key.equals(BATCH_ID_PROPERTY_NAME)) {
                    	batchCDKeys.batchId = Integer.parseInt(value);
                    }
                    else if (key.startsWith(BATCH_GEN_ID_PROPERTY_NAME_PREFIX)) {
                    	String genInfoStr = key.substring(BATCH_GEN_ID_PROPERTY_NAME_PREFIX.length());
                    	if (key.endsWith(BATCH_GEN_MAX_ID_PROPERTY_NAME_PREFIX)) {
                        	maxGenId = Integer.parseInt(value);
                    	}else if (key.endsWith(BATCH_GEN_ITEMS_PROPERTY_NAME_SUFFIX)) {
                        	String genIdStr = genInfoStr.substring(0, genInfoStr.length()-BATCH_GEN_ITEMS_PROPERTY_NAME_SUFFIX.length());
                            int genId = Integer.parseInt(genIdStr);
                            giftitems.put(genId, value);
                    	}else if (key.endsWith(BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_SUFFIX)) {
                        	String genIdStr = genInfoStr.substring(0, genInfoStr.length()-BATCH_GEN_CHANNEL_NAME_PROPERTY_NAME_SUFFIX.length());
                            int genId = Integer.parseInt(genIdStr);
                            channels.put(genId, value);
                    	}else if (key.endsWith(BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_SUFFIX)) {
                        	String genIdStr = genInfoStr.substring(0, genInfoStr.length()-BATCH_GEN_ACTIVE_DATE_PROPERTY_NAME_SUFFIX.length());
                            int genId = Integer.parseInt(genIdStr);
                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0:00"));
                        	cal.setTimeInMillis(Date.parse(value)+TimeZone.getTimeZone("GMT-14:00").getRawOffset());
                        	Date activedate = cal.getTime();
                            activeDates.put(genId, activedate);
                    	}else if (key.endsWith(BATCH_GEN_END_DATE_PROPERTY_NAME_SUFFIX)) {
                        	String genIdStr = genInfoStr.substring(0, genInfoStr.length()-BATCH_GEN_END_DATE_PROPERTY_NAME_SUFFIX.length());
                            int genId = Integer.parseInt(genIdStr);
                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0:00"));
                        	cal.setTimeInMillis(Date.parse(value)+TimeZone.getTimeZone("GMT-14:00").getRawOffset());
                        	Date enddate = cal.getTime();
                            endDates.put(genId, enddate);
                    	}
                    	else {
                    		System.out.println("find cdkey property, but key name invalid: key=" + key + ", value=" + value + "!!!!!!!!!!!!!!!!!!!");
                    	}
                    } else if (!key.startsWith(KEY_NAME_PREFIX_IN_PROPERTIES)){
                    	System.out.println("find cdkey property, but key name invalid: key=" + key + ", value=" + value + "!!!!!!!!!!!!!!!!!!!");
                    }
                }
            }
            for (int i = 1; i <= maxGenId; ++i) {
            	BatchGenInfo genInfo = new BatchGenInfo();
            	if (giftitems.containsKey(i)) 
            		genInfo.items = giftitems.get(i);
            	else {
            		System.out.println("can't find itemId property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (channels.containsKey(i)) 
            		genInfo.channelName = channels.get(i);
            	else {
            		System.out.println("can't find channelName property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (activeDates.containsKey(i)) 
            		genInfo.activeDate = activeDates.get(i);
            	else {
            		System.out.println("can't find activeDate property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	if (endDates.containsKey(i)) 
            		genInfo.endDate = endDates.get(i);
            	else {
            		System.out.println("can't find validDay property, genId=" + i + "!!!!!!!!!!!!!!!!!!!");
            		System.exit(-1);
            	}
            	batchCDKeys.genInfo.put(i, genInfo);
            }
            {
            	java.util.Enumeration<?> en = properties.propertyNames();
            	while (en.hasMoreElements()) {
                    String key = (String)en.nextElement();
                    String value = properties.getProperty(key);
                    if (key.startsWith(KEY_NAME_PREFIX_IN_PROPERTIES)) {
                    	int index = key.indexOf(SEPARATOR_CHAR_IN_PROPERTIES, KEY_NAME_PREFIX_IN_PROPERTIES.length());
                    	if (index > KEY_NAME_PREFIX_IN_PROPERTIES.length() && key.length() > index+1) {
                    		String genIdStr = key.substring(KEY_NAME_PREFIX_IN_PROPERTIES.length(), index);
                    		String keyIdStr = key.substring(index+1);
                    		int genId = Integer.parseInt(genIdStr);
                    		int keyId = Integer.parseInt(keyIdStr);
//                    		long genkeyId = genId;
//                    		genkeyId <<= 32;
//                    		genkeyId |=	keyId;
                    		if (batchCDKeys.genInfo.containsKey(genId)) {
                    			BatchGenInfo genInfo = batchCDKeys.genInfo.get(genId);
                    			genInfo.allGenCDKeys.put(keyId, value);
                    		} else {
                    			System.out.println("can't find gen id info, genId=" + genId + "!!!!!!!!!!!!!!!!!!!");
                        		System.exit(-1);
                    		}
                    	}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return batchCDKeys;
	}
    
    private static BatchCDKeys getExistBatchCDKeys(int batchid) {
    	BatchCDKeys batchCdkeys = new BatchCDKeys();
    	batchCdkeys.batchId = batchid;
    	String batchidCode = getBatchCode(batchid);
        String fileName = getPropertiesFIleNameFromBatchIdCode(batchid, batchidCode);
        File file = new File(fileName);
        if (file.exists())
        {
        	batchCdkeys = loadKeys(fileName);
        }else {
        	System.out.println("batch " + batchid + " properties file(" + fileName + ") not exist...");
        }
        return batchCdkeys;
    }
    

    static void genBatchCDKey(int batchId, int count, String giftItems, String channelName, Date activeDate, Date endDate) {
    	createNextGenCDKey(count, giftItems, channelName, activeDate, endDate, getExistBatchCDKeys(batchId));
    }
    
    private static int getNextBatchId() {
    	int nextBatchId = 1;
    	String[] allBatchCDKeyFileNames = getAllBatchCDKeyFileNames();
    	for (String name : allBatchCDKeyFileNames) {
    		String batchId2BatchCode = name.substring(PROPERTIES_FILE_NAME_PREFIX.length(), name.length() - PROPERTIES_FILE_EXTENSION_NAME.length());
			String[] childStrs = batchId2BatchCode.split(PROPERTIES_FILE_SEPARATOR_CHAR);
			int batchid = Integer.parseInt(childStrs[0]);
			String batchCode = childStrs[1];
			if (batchid > nextBatchId)
				nextBatchId = batchid;
    	}
    	return nextBatchId + 1;
    }
    
    static void genCDKey(int batchId, int count, String giftItems, String channelName, Date activeDate, Date endDate) {
    	genBatchCDKey(batchId < 0 ? getNextBatchId() : batchId, count, giftItems, channelName, activeDate, endDate);
    }
    
    //--ant gen -Dargs='10000 77 all 2014-05-30 30 1'
    //ant gen -Dargs='--c=10000 --g=300i77 --n=all --a=2014-05-30 --e=2014-06-30 --b=4
    //ant gen -Dargs='--convert'
    
    public static void main(String[] args) {
    	boolean convert = false;
    	int genCount = 1000;
    	List<Gift> allGift = new ArrayList<Gift>();
    	String channel = "all";
    	Date adate = Calendar.getInstance().getTime();
    	Date edate = Calendar.getInstance().getTime();
    	int batchId = -1;
    	for (String arg : args) {
    		if (arg.equals("--convert")) {
    			convert = true;
    		}else if (arg.startsWith("--c=")) {
    			String str = arg.substring("--c=".length());
    			genCount = Integer.parseInt(str);
    		}else if (arg.startsWith("--g=")) {
    			String str = arg.substring("--g=".length());
    			Gift gift = Gift.fromString(str);
    			if (gift != null) {
    				allGift.add(gift);
    			}
    		}else if (arg.startsWith("--n=")) {
    			String str = arg.substring("--n=".length());
    			channel = str;
    		}else if (arg.startsWith("--a=")) {
    			String str = arg.substring("--a=".length());
            	try {
            		adate = dateFormat.parse(str);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
    			
    		}else if (arg.startsWith("--e=")) {
    			String str = arg.substring("--e=".length());
    			try {
            		edate = dateFormat.parse(str);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
    		}else if (arg.startsWith("--b=")) {
    			String str = arg.substring("--b=".length());
    			batchId = Integer.parseInt(str);
    		}
    	}
    	if (convert) {
    		convertOldFormatPropertiesFile();
    		System.out.println("convert cdkey end");
    	}else {
    		String[] strs = "-1".split(";");
    		if (genCount < 0) {
    			System.out.println("gen cdkey count input error! exit...");
    			return;
    		}
    		if (allGift.isEmpty()) {
    			System.out.println("gen cdkey gift input error! exit...");
    			return;
    		}
    		if (channel.isEmpty()) {
    			System.out.println("gen cdkey channel input error! exit...");
    			return;
    		}
    		if (adate.after(edate)) {
    			System.out.println("gen cdkey date input error! exit...");
    			return;
    		}
    		genCDKey(batchId, genCount, Gift.toString(allGift), channel, adate, edate);
    		System.out.println("create cdkey end");
    	}
    }
}



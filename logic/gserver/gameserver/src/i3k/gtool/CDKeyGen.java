package i3k.gtool;



import java.util.Collections;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Properties;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;


import java.util.Random;
import java.security.MessageDigest;

	
	public class CDKeyGen
	{
		static class CodeChars
		{
			private static Random random = new Random();
			private static final char[] VALID_CHARS = 
			{
			    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
			};
			private static final Set<Character> VALID_CHAR_SET = new HashSet<Character>();
			static 
			{
				for (int i = 0; i < VALID_CHARS.length; ++i)
				{
					VALID_CHAR_SET.add(VALID_CHARS[i]);
				}
			}
			
			public static boolean isCharValid(char c)
			{
				return VALID_CHAR_SET.contains(c);
			}
			
			public static int getCharsCount()
			{
				return VALID_CHARS.length;
			}
			
			public static char getChar(int index)
			{
				return VALID_CHARS[index];
			}
			
			public static char genRandomChar()
			{
				return VALID_CHARS[random.nextInt(VALID_CHARS.length)];
			}
			public static char getByteChar(byte b)
			{
				int val = 0xff & b;
				return VALID_CHARS[val%VALID_CHARS.length];
			}
		}
		static class RandomCodeGen
		{
			private Set<String> codes = new TreeSet<String>();
			public RandomCodeGen()
			{
				
			}
			
			public StringBuilder genNext(int codeLength)
			{
				StringBuilder code = genRandomCode(codeLength);
				while (codes.contains(code.toString()))
				{
					code = genRandomCode(codeLength);
				}
				return code;
			}
			
			private StringBuilder genRandomCode(int codeLength)
			{
				StringBuilder code = new StringBuilder();
		        for (int i = 0; i < codeLength; ++i) 
		        {
		        	code.append(CodeChars.genRandomChar());
		        }
		        return code;
			}
		}
		
		static class EncryptKeyCode
		{
			private String key = "ksladslfghfkjhk";
			private MessageDigest md;
			public EncryptKeyCode()
			{
				try
				{
					md = java.security.MessageDigest.getInstance("MD5");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
			
			private byte[] genEncryptCode(StringBuilder rawCode)
			{
				StringBuilder keyCode = new StringBuilder(key);
				keyCode.append(rawCode);
				return md.digest(keyCode.toString().getBytes());
			}
			
			public StringBuilder genEncryptCodeDigest(StringBuilder rawCode, int digestLength)
			{
				byte[] encryptCode = genEncryptCode(rawCode);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < digestLength; ++i)
				{
					sb.append(CodeChars.getByteChar(encryptCode[i]));
				}
				return sb;
			}
		}
		
		
		private static final int[][] SEQ_TBL = 
		{
			{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 2, 1, 3}, {0, 2, 3, 1}, {0, 3, 1, 2}, {0, 3, 2, 1},
			{1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0},
			{2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 0, 1}, {2, 3, 1, 0},
			{3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0},
		};
		
		private static final int CODE_SEGMENT_COUNT = 4;
		private static final int CODE_SEGMENT_LENGTH = 4;
		
		private static final int BATCH_CODE_LENGTH = 3;
		private static final String INIT_BATCH_CODE = "I3K";
		private static final int BATCH_ID_OFFSET = getInitBatchCodeOffset();
		
		private static final String BATCH_ID_PROPERTY_NAME = "batchId";
		private static final String KEY_NAME_PREFIX_IN_PROPERTIES = "key_";
		private static final String SEPARATOR_CHAR_IN_PROPERTIES = "_";
		
		private static final String PROPERTIES_FILE_NAME_PREFIX = "cdkey_";
	    private static final String PROPERTIES_FILE_SEPARATOR_CHAR = "_";
	    private static final String PROPERTIES_FILE_EXTENSION_NAME = ".properties";
	    private static final String CDKEY_FILENAME_PREFIX = "key-";
	    private static final String CDKEY_FILENAME_SEPARATOR_CHAR = "_";
	    private static final String CDKEY_FILE_EXTENSION_NAME = ".txt";
		
	    EncryptKeyCode encryptKeyCode = new EncryptKeyCode();
		public CDKeyGen()
		{
			
		}
		
		private static int getInitBatchCodeOffset() 
		{
	    	int offset = 0;
	    	for (int i = 0; i < INIT_BATCH_CODE.length(); ++i) 
	    	{
	    		offset = Character.getNumericValue(INIT_BATCH_CODE.charAt(i)) + offset*CodeChars.getCharsCount();
	    	}
	    	return offset;
	    }
		
		private static String genBatchCode(int batchId) 
		{
	    	int offsetBatchId = BATCH_ID_OFFSET + batchId;
	    	int batchCodeCount = 1;
	    	for (int i = 0; i < BATCH_CODE_LENGTH; ++i)
	    		batchCodeCount *= CodeChars.getCharsCount();
	    	offsetBatchId %= batchCodeCount;
	    	char[] code = new char[BATCH_CODE_LENGTH];
	    	for (int i = 0; i < BATCH_CODE_LENGTH; ++i) 
	    	{
	    		int radix = 1;
	    		for (int j = 0; j < BATCH_CODE_LENGTH-1-i; ++j)
	    			radix *= CodeChars.getCharsCount();
	    		int index = offsetBatchId/radix%CodeChars.getCharsCount();
	    		code[i] = CodeChars.getChar(index);
	    	}
	    	return new String(code);
	    }
		
		private static int calcBatchID(String batchCode) 
		{
	    	int batchId = -1;
	    	int batchIdOffset = 0;
    		for (int i = 0; i < batchCode.length(); ++i) 
    		{
    			batchIdOffset = Character.getNumericValue(batchCode.charAt(i)) + batchIdOffset*CodeChars.getCharsCount();
        	}
    		int batchCodeMax = 1;
        	for (int i = 0; i < batchCode.length(); ++i)
        		batchCodeMax *= CodeChars.getCharsCount();
        	batchId = (batchCodeMax + batchIdOffset - BATCH_ID_OFFSET)%batchCodeMax;
	    	return batchId;
	    }
		
		private static char genGenCode(int genID)
		{
			return CodeChars.getChar(genID);
		}
		
		private static int calcGenID(char genCode)
		{
			return Character.getNumericValue(genCode);
		}
		
		static int getBatchID(String code)
		{
			return calcBatchID(code.substring(0, BATCH_CODE_LENGTH));
		}
		
		static int getGenID(String code)
		{
			return calcGenID(code.charAt(BATCH_CODE_LENGTH));
		}
		
		static String genBatchGenCode(int bid, int gid)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(genBatchCode(bid));
			sb.append(genGenCode(gid));
			return sb.toString();
		}
		
		static int getSumMod(char[] code, int m)
		{
			int sum = 0;
			for (int i = 0; i < code.length; ++i)
			{
				sum += Character.getNumericValue(code[i]);
			}
			return sum%m;
		}
		
		static int getSum(StringBuilder code)
		{
			int sum = 0;
			for (int i = 0; i < code.length(); ++i)
			{
				sum += Character.getNumericValue(code.charAt(i));
			}
			return sum;
		}
		
		static int getSumMod(StringBuilder code, int m)
		{
			int sum = getSum(code);
			return sum%m;
		}
		
		static int getSumMod(StringBuilder[] code, int m)
		{
			int sum = 0;
			for (int i = 0; i < code.length; ++i)
			{
				sum += getSum(code[i]);
			}
			return sum%m;
		}
		
		static StringBuilder shuffleCodeGen(StringBuilder rawCode)
		{
			int seqIndex = getSumMod(rawCode, SEQ_TBL.length);
			int[] seqtbl = SEQ_TBL[seqIndex];
			StringBuilder shuffleCode = new StringBuilder();
			shuffleCode.append(rawCode);
			for (int i = 0; i < shuffleCode.length(); ++i)
			{
				int index = seqtbl[i];
				shuffleCode.setCharAt(index, rawCode.charAt(i));
			}
			return shuffleCode;
		}
		
		static StringBuilder shuffleCodeRevert(StringBuilder shuffleCode)
		{
			int seqIndex = getSumMod(shuffleCode, SEQ_TBL.length);
			int[] seqtbl = SEQ_TBL[seqIndex];
			StringBuilder rawCode = new StringBuilder();
			for (int i = 0; i < shuffleCode.length(); ++i)
			{
				int index = seqtbl[i];
				rawCode.append(shuffleCode.charAt(index));
			}
			return rawCode;
		}
		
		static StringBuilder[] shuffleCodeGen(StringBuilder[] rawCodes)
		{
			int seqIndex = getSumMod(rawCodes, SEQ_TBL.length);
			int[] seqtbl = SEQ_TBL[seqIndex];
			StringBuilder[] shuffleCodes = new StringBuilder[rawCodes.length];
			for (int i = 0; i < shuffleCodes.length; ++i)
			{
				shuffleCodes[i] = new StringBuilder();
				shuffleCodes[i].append(rawCodes[i]);
			}
			for (int i = 0; i < shuffleCodes.length; ++i)
			{
				int index = seqtbl[i];
				StringBuilder code = rawCodes[i];
				for (int j = 0; j < code.length(); ++j)
				{
					shuffleCodes[j].setCharAt(index, code.charAt(j));
				}
			}
			return shuffleCodes;
		}
		
		static StringBuilder[] shuffleCodeRevert(StringBuilder[] shuffleCodes)
		{
			int seqIndex = getSumMod(shuffleCodes, SEQ_TBL.length);
			int[] seqtbl = SEQ_TBL[seqIndex];
			StringBuilder[] rawCodes = new StringBuilder[shuffleCodes.length];
			for (int i = 0; i < rawCodes.length; ++i)
			{
				rawCodes[i] = new StringBuilder();
				int index = seqtbl[i];
				for (int j = 0; j < shuffleCodes.length; ++j)
				{
					rawCodes[i].append(shuffleCodes[j].charAt(index));
				}
			}
			return rawCodes;
		}
		
		static String shuffleCode(StringBuilder[] rawCodes)
		{
			StringBuilder[] shuffleCodes = new StringBuilder[rawCodes.length];
			for (int i = 0; i < shuffleCodes.length; ++i)
			{
				shuffleCodes[i] = shuffleCodeGen(rawCodes[i]);
			}
			shuffleCodes = shuffleCodeGen(shuffleCodes);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < shuffleCodes.length; ++i)
			{
				sb.append(shuffleCodes[i]);
			}
			return sb.toString();
		}
		
		static StringBuilder[] revertCode(String codes)
		{
			StringBuilder[] shuffleCodes = new StringBuilder[CODE_SEGMENT_COUNT];
			for (int i = 0; i < shuffleCodes.length; ++i)
			{
				shuffleCodes[i] = new StringBuilder(codes.substring(i*CODE_SEGMENT_LENGTH, (i+1)*CODE_SEGMENT_LENGTH));
			}
			StringBuilder[] rawCodes = shuffleCodeRevert(shuffleCodes);
			for (int i = 0; i < rawCodes.length; ++i)
			{
				rawCodes[i] = shuffleCodeRevert(rawCodes[i]);
			}
			return rawCodes;
		}
		
		static String getCodeBatch(EncryptKeyCode ekc, String code)
		{
			String batchCode = null;
			StringBuilder[] rawCodes = revertCode(code);
			StringBuilder rawCode = new StringBuilder();
			for (int i = 0; i < rawCodes.length-1; ++i)
			{
				rawCode.append(rawCodes[i]);
			}
			StringBuilder checkCode = ekc.genEncryptCodeDigest(rawCode, CODE_SEGMENT_LENGTH);
			if (checkCode.toString().equals(rawCodes[rawCodes.length-1].toString()))
			{
				batchCode = rawCodes[0].toString();
			}
			return batchCode;
		}
		
		static boolean checkCodeFormatValid(String code)
		{
			if (code.length() != CODE_SEGMENT_COUNT*CODE_SEGMENT_LENGTH)
				return false;
			for (int i = 0; i < code.length(); ++i)
			{
				char c = code.charAt(i);
				if (!CodeChars.isCharValid(c))
					return false;
			}
			return true;
		}
		
		public static class CDKeyIDInfo
		{
			public int batchID = -1;
			public int genID = -1;
		}
		static CDKeyIDInfo getCodeIDInfo(EncryptKeyCode ekc, String code)
		{
			CDKeyIDInfo info = new CDKeyIDInfo();
			if (checkCodeFormatValid(code))
			{
				String batchCode = getCodeBatch(ekc, code);
				if (batchCode != null)
				{
					info.batchID = getBatchID(batchCode);
					info.genID = getGenID(batchCode);
				}	
			}
			return info;
		}
		
		public  CDKeyIDInfo getCodeIDInfo(String code)
		{
			code = code.toUpperCase();
			return getCodeIDInfo(encryptKeyCode, code);
		}
		
		static boolean isCodeValid(String code)
		{
			return getCodeBatch(new EncryptKeyCode(), code) != null;
		}
		
		static String genCode(String batchCode, RandomCodeGen rcg, EncryptKeyCode ekc)
		{
			StringBuilder[] rawCodes = new StringBuilder[CODE_SEGMENT_COUNT];
			int index = 0;
			rawCodes[index++] = new StringBuilder(batchCode);
			rawCodes[index++] = rcg.genNext(CODE_SEGMENT_LENGTH);
			rawCodes[index++] = rcg.genNext(CODE_SEGMENT_LENGTH);
			StringBuilder codes = new StringBuilder();
			for (int i = 0; i < rawCodes.length-1; ++i)
			{
				codes.append(rawCodes[i]);
			}
			rawCodes[index++] = ekc.genEncryptCodeDigest(codes, CODE_SEGMENT_LENGTH);
			return shuffleCode(rawCodes);
		}
		
		static private List<String> genCodes(String batchCode, int count)
		{
			List<String> codes = new ArrayList<String>();
			RandomCodeGen rcg = new RandomCodeGen();
			EncryptKeyCode ekc = new EncryptKeyCode();
			for (int i = 0; i < count; ++i)
			{
				codes.add(genCode(batchCode, rcg, ekc));
			}
			return codes;
		}
		
		static List<String> genCodes(int batchId, int genId, int count)
		{
			String batchCode = genBatchGenCode(batchId, genId);
			return genCodes(batchCode, count);
		}
		
		static class BatchGenInfo
		{
			int genId;
			Map<Integer, String> keys = new TreeMap<Integer, String>();
			BatchGenInfo(int id)
			{
				genId = id;
			}
		}
	    static class BatchCDKeys 
	    {
	    	int batchId;
	    	Map<Integer, BatchGenInfo> genInfo = new TreeMap<Integer, BatchGenInfo>();
	    }
		private static BatchCDKeys loadKeys(String propertiesFileName) 
		{
	    	BatchCDKeys batchCDKeys = new BatchCDKeys();
			try 
			{
	            InputStream in = new BufferedInputStream(new FileInputStream(propertiesFileName));
	            Properties properties = new Properties();
	            System.out.println("load properties file : " + propertiesFileName);
	            properties.load(in);
	            in.close();
	            {
	            	java.util.Enumeration<?> en = properties.propertyNames();
	                while (en.hasMoreElements()) 
	                {
	                    String key = (String)en.nextElement();
	                    String value = properties.getProperty(key);
	                    if (key.equals(BATCH_ID_PROPERTY_NAME)) 
	                    {
	                    	batchCDKeys.batchId = Integer.parseInt(value);
	                    }
	                    else if (key.startsWith(KEY_NAME_PREFIX_IN_PROPERTIES)) 
	                    {
	                    	int index = key.indexOf(SEPARATOR_CHAR_IN_PROPERTIES, KEY_NAME_PREFIX_IN_PROPERTIES.length());
	                    	if (index > KEY_NAME_PREFIX_IN_PROPERTIES.length() && key.length() > index+1) 
	                    	{
	                    		String genIdStr = key.substring(KEY_NAME_PREFIX_IN_PROPERTIES.length(), index);
	                    		String keyIdStr = key.substring(index+1);
	                    		int genId = Integer.parseInt(genIdStr);
	                    		int keyId = Integer.parseInt(keyIdStr);
	                    		BatchGenInfo genInfo = batchCDKeys.genInfo.get(genId);
	                    		if (genInfo == null)
	                    		{
	                    			genInfo = new BatchGenInfo(genId);
	                    			batchCDKeys.genInfo.put(genId, genInfo);
	                    		}
	                    		genInfo.keys.put(keyId, value);
	                    	}
	                    }
	                    else
	                    {
	                    	System.out.println("find cdkey property, but key name invalid: key=" + key + ", value=" + value + "!!!!!!!!!!!!!!!!!!!");
	                    	System.exit(-1);
	                    }
	                }
	            }
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
			return batchCDKeys;
		}
		
		private static void saveProperties(BatchCDKeys batchCdkeys, String dir) 
		{
	    	try 
	    	{
	        	Properties properties = new Properties();
	        	properties.setProperty(BATCH_ID_PROPERTY_NAME, String.valueOf(batchCdkeys.batchId));
	        	for (Map.Entry<Integer, BatchGenInfo> e : batchCdkeys.genInfo.entrySet()) 
	        	{
	        		int genId = e.getKey();
	        		System.out.println("......save batch gen info properties: batchId=" + batchCdkeys.batchId + ", genId=" + genId);
	        		BatchGenInfo genInfo = e.getValue();
	                System.out.println("......save batch gen cdkey properties: batchId=" + batchCdkeys.batchId + ", genId=" + genId + ", keyCount=" + genInfo.keys.size());
	                for (Map.Entry<Integer, String> keyentry : genInfo.keys.entrySet()) 
	                {
	                	int seqNo = keyentry.getKey();
	                	String cdkey = keyentry.getValue();
	                	String propertyName = KEY_NAME_PREFIX_IN_PROPERTIES + genId + SEPARATOR_CHAR_IN_PROPERTIES + seqNo;
	                	properties.setProperty(propertyName, cdkey);
	                }
	        	}
	        	String propertiesFileName = dir + File.separator + getPropertiesFIleNameFromBatchId(batchCdkeys.batchId);
	            FileOutputStream out = new FileOutputStream(propertiesFileName);
	            properties.store(out, "");
	            out.close();
	            System.out.println("save properties file : " + propertiesFileName);
	        } 
	    	catch (Exception e) 
	    	{
	            e.printStackTrace();
	        } 
	    	finally 
	    	{
	        }
	    }
		
		private static void saveGenCDkeyResult(int batchId, BatchGenInfo genInfo, String dir) 
		{
	    	try 
	    	{
	    		String cdKeyResultFileName = dir + File.separator +  getResultFileName(batchId, genInfo.genId);
	        	Writer resultwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cdKeyResultFileName), "UTF-8"));
	        	
	        	for (Map.Entry<Integer, String> keyentry : genInfo.keys.entrySet()) 
	        	{
	        		String cdkey = keyentry.getValue();
	                resultwriter.write(cdkey+"\r\n");
	        	}
	            resultwriter.flush();
	            resultwriter.close();
	            System.out.println("save result file : " + cdKeyResultFileName);
	        } 
	    	catch (Exception e) 
	    	{
	            e.printStackTrace();
	        } 
	    	finally 
	    	{
	        }
	    }
		
	    private static String getPropertiesFIleNameFromBatchId(int batchid) 
	    {
	    	return PROPERTIES_FILE_NAME_PREFIX + batchid + PROPERTIES_FILE_EXTENSION_NAME;
	    }
	    
	    private static String getResultFileName(int batchId, int genId) 
	    {
	    	return CDKEY_FILENAME_PREFIX  + batchId + CDKEY_FILENAME_SEPARATOR_CHAR + genId + CDKEY_FILE_EXTENSION_NAME;
	    }
	    
	    private static BatchCDKeys getExistBatchCDKeys(int batchid, String dir) 
	    {
	    	BatchCDKeys batchCdkeys = new BatchCDKeys();
	    	batchCdkeys.batchId = batchid;
	        String fileName = dir + File.separator + getPropertiesFIleNameFromBatchId(batchid);
	        File file = new File(fileName);
	        if (file.exists())
	        {
	        	batchCdkeys = loadKeys(fileName);
	        }else 
	        {
	        	System.out.println("batch " + batchid + " properties file(" + fileName + ") not exist...");
	        }
	        return batchCdkeys;
	    }
	    
	    private static void createNextGenCDKey(int count, BatchCDKeys batchCdkeys, String dir) 
	    {
	    	int newGenId = batchCdkeys.genInfo.size() + 1;
	    	System.out.println("genInfo : batchId="+batchCdkeys.batchId+", batchGenId="+newGenId);
	    	List<String> cdkeys = genCodes(batchCdkeys.batchId, newGenId, count);
	    	BatchGenInfo batchGenInfo = new BatchGenInfo(newGenId);
	    	int seq = 0;
	    	for (String key : cdkeys)
	    	{
	    		batchGenInfo.keys.put(++seq, key);
	    	}
	    	batchCdkeys.genInfo.put(newGenId, batchGenInfo);
	    	saveProperties(batchCdkeys, dir);
	    	saveGenCDkeyResult(batchCdkeys.batchId, batchGenInfo, dir);
	    }
	    
	    public static void genCDKeys(int batchID, int count, String dir)
	    {
	    	batchID = batchID >= 0 ? batchID : getNextBatchId(dir);
	    	createNextGenCDKey(count, getExistBatchCDKeys(batchID, dir), dir);
	    }
	    
	    private static class BatchCDKeyFileFilter implements FilenameFilter 
	    {
	    	public BatchCDKeyFileFilter() 
	    	{
	    		
	    	}
	    	
	    	public boolean accept(File dir, String name) 
	    	{
	    		if (name.startsWith(PROPERTIES_FILE_NAME_PREFIX) && name.endsWith(PROPERTIES_FILE_EXTENSION_NAME)) 
	    		{
	    			String batchIdStr = name.substring(PROPERTIES_FILE_NAME_PREFIX.length(), name.length() - PROPERTIES_FILE_EXTENSION_NAME.length());
	    			for (int i = 0; i < batchIdStr.length(); ++i)
	    			{
	    				if (!Character.isDigit(batchIdStr.charAt(i)))
	    					return false;	
	    			}
	    			return true;
	    		}
	    		return false;
	    	}
	    }
	    
	    private static String[] getAllBatchCDKeyFileNames(String dir) 
	    {
	    	File curfile = new File(dir);
	    	String[] files = curfile.list(new BatchCDKeyFileFilter());
	    	return files;
	    }
	    
	    private static int getNextBatchId(String dir) 
	    {
	    	int maxBatchId = 0;
	    	String[] allBatchCDKeyFileNames = getAllBatchCDKeyFileNames(dir);
	    	for (String name : allBatchCDKeyFileNames) 
	    	{
	    		String batchIdStr = name.substring(PROPERTIES_FILE_NAME_PREFIX.length(), name.length() - PROPERTIES_FILE_EXTENSION_NAME.length());
				int batchid = Integer.parseInt(batchIdStr);
				if (batchid > maxBatchId)
					maxBatchId = batchid;
	    	}
	    	return maxBatchId + 1;
	    }
		
		public static void main(String[] args)
		{
	    	int genCount = 1000;
	    	int batchId = -1;
	    	String dir = ".";
	    	for (String arg : args) 
	    	{
	    		if (arg.startsWith("--c=")) 
	    		{
	    			String str = arg.substring("--c=".length());
	    			genCount = Integer.parseInt(str);
	    		}
	    		else if (arg.startsWith("--b=")) 
	    		{
	    			String str = arg.substring("--b=".length());
	    			batchId = Integer.parseInt(str);
	    		}
	    		else if (arg.startsWith("--outdir="))
	    		{
	    			dir = arg.substring("--outdir=".length());
	    		}
	    	}
	    	genCDKeys(batchId, genCount, dir);
    		System.out.println("create cdkey end");
		}
		
		private void test()
		{
			System.out.println(Character.getNumericValue('a'));
			System.out.println(Character.getNumericValue('A'));
			List<String> codes = genCodes("i3kQ", 10);
			for (String code : codes)
			{
				System.out.println(code + ", " + isCodeValid(code));
			}
			try
			{
				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
				byte[] m = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
				byte[] d = md.digest(m);
				for (int i = 0; i < d.length; ++i)
				{
					System.out.print(d[i] + ", ");
				}
				System.out.println();
				System.out.println(d.length);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				byte[] ks = {117, -37, 79, 107, -52, 5, -89, -127,}; 
				java.security.Key key = new javax.crypto.spec.SecretKeySpec(ks, "DES");
				byte[] m = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
				javax.crypto.Cipher encryptCipher = javax.crypto.Cipher.getInstance("DES");
				encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
				byte[] d = encryptCipher.doFinal(m);
				for (int i = 0; i < d.length; ++i)
				{
					System.out.print(d[i] + ", ");
				}
				System.out.println();
				System.out.println(d.length);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	








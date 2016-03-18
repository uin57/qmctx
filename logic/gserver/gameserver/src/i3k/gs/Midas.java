
package i3k.gs;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import sun.misc.BASE64Encoder;

import org.json.JSONArray;
import org.json.JSONObject;


//import ket.util.ArgsMap;

import javax.crypto.Mac;  
import javax.crypto.spec.SecretKeySpec;

class HMACSHA1 {  
  
    private static final String HMAC_SHA1 = "HmacSHA1";  
  
    /** 
     * 生成签名数据 
     *  
     * @param data 待加密的数据 
     * @param key  加密使用的key 
     * @return 生成MD5编码的字符串  
     * @throws InvalidKeyException 
     * @throws NoSuchAlgorithmException 
     */  
    public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {  
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);  
        Mac mac = Mac.getInstance(HMAC_SHA1);  
        mac.init(signingKey);
        return new BASE64Encoder().encode(mac.doFinal(data));  
    }  
    
    public static String get(String key, String src)
    {
    	try
    	{
    		return getSignature(src.getBytes("UTF-8"), key.getBytes("UTF-8"));
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return null;
    	
    }
}  


class HttpUtil
{

	public static final int CONNECT_TIMEOUT = 5 * 1000;
	public static final int READ_TIMEOUT = 5 * 1000;
	
	public static String httpGet(String strUrl, String sid, String stype, String path, GameServer gs)
	{
		HttpURLConnection conn=null;
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			
			//
			String cookie = "session_id=" + sid + ";session_type=" + stype + ";org_loc=" + path;
			gs.getLogger().debug("cookie=" + cookie);
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("Connection", "Keep-Alive");
						
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.connect();
			
			return recv(conn);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn!=null)
				conn.disconnect();
		}
		return null;
	}
	
	public static String recv(HttpURLConnection conn)
	{
		InputStream is=null;
		try {
			is = conn.getInputStream();
			int pageSize = 256;
			int readNum = 0;
			String response = new String();
			byte[] data = new byte[pageSize];
			do {
				readNum = is.read(data,0,pageSize);
				if (readNum>0)
				{
					response += new String(data,0,readNum, "UTF-8");
				}
			} while (readNum<pageSize && readNum>0);
			//parse json
			return response;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				if (is!=null)
					is.close();
			}
			catch (Exception e) { }
		}
	}
}

public class Midas
{
	
	public static class UserInfo
	{
		public UserInfo()
		{
			
		}
		
		public UserInfo(String openID, String openKey, String payToken, String pf, String pfKey, String cookieSID, String cookieSType)
		{
			this.openID = openID;
			this.openKey = openKey;
			this.payToken = payToken;
			this.pf = pf;
			this.pfKey = pfKey;
			this.cookieSID = cookieSID;
			this.cookieSType = cookieSType;
		}
		
		public String openID = "4C7B9D7F3214BD3D23C088E37B26B314";
		public String openKey = "EC3770C26313C12DE2098BF56A91A799";
		public String payToken = "C981E11E3F68251C70492900480FC650";		
		public String pf = "desktop_m_qq-73213123-iap-9528-qq-1000001472-4C7B9D7F3214BD3D23C088E37B26B314";
		public String pfKey = "6a42e17ed1d1e1868d952b8a05dde0df";
		public String cookieSID = "";
		public String cookieSType = "";
	}
	
	public static class BalanceResult
	{		
		public BalanceResult(int errCode)
		{
			this.errCode = errCode;
		}
		
		public int errCode;
		public int ret;
		public int balance;
		public int gen_balance;
		public int first_save;
		public int save_amt;
	}
	
	public static interface GetBalanceCallback
	{
		public void onCallback(UserInfo uinfo, BalanceResult res);
	}
	
	public interface Task
	{
		public static final int ERR_BADKEY = -5;
		public static final int ERR_CLOSE = -4;
		public static final int ERR_PARSE = -3;
		public static final int ERR_RECV = -2;
		public static final int ERR_CONN = -1;
		public static final int ERR_OK = 0;
		
		public void runHTTP();
		public void runCallback();
	}
	
	public Midas(GameServer gs)
	{
		this.gs = gs;
		if( gs.getConfig().httpThreadCount <= 0 )
			executorHTTP = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("MidasHTTP");
		else
			executorHTTP = ket.util.ExecutorTool.newScheduledThreadPool(gs.getConfig().httpThreadCount, "MidasHTTP");
			
		if( gs.getConfig().httpCallbackThreadCount <= 0 )
			executorCallback = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("MidasCallback");
		else
			executorCallback = ket.util.ExecutorTool.newScheduledThreadPool(gs.getConfig().httpCallbackThreadCount, "MidasCallback");
	}
	
	public void start()
	{
	}
	
	public int getHTTPTaskQueueSize()
	{
		return taskCountHTTP.get();
	}
	
	public int getCallbackTaskQueueSize()
	{
		return taskCountCallback.get();
	}
	
	public int getHTTPRejectedTaskQueueSize()
	{
		return taskRejectCountHTTP.get();
	}
	
	public int getCallbackRejectedTaskQueueSize()
	{
		return taskRejectCountCallback.get();
	}
		
	void exec(final Task task, final boolean bCallback)
	{
		try
		{
			taskCountHTTP.incrementAndGet();
			executorHTTP.execute(new Runnable()
			{
				@Override
				public void run()
				{
					task.runHTTP();
					if( bCallback )
					{
						try
						{
							taskCountCallback.incrementAndGet();
							executorCallback.execute(new Runnable()
							{
								@Override
								public void run()
								{
									task.runCallback();
									taskCountCallback.decrementAndGet();
								}					
							});
						}
						catch(RejectedExecutionException rex)
						{
							taskRejectCountCallback.incrementAndGet();
							taskCountCallback.decrementAndGet();
							task.runCallback();
						}
					}
					taskCountHTTP.decrementAndGet();
				}			
			});
		}
		catch(RejectedExecutionException rex)
		{
			taskRejectCountHTTP.incrementAndGet();
			taskCountHTTP.decrementAndGet();
			if( bCallback )
				task.runCallback();
		}
	}
	
	public void destroy()
	{
		executorHTTP.shutdown();
		try
		{
			boolean bFinish = false;
			for(int i = 0; i < 3; ++i)
			{
				bFinish = executorHTTP.awaitTermination(1, TimeUnit.SECONDS);
				if( bFinish )
				{
					break;
				}
			}
			if( ! bFinish )
			{
				gs.getLogger().warn("midas http shutdownNow, size=" + taskCountHTTP);
				executorHTTP.shutdownNow();	
			}
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
		executorCallback.shutdown();
		try
		{
			boolean bFinish = false;
			for(int i = 0; i < 3; ++i)
			{
				bFinish = executorCallback.awaitTermination(1, TimeUnit.SECONDS);
				if( bFinish )
				{
					break;
				}
			}
			if( ! bFinish )
			{
				gs.getLogger().warn("midas httpcallback shutdownNow, size=" + taskCountCallback);
				executorHTTP.shutdownNow();	
			}
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
	}	
	
	public static interface LoginVerifyCallback
	{
		public void onCallback(int sid, byte err, String userName, String id);
	}
	
	public void loginVerify(final UserInfo uinfo, final String userName, final String ip, final int sid, final LoginVerifyCallback callback)
	{
		if( uinfo.openID == null || uinfo.openKey == null || uinfo.payToken == null || uinfo.openKey.equals("") )
		{
			callback.onCallback(sid, (byte)1, userName, "");
			return;
		}
		gs.getLogger().debug("loginVerify, userName=" + userName.toLowerCase() + ",openKey=" + uinfo.openKey);
		
		final boolean bQQ = GameData.getInstance().isQQGameServerID(gs.getConfig().id);
		final boolean bGuest = GameData.getInstance().isGuestOpenID(uinfo.openID);
		final String appid = gs.getGameAppID(bGuest);
		final String appkey = gs.getConfig().gameappKey;
		
		final String authCode = bGuest ? uinfo.payToken : uinfo.openKey;
		final String openID = uinfo.openID;
		
		exec(new Task()
		{	
			
			@Override
			public void runHTTP()
			{
				
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String timestamp = sdf.format(new Date());
					String sig = md5(appkey + timestamp);
					
					String params = "";
					String url = "";
					if( bGuest )
					{
						//guest
						params = "{\"accessToken\":\"" + authCode + "\",\"guestid\":\"" + openID + "\"}";
						url = gs.getConfig().msdkURL + "/auth/guest_check_token/?timestamp=" + timestamp + 
							"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";
					}
					else if( bQQ )
					{
						//qq
						params = "{\"appid\":" + appid + ",\"openid\":\"" + openID + "\",\"openkey\":\"" + authCode + "\",\"userip\":\"" + ip + "\"}";
						url = gs.getConfig().msdkURL + "/auth/verify_login?timestamp=" + timestamp + 
							"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";
					}
					else
					{
						//wx
						params = "{\"accessToken\":\"" + authCode + "\",\"openid\":\"" + openID + "\"}";
						url = gs.getConfig().msdkURL + "/auth/check_token?timestamp=" + timestamp + 
							"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";
					}
					gs.getLogger().debug("loginVerify, url=" + url + ",params=" + params);
					
					String retString = HttpUtils.readContentFromPost(url, params, 5000, 5000);
					
					JSONObject jo = new JSONObject(retString); 
					if (jo.getString("ret").equals("0"))
						resultNum = 1;
					else
					{
						resultNum = -2;
					}
					retMsg = jo.getString("msg");
					if( resultNum != 1 )
						gs.getLogger().warn("loginVerify failed, url=" + url + ",params=" + params + ",retMsg=" + retMsg);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}

			@Override
			public void runCallback()
			{
				if(resultNum==1)
				{
					callback.onCallback(sid, (byte)0, userName.toLowerCase(), authCode);
					gs.getLogger().debug("login verify succ, openID=" + uinfo.openID + ",n=" + resultNum + ",msg=" + retMsg);
				}
				else
				{
					callback.onCallback(sid, (byte)1, userName, "");
					gs.getLogger().warn("login verify failed, openID=" + uinfo.openID + ",n=" + resultNum + ",msg=" + retMsg);
				}
			}
			
			int resultNum = 0;
			String retMsg = "";
		}, true);
	}
	
	public static interface QueryQQVIPCallback
	{
		// res 2: SUPER 1: Y 0: N -1: unknown
		public void onCallback(int rid, byte res);
	}
	
	public void queryQQVIP(final String openID, final int rid, final QueryQQVIPCallback callback)
	{
		if( openID == null || rid <= 0 || openID.equals("") )
			return;
		
		gs.getLogger().debug("query qq vip, openID=" + openID + ", rid= " + rid);
		
		final boolean bQQ = GameData.getInstance().isQQGameServerID(gs.getConfig().id);
		final boolean bGuest = GameData.getInstance().isGuestOpenID(openID);		
		if( ! bQQ || bGuest )
			return;
		
		final String appid = gs.getGameAppID(bGuest);
		final String appkey = gs.getConfig().gameappKey;
		
		exec(new Task()
		{	
			
			@Override
			public void runHTTP()
			{
				
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String timestamp = sdf.format(new Date());
					String sig = md5(appkey + timestamp);
					
					String params = "";
					String url = "";
					
					params = "{\"appid\":" + appid
							+ ",\"login\":2"
							+ ",\"uin\":0"
							+ ", \"openid\":\"" + openID + "\""
							+ ",\"vip\":17"
							+ "}";
					url = gs.getConfig().msdkURL + "/profile/load_vip/?timestamp=" + timestamp + 
						"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";
					gs.getLogger().debug("query qq vip, url=" + url + ",params=" + params);
					
					String retString = HttpUtils.readContentFromPost(url, params, 5000, 5000);
					
					JSONObject jo = new JSONObject(retString); 
					if (jo.getString("ret").equals("0"))
					{
						boolean bIsNormalVIP = false;
						boolean bIsSuperVIP = false;
						// 抓取是否会员
						JSONArray lst = jo.getJSONArray("lists");
						for(int i = 0; i < lst.length(); ++i)
						{
							JSONObject e = lst.getJSONObject(i);
							if( e.getString("flag").equals("1") )
							{
								if( e.getString("isvip").equals("1") )
								{
									//res = 1;
									bIsNormalVIP = true;
								}
								//else
								//	res = 0;
								//break;
							}
							else if( e.getString("flag").equals("16") )
							{
								if( e.getString("isvip").equals("1") )
								{
									//res = 2;
									bIsSuperVIP = true;
								}
								//else
								//	res = 0;
								//break;
							}
						}
						if( bIsSuperVIP )
							res = 2;
						else if( bIsNormalVIP )
							res = 1;
						else
							res = 0;
					}
					retMsg = jo.getString("msg");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}

			@Override
			public void runCallback()
			{
				if( res > -1 )
				{
					callback.onCallback(rid, res);
					gs.getLogger().debug("query qq vip succ, openID=" + openID + ", res=" + res + ",msg=" + retMsg);
				}
				else
				{
					callback.onCallback(rid, res);
					gs.getLogger().debug("query qq vip failed, openID=" + openID + ", res=" + res + ",msg=" + retMsg);
				}
			}
			
			byte res = -1;
			String retMsg = "";
		}, true);
	}
	
	// TODO
	public void reportScore(final String openID, final String openKey, final int lvl, final int score)
	{
		if( openID == null || openID.equals("") )
			return;
				
		final boolean bQQ = GameData.getInstance().isQQGameServerID(gs.getConfig().id);
		final boolean bGuest = GameData.getInstance().isGuestOpenID(openID);		
		if( bGuest )
			return;		

		gs.getLogger().debug("report score, openID=" + openID + ", lvl= " + lvl + ", score= " + score);
		
		final String appid = gs.getGameAppID(bGuest);
		final String appkey = gs.getConfig().gameappKey;
		
		exec(new Task()
		{	
			
			@Override
			public void runHTTP()
			{
				
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String timestamp = sdf.format(new Date());
					String sig = md5(appkey + timestamp);
					
					String params = "";
					String url = "";
					
					if( bQQ )
					{
						params = "{\"appid\":\"" + appid + "\""
								+ ",\"accessToken\":\"" + openKey + "\""
								+ ", \"openid\":\"" + openID + "\""
								+ ",\"param\":"
								+ "[{\"type\": 1,\"bcover\": 0,\"data\": \"" + lvl + "\",\"expires\": \"0\"},"
								+ "{\"type\": 3,\"bcover\": 0,\"data\": \"" + score + "\",\"expires\": \"0\"}]"
								+ "}";
						
						url = gs.getConfig().msdkURL + "/profile/qqscore_batch/?timestamp=" + timestamp + 
								"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";												
					}
					else
					{
						params = "{\"appid\":\"" + appid + "\""
								+ ",\"grantType\":\"client_credential\""
								+ ", \"openid\":\"" + openID + "\""
								+ ",\"score\":\"" + score + "\""
								+ ",\"expires\":\"0\""
								+ "}";
						
						url = gs.getConfig().msdkURL + "/profile/wxscore/?timestamp=" + timestamp + 
								"&appid=" + appid + "&sig=" + sig + "&openid=" + openID + "&encode=1";
					}
					gs.getLogger().debug("report score, url=" + url + ",params=" + params);
					
					String retString = HttpUtils.readContentFromPost(url, params, 5000, 5000);
					
					JSONObject jo = new JSONObject(retString); 
					if (jo.getString("ret").equals("0"))
					{
						res = 0;
					}					
					retMsg = jo.getString("msg");
					if( res != 0 )
						gs.getLogger().warn("report failed, url=" + url + ",params=" + params + ",retMsg=" + retMsg);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}

			@Override
			public void runCallback()
			{
			}
			
			byte res = -1;
			String retMsg = "";
		}, false);
	}
	
	private String md5(String sourceStr){
		String signStr = "";
		try {
			byte[] bytes = sourceStr.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5"); md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if(md5Byte != null){
				signStr = HexBin.encode(md5Byte); }
		} catch (NoSuchAlgorithmException e) { e.printStackTrace();
		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		}
		return signStr.toLowerCase();
	}
	
	public void getBalance(final UserInfo uinfo, final GetBalanceCallback callback, final Integer gsIDTarget)
	{
		//if( true )
		//	return;
		if( uinfo.openKey.equals("") )
		{
			callback.onCallback(uinfo, new BalanceResult(Task.ERR_BADKEY));
			return;
		}
		exec(new Task()
		{			
			
			String getURL()
			{
				int zoneID = gs.getConfig().zoneID;
				if( gsIDTarget != null )
				{
					zoneID = gsIDTarget.intValue();
					if( zoneID == 21001 ) // TODO
						zoneID = 21000;
				}
				
				String s = "";
				s += "appid=" + gs.getConfig().offerID + "&";
				s += "openid=" + uinfo.openID + "&";
				s += "openkey=" + uinfo.openKey + "&";
				s += "pay_token=" + uinfo.payToken + "&";
				s += "pf=" + uinfo.pf + "&";
				s += "pfkey=" + uinfo.pfKey + "&";
				s += "ts=" + (int)(new Date().getTime()/1000) + "&";
				s += "zoneid=" + zoneID;
				
				return s;
			}
			
			@Override
			public void runHTTP()
			{
				final String path = "/mpay/get_balance_m";
				String infoURL = getURL();
				
				String key= gs.getConfig().offerKey + "&";
				String source = "";
				String sig = "";
				String httpReq = "";
				String httpRes = "";
				try
				{
					source = "GET&" + URLEncoder.encode(path, "UTF-8") + "&" + URLEncoder.encode(infoURL, "UTF-8");
					sig = URLEncoder.encode(HMACSHA1.get(key, source), "UTF-8");
						
					httpReq = gs.getConfig().midasURL + path + "?" + infoURL +"&sig=" + sig;
					gs.getLogger().debug("httpReq=" + httpReq);
				
					httpRes = HttpUtil.httpGet(httpReq, URLEncoder.encode(uinfo.cookieSID, "UTF-8"), 
							URLEncoder.encode(uinfo.cookieSType, "UTF-8"), URLEncoder.encode(path, "UTF-8"), gs);

				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				if( httpRes == null )
				{
					res.errCode = ERR_RECV;
					return;
				}
				gs.getLogger().debug("httpRes=" + httpRes);
				try
				{
					JSONObject jobj = new JSONObject(httpRes);
					res.ret = jobj.getInt("ret");
					if( res.ret == 0 )
					{
						res.save_amt = jobj.getInt("save_amt");
						res.balance = jobj.getInt("balance");
						res.gen_balance = jobj.getInt("gen_balance");
						res.first_save = jobj.getInt("first_save");
					}
					else
					{
						gs.getLogger().warn("midas getbalance failed, httpRes=" + httpRes + ", httpReq = " + httpReq);		
					}
					res.errCode = ERR_OK;
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					res.errCode = ERR_PARSE;
				}
								
			}

			@Override
			public void runCallback()
			{
				callback.onCallback(uinfo, res);
			}
			
			BalanceResult res = new BalanceResult(ERR_CONN);
		}, true);
	}
	
	public void present(final UserInfo uinfo, final int present)
	{
		if( getHTTPTaskQueueSize() >= gs.getConfig().httpTaskMaxCountL2 )
			return;
		gs.getLogger().debug("midas present:[" + uinfo.openID +"],+"+present);
		//if( true )
		//	return;
		if( uinfo.openKey.equals("") )
			return;
		exec(new Task()
		{			
			
			String getURL()
			{
				String s = "";
				s += "appid=" + gs.getConfig().offerID + "&";
				s += "discountid=" + gs.getConfig().payDiscountID + "&";
				s += "giftid=" + gs.getConfig().payGiftID + "&";
				s += "openid=" + uinfo.openID + "&";
				s += "openkey=" + uinfo.openKey + "&";
				s += "pay_token=" + uinfo.payToken + "&";
				s += "pf=" + uinfo.pf + "&";
				s += "pfkey=" + uinfo.pfKey + "&";
				s += "presenttimes=" + present + "&";
				s += "ts=" + (int)(new Date().getTime()/1000) + "&";
				s += "zoneid=" + gs.getConfig().zoneID;
				
				return s;
			}
			
			@Override
			public void runHTTP()
			{
				final String path = "/mpay/present_m";
				String infoURL = getURL();
				
				String key= gs.getConfig().offerKey + "&";
				String source = "";
				String sig = "";
				String httpReq = "";
				String httpRes = "";
				try
				{
					source = "GET&" + URLEncoder.encode(path, "UTF-8") + "&" + URLEncoder.encode(infoURL, "UTF-8");
					sig = URLEncoder.encode(HMACSHA1.get(key, source), "UTF-8");
						
					httpReq = gs.getConfig().midasURL + path + "?" + infoURL +"&sig=" + sig;
					//System.out.println("httpReq=" + httpReq);
				
					httpRes = HttpUtil.httpGet(httpReq, URLEncoder.encode(uinfo.cookieSID, "UTF-8"), 
							URLEncoder.encode(uinfo.cookieSType, "UTF-8"), URLEncoder.encode(path, "UTF-8"), gs);

				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				if( httpRes == null )
				{
					errCode = ERR_RECV;
					return;
				}
				gs.getLogger().debug("httpRes=" + httpRes);
				try
				{
					JSONObject jobj = new JSONObject(httpRes);
					int ret = jobj.getInt("ret");
					if( ret != 0 )
					{
						gs.getLogger().warn("midas present failed, httpRes= " + httpRes + ", httpReq= " + httpReq);
					}
					errCode = ERR_OK;
					gs.getLogger().debug("present errcode = " + errCode + ", ret = " + ret);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					errCode = ERR_PARSE;
				}
								
			}

			@Override
			public void runCallback()
			{
			}
			
			int errCode = ERR_CONN;
		}, false);
	}
	
	public void pay(final UserInfo uinfo, final int amt)
	{
		if( getHTTPTaskQueueSize() >= gs.getConfig().httpTaskMaxCountL2 )
			return;
		gs.getLogger().debug("midas pay:[" + uinfo.openID +"],-"+amt);
		//if( true )
		//	return;
		if( uinfo.openKey.equals("") )
			return;
		exec(new Task()
		{			
			
			String getURL()
			{
				String s = "";
				s += "amt=" + amt + "&";
				s += "appid=" + gs.getConfig().offerID + "&";
				s += "openid=" + uinfo.openID + "&";
				s += "openkey=" + uinfo.openKey + "&";
				s += "pay_token=" + uinfo.payToken + "&";
				s += "pf=" + uinfo.pf + "&";
				s += "pfkey=" + uinfo.pfKey + "&";
				s += "ts=" + (int)(new Date().getTime()/1000) + "&";
				s += "zoneid=" + gs.getConfig().zoneID;
				
				return s;
			}
			
			@Override
			public void runHTTP()
			{
				final String path = "/mpay/pay_m";
				String infoURL = getURL();
				
				String key= gs.getConfig().offerKey + "&";
				String source = "";
				String sig = "";
				String httpReq = "";
				String httpRes = "";
				try
				{
					source = "GET&" + URLEncoder.encode(path, "UTF-8") + "&" + URLEncoder.encode(infoURL, "UTF-8");
					sig = URLEncoder.encode(HMACSHA1.get(key, source), "UTF-8");
						
					httpReq = gs.getConfig().midasURL + path + "?" + infoURL +"&sig=" + sig;
					gs.getLogger().debug("httpReq=" + httpReq);
				
					httpRes = HttpUtil.httpGet(httpReq, URLEncoder.encode(uinfo.cookieSID, "UTF-8"), 
							URLEncoder.encode(uinfo.cookieSType, "UTF-8"), URLEncoder.encode(path, "UTF-8"), gs);

				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				if( httpRes == null )
				{
					errCode = ERR_RECV;
					return;
				}
				gs.getLogger().debug("httpRes=" + httpRes);
				try
				{
					JSONObject jobj = new JSONObject(httpRes);
					int ret = jobj.getInt("ret");
					int balance = -1;
					if( ret == 0 )
					{
						balance = jobj.getInt("balance");
					}
					else
					{
						gs.getLogger().warn("midas pay err, httpRes = " + httpRes + ", httpReq = " + httpReq);
					}
					errCode = ERR_OK;
					gs.getLogger().debug("midas pay openid=" + uinfo.openID
							+ ", amt= " + amt + ", errcode = " + errCode + ", ret = " + ret + ",balance=" + balance);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					errCode = ERR_PARSE;
				}
								
			}

			@Override
			public void runCallback()
			{
			}
			
			int errCode = ERR_CONN;
		}, false);
	}
	
	private final ExecutorService executorHTTP;
	private final ExecutorService executorCallback;
	private GameServer gs;
	
	private AtomicInteger taskCountHTTP = new AtomicInteger();
	private AtomicInteger taskCountCallback = new AtomicInteger();
	
	private AtomicInteger taskRejectCountHTTP = new AtomicInteger();
	private AtomicInteger taskRejectCountCallback = new AtomicInteger();
	
	public static void main(String[] args)
	{
		//ArgsMap am = new ArgsMap(args);
		final Midas midas = new Midas(new GameServer(null));
		midas.start();
		System.out.println("start");
		/*
		UserInfo uinfo = new UserInfo();
		//midas.present(uinfo, 33);
		//midas.pay(uinfo, 20);
		//midas.pay(uinfo, 96);
		midas.getBalance(uinfo, new GetBalanceCallback()
		{
			@Override
			public void onCallback(UserInfo uinfo, BalanceResult res)
			{
				System.out.println("getBalace callback, openID = " + uinfo.openID + ", err = " + res.errCode 
						+ ",ret=" + res.ret + ", save_amt=" + res.save_amt);
			}
			
		});
		*/
		/*
		midas.queryQQVIP("sdfasdf", 1, new Midas.QueryQQVIPCallback()
		{
			@Override
			public void onCallback(int rid, byte res)
			{
				System.out.println("Mias.QueryQQVIPCallback, rid= " + rid + ", res=" + res);
			}
		});
		*/
		//
		ket.util.FileSys.pauseWaitInput();
		midas.destroy();
	}
}

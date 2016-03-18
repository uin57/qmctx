
package ket.util;

public class StringConverter
{
	public static String getHexStringFromBytes(byte[] bytes)
	{
		String ret = "";
		for (int i = 0; i < bytes.length; i++)
		{
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if( hex.length() == 1 )
			{
				hex = "0" + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}
	
	public static byte[] getBytesFromHexString(String hexstr)
	{
		if( hexstr.length() % 2 != 0 )
			hexstr = "0" + hexstr;
		byte[] bs = new byte[hexstr.length()/2];
		for(int i=0; i< bs.length; ++i)
		{
			bs[i] = Integer.valueOf(hexstr.substring(i * 2, i * 2 + 2), 16).byteValue();
		}
		return bs;
	}
	
	public static String getPer(int n, int b, int d)
	{
		float f = 0;
		if( b > 0 && n > 0 ) f = ((float)n/b)*100;
		int d1 = (int)f;
		f -= d1;
		while( d != 0 ) { f *= 10; --d; }
		int d2 = (int)f;
		return Integer.toString(d1) + "." + Integer.toString(d2) + "%";
	}
	
	public static int parseMinuteOfDay(String s)
	{
		String[] lst = s.split(":");
		if( lst == null || lst.length != 2 )
			return -2;
		try
		{
			int h = Integer.parseInt(lst[0]);
			int m = Integer.parseInt(lst[1]);
			return h * 60 + m;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] args)
	{
		String str = "ÖÐ¹ú kitty!";
		try
		{
			byte[] bs = str.getBytes("UTF-8");
			System.out.println(str + " u8 size is " + bs.length);
			String strd = new String(bs, 0, bs.length, "UTF-8");
			System.out.println("strd is " + strd + ", src str is " + str);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}


package ket.util;

import java.util.Map;
import java.util.HashMap;

public class ArgsMap
{	
	
	public ArgsMap(String[] args)
	{
		parse(args);
	}
	
	public boolean containsKey(String key)
	{
		return map.containsKey(key);
	}
	
	public String get(String key)
	{
		return map.get(key);
	}
	
	public String get(String key, String defaultvalue)
	{
		String v = map.get(key);
		return v == null ? defaultvalue : v;
	}
	
	public int size()
	{
		return map.size();
	}
	
	public void dump()
	{
		for(Map.Entry<String, String> e : map.entrySet())
		{
			System.out.println("\t[" + e.getKey() + "] = [" + e.getValue() + "]");
		}
	}
	
	private void parse(String[] args)
	{
		for(String arg : args)
		{
			if( ! arg.startsWith("-") )
				continue;
			arg = arg.substring(1);
			int i = arg.indexOf("=");
			if( i == -1 )
				map.put(arg, "");
			else
			{
				String[] v = arg.split("=");
				String k = v[0].trim();
				if( !k.isEmpty() )
				{
					if( v[1].isEmpty() )
						map.put(k, "");
					else
						map.put(k, arg.substring(i+1).trim());
				}
			}
		}
	}
	
	private Map<String, String> map = new HashMap<String, String>();
}

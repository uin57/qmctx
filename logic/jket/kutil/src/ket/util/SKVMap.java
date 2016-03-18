
package ket.util;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public final class SKVMap
{
	public String getString(final String secname, final String key)
	{
		return getString(secname, key, null);
	}
	
	public String getString(final String secname, final String key, final String defaultValue)
	{
		String val = getValue(secname, key);
		return val != null ? val : defaultValue;
	}
	
	public Long getLong(final String secname, final String key)
	{
		return getLong(secname, key, null);
	}
	
	public Long getLong(final String secname, final String key, final Long defaultValue)
	{
		String valStr = getValue(secname, key);
		if( valStr == null )
			return defaultValue;
		try
		{
			return Long.decode(valStr);
		}
		catch (NumberFormatException ex)
		{
			throw new Error("SVKMapError", ex);
		}
	}
	
	public Integer getInteger(final String secname, final String key)
	{
		return getInteger(secname, key, null);
	}
	
	public Integer getInteger(final String secname, final String key, final Integer defaultValue)
	{
		String valStr = getValue(secname, key);
		if( valStr == null )
			return defaultValue;
		try
		{
			return Integer.decode(valStr);
		}
		catch (NumberFormatException ex)
		{
			throw new Error("SVKMapError", ex);
		}
	}
	
	public Short getShort(final String secname, final String key)
	{
		return getShort(secname, key, null);
	}
	
	public Short getShort(final String secname, final String key, final Short defaultValue)
	{
		String valStr = getValue(secname, key);
		if( valStr == null )
			return defaultValue;
		try
		{
			return Short.decode(valStr);
		}
		catch (NumberFormatException ex)
		{
			throw new Error("SVKMapError", ex);
		}
	}
	
	public Byte getByte(final String secname, final String key)
	{
		return getByte(secname, key, null);
	}
	
	public Byte getByte(final String secname, final String key, final Byte defaultValue)
	{
		String valStr = getValue(secname, key);
		if( valStr == null )
			return defaultValue;
		try
		{
			return Byte.decode(valStr);
		}
		catch (NumberFormatException ex)
		{
			throw new Error("SVKMapError", ex);
		}
	}
	
	public Boolean getBoolean(final String secname, final String key)
	{
		return getBoolean(secname, key, null);
	}
	
	public Boolean getBoolean(final String secname, final String key, final Boolean defaultValue)
	{
		String valStr = getValue(secname, key);
		if( valStr == null )
			return defaultValue;
		String s = valStr.toLowerCase();
		return s.equals("y") || s.equals("t") || s.equals("true") || s.equals("yes");
	}
	
	public List<String> getStringList(String secname, String key, String delimiter)
	{
		String valStr = getValue(secname, key);
		if( valStr != null )
		{
			String[] strs = valStr.split(delimiter);
			return Arrays.asList(strs);
		}
		return null;
	}
	
	public List<Integer> getIntegerList(String secname, String key, String delimiter)
	{
		String valStr = getValue(secname, key);
		if( valStr != null )
		{
			String[] strs = valStr.split(delimiter);
			List<Integer> ints = new ArrayList<Integer>();
			try
			{
				for(String str : strs)
				{
					if( str.indexOf("~") >= 0 )
					{
						String[] strs2 = str.split("~");
						if( strs2.length == 2 )
						{
							int from = Integer.decode(strs2[0].trim());
							int to = Integer.decode(strs2[1].trim());
							if( to >= from )
							{
								for(int i = from; i <= to; ++i)
									ints.add(i);
							}
						}
					}
					else
					{
						ints.add(Integer.decode(str));					
					}
				}
			}
			catch(NumberFormatException ex)
			{
				return null;
			}
			return ints;
		}
		return null;
	}
	
	public boolean load(String strConfigFileName)	
	{
		return load(new File(strConfigFileName));
	}
	
	public boolean load(final File fileConfig)
	{
		LineNumberReader reader = null;
		try
		{
			reader = new LineNumberReader(new FileReader(fileConfig));
		
			String line = null;
			String secname = "";
			while( true )
			{
				line = reader.readLine();
				if( line == null )
					break;
				line = line.trim();
				if( line.length() == 0 || line.startsWith("#") || line.startsWith(";") || line.startsWith("\\") )
					continue;
				if( line.charAt(0) == '[' )
				{
					int i = line.indexOf(']', 1);
					if( i == -1 )
						continue;
					secname = line.substring(1, i);
				}
				else
				{
					int i = line.indexOf('=');
					if( i == -1 || i == 0 || i == line.length() - 1 )
						continue;
					String key = line.substring(0, i).trim();
					String val = line.substring(i + 1).trim();
					Map<String, String> map = this.map.get(secname);
					if( map != null )
						map.put(key, val);
					else
					{
						map = new HashMap<String, String>();
						map.put(key, val);
						this.map.put(secname, map);
					}
				}
			}
			reader.close();
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	private String getValue(final String secname, final String key)
	{
		Map<String, String> map = this.map.get(secname);
		if( map == null )
			return null;
		return map.get(key);
	}
	
	private final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
}

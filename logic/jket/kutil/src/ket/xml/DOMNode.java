
package ket.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class DOMNode implements Node
{
	public DOMNode(Element element)
	{
		this.element = element;
	}
	
	@Override
	public Node getChild(String name)
	{
		NodeList nodelst = element.getElementsByTagName(name);
		if( nodelst.getLength() > 0 )
			return new DOMNode((Element)nodelst.item(0));
		return null;
	}
	
	@Override
	public List<Node> getChildren(String name)
	{
		List<Node> nodes = new ArrayList<Node>();
		NodeList nodelst = element.getElementsByTagName(name);
		int n = nodelst.getLength();
		for(int i = 0; i < n; ++i)
			nodes.add(new DOMNode((Element)nodelst.item(i)));
		return nodes;
	}
	
	@Override
	public String getTextContent()
	{
		try
		{
			String text = element.getTextContent();
			if( text == null || text.trim().isEmpty() )
				return "";
			return text;
		}
		catch(DOMException ex)
		{
			return "";
		}
	}
	
	@Override
	public boolean hasAttribute(String name)
	{
		return ! element.getAttribute(name).isEmpty();
	}
	
	@Override
	public String getString(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		return value;
	}
	
	@Override
	public String getString(String name, String defaultValue)
	{
		try
		{
			return getString(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public boolean getBoolean(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		String s = value.trim().toLowerCase();
		return s.equals("y") || s.equals("t") || s.equals("true") || s.equals("yes");   
	}
	
	@Override
	public boolean getBoolean(String name, boolean defaultValue)
	{
		try
		{
			return getBoolean(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public int getInteger(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		try
		{
			return Integer.parseInt(value);
		}   
		catch(NumberFormatException ex)
		{
			throw new ReaderException("invalid number format " + value + ".", ex);
		}
	}
	
	@Override
	public int getInteger(String name, int defaultValue)
	{
		try
		{
			return getInteger(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public short getShort(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		try
		{
			return Short.parseShort(value);
		}   
		catch(NumberFormatException ex)
		{
			throw new ReaderException("invalid number format " + value + ".", ex);
		}
	}
	
	@Override
	public short getShort(String name, short defaultValue)
	{
		try
		{
			return getShort(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public float getFloat(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		try
		{
			return Float.parseFloat(value);
		}   
		catch(NumberFormatException ex)
		{
			throw new ReaderException("invalid number format " + value + ".", ex);
		}
	}
	
	@Override
	public float getFloat(String name, float defaultValue)
	{
		try
		{
			return getFloat(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public byte getByte(String name) throws ReaderException
	{
		String value = element.getAttribute(name);
		if( value.isEmpty() )
			throw new ReaderException("no attribute with name " + name + " is found.");
		try
		{
			return Byte.parseByte(value);
		}   
		catch(NumberFormatException ex)
		{
			throw new ReaderException("invalid number format " + value + ".", ex);
		}
	}
	
	@Override
	public byte getByte(String name, byte defaultValue)
	{
		try
		{
			return getByte(name);
		}
		catch(ReaderException ex)
		{
			return defaultValue;
		}
	}
	
	@Override
	public <T> T setObject(T object) throws ReaderException
	{
		try
		{
			Field[] fields = object.getClass().getFields();
			for(Field field : fields)
			{
				if( field.getModifiers() != Modifier.PUBLIC )
					continue;
				String type = field.getType().getSimpleName();
				String name = field.getName();
				try
				{
					if( type.equals("String") )
						field.set(object, getString(name));
					else if( type.equals("boolean") )
						field.set(object, getBoolean(name));
					else if( type.equals("int") )
						field.set(object, getInteger(name));
					else if( type.equals("byte") )
						field.set(object, getByte(name));
					else if( type.equals("float") )
						field.set(object, getFloat(name));
					else
						throw new IllegalAccessException("type " + field.getName() + " no imp");
				}
				catch(ReaderException ex)
				{					
				}
			}
			return object;
		}
		catch(IllegalAccessException ex)
		{
			throw new ReaderException("", ex);
		}
	}
	
	@Override
	public <T> T getChild(String name, T object) throws ReaderException
	{
		Node child = getChild(name);
		if( child != null )
			child.setObject(object);
		return object;
	}
	
	@Override
	public <T> T getChild(String name, Class<T> cls) throws ReaderException
	{
		Node child = getChild(name);
		if( child == null )
			return null;
		try
		{
			T obj = (T)cls.newInstance();
			child.setObject(obj);
			return obj;
		}
		catch(Exception ex)
		{
			throw new ReaderException("reflect error", ex);
		}
	}
	
	@Override
	public <T> int getChildren(String name, List<T> lst, Class<T> cls) throws ReaderException
	{
		try
		{
			List<Node> nodes = getChildren(name);
			for(Node node : nodes)
			{
				T obj = cls.newInstance();
				node.setObject(obj);
				lst.add(obj);
			}
			return lst.size();
		}
		catch(Exception ex)
		{
			throw new ReaderException("reflect failed", ex);
		}
	}

	private Element element;
}

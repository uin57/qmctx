
package ket.xml;

import java.util.List;

public interface Node
{
	public Node getChild(String name);
	public List<Node> getChildren(String name);
	
	public String getTextContent();
	
	public boolean hasAttribute(String name);
	
	public String getString(String name) throws ReaderException;
	public String getString(String name, String defaultValue);
	
	public boolean getBoolean(String name) throws ReaderException;
	public boolean getBoolean(String name, boolean defaultValue);
	
	public int getInteger(String name) throws ReaderException;
	public int getInteger(String name, int defaultValue);
	
	public short getShort(String name) throws ReaderException;
	public short getShort(String name, short defaultValue);
	
	public byte getByte(String name) throws ReaderException;
	public byte getByte(String name, byte defaultValue);
	
	public float getFloat(String name) throws ReaderException;
	public float getFloat(String name, float defaultValue);
	
	public <T> T setObject(T object) throws ReaderException;
	public <T> T getChild(String name, T object) throws ReaderException;
	public <T> T getChild(String name, Class<T> cls) throws ReaderException;
	public <T> int getChildren(String name, List<T> lst, Class<T> cls) throws ReaderException;
}

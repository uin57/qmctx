package i3k.proxy;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Stack;
import java.io.BufferedInputStream;
import java.io.FileInputStream;




import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import ket.kio.NetAddress;


public class ForwardTable 
{
	IDIPProxyServer ps;
	Map<Integer, NetAddress> gs = null;
	CustomXMLParser parser = new CustomXMLParser();
	public ForwardTable(IDIPProxyServer ps)
	{
		this.ps = ps;
	}
	
	public NetAddress getFowardAddress(int id)
	{
		NetAddress netAddr = null;
		if (gs != null)
			netAddr = gs.get(id);
		return netAddr;
	}
	
	public void init(String cfgFileName)
	{
		final String filePath = cfgFileName;
		ps.mointorFileChanged(filePath, new Runnable()
			{
				@Override
				public void run()
				{
					setConfigureFile(filePath);
				}
			});
	}
	
	public void setConfigureFile(String filepath)
	{
		ForwardTableXMLHandler handler = new ForwardTableXMLHandler(parser);
		ParseXML(filepath, handler);			
		XMLItem item = handler.getRootItem();
		Map<Integer, NetAddress> gs = getConfig(item);
		if (gs != null)
		{
			this.gs = gs;
		}
	}
	
	private Map<Integer, NetAddress> getConfig(XMLItem item)
	{
		Map<Integer, NetAddress> gs = null;
		try
		{
			if (item != null)
			{
				for (XMLItem citem : item.childrenItems)
				{
					if (citem.getAttribute("name").equals("GameServers"))
					{
						gs = new TreeMap<Integer, NetAddress>();
						for (XMLItem ccitem : citem.childrenItems)
						{
							String aname = ccitem.getAttribute("name");
							if (aname.equals("GameSvr"))
							{
								String gsidStr = ccitem.getAttribute("gsid").trim();
								int gsid = Integer.parseInt(gsidStr);
								String ipStr = ccitem.getAttribute("ip").trim();
								String portStr = ccitem.getAttribute("port").trim();
								int port = Integer.parseInt(portStr);
								NetAddress netAddr = new NetAddress(ipStr, port);
								gs.put(gsid, netAddr);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			gs = null;
		}
		return gs;
	}
	
	public static void ParseXML(String filename, DefaultHandler handler)
	{
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(new BufferedInputStream(new FileInputStream(filename)), handler);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	static class XMLItem
	{
		XMLItem parent;
		String name;
		StringBuffer value = new StringBuffer();
		List<XMLItem> childrenItems = new ArrayList<XMLItem>();
		Map<String, String> attributes = new TreeMap<String, String>();
		
		XMLItem(String name)
		{
			this.name = name;
		}
		
		String getAttribute(String name)
		{
			String val = attributes.get(name);
			val = (val == null ? "" : val);
			return val;
		}
	}
	
	class CustomXMLParser
	{
		public CustomXMLParser()
		{
			
		}
		
		public boolean isElementValid(String parentName, String parentAttributeName, String childName, String attributeName)
		{
			if (parentName == null)
			{
				if (childName.equals("forwardtable"))
				{
					return true;
				}
			}
			else if (parentName.equals("forwardtable"))
			{
				if (childName.equals("struct") && attributeName.equals("GameServers"))
				{
					return true;
				}
			}
			else if (parentName.equals("struct") && parentAttributeName.equals("GameServers"))
			{
				if (childName.equals("entry") && attributeName.equals("GameSvr"))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	class ForwardTableXMLHandler extends DefaultHandler
	{
		CustomXMLParser parser;
		XMLItem root = null;
		XMLItem curItem = null;
		boolean parseEnd = false;
		Stack<String> stack = new Stack<String>();
		int validLength = 0;
		
		public ForwardTableXMLHandler(CustomXMLParser parser)
		{
			this.parser = parser;
		}
		
		public XMLItem getRootItem()
		{
			XMLItem item = null;
			if (parseEnd)
				item = root;
			return item;
		}
		
		private String getAttributeNameValue(Attributes attributes)
		{
			String value = "";
			for (int i = 0; i < attributes.getLength(); ++i)
			{
				if (attributes.getQName(i).equals("name"))
				{
					value = attributes.getValue(i);
					break;
				}
			}
			return value;
		}
		
		public void startDocument()
		{
			
		}
		
		public void endDocument()
		{
			parseEnd = true;
		}
		
		
		public void startElement(String uri, String localName, String qName, Attributes attributes)
		{
			int curLength = stack.size();
			stack.push(qName);
			if (curLength > validLength)
				return;
			String parentName = null;
			String parentAttributeNameValue = "";
			if (curItem!= null)
			{
				parentName = curItem.name;
				parentAttributeNameValue = curItem.getAttribute("name");
			}
			if (!parser.isElementValid(parentName, parentAttributeNameValue, qName, getAttributeNameValue(attributes)))
				return;
			++validLength;
			XMLItem item = new XMLItem(qName);
			for (int i = 0; i < attributes.getLength(); ++i)
			{
				item.attributes.put(attributes.getQName(i), attributes.getValue(i));
			}
			if (root == null)
			{
				root = curItem = item;
			}
			else
			{
				curItem.childrenItems.add(item);
				item.parent = curItem;
				curItem = item;
			}
		}
		
		public void endElement(String uri, String localName, String qName) 
		{
			int curLength = stack.size();
			stack.pop();
			if (curLength > validLength)
				return;
			--validLength;
			curItem = curItem.parent;
		}
		
		public void characters(char[] ch, int start, int length)
		{
			if (stack.size() > validLength)
				return;
			curItem.value.append(ch, start, length);
		}
	}
}






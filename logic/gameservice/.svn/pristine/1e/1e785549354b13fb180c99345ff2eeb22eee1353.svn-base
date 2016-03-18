package com.joypiegame.gameservice.util;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Stack;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParser 
{ 
	private CustomXMLVerifier verifier = new CustomXMLVerifier();
	public XmlParser()
	{
		
	}
	
	public CustomXMLVerifier getVerifier()
	{
		return verifier;
	}
	
	public XMLItem parse(String filepath)
	{
		System.out.println("parse xml file (" + filepath + ") !");
		CustomXMLHandler handler = new CustomXMLHandler(verifier);
		ParseXML(filepath, handler);			
		XMLItem item = handler.getRootItem();
		return item;
	}
	
	private static void ParseXML(String filename, DefaultHandler handler)
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
	
	public static class XMLItem
	{
		XMLItem parent;
		String name;
		StringBuffer value = new StringBuffer();
		List<XMLItem> childrenItems = new ArrayList<XMLItem>();
		Map<String, String> attributes = new TreeMap<String, String>();
		
		public XMLItem(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getAttribute(String name)
		{
			String val = attributes.get(name);
			val = (val == null ? "" : val);
			return val;
		}
		
		public Iterable<XMLItem> getChildernItems()
		{
			return childrenItems;
		}
		
		
	}
	
	public class CustomXMLVerifier
	{
		private String rootElementName = "";
		private String structElementEName = "";
		private String entryElementName = "";
		private Set<String> structAttributeNameSet = new TreeSet<String>();
		private Set<String> entryAttributeNameSet = new TreeSet<String>();
		public CustomXMLVerifier()
		{
			
		}
		
		public void reset()
		{
			rootElementName = "";
			structElementEName = "";
			entryElementName = "";
			structAttributeNameSet.clear();
			entryAttributeNameSet.clear();
		}
		
		public void setRootElementName(String elementName)
		{
			this.rootElementName = elementName;
		}
		
		public void setStructElementName(String elementName)
		{
			this.structElementEName = elementName;
		}
		
		public void setEntryElementName(String elementName)
		{
			this.entryElementName = elementName;
		}
		
		public void addStructAttributeName(String name)
		{
			structAttributeNameSet.add(name);
		}
		
		public void addEntryAttributeName(String name)
		{
			entryAttributeNameSet.add(name);
		}
		
		public boolean isElementValid(String parentName, String parentAttributeName, String childName, String childAttributeName)
		{
			if (parentName == null)
			{
				if (childName.equals(rootElementName))
				{
					return true;
				}
			}
			else if (parentName.equals(rootElementName))
			{
				if (childName.equals(structElementEName) && structAttributeNameSet.contains(childAttributeName))
				{
					return true;
				}
			}
			else if (parentName.equals(structElementEName) && structAttributeNameSet.contains(parentAttributeName))
			{
				if (childName.equals(entryElementName) && entryAttributeNameSet.contains(childAttributeName))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	class CustomXMLHandler extends DefaultHandler
	{
		CustomXMLVerifier verifier;
		XMLItem root = null;
		XMLItem curItem = null;
		boolean parseEnd = false;
		Stack<String> stack = new Stack<String>();
		int validLength = 0;
		
		public CustomXMLHandler(CustomXMLVerifier verifier)
		{
			this.verifier = verifier;
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
			//System.out.println("===============================>");
		}
		
		public void endDocument()
		{
			parseEnd = true;
			//System.out.println("<===============================");
		}
		
		
		public void startElement(String uri, String localName, String qName, Attributes attributes)
		{
			//System.out.println(">>>>>>>>>startElement>>>>>>curItem Name="+(curItem==null ? "null":curItem.name)+" selfName="+qName);
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
			if (!verifier.isElementValid(parentName, parentAttributeNameValue, qName, getAttributeNameValue(attributes)))
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
//			System.out.println("startElement-------");
//			System.out.println("uri="+uri);
//			System.out.println("localName="+localName);
//			System.out.println("qName="+qName);
//			System.out.println("---------------------");
//			for (int i = 0; i < attributes.getLength(); ++i)
//			{
//				System.out.println(i+"-uri="+attributes.getURI(i));
//				System.out.println(i+"-localName="+attributes.getLocalName(i));
//				System.out.println(i+"-qName="+attributes.getQName(i));
//				System.out.println(i+"-type="+attributes.getType(i));
//				System.out.println(i+"-value="+attributes.getValue(i));
//			}
		}
		
		public void endElement(String uri, String localName, String qName) 
		{
			//System.out.println(">>>>>>>>>endElement>>>>>>>curItem Name="+(curItem==null ? "null":curItem.name)+" selfName="+qName);
			int curLength = stack.size();
			stack.pop();
			if (curLength > validLength)
				return;
			--validLength;
			curItem = curItem.parent;
//			System.out.println("---------------------");
//			System.out.println("uri="+uri);
//			System.out.println("localName="+localName);
//			System.out.println("qName="+qName);
//			System.out.println("endElement---------");
		}
		
		public void characters(char[] ch, int start, int length)
		{
			if (stack.size() > validLength)
				return;
			curItem.value.append(ch, start, length);
//			System.out.println("***********************");
//			System.out.println(new String(ch, start, length));
//			System.out.println("***********************");
		}
	}
}





package ket.util;

import java.util.ArrayList;
import java.util.List;

public class EasyXML
{
	public static class Node
	{
		public Node()
		{		
		}
		
		public Node(String content)
		{
			this.content = content;
		}
		
		public String content = "";
	}

	public static Node findFirstNode(String doc, String nodetype)
	{
		int start = 0;
		Node node = new Node();
		while( true )
		{
			int i = doc.indexOf("<" + nodetype, start);
			if( i == -1 )
				return null;
			int next = i + 1 + nodetype.length();
			if( next >= doc.length() )
				return null;
			if( doc.charAt(next) == '>' || doc.charAt(next) == ' ' || doc.charAt(next) == '/')
			{
				int j = doc.indexOf(">", next);
				if( j == -1 )
					return null;
				if( doc.charAt(j - 1) == '/' )
					return node;
				int k = doc.indexOf("</" + nodetype + ">", next);
				if( k == -1 )
					return null;
				node.content = doc.substring(j + 1, k).trim();
				return node;
			}
			else
				start = next;
		}
	}
	
	public static List<Node> findNodes(String doc, String nodetype)
	{
		List<Node> lst = new ArrayList<Node>();
		int start = 0;
		while( true )
		{
			int i = doc.indexOf("<" + nodetype, start);
			if( i == -1 )
				break;
			int next = i + 1 + nodetype.length();
			if( next >= doc.length() )
				break;
			if( doc.charAt(next) == '>' || doc.charAt(next) == ' ' || doc.charAt(next) == '/')
			{
				int j = doc.indexOf(">", next);
				if( j == -1 )
					break;
				if( doc.charAt(j - 1) == '/' )
				{
					lst.add(new Node());
					start = j + 1;
					continue;
				}
				int k = doc.indexOf("</" + nodetype + ">", next);
				if( k == -1 )
					break;
				lst.add(new Node(doc.substring(j + 1, k).trim()));
				start = k + 3 + nodetype.length();
				continue;
			}
			else
				start = next;
		}
		return lst;
	}
}

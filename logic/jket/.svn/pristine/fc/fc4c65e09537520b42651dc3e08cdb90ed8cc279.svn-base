
package ket.kiox;

import java.io.InputStream;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.InetAddress;

public class HTTP
{
	
	public static class URL
	{
		public String host = "127.0.0.1";
		public int port = 80;
		public String path = "/";
		
		public static URL parseURL(String url)
		{
			if( url == null )
				return null;
			int npf = url.indexOf("://");
			int nhost = 0;
			if( npf != -1 )
				nhost = npf + 3;
			int npath = url.indexOf("/", nhost);
			String path = "/";
			String addr = "";
			if( npath != -1 )
			{
				path = url.substring(npath).trim();
				addr = url.substring(nhost, npath).trim();
			}
			else
				addr = url.substring(nhost).trim();
			if( addr.equals("") )
				return null;
			if( path.equals("") )
				path = "/";
			URL u = new URL();
			u.path = path;
			int nport = addr.indexOf(":");
			if( nport == -1 )
			{
				u.host = addr;
				u.port = 80;
			}
			else
			{
				try
				{
					u.host = addr.substring(0, nport);
					u.port = Integer.parseInt(addr.substring(nport+1));
				}
				catch(Exception ex)
				{
					return null;
				}
			}
			return u;
		}
		
		public String toURL()
		{
			if( port == 80 )
				return "http://" + host + path;
			else
				return "http://" + host + ":" + port + path;
		}
		
		public String toURLBase()
		{
			if( port == 80 )
				return "http://" + host;
			else
				return "http://" + host + ":" + port;
		}		
	}
	
	public static class GetRequest
	{		
		public URL url;
		
		public GetRequest(URL url)
		{
			this.url = url;
		}
		
		public byte[] toHeader()
		{
			String header = "GET " + url.path + " " + version + "\r\n";
			header = header + "Host: " + url.host + ":" + url.port + "\r\n";
			header = header + "ACCEPT-LANGUAGE: en\r\n"; // todo
			header = header + "\r\n";
			return header.getBytes();
		}
	}
	
	public static class PostRequest
	{		
		public URL url;
		public byte[] data;
		
		public PostRequest(URL url, byte[] data)
		{
			this.url = url;
			this.data = data;
		}
		public byte[] toRequest()
		{
			String header = "POST " + url.path + " " + version + "\r\n";
			header = header + "Host: " + url.host + ":" + url.port + "\r\n";
			header = header + "Content-Length: " + data.length + "\r\n";
			header = header + "Content-Type: text/xml; charset=\"utf-8\"\r\n"; // todo
			header = header + "SOAPAction: \"urn:schemas-upnp-org:service:WANIPConnection:1#AddPortMapping\"\r\n"; // todo
			header = header + "\r\n";
			byte[] headerbs = header.getBytes();
			byte[] reqbs = new byte[headerbs.length + data.length];
			System.arraycopy(headerbs, 0, reqbs, 0, headerbs.length);
			System.arraycopy(data, 0, reqbs, headerbs.length, data.length);
			return reqbs;
		}
	}
	
	public static class Response
	{
		byte[] data = null;
		
		public boolean parse(InputStream is)
		{
			byte[] buf = new byte[1024];
			int nrecv = 0;
			try
			{
				while( true )
				{
					int nread = is.read(buf, nrecv, buf.length - nrecv);
					if( nread == -1 )
						break;
					nrecv = nrecv + nread;
					if( nrecv == buf.length )
						break;
				}
				String header = new String(buf, 0, nrecv);
				String[] headers = header.split("\\n");
				if( headers.length == 0 || ! headers[0].trim().equals(version + " 200 OK") )
				{
					//System.out.println(headers[0]);
					return false;
				}
				
				int nlen = -1;
				int nheader = 0;
				for(String h : headers)
				{
					nheader = nheader + h.getBytes().length;
					nheader = nheader + 1;
					String s = h.trim();
					if( s.equals("") )
						break;
					if( s.indexOf("Content-Length") == 0 
							|| s.indexOf("CONTENT-LENGTH") == 0 )
					{
						int nnlen = s.indexOf(":");
						if( nnlen != -1 )
						{
							try
							{
								nlen = Integer.parseInt(s.substring(nnlen+1).trim());
							}
							catch(Exception ex)
							{								
							}
						}
					}
				}
				if( nlen == -1 )
					return false;
				//System.out.println("nheader is " + nheader + ", len is " + nlen);
				byte[] tmpdata = new byte[nlen];
				nrecv = nrecv - nheader;
				System.arraycopy(buf, nheader, tmpdata, 0, nrecv);
				/*
				while( true )
				{
					if( nrecv < tmpdata.length )
					{
						int nread = is.read(tmpdata, nrecv, tmpdata.length - nrecv);
						if( nread == -1 )
							break;
						nrecv = nrecv + nread;
					}
					else
					{
						int nread = is.read(buf);
						if( nread == -1 )
							break;
					}
				}
				if( nrecv == tmpdata.length )
				{
					data = tmpdata;
					return true;
				}
				*/
				while( nrecv != tmpdata.length )
				{
					int nread = is.read(tmpdata, nrecv, tmpdata.length - nrecv);
					if( nread == -1 )
						return false;
					nrecv = nrecv + nread;
				}				
				data = tmpdata;
				return true;			
			}
			catch(Exception ex)
			{				
			}
			return false;
		}
	}
	
	public static String getBase(String url)
	{
		URL u = URL.parseURL(url);
		if( u != null )
			return u.toURLBase();
		return null;
	}
	
	public static byte[] get(String url)
	{
		URL u = URL.parseURL(url);
		if( u == null )
			return null;
		GetRequest req = new GetRequest(u);
		//System.out.println("http getting: " + req.toURL());
		try
		{
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress(InetAddress.getByName(req.url.host), req.url.port));
			sock.getOutputStream().write(req.toHeader());
			Response res = new Response();
			res.parse(sock.getInputStream());
			sock.close();
			return res.data;
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
		return null;
	}
	
	public static byte[] post(String url, byte[] data)
	{
		URL u = URL.parseURL(url);
		if( u == null )
			return null;
		PostRequest req = new PostRequest(u, data);
		//System.out.println("http getting: " + req.toURL());
		try
		{
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress(InetAddress.getByName(req.url.host), req.url.port));
			sock.getOutputStream().write(req.toRequest());
			Response res = new Response();
			res.parse(sock.getInputStream());
			sock.close();
			return res.data;
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args)
	{
		
	}
	
	private static String version = "HTTP/1.1";
}

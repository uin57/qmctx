
package ket.kio;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import ket.util.Stream;

public class NetAddress implements Cloneable, Stream.IStreamable
{
	
	public NetAddress()
	{
	}
	
	public NetAddress(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	public NetAddress(NetAddress addr)
	{
		this(addr.host, addr.port);
	}
	
	@Override
	public boolean equals(Object other)
	{
		NetAddress addrOther = (NetAddress)other;
		if( addrOther == this )
			return true;
		if( addrOther == null )
			return false;
		return port == addrOther.port &&
			( host == addrOther.host || host != null && host.equals(addrOther.host) );
	}
	
	@Override
	public int hashCode()
	{
		return host.hashCode() ^ port;
	}
	
	public NetAddress set(String host, int port)
	{
		this.host = host;
		this.port = port;
		return this;
	}
	
	public NetAddress set(NetAddress other)
	{
		this.host = other.host;
		this.port = other.port;
		return this;
	}
	
	public NetAddress(InetSocketAddress addr)
	{
		if( addr.getAddress().isAnyLocalAddress() )
		{
			this.host = "0.0.0.0";
			this.port = addr.getPort();
			return;
		}
		byte[] bs = addr.getAddress().getAddress();
		int is[] = new int[bs.length];
		for(int i=0; i< is.length; ++i)
		{
			is[i] = bs[i];
			if( is[i] < 0 )
				is[i] += 256;
		}
		this.host = "";
		this.host += is[0];
		for(int i= 1; i< is.length; ++i)
			this.host += ("." + is[i]);
		this.port = addr.getPort();
	}
	
	@Override
	public Object clone()
	{
		return new NetAddress(host, port);
	}
	
	@Override
	public String toString()
	{
		return "[" + host + " " + port + "]";
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		host = is.popString();
		port = is.popInteger();
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushString(host);
		os.pushInteger(port);
	}
	
	public static NetAddress parse(String s)
	{
		if( s == null )
			return null;
		String v[] = s.split(":");
		return new NetAddress(v[0], Integer.parseInt(v[1]));
	}
	
	public boolean isLoopback()
	{
		try
		{
			return InetAddress.getByName(host).isLoopbackAddress();
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	public boolean isAnyLocal()
	{
		try
		{
			return InetAddress.getByName(host).isAnyLocalAddress();
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	public boolean isPrivate()
	{
		try
		{
			return InetAddress.getByName(host).isSiteLocalAddress();
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	

	
	public static boolean isLoopback(String ip)
	{
		try
		{
			return InetAddress.getByName(ip).isLoopbackAddress();
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	public static boolean isPrivate(String ip)
	{
		try
		{
			return InetAddress.getByName(ip).isSiteLocalAddress();
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	public String host = new String();
	public int port = 0;
}

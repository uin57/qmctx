
package ket.kio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketTool
{
	
	static InetSocketAddress newAddress(NetAddress addr)
	{
		if( addr == null )
			return null;
		if( addr.host == null || addr.host.equals("") )
			return new InetSocketAddress(addr.port);
		return new InetSocketAddress(addr.host, addr.port);
	}
	static InetSocketAddress bind(DatagramChannel dc, NetAddress addrBind, BindPolicy bindPolicy) throws SocketException
	{
		InetSocketAddress insaBind = newAddress(addrBind);
		dc.socket().bind(insaBind);
		return (InetSocketAddress)dc.socket().getLocalSocketAddress();
	}
	static InetSocketAddress bind(NetworkChannel nc, NetAddress addrBind, BindPolicy bindPolicy) throws IOException
	{
		// TODO bug ? setReuseAddress(true) doesn't work after a failure bind 
		//if( ssc.socket().getReuseAddress() )
		//	ssc.socket().setReuseAddress(false);
		if( bindPolicy == BindPolicy.eReuseListen || bindPolicy == BindPolicy.eReuseTimewait )
			nc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		else
			nc.setOption(StandardSocketOptions.SO_REUSEADDR, false);
		// TODO
		InetSocketAddress insaBind = newAddress(addrBind);
		boolean bBindError = false;
		try
		{
			nc.bind(insaBind);
		}
		catch(BindException ex)
		{			
			bBindError = true;
		}
		if( bBindError )
		{
			if( insaBind == null || insaBind.getPort() == 0 )
				return null;
			switch( bindPolicy )
			{
			case eGiveup:
				return null;
			case eLoopIncPort:
				while( bBindError )
				{
					try
					{
						insaBind = new InetSocketAddress(insaBind.getAddress(), insaBind.getPort() + 1);
						nc.bind(insaBind);
						break;
					}
					catch(BindException ex)
					{
					}
				}
				break;
			case eReuseTimewait:
				{
					InetSocketAddress addrTry = insaBind.getAddress().isAnyLocalAddress() ?
						new InetSocketAddress("127.0.0.1", insaBind.getPort()) :
							new InetSocketAddress(insaBind.getAddress(), insaBind.getPort());
					try
					{
						SocketChannel scTry = SocketChannel.open(addrTry);
						boolean bOpen = scTry.isConnected();
						try
						{
							scTry.close();
						}
						catch(Exception ex)
						{								
						}
						if( bOpen )
							return null;
					}
					catch(Exception ex)
					{						
					}
				}
			case eReuseListen:
				nc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				nc.bind(insaBind);
				break;
			default:
				break;
			}
		}
		return (InetSocketAddress)nc.getLocalAddress();
	}
	static InetSocketAddress bind(AsynchronousServerSocketChannel assc, NetAddress addrBind, int backlog, BindPolicy bindPolicy) throws IOException
	{
		// TODO bug ? setReuseAddress(true) doesn't work after a failure bind 
		//if( ssc.socket().getReuseAddress() )
		//	ssc.socket().setReuseAddress(false);
		if( bindPolicy == BindPolicy.eReuseListen || bindPolicy == BindPolicy.eReuseTimewait )
			assc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		else
			assc.setOption(StandardSocketOptions.SO_REUSEADDR, false);
		// TODO
		InetSocketAddress insaBind = newAddress(addrBind);
		boolean bBindError = false;
		try
		{
			assc.bind(insaBind, backlog < 0 ? 0 : backlog);
		}
		catch(BindException ex)
		{			
			bBindError = true;
		}
		if( bBindError )
		{
			if( insaBind == null || insaBind.getPort() == 0 )
				return null;
			switch( bindPolicy )
			{
			case eGiveup:
				return null;
			case eLoopIncPort:
				while( bBindError )
				{
					try
					{
						insaBind = new InetSocketAddress(insaBind.getAddress(), insaBind.getPort() + 1);
						assc.bind(insaBind, backlog < 0 ? 0 : backlog);
						break;
					}
					catch(BindException ex)
					{
					}
				}
				break;
			case eReuseTimewait:
				{
					InetSocketAddress addrTry = insaBind.getAddress().isAnyLocalAddress() ?
						new InetSocketAddress("127.0.0.1", insaBind.getPort()) :
							new InetSocketAddress(insaBind.getAddress(), insaBind.getPort());
					try
					{
						SocketChannel scTry = SocketChannel.open(addrTry);
						boolean bOpen = scTry.isConnected();
						try
						{
							scTry.close();
						}
						catch(Exception ex)
						{								
						}
						if( bOpen )
							return null;
					}
					catch(Exception ex)
					{						
					}
				}
			case eReuseListen:
				assc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				assc.bind(insaBind, backlog < 0 ? 0 : backlog);
				break;
			default:
				break;
			}
		}
		return (InetSocketAddress)assc.getLocalAddress();
	}
	static InetSocketAddress bind(ServerSocketChannel ssc, NetAddress addrBind, int backlog, BindPolicy bindPolicy) throws IOException
	{
		// TODO bug ? setReuseAddress(true) doesn't work after a failure bind 
		//if( ssc.socket().getReuseAddress() )
		//	ssc.socket().setReuseAddress(false);
		if( bindPolicy == BindPolicy.eReuseListen || bindPolicy == BindPolicy.eReuseTimewait )
			ssc.socket().setReuseAddress(true);
		else
			ssc.socket().setReuseAddress(false);
		// TODO
		InetSocketAddress insaBind = newAddress(addrBind);
		boolean bBindError = false;
		try
		{
			ssc.socket().bind(insaBind, backlog < 0 ? 0 : backlog);
		}
		catch(BindException ex)
		{			
			bBindError = true;
		}
		if( bBindError )
		{
			if( insaBind == null || insaBind.getPort() == 0 )
				return null;
			switch( bindPolicy )
			{
			case eGiveup:
				return null;
			case eLoopIncPort:
				while( bBindError )
				{
					try
					{
						insaBind = new InetSocketAddress(insaBind.getAddress(), insaBind.getPort() + 1);
						ssc.socket().bind(insaBind, backlog < 0 ? 0 : backlog);
						break;
					}
					catch(BindException ex)
					{
					}
				}
				break;
			case eReuseTimewait:
				{
					InetSocketAddress addrTry = insaBind.getAddress().isAnyLocalAddress() ?
						new InetSocketAddress("127.0.0.1", insaBind.getPort()) :
							new InetSocketAddress(insaBind.getAddress(), insaBind.getPort());
					try
					{
						SocketChannel scTry = SocketChannel.open(addrTry);
						boolean bOpen = scTry.isConnected();
						try
						{
							scTry.close();
						}
						catch(Exception ex)
						{								
						}
						if( bOpen )
							return null;
					}
					catch(Exception ex)
					{						
					}
				}
			case eReuseListen:
				ssc.socket().setReuseAddress(true);
				ssc.socket().bind(insaBind, backlog < 0 ? 0 : backlog);
				break;
			default:
				break;
			}
		}
		return (InetSocketAddress)ssc.socket().getLocalSocketAddress();
	}
	
	static InetSocketAddress connect(DatagramChannel dc, NetAddress addrConnect) throws SocketException
	{
		InetSocketAddress iAddrConnect = newAddress(addrConnect);
		if( iAddrConnect != null )
			dc.socket().connect(iAddrConnect);
		return iAddrConnect;
	}
	
	static InetSocketAddress connect(SocketChannel sc, NetAddress addrServer) throws IOException
	{
		InetSocketAddress iAddrServer = newAddress(addrServer);
		if( ! sc.connect(iAddrServer) )
			return null;
		InetSocketAddress addr = getLocalAddress(sc);
		if( addr == null )
			throw new BindException();
		return addr;
	}
	
	static InetSocketAddress getLocalAddress(SocketChannel channel) throws IOException
	{
		try
		{
			return (InetSocketAddress)channel.socket().getLocalSocketAddress();
		}
		catch(ClassCastException ex)
		{
			throw new IOException();
		}
	}
	
	static InetSocketAddress getLocalAddress(NetworkChannel channel) throws IOException
	{
		try
		{
			return (InetSocketAddress)channel.getLocalAddress();
		}
		catch(ClassCastException ex)
		{
			throw new IOException();
		}
	}
	
	static InetSocketAddress getLocalAddress(AsynchronousSocketChannel asc) throws IOException
	{
		try
		{
			return (InetSocketAddress)asc.getLocalAddress();
		}
		catch(ClassCastException ex)
		{
			throw new IOException();
		}
	}
	
	static InetSocketAddress getRemoteAddress(AsynchronousSocketChannel asc) throws IOException
	{
		try
		{
			return (InetSocketAddress)asc.getRemoteAddress();
		}
		catch(ClassCastException ex)
		{
			throw new IOException();
		}
	}
	
	static InetSocketAddress getRemoteAddress(SocketChannel channel) throws IOException
	{
		try
		{
			return (InetSocketAddress)channel.socket().getRemoteSocketAddress();
		}
		catch(ClassCastException ex)
		{
			throw new IOException();
		}
	}

	static void setNonBlocking(SelectableChannel sc) throws IOException
	{
		sc.configureBlocking(false);
	}
}


package ket.kio;

import java.nio.ByteBuffer;

/* 
 * 1. (udppeer, tcpserver, tcpclient)
 *    -->  open
 *                   onOpen       <--
 *                   [onDataRecv] <--  ( udppeer and tcpclient )
 *    --> [sendData...] ( udppeer and tcpclient )
 *    --> [close]
 *    				 [onDataRecv] <--  ( udppeer and tcpclient )
 *                   onClose      <--
 *    --> delete
 *                   
 * 2. (udppeer, tcpserver, tcpclient)
 *    -->  open
 *                   onOpenFailed    <--
 *    -->  delete
 *                   
 * 3. (tcpserver session)
 *                   onSessionOpen(idN)          <--
 *                   [onSessionDataRecv(idN)...] <--
 *   --> [sendSessionData...] ( udppeer and tcpclient )
 *   --> [closeSession]
 *   				 [onSessionDataRecv(idN)...] <--
 *                   onSessionClose(idN)         <--
 * 
 * 4. (tcpclient, tcpserver session)
 * 
 *                   onDataRecv(data recv from socket earlier)   <--
 *                   onDateRecv(data recv from socket later)     <--
 *                   
 * 5. (all)
 *    --> NetManager.destroy()
 *                   all peers and tcpserver sessions are in closed state   <--
 */

final public class NetManager
{
	public enum NetManagerType
	{
		eSelectNetManager,
		eAsyncNetManager,
		eMTSelectNetManager
	}
	public static NetManagerType eDefaultNetManager = NetManagerType.eSelectNetManager;
	//public static NetManagerType eDefaultNetManager = NetManagerType.eAsyncNetManager;
	//public static NetManagerType eDefaultNetManager = NetManagerType.eMTSelectNetManager;
	public NetManager()
	{
		this(eDefaultNetManager);
	}
	
	public NetManager(NetManagerType nmtype)
	{
		switch( nmtype )
		{
		case eSelectNetManager:
			imp = new NetManagerSelectImp();
			break;
		case eAsyncNetManager:
			imp = new NetManagerAsyncImp();
			break;
		case eMTSelectNetManager:
			imp = new NetManagerMTSelectImp();
			break;
		default:
			break;
		}
	}
	public interface Peer
	{
		public void open();
		public void close();
		public boolean isOpen();
		public void getStatistic(Statistic stat);
	}
	public interface TCPServerImp extends Peer
	{		
		public void closeSession(int sessionid);
		public NetAddress getServerAddr();
		public void sendSessionData(int sessionid, final ByteBuffer bb);
	}
	public interface TCPClientImp extends Peer
	{		
		public NetAddress getClientAddr();
		void sendData(final ByteBuffer bb);
	}
	public interface UDPPeerImp extends Peer
	{		
		public NetAddress getBoundAddr();
		public void sendData(final NetAddress addrTarget/*ignored by connected peer*/, final ByteBuffer bb);
	}
	public interface NetManagerImp
	{
		public TCPServerImp newTCPServerImp(TCPServer peer);
		public TCPClientImp newTCPClientImp(TCPClient peer);
		public UDPPeerImp newUDPPeerImp(UDPPeer peer);
		public boolean start();
		public void destroy();
		public void checkIdleConnections();
		public void getStatistic(Statistic stat);
	}
	public abstract class PeerHandle<ImpType extends Peer> implements Peer
	{
		public abstract void onOpen();
		public abstract void onOpenFailed(ErrorCode errcode);
		public abstract void onClose(ErrorCode errcode);
		@Override
		public void open()
		{
			imp.open();
		}
		@Override
		public void close()
		{
			imp.close();
		}
		@Override
		public boolean isOpen()
		{
			return imp.isOpen();
		}
		@Override
		public void getStatistic(Statistic stat)
		{
			imp.getStatistic(stat);
		}
		
		ImpType imp;
	}
	
	public abstract class TCPServer extends PeerHandle<TCPServerImp>
	{
		public TCPServer() { imp = NetManager.this.imp.newTCPServerImp(this); }
		public int getMaxConnectionIdleTime() { return 0; }
		abstract void onSessionOpen(int sessionid, NetAddress addrClient);
		abstract void onSessionClose(int sessionid, ErrorCode errcode);
		abstract int onSessionDataRecv(int sessionid, byte[] data, int offset, int len);
		
		public void setListenAddr(NetAddress addrListen, BindPolicy bindPolicy)
		{
			this.addrListen = addrListen;
			this.bindPolicy = bindPolicy;
		}
		public NetAddress getListenAddr()
		{
			return addrListen;
		}
		public void setListenBacklog(int backlog)
		{
			listenBacklog = backlog;
		}
		public int getListenBacklog()
		{
			return listenBacklog;
		}
		public BindPolicy getBindPolicy()
		{
			return bindPolicy;
		}
		public NetAddress getServerAddr()
		{
			return imp.getServerAddr();
		}
		
		public void closeSession(int sessionid)
		{
			imp.closeSession(sessionid);
		}
		public void sendSessionData(int sessionid, final ByteBuffer bb)
		{
			imp.sendSessionData(sessionid, bb);
		}

		private NetAddress addrListen;
		private int listenBacklog;
		private BindPolicy bindPolicy;
	}
	public abstract class TCPClient extends PeerHandle<TCPClientImp>
	{
		public TCPClient() { imp = NetManager.this.imp.newTCPClientImp(this); }
		public int getMaxConnectionIdleTime() { return 0; }
		public abstract int onDataRecv(byte[] data, int offset, int len);
		
		public void setServerAddr(NetAddress addrServer)
		{
			this.addrServer = addrServer;
		}
		
		public NetAddress getServerAddr()
		{
			return addrServer;
		}
		public NetAddress getClientAddr()
		{
			return imp.getClientAddr();
		}
		public void sendData(final ByteBuffer bb)
		{
			imp.sendData(bb);
		}
		
		private NetAddress addrServer;
	}
	public abstract class UDPPeer extends PeerHandle<UDPPeerImp>
	{
		public UDPPeer() { imp = NetManager.this.imp.newUDPPeerImp(this); }
		abstract void onDataRecv(NetAddress addrRemote, byte[] data, int offset, int len);
		
		public void setBindAddr(NetAddress addrBind, BindPolicy bindPolicy)
		{
			this.addrBind = addrBind;
			this.bindPolicy = bindPolicy;
		}
		public NetAddress getBindAddr()
		{
			return addrBind;
		}
		public BindPolicy getBindPolicy()
		{
			return bindPolicy;
		}
		public void setConnectAddr(NetAddress addrConnect)
		{
			this.addrConnect = addrConnect;
		}
		public NetAddress getConnectAddr()
		{
			return addrConnect;
		}
		public NetAddress getBoundAddr()
		{
			return imp.getBoundAddr();
		}
		public void sendData(final NetAddress addrTarget/*ignored by connected peer*/, final ByteBuffer bb)
		{
			imp.sendData(addrTarget, bb);
		}
		public void sendData(ByteBuffer bb)
		{
			sendData(null, bb);
		}
		private NetAddress addrBind;
		private BindPolicy bindPolicy;
		private NetAddress addrConnect;
	}
	
	public boolean start()
	{
		return imp.start();
	}
	
	// destroy
	public void destroy()
	{
		imp.destroy();
	}
	
	public void checkIdleConnections()
	{
		imp.checkIdleConnections();
	}
	
	public void getStatistic(Statistic stat)
	{
		imp.getStatistic(stat);
	}
	
	private NetManagerImp imp;
}

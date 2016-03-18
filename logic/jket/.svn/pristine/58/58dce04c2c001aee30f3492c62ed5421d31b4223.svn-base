
package ket.kio;

import java.io.IOException;

import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kio.NetManager.TCPClientImp;
import ket.kio.NetManager.TCPServerImp;
import ket.kio.NetManager.UDPPeerImp;
import ket.util.ExecutorTool;

class NetManagerMTSelectImp implements NetManager.NetManagerImp
{
	public static final int RECV_BUF_SIZE = 8192;
	private static enum ManagerState
	{
		eSNull, eSWorking
	}
	private static enum PeerState // TODO
	{
		eSClosed, eSOpening, eSOpen, eSClosing
	}
	class SelectGroup
	{
		private class Worker implements Runnable
		{
			@Override	
			public void run()
			{		
				try
				{
					if( state != ManagerState.eSWorking )
						return;
					Iterator<Peer<?>> iter = peers.iterator();
					while( iter.hasNext() )
					{
						Peer<?> p = iter.next();
						try
						{
							int ops = p.interestOps();
							if( ops == 0 )
								throw new Error("impossible");
							if( p.sk == null )
								p.sk = p.sc.register(selector, ops, p);				
							else if( p.sk.interestOps() != ops )
								p.sk.interestOps(ops);
						}
						catch(IOException ex)
						{
							iter.remove();
							p.completeClose(ErrorCode.eError);
						}
					}
					selector.select();
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					for(SelectionKey key : selectedKeys)
					{
						Peer<?> p = (Peer<?>)key.attachment();
						try
						{
							if( p.update(key) )
							{
								peers.remove(p);
								p.completeClose(ErrorCode.eOK);
							}
						}
						catch(ConnectException ex)
						{
							peers.remove(p);
							p.completeClose(ErrorCode.eConnectRefused);
						}
						catch(NoRouteToHostException ex)
						{
							peers.remove(p);
							p.completeClose(ErrorCode.eConnectCantReach);
						}
						catch(PortUnreachableException ex)
						{
							peers.remove(p);
							p.completeClose(ErrorCode.eConnectCantReach);
						}
						catch(IOException ex)
						{
							peers.remove(p);
							p.completeClose(ErrorCode.eError);
						}
					}
					selectedKeys.clear();
					if( state == ManagerState.eSWorking )
						executor.execute(this);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		void putTask(Task task)
		{
			task.submit(executor);
			selector.wakeup();
		}
		
		SelectGroup(final String name)
		{
			this.name = name;
		}
		
		boolean open()
		{
			try 
			{			
				executor = ExecutorTool.newSingleThreadExecutor("IOThread" + name);
				selector = Selector.open();
				executor.execute(new Worker());
				return true;
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return false;
		}
		
		void close()
		{
			if( selector != null )
				selector.wakeup();
			if( executor != null )
				executor.shutdown();
			
			try
			{
				while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
			}
			catch(Exception ex)
			{			
			}
			for(Peer<?> peer : peers)
				peer.completeClose(ErrorCode.eShutdown);
			try
			{
				selector.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		void addPeer(final ServerSession session)
		{
			putTask(new Task(){
				@Override
				public void run()
				{
					peers.add(session);
					session.server.sessions.put(session.id, session); // TODO
					session.completeOpen();
				}
			});
		}
		
		void removePeer(final ServerSession session)
		{
			putTask(new Task(){
				@Override
				public void run()
				{
					synchronized( session.state )
					{
						if( session.state == PeerState.eSClosing || session.state == PeerState.eSClosed )
							return;
					}
					if( session.doClose() )
						return;
					peers.remove(session);
					session.completeClose(ErrorCode.eActiveClose);
				}});
		}
		
		void sendPeerData(final ServerSession session, final ByteBuffer bb)
		{
			putTask(new Task(){
				@Override
				public void run()
				{
					if( session.iAddrLocal == null || session.bGracefulClose )
						return;
					session.lstSend.add(bb);
				}});
		}
		
		void checkIdleConnections()
		{
			putTask(new Task(){
				@Override
				public void run()
				{
					for(Peer<?> peer : peers)
						peer.checkIdle();
				}
			});
		}
		
		private Set<Peer<?>> peers = new HashSet<Peer<?>>();
		private Selector selector;
		private ExecutorService executor;
		private final String name;
	}
	
	abstract class Peer<SC extends SelectableChannel> implements NetManager.Peer
	{		
		Peer(SelectGroup sg)
		{
			this.sg = sg;
		}
		abstract void onOpen();
		abstract void onOpenFailed(ErrorCode errcode);
		abstract void onClose(ErrorCode errcode);

		void checkIdle() { }
		
		@Override
		public void getStatistic(Statistic stat)
		{
			stat.nBytesRecv = this.stat.nBytesRecv;
			stat.nBytesSend = this.stat.nBytesSend;
		}
		
		void increBytesRecv(int n)
		{
			stat.nBytesRecv += n;
			synchronized( NetManagerMTSelectImp.this.stat )
			{
				NetManagerMTSelectImp.this.stat.nBytesRecv += n;
			}
		}
		
		void increBytesSend(int n)
		{
			stat.nBytesSend += n;
			synchronized( NetManagerMTSelectImp.this.stat )
			{
				NetManagerMTSelectImp.this.stat.nBytesSend += n;
			}
		}
		
		void closeChannel()
		{
			try { sc.close(); } catch(IOException ex) {	}
		}
		
		@Override
		public void open()
		{
			synchronized( state )
			{
				if( state != PeerState.eSClosed )
					return;
				state = PeerState.eSOpening;
			}
			
			if( ! doOpen() )
			{
				completeClose(ErrorCode.eFailed);
				return;
			}

			sg.putTask(new Task(){
				@Override
				public void run()
				{
					sg.peers.add(Peer.this);
					if( Peer.this.iAddrLocal != null )
						Peer.this.completeOpen();
				}	
				@Override
				public void cancel()
				{
					Peer.this.completeClose(ErrorCode.eFailed);
				}
			});
		}
		
		@Override
		public void close()
		{
			synchronized( state )
			{
				if( state == PeerState.eSClosed || state == PeerState.eSClosing )
					return;
				state = PeerState.eSClosing;
			}
			
			sg.putTask(new Task(){
				@Override
				public void run()
				{
					if( ! sg.peers.contains(Peer.this) )
						return;
					if( Peer.this.doClose() )
						return;
					sg.peers.remove(Peer.this);
					Peer.this.completeClose(ErrorCode.eActiveClose);
				}
			});
		}
		
		void completeOpen()
		{
			addrLocal = new NetAddress(iAddrLocal);
			state = PeerState.eSOpen;
			onOpen();
		}
		
		void completeClose(ErrorCode errcode)
		{
			doCompleteClose();
			if( sk != null )
			{
				sk.cancel();
				sk = null;
			}
			closeChannel();
			state = PeerState.eSClosed;
			if( iAddrLocal != null )
			{
				onClose(errcode);
				iAddrLocal = null;
			}
			else
				onOpenFailed(errcode);
		}
		
		@Override
		public boolean isOpen()
		{
			return iAddrLocal != null;
		}
		
		void doCompleteClose() { }
		abstract boolean doOpen();
		boolean doClose() { return false; }
		abstract int interestOps() throws IOException;
		abstract boolean update(SelectionKey key) throws IOException;
		
		volatile PeerState state = PeerState.eSClosed;
		NetAddress addrLocal;
		volatile InetSocketAddress iAddrLocal;
		SC sc;
		SelectionKey sk;
		final SelectGroup sg;
		Statistic stat = new Statistic();
	}
	
	abstract class Session extends Peer<SocketChannel>
	{			
		Session(SelectGroup sg)
		{
			super(sg);
		}
		static final int BUF_SIZE = 32768;
		@Override
		boolean doClose()
		{
			bGracefulClose = true;
			return true;
		}
				
		@Override
		void doCompleteClose()
		{
			clear();
		}
		
		@Override
		void checkIdle() 
		{
			long max = getMaxConnectionIdleTime();
			if( max == 0 || bGracefulClose )
				return;
			if( System.currentTimeMillis() > lastRecvTime + max )
				bGracefulClose = true;
		}
		abstract int getMaxConnectionIdleTime();
		void clear()
		{
			lstSend.clear();
			lastRecvTime = 0;
			bGracefulClose = false;
			buffer.clear();
			posBuffer = 0;
		}
		
		@Override
		int interestOps() throws IOException
		{
			if( bGracefulClose && lstSend.isEmpty() && ! sc.socket().isOutputShutdown() )
			{
				sc.socket().shutdownOutput();
			}
			int ops = SelectionKey.OP_READ;
			if( ! lstSend.isEmpty() )
				ops |= SelectionKey.OP_WRITE;
			return ops;
		}
		
		@Override
		boolean update(SelectionKey key) throws IOException
		{
			if( key.isReadable() )
			{				
				while( true )
				{
					int remaining = buffer.remaining();
					int read = sc.read(buffer);
					if( read == -1 )
						return true;
					else if( read != 0 )
					{
						increBytesRecv(read);
						lastRecvTime = System.currentTimeMillis();
						int datalen = buffer.position() - posBuffer;
						int ret = onSessionDataRecv(buffer.array(), posBuffer, datalen);
						if( ret == -1 )
							throw new IOException(); // TODO
						if( ret == datalen )
						{
							buffer.position(0);
							posBuffer = 0;
						}
						else
						{
							posBuffer = buffer.position() - ( datalen - ret);
							if( read == remaining )
							{
								if( posBuffer == 0 )
									throw new IOException(); // TODO
								int newpos = datalen - ret;
								System.arraycopy(buffer.array(), posBuffer, buffer.array(), 0, newpos);
								posBuffer = 0;
								buffer.position(newpos);
							}
						}
					}
					if( read < remaining )
						break;
				}
			}
			if( key.isWritable() )
			{
				Iterator<ByteBuffer> iter = lstSend.iterator();
				while( iter.hasNext() )
				{
					ByteBuffer bb = iter.next();
					if( bb.hasRemaining() )
					{
						int ret = sc.write(bb);
						increBytesSend(ret);
						if( bb.hasRemaining() )
							break;
					}
					iter.remove();
				}
			}
			return false;
		}
		
		abstract int onSessionDataRecv(byte[] data, int offset, int len);
		
		// send data
		void sendSessionData(final ByteBuffer bb)
		{
			if( ! bb.hasRemaining() )
				return;
			sg.putTask(new Task(){
				@Override
				public void run()
				{
					if( ! sg.peers.contains(Session.this) )
						return;
					if( Session.this.iAddrLocal == null || Session.this.bGracefulClose )
						return;
					Session.this.lstSend.add(bb);
				}
			});
		}
		
		boolean bGracefulClose = false;
		long lastRecvTime = 0;
		List<ByteBuffer> lstSend = new LinkedList<ByteBuffer>();
		ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
		int posBuffer = 0;
	}
	
	class ServerSession extends Session
	{		
		ServerSession(int id, TCPServer server, SelectGroup sg)
		{
			super(sg);
			this.id = id;
			this.server = server;
		}
		
		@Override
		int onSessionDataRecv(byte[] data, int offset, int len)
		{
			return server.peer.onSessionDataRecv(id, data, offset, len);
		}

		@Override
		void onOpen()
		{	
			server.peer.onSessionOpen(id, addrRemote); // TODO
		}

		@Override
		void onClose(ErrorCode errcode)
		{	
			server.peer.onSessionClose(id, errcode);
		}
		
		@Override
		void doCompleteClose()
		{
			super.doCompleteClose();
			server.sessions.remove(id);
		}
		
		@Override
		public int getMaxConnectionIdleTime()
		{
			return server.getMaxConnectionIdleTime();
		}
		
		@Override
		void onOpenFailed(ErrorCode errcode)
		{
			throw new Error("impossible");
		}
		
		@Override
		void increBytesRecv(int n)
		{
			super.increBytesRecv(n);
			server.stat.nBytesRecv += n;
		}
		
		@Override
		void increBytesSend(int n)
		{
			super.increBytesSend(n);
			server.stat.nBytesSend += n;
		}

		@Override
		boolean doOpen()
		{
			throw new Error("impossible");
		}
		
		void setRemoteAddress(NetAddress addrRemote)
		{
			this.addrRemote = addrRemote;
		}
		
		final int id;
		final TCPServer server;
		NetAddress addrRemote;
	}
	
	public class TCPServer extends Peer<ServerSocketChannel> implements NetManager.TCPServerImp
	{
	
		TCPServer(NetManager.TCPServer peer, SelectGroup sg)
		{
			super(sg);
			this.peer = peer;
		}
		
		@Override
		void onOpen() { peer.onOpen(); }
		@Override
		void onOpenFailed(ErrorCode errcode) { peer.onOpenFailed(errcode); }
		@Override
		void onClose(ErrorCode errcode) { peer.onClose(errcode); }
		
		public int getMaxConnectionIdleTime()
		{
			return peer.getMaxConnectionIdleTime();
		}
		
		@Override
		public NetAddress getServerAddr()
		{
			return addrLocal;
		}		
		
		@Override
		boolean doOpen()
		{
			try
			{
				sc = ServerSocketChannel.open();
				SocketTool.setNonBlocking(sc);
				iAddrLocal = SocketTool.bind(sc, peer.getListenAddr(), peer.getListenBacklog(), peer.getBindPolicy());
				return iAddrLocal != null;
			}
			catch(IOException ex)
			{
			}
			return false;
		}
		
		@Override
		int interestOps()
		{
			return SelectionKey.OP_ACCEPT;
		}
		
		@Override
		boolean update(SelectionKey key) throws IOException
		{
			if( ! key.isAcceptable() )
				return false;
			SocketChannel newsc = sc.accept();
			try
			{
				int sid = sidSeed.incrementAndGet();
				SelectGroup sg = sgsSS[sid%sgsSS.length];
				ServerSession session = new ServerSession(sid, this, sg); // TODO
				SocketTool.setNonBlocking(newsc);
				session.sc = newsc;
				session.iAddrLocal = SocketTool.getLocalAddress(newsc);
				if( session.iAddrLocal == null )
				{
					newsc.close();
				}
				else
				{
					session.setRemoteAddress(new NetAddress(SocketTool.getRemoteAddress(newsc)));
					sg.addPeer(session);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			return false;
		}
		
		@Override
		public void closeSession(final int sessionid)
		{
			sg.putTask(new Task(){ /*TODO*/
				@Override
				public void run()
				{
					if( ! sg.peers.contains(TCPServer.this) )
						return;
					ServerSession session = TCPServer.this.sessions.get(sessionid);
					if( session == null )
						return;
					session.sg.removePeer(session);
				}
			});
		}
		
		@Override
		public void sendSessionData(final int sessionid, final ByteBuffer bb)
		{
			if( ! bb.hasRemaining() )
				return;
			sg.putTask(new Task(){
				@Override
				public void run()
				{
					if( ! sg.peers.contains(TCPServer.this) )
						return;
					ServerSession session = TCPServer.this.sessions.get(sessionid);
					if( session == null )
						return;
					session.sg.sendPeerData(session, bb);
				}
			});
		}
		
		private NetManager.TCPServer peer;
		private Map<Integer, ServerSession> sessions = new ConcurrentHashMap<Integer, ServerSession>();
	}
	
	public class TCPClient extends Session implements NetManager.TCPClientImp
	{
		TCPClient(NetManager.TCPClient peer, SelectGroup sg)
		{
			super(sg);
			this.peer = peer;
		}
		
		@Override
		void onOpen() { peer.onOpen(); }
		@Override
		void onOpenFailed(ErrorCode errcode) { peer.onOpenFailed(errcode); }
		@Override
		void onClose(ErrorCode errcode) { peer.onClose(errcode); }
		
		@Override
		public void sendData(final ByteBuffer bb)
		{
			sendSessionData(bb);
		}
		
		@Override
		int onSessionDataRecv(byte[] data, int offset, int len)
		{
			return peer.onDataRecv(data, offset, len);
		}
		
		@Override
		public NetAddress getClientAddr()
		{
			return addrLocal;
		}
		
		@Override
		boolean doOpen()
		{
			if( peer.getServerAddr() == null )
				return false;
			try
			{
				sc = SocketChannel.open();
				SocketTool.setNonBlocking(sc);
				iAddrLocal = SocketTool.connect(sc, peer.getServerAddr());
				return true;
			}
			catch(IOException ex)
			{
			}
			return false;
		}
		
		@Override
		int interestOps() throws IOException
		{
			if( iAddrLocal == null )
				return SelectionKey.OP_CONNECT;
			return super.interestOps();
		}
		
		@Override
		boolean update(SelectionKey key) throws IOException
		{
			if( iAddrLocal == null )
			{
				if( key.isConnectable() )
				{
					if( ! sc.finishConnect() )
						throw new ConnectException();
					iAddrLocal = SocketTool.getLocalAddress(sc);
					if( iAddrLocal == null )
						throw new BindException();
					completeOpen();
				}
				return false;
			}
			return super.update(key);
		}
		
		NetManager.TCPClient peer;

		@Override
		int getMaxConnectionIdleTime()
		{
			return peer.getMaxConnectionIdleTime();
		}
	}
	
	public class UDPPeer extends Peer<DatagramChannel> implements NetManager.UDPPeerImp
	{	
		static final int MAX_PACKET_SIZE = 8192;
		
		UDPPeer(NetManager.UDPPeer peer, SelectGroup sg)
		{
			super(sg);
			this.peer = peer;
		}
		
		@Override
		void onOpen() { peer.onOpen(); }
		@Override
		void onOpenFailed(ErrorCode errcode) { peer.onOpenFailed(errcode); }
		@Override
		void onClose(ErrorCode errcode) { peer.onClose(errcode); }
		
		@Override
		boolean doOpen()
		{
			try
			{
				sc = DatagramChannel.open();
				SocketTool.setNonBlocking(sc);
				iAddrLocal = SocketTool.bind(sc, peer.getBindAddr(), peer.getBindPolicy());
				if( iAddrLocal == null )
					return false;
				iAddrConnect = SocketTool.connect(sc, peer.getConnectAddr());
				return true;
			}
			catch(IOException ex)
			{
				iAddrLocal = null;
			}
			return false;
		}
		
		@Override
		void doCompleteClose()
		{
			lstSend.clear();
		}
		
		@Override
		public void sendData(final NetAddress addrTarget/*ignored by connected peer*/, final ByteBuffer bb)
		{
			if( ! bb.hasRemaining() )
				return;
			if( bb.remaining() > RECV_BUF_SIZE )
				throw new IllegalArgumentException();
			sg.putTask(new Task(){
				@Override
				public void run()
				{
					if( ! sg.peers.contains(UDPPeer.this) )
						return;
					if( iAddrConnect != null )
						lstSend.add(new AddressedData(bb, iAddrConnect));
					else if( addrTarget != null )
						lstSend.add(new AddressedData(bb, addrTarget));
					else
						throw new IllegalStateException();
				}
			});
		}
		
		@Override
		int interestOps()
		{
			int ops = SelectionKey.OP_READ;
			if( ! lstSend.isEmpty() )
				ops |= SelectionKey.OP_WRITE;
			return ops;
		}
		
		@Override
		boolean update(SelectionKey key) throws IOException
		{
			if( key.isReadable() )
			{				
				while( true )
				{
					buffer.clear();
					SocketAddress sa = sc.receive(buffer);
					if( sa == null || ! (sa instanceof InetSocketAddress))
						break;
					InetSocketAddress insa = (InetSocketAddress)sa;
					NetAddress addrRemote = new NetAddress(insa);
					increBytesRecv(buffer.position());
					peer.onDataRecv(addrRemote, buffer.array(), 0, buffer.position());
				}
			}
			if( key.isWritable() )
			{
				Iterator<AddressedData> iter2 = lstSend.iterator();
				while( iter2.hasNext() )
				{						
					AddressedData pair = iter2.next();
					int ret = sc.send(pair.data, pair.addr);
					if( ret > 0 )
					{
						increBytesSend(ret);
						iter2.remove();
					}
					else
						break;
				}
			}
			return false;
		}
		
		@Override
		public NetAddress getBoundAddr()
		{
			return addrLocal;
		}		
		
		NetManager.UDPPeer peer;
		private InetSocketAddress iAddrConnect;
		private List<AddressedData> lstSend = new LinkedList<AddressedData>();
		private ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
	}
	
	NetManagerMTSelectImp()
	{
		sgMain = new SelectGroup("Main");
		sgsSS = new SelectGroup[Runtime.getRuntime().availableProcessors()];
		//sgsSS = new SelectGroup[4];
		for(int i = 0; i < sgsSS.length; ++i)
			sgsSS[i] = new SelectGroup("SS" + Integer.toString(i));
	}
	
	@Override
	public TCPServerImp newTCPServerImp(NetManager.TCPServer peer)
	{
		return new TCPServer(peer, sgMain);
	}
	@Override
	public TCPClientImp newTCPClientImp(NetManager.TCPClient peer)
	{
		return new TCPClient(peer, sgMain);
	}
	@Override
	public UDPPeerImp newUDPPeerImp(NetManager.UDPPeer peer)
	{
		return new UDPPeer(peer, sgMain);
	}
	
	@Override
	public boolean start()
	{
		synchronized( state )
		{
			if( state != ManagerState.eSNull )
				throw new IllegalStateException();
		}
		state = ManagerState.eSWorking;
		if( ! sgMain.open() )
			return false;
		for(SelectGroup sg : sgsSS)
		{
			if( ! sg.open() )
				return false;
		}
		return true;
	}
	
	@Override
	public void destroy()
	{
		synchronized( state )
		{
			if( state == ManagerState.eSNull )
				return;
			state = ManagerState.eSNull;
		}
		for(SelectGroup sg : sgsSS)
		{
			sg.close();
		}
		sgMain.close();
	}
	
	@Override
	public void checkIdleConnections()
	{
		sgMain.checkIdleConnections();
		for(SelectGroup sg : sgsSS)
		{
			sg.checkIdleConnections();
		}
	}
	
	@Override
	public void getStatistic(Statistic stat)
	{
		synchronized( this.stat )
		{
			stat.nBytesRecv = this.stat.nBytesRecv;
			stat.nBytesSend = this.stat.nBytesSend;
		}
	}
	
	private Statistic stat = new Statistic();
	private AtomicInteger sidSeed = new AtomicInteger();

	private SelectGroup sgMain;
	private SelectGroup[] sgsSS;
	
	private volatile ManagerState state = ManagerState.eSNull;
}

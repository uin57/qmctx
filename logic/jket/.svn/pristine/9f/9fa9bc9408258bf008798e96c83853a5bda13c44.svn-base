
package ket.kio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import ket.kio.NetManager.TCPClientImp;
import ket.kio.NetManager.TCPServerImp;
import ket.kio.NetManager.UDPPeerImp;
import ket.util.ExecutorTool;

public class NetManagerAsyncImp implements NetManager.NetManagerImp
{
	private static enum ManagerState
	{
		eSNull, eSWorking
	}
	private static enum PeerState // TODO
	{
		eSClosed, eSOpening, eSOpen, eSClosing
	}
	@Override
	public TCPServerImp newTCPServerImp(NetManager.TCPServer peer)
	{
		return new TCPServer(peer);
		//return selectImp.newTCPServerImp(peer);
	}

	@Override
	public TCPClientImp newTCPClientImp(NetManager.TCPClient peer)
	{
		return new TCPClient(peer);
		//return selectImp.newTCPClientImp(peer);
	}

	@Override
	public UDPPeerImp newUDPPeerImp(NetManager.UDPPeer peer)
	{
		return selectImp.newUDPPeerImp(peer);
	}

	@Override
	public boolean start()
	{
		synchronized( lockerState )
		{
			if( state != ManagerState.eSNull )
				throw new IllegalStateException();
		}
		if( ! selectImp.start() )
			return false;
		try 
		{
			channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
			state = ManagerState.eSWorking;
			return true;
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
		}
		return false;
	}

	@Override
	public void destroy()
	{
		synchronized( lockerState )
		{
			if( state == ManagerState.eSNull )
				return;
			state = ManagerState.eSNull;
		}
		selectImp.destroy();
		
		try
		{
			channelGroup.shutdownNow();
			while( ! channelGroup.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println("after manager shutdown " + Thread.currentThread().getName());
		for(Peer<?> peer : peers)
			peer.completeClose(ErrorCode.eShutdown);
		peers.clear();
	}
	abstract class Peer<AC extends AsynchronousChannel> implements NetManager.Peer
	{		
		abstract void onOpen();
		abstract void onOpenFailed(ErrorCode errcode);
		abstract void onClose(ErrorCode errcode);

		void checkIdle() { }
		
		@Override
		public void open() {}
		
		@Override
		public void getStatistic(Statistic stat)
		{
			stat.nBytesRecv = this.stat.nBytesRecv;
			stat.nBytesSend = this.stat.nBytesSend;
		}
		
		void increBytesRecv(int n)
		{
			stat.nBytesRecv += n;
			NetManagerAsyncImp.this.stat.nBytesRecv += n;
		}
		
		void increBytesSend(int n)
		{
			stat.nBytesSend += n;
			NetManagerAsyncImp.this.stat.nBytesSend += n;
		}
		
		void closeChannel()
		{
			try { if( ac != null && ac.isOpen() ) ac.close(); } catch(Exception ex) {	}
		}
		
		@Override
		public void close()
		{
			synchronized( lockerState )
			{
				if( state == PeerState.eSClosed || state == PeerState.eSClosing )
					return;
				state = PeerState.eSClosing;
			}
			
			new Task(){
				@Override
				public void run()
				{
					if( ! peers.contains(Peer.this) )
						return;
					if( Peer.this.doClose() )
						return;
					peers.remove(Peer.this);
					Peer.this.completeClose(ErrorCode.eActiveClose);
				}
			}.submit(executor);
		}
		
		void completeOpen()
		{
			addrLocal = new NetAddress(iAddrLocal);
			state = PeerState.eSOpen;
			onOpen();
		}
		
		void checkClose(Throwable ex)
		{
			if( peers.remove(this) )
			{
				if( ex instanceof AsynchronousCloseException )
				{
					if( channelGroup.isShutdown() )
						completeClose(ErrorCode.eShutdown);
					else
						completeClose(ErrorCode.eOK);
				}
				else
					completeClose(ErrorCode.eError);
			}
		}
		
		void completeClose(ErrorCode errcode)
		{
			doCompleteClose();
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
		boolean doClose() { return false; }
		
		volatile PeerState state = PeerState.eSClosed;
		final Object lockerState = new Object();
		NetAddress addrLocal;
		volatile InetSocketAddress iAddrLocal;
		AC ac;
		Statistic stat = new Statistic();
	}
	
	abstract class Session extends Peer<AsynchronousSocketChannel>
	{			
		static final int BUF_SIZE = 32768;
		@Override
		boolean doClose()
		{
			bGracefulClose = true;
			return startWrite();
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
			{
				bGracefulClose = true;
				if( ! startWrite() )
				{
					if( peers.remove(this) )
						completeClose(ErrorCode.eError);
				}
			}
		}
		abstract int getMaxConnectionIdleTime();
		void clear()
		{
			lstSend.clear();
			lastRecvTime = 0;
			bGracefulClose = false;
			buffersSending = null;
			buffer.clear();
			posBuffer = 0;
		}

		abstract int onSessionDataRecv(byte[] data, int offset, int len);
		
		// send data
		void sendSessionData(final ByteBuffer bb)
		{
			if( ! bb.hasRemaining() )
				return;
			new Task(){
				@Override
				public void run()
				{
					if( ! peers.contains(Session.this) )
						return;
					if( Session.this.iAddrLocal == null || Session.this.bGracefulClose )
						return;
					Session.this.lstSend.add(bb);
					if( ! startWrite() )
					{
						if( peers.remove(Session.this) )
							Session.this.completeClose(ErrorCode.eError);
					}
				}
			}.submit(executor);
		}
		
		boolean updateRead(int nRead) throws IOException
		{
			if( nRead == -1 )
				return true;
			if( nRead <= 0 )
				return false;
			boolean bFull = buffer.position() == buffer.limit();
			increBytesRecv(nRead);
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
				if( bFull )
				{
					if( posBuffer == 0 )
						throw new IOException(); // TODO
					int newpos = datalen - ret;
					System.arraycopy(buffer.array(), posBuffer, buffer.array(), 0, newpos);
					posBuffer = 0;
					buffer.position(newpos);
				}
			}
			return false;
		}
		
		void readHandler(Integer nRead, Throwable arg0)
		{
			try
			{
				if( nRead > -2 )
				{
					if( updateRead(nRead.intValue()) )
					{
						peers.remove(Session.this);
						completeClose(ErrorCode.eOK);
						return;
					}
					if( startRead() )
						return;
				}
			}
			catch(IOException ex)
			{
			}
			checkClose(arg0);
		}
		
		boolean startRead()
		{
			try
			{
				ac.read(buffer, null, new CompletionHandler<Integer, Void>()
					{
						@Override
						public void completed(Integer nRead, Void att)
						{
							readHandler(channelGroup.isShutdown() ? -2 : nRead, null);
						}
						@Override
						public void failed(Throwable ex, Void att)
						{	
							readHandler(-2, ex);
						}				
					});
			}
			catch(Exception ex)
			{
				return false;
			}
			return true;
		}
		
		void writeHandler(int nWrite, Throwable arg0)
		{
			if( nWrite > -2 )
			{
				if( buffersSending == null || lstSend.isEmpty() ) // TODO
					return;
				increBytesSend(nWrite);
				buffersSending.eat(nWrite);
				buffersSending = null;
				if( startWrite() )
					return;
			}
			checkClose(arg0);
		}
		
		class SendingBuffers
		{
			SendingBuffers()
			{
				bbs = lstSend.toArray(new ByteBuffer[lstSend.size()]);
				remainings = new int[bbs.length];
				for(int i = 0; i < bbs.length; ++i)
					remainings[i] = bbs[i].remaining();
			}
			
			void eat(int nWrite)
			{
				int p = 0;
				while( nWrite > 0)
					nWrite -= remainings[p++];
				if( nWrite < 0 ) --p;
				lstSend.subList(0, p).clear();
			}
			
			ByteBuffer[] bbs;
			int[] remainings;
		}
		
		boolean startWrite()
		{
			if( buffersSending != null )
				return true;
			try
			{
				if( lstSend.isEmpty() )
				{
					if( bGracefulClose )
						ac.shutdownOutput();
					return true;
				}
				//
				buffersSending = new SendingBuffers();
				ac.write(buffersSending.bbs, 0, buffersSending.bbs.length, Long.MAX_VALUE, TimeUnit.SECONDS,
						null, new CompletionHandler<Long, Void>()
						{
							@Override
							public void completed(Long nWrite, Void att)
							{	
								writeHandler(channelGroup.isShutdown() ? -2 : nWrite.intValue() /*TOD*/, null);
							}

							@Override
							public void failed(Throwable arg0, Void att)
							{
								writeHandler(-2, arg0);
							}
					
						});
				
				return true;
			}
			catch(Exception ex)
			{
			}
			return false;
		}
		
		boolean bGracefulClose = false;
		SendingBuffers buffersSending = null;
		long lastRecvTime = 0;
		List<ByteBuffer> lstSend = new LinkedList<ByteBuffer>();
		ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
		int posBuffer = 0;
	}
	public class TCPClient extends Session implements NetManager.TCPClientImp
	{
		TCPClient(NetManager.TCPClient peer)
		{
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
		public void open()
		{
			new Task(){

				@Override
				public void run()
				{

					synchronized( lockerState )
					{
						if( state != PeerState.eSClosed )
							return;
						state = PeerState.eSOpening;
					}
					peers.add(TCPClient.this);
					if( peer.getServerAddr() != null )
					{
						try
						{
							ac = AsynchronousSocketChannel.open(channelGroup);
							ac.connect(SocketTool.newAddress(peer.getServerAddr()),
						               null, new CompletionHandler<Void, Void>()
						               {

										@Override
										public void completed(Void result, Void att)
										{
											openHandler(! channelGroup.isShutdown(), null);
										}

										@Override
										public void failed(Throwable exc, Void att)
										{	
											openHandler(false, exc);
										}
								
						               });
							return;
						}
						catch(Exception ex)
						{
						}
					}					
					if( peers.remove(TCPClient.this) )
						completeClose(ErrorCode.eError);
					
				}
				@Override
				public void cancel()
				{
					completeClose(ErrorCode.eError);
				}
			}.submit(executor);
		}
		
		void openHandler(boolean bOK, Throwable arg0)
		{
			try
			{
				if( bOK )
				{
					iAddrLocal = SocketTool.getLocalAddress(ac);
					if( iAddrLocal != null )
					{
						if( iAddrLocal.getAddress().isAnyLocalAddress() ) // TODO bug of jdk-7u6-windows-x64.exe?
						{
							if( peer.getServerAddr().isLoopback() )
								iAddrLocal = new InetSocketAddress(InetAddress.getLoopbackAddress(), iAddrLocal.getPort());
							else
								iAddrLocal = new InetSocketAddress(InetAddress.getLocalHost(), iAddrLocal.getPort());
						}
						completeOpen();
						if( startRead() )
							return;
					}
				}
			}
			catch(Exception ex)
			{
			}
			checkClose(arg0);
		}
		
		@Override
		int getMaxConnectionIdleTime()
		{
			return peer.getMaxConnectionIdleTime();
		}
		
		NetManager.TCPClient peer;

	}
	class ServerSession extends Session
	{
		ServerSession(int id, TCPServer server)
		{
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
			return server.peer.getMaxConnectionIdleTime();
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
		
		void setRemoteAddress(NetAddress addrRemote)
		{
			this.addrRemote = addrRemote;
		}
		
		final int id;
		final TCPServer server;
		NetAddress addrRemote;
	}
	
	public class TCPServer extends Peer<AsynchronousServerSocketChannel> implements NetManager.TCPServerImp
	{
		TCPServer(NetManager.TCPServer peer)
		{
			this.peer = peer;
		}
		
		@Override
		void onOpen() { peer.onOpen(); }
		@Override
		void onOpenFailed(ErrorCode errcode) { peer.onOpenFailed(errcode); }
		@Override
		void onClose(ErrorCode errcode) { peer.onClose(errcode); }
		
		@Override
		public void open()
		{
			synchronized( lockerState )
			{
				if( state != PeerState.eSClosed )
					return;
				state = PeerState.eSOpening;
			}
			try
			{
				ac = AsynchronousServerSocketChannel.open(channelGroup);
				iAddrLocal = SocketTool.bind(ac, peer.getListenAddr(), peer.getListenBacklog(), peer.getBindPolicy());
				if( iAddrLocal == null )
					throw new BindException();
			}
			catch(Exception ex)
			{
				completeClose(ErrorCode.eError);
				return;
			}
			new Task(){
				@Override
				public void run()
				{
					peers.add(TCPServer.this);
					completeOpen();
					doAccept();
				}	
				@Override
				public void cancel()
				{
					completeClose(ErrorCode.eFailed);
				}
			}.submit(executor);
		}
		
		void doAccept()
		{
			try
			{
				if( ! ac.isOpen() ) return;
				ac.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>()
					{
						@Override
						public void completed(AsynchronousSocketChannel newac, Void att)
						{
							if( channelGroup.isShutdown() )
								return;
							try
							{
								ServerSession session = new ServerSession(++sidSeed, TCPServer.this);
								session.ac = newac;
								session.iAddrLocal = SocketTool.getLocalAddress(newac);
								if( session.iAddrLocal == null )
								{
									newac.close();
								}
								else
								{
									session.setRemoteAddress(new NetAddress(SocketTool.getRemoteAddress(newac)));
									peers.add(session);
									sessions.put(session.id, session);
									session.completeOpen();
									if( ! session.startRead() )
									{
										if( peers.remove(session) )
											session.completeClose(ErrorCode.eError);
									}
								}
							}
							catch(Exception ex)
							{
								//ex.printStackTrace();
							}
							doAccept();
						}
						@Override
						public void failed(Throwable ex, Void att)
						{
							checkClose(ex);
						}						
					});
			}
			catch(Exception ex)
			{
				checkClose(ex);
			}
		}

		@Override
		public void closeSession(final int sessionid)
		{	
			new Task(){
				@Override
				public void run()
				{
					if( ! peers.contains(TCPServer.this) )
						return;
					ServerSession session = TCPServer.this.sessions.get(sessionid);
					if( session == null || session.state == PeerState.eSClosing || session.state == PeerState.eSClosed )
						return;
					if( session.doClose() )
						return;
					peers.remove(session);
					session.completeClose(ErrorCode.eActiveClose);
				}
			}.submit(executor);
		}

		@Override
		public NetAddress getServerAddr()
		{
			return addrLocal;
		}

		@Override
		public void sendSessionData(final int sessionid, final ByteBuffer bb)
		{	
			if( ! bb.hasRemaining() )
				return;
			new Task(){
				@Override
				public void run()
				{
					if( ! peers.contains(TCPServer.this) )
						return;
					ServerSession session = TCPServer.this.sessions.get(sessionid);
					if( session == null || session.iAddrLocal == null || session.bGracefulClose )
						return;
					session.lstSend.add(bb);
					if( ! session.startWrite() )
					{
						if( peers.remove(session) )
							session.completeClose(ErrorCode.eError);
					}
				}
			}.submit(executor);
		}
		
		NetManager.TCPServer peer;
		private Map<Integer, ServerSession> sessions = new HashMap<Integer, ServerSession>();
	}
	
	@Override
	public void checkIdleConnections()
	{
		selectImp.checkIdleConnections();
		new Task(){
			@Override
			public void run()
			{
				for(Peer<?> peer : peers)
					peer.checkIdle();
			}
		}.submit(executor);
	}

	@Override
	public void getStatistic(Statistic stat)
	{
		selectImp.getStatistic(stat);
		stat.nBytesRecv += this.stat.nBytesRecv;
		stat.nBytesSend += this.stat.nBytesSend;
	}

	private NetManagerSelectImp selectImp = new NetManagerSelectImp();
	private ExecutorService executor = ExecutorTool.newSingleThreadExecutor("IOThreadAsync");
	AsynchronousChannelGroup channelGroup = null;
	private int sidSeed;
	private Object lockerState = new Object();
	private volatile ManagerState state = ManagerState.eSNull;
	private Set<Peer<?>> peers = new HashSet<Peer<?>>();
	private Statistic stat = new Statistic();
}

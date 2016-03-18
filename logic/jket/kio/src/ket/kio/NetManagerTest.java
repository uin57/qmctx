
package ket.kio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import ket.test.Assert;
import ket.test.SimpleTimer;

interface PeerWithState
{
	public enum State
	{
		eClosed, eOpening, eOpen
	}
	
	boolean isClosed();
	
	static class VerifiedState
	{
		public synchronized boolean testState(State s)
		{
			return state == s;
		}
		public synchronized void verifyState(State s)
		{
			Assert.assertSame(state, s);
		}
		public synchronized void verifyOnOpen()
		{
			Assert.assertSame(state, State.eOpening);
			state = State.eOpen;
		}
		public synchronized void verifyOnOpenFailed()
		{
			Assert.assertSame(state, State.eOpening);
			state = State.eClosed;
		}
		public synchronized void verifyOnClose()
		{
			Assert.assertSame(state, State.eOpen);
			state = State.eClosed;
		}
		public synchronized boolean verifyDoOpen()
		{
			if( state != State.eClosed )
				return false;
			state = State.eOpening;
			return true;
		}
		State state = State.eClosed;
	}
}
 
class PeersWithState
{
	public synchronized void add(PeerWithState pws)
	{
		list.add(pws);
	}
	
	public synchronized boolean verifyClosed()
	{
		for(PeerWithState pws : list)
			if( ! pws.isClosed() )
				return false;
		return true;
	}
	
	private List<PeerWithState> list = new ArrayList<PeerWithState>();
	
	public static synchronized PeersWithState instance() { return psws; }
	
	private static PeersWithState psws = new PeersWithState();
}
class UDPPeerTest extends NetManager.UDPPeer implements PeerWithState
{

	UDPPeerTest(NetManager manager)
	{
		manager.super();
	}

	@Override
	public void onOpen()
	{
		state.verifyOnOpen();
		System.out.println("UDPPeer " + this + " onOpen(), addr is " 
				+ getConnectAddr() + ", bound " + getBoundAddr());
		if( this.getConnectAddr() != null )
			sendData(ByteBuffer.allocate(20));
	}

	@Override
	public void onOpenFailed(ErrorCode errcode)
	{	
		state.verifyOnOpenFailed();
		System.out.println("UDPPeer " + this + " onOpenFailed(" + errcode + ") " + Thread.currentThread().getName());
	}

	@Override
	public void onClose(ErrorCode errcode)
	{	
		state.verifyOnClose();
		System.out.println("UDPPeer " + this + " onClose(" + errcode + ") " + Thread.currentThread().getName());
	}
	
	@Override
	void onDataRecv(NetAddress addrRemote, byte[] data, int offset, int len)
	{
		state.verifyState(State.eOpen);
		System.out.println("UDPPeer " + this + " onDataRecv(" + addrRemote + ", size is " + len + ")");
		if( len != 0 )
			sendData(addrRemote, ByteBuffer.allocate(len - 1));
	}
	
	@Override
	public void open()
	{
		if( ! state.verifyDoOpen() )
			return;
		PeersWithState.instance().add(this);
		super.open();
	}
	
	@Override
	public boolean isClosed()
	{
		return state.testState(State.eClosed);
	}

	private VerifiedState state = new VerifiedState();
}

class TCPServerTest extends NetManager.TCPServer implements PeerWithState
{

	TCPServerTest(NetManager manager)
	{
		manager.super();
	}

	@Override
	public void onOpen()
	{
		state.verifyOnOpen();
		System.out.println("TCPServer " + this + " onOpen(), listen " 
				+ getListenAddr() + ", bound " + getServerAddr());
	}

	@Override
	public void onOpenFailed(ErrorCode errcode)
	{	
		state.verifyOnOpenFailed();
		System.out.println("TCPServer " + this + " onOpenFailed(" + errcode + ")");
	}

	@Override
	public void onClose(ErrorCode errcode)
	{	
		state.verifyOnClose();
		System.out.println("TCPServer " + this + " onClose(" + errcode + ") " + Thread.currentThread().getName());
	}

	@Override
	void onSessionOpen(int sessionid, NetAddress addrClient)
	{	
		synchronized( sessions )
		{
			Assert.assertTrue(sessions.add(sessionid));
		}
		System.out.println("TCPServer " + this + " onSessionOpen(" + sessionid + ", " + addrClient + ") " + Thread.currentThread().getName());
	}

	@Override
	void onSessionClose(int sessionid, ErrorCode errcode)
	{	
		synchronized( sessions )
		{
			Assert.assertTrue(sessions.remove(sessionid));
		}
		System.out.println("TCPServer " + this + " onSessionClose(" + sessionid + ", " + errcode + ") " + Thread.currentThread().getName());
	}
	
	@Override
	int onSessionDataRecv(int sessionid, byte[] data, int offset, int len)
	{	
		synchronized( sessions )
		{
			Assert.assertTrue(sessions.contains(sessionid));
		}
		System.out.println("TCPServer " + this + " onSessionDataRecv(" + sessionid + ", size is " + len + ") " + Thread.currentThread().getName());
		if( len > 2 )
			sendSessionData(sessionid, ByteBuffer.allocate(len - 1));
		else if( len != 0 )
			closeSession(sessionid);
		return len;
	}

	@Override
	public void open()
	{
		if( ! state.verifyDoOpen() )
			return;
		PeersWithState.instance().add(this);
		super.open();
	}
	
	@Override
	public boolean isClosed()
	{
		if( ! state.testState(State.eClosed) )
			return false;
		synchronized( sessions )
		{
			return sessions.isEmpty();
		}
	}

	private VerifiedState state = new VerifiedState();
	private Set<Integer> sessions = new HashSet<Integer>();
}

class TCPClientTest extends NetManager.TCPClient implements PeerWithState
{

	TCPClientTest(NetManager manager, CountDownLatch cdl)
	{
		manager.super();
		this.cdl = cdl;
	}

	@Override
	public void onOpen()
	{
		state.verifyOnOpen();
		System.out.println("TCPClient " + this + " onOpen(), local " 
				+ getClientAddr() + ", server " + getServerAddr());
		sendData(ByteBuffer.allocate(100));
	}

	@Override
	public void onOpenFailed(ErrorCode errcode)
	{	
		state.verifyOnOpenFailed();
		System.out.println("TCPClient " + this + " onOpenFailed(" + errcode + ")");		
	}

	@Override
	public void onClose(ErrorCode errcode)
	{	
		state.verifyOnClose();
		System.out.println("TCPClient " + this + " onClose(" + errcode + ") " + Thread.currentThread().getName());
		if( cdl != null )
			cdl.countDown();
	}
	
	@Override
	public int onDataRecv(byte[] data, int offset, int len)
	{
		state.verifyState(State.eOpen);
		System.out.println("TCPClient " + this + " onDataRecv(size is " + len + ")");
		if( len > 2 )
			sendData(ByteBuffer.allocate(len - 1));
		else if( len != 0 )
			close();
		return len;
	}
	
	@Override
	public void open()
	{
		if( ! state.verifyDoOpen() )
			return;
		PeersWithState.instance().add(this);
		super.open();
	}
	
	@Override
	public boolean isClosed()
	{
		return state.testState(State.eClosed);
	}

	private CountDownLatch cdl;
	private VerifiedState state = new VerifiedState();
}

public class NetManagerTest
{
	NetManager manager = new NetManager(NetManager.NetManagerType.eMTSelectNetManager);
	
	public static void main(String[] args)
	{
		new NetManagerTest().doTest();
	}

	public void doTest()
	{
		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler()
				{
					@Override
					public void uncaughtException(Thread arg0, Throwable arg1)
					{
						//arg1.printStackTrace();
					}
			
				});
		manager.start();
		if( System.console() != null )
			System.out.println("there is a console");
		
		System.out.println("begin udp test");		
		UDPPeerTest upt1 = new UDPPeerTest(manager);
		upt1.open();
		UDPPeerTest upt2 = new UDPPeerTest(manager);
		upt2.setConnectAddr(new NetAddress("127.0.0.1", 8000));
		upt2.open();
		UDPPeerTest upt3 = new UDPPeerTest(manager);
		upt3.setBindAddr(new NetAddress("127.0.0.1", 8000), BindPolicy.eReuseTimewait);
		upt3.open();
		UDPPeerTest upt4 = new UDPPeerTest(manager);
		upt4.setBindAddr(new NetAddress("0.0.0.0", 0), BindPolicy.eReuseTimewait);
		upt4.setConnectAddr(new NetAddress("127.0.0.1", 8000));
		upt4.open();
		
		UDPPeerTest upt5 = new UDPPeerTest(manager);
		upt5.setConnectAddr(new NetAddress("0.0.0.0", 0));
		upt5.open();
		//ket.util.FileSys.pause(false);
		System.out.println("end udp test");
		
		SimpleTimer stTCP = new SimpleTimer("tcptest");
		System.out.println("begin tcp test");
		TCPServerTest tst1 = new TCPServerTest(manager);
		tst1.open();
		TCPServerTest tst3 = new TCPServerTest(manager);
		tst3.setListenAddr(new NetAddress("127.0.0.1", 8000), BindPolicy.eReuseTimewait);
		tst3.open();
		
		TCPServerTest tst4 = new TCPServerTest(manager);
		tst4.setListenAddr(new NetAddress("0.0.0.0", 2000), BindPolicy.eReuseTimewait);
		tst4.open();
		
		TCPClientTest tct1 = new TCPClientTest(manager, null);
		tct1.setServerAddr(new NetAddress("172.16.10.57", 80));
		tct1.open();
		final int nTestClient = 100;
		CountDownLatch cdl = new CountDownLatch(nTestClient);
		for(int i = 0; i < nTestClient; ++i)
		{
			TCPClientTest tct2 = new TCPClientTest(manager, cdl);
			tct2.setServerAddr(new NetAddress("127.0.0.1", 8000));
			tct2.open();
		}
		try { cdl.await(); } catch(InterruptedException ex) { ex.printStackTrace(); }
		stTCP.done();
		System.out.println("end tcp test " + Thread.currentThread().getName());		
		manager.destroy();
		Assert.assertTrue(PeersWithState.instance().verifyClosed());
	}
}

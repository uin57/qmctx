
package ket.kpc.tracker;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ket.kio.NetAddress;
import ket.kpc.SBean;
import ket.pack.Digest;
import ket.kpc.common.PeerID;
import ket.kpc.rpc.Packet;

final class DataManager
{
private static final int UPS_TEST_MAX_WAIT_TIME = 15 * 1000;
	
	private static class UPSTest
	{
		public boolean isOK()
		{
			return addrMain != null && addrMain.equals(addrAssistant);
		}
		
		public void addResponse(NetAddress addr, boolean bAssistant)
		{
			if( bAssistant )
				addrAssistant = addr;
			else
				addrMain = addr;
		}
		
		public boolean isExpired()
		{
			return System.currentTimeMillis() >= timeExpire + UPS_TEST_MAX_WAIT_TIME;
		}
		
		private long timeExpire = System.currentTimeMillis();
		private NetAddress addrMain = null;
		private NetAddress addrAssistant = null;
	}
	

	
	private class UPSTestingTask implements Runnable
	{
		UPSTestingTask(long key)
		{
			this.key = key;
		}
		@Override
		public void run()
		{
			PeerID peerid = null;
			int sid;
			synchronized( DataManager.this )
			{
				PeerInfo pinfo = mapPeers.get(key);
				if( pinfo == null || ! pinfo.isLogon() ||  pinfo.stateTest != PeerInfo.TestState.ePTSUPSTesting )
					return;
				sid = pinfo.sid;
				try
				{
					UPSTest utr = getUPSTest(key);
					if( utr == null || utr.isExpired() )
					{
						pinfo.stateTest = PeerInfo.TestState.ePTSNull;
						peerid = new PeerID();
					}
					else if( utr.isOK() )
					{
						pinfo.peerid.addr = utr.addrMain;
						pinfo.stateTest = PeerInfo.TestState.ePTSNull;
						pinfo.peerid.setFlag(PeerID.PF_UPS);
						peerid = pinfo.peerid;
					}
					else
						tracker.getExecutor().schedule(this, 250, TimeUnit.MILLISECONDS);
				}
				catch(RejectedExecutionException ex)
				{					
				}
			}
			if( peerid != null )
				tracker.getTCPTrackerServer().sendPacket(sid, new Packet.T2P.UPSTestResponse(peerid.isNull() ? 1 : 0, peerid));
		}	
		
		public long key;
	}

	private static class DigestInfo
	{
		public DigestInfo(Digest digest)
		{
			this.digest = digest;
			peers = new HashMap<Long, PeerInfo>();
			superPeers = new HashMap<Long, PeerInfo>();
		}
		
		public Digest digest;
		public Map<Long, PeerInfo> peers;
		public Map<Long, PeerInfo> superPeers;
	}
	
	private static class PeerInfo
	{
		public PeerInfo(PeerID peerid, int sid)
		{
			this.peerid = peerid;
			this.sid = sid;
			digests = new ArrayList<DigestInfo>();
		}
		
		public static enum TestState
		{
			ePTSNull,
			ePTSTPSTesting,
			ePTSUPSTesting
		}
		
		public boolean isLogon()
		{
			return peerid.isTTC();
		}
		
		public TestState stateTest = TestState.ePTSNull;
		final public int sid;
		public PeerID peerid;
		public final List<DigestInfo> digests;
		public LocalGroup lg = null;
		public SBean.LocalPeerID localPeerID = null;
		public int load = 0;
	}
		
	private static class DataGroup
	{
		public long ownerid;
		public int membercount;
		public List<Digest> members = new ArrayList<Digest>();
		
		public boolean isOK()
		{
			return membercount == members.size();
		}
	}
	static enum TCPTestState
	{
		eOK, eFailed, eTesting
	}
	static class LocalNetwork
	{
		Map<Long, PeerInfo> peers = new TreeMap<Long, PeerInfo>();
		long headID;
		Map<String, TCPTestState> tcpTestMap = new HashMap<String, TCPTestState>();
	}
	private class LocalGroup
	{	
		String getNetworkName(String ip)
		{
			return ip.substring(0, ip.lastIndexOf("."));
		}
		
		public void addPeer(PeerInfo pinfo)
		{
			String nname = getNetworkName(pinfo.localPeerID.localIP);
			LocalNetwork nw = networks.get(nname);
			if( nw == null )
			{
				nw = new LocalNetwork();
				nw.headID = pinfo.localPeerID.peerID;
				networks.put(nname, nw);
			}
			PeerInfo oldLPID = nw.peers.put(pinfo.localPeerID.peerID, pinfo);
			if( oldLPID != null )
			{
				Logger.getLogger("kpx").error("tracker: dup local peerid " + pinfo.localPeerID.peerID + ", ip is "
						+ pinfo.localPeerID.localIP);
				return;
			}
			if( nw.peers.size() > 1 )
			{
				SBean.LocalNetworkNotice lnn = new SBean.LocalNetworkNotice();
				PeerInfo headPInfo = nw.peers.get(nw.headID);
				lnn.head = headPInfo.localPeerID;
				lnn.members = new ArrayList<SBean.LocalPeerID>();
				lnn.members.add(pinfo.localPeerID);
				tracker.noticeLocalNetwork(headPInfo.sid, lnn);
			}
			else if( ! nw.peers.isEmpty() )
				nw.headID = pinfo.localPeerID.peerID;
		}
		
		public void removePeer(long key, String ip)
		{
			String nname = getNetworkName(ip);
			LocalNetwork nw = networks.get(nname);
			if( nw != null )
			{
				nw.peers.remove(key);
				if( nw.headID == key )
				{
					Iterator<Map.Entry<String, TCPTestState>> iter = nw.tcpTestMap.entrySet().iterator();
					while( iter.hasNext() )
					{
						Map.Entry<String, TCPTestState> e = iter.next();
						if( e.getValue() != TCPTestState.eOK )
							iter.remove();
					}
					if( nw.peers.size() > 1 )
					{
						SBean.LocalNetworkNotice lnn = new SBean.LocalNetworkNotice();
						PeerInfo newHeadPInfo = nw.peers.entrySet().iterator().next().getValue();
						lnn.head = newHeadPInfo.localPeerID;
						lnn.members = new ArrayList<SBean.LocalPeerID>();
						for(Map.Entry<Long, PeerInfo> e : nw.peers.entrySet())
							lnn.members.add(e.getValue().localPeerID);
						lnn.members.remove(lnn.head);
						nw.headID = lnn.head.peerID;
						tracker.noticeLocalNetwork(newHeadPInfo.sid, lnn);
					}
					else if( ! nw.peers.isEmpty() )
						nw.headID = nw.peers.entrySet().iterator().next().getValue().localPeerID.peerID;
				}
			}
		}
		
		public boolean testTCP(String ipClient, String ipServer)
		{
			String nwClient = getNetworkName(ipClient);
			String nwServer = getNetworkName(ipServer);
			if( nwClient.equals(nwServer) )
				return true;
			LocalNetwork ln = networks.get(nwClient);
			if( ln == null )
				return false;
			TCPTestState b = ln.tcpTestMap.get(nwServer);
			if( b != null )
			{
				return b == TCPTestState.eOK;
			}
			else
			{
				long now = System.currentTimeMillis(); 
				if( now >= lastTCPTestTime + 1000 /*TODO*/ )
				{
					lastTCPTestTime = now;
					PeerInfo headPInfo = ln.peers.get(ln.headID);
					SBean.LocalPeerID headClient = headPInfo.localPeerID;
					SBean.LocalPeerID targetServer = null;
					LocalNetwork lnServer = networks.get(nwServer);
					if( lnServer != null )
					{
						for(PeerInfo pi : lnServer.peers.values())
						{
							if( pi.peerid.isSuper() )
								continue;
							targetServer = pi.localPeerID;
							break;
						}
					}
					if( headClient != null && targetServer != null)
					{
						SBean.NetworkTCPTest ntt = new SBean.NetworkTCPTest();
						ntt.headID = ln.headID;
						ntt.addr = new NetAddress(targetServer.localIP, targetServer.localTCPPort);					
						ln.tcpTestMap.put(nwServer, TCPTestState.eTesting);
						tracker.testNetworkTCP(headPInfo.sid, ntt);
					}
				}
				// TODO start testing !!
				return false;
			}
		}
		
		public void dump()
		{
			for(Map.Entry<String, LocalNetwork> e : networks.entrySet())
			{
				System.out.printf("\tnetwork: %s\n", e.getKey());
				LocalNetwork ln = e.getValue();
				for(PeerInfo pinfo : ln.peers.values())
				{
					System.out.printf("\t\tpeer %5d %s %6d %6d\n",
							(int)(0xffff&(pinfo.localPeerID.peerID>>>32)), pinfo.localPeerID.localIP, pinfo.localPeerID.localTCPPort, pinfo.localPeerID.localUDPPort);
				}
				for(Map.Entry<String, TCPTestState> e2 : ln.tcpTestMap.entrySet())
				{
					System.out.printf("\t\ttest network %s 's tcp server, result is %s\n"
							, e2.getKey(), e2.getValue());
				}
			}
		}	
		
		private Map<String, LocalNetwork> networks = new HashMap<String, LocalNetwork>();
		private long lastTCPTestTime = 0;
	}
	
	public DataManager(Tracker tracker)
	{
		this.tracker = tracker;
	}
	
	public SBean.Section createDataGroup(long key, Digest group, int membercount)
	{
		SBean.Section section = null;
		synchronized( this )
		{
			PeerInfo pinfo = mapPeers.get(key);
			if( pinfo == null )
				return null;
			DataGroup dg = mapDataGroup.get(group);
			int sid[] = new int[1];
			if( dg == null || ! dg.isOK() && getLogonPeer(dg.ownerid, sid) == null )
			{
				dg = new DataGroup();
				dg.ownerid = key;
				dg.membercount = membercount;
				mapDataGroup.put(group, dg);
				section = new SBean.Section(0, Math.min(membercount, 200));
			}
		}
		return section;
	}
	
	public SBean.Section setDataGroupMembers(long key, Digest group, SBean.Section section, List<Digest> members)
	{
		SBean.Section sectionNext = null;
		synchronized( this )
		{
			PeerInfo pinfo = mapPeers.get(key);
			if( pinfo == null )
				return null;
			DataGroup dg = mapDataGroup.get(group);
			if( dg == null || dg.ownerid != key || dg.members.size() != section.offset
					|| members.size() != section.count || section.offset + section.count > dg.membercount )
				return null;
			dg.members.addAll(members);
			if( ! dg.isOK() )
				sectionNext = new SBean.Section(dg.members.size(), Math.min(dg.membercount - dg.members.size(), 200));
			else
				tracker.getLogger().info("tracker: set data groupmemebers, " + pinfo.peerid.kuid + " create data group ok, size is " + dg.membercount);
		}
		return sectionNext;
	}
	
	public synchronized PeerID login(int sid, SBean.PeerLoginRequest req)
	{
		PeerInfo pinfo = mapPeers.get(req.KUID);
		if( pinfo != null )
			return null;
		
		final PeerID peerid = new PeerID(req.KUID);
		peerid.flag = PeerID.PF_TTC;
		if( (req.peerFlag & PeerID.PF_SUPER) != 0 )
			peerid.flag |= PeerID.PF_SUPER;
		peerid.addr.host = req.pubIP;
		// TODO
		LocalGroup lg = null;
		if( peerid.addr.isLoopback() || peerid.addr.isPrivate() )
			lg = theLocalGroup;
		else
		{
			lg = localGroups.get(peerid.addr.host);
			if( lg == null )
				lg = new LocalGroup();
			localGroups.put(peerid.addr.host, lg);
		}
		SBean.LocalPeerID lpi = new SBean.LocalPeerID(req.KUID, req.localIP, req.localTCPPort, req.localUDPPort);
		pinfo = new PeerInfo(peerid, sid);		
		pinfo.lg = lg;
		pinfo.localPeerID = lpi;
		lg.addPeer(pinfo);
		mapPeers.put(req.KUID, pinfo);
		return peerid;
	}
	
	public synchronized PeerID getLogonPeer(long key, int[] sid)
	{
		PeerInfo pinfo = mapPeers.get(key);
		if( pinfo != null && pinfo.isLogon() )
		{
			sid[0] = pinfo.sid;
			return pinfo.peerid;
		}
		return null;
	}
	
	public synchronized PeerID setPeerTCPServerTestResult(long key, int port, boolean bTestOK, int[] sid)
	{
		PeerInfo pinfo = mapPeers.get(key);
		if( pinfo != null && pinfo.isLogon() && pinfo.stateTest == PeerInfo.TestState.ePTSTPSTesting )
		{
			pinfo.stateTest = PeerInfo.TestState.ePTSNull;
			if( bTestOK )
			{
				pinfo.peerid.addr.port = port;
				pinfo.peerid.setFlag(PeerID.PF_TPS);
			}
			sid[0] = pinfo.sid;
			return pinfo.peerid;
		}
		return null;
	}
	
	public void requestTPSTest(long key, int port)
	{
		TPSTestClient client = null;
		int sid = 0;
		synchronized( this )
		{
			PeerInfo pinfo = mapPeers.get(key);
			if( pinfo == null )
				return;
			sid = pinfo.sid;

			if( pinfo.isLogon() && ! pinfo.peerid.isTPS() && pinfo.stateTest == PeerInfo.TestState.ePTSNull )
			{
				if( tracker.getTCPTrackerServer().getServerAddr().isAnyLocal() && pinfo.peerid.addr.isPrivate()
						|| tracker.getTCPTrackerServer().getServerAddr().isAnyLocal() && pinfo.peerid.addr.isLoopback()
						|| tracker.getTCPTrackerServer().getServerAddr().isLoopback() && pinfo.peerid.addr.isLoopback()
						|| tracker.getTCPTrackerServer().getServerAddr().isPrivate() && pinfo.peerid.addr.isPrivate()
						|| ! tracker.getTCPTrackerServer().getServerAddr().isPrivate() && ! pinfo.peerid.addr.isPrivate()
				)
				{
					client = new TPSTestClient(tracker, pinfo.peerid.kuid, false);
					client.setServerAddr(new NetAddress(pinfo.peerid.addr.host, port));
					pinfo.stateTest = PeerInfo.TestState.ePTSTPSTesting;
				}
			}
		}
		
		if( client == null )
		{
			tracker.getTCPTrackerServer().sendPacket(sid, new Packet.T2P.TPSTestResponse(new PeerID()));
			return;
		}
		
		client.open();
	}
	
	public void requestUPSTest(long key)
	{
		Packet.T2P.UPSTestQuestion utq = null;
		int sid = 0;
		synchronized( this )
		{
			PeerInfo pinfo = mapPeers.get(key);
			if( pinfo == null )
				return;
			sid = pinfo.sid;
			if( pinfo.isLogon() && ! pinfo.peerid.isTPS() 
					&& ! pinfo.peerid.isUPS() && pinfo.stateTest == PeerInfo.TestState.ePTSNull )
			{
				utq = new Packet.T2P.UPSTestQuestion(tracker.getAssistantUDPTrackerServer().getBoundAddr().port);
				pinfo.stateTest = PeerInfo.TestState.ePTSUPSTesting;
			}
		}
		if( utq == null )
			tracker.getTCPTrackerServer().sendPacket(sid, new Packet.T2P.UPSTestResponse(1, new PeerID()));
		else
		{
			addUPSTest(key, new UPSTest());
			try
			{
				tracker.getExecutor().execute(new UPSTestingTask(key));
				tracker.getTCPTrackerServer().sendPacket(sid, utq);
			}
			catch(RejectedExecutionException ex)
			{				
			}
		}
	}
	
	public void addUPSTestResponse(long id, NetAddress addr, boolean bAssistant)
	{
		synchronized( mapUPSTests )
		{
			UPSTest utr = mapUPSTests.get(id);
			if( utr != null )
				utr.addResponse(addr, bAssistant);
		}
	}
	
	public void addUPSTest(long key, UPSTest upstest)
	{
		synchronized( mapUPSTests )
		{
			if( mapUPSTests.containsKey(key) )
				mapUPSTests.remove(key);
			mapUPSTests.put(key, upstest);
		}
	}
	
	public UPSTest getUPSTest(long key)
	{
		synchronized( mapUPSTests )
		{
			UPSTest upstest = mapUPSTests.get(key);
			if( upstest != null && ( upstest.isExpired() || upstest.isOK()) )
				mapUPSTests.remove(key);
			return upstest;
		}
	}
	
	
	public synchronized void addSource(long key, Digest digest)
	{			
		PeerInfo pi = mapPeers.get(key);
		if( pi == null )
			return;
		
		DigestInfo di = mapDigests.get(digest);
		if( di == null )
		{
			di = new DigestInfo(digest);
			mapDigests.put(digest, di);
		}
		
		if( ! di.peers.containsKey(key) )
		{
			di.peers.put(key, pi);
			if( pi.peerid.isSuper() )
				di.superPeers.put(key, pi);
			pi.digests.add(di);
		}
	}
	
	private class GroupAddSourceTask implements Runnable
	{
		public static final int WAIT_TIMES = 8;
		public static final int WAIT_INTERVAL = 500;
		
		public GroupAddSourceTask(long key, Digest group, List<Integer> lst)
		{
			this.key = key;
			this.group = group;
			this.lst = lst;
		}
		
		@Override
		public void run()
		{
			int[] sid = new int[1];
			if( getLogonPeer(key, sid) == null )
				return;
			GroupAddSourceTask gast = null;
			synchronized( DataManager.this )
			{
				PeerInfo pi = mapPeers.get(key);
				if( pi == null )
					return;
				
				DataGroup dg = mapDataGroup.get(group);
				if( dg == null || ! dg.isOK() )
				{
					if( ++waittimes <= WAIT_TIMES )
						gast = this;
				}
				else
				{
					if( lst.isEmpty() ) // all
					{
						for(Digest digest : dg.members )
						{
							DigestInfo di = mapDigests.get(digest);
							if( di == null )
							{
								di = new DigestInfo(digest);
								mapDigests.put(digest, di);
							}
																							
							if( ! di.peers.containsKey(key) )
							{
								di.peers.put(key, pi);
								if( pi.peerid.isSuper() )
									di.superPeers.put(key, pi);
								pi.digests.add(di);
							}
						}
					}
					else
					{
						for(Integer i : lst)
						{
							if( i < 0 || i >= dg.membercount )
								continue;
							Digest digest = dg.members.get(i);
							DigestInfo di = mapDigests.get(digest);
							if( di == null )
							{
								di = new DigestInfo(digest);
								mapDigests.put(digest, di);
							}							
									
							if( ! di.peers.containsKey(key) )
							{
								di.peers.put(key, pi);
								if( pi.peerid.isSuper() )
									di.superPeers.put(key, pi);
								pi.digests.add(di);
							}
						}
					}
				}
			}
			if( gast != null )
			{
				try
				{
					tracker.getExecutor().schedule(gast, GroupAddSourceTask.WAIT_INTERVAL, TimeUnit.MILLISECONDS);
				}
				catch(Exception ex)
				{					
				}
			}
		}
		
		private long key;
		private Digest group;
		private List<Integer> lst;
		private int waittimes = 0;
	}
	
	public void groupAddSource(long key, Digest group, List<Integer> lst)
	{
		GroupAddSourceTask gast = null;
		synchronized( this )
		{
			PeerInfo pi = mapPeers.get(key);
			if( pi == null )
				return;
			
			DataGroup dg = mapDataGroup.get(group);
			if( dg == null || ! dg.isOK() )
			{
				gast = new GroupAddSourceTask(key, group, lst);
			}
			else
			{
				if( lst.isEmpty() ) // all
				{
					for(Digest digest : dg.members )
					{
						DigestInfo di = mapDigests.get(digest);
						if( di == null )
						{
							di = new DigestInfo(digest);
							mapDigests.put(digest, di);
						}													
								
						if( ! di.peers.containsKey(key) )
						{
							di.peers.put(key, pi);
							if( pi.peerid.isSuper() )
								di.superPeers.put(key, pi);
							pi.digests.add(di);
						}
					}
				}
				else
				{
					for(Integer i : lst)
					{
						if( i < 0 || i >= dg.membercount )
							continue;
						Digest digest = dg.members.get(i);
						DigestInfo di = mapDigests.get(digest);
						if( di == null )
						{
							di = new DigestInfo(digest);
							mapDigests.put(digest, di);
						}						
								
						if( ! di.peers.containsKey(key) )
						{
							di.peers.put(key, pi);
							if( pi.peerid.isSuper() )
								di.superPeers.put(key, pi);
							pi.digests.add(di);
						}
					}
				}
			}
		}
		if( gast != null )
		{
			try
			{
				tracker.getExecutor().schedule(gast, GroupAddSourceTask.WAIT_INTERVAL, TimeUnit.MILLISECONDS);
			}
			catch(Exception ex)
			{					
			}
		}
	}
	
	public synchronized boolean removeSource(long key, Digest digest)
	{	
		DigestInfo di = mapDigests.get(digest);
		if( di == null )
			return true;
		PeerInfo pi = mapPeers.get(key);
		if( pi == null )
			return true;
		if( ! di.peers.containsKey(key) )
			return true;
		
		pi.digests.remove(di);
		
		di.peers.remove(key);
		if( pi.peerid.isSuper() )
			di.superPeers.remove(key);
		if( di.peers.isEmpty() )
			mapDigests.remove(digest);
		return true;
	}
	
	public synchronized void onPeerLoadReport(long key, SBean.PeerLoadInfo loadInfo)
	{
		PeerInfo pi = mapPeers.get(key);
		if( pi == null )
			return;
		pi.load = loadInfo.load;
	}
	
	private int selectProviders(Map<Long, PeerInfo> peers, PeerInfo piDemander, List<PeerID> providers, int countMax, boolean bSuper)
	{
		class PriorPeer implements Comparable<PriorPeer>
		{
			public PriorPeer(PeerID pid, int load)
			{
				this.pid = pid;
				this.load = load;
			}
			
			@Override
			public int compareTo(PriorPeer otherPP)
			{
				return otherPP.load - load;
			}
			
			public PeerID pid;
			public int load;
		}
		PriorityQueue<PriorPeer> queue = new PriorityQueue<PriorPeer>(4);
		for(PeerInfo pi : peers.values())
		{
			PeerID peeridProvider = pi.peerid;
			if( pi.load >= 100 )
				continue;
			if( peeridProvider.isTPS() ||
				piDemander.peerid.isTPS() ||
				! tracker.getConfig().bUPSDisable &&
				peeridProvider.isUPS() &&
				piDemander.peerid.isUPS() )
			{
				if( bSuper )
				{
					queue.add(new PriorPeer(peeridProvider, pi.load));
				}
				else if( ! peeridProvider.isSuper() )
				{
					PeerID peeridAdd = peeridProvider;
					LocalGroup lg = piDemander.lg;
					if( lg != null && lg == pi.lg )
					{
						if( peeridProvider.isTPS() )
						{
							if( piDemander.peerid.isTPS() )
							{
								if( ! lg.testTCP(pi.localPeerID.localIP, piDemander.localPeerID.localIP) )
									peeridAdd = null;
								else if( ! lg.testTCP(piDemander.localPeerID.localIP, pi.localPeerID.localIP) )
								{
									peeridAdd = new PeerID();
									peeridAdd.kuid = peeridProvider.kuid;
									peeridAdd.addr = peeridProvider.addr;
									peeridAdd.flag = peeridProvider.flag;
									peeridAdd.clearFlag(PeerID.PF_TPS);
								}
							}
							else
							{
								if( ! lg.testTCP(piDemander.localPeerID.localIP, pi.localPeerID.localIP) )
									peeridAdd = null;
							}
						} 
						else if( piDemander.peerid.isTPS() )
						{
							if( ! lg.testTCP(pi.localPeerID.localIP, piDemander.localPeerID.localIP) )
								peeridAdd = null;
						}
					}
					if( peeridAdd != null )
						queue.add(new PriorPeer(peeridAdd, pi.load));
				}
			}
			if( queue.size() >= countMax ) // TODO
				break;
		}
		for(PriorPeer pp : queue) // TODO
			providers.add(pp.pid);
		return providers.size();
	}
	
	public synchronized int querySource(Digest digest, long key, List<PeerID> providers)
	{
		DigestInfo di = mapDigests.get(digest);
		if( di == null )
			return 0;
		PeerInfo piDemander = mapPeers.get(key);
		if( piDemander == null )
			return 0;
		List<PeerID> superProviders = new ArrayList<PeerID>();
		selectProviders(di.superPeers, piDemander, superProviders, 1/*TODO*/, true);
		selectProviders(di.peers, piDemander, providers, 4/*TODO*/, false);		
		providers.addAll(superProviders);
		return providers.size();
	}	
	
	public synchronized void setTestNetworkTCPResult(int id, String hostServer, boolean ok)
	{
		PeerInfo pinfo = mapPeers.get(id);
		if( pinfo == null )
			return;
		LocalGroup lg = pinfo.lg;
		if( lg == null )
			return;
		LocalNetwork ln = lg.networks.get(lg.getNetworkName(pinfo.localPeerID.localIP));
		if( ln == null )
			return;
		String nwServer= lg.getNetworkName(hostServer);
		ln.tcpTestMap.put(nwServer, ok ? TCPTestState.eOK : TCPTestState.eFailed);
	}
	
	public synchronized void removePeer(long key)
	{
		PeerInfo pinfo = mapPeers.remove(key);
		if( pinfo == null )
			return;
		for(DigestInfo di : pinfo.digests)
		{
			di.peers.remove(key);
			if( pinfo.peerid.isSuper() )
				di.superPeers.remove(key);
			if( di.peers.isEmpty() )
				mapDigests.remove(di.digest);
		}
		// TODO
		LocalGroup lg = pinfo.lg;
		if( lg != null )
			lg.removePeer(key, pinfo.localPeerID.localIP);
		//
	}
	
	public synchronized void dump() {
		System.out
				.println("\n================================================================");
		System.out
				.println("==================    tracker maps    ==========================");
		System.out
				.println("================================================================");
		System.out.printf("%s, %5d peers, %8d digests\n\n",
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
				mapPeers.size(), mapDigests.size());
		//int i = 0;
		if (mapPeers.size() <= 4) {
			for (PeerInfo pi : mapPeers.values()) {
				//++i;
				System.out.printf("peer %24s %s %6d\n", pi.peerid,
						pi.peerid.getFlagString(), pi.digests.size());
			}
			/*
			 * System.out.println(); i = 0; for(DigestHead dh :
			 * mapDigestHeads.values()) { ++i; System.out.println("digest " + i
			 * + "(" + dh.nclient + "):\t" +
			 * StringConverter.getHexStringFromBytes(dh.digest.getBytes()) +
			 * "\t(" + dh.digest.datalen + ")"); }
			 */
			System.out.println("dump the local group:");
			theLocalGroup.dump();
			for (Map.Entry<String, LocalGroup> e : localGroups.entrySet()) {
				System.out.println("dump the local group " + e.getKey() + ":");
				e.getValue().dump();
			}
		}
		System.out
				.println("================================================================");
	}
	
	private Tracker tracker;
	private Map<Long, PeerInfo> mapPeers = new HashMap<Long, PeerInfo>();
	private Map<Digest, DigestInfo> mapDigests = new HashMap<Digest, DigestInfo>();
	private Map<Digest, DataGroup> mapDataGroup = new HashMap<Digest, DataGroup>();
	private Map<Long, UPSTest> mapUPSTests = new HashMap<Long, UPSTest>();
	private LocalGroup theLocalGroup = new LocalGroup();
	private Map<String, LocalGroup> localGroups = new HashMap<String, LocalGroup>();
}

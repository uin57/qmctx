// modified by ket.kio.RPCGen at Thu Oct 11 16:51:48 CST 2012.

package ket.kpc.peer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import ket.kio.BindPolicy;
import ket.kio.NetAddress;
import ket.kio.NetManager;
import ket.kio.SimplePacket;
import ket.kiox.ILanService;
import ket.kpc.SBean;
import ket.pack.Digest;
import ket.kpc.common.PeerID;
import ket.kpc.rpc.Packet;
import ket.util.Stream;
import ket.util.Stream.BytesOutputStream;

class RPCManager
{
	
	public static final class Config
	{
		public NetAddress addrPeerBind = new NetAddress();
	}
	
	private static final long TTC_RECONNECT_INTERVAL = 10 * 1000;
	private static final int UPS_TEST_RESPONSE_COUNT = 3;
	
	private static final int MAX_LOAD_VALUE = 100;
	private static final int MAX_UPLOADING_PEER_COUNT = 8;
	private static final int MAX_OVERFLOW_UPLOADING_PEER_COUNT = 4;
	private static final int MAX_UPLOADING_SUPER_PEER_COUNT = 8400;
	private static final int MAX_OVERFLOW_UPLOADING_SUPER_PEER_COUNT = 1600;
	private static final int LOAD_REPORT_INTERVAL = 10000;

	private class TickTask implements Runnable
	{
		@Override
		public void run()
		{
			long now = System.currentTimeMillis();
			if( ttcReconnectTime != 0 )
			{
				if( ttc.isOpen() )
					ttcReconnectTime = 0;
				else if( now >= ttcReconnectTime )
				{
					ttcReconnectTime = 0;
					ttc.open();
				}
			}
			managerNet.checkIdleConnections();						
			// TODO
			localNetwork.onTimer();
		}
		
		public ScheduledFuture<?> future = null;
		
	}
	
	private class OpenFailedTask  implements Runnable
	{		
		public OpenFailedTask(PeerID peeridRemote, int aid)
		{
			this.peeridRemote = peeridRemote;
			this.aid = aid;
		}
		
		@Override
		public void run()
		{
			onPeerSessionActiveOpenFailed(peeridRemote, aid);
		}
		
		private PeerID peeridRemote;
		private int aid;
	}

	public class PunchTask implements Runnable
	{
		private static final int PUNCH_TIME = 3 * 1000;
		private static final int PUNCH_INTERVAL = 500;
		public PunchTask(NetAddress addr)
		{
			this.addr = addr;
		}
		
		public PunchTask(NetAddress addr, SimplePacket packet)
		{
			this.addr = addr;
			this.packet = packet;
		}
		
		@Override
		public void run()
		{
			if( System.currentTimeMillis() < timeStart + PUNCH_TIME )
			{
				ups.sendPacket(addr, new Packet.P2P.UPSPunch());
				System.out.println("punch " + addr);
				try
				{
					peer.getExecutor().schedule(this, PUNCH_INTERVAL, TimeUnit.MILLISECONDS);
				}
				catch(RejectedExecutionException ex)
				{
				}
			}
			else if( packet != null )
				ups.sendPacket(addr, packet);
		}
		
		private NetAddress addr;
		private long timeStart = System.currentTimeMillis();
		private SimplePacket packet = null;
	}	
	
	private class CloseTask implements Runnable
	{		
		public CloseTask(int psid, PeerID peeridRemote)
		{
			this.psid = psid;
			this.peeridRemote = peeridRemote;
		}
		
		@Override
		public void run()
		{
			onPeerSessionClose(psid, peeridRemote);
		}
		
		private int psid;
		private PeerID peeridRemote;
	}
	
	private class CheckSessionTask2 implements Runnable
	{
		@Override
		public void run()
		{
			// check timeout active open sessions
			List<ActiveConnecting> timeoutActiveConnectings = new ArrayList<ActiveConnecting>();
			synchronized( lockerSession )
			{
				Iterator<Map.Entry<Integer, ActiveConnecting>> iter = activeConnectings.entrySet().iterator();
				while( iter.hasNext() )
				{
					ActiveConnecting ac = iter.next().getValue();
					if( ac.isTimeout() )
					{
						timeoutActiveConnectings.add(ac);
						iter.remove();
					}
				}
			}
			for(ActiveConnecting ac : timeoutActiveConnectings)
			{
				addTask(new OpenFailedTask(ac.peeridRemote, ac.aid), true);
			}
		}
		
		public ScheduledFuture<?> future = null;
		
	}
	
	private class RegisterResponseTask2 implements Runnable
	{
		public RegisterResponseTask2(int psid, Digest digest, int rank)
		{
			this.psid = psid;
			this.digest = digest;
			this.rank = rank;
		}
		
		@Override
		public void run()
		{
			onPeerRegisterResponse(psid, digest, rank);
		}
		
		private int psid;
		private Digest digest;
		private int rank;
	}
	

	private class UploadResponseTask2 implements Runnable
	{
		public UploadResponseTask2(int sessionid, Digest digest, boolean bOK)
		{
			this.sessionid = sessionid;
			this.digest = digest;
			this.bOK = bOK;
		}
		
		@Override
		public void run()
		{
			onPeerUploadResponse(sessionid, digest, bOK);
		}
		
		private int sessionid;
		private Digest digest;
		private boolean bOK;
	}
	
	private class DataResponseTask2 implements Runnable
	{
		public DataResponseTask2(int sessionid, Digest digest, SBean.Section section, ByteBuffer data)
		{
			this.sessionid = sessionid;
			this.digest = digest;
			this.section = section;
			this.data = data;
		}
		
		@Override
		public void run()
		{
			onPeerDataResponse(sessionid, digest, section, data);
		}
		
		private int sessionid;
		private Digest digest;
		private SBean.Section section;
		private ByteBuffer data;
	}
	
	private enum PeerSessionType
	{
		ePSTTPC,
		ePSTTPS,
		ePSTUPS,
		ePSTRelay
	}
	
	private class PeerSession
	{			
		public PeerSession(int peerSessionID, PeerSessionType type, TCPPeerClient tpc, PeerID peeridRemote)
		{
			this.peerSessionID = peerSessionID;
			this.type = type;
			this.tpc = tpc;
			this.peeridRemote = peeridRemote;
		}
		
		public PeerSession(int peerSessionID, PeerSessionType type, int tpsSessionID, PeerID peeridRemote)
		{
			this.peerSessionID = peerSessionID;
			this.type = type;
			this.tpsSessionID = tpsSessionID;
			this.peeridRemote = peeridRemote;
		}
		
		public PeerSession(int peerSessionID, PeerSessionType type, PeerID peeridRemote)
		{
			this.peerSessionID = peerSessionID;
			this.type = type;
			this.peeridRemote = peeridRemote;
		}		
		
		private int peerSessionID;
		PeerSessionType type;
		TCPPeerClient tpc;
		int tpsSessionID;
		PeerID peeridRemote;
	}
	
	private static class ActiveConnecting
	{
		public static final long ACTIVE_OPEN_TIMEOUT = 30 * 1000;
		
		public ActiveConnecting(int peerSessionID, int aid, PeerID peeridRemote)
		{
			this.peerSessionID = peerSessionID;
			this.aid = aid;
			this.peeridRemote = peeridRemote;
		}
		
		public boolean isTimeout()
		{
			return System.currentTimeMillis() >= timeout;
		}
		
		private int peerSessionID;
		private int aid;
		private PeerID peeridRemote;
		private long timeout = System.currentTimeMillis() + ACTIVE_OPEN_TIMEOUT;
	}
	
	private static class Session
	{		
	}
	
	private static class QueryRequest
	{
		public QueryRequest(Digest digest, Digest group)
		{
			this.digest = digest;
			this.group = group;
		}
		
		private Digest digest;
		private Digest group;
	}
	
	private static class QueryResponse
	{
		private Set<Long> pids = new HashSet<Long>();
		private Map<Long, PeerID> peersOK = new HashMap<Long, PeerID>();
		boolean bOK = false;
	}
	
	private static class RegisterRequest
	{
		private static final int REGISTER_REQ_TIMEOUT = 30 * 1000;
		public RegisterRequest(Digest digest, Digest group)
		{
			this.digest = digest;
			this.group = group;
		}
		
		public boolean isTimeout()
		{
			return System.currentTimeMillis() >= timeout;
		}
		
		public int getAID()
		{
			return aid;
		}
		
		public int getSID()
		{
			return sid;
		}
		
		public void setAID(int aid)
		{
			this.aid = aid;
		}
		
		public void setSID(int sid)
		{
			this.sid = sid;
			aid = -1;
		}
		
		private Digest digest;
		private Digest group;
		private long timeout = System.currentTimeMillis() + REGISTER_REQ_TIMEOUT;
		int sid = -1;
		int aid = -1;
	}
	
	private static class UploadRequest
	{
		private static final int UPLOAD_REQ_TIMEOUT = 30 * 1000;
		public UploadRequest(Digest digest)
		{
			this.digest = digest;
		}
		
		public boolean isTimeout()
		{
			return System.currentTimeMillis() >= timeout;
		}
		
		public int getAID()
		{
			return aid;
		}
		
		public int getSID()
		{
			return sid;
		}
		
		public void setAID(int aid)
		{
			this.aid = aid;
		}
		
		public void setSID(int sid)
		{
			this.sid = sid;
			aid = -1;
		}
		
		private Digest digest;
		private long timeout = System.currentTimeMillis() + UPLOAD_REQ_TIMEOUT;
		private int sid = -1;
		private int aid = -1;
	}
	
	private static class PeerInfo
	{
		public PeerInfo(PeerID peerid)
		{
			this.peerid = peerid;
		}
		
		public static enum GroupSource
		{
			eUnknown, eExist, eNotExist
		}
		
		public boolean isIdle()
		{
			return sessions.isEmpty() && sessionRelay == 0 && reqRegisters.isEmpty() && reqUploads.isEmpty()
				&& uploading.isEmpty() && downloading.isEmpty();
		}
		
		public PeerID peerid;
		public static final long SESSION_MAX_IDLE = 5 * 1000;
		public long timeSessionIdle = 0;
		public Map<Integer, Session> sessions = new HashMap<Integer, Session>();
		public static final long SESSION_RELAY_MAX_IDLE = 30 * 1000;
		public long timeSessionRelayIdle = 0;
		public int sessionRelay = 0;
		public int aidRelayPermit = -1;
		public int aidP2POnly = -1;
		public Map<Digest, RegisterRequest> reqRegisters = new HashMap<Digest, RegisterRequest>();
		public Map<Digest, UploadRequest> reqUploads = new HashMap<Digest, UploadRequest>();
		public static final long UPLOADING_MAX_IDLE = 30 * 1000;
		public Map<Digest, Long> uploading = new HashMap<Digest, Long>();
		public static final long DOWNLOADING_MAX_IDLE = 30 * 1000;
		public Map<Digest, Long> downloading = new HashMap<Digest, Long>();
		public Map<Digest, GroupSource> groups = new HashMap<Digest, GroupSource>();
	}
	
	private class RegisterResponseTask implements Runnable
	{
		public RegisterResponseTask(Digest digest, int rank)
		{
			this.digest = digest;
			this.rank = rank;
		}
		
		@Override
		public void run()
		{
			peer.getDataManager().setRegisterRet(digest, rank);
		}
		
		private Digest digest;
		private int rank;
	}
	
	private class UploadResponseTask implements Runnable
	{
		public UploadResponseTask(PeerID peerid, Digest digest, boolean bOK)
		{
			this.peerid = peerid;
			this.digest = digest;
			this.bOK = bOK;
		}
		
		@Override
		public void run()
		{
			peer.getDataManager().setUploadRet(peerid, digest, bOK);
		}
		
		private PeerID peerid;
		private Digest digest;
		private boolean bOK;
	}
	
	private class DataRequestTask implements Runnable
	{
		public DataRequestTask(int psid, Digest digest, SBean.Section section)
		{
			this.psid = psid;
			this.digest = digest;
			this.section = section;
		}
		
		@Override
		public void run()
		{
			requestData(psid, digest, section);
		}
		
		private int psid;
		private Digest digest;
		private SBean.Section section;
	}	
	
	private class FinishUploadingTask implements Runnable
	{
		public FinishUploadingTask(Digest digest)
		{
			this.digest = digest;
		}
		
		@Override
		public void run()
		{
			peer.getDataManager().finishUpload(digest);
		}
		
		private Digest digest;
	}
	
	private class FinishDownloadingTask implements Runnable
	{
		public FinishDownloadingTask(Digest digest)
		{
			this.digest = digest;
		}
		
		@Override
		public void run()
		{
			peer.getDataManager().finishDownload(digest);
		}
		
		private Digest digest;
	}
	
	private class CheckSessionTask implements Runnable
	{
		@Override
		public void run()
		{
			List<SessionIDAndDigest> upTimeout = new ArrayList<SessionIDAndDigest>();
			List<SessionIDAndDigest> downTimeout = new ArrayList<SessionIDAndDigest>();
			List<Integer> idleSessions = new ArrayList<Integer>();
			int upPeerCount = 0;
			synchronized( lockerRPC )
			{
				Iterator<Map.Entry<Long, PeerInfo>> iterpinfo = peerinfos.entrySet().iterator();
				while( iterpinfo.hasNext() )
				{
					Map.Entry<Long, PeerInfo> epinfo = iterpinfo.next();
					PeerInfo pinfo = epinfo.getValue();
					if( pinfo.isIdle() )
					{
						iterpinfo.remove();
						continue;
					}
					Iterator<RegisterRequest> itr = pinfo.reqRegisters.values().iterator();
					while( itr.hasNext() )
					{
						RegisterRequest req = itr.next();
						if( req.isTimeout() )
						{
							addTask(new RegisterResponseTask(req.digest, 0), true);
							itr.remove();
						}
					}
					Iterator<UploadRequest> itu = pinfo.reqUploads.values().iterator();
					while( itu.hasNext() )
					{
						UploadRequest req = itu.next();
						if( req.isTimeout() )
						{
							addTask(new UploadResponseTask(pinfo.peerid, req.digest, false), true);
							itu.remove();
						}
					}
					long now = System.currentTimeMillis();
					Integer sid = null;
					if( ! pinfo.sessions.isEmpty() )
						sid = pinfo.sessions.keySet().iterator().next();
					boolean bUpTimeout = false;
					Iterator<Map.Entry<Digest, Long>> itup = pinfo.uploading.entrySet().iterator();
					while( itup.hasNext() )
					{
						Map.Entry<Digest, Long> e = itup.next();
						if( e.getValue() < now )
						{
							upTimeout.add(new SessionIDAndDigest(sid, e.getKey()));
							itup.remove();
							bUpTimeout = true;
						}
					}
					if( bUpTimeout && pinfo.uploading.isEmpty() )
						uploadingPeerIDs.remove(epinfo.getKey());
					Iterator<Map.Entry<Digest, Long>> itdown = pinfo.downloading.entrySet().iterator();
					while( itdown.hasNext() )
					{
						Map.Entry<Digest, Long> e = itdown.next();
						if( e.getValue() < now )
						{
							downTimeout.add(new SessionIDAndDigest(sid, e.getKey()));
							itdown.remove();
						}
					}
					boolean bSessionIdle = false;
					if( ! pinfo.sessions.isEmpty() && pinfo.uploading.isEmpty() && pinfo.downloading.isEmpty() )
					{
						bSessionIdle = true;
						for(RegisterRequest req : pinfo.reqRegisters.values())
						{
							if( pinfo.sessions.containsKey(req.getSID()) )
							{
								bSessionIdle = false;
								break;
							}
						}
						for(UploadRequest req : pinfo.reqUploads.values())
						{
							if( pinfo.sessions.containsKey(req.getSID()) )
							{
								bSessionIdle = false;
								break;
							}
						}
					}
					if( bSessionIdle )
					{
						if( pinfo.timeSessionIdle == 0 )
							pinfo.timeSessionIdle = System.currentTimeMillis();
						else
						{
							if( pinfo.timeSessionIdle + PeerInfo.SESSION_MAX_IDLE < System.currentTimeMillis() )
							{
								idleSessions.addAll(pinfo.sessions.keySet());
							}
						}
					}
					else
						pinfo.timeSessionIdle = 0;
					boolean bSessionIdleRelay = false;
					if( pinfo.sessionRelay != 0 )
					{
						bSessionIdleRelay = true;
						for(RegisterRequest req : pinfo.reqRegisters.values())
						{
							if( pinfo.sessionRelay == req.getSID() )
							{
								bSessionIdleRelay = false;
								break;
							}
						}
					}
					if( bSessionIdleRelay )
					{
						if( pinfo.timeSessionRelayIdle == 0 )
							pinfo.timeSessionRelayIdle = System.currentTimeMillis();
						else
						{
							if( pinfo.timeSessionRelayIdle + PeerInfo.SESSION_RELAY_MAX_IDLE < System.currentTimeMillis() )
							{
								idleSessions.add(pinfo.sessionRelay);
							}
						}
					}
					else
						pinfo.timeSessionRelayIdle = 0;
				}
				upPeerCount = uploadingPeerIDs.size();
			}
			//
			for(SessionIDAndDigest sad : upTimeout)
			{
				addTask(new FinishUploadingTask(sad.digest), true);
				if( sad.sessionid != null)
					finishDownload(sad.sessionid, sad.digest);
			}
			for(SessionIDAndDigest sad : downTimeout)
			{
				addTask(new FinishDownloadingTask(sad.digest), true);
				if( sad.sessionid != null)
					finishUpload(sad.sessionid, sad.digest);
			}
			for(Integer psid : idleSessions)
			{
				logger.info("rpcmanager: try close idle session, sid = " + psid);
				closePeerSession(psid);
			}
			//
			long now = System.currentTimeMillis();
			if( now > lastLoadReportTime + LOAD_REPORT_INTERVAL )
			{
				int load = (int)(((upPeerCount/(float)(peer.getConfig().bSuper ? MAX_UPLOADING_SUPER_PEER_COUNT : MAX_UPLOADING_PEER_COUNT)))*MAX_LOAD_VALUE);
				if( load != lastLoadReportVal )
				{
					lastLoadReportVal = load;
					// TODO report load to tracker
					ttc.sendPacket(new Packet.P2T.LoadReport(new SBean.PeerLoadInfo(load)));
				}
				lastLoadReportTime = now;
			}
		}
		
		public ScheduledFuture<?> future = null;
		
	}
	
	private class TrackerSelector
	{
		public void init(final List<NetAddress> trackers)
		{
			Packet.P2T.Ping ping = new Packet.P2T.Ping();
			final int seqOld;
			synchronized( this )
			{
				seq = seedSeq.incrementAndGet();
				this.trackers.clear();
				for(NetAddress addr : trackers)
					this.trackers.add(addr);
				ping.setSeq(seq);
				seqOld = seq;
			}
			try
			{
			peer.getExecutor().schedule(new Runnable(){

				@Override
				public void run()
				{
					synchronized( TrackerSelector.this )
					{
						if( seq == 0 || seq != seqOld )
							return;
					}
					init(trackers);
				}
				}, 1000, TimeUnit.MILLISECONDS);
			}
			catch(Exception ex)
			{				
			}
			for(NetAddress addr : trackers)
				ups.sendPacket(addr, ping);
		}
		
		public void recvPing(NetAddress addr, int seq, int load)
		{
			synchronized( this )
			{
				if( this.seq == 0 || this.seq != seq )
					return;
				if( ! trackers.contains(addr) )
					return;
				this.seq = 0;
				trackers.clear();
			}
			if( ttc.isOpen() )
				return;
			ttc.setServerAddr(addr);
			ttc.open();
		}
		
		private AtomicInteger seedSeq = new AtomicInteger();
		private int seq = 0;
		private Set<NetAddress> trackers = new HashSet<NetAddress>();
	}
	
	private class LocalNetwork
	{
		private static final int KEEPALIVE_INTERVAL = 5000;
		private static final int KEEPALIVE_TIMEOUT = 15000;
		
		private class Member
		{
			public Member(SBean.LocalPeerID id)
			{
				this.id = id;
				digests = new ArrayList<DigestInfo>();
			}
			
			public final SBean.LocalPeerID id;
			public long lastKeepaliveRecv = System.currentTimeMillis();
			public final List<DigestInfo> digests;
		}
		
		private class DigestInfo
		{
			public DigestInfo(Digest digest)
			{
				this.digest = digest;
				peers = new HashMap<Long, Member>();
			}
			
			public final Digest digest;
			public final Map<Long, Member> peers;
		}
		
		private void share(long id, List<Digest> lst)
		{
			Member m = members.get(id);
			if( m == null )
				return;
			for(Digest d : lst)
			{
				DigestInfo di = digests.get(d);
				if( di == null )
				{
					di = new DigestInfo(d);
					digests.put(d, di);
				}
				if( di.peers.containsKey(id) )
					return;
				di.peers.put(id, m);
				m.digests.add(di);
			}
		}
		
		public void recvHeadNotice(SBean.LocalNetworkNotice lnn)
		{
			List<SBean.LocalPeerID> lst = new ArrayList<SBean.LocalPeerID>();
			long collectHeadID = -1;
			synchronized( this )
			{
				if( ! ( headID != null && bHead && headID.peerID == lnn.head.peerID ) )
				{
					bHead = true;
					headID = lnn.head;
					selfID = lnn.head;
					members.clear();
					digests.clear();
					members.put(selfID.peerID, new Member(headID));
					collectHeadID = headID.peerID;
					blackList.clear();
				}
				for(SBean.LocalPeerID lpi : lnn.members)
				{
					if( lpi.peerID == headID.peerID )
						continue;
					Member m = members.put(lpi.peerID, new Member(lpi));
					if( m != null )
					{
						for(DigestInfo di : m.digests)
						{
							di.peers.remove(m.id.peerID);
							if( di.peers.isEmpty() )
								digests.remove(di.digest);
						}
					}
					lst.add(lpi);
					/*
					Member oldLPI = members.put(lpi.peerID, new Member(lpi));
					if( oldLPI == null )
						lst.add(lpi);
					*/
				}
			}
			if( collectHeadID != -1 )
			{
				synchronized( this )
				{
					share(collectHeadID, peer.getDataManager().getAllSharingDigests());
				}
			}
			for(SBean.LocalPeerID lpi : lst)
			{
				ups.sendPacket(new NetAddress(lpi.localIP, lpi.localUDPPort)
					, new Packet.P2P.NoticeLocalNetworkMember(lnn.head, lpi));
			}
		}
		
		public void recvMemberNotice(SBean.LocalPeerID head, SBean.LocalPeerID member, NetAddress addrRemote)
		{
			NetAddress addr = null;
			long id = 0;
			synchronized( this )
			{
				bHead = false;
				headID = head;
				selfID = member;
				members.clear();
				digests.clear();
				addr = new NetAddress(headID.localIP, headID.localUDPPort);
				id = selfID.peerID;
				blackList.clear();
				lastKeepaliveRecv = System.currentTimeMillis();
			}
			shareDigestsToHead(peer.getDataManager().getAllSharingDigests(), addr, id);
		}
		
		private void shareDigestsToHead(List<Digest> sdigests, NetAddress addr, long id)
		{
			final int DIGEST_COUNT = 20;
			int end = sdigests.size();
			int from = 0;
			while( from < end )
			{
				int to = from + DIGEST_COUNT;
				if( to > end )
					to = end;
				ups.sendPacket(addr, new Packet.P2P.LocalNetworkShareUpdate(id,
					sdigests.subList(from, to)));
				from = to;
			}
		}
		
		public void recvKeepaliveReq(long id, NetAddress addrRemote)
		{
			long resid = 0;
			synchronized( this )
			{
				if( headID == null || bHead || headID.peerID != id )
					return;
				lastKeepaliveRecv = System.currentTimeMillis();
				resid = selfID.peerID;
			}
			ups.sendPacket(addrRemote, new Packet.P2P.LocalNetworkKeepaliveRes(resid));
		}
		
		public synchronized void recvKeepaliveRes(long id)
		{
			if( headID == null || ! bHead )
				return;
			if( id == headID.peerID )
				return;
			Member m = members.get(id);
			if( m == null )
				return;
			m.lastKeepaliveRecv = System.currentTimeMillis();
		}

		public boolean isOK()
		{
			return headID != null && selfID != null;
		}
		
		private synchronized void recvLocalNetworkShareUpdate(long id, List<Digest> sdigests)
		{
			if( ! isOK() )
				return;
			logger.info("rpcmanager: recv localnetwork shareupdate from " + id + ", digest count is " + sdigests.size());
			if( bHead )
				share(id, sdigests);
		}
		
		public synchronized void shareDigests(List<Digest> sdigests)
		{
			if( ! isOK() )
				return;
			if( bHead )
				share(headID.peerID, sdigests);
			else
				shareDigestsToHead(sdigests, new NetAddress(headID.localIP, headID.localUDPPort), selfID.peerID);
		}
		
		private int getProviders(Digest digest, List<PeerID> providers)
		{
			DigestInfo di = digests.get(digest);
			if( di == null )
				return 0;
			for(Member m : di.peers.values())
			{
				SBean.LocalPeerID lpi = m.id;
				PeerID pid = new PeerID();
				pid.kuid = lpi.peerID;
				pid.setFlag(PeerID.PF_TPS);
				pid.setFlag(PeerID.PF_LOCAL);
				pid.addr.host = lpi.localIP;
				pid.addr.port = lpi.localTCPPort;
				providers.add(pid);
			}
			return providers.size();
		}
		
		public void onLocalQuery(int qid, Digest digest, NetAddress addrRemote)
		{
			List<PeerID> providers = new ArrayList<PeerID>();
			synchronized( this )
			{
				if( isOK() && bHead )
				{
					getProviders(digest, providers);
				}
			}
			ups.sendPacket(addrRemote, new Packet.P2P.LocalQueryAnswer(qid, providers));
		}
		
		public void onLocalQueryAnswer(int qid, List<PeerID> providers)
		{
			QueryRequest req = null;
			synchronized( queryStubs )
			{
				req = queryStubs.remove(qid);
			}
			if( req != null )
				onQueryAnswer(req, providers);
		}
		
		public void querySource(final QueryRequest req)
		{
			List<PeerID> providers = new ArrayList<PeerID>();
			NetAddress addrHead = null;
			synchronized( this )
			{
				if( isOK() )
				{
					if( bHead )
					{
						getProviders(req.digest, providers);
					}
					else
					{
						addrHead = new NetAddress(headID.localIP, headID.localUDPPort);
					}
				}
			}
			if( addrHead == null )
				onQueryAnswer(req, providers);
			else
			{
				final int qid = seedLocalQuery.incrementAndGet();
				// save qid
				synchronized( queryStubs)
				{
					queryStubs.put(qid, req);
				}
				peer.getExecutor().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							QueryRequest req = null;
							synchronized( queryStubs )
							{
								QueryRequest stub = queryStubs.remove(qid);
								if( stub != null )
									req = stub;
							}
							if( req != null )
								onQueryAnswer(req, null);
					}
					
				}, 100 /*TODO*/, TimeUnit.MILLISECONDS);
				logger.info("rpcmanager: query source from local net, digest is " + req.digest);
				ups.sendPacket(addrHead, new Packet.P2P.LocalQuery(qid, req.digest));
			}
		}
		
		private synchronized void checkProviders(List<PeerID> providers)
		{
			if( providers == null || ! isOK() )
				return;
			Iterator<PeerID> iter = providers.iterator();
			while( iter.hasNext() )
			{
				PeerID pid = iter.next();
				if( pid.kuid == selfID.peerID || blackList.contains(pid.kuid) )
				{
					iter.remove();
				}
			}
		}
		
		public synchronized void addBlack(PeerID pid)
		{
			logger.info("rpcmanager: local network add " + pid + " into black list");
			blackList.add(pid.kuid);
		}
		
		private void onQueryAnswer(QueryRequest req, List<PeerID> providers)
		{
			checkProviders(providers);
			if( providers == null || providers.isEmpty() )
			{
				queryPeerSource(req);
			}
			else
			{
				// TODO log ok, and refuse at next time
				logger.info("rpcmanager: on local source answer, digest is " + req.digest + ", providers count is " + providers.size());
				peer.getDataManager().setSourceAnswer(req.digest, providers);
			}
		}
		
		public void onTimer()
		{
			long now = System.currentTimeMillis();
			List<NetAddress> keepaliveAddressList = new ArrayList<NetAddress>();
			long headid = 0;
			synchronized( this )
			{
				if( headID == null )
					return;
				if( ! bHead )
				{
					if( now > lastKeepaliveRecv + KEEPALIVE_TIMEOUT )
					{
						headID = null;
						members.clear();
						digests.clear();
						return;
					}
				}
				else
				{
					Iterator<Map.Entry<Long, Member>> iter = members.entrySet().iterator();
					while( iter.hasNext() )
					{
						Map.Entry<Long, Member> e = iter.next();
						Member m = e.getValue();
						if( m.id.peerID != headID.peerID
								&& now > m.lastKeepaliveRecv + KEEPALIVE_TIMEOUT )
						{
							for(DigestInfo di : m.digests)
							{
								di.peers.remove(m.id.peerID);
								if( di.peers.isEmpty() )
									digests.remove(di.digest);
							}
							iter.remove();
						}
					}
			
					if( now > lastKeepaliveRequest + KEEPALIVE_INTERVAL )
					{
						lastKeepaliveRequest = now;
						headid = headID.peerID;
						for(Member m : members.values())
						{
							if( m.id.peerID != headID.peerID )
								keepaliveAddressList.add(new NetAddress(m.id.localIP, m.id.localUDPPort));
						}
					}
				}
			}
			if( ! keepaliveAddressList.isEmpty() )
			{
				SimplePacket packet = new Packet.P2P.LocalNetworkKeepaliveReq(headid);
				for(NetAddress addr : keepaliveAddressList)
					ups.sendPacket(addr, packet);
			}
			dump();
		}
		
		public synchronized PeerID getLocalPeerID()
		{
			if( ! isOK() )
				return null;
			PeerID l = new PeerID();
			l.kuid = selfID.peerID;
			l.setFlag(PeerID.PF_TPS);
			l.setFlag(PeerID.PF_LOCAL);
			l.addr.host = selfID.localIP;
			l.addr.port = selfID.localTCPPort;
			return l;
		}
		
		private String dumpLPI(SBean.LocalPeerID lpi)
		{
			return "[" + (int)(0xffff&(lpi.peerID>>>32)) + " " + lpi.localIP + " " + lpi.localTCPPort + " " + lpi.localUDPPort
            	+ "]";
		}
		
		public synchronized void dump()
		{
			if( ! isOK() )
				return;
			System.out.printf("local network information dump\n");
			System.out.printf("\tselfID is %s\n", dumpLPI(selfID));
			System.out.printf("\theadID is %s\n", dumpLPI(headID));
			if( bHead )
			{
				System.out.printf("\tmembers count is %s\n", members.size());
				for(Member m : members.values())
					System.out.printf("\t\t member %s, [%d] digests\n", dumpLPI(m.id), m.digests.size());
			}
		}
		
		private boolean bHead;
		private SBean.LocalPeerID selfID;
		private SBean.LocalPeerID headID;
		private final Map<Long, Member> members = new HashMap<Long, Member>();
		private final Map<Digest, DigestInfo> digests = new HashMap<Digest, DigestInfo>();
		private long lastKeepaliveRequest = 0;
		private long lastKeepaliveRecv = System.currentTimeMillis();
		private AtomicInteger seedLocalQuery = new AtomicInteger();
		private Map<Integer, QueryRequest> queryStubs = new HashMap<Integer, QueryRequest>();
		private Set<Long> blackList = new HashSet<Long>(); // TODO
	}
	
	public void localShareDigests(List<Digest> digests)
	{
		localNetwork.shareDigests(digests);
	}
	
	public RPCManager(Peer peer)
	{
		this.peer = peer;
	}
	
	public void start()
	{
		managerNet.start();

		if( ! peer.getConfig().bTPSDisable )
		{
			tps.setListenAddr(new NetAddress(null, peer.getConfig().tpsPort), BindPolicy.eLoopIncPort);
			tps.open();
		}
		ups.open();
				
		try
		{
			tt.future = peer.getExecutor().scheduleAtFixedRate(tt, 1, 1, TimeUnit.SECONDS);
			cst2.future = peer.getExecutor().scheduleAtFixedRate(cst2, 250, 250, TimeUnit.MILLISECONDS);
			cst.future = peer.getExecutor().scheduleAtFixedRate(cst, 250, 250, TimeUnit.MILLISECONDS);
		}
		catch(RejectedExecutionException ex)
		{			
		}
	}
	
	private void connectToTracker()
	{
		if( peer.getConfig().trackers.isEmpty() )
		{
			ttc.setServerAddr(new NetAddress("127.0.0.1", 1106)); // TODO
			ttc.open();
		}
		else if( peer.getConfig().trackers.size() == 1 )
		{
			ttc.setServerAddr(peer.getConfig().trackers.iterator().next());
			ttc.open();
		}
		else
		{
			trackerSelector.init(peer.getConfig().trackers);
		}
	}
	
	public boolean isTrackerOK()
	{
		return ! peerID.isNull();
	}
	
	public void destroy()
	{
		managerNet.destroy();
		synchronized( lockerRPC )
		{
			for(PeerInfo pinfo : peerinfos.values())
			{
				for(Digest d : pinfo.reqRegisters.keySet())
				{
					addTask(new RegisterResponseTask(d, 0), true);
				}
				pinfo.reqRegisters.clear();
				for(Digest d : pinfo.reqUploads.keySet())
				{
					addTask(new UploadResponseTask(pinfo.peerid, d, false), true);
				}
				pinfo.reqUploads.clear();
			}
			uploadingPeerIDs.clear();
		}
		
	}

	private int genNewSessionID()
	{
		return seedPSID.incrementAndGet();
	}
		
	private void onPeerSessionPacketRecv(PeerSession session, SimplePacket packet)
	{
		if( session == null )
			return;
		
		switch( packet.getType() )
		{
		case Packet.eP2PPKTRegisterRequest:
			{
				Packet.P2P.RegisterRequest p = (Packet.P2P.RegisterRequest)packet;
				onPeerRegisterRequest(session.peerSessionID, p.getDigest());
			}
			break;
		case Packet.eP2PPKTRegisterResponse:
			{
				Packet.P2P.RegisterResponse p = (Packet.P2P.RegisterResponse)packet;
				onPeerRegisterResponse(session.peerSessionID, p.getDigest(), p.getRank());
			}
			break;
		case Packet.eP2PPKTUploadRequest:
			{				
				Packet.P2P.UploadRequest p = (Packet.P2P.UploadRequest)packet;
				onPeerUploadRequest(session.peerSessionID, p.getDigest());
			}
			break;
		case Packet.eP2PPKTUploadResponse:
			{				
				Packet.P2P.UploadResponse p = (Packet.P2P.UploadResponse)packet;
				onPeerUploadResponse(session.peerSessionID, p.getDigest(), p.getOK());
			}
			break;
		case Packet.eP2PPKTDataRequest:
			{				
				Packet.P2P.DataRequest p = (Packet.P2P.DataRequest)packet;
				onPeerDataRequest(session.peerSessionID, p.getDigest(), p.getSection());
			}
			break;
		case Packet.eP2PPKTDataResponse:
			{			
				Packet.P2P.DataResponse p = (Packet.P2P.DataResponse)packet;
				onPeerDataResponse(session.peerSessionID, p.getDigest(), p.getSection(), p.getData());
			}
			break;
		case Packet.eP2PPKTFinishUpload:
			{			
				Packet.P2P.FinishUpload p = (Packet.P2P.FinishUpload)packet;
				onPeerFinishUpload(session.peerSessionID, p.getDigest());
			}
			break;
		case Packet.eP2PPKTFinishDownload:
			{			
				Packet.P2P.FinishDownload p = (Packet.P2P.FinishDownload)packet;
				onPeerFinishDownload(session.peerSessionID, p.getDigest());
			}
			break;
		case Packet.eP2PPKTGroupQuery:
			{			
				Packet.P2P.GroupQuery p = (Packet.P2P.GroupQuery)packet;
				onPeerGroupQuery(session.peerSessionID, p.getGroup());
			}
			break;
		case Packet.eP2PPKTGroupAnswer:
			{			
				Packet.P2P.GroupAnswer p = (Packet.P2P.GroupAnswer)packet;
				onPeerGroupAnswer(session.peerSessionID, p.getGroup(), p.getExist());
			}
			break;
		case Packet.eP2PPKTQueryRequest:
			{				
				Packet.P2P.QueryRequest p = (Packet.P2P.QueryRequest)packet;
				onPeerQueryRequest(session.peerSessionID, p.getDigest());
			}
			break;
		case Packet.eP2PPKTQueryResponse:
			{			
				Packet.P2P.QueryResponse p = (Packet.P2P.QueryResponse)packet;
				onPeerQueryResponse(session.peerSessionID, p.getDigest(), p.getExist());
			}
			break;
		default:
			break;
		}
	}

	private void onTPSPacketRecv(int tpsSessionID, SimplePacket packet)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			Integer sid = tpsSessions.get(tpsSessionID);
			if( sid == null )
				return;
			session = sessions.get(sid);
		}
		onPeerSessionPacketRecv(session, packet);
	}
	
	private void onTPCPacketRecv(int psid, SimplePacket packet)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
			if( session == null )
				return;
		}
		onPeerSessionPacketRecv(session, packet);
	}
	
	private void onUPSPacketRecv(NetAddress addrRemote, SimplePacket packet)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			Integer sid = udpSessionIDs.get(addrRemote);
			if( sid == null )
				return;
			session = sessions.get(sid);
		}
		onPeerSessionPacketRecv(session, packet);
	}
	
	private void sendPeerPacket(PeerSession session, SimplePacket packet)
	{
		switch( session.type )
		{
		case ePSTTPS:
			tps.sendPacket(session.tpsSessionID, packet);
			break;
		case ePSTTPC:
			session.tpc.sendPacket(packet);
			break;
		case ePSTUPS:
			ups.sendPacket(session.peeridRemote.addr, packet);
			break; // TODO
		case ePSTRelay:
			BytesOutputStream bsos = new BytesOutputStream(); 
			Stream.AOStream os = new Stream.OStreamLE(bsos);
			os.pushInteger(packet.getType());
			os.pushByteBuffer(Stream.storeObjLE(packet));
			ttc.sendPacket(new Packet.P2T.Relay(session.peeridRemote, ByteBuffer.wrap(bsos.array(), 0, bsos.size())));
			break;
		default:
			break;
		}
	}
	
	private void activeOpenPeerSession(int aid, PeerID peeridRemote, boolean bRelayPermit)
	{
		logger.info("rpcmanager: try active open " + (bRelayPermit ? "relayPermit" : "p2pOnly")
				+ (peeridRemote.isLocal() ? "local" : "") 
				+ " session, aid = " + aid
				+ ", remote peer is " + peeridRemote);
		if( peerID.isNull() )
		{
			addTask(new OpenFailedTask(peeridRemote, aid), true);
			return;
		}
		PeerID localPeerID = null;
		if( peeridRemote.isLocal() )
		{
			localPeerID = localNetwork.getLocalPeerID();
			if( localPeerID == null || ! peeridRemote.isTPS() )			
			{
				addTask(new OpenFailedTask(peeridRemote, aid), true);
				return;
			}
			// direct tps
			int peerSessionID = genNewSessionID();
			TCPPeerClient tpc = new TCPPeerClient(this, peerSessionID, 0, true, peeridRemote, localPeerID);
			tpc.setServerAddr(peeridRemote.addr);			
			synchronized( lockerSession )
			{
				activeConnectings.put(peerSessionID, new ActiveConnecting(peerSessionID, aid, peeridRemote));
			}
			tpc.open();
			return;
		}
		if( bRelayPermit ) // relay
		{
			int peerSessionID = 0;
			synchronized( lockerSession )
			{
				if( ! relaySessionIDs.containsKey(peeridRemote.kuid) )
				{
					peerSessionID = genNewSessionID();
					relaySessionIDs.put(peeridRemote.kuid, peerSessionID);
					sessions.put(peerSessionID, new PeerSession(peerSessionID, PeerSessionType.ePSTRelay, peeridRemote));
				}
			}
			if( peerSessionID != 0 )
				onPeerSessionActiveOpen(peerSessionID, peeridRemote, true, aid);
			else
				addTask(new OpenFailedTask(peeridRemote, aid), true);
		}
		else if( peeridRemote.isTPS() ) // tcp direct
		{
			int peerSessionID = genNewSessionID();
			TCPPeerClient tpc = new TCPPeerClient(this, peerSessionID, 0, true, peeridRemote, peerID);
			tpc.setServerAddr(peeridRemote.addr);			
			synchronized( lockerSession )
			{
				activeConnectings.put(peerSessionID, new ActiveConnecting(peerSessionID, aid, peeridRemote));
			}
			tpc.open();
		}
		else if( peerID.isTPS() ) // tcp reverse
		{
			int peerSessionID = genNewSessionID();			
			synchronized( lockerSession )
			{
				activeConnectings.put(peerSessionID, new ActiveConnecting(peerSessionID, aid, peeridRemote));
			}
			ttc.sendPacket(new Packet.P2T.TPSRCRequest(peeridRemote, peerSessionID));
		}
		else if( peerID.isUPS() && peeridRemote.isUPS() )
		{
			Integer udpsid = null;
			synchronized( lockerSession )
			{
				udpsid = udpSessionIDs.get(peeridRemote.addr);
			}
			if( udpsid != null )
				addTask(new OpenFailedTask(peeridRemote, aid), true);
			else
			{
				int peerSessionID = genNewSessionID();
				synchronized( lockerSession )
				{
					activeConnectings.put(peerSessionID, new ActiveConnecting(peerSessionID, aid, peeridRemote));
				}
				addTask(new PunchTask(peeridRemote.addr), true);
				ttc.sendPacket(new Packet.P2T.UPSRCRequest(peeridRemote, peerSessionID));
			}
		}
		else // no way
			addTask(new OpenFailedTask(peeridRemote, aid), true);
	}
	
	private void onTPCConnected(TCPPeerClient tpc)
	{
		logger.info("rpcmanager: on tpc connected");
		if( tpc.getDirect() )
		{
			ActiveConnecting ttcc = null;
			synchronized( lockerSession )
			{
				ttcc = activeConnectings.remove(tpc.getLocalPeerSessionID());
				if( ttcc != null )
					sessions.put(ttcc.peerSessionID, new PeerSession(ttcc.peerSessionID, PeerSessionType.ePSTTPC, tpc, ttcc.peeridRemote));
			}
			if( ttcc != null )
			{	
				onPeerSessionActiveOpen(ttcc.peerSessionID, ttcc.peeridRemote, false, ttcc.aid);
			}
		}
		else
		{
			tpc.setDirect(true);
			synchronized( lockerSession )
			{
				sessions.put(tpc.getLocalPeerSessionID(), new PeerSession(tpc.getLocalPeerSessionID(), PeerSessionType.ePSTTPC, tpc, tpc.getRemotePeerID()));
			}
			onPeerSessionPassiveOpen(tpc.getLocalPeerSessionID(), tpc.getRemotePeerID(), false);
		}
	}
	
	private void queryPeerSource(final QueryRequest req)
	{
		if( req.group == null )
		{
			onPeerQueryAnswer(req.digest, null);
			return;
		}
		QueryResponse res = new QueryResponse();
		List<Integer> sids = new ArrayList<Integer>();
		synchronized( lockerRPC )
		{
			if( peerQueryStubs.containsKey(req.digest) )
				return;
			for(PeerInfo pinfo : peerinfos.values())
			{
				// TODO
				if( pinfo.groups.get(req.group) == PeerInfo.GroupSource.eExist 
						&& ! pinfo.sessions.isEmpty() )
				{
					res.pids.add(pinfo.peerid.getLocalKey());
					sids.add(pinfo.sessions.keySet().iterator().next());
				}
			}
			if( ! res.pids.isEmpty() )
			{
				peerQueryStubs.put(req.digest, res);
				peer.getExecutor().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						List<PeerID> providers = null;
						synchronized( lockerRPC )
						{
							QueryResponse res = peerQueryStubs.remove(req.digest);
							if( res != null )
							{
								providers = new ArrayList<PeerID>();
								if( res.bOK )
								{
									if( res.peersOK.isEmpty() )
										providers = null;
									else
										providers.addAll(res.peersOK.values());
								}
							}
						}
						if( providers != null )
							onPeerQueryAnswer(req.digest, providers);
					}
				
				}, 250 /*TODO*/, TimeUnit.MILLISECONDS);
			}
		}
		if( sids.isEmpty() )
		{
			onPeerQueryAnswer(req.digest, null);
			return;
		}
		for(int sid : sids)
			requestQuery(sid, req.digest);
	}
	
	private void onPeerQueryAnswer(Digest digest, List<PeerID> providers)
	{
		if( providers == null || providers.isEmpty() )
		{
			queryTrackerSource(digest);
		}
		else
		{
			// TODO log ok, and refuse at next time
			logger.info("rpcmanager: on peer source answer, digest is " + digest + ", providers count is " + providers.size());
			peer.getDataManager().setSourceAnswer(digest, providers);
		}
	}
	
	private void queryTrackerSource(Digest digest)
	{
		if( ! isTrackerOK() )
			return;
		logger.info("rpcmanager: query source from tracker, digest is " + digest);
		ttc.sendPacket(new Packet.P2T.SourceQuery(digest));
	}
	
	public void querySource(Digest digest, Digest group)
	{
		localNetwork.querySource(new QueryRequest(digest, group));
	}
	
	public void createDataGroup(Digest group, int membercount)
	{
		if( ! isTrackerOK() )
			return;
		ttc.sendPacket(new Packet.P2T.CreateDataGroup(group, membercount));
	}
	
	public void shareUpdate(Digest digest, boolean bAddOrRemove)
	{
		if( ! isTrackerOK() )
			return;
		ttc.sendPacket(new Packet.P2T.ShareUpdateRequest(digest, bAddOrRemove));
	}
	
	public void groupShareUpdate(Digest group, List<Integer> lst)
	{
		if( ! isTrackerOK() )
			return;
		ttc.sendPacket(new Packet.P2T.GroupShareUpdateRequest(group, lst));
	}
	
	public void requestRegister(PeerID peerid, Digest digest, Digest group)
	{
		logger.info("rpcmanager: request register, digest is " + digest + ", provider is " + peerid);
		int sid = 0;
		int aid = 0;
		boolean bActiveOpen = false;
		synchronized( lockerRPC )
		{
			PeerInfo pinfo = peerinfos.get(peerid.getLocalKey());
			if( pinfo == null )
			{
				pinfo = new PeerInfo(peerid);
				peerinfos.put(peerid.getLocalKey(), pinfo);
			}
			else if( pinfo.reqRegisters.containsKey(digest) )
				return;
			RegisterRequest req = new RegisterRequest(digest, group);
			if( ! pinfo.sessions.isEmpty() )
			{
				sid = pinfo.sessions.keySet().iterator().next();
				pinfo.timeSessionIdle = 0;
			}
			else if( pinfo.sessionRelay != 0 )
			{
				sid = pinfo.sessionRelay;
				pinfo.timeSessionRelayIdle = 0;
			}
			if( sid != 0 )
				req.setSID(sid);
			else
			{
				if( pinfo.aidRelayPermit == -1 )
				{
					pinfo.aidRelayPermit = genNewAID();
					bActiveOpen = true;
					aid = pinfo.aidRelayPermit;
				}
				req.setAID(pinfo.aidRelayPermit);
			}
			pinfo.reqRegisters.put(digest, req);
		}
		if( sid != 0 )
			requestRegister(sid, digest);
		else if( bActiveOpen )
			activeOpenPeerSession(aid, peerid, true);
	}
	
	private void requestRegister(int psid, Digest digest)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session == null )
			addTask(new RegisterResponseTask2(psid, digest, 0), true);
		else
			sendPeerPacket(session, new Packet.P2P.RegisterRequest(digest));
	}
	
	public void requestUpload(PeerID peerid, Digest digest)
	{
		logger.info("rpcmanager: request upload, digest is " + digest + ", demander is " + peerid);
		int sid = 0;
		int aid = 0;
		boolean bActiveOpen = false;
		synchronized( lockerRPC )
		{
			PeerInfo pinfo = peerinfos.get(peerid.getLocalKey());
			if( pinfo == null )
			{
				pinfo = new PeerInfo(peerid);
				peerinfos.put(peerid.getLocalKey(), pinfo);
			}
			else if( pinfo.reqUploads.containsKey(digest) )
				return;
			UploadRequest req = new UploadRequest(digest);
			if( ! pinfo.sessions.isEmpty() )
			{
				sid = pinfo.sessions.keySet().iterator().next();
				pinfo.timeSessionIdle = 0;
			}
			if( sid != 0 )
				req.setSID(sid);
			else
			{
				if( pinfo.aidP2POnly == -1 )
				{
					pinfo.aidP2POnly = genNewAID();
					bActiveOpen = true;
					aid = pinfo.aidP2POnly;
				}
				req.setAID(pinfo.aidP2POnly);
			}
			pinfo.reqUploads.put(digest, req);
		}
		if( sid != 0 )
			requestUpload(sid, digest);
		else if( bActiveOpen )
			activeOpenPeerSession(aid, peerid, false);
	}
	
	private void requestUpload(int psid, Digest digest)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session == null )
			addTask(new UploadResponseTask2(psid, digest, false), true);
		else
			sendPeerPacket(session, new Packet.P2P.UploadRequest(digest));
	}
	
	private void requestQuery(int psid, Digest digest)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session == null )
			onPeerQueryResponse(psid, digest, false);
		else
			sendPeerPacket(session, new Packet.P2P.QueryRequest(digest));
	}
	
	public void onPeerQueryRequest(int psid, Digest digest)
	{
		boolean bExist = peer.getDataManager().querySource(digest);
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.QueryResponse(digest, bExist));
	}
	
	public void onPeerQueryResponse(int psid, Digest digest, boolean bExist)
	{
		List<PeerID> providers = new ArrayList<PeerID>();
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			if( pinfo.sessions.containsKey(psid) )
				pinfo.timeSessionIdle = 0;
			else
				return;
			QueryResponse res = peerQueryStubs.get(digest);
			if( res == null )
				return;
			if( ! res.pids.remove(pinfo.peerid.getLocalKey()) )
				return;
			if( bExist )
			{				
				if( res.bOK )
				{
					res.peersOK.put(pinfo.peerid.getLocalKey(), pinfo.peerid);
					if( res.pids.isEmpty() )
						providers.addAll(res.peersOK.values());
					else
						providers = null;
				}
				else // first exist res
				{
					res.bOK = true;
					providers.add(pinfo.peerid);
				}
			}
			else // query peer source failed
			{
				if( res.pids.isEmpty() )
				{
					if( res.bOK )
					{
						if( res.peersOK.isEmpty() )
							providers = null;
						else
							providers.addAll(res.peersOK.values());
					}
				}
				else
					providers = null;
			}
			if( res.pids.isEmpty() )
				peerQueryStubs.remove(digest);
		}
		if( providers != null )
			onPeerQueryAnswer(digest, providers);
	}
	
	public void finishUpload(PeerID peerid, Digest digest)
	{
		Long timeout = null;
		int sid = 0;
		synchronized( lockerRPC )
		{
			final long pid = peerid.getLocalKey();
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			if( pinfo.sessions.isEmpty() )
				return;
			timeout = pinfo.uploading.remove(digest);
			sid = pinfo.sessions.keySet().iterator().next();
			if( timeout != null && pinfo.uploading.isEmpty() )
				uploadingPeerIDs.remove(pid);
		}
		if( timeout != null )
		{
			addTask(new FinishUploadingTask(digest), true);
			finishDownload(sid, digest);
		}
	}
	
	public void finishDownload(PeerID peerid, Digest digest)
	{
		Long timeout = null;
		int sid = 0;
		synchronized( lockerRPC )
		{
			PeerInfo pinfo = peerinfos.get(peerid.getLocalKey());
			if( pinfo == null )
				return;

			if( pinfo.sessions.isEmpty() )
				return;
			timeout = pinfo.downloading.remove(digest);
			sid = pinfo.sessions.keySet().iterator().next();
		}
		if( timeout != null )
		{
			addTask(new FinishDownloadingTask(digest), true);
			finishUpload(sid, digest);
		}
	}

	private void closePeerSession(int psid)
	{
		logger.info("rpcmanager: try close peer session, sid = " + psid);
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
		{	
			switch( session.type )
			{
			case ePSTTPS:
				tps.closeSession(session.tpsSessionID);
				break;
			case ePSTTPC:
				session.tpc.close();
				break;
			case ePSTUPS:
				synchronized( lockerSession )
				{					
					sessions.remove(psid);
					udpSessionIDs.remove(session.peeridRemote.addr);
				}
				ups.sendPacket(session.peeridRemote.addr, new Packet.P2P.UPSClose());
				addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);
				break;
			case ePSTRelay:
				synchronized( lockerSession )
				{
					sessions.remove(psid);
					relaySessionIDs.remove(session.peeridRemote.kuid);
				}
				addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);
				break;
			default:
				break;
			}
		}
	}
	
	private void requestData(int psid, Digest digest, SBean.Section section)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
			if( session != null && session.type == PeerSessionType.ePSTUPS && section.count > 8192 - 128)
				section.count = 8192 - 128;
		}
		if( session == null )
			addTask(new DataResponseTask2(psid, digest, section, null), true);
		else
			sendPeerPacket(session, new Packet.P2P.DataRequest(digest, section));
	}

	private void finishUpload(int psid, Digest digest)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.FinishUpload(digest));
	}
	
	private void finishDownload(int psid, Digest digest)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.FinishDownload(digest));
	}
	
	public void requestData(PeerID peerid, Digest digest, SBean.Section section)
	{
		//logger.debug("RPCManager.requestData, section is " + section.offset + " " + section.count);
		Long timeout = null;
		int sid = 0;
		synchronized( lockerRPC )
		{
			PeerInfo pinfo = peerinfos.get(peerid.getLocalKey());
			if( pinfo == null )
				return;

			if( pinfo.sessions.isEmpty() )
				return;
			timeout = pinfo.downloading.get(digest);
			sid = pinfo.sessions.keySet().iterator().next();
			pinfo.timeSessionIdle = 0;
		}
		if( timeout != null )
			requestData(sid, digest, section);
		else
			peer.getDataManager().finishDownload(digest);
	}
	
	private static class SessionIDAndDigest
	{
		public SessionIDAndDigest(Integer sessionid, Digest digest)
		{
			this.sessionid = sessionid;
			this.digest = digest;
		}
		
		Integer sessionid;
		Digest digest;
	}
	
	public void onExecutorShutdown()
	{
		if( cst.future != null )
			cst.future.cancel(false);

		if( tt.future != null )
			tt.future.cancel(false);
		if( cst2.future != null )
			cst2.future.cancel(false);
	}
	
	private void onTrackerAbort()
	{
		peerID.setNull();
		if( ttc.getRedirectInfo() != null )
		{
			NetAddress newAddr = new NetAddress(ttc.getRedirectInfo().addr.host, ttc.getRedirectInfo().addr.port); 
			logger.info("rpcmanager: tracker redirect to " + newAddr);
			ttc.setServerAddr(newAddr);
			ttc.setRedirectInfo(null);
			ttc.open();
		}
		else if( peer.getConfig().bAutoReconnectingTTC )
			ttcReconnectTime = System.currentTimeMillis() + TTC_RECONNECT_INTERVAL;
	}

	public void onPeerSessionActiveOpen(int psid, PeerID peeridRemote,
			boolean bRelay, int aid)
	{
		logger.info("rpcmanager: " + (bRelay ? "relay" : "peer") + " session active open, psid = " + psid
				+ ", remote peer is " + peeridRemote);
		List<Digest> reqsr = new ArrayList<Digest>();
		List<Digest> reqsu = new ArrayList<Digest>();
		synchronized( lockerRPC )
		{
			if( mapS2P.containsKey(psid) )
				return;
			mapS2P.put(psid, peeridRemote.getLocalKey());
			PeerInfo pinfo = peerinfos.get(peeridRemote.getLocalKey());
			if( pinfo == null ) // TODO ??
			{
				pinfo = new PeerInfo(peeridRemote);
				peerinfos.put(peeridRemote.getLocalKey(), pinfo);
			}
			if( pinfo.aidP2POnly == aid )
				pinfo.aidP2POnly = -1;
			else if( pinfo.aidRelayPermit == aid )
				pinfo.aidRelayPermit = -1;
			Session session = new Session();
			if( bRelay )
				pinfo.sessionRelay = psid;
			else
				pinfo.sessions.put(psid, session);
			Iterator<RegisterRequest> itr = pinfo.reqRegisters.values().iterator();
			while( itr.hasNext() )
			{
				RegisterRequest req = itr.next();
				if( req.getSID() == -1 )
				{
					req.setSID(psid);
					reqsr.add(req.digest);
				}
			}
			if( ! bRelay )
			{
				Iterator<UploadRequest> itu = pinfo.reqUploads.values().iterator();
				while( itu.hasNext() )
				{
					UploadRequest req = itu.next();
					if( req.getSID() == - 1 )
					{
						req.setSID(psid);
						reqsu.add(req.digest);
					}
				}
			}
		}
		for(Digest d : reqsr)
			requestRegister(psid, d);
		for(Digest d : reqsu)
			requestUpload(psid, d);
	}

	public void onPeerSessionActiveOpenFailed(PeerID peeridRemote, int aid)
	{
		logger.info("rpcmanager: peer session active open failed, aid = " + aid + ", remote peer is " + peeridRemote);
		if( peeridRemote.isLocal() )
		{
			localNetwork.addBlack(peeridRemote);
		}
		synchronized( lockerRPC )
		{
			PeerInfo pinfo = peerinfos.get(peeridRemote.getLocalKey());
			if( pinfo == null )
				return;
			if( pinfo.aidP2POnly == aid )
				pinfo.aidP2POnly = -1;
			else if( pinfo.aidRelayPermit == aid )
				pinfo.aidRelayPermit = -1;
			Iterator<RegisterRequest> itr = pinfo.reqRegisters.values().iterator();
			while( itr.hasNext() )
			{
				RegisterRequest req = itr.next();
				if( req.getAID() == aid )
				{
					addTask(new RegisterResponseTask(req.digest, 0), true);
					itr.remove();
				}
			}
			Iterator<UploadRequest> itu = pinfo.reqUploads.values().iterator();
			while( itu.hasNext() )
			{
				UploadRequest req = itu.next();
				if( req.getAID() == aid )
				{
					addTask(new UploadResponseTask(pinfo.peerid, req.digest, false), true);
					itu.remove();
				}
			}
		}
	}

	public void onPeerSessionPassiveOpen(int psid, PeerID peeridRemote,
			boolean bRelay)
	{
		logger.info("rpcmanager: " + (bRelay ? "relay" : "peer") + " session passive open, psid = " + psid
				+ ", remote peer is " + peeridRemote);
		synchronized( lockerRPC )
		{
			if( mapS2P.containsKey(psid) )
				return;
			mapS2P.put(psid, peeridRemote.getLocalKey());
			PeerInfo pinfo = peerinfos.get(peeridRemote.getLocalKey());
			if( pinfo == null )
			{
				pinfo = new PeerInfo(peeridRemote);
				peerinfos.put(peeridRemote.getLocalKey(), pinfo);
			}
			Session session = new Session();
			if( bRelay )
				pinfo.sessionRelay = psid;
			else
				pinfo.sessions.put(psid, session);			
		}
	}

	public void onPeerSessionClose(int psid, PeerID peeridRemote)
	{
		synchronized( lockerRPC )
		{
			mapS2P.remove(psid);
			final long pid = peeridRemote.getLocalKey();
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			Session session = pinfo.sessions.remove(psid);
			if( session == null )
			{
				if( pinfo.sessionRelay != psid )
					return;
				logger.info("rpcmanager: relay session close, psid = " + psid + ", remote peer is " + peeridRemote);
				pinfo.sessionRelay = 0;
			}
			else
				logger.info("rpcmanager: peer session close, psid = " + psid + ", remote peer is " + peeridRemote);
			Iterator<RegisterRequest> itr = pinfo.reqRegisters.values().iterator();
			while( itr.hasNext() )
			{
				RegisterRequest req = itr.next();
				if( req.getSID() == psid )
				{
					addTask(new RegisterResponseTask(req.digest, 0), true);
					itr.remove();
				}
			}
			Iterator<UploadRequest> itu = pinfo.reqUploads.values().iterator();
			while( itu.hasNext() )
			{
				UploadRequest req = itu.next();
				if( req.getSID() == psid )
				{
					addTask(new UploadResponseTask(pinfo.peerid, req.digest, false), true);
					itu.remove();
				}
			}
			if( pinfo.sessions.isEmpty() )
			{
				for(Digest digest : pinfo.uploading.keySet() )
					addTask(new FinishUploadingTask(digest), true);
				if( ! pinfo.uploading.isEmpty() )
					uploadingPeerIDs.remove(pid);
				pinfo.uploading.clear();
				for(Digest digest : pinfo.downloading.keySet() )
					addTask(new FinishDownloadingTask(digest), true);
				pinfo.downloading.clear();
			}
		}
	}
	
	public void onPeerRegisterRequest(int psid, Digest digest)
	{	
		PeerID peerid = null;
		boolean bOverflow = false;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			if( pinfo.sessions.containsKey(psid) )
				pinfo.timeSessionIdle = 0;
			else if( pinfo.sessionRelay == psid )
				pinfo.timeSessionRelayIdle = 0;
			else
				return;
			peerid = pinfo.peerid;
			
			if( uploadingPeerIDs.size() >= (peer.getConfig().bSuper 
					? MAX_UPLOADING_SUPER_PEER_COUNT + MAX_OVERFLOW_UPLOADING_SUPER_PEER_COUNT
					: MAX_UPLOADING_PEER_COUNT + MAX_OVERFLOW_UPLOADING_PEER_COUNT
					) && ! uploadingPeerIDs.contains(pid) )
				bOverflow = true;
			logger.info("rpcmanager: on register request, peerremote is " + peerid + ", digest = " + digest);
		}
		if( peerid != null )
		{
			if( bOverflow )
			{
				setRegisterResponse(psid, digest, 0);
				return;
			}
			int[] ret = peer.getDataManager().register(digest, peerid);
			setRegisterResponse(psid, digest, ret[0]);
			if( ret[1] == 1 )
			{
				requestUpload(peerid, digest);
			}
		}
	}
	
	private void setRegisterResponse(int psid, Digest digest, int rank)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.RegisterResponse(digest, rank));
	}

	public void onPeerRegisterResponse(int psid, Digest digest, int rank)
	{
		logger.info("rpcmanager: on register response, digest is " + digest + ", psid is " + psid + ", rank is " + rank);
		Digest group = null;
		boolean bRelay = false;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			if( pinfo.sessions.containsKey(psid) )
				pinfo.timeSessionIdle = 0;
			else if( pinfo.sessionRelay == psid )
			{
				bRelay = true;
				pinfo.timeSessionRelayIdle = 0;
			}
			else
				return;
			RegisterRequest req = pinfo.reqRegisters.remove(digest);
			if( req != null )
			{
				addTask(new RegisterResponseTask(digest, rank), true);
				if( ! bRelay && ! pinfo.peerid.isSuper() && rank != 0 && req.group != null )
				{
					if( ! pinfo.groups.containsKey(req.group) )
					{
						pinfo.groups.put(req.group, PeerInfo.GroupSource.eUnknown);
						group = req.group;
					}
				}
			}
		}
		if( group != null )
			queryGroup(psid, group);
	}
	
	private void queryGroup(int psid, Digest group)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.GroupQuery(group));
	}
	
	public void onPeerUploadRequest(int psid, Digest digest)
	{
		PeerID peerid = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;	
			pinfo.timeSessionIdle = 0;
			peerid = pinfo.peerid;
		}
		if( peerid != null )
		{
			logger.info("rpcmanager: on upload request, peerremote " + peerid + ", psid = " + psid);
			SBean.Section section = peer.getDataManager().requestUpload(digest, peerid);
			setUploadResponse(psid, digest, section != null);
			if( section != null )
			{
				synchronized( lockerRPC )
				{
					Long pid = mapS2P.get(psid);
					if( pid == null )
						return;
					PeerInfo pinfo = peerinfos.get(pid);
					pinfo.downloading.put(digest, System.currentTimeMillis() + PeerInfo.DOWNLOADING_MAX_IDLE);
				}
				addTask(new DataRequestTask(psid, digest, section), true);
			}
		}
	}
	
	private void setUploadResponse(int psid, Digest digest, boolean bOK)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
		{
			sendPeerPacket(session, new Packet.P2P.UploadResponse(digest, bOK));
		}
	}

	public void onPeerUploadResponse(int psid, Digest digest, boolean bOK)
	{
		Runnable ures = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			pinfo.timeSessionIdle = 0;
			UploadRequest req = pinfo.reqUploads.remove(digest);
			if( req != null )
			{
				ures = new UploadResponseTask(pinfo.peerid, digest, bOK);
				if( bOK )
				{
					if( pinfo.uploading.isEmpty() )
						uploadingPeerIDs.add(pid);
					pinfo.uploading.put(digest, System.currentTimeMillis() + PeerInfo.UPLOADING_MAX_IDLE);
				}
			}
		}
		if( ures != null )
			ures.run();
	}

	public void onPeerDataRequest(int psid, Digest digest, SBean.Section section)
	{
		PeerID peerid = null;
		Long timeout = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;	
			peerid = pinfo.peerid;
			pinfo.timeSessionIdle = 0;
			timeout = pinfo.uploading.get(digest);
			if( timeout != null )
				pinfo.uploading.put(digest, System.currentTimeMillis() + PeerInfo.UPLOADING_MAX_IDLE);
		}
		if( peerid != null && timeout != null )
		{
			ByteBuffer data = peer.getDataManager().requestData(digest, peerid, section);
			setDataResponse(psid, digest, section, (data == null) ? ByteBuffer.allocate(0) : data);
		}
		else
			finishDownload(psid, digest);
	}
	
	private void setDataResponse(int psid, Digest digest, SBean.Section section, ByteBuffer data)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.DataResponse(digest, section, data));
	}

	public void onPeerDataResponse(int psid, Digest digest, SBean.Section section, ByteBuffer data)
	{
		PeerID peerid = null;
		Long timeout = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;	
			peerid = pinfo.peerid;
			pinfo.timeSessionIdle = 0;
			timeout = pinfo.downloading.get(digest);
			if( timeout != null )
				pinfo.downloading.put(digest, System.currentTimeMillis() + PeerInfo.DOWNLOADING_MAX_IDLE);
		}
		if( peerid != null && timeout != null )
		{
			if( ! peer.getDataManager().setDataRet(digest, peerid, section, data) )
			{
				addTask(new FinishDownloadingTask(digest), true);
				finishUpload(psid, digest);
			}
		}
		else
			finishUpload(psid, digest);
	}
	
	public void onPeerFinishUpload(int psid, Digest digest)
	{
		Long timeout = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			timeout = pinfo.uploading.remove(digest);
			if( timeout != null && pinfo.uploading.isEmpty() )
				uploadingPeerIDs.remove(pid);
		}
		if( timeout != null )
		{
			addTask(new FinishUploadingTask(digest), true);
		}
	}
	
	public void onPeerFinishDownload(int psid, Digest digest)
	{
		Long timeout = null;
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			timeout = pinfo.downloading.remove(digest);
		}
		if( timeout != null )
		{
			addTask(new FinishDownloadingTask(digest), true);
		}
	}
	
	public void onPeerGroupQuery(int psid, Digest group)
	{
		boolean bExist = peer.getDataManager().queryGroup(group);
		PeerSession session = null;
		synchronized( lockerSession )
		{
			session = sessions.get(psid);
		}
		if( session != null )
			sendPeerPacket(session, new Packet.P2P.GroupAnswer(group, bExist));
	}
	
	public void onPeerGroupAnswer(int psid, Digest group, boolean bExist)
	{
		synchronized( lockerRPC )
		{
			Long pid = mapS2P.get(psid);
			if( pid == null )
				return;
			PeerInfo pinfo = peerinfos.get(pid);
			if( pinfo == null )
				return;
			pinfo.groups.put(group, bExist ? PeerInfo.GroupSource.eExist : PeerInfo.GroupSource.eNotExist);
		}
	}
	
	private void addTask(Runnable r, boolean e)
	{
		try
		{
			peer.getExecutor().execute(r);
		}
		catch(RejectedExecutionException ex)
		{
			if( e )
				r.run();
		}
	}
	
	private int genNewAID()
	{
		return seedAID.incrementAndGet();
	}
	
	//// begin handlers.
	public void onUDPPeerServerOpen(UDPPeerServer peer)
	{
		logger.info("rpcmanager: ups open, addr is " + peer.getBoundAddr());
		if( tps.isOpen() && ! ttc.isOpen() )
			connectToTracker();
	}

	public void onUDPPeerServerOpenFailed(UDPPeerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onUDPPeerServerClose(UDPPeerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onUDPPeerServerRecvPing(UDPPeerServer peer, Packet.T2P.Ping packet, NetAddress addrRemote)
	{
		logger.info("rpcmanager: ping recv from " + addrRemote + ", seq is " + packet.getSeq() + ", load is " + packet.getLoad());
		trackerSelector.recvPing(addrRemote, packet.getSeq(), packet.getLoad());
	}

	public void onUDPPeerServerRecvHello(UDPPeerServer peer, Packet.P2P.Hello packet, NetAddress addrRemote)
	{
		int psid = 0;
		synchronized( lockerSession )
		{
			if( udpSessionIDs.containsKey(addrRemote) )
				return;
			psid = genNewSessionID();
			udpSessionIDs.put(addrRemote, psid);
			sessions.put(psid, new PeerSession(psid, PeerSessionType.ePSTUPS, packet.getPeerID()));
		}
		if( psid != 0 )
			onPeerSessionPassiveOpen(psid, packet.getPeerID(), false);
	}

	public void onUDPPeerServerRecvUPSRCResponse(UDPPeerServer peer, Packet.P2P.UPSRCResponse packet, NetAddress addrRemote)
	{
		ActiveConnecting ttcc = null;
		synchronized( lockerSession )
		{
			ttcc = activeConnectings.remove(packet.getPeerSessionID());
		}
		if( ttcc != null )
		{	
			if( packet.getOK() )
			{
				synchronized( lockerSession )
				{
					udpSessionIDs.put(ttcc.peeridRemote.addr, packet.getPeerSessionID());
					sessions.put(packet.getPeerSessionID(), new PeerSession(packet.getPeerSessionID(), PeerSessionType.ePSTUPS, ttcc.peeridRemote));
				}
				ups.sendPacket(ttcc.peeridRemote.addr, new Packet.P2P.Hello(peerID));
				onPeerSessionActiveOpen(ttcc.peerSessionID, ttcc.peeridRemote, false, ttcc.aid);
			}
			else
			{
				addTask(new OpenFailedTask(ttcc.peeridRemote, ttcc.aid), true);
			}
		}
	}

	public void onUDPPeerServerRecvUPSPunch(UDPPeerServer peer, Packet.P2P.UPSPunch packet, NetAddress addrRemote)
	{
	}

	public void onUDPPeerServerRecvUPSClose(UDPPeerServer peer, Packet.P2P.UPSClose packet, NetAddress addrRemote)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			Integer sid = udpSessionIDs.remove(addrRemote);
			if( sid == null )
				return;
			session = sessions.remove(sid);
		}
		if( session != null )
		{	
			addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);			
		}
	}

	public void onUDPPeerServerRecvRegisterRequest(UDPPeerServer peer, Packet.P2P.RegisterRequest packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvRegisterResponse(UDPPeerServer peer, Packet.P2P.RegisterResponse packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvUploadRequest(UDPPeerServer peer, Packet.P2P.UploadRequest packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvUploadResponse(UDPPeerServer peer, Packet.P2P.UploadResponse packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvDataRequest(UDPPeerServer peer, Packet.P2P.DataRequest packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvDataResponse(UDPPeerServer peer, Packet.P2P.DataResponse packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvFinishUpload(UDPPeerServer peer, Packet.P2P.FinishUpload packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvFinishDownload(UDPPeerServer peer, Packet.P2P.FinishDownload packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvGroupQuery(UDPPeerServer peer, Packet.P2P.GroupQuery packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvGroupAnswer(UDPPeerServer peer, Packet.P2P.GroupAnswer packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvNoticeLocalNetworkMember(UDPPeerServer peer, Packet.P2P.NoticeLocalNetworkMember packet, NetAddress addrRemote)
	{
		logger.info("rpcmanager: udp server recv local network member notice : "
				+ packet.getHeadID() + " to " + packet.getMemberID());
		localNetwork.recvMemberNotice(packet.getHeadID(), packet.getMemberID(), addrRemote);
	}

	public void onUDPPeerServerRecvLocalNetworkKeepaliveReq(UDPPeerServer peer, Packet.P2P.LocalNetworkKeepaliveReq packet, NetAddress addrRemote)
	{
		localNetwork.recvKeepaliveReq(packet.getSelfID(), addrRemote);
	}

	public void onUDPPeerServerRecvLocalNetworkKeepaliveRes(UDPPeerServer peer, Packet.P2P.LocalNetworkKeepaliveRes packet, NetAddress addrRemote)
	{
		localNetwork.recvKeepaliveRes(packet.getSelfID());
	}

	public void onUDPPeerServerRecvLocalNetworkShareUpdate(UDPPeerServer peer, Packet.P2P.LocalNetworkShareUpdate packet, NetAddress addrRemote)
	{
		//logger.info("rpcmanager: recv local network share udpate , id is " + packet.getSelfID() 
		//		+ ", addr is " + addrRemote + ", count is " + packet.getDigests().size());
		localNetwork.recvLocalNetworkShareUpdate(packet.getSelfID(), packet.getDigests());
	}

	public void onUDPPeerServerRecvLocalQuery(UDPPeerServer peer, Packet.P2P.LocalQuery packet, NetAddress addrRemote)
	{
		localNetwork.onLocalQuery(packet.getQueryID(), packet.getDigest(), addrRemote);
	}

	public void onUDPPeerServerRecvLocalQueryAnswer(UDPPeerServer peer, Packet.P2P.LocalQueryAnswer packet, NetAddress addrRemote)
	{
		localNetwork.onLocalQueryAnswer(packet.getQueryID(), packet.getProviders());
	}

	public void onUDPPeerServerRecvQueryRequest(UDPPeerServer peer, Packet.P2P.QueryRequest packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onUDPPeerServerRecvQueryResponse(UDPPeerServer peer, Packet.P2P.QueryResponse packet, NetAddress addrRemote)
	{
		onUPSPacketRecv(addrRemote, packet);
	}

	public void onTCPPeerServerOpen(TCPPeerServer peer)
	{
		logger.info("rpcmanager: tps open, addr is " + peer.getServerAddr());
		if( ups.isOpen() && ! ttc.isOpen() )
			connectToTracker();
	}

	public void onTCPPeerServerOpenFailed(TCPPeerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerServerClose(TCPPeerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerServerSessionOpen(TCPPeerServer peer, int sessionid, NetAddress addrClient)
	{
		logger.info("rpcmanager: tcp server session open, sessionid = " + sessionid);
	}

	public void onTCPPeerServerSessionClose(TCPPeerServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		logger.info("rpcmanager: tcp server session close, sessionid = " + sessionid);
		PeerSession session = null;
		synchronized( lockerSession )
		{
			Integer peerSessionID = tpsSessions.remove(sessionid);
			if( peerSessionID == null )
				return;
			session = sessions.remove(peerSessionID);
		}
		if( session != null )
		{	
			addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);
		}
	}

	public void onTCPPeerServerRecvTPSTestQuestion(TCPPeerServer peer, Packet.T2P.TPSTestQuestion packet, int sessionid)
	{
		peer.sendPacket(sessionid, new Packet.P2T.TPSTestAnswer(peerID.kuid));
	}

	public void onTCPPeerServerRecvHello(TCPPeerServer peer, Packet.P2P.Hello packet, int sessionid)
	{
		logger.info("rpcmanager: recv peer hello from tcp server, sessionid = " + sessionid);
		int psid = 0;
		synchronized( lockerSession )
		{
			if( tpsSessions.containsKey(sessionid) )
				return;
			psid = genNewSessionID();
			tpsSessions.put(sessionid, psid);
			sessions.put(psid, new PeerSession(psid, PeerSessionType.ePSTTPS, sessionid, packet.getPeerID()));
		}
		onPeerSessionPassiveOpen(psid, packet.getPeerID(), false);
	}

	public void onTCPPeerServerRecvTPSRCResponse(TCPPeerServer peer, Packet.P2P.TPSRCResponse packet, int sessionid)
	{
		synchronized( lockerSession )
		{
			if( tpsSessions.containsKey(sessionid) )
				return;
		}
		if( ! packet.getOK() )
			return;
		ActiveConnecting ttcc = null;
		synchronized( lockerSession )
		{
			ttcc = activeConnectings.remove(packet.getPeerSessionID());
		}
		if( ttcc != null )
		{
			synchronized( lockerSession )
			{
				tpsSessions.put(sessionid, packet.getPeerSessionID());
				sessions.put(packet.getPeerSessionID(), new PeerSession(packet.getPeerSessionID(), PeerSessionType.ePSTTPS, sessionid, ttcc.peeridRemote));
			}
			tps.sendPacket(sessionid, new Packet.P2P.Hello(peerID));
			onPeerSessionActiveOpen(ttcc.peerSessionID, ttcc.peeridRemote, false, ttcc.aid);			
		}
	}

	public void onTCPPeerServerRecvRegisterRequest(TCPPeerServer peer, Packet.P2P.RegisterRequest packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvRegisterResponse(TCPPeerServer peer, Packet.P2P.RegisterResponse packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvUploadRequest(TCPPeerServer peer, Packet.P2P.UploadRequest packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvUploadResponse(TCPPeerServer peer, Packet.P2P.UploadResponse packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvDataRequest(TCPPeerServer peer, Packet.P2P.DataRequest packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvDataResponse(TCPPeerServer peer, Packet.P2P.DataResponse packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvFinishUpload(TCPPeerServer peer, Packet.P2P.FinishUpload packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvFinishDownload(TCPPeerServer peer, Packet.P2P.FinishDownload packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvGroupQuery(TCPPeerServer peer, Packet.P2P.GroupQuery packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvGroupAnswer(TCPPeerServer peer, Packet.P2P.GroupAnswer packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvQueryRequest(TCPPeerServer peer, Packet.P2P.QueryRequest packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerServerRecvQueryResponse(TCPPeerServer peer, Packet.P2P.QueryResponse packet, int sessionid)
	{
		onTPSPacketRecv(sessionid, packet);
	}

	public void onTCPPeerMonitorServerOpen(TCPPeerMonitorServer peer)
	{
	}

	public void onTCPPeerMonitorServerOpenFailed(TCPPeerMonitorServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerMonitorServerClose(TCPPeerMonitorServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerMonitorServerSessionOpen(TCPPeerMonitorServer peer, int sessionid, NetAddress addrClient)
	{
	}

	public void onTCPPeerMonitorServerSessionClose(TCPPeerMonitorServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerMonitorServerRecvHello(TCPPeerMonitorServer peer, Packet.M2P.Hello packet, int sessionid)
	{
	}

	public void onTCPPeerMonitorServerRecvSnapshotRequest(TCPPeerMonitorServer peer, Packet.M2P.SnapshotRequest packet, int sessionid)
	{
	}

	public void onTCPPeerControlServerOpen(TCPPeerControlServer peer)
	{
	}

	public void onTCPPeerControlServerOpenFailed(TCPPeerControlServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerControlServerClose(TCPPeerControlServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerControlServerSessionOpen(TCPPeerControlServer peer, int sessionid, NetAddress addrClient)
	{
	}

	public void onTCPPeerControlServerSessionClose(TCPPeerControlServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPPeerControlServerRecvHello(TCPPeerControlServer peer, Packet.C2P.Hello packet, int sessionid)
	{
	}

	public void onTCPPeerControlServerRecvRegisterDistDigest(TCPPeerControlServer peer, Packet.C2P.RegisterDistDigest packet, int sessionid)
	{
		// TODO
	}

	public void onTCPPeerControlServerRecvDistFile(TCPPeerControlServer peer, Packet.C2P.DistFile packet, int sessionid)
	{
	}

	public void onTCPPeerControlServerRecvCheckFile(TCPPeerControlServer peer, Packet.C2P.CheckFile packet, int sessionid)
	{
		// TODO
	}

	public void onTCPPeerControlServerRecvDistFileList(TCPPeerControlServer peer, Packet.C2P.DistFileList packet, int sessionid)
	{
		// TODO
	}

	public void onTCPTrackerClientOpen(TCPTrackerClient peer)
	{
		logger.info("rpcmanager: tracker connected, server address is " + peer.getServerAddr());
	}

	public void onTCPTrackerClientOpenFailed(TCPTrackerClient peer, ket.kio.ErrorCode errcode)
	{
		logger.info("rpcmanager: tracker connect failed, reason is " + errcode);
		onTrackerAbort();
	}

	public void onTCPTrackerClientClose(TCPTrackerClient peer, ket.kio.ErrorCode errcode)
	{
		logger.info("rpcmanager: tracker disconnected");
		onTrackerAbort();
	}

	public void onTCPTrackerClientRecvHello(TCPTrackerClient peer, Packet.T2P.Hello packet)
	{
		//
		final String ip = packet.getClientIP();
		int i1 = ip.indexOf(".");
		int i2 = ip.indexOf(".", i1 + 1);
		int i3 = ip.indexOf(".", i2 + 1);
		int b1 = Integer.parseInt(ip.substring(0, i1));
		int b2 = Integer.parseInt(ip.substring(i1+1, i2));
		int b3 = Integer.parseInt(ip.substring(i2+1, i3));
		int b4 = Integer.parseInt(ip.substring(i3+1));
		int pubIP = (b1<<24)|(b2<<16)|(b3<<8)|b4;
		//
		int ranID = (int)((kuid>>>32)&0xffffffff);
		if( pubIP != (int)(kuid&0xffffffff) )
		{
			ranID = genRandomKID();
			kuid = (((long)ranID)<<32)|(0xffffffffL&(long)pubIP);
		}
		peer.sendPacket(new Packet.P2T.LoginRequest(new SBean.PeerLoginRequest(1,
				packet.getClientIP(), kuid,
				this.peer.getConfig().bSuper ? PeerID.PF_SUPER : PeerID.PF_NULL,
				peer.getClientAddr().host,
				tps.getServerAddr().port,
				ups.getBoundAddr().port)));
	}

	public void onTCPTrackerClientRecvLoginResponse(TCPTrackerClient peer, Packet.T2P.LoginResponse packet)
	{
		if( packet.getRet() == Packet.T2P.LoginResponse.RetCode.eLoginOK )
		{
			peerID = packet.getPeerID();
			if( peerID.kuid != kuid ) // TODO shutdown?
				logger.error("tracker: peerID.kuid != kuid!!!\n");
			logger.info("rpcmanager: login ok, my peerid is " + peerID);
			this.peer.getDataManager().onTrackerConnect();
			
			NetAddress addrServer = ttc.getServerAddr();
			final NetAddress addrClient = ttc.getClientAddr();
			if( tps.isOpen() && ! RPCManager.this.peer.getConfig().bTPSDisable )
			{
				if( addrServer.isLoopback()
					|| addrServer.isPrivate() 
					|| !addrServer.isPrivate() && ! addrClient.isPrivate() )
						ttc.sendPacket(new Packet.P2T.TPSTestRequest(tps.getServerAddr().port));
				else if( ! addrServer.isLoopback() && ! addrServer.isPrivate() && addrClient.isPrivate() )
				{
					ket.kiox.Factory.createLanService().addMapPortTask(new ILanService.IMapPortTask()
							{

								@Override
								public String getProtoType() { return "TCP"; }

								@Override
								public String getInternalIP() { return addrClient.host; }

								@Override
								public int getInternalPort() { return tps.getServerAddr().port; }

								@Override
								public int getExternalPort() { return tps.getServerAddr().port; }

								@Override
								public void onComplete(int errcode)
								{								
									if( errcode == 0 )
										ttc.sendPacket(new Packet.P2T.TPSTestRequest(tps.getServerAddr().port));
									else if( ups.isOpen() && ! RPCManager.this.peer.getConfig().bUPSDisable )
										ttc.sendPacket(new Packet.P2T.UPSTestRequest());
								}
						
							}
					);
				}
			}
			else if( ups.isOpen() && ! RPCManager.this.peer.getConfig().bUPSDisable )
				ttc.sendPacket(new Packet.P2T.UPSTestRequest());
		}
		else
			ttc.close();
	}

	public void onTCPTrackerClientRecvTPSTestResponse(TCPTrackerClient peer, Packet.T2P.TPSTestResponse packet)
	{
		if( packet.getPeerID().testFlag(PeerID.PF_TPS) )
		{
			peerID = packet.getPeerID();
			logger.info("rpcmanager: peer tcp server test ok, my peerid is " + peerID);
		}
		else
		{
			logger.info("rpcmanager: peer tcp server test failed");
			// TODO never close me
			// if( tps.isOpen() )
			//	tps.close();
		}
	}

	public void onTCPTrackerClientRecvUPSTestResponse(TCPTrackerClient peer, Packet.T2P.UPSTestResponse packet)
	{
		if( packet.getRet() == 0 )
		{
			peerID = packet.getPeerID();
			logger.info("rpcmanager: peer udp server test ok, my peerid is " + peerID);
		}
		else
			logger.info("rpcmanager: peer udp server test failed");
	}

	public void onTCPTrackerClientRecvUPSTestQuestion(TCPTrackerClient peer, Packet.T2P.UPSTestQuestion packet)
	{
		if( this.peer.getConfig().bUPSDisable )
			return;
		Packet.P2T.UPSTestAnswer pustr = new Packet.P2T.UPSTestAnswer(peerID.kuid);
		NetAddress addr = ttc.getServerAddr();
		NetAddress addrAssistant = new NetAddress(addr.host, packet.getAssistantPort());
		for(int i=0; i< UPS_TEST_RESPONSE_COUNT; ++i)
		{
			ups.sendPacket(addr, pustr);
			ups.sendPacket(addrAssistant, pustr);
		}
	}

	public void onTCPTrackerClientRecvSourceAnswer(TCPTrackerClient peer, Packet.T2P.SourceAnswer packet)
	{
		logger.info("rpcmanager: on source answer, digest is " + packet.getDigest() + ", providers count is " + packet.getProviders().size());
		this.peer.getDataManager().setSourceAnswer(packet.getDigest(), packet.getProviders());
	}

	public void onTCPTrackerClientRecvTPSRCResponse(TCPTrackerClient peer, Packet.T2P.TPSRCResponse packet)
	{
		if( packet.getOK() )
			return;
		ActiveConnecting ttcc = null;
		synchronized( lockerSession )
		{
			ttcc = activeConnectings.remove(packet.getPeerSessionID());
		}
		if( ttcc != null )
		{	
			addTask(new OpenFailedTask(ttcc.peeridRemote, ttcc.aid), true);
		}
	}

	public void onTCPTrackerClientRecvTPSRCRequest(TCPTrackerClient peer, Packet.T2P.TPSRCRequest packet)
	{
		int localPeerSessionID = genNewSessionID();
		TCPPeerClient tpc = new TCPPeerClient(this, localPeerSessionID, packet.getPeerSessionID(), false, packet.getDemanderPeerID(), peerID);
		tpc.setServerAddr(packet.getDemanderPeerID().addr);			
		tpc.open();
	}

	public void onTCPTrackerClientRecvUPSRCRequest(TCPTrackerClient peer, Packet.T2P.UPSRCRequest packet)
	{
		addTask(new PunchTask(packet.getDemanderPeerID().addr, new Packet.P2P.UPSRCResponse(true, packet.getPeerSessionID())), true);
	}

	public void onTCPTrackerClientRecvUPSRCResponse(TCPTrackerClient peer, Packet.T2P.UPSRCResponse packet)
	{
		ActiveConnecting ttcc = null;
		synchronized( lockerSession )
		{
			ttcc = activeConnectings.remove(packet.getPeerSessionID());
		}
		if( ttcc != null )
		{	
			if( packet.getOK() )
			{
				synchronized( lockerSession )
				{
					udpSessionIDs.put(ttcc.peeridRemote.addr, packet.getPeerSessionID());
					sessions.put(packet.getPeerSessionID(), new PeerSession(packet.getPeerSessionID(), PeerSessionType.ePSTUPS, ttcc.peeridRemote));
				}
				ups.sendPacket(ttcc.peeridRemote.addr, new Packet.P2P.Hello(peerID));
				onPeerSessionActiveOpen(ttcc.peerSessionID, ttcc.peeridRemote, false, ttcc.aid);
			}
			else
			{
				addTask(new OpenFailedTask(ttcc.peeridRemote, ttcc.aid), true);
			}
		}
	}

	public void onTCPTrackerClientRecvRelayClose(TCPTrackerClient peer, Packet.T2P.RelayClose packet)
	{
		PeerSession session = null;
		synchronized( lockerSession )
		{
			Integer sid = relaySessionIDs.remove(packet.getPeerID().kuid);
			if( sid == null )
				return;
			session = sessions.remove(sid);
		}
		if( session != null )
			addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);
	}

	public void onTCPTrackerClientRecvRelay(TCPTrackerClient peer, Packet.T2P.Relay packet)
	{
		PeerSession session = null;
		boolean bPassiveOpen = false;
		synchronized( lockerSession )
		{
			Integer sid = relaySessionIDs.get(packet.getSourcePeerID().kuid);
			if( sid != null )
				session = sessions.get(sid);
			else
			{
				int peerSessionID = genNewSessionID();
				relaySessionIDs.put(packet.getSourcePeerID().kuid, peerSessionID);
				session = new PeerSession(peerSessionID, PeerSessionType.ePSTRelay, packet.getSourcePeerID());
				sessions.put(peerSessionID, session);
				bPassiveOpen = true;
			}
		}
		if( bPassiveOpen )
			onPeerSessionPassiveOpen(session.peerSessionID, packet.getSourcePeerID(), true);
		if( session != null )
		{
			Stream.AIStream is = new Stream.IStreamLE(new ByteArrayInputStream(packet.getData().array(), packet.getData().position(), packet.getData().remaining()));
			try
			{
				int ptype = is.popInteger();
				ByteBuffer bb = is.popByteBuffer();
				Stream.AIStream is2 = new Stream.IStreamLE(new ByteArrayInputStream(bb.array(), 0, bb.limit()));
				SimplePacket spacket = null;
				switch( ptype )
				{
				case Packet.eP2PPKTRegisterRequest:
					{
						Packet.P2P.RegisterRequest p = new Packet.P2P.RegisterRequest();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTRegisterResponse:
					{
						Packet.P2P.RegisterResponse p = new Packet.P2P.RegisterResponse();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTUploadRequest:
					{
						Packet.P2P.UploadRequest p = new Packet.P2P.UploadRequest();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTUploadResponse:
					{
						Packet.P2P.UploadResponse p = new Packet.P2P.UploadResponse();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTDataRequest:
					{
						Packet.P2P.DataRequest p = new Packet.P2P.DataRequest();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTDataResponse:
					{
						Packet.P2P.DataResponse p = new Packet.P2P.DataResponse();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTFinishUpload:
					{
						Packet.P2P.FinishUpload p = new Packet.P2P.FinishUpload();
						p.decode(is2);
						spacket = p;	
					}
					break;
				case Packet.eP2PPKTFinishDownload:
					{
						Packet.P2P.FinishDownload p = new Packet.P2P.FinishDownload();
						p.decode(is2);
						spacket = p;	
					}
					break;
				default:
					return;
				}
				onPeerSessionPacketRecv(session, spacket);
			}
			catch(java.lang.Exception ex)
			{
			}
		}
	}

	public void onTCPTrackerClientRecvDataGroupMemberQuery(TCPTrackerClient peer, Packet.T2P.DataGroupMemberQuery packet)
	{
		List<Digest> lst = this.peer.getDataManager().queryDataGroupMembers(packet.getGroup(), packet.getSection());
		if( lst != null )
			ttc.sendPacket(new Packet.P2T.DataGroupMemberAnswer(packet.getGroup(), packet.getSection(), lst));
	}

	public void onTCPTrackerClientRecvNoticeLocalNetwork(TCPTrackerClient peer, Packet.T2P.NoticeLocalNetwork packet)
	{
		logger.info("rpcmanager: recv local network head notice : " + packet.getNotice());
		localNetwork.recvHeadNotice(packet.getNotice());		
	}

	public void onTCPTrackerClientRecvTestNetworkTCP(TCPTrackerClient peer, Packet.T2P.TestNetworkTCP packet)
	{
		logger.info("rpcmanager: recv test network tcp request : " + packet.getTest());
		//
		SBean.NetworkTCPTest ntt = packet.getTest();
		if( ntt.headID != peerID.kuid )
			return;
		NTTestClient client = new NTTestClient(this, ntt.addr.host, false);
		client.setServerAddr(new NetAddress(ntt.addr.host, ntt.addr.port));
		logger.info("rpcmanager: try open local tcp server : " + ntt.addr);
		client.open();
	}

	public void onTCPTrackerClientRecvTrackerRedirect(TCPTrackerClient peer, Packet.T2P.TrackerRedirect packet)
	{
		peer.setRedirectInfo(packet.getInfo());
		peer.close();
	}

	public void onTCPPeerClientOpen(TCPPeerClient peer)
	{
		logger.info("rpcmanager: tcp client open");
		if( peer.getDirect() )
		{
			peer.sendPacket(new Packet.P2P.Hello(peer.getLocalPeerID())); // TODO
			onTPCConnected(peer);
		}
		else
		{
			peer.sendPacket(new Packet.P2P.TPSRCResponse(true, peer.getRemotePeerSessionID()));
		}
	}

	public void onTCPPeerClientOpenFailed(TCPPeerClient peer, ket.kio.ErrorCode errcode)
	{
		logger.info("rpcmanager: tcp client open failed");
		if( peer.getDirect() )
		{
			ActiveConnecting ttcc = null;
			synchronized( lockerSession )
			{
				ttcc = activeConnectings.remove(peer.getLocalPeerSessionID());
			}
			if( ttcc != null )
			{
				addTask(new OpenFailedTask(ttcc.peeridRemote, ttcc.aid), true);
			}
		}
		else
		{
			ttc.sendPacket(new Packet.P2T.TPSRCResponse(peer.getRemotePeerID(), peer.getRemotePeerSessionID(), false));
		}
	}

	public void onTCPPeerClientClose(TCPPeerClient peer, ket.kio.ErrorCode errcode)
	{
		logger.info("rpcmanager: tcp client close");
		if( peer.getDirect() )
		{
			PeerSession session = null;
			synchronized( lockerSession )
			{
				session = sessions.remove(peer.getLocalPeerSessionID());
			}
			if( session != null )
			{	
				addTask(new CloseTask(session.peerSessionID, session.peeridRemote), true);
			}
		}
	}

	public void onTCPPeerClientRecvHello(TCPPeerClient peer, Packet.P2P.Hello packet)
	{
		if( ! peer.getDirect() )
			onTPCConnected(peer);
	}

	public void onTCPPeerClientRecvRegisterRequest(TCPPeerClient peer, Packet.P2P.RegisterRequest packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvRegisterResponse(TCPPeerClient peer, Packet.P2P.RegisterResponse packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvUploadRequest(TCPPeerClient peer, Packet.P2P.UploadRequest packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvUploadResponse(TCPPeerClient peer, Packet.P2P.UploadResponse packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvDataRequest(TCPPeerClient peer, Packet.P2P.DataRequest packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvDataResponse(TCPPeerClient peer, Packet.P2P.DataResponse packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvFinishUpload(TCPPeerClient peer, Packet.P2P.FinishUpload packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvFinishDownload(TCPPeerClient peer, Packet.P2P.FinishDownload packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvGroupQuery(TCPPeerClient peer, Packet.P2P.GroupQuery packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvGroupAnswer(TCPPeerClient peer, Packet.P2P.GroupAnswer packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvQueryRequest(TCPPeerClient peer, Packet.P2P.QueryRequest packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onTCPPeerClientRecvQueryResponse(TCPPeerClient peer, Packet.P2P.QueryResponse packet)
	{
		if( peer.getDirect() )
			onTPCPacketRecv(peer.getLocalPeerSessionID(), packet);
	}

	public void onNTTestClientOpen(NTTestClient peer)
	{
		peer.setOK(true);
		peer.close();
	}

	public void onNTTestClientOpenFailed(NTTestClient peer, ket.kio.ErrorCode errcode)
	{
		ttc.sendPacket(new Packet.P2T.TestNetworkTCP(peer.getDstHost(), peer.getOK()));
	}

	public void onNTTestClientClose(NTTestClient peer, ket.kio.ErrorCode errcode)
	{
		ttc.sendPacket(new Packet.P2T.TestNetworkTCP(peer.getDstHost(), peer.getOK()));
	}

//	public void onTCPPeerControlServerRecvRegisterDistDir(TCPPeerControlServer peer, Packet.C2P.RegisterDistDir packet, int sessionid)
//	{
//	}
//
	//// end handlers.
	
	public NetManager getNetManager()
	{
		return managerNet;
	}
	
	private int genRandomKID()
	{
		return random.nextInt() & 0xffff;
	}

	private Peer peer;
	private long kuid = 0;
	private PeerID peerID = new PeerID();
	private NetManager managerNet = new NetManager();
	private TrackerSelector trackerSelector = new TrackerSelector();
	private TCPTrackerClient ttc = new TCPTrackerClient(this);
	private long ttcReconnectTime = 0;
	private TCPPeerServer tps = new TCPPeerServer(this);
	private UDPPeerServer ups = new UDPPeerServer(this);
	private Object lockerSession = new Object();
	private Object lockerRPC = new Object();
	private CheckSessionTask cst = new CheckSessionTask();
	private Map<Integer, Long> mapS2P = new HashMap<Integer, Long>();
	private Map<Long, PeerInfo> peerinfos = new HashMap<Long, PeerInfo>();
	private Map<Digest, QueryResponse> peerQueryStubs = new HashMap<Digest, QueryResponse>();
	private Set<Long> uploadingPeerIDs = new HashSet<Long>();
	private int lastLoadReportVal = 0;
	private long lastLoadReportTime = 0;
	private AtomicInteger seedAID = new AtomicInteger();
	private TickTask tt = new TickTask();
	private CheckSessionTask2 cst2 = new CheckSessionTask2();
	private AtomicInteger seedPSID = new AtomicInteger();
	private Map<Integer, ActiveConnecting> activeConnectings = new HashMap<Integer, ActiveConnecting>();
	private Map<Integer, PeerSession> sessions = new HashMap<Integer, PeerSession>();
	private Map<Integer, Integer> tpsSessions = new HashMap<Integer, Integer>();
	private Map<Long, Integer> relaySessionIDs = new HashMap<Long, Integer>();
	private Map<NetAddress, Integer> udpSessionIDs = new HashMap<NetAddress, Integer>();
	private LocalNetwork localNetwork = new LocalNetwork();
	private Random random = new Random();
	private Logger logger = Logger.getLogger("kpx");
}

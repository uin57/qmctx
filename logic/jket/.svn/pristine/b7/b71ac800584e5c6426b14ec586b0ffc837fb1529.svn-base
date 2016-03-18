// modified by ket.kio.RPCGen at Thu May 31 13:27:49 CST 2012.

package ket.kpc.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.io.File;

import org.apache.log4j.Logger;

import ket.util.SKVMap;
import ket.kio.BindPolicy;
import ket.kio.NetManager;
import ket.kio.Statistic;
import ket.kio.NetAddress;
import ket.kpc.SBean;
import ket.kpc.ITracker;
import ket.kpc.TrackerConfig;
import ket.kpc.common.PeerID;
import ket.kpc.rpc.Packet;

public class Tracker implements ITracker
{

	private static final int VERSION = 1;
	
	private class CheckTCPTask implements Runnable
	{
		@Override
		public void run()
		{
			managerNet.checkIdleConnections();
		}
		
		public ScheduledFuture<?> future = null;
		
	}
	
	private class DumpTask implements Runnable
	{
		@Override
		public void run()
		{
			managerData.dump();
			Statistic statNew = new Statistic();
			tts.getStatistic(statNew);
			System.out.println("=== upload: " + statNew.nBytesSend + " bytes, " +
					(statNew.nBytesSend - stat.nBytesSend)/10000+ " k/s === download: " +
					statNew.nBytesRecv + " bytes, " +
					(statNew.nBytesRecv - stat.nBytesRecv)/10000+ " k/s ===========");
			statNew = stat;
			System.out.println("================================================================\n");
		}
		
		public ScheduledFuture<?> future = null;
		
	}

	@Override
	public TrackerConfig getConfig()
	{
		return cfg;
	}
	
	@Override
	public void setConfig(TrackerConfig config)
	{
		if( config != null )
			cfg = config;
	}
	
	@Override
	public void setConfig(File configFile)
	{
		SKVMap skv = new SKVMap();
		if( ! skv.load(configFile) )
			return;
		
		cfg.addrListen.host = skv.getString("TrackerServer", "host", cfg.addrListen.host);
		cfg.addrListen.port = skv.getInteger("TrackerServer", "port", cfg.addrListen.port);
		cfg.maxidlePeerSession = skv.getInteger("TrackerServer", "maxidle", cfg.maxidlePeerSession);
		cfg.bUPSDisable = skv.getBoolean("TrackerPolicy", "upsdisable", cfg.bUPSDisable);
	}
	
	private void dumpConfig()
	{
		System.out.println("TrackerConfig.dump()");
		System.out.println("\ttracker server host is " + cfg.addrListen.host);
		System.out.println("\ttracker server port is " + cfg.addrListen.port);
		System.out.println("\tmax idle time of peer session is " + cfg.maxidlePeerSession);
	}
	

	@Override
	public void start()
	{
		dumpConfig();
		
		managerNet.start();
				
		uts.setBindAddr(cfg.addrListen, BindPolicy.eReuseTimewait);
		uts.open();
		
		auts.setBindAddr(new NetAddress(cfg.addrListen.host, 0), BindPolicy.eLoopIncPort);
		auts.open();
		
		tts.setListenAddr(cfg.addrListen, BindPolicy.eReuseTimewait);
		tts.open();
		
		try
		{
			ctt.future = executor.scheduleAtFixedRate(ctt, 1, 1, TimeUnit.SECONDS);
			dt.future = executor.scheduleAtFixedRate(dt, 5, 5, TimeUnit.SECONDS);
		}
		catch(RejectedExecutionException ex)
		{			
		}
	}
	
	// destroy
	@Override
	public void destroy()
	{
		managerNet.destroy();
		
		if( ctt.future != null )
			ctt.future.cancel(false);
		if( dt.future != null )
			dt.future.cancel(false);		
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
	}
	
	
	
	public int getVersion()
	{
		return VERSION;
	}
	
	public DataManager getDataManager()
	{
		return managerData;
	}
	
	public TCPTrackerServer getTCPTrackerServer()
	{
		return tts;
	}
	
	public UDPTrackerServer getUDPTrackerServer()
	{
		return uts;
	}
	
	public AssistantUDPTrackerServer getAssistantUDPTrackerServer()
	{
		return auts;
	}
	
	public NetManager getNetManager()
	{
		return managerNet;
	}
	
	public ScheduledExecutorService getExecutor()
	{
		return executor;
	}
	
	private void addTask(Runnable r, boolean e)
	{
		try
		{
			executor.execute(r);
		}
		catch(RejectedExecutionException ex)
		{
			if( e )
				r.run();
		}
	}
	
	public Logger getLogger()
	{
		return logger;
	}
	
	public void noticeLocalNetwork(final int sid, final SBean.LocalNetworkNotice lnn)
	{
		addTask(new Runnable()
			{
				@Override
				public void run()
				{
					tts.sendPacket(sid, new Packet.T2P.NoticeLocalNetwork(lnn));
				}
			}, false);		
	}
	
	public void testNetworkTCP(final int sid, final SBean.NetworkTCPTest ntt)
	{
		addTask(new Runnable()
			{
				@Override
				public void run()
				{
					tts.sendPacket(sid, new Packet.T2P.TestNetworkTCP(ntt));
				}
			}, false);		
	}
	
	//// begin handlers.
	public void onUDPTrackerServerOpen(UDPTrackerServer peer)
	{
		System.out.println("UDPTrackerServer.onOpen(), bound address is " + peer.getBoundAddr());
	}

	public void onUDPTrackerServerOpenFailed(UDPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onUDPTrackerServerClose(UDPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onUDPTrackerServerRecvUPSTestAnswer(UDPTrackerServer peer, Packet.P2T.UPSTestAnswer packet, NetAddress addrRemote)
	{
		managerData.addUPSTestResponse(packet.getKey(), addrRemote, false);
	}

	public void onUDPTrackerServerRecvPing(UDPTrackerServer peer, Packet.P2T.Ping packet, NetAddress addrRemote)
	{
		peer.sendPacket(addrRemote, new Packet.T2P.Ping(packet.getSeq(), 0/*TODO*/));
	}

	public void onAssistantUDPTrackerServerOpen(AssistantUDPTrackerServer peer)
	{
		System.out.println("UDPTrackerAssistantServer.onOpen(), bound address is " + peer.getBoundAddr());
	}

	public void onAssistantUDPTrackerServerOpenFailed(AssistantUDPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onAssistantUDPTrackerServerClose(AssistantUDPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onAssistantUDPTrackerServerRecvUPSTestAnswer(AssistantUDPTrackerServer peer, Packet.P2T.UPSTestAnswer packet, NetAddress addrRemote)
	{
		managerData.addUPSTestResponse(packet.getKey(), addrRemote, true);
	}

	public int getTCPTrackerServerMaxConnectionIdleTime()
	{
		return cfg.maxidlePeerSession * 1000;
	}

	public void onTCPTrackerServerOpen(TCPTrackerServer peer)
	{
		System.out.println("TCPTrackerServer.onOpen(), server address is " + peer.getServerAddr());
	}

	public void onTCPTrackerServerOpenFailed(TCPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
	}

	public void onTCPTrackerServerClose(TCPTrackerServer peer, ket.kio.ErrorCode errcode)
	{
		System.out.println("TCPTrackerServer.onClose()");
	}

	public void onTCPTrackerServerSessionOpen(TCPTrackerServer peer, int sessionid, NetAddress addrClient)
	{
		peer.sendPacket(sessionid, new Packet.T2P.Hello(VERSION, addrClient.host));
		// TODO
		//peer.sendPacket(sessionid, new Packet.T2P.TrackerRedirect(new SBean.TrackerRedirectInfo(
		//		new ket.kio.SBean.NetAddress("168.160.224.81", 1106))));
	}

	public void onTCPTrackerServerSessionClose(TCPTrackerServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		Long key = null;
		synchronized( mapS2K )
		{
			key = mapS2K.remove(sessionid);
			if( key == null )
				return;
		}
		managerData.removePeer(key.longValue());
	}

	public void onTCPTrackerServerRecvLoginRequest(TCPTrackerServer peer, Packet.P2T.LoginRequest packet, int sessionid)
	{
		synchronized( mapS2K )
		{
			if( mapS2K.containsKey(sessionid) )
				return;
		}
		// TODO check req.pubIP !!!
		PeerID peerid = managerData.login(sessionid, packet.getReq());
		if( peerid == null )
			peer.sendPacket(sessionid, new Packet.T2P.Hello(VERSION, packet.getReq().pubIP));
		else
		{
			synchronized( mapS2K )
			{
				mapS2K.put(sessionid, peerid.kuid);
			}
			logger.info("tracker: peer " + peerid + " login OK!");
			peer.sendPacket(sessionid, new Packet.T2P.LoginResponse(Packet.T2P.LoginResponse.RetCode.eLoginOK, peerid));
		}
	}

	public void onTCPTrackerServerRecvTPSTestRequest(TCPTrackerServer peer, Packet.P2T.TPSTestRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		managerData.requestTPSTest(key, packet.getPort());
	}

	public void onTCPTrackerServerRecvUPSTestRequest(TCPTrackerServer peer, Packet.P2T.UPSTestRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		managerData.requestUPSTest(key);
	}

	public void onTCPTrackerServerRecvShareUpdateRequest(TCPTrackerServer peer, Packet.P2T.ShareUpdateRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		if( packet.getAddOrRemove() )
			managerData.addSource(key, packet.getDigest());
		else
			managerData.removeSource(key, packet.getDigest());
	}

	public void onTCPTrackerServerRecvSourceQuery(TCPTrackerServer peer, Packet.P2T.SourceQuery packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		List<PeerID> providers = new ArrayList<PeerID>();
		if( managerData.querySource(packet.getDigest(), key, providers) != 0 )
		{
			peer.sendPacket(sessionid, new Packet.T2P.SourceAnswer(packet.getDigest(), providers));
		}
	}

	public void onTCPTrackerServerRecvTPSRCRequest(TCPTrackerServer peer, Packet.P2T.TPSRCRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		int[] sid = new int[1];
		PeerID peerid = managerData.getLogonPeer(key, sid);
		if( peerid == null )
		{
			peer.closeSession(sessionid);
			return;
		}
		if( managerData.getLogonPeer(packet.getTargetPeerID().kuid, sid) == null )
			peer.sendPacket(sessionid, new Packet.T2P.TPSRCResponse(false, packet.getPeerSessionID()));
		else
			peer.sendPacket(sid[0], new Packet.T2P.TPSRCRequest(peerid, packet.getPeerSessionID()));
	}

	public void onTCPTrackerServerRecvTPSRCResponse(TCPTrackerServer peer, Packet.P2T.TPSRCResponse packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		int[] sid = new int[1];
		PeerID peerid = managerData.getLogonPeer(key, sid);
		if( peerid == null )
		{
			peer.closeSession(sessionid);
			return;
		}
		if( managerData.getLogonPeer(packet.getDemanderPeerID().kuid, sid) == null )
			return;
		peer.sendPacket(sid[0], new Packet.T2P.TPSRCResponse(packet.getOK(), packet.getPeerSessionID()));
	}

	public void onTCPTrackerServerRecvUPSRCRequest(TCPTrackerServer peer, Packet.P2T.UPSRCRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		int[] sid = new int[1];
		PeerID peerid = managerData.getLogonPeer(key, sid);
		if( peerid == null )
		{
			peer.closeSession(sessionid);
			return;
		}
		if( managerData.getLogonPeer(packet.getTargetPeerID().kuid, sid) == null )
			peer.sendPacket(sessionid, new Packet.T2P.UPSRCResponse(false, packet.getPeerSessionID()));
		else
			peer.sendPacket(sid[0], new Packet.T2P.UPSRCRequest(peerid, packet.getPeerSessionID()));
	}

	public void onTCPTrackerServerRecvRelay(TCPTrackerServer peer, Packet.P2T.Relay packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		int[] sid = new int[1];
		PeerID peerid = managerData.getLogonPeer(key, sid);
		if( peerid == null )
		{
			peer.closeSession(sessionid);
			return;
		}
		if( managerData.getLogonPeer(packet.getTargetPeerID().kuid, sid) == null )
			peer.sendPacket(sessionid, new Packet.T2P.RelayClose(packet.getTargetPeerID()));
		else
		{
			peer.sendPacket(sid[0], new Packet.T2P.Relay(peerid, packet.getData()));
		}
	}

	public void onTCPTrackerServerRecvCreateDataGroup(TCPTrackerServer peer, Packet.P2T.CreateDataGroup packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		SBean.Section section = managerData.createDataGroup(key, packet.getGroup(), packet.getMemberCount());
		if( section != null )
			peer.sendPacket(sessionid, new Packet.T2P.DataGroupMemberQuery(packet.getGroup(), section));
	}

	public void onTCPTrackerServerRecvDataGroupMemberAnswer(TCPTrackerServer peer, Packet.P2T.DataGroupMemberAnswer packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		SBean.Section section = managerData.setDataGroupMembers(key, packet.getGroup(), packet.getSection(), packet.getMembers());
		if( section != null )
			peer.sendPacket(sessionid, new Packet.T2P.DataGroupMemberQuery(packet.getGroup(), section));
	}

	public void onTCPTrackerServerRecvGroupShareUpdateRequest(TCPTrackerServer peer, Packet.P2T.GroupShareUpdateRequest packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		managerData.groupAddSource(key, packet.getGroup(), packet.getList());
	}

	public void onTCPTrackerServerRecvTestNetworkTCP(TCPTrackerServer peer, Packet.P2T.TestNetworkTCP packet, int sessionid)
	{
		managerData.setTestNetworkTCPResult(sessionid, packet.getHost(), packet.getOK());
	}

	public void onTCPTrackerServerRecvLoadReport(TCPTrackerServer peer, Packet.P2T.LoadReport packet, int sessionid)
	{
		Long key = getSessionKey(sessionid);
		if( key == null )
			return;
		managerData.onPeerLoadReport(key, packet.getLoadInfo());
	}

	public void onTPSTestClientOpen(TPSTestClient peer)
	{
		peer.sendPacket(new Packet.T2P.TPSTestQuestion());
	}

	public void onTPSTestClientOpenFailed(TPSTestClient peer, ket.kio.ErrorCode errcode)
	{
		int[] sid = new int[1];
		PeerID peerid = managerData.setPeerTCPServerTestResult(peer.getKey(), peer.getServerAddr().port, peer.getOK(), sid);
		if( peerid == null )
			return;
		logger.info("tracker: peer " + peerid + " test tcp server " + peer.getOK() + "!");
		tts.sendPacket(sid[0], new Packet.T2P.TPSTestResponse(peerid));
	}

	public void onTPSTestClientClose(TPSTestClient peer, ket.kio.ErrorCode errcode)
	{
		int[] sid = new int[1];
		PeerID peerid = managerData.setPeerTCPServerTestResult(peer.getKey(), peer.getServerAddr().port, peer.getOK(), sid);
		if( peerid == null )
			return;
		logger.info("tracker: peer " + peerid + " test tcp server " + peer.getOK() + "!");
		tts.sendPacket(sid[0], new Packet.T2P.TPSTestResponse(peerid));
	}

	public void onTPSTestClientRecvTPSTestAnswer(TPSTestClient peer, Packet.P2T.TPSTestAnswer packet)
	{
		peer.setOK(packet.getKey() == peer.getKey());
		peer.close();
	}
	
	private Long getSessionKey(int sessionid)
	{
		Long key = null;
		synchronized( mapS2K )
		{
			key = mapS2K.get(sessionid);
			if( key == null )
			{
				tts.closeSession(sessionid);
				return null;
			}
		}
		return key;
	}

	//// end handlers.
	
	private NetManager managerNet = new NetManager();
	private DataManager managerData = new DataManager(this);
	private TCPTrackerServer tts = new TCPTrackerServer(this);
	private UDPTrackerServer uts = new UDPTrackerServer(this);
	private AssistantUDPTrackerServer auts = new AssistantUDPTrackerServer(this);
	private Map<Integer, Long> mapS2K = new HashMap<Integer, Long>();
	private TrackerConfig cfg = new TrackerConfig();
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private CheckTCPTask ctt = new CheckTCPTask();
	private DumpTask dt = new DumpTask();
	private Statistic stat = new Statistic();
	private Logger logger = Logger.getLogger("kpx");
}

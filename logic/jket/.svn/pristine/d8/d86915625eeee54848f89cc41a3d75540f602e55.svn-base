// modified by ket.kio.RPCGen at Thu Jan 17 11:22:09 CST 2013.

package ket.kpc.rpc;

import java.util.List;
import java.nio.ByteBuffer;

import ket.util.Stream;
import ket.kio.SimplePacket;

import ket.kpc.SBean;
public abstract class Packet
{

	// tracker to peer
	public static final int eT2PPKTHello = 1;
	public static final int eT2PPKTLoginResponse = 2;
	public static final int eT2PPKTTPSTestQuestion = 3;
	public static final int eT2PPKTTPSTestResponse = 4;
	public static final int eT2PPKTUPSTestQuestion = 5;
	public static final int eT2PPKTUPSTestResponse = 6;
	public static final int eT2PPKTSourceAnswer = 7;
	public static final int eT2PPKTTPSRCResponse = 8;
	public static final int eT2PPKTTPSRCRequest = 9;
	public static final int eT2PPKTRelay = 10;
	public static final int eT2PPKTUPSRCResponse = 11;
	public static final int eT2PPKTUPSRCRequest = 12;
	public static final int eT2PPKTRelayClose = 13;
	public static final int eT2PPKTDataGroupMemberQuery = 14;
	public static final int eT2PPKTNoticeLocalNetwork = 15;
	public static final int eT2PPKTTestNetworkTCP = 16;
	public static final int eT2PPKTTrackerRedirect = 17;
	public static final int eT2PPKTPing = 18;

	// peer to tracker
	public static final int eP2TPKTLoginRequest = 1001;
	public static final int eP2TPKTTPSTestRequest = 1002;
	public static final int eP2TPKTTPSTestAnswer = 1003;
	public static final int eP2TPKTUPSTestRequest = 1004;
	public static final int eP2TPKTUPSTestAnswer = 1005;
	public static final int eP2TPKTShareUpdateRequest = 1006;
	public static final int eP2TPKTSourceQuery = 1007;
	public static final int eP2TPKTTPSRCRequest = 1008;
	public static final int eP2TPKTTPSRCResponse = 1009;
	public static final int eP2TPKTRelay = 1010;
	public static final int eP2TPKTUPSRCRequest = 1011;
	public static final int eP2TPKTCreateDataGroup = 1012;
	public static final int eP2TPKTDataGroupMemberAnswer = 1013;
	public static final int eP2TPKTGroupShareUpdateRequest = 1014;
	public static final int eP2TPKTTestNetworkTCP = 1017;
	public static final int eP2TPKTPing = 1018;
	public static final int eP2TPKTLoadReport = 1019;

	// peer to peer
	public static final int eP2PPKTHello = 2001;
	public static final int eP2PPKTTPSRCResponse = 2002;
	public static final int eP2PPKTUPSRCResponse = 2003;
	public static final int eP2PPKTUPSPunch = 2004;
	public static final int eP2PPKTUPSClose = 2005;
	public static final int eP2PPKTRegisterRequest = 2006;
	public static final int eP2PPKTRegisterResponse = 2007;
	public static final int eP2PPKTUploadRequest = 2008;
	public static final int eP2PPKTUploadResponse = 2009;
	public static final int eP2PPKTDataRequest = 2010;
	public static final int eP2PPKTDataResponse = 2011;
	public static final int eP2PPKTFinishUpload = 2012;
	public static final int eP2PPKTFinishDownload = 2013;
	public static final int eP2PPKTGroupQuery = 2014;
	public static final int eP2PPKTGroupAnswer = 2015;
	public static final int eP2PPKTNoticeLocalNetworkMember = 2016;
	public static final int eP2PPKTLocalNetworkKeepaliveReq = 2017;
	public static final int eP2PPKTLocalNetworkKeepaliveRes = 2018;
	public static final int eP2PPKTLocalNetworkShareUpdate = 2019;
	public static final int eP2PPKTLocalQuery = 2020;
	public static final int eP2PPKTLocalQueryAnswer = 2021;
	public static final int eP2PPKTQueryRequest = 2022;
	public static final int eP2PPKTQueryResponse = 2023;

	// monitor to peer
	public static final int eM2PPKTHello = 3001;
	public static final int eM2PPKTSnapshotRequest = 3002;

	// peer to monitor
	public static final int eP2MPKTHello = 3101;
	public static final int eP2MPKTSnapshotResponse = 3102;

	// control to peer
	public static final int eC2PPKTHello = 4001;
	public static final int eC2PPKTRegisterDistDigest = 4002;
	public static final int eC2PPKTDistFile = 4003;
	public static final int eC2PPKTCheckFile = 4004;
	public static final int eC2PPKTDistFileList = 4005;

	// peer to control
	public static final int eP2CPKTHello = 4501;
	public static final int eP2CPKTDistFile = 4502;
	public static final int eP2CPKTDistFileFrag = 4503;
	public static final int eP2CPKTDistFileUpdate = 4504;
	public static final int eP2CPKTCheckFile = 4505;
	public static final int eP2CPKTCheckFileFrag = 4506;
	public static final int eP2CPKTDistFileList = 4507;
	public static final int eP2CPKTUpdatePeerStatus = 4508;

	// tracker to peer
	public static class T2P
	{

		public static class Hello extends SimplePacket
		{
			public Hello() { }

			public Hello(int trackerVersion, String clientIP)
			{
				this.trackerVersion = trackerVersion;
				this.clientIP = clientIP;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				trackerVersion = is.popInteger();
				clientIP = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(trackerVersion);
				os.pushString(clientIP);
			}

			public int getTrackerVersion()
			{
				return trackerVersion;
			}

			public void setTrackerVersion(int trackerVersion)
			{
				this.trackerVersion = trackerVersion;
			}

			public String getClientIP()
			{
				return clientIP;
			}

			public void setClientIP(String clientIP)
			{
				this.clientIP = clientIP;
			}

			private int trackerVersion;
			private String clientIP;
		}

		public static class LoginResponse extends SimplePacket
		{
			public static enum RetCode
			{
				eLoginOK,
				eLoginFailed
			}
			public LoginResponse() { }

			public LoginResponse(RetCode ret, ket.kpc.common.PeerID peerID)
			{
				this.ret = ret;
				this.peerID = peerID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTLoginResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ret = is.popEnum(RetCode.values());
				if( peerID == null )
					peerID = new ket.kpc.common.PeerID();
				is.pop(peerID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushEnum(ret);
				os.push(peerID);
			}

			public RetCode getRet()
			{
				return ret;
			}

			public void setRet(RetCode ret)
			{
				this.ret = ret;
			}

			public ket.kpc.common.PeerID getPeerID()
			{
				return peerID;
			}

			public void setPeerID(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			private RetCode ret;
			private ket.kpc.common.PeerID peerID;
		}

		public static class TPSTestQuestion extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eT2PPKTTPSTestQuestion;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class TPSTestResponse extends SimplePacket
		{
			public TPSTestResponse() { }

			public TPSTestResponse(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTTPSTestResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( peerID == null )
					peerID = new ket.kpc.common.PeerID();
				is.pop(peerID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(peerID);
			}

			public ket.kpc.common.PeerID getPeerID()
			{
				return peerID;
			}

			public void setPeerID(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			private ket.kpc.common.PeerID peerID;
		}

		public static class UPSTestQuestion extends SimplePacket
		{
			public UPSTestQuestion() { }

			public UPSTestQuestion(int assistantPort)
			{
				this.assistantPort = assistantPort;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTUPSTestQuestion;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				assistantPort = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(assistantPort);
			}

			public int getAssistantPort()
			{
				return assistantPort;
			}

			public void setAssistantPort(int assistantPort)
			{
				this.assistantPort = assistantPort;
			}

			private int assistantPort;
		}

		public static class UPSTestResponse extends SimplePacket
		{
			public UPSTestResponse() { }

			public UPSTestResponse(int ret, ket.kpc.common.PeerID peerID)
			{
				this.ret = ret;
				this.peerID = peerID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTUPSTestResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ret = is.popInteger();
				if( peerID == null )
					peerID = new ket.kpc.common.PeerID();
				is.pop(peerID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(ret);
				os.push(peerID);
			}

			public int getRet()
			{
				return ret;
			}

			public void setRet(int ret)
			{
				this.ret = ret;
			}

			public ket.kpc.common.PeerID getPeerID()
			{
				return peerID;
			}

			public void setPeerID(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			private int ret;
			private ket.kpc.common.PeerID peerID;
		}

		public static class SourceAnswer extends SimplePacket
		{
			public SourceAnswer() { }

			public SourceAnswer(ket.pack.Digest digest, List<ket.kpc.common.PeerID> providers)
			{
				this.digest = digest;
				this.providers = providers;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTSourceAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				providers = is.popList(ket.kpc.common.PeerID.class);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.pushList(providers);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public List<ket.kpc.common.PeerID> getProviders()
			{
				return providers;
			}

			public void setProviders(List<ket.kpc.common.PeerID> providers)
			{
				this.providers = providers;
			}

			private ket.pack.Digest digest;
			private List<ket.kpc.common.PeerID> providers;
		}

		public static class TPSRCResponse extends SimplePacket
		{
			public TPSRCResponse() { }

			public TPSRCResponse(boolean ok, int peerSessionID)
			{
				this.ok = ok;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTTPSRCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ok = is.popBoolean();
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushBoolean(ok);
				os.pushInteger(peerSessionID);
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private boolean ok;
			private int peerSessionID;
		}

		public static class TPSRCRequest extends SimplePacket
		{
			public TPSRCRequest() { }

			public TPSRCRequest(ket.kpc.common.PeerID demanderPeerID, int peerSessionID)
			{
				this.demanderPeerID = demanderPeerID;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTTPSRCRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( demanderPeerID == null )
					demanderPeerID = new ket.kpc.common.PeerID();
				is.pop(demanderPeerID);
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(demanderPeerID);
				os.pushInteger(peerSessionID);
			}

			public ket.kpc.common.PeerID getDemanderPeerID()
			{
				return demanderPeerID;
			}

			public void setDemanderPeerID(ket.kpc.common.PeerID demanderPeerID)
			{
				this.demanderPeerID = demanderPeerID;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private ket.kpc.common.PeerID demanderPeerID;
			private int peerSessionID;
		}

		public static class Relay extends SimplePacket
		{
			public Relay() { }

			public Relay(ket.kpc.common.PeerID sourcePeerID, ByteBuffer data)
			{
				this.sourcePeerID = sourcePeerID;
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTRelay;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( sourcePeerID == null )
					sourcePeerID = new ket.kpc.common.PeerID();
				is.pop(sourcePeerID);
				data = is.popByteBuffer();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(sourcePeerID);
				os.pushByteBuffer(data);
			}

			public ket.kpc.common.PeerID getSourcePeerID()
			{
				return sourcePeerID;
			}

			public void setSourcePeerID(ket.kpc.common.PeerID sourcePeerID)
			{
				this.sourcePeerID = sourcePeerID;
			}

			public ByteBuffer getData()
			{
				return data;
			}

			public void setData(ByteBuffer data)
			{
				this.data = data;
			}

			private ket.kpc.common.PeerID sourcePeerID;
			private ByteBuffer data;
		}

		public static class UPSRCResponse extends SimplePacket
		{
			public UPSRCResponse() { }

			public UPSRCResponse(boolean ok, int peerSessionID)
			{
				this.ok = ok;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTUPSRCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ok = is.popBoolean();
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushBoolean(ok);
				os.pushInteger(peerSessionID);
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private boolean ok;
			private int peerSessionID;
		}

		public static class UPSRCRequest extends SimplePacket
		{
			public UPSRCRequest() { }

			public UPSRCRequest(ket.kpc.common.PeerID demanderPeerID, int peerSessionID)
			{
				this.demanderPeerID = demanderPeerID;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTUPSRCRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( demanderPeerID == null )
					demanderPeerID = new ket.kpc.common.PeerID();
				is.pop(demanderPeerID);
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(demanderPeerID);
				os.pushInteger(peerSessionID);
			}

			public ket.kpc.common.PeerID getDemanderPeerID()
			{
				return demanderPeerID;
			}

			public void setDemanderPeerID(ket.kpc.common.PeerID demanderPeerID)
			{
				this.demanderPeerID = demanderPeerID;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private ket.kpc.common.PeerID demanderPeerID;
			private int peerSessionID;
		}

		public static class RelayClose extends SimplePacket
		{
			public RelayClose() { }

			public RelayClose(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTRelayClose;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( peerID == null )
					peerID = new ket.kpc.common.PeerID();
				is.pop(peerID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(peerID);
			}

			public ket.kpc.common.PeerID getPeerID()
			{
				return peerID;
			}

			public void setPeerID(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			private ket.kpc.common.PeerID peerID;
		}

		public static class DataGroupMemberQuery extends SimplePacket
		{
			public DataGroupMemberQuery() { }

			public DataGroupMemberQuery(ket.pack.Digest group, SBean.Section section)
			{
				this.group = group;
				this.section = section;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTDataGroupMemberQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
				if( section == null )
					section = new SBean.Section();
				is.pop(section);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
				os.push(section);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			public SBean.Section getSection()
			{
				return section;
			}

			public void setSection(SBean.Section section)
			{
				this.section = section;
			}

			private ket.pack.Digest group;
			private SBean.Section section;
		}

		public static class NoticeLocalNetwork extends SimplePacket
		{
			public NoticeLocalNetwork() { }

			public NoticeLocalNetwork(SBean.LocalNetworkNotice notice)
			{
				this.notice = notice;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTNoticeLocalNetwork;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( notice == null )
					notice = new SBean.LocalNetworkNotice();
				is.pop(notice);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(notice);
			}

			public SBean.LocalNetworkNotice getNotice()
			{
				return notice;
			}

			public void setNotice(SBean.LocalNetworkNotice notice)
			{
				this.notice = notice;
			}

			private SBean.LocalNetworkNotice notice;
		}

		public static class TestNetworkTCP extends SimplePacket
		{
			public TestNetworkTCP() { }

			public TestNetworkTCP(SBean.NetworkTCPTest test)
			{
				this.test = test;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTTestNetworkTCP;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( test == null )
					test = new SBean.NetworkTCPTest();
				is.pop(test);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(test);
			}

			public SBean.NetworkTCPTest getTest()
			{
				return test;
			}

			public void setTest(SBean.NetworkTCPTest test)
			{
				this.test = test;
			}

			private SBean.NetworkTCPTest test;
		}

		public static class TrackerRedirect extends SimplePacket
		{
			public TrackerRedirect() { }

			public TrackerRedirect(SBean.TrackerRedirectInfo info)
			{
				this.info = info;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTTrackerRedirect;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( info == null )
					info = new SBean.TrackerRedirectInfo();
				is.pop(info);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(info);
			}

			public SBean.TrackerRedirectInfo getInfo()
			{
				return info;
			}

			public void setInfo(SBean.TrackerRedirectInfo info)
			{
				this.info = info;
			}

			private SBean.TrackerRedirectInfo info;
		}

		public static class Ping extends SimplePacket
		{
			public Ping() { }

			public Ping(int seq, int load)
			{
				this.seq = seq;
				this.load = load;
			}

			@Override
			public int getType()
			{
				return Packet.eT2PPKTPing;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				seq = is.popInteger();
				load = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(seq);
				os.pushInteger(load);
			}

			public int getSeq()
			{
				return seq;
			}

			public void setSeq(int seq)
			{
				this.seq = seq;
			}

			public int getLoad()
			{
				return load;
			}

			public void setLoad(int load)
			{
				this.load = load;
			}

			private int seq;
			private int load;
		}

	}

	// peer to tracker
	public static class P2T
	{

		public static class LoginRequest extends SimplePacket
		{
			public LoginRequest() { }

			public LoginRequest(SBean.PeerLoginRequest req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTLoginRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.PeerLoginRequest();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.PeerLoginRequest getReq()
			{
				return req;
			}

			public void setReq(SBean.PeerLoginRequest req)
			{
				this.req = req;
			}

			private SBean.PeerLoginRequest req;
		}

		public static class TPSTestRequest extends SimplePacket
		{
			public TPSTestRequest() { }

			public TPSTestRequest(int port)
			{
				this.port = port;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTTPSTestRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				port = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(port);
			}

			public int getPort()
			{
				return port;
			}

			public void setPort(int port)
			{
				this.port = port;
			}

			private int port;
		}

		public static class TPSTestAnswer extends SimplePacket
		{
			public TPSTestAnswer() { }

			public TPSTestAnswer(long key)
			{
				this.key = key;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTTPSTestAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				key = is.popLong();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushLong(key);
			}

			public long getKey()
			{
				return key;
			}

			public void setKey(long key)
			{
				this.key = key;
			}

			private long key;
		}

		public static class UPSTestRequest extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eP2TPKTUPSTestRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class UPSTestAnswer extends SimplePacket
		{
			public UPSTestAnswer() { }

			public UPSTestAnswer(long key)
			{
				this.key = key;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTUPSTestAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				key = is.popLong();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushLong(key);
			}

			public long getKey()
			{
				return key;
			}

			public void setKey(long key)
			{
				this.key = key;
			}

			private long key;
		}

		public static class ShareUpdateRequest extends SimplePacket
		{
			public ShareUpdateRequest() { }

			public ShareUpdateRequest(ket.pack.Digest digest, boolean addOrRemove)
			{
				this.digest = digest;
				this.addOrRemove = addOrRemove;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTShareUpdateRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				addOrRemove = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.pushBoolean(addOrRemove);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public boolean getAddOrRemove()
			{
				return addOrRemove;
			}

			public void setAddOrRemove(boolean addOrRemove)
			{
				this.addOrRemove = addOrRemove;
			}

			private ket.pack.Digest digest;
			private boolean addOrRemove;
		}

		public static class SourceQuery extends SimplePacket
		{
			public SourceQuery() { }

			public SourceQuery(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTSourceQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class TPSRCRequest extends SimplePacket
		{
			public TPSRCRequest() { }

			public TPSRCRequest(ket.kpc.common.PeerID targetPeerID, int peerSessionID)
			{
				this.targetPeerID = targetPeerID;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTTPSRCRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( targetPeerID == null )
					targetPeerID = new ket.kpc.common.PeerID();
				is.pop(targetPeerID);
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(targetPeerID);
				os.pushInteger(peerSessionID);
			}

			public ket.kpc.common.PeerID getTargetPeerID()
			{
				return targetPeerID;
			}

			public void setTargetPeerID(ket.kpc.common.PeerID targetPeerID)
			{
				this.targetPeerID = targetPeerID;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private ket.kpc.common.PeerID targetPeerID;
			private int peerSessionID;
		}

		public static class TPSRCResponse extends SimplePacket
		{
			public TPSRCResponse() { }

			public TPSRCResponse(ket.kpc.common.PeerID demanderPeerID, int peerSessionID, boolean ok)
			{
				this.demanderPeerID = demanderPeerID;
				this.peerSessionID = peerSessionID;
				this.ok = ok;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTTPSRCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( demanderPeerID == null )
					demanderPeerID = new ket.kpc.common.PeerID();
				is.pop(demanderPeerID);
				peerSessionID = is.popInteger();
				ok = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(demanderPeerID);
				os.pushInteger(peerSessionID);
				os.pushBoolean(ok);
			}

			public ket.kpc.common.PeerID getDemanderPeerID()
			{
				return demanderPeerID;
			}

			public void setDemanderPeerID(ket.kpc.common.PeerID demanderPeerID)
			{
				this.demanderPeerID = demanderPeerID;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			private ket.kpc.common.PeerID demanderPeerID;
			private int peerSessionID;
			private boolean ok;
		}

		public static class Relay extends SimplePacket
		{
			public Relay() { }

			public Relay(ket.kpc.common.PeerID targetPeerID, ByteBuffer data)
			{
				this.targetPeerID = targetPeerID;
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTRelay;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( targetPeerID == null )
					targetPeerID = new ket.kpc.common.PeerID();
				is.pop(targetPeerID);
				data = is.popByteBuffer();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(targetPeerID);
				os.pushByteBuffer(data);
			}

			public ket.kpc.common.PeerID getTargetPeerID()
			{
				return targetPeerID;
			}

			public void setTargetPeerID(ket.kpc.common.PeerID targetPeerID)
			{
				this.targetPeerID = targetPeerID;
			}

			public ByteBuffer getData()
			{
				return data;
			}

			public void setData(ByteBuffer data)
			{
				this.data = data;
			}

			private ket.kpc.common.PeerID targetPeerID;
			private ByteBuffer data;
		}

		public static class UPSRCRequest extends SimplePacket
		{
			public UPSRCRequest() { }

			public UPSRCRequest(ket.kpc.common.PeerID targetPeerID, int peerSessionID)
			{
				this.targetPeerID = targetPeerID;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTUPSRCRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( targetPeerID == null )
					targetPeerID = new ket.kpc.common.PeerID();
				is.pop(targetPeerID);
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(targetPeerID);
				os.pushInteger(peerSessionID);
			}

			public ket.kpc.common.PeerID getTargetPeerID()
			{
				return targetPeerID;
			}

			public void setTargetPeerID(ket.kpc.common.PeerID targetPeerID)
			{
				this.targetPeerID = targetPeerID;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private ket.kpc.common.PeerID targetPeerID;
			private int peerSessionID;
		}

		public static class CreateDataGroup extends SimplePacket
		{
			public CreateDataGroup() { }

			public CreateDataGroup(ket.pack.Digest group, int memberCount)
			{
				this.group = group;
				this.memberCount = memberCount;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTCreateDataGroup;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
				memberCount = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
				os.pushInteger(memberCount);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			public int getMemberCount()
			{
				return memberCount;
			}

			public void setMemberCount(int memberCount)
			{
				this.memberCount = memberCount;
			}

			private ket.pack.Digest group;
			private int memberCount;
		}

		public static class DataGroupMemberAnswer extends SimplePacket
		{
			public DataGroupMemberAnswer() { }

			public DataGroupMemberAnswer(ket.pack.Digest group, SBean.Section section, List<ket.pack.Digest> members)
			{
				this.group = group;
				this.section = section;
				this.members = members;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTDataGroupMemberAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
				if( section == null )
					section = new SBean.Section();
				is.pop(section);
				members = is.popList(ket.pack.Digest.class);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
				os.push(section);
				os.pushList(members);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			public SBean.Section getSection()
			{
				return section;
			}

			public void setSection(SBean.Section section)
			{
				this.section = section;
			}

			public List<ket.pack.Digest> getMembers()
			{
				return members;
			}

			public void setMembers(List<ket.pack.Digest> members)
			{
				this.members = members;
			}

			private ket.pack.Digest group;
			private SBean.Section section;
			private List<ket.pack.Digest> members;
		}

		public static class GroupShareUpdateRequest extends SimplePacket
		{
			public GroupShareUpdateRequest() { }

			public GroupShareUpdateRequest(ket.pack.Digest group, List<Integer> list)
			{
				this.group = group;
				this.list = list;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTGroupShareUpdateRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
				list = is.popIntegerList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
				os.pushIntegerList(list);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			public List<Integer> getList()
			{
				return list;
			}

			public void setList(List<Integer> list)
			{
				this.list = list;
			}

			private ket.pack.Digest group;
			private List<Integer> list;
		}

		public static class TestNetworkTCP extends SimplePacket
		{
			public TestNetworkTCP() { }

			public TestNetworkTCP(String host, boolean ok)
			{
				this.host = host;
				this.ok = ok;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTTestNetworkTCP;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				host = is.popString();
				ok = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(host);
				os.pushBoolean(ok);
			}

			public String getHost()
			{
				return host;
			}

			public void setHost(String host)
			{
				this.host = host;
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			private String host;
			private boolean ok;
		}

		public static class Ping extends SimplePacket
		{
			public Ping() { }

			public Ping(int seq)
			{
				this.seq = seq;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTPing;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				seq = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(seq);
			}

			public int getSeq()
			{
				return seq;
			}

			public void setSeq(int seq)
			{
				this.seq = seq;
			}

			private int seq;
		}

		public static class LoadReport extends SimplePacket
		{
			public LoadReport() { }

			public LoadReport(SBean.PeerLoadInfo loadInfo)
			{
				this.loadInfo = loadInfo;
			}

			@Override
			public int getType()
			{
				return Packet.eP2TPKTLoadReport;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( loadInfo == null )
					loadInfo = new SBean.PeerLoadInfo();
				is.pop(loadInfo);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(loadInfo);
			}

			public SBean.PeerLoadInfo getLoadInfo()
			{
				return loadInfo;
			}

			public void setLoadInfo(SBean.PeerLoadInfo loadInfo)
			{
				this.loadInfo = loadInfo;
			}

			private SBean.PeerLoadInfo loadInfo;
		}

	}

	// peer to peer
	public static class P2P
	{

		public static class Hello extends SimplePacket
		{
			public Hello() { }

			public Hello(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( peerID == null )
					peerID = new ket.kpc.common.PeerID();
				is.pop(peerID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(peerID);
			}

			public ket.kpc.common.PeerID getPeerID()
			{
				return peerID;
			}

			public void setPeerID(ket.kpc.common.PeerID peerID)
			{
				this.peerID = peerID;
			}

			private ket.kpc.common.PeerID peerID;
		}

		public static class TPSRCResponse extends SimplePacket
		{
			public TPSRCResponse() { }

			public TPSRCResponse(boolean ok, int peerSessionID)
			{
				this.ok = ok;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTTPSRCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ok = is.popBoolean();
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushBoolean(ok);
				os.pushInteger(peerSessionID);
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private boolean ok;
			private int peerSessionID;
		}

		public static class UPSRCResponse extends SimplePacket
		{
			public UPSRCResponse() { }

			public UPSRCResponse(boolean ok, int peerSessionID)
			{
				this.ok = ok;
				this.peerSessionID = peerSessionID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTUPSRCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				ok = is.popBoolean();
				peerSessionID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushBoolean(ok);
				os.pushInteger(peerSessionID);
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			public int getPeerSessionID()
			{
				return peerSessionID;
			}

			public void setPeerSessionID(int peerSessionID)
			{
				this.peerSessionID = peerSessionID;
			}

			private boolean ok;
			private int peerSessionID;
		}

		public static class UPSPunch extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eP2PPKTUPSPunch;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class UPSClose extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eP2PPKTUPSClose;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class RegisterRequest extends SimplePacket
		{
			public RegisterRequest() { }

			public RegisterRequest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTRegisterRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class RegisterResponse extends SimplePacket
		{
			public RegisterResponse() { }

			public RegisterResponse(ket.pack.Digest digest, int rank)
			{
				this.digest = digest;
				this.rank = rank;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTRegisterResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				rank = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.pushInteger(rank);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public int getRank()
			{
				return rank;
			}

			public void setRank(int rank)
			{
				this.rank = rank;
			}

			private ket.pack.Digest digest;
			private int rank;
		}

		public static class UploadRequest extends SimplePacket
		{
			public UploadRequest() { }

			public UploadRequest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTUploadRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class UploadResponse extends SimplePacket
		{
			public UploadResponse() { }

			public UploadResponse(ket.pack.Digest digest, boolean ok)
			{
				this.digest = digest;
				this.ok = ok;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTUploadResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				ok = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.pushBoolean(ok);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public boolean getOK()
			{
				return ok;
			}

			public void setOK(boolean ok)
			{
				this.ok = ok;
			}

			private ket.pack.Digest digest;
			private boolean ok;
		}

		public static class DataRequest extends SimplePacket
		{
			public DataRequest() { }

			public DataRequest(ket.pack.Digest digest, SBean.Section section)
			{
				this.digest = digest;
				this.section = section;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTDataRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				if( section == null )
					section = new SBean.Section();
				is.pop(section);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.push(section);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public SBean.Section getSection()
			{
				return section;
			}

			public void setSection(SBean.Section section)
			{
				this.section = section;
			}

			private ket.pack.Digest digest;
			private SBean.Section section;
		}

		public static class DataResponse extends SimplePacket
		{
			public DataResponse() { }

			public DataResponse(ket.pack.Digest digest, SBean.Section section, ByteBuffer data)
			{
				this.digest = digest;
				this.section = section;
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTDataResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				if( section == null )
					section = new SBean.Section();
				is.pop(section);
				data = is.popByteBuffer();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.push(section);
				os.pushByteBuffer(data);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public SBean.Section getSection()
			{
				return section;
			}

			public void setSection(SBean.Section section)
			{
				this.section = section;
			}

			public ByteBuffer getData()
			{
				return data;
			}

			public void setData(ByteBuffer data)
			{
				this.data = data;
			}

			private ket.pack.Digest digest;
			private SBean.Section section;
			private ByteBuffer data;
		}

		public static class FinishUpload extends SimplePacket
		{
			public FinishUpload() { }

			public FinishUpload(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTFinishUpload;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class FinishDownload extends SimplePacket
		{
			public FinishDownload() { }

			public FinishDownload(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTFinishDownload;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class GroupQuery extends SimplePacket
		{
			public GroupQuery() { }

			public GroupQuery(ket.pack.Digest group)
			{
				this.group = group;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTGroupQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			private ket.pack.Digest group;
		}

		public static class GroupAnswer extends SimplePacket
		{
			public GroupAnswer() { }

			public GroupAnswer(ket.pack.Digest group, boolean exist)
			{
				this.group = group;
				this.exist = exist;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTGroupAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( group == null )
					group = new ket.pack.Digest();
				is.pop(group);
				exist = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(group);
				os.pushBoolean(exist);
			}

			public ket.pack.Digest getGroup()
			{
				return group;
			}

			public void setGroup(ket.pack.Digest group)
			{
				this.group = group;
			}

			public boolean getExist()
			{
				return exist;
			}

			public void setExist(boolean exist)
			{
				this.exist = exist;
			}

			private ket.pack.Digest group;
			private boolean exist;
		}

		public static class NoticeLocalNetworkMember extends SimplePacket
		{
			public NoticeLocalNetworkMember() { }

			public NoticeLocalNetworkMember(SBean.LocalPeerID headID, SBean.LocalPeerID memberID)
			{
				this.headID = headID;
				this.memberID = memberID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTNoticeLocalNetworkMember;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( headID == null )
					headID = new SBean.LocalPeerID();
				is.pop(headID);
				if( memberID == null )
					memberID = new SBean.LocalPeerID();
				is.pop(memberID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(headID);
				os.push(memberID);
			}

			public SBean.LocalPeerID getHeadID()
			{
				return headID;
			}

			public void setHeadID(SBean.LocalPeerID headID)
			{
				this.headID = headID;
			}

			public SBean.LocalPeerID getMemberID()
			{
				return memberID;
			}

			public void setMemberID(SBean.LocalPeerID memberID)
			{
				this.memberID = memberID;
			}

			private SBean.LocalPeerID headID;
			private SBean.LocalPeerID memberID;
		}

		public static class LocalNetworkKeepaliveReq extends SimplePacket
		{
			public LocalNetworkKeepaliveReq() { }

			public LocalNetworkKeepaliveReq(long selfID)
			{
				this.selfID = selfID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTLocalNetworkKeepaliveReq;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				selfID = is.popLong();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushLong(selfID);
			}

			public long getSelfID()
			{
				return selfID;
			}

			public void setSelfID(long selfID)
			{
				this.selfID = selfID;
			}

			private long selfID;
		}

		public static class LocalNetworkKeepaliveRes extends SimplePacket
		{
			public LocalNetworkKeepaliveRes() { }

			public LocalNetworkKeepaliveRes(long selfID)
			{
				this.selfID = selfID;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTLocalNetworkKeepaliveRes;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				selfID = is.popLong();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushLong(selfID);
			}

			public long getSelfID()
			{
				return selfID;
			}

			public void setSelfID(long selfID)
			{
				this.selfID = selfID;
			}

			private long selfID;
		}

		public static class LocalNetworkShareUpdate extends SimplePacket
		{
			public LocalNetworkShareUpdate() { }

			public LocalNetworkShareUpdate(long selfID, List<ket.pack.Digest> digests)
			{
				this.selfID = selfID;
				this.digests = digests;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTLocalNetworkShareUpdate;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				selfID = is.popLong();
				digests = is.popList(ket.pack.Digest.class);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushLong(selfID);
				os.pushList(digests);
			}

			public long getSelfID()
			{
				return selfID;
			}

			public void setSelfID(long selfID)
			{
				this.selfID = selfID;
			}

			public List<ket.pack.Digest> getDigests()
			{
				return digests;
			}

			public void setDigests(List<ket.pack.Digest> digests)
			{
				this.digests = digests;
			}

			private long selfID;
			private List<ket.pack.Digest> digests;
		}

		public static class LocalQuery extends SimplePacket
		{
			public LocalQuery() { }

			public LocalQuery(int queryID, ket.pack.Digest digest)
			{
				this.queryID = queryID;
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTLocalQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				queryID = is.popInteger();
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(queryID);
				os.push(digest);
			}

			public int getQueryID()
			{
				return queryID;
			}

			public void setQueryID(int queryID)
			{
				this.queryID = queryID;
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private int queryID;
			private ket.pack.Digest digest;
		}

		public static class LocalQueryAnswer extends SimplePacket
		{
			public LocalQueryAnswer() { }

			public LocalQueryAnswer(int queryID, List<ket.kpc.common.PeerID> providers)
			{
				this.queryID = queryID;
				this.providers = providers;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTLocalQueryAnswer;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				queryID = is.popInteger();
				providers = is.popList(ket.kpc.common.PeerID.class);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(queryID);
				os.pushList(providers);
			}

			public int getQueryID()
			{
				return queryID;
			}

			public void setQueryID(int queryID)
			{
				this.queryID = queryID;
			}

			public List<ket.kpc.common.PeerID> getProviders()
			{
				return providers;
			}

			public void setProviders(List<ket.kpc.common.PeerID> providers)
			{
				this.providers = providers;
			}

			private int queryID;
			private List<ket.kpc.common.PeerID> providers;
		}

		public static class QueryRequest extends SimplePacket
		{
			public QueryRequest() { }

			public QueryRequest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTQueryRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			private ket.pack.Digest digest;
		}

		public static class QueryResponse extends SimplePacket
		{
			public QueryResponse() { }

			public QueryResponse(ket.pack.Digest digest, boolean exist)
			{
				this.digest = digest;
				this.exist = exist;
			}

			@Override
			public int getType()
			{
				return Packet.eP2PPKTQueryResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( digest == null )
					digest = new ket.pack.Digest();
				is.pop(digest);
				exist = is.popBoolean();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(digest);
				os.pushBoolean(exist);
			}

			public ket.pack.Digest getDigest()
			{
				return digest;
			}

			public void setDigest(ket.pack.Digest digest)
			{
				this.digest = digest;
			}

			public boolean getExist()
			{
				return exist;
			}

			public void setExist(boolean exist)
			{
				this.exist = exist;
			}

			private ket.pack.Digest digest;
			private boolean exist;
		}

	}

	// monitor to peer
	public static class M2P
	{

		public static class Hello extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eM2PPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class SnapshotRequest extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eM2PPKTSnapshotRequest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

	}

	// peer to monitor
	public static class P2M
	{

		public static class Hello extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eP2MPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class SnapshotResponse extends SimplePacket
		{
			public SnapshotResponse() { }

			public SnapshotResponse(SBean.PeerSnapshot snapshot)
			{
				this.snapshot = snapshot;
			}

			@Override
			public int getType()
			{
				return Packet.eP2MPKTSnapshotResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( snapshot == null )
					snapshot = new SBean.PeerSnapshot();
				is.pop(snapshot);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(snapshot);
			}

			public SBean.PeerSnapshot getSnapshot()
			{
				return snapshot;
			}

			public void setSnapshot(SBean.PeerSnapshot snapshot)
			{
				this.snapshot = snapshot;
			}

			private SBean.PeerSnapshot snapshot;
		}

	}

	// control to peer
	public static class C2P
	{

		public static class Hello extends SimplePacket
		{
			public Hello() { }

			public Hello(String mutexName)
			{
				this.mutexName = mutexName;
			}

			@Override
			public int getType()
			{
				return Packet.eC2PPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				mutexName = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(mutexName);
			}

			public String getMutexName()
			{
				return mutexName;
			}

			public void setMutexName(String mutexName)
			{
				this.mutexName = mutexName;
			}

			private String mutexName;
		}

		public static class RegisterDistDigest extends SimplePacket
		{
			public RegisterDistDigest() { }

			public RegisterDistDigest(String digest)
			{
				this.digest = digest;
			}

			@Override
			public int getType()
			{
				return Packet.eC2PPKTRegisterDistDigest;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				digest = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(digest);
			}

			public String getDigest()
			{
				return digest;
			}

			public void setDigest(String digest)
			{
				this.digest = digest;
			}

			private String digest;
		}

		public static class DistFile extends SimplePacket
		{
			public DistFile() { }

			public DistFile(SBean.DistFileRequest req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eC2PPKTDistFile;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DistFileRequest();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DistFileRequest getReq()
			{
				return req;
			}

			public void setReq(SBean.DistFileRequest req)
			{
				this.req = req;
			}

			private SBean.DistFileRequest req;
		}

		public static class CheckFile extends SimplePacket
		{
			public CheckFile() { }

			public CheckFile(String req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eC2PPKTCheckFile;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				req = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(req);
			}

			public String getReq()
			{
				return req;
			}

			public void setReq(String req)
			{
				this.req = req;
			}

			private String req;
		}

		public static class DistFileList extends SimplePacket
		{
			public DistFileList() { }

			public DistFileList(SBean.DistFileListRequest req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eC2PPKTDistFileList;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DistFileListRequest();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DistFileListRequest getReq()
			{
				return req;
			}

			public void setReq(SBean.DistFileListRequest req)
			{
				this.req = req;
			}

			private SBean.DistFileListRequest req;
		}

	}

	// peer to control
	public static class P2C
	{

		public static class Hello extends SimplePacket
		{
			public Hello() { }

			public Hello(String downloadDir)
			{
				this.downloadDir = downloadDir;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTHello;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				downloadDir = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(downloadDir);
			}

			public String getDownloadDir()
			{
				return downloadDir;
			}

			public void setDownloadDir(String downloadDir)
			{
				this.downloadDir = downloadDir;
			}

			private String downloadDir;
		}

		public static class DistFile extends SimplePacket
		{
			public DistFile() { }

			public DistFile(SBean.DistFileResponse res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTDistFile;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DistFileResponse();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DistFileResponse getRes()
			{
				return res;
			}

			public void setRes(SBean.DistFileResponse res)
			{
				this.res = res;
			}

			private SBean.DistFileResponse res;
		}

		public static class DistFileFrag extends SimplePacket
		{
			public DistFileFrag() { }

			public DistFileFrag(String fileName, int totalCount, int index, SBean.DistFileResponse res)
			{
				this.fileName = fileName;
				this.totalCount = totalCount;
				this.index = index;
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTDistFileFrag;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				fileName = is.popString();
				totalCount = is.popInteger();
				index = is.popInteger();
				if( res == null )
					res = new SBean.DistFileResponse();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(fileName);
				os.pushInteger(totalCount);
				os.pushInteger(index);
				os.push(res);
			}

			public String getFileName()
			{
				return fileName;
			}

			public void setFileName(String fileName)
			{
				this.fileName = fileName;
			}

			public int getTotalCount()
			{
				return totalCount;
			}

			public void setTotalCount(int totalCount)
			{
				this.totalCount = totalCount;
			}

			public int getIndex()
			{
				return index;
			}

			public void setIndex(int index)
			{
				this.index = index;
			}

			public SBean.DistFileResponse getRes()
			{
				return res;
			}

			public void setRes(SBean.DistFileResponse res)
			{
				this.res = res;
			}

			private String fileName;
			private int totalCount;
			private int index;
			private SBean.DistFileResponse res;
		}

		public static class DistFileUpdate extends SimplePacket
		{
			public DistFileUpdate() { }

			public DistFileUpdate(String fileName, SBean.DistFileDownloadInfo dinfo)
			{
				this.fileName = fileName;
				this.dinfo = dinfo;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTDistFileUpdate;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				fileName = is.popString();
				if( dinfo == null )
					dinfo = new SBean.DistFileDownloadInfo();
				is.pop(dinfo);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(fileName);
				os.push(dinfo);
			}

			public String getFileName()
			{
				return fileName;
			}

			public void setFileName(String fileName)
			{
				this.fileName = fileName;
			}

			public SBean.DistFileDownloadInfo getDinfo()
			{
				return dinfo;
			}

			public void setDinfo(SBean.DistFileDownloadInfo dinfo)
			{
				this.dinfo = dinfo;
			}

			private String fileName;
			private SBean.DistFileDownloadInfo dinfo;
		}

		public static class CheckFile extends SimplePacket
		{
			public CheckFile() { }

			public CheckFile(SBean.DistFileResponse res, SBean.DistFileDownloadInfo dinfo)
			{
				this.res = res;
				this.dinfo = dinfo;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTCheckFile;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DistFileResponse();
				is.pop(res);
				if( dinfo == null )
					dinfo = new SBean.DistFileDownloadInfo();
				is.pop(dinfo);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
				os.push(dinfo);
			}

			public SBean.DistFileResponse getRes()
			{
				return res;
			}

			public void setRes(SBean.DistFileResponse res)
			{
				this.res = res;
			}

			public SBean.DistFileDownloadInfo getDinfo()
			{
				return dinfo;
			}

			public void setDinfo(SBean.DistFileDownloadInfo dinfo)
			{
				this.dinfo = dinfo;
			}

			private SBean.DistFileResponse res;
			private SBean.DistFileDownloadInfo dinfo;
		}

		public static class CheckFileFrag extends SimplePacket
		{
			public CheckFileFrag() { }

			public CheckFileFrag(String fileName, int totalCount, int index, SBean.DistFileResponse res, 
			       														SBean.DistFileDownloadInfo dinfo)
			{
				this.fileName = fileName;
				this.totalCount = totalCount;
				this.index = index;
				this.res = res;
				this.dinfo = dinfo;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTCheckFileFrag;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				fileName = is.popString();
				totalCount = is.popInteger();
				index = is.popInteger();
				if( res == null )
					res = new SBean.DistFileResponse();
				is.pop(res);
				if( dinfo == null )
					dinfo = new SBean.DistFileDownloadInfo();
				is.pop(dinfo);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(fileName);
				os.pushInteger(totalCount);
				os.pushInteger(index);
				os.push(res);
				os.push(dinfo);
			}

			public String getFileName()
			{
				return fileName;
			}

			public void setFileName(String fileName)
			{
				this.fileName = fileName;
			}

			public int getTotalCount()
			{
				return totalCount;
			}

			public void setTotalCount(int totalCount)
			{
				this.totalCount = totalCount;
			}

			public int getIndex()
			{
				return index;
			}

			public void setIndex(int index)
			{
				this.index = index;
			}

			public SBean.DistFileResponse getRes()
			{
				return res;
			}

			public void setRes(SBean.DistFileResponse res)
			{
				this.res = res;
			}

			public SBean.DistFileDownloadInfo getDinfo()
			{
				return dinfo;
			}

			public void setDinfo(SBean.DistFileDownloadInfo dinfo)
			{
				this.dinfo = dinfo;
			}

			private String fileName;
			private int totalCount;
			private int index;
			private SBean.DistFileResponse res;
			private SBean.DistFileDownloadInfo dinfo;
		}

		public static class DistFileList extends SimplePacket
		{
			public DistFileList() { }

			public DistFileList(SBean.DistFileListResponse res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTDistFileList;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DistFileListResponse();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DistFileListResponse getRes()
			{
				return res;
			}

			public void setRes(SBean.DistFileListResponse res)
			{
				this.res = res;
			}

			private SBean.DistFileListResponse res;
		}

		public static class UpdatePeerStatus extends SimplePacket
		{
			public UpdatePeerStatus() { }

			public UpdatePeerStatus(SBean.PeerStatus status)
			{
				this.status = status;
			}

			@Override
			public int getType()
			{
				return Packet.eP2CPKTUpdatePeerStatus;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( status == null )
					status = new SBean.PeerStatus();
				is.pop(status);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(status);
			}

			public SBean.PeerStatus getStatus()
			{
				return status;
			}

			public void setStatus(SBean.PeerStatus status)
			{
				this.status = status;
			}

			private SBean.PeerStatus status;
		}

	}

}

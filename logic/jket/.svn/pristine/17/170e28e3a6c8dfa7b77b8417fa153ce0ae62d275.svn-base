// modified by ket.kio.RPCGen at Thu May 31 13:27:49 CST 2012.

package ket.kpc.tracker;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPTrackerServer extends TCPServer<SimplePacket>
{

	public TCPTrackerServer(Tracker tracker)
	{
		super(tracker.getNetManager());
		this.tracker = tracker;
	}

	@Override
	public int getMaxConnectionIdleTime()
	{
		return tracker.getTCPTrackerServerMaxConnectionIdleTime();
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to tracker
			case Packet.eP2TPKTLoginRequest:
			case Packet.eP2TPKTTPSTestRequest:
			case Packet.eP2TPKTUPSTestRequest:
			case Packet.eP2TPKTShareUpdateRequest:
			case Packet.eP2TPKTSourceQuery:
			case Packet.eP2TPKTTPSRCRequest:
			case Packet.eP2TPKTTPSRCResponse:
			case Packet.eP2TPKTUPSRCRequest:
			case Packet.eP2TPKTRelay:
			case Packet.eP2TPKTCreateDataGroup:
			case Packet.eP2TPKTDataGroupMemberAnswer:
			case Packet.eP2TPKTGroupShareUpdateRequest:
			case Packet.eP2TPKTTestNetworkTCP:
			case Packet.eP2TPKTLoadReport:
				return true;
			default:
				break;
			}
			return false;
		}
	}

	@Override
	public PacketEncoder<SimplePacket> getEncoder()
	{
		return dencoder;
	}

	@Override
	public PacketDecoder<SimplePacket> getDecoder()
	{
		return dencoder;
	}

	@Override
	public void onOpen()
	{
		tracker.onTCPTrackerServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		tracker.onTCPTrackerServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		tracker.onTCPTrackerServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		tracker.onTCPTrackerServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		tracker.onTCPTrackerServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to tracker
		case Packet.eP2TPKTLoginRequest:
			{
				Packet.P2T.LoginRequest p = (Packet.P2T.LoginRequest)packet;
				tracker.onTCPTrackerServerRecvLoginRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTTPSTestRequest:
			{
				Packet.P2T.TPSTestRequest p = (Packet.P2T.TPSTestRequest)packet;
				tracker.onTCPTrackerServerRecvTPSTestRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTUPSTestRequest:
			{
				Packet.P2T.UPSTestRequest p = (Packet.P2T.UPSTestRequest)packet;
				tracker.onTCPTrackerServerRecvUPSTestRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTShareUpdateRequest:
			{
				Packet.P2T.ShareUpdateRequest p = (Packet.P2T.ShareUpdateRequest)packet;
				tracker.onTCPTrackerServerRecvShareUpdateRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTSourceQuery:
			{
				Packet.P2T.SourceQuery p = (Packet.P2T.SourceQuery)packet;
				tracker.onTCPTrackerServerRecvSourceQuery(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTTPSRCRequest:
			{
				Packet.P2T.TPSRCRequest p = (Packet.P2T.TPSRCRequest)packet;
				tracker.onTCPTrackerServerRecvTPSRCRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTTPSRCResponse:
			{
				Packet.P2T.TPSRCResponse p = (Packet.P2T.TPSRCResponse)packet;
				tracker.onTCPTrackerServerRecvTPSRCResponse(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTUPSRCRequest:
			{
				Packet.P2T.UPSRCRequest p = (Packet.P2T.UPSRCRequest)packet;
				tracker.onTCPTrackerServerRecvUPSRCRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTRelay:
			{
				Packet.P2T.Relay p = (Packet.P2T.Relay)packet;
				tracker.onTCPTrackerServerRecvRelay(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTCreateDataGroup:
			{
				Packet.P2T.CreateDataGroup p = (Packet.P2T.CreateDataGroup)packet;
				tracker.onTCPTrackerServerRecvCreateDataGroup(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTDataGroupMemberAnswer:
			{
				Packet.P2T.DataGroupMemberAnswer p = (Packet.P2T.DataGroupMemberAnswer)packet;
				tracker.onTCPTrackerServerRecvDataGroupMemberAnswer(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTGroupShareUpdateRequest:
			{
				Packet.P2T.GroupShareUpdateRequest p = (Packet.P2T.GroupShareUpdateRequest)packet;
				tracker.onTCPTrackerServerRecvGroupShareUpdateRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTTestNetworkTCP:
			{
				Packet.P2T.TestNetworkTCP p = (Packet.P2T.TestNetworkTCP)packet;
				tracker.onTCPTrackerServerRecvTestNetworkTCP(this, p, sessionid);
			}
			break;
		case Packet.eP2TPKTLoadReport:
			{
				Packet.P2T.LoadReport p = (Packet.P2T.LoadReport)packet;
				tracker.onTCPTrackerServerRecvLoadReport(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private Tracker tracker;
}

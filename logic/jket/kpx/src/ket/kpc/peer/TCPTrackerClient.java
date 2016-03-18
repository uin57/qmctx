// modified by ket.kio.RPCGen at Mon May 28 12:03:39 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.SBean;
import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPTrackerClient extends TCPClient<SimplePacket>
{

	public TCPTrackerClient(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	public TCPTrackerClient(RPCManager managerRPC, SBean.TrackerRedirectInfo redirectInfo)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
		this.redirectInfo = redirectInfo;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// tracker to peer
			case Packet.eT2PPKTHello:
			case Packet.eT2PPKTLoginResponse:
			case Packet.eT2PPKTTPSTestResponse:
			case Packet.eT2PPKTUPSTestResponse:
			case Packet.eT2PPKTUPSTestQuestion:
			case Packet.eT2PPKTSourceAnswer:
			case Packet.eT2PPKTTPSRCResponse:
			case Packet.eT2PPKTTPSRCRequest:
			case Packet.eT2PPKTUPSRCRequest:
			case Packet.eT2PPKTUPSRCResponse:
			case Packet.eT2PPKTRelayClose:
			case Packet.eT2PPKTRelay:
			case Packet.eT2PPKTDataGroupMemberQuery:
			case Packet.eT2PPKTNoticeLocalNetwork:
			case Packet.eT2PPKTTestNetworkTCP:
			case Packet.eT2PPKTTrackerRedirect:
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
		managerRPC.onTCPTrackerClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPTrackerClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPTrackerClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// tracker to peer
		case Packet.eT2PPKTHello:
			{
				Packet.T2P.Hello p = (Packet.T2P.Hello)packet;
				managerRPC.onTCPTrackerClientRecvHello(this, p);
			}
			break;
		case Packet.eT2PPKTLoginResponse:
			{
				Packet.T2P.LoginResponse p = (Packet.T2P.LoginResponse)packet;
				managerRPC.onTCPTrackerClientRecvLoginResponse(this, p);
			}
			break;
		case Packet.eT2PPKTTPSTestResponse:
			{
				Packet.T2P.TPSTestResponse p = (Packet.T2P.TPSTestResponse)packet;
				managerRPC.onTCPTrackerClientRecvTPSTestResponse(this, p);
			}
			break;
		case Packet.eT2PPKTUPSTestResponse:
			{
				Packet.T2P.UPSTestResponse p = (Packet.T2P.UPSTestResponse)packet;
				managerRPC.onTCPTrackerClientRecvUPSTestResponse(this, p);
			}
			break;
		case Packet.eT2PPKTUPSTestQuestion:
			{
				Packet.T2P.UPSTestQuestion p = (Packet.T2P.UPSTestQuestion)packet;
				managerRPC.onTCPTrackerClientRecvUPSTestQuestion(this, p);
			}
			break;
		case Packet.eT2PPKTSourceAnswer:
			{
				Packet.T2P.SourceAnswer p = (Packet.T2P.SourceAnswer)packet;
				managerRPC.onTCPTrackerClientRecvSourceAnswer(this, p);
			}
			break;
		case Packet.eT2PPKTTPSRCResponse:
			{
				Packet.T2P.TPSRCResponse p = (Packet.T2P.TPSRCResponse)packet;
				managerRPC.onTCPTrackerClientRecvTPSRCResponse(this, p);
			}
			break;
		case Packet.eT2PPKTTPSRCRequest:
			{
				Packet.T2P.TPSRCRequest p = (Packet.T2P.TPSRCRequest)packet;
				managerRPC.onTCPTrackerClientRecvTPSRCRequest(this, p);
			}
			break;
		case Packet.eT2PPKTUPSRCRequest:
			{
				Packet.T2P.UPSRCRequest p = (Packet.T2P.UPSRCRequest)packet;
				managerRPC.onTCPTrackerClientRecvUPSRCRequest(this, p);
			}
			break;
		case Packet.eT2PPKTUPSRCResponse:
			{
				Packet.T2P.UPSRCResponse p = (Packet.T2P.UPSRCResponse)packet;
				managerRPC.onTCPTrackerClientRecvUPSRCResponse(this, p);
			}
			break;
		case Packet.eT2PPKTRelayClose:
			{
				Packet.T2P.RelayClose p = (Packet.T2P.RelayClose)packet;
				managerRPC.onTCPTrackerClientRecvRelayClose(this, p);
			}
			break;
		case Packet.eT2PPKTRelay:
			{
				Packet.T2P.Relay p = (Packet.T2P.Relay)packet;
				managerRPC.onTCPTrackerClientRecvRelay(this, p);
			}
			break;
		case Packet.eT2PPKTDataGroupMemberQuery:
			{
				Packet.T2P.DataGroupMemberQuery p = (Packet.T2P.DataGroupMemberQuery)packet;
				managerRPC.onTCPTrackerClientRecvDataGroupMemberQuery(this, p);
			}
			break;
		case Packet.eT2PPKTNoticeLocalNetwork:
			{
				Packet.T2P.NoticeLocalNetwork p = (Packet.T2P.NoticeLocalNetwork)packet;
				managerRPC.onTCPTrackerClientRecvNoticeLocalNetwork(this, p);
			}
			break;
		case Packet.eT2PPKTTestNetworkTCP:
			{
				Packet.T2P.TestNetworkTCP p = (Packet.T2P.TestNetworkTCP)packet;
				managerRPC.onTCPTrackerClientRecvTestNetworkTCP(this, p);
			}
			break;
		case Packet.eT2PPKTTrackerRedirect:
			{
				Packet.T2P.TrackerRedirect p = (Packet.T2P.TrackerRedirect)packet;
				managerRPC.onTCPTrackerClientRecvTrackerRedirect(this, p);
			}
			break;
		default:
			break;
		}
	}

	public SBean.TrackerRedirectInfo getRedirectInfo()
	{
		return redirectInfo;
	}

	public void setRedirectInfo(SBean.TrackerRedirectInfo redirectInfo)
	{
		this.redirectInfo = redirectInfo;
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManager managerRPC;
	private SBean.TrackerRedirectInfo redirectInfo;
}

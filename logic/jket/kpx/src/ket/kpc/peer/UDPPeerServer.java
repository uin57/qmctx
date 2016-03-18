// modified by ket.kio.RPCGen at Thu Oct 11 16:51:48 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.UDPPeer;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class UDPPeerServer extends UDPPeer<SimplePacket>
{

	public UDPPeerServer(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// tracker to peer
			case Packet.eT2PPKTPing:
			// peer to peer
			case Packet.eP2PPKTHello:
			case Packet.eP2PPKTUPSRCResponse:
			case Packet.eP2PPKTUPSPunch:
			case Packet.eP2PPKTUPSClose:
			case Packet.eP2PPKTRegisterRequest:
			case Packet.eP2PPKTRegisterResponse:
			case Packet.eP2PPKTUploadRequest:
			case Packet.eP2PPKTUploadResponse:
			case Packet.eP2PPKTDataRequest:
			case Packet.eP2PPKTDataResponse:
			case Packet.eP2PPKTFinishUpload:
			case Packet.eP2PPKTFinishDownload:
			case Packet.eP2PPKTGroupQuery:
			case Packet.eP2PPKTGroupAnswer:
			case Packet.eP2PPKTNoticeLocalNetworkMember:
			case Packet.eP2PPKTLocalNetworkKeepaliveReq:
			case Packet.eP2PPKTLocalNetworkKeepaliveRes:
			case Packet.eP2PPKTLocalNetworkShareUpdate:
			case Packet.eP2PPKTLocalQuery:
			case Packet.eP2PPKTLocalQueryAnswer:
			case Packet.eP2PPKTQueryRequest:
			case Packet.eP2PPKTQueryResponse:
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
		managerRPC.onUDPPeerServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onUDPPeerServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onUDPPeerServerClose(this, errcode);
	}

	@Override
	public void onPacketRecv(NetAddress addrRemote, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// tracker to peer
		case Packet.eT2PPKTPing:
			{
				Packet.T2P.Ping p = (Packet.T2P.Ping)packet;
				managerRPC.onUDPPeerServerRecvPing(this, p, addrRemote);
			}
			break;
		// peer to peer
		case Packet.eP2PPKTHello:
			{
				Packet.P2P.Hello p = (Packet.P2P.Hello)packet;
				managerRPC.onUDPPeerServerRecvHello(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTUPSRCResponse:
			{
				Packet.P2P.UPSRCResponse p = (Packet.P2P.UPSRCResponse)packet;
				managerRPC.onUDPPeerServerRecvUPSRCResponse(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTUPSPunch:
			{
				Packet.P2P.UPSPunch p = (Packet.P2P.UPSPunch)packet;
				managerRPC.onUDPPeerServerRecvUPSPunch(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTUPSClose:
			{
				Packet.P2P.UPSClose p = (Packet.P2P.UPSClose)packet;
				managerRPC.onUDPPeerServerRecvUPSClose(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTRegisterRequest:
			{
				Packet.P2P.RegisterRequest p = (Packet.P2P.RegisterRequest)packet;
				managerRPC.onUDPPeerServerRecvRegisterRequest(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTRegisterResponse:
			{
				Packet.P2P.RegisterResponse p = (Packet.P2P.RegisterResponse)packet;
				managerRPC.onUDPPeerServerRecvRegisterResponse(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTUploadRequest:
			{
				Packet.P2P.UploadRequest p = (Packet.P2P.UploadRequest)packet;
				managerRPC.onUDPPeerServerRecvUploadRequest(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTUploadResponse:
			{
				Packet.P2P.UploadResponse p = (Packet.P2P.UploadResponse)packet;
				managerRPC.onUDPPeerServerRecvUploadResponse(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTDataRequest:
			{
				Packet.P2P.DataRequest p = (Packet.P2P.DataRequest)packet;
				managerRPC.onUDPPeerServerRecvDataRequest(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTDataResponse:
			{
				Packet.P2P.DataResponse p = (Packet.P2P.DataResponse)packet;
				managerRPC.onUDPPeerServerRecvDataResponse(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTFinishUpload:
			{
				Packet.P2P.FinishUpload p = (Packet.P2P.FinishUpload)packet;
				managerRPC.onUDPPeerServerRecvFinishUpload(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTFinishDownload:
			{
				Packet.P2P.FinishDownload p = (Packet.P2P.FinishDownload)packet;
				managerRPC.onUDPPeerServerRecvFinishDownload(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTGroupQuery:
			{
				Packet.P2P.GroupQuery p = (Packet.P2P.GroupQuery)packet;
				managerRPC.onUDPPeerServerRecvGroupQuery(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTGroupAnswer:
			{
				Packet.P2P.GroupAnswer p = (Packet.P2P.GroupAnswer)packet;
				managerRPC.onUDPPeerServerRecvGroupAnswer(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTNoticeLocalNetworkMember:
			{
				Packet.P2P.NoticeLocalNetworkMember p = (Packet.P2P.NoticeLocalNetworkMember)packet;
				managerRPC.onUDPPeerServerRecvNoticeLocalNetworkMember(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTLocalNetworkKeepaliveReq:
			{
				Packet.P2P.LocalNetworkKeepaliveReq p = (Packet.P2P.LocalNetworkKeepaliveReq)packet;
				managerRPC.onUDPPeerServerRecvLocalNetworkKeepaliveReq(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTLocalNetworkKeepaliveRes:
			{
				Packet.P2P.LocalNetworkKeepaliveRes p = (Packet.P2P.LocalNetworkKeepaliveRes)packet;
				managerRPC.onUDPPeerServerRecvLocalNetworkKeepaliveRes(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTLocalNetworkShareUpdate:
			{
				Packet.P2P.LocalNetworkShareUpdate p = (Packet.P2P.LocalNetworkShareUpdate)packet;
				managerRPC.onUDPPeerServerRecvLocalNetworkShareUpdate(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTLocalQuery:
			{
				Packet.P2P.LocalQuery p = (Packet.P2P.LocalQuery)packet;
				managerRPC.onUDPPeerServerRecvLocalQuery(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTLocalQueryAnswer:
			{
				Packet.P2P.LocalQueryAnswer p = (Packet.P2P.LocalQueryAnswer)packet;
				managerRPC.onUDPPeerServerRecvLocalQueryAnswer(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTQueryRequest:
			{
				Packet.P2P.QueryRequest p = (Packet.P2P.QueryRequest)packet;
				managerRPC.onUDPPeerServerRecvQueryRequest(this, p, addrRemote);
			}
			break;
		case Packet.eP2PPKTQueryResponse:
			{
				Packet.P2P.QueryResponse p = (Packet.P2P.QueryResponse)packet;
				managerRPC.onUDPPeerServerRecvQueryResponse(this, p, addrRemote);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManager managerRPC;
}

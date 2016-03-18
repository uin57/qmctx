// modified by ket.kio.RPCGen at Thu Oct 11 16:51:48 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPPeerServer extends TCPServer<SimplePacket>
{

	public TCPPeerServer(RPCManager managerRPC)
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
			case Packet.eT2PPKTTPSTestQuestion:
			// peer to peer
			case Packet.eP2PPKTHello:
			case Packet.eP2PPKTTPSRCResponse:
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
		managerRPC.onTCPPeerServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPPeerServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// tracker to peer
		case Packet.eT2PPKTTPSTestQuestion:
			{
				Packet.T2P.TPSTestQuestion p = (Packet.T2P.TPSTestQuestion)packet;
				managerRPC.onTCPPeerServerRecvTPSTestQuestion(this, p, sessionid);
			}
			break;
		// peer to peer
		case Packet.eP2PPKTHello:
			{
				Packet.P2P.Hello p = (Packet.P2P.Hello)packet;
				managerRPC.onTCPPeerServerRecvHello(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTTPSRCResponse:
			{
				Packet.P2P.TPSRCResponse p = (Packet.P2P.TPSRCResponse)packet;
				managerRPC.onTCPPeerServerRecvTPSRCResponse(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTRegisterRequest:
			{
				Packet.P2P.RegisterRequest p = (Packet.P2P.RegisterRequest)packet;
				managerRPC.onTCPPeerServerRecvRegisterRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTRegisterResponse:
			{
				Packet.P2P.RegisterResponse p = (Packet.P2P.RegisterResponse)packet;
				managerRPC.onTCPPeerServerRecvRegisterResponse(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTUploadRequest:
			{
				Packet.P2P.UploadRequest p = (Packet.P2P.UploadRequest)packet;
				managerRPC.onTCPPeerServerRecvUploadRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTUploadResponse:
			{
				Packet.P2P.UploadResponse p = (Packet.P2P.UploadResponse)packet;
				managerRPC.onTCPPeerServerRecvUploadResponse(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTDataRequest:
			{
				Packet.P2P.DataRequest p = (Packet.P2P.DataRequest)packet;
				managerRPC.onTCPPeerServerRecvDataRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTDataResponse:
			{
				Packet.P2P.DataResponse p = (Packet.P2P.DataResponse)packet;
				managerRPC.onTCPPeerServerRecvDataResponse(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTFinishUpload:
			{
				Packet.P2P.FinishUpload p = (Packet.P2P.FinishUpload)packet;
				managerRPC.onTCPPeerServerRecvFinishUpload(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTFinishDownload:
			{
				Packet.P2P.FinishDownload p = (Packet.P2P.FinishDownload)packet;
				managerRPC.onTCPPeerServerRecvFinishDownload(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTGroupQuery:
			{
				Packet.P2P.GroupQuery p = (Packet.P2P.GroupQuery)packet;
				managerRPC.onTCPPeerServerRecvGroupQuery(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTGroupAnswer:
			{
				Packet.P2P.GroupAnswer p = (Packet.P2P.GroupAnswer)packet;
				managerRPC.onTCPPeerServerRecvGroupAnswer(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTQueryRequest:
			{
				Packet.P2P.QueryRequest p = (Packet.P2P.QueryRequest)packet;
				managerRPC.onTCPPeerServerRecvQueryRequest(this, p, sessionid);
			}
			break;
		case Packet.eP2PPKTQueryResponse:
			{
				Packet.P2P.QueryResponse p = (Packet.P2P.QueryResponse)packet;
				managerRPC.onTCPPeerServerRecvQueryResponse(this, p, sessionid);
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

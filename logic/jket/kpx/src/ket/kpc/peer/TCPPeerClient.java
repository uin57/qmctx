// modified by ket.kio.RPCGen at Thu Jan 17 11:18:26 CST 2013.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPPeerClient extends TCPClient<SimplePacket>
{

	public TCPPeerClient(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	public TCPPeerClient(RPCManager managerRPC, int localPeerSessionID, int remotePeerSessionID, boolean direct, ket.kpc.common.PeerID remotePeerID, 
		                     ket.kpc.common.PeerID localPeerID)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
		this.localPeerSessionID = localPeerSessionID;
		this.remotePeerSessionID = remotePeerSessionID;
		this.direct = direct;
		this.remotePeerID = remotePeerID;
		this.localPeerID = localPeerID;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to peer
			case Packet.eP2PPKTHello:
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
		managerRPC.onTCPPeerClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to peer
		case Packet.eP2PPKTHello:
			{
				Packet.P2P.Hello p = (Packet.P2P.Hello)packet;
				managerRPC.onTCPPeerClientRecvHello(this, p);
			}
			break;
		case Packet.eP2PPKTRegisterRequest:
			{
				Packet.P2P.RegisterRequest p = (Packet.P2P.RegisterRequest)packet;
				managerRPC.onTCPPeerClientRecvRegisterRequest(this, p);
			}
			break;
		case Packet.eP2PPKTRegisterResponse:
			{
				Packet.P2P.RegisterResponse p = (Packet.P2P.RegisterResponse)packet;
				managerRPC.onTCPPeerClientRecvRegisterResponse(this, p);
			}
			break;
		case Packet.eP2PPKTUploadRequest:
			{
				Packet.P2P.UploadRequest p = (Packet.P2P.UploadRequest)packet;
				managerRPC.onTCPPeerClientRecvUploadRequest(this, p);
			}
			break;
		case Packet.eP2PPKTUploadResponse:
			{
				Packet.P2P.UploadResponse p = (Packet.P2P.UploadResponse)packet;
				managerRPC.onTCPPeerClientRecvUploadResponse(this, p);
			}
			break;
		case Packet.eP2PPKTDataRequest:
			{
				Packet.P2P.DataRequest p = (Packet.P2P.DataRequest)packet;
				managerRPC.onTCPPeerClientRecvDataRequest(this, p);
			}
			break;
		case Packet.eP2PPKTDataResponse:
			{
				Packet.P2P.DataResponse p = (Packet.P2P.DataResponse)packet;
				managerRPC.onTCPPeerClientRecvDataResponse(this, p);
			}
			break;
		case Packet.eP2PPKTFinishUpload:
			{
				Packet.P2P.FinishUpload p = (Packet.P2P.FinishUpload)packet;
				managerRPC.onTCPPeerClientRecvFinishUpload(this, p);
			}
			break;
		case Packet.eP2PPKTFinishDownload:
			{
				Packet.P2P.FinishDownload p = (Packet.P2P.FinishDownload)packet;
				managerRPC.onTCPPeerClientRecvFinishDownload(this, p);
			}
			break;
		case Packet.eP2PPKTGroupQuery:
			{
				Packet.P2P.GroupQuery p = (Packet.P2P.GroupQuery)packet;
				managerRPC.onTCPPeerClientRecvGroupQuery(this, p);
			}
			break;
		case Packet.eP2PPKTGroupAnswer:
			{
				Packet.P2P.GroupAnswer p = (Packet.P2P.GroupAnswer)packet;
				managerRPC.onTCPPeerClientRecvGroupAnswer(this, p);
			}
			break;
		case Packet.eP2PPKTQueryRequest:
			{
				Packet.P2P.QueryRequest p = (Packet.P2P.QueryRequest)packet;
				managerRPC.onTCPPeerClientRecvQueryRequest(this, p);
			}
			break;
		case Packet.eP2PPKTQueryResponse:
			{
				Packet.P2P.QueryResponse p = (Packet.P2P.QueryResponse)packet;
				managerRPC.onTCPPeerClientRecvQueryResponse(this, p);
			}
			break;
		default:
			break;
		}
	}

	public int getLocalPeerSessionID()
	{
		return localPeerSessionID;
	}

	public void setLocalPeerSessionID(int localPeerSessionID)
	{
		this.localPeerSessionID = localPeerSessionID;
	}

	public int getRemotePeerSessionID()
	{
		return remotePeerSessionID;
	}

	public void setRemotePeerSessionID(int remotePeerSessionID)
	{
		this.remotePeerSessionID = remotePeerSessionID;
	}

	public boolean getDirect()
	{
		return direct;
	}

	public void setDirect(boolean direct)
	{
		this.direct = direct;
	}

	public ket.kpc.common.PeerID getRemotePeerID()
	{
		return remotePeerID;
	}

	public void setRemotePeerID(ket.kpc.common.PeerID remotePeerID)
	{
		this.remotePeerID = remotePeerID;
	}

	public ket.kpc.common.PeerID getLocalPeerID()
	{
		return localPeerID;
	}

	public void setLocalPeerID(ket.kpc.common.PeerID localPeerID)
	{
		this.localPeerID = localPeerID;
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManager managerRPC;
	private int localPeerSessionID;
	private int remotePeerSessionID;
	private boolean direct;
	private ket.kpc.common.PeerID remotePeerID;
	private ket.kpc.common.PeerID localPeerID;
}

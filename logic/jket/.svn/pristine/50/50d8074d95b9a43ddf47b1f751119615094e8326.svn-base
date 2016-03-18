// modified by ket.kio.RPCGen at Fri Nov 09 17:19:54 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPTrackerClientCheater extends TCPClient<SimplePacket>
{

	public TCPTrackerClientCheater(ClientCheater clientCheater)
	{
		super(clientCheater.getNetManager());
		this.clientCheater = clientCheater;
	}

	public TCPTrackerClientCheater(ClientCheater clientCheater, int clientID)
	{
		super(clientCheater.getNetManager());
		this.clientCheater = clientCheater;
		this.clientID = clientID;
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
		clientCheater.onTCPTrackerClientCheaterOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		clientCheater.onTCPTrackerClientCheaterOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		clientCheater.onTCPTrackerClientCheaterClose(this, errcode);
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
				clientCheater.onTCPTrackerClientCheaterRecvHello(this, p);
			}
			break;
		case Packet.eT2PPKTLoginResponse:
			{
				Packet.T2P.LoginResponse p = (Packet.T2P.LoginResponse)packet;
				clientCheater.onTCPTrackerClientCheaterRecvLoginResponse(this, p);
			}
			break;
		case Packet.eT2PPKTTPSTestResponse:
			{
				Packet.T2P.TPSTestResponse p = (Packet.T2P.TPSTestResponse)packet;
				clientCheater.onTCPTrackerClientCheaterRecvTPSTestResponse(this, p);
			}
			break;
		case Packet.eT2PPKTUPSTestResponse:
			{
				Packet.T2P.UPSTestResponse p = (Packet.T2P.UPSTestResponse)packet;
				clientCheater.onTCPTrackerClientCheaterRecvUPSTestResponse(this, p);
			}
			break;
		case Packet.eT2PPKTUPSTestQuestion:
			{
				Packet.T2P.UPSTestQuestion p = (Packet.T2P.UPSTestQuestion)packet;
				clientCheater.onTCPTrackerClientCheaterRecvUPSTestQuestion(this, p);
			}
			break;
		case Packet.eT2PPKTSourceAnswer:
			{
				Packet.T2P.SourceAnswer p = (Packet.T2P.SourceAnswer)packet;
				clientCheater.onTCPTrackerClientCheaterRecvSourceAnswer(this, p);
			}
			break;
		case Packet.eT2PPKTTPSRCResponse:
			{
				Packet.T2P.TPSRCResponse p = (Packet.T2P.TPSRCResponse)packet;
				clientCheater.onTCPTrackerClientCheaterRecvTPSRCResponse(this, p);
			}
			break;
		case Packet.eT2PPKTTPSRCRequest:
			{
				Packet.T2P.TPSRCRequest p = (Packet.T2P.TPSRCRequest)packet;
				clientCheater.onTCPTrackerClientCheaterRecvTPSRCRequest(this, p);
			}
			break;
		case Packet.eT2PPKTUPSRCRequest:
			{
				Packet.T2P.UPSRCRequest p = (Packet.T2P.UPSRCRequest)packet;
				clientCheater.onTCPTrackerClientCheaterRecvUPSRCRequest(this, p);
			}
			break;
		case Packet.eT2PPKTUPSRCResponse:
			{
				Packet.T2P.UPSRCResponse p = (Packet.T2P.UPSRCResponse)packet;
				clientCheater.onTCPTrackerClientCheaterRecvUPSRCResponse(this, p);
			}
			break;
		case Packet.eT2PPKTRelayClose:
			{
				Packet.T2P.RelayClose p = (Packet.T2P.RelayClose)packet;
				clientCheater.onTCPTrackerClientCheaterRecvRelayClose(this, p);
			}
			break;
		case Packet.eT2PPKTRelay:
			{
				Packet.T2P.Relay p = (Packet.T2P.Relay)packet;
				clientCheater.onTCPTrackerClientCheaterRecvRelay(this, p);
			}
			break;
		case Packet.eT2PPKTDataGroupMemberQuery:
			{
				Packet.T2P.DataGroupMemberQuery p = (Packet.T2P.DataGroupMemberQuery)packet;
				clientCheater.onTCPTrackerClientCheaterRecvDataGroupMemberQuery(this, p);
			}
			break;
		case Packet.eT2PPKTNoticeLocalNetwork:
			{
				Packet.T2P.NoticeLocalNetwork p = (Packet.T2P.NoticeLocalNetwork)packet;
				clientCheater.onTCPTrackerClientCheaterRecvNoticeLocalNetwork(this, p);
			}
			break;
		case Packet.eT2PPKTTestNetworkTCP:
			{
				Packet.T2P.TestNetworkTCP p = (Packet.T2P.TestNetworkTCP)packet;
				clientCheater.onTCPTrackerClientCheaterRecvTestNetworkTCP(this, p);
			}
			break;
		case Packet.eT2PPKTTrackerRedirect:
			{
				Packet.T2P.TrackerRedirect p = (Packet.T2P.TrackerRedirect)packet;
				clientCheater.onTCPTrackerClientCheaterRecvTrackerRedirect(this, p);
			}
			break;
		default:
			break;
		}
	}

	public int getClientID()
	{
		return clientID;
	}

	public void setClientID(int clientID)
	{
		this.clientID = clientID;
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private ClientCheater clientCheater;
	private int clientID;
}

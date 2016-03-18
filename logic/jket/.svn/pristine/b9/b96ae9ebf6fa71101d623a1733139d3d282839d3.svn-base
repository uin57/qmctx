// modified by ket.kio.RPCGen at Mon Jun 04 16:38:58 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPPeerControlServer extends TCPServer<SimplePacket>
{

	public TCPPeerControlServer(RPCManager managerRPC)
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
			// control to peer
			case Packet.eC2PPKTHello:
			case Packet.eC2PPKTRegisterDistDigest:
			case Packet.eC2PPKTDistFile:
			case Packet.eC2PPKTCheckFile:
			case Packet.eC2PPKTDistFileList:
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
		managerRPC.onTCPPeerControlServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerControlServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerControlServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPPeerControlServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPPeerControlServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// control to peer
		case Packet.eC2PPKTHello:
			{
				Packet.C2P.Hello p = (Packet.C2P.Hello)packet;
				managerRPC.onTCPPeerControlServerRecvHello(this, p, sessionid);
			}
			break;
		case Packet.eC2PPKTRegisterDistDigest:
			{
				Packet.C2P.RegisterDistDigest p = (Packet.C2P.RegisterDistDigest)packet;
				managerRPC.onTCPPeerControlServerRecvRegisterDistDigest(this, p, sessionid);
			}
			break;
		case Packet.eC2PPKTDistFile:
			{
				Packet.C2P.DistFile p = (Packet.C2P.DistFile)packet;
				managerRPC.onTCPPeerControlServerRecvDistFile(this, p, sessionid);
			}
			break;
		case Packet.eC2PPKTCheckFile:
			{
				Packet.C2P.CheckFile p = (Packet.C2P.CheckFile)packet;
				managerRPC.onTCPPeerControlServerRecvCheckFile(this, p, sessionid);
			}
			break;
		case Packet.eC2PPKTDistFileList:
			{
				Packet.C2P.DistFileList p = (Packet.C2P.DistFileList)packet;
				managerRPC.onTCPPeerControlServerRecvDistFileList(this, p, sessionid);
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

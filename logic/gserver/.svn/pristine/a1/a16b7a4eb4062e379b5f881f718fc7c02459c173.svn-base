// modified by ket.kio.RPCGen at Mon Dec 01 17:06:00 CST 2014.

package i3k.gs;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPGameServer extends TCPServer<SimplePacket>
{

	public TCPGameServer(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	@Override
	public int getMaxConnectionIdleTime()
	{
		return managerRPC.getTCPGameServerMaxConnectionIdleTime();
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// client to server
			case Packet.eC2SPKTClientResponse:
			case Packet.eC2SPKTLuaChannel:
			case Packet.eC2SPKTLuaChannel2:
			case Packet.eC2SPKTDataSync:
			case Packet.eC2SPKTAntiData:
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
		managerRPC.onTCPGameServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGameServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGameServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPGameServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGameServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// client to server
		case Packet.eC2SPKTClientResponse:
			{
				Packet.C2S.ClientResponse p = (Packet.C2S.ClientResponse)packet;
				managerRPC.onTCPGameServerRecvClientResponse(this, p, sessionid);
			}
			break;
		case Packet.eC2SPKTLuaChannel:
			{
				Packet.C2S.LuaChannel p = (Packet.C2S.LuaChannel)packet;
				managerRPC.onTCPGameServerRecvLuaChannel(this, p, sessionid);
			}
			break;
		case Packet.eC2SPKTLuaChannel2:
			{
				Packet.C2S.LuaChannel2 p = (Packet.C2S.LuaChannel2)packet;
				managerRPC.onTCPGameServerRecvLuaChannel2(this, p, sessionid);
			}
			break;
		case Packet.eC2SPKTDataSync:
			{
				Packet.C2S.DataSync p = (Packet.C2S.DataSync)packet;
				managerRPC.onTCPGameServerRecvDataSync(this, p, sessionid);
			}
			break;
		case Packet.eC2SPKTAntiData:
			{
				Packet.C2S.AntiData p = (Packet.C2S.AntiData)packet;
				managerRPC.onTCPGameServerRecvAntiData(this, p, sessionid);
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

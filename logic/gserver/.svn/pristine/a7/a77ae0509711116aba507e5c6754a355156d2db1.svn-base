// modified by ket.kio.RPCGen at Wed Oct 29 20:47:48 CST 2014.

package i3k.global;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPGlobalServer extends TCPServer<SimplePacket>
{

	public TCPGlobalServer(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	@Override
	public int getMaxConnectionIdleTime()
	{
		return managerRPC.getTCPGlobalServerMaxConnectionIdleTime();
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// client to global
			case Packet.eC2GPKTLuaChannel:
			case Packet.eC2GPKTLuaChannel2:
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
		managerRPC.onTCPGlobalServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGlobalServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGlobalServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPGlobalServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGlobalServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// client to global
		case Packet.eC2GPKTLuaChannel:
			{
				Packet.C2G.LuaChannel p = (Packet.C2G.LuaChannel)packet;
				managerRPC.onTCPGlobalServerRecvLuaChannel(this, p, sessionid);
			}
			break;
		case Packet.eC2GPKTLuaChannel2:
			{
				Packet.C2G.LuaChannel2 p = (Packet.C2G.LuaChannel2)packet;
				managerRPC.onTCPGlobalServerRecvLuaChannel2(this, p, sessionid);
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

// modified by ket.kio.RPCGen at Wed Oct 29 20:47:48 CST 2014.

package i3k.gs.test;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPGlobalClient extends TCPClient<SimplePacket>
{

	public TCPGlobalClient(RPCManagerClient managerRPC)
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
			// global to client
			case Packet.eG2CPKTLuaChannel:
			case Packet.eG2CPKTLuaChannel2:
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
		managerRPC.onTCPGlobalClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGlobalClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPGlobalClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// global to client
		case Packet.eG2CPKTLuaChannel:
			{
				Packet.G2C.LuaChannel p = (Packet.G2C.LuaChannel)packet;
				managerRPC.onTCPGlobalClientRecvLuaChannel(this, p);
			}
			break;
		case Packet.eG2CPKTLuaChannel2:
			{
				Packet.G2C.LuaChannel2 p = (Packet.G2C.LuaChannel2)packet;
				managerRPC.onTCPGlobalClientRecvLuaChannel2(this, p);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManagerClient managerRPC;
}

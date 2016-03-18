// modified by ket.kio.RPCGen at Sat Dec 27 11:14:12 CST 2014.

package i3k.friend;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPFriendServer extends TCPServer<SimplePacket>
{

	public TCPFriendServer(RPCManagerFriendServer managerRPC)
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
			// server to friend
			case Packet.eS2FPKTWhoAmI:
			case Packet.eS2FPKTCreateRole:
			case Packet.eS2FPKTSearchFriends:
			case Packet.eS2FPKTQueryFriendProp:
			case Packet.eS2FPKTUpdateFriendProp:
			case Packet.eS2FPKTSendMessageToFriend:
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
		managerRPC.onTCPFriendServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPFriendServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPFriendServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPFriendServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPFriendServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// server to friend
		case Packet.eS2FPKTWhoAmI:
			{
				Packet.S2F.WhoAmI p = (Packet.S2F.WhoAmI)packet;
				managerRPC.onTCPFriendServerRecvWhoAmI(this, p, sessionid);
			}
			break;
		case Packet.eS2FPKTCreateRole:
			{
				Packet.S2F.CreateRole p = (Packet.S2F.CreateRole)packet;
				managerRPC.onTCPFriendServerRecvCreateRole(this, p, sessionid);
			}
			break;
		case Packet.eS2FPKTSearchFriends:
			{
				Packet.S2F.SearchFriends p = (Packet.S2F.SearchFriends)packet;
				managerRPC.onTCPFriendServerRecvSearchFriends(this, p, sessionid);
			}
			break;
		case Packet.eS2FPKTQueryFriendProp:
			{
				Packet.S2F.QueryFriendProp p = (Packet.S2F.QueryFriendProp)packet;
				managerRPC.onTCPFriendServerRecvQueryFriendProp(this, p, sessionid);
			}
			break;
		case Packet.eS2FPKTUpdateFriendProp:
			{
				Packet.S2F.UpdateFriendProp p = (Packet.S2F.UpdateFriendProp)packet;
				managerRPC.onTCPFriendServerRecvUpdateFriendProp(this, p, sessionid);
			}
			break;
		case Packet.eS2FPKTSendMessageToFriend:
			{
				Packet.S2F.SendMessageToFriend p = (Packet.S2F.SendMessageToFriend)packet;
				managerRPC.onTCPFriendServerRecvSendMessageToFriend(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManagerFriendServer managerRPC;
}

// modified by ket.kio.RPCGen at Sat Dec 27 11:14:12 CST 2014.

package i3k.gs;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPFriendClient extends TCPClient<SimplePacket>
{

	public TCPFriendClient(RPCManager managerRPC)
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
			// friend to server
			case Packet.eF2SPKTStateAnnounce:
			case Packet.eF2SPKTSearchFriends:
			case Packet.eF2SPKTQueryFriendProp:
			case Packet.eF2SPKTSendMessageToFriend:
			case Packet.eF2SPKTCreateRole:
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
		managerRPC.onTCPFriendClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPFriendClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPFriendClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// friend to server
		case Packet.eF2SPKTStateAnnounce:
			{
				Packet.F2S.StateAnnounce p = (Packet.F2S.StateAnnounce)packet;
				managerRPC.onTCPFriendClientRecvStateAnnounce(this, p);
			}
			break;
		case Packet.eF2SPKTSearchFriends:
			{
				Packet.F2S.SearchFriends p = (Packet.F2S.SearchFriends)packet;
				managerRPC.onTCPFriendClientRecvSearchFriends(this, p);
			}
			break;
		case Packet.eF2SPKTQueryFriendProp:
			{
				Packet.F2S.QueryFriendProp p = (Packet.F2S.QueryFriendProp)packet;
				managerRPC.onTCPFriendClientRecvQueryFriendProp(this, p);
			}
			break;
		case Packet.eF2SPKTSendMessageToFriend:
			{
				Packet.F2S.SendMessageToFriend p = (Packet.F2S.SendMessageToFriend)packet;
				managerRPC.onTCPFriendClientRecvSendMessageToFriend(this, p);
			}
			break;
		case Packet.eF2SPKTCreateRole:
			{
				Packet.F2S.CreateRole p = (Packet.F2S.CreateRole)packet;
				managerRPC.onTCPFriendClientRecvCreateRole(this, p);
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

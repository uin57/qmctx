// modified by ket.kio.RPCGen at Fri Feb 28 16:39:48 CST 2014.

package i3k.gs;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPControlServer extends TCPServer<SimplePacket>
{

	public TCPControlServer(RPCManager managerRPC)
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
			// manager to server
			case Packet.eM2SPKTShutdown:
			case Packet.eM2SPKTAnnounce:
			case Packet.eM2SPKTDataQuery:
			case Packet.eM2SPKTDataModify:
			case Packet.eM2SPKTSysMessage:
			case Packet.eM2SPKTRoleData:
			case Packet.eM2SPKTWorldGift:
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
		managerRPC.onTCPControlServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPControlServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPControlServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPControlServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPControlServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// manager to server
		case Packet.eM2SPKTShutdown:
			{
				Packet.M2S.Shutdown p = (Packet.M2S.Shutdown)packet;
				managerRPC.onTCPControlServerRecvShutdown(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTAnnounce:
			{
				Packet.M2S.Announce p = (Packet.M2S.Announce)packet;
				managerRPC.onTCPControlServerRecvAnnounce(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTDataQuery:
			{
				Packet.M2S.DataQuery p = (Packet.M2S.DataQuery)packet;
				managerRPC.onTCPControlServerRecvDataQuery(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTDataModify:
			{
				Packet.M2S.DataModify p = (Packet.M2S.DataModify)packet;
				managerRPC.onTCPControlServerRecvDataModify(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTSysMessage:
			{
				Packet.M2S.SysMessage p = (Packet.M2S.SysMessage)packet;
				managerRPC.onTCPControlServerRecvSysMessage(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTRoleData:
			{
				Packet.M2S.RoleData p = (Packet.M2S.RoleData)packet;
				managerRPC.onTCPControlServerRecvRoleData(this, p, sessionid);
			}
			break;
		case Packet.eM2SPKTWorldGift:
			{
				Packet.M2S.WorldGift p = (Packet.M2S.WorldGift)packet;
				managerRPC.onTCPControlServerRecvWorldGift(this, p, sessionid);
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

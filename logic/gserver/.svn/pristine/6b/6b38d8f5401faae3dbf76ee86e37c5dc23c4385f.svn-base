// modified by ket.kio.RPCGen at Fri Feb 28 16:39:48 CST 2014.

package i3k.gm;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPControlClient extends TCPClient<SimplePacket>
{

	public TCPControlClient(RPCManagerControlClient managerRPC)
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
			// server to manager
			case Packet.eS2MPKTShutdown:
			case Packet.eS2MPKTAnnounce:
			case Packet.eS2MPKTOnlineUser:
			case Packet.eS2MPKTQueryRoleBrief:
			case Packet.eS2MPKTDataModify:
			case Packet.eS2MPKTSysMessage:
			case Packet.eS2MPKTDataQuery:
			case Packet.eS2MPKTRoleData:
			case Packet.eS2MPKTWorldGift:
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
		managerRPC.onTCPControlClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPControlClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPControlClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// server to manager
		case Packet.eS2MPKTShutdown:
			{
				Packet.S2M.Shutdown p = (Packet.S2M.Shutdown)packet;
				managerRPC.onTCPControlClientRecvShutdown(this, p);
			}
			break;
		case Packet.eS2MPKTAnnounce:
			{
				Packet.S2M.Announce p = (Packet.S2M.Announce)packet;
				managerRPC.onTCPControlClientRecvAnnounce(this, p);
			}
			break;
		case Packet.eS2MPKTOnlineUser:
			{
				Packet.S2M.OnlineUser p = (Packet.S2M.OnlineUser)packet;
				managerRPC.onTCPControlClientRecvOnlineUser(this, p);
			}
			break;
		case Packet.eS2MPKTQueryRoleBrief:
			{
				Packet.S2M.QueryRoleBrief p = (Packet.S2M.QueryRoleBrief)packet;
				managerRPC.onTCPControlClientRecvQueryRoleBrief(this, p);
			}
			break;
		case Packet.eS2MPKTDataModify:
			{
				Packet.S2M.DataModify p = (Packet.S2M.DataModify)packet;
				managerRPC.onTCPControlClientRecvDataModify(this, p);
			}
			break;
		case Packet.eS2MPKTSysMessage:
			{
				Packet.S2M.SysMessage p = (Packet.S2M.SysMessage)packet;
				managerRPC.onTCPControlClientRecvSysMessage(this, p);
			}
			break;
		case Packet.eS2MPKTDataQuery:
			{
				Packet.S2M.DataQuery p = (Packet.S2M.DataQuery)packet;
				managerRPC.onTCPControlClientRecvDataQuery(this, p);
			}
			break;
		case Packet.eS2MPKTRoleData:
			{
				Packet.S2M.RoleData p = (Packet.S2M.RoleData)packet;
				managerRPC.onTCPControlClientRecvRoleData(this, p);
			}
			break;
		case Packet.eS2MPKTWorldGift:
			{
				Packet.S2M.WorldGift p = (Packet.S2M.WorldGift)packet;
				managerRPC.onTCPControlClientRecvWorldGift(this, p);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManagerControlClient managerRPC;
}

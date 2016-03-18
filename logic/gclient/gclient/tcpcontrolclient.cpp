// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpcontrolclient.h"
#include "rpcmanagercontrolclient.h"
#include "packet.h"

namespace I3K
{

	TCPControlClient::TCPControlClient(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet)
	{
	}

	bool TCPControlClient::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// server to manager
		case Packet::eS2MPKTShutdown:
		case Packet::eS2MPKTAnnounce:
		case Packet::eS2MPKTOnlineUser:
		case Packet::eS2MPKTQueryRoleBrief:
		case Packet::eS2MPKTDataModify:
		case Packet::eS2MPKTSysMessage:
		case Packet::eS2MPKTDataQuery:
		case Packet::eS2MPKTRoleData:
		case Packet::eS2MPKTWorldGift:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPControlClient::OnOpen()
	{
		m_pRPCManagerControlClient->AddTCPControlClientOpenMessage();
	}

	void TCPControlClient::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerControlClient->AddTCPControlClientOpenFailedMessage(errcode);
	}

	void TCPControlClient::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerControlClient->AddTCPControlClientCloseMessage(errcode);
	}

	void TCPControlClient::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)
	{
		m_pRPCManagerControlClient->AddTCPControlClientPacket(pPacket);
	}
	void TCPControlClient::OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eS2MPKTShutdown:
			{
				const Packet::S2M::Shutdown *p = dynamic_cast<const Packet::S2M::Shutdown*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvShutdown(this, p);
			}
			break;
		case Packet::eS2MPKTAnnounce:
			{
				const Packet::S2M::Announce *p = dynamic_cast<const Packet::S2M::Announce*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvAnnounce(this, p);
			}
			break;
		case Packet::eS2MPKTOnlineUser:
			{
				const Packet::S2M::OnlineUser *p = dynamic_cast<const Packet::S2M::OnlineUser*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvOnlineUser(this, p);
			}
			break;
		case Packet::eS2MPKTQueryRoleBrief:
			{
				const Packet::S2M::QueryRoleBrief *p = dynamic_cast<const Packet::S2M::QueryRoleBrief*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvQueryRoleBrief(this, p);
			}
			break;
		case Packet::eS2MPKTDataModify:
			{
				const Packet::S2M::DataModify *p = dynamic_cast<const Packet::S2M::DataModify*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvDataModify(this, p);
			}
			break;
		case Packet::eS2MPKTSysMessage:
			{
				const Packet::S2M::SysMessage *p = dynamic_cast<const Packet::S2M::SysMessage*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvSysMessage(this, p);
			}
			break;
		case Packet::eS2MPKTDataQuery:
			{
				const Packet::S2M::DataQuery *p = dynamic_cast<const Packet::S2M::DataQuery*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvDataQuery(this, p);
			}
			break;
		case Packet::eS2MPKTRoleData:
			{
				const Packet::S2M::RoleData *p = dynamic_cast<const Packet::S2M::RoleData*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvRoleData(this, p);
			}
			break;
		case Packet::eS2MPKTWorldGift:
			{
				const Packet::S2M::WorldGift *p = dynamic_cast<const Packet::S2M::WorldGift*>(pPacket);
				m_pRPCManagerControlClient->OnTCPControlClientRecvWorldGift(this, p);
			}
			break;
		default:
			break;
		}
	}

}


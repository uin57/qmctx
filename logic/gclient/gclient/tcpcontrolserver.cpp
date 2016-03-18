// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpcontrolserver.h"
#include "rpcmanager.h"
#include "packet.h"

namespace I3K
{

	TCPControlServer::TCPControlServer(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)
	{
	}

	bool TCPControlServer::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// manager to server
		case Packet::eM2SPKTShutdown:
		case Packet::eM2SPKTAnnounce:
		case Packet::eM2SPKTDataQuery:
		case Packet::eM2SPKTDataModify:
		case Packet::eM2SPKTSysMessage:
		case Packet::eM2SPKTRoleData:
		case Packet::eM2SPKTWorldGift:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPControlServer::OnOpen()
	{
		m_pRPCManager->OnTCPControlServerOpen(this);
	}

	void TCPControlServer::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPControlServerOpenFailed(this, errcode);
	}

	void TCPControlServer::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPControlServerClose(this, errcode);
	}

	void TCPControlServer::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)
	{
		m_pRPCManager->OnTCPControlServerSessionOpen(this, sessionid, addrClient);
	}

	void TCPControlServer::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPControlServerSessionClose(this, sessionid, errcode);
	}

	void TCPControlServer::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eM2SPKTShutdown:
			{
				const Packet::M2S::Shutdown *p = dynamic_cast<const Packet::M2S::Shutdown*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvShutdown(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTAnnounce:
			{
				const Packet::M2S::Announce *p = dynamic_cast<const Packet::M2S::Announce*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvAnnounce(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTDataQuery:
			{
				const Packet::M2S::DataQuery *p = dynamic_cast<const Packet::M2S::DataQuery*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvDataQuery(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTDataModify:
			{
				const Packet::M2S::DataModify *p = dynamic_cast<const Packet::M2S::DataModify*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvDataModify(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTSysMessage:
			{
				const Packet::M2S::SysMessage *p = dynamic_cast<const Packet::M2S::SysMessage*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvSysMessage(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTRoleData:
			{
				const Packet::M2S::RoleData *p = dynamic_cast<const Packet::M2S::RoleData*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvRoleData(this, p, sessionid);
			}
			break;
		case Packet::eM2SPKTWorldGift:
			{
				const Packet::M2S::WorldGift *p = dynamic_cast<const Packet::M2S::WorldGift*>(pPacket);
				m_pRPCManager->OnTCPControlServerRecvWorldGift(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

}


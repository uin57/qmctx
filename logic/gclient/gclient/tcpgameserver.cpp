// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpgameserver.h"
#include "rpcmanager.h"
#include "packet.h"

namespace I3K
{

	TCPGameServer::TCPGameServer(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)
	{
	}

	int TCPGameServer::GetMaxConnectionIdleTime()
	{
		return m_pRPCManager->GetTCPGameServerMaxConnectionIdleTime();
	}

	bool TCPGameServer::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// client to server
		case Packet::eC2SPKTClientResponse:
		case Packet::eC2SPKTLuaChannel:
		case Packet::eC2SPKTLuaChannel2:
		case Packet::eC2SPKTDataSync:
		case Packet::eC2SPKTAntiData:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPGameServer::OnOpen()
	{
		m_pRPCManager->OnTCPGameServerOpen(this);
	}

	void TCPGameServer::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGameServerOpenFailed(this, errcode);
	}

	void TCPGameServer::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGameServerClose(this, errcode);
	}

	void TCPGameServer::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)
	{
		m_pRPCManager->OnTCPGameServerSessionOpen(this, sessionid, addrClient);
	}

	void TCPGameServer::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGameServerSessionClose(this, sessionid, errcode);
	}

	void TCPGameServer::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eC2SPKTClientResponse:
			{
				const Packet::C2S::ClientResponse *p = dynamic_cast<const Packet::C2S::ClientResponse*>(pPacket);
				m_pRPCManager->OnTCPGameServerRecvClientResponse(this, p, sessionid);
			}
			break;
		case Packet::eC2SPKTLuaChannel:
			{
				const Packet::C2S::LuaChannel *p = dynamic_cast<const Packet::C2S::LuaChannel*>(pPacket);
				m_pRPCManager->OnTCPGameServerRecvLuaChannel(this, p, sessionid);
			}
			break;
		case Packet::eC2SPKTLuaChannel2:
			{
				const Packet::C2S::LuaChannel2 *p = dynamic_cast<const Packet::C2S::LuaChannel2*>(pPacket);
				m_pRPCManager->OnTCPGameServerRecvLuaChannel2(this, p, sessionid);
			}
			break;
		case Packet::eC2SPKTDataSync:
			{
				const Packet::C2S::DataSync *p = dynamic_cast<const Packet::C2S::DataSync*>(pPacket);
				m_pRPCManager->OnTCPGameServerRecvDataSync(this, p, sessionid);
			}
			break;
		case Packet::eC2SPKTAntiData:
			{
				const Packet::C2S::AntiData *p = dynamic_cast<const Packet::C2S::AntiData*>(pPacket);
				m_pRPCManager->OnTCPGameServerRecvAntiData(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

}


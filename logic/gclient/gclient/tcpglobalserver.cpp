// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpglobalserver.h"
#include "rpcmanager.h"
#include "packet.h"

namespace I3K
{

	TCPGlobalServer::TCPGlobalServer(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)
	{
	}

	int TCPGlobalServer::GetMaxConnectionIdleTime()
	{
		return m_pRPCManager->GetTCPGlobalServerMaxConnectionIdleTime();
	}

	bool TCPGlobalServer::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// client to global
		case Packet::eC2GPKTLuaChannel:
		case Packet::eC2GPKTLuaChannel2:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPGlobalServer::OnOpen()
	{
		m_pRPCManager->OnTCPGlobalServerOpen(this);
	}

	void TCPGlobalServer::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGlobalServerOpenFailed(this, errcode);
	}

	void TCPGlobalServer::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGlobalServerClose(this, errcode);
	}

	void TCPGlobalServer::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)
	{
		m_pRPCManager->OnTCPGlobalServerSessionOpen(this, sessionid, addrClient);
	}

	void TCPGlobalServer::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->OnTCPGlobalServerSessionClose(this, sessionid, errcode);
	}

	void TCPGlobalServer::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eC2GPKTLuaChannel:
			{
				const Packet::C2G::LuaChannel *p = dynamic_cast<const Packet::C2G::LuaChannel*>(pPacket);
				m_pRPCManager->OnTCPGlobalServerRecvLuaChannel(this, p, sessionid);
			}
			break;
		case Packet::eC2GPKTLuaChannel2:
			{
				const Packet::C2G::LuaChannel2 *p = dynamic_cast<const Packet::C2G::LuaChannel2*>(pPacket);
				m_pRPCManager->OnTCPGlobalServerRecvLuaChannel2(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

}


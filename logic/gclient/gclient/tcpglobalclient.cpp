// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpglobalclient.h"
#include "rpcmanagerclient.h"
#include "packet.h"

namespace I3K
{

	TCPGlobalClient::TCPGlobalClient(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet)
	{
	}

	bool TCPGlobalClient::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// global to client
		case Packet::eG2CPKTLuaChannel:
		case Packet::eG2CPKTLuaChannel2:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPGlobalClient::OnOpen()
	{
		m_pRPCManagerClient->AddTCPGlobalClientOpenMessage();
	}

	void TCPGlobalClient::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerClient->AddTCPGlobalClientOpenFailedMessage(errcode);
	}

	void TCPGlobalClient::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerClient->AddTCPGlobalClientCloseMessage(errcode);
	}

	void TCPGlobalClient::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)
	{
		m_pRPCManagerClient->AddTCPGlobalClientPacket(pPacket);
	}
	void TCPGlobalClient::OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eG2CPKTLuaChannel:
			{
				const Packet::G2C::LuaChannel *p = dynamic_cast<const Packet::G2C::LuaChannel*>(pPacket);
				m_pRPCManagerClient->OnTCPGlobalClientRecvLuaChannel(this, p);
			}
			break;
		case Packet::eG2CPKTLuaChannel2:
			{
				const Packet::G2C::LuaChannel2 *p = dynamic_cast<const Packet::G2C::LuaChannel2*>(pPacket);
				m_pRPCManagerClient->OnTCPGlobalClientRecvLuaChannel2(this, p);
			}
			break;
		default:
			break;
		}
	}

}


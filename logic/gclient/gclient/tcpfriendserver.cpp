// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpfriendserver.h"
#include "rpcmanagerfriendserver.h"
#include "packet.h"

namespace I3K
{

	TCPFriendServer::TCPFriendServer(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)
	{
	}

	bool TCPFriendServer::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// server to friend
		case Packet::eS2FPKTWhoAmI:
		case Packet::eS2FPKTCreateRole:
		case Packet::eS2FPKTSearchFriends:
		case Packet::eS2FPKTQueryFriendProp:
		case Packet::eS2FPKTUpdateFriendProp:
		case Packet::eS2FPKTSendMessageToFriend:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPFriendServer::OnOpen()
	{
		m_pRPCManagerFriendServer->OnTCPFriendServerOpen(this);
	}

	void TCPFriendServer::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerFriendServer->OnTCPFriendServerOpenFailed(this, errcode);
	}

	void TCPFriendServer::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerFriendServer->OnTCPFriendServerClose(this, errcode);
	}

	void TCPFriendServer::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)
	{
		m_pRPCManagerFriendServer->OnTCPFriendServerSessionOpen(this, sessionid, addrClient);
	}

	void TCPFriendServer::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerFriendServer->OnTCPFriendServerSessionClose(this, sessionid, errcode);
	}

	void TCPFriendServer::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eS2FPKTWhoAmI:
			{
				const Packet::S2F::WhoAmI *p = dynamic_cast<const Packet::S2F::WhoAmI*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvWhoAmI(this, p, sessionid);
			}
			break;
		case Packet::eS2FPKTCreateRole:
			{
				const Packet::S2F::CreateRole *p = dynamic_cast<const Packet::S2F::CreateRole*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvCreateRole(this, p, sessionid);
			}
			break;
		case Packet::eS2FPKTSearchFriends:
			{
				const Packet::S2F::SearchFriends *p = dynamic_cast<const Packet::S2F::SearchFriends*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvSearchFriends(this, p, sessionid);
			}
			break;
		case Packet::eS2FPKTQueryFriendProp:
			{
				const Packet::S2F::QueryFriendProp *p = dynamic_cast<const Packet::S2F::QueryFriendProp*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvQueryFriendProp(this, p, sessionid);
			}
			break;
		case Packet::eS2FPKTUpdateFriendProp:
			{
				const Packet::S2F::UpdateFriendProp *p = dynamic_cast<const Packet::S2F::UpdateFriendProp*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvUpdateFriendProp(this, p, sessionid);
			}
			break;
		case Packet::eS2FPKTSendMessageToFriend:
			{
				const Packet::S2F::SendMessageToFriend *p = dynamic_cast<const Packet::S2F::SendMessageToFriend*>(pPacket);
				m_pRPCManagerFriendServer->OnTCPFriendServerRecvSendMessageToFriend(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

}


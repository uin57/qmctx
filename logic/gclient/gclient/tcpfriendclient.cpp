// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "tcpfriendclient.h"
#include "rpcmanager.h"
#include "packet.h"

namespace I3K
{

	TCPFriendClient::TCPFriendClient(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet)
	{
	}

	bool TCPFriendClient::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// friend to server
		case Packet::eF2SPKTStateAnnounce:
		case Packet::eF2SPKTSearchFriends:
		case Packet::eF2SPKTQueryFriendProp:
		case Packet::eF2SPKTSendMessageToFriend:
		case Packet::eF2SPKTCreateRole:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPFriendClient::OnOpen()
	{
		m_pRPCManager->AddTCPFriendClientOpenMessage();
	}

	void TCPFriendClient::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->AddTCPFriendClientOpenFailedMessage(errcode);
	}

	void TCPFriendClient::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->AddTCPFriendClientCloseMessage(errcode);
	}

	void TCPFriendClient::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)
	{
		m_pRPCManager->AddTCPFriendClientPacket(pPacket);
	}
	void TCPFriendClient::OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eF2SPKTStateAnnounce:
			{
				const Packet::F2S::StateAnnounce *p = dynamic_cast<const Packet::F2S::StateAnnounce*>(pPacket);
				m_pRPCManager->OnTCPFriendClientRecvStateAnnounce(this, p);
			}
			break;
		case Packet::eF2SPKTSearchFriends:
			{
				const Packet::F2S::SearchFriends *p = dynamic_cast<const Packet::F2S::SearchFriends*>(pPacket);
				m_pRPCManager->OnTCPFriendClientRecvSearchFriends(this, p);
			}
			break;
		case Packet::eF2SPKTQueryFriendProp:
			{
				const Packet::F2S::QueryFriendProp *p = dynamic_cast<const Packet::F2S::QueryFriendProp*>(pPacket);
				m_pRPCManager->OnTCPFriendClientRecvQueryFriendProp(this, p);
			}
			break;
		case Packet::eF2SPKTSendMessageToFriend:
			{
				const Packet::F2S::SendMessageToFriend *p = dynamic_cast<const Packet::F2S::SendMessageToFriend*>(pPacket);
				m_pRPCManager->OnTCPFriendClientRecvSendMessageToFriend(this, p);
			}
			break;
		case Packet::eF2SPKTCreateRole:
			{
				const Packet::F2S::CreateRole *p = dynamic_cast<const Packet::F2S::CreateRole*>(pPacket);
				m_pRPCManager->OnTCPFriendClientRecvCreateRole(this, p);
			}
			break;
		default:
			break;
		}
	}

}


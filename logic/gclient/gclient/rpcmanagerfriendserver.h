// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#ifndef __I3K__RPCMANAGERFRIENDSERVER_H
#define __I3K__RPCMANAGERFRIENDSERVER_H

#include <ket/kio/netmanager.h>

#include "packet.h"
#include "tcpfriendserver.h"

namespace I3K
{

	class RPCManagerFriendServer
	{
	public:

		//// begin handlers.
		void OnTCPFriendServerOpen(TCPFriendServer* pPeer);
		void OnTCPFriendServerOpenFailed(TCPFriendServer* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPFriendServerClose(TCPFriendServer* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPFriendServerSessionOpen(TCPFriendServer* pPeer, int sessionid, const KET::KIO::NetAddress &addrClient);
		void OnTCPFriendServerSessionClose(TCPFriendServer* pPeer, int sessionid, KET::KIO::ErrorCode errcode);
		void OnTCPFriendServerRecvWhoAmI(TCPFriendServer *pPeer, const I3K::Packet::S2F::WhoAmI *pPacket, int sessionid);
		void OnTCPFriendServerRecvCreateRole(TCPFriendServer *pPeer, const I3K::Packet::S2F::CreateRole *pPacket, int sessionid);
		void OnTCPFriendServerRecvSearchFriends(TCPFriendServer *pPeer, const I3K::Packet::S2F::SearchFriends *pPacket, int sessionid);
		void OnTCPFriendServerRecvQueryFriendProp(TCPFriendServer *pPeer, const I3K::Packet::S2F::QueryFriendProp *pPacket, int sessionid);
		void OnTCPFriendServerRecvUpdateFriendProp(TCPFriendServer *pPeer, const I3K::Packet::S2F::UpdateFriendProp *pPacket, int sessionid);
		void OnTCPFriendServerRecvSendMessageToFriend(TCPFriendServer *pPeer, const I3K::Packet::S2F::SendMessageToFriend *pPacket, int sessionid);
		//// end handlers.
	};
}

#endif

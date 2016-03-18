// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "rpcmanagerfriendserver.h"

namespace I3K
{

	//// begin handlers.
	void RPCManagerFriendServer::OnTCPFriendServerOpen(TCPFriendServer* /*pPeer*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerOpenFailed(TCPFriendServer* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerClose(TCPFriendServer* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerSessionOpen(TCPFriendServer* /*pPeer*/, int /*sessionid*/, const KET::KIO::NetAddress & /*addrClient*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerSessionClose(TCPFriendServer* /*pPeer*/, int /*sessionid*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvWhoAmI(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::WhoAmI * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvCreateRole(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::CreateRole * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvSearchFriends(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::SearchFriends * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvQueryFriendProp(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::QueryFriendProp * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvUpdateFriendProp(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::UpdateFriendProp * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerFriendServer::OnTCPFriendServerRecvSendMessageToFriend(TCPFriendServer* /*pPeer*/, const I3K::Packet::S2F::SendMessageToFriend * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	//// end handlers.
}


// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "rpcmanagercontrolclient.h"

namespace I3K
{

	//// begin handlers.
	void RPCManagerControlClient::OnTCPControlClientOpen(TCPControlClient* /*pPeer*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientOpenFailed(TCPControlClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientClose(TCPControlClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvShutdown(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::Shutdown * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvAnnounce(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::Announce * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvOnlineUser(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::OnlineUser * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvQueryRoleBrief(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::QueryRoleBrief * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvDataModify(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::DataModify * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvSysMessage(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::SysMessage * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvDataQuery(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::DataQuery * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvRoleData(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::RoleData * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerControlClient::OnTCPControlClientRecvWorldGift(TCPControlClient* /*pPeer*/, const I3K::Packet::S2M::WorldGift * /*pPacket*/)
	{
		// TODO
	}

	//// end handlers.
}


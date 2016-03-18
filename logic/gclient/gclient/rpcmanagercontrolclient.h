// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#ifndef __I3K__RPCMANAGERCONTROLCLIENT_H
#define __I3K__RPCMANAGERCONTROLCLIENT_H

#include <ket/kio/netmanager.h>

#include "packet.h"
#include "tcpcontrolclient.h"

namespace I3K
{

	class RPCManagerControlClient
	{
	public:

		//// begin handlers.
		void OnTCPControlClientOpen(TCPControlClient* pPeer);
		void OnTCPControlClientOpenFailed(TCPControlClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPControlClientClose(TCPControlClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPControlClientRecvShutdown(TCPControlClient *pPeer, const I3K::Packet::S2M::Shutdown *pPacket);
		void OnTCPControlClientRecvAnnounce(TCPControlClient *pPeer, const I3K::Packet::S2M::Announce *pPacket);
		void OnTCPControlClientRecvOnlineUser(TCPControlClient *pPeer, const I3K::Packet::S2M::OnlineUser *pPacket);
		void OnTCPControlClientRecvQueryRoleBrief(TCPControlClient *pPeer, const I3K::Packet::S2M::QueryRoleBrief *pPacket);
		void OnTCPControlClientRecvDataModify(TCPControlClient *pPeer, const I3K::Packet::S2M::DataModify *pPacket);
		void OnTCPControlClientRecvSysMessage(TCPControlClient *pPeer, const I3K::Packet::S2M::SysMessage *pPacket);
		void OnTCPControlClientRecvDataQuery(TCPControlClient *pPeer, const I3K::Packet::S2M::DataQuery *pPacket);
		void OnTCPControlClientRecvRoleData(TCPControlClient *pPeer, const I3K::Packet::S2M::RoleData *pPacket);
		void OnTCPControlClientRecvWorldGift(TCPControlClient *pPeer, const I3K::Packet::S2M::WorldGift *pPacket);
		//// end handlers.
	};
}

#endif

// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#ifndef __I3K__RPCMANAGERCLIENT_H
#define __I3K__RPCMANAGERCLIENT_H

#include <ket/kio/netmanager.h>

#include "packet.h"
#include "tcpgameclient.h"
#include "tcpglobalclient.h"

namespace I3K
{

	class RPCManagerClient
	{
	public:

		//// begin handlers.
		void OnTCPGameClientOpen(TCPGameClient* pPeer);
		void OnTCPGameClientOpenFailed(TCPGameClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPGameClientClose(TCPGameClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPGameClientRecvServerChallenge(TCPGameClient *pPeer, const I3K::Packet::S2C::ServerChallenge *pPacket);
		void OnTCPGameClientRecvServerResponse(TCPGameClient *pPeer, const I3K::Packet::S2C::ServerResponse *pPacket);
		void OnTCPGameClientRecvLuaChannel(TCPGameClient *pPeer, const I3K::Packet::S2C::LuaChannel *pPacket);
		void OnTCPGameClientRecvLuaChannel2(TCPGameClient *pPeer, const I3K::Packet::S2C::LuaChannel2 *pPacket);
		void OnTCPGameClientRecvCmnRPCResponse(TCPGameClient *pPeer, const I3K::Packet::S2C::CmnRPCResponse *pPacket);
		void OnTCPGameClientRecvAntiData(TCPGameClient *pPeer, const I3K::Packet::S2C::AntiData *pPacket);
		void OnTCPGlobalClientOpen(TCPGlobalClient* pPeer);
		void OnTCPGlobalClientOpenFailed(TCPGlobalClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPGlobalClientClose(TCPGlobalClient* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPGlobalClientRecvLuaChannel(TCPGlobalClient *pPeer, const I3K::Packet::G2C::LuaChannel *pPacket);
		void OnTCPGlobalClientRecvLuaChannel2(TCPGlobalClient *pPeer, const I3K::Packet::G2C::LuaChannel2 *pPacket);
		//// end handlers.
	};
}

#endif

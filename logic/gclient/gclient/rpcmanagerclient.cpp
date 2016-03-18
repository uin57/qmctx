// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "rpcmanagerclient.h"

namespace I3K
{

	//// begin handlers.
	void RPCManagerClient::OnTCPGameClientOpen(TCPGameClient* /*pPeer*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientOpenFailed(TCPGameClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientClose(TCPGameClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvServerChallenge(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::ServerChallenge * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvServerResponse(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::ServerResponse * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvLuaChannel(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::LuaChannel * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvLuaChannel2(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::LuaChannel2 * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvCmnRPCResponse(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::CmnRPCResponse * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGameClientRecvAntiData(TCPGameClient* /*pPeer*/, const I3K::Packet::S2C::AntiData * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGlobalClientOpen(TCPGlobalClient* /*pPeer*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGlobalClientOpenFailed(TCPGlobalClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGlobalClientClose(TCPGlobalClient* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGlobalClientRecvLuaChannel(TCPGlobalClient* /*pPeer*/, const I3K::Packet::G2C::LuaChannel * /*pPacket*/)
	{
		// TODO
	}

	void RPCManagerClient::OnTCPGlobalClientRecvLuaChannel2(TCPGlobalClient* /*pPeer*/, const I3K::Packet::G2C::LuaChannel2 * /*pPacket*/)
	{
		// TODO
	}

	//// end handlers.
}


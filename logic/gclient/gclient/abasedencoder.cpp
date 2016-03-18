// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#include "abasedencoder.h"
#include "packet.h"

namespace I3K
{
	KET::KIO::SimplePacket* ABaseDencoder::CreatePacket(int ptype)
	{
		KET::KIO::SimplePacket *p = NULL;
		switch( ptype )
		{
		// server to client
		case Packet::eS2CPKTServerChallenge:
			p = new Packet::S2C::ServerChallenge();
			break;
		case Packet::eS2CPKTServerResponse:
			p = new Packet::S2C::ServerResponse();
			break;
		case Packet::eS2CPKTLuaChannel:
			p = new Packet::S2C::LuaChannel();
			break;
		case Packet::eS2CPKTLuaChannel2:
			p = new Packet::S2C::LuaChannel2();
			break;
		case Packet::eS2CPKTCmnRPCResponse:
			p = new Packet::S2C::CmnRPCResponse();
			break;
		case Packet::eS2CPKTAntiData:
			p = new Packet::S2C::AntiData();
			break;

		// client to server
		case Packet::eC2SPKTClientResponse:
			p = new Packet::C2S::ClientResponse();
			break;
		case Packet::eC2SPKTLuaChannel:
			p = new Packet::C2S::LuaChannel();
			break;
		case Packet::eC2SPKTLuaChannel2:
			p = new Packet::C2S::LuaChannel2();
			break;
		case Packet::eC2SPKTDataSync:
			p = new Packet::C2S::DataSync();
			break;
		case Packet::eC2SPKTAntiData:
			p = new Packet::C2S::AntiData();
			break;

		// global to client
		case Packet::eG2CPKTLuaChannel:
			p = new Packet::G2C::LuaChannel();
			break;
		case Packet::eG2CPKTLuaChannel2:
			p = new Packet::G2C::LuaChannel2();
			break;

		// client to global
		case Packet::eC2GPKTLuaChannel:
			p = new Packet::C2G::LuaChannel();
			break;
		case Packet::eC2GPKTLuaChannel2:
			p = new Packet::C2G::LuaChannel2();
			break;

		default:
			break;
		}
		return p;
	}
}

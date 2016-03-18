// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

#include "rpcmanagerexchangeserver.h"

namespace I3K
{

	//// begin handlers.
	void RPCManagerExchangeServer::OnTCPExchangeServerOpen(TCPExchangeServer* /*pPeer*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerOpenFailed(TCPExchangeServer* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerClose(TCPExchangeServer* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerSessionOpen(TCPExchangeServer* /*pPeer*/, int /*sessionid*/, const KET::KIO::NetAddress & /*addrClient*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerSessionClose(TCPExchangeServer* /*pPeer*/, int /*sessionid*/, KET::KIO::ErrorCode /*errcode*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvWhoAmI(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::WhoAmI * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSearchCity(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SearchCity * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSearchCityForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SearchCityForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvAttackCityStart(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::AttackCityStart * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvAttackCityStartForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::AttackCityStartForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvAttackCityEnd(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::AttackCityEnd * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvAttackCityEndForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::AttackCityEndForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvQueryCityOwner(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::QueryCityOwner * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvQueryCityOwnerForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::QueryCityOwnerForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvNotifyCityOwnerChanged(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::NotifyCityOwnerChanged * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvChannelMessageReq(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ChannelMessageReq * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvChannelMessageRes(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ChannelMessageRes * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelJoin(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelJoin * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelSelectGeneral(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelSelectGeneral * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelSelectGeneralForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelSelectGeneralForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelStartAttack(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelStartAttack * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelStartAttackForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelStartAttackForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelFinishAttack(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelFinishAttack * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelRank(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelRank * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelTopRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelTopRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelOppoGiveup(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelOppoGiveup * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelFinishAttackOnOppoDownLine(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelFinishAttackOnOppoDownLine * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvDuelCancelJoin(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::DuelCancelJoin * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpatriateDBRole(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpatriateDBRole * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpatriateDBRoleForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpatriateDBRoleForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpiratBossRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpiratBossRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpiratBossRank(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpiratBossRank * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpiratBossTopRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpiratBossTopRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpiratBossGlobalRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpiratBossGlobalRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvExpiratBossGlobalRanks2(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::ExpiratBossGlobalRanks2 * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvHeroesBossSync(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::HeroesBossSync * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvHeroesBossFinishAtt(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::HeroesBossFinishAtt * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvHeroesBossInfo(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::HeroesBossInfo * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarSearch(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarSearch * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarSearchForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarSearchForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarAttack(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarAttack * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarAttackForward(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarAttackForward * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarRank(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarRank * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarTopRanks(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarTopRanks * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	void RPCManagerExchangeServer::OnTCPExchangeServerRecvSWarVote(TCPExchangeServer* /*pPeer*/, const I3K::Packet::S2E::SWarVote * /*pPacket*/, int /*sessionid*/)
	{
		// TODO
	}

	//// end handlers.
}


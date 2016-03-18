// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

#ifndef __I3K__RPCMANAGEREXCHANGESERVER_H
#define __I3K__RPCMANAGEREXCHANGESERVER_H

#include <ket/kio/netmanager.h>

#include "packet.h"
#include "tcpexchangeserver.h"

namespace I3K
{

	class RPCManagerExchangeServer
	{
	public:

		//// begin handlers.
		void OnTCPExchangeServerOpen(TCPExchangeServer* pPeer);
		void OnTCPExchangeServerOpenFailed(TCPExchangeServer* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPExchangeServerClose(TCPExchangeServer* pPeer, KET::KIO::ErrorCode errcode);
		void OnTCPExchangeServerSessionOpen(TCPExchangeServer* pPeer, int sessionid, const KET::KIO::NetAddress &addrClient);
		void OnTCPExchangeServerSessionClose(TCPExchangeServer* pPeer, int sessionid, KET::KIO::ErrorCode errcode);
		void OnTCPExchangeServerRecvWhoAmI(TCPExchangeServer *pPeer, const I3K::Packet::S2E::WhoAmI *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSearchCity(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SearchCity *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSearchCityForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SearchCityForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvAttackCityStart(TCPExchangeServer *pPeer, const I3K::Packet::S2E::AttackCityStart *pPacket, int sessionid);
		void OnTCPExchangeServerRecvAttackCityStartForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::AttackCityStartForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvAttackCityEnd(TCPExchangeServer *pPeer, const I3K::Packet::S2E::AttackCityEnd *pPacket, int sessionid);
		void OnTCPExchangeServerRecvAttackCityEndForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::AttackCityEndForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvQueryCityOwner(TCPExchangeServer *pPeer, const I3K::Packet::S2E::QueryCityOwner *pPacket, int sessionid);
		void OnTCPExchangeServerRecvQueryCityOwnerForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::QueryCityOwnerForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvNotifyCityOwnerChanged(TCPExchangeServer *pPeer, const I3K::Packet::S2E::NotifyCityOwnerChanged *pPacket, int sessionid);
		void OnTCPExchangeServerRecvChannelMessageReq(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ChannelMessageReq *pPacket, int sessionid);
		void OnTCPExchangeServerRecvChannelMessageRes(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ChannelMessageRes *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelJoin(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelJoin *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelSelectGeneral(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelSelectGeneral *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelSelectGeneralForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelSelectGeneralForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelStartAttack(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelStartAttack *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelStartAttackForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelStartAttackForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelFinishAttack(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelFinishAttack *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelRank(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelRank *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelTopRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelTopRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelOppoGiveup(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelOppoGiveup *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelFinishAttackOnOppoDownLine(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelFinishAttackOnOppoDownLine *pPacket, int sessionid);
		void OnTCPExchangeServerRecvDuelCancelJoin(TCPExchangeServer *pPeer, const I3K::Packet::S2E::DuelCancelJoin *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpatriateDBRole(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpatriateDBRole *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpatriateDBRoleForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpatriateDBRoleForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpiratBossRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpiratBossRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpiratBossRank(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpiratBossRank *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpiratBossTopRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpiratBossTopRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpiratBossGlobalRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpiratBossGlobalRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvExpiratBossGlobalRanks2(TCPExchangeServer *pPeer, const I3K::Packet::S2E::ExpiratBossGlobalRanks2 *pPacket, int sessionid);
		void OnTCPExchangeServerRecvHeroesBossSync(TCPExchangeServer *pPeer, const I3K::Packet::S2E::HeroesBossSync *pPacket, int sessionid);
		void OnTCPExchangeServerRecvHeroesBossFinishAtt(TCPExchangeServer *pPeer, const I3K::Packet::S2E::HeroesBossFinishAtt *pPacket, int sessionid);
		void OnTCPExchangeServerRecvHeroesBossInfo(TCPExchangeServer *pPeer, const I3K::Packet::S2E::HeroesBossInfo *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarSearch(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarSearch *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarSearchForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarSearchForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarAttack(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarAttack *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarAttackForward(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarAttackForward *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarRank(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarRank *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarTopRanks(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarTopRanks *pPacket, int sessionid);
		void OnTCPExchangeServerRecvSWarVote(TCPExchangeServer *pPeer, const I3K::Packet::S2E::SWarVote *pPacket, int sessionid);
		//// end handlers.
	};
}

#endif

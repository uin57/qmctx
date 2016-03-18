// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

#include "tcpexchangeserver.h"
#include "rpcmanagerexchangeserver.h"
#include "packet.h"

namespace I3K
{

	TCPExchangeServer::TCPExchangeServer(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)
	{
	}

	int TCPExchangeServer::getRecvBufferSize()
	{
		return 1048576;
	}

	bool TCPExchangeServer::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// server to exchange
		case Packet::eS2EPKTWhoAmI:
		case Packet::eS2EPKTSearchCity:
		case Packet::eS2EPKTSearchCityForward:
		case Packet::eS2EPKTAttackCityStart:
		case Packet::eS2EPKTAttackCityStartForward:
		case Packet::eS2EPKTAttackCityEnd:
		case Packet::eS2EPKTAttackCityEndForward:
		case Packet::eS2EPKTQueryCityOwner:
		case Packet::eS2EPKTQueryCityOwnerForward:
		case Packet::eS2EPKTNotifyCityOwnerChanged:
		case Packet::eS2EPKTChannelMessageReq:
		case Packet::eS2EPKTChannelMessageRes:
		case Packet::eS2EPKTDuelJoin:
		case Packet::eS2EPKTDuelSelectGeneral:
		case Packet::eS2EPKTDuelSelectGeneralForward:
		case Packet::eS2EPKTDuelStartAttack:
		case Packet::eS2EPKTDuelStartAttackForward:
		case Packet::eS2EPKTDuelFinishAttack:
		case Packet::eS2EPKTDuelRanks:
		case Packet::eS2EPKTDuelRank:
		case Packet::eS2EPKTDuelTopRanks:
		case Packet::eS2EPKTDuelOppoGiveup:
		case Packet::eS2EPKTDuelFinishAttackOnOppoDownLine:
		case Packet::eS2EPKTDuelCancelJoin:
		case Packet::eS2EPKTExpatriateDBRole:
		case Packet::eS2EPKTExpatriateDBRoleForward:
		case Packet::eS2EPKTExpiratBossRanks:
		case Packet::eS2EPKTExpiratBossRank:
		case Packet::eS2EPKTExpiratBossTopRanks:
		case Packet::eS2EPKTExpiratBossGlobalRanks:
		case Packet::eS2EPKTExpiratBossGlobalRanks2:
		case Packet::eS2EPKTHeroesBossSync:
		case Packet::eS2EPKTHeroesBossFinishAtt:
		case Packet::eS2EPKTHeroesBossInfo:
		case Packet::eS2EPKTSWarSearch:
		case Packet::eS2EPKTSWarSearchForward:
		case Packet::eS2EPKTSWarAttack:
		case Packet::eS2EPKTSWarAttackForward:
		case Packet::eS2EPKTSWarRanks:
		case Packet::eS2EPKTSWarRank:
		case Packet::eS2EPKTSWarTopRanks:
		case Packet::eS2EPKTSWarVote:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPExchangeServer::OnOpen()
	{
		m_pRPCManagerExchangeServer->OnTCPExchangeServerOpen(this);
	}

	void TCPExchangeServer::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerExchangeServer->OnTCPExchangeServerOpenFailed(this, errcode);
	}

	void TCPExchangeServer::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerExchangeServer->OnTCPExchangeServerClose(this, errcode);
	}

	void TCPExchangeServer::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)
	{
		m_pRPCManagerExchangeServer->OnTCPExchangeServerSessionOpen(this, sessionid, addrClient);
	}

	void TCPExchangeServer::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)
	{
		m_pRPCManagerExchangeServer->OnTCPExchangeServerSessionClose(this, sessionid, errcode);
	}

	void TCPExchangeServer::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eS2EPKTWhoAmI:
			{
				const Packet::S2E::WhoAmI *p = dynamic_cast<const Packet::S2E::WhoAmI*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvWhoAmI(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSearchCity:
			{
				const Packet::S2E::SearchCity *p = dynamic_cast<const Packet::S2E::SearchCity*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSearchCity(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSearchCityForward:
			{
				const Packet::S2E::SearchCityForward *p = dynamic_cast<const Packet::S2E::SearchCityForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSearchCityForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTAttackCityStart:
			{
				const Packet::S2E::AttackCityStart *p = dynamic_cast<const Packet::S2E::AttackCityStart*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvAttackCityStart(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTAttackCityStartForward:
			{
				const Packet::S2E::AttackCityStartForward *p = dynamic_cast<const Packet::S2E::AttackCityStartForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvAttackCityStartForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTAttackCityEnd:
			{
				const Packet::S2E::AttackCityEnd *p = dynamic_cast<const Packet::S2E::AttackCityEnd*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvAttackCityEnd(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTAttackCityEndForward:
			{
				const Packet::S2E::AttackCityEndForward *p = dynamic_cast<const Packet::S2E::AttackCityEndForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvAttackCityEndForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTQueryCityOwner:
			{
				const Packet::S2E::QueryCityOwner *p = dynamic_cast<const Packet::S2E::QueryCityOwner*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvQueryCityOwner(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTQueryCityOwnerForward:
			{
				const Packet::S2E::QueryCityOwnerForward *p = dynamic_cast<const Packet::S2E::QueryCityOwnerForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvQueryCityOwnerForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTNotifyCityOwnerChanged:
			{
				const Packet::S2E::NotifyCityOwnerChanged *p = dynamic_cast<const Packet::S2E::NotifyCityOwnerChanged*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvNotifyCityOwnerChanged(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTChannelMessageReq:
			{
				const Packet::S2E::ChannelMessageReq *p = dynamic_cast<const Packet::S2E::ChannelMessageReq*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvChannelMessageReq(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTChannelMessageRes:
			{
				const Packet::S2E::ChannelMessageRes *p = dynamic_cast<const Packet::S2E::ChannelMessageRes*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvChannelMessageRes(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelJoin:
			{
				const Packet::S2E::DuelJoin *p = dynamic_cast<const Packet::S2E::DuelJoin*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelJoin(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelSelectGeneral:
			{
				const Packet::S2E::DuelSelectGeneral *p = dynamic_cast<const Packet::S2E::DuelSelectGeneral*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelSelectGeneral(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelSelectGeneralForward:
			{
				const Packet::S2E::DuelSelectGeneralForward *p = dynamic_cast<const Packet::S2E::DuelSelectGeneralForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelSelectGeneralForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelStartAttack:
			{
				const Packet::S2E::DuelStartAttack *p = dynamic_cast<const Packet::S2E::DuelStartAttack*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelStartAttack(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelStartAttackForward:
			{
				const Packet::S2E::DuelStartAttackForward *p = dynamic_cast<const Packet::S2E::DuelStartAttackForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelStartAttackForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelFinishAttack:
			{
				const Packet::S2E::DuelFinishAttack *p = dynamic_cast<const Packet::S2E::DuelFinishAttack*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelFinishAttack(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelRanks:
			{
				const Packet::S2E::DuelRanks *p = dynamic_cast<const Packet::S2E::DuelRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelRank:
			{
				const Packet::S2E::DuelRank *p = dynamic_cast<const Packet::S2E::DuelRank*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelRank(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelTopRanks:
			{
				const Packet::S2E::DuelTopRanks *p = dynamic_cast<const Packet::S2E::DuelTopRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelTopRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelOppoGiveup:
			{
				const Packet::S2E::DuelOppoGiveup *p = dynamic_cast<const Packet::S2E::DuelOppoGiveup*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelOppoGiveup(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelFinishAttackOnOppoDownLine:
			{
				const Packet::S2E::DuelFinishAttackOnOppoDownLine *p = dynamic_cast<const Packet::S2E::DuelFinishAttackOnOppoDownLine*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelFinishAttackOnOppoDownLine(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTDuelCancelJoin:
			{
				const Packet::S2E::DuelCancelJoin *p = dynamic_cast<const Packet::S2E::DuelCancelJoin*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvDuelCancelJoin(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpatriateDBRole:
			{
				const Packet::S2E::ExpatriateDBRole *p = dynamic_cast<const Packet::S2E::ExpatriateDBRole*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpatriateDBRole(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpatriateDBRoleForward:
			{
				const Packet::S2E::ExpatriateDBRoleForward *p = dynamic_cast<const Packet::S2E::ExpatriateDBRoleForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpatriateDBRoleForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpiratBossRanks:
			{
				const Packet::S2E::ExpiratBossRanks *p = dynamic_cast<const Packet::S2E::ExpiratBossRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpiratBossRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpiratBossRank:
			{
				const Packet::S2E::ExpiratBossRank *p = dynamic_cast<const Packet::S2E::ExpiratBossRank*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpiratBossRank(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpiratBossTopRanks:
			{
				const Packet::S2E::ExpiratBossTopRanks *p = dynamic_cast<const Packet::S2E::ExpiratBossTopRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpiratBossTopRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpiratBossGlobalRanks:
			{
				const Packet::S2E::ExpiratBossGlobalRanks *p = dynamic_cast<const Packet::S2E::ExpiratBossGlobalRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpiratBossGlobalRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTExpiratBossGlobalRanks2:
			{
				const Packet::S2E::ExpiratBossGlobalRanks2 *p = dynamic_cast<const Packet::S2E::ExpiratBossGlobalRanks2*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvExpiratBossGlobalRanks2(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTHeroesBossSync:
			{
				const Packet::S2E::HeroesBossSync *p = dynamic_cast<const Packet::S2E::HeroesBossSync*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvHeroesBossSync(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTHeroesBossFinishAtt:
			{
				const Packet::S2E::HeroesBossFinishAtt *p = dynamic_cast<const Packet::S2E::HeroesBossFinishAtt*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvHeroesBossFinishAtt(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTHeroesBossInfo:
			{
				const Packet::S2E::HeroesBossInfo *p = dynamic_cast<const Packet::S2E::HeroesBossInfo*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvHeroesBossInfo(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarSearch:
			{
				const Packet::S2E::SWarSearch *p = dynamic_cast<const Packet::S2E::SWarSearch*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarSearch(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarSearchForward:
			{
				const Packet::S2E::SWarSearchForward *p = dynamic_cast<const Packet::S2E::SWarSearchForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarSearchForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarAttack:
			{
				const Packet::S2E::SWarAttack *p = dynamic_cast<const Packet::S2E::SWarAttack*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarAttack(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarAttackForward:
			{
				const Packet::S2E::SWarAttackForward *p = dynamic_cast<const Packet::S2E::SWarAttackForward*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarAttackForward(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarRanks:
			{
				const Packet::S2E::SWarRanks *p = dynamic_cast<const Packet::S2E::SWarRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarRank:
			{
				const Packet::S2E::SWarRank *p = dynamic_cast<const Packet::S2E::SWarRank*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarRank(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarTopRanks:
			{
				const Packet::S2E::SWarTopRanks *p = dynamic_cast<const Packet::S2E::SWarTopRanks*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarTopRanks(this, p, sessionid);
			}
			break;
		case Packet::eS2EPKTSWarVote:
			{
				const Packet::S2E::SWarVote *p = dynamic_cast<const Packet::S2E::SWarVote*>(pPacket);
				m_pRPCManagerExchangeServer->OnTCPExchangeServerRecvSWarVote(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

}


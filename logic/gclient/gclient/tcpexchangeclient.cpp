// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

#include "tcpexchangeclient.h"
#include "rpcmanager.h"
#include "packet.h"

namespace I3K
{

	TCPExchangeClient::TCPExchangeClient(KET::KIO::NetManager &managerNet)
		: KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet)
	{
	}

	int TCPExchangeClient::getRecvBufferSize()
	{
		return 1048576;
	}

	bool TCPExchangeClient::DoCheckPacketType(int ptype)
	{
		switch( ptype )
		{
		// exchange to server
		case Packet::eE2SPKTStateAnnounce:
		case Packet::eE2SPKTSearchCity:
		case Packet::eE2SPKTSearchCityForward:
		case Packet::eE2SPKTAttackCityStart:
		case Packet::eE2SPKTAttackCityStartForward:
		case Packet::eE2SPKTAttackCityEnd:
		case Packet::eE2SPKTAttackCityEndForward:
		case Packet::eE2SPKTQueryCityOwner:
		case Packet::eE2SPKTQueryCityOwnerForward:
		case Packet::eE2SPKTNotifyCityOwnerChangedForward:
		case Packet::eE2SPKTChannelMessageReq:
		case Packet::eE2SPKTChannelMessageRes:
		case Packet::eE2SPKTDuelJoin:
		case Packet::eE2SPKTDuelSelectGeneral:
		case Packet::eE2SPKTDuelSelectGeneralForward:
		case Packet::eE2SPKTDuelStartAttack:
		case Packet::eE2SPKTDuelStartAttackForward:
		case Packet::eE2SPKTDuelFinishAttack:
		case Packet::eE2SPKTDuelRanks:
		case Packet::eE2SPKTDuelRank:
		case Packet::eE2SPKTDuelTopRanks:
		case Packet::eE2SPKTDuelOppoGiveup:
		case Packet::eE2SPKTDuelOppoGiveupForward:
		case Packet::eE2SPKTDuelFinishAttackOnOppoDownLineForward:
		case Packet::eE2SPKTDuelFinishAttackOnOppoDownLine:
		case Packet::eE2SPKTDuelGlobalRanks:
		case Packet::eE2SPKTExpatriateDBRole:
		case Packet::eE2SPKTExpatriateDBRoleForward:
		case Packet::eE2SPKTExpiratBossRanks:
		case Packet::eE2SPKTExpiratBossRank:
		case Packet::eE2SPKTExpiratBossTopRanks:
		case Packet::eE2SPKTExpiratBossGlobalRanks:
		case Packet::eE2SPKTExpiratBossGlobalRanks2:
		case Packet::eE2SPKTHeroesBossSync:
		case Packet::eE2SPKTHeroesBossFinishAtt:
		case Packet::eE2SPKTHeroesBossInfo:
		case Packet::eE2SPKTSWarSearch:
		case Packet::eE2SPKTSWarSearchForward:
		case Packet::eE2SPKTSWarAttack:
		case Packet::eE2SPKTSWarAttackForward:
		case Packet::eE2SPKTSWarRanks:
		case Packet::eE2SPKTSWarRank:
		case Packet::eE2SPKTSWarTopRanks:
		case Packet::eE2SPKTSWarGlobalRanks:
		case Packet::eE2SPKTSWarVoteForward:
		case Packet::eE2SPKTSWarRelease:
			return true;
		default:
			break;
		}
		return false;
	}

	void TCPExchangeClient::OnOpen()
	{
		m_pRPCManager->AddTCPExchangeClientOpenMessage();
	}

	void TCPExchangeClient::OnOpenFailed(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->AddTCPExchangeClientOpenFailedMessage(errcode);
	}

	void TCPExchangeClient::OnClose(KET::KIO::ErrorCode errcode)
	{
		m_pRPCManager->AddTCPExchangeClientCloseMessage(errcode);
	}

	void TCPExchangeClient::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)
	{
		m_pRPCManager->AddTCPExchangeClientPacket(pPacket);
	}
	void TCPExchangeClient::OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket)
	{
		switch( pPacket->GetType() )
		{
		case Packet::eE2SPKTStateAnnounce:
			{
				const Packet::E2S::StateAnnounce *p = dynamic_cast<const Packet::E2S::StateAnnounce*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvStateAnnounce(this, p);
			}
			break;
		case Packet::eE2SPKTSearchCity:
			{
				const Packet::E2S::SearchCity *p = dynamic_cast<const Packet::E2S::SearchCity*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSearchCity(this, p);
			}
			break;
		case Packet::eE2SPKTSearchCityForward:
			{
				const Packet::E2S::SearchCityForward *p = dynamic_cast<const Packet::E2S::SearchCityForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSearchCityForward(this, p);
			}
			break;
		case Packet::eE2SPKTAttackCityStart:
			{
				const Packet::E2S::AttackCityStart *p = dynamic_cast<const Packet::E2S::AttackCityStart*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvAttackCityStart(this, p);
			}
			break;
		case Packet::eE2SPKTAttackCityStartForward:
			{
				const Packet::E2S::AttackCityStartForward *p = dynamic_cast<const Packet::E2S::AttackCityStartForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvAttackCityStartForward(this, p);
			}
			break;
		case Packet::eE2SPKTAttackCityEnd:
			{
				const Packet::E2S::AttackCityEnd *p = dynamic_cast<const Packet::E2S::AttackCityEnd*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvAttackCityEnd(this, p);
			}
			break;
		case Packet::eE2SPKTAttackCityEndForward:
			{
				const Packet::E2S::AttackCityEndForward *p = dynamic_cast<const Packet::E2S::AttackCityEndForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvAttackCityEndForward(this, p);
			}
			break;
		case Packet::eE2SPKTQueryCityOwner:
			{
				const Packet::E2S::QueryCityOwner *p = dynamic_cast<const Packet::E2S::QueryCityOwner*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvQueryCityOwner(this, p);
			}
			break;
		case Packet::eE2SPKTQueryCityOwnerForward:
			{
				const Packet::E2S::QueryCityOwnerForward *p = dynamic_cast<const Packet::E2S::QueryCityOwnerForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvQueryCityOwnerForward(this, p);
			}
			break;
		case Packet::eE2SPKTNotifyCityOwnerChangedForward:
			{
				const Packet::E2S::NotifyCityOwnerChangedForward *p = dynamic_cast<const Packet::E2S::NotifyCityOwnerChangedForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvNotifyCityOwnerChangedForward(this, p);
			}
			break;
		case Packet::eE2SPKTChannelMessageReq:
			{
				const Packet::E2S::ChannelMessageReq *p = dynamic_cast<const Packet::E2S::ChannelMessageReq*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvChannelMessageReq(this, p);
			}
			break;
		case Packet::eE2SPKTChannelMessageRes:
			{
				const Packet::E2S::ChannelMessageRes *p = dynamic_cast<const Packet::E2S::ChannelMessageRes*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvChannelMessageRes(this, p);
			}
			break;
		case Packet::eE2SPKTDuelJoin:
			{
				const Packet::E2S::DuelJoin *p = dynamic_cast<const Packet::E2S::DuelJoin*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelJoin(this, p);
			}
			break;
		case Packet::eE2SPKTDuelSelectGeneral:
			{
				const Packet::E2S::DuelSelectGeneral *p = dynamic_cast<const Packet::E2S::DuelSelectGeneral*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelSelectGeneral(this, p);
			}
			break;
		case Packet::eE2SPKTDuelSelectGeneralForward:
			{
				const Packet::E2S::DuelSelectGeneralForward *p = dynamic_cast<const Packet::E2S::DuelSelectGeneralForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelSelectGeneralForward(this, p);
			}
			break;
		case Packet::eE2SPKTDuelStartAttack:
			{
				const Packet::E2S::DuelStartAttack *p = dynamic_cast<const Packet::E2S::DuelStartAttack*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelStartAttack(this, p);
			}
			break;
		case Packet::eE2SPKTDuelStartAttackForward:
			{
				const Packet::E2S::DuelStartAttackForward *p = dynamic_cast<const Packet::E2S::DuelStartAttackForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelStartAttackForward(this, p);
			}
			break;
		case Packet::eE2SPKTDuelFinishAttack:
			{
				const Packet::E2S::DuelFinishAttack *p = dynamic_cast<const Packet::E2S::DuelFinishAttack*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelFinishAttack(this, p);
			}
			break;
		case Packet::eE2SPKTDuelRanks:
			{
				const Packet::E2S::DuelRanks *p = dynamic_cast<const Packet::E2S::DuelRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelRanks(this, p);
			}
			break;
		case Packet::eE2SPKTDuelRank:
			{
				const Packet::E2S::DuelRank *p = dynamic_cast<const Packet::E2S::DuelRank*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelRank(this, p);
			}
			break;
		case Packet::eE2SPKTDuelTopRanks:
			{
				const Packet::E2S::DuelTopRanks *p = dynamic_cast<const Packet::E2S::DuelTopRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelTopRanks(this, p);
			}
			break;
		case Packet::eE2SPKTDuelOppoGiveup:
			{
				const Packet::E2S::DuelOppoGiveup *p = dynamic_cast<const Packet::E2S::DuelOppoGiveup*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelOppoGiveup(this, p);
			}
			break;
		case Packet::eE2SPKTDuelOppoGiveupForward:
			{
				const Packet::E2S::DuelOppoGiveupForward *p = dynamic_cast<const Packet::E2S::DuelOppoGiveupForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelOppoGiveupForward(this, p);
			}
			break;
		case Packet::eE2SPKTDuelFinishAttackOnOppoDownLineForward:
			{
				const Packet::E2S::DuelFinishAttackOnOppoDownLineForward *p = dynamic_cast<const Packet::E2S::DuelFinishAttackOnOppoDownLineForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelFinishAttackOnOppoDownLineForward(this, p);
			}
			break;
		case Packet::eE2SPKTDuelFinishAttackOnOppoDownLine:
			{
				const Packet::E2S::DuelFinishAttackOnOppoDownLine *p = dynamic_cast<const Packet::E2S::DuelFinishAttackOnOppoDownLine*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelFinishAttackOnOppoDownLine(this, p);
			}
			break;
		case Packet::eE2SPKTDuelGlobalRanks:
			{
				const Packet::E2S::DuelGlobalRanks *p = dynamic_cast<const Packet::E2S::DuelGlobalRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvDuelGlobalRanks(this, p);
			}
			break;
		case Packet::eE2SPKTExpatriateDBRole:
			{
				const Packet::E2S::ExpatriateDBRole *p = dynamic_cast<const Packet::E2S::ExpatriateDBRole*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpatriateDBRole(this, p);
			}
			break;
		case Packet::eE2SPKTExpatriateDBRoleForward:
			{
				const Packet::E2S::ExpatriateDBRoleForward *p = dynamic_cast<const Packet::E2S::ExpatriateDBRoleForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpatriateDBRoleForward(this, p);
			}
			break;
		case Packet::eE2SPKTExpiratBossRanks:
			{
				const Packet::E2S::ExpiratBossRanks *p = dynamic_cast<const Packet::E2S::ExpiratBossRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpiratBossRanks(this, p);
			}
			break;
		case Packet::eE2SPKTExpiratBossRank:
			{
				const Packet::E2S::ExpiratBossRank *p = dynamic_cast<const Packet::E2S::ExpiratBossRank*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpiratBossRank(this, p);
			}
			break;
		case Packet::eE2SPKTExpiratBossTopRanks:
			{
				const Packet::E2S::ExpiratBossTopRanks *p = dynamic_cast<const Packet::E2S::ExpiratBossTopRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpiratBossTopRanks(this, p);
			}
			break;
		case Packet::eE2SPKTExpiratBossGlobalRanks:
			{
				const Packet::E2S::ExpiratBossGlobalRanks *p = dynamic_cast<const Packet::E2S::ExpiratBossGlobalRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpiratBossGlobalRanks(this, p);
			}
			break;
		case Packet::eE2SPKTExpiratBossGlobalRanks2:
			{
				const Packet::E2S::ExpiratBossGlobalRanks2 *p = dynamic_cast<const Packet::E2S::ExpiratBossGlobalRanks2*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvExpiratBossGlobalRanks2(this, p);
			}
			break;
		case Packet::eE2SPKTHeroesBossSync:
			{
				const Packet::E2S::HeroesBossSync *p = dynamic_cast<const Packet::E2S::HeroesBossSync*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvHeroesBossSync(this, p);
			}
			break;
		case Packet::eE2SPKTHeroesBossFinishAtt:
			{
				const Packet::E2S::HeroesBossFinishAtt *p = dynamic_cast<const Packet::E2S::HeroesBossFinishAtt*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvHeroesBossFinishAtt(this, p);
			}
			break;
		case Packet::eE2SPKTHeroesBossInfo:
			{
				const Packet::E2S::HeroesBossInfo *p = dynamic_cast<const Packet::E2S::HeroesBossInfo*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvHeroesBossInfo(this, p);
			}
			break;
		case Packet::eE2SPKTSWarSearch:
			{
				const Packet::E2S::SWarSearch *p = dynamic_cast<const Packet::E2S::SWarSearch*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarSearch(this, p);
			}
			break;
		case Packet::eE2SPKTSWarSearchForward:
			{
				const Packet::E2S::SWarSearchForward *p = dynamic_cast<const Packet::E2S::SWarSearchForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarSearchForward(this, p);
			}
			break;
		case Packet::eE2SPKTSWarAttack:
			{
				const Packet::E2S::SWarAttack *p = dynamic_cast<const Packet::E2S::SWarAttack*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarAttack(this, p);
			}
			break;
		case Packet::eE2SPKTSWarAttackForward:
			{
				const Packet::E2S::SWarAttackForward *p = dynamic_cast<const Packet::E2S::SWarAttackForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarAttackForward(this, p);
			}
			break;
		case Packet::eE2SPKTSWarRanks:
			{
				const Packet::E2S::SWarRanks *p = dynamic_cast<const Packet::E2S::SWarRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarRanks(this, p);
			}
			break;
		case Packet::eE2SPKTSWarRank:
			{
				const Packet::E2S::SWarRank *p = dynamic_cast<const Packet::E2S::SWarRank*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarRank(this, p);
			}
			break;
		case Packet::eE2SPKTSWarTopRanks:
			{
				const Packet::E2S::SWarTopRanks *p = dynamic_cast<const Packet::E2S::SWarTopRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarTopRanks(this, p);
			}
			break;
		case Packet::eE2SPKTSWarGlobalRanks:
			{
				const Packet::E2S::SWarGlobalRanks *p = dynamic_cast<const Packet::E2S::SWarGlobalRanks*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarGlobalRanks(this, p);
			}
			break;
		case Packet::eE2SPKTSWarVoteForward:
			{
				const Packet::E2S::SWarVoteForward *p = dynamic_cast<const Packet::E2S::SWarVoteForward*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarVoteForward(this, p);
			}
			break;
		case Packet::eE2SPKTSWarRelease:
			{
				const Packet::E2S::SWarRelease *p = dynamic_cast<const Packet::E2S::SWarRelease*>(pPacket);
				m_pRPCManager->OnTCPExchangeClientRecvSWarRelease(this, p);
			}
			break;
		default:
			break;
		}
	}

}


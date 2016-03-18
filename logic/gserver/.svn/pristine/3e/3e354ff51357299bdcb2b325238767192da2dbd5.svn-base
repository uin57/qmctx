// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.gs;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPExchangeClient extends TCPClient<SimplePacket>
{

	public TCPExchangeClient(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	@Override
	public int getRecvBufferSize()
	{
		return 1048576;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// exchange to server
			case Packet.eE2SPKTStateAnnounce:
			case Packet.eE2SPKTSearchCity:
			case Packet.eE2SPKTSearchCityForward:
			case Packet.eE2SPKTAttackCityStart:
			case Packet.eE2SPKTAttackCityStartForward:
			case Packet.eE2SPKTAttackCityEnd:
			case Packet.eE2SPKTAttackCityEndForward:
			case Packet.eE2SPKTQueryCityOwner:
			case Packet.eE2SPKTQueryCityOwnerForward:
			case Packet.eE2SPKTNotifyCityOwnerChangedForward:
			case Packet.eE2SPKTChannelMessageReq:
			case Packet.eE2SPKTChannelMessageRes:
			case Packet.eE2SPKTDuelJoin:
			case Packet.eE2SPKTDuelSelectGeneral:
			case Packet.eE2SPKTDuelSelectGeneralForward:
			case Packet.eE2SPKTDuelStartAttack:
			case Packet.eE2SPKTDuelStartAttackForward:
			case Packet.eE2SPKTDuelFinishAttack:
			case Packet.eE2SPKTDuelRanks:
			case Packet.eE2SPKTDuelRank:
			case Packet.eE2SPKTDuelTopRanks:
			case Packet.eE2SPKTDuelOppoGiveup:
			case Packet.eE2SPKTDuelOppoGiveupForward:
			case Packet.eE2SPKTDuelFinishAttackOnOppoDownLineForward:
			case Packet.eE2SPKTDuelFinishAttackOnOppoDownLine:
			case Packet.eE2SPKTDuelGlobalRanks:
			case Packet.eE2SPKTExpatriateDBRole:
			case Packet.eE2SPKTExpatriateDBRoleForward:
			case Packet.eE2SPKTExpiratBossRanks:
			case Packet.eE2SPKTExpiratBossRank:
			case Packet.eE2SPKTExpiratBossTopRanks:
			case Packet.eE2SPKTExpiratBossGlobalRanks:
			case Packet.eE2SPKTExpiratBossGlobalRanks2:
			case Packet.eE2SPKTHeroesBossSync:
			case Packet.eE2SPKTHeroesBossFinishAtt:
			case Packet.eE2SPKTHeroesBossInfo:
			case Packet.eE2SPKTSWarSearch:
			case Packet.eE2SPKTSWarSearchForward:
			case Packet.eE2SPKTSWarAttack:
			case Packet.eE2SPKTSWarAttackForward:
			case Packet.eE2SPKTSWarRanks:
			case Packet.eE2SPKTSWarRank:
			case Packet.eE2SPKTSWarTopRanks:
			case Packet.eE2SPKTSWarGlobalRanks:
			case Packet.eE2SPKTSWarVoteForward:
			case Packet.eE2SPKTSWarRelease:
				return true;
			default:
				break;
			}
			return false;
		}
	}

	@Override
	public PacketEncoder<SimplePacket> getEncoder()
	{
		return dencoder;
	}

	@Override
	public PacketDecoder<SimplePacket> getDecoder()
	{
		return dencoder;
	}

	@Override
	public void onOpen()
	{
		managerRPC.onTCPExchangeClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPExchangeClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPExchangeClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// exchange to server
		case Packet.eE2SPKTStateAnnounce:
			{
				Packet.E2S.StateAnnounce p = (Packet.E2S.StateAnnounce)packet;
				managerRPC.onTCPExchangeClientRecvStateAnnounce(this, p);
			}
			break;
		case Packet.eE2SPKTSearchCity:
			{
				Packet.E2S.SearchCity p = (Packet.E2S.SearchCity)packet;
				managerRPC.onTCPExchangeClientRecvSearchCity(this, p);
			}
			break;
		case Packet.eE2SPKTSearchCityForward:
			{
				Packet.E2S.SearchCityForward p = (Packet.E2S.SearchCityForward)packet;
				managerRPC.onTCPExchangeClientRecvSearchCityForward(this, p);
			}
			break;
		case Packet.eE2SPKTAttackCityStart:
			{
				Packet.E2S.AttackCityStart p = (Packet.E2S.AttackCityStart)packet;
				managerRPC.onTCPExchangeClientRecvAttackCityStart(this, p);
			}
			break;
		case Packet.eE2SPKTAttackCityStartForward:
			{
				Packet.E2S.AttackCityStartForward p = (Packet.E2S.AttackCityStartForward)packet;
				managerRPC.onTCPExchangeClientRecvAttackCityStartForward(this, p);
			}
			break;
		case Packet.eE2SPKTAttackCityEnd:
			{
				Packet.E2S.AttackCityEnd p = (Packet.E2S.AttackCityEnd)packet;
				managerRPC.onTCPExchangeClientRecvAttackCityEnd(this, p);
			}
			break;
		case Packet.eE2SPKTAttackCityEndForward:
			{
				Packet.E2S.AttackCityEndForward p = (Packet.E2S.AttackCityEndForward)packet;
				managerRPC.onTCPExchangeClientRecvAttackCityEndForward(this, p);
			}
			break;
		case Packet.eE2SPKTQueryCityOwner:
			{
				Packet.E2S.QueryCityOwner p = (Packet.E2S.QueryCityOwner)packet;
				managerRPC.onTCPExchangeClientRecvQueryCityOwner(this, p);
			}
			break;
		case Packet.eE2SPKTQueryCityOwnerForward:
			{
				Packet.E2S.QueryCityOwnerForward p = (Packet.E2S.QueryCityOwnerForward)packet;
				managerRPC.onTCPExchangeClientRecvQueryCityOwnerForward(this, p);
			}
			break;
		case Packet.eE2SPKTNotifyCityOwnerChangedForward:
			{
				Packet.E2S.NotifyCityOwnerChangedForward p = (Packet.E2S.NotifyCityOwnerChangedForward)packet;
				managerRPC.onTCPExchangeClientRecvNotifyCityOwnerChangedForward(this, p);
			}
			break;
		case Packet.eE2SPKTChannelMessageReq:
			{
				Packet.E2S.ChannelMessageReq p = (Packet.E2S.ChannelMessageReq)packet;
				managerRPC.onTCPExchangeClientRecvChannelMessageReq(this, p);
			}
			break;
		case Packet.eE2SPKTChannelMessageRes:
			{
				Packet.E2S.ChannelMessageRes p = (Packet.E2S.ChannelMessageRes)packet;
				managerRPC.onTCPExchangeClientRecvChannelMessageRes(this, p);
			}
			break;
		case Packet.eE2SPKTDuelJoin:
			{
				Packet.E2S.DuelJoin p = (Packet.E2S.DuelJoin)packet;
				managerRPC.onTCPExchangeClientRecvDuelJoin(this, p);
			}
			break;
		case Packet.eE2SPKTDuelSelectGeneral:
			{
				Packet.E2S.DuelSelectGeneral p = (Packet.E2S.DuelSelectGeneral)packet;
				managerRPC.onTCPExchangeClientRecvDuelSelectGeneral(this, p);
			}
			break;
		case Packet.eE2SPKTDuelSelectGeneralForward:
			{
				Packet.E2S.DuelSelectGeneralForward p = (Packet.E2S.DuelSelectGeneralForward)packet;
				managerRPC.onTCPExchangeClientRecvDuelSelectGeneralForward(this, p);
			}
			break;
		case Packet.eE2SPKTDuelStartAttack:
			{
				Packet.E2S.DuelStartAttack p = (Packet.E2S.DuelStartAttack)packet;
				managerRPC.onTCPExchangeClientRecvDuelStartAttack(this, p);
			}
			break;
		case Packet.eE2SPKTDuelStartAttackForward:
			{
				Packet.E2S.DuelStartAttackForward p = (Packet.E2S.DuelStartAttackForward)packet;
				managerRPC.onTCPExchangeClientRecvDuelStartAttackForward(this, p);
			}
			break;
		case Packet.eE2SPKTDuelFinishAttack:
			{
				Packet.E2S.DuelFinishAttack p = (Packet.E2S.DuelFinishAttack)packet;
				managerRPC.onTCPExchangeClientRecvDuelFinishAttack(this, p);
			}
			break;
		case Packet.eE2SPKTDuelRanks:
			{
				Packet.E2S.DuelRanks p = (Packet.E2S.DuelRanks)packet;
				managerRPC.onTCPExchangeClientRecvDuelRanks(this, p);
			}
			break;
		case Packet.eE2SPKTDuelRank:
			{
				Packet.E2S.DuelRank p = (Packet.E2S.DuelRank)packet;
				managerRPC.onTCPExchangeClientRecvDuelRank(this, p);
			}
			break;
		case Packet.eE2SPKTDuelTopRanks:
			{
				Packet.E2S.DuelTopRanks p = (Packet.E2S.DuelTopRanks)packet;
				managerRPC.onTCPExchangeClientRecvDuelTopRanks(this, p);
			}
			break;
		case Packet.eE2SPKTDuelOppoGiveup:
			{
				Packet.E2S.DuelOppoGiveup p = (Packet.E2S.DuelOppoGiveup)packet;
				managerRPC.onTCPExchangeClientRecvDuelOppoGiveup(this, p);
			}
			break;
		case Packet.eE2SPKTDuelOppoGiveupForward:
			{
				Packet.E2S.DuelOppoGiveupForward p = (Packet.E2S.DuelOppoGiveupForward)packet;
				managerRPC.onTCPExchangeClientRecvDuelOppoGiveupForward(this, p);
			}
			break;
		case Packet.eE2SPKTDuelFinishAttackOnOppoDownLineForward:
			{
				Packet.E2S.DuelFinishAttackOnOppoDownLineForward p = (Packet.E2S.DuelFinishAttackOnOppoDownLineForward)packet;
				managerRPC.onTCPExchangeClientRecvDuelFinishAttackOnOppoDownLineForward(this, p);
			}
			break;
		case Packet.eE2SPKTDuelFinishAttackOnOppoDownLine:
			{
				Packet.E2S.DuelFinishAttackOnOppoDownLine p = (Packet.E2S.DuelFinishAttackOnOppoDownLine)packet;
				managerRPC.onTCPExchangeClientRecvDuelFinishAttackOnOppoDownLine(this, p);
			}
			break;
		case Packet.eE2SPKTDuelGlobalRanks:
			{
				Packet.E2S.DuelGlobalRanks p = (Packet.E2S.DuelGlobalRanks)packet;
				managerRPC.onTCPExchangeClientRecvDuelGlobalRanks(this, p);
			}
			break;
		case Packet.eE2SPKTExpatriateDBRole:
			{
				Packet.E2S.ExpatriateDBRole p = (Packet.E2S.ExpatriateDBRole)packet;
				managerRPC.onTCPExchangeClientRecvExpatriateDBRole(this, p);
			}
			break;
		case Packet.eE2SPKTExpatriateDBRoleForward:
			{
				Packet.E2S.ExpatriateDBRoleForward p = (Packet.E2S.ExpatriateDBRoleForward)packet;
				managerRPC.onTCPExchangeClientRecvExpatriateDBRoleForward(this, p);
			}
			break;
		case Packet.eE2SPKTExpiratBossRanks:
			{
				Packet.E2S.ExpiratBossRanks p = (Packet.E2S.ExpiratBossRanks)packet;
				managerRPC.onTCPExchangeClientRecvExpiratBossRanks(this, p);
			}
			break;
		case Packet.eE2SPKTExpiratBossRank:
			{
				Packet.E2S.ExpiratBossRank p = (Packet.E2S.ExpiratBossRank)packet;
				managerRPC.onTCPExchangeClientRecvExpiratBossRank(this, p);
			}
			break;
		case Packet.eE2SPKTExpiratBossTopRanks:
			{
				Packet.E2S.ExpiratBossTopRanks p = (Packet.E2S.ExpiratBossTopRanks)packet;
				managerRPC.onTCPExchangeClientRecvExpiratBossTopRanks(this, p);
			}
			break;
		case Packet.eE2SPKTExpiratBossGlobalRanks:
			{
				Packet.E2S.ExpiratBossGlobalRanks p = (Packet.E2S.ExpiratBossGlobalRanks)packet;
				managerRPC.onTCPExchangeClientRecvExpiratBossGlobalRanks(this, p);
			}
			break;
		case Packet.eE2SPKTExpiratBossGlobalRanks2:
			{
				Packet.E2S.ExpiratBossGlobalRanks2 p = (Packet.E2S.ExpiratBossGlobalRanks2)packet;
				managerRPC.onTCPExchangeClientRecvExpiratBossGlobalRanks2(this, p);
			}
			break;
		case Packet.eE2SPKTHeroesBossSync:
			{
				Packet.E2S.HeroesBossSync p = (Packet.E2S.HeroesBossSync)packet;
				managerRPC.onTCPExchangeClientRecvHeroesBossSync(this, p);
			}
			break;
		case Packet.eE2SPKTHeroesBossFinishAtt:
			{
				Packet.E2S.HeroesBossFinishAtt p = (Packet.E2S.HeroesBossFinishAtt)packet;
				managerRPC.onTCPExchangeClientRecvHeroesBossFinishAtt(this, p);
			}
			break;
		case Packet.eE2SPKTHeroesBossInfo:
			{
				Packet.E2S.HeroesBossInfo p = (Packet.E2S.HeroesBossInfo)packet;
				managerRPC.onTCPExchangeClientRecvHeroesBossInfo(this, p);
			}
			break;
		case Packet.eE2SPKTSWarSearch:
			{
				Packet.E2S.SWarSearch p = (Packet.E2S.SWarSearch)packet;
				managerRPC.onTCPExchangeClientRecvSWarSearch(this, p);
			}
			break;
		case Packet.eE2SPKTSWarSearchForward:
			{
				Packet.E2S.SWarSearchForward p = (Packet.E2S.SWarSearchForward)packet;
				managerRPC.onTCPExchangeClientRecvSWarSearchForward(this, p);
			}
			break;
		case Packet.eE2SPKTSWarAttack:
			{
				Packet.E2S.SWarAttack p = (Packet.E2S.SWarAttack)packet;
				managerRPC.onTCPExchangeClientRecvSWarAttack(this, p);
			}
			break;
		case Packet.eE2SPKTSWarAttackForward:
			{
				Packet.E2S.SWarAttackForward p = (Packet.E2S.SWarAttackForward)packet;
				managerRPC.onTCPExchangeClientRecvSWarAttackForward(this, p);
			}
			break;
		case Packet.eE2SPKTSWarRanks:
			{
				Packet.E2S.SWarRanks p = (Packet.E2S.SWarRanks)packet;
				managerRPC.onTCPExchangeClientRecvSWarRanks(this, p);
			}
			break;
		case Packet.eE2SPKTSWarRank:
			{
				Packet.E2S.SWarRank p = (Packet.E2S.SWarRank)packet;
				managerRPC.onTCPExchangeClientRecvSWarRank(this, p);
			}
			break;
		case Packet.eE2SPKTSWarTopRanks:
			{
				Packet.E2S.SWarTopRanks p = (Packet.E2S.SWarTopRanks)packet;
				managerRPC.onTCPExchangeClientRecvSWarTopRanks(this, p);
			}
			break;
		case Packet.eE2SPKTSWarGlobalRanks:
			{
				Packet.E2S.SWarGlobalRanks p = (Packet.E2S.SWarGlobalRanks)packet;
				managerRPC.onTCPExchangeClientRecvSWarGlobalRanks(this, p);
			}
			break;
		case Packet.eE2SPKTSWarVoteForward:
			{
				Packet.E2S.SWarVoteForward p = (Packet.E2S.SWarVoteForward)packet;
				managerRPC.onTCPExchangeClientRecvSWarVoteForward(this, p);
			}
			break;
		case Packet.eE2SPKTSWarRelease:
			{
				Packet.E2S.SWarRelease p = (Packet.E2S.SWarRelease)packet;
				managerRPC.onTCPExchangeClientRecvSWarRelease(this, p);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManager managerRPC;
}

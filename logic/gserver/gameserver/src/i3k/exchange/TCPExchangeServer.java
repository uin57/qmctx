// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.exchange;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.TCPServer;
import ket.kio.SimplePacket;

import i3k.rpc.ABaseDencoder;
import i3k.rpc.Packet;

public class TCPExchangeServer extends TCPServer<SimplePacket>
{

	public TCPExchangeServer(RPCManagerExchangeServer managerRPC)
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
			// server to exchange
			case Packet.eS2EPKTWhoAmI:
			case Packet.eS2EPKTSearchCity:
			case Packet.eS2EPKTSearchCityForward:
			case Packet.eS2EPKTAttackCityStart:
			case Packet.eS2EPKTAttackCityStartForward:
			case Packet.eS2EPKTAttackCityEnd:
			case Packet.eS2EPKTAttackCityEndForward:
			case Packet.eS2EPKTQueryCityOwner:
			case Packet.eS2EPKTQueryCityOwnerForward:
			case Packet.eS2EPKTNotifyCityOwnerChanged:
			case Packet.eS2EPKTChannelMessageReq:
			case Packet.eS2EPKTChannelMessageRes:
			case Packet.eS2EPKTDuelJoin:
			case Packet.eS2EPKTDuelSelectGeneral:
			case Packet.eS2EPKTDuelSelectGeneralForward:
			case Packet.eS2EPKTDuelStartAttack:
			case Packet.eS2EPKTDuelStartAttackForward:
			case Packet.eS2EPKTDuelFinishAttack:
			case Packet.eS2EPKTDuelRanks:
			case Packet.eS2EPKTDuelRank:
			case Packet.eS2EPKTDuelTopRanks:
			case Packet.eS2EPKTDuelOppoGiveup:
			case Packet.eS2EPKTDuelFinishAttackOnOppoDownLine:
			case Packet.eS2EPKTDuelCancelJoin:
			case Packet.eS2EPKTExpatriateDBRole:
			case Packet.eS2EPKTExpatriateDBRoleForward:
			case Packet.eS2EPKTExpiratBossRanks:
			case Packet.eS2EPKTExpiratBossRank:
			case Packet.eS2EPKTExpiratBossTopRanks:
			case Packet.eS2EPKTExpiratBossGlobalRanks:
			case Packet.eS2EPKTExpiratBossGlobalRanks2:
			case Packet.eS2EPKTHeroesBossSync:
			case Packet.eS2EPKTHeroesBossFinishAtt:
			case Packet.eS2EPKTHeroesBossInfo:
			case Packet.eS2EPKTSWarSearch:
			case Packet.eS2EPKTSWarSearchForward:
			case Packet.eS2EPKTSWarAttack:
			case Packet.eS2EPKTSWarAttackForward:
			case Packet.eS2EPKTSWarRanks:
			case Packet.eS2EPKTSWarRank:
			case Packet.eS2EPKTSWarTopRanks:
			case Packet.eS2EPKTSWarVote:
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
		managerRPC.onTCPExchangeServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPExchangeServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPExchangeServerClose(this, errcode);
	}

	@Override
	public void onPacketSessionOpen(int sessionid, NetAddress addrClient)
	{
		managerRPC.onTCPExchangeServerSessionOpen(this, sessionid, addrClient);
	}

	@Override
	public void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)
	{
		managerRPC.onTCPExchangeServerSessionClose(this, sessionid, errcode);
	}

	@Override
	public void onPacketSessionRecv(int sessionid, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// server to exchange
		case Packet.eS2EPKTWhoAmI:
			{
				Packet.S2E.WhoAmI p = (Packet.S2E.WhoAmI)packet;
				managerRPC.onTCPExchangeServerRecvWhoAmI(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSearchCity:
			{
				Packet.S2E.SearchCity p = (Packet.S2E.SearchCity)packet;
				managerRPC.onTCPExchangeServerRecvSearchCity(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSearchCityForward:
			{
				Packet.S2E.SearchCityForward p = (Packet.S2E.SearchCityForward)packet;
				managerRPC.onTCPExchangeServerRecvSearchCityForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTAttackCityStart:
			{
				Packet.S2E.AttackCityStart p = (Packet.S2E.AttackCityStart)packet;
				managerRPC.onTCPExchangeServerRecvAttackCityStart(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTAttackCityStartForward:
			{
				Packet.S2E.AttackCityStartForward p = (Packet.S2E.AttackCityStartForward)packet;
				managerRPC.onTCPExchangeServerRecvAttackCityStartForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTAttackCityEnd:
			{
				Packet.S2E.AttackCityEnd p = (Packet.S2E.AttackCityEnd)packet;
				managerRPC.onTCPExchangeServerRecvAttackCityEnd(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTAttackCityEndForward:
			{
				Packet.S2E.AttackCityEndForward p = (Packet.S2E.AttackCityEndForward)packet;
				managerRPC.onTCPExchangeServerRecvAttackCityEndForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTQueryCityOwner:
			{
				Packet.S2E.QueryCityOwner p = (Packet.S2E.QueryCityOwner)packet;
				managerRPC.onTCPExchangeServerRecvQueryCityOwner(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTQueryCityOwnerForward:
			{
				Packet.S2E.QueryCityOwnerForward p = (Packet.S2E.QueryCityOwnerForward)packet;
				managerRPC.onTCPExchangeServerRecvQueryCityOwnerForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTNotifyCityOwnerChanged:
			{
				Packet.S2E.NotifyCityOwnerChanged p = (Packet.S2E.NotifyCityOwnerChanged)packet;
				managerRPC.onTCPExchangeServerRecvNotifyCityOwnerChanged(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTChannelMessageReq:
			{
				Packet.S2E.ChannelMessageReq p = (Packet.S2E.ChannelMessageReq)packet;
				managerRPC.onTCPExchangeServerRecvChannelMessageReq(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTChannelMessageRes:
			{
				Packet.S2E.ChannelMessageRes p = (Packet.S2E.ChannelMessageRes)packet;
				managerRPC.onTCPExchangeServerRecvChannelMessageRes(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelJoin:
			{
				Packet.S2E.DuelJoin p = (Packet.S2E.DuelJoin)packet;
				managerRPC.onTCPExchangeServerRecvDuelJoin(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelSelectGeneral:
			{
				Packet.S2E.DuelSelectGeneral p = (Packet.S2E.DuelSelectGeneral)packet;
				managerRPC.onTCPExchangeServerRecvDuelSelectGeneral(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelSelectGeneralForward:
			{
				Packet.S2E.DuelSelectGeneralForward p = (Packet.S2E.DuelSelectGeneralForward)packet;
				managerRPC.onTCPExchangeServerRecvDuelSelectGeneralForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelStartAttack:
			{
				Packet.S2E.DuelStartAttack p = (Packet.S2E.DuelStartAttack)packet;
				managerRPC.onTCPExchangeServerRecvDuelStartAttack(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelStartAttackForward:
			{
				Packet.S2E.DuelStartAttackForward p = (Packet.S2E.DuelStartAttackForward)packet;
				managerRPC.onTCPExchangeServerRecvDuelStartAttackForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelFinishAttack:
			{
				Packet.S2E.DuelFinishAttack p = (Packet.S2E.DuelFinishAttack)packet;
				managerRPC.onTCPExchangeServerRecvDuelFinishAttack(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelRanks:
			{
				Packet.S2E.DuelRanks p = (Packet.S2E.DuelRanks)packet;
				managerRPC.onTCPExchangeServerRecvDuelRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelRank:
			{
				Packet.S2E.DuelRank p = (Packet.S2E.DuelRank)packet;
				managerRPC.onTCPExchangeServerRecvDuelRank(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelTopRanks:
			{
				Packet.S2E.DuelTopRanks p = (Packet.S2E.DuelTopRanks)packet;
				managerRPC.onTCPExchangeServerRecvDuelTopRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelOppoGiveup:
			{
				Packet.S2E.DuelOppoGiveup p = (Packet.S2E.DuelOppoGiveup)packet;
				managerRPC.onTCPExchangeServerRecvDuelOppoGiveup(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelFinishAttackOnOppoDownLine:
			{
				Packet.S2E.DuelFinishAttackOnOppoDownLine p = (Packet.S2E.DuelFinishAttackOnOppoDownLine)packet;
				managerRPC.onTCPExchangeServerRecvDuelFinishAttackOnOppoDownLine(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTDuelCancelJoin:
			{
				Packet.S2E.DuelCancelJoin p = (Packet.S2E.DuelCancelJoin)packet;
				managerRPC.onTCPExchangeServerRecvDuelCancelJoin(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpatriateDBRole:
			{
				Packet.S2E.ExpatriateDBRole p = (Packet.S2E.ExpatriateDBRole)packet;
				managerRPC.onTCPExchangeServerRecvExpatriateDBRole(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpatriateDBRoleForward:
			{
				Packet.S2E.ExpatriateDBRoleForward p = (Packet.S2E.ExpatriateDBRoleForward)packet;
				managerRPC.onTCPExchangeServerRecvExpatriateDBRoleForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpiratBossRanks:
			{
				Packet.S2E.ExpiratBossRanks p = (Packet.S2E.ExpiratBossRanks)packet;
				managerRPC.onTCPExchangeServerRecvExpiratBossRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpiratBossRank:
			{
				Packet.S2E.ExpiratBossRank p = (Packet.S2E.ExpiratBossRank)packet;
				managerRPC.onTCPExchangeServerRecvExpiratBossRank(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpiratBossTopRanks:
			{
				Packet.S2E.ExpiratBossTopRanks p = (Packet.S2E.ExpiratBossTopRanks)packet;
				managerRPC.onTCPExchangeServerRecvExpiratBossTopRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpiratBossGlobalRanks:
			{
				Packet.S2E.ExpiratBossGlobalRanks p = (Packet.S2E.ExpiratBossGlobalRanks)packet;
				managerRPC.onTCPExchangeServerRecvExpiratBossGlobalRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTExpiratBossGlobalRanks2:
			{
				Packet.S2E.ExpiratBossGlobalRanks2 p = (Packet.S2E.ExpiratBossGlobalRanks2)packet;
				managerRPC.onTCPExchangeServerRecvExpiratBossGlobalRanks2(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTHeroesBossSync:
			{
				Packet.S2E.HeroesBossSync p = (Packet.S2E.HeroesBossSync)packet;
				managerRPC.onTCPExchangeServerRecvHeroesBossSync(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTHeroesBossFinishAtt:
			{
				Packet.S2E.HeroesBossFinishAtt p = (Packet.S2E.HeroesBossFinishAtt)packet;
				managerRPC.onTCPExchangeServerRecvHeroesBossFinishAtt(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTHeroesBossInfo:
			{
				Packet.S2E.HeroesBossInfo p = (Packet.S2E.HeroesBossInfo)packet;
				managerRPC.onTCPExchangeServerRecvHeroesBossInfo(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarSearch:
			{
				Packet.S2E.SWarSearch p = (Packet.S2E.SWarSearch)packet;
				managerRPC.onTCPExchangeServerRecvSWarSearch(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarSearchForward:
			{
				Packet.S2E.SWarSearchForward p = (Packet.S2E.SWarSearchForward)packet;
				managerRPC.onTCPExchangeServerRecvSWarSearchForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarAttack:
			{
				Packet.S2E.SWarAttack p = (Packet.S2E.SWarAttack)packet;
				managerRPC.onTCPExchangeServerRecvSWarAttack(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarAttackForward:
			{
				Packet.S2E.SWarAttackForward p = (Packet.S2E.SWarAttackForward)packet;
				managerRPC.onTCPExchangeServerRecvSWarAttackForward(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarRanks:
			{
				Packet.S2E.SWarRanks p = (Packet.S2E.SWarRanks)packet;
				managerRPC.onTCPExchangeServerRecvSWarRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarRank:
			{
				Packet.S2E.SWarRank p = (Packet.S2E.SWarRank)packet;
				managerRPC.onTCPExchangeServerRecvSWarRank(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarTopRanks:
			{
				Packet.S2E.SWarTopRanks p = (Packet.S2E.SWarTopRanks)packet;
				managerRPC.onTCPExchangeServerRecvSWarTopRanks(this, p, sessionid);
			}
			break;
		case Packet.eS2EPKTSWarVote:
			{
				Packet.S2E.SWarVote p = (Packet.S2E.SWarVote)packet;
				managerRPC.onTCPExchangeServerRecvSWarVote(this, p, sessionid);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private RPCManagerExchangeServer managerRPC;
}

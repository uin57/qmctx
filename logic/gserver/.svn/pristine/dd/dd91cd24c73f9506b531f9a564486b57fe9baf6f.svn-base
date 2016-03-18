// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.rpc;

import ket.kio.SimplePacket;
import ket.kio.SimpleDencoder;

public abstract class ABaseDencoder extends SimpleDencoder
{

	@Override
	public SimplePacket createPacket(int ptype)
	{
		SimplePacket packet = null;
		switch( ptype )
		{
		// server to client
		case Packet.eS2CPKTServerChallenge:
			packet = new Packet.S2C.ServerChallenge();
			break;
		case Packet.eS2CPKTServerResponse:
			packet = new Packet.S2C.ServerResponse();
			break;
		case Packet.eS2CPKTLuaChannel:
			packet = new Packet.S2C.LuaChannel();
			break;
		case Packet.eS2CPKTLuaChannel2:
			packet = new Packet.S2C.LuaChannel2();
			break;
		case Packet.eS2CPKTCmnRPCResponse:
			packet = new Packet.S2C.CmnRPCResponse();
			break;
		case Packet.eS2CPKTAntiData:
			packet = new Packet.S2C.AntiData();
			break;

		// client to server
		case Packet.eC2SPKTClientResponse:
			packet = new Packet.C2S.ClientResponse();
			break;
		case Packet.eC2SPKTLuaChannel:
			packet = new Packet.C2S.LuaChannel();
			break;
		case Packet.eC2SPKTLuaChannel2:
			packet = new Packet.C2S.LuaChannel2();
			break;
		case Packet.eC2SPKTDataSync:
			packet = new Packet.C2S.DataSync();
			break;
		case Packet.eC2SPKTAntiData:
			packet = new Packet.C2S.AntiData();
			break;

		// global to client
		case Packet.eG2CPKTLuaChannel:
			packet = new Packet.G2C.LuaChannel();
			break;
		case Packet.eG2CPKTLuaChannel2:
			packet = new Packet.G2C.LuaChannel2();
			break;

		// client to global
		case Packet.eC2GPKTLuaChannel:
			packet = new Packet.C2G.LuaChannel();
			break;
		case Packet.eC2GPKTLuaChannel2:
			packet = new Packet.C2G.LuaChannel2();
			break;

		// manager to server
		case Packet.eM2SPKTShutdown:
			packet = new Packet.M2S.Shutdown();
			break;
		case Packet.eM2SPKTDataQuery:
			packet = new Packet.M2S.DataQuery();
			break;
		case Packet.eM2SPKTAnnounce:
			packet = new Packet.M2S.Announce();
			break;
		case Packet.eM2SPKTDataModify:
			packet = new Packet.M2S.DataModify();
			break;
		case Packet.eM2SPKTSysMessage:
			packet = new Packet.M2S.SysMessage();
			break;
		case Packet.eM2SPKTRoleData:
			packet = new Packet.M2S.RoleData();
			break;
		case Packet.eM2SPKTWorldGift:
			packet = new Packet.M2S.WorldGift();
			break;

		// server to manager
		case Packet.eS2MPKTShutdown:
			packet = new Packet.S2M.Shutdown();
			break;
		case Packet.eS2MPKTOnlineUser:
			packet = new Packet.S2M.OnlineUser();
			break;
		case Packet.eS2MPKTQueryRoleBrief:
			packet = new Packet.S2M.QueryRoleBrief();
			break;
		case Packet.eS2MPKTAnnounce:
			packet = new Packet.S2M.Announce();
			break;
		case Packet.eS2MPKTDataModify:
			packet = new Packet.S2M.DataModify();
			break;
		case Packet.eS2MPKTSysMessage:
			packet = new Packet.S2M.SysMessage();
			break;
		case Packet.eS2MPKTDataQuery:
			packet = new Packet.S2M.DataQuery();
			break;
		case Packet.eS2MPKTRoleData:
			packet = new Packet.S2M.RoleData();
			break;
		case Packet.eS2MPKTWorldGift:
			packet = new Packet.S2M.WorldGift();
			break;

		// exchange to server
		case Packet.eE2SPKTStateAnnounce:
			packet = new Packet.E2S.StateAnnounce();
			break;
		case Packet.eE2SPKTSearchCity:
			packet = new Packet.E2S.SearchCity();
			break;
		case Packet.eE2SPKTSearchCityForward:
			packet = new Packet.E2S.SearchCityForward();
			break;
		case Packet.eE2SPKTAttackCityStart:
			packet = new Packet.E2S.AttackCityStart();
			break;
		case Packet.eE2SPKTAttackCityStartForward:
			packet = new Packet.E2S.AttackCityStartForward();
			break;
		case Packet.eE2SPKTAttackCityEnd:
			packet = new Packet.E2S.AttackCityEnd();
			break;
		case Packet.eE2SPKTAttackCityEndForward:
			packet = new Packet.E2S.AttackCityEndForward();
			break;
		case Packet.eE2SPKTQueryCityOwner:
			packet = new Packet.E2S.QueryCityOwner();
			break;
		case Packet.eE2SPKTQueryCityOwnerForward:
			packet = new Packet.E2S.QueryCityOwnerForward();
			break;
		case Packet.eE2SPKTNotifyCityOwnerChangedForward:
			packet = new Packet.E2S.NotifyCityOwnerChangedForward();
			break;
		case Packet.eE2SPKTChannelMessageReq:
			packet = new Packet.E2S.ChannelMessageReq();
			break;
		case Packet.eE2SPKTChannelMessageRes:
			packet = new Packet.E2S.ChannelMessageRes();
			break;
		case Packet.eE2SPKTDuelJoin:
			packet = new Packet.E2S.DuelJoin();
			break;
		case Packet.eE2SPKTDuelSelectGeneral:
			packet = new Packet.E2S.DuelSelectGeneral();
			break;
		case Packet.eE2SPKTDuelSelectGeneralForward:
			packet = new Packet.E2S.DuelSelectGeneralForward();
			break;
		case Packet.eE2SPKTDuelStartAttack:
			packet = new Packet.E2S.DuelStartAttack();
			break;
		case Packet.eE2SPKTDuelStartAttackForward:
			packet = new Packet.E2S.DuelStartAttackForward();
			break;
		case Packet.eE2SPKTDuelFinishAttack:
			packet = new Packet.E2S.DuelFinishAttack();
			break;
		case Packet.eE2SPKTDuelRanks:
			packet = new Packet.E2S.DuelRanks();
			break;
		case Packet.eE2SPKTDuelRank:
			packet = new Packet.E2S.DuelRank();
			break;
		case Packet.eE2SPKTDuelTopRanks:
			packet = new Packet.E2S.DuelTopRanks();
			break;
		case Packet.eE2SPKTDuelOppoGiveup:
			packet = new Packet.E2S.DuelOppoGiveup();
			break;
		case Packet.eE2SPKTDuelOppoGiveupForward:
			packet = new Packet.E2S.DuelOppoGiveupForward();
			break;
		case Packet.eE2SPKTDuelGlobalRanks:
			packet = new Packet.E2S.DuelGlobalRanks();
			break;
		case Packet.eE2SPKTExpatriateDBRole:
			packet = new Packet.E2S.ExpatriateDBRole();
			break;
		case Packet.eE2SPKTExpatriateDBRoleForward:
			packet = new Packet.E2S.ExpatriateDBRoleForward();
			break;
		case Packet.eE2SPKTExpiratBossRanks:
			packet = new Packet.E2S.ExpiratBossRanks();
			break;
		case Packet.eE2SPKTExpiratBossRank:
			packet = new Packet.E2S.ExpiratBossRank();
			break;
		case Packet.eE2SPKTExpiratBossTopRanks:
			packet = new Packet.E2S.ExpiratBossTopRanks();
			break;
		case Packet.eE2SPKTExpiratBossGlobalRanks:
			packet = new Packet.E2S.ExpiratBossGlobalRanks();
			break;
		case Packet.eE2SPKTExpiratBossGlobalRanks2:
			packet = new Packet.E2S.ExpiratBossGlobalRanks2();
			break;
		case Packet.eE2SPKTHeroesBossSync:
			packet = new Packet.E2S.HeroesBossSync();
			break;
		case Packet.eE2SPKTHeroesBossFinishAtt:
			packet = new Packet.E2S.HeroesBossFinishAtt();
			break;
		case Packet.eE2SPKTHeroesBossInfo:
			packet = new Packet.E2S.HeroesBossInfo();
			break;
		case Packet.eE2SPKTSWarSearch:
			packet = new Packet.E2S.SWarSearch();
			break;
		case Packet.eE2SPKTSWarSearchForward:
			packet = new Packet.E2S.SWarSearchForward();
			break;
		case Packet.eE2SPKTSWarAttack:
			packet = new Packet.E2S.SWarAttack();
			break;
		case Packet.eE2SPKTSWarAttackForward:
			packet = new Packet.E2S.SWarAttackForward();
			break;
		case Packet.eE2SPKTSWarRanks:
			packet = new Packet.E2S.SWarRanks();
			break;
		case Packet.eE2SPKTSWarRank:
			packet = new Packet.E2S.SWarRank();
			break;
		case Packet.eE2SPKTSWarTopRanks:
			packet = new Packet.E2S.SWarTopRanks();
			break;
		case Packet.eE2SPKTSWarGlobalRanks:
			packet = new Packet.E2S.SWarGlobalRanks();
			break;
		case Packet.eE2SPKTSWarVoteForward:
			packet = new Packet.E2S.SWarVoteForward();
			break;
		case Packet.eE2SPKTSWarRelease:
			packet = new Packet.E2S.SWarRelease();
			break;
		case Packet.eE2SPKTDuelFinishAttackOnOppoDownLine:
			packet = new Packet.E2S.DuelFinishAttackOnOppoDownLine();
			break;
		case Packet.eE2SPKTDuelFinishAttackOnOppoDownLineForward:
			packet = new Packet.E2S.DuelFinishAttackOnOppoDownLineForward();
			break;

		// server to exchange
		case Packet.eS2EPKTWhoAmI:
			packet = new Packet.S2E.WhoAmI();
			break;
		case Packet.eS2EPKTSearchCity:
			packet = new Packet.S2E.SearchCity();
			break;
		case Packet.eS2EPKTSearchCityForward:
			packet = new Packet.S2E.SearchCityForward();
			break;
		case Packet.eS2EPKTAttackCityStart:
			packet = new Packet.S2E.AttackCityStart();
			break;
		case Packet.eS2EPKTAttackCityStartForward:
			packet = new Packet.S2E.AttackCityStartForward();
			break;
		case Packet.eS2EPKTAttackCityEnd:
			packet = new Packet.S2E.AttackCityEnd();
			break;
		case Packet.eS2EPKTAttackCityEndForward:
			packet = new Packet.S2E.AttackCityEndForward();
			break;
		case Packet.eS2EPKTQueryCityOwner:
			packet = new Packet.S2E.QueryCityOwner();
			break;
		case Packet.eS2EPKTQueryCityOwnerForward:
			packet = new Packet.S2E.QueryCityOwnerForward();
			break;
		case Packet.eS2EPKTNotifyCityOwnerChanged:
			packet = new Packet.S2E.NotifyCityOwnerChanged();
			break;
		case Packet.eS2EPKTChannelMessageReq:
			packet = new Packet.S2E.ChannelMessageReq();
			break;
		case Packet.eS2EPKTChannelMessageRes:
			packet = new Packet.S2E.ChannelMessageRes();
			break;
		case Packet.eS2EPKTDuelJoin:
			packet = new Packet.S2E.DuelJoin();
			break;
		case Packet.eS2EPKTDuelSelectGeneral:
			packet = new Packet.S2E.DuelSelectGeneral();
			break;
		case Packet.eS2EPKTDuelSelectGeneralForward:
			packet = new Packet.S2E.DuelSelectGeneralForward();
			break;
		case Packet.eS2EPKTDuelStartAttack:
			packet = new Packet.S2E.DuelStartAttack();
			break;
		case Packet.eS2EPKTDuelStartAttackForward:
			packet = new Packet.S2E.DuelStartAttackForward();
			break;
		case Packet.eS2EPKTDuelFinishAttack:
			packet = new Packet.S2E.DuelFinishAttack();
			break;
		case Packet.eS2EPKTDuelRanks:
			packet = new Packet.S2E.DuelRanks();
			break;
		case Packet.eS2EPKTDuelRank:
			packet = new Packet.S2E.DuelRank();
			break;
		case Packet.eS2EPKTDuelTopRanks:
			packet = new Packet.S2E.DuelTopRanks();
			break;
		case Packet.eS2EPKTDuelOppoGiveup:
			packet = new Packet.S2E.DuelOppoGiveup();
			break;
		case Packet.eS2EPKTDuelCancelJoin:
			packet = new Packet.S2E.DuelCancelJoin();
			break;
		case Packet.eS2EPKTExpatriateDBRole:
			packet = new Packet.S2E.ExpatriateDBRole();
			break;
		case Packet.eS2EPKTExpatriateDBRoleForward:
			packet = new Packet.S2E.ExpatriateDBRoleForward();
			break;
		case Packet.eS2EPKTExpiratBossRanks:
			packet = new Packet.S2E.ExpiratBossRanks();
			break;
		case Packet.eS2EPKTExpiratBossRank:
			packet = new Packet.S2E.ExpiratBossRank();
			break;
		case Packet.eS2EPKTExpiratBossTopRanks:
			packet = new Packet.S2E.ExpiratBossTopRanks();
			break;
		case Packet.eS2EPKTExpiratBossGlobalRanks:
			packet = new Packet.S2E.ExpiratBossGlobalRanks();
			break;
		case Packet.eS2EPKTExpiratBossGlobalRanks2:
			packet = new Packet.S2E.ExpiratBossGlobalRanks2();
			break;
		case Packet.eS2EPKTHeroesBossSync:
			packet = new Packet.S2E.HeroesBossSync();
			break;
		case Packet.eS2EPKTHeroesBossFinishAtt:
			packet = new Packet.S2E.HeroesBossFinishAtt();
			break;
		case Packet.eS2EPKTHeroesBossInfo:
			packet = new Packet.S2E.HeroesBossInfo();
			break;
		case Packet.eS2EPKTSWarSearch:
			packet = new Packet.S2E.SWarSearch();
			break;
		case Packet.eS2EPKTSWarSearchForward:
			packet = new Packet.S2E.SWarSearchForward();
			break;
		case Packet.eS2EPKTSWarAttack:
			packet = new Packet.S2E.SWarAttack();
			break;
		case Packet.eS2EPKTSWarAttackForward:
			packet = new Packet.S2E.SWarAttackForward();
			break;
		case Packet.eS2EPKTSWarRanks:
			packet = new Packet.S2E.SWarRanks();
			break;
		case Packet.eS2EPKTSWarRank:
			packet = new Packet.S2E.SWarRank();
			break;
		case Packet.eS2EPKTSWarTopRanks:
			packet = new Packet.S2E.SWarTopRanks();
			break;
		case Packet.eS2EPKTSWarVote:
			packet = new Packet.S2E.SWarVote();
			break;
		case Packet.eS2EPKTDuelFinishAttackOnOppoDownLine:
			packet = new Packet.S2E.DuelFinishAttackOnOppoDownLine();
			break;

		// friend to server
		case Packet.eF2SPKTStateAnnounce:
			packet = new Packet.F2S.StateAnnounce();
			break;
		case Packet.eF2SPKTSearchFriends:
			packet = new Packet.F2S.SearchFriends();
			break;
		case Packet.eF2SPKTQueryFriendProp:
			packet = new Packet.F2S.QueryFriendProp();
			break;
		case Packet.eF2SPKTSendMessageToFriend:
			packet = new Packet.F2S.SendMessageToFriend();
			break;
		case Packet.eF2SPKTCreateRole:
			packet = new Packet.F2S.CreateRole();
			break;

		// server to friend
		case Packet.eS2FPKTWhoAmI:
			packet = new Packet.S2F.WhoAmI();
			break;
		case Packet.eS2FPKTCreateRole:
			packet = new Packet.S2F.CreateRole();
			break;
		case Packet.eS2FPKTSearchFriends:
			packet = new Packet.S2F.SearchFriends();
			break;
		case Packet.eS2FPKTQueryFriendProp:
			packet = new Packet.S2F.QueryFriendProp();
			break;
		case Packet.eS2FPKTUpdateFriendProp:
			packet = new Packet.S2F.UpdateFriendProp();
			break;
		case Packet.eS2FPKTSendMessageToFriend:
			packet = new Packet.S2F.SendMessageToFriend();
			break;

		default:
			break;
		}
		return packet;
	}

	@Override
	public boolean doCheckPacketType(int ptype)
	{
		return true;
	}
}

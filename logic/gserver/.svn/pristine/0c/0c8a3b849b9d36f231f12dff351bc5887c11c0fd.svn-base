// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.exchange;

import i3k.SBean;
import i3k.SBean.ExpatriateTransRoleReq;
import i3k.SBean.ExpatriateTransRoleRes;
import i3k.rpc.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ket.kio.NetAddress;
import ket.kio.NetManager;
import ket.kio.SimplePacket;

public class RPCManagerExchangeServer
{
		
	class ServerTable
	{
		public void onSessionAnnounce(int sid, byte platID, int serverID)
		{
			int oldSID = -1;
			synchronized( this )
			{
				// TODO
				Integer rsid = map.get(serverID);
				if( rsid != null )
				{
					int oldsid = rsid.intValue();
					if( oldsid == sid )
					{
						smap.put(sid, serverID);
					}
					else
					{
						map.put(serverID, sid);
						smap.put(sid, serverID);
						oldSID = oldsid;
					}
				}
				else
				{
					map.put(serverID, sid);
					smap.put(sid, serverID);
					slist.add(serverID);
				}
			}
			if( oldSID > 0 )
				tes.closeSession(oldSID);
		}
		
		public synchronized void onSessionClose(int sid)
		{
			// TODO
			Integer serverID = smap.remove(sid);
			if( serverID != null )
			{
				Integer rsid = map.get(serverID);
				if( rsid.intValue() == sid )
					map.remove(serverID);
				slist.remove(serverID);
			}
		}
		
		public synchronized int getSessionIDByServerID(int serverID)
		{
			Integer r = map.get(serverID);
			return r == null ? -1 : r.intValue();
		}
		
		public synchronized int getRandomServer()
		{
			int index = random.nextInt(slist.size());
			return slist.get(index);
		}
		
		public synchronized List<Integer> getAllServerIds()
		{
			List<Integer> sids = new ArrayList<Integer>();
			for (int sid : map.keySet())
				sids.add(sid);
			
			return sids;
		}
		
		// server id 2 sessionid
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();		
		// sessionid id 2 server id
		Map<Integer, Integer> smap = new HashMap<Integer, Integer>();
		//server id list
		List<Integer> slist = new ArrayList<Integer>();
		
		Random random = new Random();
	}
	
	public RPCManagerExchangeServer(ExchangeServer es)
	{
		this.es = es;
	}

	public NetManager getNetManager()
	{
		return managerNet;
	}
	
	public void onTimer()
	{
		// TODO managerNet.checkIdleConnections();
	}
	
	public void start()
	{
		managerNet.start();
		tes.setListenAddr(es.getConfig().addrListen, ket.kio.BindPolicy.eReuseTimewait);
		tes.setListenBacklog(128);
		tes.open();
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public void sendSearchCityReq(int serverID, SBean.SearchCityReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.SearchCityForward(req));
	}
	
	public void sendSearchCityRes(int serverID, SBean.SearchCityRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.SearchCity(res));
	}
	
	public void sendAttackCityStartReq(int serverID, SBean.AttackCityStartReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.AttackCityStartForward(req));
	}
	
	public void sendAttackCityStartRes(int serverID, SBean.AttackCityStartRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.AttackCityStart(res));
	}
	
	public void sendAttackCityEndReq(int serverID, SBean.AttackCityEndReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.AttackCityEndForward(req));
	}
	
	public void sendAttackCityEndRes(int serverID, SBean.AttackCityEndRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.AttackCityEnd(res));
	}
	
	public void sendQueryCityOwnerReq(int serverID, SBean.QueryCityOwnerReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.QueryCityOwnerForward(req));
	}
	
	public void sendQueryCityOwnerRes(int serverID, SBean.QueryCityOwnerRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.QueryCityOwner(res));
	}
	
	public void sendNotifyCityOwnerChangeReq(int serverID, SBean.NotifyCityOwnerChangedReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.NotifyCityOwnerChangedForward(req));
	}

	public void sendDuelJoinRes(int serverID, SBean.DuelJoinRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelJoin(res));
	}
	
	public void sendDuelSelectGeneralRes(int serverID, SBean.DuelSelectGeneralRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelSelectGeneral(res));
	}
	
	public void sendDuelSelectGeneralForwardReq(int serverID, SBean.DuelSelectGeneralReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelSelectGeneralForward(req));
	}
	
	public void sendDuelStartAttackRes(int serverID, SBean.DuelStartAttackRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelStartAttack(res));
	}
	
	public void sendDuelStartAttackForwardReq(int serverID, SBean.DuelStartAttackReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelStartAttackForward(req));
	}
	
	public void sendDuelFinishAttackRes(int serverID, SBean.DuelFinishAttackRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelFinishAttack(res));
	}
	
	public void sendDuelOppoGiveupRes(int serverID, SBean.DuelOppoGiveupRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelOppoGiveup(res));
	}
	
	public void sendDuelFinishAttackOnOppoDownLineRes(int serverID, SBean.DuelFinishAttackOnOppoDownLineRes res)
    {
        sendPacketToServer(serverID, new Packet.E2S.DuelFinishAttackOnOppoDownLine(res));
    }
	
	public void sendDuelOppoGiveupForward(int serverID, SBean.DuelOppoGiveupRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelOppoGiveupForward(res));
	}
	
	public void sendDuelFinishAttackOnOppoDownLineForward(int serverID, SBean.DuelFinishAttackOnOppoDownLineRes res)
    {
        sendPacketToServer(serverID, new Packet.E2S.DuelFinishAttackOnOppoDownLineForward(res));
    }
	
	public void sendDuelRankRes(int serverID, SBean.DuelRankRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelRank(res));
	}
	
	public void sendDuelTopRanksRes(int serverID, SBean.DuelTopRanksRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.DuelTopRanks(res));
	}
	
	public void sendSWarSearchRes(int serverID, SBean.SWarSearchRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarSearch(res));
	}
	
	public void sendSWarSearchForwardReq(int serverID, SBean.SWarSearchReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarSearchForward(req));
	}
	
	public void sendSWarAttackRes(int serverID, SBean.SWarAttackRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarAttack(res));
	}
	
	public void sendSWarAttackForwardReq(int serverID, SBean.SWarAttackReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarAttackForward(req));
	}
	
	public void sendSWarVoteReq(int serverID, SBean.SWarVoteReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarVoteForward(req));
	}
	
	public void sendSWarReleaseReq(int serverID, SBean.SWarReleaseReq req)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarRelease(req));
	}
	
	public void sendSWarRankRes(int serverID, SBean.SWarRankRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarRank(res));
	}
	
	public void sendSWarTopRanksRes(int serverID, SBean.SWarTopRanksRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.SWarTopRanks(res));
	}
	
	public void sendSWarRanksReq(int serverId)
	{
		sendPacketToServer(serverId, new Packet.E2S.SWarRanks());
	}
	
	public void sendSWarRanksReqToAllServers()
	{
		List<Integer> sids = table.getAllServerIds();
		for (int sid : sids)
			sendPacketToServer(sid, new Packet.E2S.SWarRanks());
	}
	
	public void sendSWarGlobalRanksRes(int serverId, SBean.SWarGlobalRanksRes res)
	{
		sendPacketToServer(serverId, new Packet.E2S.SWarGlobalRanks(res));
	}
	
	public void sendExpiratBossRankRes(int serverID, SBean.ExpiratBossRankRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.ExpiratBossRank(res));
	}
	
	public void sendSyncHeroesRes(int serverID, SBean.HeroesBossSyncRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.HeroesBossSync(res));
	}
	
	public void sendSyncHeroesInfoRes(int serverID, SBean.HeroesBossInfoRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.HeroesBossInfo(res));
	}
	
	public void sendSyncHeroesRes(int serverID, SBean.HeroesBossFinishAttRes res)
	{
		sendPacketToServer(serverID, new Packet.E2S.HeroesBossFinishAtt(res));
	}
	
	public void sendDuelRanksReq(int serverId)
	{
		sendPacketToServer(serverId, new Packet.E2S.DuelRanks());
	}
	
	public void sendDuelRanksReqToAllServers()
	{
		List<Integer> sids = table.getAllServerIds();
		for (int sid : sids)
			sendPacketToServer(sid, new Packet.E2S.DuelRanks());
	}
	
	public void sendExpiratBossRanksReqToAllServers()
	{
		List<Integer> sids = table.getAllServerIds();
		for (int sid : sids)
			sendPacketToServer(sid, new Packet.E2S.ExpiratBossRanks());
	}
	
	public void sendDuelGlobalRanksRes(int serverId, SBean.DuelGlobalRanksRes res)
	{
		sendPacketToServer(serverId, new Packet.E2S.DuelGlobalRanks(res));
	}
	
	public void sendDuelGlobalRanksRes(int serverId,short combatId, SBean.DuelGlobalRanksRes res)
	{
		sendPacketToServer(serverId, new Packet.E2S.DuelGlobalRanks(res));
	}
	public void sendExpiratBossGlobalRanksRes(int serverId,SBean.ExpiratBossTopRanksRes res)
	{
		List<Integer> sids = table.getAllServerIds();
		for (int sid : sids){
			sendPacketToServer(sid, new Packet.E2S.ExpiratBossTopRanks(res));	
		}		
	}
	
	public void sendExpiratBossGlobalRanksRes(int serverId,SBean.ExpiratBossGlobalRanksRes res)
	{
		sendPacketToServer(serverId, new Packet.E2S.ExpiratBossGlobalRanks(res));
	}
	
	public void sendExpiratBossGlobalRanksRes2(int serverId,SBean.ExpiratBossGlobalRanksRes2 res)
	{
		sendPacketToServer(serverId, new Packet.E2S.ExpiratBossGlobalRanks2(res));
	}
    /*
	public void sendQueryUserHasRoleReq(int targs, QueryHasRoleReq req)
        {
	        sendPacketToServer(targs, new Packet.E2S.QueryUserHasRolesForward(req));
        }
	
	public void sendQueryUserHasRoleRes(int targs, QueryHasRoleRes res)
        {
                sendPacketToServer(targs, new Packet.E2S.QueryUserHasRoles(res));
        }
        */
    
	public void sendTransDBRoleReq(int targs, ExpatriateTransRoleReq req)
        {
                sendPacketToServer(targs, new Packet.E2S.ExpatriateDBRoleForward(req));
        }
    
	public void sendTransDBRoleForwardRes(int targs, ExpatriateTransRoleRes res)
        {
	        sendPacketToServer(targs, new Packet.E2S.ExpatriateDBRole(res));
        }
    
	//// begin handlers.
	public void onTCPExchangeServerOpen(TCPExchangeServer peer)
	{
		// TODO
	}

	public void onTCPExchangeServerOpenFailed(TCPExchangeServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPExchangeServerClose(TCPExchangeServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPExchangeServerSessionOpen(TCPExchangeServer peer, int sessionid, NetAddress addrClient)
	{
		es.getLogger().info("session open, sid=" + sessionid + ", addr=" + addrClient.toString());
		peer.sendPacket(sessionid, new Packet.E2S.StateAnnounce(es.getConfig().id));
	}

	public void onTCPExchangeServerSessionClose(TCPExchangeServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		table.onSessionClose(sessionid);
	}

	public void onTCPExchangeServerRecvWhoAmI(TCPExchangeServer peer, Packet.S2E.WhoAmI packet, int sessionid)
	{
		es.getLogger().info("client announce sid =" + sessionid + ", platID=" + packet.getPlatID() + ", gsID=" + packet.getServerID());
		table.onSessionAnnounce(sessionid, packet.getPlatID(), packet.getServerID());
		es.getDuelManager().onServerArrived(packet.getServerID());
	}

	public void onTCPExchangeServerRecvSearchCity(TCPExchangeServer peer, Packet.S2E.SearchCity packet, int sessionid)
	{
		int serverID = table.getRandomServer();
		es.getDataManager().searchCity(serverID, packet.getReq());
	}

	public void onTCPExchangeServerRecvSearchCityForward(TCPExchangeServer peer, Packet.S2E.SearchCityForward packet, int sessionid)
	{
		es.getDataManager().sendSearchCityResult(packet.getRes());
	}

	public void onTCPExchangeServerRecvAttackCityStart(TCPExchangeServer peer, Packet.S2E.AttackCityStart packet, int sessionid)
	{
		es.getDataManager().attackCityStart(packet.getReq());
	}

	public void onTCPExchangeServerRecvAttackCityStartForward(TCPExchangeServer peer, Packet.S2E.AttackCityStartForward packet, int sessionid)
	{
		es.getDataManager().sendAttackCityStartResult(packet.getRes());
	}

	public void onTCPExchangeServerRecvAttackCityEnd(TCPExchangeServer peer, Packet.S2E.AttackCityEnd packet, int sessionid)
	{
		es.getDataManager().attackCityEnd(packet.getReq());
	}

	public void onTCPExchangeServerRecvAttackCityEndForward(TCPExchangeServer peer, Packet.S2E.AttackCityEndForward packet, int sessionid)
	{
		es.getDataManager().sendAttackCityEndResult(packet.getRes());
	}

	public void onTCPExchangeServerRecvQueryCityOwner(TCPExchangeServer peer, Packet.S2E.QueryCityOwner packet, int sessionid)
	{
		es.getDataManager().queryCityOwner(packet.getReq());
	}

	public void onTCPExchangeServerRecvQueryCityOwnerForward(TCPExchangeServer peer, Packet.S2E.QueryCityOwnerForward packet, int sessionid)
	{
		es.getDataManager().sendQueryCityOwnerResult(packet.getRes());
	}

	public void onTCPExchangeServerRecvNotifyCityOwnerChanged(TCPExchangeServer peer, Packet.S2E.NotifyCityOwnerChanged packet, int sessionid)
	{
		es.getDataManager().notifyCityOwnerChanged(packet.getReq());
	}

	public void onTCPExchangeServerRecvChannelMessageReq(TCPExchangeServer peer, Packet.S2E.ChannelMessageReq packet, int sessionid)
	{
		SBean.ExchangeChannelMessage data = packet.getData();
		sendPacketToServer(data.dstgs, new Packet.E2S.ChannelMessageReq(data));
	}

	public void onTCPExchangeServerRecvChannelMessageRes(TCPExchangeServer peer, Packet.S2E.ChannelMessageRes packet, int sessionid)
	{
		SBean.ExchangeChannelMessage data = packet.getData();
		sendPacketToServer(data.dstgs, new Packet.E2S.ChannelMessageRes(data));
	}

	public void onTCPExchangeServerRecvDuelJoin(TCPExchangeServer peer, Packet.S2E.DuelJoin packet, int sessionid)
	{
		es.getDuelManager().join(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelSelectGeneral(TCPExchangeServer peer, Packet.S2E.DuelSelectGeneral packet, int sessionid)
	{
		es.getDuelManager().selectGeneral(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelSelectGeneralForward(TCPExchangeServer peer, Packet.S2E.DuelSelectGeneralForward packet, int sessionid)
	{
		es.getDuelManager().selectGeneralForward(packet.getRes());
	}

	public void onTCPExchangeServerRecvDuelStartAttack(TCPExchangeServer peer, Packet.S2E.DuelStartAttack packet, int sessionid)
	{
		es.getDuelManager().startAttack(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelStartAttackForward(TCPExchangeServer peer, Packet.S2E.DuelStartAttackForward packet, int sessionid)
	{
		es.getDuelManager().startAttackForward(packet.getRes());
	}

	public void onTCPExchangeServerRecvDuelFinishAttack(TCPExchangeServer peer, Packet.S2E.DuelFinishAttack packet, int sessionid)
	{
		es.getDuelManager().finishAttack(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelRanks(TCPExchangeServer peer, Packet.S2E.DuelRanks packet, int sessionid)
	{
		es.getDuelManager().ranks(packet.getRes());
	}

	public void onTCPExchangeServerRecvDuelRank(TCPExchangeServer peer, Packet.S2E.DuelRank packet, int sessionid)
	{
		es.getDuelManager().rank(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelTopRanks(TCPExchangeServer peer, Packet.S2E.DuelTopRanks packet, int sessionid)
	{
		es.getDuelManager().topRanks(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelOppoGiveup(TCPExchangeServer peer, Packet.S2E.DuelOppoGiveup packet, int sessionid)
	{
		es.getDuelManager().oppoGiveup(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelFinishAttackOnOppoDownLine(TCPExchangeServer peer, Packet.S2E.DuelFinishAttackOnOppoDownLine packet, int sessionid)
	{
		es.getDuelManager().finishAttackOnOppoDownLine(packet.getReq());
	}

	public void onTCPExchangeServerRecvDuelCancelJoin(TCPExchangeServer peer, Packet.S2E.DuelCancelJoin packet, int sessionid)
	{
		es.getDuelManager().cancelJoin(packet.getReq());
	}

	public void onTCPExchangeServerRecvExpatriateDBRole(TCPExchangeServer peer, Packet.S2E.ExpatriateDBRole packet, int sessionid)
	{
		es.getDataManager().forwardTransDBRole(packet.getReq());
	}

	public void onTCPExchangeServerRecvExpatriateDBRoleForward(TCPExchangeServer peer, Packet.S2E.ExpatriateDBRoleForward packet, int sessionid)
	{
		es.getDataManager().forwardTransDBRoleForward(packet.getRes());
	}

//	public void onTCPExchangeServerRecvExpatriateHeheForward(TCPExchangeServer peer, Packet.S2E.ExpatriateHeheForward packet, int sessionid)
//	{
//		// TODO
//	}
//
////	public void onTCPExchangeServerRecvExpatriateDBRole(TCPExchangeServer peer, Packet.S2E.ExpatriateDBRole packet, int sessionid)
////	{
////		// TODO
////	}
////
////	public void onTCPExchangeServerRecvExpatriateDBRoleForward(TCPExchangeServer peer, Packet.S2E.ExpatriateDBRoleForward packet, int sessionid)
////	{
////		// TODO
////	}
////
//////	public void onTCPExchangeServerRecvQueryUserHasRolesForward(TCPExchangeServer peer, Packet.S2E.QueryUserHasRolesForward packet, int sessionid)
//////	{
//////		// TODO
//////	}
//////
//////	public void onTCPExchangeServerRecvQueryUserHasRoles(TCPExchangeServer peer, Packet.S2E.QueryUserHasRoles packet, int sessionid)
//////	{
//////	        es.getDataManager().queryUserHasRoles(packet.getReq());
//////	}
//////	
//////        public void onTCPExchangeServerRecvQueryUserHasRolesForward(TCPExchangeServer peer, Packet.S2E.QueryUserHasRolesForward packet, int sessionid)
//////	{
//////		es.getDataManager().queryUserHasRolesForward(packet.getRes());
//////	}
//////	
//////
//	public void onTCPExchangeServerRecvExpatriateHehe(TCPExchangeServer peer, Packet.S2E.ExpatriateHehe packet, int sessionid)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeServerRecvQueryUserHasRolesForward(TCPExchangeServer peer, Packet.S2E.QueryUserHasRolesForward packet, int sessionid)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeServerRecvQueryUserHasRoles(TCPExchangeServer peer, Packet.S2E.QueryUserHasRoles packet, int sessionid)
//	{
//		// TODO
//	}
//
//	public int getTCPExchangeServerRecvBufferSize()
//	{
//		// TODO zane
//		return 32768 * 2;
//	}
//
	public void onTCPExchangeServerRecvExpiratBossRanks(TCPExchangeServer peer, Packet.S2E.ExpiratBossRanks packet, int sessionid)
	{
		es.getExpiratBossManager().ranks(packet.getRes());
	}

	public void onTCPExchangeServerRecvExpiratBossRank(TCPExchangeServer peer, Packet.S2E.ExpiratBossRank packet, int sessionid)
	{
		es.getExpiratBossManager().rank(packet.getReq());
	}

	public void onTCPExchangeServerRecvExpiratBossTopRanks(TCPExchangeServer peer, Packet.S2E.ExpiratBossTopRanks packet, int sessionid)
	{
		// TODO
	}

	public void onTCPExchangeServerRecvExpiratBossGlobalRanks(TCPExchangeServer peer, Packet.S2E.ExpiratBossGlobalRanks packet, int sessionid)
	{
		// TODO
	}

	public void onTCPExchangeServerRecvExpiratBossGlobalRanks2(TCPExchangeServer peer, Packet.S2E.ExpiratBossGlobalRanks2 packet, int sessionid)
	{
		// TODO
	}

	public void onTCPExchangeServerRecvHeroesBossSync(TCPExchangeServer peer, Packet.S2E.HeroesBossSync packet, int sessionid)
	{
		es.getHeroesBossManager().syncHeroes(packet.getReq());
	}

	public void onTCPExchangeServerRecvHeroesBossFinishAtt(TCPExchangeServer peer, Packet.S2E.HeroesBossFinishAtt packet, int sessionid)
	{
		es.getHeroesBossManager().heroesBossFinishAtt(packet.getReq());
	}

	public void onTCPExchangeServerRecvHeroesBossInfo(TCPExchangeServer peer, Packet.S2E.HeroesBossInfo packet, int sessionid)
	{
		es.getHeroesBossManager().HeroesBossInfo(packet.getReq());
	}

	public void onTCPExchangeServerRecvSWarSearch(TCPExchangeServer peer, Packet.S2E.SWarSearch packet, int sessionid)
	{
		int serverID1 = table.getRandomServer();
		int serverID2 = table.getRandomServer();
		es.getSWarManager().search(serverID1, serverID2, packet.getReq());
	}

	public void onTCPExchangeServerRecvSWarSearchForward(TCPExchangeServer peer, Packet.S2E.SWarSearchForward packet, int sessionid)
	{
		int serverID = table.getRandomServer();
		es.getSWarManager().searchForward(serverID, packet.getRes());
	}

	public void onTCPExchangeServerRecvSWarAttack(TCPExchangeServer peer, Packet.S2E.SWarAttack packet, int sessionid)
	{
		es.getSWarManager().attack(packet.getReq());
	}

	public void onTCPExchangeServerRecvSWarAttackForward(TCPExchangeServer peer, Packet.S2E.SWarAttackForward packet, int sessionid)
	{
		es.getSWarManager().attackForward(packet.getRes());
	}

//	public void onTCPExchangeServerRecvForward(TCPExchangeServer peer, Packet.S2E.Forward packet, int sessionid)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeServerRecv(TCPExchangeServer peer, Packet.S2E. packet, int sessionid)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeServerRecvSWarSelect(TCPExchangeServer peer, Packet.S2E.SWarSelect packet, int sessionid)
//	{
//		es.getSWarManager().select(packet.getReq());
//	}
//
//	public void onTCPExchangeServerRecvSWarSelectForward(TCPExchangeServer peer, Packet.S2E.SWarSelectForward packet, int sessionid)
//	{
//		es.getSWarManager().selectForward(packet.getRes());
//	}
//
	public void onTCPExchangeServerRecvSWarRanks(TCPExchangeServer peer, Packet.S2E.SWarRanks packet, int sessionid)
	{
		es.getSWarManager().ranks(packet.getRes());
	}

	public void onTCPExchangeServerRecvSWarRank(TCPExchangeServer peer, Packet.S2E.SWarRank packet, int sessionid)
	{
		es.getSWarManager().rank(packet.getReq());
	}

	public void onTCPExchangeServerRecvSWarTopRanks(TCPExchangeServer peer, Packet.S2E.SWarTopRanks packet, int sessionid)
	{
		es.getSWarManager().topRanks(packet.getReq());
	}

	public void onTCPExchangeServerRecvSWarVote(TCPExchangeServer peer, Packet.S2E.SWarVote packet, int sessionid)
	{
		es.getSWarManager().voteForward(packet.getReq());
	}

//	public int getTCPExchangeServerRecvBufferSize()
//	{
//		return 32768 * 4;
//	}
//
	//// end handlers.
	
	public void sendPacketToServer(int serverID, SimplePacket packet)
	{
		int sid = table.getSessionIDByServerID(serverID);
		if( sid > 0 )
			tes.sendPacket(sid, packet);
	}
	
	NetManager managerNet = new NetManager();
	TCPExchangeServer tes = new TCPExchangeServer(this);
	ExchangeServer es;
	ServerTable table = new ServerTable();
        
        
        
        
}

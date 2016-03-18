// modified by ket.kio.RPCGen at Fri Dec 05 13:08:36 CST 2014.

package i3k.friend;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import ket.kio.NetAddress;
import ket.kio.SimplePacket;
import ket.kio.NetManager;
import i3k.rpc.Packet;
import i3k.SBean;

public class RPCManagerFriendServer
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
				tfs.closeSession(oldSID);
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
		
		// server id 2 sessionid
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();		
		// sessionid id 2 server id
		Map<Integer, Integer> smap = new HashMap<Integer, Integer>();
		//server id list
		List<Integer> slist = new ArrayList<Integer>();
		
		Random random = new Random();
	}
	
	public RPCManagerFriendServer(FriendServer fs)
	{
		this.fs = fs;
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
		tfs.setListenAddr(fs.getConfig().addrListen, ket.kio.BindPolicy.eReuseTimewait);
		tfs.setListenBacklog(128);
		tfs.open();
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public void sendSearchResult(int serverID, SBean.SearchFriendsRes res)
	{
		sendPacketToServer(serverID, new Packet.F2S.SearchFriends(res));
	}
	
	public void queryFriendProp(SBean.GlobalRoleID srcID, SBean.OpenIDRecord dstRecord)
	{
		sendPacketToServer(dstRecord.roleID.serverID, new Packet.F2S.QueryFriendProp(new SBean.QueryFriendPropReq(srcID, dstRecord)));
	}
	
	public void sendMessageToFriend(SBean.GlobalRoleID srcID, SBean.GlobalRoleID dstID, byte msgType, short arg1, int arg2)
	{
		sendPacketToServer(dstID.serverID, new Packet.F2S.SendMessageToFriend(srcID, dstID, msgType, arg1, arg2));
	}
	
	public void setCreateRoleRes(SBean.GlobalRoleID roleID)
	{
		sendPacketToServer(roleID.serverID, new Packet.F2S.CreateRole(roleID));
	}

	//// begin handlers.
	public void onTCPFriendServerOpen(TCPFriendServer peer)
	{
		// TODO
	}

	public void onTCPFriendServerOpenFailed(TCPFriendServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPFriendServerClose(TCPFriendServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPFriendServerSessionOpen(TCPFriendServer peer, int sessionid, NetAddress addrClient)
	{
		fs.getLogger().info("session open, sid=" + sessionid + ", addr=" + addrClient.toString());
		peer.sendPacket(sessionid, new Packet.F2S.StateAnnounce(fs.getConfig().id));
	}

	public void onTCPFriendServerSessionClose(TCPFriendServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		table.onSessionClose(sessionid);
	}

	public void onTCPFriendServerRecvWhoAmI(TCPFriendServer peer, Packet.S2F.WhoAmI packet, int sessionid)
	{
		fs.getLogger().info("client announce sid =" + sessionid + ", platID=" + packet.getPlatID() + ", gsID=" + packet.getServerID());
		table.onSessionAnnounce(sessionid, packet.getPlatID(), packet.getServerID());
	}

	public void onTCPFriendServerRecvCreateRole(TCPFriendServer peer, Packet.S2F.CreateRole packet, int sessionid)
	{
		fs.getDataManager().announceCreateRole(packet.getOpenID(), packet.getRoleID());
	}

	public void onTCPFriendServerRecvSearchFriends(TCPFriendServer peer, Packet.S2F.SearchFriends packet, int sessionid)
	{
		fs.getDataManager().searchFriends(packet.getReq());
	}

	public void onTCPFriendServerRecvQueryFriendProp(TCPFriendServer peer, Packet.S2F.QueryFriendProp packet, int sessionid)
	{
		fs.getDataManager().setQueryFriendPropRes(packet.getRes());
	}

	public void onTCPFriendServerRecvUpdateFriendProp(TCPFriendServer peer, Packet.S2F.UpdateFriendProp packet, int sessionid)
	{
		fs.getDataManager().updateFriendProp(packet.getReq());
	}

	public void onTCPFriendServerRecvSendMessageToFriend(TCPFriendServer peer, Packet.S2F.SendMessageToFriend packet, int sessionid)
	{
		fs.getDataManager().sendMessageToFriend(packet.getSrcID(), packet.getDstID(), packet.getMsgType(), packet.getArg1(), packet.getArg2());
	}

	//// end handlers.
	
	public void sendPacketToServer(int serverID, SimplePacket packet)
	{
		int sid = table.getSessionIDByServerID(serverID);
		if( sid > 0 )
			tfs.sendPacket(sid, packet);
	}
	
	NetManager managerNet = new NetManager();
	TCPFriendServer tfs = new TCPFriendServer(this);
	FriendServer fs;
	ServerTable table = new ServerTable();
}

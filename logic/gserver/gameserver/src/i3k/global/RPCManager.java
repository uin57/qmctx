// modified by ket.kio.RPCGen at Wed Oct 29 20:27:10 CST 2014.

package i3k.global;


import java.util.List;

import ket.kio.NetAddress;
import ket.kio.NetManager;
import i3k.rpc.Packet;
import i3k.LuaPacket;

public class RPCManager
{
	
	public RPCManager(GlobalServer gls)
	{
		this.gls = gls;
		if( gls.getConfig().nIOThread == 1 )
			managerNet = new NetManager(NetManager.NetManagerType.eSelectNetManager, gls.getConfig().nIOThread);
		else
			managerNet = new NetManager(NetManager.NetManagerType.eMTSelectNetManager, gls.getConfig().nIOThread);
		
		tgs = new TCPGlobalServer(this);
	}
	
	public void start()
	{
		managerNet.start();
		tgs.setListenAddr(gls.getConfig().addrListen, ket.kio.BindPolicy.eReuseTimewait);
		tgs.setListenBacklog(128);
		tgs.open();
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public void onTimer()
	{
		managerNet.checkIdleConnections();
	}

	public NetManager getNetManager() { return managerNet; }

	//// begin handlers.
	public int getTCPGlobalServerMaxConnectionIdleTime()
	{
		// TODO
		return 60000;
	}

	public void onTCPGlobalServerOpen(TCPGlobalServer peer)
	{
		gls.getLogger().info("global server on open");
	}

	public void onTCPGlobalServerOpenFailed(TCPGlobalServer peer, ket.kio.ErrorCode errcode)
	{
		gls.getLogger().info("global server on peer open failed errcode = " + errcode.toString());
	}

	public void onTCPGlobalServerClose(TCPGlobalServer peer, ket.kio.ErrorCode errcode)
	{
		gls.getLogger().info("global server on peer close errcode = " + errcode.toString());
	}

	public void onTCPGlobalServerSessionOpen(TCPGlobalServer peer, int sessionid, NetAddress addrClient)
	{
		gls.getLogger().info("global server session on peer open  sessionid = " + sessionid + ", client : " + addrClient.host);
	}

	public void onTCPGlobalServerSessionClose(TCPGlobalServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		gls.getLogger().info("global server session on peer close sessionid = " + sessionid + ", errcode = " + errcode.toString());
	}

	public void onTCPGlobalServerRecvLuaChannel(TCPGlobalServer peer, Packet.C2G.LuaChannel packet, int sessionid)
	{
		// TODO
		gls.getLogger().debug("session " + sessionid + " on receive peer lua channel: " + packet.getData());
		try
		{
			String[] msg = LuaPacket.decode(packet.getData());
			onLuaChannelList(peer, sessionid, msg);
		}
		catch(Exception ex)
		{			
			//ex.printStackTrace();
			gls.getLogger().warn(ex.getMessage());
		}
	}

	public void onTCPGlobalServerRecvLuaChannel2(TCPGlobalServer peer, Packet.C2G.LuaChannel2 packet, int sessionid)
	{
		// TODO
		gls.getLogger().debug("session " + sessionid + " on receive peer lua channel2:  list data " + packet.getData().toString());
		try
		{
			String[] msg = new String[packet.getData().size()];
			msg = packet.getData().toArray(msg);
			onLuaChannelList(peer, sessionid, msg);
		}
		catch(Exception ex)
		{			
			//ex.printStackTrace();
			gls.getLogger().warn(ex.getMessage());
		}
	}

	//// end handlers.
	
	public void sendLuaPacket(TCPGlobalServer peer, int sid, String data)
	{
		gls.getLogger().debug("session " + sid + " send peer lua packet: " + data);
		peer.sendPacket(sid, new Packet.G2C.LuaChannel(data));
	}
	
	public void sendLuaPacket(TCPGlobalServer peer, int sid, List<String> data)
	{
		gls.getLogger().debug("session " + sid + " send peer lua packet: list data (" + data.size() + ") " + data.subList(0, data.size() >= 10 ? 10 : data.size()).toString());
		peer.sendPacket(sid, new Packet.G2C.LuaChannel2(data));
	}
	
	public void sendLuaPacket2(TCPGlobalServer peer, int sid, List<String> data)
	{
		gls.getLogger().debug("session " + sid + " send peer lua packet2:  list data (" + data.size() + ") " + data.subList(0, data.size() >= 10 ? 10 : data.size()).toString());
		peer.sendPacket(sid, new Packet.G2C.LuaChannel2(data));
	}
	
	public void onLuaChannelList(TCPGlobalServer peer, int sessionid, String[] msg)
	{
		int msgIndex = 0;
		int protocolVersion = Integer.parseInt(msg[msgIndex++]);
		switch (msg[msgIndex++])
		{
		case "sync":
			{
				int gsid = Integer.parseInt(msg[msgIndex++]);
				int rid = Integer.parseInt(msg[msgIndex++]);
				SocialManager.SocialUserInfo info = gls.getSocialManager().getUserInfo(gsid, rid);
				sendLuaPacket2(peer, sessionid, LuaPacket.encodeSocialUserSyncInfo(protocolVersion, info));
			}
			break;
		case "page":
			{
				int gsid = Integer.parseInt(msg[msgIndex++]);
				int rid = Integer.parseInt(msg[msgIndex++]);
				int cid = Integer.parseInt(msg[msgIndex++]);
				int pageNo = Integer.parseInt(msg[msgIndex++]);
				SocialManager.PageMsgInfo info = gls.getSocialManager().getPageMsg(gsid, rid, cid, pageNo);
				sendLuaPacket2(peer, sessionid, LuaPacket.encodeSocialPageMsgInfo(protocolVersion, gsid, rid, info));
			}
			break;
		case "send":
			{
				int gsid = Integer.parseInt(msg[msgIndex++]);
				int rid = Integer.parseInt(msg[msgIndex++]);
				String gsname = msg[msgIndex++];
				String name = msg[msgIndex++];
				int cid = Integer.parseInt(msg[msgIndex++]);
				String smsg = msg[msgIndex++];
				sendLuaPacket2(peer, sessionid, LuaPacket.encodeSendSocialMsg(protocolVersion, gls.getSocialManager().sendMsg(gsid, rid, gsname, name, cid, smsg)));
			}
			break;
		case "like":
			{
				int gsid = Integer.parseInt(msg[msgIndex++]);
				int rid = Integer.parseInt(msg[msgIndex++]);
				String gsname = msg[msgIndex++];
				String name = msg[msgIndex++];
				int cid = Integer.parseInt(msg[msgIndex++]);
				int msgid = Integer.parseInt(msg[msgIndex++]);
				sendLuaPacket2(peer, sessionid, LuaPacket.encodeLikeSocialMsg(protocolVersion, gls.getSocialManager().likeMsg(gsid, rid, gsname, name, cid, msgid)));
			}
			break;
		default:
			break;
		}
	}
	
	NetManager managerNet = null;
	TCPGlobalServer tgs = null;
	GlobalServer gls;
}

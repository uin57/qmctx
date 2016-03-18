// modified by ket.kio.RPCGen at Fri Feb 28 16:39:48 CST 2014.

package i3k.gm;

import java.util.List;

import ket.kio.SimplePacket;
import ket.kio.NetManager;
import i3k.gs.test.Robot;
import i3k.rpc.Packet;
import i3k.rpc.Packet.S2M.DataModify;
import i3k.SBean;

public class RPCManagerControlClient
{
	
	public RPCManagerControlClient(GMClient gm)
	{
		this.gm = gm;
	}

	public NetManager getNetManager()
	{
		return managerNet;
	}
	
	public void start()
	{	
		managerNet.start();
		tcc = new TCPControlClient(this);
		tcc.setServerAddr(gm.getConfig().addrServer);
		tcc.open();
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public void shutdown(byte type, int s, int t)
	{
		tcc.sendPacket(new Packet.M2S.Shutdown(type, s, t));
	}
	
	public void announce(String msg, byte times)
	{
		tcc.sendPacket(new Packet.M2S.Announce(msg, times));
	}
	
	public void queryOnlineUsers()
	{
		tcc.sendPacket(new Packet.M2S.DataQuery(new SBean.DataQueryReq(SBean.DataQueryReq.eOnlineUser, 0, 0, "", "")));
	}
	
	public void queryRoleIDByRoleName(String name)
	{
		tcc.sendPacket(new Packet.M2S.DataQuery(new SBean.DataQueryReq(SBean.DataQueryReq.eRoleID, 1, 0, name, "")));
	}
	
	public void queryRoleIDByUserName(String name)
	{
		tcc.sendPacket(new Packet.M2S.DataQuery(new SBean.DataQueryReq(SBean.DataQueryReq.eRoleID, 2, 0, name, "")));
	}
	
	public void modData(SBean.DataModifyReq req)
	{
		tcc.sendPacket(new Packet.M2S.DataModify(req));
	}
	
	public void sendSysMessage(int roleID, short lvlReq, short vipReq, String title, String content, int lifeTime, int flag, List<SBean.DropEntryNew> att)
	{
		tcc.sendPacket(new Packet.M2S.SysMessage(new SBean.SysMail(roleID, lvlReq, vipReq, 0, lifeTime, flag, title, content, att)));
	}
	
	public void addWorldGift(short id, String msg, int time)
	{
		tcc.sendPacket(new Packet.M2S.WorldGift(id, time, msg));
	}
	
	public void getRoleData(SBean.RoleDataReq req)
	{
		tcc.sendPacket(new Packet.M2S.RoleData(req));
	}
	
	//// begin handlers.
	public void onTCPControlClientOpen(TCPControlClient peer)
	{
		// TODO
		System.out.println("server open");
	}

	public void onTCPControlClientOpenFailed(TCPControlClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
		System.out.println("server open failed");
	}

	public void onTCPControlClientClose(TCPControlClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
		gm.onSessionClose();
	}

	public void onTCPControlClientRecvShutdown(TCPControlClient peer, Packet.S2M.Shutdown packet)
	{
		System.out.println("shutdown commited, res is " + packet.getRes());
	}

	public void onTCPControlClientRecvAnnounce(TCPControlClient peer, Packet.S2M.Announce packet)
	{
		gm.onAnnounce(packet.getRes());
	}

	public void onTCPControlClientRecvOnlineUser(TCPControlClient peer, Packet.S2M.OnlineUser packet)
	{
		gm.onQueryOnlineUserRes(packet.getRcnt(), packet.getScnt());
	}
	public void onTCPControlClientRecvQueryRoleBrief(TCPControlClient peer, Packet.S2M.QueryRoleBrief packet)
	{
		// TODO
	}

	public void onTCPControlClientRecvDataModify(TCPControlClient peer, Packet.S2M.DataModify packet)
	{
		gm.onDataMod(packet.getRes());
	}

	public void onTCPControlClientRecvSysMessage(TCPControlClient peer, Packet.S2M.SysMessage packet)
	{
		gm.onSysMessage(packet.getRes());
	}

	public void onTCPControlClientRecvDataQuery(TCPControlClient peer, Packet.S2M.DataQuery packet)
	{
		gm.onDataQueryRes(packet.getRes());
	}
	
	public void onTCPControlClientRecvRoleData(TCPControlClient peer, Packet.S2M.RoleData packet)
	{
		gm.onRoleData(packet.getRes());
	}

	public void onTCPControlClientRecvWorldGift(TCPControlClient peer, Packet.S2M.WorldGift packet)
	{
		gm.onWorldGift(packet.getRes());
	}

	//// end handlers.
	

	public boolean testOpen()
	{
		if( tcc.isOpen() )
			return true;
		tcc.open();
		return false;
	}

	
	TCPControlClient tcc;
	NetManager managerNet = new NetManager();
	GMClient gm;
}

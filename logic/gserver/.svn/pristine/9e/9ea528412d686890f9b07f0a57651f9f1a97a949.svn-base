// modified by ket.kio.RPCGen at Mon Dec 01 17:32:08 CST 2014.

package i3k.gs.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ket.kio.NetManager;
import ket.util.FileSys;
import i3k.gs.GameData;
import i3k.gs.GameServer;
import i3k.gs.Role;
import i3k.rpc.Packet;
import i3k.rpc.Packet.C2S.LuaChannel;
import i3k.LuaPacket;
import i3k.SBean;

public class RPCManagerClient
{
	public RPCManagerClient(GameClient gc)
	{
		this.gc = gc;
	}
	
	public NetManager getNetManager() { return managerNet; }
	
	private class ReconnectTask implements Runnable
	{
		public ReconnectTask(Robot robot)
		{
			this.robot = robot;
		}
		@Override
		public void run()
		{
			//gc.getLogger().debug("reconnect Task run");
			// TODO
			gc.logStatDoConnect(true);
			if( ! robot.bReconnect )
			{
				gc.logStatDoConnectS();
				robot.bReconnect = true;
			}
			robot.open();
		}
		
		Robot robot;
	}
	
	private class WorkerTask implements Runnable
	{
		@Override
		public void run()
		{
			//gc.getLogger().debug("worker Task run");
			if( robots == null )
				return;
			for(Robot robot : robots.values())
			{
				onTimer(robot);
			}
		}
		
		Future<?> future = null;
	}
	
	public void onLuaChannelList(Robot robot, String[] msg)
	{
		switch( msg[0] )
		{
		case "createrole":
			{
				Byte errcode = Byte.parseByte(msg[1]);
				{
					gc.getLogger().debug(robot.accName + " create role, errcode is " + errcode);
					
					List<String> data = new ArrayList<String>();
					data.add("createrole");
					data.add(robot.accName);
					data.add("");
					data.add("0");
					data.add("0");
					if( gc.getConfig().useOpenKey == 1 )
						data.add(new Byte(SBean.UserLoginRequest.eLoginNormal).toString());
					else
						data.add(new Byte(SBean.UserLoginRequest.eLoginGod).toString());
					data.add(robot.getOpenID());
					data.add(robot.getRoleName());
					data.add("1");
					data.add("");
					data.add("1");
					data.add("1");
					data.add(robot.getOpenID()); // openid
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					if( gc.getConfig().useOpenKey == 1 )
						data.add(robot.getOpenID());
					else
						data.add(""); // 15
					data.add("0");
					data.add("0");
					data.add("0");
					data.add("0");
					
					robot.sendPacket(new Packet.C2S.LuaChannel2(data));
				}
			
			}
			break;
		case "userlogin":
			{
				Byte errcode = Byte.parseByte(msg[1]);				
				{
					gc.getLogger().debug(robot.accName + " login res is " + errcode);
					if( errcode == GameData.USERLOGIN_OK )
					{
						robot.bLogin = true;
						robot.state = Robot.STATE_NULL;
						final int MAX_LOGIN_TIME = 1800 + random.nextInt(3600);
						robot.logoutTime = GameData.getInstance().getTime() + MAX_LOGIN_TIME;
						gc.logStatLogin(true);
					}
					else
					{
						gc.logStatLogin(false);
						gc.getLogger().warn("login failed, err=" + errcode.byteValue());
					}
				}
			}
			break;
		case "combats":
			{
				switch(msg[1])
				{
				case "sync":
					{
						int p = 2;
						int n = Integer.parseInt(msg[p++]);
						for(int i = 0; i < n; ++i)
						{
							p += 2;
						}
						n = Integer.parseInt(msg[p++]);
						for(int i = 0; i < n; ++i)
						{
							p += 2;
						}
						n = Integer.parseInt(msg[p++]);
						for(int i = 0; i < n; ++i)
						{
							SBean.DBCombatPos pos = new SBean.DBCombatPos();
							pos.catIndex = Byte.parseByte(msg[p++]);
							pos.combatIndex = Byte.parseByte(msg[p++]);
							if( i == 0 )
							{
								robot.combatPos = pos;
							}
						}
					}
					break;
				case "start":
					{
						byte bok = Byte.parseByte(msg[6]);
						gc.logStatCombatStart(bok == 1);
						if( bok == 0 )
						{
							robot.state = Robot.STATE_NULL;
						}
						else
							robot.state = Robot.STATE_COMBAT;
					}
					break;
				case "finish":
					{
						byte bok = Byte.parseByte(msg[9]);
						gc.logStatCombatFinish(bok == 1);
						if( bok == 1 )
						{
							robot.combatPos.combatIndex++; // TODO
							if( robot.combatPos.combatIndex == 18 )
							{
								robot.combatPos.combatIndex = 0;
								robot.combatPos.catIndex++;
							}
						}
						robot.state = Robot.STATE_NULL;						
					}
					break;
				default:
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void onTimer(Robot robot)
	{
		if( ! robot.bLogin )
			return;
		int now = GameData.getInstance().getTime();
		if( now > robot.logoutTime && gc.getConfig().bTestTask == 0 )
		{
			kickRobot(robot, 1);
			return;
		}
		switch( robot.state )		
		{
		case Robot.STATE_NULL:
			int waitTime = random.nextInt(Robot.ACTION_COOL_RANDOM) + Robot.ACTION_COOL_BASE;
			if( gc.getConfig().bTestTask == 1 )
				waitTime = 0;
			if( now - robot.lastActTime >= waitTime )
			{
				// select action
				int task = robot.selectTask(random);
				robot.lastActTime = now;
				switch( task )
				{
				case Robot.TASK_COMBAT:
					robot.state = Robot.STATE_COMBAT_START;
					gc.logStatCombatStart();
					luaChannel(robot, "combats;start;0;" + robot.combatPos.catIndex
							+ ";" + robot.combatPos.combatIndex + ";" + ((robot.combatPos.catIndex)*18+(robot.combatPos.combatIndex+1)) + ";0;1;11");
					break;
				case Robot.TASK_NULL:
					break;
				default:
					throw new Error("incorrect action");
				}
			}
			break;
		case Robot.STATE_COMBAT:
			{
				int waitTime2 = random.nextInt(30) + 30;
				if( gc.getConfig().bTestTask == 1 )
					waitTime2 = 0;
				if( now - robot.lastActTime >= waitTime2 )
				{
					robot.lastActTime = now;
					robot.state = Robot.STATE_COMBAT_FINISH;
					gc.logStatCombatFinish();
					//
					luaChannel(robot, "combats;finish;0;" + robot.combatPos.catIndex
							+ ";" + robot.combatPos.combatIndex + ";" + ((robot.combatPos.catIndex)*18+(robot.combatPos.combatIndex+1)) + ";3;1;1;1;11;3;1;1;1;1;1");	
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void kickRobot(Robot robot, int reason)
	{
		if( gc.getConfig().bTestTask == 1 )
			throw new Error("test task failed");
		robot.close();
	}
	
	public void start()
	{
		// TODO
		managerNet.start();
		if( gc.getConfig().bTestTask == 1 )
			gc.getConfig().nClient = 1;
		
		if( gc.getConfig().bTestTask == 1 )
			worker.future = gc.getExecutor().scheduleAtFixedRate(worker, 200, 200, TimeUnit.MILLISECONDS);
		else
			worker.future = gc.getExecutor().scheduleAtFixedRate(worker, 1, 1, TimeUnit.SECONDS);
	}
	
	public void addRobot()
	{
		Robot robot = new Robot(this, gc);
		int i = gc.robotIDSeed.incrementAndGet();		
		robot.setIDSeed(i);
		robot.nextName();
		robot.setServerAddr(gc.getConfig().addrServer);
		robot.open();
		robots.put(i, robot);
		gc.logStatDoConnect(false);
	}
	
	public void luaChannel(Robot robot, String data)
	{
		gc.getLogger().debug("robot:" + robot.accName + " send luaPacket:" + data);
		robot.sendPacket(new Packet.C2S.LuaChannel(data));
	}
	
	public void destroy()
	{
		bDestroy = true;
		managerNet.destroy();
		if ( worker.future != null )
			worker.future.cancel(false);
	}

	//// begin handlers.
	public void onTCPGameClientOpen(TCPGameClient peer)
	{
		gc.logStatConnected();
		List<String> lst = new ArrayList<String>();
		lst.add("keepalive");
		lst.add("1");
		peer.sendPacket(new Packet.C2S.LuaChannel2(lst));
	}

	public void onTCPGameClientOpenFailed(TCPGameClient peer, ket.kio.ErrorCode errcode)
	{
		gc.logStatConnectFailed();
		Robot robot = (Robot)peer;
		//gc.log(robot.accName + " connect failed");
		if( gc.getConfig().reconnectInterval > 0 && ! bDestroy )
			gc.getExecutor().schedule(new ReconnectTask(robot), gc.getConfig().reconnectInterval, TimeUnit.SECONDS);
	}

	public void onTCPGameClientClose(TCPGameClient peer, ket.kio.ErrorCode errcode)
	{
		gc.logStatClose();
		Robot robot = (Robot)peer;
		robot.bLogin = false;
		//gc.log(robot.accName + " session closed");
		//robot.nextName();
		if( gc.getConfig().reconnectInterval > 0 && ! bDestroy )
		{
			gc.getExecutor().schedule(new ReconnectTask(robot), gc.getConfig().reconnectInterval, TimeUnit.SECONDS);
		}
	}

	public void onTCPGameClientRecvServerChallenge(TCPGameClient peer, Packet.S2C.ServerChallenge packet)
	{
		gc.getLogger().debug("on serverChallenge, flag=" + packet.getFlag() + ", istate=" + packet.getIstate());
		// TODO
		Robot robot = (Robot)peer;
		//gc.log(robot.accName + " connected");
		List<String> data = new ArrayList<String>();
		data.add("login");
		data.add(robot.accName);
		data.add("");
		data.add("0");
		data.add("0");
		if( gc.getConfig().useOpenKey == 1 )
			data.add(new Byte(SBean.UserLoginRequest.eLoginNormal).toString());
		else
			data.add(new Byte(SBean.UserLoginRequest.eLoginGod).toString());
		data.add(robot.getOpenID()); // 6
		data.add(""); // 7
		data.add("0"); // 8
		data.add(robot.getOpenID()); // 9 openid
		data.add("0"); // 10
		data.add(""); // 11
		data.add(""); // 12
		data.add(""); // 13
		data.add("0"); // 14
		if( gc.getConfig().useOpenKey == 1 )
			data.add(robot.getOpenID());
		else
			data.add(""); // 15
		data.add(""); // 16
		data.add(""); // 17
		data.add(""); // 18
		robot.sendPacket(new Packet.C2S.LuaChannel2(data));
		gc.logStatLogin();
	}

	public void onTCPGameClientRecvServerResponse(TCPGameClient peer, Packet.S2C.ServerResponse packet)
	{
		// TODO
	}

	public void onTCPGameClientRecvLuaChannel(TCPGameClient peer, Packet.S2C.LuaChannel packet)
	{
		gc.getLogger().debug("recv luachannel: " + packet.getData());
		try
		{
			String[] msg = LuaPacket.decode(packet.getData().substring(1));
			onLuaChannelList((Robot)peer, msg);
		}
		catch(Exception ex)
		{			
			gc.getLogger().warn("lua channel err", ex);
		}
	}

	public void onTCPGameClientRecvLuaChannel2(TCPGameClient peer, Packet.S2C.LuaChannel2 packet)
	{
		try
		{
			String[] msg = new String[packet.getData().size()]; 
			msg = packet.getData().toArray(msg);
			onLuaChannelList((Robot)peer, msg);
		}
		catch(Exception ex)
		{			
			gc.getLogger().warn("lua channel err", ex);
		}
	}

	public void onTCPGameClientRecvCmnRPCResponse(TCPGameClient peer, Packet.S2C.CmnRPCResponse packet)
	{
	}

////
//////	public void onTCPGameClientRecvRoleEquipsSync(TCPGameClient peer, Packet.S2C.RoleEquipsSync packet)
//////	{
//////		//Robot robot = (Robot)peer;
//////		//gc.log(robot.accName + " equips sync, count is " + packet.getEquips().size());
//////	}
//////
//////
//////	public void onTCPGameClientRecvRoleBriefSync(TCPGameClient peer, Packet.S2C.RoleBriefSync packet)
//////	{
//////		Robot robot = (Robot)peer;
//////		gc.log(robot.accName + " role brief sync, id is " + packet.getBrief().id 
//////				+ ", lvl = " + packet.getBrief().lvl
//////				+ ", sid = " + packet.getBrief().scriptID);
//////		SBean.RoleBrief b = packet.getBrief();
//////		robot.rid = b.id;
//////		robot.exp2 = b.exp2;
//////		robot.lvl2 = b.lvl2;
//////		robot.scriptID = b.scriptID;
//////	}
//////
//////	public void onTCPGameClientRecvFriendsSync(TCPGameClient peer, Packet.S2C.FriendsSync packet)
//////	{
//////		Robot robot = (Robot)peer;
//////		if( packet.getRes().ret != SBean.RPCRes.eOK )
//////		{
//////			kickRobot(robot, 0);
//////			return;
//////		}
//////		robot.friend = packet.getDbfriend();
//////	}
////
////	public void onTCPGameClientRecvRoleGeneralsSync(TCPGameClient peer, Packet.S2C.RoleGeneralsSync packet)
////	{
////		Robot robot = (Robot)peer;
////		robot.generals = packet.getGenerals();
////		//gc.log(robot.accName + " generals sync, count is " + packet.getGenerals().size());
////	}
////
////	public void onTCPGameClientRecvFinishCombat(TCPGameClient peer, Packet.S2C.FinishCombat packet)
////	{
////		Robot robot = (Robot)peer;
////		if( packet.getRes().ret != SBean.RPCRes.eOK )
////		{
////			kickRobot(robot, 0);
////			return;
////		}
////		//gc.log(robot.accName + " finish combat res is " + packet.getRes().ret + ", money = " + packet.getBonus().money);
////		robot.act = Robot.ACT_NULL;
////		robot.lastActTime = FileSys.getLocalTime();
////		if( packet.getBonus().money > 0 )
////			robot.bCombatFinished = true;
////	}
////	public void onTCPGameClientRecvStartCombat(TCPGameClient peer, Packet.S2C.StartCombat packet)
////	{
////		Robot robot = (Robot)peer;
////		if( packet.getRes().ret != SBean.RPCRes.eOK )
////		{
////			robot.close(); // TODO
////			return;
////		}
////		//gc.log(robot.accName + " start combat res is " + packet.getRes().ret);
////	}
////
////	public void onTCPGameClientRecvTaskComplete(TCPGameClient peer, Packet.S2C.TaskComplete packet)
////	{
////		Robot robot = (Robot)peer;
////		if( packet.getRes().ret != SBean.RPCRes.eOK )
////		{
////			kickRobot(robot, 0);
////			return;
////		}
////		//gc.log(robot.accName + " complete task res is " + packet.getRes().ret);
////		if( packet.getRes().ret == SBean.RPCRes.eOK )
////		{
////			robot.scriptID++; // TODO			
////		}
////	}
//
//
	public void onTCPGameClientRecvAntiData(TCPGameClient peer, Packet.S2C.AntiData packet)
	{
		// TODO
	}

	public void onTCPGlobalClientOpen(TCPGlobalClient peer)
	{
		// TODO
	}

	public void onTCPGlobalClientOpenFailed(TCPGlobalClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPGlobalClientClose(TCPGlobalClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPGlobalClientRecvLuaChannel(TCPGlobalClient peer, Packet.G2C.LuaChannel packet)
	{
		// TODO
	}

	public void onTCPGlobalClientRecvLuaChannel2(TCPGlobalClient peer, Packet.G2C.LuaChannel2 packet)
	{
		// TODO
	}

	//// end handlers.
	
	ConcurrentHashMap<Integer, Robot> robots = new ConcurrentHashMap<Integer, Robot>();
	volatile boolean bDestroy = false;
	NetManager managerNet = new NetManager();
	GameClient gc;
	WorkerTask worker = new WorkerTask();
	Random random = new Random();
}

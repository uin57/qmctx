// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.gs;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.ObjectName;

import i3k.DBForce;
import i3k.DBMail;
import i3k.DBMail.MailAttachments;
import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.IDIP;
import i3k.IDIP.SBagInfo;
import i3k.IDIP.SGeneralEquipInfo;
import i3k.IDIP.SGeneralInfo;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.SBean.DBBeMonsterLineup;
import i3k.SBean.DBDiskBetRank;
import i3k.SBean.DBExpiratBossTimes;
import i3k.SBean.DBGeneralStone;
import i3k.SBean.DBGeneralStoneProp;
import i3k.SBean.DBHeadIcon;
import i3k.SBean.DBHeroesBossInfo;
import i3k.SBean.DBInvitationFriend;
import i3k.SBean.DBRoleItem2;
import i3k.SBean.DropEntry;
import i3k.SBean.DropEntryNew;
import i3k.SBean.ExpatriateTransRoleRes;
import i3k.SBean.HeroesBossFinishAttRes;
import i3k.SBean.HeroesBossSyncRes;
import i3k.SBean.HeroesRank;
import i3k.SBean.RoleDataRes;
import i3k.TLog;
import i3k.gs.DuelManager.RTMatchData;
import i3k.gs.ForceManager.ForceBrief;
import i3k.gs.GameActivities.ActivityDisplayConfig;
import i3k.gs.GameActivities.DiskBetConfig;
import i3k.gs.GameActivities.DiskGiftConfig;
import i3k.gs.GameActivities.RechargeGiftConfig;
import i3k.gs.GameActivities.RechargeRankConfig;
import i3k.gs.LoginManager.CommonRoleVisitor;
import i3k.gs.Role.Pet;
import i3k.gs.Role.ShopRandBrief;
import i3k.rpc.Packet;
import ket.kio.NetAddress;
import ket.kio.NetManager;
import ket.util.ARC4StreamSecurity;
import ket.util.MD5Digester;
import ket.util.Stream;
import ket.util.StringConverter;

public class RPCManager
{
	
	public interface GSNetMBean
	{
		int getTGSOpenCount();
		int getTGSCloseCount();
	}
	
	public class GSNet implements GSNetMBean
	{
		@Override
		public int getTGSOpenCount()
		{
			return tgsOpenCount.get();
		}
		
		@Override
		public int getTGSCloseCount()
		{
			return tgsCloseCount.get();
		}
		
		public void incTGSOpenCount()
		{
			tgsOpenCount.incrementAndGet();
		}
		
		public void incTGSCloseCount()
		{
			tgsCloseCount.incrementAndGet();
		}
		
		AtomicInteger tgsOpenCount = new AtomicInteger();
		AtomicInteger tgsCloseCount = new AtomicInteger();
	}
	
	public static class SessionInfo
	{
		//public int sid;
		public NetAddress addrClient;
		AtomicInteger pps = new AtomicInteger(0);
	}
	
	public SessionInfo getSessionInfo(int sid)
	{
		return sessions.get(sid);
	}
	
	public boolean incSessionPackets(int sid)
	{
		final int ppsMax = gs.getConfig().pps;
		if( ppsMax <= 0 )
			return true;
		SessionInfo sinfo = getSessionInfo(sid);
		if( sinfo == null )
			return true;		
		boolean bOK = sinfo.pps.incrementAndGet() < ppsMax;
		if( ! bOK )
		{
			gs.getLogger().warn("pps trigger close, val=" + sinfo.pps.get());
			closeSession(sid);
		}
		return bOK;
	}
	
	public RPCManager(GameServer gs)
	{
		this.gs = gs;
		if( gs.getConfig().nIOThread == 1 )
			managerNet = new NetManager(NetManager.NetManagerType.eSelectNetManager, gs.getConfig().nIOThread);
		else
			managerNet = new NetManager(NetManager.NetManagerType.eMTSelectNetManager, gs.getConfig().nIOThread);
		tgs = new TCPGameServer(this);
		tcs = new TCPControlServer(this);
		udpLogger = new UDPLogger(this);
		idips = new TCPIDIPServer(this);
		tec = new TCPExchangeClient(this);
		tfc = new TCPFriendClient(this);
	}
	
	public void start()
	{
		managerNet.start();
		tgs.setListenAddr(gs.getConfig().addrListen, ket.kio.BindPolicy.eReuseTimewait);
		tgs.setListenBacklog(128);
		tgs.open();
		
		tcs.setListenAddr(gs.getConfig().addrControl, ket.kio.BindPolicy.eReuseTimewait);
		tcs.setListenBacklog(16);
		tcs.open();
		
		idips.setListenAddr(gs.getConfig().addrIDIP, ket.kio.BindPolicy.eReuseTimewait);
		idips.setListenBacklog(16);
		idips.open();
		
		udpLogger.setConnectAddr(gs.getConfig().addrTLog);
		udpLogger.open();
		
		tec.setServerAddr(gs.getConfig().addrExchange);
		tec.open();
		
		tfc.setServerAddr(gs.getConfig().addrFriend);
		tfc.open();
		
		try
		{
			ManagementFactory.getPlatformMBeanServer().registerMBean(gsNetStat, new ObjectName("i3k.gs:type=GSNet"));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public void onTimer()
	{
		sps.set(0);
		if( gs.getConfig().pps > 0 )
		{
			for(SessionInfo e : sessions.values())
			{
				e.pps.set(0);
			}
		}
		managerNet.checkIdleConnections();
		if( ! tec.isOpen() )
			tec.open();
		if( ! tfc.isOpen() )
			tfc.open();
		
		//TODO
//		gs.getLogger().debug("NET STAT: OPEN=" + gsNetStat.getTGSOpenCount() + ", CLOSE=" + gsNetStat.getTGSCloseCount()
//				+ ", SESSION=" + sessions.size() + ", ROLES=" + gs.getLoginManager().mapRoles.size()
//				+ ", SR2S=" + gs.getLoginManager().maps2r.size()
//				);
	}
	
	public void sendTLog(String log)
	{
		udpLogger.sendString(log);
		//gs.getTLogger().info(log);
	}
	
	public void sendLuaPatchTip(int sid)
	{
		sendLuaPacket(sid, LuaPacket.encodeLuaPatchTip());
	}
	
	public void sendLuaPatch(int sid, String code)
	{
		sendLuaPacket(sid, LuaPacket.encodeLuaPatch(code));
	}
	
	public void sendAntiData(int sid, byte[] data)
	{
		tgs.sendPacket(sid, new Packet.S2C.AntiData(new SBean.TSSAntiData(ByteBuffer.wrap(data))));
	}

	public NetManager getNetManager() { return managerNet; }
	
	public void closeSession(int sid)
	{
		tgs.closeSession(sid);
	}
	
	public void onCreateRole(int sessionid, SBean.UserLoginRequest reql, Role.MSDKInfo msdkInfo, String rname, short headIconID, String userNameOrg)
	{
		if( headIconID < 1) headIconID = 1;
		String uname = gs.getLoginManager().checkUserInput(reql.userName, gs.getGameData().getUserInputCFG().userName, true, false);
		if( uname != null )
		{
			gs.getLogger().debug("user start createrole:" + reql.userName + ", sid=["
					+ reql.key + "], type="+reql.loginType+",deviceID="+reql.deviceID+",rname=" + rname +",gender=" + headIconID);
			reql.userName = uname;
			if( reql.loginType == SBean.UserLoginRequest.eLoginNormal )
			{
				if( gs.getMidas().getHTTPTaskQueueSize() >= gs.getConfig().httpTaskMaxCountL1 )
				{
					gs.getLoginManager().startCreateRole(sessionid, reql, msdkInfo, rname, headIconID);
				}
				else
				{
					if( gs.getLoginManager().addUserLogin(sessionid, reql, msdkInfo, rname, headIconID) )
					{
						if( true )
						{
							gs.getMidas().loginVerify(new Midas.UserInfo(msdkInfo.openID, msdkInfo.openKey, msdkInfo.payToken, msdkInfo.pf, msdkInfo.pfKey,
									msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSIDGuest:gs.getConfig().offerCookieSID,
											msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSTypeGuest:gs.getConfig().offerCookieSType), reql.userName, msdkInfo.ip, sessionid,
											new Midas.LoginVerifyCallback() {

								@Override
								public void onCallback(int sid, byte err, String userName, String id) {
									gs.getLoginManager().setUserLoginRes(sid, err, userName, id);
								}
							});
						}
					}
					else
					{
						notifyUserLoginFailed(sessionid, GameData.USERLOGIN_FAILED);
					}
				}
			}
			else if( gs.getLoginManager().checkUserLogin(reql) )
				gs.getLoginManager().startCreateRole(sessionid, reql, msdkInfo, rname, headIconID);
		}
	}
	
	public void onChangeName(final int sessionid, final String oldName, final String newName) 
	{
		final Role role = gs.getLoginManager().getLoginRole(sessionid);
		if (role == null || oldName == null || newName == null)
			return;
		
		synchronized (role) {
			if (!role.name.equals(oldName))
				return;
		}
		
		String rname = gs.getLoginManager().checkUserInput(newName, gs.getGameData().getUserInputCFG().roleName, true, true);
		if (rname == null) {
			sendLuaPacket(sessionid, LuaPacket.encodeChangeName(GameData.CHANGENAME_INVALID, newName));
			return;
		}
		
		if( gs.getGameData().isBadName(rname) ) {
			sendLuaPacket(sessionid, LuaPacket.encodeChangeName(GameData.CHANGENAME_INVALID, newName));
			return;
		}
		
		final int rid = role.id;
		gs.getLoginManager().changeRoleNameByRoleID(rid, oldName, rname, new LoginManager.ChangeRoleNameByRoleIDCallback() {
			
			@Override
			public void onCallback(String newName)
			{
				if( newName != null )
				{
					boolean bok = false;
					SBean.DBRoleBrief rb = null;				
					synchronized (role) 
					{
						int cost = gs.getGameData().getRoleCmnCFG().changeNameCost;
						if (role.getStoneWithoutLock() >= cost) 
						{
							role.useStone(cost);
							//role.name = newName;
							role.changeName(newName);
							role.lastSaveTime = -1;
							rb = role.getDBRoleBriefWithoutLock();
							bok = true;
						}
					}
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					TLogger.SecTalkRecord secRecord = new TLogger.SecTalkRecord(TLog.SEC_TALK_NAME, newName);
					tlogEvent.addRecord(secRecord);
					gs.getTLogger().logEvent(role, tlogEvent);
					if (rb != null)
						gs.getLoginManager().actives.put(rb);
					sendLuaPacket(sessionid, LuaPacket.encodeChangeName(bok ? GameData.CHANGENAME_OK : GameData.CHANGENAME_NOMONEY, newName));
				}
				else
				{
					sendLuaPacket(sessionid, LuaPacket.encodeChangeName(GameData.CHANGENAME_EXIST, newName));
				}
			}
		});
	}
	
	private Role getLoginRole(int sessionid)
	{
		Role role = gs.getLoginManager().getLoginRole(sessionid);
		if (role != null)
			return role;
		closeSession(sessionid);
		return null;
	}
	
	public void onUserLogin(int sessionid, SBean.UserLoginRequest req, Role.MSDKInfo msdkInfo)
	{
		//
		if( gs.getLoginManager().getRoleCount() >= gs.getConfig().cap )
		{
			sendLuaPacket(sessionid, LuaPacket.encodeUserLogin(GameData.USERLOGIN_FULL, 0, ""));
			return;
		}
		if( ! (gs.getConfig().verMajor == 0 && gs.getConfig().verMinor == 0 ) 
				&& ! (req.verMajor == 0 && req.verMinor == 0 ) )
		{
			if( gs.getConfig().verMinor != req.verMinor )
			{
				forceClose(sessionid, GameData.FORCE_CLOSE_VERSION_CODE);
				return;
			}
			if( gs.getConfig().verMajor != req.verMajor )
			{
				forceClose(sessionid, GameData.FORCE_CLOSE_VERSION_RES);
				return;
			}
		}
		if( ! gs.getLoginManager().allowOpenID(msdkInfo.openID) )
		{
			sendLuaPacket(sessionid, LuaPacket.encodeUserLogin(GameData.USERLOGIN_RESTART, 0, ""));
			return;
		}
		
		if( req.userName.isEmpty() )
		{
			gs.getLoginManager().genAutoUserID(sessionid);
		}
		else 
		{
			String uname = gs.getLoginManager().checkUserInput(req.userName, gs.getGameData().getUserInputCFG().userName, true, false);
			if( uname != null )
			{
				SBean.UserLoginRequest reql = req;
				gs.getLogger().debug("user start login:" + reql.userName + ", sid=["
						+ reql.key + "], type="+reql.loginType+",deviceID="+reql.deviceID);
				req.userName = uname;
				if( req.loginType == SBean.UserLoginRequest.eLoginNormal )
				{
					if( gs.getMidas().getHTTPTaskQueueSize() >= gs.getConfig().httpTaskMaxCountL1 )
					{
						gs.getLoginManager().startLogin(sessionid, req, msdkInfo);		
					}
					else
					{
						if( gs.getLoginManager().addUserLogin(sessionid, req, msdkInfo, null, (byte)0) )
						{
							if( true )
							{
								gs.getMidas().loginVerify(new Midas.UserInfo(msdkInfo.openID, msdkInfo.openKey, msdkInfo.payToken, msdkInfo.pf, msdkInfo.pfKey,
										msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSIDGuest:gs.getConfig().offerCookieSID,
												msdkInfo.isIOSGuest()?gs.getConfig().offerCookieSTypeGuest:gs.getConfig().offerCookieSType), req.userName, msdkInfo.ip, sessionid,
												new Midas.LoginVerifyCallback() {

									@Override
									public void onCallback(int sid, byte err, String userName, String id) {
										gs.getLoginManager().setUserLoginRes(sid, err, userName, id);
									}
								});
							}
						}
						else
						{
						notifyUserLoginFailed(sessionid, GameData.USERLOGIN_FAILED);
					}
					}
				}
				else if( gs.getLoginManager().checkUserLogin(req) )
					gs.getLoginManager().startLogin(sessionid, req, msdkInfo);		
			}
		}
	}
	
	public void notifyUserLoginFailed(int sid, byte err)
	{
		sendLuaPacket(sid, LuaPacket.encodeUserLogin(err, 0, ""));
	}
	
	private boolean verifySig(final Role role, String data, String[] msg)
	{
		if( gs.getConfig().luaChannelVerifyFlag == 0 )
			return true;
		try
		{
			String sig = msg[msg.length-1];
			String str = data.substring(0, data.length()-1-sig.length()) + String.valueOf(role.id) + "qmctx"; 
			byte[] d1 = str.getBytes("UTF-8");
			byte[] bs1 = new MD5Digester().digest(d1, 0, d1.length);
			String strSig = ket.util.StringConverter.getHexStringFromBytes(bs1);
			return sig.equalsIgnoreCase(strSig);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	private void onForceChannel(final Role role, String[] msg, final int sessionid)
	{
		int msgIndex = 1;
		switch( msg[msgIndex++] )
		{
		case "syncself":
			{
				Role.ForceInfo res = role.getForceInfo();
				sendLuaPacket(sessionid, LuaPacket.encodeSelfForceInfo(res));
			}
			break;
		case "list":
			{
				gs.getForceManager().listForce(0, sessionid, role.id, role.nextListForceLevel());
			}
			break;
		case "stamp":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().getStamp(fid, sessionid, role.id);
			}
			break;
		case "brief":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().getBrief(fid, sessionid, role);
			}
			break;
		case "mobai":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				int type = Integer.parseInt(msg[4]);
				boolean bOK = role.forceMobai(fid, rid, type);
				sendLuaPacket(sessionid, LuaPacket.encodeForceMobaiRes(fid, rid, type, bOK));
			}
			break;
		case "mobaireward":
			{
				int fid = Integer.parseInt(msg[2]);
				int reward = role.forceMobaiTakeReward(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceMobaiRewardRes(fid, reward));
			}
			break;
		case "getpos":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().getPos(fid, sessionid, role.id);
			}
			break;
		case "member":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().getMembers(fid, sessionid, role.id);
			}
			break;
		case "apply":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().getApply(fid, sessionid, role.id);
			}
			break;
		case "logs":
			{
				int fid = Integer.parseInt(msg[2]);
				int ltime = Integer.parseInt(msg[3]);
				gs.getForceManager().getLogs(fid, sessionid, role.id, ltime);
			}
			break;
		case "announce":
			{
				int fid = Integer.parseInt(msg[2]);
				String anc = gs.getLoginManager().checkUserInput(msg[3], gs.getGameData().getUserInputCFG().forceAnnounce, false, true);
				if( anc == null )
					sendLuaPacket(sessionid, LuaPacket.encodeForceAnnounceRes(fid, 0));
				else
				{
					gs.getForceManager().setAnnounce(fid, sessionid, role.id, anc, role);
				}
			}
			break;
		case "qq":
			{
				int fid = Integer.parseInt(msg[2]);
				String qq = gs.getLoginManager().checkUserInput(msg[3], gs.getGameData().getUserInputCFG().forceQQ, false, true);
				if( qq == null )
					sendLuaPacket(sessionid, LuaPacket.encodeForceQQRes(fid, 0));
				else
				{
					gs.getForceManager().setQQ(fid, sessionid, role.id, qq);
				}
			}
			break;
		case "chuanwei":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				gs.getForceManager().chuanwei(fid, sessionid, role, rid);
			}
			break;
		case "accept":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				byte acc = Byte.parseByte(msg[4]);
				gs.getForceManager().accept(fid, sessionid, role, role.id, role.name, rid, acc);
			}
			break;
		case "dismiss":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().dismiss(fid, sessionid, role);
			}
			break;
		case "quit":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().quit(fid, sessionid, role);
			}
			break;
		case "kick":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				gs.getForceManager().kick(fid, sessionid, role, rid);
			}
			break;
		case "setpos":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				byte newpos = Byte.parseByte(msg[4]);
				gs.getForceManager().setPos(fid, sessionid, role, rid, newpos);
			}
			break;
		case "create":
			{
				short iconID = Short.parseShort(msg[2]);
				String fname = gs.getLoginManager().checkUserInput(msg[3], gs.getGameData().getUserInputCFG().forceName, true, true);
				if( fname == null || fname.isEmpty() || gs.getGameData().isBadName(fname) )
				{
					sendLuaPacket(sessionid, LuaPacket.encodeCreateForceRes(-2));
					break;
				}
				if( iconID <= 0 ||
						! role.createForce(iconID, fname) )
					sendLuaPacket(sessionid, LuaPacket.encodeCreateForceRes(0));
			}
			break;
		case "queryid":
			{
				int n1 = gs.getGameData().getUserInputCFG().forceName;
				int n2 = gs.getGameData().getUserInputCFG().roleName;
				String fname = gs.getLoginManager().checkUserInput(msg[2], n1>n2?n1:n2, true, true);
				if( fname == null || fname.isEmpty() )
					sendLuaPacket(sessionid, LuaPacket.encodeQueryForceIDRes(0));
				gs.getLoginManager().queryName(fname, new LoginManager.QueryNameCallback() {
					
					@Override
					public void onCallback(String name, Integer val)
					{
						// TODO Auto-generated method stub
						int fid = 0;
						if( val != null && val.intValue() < 0 )
							fid = -val.intValue();
						sendLuaPacket(sessionid, LuaPacket.encodeQueryForceIDRes(fid));
					}
				});
			}
			break;
		case "join":
			{
				int fid = Integer.parseInt(msg[2]); 
				if ( role.alreadyInForce(fid))
				{
					sendLuaPacket(sessionid, LuaPacket.encodeForceJoin(0, (byte)4));
				}
				else if( ! role.canJoinForce() )
				{
					sendLuaPacket(sessionid, LuaPacket.encodeForceJoin(0, (byte)1));
				}
				else
				{
					gs.getForceManager().join(fid, sessionid, role);
				}
			}
			break;
		case "query":
			{
				int fid = Integer.parseInt(msg[2]);
				if( fid <= 0 )
					sendLuaPacket(sessionid, LuaPacket.encodeQueryForceRes(null));
				gs.getForceManager().queryForce(fid, new ForceManager.QueryForceCallback() {
					
					@Override
					public void onCallback(ForceManager.ForceBrief force)
					{
						sendLuaPacket(sessionid, LuaPacket.encodeQueryForceRes(force));
					}
				});
			}
			break;
		case "seticon":
			{
				int fid = Integer.parseInt(msg[2]); 
				int icon = Integer.parseInt(msg[3]);
				gs.getForceManager().setIcon(fid, sessionid, role.id, icon, role);
			}
			break;
		case "setjoin":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				byte joinMode = Byte.parseByte(msg[msgIndex++]);
				short joinLvlReq = Short.parseShort(msg[msgIndex++]);
				gs.getForceManager().setJoin(fid, sessionid, role.id, joinMode, joinLvlReq, role);
			}
			break;
		case "mail":
			{
				final int fid = Integer.parseInt(msg[msgIndex++]);
				String title = msg[msgIndex++];
				String content = msg[msgIndex++];
				
				gs.getForceManager().sendForceMail(fid, role, title, content, new ForceManager.SendForceMailCallback() 
				{
					
					@Override
					public void onCallback(int errorCode) 
					{
						sendLuaPacket(sessionid, LuaPacket.encodeForceSendMail(fid, errorCode));
					}
				});
			}
			break;
		case "getactivity":
			{
				int fid = Integer.parseInt(msg[2]); 
				gs.getForceManager().getActivity(fid, sessionid, role);
			}
			break;
		case "activityreward":
			{
				int fid = Integer.parseInt(msg[2]); 
				int reward = Integer.parseInt(msg[3]); 
				gs.getForceManager().activityReward(fid, sessionid, role, reward);
			}
			break;
		case "syncbattle":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				gs.getForceManager().getBattleInfo(fid, sessionid, role);
				//notifyForceBattleSync
			}
			break;
		case "openbattle":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int bid = Integer.parseInt(msg[msgIndex++]);
				gs.getForceManager().openBattle(fid, bid, sessionid, role);
				//notifyForceBattleOpen
			}
			break;
		case "joinbattle":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int bid = Integer.parseInt(msg[msgIndex++]);
				int gcount = Integer.parseInt(msg[msgIndex++]);
				List<Short> gids = new ArrayList<Short>();
				for (int i = 0; i < gcount; i ++)
				{
					short gid = Short.parseShort(msg[msgIndex++]);
					if (gid > 0)
						gids.add(gid);
				}
				int pcount = Integer.parseInt(msg[msgIndex++]);
				List<Short> pids = new ArrayList<Short>();
				for (int i = 0; i < pcount; i ++)
				{
					short pid = Short.parseShort(msg[msgIndex++]);
					if (pid > 0)
						pids.add(pid);
				}
				gs.getForceManager().joinBattle(fid, bid, gids, pids, sessionid, role);
				//notifyForceBattlePlayerJoin
			}
			break;
		case "battlechangeseq":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int bid = Integer.parseInt(msg[msgIndex++]);
				int seq = Integer.parseInt(msg[msgIndex++]);
				int change = Integer.parseInt(msg[msgIndex++]);
				gs.getForceManager().changeBattlePlayerSeq(fid, bid, seq, change, sessionid, role);
			}
			break;
		case "synccontribution":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				gs.getForceManager().getBattleContributionInfo(fid, sessionid, role);
				//notifyForceBattleSyncContribution
			}
			break;
		case "battlecontribute":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int bid = Integer.parseInt(msg[msgIndex++]);
				short cid = Short.parseShort(msg[msgIndex++]);
				int ccount = Integer.parseInt(msg[msgIndex++]);
				gs.getForceManager().battleContribute(fid, bid, cid, ccount, sessionid, role);
				//notifyForceBattlePlayerContribute
			}
			break;
		case "battlequiz":
			{
				int bid = Integer.parseInt(msg[msgIndex++]);
				int qid = Integer.parseInt(msg[msgIndex++]);
				int fid = Integer.parseInt(msg[msgIndex++]);
				//gs.getForceBattleManager().quizForceBattle(bid, role.id, qid, win);
				notifyForceBattleQuiz(sessionid, bid, qid, gs.getWarManager().quizCup(bid, role, qid, fid));
				//notifyForceBattleQuiz
			}
			break;
		case "warcalcresult":
			{
				int fid1 = Integer.parseInt(msg[msgIndex++]);
				int fid2 = Integer.parseInt(msg[msgIndex++]);
				byte rIndex1 = Byte.parseByte(msg[msgIndex++]);
				byte rIndex2 = Byte.parseByte(msg[msgIndex++]);
				byte result = Byte.parseByte(msg[msgIndex++]);
				int s1count = Short.parseShort(msg[msgIndex++]);
				int s2count = Short.parseShort(msg[msgIndex++]);
				List<SBean.DBForceWarGeneralStatus> status1 = new ArrayList<SBean.DBForceWarGeneralStatus>();
				List<SBean.DBForceWarGeneralStatus> status2 = new ArrayList<SBean.DBForceWarGeneralStatus>();
				for (int i = 0; i < s1count; i ++) {
					short id = Short.parseShort(msg[msgIndex ++]);
					int hp = Integer.parseInt(msg[msgIndex ++]);
					int mp = Integer.parseInt(msg[msgIndex ++]);
					int st = Integer.parseInt(msg[msgIndex ++]);
					mp = 0;
					SBean.DBForceWarGeneralStatus state = new SBean.DBForceWarGeneralStatus(id, hp, mp, st);
					status1.add(state);
				}
				for (int i = 0; i < s2count; i ++) {
					short id = Short.parseShort(msg[msgIndex ++]);
					int hp = Integer.parseInt(msg[msgIndex ++]);
					int mp = Integer.parseInt(msg[msgIndex ++]);
					int st = Integer.parseInt(msg[msgIndex ++]);
					mp = 0;
					SBean.DBForceWarGeneralStatus state = new SBean.DBForceWarGeneralStatus(id, hp, mp, st);
					status2.add(state);
				}
				gs.getWarManager().setCalcResult(fid1, fid2, rIndex1, rIndex2, result, status1, status2);
			}
			break;
		case "warleaguedata":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.WarData> data = gs.getWarManager().getLeagueData(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarLeagueDataStartRes());
				if (data != null) {
					for (ForceWarManager.WarData d : data)
						sendLuaPacket(sessionid, LuaPacket.encodeForceWarLeagueDataRes(d));
				}
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarLeagueDataEndRes());
			}
			break;
		case "warcupdata":
			{
				List<ForceWarManager.WarData> data = gs.getWarManager().getCupData(role.id);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarCupDataStartRes());
				if (data != null) {
					for (ForceWarManager.WarData d : data)
						sendLuaPacket(sessionid, LuaPacket.encodeForceWarCupDataRes(d));
				}
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarCupDataEndRes());
			}
			break;
		case "wargetrecord":
			{
				int league = Integer.parseInt(msg[msgIndex ++]);
				int selffid = Integer.parseInt(msg[msgIndex ++]);
				int fid1 = Integer.parseInt(msg[msgIndex ++]);
				int fid2 = Integer.parseInt(msg[msgIndex ++]);
				int index1 = Integer.parseInt(msg[msgIndex ++]);
				int index2 = Integer.parseInt(msg[msgIndex ++]);
				boolean[] reverse = {false};
				SBean.DBForceWarMatchRecord record = gs.getWarManager().getRecord(league > 0, fid1, fid2, index1, index2, selffid, reverse);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarGetRecordRes(fid1, fid2, index1, index2, record, reverse[0]));
			}
			break;
		case "warscoreranks":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.ScoreRank> ranks = gs.getWarManager().getScoreRanks(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarScoreRanksRes(fid, ranks));
			}
			break;
		case "warpowerranks":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.PowerRank> ranks = gs.getWarManager().getPowerRanks(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarPowerRanksRes(fid, ranks));
			}
			break;
		case "warrolescoreranks":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.RoleScoreRank> ranks = gs.getWarManager().getRoleScoreRanks(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarRoleScoreRanksRes(fid, ranks));
			}
			break;
		case "wardonateranks":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.DonateRank> ranks = gs.getWarManager().getDonateRanks(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarDonateRanksRes(fid, ranks));
			}
			break;
		case "warcupwinranks":
			{
				int fid = Integer.parseInt(msg[2]);
				List<ForceWarManager.CupWinRank> ranks = gs.getWarManager().getCupWinRanks(fid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceWarCupWinRanksRes(fid, ranks));
			}
			break;
		case "shopsync": // TODO
			{
				Role.ShopInfo info = role.shopForceSync();
				sendLuaPacket(sessionid, LuaPacket.encodeShopForceSync(info));
			}
			break;
		case "shopbuy": // TODO
			{
				byte seq = Byte.parseByte(msg[2]);
				byte type = Byte.parseByte(msg[3]);
				short id = Short.parseShort(msg[4]);
				boolean bOK = role.shopForceBuy(seq, type, id);
				sendLuaPacket(sessionid, LuaPacket.encodeShopForceBuyRes(seq, type, id, bOK));
			}
			break;
		case "shoprefresh": // TODO
			{
				boolean bOK = role.shopForceRefresh();
				sendLuaPacket(sessionid, LuaPacket.encodeShopForceRefreshRes(bOK));
			}
			break;
		case "thiefstartattack":
			{
				int fid = Integer.parseInt(msg[msgIndex ++]);
				int index = Integer.parseInt(msg[msgIndex ++]);
				List<SBean.DBForceWarGeneralStatus> status = new ArrayList<SBean.DBForceWarGeneralStatus>();
				if (role != null) {
					List<SBean.CombatBonus> bonus = new ArrayList<SBean.CombatBonus>();
					int res = gs.getWarManager().startAttackThief(fid, role.id, index, status, bonus);
					SBean.CombatBonus bo = null;
					if (bonus.size() > 0)
						bo = bonus.get(0);
					sendLuaPacket(sessionid, LuaPacket.encodeForceThiefStartAttackRes(fid, index, res, status, bo));						
				}
			}
			break;
		case "thieffinishattack":
			{
				int fid = Integer.parseInt(msg[msgIndex ++]);
				int index = Integer.parseInt(msg[msgIndex ++]);
				int win = Integer.parseInt(msg[msgIndex ++]);
				int damage = Integer.parseInt(msg[msgIndex ++]);
				int count = Integer.parseInt(msg[msgIndex ++]);
				List<Short> gids = new ArrayList<Short>();
				for (int i = 0; i < count; i ++)
					gids.add(Short.parseShort(msg[msgIndex++]));
				count = Integer.parseInt(msg[msgIndex ++]);
				List<SBean.DBForceWarGeneralStatus> status = new ArrayList<SBean.DBForceWarGeneralStatus>();
				for (int i = 0; i < count; i ++) {
					short id = Short.parseShort(msg[msgIndex ++]);
					int hp = Integer.parseInt(msg[msgIndex ++]);
					int mp = Integer.parseInt(msg[msgIndex ++]);
					int st = Integer.parseInt(msg[msgIndex ++]);
					SBean.DBForceWarGeneralStatus state = new SBean.DBForceWarGeneralStatus(id, hp, mp, st);
					status.add(state);
				}
				if (role != null) {
					role.setForceThiefDamage(index, gids, damage);
					List<SBean.CombatBonus> bonus = new ArrayList<SBean.CombatBonus>();
					int res = gs.getWarManager().finishAttackThief(fid, role, index, win>0, damage, status, bonus);
					if (bonus.size() > 0) {
						SBean.CombatBonus cb = bonus.get(0);
						role.addCombatBonus(cb);
					}
					sendLuaPacket(sessionid, LuaPacket.encodeForceThiefFinishAttackRes(fid, index, win, res));
				}
			}
			break;
		case "thiefdamageranks":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				List<ForceWarManager.DamageRank> ranks = gs.getWarManager().getDamageRanks(fid, rid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceThiefDamageRanksRes(rid, ranks));
			}
			break;
		case "beastsync":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getWarManager().beastSync(sessionid, fid);
			}
			break;
		case "beastresetattackprop":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getWarManager().beastResetAttackProp(role, sessionid, fid, gid);
			}
			break;
		case "beastsetdefault":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				byte def = Byte.parseByte(msg[4]);
				gs.getWarManager().beastSetDefault(role.id, sessionid, fid, gid, def);
			}
			break;
		case "beastupgradeskill":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				byte skill = Byte.parseByte(msg[4]);
				gs.getWarManager().beastUpgradeSkill(role.id, sessionid, fid, gid, skill);
			}
			break;
		case "beastupgradeprop":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				byte prop = Byte.parseByte(msg[4]);
				gs.getWarManager().beastUpgradeProp(role.id, sessionid, fid, gid, prop);
			}
			break;
		case "beastresetskill":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getWarManager().beastResetSkill(role.id, sessionid, fid, gid);
			}
			break;
		case "beastresetprop":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getWarManager().beastResetProp(role, sessionid, fid, gid);
			}
			break;
		case "beastupgradeevo":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getWarManager().beastUpgradeEvo(role.id, sessionid, fid, gid);
			}
			break;
		case "beastfeed":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getWarManager().beastFeed(role, sessionid, fid, gid);
			}
			break;
		case "dungeonsync":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().dungeonSync(sessionid, fid);
			}
			break;
		case "dungeonsyncchapter":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				chapter --;
				gs.getForceManager().dungeonSyncChapter(sessionid, fid, chapter, role.id);
			}
			break;
		case "dungeonfighting":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				chapter --;
				gs.getForceManager().dungeonFighting(sessionid, fid, chapter);
			}
			break;
		case "dungeonactivatechapter":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				chapter --;
				gs.getForceManager().dungeonActivateChapter(sessionid, fid, chapter, role);
			}
			break;
		case "dungeonstartattack":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				byte stage = Byte.parseByte(msg[4]);
				chapter --;
				stage --;
				gs.getForceManager().dungeonStartAttack(sessionid, role, fid, chapter, stage);
			}
			break;
		case "dungeonfinishattack":
			{
				int fid = Integer.parseInt(msg[msgIndex ++]);
				byte chapter = Byte.parseByte(msg[msgIndex ++]);
				byte stage = Byte.parseByte(msg[msgIndex ++]);
				chapter --;
				stage --;
				int win = Integer.parseInt(msg[msgIndex ++]);
				int damage = Integer.parseInt(msg[msgIndex ++]);
				int total = Integer.parseInt(msg[msgIndex ++]);
				int count = Integer.parseInt(msg[msgIndex ++]);
				List<Short> gids = new ArrayList<Short>();
				for (int i = 0; i < count; i ++)
					gids.add(Short.parseShort(msg[msgIndex++]));
				count = Integer.parseInt(msg[msgIndex ++]);
				List<SBean.DBForceChapterGeneralStatus> status = new ArrayList<SBean.DBForceChapterGeneralStatus>();
				for (int i = 0; i < count; i ++) {
					short id = Short.parseShort(msg[msgIndex ++]);
					int hp = Integer.parseInt(msg[msgIndex ++]);
					int mp = Integer.parseInt(msg[msgIndex ++]);
					int st = Integer.parseInt(msg[msgIndex ++]);
					SBean.DBForceChapterGeneralStatus state = new SBean.DBForceChapterGeneralStatus(id, hp, mp, st);
					status.add(state);
				}
				//role.setForceChapterDamage(chapter, gids, damage);
				gs.getForceManager().dungeonFinishAttack(sessionid, role, fid, chapter, stage, win>0, status, damage, total, gids);
			}
			break;
		case "dungeonresetchapter":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				chapter --;
				gs.getForceManager().dungeonResetChapter(sessionid, role.id,role.name, fid, chapter);
			}
			break;
		case "dungeonapplytreasure":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				byte type = Byte.parseByte(msg[4]);
				short id = Short.parseShort(msg[5]);
				chapter --;
				gs.getForceManager().dungeonApplyTreasure(sessionid, role, fid, chapter, type, id);
			}
			break;
		case "dungeonlisttreasure":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				chapter --;
				gs.getForceManager().dungeonListTreasure(sessionid, fid, role.id, chapter);
			}
			break;
		case "dungeonlistapply":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				byte type = Byte.parseByte(msg[4]);
				short id = Short.parseShort(msg[5]);
				chapter --;
				gs.getForceManager().dungeonListApply(sessionid, fid, chapter, type, id);
			}
			break;
		case "dungeonkinglisttreasure":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().dungeonKingListTreasure(sessionid, fid, role.id);
			}
			break;
		case "dungeonkinglistapply":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				byte type = Byte.parseByte(msg[4]);
				short id = Short.parseShort(msg[5]);
				chapter --;
				gs.getForceManager().dungeonKingListApply(sessionid, fid, chapter, type, id);
			}
			break;
		case "dungeonkingdeploy":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				byte chapter = Byte.parseByte(msg[4]);
				byte type = Byte.parseByte(msg[5]);
				short id = Short.parseShort(msg[6]);
				chapter --;
				gs.getForceManager().dungeonKingDeploy(sessionid, fid, rid, chapter, type, id);
			}
			break;
		case "dungeontreasurelog":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().dungeonTreasureLog(sessionid, fid);
			}
			break;
		case "dungeondamageranks":
			{
				byte chapter = Byte.parseByte(msg[2]);
				int fid = Integer.parseInt(msg[3]);
				int rid = Integer.parseInt(msg[4]);
				chapter --;
				List<ForceManager.DamageRank> ranks = gs.getForceManager().getDamageRanks(chapter, fid, rid);
				sendLuaPacket(sessionid, LuaPacket.encodeForceDungeonDamageRanksRes(chapter, fid, rid, ranks));
			}
			break;
		case "dungeonopenbox":
			{
				int fid = Integer.parseInt(msg[2]);
				byte chapter = Byte.parseByte(msg[3]);
				byte stage = Byte.parseByte(msg[4]);
				chapter --;
				stage --;
				gs.getForceManager().dungeonOpenBox(sessionid, role, fid, chapter, stage);
			}
			break;
		case "upgrade":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().upgrade(fid, sessionid, role);
			}
			break;
		case "mercsyncself":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().mercSyncSelf(sessionid, fid, role);
			}
			break;
		case "mercsync":
			{
				int fid = Integer.parseInt(msg[2]);
				gs.getForceManager().mercSync(sessionid, fid, role);
			}
			break;
		case "mercshare":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getForceManager().mercShare(sessionid, fid, gid, role);
			}
			break;
		case "mercwithdraw":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getForceManager().mercWithdraw(sessionid, fid, gid, role);
			}
			break;
		case "merchire":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				short gid = Short.parseShort(msg[4]);
				gs.getForceManager().mercHire(sessionid, fid, rid, gid, role);
			}
			break;
		case "mercmarchhire":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				short gid = Short.parseShort(msg[4]);
				gs.getForceManager().mercMarchHire(sessionid, fid, rid, gid, role);
			}
			break;
		case "mercsurahire":
			{
				int fid = Integer.parseInt(msg[2]);
				int rid = Integer.parseInt(msg[3]);
				short gid = Short.parseShort(msg[4]);
				gs.getForceManager().mercSuraHire(sessionid, fid, rid, gid, role);
			}
			break;
		case "mercremovecitygeneral":
			{
				int fid = Integer.parseInt(msg[2]);
				short gid = Short.parseShort(msg[3]);
				gs.getForceManager().mercRemoveCityGeneral(sessionid, fid, gid, role);
			}
			break;
		case "swarsync":
			{
				String serverName = msg[2];
				if (role != null && role.forceInfo.id > 0) {
					int status[] = {0, 0};
					SBean.DBForceSWar swar = gs.getSWarManager().sync(role.forceInfo.id, role.forceInfo.iconId, role.forceInfo.name, serverName, status);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSyncBegin(role.id, swar, status));
					for (SBean.DBForceSWarBuilding b : swar.brief.buildings) {
						List<SBean.DBForceSWarTeam> teams = new ArrayList<SBean.DBForceSWarTeam>();
						for (SBean.DBForceSWarTeam t : b.teams) {
							teams.add(t);
							if (teams.size() >= 50) {
								sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSyncTeams(b.bid, teams));
								teams.clear();
							}
						}
						if (teams.size() > 0)
							sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSyncTeams(b.bid, teams));
					}
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSyncEnd());
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSyncBegin(0, null, null));
			}
			break;
		case "swarlogs":
			{
				if (role != null && role.forceInfo.id > 0) {
					List<SBean.DBForceSWarLog> logs = gs.getSWarManager().getLogs(role.forceInfo.id);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarLogs(logs));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarLogs(null));
			}
			break;
		case "swarsearch":
			{
				gs.getSWarManager().search(role);
			}
			break;
		case "swarviewoppos":
			{
				if (role != null && role.forceInfo.id > 0) {
					List<SBean.DBForceSWarOppo> oppos = gs.getSWarManager().getOppos(role.forceInfo.id);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarViewOppos(gs, role.id, oppos));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarViewOppos(gs, 0, null));
			}
			break;
		case "swarviewoppobuilding":
			{
				int fid = Integer.parseInt(msg[2]);
				int sid = Integer.parseInt(msg[3]);
				byte bid = Byte.parseByte(msg[4]);
				if (role != null && role.forceInfo.id > 0) {
					SBean.DBForceSWarBuilding building = gs.getSWarManager().getOppoBuilding(role.forceInfo.id, fid, sid, bid);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarViewOppoBuilding(fid, sid, bid, building));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarViewOppoBuilding(fid, sid, bid, null));
			}
			break;
		case "swarvote":
			{
				byte oindex = Byte.parseByte(msg[msgIndex++]);
				if (role != null && role.forceInfo.id > 0) {
					boolean ok = gs.getSWarManager().vote(role, oindex);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarVote(oindex, ok));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarVote(oindex, false));
			}
			break;
		case "swarsetteam":
			{
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				int gcount = Integer.parseInt(msg[msgIndex++]);
				List<Short> gids = new ArrayList<Short>();
				for (int i = 0; i < gcount; i ++)
				{
					short gid = Short.parseShort(msg[msgIndex++]);
					if (gid > 0)
						gids.add(gid);
				}
				int pcount = Integer.parseInt(msg[msgIndex++]);
				List<Short> pids = new ArrayList<Short>();
				for (int i = 0; i < pcount; i ++)
				{
					short pid = Short.parseShort(msg[msgIndex++]);
					if (pid > 0)
						pids.add(pid);
				}
				bindex --;
				if (role != null && role.forceInfo.id > 0) {
					boolean ok = gs.getSWarManager().setTeam(role, bid, bindex, gids, pids);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSetTeam(bid, bindex, ok));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarSetTeam(bid, bindex, false));
			}
			break;
		case "swarstartattack":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int sid = Integer.parseInt(msg[msgIndex++]);
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				List<Short> generals = new ArrayList<Short>();
				for (int i = 0; i < 5; i ++)
					generals.add(Short.parseShort(msg[msgIndex++]));
				List<Short> pets = new ArrayList<Short>();
				pets.add(Short.parseShort(msg[msgIndex++]));
				bindex --;
				gs.getSWarManager().startAttack(sessionid, role, fid, sid, bid, bindex, generals, pets);
			}
			break;
		case "swarfinishattack":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int sid = Integer.parseInt(msg[msgIndex++]);
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				byte win = Byte.parseByte(msg[msgIndex++]);
				bindex --;
				int bufCount = Integer.parseInt(msg[msgIndex++]);
				List<SBean.DBForceSWarBuf> attackBufs = new ArrayList<SBean.DBForceSWarBuf>();
				for (int i = 0; i < bufCount; i ++) {
					SBean.DBForceSWarBuf buf = new SBean.DBForceSWarBuf();
					buf.id = Short.parseShort(msg[msgIndex++]);
					buf.type = Byte.parseByte(msg[msgIndex++]);
					buf.value = Float.parseFloat(msg[msgIndex++]);
					attackBufs.add(buf);
				}
				bufCount = Integer.parseInt(msg[msgIndex++]);
				List<SBean.DBForceSWarBuf> defendBufs = new ArrayList<SBean.DBForceSWarBuf>();
				for (int i = 0; i < bufCount; i ++) {
					SBean.DBForceSWarBuf buf = new SBean.DBForceSWarBuf();
					buf.id = Short.parseShort(msg[msgIndex++]);
					buf.type = Byte.parseByte(msg[msgIndex++]);
					buf.value = Float.parseFloat(msg[msgIndex++]);
					defendBufs.add(buf);
				}
				gs.getSWarManager().finishAttack(sessionid, role, fid, sid, bid, bindex, win, attackBufs, defendBufs);
			}
			break;
		case "swarrecordlist":
			{
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				bindex --;
				if (role != null && role.forceInfo.id > 0) {
					List<SBean.DBForceSWarMatchRecord> records = gs.getSWarManager().getRecordList(role.forceInfo.id, bid, bindex);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarRecordList(bid, bindex, records));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarRecordList(bid, bindex, null));
			}
			break;
		case "swarrecord":
			{
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				short mindex = Short.parseShort(msg[msgIndex++]);
				bindex --;
				mindex --;
				if (role != null && role.forceInfo.id > 0) {
					SBean.DBForceSWarMatchRecord record = gs.getSWarManager().getRecord(role.forceInfo.id, bid, bindex, mindex);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarRecord(bid, bindex, mindex, record));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarRecord(bid, bindex, mindex, null));
			}
			break;
		case "swaropporecordlist":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int sid = Integer.parseInt(msg[msgIndex++]);
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				bindex --;
				if (role != null && role.forceInfo.id > 0) {
					List<SBean.DBForceSWarMatchRecord> records = gs.getSWarManager().getOppoRecordList(role.forceInfo.id, fid, sid, bid, bindex);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarOppoRecordList(fid, sid, bid, bindex, records));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarOppoRecordList(fid, sid, bid, bindex, null));
			}
			break;
		case "swaropporecord":
			{
				int fid = Integer.parseInt(msg[msgIndex++]);
				int sid = Integer.parseInt(msg[msgIndex++]);
				byte bid = Byte.parseByte(msg[msgIndex++]);
				short bindex = Short.parseShort(msg[msgIndex++]);
				short mindex = Short.parseShort(msg[msgIndex++]);
				bindex --;
				mindex --;
				if (role != null && role.forceInfo.id > 0) {
					SBean.DBForceSWarMatchRecord record = gs.getSWarManager().getOppoRecord(role.forceInfo.id, fid, sid, bid, bindex, mindex);
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarOppoRecord(fid, sid, bid, bindex, mindex, record));
				}
				else
					sendLuaPacket(sessionid, LuaPacket.encodeForceSWarOppoRecord(fid, sid, bid, bindex, mindex, null));
			}
			break;
		case "swarrank":
			{
				gs.getSWarManager().rank(sessionid, role);
			}
			break;
		case "swartopranks":
			{
				gs.getSWarManager().topRanks(sessionid, role);
			}
			break;
		case "swardonatemoney":
			{
				int count = Integer.parseInt(msg[msgIndex++]);
				gs.getSWarManager().donateMoney(sessionid, role, count);
			}
			break;
		case "swardonateitems":
			{
				gs.getSWarManager().donateItems(sessionid, role);
			}
			break;
		case "swardonateranks":
			{
				int history = Integer.parseInt(msg[msgIndex++]);
				gs.getSWarManager().donateRanks(sessionid, role, history > 0);
			}
			break;
		default:
			break;
		}

	}
	
	public void onLuaChannelList(final Role role, String dataOrg, String[] msg, final int sessionid)
	{
		switch( msg[0] )
		{
		case "shopnormal":
			{
				onShopNormal(role, msg, sessionid);
			}
			break;
		case "qqshare":
			{
				switch( msg[1] )
				{
				case "report":
					{
						role.qqShareReport();
					}
					break;
				default:
					break;
				}
			}
			break;
		case "shoprand":
			{
				onShopRand(role, msg, sessionid);
			}
			break;
		case "combatevent":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						byte ceid = Byte.parseByte(msg[2]);
						SBean.DBCombatEventLog log = role.combatEventSync(ceid);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatEventSync(ceid, log));
					}
					break;
				case "start":
					{
						int msgIndex = 2;
						byte ceid = Byte.parseByte(msg[msgIndex++]);
						byte celvl = Byte.parseByte(msg[msgIndex++]);
						short cid = Short.parseShort(msg[msgIndex++]);
						int startTime = Integer.parseInt(msg[msgIndex++]);
						byte nGeneral = Byte.parseByte(msg[msgIndex++]);
						List<Short> generalIDs = new ArrayList<Short>();
						for(byte i = 0; i < nGeneral; ++i)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							int rid = Integer.parseInt(msg[msgIndex++]);
							if (rid == 0)
								generalIDs.add(gid);
						}
						SBean.CombatBonus[] cb = new SBean.CombatBonus[1];
						boolean bOK = role.combatEventStart(ceid, celvl, cid, cb, startTime, generalIDs);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatEventStartRes(ceid, celvl, cid, bOK, cb[0]));
					}
					break;
				case "finish":
					{
						int msgIndex = 2;
						byte ceid = Byte.parseByte(msg[msgIndex++]);
						byte celvl = Byte.parseByte(msg[msgIndex++]);
						short cid = Short.parseShort(msg[msgIndex++]);
						byte star = Byte.parseByte(msg[msgIndex++]);
						// TODO
						if( star > 0 && dataOrg != null && ! verifySig(role, dataOrg, msg) )
							break;
						//
						int costTime = Integer.parseInt(msg[msgIndex++]);
						int endTime = Integer.parseInt(msg[msgIndex++]);
						byte nGeneral = Byte.parseByte(msg[msgIndex++]);
						List<Short> generalIDs = new ArrayList<Short>();
						for(byte i = 0; i < nGeneral; ++i)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							int rid = Integer.parseInt(msg[msgIndex++]);
							if (rid == 0)
								generalIDs.add(gid);
						}  
						int waveCount = Integer.parseInt(msg[msgIndex++]);;
						int dpsTotal = Integer.parseInt(msg[msgIndex++]);
						int clientSpeed = Integer.parseInt(msg[msgIndex++]);
						int clientMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int clientKillMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int pauseTimeTotal = Integer.parseInt(msg[msgIndex++]);
						boolean bOK = role.combatEventFinish(ceid, celvl, cid, star < 0 ? 0 : star, costTime, endTime, generalIDs, waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatEventFinishRes(ceid, celvl, cid, star, generalIDs, bOK));
					}
					break;
				case "skip":
					{
						int msgIndex = 2;
						byte ceid = Byte.parseByte(msg[msgIndex++]);
						byte celvl = Byte.parseByte(msg[msgIndex++]);
						short cid = Short.parseShort(msg[msgIndex++]);
						byte nTimes = Byte.parseByte(msg[msgIndex++]);
						if( nTimes != 1 )
							break;
						SBean.CombatBonus[] cbs = new SBean.CombatBonus[nTimes+1];
						boolean bOK = role.combatEventSkip(ceid, celvl, cid, nTimes, cbs);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatEventSkipRes(ceid, celvl, cid, nTimes, bOK, cbs));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "combats":
			{
				switch( msg[1] )
				{
				case "start":
					{
						int msgIndex = 2;
						byte ctype = Byte.parseByte(msg[msgIndex++]);
						byte cindex = Byte.parseByte(msg[msgIndex++]);
						byte bindex = Byte.parseByte(msg[msgIndex++]);
						short cid = Short.parseShort(msg[msgIndex++]);
						int startTime = Integer.parseInt(msg[msgIndex++]);
						byte nGeneral = Byte.parseByte(msg[msgIndex++]);
						List<Short> generalIDs = new ArrayList<Short>();
						for(byte i = 0; i < nGeneral; ++i)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							int rid = Integer.parseInt(msg[msgIndex++]);
							if (rid == 0)
								generalIDs.add(gid);
						}
						SBean.CombatBonus[] cb = new SBean.CombatBonus[1];
						int[] normalDropCnt = new int[1];
						boolean bOK = role.combatsStart(ctype, cindex, bindex, cid, cb, normalDropCnt, startTime, generalIDs);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatsStartRes(ctype, cindex, bindex, cid, bOK, cb[0], normalDropCnt[0]));
					}
					break;
				case "finish":
					{
						int msgIndex = 2;
						byte ctype = Byte.parseByte(msg[msgIndex++]);
						byte cindex = Byte.parseByte(msg[msgIndex++]);
						byte bindex = Byte.parseByte(msg[msgIndex++]);
						short cid = Short.parseShort(msg[msgIndex++]);
						byte star = Byte.parseByte(msg[msgIndex++]);
						// TODO
						if( star > 0 && dataOrg != null && ! verifySig(role, dataOrg, msg) )
							break;
						//
						int costTime = Integer.parseInt(msg[msgIndex++]);
						int endTime = Integer.parseInt(msg[msgIndex++]);
						byte nGeneral = Byte.parseByte(msg[msgIndex++]);
						List<Short> generalIDs = new ArrayList<Short>();
						for(byte i = 0; i < nGeneral; ++i)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							int rid = Integer.parseInt(msg[msgIndex++]);
							if (rid == 0)
								generalIDs.add(gid);
						}
						int waveCount = Integer.parseInt(msg[msgIndex++]);;
						int dpsTotal = Integer.parseInt(msg[msgIndex++]);
						int clientSpeed = Integer.parseInt(msg[msgIndex++]);
						int clientMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int clientKillMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int pauseTimeTotal = Integer.parseInt(msg[msgIndex++]);
						boolean bOK = role.combatsFinish(ctype, cindex, bindex, cid, star < 0 ? 0 : star, costTime, endTime, generalIDs, waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal);
						//
						if( bOK && star > 0 )
						{
							if( ctype == 0 )
							{
								if( role.shopRandTest(cid, (byte)1, 1) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)1));			
								}
							}
							if( ctype == 1 )
							{
								if( role.shopRandTest(cid, (byte)1, 1) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)1));			
								}
								if( role.shopRandTest(cid, (byte)2, 1) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)2));			
								}
							}
						}
						//
						sendLuaPacket(sessionid, LuaPacket.encodeCombatsFinishRes(ctype, cindex, bindex, cid, star, generalIDs, bOK, role.getQQVIP()));
						// TODO !!!
						if( bOK && ctype == 0 && cindex == 0 && bindex == 0 && star > 0 )
						{
							role.testPetEggAndUse();
						}
					}
					break;
				case "skip":
					{
						byte ctype = Byte.parseByte(msg[2]);
						byte cindex = Byte.parseByte(msg[3]);
						byte bindex = Byte.parseByte(msg[4]);
						short cid = Short.parseShort(msg[5]);
						byte nTimes = Byte.parseByte(msg[6]);
						final int nMaxTimes = 10;
						if( nTimes <= 0 || nTimes > nMaxTimes )
							break;
						SBean.CombatBonus[] cbs = new SBean.CombatBonus[nMaxTimes+1];
						boolean bOK = role.combatsSkip(ctype, cindex, bindex, cid, nTimes, cbs);
						//
						if( bOK )
						{
							if( ctype == 0 )
							{
								if( role.shopRandTest(cid, (byte)1, nTimes) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)1));			
								}
							}
							if( ctype == 1 )
							{
								if( role.shopRandTest(cid, (byte)1, nTimes) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)1));			
								}
								if( role.shopRandTest(cid, (byte)2, nTimes) )
								{
									sendLuaPacket(sessionid, LuaPacket.encodeShopRandNotice((byte)2));			
								}
							}
						}
						//
						sendLuaPacket(sessionid, LuaPacket.encodeCombatsSkipRes(ctype, cindex, bindex, cid, nTimes, bOK, role.getQQVIP(), cbs));
					}
					break;
				case "reset":
					{
						short cid = Short.parseShort(msg[2]);
						boolean bOK = role.combatsReset(cid);
						sendLuaPacket(sessionid, LuaPacket.encodeCombatsResetRes(cid, bOK));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "combatdata":
			{
				switch( msg[1] )
				{
				case "syncscore":
					{
						byte ctype = Byte.parseByte(msg[2]);
						byte cindex = Byte.parseByte(msg[3]);
						SBean.DBCombatTypeScore ts = role.copyCombatScoreWithLock(ctype, cindex);
						if( ts == null )
							ts = new SBean.DBCombatTypeScore(cindex, SBean.DBCombatTypeScore.eNull, new ArrayList<Byte>());
						sendLuaPacket(sessionid, LuaPacket.encodeCombatScore(ctype, cindex, ts));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "friend":
			{
				switch( msg[1] )
				{
				case "searchfriends":
					{
						int n = Integer.parseInt(msg[2]);
						List<String> ids = new ArrayList<String>();
						for(int i = 0; i < n; ++i)
						{
							ids.add(msg[3+i]);
						}
						gs.getExchangeManager().searchFriends(role.id, ids);
					}
					break;
				case "syncstate":
					{						
						sendLuaPacket(sessionid, LuaPacket.encodeFriendSyncState(role.copyDBFriendWithLock().removeInvalid()));
					}
					break;
				case "mailbaozi":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendMailBaoziRes(openID, serverID, roleID, role.friendMailBaozi(serverID, roleID)));
					}
					break;
				case "mailallbaozi":
					{	
						sendLuaPacket(sessionid, LuaPacket.encodeFriendMailAllBaoziRes(role.friendMailAllBaozi()));
					}
					break;
				case "mailpetexp":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendMailPetExpRes(openID, serverID, roleID, role.friendMailPetExp(serverID, roleID)));
					}
					break;
				case "batchmailpetexp":
					{	
						String openID = msg[2];
						List<SBean.GlobalRoleID> ids = new ArrayList<SBean.GlobalRoleID>();
						int count = Integer.parseInt(msg[3]);
						int index = 4;
						for (int i = 0; i < count; i ++) {
							int serverID = Integer.parseInt(msg[index++]);
							int roleID = Integer.parseInt(msg[index++]);
							ids.add(new SBean.GlobalRoleID(serverID, roleID));
						}
						sendLuaPacket(sessionid, LuaPacket.encodeFriendBatchMailPetExpRes(openID, role.friendBatchMailPetExp(ids)));
					}
					break;
				case "recall":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendRecallRes(openID, serverID, roleID, role.friendRecall(serverID, roleID)));
					}
					break;
				case "recallall":
					{	
						List<String> openIDs = new ArrayList<String>();
						int moneyAll = role.friendRecallAll(openIDs);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendRecallAllRes(moneyAll, openIDs));
					}
					break;
				case "syncmessage":
					{	
						sendLuaPacket(sessionid, LuaPacket.encodeFriendSyncMessage(role.copyDBFriendWithLock()));
					}
					break;
				case "syncheart":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeFriendSyncHeart(role.friendSyncHeart()));
					}
					break;
				case "eatbaozi":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						Integer time = Integer.parseInt(msg[5]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendEatBaoziRes(openID, serverID, roleID, time, role.friendEatBaozi(serverID, roleID, time)));
					}
					break;
				case "takebreedreward":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						Integer time = Integer.parseInt(msg[5]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendTakeBreedRewardRes(openID, serverID, roleID, time, role.friendTakeBreedReward(serverID, roleID, time)));
					}
					break;
				case "takepetexp":
					{	
						String openID = msg[2];
						int serverID = Integer.parseInt(msg[3]);
						int roleID = Integer.parseInt(msg[4]);
						Integer time = Integer.parseInt(msg[5]);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendTakePetExpRes(openID, serverID, roleID, time, role.friendTakePetExp(serverID, roleID, time)));
					}
					break;
				case "eatallbaozi":
					{	
						List<SBean.DropEntry> items = new ArrayList<SBean.DropEntry>();
						int ret[] = new int[2];
						boolean bOK = role.friendTakeAll(items, ret);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendEatAllBaoziRes(bOK, ret[0], ret[1], items));
					}
					break;
				case "apply":
					{
						int targetRoleID = Integer.parseInt(msg[2]);
						role.friendApply(targetRoleID);
					}
					break;
				case "accept":
					{
						int applyRoleID = Integer.parseInt(msg[2]);
						role.friendAccept(applyRoleID);
					}
					break;
				case "refuse":
					{
						int applyRoleID = Integer.parseInt(msg[2]);
						boolean bOK = role.friendRefuse(applyRoleID);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendRefuseRes(applyRoleID, bOK));
					}
					break;
				case "del":
					{
						int friendServerID = Integer.parseInt(msg[2]);
						int friendRoleID = Integer.parseInt(msg[3]);
						boolean bOK = role.friendDel(friendServerID, friendRoleID);
						sendLuaPacket(sessionid, LuaPacket.encodeFriendDelRes(friendServerID, friendRoleID, bOK));
					}
					break;
				case "undelall":
					{
						int count = role.friendUndelAll();
						sendLuaPacket(sessionid, LuaPacket.encodeFriendUndelAllRes(count));
					}
					break;
				case "syncupdate":
					{
						int friendServerID = Integer.parseInt(msg[2]);
						int friendRoleID = Integer.parseInt(msg[3]);
						role.friendSyncUpdate(friendServerID, friendRoleID);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "randomroles":
			{
				switch( msg[1] )
				{
				case "find":
					{
						String rname = msg[2].toLowerCase();
						gs.getLoginManager().queryName(rname, new LoginManager.QueryNameCallback() {
							
							@Override
							public void onCallback(String name, Integer val)
							{
								// TODO Auto-generated method stub
								int roleID = -1;
								if( val != null && val.intValue() > 0 )
									roleID = val.intValue();
								if( roleID > 0 )
									gs.getLoginManager().getRoleByID(sessionid, roleID);
								else
									notifyGetRoleByIDResult(sessionid, null);
							}
						});
					}	
					break;
				default:
					break;
				}
			}
			break;
		case "pets":
			{
				onPetMessage(role, msg, sessionid);
			}
			break;
		case "generals":
			{
				switch( msg[1] )
				{
				case "setequip":
					{
						short gid = Short.parseShort(msg[2]);
						short eid = Short.parseShort(msg[3]);
						byte pos = Byte.parseByte(msg[4]);
						boolean bOK = role.generalsSetEquip(gid, eid, pos);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsSetEquipRes(gid, eid, pos, bOK));
					}
					break;
				case "gildequip":
					{
						short gid = Short.parseShort(msg[2]);
						short eid = Short.parseShort(msg[3]);
						byte pos = Byte.parseByte(msg[4]);
						List<SBean.DropEntryNew> matList = new ArrayList<SBean.DropEntryNew>();
						int n = Integer.parseInt(msg[5]);
						for(int i = 0; i < n; ++i)
						{
							SBean.DropEntryNew e = new SBean.DropEntryNew(Byte.parseByte(msg[6+3*i]), Short.parseShort(msg[6+3*i+1]), Integer.parseInt(msg[6+3*i+2]));
							if( e.type != GameData.COMMON_TYPE_EQUIP && e.type != GameData.COMMON_TYPE_ITEM )
								return;
							matList.add(e);
						}
						boolean bOK = role.generalsGildEquip(gid, eid, pos, matList);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsGildEquipRes(gid, eid, pos, matList, bOK));
					}
					break;
				case "upgradeadv":
					{
						short gid = Short.parseShort(msg[2]);
						List<SBean.DropEntryNew> drops = new ArrayList<SBean.DropEntryNew>();
						int[] err = {0};
						boolean bOK = role.generalsUpgradeAdv(gid, drops, err);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsUpgradeAdvRes(gid, bOK, drops, err[0]));
					}
					break;
				case "upgradeevo":
					{
						short gid = Short.parseShort(msg[2]);
						boolean bOK = role.generalsUpgradeEvo(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsUpgradeEvoRes(gid, bOK));
					}
					break;
				case "firstevo":
					{
						short gid = Short.parseShort(msg[2]);
						boolean bOK = role.generalsFirstEvo(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsFirstEvoRes(gid, bOK));
					}
					break;
				case "upgradeskill":
					{
						short gid = Short.parseShort(msg[2]);
						byte pos = Byte.parseByte(msg[3]);
						boolean bOK = role.generalsUpgradeSkill(gid, pos);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsUpgradeSkillRes(gid, pos, bOK));
					}
					break;
				case "activeweapon":
					{
						short gid = Short.parseShort(msg[2]);
						boolean bOK = role.generalsActiveWeapon(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsActiveWeaponRes(gid, bOK));						
					}
					break;
				case "enhanceweapon":
					{
						short gid = Short.parseShort(msg[2]);
						short stage = role.generalsEnhanceWeapon(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsEnhanceWeaponRes(gid, stage));
					}
					break;
				case "evoweapon":
					{
						short gid = Short.parseShort(msg[2]);
						byte evo = role.generalsEvoWeapon(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsEvoWeaponRes(gid, evo));	
					}
					break;
				case "resetweapon":
					{
						short gid = Short.parseShort(msg[2]);
						byte lockCount = Byte.parseByte(msg[3]);
						Set<Integer> lock = new TreeSet<Integer>();
						int index = 4;
						for (byte i = 0; i < lockCount; i ++)
							lock.add(Integer.parseInt(msg[index ++]));
						Set<Integer> lock2 = new TreeSet<Integer>();
						byte lockCount2 = Byte.parseByte(msg[index++]);
						for (byte i = 0; i < lockCount2; i ++)
							lock2.add(Integer.parseInt(msg[index ++]));
						List<SBean.DBWeaponProp> props = new ArrayList<SBean.DBWeaponProp>();
						boolean bOK = role.generalsResetWeapon(gid, lock,lock2, props);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsResetWeaponRes(gid, bOK, props));						
					}
					break;
				case "saveweapon":
					{
						short gid = Short.parseShort(msg[2]);
						List<SBean.DBWeaponProp> props = new ArrayList<SBean.DBWeaponProp>();
						boolean bOK = role.generalsSaveResetWeapon(gid, props);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsSaveResetWeaponRes(gid, bOK, props));	
					}
					break;
				case "syncSeyen":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeRoleSeyen(role.getRoleSeyen()));	
					}
				    break;	
				case "sync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsSync(role.copyGeneralsWithoutLock()));	
					}
			    break;	    
				case "addSeyenExp":
					{
						short gid = Short.parseShort(msg[2]);
						byte  type = Byte.parseByte(msg[3]);
						List<Short> props = new ArrayList<Short>();
						boolean bOK = role.generalsAddSeyenExp(gid,type, props);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsAddSeyenExpRes(gid, type,bOK, props));	
					}
					break;
				case "revertSeyen":
					{
						short gid = Short.parseShort(msg[2]);
						boolean bOK = role.generalsRevertSeyen(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeRevertSeyenRes(gid, bOK));	
					}
					break;
				case "syncofficial":
					{
						short gid = Short.parseShort(msg[2]);
						SBean.DBGeneralOfficial official = role.getGeneralOfficial(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsSyncOfficialRes(gid, official));	
					}
				    break;	
				case "addofficialexp":
					{
						short gid = Short.parseShort(msg[2]);
						int itemCount = Integer.parseInt(msg[3]);
						int[] itemUsed = {0};
						boolean ok = role.addOfficialExp(gid, itemCount, itemUsed);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsAddOfficialExpRes(gid, itemCount, ok, itemUsed[0]));	
					}
					break;
				case "upgradeofficialskill":
					{
						short gid = Short.parseShort(msg[2]);
						short targetLvl = Short.parseShort(msg[3]);
						int skillLvl = role.upgradeOfficialSkill(gid, targetLvl);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsUpgradeOfficialSkillRes(gid, skillLvl));	
					}
					break;
				case "resetofficial":
					{
						short gid = Short.parseShort(msg[2]);
						int[] count = {0};
						boolean ok = role.resetOfficial(gid, count);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsResetOfficialRes(gid, ok, count[0]));	
					}
					break;
				case "resetofficialskill":
					{
						short gid = Short.parseShort(msg[2]);
						int[] count = {0};
						boolean ok = role.resetOfficialSkill(gid, count);
						sendLuaPacket(sessionid, LuaPacket.encodeGeneralsResetOfficialSkillRes(gid, ok, count[0]));	
					}
					break;
				default:
					break;
				}
			}
			break;
		case "generalstone":
		{
			switch( msg[1] )
			{
			case "sync":
			{
				List<SBean.DBGeneralStone> stones = role.getGeneralStones();
				sendLuaPacket(sessionid, LuaPacket.encodeStonesSyncRes(stones));
			}
		    break;
			case "activestone":
				{
					int itemId = Integer.parseInt(msg[2]);
					short gid = Short.parseShort(msg[3]);
					byte pos = Byte.parseByte(msg[4]);
					SBean.DBGeneralStone gstone = new SBean.DBGeneralStone();
					boolean bOK = role.generalsActiveStone(itemId,gid,pos,gstone);
					sendLuaPacket(sessionid, LuaPacket.encodeGeneralStoneActiveRes(itemId,gid,pos,gstone, bOK));						
				}
			break;
			case "addstoneexp":
				{
					int itemId = Integer.parseInt(msg[2]);
					List<SBean.DropEntryNew> matList = new ArrayList<SBean.DropEntryNew>();
					List<Integer> itemList = new ArrayList<Integer>();
					Byte n = Byte.parseByte(msg[3]);
					int index = 4;
					for(Byte i = 0; i < n; ++i)
					{
						SBean.DropEntryNew e = new SBean.DropEntryNew(Byte.parseByte(msg[index++]), Short.parseShort(msg[index++]), Integer.parseInt(msg[index++]));
						if( e.type != GameData.COMMON_TYPE_EQUIP && e.type != GameData.COMMON_TYPE_ITEM )
							return;
						matList.add(e);
					}
					Byte n1 = Byte.parseByte(msg[index++]);
					for(Byte i = 0; i < n1; ++i)
					{
						itemList.add(Integer.parseInt(msg[index++]));
					}
					
					boolean bOK = role.generalsStoneAddExp(itemId, matList,itemList);
					sendLuaPacket(sessionid, LuaPacket.encodeGeneralsStoneRes(itemId, matList,itemList, bOK));
				}
				break;
			case "removestone":
				{
					int itemId = Integer.parseInt(msg[2]);
					boolean remove = role.generalsRemoveStone(itemId);
					sendLuaPacket(sessionid, LuaPacket.encodeGeneralRemoveStoneRes(itemId, remove));	
				}
			break;
			case "resetstone":
			{
				int itemId = Integer.parseInt(msg[2]);
				byte lockCount = Byte.parseByte(msg[3]);
				Set<Integer> lock = new TreeSet<Integer>();
				int index = 4;
				for (byte i = 0; i < lockCount; i ++)
					lock.add(Integer.parseInt(msg[index ++]));
				
				byte lockCount2 = Byte.parseByte(msg[index++]);
				Set<Integer> lock2 = new TreeSet<Integer>();
				for (byte i = 0; i < lockCount2; i ++)
					lock2.add(Integer.parseInt(msg[index ++]));
				List<DBGeneralStoneProp> props = new ArrayList<SBean.DBGeneralStoneProp>();
				boolean bOK = role.generalsResetGeneralStone(itemId, lock,lock2, props);
				sendLuaPacket(sessionid, LuaPacket.encodeGeneralsResetStoneRes(itemId, bOK, props));						
			}
			break;
		   case "savestone":
			{
				int itemId = Integer.parseInt(msg[2]);
				List<DBGeneralStoneProp> props = new ArrayList<SBean.DBGeneralStoneProp>();
				boolean bOK = role.generalsSaveResetGeneralStone(itemId, props);
				sendLuaPacket(sessionid, LuaPacket.encodeGeneralsSaveResetStoneRes(itemId, bOK, props));	
			}
			break;
			default:
				break;
			}
		}
		break;
		case "equips":
			{
				switch( msg[1] )
				{
				case "sell":
					{
						short eid = Short.parseShort(msg[2]);
						short cnt = Short.parseShort(msg[3]);
						short cntNow = Short.parseShort(msg[4]);
						boolean bOK = role.equipsSell(eid, cnt, cntNow);
						sendLuaPacket(sessionid, LuaPacket.encodeEquipsSellRes(eid, cnt, bOK));
					}
					break;
				case "combine":
					{
						short eid = Short.parseShort(msg[2]);
						boolean bOK = role.equipsCombine(eid);
						sendLuaPacket(sessionid, LuaPacket.encodeEquipsCombineRes(eid, bOK));
					}	
					break;
				default:
					break;
				}
			}
			break;
		case "debug":
			{
				switch( msg[1] )
				{
				case "log":
					{
						String dmsg = msg[2];
						gs.getLogger().warn("lua_debug_log: " + dmsg);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "items":
			{
				switch( msg[1] )
				{
				case "combine":
					{
						short iid = Short.parseShort(msg[2]);
						boolean bOK = role.itemsCombine(iid);
						sendLuaPacket(sessionid, LuaPacket.encodeItemCombineRes(iid, bOK));
					}	
					break;
				case "gamble":
					{
						short iid = Short.parseShort(msg[2]);
						int icount = Integer.parseInt(msg[3]);
						List<List<DropEntry>> drops = role.itemsGamble(iid,icount);
						sendLuaPacket(sessionid, LuaPacket.encodeItemsGambleRes(iid,icount, drops));
					}
					break;
				case "sell":
					{
						short iid = Short.parseShort(msg[2]);
						int cnt = Integer.parseInt(msg[3]);
						int cntNow = Integer.parseInt(msg[4]);
						boolean bOK = role.itemsSell(iid, cnt, cntNow);
						sendLuaPacket(sessionid, LuaPacket.encodeItemsSellRes(iid, cnt, bOK));
					}
					break;
				case "sellall":
					{
						int money = role.itemsSellAll();
						sendLuaPacket(sessionid, LuaPacket.encodeItemsSellAllRes(money));
					}
					break;
				case "use":
					{
						short iid = Short.parseShort(msg[2]);
						short cnt = Short.parseShort(msg[3]);
						short gid = Short.parseShort(msg[4]);
						int[] dataRet = new int[2];
						short rcnt = role.itemsUse(iid, cnt, gid, dataRet);
						sendLuaPacket(sessionid, LuaPacket.encodeItemsUseRes(iid, cnt, gid, rcnt, dataRet));
						if (dataRet[1] == GameData.ITEM_TYPE_VIP_EXP)
							gs.getRPCManager().syncRolePaytoatalViplvl(sessionid, role.getStonePayTotal(), role.lvlVIP);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "login":
			{
				SBean.UserLoginRequest req = new SBean.UserLoginRequest();
				req.userName = msg[1];
				req.deviceID = msg[2];
				req.verMajor = Integer.parseInt(msg[3]);
				req.verMinor = Integer.parseInt(msg[4]);
				req.loginType = Byte.parseByte(msg[5]);
				req.key = msg[6];
				//
				Role.MSDKInfo msdkInfo = new Role.MSDKInfo();
				msdkInfo.loginType = req.loginType;
				msdkInfo.gameappID = msg[7];
				msdkInfo.platID = Integer.parseInt(msg[8]);
				msdkInfo.openID = msg[9];
				if( GameData.getInstance().isGuestOpenID(msdkInfo.openID) )
				{
					msdkInfo.bGuest = true;
					msdkInfo.gameappID = gs.getGameAppID(true);
				}
				msdkInfo.clientVer = msg[10];
				try
				{
					msdkInfo.clientVerInt = Integer.parseInt(msdkInfo.clientVer);
				}
				catch(Exception ex)
				{					
				}
				msdkInfo.systemHardware = msg[11];
				msdkInfo.telecomOper = msg[12];
				msdkInfo.network = msg[13];
				msdkInfo.loginChannel = Integer.parseInt(msg[14]);
				//
				msdkInfo.openKey = msg[15];
				msdkInfo.payToken = msg[16];
				msdkInfo.pf = msg[17];
				msdkInfo.pfKey = msg[18];
				
				if( req.loginType == SBean.UserLoginRequest.eLoginGod )
				{
					// change platid
					msdkInfo.platID = gs.getConfig().platID;
					// change openid
					msdkInfo.openID = GameData.getInstance().getOpenIDByUserName(req.userName);
					// change gameappid
					msdkInfo.gameappID = gs.getConfig().gameappID;
					if( msdkInfo.isIOSGuest() )
						msdkInfo.gameappID = gs.getGameAppID(true);
					msdkInfo.clientVerInt = gs.getConfig().verMinor;
					msdkInfo.clientVer = new Integer(msdkInfo.clientVerInt).toString();
					msdkInfo.systemHardware = "mx4";
					msdkInfo.systemSoftware = "4.0.4";
					msdkInfo.telecomOper = "CMCC";
					msdkInfo.network = "";
					msdkInfo.loginChannel = 2002;
					msdkInfo.screenWidth = 1280;
					msdkInfo.screenHeight = 720;
					msdkInfo.density = 2.0f;
					msdkInfo.cpuHardware = "armeabi-v7a";
					msdkInfo.memory = 0;
					msdkInfo.gLRender = "";
					msdkInfo.gLVersion = "";
					msdkInfo.deviceID = "IMM76D";
				}
				
				SessionInfo sinfo = sessions.get(sessionid);
				if( sinfo != null && sinfo.addrClient != null && sinfo.addrClient.host != null )
				{
					msdkInfo.ip = sinfo.addrClient.host;
					msdkInfo.ipInt = StringConverter.parseIPStr(msdkInfo.ip);
				}				
				//
				onUserLogin(sessionid, req, msdkInfo);
			}
			break;
		case "createrole":
			{
				SBean.UserLoginRequest req = new SBean.UserLoginRequest();
				req.userName = msg[1];
				req.deviceID = msg[2];
				req.verMajor = Integer.parseInt(msg[3]);
				req.verMinor = Integer.parseInt(msg[4]);
				req.loginType = Byte.parseByte(msg[5]);
				req.key = msg[6];
				String rname = msg[7];
				short headIconID = Short.parseShort(msg[8]);
				String userNameOrg = msg[9];
				//
				Role.MSDKInfo msdkInfo = new Role.MSDKInfo();
				msdkInfo.loginType = req.loginType;
				msdkInfo.gameappID = msg[10];
				msdkInfo.platID = Integer.parseInt(msg[11]);
				msdkInfo.openID = msg[12];
				if( GameData.getInstance().isGuestOpenID(msdkInfo.openID) )
				{
					msdkInfo.bGuest = true;
					msdkInfo.gameappID = gs.getGameAppID(true);
				}
				msdkInfo.clientVer = msg[13];
				try
				{
					msdkInfo.clientVerInt = Integer.parseInt(msdkInfo.clientVer);
				}
				catch(Exception ex)
				{					
				}
				msdkInfo.systemSoftware = msg[14];
				msdkInfo.systemHardware = msg[15];
				msdkInfo.telecomOper = msg[16];
				msdkInfo.network = msg[17];
				msdkInfo.screenWidth = Integer.parseInt(msg[18]);
				msdkInfo.screenHeight = Integer.parseInt(msg[19]);
				msdkInfo.density = Float.parseFloat(msg[20]);
				msdkInfo.loginChannel = Integer.parseInt(msg[21]);
				msdkInfo.cpuHardware = msg[22];
				msdkInfo.memory = Integer.parseInt(msg[23]);
				msdkInfo.gLRender = msg[24];
				msdkInfo.gLVersion = msg[25];
				msdkInfo.deviceID = msg[26];
				//			
				msdkInfo.openKey = msg[27];
				msdkInfo.payToken = msg[28];
				msdkInfo.pf = msg[29];
				msdkInfo.pfKey = msg[30];
				
				if( req.loginType == SBean.UserLoginRequest.eLoginGod )
				{
					// change platid
					msdkInfo.platID = gs.getConfig().platID;
					// change openid
					msdkInfo.openID = GameData.getInstance().getOpenIDByUserName(req.userName);
					// change gameappid
					msdkInfo.gameappID = gs.getConfig().gameappID;
					if( msdkInfo.isIOSGuest() )
						msdkInfo.gameappID = gs.getGameAppID(true);
					msdkInfo.clientVerInt = gs.getConfig().verMinor;
					msdkInfo.clientVer = new Integer(msdkInfo.clientVerInt).toString();
					msdkInfo.systemHardware = "mx4";
					msdkInfo.systemSoftware = "4.0.4";
					msdkInfo.telecomOper = "CMCC";
					msdkInfo.network = "";
					msdkInfo.loginChannel = 2002;
					msdkInfo.screenWidth = 1280;
					msdkInfo.screenHeight = 720;
					msdkInfo.density = 2.0f;
					msdkInfo.cpuHardware = "armeabi-v7a";
					msdkInfo.memory = 0;
					msdkInfo.gLRender = "";
					msdkInfo.gLVersion = "";
					msdkInfo.deviceID = "IMM76D";
					
				}
				
				SessionInfo sinfo = sessions.get(sessionid);
				if( sinfo != null && sinfo.addrClient != null && sinfo.addrClient.host != null )
				{
					msdkInfo.ip = sinfo.addrClient.host;
					msdkInfo.ipInt = StringConverter.parseIPStr(msdkInfo.ip);
				}
				//
				onCreateRole(sessionid, req, msdkInfo, rname, headIconID, userNameOrg);
			}
			break;
		case "changename":
			{
				onChangeName(sessionid, msg[1], msg[2]);
			}
			break;
//		case "levellimit":
//			{
//				sendLuaPacket(sessionid, LuaPacket.encodeLevelLimitInfo(gs.getLoginManager().getLevelLimitInfo()));
//			}
//			break;
		case "toplevel":
			{
				sendLuaPacket(sessionid, LuaPacket.encodeLevelLimitInfo(gs.getLoginManager().getTopLevelHaloInfo()));
			}
			break;
		case "keepalive":
			{
				//gs.getLogger().info("keepalive" + msg[1]);
			}
			break;
		case "egg":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						List<Role.EggInfo> lst = role.syncEggsInfo();
						if( lst != null )
							//sendLuaPacket(sessionid, LuaPacket.encodeEggsInfo(lst, true, role.getSoulBoxNow(gs.getTime())));
							sendLuaPacket(sessionid, LuaPacket.encodeEggsInfo(lst, true));
					}
					break;
				case "take":
					{
						byte eid = Byte.parseByte(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodeEggTakeRes(eid, role.eggTake(eid)));
					}
					break;
				case "take10":
					{
						byte eid = Byte.parseByte(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodeEggTake10Res(eid, role.eggTake10(eid)));
					}
					break;
				case "take100":
				{
					byte eid =(byte)1;// Byte.parseByte(msg[2]);
					sendLuaPacket(sessionid, LuaPacket.encodeEggTake100Res(eid, role.eggTake100(eid)));
				}
				break;	
				default:
					break;
				}
			}
			break;
		case "soulbox":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						Role.SoulBoxInfo info = role.getSoulBoxNow(gs.getTime());
						sendLuaPacket(sessionid, LuaPacket.encodeSoulBoxInfo(info, role.getSoulBoxLogTimes(info)));
					}
					break;
				case "take":
					{
						byte sbid = Byte.parseByte(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodeTakeSoulBoxRes(sbid, role.takeSoulBox(sbid)));
					}
					break;
				case "optional":
					{
						short gid = Short.parseShort(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodeOptionalSoulBoxRes(gid, role.optionalSoulBox(gid)));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "setflag":
			{
				switch( msg[1] )
				{
				case "boss":
					{
						role.setFlagWithLock(Role.ROLE_FLAG_TIPS_BOSS);
					}
					break;
				case "country":
					{
						role.setFlagWithLock(Role.ROLE_FLAG_TIPS_COUNTRY);
					}
					break;
				case "zhenfa":
					{
						role.setFlagWithLock(Role.ROLE_FLAG_TIPS_ZHENFA);
					}
					break;
				case "combat":
					{
						role.setFlagWithLock(Role.ROLE_FLAG_TIPS_COMBAT);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "pay":
			{
				switch( msg[1] )
				{
				case "table":
					{
						if( gs.getConfig().payState == 0 )
						{
							sendLuaPacket(sessionid, LuaPacket.encodePayTableErr(-4, 0));
						}
						else if( gs.getConfig().godMode == 1 || gs.getConfig().godMode == 2 )
						{
							role.paySyncTable(sessionid);
						}
						else
						{
							role.paySync(new Role.PaySyncCallback() 
							{								
								@Override
								public void onCallback(boolean bOK, int errCode, int ret)
								{
									if( bOK )
										role.paySyncTable(sessionid);
									else
									{
										sendLuaPacket(sessionid, LuaPacket.encodePayTableErr(errCode, ret));
									}
								}
							});
						}
					}
					break;
				case "init":
					{
						int rmb = Integer.parseInt(msg[2]);
						byte payLvl = Byte.parseByte(msg[3]);
						if( rmb <= 0 || payLvl < 0 || payLvl > gs.getGameData().getPayCFG(role.userName).levels.size() )
							break; // TODO
						//TODO check pid
						if( gs.getConfig().godMode == 1 || gs.getConfig().godMode == 2 )
						{
							role.payAddGod(payLvl, rmb);
							role.testDTask2Notice();
						}
						else
						{	
							role.payInit(payLvl, rmb);
						}
					}
					break;
				case "initres":
					{
						//id, retCode, payState, provideState, realSave
						String uuid = msg[2];

						int retCode = Integer.parseInt(msg[3]); 
						int payState = Integer.parseInt(msg[4]);
						// TODO
						gs.getLogger().info("user pay initres:[" + role.userName +"(" + role.id + ")]" + ",iid=" + uuid + ",ret=" + retCode + "," + payState);
						if( retCode < 0 || retCode == 0 && payState <= 0 )
						{
							role.payConfirm(uuid);
							role.paySyncPayRes();
						}
						else
						{
							role.payCancel(uuid);
						}
					}
					break;
				default:
					break;
				}
			}
			break;
		case "vip":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeVIPInfo(role.syncVIPInfo()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "task":
			{
				switch( msg[1] )
				{
				case "finish":
					{
						short gid = Short.parseShort(msg[2]);
						short seq = Short.parseShort(msg[3]);
						sendLuaPacket(sessionid, LuaPacket.encodeTaskFinishRes(gid, seq, role.taskFinish(gid, seq)));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "checkin":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeCheckinSyncInfo(role.checkinSync(), role.getQQVIP()));
					}
					break;
				case "take":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeCheckinTakeRes(role.checkinTake(), role.getQQVIP()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "act":
			{
				switch( msg[1] )
				{
				case "table":
					{
						List<ActivityDisplayConfig> openSoonCfgs = gs.getGameActivities().getDisplayActivityOpenSoon();
						List<ActivityDisplayConfig> openedCfgs = gs.getGameActivities().getDisplayActivityOpened();
						sendLuaPacket2(sessionid, LuaPacket.encodeActivitiesCenterInfo(openSoonCfgs, openedCfgs, role.getGameActivityCanRewardStatus(openedCfgs), role.getGameActivityAlreadyTakeAllRewardStatus(openedCfgs)));
					}
					break;
				case "doubledropsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.DoubleDropConfig cfg = gs.getGameActivities().getDoubleDropActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeDoubleDropSyncInfo(cfg));
					}
					break;
				case "extradropsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.ExtraDropConfig cfg = gs.getGameActivities().getExtraDropActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeExtraDropSyncInfo(cfg));
					}
					break;
				case "checkingiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.CheckinGiftConfig cfg = gs.getGameActivities().getCheckinGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeCheckinGiftSyncInfo(cfg, role.checkinGiftSync(cfg)));
					}
					break;
				case "checkingifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int dayseq = Integer.parseInt(msg[3]);
						GameActivities.CheckinGiftConfig cfg = gs.getGameActivities().getCheckinGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeCheckinGiftTakeRes(id, dayseq, role.takeCheckinGiftReward(cfg, id, dayseq)));
					}
					break;
				case "firstpaygiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.FirstPayGiftConfig cfg = gs.getGameActivities().getFirstPayGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeFirstPayGiftSyncInfo(cfg, role.firstPayGiftSync(cfg)));
					}
					break;
				case "firstpaygifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int lvlseq = Integer.parseInt(msg[3]);
						GameActivities.FirstPayGiftConfig cfg = gs.getGameActivities().getFirstPayGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeFirstPayGiftTakeRes(id, lvlseq, role.takeFirstPayGiftReward(cfg, id)));
					}
					break;
				case "paygiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.PayGiftConfig cfg = gs.getGameActivities().getPayGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodePayGiftSyncInfo(cfg, role.payGiftSync(cfg)));
					}
					break;
				case "paygifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int pay = Integer.parseInt(msg[3]);
						GameActivities.PayGiftConfig cfg = gs.getGameActivities().getPayGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodePayGiftTakeRes(id, pay, role.takePayGiftReward(cfg, id, pay)));
					}
					break;
				case "consumegiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.ConsumeGiftConfig cfg = gs.getGameActivities().getConsumeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeConsumeGiftSyncInfo(cfg, role.consumeGiftSync(cfg)));
					}
					break;
				case "consumegifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int consume = Integer.parseInt(msg[3]);
						GameActivities.ConsumeGiftConfig cfg = gs.getGameActivities().getConsumeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeConsumeGiftTakeRes(id, consume, role.takeConsumeGiftReward(cfg, id, consume)));
					}
					break;
				case "consumerebatesync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.ConsumeRebateConfig cfg = gs.getGameActivities().getConsumeRebateActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeConsumeRebateSyncInfo(cfg, role.consumeRebateSync(cfg)));
					}
					break;
				case "consumerebatetake":
					{
						int id = Integer.parseInt(msg[2]);
						int consume = Integer.parseInt(msg[3]);
						GameActivities.ConsumeRebateConfig cfg = gs.getGameActivities().getConsumeRebateActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeConsumeRebateTakeRes(id, consume, role.takeConsumeRebateReward(cfg, id, consume)));
					}
					break;
				case "upgradegiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.UpgradeGiftConfig cfg = gs.getGameActivities().getUpgradeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeUpgradeGiftSyncInfo(cfg, role.upgradeGiftSync(cfg)));
					}
					break;
				case "upgradegifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int level = Integer.parseInt(msg[3]);
						GameActivities.UpgradeGiftConfig cfg = gs.getGameActivities().getUpgradeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeUpgradeGiftTakeRes(id, level, role.takeUpgradeGiftReward(cfg, id, level)));
					}
					break;
				case "exchangegiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.ExchangeGiftConfig cfg = gs.getGameActivities().getExchangeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeExchangeGiftSyncInfo(cfg));
					}
					break;
				case "exchangegifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int seq = Integer.parseInt(msg[3]);
						GameActivities.ExchangeGiftConfig cfg = gs.getGameActivities().getExchangeGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeExchangeGiftTakeRes(id, seq, role.takeExchangeGiftReward(cfg, id, seq)));
					}
					break;
				case "gathergiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.GatherGiftConfig cfg = gs.getGameActivities().getGatherGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeGatherGiftSyncInfo(cfg, role.gatherGiftSync(cfg)));
					}
					break;
				case "gathergifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int seq = Integer.parseInt(msg[3]);
						GameActivities.GatherGiftConfig cfg = gs.getGameActivities().getGatherGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeGatherGiftTakeRes(id, seq, role.takeGatherGiftReward(cfg, id, seq)));
					}
					break;
				case "limitedshopsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.LimitedShopConfig cfg = gs.getGameActivities().getLimitedShopActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeLimitedShopSyncInfo(cfg, role.limitedShopSync(cfg)));
					}
					break;
				case "limitedshoptake":
					{
						int id = Integer.parseInt(msg[2]);
						int seq = Integer.parseInt(msg[3]);
						int count = Integer.parseInt(msg[4]);
						GameActivities.LimitedShopConfig cfg = gs.getGameActivities().getLimitedShopActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeLimitedShopTakeRes(id, seq, count,role.takeLimitedShopReward(cfg, id, seq,count)));
					}
					break;
				case "logingiftsync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.LoginGiftConfig cfg = gs.getGameActivities().getLoginGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeLoginGiftSyncInfo(cfg, role.loginGiftSync(cfg)));
					}
					break;
				case "logingifttake":
					{
						int id = Integer.parseInt(msg[2]);
						int days = Integer.parseInt(msg[3]);
						GameActivities.LoginGiftConfig cfg = gs.getGameActivities().getLoginGiftActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeLoginGiftTakeRes(id, days, role.takeLoginGiftReward(cfg, id, days)));
					}
					break;
				case "tradingcentersync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.TradingCenterConfig cfg = gs.getGameActivities().getTradingCenterActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeTradingCenterSyncInfo(cfg, role.tradingCenterSync(cfg)));
					}
					break;
				case "tradingcenterbuy":
					{
						int id = Integer.parseInt(msg[2]);
						int type = Integer.parseInt(msg[3]);
						int count = Integer.parseInt(msg[4]);
						GameActivities.TradingCenterConfig cfg = gs.getGameActivities().getTradingCenterActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeTradingCenterBuyRes(id, type, count, role.buyTradingCenterGoods(cfg, id, type, count)));
					}
					break;
				case "tradingcentertake":
					{
						int id = Integer.parseInt(msg[2]);
						int type = Integer.parseInt(msg[3]);
						GameActivities.TradingCenterConfig cfg = gs.getGameActivities().getTradingCenterActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeTradingCenterTakeRes(id, type, role.takeTradingCenterReward(cfg, id, type)));
					}
					break;
				case "giftpackagesync":
					{
						int id = Integer.parseInt(msg[2]);
						GameActivities.GiftPackageConfig cfg = gs.getGameActivities().getGiftPackageActivity().getOpenedConfigById(id);
						sendLuaPacket2(sessionid, LuaPacket.encodeGiftPackageSyncInfo(cfg));
					}
					break;
				case "giftpackagetake":
					{
						int id = Integer.parseInt(msg[2]);
						String key = msg[3];
						GameActivities.GiftPackageConfig cfg = gs.getGameActivities().getGiftPackageActivity().getOpenedConfigById(id);
						role.exchangeCDKeyGift(cfg, key);
					}
					break;
				case "diskBetSync":
				{
					int id = Integer.parseInt(msg[2]);
					DiskBetConfig cfg = gs.getGameActivities().getDiskBetActivity().getOpenedConfigById(id);
					sendLuaPacket2(sessionid, LuaPacket.encodeDiskBetSyncInfo(cfg, role.diskBetSync(cfg),gs.getDiskManager().diskBet,gs.getDiskManager().getselfRank(role.diskBetSyncSelf(), role.id),gs.getDiskManager().getselfRank((short)2, role.id)));
				}
				break;
				case "diskbet":
					{
						int id = Integer.parseInt(msg[2]);
						short tid = Short.parseShort(msg[3]);
						DiskBetConfig cfg = gs.getGameActivities().getDiskBetActivity().getOpenedConfigById(id);
						List<DropEntryNew> bOK = role.diskBet(cfg,tid);
						if(bOK==null)
						sendLuaPacket(sessionid, LuaPacket.encodeDiskbetRes(id,tid,cfg, bOK,-1));
					}
					break;
				case "diskrank":
					{
						int id = Integer.parseInt(msg[2]);
						short tid = Short.parseShort(msg[3]);
						DiskBetConfig cfg = gs.getGameActivities().getDiskBetActivity().getOpenedConfigById(id);
						List<DBDiskBetRank> bOK = role.diskrank(cfg,tid);
						sendLuaPacket(sessionid, LuaPacket.encodeDiskrankRes(id,tid,cfg, bOK,gs.getDiskManager().getselfRank((short)tid, role.id)));
					}	
					break;
				case "diskrankHis":
					{
						List<DBDiskBetRank> bOK = role.diskrankHis();
						sendLuaPacket(sessionid, LuaPacket.encodeDiskrankHisRes(bOK));
					}	
				break;
				case "diskgiftsync":
				{
					int id = Integer.parseInt(msg[2]);
					DiskGiftConfig cfg = gs.getGameActivities().getDiskGiftActivity().getOpenedConfigById(id);
					sendLuaPacket2(sessionid, LuaPacket.encodeDiskGiftSyncInfo(cfg, role.diskGiftSync(cfg)));
				}
				break;
			    case "diskgifttake":
				{
					int id = Integer.parseInt(msg[2]);
					int pay = Integer.parseInt(msg[3]);
					DiskGiftConfig cfg = gs.getGameActivities().getDiskGiftActivity().getOpenedConfigById(id);
					sendLuaPacket2(sessionid, LuaPacket.encodeDiskGiftTakeRes(id, pay, role.takeDiskGiftReward(cfg, id, pay)));
				}
				break;
				
			    case "rechargeRankSync":
				{
					int id = Integer.parseInt(msg[2]);
					RechargeRankConfig cfg = gs.getGameActivities().getRechargeRankActivity().getOpenedConfigById(id);
					List<DBDiskBetRank> bOK = role.diskrank2(cfg,(short)2);
					sendLuaPacket2(sessionid, LuaPacket.encodeDiskBetSyncInfo2(cfg, role.diskBetSync2(cfg),gs.getDiskManager2().diskBet,gs.getDiskManager2().getselfRank(role.diskBetSyncSelf(), role.id),gs.getDiskManager2().getselfRank((short)2, role.id),bOK));
				}
				break;
				case "rechargerank":
					{
						int id = Integer.parseInt(msg[2]);
						short tid = (short)2;
						RechargeRankConfig cfg = gs.getGameActivities().getRechargeRankActivity().getOpenedConfigById(id);
						List<DBDiskBetRank> bOK = role.diskrank2(cfg,tid);
						sendLuaPacket(sessionid, LuaPacket.encodeDiskrankRes2(id,tid,cfg, bOK,gs.getDiskManager2().getselfRank((short)tid, role.id)));
					}	
					break;
			    case "rechargegiftsync":
				{
					int id = Integer.parseInt(msg[2]);
					RechargeGiftConfig cfg = gs.getGameActivities().getRechargeGiftActivity().getOpenedConfigById(id);
					sendLuaPacket2(sessionid, LuaPacket.encodeRechargeGiftSyncInfo(cfg, role.diskRechargeSync(cfg)));
				}
				break;
			    case "rechargegifttake":
				{
					int id = Integer.parseInt(msg[2]);
					int pay = Integer.parseInt(msg[3]);
					RechargeGiftConfig cfg = gs.getGameActivities().getRechargeGiftActivity().getOpenedConfigById(id);
					sendLuaPacket2(sessionid, LuaPacket.encodeRechargeGiftTakeRes(id, pay, role.takeRechargeGiftReward(cfg, id, pay)));
				}
				break;
				default:
					break;
				}
			}
			break;
		case "view":
			{
				switch( msg[1] )
				{
				case "fighters":
					break;
				default:
					break;
				}
			}
			break;
		case "mail":
			{
				switch( msg[1] )
				{
				case "test":
					{
						boolean bRet = role.testNewMail();
						sendLuaPacket(sessionid, LuaPacket.encodeTestNewMail(bRet));	
					}
					break;
				case "sync":
					{
						List<DBMail.Message> lst = role.syncMails();
						sendLuaPacket(sessionid, LuaPacket.encodeSyncMails(lst));	
					}
					break;
				case "readcontent":
					{
						short mid = Short.parseShort(msg[2]);
						DBMail.MailAttachments att = new MailAttachments();
						String content = role.mailReadContent(mid, att);
						// TODO
						sendLuaPacket(sessionid, LuaPacket.encodeMailReadContent(mid, content, att.attLst));
					}
					break;
				case "setread":
					{
						short mid = Short.parseShort(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodeMailSetRead(mid, role.mailSetRead(mid)));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "dtask2":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						List<SBean.DBDailyTask2Log> lst = role.getDailyTask2Info();
						sendLuaPacket(sessionid, LuaPacket.encodeDailyTask2Info(
								role.getMonthlyCardStartTime(), gs.getGameActivities().getFreePhysicalActivity().getFreeVit(), lst, role.getDailyActivity(), role.getDailyActivityRewards()));
						role.testDTask2Notice();
					}
					break;
				case "reward":
					{
						byte id = Byte.parseByte(msg[2]);
						short[] lvlReward = new short[1];
						boolean bOK = role.getDailyTask2Reward(id, lvlReward);
						sendLuaPacket(sessionid, LuaPacket.encodeDailyTask2RewardRes(id, bOK, lvlReward[0]));
						if( bOK )
							role.testDTask2Notice();
					}
					break;
				case "activityreward":
					{
						int reward = Integer.parseInt(msg[2]); 
						role.dailyActivityReward(reward);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "arena":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						role.arenaSyncInfo();
					}
					break;
				case "brief":
					{
						role.arenaSyncBrief();
					}
					break;
				case "logs":
					{
						role.arenaSyncLogs();
					}
					break;
				case "ranks":
					{
						gs.getArenaManager().listRanks(sessionid);
					}
					break;
				case "startattack":
					{
						int rank = Integer.parseInt(msg[2]);
						int erid = Integer.parseInt(msg[3]);
						int index = 4;
						List<Short> generals = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
							generals.add(Short.parseShort(msg[index ++]));
						List<Short> pets = new ArrayList<Short>();
						pets.add(Short.parseShort(msg[index ++]));
						role.arenaStartAttack(rank, erid, generals, pets);
					}
					break;
				case "finishattack":
					{
						int rank = Integer.parseInt(msg[2]);
						byte win = Byte.parseByte(msg[3]);
						
						int index = 4;
						SBean.ArenaRecord record = new SBean.ArenaRecord();
						record.rid1 = Integer.parseInt(msg[index ++]);
						record.rid2 = Integer.parseInt(msg[index ++]);
						record.headIconId1 = Integer.parseInt(msg[index ++]);
						record.headIconId2 = Integer.parseInt(msg[index ++]);
						record.name1 = msg[index ++];
						record.name2 = msg[index ++];
						record.lvl1 = Short.parseShort(msg[index ++]);
						record.lvl2 = Short.parseShort(msg[index ++]);
						record.seed = Integer.parseInt(msg[index ++]);
						record.generals1 = new ArrayList<DBRoleGeneral>();
						short gcount = Short.parseShort(msg[index ++]);
						for (short k = 0; k < gcount; k ++) {
							DBRoleGeneral g = new DBRoleGeneral();
							g.id = Short.parseShort(msg[index ++]);
							g.lvl = Short.parseShort(msg[index ++]);
							g.advLvl = Byte.parseByte(msg[index ++]);
							g.evoLvl = Byte.parseByte(msg[index ++]);
							g.skills = new ArrayList<Short>();
							for (short j = 0; j < 4; j ++)
								g.skills.add(Short.parseShort(msg[index ++]));
							g.equips = new ArrayList<SBean.DBGeneralEquip>();
							for (short j = 0; j < 6; j ++) {
								byte lvl = Byte.parseByte(msg[index ++]);
								short exp = Short.parseShort(msg[index ++]);
								g.equips.add(new SBean.DBGeneralEquip(lvl, exp));								
							}
							g.weapon = new SBean.DBWeapon();
							g.weapon.passes = new ArrayList<SBean.DBWeaponProp>();
							g.weapon.resetPasses = new ArrayList<SBean.DBWeaponProp>();
							g.weapon.id = g.id;
							g.weapon.lvl = Short.parseShort(msg[index ++]);
							if (g.weapon.lvl >= 0) {
								g.weapon.evo = Byte.parseByte(msg[index ++]);
								int pcount = Integer.parseInt(msg[index ++]);
								for (int j = 0; j < pcount; j ++) {
									SBean.DBWeaponProp prop = new SBean.DBWeaponProp();
									prop.propId = Short.parseShort(msg[index ++]);
									prop.type = Byte.parseByte(msg[index ++]);
									prop.value = Float.parseFloat(msg[index ++]);
									prop.rank = Byte.parseByte(msg[index ++]);
									g.weapon.passes.add(prop);
								}
							}
							g.generalSeyen =new ArrayList<SBean.DBGeneralSeyen>();
							SBean.DBGeneralSeyen seyen = new SBean.DBGeneralSeyen();
							seyen.lvl= Short.parseShort(msg[index ++]);
							seyen.exp = Integer.parseInt(msg[index ++]);
							seyen.seyenTotal = Integer.parseInt(msg[index ++]);
							g.generalSeyen.add(seyen);
							g.headicon = Byte.parseByte(msg[index ++]);
							g.bless =new ArrayList<SBean.DBGeneralBless>();
							SBean.DBGeneralBless bless = new SBean.DBGeneralBless();
							bless.p1 = Float.parseFloat(msg[index ++]);
							bless.p2 = Float.parseFloat(msg[index ++]);
							bless.p3 = Float.parseFloat(msg[index ++]);
							g.bless.add(bless);
							g.bestow  =new ArrayList<SBean.DBGeneralBestow>();
							SBean.DBGeneralBestow bestow = new SBean.DBGeneralBestow();
							bestow.lvl = Short.parseShort(msg[index ++]);
							int pcount = Integer.parseInt(msg[index ++]);
							bestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
							for (int j = 0; j < pcount; j ++) {
								SBean.DBBestowLevel prop = new SBean.DBBestowLevel();
								prop.gid = Short.parseShort(msg[index ++]);
								prop.lvl = Short.parseShort(msg[index ++]);
								bestow.bestowLevel.add(prop);
							}
							g.bestow.add(bestow);
							g.official = new ArrayList<SBean.DBGeneralOfficial>();
							SBean.DBGeneralOfficial official = new SBean.DBGeneralOfficial();
							official.lvl = (short)(Short.parseShort(msg[index ++])-1);
							official.exp = Integer.parseInt(msg[index ++]);
							official.skillLvl = Short.parseShort(msg[index ++]);
							g.official.add(official);
							record.generals1.add(g);
						}
						record.generals2 = new ArrayList<DBRoleGeneral>();
						gcount = Short.parseShort(msg[index ++]);
						for (short k = 0; k < gcount; k ++) {
							DBRoleGeneral g = new DBRoleGeneral();
							g.id = Short.parseShort(msg[index ++]);
							g.lvl = Short.parseShort(msg[index ++]);
							g.advLvl = Byte.parseByte(msg[index ++]);
							g.evoLvl = Byte.parseByte(msg[index ++]);
							g.skills = new ArrayList<Short>();
							for (short j = 0; j < 4; j ++)
								g.skills.add(Short.parseShort(msg[index ++]));
							g.equips = new ArrayList<SBean.DBGeneralEquip>();
							for (short j = 0; j < 6; j ++) {
								byte lvl = Byte.parseByte(msg[index ++]);
								short exp = Short.parseShort(msg[index ++]);
								g.equips.add(new SBean.DBGeneralEquip(lvl, exp));								
							}
							g.weapon = new SBean.DBWeapon();
							g.weapon.passes = new ArrayList<SBean.DBWeaponProp>();
							g.weapon.resetPasses = new ArrayList<SBean.DBWeaponProp>();
							g.weapon.id = g.id;
							g.weapon.lvl = Short.parseShort(msg[index ++]);
							if (g.weapon.lvl >= 0) {
								g.weapon.evo = Byte.parseByte(msg[index ++]);
								int pcount = Integer.parseInt(msg[index ++]);
								for (int j = 0; j < pcount; j ++) {
									SBean.DBWeaponProp prop = new SBean.DBWeaponProp();
									prop.propId = Short.parseShort(msg[index ++]);
									prop.type = Byte.parseByte(msg[index ++]);
									prop.value = Float.parseFloat(msg[index ++]);
									prop.rank = Byte.parseByte(msg[index ++]);
									g.weapon.passes.add(prop);
								}
							}
							g.generalSeyen =new ArrayList<SBean.DBGeneralSeyen>();
							SBean.DBGeneralSeyen seyen = new SBean.DBGeneralSeyen();
							seyen.lvl= Short.parseShort(msg[index ++]);
							seyen.exp = Integer.parseInt(msg[index ++]);
							seyen.seyenTotal = Integer.parseInt(msg[index ++]);
							g.generalSeyen.add(seyen);
							g.headicon = Byte.parseByte(msg[index ++]);
							g.bless =new ArrayList<SBean.DBGeneralBless>();
							SBean.DBGeneralBless bless = new SBean.DBGeneralBless();
							bless.p1 = Float.parseFloat(msg[index ++]);
							bless.p2 = Float.parseFloat(msg[index ++]);
							bless.p3 = Float.parseFloat(msg[index ++]);
							g.bless.add(bless);
							g.bestow  =new ArrayList<SBean.DBGeneralBestow>();
							SBean.DBGeneralBestow bestow = new SBean.DBGeneralBestow();
							bestow.lvl = Short.parseShort(msg[index ++]);
							int pcount = Integer.parseInt(msg[index ++]);
							bestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
							for (int j = 0; j < pcount; j ++) {
								SBean.DBBestowLevel prop = new SBean.DBBestowLevel();
								prop.gid = Short.parseShort(msg[index ++]);
								prop.lvl = Short.parseShort(msg[index ++]);
								bestow.bestowLevel.add(prop);
							}
							g.bestow.add(bestow);
							g.official = new ArrayList<SBean.DBGeneralOfficial>();
							SBean.DBGeneralOfficial official = new SBean.DBGeneralOfficial();
							official.lvl = (short)(Short.parseShort(msg[index ++])-1);
							official.exp = Integer.parseInt(msg[index ++]);
							official.skillLvl = Short.parseShort(msg[index ++]);
							g.official.add(official);
							record.generals2.add(g);
						}
						record.pets1 = new ArrayList<SBean.DBPetBrief>();
						short pcount = Short.parseShort(msg[index ++]);
						if (pcount > 0) {
							SBean.DBPetBrief p = new SBean.DBPetBrief();
							p.name = "";
							p.id = Byte.parseByte(msg[index ++]);
							p.awakLvl = Byte.parseByte(msg[index ++]);
							p.lvl = Short.parseShort(msg[index ++]);
							p.evoLvl = Byte.parseByte(msg[index ++]);
							p.growthRate = Byte.parseByte(msg[index ++]);
							p.deformStage = Byte.parseByte(msg[index ++]);
							record.pets1.add(p);
						}
						record.pets2 = new ArrayList<SBean.DBPetBrief>();
						pcount = Short.parseShort(msg[index ++]);
						if (pcount > 0) {
							SBean.DBPetBrief p = new SBean.DBPetBrief();
							p.name = "";
							p.id = Byte.parseByte(msg[index ++]);
                            p.awakLvl = Byte.parseByte(msg[index ++]);
							p.lvl = Short.parseShort(msg[index ++]);
							p.evoLvl = Byte.parseByte(msg[index ++]);
							p.growthRate = Byte.parseByte(msg[index ++]);
							p.deformStage = Byte.parseByte(msg[index ++]);
							record.pets2.add(p);
						}
						record.relation1 = new ArrayList<SBean.DBRelation>();
						int rcount = Short.parseShort(msg[index ++]);
						for (int k = 0; k < rcount; k ++) 
						{
							SBean.DBRelation r = new SBean.DBRelation();
							r.id = Short.parseShort(msg[index ++]);
							r.lvls = new ArrayList<Short>();
							int lcount = Integer.parseInt(msg[index ++]);
							for (int j = 0; j < lcount; j++) {
								short lvl = Short.parseShort(msg[index ++]);
								r.lvls.add(lvl);
							}
							record.relation1.add(r);
						}
						record.relation2 = new ArrayList<SBean.DBRelation>();
						rcount = Short.parseShort(msg[index ++]);
						for (int k = 0; k < rcount; k ++) 
						{
							SBean.DBRelation r = new SBean.DBRelation();
							r.id = Short.parseShort(msg[index ++]);
							r.lvls = new ArrayList<Short>();
							int lcount = Integer.parseInt(msg[index ++]);
							for (int j = 0; j < lcount; j++) {
								short lvl = Short.parseShort(msg[index ++]);
								r.lvls.add(lvl);
							}
							record.relation2.add(r);
						}
						
						record.gStones1 = new ArrayList<SBean.DBGeneralStone>();
						int gcount1 = Short.parseShort(msg[index ++]);
						for (int k = 0; k < gcount1; k ++) 
						{
							SBean.DBGeneralStone r = new SBean.DBGeneralStone();
							r.id = Integer.parseInt(msg[index ++]);
							r.itemId = Integer.parseInt(msg[index ++]);
							r.exp = Integer.parseInt(msg[index ++]);
							r.gid = Byte.parseByte(msg[index ++]);
							r.pos = Byte.parseByte(msg[index ++]);
							r.passes = new ArrayList<SBean.DBGeneralStoneProp>();
							r.resetPasses = new ArrayList<SBean.DBGeneralStoneProp>();
							int lcount = Integer.parseInt(msg[index ++]);
							
							for (int j = 0; j < lcount; j++) {
								SBean.DBGeneralStoneProp pass = new SBean.DBGeneralStoneProp();
								pass.propId = Short.parseShort(msg[index ++]);
								pass.type = Byte.parseByte(msg[index ++]);
								pass.rank = Byte.parseByte(msg[index ++]);
								pass.value = Float.parseFloat(msg[index ++]);
								r.passes.add(pass);
							}
                            
							record.gStones1.add(r);
						}
						record.gStones2 = new ArrayList<SBean.DBGeneralStone>();
						gcount1 = Short.parseShort(msg[index ++]);
						for (int k = 0; k < gcount1; k ++) 
						{
							SBean.DBGeneralStone r = new SBean.DBGeneralStone();
							r.id = Integer.parseInt(msg[index ++]);
							r.itemId = Integer.parseInt(msg[index ++]);
							r.exp = Integer.parseInt(msg[index ++]);
							r.gid = Byte.parseByte(msg[index ++]);
							r.pos = Byte.parseByte(msg[index ++]);
							r.passes = new ArrayList<SBean.DBGeneralStoneProp>();
							r.resetPasses = new ArrayList<SBean.DBGeneralStoneProp>();
							int lcount = Integer.parseInt(msg[index ++]);
							
							for (int j = 0; j < lcount; j++) {
								SBean.DBGeneralStoneProp pass = new SBean.DBGeneralStoneProp();
								pass.propId = Short.parseShort(msg[index ++]);
								pass.type = Byte.parseByte(msg[index ++]);
								pass.rank = Byte.parseByte(msg[index ++]);
								pass.value = Float.parseFloat(msg[index ++]);
								r.passes.add(pass);
							}
							record.gStones2.add(r);
						}
						record.time = gs.getTime();
						role.arenaFinishAttack(rank, win > 0, record);
					}
					break;
				case "cool":
					{
						boolean bOK = role.arenaClearCool();
						sendLuaPacket(sessionid, LuaPacket.encodeArenaClearCoolRes(bOK));	
					}
					break;
				case "buytimes":
					{
						boolean bOK = role.arenaBuyTimes();
						sendLuaPacket(sessionid, LuaPacket.encodeArenaBuyTimesRes(bOK));
					}
					break;
				case "reward":
					{
//						boolean bOK = role.arenaTakeReward();
//						sendLuaPacket(sessionid, LuaPacket.encodeArenaTakeRewardRes(bOK));
					}
					break;
				case "shopsync": // TODO
					{
						Role.ShopInfo info = role.shopArenaSync();
						sendLuaPacket(sessionid, LuaPacket.encodeShopArenaSync(info));
					}
					break;
				case "shopbuy": // TODO
					{
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopArenaBuy(seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopArenaBuyRes(seq, type, id, bOK));
					}
					break;
				case "shoprefresh": // TODO
					{
						boolean bOK = role.shopArenaRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeShopArenaRefreshRes(bOK));
					}
					break;
				case "view":
					{
						final int rid = Integer.parseInt(msg[2]);
						final int rankNow = gs.getArenaManager().getRoleRank(rid);
						gs.getLoginManager().getRoleData(new SBean.RoleDataReq(
								SBean.RoleDataReq.eArenaGeneral | SBean.RoleDataReq.eBrief, rid)
							, new LoginManager.GetRoleDataCallback() {
								
								@Override
								public void onCallback(RoleDataRes res)
								{
									sendLuaPacket(sessionid, LuaPacket.encodeArenaViewRes(rid, rankNow, res));
								}
							});
					}
					break;
				case "refresh":
					{
						boolean bOK = role.arenaRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeArenaRefreshRes(bOK));					
					}
					break;
				case "setgenerals":
					{
						short[] generals = new short[5];
						int index = 2;
						for (int i = 0; i < 5; i ++)
							generals[i] = Short.parseShort(msg[index ++]);
						short pet = Short.parseShort(msg[index ++]);
						int err = role.setArenaGenerals(generals, pet);
						sendLuaPacket(sessionid, LuaPacket.encodeArenaSetGeneralsRes(err));					
					}
					break;
				case "calcresult":
					{
						int rid1 = Integer.parseInt(msg[2]);
						int rid2 = Integer.parseInt(msg[3]);
						int result = Integer.parseInt(msg[4]);
						gs.getArenaManager().setCalcResult(0, rid1, rid2, result);
					}
					break;
				case "calcresult2":
					{
						int seed = Integer.parseInt(msg[2]);
						int rid1 = Integer.parseInt(msg[3]);
						int rid2 = Integer.parseInt(msg[4]);
						int result = Integer.parseInt(msg[5]);
						gs.getArenaManager().setCalcResult(seed, rid1, rid2, result);
					}
					break;
				case "getrecord":
					{
						int recId = Integer.parseInt(msg[2]);
						int recTime = Integer.parseInt(msg[3]);
						SBean.ArenaRecord record = gs.getArenaManager().getArenaRecord(recId, recTime);
						sendLuaPacket(sessionid, LuaPacket.encodeArenaGetRecordRes(recId, record));
					}
					break;
				case "gethighrecords":
					{
						int begin = Integer.parseInt(msg[2]);
						int count = Integer.parseInt(msg[3]);
						int[] total = {0};
						List<SBean.ArenaRecord> records = gs.getArenaManager().getHighRecords(begin, count, total);
						sendLuaPacket(sessionid, LuaPacket.encodeArenaGetHighRecordsRes(records, total[0]));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "superarena":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaSyncInfo(role.superArenaSyncInfo()));
					}
					break;
				case "ranks":
					{
						role.superArenaListRanks();
					}
					break;
				case "takereward":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaTakeRankRewardRes(role.superArenaTakeRankReward()));
					}
					break;
				case "setgenerals":
					{
						SBean.DBRoleArenaFightTeam [] teams = new SBean.DBRoleArenaFightTeam[3];
						for (int i = 0; i < 3; ++i)
						{
							List<Short> generals = new ArrayList<Short>();
							int gcount = Integer.parseInt(msg[msgIndex++]);
							for (int j = 0; j < gcount; ++j)
							{
								short gid = Short.parseShort(msg[msgIndex++]);
								generals.add(gid);
							}
							List<Short> pets = new ArrayList<Short>();
							int pcount = Integer.parseInt(msg[msgIndex++]);
							for (int j = 0; j < pcount; ++j)
							{
								short pid = Short.parseShort(msg[msgIndex++]);
								pets.add(pid);
							}
							teams[i] = new SBean.DBRoleArenaFightTeam(generals, pets);
						}
						boolean bOK = role.superArenaSetGenerals(teams);
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaSetGeneralsRes(bOK));					
					}
					break;
				case "gethide":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaGetHideRes(role.superArenaGetTeamHideInfo()));	
					}
					break;
				case "sethide":
					{
						int count = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaSetHideRes(role.superArenaSetTeamHide(count)));
					}
					break;
				case "getorder":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaGetOrderRes(role.superArenaGetTeamFightOrders()));
					}
					break;
				case "setorder":
					{
						List<Integer> orders = new ArrayList<Integer>();
						int count = Integer.parseInt(msg[msgIndex++]);
						for (int i = 0; i < count; ++i)
						{
							int orderIndex = Integer.parseInt(msg[msgIndex++]);
							orders.add(orderIndex);
						}
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaSetOrderRes(role.superArenaSetTeamFightOrders(orders)));
					}
					break;
				case "search":
					{
						role.superArenaSearchEnemy();
					}
					break;
				case "view":
					{
						int rid = Integer.parseInt(msg[msgIndex++]);
						role.superArenaViewEnemy(rid);
					}
					break;
				case "startattack":
					{
						int srank = Integer.parseInt(msg[msgIndex++]);
						int trid = Integer.parseInt(msg[msgIndex++]);
						int trank = Integer.parseInt(msg[msgIndex++]);
						SBean.DBRoleArenaFightTeam[] teams = new SBean.DBRoleArenaFightTeam[3];
						for (int i = 0; i < teams.length; ++i)
						{
							List<Short> generals = new ArrayList<Short>();
							int gcount = Integer.parseInt(msg[msgIndex++]);
							for (int gi = 0; gi < gcount; ++gi)
							{
								generals.add(Short.parseShort(msg[msgIndex++]));
							}
							List<Short> pets = new ArrayList<Short>();
							int pcount = Integer.parseInt(msg[msgIndex++]);
							for (int pi = 0; pi < pcount; ++pi)
							{
								pets.add(Short.parseShort(msg[msgIndex++]));
							}
							teams[i] = new SBean.DBRoleArenaFightTeam();
							teams[i].generals = generals;
							teams[i].pets = pets;
						}
						role.superArenaStartAttack(srank, trid, trank, teams);
					}
					break;
				case "finishattack":
					{
						SBean.SuperArenaRecord record = new SBean.SuperArenaRecord();
						record.result = new ArrayList<Integer>();
						for (int i = 0; i < 3; ++i)
						{
							int win = Integer.parseInt(msg[msgIndex ++]);
							record.result.add(win);
						}
						record.seed = Integer.parseInt(msg[msgIndex ++]);
						for (int idx = 0; idx < 2; ++idx)
						{
							SBean.SuperArenaFightInfo fteam = new SBean.SuperArenaFightInfo();
							fteam.rid = Integer.parseInt(msg[msgIndex ++]);
							fteam.headIconId = Integer.parseInt(msg[msgIndex ++]);
							fteam.name = msg[msgIndex ++];
							fteam.lvl = Short.parseShort(msg[msgIndex ++]);
							fteam.teams = new ArrayList<SBean.SuperArenaTeamDetail>();
							for (int i = 0; i < 3; ++i)
							{
								List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
								short gcount = Short.parseShort(msg[msgIndex ++]);
								for (short k = 0; k < gcount; k ++) 
								{
									DBRoleGeneral g = new DBRoleGeneral();
									g.id = Short.parseShort(msg[msgIndex ++]);
									g.lvl = Short.parseShort(msg[msgIndex ++]);
									g.advLvl = Byte.parseByte(msg[msgIndex ++]);
									g.evoLvl = Byte.parseByte(msg[msgIndex ++]);
									g.skills = new ArrayList<Short>();
									for (short j = 0; j < 4; j ++)
										g.skills.add(Short.parseShort(msg[msgIndex ++]));
									g.equips = new ArrayList<SBean.DBGeneralEquip>();
									for (short j = 0; j < 6; j ++) 
									{
										byte lvl = Byte.parseByte(msg[msgIndex ++]);
										short exp = Short.parseShort(msg[msgIndex ++]);
										g.equips.add(new SBean.DBGeneralEquip(lvl, exp));								
									}
									g.weapon = new SBean.DBWeapon();
									g.weapon.passes = new ArrayList<SBean.DBWeaponProp>();
									g.weapon.resetPasses = new ArrayList<SBean.DBWeaponProp>();
									g.weapon.id = g.id;
									g.weapon.lvl = Short.parseShort(msg[msgIndex ++]);
									if (g.weapon.lvl >= 0) 
									{
										g.weapon.evo = Byte.parseByte(msg[msgIndex ++]);
										int pcount = Integer.parseInt(msg[msgIndex ++]);
										for (int j = 0; j < pcount; j ++) {
											SBean.DBWeaponProp prop = new SBean.DBWeaponProp();
											prop.propId = Short.parseShort(msg[msgIndex ++]);
											prop.type = Byte.parseByte(msg[msgIndex ++]);
											prop.value = Float.parseFloat(msg[msgIndex ++]);
											prop.rank = Byte.parseByte(msg[msgIndex ++]);
											g.weapon.passes.add(prop);
										}
									}
									g.generalSeyen =new ArrayList<SBean.DBGeneralSeyen>();
									SBean.DBGeneralSeyen seyen = new SBean.DBGeneralSeyen();
									seyen.lvl= Short.parseShort(msg[msgIndex ++]);
									seyen.exp = Integer.parseInt(msg[msgIndex ++]);
									seyen.seyenTotal = Integer.parseInt(msg[msgIndex ++]);
									g.generalSeyen.add(seyen);
									g.headicon = Byte.parseByte(msg[msgIndex ++]);
									g.bless =new ArrayList<SBean.DBGeneralBless>();
									SBean.DBGeneralBless bless = new SBean.DBGeneralBless();
									bless.p1 = Float.parseFloat(msg[msgIndex ++]);
									bless.p2 = Float.parseFloat(msg[msgIndex ++]);
									bless.p3 = Float.parseFloat(msg[msgIndex ++]);
									g.bless.add(bless);
									g.bestow  =new ArrayList<SBean.DBGeneralBestow>();
									SBean.DBGeneralBestow bestow = new SBean.DBGeneralBestow();
									bestow.lvl = Short.parseShort(msg[msgIndex ++]);
									int pcount = Integer.parseInt(msg[msgIndex++]);
									bestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
									for (int j = 0; j < pcount; j ++) {
										SBean.DBBestowLevel prop = new SBean.DBBestowLevel();
										prop.gid = Short.parseShort(msg[msgIndex ++]);
										prop.lvl = Short.parseShort(msg[msgIndex ++]);
										bestow.bestowLevel.add(prop);
									}
									g.bestow.add(bestow);
									g.official = new ArrayList<SBean.DBGeneralOfficial>();
									SBean.DBGeneralOfficial official = new SBean.DBGeneralOfficial();
									official.lvl = (short)(Short.parseShort(msg[msgIndex ++])-1);
									official.exp = Integer.parseInt(msg[msgIndex ++]);
									official.skillLvl = Short.parseShort(msg[msgIndex ++]);
									g.official.add(official);
									generals.add(g);
								}
								List<SBean.DBPetBrief> pets = new ArrayList<SBean.DBPetBrief>();
								short pcount = Short.parseShort(msg[msgIndex ++]);
								if (pcount > 0) 
								{
									SBean.DBPetBrief p = new SBean.DBPetBrief();
									p.name = "";
									p.id = Byte.parseByte(msg[msgIndex ++]);
		                            p.awakLvl = Byte.parseByte(msg[msgIndex ++]);
									p.lvl = Short.parseShort(msg[msgIndex ++]);
									p.evoLvl = Byte.parseByte(msg[msgIndex ++]);
									p.growthRate = Byte.parseByte(msg[msgIndex ++]);
									p.deformStage = Byte.parseByte(msg[msgIndex ++]);
									pets.add(p);
								}
								List<SBean.DBRelation> relation = new ArrayList<SBean.DBRelation>();
								int rcount = Short.parseShort(msg[msgIndex ++]);
								for (int k = 0; k < rcount; k ++) 
								{
									SBean.DBRelation r = new SBean.DBRelation();
									r.id = Short.parseShort(msg[msgIndex ++]);
									r.lvls = new ArrayList<Short>();
									int lcount = Integer.parseInt(msg[msgIndex ++]);
									for (int j = 0; j < lcount; j++) {
										short lvl = Short.parseShort(msg[msgIndex ++]);
										r.lvls.add(lvl);
									}
									relation.add(r);
								}
								List<SBean.DBGeneralStone> gstones = new ArrayList<SBean.DBGeneralStone>();
								int gcount1 = Short.parseShort(msg[msgIndex ++]);
								for (int k = 0; k < gcount1; k ++) 
								{
									SBean.DBGeneralStone gstone = new SBean.DBGeneralStone();
									gstone.id = Integer.parseInt(msg[msgIndex ++]);
									gstone.itemId = Integer.parseInt(msg[msgIndex ++]);
									gstone.exp = Integer.parseInt(msg[msgIndex ++]);
									gstone.gid = Byte.parseByte(msg[msgIndex ++]);
									gstone.pos = Byte.parseByte(msg[msgIndex ++]);
									gstone.passes = new ArrayList<SBean.DBGeneralStoneProp>();
									gstone.resetPasses = new ArrayList<SBean.DBGeneralStoneProp>();
									int lcount = Integer.parseInt(msg[msgIndex ++]);
									
									for (int j = 0; j < lcount; j++) {
										SBean.DBGeneralStoneProp pass = new SBean.DBGeneralStoneProp();
										pass.propId = Short.parseShort(msg[msgIndex ++]);
										pass.type = Byte.parseByte(msg[msgIndex ++]);
										pass.rank = Byte.parseByte(msg[msgIndex ++]);
										pass.value = Float.parseFloat(msg[msgIndex ++]);
										gstone.passes.add(pass);
									}
									gstones.add(gstone);
								}
								fteam.teams.add(new SBean.SuperArenaTeamDetail(generals, pets, relation,gstones));
							}
							if (idx == 0)
								record.fteam1 = fteam;
							else
								record.fteam2 = fteam;
						}
						record.time = gs.getTime();
						role.superArenaFinishAttack(record);
					}
					break;
				case "cool":
					{
						boolean bOK = role.superArenaClearCool();
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaClearCoolRes(bOK));	
					}
					break;
				case "buytimes":
					{
						boolean bOK = role.superArenaBuyTimes();
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaBuyTimesRes(bOK));
					}
					break;
				case "logs":
					{
						role.superArenaSyncLogs();
					}
					break;
				case "getrecord":
					{
						int recId = Integer.parseInt(msg[2]);
						int recTime = Integer.parseInt(msg[3]);
						SBean.SuperArenaRecord record = gs.getArenaManager().getSuperArena().getRecord(recId, recTime);
						sendLuaPacket(sessionid, LuaPacket.encodeSuperArenaGetRecordRes(recId, record));
					}
					break;
				case "shopsync": // TODO
					{
						Role.ShopInfo info = role.shopSuperArenaSync();
						sendLuaPacket(sessionid, LuaPacket.encodeShopSuperArenaSync(info));
					}
					break;
				case "shopbuy": // TODO
					{
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopSuperArenaBuy(seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopSuperArenaBuyRes(seq, type, id, bOK));
					}
					break;
				case "shoprefresh": // TODO
					{
						boolean bOK = role.shopSuperArenaRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeShopSuperArenaRefreshRes(bOK));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "march":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						role.marchSyncInfo();
					}
					break;
				case "reward":
					{
						role.marchReward();
					}
					break;
				case "reset":
					{
						role.marchReset();
					}
					break;
				case "startattack":
					{
						int msgIndex = 2;
						byte stage = Byte.parseByte(msg[msgIndex++]);
						int startTime = Integer.parseInt(msg[msgIndex++]);
						List<Short> generals = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
							generals.add(Short.parseShort(msg[msgIndex++]));
						SBean.DBPetBrief pet = null;
						byte petExist = Byte.parseByte(msg[msgIndex++]);
						if (petExist > 0) {
							pet = new SBean.DBPetBrief();
							pet.id = Byte.parseByte(msg[msgIndex ++]);
                            pet.awakLvl = Byte.parseByte(msg[msgIndex ++]);
							pet.lvl = Short.parseShort(msg[msgIndex ++]);
							pet.evoLvl = Byte.parseByte(msg[msgIndex ++]);
							pet.growthRate = Byte.parseByte(msg[msgIndex ++]);
							pet.deformStage = Byte.parseByte(msg[msgIndex ++]);
						}
						boolean useMerc = Byte.parseByte(msg[msgIndex ++]) > 0;
						role.marchStartAttack(stage, startTime, generals, pet, useMerc);
					}
					break;
				case "finishattack":
					{
						int msgIndex = 2;
						byte stage = Byte.parseByte(msg[msgIndex++]);
						byte star = Byte.parseByte(msg[msgIndex++]);
						int costTime = Integer.parseInt(msg[msgIndex++]);
						int endTime = Integer.parseInt(msg[msgIndex++]);
						short scount = Short.parseShort(msg[msgIndex++]);
						short ecount = Short.parseShort(msg[msgIndex++]);
						List<SBean.DBRoleMarchGeneralState> sgeneralStates = new ArrayList<SBean.DBRoleMarchGeneralState>();
						List<SBean.DBRoleMarchGeneralState> egeneralStates = new ArrayList<SBean.DBRoleMarchGeneralState>();
						for (short i = 0; i < scount; i ++) {
							short id = Short.parseShort(msg[msgIndex ++]);
							int hp = Integer.parseInt(msg[msgIndex ++]);
							int mp = Integer.parseInt(msg[msgIndex ++]);
							int st = Integer.parseInt(msg[msgIndex ++]);
							SBean.DBRoleMarchGeneralState state = new SBean.DBRoleMarchGeneralState(id, hp, mp, st);
							sgeneralStates.add(state);
						}
						for (short i = 0; i < ecount; i ++) {
							short id = Short.parseShort(msg[msgIndex ++]);
							int hp = Integer.parseInt(msg[msgIndex ++]);
							int mp = Integer.parseInt(msg[msgIndex ++]);
							int st = Integer.parseInt(msg[msgIndex ++]);
							SBean.DBRoleMarchGeneralState state = new SBean.DBRoleMarchGeneralState(id, hp, mp, st);
							egeneralStates.add(state);
						}
						int waveCount = Integer.parseInt(msg[msgIndex++]);;
						int dpsTotal = (int)Float.parseFloat(msg[msgIndex++]);
						int clientSpeed = Integer.parseInt(msg[msgIndex++]);
						int clientMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int clientKillMonsterCount = Integer.parseInt(msg[msgIndex++]);
						int pauseTimeTotal = Integer.parseInt(msg[msgIndex++]);
						boolean useMerc = Byte.parseByte(msg[msgIndex ++]) > 0;
						role.marchFinishAttack(stage, star < 0 ? 0 : star, costTime, endTime, sgeneralStates, egeneralStates, waveCount, dpsTotal, clientSpeed, clientMonsterCount, clientKillMonsterCount, pauseTimeTotal, useMerc);
					}
					break;
				case "syncrobots":
					{
						short count = Short.parseShort(msg[2]);
						int index = 3;
						Map<Integer, SBean.DBRoleMarchEnemy> robots = new HashMap<Integer, SBean.DBRoleMarchEnemy>();
						for (short i = 0; i < count; i ++) {
							SBean.DBRoleMarchEnemy enemy = new SBean.DBRoleMarchEnemy();
							enemy.id = Integer.parseInt(msg[index ++]);
							enemy.lvl = Short.parseShort(msg[index ++]);
							enemy.headIconID = Short.parseShort(msg[index ++]);
							enemy.name = msg[index ++];
							enemy.fname = msg[index ++];
							enemy.generals = new ArrayList<DBRoleGeneral>();
							short gcount = Short.parseShort(msg[index ++]);
							for (short k = 0; k < gcount; k ++) {
								DBRoleGeneral g = new DBRoleGeneral();
								g.id = Short.parseShort(msg[index ++]);
								g.lvl = Short.parseShort(msg[index ++]);
								g.advLvl = Byte.parseByte(msg[index ++]);
								g.evoLvl = Byte.parseByte(msg[index ++]);
								g.skills = new ArrayList<Short>();
								for (short j = 0; j < 4; j ++)
									g.skills.add(Short.parseShort(msg[index ++]));
								g.equips = new ArrayList<SBean.DBGeneralEquip>();
								for (short j = 0; j < 6; j ++) {
									byte lvl = Byte.parseByte(msg[index ++]);
									short exp = Short.parseShort(msg[index ++]);
									g.equips.add(new SBean.DBGeneralEquip(lvl, exp));								
								}
								g.weapon = new SBean.DBWeapon();
								g.weapon.passes = new ArrayList<SBean.DBWeaponProp>();
								g.weapon.resetPasses = new ArrayList<SBean.DBWeaponProp>();
								g.weapon.id = g.id;
								g.weapon.lvl = -1;
								enemy.generals.add(g);
							}
							enemy.pets = new ArrayList<SBean.DBPetBrief>();
							short pcount = Short.parseShort(msg[index ++]);
							if (pcount > 0) {
								SBean.DBPetBrief p = new SBean.DBPetBrief();
								p.id = Byte.parseByte(msg[index ++]);
								p.lvl = Short.parseShort(msg[index ++]);
								p.evoLvl = Byte.parseByte(msg[index ++]);
								p.growthRate = Byte.parseByte(msg[index ++]);
								p.deformStage = 0;
								p.name = "";
								enemy.pets.add(p);
							}
							robots.put(enemy.id, enemy);
						}
						role.marchSyncRobots(robots);
					}
					break;
				case "shopsync": // TODO
					{
						Role.ShopInfo info = role.shopMarchSync();
						sendLuaPacket(sessionid, LuaPacket.encodeShopMarchSync(info));
					}
					break;
				case "shopbuy": // TODO
					{
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopMarchBuy(seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopMarchBuyRes(seq, type, id, bOK));
					}
					break;
				case "shoprefresh": // TODO
					{
						boolean bOK = role.shopMarchRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeShopMarchRefreshRes(bOK));
					}
					break;
				case "wipe":
					{
						role.marchWipe();
					}
					break;
				}
			}
			break;
		case "sura":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						role.suraSync();
					}
					break;
				case "startattack":
					{
						int msgIndex = 2;
						byte stage = Byte.parseByte(msg[msgIndex++]);
						List<Short> generals = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
							generals.add(Short.parseShort(msg[msgIndex++]));
						SBean.DBPetBrief pet = null;
						byte petExist = Byte.parseByte(msg[msgIndex++]);
						if (petExist > 0) {
							pet = new SBean.DBPetBrief();
							pet.id = Byte.parseByte(msg[msgIndex ++]);
                            pet.awakLvl = Byte.parseByte(msg[msgIndex ++]);
							pet.lvl = Short.parseShort(msg[msgIndex ++]);
							pet.evoLvl = Byte.parseByte(msg[msgIndex ++]);
							pet.growthRate = Byte.parseByte(msg[msgIndex ++]);
							pet.deformStage = Byte.parseByte(msg[msgIndex ++]);
						}
						byte useMerc = Byte.parseByte(msg[msgIndex++]);
						stage --;
						role.suraStartAttack(stage, generals, pet, useMerc > 0);
					}
					break;
				case "finishattack":
					{
						int msgIndex = 2;
						byte stage = Byte.parseByte(msg[msgIndex++]);
						byte star = Byte.parseByte(msg[msgIndex++]);
						int count = Integer.parseInt(msg[msgIndex++]);
						List<Short> deadGids = new ArrayList<Short>();
						for (int i = 0; i < count; i ++) {
							short id = Short.parseShort(msg[msgIndex ++]);
							deadGids.add(id);
						}
						byte mercDead = Byte.parseByte(msg[msgIndex++]);
						stage --;
						role.suraFinishAttack(stage, star < 0 ? 0 : star, deadGids, mercDead>0);
					}
					break;
				case "activate":
					{
						int msgIndex = 2;
						short gid = Short.parseShort(msg[msgIndex++]);
						boolean succ = role.suraActivate(gid);
						sendLuaPacket(sessionid, LuaPacket.encodeSuraActivateRes(gid, succ));
					}
					break;
				}
			}
			break;
		case "force":
			{
				onForceChannel(role, msg, sessionid);
			}
			break;
		case "role":
			{
				switch( msg[1] )
				{
				case "buymoney":
					{
						int buyone = Integer.parseInt(msg[2]);
						List<Integer> critMultipliers = role.roleBuyMoney(buyone == 0 ? true : false);
						sendLuaPacket(sessionid, LuaPacket.encodeRoleBuyMoneyRes(critMultipliers));
					}
					break;
				case "buyvit":
					{
						boolean bOK = role.roleBuyVit();
						sendLuaPacket(sessionid, LuaPacket.encodeRoleBuyVitRes(bOK));
					}
					break;
				case "buyskillpoint":
					{
						boolean bOK = role.roleBuySkillPoint();
						sendLuaPacket(sessionid, LuaPacket.encodeRoleBuySkillPointRes(bOK));
					}
					break;
				case "setheadicon":
					{
						short headIconID = Short.parseShort(msg[2]); 
						boolean bOK = role.roleSetHeadIcon(headIconID);
						sendLuaPacket(sessionid, LuaPacket.encodeRoleSetHeadIconRes(headIconID, bOK));
					}
					break;
				case "loghelp":
					{
						int hid = Integer.parseInt(msg[2]);
						if( hid < 1 || hid > 64 )
							break;
						role.setHelpFlag(hid);
					}
					break;
				default:
					break;
				}
			}
			break;
		case "anc":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "open":
					{
						int time = Integer.parseInt(msg[2]);
						List<LoginManager.WorldChatCache.Msg> msgs = gs.getLoginManager().getWorldMsg(time);
						sendLuaPacket(sessionid, LuaPacket.encodeOpenWorldChat(msgs));
						int stampnow = 0;
						if( ! msgs.isEmpty() )
						{
							stampnow = msgs.get(msgs.size()-1).time;
						}
						if( stampnow == 0 && time > 0 )
							stampnow = time;
						role.announceOpen(true, stampnow);
					}
					break;
				case "close":
					role.announceOpen(false, 0);
					break;
				case "msg":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeWorldChatRet(role.announceNormal(msg[2]), role.getBanChatLeftTime(), role.getBanChatReason(), false));	
					}
					break;
				case "replay":
					{
						String image = msg[msgIndex++];
						String name1 = msg[msgIndex++];
						String name2 = msg[msgIndex++];
						int recId = Integer.parseInt(msg[msgIndex++]);
						int ftime = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeWorldChatRet(role.announceReplay(image, name1, name2, recId, ftime), role.getBanChatLeftTime(), role.getBanChatReason(), true));
					}
					break;
				case "recruit":
					{
						int forceID = Integer.parseInt(msg[msgIndex++]);
						String forceName = msg[msgIndex++];
						short forceIcon = Short.parseShort(msg[msgIndex++]);
						int forceMemCnt = Integer.parseInt(msg[msgIndex++]);
						int forceMaxMemCnt = Integer.parseInt(msg[msgIndex++]);
						int forceJoinLvlReq = Integer.parseInt(msg[msgIndex++]);
						String m = msg[msgIndex++];
						sendLuaPacket(sessionid, LuaPacket.encodeWorldChatRet(role.announceRecruit(forceID, forceName, forceIcon, forceMemCnt, forceMaxMemCnt, forceJoinLvlReq, m), role.getBanChatLeftTime(), role.getBanChatReason(), false));
					}
					break;
				case "test":
					{
						int[] stampNew = new int[1];
						int r = role.testChat(stampNew); 
						if( r == 1 )
							sendLuaPacket(sessionid, LuaPacket.encodeWorldChatTestRes(true, stampNew[0]));
						else if( r == 2 )
							sendLuaPacket(sessionid, LuaPacket.encodeForceChatTestRes(true, stampNew[0]));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "forcechat":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "open":
					{
						int time = Integer.parseInt(msg[2]);
						int fid = role.getForceID();
						if( fid > 0 )
						{
							List<LoginManager.WorldChatCache.Msg> msgs = gs.getLoginManager().getForceMsg(fid, time);
							sendLuaPacket(sessionid, LuaPacket.encodeOpenForceChat(msgs));
							int stampnow = 0;
							if( ! msgs.isEmpty() )
							{
								stampnow = msgs.get(msgs.size()-1).time;
							}
							if( stampnow == 0 && time > 0 )
								stampnow = time;
							role.forceChatOpen(true, stampnow);
						}
					}
					break;
				case "close":
					role.forceChatOpen(false, 0);
					break;
				case "msg":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeForceChatRet(role.forceAnnounceNormal(msg[2]), role.getBanChatLeftTime(), role.getBanChatReason()));
					}
					break;
				case "replay":
					{
						String image = msg[msgIndex++];
						String name1 = msg[msgIndex++];
						String name2 = msg[msgIndex++];
						int recId = Integer.parseInt(msg[msgIndex++]);
						int ftime = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeForceChatRet(role.forceAnnounceReplay(image, name1, name2, recId, ftime), role.getBanChatLeftTime(), role.getBanChatReason()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "privatechat":
			{
				switch( msg[1] )
				{
				case "open":
					{
						int time = Integer.parseInt(msg[2]);
						List<LoginManager.WorldChatCache.Msg> msgs = role.getPrivateMsg(time);
						sendLuaPacket(sessionid, LuaPacket.encodeOpenPrivateChat(msgs));
						int stampnow = 0;
						if( ! msgs.isEmpty() )
						{
							stampnow = msgs.get(msgs.size()-1).time;
						}
						if( stampnow == 0 && time > 0 )
							stampnow = time;
						role.privateChatOpen(true, stampnow);
					}
					break;
				case "close":
					{
						role.privateChatOpen(false, 0);
					}
					break;
				case "msg":
					{
						int rid = Integer.parseInt(msg[2]);
						sendLuaPacket(sessionid, LuaPacket.encodePrivateChatRet(role.privateAnnounce(rid, msg[3]), role.getBanChatLeftTime(), role.getBanChatReason()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "gbt":
			{
				switch( msg[1] )
				{
				case "sync":
					{
						int id = Integer.parseInt(msg[2]);
						//gs.getLogger().info("sync gbt id=" + id);
						SBean.Broadcast broadcast = null;
						List<SBean.Broadcast> allBroadcasts = gs.getLoginManager().getCurrentBroadcasts();
						for (SBean.Broadcast e : allBroadcasts)
						{
							if (e.id == id)
							{
								broadcast = e;
								break;
							}
						}
						if (broadcast != null)
						{
							sendLuaPacket2(sessionid, LuaPacket.encodeBroadcast(broadcast));
							//gs.getLogger().info("sync gbt send content id=" + id);
						}
					}
					break;
				case "vipshow":
					{
						int show = Integer.parseInt(msg[2]);
						role.setVipShow(show > 0);
						sendLuaPacket(sessionid, LuaPacket.encodeVipShowRes(show > 0));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "capturecity":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					{
						role.syncCitys();
						sendLuaPacket(sessionid, LuaPacket.encodeCitysSync(role.getBaseCitysBrief(), role.getOccupiedCitysBrief(), role.canTakeOccupyCitysReward()));
					}
					break;
				case "brief":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeCitysBrief(role.getBaseCitysBrief(), role.getOccupiedCitysBrief()));
					}
					break;
				case "cityself":
					{
						int cid = Integer.parseInt(msg[msgIndex++]);
						CityManager.InvokeRes<SBean.CityInfo> res = role.getBaseCityInfo(cid);
						sendLuaPacket(sessionid, LuaPacket.encodeCitySelfSync(res));
					}
					break;
				case "cityother":
					{
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						CityManager.InvokeRes<SBean.CityInfo> res = role.getOccupiedCityInfo(basegsid, baseroleid, cid);
						sendLuaPacket(sessionid, LuaPacket.encodeCityOtherSync(res));
					}
					break;
				case "cityowner":
					{
						int ownergsid = Integer.parseInt(msg[msgIndex++]);
						int ownerroleid = Integer.parseInt(msg[msgIndex++]);
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						role.queryCityOwner(ownergsid, ownerroleid, basegsid, baseroleid, cid);
					}
					break;
				case "guardself":
					{
						int cid = Integer.parseInt(msg[msgIndex++]);
						int guardLifeTimeType = Integer.parseInt(msg[msgIndex++]);
						List<Short> gids = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							if (gid > 0)
								gids.add(gid);
						}
						List<Short> pids = new ArrayList<Short>();
						short pid = Short.parseShort(msg[msgIndex++]);
						if (pid > 0)
							pids.add(pid);
						sendLuaPacket(sessionid, LuaPacket.encodeGuardSelfCity(role.guardBaseCity(cid, guardLifeTimeType, gids, pids)));
					}
					break;
				case "guardother":
					{
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						List<Short> gids = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							if (gid > 0)
								gids.add(gid);
						}
						List<Short> pids = new ArrayList<Short>();
						short pid = Short.parseShort(msg[msgIndex++]);
						if (pid > 0)
							pids.add(pid);
						sendLuaPacket(sessionid, LuaPacket.encodeGuardOtherCity(role.guardOccpiedCity(basegsid, baseroleid, cid, gids, pids)));
					}
					break;
				case "guardrobot":
					{
						String baseRoleName = msg[msgIndex++];
						int cid = Integer.parseInt(msg[msgIndex++]);
						int guardStartTime = Integer.parseInt(msg[msgIndex++]);
						int guardLifeTime = Integer.parseInt(msg[msgIndex++]);
						List<Short> gids = new ArrayList<Short>();
						for (int i = 0; i < 5; i ++)
						{
							short gid = Short.parseShort(msg[msgIndex++]);
							if (gid > 0)
								gids.add(gid);
						}
						List<Short> pids = new ArrayList<Short>();
						short pid = Short.parseShort(msg[msgIndex++]);
						if (pid > 0)
							pids.add(pid);
						sendLuaPacket(sessionid, LuaPacket.encodeGuardRobotCity(role.guardRobotCity(baseRoleName, cid, guardStartTime, guardLifeTime, gids, pids)));
					}
					break;
				case "search":
					{
						role.searchCity();
					}
					break;
				case "attackstart":
					{
						int gsid = Integer.parseInt(msg[msgIndex++]);
						int roleid = Integer.parseInt(msg[msgIndex++]);
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						role.attackCityStart(gsid, roleid, basegsid, baseroleid, cid);
					}
					break;
				case "attackend":
					{
						int gsid = Integer.parseInt(msg[msgIndex++]);
						int roleid = Integer.parseInt(msg[msgIndex++]);
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						byte win = Byte.parseByte(msg[msgIndex++]);
						role.attackCityEnd(gsid, roleid, basegsid, baseroleid, cid, win);
					}
					break;
				case "rewardself":
					{
						int cid = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeTakeCitySelfRewards(role.takeBaseCityReward(cid)));
					}
					break;
				case "rewardother":
					{
						int basegsid = Integer.parseInt(msg[msgIndex++]);
						int baseroleid = Integer.parseInt(msg[msgIndex++]);
						int cid = Integer.parseInt(msg[msgIndex++]);
						int guradStartTime = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeTakeCityOtherRewards(role.takeOccupyCityReward(basegsid, baseroleid, cid, guradStartTime)));
					}
					break;
				case "rewardlog":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeSyncCityRewardLog(role.getOccupyCitysRewardLog()));
					}
					break;
				case "onekeyguardself":
				    {
				        System.out.println(msg);
				        int cnt = Integer.parseInt(msg[msgIndex++]);
				        for (int j = 0; j < cnt; j++ ) {
				            int cid = Integer.parseInt(msg[msgIndex++]);
	                        int guardLifeTimeType = Integer.parseInt(msg[msgIndex++]);
	                        List<Short> gids = new ArrayList<Short>();
	                        for (int i = 0; i < 5; i ++)
	                        {
	                            short gid = Short.parseShort(msg[msgIndex++]);
	                            if (gid > 0)
	                                gids.add(gid);
	                        }
	                        List<Short> pids = new ArrayList<Short>();
	                        short pid = Short.parseShort(msg[msgIndex++]);
	                        if (pid > 0)
	                            pids.add(pid);
	                        role.guardBaseCity(cid, guardLifeTimeType, gids, pids);
				        }
				        sendLuaPacket(sessionid, ";capturecity;onekeyguardself;1;");
				    }
				    break;
				case "onekeyreward":
				    {
				        int selfCnt = Integer.parseInt(msg[msgIndex++]);
				        List<Integer> selfCities = new ArrayList<Integer>();
				        for (int i = 0; i < selfCnt; i++) {
                            selfCities.add(Integer.parseInt(msg[msgIndex++]));
                        }
				        int otherCnt = Integer.parseInt(msg[msgIndex++]);
				        List<String> otherCities = new ArrayList<String>();
				        for (int i = 0; i < otherCnt; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(msg[msgIndex++]).append(",");
                            sb.append(msg[msgIndex++]).append(",");
                            sb.append(msg[msgIndex++]).append(",");
                            sb.append(msg[msgIndex++]);
                            otherCities.add(sb.toString());
                        }
				        sendLuaPacket(sessionid, ";capturecity;onekeyreward;" + role.OneKeyCityReward(selfCities, otherCities));
				    }
				    break;
				default:
					break;
				}
			}
			break;
		case "sns":
			{
				int msgIndex = 1;
				int snsType = Integer.parseInt(msg[msgIndex++]);
				int recNum = Integer.parseInt(msg[msgIndex++]);
				int count = Integer.parseInt(msg[msgIndex++]);
				gs.getTLogger().logSns(role, recNum, count, snsType, 0);
				if (snsType == TLog.SNS_SENDEXPITEMS)
				{
					TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
					tlogEvent.addRecord(new TLogger.PetPoundRecord(TLog.PETPOUNDEVENT_SENDEXPITEMS, count));
					gs.getTLogger().logEvent(role, tlogEvent);
				}
			}
			break;
		case "rich":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					role.richSync();
					break;
				case "setgender":
					{
						byte gender = Byte.parseByte(msg[msgIndex++]);
						role.richSetGender(gender);
					}
					break;
				case "dice":
					{
						int cheat = Integer.parseInt(msg[msgIndex++]);
						role.richDice(cheat);
					}
					break;
				case "handleobj":
					{
						int action = Integer.parseInt(msg[msgIndex++]);
						role.richHandleObj(action > 0);
					}
					break;
				case "blackshop":
					role.richBlackshop();
					break;
				case "lotery":
					{
						List<Byte> numbers = new ArrayList<Byte>();
						int size = Integer.parseInt(msg[msgIndex++]);
						for (int i = 0; i < size; i ++)
							numbers.add(Byte.parseByte(msg[msgIndex++]));
						role.richLotery(numbers);
					}
					break;
				case "divine":
					{
						byte type = Byte.parseByte(msg[msgIndex++]);
						role.richDivine(type);
					}
					break;
				case "minebuypass":
					role.richMineBuyPassRes();
					break;
				case "minetax":
					role.richMineTaxRes();
					break;
				case "minestartattack":
					role.richMineStartAttackRes();
					break;
				case "minefinishattack":
					{
						byte win = Byte.parseByte(msg[msgIndex++]);
						role.richMineFinishAttackRes(win>0);
					}
					break;
				case "bossstartattack":
					role.richBossStartAttackRes();
					break;
				case "bossfinishattack":
					{
						int damage = Integer.parseInt(msg[msgIndex++]);
						int count = Integer.parseInt(msg[msgIndex++]);
						List<Short> gids = new ArrayList<Short>();
						for (int i = 0; i < count; i ++)
							gids.add(Short.parseShort(msg[msgIndex++]));
						role.richBossFinishAttackRes(damage, gids);
					}
					break;
				case "goldranks":
					{
						int rid = Integer.parseInt(msg[2]);
						List<RichManager.GoldRank> ranks = gs.getRichManager().getGoldRanks(rid);
						sendLuaPacket(sessionid, LuaPacket.encodeRichGoldRanksRes(rid, ranks));
					}
					break;
				case "damageranks":
					{
						int rid = Integer.parseInt(msg[2]);
						short combatId = Short.parseShort(msg[3]);
						List<RichManager.DamageRank> ranks = gs.getRichManager().getDamageRanks(rid, combatId);
						sendLuaPacket(sessionid, LuaPacket.encodeRichDamageRanksRes(rid, ranks));
					}
					break;
				case "buymovement":
					{
						boolean succ = role.richBuyMovement();
						sendLuaPacket(sessionid, LuaPacket.encodeRichBuyMovementRes(succ));
					}
					break;
				case "useitem":
					{
						short iid = Short.parseShort(msg[msgIndex++]);
						short cnt = Short.parseShort(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeRichUseItem(iid, cnt, role.richUseItem(iid, cnt)));
					}
					break;
				case "techtreesync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeRichTechTreeSync(role.getRichTechTree()));
					}
					break;
				case "techtreeupgrade":
					{
						int tid = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeRichTechTreeUpgrade(tid, role.upgradeRichTechTree(tid)));
					}
					break;
				case "dailytaskcanreward":
					{
						gs.getRPCManager().notifyRichTaskCanReward(sessionid, role.canTakeRichDailyTaskReward());
					}
					break;
				case "dailytasksync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeRichDailyTaskSync(role.getRichDailyTask()));
					}
					break;
				case "dailytaskrewardtake":
					{
						int tid = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeRichDailyTaskRewardTake(tid, role.takeRichDailyTaskReward(tid)));
					}
					break;
				case "stoneshopsync": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_STONE;
						Role.ShopInfo info = role.shopRichSync(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichSync(index, info));
					}
					break;
				case "stoneshopbuy": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_STONE;
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopRichBuy(index, seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichBuyRes(index, seq, type, id, bOK));
					}
					break;
				case "stoneshoprefresh": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_STONE;
						boolean bOK = role.shopRichRefresh(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichRefreshRes(index, bOK));
					}
					break;
				case "pointshopsync": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_POINT;
						Role.ShopInfo info = role.shopRichSync(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichSync(index, info));
					}
					break;
				case "pointshopbuy": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_POINT;
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopRichBuy(index, seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichBuyRes(index, seq, type, id, bOK));
					}
					break;
				case "pointshoprefresh": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_POINT;
						boolean bOK = role.shopRichRefresh(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichRefreshRes(index, bOK));
					}
					break;
				case "goldshopsync": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_GOLD;
						Role.ShopInfo info = role.shopRichSync(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichSync(index, info));
					}
					break;
				case "goldshopbuy": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_GOLD;
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopRichBuy(index, seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichBuyRes(index, seq, type, id, bOK));
					}
					break;
				case "goldshoprefresh": // TODO
					{
						int index = Role.SHOP_RICH_INDEX_GOLD;
						boolean bOK = role.shopRichRefresh(index);
						sendLuaPacket(sessionid, LuaPacket.encodeShopRichRefreshRes(index, bOK));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "invitation":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeInvitationTaskInfo(role.invitationTaskSync()));
					}
					break;
				case "taketaskreward":
					{
						int type = Integer.parseInt(msg[msgIndex++]);
						int groupID = Integer.parseInt(msg[msgIndex++]);
						int seq = Integer.parseInt(msg[msgIndex++]);
						boolean success = role.takeInvitationTaskReward(type, groupID, seq);
						sendLuaPacket(sessionid, LuaPacket.encodeInvitationTakeTaskReward(type, groupID, seq, success));
						if (success)
						{
							gs.getRPCManager().syncRolePaytoatalViplvl(sessionid, role.getStonePayTotal(), role.lvlVIP);
						}
					}
					break;	
				case "takepointsreward":
					{
						int type = Integer.parseInt(msg[msgIndex++]);
						int rid = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeInvitationTakePointsReward(type, rid, role.takeInvitationPointsReward(type, rid)));
					}
					break;
				case "friends":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeInvitationFriendsInfo(role.invitationFriendsSync()));
					}
					break;
				case "code":
					{
						String code = msg[msgIndex++];
						role.inputInvitationFriendCode(code);
					}
					break;
				case "self":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeInvitationSelfCode(role.getInvitationSelfCode()));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "qqvip":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "rewards":
					{
						sendLuaPacket(sessionid, LuaPacket.encodeQQVIPRewards(role.getQQVIPRewards()));
					}
					break;
				case "takereward":
					{
						int vipRtype = Integer.parseInt(msg[msgIndex++]);
						sendLuaPacket(sessionid, LuaPacket.encodeTakeQQVIPReward(vipRtype, role.takeQQVIPReward(vipRtype)));
					}
					break;
				case "openinit":
					{
						int openType = Integer.parseInt(msg[msgIndex++]);
						if( gs.getConfig().godMode == 1 || gs.getConfig().godMode == 2 )
						{
							sendLuaPacket(sessionid, LuaPacket.encodeQQVIPOpenInit(gs.getConfig().zoneID));
							role.addQQVipFinish(openType, true);
							sendLuaPacket(sessionid, LuaPacket.encodeQQVIPSync(role.getQQVIP(), role.isAddQQVip(1), role.isAddQQVip(2)));
						}
						else
						{
							sendLuaPacket(sessionid, LuaPacket.encodeQQVIPOpenInit(gs.getConfig().zoneID));
						}
					}
					break;
				case "openres":
					{
						int openType = Integer.parseInt(msg[msgIndex++]);
						int retCode = Integer.parseInt(msg[msgIndex++]);

						if (retCode <= 0)
						{
							role.addQQVipFinish(openType, false);
							role.queryQQVIP();
						}
					}
					break;
				default:
					break;
				}
			}
			break;
		case "relation":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					{
						List<SBean.DBRelation> relations = role.getRelations();
						byte stone = role.relationStone;
						sendLuaPacket(sessionid, LuaPacket.encodeRelationSyncRes(relations, stone));
					}
					break;
				case "activate":
					{
						short id = Short.parseShort(msg[msgIndex++]);
						short iid = Short.parseShort(msg[msgIndex++]);
						boolean res = role.activateRelation(id, iid);
						sendLuaPacket(sessionid, LuaPacket.encodeRelationActivateRes(id, iid, res));
					}
					break;
				case "upgrade":
					{
						short id = Short.parseShort(msg[msgIndex++]);
						short gid = Short.parseShort(msg[msgIndex++]);
						int[] ret = {0};
						boolean res = role.upgradeRelation(id, gid, ret);
						sendLuaPacket(sessionid, LuaPacket.encodeRelationUpgradeRes(id, gid, res, ret[0]));
					}
					break;
				case "stone":
					{
						byte stone = Byte.parseByte(msg[msgIndex++]);
						boolean res = role.setRelationStone(stone);
						sendLuaPacket(sessionid, LuaPacket.encodeRelationStoneRes(stone, res));
					}
					break;
				default:
					break;
				}
			}
			break;
		case "expiratBoss":
		{
			int msgIndex = 1;
			switch( msg[msgIndex++] )
			{
			case "sync":
				{
					List<DBExpiratBossTimes> bossInfo = role.getExpiratBossTimesInfo(role);
					int day = gs.getDayByOffset((short) 1366);
					short combatId = Short.parseShort(msg[2]);
					if(combatId>0){
						gs.getExpiratBossManager().setExpiratBoss(combatId);
					}					
					sendLuaPacket(sessionid, LuaPacket.encodeExpiratBossSyncRes(bossInfo,day,role.getExpiratBossLevel()));
				}
				break;
			case "startBattle":
				{   short combatId = Short.parseShort(msg[2]);
				    role.ExpiratBossStartAttackRes(combatId);
					
				}
				break;	
			case "finishBattle":
				{
					int damage = Integer.parseInt(msg[msgIndex++]);
					short combatId = Short.parseShort(msg[msgIndex++]);
					String serverName = msg[msgIndex++];
					role.ExpiratBossFinishAttackRes(combatId,damage,serverName, null);
				}
				break;
			case "rank":
				{
					int rid = role.id;//Integer.parseInt(msg[2])
					short combatId = Short.parseShort(msg[2]);
					List<ExpiratBossManager.DamageRank> ranks = gs.getExpiratBossManager().getDamageRanks(rid, combatId);
					sendLuaPacket(sessionid, LuaPacket.encodeExpiratBossDamageRanksRes(rid,combatId, ranks,role.getExpiratBossDamage()));
				}
				break;
			case "buyTimes":
				{
					short combatId = Short.parseShort(msg[2]);
					boolean ok=role.ExpiratBossBuyTimes(combatId,role);
					sendLuaPacket(sessionid, LuaPacket.encodeBuyTimesRes(combatId,ok, role.getExpiratBossTimes(combatId)));
				}
			break;
			default:
				break;
			}
		}
		break;
		case "heroesBoss":
		{
			int msgIndex = 1;
			switch( msg[msgIndex++] )
			{
			case "sync":
				{
					String serverName = msg[msgIndex++];
					DBHeroesBossInfo bossInfo = role.getHeroesBossTimesInfo(role,serverName);
					
					if(bossInfo!=null)
					sendLuaPacket(sessionid, LuaPacket.encodeHeroesBossSyncRes(gs.getHeroesBossManager().getHeroesBossInfo(),bossInfo));
				}
				break;	
			case "finishBattle":
				{
					short type = Short.parseShort(msg[msgIndex++]);
					int damage = Integer.parseInt(msg[msgIndex++]);
					int power = Integer.parseInt(msg[msgIndex++]);
					String serverName = msg[msgIndex++];
					role.heroesBossFinishAttackRes(type,damage,serverName,power);
				}
				break;
			case "rank":
				{
					List<HeroesRank> ranks = role.getDamageRanks();
					sendLuaPacket(sessionid, LuaPacket.encodeHeroesBossDamageRanksRes(ranks));
				}
				break;
			case "buffTimes":
				{
					boolean ok=role.heroesBossBuffTimes();
					sendLuaPacket(sessionid, LuaPacket.encodeHeroesBuyTimesRes(ok));
				}
			break;
			default:
				break;
			}
		}
		break;
		case "headicon":
		{
			int msgIndex = 1;
			switch( msg[msgIndex++] )
			{
				case "headiconSync":
				{
					DBHeadIcon ok=role.headiconSync();
					sendLuaPacket(sessionid, LuaPacket.encodeHeadiconSyncRes(ok));
				}
			    break;
				case "activeheadicon":
				{
					short type = Short.parseShort(msg[msgIndex++]);
					short ok=role.activeheadicon(type);
					sendLuaPacket(sessionid, LuaPacket.encodeActiveheadiconRes(type,ok));
				}
			    break;
				default:
					break;
			}
		}
		break;	
		case "bestow":
		{
			int msgIndex = 1;
			switch( msg[msgIndex++] )
			{
				case "bestowSync":
				{
					DBHeadIcon ok=role.headiconSync();
					sendLuaPacket(sessionid, LuaPacket.encodeHeadiconSyncRes(ok));
				}
			    break;
				case "upbestow":
				{
					short gid = Short.parseShort(msg[msgIndex++]);
					boolean ok=role.upbestow(gid);
					sendLuaPacket(sessionid, LuaPacket.encodeBestowUp(gid,ok?(short)1:(short)0));
				}
				break;
				case "bestowItem":
				{
					short iid = Short.parseShort(msg[msgIndex++]);
					short ok=role.bestowItem(iid);
					sendLuaPacket(sessionid, LuaPacket.encodeBestowItem(iid,ok));
				}
			    break;
				default:
					break;
			}
		}
		break;
		case "bless":
		{
			int msgIndex = 1;
			switch( msg[msgIndex++] )
			{
				case "upbless":
				{
					short type = Short.parseShort(msg[msgIndex++]);
					int count = Integer.parseInt(msg[msgIndex++]);
					List<Short> gids = new ArrayList<Short>();
					for (int i = 0; i < count; i ++) {
						short gid = Short.parseShort(msg[msgIndex++]);
						gids.add(gid);
					}
					boolean ok=role.upbless(gids,type);
					sendLuaPacket(sessionid, LuaPacket.encodeBlessUp(type,gids,ok?(short)1:(short)0));
				}
				break;
				case "sync":
				{
					Role.ShopInfo info = role.shopBlessSync();
					sendLuaPacket(sessionid, LuaPacket.encodeShopBlessSync(info));
				}
				break;
				case "buy":
					{
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role. shopBlessBuy(seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopBlessBuyRes(seq, type, id, bOK));
					}
					break;
				case "refresh":
					{
						boolean bOK = role.shopBlessRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeShopNormalRefreshRes(bOK));
					}
				break;
				default:
					break;
			}
		}
		break;
		
        case "duel":
			{
				int msgIndex = 1;
				switch( msg[msgIndex++] )
				{
				case "sync":
					{
						SBean.DBDuelRole dr = gs.getDuelManager().sync(role.id);
						sendLuaPacket(sessionid, LuaPacket.encodeDuelSyncRes(dr, role.getDuelFightTimes(), role.getDuelBuyTimes()));
					}
					break;
				case "join":
					{
						byte type = Byte.parseByte(msg[msgIndex++]);
						String serverName = msg[msgIndex++];
						gs.getDuelManager().join(sessionid, type, serverName, role);
					}
					break;
				case "canceljoin":
					{
						byte type = Byte.parseByte(msg[msgIndex++]);
						gs.getDuelManager().cancelJoin(type, role);
					}
					break;
				case "selectgeneral":
					{
						int count = Integer.parseInt(msg[msgIndex++]);
						List<Short> gids = new ArrayList<Short>();
						for (int i = 0; i < count; i ++) {
							short gid = Short.parseShort(msg[msgIndex++]);
							gids.add(gid);
						}
						gs.getDuelManager().selectGeneral(sessionid, role, gids);
					}
					break;
				case "startattack":
					{
						gs.getDuelManager().startAttack(sessionid, role);
					}
					break;
				case "finishattack":
					{
						int win = Integer.parseInt(msg[msgIndex++]);
						int timeSpan = Integer.parseInt(msg[msgIndex++]);
						gs.getDuelManager().finishAttack(sessionid, role, win>0, timeSpan);
					}
					break;
				case "rank":
					{
						gs.getDuelManager().rank(sessionid, role);
					}
					break;
				case "topranks":
					{
						gs.getDuelManager().topRanks(sessionid, role);
					}
					break;
				case "oppogiveup":
					{
						int timeSpan = Integer.parseInt(msg[msgIndex++]);
						gs.getDuelManager().oppoGiveup(sessionid, timeSpan, role);
					}
					break;
				case "recordlist":
					{
						List<SBean.DBDuelRecord> records = gs.getDuelManager().getRecordList(role.id);
						sendLuaPacket(sessionid, LuaPacket.encodeDuelRecordListRes(records));
					}
					break;
				case "getmatchrecord":
					{
						int rindex = Integer.parseInt(msg[msgIndex++]);
						rindex --;
						int mindex = Integer.parseInt(msg[msgIndex++]);
						mindex --;
						String names[] = {"", ""};
						short headIconIds[] = {0, 0};
						short levels[] = {0, 0};
						boolean first[] = {true};
						SBean.DBDuelMatchRecord record = gs.getDuelManager().getMatchRecord(role.id, rindex, mindex, names, headIconIds, levels, first);
						sendLuaPacket(sessionid, LuaPacket.encodeDuelMatchRecordRes(record, names, headIconIds, levels, first[0], (byte)mindex));
					}
					break;
				case "shopsync":
					{
						Role.ShopInfo info = role.shopDuelSync();
						sendLuaPacket(sessionid, LuaPacket.encodeShopDuelSync(info));
					}
					break;
				case "shopbuy":
					{
						byte seq = Byte.parseByte(msg[2]);
						byte type = Byte.parseByte(msg[3]);
						short id = Short.parseShort(msg[4]);
						boolean bOK = role.shopDuelBuy(seq, type, id);
						sendLuaPacket(sessionid, LuaPacket.encodeShopDuelBuyRes(seq, type, id, bOK));
					}
					break;
				case "shoprefresh":
					{
						boolean bOK = role.shopDuelRefresh();
						sendLuaPacket(sessionid, LuaPacket.encodeShopDuelRefreshRes(bOK));
					}
					break;
				case "buytime":
					{
						boolean bOK = role.duelBuyTime();
						byte buyTimes = 0;
						if (bOK)
							buyTimes = role.getDuelBuyTimes();
						sendLuaPacket(sessionid, LuaPacket.encodeDuelBuyTimeRes(bOK, buyTimes));
					}
					break;
				case "reconnect":
				    {
				        RTMatchData match = gs.getDuelManager().matchMap.get(role.id);
				        if (match != null && gs.getTime() < match.countDownTime + 120 && match.quit == (byte)0) {
                            sendLuaPacket(role.netsid, LuaPacket.encodeDuelReconnect(gs.getDuelManager().matchMap.get(role.id)));
                        } else {
                            sendLuaPacket(sessionid, ";duel;reconnect;0;");
                        }
				    }
				    break;
				case "finishattackonoppodownline":
				    {
				        byte attackResult = Byte.parseByte(msg[2]);
                        gs.getDuelManager().finishAttackOnOppoDownLine(sessionid, role, attackResult);
				    }
				    break;
				case "quit":
				    {
				        byte result = gs.getDuelManager().quit(role);
				        sendLuaPacket(sessionid, ";duel;quit;" + result + ";");
				    }
				    break;
				default:
					break;
				}
			}
			break;
		case "expatriate":
		        {
		                switch (msg[1])
		                {
		                case "queryrole":
		                      {
		                        
		                        if(role.stone<GameData.expirateCost){
		                        	 List<String> res = new ArrayList<String>();
				                        res.add("3");
				                        sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
				                        break;
		                        }
		                        
		                        if(role.lvl<GameData.getInstance().changeServer.cLevel){
		                        	 List<String> res = new ArrayList<String>();
				                     res.add("6");
				                     sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
				                     break;
		                        }
		                        
		                        String tarGameServer = msg[2];
		                        String openId = role.openID;
		                        if(role.forceInfo!=null&&role.forceInfo.id>0){
		                        	gs.getForceManager().sendExpirate(role.forceInfo.id, sessionid, role.id,tarGameServer,openId);
		                        }else{
		                        	sendConfirmTargetRequest(sessionid , tarGameServer, openId);
		                        }
		                        		                         
		                     }
		                     break;
		                case "transfer":
		                     {
		                        String tarGameServer = msg[2];
		                        //String openId = role.openID;
		                        if(role.stone<GameData.expirateCost){
		                        	sendLuaPacket(sessionid, ";expatriate;transfer;0;");
				                    break;
		                        }
		                        if(role.lvl<GameData.getInstance().changeServer.cLevel){
		                        	sendLuaPacket(sessionid, ";expatriate;transfer;0;");
				                    break;
		                        }
		                        transferDBRole(sessionid, role, Integer.valueOf(tarGameServer), Integer.valueOf(role.id));
		                     }
		                     break;
		                }
		        }
		        break;
		case "treasure":
			{
				switch (msg[1]) 
				{
				case "myTreasure":
				{
				    role.refreshTreasureLimiCnt();
				    SBean.DBTreasureMap map = gs.getTreasureMapManager().getRoleTreasureMap(role.id);
				    sendLuaPacket(sessionid, ";treasure;myTreasure;" + role.treasureLimitCnt.useTreasureCnt + ";" + gs.getTreasureMapManager().getTotalCount() + ";" + (map == null ? 0 : 1) + ";");
				}
				break;
				case "openMyTreasureMap":
				{
				    SBean.DBTreasureMap map = gs.getTreasureMapManager().getRoleTreasureMap(role.id);
				    sendLuaPacket(sessionid, LuaPacket.encodeOpenMyTreasureMap(map));
				}
				break;
				case "allTreasure":
				{
				    byte id = Byte.parseByte(msg[2]);
				    byte page = Byte.parseByte(msg[3]);
				    byte pageSize = Byte.parseByte(msg[4]);
				    SBean.TreasureMapBaseCFGS cfgs = gs.gameData.getTreasureMapCFG();
				    role.refreshTreasureLimiCnt();
				    int digCount = cfgs.digOtherMaxCount - role.treasureLimitCnt.digOtherCnt;
				    List<SBean.DBTreasureMap> list = gs.getTreasureMapManager().getTreasureMapList(id, role.id);
				    int totalCount = list.size();
				    int pageCount = (totalCount%pageSize) > 0 ? (totalCount/pageSize + 1) : (totalCount/pageSize);
				    List<SBean.DBTreasureMap> subList = new ArrayList<SBean.DBTreasureMap>();
				    if (totalCount%pageSize == 0 && totalCount < pageSize) {
				        subList = list.subList((page-1) * pageSize, totalCount);
				    } else {
				        if (page == pageCount) {
				            subList = list.subList((page-1) * pageSize, totalCount); 
				        } else {
				            subList = list.subList((page-1) * pageSize, pageSize * page);
				        }
				    }
				    sendLuaPacket(sessionid, LuaPacket.encodeAllTreasureMap(role.id, digCount, role.treasureLimitCnt.lastAddTime, pageCount, subList));
				}
				break;
				case "enterTreasureMap":
				{
				    byte id = Byte.parseByte(msg[2]);
				    int rid = Integer.parseInt(msg[3]);
				    SBean.DBTreasureMap map = gs.getTreasureMapManager().getTreasureMap(id, rid);
				    sendLuaPacket(sessionid, LuaPacket.encodeEnterTreasureMap(map, role.id));
				}
				break;
				case "dig":
				{
				    byte id = Byte.parseByte(msg[2]);
				    int rid = Integer.parseInt(msg[3]);
				    byte num = Byte.parseByte(msg[4]);
				    String result = role.digTreasureMap(id, num, rid);
				    sendLuaPacket(sessionid, ";treasure;dig;" + result + ";");
				}
				break;
				case "use":
				{
				    byte id = Byte.parseByte(msg[2]);
				    byte result = role.useTreasureMap(id);
				    sendLuaPacket(sessionid, ";treasure;use;" + result + ";");
				}
				break;
				default:
				    break;
				}
			}
			break;
		 case "bemonster":
         {
             switch (msg[1]) 
                 {
                 case "sync":
                     {
                         int state = gs.getBeMonsterManager().refreshState();
                         SBean.DBBeMonsterAttacker attacker = gs.getBeMonsterManager().getAttackByRid(role.id);
                         SBean.DBBeMonsterBoss boss = gs.getBeMonsterManager().getBossByRid(role.id);
                         SBean.DBBeMonsterBoss nowBoss = gs.getBeMonsterManager().getNowBoss();
                         sendLuaPacket(sessionid, LuaPacket.encodeBeMonsterSync(state, attacker, boss, nowBoss));
                     }
                     break;
                 case "register":
                     {
                         int zuanshi = Integer.parseInt(msg[2]);
                         byte result = role.beMonsterRegister(zuanshi);
                         sendLuaPacket(sessionid, ";bemonster;register;" + result + ";");
                     }
                     break;
                 case "setlineup":
                     {
                         SBean.DBBeMonsterLineup lineup = new SBean.DBBeMonsterLineup();
                         lineup.generals = new ArrayList<SBean.DBBeMonsterGeneral>();
                         lineup.id = Byte.parseByte(msg[2]);
                         short petid = Short.parseShort(msg[3]);
                         byte cnt = Byte.parseByte(msg[4]);
                         byte index = 5;
                         for (int i = 0; i < cnt; i++) {
                             SBean.DBBeMonsterGeneral g = new SBean.DBBeMonsterGeneral();
                             g.gid = Short.parseShort(msg[index++]);
                             g.baseHp = Integer.parseInt(msg[index++]);
                             lineup.generals.add(g);
                         }
                         byte result = role.beMonsterSetLineup(lineup, petid);
                         sendLuaPacket(sessionid, ";bemonster;setlineup;" + result + ";");
                     }
                     break;
                 case "uphp":
                     {
                         byte lineupId = Byte.parseByte(msg[2]);
                         byte result = role.beMonsterUpHp(lineupId);
                         sendLuaPacket(sessionid, ";bemonster;uphp;" + result + ";");
                     }
                     break;
                 case "attack":
                     {
                         byte lineupId = Byte.parseByte(msg[2]);
                         DBBeMonsterLineup lineup = role.beMonsterStartAttack(lineupId);
                         sendLuaPacket(sessionid, LuaPacket.encodeBeMonsterStartAttack(lineup));
                     }
                     break;
                 case "finishattack":
                     {
                         byte lineupid = Byte.parseByte(msg[2]);
                         int totalAttackValue = Integer.parseInt(msg[3]);
                         byte cnt = Byte.parseByte(msg[4]);
                         int index = 4;
                         List<SBean.DBBeMonsterGeneral> generals = new ArrayList<SBean.DBBeMonsterGeneral>();
                         for (int i = 0; i < cnt; i++) {
                             SBean.DBBeMonsterGeneral g = new SBean.DBBeMonsterGeneral();
                             g.gid = Short.parseShort(msg[index++]);
                             g.hp = Integer.parseInt(msg[index++]);
                         }
                         byte result = role.beMonsterFinishAttack(lineupid, totalAttackValue, generals);
                         sendLuaPacket(sessionid, ";bemonster;finishattack;" + result + ";");
                     }
                     break;
                 case "ranklist":
                     {
                         byte page = Byte.parseByte(msg[2]);
                         byte pageSize = Byte.parseByte(msg[3]);
                         List<SBean.DBBeMonsterAttacker> list = gs.getBeMonsterManager().getAttackValueRank();
                         int totalCount = list.size();
                         int pageCount = (totalCount%pageSize) > 0 ? (totalCount/pageSize + 1) : (totalCount/pageSize);
                         
                         List<SBean.DBBeMonsterAttacker> subList = new ArrayList<SBean.DBBeMonsterAttacker>();
                         if (totalCount%pageSize == 0 && totalCount < pageSize) {
                             subList = list.subList((page-1) * pageSize, totalCount);
                         } else {
                             if (page == pageCount) {
                             } else {
                             }
                         }
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
		if(role!=null)
		role.checkHeadTime();
	}

	public void onShopRand(final Role role, String[] msg, final int sessionid) {
		switch( msg[1] )
		{
		case "sync":
			{
				byte shopid = Byte.parseByte(msg[2]);
				Role.ShopInfo info = role.shopRandSync(shopid);
				sendLuaPacket(sessionid, LuaPacket.encodeShopRandSync(shopid, info));
			}
			break;
		case "buy":
			{
				byte shopid = Byte.parseByte(msg[2]);
				byte seq = Byte.parseByte(msg[3]);
				byte type = Byte.parseByte(msg[4]);
				short id = Short.parseShort(msg[5]);
				boolean bOK = role.shopRandBuy(shopid, seq, type, id);
				sendLuaPacket(sessionid, LuaPacket.encodeShopRandBuyRes(shopid, seq, type, id, bOK));
			}
			break;
		case "refresh":
			{
				byte shopid = Byte.parseByte(msg[2]);
				boolean bOK = role.shopRandRefresh(shopid);
				sendLuaPacket(sessionid, LuaPacket.encodeShopRandRefreshRes(shopid, bOK));
			}
			break;
		case "summon":
			{
				byte shopid = Byte.parseByte(msg[2]);
				boolean bOK = role.shopRandSummon(shopid);
				sendLuaPacket(sessionid, LuaPacket.encodeShopRandSummonRes(shopid, bOK));
			}
			break;
		case "brief":
			{
				List<ShopRandBrief> lst = role.shopRandGetBrief();
				sendLuaPacket(sessionid, LuaPacket.encodeShopRandBrief(lst));
			}
			break;
		default:
			break;
		}
	}

	public void onShopNormal(final Role role, String[] msg, final int sessionid) {
		switch( msg[1] )
		{
		case "sync":
			{
				Role.ShopInfo info = role.shopNormalSync();
				sendLuaPacket(sessionid, LuaPacket.encodeShopNormalSync(info));
			}
			break;
		case "buy":
			{
				byte seq = Byte.parseByte(msg[2]);
				byte type = Byte.parseByte(msg[3]);
				short id = Short.parseShort(msg[4]);
				boolean bOK = role.shopNormalBuy(seq, type, id);
				sendLuaPacket(sessionid, LuaPacket.encodeShopNormalBuyRes(seq, type, id, bOK));
			}
			break;
		case "refresh":
			{
				boolean bOK = role.shopNormalRefresh();
				sendLuaPacket(sessionid, LuaPacket.encodeShopNormalRefreshRes(bOK));
			}
			break;
		default:
			break;
		}
	}

	public void onPetMessage(final Role role, String[] msg, final int sessionid) {
		switch( msg[1] )
		{
		case "upgradeevo":
			{
				short pid = Short.parseShort(msg[2]);
				boolean bOK = role.petsUpgradeEvo(pid);
				sendLuaPacket(sessionid, LuaPacket.encodePetsUpgradeEvoRes(pid, bOK));
			}
			break;
		case "listbreed":
			{
				short pid = Short.parseShort(msg[2]);
				List<SBean.FriendBreedPet> lst = role.petsListBreed(pid);
				sendLuaPacket(sessionid, LuaPacket.encodePetsListBreed(pid, lst));
			}
			break;
		case "breed":
			{
				short pid = Short.parseShort(msg[2]);
				String openID = msg[3];
				int serverID = Integer.parseInt(msg[4]);
				int roleID = Integer.parseInt(msg[5]);
				short eggItemID = role.petsBreed(pid, openID, new SBean.GlobalRoleID(serverID, roleID));
				sendLuaPacket(sessionid, LuaPacket.encodePetsBreedRes(pid, openID, serverID, roleID, eggItemID));
			}
			break;
		case "settop":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsSetTopRes(pid, role.petsSetTop(pid)));
			}
			break;
		case "poundupgrade":
			{
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundUpgradeRes(role.petsPoundUpgrade()));
			}
			break;
		case "poundput":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundPutRes(pid, role.petsPoundPut(pid)));
			}
			break;
		case "poundget":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundGetRes(pid, role.petsPoundGet(pid)));
			}
			break;
		case "hungry":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundHungryRes(pid, role.petsPoundHungry(pid)));
			}
			break;
		case "feed":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundFeedRes(pid, role.petsPoundFeed(pid)));
			}
			break;
		case "produce":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundProduceRes(pid, role.petsPoundProduce(pid)));
			}
			break;
		case "getproduct":
			{
				short pid = Short.parseShort(msg[2]);
				sendLuaPacket(sessionid, LuaPacket.encodePetsPoundGetProductRes(pid, role.petsPoundGetProduct(pid)));
			}
			break;
		case "changename":
			{
				short pid = Short.parseShort(msg[2]);
				String nName = msg[3];
				List<String> retName = new ArrayList<String>();
				byte ret = role.petsChangeName(pid, nName, retName);
				if (retName.size() > 0)
					nName = retName.get(0);
				sendLuaPacket(sessionid, LuaPacket.encodePetsChangeNameRes(pid, nName, ret));
			}
			break;
		case "deformsync":
			{
				role.petsDeformSync(sessionid);
			}
			break;
		case "deformfeed":
			{
				short pid = Short.parseShort(msg[2]);
				byte itemIndex = Byte.parseByte(msg[3]);
				role.petsDeformFeed(sessionid, pid, itemIndex);
			}
			break;
		case "deformzan":
			{
				short pid = Short.parseShort(msg[2]);
				role.petsDeformZan(sessionid, pid);
			}
			break;
		case "deformzanoffer":
			{
				short pid = Short.parseShort(msg[2]);
				short reward = Short.parseShort(msg[3]);
				short count = Short.parseShort(msg[4]);
				String message = msg[5];
				role.petsDeformZanOffer(sessionid, pid, reward, count, message);
			}
			break;
		case "deformtakeoffer":
			{
				int rid = Integer.parseInt(msg[2]);
				short pid = Short.parseShort(msg[3]);
				short reward = Short.parseShort(msg[4]);
				role.petsDeformTakeOffer(sessionid, rid, pid, reward);
			}
			break;
		case "deformtry":
			{
				short pid = Short.parseShort(msg[2]);
				role.petsDeformTry(sessionid, pid);
			}
			break;
		case "deformbuytry":
			{
				short pid = Short.parseShort(msg[2]);
				role.petsDeformBuyTry(sessionid, pid);
			}
			break;
		case "awakelevel":
			{
				short pid = Short.parseShort(msg[2]);
				byte level = 0;
				Pet p = role.pets.get(pid);
				if (p != null) {
				    level = p.awakeLvl;
				}
				sendLuaPacket(sessionid, ";pets;awakelevel;" + level + ";");
			}
			break;
		case "awake":
			{
				short pid = Short.parseShort(msg[2]);
				byte reslut = role.petsAwake(pid);
				sendLuaPacket(sessionid, ";pets;awake;" + reslut + ";");
			}
			break;
		default:
			break;
		}
	}
	
	
	private void transferDBRole(final int sid, final Role role, final int targs, final int roleid)
        {
	       synchronized (role)
	       {
	               final DBRole dbrole = role.copyDBRoleWithoutLock();
	               if (dbrole!=null)
                        {
                                gs.getExchangeManager().getExpatriateService().sendExpatriateTransRoleRequest(targs, dbrole, new ExchangeManager.ExpatriateCallback()
                                {
                                        @Override
                                        public void onCallback(int errorCode, String[] msg)
                                        {
                                                if (errorCode==0)
                                                {
                                                	    gs.getLogger().warn("ExpatriateUserTrans 1 transferDBRole dbrole.id:"+dbrole.id+" time:"+gs.getTime());
                                                        gs.getLoginManager().banRole(dbrole.id, -1, "user expatriate gs", new LoginManager.ModifyRoleDataCallback()
                                                        {
                                                                @Override
                                                                public void onCallback(int errcode)
                                                                {
                                                                       //if (errcode==ERR_OK)
                                                                       {
                                                                               gs.getLogger().warn("ExpatriateUserTrans  2 Do Expatirate Finished dbrole.id:"+dbrole.id+" time:"+gs.getTime());
                                                                               sendLuaPacket(sid, ";expatriate;transfer;1;");
                                                                       }
                                                                }
                                                        });
                                                }
                                                else
                                                {
                                                        gs.getLogger().warn(errorCode + "ExpatriateUserTrans Do Expatirate Occur Error");
                                                        sendLuaPacket(sid, ";expatriate;transfer;0;");
                                                }
                                        }
                                });
                        }else
                        {
                                
                               gs.getLogger().warn("ExpatriateUserTrans dbRole is null! Do Expatirate Occur Error");
                               sendLuaPacket(sid, ";expatriate;transfer;0;");
                        }
	       }
	       
	       /*
	       gs.getLoginManager().execCommonDBRoleVisitor(roleid, new CommonRoleVisitor()
	       {
                        @Override
                        public byte visit(final DBRole dbrole)
                        {
                                if (dbrole!=null)
                                {
                                        //after moment , here will do a deepcopy for dbrole, but now it simple used
                                        gs.getExchangeManager().getExpatriateService().sendExpatriateTransRoleRequest(targs, dbrole, new ExchangeManager.ExpatriateCallback()
                                        {
                                                @Override
                                                public void onCallback(int errorCode, String[] msg)
                                                {
                                                        if (errorCode==0)
                                                        {
                                                                // TODO transfer dbrole finished , do the rest things 
                                                                gs.getLoginManager().banRole(dbrole.id, -1, "user expatriate gs", new LoginManager.ModifyRoleDataCallback()
                                                                {
                                                                        @Override
                                                                        public void onCallback(int errcode)
                                                                        {
                                                                               //if (errcode==ERR_OK)
                                                                               {
                                                                                       gs.getLogger().info("Do Expatirate Finished");
                                                                                       sendLuaPacket(sid, ";expatriate;transfer;true;");
                                                                               }
                                                                        }
                                                                });
                                                        }
                                                        else
                                                        {
                                                                gs.getLogger().error(errorCode + "Do Expatirate Occur Error");
                                                                sendLuaPacket(sid, ";expatriate;transfer;false;");
                                                        }
                                                }
                                        });
                                }
                                return 0;
                        }
                        
                        @Override
                        public boolean visit(Role role)
                        {
                                // here the call-method ensure this method will not be invoked
                                return false;
                        }
                        
                        @Override
                        public void onCallback(boolean bDB, byte errcode, String rname)
                        {
                                // TODO Auto-generated method stub
                                
                        }
	       }); 
	       */
        }

        public void sendConfirmTargetRequest(final int sessionid,
                        String tarGameServer, final String openId)
        {
	       List<String> req = new ArrayList<String>();
               req.add("expatriate");
               req.add("queryrole");
               req.add(tarGameServer);
               req.add(openId);
               
               try {
                      int targs = Integer.valueOf(tarGameServer); 
                      gs.getExchangeManager().getExchangeService().sendExchangeRequest(targs, req, new ExchangeManager.ExchangeCallback(){
                        @Override
                        public void onCallback(int errorCode, String[] msg)
                        {
                                List<String> res = new ArrayList<String>();
                                
                                if (errorCode == ExchangeManager.REQUEST_RESPONSE_SUCCESS && msg!=null && msg.length>2)
                                {
                                        String roleID = msg[3];
                                       
                                       if(roleID==null||"0".equals(roleID)){
                                    	   res.add("0");
                                           gs.getLogger().error(errorCode + "when query has  role error ");
                                           sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
                                       }else{
                                    	   res.add("1");
                                           sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
                                           res.add(openId);
                                           res.add(roleID);
                                           gs.getLogger().info("confirm gs has role " + res);
                                       }
                                	    
                                }
                                else
                                {
                                        res.add("2");
                                        gs.getLogger().error(errorCode + "when query has  role error ");
                                        sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
                                }
                        }
                      });
               }catch(NumberFormatException e)
               {
                       e.printStackTrace();
                       gs.getLogger().warn("when receive a queryRole request from client get a wrong paramater targs:"+tarGameServer);
                       List<String> res = new ArrayList<String>();
                       res.add("5");
                       sendLuaPacket(sessionid, LuaPacket.encodeExpatriateQueryRoleResponse(res));
	       }
                
        }

        public void sendLuaPacket(int sid, String data)
	{
		gs.getLogger().debug("send lua packet:[" + data + "]");
		tgs.sendPacket(sid, new Packet.S2C.LuaChannel(data));
	}
	
	public void sendLuaPacket(int sid, List<String> data)
	{
		gs.getLogger().debug("send lua packet:[" + data + "]");
		tgs.sendPacket(sid, new Packet.S2C.LuaChannel2(data));
	}
	
	public void sendLuaPacket2(int sid, List<String> data)
	{
		gs.getLogger().debug("send lua packet:[" + data + "]");
		tgs.sendPacket(sid, new Packet.S2C.LuaChannel2(data));
	}
	
	public void notifyAutoUserID(int sid, int uid)
	{
		sendLuaPacket(sid, LuaPacket.encodeAutoUserID(uid));
	}
	
	public void notifyUserLoginResponse(int sid, byte err, byte state, SBean.RoleBrief brief, List<DBRoleGeneral> generals
			, List<SBean.DBPet> pets, List<SBean.DBFeedingPet> petsFeeding, short petsTop, List<DBRoleItem2> items, List<SBean.DBRoleEquip> equips
			, List<SBean.DBCombatLog> combatFightLogs, List<SBean.DBCombatLog> combatResetLogs, List<SBean.DBCombatPos> combatPos, List<SBean.DBTaskPos> taskPos
			, byte lvlVIP, int payTotal, int feedTotal, List<SBean.DBPetDeform> petsDeform, int activity, byte takeTimes, List<SBean.DBRelation> relations,int seyen)
	{
		if( brief != null )
		{
			sendLuaPacket(sid, LuaPacket.encodeVIPUpdate(lvlVIP, payTotal, false));
			syncRoleBrief(sid, brief);
			sendLuaPacket(sid, LuaPacket.encodeGeneralsSync(generals));
			sendLuaPacket(sid, LuaPacket.encodePetsSync(pets, petsFeeding, petsTop, feedTotal, petsDeform, activity, takeTimes));
			syncRoleItems(sid, items);
			syncRoleEquips(sid, equips);
			sendLuaPacket(sid, LuaPacket.encodeCombatDataSync(combatFightLogs, combatResetLogs, combatPos));
			sendLuaPacket(sid, LuaPacket.encodeTaskPosSync(taskPos));
			sendLuaPacket(sid, LuaPacket.encodeRelationSync(relations));
			sendLuaPacket(sid, LuaPacket.encodeRoleLevelLimit(gs.getRoleLevelLimit(),seyen));
		}
		sendLuaPacket(sid, LuaPacket.encodeUserLogin(err, state, ""));
		sendLuaPacket(sid, LuaPacket.encodeGlobalState(gs.getLoginManager().getGlobalState()));
	}
	
	public void syncRoleBrief(int sid, SBean.RoleBrief brief)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleBriefSync(brief));
	}
	public void syncRoleItems(int sid, List<SBean.DBRoleItem2> items)
	{
		sendLuaPacket(sid, LuaPacket.encodeItemsSync(items));
	}
	public void notifyUserStones(int sid, List<DBGeneralStone> generalStones)
	{
		sendLuaPacket(sid, LuaPacket.encodeStonesSyncRes(generalStones));
	}
	
	public void syncRoleEquips(int sid, List<SBean.DBRoleEquip> equips)
	{
		sendLuaPacket(sid, LuaPacket.encodeEquipsSync(equips));
	}
	
	public void notifyCreateForceRes(int sid, int fid)
	{
		sendLuaPacket(sid, LuaPacket.encodeCreateForceRes(fid));
	}
	
	public void syncRoleStone(int sid, int stone)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleStoneSync(stone));
	}
	
	public void syncRoleMoney(int sid, int money)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleMoneySync(money));
	}
	
	public void syncRoleVit(int sid, int vit)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleVitSync(vit));
	}
	
	public void syncRoleName(int sid, String name)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleNameSync(name));
	}
	
	public void syncRoleLvlExp(int sid, int lvl, int exp)
	{
		sendLuaPacket(sid, LuaPacket.encodeRoleLvlExpSync(lvl, exp));
	}
	
	public void syncRolePaytoatalViplvl(int sid, int paytotal, int viplvl)
	{
		sendLuaPacket(sid, LuaPacket.encodeRolePayVipSync(paytotal, viplvl));
	}
	
	public void notifyForceInfo(int sid, int fid, ForceManager.ForceInfo info)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceInfo(fid, info));
	}
	
	public void notifyForceStamp(int sid, int fid, int stamp)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceStamp(fid, stamp));
	}
	
	public void notifyForceMembers(int sid, int fid, List<ForceManager.MemberInfo> members, List<DBForce.Manager> managers)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceMembers(fid, members, managers));
	}
	
	public void notifyForceApply(int sid, int fid, List<ForceManager.ApplyInfo> members)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceApply(fid, members));
	}
	
	public void notifyForceLogs(int sid, int fid, List<DBForce.History> logs)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceLogs(fid, logs));
	}
	
	public void notifyForceActivity(int sid, int fid, int rid, int selfActivity, int activity, int forceActivity, List<Byte> activityRewards)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceActivity(fid, rid, selfActivity, activity, forceActivity, activityRewards));
	}
	
	public void notifyForceActivityReward(int sid, int rid, boolean ok, int money, SBean.DropEntry drop)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceActivityReward(rid, ok, money, drop));
	}
	
	public void notifyDailyActivityReward(int sid, int reward, boolean ok, List<SBean.DropEntryNew> drops)
	{
		sendLuaPacket(sid, LuaPacket.encodeDailyActivityReward(reward, ok, drops));
	}
	
	public void notifyForceSetAnnounce(int sid, int fid, int ret)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceAnnounceRes(fid, ret));
	}
	
	public void notifyForceSetQQ(int sid, int fid, int ret)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceQQRes(fid, ret));
	}
	
	public void notifyForceCmnRes(int sid, int fid, int ret)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceCmnRes(fid, ret));
	}
	
	public void notifyForceJoin(int sid, int fid, byte res)
	{
		if( sid > 0 )
			sendLuaPacket(sid, LuaPacket.encodeForceJoin(fid, res));
	}
	
	public void notifyForceAccept(int sid, byte acc, int modTime, byte res)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceAccept(acc, modTime, res));
	}
	
	public void notifyForceList(int sid, List<ForceManager.ForceBrief> lst)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceList(lst));
	}
	
	public void notifyForceInfoInit(int sid, int fid, String fname)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceInfoInit(fid, fname));
	}
	
	public void notifyForceApplyExist(int sid, boolean applyExist)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceApplyExist(applyExist));
	}
	
	public void notifyForceSetIcon(int sid, int modTime, int newicon)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceSetIcon(modTime, newicon));
	}
	
	public void notifyForceSetJoin(int sid, int fid, int modTime, byte joinMode, short joinLvlReq)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceSetJoin(fid, modTime, joinMode, joinLvlReq));
	}

	public void notifyForceDismissRes(int sid, int fid, int modTime, short itemId, int itemCount)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceDismiss(fid, modTime, itemId, itemCount));
	}

	
	public void notifyForceBattleSync(int sid, int fid, SBean.ForceBattleCFGS cfg, List<ForceWarManager.RolePlayerInfo> rps, SBean.DBForceBeast beast)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceSyncBattle(fid, cfg, rps, beast));
	}
	
	public void notifyForceBattleOpen(int sid, int fid, int bid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattleOpen(fid, bid, bOk));
	}
	
	public void notifyForceBattlePlayerJoin(int sid, int fid, int bid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattlePlayerJoin(fid, bid, bOk));
	}
	
	public void notifyForceBattlePlayerQuit(int sid, int fid, int bid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattlePlayerQuit(fid, bid, bOk));
	}
	
	public void notifyForceBattlePlayerPosChange(int sid, int fid, int bid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattlePlayerPosChange(fid, bid, bOk));
	}
	
	public void notifyForceBattleSyncContribution(int sid, int fid, int bid, int startTime, int endTime, int selectionTimeStart, List<ForceWarManager.PlusEffectItem> items)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattleSyncContribution(fid, bid, startTime, endTime, selectionTimeStart, items));
	}
	
	public void notifyForceBattlePlayerContribute(int sid, int fid, int bid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattlePlayerContribute(fid, bid, bOk));
	}
	
	public void notifyForceBattleQuiz(int sid, int bid, int qid, boolean bOk)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceBattleQuiz(bid, qid, bOk));
	}
	
	public void notifyMailAcceptFriend(int sid, int modTime, int res, byte acc)
	{
		sendLuaPacket(sid, LuaPacket.encodeMailAcceptFriend(modTime, res, acc));
	}
	
	public void notifyMailNew(int sid)
	{
		sendLuaPacket(sid, LuaPacket.encodeMailNew());
	}
	
	public void notifyBroadcasts(int sid, List<Integer> ids)
	{
		sendLuaPacket(sid, LuaPacket.encodeBroadcastIDs(ids));
		//gs.getLogger().info("notifyBroadcasts id size="+ids.size());
	}
	
	public void notifyBanPlayInfo(int sid, List<LoginManager.BanInfo> banPlayInfo)
	{
		sendLuaPacket(sid, LuaPacket.encodeBanPlayInfo(banPlayInfo));
	}
	
	public void notifyRichTaskCanReward(int sid, boolean canReward)
	{
		sendLuaPacket(sid, LuaPacket.encodeRichCanReward(canReward));
	}
	
//	public void notifyNewCityOpen(int sid)
//	{
//		sendLuaPacket(sid, LuaPacket.encodeNewCityOpen());
//	}
	
	public void notifyRPCFailed(int sid)
	{
		tgs.sendPacket(sid, new Packet.S2C.CmnRPCResponse(new SBean.RPCRes(SBean.RPCRes.eFailed)));
	}
	
	public void forceClose(int sid, byte errcode)
	{
		sendLuaPacket(sid, LuaPacket.encodeForceClose(errcode));
	}
	
	public void notifyGetRoleByIDResult(int sid, SBean.DBRoleBrief brief)
	{
		List<SBean.DBRoleBrief> lst = new ArrayList<SBean.DBRoleBrief>();
		if( brief != null )
			lst.add(brief);
		sendLuaPacket(sid, LuaPacket.encodeRandomRoles(GameData.RANDOMROLES_FIND, lst));
	}
	
	public void notifyTaskComplete(int sid, int roleid, short scriptID)
	{
		gs.getLogger().debug("role " + roleid + " notify task complete, scriptID = " + scriptID);
		//tgs.sendPacket(sid, new Packet.S2C.TaskComplete(scriptID));
	}
	
	public void notifyDataModify(int sid, SBean.DataModifyRes res)
	{
		gs.getLogger().warn("gm datamod, gmid=" + res.req.gm
				+ ", res=" + res.res
				+ ", rid=" + res.req.rid
				+ ", dtype=" + res.req.dtype
				+ ", mtype=" + res.req.mtype
				+ ", id=" + res.req.id
				+ ", val=" + res.req.val
				+ ", old=" + res.oldVal
				+ ", new=" + res.newVal);
		tcs.sendPacket(sid, new Packet.S2M.DataModify(res));
	}
	
	public void notifySysMessage(int sid, byte res)
	{
		tcs.sendPacket(sid, new Packet.S2M.SysMessage(res));
	}
	
	public void notifyUserMessage(int sid, byte res)
	{
		sendLuaPacket(sid, LuaPacket.encodeMailSendRes(res));
	}
	
	public void notifySearchCityResult(int sid, SBean.SearchCityRes res)
	{
		gs.getLogger().debug("notifySearchCityResult ==> error=" + res.error);
		sendLuaPacket(sid, LuaPacket.encodeSearchCity(res));
	}
	
	public void notifyAttackCityStartResult(int sid, SBean.AttackCityStartRes res)
	{
		gs.getLogger().debug("notifyAttackCityStartResult ==> error=" + res.error);
		sendLuaPacket(sid, LuaPacket.encodeAttackCityStart(res));
	}
	
	public void notifyAttackCityEndResult(int sid, SBean.AttackCityEndRes res)
	{
		gs.getLogger().debug("notifyAttackCityEndResult ==> error=" + res.error);
		sendLuaPacket(sid, LuaPacket.encodeAttackCityEnd(res));
	}
	
	public void notifyQueryCityOwnerResult(int sid, SBean.QueryCityOwnerRes res)
	{
		gs.getLogger().debug("notifyQueryCityOwnerResult ==> error=" + res.error);
		sendLuaPacket(sid, LuaPacket.encodeQueryCityOwner(res));
	}
	
	public void friendAnnounceCreateRole(String openID, int roleID)
	{
		tfc.sendPacket(new Packet.S2F.CreateRole(openID, new SBean.GlobalRoleID(gs.getConfig().id, roleID)));
	}
	
	public void friendSearchFriends(int roleID, List<String> openIDs)
	{
		tfc.sendPacket(new Packet.S2F.SearchFriends(new SBean.SearchFriendsReq(new SBean.GlobalRoleID(gs.getConfig().id, roleID), openIDs)));
	}
	
	public void friendSetFriendPropRes(SBean.QueryFriendPropReq req, SBean.FriendProp prop)
	{
		tfc.sendPacket(new Packet.S2F.QueryFriendProp(new SBean.QueryFriendPropRes(req, prop)));
	}
	
	public void friendUpdateFriendProp(SBean.UpdateFriendPropReq req)
	{
		tfc.sendPacket(new Packet.S2F.UpdateFriendProp(req));
	}
	
	public void friendSendMessageToFriend(SBean.GlobalRoleID srcID, SBean.GlobalRoleID dstID, byte msgType, short arg1, int arg2)
	{
		tfc.sendPacket(new Packet.S2F.SendMessageToFriend(srcID, dstID, msgType, arg1, arg2));
	}
	
	public void exchangeSendSearchCityReq(SBean.SearchCityReq req)
	{
		tec.sendPacket(new Packet.S2E.SearchCity(req));
	}
	
	public void exchangeSendSearchCityRes(SBean.SearchCityRes res)
	{
		tec.sendPacket(new Packet.S2E.SearchCityForward(res));
	}
	
	public void exchangeSendAttackCityStartReq(SBean.AttackCityStartReq req)
	{
		tec.sendPacket(new Packet.S2E.AttackCityStart(req));
	}
	
	public void exchangeSendAttackCityStartRes(SBean.AttackCityStartRes res)
	{
		tec.sendPacket(new Packet.S2E.AttackCityStartForward(res));
	}
	
	public void exchangeSendAttackCityEndReq(SBean.AttackCityEndReq req)
	{
		tec.sendPacket(new Packet.S2E.AttackCityEnd(req));
	}
	
	public void exchangeSendAttackCityEndRes(SBean.AttackCityEndRes res)
	{
		tec.sendPacket(new Packet.S2E.AttackCityEndForward(res));
	}
	
	public void exchangeSendQueryCityOwnerReq(SBean.QueryCityOwnerReq req)
	{
		tec.sendPacket(new Packet.S2E.QueryCityOwner(req));
	}
	
	public void exchangeSendQueryCityOwnerRes(SBean.QueryCityOwnerRes res)
	{
		tec.sendPacket(new Packet.S2E.QueryCityOwnerForward(res));
	}
	
	public void exchangeSendNotifyCityOwnerChangedReq(SBean.NotifyCityOwnerChangedReq req)
	{
		tec.sendPacket(new Packet.S2E.NotifyCityOwnerChanged(req));
	}
    
    /*
	public void exchangeSendQueryUserHasRoleRes(QueryHasRoleRes res)
        {
                tec.sendPacket(new Packet.S2E.QueryUserHasRolesForward(res));
        }
        */
	
	public void notifyArenaAttacked(int sid, boolean attacked)
	{
		sendLuaPacket(sid, LuaPacket.encodeArenaAttacked(attacked));
	}
	
	public void notifySuperArenaAttacked(int sid, boolean attacked)
	{
		sendLuaPacket(sid, LuaPacket.encodeSuperArenaAttacked(attacked));
	}
	
	public void sendExchangeChannelMsgReq(int tag, int msgid, int gsid, List<String> data)
	{
		gs.getLogger().debug("send exchange channel msg req:[" + data + "]");
		tec.sendPacket(new Packet.S2E.ChannelMessageReq(new SBean.ExchangeChannelMessage(tag, msgid, gs.getConfig().id, gsid, data)));
	}
	public void sendExpatriateDBRoleReq(int tag, int taskID, int dstgsID,
                        DBRole dbRole)
        {
	        gs.getLogger().warn("ExpatriateUserTrans send expriate transfer dbrole req " + dstgsID+",curGsID:"+gs.getConfig().id+"useName:"
	        		+dbRole.userName+" roleId:"+dbRole.id);	        
	        SBean.ExpatriateTransRoleReq req = new SBean.ExpatriateTransRoleReq(tag,taskID, gs.getConfig().id, dstgsID, "", dbRole);
	        tec.sendPacket(new Packet.S2E.ExpatriateDBRole(req));
        }
	
	public void sendExchangeChannelMsgRes(int tag, int msgid, int gsid, List<String> data)
	{
		gs.getLogger().debug("send exchange channel msg res:[" + data + "]");
		tec.sendPacket(new Packet.S2E.ChannelMessageRes(new SBean.ExchangeChannelMessage(tag, msgid, gs.getConfig().id, gsid, data)));
	}
	
	public void exchangeSendDuelJoinReq(SBean.DuelJoinReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelJoin(req));
	}
	
	public void exchangeSendDuelCancelJoinReq(SBean.DuelCancelJoinReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelCancelJoin(req));
	}
	
	public void exchangeSendDuelSelectGeneralReq(SBean.DuelSelectGeneralReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelSelectGeneral(req));
	}
	
	public void exchangeSendDuelSelectGeneralForwardRes(SBean.DuelSelectGeneralRes res)
	{
		tec.sendPacket(new Packet.S2E.DuelSelectGeneralForward(res));
	}
	
	public void exchangeSendDuelStartAttackReq(SBean.DuelStartAttackReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelStartAttack(req));
	}
	
	public void exchangeSendDuelStartAttackForwardRes(SBean.DuelStartAttackRes res)
	{
		tec.sendPacket(new Packet.S2E.DuelStartAttackForward(res));
	}
	
	public void exchangeSendDuelFinishAttackReq(SBean.DuelFinishAttackReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelFinishAttack(req));
	}
	
	public void exchangeSendDuelOppoGiveupReq(SBean.DuelOppoGiveupReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelOppoGiveup(req));
	}
	
	public void exchangeSendDuelFinishAttackOnOppoDownLineReq(SBean.DuelFinishAttackOnOppoDownLineReq req)
    {
        tec.sendPacket(new Packet.S2E.DuelFinishAttackOnOppoDownLine(req));
    }
	
	public void exchangeSendDuelRankReq(SBean.DuelRankReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelRank(req));
	}
	
	public void exchangeSendSWarSearchReq(SBean.SWarSearchReq req)
	{
		tec.sendPacket(new Packet.S2E.SWarSearch(req));
	}
	
	public void exchangeSendSWarSearchForwardRes(SBean.SWarSearchRes res)
	{
		tec.sendPacket(new Packet.S2E.SWarSearchForward(res));
	}
	
	public void exchangeSendSWarAttackReq(SBean.SWarAttackReq req)
	{
		tec.sendPacket(new Packet.S2E.SWarAttack(req));
	}
	
	public void exchangeSendSWarAttackForwardRes(SBean.SWarAttackRes res)
	{
		tec.sendPacket(new Packet.S2E.SWarAttackForward(res));
	}
	
	public void exchangeSendSWarRankReq(SBean.SWarRankReq req)
	{
		tec.sendPacket(new Packet.S2E.SWarRank(req));
	}
	
	public void exchangeSendSWarTopRanksReq(SBean.SWarTopRanksReq req)
	{
		tec.sendPacket(new Packet.S2E.SWarTopRanks(req));
	}
	
	public void exchangeSendSWarRanksRes(SBean.SWarRanksRes res)
	{
		tec.sendPacket(new Packet.S2E.SWarRanks(res));
	}
	
	public void exchangeSendSWarVoteReq(SBean.SWarVoteReq req)
	{
		tec.sendPacket(new Packet.S2E.SWarVote(req));
	}
	
	public void exchangeSendExpiratBossRank(SBean.ExpiratBossRankReq req)
	{
		tec.sendPacket(new Packet.S2E.ExpiratBossRank(req));
	}
	
	public void exchangeSendHeroesBossSync(SBean.HeroesBossSyncReq req)
	{
		tec.sendPacket(new Packet.S2E.HeroesBossSync(req));
	}
	
	public void exchangeSendHeroesBossFinishAtt(SBean.HeroesBossFinishAttReq req)
	{
		tec.sendPacket(new Packet.S2E.HeroesBossFinishAtt(req));
	}
	
	public void exchangeSendHeroesBossInfo(SBean.HeroesBossInfoReq req)
	{
		tec.sendPacket(new Packet.S2E.HeroesBossInfo(req));
	}
	
	public void exchangeSendDuelTopRanksReq(SBean.DuelTopRanksReq req)
	{
		tec.sendPacket(new Packet.S2E.DuelTopRanks(req));
	}
	
	public void exchangeSendDuelRanksRes(SBean.DuelRanksRes res)
	{
		tec.sendPacket(new Packet.S2E.DuelRanks(res));
	}
	
	public void exchangeSendExpiratBossRanksRes(SBean.ExpiratBossRanksRes res)
	{
		tec.sendPacket(new Packet.S2E.ExpiratBossRanks(res));
	}
    
	public void exchangeSendExpatriateTransRoleResponse(byte errFlag, String openID, int srcgs, int resTag, int resId, String errDes)
        {
                SBean.ExpatriateTransRoleRes res = new ExpatriateTransRoleRes(resTag, resId, gs.getConfig().id, srcgs, errFlag, errDes);
                tec.sendPacket(new Packet.S2E.ExpatriateDBRoleForward(res));
        }
    
	//// begin handlers.
	public int getTCPGameServerMaxConnectionIdleTime()
	{
		// TODO
		return 900 * 1000;
	}

	public void onTCPGameServerOpen(TCPGameServer peer)
	{
		// TODO
	}

	public void onTCPGameServerOpenFailed(TCPGameServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPGameServerClose(TCPGameServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPGameServerSessionOpen(TCPGameServer peer, int sessionid, NetAddress addrClient)
	{
		gsNetStat.incTGSOpenCount();
		if( bDisconnectMode )
		{
			closeSession(sessionid);
			return;
		}
		if( sps.incrementAndGet() >= gs.getConfig().sps )
		{
			closeSession(sessionid);
			return;
		}
		gs.getLogger().debug("tcpserver: session open , sid = " + sessionid
				+ ", client addr is " + addrClient);
		//synchronized (this)
		{
			SessionInfo sinfo = new SessionInfo();
			sinfo.addrClient = addrClient;
			//sinfo.sid = sessionid;
			sessions.put(sessionid, sinfo);
		}
		List<Byte> keyRandList = new ArrayList<Byte>();
		if( gs.getConfig().challengeFlag == 1 )
		{
			byte[] keyRand = GameData.getInstance().secureRandBytes(16);
			for(byte e : keyRand)
			{
				keyRandList.add(e);
			}
			try
			{
				byte[] keyC = gs.getChallengeKey(keyRand, gs.getConfig().challengeFuncArg.getBytes("UTF-8"));
				peer.setInputSecurity(sessionid, new ARC4StreamSecurity(keyC));
			}
			catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tgs.sendPacket(sessionid, new Packet.S2C.ServerChallenge(gs.getConfig().getServerState(), "", gs.getConfig().challengeFlag, keyRandList));
	}

	public void onTCPGameServerSessionClose(TCPGameServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		gs.getLogger().debug("tcpserver: session close , sid = " + sessionid);
		gs.getLoginManager().startLogout(sessionid);
		//synchronized (this)
		{
			sessions.remove(sessionid);
		}
		gsNetStat.incTGSCloseCount();
	}

	public void onTCPGameServerRecvClientResponse(TCPGameServer peer, Packet.C2S.ClientResponse packet, int sessionid)
	{
		if( ! incSessionPackets(sessionid) )
			return;
		List<Byte> keyRandC = packet.getKey();
		if( keyRandC.isEmpty() || keyRandC.size() > 32 )
			return;
		byte[] keyRand = new byte[keyRandC.size()];
		for(int i = 0; i < keyRand.length; ++i)
		{
			keyRand[i] = keyRandC.get(i).byteValue();
		}
		try
		{
			byte[] keyS = gs.getChallengeKey(keyRand, gs.getConfig().challengeFuncArg.getBytes("UTF-8"));
			peer.setOutputSecurity(sessionid, new ARC4StreamSecurity(keyS));
			tgs.sendPacket(sessionid, new Packet.S2C.ServerResponse(0));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void onTCPGameServerRecvLuaChannel(TCPGameServer peer, Packet.C2S.LuaChannel packet, final int sessionid)
	{
		if( ! incSessionPackets(sessionid) )
			return;
		Role role = getLoginRole(sessionid);
		if (role == null)
			return;
		gs.getLogger().debug("role " + role.id + " send lua packet, data is " + packet.getData());
		try
		{
			String data = packet.getData();
			String[] msg = LuaPacket.decode(data);
			//
			// msg, role, return int 0:1:2
			int hret = gs.getHotChecker().checkRoleBeforeLuaPacket(gs, role, msg);
			if( hret != 0 )
			{
				switch( hret )
				{
				case 1:	// disable
					sendLuaPatchTip(sessionid);
					return;
				default:
					break;
				}
			}
			//
			onLuaChannelList(role, data, msg, sessionid);
		}
		catch(Exception ex)
		{			
			gs.getLogger().warn(ex.getMessage(), ex);
		}
	}

	public void onTCPGameServerRecvLuaChannel2(TCPGameServer peer, Packet.C2S.LuaChannel2 packet, int sessionid)
	{
		if( ! incSessionPackets(sessionid) )
			return;
		try
		{
			String[] msg = new String[packet.getData().size()];
			msg = packet.getData().toArray(msg);
			onLuaChannelList(null, null, msg, sessionid);
		}
		catch(Exception ex)
		{			
			gs.getLogger().warn(ex.getMessage(), ex);
		}
	}

	public void onTCPGameServerRecvDataSync(TCPGameServer peer, Packet.C2S.DataSync packet, int sessionid)
	{
		Role role = getLoginRole(sessionid);
		if (role == null)
			return;
		switch( packet.getDataType() )
		{
		case Packet.C2S.DataSync.eAutoUserID:
			{
				//gs.getLoginManager().genAutoUserID(sessionid);
			}
			break;
		default:
			break;
		}
	}

	public void onTCPGameServerRecvAntiData(TCPGameServer peer, Packet.C2S.AntiData packet, int sessionid)
	{
		if( ! incSessionPackets(sessionid) )
			return;
		Role role = getLoginRole(sessionid);
		if (role == null)
			return;
		gs.getTSSAntiManager().onRecvAntiData(role, packet.getData().anti_data);
	}

	public void onTCPControlServerOpen(TCPControlServer peer)
	{
		// TODO
	}

	public void onTCPControlServerOpenFailed(TCPControlServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPControlServerClose(TCPControlServer peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPControlServerSessionOpen(TCPControlServer peer, int sessionid, NetAddress addrClient)
	{
		// TODO
	}

	public void onTCPControlServerSessionClose(TCPControlServer peer, int sessionid, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPControlServerRecvShutdown(TCPControlServer peer, Packet.M2S.Shutdown packet, int sessionid)
	{
		// TODO
	}

	public void onTCPControlServerRecvAnnounce(TCPControlServer peer, Packet.M2S.Announce packet, int sessionid)
	{
		boolean bOK = gs.getLoginManager().announceGMMessage(packet.getMsg(), packet.getTimes());
		tcs.sendPacket(sessionid, new Packet.S2M.Announce(bOK?(byte)0:(byte)1));
	}

	public void onTCPControlServerRecvDataQuery(TCPControlServer peer, Packet.M2S.DataQuery packet, final int sessionid)
	{
		final SBean.DataQueryReq req = packet.getReq();
		switch( req.qtype )
		{
		case SBean.DataQueryReq.eOnlineUser:
			{
				int rcnt = gs.getLoginManager().getRoleCount();
				int scnt = gs.getLoginManager().getSessionCount();
				tcs.sendPacket(sessionid, new Packet.S2M.OnlineUser(rcnt, scnt));
			}
			break;
		case SBean.DataQueryReq.eRoleBrief:
			{
				
			}
			break;
		case SBean.DataQueryReq.eRoleID:
			{
				byte srcType = (byte)req.iArg1;
				switch( srcType )
				{
				case 1: // by roleName
					{
						String roleName = req.sArg1;
						gs.getLoginManager().getRoleIDByRoleName(roleName, new LoginManager.GetRoleIDByRoleNameCallback() {
							
							@Override
							public void onCallback(String roleName, Integer roleID)
							{
								if( roleID == null )
								{
									tcs.sendPacket(sessionid, new Packet.S2M.DataQuery(
											new SBean.DataQueryRes(SBean.DataQueryRes.eNotFound, req, 0, 0, "", "")));
								}
								else
								{
									tcs.sendPacket(sessionid, new Packet.S2M.DataQuery(
											new SBean.DataQueryRes(SBean.DataQueryRes.eOK, req, roleID.intValue(), 0, "", "")));
								}
							}
						});
					}
					break;
				case 2: // by userName
					{
						String userName = req.sArg1;
						gs.getLoginManager().getRoleIDByUserName(userName, new LoginManager.GetRoleIDByUserNameCallback() {
						
						@Override
						public void onCallback(String roleName, Integer roleID)
						{
							if( roleID == null )
							{
								tcs.sendPacket(sessionid, new Packet.S2M.DataQuery(
										new SBean.DataQueryRes(SBean.DataQueryRes.eNotFound, req, 0, 0, "", "")));
							}
							else
							{
								tcs.sendPacket(sessionid, new Packet.S2M.DataQuery(
										new SBean.DataQueryRes(SBean.DataQueryRes.eOK, req, roleID.intValue(), 0, "", "")));
							}
						}
					});
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

	public void onTCPControlServerRecvDataModify(TCPControlServer peer, Packet.M2S.DataModify packet, int sessionid)
	{
		String mailTitle = null; // TODO yxh
		String mailContent = null; // TODO yxh
		gs.getLoginManager().modifyData(sessionid, packet.getReq(), mailTitle, mailContent);
	}

	public void onTCPControlServerRecvSysMessage(TCPControlServer peer, Packet.M2S.SysMessage packet, final int sessionid)
	{
		SBean.SysMail m = packet.getMail();
		m.timeStart = gs.getTime();
		String content = gs.getLoginManager().checkUserInput(m.content, gs.getGameData().getUserInputCFG().sysMail, false, false);
		if( content == null )
		{
			notifySysMessage(sessionid, (byte)2);
			return;
		}
		String title = gs.getLoginManager().checkUserInput(m.title, gs.getGameData().getUserInputCFG().userName/*TODO*/, false, false);
		if( title == null )
		{
			notifySysMessage(sessionid, (byte)2);
			return;
		}
		List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
		boolean bOK = true;
		for(SBean.DropEntryNew e : m.att)
		{
			switch( e.type )
			{
			case GameData.COMMON_TYPE_ITEM:
				{
					short iid = e.id;
					if( GameData.getInstance().getItemCFG(iid) != null && e.arg > 0 )
					{
						attLst.add(e);
					}
					else
						bOK = false;
				}
				break;
			case GameData.COMMON_TYPE_EQUIP:
				{
					short eid = e.id;
					if( GameData.getInstance().getEquipCFG(eid) != null && e.arg > 0 )
					{
						attLst.add(e);
					}
					else
						bOK = false;
				}
				break;
			case GameData.COMMON_TYPE_GENERAL:
				{
					short gid = e.id;
					if( GameData.getInstance().getGeneralCFG(gid) != null && e.arg > 0 && e.arg <= GameData.MAX_GENERAL_EVO_LEVEL )
					{
						attLst.add(e);
					}
					else
						bOK = false;
				}
				break;
			case GameData.COMMON_TYPE_MONEY:
			case GameData.COMMON_TYPE_STONE:
			case GameData.COMMON_TYPE_ARENA_POINT:
			case GameData.COMMON_TYPE_MARCH_POINT:
			case GameData.COMMON_TYPE_FORCE_POINT:
				{
					if( e.arg > 0 )
					{
						attLst.add(e);
					}
					else
						bOK = false;
				}
				break;
			default:
				break;
			}
		}
		if( ! bOK )
		{
			notifySysMessage(sessionid, (byte)3);
			return;
		}
		// TODO
		if( m.id > 0 )
		{
			gs.getLoginManager().sysSendMessage(sessionid, m.id
				, DBMail.Message.SUB_TYPE_GM_MAIL, title, content
				, m.lifeTime, true, attLst);
		}
		else if( m.id == 0 ) // world mail
		{
			// TODO
			if( m.lifeTime <= 0 || m.lvlReq <= 0  || m.lvlReq > gs.getRoleLevelLimit() )
			{
				notifySysMessage(sessionid, (byte)4);
				return;
			}
			gs.getLoginManager().addWorldMail(m,
					new LoginManager.AddWorldMailCallback() {
						
						@Override
						public void onCallback(boolean bOK)
						{
							notifySysMessage(sessionid, bOK?0:(byte)6);
							return;
						}
					});
		}
	}

	public void onTCPControlServerRecvRoleData(TCPControlServer peer, Packet.M2S.RoleData packet, final int sessionid)
	{
		gs.getLoginManager().getRoleData(packet.getReq(), new LoginManager.GetRoleDataCallback() {
			
			@Override
			public void onCallback(RoleDataRes res)
			{
				if( res.brief == null )
				{
					res.brief = new SBean.RoleDataBrief(); res.brief.name = ""; res.brief.uname = ""; res.brief.fname = "";
				}
				if( res.generals == null )
					res.generals = new ArrayList<SBean.RoleDataGeneral>();
				if( res.equips == null )
					res.equips = new ArrayList<SBean.DBRoleEquip>();
				if( res.items == null )
					res.items = new ArrayList<SBean.DBRoleItem>();
				tcs.sendPacket(sessionid, new Packet.S2M.RoleData(res));
			}
		});
	}
//
	public void onTCPControlServerRecvWorldGift(final TCPControlServer peer, Packet.M2S.WorldGift packet, final int sessionid)
	{
		
	}

	public void onTCPExchangeClientOpen(TCPExchangeClient peer)
	{
		gs.getLogger().info("exchange server connected");
	}

	public void onTCPExchangeClientOpenFailed(TCPExchangeClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPExchangeClientClose(TCPExchangeClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPExchangeClientRecvStateAnnounce(TCPExchangeClient peer, Packet.E2S.StateAnnounce packet)
	{
		peer.sendPacket(new Packet.S2E.WhoAmI(gs.getConfig().platID, gs.getConfig().id));
	}

	public void onTCPExchangeClientRecvSearchCity(TCPExchangeClient peer, Packet.E2S.SearchCity packet)
	{
		gs.getExchangeManager().handleSearchCityResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvSearchCityForward(TCPExchangeClient peer, Packet.E2S.SearchCityForward packet)
	{
		gs.getExchangeManager().handleSearchCityRequest(packet.getReq());
	}

	public void onTCPExchangeClientRecvAttackCityStart(TCPExchangeClient peer, Packet.E2S.AttackCityStart packet)
	{
		gs.getExchangeManager().handleAttackCityStartResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvAttackCityStartForward(TCPExchangeClient peer, Packet.E2S.AttackCityStartForward packet)
	{
		gs.getExchangeManager().handleCityAttackedStartRequest(packet.getReq());
	}

	public void onTCPExchangeClientRecvAttackCityEnd(TCPExchangeClient peer, Packet.E2S.AttackCityEnd packet)
	{
		gs.getExchangeManager().handleAttackCityEndResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvAttackCityEndForward(TCPExchangeClient peer, Packet.E2S.AttackCityEndForward packet)
	{
		gs.getExchangeManager().handleCityAttackedEndRequest(packet.getReq());
	}

	public void onTCPExchangeClientRecvQueryCityOwner(TCPExchangeClient peer, Packet.E2S.QueryCityOwner packet)
	{
		gs.getExchangeManager().handleQueryCityOwnerResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvQueryCityOwnerForward(TCPExchangeClient peer, Packet.E2S.QueryCityOwnerForward packet)
	{
		gs.getExchangeManager().handleCityOwnerQueriedRequest(packet.getReq());
	}

	public void onTCPExchangeClientRecvNotifyCityOwnerChangedForward(TCPExchangeClient peer, Packet.E2S.NotifyCityOwnerChangedForward packet)
	{
		gs.getExchangeManager().handleNotifyCityOwnerChangedRequest(packet.getReq());
	}

	public void onTCPExchangeClientRecvChannelMessageReq(TCPExchangeClient peer, Packet.E2S.ChannelMessageReq packet)
	{
		onReceiveExchangeChannelMessageRequest(packet.getData());
	}

	public void onTCPExchangeClientRecvChannelMessageRes(TCPExchangeClient peer, Packet.E2S.ChannelMessageRes packet)
	{
		onReceiveExchangeChannelMessageResponse(packet.getData());
	}

	public void onTCPExchangeClientRecvDuelJoin(TCPExchangeClient peer, Packet.E2S.DuelJoin packet)
	{
		gs.getExchangeManager().handleDuelJoinResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelSelectGeneral(TCPExchangeClient peer, Packet.E2S.DuelSelectGeneral packet)
	{
		gs.getExchangeManager().handleDuelSelectGeneralResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelSelectGeneralForward(TCPExchangeClient peer, Packet.E2S.DuelSelectGeneralForward packet)
	{
		gs.getExchangeManager().handleDuelSelectGeneralForward(packet.getReq());
	}

	public void onTCPExchangeClientRecvDuelStartAttack(TCPExchangeClient peer, Packet.E2S.DuelStartAttack packet)
	{
		gs.getExchangeManager().handleDuelStartAttackResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelStartAttackForward(TCPExchangeClient peer, Packet.E2S.DuelStartAttackForward packet)
	{
		gs.getExchangeManager().handleDuelStartAttackForward(packet.getReq());
	}

	public void onTCPExchangeClientRecvDuelFinishAttack(TCPExchangeClient peer, Packet.E2S.DuelFinishAttack packet)
	{
		gs.getExchangeManager().handleDuelFinishAttackResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelRanks(TCPExchangeClient peer, Packet.E2S.DuelRanks packet)
	{
		gs.getExchangeManager().handleDuelRanksRequest();
	}

	public void onTCPExchangeClientRecvDuelRank(TCPExchangeClient peer, Packet.E2S.DuelRank packet)
	{
		gs.getExchangeManager().handleDuelRankResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelTopRanks(TCPExchangeClient peer, Packet.E2S.DuelTopRanks packet)
	{
		gs.getExchangeManager().handleDuelTopRanksResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelOppoGiveup(TCPExchangeClient peer, Packet.E2S.DuelOppoGiveup packet)
	{
		gs.getExchangeManager().handleDuelOppoGiveupResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelOppoGiveupForward(TCPExchangeClient peer, Packet.E2S.DuelOppoGiveupForward packet)
	{
		gs.getExchangeManager().handleDuelOppoGiveupForward(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelFinishAttackOnOppoDownLineForward(TCPExchangeClient peer, Packet.E2S.DuelFinishAttackOnOppoDownLineForward packet)
	{
		gs.getExchangeManager().handleDuelFinishAttackOnOppoDownLineForward(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelFinishAttackOnOppoDownLine(TCPExchangeClient peer, Packet.E2S.DuelFinishAttackOnOppoDownLine packet)
	{
	    gs.getExchangeManager().handleDuelFinishAttackOnOppoDownLineResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvDuelGlobalRanks(TCPExchangeClient peer, Packet.E2S.DuelGlobalRanks packet)
	{
		// TODO
	}

	public void onTCPExchangeClientRecvExpatriateDBRole(TCPExchangeClient peer, Packet.E2S.ExpatriateDBRole packet)
	{
	        gs.getExchangeManager().handleTransDBRoleResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvExpatriateDBRoleForward(TCPExchangeClient peer, Packet.E2S.ExpatriateDBRoleForward packet)
	{
	        gs.getExchangeManager().handleTransDBRole(packet.getReq());
	}

	public void onTCPExchangeClientRecvExpiratBossRanks(TCPExchangeClient peer, Packet.E2S.ExpiratBossRanks packet)
	{
		gs.getExchangeManager().handleExpiratBossRanksRequest();
	}

	public void onTCPExchangeClientRecvExpiratBossRank(TCPExchangeClient peer, Packet.E2S.ExpiratBossRank packet)
	{
		// TODO
	}

	public void onTCPExchangeClientRecvExpiratBossTopRanks(TCPExchangeClient peer, Packet.E2S.ExpiratBossTopRanks packet)
	{
		gs.getExpiratBossManager().handleTopRanksRequest(packet.getRes());
	}

	public void onTCPExchangeClientRecvExpiratBossGlobalRanks(TCPExchangeClient peer, Packet.E2S.ExpiratBossGlobalRanks packet)
	{
		gs.getExpiratBossManager().handleGlobalRanksRequest(packet.getRes());
	}

	public void onTCPExchangeClientRecvExpiratBossGlobalRanks2(TCPExchangeClient peer, Packet.E2S.ExpiratBossGlobalRanks2 packet)
	{
		gs.getExpiratBossManager().handleGlobalRanksRequest2(packet.getRes());
	}

	public void onTCPExchangeClientRecvHeroesBossSync(TCPExchangeClient peer, Packet.E2S.HeroesBossSync packet)
	{
		HeroesBossSyncRes res = (SBean.HeroesBossSyncRes) packet.getRes();
		if(res.roleID.serverID==gs.cfg.id)
		{   
			gs.getHeroesBossManager().handleEventTaskResult(packet.getRes().id, packet.getRes());
			return;
		}
				
		gs.getHeroesBossManager().execHeroesSync(packet.getRes());
	}

	public void onTCPExchangeClientRecvHeroesBossFinishAtt(TCPExchangeClient peer, Packet.E2S.HeroesBossFinishAtt packet)
	{
		HeroesBossFinishAttRes res = (SBean.HeroesBossFinishAttRes) packet.getRes();
		if(res.roleID.serverID==gs.cfg.id)
		{   
			gs.getHeroesBossManager().handleEventTaskResult(packet.getRes().id, packet.getRes());
			return;
		}
//		gs.getHeroesBossManager().handleEventTaskResult(packet.getRes().id, packet.getRes());
		gs.getHeroesBossManager().execFinishAtt(packet.getRes());
	}

	public void onTCPExchangeClientRecvHeroesBossInfo(TCPExchangeClient peer, Packet.E2S.HeroesBossInfo packet)
	{
		gs.getHeroesBossManager().handleHeroesBossInfo(packet.getRes());
	}

	public void onTCPExchangeClientRecvSWarSearch(TCPExchangeClient peer, Packet.E2S.SWarSearch packet)
	{
		gs.getExchangeManager().handleSWarSearchResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvSWarSearchForward(TCPExchangeClient peer, Packet.E2S.SWarSearchForward packet)
	{
		gs.getExchangeManager().handleSWarSearchForward(packet.getReq());
	}

	public void onTCPExchangeClientRecvSWarAttack(TCPExchangeClient peer, Packet.E2S.SWarAttack packet)
	{
		gs.getExchangeManager().handleSWarAttackResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvSWarAttackForward(TCPExchangeClient peer, Packet.E2S.SWarAttackForward packet)
	{
		gs.getExchangeManager().handleSWarAttackForward(packet.getReq());
	}

	public void onTCPExchangeClientRecvSWarRanks(TCPExchangeClient peer, Packet.E2S.SWarRanks packet)
	{
		gs.getExchangeManager().handleSWarRanksRequest();
	}

	public void onTCPExchangeClientRecvSWarRank(TCPExchangeClient peer, Packet.E2S.SWarRank packet)
	{
		gs.getExchangeManager().handleSWarRankResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvSWarTopRanks(TCPExchangeClient peer, Packet.E2S.SWarTopRanks packet)
	{
		gs.getExchangeManager().handleSWarTopRanksResult(packet.getRes());
	}

	public void onTCPExchangeClientRecvSWarGlobalRanks(TCPExchangeClient peer, Packet.E2S.SWarGlobalRanks packet)
	{
		// TODO
	}

	public void onTCPExchangeClientRecvSWarVoteForward(TCPExchangeClient peer, Packet.E2S.SWarVoteForward packet)
	{
		gs.getExchangeManager().handleSWarVoteForward(packet.getReq());
	}

	public void onTCPExchangeClientRecvSWarRelease(TCPExchangeClient peer, Packet.E2S.SWarRelease packet)
	{
		gs.getExchangeManager().handleSWarRelease(packet.getReq());
	}

	public void onTCPFriendClientOpen(TCPFriendClient peer)
	{
		gs.getLogger().info("friend server connected");
	}

	public void onTCPFriendClientOpenFailed(TCPFriendClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPFriendClientClose(TCPFriendClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPFriendClientRecvStateAnnounce(TCPFriendClient peer, Packet.F2S.StateAnnounce packet)
	{
		peer.sendPacket(new Packet.S2F.WhoAmI(gs.getConfig().platID, gs.getConfig().id));
	}

	public void onTCPFriendClientRecvSearchFriends(TCPFriendClient peer, Packet.F2S.SearchFriends packet)
	{
		gs.getExchangeManager().setSearchFriendsRes(packet.getRes());
	}

	public void onTCPFriendClientRecvQueryFriendProp(TCPFriendClient peer, Packet.F2S.QueryFriendProp packet)
	{
		gs.getLoginManager().queryFriendProp(packet.getReq());
	}

	public void onTCPFriendClientRecvSendMessageToFriend(TCPFriendClient peer, Packet.F2S.SendMessageToFriend packet)
	{
		gs.getLoginManager().recvMessageFromFriend(packet.getSrcID(), packet.getDstID(), packet.getMsgType(), packet.getArg1(), packet.getArg2());
	}

	public void onTCPFriendClientRecvCreateRole(TCPFriendClient peer, Packet.F2S.CreateRole packet)
	{
		gs.getExchangeManager().setCreateRoleRes(packet.getRoleID());
	}

//	public void onTCPExchangeClientRecvDuelCancel(TCPExchangeClient peer, Packet.E2S.DuelCancel packet)
	
////	public void onTCPExchangeClientRecvDuelCancel(TCPExchangeClient peer, Packet.E2S.DuelCancel packet)
////	{
////		// TODO
////	}
////
////	public void onTCPExchangeClientRecvDuelFinishAttackForward(TCPExchangeClient peer, Packet.E2S.DuelFinishAttackForward packet)
////	{
////		gs.getExchangeManager().handleDuelFinishAttackForward(packet.getReq());
////	}
////
////	public void onTCPExchangeClientRecvDuelCancelJoin(TCPExchangeClient peer, Packet.E2S.DuelCancelJoin packet)
////	{
////		// TODO
////	}
////
//	public void onTCPExchangeClientRecvExpatriateDBRole(TCPExchangeClient peer, Packet.E2S.ExpatriateDBRole packet)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeClientRecvDuelFinishAttackForward(TCPExchangeClient peer, Packet.E2S.DuelFinishAttackForward packet)
//		gs.getExchangeManager().handleDuelFinishAttackForward(packet.getReq());
//	}
//
//	public void onTCPExchangeClientRecvDuelCancelJoin(TCPExchangeClient peer, Packet.E2S.DuelCancelJoin packet)
//	{
//		// TODO
//	}
//
//	public void onTCPExchangeClientRecvSWarAttackResult(TCPExchangeClient peer, Packet.E2S.SWarAttackResult packet)
//	{
//		gs.getExchangeManager().handleSWarAttackResultResult(packet.getRes());
//	}
//
//	public void onTCPExchangeClientRecvSWarAttackResultForward(TCPExchangeClient peer, Packet.E2S.SWarAttackResultForward packet)
//	{
//		gs.getExchangeManager().handleSWarAttackResultForward(packet.getReq());
//	}
//
//	public void onTCPExchangeClientRecvSWarSelect(TCPExchangeClient peer, Packet.E2S.SWarSelect packet)
//	{
//		gs.getExchangeManager().handleSWarSelectResult(packet.getRes());
//	}
//
//	public void onTCPExchangeClientRecvSWarSelectForward(TCPExchangeClient peer, Packet.E2S.SWarSelectForward packet)
//	{
//		gs.getExchangeManager().handleSWarSelectForward(packet.getReq());
//	}
//
//	public int getTCPExchangeClientRecvBufferSize()
//	{
//		return 32768 * 4;
//	}
//
	//// end handlers.
	

	void onReceiveExchangeChannelMessageRequest(SBean.ExchangeChannelMessage req)
	{
		try
		{
			gs.getLogger().debug("recevice exchange channel msg req, data is " + req.msg);
			String[] msgs = new String[req.msg.size()];
			msgs = req.msg.toArray(msgs);
			handleExchangeChannelMessageRequest(req.tag, req.id, req.srcgs, msgs);
		}
		catch(Exception ex)
		{			
			gs.getLogger().warn(ex.getMessage(), ex);
		}
		
	}
	
	void onReceiveExchangeChannelMessageResponse(SBean.ExchangeChannelMessage res)
	{
		gs.getLogger().debug("recevice exchange channel msg res, data is " + res.msg);
		gs.getExchangeManager().getExchangeService().onReceiveExhcnageResponse(res);
	}
	
	void handleExchangeChannelMessageRequest(final int tag, final int mid, final int srcgs, String[] msg)
	{
		int msgIndex = 0;
		switch (msg[msgIndex++])
		{
		case "invitation":
			{
				switch(msg[msgIndex++])
				{
					case "setcode":
						{
							int targetrid = Integer.parseInt(msg[msgIndex++]);
							int gsid = Integer.parseInt(msg[msgIndex++]);
							int roleid = Integer.parseInt(msg[msgIndex++]);
							String rolename = msg[msgIndex++];
							int level = Integer.parseInt(msg[msgIndex++]);
							int vip = Integer.parseInt(msg[msgIndex++]);
							short headicon = Short.parseShort(msg[msgIndex++]);
							int syncTime = Integer.parseInt(msg[msgIndex++]);
							int lastLogin = Integer.parseInt(msg[msgIndex++]);
							gs.getLoginManager().setInvitationFriend(targetrid, gsid, roleid, rolename, level, vip, headicon, syncTime, lastLogin, new LoginManager.SetInvitationFriendCallback()
									{
										public void onCallback(int errorCode)
										{
											List<String> res = new ArrayList<String>();
											res.add(String.valueOf(errorCode));
											sendExchangeChannelMsgRes(tag, mid, srcgs, res);
										}
									});
						}
						break;
					case "update":
						{
							int targetrid = Integer.parseInt(msg[msgIndex++]);
							gs.getLoginManager().updateInvitationFriendInfo(targetrid, new LoginManager.UpdateInvitationFriendInfoCallback() 
								{
									@Override
									public void onCallback(DBInvitationFriend info) 
									{
										List<String> res = new ArrayList<String>();
										if (info == null)
										{
											res.add(String.valueOf(-3));
										}
										else
										{
											res.add(String.valueOf(0));
											res.add(String.valueOf(info.gsid));
											res.add(String.valueOf(info.roleid));
											res.add(String.valueOf(info.rolename));
											res.add(String.valueOf(info.level));
											res.add(String.valueOf(info.vip));
											res.add(String.valueOf(info.icon));
											res.add(String.valueOf(info.lastSyncTime));
											res.add(String.valueOf(info.lastLoginTime));
											sendExchangeChannelMsgRes(tag, mid, srcgs, res);
										}
									}
								});
						}
						break;
					default:
						break;
				}
			}
			break;
            case "expatriate":
		        {
                        switch (msg[1]) {
                                case "queryrole":
                                {
                                        String vopenID = msg[3];
                                        gs.getLoginManager().queryRoleExistsByOtherGS(vopenID, new LoginManager.QueryRoleExistsByOtherGSCallBack(){
                                                @Override
                                                public void onCallBack(String openID, Integer roleID)
                                                {
                                                       List<String> res = new ArrayList<String>();
                                                       res.add("expatriate");
                                                       res.add("queryRoleRes");
                                                       res.add(openID);
                                                       if (roleID==null || roleID.intValue()<0)
                                                       {
                                                               res.add(String.valueOf(0));
                                                       }else{
                                                               res.add(String.valueOf(roleID.intValue()));
                                                       }
                                                       sendExchangeChannelMsgRes(tag, mid, srcgs, res);
                                                }
                                        });
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
	
	public void sendInvitationSetFriendRequest(int dstgs, int dstrid, 
			                                   int gsid, int roleid, String rolename, int level, int vip, short headicon, int syncTime, int lastLogin,
			                                   final LoginManager.SetInvitationFriendCallback callback)
	{
		List<String> msg = new ArrayList<String>();
		msg.add("invitation");
		msg.add("setcode");
		msg.add(String.valueOf(dstrid));
		msg.add(String.valueOf(gsid));
		msg.add(String.valueOf(roleid));
		msg.add(String.valueOf(rolename));
		msg.add(String.valueOf(level));
		msg.add(String.valueOf(vip));
		msg.add(String.valueOf(headicon));
		msg.add(String.valueOf(syncTime));
		msg.add(String.valueOf(lastLogin));
		gs.getExchangeManager().getExchangeService().sendExchangeRequest(dstgs, msg, new ExchangeManager.ExchangeCallback() 
			{
				
				@Override
				public void onCallback(int errorCode, String[] resdata) 
				{
					if (errorCode == ExchangeManager.REQUEST_RESPONSE_SUCCESS)
					{
						if (resdata == null)
						{
							errorCode = ExchangeManager.REQUEST_RESPONSE_UNKNOWN_ERROR;
						}
						else
						{
							errorCode = Integer.parseInt(resdata[0]);
						}
					}
					callback.onCallback(errorCode);
				}
			});
	}
	
	public void notifyInvitationSetFriendResult(int sid, int errorCode)
	{
		gs.getLogger().debug("notifyInvitationSetFriendResult ==> errorCode=" + errorCode);
		sendLuaPacket(sid, LuaPacket.encodeInvitationInputFriendCode(errorCode));
	}
	
	public void sendInvitationFriendUpdateInfoRequest(int dstgs, int dstrid, final LoginManager.UpdateInvitationFriendInfoCallback callback)
	{
		List<String> msg = new ArrayList<String>();
		msg.add("invitation");
		msg.add("update");
		msg.add(String.valueOf(dstrid));
		gs.getExchangeManager().getExchangeService().sendExchangeRequest(dstgs, msg, new ExchangeManager.ExchangeCallback() 
			{
				
				@Override
				public void onCallback(int errorCode, String[] resdata) 
				{
					SBean.DBInvitationFriend info = null;
					if (errorCode == ExchangeManager.REQUEST_RESPONSE_SUCCESS)
					{
						if (resdata == null)
						{
							errorCode = ExchangeManager.REQUEST_RESPONSE_UNKNOWN_ERROR;
						}
						else
						{
							int msgIndex = 0;
							errorCode = Integer.parseInt(resdata[msgIndex++]);
							if (errorCode == 0)
							{
								info = new SBean.DBInvitationFriend();
								info.gsid = Integer.parseInt(resdata[msgIndex++]);
								info.roleid = Integer.parseInt(resdata[msgIndex++]);
								info.rolename = resdata[msgIndex++];
								info.level = Integer.parseInt(resdata[msgIndex++]);
								info.vip = Integer.parseInt(resdata[msgIndex++]);
								info.icon = Short.parseShort(resdata[msgIndex++]);
								info.lastSyncTime = Integer.parseInt(resdata[msgIndex++]);
								info.lastLoginTime = Integer.parseInt(resdata[msgIndex++]);
							}
						}
					}
					callback.onCallback(info);
				}
			});
	}
	
	public void onIDIPReq(final int sessionid, final IDIPPacket packet)
	{
		// TODO
		IDIP.IdipHeader headerReq = packet.header;
		gs.getLogger().info("idip req, sessionid=" + sessionid + ", type=0x" + Integer.toHexString(headerReq.Cmdid) + ", size = " + headerReq.PacketLen);
		Stream.IStreamable reqstream = IDIP.decodePacket(headerReq.Cmdid, packet.body);
		if (reqstream == null)
		{
			gs.getLogger().warn("idip req, sessionid=" + sessionid + ", type=0x" + Integer.toHexString(headerReq.Cmdid) + ", size = " + headerReq.PacketLen + ", decode failed");
			return;
		}
		switch( headerReq.Cmdid )
		{
			case IDIP.IDIP_DO_BAN_USR_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoBanUsrReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UNBAN_USR_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUnbanUsrReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_DEL_ITEM_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoDelItemReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_EXP_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdateExpReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_PHYSICAL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdatePhysicalReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_DIAMOND_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdateDiamondReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_MONEY_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdateMoneyReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SEND_ITEM_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSendItemReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_USR_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryUsrInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_GENERAL_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryGeneralInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_GENERAL_EQUIP_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryGeneralEquipInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_BAG_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryBagInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SEND_MAIL_ALL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSendMailAllReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SEND_ROLLNOTICE_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSendRollnoticeReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_NOTICE_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryNoticeReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_DEL_NOTICE_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoDelNoticeReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SEND_MAIL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSendMailReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_ARENA_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryArenaReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_CLEAR_DATA_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoClearDataReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SET_GENERAL_LV_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSetGeneralLvReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_SET_PET_LV_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoSetPetLvReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_BAGITEM_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryBagitemInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_VIP_EXP_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdateVipExpReq)reqstream);
				}
				break;
			case IDIP.IDIP_DO_UPDATE_KING_LEVEL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.DoUpdateKingLevelReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_FIGHT_INFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryFightInfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_VIP_LEVEL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryVipLevelReq)reqstream);
				}
				break;
			case IDIP.IDIP_QUERY_USER_SIGNIN_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.QueryUserSigninReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_QUERY_USERBASEINFO_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqQueryUserbaseinfoReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_SEND_MAIL_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoSendMailReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_UPDATE_MONEY_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoUpdateMoneyReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_BAN_PLAY_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoBanPlayReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_BAN_USR_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoBanUsrReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_RELIEVE_PUNISH_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoRelievePunishReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_UPDATE_DIAMOND_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoUpdateDiamondReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqQueryUsrsignGuildnoticeReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoSetUsrsignGuildnoticeReq)reqstream);
				}
				break;
			case IDIP.IDIP_AQ_DO_MASKCHAT_USR_REQ:
				{
					onHandleIDIPReq(sessionid, headerReq, (IDIP.AqDoMaskchatUsrReq)reqstream);
				}
				break;
			default:
				gs.getLogger().warn("idip req, type=" + Integer.toHexString(headerReq.Cmdid) + ", size = " + headerReq.PacketLen + ", can't find handler!");
				break;
		}
	}
	
	private int getIDIPResult(int errcode)
	{
		int result = IDIP.IDIP_HEADER_RESULT_OTHER_ERROR;
		switch (errcode)
		{
		case CommonRoleVisitor.ERR_NULL:
			result = IDIP.IDIP_HEADER_RESULT_SERVER_BUSY;
			break;
		case CommonRoleVisitor.ERR_FAILED:
			result = IDIP.IDIP_HEADER_RESULT_OTHER_ERROR;
			break;
		case CommonRoleVisitor.ERR_OK:
			result = IDIP.IDIP_HEADER_RESULT_SUCCESS;
			break;
		}
		return result;
	}
	
	private String getIDIPErrMsg(int errcode)
	{
		String msg = "not known error";
		switch (errcode)
		{
		case CommonRoleVisitor.ERR_NULL:
			msg = "Database busy";
			break;
		case CommonRoleVisitor.ERR_FAILED:
			msg = "failed";
			break;
		case CommonRoleVisitor.ERR_OK:
			msg = "success";
			break;
		}
		return msg;
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoBanUsrReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoBanUsrReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoBanUsrReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
				{
					public void onCallback(String openID, Integer roleID)
					{
						if (roleID == null)
						{
							String errMsg = "can't find role";
							IDIP.DoBanUsrRsp res = new IDIP.DoBanUsrRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
							
							IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
							headerRes.Cmdid = IDIP.IDIP_DO_BAN_USR_RSP;
							headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
							headerRes.RetErrMsg = errMsg;
							idips.sendRes(sessionid, headerRes, res);
							gs.getLogger().info("idip sessionid=" + sessionid + ", DoBanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
							return;
						}
						{
							int roleId = roleID.intValue();
							int banSecond = req.BanTime == 0 ? -1 : req.BanTime;
							gs.getLoginManager().banRole(roleId, banSecond,  req.Reason, new LoginManager.ModifyRoleDataCallback()
								{
									
									@Override
									public void onCallback(final int errcode) {
										IDIP.DoBanUsrRsp res = new IDIP.DoBanUsrRsp(errcode,  getIDIPErrMsg(errcode));
										
										IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
										headerRes.Cmdid = IDIP.IDIP_DO_BAN_USR_RSP;
										headerRes.Result = getIDIPResult(errcode);
										headerRes.RetErrMsg = getIDIPErrMsg(errcode);
										idips.sendRes(sessionid, headerRes, res);
										gs.getLogger().info("idip sessionid=" + sessionid + ", DoBanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
									}
								});
						}
					}
				});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUnbanUsrReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUnbanUsrReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUnbanUsrReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUnbanUsrRsp res = new IDIP.DoUnbanUsrRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UNBAN_USR_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUnbanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().unbanRole(roleId, 
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUnbanUsrRsp res = new IDIP.DoUnbanUsrRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UNBAN_USR_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUnbanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoDelItemReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelItemReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelItemReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoDelItemRsp res = new IDIP.DoDelItemRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_DEL_ITEM_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				if (GameData.getInstance().getItemCFG((short)(req.ItemId)) == null)
				{
					String errMsg = "item id invalid";
					IDIP.DoDelItemRsp res = new IDIP.DoDelItemRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_DEL_ITEM_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().delRoleItem(roleId, (int)req.ItemId, req.ItemNum,
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoDelItemRsp res = new IDIP.DoDelItemRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_DEL_ITEM_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdateExpReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateExpReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateExpReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdateExpRsp res = new IDIP.DoUpdateExpRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_EXP_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateExpRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleExp(roleId,  req.Value,
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUpdateExpRsp res = new IDIP.DoUpdateExpRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_EXP_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateExpRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdatePhysicalReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdatePhysicalReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdatePhysicalReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdatePhysicalRsp res = new IDIP.DoUpdatePhysicalRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_PHYSICAL_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdatePhysicalRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleVit(roleId,  req.Value,
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUpdatePhysicalRsp res = new IDIP.DoUpdatePhysicalRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_PHYSICAL_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdatePhysicalRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdateDiamondReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateDiamondReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateDiamondReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdateDiamondRsp res = new IDIP.DoUpdateDiamondRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_DIAMOND_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateDiamondRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleStone(roleId,  req.Value, false, new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUpdateDiamondRsp res = new IDIP.DoUpdateDiamondRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_DIAMOND_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
									gs.getTLogger().logIDIPCMD(req.OpenId, GameData.COMMON_TYPE_STONE, 0, req.Value, req.Serial, req.Source, "10102005");
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateDiamondRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdateMoneyReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateMoneyReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateMoneyReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdateMoneyRsp res = new IDIP.DoUpdateMoneyRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_MONEY_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateMoneyRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleMoney(roleId,  req.Value,
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUpdateMoneyRsp res = new IDIP.DoUpdateMoneyRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_MONEY_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
									gs.getTLogger().logIDIPCMD(req.OpenId, GameData.COMMON_TYPE_MONEY, 0, req.Value, req.Serial, req.Source, "10102006");
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateMoneyRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSendItemReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendItemReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendItemReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoSendItemRsp res = new IDIP.DoSendItemRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_SEND_ITEM_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				if (GameData.getInstance().getItemCFG((short)(req.ItemId)) == null)
				{
					String errMsg = "item id invalid";
					IDIP.DoSendItemRsp res = new IDIP.DoSendItemRsp(-1,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_SEND_ITEM_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().addRoleItem(roleId, (int)req.ItemId, req.ItemNum,
							new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoSendItemRsp res = new IDIP.DoSendItemRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_SEND_ITEM_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
									gs.getTLogger().logIDIPCMD(req.OpenId, GameData.COMMON_TYPE_ITEM, (int)req.ItemId, req.ItemNum, req.Serial, req.Source, "10102007");
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendItemRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryUsrInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUsrInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUsrInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryUsrInfoRsp res = new IDIP.QueryUsrInfoRsp();
					res.RoleName = "";
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_USR_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUsrInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryRoleInfo(roleId,
							new LoginManager.QueryRoleInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final LoginManager.SRoleInfo roleinfo) 
							{
								IDIP.QueryUsrInfoRsp res = new IDIP.QueryUsrInfoRsp();
								res.Level = roleinfo.lvl;
								res.Exp = roleinfo.exp;
								res.Money = roleinfo.money;
								res.Diamond = roleinfo.stone;
								res.Physical = roleinfo.vit;
								res.MaxPhysical = roleinfo.maxVit;
								res.ItemTypeNum = roleinfo.itemTypeNum;
								res.RegisterTime = roleinfo.registerTime;
								res.IsOnline = (byte)roleinfo.isOnLine;
								res.Status = (byte)roleinfo.banStatus;
								res.BanEndTime = roleinfo.banEndTime;
								res.NormalBigPassProgress = roleinfo.normalBigPassProgress;
								res.NormalSmallPassProgress = roleinfo.normalSmallPassProgress;
								res.HeroBigPassProgress = roleinfo.heroBigPassProgress;
								res.HeroSmallPassProgress = roleinfo.heroSmallPassProgress;
								res.RoleName = roleinfo.roleName;
								res.LastLoginTime = roleinfo.lastLoginTime;
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_USR_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUsrInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryGeneralInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryGeneralInfoRsp res = new IDIP.QueryGeneralInfoRsp(0, new ArrayList<SGeneralInfo>());
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_GENERAL_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryGeneralInfo(roleId, req.PageNo,
							new LoginManager.QueryGeneralInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final List<IDIP.SGeneralInfo> ginfo) {
								IDIP.QueryGeneralInfoRsp res = new IDIP.QueryGeneralInfoRsp();
								res.GeneralList_count = ginfo.size();
								res.GeneralList = ginfo;
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_GENERAL_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryGeneralEquipInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralEquipInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralEquipInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryGeneralEquipInfoRsp res = new IDIP.QueryGeneralEquipInfoRsp(0,  new ArrayList<SGeneralEquipInfo>());
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_GENERAL_EQUIP_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralEquipInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryGeneralEquipInfo(roleId, req.PageNo, new LoginManager.QueryGeneralEquipInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final List<IDIP.SGeneralEquipInfo> geinfo) {
								IDIP.QueryGeneralEquipInfoRsp res = new IDIP.QueryGeneralEquipInfoRsp();
								res.GeneralEquipList_count = geinfo.size();
								res.GeneralEquipList = geinfo;
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_GENERAL_EQUIP_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryGeneralEquipInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryBagInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryBagInfoRsp res = new IDIP.QueryBagInfoRsp(0,  new ArrayList<SBagInfo>());
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_BAG_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryItemInfo(roleId, req.PageNo,
							new LoginManager.QueryItemInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final List<IDIP.SBagInfo> binfo) {
								IDIP.QueryBagInfoRsp res = new IDIP.QueryBagInfoRsp();
								res.BagList_count = binfo.size();
								res.BagList = binfo;
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_BAG_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg
										+ ", BagList_count=" + res.BagList_count + ", BagList size =" + res.BagList.size()
										+ " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSendMailAllReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailAllReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition + ", title=" + req.MailTitle);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailAllReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		{
			List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
			String errMsg1 = LoginManager.checkMailAttachment(req.ItemType1, req.ItemId1, req.ItemNum1);
			String errMsg2 = LoginManager.checkMailAttachment(req.ItemType2, req.ItemId2, req.ItemNum2);
			String errMsg3 = LoginManager.checkMailAttachment(req.ItemType3, req.ItemId3, req.ItemNum3);
			String errMsg4 = LoginManager.checkMailAttachment(req.ItemType4, req.ItemId4, req.ItemNum4);
			String errMsg = !errMsg1.isEmpty() ? errMsg1 : (!errMsg2.isEmpty() ? errMsg2 : (!errMsg3.isEmpty() ? errMsg3 : (!errMsg4.isEmpty() ? errMsg4 : "")));
			if (errMsg.isEmpty())
			{
				if (req.ItemNum1 > 0)
					attLst.add(new SBean.DropEntryNew((byte)req.ItemType1,(short)req.ItemId1, req.ItemNum1));
				if (req.ItemNum2 > 0)
					attLst.add(new SBean.DropEntryNew((byte)req.ItemType2,(short)req.ItemId2, req.ItemNum2));
				if (req.ItemNum3 > 0)
					attLst.add(new SBean.DropEntryNew((byte)req.ItemType3,(short)req.ItemId3, req.ItemNum3));
				if (req.ItemNum4 > 0)
					attLst.add(new SBean.DropEntryNew((byte)req.ItemType4,(short)req.ItemId4, req.ItemNum4));
				if ((req.Type != 1 && req.Type != 2) || (req.Type != 1 && !attLst.isEmpty()))
				{
					errMsg = "mail type invalid";
				}
				else if (req.BeginTime <= 0 || req.EndTime <= 0 || req.EndTime < req.BeginTime)
				{
					errMsg = "mail valid time error";
				}
				else if (req.MailTitle.length() > IDIP.MAX_MAILTITLE_LEN)
				{
					errMsg = "mail title length too long";
				}
				else if (req.MailContent.length() > IDIP.MAX_MAILCONTENT_LEN)
				{
					errMsg = "mail content length too long";
				}
				else if (req.MailTitle.isEmpty())
				{
					errMsg = "mail title null";
				}
				else if (req.MailContent.isEmpty())
				{
					errMsg = "mail content null";
				}
			}
			if (!errMsg.isEmpty())
			{
				IDIP.DoSendMailAllRsp res = new IDIP.DoSendMailAllRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
				
				IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
				headerRes.Cmdid = IDIP.IDIP_DO_SEND_MAIL_ALL_RSP;
				headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
				headerRes.RetErrMsg = errMsg;
				idips.sendRes(sessionid, headerRes, res);
				gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailAllRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
				return;
			}
			SBean.SysMail mail = new SBean.SysMail();
			mail.id = 0;
			mail.lvlReq = (short)req.MinLevel;
			mail.vipReq = (short)req.VipLevel;
			mail.timeStart = (int)(req.BeginTime+8*3600);
			mail.lifeTime = (int)(req.EndTime-req.BeginTime);
			mail.flag = req.Type;
			mail.title = req.MailTitle;
			mail.content = req.MailContent;
			mail.att = attLst;
			gs.getLoginManager().addDelayMail(mail, new LoginManager.AddDelayMailCallback() 
					{
						@Override
						public void onCallback(final int errcode) 
						{
							String errMsg = errcode == 0 ? "success" : "Datebase exception";
							IDIP.DoSendMailAllRsp res = new IDIP.DoSendMailAllRsp(errcode,  errMsg);
							
							IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
							headerRes.Cmdid = IDIP.IDIP_DO_SEND_MAIL_ALL_RSP;
							headerRes.Result = errcode == 0 ? IDIP.IDIP_HEADER_RESULT_SUCCESS : IDIP.IDIP_HEADER_RESULT_BD_EXCEPTION;
							headerRes.RetErrMsg = errMsg;
							idips.sendRes(sessionid, headerRes, res);
							gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailAllRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
						}
					});
		}
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSendRollnoticeReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendRollnoticeReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition + ", msg=" + req.NoticeContent);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendRollnoticeReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		{
			String errMsg = "";
			if (req.Times <= 0)
			{
				errMsg = "roll times invalid";
			}
			else if (req.BeginTime <= 0 || req.EndTime <= 0 || req.EndTime <= req.BeginTime || req.EndTime+8*3600 < gs.getTime())
			{
				errMsg = "roll time period invalid";
			}
			else if (req.NoticeContent.isEmpty())
			{
				errMsg = "notice content null";
			}
			if (!errMsg.isEmpty())
			{
				IDIP.DoSendRollnoticeRsp res = new IDIP.DoSendRollnoticeRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
				
				IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
				headerRes.Cmdid = IDIP.IDIP_DO_SEND_ROLLNOTICE_RSP;
				headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
				headerRes.RetErrMsg = errMsg;
				idips.sendRes(sessionid, headerRes, res);
				gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendRollnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
				return;
			}
			SBean.Broadcast broadcast = new SBean.Broadcast();
			broadcast.id = 0;
			broadcast.sendTime = gs.getTime();
			broadcast.timeStart = (int)(req.BeginTime+8*3600);
			broadcast.lifeTime = (int)(req.EndTime-req.BeginTime);
			broadcast.freq = req.Freq;
			broadcast.times = req.Times;
			broadcast.content = req.NoticeContent;
			gs.getLoginManager().addBroadcast(broadcast, new LoginManager.AddBroadcastCallback()
					{
						@Override
						public void onCallback(final int errcode) 
						{
							String errMsg = errcode == 0 ? "success" : "Datebase exception";
							IDIP.DoSendRollnoticeRsp res = new IDIP.DoSendRollnoticeRsp(errcode,  errMsg);
							
							IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
							headerRes.Cmdid = IDIP.IDIP_DO_SEND_ROLLNOTICE_RSP;
							headerRes.Result = errcode == 0 ? IDIP.IDIP_HEADER_RESULT_SUCCESS : IDIP.IDIP_HEADER_RESULT_BD_EXCEPTION;
							headerRes.RetErrMsg = errMsg;
							idips.sendRes(sessionid, headerRes, res);
							gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendRollnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
						}
					});
		}
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryNoticeReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryNoticeReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryNoticeReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		{
			String errMsg = "";
			if (req.BeginTime <= 0 || req.EndTime <= 0 || req.BeginTime >= req.EndTime || req.EndTime+8*3600 < gs.getTime())
			{
				errMsg = "time period invalid";
			}
			if (!errMsg.isEmpty())
			{
				IDIP.QueryNoticeRsp res = new IDIP.QueryNoticeRsp(0,  new ArrayList<IDIP.SNoticeInfo>());
				
				IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
				headerRes.Cmdid = IDIP.IDIP_QUERY_NOTICE_RSP;
				headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
				headerRes.RetErrMsg = errMsg;
				idips.sendRes(sessionid, headerRes, res);
				gs.getLogger().info("idip sessionid=" + sessionid + ", QueryNoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg
						+ ", NoticeList_count=" + res.NoticeList_count);
				return;
			}
			List<SBean.Broadcast> broadcasts = gs.getLoginManager().queryBroadcasts((int)(req.BeginTime+8*3600), (int)(req.EndTime+8*3600));
			
			List<IDIP.SNoticeInfo> ninfo = new ArrayList<IDIP.SNoticeInfo>();
			for (SBean.Broadcast e : broadcasts)
			{
				ninfo.add(new IDIP.SNoticeInfo(e.sendTime-8*3600, e.timeStart-8*3600, e.timeStart+e.lifeTime-8*3600, e.id, e.content));
			}
			IDIP.QueryNoticeRsp res = new IDIP.QueryNoticeRsp(ninfo.size(),  ninfo);
			
			IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
			headerRes.Cmdid = IDIP.IDIP_QUERY_NOTICE_RSP;
			headerRes.Result = IDIP.IDIP_HEADER_RESULT_SUCCESS;
			headerRes.RetErrMsg = "success";
			idips.sendRes(sessionid, headerRes, res);
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryNoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg
					+ ", NoticeList_count=" + res.NoticeList_count);
		}
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoDelNoticeReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelNoticeReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelNoticeReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		{
			gs.getLoginManager().delBroadcast((int)req.NoticeId, new LoginManager.DelBroadcastCallback()
					{
						@Override
						public void onCallback(final int errcode) 
						{
							String errMsg = errcode == 0 ? "success" : "Datebase exception";
							IDIP.DoDelNoticeRsp res = new IDIP.DoDelNoticeRsp(errcode,  errMsg);
							
							IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
							headerRes.Cmdid = IDIP.IDIP_DO_DEL_NOTICE_RSP;
							headerRes.Result = errcode == 0 ? IDIP.IDIP_HEADER_RESULT_SUCCESS : IDIP.IDIP_HEADER_RESULT_BD_EXCEPTION;
							headerRes.RetErrMsg = errMsg;
							idips.sendRes(sessionid, headerRes, res);
							gs.getLogger().info("idip sessionid=" + sessionid + ", DoDelNoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
						}
					});
		}
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSendMailReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition + ", title=" + req.MailTitle);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoSendMailRsp res = new IDIP.DoSendMailRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_SEND_MAIL_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					int lifetime = (int)(req.EndTime - gs.getTimeGMT());
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					String errMsg1 = LoginManager.checkMailAttachment(req.ItemType1, req.ItemId1, req.ItemNum1);
					String errMsg2 = LoginManager.checkMailAttachment(req.ItemType2, req.ItemId2, req.ItemNum2);
					String errMsg3 = LoginManager.checkMailAttachment(req.ItemType3, req.ItemId3, req.ItemNum3);
					String errMsg4 = LoginManager.checkMailAttachment(req.ItemType4, req.ItemId4, req.ItemNum4);
					String errMsg = !errMsg1.isEmpty() ? errMsg1 : (!errMsg2.isEmpty() ? errMsg2 : (!errMsg3.isEmpty() ? errMsg3 : (!errMsg4.isEmpty() ? errMsg4 : "")));
					if (errMsg.isEmpty())
					{
						if (req.ItemNum1 > 0)
							attLst.add(new SBean.DropEntryNew((byte)req.ItemType1,(short)req.ItemId1, req.ItemNum1));
						if (req.ItemNum2 > 0)
							attLst.add(new SBean.DropEntryNew((byte)req.ItemType2,(short)req.ItemId2, req.ItemNum2));
						if (req.ItemNum3 > 0)
							attLst.add(new SBean.DropEntryNew((byte)req.ItemType3,(short)req.ItemId3, req.ItemNum3));
						if (req.ItemNum4 > 0)
							attLst.add(new SBean.DropEntryNew((byte)req.ItemType4,(short)req.ItemId4, req.ItemNum4));
						if ((req.Type != 1 && req.Type != 2) || (req.Type != 1 && !attLst.isEmpty()))
						{
							errMsg = "mail type invalid";
						}
						else if (lifetime < 0)
						{
							errMsg = "mail life time error";
						}
						else if (req.MailTitle.length() > IDIP.MAX_MAILTITLE_LEN)
						{
							errMsg = "mail title length too long";
						}
						else if (req.MailContent.length() > IDIP.MAX_MAILCONTENT_LEN)
						{
							errMsg = "mail content length too long";
						}
						else if (req.MailTitle.isEmpty())
						{
							errMsg = "mail title null";
						}
						else if (req.MailContent.isEmpty())
						{
							errMsg = "mail content null";
						}
					}
					if (!errMsg.isEmpty())
					{
						IDIP.DoSendMailRsp res = new IDIP.DoSendMailRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_DO_SEND_MAIL_RSP;
						headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
						headerRes.RetErrMsg = errMsg;
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
						return;
					}
					if (req.Type == 1)
						lifetime = 0;
					final List<SBean.DropEntryNew> attLstCopy = attLst;
					gs.getLoginManager().sendRoleMail(roleId, req.MailTitle, req.MailContent, lifetime, attLst,
							new LoginManager.SendRoleMailCallback() 
							{
								
								@Override
								public void onCallback(final int errcode) {
									IDIP.DoSendMailRsp res = new IDIP.DoSendMailRsp(errcode,  getIDIPErrMsg(errcode));
									
									IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
									headerRes.Cmdid = IDIP.IDIP_DO_SEND_MAIL_RSP;
									headerRes.Result = getIDIPResult(errcode);
									headerRes.RetErrMsg = getIDIPErrMsg(errcode);
									idips.sendRes(sessionid, headerRes, res);
									if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
									{
										for (SBean.DropEntryNew e : attLstCopy)
										{
											gs.getTLogger().logIDIPCMD(req.OpenId, e.type, e.id, e.arg, req.Serial, req.Source, "10102016");
										}
									}
									gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
								}
							});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryArenaReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryArenaReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryArenaReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryArenaRsp res = new IDIP.QueryArenaRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  -1);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_ARENA_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryArenaRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					int rank = gs.getLoginManager().getRoleArenaRank(roleId);
					{
						IDIP.QueryArenaRsp res = new IDIP.QueryArenaRsp(roleId,  rank);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_QUERY_ARENA_RSP;
						headerRes.Result = getIDIPResult(CommonRoleVisitor.ERR_OK);
						headerRes.RetErrMsg = getIDIPErrMsg(CommonRoleVisitor.ERR_OK);
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", QueryArenaRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
					}
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoClearDataReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoClearDataReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoClearDataReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoClearDataRsp res = new IDIP.DoClearDataRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_CLEAR_DATA_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoClearDataRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					if (req.IsDiamond != 1 && req.IsCopper!= 1 && req.IsPhysical != 1)
					{
						String errMsg = "no item need clear";
						IDIP.DoClearDataRsp res = new IDIP.DoClearDataRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_DO_CLEAR_DATA_RSP;
						headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
						headerRes.RetErrMsg = errMsg;
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", DoClearDataRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
						return;
					}
					gs.getLoginManager().clearRoleData(roleId,  req.IsDiamond, req.IsCopper, req.IsPhysical,
							new LoginManager.ClearRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoClearDataRsp res = new IDIP.DoClearDataRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_CLEAR_DATA_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoClearDataRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSetGeneralLvReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetGeneralLvReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetGeneralLvReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoSetGeneralLvRsp res = new IDIP.DoSetGeneralLvRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_SET_GENERAL_LV_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetGeneralLvRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().setGeneralLevel(roleId,  (int)req.GeneralId, req.Value, new LoginManager.SetGeneralLevelCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.DoSetGeneralLvRsp res = new IDIP.DoSetGeneralLvRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_SET_GENERAL_LV_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetGeneralLvRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoSetPetLvReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetPetLvReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetPetLvReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoSetPetLvRsp res = new IDIP.DoSetPetLvRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_SET_PET_LV_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetPetLvRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().setPetLevel(roleId,  (int)req.PetId, req.Value,
							new LoginManager.SetGeneralLevelCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoSetPetLvRsp res = new IDIP.DoSetPetLvRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_SET_PET_LV_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoSetPetLvRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryBagitemInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagitemInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagitemInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryBagitemInfoRsp res = new IDIP.QueryBagitemInfoRsp(0,  new ArrayList<IDIP.BackPackListInfo>());
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_BAGITEM_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagitemInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryEquipInfo(roleId, req.PageNo, new LoginManager.QueryEquipInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final List<IDIP.BackPackListInfo> binfo) {
								IDIP.QueryBagitemInfoRsp res = new IDIP.QueryBagitemInfoRsp();
								res.BackPackList_count = binfo.size();
								res.BackPackList = binfo;
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_BAGITEM_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryBagitemInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg
										+ ", BagList_count=" + res.BackPackList_count + ", BagList size =" + res.BackPackList.size()
										+ " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdateVipExpReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateVipExpReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateVipExpReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdateVipExpRsp res = new IDIP.DoUpdateVipExpRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_VIP_EXP_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateVipExpRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRolePay(roleId,  (int)req.Value, new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.DoUpdateVipExpRsp res = new IDIP.DoUpdateVipExpRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_VIP_EXP_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateVipExpRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.DoUpdateKingLevelReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateKingLevelReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateKingLevelReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.DoUpdateKingLevelRsp res = new IDIP.DoUpdateKingLevelRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_KING_LEVEL_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateKingLevelRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleLevel(roleId,  req.Value, new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.DoUpdateKingLevelRsp res = new IDIP.DoUpdateKingLevelRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_DO_UPDATE_KING_LEVEL_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", DoUpdateKingLevelRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryFightInfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryFightInfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryFightInfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryFightInfoRsp res = new IDIP.QueryFightInfoRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_FIGHT_INFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryFightInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryUserFightPowerInfo(roleId,  new LoginManager.QueryUserFightPowerCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, int power) 
							{
								IDIP.QueryFightInfoRsp res = new IDIP.QueryFightInfoRsp(power);
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_FIGHT_INFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryFightInfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryVipLevelReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryVipLevelReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryVipLevelReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryVipLevelRsp res = new IDIP.QueryVipLevelRsp(gs.getConfig().id, gs.getTimeGMT(), IDIP.IDIP_RSP_ROLE_NOT_EXIST, 0, 0);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_VIP_LEVEL_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryVipLevelRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryUsrVipLevel(roleId,  new LoginManager.QueryUsrVipLevelCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, int vip, int vipExp, int lackVipExp) 
							{
								IDIP.QueryVipLevelRsp res = new IDIP.QueryVipLevelRsp(gs.getConfig().id, gs.getTimeGMT(), vip, vipExp, lackVipExp);
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_VIP_LEVEL_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryVipLevelRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.QueryUserSigninReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUserSigninReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUserSigninReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.QueryUserSigninRsp res = new IDIP.QueryUserSigninRsp(gs.getConfig().id, gs.getTimeGMT(), IDIP.IDIP_RSP_ROLE_NOT_EXIST, "");
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_QUERY_USER_SIGNIN_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUserSigninRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryUsrCheckin(roleId,  new LoginManager.QueryUsrCheckinCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, int day, String reward) 
							{
								IDIP.QueryUserSigninRsp res = new IDIP.QueryUserSigninRsp(gs.getConfig().id, gs.getTimeGMT(), day, reward);
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_QUERY_USER_SIGNIN_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", QueryUserSigninRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqQueryUserbaseinfoReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUserbaseinfoReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUserbaseinfoReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqQueryUserbaseinfoRsp res = new IDIP.AqQueryUserbaseinfoRsp("", 0, 0, 0, 0, 0);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_QUERY_USERBASEINFO_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUserbaseinfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().queryUserBaseInfo(roleId, new LoginManager.QueryUserBaseInfoCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, String roleName, int lvl, int money, int diamond, int status, int lays) 
							{
								IDIP.AqQueryUserbaseinfoRsp res = new IDIP.AqQueryUserbaseinfoRsp(roleName, lvl, money, diamond, status, lays);
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_QUERY_USERBASEINFO_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUserbaseinfoRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoSendMailReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSendMailReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", DoSendMailReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoSendMailRsp res = new IDIP.AqDoSendMailRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_SEND_MAIL_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					int lifetime = 3600*24*7;
					String mailTile = "";
					List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
					if (req.MailContent.isEmpty())
					{
						String errMsg = "mail content null";
						IDIP.AqDoSendMailRsp res = new IDIP.AqDoSendMailRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_AQ_DO_SEND_MAIL_RSP;
						headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
						headerRes.RetErrMsg = errMsg;
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
						return;
					}
					gs.getLoginManager().sendRoleMail(roleId, mailTile, req.MailContent, lifetime, attLst,
							new LoginManager.SendRoleMailCallback() 
							{
								
								@Override
								public void onCallback(final int errcode) {
									IDIP.DoSendMailRsp res = new IDIP.DoSendMailRsp(errcode,  getIDIPErrMsg(errcode));
									
									IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
									headerRes.Cmdid = IDIP.IDIP_AQ_DO_SEND_MAIL_RSP;
									headerRes.Result = getIDIPResult(errcode);
									headerRes.RetErrMsg = getIDIPErrMsg(errcode);
									idips.sendRes(sessionid, headerRes, res);
									gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSendMailRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
								}
							});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoUpdateMoneyReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateMoneyReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateMoneyReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoUpdateMoneyRsp res = new IDIP.AqDoUpdateMoneyRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_UPDATE_MONEY_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateMoneyRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleMoney(roleId,  req.Value, new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.DoUpdateMoneyRsp res = new IDIP.DoUpdateMoneyRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_UPDATE_MONEY_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
								{
									gs.getTLogger().logIDIPCMD(req.OpenId, GameData.COMMON_TYPE_MONEY, 0, req.Value, req.Serial, req.Source, "10102813");
								}
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateMoneyRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoBanPlayReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanPlayReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanPlayReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoBanPlayRsp res = new IDIP.AqDoBanPlayRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_BAN_PLAY_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanPlayRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					String errMsg = (req.Type < 1 || req.Type > 4 && req.Type != 99) ? "ban play type error" : "";
					if (!errMsg.isEmpty())
					{
						IDIP.AqDoBanPlayRsp res = new IDIP.AqDoBanPlayRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_AQ_DO_BAN_PLAY_RSP;
						headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
						headerRes.RetErrMsg = errMsg;
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanPlayRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
						return;
					}
					int playBitTypes = req.Type != 99 ? (1 << (req.Type+1)) : (1 << LoginManager.BAN_BATTLE_INDEX) | (1 << LoginManager.BAN_MARCH_INDEX) | (1 << LoginManager.BAN_CHIBI_INDEX) | (1 << LoginManager.BAN_ARENA_INDEX);
					int banSecond = req.Time == 0 ? -1 : req.Time;
					gs.getLoginManager().userMultiBan(roleId, playBitTypes, banSecond, req.Reason, new LoginManager.UserMultiBanCallback()
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.AqDoBanPlayRsp res = new IDIP.AqDoBanPlayRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_BAN_PLAY_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanPlayRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoBanUsrReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanUsrReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanUsrReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
				{
					public void onCallback(String openID, Integer roleID)
					{
						if (roleID == null)
						{
							String errMsg = "can't find role";
							IDIP.AqDoBanUsrRsp res = new IDIP.AqDoBanUsrRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
							
							IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
							headerRes.Cmdid = IDIP.IDIP_AQ_DO_BAN_USR_RSP;
							headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
							headerRes.RetErrMsg = errMsg;
							idips.sendRes(sessionid, headerRes, res);
							gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
							return;
						}
						{
							int roleId = roleID.intValue();
							int banSecond = req.Time == 0 ? -1 : req.Time;
							String reason = req.Reason;
							gs.getLoginManager().banRole(roleId, banSecond, reason,  new LoginManager.ModifyRoleDataCallback()
								{
									
									@Override
									public void onCallback(final int errcode) {
										IDIP.AqDoBanUsrRsp res = new IDIP.AqDoBanUsrRsp(errcode,  getIDIPErrMsg(errcode));
										
										IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
										headerRes.Cmdid = IDIP.IDIP_AQ_DO_BAN_USR_RSP;
										headerRes.Result = getIDIPResult(errcode);
										headerRes.RetErrMsg = getIDIPErrMsg(errcode);
										idips.sendRes(sessionid, headerRes, res);
										gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoBanUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
									}
								});
						}
					}
				});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoRelievePunishReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoRelievePunishReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoRelievePunishReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoRelievePunishRsp res = new IDIP.AqDoRelievePunishRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_RELIEVE_PUNISH_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoRelievePunishRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					int playBitTypes = 0;
					if (req.RelieveAllPlay != 0)
						playBitTypes |= (1 << LoginManager.BAN_BATTLE_INDEX) | (1 << LoginManager.BAN_MARCH_INDEX) | (1 << LoginManager.BAN_CHIBI_INDEX) | (1 << LoginManager.BAN_ARENA_INDEX);
					if (req.RelieveMaskchat != 0)
						playBitTypes |= (1 << LoginManager.BAN_CHAT_INDEX);
					if (req.RelieveBan != 0)
						playBitTypes |= (1 << LoginManager.BAN_LOGIN_INDEX);
					gs.getLoginManager().userMultiUnban(roleId, playBitTypes, new LoginManager.UserMultiUnbanCallback()
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.AqDoRelievePunishRsp res = new IDIP.AqDoRelievePunishRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_RELIEVE_PUNISH_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoRelievePunishRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoUpdateDiamondReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateDiamondReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateDiamondReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoUpdateDiamondRsp res = new IDIP.AqDoUpdateDiamondRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_UPDATE_DIAMOND_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateDiamondRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					gs.getLoginManager().modifyRoleStone(roleId,  req.Value, req.IsLogin == 1, new LoginManager.ModifyRoleDataCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) {
								IDIP.DoUpdateDiamondRsp res = new IDIP.DoUpdateDiamondRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_UPDATE_DIAMOND_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								if (headerRes.Result == IDIP.IDIP_HEADER_RESULT_SUCCESS)
								{
									gs.getTLogger().logIDIPCMD(req.OpenId, GameData.COMMON_TYPE_STONE, 0, req.Value, req.Serial, req.Source, "10102822");
								}
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoUpdateDiamondRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqQueryUsrsignGuildnoticeReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUsrsignGuildnoticeReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUsrsignGuildnoticeReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqQueryUsrsignGuildnoticeRsp res = new IDIP.AqQueryUsrsignGuildnoticeRsp("", 0, "", 0, 0, "");
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					final int roleId = roleID.intValue();
					gs.getLoginManager().queryUsrSignGuildID(roleId, new LoginManager.QueryUsrSignGuildIDCallback() 
						{
							
							@Override
							public void onCallback(final int errcode, final String roleName, final int lvl, final String usrSign, final int guildId) 
							{
								gs.getForceManager().queryForce(guildId, new ForceManager.QueryForceCallback()
								{
										public void onCallback(ForceBrief brief)
										{
											int guildJob = 0;
											String guildNotice = "";
											if (brief != null)
											{
												if (roleId == brief.headID)
													guildJob = 1;
												guildNotice = brief.msg;
											}
											IDIP.AqQueryUsrsignGuildnoticeRsp res = new IDIP.AqQueryUsrsignGuildnoticeRsp(roleName, lvl, usrSign, guildId, guildJob, guildNotice);
											
											IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
											headerRes.Cmdid = IDIP.IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_RSP;
											headerRes.Result = getIDIPResult(errcode);
											headerRes.RetErrMsg = getIDIPErrMsg(errcode);
											idips.sendRes(sessionid, headerRes, res);
											gs.getLogger().info("idip sessionid=" + sessionid + ", AqQueryUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
										}
								});
							}
						});
				}
			}
		});
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoSetUsrsignGuildnoticeReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		
		String rname = req.GuildNotice;
		String errMsg = (req.Type == 1 || req.Type == 2) ? "" : "type error";
		if (errMsg.isEmpty())
		{
			if (req.Type == 1)
			{
				rname = gs.getLoginManager().checkUserInput(req.GuildNotice, GameData.getInstance().getUserInputCFG().roleName, true, true);
				errMsg = (rname == null) ? "bad name input" : (GameData.getInstance().isBadName(rname) ? "invalid name input" : "");
			}
			else
			{
				rname = gs.getLoginManager().checkUserInput(req.GuildNotice, GameData.getInstance().getUserInputCFG().forceAnnounce, false, true);
				errMsg = (rname == null) ? "bad notice input" : "";
			}	
		}
		if (!errMsg.isEmpty())
		{
			IDIP.AqDoSetUsrsignGuildnoticeRsp res = new IDIP.AqDoSetUsrsignGuildnoticeRsp(IDIP.IDIP_RSP_FAILED,  errMsg);
			
			IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
			headerRes.Cmdid = IDIP.IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP;
			headerRes.Result = IDIP.IDIP_HEADER_RESULT_API_EXCEPTION;
			headerRes.RetErrMsg = errMsg;
			idips.sendRes(sessionid, headerRes, res);
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg);
			return;
		}
		
		if (req.Type == 1)
		{
			final String setname = rname;
			gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
			{
				public void onCallback(String openID, Integer roleID)
				{
					if (roleID == null)
					{
						String errMsg = "can't find role";
						IDIP.AqDoSetUsrsignGuildnoticeRsp res = new IDIP.AqDoSetUsrsignGuildnoticeRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
						
						IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
						headerRes.Cmdid = IDIP.IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP;
						headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
						headerRes.RetErrMsg = errMsg;
						idips.sendRes(sessionid, headerRes, res);
						gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
						return;
					}
					{
						int roleId = roleID.intValue();
						gs.getLoginManager().modifyRoleName(roleId, setname, new LoginManager.ModifyRoleNameCallback() 
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.AqDoSetUsrsignGuildnoticeRsp res = new IDIP.AqDoSetUsrsignGuildnoticeRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
					}
				}
			});
		}
		else
		{
			int foreceID = (int)req.GuildId;
			gs.getForceManager().setForceAnnounce(foreceID, rname, new ForceManager.ModifyForceAnnounceCallback() 
			{
				@Override
				public void onCallback(final int errcode) 
				{
					IDIP.AqDoSetUsrsignGuildnoticeRsp res = new IDIP.AqDoSetUsrsignGuildnoticeRsp(errcode,  getIDIPErrMsg(errcode));
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP;
					headerRes.Result = getIDIPResult(errcode);
					headerRes.RetErrMsg = getIDIPErrMsg(errcode);
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoSetUsrsignGuildnoticeRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
				}
			});
		}
		
		
	}
	
	private void onHandleIDIPReq(final int sessionid, final IDIP.IdipHeader headerReq, final IDIP.AqDoMaskchatUsrReq req)
	{
		gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoMaskchatUsrReq: PlatId=" + req.PlatId + ", Partition=" + req.Partition);
		if (req.PlatId != gs.getConfig().platID || req.Partition != gs.getConfig().id)
		{
			gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoMaskchatUsrReq: discard packet for id is not match(gs PlatId=" + gs.getConfig().platID + ", gs Partition=" + gs.getConfig().id + ")");
			return;
		}
		gs.getLoginManager().getRoleIDByOpendID(req.OpenId, new LoginManager.GetRoleIDByOpenIDCallback()
		{
			public void onCallback(String openID, Integer roleID)
			{
				if (roleID == null)
				{
					String errMsg = "can't find role";
					IDIP.AqDoMaskchatUsrRsp res = new IDIP.AqDoMaskchatUsrRsp(IDIP.IDIP_RSP_ROLE_NOT_EXIST,  errMsg);
					
					IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
					headerRes.Cmdid = IDIP.IDIP_AQ_DO_MASKCHAT_USR_RSP;
					headerRes.Result = IDIP.IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS;
					headerRes.RetErrMsg = errMsg;
					idips.sendRes(sessionid, headerRes, res);
					gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoMaskchatUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + openID + ")");
					return;
				}
				{
					int roleId = roleID.intValue();
					int playBitTypes = 1 << LoginManager.BAN_CHAT_INDEX;
					int banSecond = req.Time == 0 ? -1 : req.Time;
					String reason = req.Reason;
					gs.getLoginManager().userMultiBan(roleId, playBitTypes, banSecond, reason, new LoginManager.UserMultiBanCallback()
						{
							
							@Override
							public void onCallback(final int errcode) 
							{
								IDIP.AqDoMaskchatUsrRsp res = new IDIP.AqDoMaskchatUsrRsp(errcode,  getIDIPErrMsg(errcode));
								
								IDIP.IdipHeader headerRes = idips.getResHeader(headerReq, gs.getTimeGMT());
								headerRes.Cmdid = IDIP.IDIP_AQ_DO_MASKCHAT_USR_RSP;
								headerRes.Result = getIDIPResult(errcode);
								headerRes.RetErrMsg = getIDIPErrMsg(errcode);
								idips.sendRes(sessionid, headerRes, res);
								gs.getLogger().info("idip sessionid=" + sessionid + ", AqDoMaskchatUsrRsp: result="+ headerRes.Result + ",RetErrMsg=" + headerRes.RetErrMsg + " (opendID=" + req.OpenId + ")");
							}
						});
				}
			}
		});
	}
	
	public void setDisconnectMode(boolean bDisconnectMode)
	{
		if( bDisconnectMode && ! this.bDisconnectMode )
		{
			for(int sid : sessions.keySet())
			{
				tgs.closeSession(sid);
			}
		}
		this.bDisconnectMode = bDisconnectMode;	
	}

	NetManager managerNet = null;
	TCPGameServer tgs = null;
	TCPControlServer tcs = null;
	UDPLogger udpLogger = null;
	TCPIDIPServer idips = null;
	TCPExchangeClient tec = null;
	TCPFriendClient tfc = null;
	GameServer gs;
	Map<Integer, SessionInfo> sessions = new ConcurrentHashMap<Integer, SessionInfo>();
	AtomicInteger sps = new AtomicInteger(0);
	// for debug
	boolean bDisconnectMode = false;
	GSNet gsNetStat = new GSNet();
        
}


package i3k.gs;

import i3k.DBRole;
import i3k.SBean;
import i3k.SBean.CityInfo;
import i3k.SBean.ExpatriateTransRoleReq;
import i3k.SBean.ExpatriateTransRoleRes;
import i3k.SBean.ExpiratBossRanks;
import i3k.SBean.ExpiratBossRanksRes;
import i3k.gs.LoginManager.CommonRoleVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class ExchangeManager
{
	
	public static boolean needUpdate(boolean bLocalServer, int timeLastSync, int timeLastLogin, int now)
	{
		int cool = 0;
		if( timeLastLogin + 86400 * 30 < now )
		{
			cool = 86400 * 7;
		}
		else if( timeLastLogin + 86400 * 10 < now )
		{
			cool = 86400 * 3;
		}
		else if( timeLastLogin + 86400 * 3 < now )
		{
			cool = 86400;
		}
		else if( bLocalServer )
		{
			cool = 14400;
		}
		else
		{
			cool = 43200;
		}
		return timeLastSync + cool < now;
	}

	public ExchangeManager(GameServer gs)
	{
		this.gs = gs;
		this.service.setTag(gs.getTime());
		this.expatriateService.setTag(gs.getTime());
	}
	
	public void announceCreateRole(String openID, int roleID)
	{
		if( openID == null )
			return;
		// TODO
		if( openID.equals("") )
			return;
		gs.getRPCManager().friendAnnounceCreateRole(openID, roleID);
	}
	
	public void searchFriends(int roleID, List<String> openIDs)
	{
		if( openIDs.isEmpty() )
			return;
		gs.getRPCManager().friendSearchFriends(roleID, openIDs);
	}
	
	public void setSearchFriendsRes(SBean.SearchFriendsRes res)
	{
		if( res.roleID.serverID != gs.getConfig().id )
			return;
		int rid = res.roleID.roleID;
		Role role = gs.getLoginManager().getRole(rid);
		if( role == null || role.isNull() )
			return;
		role.setSearchFriendsRes(res);
	}
	
	public void setCreateRoleRes(SBean.GlobalRoleID roleID)
	{
		if( roleID.serverID != gs.getConfig().id )
			return;
		int rid = roleID.roleID;
		Role role = gs.getLoginManager().getRole(rid);
		if( role == null || role.isNull() )
			return;
		role.setCreateRoleExchangeOK();
	}
	
	public void sendMessageToFriend(final int roleID, final SBean.GlobalRoleID targetID, final byte msgType, final short arg1, final int arg2)
	{
		// TODO local msg list
		if( targetID.serverID == gs.getConfig().id )
		{
			localTasks.add(new Runnable()
			{
				@Override
				public void run()
				{
					gs.getLoginManager().recvMessageFromFriend(new SBean.GlobalRoleID(targetID.serverID, roleID), targetID, msgType, arg1, arg2);		
				}
				
			});
			return;
		}
		gs.getRPCManager().friendSendMessageToFriend(new SBean.GlobalRoleID(gs.getConfig().id, roleID), targetID, msgType, arg1, arg2);
	}
	
	public void handleSearchCityRequest(final SBean.SearchCityReq req)
	{
		gs.getLogger().debug("recevice search city req : " + CityManager.toBriefString(req));
		gs.getCityManager().citySearchedByOther(req.roleID.serverID, req.roleID.roleID, req.level, req.cityIDMax, req.searchType, new CityManager.CitySearchedCallback()
				{
					public void onCallback(int error, SBean.CityInfo cinfo)
					{
						if (cinfo == null)
						{
							cinfo = CityManager.makeCityInfo();
							if (error == CityManager.INVOKE_SUCCESS)
								error = CityManager.INVOKE_UNKNOWN_ERROR;
						}
						SBean.SearchCityRes res = CityManager.makeResponse(error, cinfo, req);
						gs.getRPCManager().exchangeSendSearchCityRes(res);
					}
				});
	}
	
	public void handleSearchCityResult(SBean.SearchCityRes res)
	{
		gs.getLogger().debug("recevice search city res : " + CityManager.toBriefString(res));
		gs.getCityManager().handleSearchCityResult(res);
	}
	
	public void handleCityAttackedStartRequest(final SBean.AttackCityStartReq req)
	{
		gs.getLogger().debug("recevice city attacked start req : " + CityManager.toBriefString(req));
		gs.getCityManager().cityAttackedStartByOther(req.roleID.serverID, req.roleID.roleID, req.targetRoleID.roleID, req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, new CityManager.CityAttackedStartCallback() 
			{
				@Override
				public void onCallback(int errcode, SBean.CityInfo info) 
				{
					if (info == null)
					{
						info = CityManager.makeCityInfo();
						if (errcode == CityManager.INVOKE_SUCCESS)
							errcode = CityManager.INVOKE_UNKNOWN_ERROR;
					}
					SBean.AttackCityStartRes res = CityManager.makeResponse(errcode, info, req);
					gs.getRPCManager().exchangeSendAttackCityStartRes(res);
				}
			});
	}
	
	public void handleAttackCityStartResult(SBean.AttackCityStartRes res)
	{
		gs.getLogger().debug("recevice attack city start res : " + CityManager.toBriefString(res));
		gs.getCityManager().handleAttackCityStart(res);
	}
	
	public void handleCityAttackedEndRequest(final SBean.AttackCityEndReq req)
	{
		gs.getLogger().debug("recevice city attacked end req : " + CityManager.toBriefString(req));
		gs.getCityManager().cityAttackedEndByOther(req.roleID.serverID, req.roleID.roleID, req.roleName, req.targetRoleID.roleID, req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, req.win, new CityManager.CityAttackedEndCallback() 
		{
			@Override
			public void onCallback(int errcode, SBean.CityInfo info) 
			{
				if (info == null)
				{
					info = CityManager.makeCityInfo();
					if (errcode == CityManager.INVOKE_SUCCESS)
						errcode = CityManager.INVOKE_UNKNOWN_ERROR;
				}
				SBean.AttackCityEndRes res = CityManager.makeResponse(errcode, info, req);
				gs.getRPCManager().exchangeSendAttackCityEndRes(res);
			}
		});
	}
	
	public void handleAttackCityEndResult(SBean.AttackCityEndRes res)
	{
		gs.getLogger().debug("recevice attack city end res : " + CityManager.toBriefString(res));
		gs.getCityManager().handleAttackCityEnd(res);
	}
	
	public void handleCityOwnerQueriedRequest(final SBean.QueryCityOwnerReq req)
	{
		gs.getLogger().debug("recevice city owner queried req : " + CityManager.toBriefString(req));
		gs.getCityManager().cityOwnerQueriedByOther(req.roleID.serverID, req.roleID.roleID, req.targetRoleID.roleID, req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, new CityManager.CityOwnerQueriedCallback() 
		{
			public void onCallback(int errcode, CityInfo cinfo) 
			{		
				if (cinfo == null)
				{
					cinfo = CityManager.makeCityInfo();
					if (errcode == CityManager.INVOKE_SUCCESS)
						errcode = CityManager.INVOKE_UNKNOWN_ERROR;
				}
				SBean.QueryCityOwnerRes res = CityManager.makeResponse(errcode, cinfo, req);
				gs.getRPCManager().exchangeSendQueryCityOwnerRes(res);
			}
		});
	}
	
	public void handleQueryCityOwnerResult(SBean.QueryCityOwnerRes res)
	{
		gs.getLogger().debug("recevice query city owner res : " + CityManager.toBriefString(res));
		gs.getCityManager().handleQueryCityOwner(res);
	}
	
	public void handleNotifyCityOwnerChangedRequest(final SBean.NotifyCityOwnerChangedReq req)
	{
		gs.getLogger().debug("recevice city owner changed req : " + CityManager.toBriefString(req));
		gs.getCityManager().baseCityOwnerChanged(req.roleID.serverID, req.roleID.roleID, req.roleName, req.baseRoleID.serverID, req.baseRoleID.roleID, req.cityID, new CityManager.BaseCityOwnerChangedCallback()
			{
				public void onCallback(int errcode)
				{
					
				}
			});
	}
	
	public void handleDuelJoinResult(SBean.DuelJoinRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelSelectGeneralResult(SBean.DuelSelectGeneralRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelSelectGeneralForward(SBean.DuelSelectGeneralReq req)
	{
		SBean.DuelSelectGeneralRes res = gs.getDuelManager().handleSelectGeneralForward(req);
		if (res == null)
			res = new SBean.DuelSelectGeneralRes(req.id, req.roleID, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId);
		gs.getRPCManager().exchangeSendDuelSelectGeneralForwardRes(res);
	}

	public void handleDuelStartAttackResult(SBean.DuelStartAttackRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelStartAttackForward(SBean.DuelStartAttackReq req)
	{
		SBean.DuelStartAttackRes res = gs.getDuelManager().handleStartAttackForward(req);
		if (res == null)
			res = new SBean.DuelStartAttackRes(req.id, req.roleID, req.index, DuelManager.INVOKE_UNKNOWN_ERROR, req.matchId);
		gs.getRPCManager().exchangeSendDuelStartAttackForwardRes(res);
	}

	public void handleDuelFinishAttackResult(SBean.DuelFinishAttackRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelOppoGiveupResult(SBean.DuelOppoGiveupRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}
	
	public void handleDuelFinishAttackOnOppoDownLineResult(SBean.DuelFinishAttackOnOppoDownLineRes res)
	{
	    gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelOppoGiveupForward(SBean.DuelOppoGiveupRes res)
	{
		gs.getDuelManager().handleOppoGiveupForward(res);
	}
	
	public void handleDuelFinishAttackOnOppoDownLineForward(SBean.DuelFinishAttackOnOppoDownLineRes res)
	{
	    gs.getDuelManager().handleFinishAttackOnOppoDownLineForward(res);
	}

	public void handleDuelRankResult(SBean.DuelRankRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelTopRanksResult(SBean.DuelTopRanksRes res)
	{
		gs.getDuelManager().handleEventTaskResult(res.id, res);
	}

	public void handleDuelRanksRequest()
	{
		SBean.DuelRanksRes res = gs.getDuelManager().handleRanksRequest();
		if (res == null)
			res = new SBean.DuelRanksRes(gs.getConfig().id, new ArrayList<SBean.DuelRank>());
		gs.getRPCManager().exchangeSendDuelRanksRes(res);
	}
	
	public void handleSWarSearchResult(SBean.SWarSearchRes res)
	{
		gs.getSWarManager().handleEventTaskResult(res.id, res);
	}

	public void handleSWarSearchForward(SBean.SWarSearchReq req)
	{
		gs.getSWarManager().handleSearchForward(req);
	}

	public void handleSWarAttackResult(SBean.SWarAttackRes res)
	{
		gs.getSWarManager().handleEventTaskResult(res.id, res);
	}

	public void handleSWarAttackForward(SBean.SWarAttackReq req)
	{
		gs.getSWarManager().handleAttackForward(req);
	}

	public void handleSWarRankResult(SBean.SWarRankRes res)
	{
		gs.getSWarManager().handleEventTaskResult(res.id, res);
	}

	public void handleSWarTopRanksResult(SBean.SWarTopRanksRes res)
	{
		gs.getSWarManager().handleEventTaskResult(res.id, res);
	}

	public void handleSWarRanksRequest()
	{
		SBean.SWarRanksRes res = gs.getSWarManager().handleRanksRequest();
		if (res == null)
			res = new SBean.SWarRanksRes(gs.getConfig().id, new ArrayList<SBean.SWarRank>());
		gs.getRPCManager().exchangeSendSWarRanksRes(res);
	}
	
	public void handleSWarVoteForward(SBean.SWarVoteReq req)
	{
		gs.getSWarManager().handleVoteForward(req);
	}

	public void handleSWarRelease(SBean.SWarReleaseReq req)
	{
		gs.getSWarManager().handleRelease(req);
	}

	public void handleExpiratBossRanksRequest()
	{
		ExpiratBossRanksRes res = gs.getExpiratBossManager().handleRanksRequest();
		if (res == null){
			List<ExpiratBossRanks> rankslist =new ArrayList<ExpiratBossRanks>();
			res = new SBean.ExpiratBossRanksRes(gs.getConfig().id, rankslist);
		}
			
		gs.getRPCManager().exchangeSendExpiratBossRanksRes(res);
	}
	
    /*
	public void handleQueryUserHasRolesRequest(final QueryHasRoleReq req)
        {
                gs.getLogger().debug("recevice city owner queried req : fromgs["+req.srcgs+"]   togs["+req.targs+"]"  );
                gs.getLoginManager().getRoleIDByOpendID(req.openid, new LoginManager.GetRoleIDByOpenIDCallback()
                {
                        @Override
                        public void onCallback(String openID, Integer roleID)
                        {
                                SBean.QueryHasRoleRes res = new SBean.QueryHasRoleRes();
                                res.tag = req.tag;
                                res.id = req.id+1;
                                res.srcgs = gs.getConfig().id;
                                res.targs = req.srcgs;
                                res.plat = req.plat;
                                res.area = req.area;
                                if (roleID!=null && roleID.intValue()>0)
                                {
                                        res.conflict_flag = 0;
                                        res.role_count = 0;
                                        res.role_names = "";
                                        gs.getRPCManager().exchangeSendQueryUserHasRoleRes(res);
                                }
                                else
                                {
                                        res.conflict_flag = 1;
                                        res.role_count = 1;
                                        gs.getRPCManager().exchangeSendQueryUserHasRoleRes(res);
                                }
                        }
                });
        }
        
	
	public void handleQueryUserHasRolesResponse(QueryHasRoleRes res)
        {
                boolean hasConflict = res.conflict_flag==0? false : true;
                if (hasConflict)
                {
                        
                }
                else
                {
                        
                }
                
        }
    */
	public void handleTransDBRoleResult(ExpatriateTransRoleRes res)
        {
                gs.getLogger().debug("recevice transdbrole  res : " + res.operror);
		handleExpatriateResult(res);               
        }
    
	private void handleExpatriateResult(ExpatriateTransRoleRes res)
        {
               expatriateService.onReceiveExpatriateResponse(res); 
        }

        public void updateFriendProp(final int roleID, final List<SBean.OpenIDRecord> recordsLocal, final List<SBean.OpenIDRecord> recordsRemote)
	{
		// TODO local msg list
		if( ! recordsRemote.isEmpty() )
			gs.getRPCManager().friendUpdateFriendProp(new SBean.UpdateFriendPropReq(new SBean.GlobalRoleID(gs.getConfig().id, roleID), recordsRemote));
		
		for(final SBean.OpenIDRecord r : recordsLocal)
		{
			localTasks.add(new Runnable()
			{
				@Override
				public void run()
				{
					//TODO
					final int rid = r.roleID.roleID;
					//
					gs.getLoginManager().execCommonRoleVisitor(rid, new CommonRoleVisitor()
					{
						List<SBean.FriendProp> prop = new ArrayList<SBean.FriendProp>();
						@Override
						public boolean visit(Role role)
						{
							synchronized( role )
							{
								prop.add(role.getFriendPropWithoutLock());
							}
							return true;
						}

						@Override
						public byte visit(DBRole dbrole)
						{
							prop.add(dbrole.getFriendProp());
							return CommonRoleVisitor.ERR_DB_OK_NOSAVE;
						}

						@Override
						public void onCallback(boolean bDB, byte errcode, String rname)
						{
							if( ! prop.isEmpty() )
							{
								// TODO
								Role role = gs.getLoginManager().getRole(roleID);
								if( role == null || role.isNull() )
									return;
								role.setSearchFriendsRes(new SBean.SearchFriendsRes(new SBean.GlobalRoleID(gs.getConfig().id, roleID),
										r, prop.get(0)));
								//
							}
								
						}
					
					});
					//
				}
				
			});
		}
	}
	
	public void onTimer()
	{
		while( true )
		{
			Runnable runnable = localTasks.poll();
			if( runnable == null )
				break;
			runnable.run();
		}
		service.onTimer();
//		expatriateService.onTimer();
	}
    
	interface ExpatriateCallback
	{
		void onCallback(int errorCode, String[] msg);
	}
    
	
	class ExpatriateTask
	{
		ExpatriateTask(int gsid, DBRole dbrole, ExpatriateCallback callback)
		{
			this.taskID = service.getNextID();
			this.dstgsID = gsid;
			this.dbRole = dbrole;
			this.callback = callback;
		}
		
		void doTask()
		{
			gs.getRPCManager().sendExpatriateDBRoleReq(service.getTag(), taskID, dstgsID, dbRole);
			this.sendTime = System.currentTimeMillis();
		}
		
		int getID()
		{
			return taskID;
		}
		
		boolean isTooOld(long now) 
		{
			return sendTime + MAX_WAIT_TIME <= now;
		}
		
		void onCallback(int errorCode,  SBean.ExpatriateTransRoleRes data)
		{
			try
			{
				if (this.callback != null)
				{
					String[] msgs = null;
					if (data != null)
					{
					}
					this.callback.onCallback(errorCode, msgs);
				}
			}
			catch(Exception ex)
			{			
				gs.getLogger().warn(ex.getMessage(), ex);
			}
		}
		int taskID;
		int dstgsID;
		DBRole dbRole;
		ExpatriateCallback callback;
		long sendTime;
	}
	class ExpatriateService
	{
		ExpatriateService()
		{
			
		}

                void setTag(int tag)
		{
			this.tag = tag;
		}
		
		void onTimer()
		{
			List<ExpatriateTask> timeoutTasks = removeAndGetTimeoutTasks(System.currentTimeMillis());
			for (ExpatriateTask task : timeoutTasks) 
			{
				task.onCallback(REQUEST_RESPONSE_TIMEOUT, null);
				gs.getLogger().warn("exchange task timeout remove callback, id=" + task.taskID + ", dstgsid=" + task.dstgsID);
			}
		}
		
		int getTag()
		{
			return this.tag;
		}

		int getNextID() 
		{
			return taskID.incrementAndGet();
		}
		
		public synchronized void sendExpatriateTransRoleRequest(int gsid, DBRole dbrole, ExpatriateCallback callback) 
		{
			ExpatriateTask t = new ExpatriateTask(gsid, dbrole, callback);
			t.doTask();
			tasks.put(t.getID(), t);
			gs.getLogger().warn("ExpatriateUserTrans 3 sendExpatriateTransRoleRequest "+" gsid:"+gsid
					+ " dbrole.id:"+dbrole.id+" userName :"+dbrole.userName+" time:"+gs.getTime());
		}
		
		public synchronized void sendExpatriateTransRoleRequest(int gsid, DBRole dbrole) 
		{
			ExpatriateTask t = new ExpatriateTask(gsid, dbrole, null);
			t.doTask();
		}
		
		public void onReceiveExpatriateResponse(SBean.ExpatriateTransRoleRes data)
		{
			if (data.tag == this.tag)
			{
				ExpatriateTask task = this.removeAndGetTask(data.id);
				if (task != null) 
				{
					task.onCallback(REQUEST_RESPONSE_SUCCESS, data);
				} 
				else 
				{
					gs.getLogger().warn("expatriate task callback tag=" + tag + ", id=" + data.id + "==>but timeout");
				}
			}
		}
		
		private synchronized ExpatriateTask removeAndGetTask(int id) 
		{
			return tasks.remove(id);
		}
		
		private synchronized List<ExpatriateTask> removeAndGetTimeoutTasks(long now) 
		{
			List<ExpatriateTask> timeoutTasks = new ArrayList<ExpatriateTask>();
			Iterator<Map.Entry<Integer, ExpatriateTask>> it = tasks.entrySet().iterator();
			while (it.hasNext()) 
			{
				ExpatriateTask task = it.next().getValue();
				if (task.isTooOld(now)) 
				{
					timeoutTasks.add(task);
					it.remove();
				}
			}
			return timeoutTasks;
		}
		
		int tag;
		AtomicInteger taskID = new AtomicInteger();
		Map<Integer, ExpatriateTask> tasks = new HashMap<Integer, ExpatriateTask>();
	}
    
	interface ExchangeCallback
	{
		void onCallback(int errorCode, String[] msg);
	}
    
	class ExchangeTask
	{
		int taskID;
		int dstgsID;
		List<String> msg;
		ExchangeCallback callback;
		long sendTime;
		
		
		ExchangeTask(int gsid, List<String> msg, ExchangeCallback callback)
		{
			this.taskID = service.getNextID();
			this.dstgsID = gsid;
			this.msg = (msg == null) ? new ArrayList<String>() : msg;
			this.callback = callback;
		}
		
		
		void doTask()
		{
			gs.getRPCManager().sendExchangeChannelMsgReq(service.getTag(), taskID, dstgsID, msg);
			this.sendTime = System.currentTimeMillis();
		}
		
		int getID()
		{
			return taskID;
		}
		
		boolean isTooOld(long now) 
		{
			return sendTime + MAX_WAIT_TIME <= now;
		}
		
		void onCallback(int errorCode, List<String> data)
		{
			try
			{
				if (this.callback != null)
				{
					String[] msgs = null;
					if (data != null)
					{
						msgs = new String[data.size()];
						msgs = data.toArray(msgs);
					}
					this.callback.onCallback(errorCode, msgs);
				}
			}
			catch(Exception ex)
			{			
				gs.getLogger().warn(ex.getMessage(), ex);
			}
		}
	}
	
	class ExchangeService
	{
		ExchangeService()
		{
			
		}
		
		void setTag(int tag)
		{
			this.tag = tag;
		}
		
		void onTimer()
		{
			List<ExchangeTask> timeoutTasks = removeAndGetTimeoutTasks(System.currentTimeMillis());
			for (ExchangeTask task : timeoutTasks) 
			{
				task.onCallback(REQUEST_RESPONSE_TIMEOUT, null);
				gs.getLogger().warn("exchange task timeout remove callback, id=" + task.taskID + ", dstgsid=" + task.dstgsID);
			}
		}
		
		int getTag()
		{
			return this.tag;
		}

		int getNextID() 
		{
			return taskID.incrementAndGet();
		}
		
		public synchronized void sendExchangeRequest(int gsid, List<String> msg, ExchangeCallback callback) 
		{
			ExchangeTask t = new ExchangeTask(gsid, msg, callback);
			t.doTask();
			tasks.put(t.getID(), t);
		}
		
		public synchronized void sendExchangeRequest(int gsid, List<String> msg) 
		{
			ExchangeTask t = new ExchangeTask(gsid, msg, null);
			t.doTask();
		}
		
		public void onReceiveExhcnageResponse(SBean.ExchangeChannelMessage data)
		{
			if (data.tag == this.tag)
			{
				ExchangeTask task = this.removeAndGetTask(data.id);
				if (task != null) 
				{
					task.onCallback(REQUEST_RESPONSE_SUCCESS, data.msg);
				} 
				else 
				{
					gs.getLogger().warn("exchange task callback tag=" + tag + ", id=" + data.id + "==>but timeout");
				}
			}
		}
		
		private synchronized ExchangeTask removeAndGetTask(int id) 
		{
			return tasks.remove(id);
		}
		
		private synchronized List<ExchangeTask> removeAndGetTimeoutTasks(long now) 
		{
			List<ExchangeTask> timeoutTasks = new ArrayList<ExchangeTask>();
			Iterator<Map.Entry<Integer, ExchangeTask>> it = tasks.entrySet().iterator();
			while (it.hasNext()) 
			{
				ExchangeTask task = it.next().getValue();
				if (task.isTooOld(now)) 
				{
					timeoutTasks.add(task);
					it.remove();
				}
			}
			return timeoutTasks;
		}
		
		int tag;
		AtomicInteger taskID = new AtomicInteger();
		Map<Integer, ExchangeTask> tasks = new HashMap<Integer, ExchangeTask>();
	}
	
	public ExchangeService getExchangeService()
	{
		return service;
	}
    
	public ExpatriateService getExpatriateService()
	{
	        return expatriateService;
	}
    
	public void handleTransDBRole(ExpatriateTransRoleReq req)
        {
                gs.getLoginManager().expatriateDBRole(req);
        }
    
	private GameServer gs;
	private ConcurrentLinkedDeque<Runnable> localTasks = new ConcurrentLinkedDeque<Runnable>();
	private ExchangeService service = new ExchangeService();
	private ExpatriateService expatriateService = new ExpatriateService();
	private static final int MAX_WAIT_TIME = 2000;
	public static final int REQUEST_RESPONSE_SUCCESS = 0;
	public static final int REQUEST_RESPONSE_UNKNOWN_ERROR = -1;
	public static final int REQUEST_RESPONSE_TIMEOUT = -2;
	public static final int REQUEST_RESPONSE_SEARCH_ERROR = -3;
}

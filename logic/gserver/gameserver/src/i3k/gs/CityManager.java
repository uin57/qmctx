package i3k.gs;

import i3k.DBRole;
import i3k.DBRoleGeneral;
import i3k.LuaPacket;
import i3k.SBean;
import i3k.TLog;
import i3k.gs.Role.General;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class CityManager 
{

	static public CityManager getInstance() 
	{
		return instance;
	}

	public CityManager(GameServer gs) 
	{
		this.gs = gs;
		this.service = new CityEventService(gs);
		instance = this;
	}

	public void init() 
	{

	}

	public void save() 
	{

	}

	public void onTimer(int timeTick) 
	{
		service.onTaskTimer();
	}

	interface CitySearchedCallback 
	{
		public void onCallback(int error, SBean.CityInfo info);
	}

	public void citySearchedByOther(int searchRoleGSID, int searchRoleRoleID, int slvl, int scityMax, int searchType, final CitySearchedCallback callback) 
	{
		List<SBean.CitySearchLevelCFGS> scfgs = GameData.getInstance().getCaptureCityCfg().searchTypeLvlRanges;
		if (searchType <= 0)
			searchType = 1;
		if (searchType > scfgs.size())
			searchType = scfgs.size();
		SBean.CitySearchLevelCFGS scfg = scfgs.get(searchType-1);
		int minLvl = slvl + scfg.lvlDiffMin;
		int maxLvl = slvl + scfg.lvlDiffMax;
		Integer rid = getLevelRandomRole(searchRoleGSID, searchRoleRoleID, minLvl, maxLvl);
		if (rid != null) 
		{
			gs.getLogger().debug("searchCityByPolicy match random role(" + rid + ")");
			queryRoleCaptureCityInfo(rid, scityMax, searchType, new QueryRoleCaptureCityInfoCallback() 
					{
						public void onCallback(int errcode, SBean.CityInfo cinfo) 
						{
							callback.onCallback(errcode, cinfo);
						}
					});
		} 
		else 
		{
			gs.getLogger().debug("searchCityByPolicy no math random role!");
			callback.onCallback(INVOKE_SEARCH_CANNOT_FIND_MATCH_ROLE, null);
		}
	}
	
	public interface QueryRoleCaptureCityInfoCallback 
	{
		public void onCallback(int errcode, SBean.CityInfo info);
	}

	public void queryRoleCaptureCityInfo(final int rid, final int cityIdMax, final int type, final QueryRoleCaptureCityInfoCallback callback) 
	{
		gs.getLoginManager().execCommonRoleVisitor(rid, new LoginManager.CommonRoleVisitor() 
				{
					int error = INVOKE_UNKNOWN_ERROR;
					SBean.CityInfo cinfo;

					@Override
					public byte visit(DBRole dbrole) 
					{
						DBRoleCitys citys = new DBRoleCitys(gs, dbrole, dbrole.citys);
						cinfo = citys.getSearchCityInfo(cityIdMax, type);
						error = (cinfo == null) ? INVOKE_NOT_FIND_CITY_ERROR : INVOKE_SUCCESS;
						return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
					}

					@Override
					public boolean visit(Role role) 
					{
						synchronized (role)
						{
							cinfo = role.citys.getSearchCityInfo(cityIdMax, type);
							error = (cinfo == null) ? INVOKE_NOT_FIND_CITY_ERROR : INVOKE_SUCCESS;
						}
						return true;
					}

					@Override
					public void onCallback(boolean bDB, byte errcode, String rname) 
					{
						if (errcode != 0)
							error = bDB ? INVOKE_VIST_DBROLE_ERROR : INVOKE_VIST_ROLE_ERROR;
						else if (error == INVOKE_SUCCESS && cinfo == null)
							error = INVOKE_UNKNOWN_ERROR;
						if (cinfo == null)
							cinfo = CityManager.makeCityInfo();
						callback.onCallback(error, cinfo);
					}
				});
	}

	interface CityAttackedStartCallback 
	{
		public void onCallback(int errcode, SBean.CityInfo info);
	}
	public void cityAttackedStartByOther(final int srcGSID, final int srcRoleID, final int ownerRoleID, 
										 final int baseGSID, final int baseRoleID, final int cityID, final CityAttackedStartCallback callback)
	{
		gs.getLoginManager().execCommonRoleVisitor(ownerRoleID, new LoginManager.CommonRoleVisitor() 
		{
			int error = INVOKE_UNKNOWN_ERROR;
			SBean.CityInfo cinfo;

			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleCitys citys = new DBRoleCitys(gs, dbrole, dbrole.citys);
				InvokeRes<SBean.CityInfo> res = citys.ownCityAttackedStartByOther(srcGSID, srcRoleID, baseGSID, baseRoleID, cityID);
				if (res != null)
				{
					error = res.error;
					cinfo = res.info;	
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role) 
			{
				synchronized (role)
				{
					InvokeRes<SBean.CityInfo> res = role.citys.ownCityAttackedStartByOther(srcGSID, srcRoleID, baseGSID, baseRoleID, cityID);
					if (res != null)
					{
						error = res.error;
						cinfo = res.info;	
					}
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode, String rname) 
			{
				if (errcode != 0)
					error = bDB ? INVOKE_VIST_DBROLE_ERROR : INVOKE_VIST_ROLE_ERROR;
				else if (error == INVOKE_SUCCESS && cinfo == null)
					error = INVOKE_UNKNOWN_ERROR;
				if (cinfo == null)
					cinfo = CityManager.makeCityInfo();
				callback.onCallback(error, cinfo);
			}
		});
	}
	
	interface CityAttackedEndCallback 
	{
		public void onCallback(int errcode, SBean.CityInfo info);
	}
	public void cityAttackedEndByOther(final int srcGSID, final int srcRoleID, final String srcRoleName, final int ownerRoleID, 
										 final int baseGSID, final int baseRoleID, final int cityID, final byte win, final CityAttackedEndCallback callback)
	{
		gs.getLoginManager().execCommonRoleVisitor(ownerRoleID, new LoginManager.CommonRoleVisitor() 
		{
			int error = INVOKE_UNKNOWN_ERROR;
			SBean.CityInfo cinfo;
			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleCitys citys = new DBRoleCitys(gs, dbrole, dbrole.citys);
				InvokeRes<SBean.CityInfo> res = citys.ownCityAttackedEndByOther(srcGSID, srcRoleID, srcRoleName, baseGSID, baseRoleID, cityID, win);
				if (res != null)
				{
					error = res.error;
					cinfo = res.info;
					
//					if(res.error==INVOKE_SUCCESS){
//						synchronized (CityManager.this) {
//							attacked.add(dbrole.id);
//						}
//					}					
				}
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role) 
			{
				synchronized (role)
				{
					InvokeRes<SBean.CityInfo> res = role.citys.ownCityAttackedEndByOther(srcGSID, srcRoleID, srcRoleName, baseGSID, baseRoleID, cityID, win);
					if (res != null)
					{
						error = res.error;
						cinfo = res.info;	
						
						if(res.error==INVOKE_SUCCESS){
//							synchronized (CityManager.this) {
//								attacked.add(role.id);
//							}
							int sid = gs.getLoginManager().getLoginRoleSessionID(role.id);
							if(sid>0){
								gs.getRPCManager().sendLuaPacket(sid, LuaPacket.encodeCitysNotice(true));
							}
						}
					}
									
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode, String rname) 
			{
				if (errcode != 0)
					error = bDB ? INVOKE_VIST_DBROLE_ERROR : INVOKE_VIST_ROLE_ERROR;
				else if (error == INVOKE_SUCCESS && cinfo == null)
					error = INVOKE_UNKNOWN_ERROR;
				if (cinfo == null)
					cinfo = CityManager.makeCityInfo();
				callback.onCallback(error, cinfo);
			}
		});
	}
	

	public interface CityOwnerQueriedCallback 
	{
		public void onCallback(int errcode, SBean.CityInfo cinfo);
	}

	public void cityOwnerQueriedByOther(final int srcGSID, final int srcRoleID, final int ownerRoleID,  final int baseGSID, final int baseRoleID, final int cityID, final CityOwnerQueriedCallback callback) 
	{
		gs.getLoginManager().execCommonRoleVisitor(ownerRoleID, new LoginManager.CommonRoleVisitor() 
		{
					int error = INVOKE_UNKNOWN_ERROR;
					SBean.CityInfo cinfo;

					@Override
					public byte visit(DBRole dbrole) 
					{
						DBRoleCitys citys = new DBRoleCitys(gs, dbrole, dbrole.citys);
						InvokeRes<SBean.CityInfo> res = citys.getOwnCityInfo(baseGSID, baseRoleID, cityID);
						if (res != null)
						{
							error = res.error;
							cinfo = res.info;	
						}
						return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
					}

					@Override
					public boolean visit(Role role) 
					{
						synchronized (role)
						{
							InvokeRes<SBean.CityInfo> res = role.citys.getOwnCityInfo(baseGSID, baseRoleID, cityID);
							if (res != null)
							{
								error = res.error;
								cinfo = res.info;	
							}
						}
						return true;
					}

					@Override
					public void onCallback(boolean bDB, byte errcode, String rname) 
					{
						if (errcode != 0)
							error = bDB ? INVOKE_VIST_DBROLE_ERROR : INVOKE_VIST_ROLE_ERROR;
						else if (error == INVOKE_SUCCESS && cinfo == null)
							error = INVOKE_UNKNOWN_ERROR;
						if (cinfo == null)
							cinfo = CityManager.makeCityInfo();
						callback.onCallback(error, cinfo);
					}
				});
	}

	
	interface BaseCityOwnerChangedCallback 
	{
		public void onCallback(int errcode);
	}
	public void baseCityOwnerChanged(final int ownerGSID, final int ownerRoleID, final String ownerRoleName,
									 final int baseGSID, final int baseRoleID, final int cityID, final BaseCityOwnerChangedCallback callback)
	{
		gs.getLoginManager().execCommonRoleVisitor(baseRoleID, new LoginManager.CommonRoleVisitor() 
		{
			@Override
			public byte visit(DBRole dbrole)
			{
				DBRoleCitys citys = new DBRoleCitys(gs, dbrole, dbrole.citys);
				citys.baseCityOwnerChanged(ownerGSID, ownerRoleID, ownerRoleName, cityID);	
				return LoginManager.CommonRoleVisitor.ERR_DB_OK_SAVE;
			}

			@Override
			public boolean visit(Role role) 
			{
				synchronized (role)
				{
					role.citys.baseCityOwnerChanged(ownerGSID, ownerRoleID, ownerRoleName, cityID);
				}
				return true;
			}

			@Override
			public void onCallback(boolean bDB, byte errcode, String rname) 
			{
				callback.onCallback(errcode);
			}
		});
	}
///////////////////////////////////////////////////////////////////////////////////////////////
	static class CityEventService 
	{
		public CityEventService(GameServer gs) 
		{
			this.gs = gs;
			this.tag = gs.getTime();
		}

		public int getTag() 
		{
			return tag;
		}

		public int getNextID() 
		{
			return eventID.incrementAndGet();
		}

		public void onTaskTimer() 
		{
			List<EventTask> timeoutTasks = removeAndGetTimeoutTasks(System.currentTimeMillis());
			for (EventTask task : timeoutTasks) 
			{
				task.onCallback(task.makeDefaultResponse(INVOKE_TIMEOUT));
				gs.getLogger().debug("city event task timeout remove callback");
			}
		}

		private synchronized void addEventTask(EventTask task) 
		{
			if (task.getTag() == this.tag)
				tasks.put(task.getID(), task);
		}

		private synchronized EventTask removeAndGetTask(int id) 
		{
			return tasks.remove(id);
		}

		private synchronized List<EventTask> removeAndGetTimeoutTasks(long now) 
		{
			List<EventTask> timeoutTasks = new ArrayList<EventTask>();
			Iterator<Map.Entry<Integer, EventTask>> it = tasks.entrySet().iterator();
			while (it.hasNext()) 
			{
				EventTask si = it.next().getValue();
				if (si.isTooOld(now)) 
				{
					timeoutTasks.add(si);
					it.remove();
				}
			}
			return timeoutTasks;
		}

		public void excuteEventTaskWaitResponse(EventTask task) 
		{
			addEventTask(task);
			task.doTask();
		}
		
		public void excuteEventTask(EventTask task) 
		{
			task.doTask();
		}

		private void onCallback(int tag, int id, Object obj) 
		{
			if (tag == this.tag) 
			{
				EventTask task = removeAndGetTask(id);
				if (task != null) 
				{
					task.onCallback(obj);
				} 
				else 
				{
					gs.getLogger().warn("city event task callback tag=" + tag + ", id=" + id + "==>but timeout");
				}
			}
		}

		GameServer gs;
		int tag;
		AtomicInteger eventID = new AtomicInteger();
		Map<Integer, EventTask> tasks = new HashMap<Integer, EventTask>();
	}

	abstract class EventTask
	{
		long callTime;

		public EventTask() 
		{
			this.callTime = System.currentTimeMillis();
		}

		public boolean isTooOld(long now) 
		{
			return callTime + MAX_WAIT_TIME <= now;
		}

		abstract public int getTag();
		abstract public int getID();
		abstract public void setTag(int tag);
		abstract public void setID(int id);
		abstract public void doTask();
		abstract public void onCallback(Object res);
		abstract public Object makeDefaultResponse(int error);
	}

	public void excuteEventTask(EventTask task) 
	{
		service.excuteEventTask(task);
	}
	
	public void excuteEventTaskWaitResponse(EventTask task)
	{
		service.excuteEventTaskWaitResponse(task);
	}

	public void handleEventTaskResult(int tag, int id, Object obj) 
	{
		service.onCallback(tag, id, obj);
	}

	class SearchCityTask extends EventTask 
	{
		SBean.SearchCityReq req;

		SearchCityTask(SBean.SearchCityReq req) 
		{
			this.req = req;
			setTag(service.getTag());
			setID(service.getNextID());
		}
		
		public int getTag()
		{
			return req.tag;
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setTag(int tag)
		{
			req.tag = tag;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendSearchCityReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.SearchCityRes res = (SBean.SearchCityRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null) 
			{
				role.handleSearchCityResult(res);
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return CityManager.makeResponse(error, CityManager.makeCityInfo(), req);
		}
	}

	public void searchCity(SBean.SearchCityReq req) 
	{
		excuteEventTaskWaitResponse(new SearchCityTask(req));
	}

	class AttackCityStartTask extends EventTask  
	{
		SBean.AttackCityStartReq req;

		AttackCityStartTask(SBean.AttackCityStartReq req) 
		{
			this.req = req;
			setTag(service.getTag());
			setID(service.getNextID());
		}
		
		public int getTag()
		{
			return req.tag;
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setTag(int tag)
		{
			req.tag = tag;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendAttackCityStartReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.AttackCityStartRes res = (SBean.AttackCityStartRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null) 
			{
				role.handleAttackCityStartResult(res);
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return CityManager.makeResponse(error, CityManager.makeCityInfo(), req);
		}
		
	}

	public void attackCityStart(SBean.AttackCityStartReq req) 
	{
		excuteEventTaskWaitResponse(new AttackCityStartTask(req));
	}

	class AttackCityEndTask extends EventTask  
	{
		SBean.AttackCityEndReq req;

		AttackCityEndTask(SBean.AttackCityEndReq req) 
		{
			this.req = req;
			setTag(service.getTag());
			setID(service.getNextID());
		}

		public int getTag()
		{
			return req.tag;
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setTag(int tag)
		{
			req.tag = tag;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendAttackCityEndReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.AttackCityEndRes res = (SBean.AttackCityEndRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null) 
			{
				role.handleAttackCityEndResult(res);
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return CityManager.makeResponse(error, CityManager.makeCityInfo(), req);
		}
	}

	public void attackCityEnd(SBean.AttackCityEndReq req) 
	{
		excuteEventTaskWaitResponse(new AttackCityEndTask(req));
	}
	
	class QueryCityOwnerTask extends EventTask  
	{
		SBean.QueryCityOwnerReq req;

		QueryCityOwnerTask(SBean.QueryCityOwnerReq req) 
		{
			this.req = req;
			setTag(service.getTag());
			setID(service.getNextID());
		}

		public int getTag()
		{
			return req.tag;
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setTag(int tag)
		{
			req.tag = tag;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendQueryCityOwnerReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			SBean.QueryCityOwnerRes res = (SBean.QueryCityOwnerRes) obj;
			int roleId = res.roleID.roleID;
			Role role = gs.getLoginManager().getRole(roleId);
			if (role != null) 
			{
				role.handleQueryCityOwnerResult(res);
			}
		}
		
		public Object makeDefaultResponse(int error)
		{
			return CityManager.makeResponse(error, makeCityInfo(), req);
		}
	}
	
	public void queryCityOwner(SBean.QueryCityOwnerReq req) 
	{
		excuteEventTaskWaitResponse(new QueryCityOwnerTask(req));
	}
	
	class NotifyCityOwnerChangedTask extends EventTask  
	{
		SBean.NotifyCityOwnerChangedReq req;

		NotifyCityOwnerChangedTask(SBean.NotifyCityOwnerChangedReq req) 
		{
			this.req = req;
			setTag(service.getTag());
			setID(service.getNextID());
		}

		public int getTag()
		{
			return req.tag;
		}
		
		public int getID()
		{
			return req.id;
		}
		
		public void setTag(int tag)
		{
			req.tag = tag;
		}
		
		public void setID(int id)
		{
			req.id = id;
		}
		
		public void doTask() 
		{
			gs.getRPCManager().exchangeSendNotifyCityOwnerChangedReq(req);
		}
		
		public void onCallback(Object obj) 
		{
			
		}
		
		public Object makeDefaultResponse(int error)
		{
			return null;
		}
	}
	
	public void notifyCityOwnerChanged(SBean.NotifyCityOwnerChangedReq req)
	{
		excuteEventTask(new NotifyCityOwnerChangedTask(req));
	}

	public void handleSearchCityResult(SBean.SearchCityRes res) 
	{
		handleEventTaskResult(res.tag, res.id, res);
	}

	public void handleAttackCityStart(SBean.AttackCityStartRes res) 
	{
		handleEventTaskResult(res.tag, res.id, res);
	}

	public void handleAttackCityEnd(SBean.AttackCityEndRes res) 
	{
		handleEventTaskResult(res.tag, res.id, res);
	}

	public void handleQueryCityOwner(SBean.QueryCityOwnerRes res) 
	{
		handleEventTaskResult(res.tag, res.id, res);
	}

	public static SBean.SearchCityRes makeResponse(int error, SBean.CityInfo cinfo, SBean.SearchCityReq req)
	{
		SBean.SearchCityRes res = new SBean.SearchCityRes(req.tag, req.id, error, req.roleID, cinfo);
		return res;
	}
	
	public static SBean.AttackCityStartRes makeResponse(int error, SBean.CityInfo cinfo, SBean.AttackCityStartReq req)
	{
		SBean.AttackCityStartRes res = new SBean.AttackCityStartRes();
		res.tag = req.tag;
		res.id = req.id;
		res.error = error;
		res.roleID = req.roleID;
		res.cityInfo = cinfo;
		return res;
	}
	
	public static SBean.AttackCityEndRes makeResponse(int error, SBean.CityInfo cinfo, SBean.AttackCityEndReq req)
	{
		SBean.AttackCityEndRes res = new SBean.AttackCityEndRes();
		res.tag = req.tag;
		res.id = req.id;
		res.error = error;
		res.roleID = req.roleID;
		res.cityInfo = cinfo;
		return res;
	}
	
	public static SBean.QueryCityOwnerRes makeResponse(int error, SBean.CityInfo cinfo, SBean.QueryCityOwnerReq req)
	{
		SBean.QueryCityOwnerRes res = new SBean.QueryCityOwnerRes(req.tag, req.id, error, req.roleID, cinfo);
		return res;
	}
	
	public static SBean.CityInfo makeCityInfo()
	{
		SBean.CityInfo cinfo = new SBean.CityInfo();
		cinfo.baseRoleID = new SBean.GlobalRoleID(0, 0);
		cinfo.baseRoleName = "";
		cinfo.cityID = 0;
		cinfo.ownerRoleID = new SBean.GlobalRoleID(0, 0);
		cinfo.ownerRoleName = "";
		cinfo.guardStartTime = 0;
		cinfo.guardLifeTime = 0;
		cinfo.guardGenerals = new ArrayList<DBRoleGeneral>();
		cinfo.guardPets = new ArrayList<SBean.DBPetBrief>();
		return cinfo;
	}
	
	public static String toBriefString(SBean.SearchCityReq req)
	{
		return "tagID=[" + req.tag + ", " + req.id + "], reqRole=[" + req.roleID.serverID + "," + req.roleID.roleID + "], level = " + req.level + " , cityIDMax = " + req.cityIDMax + ",  searchType = " + req.searchType;
	}
	
	public static String toBriefString(SBean.SearchCityRes res)
	{
		return "tagID=[" + res.tag + ", " + res.id + "], reqRole=[" + res.roleID.serverID + "," + res.roleID.roleID + "], error=" + res.error + ", ownerRole=[" + res.cityInfo.ownerRoleID.serverID + "," + res.cityInfo.ownerRoleID.roleID + "], city=[" + res.cityInfo.baseRoleID.serverID + "," + res.cityInfo.baseRoleID.roleID + "," + res.cityInfo.cityID + "]";
	}
	
	public static String toBriefString(SBean.AttackCityStartReq req)
	{
		return "tagID=[" + req.tag + ", " + req.id + "], reqRole=[" + req.roleID.serverID + "," + req.roleID.roleID + "], toRole=[" + req.targetRoleID.serverID + "," + req.targetRoleID.roleID + "], city=[" + req.baseRoleID.serverID + "," + req.baseRoleID.roleID + "," + req.cityID + "]";
	}
	
	public static String toBriefString(SBean.AttackCityStartRes res)
	{
		return "tagID=[" + res.tag + ", " + res.id + "], reqRole=[" + res.roleID.serverID + "," + res.roleID.roleID + "], error=" + res.error + ", ownerRole=[" + res.cityInfo.ownerRoleID.serverID + "," + res.cityInfo.ownerRoleID.roleID + "], city=[" + res.cityInfo.baseRoleID.serverID + "," + res.cityInfo.baseRoleID.roleID + "," + res.cityInfo.cityID + "]";
	}
	
	public static String toBriefString(SBean.AttackCityEndReq req)
	{
		return "tagID=[" + req.tag + ", " + req.id + "], reqRole=[" + req.roleID.serverID + "," + req.roleID.roleID + "], toRole=[" + req.targetRoleID.serverID + "," + req.targetRoleID.roleID + "], city=[" + req.baseRoleID.serverID + "," + req.baseRoleID.roleID + "," + req.cityID + "], win=" + req.win;
	}
	
	public static String toBriefString(SBean.AttackCityEndRes res)
	{
		return "tagID=[" + res.tag + ", " + res.id + "], reqRole=[" + res.roleID.serverID + "," + res.roleID.roleID + "], error=" + res.error + ", ownerRole=[" + res.cityInfo.ownerRoleID.serverID + "," + res.cityInfo.ownerRoleID.roleID + "], city=[" + res.cityInfo.baseRoleID.serverID + "," + res.cityInfo.baseRoleID.roleID + "," + res.cityInfo.cityID + "]";
	}
	
	public static String toBriefString(SBean.QueryCityOwnerReq req)
	{
		return "tagID=[" + req.tag + ", " + req.id + "], reqRole=[" + req.roleID.serverID + "," + req.roleID.roleID + "], toRole=[" + req.targetRoleID.serverID + "," + req.targetRoleID.roleID + "], city=[" + req.baseRoleID.serverID + "," + req.baseRoleID.roleID + "," + req.cityID + "]";
	}
	
	public static String toBriefString(SBean.QueryCityOwnerRes res)
	{
		return "tagID=[" + res.tag + ", " + res.id + "], reqRole=[" + res.roleID.serverID + "," + res.roleID.roleID + "], error=" + res.error + ", ownerRole=[" + res.cityInfo.ownerRoleID.serverID + "," + res.cityInfo.ownerRoleID.roleID + "], city=[" + res.cityInfo.baseRoleID.serverID + "," + res.cityInfo.baseRoleID.roleID + "," + res.cityInfo.cityID + "]";
	}
	
	public static String toBriefString(SBean.NotifyCityOwnerChangedReq req)
	{
		return "tagID=[" + req.tag + ", " + req.id + "], reqRole=[" + req.roleID.serverID + "," + req.roleID.roleID + "], city=[" + req.baseRoleID.serverID + "," + req.baseRoleID.roleID + "," + req.cityID + "]";
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class CityBrief
	{
		public int baseGSID;
		public int baseRoleID;
		public String baseRoleName;
		public int cityID;
		public int ownerGSID;
		public int ownerRoleID;
		public String ownerRoleName; 
		public int guardStartTime;
		public int guardLifeTime;
		public List<Short> gids = new ArrayList<Short>();
		public List<Short> pids = new ArrayList<Short>();
		public boolean hasRewards;
	}
	
	public static class CityRewardLog
	{
		public List<SBean.DBCityReward> rewards = new ArrayList<SBean.DBCityReward>();
		public int nTimesStone = 1;
		public int nTimesMoney = 1;
		public int nTimesItem = 1;
	}
	
	public static class CityReward
	{
		public List<SBean.DropEntryNew> rewards = new ArrayList<SBean.DropEntryNew>();
		public int nTimesStone = 1;
		public int nTimesMoney = 1;
		public int nTimesItem = 1;
	}
	
	public static class InvokeRes<T>
	{
		public int error = INVOKE_UNKNOWN_ERROR;
		public T info;
		
		public InvokeRes(int error, T info)
		{
			this.error = error;
			this.info = info;
		}
	}
	
	public static abstract class Citys
	{
		SBean.DBCitys citys;
		public Citys()
		{
			citys = new SBean.DBCitys(new ArrayList<SBean.DBBaseCity>(), new ArrayList<SBean.DBBaseCity>(), new ArrayList<SBean.DBCityReward>(), 0);
		}
		
		public Citys(SBean.DBCitys citys)
		{
			this.citys = citys;
		}
		
		abstract GameServer getGameServer();
		abstract int getServerID();
		abstract int getRoleID();
		abstract String getRoleName();
		abstract int getTime();
		abstract boolean hasGeneral(short gid);
		abstract boolean hasPet(short pid);
		abstract DBRoleGeneral getGeneral(short gid);
		abstract SBean.DBPet getPet(short pid, byte[] deformStage);
		
		boolean isSelfOwnCity(SBean.DBBaseCity city)
		{
			return city.state == 0;
		}
		
		boolean isSelfCity(SBean.DBBaseCity city)
		{
			return city.state == 0 || city.state == 1;
		}
		
		boolean isLostCity(SBean.DBBaseCity city)
		{
			return city.state == 1;
		}
		
		boolean isOtherCity(SBean.DBBaseCity city)
		{
			return city.state == 2;
		}
		
		
		boolean isValidCity(SBean.DBBaseCity city)
		{
			return city.state == 0 || city.state == 1 || city.state == 2;
		}
		
		boolean isOwnCity(SBean.DBBaseCity city)
		{
			return city.state == 0 || city.state == 2;
		}
		
		void resetCityState(SBean.DBBaseCity city)
		{
			if (city.state == 0 || city.state == 1)
				city.state = 0;
			else if (city.state == 2)
				city.state = -1;
		}
		
		void setCityLost(SBean.DBBaseCity city)
		{
			if (city.state == 0)
				city.state = 1;
			else if (city.state == 2)
				city.state = -1;
		}
		
		void setCityBack(SBean.DBBaseCity city)
		{
			if (city.state == 1)
				city.state = 0;
		}
		
		boolean isCityInGuardTime(SBean.DBBaseCity city, int curTime)
		{
			return (city.guardStartTime != 0 && curTime >= city.guardStartTime && curTime < city.guardStartTime + city.guardLifeTime);
		}
		
		boolean isCityOutGuardTime(SBean.DBBaseCity city, int curTime)
		{
			return (city.guardStartTime != 0 && curTime > city.guardStartTime + city.guardLifeTime);
		}
		
		float getCityIncomeRatio(SBean.DBBaseCity city)
		{
			float ratio = 0.0f;
			SBean.CaptureCityCFGS cccfg = GameData.getInstance().getCaptureCityCfg();
			if (city.state == 0)
				ratio = 1.0f;
			else if (city.state == 1)
				ratio = cccfg.lostProduceRatio;
			else
				ratio = cccfg.captureProduceRatio;
			return ratio;
		}
		
		boolean isOpenBaseCity()
		{
			return !citys.baseCitys.isEmpty();
		}
		
		int getBaseCityMax()
		{
			return citys.baseCitys.size();
		}
		
		SBean.DBBaseCity getBaseCity(int cityid)
		{
			if (cityid <= 0 || cityid > citys.baseCitys.size())
				return null;
			return citys.baseCitys.get(cityid-1);
		}
		
		int getOccupiedCityCount()
		{
			return citys.occupiedCitys.size();
		}
		
		int getGuardOccupiedCityCount()
		{
			int count = 0;
			for (SBean.DBBaseCity c : citys.occupiedCitys)
				if (!c.guardGenerals.isEmpty())
					count ++;
			
			return count;
		}
		
		SBean.DBBaseCity getOccupiedCity(int baseGSID, int baseRoleID, int cityid)
		{
			for (SBean.DBBaseCity c : citys.occupiedCitys)
			{
				if (c.baseGSID > 0 && c.baseRoleID > 0 && c.baseGSID == baseGSID && c.baseRoleID == baseRoleID && c.cityID == cityid)
					return c;
			}
			return null;
		}
		
		SBean.DBBaseCity getRobotCity(String baseRoleName, int cityid, int guardStartTime, int guardLifeTime)
		{
			SBean.DBBaseCity city = createOccupiedCity(-1, -1, baseRoleName, cityid, guardStartTime, guardLifeTime);
			citys.occupiedCitys.add(city);
			return city;
		}
		
		void occupyCity(int baseGSID, int baseRoleID, String baseRoleName, int cityid, int guardStartTime, int guardLifeTime)
		{
			for (SBean.DBBaseCity c : citys.occupiedCitys)
			{
				if (c.baseGSID > 0 && c.baseRoleID > 0 && c.baseGSID == baseGSID && c.baseRoleID == baseRoleID && c.cityID == cityid)
					return;
			}
			SBean.DBBaseCity city = createOccupiedCity(baseGSID, baseRoleID, baseRoleName, cityid, guardStartTime, guardLifeTime);
			citys.occupiedCitys.add(city);
		}
		
		private void resetBaseCity(SBean.DBBaseCity city)
		{
			resetCityState(city);
			if (isSelfCity(city))
			{
				SBean.CityCFGS ccfg = GameData.getInstance().getCityCfg(city.cityID);
				city.ownerGSID = city.baseGSID;
				city.ownerRoleID = city.baseRoleID;
				city.ownerRoleName = city.baseRoleName;
				city.guardStartTime = 0;
				city.guardLifeTime = 0;
				city.lastBalanceTime = 0;
				city.guardGenerals.clear();
				city.guardPets.clear();
				city.guardTimeLog.clear();
				int plvl = ccfg.produce.get(0).produce.size()-1;
				for (int i = 0; i < plvl; ++i)
				{
					city.guardTimeLog.add(0);
				}
				city.attackGSID = 0;
				city.attackRoleID = 0;
				city.attackTime = 0;	
			}
		}
		
		private SBean.DBBaseCity createOccupiedCity(int baseGSID, int baseRoleID, String baseRoleName, int cityid, int guardStartTime, int guardLifeTime)
		{
			SBean.DBBaseCity city = new SBean.DBBaseCity();
			city.baseGSID = baseGSID;
			city.baseRoleID = baseRoleID;
			city.baseRoleName = baseRoleName;
			city.cityID = cityid;
			city.openTime = this.getTime();
			city.guardGenerals = new ArrayList<Short>();
			city.guardPets = new ArrayList<Short>();
			city.guardTimeLog = new ArrayList<Integer>();
			city.totalIncome = new ArrayList<SBean.DropEntryNew>();
			
			
			city.ownerGSID = this.getServerID();
			city.ownerRoleID = this.getRoleID();
			city.ownerRoleName = this.getRoleName();
			city.guardStartTime = guardStartTime;
			city.guardLifeTime = guardLifeTime;
			city.lastBalanceTime = this.getTime();
			city.guardGenerals.clear();
			city.guardPets.clear();
			city.guardTimeLog.clear();
			SBean.CityCFGS ccfg = GameData.getInstance().getCityCfg(city.cityID);
			int plvl = ccfg.produce.get(0).produce.size()-1;
			for (int i = 0; i < plvl; ++i)
			{
				city.guardTimeLog.add(0);
			}
			city.state = 2;
			city.attackGSID = 0;
			city.attackRoleID = 0;
			city.attackTime = 0;
			return city;
		}
		
		void syncBaseCity(SBean.DBBaseCity city, int curTime)
		{
			if (isCityOutGuardTime(city, curTime))
			{
				balanceIncome(city, curTime);
				SBean.CityCFGS citycfg = GameData.getInstance().getCityCfg(city.cityID);
				for (SBean.CityProduceCFGS cp : citycfg.produce)
				{
					float count = 0;
					for (int i = 0; i < city.guardTimeLog.size(); ++i)
					{
						if (i+1 < cp.produce.size())
						{
							int cityGuardTime = city.guardTimeLog.get(i);
							int countPerHour = cp.produce.get(i+1);
							count += countPerHour*cityGuardTime/3600.0f;	
						}
					}
					addIncome(cp.type, cp.id, (int)count, city.totalIncome);
				}
				resetBaseCity(city);
			}
		}
		
		void syncOccupiedCity(SBean.DBBaseCity city, int curTime)
		{
			syncBaseCity(city, curTime);
			clearOccuoiedCity(city);
		}
		
		void clearOccuoiedCity(SBean.DBBaseCity city)
		{
			if (!isValidCity(city))
			{
				if (!city.totalIncome.isEmpty()) 
				{
					SBean.DBCityReward reward = getRewardLog(city);
					citys.occupiedCitysRewardLog.add(reward);
				}
				citys.occupiedCitys.remove(city);
			}
		}
		
		SBean.DBCityReward getRewardLog(SBean.DBBaseCity city) 
		{
			SBean.DBCityReward reward = new SBean.DBCityReward();
			reward.baseGSID = city.baseGSID;
			reward.baseRoleID = city.baseRoleID;
			reward.baseRoleName = city.baseRoleName;
			reward.cityID = city.cityID;
			reward.guardStartTime = city.guardStartTime;
			reward.guardLifeTime = city.guardLifeTime;
			reward.totalIncome = city.totalIncome;
			return reward;
		}
		
		void syncCitys()
		{
			int curTime = this.getTime();
			syncBaseCitys(curTime);
			syncOccupyCitys(curTime);
			CityManager.getInstance().attacked.remove(this.getRoleID());
		}
		
		void syncBaseCitys(int curTime)
		{
			for (SBean.DBBaseCity city : citys.baseCitys)
			{
				syncBaseCity(city, curTime);
			}
			balanceBaseRewards();
		}
		
		void syncOccupyCitys(int curTime)
		{
			List<SBean.DBBaseCity> occupiedCitys = new ArrayList<SBean.DBBaseCity>();
			occupiedCitys.addAll(citys.occupiedCitys);
			for (SBean.DBBaseCity city : occupiedCitys)
			{
				syncOccupiedCity(city, curTime);
			}
			Iterator<SBean.DBCityReward> it = citys.occupiedCitysRewardLog.iterator();
			while (it.hasNext())
			{
				SBean.DBCityReward r = it.next();
				if (r.guardStartTime + r.guardLifeTime + 7*24*3600 < curTime)
				{
					it.remove();
				}
			}
		}
		
		void changeBaseCitysName(String name)
		{
			for (SBean.DBBaseCity city : citys.baseCitys)
			{
				city.baseRoleName = name;
			}
		}
		
		void balanceIncome(SBean.DBBaseCity city, int curTime)
		{
			if (city.guardStartTime != 0 && !city.guardGenerals.isEmpty())
			{
				int balanceTime = city.guardStartTime + city.guardLifeTime;
				if (curTime < balanceTime)
					balanceTime = curTime;
				int ngeneralCount = city.guardGenerals.size();
				if (ngeneralCount > 0 && ngeneralCount <= city.guardTimeLog.size())
				{
					int ngeneralTime = balanceTime - city.lastBalanceTime;
					if (ngeneralTime > 0)
					{
						int cityGuardTime = city.guardTimeLog.get(ngeneralCount-1);
						float ratio = getCityIncomeRatio(city);
						cityGuardTime += (int)(ratio * ngeneralTime);
						city.guardTimeLog.set(ngeneralCount-1, cityGuardTime);
					}
				}	
			}
			city.lastBalanceTime = curTime;
		}
		
		void balanceOccupiedCityReward(SBean.DBBaseCity city, int curTime)
		{
			balanceIncome(city, curTime);
			SBean.CityCFGS citycfg = GameData.getInstance().getCityCfg(city.cityID);
			for (SBean.CityProduceCFGS cp : citycfg.produce)
			{
				float count = 0;
				for (int i = 0; i < city.guardTimeLog.size(); ++i)
				{
					if (i+1 < cp.produce.size())
					{
						int cityGuardTime = city.guardTimeLog.get(i);
						int countPerHour = cp.produce.get(i+1);
						count += countPerHour*cityGuardTime/3600.0f;	
					}
				}
				addIncome(cp.type, cp.id, (int)count, city.totalIncome);
			}
		}
		
		void balanceBaseReward(SBean.DBBaseCity city, int curTime, int lastBaseRewardBalanceTime, int curRewardBalanceTime)
		{
			int lastRewardTime = city.openTime > lastBaseRewardBalanceTime ? city.openTime : lastBaseRewardBalanceTime;
			float rewards = (curRewardBalanceTime - lastRewardTime)/3600.0f;
			if (rewards > 7*24)
				rewards = 7*24;
			this.getGameServer().getLogger().debug("balanceBaseReward city="+city.cityID+", rewards="+rewards);
			SBean.CityCFGS citycfg = GameData.getInstance().getCityCfg(city.cityID);
			for (SBean.CityProduceCFGS cp : citycfg.produce)
			{
				int countPerHour = cp.produce.get(0);
				float count = countPerHour * rewards;
				addIncome(cp.type, cp.id, (int)count, city.totalIncome);
			}
		}
		
		void balanceBaseRewards()
		{
			if (citys.baseCitys.isEmpty() || citys.lastBaseRewardTime == 0)
				return;
			int curTime = this.getTime();
			SBean.CaptureCityCFGS cccfg = GameData.getInstance().getCaptureCityCfg();
			int rewardBalanceDayTime = cccfg.baseRewardTime*60;
			int lastBaseRewardDay = citys.lastBaseRewardTime/86400;
			if (citys.lastBaseRewardTime%86400 < rewardBalanceDayTime)
				lastBaseRewardDay--; 
			int lastBaseRewardBalanceTime = lastBaseRewardDay * 86400  + rewardBalanceDayTime;
			int nextRewardTime = lastBaseRewardBalanceTime + 86400;
			getGameServer().getLogger().debug("balanceBaseRewards : curTime="+GameServer.getTimeStampStr(curTime)+", lastBaseRewardBalanceTime="+GameServer.getTimeStampStr(lastBaseRewardBalanceTime)
					                         +", lastBaseRewardTime="+GameServer.getTimeStampStr(citys.lastBaseRewardTime)+", nextRewardTime="+GameServer.getTimeStampStr(nextRewardTime));
			if (curTime >= nextRewardTime)
			{
				//this.getGameServer().getLogger().info("balanceBaseRewards @@@@@@@@@@@@");
				int curDayRewardBalanceTime = (curTime/86400)*86400 + rewardBalanceDayTime;
				int curRewardBalanceTime = curDayRewardBalanceTime > curTime ? curDayRewardBalanceTime-86400 : curDayRewardBalanceTime;
				for (SBean.DBBaseCity city : citys.baseCitys)
				{
					balanceBaseReward(city, curTime, lastBaseRewardBalanceTime, curRewardBalanceTime);
				}
				citys.lastBaseRewardTime = curTime;
			}
		}
		
//		List<Integer> getBaseCitysSyncInfo()
//		{
//			List<Integer> lst = new ArrayList<Integer>();
//			int curTime = this.getTime();
//			for (SBean.DBBaseCity city : citys.baseCitys)
//			{
//				syncBaseCity(city, curTime);
//				lst.add(city.state);
//			}
//			return lst;
//		}
		
		CityBrief toCityBrief(SBean.DBBaseCity city)
		{
			CityBrief cb = new CityBrief();
			cb.baseGSID = city.baseGSID;
			cb.baseRoleID = city.baseRoleID;
			cb.baseRoleName = city.baseRoleName;
			cb.cityID = city.cityID;
			cb.ownerGSID = city.ownerGSID;
			cb.ownerRoleID = city.ownerRoleID;
			cb.ownerRoleName = city.ownerRoleName;
			cb.guardStartTime = city.guardStartTime;
			cb.guardLifeTime = city.guardLifeTime;
			cb.gids = new ArrayList<Short>(city.guardGenerals);
			cb.pids = new ArrayList<Short>(city.guardPets);
			cb.hasRewards = !city.totalIncome.isEmpty();
			return cb;
		}
		
		List<CityBrief> getBaseCitysBrief()
		{
			List<CityBrief> lst = new ArrayList<CityBrief>();
			int curTime = this.getTime();
			for (SBean.DBBaseCity city : citys.baseCitys)
			{
				syncBaseCity(city, curTime);
				lst.add(toCityBrief(city));
			}
			return lst;
		}
		
		List<CityBrief> getOccupiedCitysBrief()
		{
			List<CityBrief> lst = new ArrayList<CityBrief>();
			int curTime = this.getTime();
			List<SBean.DBBaseCity> occupiedCitys = new ArrayList<SBean.DBBaseCity>();
			occupiedCitys.addAll(citys.occupiedCitys);
			for (SBean.DBBaseCity city : occupiedCitys)
			{
				syncOccupiedCity(city, curTime);
				if (isValidCity(city))
					lst.add(toCityBrief(city));
			}
			return lst;
		}
		
		InvokeRes<SBean.CityInfo> getBaseCityInfo(int cityId)
		{
			SBean.DBBaseCity city = getBaseCity(cityId);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			syncBaseCity(city, this.getTime());
			SBean.CityInfo cinfo = toCityInfo(city);
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, cinfo);
		}
		
		InvokeRes<SBean.CityInfo> getOccupiedCityInfo(int baseGSID, int baseRoleID, int cityID)
		{
			SBean.DBBaseCity city = getOccupiedCity(baseGSID, baseRoleID, cityID);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			syncOccupiedCity(city, this.getTime());
			if (!isValidCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OWN_CITY_ERROR, null);
			SBean.CityInfo cinfo = toCityInfo(city);
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, cinfo);
		}
		
		InvokeRes<SBean.CityInfo> getOwnCityInfo(int baseGSID, int baseRoleID, int cityID)
		{
			if (this.getServerID() == baseGSID && this.getRoleID() == baseRoleID)
				return getBaseCityInfo(cityID);
			else
				return getOccupiedCityInfo(baseGSID, baseRoleID, cityID);
		}
		
		int guardBaseCity(int cityId, int guardLifeTimeType, List<Short> gids, List<Short> pids)
		{
			SBean.DBBaseCity city = getBaseCity(cityId);
			if (city == null)
				return INVOKE_NOT_FIND_CITY_ERROR;
			if (guardLifeTimeType == 0)
			{
				return updateGuardCityGenerals(city, gids, pids);
			}
			else
			{
				SBean.CaptureCityCFGS cccfg = GameData.getInstance().getCaptureCityCfg();
				if (guardLifeTimeType <= 0 || guardLifeTimeType > cccfg.guardTimes.size())
					return INVOKE_SET_CITY_GUARD_TIME_ERROR;
				int guardLifeTime = cccfg.guardTimes.get(guardLifeTimeType-1);
				return startGuardCity(city, guardLifeTime, gids, pids);
			}
			//this.getGameServer().getLogger().info("guardBaseCity(" + cityId + ", "  + guardLifeTimeType + ", "  + gids.toString() + ", "  + pids.toString() + ") ==> " + bOk);
		}
		
		int guardOccpiedCity(int baseGSID, int baseRoleID, int cityId, List<Short> gids, List<Short> pids)
		{
			SBean.DBBaseCity city = getOccupiedCity(baseGSID, baseRoleID, cityId);
			if (city == null)
				return INVOKE_NOT_FIND_CITY_ERROR;
			return updateGuardCityGenerals(city, gids, pids);
		}
		
		int guardRobotCity(String baseRoleName, int cityid, int guardStartTime, int guardLifeTime, List<Short> gids, List<Short> pids)
		{
			SBean.DBBaseCity city = getRobotCity(baseRoleName, cityid, guardStartTime, guardLifeTime);
			if (city == null)
				return INVOKE_NOT_FIND_CITY_ERROR;
			return updateGuardCityGenerals(city, gids, pids);
		}
		
		private int startGuardCity(SBean.DBBaseCity city, int lifeTime, List<Short> gids, List<Short> pids)
		{
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (isCityInGuardTime(city, curTime))
				return INVOKE_CITY_ALREADY_IN_GUARD_TIME_ERROR;
			if (!isSelfOwnCity(city))
				return INVOKE_NOT_SELF_CITY_ERROR;
			if (!setCityGenerals(city, gids, pids, curTime))
				return INVOKE_SET_CITY_GUARD_GENERAL_ERROR;
			city.guardStartTime = curTime;
			city.guardLifeTime = lifeTime;
			return INVOKE_SUCCESS;
		}
		
		private int updateGuardCityGenerals(SBean.DBBaseCity city, List<Short> gids, List<Short> pids)
		{
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (!isCityInGuardTime(city, curTime))
				return INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR;
			if (!isOwnCity(city))
				return INVOKE_NOT_OWN_CITY_ERROR;
			boolean notSetGenerals = city.guardGenerals.isEmpty(); 
			if (!setCityGenerals(city, gids, pids, curTime))
				return INVOKE_SET_CITY_GUARD_GENERAL_ERROR;
			if (isOtherCity(city) && notSetGenerals)
			{
				int leftGuardLifeTime = city.guardLifeTime - (curTime - city.guardStartTime);
				city.guardStartTime = curTime;
				city.guardLifeTime = leftGuardLifeTime;
			}
			return INVOKE_SUCCESS;
		}
		
		private boolean setCityGenerals(SBean.DBBaseCity city, List<Short> gids, List<Short> pids, int curTime)
		{
			SBean.CityCFGS ccfg = GameData.getInstance().getCityCfg(city.cityID);	
			if (gids.isEmpty() || gids.size() > ccfg.produce.get(0).produce.size()-1)
				return false;
			Set<Short> checkSet = new TreeSet<Short>(gids);
			if (checkSet.size() != gids.size())
				return false;
			for (short gid : gids)
			{
				if (!this.hasGeneral(gid))
					return false;
				List<SBean.DBBaseCity> citylst = isSelfCity(city) ? citys.baseCitys : citys.occupiedCitys;
				for (SBean.DBBaseCity c : citylst)
				{
					if (c != city)
					{
						if (c.guardGenerals.contains(gid))
							return false;
					}
				}
			}
			if (pids.size() != 0 && pids.size() != 1)
				return false;
			for (short pid : pids)
			{
				if (!this.hasPet(pid))
					return false;
				List<SBean.DBBaseCity> citylst = isSelfCity(city) ? citys.baseCitys : citys.occupiedCitys;
				for (SBean.DBBaseCity c : citylst)
				{
					if (c != city)
					{
						if (c.guardPets.contains(pid))
							return false;
					}
				}
			}
			balanceIncome(city, curTime);
			city.guardGenerals.clear();
			city.guardGenerals.addAll(gids);
			city.guardPets.clear();
			city.guardPets.addAll(pids);
			return true;
		}
		
		private void addIncome(byte type, short id, int count, List<SBean.DropEntryNew> income)
		{
			if (count > 0)
			{
				SBean.DropEntryNew den = null;
				for (SBean.DropEntryNew e : income)
				{
					if (e.type == type && e.id == id)
					{
						den = e;
						break;
					}
				}
				if (den == null)
				{
					income.add(new SBean.DropEntryNew(type, id, count));
				}
				else
				{
					den.arg += count;
				}	
			}
		}
				
		private SBean.CityInfo getSearchCityInfo(int cityid)
		{
			SBean.DBBaseCity city = getBaseCity(cityid);
			if (city == null)
				return null;
			getGameServer().getLogger().debug("getSearchCityInfo : find city id="+cityid);
			return toCityInfo(city);
		}
		
		SBean.CityInfo toCityInfo(SBean.DBBaseCity city)
		{
			SBean.CityInfo cinfo = new SBean.CityInfo();
			cinfo.baseRoleID = new SBean.GlobalRoleID(city.baseGSID, city.baseRoleID);
			cinfo.baseRoleName = city.baseRoleName;
			cinfo.cityID = city.cityID;
			cinfo.ownerRoleID = new SBean.GlobalRoleID(city.ownerGSID, city.ownerRoleID);
			cinfo.ownerRoleName = city.ownerRoleName;
			cinfo.guardStartTime = city.guardStartTime;
			cinfo.guardLifeTime = city.guardLifeTime;
			cinfo.guardGenerals = new ArrayList<DBRoleGeneral>();
			cinfo.guardPets = new ArrayList<SBean.DBPetBrief>();
			for (short gid : city.guardGenerals)
			{
				DBRoleGeneral g = this.getGeneral(gid);
				if (g != null)
				{
					cinfo.guardGenerals.add(g.kdClone());
				}
			}
			for (short pid : city.guardPets)
			{
				byte[] deformStage = {0};
				SBean.DBPet p = this.getPet(pid, deformStage);
				if (p != null)
				{
					cinfo.guardPets.add(new SBean.DBPetBrief((byte)p.id, p.awakeLvl, p.lvl, p.evoLvl, (byte)p.growthRate, deformStage[0], p.name));
				}
			}
			return cinfo;
		}
		
		SBean.CityInfo getSearchCityInfo(int maxCityId, int type)
		{
			SBean.CityInfo cinfo = null;
			switch (type)
			{
			case 1:
				cinfo = getSearchCityInfoA(maxCityId);
				break;
			case 2:
				cinfo = getSearchCityInfoB(maxCityId);
				break;
			case 3:
				cinfo = getSearchCityInfoC();
				break;
			default:
				break;
			}
			return cinfo;
		}
		
		SBean.CityInfo getSearchCityInfoA(int maxCityId)
		{
			List<Integer> lst = getCanbeAttackedCitys(maxCityId);
			if (lst.isEmpty())
				return null;
			int index = lst.size()-1;
			int cityID = lst.get(index);
			return getSearchCityInfo(cityID);
		}
		
		SBean.CityInfo getSearchCityInfoB(int maxCityId)
		{
			List<Integer> lst = getCanbeAttackedCitys(maxCityId);
			if (lst.isEmpty())
				return null;
			int index = GameData.getInstance().getRandom().nextInt(lst.size());
			int cityID = lst.get(index);
			return getSearchCityInfo(cityID);
		}
		
		SBean.CityInfo getSearchCityInfoC()
		{
			List<Integer> lst = getCanbeAttackedCitys(Integer.MAX_VALUE);
			if (lst.isEmpty())
				return null;
			int index = lst.size()-1;
			int cityID = lst.get(index);
			return getSearchCityInfo(cityID);
		}
		
		boolean isCityCanbeAttacked(SBean.DBBaseCity city, int curTime)
		{
			syncBaseCity(city, curTime);
			if (city.guardStartTime == 0 || city.guardStartTime + city.guardLifeTime - 600 < curTime )
				return false;
			if (city.guardGenerals.isEmpty())
				return false;
			if (!isSelfOwnCity(city))
				return false;
			if (city.attackTime != 0 && curTime <= city.attackTime + 90)
				return false;
			return true;
		}
		
		List<Integer> getCanbeAttackedCitys(int maxCityID)
		{
			List<Integer> lst = new ArrayList<Integer>();
			int curTime = this.getTime();
			for (SBean.DBBaseCity city : citys.baseCitys)
			{
				if (isCityCanbeAttacked(city, curTime) && city.cityID <= maxCityID)
					lst.add(city.cityID);
			}
			return lst;
		}
		
		InvokeRes<SBean.CityInfo> ownCityAttackedStartByOther(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid)
		{
			if (baseGSID == this.getServerID() && baseRoleID == this.getRoleID())
				return selfOwnCityAttackedStartByOther(gsid, roleid, cityid);
			else
				return ownOtherCityAttackedStartByOther(gsid, roleid, baseGSID, baseRoleID, cityid);
		}
		
		InvokeRes<SBean.CityInfo> selfOwnCityAttackedStartByOther(int gsid, int roleid, int cityid)
		{
			SBean.DBBaseCity city = getBaseCity(cityid);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (!isCityInGuardTime(city, curTime))
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR, null);
			if (city.attackTime != 0 && curTime <= city.attackTime + 90)
				if (city.attackGSID != gsid || city.attackRoleID != roleid)
					return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ALREADY_ATTACKED_BY_OTHER_ERROR, null);
			if (gsid <= 0 || roleid <= 0)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ATTACK_ROLE_ID_ERROR, null);
			if (!isSelfOwnCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_SELF_CITY_ERROR, null);
			city.attackTime = curTime;
			city.attackGSID = gsid;
			city.attackRoleID = roleid;
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, toCityInfo(city));
		}
		
		InvokeRes<SBean.CityInfo> ownOtherCityAttackedStartByOther(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid)
		{
			SBean.DBBaseCity city = getOccupiedCity(baseGSID, baseRoleID, cityid);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			int curTime = this.getTime();
			syncOccupiedCity(city, curTime);
			if (!isValidCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OWN_CITY_ERROR, null);
			if (!isCityInGuardTime(city, curTime))
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR, null);
			if (city.attackTime != 0 && curTime <= city.attackTime + 90)
				if (city.attackGSID != gsid || city.attackRoleID != roleid)
					return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ALREADY_ATTACKED_BY_OTHER_ERROR, null);
			if (gsid <= 0 || roleid <= 0)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ATTACK_ROLE_ID_ERROR, null);
			if (!isOtherCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OTHER_CITY_ERROR, null);
			city.attackTime = curTime;
			city.attackGSID = gsid;
			city.attackRoleID = roleid;
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, toCityInfo(city));
		}
		
		InvokeRes<SBean.CityInfo> ownCityAttackedEndByOther(int gsid, int roleid, String roleName, int baseGSID, int baseRoleID, int cityid, byte win)
		{
			if (baseGSID == this.getServerID() && baseRoleID == this.getRoleID())
				return selfOwnCityAttackedEndByOther(gsid, roleid, roleName, cityid, win);
			else
				return ownOtherCityAttackedEndByOther(gsid, roleid, roleName, baseGSID, baseRoleID, cityid, win);
		}
		
		InvokeRes<SBean.CityInfo> selfOwnCityAttackedEndByOther(int gsid, int roleid, String roleName, int cityid, byte win)
		{
			SBean.DBBaseCity city = getBaseCity(cityid);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (!isCityInGuardTime(city, curTime))
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR, null);
			if (city.attackTime == 0)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_ATTACKED_START_BY_SELF_ERROR, null);
			if (city.attackGSID != gsid || city.attackRoleID != roleid)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_ATTACKED_START_BY_SELF_ERROR, null);
			if (gsid <= 0 || roleid <= 0)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ATTACK_ROLE_ID_ERROR, null);
			if (!isSelfOwnCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OWN_CITY_ERROR, null);
			if (win > 0)
			{
				balanceIncome(city, curTime);
				setCityLost(city);
				city.ownerGSID = gsid;
				city.ownerRoleID = roleid;
				city.ownerRoleName = roleName;
			}
			city.attackTime = 0;
			city.attackGSID = 0;
			city.attackRoleID = 0;
			SBean.CityInfo info = toCityInfo(city);
			
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, info);
		}
		
		InvokeRes<SBean.CityInfo> ownOtherCityAttackedEndByOther(int gsid, int roleid, String roleName, int baseGSID, int baseRoleID, int cityid, byte win)
		{
			SBean.DBBaseCity city = getOccupiedCity(baseGSID, baseRoleID, cityid);
			if (city == null)
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_FIND_CITY_ERROR, null);
			int curTime = this.getTime();
			syncOccupiedCity(city, curTime);
			if (!isValidCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OWN_CITY_ERROR, null);
			if (!isCityInGuardTime(city, curTime))
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR, null);
			if (city.attackTime != 0 && (city.attackGSID != gsid || city.attackRoleID != roleid))
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_NOT_ATTACKED_START_BY_SELF_ERROR, null);
			if (gsid <= 0 || roleid <= 0)
				return new InvokeRes<SBean.CityInfo>(INVOKE_CITY_ATTACK_ROLE_ID_ERROR, null);
			if (!isOtherCity(city))
				return new InvokeRes<SBean.CityInfo>(INVOKE_NOT_OTHER_CITY_ERROR, null);
			if (win > 0)
			{
				//balanceIncome(city, curTime);
				balanceOccupiedCityReward(city, curTime);
				setCityLost(city);
				city.guardGenerals.clear();
				city.guardPets.clear();
				city.ownerGSID = gsid;
				city.ownerRoleID = roleid;
				city.ownerRoleName = roleName;
				city.guardLifeTime = curTime - city.guardStartTime;
				clearOccuoiedCity(city);
			}
			city.attackTime = 0;
			city.attackGSID = 0;
			city.attackRoleID = 0;
			SBean.CityInfo info = toCityInfo(city);
			return new InvokeRes<SBean.CityInfo>(INVOKE_SUCCESS, info);
		}
		
		void baseCityOwnerChanged(int gsid, int roleid, String roleName, int cityid)
		{
			SBean.DBBaseCity city = getBaseCity(cityid);
			if (city == null)
				return;
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (!isCityInGuardTime(city, curTime))
				return;
			if (!isLostCity(city))
				return;
			if (gsid <= 0 || roleid <= 0)
				return;
			city.ownerGSID = gsid;
			city.ownerRoleID = roleid;
			city.ownerRoleName = roleName;
		}
	}
	
	public static class RoleCitys extends Citys
	{
		Role role;
		SBean.CityInfo curAttackCity;
		int curAttackTime;
		int curSearchCount;
		int curSearchTimes;
		public RoleCitys(Role role)
		{
			this.role = role;
		}
		
		public RoleCitys(Role role, SBean.DBCitys citys)
		{
			super(citys);
			this.role = role;
			for (SBean.DBBaseCity city : citys.baseCitys) {
				boolean selfOwn = (city.ownerGSID == city.baseGSID && city.ownerRoleID == city.baseRoleID);
				city.baseGSID = role.gs.getConfig().id;
				city.baseRoleID = role.id;
				if (selfOwn) {
					city.ownerGSID = city.baseGSID;
					city.ownerRoleID = city.baseRoleID;
				}
			}
		}
		
		
		SBean.DBCitys getDBCitys()
		{
			return this.citys;
		}
		
		GameServer getGameServer()
		{
			return this.role.gs;
		}
		
		int getServerID()
		{
			return role.gs.getConfig().id;
		}
		
		int getRoleID()
		{
			return role.id;
		}
		
		String getRoleName()
		{
			return role.name;
		}
		
		int getTime()
		{
			return role.gs.getTime();
		}
		
		boolean hasGeneral(short gid)
		{
			return role.generals.containsKey(gid);
		}
		
		boolean hasPet(short pid)
		{
			return role.pets.containsKey(pid);
		}
		
		DBRoleGeneral getGeneral(short gid)
		{
			General general = role.generals.get(gid);
			if(general!=null){
				general.generalStone=role.copyGeneralStoneWithoutLock(gid);
			}
			return general;
		}
		
		SBean.DBPet getPet(short pid, byte[] deformStage)
		{
			SBean.DBPetDeform deform = role.petDeforms.get(pid);
			if (deform != null)
				deformStage[0] = deform.deformStage;
			return role.pets.get(pid);
		}
		
		synchronized boolean testUnlockBaseCity(int combatid)
		{
			SBean.CityCFGS ccfg = GameData.getInstance().getUnlockCity(combatid);
			if (ccfg == null)
				return false;
			//int cityNum = 
			unlockBaseCity(ccfg.id);
			return true;
		}
		
		
		int unlockBaseCity(int cityID)
		{
			if (cityID != super.citys.baseCitys.size() + 1)
				return 0;
			SBean.DBBaseCity city = new SBean.DBBaseCity();
			city.baseGSID = this.getServerID();
			city.baseRoleID = role.id;
			city.baseRoleName = role.name;
			city.cityID = cityID;
			city.openTime = this.getTime();
			city.guardGenerals = new ArrayList<Short>();
			city.guardPets = new ArrayList<Short>();
			city.guardTimeLog = new ArrayList<Integer>();
			city.totalIncome = new ArrayList<SBean.DropEntryNew>();
			super.resetBaseCity(city);
			super.citys.baseCitys.add(city);
			if (super.citys.baseCitys.size() == 1)
				super.citys.lastBaseRewardTime = this.getTime();
			return super.citys.baseCitys.size();
		}
		
		boolean canTakeOccupyCitysReward()
		{
			syncOccupyCitys(this.getTime());
			return !citys.occupiedCitysRewardLog.isEmpty();
		}
		

		CityRewardLog getOccupyCitysRewardLog()
		{
			CityRewardLog log = new CityRewardLog();
			syncOccupyCitys(this.getTime());
			log.rewards.addAll(citys.occupiedCitysRewardLog);
			log.nTimesStone = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_DIAMOND);
			log.nTimesMoney = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_MONEY);
			log.nTimesItem = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_ITEM);
			return log;
		}

		
		CityReward takeBaseCityReward(int cityID)
		{
			CityReward creward = new CityReward();
			SBean.DBBaseCity city = getBaseCity(cityID);
			if (city == null)
				return creward;
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			synchronized (role)
			{
				syncBaseCity(city, this.getTime());
				//this.getGameServer().getLogger().info("totalIncome " + city.totalIncome.isEmpty());
				List<SBean.DropEntryNew> tmp = creward.rewards;
				creward.rewards = city.totalIncome;
				city.totalIncome = tmp;
				creward.nTimesStone = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_DIAMOND);
				creward.nTimesMoney = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_MONEY);
				creward.nTimesItem = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_ITEM);
				for (SBean.DropEntryNew e : creward.rewards)
				{
					int nTimes = 1;
					if (e.type == GameData.COMMON_TYPE_STONE)
					{
						nTimes = creward.nTimesStone;
					}
					else if (e.type == GameData.COMMON_TYPE_MONEY)
					{
						nTimes = creward.nTimesMoney;
					}
					else
					{
						nTimes = creward.nTimesItem;
					}
					commonFlowRecord.addChange(role.addDropNew(new SBean.DropEntryNew(e.type, e.id, e.arg*nTimes)));
				}
			}
			commonFlowRecord.setEvent(TLog.AT_CITY_REWARD);
			commonFlowRecord.setArg(0);
			tlogEvent.addCommonFlow(commonFlowRecord);
			TLogger.CityWarRecord crecord = new TLogger.CityWarRecord(TLog.CITYWAREVENT_REWARD);
			crecord.setArgs(0, 0, cityID, 0);
			tlogEvent.addRecord(crecord);
			this.getGameServer().getTLogger().logEvent(role, tlogEvent);
			return creward;
		}
		
		CityReward takeOccupyCityReward(int baseGSID, int baseRoleID, int cityID, int guardStartTime)
		{
			CityReward creward = new CityReward();
			TLogger.TLogEvent tlogEvent = new TLogger.TLogEvent();
			TLogger.CommonFlowRecord commonFlowRecord = new TLogger.CommonFlowRecord();
			synchronized (role)
			{
				//syncOccupyCitys(this.getTime());
				Iterator<SBean.DBCityReward> it = citys.occupiedCitysRewardLog.iterator();
				while (it.hasNext())
				{
					SBean.DBCityReward r = it.next();
					if (r.baseGSID == baseGSID && r.baseRoleID == baseRoleID && r.cityID == cityID && r.guardStartTime == guardStartTime)
					{
						creward.rewards.addAll(r.totalIncome);
						it.remove();
						break;
					}
				}
				creward.nTimesStone = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_DIAMOND);
				creward.nTimesMoney = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_MONEY);
				creward.nTimesItem = this.getGameServer().getGameActivities().getDoubleDropActivity().getDropMultiple(GameActivities.CAPTURE_CITY, GameActivities.DROP_ITEM);
				for (SBean.DropEntryNew e : creward.rewards)
				{
					int nTimes = 1;
					if (e.type == GameData.COMMON_TYPE_STONE)
					{
						nTimes = creward.nTimesStone;
					}
					else if (e.type == GameData.COMMON_TYPE_MONEY)
					{
						nTimes = creward.nTimesMoney;
					}
					else
					{
						nTimes = creward.nTimesItem;
					}
					commonFlowRecord.addChange(role.addDropNew(new SBean.DropEntryNew(e.type, e.id, e.arg*nTimes)));
				}
			}
			commonFlowRecord.setEvent(TLog.AT_CITY_REWARD);
			commonFlowRecord.setArg(0);
			tlogEvent.addCommonFlow(commonFlowRecord);
			TLogger.CityWarRecord crecord = new TLogger.CityWarRecord(TLog.CITYWAREVENT_REWARD);
			crecord.setArgs(baseGSID, baseRoleID, cityID, guardStartTime);
			tlogEvent.addRecord(crecord);
			this.getGameServer().getTLogger().logEvent(role, tlogEvent);
			return creward;
		}
		
		int getCurSearchType()
		{
			List<Integer> seqs = GameData.getInstance().getCaptureCityCfg().searchTypeSequence;
			int index = this.curSearchCount % seqs.size();
			return seqs.get(index);
		}
		
		void setCurSearchTypeResult(boolean success)
		{
			this.curSearchTimes++;
			GameData.getInstance().getCaptureCityCfg().searchTypeLvlRanges.size();
			if (success || this.curSearchTimes > 0)
			{
				this.curSearchCount++;
				this.curSearchTimes = 0;
			}
		}
		
		boolean onSelfCityAttackBackSuccess(int cityid)
		{
			SBean.DBBaseCity city = getBaseCity(cityid);
			if (city == null)
				return false;
			int curTime = this.getTime();
			syncBaseCity(city, curTime);
			if (!isCityInGuardTime(city, curTime))
				return false;
			if (!isLostCity(city))
				return false;
			balanceIncome(city, curTime);
			setCityBack(city);
			city.ownerGSID = city.baseGSID;
			city.ownerRoleID = city.baseRoleID;
			city.ownerRoleName = this.getRoleName();
			return true;
		}
		
		void onOccupyOtherCitySuccess(int baseGSID, int baseRoleID, String baseRoleName, int cityid, int guardStartTime, int guardLifeTime)
		{
			occupyCity(baseGSID, baseRoleID, baseRoleName, cityid, guardStartTime, guardLifeTime);
		}
		
		void onOtherCityAttackEndSuccess(int baseGSID, int baseRoleID, String baseRoleName, int cityid, int guardStartTime, int guardLifeTime)
		{
			if (this.getServerID() == baseGSID)
			{
				if (this.getRoleID() == baseRoleID)
				{
					onSelfCityAttackBackSuccess(cityid);
				}
				else
				{
					onOccupyOtherCitySuccess(baseGSID, baseRoleID, baseRoleName, cityid, guardStartTime, guardLifeTime);
					this.getGameServer().getCityManager().baseCityOwnerChanged(this.getServerID(), this.getRoleID(), this.getRoleName(), baseGSID, baseRoleID, cityid, new CityManager.BaseCityOwnerChangedCallback()
							{
								public void onCallback(int errcode)
								{
									
								}
							});
				}
			}
			else
			{
				onOccupyOtherCitySuccess(baseGSID, baseRoleID, baseRoleName, cityid, guardStartTime, guardLifeTime);
				SBean.NotifyCityOwnerChangedReq req = new SBean.NotifyCityOwnerChangedReq();
				req.roleID = new SBean.GlobalRoleID(this.getServerID(), this.getRoleID());
				req.roleName = this.getRoleName();
				req.baseRoleID = new SBean.GlobalRoleID(baseGSID, baseRoleID);
				req.cityID = cityid;
				this.getGameServer().getCityManager().notifyCityOwnerChanged(req);
			}
		}
		
		void onHandleSearchCityResponse(boolean opSuccess)
		{
			this.setCurSearchTypeResult(opSuccess);
		}
		
		void onHandleAttackCityStartResponse(boolean opSuccess, SBean.CityInfo info)
		{
			if (opSuccess)
			{
				this.curAttackCity = info;
				this.curAttackTime = this.getTime();	
			}
		}
		
		void onHandleAttackCityEndResponse(boolean opSuccess, SBean.CityInfo info)
		{
			if (opSuccess)
			{
				if (this.curAttackCity != null && info.baseRoleID.serverID == curAttackCity.baseRoleID.serverID && info.baseRoleID.roleID == curAttackCity.baseRoleID.roleID)
					if (info.ownerRoleID.serverID == this.getServerID() && info.ownerRoleID.roleID == this.getRoleID())
						this.onOtherCityAttackEndSuccess(info.baseRoleID.serverID, info.baseRoleID.roleID, info.baseRoleName, info.cityID, info.guardStartTime, info.guardLifeTime);
			}
			this.curAttackCity = null;
			this.curAttackTime = 0; 
		}
		
		boolean checkCurAttackCity(int gsid, int roleid, int baseGSID, int baseRoleID, int cityid)
		{
			return (this.curAttackCity != null && baseGSID == curAttackCity.baseRoleID.serverID && baseRoleID == curAttackCity.baseRoleID.roleID && curAttackCity.cityID == cityid
					&& gsid == curAttackCity.ownerRoleID.serverID && roleid == curAttackCity.ownerRoleID.roleID);
		}
	}
	
	public static class DBRoleCitys extends Citys
	{
		GameServer gs;
		DBRole dbrole;
		
		public DBRoleCitys(GameServer gs, DBRole dbrole, SBean.DBCitys citys)
		{
			super(citys);
			this.gs = gs;
			this.dbrole = dbrole;
		}
		
		
		SBean.DBCitys getDBCitys()
		{
			return this.citys;
		}
		
		GameServer getGameServer()
		{
			return this.gs;
		}
		
		int getServerID()
		{
			return this.gs.getConfig().id;
		}
		
		int getRoleID()
		{
			return dbrole.id;
		}
		
		String getRoleName()
		{
			return dbrole.name;
		}
		
		int getTime()
		{
			return this.gs.getTime();
		}
		
		boolean hasGeneral(short gid)
		{
			for (DBRoleGeneral g : dbrole.generals)
			{
				if (g.id == gid)
					return true;
			}
			return false;
		}
		
		boolean hasPet(short pid)
		{
			for (SBean.DBPet p : dbrole.pets)
			{
				if (p.id == pid)
					return true;
			}
			return false;
		}
		
		DBRoleGeneral getGeneral(short gid)
		{
			for (DBRoleGeneral g : dbrole.generals)
			{
				if (g.id == gid){
					if(g!=null){
						g.generalStone=dbrole.getActiveGeneralStones(gid);
					}
					return g;
				}
				
					
			}
			return null;
		}
		
		SBean.DBPet getPet(short pid, byte[] deformStage)
		{
			for (SBean.DBPet p : dbrole.pets)
			{
				if (p.id == pid) {
					for (SBean.DBPetDeform d : dbrole.petDeforms)
						if (d.id == pid)
							deformStage[0] = d.deformStage;
					return p;
				}
			}
			return null;
		}
	}
	
	public synchronized boolean isAttacked(int rid)
	{
		return attacked.contains(rid);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private Integer getLevelRandomRole(int searchRoleGSID, int searchRoleRoleID, int minLvl, int maxLvl) 
	{
		gs.getLogger().debug("getLevelRandomRole(" + searchRoleGSID + "," + searchRoleRoleID + ", " + minLvl +", " + maxLvl +")");
		int gsid = gs.getConfig().id;
		for (int i = 0; i < 5; ++i) 
		{
			Integer rid = gs.getLoginManager().getRandomLevelRole(minLvl, maxLvl);
			if (rid != null) 
			{
				if (gsid != searchRoleGSID || rid != searchRoleRoleID)
					return rid;
				gs.getLogger().debug("getLevelRandomRole find self...");
			}
		}
		return null;
	}

	private static CityManager instance;
	private GameServer gs;
	private CityEventService service;
	
	Set<Integer> attacked = new HashSet<Integer>();	

	public static final int INVOKE_SUCCESS = 0;
	public static final int INVOKE_UNKNOWN_ERROR = -1;
	public static final int INVOKE_TIMEOUT = -2;
	public static final int INVOKE_NOT_FIND_CITY_ERROR = -3;
	public static final int INVOKE_CITY_NOT_IN_GUARD_TIME_ERROR = -4;
	public static final int INVOKE_NOT_OWN_CITY_ERROR = -5;
	public static final int INVOKE_NOT_SELF_CITY_ERROR = -6;
	public static final int INVOKE_NOT_OTHER_CITY_ERROR = -7;
	public static final int INVOKE_SET_CITY_GUARD_GENERAL_ERROR = -8;
	public static final int INVOKE_SET_CITY_GUARD_TIME_ERROR = -9;
	public static final int INVOKE_CITY_ALREADY_IN_GUARD_TIME_ERROR = -10;
	public static final int INVOKE_SEARCH_CANNOT_FIND_MATCH_ROLE = -11;
	public static final int INVOKE_SEARCH_NOT_ENOUGH_MOENY_ERROR = -12;
	public static final int INVOKE_ATTACK_OTHER_MAX_ERROR = -13;
	public static final int INVOKE_CITY_ALREADY_ATTACKED_BY_OTHER_ERROR = -14;
	public static final int INVOKE_CITY_ATTACK_ROLE_ID_ERROR = -15;
	public static final int INVOKE_CITY_NOT_ATTACKED_START_BY_SELF_ERROR = -16;
	public static final int INVOKE_SEARCH_MAX_CITY_ERROR = -17;
	
	public static final int INVOKE_VIST_ROLE_ERROR = -100;
	public static final int INVOKE_VIST_DBROLE_ERROR = -101;
	
	
	public static final long MAX_WAIT_TIME = 2000;
}

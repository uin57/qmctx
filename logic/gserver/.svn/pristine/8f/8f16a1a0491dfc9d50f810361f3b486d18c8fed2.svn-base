
package i3k.exchange;


import i3k.SBean;
import i3k.SBean.ExpatriateTransRoleReq;
import i3k.SBean.ExpatriateTransRoleRes;
import i3k.gs.CityManager;

public class DataManager 
{
	
	public DataManager(ExchangeServer es)
	{
		this.es = es;
	}

	public void searchCity(int serverID, SBean.SearchCityReq req)
	{
		es.getLogger().info("search city req, " + CityManager.toBriefString(req) + ", select server " + serverID);
		es.getRPCManager().sendSearchCityReq(serverID, req);
	}
	
	public void sendSearchCityResult(SBean.SearchCityRes res)
	{
		es.getLogger().info("search city res, " + CityManager.toBriefString(res));
		es.getRPCManager().sendSearchCityRes(res.roleID.serverID, res);
	}
	
	public void attackCityStart(SBean.AttackCityStartReq req)
	{
		es.getLogger().info("attack city start req, " + CityManager.toBriefString(req));
		es.getRPCManager().sendAttackCityStartReq(req.targetRoleID.serverID, req);
	}
	
	public void sendAttackCityStartResult(SBean.AttackCityStartRes res)
	{
		es.getLogger().info("attack city start res, " + CityManager.toBriefString(res));
		es.getRPCManager().sendAttackCityStartRes(res.roleID.serverID, res);
	}
	
	public void attackCityEnd(SBean.AttackCityEndReq req)
	{
		es.getLogger().info("attack city end req, " + CityManager.toBriefString(req));
		es.getRPCManager().sendAttackCityEndReq(req.targetRoleID.serverID, req);
	}
	
	public void sendAttackCityEndResult(SBean.AttackCityEndRes res)
	{
		es.getLogger().info("attack city end res, " + CityManager.toBriefString(res));
		es.getRPCManager().sendAttackCityEndRes(res.roleID.serverID, res);
	}
	
	public void queryCityOwner(SBean.QueryCityOwnerReq req)
	{
		es.getLogger().info("query city owner req, " + CityManager.toBriefString(req));
		es.getRPCManager().sendQueryCityOwnerReq(req.targetRoleID.serverID, req);
	}
	
	public void sendQueryCityOwnerResult(SBean.QueryCityOwnerRes res)
	{
		es.getLogger().info("query city owner res, " + CityManager.toBriefString(res));
		es.getRPCManager().sendQueryCityOwnerRes(res.roleID.serverID, res);
	}
	
	public void notifyCityOwnerChanged(SBean.NotifyCityOwnerChangedReq req)
	{
		es.getLogger().info("notify city owner changed req, " + CityManager.toBriefString(req));
		es.getRPCManager().sendNotifyCityOwnerChangeReq(req.baseRoleID.serverID, req);
	}
	
    /*
	public void queryUserHasRoles(QueryHasRoleReq req)
        {
                es.getLogger().info("query user_has_roles req, ");
                es.getRPCManager().sendQueryUserHasRoleReq(req.targs, req);
        }
	
	public void queryUserHasRolesForward(QueryHasRoleRes res)
        {
                es.getLogger().info("query user_has_roles res, ");
                es.getRPCManager().sendQueryUserHasRoleRes(res.targs, res);
                
        }
        */
    
	public void forwardTransDBRole(ExpatriateTransRoleReq req)
        {
                es.getLogger().info("forward dbrole to other gs req"); 
                es.getRPCManager().sendTransDBRoleReq(req.targs, req);
        }
    
        public void forwardTransDBRoleForward(ExpatriateTransRoleRes res)
        {
                es.getLogger().info("forward dbrole from other gs res");
                es.getRPCManager().sendTransDBRoleForwardRes(res.targs, res);
                
        }
       
        ExchangeServer es;
        

}


package i3k.friend;

import java.util.ArrayList;
import java.util.List;

import i3k.DBOpenUser;
import i3k.SBean;
import i3k.DBRoleGeneral;
import ket.kdb.Table;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;

public class DataManager 
{
	
	public class SaveTrans implements Transaction
	{	
		public SaveTrans(String openID, SBean.GlobalRoleID roleID)
		{
			this.openID = openID;
			this.roleID = roleID;
		}

		@Override
		public boolean doTransaction()
		{	
			DBOpenUser u = user.get(openID);
			if( u == null )
			{
				u = new DBOpenUser();
				u.records = new ArrayList<SBean.GlobalRoleID>();
				u.records.add(roleID);
				user.put(openID, u);
			}
			else
			{
				boolean bFound = false;
				boolean bSame = false;
				for(SBean.GlobalRoleID e : u.records)
				{
					if( e.serverID == roleID.serverID )
					{
						bFound = true;
						if( e.roleID == roleID.roleID )
						{
							bSame = true;
						}
						else
							e.roleID = roleID.roleID;
						break;
					}
				}
				if( ! bFound )
					u.records.add(roleID);
				if( ! bFound || ! bSame )
					user.put(openID, u);
					
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
			{
				fs.getLogger().warn("user save failed");
			}
			else
			{
				fs.getRPCManager().setCreateRoleRes(roleID);
			}
		}
		
		@AutoInit
		public Table<String, DBOpenUser> user;
		
		public final String openID;
		public final SBean.GlobalRoleID roleID;
	}
	
	public class SearchFriendsTrans implements Transaction
	{	
		public SearchFriendsTrans(SBean.SearchFriendsReq req)
		{
			this.req = req;
			lst = new ArrayList<SBean.OpenIDRecord>();
		}

		@Override
		public boolean doTransaction()
		{	
			for(String openID : req.openIDs)
			{
				DBOpenUser u = user.get(openID);
				if( u == null || u.records.isEmpty() )
				{
					lst.add(new SBean.OpenIDRecord(openID, new SBean.GlobalRoleID(0, 0)));
				}
				else
				{
					for(SBean.GlobalRoleID e : u.records)
					{
						lst.add(new SBean.OpenIDRecord(openID, e));
					}
				}
			}
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode != ErrorCode.eOK )
				return; // TODO
			for(SBean.OpenIDRecord e : lst)
			{
				if( e.roleID.serverID <= 0 )
					fs.getRPCManager().sendSearchResult(req.roleID.serverID, new SBean.SearchFriendsRes(req.roleID, e,
							new SBean.FriendProp((short)0, 0, "", new ArrayList<DBRoleGeneral>(), new ArrayList<SBean.DBPetBrief>(), new SBean.FriendPets(
									new ArrayList<SBean.DBPetBrief>(), new ArrayList<SBean.DBPetBrief>()), "", (short)-1, (byte)0, 
									new ArrayList<SBean.FriendPetDeform>(), new ArrayList<SBean.DBRelation>(), new ArrayList<SBean.DBGeneralStone>(), (byte)0)));
				else
				{
					fs.getRPCManager().queryFriendProp(req.roleID, e);
				}
			}
		}
		
		@AutoInit
		public TableReadonly<String, DBOpenUser> user;
		
		public final SBean.SearchFriendsReq req;
		public final List<SBean.OpenIDRecord> lst;
	}
	
	public DataManager(FriendServer fs)
	{
		this.fs = fs;
	}
	
	public void announceCreateRole(String openID, SBean.GlobalRoleID roleID)
	{
		fs.getLogger().info("announce create role, openID=" + openID + ",roleID=[" + roleID.serverID + "," + roleID.roleID + "]");
		
		fs.getDB().execute(new SaveTrans(openID, roleID));
	}
	
	public void searchFriends(SBean.SearchFriendsReq req)
	{
		fs.getLogger().info("search friends, roleID=[" + req.roleID.serverID + "," + req.roleID.roleID + "], nSearch = " + req.openIDs.size());
		
		fs.getDB().execute(new SearchFriendsTrans(req));
	}
	
	public void setQueryFriendPropRes(SBean.QueryFriendPropRes res)
	{
		fs.getRPCManager().sendSearchResult(res.req.roleIDSrc.serverID, new SBean.SearchFriendsRes(res.req.roleIDSrc
				, res.req.recordTarget, res.prop));
	}
	
	public void updateFriendProp(SBean.UpdateFriendPropReq req)
	{
		for(SBean.OpenIDRecord e : req.recordTargets)
		{
			fs.getRPCManager().queryFriendProp(req.roleIDSrc, e);
		}
	}
	
	public void sendMessageToFriend(SBean.GlobalRoleID srcID, SBean.GlobalRoleID dstID, byte msgType, short arg1, int arg2)
	{
		fs.getRPCManager().sendMessageToFriend(srcID, dstID, msgType, arg1, arg2);
	}
	
	FriendServer fs;
}

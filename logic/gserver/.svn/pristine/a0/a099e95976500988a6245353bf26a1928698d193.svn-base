
package i3k;

import i3k.gs.GameData;
import i3k.gs.Role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ket.util.Stream;

public class DBFriend implements Stream.IStreamable
{
	
	public static int VERSION_NOW = 2;
	
	public static final byte MSG_TYPE_BAOZI = 1;
	public static final byte MSG_TYPE_PET_EXP = 2;
	public static final byte MSG_TYPE_PET_BREED = 3;
	
	public static final byte DAY_FLAG_BAOZI = 0x1;
	public static final byte DAY_FLAG_RECALL = 0x2;
	public static final byte DAY_FLAG_PET_EXP = 0x4;
	public static final byte DAY_FLAG_PET_BREED = 0x8;
	
	public static final byte FLAG_LOCAL = 0x10;
	
	public static final byte ERR_APPLY_FAILED = -1;
	public static final byte ERR_APPLY_OK = 0;
	public static final byte ERR_APPLY_SELF_FULL = 1;
	public static final byte ERR_APPLY_EXIST = 2;
	public static final byte ERR_APPLY_TARGET_FULL = 3;
	public static final byte ERR_APPLY_TARGET_APPLY_FULL = 4;
	public static final byte ERR_APPLY_TARGET_EXIST = 5;
	public static final byte ERR_APPLY_TARGET_APPLY_EXIST = 6;
	
	public static final byte ERR_ACCEPT_FAILED = -1;
	public static final byte ERR_ACCEPT_OK = 0;
	public static final byte ERR_ACCEPT_SELF_FULL = 1;
	public static final byte ERR_ACCEPT_EXIST = 2;
	public static final byte ERR_ACCEPT_TARGET_FULL = 3;
	public static final byte ERR_ACCEPT_TARGET_EXIST = 4;

	public DBFriend() {  }

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		point = is.popInteger();
		pointToday = is.popShort();
		baoziVitToday = is.popShort();
		recallCount = is.popByte();
		friends = is.popList(SBean.DBFriendData.class);
		msgs = is.popList(SBean.DBFriendMessage.class);
		msgsBack = is.popList(SBean.DBFriendMessage.class);
		if( dbVersion < 2 )
		{
			heart = 0;
			lastGetHeartTime = 0;
			breedRewardToday = 0;
			petExpItemToday = 0;
			applyLst = new ArrayList<SBean.DBFriendApply>();
			friendsDel = new ArrayList<String>();
			friendsSearching = new ArrayList<SBean.DBFriendSearching>();
			padding32 = 0;
			padding33 = 0;
			padding34 = 0;
			padding4 = 0;
			padding5 = 0;
			padding6 = 0;
		}
		else
		{
			heart = is.popShort();
			lastGetHeartTime = is.popInteger();
			breedRewardToday = is.popInteger();
			petExpItemToday = is.popShort();
			applyLst = is.popList(SBean.DBFriendApply.class);
			friendsDel = is.popStringList();
			friendsSearching = is.popList(SBean.DBFriendSearching.class);
			padding32 = is.popByte();
			padding33 = is.popByte();
			padding34 = is.popByte();
			padding4 = is.popInteger();
			padding5 = is.popInteger();
			padding6 = is.popInteger();
		}
		
		// TODO
		{
			Iterator<SBean.DBFriendData> iter = friends.iterator();
			while( iter.hasNext() )
			{
				SBean.DBFriendData f = iter.next();
				if( f.roleID.serverID <= 0 )
				{
					friendsSearching.add(new SBean.DBFriendSearching(f.openID, f.lastPropSyncTime, f.cool));
					iter.remove();
				}
			}
		}
		//		
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);
		os.pushInteger(point);
		os.pushShort(pointToday);
		os.pushShort(baoziVitToday);
		os.pushByte(recallCount);
		os.pushList(friends);
		os.pushList(msgs);
		os.pushList(msgsBack);
		
		os.pushShort(heart);
		os.pushInteger(lastGetHeartTime);
		os.pushInteger(breedRewardToday);
		os.pushShort(petExpItemToday);
		os.pushList(applyLst);
		os.pushStringList(friendsDel);
		os.pushList(friendsSearching);
		os.pushByte(padding32);
		os.pushByte(padding33);
		os.pushByte(padding34);
		os.pushInteger(padding4);
		os.pushInteger(padding5);
		os.pushInteger(padding6);
	}
	
	public DBFriend kdClone()
	{
		DBFriend e = new DBFriend();
		e.point = point;
		e.pointToday = pointToday;
		e.baoziVitToday = baoziVitToday;
		e.breedRewardToday = breedRewardToday;
		e.petExpItemToday = petExpItemToday;
		e.recallCount = recallCount;
		e.friends = new ArrayList<SBean.DBFriendData>();
		for(SBean.DBFriendData f : friends)
			e.friends.add(f.kdClone());
		e.msgs = new ArrayList<SBean.DBFriendMessage>();
		for(SBean.DBFriendMessage f : msgs)
			e.msgs.add(f.kdClone());
		e.msgsBack = new ArrayList<SBean.DBFriendMessage>();
		for(SBean.DBFriendMessage f : msgsBack)
			e.msgsBack.add(f.kdClone());
		e.applyLst = new ArrayList<SBean.DBFriendApply>();
		for(SBean.DBFriendApply a : applyLst)
			e.applyLst.add(a.kdClone());
		e.friendsDel = new ArrayList<String>();
		for(String s : friendsDel)
			e.friendsDel.add(s);
		e.friendsSearching = new ArrayList<SBean.DBFriendSearching>();
		for(SBean.DBFriendSearching fs : friendsSearching)
			e.friendsSearching.add(fs.kdClone());
		e.heart = heart;
		e.lastGetHeartTime = lastGetHeartTime;
		return e;
	}
	
	public DBFriend removeInvalid()
	{
		Iterator<SBean.DBFriendData> iter = friends.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendData e = iter.next();
			if( e.roleID.serverID <= 0 || e.roleID.roleID <= 0 )
				iter.remove();
		}
		return this;
	}
	
	public int getFriendCount()
	{
		int c = 0;
		for(SBean.DBFriendData e : friends)
		{
			if( e.roleID.serverID > 0 )
				++c;
		}
		return c;
	}
	
	public boolean isFriend(int serverID, int roleID)
	{
		for(SBean.DBFriendData e : friends)
		{
			if( e.roleID.serverID > 0 && e.roleID.serverID == serverID && e.roleID.roleID == roleID )
				return true;
		}
		return false;
	}
	
	public int addApply(int serverID, int applyRoleID, short headIconID, String applyRoleName, String applyForceName, short applyRoleLevel, int now)
	{
		if( serverID <= 0 || applyRoleID <= 0 || applyRoleName.equals("") || applyRoleLevel < 1 )
			return ERR_APPLY_FAILED;
		if( isFriend(serverID, applyRoleID) )
			return ERR_APPLY_TARGET_EXIST;
		if( getFriendCount() >= GameData.getInstance().getFriendCFG().cap )
			return ERR_APPLY_TARGET_FULL;
		for(SBean.DBFriendApply e : applyLst)
		{
			if( e.roleID == applyRoleID )
				return ERR_APPLY_TARGET_APPLY_EXIST;
		}
		if( applyLst.size() >= 100 )
			return ERR_APPLY_TARGET_APPLY_FULL;
		applyLst.add(new SBean.DBFriendApply(applyRoleID, applyRoleLevel, headIconID, applyRoleName, applyForceName, now, 0));
		return ERR_APPLY_OK;
	}
	
	public int applyAccepted(int serverID, int targetID, SBean.DBFriendData friendData, int now)
	{
		if( serverID <= 0 || targetID <= 0 )
			return ERR_ACCEPT_FAILED;
		if( isFriend(serverID, targetID) )
			return ERR_ACCEPT_TARGET_EXIST;
		if( getFriendCount() >= GameData.getInstance().getFriendCFG().cap )
			return ERR_ACCEPT_TARGET_FULL;
		friends.add(friendData.kdClone());
		return ERR_ACCEPT_OK;
	}
	
	public boolean deleteApply(int applyRoleID)
	{
		boolean bFound = false;
		Iterator<SBean.DBFriendApply> iter = applyLst.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendApply a = iter.next();
			if( a.roleID == applyRoleID )
			{
				bFound = true;
				iter.remove();
			}
		}
		return bFound;
	}
	
	public boolean delLocalFriend(int serverID, int roleID)
	{
		boolean bDel = false;
		Iterator<SBean.DBFriendData> iter = friends.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendData data = iter.next();
			if( (data.flag&FLAG_LOCAL) != 0 && data.roleID.roleID == roleID )
			{
				bDel = true;
				iter.remove();
			}
		}
		if( bDel )
		{
		{
			Iterator<SBean.DBFriendMessage> it = msgs.iterator();
			while( it.hasNext() )
			{
				SBean.DBFriendMessage m = it.next();
				if( m.roleID.serverID == serverID && m.roleID.roleID == roleID )
				{
					it.remove();
				}
			}
		}
		{
			Iterator<SBean.DBFriendMessage> it = msgsBack.iterator();
			while( it.hasNext() )
			{
				SBean.DBFriendMessage m = it.next();
				if( m.roleID.serverID == serverID && m.roleID.roleID == roleID )
				{
					it.remove();
				}
			}
		}
		}
		return bDel;
	}
	
	public String checkOpenID(int serverID, int roleID)
	{
		Iterator<SBean.DBFriendData> iter = friends.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendData data = iter.next();
			if(  data.roleID.roleID == roleID && data.roleID.serverID == serverID )
			{
				return data.openID;
			}
		}
		return null;
	}
	
	public int delFriend(int serverID, int roleID)
	{
		boolean bDel = false;
		boolean bLocal = false;
		String openID = null;
		Iterator<SBean.DBFriendData> iter = friends.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendData data = iter.next();
			if(  data.roleID.roleID == roleID && data.roleID.serverID == serverID )
			{
				bDel = true;
				if( (data.flag&FLAG_LOCAL) != 0 )
					bLocal = true;
				else
					openID = data.openID;
				iter.remove();
			}
		}
		if( bDel )
		{
			{
				Iterator<SBean.DBFriendMessage> it = msgs.iterator();
				while( it.hasNext() )
				{
					SBean.DBFriendMessage m = it.next();
					if( m.roleID.serverID == serverID && m.roleID.roleID == roleID )
					{
						it.remove();
					}
				}
			}
			{
				Iterator<SBean.DBFriendMessage> it = msgsBack.iterator();
				while( it.hasNext() )
				{
					SBean.DBFriendMessage m = it.next();
					if( m.roleID.serverID == serverID && m.roleID.roleID == roleID )
					{
						it.remove();
					}
				}
			}
			if( openID != null )
			{
				boolean bDelAll = true;
				for(SBean.DBFriendData e : friends)
				{
					if( e.openID.equals(openID) )
					{
						bDelAll = false;
						break;
					}
				}
				if( bDelAll )
				{
					// TODO del all openID
					if( ! friendsDel.contains(openID) )
						friendsDel.add(openID);
				}
			}
		}
		if( bDel )
			return bLocal ? 1 : 2;
		return 0;
	}
	
	public void recvMessageFromFriend(SBean.GlobalRoleID srcID, int now, byte msgType, short arg1, int arg2)
	{
		switch( msgType )
		{
		case MSG_TYPE_PET_EXP:
			if( arg2 <= 0 || arg2 > 100 || GameData.getInstance().getItemCFG(arg1) == null )
				return;
			break;
		case MSG_TYPE_PET_BREED:
			if( arg2 <= 0 || GameData.getInstance().getPetCFG(arg1, false) == null )
				return;
			break;
		default:
			break;
		}
		//
		Iterator<SBean.DBFriendMessage> iter = msgs.iterator();
		while( iter.hasNext() )
		{
			SBean.DBFriendMessage m = iter.next();
			if( m.time + 86400 < now )
				iter.remove();
		}
		//
		String openID = null;
		for(SBean.DBFriendData e : friends)
		{
			if( srcID.serverID == e.roleID.serverID && srcID.roleID == e.roleID.roleID )
			{
				openID = e.openID;
				break;
			}
		}
		if( openID != null && msgs.size() < 256 /*TODO*/)
		{
			msgs.add(new SBean.DBFriendMessage(openID, srcID, msgType, (byte)0, arg1, arg2, now));
		}
		if( openID == null )
		{
			msgsBack.add(new SBean.DBFriendMessage("", srcID, msgType, (byte)0, arg1, arg2, now));
		}
	}

	public int point;
	public short pointToday;
	public short baoziVitToday;
	public byte recallCount;
	public List<SBean.DBFriendData> friends = new ArrayList<SBean.DBFriendData>();
	public List<SBean.DBFriendMessage> msgs = new ArrayList<SBean.DBFriendMessage>();
	public List<SBean.DBFriendMessage> msgsBack = new ArrayList<SBean.DBFriendMessage>();
	
	public short heart;
	public int lastGetHeartTime;
	public int breedRewardToday;
	public short petExpItemToday;
	public List<SBean.DBFriendApply> applyLst = new ArrayList<SBean.DBFriendApply>();
	public List<String> friendsDel = new ArrayList<String>();
	public List<SBean.DBFriendSearching> friendsSearching = new ArrayList<SBean.DBFriendSearching>();
	public byte padding32;
	public byte padding33;
	public byte padding34;
	public int padding4;
	public int padding5;
	public int padding6;
}
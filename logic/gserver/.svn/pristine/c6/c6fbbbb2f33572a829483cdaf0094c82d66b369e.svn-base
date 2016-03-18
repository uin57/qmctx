
package i3k.global;

import i3k.DBSocialUser;
import i3k.DBSocialMsg;
import i3k.DBThemeMsg;
import i3k.gs.GameData;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;
import java.util.Comparator;



import ket.kdb.DB;
import ket.kdb.Table;
import ket.kdb.TableEntry;
import ket.kdb.TableReadonly;
import ket.kdb.Transaction;
import ket.kdb.Transaction.AutoInit;
import ket.kdb.Transaction.ErrorCode;
import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.MD5Digester;
import ket.util.SKVMap;
import ket.util.Stream;


public class SocialManager
{

	public SocialManager(GlobalServer gls)
	{
		this.gls = gls;
	}
	
	public static class SocialMsg
	{
		public SocialMsg()
		{
			
		}
		
		public SocialMsg(int cid, int id, long owner, String msg)
		{
			this.msgcid = cid;
			this.msgid = id;
			this.owner = owner;
			this.msg = msg;
			this.postTime = GlobalServer.getTime();
		}
		
		public SocialMsg(DBSocialMsg dbmsg)
		{
			msgcid = dbmsg.msgcid;
			msgid = (int)dbmsg.msgid;
			owner = dbmsg.owner;
			msg = dbmsg.msg.replaceAll("\n", "");
			postTime = dbmsg.postTime;
			liked.addAll(dbmsg.liked);
		}
		
		public DBSocialMsg toDBSocialMsg()
		{
			DBSocialMsg dbmsg = new DBSocialMsg();
			dbmsg.msgcid = this.msgcid;
			dbmsg.msgid = this.msgid;
			dbmsg.owner = this.owner;
			dbmsg.msg = this.msg;
			dbmsg.postTime = this.postTime;
			dbmsg.liked.addAll(this.liked);
			return dbmsg;
		}
		
		public boolean hasReceiveLike(long grid)
		{
			return liked.contains(grid);
		}
		
		public int receiveLike(long grid)
		{
			liked.add(grid);
			return liked.size();
		}
		
		public int msgcid;
		public int msgid;
		public long owner;
		public String msg;
		public int postTime;
		public Set<Long> liked = new TreeSet<Long>();
		public int linearIndex;
	}
	
	public static class SocialUser extends DBSocialUser
	{
		public SocialUser()
		{
			
		}
		
		public SocialUser(long grid, String gsname, String name)
		{
			this.grid = grid;
			this.gsname = gsname;
			this.name = name;
		}
		
		public void updateName(String gsname, String name)
		{
			this.gsname = gsname;
			this.name = name;
		}
		
		public SocialUser(DBSocialUser dbuser)
		{
			grid = dbuser.grid;
			gsname = dbuser.gsname;
			name = dbuser.name;
			level = dbuser.level;
			exp = dbuser.exp;
			postsCount = dbuser.postsCount;
			likesCount = dbuser.likesCount;
			likedCount = dbuser.likedCount;
			dayCommon = dbuser.dayCommon;
			dayPostCount = dbuser.dayPostCount;
			dayLikeCount = dbuser.dayLikeCount;
		}
		
		public DBSocialUser toDBSocialUser()
		{
			DBSocialUser dbuser = new DBSocialUser();
			dbuser.grid = this.grid;
			dbuser.gsname = this.gsname;
			dbuser.name = this.name;
			dbuser.level = this.level;
			dbuser.exp = this.exp;
			dbuser.postsCount = this.postsCount;
			dbuser.likesCount = this.likesCount;
			dbuser.likedCount = this.likedCount;
			dbuser.dayCommon = this.dayCommon;
			dbuser.dayPostCount = this.dayPostCount;
			dbuser.dayLikeCount = this.dayLikeCount;
			return dbuser;
		}
		
		public void addMsg()
		{
			dayRefresh();
			this.postsCount += 1;
			this.exp += 2;
			this.dayPostCount += 1;
		}
		
		public void likeOther()
		{
			dayRefresh();
			this.likesCount += 1;
			this.exp += 1;
			this.dayLikeCount += 1;
		}
		
		public void receiveLike()
		{
			this.likedCount += 1;
			this.exp = postsCount*2 + likesCount + likedCount/50;
		}
		
		public void dayRefresh()
		{
			int newDay = GlobalServer.getDay();
			if (newDay != this.dayCommon)
			{
				this.dayCommon = newDay;
				this.dayPostCount = 0;
				this.dayLikeCount = 0;
			}
		}
		
		public int getDayPostCount()
		{
			dayRefresh();
			return this.dayPostCount;
		}
		
		public int getDayLikeCount()
		{
			dayRefresh();
			return this.dayLikeCount;
		}
		
		public int getExp()
		{
			return this.exp;
		}
	}
	
	public static class ThemeMsgs
	{
		public ThemeMsgs(int id)
		{
			themeID = id;
		}
		
		public void addMsg(int cid, long grid, String msg)
		{
			int msgID = curID.incrementAndGet();
			if (msgID == 0)
				msgID = curID.incrementAndGet();
			SocialMsg smsg = new SocialMsg(cid, msgID, grid, msg);
			if (msgs.put(msgID, smsg) == null)
			{
				linearMsgs.add(smsg);
				smsg.linearIndex = linearMsgs.size()-1;
			}
			shrinkMsgs();
		}
		
		private void shrinkMsgs()
		{
			if (linearMsgs.size() >= HighWaterMark)
			{
				int shrinkCount = linearMsgs.size() - WaterMark;
				List<SocialMsg> shrinkMsgs = linearMsgs.subList(0,  shrinkCount);
				List<SocialMsg> retainMsgs = new ArrayList<>();
				for (SocialMsg e : shrinkMsgs)
				{
					if (topLikeIDMsgs.containsKey(e.msgid))
					{
						retainMsgs.add(e);
					}
					else
					{
						msgs.remove(e.msgid);
					}
				}
				for (int i = 0; i < retainMsgs.size(); ++i)
				{
					linearMsgs.set(shrinkCount-i-1, retainMsgs.get(retainMsgs.size()-i-1));
				}
				linearMsgs.subList(0,  shrinkCount-retainMsgs.size()).clear();
				int index = 0;
				for (SocialMsg e : linearMsgs)
				{
					e.linearIndex = index++;
				}
			}
		}
		
		public void setMsg(DBThemeMsg tmsg)
		{
			int maxMsgID = 0;
			for (DBSocialMsg m : tmsg.msgs)
			{
				SocialMsg smsg = new SocialMsg(m);
				if (msgs.put(smsg.msgid, smsg) == null)
				{
					linearMsgs.add(smsg);
					smsg.linearIndex = linearMsgs.size()-1;
				}
				refreshTopLike(smsg);
				if (smsg.msgid > maxMsgID)
					maxMsgID = smsg.msgid;
			}
			curID.set(maxMsgID);
		}
		
		public long getMsgOwner(int msgid)
		{
			long owner = 0;
			SocialMsg smsg = msgs.get(msgid);
			if (smsg != null)
				owner = smsg.owner;
			return owner;
		}
		
		public int getLikeCount(int msgid)
		{
			int likeCount = 0;
			SocialMsg msg = msgs.get(msgid);
			if (msg != null)
				likeCount = msg.liked.size();
			return likeCount;
		}
		
		public int receiveLike(long likegrid, int msgid)
		{
			int likeCount = 0;
			SocialMsg msg = msgs.get(msgid);
			if (msg == null)
				return likeCount;
			likeCount = msg.receiveLike(likegrid);
			refreshTopLike(msg);
			
			return likeCount;
		}
		
		private void refreshTopLike(SocialMsg msg)
		{
			if (msg.liked.isEmpty())
				return;
			if (topLikeIDMsgs.size() < TopLikeCount)
			{
				if (!topLikeIDMsgs.containsKey(msg.msgid))
				{
					topLikeIDMsgs.put(msg.msgid, msg);
					topLikeMsgs.add(msg);
					Collections.sort(topLikeMsgs, new Comparator<SocialMsg>()
							{
								public int compare(SocialMsg l, SocialMsg r)
								{
									return -(l.liked.size() - r.liked.size());
								}
							});	
				}
			}
			else
			{
				SocialMsg last = topLikeMsgs.get(TopLikeCount-1);
				if (msg.liked.size() >= last.liked.size())
				{
					if (!topLikeIDMsgs.containsKey(msg.msgid))
					{
						topLikeIDMsgs.remove(last.msgid);
						topLikeIDMsgs.put(msg.msgid, msg);
						topLikeMsgs.set(TopLikeCount-1, msg);
					}
					Collections.sort(topLikeMsgs, new Comparator<SocialMsg>()
								{
									public int compare(SocialMsg l, SocialMsg r)
									{
										return -(l.liked.size() - r.liked.size());
									}
								});	
				}
			}
		}
		
		public List<SocialMsg> getAllMsgs()
		{
			return linearMsgs;
		}
		
		public List<SocialMsg> getPageMsgs(int pageNo, int msgCountPerPage)
		{
			List<SocialMsg> pageMsgs = new ArrayList<SocialMsg>();
			if (pageNo < 1 || msgCountPerPage < TopLikeCount)
				return pageMsgs;
			int fromIndex = (pageNo-1)*msgCountPerPage - topLikeMsgs.size();
			int toIndex = fromIndex + msgCountPerPage;
			if (pageNo == 1)
			{
				fromIndex = 0;
				for (SocialMsg e : topLikeMsgs)
				{
					pageMsgs.add(msgs.get(e.msgid));
				}
			}
			int allMsgCount = linearMsgs.size();
			for (Map.Entry<Integer, SocialMsg> e : topLikeIDMsgs.descendingMap().entrySet())
			{
				int index = e.getValue().linearIndex;
				if (allMsgCount - 1 - index <= fromIndex)
				{
					fromIndex++;
					toIndex++;
				}
				else if (allMsgCount - 1 - index < toIndex)
				{
					toIndex++;
				}
			}
			
			if (fromIndex > allMsgCount)
				fromIndex = allMsgCount;
			if (toIndex > allMsgCount)
				toIndex = allMsgCount;
			for (int i = fromIndex; i < toIndex; ++i)
			{
				SocialMsg smsg = linearMsgs.get(allMsgCount-1-i);
				if (!topLikeIDMsgs.containsKey(smsg.msgid))
					pageMsgs.add(smsg);
			}
			return pageMsgs;
		}
		
		public int getPageCount(int msgCountPerPage)
		{
			return (msgCountPerPage - 1 + linearMsgs.size())/msgCountPerPage;
		}
		
		public int themeID;
		private AtomicInteger curID = new AtomicInteger();
		public NavigableMap<Integer, SocialMsg> msgs = new TreeMap<Integer, SocialMsg>();
		public List<SocialMsg> linearMsgs = new ArrayList<SocialMsg>();
		public final static int WaterMark = 10000;
		public final static int HighWaterMark = (int)(WaterMark*(1+0.2f));
		
		public final static int TopLikeCount = 3; 
		public NavigableMap<Integer, SocialMsg> topLikeIDMsgs = new TreeMap<Integer, SocialMsg>();
		public List<SocialMsg> topLikeMsgs = new ArrayList<SocialMsg>();
	}
	
	public static class DBSocialData
	{
		List<DBSocialUser> dbusers = new ArrayList<DBSocialUser>();
		Map<Integer, DBThemeMsg> dbmsgs = new TreeMap<Integer, DBThemeMsg>();
	}
	
	public synchronized DBSocialData copyDBData()
	{
		DBSocialData data = new DBSocialData();
		for (Map.Entry<Long, SocialUser> e : users.entrySet())
		{
			data.dbusers.add(e.getValue().toDBSocialUser());
		}
		for (Map.Entry<Integer, ThemeMsgs> e : msgs.entrySet())
		{
			DBThemeMsg tmsg = new DBThemeMsg();
			tmsg.cid = e.getKey();
			for (SocialMsg m : e.getValue().getAllMsgs())
			{
				tmsg.msgs.add(m.toDBSocialMsg());
			}
			data.dbmsgs.put(tmsg.cid, tmsg);
		}
		return data;
	}
	
	public void setUsers(Iterable<TableEntry<Long, DBSocialUser>> users)
	{
		for (TableEntry<Long, DBSocialUser> e : users)
		{
			this.users.put(e.getKey(), new SocialUser(e.getValue()));
		}
	}
	
	public void setMsgs(Iterable<TableEntry<Integer, DBThemeMsg>> msgs)
	{
		for (TableEntry<Integer, DBThemeMsg> e : msgs)
		{
			int cid = e.getKey();
			ThemeMsgs tmsgs = new ThemeMsgs(cid);
			tmsgs.setMsg(e.getValue());
			this.msgs.put(cid, tmsgs);
		}
	}
	
	public class SocialDataInitTrans implements Transaction
	{	
		public SocialDataInitTrans()
		{
		}

		@Override
		public boolean doTransaction()
		{	
			setUsers(user);
			setMsgs(msg);
 
			return true;
		}

		@Override
		public void onCallback(ErrorCode errcode)
		{
			if( errcode == ErrorCode.eOK )
			{
				gls.getLogger().info("social data init read success");
				gls.start();
			}
			else
			{
				gls.getLogger().error("social data init failed : " + errcode.toString());
			}
		}

		@AutoInit
		public TableReadonly<Long, DBSocialUser> user;
		@AutoInit
		public TableReadonly<Integer, DBThemeMsg> msg;
	
	}
	
	public class SaveSocialDataTrans implements Transaction
	{
		public SaveSocialDataTrans()
		{
			
		}

		@Override
		public boolean doTransaction()
		{
			gls.getLogger().info("save social data to DB...");
			DBSocialData data = copyDBData();
			for (DBSocialUser e : data.dbusers)
			{
				user.put(e.grid, e);
			}
			for (Map.Entry<Integer, DBThemeMsg> e : data.dbmsgs.entrySet())
			{
				msg.put(e.getKey(), e.getValue());
			}
			return true;
		}
		
		@AutoInit
		public Table<Long, DBSocialUser> user;
		@AutoInit
		public Table<Integer, DBThemeMsg> msg;
		
		@Override
		public void onCallback(ErrorCode errcode)
		{
			gls.getLogger().info("save social data to DB return " + errcode.toString());
		}
	}
	
	public void initReadDB()
	{
		gls.getDB().execute(new SocialDataInitTrans());
	}
	
	public void saveData()
	{
		gls.getDB().execute(new SaveSocialDataTrans());
	}
	
	public void onTimer(long timeNow, int timeTick)
	{
		if ( timeTick % 300 == 15)
		{
			saveData();
		}
	}
	
	private long getGlobalRoleID(int gsid, int rid)
	{
		return ((long)gsid << 32) | ((long)rid & 0xffffffffL);
	}
	
	private int getGameServerID(long grid)
	{
		return (int)((grid >> 32) & 0xffffffffL);
	}
	
	private int getRoleID(long grid)
	{
		return (int)(grid & 0xffffffffL);
	}
	
	public static class PostMsgInfo
	{
		public boolean selfPost;
		public int exp;
		public int daySelfPostCount;
	}
	
	public synchronized PostMsgInfo sendMsg(int gsid, int rid, String gsname, String name, int cid, String msg)
	{
		PostMsgInfo info = new PostMsgInfo();
		if (gls.getConfig().trustAllGS == 0 && gsid % 1000  >= 900)
			return info;
		if (gls.isQQDirtyWords(msg) || msg.indexOf("[") != -1)
			return info;
		msg = msg.replaceAll("\n", "");
		msg = msg.replaceAll("\r", "");
		ThemeMsgs tmsgs = msgs.get(cid);
		if (tmsgs == null)
		{
			tmsgs = new ThemeMsgs(cid);
			msgs.put(cid, tmsgs);
		}
		long grid = getGlobalRoleID(gsid, rid);
		SocialUser suser = users.get(grid);
		if (suser == null)
		{
			suser = new SocialUser(grid, gsname, name);
			users.put(grid, suser);
		}
		else
		{
			suser.updateName(gsname, name);
		}
		tmsgs.addMsg(cid, grid, msg);
		suser.addMsg();
		
		info.selfPost = true;
		info.exp = suser.getExp();
		info.daySelfPostCount = suser.getDayPostCount();
		return info;
	}
	
	public static class LikeMsgInfo
	{
		public long msgid;
		public boolean selflike;
		public int exp;
		public int daySelfLikeCount;
		public int likeCount;
	}
	
	public synchronized LikeMsgInfo likeMsg(int gsid, int rid, String gsname, String name, int cid, int msgid)
	{
		LikeMsgInfo info = new LikeMsgInfo(); 
		info.msgid = msgid;
		if (gsid % 1000  >= 900)
			return info;
		long grid = getGlobalRoleID(gsid, rid);
		SocialUser luser = users.get(grid);
		if (luser == null)
		{
			luser = new  SocialUser(grid, gsname, name);
			users.put(grid, luser);
		}
		else
		{
			luser.updateName(gsname, name);
		}
		ThemeMsgs tmsgs = msgs.get(cid);
		if (tmsgs == null)
		{
			tmsgs = new ThemeMsgs(cid);
			msgs.put(cid, tmsgs);
		}
		long ownerGrid = tmsgs.getMsgOwner(msgid);
		if (ownerGrid == 0)
			return info;
		SocialUser muser = users.get(ownerGrid);
		if (muser == null)
			return info;
		
		int beforeLikeCount = tmsgs.getLikeCount(msgid);
		int likeCount = tmsgs.receiveLike(grid, msgid);
		if (likeCount > beforeLikeCount)
		{
			muser.receiveLike();
			luser.likeOther();	
		}
		

		info.selflike = true;
		info.exp = luser.getExp();
		info.daySelfLikeCount = luser.getDayLikeCount();
		info.likeCount = likeCount;
		return info;
	}
	
	public static class SocialMsgInfo
	{
		public int gsid;
		public int rid;
		public String gsname;
		public String name;
		public int exp;
		public long msgid;
		public String msg;
		public int likeCount;
		public boolean selflike;
	}
	
	public static class PageMsgInfo
	{
		public int cid;
		public int pageNo;
		public int pageCount;
		public int msgCountPerPage;
		public List<SocialMsgInfo> msgs = new ArrayList<SocialMsgInfo>();
	}
	
	public synchronized PageMsgInfo getPageMsg(int gsid, int rid, int cid, int pageNo)
	{
		long grid = getGlobalRoleID(gsid, rid);
		PageMsgInfo info = new PageMsgInfo();
		info.cid = cid;
		info.pageNo = pageNo;
		info.msgCountPerPage = MsgCountPerPage;
		info.pageCount = 1;
		ThemeMsgs tmsgs = msgs.get(cid);
		if (tmsgs == null)
			return info;
		info.pageCount = tmsgs.getPageCount(MsgCountPerPage);
		List<SocialMsg> pageMsgs = tmsgs.getPageMsgs(pageNo, MsgCountPerPage);
		for (SocialMsg e : pageMsgs)
		{
			SocialMsgInfo minfo = new SocialMsgInfo();
			minfo.gsid = getGameServerID(e.owner);
			minfo.rid = getRoleID(e.owner);
			SocialUser suser = users.get(e.owner);
			if (suser != null)
			{
				minfo.gsname = suser.gsname;
				minfo.name = suser.name;
				minfo.exp = suser.exp;
			}
			else
			{
				minfo.gsname = "";
				minfo.name = "";
				minfo.exp = 0;
			}
			minfo.msgid = e.msgid;
			minfo.msg = e.msg;
			minfo.likeCount = e.liked.size();
			minfo.selflike = e.liked.contains(grid);
			info.msgs.add(minfo);
		}
		return info;
	}
	
	public static class SocialUserInfo
	{
		public int gsid;
		public int rid;
		public int exp;
		public int dayPostCount;
		public int dayLikeCount;
		
		SocialUserInfo(int gsid, int rid)
		{
			this.gsid = gsid;
			this.rid = rid;
		}
	}
	
	public synchronized SocialUserInfo getUserInfo(int gsid, int rid)
	{
		SocialUserInfo info = new SocialUserInfo(gsid, rid);
		long grid = getGlobalRoleID(gsid, rid);
		SocialUser muser = users.get(grid);
		if (muser == null)
			return info;
		muser.dayRefresh();
		info.exp = muser.exp;
		info.dayPostCount = muser.dayPostCount;
		info.dayLikeCount = muser.dayLikeCount;
		return info;
	}
	
	private GlobalServer gls;
	public NavigableMap<Long, SocialUser> users = new TreeMap<Long, SocialUser>();
	public NavigableMap<Integer, ThemeMsgs> msgs = new TreeMap<Integer, ThemeMsgs>();
	private final static int MsgCountPerPage = 4;
}

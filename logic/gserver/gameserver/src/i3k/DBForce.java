
package i3k;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ket.util.Stream;

public class DBForce implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public static final byte POS_KING = 1;
	public static final byte POS_QUEEN = 2;
	public static final byte POS_R1 = 3;
	public static final byte POS_R2 = 4;
	public static final byte POS_R3 = 5;
	public static final byte POS_NONE = 0;
	
	public static final byte JOIN_MODE_VERIFY = 0;
	public static final byte JOIN_MODE_ANYONE = 1;
	public static final byte JOIN_MODE_NOONE = 2;
	
	public DBForce() { }	
	
	public static class Member implements Stream.IStreamable
	{
		public Member() { }
		public Member(int id, int joinTime, int point, int contrib)
		{
			this.id = id;
			this.joinTime = joinTime;
			this.point = point;
			this.contrib = contrib;
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			joinTime = is.popInteger();
			point = is.popInteger();
			contrib = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(joinTime);
			os.pushInteger(point);
			os.pushInteger(contrib);
		}
		
		public int id;
		public int joinTime;
		public int point;
		public int contrib;
	}
	
	public static class Apply implements Stream.IStreamable
	{
		public Apply() { }
		public Apply(int id, int applyTime)
		{
			this.id = id;
			this.applyTime = applyTime;
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			applyTime = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(applyTime);
		}
		
		public int id;
		public int applyTime;
	}
	
	public static class History implements Stream.IStreamable
	{
		public History() { }
		public History(int time, String msg)
		{
			this.time = time;
			this.msg = msg;
		}
		
		public History logJoin(int time, String name, String op)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#join#");
			sb.append(name).append('#');
			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logQuit(int time, String name)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#quit#");
			sb.append(name).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logKick(int time, String name, String op,byte pos)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#kick#");
			sb.append(name).append('#');
			sb.append(pos).append('#');
			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logFinishChapter(int time, int chapterId)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#finishchapter#");
			sb.append(chapterId).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logResetChapter(int time, String name, byte pos,int chapterId)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#resetchapter#");
			sb.append(name).append('#');
			sb.append(pos).append('#');
			sb.append(chapterId).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logOpenBattle(int time,String name)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#openbattle#");
			sb.append(name).append('#');
//			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logForceActivity(int time,String name,int activity)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#forceactivity#");
			sb.append(name).append('#');
			sb.append(activity).append('#');
//			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logUpgrade(int time, byte newLvl, String op)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#upgrade#");
			sb.append(newLvl).append('#');
			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logChuanwei(int time, String name, String op)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#chuanwei#");
			sb.append(name).append('#');
			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		public History logSetpos(int time, String name, String op, byte oldPos, byte newPos)
		{
			this.time = time;
			StringBuilder sb = new StringBuilder("#setpos#");
			sb.append(oldPos).append('#');
			sb.append(newPos).append('#');
			sb.append(name).append('#');
			sb.append(op).append('#');
			msg = sb.toString();
			return this;
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			time = is.popInteger();
			msg = is.popString();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(time);
			os.pushString(msg);
		}
		
		public int time;
		public String msg;
	}
	
	public static class Manager implements Stream.IStreamable
	{
		public Manager()
		{			
		}
		
		public Manager(int id, byte pos)
		{
			this.id = id;
			this.pos = pos;
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			pos = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushByte(pos);
		}
		
		public int id;
		public byte pos;
	}

	public static class Activity implements Stream.IStreamable
	{
		public Activity() { }
		public Activity(int id, int activity)
		{
			this.id = id;
			this.activity = activity;
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			activity = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushInteger(activity);
		}
		
		public int id;
		public int activity;
	}
	
//	public static class Battle implements Stream.IStreamable
//	{
//		public Battle()
//		{
//			
//		}
//		
//		public Battle(int bid, List<BattlePlayer> players, List<DBRoleItem> contributions)
//		{
//			this.bid = bid;
//			this.players = players;
//			this.contributions = contributions;
//		}
//		
//		@Override
//		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
//		{
//			//int dbVersion = is.popInteger();
//			bid = is.popInteger();
//			players = is.popList(BattlePlayer.class);
//			contributions = is.popList(DBRoleItem.class);
//		}
//
//		@Override
//		public void encode(Stream.AOStream os)
//		{
//			//os.pushInteger(VERSION_NOW);
//			os.pushInteger(bid);
//			os.pushList(players);
//			os.pushList(contributions);
//		}
//		public int bid;
//		public List<BattlePlayer> players; 
//		public List<DBRoleItem> contributions;
//	}
//	
//	public static class BattlePlayer implements Stream.IStreamable
//	{
//		public BattlePlayer()
//		{
//			
//		}
//		
//		public BattlePlayer(int id, String name, int icon, int level, List<DBRoleGeneral> generals, List<DBPetBrief> pets)
//		{
//			this.id = id;
//			this.generals = generals;
//			this.pets = pets;
//		}
//		
//		@Override
//		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
//		{
//			id = is.popInteger();
//			name = is.popString();
//			icon = is.popInteger();
//			level = is.popInteger();
//			generals = is.popList(DBRoleGeneral.class);
//			pets = is.popList(DBPetBrief.class);
//		}
//
//		@Override
//		public void encode(Stream.AOStream os)
//		{
//			os.pushInteger(id);
//			os.pushString(name);
//			os.pushInteger(icon);
//			os.pushInteger(level);
//			os.pushList(generals);
//			os.pushList(pets);
//		}
//		public int id;
//		public String name;
//		public int icon;
//		public int level;
//		public List<DBRoleGeneral> generals;
//		public List<DBPetBrief> pets; 
//	}
	
	
	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		id = is.popInteger();
		dayCommon = is.popInteger();
		iconID = is.popShort();
		lvl = is.popByte();
		msg = is.popString();
		qq = is.popString();
		name = is.popString();
		headName = is.popString();
		point = is.popInteger();
		contrib = is.popInteger();
		upgradeTime = is.popInteger();
		lastModifyTime = is.popInteger();
		members = is.popList(Member.class);
		queue = is.popList(Apply.class);
		logs = is.popList(History.class);
				
		managers = is.popList(Manager.class);
		activities = is.popList(Activity.class);
		
		joinMode = is.popByte();
		joinLvlReq = is.popShort();

		sendMailCnt = is.popByte();
		
		beasts = is.popList(SBean.DBForceBeast.class);
		defaultBeast = is.popByte();
		
		dungeon = is.popList(SBean.DBForceDungeon.class);
		merc = is.popList(SBean.DBMerc.class);
		
		activity = is.popInteger();
		
		cupCount = is.popInteger();
		pad5 = is.popInteger();
		pad6 = is.popInteger();
		
		List<History> delHis = new ArrayList<History>();
		
		for(History history :logs){
			if(history.time< 1438974934){
				delHis.add(history);
			}
		}
		
		for (History history : delHis)
			logs.remove(history);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);
		os.pushInteger(id);
		os.pushInteger(dayCommon);
		os.pushShort(iconID);
		os.pushByte(lvl);
		os.pushString(msg);
		os.pushString(qq);
		os.pushString(name);
		os.pushString(headName);
		os.pushInteger(point);
		os.pushInteger(contrib);
		os.pushInteger(upgradeTime);
		os.pushInteger(lastModifyTime);
		os.pushList(members);
		os.pushList(queue);
		os.pushList(logs);
		os.pushList(managers);
		os.pushList(activities);
		
		os.pushByte(joinMode);
		os.pushShort(joinLvlReq);

		os.pushByte(sendMailCnt);
		
		os.pushList(beasts);
		os.pushByte(defaultBeast);
		
		os.pushList(dungeon);
		os.pushList(merc);
		
		os.pushInteger(activity);
		
		os.pushInteger(cupCount);
		os.pushInteger(pad5);
		os.pushInteger(pad6);
	}
	
	public boolean dayRefresh(int newDay)
	{
		if( dayCommon == newDay )
			return false;
		clearOldLogs(newDay);
		activities.clear();
		dayCommon = newDay;
		sendMailCnt = 0;
		return true;
	}
	
	public Member getMember(int rid)
	{
		for(Member m : members)
		{
			if( m.id == rid )
				return m;
		}
		return null;
	}
	
	public Manager getManager(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid )
				return m;
		}
		return null;
	}
	
	public int getManagerCount(byte pos)
	{
		int r = 0;
		for(Manager m : managers)
		{
			if( m.pos == pos )
				++r;
		}
		return r;
	}
	
	private void clearOldLogs(int day)	
	{
		Iterator<History> iter = logs.iterator();
		while( iter.hasNext() )
		{
			History h = iter.next();
			if( h.time/86400 + 7 < day )
				iter.remove();
		}
	}
	
	public void addLog(History log)
	{
		logs.add(log);
		if( logs.size() > 50 )
		{
			logs.remove(0);
		}
	}
	
	public List<History> copyLogs(int ltime)
	{
		List<History> lst = new ArrayList<History>();
		for(History h : logs)
		{
			if( h.time > ltime )
				lst.add(h);
		}
		return lst;
	}
	
	public boolean isMember(int rid)
	{
		return getMember(rid) != null;
	}	
	
	public boolean isKing(int rid)
	{
		return managers.get(0).id == rid;
	}
	
	public int getKing()
	{
		return managers.get(0).id;
	}
	
	public boolean canKick(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public byte getPos(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid )
				return m.pos;
		}
		return 0;
	}
	
	public boolean canAccept(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN || m.pos == POS_R1 || m.pos == POS_R2) )
				return true;
		}
		return false;
	}
	
	public boolean canUpgrade(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canSetPos(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModQQ(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModAnnounce(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModIcon(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModJoin(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModBeast(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canModDungeon(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean chuanwei(String newHeadName, int rid)
	{
		Manager ma = null;
		for(Manager e : managers)
		{
			if( e.pos == 2 && rid == e.id )
			{
				ma = e;
				break;
			}
		}
		if( ma == null )
			return false;
		managers.remove(ma);
		ma.pos = 1;
		managers.get(0).pos = 2;
		managers.add(0, ma);
		headName = newHeadName;
		return true;
	}
	
	public boolean canOpenBattle(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public boolean canChangeBattleSeq(int rid)
	{
		for(Manager m : managers)
		{
			if( m.id == rid && (m.pos == POS_KING || m.pos == POS_QUEEN) )
				return true;
		}
		return false;
	}
	
	public int id;
	public int dayCommon;
	public short iconID;
	public byte lvl;
	public String msg;
	public String qq;
	public String name;
	public String headName;
	public int point;
	public int contrib;
	public int upgradeTime;
	public int lastModifyTime;
	public List<Member> members;
	public List<Apply> queue;
	public List<History> logs;
	public List<Manager> managers;
	public List<Activity> activities;
	
	public byte joinMode;
	public short joinLvlReq;
	public byte sendMailCnt;
	
	public List<SBean.DBForceBeast> beasts;
	public byte defaultBeast;
	
	public List<SBean.DBForceDungeon> dungeon;
	public List<SBean.DBMerc> merc;
	public int activity;
	public int cupCount;
	public int pad5;
	public int pad6;
}
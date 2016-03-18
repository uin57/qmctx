
package i3k;

import i3k.gs.GameServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ket.util.Stream;

public class DBMail implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public static int MAX_RESERVE_TIME = 86400 * 30;
	public static int MAX_MAIL_COUNT = 100;
	public static int CLEAN_DEL_COUNT = 40;
	public static int SYNC_COUNT = 12;
	
	public DBMail()
	{
		msgs = new ArrayList<Message>();
	}
	
	public static class Message implements Stream.IStreamable
	{
		public static final byte TYPE_SYS = 1;
		public static final byte TYPE_WORLD = 2;
		public static final byte TYPE_USER = 3;
		
		public static final byte SUB_TYPE_GM_MAIL = 1;
		public static final byte SUB_TYPE_GIFT_TEST = 2;
		public static final byte SUB_TYPE_ALPHA_GIFT = 3;
		public static final byte SUB_TYPE_ALPHA_MAIL = 4;
		public static final byte SUB_TYPE_ARENA_REWARD = 5;
		public static final byte SUB_TYPE_NEWUSER_MAIL = 6;
		public static final byte SUB_TYPE_LOGIN_GIFT_MAIL = 7;
		public static final byte SUB_TYPE_FORCE_KICKED = 8;
		public static final byte SUB_TYPE_FORCE_BATTLE_REWARD = 9;
		public static final byte SUB_TYPE_FORCE_BATTLE_QUIZ_REWARD = 10;
		public static final byte SUB_TYPE_FORCE_BATTLE_HEAD_REWARD = 11;
		public static final byte SUB_TYPE_RICH_TIANNVSANHUA_GIFT = 12;
		public static final byte SUB_TYPE_RICH_REWARD = 13;
		public static final byte SUB_TYPE_RICH_LOTERY_REWARD = 14;
		public static final byte SUB_TYPE_QQ_VIP_REWARD = 15;
		public static final byte SUB_TYPE_FORCE_THIEF_REWARD = 16;
		public static final byte SUB_TYPE_ZAN_BACK = 17;
		public static final byte SUB_TYPE_GET_TREASURE = 18;
		public static final byte SUB_TYPE_DUNGEON_DAMAGE_REWARD = 19;
		public static final byte SUB_TYPE_DUNGEON_BEYOND_REWARD = 20;
		public static final byte SUB_TYPE_DUEL_REWARD = 21;
		public static final byte SUB_TYPE_EXPATRIATE_REWARD = 22;
		public static final byte SUB_TYPE_EXPATRIATEBOSS_REWARD = 23;
		public static final byte SUB_TYPE_FORCE_SWAR_REWARD = 24;
		public static final byte SUB_TYPE_FORCE_SWAR_VOTE = 25;
		public static final byte SUB_TYPE_FORCE_SWAR_ATTACK = 26;
		public static final byte SUB_TYPE_HEROESBOSS_REWARD = 27;
		public static final byte SUB_TYPE_DISK_BET = 28;
		public static final byte SUB_TYPE_DISK_BET_RMB = 29;
		public static final byte SUB_TYPE_TREASURE_MAP = 30;
		public static final byte SUB_TYPE_DISK_BET_HAOHUA = 31;
		public static final byte SUB_TYPE_HEAD_ICON = 32;
		public static final byte SUB_TYPE_HEAD_ICON_DELETE = 33;
		public static final byte SUB_TYPE_BE_MONSTER_SEND_BACK = 34;
		public static final byte SUB_TYPE_BE_MONSTER_ATTACKER_REWARD = 35;
		public static final byte SUB_TYPE_BE_MONSTER_BOSS_NOT_DIE = 36;
		public static final byte SUB_TYPE_BE_MONSTER_BOSS_KILL_ROLE_REWARD = 37;
		public static final byte SUB_TYPE_BE_MONSTER_RANK_REWARD = 38;
		
		public Message()
		{
			id = 0;
			type = 0;
			attLst = new ArrayList<SBean.DropEntryNew>();
		}
		
		public Message(short id, byte type, byte subType, byte state, int sendTime, int delTime
				, int srcID, String srcName, String title, String content, List<SBean.DropEntryNew> attLst)
		{
			this.id = id;
			this.type = type;
			this.subType = subType;
			this.state = state;
			this.sendTime = sendTime;
			this.delTime = delTime;
			this.srcID = srcID;
			this.srcName = srcName;
			this.title = title;
			this.content = content;
			this.attLst = attLst;
		}
		
		public Message kdclone()
		{
			return new Message(id, type, subType, state, sendTime, delTime
					, srcID, srcName, title, content, new ArrayList<SBean.DropEntryNew>(attLst));
		}
		
		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popShort();
			type = is.popByte();
			subType = is.popByte();
			state = is.popByte();
			sendTime = is.popInteger();
			delTime = is.popInteger();
			srcID = is.popInteger();
			srcName = is.popString();
			title = is.popString();
			content = is.popString();
			attLst = is.popList(SBean.DropEntryNew.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushShort(id);
			os.pushByte(type);
			os.pushByte(subType);
			os.pushByte(state);
			os.pushInteger(sendTime);
			os.pushInteger(delTime);
			os.pushInteger(srcID);
			os.pushString(srcName);
			os.pushString(title);
			os.pushString(content);
			os.pushList(attLst);
		}

		public short id;		// unique id
		public byte type;		// 1: sys 2: announce
		public byte subType;	// depends type
		public byte state;		// 0: unread, 1: read
		public int sendTime;	// send time
		public int delTime;		// delete time
		public int srcID;		//
		public String srcName;
		public String title;	// title
		public String content;	// content
		public List<SBean.DropEntryNew> attLst;
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = -is.popInteger();
		idSeed = is.popShort();
		worldMailID = is.popInteger();
		msgs = is.popList(Message.class);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(-VERSION_NOW);
		os.pushShort(idSeed);
		os.pushInteger(worldMailID);
		os.pushList(msgs);
	}
	
	public DBMail copy(int now)
	{
		DBMail o = new DBMail();
		o.idSeed = idSeed;
		o.worldMailID = worldMailID;
		o.msgs = new ArrayList<Message>();
		doClean(now);
		for(Message m : msgs)
		{
			o.msgs.add(m.kdclone());
		}
		return o;
	}
	
	private void doClean(int now)
	{
		Iterator<Message> iter = msgs.iterator();
		while( iter.hasNext() )
		{
			Message m = iter.next();
			if( m.delTime > 0 && now >= m.delTime 
					|| m.sendTime + MAX_RESERVE_TIME < now )
			{
				iter.remove();
			}
		}
		if( msgs.size() > MAX_MAIL_COUNT )
		{
			msgs.subList(0, CLEAN_DEL_COUNT).clear();
		}
	}
	
	public static class MailAttachments
	{
		public List<SBean.DropEntryNew> attLst = new ArrayList<SBean.DropEntryNew>();
		public int mailSubType;
	}
	
	public int setRead(short id, MailAttachments att)
	{
		Iterator<DBMail.Message> iter = msgs.iterator();
		while( iter.hasNext() )
		{
			DBMail.Message m = iter.next();
			if( m.id == id && m.state == 0 )
			{
				m.state = 1;
				boolean bDel = false;
				if( m.delTime == 0 )
				{
					iter.remove();
					bDel = true;
				}
				if( m.type != Message.TYPE_WORLD )
				{
					att.attLst.addAll(m.attLst);
					att.mailSubType = m.subType;
					return bDel ? 2 : 1;
				}
				else
				{
					if( bDel )
						return -m.srcID;
					return 1;
				}
			}
		}		
		return 0;
	}
	
	public int readContent(short id, List<String> contents, MailAttachments att)
	{
		Iterator<DBMail.Message> iter = msgs.iterator();
		while( iter.hasNext() )
		{
			DBMail.Message m = iter.next();
			if( m.id == id )
			{
				if( m.type == Message.TYPE_SYS || m.type == Message.TYPE_USER)
				{
					att.attLst.addAll(m.attLst);
					att.mailSubType = m.subType;
					contents.add(m.content);
					return 0;
				}
				if( m.type == Message.TYPE_WORLD )
				{
					return m.srcID;
				}
				return -1;
			}
		}		
		return -1;
	}
	
	public void addUserMessage(String sendRoleName, String title, String content, List<SBean.DropEntryNew> attLst, GameServer gs, int lifeTime)
	{
		short newid = ++idSeed;
		int now = gs.getTime();
		int sendTime = now;
		int delTime = (lifeTime == 0) ? 0 : (sendTime + lifeTime);
		DBMail.Message m = new DBMail.Message(newid, Message.TYPE_USER, (byte)0, (byte)0, sendTime, delTime, 0, sendRoleName, title, content, attLst);
		msgs.add(m);
		doClean(now);
	}
	
	public void addSysMessage(byte subType, String title, String content, List<SBean.DropEntryNew> attLst, GameServer gs, int lifeTime)
	{
		short newid = ++idSeed;
		int now = gs.getTime();
		int sendTime = now;
		int delTime = (lifeTime == 0) ? 0 : (sendTime + lifeTime);
		DBMail.Message m = new DBMail.Message(newid, Message.TYPE_SYS, subType, (byte)0, sendTime, delTime
				, 0, "", title, content, attLst);
		msgs.add(m);
		doClean(now);
	}
	
	public void addWorldMail(SBean.SysMail m)
	{
		short newid = ++idSeed;
		int sendTime = m.timeStart;
		int delTime = m.timeStart + m.lifeTime;
		if( m.flag == 1 )
			delTime = 0;
		//
		DBMail.Message newMail = new DBMail.Message(newid, Message.TYPE_WORLD, (byte)0, (byte)0, sendTime, delTime
				, m.id, "", m.title, "", new ArrayList<SBean.DropEntryNew>());
		msgs.add(newMail);
	}
	
	/*
	public static String encodeSysMessageAddGift(Role.MailGiftInfo info)
	{
		StringBuilder sb = new StringBuilder("#gift#");
		sb.append(info.type).append('#');
		sb.append(info.id).append('#');
		sb.append(info.cnt).append('#');
		return sb.toString();
	}
	*/
	
	public boolean testNewMail()
	{
		for(DBMail.Message e : msgs)
		{
			if( e.state == 0 )
			{
				return true;
			}
		}
		return false;
	}
	
	public List<DBMail.Message> syncMails(int now) // sync to client
	{
		doClean(now);
		List<DBMail.Message> lst = new ArrayList<DBMail.Message>();
		int n = msgs.size();
		for(int i = n-1; i>=0; --i)
		{
			DBMail.Message e = msgs.get(i);
			if( e.state == 0 )
			{
				lst.add(e.kdclone());
				//TODO
				if( lst.size() >= 10 )
					break;
			}
		}
		for(int i = n-1; i>=0; --i)
		{
			DBMail.Message e = msgs.get(i);
			if( e.state == 1 )
			{
				lst.add(e.kdclone());
				//TODO
				if( lst.size() >= 10 )
					break;
			}
		}
		return lst;
	}

	public short idSeed;
	public int worldMailID;
	public List<Message> msgs;
}

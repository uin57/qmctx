package i3k;

import java.util.List;
import java.util.ArrayList;

import ket.util.Stream;

public class DBSocialMsg implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public DBSocialMsg() { }

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		
		msgcid = is.popInteger();
		msgid = is.popLong();
		owner = is.popLong();
		msg = is.popString();
		postTime = is.popInteger();
		liked = is.popLongList();
		
		
		padding1 = is.popInteger();
		padding2 = is.popInteger();
		padding3 = is.popInteger();
		padding4 = is.popInteger();
		padding5 = is.popInteger();
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);
		
		os.pushInteger(msgcid);
		os.pushLong(msgid);
		os.pushLong(owner);
		os.pushString(msg);
		os.pushInteger(postTime);
		os.pushLongList(liked);

		os.pushInteger(padding1);
		os.pushInteger(padding2);
		os.pushInteger(padding3);
		os.pushInteger(padding4);
		os.pushInteger(padding5);
	}
	
	public int msgcid;
	public long msgid;
	public long owner;
	public String msg;
	public int postTime;
	public List<Long> liked = new ArrayList<Long>();
	
	public int padding1;
	public int padding2;
	public int padding3;
	public int padding4;
	public int padding5;
}
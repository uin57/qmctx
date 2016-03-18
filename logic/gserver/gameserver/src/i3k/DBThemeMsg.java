package i3k;

import java.util.List;
import java.util.ArrayList;

import ket.util.Stream;

public class DBThemeMsg implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public DBThemeMsg() { }

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		
		cid = is.popInteger();
		msgs = is.popList(DBSocialMsg.class);
		
		
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
		
		os.pushInteger(cid);
		os.pushList(msgs);

		os.pushInteger(padding1);
		os.pushInteger(padding2);
		os.pushInteger(padding3);
		os.pushInteger(padding4);
		os.pushInteger(padding5);
	}
	
	public int cid;
	public List<DBSocialMsg> msgs = new ArrayList<DBSocialMsg>();
	
	public int padding1;
	public int padding2;
	public int padding3;
	public int padding4;
	public int padding5;
}
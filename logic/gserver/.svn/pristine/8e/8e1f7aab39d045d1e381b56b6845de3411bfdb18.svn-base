package i3k;

import java.util.List;

import ket.util.Stream;

public class DBSocialUser implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public DBSocialUser() { }


	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		
		grid = is.popLong();
		gsname = is.popString();
		name = is.popString();
		level = is.popInteger();
		exp = is.popInteger();
		postsCount = is.popInteger();
		likesCount = is.popInteger();
		likedCount = is.popInteger();
		dayCommon = is.popInteger();
		dayPostCount = is.popInteger();
		dayLikeCount = is.popInteger();
		
		
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
		
		os.pushLong(grid);
		os.pushString(gsname);
		os.pushString(name);
		os.pushInteger(level);
		os.pushInteger(exp);
		os.pushInteger(postsCount);
		os.pushInteger(likesCount);
		os.pushInteger(likedCount);
		os.pushInteger(dayCommon);
		os.pushInteger(dayPostCount);
		os.pushInteger(dayLikeCount);

		
		os.pushInteger(padding1);
		os.pushInteger(padding2);
		os.pushInteger(padding3);
		os.pushInteger(padding4);
		os.pushInteger(padding5);
	}
	
	public long grid;
	public String gsname;
	public String name;
	public int level;
	public int exp;
	public int postsCount;
	public int likesCount;
	public int likedCount;
	public int dayCommon;
	public int dayPostCount;
	public int dayLikeCount;
	
	public int padding1;
	public int padding2;
	public int padding3;
	public int padding4;
	public int padding5;
}
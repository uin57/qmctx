
package i3k;

import ket.util.Stream;

public class DBUser implements Stream.IStreamable
{

	public DBUser() { }

	public DBUser(int roleID, int createTime, int lastLoginTime)
	{
		this.roleID = roleID;
		this.createTime = createTime;
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		roleID = is.popInteger();
		createTime = is.popInteger();
		lastLoginTime = is.popInteger();
		lastLoginKey = is.popString();
		nouse = is.popByte();
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(roleID);
		os.pushInteger(createTime);
		os.pushInteger(lastLoginTime);
		os.pushString(lastLoginKey);
		os.pushByte(nouse);
	}

	public int roleID;
	public int createTime;
	public int lastLoginTime;
	public String lastLoginKey = "";
	public byte nouse;
}
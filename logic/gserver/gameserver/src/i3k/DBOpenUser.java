package i3k;

import java.util.List;
import ket.util.Stream;

public class DBOpenUser implements Stream.IStreamable
{
	public static int VERSION_NOW = 1;
	
	public DBOpenUser() { }

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		int dbVersion = is.popInteger();
		
		records = is.popList(SBean.GlobalRoleID.class);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);

		os.pushList(records);
	}
	
	public List<SBean.GlobalRoleID> records;
}
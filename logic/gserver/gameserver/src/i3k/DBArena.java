
package i3k;

import i3k.SBean.DBArenaRank;
import ket.util.Stream;

public class DBArena implements Stream.IStreamable
{
	public static int VERSION_NOW = 3;
	
	public DBArena() { }

	public DBArena(DBArenaRank history, DBArenaRank current)
	{
		this.history = history;
		this.current = current;
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		//int dbVersion = 
		is.popInteger();
		if( history == null )
			history = new DBArenaRank();
		is.pop(history);
		if( current == null )
			current = new DBArenaRank();
		is.pop(current);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(VERSION_NOW);
		os.push(history);
		os.push(current);
	}

	public DBArenaRank history;
	public DBArenaRank current;
}

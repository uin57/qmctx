
package ket.kdb;

final class CloneOnWrite
{
	public void reset(final PageBuffer ref, final int count)
	{
		readerCount = count;
		this.ref = count > 0 ? ref : null;
	}
	
	public synchronized void readerRelease()
	{
		if( readerCount > 0 )
		{
			readerCount--;
			if( readerCount == 0 )
			{
				//System.out.println("release and set null " + ref.range);
				ref = null;
			}
		}
	}
	
	public synchronized void write(final PageBuffer pb)
	{
		if( readerCount > 0 && pb == ref )
		{
			//System.out.println("write after clone " + ref.range);
			ref = ref.clonePageBuffer();
		}
	}
	
	public synchronized void save(final DBFile dbFile, final long offset, final Range range)
	{
		ref.save(dbFile, offset, range);
	}
	
	public Range getRange()
	{
		return ref.range();
	}
	
	private int readerCount = 0;
	private PageBuffer ref = null;
}

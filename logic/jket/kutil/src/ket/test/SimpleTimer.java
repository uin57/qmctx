
package ket.test;

public class SimpleTimer
{
	public SimpleTimer()
	{		
	}
	public SimpleTimer(String name)
	{ 
		this.name = name;
	}
	
	public synchronized void done()
	{
		if( name == null )
			name = this.toString();
		if( bDone )
			throw new Error(name + "has done!");
		System.out.println(name + " done, time used: " + (System.currentTimeMillis() - timeStart) + " ms.");
		bDone = true;
	}
	
	private String name;
	private long timeStart = System.currentTimeMillis();
	private boolean bDone = false;
}

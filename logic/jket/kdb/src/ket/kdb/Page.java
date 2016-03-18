
package ket.kdb;

class Page
{
	public static final int SIZE = 4096;
	
	public static final int getCount(int dataLength) // a better method name?
	{
		return (dataLength + SIZE - 1 )/ SIZE;
	}
}

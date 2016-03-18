
package ket.kdb;

class Address
{
	public Address()
	{			
	}
	
	public Address(final Address other)
	{
		set(other);
	}
	
	public final boolean isNull()
	{
		return pageIndex == 0;
	}
	
	public final void setNull()
	{
		pageIndex = 0;
	}
	
	public Range getPageRange()
	{
		return new Range(pageIndex, pageIndex + ( pageOffset + dataLength + Page.SIZE - 1)/Page.SIZE);
	}
	
	public final void set(final Address other)
	{
		pageIndex = other.pageIndex;
		pageOffset = other.pageOffset;
		dataLength = other.dataLength;
	}
	
	public final void advance(int distance)
	{
		int newPageOffset = pageOffset + distance;
		if( dataLength == 0 ) // TODO
		{
			while( newPageOffset > Page.SIZE )
			{
				newPageOffset -= Page.SIZE;
				pageIndex++;
			}
		}
		else
		{
			while( newPageOffset >= Page.SIZE )
			{
				newPageOffset -= Page.SIZE;
				pageIndex++;
			}
		}
		pageOffset = (short)newPageOffset;
	}
	
	public final void disadvance(int distance)
	{
		int newPageOffset = (int)pageOffset - distance;		
		while( newPageOffset < 0 )
		{
			newPageOffset += Page.SIZE;
			pageIndex--;
		}
		pageOffset = (short)newPageOffset;
	}
	
	public int pageIndex;
	public short pageOffset;
	public int dataLength;
	
}
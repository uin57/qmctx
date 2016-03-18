
package ket.kdb;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class Range implements Iterable<Integer>
{
	public Range(final int from, final int to)
	{
		if( from > to )
			throw new Error();
		this.from = from;
		this.to = to;
	}
	
	public int size()
	{
		return to - from;
	}	
	
	public boolean isEmpty()
	{
		return from == to;
	}
	
	public boolean contains(final Range other)
	{
		return from <= other.from && to >= other.to;
	}
	
	public boolean contains(final int i)
	{
		return from <= i && i < to;
	}
	
	public boolean intersect(final Range other)
	{
		for(int i : other)
			if( contains(i) )
				return true;
		return false;
	}
	
	public Range add(int d)
	{
		return new Range(from + d, to + d);
	}
	
	public Range div(int i)
	{
		return new Range(from / i, ( to + i - 1)/i);
	}	
	
	@Override
	public boolean equals(Object other)
	{
		return other != null && from == ((Range)other).from && to == ((Range)other).to;
	}
	
	@Override
	public String toString()
	{
		return "[" + from + ", " + to + ")";
	}
	
	private class RangeIterator implements Iterator<Integer>
	{
		@Override
		public boolean hasNext()
		{
			return index < to;
		}

		@Override
		public Integer next()
		{
			if( index >= to )
				throw new NoSuchElementException();
			int i = index++;
			return i;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
		private int index = from;
	}
	
	@Override
	public Iterator<Integer> iterator()
	{
		return new RangeIterator();
	}
	
	public final int from;
	public final int to;
}

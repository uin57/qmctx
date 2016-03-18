
package ket.test;

public class Assert
{
	public static class AssertFailedError extends Error
	{
		private static final long serialVersionUID = 3182560117660145118L;
		public AssertFailedError() { super(); }
		public AssertFailedError(String msg) { super(msg); }
		public AssertFailedError(Throwable cause) { super(cause); }
		public AssertFailedError(String msg, Throwable cause) { super(msg, cause); }
	}
	
	public static void assertTrue(boolean b)
	{
		if( ! b ) throw new AssertFailedError();
	}
	public static void assertTrue(boolean b, String msg)
	{
		if( ! b ) throw new AssertFailedError(msg);
	}
	public static void assertFalse(boolean b)
	{
		assertTrue(! b);
	}
	public static void assertFalse(boolean b, String msg)
	{
		assertTrue(! b, msg);
	}
	public static void assertSame(Object tom, Object jerry)
	{
		assertTrue(tom == jerry, "(" + tom + ", " + jerry + ")");
	}
	public static void assertNotSame(Object tom, Object jerry)
	{
		assertTrue(tom != jerry, "(" + tom + ", " + jerry + ")");
	}
	public static void assertNull(Object o)
	{
		assertTrue(o == null);
	}
	public static void assertNotNull(Object o)
	{
		assertTrue(o != null);
	}
	public static void assertEqual(byte tom, byte jerry)
	{
		assertTrue(tom == jerry);
	}
	public static void assertEqual(char tom, char jerry)
	{
		assertTrue(tom == jerry);
	}
	public static void assertEqual(short tom, short jerry)
	{
		assertTrue(tom == jerry);
	}
	public static void assertEqual(int tom, int jerry)
	{
		assertTrue(tom == jerry);
	}
	public static void assertEqual(long tom, long jerry)
	{
		assertTrue(tom == jerry);
	}
	public static void assertNotEqual(byte tom, byte jerry)
	{
		assertTrue(tom != jerry);
	}
	public static void assertNotEqual(char tom, char jerry)
	{
		assertTrue(tom != jerry);
	}
	public static void assertNotEqual(short tom, short jerry)
	{
		assertTrue(tom != jerry);
	}
	public static void assertNotEqual(int tom, int jerry)
	{
		assertTrue(tom != jerry);
	}
	public static void assertNotEqual(long tom, long jerry)
	{
		assertTrue(tom != jerry);
	}
	public static void assertEqual(Object tom, Object jerry)
	{
		assertTrue(tom.equals(jerry));
	}
}

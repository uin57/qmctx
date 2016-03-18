
package ket.util;

public class Dump
{
	public static void dump(byte[] data, int offset, int len, String sym, boolean bHex, boolean bStdout)
	{
		// todo1 ignore bHex and bStdout now, both true
		
		final int nbpg = 4;
		final int ngpl = 4;
		if( data == null || len == 0 )
			return;
		if( sym != null )
			System.out.print(sym + "  ");
		System.out.print(len + " bytes:");
		for(int i = 0; i< len; ++i)
		{
			if( (i%(nbpg*ngpl)) == 0 )
				System.out.println();
			if( (i%nbpg) == 0 )
				System.out.print("  ");
			String hs = Integer.toHexString(data[i]);
			if( hs.length() == 1 )
				hs = "0" + hs;
			System.out.print(hs);
		}
		System.out.println();
	}
}

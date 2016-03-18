
package ket.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Digester implements IDigester
{

	@Override
	public byte[] digest(byte[] data, int offset, int len)
	{
		if( data == null || offset < 0 || offset > data.length || len < 0 || offset + len > data.length )
			return null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data, offset, len);
			return md.digest();
		}
		catch(NoSuchAlgorithmException ex)
		{
			return null;
		}
	}

}

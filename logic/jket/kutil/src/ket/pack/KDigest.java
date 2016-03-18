
package ket.pack;

import java.util.Arrays;

public class KDigest implements Cloneable
{
	@Override
	public boolean equals(Object obj)
	{
		KDigest kd = (KDigest)obj;
		if( kd == this )
			return true;
		if( kd == null )
			return false;
		return datalen == kd.datalen &&
			( digest == kd.digest || digest != null && Arrays.equals(digest, kd.digest) );
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		KDigest kd = (KDigest)super.clone();
		if( this.digest != null )
			kd.digest = Arrays.copyOf(this.digest, this.digest.length);
		return kd;
	}
	
	@Override
	public int hashCode()
	{
		int hash = datalen;
		if( digest != null )
		{
			for(int i= 0; i<digest.length; i+=4)
			{
				byte b1 = digest[i];
				byte b2 = digest[i+1];
				byte b3 = digest[i+2];
				byte b4 = digest[i+3];
				int j = ( ((int)b4) <<24 )|( ((int)b3<<16)&0xffffff )|( ((int)b2<<8)&0xffff )| ( ((int)(b1))&0xff );
				hash ^= j;
			}
		}
		return hash;
	}
	
	public KDigest()
	{
	}
	
	public KDigest(int datalen, byte[] digest)
	{
		this.datalen = datalen;
		this.digest = digest;
	}
	
	public KDigest(byte[] digest)
	{
		this.digest = digest;
	}
	
	public int datalen = 0;
	public byte[] digest = null;
}

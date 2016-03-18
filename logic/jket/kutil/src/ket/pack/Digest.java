
package ket.pack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import ket.util.Stream;

public class Digest implements Stream.IStreamable, Cloneable
{
	public final static int DIGEST_LENGTH = 16;
	
	public Digest()
	{		
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
	
	@Override
	public boolean equals(Object obj)
	{
		Digest dd = (Digest)obj;
		if( dd == this )
			return true;
		if( dd == null )
			return false;
		return datalen == dd.datalen &&
			( digest == dd.digest || digest != null && Arrays.equals(digest, dd.digest) );
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Digest d = (Digest)super.clone();
		if( this.digest != null )
			d.digest = Arrays.copyOf(this.digest, this.digest.length);
		return d;
	}
	
	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		datalen = is.popInteger();
		if( datalen < 0 )
			throw new Stream.DecodeException();
		if( digest == null )
			digest = new byte[DIGEST_LENGTH];
		is.popBytes(digest.length, digest);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushInteger(datalen);
		if( digest == null )
			digest = new byte[DIGEST_LENGTH];
		os.pushBytes(digest);
	}
	
	public Digest(byte[] digest, int datalen)
	{
		this.digest = digest;
		this.datalen = datalen;
	}
	
	public byte[] getBytes()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Stream.AOStream aos = new Stream.OStreamLE(baos);
		aos.pushInteger(datalen);
		aos.pushBytes(digest);
		return baos.toByteArray();
	}
	
	public KDigest getKDigest()
	{
		return new KDigest(datalen, getBytes());
	}
	
	public Digest(KDigest kd)
	{
		if( kd == null || kd.digest == null )
			return;
		try
		{
			Stream.AIStream ais = new Stream.IStreamLE(new ByteArrayInputStream(kd.digest));
			datalen = ais.popInteger();
			digest = ais.popBytes(Digest.DIGEST_LENGTH);
		}
		catch(Exception ex)
		{
		}
	}
	
	public Digest(byte[] bs)
	{
		if( bs == null )
			return;
		try
		{
			Stream.AIStream ais = new Stream.IStreamLE(new ByteArrayInputStream(bs));
			datalen = ais.popInteger();
			digest = ais.popBytes(DIGEST_LENGTH);
		}
		catch(Exception ex)
		{
		}
	}
	
	public int datalen = 0;
	public byte[] digest = null;
}

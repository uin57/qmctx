
package ket.pack;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ket.util.IDigester;
import ket.util.MD5Digester;
import ket.util.Storage;
import ket.util.WrappedMemoryDataStorage;
import ket.util.FileDataSource;

public class DigestTool
{
	public static final int DEFAULT_PIECESIZE = 0;
	public final static int DIGEST_PIECE_SIZE = 8 * 1024 * 1024;
	
	public static IDigester getDigester()
	{
		return new MD5Digester();
	}
	
	public KDigest digest(ByteBuffer bb)
	{
		Digest d = digestbb(bb);
		return ( d == null ) ? null : d.getKDigest();
	}
	
	public boolean digest(byte[] data, int offset, int count, int piecesize, List<KDigest> digest)
	{
		if( digest == null )
			return false;
		List<Digest> lst = digest(data, offset, count, piecesize);
		if( lst == null )
			return false;
		digest.clear();
		for(Digest dd : lst)
		{
			digest.add(dd.getKDigest());
		}
		return true;
	}

	public boolean digest(String fn, int piecesize, List<KDigest> digest)
	{
		if( digest == null )
			return false;
		List<Digest> lst = digest(fn, piecesize);
		if( lst == null )
			return false;
		digest.clear();
		for(Digest dd : lst)
		{
			digest.add(dd.getKDigest());
		}
		return true;		
	}
	
	public Digest digestbb(ByteBuffer bb)
	{
		if( bb == null )
			return null;
		
		Digest d = new Digest();
		d.digest = getDigester().digest(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
		d.datalen = bb.remaining();
		return d;
	}
	
	public List<Digest> digest(byte[] data, int offset, int count, int piecesize)
	{
		if( data == null || offset < 0 || offset > data.length || count < 0 || offset + count > data.length )
			return null;
		if( piecesize <= 0 )
			piecesize = DIGEST_PIECE_SIZE;
		List<Digest> lst = new ArrayList<Digest>();
		if( count == 0 )
			return lst;
		WrappedMemoryDataStorage storage = new WrappedMemoryDataStorage(data, offset, count);
		//
		if( ! storage.isOK() )
			return null;
		byte[] buf = new byte[piecesize];
		long start = 0;
		while( start < storage.getLength() )
		{
			int len = piecesize;
			if( start + len > storage.getLength() )
				len = (int)(storage.getLength() - start);
			ByteBuffer buffer = storage.read(start, len, buf);
			if( buffer == null )
				return null;
			Digest dd = new Digest();
			dd.digest = getDigester().digest(buffer.array(), buffer.arrayOffset() + buffer.position(), len);
			dd.datalen = len;
			lst.add(dd);
			start += len;
		}
		//
		return lst;
	}
	
	public List<Digest> digest(String fn, int piecesize)
	{
		if( fn == null )
			return null;
		if( piecesize <= 0 )
			piecesize = DIGEST_PIECE_SIZE;
		List<Digest> lst = new ArrayList<Digest>();
		FileDataSource storage = new FileDataSource(fn);
		//
		if( ! storage.isOK() )
			return null;
		if( storage.getLength() == 0 )
			return lst;
		byte[] buf = new byte[piecesize];
		long start = 0;
		while( start < storage.getLength() )
		{
			int len = piecesize;
			if( start + len > storage.getLength() )
				len = (int)(storage.getLength() - start);
			ByteBuffer buffer = storage.read(start, len, buf);
			if( buffer == null )
				return null;
			Digest dd = new Digest();
			dd.digest = getDigester().digest(buffer.array(), buffer.arrayOffset() + buffer.position(), len);
			dd.datalen = len;
			lst.add(dd);
			start += len;
		}
		//
		return lst;
	}
	
	public boolean verify(Storage storage, Digest digest)
	{
		if( storage == null || digest == null )
			return false;
		if( ! storage.isOK() || storage.getLength() != digest.datalen )
			return false;
		ByteBuffer buffer = storage.read(0, digest.datalen, null);
		if( buffer == null )
			return false;
		if( ! Arrays.equals(getDigester().digest(buffer.array(), buffer.arrayOffset() + buffer.position(), digest.datalen), digest.digest) )
			return false;
		return true;
	}
}

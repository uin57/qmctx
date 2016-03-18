
package ket.pack;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import ket.util.Stream;

public class IndexData implements Stream.IStreamable
{
	
	private static final int VERSION = 3;
	
	public static class PieceInfo
	{
		public static final byte PCRY_NULL = 0;
		public static final byte PCRY_ZIP = 1;
		
		public PieceInfo()
		{			
		}
		
		public PieceInfo(KDigest digest)
		{
			this.crypttype = PCRY_NULL;
			this.size = digest.datalen;
			this.digest = digest;
		}
		
		public PieceInfo(byte crypttype, int size, KDigest digest)
		{
			this.crypttype = crypttype;
			this.size = size;
			this.digest = digest;
		}
		
		public byte crypttype;
		public int size;
		public KDigest digest;
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{	
		int version = is.popInteger();
		switch( version )
		{
		case VERSION:
			{
				bIgnoreCase = is.popBoolean();
				int n = is.popInteger();
				pieces.clear();
				for(int i = 0; i < n; ++i)
				{
					PieceInfo pi = new PieceInfo();
					pi.crypttype = is.popByte();
					if( pi.crypttype != PieceInfo.PCRY_NULL )
						pi.size = is.popInteger();					
					pi.digest = new KDigest();
					pi.digest.datalen = is.popInteger();
					int l = is.popSizeT();
					pi.digest.digest = is.popBytes(l);
					if( pi.crypttype == PieceInfo.PCRY_NULL )
						pi.size = pi.digest.datalen;
					pieces.add(pi);
				}
				mapp2d.clear();
				n = is.popInteger();
				for(int i=0; i<n; ++i)
				{
					String k = is.popString();
					int lstsize = is.popSizeT();
					List<Integer> lst = new ArrayList<Integer>();
					for(int j = 0; j < lstsize; ++j)
					{
						lst.add(is.popInteger());
					}
					mapp2d.put(k, lst);
				}
			}
			break;
		default:
			throw new Stream.DecodeException();
		}
	}

	@Override
	public void encode(Stream.AOStream os)
	{	
		os.pushInteger(VERSION);
		os.pushBoolean(bIgnoreCase);
		os.pushInteger(pieces.size());
		for(PieceInfo pi : pieces)
		{
			os.pushByte(pi.crypttype);
			if( pi.crypttype != PieceInfo.PCRY_NULL )
				os.pushInteger(pi.size);
			os.pushInteger(pi.digest.datalen);
			os.pushSizeT(pi.digest.digest.length);
			os.pushBytes(pi.digest.digest);
		}
		os.pushInteger(mapp2d.size());
		for(Map.Entry<String, List<Integer>> e : mapp2d.entrySet())
		{
			os.pushString(e.getKey());
			os.pushSizeT(e.getValue().size());
			for(Integer i : e.getValue())
			{
				os.pushInteger(i);
			}
		}
	}
	
	public void setPieces(List<PieceInfo> digests)
	{
		this.pieces = digests;
	}
	
	public List<PieceInfo> getPieces()
	{
		return pieces;
	}
	
	public List<KDigest> getDigests()
	{
		List<KDigest> lst = new ArrayList<KDigest>();
		for(PieceInfo pi : pieces)
			lst.add(pi.digest);
		return lst;
	}
	
	public Map<String, List<Integer>> getMap()
	{
		return mapp2d;
	}
	
	public boolean isIgnoreCase()
	{
		return bIgnoreCase;
	}
	
	public void setIgnoreCase(boolean b)
	{
		bIgnoreCase = b;
	}

	private boolean bIgnoreCase = false;
	private List<PieceInfo> pieces = new ArrayList<PieceInfo>();
	private Map<String, List<Integer>> mapp2d = new HashMap<String, List<Integer>>();
}

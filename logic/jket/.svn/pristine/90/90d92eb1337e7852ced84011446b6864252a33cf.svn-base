
package ket.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipCompressor implements ICompressor
{
	private int getCompressBound(int sizeUncompress) // todo
	{
		return sizeUncompress / 2;
	}
	
	private int getUncompressBound(int sizeCompress) // todo
	{
		return sizeCompress * 2;
	}
	
	@Override
	public ByteBuffer compress(ByteBuffer dataUncompress)
	{
		if( dataUncompress == null )
			return null;
		
		Deflater def = new Deflater();
		def.setInput(dataUncompress.array(), dataUncompress.arrayOffset() + dataUncompress.position(), dataUncompress.remaining());
		def.finish();
		List<byte[]> cdatas = new ArrayList<byte[]>();
		final int cbound = getCompressBound(dataUncompress.remaining());
		int sizeCompress = 0;
		while( true )
		{
			byte[] out = new byte[cbound];
			int clen = def.deflate(out);
			if( clen == 0 )
			{
				if( def.needsInput() )
				{
					def.end();
					return null;
				}
			}
			sizeCompress += clen;
			// if( clen != out.length )
			if( def.finished() )
			{
				def.end();
				if( cdatas.isEmpty() )
				{
					return ByteBuffer.wrap(out, 0, sizeCompress);
				}
				else
				{
					ByteBuffer bb = ByteBuffer.allocate(sizeCompress);
					for(byte[] cdata : cdatas)
						bb.put(cdata);
					bb.put(out, 0, clen);
					bb.flip();
					return bb;
				}
			}
			else
				cdatas.add(out);
		}
	}
	
	@Override
	public ByteBuffer uncompress(ByteBuffer dataCompress)
	{
		if( dataCompress == null )
			return null;
		
		Inflater inf = new Inflater();
		inf.setInput(dataCompress.array(), dataCompress.arrayOffset() + dataCompress.position(), dataCompress.remaining());
		
		List<byte[]> ucdatas = new ArrayList<byte[]>();
		final int ucbound = getUncompressBound(dataCompress.remaining());
		int sizeUncompress = 0;
		
		while( true )
		{
			byte[] out = new byte[ucbound];
			int uclen = 0;
			try
			{
				uclen = inf.inflate(out);
			}
			catch(Exception ex)
			{
				inf.end();
				return null;
			}
			if( uclen == 0 )
			{
				if( inf.needsInput() || inf.needsDictionary() )
				{
					inf.end();
					return null;
				}
			}
			sizeUncompress += uclen;
			//if( uclen != out.length )
			if( inf.finished() )
			{
				inf.end();
				if( ucdatas.isEmpty() )
				{
					return ByteBuffer.wrap(out, 0, sizeUncompress);
				}
				else
				{
					ByteBuffer bb = ByteBuffer.allocate(sizeUncompress);
					for(byte[] ucdata : ucdatas)
						bb.put(ucdata);
					bb.put(out, 0, uclen);
					bb.flip();
					return bb;
				}
			}
			else
				ucdatas.add(out);
		}
	}
	
	public static void main(String[] args)
	{
		ZipCompressor zc = new ZipCompressor();
		byte[] data = new String("0000000000000000000000000000000000000000000000000000000000000000000000000000000").getBytes();
		ByteBuffer bb = zc.compress(ByteBuffer.wrap(data));
		System.out.println("compress " + data.length + " to " + bb.remaining());
		ByteBuffer ubb = zc.uncompress(bb);
		System.out.println("ucompress " + bb.remaining() + " to " + ubb.remaining());
		byte[] data2 = Arrays.copyOfRange(ubb.array(), ubb.arrayOffset() + ubb.position(), ubb.arrayOffset() + ubb.limit());
		System.out.println("data verify: " + Arrays.equals(data, data2));
	}
}

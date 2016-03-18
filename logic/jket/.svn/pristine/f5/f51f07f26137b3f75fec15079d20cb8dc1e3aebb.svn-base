
package ket.kio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ket.util.Stream;

public abstract class SimpleDencoder implements PacketDecoder<SimplePacket>, PacketEncoder<SimplePacket>
{
	
	@Override
	public void encode(SimplePacket packet, Stream.AOStream os)
	{
		os.pushInteger(packet.getType());
		os.pushByteBuffer(Stream.storeObjLE(packet));
	}

	@Override
	public SimplePacket decode(Stream.AIStream is) throws Exception
	{
		SimplePacket packet = null;
		try
		{
			is.getInputStream().mark(Integer.MAX_VALUE);
			int ptype = is.popInteger();
			if( !doCheckPacketType(ptype) )
				throw new Exception();			
			packet = createPacket(ptype);
			if( packet == null )
				throw new Exception();
			ByteBuffer bb = is.popByteBuffer();
			packet.decode(new Stream.IStreamLE(new ByteArrayInputStream(bb.array(), 0, bb.limit())));
		}
		catch(Stream.EOFException ex)
		{
			try
			{
				is.getInputStream().reset();
			}
			catch(IOException ex2)
			{
				throw new Exception();
			}
			packet = null;
		}
		catch(Stream.DecodeException ex)
		{
			try
			{
				is.getInputStream().reset();
			}
			catch(IOException ex2)
			{
			}
			throw new Exception();
		}
		return packet;
	}

	public abstract boolean doCheckPacketType(int ptype);
	public abstract SimplePacket createPacket(int ptype);
}

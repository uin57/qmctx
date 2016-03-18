
package ket.kio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;
import ket.util.Stream.BytesOutputStream;

public abstract class TCPClient<Packet> extends NetManager.TCPClient
{
	public TCPClient(NetManager manager)
	{
		manager.super();
	}

	@Override
	public abstract void onOpen();
	
	@Override
	public abstract void onOpenFailed(ErrorCode errcode);

	@Override
	public abstract void onClose(ErrorCode errcode);

	public abstract PacketDecoder<Packet> getDecoder();
	public abstract PacketEncoder<Packet> getEncoder();
	public abstract void onPacketRecv(Packet packet);
	
	@Override
	public int onDataRecv(byte[] data, int offset, int len)
	{
		List<Packet> packets = new ArrayList<Packet>();
		Stream.BytesInputStream bais = new Stream.BytesInputStream(data, offset, len);
		Stream.AIStream is = new Stream.IStreamLE(bais);
		while( is.hasMoreData() )
		{
			Packet packet = null;
			try
			{
				packet = getDecoder().decode(is);
			}
			catch(PacketDecoder.Exception ex)
			{
				return -1;
			}
			if( packet == null )
				break;
			packets.add(packet);
		}
		for(Packet packet : packets)
			onPacketRecv(packet);
		return bais.pos() - offset;
	}
	
	public void sendPacket(Packet packet)
	{
		BytesOutputStream bsos = new BytesOutputStream();
		Stream.AOStream os = new Stream.OStreamLE(bsos);
		getEncoder().encode(packet, os);
		if( bsos.size() != 0 )
			sendData(ByteBuffer.wrap(bsos.array(), 0, bsos.size()));
	}
}

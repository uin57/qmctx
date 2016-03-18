
package ket.kio;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;
import ket.util.Stream.BytesOutputStream;

public abstract class UDPPeer<Packet> extends NetManager.UDPPeer
{
	public UDPPeer(NetManager manager)
	{
		manager.super();
	}

	@Override
	public abstract void onOpen();
	
	@Override
	public abstract void onOpenFailed(ErrorCode errcode);

	@Override
	public abstract void onClose(ErrorCode errcode);

	public abstract PacketEncoder<Packet> getEncoder();
	public abstract PacketDecoder<Packet> getDecoder();
	public abstract void onPacketRecv(NetAddress addrRemote, Packet packet);
	
	@Override
	void onDataRecv(NetAddress addrRemote, byte[] data, int offset, int len)
	{
		List<Packet> packets = new ArrayList<Packet>();
		Stream.AIStream is = new Stream.IStreamLE(new ByteArrayInputStream(data, offset, len));
		while( is.hasMoreData() )
		{
			Packet packet = null;
			try
			{
				packet = getDecoder().decode(is);
			}
			catch( PacketDecoder.Exception ex )
			{
				break;
			}
			if( packet == null )
				break;
			packets.add(packet);
		}
		for(Packet packet : packets)
			onPacketRecv(addrRemote, packet);
	}
	
	public void sendPacket(NetAddress addrTarget, Packet packet)
	{
		BytesOutputStream bsos = new BytesOutputStream();
		Stream.AOStream os = new Stream.OStreamLE(bsos);
		getEncoder().encode(packet, os);
		if( bsos.size() != 0 )
			sendData(addrTarget, ByteBuffer.wrap(bsos.array(), 0, bsos.size()));
	}
	
	public void sendPacket(Packet packet)
	{
		sendPacket(null, packet);
	}

}

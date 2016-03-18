
package ket.kio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;
import ket.util.Stream.BytesOutputStream;

public abstract class TCPServer<Packet> extends NetManager.TCPServer
{
	public TCPServer(NetManager manager)
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
	
	public abstract void onPacketSessionOpen(int sessionid, NetAddress addrClient);
	public abstract void onPacketSessionClose(int sessionid, ErrorCode errcode);
	public abstract void onPacketSessionRecv(int sessionid, Packet packet);
	
	@Override
	void onSessionOpen(int sessionid, NetAddress addrClient)
	{
		onPacketSessionOpen(sessionid, addrClient);
	}
	
	@Override
	void onSessionClose(int sessionid, ErrorCode errcode)
	{
		onPacketSessionClose(sessionid, errcode);
	}
	
	@Override
	int onSessionDataRecv(int sessionid, byte[] data, int offset, int len)
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
			onPacketSessionRecv(sessionid, packet);
		return bais.pos() - offset;
	}
	
	public void sendPacket(int sessionid, Packet packet)
	{
		BytesOutputStream bsos = new BytesOutputStream();
		Stream.AOStream os = new Stream.OStreamLE(bsos);
		getEncoder().encode(packet, os);
		if( bsos.size() != 0 )
			sendSessionData(sessionid, ByteBuffer.wrap(bsos.array(), 0, bsos.size()));
	}
}

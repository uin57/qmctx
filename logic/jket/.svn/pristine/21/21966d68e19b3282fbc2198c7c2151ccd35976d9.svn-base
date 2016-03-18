// modified by ket.kio.RPCGen at Mon Apr 16 19:38:07 CST 2012.

package ket.kpc.monitor;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPPeerMonitorClient extends TCPClient<SimplePacket>
{

	public TCPPeerMonitorClient(Monitor monitor)
	{
		super(monitor.getNetManager());
		this.monitor = monitor;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to monitor
			case Packet.eP2MPKTHello:
			case Packet.eP2MPKTSnapshotResponse:
				return true;
			default:
				break;
			}
			return false;
		}
	}

	@Override
	public PacketEncoder<SimplePacket> getEncoder()
	{
		return dencoder;
	}

	@Override
	public PacketDecoder<SimplePacket> getDecoder()
	{
		return dencoder;
	}

	@Override
	public void onOpen()
	{
		monitor.onTCPPeerMonitorClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		monitor.onTCPPeerMonitorClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		monitor.onTCPPeerMonitorClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to monitor
		case Packet.eP2MPKTHello:
			{
				Packet.P2M.Hello p = (Packet.P2M.Hello)packet;
				monitor.onTCPPeerMonitorClientRecvHello(this, p);
			}
			break;
		case Packet.eP2MPKTSnapshotResponse:
			{
				Packet.P2M.SnapshotResponse p = (Packet.P2M.SnapshotResponse)packet;
				monitor.onTCPPeerMonitorClientRecvSnapshotResponse(this, p);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private Monitor monitor;
}

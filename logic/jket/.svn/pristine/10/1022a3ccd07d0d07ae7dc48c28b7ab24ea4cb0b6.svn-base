// modified by ket.kio.RPCGen at Mon Apr 16 19:38:07 CST 2012.

package ket.kpc.tracker;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.NetAddress;
import ket.kio.UDPPeer;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class AssistantUDPTrackerServer extends UDPPeer<SimplePacket>
{

	public AssistantUDPTrackerServer(Tracker tracker)
	{
		super(tracker.getNetManager());
		this.tracker = tracker;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to tracker
			case Packet.eP2TPKTUPSTestAnswer:
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
		tracker.onAssistantUDPTrackerServerOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		tracker.onAssistantUDPTrackerServerOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		tracker.onAssistantUDPTrackerServerClose(this, errcode);
	}

	@Override
	public void onPacketRecv(NetAddress addrRemote, SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to tracker
		case Packet.eP2TPKTUPSTestAnswer:
			{
				Packet.P2T.UPSTestAnswer p = (Packet.P2T.UPSTestAnswer)packet;
				tracker.onAssistantUDPTrackerServerRecvUPSTestAnswer(this, p, addrRemote);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private Tracker tracker;
}

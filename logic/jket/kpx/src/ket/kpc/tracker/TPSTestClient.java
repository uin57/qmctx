// modified by ket.kio.RPCGen at Sat Apr 28 16:46:17 CST 2012.

package ket.kpc.tracker;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TPSTestClient extends TCPClient<SimplePacket>
{

	public TPSTestClient(Tracker tracker)
	{
		super(tracker.getNetManager());
		this.tracker = tracker;
	}

	public TPSTestClient(Tracker tracker, long key, boolean ok)
	{
		super(tracker.getNetManager());
		this.tracker = tracker;
		this.key = key;
		this.ok = ok;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to tracker
			case Packet.eP2TPKTTPSTestAnswer:
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
		tracker.onTPSTestClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		tracker.onTPSTestClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		tracker.onTPSTestClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to tracker
		case Packet.eP2TPKTTPSTestAnswer:
			{
				Packet.P2T.TPSTestAnswer p = (Packet.P2T.TPSTestAnswer)packet;
				tracker.onTPSTestClientRecvTPSTestAnswer(this, p);
			}
			break;
		default:
			break;
		}
	}

	public long getKey()
	{
		return key;
	}

	public void setKey(long key)
	{
		this.key = key;
	}

	public boolean getOK()
	{
		return ok;
	}

	public void setOK(boolean ok)
	{
		this.ok = ok;
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private Tracker tracker;
	private long key;
	private boolean ok;
}

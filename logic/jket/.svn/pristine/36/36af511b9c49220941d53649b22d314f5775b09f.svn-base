// modified by ket.kio.RPCGen at Fri Jun 15 15:43:04 CST 2012.

package ket.kpc.control;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;
import ket.kpc.rpc.Packet;

public class TCPPeerControlClient extends TCPClient<SimplePacket>
{

	public TCPPeerControlClient(Control control)
	{
		super(control.getNetManager());
		this.control = control;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
			switch( ptype )
			{
			// peer to control
			case Packet.eP2CPKTHello:
			case Packet.eP2CPKTDistFile:
			case Packet.eP2CPKTDistFileFrag:
			case Packet.eP2CPKTDistFileUpdate:
			case Packet.eP2CPKTCheckFile:
			case Packet.eP2CPKTCheckFileFrag:
			case Packet.eP2CPKTDistFileList:
			case Packet.eP2CPKTUpdatePeerStatus:
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
		control.onTCPPeerControlClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		control.onTCPPeerControlClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		control.onTCPPeerControlClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
		switch( packet.getType() )
		{
		// peer to control
		case Packet.eP2CPKTHello:
			{
				Packet.P2C.Hello p = (Packet.P2C.Hello)packet;
				control.onTCPPeerControlClientRecvHello(this, p);
			}
			break;
		case Packet.eP2CPKTDistFile:
			{
				Packet.P2C.DistFile p = (Packet.P2C.DistFile)packet;
				control.onTCPPeerControlClientRecvDistFile(this, p);
			}
			break;
		case Packet.eP2CPKTDistFileFrag:
			{
				Packet.P2C.DistFileFrag p = (Packet.P2C.DistFileFrag)packet;
				control.onTCPPeerControlClientRecvDistFileFrag(this, p);
			}
			break;
		case Packet.eP2CPKTDistFileUpdate:
			{
				Packet.P2C.DistFileUpdate p = (Packet.P2C.DistFileUpdate)packet;
				control.onTCPPeerControlClientRecvDistFileUpdate(this, p);
			}
			break;
		case Packet.eP2CPKTCheckFile:
			{
				Packet.P2C.CheckFile p = (Packet.P2C.CheckFile)packet;
				control.onTCPPeerControlClientRecvCheckFile(this, p);
			}
			break;
		case Packet.eP2CPKTCheckFileFrag:
			{
				Packet.P2C.CheckFileFrag p = (Packet.P2C.CheckFileFrag)packet;
				control.onTCPPeerControlClientRecvCheckFileFrag(this, p);
			}
			break;
		case Packet.eP2CPKTDistFileList:
			{
				Packet.P2C.DistFileList p = (Packet.P2C.DistFileList)packet;
				control.onTCPPeerControlClientRecvDistFileList(this, p);
			}
			break;
		case Packet.eP2CPKTUpdatePeerStatus:
			{
				Packet.P2C.UpdatePeerStatus p = (Packet.P2C.UpdatePeerStatus)packet;
				control.onTCPPeerControlClientRecvUpdatePeerStatus(this, p);
			}
			break;
		default:
			break;
		}
	}

	//todo
	private Dencoder dencoder = new Dencoder();
	private Control control;
}

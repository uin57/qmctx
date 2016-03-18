// modified by ket.kio.RPCGen at Mon Apr 23 15:24:29 CST 2012.

package ket.kpc.peer;

import ket.kio.PacketDecoder;
import ket.kio.PacketEncoder;
import ket.kio.TCPClient;
import ket.kio.SimplePacket;

import ket.kpc.rpc.ABaseDencoder;

public class NTTestClient extends TCPClient<SimplePacket>
{

	public NTTestClient(RPCManager managerRPC)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
	}

	public NTTestClient(RPCManager managerRPC, String dstHost, boolean ok)
	{
		super(managerRPC.getNetManager());
		this.managerRPC = managerRPC;
		this.dstHost = dstHost;
		this.ok = ok;
	}

	private class Dencoder extends ABaseDencoder
	{
		@Override
		public boolean doCheckPacketType(int ptype)
		{
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
		managerRPC.onNTTestClientOpen(this);
	}

	@Override
	public void onOpenFailed(ket.kio.ErrorCode errcode)
	{
		managerRPC.onNTTestClientOpenFailed(this, errcode);
	}

	@Override
	public void onClose(ket.kio.ErrorCode errcode)
	{
		managerRPC.onNTTestClientClose(this, errcode);
	}

	@Override
	public void onPacketRecv(SimplePacket packet)
	{
	}

	public String getDstHost()
	{
		return dstHost;
	}

	public void setDstHost(String dstHost)
	{
		this.dstHost = dstHost;
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
	private RPCManager managerRPC;
	private String dstHost;
	private boolean ok;
}

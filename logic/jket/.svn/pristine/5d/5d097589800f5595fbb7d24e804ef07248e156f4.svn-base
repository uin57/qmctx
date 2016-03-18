// modified by ket.kio.RPCGen at Fri Nov 09 16:19:43 CST 2012.

package ket.kpc.peer;

import ket.kio.NetAddress;
import ket.kio.NetManager;
import ket.kpc.common.PeerID;
import ket.kpc.rpc.Packet;
import ket.kpc.SBean;
import ket.util.FileSys;

public class ClientCheater
{

	//// begin handlers.
	public void onTCPTrackerClientCheaterOpen(TCPTrackerClientCheater peer)
	{
		System.out.println("client " + peer.getClientID() + " open");
	}

	public void onTCPTrackerClientCheaterOpenFailed(TCPTrackerClientCheater peer, ket.kio.ErrorCode errcode)
	{
		System.out.println("client " + peer.getClientID() + " open failed");
		peer.open();
	}

	public void onTCPTrackerClientCheaterClose(TCPTrackerClientCheater peer, ket.kio.ErrorCode errcode)
	{
		System.out.println("client " + peer.getClientID() + " closed");
		peer.open();
	}

	public void onTCPTrackerClientCheaterRecvHello(TCPTrackerClientCheater peer, Packet.T2P.Hello packet)
	{
		peer.sendPacket(new Packet.P2T.LoginRequest(new SBean.PeerLoginRequest(1,
				packet.getClientIP(), peer.getClientID(),
				PeerID.PF_NULL,
				peer.getClientAddr().host,
				0,
				0)));
	}

	public void onTCPTrackerClientCheaterRecvLoginResponse(TCPTrackerClientCheater peer, Packet.T2P.LoginResponse packet)
	{
		//System.out.println("client " + peer.getClientID() + " recv login response " + packet.getRet());
	}

	public void onTCPTrackerClientCheaterRecvTPSTestResponse(TCPTrackerClientCheater peer, Packet.T2P.TPSTestResponse packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvUPSTestResponse(TCPTrackerClientCheater peer, Packet.T2P.UPSTestResponse packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvUPSTestQuestion(TCPTrackerClientCheater peer, Packet.T2P.UPSTestQuestion packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvSourceAnswer(TCPTrackerClientCheater peer, Packet.T2P.SourceAnswer packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvTPSRCResponse(TCPTrackerClientCheater peer, Packet.T2P.TPSRCResponse packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvTPSRCRequest(TCPTrackerClientCheater peer, Packet.T2P.TPSRCRequest packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvUPSRCRequest(TCPTrackerClientCheater peer, Packet.T2P.UPSRCRequest packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvUPSRCResponse(TCPTrackerClientCheater peer, Packet.T2P.UPSRCResponse packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvRelayClose(TCPTrackerClientCheater peer, Packet.T2P.RelayClose packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvRelay(TCPTrackerClientCheater peer, Packet.T2P.Relay packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvDataGroupMemberQuery(TCPTrackerClientCheater peer, Packet.T2P.DataGroupMemberQuery packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvNoticeLocalNetwork(TCPTrackerClientCheater peer, Packet.T2P.NoticeLocalNetwork packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvTestNetworkTCP(TCPTrackerClientCheater peer, Packet.T2P.TestNetworkTCP packet)
	{
		// TODO
	}

	public void onTCPTrackerClientCheaterRecvTrackerRedirect(TCPTrackerClientCheater peer, Packet.T2P.TrackerRedirect packet)
	{
		// TODO
	}

	//// end handlers.
	
	public NetManager getNetManager()
	{
		return managerNet;
	}
	
	public void start()
	{
		managerNet.start();
		int nClient = 1000;
		for(int i = 0; i < nClient; ++i)
		{
			TCPTrackerClientCheater ttcc = new TCPTrackerClientCheater(this, i);
			ttcc.setServerAddr(new NetAddress("127.0.0.1", 1106));
			ttcc.open();
		}
	}
	
	public void destroy()
	{
		managerNet.destroy();
	}
	
	public static void main(String[] args)
	{
		ClientCheater cc = new ClientCheater();
		cc.start();
		FileSys.pause(false);
		cc.destroy();
	}
	
	private NetManager managerNet = new NetManager();
}

// modified by ket.kio.RPCGen at Wed Apr 18 00:52:05 CST 2012.

package ket.kpc.monitor;

import ket.kio.NetManager;
import ket.kpc.rpc.Packet;

public class Monitor
{
	//// begin handlers.
	public void onTCPPeerMonitorClientOpen(TCPPeerMonitorClient peer)
	{
		// TODO
	}

	public void onTCPPeerMonitorClientOpenFailed(TCPPeerMonitorClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPPeerMonitorClientClose(TCPPeerMonitorClient peer, ket.kio.ErrorCode errcode)
	{
		// TODO
	}

	public void onTCPPeerMonitorClientRecvHello(TCPPeerMonitorClient peer, Packet.P2M.Hello packet)
	{
		// TODO
	}

	public void onTCPPeerMonitorClientRecvSnapshotResponse(TCPPeerMonitorClient peer, Packet.P2M.SnapshotResponse packet)
	{
		// TODO
	}

	//// end handlers.
	
	public NetManager getNetManager()
	{
		return null; // TODO
	}
}

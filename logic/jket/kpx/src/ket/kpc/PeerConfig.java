
package ket.kpc;

import java.util.ArrayList;
import java.util.List;

import ket.kio.NetAddress;

public class PeerConfig 
{
	public List<NetAddress> trackers = new ArrayList<NetAddress>();
	public int tpsPort = 61206;
	public int reconnectingintervalTrackerClient = 10;
	public boolean bSuper = false;
	public boolean bTPSDisable = false;
	public boolean bUPSDisable = false;
	public int maxidleUploadSession = 60;
	public int maxidleDownloadSession = 60;
	public int maxidlePeerSession = 60;
	public boolean bAutoReconnectingTTC = true; // TODO
}

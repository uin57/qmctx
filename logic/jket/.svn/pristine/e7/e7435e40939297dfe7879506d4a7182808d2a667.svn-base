
package ket.kiox;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ket.util.EasyXML;

public class UPNP
{
	
	public static Set<String> searchGateways(String ipInternal, boolean bSearchAll)
	{		
		DatagramSocket ds;
		Set<String> locations = new HashSet<String>();
		try
		{
			ds = new DatagramSocket(0, InetAddress.getByName(ipInternal));
			final int mx = 6;
			final int sendtimes = 3;
			byte[] bufsend = getDiscoverMsg("wanip", mx).getBytes();
			DatagramPacket packet = new DatagramPacket(bufsend, bufsend.length, InetAddress.getByName(ssdphost), ssdpport);
			long timeout = System.currentTimeMillis() + mx * 1000;
			for(int i = 0; i < sendtimes; ++i)
				ds.send(packet);
			try
			{
				while( true )
				{
					byte[] bufrecv = new byte[1024];
					DatagramPacket packetrecv = new DatagramPacket(bufrecv, bufrecv.length);
					long now = System.currentTimeMillis();
					if( now >= timeout )
						break;
					ds.setSoTimeout((int)(timeout - now));
					ds.receive(packetrecv);
					//System.out.println(ds.getLocalAddress());
					String msg = new String(packetrecv.getData(), 0, packetrecv.getLength());
					//System.out.println(msg);
					String location = getLocation(msg);
					if( location != null )
					{
						locations.add(location);
						if( ! bSearchAll )
							break;
					}
				}
			}
			catch(SocketTimeoutException ste)
			{
				//ste.printStackTrace();
			}						
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return locations;
	}
	
	private static String getLocation(String msg)
	{
		int firstn = msg.indexOf("\n");
		int ok200 = msg.indexOf("200 OK");
		if( firstn == -1 || ok200 == -1 || ok200 >= firstn )
			return null;
		int loc = msg.indexOf("\nLOCATION");
		if( loc == -1 )
			loc = msg.indexOf("\nlocation");
		if( loc == -1 )
			return null;
		loc = msg.indexOf(":", loc + 9);
		if( loc == -1 )
			return null;
		int locn = msg.indexOf("\n", loc+1);
		if( locn == -1 || locn <= loc)
			return null;
		return msg.substring(loc + 9, locn).trim();
	}

	private static String getDiscoverMsg(String msgtype, int mx)
	{
		String msg = "M-SEARCH * HTTP/1.1";
		msg = msg + "\r\n" + "Host: " + ssdphost + ":" + ssdpport;
		msg = msg + "\r\n" + "ST: ";
		if( msgtype.equals("wanip") )
			msg = msg + "urn:schemas-upnp-org:service:WANIPConnection:1";
		else if( msgtype.equals("wanppp") )
			msg = msg + "urn:schemas-upnp-org:service:WANPPPConnection:1";
		else if( msgtype.equals("gateway") )
			msg = msg + "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
		else if( msgtype.equals("media") )
			msg = msg + "urn:schemas-upnp-org:device:MediaServer:1";
		else
			msg = msg + "upnp:rootdevice";
		msg = msg + "\r\n" + "Man: \"ssdp:discover\"";
		msg = msg + "\r\n" + "MX: " + mx;
		msg = msg + "\r\n";
		msg = msg + "\r\n";
		return msg;
	}
	
	private static String getControlURL(String xml, String base)
	{
		EasyXML.Node urlbasenode = EasyXML.findFirstNode(xml, "URLBase");
		//System.out.println(urlbasenode.content);
		String urlbase = urlbasenode == null ? "" : urlbasenode.content;
		if( urlbase.isEmpty() )
			urlbase = base;
		List<EasyXML.Node> snodes = EasyXML.findNodes(xml, "service");
		for(EasyXML.Node snode : snodes)
		{
			EasyXML.Node stnode = EasyXML.findFirstNode(snode.content, "serviceType");
			if( stnode == null )
				continue;
			if( ! stnode.content.equals("urn:schemas-upnp-org:service:WANIPConnection:1") )
				continue;
			EasyXML.Node cunode = EasyXML.findFirstNode(snode.content, "controlURL");
			if( cunode == null )
				continue;
			return urlbase + cunode.content;
		}
		return null;
	}
	
	private static String getSoapAddMap(String protype, String ipInternal, int portInternal, int portExternal)
	{
		//
		return "<?xml version=\"1.0\"?>" +
			"<s:Envelope " +
	    "xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
	    "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
	    "<s:Body>" +
	    "<u:AddPortMapping xmlns:u=\"urn:schemas-upnp-org:service:WANIPConnection:1\">" +
	"<NewRemoteHost></NewRemoteHost>" +
	"<NewExternalPort>" + portExternal + "</NewExternalPort>" +
	"<NewProtocol>" + protype + "</NewProtocol>" +
	"<NewInternalPort>" + portInternal + "</NewInternalPort>" +
	"<NewInternalClient>" + ipInternal + "</NewInternalClient>" +
	"<NewEnabled>1</NewEnabled>" +
	"<NewPortMappingDescription>kpc</NewPortMappingDescription>" +
	"<NewLeaseDuration>0</NewLeaseDuration></u:AddPortMapping>" +
	  "</s:Body>" +
		"</s:Envelope>";
	}
	
	private static String ssdphost = "239.255.255.250";
	private static int ssdpport = 1900;
	
	public static void main(String[] args)
	{
		// test
		System.out.println(addPortMapping("TCP", "192.168.1.100", 61106, 61106));
	}
	
	public static int addPortMapping(String protype, String ipInternal, int portInternal, int portExternal)
	{
		Set<String> gateways = searchGateways(ipInternal, false);
		if( gateways.isEmpty() )
			return 1;
		String gloc = gateways.iterator().next();
		//System.out.println(gloc);
		byte[] data = HTTP.get(gloc);
		if( data == null )
			return 2; // todo
		String xml = new String(data);
		//System.out.println(xml);
		String controlurl = getControlURL(xml, HTTP.getBase(gloc));
		if( controlurl == null )
			return 3; // todo
		//System.out.println(controlurl);
		String soapmap = getSoapAddMap(protype, ipInternal, portInternal, portExternal);
		byte[] mapdata = soapmap.getBytes();
		data = HTTP.post(controlurl, mapdata);
		if( data == null )
			return 4;
		xml = new String(data);
		//System.out.println(xml);
		return 0;
	}
}

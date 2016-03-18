
package ket.kio.test;

import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.Set;

import ket.util.ArgsMap;

public class MulticastTest
{
	
	public MulticastTest(ArgsMap am)
	{
		this.am = am;
	}
	
	public void runServer()
	{
		MulticastSocket ms;
		try
		{
			ms = new MulticastSocket(ssdpport);
			InetAddress group = InetAddress.getByName(ssdphost);
			ms.joinGroup(group);
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			while( true )
			{
				ms.receive(packet);
				System.out.println("recieve ok, length is " + packet.getLength()
						+ ", from " + packet.getSocketAddress());
				String msg = new String(packet.getData(), 0, packet.getLength());
				System.out.println(msg);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void runClient()
	{
		
		DatagramSocket ds;
		try
		{
			ds = new DatagramSocket();
			int mx = Integer.parseInt(am.get("-mx", "5"));
			int sendtimes = Integer.parseInt(am.get("-ns", "3"));
			byte[] buf = getDiscoverMsg(am.get("-st", "root"), mx).getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ssdphost), ssdpport);
			long timeout = System.currentTimeMillis() + mx * 1000;
			for(int i = 0; i < sendtimes; ++i)
				ds.send(packet);
			Set<String> locations = new HashSet<String>();
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
					System.out.println("recieve ok, length is " + packetrecv.getLength()
						+ ", from " + packetrecv.getSocketAddress());
					String msg = new String(packetrecv.getData(), 0, packetrecv.getLength());					
					System.out.println(msg);
					String location = getLocation(msg);
					if( location != null )
					{
						locations.add(location);
					}
				}
			}
			catch(SocketTimeoutException ste)
			{
				System.out.println("recieve timeout");
			}
			
			System.out.println();
			
			if( locations.isEmpty() )
				System.out.println("no gateway found.");
			else
			{
				System.out.println("" + locations.size() + " gateway found");
				for(String loc : locations)
					System.out.println("\t" + loc);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private String getLocation(String msg)
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
	
	private String getDiscoverMsg(String msgtype, int mx)
	{
		String msg = "M-SEARCH * HTTP/1.1";
		msg = msg + "\r\n" + "Host: " + ssdphost + ":" + ssdpport;
		msg = msg + "\r\n" + "ST: ";
		if( msgtype.equals("gateway") )
			msg = msg + "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
		else if( msgtype.equals("media") )
			msg = msg + "urn:schemas-upnp-org:device:MediaServer:1";
		else
			msg = msg + "upnp:rootdevice";
		msg = msg + "\r\n" + "Man: \"ssdp:discover\"";
		msg = msg + "\r\n" + "MX: " + mx;
		msg = msg + "\r\n";
		return msg;
	}
	
	private String ssdphost = "239.255.255.250";
	private int ssdpport = 1900;
	
	private ArgsMap am;

	public static void main(String[] args)
	{		
		ArgsMap am = new ArgsMap(args);

		MulticastTest test = new MulticastTest(am);
		
		if( am.containsKey("s") )
			test.runServer();
		else
			test.runClient();
	}
}
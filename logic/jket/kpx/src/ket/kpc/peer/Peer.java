
package ket.kpc.peer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;

import ket.xml.Node;
import ket.kio.NetAddress;
import ket.kpc.ErrorCode;
import ket.kpc.IDownloadCallback;
import ket.kpc.IPeer;
import ket.pack.KDigest;
import ket.kpc.KDownloadTask;
import ket.kpc.PeerConfig;

public class Peer implements IPeer
{
	
	private static final int VERSION = 1;
	
	@Override
	public PeerConfig getConfig()
	{
		return cfg;
	}
	
	@Override
	public void setConfig(PeerConfig config)
	{
		if( config != null )
			cfg = config;
	}
	
	@Override
	public void setConfig(File configFile)
	{
		if( ! configFile.exists() )
			return;
		try
		{
			Node nodeRoot = ket.xml.Factory.newReader(configFile).getRoot();
			Node nodeTrackerList = nodeRoot.getChild("tracker_list");
			if( nodeTrackerList != null )
			{
				cfg.trackers.clear();
				List<Node> nodesTrackers = nodeTrackerList.getChildren("tracker");
				for(Node node : nodesTrackers)
				{
					String host = node.getString("host", "");
					int port = node.getInteger("port", 0);
					if( ! host.equals("") && port != 0 )
						cfg.trackers.add(new NetAddress(host, port));
				}
			}
			// TODO
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void dumpConfig()
	{
		System.out.println("PeerConfig.Dump()");
		System.out.println("\ttrackers:");
		for(ket.kio.NetAddress addr : cfg.trackers)
		{
			System.out.println("\t\ttracker server :[" + addr + "]");
		}
		System.out.println("\ttcp peer server port is " + cfg.tpsPort);
		System.out.println("\ttracker client reconnecting interval is " + cfg.reconnectingintervalTrackerClient);
		System.out.println("\ttps disable is " + (cfg.bTPSDisable? "true" : "false"));
		System.out.println("\tups disable is " + (cfg.bUPSDisable? "true" : "false"));
		System.out.println("\tmax idle time of upload session is " + cfg.maxidleUploadSession);
		System.out.println("\tmax idle time of download session is " + cfg.maxidleDownloadSession);
		System.out.println("\tmax idle time of peer session is " + cfg.maxidlePeerSession);
	}
	
	@Override
	public void start()
	{
		dumpConfig();

		managerData.start();
		//
		managerRPC.start();
		//
		
	}
	@Override
	public void destroy()
	{	
		managerRPC.onExecutorShutdown();
		managerData.onExecutorShutdown();
		executor.shutdown();
		
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
		managerRPC.destroy();		
		managerData.destroy();
	}			
	
	//
	public void createDataGroup(KDigest group, List<KDigest> members, boolean bLocal)
	{
		managerData.createDataGroup(group, members, bLocal);
	}
	
	public int getVersion()
	{
		return VERSION;
	}
	
	public DataManager getDataManager()
	{
		return managerData;
	}
	
	public RPCManager getRPCManager()
	{
		return managerRPC;
	}
	
	@Override
	public ErrorCode addShareDataByCopyTask(byte[] data, int offset, int len, List<KDigest> digest)
	{
		return managerData.addShareDataByCopyTask(data, offset, len, digest);
	}
	@Override
	public ErrorCode addShareFileTask(String fnShare, List<KDigest> digest)
	{
		return managerData.addShareFileTask(fnShare, digest);
	}
	@Override
	public ErrorCode addDownloadTask(KDownloadTask dtask, IDownloadCallback callback)
	{
		return managerData.addDownloadTask(dtask, callback);
	}
	@Override
	public ErrorCode removeTask(KDigest digest)
	{
		return managerData.removeTask(digest);
	}
	
	public ScheduledExecutorService getExecutor()
	{
		return executor;
	}
	
	private DataManager managerData = new DataManager(this);
	private RPCManager managerRPC = new RPCManager(this);
	private PeerConfig cfg = new PeerConfig();
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
}

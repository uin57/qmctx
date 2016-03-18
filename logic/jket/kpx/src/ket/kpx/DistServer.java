
package ket.kpx;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import ket.kpc.IPeer;
import ket.kpc.Factory;
import ket.pack.KDigest;
import ket.pack.IndexData;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.Stream;
import ket.util.StringConverter;

public class DistServer
{
	
	private static void share(IPeer peer, String dirshare)
	{
		try
		{
			File fShare = new File(dirshare).getCanonicalFile();
			if( !fShare.exists() || ! fShare.isDirectory() )
			{
				System.out.println("bad share dir.");
				return;
			}
			String[] lst = fShare.list();
			for(String d : lst)
			{
				File fDist = new File(fShare.getAbsolutePath() + File.separator + d).getCanonicalFile();
				if( ! fDist.isDirectory() )
					continue;
				dist(peer, fDist);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void dist(IPeer peer, File fDist)
	{
		try
		{
			String[] stampanddd = fDist.getName().split("_");
			if( stampanddd.length < 2 )
				return;
			String fnDistData = stampanddd[1];
			File fDistdata = new File(fDist.getAbsolutePath() + File.separator + fnDistData).getCanonicalFile();
			if( ! fDistdata.exists() || ! fDistdata.isFile() || ! fDistdata.canRead() )
				return;
			IndexData dd = new IndexData();
			if( ! Stream.loadZipObjLE(dd, fDistdata) )
			{
				System.out.println("load dist data failed.");
				return;
			}
			List<KDigest> distdigest = new ArrayList<KDigest>();
			peer.addShareFileTask(fDistdata.getAbsolutePath(), distdigest);
			System.out.println("add dist data: " + fDistdata.getAbsolutePath());
			
			String sdistdigest = StringConverter.getHexStringFromBytes(distdigest.get(0).digest);
			if( ! fnDistData.equals(sdistdigest) )
			{
				System.out.println("verify dist data failed.");
				return;
			}
			System.out.println("    stamp is : " + stampanddd[0] + ", distdigest is " + sdistdigest);
			List<KDigest> digestlst = dd.getDigests();
			peer.createDataGroup(distdigest.get(0), digestlst, false);
			for(KDigest kd : digestlst)
			{
				String s = StringConverter.getHexStringFromBytes(kd.digest);
				File f = new File(fDist.getAbsolutePath() + File.separator + s).getCanonicalFile();
				if( ! f.exists() || ! f.isFile() || ! f.canRead() )
				{
					continue;
				}
				List<KDigest> lst = new ArrayList<KDigest>();
				lst.add(new KDigest(StringConverter.getBytesFromHexString(s)));
				peer.addShareFileTask(f.getAbsolutePath(), lst);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		String cfgfilename = am.get("-xmlcfg", "peercfg.xml");
		String sharedirname = am.get("-sharedir", "share");
		
		IPeer peer = Factory.createPeer();
		peer.setConfig(new File(cfgfilename));
		peer.getConfig().bSuper = true;
		peer.start();
		share(peer, sharedirname);
		FileSys.pause(am.containsKey("bg"));
		peer.destroy();
		System.out.println("end");
	}
}

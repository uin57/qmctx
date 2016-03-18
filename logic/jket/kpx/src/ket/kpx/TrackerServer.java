
package ket.kpx;

import java.io.File;

import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.kpc.Factory;
import ket.kpc.ITracker;

public class TrackerServer
{

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		String cfgfilename = am.get("-cfgfile", "tracker.cfg");
		
		ITracker tracker = Factory.createTracker();
		tracker.setConfig(new File(cfgfilename));
		tracker.start();
		FileSys.pause(am.containsKey("bg"));
		tracker.destroy();
		System.out.println("end");
	}

}

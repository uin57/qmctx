
package ket.kpc;

import java.io.File;

public interface ITracker
{
	// get config
	public TrackerConfig getConfig();
	// set config
	public void setConfig(TrackerConfig config);
	// set config
	public void setConfig(File configFile);
	// start service
	public void start();
	// destroy
	public void destroy();
}


package ket.kpc;

import java.util.List;
import java.io.File;

import ket.pack.KDigest;

public interface IPeer
{
	// get config
	public PeerConfig getConfig();
	// set config
	public void setConfig(PeerConfig config);
	// set config
	public void setConfig(File configFile);
	// start service
	public void start();
	// destroy
	public void destroy();
	//
	public void createDataGroup(KDigest group, List<KDigest> members, boolean bLocal);
	//
	public ErrorCode addShareDataByCopyTask(byte[] data, int offset, int len, List<KDigest> digest);
	//
	public ErrorCode addShareFileTask(String fnShare, List<KDigest> digest);
	//
	public ErrorCode addDownloadTask(KDownloadTask dtask, IDownloadCallback callback);
	//
	public ErrorCode removeTask(KDigest digest);
}


package ket.kpc;

public interface IDownloadCallback
{
	public void onFailed(ErrorCode errcode);
	public void onSaveDownloadTask(KDownloadTask dtask);
	public void onComplete();
}

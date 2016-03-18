
package ket.kiox;

public interface ILanService
{
	public static interface IMapPortTask
	{
		public String getProtoType();
		public String getInternalIP();
		public int getInternalPort();
		public int getExternalPort();
		
		public void onComplete(int errcode);
	}
	
	public void addMapPortTask(IMapPortTask task);
}

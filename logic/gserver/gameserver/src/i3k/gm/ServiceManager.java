
package i3k.gm;

public class ServiceManager
{
	public static Service createService()
	{
		return new ServiceImp();
	}
}

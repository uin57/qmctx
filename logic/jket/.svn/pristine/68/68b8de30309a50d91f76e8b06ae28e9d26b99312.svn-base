
package ket.kiox;

class LanService implements ILanService
{
	
	private static class MapPortTask implements Runnable
	{
		public MapPortTask(IMapPortTask task)
		{
			this.task = task;
		}

		@Override
		public void run()
		{	
			task.onComplete(UPNP.addPortMapping(task.getProtoType(), task.getInternalIP(), task.getInternalPort(), task.getExternalPort()));
		}
		
		IMapPortTask task;
	}

	@Override
	public void addMapPortTask(IMapPortTask task)
	{
		try
		{
			new Thread(new MapPortTask(task)).run();
		}
		catch(Exception ex)
		{
			task.onComplete(-1);
		}
	}

}

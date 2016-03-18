
package i3k.gm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ket.kio.NetAddress;
import ket.util.ArgsMap;
import ket.util.FileSys;
import ket.util.SKVMap;
import ket.util.Stream;
import i3k.SBean;
import i3k.gm.Service.ServiceConfig;
import i3k.gs.GameData;
import i3k.gs.ToolData;

public class GMClient
{

	public static class Config
	{
		public NetAddress addrServer = new NetAddress("127.0.0.1", 10907);
	}
	
	private class TimerTask implements Runnable
	{
		@Override
		public void run()
		{
			// TODO
		}
		
		public ScheduledFuture<?> future = null;
	}
	
	public GMClient()
	{
		managerRPC = new RPCManagerControlClient(this);
		timertask.future = executor.scheduleAtFixedRate(timertask, 1, 1, TimeUnit.SECONDS);
	}
	
	public void setConfig(Config config)
	{
		if( config != null )
			cfg = config;
	}
	
	public Config getConfig()
	{
		return cfg;
	}
	
	public void log(String msg)
	{
		System.out.println(msg);
	}
	
	public static int getTime()
	{
		// TODO
		return (int)(new java.util.Date().getTime()/1000) + (3600*8);
	}
	
	int getDay()
	{
		return getTime() / 86400;
	}
	
	int getDay(int t)
	{
		return t / 86400;
	}
	
	void init(ServiceConfig cfg)
	{
		if( cfg != null )
		{
			this.cfg.addrServer.host = cfg.host;
			this.cfg.addrServer.port = cfg.port;
		}
		loadToolData(cfg == null ? "." : cfg.cfgPath);
	}
	
	void destroy()
	{
		if ( timertask.future != null )
			timertask.future.cancel(false);
		managerRPC.destroy();
		executor.shutdown();
		try
		{
			while( ! executor.awaitTermination(1, TimeUnit.SECONDS) ) { }
		}
		catch(Exception ex)
		{			
		}
	}
	
	void start()
	{
		managerRPC.start();
	}	
	
	RPCManagerControlClient getRPCManager()
	{
		return managerRPC;
	}
	
	public ScheduledExecutorService getExecutor()
	{
		return executor;
	}	
	
	public void process(String cmd, ArgsMap am)
	{
		try
		{
		switch( cmd )
		{
		case "shutdown":
			{
				String nStart = am.get("-start", "10");
				String nTime = am.get("-time", "10");
				managerRPC.shutdown((byte)0, Integer.parseInt(nStart) * 60 + getTime(), Integer.parseInt(nTime));
			}
			break;
		case "announce":
			{
				String msg = am.get("-msg");
				String nTime = am.get("-times", "3");
				managerRPC.announce(msg, Byte.parseByte(nTime));
			}
			break;
		case "who":
			{
				managerRPC.queryOnlineUsers();
			}
			break;
		case "sysmsg":
			{
				String rid = am.get("-rid", "0");
				String title = am.get("-t", "sysmsg");
				String content = am.get("-c", "sysmsg");
				int lifeTime = Integer.parseInt(am.get("-life", "0"));
				int flag = Integer.parseInt(am.get("-flag", "1"));
				int lvlReq = Integer.parseInt(am.get("-lvl", "1"));
				String satt = am.get("-att", "");
				List<SBean.DropEntryNew> att = new ArrayList<SBean.DropEntryNew>();
				if( satt != null && ! satt.equals("") )
				{
					String[] v = satt.split(":");
					att.add(new SBean.DropEntryNew(Byte.parseByte(v[0].trim()), Short.parseShort(v[1].trim()), Integer.parseInt(v[2].trim())));
				}
				managerRPC.sendSysMessage(Integer.parseInt(rid), (short)lvlReq, (short)0, title, content, lifeTime, flag, att);
			}
			break;
		case "worldgift":
			{
				String iid = am.get("-iid", "0");
				String msg = am.get("-msg", "haha");
				String time = am.get("-time", "86400");
				short id = Short.parseShort(iid);
				int itime = Integer.parseInt(time);
				if( id > 0 && msg.length() > 0 && itime > 0 )
					managerRPC.addWorldGift(id, msg, itime);
			}
			break;
		case "qid":
			{
				String rname = am.get("-rname", null);
				String uname = am.get("-uname", null);
				if( rname != null )
					managerRPC.queryRoleIDByRoleName(rname);
				else if( uname != null )
					managerRPC.queryRoleIDByUserName(uname);
			}
			break;
		case "mod":
			{
				String rid = am.get("-rid", "0");
				String mtype = am.get("-mtype", "set");
				String dtype = am.get("-dtype", "");
				String val = am.get("-val", "");
				String id = am.get("-id", "0");
				SBean.DataModifyReq req = new SBean.DataModifyReq();
				req.rid = Integer.parseInt(rid);
				req.val = Integer.parseInt(val);
				req.id = Short.parseShort(id);
				req.gm = 0;
				req.mail = 1;
				req.dtype = 0;
				req.mtype = 0;
				switch( mtype )
				{
				case "set":
					req.mtype = SBean.DataModifyReq.eSet;
					break;
				case "add":
					req.mtype = SBean.DataModifyReq.eAdd;
					break;
				default:
					break;
				}
				switch( dtype )
				{
				case "stone":
					req.dtype = SBean.DataModifyReq.eStone;
					break;
				case "money":
					req.dtype = SBean.DataModifyReq.eMoney;
					break;
				case "item":
					req.dtype = SBean.DataModifyReq.eItem;
					break;
				case "equip":
					req.dtype = SBean.DataModifyReq.eEquip;
					break;
				case "general":
					req.dtype = SBean.DataModifyReq.eGeneral;
					break;
				case "lvl":
					req.dtype = SBean.DataModifyReq.eLevel;
					break;
				case "forcescore":
					req.dtype = SBean.DataModifyReq.eForceScore;
					break;
				case "seyen":
					req.dtype = SBean.DataModifyReq.eSeyen;
					break;
				default:
					break;
				}
				if( req.dtype != 0 && req.mtype != 0 )
					managerRPC.modData(req);
			}
			break;
		default:
			System.out.println("unknown cmd " + cmd);
			break;
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void onQueryOnlineUserRes(int rcnt, int scnt)
	{
		System.out.println("onlineuser commited, rcnt is " + rcnt + ", scnt is " + scnt);
	}
	
	public void onRoleData(SBean.RoleDataRes res)
	{
		
	}
	
	public void onWorldGift(byte res)
	{
		System.out.println("add world gift res is " + res);
	}
	
	public void onDataQueryRes(SBean.DataQueryRes res)
	{
		System.out.println("dataquery res, res is " + res.res + ", res1 = " + res.iRes1);
	}
	
	public void onSysMessage(byte res)
	{
		System.out.println("sysmsg commited, res is " + res);
	}
	
	public void onAnnounce(byte res)
	{
		System.out.println("announce commited, res is " + res);
	}
	
	public void onSessionClose()
	{
		System.out.println("server closed");
	}
	
	public void onDataMod(SBean.DataModifyRes res)
	{
		System.out.println("data mod commited"
				+ ", rid = " + res.req.rid
				+ ", id = " + res.req.id
				+ ", mtype = " + res.req.mtype
				+ ", dtype = " + res.req.dtype
				+ ", val = " + res.req.val
				+ ", oldVal = " + res.oldVal
				+ ", newVal = " + res.newVal
				+ ", res = " + res.res);
	}
	
	boolean loadToolData(String dirCData)
	{
		try
		{
			SBean.GameDataCFGT gamedata = new SBean.GameDataCFGT();
			if( ! Stream.loadObjLE(gamedata, new File(dirCData + File.separator + "tool_cfg.dat")) )
				return false;
			toolData = new ToolData(gamedata);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		GMClient gm = new GMClient();
		ServiceConfig cfg = new ServiceConfig();
		cfg.host = am.get("-host", "127.0.0.1");
		String sport = am.get("-port", null);
		if( sport == null )
		{
			gm.init(null);
		}
		else
		{
			try
			{
				cfg.port = Integer.parseInt(sport);
				gm.init(cfg);
			}
			catch(Exception ex)
			{
				gm.init(null);
			}
		}
		gm.start();
		//FileSys.pause(am.containsKey("bg"));
		Scanner scanner = new Scanner(System.in);
		String last = "";
		while( true )
		{
			String s = scanner.nextLine();
			if( s == null )
				break;
			s = s.trim();
			if( s.isEmpty() )
				continue;
			if( s.equals("q") || s.equals("quit") )
			{
				break;
			}
			if( s.equals("!") )
				s = last;
			else
				last = s;
			String[] as = s.split(" ");
			ArgsMap am2 = new ArgsMap(as);
			gm.process(as[0], am2);
		}
		System.out.println("bye");
		gm.destroy();
	}
	RPCManagerControlClient managerRPC;
	ToolData toolData;
	Config cfg = new Config();
	ScheduledExecutorService executor = ket.util.ExecutorTool.newSingleThreadScheduledExecutor("TimerThread");
	final TimerTask timertask = new TimerTask();
}

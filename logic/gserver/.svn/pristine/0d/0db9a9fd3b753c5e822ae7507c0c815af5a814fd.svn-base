
package i3k.gm;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import i3k.gm.Service;
import i3k.gm.ServiceManager;

public class ServiceTest
{
	public static int getStringCount(String s)
	{
		int n = 0;
		for(int i = 0; i < s.length(); ++i)
		{
			char c = s.charAt(i);
			if( c < (char)128 )
				n++;
			else
				n+=2;
		}
		return n;
	}
	
	public static void main(String[] args)
	{
		Service service = ServiceManager.createService();
		Service.ServiceConfig cfg = new Service.ServiceConfig();
		cfg.host = "127.0.0.1";
		cfg.port = 10907;
		cfg.cfgPath = ".";
		service.init(cfg);
		
		try
		{
			for(int i =0; i<2; ++i) Thread.currentThread().sleep(500);
			boolean bOK = service.addWorldGift((short)3, 300, "»¶Ó­»Ý¹Ë");
			System.out.println("world gift ok, res is " + bOK);
			//int res = service.getOnlineCount();
			//System.out.println("service.getOnlineCount ok, res is " + res);
			//int rid = service.getRoleIDByUserName("r_0_1");
			//System.out.println("query roleid is " + rid);
			//for(int i = 0; i < 2000; ++i)
			//{
			//	byte bres = service.addMoney(i, 1000, (byte)1);
			//}
			System.out.println("add money res is " + "ok");
			/*
			byte bres = service.addItem(rid, (short)1, 3, (byte)1);
			System.out.println("add item res is " + bres);
			bres = service.sendMail(rid, "lalalaeadsfsdf");
			System.out.println("send msg res is " + bres);
			bres = service.addMoney(rid, 1000, (byte)1);
			System.out.println("add money res is " + bres);
			bres = service.announce((byte)10, "lalalaeadsfsdf");
			System.out.println("send msg res is " + bres);
			Service.RoleBrief b = service.getRoleBrief(rid);
			System.out.println("brief name = " + b.name + ", money = " + b.money);
			System.out.println("general name = " + service.getGeneralName((short)1));
			System.out.println("item name = " + service.getItemName((short)1));
			System.out.println("equip name = " + service.getEquipName((short)101));
			List<Service.ItemBrief> items = service.getItems(rid);
			for(Service.ItemBrief e : items)
				System.out.println("\titem " + service.getItemName(e.id) + ", count = " + e.count);
			List<Service.GeneralBrief> generals = service.getGenerals(rid);
			for(Service.GeneralBrief e : generals)
				System.out.println("\tgeneral " + service.getGeneralName(e.id) + ", lvl = " + e.lvl);
			List<Service.GeneralEquip> generalEquips = service.getGeneralEquips(rid);
			for(Service.GeneralEquip e : generalEquips)
				System.out.println("\tgeneralequip " + service.getGeneralName(e.generalID) + ", equip " 
						+ service.getEquipName(e.equipID));
			*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		service.destroy();
	}
}

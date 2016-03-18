
package i3k.gm;

import i3k.SBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ket.kio.NetAddress;

public class ServiceImp extends GMClient implements Service
{
		
	@Override
	public void init(ServiceConfig cfg)
	{
		locker = new ReentrantLock();
		condIdle  = locker.newCondition();
		condRes  = locker.newCondition();
		bIdle = true;
		bRes = false;
		super.init(cfg);
		super.start();
	}

	@Override
	public void destroy()
	{
		super.destroy();
	}

	@Override
	public int getOnlineCount() throws ServiceException
	{
		testOpen();
		lockIdle();
		managerRPC.queryOnlineUsers();
		lockRes();
		return scnt;
	}
	
	@Override
	public void onQueryOnlineUserRes(int rcnt, int scnt)
	{
		this.scnt = scnt;
		unlockRes();
	}
	
	@Override
	public void onDataQueryRes(SBean.DataQueryRes res)
	{
		switch( res.req.qtype )
		{
		case SBean.DataQueryReq.eRoleID:
			{
				if( res.res == 0 )
					roleid = res.iRes1;
				else 
					roleid = -1;
				unlockRes();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onWorldGift(byte res)
	{
		this.res = res;
		unlockRes();
	}
	
	@Override
	public void onDataMod(SBean.DataModifyRes res)
	{
		this.res = res.res;
		unlockRes();
	}
	
	@Override
	public void onSessionClose()
	{
		unlockRes();
	}
	
	@Override
	public void onSysMessage(byte res)
	{
		this.res = res;
		unlockRes();
	}
	
	@Override
	public void onAnnounce(byte res)
	{
		this.res = res;
		unlockRes();
	}
	
	@Override
	public void onRoleData(SBean.RoleDataRes res)
	{
		this.rdRes = res;
		unlockRes();
	}
	
	void testOpen() throws ServiceException
	{
		if( ! managerRPC.testOpen() )
			throw new ServiceException("服务器连接失败");
	}
	
	void testToolData() throws ServiceException
	{
		if( toolData == null )
			throw new ServiceException("配置数据读取失败");
	}

	int scnt = 0;
	int roleid = 0;
	byte res = 0;
	SBean.RoleDataRes rdRes;

	@Override
	public byte addItem(int roleID, short id, int cnt, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eAdd, 
				SBean.DataModifyReq.eItem, mail, id, cnt));
		
		lockRes();
		return res;
	}
	
	@Override
	public byte addEquip(int roleID, short id, int cnt, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eAdd, 
				SBean.DataModifyReq.eEquip, mail, id, cnt));
		
		lockRes();
		return res;
	}
	
	@Override
	public byte addGeneral(int roleID, short id, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eAdd, 
				SBean.DataModifyReq.eGeneral, mail, id, 1));
		
		lockRes();
		return res;
	}

	@Override
	public byte modSeyen(int roleID, short id, int cnt, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eSet, 
				SBean.DataModifyReq.eSeyen, mail, id, cnt));
		
		lockRes();
		return res;
	}

	@Override
	public int getRoleIDByUserName(String uname) throws ServiceException
	{
		testOpen();
		lockIdle();
		roleid = -1;
		managerRPC.queryRoleIDByUserName(uname);
		lockRes();
		return roleid;
	}
	
	@Override
	public int getRoleIDByRoleName(String rname) throws ServiceException
	{
		testOpen();
		lockIdle();
		roleid = -1;
		managerRPC.queryRoleIDByRoleName(rname);
		lockRes();
		return roleid;
	}

	@Override
	public byte sendMail(int roleID, String content) throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		// TODO yxh managerRPC.sendSysMessage(roleID, content);
		
		lockRes();
		return res;
	}

	@Override
	public byte announce(byte times, String content) throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.announce(content, times);
		
		lockRes();
		return res;
	}

	@Override
	public RoleBrief getRoleBrief(int roleID) throws ServiceException
	{
		testOpen();
		lockIdle();
		rdRes = null;
		managerRPC.getRoleData(new SBean.RoleDataReq(SBean.RoleDataReq.eBrief, roleID));
		
		lockRes();
		if( rdRes != null && rdRes.res == SBean.RoleDataRes.eOK )
		{
			if( rdRes.brief == null )
				return null;
			RoleBrief b = new RoleBrief();
			b.id = rdRes.brief.id;
			b.name = rdRes.brief.name;
			b.uname = rdRes.brief.uname;
			b.level = rdRes.brief.lvl;
			b.money = rdRes.brief.money;
			b.stone = rdRes.brief.stone;
			b.gcount = rdRes.brief.gcount;
			b.icount = rdRes.brief.icount;
			b.ecount = rdRes.brief.ecount;			
			b.id = rdRes.brief.id;
			b.id = rdRes.brief.id;			
			b.id = rdRes.brief.id;
			b.id = rdRes.brief.id;
			//b.jianghunPoint = rdRes.brief.exp3;
			return b;
		}
		return null;
	}

	@Override
	public String getGeneralName(short gid) throws ServiceException
	{
		testToolData();
		return toolData.getGeneralName(gid);
	}
	
	@Override
	public String getEquipName(short eid) throws ServiceException
	{
		testToolData();
		return toolData.getEquipName(eid);
	}
	
	@Override
	public String getItemName(short iid) throws ServiceException
	{
		testToolData();
		return toolData.getItemName(iid);
	}

	@Override
	public List<EquipBrief> getEquips(int roleID) throws ServiceException
	{
		testOpen();
		lockIdle();
		rdRes = null;
		managerRPC.getRoleData(new SBean.RoleDataReq(SBean.RoleDataReq.eEquip, roleID));
		
		lockRes();
		if( rdRes != null && rdRes.res == SBean.RoleDataRes.eOK )
		{
			if( rdRes.equips == null )
				return null;
			List<EquipBrief> r = new ArrayList<EquipBrief>();
			for(SBean.DBRoleEquip e : rdRes.equips)
			{
				EquipBrief b = new EquipBrief();
				b.id = e.id;
				b.count = e.count;
				r.add(b);
			}
			return r;
		}
		return null;
	}

	@Override
	public List<ItemBrief> getItems(int roleID) throws ServiceException
	{
		testOpen();
		lockIdle();
		rdRes = null;
		managerRPC.getRoleData(new SBean.RoleDataReq(SBean.RoleDataReq.eItem, roleID));
		
		lockRes();
		if( rdRes != null && rdRes.res == SBean.RoleDataRes.eOK )
		{
			if( rdRes.items == null )
				return null;
			List<ItemBrief> r = new ArrayList<ItemBrief>();
			for(SBean.DBRoleItem e : rdRes.items)
			{
				ItemBrief b = new ItemBrief();
				b.id = e.id;
				b.count = e.count;
				int count =b.count;
				
				if(count<=0){
					b.count=60000+count;
				}
				r.add(b);
			}
			return r;
		}
		return null;
	}

	@Override
	public List<GeneralBrief> getGenerals(int roleID) throws ServiceException
	{
		testOpen();
		lockIdle();
		rdRes = null;
		managerRPC.getRoleData(new SBean.RoleDataReq(SBean.RoleDataReq.eGeneral, roleID));
		
		lockRes();
		if( rdRes != null && rdRes.res == SBean.RoleDataRes.eOK )
		{
			if( rdRes.generals == null )
				return null;
			List<GeneralBrief> r = new ArrayList<GeneralBrief>();
			for(SBean.RoleDataGeneral e : rdRes.generals)
			{
				GeneralBrief b = new GeneralBrief();
				b.id = e.id;
				b.lvl = e.lvl;
				b.evoLvl = e.evoLvl;
				b.advLvl = e.advLvl;
				r.add(b);
			}
			return r;
		}
		return null;
	}

	@Override
	public List<GeneralEquip> getGeneralEquips(int roleID) throws ServiceException
	{
		return null;
	}
	

	@Override
	public boolean addWorldGift(short itemID, int time, String mail) throws ServiceException
	{
		if( itemID <= 0 || time <= 0 || mail == null || mail.equals("") )
			return false;
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.addWorldGift(itemID, mail, time);
		lockRes();
		return res == 0;
	}

	@Override
	public byte addMoney(int roleID, int money, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eAdd, 
				SBean.DataModifyReq.eMoney, mail, (short)0, money));
		lockRes();
		return res;
	}
	
	@Override
	public byte addStone(int roleID, int stone, byte mail)
			throws ServiceException
	{
		testOpen();
		lockIdle();
		res = -1;
		managerRPC.modData(new SBean.DataModifyReq(0, roleID, SBean.DataModifyReq.eAdd, 
				SBean.DataModifyReq.eStone, mail, (short)0, stone));		
		lockRes();		
		return res;
	}
	
	private void lockRes() throws ServiceException
	{
		locker.lock();
		try
		{
	       while ( ! bRes )
	    	   condRes.await();
	       	   bIdle = true;
	       	   condIdle.signal();
		} catch (InterruptedException e) {
			throw new ServiceException("系统忙");
		}
		finally
	    {
			locker.unlock();
		}
	}
	
	private void unlockRes()
	{
		locker.lock();
		try
		{
	       bRes = true;
	       condRes.signal();
		}finally
	    {
			locker.unlock();
		}
	}
	
	private void lockIdle() throws ServiceException
	{
		locker.lock();
		try
		{
	       while ( ! bIdle )
	    	   condIdle.await();
	       bIdle = false;
	       bRes = false;
		} catch (InterruptedException e) {
			throw new ServiceException("系统忙");
		}
		finally
	    {
			locker.unlock();
		}
	}

	Lock locker;
	Condition condIdle;
	Condition condRes;
	boolean bIdle = true;
	boolean bRes = false;
}

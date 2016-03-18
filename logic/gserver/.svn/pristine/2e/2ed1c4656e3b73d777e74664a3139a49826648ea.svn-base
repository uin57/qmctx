
package i3k.gm;

import java.util.List;

import ket.util.Stream;

public interface Service
{
	public static class ServiceException extends Exception
	{
		public ServiceException(String msg)
		{
			super(msg);
		}
	}
	
	public static class ServiceConfig
	{
		/**
		 * 服务器地址
		 */
		public String host;
		/**
		 * 服务器端口
		 */
		public int port;
		/**
		 * 配置路径
		 */
		public String cfgPath;
	}
	
	/**
	 * 角色基础信息
	 */
	public static class RoleBrief
	{
		/**
		 * roleID
		 */
		public int id;
		/**
		 * 角色名
		 */
		public String name;
		/**
		 * 账号名
		 */
		public String uname;
		/**
		 * 等级
		 */
		public short level;
		/**
		 * 金钱
		 */
		public int money;
		/**
		 * 玉石
		 */
		public int stone;
		/**
		 * 武将数量
		 */
		public int gcount;
		/**
		 * 道具数量
		 */
		public int icount;
		/**
		 * 装备数量
		 */
		public int ecount;
		/**
		 * 将魂点数
		 */
		public int jianghunPoint;
	}
	
	/**
	 * 装备基础信息
	 */
	public static class EquipBrief
	{
		/**
		 * equipID
		 */
		public short id;
		/**
		 * 装备数量
		 */
		public short count;
	}
	
	/**
	 * 道具基础信息
	 */
	public static class ItemBrief
	{
		/**
		 * itemID
		 */
		public short id;
		/**
		 * 道具数量
		 */
		public int count;
	}
	
	/**
	 * 武将基础信息
	 */
	public static class GeneralBrief
	{
		/**
		 * generalID
		 */
		public short id;
		/**
		 * 等级
		 */
		public short lvl;
		/**
		 * 进阶等级
		 */
		public byte advLvl;
		/**
		 * 进化等级
		 */
		public byte evoLvl;
	}
	
	/**
	 * 武将装备信息
	 */
	public static class GeneralEquip
	{
		/**
		 * generalID
		 */
		public short generalID;
		/**
		 * equipID
		 */
		public short equipID;
	}
	
	/**
	 * 初始化
	 */
	public void init(ServiceConfig cfg);
	
	/**
	 * 销毁
	 */
	public void destroy();
	
	/**
	 * 根据账号名取得 roleID
	 * @return <=0 说明没找到
	 */
	public int getRoleIDByUserName(String uname) throws ServiceException;
	
	/**
	 * 根据角色名取得 roleID
	 * @return <=0 说明没找到
	 */
	public int getRoleIDByRoleName(String rname) throws ServiceException;
	
	/**
	 * 取得在线人数
	 */
	public int getOnlineCount() throws ServiceException;
	
	/**
	 * 全服发放道具，所有在发放时刻之前注册的用户从发放时候到持续时间结束之间登录会通过邮件收到且只收到一次此道具
	 * @param itemID 道具ID
	 * @param time 持续时间（秒）
	 * @param mail 伴随邮件内容
	 */
	public boolean addWorldGift(short itemID, int time, String mail) throws ServiceException;
	
	/**
	 * 发放游戏币
	 * @param roleID
	 * @param money
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte addMoney(int roleID, int money, byte mail) throws ServiceException;
	
	/**
	 * 发放玉石
	 * @param roleID
	 * @param stone
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte addStone(int roleID, int stone, byte mail) throws ServiceException;
	
	/**
	 * 发放道具
	 * @param roleID
	 * @param id 道具ID
	 * @param cnt 数量
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte addItem(int roleID, short id, int cnt, byte mail) throws ServiceException;
	
	/**
	 * 发放装备
	 * @param roleID
	 * @param id 装备ID
	 * @param cnt 数量
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte addEquip(int roleID, short id, int cnt, byte mail) throws ServiceException;
	
	/**
	 * 发放武将
	 * @param roleID
	 * @param id 武将ID
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte addGeneral(int roleID, short id, byte mail) throws ServiceException;
	
	/**
	 * 修改精元
	 * @param roleID
	 * @param id 武将ID
	 * @param mail 1:发送邮件提醒  0:不发送
	 * @return 0: 成功
	 */
	public byte modSeyen(int roleID, short id, int cnt, byte mail) throws ServiceException;
	
	/**
	 * 发送邮件
	 * @param roleID
	 * @param content 内容
	 * @return 0: 成功
	 */	
	public byte sendMail(int roleID, String content) throws ServiceException;
	
	/**
	 * 发送广播
	 * @param times 播放几次
	 * @param content 内容
	 * @return 0: 成功
	 */	
	public byte announce(byte times, String content) throws ServiceException;
	
	/**
	 * 取得角色基础信息
	 * @param roleID roleID
	 * @return not null: 成功
	 */	
	public RoleBrief getRoleBrief(int roleID) throws ServiceException;
	
	/**
	 * 取得装备信息
	 * @param roleID roleID
	 * @return not null: 成功
	 */	
	public List<EquipBrief> getEquips(int roleID) throws ServiceException;
	
	/**
	 * 取得道具信息
	 * @param roleID roleID
	 * @return not null: 成功
	 */	
	public List<ItemBrief> getItems(int roleID) throws ServiceException;
	
	/**
	 * 取得武将信息
	 * @param roleID roleID
	 * @return not null: 成功
	 */	
	public List<GeneralBrief> getGenerals(int roleID) throws ServiceException;
	
	/**
	 * 取得武将装备信息
	 * @param roleID roleID
	 * @return not null: 成功
	 */	
	public List<GeneralEquip> getGeneralEquips(int roleID) throws ServiceException;
	
	/**
	 * 取得武将名称
	 * @param gid 武将ID
	 */
	public String getGeneralName(short gid) throws ServiceException;
	
	/**
	 * 取得装备
	 * @param eid 装备ID
	 */
	public String getEquipName(short eid) throws ServiceException;
	
	/**
	 * 取得道具名称
	 * @param iid 道具ID
	 */
	public String getItemName(short iid) throws ServiceException;
}

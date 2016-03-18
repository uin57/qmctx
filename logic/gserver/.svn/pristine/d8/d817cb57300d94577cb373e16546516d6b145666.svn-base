// modified by i3k.gtool.QQMetaGen at Thu Mar 17 10:14:53 CST 2016.

package i3k;

import java.util.List;
import java.util.ArrayList;
import ket.util.Stream;

public final class IDIP
{

	public static final int IDIP_RSP_SUCCESS = 0;
	public static final int IDIP_RSP_FAILED = -1;
	public static final int IDIP_RSP_ROLE_NOT_EXIST = -2;
	public static final int IDIP_RSP_ITEM_ID_INVALID = -3;
	public static final int IDIP_RSP_EQUIP_ID_INVALID = -4;
	public static final int IDIP_RSP_GENERAL_ID_INVALID = -5;

	public static final int IDIP_HEADER_RESULT_SUCCESS = 0;
	public static final int IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS = 1;
	public static final int IDIP_HEADER_RESULT_NETWORK_EXCEPTION = -1;
	public static final int IDIP_HEADER_RESULT_TIMEOUT = -2;
	public static final int IDIP_HEADER_RESULT_BD_EXCEPTION = -3;
	public static final int IDIP_HEADER_RESULT_API_EXCEPTION = -4;
	public static final int IDIP_HEADER_RESULT_SERVER_BUSY = -5;
	public static final int IDIP_HEADER_RESULT_OTHER_ERROR = -100;

	// IDIP命令编码
	// 封号请求
	public static final int IDIP_DO_BAN_USR_REQ = 0x1001;
	// 封号应答
	public static final int IDIP_DO_BAN_USR_RSP = 0x1002;
	// 解封请求
	public static final int IDIP_DO_UNBAN_USR_REQ = 0x1003;
	// 解封应答
	public static final int IDIP_DO_UNBAN_USR_RSP = 0x1004;
	// 删除道具请求
	public static final int IDIP_DO_DEL_ITEM_REQ = 0x1005;
	// 删除道具应答
	public static final int IDIP_DO_DEL_ITEM_RSP = 0x1006;
	// 修改角色经验请求
	public static final int IDIP_DO_UPDATE_EXP_REQ = 0x1007;
	// 修改角色经验应答
	public static final int IDIP_DO_UPDATE_EXP_RSP = 0x1008;
	// 修改用户体力请求
	public static final int IDIP_DO_UPDATE_PHYSICAL_REQ = 0x1009;
	// 修改用户体力应答
	public static final int IDIP_DO_UPDATE_PHYSICAL_RSP = 0x100a;
	// 修改用户钻石请求
	public static final int IDIP_DO_UPDATE_DIAMOND_REQ = 0x100b;
	// 修改用户钻石应答
	public static final int IDIP_DO_UPDATE_DIAMOND_RSP = 0x100c;
	// 修改用户金钱请求
	public static final int IDIP_DO_UPDATE_MONEY_REQ = 0x100d;
	// 修改用户金钱应答
	public static final int IDIP_DO_UPDATE_MONEY_RSP = 0x100e;
	// 发放道具请求
	public static final int IDIP_DO_SEND_ITEM_REQ = 0x100f;
	// 发放道具应答
	public static final int IDIP_DO_SEND_ITEM_RSP = 0x1010;
	// 查询玩家基本信息请求
	public static final int IDIP_QUERY_USR_INFO_REQ = 0x1011;
	// 查询玩家基本信息应答
	public static final int IDIP_QUERY_USR_INFO_RSP = 0x1012;
	// 查询武将信息请求
	public static final int IDIP_QUERY_GENERAL_INFO_REQ = 0x1013;
	// 查询武将信息应答
	public static final int IDIP_QUERY_GENERAL_INFO_RSP = 0x1014;
	// 查询武将装备信息请求
	public static final int IDIP_QUERY_GENERAL_EQUIP_INFO_REQ = 0x1015;
	// 查询武将装备信息应答
	public static final int IDIP_QUERY_GENERAL_EQUIP_INFO_RSP = 0x1016;
	// 查询背包当前存量信息请求
	public static final int IDIP_QUERY_BAG_INFO_REQ = 0x1019;
	// 查询背包当前存量信息应答
	public static final int IDIP_QUERY_BAG_INFO_RSP = 0x101a;
	// 发放全区全服邮件请求
	public static final int IDIP_DO_SEND_MAIL_ALL_REQ = 0x101b;
	// 发放全区全服邮件应答
	public static final int IDIP_DO_SEND_MAIL_ALL_RSP = 0x101c;
	// 发送滚动公告请求
	public static final int IDIP_DO_SEND_ROLLNOTICE_REQ = 0x101d;
	// 发送滚动公告应答
	public static final int IDIP_DO_SEND_ROLLNOTICE_RSP = 0x101e;
	// 查询公告请求
	public static final int IDIP_QUERY_NOTICE_REQ = 0x101f;
	// 查询公告应答
	public static final int IDIP_QUERY_NOTICE_RSP = 0x1020;
	// 删除公告请求
	public static final int IDIP_DO_DEL_NOTICE_REQ = 0x1021;
	// 删除公告应答
	public static final int IDIP_DO_DEL_NOTICE_RSP = 0x1022;
	// 发送邮件请求
	public static final int IDIP_DO_SEND_MAIL_REQ = 0x1023;
	// 发送邮件应答
	public static final int IDIP_DO_SEND_MAIL_RSP = 0x1024;
	// 竞技场查询请求
	public static final int IDIP_QUERY_ARENA_REQ = 0x102d;
	// 竞技场查询应答
	public static final int IDIP_QUERY_ARENA_RSP = 0x102e;
	// 清零游戏数据请求
	public static final int IDIP_DO_CLEAR_DATA_REQ = 0x1025;
	// 清零游戏数据应答
	public static final int IDIP_DO_CLEAR_DATA_RSP = 0x1026;
	// 设置武将等级请求
	public static final int IDIP_DO_SET_GENERAL_LV_REQ = 0x1027;
	// 设置武将等级应答
	public static final int IDIP_DO_SET_GENERAL_LV_RSP = 0x1028;
	// 设置宠物等级请求
	public static final int IDIP_DO_SET_PET_LV_REQ = 0x1029;
	// 设置宠物等级应答
	public static final int IDIP_DO_SET_PET_LV_RSP = 0x102a;
	// 查询背包当前装备请求
	public static final int IDIP_QUERY_BAGITEM_INFO_REQ = 0x102b;
	// 查询背包当前装备应答
	public static final int IDIP_QUERY_BAGITEM_INFO_RSP = 0x102c;
	// 修改vip经验请求
	public static final int IDIP_DO_UPDATE_VIP_EXP_REQ = 0x1043;
	// 修改vip经验应答
	public static final int IDIP_DO_UPDATE_VIP_EXP_RSP = 0x1044;
	// 修改君主等级请求
	public static final int IDIP_DO_UPDATE_KING_LEVEL_REQ = 0x1045;
	// 修改君主等级应答
	public static final int IDIP_DO_UPDATE_KING_LEVEL_RSP = 0x1046;
	// 查询玩家战斗力请求
	public static final int IDIP_QUERY_FIGHT_INFO_REQ = 0x1047;
	// 查询玩家战斗力应答
	public static final int IDIP_QUERY_FIGHT_INFO_RSP = 0x1048;
	// 查询玩家VIP等级请求
	public static final int IDIP_QUERY_VIP_LEVEL_REQ = 0x1049;
	// 查询玩家VIP等级应答
	public static final int IDIP_QUERY_VIP_LEVEL_RSP = 0x104a;
	// 查询玩家签到信息请求
	public static final int IDIP_QUERY_USER_SIGNIN_REQ = 0x104b;
	// 查询玩家签到信息应答
	public static final int IDIP_QUERY_USER_SIGNIN_RSP = 0x104c;
	// 查询openid基本信息（AQ）请求
	public static final int IDIP_AQ_QUERY_USERBASEINFO_REQ = 0x102f;
	// 查询openid基本信息（AQ）应答
	public static final int IDIP_AQ_QUERY_USERBASEINFO_RSP = 0x1030;
	// 发送邮件（AQ）请求
	public static final int IDIP_AQ_DO_SEND_MAIL_REQ = 0x1031;
	// 发送邮件（AQ）应答
	public static final int IDIP_AQ_DO_SEND_MAIL_RSP = 0x1032;
	// 修改游戏币数量（AQ）请求
	public static final int IDIP_AQ_DO_UPDATE_MONEY_REQ = 0x1035;
	// 修改游戏币数量（AQ）应答
	public static final int IDIP_AQ_DO_UPDATE_MONEY_RSP = 0x1036;
	// 禁止玩法（AQ）请求
	public static final int IDIP_AQ_DO_BAN_PLAY_REQ = 0x1037;
	// 禁止玩法（AQ）应答
	public static final int IDIP_AQ_DO_BAN_PLAY_RSP = 0x1038;
	// 封号（AQ）请求
	public static final int IDIP_AQ_DO_BAN_USR_REQ = 0x103f;
	// 封号（AQ）应答
	public static final int IDIP_AQ_DO_BAN_USR_RSP = 0x1040;
	// 解除处罚（AQ）请求
	public static final int IDIP_AQ_DO_RELIEVE_PUNISH_REQ = 0x1041;
	// 解除处罚（AQ）应答
	public static final int IDIP_AQ_DO_RELIEVE_PUNISH_RSP = 0x1042;
	// 修改钻石数量（AQ）请求
	public static final int IDIP_AQ_DO_UPDATE_DIAMOND_REQ = 0x1033;
	// 修改钻石数量（AQ）应答
	public static final int IDIP_AQ_DO_UPDATE_DIAMOND_RSP = 0x1034;
	// 查询用户签名和公会公告（AQ）请求
	public static final int IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_REQ = 0x1039;
	// 查询用户签名和公会公告（AQ）应答
	public static final int IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_RSP = 0x103a;
	// 设置用户签名和公会公告（AQ）请求
	public static final int IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_REQ = 0x103b;
	// 设置用户签名和公会公告（AQ）应答
	public static final int IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP = 0x103c;
	// 禁言（AQ）请求
	public static final int IDIP_AQ_DO_MASKCHAT_USR_REQ = 0x103d;
	// 禁言（AQ）应答
	public static final int IDIP_AQ_DO_MASKCHAT_USR_RSP = 0x103e;

	// 系统宏
	// 封装包包体的最大值, 基本的数据包的大小在15K以下，只有查询邮件列表这个数据量比较大，在23K左右
	public static final int IDIP_BODY_LENGTH = 24000;
	// SERVICENAME的最大长度
	public static final int SERVICE_NAME_LENGTH = 16;
	// 加密串的最大长度
	public static final int AUTHENTICATE_LENGTH = 32;
	// 错误码
	public static final int ERROR_MSG_LENGTH = 100;
	// openid的长度
	public static final int MAX_OPENID_LEN = 64;
	// 流水号，由前端生成，不需要填写的长度
	public static final int MAX_SERIAL_LEN = 64;
	// 返回消息的长度
	public static final int MAX_RETMSG_LEN = 100;
	// 武将名的长度
	public static final int MAX_GENERALNAME_LEN = 64;
	// 武将信息列表的数组长度
	public static final int MAX_GENERALLIST_NUM = 100;
	// 武将装备信息列表的数组长度
	public static final int MAX_GENERALEQUIPLIST_NUM = 100;
	// 道具名称的长度
	public static final int MAX_ITEMNAME_LEN = 64;
	// 背包信息列表的数组长度
	public static final int MAX_BAGLIST_NUM = 200;
	// 装备名称的长度
	public static final int MAX_EQUIPNAME_LEN = 64;
	// 邮件标题的长度
	public static final int MAX_MAILTITLE_LEN = 70;
	// 邮件内容的长度
	public static final int MAX_MAILCONTENT_LEN = 1000;
	// 公告内容的长度
	public static final int MAX_NOTICECONTENT_LEN = 300;
	// 公告信息列表的数组长度
	public static final int MAX_NOTICELIST_NUM = 10;
	// 背包当前装备列表的数组长度
	public static final int MAX_BACKPACKLIST_NUM = 200;
	// 角色名称的长度
	public static final int MAX_ROLENAME_LEN = 64;
	// 用户签名的长度
	public static final int MAX_USRSIGN_LEN = 64;
	// 设定内容的长度
	public static final int MAX_GUILDNOTICE_LEN = 300;
	// 禁言原因的长度
	public static final int MAX_REASON_LEN = 64;
	// 奖励ID的长度
	public static final int MAX_AWARDID_LEN = 64;

	public static final int PACKET_HEADER_SIZE = 4 + 4 + 4 + SERVICE_NAME_LENGTH + 4 + 4 + AUTHENTICATE_LENGTH + 4 + ERROR_MSG_LENGTH;



	// IDIP消息头
	public static class IdipHeader implements Stream.IStreamable
	{

		public IdipHeader() { }

		public IdipHeader(int PacketLen, int Cmdid, int Seqid, String ServiceName, 
		                  int SendTime, int Version, String Authenticate, int Result, 
		                  String RetErrMsg)
		{
			this.PacketLen = PacketLen;
			this.Cmdid = Cmdid;
			this.Seqid = Seqid;
			this.ServiceName = ServiceName;
			this.SendTime = SendTime;
			this.Version = Version;
			this.Authenticate = Authenticate;
			this.Result = Result;
			this.RetErrMsg = RetErrMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			PacketLen = is.popInteger();
			Cmdid = is.popInteger();
			Seqid = is.popInteger();
			ServiceName = is.popString(SERVICE_NAME_LENGTH);
			SendTime = is.popInteger();
			Version = is.popInteger();
			Authenticate = is.popString(AUTHENTICATE_LENGTH);
			Result = is.popInteger();
			RetErrMsg = is.popString(ERROR_MSG_LENGTH);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(PacketLen);
			os.pushInteger(Cmdid);
			os.pushInteger(Seqid);
			os.pushString(ServiceName, SERVICE_NAME_LENGTH);
			os.pushInteger(SendTime);
			os.pushInteger(Version);
			os.pushString(Authenticate, AUTHENTICATE_LENGTH);
			os.pushInteger(Result);
			os.pushString(RetErrMsg, ERROR_MSG_LENGTH);
		}

		// 包长
		public int PacketLen;
		// 命令ID
		public int Cmdid;
		// 流水号
		public int Seqid;
		// 服务名
		public String ServiceName;
		// 发送时间YYYYMMDD对应的整数
		public int SendTime;
		// 版本号
		public int Version;
		// 加密串
		public String Authenticate;
		// 错误码,返回码类型：0：处理成功，需要解开包体获得详细信息,1：处理成功，但包体返回为空，不需要处理包体（eg：查询用户角色，用户角色不存在等），-1: 网络通信异常,-2：超时,-3：数据库操作异常,-4：API返回异常,-5：服务器忙,-6：其他错误,小于-100 ：用户自定义错误，需要填写szRetErrMsg
		public int Result;
		// 错误信息
		public String RetErrMsg;
	}

	// IDIP数据包
	public static class IdipDataPaket implements Stream.IStreamable
	{

		public IdipDataPaket() { }

		public IdipDataPaket(IdipHeader IdipHead, String IdipBody)
		{
			this.IdipHead = IdipHead;
			this.IdipBody = IdipBody;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( IdipHead == null )
				IdipHead = new IdipHeader();
			is.pop(IdipHead);
			IdipBody = is.popString(IDIP_BODY_LENGTH);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(IdipHead);
			os.pushString(IdipBody, IDIP_BODY_LENGTH);
		}

		// 包头信息
		public IdipHeader IdipHead;
		// 包体信息
		public String IdipBody;
	}

	// 武将信息对象
	public static class SGeneralInfo implements Stream.IStreamable
	{

		public SGeneralInfo() { }

		public SGeneralInfo(String GeneralName, int Level, int Grade, int Star, 
		                    byte IsBattle, int TotalPageNo)
		{
			this.GeneralName = GeneralName;
			this.Level = Level;
			this.Grade = Grade;
			this.Star = Star;
			this.IsBattle = IsBattle;
			this.TotalPageNo = TotalPageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			GeneralName = is.popString(MAX_GENERALNAME_LEN);
			Level = is.popInteger();
			Grade = is.popInteger();
			Star = is.popInteger();
			IsBattle = is.popByte();
			TotalPageNo = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(GeneralName, MAX_GENERALNAME_LEN);
			os.pushInteger(Level);
			os.pushInteger(Grade);
			os.pushInteger(Star);
			os.pushByte(IsBattle);
			os.pushInteger(TotalPageNo);
		}

		// 武将名
		public String GeneralName;
		// 等级
		public int Level;
		// 进阶度
		public int Grade;
		// 星级
		public int Star;
		// 武将上阵1/未上阵0
		public byte IsBattle;
		// 总页码
		public int TotalPageNo;
	}

	// 武将装备信息对象
	public static class SGeneralEquipInfo implements Stream.IStreamable
	{

		public SGeneralEquipInfo() { }

		public SGeneralEquipInfo(String GeneralName, long EquipId, String EquipName, byte IsBattle, 
		                         int TotalPageNo)
		{
			this.GeneralName = GeneralName;
			this.EquipId = EquipId;
			this.EquipName = EquipName;
			this.IsBattle = IsBattle;
			this.TotalPageNo = TotalPageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			GeneralName = is.popString(MAX_GENERALNAME_LEN);
			EquipId = is.popLong();
			EquipName = is.popString(MAX_EQUIPNAME_LEN);
			IsBattle = is.popByte();
			TotalPageNo = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(GeneralName, MAX_GENERALNAME_LEN);
			os.pushLong(EquipId);
			os.pushString(EquipName, MAX_EQUIPNAME_LEN);
			os.pushByte(IsBattle);
			os.pushInteger(TotalPageNo);
		}

		// 武将名
		public String GeneralName;
		// 装备ID
		public long EquipId;
		// 装备名称
		public String EquipName;
		// 武将上阵1/未上阵0
		public byte IsBattle;
		// 总页码
		public int TotalPageNo;
	}

	// 背包信息对象
	public static class SBagInfo implements Stream.IStreamable
	{

		public SBagInfo() { }

		public SBagInfo(String ItemName, long ItemId, int ItemNum, int TotalPageNo)
		{
			this.ItemName = ItemName;
			this.ItemId = ItemId;
			this.ItemNum = ItemNum;
			this.TotalPageNo = TotalPageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			ItemName = is.popString(MAX_ITEMNAME_LEN);
			ItemId = is.popLong();
			ItemNum = is.popInteger();
			TotalPageNo = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(ItemName, MAX_ITEMNAME_LEN);
			os.pushLong(ItemId);
			os.pushInteger(ItemNum);
			os.pushInteger(TotalPageNo);
		}

		// 道具名称
		public String ItemName;
		// 道具ID
		public long ItemId;
		// 道具存量
		public int ItemNum;
		// 总页码
		public int TotalPageNo;
	}

	// 公告信息对象
	public static class SNoticeInfo implements Stream.IStreamable
	{

		public SNoticeInfo() { }

		public SNoticeInfo(long SendTime, long BeginTime, long EndTime, long NoticeId, 
		                   String NoticeContent)
		{
			this.SendTime = SendTime;
			this.BeginTime = BeginTime;
			this.EndTime = EndTime;
			this.NoticeId = NoticeId;
			this.NoticeContent = NoticeContent;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			SendTime = is.popLong();
			BeginTime = is.popLong();
			EndTime = is.popLong();
			NoticeId = is.popLong();
			NoticeContent = is.popString(MAX_NOTICECONTENT_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushLong(SendTime);
			os.pushLong(BeginTime);
			os.pushLong(EndTime);
			os.pushLong(NoticeId);
			os.pushString(NoticeContent, MAX_NOTICECONTENT_LEN);
		}

		// 公告发送时间
		public long SendTime;
		// 公告生效时间
		public long BeginTime;
		// 公告结束时间
		public long EndTime;
		// 公告ID
		public long NoticeId;
		// 公告内容
		public String NoticeContent;
	}

	// 背包当前装备列表信息
	public static class BackPackListInfo implements Stream.IStreamable
	{

		public BackPackListInfo() { }

		public BackPackListInfo(String EquipName, long EquipId, int EquipNum, int TotalPageNo)
		{
			this.EquipName = EquipName;
			this.EquipId = EquipId;
			this.EquipNum = EquipNum;
			this.TotalPageNo = TotalPageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			EquipName = is.popString(MAX_EQUIPNAME_LEN);
			EquipId = is.popLong();
			EquipNum = is.popInteger();
			TotalPageNo = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(EquipName, MAX_EQUIPNAME_LEN);
			os.pushLong(EquipId);
			os.pushInteger(EquipNum);
			os.pushInteger(TotalPageNo);
		}

		// 装备名称
		public String EquipName;
		// 装备ID
		public long EquipId;
		// 装备存量
		public int EquipNum;
		// 总页码
		public int TotalPageNo;
	}

	// 封号请求
	public static class DoBanUsrReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_BAN_USR_REQ;

		public DoBanUsrReq() { }

		public DoBanUsrReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                   int BanTime, String Reason, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.BanTime = BanTime;
			this.Reason = Reason;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			BanTime = is.popInteger();
			Reason = is.popString(MAX_REASON_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(BanTime);
			os.pushString(Reason, MAX_REASON_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 封号时长0 永久，**秒）
		public int BanTime;
		// 封号原因
		public String Reason;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 封号应答
	public static class DoBanUsrRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_BAN_USR_RSP;

		public DoBanUsrRsp() { }

		public DoBanUsrRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 解封请求
	public static class DoUnbanUsrReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UNBAN_USR_REQ;

		public DoUnbanUsrReq() { }

		public DoUnbanUsrReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                     int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 解封应答
	public static class DoUnbanUsrRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UNBAN_USR_RSP;

		public DoUnbanUsrRsp() { }

		public DoUnbanUsrRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 删除道具请求
	public static class DoDelItemReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_DEL_ITEM_REQ;

		public DoDelItemReq() { }

		public DoDelItemReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                    long ItemId, int ItemNum, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.ItemId = ItemId;
			this.ItemNum = ItemNum;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			ItemId = is.popLong();
			ItemNum = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushLong(ItemId);
			os.pushInteger(ItemNum);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 道具ID
		public long ItemId;
		// 数量
		public int ItemNum;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 删除道具应答
	public static class DoDelItemRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_DEL_ITEM_RSP;

		public DoDelItemRsp() { }

		public DoDelItemRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改角色经验请求
	public static class DoUpdateExpReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_EXP_REQ;

		public DoUpdateExpReq() { }

		public DoUpdateExpReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                      int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 修改值（正加负减）
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改角色经验应答
	public static class DoUpdateExpRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_EXP_RSP;

		public DoUpdateExpRsp() { }

		public DoUpdateExpRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改用户体力请求
	public static class DoUpdatePhysicalReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_PHYSICAL_REQ;

		public DoUpdatePhysicalReq() { }

		public DoUpdatePhysicalReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                           int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 数量（正加负减）
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改用户体力应答
	public static class DoUpdatePhysicalRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_PHYSICAL_RSP;

		public DoUpdatePhysicalRsp() { }

		public DoUpdatePhysicalRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改用户钻石请求
	public static class DoUpdateDiamondReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_DIAMOND_REQ;

		public DoUpdateDiamondReq() { }

		public DoUpdateDiamondReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                          int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 数量（正加负减）
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改用户钻石应答
	public static class DoUpdateDiamondRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_DIAMOND_RSP;

		public DoUpdateDiamondRsp() { }

		public DoUpdateDiamondRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改用户金钱请求
	public static class DoUpdateMoneyReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_MONEY_REQ;

		public DoUpdateMoneyReq() { }

		public DoUpdateMoneyReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                        int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 数量（正加负减）
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改用户金钱应答
	public static class DoUpdateMoneyRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_MONEY_RSP;

		public DoUpdateMoneyRsp() { }

		public DoUpdateMoneyRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 发放道具请求
	public static class DoSendItemReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_ITEM_REQ;

		public DoSendItemReq() { }

		public DoSendItemReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                     long ItemId, int ItemNum, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.ItemId = ItemId;
			this.ItemNum = ItemNum;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			ItemId = is.popLong();
			ItemNum = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushLong(ItemId);
			os.pushInteger(ItemNum);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 道具ID
		public long ItemId;
		// 道具数量
		public int ItemNum;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 发放道具应答
	public static class DoSendItemRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_ITEM_RSP;

		public DoSendItemRsp() { }

		public DoSendItemRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 查询玩家基本信息请求
	public static class QueryUsrInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_USR_INFO_REQ;

		public QueryUsrInfoReq() { }

		public QueryUsrInfoReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询玩家基本信息应答
	public static class QueryUsrInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_USR_INFO_RSP;

		public QueryUsrInfoRsp() { }

		public QueryUsrInfoRsp(int Level, int Exp, int Money, int Diamond, 
		                       int Physical, int MaxPhysical, int ItemTypeNum, long RegisterTime, 
		                       byte IsOnline, byte Status, long BanEndTime, int NormalBigPassProgress, 
		                       int NormalSmallPassProgress, int HeroBigPassProgress, int HeroSmallPassProgress, String RoleName, 
		                       long LastLoginTime)
		{
			this.Level = Level;
			this.Exp = Exp;
			this.Money = Money;
			this.Diamond = Diamond;
			this.Physical = Physical;
			this.MaxPhysical = MaxPhysical;
			this.ItemTypeNum = ItemTypeNum;
			this.RegisterTime = RegisterTime;
			this.IsOnline = IsOnline;
			this.Status = Status;
			this.BanEndTime = BanEndTime;
			this.NormalBigPassProgress = NormalBigPassProgress;
			this.NormalSmallPassProgress = NormalSmallPassProgress;
			this.HeroBigPassProgress = HeroBigPassProgress;
			this.HeroSmallPassProgress = HeroSmallPassProgress;
			this.RoleName = RoleName;
			this.LastLoginTime = LastLoginTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Level = is.popInteger();
			Exp = is.popInteger();
			Money = is.popInteger();
			Diamond = is.popInteger();
			Physical = is.popInteger();
			MaxPhysical = is.popInteger();
			ItemTypeNum = is.popInteger();
			RegisterTime = is.popLong();
			IsOnline = is.popByte();
			Status = is.popByte();
			BanEndTime = is.popLong();
			NormalBigPassProgress = is.popInteger();
			NormalSmallPassProgress = is.popInteger();
			HeroBigPassProgress = is.popInteger();
			HeroSmallPassProgress = is.popInteger();
			RoleName = is.popString(MAX_ROLENAME_LEN);
			LastLoginTime = is.popLong();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Level);
			os.pushInteger(Exp);
			os.pushInteger(Money);
			os.pushInteger(Diamond);
			os.pushInteger(Physical);
			os.pushInteger(MaxPhysical);
			os.pushInteger(ItemTypeNum);
			os.pushLong(RegisterTime);
			os.pushByte(IsOnline);
			os.pushByte(Status);
			os.pushLong(BanEndTime);
			os.pushInteger(NormalBigPassProgress);
			os.pushInteger(NormalSmallPassProgress);
			os.pushInteger(HeroBigPassProgress);
			os.pushInteger(HeroSmallPassProgress);
			os.pushString(RoleName, MAX_ROLENAME_LEN);
			os.pushLong(LastLoginTime);
		}

		// 当前等级
		public int Level;
		// 当前经验
		public int Exp;
		// 当前金钱
		public int Money;
		// 当前钻石数量
		public int Diamond;
		// 当前体力值
		public int Physical;
		// 体力值上限
		public int MaxPhysical;
		// 背包道具种类数量
		public int ItemTypeNum;
		// 注册时间
		public long RegisterTime;
		// 是否在线（0在线，1离线）
		public byte IsOnline;
		// 帐号状态（0 正常，1封号）
		public byte Status;
		// 封号截至时间
		public long BanEndTime;
		// 当前大关卡进度（普通）
		public int NormalBigPassProgress;
		// 当前小关卡进度（普通）
		public int NormalSmallPassProgress;
		// 当前大关卡进度（英雄）
		public int HeroBigPassProgress;
		// 当前小关卡进度（英雄）
		public int HeroSmallPassProgress;
		// 玩家昵称
		public String RoleName;
		// 玩家最后登录时间
		public long LastLoginTime;
	}

	// 查询武将信息请求
	public static class QueryGeneralInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_GENERAL_INFO_REQ;

		public QueryGeneralInfoReq() { }

		public QueryGeneralInfoReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                           byte PageNo)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.PageNo = PageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			PageNo = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(PageNo);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 当前页码
		public byte PageNo;
	}

	// 查询武将信息应答
	public static class QueryGeneralInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_GENERAL_INFO_RSP;

		public QueryGeneralInfoRsp() { }

		public QueryGeneralInfoRsp(int GeneralList_count, List<SGeneralInfo> GeneralList)
		{
			this.GeneralList_count = GeneralList_count;
			this.GeneralList = GeneralList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			GeneralList_count = is.popInteger();
			GeneralList = is.popList(SGeneralInfo.class, GeneralList_count);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(GeneralList_count);
			os.pushList(GeneralList, GeneralList_count);
		}

		// 武将信息列表的最大数量
		public int GeneralList_count;
		// 武将信息列表
		public List<SGeneralInfo> GeneralList;
	}

	// 查询武将装备信息请求
	public static class QueryGeneralEquipInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_GENERAL_EQUIP_INFO_REQ;

		public QueryGeneralEquipInfoReq() { }

		public QueryGeneralEquipInfoReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                                byte PageNo)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.PageNo = PageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			PageNo = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(PageNo);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 当前页码
		public byte PageNo;
	}

	// 查询武将装备信息应答
	public static class QueryGeneralEquipInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_GENERAL_EQUIP_INFO_RSP;

		public QueryGeneralEquipInfoRsp() { }

		public QueryGeneralEquipInfoRsp(int GeneralEquipList_count, List<SGeneralEquipInfo> GeneralEquipList)
		{
			this.GeneralEquipList_count = GeneralEquipList_count;
			this.GeneralEquipList = GeneralEquipList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			GeneralEquipList_count = is.popInteger();
			GeneralEquipList = is.popList(SGeneralEquipInfo.class, GeneralEquipList_count);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(GeneralEquipList_count);
			os.pushList(GeneralEquipList, GeneralEquipList_count);
		}

		// 武将装备信息列表的最大数量
		public int GeneralEquipList_count;
		// 武将装备信息列表
		public List<SGeneralEquipInfo> GeneralEquipList;
	}

	// 查询背包当前存量信息请求
	public static class QueryBagInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_BAG_INFO_REQ;

		public QueryBagInfoReq() { }

		public QueryBagInfoReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                       byte PageNo)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.PageNo = PageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			PageNo = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(PageNo);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID（-1 全部，*** 大于0的具体小区）
		public int Partition;
		// openid
		public String OpenId;
		// 当前页码
		public byte PageNo;
	}

	// 查询背包当前存量信息应答
	public static class QueryBagInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_BAG_INFO_RSP;

		public QueryBagInfoRsp() { }

		public QueryBagInfoRsp(int BagList_count, List<SBagInfo> BagList)
		{
			this.BagList_count = BagList_count;
			this.BagList = BagList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			BagList_count = is.popInteger();
			BagList = is.popList(SBagInfo.class, BagList_count);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(BagList_count);
			os.pushList(BagList, BagList_count);
		}

		// 背包信息列表的最大数量
		public int BagList_count;
		// 背包信息列表
		public List<SBagInfo> BagList;
	}

	// 发放全区全服邮件请求
	public static class DoSendMailAllReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_MAIL_ALL_REQ;

		public DoSendMailAllReq() { }

		public DoSendMailAllReq(int AreaId, byte PlatId, int Partition, int MinLevel, 
		                        int VipLevel, String MailTitle, String MailContent, long BeginTime, 
		                        long EndTime, byte Type, int ItemType1, long ItemId1, 
		                        int ItemNum1, int ItemType2, long ItemId2, int ItemNum2, 
		                        int ItemType3, long ItemId3, int ItemNum3, int ItemType4, 
		                        long ItemId4, int ItemNum4, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.MinLevel = MinLevel;
			this.VipLevel = VipLevel;
			this.MailTitle = MailTitle;
			this.MailContent = MailContent;
			this.BeginTime = BeginTime;
			this.EndTime = EndTime;
			this.Type = Type;
			this.ItemType1 = ItemType1;
			this.ItemId1 = ItemId1;
			this.ItemNum1 = ItemNum1;
			this.ItemType2 = ItemType2;
			this.ItemId2 = ItemId2;
			this.ItemNum2 = ItemNum2;
			this.ItemType3 = ItemType3;
			this.ItemId3 = ItemId3;
			this.ItemNum3 = ItemNum3;
			this.ItemType4 = ItemType4;
			this.ItemId4 = ItemId4;
			this.ItemNum4 = ItemNum4;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			MinLevel = is.popInteger();
			VipLevel = is.popInteger();
			MailTitle = is.popString(MAX_MAILTITLE_LEN);
			MailContent = is.popString(MAX_MAILCONTENT_LEN);
			BeginTime = is.popLong();
			EndTime = is.popLong();
			Type = is.popByte();
			ItemType1 = is.popInteger();
			ItemId1 = is.popLong();
			ItemNum1 = is.popInteger();
			ItemType2 = is.popInteger();
			ItemId2 = is.popLong();
			ItemNum2 = is.popInteger();
			ItemType3 = is.popInteger();
			ItemId3 = is.popLong();
			ItemNum3 = is.popInteger();
			ItemType4 = is.popInteger();
			ItemId4 = is.popLong();
			ItemNum4 = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushInteger(MinLevel);
			os.pushInteger(VipLevel);
			os.pushString(MailTitle, MAX_MAILTITLE_LEN);
			os.pushString(MailContent, MAX_MAILCONTENT_LEN);
			os.pushLong(BeginTime);
			os.pushLong(EndTime);
			os.pushByte(Type);
			os.pushInteger(ItemType1);
			os.pushLong(ItemId1);
			os.pushInteger(ItemNum1);
			os.pushInteger(ItemType2);
			os.pushLong(ItemId2);
			os.pushInteger(ItemNum2);
			os.pushInteger(ItemType3);
			os.pushLong(ItemId3);
			os.pushInteger(ItemNum3);
			os.pushInteger(ItemType4);
			os.pushLong(ItemId4);
			os.pushInteger(ItemNum4);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID
		public int Partition;
		// 玩家等级（只发送给大于或等于该等级的玩家）
		public int MinLevel;
		// VIP等级
		public int VipLevel;
		// 邮件标题
		public String MailTitle;
		// 邮件内容
		public String MailContent;
		// 邮件生效时间
		public long BeginTime;
		// 邮件失效时间
		public long EndTime;
		// 邮件类型：1 带附件  2 文本
		public byte Type;
		// 附件道具1类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType1;
		// 附件道具1ID
		public long ItemId1;
		// 附件道具1数量
		public int ItemNum1;
		// 附件道具2类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType2;
		// 附件道具2ID
		public long ItemId2;
		// 附件道具2数量
		public int ItemNum2;
		// 附件道具3类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType3;
		// 附件道具3ID
		public long ItemId3;
		// 附件道具3数量
		public int ItemNum3;
		// 附件道具4类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType4;
		// 附件道具4ID
		public long ItemId4;
		// 附件道具4数量
		public int ItemNum4;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 发放全区全服邮件应答
	public static class DoSendMailAllRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_MAIL_ALL_RSP;

		public DoSendMailAllRsp() { }

		public DoSendMailAllRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 发送滚动公告请求
	public static class DoSendRollnoticeReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_ROLLNOTICE_REQ;

		public DoSendRollnoticeReq() { }

		public DoSendRollnoticeReq(int AreaId, byte PlatId, int Partition, int Freq, 
		                           int Times, String NoticeContent, long BeginTime, long EndTime, 
		                           int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.Freq = Freq;
			this.Times = Times;
			this.NoticeContent = NoticeContent;
			this.BeginTime = BeginTime;
			this.EndTime = EndTime;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			Freq = is.popInteger();
			Times = is.popInteger();
			NoticeContent = is.popString(MAX_NOTICECONTENT_LEN);
			BeginTime = is.popLong();
			EndTime = is.popLong();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushInteger(Freq);
			os.pushInteger(Times);
			os.pushString(NoticeContent, MAX_NOTICECONTENT_LEN);
			os.pushLong(BeginTime);
			os.pushLong(EndTime);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID
		public int Partition;
		// 滚动频率：**秒
		public int Freq;
		// 滚动次数
		public int Times;
		// 公告内容
		public String NoticeContent;
		// 开始生效时间
		public long BeginTime;
		// 结束时间：不配置，则表示不限制结束时间
		public long EndTime;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 发送滚动公告应答
	public static class DoSendRollnoticeRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_ROLLNOTICE_RSP;

		public DoSendRollnoticeRsp() { }

		public DoSendRollnoticeRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 查询公告请求
	public static class QueryNoticeReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_NOTICE_REQ;

		public QueryNoticeReq() { }

		public QueryNoticeReq(int AreaId, byte PlatId, int Partition, long BeginTime, 
		                      long EndTime)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.BeginTime = BeginTime;
			this.EndTime = EndTime;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			BeginTime = is.popLong();
			EndTime = is.popLong();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushLong(BeginTime);
			os.pushLong(EndTime);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID
		public int Partition;
		// 开始时间
		public long BeginTime;
		// 结束时间
		public long EndTime;
	}

	// 查询公告应答
	public static class QueryNoticeRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_NOTICE_RSP;

		public QueryNoticeRsp() { }

		public QueryNoticeRsp(int NoticeList_count, List<SNoticeInfo> NoticeList)
		{
			this.NoticeList_count = NoticeList_count;
			this.NoticeList = NoticeList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			NoticeList_count = is.popInteger();
			NoticeList = is.popList(SNoticeInfo.class, NoticeList_count);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(NoticeList_count);
			os.pushList(NoticeList, NoticeList_count);
		}

		// 公告信息列表的最大数量
		public int NoticeList_count;
		// 公告信息列表
		public List<SNoticeInfo> NoticeList;
	}

	// 删除公告请求
	public static class DoDelNoticeReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_DEL_NOTICE_REQ;

		public DoDelNoticeReq() { }

		public DoDelNoticeReq(int AreaId, byte PlatId, int Partition, long NoticeId, 
		                      int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.NoticeId = NoticeId;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			NoticeId = is.popLong();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushLong(NoticeId);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID
		public int Partition;
		// 公告ID
		public long NoticeId;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 删除公告应答
	public static class DoDelNoticeRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_DEL_NOTICE_RSP;

		public DoDelNoticeRsp() { }

		public DoDelNoticeRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 发送邮件请求
	public static class DoSendMailReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_MAIL_REQ;

		public DoSendMailReq() { }

		public DoSendMailReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                     String MailTitle, String MailContent, long EndTime, byte Type, 
		                     int ItemType1, long ItemId1, int ItemNum1, int ItemType2, 
		                     long ItemId2, int ItemNum2, int ItemType3, long ItemId3, 
		                     int ItemNum3, int ItemType4, long ItemId4, int ItemNum4, 
		                     int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.MailTitle = MailTitle;
			this.MailContent = MailContent;
			this.EndTime = EndTime;
			this.Type = Type;
			this.ItemType1 = ItemType1;
			this.ItemId1 = ItemId1;
			this.ItemNum1 = ItemNum1;
			this.ItemType2 = ItemType2;
			this.ItemId2 = ItemId2;
			this.ItemNum2 = ItemNum2;
			this.ItemType3 = ItemType3;
			this.ItemId3 = ItemId3;
			this.ItemNum3 = ItemNum3;
			this.ItemType4 = ItemType4;
			this.ItemId4 = ItemId4;
			this.ItemNum4 = ItemNum4;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			MailTitle = is.popString(MAX_MAILTITLE_LEN);
			MailContent = is.popString(MAX_MAILCONTENT_LEN);
			EndTime = is.popLong();
			Type = is.popByte();
			ItemType1 = is.popInteger();
			ItemId1 = is.popLong();
			ItemNum1 = is.popInteger();
			ItemType2 = is.popInteger();
			ItemId2 = is.popLong();
			ItemNum2 = is.popInteger();
			ItemType3 = is.popInteger();
			ItemId3 = is.popLong();
			ItemNum3 = is.popInteger();
			ItemType4 = is.popInteger();
			ItemId4 = is.popLong();
			ItemNum4 = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushString(MailTitle, MAX_MAILTITLE_LEN);
			os.pushString(MailContent, MAX_MAILCONTENT_LEN);
			os.pushLong(EndTime);
			os.pushByte(Type);
			os.pushInteger(ItemType1);
			os.pushLong(ItemId1);
			os.pushInteger(ItemNum1);
			os.pushInteger(ItemType2);
			os.pushLong(ItemId2);
			os.pushInteger(ItemNum2);
			os.pushInteger(ItemType3);
			os.pushLong(ItemId3);
			os.pushInteger(ItemNum3);
			os.pushInteger(ItemType4);
			os.pushLong(ItemId4);
			os.pushInteger(ItemNum4);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 大区（1微信，2手Q）
		public int AreaId;
		// 平台（0ios，1安卓）
		public byte PlatId;
		// 小区ID
		public int Partition;
		// openid
		public String OpenId;
		// 邮件标题
		public String MailTitle;
		// 邮件内容
		public String MailContent;
		// 邮件失效时间
		public long EndTime;
		// 邮件类型：1 带附件  2 文本
		public byte Type;
		// 附件道具1类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType1;
		// 附件道具1ID
		public long ItemId1;
		// 附件道具1数量
		public int ItemNum1;
		// 附件道具2类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType2;
		// 附件道具2ID
		public long ItemId2;
		// 附件道具2数量
		public int ItemNum2;
		// 附件道具3类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType3;
		// 附件道具3ID
		public long ItemId3;
		// 附件道具3数量
		public int ItemNum3;
		// 附件道具4类型（0道具，1装备，-1铜钱，-2钻石）
		public int ItemType4;
		// 附件道具4ID
		public long ItemId4;
		// 附件道具4数量
		public int ItemNum4;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 发送邮件应答
	public static class DoSendMailRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SEND_MAIL_RSP;

		public DoSendMailRsp() { }

		public DoSendMailRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 清零游戏数据请求
	public static class DoClearDataReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_CLEAR_DATA_REQ;

		public DoClearDataReq() { }

		public DoClearDataReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                      byte IsCopper, byte IsPhysical, byte IsDiamond, int Source, 
		                      String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.IsCopper = IsCopper;
			this.IsPhysical = IsPhysical;
			this.IsDiamond = IsDiamond;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			IsCopper = is.popByte();
			IsPhysical = is.popByte();
			IsDiamond = is.popByte();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(IsCopper);
			os.pushByte(IsPhysical);
			os.pushByte(IsDiamond);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 铜钱清零：否（0），是（1）
		public byte IsCopper;
		// 体力清零：否（0），是（1）
		public byte IsPhysical;
		// 钻石清零：否（0），是（1）
		public byte IsDiamond;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 清零游戏数据应答
	public static class DoClearDataRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_CLEAR_DATA_RSP;

		public DoClearDataRsp() { }

		public DoClearDataRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 设置武将等级请求
	public static class DoSetGeneralLvReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SET_GENERAL_LV_REQ;

		public DoSetGeneralLvReq() { }

		public DoSetGeneralLvReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                         long GeneralId, int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.GeneralId = GeneralId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			GeneralId = is.popLong();
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushLong(GeneralId);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 武将ID
		public long GeneralId;
		// 武将等级：填1则表示1级，2则表示2级
		public int Value;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 设置武将等级应答
	public static class DoSetGeneralLvRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SET_GENERAL_LV_RSP;

		public DoSetGeneralLvRsp() { }

		public DoSetGeneralLvRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 设置宠物等级请求
	public static class DoSetPetLvReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SET_PET_LV_REQ;

		public DoSetPetLvReq() { }

		public DoSetPetLvReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                     long PetId, int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.PetId = PetId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			PetId = is.popLong();
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushLong(PetId);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 宠物ID
		public long PetId;
		// 宠物等级：填1则表示1级，2则表示2级
		public int Value;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 设置宠物等级应答
	public static class DoSetPetLvRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_SET_PET_LV_RSP;

		public DoSetPetLvRsp() { }

		public DoSetPetLvRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 查询背包当前装备请求
	public static class QueryBagitemInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_BAGITEM_INFO_REQ;

		public QueryBagitemInfoReq() { }

		public QueryBagitemInfoReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                           byte PageNo)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.PageNo = PageNo;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			PageNo = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(PageNo);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 当前页码
		public byte PageNo;
	}

	// 查询背包当前装备应答
	public static class QueryBagitemInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_BAGITEM_INFO_RSP;

		public QueryBagitemInfoRsp() { }

		public QueryBagitemInfoRsp(int BackPackList_count, List<BackPackListInfo> BackPackList)
		{
			this.BackPackList_count = BackPackList_count;
			this.BackPackList = BackPackList;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			BackPackList_count = is.popInteger();
			BackPackList = is.popList(BackPackListInfo.class, BackPackList_count);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(BackPackList_count);
			os.pushList(BackPackList, BackPackList_count);
		}

		// 背包当前装备列表的最大数量
		public int BackPackList_count;
		// 背包当前装备列表
		public List<BackPackListInfo> BackPackList;
	}

	// 竞技场查询请求
	public static class QueryArenaReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_ARENA_REQ;

		public QueryArenaReq() { }

		public QueryArenaReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 竞技场查询应答
	public static class QueryArenaRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_ARENA_RSP;

		public QueryArenaRsp() { }

		public QueryArenaRsp(long RoleId, int CurRank)
		{
			this.RoleId = RoleId;
			this.CurRank = CurRank;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			RoleId = is.popLong();
			CurRank = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushLong(RoleId);
			os.pushInteger(CurRank);
		}

		// 角色ID
		public long RoleId;
		// 当前排名
		public int CurRank;
	}

	// 查询openid基本信息（AQ）请求
	public static class AqQueryUserbaseinfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_QUERY_USERBASEINFO_REQ;

		public AqQueryUserbaseinfoReq() { }

		public AqQueryUserbaseinfoReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询openid基本信息（AQ）应答
	public static class AqQueryUserbaseinfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_QUERY_USERBASEINFO_RSP;

		public AqQueryUserbaseinfoRsp() { }

		public AqQueryUserbaseinfoRsp(String RoleName, int Level, int Money, int Diamond, 
		                              int Status, int Lays)
		{
			this.RoleName = RoleName;
			this.Level = Level;
			this.Money = Money;
			this.Diamond = Diamond;
			this.Status = Status;
			this.Lays = Lays;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			RoleName = is.popString(MAX_ROLENAME_LEN);
			Level = is.popInteger();
			Money = is.popInteger();
			Diamond = is.popInteger();
			Status = is.popInteger();
			Lays = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(RoleName, MAX_ROLENAME_LEN);
			os.pushInteger(Level);
			os.pushInteger(Money);
			os.pushInteger(Diamond);
			os.pushInteger(Status);
			os.pushInteger(Lays);
		}

		// 角色名称
		public String RoleName;
		// 等级
		public int Level;
		// 游戏币数量
		public int Money;
		// 钻石数量
		public int Diamond;
		// 副本进度
		public int Status;
		// 过关斩将层数
		public int Lays;
	}

	// 发送邮件（AQ）请求
	public static class AqDoSendMailReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_SEND_MAIL_REQ;

		public AqDoSendMailReq() { }

		public AqDoSendMailReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                       String MailContent, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.MailContent = MailContent;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			MailContent = is.popString(MAX_MAILCONTENT_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushString(MailContent, MAX_MAILCONTENT_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 邮件内容
		public String MailContent;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 发送邮件（AQ）应答
	public static class AqDoSendMailRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_SEND_MAIL_RSP;

		public AqDoSendMailRsp() { }

		public AqDoSendMailRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改钻石数量（AQ）请求
	public static class AqDoUpdateDiamondReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_UPDATE_DIAMOND_REQ;

		public AqDoUpdateDiamondReq() { }

		public AqDoUpdateDiamondReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                            byte Type, int Value, byte IsLogin, int Source, 
		                            String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Type = Type;
			this.Value = Value;
			this.IsLogin = IsLogin;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Type = is.popByte();
			Value = is.popInteger();
			IsLogin = is.popByte();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(Type);
			os.pushInteger(Value);
			os.pushByte(IsLogin);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 钻石类型（0 绑定钻石，1 充值钻石）
		public byte Type;
		// 变动值（正数加钻石，负数减钻石）
		public int Value;
		// 是否需要重新登录（0 否，1 是）
		public byte IsLogin;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 修改钻石数量（AQ）应答
	public static class AqDoUpdateDiamondRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_UPDATE_DIAMOND_RSP;

		public AqDoUpdateDiamondRsp() { }

		public AqDoUpdateDiamondRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改游戏币数量（AQ）请求
	public static class AqDoUpdateMoneyReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_UPDATE_MONEY_REQ;

		public AqDoUpdateMoneyReq() { }

		public AqDoUpdateMoneyReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                          int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 变动值（正数增加，负数减少）
		public int Value;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需填写
		public String Serial;
	}

	// 修改游戏币数量（AQ）应答
	public static class AqDoUpdateMoneyRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_UPDATE_MONEY_RSP;

		public AqDoUpdateMoneyRsp() { }

		public AqDoUpdateMoneyRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 禁止玩法（AQ）请求
	public static class AqDoBanPlayReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_BAN_PLAY_REQ;

		public AqDoBanPlayReq() { }

		public AqDoBanPlayReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                      byte Type, int Time, String Reason, int Source, 
		                      String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Type = Type;
			this.Time = Time;
			this.Reason = Reason;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Type = is.popByte();
			Time = is.popInteger();
			Reason = is.popString(MAX_REASON_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(Type);
			os.pushInteger(Time);
			os.pushString(Reason, MAX_REASON_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 禁止类型（1 战役，2 过关斩将，3 赤壁之战，4，竞技场，99 全选）
		public byte Type;
		// 禁止玩法时长（秒）
		public int Time;
		// 禁止玩法原因
		public String Reason;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 禁止玩法（AQ）应答
	public static class AqDoBanPlayRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_BAN_PLAY_RSP;

		public AqDoBanPlayRsp() { }

		public AqDoBanPlayRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 查询用户签名和公会公告（AQ）请求
	public static class AqQueryUsrsignGuildnoticeReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_REQ;

		public AqQueryUsrsignGuildnoticeReq() { }

		public AqQueryUsrsignGuildnoticeReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询用户签名和公会公告（AQ）应答
	public static class AqQueryUsrsignGuildnoticeRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_RSP;

		public AqQueryUsrsignGuildnoticeRsp() { }

		public AqQueryUsrsignGuildnoticeRsp(String RoleName, int Level, String UsrSign, long GuildId, 
		                                    int GuildJob, String GuildNotice)
		{
			this.RoleName = RoleName;
			this.Level = Level;
			this.UsrSign = UsrSign;
			this.GuildId = GuildId;
			this.GuildJob = GuildJob;
			this.GuildNotice = GuildNotice;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			RoleName = is.popString(MAX_ROLENAME_LEN);
			Level = is.popInteger();
			UsrSign = is.popString(MAX_USRSIGN_LEN);
			GuildId = is.popLong();
			GuildJob = is.popInteger();
			GuildNotice = is.popString(MAX_GUILDNOTICE_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(RoleName, MAX_ROLENAME_LEN);
			os.pushInteger(Level);
			os.pushString(UsrSign, MAX_USRSIGN_LEN);
			os.pushLong(GuildId);
			os.pushInteger(GuildJob);
			os.pushString(GuildNotice, MAX_GUILDNOTICE_LEN);
		}

		// 角色名称
		public String RoleName;
		// 等级
		public int Level;
		// 用户签名
		public String UsrSign;
		// 公会ID
		public long GuildId;
		// 公会身份
		public int GuildJob;
		// 公会公告
		public String GuildNotice;
	}

	// 设置用户签名和公会公告（AQ）请求
	public static class AqDoSetUsrsignGuildnoticeReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_REQ;

		public AqDoSetUsrsignGuildnoticeReq() { }

		public AqDoSetUsrsignGuildnoticeReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                                    long GuildId, byte Type, String GuildNotice, int Source, 
		                                    String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.GuildId = GuildId;
			this.Type = Type;
			this.GuildNotice = GuildNotice;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			GuildId = is.popLong();
			Type = is.popByte();
			GuildNotice = is.popString(MAX_GUILDNOTICE_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushLong(GuildId);
			os.pushByte(Type);
			os.pushString(GuildNotice, MAX_GUILDNOTICE_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 分区
		public int Partition;
		// openid
		public String OpenId;
		// 公会ID
		public long GuildId;
		// 信息类型（1 用户签名，2 公会公告）
		public byte Type;
		// 设定内容
		public String GuildNotice;
		// 渠道号，由前端生成，不需填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 设置用户签名和公会公告（AQ）应答
	public static class AqDoSetUsrsignGuildnoticeRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP;

		public AqDoSetUsrsignGuildnoticeRsp() { }

		public AqDoSetUsrsignGuildnoticeRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：0 成功，其它失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 禁言（AQ）请求
	public static class AqDoMaskchatUsrReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_MASKCHAT_USR_REQ;

		public AqDoMaskchatUsrReq() { }

		public AqDoMaskchatUsrReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                          int Time, String Reason, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Time = Time;
			this.Reason = Reason;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Time = is.popInteger();
			Reason = is.popString(MAX_REASON_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Time);
			os.pushString(Reason, MAX_REASON_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1），全部（2）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 禁言时长（秒）
		public int Time;
		// 禁言原因
		public String Reason;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 禁言（AQ）应答
	public static class AqDoMaskchatUsrRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_MASKCHAT_USR_RSP;

		public AqDoMaskchatUsrRsp() { }

		public AqDoMaskchatUsrRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 封号（AQ）请求
	public static class AqDoBanUsrReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_BAN_USR_REQ;

		public AqDoBanUsrReq() { }

		public AqDoBanUsrReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                     int Time, String Reason, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Time = Time;
			this.Reason = Reason;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Time = is.popInteger();
			Reason = is.popString(MAX_REASON_LEN);
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Time);
			os.pushString(Reason, MAX_REASON_LEN);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 封停时长（秒）
		public int Time;
		// 封号原因
		public String Reason;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 封号（AQ）应答
	public static class AqDoBanUsrRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_BAN_USR_RSP;

		public AqDoBanUsrRsp() { }

		public AqDoBanUsrRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 解除处罚（AQ）请求
	public static class AqDoRelievePunishReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_RELIEVE_PUNISH_REQ;

		public AqDoRelievePunishReq() { }

		public AqDoRelievePunishReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                            byte RelieveZeroProfit, byte RelieveAllPlay, byte RelieveBanJoinRank, byte RelieveBan, 
		                            byte RelieveMaskchat, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.RelieveZeroProfit = RelieveZeroProfit;
			this.RelieveAllPlay = RelieveAllPlay;
			this.RelieveBanJoinRank = RelieveBanJoinRank;
			this.RelieveBan = RelieveBan;
			this.RelieveMaskchat = RelieveMaskchat;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			RelieveZeroProfit = is.popByte();
			RelieveAllPlay = is.popByte();
			RelieveBanJoinRank = is.popByte();
			RelieveBan = is.popByte();
			RelieveMaskchat = is.popByte();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushByte(RelieveZeroProfit);
			os.pushByte(RelieveAllPlay);
			os.pushByte(RelieveBanJoinRank);
			os.pushByte(RelieveBan);
			os.pushByte(RelieveMaskchat);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 解除零收益状态（0 否，1 是）
		public byte RelieveZeroProfit;
		// 解除所有玩法限制（0 否，1 是）
		public byte RelieveAllPlay;
		// 解除禁止参与竞技场排行榜限制（0 否，1 是）
		public byte RelieveBanJoinRank;
		// 解除封号（0 否，1 是）
		public byte RelieveBan;
		// 解除禁言（0 否，1 是）
		public byte RelieveMaskchat;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 解除处罚（AQ）应答
	public static class AqDoRelievePunishRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_AQ_DO_RELIEVE_PUNISH_RSP;

		public AqDoRelievePunishRsp() { }

		public AqDoRelievePunishRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 操作结果
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改vip经验请求
	public static class DoUpdateVipExpReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_VIP_EXP_REQ;

		public DoUpdateVipExpReq() { }

		public DoUpdateVipExpReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                         int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2），游客（5）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 修改数量：正+/-减
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改vip经验应答
	public static class DoUpdateVipExpRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_VIP_EXP_RSP;

		public DoUpdateVipExpRsp() { }

		public DoUpdateVipExpRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 修改君主等级请求
	public static class DoUpdateKingLevelReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_KING_LEVEL_REQ;

		public DoUpdateKingLevelReq() { }

		public DoUpdateKingLevelReq(int AreaId, byte PlatId, int Partition, String OpenId, 
		                            int Value, int Source, String Serial)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
			this.Value = Value;
			this.Source = Source;
			this.Serial = Serial;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
			Value = is.popInteger();
			Source = is.popInteger();
			Serial = is.popString(MAX_SERIAL_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
			os.pushInteger(Value);
			os.pushInteger(Source);
			os.pushString(Serial, MAX_SERIAL_LEN);
		}

		// 服务器：微信（1），手Q（2），游客（5）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
		// 修改数量：只允许正值
		public int Value;
		// 渠道号，由前端生成，不需要填写
		public int Source;
		// 流水号，由前端生成，不需要填写
		public String Serial;
	}

	// 修改君主等级应答
	public static class DoUpdateKingLevelRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_DO_UPDATE_KING_LEVEL_RSP;

		public DoUpdateKingLevelRsp() { }

		public DoUpdateKingLevelRsp(int Result, String RetMsg)
		{
			this.Result = Result;
			this.RetMsg = RetMsg;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Result = is.popInteger();
			RetMsg = is.popString(MAX_RETMSG_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Result);
			os.pushString(RetMsg, MAX_RETMSG_LEN);
		}

		// 结果：（0）成功，（其他）失败
		public int Result;
		// 返回消息
		public String RetMsg;
	}

	// 查询玩家战斗力请求
	public static class QueryFightInfoReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_FIGHT_INFO_REQ;

		public QueryFightInfoReq() { }

		public QueryFightInfoReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 服务器：微信（1），手Q（2），游客（5）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询玩家战斗力应答
	public static class QueryFightInfoRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_FIGHT_INFO_RSP;

		public QueryFightInfoRsp() { }

		public QueryFightInfoRsp(int Fight)
		{
			this.Fight = Fight;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			Fight = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(Fight);
		}

		// 玩家战斗力
		public int Fight;
	}

	// 查询玩家VIP等级请求
	public static class QueryVipLevelReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_VIP_LEVEL_REQ;

		public QueryVipLevelReq() { }

		public QueryVipLevelReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 大区：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询玩家VIP等级应答
	public static class QueryVipLevelRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_VIP_LEVEL_RSP;

		public QueryVipLevelRsp() { }

		public QueryVipLevelRsp(int SvrID, int Time, int VipLevel, int Exp, 
		                        int LackExp)
		{
			this.SvrID = SvrID;
			this.Time = Time;
			this.VipLevel = VipLevel;
			this.Exp = Exp;
			this.LackExp = LackExp;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			SvrID = is.popInteger();
			Time = is.popInteger();
			VipLevel = is.popInteger();
			Exp = is.popInteger();
			LackExp = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(SvrID);
			os.pushInteger(Time);
			os.pushInteger(VipLevel);
			os.pushInteger(Exp);
			os.pushInteger(LackExp);
		}

		// 服务器ID
		public int SvrID;
		// 时间
		public int Time;
		// VIP等级
		public int VipLevel;
		// 经验
		public int Exp;
		// 升级所需经验
		public int LackExp;
	}

	// 查询玩家签到信息请求
	public static class QueryUserSigninReq implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_USER_SIGNIN_REQ;

		public QueryUserSigninReq() { }

		public QueryUserSigninReq(int AreaId, byte PlatId, int Partition, String OpenId)
		{
			this.AreaId = AreaId;
			this.PlatId = PlatId;
			this.Partition = Partition;
			this.OpenId = OpenId;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			AreaId = is.popInteger();
			PlatId = is.popByte();
			Partition = is.popInteger();
			OpenId = is.popString(MAX_OPENID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(AreaId);
			os.pushByte(PlatId);
			os.pushInteger(Partition);
			os.pushString(OpenId, MAX_OPENID_LEN);
		}

		// 大区：微信（1），手Q（2）
		public int AreaId;
		// 平台：IOS（0），安卓（1）
		public byte PlatId;
		// 小区
		public int Partition;
		// openid
		public String OpenId;
	}

	// 查询玩家签到信息应答
	public static class QueryUserSigninRsp implements Stream.IStreamable
	{

		public static final int idipID = IDIP_QUERY_USER_SIGNIN_RSP;

		public QueryUserSigninRsp() { }

		public QueryUserSigninRsp(int SvrID, int Time, int SiginDay, String AwardID)
		{
			this.SvrID = SvrID;
			this.Time = Time;
			this.SiginDay = SiginDay;
			this.AwardID = AwardID;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			SvrID = is.popInteger();
			Time = is.popInteger();
			SiginDay = is.popInteger();
			AwardID = is.popString(MAX_AWARDID_LEN);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(SvrID);
			os.pushInteger(Time);
			os.pushInteger(SiginDay);
			os.pushString(AwardID, MAX_AWARDID_LEN);
		}

		// 服务器ID
		public int SvrID;
		// 时间
		public int Time;
		// 签到第几天
		public int SiginDay;
		// 奖励ID
		public String AwardID;
	}

	public static Stream.IStreamable decodePacket(int cmdID, byte[] bodyData)
	{
		Stream.BytesInputStream bais = new Stream.BytesInputStream(bodyData, 0, bodyData.length);
		Stream.AIStream is = new Stream.IStreamBE(bais);
		try
		{
			switch( cmdID )
			{
			case IDIP_DO_BAN_USR_REQ:
				{
					DoBanUsrReq obj = new DoBanUsrReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_BAN_USR_RSP:
				{
					DoBanUsrRsp obj = new DoBanUsrRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UNBAN_USR_REQ:
				{
					DoUnbanUsrReq obj = new DoUnbanUsrReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UNBAN_USR_RSP:
				{
					DoUnbanUsrRsp obj = new DoUnbanUsrRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_DEL_ITEM_REQ:
				{
					DoDelItemReq obj = new DoDelItemReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_DEL_ITEM_RSP:
				{
					DoDelItemRsp obj = new DoDelItemRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_EXP_REQ:
				{
					DoUpdateExpReq obj = new DoUpdateExpReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_EXP_RSP:
				{
					DoUpdateExpRsp obj = new DoUpdateExpRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_PHYSICAL_REQ:
				{
					DoUpdatePhysicalReq obj = new DoUpdatePhysicalReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_PHYSICAL_RSP:
				{
					DoUpdatePhysicalRsp obj = new DoUpdatePhysicalRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_DIAMOND_REQ:
				{
					DoUpdateDiamondReq obj = new DoUpdateDiamondReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_DIAMOND_RSP:
				{
					DoUpdateDiamondRsp obj = new DoUpdateDiamondRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_MONEY_REQ:
				{
					DoUpdateMoneyReq obj = new DoUpdateMoneyReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_MONEY_RSP:
				{
					DoUpdateMoneyRsp obj = new DoUpdateMoneyRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_ITEM_REQ:
				{
					DoSendItemReq obj = new DoSendItemReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_ITEM_RSP:
				{
					DoSendItemRsp obj = new DoSendItemRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_USR_INFO_REQ:
				{
					QueryUsrInfoReq obj = new QueryUsrInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_USR_INFO_RSP:
				{
					QueryUsrInfoRsp obj = new QueryUsrInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_GENERAL_INFO_REQ:
				{
					QueryGeneralInfoReq obj = new QueryGeneralInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_GENERAL_INFO_RSP:
				{
					QueryGeneralInfoRsp obj = new QueryGeneralInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_GENERAL_EQUIP_INFO_REQ:
				{
					QueryGeneralEquipInfoReq obj = new QueryGeneralEquipInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_GENERAL_EQUIP_INFO_RSP:
				{
					QueryGeneralEquipInfoRsp obj = new QueryGeneralEquipInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_BAG_INFO_REQ:
				{
					QueryBagInfoReq obj = new QueryBagInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_BAG_INFO_RSP:
				{
					QueryBagInfoRsp obj = new QueryBagInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_MAIL_ALL_REQ:
				{
					DoSendMailAllReq obj = new DoSendMailAllReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_MAIL_ALL_RSP:
				{
					DoSendMailAllRsp obj = new DoSendMailAllRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_ROLLNOTICE_REQ:
				{
					DoSendRollnoticeReq obj = new DoSendRollnoticeReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_ROLLNOTICE_RSP:
				{
					DoSendRollnoticeRsp obj = new DoSendRollnoticeRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_NOTICE_REQ:
				{
					QueryNoticeReq obj = new QueryNoticeReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_NOTICE_RSP:
				{
					QueryNoticeRsp obj = new QueryNoticeRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_DEL_NOTICE_REQ:
				{
					DoDelNoticeReq obj = new DoDelNoticeReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_DEL_NOTICE_RSP:
				{
					DoDelNoticeRsp obj = new DoDelNoticeRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_MAIL_REQ:
				{
					DoSendMailReq obj = new DoSendMailReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SEND_MAIL_RSP:
				{
					DoSendMailRsp obj = new DoSendMailRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_CLEAR_DATA_REQ:
				{
					DoClearDataReq obj = new DoClearDataReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_CLEAR_DATA_RSP:
				{
					DoClearDataRsp obj = new DoClearDataRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SET_GENERAL_LV_REQ:
				{
					DoSetGeneralLvReq obj = new DoSetGeneralLvReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SET_GENERAL_LV_RSP:
				{
					DoSetGeneralLvRsp obj = new DoSetGeneralLvRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SET_PET_LV_REQ:
				{
					DoSetPetLvReq obj = new DoSetPetLvReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_SET_PET_LV_RSP:
				{
					DoSetPetLvRsp obj = new DoSetPetLvRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_BAGITEM_INFO_REQ:
				{
					QueryBagitemInfoReq obj = new QueryBagitemInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_BAGITEM_INFO_RSP:
				{
					QueryBagitemInfoRsp obj = new QueryBagitemInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_ARENA_REQ:
				{
					QueryArenaReq obj = new QueryArenaReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_ARENA_RSP:
				{
					QueryArenaRsp obj = new QueryArenaRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_QUERY_USERBASEINFO_REQ:
				{
					AqQueryUserbaseinfoReq obj = new AqQueryUserbaseinfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_QUERY_USERBASEINFO_RSP:
				{
					AqQueryUserbaseinfoRsp obj = new AqQueryUserbaseinfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_SEND_MAIL_REQ:
				{
					AqDoSendMailReq obj = new AqDoSendMailReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_SEND_MAIL_RSP:
				{
					AqDoSendMailRsp obj = new AqDoSendMailRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_UPDATE_DIAMOND_REQ:
				{
					AqDoUpdateDiamondReq obj = new AqDoUpdateDiamondReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_UPDATE_DIAMOND_RSP:
				{
					AqDoUpdateDiamondRsp obj = new AqDoUpdateDiamondRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_UPDATE_MONEY_REQ:
				{
					AqDoUpdateMoneyReq obj = new AqDoUpdateMoneyReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_UPDATE_MONEY_RSP:
				{
					AqDoUpdateMoneyRsp obj = new AqDoUpdateMoneyRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_BAN_PLAY_REQ:
				{
					AqDoBanPlayReq obj = new AqDoBanPlayReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_BAN_PLAY_RSP:
				{
					AqDoBanPlayRsp obj = new AqDoBanPlayRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_REQ:
				{
					AqQueryUsrsignGuildnoticeReq obj = new AqQueryUsrsignGuildnoticeReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_QUERY_USRSIGN_GUILDNOTICE_RSP:
				{
					AqQueryUsrsignGuildnoticeRsp obj = new AqQueryUsrsignGuildnoticeRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_REQ:
				{
					AqDoSetUsrsignGuildnoticeReq obj = new AqDoSetUsrsignGuildnoticeReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_SET_USRSIGN_GUILDNOTICE_RSP:
				{
					AqDoSetUsrsignGuildnoticeRsp obj = new AqDoSetUsrsignGuildnoticeRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_MASKCHAT_USR_REQ:
				{
					AqDoMaskchatUsrReq obj = new AqDoMaskchatUsrReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_MASKCHAT_USR_RSP:
				{
					AqDoMaskchatUsrRsp obj = new AqDoMaskchatUsrRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_BAN_USR_REQ:
				{
					AqDoBanUsrReq obj = new AqDoBanUsrReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_BAN_USR_RSP:
				{
					AqDoBanUsrRsp obj = new AqDoBanUsrRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_RELIEVE_PUNISH_REQ:
				{
					AqDoRelievePunishReq obj = new AqDoRelievePunishReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_AQ_DO_RELIEVE_PUNISH_RSP:
				{
					AqDoRelievePunishRsp obj = new AqDoRelievePunishRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_VIP_EXP_REQ:
				{
					DoUpdateVipExpReq obj = new DoUpdateVipExpReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_VIP_EXP_RSP:
				{
					DoUpdateVipExpRsp obj = new DoUpdateVipExpRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_KING_LEVEL_REQ:
				{
					DoUpdateKingLevelReq obj = new DoUpdateKingLevelReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_DO_UPDATE_KING_LEVEL_RSP:
				{
					DoUpdateKingLevelRsp obj = new DoUpdateKingLevelRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_FIGHT_INFO_REQ:
				{
					QueryFightInfoReq obj = new QueryFightInfoReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_FIGHT_INFO_RSP:
				{
					QueryFightInfoRsp obj = new QueryFightInfoRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_VIP_LEVEL_REQ:
				{
					QueryVipLevelReq obj = new QueryVipLevelReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_VIP_LEVEL_RSP:
				{
					QueryVipLevelRsp obj = new QueryVipLevelRsp();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_USER_SIGNIN_REQ:
				{
					QueryUserSigninReq obj = new QueryUserSigninReq();
					obj.decode(is);
					return obj;
				}
			case IDIP_QUERY_USER_SIGNIN_RSP:
				{
					QueryUserSigninRsp obj = new QueryUserSigninRsp();
					obj.decode(is);
					return obj;
				}
			default:
				break;
			}
		}
		catch(Exception ex)
		{
		}
		return null;
	}
}

// modified by ket.kio.RPCGen at Wed Mar 02 11:43:47 CST 2016.

package i3k.rpc;

import java.util.List;

import ket.util.Stream;
import ket.kio.SimplePacket;

import i3k.SBean;
public abstract class Packet
{

	// server to client
	public static final int eS2CPKTServerChallenge = 1;
	public static final int eS2CPKTServerResponse = 2;
	public static final int eS2CPKTLuaChannel = 5;
	public static final int eS2CPKTLuaChannel2 = 7;
	public static final int eS2CPKTCmnRPCResponse = 100;
	public static final int eS2CPKTAntiData = 31;

	// client to server
	public static final int eC2SPKTClientResponse = 10001;
	public static final int eC2SPKTLuaChannel = 10004;
	public static final int eC2SPKTLuaChannel2 = 10005;
	public static final int eC2SPKTDataSync = 10030;
	public static final int eC2SPKTAntiData = 10031;

	// global to client
	public static final int eG2CPKTLuaChannel = 80001;
	public static final int eG2CPKTLuaChannel2 = 80002;

	// client to global
	public static final int eC2GPKTLuaChannel = 90001;
	public static final int eC2GPKTLuaChannel2 = 90002;

	// manager to server
	public static final int eM2SPKTShutdown = 20001;
	public static final int eM2SPKTDataQuery = 20002;
	public static final int eM2SPKTAnnounce = 20003;
	public static final int eM2SPKTDataModify = 20004;
	public static final int eM2SPKTSysMessage = 20005;
	public static final int eM2SPKTRoleData = 20006;
	public static final int eM2SPKTWorldGift = 20007;

	// server to manager
	public static final int eS2MPKTShutdown = 30001;
	public static final int eS2MPKTOnlineUser = 30002;
	public static final int eS2MPKTQueryRoleBrief = 30003;
	public static final int eS2MPKTAnnounce = 30004;
	public static final int eS2MPKTDataModify = 30005;
	public static final int eS2MPKTSysMessage = 30006;
	public static final int eS2MPKTDataQuery = 30007;
	public static final int eS2MPKTRoleData = 30008;
	public static final int eS2MPKTWorldGift = 30009;

	// exchange to server
	public static final int eE2SPKTStateAnnounce = 60001;
	public static final int eE2SPKTSearchCity = 60006;
	public static final int eE2SPKTSearchCityForward = 60007;
	public static final int eE2SPKTAttackCityStart = 60008;
	public static final int eE2SPKTAttackCityStartForward = 60009;
	public static final int eE2SPKTAttackCityEnd = 60010;
	public static final int eE2SPKTAttackCityEndForward = 60011;
	public static final int eE2SPKTQueryCityOwner = 60012;
	public static final int eE2SPKTQueryCityOwnerForward = 60013;
	public static final int eE2SPKTNotifyCityOwnerChangedForward = 60014;
	public static final int eE2SPKTChannelMessageReq = 60015;
	public static final int eE2SPKTChannelMessageRes = 60016;
	public static final int eE2SPKTDuelJoin = 60017;
	public static final int eE2SPKTDuelSelectGeneral = 60018;
	public static final int eE2SPKTDuelSelectGeneralForward = 60019;
	public static final int eE2SPKTDuelStartAttack = 60020;
	public static final int eE2SPKTDuelStartAttackForward = 60021;
	public static final int eE2SPKTDuelFinishAttack = 60022;
	public static final int eE2SPKTDuelRanks = 60023;
	public static final int eE2SPKTDuelRank = 60024;
	public static final int eE2SPKTDuelTopRanks = 60025;
	public static final int eE2SPKTDuelOppoGiveup = 60026;
	public static final int eE2SPKTDuelOppoGiveupForward = 60027;
	public static final int eE2SPKTDuelGlobalRanks = 60028;
	public static final int eE2SPKTExpatriateDBRole = 60029;
	public static final int eE2SPKTExpatriateDBRoleForward = 60030;
	public static final int eE2SPKTExpiratBossRanks = 60031;
	public static final int eE2SPKTExpiratBossRank = 60032;
	public static final int eE2SPKTExpiratBossTopRanks = 60033;
	public static final int eE2SPKTExpiratBossGlobalRanks = 60034;
	public static final int eE2SPKTExpiratBossGlobalRanks2 = 60051;
	public static final int eE2SPKTHeroesBossSync = 60052;
	public static final int eE2SPKTHeroesBossFinishAtt = 60053;
	public static final int eE2SPKTHeroesBossInfo = 60054;
	public static final int eE2SPKTSWarSearch = 60035;
	public static final int eE2SPKTSWarSearchForward = 60036;
	public static final int eE2SPKTSWarAttack = 60039;
	public static final int eE2SPKTSWarAttackForward = 60040;
	public static final int eE2SPKTSWarRanks = 60041;
	public static final int eE2SPKTSWarRank = 60042;
	public static final int eE2SPKTSWarTopRanks = 60043;
	public static final int eE2SPKTSWarGlobalRanks = 60044;
	public static final int eE2SPKTSWarVoteForward = 60045;
	public static final int eE2SPKTSWarRelease = 60046;
	public static final int eE2SPKTDuelFinishAttackOnOppoDownLine = 60047;
	public static final int eE2SPKTDuelFinishAttackOnOppoDownLineForward = 60048;

	// server to exchange
	public static final int eS2EPKTWhoAmI = 70001;
	public static final int eS2EPKTSearchCity = 70007;
	public static final int eS2EPKTSearchCityForward = 70008;
	public static final int eS2EPKTAttackCityStart = 70009;
	public static final int eS2EPKTAttackCityStartForward = 70010;
	public static final int eS2EPKTAttackCityEnd = 70011;
	public static final int eS2EPKTAttackCityEndForward = 70012;
	public static final int eS2EPKTQueryCityOwner = 70013;
	public static final int eS2EPKTQueryCityOwnerForward = 70014;
	public static final int eS2EPKTNotifyCityOwnerChanged = 70015;
	public static final int eS2EPKTChannelMessageReq = 70016;
	public static final int eS2EPKTChannelMessageRes = 70017;
	public static final int eS2EPKTDuelJoin = 70018;
	public static final int eS2EPKTDuelSelectGeneral = 70019;
	public static final int eS2EPKTDuelSelectGeneralForward = 70020;
	public static final int eS2EPKTDuelStartAttack = 70021;
	public static final int eS2EPKTDuelStartAttackForward = 70022;
	public static final int eS2EPKTDuelFinishAttack = 70023;
	public static final int eS2EPKTDuelRanks = 70024;
	public static final int eS2EPKTDuelRank = 70025;
	public static final int eS2EPKTDuelTopRanks = 70026;
	public static final int eS2EPKTDuelOppoGiveup = 70027;
	public static final int eS2EPKTDuelCancelJoin = 70028;
	public static final int eS2EPKTExpatriateDBRole = 70031;
	public static final int eS2EPKTExpatriateDBRoleForward = 70032;
	public static final int eS2EPKTExpiratBossRanks = 70033;
	public static final int eS2EPKTExpiratBossRank = 70034;
	public static final int eS2EPKTExpiratBossTopRanks = 70035;
	public static final int eS2EPKTExpiratBossGlobalRanks = 70036;
	public static final int eS2EPKTExpiratBossGlobalRanks2 = 70051;
	public static final int eS2EPKTHeroesBossSync = 70052;
	public static final int eS2EPKTHeroesBossFinishAtt = 70053;
	public static final int eS2EPKTHeroesBossInfo = 70054;
	public static final int eS2EPKTSWarSearch = 70037;
	public static final int eS2EPKTSWarSearchForward = 70038;
	public static final int eS2EPKTSWarAttack = 70041;
	public static final int eS2EPKTSWarAttackForward = 70042;
	public static final int eS2EPKTSWarRanks = 70043;
	public static final int eS2EPKTSWarRank = 70044;
	public static final int eS2EPKTSWarTopRanks = 70045;
	public static final int eS2EPKTSWarVote = 70046;
	public static final int eS2EPKTDuelFinishAttackOnOppoDownLine = 70047;

	// friend to server
	public static final int eF2SPKTStateAnnounce = 100001;
	public static final int eF2SPKTSearchFriends = 100002;
	public static final int eF2SPKTQueryFriendProp = 100003;
	public static final int eF2SPKTSendMessageToFriend = 100004;
	public static final int eF2SPKTCreateRole = 100005;

	// server to friend
	public static final int eS2FPKTWhoAmI = 110001;
	public static final int eS2FPKTCreateRole = 110002;
	public static final int eS2FPKTSearchFriends = 110003;
	public static final int eS2FPKTQueryFriendProp = 110004;
	public static final int eS2FPKTUpdateFriendProp = 110005;
	public static final int eS2FPKTSendMessageToFriend = 110006;

	// server to client
	public static class S2C
	{

		public static class ServerChallenge extends SimplePacket
		{
			public ServerChallenge() { }

			public ServerChallenge(int istate, String sstate, int flag, List<Byte> key)
			{
				this.istate = istate;
				this.sstate = sstate;
				this.flag = flag;
				this.key = key;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTServerChallenge;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				istate = is.popInteger();
				sstate = is.popString();
				flag = is.popInteger();
				key = is.popByteList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(istate);
				os.pushString(sstate);
				os.pushInteger(flag);
				os.pushByteList(key);
			}

			public int getIstate()
			{
				return istate;
			}

			public void setIstate(int istate)
			{
				this.istate = istate;
			}

			public String getSstate()
			{
				return sstate;
			}

			public void setSstate(String sstate)
			{
				this.sstate = sstate;
			}

			public int getFlag()
			{
				return flag;
			}

			public void setFlag(int flag)
			{
				this.flag = flag;
			}

			public List<Byte> getKey()
			{
				return key;
			}

			public void setKey(List<Byte> key)
			{
				this.key = key;
			}

			private int istate;
			private String sstate;
			private int flag;
			private List<Byte> key;
		}

		public static class ServerResponse extends SimplePacket
		{
			public ServerResponse() { }

			public ServerResponse(int res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTServerResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				res = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(res);
			}

			public int getRes()
			{
				return res;
			}

			public void setRes(int res)
			{
				this.res = res;
			}

			private int res;
		}

		public static class LuaChannel extends SimplePacket
		{
			public LuaChannel() { }

			public LuaChannel(String data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTLuaChannel;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(data);
			}

			public String getData()
			{
				return data;
			}

			public void setData(String data)
			{
				this.data = data;
			}

			private String data;
		}

		public static class LuaChannel2 extends SimplePacket
		{
			public LuaChannel2() { }

			public LuaChannel2(List<String> data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTLuaChannel2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popStringList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushStringList(data);
			}

			public List<String> getData()
			{
				return data;
			}

			public void setData(List<String> data)
			{
				this.data = data;
			}

			private List<String> data;
		}

		public static class CmnRPCResponse extends SimplePacket
		{
			public CmnRPCResponse() { }

			public CmnRPCResponse(SBean.RPCRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTCmnRPCResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.RPCRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.RPCRes getRes()
			{
				return res;
			}

			public void setRes(SBean.RPCRes res)
			{
				this.res = res;
			}

			private SBean.RPCRes res;
		}

		public static class AntiData extends SimplePacket
		{
			public AntiData() { }

			public AntiData(SBean.TSSAntiData data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eS2CPKTAntiData;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.TSSAntiData();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.TSSAntiData getData()
			{
				return data;
			}

			public void setData(SBean.TSSAntiData data)
			{
				this.data = data;
			}

			private SBean.TSSAntiData data;
		}

	}

	// client to server
	public static class C2S
	{

		public static class ClientResponse extends SimplePacket
		{
			public ClientResponse() { }

			public ClientResponse(List<Byte> key)
			{
				this.key = key;
			}

			@Override
			public int getType()
			{
				return Packet.eC2SPKTClientResponse;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				key = is.popByteList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByteList(key);
			}

			public List<Byte> getKey()
			{
				return key;
			}

			public void setKey(List<Byte> key)
			{
				this.key = key;
			}

			private List<Byte> key;
		}

		public static class LuaChannel extends SimplePacket
		{
			public LuaChannel() { }

			public LuaChannel(String data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eC2SPKTLuaChannel;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(data);
			}

			public String getData()
			{
				return data;
			}

			public void setData(String data)
			{
				this.data = data;
			}

			private String data;
		}

		public static class LuaChannel2 extends SimplePacket
		{
			public LuaChannel2() { }

			public LuaChannel2(List<String> data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eC2SPKTLuaChannel2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popStringList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushStringList(data);
			}

			public List<String> getData()
			{
				return data;
			}

			public void setData(List<String> data)
			{
				this.data = data;
			}

			private List<String> data;
		}

		public static class DataSync extends SimplePacket
		{
			public static final byte eBattleData = 9;
			public static final byte eAutoUserID = 12;

			public DataSync() { }

			public DataSync(byte dataType)
			{
				this.dataType = dataType;
			}

			@Override
			public int getType()
			{
				return Packet.eC2SPKTDataSync;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				dataType = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(dataType);
			}

			public byte getDataType()
			{
				return dataType;
			}

			public void setDataType(byte dataType)
			{
				this.dataType = dataType;
			}

			private byte dataType;
		}

		public static class AntiData extends SimplePacket
		{
			public AntiData() { }

			public AntiData(SBean.TSSAntiData data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eC2SPKTAntiData;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.TSSAntiData();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.TSSAntiData getData()
			{
				return data;
			}

			public void setData(SBean.TSSAntiData data)
			{
				this.data = data;
			}

			private SBean.TSSAntiData data;
		}

	}

	// global to client
	public static class G2C
	{

		public static class LuaChannel extends SimplePacket
		{
			public LuaChannel() { }

			public LuaChannel(String data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eG2CPKTLuaChannel;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(data);
			}

			public String getData()
			{
				return data;
			}

			public void setData(String data)
			{
				this.data = data;
			}

			private String data;
		}

		public static class LuaChannel2 extends SimplePacket
		{
			public LuaChannel2() { }

			public LuaChannel2(List<String> data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eG2CPKTLuaChannel2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popStringList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushStringList(data);
			}

			public List<String> getData()
			{
				return data;
			}

			public void setData(List<String> data)
			{
				this.data = data;
			}

			private List<String> data;
		}

	}

	// client to global
	public static class C2G
	{

		public static class LuaChannel extends SimplePacket
		{
			public LuaChannel() { }

			public LuaChannel(String data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eC2GPKTLuaChannel;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(data);
			}

			public String getData()
			{
				return data;
			}

			public void setData(String data)
			{
				this.data = data;
			}

			private String data;
		}

		public static class LuaChannel2 extends SimplePacket
		{
			public LuaChannel2() { }

			public LuaChannel2(List<String> data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eC2GPKTLuaChannel2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				data = is.popStringList();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushStringList(data);
			}

			public List<String> getData()
			{
				return data;
			}

			public void setData(List<String> data)
			{
				this.data = data;
			}

			private List<String> data;
		}

	}

	// manager to server
	public static class M2S
	{

		public static class Shutdown extends SimplePacket
		{
			public Shutdown() { }

			public Shutdown(byte stype, int timeStart, int timeUse)
			{
				this.stype = stype;
				this.timeStart = timeStart;
				this.timeUse = timeUse;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTShutdown;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				stype = is.popByte();
				timeStart = is.popInteger();
				timeUse = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(stype);
				os.pushInteger(timeStart);
				os.pushInteger(timeUse);
			}

			public byte getStype()
			{
				return stype;
			}

			public void setStype(byte stype)
			{
				this.stype = stype;
			}

			public int getTimeStart()
			{
				return timeStart;
			}

			public void setTimeStart(int timeStart)
			{
				this.timeStart = timeStart;
			}

			public int getTimeUse()
			{
				return timeUse;
			}

			public void setTimeUse(int timeUse)
			{
				this.timeUse = timeUse;
			}

			private byte stype;
			private int timeStart;
			private int timeUse;
		}

		public static class DataQuery extends SimplePacket
		{
			public DataQuery() { }

			public DataQuery(SBean.DataQueryReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTDataQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DataQueryReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DataQueryReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DataQueryReq req)
			{
				this.req = req;
			}

			private SBean.DataQueryReq req;
		}

		public static class Announce extends SimplePacket
		{
			public Announce() { }

			public Announce(String msg, byte times)
			{
				this.msg = msg;
				this.times = times;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTAnnounce;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				msg = is.popString();
				times = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(msg);
				os.pushByte(times);
			}

			public String getMsg()
			{
				return msg;
			}

			public void setMsg(String msg)
			{
				this.msg = msg;
			}

			public byte getTimes()
			{
				return times;
			}

			public void setTimes(byte times)
			{
				this.times = times;
			}

			private String msg;
			private byte times;
		}

		public static class DataModify extends SimplePacket
		{
			public DataModify() { }

			public DataModify(SBean.DataModifyReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTDataModify;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DataModifyReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DataModifyReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DataModifyReq req)
			{
				this.req = req;
			}

			private SBean.DataModifyReq req;
		}

		public static class SysMessage extends SimplePacket
		{
			public SysMessage() { }

			public SysMessage(SBean.SysMail mail)
			{
				this.mail = mail;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTSysMessage;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( mail == null )
					mail = new SBean.SysMail();
				is.pop(mail);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(mail);
			}

			public SBean.SysMail getMail()
			{
				return mail;
			}

			public void setMail(SBean.SysMail mail)
			{
				this.mail = mail;
			}

			private SBean.SysMail mail;
		}

		public static class RoleData extends SimplePacket
		{
			public RoleData() { }

			public RoleData(SBean.RoleDataReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTRoleData;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.RoleDataReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.RoleDataReq getReq()
			{
				return req;
			}

			public void setReq(SBean.RoleDataReq req)
			{
				this.req = req;
			}

			private SBean.RoleDataReq req;
		}

		public static class WorldGift extends SimplePacket
		{
			public WorldGift() { }

			public WorldGift(short itemID, int time, String mail)
			{
				this.itemID = itemID;
				this.time = time;
				this.mail = mail;
			}

			@Override
			public int getType()
			{
				return Packet.eM2SPKTWorldGift;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				itemID = is.popShort();
				time = is.popInteger();
				mail = is.popString();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushShort(itemID);
				os.pushInteger(time);
				os.pushString(mail);
			}

			public short getItemID()
			{
				return itemID;
			}

			public void setItemID(short itemID)
			{
				this.itemID = itemID;
			}

			public int getTime()
			{
				return time;
			}

			public void setTime(int time)
			{
				this.time = time;
			}

			public String getMail()
			{
				return mail;
			}

			public void setMail(String mail)
			{
				this.mail = mail;
			}

			private short itemID;
			private int time;
			private String mail;
		}

	}

	// server to manager
	public static class S2M
	{

		public static class Shutdown extends SimplePacket
		{
			public Shutdown() { }

			public Shutdown(byte res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTShutdown;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				res = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(res);
			}

			public byte getRes()
			{
				return res;
			}

			public void setRes(byte res)
			{
				this.res = res;
			}

			private byte res;
		}

		public static class OnlineUser extends SimplePacket
		{
			public OnlineUser() { }

			public OnlineUser(int rcnt, int scnt)
			{
				this.rcnt = rcnt;
				this.scnt = scnt;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTOnlineUser;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				rcnt = is.popInteger();
				scnt = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(rcnt);
				os.pushInteger(scnt);
			}

			public int getRcnt()
			{
				return rcnt;
			}

			public void setRcnt(int rcnt)
			{
				this.rcnt = rcnt;
			}

			public int getScnt()
			{
				return scnt;
			}

			public void setScnt(int scnt)
			{
				this.scnt = scnt;
			}

			private int rcnt;
			private int scnt;
		}

		public static class QueryRoleBrief extends SimplePacket
		{
			public QueryRoleBrief() { }

			public QueryRoleBrief(SBean.RoleBrief brief)
			{
				this.brief = brief;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTQueryRoleBrief;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( brief == null )
					brief = new SBean.RoleBrief();
				is.pop(brief);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(brief);
			}

			public SBean.RoleBrief getBrief()
			{
				return brief;
			}

			public void setBrief(SBean.RoleBrief brief)
			{
				this.brief = brief;
			}

			private SBean.RoleBrief brief;
		}

		public static class Announce extends SimplePacket
		{
			public Announce() { }

			public Announce(byte res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTAnnounce;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				res = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(res);
			}

			public byte getRes()
			{
				return res;
			}

			public void setRes(byte res)
			{
				this.res = res;
			}

			private byte res;
		}

		public static class DataModify extends SimplePacket
		{
			public DataModify() { }

			public DataModify(SBean.DataModifyRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTDataModify;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DataModifyRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DataModifyRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DataModifyRes res)
			{
				this.res = res;
			}

			private SBean.DataModifyRes res;
		}

		public static class SysMessage extends SimplePacket
		{
			public SysMessage() { }

			public SysMessage(byte res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTSysMessage;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				res = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(res);
			}

			public byte getRes()
			{
				return res;
			}

			public void setRes(byte res)
			{
				this.res = res;
			}

			private byte res;
		}

		public static class DataQuery extends SimplePacket
		{
			public DataQuery() { }

			public DataQuery(SBean.DataQueryRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTDataQuery;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DataQueryRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DataQueryRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DataQueryRes res)
			{
				this.res = res;
			}

			private SBean.DataQueryRes res;
		}

		public static class RoleData extends SimplePacket
		{
			public RoleData() { }

			public RoleData(SBean.RoleDataRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTRoleData;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.RoleDataRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.RoleDataRes getRes()
			{
				return res;
			}

			public void setRes(SBean.RoleDataRes res)
			{
				this.res = res;
			}

			private SBean.RoleDataRes res;
		}

		public static class WorldGift extends SimplePacket
		{
			public WorldGift() { }

			public WorldGift(byte res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2MPKTWorldGift;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				res = is.popByte();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(res);
			}

			public byte getRes()
			{
				return res;
			}

			public void setRes(byte res)
			{
				this.res = res;
			}

			private byte res;
		}

	}

	// exchange to server
	public static class E2S
	{

		public static class StateAnnounce extends SimplePacket
		{
			public StateAnnounce() { }

			public StateAnnounce(int serverID)
			{
				this.serverID = serverID;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTStateAnnounce;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				serverID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(serverID);
			}

			public int getServerID()
			{
				return serverID;
			}

			public void setServerID(int serverID)
			{
				this.serverID = serverID;
			}

			private int serverID;
		}

		public static class SearchCity extends SimplePacket
		{
			public SearchCity() { }

			public SearchCity(SBean.SearchCityRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSearchCity;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SearchCityRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SearchCityRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SearchCityRes res)
			{
				this.res = res;
			}

			private SBean.SearchCityRes res;
		}

		public static class SearchCityForward extends SimplePacket
		{
			public SearchCityForward() { }

			public SearchCityForward(SBean.SearchCityReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSearchCityForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SearchCityReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SearchCityReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SearchCityReq req)
			{
				this.req = req;
			}

			private SBean.SearchCityReq req;
		}

		public static class AttackCityStart extends SimplePacket
		{
			public AttackCityStart() { }

			public AttackCityStart(SBean.AttackCityStartRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTAttackCityStart;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.AttackCityStartRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.AttackCityStartRes getRes()
			{
				return res;
			}

			public void setRes(SBean.AttackCityStartRes res)
			{
				this.res = res;
			}

			private SBean.AttackCityStartRes res;
		}

		public static class AttackCityStartForward extends SimplePacket
		{
			public AttackCityStartForward() { }

			public AttackCityStartForward(SBean.AttackCityStartReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTAttackCityStartForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.AttackCityStartReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.AttackCityStartReq getReq()
			{
				return req;
			}

			public void setReq(SBean.AttackCityStartReq req)
			{
				this.req = req;
			}

			private SBean.AttackCityStartReq req;
		}

		public static class AttackCityEnd extends SimplePacket
		{
			public AttackCityEnd() { }

			public AttackCityEnd(SBean.AttackCityEndRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTAttackCityEnd;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.AttackCityEndRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.AttackCityEndRes getRes()
			{
				return res;
			}

			public void setRes(SBean.AttackCityEndRes res)
			{
				this.res = res;
			}

			private SBean.AttackCityEndRes res;
		}

		public static class AttackCityEndForward extends SimplePacket
		{
			public AttackCityEndForward() { }

			public AttackCityEndForward(SBean.AttackCityEndReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTAttackCityEndForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.AttackCityEndReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.AttackCityEndReq getReq()
			{
				return req;
			}

			public void setReq(SBean.AttackCityEndReq req)
			{
				this.req = req;
			}

			private SBean.AttackCityEndReq req;
		}

		public static class QueryCityOwner extends SimplePacket
		{
			public QueryCityOwner() { }

			public QueryCityOwner(SBean.QueryCityOwnerRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTQueryCityOwner;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.QueryCityOwnerRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.QueryCityOwnerRes getRes()
			{
				return res;
			}

			public void setRes(SBean.QueryCityOwnerRes res)
			{
				this.res = res;
			}

			private SBean.QueryCityOwnerRes res;
		}

		public static class QueryCityOwnerForward extends SimplePacket
		{
			public QueryCityOwnerForward() { }

			public QueryCityOwnerForward(SBean.QueryCityOwnerReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTQueryCityOwnerForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.QueryCityOwnerReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.QueryCityOwnerReq getReq()
			{
				return req;
			}

			public void setReq(SBean.QueryCityOwnerReq req)
			{
				this.req = req;
			}

			private SBean.QueryCityOwnerReq req;
		}

		public static class NotifyCityOwnerChangedForward extends SimplePacket
		{
			public NotifyCityOwnerChangedForward() { }

			public NotifyCityOwnerChangedForward(SBean.NotifyCityOwnerChangedReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTNotifyCityOwnerChangedForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.NotifyCityOwnerChangedReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.NotifyCityOwnerChangedReq getReq()
			{
				return req;
			}

			public void setReq(SBean.NotifyCityOwnerChangedReq req)
			{
				this.req = req;
			}

			private SBean.NotifyCityOwnerChangedReq req;
		}

		public static class ChannelMessageReq extends SimplePacket
		{
			public ChannelMessageReq() { }

			public ChannelMessageReq(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTChannelMessageReq;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.ExchangeChannelMessage();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.ExchangeChannelMessage getData()
			{
				return data;
			}

			public void setData(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			private SBean.ExchangeChannelMessage data;
		}

		public static class ChannelMessageRes extends SimplePacket
		{
			public ChannelMessageRes() { }

			public ChannelMessageRes(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTChannelMessageRes;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.ExchangeChannelMessage();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.ExchangeChannelMessage getData()
			{
				return data;
			}

			public void setData(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			private SBean.ExchangeChannelMessage data;
		}

		public static class DuelJoin extends SimplePacket
		{
			public DuelJoin() { }

			public DuelJoin(SBean.DuelJoinRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelJoin;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelJoinRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelJoinRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelJoinRes res)
			{
				this.res = res;
			}

			private SBean.DuelJoinRes res;
		}

		public static class DuelSelectGeneral extends SimplePacket
		{
			public DuelSelectGeneral() { }

			public DuelSelectGeneral(SBean.DuelSelectGeneralRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelSelectGeneral;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelSelectGeneralRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelSelectGeneralRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelSelectGeneralRes res)
			{
				this.res = res;
			}

			private SBean.DuelSelectGeneralRes res;
		}

		public static class DuelSelectGeneralForward extends SimplePacket
		{
			public DuelSelectGeneralForward() { }

			public DuelSelectGeneralForward(SBean.DuelSelectGeneralReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelSelectGeneralForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelSelectGeneralReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelSelectGeneralReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelSelectGeneralReq req)
			{
				this.req = req;
			}

			private SBean.DuelSelectGeneralReq req;
		}

		public static class DuelStartAttack extends SimplePacket
		{
			public DuelStartAttack() { }

			public DuelStartAttack(SBean.DuelStartAttackRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelStartAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelStartAttackRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelStartAttackRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelStartAttackRes res)
			{
				this.res = res;
			}

			private SBean.DuelStartAttackRes res;
		}

		public static class DuelStartAttackForward extends SimplePacket
		{
			public DuelStartAttackForward() { }

			public DuelStartAttackForward(SBean.DuelStartAttackReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelStartAttackForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelStartAttackReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelStartAttackReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelStartAttackReq req)
			{
				this.req = req;
			}

			private SBean.DuelStartAttackReq req;
		}

		public static class DuelFinishAttack extends SimplePacket
		{
			public DuelFinishAttack() { }

			public DuelFinishAttack(SBean.DuelFinishAttackRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelFinishAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelFinishAttackRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelFinishAttackRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelFinishAttackRes res)
			{
				this.res = res;
			}

			private SBean.DuelFinishAttackRes res;
		}

		public static class DuelRanks extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class DuelRank extends SimplePacket
		{
			public DuelRank() { }

			public DuelRank(SBean.DuelRankRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelRankRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelRankRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelRankRes res)
			{
				this.res = res;
			}

			private SBean.DuelRankRes res;
		}

		public static class DuelTopRanks extends SimplePacket
		{
			public DuelTopRanks() { }

			public DuelTopRanks(SBean.DuelTopRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelTopRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelTopRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelTopRanksRes res)
			{
				this.res = res;
			}

			private SBean.DuelTopRanksRes res;
		}

		public static class DuelOppoGiveup extends SimplePacket
		{
			public DuelOppoGiveup() { }

			public DuelOppoGiveup(SBean.DuelOppoGiveupRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelOppoGiveup;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelOppoGiveupRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelOppoGiveupRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelOppoGiveupRes res)
			{
				this.res = res;
			}

			private SBean.DuelOppoGiveupRes res;
		}

		public static class DuelOppoGiveupForward extends SimplePacket
		{
			public DuelOppoGiveupForward() { }

			public DuelOppoGiveupForward(SBean.DuelOppoGiveupRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelOppoGiveupForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelOppoGiveupRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelOppoGiveupRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelOppoGiveupRes res)
			{
				this.res = res;
			}

			private SBean.DuelOppoGiveupRes res;
		}

		public static class DuelGlobalRanks extends SimplePacket
		{
			public DuelGlobalRanks() { }

			public DuelGlobalRanks(SBean.DuelGlobalRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelGlobalRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelGlobalRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelGlobalRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelGlobalRanksRes res)
			{
				this.res = res;
			}

			private SBean.DuelGlobalRanksRes res;
		}

		public static class ExpatriateDBRole extends SimplePacket
		{
			public ExpatriateDBRole() { }

			public ExpatriateDBRole(SBean.ExpatriateTransRoleRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpatriateDBRole;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpatriateTransRoleRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpatriateTransRoleRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpatriateTransRoleRes res)
			{
				this.res = res;
			}

			private SBean.ExpatriateTransRoleRes res;
		}

		public static class ExpatriateDBRoleForward extends SimplePacket
		{
			public ExpatriateDBRoleForward() { }

			public ExpatriateDBRoleForward(SBean.ExpatriateTransRoleReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpatriateDBRoleForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpatriateTransRoleReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpatriateTransRoleReq getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpatriateTransRoleReq req)
			{
				this.req = req;
			}

			private SBean.ExpatriateTransRoleReq req;
		}

		public static class ExpiratBossRanks extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpiratBossRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class ExpiratBossRank extends SimplePacket
		{
			public ExpiratBossRank() { }

			public ExpiratBossRank(SBean.ExpiratBossRankRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpiratBossRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpiratBossRankRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpiratBossRankRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpiratBossRankRes res)
			{
				this.res = res;
			}

			private SBean.ExpiratBossRankRes res;
		}

		public static class ExpiratBossTopRanks extends SimplePacket
		{
			public ExpiratBossTopRanks() { }

			public ExpiratBossTopRanks(SBean.ExpiratBossTopRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpiratBossTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpiratBossTopRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpiratBossTopRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpiratBossTopRanksRes res)
			{
				this.res = res;
			}

			private SBean.ExpiratBossTopRanksRes res;
		}

		public static class ExpiratBossGlobalRanks extends SimplePacket
		{
			public ExpiratBossGlobalRanks() { }

			public ExpiratBossGlobalRanks(SBean.ExpiratBossGlobalRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpiratBossGlobalRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpiratBossGlobalRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpiratBossGlobalRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpiratBossGlobalRanksRes res)
			{
				this.res = res;
			}

			private SBean.ExpiratBossGlobalRanksRes res;
		}

		public static class ExpiratBossGlobalRanks2 extends SimplePacket
		{
			public ExpiratBossGlobalRanks2() { }

			public ExpiratBossGlobalRanks2(SBean.ExpiratBossGlobalRanksRes2 res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTExpiratBossGlobalRanks2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpiratBossGlobalRanksRes2();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpiratBossGlobalRanksRes2 getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpiratBossGlobalRanksRes2 res)
			{
				this.res = res;
			}

			private SBean.ExpiratBossGlobalRanksRes2 res;
		}

		public static class HeroesBossSync extends SimplePacket
		{
			public HeroesBossSync() { }

			public HeroesBossSync(SBean.HeroesBossSyncRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTHeroesBossSync;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.HeroesBossSyncRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.HeroesBossSyncRes getRes()
			{
				return res;
			}

			public void setRes(SBean.HeroesBossSyncRes res)
			{
				this.res = res;
			}

			private SBean.HeroesBossSyncRes res;
		}

		public static class HeroesBossFinishAtt extends SimplePacket
		{
			public HeroesBossFinishAtt() { }

			public HeroesBossFinishAtt(SBean.HeroesBossFinishAttRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTHeroesBossFinishAtt;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.HeroesBossFinishAttRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.HeroesBossFinishAttRes getRes()
			{
				return res;
			}

			public void setRes(SBean.HeroesBossFinishAttRes res)
			{
				this.res = res;
			}

			private SBean.HeroesBossFinishAttRes res;
		}

		public static class HeroesBossInfo extends SimplePacket
		{
			public HeroesBossInfo() { }

			public HeroesBossInfo(SBean.HeroesBossInfoRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTHeroesBossInfo;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.HeroesBossInfoRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.HeroesBossInfoRes getRes()
			{
				return res;
			}

			public void setRes(SBean.HeroesBossInfoRes res)
			{
				this.res = res;
			}

			private SBean.HeroesBossInfoRes res;
		}

		public static class SWarSearch extends SimplePacket
		{
			public SWarSearch() { }

			public SWarSearch(SBean.SWarSearchRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarSearch;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarSearchRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarSearchRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarSearchRes res)
			{
				this.res = res;
			}

			private SBean.SWarSearchRes res;
		}

		public static class SWarSearchForward extends SimplePacket
		{
			public SWarSearchForward() { }

			public SWarSearchForward(SBean.SWarSearchReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarSearchForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarSearchReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarSearchReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarSearchReq req)
			{
				this.req = req;
			}

			private SBean.SWarSearchReq req;
		}

		public static class SWarAttack extends SimplePacket
		{
			public SWarAttack() { }

			public SWarAttack(SBean.SWarAttackRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarAttackRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarAttackRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarAttackRes res)
			{
				this.res = res;
			}

			private SBean.SWarAttackRes res;
		}

		public static class SWarAttackForward extends SimplePacket
		{
			public SWarAttackForward() { }

			public SWarAttackForward(SBean.SWarAttackReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarAttackForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarAttackReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarAttackReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarAttackReq req)
			{
				this.req = req;
			}

			private SBean.SWarAttackReq req;
		}

		public static class SWarRanks extends SimplePacket
		{
			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
			}

			@Override
			public void encode(Stream.AOStream os)
			{
			}

		}

		public static class SWarRank extends SimplePacket
		{
			public SWarRank() { }

			public SWarRank(SBean.SWarRankRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarRankRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarRankRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarRankRes res)
			{
				this.res = res;
			}

			private SBean.SWarRankRes res;
		}

		public static class SWarTopRanks extends SimplePacket
		{
			public SWarTopRanks() { }

			public SWarTopRanks(SBean.SWarTopRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarTopRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarTopRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarTopRanksRes res)
			{
				this.res = res;
			}

			private SBean.SWarTopRanksRes res;
		}

		public static class SWarGlobalRanks extends SimplePacket
		{
			public SWarGlobalRanks() { }

			public SWarGlobalRanks(SBean.SWarGlobalRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarGlobalRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarGlobalRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarGlobalRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarGlobalRanksRes res)
			{
				this.res = res;
			}

			private SBean.SWarGlobalRanksRes res;
		}

		public static class SWarVoteForward extends SimplePacket
		{
			public SWarVoteForward() { }

			public SWarVoteForward(SBean.SWarVoteReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarVoteForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarVoteReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarVoteReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarVoteReq req)
			{
				this.req = req;
			}

			private SBean.SWarVoteReq req;
		}

		public static class SWarRelease extends SimplePacket
		{
			public SWarRelease() { }

			public SWarRelease(SBean.SWarReleaseReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTSWarRelease;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarReleaseReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarReleaseReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarReleaseReq req)
			{
				this.req = req;
			}

			private SBean.SWarReleaseReq req;
		}

		public static class DuelFinishAttackOnOppoDownLine extends SimplePacket
		{
			public DuelFinishAttackOnOppoDownLine() { }

			public DuelFinishAttackOnOppoDownLine(SBean.DuelFinishAttackOnOppoDownLineRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelFinishAttackOnOppoDownLine;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelFinishAttackOnOppoDownLineRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelFinishAttackOnOppoDownLineRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelFinishAttackOnOppoDownLineRes res)
			{
				this.res = res;
			}

			private SBean.DuelFinishAttackOnOppoDownLineRes res;
		}

		public static class DuelFinishAttackOnOppoDownLineForward extends SimplePacket
		{
			public DuelFinishAttackOnOppoDownLineForward() { }

			public DuelFinishAttackOnOppoDownLineForward(SBean.DuelFinishAttackOnOppoDownLineRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eE2SPKTDuelFinishAttackOnOppoDownLineForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelFinishAttackOnOppoDownLineRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelFinishAttackOnOppoDownLineRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelFinishAttackOnOppoDownLineRes res)
			{
				this.res = res;
			}

			private SBean.DuelFinishAttackOnOppoDownLineRes res;
		}

	}

	// server to exchange
	public static class S2E
	{

		public static class WhoAmI extends SimplePacket
		{
			public WhoAmI() { }

			public WhoAmI(byte platID, int serverID)
			{
				this.platID = platID;
				this.serverID = serverID;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTWhoAmI;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				platID = is.popByte();
				serverID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(platID);
				os.pushInteger(serverID);
			}

			public byte getPlatID()
			{
				return platID;
			}

			public void setPlatID(byte platID)
			{
				this.platID = platID;
			}

			public int getServerID()
			{
				return serverID;
			}

			public void setServerID(int serverID)
			{
				this.serverID = serverID;
			}

			private byte platID;
			private int serverID;
		}

		public static class SearchCity extends SimplePacket
		{
			public SearchCity() { }

			public SearchCity(SBean.SearchCityReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSearchCity;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SearchCityReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SearchCityReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SearchCityReq req)
			{
				this.req = req;
			}

			private SBean.SearchCityReq req;
		}

		public static class SearchCityForward extends SimplePacket
		{
			public SearchCityForward() { }

			public SearchCityForward(SBean.SearchCityRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSearchCityForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SearchCityRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SearchCityRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SearchCityRes res)
			{
				this.res = res;
			}

			private SBean.SearchCityRes res;
		}

		public static class AttackCityStart extends SimplePacket
		{
			public AttackCityStart() { }

			public AttackCityStart(SBean.AttackCityStartReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTAttackCityStart;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.AttackCityStartReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.AttackCityStartReq getReq()
			{
				return req;
			}

			public void setReq(SBean.AttackCityStartReq req)
			{
				this.req = req;
			}

			private SBean.AttackCityStartReq req;
		}

		public static class AttackCityStartForward extends SimplePacket
		{
			public AttackCityStartForward() { }

			public AttackCityStartForward(SBean.AttackCityStartRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTAttackCityStartForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.AttackCityStartRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.AttackCityStartRes getRes()
			{
				return res;
			}

			public void setRes(SBean.AttackCityStartRes res)
			{
				this.res = res;
			}

			private SBean.AttackCityStartRes res;
		}

		public static class AttackCityEnd extends SimplePacket
		{
			public AttackCityEnd() { }

			public AttackCityEnd(SBean.AttackCityEndReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTAttackCityEnd;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.AttackCityEndReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.AttackCityEndReq getReq()
			{
				return req;
			}

			public void setReq(SBean.AttackCityEndReq req)
			{
				this.req = req;
			}

			private SBean.AttackCityEndReq req;
		}

		public static class AttackCityEndForward extends SimplePacket
		{
			public AttackCityEndForward() { }

			public AttackCityEndForward(SBean.AttackCityEndRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTAttackCityEndForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.AttackCityEndRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.AttackCityEndRes getRes()
			{
				return res;
			}

			public void setRes(SBean.AttackCityEndRes res)
			{
				this.res = res;
			}

			private SBean.AttackCityEndRes res;
		}

		public static class QueryCityOwner extends SimplePacket
		{
			public QueryCityOwner() { }

			public QueryCityOwner(SBean.QueryCityOwnerReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTQueryCityOwner;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.QueryCityOwnerReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.QueryCityOwnerReq getReq()
			{
				return req;
			}

			public void setReq(SBean.QueryCityOwnerReq req)
			{
				this.req = req;
			}

			private SBean.QueryCityOwnerReq req;
		}

		public static class QueryCityOwnerForward extends SimplePacket
		{
			public QueryCityOwnerForward() { }

			public QueryCityOwnerForward(SBean.QueryCityOwnerRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTQueryCityOwnerForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.QueryCityOwnerRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.QueryCityOwnerRes getRes()
			{
				return res;
			}

			public void setRes(SBean.QueryCityOwnerRes res)
			{
				this.res = res;
			}

			private SBean.QueryCityOwnerRes res;
		}

		public static class NotifyCityOwnerChanged extends SimplePacket
		{
			public NotifyCityOwnerChanged() { }

			public NotifyCityOwnerChanged(SBean.NotifyCityOwnerChangedReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTNotifyCityOwnerChanged;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.NotifyCityOwnerChangedReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.NotifyCityOwnerChangedReq getReq()
			{
				return req;
			}

			public void setReq(SBean.NotifyCityOwnerChangedReq req)
			{
				this.req = req;
			}

			private SBean.NotifyCityOwnerChangedReq req;
		}

		public static class ChannelMessageReq extends SimplePacket
		{
			public ChannelMessageReq() { }

			public ChannelMessageReq(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTChannelMessageReq;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.ExchangeChannelMessage();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.ExchangeChannelMessage getData()
			{
				return data;
			}

			public void setData(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			private SBean.ExchangeChannelMessage data;
		}

		public static class ChannelMessageRes extends SimplePacket
		{
			public ChannelMessageRes() { }

			public ChannelMessageRes(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTChannelMessageRes;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( data == null )
					data = new SBean.ExchangeChannelMessage();
				is.pop(data);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(data);
			}

			public SBean.ExchangeChannelMessage getData()
			{
				return data;
			}

			public void setData(SBean.ExchangeChannelMessage data)
			{
				this.data = data;
			}

			private SBean.ExchangeChannelMessage data;
		}

		public static class DuelJoin extends SimplePacket
		{
			public DuelJoin() { }

			public DuelJoin(SBean.DuelJoinReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelJoin;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelJoinReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelJoinReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelJoinReq req)
			{
				this.req = req;
			}

			private SBean.DuelJoinReq req;
		}

		public static class DuelSelectGeneral extends SimplePacket
		{
			public DuelSelectGeneral() { }

			public DuelSelectGeneral(SBean.DuelSelectGeneralReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelSelectGeneral;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelSelectGeneralReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelSelectGeneralReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelSelectGeneralReq req)
			{
				this.req = req;
			}

			private SBean.DuelSelectGeneralReq req;
		}

		public static class DuelSelectGeneralForward extends SimplePacket
		{
			public DuelSelectGeneralForward() { }

			public DuelSelectGeneralForward(SBean.DuelSelectGeneralRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelSelectGeneralForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelSelectGeneralRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelSelectGeneralRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelSelectGeneralRes res)
			{
				this.res = res;
			}

			private SBean.DuelSelectGeneralRes res;
		}

		public static class DuelStartAttack extends SimplePacket
		{
			public DuelStartAttack() { }

			public DuelStartAttack(SBean.DuelStartAttackReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelStartAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelStartAttackReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelStartAttackReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelStartAttackReq req)
			{
				this.req = req;
			}

			private SBean.DuelStartAttackReq req;
		}

		public static class DuelStartAttackForward extends SimplePacket
		{
			public DuelStartAttackForward() { }

			public DuelStartAttackForward(SBean.DuelStartAttackRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelStartAttackForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelStartAttackRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelStartAttackRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelStartAttackRes res)
			{
				this.res = res;
			}

			private SBean.DuelStartAttackRes res;
		}

		public static class DuelFinishAttack extends SimplePacket
		{
			public DuelFinishAttack() { }

			public DuelFinishAttack(SBean.DuelFinishAttackReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelFinishAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelFinishAttackReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelFinishAttackReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelFinishAttackReq req)
			{
				this.req = req;
			}

			private SBean.DuelFinishAttackReq req;
		}

		public static class DuelRanks extends SimplePacket
		{
			public DuelRanks() { }

			public DuelRanks(SBean.DuelRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.DuelRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.DuelRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.DuelRanksRes res)
			{
				this.res = res;
			}

			private SBean.DuelRanksRes res;
		}

		public static class DuelRank extends SimplePacket
		{
			public DuelRank() { }

			public DuelRank(SBean.DuelRankReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelRankReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelRankReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelRankReq req)
			{
				this.req = req;
			}

			private SBean.DuelRankReq req;
		}

		public static class DuelTopRanks extends SimplePacket
		{
			public DuelTopRanks() { }

			public DuelTopRanks(SBean.DuelTopRanksReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelTopRanksReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelTopRanksReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelTopRanksReq req)
			{
				this.req = req;
			}

			private SBean.DuelTopRanksReq req;
		}

		public static class DuelOppoGiveup extends SimplePacket
		{
			public DuelOppoGiveup() { }

			public DuelOppoGiveup(SBean.DuelOppoGiveupReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelOppoGiveup;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelOppoGiveupReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelOppoGiveupReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelOppoGiveupReq req)
			{
				this.req = req;
			}

			private SBean.DuelOppoGiveupReq req;
		}

		public static class DuelCancelJoin extends SimplePacket
		{
			public DuelCancelJoin() { }

			public DuelCancelJoin(SBean.DuelCancelJoinReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelCancelJoin;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelCancelJoinReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelCancelJoinReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelCancelJoinReq req)
			{
				this.req = req;
			}

			private SBean.DuelCancelJoinReq req;
		}

		public static class ExpatriateDBRole extends SimplePacket
		{
			public ExpatriateDBRole() { }

			public ExpatriateDBRole(SBean.ExpatriateTransRoleReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpatriateDBRole;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpatriateTransRoleReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpatriateTransRoleReq getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpatriateTransRoleReq req)
			{
				this.req = req;
			}

			private SBean.ExpatriateTransRoleReq req;
		}

		public static class ExpatriateDBRoleForward extends SimplePacket
		{
			public ExpatriateDBRoleForward() { }

			public ExpatriateDBRoleForward(SBean.ExpatriateTransRoleRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpatriateDBRoleForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpatriateTransRoleRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpatriateTransRoleRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpatriateTransRoleRes res)
			{
				this.res = res;
			}

			private SBean.ExpatriateTransRoleRes res;
		}

		public static class ExpiratBossRanks extends SimplePacket
		{
			public ExpiratBossRanks() { }

			public ExpiratBossRanks(SBean.ExpiratBossRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpiratBossRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.ExpiratBossRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.ExpiratBossRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.ExpiratBossRanksRes res)
			{
				this.res = res;
			}

			private SBean.ExpiratBossRanksRes res;
		}

		public static class ExpiratBossRank extends SimplePacket
		{
			public ExpiratBossRank() { }

			public ExpiratBossRank(SBean.ExpiratBossRankReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpiratBossRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpiratBossRankReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpiratBossRankReq getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpiratBossRankReq req)
			{
				this.req = req;
			}

			private SBean.ExpiratBossRankReq req;
		}

		public static class ExpiratBossTopRanks extends SimplePacket
		{
			public ExpiratBossTopRanks() { }

			public ExpiratBossTopRanks(SBean.ExpiratBossTopRanksReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpiratBossTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpiratBossTopRanksReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpiratBossTopRanksReq getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpiratBossTopRanksReq req)
			{
				this.req = req;
			}

			private SBean.ExpiratBossTopRanksReq req;
		}

		public static class ExpiratBossGlobalRanks extends SimplePacket
		{
			public ExpiratBossGlobalRanks() { }

			public ExpiratBossGlobalRanks(SBean.ExpiratBossGlobalRanksReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpiratBossGlobalRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpiratBossGlobalRanksReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpiratBossGlobalRanksReq getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpiratBossGlobalRanksReq req)
			{
				this.req = req;
			}

			private SBean.ExpiratBossGlobalRanksReq req;
		}

		public static class ExpiratBossGlobalRanks2 extends SimplePacket
		{
			public ExpiratBossGlobalRanks2() { }

			public ExpiratBossGlobalRanks2(SBean.ExpiratBossGlobalRanksReq2 req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTExpiratBossGlobalRanks2;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.ExpiratBossGlobalRanksReq2();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.ExpiratBossGlobalRanksReq2 getReq()
			{
				return req;
			}

			public void setReq(SBean.ExpiratBossGlobalRanksReq2 req)
			{
				this.req = req;
			}

			private SBean.ExpiratBossGlobalRanksReq2 req;
		}

		public static class HeroesBossSync extends SimplePacket
		{
			public HeroesBossSync() { }

			public HeroesBossSync(SBean.HeroesBossSyncReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTHeroesBossSync;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.HeroesBossSyncReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.HeroesBossSyncReq getReq()
			{
				return req;
			}

			public void setReq(SBean.HeroesBossSyncReq req)
			{
				this.req = req;
			}

			private SBean.HeroesBossSyncReq req;
		}

		public static class HeroesBossFinishAtt extends SimplePacket
		{
			public HeroesBossFinishAtt() { }

			public HeroesBossFinishAtt(SBean.HeroesBossFinishAttReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTHeroesBossFinishAtt;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.HeroesBossFinishAttReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.HeroesBossFinishAttReq getReq()
			{
				return req;
			}

			public void setReq(SBean.HeroesBossFinishAttReq req)
			{
				this.req = req;
			}

			private SBean.HeroesBossFinishAttReq req;
		}

		public static class HeroesBossInfo extends SimplePacket
		{
			public HeroesBossInfo() { }

			public HeroesBossInfo(SBean.HeroesBossInfoReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTHeroesBossInfo;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.HeroesBossInfoReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.HeroesBossInfoReq getReq()
			{
				return req;
			}

			public void setReq(SBean.HeroesBossInfoReq req)
			{
				this.req = req;
			}

			private SBean.HeroesBossInfoReq req;
		}

		public static class SWarSearch extends SimplePacket
		{
			public SWarSearch() { }

			public SWarSearch(SBean.SWarSearchReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarSearch;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarSearchReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarSearchReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarSearchReq req)
			{
				this.req = req;
			}

			private SBean.SWarSearchReq req;
		}

		public static class SWarSearchForward extends SimplePacket
		{
			public SWarSearchForward() { }

			public SWarSearchForward(SBean.SWarSearchRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarSearchForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarSearchRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarSearchRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarSearchRes res)
			{
				this.res = res;
			}

			private SBean.SWarSearchRes res;
		}

		public static class SWarAttack extends SimplePacket
		{
			public SWarAttack() { }

			public SWarAttack(SBean.SWarAttackReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarAttack;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarAttackReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarAttackReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarAttackReq req)
			{
				this.req = req;
			}

			private SBean.SWarAttackReq req;
		}

		public static class SWarAttackForward extends SimplePacket
		{
			public SWarAttackForward() { }

			public SWarAttackForward(SBean.SWarAttackRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarAttackForward;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarAttackRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarAttackRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarAttackRes res)
			{
				this.res = res;
			}

			private SBean.SWarAttackRes res;
		}

		public static class SWarRanks extends SimplePacket
		{
			public SWarRanks() { }

			public SWarRanks(SBean.SWarRanksRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SWarRanksRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SWarRanksRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SWarRanksRes res)
			{
				this.res = res;
			}

			private SBean.SWarRanksRes res;
		}

		public static class SWarRank extends SimplePacket
		{
			public SWarRank() { }

			public SWarRank(SBean.SWarRankReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarRank;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarRankReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarRankReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarRankReq req)
			{
				this.req = req;
			}

			private SBean.SWarRankReq req;
		}

		public static class SWarTopRanks extends SimplePacket
		{
			public SWarTopRanks() { }

			public SWarTopRanks(SBean.SWarTopRanksReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarTopRanks;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarTopRanksReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarTopRanksReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarTopRanksReq req)
			{
				this.req = req;
			}

			private SBean.SWarTopRanksReq req;
		}

		public static class SWarVote extends SimplePacket
		{
			public SWarVote() { }

			public SWarVote(SBean.SWarVoteReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTSWarVote;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SWarVoteReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SWarVoteReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SWarVoteReq req)
			{
				this.req = req;
			}

			private SBean.SWarVoteReq req;
		}

		public static class DuelFinishAttackOnOppoDownLine extends SimplePacket
		{
			public DuelFinishAttackOnOppoDownLine() { }

			public DuelFinishAttackOnOppoDownLine(SBean.DuelFinishAttackOnOppoDownLineReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2EPKTDuelFinishAttackOnOppoDownLine;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.DuelFinishAttackOnOppoDownLineReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.DuelFinishAttackOnOppoDownLineReq getReq()
			{
				return req;
			}

			public void setReq(SBean.DuelFinishAttackOnOppoDownLineReq req)
			{
				this.req = req;
			}

			private SBean.DuelFinishAttackOnOppoDownLineReq req;
		}

	}

	// friend to server
	public static class F2S
	{

		public static class StateAnnounce extends SimplePacket
		{
			public StateAnnounce() { }

			public StateAnnounce(int serverID)
			{
				this.serverID = serverID;
			}

			@Override
			public int getType()
			{
				return Packet.eF2SPKTStateAnnounce;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				serverID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushInteger(serverID);
			}

			public int getServerID()
			{
				return serverID;
			}

			public void setServerID(int serverID)
			{
				this.serverID = serverID;
			}

			private int serverID;
		}

		public static class SearchFriends extends SimplePacket
		{
			public SearchFriends() { }

			public SearchFriends(SBean.SearchFriendsRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eF2SPKTSearchFriends;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.SearchFriendsRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.SearchFriendsRes getRes()
			{
				return res;
			}

			public void setRes(SBean.SearchFriendsRes res)
			{
				this.res = res;
			}

			private SBean.SearchFriendsRes res;
		}

		public static class QueryFriendProp extends SimplePacket
		{
			public QueryFriendProp() { }

			public QueryFriendProp(SBean.QueryFriendPropReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eF2SPKTQueryFriendProp;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.QueryFriendPropReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.QueryFriendPropReq getReq()
			{
				return req;
			}

			public void setReq(SBean.QueryFriendPropReq req)
			{
				this.req = req;
			}

			private SBean.QueryFriendPropReq req;
		}

		public static class SendMessageToFriend extends SimplePacket
		{
			public SendMessageToFriend() { }

			public SendMessageToFriend(SBean.GlobalRoleID srcID, SBean.GlobalRoleID dstID, byte msgType, short arg1, 
			                           int arg2)
			{
				this.srcID = srcID;
				this.dstID = dstID;
				this.msgType = msgType;
				this.arg1 = arg1;
				this.arg2 = arg2;
			}

			@Override
			public int getType()
			{
				return Packet.eF2SPKTSendMessageToFriend;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( srcID == null )
					srcID = new SBean.GlobalRoleID();
				is.pop(srcID);
				if( dstID == null )
					dstID = new SBean.GlobalRoleID();
				is.pop(dstID);
				msgType = is.popByte();
				arg1 = is.popShort();
				arg2 = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(srcID);
				os.push(dstID);
				os.pushByte(msgType);
				os.pushShort(arg1);
				os.pushInteger(arg2);
			}

			public SBean.GlobalRoleID getSrcID()
			{
				return srcID;
			}

			public void setSrcID(SBean.GlobalRoleID srcID)
			{
				this.srcID = srcID;
			}

			public SBean.GlobalRoleID getDstID()
			{
				return dstID;
			}

			public void setDstID(SBean.GlobalRoleID dstID)
			{
				this.dstID = dstID;
			}

			public byte getMsgType()
			{
				return msgType;
			}

			public void setMsgType(byte msgType)
			{
				this.msgType = msgType;
			}

			public short getArg1()
			{
				return arg1;
			}

			public void setArg1(short arg1)
			{
				this.arg1 = arg1;
			}

			public int getArg2()
			{
				return arg2;
			}

			public void setArg2(int arg2)
			{
				this.arg2 = arg2;
			}

			private SBean.GlobalRoleID srcID;
			private SBean.GlobalRoleID dstID;
			private byte msgType;
			private short arg1;
			private int arg2;
		}

		public static class CreateRole extends SimplePacket
		{
			public CreateRole() { }

			public CreateRole(SBean.GlobalRoleID roleID)
			{
				this.roleID = roleID;
			}

			@Override
			public int getType()
			{
				return Packet.eF2SPKTCreateRole;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( roleID == null )
					roleID = new SBean.GlobalRoleID();
				is.pop(roleID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(roleID);
			}

			public SBean.GlobalRoleID getRoleID()
			{
				return roleID;
			}

			public void setRoleID(SBean.GlobalRoleID roleID)
			{
				this.roleID = roleID;
			}

			private SBean.GlobalRoleID roleID;
		}

	}

	// server to friend
	public static class S2F
	{

		public static class WhoAmI extends SimplePacket
		{
			public WhoAmI() { }

			public WhoAmI(byte platID, int serverID)
			{
				this.platID = platID;
				this.serverID = serverID;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTWhoAmI;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				platID = is.popByte();
				serverID = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushByte(platID);
				os.pushInteger(serverID);
			}

			public byte getPlatID()
			{
				return platID;
			}

			public void setPlatID(byte platID)
			{
				this.platID = platID;
			}

			public int getServerID()
			{
				return serverID;
			}

			public void setServerID(int serverID)
			{
				this.serverID = serverID;
			}

			private byte platID;
			private int serverID;
		}

		public static class CreateRole extends SimplePacket
		{
			public CreateRole() { }

			public CreateRole(String openID, SBean.GlobalRoleID roleID)
			{
				this.openID = openID;
				this.roleID = roleID;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTCreateRole;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				openID = is.popString();
				if( roleID == null )
					roleID = new SBean.GlobalRoleID();
				is.pop(roleID);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.pushString(openID);
				os.push(roleID);
			}

			public String getOpenID()
			{
				return openID;
			}

			public void setOpenID(String openID)
			{
				this.openID = openID;
			}

			public SBean.GlobalRoleID getRoleID()
			{
				return roleID;
			}

			public void setRoleID(SBean.GlobalRoleID roleID)
			{
				this.roleID = roleID;
			}

			private String openID;
			private SBean.GlobalRoleID roleID;
		}

		public static class SearchFriends extends SimplePacket
		{
			public SearchFriends() { }

			public SearchFriends(SBean.SearchFriendsReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTSearchFriends;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.SearchFriendsReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.SearchFriendsReq getReq()
			{
				return req;
			}

			public void setReq(SBean.SearchFriendsReq req)
			{
				this.req = req;
			}

			private SBean.SearchFriendsReq req;
		}

		public static class QueryFriendProp extends SimplePacket
		{
			public QueryFriendProp() { }

			public QueryFriendProp(SBean.QueryFriendPropRes res)
			{
				this.res = res;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTQueryFriendProp;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( res == null )
					res = new SBean.QueryFriendPropRes();
				is.pop(res);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(res);
			}

			public SBean.QueryFriendPropRes getRes()
			{
				return res;
			}

			public void setRes(SBean.QueryFriendPropRes res)
			{
				this.res = res;
			}

			private SBean.QueryFriendPropRes res;
		}

		public static class UpdateFriendProp extends SimplePacket
		{
			public UpdateFriendProp() { }

			public UpdateFriendProp(SBean.UpdateFriendPropReq req)
			{
				this.req = req;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTUpdateFriendProp;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( req == null )
					req = new SBean.UpdateFriendPropReq();
				is.pop(req);
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(req);
			}

			public SBean.UpdateFriendPropReq getReq()
			{
				return req;
			}

			public void setReq(SBean.UpdateFriendPropReq req)
			{
				this.req = req;
			}

			private SBean.UpdateFriendPropReq req;
		}

		public static class SendMessageToFriend extends SimplePacket
		{
			public SendMessageToFriend() { }

			public SendMessageToFriend(SBean.GlobalRoleID srcID, SBean.GlobalRoleID dstID, byte msgType, short arg1, 
			                           int arg2)
			{
				this.srcID = srcID;
				this.dstID = dstID;
				this.msgType = msgType;
				this.arg1 = arg1;
				this.arg2 = arg2;
			}

			@Override
			public int getType()
			{
				return Packet.eS2FPKTSendMessageToFriend;
			}

			@Override
			public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
			{
				if( srcID == null )
					srcID = new SBean.GlobalRoleID();
				is.pop(srcID);
				if( dstID == null )
					dstID = new SBean.GlobalRoleID();
				is.pop(dstID);
				msgType = is.popByte();
				arg1 = is.popShort();
				arg2 = is.popInteger();
			}

			@Override
			public void encode(Stream.AOStream os)
			{
				os.push(srcID);
				os.push(dstID);
				os.pushByte(msgType);
				os.pushShort(arg1);
				os.pushInteger(arg2);
			}

			public SBean.GlobalRoleID getSrcID()
			{
				return srcID;
			}

			public void setSrcID(SBean.GlobalRoleID srcID)
			{
				this.srcID = srcID;
			}

			public SBean.GlobalRoleID getDstID()
			{
				return dstID;
			}

			public void setDstID(SBean.GlobalRoleID dstID)
			{
				this.dstID = dstID;
			}

			public byte getMsgType()
			{
				return msgType;
			}

			public void setMsgType(byte msgType)
			{
				this.msgType = msgType;
			}

			public short getArg1()
			{
				return arg1;
			}

			public void setArg1(short arg1)
			{
				this.arg1 = arg1;
			}

			public int getArg2()
			{
				return arg2;
			}

			public void setArg2(int arg2)
			{
				this.arg2 = arg2;
			}

			private SBean.GlobalRoleID srcID;
			private SBean.GlobalRoleID dstID;
			private byte msgType;
			private short arg1;
			private int arg2;
		}

	}

}

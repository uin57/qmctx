
package ket.kpc.common;

import ket.kio.NetAddress;
import ket.kpc.SBean;
import ket.util.Stream;

public class PeerID implements Stream.IStreamable, Cloneable
{
	public static final int PF_NULL =  0;
	public static final int PF_TTC  =  1;  // tcp tracker client is connected
	public static final int PF_TPS  =  2;  // tcp peer server is open
	public static final int PF_UPS  =  4;  // udp peer server is open
	public static final int PF_SUPER = ( 1 << 31 ); // super peer
	public static final int PF_LOCAL = ( 1 << 30 ); // local peer
	
	public PeerID()
	{
		kuid = 0;
	}
	
	public PeerID(long kuid)
	{
		this.kuid = kuid;
		flag = PF_NULL;		
	}
	
	public boolean equalsID(PeerID peeridOther)
	{
		return peeridOther != null && kuid == peeridOther.kuid;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		PeerID newpeerid = (PeerID)super.clone();
		return newpeerid;
	}
	
	public String getFlagString()
	{
		if( isNull() )
			return "Null";
		String r = "";
		r += isSuper() ? "S" : " ";
		r += isTPS() ? "T" : " ";
		r += isUPS() ? "U" : " ";
		r += "C";
		return r;
	}
	
	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		kuid = is.popLong();
		flag = is.popInteger();
		is.pop(addr);
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushLong(kuid);
		os.pushInteger(flag);
		os.push(addr);
	}
	
	public boolean testFlag(int flag)
	{
		return (this.flag & flag) != 0;
	}
	
	public boolean isTTC()
	{
		return testFlag(PF_TTC);
	}
	
	public boolean isTPS()
	{
		return testFlag(PF_TPS);
	}
	
	public boolean isUPS()
	{
		return testFlag(PF_UPS);
	}
	
	public boolean isSuper()
	{
		return testFlag(PF_SUPER);
	}
	
	public boolean isLocal()
	{
		return testFlag(PF_LOCAL);
	}
	
	public PeerID setFlag(int flag)
	{
		this.flag |= flag;
		return this;
	}
	
	public PeerID clearFlag(int flag)
	{
		this.flag &= (~flag);
		return this;
	}
	
	public void setNull()
	{
		flag = PF_NULL;
	}
	
	public boolean isNull()
	{
		return flag == PF_NULL;
	}
	
	public long getLocalKey()
	{
		if( isLocal() )
			return kuid | 0x8000000000000000L;
		return kuid;
	}
	
	@Override
	public String toString()
	{
		if( isLocal() )
			return "[L(" + (int)((kuid>>>32)&0xffff) + ")" + addr.host + ":" + addr.port + "]";
		return "[P(" + (int)((kuid>>>32)&0xffff) + ")" + addr.host + ":" + addr.port + "]";
	}

	public long kuid;
	public volatile int flag;
	public volatile NetAddress addr = new NetAddress();
}

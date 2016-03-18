
package i3k.gs;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;
import tsssdk.jni.TssSdk;
import tsssdk.jni.TssSdkAntiAddUserInfoEx2;
import tsssdk.jni.TssSdkAntiDelUserInfoEx2;
import tsssdk.jni.TssSdkAntiRecvDataInfoEx2;
import tsssdk.jni.TssSdkAntiSendDataInfoEx2;
import tsssdk.jni.TssSdkInitInfo;


public class TSSAntiManager
{
	class MyTssSdk extends TssSdk
	{

		@Override
	    public int AntiSendDataToClientEx2(TssSdkAntiSendDataInfoEx2 send_pkg_info)
	    {
			if( send_pkg_info != null )
				queue.add(send_pkg_info);
			return 0;
	    }
		
	}
	
	public TSSAntiManager(GameServer gs)
	{
		this.gs = gs;
	}
	
	public boolean isRunning()
	{
		return bRunning;
	}
	
	public boolean init()
	{
		if( bRunning || gs.getConfig().tssAntiState == 0 )
			return false;
		TssSdkInitInfo initInfo = new TssSdkInitInfo();
		initInfo.unique_instance_id = gs.getConfig().tssAntiUID;
		initInfo.tss_sdk_conf = gs.getConfig().tssAntiCfgFileName;
		
		try
		{
			sdk = new MyTssSdk();
			int ret = sdk.Init(initInfo);
			if( ret != 0 )
			{
				gs.getLogger().warn("tss_sdk init failed, ret: " + ret);
				return false;
			}

			gs.getLogger().info("tss_sdk init succ");

			ret = sdk.AntiInterfInitEx2();
			if( ret != 0 )
			{
				gs.getLogger().warn("anti interf init failed, ret: " + ret);
				return false;
			}
		}
		catch(Throwable t)
		{
			gs.getLogger().warn(t.getMessage(), t);
			return false;
		}

        gs.getLogger().info("anti intef init succ");
        bRunning = true;
        
        return bRunning;
	}
	
	public void onRecvAntiData(Role role, ByteBuffer data)
	{
		if( ! bRunning || sdk == null )
			return;
		TssSdkAntiRecvDataInfoEx2 recv_pkg_info = new TssSdkAntiRecvDataInfoEx2();

        recv_pkg_info.openid = role.msdkInfo.openID;
        recv_pkg_info.platid = gs.getConfig().platID;
        recv_pkg_info.anti_data = data.array();
        recv_pkg_info.user_ext_data = Stream.encodeIntegerLE(role.id);

        int ret = 0;
        synchronized( sdk )
        {
        	ret = sdk.AntiRecvDataFromClientEx2(recv_pkg_info);
        }
        if(ret != 0)
        {
            gs.getLogger().warn("anti recv data from client failed, ret: " + ret + ", rid=" + role.id);
        }
	}
	
	public void addUser(Role role)
	{
		if( ! bRunning || sdk == null )
			return;
		
		TssSdkAntiAddUserInfoEx2 add_user_info = new TssSdkAntiAddUserInfoEx2();

        add_user_info.openid = role.msdkInfo.openID;
        add_user_info.platid = gs.getConfig().platID;
        add_user_info.client_ver = role.msdkInfo.clientVerInt;
        add_user_info.client_ip = role.msdkInfo.ipInt;
        add_user_info.role_name = role.name;
        add_user_info.user_ext_data = Stream.encodeIntegerLE(role.id);

        int ret = 0;
        synchronized( sdk )
        {
        	ret = sdk.AntiAddUserEx2(add_user_info);
        }
        if( ret != 0 )
        {
            gs.getLogger().warn("anti add user failed, ret: " + ret + ", id= " + role.id);
        }
	}
	
	public void delUser(Role role)
	{
		if( ! bRunning || sdk == null )
			return;
		TssSdkAntiDelUserInfoEx2 del_user_info = new TssSdkAntiDelUserInfoEx2();

        del_user_info.openid = role.msdkInfo.openID;
        del_user_info.platid = gs.getConfig().platID;
        del_user_info.user_ext_data = Stream.encodeIntegerLE(role.id);

        int ret = 0;
        synchronized( sdk )
        {
        	ret = sdk.AntiDelUserEx2(del_user_info);
        }
        if( ret != 0 )
        {
        	gs.getLogger().warn("anti del user failed, ret: " + ret + ", id= " + role.id);
        }
	}
	
	public void destroy()
	{
		if( sdk != null )
		{
			sdk.UnLoad();
			sdk = null;
		}
		bRunning = false;
	}
	
	// √ø√Î100¥Œ?
	public void onTimer()
	{
		if( bRunning && sdk != null )
		{
			List<TssSdkAntiSendDataInfoEx2> lst = null;
			synchronized( sdk )
			{
				sdk.Proc();
				if( ! queue.isEmpty() )
				{
					lst = queue;
					queue = new ArrayList<TssSdkAntiSendDataInfoEx2>();
				}
			}
			if( lst == null || lst.isEmpty() )
				return;
			try 
			{
				for(TssSdkAntiSendDataInfoEx2 send_pkg_info : lst)
				{
					int rid = Stream.decodeIntegerLE(send_pkg_info.user_ext_data);
					int sid = gs.getLoginManager().getLoginRoleSessionID(rid);
					if( sid > 0 )
						gs.getRPCManager().sendAntiData(sid, send_pkg_info.anti_data);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private final GameServer gs;
	private MyTssSdk sdk;
	//private ConcurrentLinkedQueue<TssSdkAntiSendDataInfoEx2> queue = new ConcurrentLinkedQueue<TssSdkAntiSendDataInfoEx2>();
	private List<TssSdkAntiSendDataInfoEx2> queue = new ArrayList<TssSdkAntiSendDataInfoEx2>();
	private volatile boolean bRunning = false;
}

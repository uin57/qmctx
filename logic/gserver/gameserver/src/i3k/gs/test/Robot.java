
package i3k.gs.test;

import i3k.SBean;
import i3k.DBRoleGeneral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robot extends TCPGameClient
{
	
	public static final int STATE_NULL = -1;
	public static final int STATE_COMBAT_START = 0;
	public static final int STATE_COMBAT = 1;
	public static final int STATE_COMBAT_FINISH = 2;
	
	public static final int TASK_NULL = 0;
	public static final int TASK_COMBAT = 1;
	public static final int TASK_COUNT = 2;
	
	public static final int ACTION_COOL_BASE = 10;
	public static final int ACTION_COOL_RANDOM = 5;
	
	public Robot(RPCManagerClient managerRPC, GameClient gc)
	{
		super(managerRPC);
		this.gc = gc;
	}
	
	public int selectTask(Random random)
	{
		if( gc.getConfig().bTestCombat == 1 )
			return TASK_COMBAT;
		return TASK_NULL;
	}
	
	public void setIDSeed(int id)
	{
		idSeed = id;
	}
	
	public void nextName()
	{
		accName = "aqq_" + getOpenID();
		/*
		final int MAX_SEQ = 6;
		seq++;
		if( seq > MAX_SEQ )
			seq = 1;
		//accName = "r_" + new Integer(idSeed).toString() + "_" + new Integer(seq).toString();
		accName = "B624064BA065E01CB73F83a" + new Integer(idSeed+100000000).toString();
		*/
	}
	
	public String getRoleName()
	{
		return "R_" + new Integer(idSeed).toString();
	}
	
	public String getOpenID()
	{
		return "B624064BA065E01CB73F83a" + new Integer(idSeed+100000000).toString();
	}

	public int idSeed;
	public int seq = 0;
	public String accName;
	public int logoutTime;
	
	public volatile boolean bReconnect = false;
	
	public int rid;
	public boolean bLogin = false;
	public int state = STATE_NULL;
	public int lastActTime = 0;
	
	public List<DBRoleGeneral> generals = new ArrayList<DBRoleGeneral>();
	public SBean.DBCombatPos combatPos = new SBean.DBCombatPos();
	private GameClient gc;
}

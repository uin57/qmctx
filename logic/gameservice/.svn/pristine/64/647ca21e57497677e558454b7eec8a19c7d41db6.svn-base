package com.joypiegame.gameservice.webservice.server;

import javax.jws.WebService;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.joypiegame.gameservice.webservice.client.GameWebService;
import com.joypiegame.gameservice.conf.GSConf;
import com.joypiegame.gameservice.db.DBExecutor;
import com.joypiegame.gameservice.db.DBServers;
import com.joypiegame.gameservice.webservice.service.GWService;

@WebService(endpointInterface = "com.joypiegame.gameservice.webservice.service.GWService")
public class GWServiceImpl implements GWService {
	private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
    public String exchangeItem(int gsid, int roleid, String rolename, String cdkey, String channel) {
    	String result = "-1|";
//    	if (GameWebService.checkCDKeyValid(cdkey)) {
//    		java.sql.Connection con = DBExecutor.getDbCdkeyConnection();
//            if (con != null) {
//            	java.sql.CallableStatement cstm = DBExecutor.createPrepareCall(con, "{call exchangecdkey(?, ?, ?, ?, ?, ?, ?)}");
//                if (cstm != null) {
//                  	try {
//                   		cstm.setString(1, cdkey.toUpperCase());
//                   		cstm.setString(2, channel);
//                   		cstm.setInt(3, gsid);
//                   		cstm.setInt(4, roleid);
//                   		cstm.setString(5, rolename);
//                   		cstm.registerOutParameter(6, java.sql.Types.INTEGER);
//                   		cstm.registerOutParameter(7, java.sql.Types.VARCHAR);
//                   		if (DBExecutor.executeUpdate(cstm) >= 0) {
//                   			int error = cstm.getInt(6);
//                   			String gift = cstm.getString(7);
//                   			result = error + "|" + gift;
//                   		}
//                   	}catch (Exception e) {
//                   		System.out.println("Caught " + e);
//                           e.printStackTrace();
//                   	}
//                  	DBExecutor.closeCallableStatement(cstm);
//                }
//                DBExecutor.closeConnection(con);
//            }	
//    	}
//        System.out.println(String.format("exchangeItem gsid=%d roleid=%d rolename=%s cdkey=%s channel=%s result=(%s)", gsid, roleid, rolename, cdkey, channel, result));
        return result;
    }
	
	@Override
	public int reportOnlineInfo(int time, int areaId, int platId, int gsid, String gameAppId, int userCount)
	{
		int result = -1;
		if (GSConf.getWriteOnlineDataConf()) {
			DBExecutor dbExecutor = DBServers.getGameInfoDBExecutor();
			if (dbExecutor != null) {
				java.sql.Connection con = dbExecutor.getDbConnection();
			    if (con != null) {
			    	String sql = String.format("INSERT INTO OnlineUsers(CheckTime, AreaID, PlatID, GameSvrID, GameAppID, UserCount) VALUES(FROM_UNIXTIME(%d), %d, %d, %d, '%s', %d);",
			    							  time, areaId, platId, gsid, gameAppId, userCount);
			    	result = DBExecutor.executeUpdate(con, sql);
//			    	java.sql.CallableStatement cstm = DBExecutor.createPrepareCall(con, "{call AddOnlineInfo(?, ?, ?, ?, ?, ?)}");
//			        if (cstm != null) {
//			          	try {
//			          		int seq = 1;
//			           		cstm.setInt(seq++, time);
//			           		cstm.setInt(seq++, areaId);
//			           		cstm.setInt(seq++, platId);
//			           		cstm.setInt(seq++, gsid);
//			           		cstm.setString(seq++, gameAppId);
//			           		cstm.setInt(seq++, userCount);
//			           		result = DBExecutor.executeUpdate(cstm);
//			           	}catch (Exception e) {
//			           		System.out.println("Caught " + e);
//			                   e.printStackTrace();
//			           	}
//			          	DBExecutor.closeCallableStatement(cstm);
//			        }
			        DBExecutor.closeConnection(con);
			    }
			}
		} else {
			result = -99;
		}
	    System.out.println(String.format("report online info time=%s areaId=%d platId=%d gsid=%d gameAppId=%s userCount=%d result=%d", dataFormat.format(new Date(time*1000l)), areaId, platId, gsid, gameAppId, userCount, result));
		return result;
	}
	
	@Override
	public int reportRegisterInfo(int time, int areaId, int platId, int gsid, int registerCount)
	{
		int result = -1;
		if (GSConf.getWriteOnlineDataConf()) {
			DBExecutor dbExecutor = DBServers.getGameInfoDBExecutor();
			if (dbExecutor != null) {
				java.sql.Connection con = dbExecutor.getDbConnection();
			    if (con != null) {
			    	String sql = String.format("INSERT INTO RegisterUsers(CheckTime, AreaID, PlatID, GameSvrID, RegisterCount) VALUES(FROM_UNIXTIME(%d), %d, %d, %d, %d);",
							  				   time, areaId, platId, gsid, registerCount);
			    	result = DBExecutor.executeUpdate(con, sql);
//			    	java.sql.CallableStatement cstm = DBExecutor.createPrepareCall(con, "{call AddRegisterInfo(?, ?, ?, ?, ?)}");
//			        if (cstm != null) {
//			          	try {
//			          		int seq = 1;
//			           		cstm.setInt(seq++, time);
//			           		cstm.setInt(seq++, areaId);
//			           		cstm.setInt(seq++, gsid);
//			           		cstm.setInt(seq++, platId);
//			           		cstm.setInt(seq++, registerCount);
//			           		result = DBExecutor.executeUpdate(cstm);
//			           	}catch (Exception e) {
//			           		System.out.println("Caught " + e);
//			                   e.printStackTrace();
//			           	}
//			          	DBExecutor.closeCallableStatement(cstm);
//			        }
			        DBExecutor.closeConnection(con);
			    }
			}
		} else {
			result = -99;
		}
	    System.out.println(String.format("report register info time=%s areaId=%d platId=%d gsid=%d registerCount=%d result=%d", dataFormat.format(new Date(time*1000l)), areaId, platId, gsid, registerCount, result));
		return result;
	}
}







package com.joypiegame.gameservice.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * ExchangeCDKey web service interface
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface GWService {

	/**
	 * use cdkey exchange game item id
	 * @param gsid game server id
	 * @param roleid role id
	 * @param rolename role name
	 * @param cdkey cdkey code
	 * @param channel channel name
	 * @return  0|0,404,1 : success   -1| :failed
	 */
    @WebMethod
    String exchangeItem(int gsid, int roleid, String rolename, String cdkey, String channel);
    
    
    /**
	 * report game server online users
	 * @param time unix time stamp
	 * @param areaId area id
	 * @param platId plat id
	 * @param gsid game server id
	 * @param gameAppId game app id
	 * @param userCount online user count
	 * @return  0 : success 
	 */
    @WebMethod
    int reportOnlineInfo(int time, int areaId, int platId, int gsid, String gameAppId, int userCount);
    
    /**
	 * report game server register users
	 * @param time unix time stamp
	 * @param areaId area id
	 * @param platId plat id
	 * @param gsid game server id
	 * @param registerCount register user count
	 * @param other info type
	 * @param info
	 * @return  0 : success 
	 */
    @WebMethod
    int reportRegisterInfo(int time, int areaId, int platId, int gsid, int registerCount);

}



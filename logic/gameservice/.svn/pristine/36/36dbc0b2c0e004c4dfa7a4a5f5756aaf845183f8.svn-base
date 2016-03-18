package com.joypiegame.gameservice.webservice.client;


import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import javax.xml.ws.WebServiceException;

import com.joypiegame.gameservice.webservice.service.GWService;
import com.joypiegame.gameservice.webservice.service.cdkey.CDKeyGen;


/**
 * for Web Service Client use
 *
 */
public class GameWebService {
   
	public GameWebService() {
		
	}
    /**
     * get game web service port GService
     * @param wsdlUrl as http://localhost:8080/gs/gws?wsdl
     * @param targetNS as http://server.webservice.gameservice.joypiegame.com/[指定的targetNamespace命名空间URI]
     * @param serviceName as GWServiceImplService[实现类名称+Service]
     * @return interface GService, not null success
     */
    public GWService getGameService(String wsdlUrl, String targetNS, String serviceName) throws MalformedURLException, WebServiceException{
    	URL url = new URL(wsdlUrl);
		QName qname = new QName(targetNS, serviceName);
		Service service = Service.create(url, qname); 
		return service.getPort(GWService.class);
    }
    
    /**
     * get game web service port GService
     * @param host : web service ip
     * @param port : web service port
     * @return interface GService, not null success
     */
    public GWService getGameService(String host, int port) throws MalformedURLException, WebServiceException {
    	String wsdlUrl = String.format("http://%s:%d/gs/gws?wsdl", host, port);
		String targetNS = "http://server.webservice.gameservice.joypiegame.com/";
		String serviceName = "GWServiceImplService";
		return getGameService(wsdlUrl, targetNS, serviceName);
    }
    
    
    public static boolean checkCDKeyValid(String cdkey)
    {
    	return CDKeyGen.isValid(cdkey);
    }
    
    public static void main(String[] args)
    {
    	GameWebService gws = new GameWebService();
    	//GService gservice = gws.getGameService("http://localhost:8080/gs/gws?wsdl", "http://server.gameservice.joypiegame.com/", "GWServiceImplService");
    	try {
    		GWService gservice = gws.getGameService("localhost", 8080);
    		Thread.sleep(1500);
    		//String result = gservice.exchangeItem(-1, -1, "xxx", "4235432", "dummy");
    		int result = gservice.reportOnlineInfo((int)(new java.util.Date().getTime()/1000), 1, 1, 1001, "aaa", 1001);
    		System.out.println(result);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
}



package com.joypiegame.gameservice.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;




public class GSServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("web app root : " + sce.getServletContext().getRealPath("/"));
		System.out.println("GSServletContextListener initialize start");
		GSWebApp.instance(sce.getServletContext());
		System.out.println("GSServletContextListener initialize end");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("GSServletContextListener destory start");
		GSWebApp.destory(sce.getServletContext());
		System.out.println("GSServletContextListener destory end");
	}
	

}



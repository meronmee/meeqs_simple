package com.meronmee.core.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.meronmee.core.common.util.SpringContextUtils;

/**
 * 系统初始化监听器
 * @author meron
 *
 */
@WebListener
public class SystemInitContextListener implements ServletContextListener {	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Autowired
	private SpringContextUtils context;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//...
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {		
	}
  
}
package com.meronmee.pattern.struct.proxy;

import com.meronmee.app.Log;

/**
 * 代理对象
 *
 */
public class ProxyConnector implements Connector {
	private HttpConnector connector = new HttpConnector();
	
	public void connect() {
		//前置处理
		Log.info("before...");
		connector.connect();
		//后置处理
		Log.info("after...");		
	}

}

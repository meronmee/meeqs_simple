package com.meronmee.pattern.struct.proxy;

import com.meronmee.app.Log;
/**
 * 被代理对象
 *
 */
public class HttpConnector implements Connector {

	public void connect() {
		Log.info("HttpConnector connect...");
	}

}

package com.meronmee.common.utils;

import org.apache.commons.lang3.StringUtils;

public class BaseUtil {
	public static void isNotBlank(String arg, String msg){
		if(StringUtils.isBlank(arg)){
			BaseUtil.error(msg);
		}
	}
	
	public static void error(String msg) throws IllegalArgumentException {
		throw new IllegalArgumentException(msg);
	}
}

package com.meronmee.test;

import java.util.Map;
import java.util.Map.Entry;

import com.meronmee.core.common.util.Cache;
import com.meronmee.core.common.util.ShortCache;
 

/**
 * Java 代码测试工具类
 * @author Meron
 *
 */
public class Test {

	public static void main(String[] args) {
		Log.info("Cache:"+Cache.get("k1"));
		Log.info("ShortCache:"+ShortCache.get("k1"));
		
	}
	
	public static void run(Map<String, Object> map){
		for(Entry<String, Object> entry : map.entrySet()){
			Log.info(entry.getKey()+"="+entry.getValue());
		}
	}
}

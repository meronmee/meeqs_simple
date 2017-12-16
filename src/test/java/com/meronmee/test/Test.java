package com.meronmee.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.meronmee.core.utils.DateUtils;

/**
 * Java 代码测试工具类
 * @author Meron
 *
 */
public class Test {

	public static void main(String[] args) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("11qqqqqq", 11);	
		map.put("cc", "cc");	
		map.put("22阿发", 22);	
		map.put("bb", "bb");	
		map.put("33", 33);	
		map.put("aa阿萨德飞洒", "aa");	
		run(map);		
	}
	
	public static void run(Map<String, Object> map){
		for(Entry<String, Object> entry : map.entrySet()){
			Log.info(entry.getKey()+"="+entry.getValue());
		}
	}
}

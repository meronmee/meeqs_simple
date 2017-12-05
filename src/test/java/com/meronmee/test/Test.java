package com.meronmee.test;

import java.util.Calendar;
import java.util.Date;

import com.meronmee.core.utils.DateUtils;

/**
 * Java 代码测试工具类
 * @author Meron
 *
 */
public class Test {

	public static void main(String[] args) {
		run();		
	}
	
	public static void run(){
		Log.info(DateUtils.getInteger(new Date(), Calendar.MONTH));
	}
}

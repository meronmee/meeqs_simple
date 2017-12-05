package com.meronmee.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.meronmee.core.exception.BizException;

/**
 * 参数校验工具类
 * @author Meron
 *
 */
public class Assert {
	
	/**
	 * @param arg
	 * @param message
	 * @throws BizException arg 为空或者为 {@code "null"}
	 * @see StringUtils#isBlank
	 * @see BaseUtils#isBlank
	 */
	public static void isNotBlank(String arg, String message) {
		if(BaseUtils.isBlank(arg)){
			error(message);
		}
	}
	
	/**
	 * @param arg
	 * @param message
	 * @throws BizException arg 为 {@code null} 或者为 {@code 0}
	 */
	public static void isNotNull0(Integer arg, String message) {
		if(arg == null || (arg.compareTo(0) == 0)){
			error(message);
		}
	}
	/**
	 * @param arg
	 * @param message
	 * @throws BizException arg 为 {@code null} 或者为 {@code 0}
	 */
	public static void isNotNull0(Long arg, String message) {
		if(arg == null || (arg.compareTo(0L) == 0)){
			error(message);
		}
	}
	
	public static void isNotNull(Object arg, String message) {
		if (arg == null) {
			error(message);
		}
	}
	
	/**
	 * @param array
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 不在数组 array 中
	 * @see StringUtils#isBlank
	 * @see BaseUtils#isBlank
	 * @see BaseUtils#isInArray
	 */
	public static void isInArray(String[] array, String arg, String message) {
		if(!BaseUtils.isInArray(array, arg)){
			error(message);
		}		
	} 
	
	/**
	 * @param array
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 不在数组 array 中
	 * @see StringUtils#isBlank
	 * @see BaseUtils#isBlank
	 * @see BaseUtils#isInArray
	 */
	public static void isInArray(Integer[] array, Integer arg, String message) {
		if(!BaseUtils.isInArray(array, arg)){
			error(message);
		}		
	}
	/**
	 * @param list
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 不在逗号分割的列表 list 中
	 */
	public static void isInList(String list, String arg, String message) {
		if(!BaseUtils.isInList(list, arg)){
			error(message);
		}		
	}

	/**
	 * @param list
	 * @param arg 
	 * @param message
	 * 
	 * @throws BizException arg 不在逗号分割的列表 list 中
	 */
	public static void isInList(String list, Integer arg, String message) {
		if(!BaseUtils.isInList(list, arg)){
			error(message);
		}		
	}
	/**
	 * @param array
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 在数组 array 中
	 * @see StringUtils#isBlank
	 * @see BaseUtils#isBlank
	 * @see BaseUtils#isInArray
	 */
	public static void isNotInArray(String[] array, String arg, String message) {
		if(BaseUtils.isInArray(array, arg)){
			error(message);
		}		
	}

	/**
	 * @param array
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 在数组 array 中
	 * @see StringUtils#isBlank
	 * @see BaseUtils#isBlank
	 * @see BaseUtils#isInArray
	 */
	public static void isNotInArray(Integer[] array, Integer arg, String message) {
		if(BaseUtils.isInArray(array, arg)){
			error(message);
		}		
	}

	/**
	 * @param list
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 在逗号分割的列表 list 中
	 */
	public static void isNotInList(String list, String arg, String message) {
		if(BaseUtils.isInList(list, arg)){
			error(message);
		}		
	}
	/**
	 * @param list
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 在逗号分割的列表 list 中
	 */
	public static void isNotInList(String list, Integer arg, String message) {
		if(BaseUtils.isInList(list, arg)){
			error(message);
		}		
	}
	
	/**
	 * @param arg
	 * @param maxLen
	 * @param message
	 * 
	 * @throws BizException arg 的字符个数大于 maxLen
	 */
	public static void maxLength(String arg, Integer maxLen, String message) {
		if(BaseUtils.isNull0(maxLen)){//为0视为没有限制
			return;
		}
		if(StringUtils.isNotBlank(arg)){
			arg = arg.trim();
			if(arg.length() > maxLen){
				error(message);
			}
		}		
	}
	
	/**
	 * @param arg
	 * @param maxLen
	 * @param message
	 * 
	 * @throws BizException arg 的长度(一个汉字2个字节长度)大于 maxLen
	 */
	public static void maxLen(String arg, Integer maxLen, String message) {
		if(BaseUtils.isNull0(maxLen)){//为0视为没有限制
			return;
		}
		if(StringUtils.isNotBlank(arg)){
			arg = arg.trim();
			if(BaseUtils.len(arg) > maxLen){
				error(message);
			}
		}		
	}
	/**
	 * @param arg
	 * @param minLen
	 * @param message
	 * 
	 * @throws BizException arg 的字符个数小于 minLen
	 */
	public static void minLength(String arg, Integer minLen, String message) {
		if(BaseUtils.isNull0(minLen)){//为0视为没有限制
			return;
		}
		if(StringUtils.isBlank(arg)){
			error(message);
		} else {
			arg = arg.trim();
			if(arg.length() < minLen){
				error(message);
			}
		}		
	}
	
	/**
	 * @param arg
	 * @param minLen
	 * @param message
	 * 
	 * @throws BizException arg 的长度(一个汉字2个字节长度)小于 minLen
	 */
	public static void minLen(String arg, Integer minLen, String message) {
		if(BaseUtils.isNull0(minLen)){//为0视为没有限制
			return;
		}
		if(StringUtils.isBlank(arg)){
			error(message);
		} else {
			arg = arg.trim();
			if(BaseUtils.len(arg) < minLen){
				error(message);
			}
		}		
	}

	/**
	 * @param arg
	 * @param minLen
	 * @param maxLen
	 * @param message
	 * 
	 * @throws BizException arg 的字符个数小于 minLen 或大于 maxLen
	 */
	public static void rangeLength(String arg, Integer minLen, Integer maxLen, String message) {
		if(BaseUtils.isNull0(minLen)){//为0视为没有限制
			maxLength(arg, maxLen, message);
			return;
		}
		if(BaseUtils.isNull0(maxLen)){//为0视为没有限制
			minLength(arg, minLen, message);
			return;
		}
		if(StringUtils.isBlank(arg)){
			error(message);
		} else {
			arg = arg.trim();
			if(arg.length() < minLen || arg.length() > maxLen){
				error(message);
			}
		}		
	}
	/**
	 * @param arg
	 * @param minLen
	 * @param maxLen
	 * @param message
	 * 
	 * @throws BizException arg 的长度(一个汉字2个字节长度)小于 minLen 或大于 maxLen
	 */
	public static void rangeLen(String arg, Integer minLen, Integer maxLen, String message) {
		if(BaseUtils.isNull0(minLen)){//为0视为没有限制
			maxLength(arg, maxLen, message);
			return;
		}
		if(BaseUtils.isNull0(maxLen)){//为0视为没有限制
			minLength(arg, minLen, message);
			return;
		}
		if(StringUtils.isBlank(arg)){
			error(message);
		} else {
			arg = arg.trim();
			if(BaseUtils.len(arg) < minLen || BaseUtils.len(arg) > maxLen){
				error(message);
			}
		}
	}

	/**
	   * @param arg
	   * @param max
	   * @param message
	   * 
	   * @throws BizException arg 大于 max
	   */
	  public static void max(Integer arg, Integer max, String message) {
	    if(arg==null || max==null){//为null视为没有限制
	      return;
	    }
	    if(arg.compareTo(max) > 0){
	        error(message);
	    }   
	  }
	  /**
	   * @param arg
	   * @param min
	   * @param message
	   * 
	   * @throws BizException arg 小于 min
	   */
	  public static void min(Integer arg, Integer min, String message) {
		  if(arg==null || min==null){//为null视为没有限制
			  return;
		  }
		  if(arg.compareTo(min) < 0){
	        error(message);
		  }   
	  }
	  
	/**
	 * @param arg
	 * @param min
	 * @param max
	 * @param message
	 * 
	 * @throws BizException arg 小于 min 或大于 max
	 */
	public static void range(Integer arg, Integer min, Integer max, String message) {
		if(min == null){//为0视为没有限制
			max(arg, max, message);
			return;
		}
		if(max == null){//为0视为没有限制
			min(arg, min, message);
			return;
		}
		if(arg == null){
			return;
		} 
			
		if(arg.compareTo(min) < 0 || arg.compareTo(max) > 0 ){
			error(message);
		}		
	}
	
	/**
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 不是11位的电话号码
	 */
	public static void phone11(String arg,  String message) {
		if(StringUtils.isBlank(arg)){
			error(message);
		}
		if(!arg.matches("^1[3-9]\\d{9}$")){
			error(message);
		}
	}

	/**
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 不是合法的email格式
	 */
	public static void isEmail(String arg, String message) {
		if(!BaseUtils.isEmail(arg)){
			error(message);
		}
	}
	/**
	 * @param arg
	 * @param pattern 正则表达式
	 * @param message
	 * 
	 * @throws BizException arg 不匹配 pattern 格式
	 */
	public static void pattern(String arg, String pattern, String message) {
		if(StringUtils.isBlank(arg)){
			error(message);
		}
		if(!arg.matches(pattern)){
			error(message);
		}
	}

	/**
	 * @param arg
	 * @param message
	 * 
	 * @throws BizException arg 包含了特殊字符
	 */
	public static void noSpecialChar(String arg, String message) {
		if(StringUtils.containsAny(arg, ",./?<>;':\"[]{}|\\!`~@#$%^&*()")){
			error(message);
		}
	}
	
	/**
	 * @param arg
	 * @param format
	 * @param message
	 * 
	 * @throws BizException arg 不是 format格式
	 */
	public static void widthDateFormat(String arg, String format, String message) {
		if(arg.length() != format.length()){
			error(message);
		}
		String str = DateUtils.dateToString(DateUtils.stringToDate(arg, format), format);
		if(!arg.equals(str)){
			error(message);
		}		
	}
	
	/**
	 * @param arg
	 * @param beginTime
	 * @param message
	 * 
	 * @throws BizException arg 早于 beginTime
	 */
	public static void affter(Date arg, Date beginTime,String message) {
		if(arg == null || beginTime == null){
			return;
		}		
		if(arg.before(beginTime)){
			error(message);
		}		
	}
	/**
	 * @param arg
	 * @param beginTime yyyy-MM-dd HH:mm:ss 格式的时间字符串
	 * @param message
	 * 
	 * @throws BizException arg 早于 beginTime
	 */
	public static void affter(Date arg, String beginTime, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = null;
		try{
			begin = sdf.parse(beginTime);
		} catch(Exception e){
			return;
		}
		affter(arg, begin, message);
	}

	/**
	 * @param arg
	 * @param endTime
	 * @param message
	 * 
	 * @throws BizException arg 晚于 endTime
	 */
	public static void before(Date arg, Date endTime, String message) {
		if(arg == null || endTime == null){
			return;
		}		
		if(arg.after(endTime)){
			error(message);
		}		
	}
	
	/**
	 * @param arg
	 * @param endTime yyyy-MM-dd HH:mm:ss 格式的时间字符串
	 * @param message
	 * 
	 * @throws BizException arg 晚于 endTime
	 */
	public static void before(Date arg, String endTime, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = null;
		try{
			end = sdf.parse(endTime);
		} catch(Exception e){
			return;
		}
		before(arg, end, message);
	}
	
	/**
	 * @param arg
	 * @param beginTime
	 * @param endTime
	 * @param message
	 * 
	 * @throws BizException arg 晚于 endTime 或早于 beginTime
	 */
	public static void between(Date arg, Date beginTime, Date endTime, String message) {
		if(arg == null || endTime == null || endTime == null){
			return;
		}		
		if(arg.after(endTime) || arg.before(beginTime)){
			error(message);
		}		
	}
	/**
	 * 不能含有Emoji等4字节字符
	 * @param arg
	 * @param message
	 * @throws BizException arg 晚于 endTime 或早于 beginTime
	 */
	public static void hasNoEmoji(String arg, String message) {
		if(StringUtils.isNotBlank(arg)){
			String str = BaseUtils.trans4bytesChar(arg, "");
			if(str.length() != arg.length()){
				error(message);
			}
		}
	}
	
	//---------------------------------------	
	
	/**
	 * 抛出一个BizException(message)
	 * @param message
	 * @throws BizException
	 */
	public static void error(String msg) {
		throw new BizException(msg);
	}
}

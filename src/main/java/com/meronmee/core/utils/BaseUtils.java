package com.meronmee.core.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 常用工具类
 * @author Meron
 *
 */
public final class BaseUtils {	
	public static String UTF8 = "UTF8";
	/**
	 * 获取文件扩展名(不含点号)
	 * @param fileName
	 * @return 如：jpg
	 */
	public static String getFileExt(String fileName){
		return getFileExt(fileName, "");
	}
	/**
	 * 获取文件扩展名(不含点号)
	 * @param fileName
	 * @param defaultExt 群不到是的默认扩展名
	 * @return 如：jpg
	 */
	public static String getFileExt(String fileName, String defaultExt){
		String ext = fileName.replaceAll("^[^.]+$", "").replaceAll("^.*\\.", "").toLowerCase();		
		if(StringUtils.isBlank(ext)){
			ext = defaultExt;
		}
		return ext;
	}
	public static String getFilePath(String fileName){
		return fileName.replaceAll("^(.*[\\\\/]).*$", "$1");
	}
	public static String getFileName(String filepath){
		return filepath.replaceAll("^.*[\\\\/]", "");
	}
	public static String getFilePathWithOutExt(String fileName){
		if(fileName.lastIndexOf(".") == -1){
			return fileName;
		}
		return fileName.substring(0, fileName.lastIndexOf("."));
	}
	
	/**
	 * 取得参数列表中第一个不为空的值
	 * @param strings
	 */
	public static String getFirstNotBlank(Object ...strings){
		for(Object str : strings){
			if(str != null && isNotBlank(String.valueOf(str))){
				return String.valueOf(str);
			}
		}		
		return "";
	}
	/**
	 * 取得参数列表中第一个不为空的值
	 * @param strings
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T  getFirstNotNull(T ...objs){
		for(Object obj : objs){
			if(obj != null){				
				return (T)obj;
			}
		}		
		return null;
	}
	/**
	 * 如果in为null返回out,否则返回in
	 * @param in
	 * @param out
	 * @return
	 */
	public static <T> T ifNull(T in, T out){
		if(in==null){
			return out;
		}
		return in;
	}
	
	/**
	 * 取得参数列表中第一个不为空的值
	 * @param strings
	 * @return
	 */
	public static Integer getFirstNotNull0(Integer ...ids){
		for(Integer id : ids){
			if(isNotNull0(id)){
				return id;
			}
		}		
		return null;
	}
	
	/**
	  * 生成6位随机数字
	  */
	public static int random6(){
		Random random = new Random();  
		int x = random.nextInt(899999); //返回值范围为0到n - 1的分布 
		x = x + 100000; 
		return x;
	}
	/**
	  * 生成4位随机数字
	  */
	public static int random4(){
		Random random = new Random();  
		int x = random.nextInt(9000); //返回值范围为0到n - 1的分布 
		x = x + 1000; 
		return x;
	}
	/**
	  * 生成N位随机数字
	  */
	public static int randomN(int n){
		int max = new Double(Math.pow(10,n)).intValue() - 1;
		int min = new Double(Math.pow(10,n-1)).intValue();
		Random random = new Random();  
		int x = random.nextInt(max); //返回值范围为0到max - 1的分布 
		if(x < min){			
			x = x + min;
		}
		return x;
	}
	
	/**
	  * 生成范围内随机数字
	  */
	public static int randomAB(int min, int max){
		if(max > min){
			//Double d = Math.floor(Math.random()*(max-min)+min);
			//return d.intValue();
			Random random = new Random();
			int s = random.nextInt(max)%(max-min+1) + min;
			return s;
		}else{
			Double d = Math.floor(Math.random());
			return d.intValue();
		}
	}
		
	/**
	 * 格式化整型逗号分割的列表, 去除空格，多余的逗号，非数字<p>
	 *  如 ：",,   , 55 ,bb3, 3dd, 2d3e ,  11, a2,null,  , c ,33 , 4 4,  , ,," --> "55,11,33"
	 * 
	 * @param str
	 * @return
	 */
	public static String formatIntListStr(String str){
		return (","+str+",").replaceAll(",\\s+", ",").replaceAll("\\s+,", ",").replaceAll(",[^,]*[^0-9,]+[^,]*", "").replaceAll("^,+|,+$", "").replaceAll(",+", ",");
	}
	/**
	 * 分割逗号分割的整数列表
	 * @param str - 逗号分割的整数列表
	 * @return
	 */
	public static List<Integer> getIntList(String str){
		return getIntList(str, true);
	}
	/**
	 * 分割逗号分割的Integer列表
	 * @param str - 逗号分割的整数列表
	 * @param unique - 是否去重, true:去重, false:不去重
	 * @return
	 */
	public static List<Integer> getIntList(String str, boolean unique){
		List<Integer> list = new LinkedList<>();
		if(StringUtils.isNotBlank(str)){
			String[] arr = formatIntListStr(str).split(",");
			
			if(unique){
				for (int i = 0; i < arr.length; i++) {					
					Integer intVal = Integer.parseInt(arr[i]);
					if(!list.contains(intVal)){
						list.add(intVal);
					}
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					Integer intVal = Integer.parseInt(arr[i]);
					list.add(intVal);   
				}				
			}			
		}
		return list;
	}
	
	/**
	 * 分割逗号分割的Long列表
	 * @param str - 逗号分割的整数列表
	 * @return
	 */
	public static List<Long> getLongList(String str){
		return getLongList(str, true);
	}
	/**
	 * 分割逗号分割的Long列表
	 * @param str - 逗号分割的整数列表
	 * @param unique - 是否去重, true:去重, false:不去重
	 * @return
	 */
	public static List<Long> getLongList(String str, boolean unique){
		List<Long> list = new LinkedList<>();
		if(StringUtils.isNotBlank(str)){
			String[] arr = formatIntListStr(str).split(",");
			
			if(unique){
				for (int i = 0; i < arr.length; i++) {					
					Long val = Long.parseLong(arr[i]);
					if(!list.contains(val)){
						list.add(val);
					}
				}
			} else {
				for (int i = 0; i < arr.length; i++) {
					Long val = Long.parseLong(arr[i]);
					list.add(val);   
				}				
			}			
		}
		return list;
	}


	/**
	 * 获取不重复的字符串列表
	 * @param strs - 字符串列表
	 * @param spilter - 分割符
	 * @return
	 */
	public static List<String> getStrList(String strs, String spilter){
		List<String> list = new ArrayList<String>();
		if(StringUtils.isNotBlank(strs)){
			String[] strArray = strs.trim().split(spilter);            
			for (int i = 0; i < strArray.length; i++) {
				if(isBlank(strArray[i]) || list.contains(strArray[i].trim())){
					continue;
				}
				list.add(strArray[i].trim());
			}
		}
		return list;
	}
	public static List<String> getStrList(String strs){		
		return getStrList(strs, ",");
	}
	/**
	 * 判断两个list是否是相同的list(忽略顺序,去除空元素,忽略重复元素)
	 * @param list1
	 * @param list2
	 */
	public static boolean isSameList(String list1, String list2, String spilter){
		if(StringUtils.isBlank(list1) || StringUtils.isBlank(list2)){
			return false;
		}
		list1 = list1.trim();
		list2 = list2.trim();
		if(list1.equals(list2)){
			return true;
		}
		Set<String> set1 = getStrSet(list1, spilter);
		Set<String> set2 = getStrSet(list2, spilter);
		
		return set1.equals(set2);
	}
	/**
	 * 
	 * @param strs - 逗号分隔的字符串列表
	 * @param spilter - 分割符
	 * @return
	 */
	public static Set<String> getStrSet(String strs, String spilter){
		Set<String> set = new HashSet<String>();
		if(isNotBlank(strs)){
			String[] strArray = strs.trim().split(spilter);            
			for (int i = 0; i < strArray.length; i++) {
				String thisStr = strArray[i];
				if(isNotBlank(thisStr)){
					set.add(thisStr.trim());
				}
			}  
		}
		return set;
	}	
	/**
	 * 合并两个逗号list,去除重复元素
	 * @param list1  
	 * @param list2  
	 * @return
	 */
	public static String mergeList(String list1, String list2){
		if(StringUtils.isBlank(list1) && StringUtils.isBlank(list2)){
			return "";
		}
		
		String list = ifNull(list1, "") +","+ ifNull(list2, "");
		Set<String> set = new HashSet<String>();
		String[] strArray = list.trim().split(",");            
		for (int i=0; i<strArray.length; i++) {
			String thisStr = strArray[i];
			if(isNotBlank(thisStr)){
				set.add(thisStr.trim());
			}
		}
		if(set.size() == 0 ){
			return "";
		}
		return StringUtils.join(set, ",");
	}	
	
	/**
	 * 获取两个结合的交叉元素
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static Set<String> getCrossElements(Set<String> set1, Set<String> set2){
		Set<String> set = new HashSet<String>();
		if(set1 == null || set1.isEmpty() || set2 == null || set2.isEmpty()){
			return set;
		}
		Set<String> formatedSet1 = new HashSet<String>();
		for(String element : set1){
			if(StringUtils.isNotBlank(element)){
				formatedSet1.add(element.trim().toLowerCase());
			}
		}
		Set<String> formatedSet2 = new HashSet<String>();
		for(String element : set2){
			if(StringUtils.isNotBlank(element)){
				formatedSet2.add(element.trim().toLowerCase());
			}
		}
		for(String element : formatedSet1){
			if(formatedSet2.contains(element)){
				set.add(element);
			}
		}
		return set;
	}
	/**
	 * 获取两个结合的交叉元素个数
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static int getCrossElementsCount(Set<String> set1, Set<String> set2){
		int count = 0;
		if(set1 == null || set1.isEmpty() || set2 == null || set2.isEmpty()){
			return 0;
		}
		Set<String> formatedSet1 = new HashSet<String>();
		for(String element : set1){
			if(StringUtils.isNotBlank(element)){
				formatedSet1.add(element.trim().toLowerCase());
			}
		}
		Set<String> formatedSet2 = new HashSet<String>();
		for(String element : set2){
			if(StringUtils.isNotBlank(element)){
				formatedSet2.add(element.trim().toLowerCase());
			}
		}
		for(String element : formatedSet1){
			if(formatedSet2.contains(element)){
				count ++;
			}
		}
		return count;
	}
	
	public static Set<String> getStrSet(String strs){
		return getStrSet(strs, ",");
	}
	
	
	public static Set<Integer> array2Set(Integer[] array){
		Set<Integer> set = new HashSet<Integer>();
		for(Integer id : array) {
			set.add(id);
		}  
		return set;
	}
	
	public static List<Integer> array2List(Integer[] array){
		List<Integer> list = new ArrayList<Integer>();
		for(Integer id : array) {
			list.add(id);
		}  
		return list;
	}
	public static List<String> array2List(String[] array){
		List<String> list = new ArrayList<String>();
		for(String id : array) {
			if(StringUtils.isNotBlank(id)){
				list.add(id);
			}
		}  
		return list;
	}
	public static Integer[] set2Array(Set<Integer> set){
		Integer[] ids = {};
		ids = set.toArray(new Integer[set.size()]);
		return ids;
	}

	public static String[] set2StringArray(Set<String> set){
		String[] ids = {};
		ids = set.toArray(new String[set.size()]);
		return ids;
	}
	
	public static Integer[] list2Array(List<Integer> list){
		Integer[] ids = {};
		ids = list.toArray(new Integer[list.size()]);
		return ids;
	}
	public static String[] list2StringArray(List<String> list){
		String[] ids = {};
		ids = list.toArray(new String[list.size()]);
		return ids;
	}
	public static <E> List<E> set2List(Set<E> set){
		List<E> list = new ArrayList<E>();
		if(set != null && !set.isEmpty()){
			for(E id : set) {
				list.add(id);
			}  
		}
		return list;
	}
	public static List<String> set2ListStr(Set<String> set){
		List<String> list = new ArrayList<String>();
		for(String id : set) {
			list.add(id);
		}  
		return list;
	}
	public static <E> Set<E> list2Set(List<E> list){
		Set<E> set = new HashSet<E>();
		if(list != null && !list.isEmpty()){
			for(E obj : list) {
				set.add(obj);
			}  
		}
		return set;
	}
	/**
	 * 用connector连接strList中各个元素成字符串，并且忽略空串
	 * @param connector
	 * @param strList
	 * @return
	 */
	public static String join(String connector, List<String> strList){
		if(strList == null || strList.isEmpty()){
			return "";
		}
		List<String> finalList = new ArrayList<String>();
		for(String str : strList){
			if(StringUtils.isBlank(str)){
				continue;
			}
			finalList.add(str.trim());
		}
		return StringUtils.join(finalList, connector);
	}
	/**
	 * 判断是否是八种基本类型数组，是的话返回元素类型，不是的话返回null
	 * @param objs
	 * @return
	 */
	public static String getPrimitiveArrayType(Object array){
		if(array instanceof byte[]){
			return "byte";
		}
		if(array instanceof short[]){
			return "short";
		}
		if(array instanceof int[]){
			return "int";
		}
		if(array instanceof long[]){
			return "long";
		}
		if(array instanceof float[]){
			return "float";
		}
		if(array instanceof double[]){
			return "double";
		}
		if(array instanceof boolean[]){
			return "boolean";
		}
		if(array instanceof char[]){
			return "char";
		}
		return null;		
	}
	
	/**
	 * 连接成字符串
	 * @param objs
	 * @return
	 */
	public static String join(Object...objs){
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs){
			sb.append(obj);
		}
		return sb.toString();
	}
	/**
	 * 用connector连接成字符串，并且忽略空串，去除空白
	 * @param connector
	 * @param strings
	 * @return
	 */
	public static String joinWith(char connector, String...strings){
		StringBuilder sb = new StringBuilder();
		for(String string : strings){
			if(StringUtils.isNotBlank(string)){
				sb.append(connector).append(string.trim());
			}
		}
		String result = sb.toString().replaceAll("^"+connector+"+|"+connector+"+$", "");
		return result;
	}
	 /**
	 * 用connector连接成字符串，并且忽略空串，去除空白
	 * @param connector
	 * @param strings
	 * @return
	 */
	public static String joinWith(String connector, Object...objs){
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs){
			if(obj != null){
				sb.append(connector).append(String.valueOf(obj).trim());
			}
		}
		String result = sb.toString().replaceAll("^"+connector+"+|"+connector+"+$", "");
		return result;
	}
	public static boolean isInArray(Integer[] ids, Integer id){
		if(ids==null || id==null){
			return false;
		}
		for(Integer thisId : ids){
			//if(thisId == id){
			if(isEqual(thisId, id)){
				return true;
			}
		}
		return false;
	}

	public static boolean isInArray(String[] arr, String str){
		if(arr==null || str==null){
			return false;
		}
		for(String thisStr : arr){
			if(thisStr == null){
				continue;
			}
			if(thisStr.trim().equals(str.trim())){
				return true;
			}
		}
		return false;
	}
	public static boolean isInList(String idsStr, Integer id){
		if(idsStr==null || id==null){
			return false;
		}
		return join(",",idsStr,",").indexOf(join(",",id,",")) > 0;
	}
	public static boolean isInList(String list, String str){
		if(list==null || str==null){
			return false;
		}
		String[] arr = list.split(",");
		return isInArray(arr, str);
	}
	
	/**
	 * 判断两个Integer型的对象是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(BigDecimal a, BigDecimal  b){
		if(a == null || b == null){
			return false;
		}
		return (a.compareTo(b) == 0);		
	}
	/**
	 * 判断两个Integer型的对象是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(Integer a, Integer b){
		if(a == null || b == null){
			return false;
		}
		return (a.compareTo(b) == 0);		
	}
	/**
	 * 判断两个Long型的对象是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(Long a, Long b){
		if(a == null || b == null){
			return false;
		}
		return (a.compareTo(b) == 0);		
	}
	/**
	 * 判断两个Double型的对象是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isEqual(Double a, Double b){
		if(a == null || b == null){
			return false;
		}
		return (a.compareTo(b) == 0);		
	}
	public static boolean isNotEqual(Integer a, Integer b){
		return !isEqual(a,b);
	}
	public static boolean isNotEqual(Long a, Long b){
		return !isEqual(a,b);
	}
	public static boolean isNotEqual(Double a, Double b){
		return !isEqual(a,b);
	}
	/**
	 * 为null或者为0
	 * @param a
	 * @return
	 */
	public static boolean isNull0(Integer a){
		if(a == null){
			return true;
		}
		return (a.compareTo(0) == 0);		
	}
	public static boolean isNull0(Long a){
		if(a == null){
			return true;
		}
		return (a.compareTo(0l) == 0);		
	}
	/**
	 * 不为null且不为0
	 */
	public static boolean isNotNull0(Integer a){
		if(a == null){
			return false;
		}
		return (a.compareTo(0) != 0);		
	}
	/**
	 * 不为null且不为0
	 */
	public static boolean isNotNull0(BigDecimal a){
		if(a == null){
			return false;
		}
		return (a.compareTo(new BigDecimal(0)) != 0);		
	}
	public static boolean isNotNull0(Long a){
		if(a == null){
			return false;
		}
		return (a.compareTo(0l) != 0);		
	}
	public static boolean isNotNull0(Float a){
		if(a == null){
			return false;
		}
		return (a.compareTo(0f) != 0);		
	}
	public static boolean isNull0(Float a){
		if(a == null){
			return true;
		}
		return (a.compareTo(0f) == 0);		
	}
	
	public static boolean isNull0(BigDecimal a){
		if(a == null){
			return true;
		}
		return (a.compareTo(new BigDecimal(0)) == 0);		
	}
	
	/**
	 * @param dateTime
	 */
	public static String showTime(long dateTime) {
		if(dateTime==0){
			return "";
		}
		long now = System.currentTimeMillis();		
		long delay = (now - dateTime) / 1000 / 60;// 单位：分钟	
		String showTime = "刚刚";
		if(delay > 0){//过去的时间
			if (delay < 5) {
				return showTime;
			}
			if (delay < 60) {
				showTime = delay + "分钟前";
				return showTime;
			}
			
			delay /= 60;
			if(delay < 24) {
				showTime = delay + "小时前";
				return showTime;
			}
			
			delay /= 24;
			if(delay < 2) {
				showTime = "昨天";
				return showTime;
			}
		} else {
			delay = 0-delay;
			if (delay < 5) {
				showTime = "稍后";
				return showTime;
			}
			if (delay < 60) {
				showTime = delay + "分钟后";
				return showTime;
			}
			
			delay /= 60;
			if(delay < 24) {
				showTime = delay + "小时后";
				return showTime;
			}
			
			delay /= 24;
			if(delay < 2) {
				showTime = "明天";
				return showTime;
			}
		}
		
		return shortDate(dateTime);
	}
	/**
	 * @param dateTime
	 */
	public static String showTime(Date dateTime) {	
		if(dateTime==null){
			return "";
		}
		return showTime(dateTime.getTime());
	}
	/**
	 * 返回简洁的日期
	 * @param dateTime
	 */
	public static String shortDate(long dateTime) {	
		return shortDate(new Date(dateTime));
	}
	/**
	 * 返回简洁的日期
	 * @param dateTime
	 */
	public static String shortDate(Date dateTime) {	
		Date now = new Date();
		if(DateUtils.isSameDay(now, dateTime)){//同一天
			return DateUtils.dateToString(dateTime, "HH:mm");
		}
		if(DateUtils.isSameYear(now, dateTime)){//同一年
			return DateUtils.dateToString(dateTime, "MM-dd");
		}
		return DateUtils.dateToString(dateTime, "yyyy-MM-dd");
	}
	/**
	 * 获取通用图片地址
	 * @param frontBaseUrl
	 * @param picFileName
	 * @return
	 */
	public static String getCommonPicUtr(String frontBaseUrl, String picFileName) {
		String url = mergeUrl(frontBaseUrl, "/res/jianyeft/img/common/", picFileName);
		
		return url;
	}
	/**
	 * 将 4 个字节的字符（如：未经过处理的表情符）转换掉
	 * @param str
	 * @param replacement
	 * @return
	 */
	public static String trans4bytesChar(String str, String replacement){
		if(StringUtils.isBlank(str)){
			return str;
		}
		
		try {
			byte[] bytes = str.getBytes("UTF-8");
			for (int i=0; i<bytes.length; i++)  {  
				if((bytes[i] & 0xF8) == 0xF0){//四字节长度的字符
					for(int j=0; j<4; j++) {                          
						bytes[i+j] = 0x60;//重音符`，16进制的ASCII值 
					}//四个字节都替换为重音符
					i += 3;  
				}
			} 
			String newStr = new String(bytes, "UTF-8");
			return newStr.replaceAll("````", replacement);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 将字符串中的汉字 一、二、三等译为1,2,3以便排序
	 * @param str
	 * @return
	 */
	public static String decodeHanZi(String str) {	
		if(StringUtils.isBlank(str)){
			return "";
		}
		return str.replaceAll("[一]", "1")
			.replaceAll("[二]", "2")
			.replaceAll("[三]", "3")
			.replaceAll("[四]", "4")
			.replaceAll("[五]", "5")
			.replaceAll("[六]", "6")
			.replaceAll("[七]", "7")
			.replaceAll("[八]", "8")
			.replaceAll("[九]", "9")
			.replaceAll("[十]", "10")
			.replaceAll("[零]", "0");
	}
	/**
	 * 批量替换
	 * regexs 和 replacements的长度需一致
	 * @param str 
	 * @param regexs 
	 * @param replacements
	 * @return
	 */
	public static String decode(String str, String[] regexs, String[] replacements) {	
		if(str == null){
			return null;
		}
		if(StringUtils.isBlank(str)){
			return "";
		}
		if(regexs==null||replacements==null){
			return str;
		}
		int len = Math.min(regexs.length, replacements.length);
		String newStr = str;
		for(int i=0; i<len; i++){
			newStr = newStr.replaceAll(regexs[i], replacements[i]);
		}
		return newStr;
	}
	/**
	 * 批量替换
	 * 
	 * regexStr 和 replacementStr的逗号分割后的长度需一致
	 * 
	 * @param str
	 * @param regexStr 逗号分隔的正则列表
	 * @param replacementStr 逗号分隔的替换列表
	 * @return
	 */
	public static String decode(String str, String regexStr, String replacementStr) {	
		String[] regexs = regexStr.split(",");
		String[] replacements = replacementStr.split(",");
		return decode(str, regexs, replacements);
	}
	public static String decodeDic(Integer code, String codeList, String nameList) {	
		return transDic(code+"", codeList, nameList);
	}
	public static String decodeDic(String code, String codeList, String nameList) {	
		return transDic(code, codeList, nameList);
	}
	/**
	 * 解析字典的code为名称
	 * <pre>如果code不在codeList中：
	 * 		- 如果nameList的元素数  > codeList的元素，则返回nameList的最后一个，视其为默认值
	 * 		- 如果nameList的元素数 <= codeList的元素，则返回 code本身
	 *其他特殊情况，返回code本身
	 * </pre>
	 * @param code, 如: 1
	 * @param codeList, 如: 1,2
	 * @param nameList, 如: 家长,教师
	 * @return
	 */
	public static String transDic(Object code, String codeList, String nameList) {	
		String str = null;
		if(code==null){
			str = "";
		} else {
			str = String.valueOf(code);
		}
		if(StringUtils.isBlank(codeList) || StringUtils.isBlank(nameList)){
			return "";
		}
		if(StringUtils.isBlank(str)){
			str = "";
		}
		str = trim(str);
		String[] codeParts = codeList.split("[,，]");
		String[] nameParts = nameList.split("[,，]");
		for(int i=0; i<codeParts.length; i++){			
			if(str.equals(trim(codeParts[i]))){
				if(nameParts.length>i){
					return trim(nameParts[i]);
				} else {//code列表长度和比name列表长度大
					return str;
				}
			}
		}
		//到此则codeList中没有目标code
		if(codeParts.length < nameParts.length){//nameList较长，则认为其最后一个为默认值
			return trim(nameParts[nameParts.length-1]);
		}
		//返回原值
		return str;
		
	}
	public static long totalPage(long totalRecords, int pageSize){
		if(totalRecords <= 1){
			return 1;
		}
		if(pageSize <= 0){//不分页
			return 1;
		}
		long totalPage = 0;
		if(totalRecords%pageSize==0) {
			totalPage = totalRecords / pageSize;
		}else {
			totalPage = totalRecords / pageSize + 1;
		}
		return totalPage;
	}
	
	public static int totalPage(int totalRecords, int pageSize){
		if(totalRecords <= 1){
			return 1;
		}
		if(pageSize <= 0){//不分页
			return 1;
		}
		int totalPage = 0;
		if(totalRecords%pageSize==0) {
			totalPage = totalRecords / pageSize;
		}else {
			totalPage = totalRecords / pageSize + 1;
		}
		return totalPage;
	}
	/**
	 * value 转为整型，如果value是浮点型的，直接去掉末尾 
	 * @param value
	 * @return
	 */
	public static Integer toInteger(Object value){
		return toInteger(value, null);				
	}
	public static Integer toInteger(Object value, Integer defaultValue){
		if(value == null){
			return defaultValue;
		}
		Integer result = null;
		try{
			result = (Integer)value;
			return result;
		}catch(ClassCastException cce){
			String clazz = value.getClass().getSimpleName();
			if("Double".equals(clazz)){
				result = ((Double)value).intValue();
				return result;
			} else if("Float".equals(clazz)){
				result = ((Float)value).intValue();
				return result;
			} else if("BigDecimal".equals(clazz)){
				result = ((BigDecimal)value).intValue();
				return result;
			} else {
				try{
					result = Integer.parseInt(value.toString().trim().replaceAll("\\..*$", ""));
					return result;
				} catch(Exception e){
					//e.printStackTrace();
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return defaultValue;
	}
	public static List<Integer> toIntegerList(List<Object> list){
		List<Integer> result = new ArrayList<Integer>();
		if(list != null){
			for(Object obj : list){
				Integer temp = toInteger(obj);
				if(temp != null){
					result.add(temp);
				}				
			}
		}
		return result;		
	}
	public static List<Long> toLongList(List<Object> list){
		List<Long> result = new ArrayList<Long>();
		if(list != null){
			for(Object obj : list){
				Long temp = toLong(obj);
				if(temp != null){
					result.add(temp);
				}				
			}
		}
		return result;		
	}
	public static Float toFloat(Object value){
		return toFloat(value, null);
	}
	public static Float toFloat(Object value, Float defaultValue){
		if(value == null){
			return defaultValue;
		}
		Float result = defaultValue;
		try{
			result = (Float)value;
			return result;
		}catch(ClassCastException cce){
			try{
				result = Float.parseFloat(value.toString().trim());
				return result;
			} catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultValue;
	}
	public static Double toDouble(Object value){
		return toDouble(value, null);
	}
	public static Double toDouble(Object value, Double defaultValue){
		if(value == null){
			return defaultValue;
		}
		Double result = defaultValue;
		try{
			 String valueClass = value.getClass().getSimpleName();           
			//基本类型数据
			if("Double".indexOf(valueClass) != -1){			
				result = (Double)value;
			} else {
				result = Double.parseDouble(value.toString().trim());
			}
			return result;
			
		}catch(ClassCastException cce){
			try{
				result = Double.parseDouble(value.toString().trim());
				return result;
			} catch(Exception e){
				//e.printStackTrace();
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return defaultValue;
	}
	/**
	 * 获取最大值
	 * @return
	 */
	public static BigDecimal max(BigDecimal... ds){ 
		if(ds == null || ds.length == 0){
			return null;
		}
		BigDecimal max = ds[0]; 	    	
		for(BigDecimal d : ds){
			if(d.compareTo(max) > 0){
				max = d;
			}
		}
		return max;	    	
	} 
	
	/**
	 * 获取最小值
	 * @return
	 */
	public static BigDecimal min(BigDecimal... ds){ 
		if(ds == null || ds.length == 0){
			return null;
		}
		BigDecimal min = ds[0]; 	    	
		for(BigDecimal d : ds){
			if(d.compareTo(min) < 0){
				min = d;
			}
		}
		return min;	    	
	} 
	
	
	//---------浮点数加减乘除---------
	/*
	  在进行数字运算时，如果有double或float类型的浮点数参与计算，偶尔会出现计算不准确的情况
	 	System.out.println(0.05+0.01); //==>0.060000000000000005 
    	System.out.println(1.0-0.42);  //==>0.5800000000000001
    	System.out.println(19.9*100); //==>1989.9999999999998
    	System.out.println(123.3/100); //==>1.2329999999999999  
    	
    	《Effective Java》中提到一个原则，那就是float和double只能用来作科学计算或者是工程计算，
    	但在商业计算中我们要用java.math.BigDecimal
	 */
	/**
	 * d1 + d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double add(Object d1, Object d2){  		
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.add(b2).doubleValue(); 
	}
	/**
	 * d1 + d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal addBD(Object d1, Object d2){  		
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.add(b2); 
	}
	
    /**
	 * d1 - d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
	 * @param d1
	 * @param d2
	 * @return
	 */
    public static double sub(Object d1,Object d2){  
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.subtract(b2).doubleValue(); 
    } 
    /**
   	 * d1 - d2
   	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
   	 * @param d1
   	 * @param d2
   	 * @return
   	 */
       public static BigDecimal subBD(Object d1,Object d2){  
       	   BigDecimal b1 = new BigDecimal(String.valueOf(d1));
           BigDecimal b2 = new BigDecimal(String.valueOf(d2));
           return b1.subtract(b2); 
       }
    
     /**
      * d1*d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
      * @param d1
      * @param d2
      * @return
      */
    public static double mul(Object d1,Object d2){  
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));
        return b1.multiply(b2).doubleValue();  
    }
    /**
     * d1*d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
     * @param d1
     * @param d2
     * @return
     */
   public static BigDecimal mulBD(Object d1,Object d2){  
   	   BigDecimal b1 = new BigDecimal(String.valueOf(d1));
       BigDecimal b2 = new BigDecimal(String.valueOf(d2));
       return b1.multiply(b2);  
   }
    
    /**
     * d1/d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
     * @param d1
     * @param d2
     * @return
     */
    public static double div(Object d1,Object d2){   
        return div(d1, d2, 2);            
    }
    /**
     * d1/d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal divBD(Object d1,Object d2){   
        return divBD(d1, d2, 2);            
    }
    
    /**
     * d1/d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
     * @param d1
     * @param d2
     * @param scale 四舍五入保留小数位数
     * @return
     */
    public static double div(Object d1, Object d2, int scale){  
        if(scale<0){  
            //throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        	scale = 2;
        }
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2=new BigDecimal(String.valueOf(d2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();            
    }
    /**
     * d1/d2
	 * <p>浮点数加减乘除用float、double直接运算，有时会出现精度不准确的情况
     * @param d1
     * @param d2
     * @param scale 四舍五入保留小数位数
     * @return
     */
    public static BigDecimal divBD(Object d1, Object d2, int scale){  
        if(scale<0){  
            //throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        	scale = 2;
        }
    	BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2=new BigDecimal(String.valueOf(d2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);            
    }
	//-------------------------------
	
	/**
	 * 转为BigDecimal并保留指定小数位数
	 * @param value
	 * @param scale - 小数位数
	 * @return
	 */
	public static BigDecimal toDecimal(Object value, BigDecimal defaultValue){
		if(value == null){
			return defaultValue;
		}
		if(value instanceof BigDecimal){
			return (BigDecimal)value;
		}
		Double doubleValue = toDouble(value, 0.00d);
		String decimalString = getDecimalAutoStr(doubleValue, 2);
		BigDecimal decimal = new BigDecimal(decimalString);
		return decimal;
	}
	
	/**
	 * 转为BigDecimal并保留指定小数位数
	 * @param value
	 * @param scale - 小数位数
	 * @return
	 */
	public static BigDecimal toDecimal(Object value, int scale){
		if(value == null){
			return null;
		}
		if(value instanceof BigDecimal){
			return (BigDecimal)value;
		}
		Double doubleValue = toDouble(value, 0.00d);
		String decimalString = getDecimalAutoStr(doubleValue, scale);
		BigDecimal decimal = new BigDecimal(decimalString);
		return decimal;
	}
	/**
	 * 转为BigDecimal并保留2位小数
	 * @param value
	 * @return
	 */
	public static BigDecimal toDecimal(Object value){
		return toDecimal(value, 2);
	}
	
	public static Long toLong(Object value){
		return toLong(value, null);
	}
	public static Long toLong(Object value, Long defaultValue){
		if(value == null){
			return defaultValue;
		}
		Long result = defaultValue;
		try{
			result = (Long)value;
			return result;
		}catch(ClassCastException cce){
			try{
				result = Long.parseLong(value.toString().trim());
				return result;
			} catch(Exception e){
				//e.printStackTrace();
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return defaultValue;
	}
	public static Short toShort(Object value){
		return toShort(value, null);
	}
	public static Short toShort(Object value, Short defaultValue){
		if(value == null){
			return defaultValue;
		}
		Short result = defaultValue;
		try{
			result = (Short)value;
			return result;
		}catch(ClassCastException cce){
			try{
				result = Short.parseShort(value.toString().trim());
				return result;
			} catch(Exception e){
				//e.printStackTrace();
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return defaultValue;
	}
	public static String toString(Object value){
		return toString(value, null);
	}
	public static String toString(Object value, String defaultValue){
		if(value == null){
			return defaultValue;
		}
		return String.valueOf(value).trim();
	}
	
	public static List<String> toStringList(List<Object> list){
		List<String> result = new ArrayList<String>();
		if(list != null){
			for(Object obj : list){
				String temp = toString(obj);
				if(temp != null){
					result.add(temp);
				}
			}
		}
		return result;		
	}
	public static Boolean toBoolean(Object value){
		return toBoolean(value, null);
	}
	public static Boolean toBoolean(Object value, Boolean defaultValue){
		if(value == null){
			return defaultValue;
		}
		Boolean result = defaultValue;
		try{
			result = (Boolean)value;
			return result;
		}catch(ClassCastException cce){
			String strValue = value.toString().trim();
			if("true".equalsIgnoreCase(strValue) || "yes".equalsIgnoreCase(strValue) || "1".equalsIgnoreCase(strValue)){
				return true;
			}
			return false;
		}catch(Exception e){
			//e.printStackTrace();
		}
		return defaultValue;
	}
	public static List<Boolean> toBooleanList(List<Object> list){
		List<Boolean> result = new ArrayList<Boolean>();
		if(list != null){
			for(Object obj : list){
				Boolean temp = toBoolean(obj);
				if(temp != null){
					result.add(temp);
				}
			}
		}
		return result;		
	}
	public static Date toDate(Object value){
		return toDate(value, null);
	}
	/**
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static Date toDate(Object value, String format){
		if(value == null){
			return null;
		}
		if("0".equals(value+"")){
			return null;
		}
		Date result = null;
		try{
			result = (Date)value;
			return result;
		}catch(ClassCastException cce){
			String strValue = value.toString().trim();
			strValue = strValue.replaceAll("\\s+", " ").replaceAll("\\s0000$", " +0000");//ios传过来的时区信息
			if (strValue.matches("^\\d+$")) {//long
				try {
					long timeLong = Long.parseLong(strValue);
					return new Date(timeLong);
				} catch (Exception e) {
					throw e;
				}
			} else {			
				if(isBlank(format)){
					DateFormat[] acceptDateFormats = {
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"),
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),	
							new SimpleDateFormat("yyyy-MM-dd"),		 
							new SimpleDateFormat("yyyy年MM月dd日"),       
							new SimpleDateFormat("yyyy-M-d HH:m:s"),
							new SimpleDateFormat("yyyy-MM-dd HH:mm"),
							new SimpleDateFormat("yyyy-M-d"),
							new SimpleDateFormat("yyyy年M月d日"),
							new SimpleDateFormat("yyyy/MM/dd"),
							new SimpleDateFormat("yyyy/M/d"),
							new SimpleDateFormat("yyyy-MM"),
							new SimpleDateFormat("yyyy-M"),
							new SimpleDateFormat("yyyy年MM月"),
							new SimpleDateFormat("yyyy年M月")
					};			
					for (DateFormat f : acceptDateFormats) {
						try {
							return f.parse(strValue);
						} catch (ParseException | RuntimeException e) {
							continue;
						}
					}	 
				} else {
					try {
						return (new SimpleDateFormat(format)).parse(strValue);
					} catch (ParseException | RuntimeException e) {
						//e.printStackTrace();
						return null;
					}
					
				}
			}//long..else
		}catch(Exception e){
			//e.printStackTrace();
		}
		return null;
	}
	public static List<Date> toDateList(List<Object> list){
		List<Date> result = new ArrayList<Date>();
		if(list != null){
			for(Object obj : list){
				Date temp = toDate(obj);
				if(temp != null){
					result.add(temp);
				}
			}
		}
		return result;		
	}
	/**
	 * <p>Checks if a String is whitespace, empty ("") or null(null, "null").</p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("null")    = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str  the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(CharSequence cs){
		int strLen;
		if ((cs == null) || ((strLen = cs.length()) == 0))
			return true;
		
		for (int i = 0; i < strLen; ++i) {
			if (!(Character.isWhitespace(cs.charAt(i)))) {
				return false;
			}
		}
		
		if("null".contentEquals(cs)){
			return true;
		}
		
		return false;
	}
	/**
	 * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
	 *
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("null")    = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param str  the String to check, may be null
	 * @return <code>true</code> if the String is
	 *  not empty and not null and not whitespace
	 */
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	public static String trim(String str){
		if(str==null){
			return "";
		}
		return str.replaceAll("　", " ").trim();
	}

	public static <E> boolean isEmpty(Collection<E> c){
		return c==null || c.isEmpty();
	}
	public static <K,V> boolean isEmpty(Map<K, V> m){
		return m==null || m.isEmpty();
	}
	public static <E> boolean isNotEmpty(Collection<E> c){
		return !isEmpty(c);
	}
	
	/** 
	 * 检测邮箱地址是否合法 
	 * @param email 
	 * @return true合法 false不合法 
	 */  
	public static boolean isEmail(String email){  
	  if(StringUtils.isBlank(email)) return false;
	  email = email.trim();
	  Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
	  Matcher m = p.matcher(email);  
	  return m.matches();  
	 }

	/** 
	 * 检测伪邮箱地址是否合法,如a#c.com
	 * @param email 
	 * @param pseudo - 伪装符号,如:#
	 * @return true合法 false不合法 
	 */  
	public static boolean isPseudoEmail(String email, String pseudo){  
	  if(StringUtils.isBlank(email)) return false;
	  email = email.trim();
	  Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*"+pseudo+"\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
	  Matcher m = p.matcher(email);  
	  return m.matches();  
	}
	/** 
	 * 将伪邮箱地址转为合法邮箱,如a#c.com --> a@c.com
	 * @param email 
	 * @param pseudo - 伪装符号,如:#
	 * @return 
	 */  
	public static String transPseudo2Email(String email, String pseudo){  
		 if(StringUtils.isBlank(email)) return "";
		return email.replaceAll(pseudo, "@");
	}
	/** 
	 * 将合法邮箱转为伪邮箱地址,如a@c.com --> a#c.com
	 * @param email 
	 * @param pseudo - 伪装符号,如:#
	 * @return 
	 */  
	public static String transEmail2Pseudo(String email, String pseudo){  
		 if(StringUtils.isBlank(email)) return "";
		return email.replaceAll("@", pseudo);
	}
	 /**
     * 将 Date 转化为指定格式的String
     * 月(M)、月(Y中文月)、日(d)、小时(H)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
     * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
     * 例子： 
     * formatDate((new Date()), "yyyy-MM-dd HH:mm:ss") ==> 2006-07-02 08:09:04
     * formatDate((new Date()), "yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18 
     */
	public static String formatDate(Object date, String format){ 
		if(date==null){
			return "";
		}
		if(isBlank(format)){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		String clazz = date.getClass().getSimpleName();
		if("Date".equals(clazz)){
			return DateUtils.dateToString((Date)date, format);
		} else if("Long".equals(clazz)){
			return DateUtils.dateToString((Long)date, format);
		} else if("String".equals(clazz)){
			return (String)date;
		}
		return "";
	}
	/**
	 * <pre>
	 * 替换字符串模板中的变量,变量格式{n}, n为变量序号,从0开始
	 * 如：xxxx{0}yyy{1}zzz,
	 * </pre>
	 * @param template - 字符串模板
	 * @param values - 替换后的值列表
	 * @return
	 */
	public static String format(String template, Object... values){
		return parseTemplate(template, values);
	}
	/**
	 * 条件表达式
	 * @param cond
	 * @param trueResult
	 * @param falseResult
	 * @return
	 */
	public static Object condExp(Object cond, Object trueResult, Object falseResult){
		boolean flag = true;
		if(cond == null){
			flag = false;
		} else {
			String clazz = cond.getClass().getSimpleName();
			if("Boolean".equals(clazz)){
				flag = toBoolean(cond);
			} else if("String".equals(clazz)){
				flag = isNotBlank((String)cond);
			}
		}
		return flag ? trueResult : falseResult;
	}
	/**
	 * <pre>
	 * 替换字符串模板中的变量,变量格式{n}, n为变量序号,从0开始
	 * 如：xxxx{0}yyy{1}zzz,
	 * </pre>
	 * @param template - 字符串模板
	 * @param values - 替换后的值列表
	 * @return
	 */
	public static String parseTemplate(String template, Object... values){
		String result = template;
		
		for(int i=0; i<values.length; i++){
			result = result.replaceAll("\\{"+i+"\\}", String.valueOf(values[i]));
		}
		
		return result;
	}
	
	/** 
	  * 替换一个字符串中的某些指定字符 
	  * @param strData String 原始字符串 
	  * @param regex String 要替换的字符串 
	  * @param replacement String 替代字符串 
	  * @return String 替换后的字符串 
	  */  
	 public static String replaceString(String strData, String regex,  
			 String replacement)  
	 {  
		 if (strData == null)  
		 {  
			 return null;  
		 }  
		 int index;  
		 index = strData.indexOf(regex);  
		 String strNew = "";  
		 if (index >= 0)  
		 {  
			 while (index >= 0)  
			 {  
				 strNew += strData.substring(0, index) + replacement;  
				 strData = strData.substring(index + regex.length());  
				 index = strData.indexOf(regex);  
			 }  
			 strNew += strData;  
			 return strNew;  
		 }  
		 return strData;  
	 }  

	public static String URLEncoder(String str){
		try {
			if(StringUtils.isBlank(str)){
				return "";
			}
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}
	public static String URLDecoder(String str){
		try {
			if(StringUtils.isBlank(str)){
				return "";
			}
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}
	
	/**
	 * 替换字符串中HTML实体为特殊字符
	 * @param strData
	 * @return
	 */
	public static String decodeString(String strData)
	 {  
		 if(StringUtils.isBlank(strData)) {  
			 return "";
		 }
		 strData = strData.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")
			.replaceAll("&quot;", "\"")
			.replaceAll("&apos;", "'")
			.replaceAll("&amp;", "&");
		 
		 return strData;  
	 } 

    /**
     * 将\u60a8形式的Unicode转为UTF-8编码，可以混合
     * @param theString 如 - 123中国abc\u60a8\u7684\ 转为 123中国abv您的
     * @return
     */
    public static String decodeUnicode(String theString) { 
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

	/**
	 * 获取字符串的字节长度,一个汉字2个字节长度
	 * @param s
	 * @return
	 */
	 public static int len(String s){
		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	 } 

	 /**
	  * 根据账号，判断其类型
	  * @param account
	  * @return 0:未知类型，1:手机号, 2:邮箱, 3:普通电话号码(存于phoneNumber2)
	  */
	 public static int getAccountType(String account){
		 if(StringUtils.isBlank(account)){
			 return 0;
		 }
		 //去除"_用户类型"后缀
		 account = account.trim().replaceAll("(^.+)_[12]{1}$", "$1");
		 
		 if(account.matches("^1\\d{10}$")){//有效的11位手机号
			 return 1;
		 }

		 if(isEmail(account)){
			 return 2;
		 }
		 
		 if(account.matches("^\\d{11}$")){//11位非手机号的电话号码(存在phoneNumber2中)
			 return 3;
		 }
		 
		 return 0;
	 }
	 public static boolean isMobilePhone(String phone){
		 if(StringUtils.isBlank(phone)){
			 return false;
		 }
		 if(phone.matches("^1\\d{10}$")){//有效的11位手机号
			 return true;
		 }
		 return false;
	 }
	 public static boolean isMobilePhone_new(String phone){
		 if(StringUtils.isBlank(phone)){
			 return false;
		 }
		 if(phone.matches("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$")){//有效的11位手机号
			 return true;
		 }
		 return false;
	 }
	 
	 

	 public static String getString(Object obj){
		 if(obj==null||obj.equals("null"))
			 return null;
		 return (String)obj;
	 }
	 
	 /**
	  * 取得字符串的字节长度
	  * 
	  * @param str
	  * @param chineseLength - 一个中文字符占的长度
	  * @return
	  */
	 public static int getLength(String str, int chineseLength){
		 if(StringUtils.isBlank(str)){
			 return 0;
		 }
		 StringBuilder sb = new StringBuilder();
		 for(int i=0; i<chineseLength; i++){
			 sb.append("_");
		 }
		 String replacer = sb.toString();
		 return str.replaceAll("[\u4E00-\u9FA5]", replacer).length();
	 }
	 /**
	  * 取得字符串的字节长度(字一个汉字两个字符)
	  * 
	  * @param str
	  * @return
	  */
	 public static int getLength(String str){
		 if(StringUtils.isBlank(str)){
			 return 0;
		 }
		 return str.replaceAll("[\u4E00-\u9FA5]", "__").length();
	 }
	 /**
	  * 取得比例
	  * @param fenzi - 分子
	  * @param fenmu - 分母
	  * @return
	  */
	 public static double getRatio(long fenzi, long fenmu){
		if(fenzi==0 || fenmu==0){
			return 0;
		}
		return (double)fenzi/fenmu;
	 }
	 
	 /**
	  * 取得比例
	  * @param fenzi - 分子
	  * @param fenmu - 分母
	  * @return
	  */
	 public static float getRate(int fenzi, int fenmu){
		if(fenzi==0 || fenmu==0){
			return 0f;
		}
		return (float)fenzi/fenmu;
	 }
	
	 public static float getRate(float fenzi, int fenmu){
		if(fenzi==0 || fenmu==0){
			return 0f;
		}
		return (float)fenzi/fenmu;
	 }
	 public static float getRate(int fenzi, float fenmu){
		if(fenzi==0 || fenmu==0){
			return 0f;
		}
		return (float)fenzi/fenmu;
	 }
	 public static float getRate(float fenzi, float fenmu){
		if(fenzi==0 || fenmu==0){
			return 0f;
		}
		return (float)fenzi/fenmu;
	 }
	 public static String getRateStr(float rate){
		 return String.format("%.2f", rate * 100) + "%";
	 }
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -最大小数位数
	  * @return
	  */
	 public static String getDecimalAutoStr(float decimal, int scale){
		 NumberFormat nf = NumberFormat.getInstance();  
		 // 设置是否使用分组  
		 nf.setGroupingUsed(false);   
		 // 设置最小整数位数  
		 nf.setMaximumFractionDigits(scale);  
		 
		 return nf.format(decimal);
	 }
	 
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -最大小数位数
	  * @return
	  */
	 public static String getDecimalAutoStr(double decimal, int scale){
		 NumberFormat nf = NumberFormat.getInstance();  
		 // 设置是否使用分组  
		 nf.setGroupingUsed(false);   
		 // 设置最小整数位数  
		 nf.setMaximumFractionDigits(scale);  
		 
		 return nf.format(decimal);
	 }
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -最大小数位数
	  * @return
	  */
	 public static String getDecimalAutoStr(BigDecimal decimal, int scale){
		 NumberFormat nf = NumberFormat.getInstance();  
		 // 设置是否使用分组  
		 nf.setGroupingUsed(false);   
		 // 设置最小整数位数  
		 nf.setMaximumFractionDigits(scale);  
		 
		 return nf.format(decimal);
	 }
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -固定小数位数
	  * @return
	  */
	 public static String getDecimalStr(double decimal, int scale){
		 return String.format("%."+scale+"f", decimal);
	 }
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -固定小数位数
	  * @return
	  */
	 public static String getDecimalStr(float decimal, int scale){
		 return String.format("%."+scale+"f", decimal);
	 }
	 /**
	  * 获取小数显示值
	  * @param decimal - 小数
	  * @param scale -固定小数位数
	  * @return
	  */
	 public static String getDecimalStr(BigDecimal decimal, int scale){
		 return String.format("%."+scale+"f", decimal);
	 }
	/**
	 * <p>Escapes the characters in a <code>String</code> to be suitable to pass to
	 * an SQL query.</p>
	 *
	 * <p>For example,
	 * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" + 
	 *   StringEscapeUtils.escapeSql("McHale's Navy") + 
	 *   "'");</pre>
	 * </p>
	 *
	 * <p>At present, this method only turns single-quotes into doubled single-quotes
	 * (<code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not
	 * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.</p>
	 *
	 * see http://www.jguru.com/faq/view.jsp?EID=8881
	 * @param str  the string to escape, may be null
	 * @return a new String, escaped for SQL, <code>null</code> if null string input
	 */
	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return StringUtils.replace(str, "'", "\\'");
	}
	 public static String makeUUID(){
		return uuid();
	 }
	 public static String uuid(){
		return java.util.UUID.randomUUID().toString();
	 }
	 

	/**
	 * Map拷贝
	 * @param from
	 * @param to
	 * @param fromKeys 源Map中要拷贝的键,逗号分割
	 */
	public static Map<String, Object> copyMap(Map<String, Object> from,  Map<String, Object> to, String fromKeys){
		return copyMap(from, to, fromKeys, fromKeys);
	}
	/**
	 * Map拷贝
	 * @param from
	 * @param to
	 * @param fromKeys 	源Map中要拷贝的键,逗号分割
	 * @param toKeys	目标Map中要设置的键,逗号分割
	 */
	public static Map<String, Object> copyMap(Map<String, Object> from,  Map<String, Object> to, String fromKeys, String toKeys){
		if(to == null){
			to = new HashMap<String, Object>();
		}
		if(from == null){
			return to;
		}
		if(StringUtils.isBlank(fromKeys)){
			return to;
		}
		if(StringUtils.isBlank(toKeys)){
			toKeys = fromKeys;
		}
		String [] fromKeyArr = fromKeys.split(",");
		String [] toKeyArr = toKeys.split(",");
		int len = Math.min(fromKeyArr.length, toKeyArr.length);
		for(int i=0; i<len; i++){
			String fromKey = fromKeyArr[i];
			String toKey = toKeyArr[i];
			if(StringUtils.isBlank(fromKey) || StringUtils.isBlank(toKey)){
				continue;
			}
			fromKey = fromKey.trim();
			toKey = toKey.trim();
			to.put(toKey, from.get(fromKey));
		}
		return to;
	}	
	
	/**
	 * 将 str 的 beginIndex~endIndex部分用replacement替换
	 * @param str
	 * @param beginIndex 	起始位置，从0开始，包含在替换范围内
	 * @param endIndex		结束位置，从0开始，包含在替换范围内
	 * @param replacement
	 * @return
	 */
	public static String subReplace(String str, int beginIndex, int endIndex, String replacement){	
		if(StringUtils.isBlank(str)){
			return "";
		}
		return str.substring(0, beginIndex) + replacement + str.substring(endIndex+1);
	}
	/**
	 * 将一个元素加入到唯一性列表尾部
	 * @param list
	 * @param element
	 * @return
	 */
	public static String addToList(String list, Object element){
		return addToList(list, element, true);
	}
	/**
	 * 将一个元素加入到列表中尾部
	 * @param list
	 * @param element
	 * @param unique
	 * @return
	 */
	public static String addToList(String list, Object element, boolean unique){
		return addToList(list, element, unique, true);
	}
	/**
	 * @param count
	 * @param limit
	 * @return
	 */
	public static String showCount(int count, int limit){
		return count<=limit ? count+"" : limit+"+";
	}
	/**
	 * @param count
	 * @return
	 */
	public static String showCount(int count){
		return showCount(count, 99);
	}
	/**
	 * @param count
	 * @param limit
	 * @return
	 */
	public static String showCount(long count, int limit){
		return count<=limit ? count+"" : limit+"+";
	}
	/**
	 * @param count
	 * @return
	 */
	public static String showCount(long count){
		return showCount(count, 99);
	}
	/**
	 * 将一个元素加入到列表中
	 * @param list
	 * @param element
	 * @param unique
	 * @param add2Tail  - 是否从队尾追加， true:队尾追加, false:队首添加
	 * @return
	 */
	public static String addToList(String list, Object element, boolean unique, boolean add2Tail){
		if(element == null){
			return list;
		}
		String str = String.valueOf(element).trim();
		if(StringUtils.isBlank(list)){
			return str;
		}
		list = list.trim();
		if(unique && isInList(list, str)){
			//return list;
		} else {
			if(add2Tail){
				list = list + "," + str;
			} else {
				list =  str + "," + list;
			}
		}
		
		list = list.replaceAll("\\s+", "").replaceAll("^,*", "").replaceAll(",*$", "").replaceAll(",+", ",");		
		return list;		
	}


	/**
	 * 将一个元素从列表中移除
	 * @param list
	 * @param element
	 * @return
	 */
	public static String removeFromList(String list, Object element){
		if(element == null || list == null){
			return list;
		}
		if(StringUtils.isBlank(list)){
			return list;
		}
		String str = String.valueOf(element).trim();
		if(StringUtils.isBlank(str)){
			return list;
		}	
		list = ","+list.trim()+",";				
		list = list.replaceAll(","+str+",", ",").replaceAll("\\s+", "").replaceAll("^,*", "").replaceAll(",*$", "").replaceAll(",+", ",");		
		return list;		
	}

	
	/**
	 * 删除Map中的键值
	 * @param map
	 * @param keys
	 */
	public static Map<String, Object> deleteMapKeys(Map<String, Object> map, String keys){
		if(map == null){
			return null;
		}
		if(StringUtils.isBlank(keys)){
			return map;
		}
		String [] keyArr = keys.split(",");
		for(String key : keyArr){
			key = key.trim();
			map.remove(key);
		}
		return map;
	}
	/**
	 * 删除Map中的键值
	 * @param map
	 * @param keys
	 */
	public static Map<String, Object> removeMapKeys(Map<String, Object> map, String keys){
		return deleteMapKeys(map, keys);
	}
	
	/**
	 * 合并为一个URL地址
	 * @param parts
	 * @return
	 */
	public static String mergeUrl(String...parts){
		String url = StringUtils.join(parts, "/");	
		return url.replaceAll("://", ":##").replaceAll("/+", "/").replaceAll(":##", "://");
	}
	
	/**
	 * 合并为一个文件路径
	 * @param parts
	 * @return
	 */
	public static String mergePath(String...parts){
		String path = StringUtils.join(parts, File.separator);
		return path.replaceAll("\\\\+", "/").replaceAll("/+", "\\"+File.separator);
	}
	/**
	 * 从fastdfs的完整文件路径中截取出文件id
	 * @param fastDfsFullUrl 如，http://123.57.255.1:8068/group1/M00/00/08/ezn_AVU8uYOAHIdKAAEKVm1GFRg786.apk
	 * @return group1/M00/00/08/ezn_AVU8uYOAHIdKAAEKVm1GFRg786.apk
	 */
	public static String getFastDfsFileId(String fastDfsFullUrl){	
		if(StringUtils.isBlank(fastDfsFullUrl)){
			return "";
		}
		if(fastDfsFullUrl.indexOf("://") == -1){
			return  fastDfsFullUrl.replaceAll("^\\s*/", "");
		}
		return fastDfsFullUrl.replaceAll("^.*://", "").replaceAll("^.*?/", "");
	}
	/**
	 * 克隆一个map，并添加一个随机键randomKey
	 * @param map
	 * @return
	 */
	public static Map<String, Object> genNewMap(Map<String, Object> map){
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.putAll(map);
		newMap.put("randomKey", System.currentTimeMillis() + "_" + random6());
		return newMap;
	}
	
	
	/**
	 * 解析emoji表情
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static String transEmoji(String str){
		String pattern = "\\[e\\]\\s*(.+?)\\s*\\[/e\\]";
		Pattern p = Pattern.compile(pattern); 
		java.util.regex.Matcher matcher = p.matcher(str);
		if(matcher.find()){
			str = matcher.replaceAll("<span class=\"emoji emoji-$1\"></span>");
		}
		return str;
	}
	
	/**
	 * 去掉emoji表情
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static String killEmoji(String str){
		
		String pattern = "\\[e\\]\\s*(.+?)\\s*\\[/e\\]";
		Pattern p = Pattern.compile(pattern); 
		java.util.regex.Matcher matcher = p.matcher(str);
		if(matcher.find()){
			str = matcher.replaceAll("[表情]");
		}
		return str;
	}
	
	/**
	* 数字不足位数左补0
	* @param str
	* @param strLength
	*/
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);//左补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}
	
	/**
	 * 保留两位小数
	 * @param param1  分子
	 * @param param2 分母
	 */
	public static double getRatio(double param1, double param2){
		
		if(param2 == 0){
			return 0;
		}
		
		BigDecimal bg = new BigDecimal(param1 / param2);
		double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		return f1;
	}
	
	/** 
	 * 获取本机所有IP 
	 */  
	public static String[] getAllLocalHostIP() {  
		List<String> res = new ArrayList<String>();  
		Enumeration<NetworkInterface> netInterfaces;  
		try {  
			netInterfaces = NetworkInterface.getNetworkInterfaces();  
			InetAddress ip = null;  
			while (netInterfaces.hasMoreElements()) {  
				NetworkInterface ni = netInterfaces.nextElement();  
				Enumeration<InetAddress> nii = ni.getInetAddresses();  
				while (nii.hasMoreElements()) {  
					ip = nii.nextElement();  
					if (ip.getHostAddress().indexOf(":") == -1) {  
						res.add(ip.getHostAddress());  
						//System.out.println("本机的ip=" + ip.getHostAddress());  
					}  
				}  
			}  
		} catch (SocketException e) {  
		}  
		return (String[]) res.toArray(new String[0]);  
	}
		

	/**
	 * 将nativeQuery的结果转为List<Map>
	 * @param sqlResults - nativeQuery 查询结果
	 * @param keyStr - 一条记录中的各个字段名有序拼串
	 * @return
	 */
	public static List<Map<String, Object>> sqlResultToList(List<Object[]> sqlResults, String keyStr){
		return sqlResultToList(sqlResults, keyStr, false, false);
	}
	/**
	 * 将nativeQuery的结果转为List<Map>
	 * @param sqlResults - nativeQuery 查询结果
	 * @param keyStr - 一条记录中的各个字段名有序拼串
	 * @param useStringValue - 是否都统一使用字符串值
	 * @return
	 */
	public static List<Map<String, Object>> sqlResultToList(List<Object[]> sqlResults, String keyStr, boolean useStringValue){
		return sqlResultToList(sqlResults, keyStr, useStringValue, false);
	}
	/**
	 * 将nativeQuery的结果转为List<Map>
	 * @param sqlResults - nativeQuery 查询结果
	 * @param keyStr - 一条记录中的各个字段名有序拼串
	 * @param useStringValue - 是否都统一使用字符串值
	 * @param date4excel - 日期字段是否要针对导出excel做特殊处理
	 * @return
	 */
	public static List<Map<String, Object>> sqlResultToList(List<Object[]> sqlResults, String keyStr, boolean useStringValue, boolean date4excel){
		List<Map<String, Object>> list = new ArrayList<>();
		if(sqlResults == null || sqlResults.isEmpty()){
			return list;
		}
		
		String[] keys = keyStr.split(",");
		for(Object[] objs: sqlResults){ 
			Map<String, Object> map = new HashMap<String, Object>();
			int count = Math.min(objs.length, keys.length);
			for(int i=0; i<count; i++){
				//将java.math.BigInteger等复杂类型转换简单类型
				Object value = objs[i];
				String thisKey = trim(keys[i]);
				if(value != null){
					if(useStringValue){
						String className = value.getClass().getSimpleName();
						if("Date".equals(className) || "Timestamp".equals(className)){
							if(date4excel){
								map.put(thisKey, DateUtils.dateToString((Date)value, "yyyy-MM-dd'T'HH:mm:ss"));	
							} else {
								map.put(thisKey, DateUtils.dateToString((Date)value, "yyyy-MM-dd HH:mm:ss"));									
							}
						} else {
							map.put(thisKey, String.valueOf(value));
						}
					} else {
						String className = value.getClass().getSimpleName();
						if("BigInteger".equals(className)){
							map.put(thisKey, toLong(value));	
						} else if("BigDecimal".equals(className)){
							map.put(thisKey, toDouble(value));	
						} else {
							map.put(thisKey, value);						
						}
					}
				} else {
					map.put(thisKey, null);
				}
				
			}
			list.add(map);					
		}
		return list;
	}
	
	/**
	 * 将nativeQuery的结果转为Map
	 * @param sqlResults - nativeQuery 查询结果
	 * @param keyStr - 对应map中的各个字段名有序拼串
	 * @return
	 */
	public static Map<String, Object> sqlResultToMap(List<Object[]> sqlResults, String keyStr){
		Map<String, Object> map = new HashMap<>();
		if(sqlResults == null || sqlResults.isEmpty()){
			return map;
		}
		
		String[] keys = keyStr.split(",");
		Object[] objs = sqlResults.get(0);
		
		int count = Math.min(objs.length, keys.length);
		for(int i=0; i<count; i++){
			//将java.math.BigInteger等复杂类型转换简单类型
			Object value = objs[i];
			String thisKey = trim(keys[i]);
			if(value != null){
				String className = value.getClass().getSimpleName();
				if("BigInteger".equals(className)){
					map.put(thisKey, toLong(value));	
				} else if("BigDecimal".equals(className)){
					map.put(thisKey, toDouble(value));	
				} else {
					map.put(thisKey, value);						
				}
			} else {
				map.put(thisKey, null);
			}
		}
		return map;
	}
	
	public static String leftPad(Object obj, int len, char padding){
		return strPad(obj, len, padding, true);		
	}
	public static String rightPad(Object obj, int len, char padding){
		return strPad(obj, len, padding, false);		
	}
	public static String strPad(Object obj, int len, char padding, boolean left){
		String str = obj==null? "" : String.valueOf(obj);
		
		int diff = len - str.length();
		if(diff <= 0){
			return str;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<diff; i++){
			sb.append(padding);
		}
		
		if(left){
			return sb.append(str).toString();
		} else {
			return str+sb.toString();
		}		
	}

	/**
	 * 将name中间部分用星号代替
	 * @param str
	 * @return
	 */
	public static String starName(Object nameObj){
		if(nameObj == null){
			return null;
		}
		String name = nameObj.toString();
		if(StringUtils.isBlank(name)){
			return name;
		}
		int length = name.length();
		if(length == 1){
			return name;
		}
		if(length == 2){
			return subReplace(name, 1, 1, "***");
		}
		if(length == 11){
			return maskStr(name, "****");			
		}
		return maskStr(name, "***");
	}
	/**
	 * 将str中间的三分之一用****代替
	 * @param str
	 * @return
	 */
	public static String starStr(String str){
		return maskStr(str, "****");
	}
	/**
	 * 将str中间的三分之一用mask代替
	 * @param str
	 * @param mask
	 * @return
	 */
	public static String maskStr(String str, String mask){
		if(StringUtils.isBlank(str)){
			return str;
		}
		int length = str.length();
		if(length == 1){
			return str;
		}
		if(length == 2){
			return subReplace(str, 0, 0, mask);
		}
		if(StringUtils.isBlank(mask)){
			mask = "***";
		}
		if(length == 3 || length == 4){
			return subReplace(str, 1, 1, mask);
		}
		//分成3段，替换掉第二段
		int segment = length / 3;
		return subReplace(str, segment, 2 * segment, mask);
	}
	/**
	 * 截取前几位字符串
	 * @param content 截取的内容
	 * @param length  截取的长度
	 * @return
	 */
	public static String cutContent(String content,Integer length){
		String cutContent = "";
		if(StringUtils.isNotBlank(content)){
			if(content.length()>length){
				cutContent = content.substring(0,length)+"...";
			}else{
				cutContent = content;
			}
		}
		return cutContent;		
	}
	/**
	 * 从详情中截取分享内容
	 * @param content
	 * @param length
	 * @return
	 */
	public static String getShareDesc(String content, int length){
		if(StringUtils.isBlank(content)){
			return "";
		}
		return cutContent(content.replaceAll("<.+?>|\n|\r|&.+?;", ""), length);
	}
	
	/**
	 *  把list转换为一个用逗号分隔的字符串
	 * @param list
	 * @return
	 */
	public static String listToString(List<Object> list) {  
		StringBuilder sb = new StringBuilder();  
		if (list != null && list.size() > 0) {  
			for (int i = 0; i < list.size(); i++) {  
				if (i < list.size() - 1) {  
					sb.append(list.get(i) + ",");  
				} else {  
					sb.append(list.get(i));  
				}  
			}  
		}  
		return sb.toString();  
	}  
	
	
	/**
	 * 
	 * @param list 获取内容的list
	 * @param length  去list的长度
	 * @param key list中对象在map的key
	 * @param prefix  分隔符
	 * @return
	 */
	public static String listToString(List<Map<String,Object>> list,int length,String key,String prefix){
		String result = "";
		if(list == null || list.size()==0){
			return null;
		}
		int listsize = list.size();
		if(listsize<=length){
			for(int i=0;i<listsize;i++){
				if(i==listsize-1){
					result += list.get(i).get(key);
				}else{
					result += list.get(i).get(key)+prefix;
				}
			}
		}else{
			for(int i=0;i<length;i++){
				if(i==length-1){
					result += list.get(i).get(key)+"...";
				}else{
					result += list.get(i).get(key)+prefix;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param map
	 * @return key1=1&key2=true
	 */
	public static String map2QueryString(Map<String, Object> map){
		if(map==null ||map.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, Object> entry : map.entrySet()){
			sb.append("&"+entry.getKey()+"="+URLEncoder(toString(entry.getValue(), "")));			
		}
		return sb.substring(1);
	}
	/**
	 *  url后面增加GET请求参数对
	 * @param url
	 * @param kvPairs
	 * @return url?kvPairs 或 url&kvPairs
	 */
	public static String addQueryParam(String url, String kvPairs){
		if(StringUtils.isBlank(url) || StringUtils.isBlank(kvPairs) ){
			return url;
		}
		if(url.contains("?")){
			return url + "&" + kvPairs;
		} else {
			return url + "?" + kvPairs;
		}
	} 
	
	/**
	 *  url后面增加一个GET请求参数
	 * @param url
	 * @param key
	 * @param value
	 * @return
	 */
	public static String addQueryParam(String url, String key, Object value){
		if(StringUtils.isBlank(url)){
			return url;
		}
		String valueStr = URLEncoder(toString(value));
		if(url.contains("?")){
			return url + "&" + key + "=" + valueStr;
		} else {
			return url + "?" + key + "=" + valueStr;
		}
	}
	public static JSONObject toJson(Object obj){
    	if(obj == null){
    		return new JSONObject();
    	}
    	String str = obj.toString();
    	if(isBlank(str)){
    		return new JSONObject();    		
    	}
    	try{
    		JSONObject json = JSON.parseObject(str);
    		return json;
    	}catch(Exception e){
    		return new JSONObject();
    	}    	
    }
	/**
	 * 四舍五入
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static double toRound(Object value,Double defaultValue) {
		if(value == null){
			return defaultValue;
		}
        BigDecimal bg = new BigDecimal(String.valueOf(value));
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
    }
	public static boolean isNum(String str){
		 if(StringUtils.isBlank(str)){
			 return false;
		 }
		 if(str.matches("^[0-9]*$")){
			 return true;
		 }
		 return false;
	 }
	/***
	 * 去掉list中重复的元素
	 * @param list
	 * @return
	 */
	public static <T> List<T> removeDuplicate(List<T> list){
		HashSet<T> h  =   new  HashSet<T>(list); 
	    list.clear(); 
	    list.addAll(h); 
		return list;
	}
	/**
	 * 截取前几位字符串
	 * @param content 截取的内容
	 * @param cutStr  被截取的字符串
	 * @return
	 */
	public static String cutContent(String content,String cutStr){
		String cutContent = "";
		if(StringUtils.isNotBlank(content) && StringUtils.isNotBlank(cutStr)){
			int loc = content.indexOf(cutStr);
			if(loc>-1){
				cutContent = content.substring(0,loc);
			}else{
				cutContent = content;
			}
		}
		return cutContent;		
	}
	
	/**
	 * 去掉HTML标签
	 * @param content
	 * @param trybetter 是否尝试将br p &nbsp;替换为空白
	 * @return
	 */
	public static String trimHtmlTag(String content, boolean trybetter){
		if(StringUtils.isBlank(content)){
			return content;
		}
		String str = content;
		if(trybetter){
			str = Pattern.compile("<br\\s*/?>", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(" ");
			str = Pattern.compile("</?\\s*p\\s*>", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(" ");
			str = Pattern.compile("&nbsp;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(" ");
			str = Pattern.compile("&lt;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("<");
			str = Pattern.compile("&gt;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(">");
			str = Pattern.compile("&amp;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("&");
			str = Pattern.compile("&quot;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("\"");
			str = Pattern.compile("&apos;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("'");
			str = Pattern.compile("&yen;", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("¥");		
		}
		str = str.replaceAll("<.+?>|\n|\r|&.+?;", "");
		//str = str.replaceAll("<.+?>|&.+?;", "");
		str = trim(str);
		return str;
	}
}

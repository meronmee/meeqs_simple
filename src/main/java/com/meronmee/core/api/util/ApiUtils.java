package com.meronmee.core.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * @author Meron
 *
 */
public class ApiUtils { 
	/**
	 * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 *
	 * <pre>
	 * isBlank(null)      = true
	 * isBlank("")        = true
	 * isBlank(" ")       = true
	 * isBlank("bob")     = false
	 * isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs  the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}

		if("null".contentEquals(cs)){
			return true;
		}

		return true;
	}

	/**
	 * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
	 *
	 * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 *
	 * <pre>
	 * isNotBlank(null)      = false
	 * isNotBlank("")        = false
	 * isNotBlank(" ")       = false
	 * isNotBlank("bob")     = true
	 * isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs  the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is
	 *  not empty and not null and not whitespace only
	 * @since 2.0
	 * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}
    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int newCodePoints[] = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
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
	 * @param objs
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
	 * @param ids
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
	 * <p>Joins the elements of the provided {@code Iterator} into
	 * a single String containing the provided elements.</p>
	 *
	 * <p>No delimiter is added before or after the list.
	 * A {@code null} separator is the same as an empty String ("").</p>
	 *
	 * <p>See the examples here: {@link #join(Object[],String)}. </p>
	 *
	 * @param iterator  the {@code Iterator} of values to join together, may be null
	 * @param separator  the separator character to use, null treated as ""
	 * @return the joined String, {@code null} if null iterator input
	 */
	public static String join(final Iterator<?> iterator, final String separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return "";
		}
		final Object first = iterator.next();
		if (!iterator.hasNext()) {
			final String result = Objects.toString(first, "");
			return result;
		}

		// two or more elements
		final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			final Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

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
		if(isBlank(ext)){
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
		if(isNotBlank(str)){
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
		if(isNotBlank(str)){
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
		if(isNotBlank(strs)){
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
		if(isBlank(list1) || isBlank(list2)){
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
		if(isBlank(list1) && isBlank(list2)){
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
		return join(set, ",");
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
			if(isNotBlank(element)){
				formatedSet1.add(element.trim().toLowerCase());
			}
		}
		Set<String> formatedSet2 = new HashSet<String>();
		for(String element : set2){
			if(isNotBlank(element)){
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
			if(isNotBlank(element)){
				formatedSet1.add(element.trim().toLowerCase());
			}
		}
		Set<String> formatedSet2 = new HashSet<String>();
		for(String element : set2){
			if(isNotBlank(element)){
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
			if(isNotBlank(id)){
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
	 * 判断是否是八种基本类型数组，是的话返回元素类型，不是的话返回null
	 * @param array
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
			if(isNotBlank(string)){
				sb.append(connector).append(string.trim());
			}
		}
		String result = sb.toString().replaceAll("^"+connector+"+|"+connector+"+$", "");
		return result;
	}
	 /**
	 * 用connector连接成字符串，并且忽略空串，去除空白
	 * @param connector
	 * @param objs
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
	 * 将 4 个字节的字符（如：未经过处理的表情符）转换掉
	 * @param str
	 * @param replacement
	 * @return
	 */
	public static String trans4bytesChar(String str, String replacement){
		if(isBlank(str)){
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
		if(isBlank(str)){
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
		if(isBlank(str)){
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
		if(isBlank(codeList) || isBlank(nameList)){
			return "";
		}
		if(isBlank(str)){
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
	 * @param defaultValue
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

	public static <K,V> boolean isNotEmpty(Map<K, V> m){
		return !isEmpty(m);
	}

	/**
	 * 检测邮箱地址是否合法
	 * @param email
	 * @return true合法 false不合法
	 */
	public static boolean isEmail(String email){
	  if(isBlank(email)) return false;
	  email = email.trim();
	  Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
	  Matcher m = p.matcher(email);
	  return m.matches();
	 } 
	
    /**
     * <pre>
     * 按顺序替换字符串模板中的占位符,占位符使用{}
     * 如：xxxx{}yyy{}zzz,
     * </pre>
     * @param template - 字符串模板
     * @param values - 替换后的值顺序列表
     * @return
     */
    public static String format(String template, Object... values){
        if(isBlank(template)){
            return "";
        }
        String format = template.replaceAll("\\{\\s*\\d*\\s*\\}", "%s");

        int count = countMatches(format, "%s");
        if(count > values.length){
            Object[] newValues = Arrays.copyOf(values, count);
            Arrays.fill(newValues, values.length, count, "");
            return String.format(format, newValues);
        } else {
            return String.format(format, values);
        }
    }
    /**
     * <p>Counts how many times the substring appears in the larger string.</p>
     *
     * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
     *
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param sub  the substring to count, may be null
     * @return the number of occurrences, 0 if either CharSequence is {@code null}
     * @since 3.0 Changed signature from countMatches(String, String) to countMatches(CharSequence, CharSequence)
     */
    public static int countMatches(final String str, final String sub) {
        if (isBlank(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
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
			if(isBlank(str)){
				return "";
			}
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}
	public static String URLDecoder(String str){
		try {
			if(isBlank(str)){
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
		 if(isBlank(strData)) {
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
	 
	 public static boolean isMobilePhone(String phone){
		 if(isBlank(phone)){
			 return false;
		 }
		 if(phone.matches("^1\\d{10}$")){//有效的11位手机号
			 return true;
		 }
		 return false;
	 }
	 public static boolean isMobilePhone_new(String phone){
		 if(isBlank(phone)){
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
		 if(isBlank(str)){
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
		 if(isBlank(str)){
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
		if(isBlank(fromKeys)){
			return to;
		}
		if(isBlank(toKeys)){
			toKeys = fromKeys;
		}
		String [] fromKeyArr = fromKeys.split(",");
		String [] toKeyArr = toKeys.split(",");
		int len = Math.min(fromKeyArr.length, toKeyArr.length);
		for(int i=0; i<len; i++){
			String fromKey = fromKeyArr[i];
			String toKey = toKeyArr[i];
			if(isBlank(fromKey) || isBlank(toKey)){
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
		if(isBlank(str)){
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
		if(isBlank(list)){
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
		if(isBlank(list)){
			return list;
		}
		String str = String.valueOf(element).trim();
		if(isBlank(str)){
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
		if(isBlank(keys)){
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
		String url = join(parts, "/");
		return url.replaceAll("://", ":##").replaceAll("/+", "/").replaceAll(":##", "://");
	}

	/**
	 * 合并为一个文件路径
	 * @param parts
	 * @return
	 */
	public static String mergePath(String...parts){
		String path = join(parts, File.separator);
		return path.replaceAll("\\\\+", "/").replaceAll("/+", "\\"+File.separator);
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
	 * @param nameObj
	 * @return
	 */
	public static String starName(Object nameObj){
		if(nameObj == null){
			return null;
		}
		String name = nameObj.toString();
		if(isBlank(name)){
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
		if(isBlank(str)){
			return str;
		}
		int length = str.length();
		if(length == 1){
			return str;
		}
		if(length == 2){
			return subReplace(str, 0, 0, mask);
		}
		if(isBlank(mask)){
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
		if(isNotBlank(content)){
			if(content.length()>length){
				cutContent = content.substring(0,length)+"...";
			}else{
				cutContent = content;
			}
		}
		return cutContent;
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
		 if(isBlank(str)){
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
		if(isNotBlank(content) && isNotBlank(cutStr)){
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
		if(isBlank(content)){
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



    /**
     * 目标版本号appversion是否高于或等于起始版本号startVersion
     * @param startVersion 起始版本号，如：ios2.3.0或2.3.0
     * @param appversion 目标版本号，如：ios2.4.0或2.4.0
     * @return
     */
    public static boolean versionGe(String startVersion, String appversion){
        if(isBlank(startVersion) || isBlank(appversion)){
            return false;
        }

        String version = appversion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "").replaceAll(" ", "");
        startVersion = startVersion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "").replaceAll(" ", "");

        if(version.equalsIgnoreCase(startVersion)){
            return true;
        }

        String[] versionParts = version.split("\\.");
        String[] startVersionParts = startVersion.split("\\.");
        for(int i=0; i<versionParts.length; i++){
            int appverPart = toInteger(versionParts[i], 0);
            int startVersionPart = 0;
            if(startVersionParts.length > i){
                startVersionPart = toInteger(startVersionParts[i], 0);
            }
            if(appverPart > startVersionPart){
                return true;
            } else if(appverPart < startVersionPart){
                return false;
            }
        }
        return false;
    }
    
	/**
	 *
	 * 方法描述:<br/> 获取随即的32位UUID
	 *
	 * @author huanghyd
	 * @return
	 */
	public static String getRadomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 过滤字符串中的空格和换行
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String formatList(String list){
		if(isBlank(list)){
			return "";
		}
		return list.replaceAll("\\s*,\\s*",",").replaceAll(",+",",").replaceAll("^,+|,+$","");
	}

	/**
	 * 将日期转化为日期字符串。失败返回null。
	 * @param date 日期
	 * @param parttern 日期格式
	 * @return 日期字符串
	 */
	public  static String DateToString(Date date, String parttern) {
		String dateString = null;
		if (date != null) {
			try {
				dateString = new SimpleDateFormat(parttern).format(date);
			} catch (Exception e) {
			}
		}
		return dateString;
	}

	public static boolean listContains(String bigList, String smallList){
		if(isBlank(bigList) || isBlank(smallList)){
			return false;
		}
		String bigListPlus = ","+bigList+",";

		String[] smallArr = smallList.split(",");
		for(String thisStr : smallArr){
			if(isBlank(thisStr)){
				continue;
			}

			//if(bigListPlus.indexOf(","+thisStr.trim()+",") == -1){
			if(!bigListPlus.matches("^.*,\\s*"+thisStr.trim()+"\\s*,.*$")){
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断两个list是否有共同元素
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean hasSameElement(String list1, String list2){
		if(isBlank(list1) || isBlank(list2)){
			return false;
		}
		String[] arr1 = list1.split(",");
		String list2Plus = ","+list2+",";
		for(String thisStr : arr1){
			if(isBlank(thisStr)){
				continue;
			}

			//if(list2Plus.indexOf(","+thisStr.trim()+",") != -1){
			if(list2Plus.matches("^.*,\\s*"+thisStr.trim()+"\\s*,.*$")){
				return true;
			}
		}
		return false;
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
			if(json == null){
				return new JSONObject();
			} else {
				return json;
			}
		}catch(Exception e){
			return new JSONObject();
		}
	}
    public static JSONArray toJsonArray(Object obj){
    	if(obj == null){
    		return new JSONArray();
    	}
    	try{
    		JSONArray json = JSON.parseArray(obj.toString());
			if(json == null){
				return new JSONArray();
			} else {
				return json;
			}
    	}catch(Exception e){
    		return new JSONArray();
    	}    	
    }

	public static String decode(String s) {
		String ret = s;
		try {
			ret = URLDecoder.decode(s.trim(), "UTF-8");
		} catch (Exception localException) {
		}
		return ret;
	}

	public static String encode(String s) {
		String ret = s;
		try {
			ret = URLEncoder.encode(s.trim(), "UTF-8");
		} catch (Exception localException) {
		}
		return ret;
	}


	/**
	 *
	 * @param idsStr - id列表
	 * @param spilter - 分割符
	 * @return
	 */
	public static Long[] getLongs(String intStr, String spilter){
		Long[] ints = {};
		if(isNotBlank(intStr)){
			String[] parts = intStr.trim().split(spilter);
			List<Long> result = new ArrayList<Long>();
			for (int i = 0; i < parts.length; i++) {
				if(isBlank(parts[i]) || "null".equalsIgnoreCase(parts[i].trim())){
					continue;
				}
				try{
					Long intVal = toLong(parts[i].trim());
					if(intVal != null && !result.contains(intVal)){
						result.add(intVal);
					}
				} catch(Exception e){}

				//result.add(Integer.parseInt(idInfo[i].trim()));
			}
			ints = result.toArray(new Long[result.size()]);
		}
		return ints;
	}

	public static Long[] getLongArray(String longStr){
		return getLongs(longStr, ",");
	}

	public static String encodeURIComponent(String s) {
		String result = null;
		try {
			result = URLEncoder.encode(s, "UTF-8")
					.replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	} 

	/*---------------DateTime---------------*/

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
			return dateToString((Date)date, format);
		} else if("Long".equals(clazz)){
			return dateToString(new Date((Long)date), format);
		} else if("String".equals(clazz)){
			return (String)date;
		}
		return "";
	}
	
	public static String showTime(Date date) {
		if(date==null){
			return "";
		}
		
		long dateTime = date.getTime(); 		
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

		return shortDate(date);
	}
	 	
	/**
	 * 返回简洁的日期
	 * @param dateTime
	 */
	public static String shortDate(Date dateTime) {
		Date now = new Date();
		if(isSameDay(now, dateTime)){//同一天
			return dateToString(dateTime, "HH:mm");
		}
		if(isSameYear(now, dateTime)){//同一年
			return dateToString(dateTime, "MM-dd HH:mm");
		}
		return dateToString(dateTime, "yyyy-MM-dd HH:mm");
	} 
	
	/**
     * 判断是否是同一天
     *
     * @param date1
     * @param date2
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        return getYear(calendar1) == getYear(calendar2)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }
    /**
     * 判断是否是同一年
     *
     * @param date1
     * @param date2
     */
    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        return getYear(calendar1) == getYear(calendar2);
    }
    /**
     * 获取 date 对应的日历
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 获取指定日历对应的年份
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }
    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date     日期
     * @param parttern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String parttern) {
        String dateString = "";
        if (date != null) {
            try {
                dateString = new SimpleDateFormat(parttern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    //----------反射相关--------------

    /**
     * 优先使用Getter方法读取字段值，如果失败再直接读取(无视private/protected修饰符)
     */
    public static Object getFieldValue(final Object object, final Field field) {
        if(field == null){
            return null;
        }
        String fieldName = field.getName();

        try{
            Object value = null;
            String cFieldName = capitalize(fieldName);//首字母大写
            String methodName = "get"+cFieldName;
            Method getter =  getDeclaredMethod(object, methodName);
            if(getter == null){//boolean型
                methodName = "is"+cFieldName;
                getter = getDeclaredMethod(object, methodName);
            }
            if(getter == null){//没有Getter方法
                value = null;
            } else {//调用Getter方法
                try{
                    value = getter.invoke(object);
                } catch(Exception e){
                    e.printStackTrace();
                    value = null;
                }
            }

            //Getter方法获取字段的值失败，再尝试直接读取
            if(value == null){
                value = getFieldValueDirectly(object, field);
            }
            return value;
        } catch(Exception e){
            return null;
        }
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValueDirectly(final Object object, final Field field) {
        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param field the field to make accessible
     * @see java.lang.reflect.Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     */
    public static Method getDeclaredMethod(final Object object, final String methodName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                //e.printStackTrace();
            }
        }
        return null;
    }

}

package com.meronmee.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UrlPathHelper;


/**
 * HttpServletRequest帮助类
 */
public class RequestUtils {
	private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);
	private static final String UTF8 = "UTF-8";
	private static final String POST = "POST";
	/**
	 * cookie中的JSESSIONID名称
	 */
	private static final String JSESSION_COOKIE = "JSESSIONID";
	/**
	 * url中的jsessionid名称
	 */
	private static final String JSESSION_URL = "jsessionid";
	
	  /**Wap网关Via头信息中特有的描述信息*/
	  private static String mobileGateWayHeaders[]=new String[]{
		  "ZXWAP",//中兴提供的wap网关的via信息，例如：Via=ZXWAP GateWayZTE Technologies，
		  "chinamobile.com",//中国移动的诺基亚wap网关，例如：Via=WTP/1.1 GDSZ-PB-GW003-WAP07.gd.chinamobile.com (Nokia WAP Gateway 4.1 CD1/ECD13_D/4.1.04)
		  "monternet.com",//移动梦网的网关，例如：Via=WTP/1.1 BJBJ-PS-WAP1-GW08.bj1.monternet.com. (Nokia WAP Gateway 4.1 CD1/ECD13_E/4.1.05)
		  "infoX",//华为提供的wap网关，例如：Via=HTTP/1.1 GDGZ-PS-GW011-WAP2 (infoX-WISG Huawei Technologies)，或Via=infoX WAP Gateway V300R001 Huawei Technologies
		  "XMS 724Solutions HTG",//国外电信运营商的wap网关，不知道是哪一家
		  "wap.lizongbo.com",//自己测试时模拟的头信息
		  "Bytemobile",//貌似是一个给移动互联网提供解决方案提高网络运行效率的，例如：Via=1.1 Bytemobile OSN WebProxy/5.1
	  };
	  /**电脑上的IE或Firefox浏览器等的User-Agent关键词*/
	  private static String[] pcHeaders=new String[]{
		  "Windows 98",
		  "Windows ME",
		  "Windows 2000",
		  "Windows XP",
		  "Windows NT",
		  "Ubuntu"
	  };
	  /**手机浏览器的User-Agent里的关键词*/
	  private static String[] mobileUserAgents=new String[]{
		  "iPhone",//iPhone是否也转wap？不管它，先区分出来再说。Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_1 like Mac OS X; zh-cn) AppleWebKit/532.9 (KHTML like Gecko) Mobile/8B117
		  "iPad",//iPad的ua，Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; zh-cn) AppleWebKit/531.21.10 (KHTML like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10
		  "ios",
		  "iPad",
		  "Android",//Android是否也转wap？Mozilla/5.0 (Linux; U; Android 2.1-update1; zh-cn; XT800 Build/TITA_M2_16.22.7) AppleWebKit/530.17 (KHTML like Gecko) Version/4.0 Mobile Safari/530.17
		  "MicroMessenger", //微信
		  "Nokia",//诺基亚，有山寨机也写这个的，总还算是手机，Mozilla/5.0 (Nokia5800 XpressMusic)UC AppleWebkit(like Gecko) Safari/530
		  "SAMSUNG",//三星手机 SAMSUNG-GT-B7722/1.0+SHP/VPP/R5+Dolfin/1.5+Nextreaming+SMM-MMS/1.2.0+profile/MIDP-2.1+configuration/CLDC-1.1
		  "MIDP-2",//j2me2.0，Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2 NokiaE75-1 /110.48.125 Profile/MIDP-2.1 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML like Gecko) Safari/413
		  "CLDC1.1",//M600/MIDP2.0/CLDC1.1/Screen-240X320
		  "SymbianOS",//塞班系统的，
		  "MAUI",//MTK山寨机默认ua
		  "UNTRUSTED/1.0",//疑似山寨机的ua，基本可以确定还是手机
		  "Windows CE",//Windows CE，Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 7.11)
		  "BlackBerry",//BlackBerry8310/2.7.0.106-4.5.0.182
		  "UCWEB",//ucweb是否只给wap页面？ Nokia5800 XpressMusic/UCWEB7.5.0.66/50/999
		  "ucweb",//小写的ucweb貌似是uc的代理服务器Mozilla/6.0 (compatible; MSIE 6.0;) Opera ucweb-squid
		  "BREW",//很奇怪的ua，例如：REW-Applet/0x20068888 (BREW/3.1.5.20; DeviceId: 40105; Lang: zhcn) ucweb-squid
		  "J2ME",//很奇怪的ua，只有J2ME四个字母
		  "YULONG",//宇龙手机，YULONG-CoolpadN68/10.14 IPANEL/2.0 CTC/1.0
		  "YuLong",//还是宇龙
		  "COOLPAD",//宇龙酷派YL-COOLPADS100/08.10.S100 POLARIS/2.9 CTC/1.0
		  "TIANYU",//天语手机TIANYU-KTOUCH/V209/MIDP2.0/CLDC1.1/Screen-240X320
		  "TY-",//天语，TY-F6229/701116_6215_V0230 JUPITOR/2.2 CTC/1.0
		  "K-Touch",//还是天语K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
		  "Haier",//海尔手机，Haier-HG-M217_CMCC/3.0 Release/12.1.2007 Browser/WAP2.0
		  "DOPOD",//多普达手机
		  "Lenovo",// 联想手机，Lenovo-P650WG/S100 LMP/LML Release/2010.02.22 Profile/MIDP2.0 Configuration/CLDC1.1
		  "LENOVO",// 联想手机，比如：LENOVO-P780/176A
		  "HUAQIN",//华勤手机
		  "AIGO-",//爱国者居然也出过手机，AIGO-800C/2.04 TMSS-BROWSER/1.0.0 CTC/1.0
		  "CTC/1.0",//含义不明
		  "CTC/2.0",//含义不明
		  "CMCC",//移动定制手机，K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223 Release/30.07.2008 Browser/WAP2.0
		  "DAXIAN",//大显手机DAXIAN X180 UP.Browser/6.2.3.2(GUI) MMP/2.0
		  "MOT-",//摩托罗拉，MOT-MOTOROKRE6/1.0 LinuxOS/2.4.20 Release/8.4.2006 Browser/Opera8.00 Profile/MIDP2.0 Configuration/CLDC1.1 Software/R533_G_11.10.54R
		  "SonyEricsson",// 索爱手机，SonyEricssonP990i/R100 Mozilla/4.0 (compatible; MSIE 6.0; Symbian OS; 405) Opera 8.65 [zh-CN]
		  "GIONEE",//金立手机
		  "HTC",//HTC手机
		  "ZTE",//中兴手机，ZTE-A211/P109A2V1.0.0/WAP2.0 Profile
		  "HUAWEI",//华为手机，
		  "webOS",//palm手机，Mozilla/5.0 (webOS/1.4.5; U; zh-CN) AppleWebKit/532.2 (KHTML like Gecko) Version/1.0 Safari/532.2 Pre/1.0
		  "GoBrowser",//3g GoBrowser.User-Agent=Nokia5230/GoBrowser/2.0.290 Safari
		  "IEMobile",//Windows CE手机自带浏览器，
		  "WAP2.0"//支持wap 2.0的
	  };
	  /**
	  * 根据当前请求的特征，判断该请求是否来自手机终端，主要检测特殊的头信息，以及user-Agent这个header
	  * @param request http请求
	  * @return 如果命中手机特征规则，则返回对应的特征字符串
	  */
	  public static boolean isMobile(HttpServletRequest request){
	    boolean pcFlag = false;
	    boolean mobileFlag = false;
	    String via = request.getHeader("Via");
	    String userAgent = request.getHeader("user-agent");
	    
	    for (int i = 0; via!=null && !via.trim().equals("") && i < mobileGateWayHeaders.length; i++) {
	      if(via.contains(mobileGateWayHeaders[i])){
	        mobileFlag = true;
	        break;
	      }
	    }
	    for (int i = 0;!mobileFlag && userAgent!=null && !userAgent.trim().equals("") && i < mobileUserAgents.length; i++) {
	      if(userAgent.contains(mobileUserAgents[i])){
	        mobileFlag = true;
	        break;
	      }
	    }
	    for (int i = 0; userAgent!=null && !userAgent.trim().equals("") && i < pcHeaders.length; i++) {
	      if(userAgent.contains(pcHeaders[i])){
	        pcFlag = true;
	      }
	    }
	    if(mobileFlag==true && mobileFlag!=pcFlag){
	      return true;
	    }
	    return false;
	  }
	
	/**
	 * 获取请求参数
	 * 
	 * @param request web请求
	 * @param name 参数名称
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String name) {
		return getParam(request, name, null);
	}
	
	/**
	 * 获取请求参数<p>
	 * 优先读取QueryString的第一个，没有再读取Body中的参数，为空再使用默认值
	 * 
	 * @param request web请求
	 * @param name 参数名称
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String name, String defaultValue) {
		if (StringUtils.isBlank(name) || request == null) {
			return defaultValue;
		}
		
		String value = request.getParameter(name);		
		return getFirstNotBlank(value, defaultValue);
	}
	


	/**
	 * 从各个途径获取请求参数
	 * querystring-body-header-cookie-requestAttr-sessionAttr
	 * @param request web请求
	 * @param name 参数名称,多个别名用逗号隔开。
	 * @return
	 */
	public static String getParamHard(HttpServletRequest request, String name) {
		if(StringUtils.isBlank(name)){
		    return  null;
        }
        String[] names = name.split(",");

		String value = null;

		//通过getParameter 获取
        for(String thisName : names){
            value = request.getParameter(thisName);
            if(StringUtils.isNotBlank(value)){
                return value;
            }
        }

        //通过 getParameter 获取
        for(String thisName : names){
            value = request.getHeader(thisName);
            if(StringUtils.isNotBlank(value)){
                return value;
            }
        }

        //通过 cookie 获取
        for(String thisName : names){
            value = CookieUtils.getCookieValue(request, thisName);
            if(StringUtils.isNotBlank(value)){
                return value;
            }
        }

        //通过 request attrbute 获取
        for (String thisName : names) {
            value = BaseUtils.toString(request.getAttribute(thisName));
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }

        //通过 session attrbute 获取
        HttpSession session = request.getSession(false);
        if(session != null) {
            for (String thisName : names) {
                value = BaseUtils.toString(session.getAttribute(thisName));
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
            }
        }

        return value;
	}
	
	/**
	 * 获取QueryString中的请求参数<p>
	 * 优先读取QueryString的第一，为空再第二个，...
	 * 
	 * @param request web请求
	 * @param name 参数名称
	 * @return
	 */
	public static String getQueryParam(HttpServletRequest request, String name) {
		return getQueryParam(request, name, null);
	}
	/**
	 * 获取QueryString中的请求参数<p>
	 * 优先读取QueryString的第一，为空再第二个，最终为空再使用默认值
	 * 
	 * @param request web请求
	 * @param name 参数名称
	 * @param defaultValue 默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getQueryParam(HttpServletRequest request, String name, String defaultValue) {
		if (StringUtils.isBlank(name) || request == null) {
			return defaultValue;
		}
		
		//先检查Request中缓存的queryStringMap
		Map<String, String[]> queryStringMap = (Map<String, String[]>)request.getAttribute("__queryStringMap");
		if(queryStringMap == null){
			String s = request.getQueryString();
			if (StringUtils.isBlank(s)) {
				return defaultValue;
			}
			try {
				s = URLDecoder.decode(s, UTF8);
			} catch (UnsupportedEncodingException e) {
				log.error("encoding " + UTF8 + " not support?", e);
				return defaultValue;
			}
			
			queryStringMap = parseQueryString(s);
			if(BaseUtils.isNotEmpty(queryStringMap)){
				request.setAttribute("__queryStringMap", queryStringMap);
			}
		}
		
		if(BaseUtils.isEmpty(queryStringMap)){
			return defaultValue;			
		}
				
		String[] values = queryStringMap.get(name);
		if (values != null && values.length > 0) {			
			String value = getFirstNotBlank(values);
			if(StringUtils.isNotBlank(value)){
				return value;
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}
	

	/**
	 * 获取String类型请求参数
	 * @param request web请求
	 * @param name 参数名称
	 * @return
	 */
	public static String getStringParam(HttpServletRequest request, String name) {
		return getParam(request, name);
	}
	/**
	 * 获取String类型请求参数
	 * @param request web请求
	 * @param name 参数名称
	 * @param defaultValue  默认值
	 * @return
	 */
	public static String getStringParam(HttpServletRequest request, String name, String defaultValue){
		return getParam(request, name, defaultValue);
	}

	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Integer getIntegerParam(HttpServletRequest request, String name, Integer defaultValue){
		String value = getParam(request, name);
		Integer result = BaseUtils.toInteger(value);
		return result != null ? result : defaultValue;
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Integer getIntegerParam(HttpServletRequest request, String name) {
		return getIntegerParam(request, name, null);
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Long getLongParam(HttpServletRequest request, String name, Long defaultValue){
		String value = getParam(request, name);
		Long result = BaseUtils.toLong(value);
				
		return result != null ? result : defaultValue;
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Long getLongParam(HttpServletRequest request, String name) {
		return getLongParam(request, name, null);
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Float getFloatParam(HttpServletRequest request, String name, Float defaultValue){
		String value = getParam(request, name);
		if(value == null || "".equals(value)){
			return defaultValue;
		}
		Float result = BaseUtils.toFloat(value);
		return result != null ? result : defaultValue;
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Float getFloatParam(HttpServletRequest request, String name) {
		return getFloatParam(request, name, null);
	}
	
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Double getDoubleParam(HttpServletRequest request, String name) {
		String data = getStringParam(request, name);
		if(BaseUtils.isBlank(data)){
			return null;
		}else{
			return BaseUtils.toDouble(data);
		}
	}
	
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Double getDoubleParam(HttpServletRequest request, String name, Double defaultValue) {
		String data = getStringParam(request, name, defaultValue.toString());
		if(BaseUtils.isBlank(data)){
			return null;
		}else{
			return BaseUtils.toDouble(data);
		}
	}
	
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Boolean getBooleanParam(HttpServletRequest request, String name, Boolean defaultValue){
		String value = getParam(request, name);
		Boolean result = BaseUtils.toBoolean(value);
		return result != null ? result : defaultValue;
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Boolean getBooleanParam(HttpServletRequest request, String name) {
		return getBooleanParam(request, name, null);
	}
	
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Date getDateParam(HttpServletRequest request, String name, Date defaultValue){
		String value = getParam(request, name);
		Date result = BaseUtils.toDate(value);
		return result != null ? result : defaultValue;
	}
	/**
	 * 获取QueryString的参数，并使用URLDecoder以UTF-8格式转码。如果请求是以post方法提交的，
	 * 那么将通过HttpServletRequest#getParameter获取。
	 * 
	 * @param request
	 *            web请求
	 * @param name
	 *            参数名称
	 * @return
	 */
	public static Date getDateParam(HttpServletRequest request, String name) {
		return getDateParam(request, name, null);
	}
	

	/**
	 * 获取文件参数
	 * @param request
	 * @param name
	 * @return
	 */
	public static MultipartFile getFileParam(HttpServletRequest request, String name){
		MultiFileMap map = getMultiFileMap(request);
		MultipartFile file = map.getFile(name);
		if(file != null && file.getSize() > 0){
			return file;
		}
		return null;
	}
	/**
	 * 获取文件参数
	 * @param request
	 * @param name
	 * @return
	 */
	public static List<MultipartFile> getFilesParam(HttpServletRequest request, String name){
		MultiFileMap map = getMultiFileMap(request);
		List<MultipartFile> temp = map.getFiles(name);
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		if(temp!=null && temp.size()>0){
			for(MultipartFile file : temp){
				if(file != null && file.getSize() > 0){
					files.add(file);
				}
			}
		}
		return files;
	}
	
	/**
	 * 解析文件请求为MultiFileMap
	 * @param request
	 * @param name
	 * @return
	 */
	public static MultiFileMap getMultiFileMap(HttpServletRequest request){	
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();
		MultiFileMap map = null;
		if(request instanceof MultipartHttpServletRequest){		
			try{
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
				for (Entry<String,List<MultipartFile>> entry : multiFileMap.entrySet()) {
					String key = entry.getKey();
					List<MultipartFile> files = entry.getValue();
					multipartFiles.put(key, files);
				}
				map = new MultiFileMap(multipartFiles);
			}catch(Exception e){
				log.error("解析MultipartHttpServletRequest出错", e);				
			}
		} else {
			map = new MultiFileMap(multipartFiles);
		}
		return map;
	}
	/**
	 * 获取文件请求中的第一个文件
	 * @param request
	 * @return
	 */
	public static MultipartFile getFirstFile(HttpServletRequest request){	
		MultipartFile file = null;
		if(request instanceof MultipartHttpServletRequest){		
			try{
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();		
				for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
					file = entry.getValue();
					if(file != null){
						return file;
					}
				}
			}catch(Exception e){
				log.error("解析MultipartHttpServletRequest出错", e);				
			}
		}
		return file;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQueryParams(HttpServletRequest request) {
		Map<String, String[]> map;
		if (request.getMethod().equalsIgnoreCase(POST)) {
			map = request.getParameterMap();
		} else {
			String s = request.getQueryString();
			if (StringUtils.isBlank(s)) {
				return new HashMap<String, Object>();
			}
			try {
				s = URLDecoder.decode(s, UTF8);
			} catch (UnsupportedEncodingException e) {
				log.error("encoding " + UTF8 + " not support?", e);
			}
			map = parseQueryString(s);
		}

		Map<String, Object> params = new HashMap<String, Object>(map.size());
		int len;
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			len = entry.getValue().length;
			if (len == 1) {
				params.put(entry.getKey(), entry.getValue()[0]);
			} else if (len > 1) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		return params;
	}

	/**
	 * 
	 * Parses a query string passed from the client to the server and builds a
	 * <code>HashTable</code> object with key-value pairs. The query string
	 * should be in the form of a string packaged by the GET or POST method,
	 * that is, it should have key-value pairs in the form <i>key=value</i>,
	 * with each pair separated from the next by a &amp; character.
	 * 
	 * <p>
	 * A key can appear more than once in the query string with different
	 * values. However, the key appears only once in the hashtable, with its
	 * value being an array of strings containing the multiple values sent by
	 * the query string.
	 * 
	 * <p>
	 * The keys and values in the hashtable are stored in their decoded form, so
	 * any + characters are converted to spaces, and characters sent in
	 * hexadecimal notation (like <i>%xx</i>) are converted to ASCII characters.
	 * 
	 * @param s
	 *            a string containing the query to be parsed
	 * 
	 * @return a <code>HashTable</code> object built from the parsed key-value
	 *         pairs
	 * 
	 * @exception IllegalArgumentException
	 *                if the query string is invalid
	 * 
	 */
	public static Map<String, String[]> parseQueryString(String s) {
		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException();
		}
		Map<String, String[]> ht = new HashMap<String, String[]>();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = (String) st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				continue;
			}
			String key = pair.substring(0, pos);
			String val = pair.substring(pos + 1, pair.length());
			if (ht.containsKey(key)) {
				String oldVals[] = (String[]) ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++) {
					valArray[i] = oldVals[i];
				}
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}
		return ht;
	}

	public static Map<String, String> getRequestMap(HttpServletRequest request,
			String prefix) {
		return getRequestMap(request, prefix, false);
	}

	public static Map<String, String> getRequestMapWithPrefix(
			HttpServletRequest request, String prefix) {
		return getRequestMap(request, prefix, true);
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getRequestMap(
			HttpServletRequest request, String prefix, boolean nameWithPrefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name, key, value;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				key = nameWithPrefix ? name : name.substring(prefix.length());
				value = StringUtils.join(request.getParameterValues(name), ',');
				map.put(key, value);
			}
		}
		return map;
	}
	/**
	 * 获取当前域名
	 * @param request
	 */
	public static String getDomain(HttpServletRequest request) {
	     String serverName = request.getServerName();
	     return serverName;
	}
	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。
	 * 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 * 
	 * @see HttpServletRequest#getRequestedSessionId()
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {
		String sid = request.getRequestedSessionId();
		String ctx = request.getContextPath();
		// 如果session id是从url中获取，或者部署路径为空，那么是在正确的。
		if (request.isRequestedSessionIdFromURL() || StringUtils.isBlank(ctx)) {
			return sid;
		} else {
			// 手动从cookie获取
			Cookie cookie = CookieUtils.getCookie(request, JSESSION_COOKIE);
			if (cookie != null) {
				return cookie.getValue();
			} else {
				return request.getSession().getId();
			}
		}
	}
	
	/**
	 * 获取 HttpServletRequest 
	 * @return HttpServletRequest
	 * @see web.xml#org.springframework.web.context.request.RequestContextListener
	 */
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(attributes != null){
			HttpServletRequest request = attributes.getRequest(); 
			return request;
		}
		return null;
	}
	
	/**
	 * 判断是否是Ajax请求
	 * @param req HttpServletRequest
	 * @return true or false
	 */
	public static boolean isAjaxRequest(HttpServletRequest req){		
		boolean isAjax = false;
		if(req.getAttribute("ajax") != null){
    		isAjax = true;
    	} else if("XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"))){
			isAjax = true;
		}else if("Ext.basex".equalsIgnoreCase(req.getHeader("X-Requested-With"))){
			isAjax = true;
		}else if("true".equalsIgnoreCase(req.getHeader("ajax")) || "".equalsIgnoreCase(req.getHeader("ajax"))){
			isAjax = true;
		}else if("true".equalsIgnoreCase(req.getParameter("ajax")) || "".equalsIgnoreCase(req.getParameter("ajax"))){
			isAjax = true;
		}else if("XMLHttpRequest".equalsIgnoreCase(req.getParameter("HTTP_X_REQUESTED_WITH")) 
				|| "true".equalsIgnoreCase(req.getParameter("HTTP_X_REQUESTED_WITH"))
				|| "".equalsIgnoreCase(req.getParameter("HTTP_X_REQUESTED_WITH"))){
			isAjax = true;
		} else {
			
		}
		return isAjax;
	}
	/**
	 * 获取当前页面的完整访问路径
	 * @param request
	 * @return 
	 */
	public static String getCurrentUrl(HttpServletRequest request){
		String url =  request.getRequestURL().toString();
		String queryString = "";
		if (StringUtils.isNotBlank(request.getQueryString())){
			queryString = "?" + request.getQueryString();
		}
		String currentUrl = url + queryString;
		return currentUrl;
	}

	
	
	/**
	 * 获取服务器Web基地址
	 * @param request
	 * @return http://localhost:8080/meeqs
	 */
	public static String getBaseURL(HttpServletRequest request) {	
		 String contextPath = request.getContextPath();
		 if(contextPath.equals("/")){
			 contextPath = "";
		 }
		 
	     String url = request.getScheme() + "://" + request.getServerName();
	     
	     if(!BaseUtils.isEqual(request.getServerPort(), 80)){
	    	 url = url + ":" + request.getServerPort() + contextPath;
	     } else {
	       url = url + contextPath;
	     }
	     
	     return url;
	}

	/**
	 * 获取服务器Web基地址
	 * @param request
	 * @return http://localhost:8080/meeqs
	 */
	public static String getBaseURL() {
		HttpServletRequest request = getRequest();
		return getBaseURL(request);
	}

	/**
	 * 判断是否是iOS
	 * @param request
	 * @return
	 */
	public static boolean isIOS(HttpServletRequest request){
		if(request == null){
			request = getRequest();
		}
		if(request == null){
			return false;
		}
		String appversion = getAppversion(request);
		if(StringUtils.isNotBlank(appversion)){
			if(appversion.startsWith("ios")){
				return true;
			} 
			return false;			
		} else {
			String userAgent = request.getHeader("user-agent");
			if(StringUtils.isNotBlank(userAgent)){
				String[] iosUserAgents = new String[]{
						  "iPhone",
						  "iPad",
						  "ios",
						  "iPad"
				};
				for (int i=0; i<iosUserAgents.length; i++) {
				      if(userAgent.contains(iosUserAgents[i])){
				    	  return true;
				      }
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 判断是否是Android
	 * @param request
	 * @return
	 */
	public static boolean isAndroid(HttpServletRequest request){
		if(request == null){
			request = getRequest();
		}
		if(request == null){
			return false;
		}
		String appversion = getAppversion(request);
		if(StringUtils.isNotBlank(appversion)){
			if(appversion.startsWith("android")){
				return true;
			} 
			return false;			
		} else {
			String userAgent = request.getHeader("user-agent");
			if(StringUtils.isNotBlank(userAgent)){
				String[] userAgents = new String[]{
						  "Android"
				};
				for (int i=0; i<userAgents.length; i++) {
				      if(userAgent.contains(userAgents[i])){
				    	  return true;
				      }
				}
			}
		}
		
		return false;
	}

	/**
	 * 获取用户orgId
	 * @param request
	 * @return
	 */
	public static String getOrgIdentity(HttpServletRequest request){
		Object id = request.getAttribute("orgId");
		if(id == null){
			 id = request.getHeader("orgid");
			 if(id == null){					
				 HttpSession session = request.getSession(false);
				 if(session != null){
					 id = session.getAttribute("orgId");
				 } 
				 if(id == null){
					 id = getStringParam(request, "orgId");
				 }	
				 if(id == null){
					 id = getStringParam(request, "orgid");
				 }				 
				 if(id == null || StringUtils.isBlank(String.valueOf(id))){
					 id = getStringParam(request, "_o");
				 }
			 }
		}
		return String.valueOf(id);
	}


	/**
	 * 判断是否是微信访问
	 * @param request
	 * @return
	 */
	public static boolean isWeixin(HttpServletRequest request){
		if(request == null){
			request = getRequest();
		}
		if(request == null){
			return false;
		}
		String userAgent = request.getHeader("user-agent");
		if(StringUtils.isNotBlank(userAgent) && userAgent.contains("MicroMessenger")){
		   return true;
		}
		
		if("wx".equalsIgnoreCase(getStringParam(request, "from"))){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断是否是app打开
	 * @param request
	 * @return
	 */
	public static boolean isApp(HttpServletRequest request){
		if(request == null){
			request = getRequest();
		}
		if(request == null){
			return false;
		}
		String isApp = getStringParam(request, "isApp");
		if(isApp != null){
			boolean result = false;
			if("1".equals(isApp)){
				result = true;
			} else if("true".equals(isApp)){
				result = true;
			} else {
				result = false;				
			}
			return result;
		}
		if(StringUtils.isNotBlank(request.getHeader("jyo2o")) 
				|| StringUtils.isNotBlank(getStringParam(request, "jyo2o"))
				|| StringUtils.isNotBlank(request.getHeader("zuqiuapp"))	
				|| StringUtils.isNotBlank(getStringParam(request, "zuqiuapp"))
				){
			return true;
		}

		if(!isMobile(request)){
			return false;
		}
		/*
		int srcApp = BaseUtils.toInteger(getSrcApp(request), 2);//1:足球, 2:一家
		if(srcApp == 2){//一家的不进行后续判断
			return false;
		}
		*/
		//足球的
		String userAgent = request.getHeader("User-Agent");
		if(StringUtils.isBlank(userAgent)){
			return false;
		}
		String useragent = userAgent.toLowerCase();
		String[] browser={"Sogou","UCWEB","UCBrowser","Opera","QQBrowser","TencentTraveler","MetaSr","360SE","MicroMessenger",
				"MQQBrowser","Maxthon","JUC","IUC","Xiaomi","360 Aphone Browser","QQ","baidu","Firefox",
				"TencentTraveler","UCWEB","opr","mxbrowser","liebaofast","sogoumobile","dolphin","safari", "chrome"}; 		
		for(String str:browser){
			if(useragent.indexOf(str.toLowerCase())>=0){
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 判断是否是app打开
	 * @param request
	 * @return ios,android,unknown
	 */
	public static String getClientType(HttpServletRequest request){
		String appversion = getAppversion(request);
		if(StringUtils.isNotBlank(appversion)){
			appversion = appversion.toLowerCase();
			if(appversion.indexOf("ios") != -1){
				return "ios";
			}
			if(appversion.indexOf("android") != -1){
				return "android";
			}
		}
		
		String useragent = request.getHeader("User-Agent");
		if(StringUtils.isNotBlank(useragent)){
			useragent = useragent.toLowerCase();
			if(appversion.indexOf("ios") != -1){
				return "ios";
			}
			if(appversion.indexOf("android") != -1){
				return "android";
			}
		}
		return "unknown";
	}
	
	/**
	 * 取得参数列表中第一个不为空的值
	 * @param strings
	 * @return
	 */
	public static String getFirstNotBlank(String ...strings){
		for(String str : strings){
			if(StringUtils.isNotBlank(str)){
				return str;
			}
		}
		
		return "";
	}
	/**
	 * 获取appversion
	 * @param request
	 * @return ios1.0.2
	 */
	public static String getAppversion(HttpServletRequest request){
		return BaseUtils.getFirstNotBlank(getStringParam(request, "appversion"), request.getHeader("appversion"), BaseUtils.toString(request.getAttribute("appversion"))).toLowerCase();// 如:ios1.0.2
	}
	/**
	 * 获取appversion
	 * @param request
	 * @return ios1.0.2
	 */
	public static String getVersion(HttpServletRequest request){
		String appversion = getAppversion(request);
		String version = appversion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "");
		
		return version;
	}
	/**
	 * 查看当前的版本号是否高于或等于给定的版本号appversion
	 * @param appversion
	 * @return
	 */
	public static boolean checkAppversion(String appversion, HttpServletRequest request){
		if(!RequestUtils.isApp(request)){//不是在app里面，都认为是使用最新的接口及方法
			return true;
		}
		String appver = getVersion(request);	//当前版本号
		if(StringUtils.isBlank(appver) || StringUtils.isBlank(appversion)){
			return false;
		}
		appversion = appversion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "");
		if(appver.equalsIgnoreCase(appversion)){
			return true;
		}		 
		
		String[] appverParts = appver.split("\\.");
		String[] appversionParts = appversion.split("\\.");
		for(int i=0; i<appverParts.length; i++){
			int appverPart = BaseUtils.toInteger(appverParts[i], 0);
			int appversionPart = 0;
			if(appversionParts.length > i){
				appversionPart = BaseUtils.toInteger(appversionParts[i], 0);
			}		
			if(appverPart > appversionPart){
				return true;
			} else if(appverPart < appversionPart){
				return false;
			}
		}	
		return false;
	}

	/**
	 * 查看当前的版本号appversion是否高于或等于给定的版本号startVersion
	 * @param appversion - app传入的当前版本号， 如，ios2.0.0
	 * @param startVersion  - 目标起始版本号,如2.0.0
	 * @return
	 */
	public static boolean checkAppversion(String appversion, String startVersion){
		String version = appversion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "").replaceAll(" ", "");
		startVersion = startVersion.toLowerCase().replaceAll("ios", "").replaceAll("android", "").replaceAll("andriod", "").replaceAll(" ", "");
		
		if(version.equalsIgnoreCase(startVersion)){
			return true;
		}
		
		String[] versionParts = version.split("\\.");
		String[] startVersionParts = startVersion.split("\\.");
		for(int i=0; i<versionParts.length; i++){
			int appverPart = BaseUtils.toInteger(versionParts[i], 0);
			int appversionPart = 0;
			if(startVersionParts.length > i){
				appversionPart = BaseUtils.toInteger(startVersionParts[i], 0);
			}		
			if(appverPart > appversionPart){
				return true;
			} else if(appverPart < appversionPart){
				return false;
			}
		}	
		return false;
	}
	
	//--------------------内部类----------------------------	

	/**
	 * MultipartFile Map
	 * @author Meron
	 *
	 */
	public static final class MultiFileMap{
		MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap<String, MultipartFile>();	
	
		public MultiFileMap(MultiValueMap<String, MultipartFile> multipartFiles) {
			this.multipartFiles = multipartFiles;
		}
	
		public MultipartFile getFile(String name) {
			return this.multipartFiles.getFirst(name);
		}
		
		public List<MultipartFile> getFiles(String name) {
			return this.multipartFiles.get(name);
		}	
	}
	
}

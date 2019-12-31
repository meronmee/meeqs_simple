package com.meronmee.core.web.util;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.meronmee.core.api.domain.RequestInfo;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.web.constant.RequestKeyConst;


/**
 * HttpServletRequest帮助类
 */
public class RequestInfoUtils {
	private static final Logger log = LoggerFactory.getLogger(RequestInfoUtils.class);

	/**
	 * 将request转为RequestInfo
	 */
	public static RequestInfo getRequestInfo(HttpServletRequest request){
		if(request == null){
			request = RequestUtils.getRequest();
		}
		if(request == null){
			return null;
		}

		//先从request缓存中取
		Object _requestInfo = request.getAttribute(RequestKeyConst.REQUEST_INFO);
		if(_requestInfo != null){
			return (RequestInfo)_requestInfo;
		}

		//常用信息
		RequestInfo requestInfo = new RequestInfo()
			.setAccessToken(RequestUtils.getParamHard(request,"access_token,accessToken,accesstoken,accessTicket,_t,_t_"))
			.setWxOpenId(RequestUtils.getParamHard(request,"_wo_,openid,openId"))
			.setWxUnoinId(RequestUtils.getParamHard(request,"_wu_,unionid,unionId")) 
			.setAppversion(RequestUtils.getParamHard(request,"appversion,appVersion")) 
			.setUserId(RequestUtils.getParamHard(request,"userid"))
			.setClientId(RequestUtils.getParamHard(request,"clientid,clientId"))
			.setLat(RequestUtils.getParamHard(request,"_lat_"))
			.setLng(RequestUtils.getParamHard(request,"_lng_"))  
			.setIsIOS(RequestUtils.isIOS(request))
			.setIsAndroid(RequestUtils.isAndroid(request))
			.setIsWeixin(RequestUtils.isWeixin(request))
			.setIsApp(RequestUtils.isApp(request))
			;

		String ip = RequestUtils.getClientIp(request);
		String reqId = System.currentTimeMillis() + "_" + ip + "_" + BaseUtils.uuidShort();
		String url = request.getRequestURL().toString();
		request.setAttribute("_reqId", reqId);

		requestInfo.setIp(ip).setId(reqId).setUrl(url);
 
		//body
		JSONObject body = new JSONObject();
		Enumeration<String> paramEnum = request.getParameterNames();
		while (paramEnum.hasMoreElements()) {
			String name = BaseUtils.toString(paramEnum.nextElement());
			String value = request.getParameter(name);
			body.put(name, value);
		}
		requestInfo.setBody(body);

		//headers
		JSONObject header = new JSONObject();
		Enumeration<String> headerEnum = request.getHeaderNames();
		StringBuilder headers = new StringBuilder();
		while (headerEnum.hasMoreElements()) {
			String name = BaseUtils.toString(headerEnum.nextElement());
			String value = request.getHeader(name);
			if(value == null){
				continue;
			}
			header.put(name, value);
		}
		requestInfo.setHeader(header);

		//cookies
		JSONObject cookie = new JSONObject();
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				cookie.put(c.getName(), c.getValue());
			}
		}
		requestInfo.setCookie(cookie);

		request.setAttribute(RequestKeyConst.REQUEST_INFO, requestInfo);
		return requestInfo;
	}

}

package com.meronmee.core.web.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meronmee.core.web.interceptor.InterceptorHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
	private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);
	
	/**
	 * 获取HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = null;
		HttpServletRequest request = RequestUtils.getRequest();
		if(request != null){
			//依赖于com.iskyshop.core.security.support.BaseMonitorFilter#preHandle
			response = (HttpServletResponse)request.getAttribute("response");
		}
		return response;
	}
	
	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}
	/**
	 * 自动返回JSON或JSONP<p>
	 * 如果请求参数中传入的callback参数，则使用JSONP返回，否则返回普通JSON。使用UTF-8编码。
	 * 
	 * @param response HttpServletResponse
	 * @param request HttpServletRequest
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, HttpServletRequest request, String text) {
		if(StringUtils.isBlank(text)){
			return;
		}
		String responseText = text;
		String callbackFunc = RequestUtils.getStringParam(request, "callback");
		if(StringUtils.isNotBlank(callbackFunc)){
			responseText = callbackFunc+"("+text+")";
		}
		render(response, request, "application/json;charset=UTF-8", responseText);
	}
	
	/**
	 * 支持跨域请求，返回JSONP，回调函数名称在request中的callback参数中指定。使用UTF-8编码。
	 * 
	 * @param response HttpServletResponse
	 * @param request HttpServletRequest
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJsonp(HttpServletResponse response, HttpServletRequest request, String text) {
		if(StringUtils.isBlank(text)){
			return;
		}
		String callbackFunc = RequestUtils.getStringParam(request, "callback");
		if(StringUtils.isBlank(callbackFunc)){
			callbackFunc = "callback";
		}
		render(response, "application/json;charset=UTF-8", callbackFunc+"("+text+")");
	}
	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * html5 Server-sent Events 使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderEvent(HttpServletResponse response, String text) {
		render(response, "text/event-stream;charset=UTF-8", text);
	}
	
	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType, String text) {
		HttpServletRequest request = RequestUtils.getRequest();		
		render(response, request, contentType, text);
	}
	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param request
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, HttpServletRequest request, String contentType, String text) {		
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

        //打印response日志
        InterceptorHelper.printResponseLog(request, response);

		try {
			PrintWriter writer = response.getWriter();
			writer.write(text);						
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

    /**
     * 支持跨域请求
     * @author Meron
     * @date 2020-02-04 17:00
     * @param response
     * @return void
     */
    public static void suppportCrossDomain(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials","true");
    }
	
}

package com.meronmee.core.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;


/**
 * Cookie 辅助类
 */
public class CookieUtils {
	
	/**
	 * 获得cookie
	 * 
	 * @param request HttpServletRequest
	 * @param name cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.isNotNull(request, "request invalid");
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获得cookie
	 * 
	 * @param request HttpServletRequest
	 * @param name cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Assert.isNotNull(request, "request invalid");
		Cookie cookie = getCookie(request, name);
		String value = null;
		if(cookie != null){
			value = cookie.getValue();
		}
		return value;
	}
	
	/**
	 * 添加cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry 生命周期，单位:秒
	 * @param domain
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,HttpServletResponse response, 
			String name, String value, Integer expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);
		return cookie;
	}
	
	/**
	 * 添加cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry  生命周期，单位:秒
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,HttpServletResponse response,
			String name, String value, Integer expiry) {
		String domain = getDomain(request);
		return addCookie(request, response, name, value, expiry, domain);
	}
	/**
	 * 添加cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,HttpServletResponse response,
			String name, String value) {
		String domain = getDomain(request);
		return addCookie(request, response, name, value, null, domain);
	}

	
	/**
	 * 删除cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletRequest request,HttpServletResponse response, String name) {
		String domain = getDomain(request);
		removeCookie(request, response, name, domain);
	}
	/**
	 * 删除cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void removeCookie(HttpServletRequest request,HttpServletResponse response, 
			String name, String domain) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setValue("");
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
	/**
	 * 获取当前域名
	 * @param request
	 */
	public static String getDomain(HttpServletRequest request) {
	     String serverName = request.getServerName();
	     return serverName;
	}
}

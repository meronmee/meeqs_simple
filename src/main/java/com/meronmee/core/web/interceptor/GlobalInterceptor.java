package com.meronmee.core.web.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.meronmee.core.web.constant.SessionKeyConst;

/**
 * 接口请求拦截器
 * @author Meron
 *
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 请求处理之前进行调用<p>
	 * @return true:进入链路中的下一个Interceptor#preHandle或者进入Controller, false:请求在此被拦截，直接结束
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {	
		//log.info("------preHandle---------");	
		
		InterceptorHelper.printRequestLog(request, response);
		return true;
	}
	
	/**
	 * Controller方法调用之后执行，但是它会在DispatcherServlet进行视图返回渲染之前被调用<p>
	 * 注：Controller中出现异常则不会走到处，会进入GlobalExceptionResolver{@link com.meronmee.core.exception.GlobalExceptionResolver}}
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		//log.info("------postHandle---------");		
		
		int statusCode = response.getStatus();
		if(statusCode == 404 || statusCode == 500){
			HttpSession session = request.getSession(true);
			session.setAttribute(SessionKeyConst.ERROR_SOURCE_URL, request.getRequestURL().toString());
		}
		
		if (null == modelAndView) {
			return;
		}
							
		//此处modelAndView中的 变量会覆盖Controller中ModelMap中的同名变量
		InterceptorHelper.initCommomVar4View(request, response, modelAndView);
	}
	
}
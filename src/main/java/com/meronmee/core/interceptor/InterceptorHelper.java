package com.meronmee.core.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.FreemarkerUtils;
import com.meronmee.core.utils.RequestUtils;

/**
 * 拦截器辅助类
 * @author Meron
 *
 */
public class InterceptorHelper extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory.getLogger(InterceptorHelper.class);
			
	/**
	 * 初始化 ModelAndView 中的公共变量
	 * @param request
	 * @param response
	 * @param modelAndView
	 */
	public static void initCommomVar4View(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView){
		if(modelAndView == null){
			return;
		}

		//重定向时modelAndView中的参数会被当做url的querystring，跳过
		String viewName = modelAndView.getViewName();
		if(BaseUtils.isBlank(viewName)){
			viewName = String.valueOf(modelAndView.getView());	
		}
		if(viewName.toLowerCase().startsWith("redirect:")){
			return;			
		}			
		
		String contextPath = request.getContextPath();
		modelAndView.addObject("baseUrl", RequestUtils.getBaseURL(request));//如：http://localhost:8080/meeqs
		modelAndView.addObject("base", contextPath);//如：/meeqs
		modelAndView.addObject("assets", BaseUtils.join(contextPath, "/public/assets"));//如：/meeqs/public/assets
		
		modelAndView.addObject("BaseUtils", FreemarkerUtils.wrapperStaticTool(BaseUtils.class));
	}
	
	/**
	 * 打印request日志
	 * 
	 * @param request
	 * @param response
	 */
	public static void printRequestLog(HttpServletRequest request, HttpServletResponse response) {
		try {			
			HttpSession session = request.getSession();
			String ip = RequestUtils.getIpAddr(request);
			String reqId = System.currentTimeMillis() + "_" + ip + "_" + BaseUtils.random6();
			request.setAttribute("reqId", reqId);

			String url = request.getRequestURL().toString();
			StringBuffer logSB=new StringBuffer();
			logSB.append("[type=request")
				 .append("##reqId=").append(reqId)
			     .append("##sessionid=").append(session.getId())       
			     .append("##url=").append(url);
			
			if (url.matches("^.+\\.(json|htm)(\\?.*)?$")) {				
				Enumeration<String> headerEnum = request.getHeaderNames();
				StringBuilder headers = new StringBuilder();
				while (headerEnum.hasMoreElements()) {
					String name = (String) headerEnum.nextElement();
					String value = request.getHeader(name);
					headers.append(name + ":" + value + ",");
				}
				logSB.append("##headers=").append(headers);

				StringBuilder parameters = new StringBuilder();
				String contentType = request.getHeader("content-type");
				if(contentType != null && contentType.startsWith("multipart/form-data")) {
					// multipart/form-data, req.getParameterNames()将拿不到数据
					Map<String, Object> params = RequestUtils.getQueryParams(request);
					for (Map.Entry<String, Object> entry : params.entrySet()) {
						Object value = entry.getValue();
						if (value == null) {
							parameters.append(entry.getKey() + ":null, ");
						} else {
							if (value instanceof String) {
								parameters.append(entry.getKey() + ":" + value.toString() + ", ");
							} else if (value instanceof String[]) {
								for (String val : (String[]) value) {
									parameters.append(entry.getKey() + ":"+ val + ", ");
								}
							}
						}
					}
				} else {
					Enumeration<String> paramEnum = request.getParameterNames();
					while (paramEnum.hasMoreElements()) {
						String name = (String) paramEnum.nextElement();
						String[] values = request.getParameterValues(name);
						for (String value : values) {
							parameters.append(name + ":" + value + ", ");
						}
					}
				}                
				logSB.append("##parameters=").append(parameters.toString().replaceAll("\r\n$", ""));
			}

			log.info(logSB.append("]").toString());
		} catch (Exception e) {
			log.error("打印request日志出现异常", e);
		}
	}
	
	/**
	 * 打印response日志
	 * 
	 * @param request
	 * @param response
	 */
	public static void printResponseLog(HttpServletRequest request, HttpServletResponse response) {
		try {			
			String url = request.getRequestURL().toString();
			String sessionid = "";
			HttpSession session = request.getSession(false);
			if(session != null){
				sessionid = session.getId();
			}
			StringBuffer logSb=new StringBuffer();
			logSb.append("[type=response")
			 	.append("##reqId=").append(request.getAttribute("reqId"))
			 	.append("##sessionid=").append(sessionid)
			 	.append("##url=").append(url)
			 	.append("##repBody=").append("text")
			 	.append("]");
			log.info(logSb.toString());	
		} catch (Exception e) {
			log.error("打印response日志出现异常", e);
		}
	}
	
	/**
	 * 判断是否是Ajax请求<p>
	 * 根据url后缀结合实际业务情况判断
	 * @param request
	 */
	public static boolean isAjax(HttpServletRequest request){
		if(request == null){
			return false;
		}
		String url = request.getRequestURL().toString();
		
		//return url.matches("^.+\\.(json|xml|htm|api)(\\?.*)?$"));
		return url.matches("^.+\\.(json)(\\?.*)?$");

	}
}
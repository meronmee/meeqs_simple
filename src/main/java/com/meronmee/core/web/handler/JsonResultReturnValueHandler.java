package com.meronmee.core.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.interceptor.InterceptorHelper;
 

/**
 * Controller中 Ajax请求 JsonResult 类型返回值处理器
 * @author Meron
 *
 */
public class JsonResultReturnValueHandler implements HandlerMethodReturnValueHandler {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 根据returnType判断该返回值是否需要当前Handler处理
	 */
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		//只处理JsonResult类型的返回值
		return JsonResult.class.isAssignableFrom(returnType.getParameterType());
	}
	
	/**
	 * 处理返回值，并结束整个处理流程(mavContainer.setRequestHandled(true))
	 */
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		//log.info("------handleReturnValue---------");	
		if (returnValue == null) {
			mavContainer.setRequestHandled(true);//设为整个请求已经处理完成
			return;
		}

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Assert.state(request != null, "No HttpServletRequest");
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		
		ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);

		String json = ((JsonResult)returnValue).toJson();
        outputMessage.getBody().write(json.getBytes("UTF-8"));
        outputMessage.close();
        
		mavContainer.setRequestHandled(true);//设为整个请求已经处理完成
		
		InterceptorHelper.printResponseLog(request, response);
	}	
	
}

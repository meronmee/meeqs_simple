package com.meronmee.core.exception;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.meronmee.core.dto.Failure;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.JsonResult.Code;
import com.meronmee.core.interceptor.InterceptorHelper;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.RequestUtils;
import com.meronmee.core.utils.ResponseUtils;

/**
 * 错误信息统一处理
 * 对未处理的错误信息做一个统一处理
 * @author meron
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {	
		log.info("------resolveException---------");
		String url = request.getRequestURL().toString();
		//if (url.matches("^.+\\.(json|xml|htm|api)(\\?.*)?$")) { 
		if (url.matches("^.+\\.(json)(\\?.*)?$")) {//Ajax请求
			JsonResult result = null;
			if(e instanceof BizException){
				result = new Failure().setMsg(e.getMessage());
			} else {
				result = new JsonResult().setMsg("操作失败").setCode(Code.EXCEPTION);
				log.error(BaseUtils.join("操作失败, URL:", url, ", Params:", request.getParameterMap(),", 异常信息:", e.getMessage()), e);
			}
			
			ResponseUtils.renderJson(response, request, result.toJson());	
			//打印response日志
			InterceptorHelper.printResponseLog(request, response);
			
			//不能返回null，返回null将继续走默认的处理流程（还会被后续处理器打印出异常）
			return new ModelAndView();		
		} else {//Web请求
			ModelAndView errorView = null;
			if(RequestUtils.isMobile(request)){//移动端请求
				errorView = new ModelAndView("/app/error"); 
			} else {
				errorView = new ModelAndView("/manage/error"); 
			}
			
			//初始化  ModelAndView 中的公共变量
			InterceptorHelper.initCommomVar4View(request, response, errorView);
			
			if(e instanceof BizException){
				errorView.addObject("errMsg", e.getMessage());
			} else {
				errorView.addObject("errMsg", "操作失败");
				log.error(BaseUtils.join("操作失败, URL:", url, ", Params:", request.getParameterMap(),", 异常信息:", e.getMessage()), e);	
			}
			
			return errorView;
		}		
	}	

}
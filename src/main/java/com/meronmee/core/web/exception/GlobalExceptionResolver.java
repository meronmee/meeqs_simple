package com.meronmee.core.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.meronmee.core.common.exception.BizException;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.web.dto.AuthError;
import com.meronmee.core.web.dto.BizError;
import com.meronmee.core.web.dto.CertError;
import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.dto.JsonResult.Code;
import com.meronmee.core.web.interceptor.InterceptorHelper;
import com.meronmee.core.web.util.RequestUtils;
import com.meronmee.core.web.util.ResponseUtils;
 

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
		//log.info("------resolveException---------");
		String url = request.getRequestURL().toString();
		if (InterceptorHelper.isAjax(request)) {//Ajax请求
			JsonResult result = null;
			if(e instanceof BizException){//业务报错
				result = new BizError().setMsg(e.getMessage());
			} else if(e instanceof UnauthorizedException){//授权失败(权限不足)
				result = new CertError();
			} else if(e instanceof UnauthenticatedException){//认证失败(未登录)
				result = new AuthError();
			} else {
				result = new JsonResult().setMsg("操作失败").setCode(Code.EXCEPTION);
				log.error(BaseUtils.join("操作失败, URL:", url, ", Params:", RequestUtils.getQueryParams(request),", 异常信息:", e.getMessage()), e);
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
				errorView = new ModelAndView("/manage/error/error"); 
			}
			
			//初始化  ModelAndView 中的公共变量
			InterceptorHelper.initCommomVar4View(request, response, errorView);
			
			if(e instanceof BizException){
				errorView.addObject("errMsg", e.getMessage());
			} else if(e instanceof UnauthorizedException){//授权失败(权限不足)
				errorView.addObject("errMsg", "授权失败");
			} else if(e instanceof UnauthenticatedException){//认证失败(未登录)
				errorView.addObject("errMsg", "请登录后再操作");
			} else {
				if(RequestUtils.isMobile(request)){
					errorView.addObject("errMsg", "操作失败");
				} else {
					errorView.addObject("errMsg", "操作失败，" + e.getMessage());					
				}
				log.error(BaseUtils.join("操作失败, URL:", url, ", Params:", request.getParameterMap(),", 异常信息:", e.getMessage()), e);	
			}
			
			return errorView;
		}		
	}	

}
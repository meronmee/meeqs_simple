package com.meronmee.core.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meronmee.core.dto.AuthError;
import com.meronmee.core.dto.CertError;
import com.meronmee.core.dto.Failure;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.JsonResult.Code;
import com.meronmee.core.exception.BizException;
import com.meronmee.core.interceptor.InterceptorHelper;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.CookieUtils;
import com.meronmee.core.utils.RequestUtils;
import com.meronmee.core.utils.ResponseUtils;

/**
 * 自定义登录认证过滤器
 */
public class CustomAuthenticationFilter extends FormAuthenticationFilter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
     * Processes requests where the subject was denied access as determined by the
     * {@link #isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, Object) isAccessAllowed}
     * method, retaining the {@code mappedValue} that was used during configuration.
     * <p/>
     * This method immediately delegates to {@link #onAccessDenied(ServletRequest,ServletResponse)} as a
     * convenience in that most post-denial behavior does not need the mapped config again.
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the config specified for the filter in the matching request's filter chain.
     * @return <code>true</code> if the request should continue to be processed(将交由下一环节处理); false if the subclass will
     *         handle/render the response directly.(反馈response,请求到此结束)
     * @throws Exception if there is an error processing the request.
     * @since 1.0
     */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//log.info("------CustomAuthenticationFilter#onAccessDenied------");
		HttpServletRequest httpRequest = WebUtils.toHttp(request);  
        HttpServletResponse httpResponse = WebUtils.toHttp(response);  
          
        //只处理Ajax的，非Ajax仍然交由spring-shiro.xml#filterChainDefinitions处理
        if (InterceptorHelper.isAjax(httpRequest)) {//Ajax请求
	        JsonResult result = null;
	        
	        Subject subject = SecurityUtils.getSubject();  	        
	        if (subject.getPrincipal() == null) {//未登录或登录时间过长
	        	
	        	result = new AuthError();	        	
	        } else { //没有足够的权限执行该操作	        	
	        	result = new CertError();
	        }

	        //打印request日志(至此不会进入GlobalInterceptor#preHandle)
			InterceptorHelper.printRequestLog(httpRequest, httpResponse);	
			
			ResponseUtils.renderJson(httpResponse, httpRequest, result.toJson());	
			
	        //打印response日志
			InterceptorHelper.printResponseLog(httpRequest, httpResponse);
			
			
			
	        return false; 
        } else {
        	return super.onAccessDenied(request, response);
        }	
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		return super.onLoginFailure(token, e, request, response);
	}
	
	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return super.onPreHandle(request, response, mappedValue);
	}
}

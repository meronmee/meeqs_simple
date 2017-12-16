package com.meronmee.core.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meronmee.core.dto.AuthError;
import com.meronmee.core.dto.CertError;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.interceptor.InterceptorHelper;
import com.meronmee.core.utils.ResponseUtils;

/**
 * 自定义perms权限过虑器
 * @author Meron
 *
 */
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Handles the response when access has been denied.  It behaves as follows:
     * <ul>
     * <li>If the {@code Subject} is unknown<sup><a href="#known">[1]</a></sup>:
     * <ol><li>The incoming request will be saved and they will be redirected to the login page for authentication
     * (via the {@link #saveRequestAndRedirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}
     * method).</li>
     * <li>Once successfully authenticated, they will be redirected back to the originally attempted page.</li></ol>
     * </li>
     * <li>If the Subject is known:</li>
     * <ol>
     * <li>The HTTP {@link HttpServletResponse#SC_UNAUTHORIZED} header will be set (401 Unauthorized)</li>
     * <li>If the {@link #getUnauthorizedUrl() unauthorizedUrl} has been configured, a redirect will be issued to that
     * URL.  Otherwise the 401 response is rendered normally</li>
     * </ul>
     * <code><a name="known">[1]</a></code>: A {@code Subject} is 'known' when
     * <code>subject.{@link org.apache.shiro.subject.Subject#getPrincipal() getPrincipal()}</code> is not {@code null},
     * which implicitly means that the subject is either currently authenticated or they have been remembered via
     * 'remember me' services.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return {@code false} always for this implementation.
     * @throws IOException if there is any servlet error.
     */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		//log.info("------CustomPermissionsAuthorizationFilter#onAccessDenied------");
		HttpServletRequest httpRequest = WebUtils.toHttp(request);  
        HttpServletResponse httpResponse = WebUtils.toHttp(response);  
          
        //只处理Ajax的，非Ajax仍然交由spring-shiro.xml#filterChainDefinitions处理
        if (InterceptorHelper.isAjax(httpRequest)) {//Ajax请求
	        JsonResult result = null;
	        
	        Subject subject = SecurityUtils.getSubject();  	     
	        if (subject.getPrincipal() == null) {//未登录或登录时间过长	        	
	        	result = new AuthError();	        	
	        } else { //没有足够的权限执行该操作	        	
	        	result = new CertError().setMsg("授权失败，您没有该项操作的权限");
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
}

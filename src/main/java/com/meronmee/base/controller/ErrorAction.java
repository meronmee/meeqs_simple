package com.meronmee.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.web.constant.SessionKeyConst;
import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.dto.JsonResult.Code;
import com.meronmee.core.web.interceptor.InterceptorHelper;
import com.meronmee.core.web.util.ResponseUtils;

/**
 * Controller 基础请求控制器
 * @author Meron
 *
 */
@Controller
public class ErrorAction  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
    /**
	 * 404页面
	 */
    @RequestMapping(value = "/404.htm")
    public String notFoundPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {
    	String url = getErrorSourceURL(request);//获取跳转来源地址		
    	if (InterceptorHelper.isAjax(url)) {//Ajax请求
    		JsonResult result = new JsonResult().setMsg("请求地址无效").setCode(Code.NOT_FOUND);    				
    		ResponseUtils.renderJson(response, request, result.toJson());
    		return null;
    	} else {
	    	/*
	    	if(RequestUtils.isMobile(request)){
	    		return "app/error/404";
	    	} else {
	    		return "manage/error/404";    		
	    	}
	    	*/
	
			return "manage/error/404";  		
    	}		      
    } 
    
    /**
	 * 500页面
	 */
    @RequestMapping(value = "/500.htm")
    public String fatalPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {
		String url = getErrorSourceURL(request);//获取跳转来源地址
    	if (InterceptorHelper.isAjax(url)) {//Ajax请求
    		JsonResult result = new JsonResult().setMsg("操作失败").setCode(Code.EXCEPTION);    				
    		ResponseUtils.renderJson(response, request, result.toJson());
    		return null;
    	} else {
        	/*
        	if(RequestUtils.isMobile(request)){
        		return "app/error/500";
        	} else {
        		return "manage/error/500";    		
        	}
        	*/

    		return "manage/error/500";     		
    	}		   
    }
    
    /**
     * 获取错误页面跳转的来源地址
     * @param request
     * @return
     */
    private String getErrorSourceURL(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		HttpSession session = request.getSession(false);
		if(session != null){
			url = BaseUtils.toString(session.getAttribute(SessionKeyConst.ERROR_SOURCE_URL), url);
			session.removeAttribute(SessionKeyConst.ERROR_SOURCE_URL);
		}
		return url;    	
    }
    
}

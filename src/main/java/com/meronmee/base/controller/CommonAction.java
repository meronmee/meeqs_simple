package com.meronmee.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meronmee.core.commons.Const;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.JsonResult.Code;
import com.meronmee.core.interceptor.InterceptorHelper;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.ResponseUtils;

/**
 * Controller 基础请求控制器
 * @author Meron
 *
 */
@Controller
public class CommonAction extends BaseAction{  

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
			url = BaseUtils.toString(session.getAttribute(Const.SESSION_KEY_ERROR_SOURCE_URL), url);
			session.removeAttribute(Const.SESSION_KEY_ERROR_SOURCE_URL);
		}
		return url;    	
    }
    
}

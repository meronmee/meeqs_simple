package com.meronmee.manage.controller;

import com.meronmee.base.domain.User;
import com.meronmee.core.common.exception.BizException;
import com.meronmee.core.common.util.Assert;
import com.meronmee.core.web.shiro.UserHolder;
import com.meronmee.core.web.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
 

/**
 * 管理端基础控制器
 * @author Meron
 *
 */
@Controller
public class ManageMainAction {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 登录页面
	 */
    @RequestMapping(value = "/manage/login.htm",  method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        String msg = RequestUtils.getStringParam(request, "msg"); 
        viewData.put("msg", msg);
        
        return "manage/login";
    }
    
    /**
	 * 登录请求
	 */
    @RequestMapping(value = "/manage/login.htm",  method = RequestMethod.POST)
    public String loginRequest(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {
        try{
	        String username = RequestUtils.getStringParam(request, "username");
	        String password = RequestUtils.getStringParam(request, "password");
	        
        	Assert.isNotBlank(username, "用户名不能为空");
        	Assert.isNotBlank(password, "密码不能为空");
        	
	        try {
		        //进行Shiro登录认证
	        	UsernamePasswordToken token = new UsernamePasswordToken(username, password) ;		        
		        SecurityUtils.getSubject().login(token);   	        	
	        } catch (UnknownAccountException e){
	        	 Assert.error("用户名不存在！");
	        } catch (IncorrectCredentialsException e){
	        	 Assert.error("密码不正确！");
	        } catch (Exception e){
	            log.error("登录认证失败，", e);	            
	            Assert.error("登录认证失败");
	        }
        }catch(BizException e){
            viewData.put("msg", e.getMessage());
        	return "redirect:/manage/login.htm";           	
        }
        
        //跳转到登录之前的地址（如果有的话），否则跳转到首页
        //see: http://blog.csdn.net/lhacker/article/details/20450855
        try {
			WebUtils.redirectToSavedRequest(request, response, "/manage/index.htm");
			return null;
		} catch (IOException e) {
	        return "redirect:/manage/index.htm";
		}
    }

    /**
	 * 注销
	 */
    @RequestMapping(value = "/manage/logout.htm",  method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {             
        SecurityUtils.getSubject().logout();
        return "redirect:/manage/login.htm"; 
    }

    /**
	 * 权限不足时跳转页面
	 */
    @RequestMapping(value = "/manage/error/unauthorized.htm",  method = RequestMethod.GET)
    public String unauthorizedPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {        
        return "manage/error/unauthorized";
    }
    
    /**
	 * 认证失败时跳转页面
	 */
    @RequestMapping(value = "/manage/error/unauthenticated.htm",  method = RequestMethod.GET)
    public String unauthenticatedPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {        
        return "manage/error/unauthenticated";
    }

    /**
	 * 错误页面
	 */
    @RequestMapping(value = "/manage/error/error.htm",  method = RequestMethod.GET)
    public String errorPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {        
        return "manage/error/error";
    }
    
	/**
	 * 管理端首页
	 */
    @RequestMapping(value = "/manage/index.htm",  method = RequestMethod.GET)
    public String indexPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) { 
    	//获取当前登录的用户
    	User user = UserHolder.getCurrentUser();
    	
        viewData.put("user", user);
    	 
        return "manage/index";
    }      
}

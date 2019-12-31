package com.meronmee.core.web.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.meronmee.base.domain.User;
 

/**
 * 获取当前登录的用户,只能用在Controller中，不能再后面的层中使用
 */
public class UserHolder {
		
	/**
	 * 获取当前登录的用户
	 * @return
	 */
	public static User getCurrentUser(){
		//从Shiro的session中或取user 
    	Subject subject = SecurityUtils.getSubject(); 
    	if(subject == null){
    		return null;
    	}
    	
    	Object principal = subject.getPrincipal();//CustomRealm#doGetAuthenticationInfo中设置的
    	if(principal == null){
    		return null;
    	}
    	if(principal instanceof User){
    		User user = (User)principal; 
        	return user;
    	}
    	
    	return null;    	
	}
}

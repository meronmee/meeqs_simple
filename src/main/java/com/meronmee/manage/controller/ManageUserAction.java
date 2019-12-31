package com.meronmee.manage.controller;

import com.meronmee.base.api.UserApi;
import com.meronmee.base.domain.User;
import com.meronmee.core.common.util.Assert;
import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.dto.Success;
import com.meronmee.core.web.shiro.UserHolder;
import com.meronmee.core.web.util.RequestUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**	
 * 管理端用户控制器
 * @author Meron
 *
 */
@Controller
public class ManageUserAction  {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserApi userApi;
	   

	/**
	 * 用户列表页
	 */
    //@RequiresRoles("admin")
    @RequiresPermissions(value={"admin", "user:list"}, logical=Logical.AND)
    @RequestMapping(value = "/manage/user/list.htm",  method = RequestMethod.GET)
    public String listPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {
    	//获取当前登录的用户
    	User user = UserHolder.getCurrentUser();
    	
        viewData.put("user", user);
        return "manage/user/user_list";
    }  


	/**
	 * 用户详情
	 */
    //@RequiresRoles("admin")
    @RequiresPermissions(value={"admin", "user:profile"}, logical=Logical.OR)
    @RequestMapping(value = "/manage/user/profile.htm",  method = RequestMethod.GET)
    public String profilePage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {
		Long id = RequestUtils.getLongParam(request, "id");
		Assert.isNotNull(id, "ID不能为空");
				
    	User user = this.userApi.retrieve(id);
    	Assert.isNotNull(user, "无效的用户ID");
    	
        viewData.put("user", user); 
        
        return "manage/user/user_profile";
    }  

    /**
	 * 用户数据
	 */
    //@RequiresRoles("admin")
    @RequiresPermissions(value={"admin", "user:profile"}, logical=Logical.OR)
    @RequestMapping(value = "/manage/user/profile.json", method = RequestMethod.POST)
    public JsonResult profileData(HttpServletRequest request, HttpServletResponse response) {
                
    	return new Success(); 
    }
    
    
}

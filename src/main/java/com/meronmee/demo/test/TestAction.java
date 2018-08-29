package com.meronmee.demo.test;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.meronmee.base.controller.BaseAction;
import com.meronmee.base.model.User;
import com.meronmee.baseservice.cache.redis.RedisService;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.Success;
import com.meronmee.core.utils.Assert;
import com.meronmee.core.utils.LinkMap;
import com.meronmee.core.utils.RequestUtils;

/**
 * 调试
 * @author Meron
 *
 */
@Controller
public class TestAction extends BaseAction{
		
    @Resource(name="man")  
    private Human human; 
    
    @Autowired
    private RedisService redisService;
    
	/**
	 * 页面请求
	 */
    @RequestMapping(value = "/demo/test/test.htm",  method = RequestMethod.GET)
    public String testPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
      
        if(StringUtils.isBlank(msg)){
            viewData.put("msg", "redirect");
        	return "redirect:/demo/test/error.htm";        	
        }
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

    /**
	 * Ajax请求
	 */ 
    @RequestMapping(value = "/demo/test/test.json")
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
    	/*
        log.info("msg1:{}", RequestUtils.getStringParam(request, "msg"));
        log.info("msg2:{}", RequestUtils.getQueryParam(request, "msg"));   
        log.info("msgs:" + JSON.toJSONString(request.getParameterValues("msg"))); 
        */
    	human.speak();
        
    	return new Success(); 
    }
    
    /**
	 * 操控redis
	 */
	@RequestMapping(value = "/sys/redis.json") 
	public JsonResult redis(HttpServletRequest request, HttpServletResponse response) { 
		String oper = RequestUtils.getStringParam(request, "oper", "get");
		String key = RequestUtils.getStringParam(request, "key");
		String value = RequestUtils.getStringParam(request, "value"); 

		if("get".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空"); 
		} else if("set".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空");
			//Assert.isNotBlank(value, "value 不能为空"); 	
			User user = this.service.retrieveModel(User.class, 6L);
			
			User userInfo = user; 
			//Map<String,Object> userInfo = user.toMap();
			//String userInfo = JSON.toJSONString(user); 
			this.redisService.setObj(key, userInfo, 8); 			
			//this.redisService.set(key, userInfo, 8); 
		} else if("delete".equals(oper)){
			Assert.isNotBlank(key, "key 不能为空");
			this.redisService.delete(key); 
		} 
		
		User user = this.redisService.getObj(key);
		//Map<String, Object> user = this.redisService.getObj(key);
		//String user = this.redisService.getObj(key);
		//String user = this.redisService.get(key);
		return new Success(new LinkMap("value", user)); 
	}
        
}

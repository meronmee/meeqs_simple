package com.meronmee.demo.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.meronmee.base.model.User;
import com.meronmee.core.controller.BaseAction;
import com.meronmee.core.dao.helper.SqlKey;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.Success;
import com.meronmee.core.utils.Assert;
import com.meronmee.core.utils.RequestUtils;
import com.meronmee.core.utils.ResponseUtils;

@Controller
public class TestAction extends BaseAction{
  
	/**
	 * 普通页面请求
	 */
    @RequestMapping(value = "/test/error.htm", method = RequestMethod.GET)
    public String testPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {    	
        String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

	/**
	 * 普通页面请求-带变量
	 */
    @RequestMapping(value = "/test/{page}.htm", method = RequestMethod.GET)
    public String testPageVar(@PathVariable("page") String page, HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        Assert.isNotBlank(page, "page不能为空");        
        return "app/"+page;
    }

    /**
	 * 含有重定向的面请求
	 */    
    @RequestMapping(value = "/test/redirect.htm", method = RequestMethod.GET)
    public String testPageRedirect(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {    	
        String msg = RequestUtils.getStringParam(request, "msg");
        if(StringUtils.isBlank(msg)){
        	//重定向时viewData中的参数会被当做url的querystring
        	//下面两行等价于return "redirect:/test/test.htm?msg=redirect";
            viewData.put("msg", "redirect");
        	return "redirect:/test/error.htm";
        	
        }
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

    /**
	 * 普通Ajax请求
	 */ 
    @RequestMapping(value = "/test/test.json", method = RequestMethod.POST)
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
    	String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
                
    	return new Success().setMsg(msg);
    }
    
    /**
	 * Ajax请求返回后进行后续的处理
	 */  
    @RequestMapping(value = "/test/retfirst.json", method = RequestMethod.POST)
    public void testAjaxReturnFirst(HttpServletRequest request, HttpServletResponse response) {
    	String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
                
        JsonResult result = new Success().setMsg(msg);
        //先返回数据给客户端
		ResponseUtils.renderJson(response, request, result.toJson());	
		
		//后续处理
		//....
		log.info("after response");
    }
    
    /**
	 * 文件上传
	 */ 
    @RequestMapping(value = "/test/file.json", method = RequestMethod.POST)
    public JsonResult testFile(HttpServletRequest request, HttpServletResponse response) {
    	MultipartFile file = RequestUtils.getFileParam(request, "attach");
    	List<MultipartFile> files = RequestUtils.getFilesParam(request, "attachs");
        Assert.isNotNull(file, "attach不能为空");
        Assert.isNotNull(files, "attachs不能为空");
        
    	String text = RequestUtils.getStringParam(request, "text");
    	String radio = RequestUtils.getStringParam(request, "radio");
    	String password = RequestUtils.getStringParam(request, "password");
        log.info("text:{}, radio:{}, password:{}", text,radio,password);
        
    	return new Success().setMsg(file.getOriginalFilename());
    }
    
    
    /**
	 * 测试数据
	 */ 
    @RequestMapping(value = "/test/db.json", method = RequestMethod.POST)
    public JsonResult testDB(HttpServletRequest request, HttpServletResponse response) {
    	/* */
    	User user = new User();
    	user.setUsername("meron2");
    	user.setPassword("11111");
    	user.setMobile("13455667799");
    	user.setEmail("abc@d.com");
    	user.setNickname("小米");
    	user.setRealname("米饭");
    	this.service.createModel(User.class, user); 
    	
    	/*
    	User user = this.service.retrieveModel(User.class, 6L);
    	user.setNickname("小米粒");
    	user.setPassword("123456");
    	this.service.updateModel(User.class, user);
    	user = this.service.retrieveModel(User.class, 6L);
    	*/
    	
    	//List<User> users = this.service.findModelByProperty(User.class, "id", Arrays.asList());
    	
    	//User user = this.service.findOneModelByProperty(User.class, "password", "11111");
    	
    	//this.service.deleteModel(User.class, user);
    	/*
    	int ret = this.service.deleteModelPhysically(User.class, user);
    	log.info("deleted:{}", ret);
    	*/
    	return new Success(user);
    }
    

	/**
	 * 测试存储过程
	 */
	@RequestMapping(value = "/test/proc.json", method = RequestMethod.POST)
	public JsonResult testProc(HttpServletRequest request, HttpServletResponse response) {
			//参数校验
			Map<String, Object> map = new HashMap<>();
			map.put("in_str", "1, aa ,  bb, ccc ,  4");
			map.put("in_delimiter", ",");
			map.put("in_trim", 1);
			map.put("io_var", 2);
			//map.put("out_total", 0);
			//map.put("out_result", "");
			
			this.service.runProc(SqlKey.demo_testProc, map);			

	    	return new Success(map);
	}
    
}

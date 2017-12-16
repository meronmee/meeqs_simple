package com.meronmee.demo.code;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.meronmee.base.controller.BaseAction;
import com.meronmee.base.model.User;
import com.meronmee.base.service.UserService;
import com.meronmee.core.dao.helper.Pager;
import com.meronmee.core.dao.helper.SqlKey;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.Success;
import com.meronmee.core.utils.Assert;
import com.meronmee.core.utils.RequestUtils;
import com.meronmee.core.utils.ResponseUtils;

/**
 * 基本用法示例
 * @author Meron
 *
 */
@Controller
public class TemplateAction extends BaseAction{
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * 页面请求
	 */
    @RequestMapping(value = "/demo/code/error.htm",  method = RequestMethod.GET)
    public String testPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
                
        viewData.put("errMsg", msg);
        
        return "app/error";        
    }

	/**
	 *页面请求 - 带路径变量
	 */
    @RequestMapping(value = "/demo/code/{page}.htm", method = RequestMethod.GET)
    public String testPageVar(@PathVariable("page") String page, HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        Assert.isNotBlank(page, "page不能为空");        
        
        return "app/"+page;
    }

    /**
	 * 含有重定向的面请求
	 */    
    @RequestMapping(value = "/demo/code/redirect.htm", method = RequestMethod.GET)
    public String testPageRedirect(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {    	
        String msg = RequestUtils.getStringParam(request, "msg");
        
        if(StringUtils.isBlank(msg)){
            viewData.put("msg", "redirect");
        	return "redirect:/demo/code/error.htm";        	
        }
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

    /**
	 * Ajax请求
	 */ 
    @RequestMapping(value = "/demo/code/ajax.json", method = RequestMethod.POST)
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
    	String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
        
    	return new Success().setMsg(msg);
    }
    
    /**
	 * Ajax请求 - 响应客户端后还进行一些后续的处理
	 */  
    @RequestMapping(value = "/demo/code/retfirst.json", method = RequestMethod.POST)
    public void testAjaxReturnFirst(HttpServletRequest request, HttpServletResponse response) {
    	String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
                
        JsonResult result = new Success().setMsg(msg);
        
		ResponseUtils.renderJson(response, request, result.toJson());	
		
		log.info("after response");
    }
    
    /**
	 * 文件上传
	 */ 
    @RequestMapping(value = "/demo/code/file.json", method = RequestMethod.POST)
    public JsonResult testFile(HttpServletRequest request, HttpServletResponse response) {
    	MultipartFile file = RequestUtils.getFileParam(request, "attach");
    	List<MultipartFile> files = RequestUtils.getFilesParam(request, "attachs");
    	
        Assert.isNotNull(file, "attach不能为空");
        Assert.isNotNull(files, "attachs不能为空");
        
    	String text = RequestUtils.getStringParam(request, "text");
    	String radio = RequestUtils.getStringParam(request, "radio");
    	String password = RequestUtils.getStringParam(request, "password");
        log.info("text:{}, radio:{}, password:{}", text,radio,password);
        
        //...
        
    	return new Success().setMsg(file.getOriginalFilename());
    }
    
    /** 
	 * MyBatis数据库查询 - 1、使用公共接口面向 Model 对象的增删改查<p>
	 */
    @RequestMapping(value = "/demo/code/db/model.json", method = RequestMethod.POST)
    public JsonResult testDBModel(HttpServletRequest request, HttpServletResponse response) {
    	/*
    	User user = new User();
    	user.setUsername("meron2");
    	user.setPassword("11111");
    	user.setMobile("13455667799");
    	user.setEmail("abc@d.com");
    	user.setNickname("小米");
    	user.setRealname("米饭");
    	this.service.createModel(User.class, user); 
    	*/
    	
    	/*
    	User user = this.service.retrieveModel(User.class, 6L);
    	user.setNickname("小米粒");
    	user.setPassword("123456");
    	this.service.updateModel(User.class, user);
    	user = this.service.retrieveModel(User.class, 6L);
    	*/
    	
    	//List<User> users = this.service.findModelByProperty(User.class, "id", Arrays.asList());
    	
    	//User user = this.service.findOneModelByProperty(User.class, "password", "11111");
    	

    	
    	Map<String, Object> params = new LinkedHashMap<>();
    	params.put("password", "111111");    
    	params.put("sex", 3);    		
    	List<User> list = this.service.findModelByProps(User.class, params);    	
       	return new Success(list);
       	
    	//this.service.deleteModel(User.class, user);
       	
    	/*
    	int ret = this.service.deleteModelPhysically(User.class, user);
    	log.info("deleted:{}", ret);
    	return new Success(user);
    	*/
    }
    
    /**
	 * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
   	 */ 
    @RequestMapping(value = "/demo/code/db/sql.json", method = RequestMethod.POST)
    public JsonResult testDBSql(HttpServletRequest request, HttpServletResponse response) {    	
    	/*
    	List<User> list = new ArrayList<>();
    	for(int i=1; i<10; i++){
    		User user = new User();
        	user.setUsername("meron"+i);
        	user.setPassword("11111"+i);
        	user.setMobile("1345566779"+i);
        	user.setEmail("abc"+i+"@d.com");
        	user.setNickname("小米"+i);
        	user.setRealname("米饭"+i);

        	list.add(user);
    	}
    	this.service.create(SqlKey.demo_createUserBatch, list);
       	return new Success(list);
    	*/
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("nickname", "小米");    	
    	Pager<Map<String, Object>> pager = this.service.query(SqlKey.demo_demoQueryCount, SqlKey.demo_demoQueryList, 2, 3, params);
       	return new Success(pager);
    	
    	
    	/*
    	int  i = 0;
    	User user = new User();
    	user.setUsername("meron"+i);
    	user.setPassword("11111"+i);
    	user.setMobile("1345566779"+i);
    	user.setEmail("abc"+i+"@d.com");
    	user.setNickname("小小米"+i);
    	user.setRealname("米小饭"+i);
    	this.service.create(SqlKey.demo_createUser, user);
    	*/
    	
    	/*
    	User user = this.service.retrieveModel(User.class, 17L);
    	user.setRealname("笑傲米饭");
    	this.service.update(SqlKey.demo_updateUser, user);
    	
    	return new Success(user);  	
    	*/
   	}    

	/**
	 * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
	 * 特例：使用存储过程
	 */
	@RequestMapping(value = "/demo/code/db/proc.json", method = RequestMethod.POST)
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
	
	 /**
	 * MyBatis数据库查询 - 3、使用 DAO 引用 SQL 语句的增删改查<p>
   	 */ 
    @RequestMapping(value = "/demo/code/db/dao.json", method = RequestMethod.POST)
    public JsonResult testDBDao(HttpServletRequest request, HttpServletResponse response) {    	
    	List<User> list = this.userService.getUserList(5, 3);
    	return new Success(list);    	
    	
    	/*
    	User user  = this.userService.queryByPhone("13455667795");
    	return new Success(user);
    	*/
    	
    	/*
    	this.userService.updateNickname(17L, "小米007");
    	return new Success(this.service.retrieveModel(User.class, 17L));
    	*/
   	}
    
}

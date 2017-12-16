package com.meronmee.demo.example;

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

import com.meronmee.base.model.User;
import com.meronmee.base.service.UserService;
import com.meronmee.base.controller.BaseAction;
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
//最好不要在Controller的类层面添加@RequestMapping，因为不利于代码阅读和问题排查
@Controller
public class BaseExampleAction extends BaseAction{
	
	//注入服务类
	@Autowired
	private UserService userService;
	
	
	/**
	 * 页面请求
	 */
    @RequestMapping(value = "/demo/example/error.htm",  method = RequestMethod.GET)
    public String testPage(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
    	//使用RequestUtils.get*Param获取各种类型（包括文件类型）的参数
        String msg = RequestUtils.getStringParam(request, "msg");
        //使用Assert来校验，不满足条件是会抛出一个BizException，这个异常会在com.meronmee.core.exception.GlobalExceptionResolver中捕获，并返回错误页面
        Assert.isNotBlank(msg, "msg不能为空");
        
        
        //通过viewData向模板输出变量
        viewData.put("errMsg", msg);
        
        //freemark模板文件路径，此处对应的是src/main/webapp/WEB-INF/view/app/error.html
        //不需要/WEB-INF/view/前缀，不需要.html后缀（相关配置参考src/main/resources/spring/spring-web.xml#freemarkerConfig和#freemarkerViewResolver）
        return "app/error";
        
        //一般不需要在Controller中处理异常，在com.meronmee.core.exception.GlobalExceptionResolver有全局的异常处理
    }

	/**
	 *页面请求 - 带路径变量
	 */
    @RequestMapping(value = "/demo/example/{page}.htm", method = RequestMethod.GET)
    public String testPageVar(@PathVariable("page") String page, HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {   
        Assert.isNotBlank(page, "page不能为空");        
        
        return "app/"+page;
    }

    /**
	 * 含有重定向的面请求
	 */    
    @RequestMapping(value = "/demo/example/redirect.htm", method = RequestMethod.GET)
    public String testPageRedirect(HttpServletRequest request, HttpServletResponse response, ModelMap viewData) {    	
        String msg = RequestUtils.getStringParam(request, "msg");
        
        if(StringUtils.isBlank(msg)){
        	//重定向时viewData中的参数会被当做url的querystring
        	//下面两行等价于return "redirect:/demo/example/demo/example.htm?msg=redirect";
            viewData.put("msg", "redirect");
        	return "redirect:/demo/example/error.htm";        	
        }
        
        viewData.put("errMsg", msg);
        return "app/error";
    }

    /**
	 * Ajax请求
	 */ 
    @RequestMapping(value = "/demo/example/ajax.json", method = RequestMethod.POST)
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
    	String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
        
        //返回JsonResult类或其子类对象，
        //系统会在src/main/java/com/meronmee/core/handler/JsonResultReturnValueHandler.java中将其用FastJson格式化为JSON字符串返回给客户端
    	return new Success().setMsg(msg);
    }
    
    /**
	 * Ajax请求 - 响应客户端后还进行一些后续的处理
	 */  
    @RequestMapping(value = "/demo/example/retfirst.json", method = RequestMethod.POST)
    //返回值不再是JsonResult，而是void
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
    @RequestMapping(value = "/demo/example/file.json", method = RequestMethod.POST)
    public JsonResult testFile(HttpServletRequest request, HttpServletResponse response) {
    	//html表单提交的话，需要将form设为 enctype="multipart/form-data"
    	MultipartFile file = RequestUtils.getFileParam(request, "attach");//attach未客户端提交的单文件字段名称
    	List<MultipartFile> files = RequestUtils.getFilesParam(request, "attachs");//attachs未客户端提交的多文件字段名称
    	
        Assert.isNotNull(file, "attach不能为空");
        Assert.isNotNull(files, "attachs不能为空");
        
    	String text = RequestUtils.getStringParam(request, "text");
    	String radio = RequestUtils.getStringParam(request, "radio");
    	String password = RequestUtils.getStringParam(request, "password");
        log.info("text:{}, radio:{}, password:{}", text,radio,password);
        
        //可以在此将文件上传到分布式文件服务器中，如：FastDFS
        
    	return new Success().setMsg(file.getOriginalFilename());
    }
    
    /**
	 * MyBatis+MySQL数据库查询这两提供了三种方式：
	 * <ol>1、使用公共接口面向 Model 对象的增删改查<br>
	 *			优点:快速便捷<br>
	 *			使用条件:只能对单个规范化的实体进行增删改查，无法做复杂的查询<br>
	 *			推荐度:☆☆☆☆☆</ol>
	 *
	 * <ol>2、使用公共接口引用 SQL 语句的增删改查<br>
	 *			优点:支持所有SQL语句<br>
	 *			使用条件:需要在Mapper中写SQL语句，需要在SqlKey中维护SQL语句ID<br>
	 *			推荐度:☆☆☆☆，在“方式1”无法满足的情况下推荐使用该方式</ol>
	 *
	 * <ol>3、使用 DAO 引用 SQL 语句的增删改查<br>
	 *			优点:支持所有SQL语句<br>
	 *			使用条件:需要在Mapper中写SQL语句，需要在针对各个业务模块写DAO层接口。分页查询需要自己在SQL中写limit，相比方式二的仅有优势在于SQL调用参数更加直观<br>
	 *			推荐度:☆☆☆</ol>
	 *
	 * 以上三种方式可以任意混合使用<p>
	 * 
	 * MyBatis数据库查询 - 1、使用公共接口面向 Model 对象的增删改查<p>
	 */
    @RequestMapping(value = "/demo/example/db/model.json", method = RequestMethod.POST)
    public JsonResult testDBModel(HttpServletRequest request, HttpServletResponse response) {
    	/*
    	 * 
		 * 使用此类接口的前提是：<p>
		 * <li>实体必须继承com.meronmee.core.model.Model</li>
		 * <li>实体必须含有数据库表名称注解<code>@Table("表名称")</code></li>
		 * <li>实体属性名和对应的表字段名建议保持一致，否则须添加表字段名注解<code>@Column("列名称")</code></li>
		 * <li>数据库表中必须含有id,createTime,updateTime,deleteStatus等几个字段</li>
		 * <li>需要注意MySQL字段类型和Java字段类型转换问题，如tinyint(1)-Boolean, tinyint(2)-Integer，可以在实体中增加对应字段相关兼容类型的Setter</li>
    	 */
    	
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
    @RequestMapping(value = "/demo/example/db/sql.json", method = RequestMethod.POST)
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
	@RequestMapping(value = "/demo/example/db/proc.json", method = RequestMethod.POST)
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
    @RequestMapping(value = "/demo/example/db/dao.json", method = RequestMethod.POST)
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

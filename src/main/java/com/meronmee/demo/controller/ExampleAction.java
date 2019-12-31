package com.meronmee.demo.controller;

import com.meronmee.base.api.UserApi;
import com.meronmee.base.domain.User;
import com.meronmee.core.common.util.Assert;
import com.meronmee.core.web.dto.JsonResult;
import com.meronmee.core.web.dto.Success;
import com.meronmee.core.web.util.RequestInfoUtils;
import com.meronmee.core.web.util.RequestUtils;
import com.meronmee.core.web.util.ResponseUtils;
import com.meronmee.demo.api.DemoApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 基本用法示例，带有详细的注释讲解
 * @author Meron
 *
 */
//不要在Controller的类层面添加@RequestMapping，因为不利于代码阅读和问题排查
@Controller
public class ExampleAction {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserApi userApi;

    @Autowired
    private DemoApi demoApi;

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
        demoApi.testDBModel(RequestInfoUtils.getRequestInfo(request));
        return new Success();
    }

    /**
	 * MyBatis数据库查询 - 2、使用公共接口引用 SQL 语句的增删改查<p>
   	 */
    @RequestMapping(value = "/demo/example/db/sql.json", method = RequestMethod.POST)
    public JsonResult testDBSql(HttpServletRequest request, HttpServletResponse response) {
        demoApi.testDBSql(RequestInfoUtils.getRequestInfo(request));
        return new Success();
   	}


	 /**
	 * MyBatis数据库查询 - 3、使用 DAO 引用 SQL 语句的增删改查<p>
   	 */
    @RequestMapping(value = "/demo/example/db/dao.json", method = RequestMethod.POST)
    public JsonResult testDBDao(HttpServletRequest request, HttpServletResponse response) {
    	List<User> list = this.userApi.getUserList(5, 3);
        return new Success(list);

    	/*
    	demoApi.testDBDao(RequestInfoUtils.getRequestInfo(request));
    	return new Success();
    	*/
   	}
    
}

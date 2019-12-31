package ${basePackage!}.manage.controller;


import com.alibaba.fastjson.JSONObject;
import ${basePackage!}.base.api.UserApi;
import ${basePackage!}.base.domain.User;
import ${basePackage!}.core.common.util.Assert;
import ${basePackage!}.core.web.dto.AuthError;
import ${basePackage!}.core.web.dto.JsonResult;
import ${basePackage!}.core.web.dto.Success;
import ${basePackage!}.core.web.shiro.UserHolder;
import ${basePackage!}.core.web.util.RequestUtils;
import ${basePackage!}.${modelVarName!}.api.${modelClassName!}Api;
import ${basePackage!}.${modelVarName!}.domain.${modelClassName!};
import org.apache.shiro.authz.UnauthenticatedException;
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
 * ${modelCNName!}管理端控制器
 * @author Meron
 *
 */
@Controller
public class Manage${modelClassName!}Action {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${modelClassName!}Api ${modelVarName!}Api;
    @Autowired
    private UserApi userApi;

    
    /**
	 * ${modelCNName!}页面
	 */
    @RequestMapping(value = "/app/${modelVarName!}/demo.htm",  method = RequestMethod.GET)
    public String ${modelVarName!}Page(HttpServletRequest request, HttpServletResponse response, ModelMap viewData){
        User user = UserHolder.getCurrentUser();
        if(user == null){
            throw new UnauthenticatedException("请登录后再操作");
        }

    	//参数处理
		String aaa = RequestUtils.getStringParam(request, "aaa");
		Long bbb = RequestUtils.getLongParam(request, "bbb");
		Integer ccc = RequestUtils.getIntegerParam(request, "ccc");	

		Assert.isNotBlank(aaa, "aaa不能为空");
		Assert.isNotNull0(ccc, "ccc不能为空");
			
		//业务逻辑
    	${modelClassName!} ${modelVarName!} = this.${modelVarName!}Api.retrieve(1L);
    	
        viewData.put("${modelVarName!}", ${modelVarName!}); 
        
        //返回模板
        return "${modelVarName!}/${modelVarName!}_demo";
    }  

    /**
	 * ${modelCNName!}数据
	 */
    @RequestMapping(value = "/app/${modelVarName!}/demo.json", method = RequestMethod.POST)
    public JsonResult ${modelVarName!}Data(HttpServletRequest request, HttpServletResponse response) {
        User user = UserHolder.getCurrentUser();
        if(user == null){
            return new AuthError();
        }

    	//参数处理
    	String aaa = RequestUtils.getStringParam(request, "aaa");
		Long bbb = RequestUtils.getLongParam(request, "bbb");
		Integer ccc = RequestUtils.getIntegerParam(request, "ccc");	

		Assert.isNotBlank(aaa, "aaa不能为空");
		Assert.isNotNull0(ccc, "ccc不能为空");
		
    	//业务逻辑
        ${modelClassName!} ${modelVarName!} = this.${modelVarName!}Api.retrieve(1L);

        JSONObject result = new JSONObject();
		result.put("key", "value");	
		
		//返回
    	return new Success(result); 
    }
}

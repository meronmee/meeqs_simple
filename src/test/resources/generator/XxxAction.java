package ${basePackage!}.${moduleRootPackage!}.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import ${basePackage!}.base.controller.BaseAction;
import ${basePackage!}.base.model.User;
import ${basePackage!}.base.model.${modelClassName!};
import ${basePackage!}.core.dto.JsonResult;
import ${basePackage!}.core.dto.Success;
import ${basePackage!}.core.service.MyService;
import ${basePackage!}.core.utils.Assert;
import ${basePackage!}.core.utils.RequestUtils;
import ${basePackage!}.${moduleRootPackage!}.service.${moduleClassPrefix!}${modelClassName!}Service;

import freemarker.template.TemplateModelException;

/**
 * ${modelCNName!} ${moduleClassPrefix!} 端控制器
 * @author Meron
 *
 */
@Controller
public class ${moduleClassPrefix!}${modelClassName!}Action extends BaseAction {
    
    @Autowired
	protected MyService service;    
    
    @Autowired
	protected ${moduleClassPrefix!}${modelClassName!}Service ${moduleRootPackage!}${modelClassName!}Service; 

    
    /**
	 * ${modelCNName!}页面
	 */
    @RequestMapping(value = "/${moduleRootPackage!}/${modelVarName!}/demo.htm",  method = RequestMethod.GET)
    public String ${modelVarName!}Page(HttpServletRequest request, HttpServletResponse response, ModelMap viewData){
    	//参数处理
		String aaa = RequestUtils.getStringParam(request, "aaa");
		Long bbb = RequestUtils.getLongParam(request, "bbb");
		Integer ccc = RequestUtils.getIntegerParam(request, "ccc");	

		Assert.isNotBlank(aaa, "aaa不能为空");
		Assert.isNotNull0(ccc, "ccc不能为空");
			
		//业务逻辑
    	${modelClassName!} ${modelVarName!} = this.service.retrieveModel(${modelClassName!}.class, 1L);
    	
        viewData.put("${modelVarName!}", ${modelVarName!}); 
        
        //返回模板
        return "${moduleRootPackage!}/${modelVarName!}/${modelVarName!}_demo";
    }  

    /**
	 * ${modelCNName!}数据
	 */
    @RequestMapping(value = "/${moduleRootPackage!}/${modelVarName!}/demo.json", method = RequestMethod.POST)
    public JsonResult ${modelVarName!}Data(HttpServletRequest request, HttpServletResponse response) {
    	//参数处理
    	String aaa = RequestUtils.getStringParam(request, "aaa");
		Long bbb = RequestUtils.getLongParam(request, "bbb");
		Integer ccc = RequestUtils.getIntegerParam(request, "ccc");	

		Assert.isNotBlank(aaa, "aaa不能为空");
		Assert.isNotNull0(ccc, "ccc不能为空");
		
    	//业务逻辑
		JSONObject result = new JSONObject();
		result.put("key", "value");	
		
		//返回
    	return new Success(result); 
    }
}

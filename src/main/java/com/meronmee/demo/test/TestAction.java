package com.meronmee.demo.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meronmee.base.controller.BaseAction;
import com.meronmee.base.model.User;
import com.meronmee.core.dto.JsonResult;
import com.meronmee.core.dto.Success;
import com.meronmee.core.utils.Assert;
import com.meronmee.core.utils.HttpUtils;
import com.meronmee.core.utils.RequestUtils;
import com.meronmee.core.utils.SettingHolder;
import com.meronmee.core.utils.SysPropHolder;

/**
 * 基本用法示例
 * @author Meron
 *
 */
@Controller
public class TestAction extends BaseAction{
		
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
    @RequestMapping(value = "/demo/test/test.json", method = RequestMethod.POST)
    public JsonResult testAjax(HttpServletRequest request, HttpServletResponse response) {
        String msg = RequestUtils.getStringParam(request, "msg");
        Assert.isNotBlank(msg, "msg不能为空");
        log.info("test.arg1:{}", SettingHolder.getString("test.arg1"));              
        log.info("SysPropHolder#system.arg1:{}", SysPropHolder.getString("system.arg1"));
            
        User user = this.service.retrieveModel(User.class, 2L);
        log.info(user.getUsername());
        
        try {
			HttpUtils.post("http://yscmall.yijiahn.com/mall/demo/test.json", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	return new Success(); 
    }
        
}

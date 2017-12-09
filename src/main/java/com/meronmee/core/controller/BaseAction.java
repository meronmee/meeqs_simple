package com.meronmee.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.meronmee.core.dao.CommonDao;
import com.meronmee.core.service.MyService;

import java.util.List;

/**
 * Controller 基类
 * @author Meron
 *
 */
@Controller
public class BaseAction {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	protected MyService service;
    
}

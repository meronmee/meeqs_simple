package com.meronmee.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meronmee.core.service.MyService;

/**
 * Service 基类
 * @author Meron
 *
 */
@Service
public class BaseService {
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	protected MyService service;
}

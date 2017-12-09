package com.meronmee.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service 基类
 * @author Meron
 *
 */
@Service
public class BaseService {
    public final Logger log = LoggerFactory.getLogger(this.getClass());
}

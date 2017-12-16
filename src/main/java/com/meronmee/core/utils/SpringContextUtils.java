package com.meronmee.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Spring 上下文工具类
 * @author Meron
 *
 */
@Service
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

    @Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据名称获取bean
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		if (applicationContext == null){
             return null;
        }
		return (T) applicationContext.getBean(name); 
	}
	/**
	 * 根据类型获取bean
	 * @param clazz
	 * @return
	 * @throws BeansException
	 */
	public static <T> T getBean(Class<T> clazz) throws BeansException {
		if (applicationContext == null){
             return null;
        }
		return applicationContext.getBean(clazz); 
	}
}
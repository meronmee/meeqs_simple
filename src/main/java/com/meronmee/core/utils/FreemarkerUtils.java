package com.meronmee.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * Freemarker模板工具类
 * @author Meron
 *
 */
public final class FreemarkerUtils {
	private static final Logger log = LoggerFactory.getLogger(FreemarkerUtils.class);

    /**
     * 包装静态工具类以便可在Freemarker模板中直接使用
     * @param clazz - 要包装的静态工具类
     * @return 静态工具类的包装模型
     */
    @SuppressWarnings("deprecation")
    public static TemplateHashModel wrapperStaticTool(Class<?> clazz) {
    	try {
    		TemplateHashModel staticTool = (TemplateHashModel) BeansWrapper.getDefaultInstance().getStaticModels().get(clazz.getName());
    		return staticTool;
		} catch (TemplateModelException e) {
			log.error("向Freemarker模板中添加静态工具类({})失败", clazz.getName(), e);
			return null;
		}
    }
}

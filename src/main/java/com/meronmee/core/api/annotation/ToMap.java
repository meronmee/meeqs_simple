package com.meronmee.core.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToMap {
	
	/**
	 * 自身toMap时是否忽略, ignore为true时将忽略deep
	 * 默认不忽略
	 */
	boolean ignore() default false;
	
	/**
	 * 将日期时间转换为毫秒值
	 * @return
	 */
	boolean longtime() default false;
	
	/**
	 * 将日期时间转换为yyyy-MM-dd
	 * @return
	 */
	boolean date() default false;
	/**
	 * 将日期时间转换为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	boolean time() default false;
	
	
}
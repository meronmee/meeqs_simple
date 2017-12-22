package com.meronmee.core.commons;

/**
 * 常量
 * @author Meron
 *
 */
public class Const {
	/**
	 * MyBatis事务管理器
	 * @see src/main/resources/spring/spring-db.xml#transactionManager
	 */
	public static final String MYBATIS_TRANSACTION_MANAGER = "transactionManager";
	
	/**
	 * MD5、SHA1等加密盐
	 */
	public static final String MESSAGE_DIGEST_SALT = "";
	
	/**
	 * 404/500等错误跳转前在session中记录来源地址
	 */
	public static final String SESSION_KEY_ERROR_SOURCE_URL = "__sourceURL";
}
package com.meronmee.core.service.database.constant;

/**
 * 常量
 * @author Meron
 *
 */
public class DbConst {
	/**
	 * MyBatis事务管理器
	 * @see src/main/resources/spring/spring-db.xml#transactionManager
	 */
	public static final String MYBATIS_TRANSACTION_MANAGER = "transactionManager";
	
	/*---------SqlKeys-------------*/
	/** 根据ID查询实体 */
	public static final String sqlKeyRetrieve = "commonMapper.retrieve";
	
    /** 根据某个属性不分页查询实体列表 */
	public static final String sqlKeyFindByProperty = "commonMapper.findByProperty";
	
	/** 根据某个属性查询一个实体 */
	public static final String sqlKeyFindOneByProperty = "commonMapper.findOneByProperty";
	
	/** 根据一组属性不分页查询实体列表 */
	public static final String sqlKeyFindByProps = "commonMapper.findByProps";
	
	/** 根据一组属性查询一个实体 */
	public static final String sqlKeyFindOneByProps = "commonMapper.findOneByProps";
	
	/** 新增一条表记录 */
	public static final String sqlKeyCreate = "commonMapper.create";
	
	/** 更新实体 */	
	public static final String sqlKeyUpdate = "commonMapper.update";
	
	/** 逻辑删除实体 */
	public static final String sqlKeyDelete = "commonMapper.delete";
	
	/** 物理删除实体 */
	public static final String sqlKeyDeletePhysically = "commonMapper.deletePhysically";
	
}
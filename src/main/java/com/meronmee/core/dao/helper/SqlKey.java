package com.meronmee.core.dao.helper;

/**
 * SQL语句ID(Mapper中的ID)枚举值列表
 * @author Meron
 *
 */
public enum SqlKey {
	/** Common - 根据ID查询实体 */
	common_retrieve("commonMapper.retrieve"),
	
	/** Common - 根据某个属性不分页查询实体列表 */
	common_findByProperty("commonMapper.findByProperty"),
	
	/** Common - 根据某个属性查询一个实体 */
	common_findOneByProperty("commonMapper.findOneByProperty"),
	
	/** Common - 新增一条表记录 */
	common_create("commonMapper.create"),
	
	/** Common - 更新实体 */	
	common_update("commonMapper.update"),
	
	/** Common - 逻辑删除实体 */
	common_delete("commonMapper.delete"),
	
	/** Common - 物理删除实体 */
	common_deletePhysically("commonMapper.deletePhysically"),
	
	
	
	/** Demo - 测试存储过程 */
	demo_testProc("demoMapper.testProc"),
	;
	
	/**Key对应的SQL语句ID*/
	private String id;
	
	//构造函数，枚举类型只能为私有
    private SqlKey(String id) {
        this.id = id;
    }
    
    /**Key对应的SQL语句ID*/
    public String getId(){
    	return this.id;
    }	   
}

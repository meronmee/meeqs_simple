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
	
	/** Common - 根据一组属性不分页查询实体列表 */
	common_findByProps("commonMapper.findByProps"),
	
	/** Common - 根据一组属性查询一个实体 */
	common_findOneByProps("commonMapper.findOneByProps"),
	
	/** Common - 新增一条表记录 */
	common_create("commonMapper.create"),
	
	/** Common - 更新实体 */	
	common_update("commonMapper.update"),
	
	/** Common - 逻辑删除实体 */
	common_delete("commonMapper.delete"),
	
	/** Common - 物理删除实体 */
	common_deletePhysically("commonMapper.deletePhysically"),
	
	//---------------------------------
	
	/** Demo - 测试存储过程 */
	demo_testProc("demoMapper.testProc"),
	/** Demo - 测试分页查询 */
	demo_demoQueryList("demoMapper.demoQueryList"),
	/** Demo - 测试分页统计 */
	demo_demoQueryCount("demoMapper.demoQueryCount"),
	/** Demo - 测试批量新增 */
	demo_createUserBatch("demoMapper.createUserBatch"),
	/** Demo - 测试新增实体 */
	demo_createUser("demoMapper.createUser"),
	/** Demo - 测试更新实体 */
	demo_updateUser("demoMapper.updateUser"),
	
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

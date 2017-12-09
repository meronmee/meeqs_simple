package com.meronmee.core.dao;

import java.util.List;

import com.meronmee.core.dao.helper.Pager;
import com.meronmee.core.dao.helper.SqlKey;


/**
 * MyBatis 公共 DAO 接口
 * @author Meron
 *
 */
public interface CommonDao{
	
	//-------------不分页查询---------------------
	/**
	 * 不分页查询记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	public <T> List<T> find(SqlKey key, Object params);
	
	/**
	 * 查询一条记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	public <T> T findOne(SqlKey key, Object params);


	//-------------分页查询---------------------
	/**
	 * 
	 * 分页查询记录(需要统计总数)
	 * @param countKey 	- 用于统计数目的SQL语句ID(Mapper中的ID)
	 * @param queryKey 	- 用于查询记录的SQL语句ID(Mapper中的ID)
	 * @param pageNo   	- 页码
	 * @param pageSize 	- 每页最大记录条数
	 * @param params   	- 参数
	 * @return
	 */
	public <T> Pager<T> query(SqlKey countKey, SqlKey queryKey, int pageNo, int pageSize, Object params);
	/**
	 * 
	 * 分页查询记录(不统计总数)
	 * @param key 		- 用于查询记录的SQL语句ID(Mapper中的ID)
	 * @param pageNo   	- 页码
	 * @param pageSize 	- 每页最大记录条数
	 * @param params   	- 参数
	 * @return
	 */
	public <T> Pager<T> query(SqlKey key, int pageNo, int pageSize, Object params);
	

	//-------------统计---------------------	
	/**
	 * 查询记录条数
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return
	 */
	public long count(SqlKey key, Object params);
	
	
	//-------------新增---------------------		
	/**
	 * 新增记录
	 * @param key		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return 新增成功的数量
	 */
	public int create(SqlKey key, Object params);
	

	//-------------更新---------------------		
	/**
	 * 更新记录
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	要更新的实体
	 * @return 更新成功的数量
	 */
	public int update(SqlKey key, Object params);
	
	
	//-------------删除---------------------	
	/**
	 * 根据参数删除对象
	 * @param key
	 * @param params
	 * @return  删除的对象数量
	 */
	public int delete(SqlKey key, Object params);
	
	/**
     * 删除指定的唯一标识符对应的持久化对象
     *
     * @param id 指定的唯一标识符
	 * @return 删除的对象数量
     */
	public int deleteById(SqlKey key, Long id);

    /**
     * 删除指定的唯一标识符列表对应的持久化对象
     *
     * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
     */
	public int deleteByIds(SqlKey key,  List<Long> ids);
	
}

package com.meronmee.core.service.database.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.meronmee.core.api.domain.Pager;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.common.util.PropertyHolder;
import com.meronmee.core.service.database.CommonDao;
 

/**
 * MyBatis 公共 DAO 实现
 * @author Meron
 *
 */
@Repository
public class CommonDaoImpl implements CommonDao{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
		
	//-------------不分页查询---------------------
	/**
	 * 不分页查询记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	@Override
	public <T> List<T> find(String key, Object params){
		return sqlSessionTemplate.selectList(key, params);
	}

	/**
	 * 查询一条记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	@Override
	public <T> T findOne(String key, Object params){
		T t = sqlSessionTemplate.selectOne(key, params);
		return t;
	}
	
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
	@Override
	public <T> Pager<T> query(String countKey, String queryKey, int pageNo, int pageSize, Object params){
		Long total = PropertyHolder.getLong("db.query.page.fixed.total");
		long totalCount  = 1000L; 
		if(total != null){
			totalCount = total.longValue();
		} else {
			totalCount = count(countKey, params);
		}
		
		if(totalCount < 1){
			return new Pager<T>();
		}
		//分页条件，配合com.github.pagehelper.PageHelper插件使用
		int start = Pager.getStartOfPage(pageNo, pageSize) - 1;
		
		RowBounds rowBounds = new RowBounds(start, pageSize);	
		
		List<T> records = sqlSessionTemplate.selectList(queryKey, params, rowBounds);
		
		return new Pager<T>(pageNo, pageSize, records, totalCount);
	}

	/**
	 * 
	 * 分页查询记录(不统计总数)
	 * @param key 		- 用于查询记录的SQL语句ID(Mapper中的ID)
	 * @param pageNo   	- 页码
	 * @param pageSize 	- 每页最大记录条数
	 * @param params   	- 参数
	 * @return
	 */
	@Override
	public <T> Pager<T> query(String key, int pageNo, int pageSize, Object params){
		//分页条件，配合com.github.pagehelper.PageHelper插件使用
		int start = Pager.getStartOfPage(pageNo, pageSize) - 1;
	
		RowBounds rowBounds = new RowBounds(start, pageSize);				
		
		List<T> records = sqlSessionTemplate.selectList(key, null, rowBounds);
		
		return new Pager<T>(pageNo, pageSize, records);
	}
	
	//-------------统计--------------------
	/**
	 * 查询记录条数
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return
	 */
	@Override
	public long count(String key, Object params){
		return BaseUtils.toLong(sqlSessionTemplate.selectOne(key, params), 0L);
	}
	
	
	//-------------新增---------------------		
	/**
	 * 新增记录
	 * @param key		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return 新增成功的数量
	 */
	@Override
	public int create(String key, Object params){
		return sqlSessionTemplate.insert(key, params);		
	}	
	
	//-------------更新---------------------		
	/**
	 * 更新记录
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	要更新的实体
	 * @return 更新成功的数量
	 */
	@Override
	public int update(String key, Object params){
		return sqlSessionTemplate.update(key, params);
	}
	
	//-------------删除---------------------	
	/**
     * 删除指定的唯一标识符对应的持久化对象
     *
     * @param id 指定的唯一标识符
	 * @return 删除的对象数量
     */
	@Override
	public int deleteById(String key, Long id){
		if(BaseUtils.isNull0(id)){
			return 0;
		}
		return sqlSessionTemplate.delete(key, id);
	}

	 /**
     * 删除指定的唯一标识符列表对应的持久化对象
     *
     * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
     */
	@Override
	public int deleteByIds(String key,  List<Long> ids){
		if(BaseUtils.isEmpty(ids)){
			return 0;
		}	
		return sqlSessionTemplate.delete(key, ids);
	}
	
	/**
	 * 根据参数删除对象
	 * @param key
	 * @param params
	 * @return  删除的对象数量
	 */
	@Override
	public int delete(String key, Object params){
		return sqlSessionTemplate.delete(key, params);
	}
	
}

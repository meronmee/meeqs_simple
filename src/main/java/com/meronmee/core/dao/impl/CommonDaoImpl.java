package com.meronmee.core.dao.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.meronmee.core.dao.CommonDao;
import com.meronmee.core.dao.helper.Pager;
import com.meronmee.core.dao.helper.SqlKey;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.SettingHolder;

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
	public <T> List<T> find(SqlKey key, Object params){
		return sqlSessionTemplate.selectList(key.getId(), params);
	}

	/**
	 * 查询一条记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	@Override
	public <T> T findOne(SqlKey key, Object params){
		T t = sqlSessionTemplate.selectOne(key.getId(), params);
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
	public <T> Pager<T> query(SqlKey countKey, SqlKey queryKey, int pageNo, int pageSize, Object params){
		Long total = SettingHolder.getLong("db.query.page.fixed.total");
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
		
		List<T> records = sqlSessionTemplate.selectList(queryKey.getId(), params, rowBounds);
		
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
	public <T> Pager<T> query(SqlKey key, int pageNo, int pageSize, Object params){
		//分页条件，配合com.github.pagehelper.PageHelper插件使用
		int start = Pager.getStartOfPage(pageNo, pageSize) - 1;
	
		RowBounds rowBounds = new RowBounds(start, pageSize);				
		
		List<T> records = sqlSessionTemplate.selectList(key.getId(), null, rowBounds);
		
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
	public long count(SqlKey key, Object params){
		return BaseUtils.toLong(sqlSessionTemplate.selectOne(key.getId(), params), 0L);
	}
	
	
	//-------------新增---------------------		
	/**
	 * 新增记录
	 * @param key		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return 新增成功的数量
	 */
	@Override
	public int create(SqlKey key, Object params){
		return sqlSessionTemplate.insert(key.getId(), params);		
	}	
	
	//-------------更新---------------------		
	/**
	 * 更新记录
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	要更新的实体
	 * @return 更新成功的数量
	 */
	@Override
	public int update(SqlKey key, Object params){
		return sqlSessionTemplate.update(key.getId(), params);
	}
	
	//-------------删除---------------------	
	/**
     * 删除指定的唯一标识符对应的持久化对象
     *
     * @param id 指定的唯一标识符
	 * @return 删除的对象数量
     */
	@Override
	public int deleteById(SqlKey key, Long id){
		if(BaseUtils.isNull0(id)){
			return 0;
		}
		return sqlSessionTemplate.delete(key.getId(), id);
	}

	 /**
     * 删除指定的唯一标识符列表对应的持久化对象
     *
     * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
     */
	@Override
	public int deleteByIds(SqlKey key,  List<Long> ids){
		if(BaseUtils.isEmpty(ids)){
			return 0;
		}	
		return sqlSessionTemplate.delete(key.getId(), ids);
	}
	
	/**
	 * 根据参数删除对象
	 * @param key
	 * @param params
	 * @return  删除的对象数量
	 */
	@Override
	public int delete(SqlKey key, Object params){
		return sqlSessionTemplate.delete(key.getId(), params);
	}
	
}

package com.meronmee.core.service.database;

import java.util.List;
import java.util.Map;

import com.meronmee.core.api.domain.Model;
import com.meronmee.core.api.domain.Pager;


/**
 * MyBatis常用公共数据操作服务类<p>
 *
 * 提供两大类的接口：
 * 		<li>面向 Model 对象的增删改查</li>
 *  	<li>引用 SQL 语句的增删改查</li>
 *
 * <h3>一、面向 Model 对象的增删改查</h3>
 *
 * 使用此类接口的前提是：<p>
 * <li>实体必须继承com.meronmee.core.api.domain.Model</li>
 * <li>实体必须含有数据库表名称注解<code>@Table("表名称")</code></li>
 * <li>实体属性名和对应的表字段名建议保持一致，否则须添加表字段名注解<code>@Column("列名称")</code></li>
 * <li>数据库表中必须含有id,createTime,updateTime,deleteStatus等几个字段</li>
 * <li>需要注意MySQL字段类型和Java字段类型转换问题，如tinyint(1)-Boolean, tinyint(2)-Integer，可以在实体中增加对应字段相关兼容类型的Setter</li>
 *
 *
 * <h3>二、引用 SQL 语句的增删改查</h3>
 * {@literal 此类接口的方法名称中都包含UseSql，第一个参数都是String key}<p>
 *
 * 此类接口直接使用Mapper中的SQL语句，功能强大，可以做任何SQL增删改查
 *
 * @author Meron
 *
 */
public interface DataService {

	/*----------------------------------------*
	 * 		1、面向 Model 对象的增删改查		  *
	 *----------------------------------------*/	
	/**
	 * 根据ID查询实体
	 * @param modelClass 实体类
     * @param modelId 实体ID
	 * @return Model类型实体
	 */
	public <T extends Model> T retrieve(Class<T> modelClass, Long modelId);
	
	
	/**
	 * 根据某个属性不分页查询实体列表
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值, 如果是List类型，则会使用IN查询
	 * @return Model类型实体列表
	 */
	public <T extends Model> List<T> findByProperty(Class<T> modelClass, String propertyName, Object propertyValue);

		
	/**
	 * 根据某个属性查询一个实体
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值
	 * @return Model类型实体
	 */
	public <T extends Model> T findOneByProperty(Class<T> modelClass, String propertyName, Object propertyValue);

	/**
	 * 根据一组属性不分页查询实体Map列表
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
   	 * @return Model类型实体列表
   	 */
	public <T extends Model> List<T> findByProps(Class<T> modelClass, Map<String, Object> params);
	
	
	/**
	 * 根据一组属性查询一个实体
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
   	 * @return Model类型实体列表
   	 */
	public <T extends Model> T findOneByProps(Class<T> modelClass, Map<String, Object> params);
	
	/**
	 * 新增一条实体
	 * @param modelClass 实体类
	 * @param model 要新增的实体
	 * @return 新增成功的记录数
	 */
	public <T extends Model> int create(T model);
			
	
	/**
	 * 更新一条实体
	 * @param modelClass 实体类
	 * @param model 要更新的实体
	 * @return 更新成功的记录数
	 */
	public <T extends Model> int update(T model);
	

    /**
	 * 部分更新一条实体
	 * @param model 要更新的实体
	 * @param propNames 要更新的属性字段名称，逗号分割
	 * @return 更新成功的记录数
	 */ 
	public <T extends Model> int updatePart(T model, String propNames);
	
	/**
	 * 逻辑删除实体
	 * @param model 要删除的实体
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int delete(T model);
	/**
	 * 根据ID逻辑删除实体
	 * @param modelClass 实体类
	 * @param modelId 要删除的实体ID
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int deleteById(Class<T> modelClass, Long modelId);
	

	/**
	 * 根据ID逻辑删除实体
	 * @param modelClass 实体类
	 * @param modelIds 要删除的实体ID列表
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int deleteByIds(Class<T> modelClass, List<Long> modelIds);
	
	/**
	 * 物理删除实体
	 * @param model 要删除的实体
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int deletePhysically(T model);

	/**
	 * 根据ID物理删除实体
	 * @param modelClass 实体类
	 * @param modelId 要删除的实体ID
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int deletePhysicallyById(Class<T> modelClass, Long modelId);

	/**
	 * 根据ID物理删除实体
	 * @param modelClass 实体类
	 * @param modelIds 要删除的实体ID列表
	 * @return 删除成功的记录数
	 */
	public <T extends Model> int deletePhysicallyByIds(Class<T> modelClass, List<Long> modelIds);
	
	

	/*----------------------------------------*
	 * 		 2、引用 SQL 语句的增删改查		  *
	 *----------------------------------------*/

	//-------------不分页查询---------------------
	/**
	 * 不分页查询记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	public <T> List<T> findUseSql(String key, Object params);
	
	/**
	 * 查询一条记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	public <T> T findOneUseSql(String key, Object params);

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
	public <T> Pager<T> queryUseSql(String countKey, String queryKey, int pageNo, int pageSize, Object params);
	/**
	 * 
	 * 分页查询记录(不统计总数)
	 * @param key 		- 用于查询记录的SQL语句ID(Mapper中的ID)
	 * @param pageNo   	- 页码
	 * @param pageSize 	- 每页最大记录条数
	 * @param params   	- 参数
	 * @return
	 */
	public <T> Pager<T> queryUseSql(String key, int pageNo, int pageSize, Object params);

	//-------------统计---------------------	
	/**
	 * 查询记录条数
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return
	 */
	public long countUseSql(String key, Object params);
	
	
	//-------------新增---------------------		
	/**
	 * 新增记录
	 * @param key		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return 新增成功的数量
	 */
	public int createUseSql(String key, Object params);
	

	//-------------更新---------------------		
	/**
	 * 更新记录
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	要更新的实体
	 * @return 更新成功的数量
	 */
	public int updateUseSql(String key, Object params);
	
	
	//-------------删除---------------------	
	/**
	 * 根据参数删除对象
     * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params
	 * @return  删除的对象数量
	 */
	public int deleteUseSql(String key, Object params);
	
	/**
     * 删除指定的唯一标识符对应的持久化对象
     *
     * @param key 		SQL语句ID(Mapper中的ID)
     * @param id 指定的唯一标识符
	 * @return 删除的对象数量
     */
	public int deleteByIdUseSql(String key, Long id);

    /**
     * 删除指定的唯一标识符列表对应的持久化对象
     *
     * @param key 		SQL语句ID(Mapper中的ID)
     * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
     */
	public int deleteByIdsUseSql(String key,  List<Long> ids);
 
}

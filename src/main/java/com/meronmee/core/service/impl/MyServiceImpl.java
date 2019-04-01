package com.meronmee.core.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.meronmee.core.annotation.AttrIgnore;
import com.meronmee.core.annotation.Column;
import com.meronmee.core.annotation.Table;
import com.meronmee.core.annotation.ToMapUtils;
import com.meronmee.core.commons.Const;
import com.meronmee.core.dao.CommonDao;
import com.meronmee.core.dao.helper.Pager;
import com.meronmee.core.dao.helper.SqlKey;
import com.meronmee.core.model.Model;
import com.meronmee.core.service.MyService;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.ReflectionUtils;


/**
 * MyBatis常用公共数据操作服务类<p>
 * 
 * 提供两大类的接口：
 * 		<li>面向 Model 对象的增删改查</li>
 *  	<li>引用 SQL 语句的增删改查</li>
 *  
 * <h3>一、面向 Model 对象的增删改查</h3>
 * {@literal 此类接口的方法名称都有Map或Model，第一个参数都是Class<T> modelClass}<p>
 * 
 * 使用此类接口的前提是：<p>
 * <li>实体必须继承com.meronmee.core.model.Model</li>
 * <li>实体必须含有数据库表名称注解<code>@Table("表名称")</code></li>
 * <li>实体属性名和对应的表字段名建议保持一致，否则须添加表字段名注解<code>@Column("列名称")</code></li>
 * <li>数据库表中必须含有id,createTime,updateTime,deleteStatus等几个字段</li>
 * <li>需要注意MySQL字段类型和Java字段类型转换问题，如tinyint(1)-Boolean, tinyint(2)-Integer，可以在实体中增加对应字段相关兼容类型的Setter</li>
 * 
 * 
 * <h3>二、引用 SQL 语句的增删改查</h3>
 * {@literal 此类接口的方法名称都比较简短，第一个参数都是SqlKey key}<p>
 * 
 * 此类接口直接使用Mapper中的SQL语句，功能强大，可以做任何SQL增删改查
 * 
 * @author Meron
 *
 */
@Service
public class MyServiceImpl implements MyService {
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
	@Autowired
	private CommonDao commonDao;

	/*----------------------------------------*
	 * 		1、面向 Model 对象的增删改查		  *
	 *----------------------------------------*/
	
	/**
	 * 根据ID查询实体Map
	 * @param modelClass 实体类
     * @param modelId 实体ID
	 * @return Model类型实体对应的Map
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> Map<String, Object> retrieveMap(Class<T> modelClass, Long modelId) {	
		if(modelClass == null || BaseUtils.isNull0(modelId)){
			return null;
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", this.getTableName(modelClass));
		params.put("selectColumnNames", this.getSelectColumnNames(modelClass));
		params.put("id", modelId);
				
		Map<String, Object> map = this.commonDao.findOne(SqlKey.common_retrieve, params);
		
		return map;
	}
	
	/**
	 * 根据ID查询实体
	 * @param modelClass 实体类
     * @param modelId 实体ID
	 * @return Model类型实体
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> T retrieveModel(Class<T> modelClass, Long modelId) {	
		if(modelClass == null || BaseUtils.isNull0(modelId)){
			return null;
		}
		
		Map<String, Object> map = this.retrieveMap(modelClass, modelId);
		
		T model = mapToModel(modelClass, map);
		return model;
	}
	
	
	/**
	 * 根据某个属性不分页查询实体Map列表
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值, 如果是List类型，则会使用IN查询
	 * @return Model类型实体对应的Map列表
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> List<Map<String, Object>> findMapByProperty(Class<T> modelClass, String propertyName, Object propertyValue){
		if(modelClass == null || BaseUtils.isBlank(propertyName) || propertyValue==null){
			return new ArrayList<>();
		}
		if((propertyValue instanceof List) && ((List<?>)propertyValue).isEmpty()){
			return new ArrayList<>();
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", this.getTableName(modelClass));
		params.put("selectColumnNames", this.getSelectColumnNames(modelClass));
		params.put("columnName", this.getColumnName(modelClass, propertyName));
		if(propertyValue instanceof List){
			params.put("columnValues", propertyValue);			
		} else {			
			params.put("columnValue", propertyValue);
		}
				
		List<Map<String, Object>> list = this.commonDao.find(SqlKey.common_findByProperty, params);
		
		return list;
	}
	/**
	 * 根据某个属性不分页查询实体列表
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值, 如果是List类型，则会使用IN查询
	 * @return Model类型实体列表
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> List<T> findModelByProperty(Class<T> modelClass, String propertyName, Object propertyValue){
		if(modelClass == null || StringUtils.isBlank(propertyName) || propertyValue==null){
			return new ArrayList<>();
		}
		if((propertyValue instanceof List) && ((List<?>)propertyValue).isEmpty()){
			return new ArrayList<>();
		}
				
		List<Map<String, Object>> list = this.findMapByProperty(modelClass, propertyName, propertyValue);
		List<T> result = mapToModel(modelClass, list);
		
		return result;
	}
	/**
	 * 根据某个属性查询一个实体Map
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值
	 * @return Model类型实体对应的Map
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> Map<String, Object> findOneMapByProperty(Class<T> modelClass, String propertyName, Object propertyValue){
		if(modelClass == null || StringUtils.isBlank(propertyName) || propertyValue==null){
			return null;
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", this.getTableName(modelClass));
		params.put("selectColumnNames", this.getSelectColumnNames(modelClass));
		params.put("columnName", this.getColumnName(modelClass, propertyName));
		params.put("columnValue", propertyValue);
				
		Map<String, Object> map = this.commonDao.findOne(SqlKey.common_findOneByProperty, params);
		
		return map;
	}
	
	/**
	 * 根据某个属性查询一个实体
	 * @param modelClass 实体类
	 * @param propertyName 参数名
	 * @param propertyValue 参数值
	 * @return Model类型实体
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> T findOneModelByProperty(Class<T> modelClass, String propertyName, Object propertyValue){
		if(modelClass == null || StringUtils.isBlank(propertyName) || propertyValue==null){
			return null;
		}
		
		Map<String, Object> map = this.findOneMapByProperty(modelClass, propertyName, propertyValue);
		
		T model = mapToModel(modelClass, map);
		
		return model;
	}
    
    /**
	 * 根据一组属性不分页查询实体Map列表
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
	 * @return Model类型实体对应的Map列表
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> List<Map<String, Object>> findMapByProps(Class<T> modelClass, Map<String, Object> params){
		if(modelClass==null){
			return new ArrayList<>();
		}

		List<Map<String, Object>> pairs = new LinkedList<>();
		if(params!=null && !params.isEmpty()){
	        for (Entry<String, Object> entry : params.entrySet()) {	            
	            String columnName = this.getColumnName(modelClass, entry.getKey());
	
	            Map<String, Object> pair = new HashMap<>();
	            pair.put("columnName", columnName);
	            pair.put("columnValue", entry.getValue());
	            
	            pairs.add(pair);
	        } 		
		}

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("tableName", this.getTableName(modelClass));
		sqlParams.put("selectColumnNames", this.getSelectColumnNames(modelClass));
		sqlParams.put("pairs", pairs);	
				
		List<Map<String, Object>> list = this.commonDao.find(SqlKey.common_findByProps, sqlParams);
		
		return list;
	}
    
    /**
	 * 根据一组属性不分页查询实体Map列表
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
   	 * @return Model类型实体列表
   	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> List<T> findModelByProps(Class<T> modelClass, Map<String, Object> params){
    	if(modelClass==null){
			return new ArrayList<>();
		}
						
		List<Map<String, Object>> list = this.findMapByProps(modelClass, params);
		List<T> result = mapToModel(modelClass, list);
		
		return result;
	}

    /**
	 * 根据一组属性查询一个实体
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
	 * @return Model类型实体对应的Map列表
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> Map<String, Object> findOneMapByProps(Class<T> modelClass, Map<String, Object> params){
		if(modelClass==null){
			return null;
		}

		List<Map<String, Object>> pairs = new LinkedList<>();
		if(params!=null && !params.isEmpty()){
	        for (Entry<String, Object> entry : params.entrySet()) {	            
	            String columnName = this.getColumnName(modelClass, entry.getKey());
	
	            Map<String, Object> pair = new HashMap<>();
	            pair.put("columnName", columnName);
	            pair.put("columnValue", entry.getValue());
	            
	            pairs.add(pair);
	        } 		
		}

		Map<String, Object> sqlParams = new HashMap<>();
		sqlParams.put("tableName", this.getTableName(modelClass));
		sqlParams.put("selectColumnNames", this.getSelectColumnNames(modelClass));
		sqlParams.put("pairs", pairs);	
				
		Map<String, Object> map = this.commonDao.findOne(SqlKey.common_findOneByProps, sqlParams);
		
		return map;
	}
    
    /**
	 * 根据一组属性查询一个实体
	 * @param modelClass 实体类
	 * @param params 参数键值对, 多个参数之间是 AND 关系。为null或为空查询全部
   	 * @return Model类型实体列表
   	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T extends Model> T findOneModelByProps(Class<T> modelClass, Map<String, Object> params){
    	if(modelClass==null){
			return null;
		}
						
    	Map<String, Object> map = this.findOneMapByProps(modelClass, params);
		T result = mapToModel(modelClass, map);
		
		return result;
	}

	/**
	 * 新增一条实体
	 * @param modelClass 实体类
	 * @param model 要新增的实体
	 * @return 新增成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int createModel(Class<T> modelClass, T model){
		if(modelClass == null || model==null){
			return 0;
		}
		StringBuilder columnNames = new StringBuilder();
		List<Object> values = new LinkedList<>();		
		
		List<Field> fields = ReflectionUtils.getDeclaredFields(modelClass);
		List<String> excludes = Arrays.asList("id");//要排除的字段
        for (Field field : fields) {
            if(!this.checkField(field, excludes)){
            	continue;
            }

            String columnName = this.getColumnName(modelClass, field);
            
            columnNames.append(",").append(columnName);
            
            Object value = ReflectionUtils.getFieldValue(model, field);
            values.add(value);
        }  

		Map<String, Object> params = new HashMap<>();
		params.put("tableName", this.getTableName(modelClass));			
		params.put("columnNames", columnNames.substring(1));
		params.put("columnValues", values);		
		
		int okCount = this.commonDao.create(SqlKey.common_create, params);
		
		model.setId(BaseUtils.toLong(params.get("id"), 0L));
		
		return okCount;
	}
			
	
	/**
	 * 更新一条实体
	 * @param modelClass 实体类
	 * @param model 要更新的实体
	 * @return 更新成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int updateModel(Class<T> modelClass, T model){
		if(modelClass == null || model==null ||BaseUtils.isNull0(model.getId())){
			return 0;
		} 
		
		List<Map<String, Object>> pairs = new LinkedList<>();		
		
		List<Field> fields = ReflectionUtils.getDeclaredFields(modelClass);
		List<String> excludes = Arrays.asList("id", "createTime", "updateTime");//要排除的字段
        for (Field field : fields) {
            if(!this.checkField(field, excludes)){
            	continue;
            }
            String columnName = this.getColumnName(modelClass, field);
            Object value = ReflectionUtils.getFieldValue(model, field);

            Map<String, Object> pair = new HashMap<>();
            pair.put("columnName", columnName);
            pair.put("columnValue", value);
            
            pairs.add(pair);
        }  

		Map<String, Object> params = new HashMap<>();
		params.put("id", model.getId());
		params.put("tableName", this.getTableName(modelClass));
		params.put("pairs", pairs);		

		model.setUpdateTime(new Date());
		
		return this.commonDao.update(SqlKey.common_update, params);
	}
    
    /**
	 * 部分更新一条实体
	 * @param model 要更新的实体
	 * @param propNames 要更新的属性字段名称，逗号分割
	 * @return 更新成功的记录数
	 */
	@Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int updatePart(T model, String propNames){
		if(model==null || BaseUtils.isNull0(model.getId())  || BaseUtils.isBlank(BaseUtils.formatList(propNames))){
			return 0;
		}

		Set<String> propNameSet = BaseUtils.getStrSet(propNames);
		if(propNameSet.isEmpty()){
			return 0;
		}

		Class modelClass = model.getClass();

		List<Map<String, Object>> pairs = new LinkedList<>();

		List<Field> fields = ReflectionUtils.getDeclaredFields(modelClass);
		List<String> excludes = Arrays.asList("id", "createTime", "updateTime");//要排除的字段
		for (Field field : fields) {
			if(!this.checkField(field, excludes)){
				continue;
			}

			if(!propNameSet.contains(field.getName())){
				continue;
			}

			String columnName = this.getColumnName(modelClass, field);
			Object value = ReflectionUtils.getFieldValue(model, field);

			Map<String, Object> pair = new HashMap<>();
			pair.put("columnName", columnName);
			pair.put("columnValue", value);

			pairs.add(pair);
		}

		Map<String, Object> params = new HashMap<>();
		params.put("id", model.getId());
		params.put("tableName", this.getTableName(modelClass));
		params.put("pairs", pairs);

		model.setUpdateTime(new Date());

		return this.commonDao.update(SqlKey.common_update, params);
	}

	/**
	 * 逻辑删除实体
	 * @param modelClass 实体类
	 * @param model 要删除的实体
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModel(Class<T> modelClass, T model){
		if(modelClass == null || model==null ||BaseUtils.isNull0(model.getId())){
			return 0;
		}

		Map<String, Object> param = new HashMap<>();
		param.put("id", model.getId());
		param.put("tableName", this.getTableName(modelClass));
		
		model.setDeleteStatus(true);
		model.setUpdateTime(new Date());
		
		return this.commonDao.update(SqlKey.common_delete, param);
	}
	/**
	 * 根据ID逻辑删除实体
	 * @param modelClass 实体类
	 * @param modelId 要删除的实体ID
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModelById(Class<T> modelClass, Long modelId){
		if(modelClass == null || BaseUtils.isNull0(modelId)){
			return 0;
		}

		Map<String, Object> param = new HashMap<>();
		param.put("id", modelId);
		param.put("tableName", this.getTableName(modelClass));
		
		return this.commonDao.update(SqlKey.common_delete, param);
	}
	

	/**
	 * 根据ID逻辑删除实体
	 * @param modelClass 实体类
	 * @param modelIds 要删除的实体ID列表
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModelByIds(Class<T> modelClass, List<Long> modelIds){
		if(modelClass == null || modelIds==null || modelIds.isEmpty()){
			return 0;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("ids", StringUtils.join(modelIds, ","));
		params.put("tableName", this.getTableName(modelClass));
		
		return this.commonDao.update(SqlKey.common_delete, params);
	}
	
	/**
	 * 物理删除实体
	 * @param modelClass 实体类
	 * @param model 要删除的实体
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModelPhysically(Class<T> modelClass, T model){
		if(modelClass == null || model==null ||BaseUtils.isNull0(model.getId())){
			return 0;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("id", model.getId());
		params.put("tableName", this.getTableName(modelClass));
		
		return this.commonDao.delete(SqlKey.common_deletePhysically, params);
	}

	/**
	 * 根据ID物理删除实体
	 * @param modelClass 实体类
	 * @param modelId 要删除的实体ID
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModelPhysicallyById(Class<T> modelClass, Long modelId){
		if(modelClass == null || BaseUtils.isNull0(modelId)){
			return 0;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("id", modelId);
		params.put("tableName", this.getTableName(modelClass));
		
		return this.commonDao.delete(SqlKey.common_deletePhysically, params);
	}

	/**
	 * 根据ID物理删除实体
	 * @param modelClass 实体类
	 * @param modelIds 要删除的实体ID列表
	 * @return 删除成功的记录数
	 */
    @Override
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public <T extends Model> int deleteModelPhysicallyByIds(Class<T> modelClass, List<Long> modelIds){
		if(modelClass == null || modelIds==null || modelIds.isEmpty()){
			return 0;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("ids", StringUtils.join(modelIds, ","));
		params.put("tableName", this.getTableName(modelClass));
		
		return this.commonDao.delete(SqlKey.common_deletePhysically, params);
	}
	
	//---------------相关辅助方法-----------------
	
	/**
     * 获取实体对应的数据库表名称
     * @param modelClass
     * @return
     */
    private <T extends Model> String getTableName(Class<T> modelClass) {
        String classname = modelClass.getSimpleName();

        Table table = modelClass.getAnnotation(Table.class);
        if (table != null && table.value() != null && !"".equals(table.value())) {
        	classname = table.value();
        }
        
        return getSafeColName(classname);
    }
    /**
     * 获取一个实体属性对应的数据库表字段名称
     * @param modelClass
     * @param fieldName 实体属性名称
     * @return 
     */
    private <T extends Model> String getColumnName(Class<T> modelClass, String fieldName) {         
    	//Field field = modelClass.getDeclaredField(fieldName);
    	Field field = ReflectionUtils.findField(modelClass, fieldName);    	 
    	String columnName = this.getColumnName(modelClass, field);
    	    	
        return getSafeColName(BaseUtils.getFirstNotBlank(columnName, fieldName));
    }
    /**
     * 获取一个实体属性对应的数据库表字段名称
     * @param modelClass
     * @param field 实体属性
     * @return 
     */
    private <T extends Model> String getColumnName(Class<T> modelClass, Field field) {
    	if(field == null){
    		return "";
    	}
    	String columnName = field.getName();   
          
        if(field.isAnnotationPresent(Column.class)){
         	Column column = field.getAnnotation(Column.class);
         	if (column != null && column.value() != null && !"".equals(column.value())) {
         		columnName = column.value();
             }
 		}
        return getSafeColName(columnName);
    }
    /**
     * 获取实体对应数据库表字段名称列表，主要用于Select的返回字段
     * @param modelClass
     * @return columnName的逗号拼串
     */
    private <T extends Model> String getSelectColumnNames(Class<T> modelClass) {
    	return this.getSelectColumnNames(modelClass, null);
    }
    /**
     * 获取实体属性对应数据库表字段名称列表，主要用于Select的返回字段
     * @param modelClass
     * @param excludes 要排除的实体属性列表
     * @return columnName的逗号拼串
     */
    private <T extends Model> String getSelectColumnNames(Class<T> modelClass, List<String> excludes) {    	
    	StringBuilder sb = new StringBuilder();
        List<Field> fields = ReflectionUtils.getDeclaredFields(modelClass);
        for (Field field : fields) {    
            if(!this.checkField(field, excludes)){
            	continue;
            }
            
            String filedName = field.getName(); 
            String columnName = this.getColumnName(modelClass, field);
            
            sb.append(",").append(columnName).append(" AS ").append(filedName);            
        }  
        return sb.substring(1);
    }
    /**
	 * 检查一个field是否需要保留
	 * @param field
	 * @param excludes
	 * @return true:保留, false:丢弃
	 */
	private boolean checkField(Field field, List<String> excludes){
		String fieldName = field.getName();   
		
		if(excludes!=null && !excludes.isEmpty() && excludes.contains(fieldName) ){
			return false; 
        }
		
        //@ToMap(ignore=true), 忽略
        if(ToMapUtils.ignore(field)){
			return false; 
		}
        //@AttrIgnore
        if(ReflectionUtils.hasAnnotation(field, AttrIgnore.class)){
        	return false; 
		}
        
        //继承自Serializable接口的字段
        if("serialVersionUID".equals(fieldName)){
        	return false; 
        }
        //Hibernate懒加载的实体对象会增加几个字段,过滤之
        if(BaseUtils.isInList("default_interceptor,_method_filter,_methods_", fieldName)){
        	return false; 	
        }
        if("handler".equals(fieldName)){            	
        	String fieldClassName = field.getType().getName();
        	//System.out.println(fieldClassName);
        	//org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer
        	//javassist.util.proxy.MethodHandler
        	if("javassist.util.proxy.MethodHandler".equals(fieldClassName)){
        		return false;
        	}
        }
        return true;
	}
	
    
    /**
     * 根据Map生成Model
     * @param modelClass
     * @param map
     * @return
     */
    private <T extends Model> T mapToModel(Class<T> modelClass, Map<String, Object> map){
    	if(modelClass == null || map == null){
    		return null;
    	}
    	T model = ReflectionUtils.newInstance(modelClass);
    	for(Map.Entry<String, Object> entry : map.entrySet()){
    		try{
    			ReflectionUtils.setFieldValue(model, entry.getKey(), entry.getValue());
    		} catch(Exception e){
    			continue;
    		}
    	}
    	return model;
    }
    /**
     * 根据Map生成Model
     * @param modelClass
     * @param contentTypeExtMap
     * @return
     */
    private <T extends Model> List<T> mapToModel(Class<T> modelClass, List<Map<String, Object>> list){
        List<T> result = new LinkedList<>();
        if(modelClass == null || list==null || list.isEmpty()){
        	return result;
        }
    	for(Map<String, Object> map : list){
        	T model = mapToModel(modelClass, map);
        	result.add(model);
        }
    	return result;
    }

    
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
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T> List<T> find(SqlKey key, Object params){
		return commonDao.find(key, params);
	}
	
	/**
	 * 查询一条记录
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T> T findOne(SqlKey key, Object params){
		return commonDao.findOne(key, params);
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
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T> Pager<T> query(SqlKey countKey, SqlKey queryKey, int pageNo, int pageSize, Object params){
		return commonDao.query(countKey, queryKey, pageNo, pageSize, params);
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
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public <T> Pager<T> query(SqlKey key, int pageNo, int pageSize, Object params){
		return commonDao.query(key, pageNo, pageSize, params);
	}

	//-------------统计---------------------	
	/**
	 * 查询记录条数
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.SUPPORTS, readOnly=true)
	public long count(SqlKey key, Object params){
		return commonDao.count(key, params);
	}
	
	
	//-------------新增---------------------		
	/**
	 * 新增记录
	 * @param key		SQL语句ID(Mapper中的ID)
	 * @param params 	参数
	 * @return 新增成功的数量
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int create(SqlKey key, Object params){
		return commonDao.create(key, params);
	}
	

	//-------------更新---------------------		
	/**
	 * 更新记录
	 * @param key 		SQL语句ID(Mapper中的ID)
	 * @param params 	要更新的实体
	 * @return 更新成功的数量
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int update(SqlKey key, Object params){
		return commonDao.update(key, params);
	}
	
	
	//-------------删除---------------------	
	/**
	 * 根据参数删除对象
	 * @param key
	 * @param params
	 * @return  删除的对象数量
	 */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int delete(SqlKey key, Object params){
		return commonDao.delete(key, params);
	}
	
	/**
     * 删除指定的唯一标识符对应的持久化对象
     *
     * @param id 指定的唯一标识符
	 * @return 删除的对象数量
     */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int deleteById(SqlKey key, Long id){
		return commonDao.deleteById(key, id);
	}

    /**
     * 删除指定的唯一标识符列表对应的持久化对象
     *
     * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
     */
	@Transactional(value=Const.MYBATIS_TRANSACTION_MANAGER, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int deleteByIds(SqlKey key,  List<Long> ids){
		return commonDao.deleteByIds(key, ids);
	}

	//-------------存储过程---------------------	
	/**
	 * 执行一条MySQL存储过程
	 * @param key SQL语句ID(Mapper中的ID)
	 * @param params 参数
	 * @return
	 */
	public <T> T runProc(SqlKey key, Object params){
		return commonDao.findOne(key, params);
	}
	/**
	 * 获取安全的数据库表名，字段名，避免名称命中数据库保留字
	 * @param columnName
	 * @return
	 */
	private String getSafeColName(String columnName){
		return "`"+columnName.replaceAll("`","")+"`";
	}
}

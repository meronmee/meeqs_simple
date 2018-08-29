package com.meronmee.core.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.meronmee.core.annotation.AttrIgnore;
import com.meronmee.core.annotation.ToMapUtils;
import com.meronmee.core.utils.BaseUtils;
import com.meronmee.core.utils.DateUtils;
import com.meronmee.core.utils.ReflectionUtils;

/**
 *
 * 模型对象的基类
 *
 * @author Meron
 */
public abstract class Model implements Serializable{
    private static final long serialVersionUID = 1L;

    protected Long id;
    
    protected Date createTime;
    
    protected Date updateTime;    
    
    protected Boolean deleteStatus = false;
    
    
    public Model(){
    	this.createTime = new Date();    	
    	this.updateTime = new Date();
    	this.deleteStatus = false;
    }
    
    

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setId(BigInteger id) {
		this.id = BaseUtils.toLong(id);
	}
	public void setId(Integer id) {
		this.id = BaseUtils.toLong(id);
	}
	
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean getDeleteStatus() {
		if(deleteStatus==null){
			deleteStatus = false;
		}
		return deleteStatus;
	}
	public void setDeleteStatus(Boolean deleteStatus) {
		if(deleteStatus==null){
			deleteStatus = false;
		}
		this.deleteStatus = deleteStatus;
	}



	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Model)) {
            return false;
        }
        Model model = (Model) obj;
        
        return model.getId()!=null && this.getId()!=null && model.getId().longValue() == this.getId().longValue();
    }

    @Override
    public int hashCode() {
        if (id == null) {
            id = -1L;
        }
        return Long.valueOf(id + 1000L).hashCode();
    }
    

    /**
     * <pre>
	 * 对象转换为Map，以便JSON化
	 * 
	 * 忽略字段:
	 *	-@AttributeIgnore
	 *	-@RenderIgnore
	 *	-@ToMap(ignore=true)
	 *
	 * 处理字段:
	 *	-value==null 
	 *			--> ""
	 *	-Timestamp|Date
	 *			-@ToMap(date=true)|Date
	 *					--> yyyy-MM-dd
	 *		  	-@ToMap(time=true)|Timestamp
	 *			 		--> yyyy-MM-dd HH:mm:ss
	 *		  	-@ToMap(longtime=true)
	 *			 		--> 毫秒值
	 *	</pre>	
	 */   
    public Map<String, Object> toMap(){ 
    	return this.toMap(null);
    }
    
    /**
     * <pre>
	 * 对象转换为Map，以便JSON化
	 * 
	 * 忽略字段:
	 *	-@AttributeIgnore
	 *	-@RenderIgnore
	 *	-@ToMap(ignore=true)
	 *
	 * 处理字段:
	 *	-value==null 
	 *			--> ""
	 *	-Timestamp|Date
	 *			-@ToMap(date=true)|Date
	 *					--> yyyy-MM-dd
	 *		  	-@ToMap(time=true)|Timestamp
	 *			 		--> yyyy-MM-dd HH:mm:ss
	 *		  	-@ToMap(longtime=true)
	 *			 		--> 毫秒值
	 *	</pre>	
	 * @param excludes 要排除的字段列表，没有请传null
	 * @return
	 */   
    public Map<String, Object> toMap(List<String> excludes){   
    	Map<String, Object> map = new HashMap<>();
    	
        List<Field> fields = ReflectionUtils.getDeclaredFields(this);//获取所有字段，包括继承的
        for (Field field : fields) {        	
        	String fieldName = field.getName();   
        	//过滤字段
        	//------------------------------------
        	//排除指定字段
    		if(excludes!=null && !excludes.isEmpty() && excludes.contains(fieldName) ){
    			continue; 
            }
    		
            //@ToMap(ignore=true), 忽略
            if(ToMapUtils.ignore(field)){
            	continue; 
    		}
            //@AttrIgnore
            if(ReflectionUtils.hasAnnotation(field, AttrIgnore.class)){
            	continue; 
    		}
            
            //继承自Serializable接口的字段
            if("serialVersionUID".equals(fieldName)){
            	continue; 
            }
            //Hibernate懒加载的实体对象会增加几个字段,过滤之
            if(BaseUtils.isInList("default_interceptor,_method_filter,_methods_", fieldName)){
            	continue; 	
            }
            
            if("handler".equals(fieldName)){            	
            	String fieldClassName = field.getType().getName();
            	//System.out.println(fieldClassName);
            	//org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer
            	//javassist.util.proxy.MethodHandler
            	if("javassist.util.proxy.MethodHandler".equals(fieldClassName)){
            		continue;
            	}
            }
            
        	//处理字段值
        	//------------------------------------
            Object value = ReflectionUtils.getFieldValue(this, field);//调用Getter方法获取字段的值               
            if(value == null){
                map.put(fieldName, null);
                continue;
            }
            if(value.getClass() == null){
            	map.put(fieldName, value);
                continue;
            }
            String valueClass = value.getClass().getSimpleName();   
                          
            if("Timestamp".equals(valueClass) || "Date".equals(valueClass)){//处理日期类型
            	if(ToMapUtils.longtime(field)){//@ToMap(longtime=true),日期时间转为毫秒值
            		value = ((Date)value).getTime();
            	}else if(ToMapUtils.date(field)){//@ToMap(date=true),日期时间转为yyyy-MM-dd
                    value = DateUtils.dateToString((Date)value, "yyyy-MM-dd");
                }else if(ToMapUtils.time(field)){//@ToMap(time=true),日期时间转为yyyy-MM-dd HH:mm:ss
                    value = DateUtils.dateToString((Date)value, "yyyy-MM-dd HH:mm:ss");
                }else{
                	value = ((Date)value).getTime();
                }
            	map.put(fieldName, value);
            } else if("String,Boolean,Integer,Float,Short,Long,Double".indexOf(valueClass) != -1){//处理常见类型
            	map.put(fieldName, value);
            } else if("BigInteger".equals(valueClass)){//将java.math.BigInteger类型转换简单类型                
				map.put(fieldName, BaseUtils.toLong(value));	
			} else if("BigDecimal".equals(valueClass)){//将java.math.BigDecimal类型转换简单类型
				map.put(fieldName, BaseUtils.toDouble(value));	
			} 
            //其他复杂类型忽略
        }//for 
        return map;
    }


    /**
     * 从Map中反序列化model信息
     * @param model model对象的新实例
     * @param map
     * @return
     */
    public static <T extends Model> T fromMap(Class<T> clazz, Map<String,Object> map){
        if(map == null || map.isEmpty()){
            System.out.println("反序列化MyModel失败，数据有误");
            return null;
        }
        T model = ReflectionUtils.newInstance(clazz);

        List<Field> fields = ReflectionUtils.getDeclaredFields(clazz);//获取所有字段，包括继承的
        for (Field field : fields) {
            String fieldName = field.getName();
            //过滤字段
            //------------------------------------
            //@ToMap(ignore=true), 忽略
            if(ToMapUtils.ignore(field)){
                continue;
            }
            //@AttrIgnore，忽略
            if(ReflectionUtils.hasAnnotation(field, AttrIgnore.class)){
                continue;
            }

            //继承自Serializable接口的字段
            if("serialVersionUID".equals(fieldName)){
                continue;
            }
            //Hibernate懒加载的实体对象会增加几个字段,过滤之
            if(BaseUtils.isInList("default_interceptor,_method_filter,_methods_", fieldName)){
                continue;
            }

            if("handler".equals(fieldName)){
                String fieldClassName = field.getType().getName();
                //System.out.println(fieldClassName);
                //org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer
                //javassist.util.proxy.MethodHandler
                if("javassist.util.proxy.MethodHandler".equals(fieldClassName)){
                    continue;
                }
            }

            //处理字段值
            //------------------------------------
            Object value = map.get(fieldName);
            if(value == null){
                continue;
            }
            String fieldClass = field.getType().getSimpleName();
            if(StringUtils.isBlank(fieldClass)){
                continue;
            }
            if("Timestamp".equals(fieldClass) || "Date".equals(fieldClass)){//处理日期类型
                ReflectionUtils.setFieldValue(model, field, (Date)value);
            } else if("String".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toString(value));
            } else if("Boolean".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toBoolean(value));
            } else if("Integer".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toInteger(value));
            } else if("Float".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toFloat(value));
            } else if("Long".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toLong(value));
            } else if("Double".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toDouble(value));
            } else if("Short".equals(fieldClass)){
                ReflectionUtils.setFieldValue(model, field, BaseUtils.toShort(value));
            } else if("BigInteger".equals(fieldClass)){//将java.math.BigInteger类型转换简单类型
                ReflectionUtils.setFieldValue(model, field, new BigInteger(BaseUtils.toString(value)));
            } else if("BigDecimal".equals(fieldClass)){//将java.math.BigDecimal类型转换简单类型
                ReflectionUtils.setFieldValue(model, field, new BigDecimal(BaseUtils.toString(value)));
            }
            //其他复杂类型忽略
        }//for

        return model;
    }
}
package com.meronmee.core.common.util;
 
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.meronmee.core.api.annotation.AttrIgnore;
import com.meronmee.core.api.domain.Model;
import com.meronmee.core.api.util.ToMapUtils;

/**
 * Model工具类
 *
 * @author Meron
 * @create 2018-08-14 15:35
 */
public class ModelUtils {



    /**
     * <pre>
     * Model对象转换为Map
     *
     * 忽略字段:
     *	-@AttrIgnore
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
    public static Map<String, Object> toMap(Model model){
        return toMap(model, null);
    }

    /**
     * <pre>
     * Model对象转换为Map
     *
     * 忽略字段:
     *	-@AttrIgnore
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
    public static Map<String, Object> toMap(Model model, List<String> excludes){
        Map<String, Object> map = new HashMap<>();
        if(model == null){
            return map;
        }

        List<Field> fields = ReflectionUtils.getDeclaredFields(model);//获取所有字段，包括继承的
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
            Object value = ReflectionUtils.getFieldValue(model, field);//调用Getter方法获取字段的值
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

            //@AttrIgnore 忽略
            if(ReflectionUtils.hasAnnotation(field, AttrIgnore.class) ){
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

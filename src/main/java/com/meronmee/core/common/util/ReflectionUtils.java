package com.meronmee.core.common.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

/**
 * 反射工具类
 * @author meron
 */
public class ReflectionUtils{
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);
    
    private ReflectionUtils(){};

    /**
     * 搜索给定的所有的类里，某个类的所有子类或实现类
     */
    public static List<Class<?>> getAssignedClass(Class<?> cls, List<Class<?>> clses) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : clses) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 获取某个类型的同一路径下的所有类
     */
    public static List<Class<?>> getClasses(Class<?> cls) throws ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk, null);
    }

    /**
     * 获取某个路径下的指定的Package下的所有类，不包括<code>outsides</code>中的
     */
    public static List<Class<?>> getClasses(File dir, String pk, String[] outsides) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        String thisPk = StringUtils.isBlank(pk) ? "" : pk + ".";
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, thisPk + f.getName(), outsides));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                Class<?> clazz = null;
                String clazzName = thisPk + name.substring(0, name.length() - 6);
                if (outsides == null || outsides.length == 0 || !ArrayUtils.contains(outsides, clazzName)) {
                    try {
                        clazz = Class.forName(clazzName);
                    } catch (Throwable e) {
                        LOG.error("实例化失败",e);
                    }
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }

    /**
     *
     * @param <T>
     * @param instance
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneInstance(T instance) {
        Class<T> cls = (Class<T>) instance.getClass();
        T newIns = (T) BeanUtils.instantiateClass(cls);
        BeanUtils.copyProperties(instance, newIns);
        return newIns;
    }
    
    /**
    *
    * @param <T>
    * @param instance
    * @return
    */
   @SuppressWarnings("unchecked")
   public static <T> T newInstance(Class<T> clazz) {
       T newIns = (T) BeanUtils.instantiateClass(clazz);
       return newIns;
   }
    

    /**
     * 类似Class.getSimpleName()，但是可以忽略它是一个javassist生成的动态类
     *
     * @see Class#getSimpleName()
     */
    public static String getSimpleSurname(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return StringUtils.substringBefore(clazz.getSimpleName(), "_$$_");
    }

    /**
     * 类似Class.getName()，但是可以忽略它是一个javassist生成的动态类
     *
     * @see Class#getName()
     */
    public static String getSurname(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return StringUtils.substringBefore(clazz.getName(), "_$$_");
    }
    /**
     * 获取字段的类型
     * @return 出错返回null， 否则返回类型名称，如"java.lang.String"
     */
    public static String getFieldType(final Class<?> clazz, final String fieldName) {
    	try {
			Field field = clazz.getDeclaredField(fieldName);
			String type = field.getType().getName();		
			return type;
		} catch (Exception e) {}
    	return null;
    }
    /**
     * 获取字段的类型
     * @return 出错返回null， 否则返回类型名称，如"java.lang.String"
     */
    public static String getFieldType(final Field field) {
    	try {
			String type = field.getType().getName();		
			return type;
		} catch (Exception e) {}
    	return null;
    }
    /**
     * 优先使用Getter方法读取字段值，如果失败再直接读取(无视private/protected修饰符)
     */
    public static Object getFieldValue(final Object object, final Field field) {
       if(field == null){
    	   return null;
       }
       String fieldName = field.getName();
       return getFieldValue(object, fieldName);
    }

    /**
     * 优先使用Getter方法读取字段值，如果失败再直接读取(无视private/protected修饰符)
     */
    public static Object getFieldValue(final Object object, final String fieldName) {    	   
    	try{
        	Object value = null;
	    	String cFieldName = StringUtils.capitalize(fieldName);//首字母大写
	        String methodName = "get"+cFieldName;
	        Method getter =  ReflectionUtils.getDeclaredMethod(object, methodName);
	        if(getter == null){//boolean型
	        	methodName = "is"+cFieldName;
	        	getter = ReflectionUtils.getDeclaredMethod(object, methodName);
	        }
	        if(getter == null){//没有Getter方法
	        	value = null;
	        } else {//调用Getter方法
	        	try{
	            	value = getter.invoke(object);
	            } catch(Exception e){
	            	e.printStackTrace();
	            	value = null;
	            } 
	        }
	
	    	//Getter方法获取字段的值失败，再尝试直接读取
	        if(value == null){
	        	value = ReflectionUtils.getFieldValueDirectly(object, fieldName);
	        }
	    	return value;
    	} catch(Exception e){
    		return null;
    	}
    }
    
    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValueDirectly(final Object object, final Field field) {
        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LOG.error("不可能抛出的异常", e);
        }
        return result;
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValueDirectly(final Object object, final String fieldName) {
        try{
            Field field = getDeclaredField(object, fieldName);

            if (field == null) {
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            return getFieldValueDirectly(object,field);
        }catch(Exception e){
            String methodName="get"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
            try {
                Method method=object.getClass().getMethod(methodName);
                return method.invoke(object);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.error("Could not exec method [" + methodName + "] on target [" + object + "]", ex);
            }
        }
        return null;
    }

    /**
     * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            LOG.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            LOG.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOG.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static <T> void setFieldValueDirectly(final T object, final Field field, final Object value) {
    	if (field == null) {
    		return;
    	}
    	
        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOG.error("不可能抛出的异常:{}", e);
        }
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static <T> void setFieldValueDirectly(final T object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
        setFieldValueDirectly(object,field,value);       
    }
    
    /**
     * 设置对象属性值
     * 先尝试使用经过setter函数，失败的话再直接设置对象属性值,无视private/protected修饰符.
     */
    public static <T> void setFieldValue(final T object, final String fieldName, final Object value) {
    	 String methodName="set"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
         try {
             Method method=object.getClass().getMethod(methodName,value.getClass());
             method.invoke(object,value);
         } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        	 try{
                 Field field = getDeclaredField(object, fieldName);
                 setFieldValueDirectly(object,field,value);
             }catch(Exception e){  
            	 LOG.error("Could not exec method [" + methodName + "] on target [" + object + "]",ex);                
             }
         }        
        
    }
    /**
     * 设置对象属性值
     * 先尝试使用经过setter函数，失败的话再直接设置对象属性值,无视private/protected修饰符.
     */
    public static <T> void setFieldValue(final T object, final Field field, final Object value) {
    	 if(field == null) {
    		return;
    	 }
    	 String fieldName = field.getName();
    	 setFieldValue(object, fieldName, value);
    }

    /**
     * 循环向上转型,获取对象的DeclaredMethod.
     */
    public static Method getDeclaredMethod(final Object object, final String methodName) {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(methodName, "methodName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                //e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.notNull(object, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
                //e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static List<Field> getDeclaredFields(final Object object) {
        Assert.notNull(object, "object不能为空");
        List<Field> fields=new ArrayList<>();
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] f=superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(f));
        }
        return fields;
    }


    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    public static List<Field> getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "clazz不能为空");
        List<Field> fields=new ArrayList<>();
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] f=superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(f));
        }
        return fields;
    }

 	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * @param field the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
				Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	/**
	 * 查看字段是否具有指定注解
	 * @param field
	 * @param annotationClass
	 * @return
	 */
	public static boolean hasAnnotation(Field field, Class<? extends Annotation> annotationClass){
		return field.isAnnotationPresent(annotationClass);
	}
	/**
	 * 查看类是否具有指定注解
	 * @param field
	 * @param annotationClass
	 * @return
	 */
	public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass){
		return clazz.isAnnotationPresent(annotationClass);
	}
    
    //----------------------------------
    /**
	 * 对象拷贝
	 * @param from  JavaBean或Map
	 * @param to	JavaBean
	 * @param fromProperties
	 */
	public static void copy(Object from,  Object to, String fromProperties){
		copy(from, to, fromProperties, fromProperties);
	}
	/**
	 * 对象拷贝
	 * @param from 	JavaBean或Map
	 * @param to	JavaBean
	 * @param fromProperties 来源对象的属性
	 * @param toProperties
	 */
	public static void copy(Object from,  Object to, String fromProperties, String toProperties){
		if(from == null){
			return;
		}
		if(to == null){
			return;
		}
		if(StringUtils.isBlank(fromProperties)){
			return;
		}
		if(StringUtils.isBlank(toProperties)){
			toProperties = fromProperties;
		}
		String [] fromProps = fromProperties.split(",");
		String [] toProps = toProperties.split(",");
		int len = Math.min(fromProps.length, toProps.length);
		for(int i=0; i<len; i++){
			String fromProp = fromProps[i];
			String toProp = toProps[i];
			if(StringUtils.isBlank(fromProp) || StringUtils.isBlank(toProp)){
				continue;
			}
			fromProp = fromProp.trim();
			toProp = toProp.trim();
			
			try{
				Object value = 2;
				if(from instanceof Map){
					value = ((Map)from).get(fromProp);
				} else {
					value = getFieldValue(from, fromProp);
				}
				setFieldValue(to, toProp, value);
			}catch(Exception e){
				continue;
			}
		}
	}	
	/**
	 * Map拷贝
	 * @param from
	 * @param to
	 * @param fromKeys 源Map中要拷贝的键,逗号分割
	 */
	public static Map<String, Object> copyMap(Map<String, Object> from,  Map<String, Object> to, String fromKeys){
		return copyMap(from, to, fromKeys, fromKeys);
	}
	/**
	 * Map拷贝
	 * @param from
	 * @param to
	 * @param fromKeys 	源Map中要拷贝的键,逗号分割
	 * @param toKeys	目标Map中要设置的键,逗号分割
	 */
	public static Map<String, Object> copyMap(Map<String, Object> from,  Map<String, Object> to, String fromKeys, String toKeys){
		if(to == null){
			to = new HashMap<String, Object>();
		}
		if(from == null){
			return to;
		}
		if(StringUtils.isBlank(fromKeys)){
			return to;
		}
		if(StringUtils.isBlank(toKeys)){
			toKeys = fromKeys;
		}
		String [] fromKeyArr = fromKeys.split(",");
		String [] toKeyArr = toKeys.split(",");
		int len = Math.min(fromKeyArr.length, toKeyArr.length);
		for(int i=0; i<len; i++){
			String fromKey = fromKeyArr[i];
			String toKey = toKeyArr[i];
			if(StringUtils.isBlank(fromKey) || StringUtils.isBlank(toKey)){
				continue;
			}
			fromKey = fromKey.trim();
			toKey = toKey.trim();
			to.put(toKey, from.get(fromKey));
		}
		return to;
	}	
	
	/**
     * 优先调用getter方法获取属性值
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getValue(final Object object, final String fieldName) {
    	Object value = null;
    	//String cFieldName="get"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
    	String cFieldName = StringUtils.capitalize(fieldName);//首字母大写
        String methodName = "get"+cFieldName;
        //Method getter = object.getClass().getMethod(methodName);
        Method getter = ReflectionUtils.getDeclaredMethod(object, methodName);
        if(getter == null){//boolean型
        	methodName = "is"+cFieldName;
        	getter = ReflectionUtils.getDeclaredMethod(object, methodName);
        }
        if(getter == null){//没有Getter方法
        	value = null;
        } else {//调用Getter方法
        	try{
            	value = getter.invoke(object);
            } catch(Exception e){
            	e.printStackTrace();
            	value = null;
            } 
        }

    	//Getter方法获取字段的值失败，再尝试直接读取
        if(value == null){
        	value = ReflectionUtils.getFieldValue(object, fieldName);
        }
    	return value;
    }
    
    /**
     * 优先调用setter方法设置属性值
     * @param object
     * @param fieldName
     * @param value
     * @return
     */
    public static void setValue(final Object object, final String fieldName, final Object value) {
    	String cFieldName = StringUtils.capitalize(fieldName);//首字母大写
        String methodName = "set"+cFieldName;
      
        //Method setter = ReflectionUtils.getDeclaredMethod(object, methodName);
        Method setter = null;
        try{        	
        	setter = object.getClass().getMethod(methodName, value.getClass());
        }catch(NoSuchMethodException e){
        	try{
	        	if(value != null && "Boolean".equals(value.getClass().getSimpleName())){
	    			setter = object.getClass().getMethod(methodName, boolean.class);
	    		} else if(value != null && "Integer".equals(value.getClass().getSimpleName())){
	    			setter = object.getClass().getMethod(methodName, int.class);
	    		}
        	} catch (Exception e1){
        		setter = null;
        	}
        }
        catch(Exception e){
        	setter = null;
        }
        if(setter != null){//调用setter方法
        	try{
        		setter.invoke(object, value);
        		/*
        		if(value != null && "Boolean".equals(value.getClass().getSimpleName())){
        			boolean bvalue = ((Boolean)value).booleanValue();
        			setter.invoke(object, bvalue);
        		} else {
        			setter.invoke(object, value);
        		}
        		*/
            } 
        	catch(Exception e){
            	e.printStackTrace();
            	ReflectionUtils.setFieldValue(object, fieldName, value);
            } 
        } else {
        	/*
        	if(value != null && "Boolean".equals(value.getClass().getSimpleName())){
    			boolean bvalue = ((Boolean)value).booleanValue();
            	ReflectionUtils.setFieldValue(object, fieldName, bvalue);
    		} else {
            	ReflectionUtils.setFieldValue(object, fieldName, value);
    		}
    		*/
        	ReflectionUtils.setFieldValue(object, fieldName, value);
        }    	
    }
    /**
     * 从枚举值的名称，反向解析枚举值
     * @param enumType - 枚举类型
     * @param name - 枚举值名称
     * @return 没有相关的值，则返回null，否则返回具体的枚举值
     */
    public static <T extends Enum<T>> T getEnumFromName(Class<T> enumType, String name) {
    	return getEnumFromName(enumType, name, null);
    }
    /**
     * 从枚举值的名称，反向解析枚举值
     * @param enumType - 枚举类型
     * @param name - 枚举值名称
     * @param defaultEnum - 默认的枚举值
     * @return 没有相关的值，则返回defaultEnum，否则返回具体的枚举值
     */
    public static <T extends Enum<T>> T getEnumFromName(Class<T> enumType, String name, T defaultEnum) {
		if(enumType == null || name == null){
			return defaultEnum;
		}
		try{
			return Enum.valueOf(enumType, name);
		}catch(Exception e){
			return defaultEnum;
		}
	}
    
    
    //--------------------------------
    //来自org.springframework.util.ReflectionUtils
	/**
	 * Naming prefix for CGLIB-renamed methods.
	 * @see #isCglibRenamedMethod
	 */
	private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";

	/**
	 * Pattern for detecting CGLIB-renamed methods.
	 * @see #isCglibRenamedMethod
	 */
	private static final Pattern CGLIB_RENAMED_METHOD_PATTERN = Pattern.compile("(.+)\\$\\d+");

	/**
	 * Cache for {@link Class#getDeclaredMethods()}, allowing for fast resolution.
	 */
	private static final Map<Class<?>, Method[]> declaredMethodsCache =
			new ConcurrentReferenceHashMap<Class<?>, Method[]>(256);


	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name}. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name} and/or {@link Class type}. Searches all superclasses
	 * up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field (may be {@code null} if type is specified)
	 * @param type the type of the field (may be {@code null} if name is specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Set the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object} to the specified {@code value}.
	 * In accordance with {@link Field#set(Object, Object)} semantics, the new value
	 * is automatically unwrapped if the underlying field has a primitive type.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
	 * @param field the field to set
	 * @param target the target object on which to set the field
	 * @param value the value to set; may be {@code null}
	 */
	public static void setField(Field field, Object target, Object value) {
		try {
			field.set(target, value);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
	 * semantics, the returned value is automatically wrapped if the underlying field
	 * has a primitive type.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
	 * @param field the field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getField(Field field, Object target) {
		try {
			return field.get(target);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and no parameters. Searches all superclasses up to {@code Object}.
	 * <p>Returns {@code null} if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, new Class<?>[0]);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and parameter types. Searches all superclasses up to {@code Object}.
	 * <p>Returns {@code null} if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @param paramTypes the parameter types of the method
	 * (may be {@code null} to indicate any signature)
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) &&
						(paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object with no arguments.
	 * The target object can be {@code null} when invoking a static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, new Object[0]);
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object with the
	 * supplied arguments. The target object can be {@code null} when invoking a
	 * static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be {@code null})
	 * @return the invocation result, if any
	 */
	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		}
		catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Invoke the specified JDBC API {@link Method} against the supplied target
	 * object with no arguments.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @throws SQLException the JDBC API SQLException to rethrow (if any)
	 * @see #invokeJdbcMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
		return invokeJdbcMethod(method, target, new Object[0]);
	}

	/**
	 * Invoke the specified JDBC API {@link Method} against the supplied target
	 * object with the supplied arguments.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be {@code null})
	 * @return the invocation result, if any
	 * @throws SQLException the JDBC API SQLException to rethrow (if any)
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
		try {
			return method.invoke(target, args);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
		}
		catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof SQLException) {
				throw (SQLException) ex.getTargetException();
			}
			handleInvocationTargetException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * @param ex the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 * @param ex the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>Rethrows the underlying exception cast to an {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>Rethrows the underlying exception cast to an {@link Exception} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws Exception the rethrown exception (in case of a checked exception)
	 */
	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception) {
			throw (Exception) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Determine whether the given method explicitly declares the given
	 * exception or one of its superclasses, which means that an exception of
	 * that type can be propagated as-is within a reflective invocation.
	 * @param method the declaring method
	 * @param exceptionType the exception to throw
	 * @return {@code true} if the exception can be thrown as-is;
	 * {@code false} if it needs to be wrapped
	 */
	public static boolean declaresException(Method method, Class<?> exceptionType) {
		Assert.notNull(method, "Method must not be null");
		Class<?>[] declaredExceptions = method.getExceptionTypes();
		for (Class<?> declaredException : declaredExceptions) {
			if (declaredException.isAssignableFrom(exceptionType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether the given field is a "public static final" constant.
	 * @param field the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}

	/**
	 * Determine whether the given method is an "equals" method.
	 * @see java.lang.Object#equals(Object)
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}

	/**
	 * Determine whether the given method is a "hashCode" method.
	 * @see java.lang.Object#hashCode()
	 */
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}

	/**
	 * Determine whether the given method is a "toString" method.
	 * @see java.lang.Object#toString()
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}

	/**
	 * Determine whether the given method is originally declared by {@link java.lang.Object}.
	 */
	public static boolean isObjectMethod(Method method) {
		if (method == null) {
			return false;
		}
		try {
			Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Determine whether the given method is a CGLIB 'renamed' method,
	 * following the pattern "CGLIB$methodName$0".
	 * @param renamedMethod the method to check
	 * @see org.springframework.cglib.proxy.Enhancer#rename
	 */
	public static boolean isCglibRenamedMethod(Method renamedMethod) {
		String name = renamedMethod.getName();
		return (name.startsWith(CGLIB_RENAMED_METHOD_PREFIX) &&
				CGLIB_RENAMED_METHOD_PATTERN.matcher(name.substring(CGLIB_RENAMED_METHOD_PREFIX.length())).matches());
	}


	/**
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary. The {@code setAccessible(true)} method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * @param method the method to make accessible
	 * @see java.lang.reflect.Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) &&
				!method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible
	 * if necessary. The {@code setAccessible(true)} method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * @param ctor the constructor to make accessible
	 * @see java.lang.reflect.Constructor#setAccessible
	 */
	public static void makeAccessible(Constructor<?> ctor) {
		if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) &&
				!ctor.isAccessible()) {
			ctor.setAccessible(true);
		}
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by a {@link MethodFilter}.
	 * @param clazz class to start looking at
	 * @param mc the callback to invoke for each method
	 * @see #doWithMethods(Class, MethodCallback, MethodFilter)
	 */
	public static void doWithMethods(Class<?> clazz, MethodCallback mc) throws IllegalArgumentException {
		doWithMethods(clazz, mc, null);
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses (or given interface and super-interfaces).
	 * <p>The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by the specified {@link MethodFilter}.
	 * @param clazz class to start looking at
	 * @param mc the callback to invoke for each method
	 * @param mf the filter that determines the methods to apply the callback to
	 */
	public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf)
			throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		Method[] methods = getDeclaredMethods(clazz);
		for (Method method : methods) {
			if (mf != null && !mf.matches(method)) {
				continue;
			}
			try {
				mc.doWith(method);
			}
			catch (IllegalAccessException ex) {
				throw new IllegalStateException("Shouldn't be illegal to access method '" + method.getName() + "': " + ex);
			}
		}
		if (clazz.getSuperclass() != null) {
			doWithMethods(clazz.getSuperclass(), mc, mf);
		}
		else if (clazz.isInterface()) {
			for (Class<?> superIfc : clazz.getInterfaces()) {
				doWithMethods(superIfc, mc, mf);
			}
		}
	}

	/**
	 * Get all declared methods on the leaf class and all superclasses. Leaf
	 * class methods are included first.
	 */
	public static Method[] getAllDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
		final List<Method> methods = new ArrayList<Method>(32);
		doWithMethods(leafClass, new MethodCallback() {
			@Override
			public void doWith(Method method) {
				methods.add(method);
			}
		});
		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * Get the unique set of declared methods on the leaf class and all superclasses. Leaf
	 * class methods are included first and while traversing the superclass hierarchy any methods found
	 * with signatures matching a method already included are filtered out.
	 */
	public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
		final List<Method> methods = new ArrayList<Method>(32);
		doWithMethods(leafClass, new MethodCallback() {
			@Override
			public void doWith(Method method) {
				boolean knownSignature = false;
				Method methodBeingOverriddenWithCovariantReturnType = null;
				for (Method existingMethod : methods) {
					if (method.getName().equals(existingMethod.getName()) &&
							Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
						// Is this a covariant return type situation?
						if (existingMethod.getReturnType() != method.getReturnType() &&
								existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
							methodBeingOverriddenWithCovariantReturnType = existingMethod;
						}
						else {
							knownSignature = true;
						}
						break;
					}
				}
				if (methodBeingOverriddenWithCovariantReturnType != null) {
					methods.remove(methodBeingOverriddenWithCovariantReturnType);
				}
				if (!knownSignature && !isCglibRenamedMethod(method)) {
					methods.add(method);
				}
			}
		});
		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * This method retrieves {@link Class#getDeclaredMethods()} from a local cache
	 * in order to avoid the JVM's SecurityManager check and defensive array copying.
	 */
	private static Method[] getDeclaredMethods(Class<?> clazz) {
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredMethods();
			declaredMethodsCache.put(clazz, result);
		}
		return result;
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * @param clazz the target class to analyze
	 * @param fc the callback to invoke for each field
	 */
	public static void doWithFields(Class<?> clazz, FieldCallback fc) throws IllegalArgumentException {
		doWithFields(clazz, fc, null);
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * @param clazz the target class to analyze
	 * @param fc the callback to invoke for each field
	 * @param ff the filter that determines the fields to apply the callback to
	 */
	public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff)
			throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		Class<?> targetClass = clazz;
		do {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(field)) {
					continue;
				}
				try {
					fc.doWith(field);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalStateException("Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
	}
	
	/** 
     * 获得超类的参数类型，取第一个参数类型 
     * @param <T> 类型参数 
     * @param clazz 超类类型 
     */  
    @SuppressWarnings("rawtypes")  
    public static <T> Class<T> getClassGenricType(final Class clazz) {  
        return getClassGenricType(clazz, 0);  
    }  
      
    /** 
     * 根据索引获得超类的参数类型 
     * @param clazz 超类类型 
     * @param index 索引 
     */  
    @SuppressWarnings("rawtypes")  
    public static Class getClassGenricType(final Class clazz, final int index) {  
        Type genType = clazz.getGenericSuperclass();  
        if (!(genType instanceof ParameterizedType)) {  
            return Object.class;  
        }  
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();  
        if (index >= params.length || index < 0) {  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            return Object.class;  
        }  
        return (Class) params[index];  
    }

   
}
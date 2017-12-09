package com.meronmee.core.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * setting.properties 配置文件读取工具
 * @author Meron
 *
 */
public class SettingHolder {
    public static final Logger LOG = LoggerFactory.getLogger(SettingHolder.class);
    
    private static final Map<String, String> PROPERTIES = new HashMap<>();

    static {
        init();
    }
    
    /**
     * 从配置文件加载配置信息到缓存map中
     * @param inputStream
     * @param map
     */
    private static void load(InputStream inputStream, Map<String, String> map){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))){
            String line;
            while((line = reader.readLine()) != null){
                line = line.trim();
                if("".equals(line) || line.startsWith("#")){
                    continue;
                }
                int index = line.indexOf("=");
                if(index==-1){
                    LOG.error("错误的配置："+line);
                    continue;
                }
                if(index>0 && line.length()>index+1) {
                    String key = line.substring(0, index).trim();
                    String value = BaseUtils.decodeUnicode(line.substring(index + 1, line.length()).trim());
                    map.put(key, value);
                }else{
                    LOG.error("错误的配置："+line);
                }
            }
        } catch (IOException ex) {
            LOG.error("配置文件加载失败:" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * 初始化配置信息
     */
    private static void init() {
    		String[] configFiles = {
    			"/config/setting.properties" //src/main/resources/config/setting.properties
    		};
    	
            ClassPathResource cr = null;
            for(String file : configFiles){
                try{
                    cr = new ClassPathResource(file);
                    load(cr.getInputStream(), PROPERTIES);
                    LOG.info("装入配置文件:"+file);
                } catch(Exception e){
                    LOG.info("装入配置文件"+file+"失败!", e);
                }
            }
    }
      
    /**
     * 重新加载配置信息
     */
    public static void reload() {
    	init();
    }
    
    /**
     * 获取配置信息缓存
     * @return
     */
    public static Map<String, String> getProperties() {
        return PROPERTIES;
    }
    
    /**
     * 设置参数
     * @param name
     * @param value
     */
    public static void setProperty(String name, Object value) {
        PROPERTIES.put(name, String.valueOf(value));
    }
    
    //--------------------
    
    /**
     * @param name 配置文件中的参数名
     */
    private static String getProperty(String name) {
    	String config = PROPERTIES.get(name);
        return config;
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    private static String getProperty(String name, String defaultValue) {
        String config = PROPERTIES.get(name);
        if(StringUtils.isBlank(config)){
        	return defaultValue;
        }
        return config;
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static String getString(String name) {
        return getProperty(name);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static String getString(String name, String defaultValue) {
        return getProperty(name, defaultValue);
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static Integer getInteger(String name) {
    	String config = PROPERTIES.get(name);
        return BaseUtils.toInteger(config);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static Integer getInteger(String name, Integer defaultValue) {
    	String config = PROPERTIES.get(name);
        Integer value = BaseUtils.toInteger(config);
        if(value == null){
        	value = defaultValue;
        }
        return value;
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static Long getLong(String name) {
    	String config = PROPERTIES.get(name);
        return BaseUtils.toLong(config);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static Long getLong(String name, Long defaultValue) {
    	String config = PROPERTIES.get(name);
        Long value = BaseUtils.toLong(config);
        if(value == null){
        	value = defaultValue;
        }
        return value;
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static Float getFloat(String name) {
    	String config = PROPERTIES.get(name);
        return BaseUtils.toFloat(config);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static Float getFloat(String name, Float defaultValue) {
    	String config = PROPERTIES.get(name);
    	Float value = BaseUtils.toFloat(config);
        if(value == null){
        	value = defaultValue;
        }
        return value;
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static Double getDouble(String name) {
    	String config = PROPERTIES.get(name);
        return BaseUtils.toDouble(config);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static Double getDouble(String name, Double defaultValue) {
    	String config = PROPERTIES.get(name);
    	Double value = BaseUtils.toDouble(config);
        if(value == null){
        	value = defaultValue;
        }
        return value;
    }
    /**
     * @param name 配置文件中的参数名
     * @return
     */
    public static Boolean getBoolean(String name) {
    	String config = PROPERTIES.get(name);
        return BaseUtils.toBoolean(config);
    }
    /**
     * @param name 配置文件中的参数名
     * @param defaultValue 配置文件中参数缺失时的默认值
     * @return
     */
    public static Boolean getBoolean(String name, Boolean defaultValue) {
    	String config = PROPERTIES.get(name);
    	Boolean value = BaseUtils.toBoolean(config);
        if(value == null){
        	value = defaultValue;
        }
        return value;
    }
}
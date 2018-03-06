package com.meronmee.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 增强型HashMap,支持链式操作
 * @author Meron
 *
 */
@SuppressWarnings("serial")
public class LinkMap extends HashMap<String, Object> {
	
	public LinkMap(){}
	
	public LinkMap(Map<String, Object> map){
		this.putAll(map);
	}
	public LinkMap(String key, Object value){
		this.put(key, value);
	}	
	
	/**
	 * 支持链式操作的append
	 * @param key
	 * @param value
	 * @return
	 */
	public LinkMap append(String key, Object value){
		this.put(key, value);		
		return this;
	}
	public LinkMap append(Map<String, Object> map){
		this.putAll(map);		
		return this;
	}
	
	public String getString(String key){
		return getString(key, null);
	}
	public String getString(String key, String defaultValue){
		Object value = this.get(key);		
		return BaseUtils.toString(value, defaultValue);
	}
	
	public Integer getInteger(String key){
		return this.getInteger(key, null);
	}	
	public Integer getInteger(String key, Integer defaultValue){
		Object value = this.get(key);
		return BaseUtils.toInteger(value, defaultValue);
	}
	
	public Long getLong(String key){
		return this.getLong(key, null);
	}	
	public Long getLong(String key, Long defaultValue){
		Object value = this.get(key);
		return BaseUtils.toLong(value, defaultValue);
	}
	
	public Float getFloat(String key){
		return this.getFloat(key, null);
	}
	public Float getFloat(String key, Float defaultValue){
		Object value = this.get(key);
		return BaseUtils.toFloat(value, defaultValue);
	}
	
	public Boolean getBoolean(String key){
		return this.getBoolean(key, null);
	}
	public Boolean getBoolean(String key, Boolean defaultValue){
		Object value = this.get(key);
		return BaseUtils.toBoolean(value, defaultValue);
	}

	public BigDecimal getDecimal(String key){
		return this.getDecimal(key, null);
	}
	public BigDecimal getDecimal(String key, BigDecimal defaultValue){
		Object value = this.get(key);
		return BaseUtils.toDecimal(value, defaultValue);
	}
	
	public Date getDate(String key){
		return getDate(key, null);
	}
	public Date getDate(String key, String format){
		Object value = this.get(key);
		return BaseUtils.toDate(value, format);
	}
	
	
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(this);
		return map;
	}
}

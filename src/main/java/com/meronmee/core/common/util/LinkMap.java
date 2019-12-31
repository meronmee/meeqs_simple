package com.meronmee.core.common.util;

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
public class LinkMap<K, V> extends HashMap<K, V> {
	
	public LinkMap(){}
	
	public LinkMap(Map<K, V> map){
		this.putAll(map);
	}
	public LinkMap(K key, V value){
		this.put(key, value);
	}	
	/**
	 * 支持链式操作的append
	 * @param key
	 * @param value
	 * @return
	 */
	public LinkMap<K, V> append(K key, V value){
		this.put(key, value);		
		return this;
	}

	/**
	 * 支持链式操作的append
	 * @param key
	 * @param value
	 * @param cond 条件为true时放入map
	 * @return
	 */
	public LinkMap<K, V> append(K key, V value, boolean cond){
		if(cond) {
			this.put(key, value);
		}
		return this;
	}
	public LinkMap<K, V> append(Map<K, V> map){
		this.putAll(map);		
		return this;
	}
	public String getString(K key){
		return this.getString(key, null);
	}
	public String getString(K key, String defaultValue){
		V value = this.get(key);
		return BaseUtils.toString(value, defaultValue);
	}
	
	public Integer getInteger(K key){
		return this.getInteger(key, null);
	}	
	public Integer getInteger(K key, Integer defaultValue){
		V value = this.get(key);
		return BaseUtils.toInteger(value, defaultValue);
	}
	
	public Long getLong(K key){
		return this.getLong(key, null);
	}	
	public Long getLong(K key, Long defaultValue){
		V value = this.get(key);
		return BaseUtils.toLong(value, defaultValue);
	}
	
	public Float getFloat(K key){
		return this.getFloat(key, null);
	}
	public Float getFloat(K key, Float defaultValue){
		V value = this.get(key);
		return BaseUtils.toFloat(value, defaultValue);
	}
	
	public Boolean getBoolean(K key){
		return this.getBoolean(key, null);
	}
	public Boolean getBoolean(K key, Boolean defaultValue){
		V value = this.get(key);
		return BaseUtils.toBoolean(value, defaultValue);
	}

	public BigDecimal getDecimal(K key){
		return this.getDecimal(key, null);
	}
	public BigDecimal getDecimal(K key, BigDecimal defaultValue){
		V value = this.get(key);
		return BaseUtils.toDecimal(value, defaultValue);
	}
	
	public Date getDate(K key){
		return getDate(key, null);
	}
	public Date getDate(K key, String format){
		V value = this.get(key);
		return BaseUtils.toDate(value, format);		
	}
	
	public Map<K, V> toMap(){
		Map<K, V> map = new HashMap<K, V>();
		map.putAll(this);
		return map;
	}
}

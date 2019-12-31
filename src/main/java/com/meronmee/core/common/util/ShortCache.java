package com.meronmee.core.common.util;


import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 内存缓存工具 - 5秒缓存，一般用于一个Request周期内
 * @see https://www.cnblogs.com/liujinhua306/p/9808500.html
 * @author Meron
 *
 */
public enum ShortCache {
	//单元素的枚举类型是创建单例的最佳方式
	INSTANCE;

    private static final int init = 1;  //缓存容量初始大小
    private static final int max = 1000;  //缓存实体的最大数目
    private static final int duration = 5;  //缓存的有效期，单位：秒

    /**
     * 缓存
     */
    private LoadingCache<String, Object> cache;;

    //构造函数（枚举类型的构造函数只能为私有类型）
    private ShortCache() {
    	if(cache == null) {
    		cache = Caffeine.newBuilder()
                    .initialCapacity(init)
    				.maximumSize(max)
    				.expireAfterWrite(duration, TimeUnit.SECONDS)
    				.build(new CacheLoader<String, Object>() {
                        //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
	                    @Override  
	                    public Object load(String key) throws Exception {  
	                        return null;  
	                    }  
    				});
        } 
    }
    
    /**
     * 获取缓存
     * @param key
     * @return
     */
    public static Object get(String key){
    	if(key==null || key.length()==0){
    		return null;
    	}
    	return ShortCache.INSTANCE.cache.getIfPresent(key);
    }    
    /**
     * 获取缓存， "if cached, return; otherwise create, cache and return"
     * @param key
     * @param function
     * @return
     */
    public static Object get(String key, Function<String, ?> function){
        if(key==null || key.length()==0){
            return null;
        }
        return ShortCache.INSTANCE.cache.get(key, function);
    }
    
    /**
     * 批量获取缓存
     * @param keys
     * @return 返回不可更改的map
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getAll(String... keys){
    	if(keys==null || keys.length==0){
    		return Collections.EMPTY_MAP;
    	}
    	return ShortCache.INSTANCE.cache.getAllPresent(Arrays.asList(keys));
    }
    
    /**
     * 设置缓存
     * @param key
     * @param value
     */
    public static void set(String key, Object value){
    	if(key==null || key.length()==0||value==null){
    		return;
    	}
    	ShortCache.INSTANCE.cache.put(key, value);
    }
    /**
     * 删除缓存
     * @param key
     */
    public static void delete(String key){
    	if(key==null){
    		return;
    	}
    	ShortCache.INSTANCE.cache.invalidate(key);
    }
}

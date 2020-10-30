package com.meronmee.base.api.impl;

import com.meronmee.base.api.ConfigApi;
import com.meronmee.base.domain.Config;
import com.meronmee.core.common.util.BaseUtils;
import com.meronmee.core.common.util.Cache;
import com.meronmee.core.common.util.LinkMap;
import com.meronmee.core.service.database.DataService;
import com.meronmee.core.service.redis.RedisApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置信息服务类
 * @author Meron
 *
 */
@Service
public class ConfigApiImpl implements ConfigApi {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String cacheKey = "_DbConfig_";

	@Autowired
	private RedisApi redisApi;
    @Autowired
    private DataService dataService;

    @PostConstruct
	public void init(){
		clearCache();
	}

	/**
	 * 清空缓存
	 */
	@Override
    public void clearCache() {
		log.info("清空缓存的配置信息");
		this.redisApi.delete(cacheKey);
        getAll();
	}

	/**
	 * 查询所有配置信息
	 * @return
	 */
	@Override
    public Map<String, String> getAll(){
		//先从缓存中获取
		Map<String, String> configs = this.redisApi.hGetAll(cacheKey);
		if(BaseUtils.isNotEmpty(configs)){
			//log.info("使用缓存的全量配置信息");
			return configs;
		}

		configs = getFromDBAndCacheIt();

		return configs;
	}

    /**
     * 查询所有配置信息
     * @return
     */
    private Map<String, String> getFromDBAndCacheIt(){
        Map<String, String> configs = new HashMap<>();
        List<Config>  list = this.dataService.findByProperty(Config.class, "deleteStatus", 0);
        if(list==null||list.isEmpty()){
            return configs;
        }

        for(Config config : list){
            configs.put(config.getConfigKey(), config.getConfigValue());
        }

        //缓存配置
        this.redisApi.hSet(cacheKey, configs);

        return configs;
    }

	/**
	 * 查看特定前缀的配置信息
	 * @param prefix - 前缀， 如：fastDfs.
	 * @return
	 */
	@Override
    public Map<String, String> getWithPrefix(String prefix){
		Map<String, String> configs = new HashMap<String, String>();
		if(StringUtils.isBlank(prefix)){
			return configs;
		}

		//先加载全部配置
		Map<String, String> allConfigs = getAll();

		prefix = prefix.toLowerCase();
		for(Map.Entry<String, String> config : allConfigs.entrySet()){
			if(config.getKey().toLowerCase().startsWith(prefix)){
				configs.put(config.getKey(), config.getValue());
			}
		}

		return configs;
	}
    /**
     * 读取一条配置信息，使用本地缓存
     * @param key
     * @return
     */
    @Override
    public String getUseLocalCache(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }

        //先从本地缓读取
        String localCacheKey = cacheKey + key;
        String localCache = BaseUtils.toString(Cache.get(localCacheKey));
        if(StringUtils.isNotBlank(localCache)){//本地缓存
            return localCache;
        }

        //再从redis缓存中读取
        String cache = this.redisApi.hGet(cacheKey, key);
        if(StringUtils.isNotBlank(cache)){
            //存入本地缓存
            Cache.set(localCacheKey, cache);
            return cache;
        }

        Map<String, String> configs = getFromDBAndCacheIt();
        String result = BaseUtils.getFirstNotBlank(configs.get(key), "");

        //本地缓存当前配置
        Cache.set(localCacheKey, result);

        return result;
    }


    /**
     * 查看一条配置信息
     * @param key
     * @return
     */
    @Override
    public String get(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }

        String cache = this.redisApi.hGet(cacheKey, key);
        if(StringUtils.isNotBlank(cache)){
            //log.info("使用缓存的配置信息： "+key+"="+cache);
            return cache;
        }

        Map<String, String> configs = getFromDBAndCacheIt();

        return configs.get(key);
    }

	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public String get(String key, String defaultValue){
		String value = get(key);
		if(StringUtils.isBlank(value)){
			return defaultValue;
		}
		return value;
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Integer getAsInteger(String key, Integer defaultValue){
		String value = this.get(key);
		return BaseUtils.toInteger(value, defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Long getAsLong(String key, Long defaultValue){
		String value = this.get(key);
		return BaseUtils.toLong(value, defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Float getAsFloat(String key, Float defaultValue){
		String value = this.get(key);
		return BaseUtils.toFloat(value, defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Double getAsDouble(String key, Double defaultValue){
		String value = this.get(key);
		return BaseUtils.toDouble(value, defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Boolean getAsBoolean(String key, Boolean defaultValue){
		String value = this.get(key);
		return BaseUtils.toBoolean(value, defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
	@Override
    public Date getAsDate(String key, Date defaultValue){
		String value = this.get(key);
		return BaseUtils.ifNull(BaseUtils.toDate(value), defaultValue);
	}
	/**
     * 查看一条配置信息,如果为空，则返回默认值
     * @param key
     * @param format
     * @param defaultValue
     * @return
     */
	@Override
    public Date getAsDate(String key, String format, Date defaultValue){
		String value = this.get(key);
		return BaseUtils.ifNull(BaseUtils.toDate(value, format), defaultValue);
	}

	/**
    * 修改或新增一个配置信息
    * @param key
    * @param value
    * @return
    */
	@Override
    public void set(String key, String value){
		this.set(key, value, null);
	}
   /**
    * 修改或新增一个配置信息
    * @param key
    * @param value
    * @param remark
    * @return
    */
	@Override
    public void set(String key, String value, String remark){
		if(StringUtils.isBlank(key)){
			return;
		}
		this.redisApi.hPut(cacheKey, key, value);

        Map<String, Object> params = new LinkMap<String, Object> ()
                .append("deleteStatus", 0)
                .append("configKey", key);
		Config config = this.dataService.findOneByProps(Config.class, params);
		if(config == null){
            config = new Config();
            config.setConfigKey(key);
        }
        config.setConfigValue(value);
		if(BaseUtils.isNotBlank(remark)){
            config.setRemark(remark);
        }

		if(config.getId() != null){
		    this.dataService.update(config);
        } else {
		    this.dataService.create(config);
        }

		// 清除本地缓存
		try {
			String localCacheKey = cacheKey + key;
			Cache.delete(localCacheKey);
		} catch (Exception e) {
			
		}
	}

}

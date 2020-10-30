package com.meronmee.base.api;

import java.util.Date;
import java.util.Map;
/**
 * 系统数据库配置信息服务类
 * @author Meron
 *
 */
public interface ConfigApi {
    /**
     * 清空缓存
     */
    void clearCache();

    /**
     * 查询所有配置信息
     * @return
     */
    Map<String, String> getAll();

    /**
     * 查看特定前缀的配置信息
     * @param prefix - 前缀， 如：fastDfs.
     * @return
     */
    Map<String, String> getWithPrefix(String prefix);

    /**
     * 读取一条配置信息，使用本地缓存
     * @param key
     * @return
     */
    String getUseLocalCache(String key);

    /**
     * 查看一条配置信息
     * @param key
     * @return
     */
    String get(String key);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    String get(String key, String defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Integer getAsInteger(String key, Integer defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Long getAsLong(String key, Long defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Float getAsFloat(String key, Float defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Double getAsDouble(String key, Double defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Boolean getAsBoolean(String key, Boolean defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param defaultValue
* @return
*/
    Date getAsDate(String key, Date defaultValue);

    /**
* 查看一条配置信息,如果为空，则返回默认值
* @param key
* @param format
* @param defaultValue
* @return
*/
    Date getAsDate(String key, String format, Date defaultValue);

    /**
* 修改或新增一个配置信息
* @param key
* @param value
* @return
*/
    void set(String key, String value);

    /**
     * 修改或新增一个配置信息
     * @param key
     * @param value
     * @param remark
     * @return
     */
    void set(String key, String value, String remark);
}

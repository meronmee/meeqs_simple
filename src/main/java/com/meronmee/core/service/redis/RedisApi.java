package com.meronmee.core.service.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis常用操作服务类
 * <p>不常用的方法设为了private，需要的可以打开
 * @see https://github.com/whvcse/RedisUtil
 * @see https://blog.csdn.net/huijie618/article/details/68951477
 * @see https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:template
 * @see https://www.cnblogs.com/EasonJim/p/7805665.html
 * @see 集群版https://www.cnblogs.com/EasonJim/p/7805297.html
 * @since 2018-08-20
 * @author Meron 
 *
 */
public interface RedisApi {
	/**
	RedisTemplate和StringRedisTemplate的区别：
	1. StringRedisTemplate继承RedisTemplate。
	2. 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。使用xshell连接redis插入的数据 等同于使用StringRedisTemplate操作redis
	3. SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。
		StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。
		RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。	
	 */	


	/** -------------------key相关操作--------------------- */

	/**
	 * 单个或批量删除key
	 * 
	 * @param key
	 */
	public void delete(String... key);

	/**
	 * 批量删除key
	 * 
	 * @param keys
	 */
	public void delete(Collection<String> keys);


	/**
	 * 是否存在key
	 * 
	 * @param key
	 * @return
	 */
	public Boolean hasKey(String key);

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param timeout 单位：秒 
	 * @return
	 */
	public Boolean expire(String key, long timeout);

	/**
	 * 返回 key 所储存的值的类型
	 * 
	 * @param key
	 * @return
	 */
	public DataType type(String key);

	/** -------------------string相关操作--------------------- */

	/**
	 * 设置指定 key 的值为 vaule（无则新增，有则修改）
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);
	
	/**
	 * 设置指定 key 的值为 vaule，并将 key 的过期时间设为 timeout
	 * 
	 * @param key
	 * @param value
	 * @param timeout 过期时间,单位：秒 
	 */
	public void set(String key, String value, long timeout);

	/**
	 * 只有在 key 不存在时设置 key 的值
	 * 
	 * @param key
	 * @param value
	 * @return 之前已经存在返回false,不存在返回true
	 */
	public boolean setIfAbsent(String key, String value);
	
	/**
	 * 获取指定 key 的值
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String getAndSet(String key, String value);

	/**
	 * 批量获取
	 * 
	 * @param keys
	 * @return
	 */
	public List<String> multiGet(Collection<String> keys);

	/**
	 * 批量添加
	 * 
	 * @param maps
	 */
	public void multiSet(Map<String, String> maps);

	/**
	 * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
	 * 
	 * @param maps
	 * @return 之前已经存在返回false,不存在返回true
	 */
	public boolean multiSetIfAbsent(Map<String, String> maps);
	/**
	 * 增加(自增长), 负数则为自减
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long incrBy(String key, long increment);
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Double incrByFloat(String key, double increment) ;

	/**
	 * 追加到末尾
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Integer append(String key, String value);
	/** -------------------Object相关操作--------------------- */

	/**
	 * 设置指定 key 的值为 vaule（无则新增，有则修改） 
	 * @param <T>
	 * @param key
	 * @param value
	 */
	public <T> void setObj(String key, T value) ;
	
	/**
	 * 设置指定 key 的值为 vaule，并将 key 的过期时间设为 timeout
	 * @param <T>
	 * 
	 * @param key
	 * @param value
	 * @param timeout 过期时间,单位：秒 
	 */
	public <T> void setObj(String key, T value, long timeout);

	/**
	 * 只有在 key 不存在时设置 key 的值
	 * @param <T>
	 * 
	 * @param key
	 * @param value
	 * @return 之前已经存在返回false,不存在返回true
	 */
	public <T> boolean setObjIfAbsent(String key, T value);
	
	/**
	 * 获取指定 key 的值
	 * @param <V>
	 * @param key
	 * @return
	 */
	public <T> T getObj(String key);
	
	/** -------------------hash相关操作------------------------- */

	/**
	 * 获取存储在哈希表中指定字段的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
    public <V> V hGet(String key, String field);

	/**
	 * 获取所有给定字段的值
	 * 
	 * @param key
	 * @return
	 */
    public <V> Map<String, V> hGetAll(String key);

	/**
	 * 获取所有给定字段的值
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
    public <V> List<V> hMultiGet(String key, Collection<String> fields) ;
    /**
     * 设置key指向的hash的field的条目的值为value
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     */
    
    public <V> void hPut(String key, String field, V value);
    /**
     * 设置key的值为maps,同hPutAll
     * @param key
     * @param map
     */
    
    public <V> void hSet(String key, Map<String, V> map);
    /**
     * 往设置key指向的hash中加入maps,同hSet
     * @param key
     * @param map
     */
    
    public <V> void hPutAll(String key, Map<String, V> map);
    /**
     * 仅当field不存在时才设置
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    
    public <V> Boolean hPutIfAbsent(String key, String field, V value) ;

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key
     * @param fields
     * @return
     */
    
    public <V> void hDelete(String key, String... fields) ;

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key
     * @param field
     * @return
     */
    
    public <V> boolean hExists(String key, String field);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key
     * @param field
     * @param increment
     * @return
     */
    
    public <V> Long hIncrBy(String key, String field, long increment) ;

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 delta
     *
     * @param key
     * @param field
     * @param delta
     * @return
     */
    
    public <V> Double hIncrByFloat(String key, String field, double delta) ;

    /**
     * 获取所有哈希表中的字段
     *
     * @param key
     * @return
     */
    
    public <V> Set<String> hKeys(String key);

    /**
     * 获取哈希表中字段的数量
     *
     * @param key
     * @return
     */
    
    public <V> Long hSize(String key);

    /**
     * 获取哈希表中所有值
     *
     * @param key
     * @return
     */
    
    public <V> List<V> hValues(String key);

    /**
     * 迭代哈希表中的键值对
     *
     * @param key
     * @param options
     * @return
     */
    
    public <V> Cursor<Map.Entry<String, V>> hScan(String key, ScanOptions options);

	/** ------------------------list相关操作---------------------------- */
	/**
	 * 通过索引获取列表中的元素
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String lGet(String key, long index);

	/**
	 * 获取列表指定范围内的元素
	 * 
	 * @param key
	 * @param start
	 *            开始位置, 0是开始位置
	 * @param end
	 *            结束位置, -1返回所有
	 * @return
	 */
	public List<String> lSubList(String key, long start, long end);

	/**
	 * 存储在list头部
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPush(String key, String value);
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, String... value);
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushAll(String key, Collection<String> value) ;

	/**
	 * 当list存在的时候才加入
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lLeftPushIfPresent(String key, String value) ;

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPush(String key, String value);
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, String... value);
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushAll(String key, Collection<String> value);

	/**
	 * 为已存在的列表添加值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lRightPushIfPresent(String key, String value);


	/**
	 * 通过索引设置列表元素的值
	 * 
	 * @param key
	 * @param index
	 *            位置
	 * @param value
	 */
	public void lSet(String key, long index, String value);
	/**
	 * 移出并获取列表的第一个元素
	 * 
	 * @param key
	 * @return 删除的元素
	 */
	public String lLeftPop(String key);

	/**
	 * 移除并获取列表最后一个元素
	 * 
	 * @param key
	 * @return 删除的元素
	 */
	public String lRightPop(String key);



	/**
	 * 删除集合中值等于value得元素
	 * 
	 * @param key
	 * @param index
	 *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
	 *            index<0, 从尾部开始删除第一个值等于value的元素;
	 * @param value
	 * @return
	 */
	public Long lRemove(String key, long index, String value) ;

	/**
	 * 获取列表长度
	 * 
	 * @param key
	 * @return
	 */
	public Long lLen(String key);
	/** --------------------set相关操作-------------------------- */

	/**
	 * set添加元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sAdd(String key, String... values);
	/**
	 * set移除元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long sRemove(String key, Object... values);
	/**
	 * 移除并返回集合的一个随机元素
	 * 
	 * @param key
	 * @return
	 */
	public String sPop(String key);
	/**
	 * 将元素value从一个集合移到另一个集合
	 * 
	 * @param key
	 * @param value
	 * @param destKey
	 * @return
	 */
	public Boolean sMove(String key, String value, String destKey);

	/**
	 * 获取集合的大小
	 * 
	 * @param key
	 * @return
	 */
	public Long sSize(String key);
	/**
	 * 判断集合是否包含value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean sIsMember(String key, Object value);

	/**
	 * 获取集合所有元素
	 * 
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public Set<String> sMembers(String key);

	/**------------------zSet相关操作--------------------------------*/
	
	/**
	 * 添加元素,有序集合是按照元素的score值由小到大排列
	 * 
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public Boolean zAdd(String key, String value, double score);

	/**
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zAdd(String key, Set<TypedTuple<String>> values);
	/**
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public Long zRemove(String key, Object... values);
	/**
	 * 增加元素的score值，并返回增加后的值
	 * 
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public Double zIncrementScore(String key, String value, double delta);

	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 * 
	 * @param key
	 * @param value
	 * @return 0表示第一位
	 */
	public Long zRank(String key, Object value);
	/**
	 * 返回元素在集合的排名,按元素的score值由大到小排列
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long zReverseRank(String key, Object value);
	/**
	 * 获取集合的元素, 从小到大排序
	 * 
	 * @param key
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置, -1查询所有
	 * @return
	 */
	public Set<String> zRange(String key, long start, long end);


	/**
	 * 根据Score值查询集合元素
	 * 
	 * @param key
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public Set<String> zRangeByScore(String key, double min, double max);

	/**
	 * 根据Score值查询集合元素, 从小到大排序
	 * 
	 * @param key
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public Set<TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max);


	/**
	 * 获取集合的元素, 从大到小排序
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zReverseRange(String key, long start, long end);


	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
    public Set<String> zReverseRangeByScore(String key, double min, double max);

	/**
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
    public Set<String> zReverseRangeByScore(String key, double min, double max, long start, long end);

	/**
	 * 根据score值获取集合元素数量
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zCount(String key, double min, double max);
	/**
	 * 获取集合大小
	 * 
	 * @param key
	 * @return
	 */
	public Long zSize(String key);

	/**
	 * 获取集合中value元素的score值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Double zScore(String key, Object value);
	/**
	 * 移除指定索引位置的成员
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zRemoveRange(String key, long start, long end);

	/**
	 * 根据指定的score值的范围来移除成员
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long zRemoveRangeByScore(String key, double min, double max);


    /**-----------------使用RedisCallback --------------------------------*/

    /**
     * 使用StringRedisTemplate执行RedisCallback<p>
     * 使用RedisCallback能够直接操作RedisConnection，具有更底层操作权限，可以一次执行多个redis命令，方便事务控制。<p>
     * 注意：使用StringRedisTemplate的时候，callback中拿到的是一个StringRedisConnection实例
     * @param callback 示例：<pre>
     * 	new RedisCallback<Object>() {
    public Object doInRedis(RedisConnection connection) throws DataAccessException {
    StringRedisConnection	sconnection = (StringRedisConnection)connection;

    Long size = sconnection.dbSize();

    sconnection.set("key", "value");
    }
    });<pre>
     * @return
     */
    
    public <T> T execute(RedisCallback<T> callback);

    /**-----------------分布式锁 --------------------------------*/
    /**
     * 尝试获取分布式锁
     * @param key 锁
     * @param requestId 请求标识，释放锁时必须具有相同的requestId，一般使用UUID
     * @param expire 超时时间，即最大锁定时间，单位：秒。可以防止加锁者出错导致死锁
     * @return 是否获取成功
     * @see https://www.cnblogs.com/linjiqin/p/8003838.html
     * @see https://blog.csdn.net/qq_28397259/article/details/80839072
     */
    public boolean getLock(final String key, final String requestId, final int expire);
    /**
     * 尝试释放分布式锁
     * @param key 锁
     * @param requestId 请求标识，该值必须与加锁时的requestId相同
     * @return 是否释放成功
     * @see https://www.cnblogs.com/linjiqin/p/8003838.html
     * @see https://blog.csdn.net/qq_28397259/article/details/80839072
     * @see https://blog.csdn.net/zhanghan18333611647/article/details/79594974
     */
    public boolean releaseLock(final String key, final String requestId);
    
    /**-----------------分布式计数器 --------------------------------*/
    //incr
    //get
    //check
    //https://blog.csdn.net/zhanghan18333611647/article/details/79594974
}

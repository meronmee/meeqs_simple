package com.meronmee.core.common.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Kryo 版的 RedisSerializer 
 * @author Meron
 *
 * @param <T>
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {     
    @Override
    public byte[] serialize(Object obj) throws SerializationException {
    	return KryoSerializerUtil.serialize(obj);
    }
   
	@Override
    public T deserialize(byte[] bytes) throws SerializationException {
		return KryoSerializerUtil.deserialize(bytes);
    }
}


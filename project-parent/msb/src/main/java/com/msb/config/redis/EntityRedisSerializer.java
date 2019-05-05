package com.msb.config.redis;

import com.personal.common.utils.base.SerializeUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

public class EntityRedisSerializer implements RedisSerializer<Object> {
    @Nullable
    @Override
    public byte[] serialize(@Nullable Object o) throws SerializationException {
        if (o == null) {
            return new byte[0];
        }
        return SerializeUtil.serialize(o);
    }

    @Nullable
    @Override
    public Object deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return SerializeUtil.unSerialize(bytes);
    }
}

package com.baiyi.mybatis.cache;

import com.baiyi.mybatis.util.JedisUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.ibatis.cache.Cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/29
 */
public class MyMybatisCache2 implements Cache {

    private static final String KEY_MUST_NOT_NULL = "key must not null";
    private String id;

    public MyMybatisCache2(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        Objects.requireNonNull(key, KEY_MUST_NOT_NULL);
        Objects.requireNonNull(value, "value must not null");
        byte[] keySerialization = SerializationUtils.serialize((Serializable) key);
        byte[] valueSerialization = SerializationUtils.serialize((Serializable) value);
        if (id == null) {
            id = getClass().getName();
        }
        byte[] idSerialization = SerializationUtils.serialize(this.id);
        JedisUtils.openJedis().hset(idSerialization, keySerialization, valueSerialization);
    }

    @Override
    public Object getObject(Object key) {
        Objects.requireNonNull(key, KEY_MUST_NOT_NULL);
        byte[] keySerialization = SerializationUtils.serialize((Serializable) key);
        if (id == null) {
            id = getClass().getName();
        }
        byte[] idSerialization = SerializationUtils.serialize(this.id);
        byte[] values = JedisUtils.openJedis().hget(idSerialization, keySerialization);
        if (values != null) {
            return SerializationUtils.deserialize(values);
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        Objects.requireNonNull(key, KEY_MUST_NOT_NULL);
        if (id == null) {
            id = getClass().getName();
        }
        byte[] idSerialization = SerializationUtils.serialize(this.id);
        byte[] serialize = SerializationUtils.serialize((Serializable) key);
        return JedisUtils.openJedis().hdel(idSerialization, serialize);
    }

    @Override
    public void clear() {
        if (id == null) {
            id = getClass().getName();
        }
        byte[] idSerialization = SerializationUtils.serialize(this.id);
        JedisUtils.openJedis().hdel(idSerialization);
    }

    @Override
    public int getSize() {
        if (id == null) {
            id = getClass().getName();
        }
        byte[] idSerialization = SerializationUtils.serialize(this.id);
        Long hlen = JedisUtils.openJedis().hlen(idSerialization);
        return hlen.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}

package com.baiyi.mybatis.cache;

import org.apache.ibatis.cache.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @description: 自定义 Mybatis cache 实现
 * @author: BaiYi
 * @date: 2023/5/26 14:48
 */
public class MyMybatisCache implements Cache {
    private final Map<Object, Object> internalCache = new ConcurrentHashMap<>();
    @Override
    public String getId() {
        return MyMybatisCache.class.getName() + System.currentTimeMillis();
    }

    @Override
    public void putObject(Object key, Object value) {
        internalCache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return internalCache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return internalCache.remove(key);
    }

    @Override
    public void clear() {
        internalCache.clear();
    }

    @Override
    public int getSize() {
        return internalCache.size();
    }

//    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}

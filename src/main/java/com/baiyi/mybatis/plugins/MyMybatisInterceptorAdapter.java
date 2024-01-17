package com.baiyi.mybatis.plugins;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

/**
 * @description: Mybatis Interceptor 适配器，减少重复代码
 * @author: baiyi
 * @date: 2023/6/12
 */
public abstract class MyMybatisInterceptorAdapter implements Interceptor {

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

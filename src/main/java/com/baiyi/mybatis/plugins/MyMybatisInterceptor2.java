package com.baiyi.mybatis.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * @description:
 * @author: baiyi
 * @date: 2023/6/12
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MyMybatisInterceptor2 extends MyMybatisInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(MyMybatisInterceptor2.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 使用 Mybatis 封装的反射工具类，这种方式是常用的
        MetaObject metaObject = SystemMetaObject.forObject(invocation);
        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");
        if (log.isDebugEnabled()) {
            log.debug("sql: {}", sql);
        }
        return invocation.proceed();
    }
}

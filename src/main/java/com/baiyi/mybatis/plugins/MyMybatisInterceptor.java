package com.baiyi.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/6/7
 */
@Intercepts(
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
)
public class MyMybatisInterceptor extends MyMybatisInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(MyMybatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("invocation: {}", invocation.toString());
        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        String username = properties.getProperty("username");
        log.info("username: {}", username);
    }
}

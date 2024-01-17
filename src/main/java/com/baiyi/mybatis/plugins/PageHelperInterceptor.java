package com.baiyi.mybatis.plugins;

import com.baiyi.mybatis.util.Page;
import com.baiyi.mybatis.util.ThreadLocalUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @description: 自定义分页插件
 * @author: baiyi
 * @date: 2023/6/14 10:53
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PageHelperInterceptor extends MyMybatisInterceptorAdapter {
    // 分页方法前缀
    private String pageHelperPrefix;
    // 分页方法后缀
    private String pageHelperSuffix;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 使用 Mybatis 封装的反射工具类，这种方式是常用的
        MetaObject metaObject = SystemMetaObject.forObject(invocation);
        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("target.delegate.mappedStatement");
        String id = mappedStatement.getId();
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.SELECT.equals(sqlCommandType)) {
            if (id.contains(pageHelperPrefix) && id.endsWith(pageHelperSuffix)) {
                // 分页相关的操作封装 对象（vo dto)
                // 获得Page对象 并设置Page对象 totalSize属性 算出总页数

                // 假设 Page
                // Page page = new Page(1);

                // 直接通过DAO方法的参数 获得Page对象
                // Page page = (Page) metaObject.getValue("target.delegate.parameterHandler.parameterObject");

                //通过ThreadLocalUtils
                Page page = ThreadLocalUtils.get();
                //清空一下

                //select id,name from t_user 获得 全表有多少条数据
                // select count(*) from t_user

                //select id,name from t_user where name = ?;
                //select count(*）fromt t_user where name = ?

                //select id,name from t_user where  name = ? and id = ?;

                String countSql = "select count(*) " + sql.substring(sql.indexOf("from"));
                //JDBC操作
                //1 Connection  PreapredStatement
                Connection conn = (Connection) invocation.getArgs()[0];
                PreparedStatement preparedStatement = conn.prepareStatement(countSql);

               /* preparedStatement.setString(1,?)
                preparedStatement.setString(2,?);*/
                ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("target.delegate.parameterHandler");
                parameterHandler.setParameters(preparedStatement);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    page.setTotalSize(resultSet.getInt(1));
                }
                //page.setTotalSize();

                //做一个判断 如果当前是MySQL 如果是Oracle....
                //if databaseType == "oracle" or "mysql"
                String newSql = sql + " limit " + page.getFirstItem() + "," + page.getPageCount();

                metaObject.setValue("target.delegate.boundSql.sql", newSql);
            }
        }


        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        pageHelperPrefix = properties.getProperty("pageHelperPrefix");
        pageHelperSuffix = properties.getProperty("pageHelperSuffix");
    }
}

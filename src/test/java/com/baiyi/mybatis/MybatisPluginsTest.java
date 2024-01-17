package com.baiyi.mybatis;

import com.baiyi.mybatis.dao.UserDAO;
import com.baiyi.mybatis.entity.User;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

/**
 * @description:
 * @author: baiyi
 * @date: 2023/6/10
 */
public class MybatisPluginsTest {

    /**
     * 用于测试:
     */
    @Test
    public void test_plugins() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDAO mapper = sqlSession.getMapper(UserDAO.class);
        List<User> users = mapper.queryAllUsersByPage();
        users.forEach(System.out::println);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 用于测试: jSqlParser
     */
    @Test
    public void test_jSqlParser() throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader("select id, name, password from t_user where name = 'baiyi'"));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        FromItem fromItem = plainSelect.getFromItem();
        System.out.println("fromItem = " + fromItem);
        Assert.assertEquals("t_user", fromItem.toString());
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        selectItems.forEach(System.out::println);

        Expression where = plainSelect.getWhere();
        System.out.println("where = " + where);
    }
}

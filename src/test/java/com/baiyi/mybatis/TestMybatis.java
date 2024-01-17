package com.baiyi.mybatis;

import com.baiyi.mybatis.dao.UserDAO;
import com.baiyi.mybatis.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/8 21:35
 */
public class TestMybatis {

    /**
     * 用于测试:基本的Mybatis开发步骤
     */
    @Test
    public void test1() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserDAO userDAO = sqlSession.getMapper(UserDAO.class);

        List<User> users = userDAO.queryAllUsersByPage();

        for (User user : users) {
            System.out.println("user = " + user);
        }
    }

    /**
     * 用于测试:Mybatis SQLSession的第二种用法
     */
    @Test
    public void test2() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        List<User> users = sqlSession.selectList("com.baiyi.mybatis.dao.UserDAO.queryAllUsersByPage");
                        /* sqlSession.selectOne()
                           sqlSession.insert()
                           sqlSession.delete()
                           sqlSession.update() */
        for (User user : users) {
            System.out.println("user = " + user);
        }

        sqlSession.insert("");
    }

    /**
     * 用于测试: XPathParser 测试
     */
    @Test
    public void test_xml() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("users.xml");
        XPathParser xPathParser = new XPathParser(inputStream);
        // xNodes 里面对应的是 <user>
        // 实际上 Mybatis 这里解析的是 configuration 标签
        List<XNode> xNodes = xPathParser.evalNodes("/users/*");
        List<com.baiyi.mybatis.xml.User> users = new ArrayList<>();
        xNodes.forEach(xNode -> {
            com.baiyi.mybatis.xml.User user = new com.baiyi.mybatis.xml.User();
            List<XNode> children = xNode.getChildren();
            user.setName(children.get(0).getStringBody());
            user.setPassword(children.get(1).getStringBody());
            users.add(user);
        });
        users.forEach(System.out::println);
    }

    /**
     * 用于测试: 一级缓存
     * 一级缓存默认是打开的，只能在一个 SqlSession 会话生效
     */
    @Test
    public void test_cache() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserDAO userDAO = sqlSession.getMapper(UserDAO.class);

        List<User> users = userDAO.queryAllUsersByPage();

        for (User user : users) {
            System.out.println("user = " + user);
        }

        List<User> users1 = userDAO.queryAllUsersByPage();
        users1.forEach(user -> System.out.println("user = " + user));

        // 第二个 SqlSession 无法使用第一个会话的缓存，这就是一级缓存
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        UserDAO userDAO2 = sqlSession2.getMapper(UserDAO.class);

        List<User> users2 = userDAO2.queryAllUsersByPage();

        for (User user : users2) {
            System.out.println("user = " + user);
        }
    }


    /**
     * 用于测试: 二级缓存
     */
    @Test
    public void test_second_cache() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserDAO userDAO = sqlSession.getMapper(UserDAO.class);

        List<User> users = userDAO.queryAllUsersByPage();

        for (User user : users) {
            System.out.println("user = " + user);
        }
        sqlSession.commit();
        sqlSession.close();

        // 第二个 SqlSession 使用第一个会话生成的缓存
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        UserDAO userDAO2 = sqlSession2.getMapper(UserDAO.class);

        List<User> users2 = userDAO2.queryAllUsersByPage();

        for (User user : users2) {
            System.out.println("user = " + user);
        }
        sqlSession2.commit();
        sqlSession2.close();
    }


    /**
     * 用于测试:
     */
    @Test
    public void test_covert() {
        List<Student> students = Arrays.asList(
                new Student("a", 100),
                new Student("a", 200),
                new Student("b", 200)
        );
        List<Student> resultList = new ArrayList<>(students.stream()
                .collect(Collectors.toMap(Student::getName, Function.identity(), (oldValue, newValue) -> {
                    oldValue.setNum(oldValue.getNum() + newValue.getNum());
                    return oldValue;
                })).values());
        System.out.println("resultList = " + resultList);

        List<Student> result = students.stream()
                .collect(Collectors.groupingBy(Student::getName))
                .values().stream()
                .map(studentList -> studentList.stream()
                        .reduce((s1, s2) -> new Student(s1.getName(), s1.getNum() + s2.getNum()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("result = " + result);
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
        private Integer num;
    }
}

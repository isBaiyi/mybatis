package com.baiyi.mybatis;

import com.baiyi.mybatis.dao.Cache;
import com.baiyi.mybatis.dao.ProductDAO;
import com.baiyi.mybatis.dao.ProductDAOImpl;
import com.baiyi.mybatis.entity.Product;
import com.baiyi.mybatis.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/25 14:50
 */
public class TestMybatis2 {

    /**
     * 用于测试: 自定义缓存cache
     */
    @Test
    public void test_define_cache() {
        ProductDAO productDAO = new ProductDAOImpl();
        ProductDAO productDAOProxy = (ProductDAO) Proxy.newProxyInstance(TestMybatis2.class.getClassLoader(), new Class[]{ProductDAO.class}, (proxy, method, args) -> {
//            // 方法 只有以query开头，在进行缓存的处理 如果不是以query开头，就直接运行
//            if (method.getName().startsWith("query")) {
//                System.out.println("连接redis 查看数据是否 存在 如果存在 则直接返回 return data");
//                return method.invoke(productDAO, args);
//            }
            Cache cache = method.getDeclaredAnnotation(Cache.class);
            if (cache != null) {
                System.out.println("连接redis 查看数据是否 存在 如果存在 则直接返回 return data");
                return method.invoke(productDAO, args);
            }

            //非查询方法
            return method.invoke(productDAO, args);
        });

        productDAOProxy.save(new Product());
        System.out.println("---------------------------------");
        productDAOProxy.queryProductById(10);
        System.out.println("---------------------------------");
        productDAOProxy.queryAllProducts();
    }


    /**
     * 用于测试: common-lang3 SerializationUtils 序列化和反序列化测试
     */
    @Test
    public void test_serializable() {
        byte[] serialize = SerializationUtils.serialize(new User(1, "baiyi"));
        System.out.println("serialize = " + serialize);
        Object deserialize = SerializationUtils.deserialize(serialize);
        System.out.println("deserialize = " + deserialize);
    }


}

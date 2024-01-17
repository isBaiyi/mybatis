package com.baiyi.mybatis.dao;

import com.baiyi.mybatis.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/25 14:47
 */
public class ProductDAOImpl implements ProductDAO {
    @Override
    public Product queryProductById(Integer id) {
        System.out.printf("jdbc根据id: %s 查询商品%n", id);
        return new Product();
    }

    @Override
    public void save(Product product) {
        System.out.println("jdbc新增商品");
    }

    @Override
    public List<Product> queryAllProducts() {
        System.out.println("jdbc查询全量商品");
        return new ArrayList<>();
    }
}

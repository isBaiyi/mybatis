package com.baiyi.mybatis.dao;

import com.baiyi.mybatis.entity.Product;

import java.util.List;

/**
 * @description: 商品DAO
 * @author: BaiYi
 * @date: 2023/5/25 14:44
 */
public interface ProductDAO {
    @Cache
    Product queryProductById(Integer id);

    void save(Product product);

    @Cache
    List<Product> queryAllProducts();
}

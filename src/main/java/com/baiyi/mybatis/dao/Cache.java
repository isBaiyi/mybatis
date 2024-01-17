package com.baiyi.mybatis.dao;

import java.lang.annotation.*;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/25 15:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * 过期算法：LRU、FIFO等
     */
    String eviction() default "";
}

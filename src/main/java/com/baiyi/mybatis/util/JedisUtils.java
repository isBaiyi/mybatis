package com.baiyi.mybatis.util;

import redis.clients.jedis.Jedis;

/**
 * @description:
 * @author: BaiYi
 * @date: 2023/5/29
 */
public class JedisUtils {

    public static Jedis openJedis() {
        return new Jedis("127.0.0.1", 6379);
    }

}

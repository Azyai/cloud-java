package com.cn.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.cn.redis.utils.RedisConstants.BLOOM_FILTER_KEY;

@Component
public class RedisBloomFilterUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean add(String value) {
        return Boolean.TRUE.equals(redisTemplate
                .opsForValue().setBit(BLOOM_FILTER_KEY,
                hash(value), true));
    }

    public boolean mightContain(String value) {
        // 检查多个hash位置是否都为true
        return Boolean.TRUE.equals(redisTemplate
                .opsForValue().getBit(BLOOM_FILTER_KEY,
                hash(value)));
    }

    private long hash(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }

        long hash = 0;
        for (int i = 0; i < value.length(); i++) {
            hash = 31 * hash + value.charAt(i);
        }

        // 限制在100万位范围内 (0 - 1000000)
        long result = Math.abs(hash) % 1000000;
        return result;
    }

    // 添加随机因子，避免同时过期
    public Long getRandomTTL(Long baseTTL) {
        return baseTTL + (int)(Math.random() * 10); // 增加0-10分钟的随机时间
    }

}

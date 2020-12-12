package com.example.demo.redisdemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "ZX:TEST:KEY";

    public String get(final String key) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + key);
    }

    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value);
            result = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public boolean zSetAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(REDIS_KEY_PREFIX + key, value, score);
    }

    public Set<String> zSetGet(String key, double score1, double score2) {
        return redisTemplate.opsForZSet().rangeByScore(REDIS_KEY_PREFIX + key, score1, score2);
    }
}

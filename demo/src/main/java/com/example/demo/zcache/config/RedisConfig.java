package com.example.demo.zcache.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisFactory);
        RedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(keySerializer);
        return redisTemplate;
    }

}

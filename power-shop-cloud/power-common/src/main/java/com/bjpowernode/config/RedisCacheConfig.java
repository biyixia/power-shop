package com.bjpowernode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        //存储键值对时，指定value的存储格式为json
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        return redisCacheConfiguration;
    }
}

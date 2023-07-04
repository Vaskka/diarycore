package com.vaskka.diary.core.config;

import com.vaskka.diary.core.model.dataobject.AuthStorageDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, AuthStorageDO> authRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, AuthStorageDO> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}

package com.example.userprofileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.userprofileservice.entity.User;

@Configuration
public class AppConfig {

    @Bean
    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, User> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}

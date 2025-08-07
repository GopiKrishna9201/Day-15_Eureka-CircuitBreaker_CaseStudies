package com.example.userprofileservice.service;

import com.example.userprofileservice.entity.User;
import com.example.userprofileservice.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final String CACHE_PREFIX = "user::";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Override
    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "getUserFromFallback")
    public User getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found in DB"));

        
        redisTemplate.opsForValue().set(CACHE_PREFIX + userId, user);
        return user;
    }

    
    public User getUserFromFallback(String userId, Throwable t) {
        System.out.println("⚠️ Circuit breaker fallback triggered for user: " + userId);

        User cachedUser = redisTemplate.opsForValue().get(CACHE_PREFIX + userId);
        if (cachedUser != null) {
            return cachedUser;
        }

       
        return new User(userId, "Fallback User", "fallback@example.com");
    }
}

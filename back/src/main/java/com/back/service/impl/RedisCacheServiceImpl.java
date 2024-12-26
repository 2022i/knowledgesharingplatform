package com.back.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lzz
 */
@Service
public class RedisCacheServiceImpl  {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Long del(String... key) {
        List<String> keys = Arrays.asList(key);
        return stringRedisTemplate.delete(keys);
    }
}
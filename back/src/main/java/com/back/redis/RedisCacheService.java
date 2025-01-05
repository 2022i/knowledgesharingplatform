package com.back.redis;

import java.util.concurrent.TimeUnit;

/**
 * @author lzz
 */
public interface RedisCacheService {

    void set(String key, String value,long timeout, TimeUnit unit) ;

    void set(String key, String value) ;

    String get(String key) ;

    Long del(String... key) ;
}

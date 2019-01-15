package com.edu.caradmin.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableList;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 缓存配置
 *
 * @author Administrator
 * @date 2019/1/14 10:14
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "Caffeine")
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }

    private static List<String> getNames() {
        return ImmutableList.of("cars","customers");
    }
}

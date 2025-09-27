package com.taskmanager.config;

import com.taskmanager.service.cache.CacheService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import com.taskmanager.service.messaging.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {
    
    @Bean
    public CacheService cacheService() {
        return mock(CacheService.class);
    }
    
    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return mock(CacheKeyGenerator.class);
    }
    
    @Bean
    public MessageProducer messageProducer() {
        return mock(MessageProducer.class);
    }
}

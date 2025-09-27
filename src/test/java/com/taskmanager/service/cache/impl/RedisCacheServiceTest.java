package com.taskmanager.service.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.service.cache.impl.RedisCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private RedisCacheService cacheService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        cacheService = new RedisCacheService(redisTemplate, objectMapper);
    }

    @Test
    void get_shouldReturnValueWhenExists() {
        String key = "test:key";
        String expectedValue = "test value";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(expectedValue);

        Optional<String> result = cacheService.get(key, String.class);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedValue);
        verify(valueOperations).get(key);
    }

    @Test
    void get_shouldReturnEmptyWhenNotExists() {
        String key = "test:key";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        Optional<String> result = cacheService.get(key, String.class);

        assertThat(result).isEmpty();
        verify(valueOperations).get(key);
    }

    @Test
    void get_shouldReturnEmptyWhenException() {
        String key = "test:key";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenThrow(new RuntimeException("Redis error"));

        Optional<String> result = cacheService.get(key, String.class);

        assertThat(result).isEmpty();
    }

    @Test
    void put_shouldStoreValueWithDefaultTTL() {
        String key = "test:key";
        String value = "test value";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheService.put(key, value);

        verify(valueOperations).set(key, value);
    }

    @Test
    void put_shouldStoreValueWithCustomTTL() {
        String key = "test:key";
        String value = "test value";
        Integer ttlSeconds = 300;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        cacheService.put(key, value, ttlSeconds);

        verify(valueOperations).set(key, value, java.time.Duration.ofSeconds(ttlSeconds));
    }

    @Test
    void put_shouldHandleException() {
        String key = "test:key";
        String value = "test value";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new RuntimeException("Redis error")).when(valueOperations).set(anyString(), any());

        cacheService.put(key, value);
    }

    @Test
    void evict_shouldDeleteKey() {
        String key = "test:key";
        when(redisTemplate.delete(key)).thenReturn(true);

        cacheService.evict(key);

        verify(redisTemplate).delete(key);
    }

    @Test
    void evict_shouldHandleKeyNotFound() {
        String key = "test:key";
        when(redisTemplate.delete(key)).thenReturn(false);

        cacheService.evict(key);

        verify(redisTemplate).delete(key);
    }

    @Test
    void evictByPattern_shouldDeleteMatchingKeys() {
        String pattern = "test:*";
        java.util.Set<String> keys = java.util.Set.of("test:key1", "test:key2");
        when(redisTemplate.keys(pattern)).thenReturn(keys);
        when(redisTemplate.delete(keys)).thenReturn(2L);

        cacheService.evictByPattern(pattern);

        verify(redisTemplate).keys(pattern);
        verify(redisTemplate).delete(keys);
    }

    @Test
    void evictByPattern_shouldHandleNoKeysFound() {
        String pattern = "test:*";
        when(redisTemplate.keys(pattern)).thenReturn(java.util.Collections.emptySet());

        cacheService.evictByPattern(pattern);

        verify(redisTemplate).keys(pattern);
        verify(redisTemplate, never()).delete(any(java.util.Collection.class));
    }

    @Test
    void exists_shouldReturnTrueWhenKeyExists() {   
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenReturn(true);

        boolean result = cacheService.exists(key);

        assertThat(result).isTrue();
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void exists_shouldReturnFalseWhenKeyNotExists() {
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenReturn(false);

        boolean result = cacheService.exists(key);

        assertThat(result).isFalse();
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void exists_shouldReturnFalseWhenException() {
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenThrow(new RuntimeException("Redis error"));

        boolean result = cacheService.exists(key);

        assertThat(result).isFalse();
    }

    @Test
    void clear_shouldHandleException() {
        when(redisTemplate.getConnectionFactory()).thenThrow(new RuntimeException("Connection error"));

        cacheService.clear();
    }

    @Test
    void getStats_shouldHandleException() {
        when(redisTemplate.getConnectionFactory()).thenThrow(new RuntimeException("Connection error"));

        Map<String, Object> stats = cacheService.getStats();

        assertThat(stats).isNotNull();
        assertThat(stats).containsKey("error");
    }
}
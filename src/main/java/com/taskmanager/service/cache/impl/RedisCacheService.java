package com.taskmanager.service.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile({"redis", "postgres"})
public class RedisCacheService implements CacheService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("Cache miss for key: {}", key);
                return Optional.empty();
            }
            
            T result = objectMapper.convertValue(value, type);
            log.debug("Cache hit for key: {}", key);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Error getting value from cache for key: {}", key, e);
            return Optional.empty();
        }
    }
    
    @Override
    public <T> Optional<List<T>> getList(String key, Class<T> elementType) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("Cache miss for list key: {}", key);
                return Optional.empty();
            }
            
            List<T> result = objectMapper.convertValue(value, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
            log.debug("Cache hit for list key: {}", key);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Error getting list from cache for key: {}", key, e);
            return Optional.empty();
        }
    }
    
    @Override
    public void put(String key, Object value, Integer ttlSeconds) {
        try {
            if (ttlSeconds != null && ttlSeconds > 0) {
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
                log.debug("Cached value with TTL {} seconds for key: {}", ttlSeconds, key);
            } else {
                redisTemplate.opsForValue().set(key, value);
                log.debug("Cached value with default TTL for key: {}", key);
            }
        } catch (Exception e) {
            log.error("Error putting value to cache for key: {}", key, e);
        }
    }
    
    @Override
    public void put(String key, Object value) {
        put(key, value, null);
    }
    
    @Override
    public void evict(String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.debug("Evicted key: {}", key);
            } else {
                log.debug("Key not found for eviction: {}", key);
            }
        } catch (Exception e) {
            log.error("Error evicting key from cache: {}", key, e);
        }
    }
    
    @Override
    public void evictByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                log.debug("Evicted {} keys matching pattern: {}", deletedCount, pattern);
            } else {
                log.debug("No keys found matching pattern: {}", pattern);
            }
        } catch (Exception e) {
            log.error("Error evicting keys by pattern: {}", pattern, e);
        }
    }
    
    @Override
    public boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Error checking key existence: {}", key, e);
            return false;
        }
    }
    
    @Override
    public void clear() {
        try {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
            log.info("Cleared all cache");
        } catch (Exception e) {
            log.error("Error clearing cache", e);
        }
    }
    
    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Properties info = redisTemplate.getConnectionFactory().getConnection().serverCommands().info();
            
            stats.put("redis_version", info.getProperty("redis_version"));
            stats.put("used_memory_human", info.getProperty("used_memory_human"));
            stats.put("connected_clients", info.getProperty("connected_clients"));
            stats.put("total_commands_processed", info.getProperty("total_commands_processed"));
            stats.put("keyspace_hits", info.getProperty("keyspace_hits"));
            stats.put("keyspace_misses", info.getProperty("keyspace_misses"));
            
            String hitsStr = info.getProperty("keyspace_hits");
            String missesStr = info.getProperty("keyspace_misses");
            
            if (hitsStr != null && missesStr != null) {
                try {
                    long hits = Long.parseLong(hitsStr);
                    long misses = Long.parseLong(missesStr);
                    long total = hits + misses;
                    
                    if (total > 0) {
                        double hitRatio = (double) hits / total;
                        stats.put("hit_ratio", String.format("%.2f%%", hitRatio * 100));
                    } else {
                        stats.put("hit_ratio", "0.00%");
                    }
                } catch (NumberFormatException e) {
                    stats.put("hit_ratio", "N/A");
                }
            } else {
                stats.put("hit_ratio", "N/A");
            }
            
            Set<String> allKeys = redisTemplate.keys("*");
            stats.put("total_keys", allKeys != null ? allKeys.size() : 0);
            
        } catch (Exception e) {
            log.error("Error getting cache stats", e);
            stats.put("error", "Failed to get stats: " + e.getMessage());
        }
        
        return stats;
    }
}

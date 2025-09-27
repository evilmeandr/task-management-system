package com.taskmanager.service.cache;

import java.util.List;
import java.util.Optional;

public interface CacheService {
    <T> Optional<T> get(String key, Class<T> type);
    
    <T> Optional<List<T>> getList(String key, Class<T> elementType);
    
    void put(String key, Object value, Integer ttlSeconds);
    
    void put(String key, Object value);
    
    void evict(String key);
    
    void evictByPattern(String pattern);
    
    boolean exists(String key);
    
    void clear();
    
    java.util.Map<String, Object> getStats();
}


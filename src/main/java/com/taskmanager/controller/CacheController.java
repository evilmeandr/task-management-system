package com.taskmanager.controller;

import com.taskmanager.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Profile({"redis", "postgres"})
public class CacheController {
    
    private final CacheService cacheService;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        log.debug("Getting cache statistics");
        
        try {
            Map<String, Object> stats = cacheService.getStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting cache stats", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cache statistics: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearCache() {
        log.info("Clearing all cache");
        
        try {
            cacheService.clear();
            return ResponseEntity.ok(Map.of("message", "Cache cleared successfully"));
        } catch (Exception e) {
            log.error("Error clearing cache", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to clear cache: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/clear/pattern")
    public ResponseEntity<Map<String, String>> clearCacheByPattern(@RequestParam String pattern) {
        log.info("Clearing cache with pattern: {}", pattern);
        
        try {
            cacheService.evictByPattern(pattern);
            return ResponseEntity.ok(Map.of("message", "Cache cleared for pattern: " + pattern));
        } catch (Exception e) {
            log.error("Error clearing cache with pattern: {}", pattern, e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to clear cache with pattern: " + e.getMessage()));
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkKeyExists(@RequestParam String key) {
        log.debug("Checking if key exists: {}", key);
        
        try {
            boolean exists = cacheService.exists(key);
            return ResponseEntity.ok(Map.of("key", key, "exists", exists));
        } catch (Exception e) {
            log.error("Error checking key existence: {}", key, e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to check key existence: " + e.getMessage()));
        }
    }
    
    @GetMapping("/info")
    @Profile("dev")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        log.debug("Getting cache info");
        
        try {
            Map<String, Object> stats = cacheService.getStats();
            return ResponseEntity.ok(Map.of(
                "cache_type", "Redis",
                "statistics", stats,
                "available_operations", new String[]{
                    "GET /api/cache/stats - получить статистику",
                    "DELETE /api/cache/clear - очистить весь кеш",
                    "DELETE /api/cache/clear?pattern=* - очистить по паттерну",
                    "GET /api/cache/exists?key=* - проверить существование ключа"
                }
            ));
        } catch (Exception e) {
            log.error("Error getting cache info", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cache info: " + e.getMessage()));
        }
    }
}


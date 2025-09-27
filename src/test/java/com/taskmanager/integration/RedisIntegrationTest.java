package com.taskmanager.integration;

import com.taskmanager.BaseIntegrationTest;
import com.taskmanager.service.cache.CacheService;
import com.taskmanager.service.cache.CacheKeyGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RedisIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyGenerator cacheKeyGenerator;

    @Test
    void shouldStoreAndRetrieveValue() {
        String key = "test:key";
        String value = "test value";

        cacheService.put(key, value);
        Optional<String> result = cacheService.get(key, String.class);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(value);
    }

    @Test
    void shouldStoreAndRetrieveList() {
        String key = "test:list";
        List<String> value = List.of("item1", "item2", "item3");

        cacheService.put(key, value);
        Optional<List<String>> result = cacheService.getList(key, String.class);

        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(3);
        assertThat(result.get()).containsExactly("item1", "item2", "item3");
    }

    @Test
    void shouldStoreWithTTL() {
        String key = "test:ttl";
        String value = "test value";
        Integer ttlSeconds = 1;

        cacheService.put(key, value, ttlSeconds);
        
        Optional<String> result1 = cacheService.get(key, String.class);
        assertThat(result1).isPresent();
        assertThat(result1.get()).isEqualTo(value);

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Optional<String> result2 = cacheService.get(key, String.class);
        assertThat(result2).isEmpty();
    }

    @Test
    void shouldEvictKey() {
        String key = "test:evict";
        String value = "test value";
        cacheService.put(key, value);
    
        assertThat(cacheService.exists(key)).isTrue();

        cacheService.evict(key);

        assertThat(cacheService.exists(key)).isFalse();
        assertThat(cacheService.get(key, String.class)).isEmpty();
    }

    @Test
    void shouldEvictByPattern() {
        String key1 = "test:pattern:key1";
        String key2 = "test:pattern:key2";
        String key3 = "test:other:key3";
        String value = "test value";

        cacheService.put(key1, value);
        cacheService.put(key2, value);
        cacheService.put(key3, value);

        assertThat(cacheService.exists(key1)).isTrue();
        assertThat(cacheService.exists(key2)).isTrue();
        assertThat(cacheService.exists(key3)).isTrue();

        cacheService.evictByPattern("test:pattern:*");

        assertThat(cacheService.exists(key1)).isFalse();
        assertThat(cacheService.exists(key2)).isFalse();
        assertThat(cacheService.exists(key3)).isTrue(); 
    }

    @Test
    void shouldCheckKeyExistence() {
        String existingKey = "test:exists";
        String nonExistingKey = "test:notexists";
        String value = "test value";

        cacheService.put(existingKey, value);

        assertThat(cacheService.exists(existingKey)).isTrue();
        assertThat(cacheService.exists(nonExistingKey)).isFalse();
    }

    @Test
    void shouldClearAllCache() {
        String key1 = "test:clear:key1";
        String key2 = "test:clear:key2";
        String value = "test value";

        cacheService.put(key1, value);
        cacheService.put(key2, value);

        assertThat(cacheService.exists(key1)).isTrue();
        assertThat(cacheService.exists(key2)).isTrue();

        cacheService.clear();

        assertThat(cacheService.exists(key1)).isFalse();
        assertThat(cacheService.exists(key2)).isFalse();
    }

    @Test
    void shouldGetCacheStats() {
        String key1 = "test:stats:key1";
        String key2 = "test:stats:key2";
        String value = "test value";

        cacheService.put(key1, value);
        cacheService.put(key2, value);

        Map<String, Object> stats = cacheService.getStats();

        assertThat(stats).isNotNull();
        assertThat(stats).containsKey("total_keys");
        assertThat(stats.get("total_keys")).isInstanceOf(Integer.class);
        assertThat((Integer) stats.get("total_keys")).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldGenerateCorrectCacheKeys() {
        String userId = "user123";
        String taskId = "task456";
        String notificationId = "notif789";

        String userKey = cacheKeyGenerator.generateUserKey(userId);
        String taskKey = cacheKeyGenerator.generateTaskKey(taskId);
        String notificationKey = cacheKeyGenerator.generateNotificationKey(notificationId);
        String tasksByUserKey = cacheKeyGenerator.generateTasksByUserKey(userId);
        String notificationsByUserKey = cacheKeyGenerator.generateNotificationsByUserKey(userId);

        assertThat(userKey).isEqualTo("user:user123");
        assertThat(taskKey).isEqualTo("task:task456");
        assertThat(notificationKey).isEqualTo("notification:notif789");
        assertThat(tasksByUserKey).isEqualTo("tasks:user:user123");
        assertThat(notificationsByUserKey).isEqualTo("notifications:user:user123");
    }

    @Test
    void shouldGenerateCorrectPatterns() {
        String userId = "user123";

        String userPattern = cacheKeyGenerator.generateUserPattern(userId);
        String tasksPattern = cacheKeyGenerator.generateTasksPattern(userId);
        String notificationsPattern = cacheKeyGenerator.generateNotificationsPattern(userId);

        assertThat(userPattern).isEqualTo("*:user:user123*");
        assertThat(tasksPattern).isEqualTo("tasks:user:user123*");
        assertThat(notificationsPattern).isEqualTo("notifications:user:user123*");
    }
}


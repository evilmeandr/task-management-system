package com.taskmanager.service.cache;

import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator {
    
    public static final String TASK_PREFIX = "task";
    public static final String NOTIFICATION_PREFIX = "notification";
    public static final String USER_PREFIX = "user";
    public static final String TASKS_BY_USER_PREFIX = "tasks:user";
    public static final String NOTIFICATIONS_BY_USER_PREFIX = "notifications:user";
    public static final String PENDING_TASKS_BY_USER_PREFIX = "pending:tasks:user";
    public static final String PENDING_NOTIFICATIONS_BY_USER_PREFIX = "pending:notifications:user";
    
    public String generateTaskKey(String taskId) {
        return TASK_PREFIX + ":" + taskId;
    }
    
    public String generateNotificationKey(String notificationId) {
        return NOTIFICATION_PREFIX + ":" + notificationId;
    }
    
    public String generateUserKey(String userId) {
        return USER_PREFIX + ":" + userId;
    }
    
    public String generateTasksByUserKey(String userId) {
        return TASKS_BY_USER_PREFIX + ":" + userId;
    }
    
    public String generateNotificationsByUserKey(String userId) {
        return NOTIFICATIONS_BY_USER_PREFIX + ":" + userId;
    }
    
    public String generatePendingTasksByUserKey(String userId) {
        return PENDING_TASKS_BY_USER_PREFIX + ":" + userId;
    }
    
    public String generatePendingNotificationsByUserKey(String userId) {
        return PENDING_NOTIFICATIONS_BY_USER_PREFIX + ":" + userId;
    }
    
    public String generateUserPattern(String userId) {
        return "*:user:" + userId + "*";
    }
    
    public String generateTasksPattern(String userId) {
        return TASKS_BY_USER_PREFIX + ":" + userId + "*";
    }

    public String generateNotificationsPattern(String userId) {
        return NOTIFICATIONS_BY_USER_PREFIX + ":" + userId + "*";
    }
}


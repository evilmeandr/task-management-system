package com.taskmanager.controller;

import com.taskmanager.service.NotificationService;
import com.taskmanager.model.Notification;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam String userId) {
        List<Notification> notifications = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications(@RequestParam String userId) {
        List<Notification> notifications = notificationService.getPendingNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}

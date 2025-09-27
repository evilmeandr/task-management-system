package com.taskmanager.service.scheduling.impl;

import com.taskmanager.service.scheduling.OverdueTaskChecker;
import com.taskmanager.service.messaging.MessageProducer;
import com.taskmanager.service.messaging.TaskOverdueEvent;
import com.taskmanager.storage.TaskStorage;
import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("scheduler")
public class ScheduledOverdueChecker implements OverdueTaskChecker {
    
    private final TaskStorage taskStorage;
    private final MessageProducer messageProducer;
    
    @Scheduled(fixedRate = 300000)
    public void checkOverdueTasks() {
        try {
            log.debug("Starting overdue tasks check");
            
            List<TaskEntity> allTaskEntities = taskStorage.findAllPendingTasks();
            
            LocalDateTime now = LocalDateTime.now();
            int overdueCount = 0;
            
            log.debug("Checked {} pending tasks for overdue status", allTaskEntities.size());
            
            if (allTaskEntities.isEmpty()) {
                log.debug("No pending tasks found to check");
                return;
            }
            
            for (TaskEntity taskEntity : allTaskEntities) {
                if (taskEntity.getTargetDate().isBefore(now)) {
                    TaskOverdueEvent event = TaskOverdueEvent.builder()
                        .userId(taskEntity.getUserId().toString())
                        .taskId(taskEntity.getId().toString())
                        .taskTitle(taskEntity.getTitle())
                        .build();
                    
                    messageProducer.sendTaskOverdueEvent(event);
                    overdueCount++;
                    log.debug("Sent overdue event for task: {}", taskEntity.getId());
                }
            }
            
            log.info("Overdue check completed. Found {} overdue tasks", overdueCount);
            
        } catch (Exception e) {
            log.error("Error during overdue tasks check", e);
        }
    }
}

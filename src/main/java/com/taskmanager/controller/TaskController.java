package com.taskmanager.controller;

import com.taskmanager.service.TaskService;
import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Task;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestParam String userId,
            @Valid @RequestBody TaskCreateDto dto) {
        Task task = taskService.createTask(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam String userId) {
        List<Task> tasks = taskService.getAllTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<Task>> getPendingTasks(@RequestParam String userId) {
        List<Task> tasks = taskService.getPendingTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
    
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}

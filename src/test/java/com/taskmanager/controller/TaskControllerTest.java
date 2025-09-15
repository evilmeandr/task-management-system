package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.TaskCreateDto;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask_shouldReturnCreated() throws Exception {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Test Task");
        dto.setTargetDate(LocalDateTime.now().plusDays(1));
        Task task = Task.builder().id("1").title("Test Task").build();
        when(taskService.createTask(anyString(), any(TaskCreateDto.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .param("userId", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService).createTask("user123", dto);
    }

    @Test
    void getAllTasks_shouldReturnOk() throws Exception {
        Task task = Task.builder().id("1").title("Task").build();
        when(taskService.getAllTasksByUserId("user123")).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task"));
    }

    @Test
    void getPendingTasks_shouldReturnOk() throws Exception {
        Task task = Task.builder().id("1").title("Pending").status(TaskStatus.PENDING).build();
        when(taskService.getPendingTasksByUserId("user123")).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/pending")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Pending"));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/task123"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask("task123");
    }
}

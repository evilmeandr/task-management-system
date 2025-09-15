package com.taskmanager.controller;

import com.taskmanager.model.Notification;
import com.taskmanager.model.NotificationType;
import com.taskmanager.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void getAllNotifications_shouldReturnOk() throws Exception {
        Notification notif = Notification.builder().id("1").message("Test").type(NotificationType.TASK_CREATED).build();
        when(notificationService.getAllNotificationsByUserId("user123")).thenReturn(List.of(notif));

        mockMvc.perform(get("/api/notifications")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test"));
    }

    @Test
    void getPendingNotifications_shouldReturnOk() throws Exception {
        Notification notif = Notification.builder().id("1").message("Unread").read(false).build();
        when(notificationService.getPendingNotificationsByUserId("user123")).thenReturn(List.of(notif));

        mockMvc.perform(get("/api/notifications/pending")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Unread"));
    }
}

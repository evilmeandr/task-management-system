package com.taskmanager.service.messaging;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class TaskCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String taskTitle;
    private String taskDescription;
}



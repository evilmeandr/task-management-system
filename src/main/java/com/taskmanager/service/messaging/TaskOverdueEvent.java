package com.taskmanager.service.messaging;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class TaskOverdueEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String taskId;
    private String taskTitle;
}

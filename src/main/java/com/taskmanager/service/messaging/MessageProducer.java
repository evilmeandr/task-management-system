package com.taskmanager.service.messaging;

public interface MessageProducer {
    void sendTaskCreatedEvent(TaskCreatedEvent event);
}



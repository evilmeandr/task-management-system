package com.taskmanager.service.messaging;

public interface MessageConsumer {
    void consumeTaskCreatedEvent(TaskCreatedEvent event);
    void consumeTaskOverdueEvent(TaskOverdueEvent event);
}


    
package com.taskmanager.entity;

import com.taskmanager.model.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "creation_date", nullable = false)
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "target_date", nullable = false)
    private LocalDateTime targetDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;
}



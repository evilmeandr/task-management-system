package com.taskmanager.repository;

import com.taskmanager.entity.TaskEntity;
import com.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findByUserIdAndDeletedFalse(UUID userId);

    @Query("SELECT t FROM TaskEntity t WHERE t.userId = :userId AND t.deleted = false AND t.status = :status")
    List<TaskEntity> findPendingByUserIdAndDeletedFalse(@Param("userId") UUID userId, @Param("status") TaskStatus status);

    @Modifying
    @Query("UPDATE TaskEntity t SET t.deleted = true WHERE t.id = :id")
    void markAsDeleted(@Param("id") UUID id);

    List<TaskEntity> findByStatusAndDeletedFalse(TaskStatus status);
}



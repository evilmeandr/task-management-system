package com.taskmanager.repository;

import com.taskmanager.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    List<NotificationEntity> findByUserId(UUID userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.userId = :userId AND n.read = false")
    List<NotificationEntity> findPendingByUserId(@Param("userId") UUID userId);
}



package com.todo.notificationservice.model;

import com.todo.notificationservice.enums.NotificationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {
    @Id
    private String id;
    @NonNull
    private Long userId;  // FK to Receiver
    @NonNull
    private String title;
    @NonNull
    private String message;
    @NonNull
    private NotificationType type;
    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;


}
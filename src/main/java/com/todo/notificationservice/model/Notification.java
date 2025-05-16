package com.todo.notificationservice.model;

import com.todo.notificationservice.enums.NotificationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "notifications")
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
    @Field("created_at")
    private LocalDateTime createdAt;

}
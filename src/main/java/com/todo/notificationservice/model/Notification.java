package com.todo.notificationservice.model;

import com.todo.notificationservice.enums.NotificationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.CreatedDate;
import lombok.*;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {

    @Id
    private String id;

    @NonNull
    @Field("userId")
    private Long userId;  // FK to Receiver

    @NonNull
    @Field("title")
    private String title;

    @NonNull
    @Field("message")
    private String message;

    @NonNull
    @Field("type")
    private NotificationType type;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    public Notification(Long userId, String title, String message, NotificationType type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
    }


}

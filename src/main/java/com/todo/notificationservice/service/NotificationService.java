package com.todo.notificationservice.service;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.repo.NotificationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByEmail(String email) {
        return notificationRepository.findByEmail(email);
    }

    public Notification updateNotification(String notificationId, Notification updatedNotification) {
        Notification existingNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        existingNotification.setTitle(updatedNotification.getTitle());
        existingNotification.setMessage(updatedNotification.getMessage());
        return notificationRepository.save(existingNotification);
    }

    public void deleteNotification(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}

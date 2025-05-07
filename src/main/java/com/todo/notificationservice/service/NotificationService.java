package com.todo.notificationservice.service;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.repo.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification create(Notification notification){
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification update(String id,Notification updatedNotification){
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        existing.setTitle(updatedNotification.getTitle());
        existing.setMessage(updatedNotification.getMessage());
        existing.setType(updatedNotification.getType());
        return notificationRepository.save(existing);
    }

    public void delete(String id){
        notificationRepository.deleteById(id);
    }

    public List<Notification> getDueWithinWeek() {
        LocalDateTime cutoff = LocalDateTime.now().plusDays(7);
        return notificationRepository.findByCreatedAtBefore(cutoff);
    }
}

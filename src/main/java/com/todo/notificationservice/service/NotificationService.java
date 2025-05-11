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
        return notificationRepository.save(existing);
    }

    public void delete(String id){
        notificationRepository.deleteById(id);
    }


}

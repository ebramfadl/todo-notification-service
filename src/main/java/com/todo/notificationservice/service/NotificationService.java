package com.todo.notificationservice.service;

import com.todo.notificationservice.repo.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
@Data
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveUserNotification(String message) {
        emailSenderService.sendEmail("markmahrous012@gmail.com",
                                     "User Notification",
                                     message);
        System.out.println("Received user notification: " + message);
    }
}

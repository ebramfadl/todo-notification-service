package com.todo.notificationservice.observer;

import com.todo.notificationservice.dto.EmailMessage;
import com.todo.notificationservice.service.EmailSenderService;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserObserver {

    private final EmailSenderService emailSenderService;

    @Autowired
    public UserObserver(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void onUserCreated(EmailMessage emailMessage) {
        emailSenderService.sendEmail(
                emailMessage.getToEmail(),
                emailMessage.getSubject(),
                emailMessage.getBody()
        );
        System.out.println("UserObserver: Received user notification: " + emailMessage);
    }
}

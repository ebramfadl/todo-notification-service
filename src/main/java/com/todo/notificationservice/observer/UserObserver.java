package com.todo.notificationservice.observer;

import com.todo.notificationservice.dto.EmailMessage;
import com.todo.notificationservice.service.EmailSenderService;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserObserver implements Observer{

    private final EmailSenderService emailSenderService;

    @Autowired
    public UserObserver(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void update(EmailMessage emailMessage) {
        emailSenderService.sendEmail(
                emailMessage.getToEmail(),
                emailMessage.getSubject(),
                emailMessage.getBody()
        );
        System.out.println("UserObserver: Updated with new message: " + emailMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void onUserCreated(EmailMessage emailMessage) {
        update(emailMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.BOARD_QUEUE)
    public void onUserAddedToBoard(EmailMessage emailMessage) {
        update(emailMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.TASK_QUEUE)
    public void onTaskDeadline(EmailMessage emailMessage) {
        emailSenderService.sendReminderEmail(
                emailMessage.getToEmail(),
                emailMessage.getSubject(),
                emailMessage.getBody()
        );
    }
}

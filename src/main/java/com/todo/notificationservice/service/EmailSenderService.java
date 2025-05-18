package com.todo.notificationservice.service;

import com.todo.notificationservice.enums.NotificationType;
import com.todo.notificationservice.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final NotificationService notificationService;

    @Autowired
    public EmailSenderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        JavaMailSender javaMailSender = MailSenderSingleton.getInstance().getMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);

        Notification notification = new Notification();
        notification.setUserId(1L);
        notification.setTitle(subject);
        notification.setMessage(body);
        notification.setType(NotificationType.INFO);
        notificationService.createNotification(notification);

        System.out.println("Email sent successfully to " + toEmail);
    }
}

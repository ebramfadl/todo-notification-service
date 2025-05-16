package com.todo.notificationservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    public void sendEmail(String toEmail, String subject, String body) {
        JavaMailSender javaMailSender = MailSenderSingleton.getInstance().getMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
        System.out.println("Email sent successfully to " + toEmail);
    }
}

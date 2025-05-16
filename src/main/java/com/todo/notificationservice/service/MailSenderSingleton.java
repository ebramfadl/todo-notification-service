package com.todo.notificationservice.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class MailSenderSingleton {

    private static MailSenderSingleton instance;
    private final JavaMailSender mailSender;

    private MailSenderSingleton() {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost("smtp.gmail.com");
        senderImpl.setPort(587);
        senderImpl.setUsername("todoappnotificationsystem@gmail.com");
        senderImpl.setPassword("lwim uqoc cigd prrp");

        Properties props = senderImpl.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        this.mailSender = senderImpl;
    }

    public static synchronized MailSenderSingleton getInstance() {
        if (instance == null) {
            instance = new MailSenderSingleton();
        }
        return instance;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }
}

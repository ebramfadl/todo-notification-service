package com.todo.notificationservice.service;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig; // Your RabbitMQ constants
// import com.todo.notificationservice.service.EmailService; // Example: if you have an email service
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AlertConsumer {

    private static final Logger log = LoggerFactory.getLogger(AlertConsumer.class);



    @RabbitListener(queues = RabbitMQConfig.DEADLINE_ALERT_QUEUE)
    public void handleDeadlineAlert(@Payload Notification notification) {
        log.info("Consumer: Received deadline alert from RabbitMQ for Notification ID: {}, User ID: {}, Title: '{}'",
                notification.getId(), notification.getUserId(), notification.getTitle());

        try {


            log.info("Consumer: SIMULATING SENDING ALERT -> User: {}, Task: {}, Message: {}",
                    notification.getUserId(),
                    notification.getTitle(),
                    notification.getMessage());


            log.info("Consumer: Successfully processed (simulated sending) alert for Notification ID: {}.", notification.getId());

        } catch (Exception e) {
            log.error("Consumer: Error processing deadline alert for Notification ID {}: {}",
                    notification.getId(), e.getMessage(), e);

        }
    }
}
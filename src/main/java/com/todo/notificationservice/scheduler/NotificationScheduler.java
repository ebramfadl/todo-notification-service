package com.todo.notificationservice.scheduler;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig; // Your RabbitMQ constants
import com.todo.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {

    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationScheduler(NotificationService notificationService, RabbitTemplate rabbitTemplate) {
        this.notificationService = notificationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Scheduled task to find notifications for tasks with upcoming deadlines
     * and publish them to RabbitMQ for further processing (e.g., sending actual alerts).
     * Runs daily at 8:00 AM by default.
     */
    @Scheduled(cron = "0 0 8 * * *") // Daily at 8:00 AM server time
    // For testing:
    // @Scheduled(cron = "0 * * * * ?") // Every minute
    public void publishUpcomingDeadlineAlerts() {
        log.info("Scheduler: Checking for upcoming task deadlines to publish alerts.");

        List<Notification> upcomingDeadlineNotifications;
        try {
            upcomingDeadlineNotifications = notificationService.getAlertsForUpcomingDeadlines();
        } catch (Exception e) {
            log.error("Scheduler: Error fetching upcoming deadline alerts from service: {}", e.getMessage(), e);
            return;
        }

        if (upcomingDeadlineNotifications.isEmpty()) {
            log.info("Scheduler: No upcoming task deadlines found to publish for alerts.");
            return;
        }

        log.info("Scheduler: Found {} notifications for upcoming task deadlines. Publishing to RabbitMQ...", upcomingDeadlineNotifications.size());
        int publishedCount = 0;
        for (Notification notification : upcomingDeadlineNotifications) {
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.DEADLINE_ALERT_EXCHANGE,
                        RabbitMQConfig.DEADLINE_ALERT_ROUTING_KEY,
                        notification
                );
                log.debug("Scheduler: Successfully published alert for notification ID {} (User ID: {}) to RabbitMQ.",
                        notification.getId(), notification.getUserId());
                publishedCount++;

                // CONSIDERATION: Idempotency. How do you prevent re-publishing every day?
                // Option A: Update notification status here (e.g., alertPublished = true)
                // Option B: Consumer handles idempotency and updates status.
                // This example doesn't implement it for simplicity.

            } catch (Exception e) {
                log.error("Scheduler: Failed to publish alert for notification ID {} (User ID: {}) to RabbitMQ: {}",
                        notification.getId(), notification.getUserId(), e.getMessage(), e);
            }
        }
        log.info("Scheduler: Finished publishing {} out of {} found upcoming task deadline alerts to RabbitMQ.", publishedCount, upcomingDeadlineNotifications.size());
    }
}
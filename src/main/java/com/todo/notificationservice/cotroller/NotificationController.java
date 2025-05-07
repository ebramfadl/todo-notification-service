package com.todo.notificationservice.cotroller;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.rabbitmq.RabbitMQConfig;
import com.todo.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/notification")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate; // <<<< DECLARE RabbitTemplate field

    @Autowired
    public NotificationController(NotificationService notificationService, RabbitTemplate rabbitTemplate) { // <<<< ADD RabbitTemplate to constructor
        this.notificationService = notificationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification n){
        log.info("Creating notification: {}", n.getTitle());
        // Assuming your service might throw an exception or return null on failure
        Notification createdNotification = notificationService.create(n);
        if (createdNotification == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@PathVariable Long userId){
        log.info("Fetching all notifications for user ID: {}", userId);
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable String id, // Renamed method
                                                           @RequestBody Notification n) {
        log.info("Updating notification with ID: {}", id);
        try {
            Notification updatedNotification = notificationService.update(id, n);
            return ResponseEntity.ok(updatedNotification);
        } catch (NoSuchElementException e) { // Assuming service throws this for not found
            log.warn("Update failed: Notification with ID {} not found. Details: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating notification with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) { // Renamed method
        log.info("Deleting notification with ID: {}", id);
        try {
            notificationService.delete(id);
            return ResponseEntity.ok("Notification with ID " + id + " has been deleted successfully.");
        } catch (NoSuchElementException e) { // Assuming service throws this for not found
            log.warn("Delete failed: Notification with ID {} not found. Details: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting notification with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/alerts/created-within-future-week")
    public ResponseEntity<List<Notification>> getNotificationsCreatedWithinFutureWeek() {
        log.info("Request received for /alerts/created-within-future-week");
        List<Notification> notifications;
        try {
            notifications = notificationService.getAlertsForUpcomingDeadlines(); // Ensure this method has the createdAt logic
        } catch (Exception e) {
            log.error("Error fetching notifications for /alerts/created-within-future-week: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }

        if (notifications.isEmpty()) {
            log.info("No notifications found for /alerts/created-within-future-week.");
            return ResponseEntity.noContent().build();
        }

        log.info("Found {} notifications for /alerts/created-within-future-week.", notifications.size());
        return ResponseEntity.ok(notifications);
    }


    @PostMapping("/alerts/publish-upcoming-deadlines")
    public ResponseEntity<String> triggerPublishUpcomingDeadlineAlerts() {
        log.info("Controller: Manual trigger received to publish upcoming deadline alerts.");
        List<Notification> upcomingDeadlineNotifications;
        try {

            upcomingDeadlineNotifications = notificationService.getAlertsForUpcomingDeadlines(); // Ensure this method uses dueDate for this endpoint's purpose
        } catch (Exception e) {
            log.error("Controller: Error fetching upcoming deadline alerts for publishing: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching alerts for publishing: " + e.getMessage());
        }

        if (upcomingDeadlineNotifications.isEmpty()) {
            log.info("Controller: No upcoming task deadlines found to publish.");
            return ResponseEntity.ok("No upcoming task deadlines found to publish.");
        }

        log.info("Controller: Found {} notifications. Publishing to RabbitMQ...", upcomingDeadlineNotifications.size());
        int publishedCount = 0;
        for (Notification notification : upcomingDeadlineNotifications) {
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.DEADLINE_ALERT_EXCHANGE,
                        RabbitMQConfig.DEADLINE_ALERT_ROUTING_KEY,
                        notification
                );
                publishedCount++;
                log.debug("Controller: Published alert for notification ID {} to RabbitMQ.", notification.getId());
            } catch (Exception e) {
                log.error("Controller: Failed to publish alert for notification ID {} to RabbitMQ: {}",
                        notification.getId(), e.getMessage(), e);
            }
        }
        String responseMessage = String.format("Successfully published %d out of %d found upcoming deadline alerts to RabbitMQ.",
                publishedCount, upcomingDeadlineNotifications.size());
        log.info("Controller: " + responseMessage);
        return ResponseEntity.ok(responseMessage);
    }
}
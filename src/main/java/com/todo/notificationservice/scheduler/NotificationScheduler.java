package com.todo.notificationservice.scheduler;

import com.todo.notificationservice.model.Notification;
import com.todo.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class NotificationScheduler {

    private final NotificationService service;

    @Autowired
    public NotificationScheduler(NotificationService service) {
        this.service = service;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyAlerts() {
        List<Notification> due = service.getDueWithinWeek();
        due.forEach(n ->
                // Replace with real email/push logic or event emission
                System.out.printf("Reminder for user %d: %s – %s%n",
                        n.getUserId(), n.getTitle(), n.getMessage())
        );
    }
}

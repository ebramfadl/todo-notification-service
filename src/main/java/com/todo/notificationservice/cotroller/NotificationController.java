package com.todo.notificationservice.cotroller;

import com.todo.notificationservice.model.Notification;
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


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService service) {
        this.notificationService = service;
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification n) {
        return ResponseEntity.ok(notificationService.create(n));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable String id,
                                               @RequestBody Notification n) {
        return ResponseEntity.ok(notificationService.update(id, n));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
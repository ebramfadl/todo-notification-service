package com.todo.notificationservice.observer;

import com.todo.notificationservice.dto.EmailMessage;

public interface Observer {
    void update(EmailMessage emailMessage);
}

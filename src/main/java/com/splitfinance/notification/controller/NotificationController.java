package com.splitfinance.notification.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.splitfinance.notification.service.NotificationMessage;

@Controller
public class NotificationController {

    // When a message is sent to the /app/notify mapping, broadcast it to /topic/notifications
    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationMessage sendNotification(NotificationMessage message) throws Exception {
        // Add any business logic here (for example, logging, enrich message, etc.)
        return message;
    }
}

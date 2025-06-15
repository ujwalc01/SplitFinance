package com.splitfinance.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.splitfinance.notification.service.NotificationMessage;
import com.splitfinance.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;
    
    @Autowired
    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    // REST endpoint to trigger a notification
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage message) {
        notificationService.sendNotification(message.getTitle(), message.getContent());
        return ResponseEntity.ok("Notification sent!");
    }
}

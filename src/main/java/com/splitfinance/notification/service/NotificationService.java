package com.splitfinance.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public void sendNotification(String title, String content) {
        NotificationMessage message = new NotificationMessage(title, content);
        // Send message to all subscribers on /topic/notifications
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}

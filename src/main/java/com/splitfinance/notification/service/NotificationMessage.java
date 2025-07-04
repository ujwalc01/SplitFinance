package com.splitfinance.notification.service;

public class NotificationMessage {
    private String title;
    private String content;

    public NotificationMessage() {}

    public NotificationMessage(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    // Getters and setters
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}

package com.splitfinance.audit.service;

public interface AuditLogService {
    void logAction(String username, String operation, String resource, String details);
}

package com.splitfinance.audit.service;

import com.splitfinance.audit.entity.AuditLog;
import com.splitfinance.audit.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    @Override
    public void logAction(String username, String operation, String resource, String details) {
        AuditLog log = new AuditLog();
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setOperation(operation);
        log.setResource(resource);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}

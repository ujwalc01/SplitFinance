package com.splitfinance.audit.aspect;

import com.splitfinance.audit.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogService auditLogService;

    public AuditAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    
    // Intercepts methods annotated with @Audit after they return successfully.
    @AfterReturning("@annotation(auditAnnotation)")
    public void logAfter(JoinPoint joinPoint, Audit auditAnnotation) {
        // Get the username from the security context. Adapt if you're not using Spring Security.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String operation = auditAnnotation.operation();
        String resource = auditAnnotation.resource();
        String details = auditAnnotation.details();
        
        // Optionally, include the method name and its arguments if no details provided.
        if(details.isEmpty()) {
            details = "Method " + joinPoint.getSignature().getName() +
                    " called with args " + Arrays.toString(joinPoint.getArgs());
        }
        
        auditLogService.logAction(username, operation, resource, details);
    }
}

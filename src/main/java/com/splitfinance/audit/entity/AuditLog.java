package com.splitfinance.audit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime timestamp;
    
    // The username of the user performing the operation.
    private String username;
    
    // Type of operation e.g., CREATE, UPDATE, DELETE, LOGIN, etc.
    private String operation;
    
    // The resource being affected (e.g., Expense, User).
    private String resource;
    
    // Optional details about the operation.
    @Column(length = 1000)
    private String details;
}

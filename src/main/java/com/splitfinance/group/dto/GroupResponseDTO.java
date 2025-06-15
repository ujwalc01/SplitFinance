package com.splitfinance.group.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class GroupResponseDTO {
    private Long id;
    private String groupName;
    private String description;
    private LocalDateTime createdAt;
    private String createdBy;
    // Returns the usernames of all members in the group.
    private Set<String> memberUsernames;
}

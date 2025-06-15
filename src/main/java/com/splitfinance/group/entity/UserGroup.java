package com.splitfinance.group.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.splitfinance.auth.entity.User;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_groups")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    // The username of the administrator (creator) of the group
    private String createdBy;

    // Many-to-many mapping with users; assumes User entity exists.
    @ManyToMany
    @JoinTable(
         name = "group_members",
         joinColumns = @JoinColumn(name = "group_id"),
         inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();
}

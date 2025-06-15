package com.splitfinance.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitfinance.group.entity.UserGroup;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    // Find groups by the admin (creator)
    List<UserGroup> findByCreatedBy(String createdBy);
    
    // Optionally, add custom finder methods if you wish to retrieve groups where a user is a member.
}

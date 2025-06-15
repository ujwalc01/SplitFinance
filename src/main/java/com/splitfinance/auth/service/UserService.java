package com.splitfinance.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.splitfinance.auth.dto.UserRegistrationDTO;
import com.splitfinance.auth.entity.User;

public interface UserService extends UserDetailsService {
    User registerUser(UserRegistrationDTO registrationDTO);
    User findByUsername(String username);
}

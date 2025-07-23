package com.example.bankcards.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        // You may need to cast principal to your custom user details class if needed
        // For now, assume it has a getId() method
        try {
            return (Long) principal.getClass().getMethod("getId").invoke(principal);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot extract user ID from principal", e);
        }
    }
} 
package com.example.bankcards.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Implements custom user details service
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Basic constructor
     */
    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {

        return null;
    }
}
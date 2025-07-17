package com.example.bankcards.config;

import com.example.bankcards.security.JWTTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for the security
 */
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JWTTokenService jwtTokenService;

    /**
     * Basic constructor
     *
     * @param userDetailsService - custom user details service
     * @param jwtTokenService       - service for creating and processing with JWT tokens
     */
    public SecurityConfig(final UserDetailsService userDetailsService,
                          final JWTTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Password encoder bean
     *
     * @return BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean
     *
     * @param authConfig - authentication configuration
     * @return authentication manager
     * @throws Exception if it isn't possible to get authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Authentication provider bean
     *
     * @return authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Security filter chain bean
     *
     * @param http - http security object
     * @return security filter chain object
     * @throws Exception if it isn't possible to get security filter chain object
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()  // Доступ к Swagger без авторизации
                        .anyRequest().authenticated()  // Для всех остальных запросов требуется аутентификация
        ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
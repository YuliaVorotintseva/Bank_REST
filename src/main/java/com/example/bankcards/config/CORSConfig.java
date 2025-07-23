package com.example.bankcards.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for CORS
 */
@Configuration
public class CORSConfig implements WebMvcConfigurer {
    /**
     * Configuration for CORS
     *
     * @param registry - registry for CORS
     */
    @Override
    public void addCorsMappings(@NonNull final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
package com.backendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for Java Backend API System
 * 
 * This is a comprehensive Spring Boot application that provides:
 * - RESTful API endpoints
 * - JWT-based authentication and authorization
 * - MySQL database integration
 * - Comprehensive testing suite
 * - API documentation with Swagger/OpenAPI
 */
@SpringBootApplication
@EnableJpaAuditing
public class JavaBackendApiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaBackendApiSystemApplication.class, args);
    }
}

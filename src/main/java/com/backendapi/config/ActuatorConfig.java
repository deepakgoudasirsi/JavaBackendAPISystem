package com.backendapi.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Actuator Configuration
 * 
 * Configures Spring Boot Actuator endpoints for monitoring and management
 * Provides custom health checks and application information
 */
@Configuration
public class ActuatorConfig {

    /**
     * Custom health indicator for application health checks
     * @return HealthIndicator
     */
    @Bean
    public HealthIndicator customHealthIndicator() {
        return new HealthIndicator() {
            @Override
            public Health health() {
                // Add custom health checks here
                boolean isHealthy = checkDatabaseConnection() && checkExternalServices();
                
                if (isHealthy) {
                    return Health.up()
                            .withDetail("database", "Connected")
                            .withDetail("external-services", "Available")
                            .withDetail("timestamp", LocalDateTime.now())
                            .build();
                } else {
                    return Health.down()
                            .withDetail("database", "Disconnected")
                            .withDetail("external-services", "Unavailable")
                            .withDetail("timestamp", LocalDateTime.now())
                            .build();
                }
            }
            
            private boolean checkDatabaseConnection() {
                // Implement database connection check
                return true; // Simplified for demo
            }
            
            private boolean checkExternalServices() {
                // Implement external service checks
                return true; // Simplified for demo
            }
        };
    }

    /**
     * Custom info contributor for application information
     * @return InfoContributor
     */
    @Bean
    public InfoContributor customInfoContributor() {
        return new InfoContributor() {
            @Override
            public void contribute(Info.Builder builder) {
                Map<String, Object> details = new HashMap<>();
                details.put("name", "Java Backend API System");
                details.put("description", "Comprehensive Java backend system using Spring Boot");
                details.put("version", "1.0.0");
                details.put("build-time", LocalDateTime.now());
                details.put("java-version", System.getProperty("java.version"));
                details.put("spring-boot-version", org.springframework.boot.SpringBootVersion.getVersion());
                
                builder.withDetail("application", details);
            }
        };
    }
}

package com.example.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing for createdAt and updatedAt.
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfig {
}
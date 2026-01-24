package com.aura.app.configs;

import com.aura.app.audit.AuditorAwareImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@Slf4j
public class AuditConfig {

    // INFO: could not set default_schema for envers in application properties and have to do it here instead
    @Bean
    HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> hibernateProperties.put("org.hibernate.envers.default_schema", "audit");
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}

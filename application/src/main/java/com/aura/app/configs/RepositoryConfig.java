package com.aura.app.configs;

import com.aura.core.repositories.InstitutionRepository;
import com.aura.details.repositories.impl.InstitutionRepositoryImpl;
import com.aura.details.repositories.jpa.institutions.JpaInstitutionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public InstitutionRepository getInstitutionRepository(JpaInstitutionRepository jpaRepository) {
        return new InstitutionRepositoryImpl(jpaRepository);
    }
}

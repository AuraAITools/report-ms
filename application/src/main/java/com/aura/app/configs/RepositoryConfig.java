package com.aura.app.configs;

import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.details.springdata.repositories.impl.InstitutionRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LevelRepositoryImpl;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    InstitutionRepository institutionRepository(JpaInstitutionRepository jpaRepository) {
        return new InstitutionRepositoryImpl(jpaRepository);
    }

    @Bean
    LevelRepository levelRepository(JpaLevelRepository jpaRepository) {
        return new LevelRepositoryImpl(jpaRepository);
    }
}

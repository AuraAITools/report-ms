package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.services.institutions.InstitutionQueryService;
import com.aura.core.services.institutions.InstitutionQueryServiceImpl;
import com.aura.core.services.institutions.OnboardingInstitutionService;
import com.aura.core.services.institutions.onboarding.LevelQueryService;
import com.aura.core.services.institutions.onboarding.TenantInitialDataLoader;
import com.aura.core.services.levels.LevelQueryServiceImpl;
import com.aura.details.institutions.onboarding.OnboardingServiceImpl;
import com.aura.details.institutions.onboarding.TenantInitialDataLoaderImpl;
import com.aura.details.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.repositories.jpa.levels.JpaLevelTemplateRepository;
import com.aura.details.repositories.jpa.links.JpaSubjectLevelLinkRepository;
import com.aura.details.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.repositories.jpa.schools.JpaSchoolTemplateRepository;
import com.aura.details.repositories.jpa.subjects.JpaSubjectLevelLinkTemplateRepository;
import com.aura.details.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.repositories.jpa.subjects.JpaSubjectTemplateRepository;
import com.aura.details.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.repositories.jpa.topics.JpaTopicSubjectLinkTemplateRepository;
import com.aura.details.repositories.jpa.topics.JpaTopicTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    TenantInitialDataLoader tenantInitialDataLoader(JpaSchoolTemplateRepository jpaSchoolTemplateRepository, JpaSchoolRepository jpaSchoolRepository, JpaLevelTemplateRepository jpaLevelTemplateRepository, JpaLevelRepository jpaLevelRepository, JpaSubjectTemplateRepository jpaSubjectTemplateRepository, JpaSubjectRepository jpaSubjectRepository, JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository, JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository, JpaTopicTemplateRepository jpaTopicTemplateRepository, JpaTopicRepository jpaTopicRepository, JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository, JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository) {
        return new TenantInitialDataLoaderImpl(jpaSchoolTemplateRepository, jpaSchoolRepository, jpaLevelTemplateRepository, jpaLevelRepository, jpaSubjectTemplateRepository, jpaSubjectRepository, jpaSubjectLevelLinkTemplateRepository, jpaSubjectLevelLinkRepository, jpaTopicTemplateRepository, jpaTopicRepository, jpaTopicSubjectLinkTemplateRepository, jpaTopicSubjectLinkRepository);
    }

    @Bean
    OnboardingInstitutionService onboardInstitutionService(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        return new OnboardingServiceImpl(institutionRepository, identityService, tenantInitialDataLoader);
    }

    @Bean
    InstitutionQueryService institutionQueryService(InstitutionRepository institutionRepository) {
        return new InstitutionQueryServiceImpl(institutionRepository);
    }


    @Bean
    LevelQueryService levelQueryService(LevelRepository levelRepository) {
        return new LevelQueryServiceImpl(levelRepository);
    }
}

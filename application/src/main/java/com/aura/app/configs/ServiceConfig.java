package com.aura.app.configs;

import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.usecases.institutions.InstitutionQueryUseCaseImpl;
import com.aura.core.usecases.levels.LevelQueryUseCase;
import com.aura.core.usecases.levels.LevelQueryUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.details.institutions.onboarding.TenantInitialDataLoaderImpl;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelTemplateRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLevelLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectLevelLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicSubjectLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicTemplateRepository;
import com.aura.details.services.OnboardingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    TenantInitialDataLoader tenantInitialDataLoader(JpaSchoolTemplateRepository jpaSchoolTemplateRepository, JpaSchoolRepository jpaSchoolRepository, JpaLevelTemplateRepository jpaLevelTemplateRepository, JpaLevelRepository jpaLevelRepository, JpaSubjectTemplateRepository jpaSubjectTemplateRepository, JpaSubjectRepository jpaSubjectRepository, JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository, JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository, JpaTopicTemplateRepository jpaTopicTemplateRepository, JpaTopicRepository jpaTopicRepository, JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository, JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository) {
        return new TenantInitialDataLoaderImpl(jpaSchoolTemplateRepository, jpaSchoolRepository, jpaLevelTemplateRepository, jpaLevelRepository, jpaSubjectTemplateRepository, jpaSubjectRepository, jpaSubjectLevelLinkTemplateRepository, jpaSubjectLevelLinkRepository, jpaTopicTemplateRepository, jpaTopicRepository, jpaTopicSubjectLinkTemplateRepository, jpaTopicSubjectLinkRepository);
    }

    @Bean
    OnboardingService onboardingService(OnboardInstitutionUseCase onboardInstitutionUseCase) {
        return new OnboardingService(onboardInstitutionUseCase);
    }

    @Bean
    InstitutionQueryUseCase institutionQueryService(InstitutionRepository institutionRepository) {
        return new InstitutionQueryUseCaseImpl(institutionRepository);
    }


    @Bean
    LevelQueryUseCase levelQueryService(LevelRepository levelRepository) {
        return new LevelQueryUseCaseImpl(levelRepository);
    }
}

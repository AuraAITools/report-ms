package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.usecases.onboarding.DefaultOnboardInstitutionUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    OnboardInstitutionUseCase onboardInstitutionUseCase(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        return new DefaultOnboardInstitutionUseCaseImpl(institutionRepository, identityService, tenantInitialDataLoader);
    }
}

package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCaseImpl;
import com.aura.core.usecases.onboarding.DefaultOnboardInstitutionUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.CreateOutletUseCaseImpl;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    OnboardInstitutionUseCase onboardInstitutionUseCase(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        return new DefaultOnboardInstitutionUseCaseImpl(institutionRepository, identityService, tenantInitialDataLoader);
    }

    @Bean
    CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository, IdentityService identityService) {
        return new CreateAccountUseCaseImpl(accountRepository, identityService);
    }

    @Bean
    CreateOutletUseCase createOutletUseCase(OutletRepository outletRepository) {
        return new CreateOutletUseCaseImpl(outletRepository);
    }

    @Bean
    OutletQueryUseCase outletQueryUseCase(OutletRepository outletRepository) {
        return new OutletQueryUseCaseImpl(outletRepository);
    }
}

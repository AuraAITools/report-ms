package com.aura.app.configs;

import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.details.springdata.repositories.impl.AccountRepositoryImpl;
import com.aura.details.springdata.repositories.impl.InstitutionRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LevelRepositoryImpl;
import com.aura.details.springdata.repositories.impl.OutletRepositoryImpl;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
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

    @Bean
    AccountRepository accountRepository(JpaAccountRepository jpaRepository) {
        return new AccountRepositoryImpl(jpaRepository);
    }

    @Bean
    OutletRepository outletRepository(JpaOutletRepository jpaRepository) {
        return new OutletRepositoryImpl(jpaRepository);
    }
}

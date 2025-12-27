package com.aura.app.configs;

import com.aura.core.auth.AuthorizationValidator;
import com.aura.core.auth.AuthorizationValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public AuthorizationValidator authorizationValidator() {
        return new AuthorizationValidatorImpl();
    }

}

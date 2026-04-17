package com.aura.app.configs;

import com.aura.app.interceptors.CorrelationIdInterceptor;
import com.aura.app.interceptors.KeycloakAuthorizationInterceptor;
import com.aura.app.interceptors.UserRequestContextInterceptor;
import com.aura.core.clients.PolicyEnforcementService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class InterceptorConfig {
    @Bean
    @Order(1)
    CorrelationIdInterceptor correlationIdInterceptor() {
        return new CorrelationIdInterceptor();
    }

    @Bean
    @Order(2)
    UserRequestContextInterceptor userRequestContextInterceptor() {
        return new UserRequestContextInterceptor();
    }

    @Bean
    @Order(3)
    KeycloakAuthorizationInterceptor keycloakAuthorizationInterceptor(
            PolicyEnforcementService policyEnforcementService,
            ApplicationContext applicationContext) {
        return new KeycloakAuthorizationInterceptor(policyEnforcementService, applicationContext);
    }
}

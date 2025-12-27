package com.aura.app.configs;

import com.aura.app.interceptors.CorrelationIdInterceptor;
import com.aura.app.interceptors.UserRequestContextInterceptor;
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
    UserRequestContextInterceptor correlationIdInterceptor2() {
        return new UserRequestContextInterceptor();
    }
}

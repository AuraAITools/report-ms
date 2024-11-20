package com.reportai.www.reportapi.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
public class OAuth2ResourceServerSecurityConfiguration {

    @Value("${oauth2.security.resource-server.jwk-set-uri}")
    @NotEmpty(message = "jwk url cannot be empty")
    public String jwksUrl;

    public OAuth2ResourceServerSecurityConfiguration() {
    }

    // TODO: add scopes and userinfo into security context
    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
                    httpSecurityOAuth2ResourceServerConfigurer.jwt(
                            jwtConfigurer -> jwtConfigurer
                                    .jwkSetUri(jwksUrl)
                    );
                })
                .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());

        return http.build();
    }


    @PostConstruct
    private void log() {
        log.info("jwks url: {}", jwksUrl);
    }
}

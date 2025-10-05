package com.aura.app.configs;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
public class WebSecurityConfig {

    @Value("${oauth2.security.resource-server.jwk-set-uri}")
    @NotEmpty(message = "jwk url cannot be empty")
    public String jwksUrl;


    // TODO: add scopes and userinfo into security context
    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/graphiql"
                        ).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .jwkSetUri(jwksUrl)
                        ));

        return http.build();
    }


    @PostConstruct
    private void log() {
        log.info("jwks url: {}", jwksUrl);
    }

}

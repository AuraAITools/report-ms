package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.config.converters.AuraAuthenticationToken;
import com.reportai.www.reportapi.config.converters.KeycloakUserPrincipalConverter;
import com.reportai.www.reportapi.filters.TenantFilter;
import com.reportai.www.reportapi.repositories.OutletRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
public class WebSecurityConfig {
    private final OutletRepository outletRepository;


    private final TenantFilter tenantFilter;

    @Autowired
    public WebSecurityConfig(TenantFilter tenantFilter,
                             OutletRepository outletRepository) {
        this.tenantFilter = tenantFilter;
        this.outletRepository = outletRepository;
    }

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
                .authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .jwkSetUri(jwksUrl)
                                        .jwtAuthenticationConverter(jwtAuraAuthenticationTokenConverter())
                        ))
                .addFilterAfter(tenantFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @PostConstruct
    private void log() {
        log.info("jwks url: {}", jwksUrl);
    }

    private Converter<Jwt, AuraAuthenticationToken> jwtAuraAuthenticationTokenConverter() {
        return new KeycloakUserPrincipalConverter(outletRepository);
    }
}

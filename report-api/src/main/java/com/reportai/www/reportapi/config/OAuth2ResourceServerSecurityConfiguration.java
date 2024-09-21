package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.configproperties.OauthResourceServerConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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

    private final OauthResourceServerConfigProperties oauthResourceServerConfigProperties;


    public OAuth2ResourceServerSecurityConfiguration(OauthResourceServerConfigProperties oauthResourceServerConfigProperties) {
        this.oauthResourceServerConfigProperties = oauthResourceServerConfigProperties;
    }

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)-> auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }


    // TODO: add scopes and userinfo into security context
    @PostConstruct
    private void log() {
        log.info("Web security initialising");
    }


}

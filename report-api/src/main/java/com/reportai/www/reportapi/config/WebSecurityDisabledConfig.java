//package com.reportai.www.reportapi.config;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//;
//
//@Configuration
//@Slf4j
//@EnableWebSecurity
//@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "false", matchIfMissing = true)
//public class WebSecurityDisabledConfig {
//    public WebSecurityDisabledConfig() {
//    }
//
//    @Bean
//    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable)
//                .headers(HeadersConfigurer::disable)
//                .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());
//        return http.build();
//    }
//
//    @PostConstruct
//    private void log() {
//        log.info("Security disabled, allowing all requests");
//    }
//
//}
//

package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.config.properties.KeycloakConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@EnableConfigurationProperties(KeycloakConfigProperties.class)
@Profile({"local", "cloud"})
public class KeycloakClientConfig {
    public final Config keycloakConfig;

    @Autowired
    public KeycloakClientConfig(KeycloakConfigProperties keycloakConfigProperties) {
        this.keycloakConfig = keycloakConfigProperties.toKeycloakConfig();
    }

    @Bean
    public Keycloak getKeycloakClient() {
        return KeycloakBuilder
                .builder()
                .clientId(keycloakConfig.getClientId())
                .clientSecret(keycloakConfig.getClientSecret())
                .grantType("client_credentials")
                .serverUrl(keycloakConfig.getServerUrl())
                .realm(keycloakConfig.getRealm())
                .password(keycloakConfig.getPassword())
                .username(keycloakConfig.getUsername())
                .build();
    }

}

package com.reportai.www.reportapi.config.properties;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Slf4j
@ConfigurationProperties(prefix = "keycloak.client")
@Getter
@Setter
@Validated
public class KeycloakConfigProperties {
    @NotEmpty(message = "Keycloak serverUrl must not be empty")
    private String serverUrl;

    @NotEmpty(message = "Keycloak realm must not be empty")
    private String realm;

    @NotEmpty(message = "Keycloak clientId must not be empty")
    private String clientId;

    @NotEmpty(message = "Keycloak clientSecret must not be empty")
    private String clientSecret;

    public Config toKeycloakConfig() {
        return new Config(serverUrl, realm, "", "", clientId, clientSecret);
    }

    @PostConstruct
    private void log() {
        log.info("keycloak configurations for client {} is read ", clientId);
    }
}


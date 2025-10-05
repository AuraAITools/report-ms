package com.aura.details.keycloak.client;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Config;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class KeycloakCredentials {
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

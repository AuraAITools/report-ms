package com.aura.tests.component.initializers;

import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

// TODO: extract variables to a separate properties
public class KeycloakInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        KeycloakContainer keycloakContainer = KeycloakTestContainer.getSingleton();
        String keycloakServerUrl = keycloakContainer.getAuthServerUrl();
        String realm = "aura";
        String jwkSetUri = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        String clientId = "aura-application-client";
        String clientSecret = getClientSecret(keycloakContainer, realm, clientId);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "oauth2.security.resource-server.jwk-set-uri=" + jwkSetUri,
                "oauth2.security.resource-server.enabled=true",
                "keycloak.client.serverUrl=" + keycloakServerUrl,
                "keycloak.client.realm=aura",
                "keycloak.client.clientId=" + clientId,
                "keycloak.client.clientSecret=" + clientSecret
        );
    }

    private String getClientSecret(KeycloakContainer container, String realm, String clientId) {
        // Create Keycloak admin client
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build();

        try {
            // Get the client by clientId
            ClientRepresentation client = keycloak.realm(realm)
                    .clients()
                    .findByClientId(clientId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

            // Get the client secret
            CredentialRepresentation credential = keycloak.realm(realm)
                    .clients()
                    .get(client.getId())
                    .getSecret();

            return credential.getValue();
        } finally {
            keycloak.close();
        }
    }
}

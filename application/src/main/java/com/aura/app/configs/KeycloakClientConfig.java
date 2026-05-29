package com.aura.app.configs;

import com.aura.details.keycloak.client.KeycloakCredentials;
import com.aura.details.keycloak.client.KeycloakIdentityService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile({"local", "cloud", "component-test"}) // TODO: make this a toggle in application.yaml instead
public class KeycloakClientConfig {

    @Bean
    KeycloakIdentityService getKeycloakRemoteIdentityService(ClientResource clientResource,
                                                             UsersResource usersResource,
                                                             KeycloakCredentials keycloakCredentials,
                                                             @Value("${aura.email.action-token-lifespan-seconds:86400}") int actionTokenLifespanSeconds,
                                                             @Value("${aura.email.action-client-id:}") String configuredActionClientId) {
        // Default to the admin/service-account client we already authenticate
        // as; only override if the action-email redirect should be validated
        // against a different (e.g. user-facing) client.
        String actionEmailClientId = configuredActionClientId == null || configuredActionClientId.isBlank()
                ? keycloakCredentials.getClientId()
                : configuredActionClientId;
        return new KeycloakIdentityService(clientResource, usersResource, actionTokenLifespanSeconds, actionEmailClientId);
    }

    @Bean
    Keycloak getKeycloakClient(KeycloakCredentials keycloakCredentials) {
        return KeycloakBuilder
                .builder()
                .clientId(keycloakCredentials.getClientId())
                .clientSecret(keycloakCredentials.getClientSecret())
                .grantType("client_credentials")
                .serverUrl(keycloakCredentials.getServerUrl())
                .realm(keycloakCredentials.getRealm())
                .password("")
                .username("")
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "keycloak.client")
    KeycloakCredentials getKeycloakCredentials() {
        return new KeycloakCredentials();
    }

    @Bean
    RealmResource getKeycloakRealmResource(Keycloak keycloak, KeycloakCredentials keycloakCredentials) {
        return keycloak.realm(keycloakCredentials.getRealm());
    }

    @Bean
    UsersResource getUsersResource(RealmResource realmResource) {
        return realmResource.users();
    }

    @Bean
    ClientResource getAuraApplicationClientResource(RealmResource realmResource, KeycloakCredentials keycloakCredentials) {
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(keycloakCredentials.getClientId()).getFirst();
        return realmResource.clients().get(clientRepresentation.getId());
    }

    @Bean
    ClientRepresentation getAuraApplicationClientRepresentation(ClientResource clientResource) {
        return clientResource.toRepresentation();
    }

}

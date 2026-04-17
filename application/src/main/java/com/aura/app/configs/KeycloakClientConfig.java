package com.aura.app.configs;

import com.aura.details.keycloak.client.KeycloakCredentials;
import com.aura.details.keycloak.client.KeycloakIdentityService;
import com.aura.details.keycloak.client.KeycloakPolicyEnforcementService;
import com.aura.details.keycloak.client.KeycloakResourceAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
@Profile({"local", "cloud", "component-test"}) // TODO: make this a toggle in application.yaml instead
public class KeycloakClientConfig {

    @Bean
    KeycloakIdentityService getKeycloakRemoteIdentityService(ClientResource clientResource, UsersResource usersResource) {
        return new KeycloakIdentityService(clientResource, usersResource);
    }

    @Bean
    KeycloakResourceAuthorizationService keycloakResourceAuthorizationService(ClientResource clientResource) {
        return new KeycloakResourceAuthorizationService(clientResource);
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

    @Bean
    KeycloakPolicyEnforcementService keycloakPolicyEnforcementService(KeycloakCredentials keycloakCredentials) {
        String tokenEndpoint = keycloakCredentials.getServerUrl()
                + "/realms/" + keycloakCredentials.getRealm()
                + "/protocol/openid-connect/token";
        RestClient restClient = RestClient.builder()
                .baseUrl(tokenEndpoint)
                .build();
        return new KeycloakPolicyEnforcementService(restClient, keycloakCredentials.getClientId());
    }

}

package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.clients.keycloak.KeycloakCredentials;
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

@Slf4j
@Configuration
@Profile({"local", "cloud"})
public class KeycloakClientConfig {

    @Bean
    public Keycloak getKeycloakClient(KeycloakCredentials keycloakCredentials) {
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
    public KeycloakCredentials getKeycloakCredentials() {
        return new KeycloakCredentials();
    }

    @Bean
    public RealmResource getKeycloakRealmResource(Keycloak keycloak, KeycloakCredentials keycloakCredentials) {
        return keycloak.realm(keycloakCredentials.getRealm());
    }

    @Bean
    public UsersResource getUsersResource(RealmResource realmResource) {
        return realmResource.users();
    }

    @Bean
    public ClientResource getAuraApplicationClientResource(RealmResource realmResource, KeycloakCredentials keycloakCredentials) {
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(keycloakCredentials.getClientId()).getFirst();
        return realmResource.clients().get(clientRepresentation.getId());
    }

    @Bean
    public ClientRepresentation getAuraApplicationClientRepresentation(ClientResource clientResource) {
        return clientResource.toRepresentation();
    }

}

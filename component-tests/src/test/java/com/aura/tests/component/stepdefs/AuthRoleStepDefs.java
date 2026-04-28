package com.aura.tests.component.stepdefs;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthRoleStepDefs {

    private static final String TEST_PASSWORD = "test-password";
    private static final String REALM = "aura";
    private static final String CLIENT_ID = "aura-application-client";

    private final TestContext testContext;
    private final Keycloak keycloak;
    private final RelationshipAuthorizationService relationshipService;

    @Given("user {string} has {string} role on {string} {string}")
    public void userHasRoleOnResource(String email, String role, String resourceType, String resourceId) {
        var container = KeycloakTestContainer.getSingleton();
        var adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build();

        try {
            // Create user in Keycloak if not exists
            List<UserRepresentation> existing = adminClient.realm(REALM).users().searchByEmail(email, true);
            String userId;
            if (existing.isEmpty()) {
                UserRepresentation user = new UserRepresentation();
                user.setEmail(email);
                user.setUsername(email);
                user.setEnabled(true);
                user.setEmailVerified(true);

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(TEST_PASSWORD);
                credential.setTemporary(false);
                user.setCredentials(List.of(credential));

                adminClient.realm(REALM).users().create(user);
                userId = adminClient.realm(REALM).users().searchByEmail(email, true).getFirst().getId();
                log.info("Created Keycloak user {} with ID {}", email, userId);
            } else {
                userId = existing.getFirst().getId();
            }

            // Resolve {institution_id} and {outlet_id} placeholders in resourceId
            String resolvedResourceId = resolveResourceId(resourceId);

            // Write SpiceDB relationship
            relationshipService.writeRelationship(resourceType, resolvedResourceId, role, "user", userId);
            log.info("Wrote SpiceDB relationship: {}:{}#{}@user:{}", resourceType, resolvedResourceId, role, userId);
        } finally {
            adminClient.close();
        }
    }

    @Given("user switches to {string} identity")
    public void userSwitchesToIdentity(String email) {
        var container = KeycloakTestContainer.getSingleton();

        // Get client secret from Keycloak
        var adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build();

        String clientSecret;
        try {
            var client = adminClient.realm(REALM).clients().findByClientId(CLIENT_ID).getFirst();
            clientSecret = adminClient.realm(REALM).clients().get(client.getId()).getSecret().getValue();
        } finally {
            adminClient.close();
        }

        // Authenticate as the user via password grant
        var userKeycloak = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(clientSecret)
                .username(email)
                .password(TEST_PASSWORD)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        String accessToken = userKeycloak.tokenManager().getAccessToken().getToken();
        userKeycloak.close();

        testContext.setGraphQLClient(
                testContext.getGraphQLClient().mutate()
                        .headers(headers -> headers.setBearerAuth(accessToken))
                        .build()
        );
        log.info("Switched identity to {}", email);
    }

    @Given("user switches back to aura-admin identity")
    public void userSwitchesBackToAuraAdmin() {
        String accessToken = keycloak.tokenManager().getAccessToken().getToken();
        testContext.setGraphQLClient(
                testContext.getGraphQLClient().mutate()
                        .headers(headers -> headers.setBearerAuth(accessToken))
                        .build()
        );
        log.info("Switched back to aura-admin identity");
    }

    private String resolveResourceId(String resourceId) {
        return resourceId
                .replace("{institution_id}", testContext.getInstitutionId() != null ? testContext.getInstitutionId() : "")
                .replace("{outlet_id}", testContext.getOutletId() != null ? testContext.getOutletId() : "");
    }
}

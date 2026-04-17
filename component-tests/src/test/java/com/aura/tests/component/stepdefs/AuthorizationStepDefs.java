package com.aura.tests.component.stepdefs;

import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.contexts.TestContext;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.en.Then;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationStepDefs {

    private final TestContext testContext;

    @Then("Keycloak should have a {string} resource with scopes {string}")
    public void keycloakShouldHaveResourceWithScopes(String resourceName, String scopesCsv) {
        List<String> expectedScopes = List.of(scopesCsv.split(","));

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            ClientRepresentation client = adminClient.realm("aura")
                    .clients().findByClientId("aura-application-client").getFirst();
            ClientResource clientResource = adminClient.realm("aura")
                    .clients().get(client.getId());

            List<ResourceRepresentation> resources = clientResource.authorization()
                    .resources().findByName(resourceName);

            assertThat(resources)
                    .as("Resource '%s' should exist in Keycloak", resourceName)
                    .isNotEmpty();

            ResourceRepresentation resource = resources.getFirst();
            assertThat(resource.getName()).isEqualTo(resourceName);

            List<String> actualScopes = resource.getScopes().stream()
                    .map(ScopeRepresentation::getName)
                    .toList();
            assertThat(actualScopes)
                    .as("Resource '%s' should have scopes %s", resourceName, expectedScopes)
                    .containsExactlyInAnyOrderElementsOf(expectedScopes);
        }
    }

    @Then("Keycloak should have a {string} policy of type {string}")
    public void keycloakShouldHavePolicyOfType(String policyName, String policyType) {
        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            ClientRepresentation client = adminClient.realm("aura")
                    .clients().findByClientId("aura-application-client").getFirst();
            ClientResource clientResource = adminClient.realm("aura")
                    .clients().get(client.getId());

            List<PolicyRepresentation> allPolicies = clientResource.authorization()
                    .policies().policies();

            PolicyRepresentation policy = allPolicies.stream()
                    .filter(p -> policyName.equals(p.getName()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            String.format("Policy '%s' should exist in Keycloak. Found policies: %s",
                                    policyName, allPolicies.stream().map(PolicyRepresentation::getName).toList())));
            assertThat(policy.getType())
                    .as("Policy '%s' should be of type '%s'", policyName, policyType)
                    .isEqualTo(policyType);
        }
    }
}

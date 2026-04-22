package com.aura.tests.component.hooks;

import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.Before;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class DatabaseCleanupHooks {

    private final JdbcTemplate jdbcTemplate;

    @Before
    public void cleanEnvironment() {
        cleanDatabase();
        cleanKeycloakResources();
    }

    private void cleanDatabase() {
        log.info("Cleaning database before scenario");
        long start = System.currentTimeMillis();

        List<String> tables = jdbcTemplate.queryForList("""
                SELECT quote_ident(table_schema) || '.' || quote_ident(table_name)
                FROM information_schema.tables
                WHERE table_schema IN ('public', 'audit')
                  AND table_type = 'BASE TABLE'
                """, String.class);

        if (!tables.isEmpty()) {
            String truncateSql = "TRUNCATE " + String.join(", ", tables) + " CASCADE";
            jdbcTemplate.execute(truncateSql);
        }

        log.info("Database cleanup complete in {}ms", System.currentTimeMillis() - start);
    }

    private void cleanKeycloakResources() {
        log.info("Cleaning Keycloak authorization resources before scenario");
        long start = System.currentTimeMillis();

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

            // Remove dynamically created UMA resources (created during onboarding)
            // Keep static resources from realm-import.json (simple names without ':')
            List<ResourceRepresentation> resources = clientResource.authorization()
                    .resources().resources();
            List<String> removedResources = new java.util.ArrayList<>();
            for (ResourceRepresentation resource : resources) {
                if (resource.getName().contains(":")) {
                    clientResource.authorization().resources().resource(resource.getId()).remove();
                    removedResources.add(resource.getName());
                }
            }
            log.info("Removed {} UMA resources: {}", removedResources.size(), removedResources);

            // Remove dynamically created policies (group policies and permissions from onboarding)
            // Keep the static policies from realm-import.json (prefixed with "aura-admin-")
            List<PolicyRepresentation> policies = clientResource.authorization()
                    .policies().policies();
            List<String> removedPolicies = new java.util.ArrayList<>();
            for (PolicyRepresentation policy : policies) {
                if (!policy.getName().startsWith("aura-admin-")) {
                    clientResource.authorization().policies().policy(policy.getId()).remove();
                    removedPolicies.add(policy.getName());
                }
            }
            log.info("Removed {} policies: {}", removedPolicies.size(), removedPolicies);

            // Remove dynamically created groups (institution admin groups from onboarding)
            List<GroupRepresentation> groups = adminClient.realm("aura").groups().groups();
            List<String> removedGroups = new java.util.ArrayList<>();
            for (GroupRepresentation group : groups) {
                if (!group.getName().equals("aura-admin")) {
                    adminClient.realm("aura").groups().group(group.getId()).remove();
                    removedGroups.add(group.getName());
                }
            }
            log.info("Removed {} groups: {}", removedGroups.size(), removedGroups);

            // Remove dynamically created users (accounts created during tests)
            List<UserRepresentation> users = adminClient.realm("aura").users().list();
            List<String> removedUsers = new java.util.ArrayList<>();
            for (UserRepresentation user : users) {
                if (!user.getUsername().startsWith("service-account-")) {
                    adminClient.realm("aura").users().delete(user.getId());
                    removedUsers.add(user.getUsername());
                }
            }
            log.info("Removed {} users: {}", removedUsers.size(), removedUsers);
        }

        log.info("Keycloak cleanup complete in {}ms", System.currentTimeMillis() - start);
    }
}

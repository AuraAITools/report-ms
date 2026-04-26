package com.aura.tests.component.hooks;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.Before;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class DatabaseCleanupHooks {

    private static final List<String> SPICEDB_RESOURCE_TYPES = List.of(
            "institution", "outlet", "course", "lesson", "account", "level", "subject", "topic", "system"
    );

    private final JdbcTemplate jdbcTemplate;
    private final RelationshipAuthorizationService relationshipService;
    private final Keycloak keycloak;

    @Before
    public void cleanEnvironment() {
        cleanDatabase();
        cleanSpiceDbRelationships();
        cleanKeycloakUsers();
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

    private void cleanSpiceDbRelationships() {
        log.info("Cleaning SpiceDB relationships before scenario");
        long start = System.currentTimeMillis();

        for (String resourceType : SPICEDB_RESOURCE_TYPES) {
            relationshipService.deleteAllRelationships(resourceType);
        }

        // Re-create the system:global admin relationship for the service account
        String serviceAccountUserId = getServiceAccountUserId();
        relationshipService.writeRelationship("system", "global", "admin", "user", serviceAccountUserId);

        log.info("SpiceDB cleanup complete in {}ms", System.currentTimeMillis() - start);
    }

    private void cleanKeycloakUsers() {
        log.info("Cleaning Keycloak users before scenario");
        long start = System.currentTimeMillis();

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().list();
            List<String> removedUsers = new ArrayList<>();
            for (UserRepresentation user : users) {
                if (!user.getUsername().startsWith("service-account-")) {
                    adminClient.realm("aura").users().delete(user.getId());
                    removedUsers.add(user.getUsername());
                }
            }
            log.info("Removed {} Keycloak users: {}", removedUsers.size(), removedUsers);
        }

        log.info("Keycloak cleanup complete in {}ms", System.currentTimeMillis() - start);
    }

    private String getServiceAccountUserId() {
        String token = keycloak.tokenManager().getAccessToken().getToken();
        String[] parts = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        // Extract "sub" from JSON payload
        int subStart = payload.indexOf("\"sub\":\"") + "\"sub\":\"".length();
        int subEnd = payload.indexOf("\"", subStart);
        return payload.substring(subStart, subEnd);
    }
}

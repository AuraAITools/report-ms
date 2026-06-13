package com.aura.tests.component.hooks;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.details.aws.AwsS3Properties;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.email.FakeEmailGateway;
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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Slf4j
@RequiredArgsConstructor
public class DatabaseCleanupHooks {

    private static final List<String> SPICEDB_RESOURCE_TYPES = List.of(
            "institution", "outlet", "course", "lesson", "level", "subject", "topic", "material", "system",
            "institution_user_group", "outlet_user_group", "course_user_group", "lesson_user_group",
            "level_user_group", "subject_user_group", "topic_user_group", "material_user_group", "system_user_group"
    );

    private final JdbcTemplate jdbcTemplate;
    private final RelationshipAuthorizationService relationshipService;
    private final Keycloak keycloak;
    private final FakeEmailGateway fakeEmailGateway;
    private final S3Client testS3Client;
    private final AwsS3Properties awsS3Properties;

    @Before
    public void cleanEnvironment() {
        cleanDatabase();
        cleanSpiceDbRelationships();
        cleanKeycloakUsers();
        cleanEmailGateway();
        cleanS3Bucket();
    }

    private void cleanS3Bucket() {
        String bucket = awsS3Properties.getBucketName();
        log.info("Cleaning S3 bucket {} before scenario", bucket);
        long start = System.currentTimeMillis();

        ListObjectsV2Response listed = testS3Client.listObjectsV2(
                ListObjectsV2Request.builder().bucket(bucket).build());
        if (listed.hasContents() && !listed.contents().isEmpty()) {
            List<ObjectIdentifier> toDelete = listed.contents().stream()
                    .map(obj -> ObjectIdentifier.builder().key(obj.key()).build())
                    .toList();
            testS3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build());
            log.info("Removed {} S3 object(s) from {}", toDelete.size(), bucket);
        } else {
            log.info("S3 bucket {} is already empty", bucket);
        }
        log.info("S3 cleanup complete in {}ms", System.currentTimeMillis() - start);
    }

    private void cleanEmailGateway() {
        int previousCount = fakeEmailGateway.getSent().size();
        fakeEmailGateway.reset();
        log.info("Cleared FakeEmailGateway sent list (had {} message(s))", previousCount);
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

        // Re-create the system bootstrap binding plus the root operator's roles. `admin` grants
        // resource permissions everywhere via parent inheritance; `group_admin` grants user-group
        // management (the two role sets are deliberately decoupled in the schema).
        String serviceAccountUserId = getServiceAccountUserId();
        relationshipService.writeRelationship("system", "aura", "user_group", "system_user_group", "aura");
        relationshipService.writeRelationship("system_user_group", "aura", "admin", "user", serviceAccountUserId);
        relationshipService.writeRelationship("system_user_group", "aura", "group_admin", "user", serviceAccountUserId);

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

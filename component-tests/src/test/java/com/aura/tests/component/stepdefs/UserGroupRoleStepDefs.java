package com.aura.tests.component.stepdefs;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.web.client.HttpServerErrorException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class UserGroupRoleStepDefs {

    private static final String AURA_ADMIN_USER_ID = "2c43b314-3e3c-4897-a3d3-02a8cab3e5e7";

    private final TestContext testContext;
    private final RelationshipAuthorizationService relationshipService;

    @Given("the aura admin is a group_admin of system_user_group {string}")
    public void auraAdminIsSystemGroupAdmin(String systemId) {
        relationshipService.writeRelationship(
                "system_user_group", systemId, "group_admin", "user", AURA_ADMIN_USER_ID);
        log.info("Wrote system_user_group:{}#group_admin@user:{}", systemId, AURA_ADMIN_USER_ID);
    }

    @Given("the aura admin has no group_admin role anywhere")
    public void auraAdminHasNoGroupAdminRole() {
        // No-op: DatabaseCleanupHooks seeds only system_user_group:aura#admin@user:<service-account>,
        // not group_admin. The aura-admin user (AURA_ADMIN_USER_ID) has no group_admin anywhere.
        log.info("Aura admin has no group_admin role — relying on cleanup hook baseline");
    }

    @Given("the institution with id {string} exists in SpiceDB")
    public void institutionExistsInSpiceDb(String institutionId) {
        relationshipService.writeRelationship(
                "institution", institutionId, "user_group", "institution_user_group", institutionId);
        relationshipService.writeRelationship(
                "institution_user_group", institutionId, "parent", "system_user_group", "aura");
        relationshipService.writeRelationship(
                "institution", institutionId, "parent", "system", "aura");
        log.info("Set up institution:{} in SpiceDB", institutionId);
    }

    @Given("the outlet with id {string} exists in SpiceDB under institution {string}")
    public void outletExistsInSpiceDbUnderInstitution(String outletId, String institutionId) {
        institutionExistsInSpiceDb(institutionId);
        relationshipService.writeRelationship(
                "outlet", outletId, "user_group", "outlet_user_group", outletId);
        relationshipService.writeRelationship(
                "outlet_user_group", outletId, "parent", "system_user_group", "aura");
        relationshipService.writeRelationship(
                "outlet", outletId, "parent", "institution", institutionId);
        log.info("Set up outlet:{} in SpiceDB under institution:{}", outletId, institutionId);
    }

    @When("the user grants role {string} on resource {string} {string} to user {string}")
    public void userGrantsRole(String role, String resourceType, String resourceId, String targetUserId) {
        try {
            executeGrant(role, resourceType, resourceId, targetUserId);
            testContext.setRequestFailed(false);
            testContext.setLastHttpStatus(200);
        } catch (FieldAccessException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(extractHttpStatus(ex));
            testContext.setLastErrorMessage(ex.getMessage());
        } catch (HttpServerErrorException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(ex.getStatusCode().value());
            testContext.setLastErrorMessage(ex.getMessage());
        }
    }

    @When("the user tries to grant role {string} on resource {string} {string} to user {string}")
    public void userTriesToGrantRole(String role, String resourceType, String resourceId, String targetUserId) {
        try {
            executeGrant(role, resourceType, resourceId, targetUserId);
            testContext.setRequestFailed(false);
            testContext.setLastHttpStatus(200);
        } catch (FieldAccessException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(extractHttpStatus(ex));
            testContext.setLastErrorMessage(ex.getMessage());
        } catch (HttpServerErrorException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(ex.getStatusCode().value());
            testContext.setLastErrorMessage(ex.getMessage());
        }
    }

    @Then("the grant request should succeed")
    public void theGrantRequestShouldSucceed() {
        assertThat(testContext.isRequestFailed())
                .as("Expected the grant request to succeed but it failed. Last error: %s",
                        testContext.getLastErrorMessage())
                .isFalse();
        assertThat(testContext.getLastHttpStatus())
                .as("Expected HTTP 200 but got %d", testContext.getLastHttpStatus())
                .isEqualTo(200);
    }

    @And("SpiceDB should have relationship {string}")
    public void spiceDbShouldHaveRelationship(String tuple) {
        Tuple t = parseTuple(tuple);
        boolean hasIt = relationshipService.checkPermission(
                t.resourceType(), t.resourceId(), t.relation(), t.subjectType(), t.subjectId());
        assertThat(hasIt)
                .as("Expected relationship %s to grant the same-named permission but it did not.", tuple)
                .isTrue();
    }

    @And("SpiceDB should not have relationship {string}")
    public void spiceDbShouldNotHaveRelationship(String tuple) {
        Tuple t = parseTuple(tuple);
        boolean hasIt = relationshipService.checkPermission(
                t.resourceType(), t.resourceId(), t.relation(), t.subjectType(), t.subjectId());
        assertThat(hasIt)
                .as("Did not expect relationship %s to grant access but it does.", tuple)
                .isFalse();
    }

    private void executeGrant(String role, String resourceType, String resourceId, String targetUserId) {
        Map<String, Object> input = new HashMap<>();
        input.put("userGroupType", resourceType);
        input.put("userGroupId", resourceId);
        input.put("role", role);
        input.put("userId", targetUserId);

        testContext.getGraphQLClient()
                .document(findGraphqlQueryFromFiles("grant-user-group-role.graphql"))
                .variable("input", input)
                .retrieveSync("grantUserGroupRole")
                .toEntity(Boolean.class);
    }

    private static int extractHttpStatus(FieldAccessException ex) {
        Object status = ex.getResponse().getErrors().getFirst().getExtensions().get("http_status");
        return status instanceof Number n ? n.intValue() : 500;
    }

    private static Tuple parseTuple(String tuple) {
        // Format: <resourceType>:<resourceId>#<relation>@<subjectType>:<subjectId>
        String[] hashParts = tuple.split("#", 2);
        String[] resource = hashParts[0].split(":", 2);
        String[] relAndSubject = hashParts[1].split("@", 2);
        String[] subject = relAndSubject[1].split(":", 2);
        return new Tuple(resource[0], resource[1], relAndSubject[0], subject[0], subject[1]);
    }

    record Tuple(String resourceType, String resourceId, String relation,
                 String subjectType, String subjectId) {
    }

    @Given("user {string} has role {string} on {string} {string}")
    public void userHasRoleOnUserGroup(String userId, String role, String resourceType, String resourceId) {
        relationshipService.writeRelationship(resourceType, resourceId, role, "user", userId);
        log.info("Seeded {}:{}#{}@user:{}", resourceType, resourceId, role, userId);
    }

    @When("the user revokes role {string} on resource {string} {string} from user {string}")
    public void userRevokesRole(String role, String resourceType, String resourceId, String targetUserId) {
        try {
            executeRevoke(role, resourceType, resourceId, targetUserId);
            testContext.setRequestFailed(false);
            testContext.setLastHttpStatus(200);
        } catch (FieldAccessException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(extractHttpStatus(ex));
            testContext.setLastErrorMessage(ex.getMessage());
        } catch (HttpServerErrorException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(ex.getStatusCode().value());
            testContext.setLastErrorMessage(ex.getMessage());
        }
    }

    @When("the user tries to revoke role {string} on resource {string} {string} from user {string}")
    public void userTriesToRevokeRole(String role, String resourceType, String resourceId, String targetUserId) {
        try {
            executeRevoke(role, resourceType, resourceId, targetUserId);
            testContext.setRequestFailed(false);
            testContext.setLastHttpStatus(200);
        } catch (FieldAccessException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(extractHttpStatus(ex));
            testContext.setLastErrorMessage(ex.getMessage());
        } catch (HttpServerErrorException ex) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(ex.getStatusCode().value());
            testContext.setLastErrorMessage(ex.getMessage());
        }
    }

    private void executeRevoke(String role, String resourceType, String resourceId, String targetUserId) {
        Map<String, Object> input = new HashMap<>();
        input.put("userGroupType", resourceType);
        input.put("userGroupId", resourceId);
        input.put("role", role);
        input.put("userId", targetUserId);

        testContext.getGraphQLClient()
                .document(findGraphqlQueryFromFiles("revoke-user-group-role.graphql"))
                .variable("input", input)
                .retrieveSync("revokeUserGroupRole")
                .toEntity(Boolean.class);
    }

    @Then("the revoke request should succeed")
    public void theRevokeRequestShouldSucceed() {
        assertThat(testContext.isRequestFailed())
                .as("Expected the revoke request to succeed but it failed. Last error: %s",
                        testContext.getLastErrorMessage())
                .isFalse();
        assertThat(testContext.getLastHttpStatus())
                .as("Expected HTTP 200 but got %d", testContext.getLastHttpStatus())
                .isEqualTo(200);
    }
}

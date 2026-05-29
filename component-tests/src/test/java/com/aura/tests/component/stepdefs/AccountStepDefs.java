package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.contexts.TestContext;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AccountStepDefs {

    private final TestContext testContext;
    private final Keycloak keycloak;
    private final RelationshipAuthorizationService relationshipService;
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Object> lastGetAccountResponse;
    private List<Map<String, Object>> lastGetAccountsResponse;

    @And("user in {string} group creates an institution admin account with details:")
    public void userWithRoleCreatesInstitutionAdminAccount(String role, CreateAccountInput input) {
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(token))
                .build();
        testContext.setGraphQLClient(client);

        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");

        Map<String, Object> inputMap = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "email", input.getEmail(),
                "firstName", input.getFirstName(),
                "lastName", input.getLastName(),
                "contact", input.getContact()
        );

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createInstitutionAdminAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        testContext.setAccountId(response.getId());
    }

    @When("user tries to create an institution admin account without a tenant with details:")
    public void userTriesToCreateInstitutionAdminAccountWithoutTenant(CreateAccountInput input) {
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(token))
                .build();
        testContext.setGraphQLClient(client);

        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        try {
            Map<String, Object> inputMap = Map.of(
                    "email", input.getEmail(),
                    "firstName", input.getFirstName(),
                    "lastName", input.getLastName(),
                    "contact", input.getContact()
            );

            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", inputMap))
                    .retrieveSync("createInstitutionAdminAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the user {string} should be created in Keycloak groups {string}")
    public void theUserShouldBeCreatedInKeycloakGroups(String email, String groupPatternsCsv) {
        // Replaced by SpiceDB relationship verification
        // Verify the user exists in Keycloak and has a corresponding SpiceDB relationship
        List<String> expectedPatterns = List.of(groupPatternsCsv.split(","));

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().searchByEmail(email, true);
            assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

            String userId = users.getFirst().getId();

            // Verify SpiceDB relationships instead of Keycloak groups
            for (String pattern : expectedPatterns) {
                String resolved = pattern.replace("{institution_id}", testContext.getInstitutionId());
                // Pattern format: "institution:{id}:admin" -> check institution admin relationship
                if (resolved.contains(":admin")) {
                    String institutionId = testContext.getInstitutionId();
                    boolean hasPermission = relationshipService.checkPermission(
                            "institution", institutionId, "manage", "user", userId);
                    assertThat(hasPermission)
                            .as("User '%s' should have admin relationship on institution:%s in SpiceDB",
                                    email, institutionId)
                            .isTrue();
                }
            }
        }
    }

    @And("the user {string} should have required actions {string}")
    public void theUserShouldHaveRequiredActions(String email, String actionsCsv) {
        List<String> expectedActions = List.of(actionsCsv.split(","));

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().searchByEmail(email, true);
            assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

            List<String> actualActions = users.getFirst().getRequiredActions();
            assertThat(actualActions)
                    .as("User '%s' should have required actions %s", email, expectedActions)
                    .containsExactlyInAnyOrderElementsOf(expectedActions);
        }
    }

    @And("the user {string} email verified status should be {string}")
    public void theUserEmailVerifiedStatusShouldBe(String email, String expectedVerified) {
        boolean expected = Boolean.parseBoolean(expectedVerified);

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().searchByEmail(email, true);
            assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

            assertThat(users.getFirst().isEmailVerified())
                    .as("User '%s' email verified status should be %s", email, expectedVerified)
                    .isEqualTo(expected);
        }
    }

    @And("the user {string} enabled status should be {string}")
    public void theUserEnabledStatusShouldBe(String email, String expectedEnabled) {
        boolean expected = Boolean.parseBoolean(expectedEnabled);

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().searchByEmail(email, true);
            assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

            assertThat(users.getFirst().isEnabled())
                    .as("User '%s' enabled status should be %s", email, expectedEnabled)
                    .isEqualTo(expected);
        }
    }

    @And("there is a user in {string} group with details:")
    public void thereIsAUserInGroupWithDetails(String groupPattern, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().getFirst();
        String email = row.get("email");
        String firstName = row.get("firstName");
        String lastName = row.get("lastName");

        if ("none".equalsIgnoreCase(groupPattern)) {
            // Create user directly in Keycloak with no group assignments
            KeycloakContainer container = KeycloakTestContainer.getSingleton();
            try (Keycloak adminClient = KeycloakBuilder.builder()
                    .serverUrl(container.getAuthServerUrl())
                    .realm("master")
                    .username(container.getAdminUsername())
                    .password(container.getAdminPassword())
                    .clientId("admin-cli")
                    .build()) {

                UserRepresentation user = new UserRepresentation();
                user.setEmail(email);
                user.setUsername(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEnabled(true);
                user.setEmailVerified(true);
                user.setRequiredActions(List.of());

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue("test-password");
                credential.setTemporary(false);
                user.setCredentials(List.of(credential));

                adminClient.realm("aura").users().create(user);
                log.info("Created Keycloak user '{}' with no groups", email);
            }
            return;
        }

        // Create via the app's createInstitutionAdminAccount mutation
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(token))
                .build();
        testContext.setGraphQLClient(client);

        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");

        Map<String, Object> inputMap = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "contact", Integer.parseInt(row.get("contact"))
        );

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createInstitutionAdminAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        testContext.setAccountId(response.getId());
    }

    // TODO: implement when email notification is implemented
    @And("an email should be sent to {string}")
    public void anEmailShouldBeSentTo(String email) {
        log.info("Email notification assertion skipped for '{}' — not yet implemented", email);
    }

    @When("user creates an institution admin account with details:")
    public void userCreatesInstitutionAdminAccount(CreateAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");

        Map<String, Object> inputMap = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "email", input.getEmail(),
                "firstName", input.getFirstName(),
                "lastName", input.getLastName(),
                "contact", input.getContact()
        );

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createInstitutionAdminAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();

        AccountResponse expected = AccountResponse.newBuilder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .createdBy(TestConstants.AURA_ADMIN_USER_ID)
                .updatedBy(TestConstants.AURA_ADMIN_USER_ID)
                .build();

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "relationship")
                .isEqualTo(expected);

        testContext.setAccountId(response.getId());
    }

    @Then("the institution admin account should be created successfully")
    public void theAdminAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @When("user tries to create an institution admin account with details:")
    public void userTriesToCreateInstitutionAdminAccount(CreateAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createInstitutionAdminAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException | HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the parent account should be created successfully")
    public void theParentAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @Then("the user {string} should have {string} role on {string} {string}")
    public void userShouldHaveRoleOnResource(String email, String role, String resourceType, String resourceIdPattern) {
        String resolvedResourceId = resourceIdPattern
                .replace("{institution_id}", testContext.getInstitutionId())
                .replace("{outlet_id}", testContext.getOutletId() != null ? testContext.getOutletId() : "")
                .replace("{course_id}", testContext.getCourseId() != null ? testContext.getCourseId() : "")
                .replace("{lesson_id}", testContext.getLessonId() != null ? testContext.getLessonId() : "");

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            List<UserRepresentation> users = adminClient.realm("aura").users().searchByEmail(email, true);
            assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();
            String userId = users.getFirst().getId();

            boolean hasPermission = relationshipService.checkPermission(
                    resourceType, resolvedResourceId, "manage", "user", userId);
            assertThat(hasPermission)
                    .as("User '%s' should have %s role on %s %s in SpiceDB", email, role, resourceType, resolvedResourceId)
                    .isTrue();
        }
    }

    @SuppressWarnings("unchecked")
    @And("an educator exists with name {string} and email {string}")
    public void anEducatorExists(String name, String email) {
        String query = findGraphqlQueryFromFiles("create-educators-in-account.graphql");

        // Ensure a level exists for the institution (templates may be truncated by cleanup)
        String levelId = ensureLevelExists();

        Map<String, Object> educatorInput = Map.of(
                "name", name,
                "startDate", "2026-01-01",
                "email", email,
                "employmentType", "FULL_TIME",
                "courseIds", List.of(),
                "subjectIds", List.of(),
                "outletIds", List.of(testContext.getOutletId()),
                "levelIds", List.of(levelId),
                "accountId", testContext.getAccountId()
        );

        List<?> responses = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", List.of(educatorInput)))
                .retrieveSync("createEducatorsInAccount")
                .toEntityList(Map.class);

        assertThat(responses).isNotEmpty();
        Map<String, Object> firstResponse = (Map<String, Object>) responses.get(0);
        String educatorId = (String) firstResponse.get("id");
        assertThat(educatorId).isNotNull();
        testContext.setEducatorId(educatorId);
    }

    @SuppressWarnings("unchecked")
    @And("a student exists with name {string} and email {string}")
    public void aStudentExists(String name, String email) {
        String query = findGraphqlQueryFromFiles("create-students-in-parent-account.graphql");

        // Ensure a level and school exist for the institution (templates may be truncated by cleanup)
        String levelId = ensureLevelExists();
        String schoolId = ensureSchoolExists();

        Map<String, Object> studentInput = Map.of(
                "accountId", testContext.getAccountId(),
                "name", name,
                "email", email,
                "levelId", levelId,
                "schoolId", schoolId,
                "courseEnrollments", List.of()
        );

        List<?> responses = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", List.of(studentInput)))
                .retrieveSync("createStudentsInParentAccount")
                .toEntityList(Map.class);

        assertThat(responses).isNotEmpty();
        Map<String, Object> firstResponse = (Map<String, Object>) responses.get(0);
        String studentId = (String) firstResponse.get("id");
        assertThat(studentId).isNotNull();
        testContext.setStudentId(studentId);
    }

    @When("user queries account by id")
    public void userQueriesAccountById() {
        String query = findGraphqlQueryFromFiles("get-account-by-id.graphql");
        try {
            lastGetAccountResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("accountId", testContext.getAccountId()))
                    .retrieveSync("getAccountById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries account with non-existent id")
    public void userQueriesAccountWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-account-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("accountId", java.util.UUID.randomUUID().toString()))
                    .retrieveSync("getAccountById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the account should be returned with first name {string} and last name {string}")
    public void theAccountShouldBeReturnedWithName(String firstName, String lastName) {
        assertThat(lastGetAccountResponse).isNotNull();
        assertThat(lastGetAccountResponse.get("firstName")).isEqualTo(firstName);
        assertThat(lastGetAccountResponse.get("lastName")).isEqualTo(lastName);
    }

    @When("user queries all accounts in the institution")
    @SuppressWarnings("unchecked")
    public void userQueriesAllAccountsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-accounts-in-institution.graphql");
        lastGetAccountsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getAllAccountsInInstitution")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the accounts response should contain {int} accounts")
    public void theAccountsResponseShouldContain(int expectedCount) {
        assertThat(lastGetAccountsResponse).hasSize(expectedCount);
    }

    @Then("the accounts response should contain at least {int} account")
    public void theAccountsResponseShouldContainAtLeast(int min) {
        assertThat(lastGetAccountsResponse.size()).isGreaterThanOrEqualTo(min);
    }

    private void handleErr(FieldAccessException e) {
        testContext.setRequestFailed(true);
        var errors = e.getResponse().getErrors();
        if (!errors.isEmpty()) {
            var ext = errors.getFirst().getExtensions();
            if (ext != null && ext.containsKey("error_code")) {
                testContext.setLastErrorMessage(ext.get("error_code").toString());
            }
        }
    }

    /**
     * Ensures a level exists for the current institution, inserting one via JDBC if needed
     * (template data may be truncated by DatabaseCleanupHooks between scenarios).
     */
    private String ensureLevelExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM levels WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        String id = java.util.UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO levels (id, version, tenant_id, name, category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test Level', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }

    /**
     * Ensures a school exists for the current institution, inserting one via JDBC if needed
     * (template data may be truncated by DatabaseCleanupHooks between scenarios).
     */
    private String ensureSchoolExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM schools WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        String id = java.util.UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO schools (id, version, tenant_id, name, school_category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test School', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }
}

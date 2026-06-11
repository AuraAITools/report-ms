package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.contexts.TestContext;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
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
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Object> lastGetAccountResponse;
    private List<Map<String, Object>> lastGetAccountsResponse;
    private Map<String, Object> lastParentResponse;
    private String lastCreatedAccountUserId;

    @When("user creates an account with details:")
    @SuppressWarnings("unchecked")
    public void userCreatesAnAccount(CreateAccountInput input) {
        Map<String, Object> response = createAccount(
                input.getEmail(), input.getFirstName(), input.getLastName(), input.getContact());

        assertThat(response.get("firstName")).isEqualTo(input.getFirstName());
        assertThat(response.get("lastName")).isEqualTo(input.getLastName());
    }

    @When("user tries to create an account with details:")
    public void userTriesToCreateAnAccount(CreateAccountInput input) {
        try {
            createAccount(input.getEmail(), input.getFirstName(), input.getLastName(), input.getContact());
            testContext.setRequestFailed(false);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Given("an account exists in the institution for {string} with details:")
    public void anAccountExistsInTheInstitutionFor(String email, DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().getFirst();
        createAccount(email, row.get("firstName"), row.get("lastName"), Integer.parseInt(row.get("contact")));
    }

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @When("user creates a parent in the account with relationship {string}")
    public void userCreatesAParentInTheAccount(String relationship) {
        lastParentResponse = createParentInAccount(relationship);
        assertThat(lastParentResponse.get("id")).isNotNull();
    }

    @When("user tries to create a parent in the account with relationship {string}")
    public void userTriesToCreateAParentInTheAccount(String relationship) {
        try {
            createParentInAccount(relationship);
            testContext.setRequestFailed(false);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the parent should be created with relationship {string}")
    public void theParentShouldBeCreatedWithRelationship(String relationship) {
        assertThat(lastParentResponse).isNotNull();
        assertThat(lastParentResponse.get("relationship")).isEqualTo(relationship);
    }

    @And("the user {string} should have required actions {string}")
    public void theUserShouldHaveRequiredActions(String email, String actionsCsv) {
        List<String> expectedActions = List.of(actionsCsv.split(","));

        List<UserRepresentation> users = searchKeycloakUsersByEmail(email);
        assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

        List<String> actualActions = users.getFirst().getRequiredActions();
        assertThat(actualActions)
                .as("User '%s' should have required actions %s", email, expectedActions)
                .containsExactlyInAnyOrderElementsOf(expectedActions);
    }

    @And("the user {string} email verified status should be {string}")
    public void theUserEmailVerifiedStatusShouldBe(String email, String expectedVerified) {
        boolean expected = Boolean.parseBoolean(expectedVerified);

        List<UserRepresentation> users = searchKeycloakUsersByEmail(email);
        assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

        assertThat(users.getFirst().isEmailVerified())
                .as("User '%s' email verified status should be %s", email, expectedVerified)
                .isEqualTo(expected);
    }

    @And("the user {string} enabled status should be {string}")
    public void theUserEnabledStatusShouldBe(String email, String expectedEnabled) {
        boolean expected = Boolean.parseBoolean(expectedEnabled);

        List<UserRepresentation> users = searchKeycloakUsersByEmail(email);
        assertThat(users).as("User '%s' should exist in Keycloak", email).isNotEmpty();

        assertThat(users.getFirst().isEnabled())
                .as("User '%s' enabled status should be %s", email, expectedEnabled)
                .isEqualTo(expected);
    }

    @SuppressWarnings("unchecked")
    @And("an educator exists with name {string} and email {string}")
    public void anEducatorExists(String name, String email) {
        String query = findGraphqlQueryFromFiles("create-educators-in-account.graphql");

        // Ensure a level exists for the institution (templates may be truncated by cleanup)
        String levelId = ensureLevelExists();

        Map<String, Object> educatorInput = Map.ofEntries(
                Map.entry("name", name),
                Map.entry("startDate", "2026-01-01"),
                Map.entry("dateOfBirth", "1990-01-01"),
                Map.entry("email", email),
                Map.entry("employmentType", "FULL_TIME"),
                Map.entry("courseIds", List.of()),
                Map.entry("subjectIds", List.of()),
                Map.entry("outletIds", List.of(testContext.getOutletId())),
                Map.entry("levelIds", List.of(levelId)),
                Map.entry("accountId", testContext.getAccountId()),
                Map.entry("institutionId", testContext.getInstitutionId())
        );
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "educators", List.of(educatorInput)
        );

        List<?> responses = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
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
        String query = findGraphqlQueryFromFiles("create-students-in-account.graphql");

        // Ensure a level and school exist for the institution (templates may be truncated by cleanup)
        String levelId = ensureLevelExists();
        String schoolId = ensureSchoolExists();

        Map<String, Object> studentInput = Map.of(
                "accountId", testContext.getAccountId(),
                "name", name,
                "email", email,
                "dateOfBirth", "2015-01-01",
                "levelId", levelId,
                "schoolId", schoolId,
                "institutionId", testContext.getInstitutionId(),
                "courseEnrollments", List.of()
        );
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "students", List.of(studentInput)
        );

        List<?> responses = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createStudentsInAccount")
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
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "accountId", testContext.getAccountId()))
                    .retrieveSync("getAccountById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries account by user id")
    public void userQueriesAccountByUserId() {
        String query = findGraphqlQueryFromFiles("get-account-by-user-id.graphql");
        try {
            lastGetAccountResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "userId", lastCreatedAccountUserId))
                    .retrieveSync("getAccountByUserId")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries account by a non-existent user id")
    public void userQueriesAccountByNonExistentUserId() {
        String query = findGraphqlQueryFromFiles("get-account-by-user-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "userId", java.util.UUID.randomUUID().toString()))
                    .retrieveSync("getAccountByUserId")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the account by user id should be returned with first name {string} and last name {string}")
    public void theAccountByUserIdShouldBeReturnedWithName(String firstName, String lastName) {
        assertThat(lastGetAccountResponse).isNotNull();
        assertThat(lastGetAccountResponse.get("firstName")).isEqualTo(firstName);
        assertThat(lastGetAccountResponse.get("lastName")).isEqualTo(lastName);
        assertThat(lastGetAccountResponse.get("userId")).isEqualTo(lastCreatedAccountUserId);
    }

    @When("user queries account with non-existent id")
    public void userQueriesAccountWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-account-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "accountId", java.util.UUID.randomUUID().toString()))
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

    @Then("the accounts response should contain {int} account(s)")
    public void theAccountsResponseShouldContain(int expectedCount) {
        assertThat(lastGetAccountsResponse).hasSize(expectedCount);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createAccount(String email, String firstName, String lastName, int contact) {
        String query = findGraphqlQueryFromFiles("create-account.graphql");

        Map<String, Object> inputMap = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "contact", contact
        );

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createAccount")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        testContext.setAccountId(response.get("id").toString());
        lastCreatedAccountUserId = (String) response.get("userId");
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createParentInAccount(String relationship) {
        String query = findGraphqlQueryFromFiles("create-parent-in-account.graphql");

        Map<String, Object> inputMap = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "accountId", testContext.getAccountId(),
                "relationship", relationship
        );

        return testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createParentInAccount")
                .toEntity(Map.class);
    }

    private List<UserRepresentation> searchKeycloakUsersByEmail(String email) {
        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {
            return adminClient.realm("aura").users().searchByEmail(email, true);
        }
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

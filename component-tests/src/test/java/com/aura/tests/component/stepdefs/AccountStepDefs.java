package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
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
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AccountStepDefs {

    private final TestContext testContext;
    private final Keycloak keycloak;
    private final RelationshipAuthorizationService relationshipService;

    @And("user in {string} group creates an institution admin account with details:")
    public void userWithRoleCreatesInstitutionAdminAccount(String role, CreateInstitutionAdminAccountInput input) {
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
    public void userTriesToCreateInstitutionAdminAccountWithoutTenant(CreateInstitutionAdminAccountInput input) {
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
    public void userCreatesInstitutionAdminAccount(CreateInstitutionAdminAccountInput input) {
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
    public void userTriesToCreateInstitutionAdminAccount(CreateInstitutionAdminAccountInput input) {
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

    @When("user creates a parent account with details:")
    public void userCreatesParentAccount(CreateParentAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-parent-account.graphql");

        Map<String, Object> inputMap = new java.util.HashMap<>();
        inputMap.put("institutionId", testContext.getInstitutionId());
        inputMap.put("email", input.getEmail());
        inputMap.put("firstName", input.getFirstName());
        inputMap.put("lastName", input.getLastName());
        inputMap.put("contact", input.getContact());
        inputMap.put("relationship", input.getRelationship().name());

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createParentAccount")
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

    @Then("the parent account should be created successfully")
    public void theParentAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @When("institution admin adds user {string} as institution admin")
    public void institutionAdminAddsUserAsInstitutionAdmin(String targetUserEmail) {
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(token))
                .build();
        testContext.setGraphQLClient(client);

        String query = findGraphqlQueryFromFiles("add-institution-admin.graphql");

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "userEmail", targetUserEmail
        );

        Boolean result = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("addInstitutionAdmin")
                .toEntity(Boolean.class);

        assertThat(result).isTrue();
    }

    @When("outlet admin adds user {string} as outlet admin")
    public void outletAdminAddsUserAsOutletAdmin(String targetUserEmail) {
        String query = findGraphqlQueryFromFiles("add-outlet-admin.graphql");

        Map<String, Object> input = Map.of(
                "outletId", testContext.getOutletId(),
                "userEmail", targetUserEmail
        );

        Boolean result = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("addOutletAdmin")
                .toEntity(Boolean.class);

        assertThat(result).isTrue();
    }

    @When("course admin adds user {string} as course admin")
    public void courseAdminAddsUserAsCourseAdmin(String targetUserEmail) {
        String query = findGraphqlQueryFromFiles("add-course-admin.graphql");

        Map<String, Object> input = Map.of(
                "courseId", testContext.getCourseId(),
                "userEmail", targetUserEmail
        );

        Boolean result = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("addCourseAdmin")
                .toEntity(Boolean.class);

        assertThat(result).isTrue();
    }

    @Then("the user {string} should have {string} role on {string} {string}")
    public void userShouldHaveRoleOnResource(String email, String role, String resourceType, String resourceIdPattern) {
        String resolvedResourceId = resourceIdPattern
                .replace("{institution_id}", testContext.getInstitutionId())
                .replace("{outlet_id}", testContext.getOutletId() != null ? testContext.getOutletId() : "")
                .replace("{course_id}", testContext.getCourseId() != null ? testContext.getCourseId() : "");

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

    @When("user tries to create a parent account with details:")
    public void userTriesToCreateParentAccount(CreateParentAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-parent-account.graphql");
        try {
            Map<String, Object> inputMap = new java.util.HashMap<>();
            inputMap.put("email", input.getEmail());
            inputMap.put("firstName", input.getFirstName());
            inputMap.put("lastName", input.getLastName());
            inputMap.put("contact", input.getContact());
            inputMap.put("relationship", input.getRelationship().name());

            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", inputMap))
                    .retrieveSync("createParentAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException | HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
}

package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
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
import org.keycloak.representations.idm.GroupRepresentation;
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

    @And("user in {string} group creates an institution admin account with details:")
    public void userWithRoleCreatesInstitutionAdminAccount(String role, CreateInstitutionAdminAccountInput input) {
        // Set auth token
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var tenantClient = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.set("x-tenant-id", testContext.getInstitutionId());
                })
                .build();
        testContext.setGraphQLClient(tenantClient);

        // Create the account
        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createInstitutionAdminAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        testContext.setAccountId(response.getId());
    }

    @When("user tries to create an institution admin account without a tenant with details:")
    public void userTriesToCreateInstitutionAdminAccountWithoutTenant(CreateInstitutionAdminAccountInput input) {
        // Set auth token but NO x-tenant-id
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var tenantClient = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(token))
                .build();
        testContext.setGraphQLClient(tenantClient);

        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
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
            List<GroupRepresentation> userGroups = adminClient.realm("aura").users().get(userId).groups();
            List<String> actualGroupNames = userGroups.stream()
                    .map(GroupRepresentation::getName)
                    .toList();

            for (String pattern : expectedPatterns) {
                String resolved = pattern.replace("{institution_id}", testContext.getInstitutionId());
                assertThat(actualGroupNames)
                        .as("User '%s' should be in group '%s', but found %s", email, resolved, actualGroupNames)
                        .contains(resolved);
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
                user.setEmailVerified(false);

                adminClient.realm("aura").users().create(user);
                log.info("Created Keycloak user '{}' with no groups", email);
            }
            return;
        }

        // Create via the app's createInstitutionAdminAccount mutation
        String token = keycloak.tokenManager().getAccessToken().getToken();
        var tenantClient = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.set("x-tenant-id", testContext.getInstitutionId());
                })
                .build();
        testContext.setGraphQLClient(tenantClient);

        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        CreateInstitutionAdminAccountInput input = CreateInstitutionAdminAccountInput.newBuilder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .contact(row.get("contact"))
                .build();

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
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

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
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

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
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

    @When("user tries to create a parent account with details:")
    public void userTriesToCreateParentAccount(CreateParentAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-parent-account.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createParentAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException | HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
}

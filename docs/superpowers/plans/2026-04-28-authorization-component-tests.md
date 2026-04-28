# Authorization Component Tests Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Write component tests verifying the SpiceDB authorization model for all roles (platform admin, institution admin/viewer, outlet admin/modifier/viewer, educator, parent).

**Architecture:** New `AuthRoleStepDefs` handles user creation + SpiceDB role assignment + identity switching via Keycloak password grant. "Tries" variants added to existing step defs for denied-access testing. Eight feature files test each role's access grants and denials.

**Tech Stack:** Cucumber BDD, Spring Boot TestContainers, Keycloak (auth), SpiceDB (authz), GraphQL

**Spec:** `docs/superpowers/specs/2026-04-28-authorization-component-tests-design.md`

**IMPORTANT:** Run only specific scenarios, never all component tests: `mvn test -pl component-tests -Dcucumber.filter.tags="@TagName"`

---

### Task 1: Create AuthRoleStepDefs

Core infrastructure for assigning SpiceDB roles and switching user identity in tests.

**Files:**
- Create: `component-tests/src/test/java/com/aura/tests/component/stepdefs/AuthRoleStepDefs.java`

- [ ] **Step 1: Create the AuthRoleStepDefs file**

```java
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
```

- [ ] **Step 2: Commit**

```bash
git add component-tests/src/test/java/com/aura/tests/component/stepdefs/AuthRoleStepDefs.java
git commit -m "feat: add AuthRoleStepDefs for SpiceDB role assignment and identity switching"
```

---

### Task 2: Add "tries" Variants to Existing Step Defs

Add try-catch wrappers for mutations and queries that don't have "tries" variants yet.

**Files:**
- Modify: `component-tests/src/test/java/com/aura/tests/component/stepdefs/CourseStepDefs.java`
- Modify: `component-tests/src/test/java/com/aura/tests/component/stepdefs/SubjectStepDefs.java`
- Modify: `component-tests/src/test/java/com/aura/tests/component/stepdefs/TopicStepDefs.java`
- Modify: `component-tests/src/test/java/com/aura/tests/component/stepdefs/QueryStepDefs.java`

- [ ] **Step 1: Add "tries" variant to CourseStepDefs**

Add this method to `CourseStepDefs.java` after the existing `userCreatesACourse` method:

```java
    @When("user tries to create a course with details:")
    public void userTriesToCreateACourse(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", data.get("name"),
                "maxSize", Integer.parseInt(data.get("maxSize")),
                "lessonFrequency", data.get("lessonFrequency"),
                "priceRecord", Map.of(
                        "price", Double.parseDouble(data.get("price")),
                        "frequency", data.get("priceFrequency")
                ),
                "courseStartTimestamptz", data.get("startDate"),
                "courseEndTimestamptz", data.get("endDate"),
                "outletId", testContext.getOutletId()
        );

        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createCourse")
                    .toEntity(CourseResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
```

Also add import at the top of the file:
```java
import org.springframework.graphql.client.FieldAccessException;
```

- [ ] **Step 2: Add "tries" variant to SubjectStepDefs**

Add this method to `SubjectStepDefs.java`:

```java
    @When("user tries to create a subject with name {string}")
    public void userTriesToCreateASubjectWithName(String subjectName) {
        String query = findGraphqlQueryFromFiles("create-subject.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", Map.of("institutionId", testContext.getInstitutionId(), "name", subjectName)))
                    .retrieveSync("createSubject")
                    .toEntity(SubjectResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
```

Also add import:
```java
import org.springframework.graphql.client.FieldAccessException;
```

- [ ] **Step 3: Add "tries" variant to TopicStepDefs**

Add this method to `TopicStepDefs.java`:

```java
    @When("user tries to create a topic with name {string}")
    public void userTriesToCreateATopicWithName(String topicName) {
        String query = findGraphqlQueryFromFiles("create-topic.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", Map.of("institutionId", testContext.getInstitutionId(), "name", topicName)))
                    .retrieveSync("createTopic")
                    .toEntity(TopicResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
```

Also add import:
```java
import org.springframework.graphql.client.FieldAccessException;
```

- [ ] **Step 4: Add "tries" variants to QueryStepDefs for outlets and levels**

Add these methods to `QueryStepDefs.java`:

```java
    @When("user tries to query outlets by page")
    public void userTriesToQueryOutletsByPage() {
        String query = findGraphqlQueryFromFiles("get-outlets-by-page.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                    .retrieveSync("getOutletsByPage")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
            testContext.setLastQuerySucceeded(true);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user tries to query levels by page")
    public void userTriesToQueryLevelsByPage() {
        String query = findGraphqlQueryFromFiles("get-levels-by-page.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                    .retrieveSync("getLevelByPage")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
            testContext.setLastQuerySucceeded(true);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
```

- [ ] **Step 5: Compile to verify no errors**

Run: `mvn clean compile -pl component-tests`

Expected: BUILD SUCCESS

- [ ] **Step 6: Commit**

```bash
git add component-tests/src/test/java/com/aura/tests/component/stepdefs/CourseStepDefs.java \
       component-tests/src/test/java/com/aura/tests/component/stepdefs/SubjectStepDefs.java \
       component-tests/src/test/java/com/aura/tests/component/stepdefs/TopicStepDefs.java \
       component-tests/src/test/java/com/aura/tests/component/stepdefs/QueryStepDefs.java
git commit -m "feat: add tries variants for course, subject, topic, and query step defs"
```

---

### Task 3: Platform Admin Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_platform_admin.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Platform admin authorization

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create outlet under onboarded institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user creates an outlet with details:
      | name        | address        | postalCode | contactNumber | description    | email            |
      | Test Outlet | 123 Test St    | 123456     | 91234567      | A test outlet  | outlet@test.com  |
    Then the outlet should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create course under outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the course should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create standalone lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    When user creates a standalone lesson with details:
      | name          | description    | startDate              | endDate                |
      | Test Lesson   | A test lesson  | 2026-03-01T09:00:00Z   | 2026-03-01T11:00:00Z   |
    Then the lesson should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can view institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user queries levels by page
    Then the levels query should respond successfully
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthPlatformAdmin"`

Expected: All 6 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_platform_admin.feature
git commit -m "test: add platform admin authorization component tests"
```

---

### Task 4: Institution Admin Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_institution_admin.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Institution admin authorization

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates an outlet with details:
      | name        | address        | postalCode | contactNumber | description    | email            |
      | Test Outlet | 123 Test St    | 123456     | 91234567      | A test outlet  | outlet@test.com  |
    Then the outlet should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create subject
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates a subject with name "Mathematics"
    Then the subject "Mathematics" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can create topic
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user creates a topic with name "Algebra"
    Then the topic "Algebra" should be created successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionAdmin
  Scenario: Institution admin cannot create institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "instadmin@test.com" has "admin" role on "institution" "{institution_id}"
    And user switches to "instadmin@test.com" identity
    When user tries to onboard an institution with details:
      | name       | email           | contactNumber | address        | uen       |
      | Other Inst | other@test.com  | 91234567      | 456 Other St   | UEN123456 |
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthInstitutionAdmin"`

Expected: All 7 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_institution_admin.feature
git commit -m "test: add institution admin authorization component tests"
```

---

### Task 5: Institution Viewer Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_institution_viewer.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Institution viewer authorization

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user queries levels by page
    Then the levels query should respond successfully

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create subject
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create a subject with name "Unauthorized Subject"
    Then the request should fail with an error

  @ComponentTest @AuthInstitutionViewer
  Scenario: Institution viewer cannot create institution admin account
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "viewer@test.com" has "viewer" role on "institution" "{institution_id}"
    And user switches to "viewer@test.com" identity
    When user tries to create an institution admin account with details:
      | email              | firstName | lastName | contact  |
      | newadmin@test.com  | New       | Admin    | 91234567 |
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthInstitutionViewer"`

Expected: All 6 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_institution_viewer.feature
git commit -m "test: add institution viewer authorization component tests"
```

---

### Task 6: Outlet Admin Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_outlet_admin.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Outlet admin authorization

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin can create course under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the course should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin can create standalone lesson under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user creates a standalone lesson with details:
      | name          | description    | startDate              | endDate                |
      | Test Lesson   | A test lesson  | 2026-03-01T09:00:00Z   | 2026-03-01T11:00:00Z   |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot create institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user tries to onboard an institution with details:
      | name       | email           | contactNumber | address        | uen       |
      | Other Inst | other@test.com  | 91234567      | 456 Other St   | UEN123456 |
    Then the request should fail with an error

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthOutletAdmin"`

Expected: All 4 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_outlet_admin.feature
git commit -m "test: add outlet admin authorization component tests"
```

---

### Task 7: Outlet Modifier Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_outlet_modifier.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Outlet modifier authorization

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier can create course under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the course should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier can create standalone lesson under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user creates a standalone lesson with details:
      | name          | description    | startDate              | endDate                |
      | Test Lesson   | A test lesson  | 2026-03-01T09:00:00Z   | 2026-03-01T11:00:00Z   |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthOutletModifier"`

Expected: All 3 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_outlet_modifier.feature
git commit -m "test: add outlet modifier authorization component tests"
```

---

### Task 8: Outlet Viewer Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_outlet_viewer.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Outlet viewer authorization

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer can view institution via outlet view cascade
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the request should fail with an error

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create standalone lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user tries to create a standalone lesson with name "Unauthorized Lesson"
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthOutletViewer"`

Expected: All 3 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_outlet_viewer.feature
git commit -m "test: add outlet viewer authorization component tests"
```

---

### Task 9: Educator Authorization Tests

Tests the cascade boundary — educators see institution + outlets but NOT courses.

**Files:**
- Create: `component-tests/src/test/resources/features/auth_educator.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Educator authorization

  @ComponentTest @AuthEducator
  Scenario: Educator can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthEducator
  Scenario: Educator can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "educator@test.com" has "educator" role on "institution" "{institution_id}"
    And user switches to "educator@test.com" identity
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthEducator"`

Expected: All 4 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_educator.feature
git commit -m "test: add educator authorization component tests"
```

---

### Task 10: Parent Authorization Tests

**Files:**
- Create: `component-tests/src/test/resources/features/auth_parent.feature`

- [ ] **Step 1: Create the feature file**

```gherkin
@Api
Feature: Parent authorization

  @ComponentTest @AuthParent
  Scenario: Parent can view their institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "parent@test.com" has "parent_user" role on "institution" "{institution_id}"
    And user switches to "parent@test.com" identity
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthParent
  Scenario: Parent can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "parent@test.com" has "parent_user" role on "institution" "{institution_id}"
    And user switches to "parent@test.com" identity
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthParent
  Scenario: Parent cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "parent@test.com" has "parent_user" role on "institution" "{institution_id}"
    And user switches to "parent@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

  @ComponentTest @AuthParent
  Scenario: Parent cannot create course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "parent@test.com" has "parent_user" role on "institution" "{institution_id}"
    And user switches to "parent@test.com" identity
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the request should fail with an error

  @ComponentTest @AuthParent
  Scenario: Parent cannot create institution admin account
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And user "parent@test.com" has "parent_user" role on "institution" "{institution_id}"
    And user switches to "parent@test.com" identity
    When user tries to create an institution admin account with details:
      | email              | firstName | lastName | contact  |
      | newadmin@test.com  | New       | Admin    | 91234567 |
    Then the request should fail with an error
```

- [ ] **Step 2: Run the tests**

Run: `mvn test -pl component-tests -Dcucumber.filter.tags="@AuthParent"`

Expected: All 5 scenarios PASS

- [ ] **Step 3: Commit**

```bash
git add component-tests/src/test/resources/features/auth_parent.feature
git commit -m "test: add parent authorization component tests"
```

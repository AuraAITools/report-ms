# Implementation Plan: ADR-004 — SpiceDB ReBAC Migration

## Overview

Replace Keycloak's authorization services (UMA resources, group policies, RPT tokens) with SpiceDB for all instance-level permission decisions. Keycloak remains as the identity provider (authentication, user management, JWT issuance).

## Prerequisites

- ADR-004 accepted (Option 2: SpiceDB)
- Familiarity with SpiceDB schema language and gRPC API
- Docker available for local SpiceDB development

---

## Phase 1: Add SpiceDB Dependencies and Schema

**Goal:** Add the SpiceDB Java client library to the project and define the authorization schema.

### Step 1.1: Add SpiceDB dependencies to parent POM

**File:** `parent/pom.xml`

Add to `<properties>`:
```xml
<authzed.version>1.5.0</authzed.version>
<grpc.version>1.72.0</grpc.version>
```

Add to `<dependencyManagement>`:
```xml
<dependency>
    <groupId>com.authzed.api</groupId>
    <artifactId>authzed</artifactId>
    <version>${authzed.version}</version>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
    <version>${grpc.version}</version>
</dependency>
```

### Step 1.2: Add SpiceDB dependency to `details` module

**File:** `details/pom.xml`

```xml
<dependency>
    <groupId>com.authzed.api</groupId>
    <artifactId>authzed</artifactId>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
</dependency>
```

### Step 1.3: Add SpiceDB dependency to `application` module

**File:** `application/pom.xml`

Same dependencies as details (needed for `SpiceDbPermissionEvaluator` and config).

### Step 1.4: Create the SpiceDB schema file

**File (new):** `application/src/main/resources/spicedb/schema.zed`

```zed
definition user {}

definition institution {
    relation admin: user
    relation viewer: user

    permission manage = admin
    permission create = admin
    permission view = viewer + admin
    permission delegate = admin
}

definition outlet {
    relation parent_institution: institution
    relation admin: user
    relation viewer: user

    permission manage = admin + parent_institution->manage
    permission create = admin + parent_institution->manage
    permission view = viewer + admin + parent_institution->view
    permission delegate = admin + parent_institution->delegate
}

definition course {
    relation parent_outlet: outlet
    relation admin: user
    relation educator: user
    relation viewer: user

    permission manage = admin + educator + parent_outlet->manage
    permission create = admin + parent_outlet->manage
    permission view = viewer + educator + admin + parent_outlet->view
    permission delegate = admin + parent_outlet->delegate
}

definition lesson {
    relation parent_course: course
    relation parent_outlet: outlet
    relation educator: user

    permission manage = educator + parent_course->manage
    permission view = educator + parent_course->view
}

definition account {
    relation owner: user
    relation parent_institution: institution

    permission manage = owner + parent_institution->manage
    permission view = owner + parent_institution->view
}

definition level {
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}

definition subject {
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}

definition topic {
    relation parent_subject: subject

    permission manage = parent_subject->manage
    permission view = parent_subject->view
}
```

### Step 1.5: Verify

- `mvn clean install -DskipTests` — confirm dependencies resolve and project compiles.

---

## Phase 2: Core Layer — Replace Authorization Interfaces

**Goal:** Replace Keycloak-specific `GroupAuthorizationService` and `ResourceAuthorizationService` with a single `RelationshipAuthorizationService` interface. Keep `IdentityService` unchanged (Keycloak still manages identity).

### Step 2.1: Create `RelationshipAuthorizationService` interface

**File (new):** `core/src/main/java/com/aura/core/clients/RelationshipAuthorizationService.java`

```java
public interface RelationshipAuthorizationService {
    void writeRelationship(String resourceType, String resourceId,
                           String relation, String subjectType, String subjectId);

    void deleteRelationship(String resourceType, String resourceId,
                            String relation, String subjectType, String subjectId);

    void writeRelationships(List<Relationship> relationships);

    boolean checkPermission(String resourceType, String resourceId,
                            String permission, String subjectType, String subjectId);

    record Relationship(String resourceType, String resourceId,
                        String relation, String subjectType, String subjectId) {}
}
```

No Spring imports. Pure Java. Follows existing core conventions.

### Step 2.2: Delete old authorization interfaces

**Delete:** `core/src/main/java/com/aura/core/clients/GroupAuthorizationService.java`
**Delete:** `core/src/main/java/com/aura/core/clients/ResourceAuthorizationService.java`

### Step 2.3: Delete Keycloak authorization naming/path utilities

**Delete:** `core/src/main/java/com/aura/core/auth/AuthorizationGroupPath.java`
**Delete:** `core/src/main/java/com/aura/core/auth/AuthorizationNaming.java`
**Delete:** `core/src/main/java/com/aura/core/auth/AuthorizationRoles.java`

These are replaced by the SpiceDB schema. The schema defines roles (relations), permissions, and hierarchy — no Java code needed for naming conventions.

### Step 2.4: Fix compilation in `AddUserToGroupUseCase`

This use case currently depends on `GroupAuthorizationService`. Refactor to use `RelationshipAuthorizationService`:

**File:** `core/src/main/java/com/aura/core/usecases/accounts/AddUserToGroupUseCase.java` (and its impl)

Change to accept `RelationshipAuthorizationService` and write a relationship instead of adding to a group. The method signature changes from `addUserToGroup(email, groupName)` to something like `assignRole(userId, resourceType, resourceId, role)`.

### Step 2.5: Update unit tests

- Delete: `core/src/test/java/.../auth/AuthorizationGroupPathTest.java`
- Delete: `core/src/test/java/.../auth/AuthorizationNamingTest.java`
- Delete: `core/src/test/java/.../auth/AuthorizationRolesTest.java` (if exists)
- Update: `DefaultInstitutionOnboardingStrategyTest.java` — remove any auth-related assertions (already minimal)

### Step 2.6: Verify

- `mvn clean compile -pl core` — confirm core compiles with no Keycloak references.

---

## Phase 3: Details Layer — SpiceDB Client Implementation

**Goal:** Implement `RelationshipAuthorizationService` using SpiceDB's Java gRPC client.

### Step 3.1: Create SpiceDB credentials class

**File (new):** `details/src/main/java/com/aura/details/spicedb/SpiceDbCredentials.java`

```java
@Getter @Setter @NoArgsConstructor
public class SpiceDbCredentials {
    private String target;       // e.g., "localhost:50051"
    private String presharedKey;
    private boolean plaintext;   // true for local dev, false for production
}
```

### Step 3.2: Create SpiceDB implementation of `RelationshipAuthorizationService`

**File (new):** `details/src/main/java/com/aura/details/spicedb/SpiceDbRelationshipService.java`

```java
@Slf4j
@RequiredArgsConstructor
public class SpiceDbRelationshipService implements RelationshipAuthorizationService {

    private final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    @Override
    public void writeRelationship(String resourceType, String resourceId,
                                   String relation, String subjectType, String subjectId) {
        // Build WriteRelationshipsRequest with OPERATION_TOUCH
        // Call permissionsService.writeRelationships()
    }

    @Override
    public void deleteRelationship(String resourceType, String resourceId,
                                    String relation, String subjectType, String subjectId) {
        // Build WriteRelationshipsRequest with OPERATION_DELETE
        // Call permissionsService.writeRelationships()
    }

    @Override
    public void writeRelationships(List<Relationship> relationships) {
        // Batch write all relationships in a single gRPC call
    }

    @Override
    public boolean checkPermission(String resourceType, String resourceId,
                                    String permission, String subjectType, String subjectId) {
        // Build CheckPermissionRequest with minimize_latency consistency
        // Return true if PERMISSIONSHIP_HAS_PERMISSION
    }
}
```

### Step 3.3: Delete old Keycloak authorization implementations

**Delete:** `details/src/main/java/com/aura/details/keycloak/client/KeycloakGroupAuthorizationService.java`
**Delete:** `details/src/main/java/com/aura/details/keycloak/client/KeycloakResourceAuthorizationService.java`

Keep `KeycloakIdentityService.java` — it still manages users in Keycloak. But remove any group-assignment calls from it (those move to `RelationshipAuthorizationService`).

### Step 3.4: Delete `ResourceAuthorizationRegistrar`

**Delete:** `details/src/main/java/com/aura/details/services/ResourceAuthorizationRegistrar.java`

This entire class is replaced by simple `writeRelationship()` calls in service methods. No registrar abstraction needed — writing a parent relationship is a single call.

### Step 3.5: Update unit tests

- Delete: `details/src/test/java/.../KeycloakGroupAuthorizationServiceTest.java`
- Delete: `details/src/test/java/.../KeycloakResourceAuthorizationServiceTest.java`
- Delete: `details/src/test/java/.../ResourceAuthorizationRegistrarTest.java`
- Create: `details/src/test/java/.../spicedb/SpiceDbRelationshipServiceTest.java` — mock the gRPC stub, verify correct request construction

### Step 3.6: Verify

- `mvn clean compile -pl details` — confirm details compiles.

---

## Phase 4: Refactor Service Layer — Relationship Writes

**Goal:** Replace `ResourceAuthorizationRegistrar` calls in all services with direct `RelationshipAuthorizationService.writeRelationship()` calls.

### Step 4.1: Refactor `OnboardingService`

**File:** `details/src/main/java/com/aura/details/services/OnboardingService.java`

Before (current — 20 lines of group path/policy wiring):
```java
Map<String, String> parentPaths = AuthorizationRoles.ALL_ROLES.stream()...
Map<String, Set<String>> ancestorPolicies = AuthorizationRoles.ALL_ROLES.stream()...
registrar.registerResource("institution", INSTITUTION_RESOURCE_TYPE, created.getId(), parentPaths, ancestorPolicies);
```

After (SpiceDB — no hierarchy wiring needed, institution is top-level):
```java
// No relationship write needed here — institution is the root.
// Users are linked to institutions via admin/viewer relations in AccountService.
```

The `OnboardingService` constructor changes: remove `ResourceAuthorizationRegistrar` dependency. Add `RelationshipAuthorizationService` only if institution needs explicit relationships (e.g., for future system-level admin).

### Step 4.2: Refactor `OutletService`

**File:** `details/src/main/java/com/aura/details/services/OutletService.java`

Before:
```java
registrar.registerResource("outlet", "urn:aura:outlet", outlet.getId(), parentPaths, ancestorPolicies);
```

After:
```java
relationshipService.writeRelationship("outlet", outlet.getId(),
    "parent_institution", "institution", outlet.getTenantId());
```

One line replaces the entire group/policy/permission creation (which was 8+ Keycloak API calls under the hood).

### Step 4.3: Refactor `CourseService`

**File:** `details/src/main/java/com/aura/details/services/CourseService.java`

After:
```java
relationshipService.writeRelationship("course", course.getId(),
    "parent_outlet", "outlet", outletId);
```

### Step 4.4: Refactor `LessonService`

**File:** `details/src/main/java/com/aura/details/services/LessonService.java`

After (lesson under course):
```java
relationshipService.writeRelationship("lesson", lesson.getId(),
    "parent_course", "course", courseId);
```

After (standalone lesson under outlet):
```java
relationshipService.writeRelationship("lesson", lesson.getId(),
    "parent_outlet", "outlet", outletId);
```

### Step 4.5: Refactor `AccountService`

**File:** `details/src/main/java/com/aura/details/services/AccountService.java`

Before:
```java
String groupPath = AuthorizationGroupPath.institution(account.getTenantId());
groupAuthorizationService.addUserToGroup(createdAccount.getUserId(), groupPath);
```

After:
```java
relationshipService.writeRelationship("institution", account.getTenantId(),
    "admin", "user", createdAccount.getUserId());
```

Change the `GroupAuthorizationService` constructor dependency to `RelationshipAuthorizationService`.

### Step 4.6: Update service unit tests

Update all service tests to verify `RelationshipAuthorizationService` calls instead of `ResourceAuthorizationRegistrar` calls:

- `OnboardingServiceTest.java` — verify no registrar call (or verify institution relationship if needed)
- `OutletServiceTest.java` — verify `writeRelationship("outlet", id, "parent_institution", "institution", tenantId)`
- `CourseServiceTest.java` — verify `writeRelationship("course", id, "parent_outlet", "outlet", outletId)`
- `LessonServiceTest.java` — verify `writeRelationship("lesson", id, "parent_course"/"parent_outlet", ...)`
- `AccountServiceTest.java` — verify `writeRelationship("institution", tenantId, "admin", "user", userId)`

### Step 4.7: Verify

- `mvn clean test -pl core,details` — all unit tests pass.

---

## Phase 5: Application Layer — Permission Evaluator and Config

**Goal:** Replace `RptPermissionEvaluator` with `SpiceDbPermissionEvaluator` and wire all new beans.

### Step 5.1: Create `SpiceDbPermissionEvaluator`

**File (new):** `application/src/main/java/com/aura/app/security/SpiceDbPermissionEvaluator.java`

```java
@Slf4j
@RequiredArgsConstructor
public class SpiceDbPermissionEvaluator implements PermissionEvaluator {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
                                 Object permission) {
        log.warn("unsupported hasPermission(Object, Object) called");
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return false;
        }

        String userId = jwt.getSubject();
        String resourceId = targetId != null ? targetId.toString() : "";
        String scope = (String) permission;

        boolean granted = relationshipService.checkPermission(
            targetType, resourceId, scope, "user", userId);
        log.debug("SpiceDB permission check: {}:{}#{} for user {} = {}",
                  targetType, resourceId, scope, userId, granted);
        return granted;
    }
}
```

Key: All existing `@PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")` annotations work unchanged. The evaluator extracts `targetType` ("outlet"), `targetId` ("outlet-123"), and `permission` ("view") — exactly matching SpiceDB's `CheckPermission(outlet:outlet-123, "view", user:userId)`.

**Type-level checks** (no instance ID): When `targetId` is null, use an empty resourceId. The SpiceDB schema must handle this — type-level permissions (e.g., `create` on `outlet` type) are checked against the parent institution. This may require a controller-level change to pass the institution ID instead of null for create operations. Evaluate during implementation.

### Step 5.2: Delete old permission evaluator

**Delete:** `application/src/main/java/com/aura/app/security/RptPermissionEvaluator.java`

### Step 5.3: Create `SpiceDbConfig`

**File (new):** `application/src/main/java/com/aura/app/configs/SpiceDbConfig.java`

```java
@Configuration
public class SpiceDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spicedb")
    SpiceDbCredentials spiceDbCredentials() {
        return new SpiceDbCredentials();
    }

    @Bean
    ManagedChannel spiceDbChannel(SpiceDbCredentials credentials) {
        var builder = ManagedChannelBuilder.forTarget(credentials.getTarget());
        if (credentials.isPlaintext()) {
            builder.usePlaintext();
        } else {
            builder.useTransportSecurity();
        }
        return builder.build();
    }

    @Bean
    PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsServiceStub(
            ManagedChannel spiceDbChannel, SpiceDbCredentials credentials) {
        return PermissionsServiceGrpc.newBlockingStub(spiceDbChannel)
            .withCallCredentials(new BearerToken(credentials.getPresharedKey()));
    }

    @Bean
    SchemaServiceGrpc.SchemaServiceBlockingStub schemaServiceStub(
            ManagedChannel spiceDbChannel, SpiceDbCredentials credentials) {
        return SchemaServiceGrpc.newBlockingStub(spiceDbChannel)
            .withCallCredentials(new BearerToken(credentials.getPresharedKey()));
    }

    @Bean
    SpiceDbRelationshipService spiceDbRelationshipService(
            PermissionsServiceGrpc.PermissionsServiceBlockingStub stub) {
        return new SpiceDbRelationshipService(stub);
    }
}
```

### Step 5.4: Update `KeycloakClientConfig`

**File:** `application/src/main/java/com/aura/app/configs/KeycloakClientConfig.java`

Remove beans:
- `keycloakResourceAuthorizationService` — deleted class
- `keycloakGroupAuthorizationService` — deleted class

Keep beans:
- `getKeycloakClient` — still needed for identity management
- `getKeycloakCredentials` — still needed
- `getKeycloakRemoteIdentityService` — still needed for user creation
- `getKeycloakRealmResource`, `getUsersResource`, `getAuraApplicationClientResource` — still needed by `KeycloakIdentityService`

### Step 5.5: Update `ServiceConfig`

**File:** `application/src/main/java/com/aura/app/configs/ServiceConfig.java`

Changes:
- Remove `ResourceAuthorizationRegistrar` bean
- Update `OnboardingService` bean — remove registrar dependency, add `RelationshipAuthorizationService` if needed
- Update `OutletService` bean — `RelationshipAuthorizationService` instead of `ResourceAuthorizationRegistrar`
- Update `CourseService` bean — same
- Update `LessonService` bean — same
- Update `AccountService` bean — `RelationshipAuthorizationService` instead of `GroupAuthorizationService`

### Step 5.6: Update `WebSecurityConfig` (or wherever PermissionEvaluator is wired)

Wire `SpiceDbPermissionEvaluator` instead of `RptPermissionEvaluator`:

```java
@Bean
PermissionEvaluator permissionEvaluator(RelationshipAuthorizationService relationshipService) {
    return new SpiceDbPermissionEvaluator(relationshipService);
}
```

### Step 5.7: Add application config

**File:** `application/src/main/resources/application.yaml`

```yaml
spicedb:
  target: ${SPICEDB_TARGET:localhost:50051}
  preshared-key: ${SPICEDB_PRESHARED_KEY:}
  plaintext: ${SPICEDB_PLAINTEXT:true}
```

### Step 5.8: Update `PreAuthorizeConventionTest`

**File:** `application/src/test/java/.../PreAuthorizeConventionTest.java`

Update `VALID_RESOURCES` and `VALID_SCOPES` if needed. The `hasPermission(targetId, 'resource', 'scope')` format is unchanged, so this test likely needs minimal changes. Verify the valid scope list matches the SpiceDB schema permissions (view, manage, create, delegate).

### Step 5.9: Update application-layer unit tests

- Create: `SpiceDbPermissionEvaluatorTest.java` — mock `RelationshipAuthorizationService`, verify correct delegation
- Delete: `RptPermissionEvaluatorTest.java` (if exists)

### Step 5.10: Verify

- `mvn clean compile -pl application` — confirm application compiles.
- `mvn clean test -pl core,details,application` — all unit tests pass.

---

## Phase 6: Type-Level Permission Checks

**Goal:** Handle `@PreAuthorize` calls where `targetId` is null (type-level checks like `hasPermission(null, 'outlet', 'create')`).

### Problem

The current Keycloak approach has type-level resources (e.g., a resource named `outlet` with no instance ID) that grant blanket create/view permissions to system admins. SpiceDB is instance-based — there's no concept of a "type-level" resource.

### Solution Options

**Option A: Check against the parent resource instead.**

Change controller annotations from:
```java
@PreAuthorize("hasPermission(null, 'outlet', 'create')")
```
to:
```java
@PreAuthorize("hasPermission(#institutionId, 'institution', 'create')")
```

This means "can the user create things under this institution?" which is semantically more accurate and maps cleanly to SpiceDB's `institution:inst1#create`.

**Option B: Create a synthetic "system" resource in SpiceDB.**

Add to schema:
```zed
definition system {
    relation admin: user
    permission manage = admin
    permission create = admin
    permission view = admin
}
```

Write `system:global#admin@user:service-account-id` during bootstrap. Type-level checks become `checkPermission("system", "global", "create", "user", userId)`.

**Recommendation:** Option A is cleaner. It forces controllers to specify which parent resource the action applies to, which is more precise. Evaluate during implementation — some mutations already receive the parent ID (e.g., `createOutlet` receives `institutionId`), some may need signature changes.

### Step 6.1: Audit all `@PreAuthorize` annotations

Find every `hasPermission(null, ...)` call and determine which parent resource to check against. Document the mapping.

### Step 6.2: Update controller annotations and method signatures

For each type-level check, update to reference the parent resource. This may require adding parameters to some GraphQL mutations.

### Step 6.3: Update `PreAuthorizeConventionTest`

Ensure the test validates the new pattern.

### Step 6.4: Verify

- `mvn clean test -pl application` — ArchUnit and unit tests pass.

---

## Phase 7: Component Tests — SpiceDB TestContainer

**Goal:** Add SpiceDB as a test container alongside the existing Keycloak container (Keycloak is still needed for authentication). Update test infrastructure to use SpiceDB for authorization.

### Step 7.1: Add SpiceDB test container dependency

**File:** `component-tests/pom.xml`

```xml
<dependency>
    <groupId>com.authzed.api</groupId>
    <artifactId>authzed</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
    <scope>test</scope>
</dependency>
```

### Step 7.2: Create `SpiceDbTestContainer`

**File (new):** `component-tests/src/test/java/.../containers/spicedb/SpiceDbTestContainer.java`

```java
public class SpiceDbTestContainer {
    private static GenericContainer<?> container;

    public static GenericContainer<?> getSingleton() {
        if (container == null) {
            container = new GenericContainer<>("authzed/spicedb:latest")
                .withCommand("serve", "--grpc-preshared-key", "test-key", "--datastore-engine", "memory")
                .withExposedPorts(50051)
                .waitingFor(Wait.forLogMessage(".*grpc server started serving.*", 1));
            container.start();
        }
        return container;
    }
}
```

### Step 7.3: Create `SpiceDbInitializer`

**File (new):** `component-tests/src/test/java/.../initializers/SpiceDbInitializer.java`

Sets Spring properties (`spicedb.target`, `spicedb.preshared-key`, `spicedb.plaintext`) from the test container.

Also writes the schema to SpiceDB on startup (reads `schema.zed` from classpath and calls `SchemaServiceStub.writeSchema()`).

### Step 7.4: Update `CucumberTestConfig`

Add `SpiceDbInitializer` to the `@ContextConfiguration` initializers list.

### Step 7.5: Simplify `realm-import.json`

**File:** `component-tests/src/test/resources/keycloak/realm-import.json`

Remove:
- `authorizationSettings` section (all resources, policies, permissions, scopes)
- Nested groups (`system:1:admin`, `system:1:viewer` and their subtrees)
- Authorization-related client settings

Keep:
- Realm configuration (name, login settings, token settings)
- Client definition (`aura-application-client`) — but disable authorization services
- Service account user
- Roles (if used for identity purposes)

### Step 7.6: Simplify `DatabaseCleanupHooks`

**File:** `component-tests/src/test/java/.../hooks/DatabaseCleanupHooks.java`

Replace `cleanKeycloakResources()` with `cleanSpiceDbRelationships()`:

```java
private void cleanSpiceDbRelationships() {
    // Delete all relationships in SpiceDB
    // Re-write the schema (ensures clean state)
    // Or use DeleteRelationships with a wildcard filter
}
```

Keep `cleanDatabase()` unchanged.
Keep Keycloak user cleanup (test users still need to be removed from Keycloak).

### Step 7.7: Delete `RptTokenExchange`

**Delete:** `component-tests/src/test/java/.../utils/RptTokenExchange.java`

No longer needed — JWT is identity-only, no RPT exchange required.

### Step 7.8: Update step definitions

**Files:**
- `RoleStepDefs.java` — remove `RptTokenExchange.exchangeForRpt()` calls. Use the access token directly.
- `InstitutionStepDefs.java` — remove RPT exchange. Access token from Keycloak is sufficient (contains `sub` claim for user ID).
- `AccountStepDefs.java` — remove all RPT exchange calls.
- `AuthorizationStepDefs.java` — rewrite Keycloak policy/permission assertions to SpiceDB relationship assertions. Verify relationships exist in SpiceDB instead of checking Keycloak policies.

### Step 7.9: Update `application-component-test.yml`

Add SpiceDB config (will be overridden by `SpiceDbInitializer` but good to have defaults):

```yaml
spicedb:
  target: localhost:50051
  preshared-key: test-key
  plaintext: true
```

### Step 7.10: Update `authorization.feature`

**File:** `component-tests/src/test/resources/features/authorization.feature`

Rewrite to verify SpiceDB schema and relationships instead of Keycloak policies:

```gherkin
@Api
Feature: SpiceDB Authorization Schema

  @ComponentTest
  Scenario: SpiceDB schema supports hierarchical permissions
    Given SpiceDB is set up with the authorization schema
    Then the schema should define the following resource types:
      | type        |
      | institution |
      | outlet      |
      | course      |
      | lesson      |
      | account     |
      | level       |
      | subject     |
      | topic       |
```

### Step 7.11: Verify

- `mvn test -pl component-tests` — all component tests pass.

---

## Phase 8: Clean Up and Final Verification

**Goal:** Remove all dead code, verify the full build, and update documentation.

### Step 8.1: Remove dead imports and unused files

Scan for any remaining references to:
- `AuthorizationGroupPath`
- `AuthorizationNaming`
- `AuthorizationRoles`
- `GroupAuthorizationService`
- `ResourceAuthorizationService`
- `ResourceAuthorizationRegistrar`
- `RptPermissionEvaluator`
- `RptTokenExchange`
- `KeycloakGroupAuthorizationService`
- `KeycloakResourceAuthorizationService`

### Step 8.2: Remove Keycloak admin client dependencies (if no longer needed)

Evaluate whether `keycloak-admin-client` is still needed:
- **YES** if `KeycloakIdentityService` still uses it for user creation/lookup
- If yes, keep the dependency but note it's only for identity management

### Step 8.3: Update `CLAUDE.md`

Update the authorization section to reflect SpiceDB:
- Replace Keycloak authorization documentation with SpiceDB documentation
- Update "Adding a new use case" section — relationship writes instead of registrar calls
- Update component test documentation

### Step 8.4: Update ADR-004 status

Change status from "Proposed" to "Accepted".

### Step 8.5: Full build and test

```bash
mvn clean install -DskipTests          # Compilation check
mvn clean test -pl core                 # Core unit tests
mvn clean test -pl details              # Details unit tests
mvn clean test -pl application          # Application unit tests + ArchUnit
mvn test -pl component-tests            # Full integration tests
```

All tests must pass.

---

## Phase 9 (Deferred): Educator Role Assignment

**Goal:** Wire educator-specific assignment when the educator workflow is implemented.

When an educator is assigned to a course:
```java
relationshipService.writeRelationship("course", courseId,
    "educator", "user", educatorUserId);
```

When an educator is assigned to a specific lesson:
```java
relationshipService.writeRelationship("lesson", lessonId,
    "educator", "user", educatorUserId);
```

The SpiceDB schema already supports this — no schema changes needed. This phase only requires wiring the relationship writes in the educator assignment service methods.

---

## Summary of File Changes

### New Files (8)
| File | Module | Purpose |
|------|--------|---------|
| `core/clients/RelationshipAuthorizationService.java` | core | New authorization interface |
| `details/spicedb/SpiceDbCredentials.java` | details | Connection config |
| `details/spicedb/SpiceDbRelationshipService.java` | details | SpiceDB implementation |
| `application/security/SpiceDbPermissionEvaluator.java` | application | Spring Security integration |
| `application/configs/SpiceDbConfig.java` | application | Bean wiring |
| `application/resources/spicedb/schema.zed` | application | Authorization schema |
| `component-tests/containers/spicedb/SpiceDbTestContainer.java` | component-tests | Test container |
| `component-tests/initializers/SpiceDbInitializer.java` | component-tests | Test setup |

### Deleted Files (10)
| File | Module | Reason |
|------|--------|--------|
| `core/auth/AuthorizationGroupPath.java` | core | Replaced by SpiceDB schema |
| `core/auth/AuthorizationNaming.java` | core | Replaced by SpiceDB schema |
| `core/auth/AuthorizationRoles.java` | core | Replaced by SpiceDB schema |
| `core/clients/GroupAuthorizationService.java` | core | Replaced by RelationshipAuthorizationService |
| `core/clients/ResourceAuthorizationService.java` | core | No resource registration in SpiceDB |
| `details/keycloak/client/KeycloakGroupAuthorizationService.java` | details | Replaced by SpiceDbRelationshipService |
| `details/keycloak/client/KeycloakResourceAuthorizationService.java` | details | Deleted |
| `details/services/ResourceAuthorizationRegistrar.java` | details | Replaced by direct relationship writes |
| `application/security/RptPermissionEvaluator.java` | application | Replaced by SpiceDbPermissionEvaluator |
| `component-tests/utils/RptTokenExchange.java` | component-tests | No RPT needed |

### Modified Files (~20)
| File | Change |
|------|--------|
| `parent/pom.xml` | Add authzed + grpc dependencies |
| `details/pom.xml` | Add authzed + grpc dependencies |
| `application/pom.xml` | Add authzed + grpc dependencies |
| `component-tests/pom.xml` | Add authzed + grpc dependencies |
| `OnboardingService.java` | Replace registrar with relationship writes |
| `OutletService.java` | Replace registrar with relationship writes |
| `CourseService.java` | Replace registrar with relationship writes |
| `LessonService.java` | Replace registrar with relationship writes |
| `AccountService.java` | Replace group assignment with relationship writes |
| `KeycloakClientConfig.java` | Remove authorization service beans |
| `ServiceConfig.java` | Update bean wiring |
| `WebSecurityConfig.java` | Wire SpiceDbPermissionEvaluator |
| `application.yaml` | Add spicedb config |
| `application-component-test.yml` | Add spicedb config |
| `realm-import.json` | Remove authorization sections |
| `DatabaseCleanupHooks.java` | Replace Keycloak cleanup with SpiceDB cleanup |
| `RoleStepDefs.java` | Remove RPT exchange |
| `InstitutionStepDefs.java` | Remove RPT exchange |
| `AccountStepDefs.java` | Remove RPT exchange |
| `AuthorizationStepDefs.java` | Rewrite for SpiceDB assertions |
| `authorization.feature` | Rewrite for SpiceDB |
| `PreAuthorizeConventionTest.java` | Verify compatibility |
| `CLAUDE.md` | Update documentation |

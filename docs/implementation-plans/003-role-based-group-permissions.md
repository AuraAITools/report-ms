# Implementation Plan: Role-Based Group Permissions (ADR-003)

**ADR:** [003-role-based-group-permissions.md](../adr/003-role-based-group-permissions.md)  
**Date:** 2026-04-25  
**Goal:** Extend the nested group hierarchy (ADR-002) with parallel role subtrees. Each role (admin, viewer) gets its own group tree and its own resource permission per resource, with role-appropriate scopes.

**Depends on:** ADR-002 implementation (nested groups + Extend to Children) must be complete first.

---

## Current State (ADR-002)

Each resource has:
- 1 group (`:admin` only)
- 1 policy (admin only)
- 1 permission (all scopes: view, manage, delegate)

Group hierarchy:
```
/system:1:admin
  /institution:{id}:admin
    /outlet:{id}:admin
      /course:{id}:admin
        /lesson:{id}:admin
```

## Target State (ADR-003)

Each resource has:
- 2 groups (`:admin` + `:viewer`)
- 2 policies (admin + viewer)
- 2 permissions (admin with all scopes, viewer with view only)

Group hierarchy:
```
/system:1:admin                              ← extendChildren = true
  /institution:{id}:admin
    /outlet:{id}:admin
      /course:{id}:admin
        /lesson:{id}:admin

/system:1:viewer                             ← extendChildren = true (new)
  /institution:{id}:viewer
    /outlet:{id}:viewer
      /course:{id}:viewer
        /lesson:{id}:viewer
```

---

## Phase 1: Introduce Role Concept into Utilities

### Step 1.1: Update `AuthorizationGroupPath`

**File:** `core/src/main/java/com/aura/core/auth/AuthorizationGroupPath.java`

Add a `role` parameter to all methods. Keep the existing admin methods as convenience shortcuts.

```java
public final class AuthorizationGroupPath {

    public static final String SYSTEM_ADMIN_GROUP = "/system:1:admin";
    public static final String SYSTEM_VIEWER_GROUP = "/system:1:viewer";

    // Role-parameterised methods
    public static String systemGroup(String role) {
        return "/system:1:" + role;
    }

    public static String institution(String institutionId, String role) {
        return systemGroup(role) + "/institution:" + institutionId + ":" + role;
    }

    public static String outlet(String institutionId, String outletId, String role) {
        return institution(institutionId, role) + "/outlet:" + outletId + ":" + role;
    }

    public static String course(String institutionId, String outletId, String courseId, String role) {
        return outlet(institutionId, outletId, role) + "/course:" + courseId + ":" + role;
    }

    public static String lesson(String institutionId, String outletId, String courseId, String lessonId, String role) {
        return course(institutionId, outletId, courseId, role) + "/lesson:" + lessonId + ":" + role;
    }

    public static String lessonUnderOutlet(String institutionId, String outletId, String lessonId, String role) {
        return outlet(institutionId, outletId, role) + "/lesson:" + lessonId + ":" + role;
    }

    // Convenience: existing admin methods delegate to role-parameterised
    public static String institution(String institutionId) {
        return institution(institutionId, "admin");
    }

    public static String outlet(String institutionId, String outletId) {
        return outlet(institutionId, outletId, "admin");
    }

    // ... same pattern for course, lesson, lessonUnderOutlet
}
```

### Step 1.2: Update `AuthorizationNaming`

**File:** `core/src/main/java/com/aura/core/auth/AuthorizationNaming.java`

Add role-parameterised methods. The existing methods become `admin` convenience shortcuts.

```java
public final class AuthorizationNaming {

    public static final String SYSTEM_ADMIN_POLICY = "system-1-admin-policy";
    public static final String SYSTEM_VIEWER_POLICY = "system-1-viewer-policy";

    // Role-parameterised methods
    public static String policyName(String resourceType, String id, String role) {
        return resourceType + "-" + id + "-" + role + "-policy";
    }

    public static String permissionName(String resourceType, String id, String role) {
        return resourceType + "-" + id + "-" + role + "-permission";
    }

    public static String groupName(String resourceType, String id, String role) {
        return resourceType + ":" + id + ":" + role;
    }

    // Convenience: existing admin methods
    public static String policyName(String resourceType, String id) {
        return policyName(resourceType, id, "admin");
    }

    public static String permissionName(String resourceType, String id) {
        return permissionName(resourceType, id, "admin");
    }

    public static String groupName(String resourceType, String id) {
        return groupName(resourceType, id, "admin");
    }

    // resourceName is role-agnostic (resource is the same regardless of role)
    public static String resourceName(String resourceType, String id) {
        return resourceType + ":" + id;
    }
}
```

### Step 1.3: Define role scopes

**File:** `core/src/main/java/com/aura/core/auth/AuthorizationRoles.java` (new)

```java
public final class AuthorizationRoles {

    public static final String ADMIN = "admin";
    public static final String VIEWER = "viewer";

    // Scopes granted per role
    public static final Set<String> ADMIN_SCOPES = Set.of("view", "manage", "delegate", "create");
    public static final Set<String> VIEWER_SCOPES = Set.of("view");

    // All roles that should be created per resource
    public static final List<RoleDefinition> ALL_ROLES = List.of(
        new RoleDefinition(ADMIN, ADMIN_SCOPES),
        new RoleDefinition(VIEWER, VIEWER_SCOPES)
    );

    public record RoleDefinition(String name, Set<String> scopes) {}
}
```

### Step 1.4: Update unit tests

**File:** `core/src/test/java/com/aura/core/auth/AuthorizationGroupPathTest.java`

Add tests for the role-parameterised methods:
- `institution("inst-001", "viewer")` → `/system:1:viewer/institution:inst-001:viewer`
- Existing admin tests should still pass via convenience methods

**File:** `core/src/test/java/com/aura/core/auth/AuthorizationNamingTest.java`

Add tests for:
- `policyName("outlet", "001", "viewer")` → `outlet-001-viewer-policy`
- `permissionName("outlet", "001", "viewer")` → `outlet-001-viewer-permission`
- Existing admin tests should still pass

**File:** `core/src/test/java/com/aura/core/auth/AuthorizationRolesTest.java` (new)

Test that role definitions have the correct scopes.

---

## Phase 2: Create a Reusable Authorization Registration Service

Currently each service (OutletService, CourseService, LessonService) and `DefaultInstitutionOnboardingStrategy` manually constructs group/policy/permission names and makes Keycloak calls inline. With 2 roles, this logic doubles. Extract it into a shared service.

### Step 2.1: Create `ResourceAuthorizationRegistrar`

**File:** `details/src/main/java/com/aura/details/services/ResourceAuthorizationRegistrar.java` (new)

This service encapsulates the "register a resource in Keycloak for all roles" pattern:

```java
@RequiredArgsConstructor
public class ResourceAuthorizationRegistrar {

    private final ResourceAuthorizationService resourceAuthorizationService;
    private final GroupAuthorizationService groupAuthorizationService;

    /**
     * Register a resource in Keycloak with groups, policies, and permissions for all roles.
     *
     * @param resourceType    e.g., "outlet"
     * @param resourceUrn     e.g., "urn:aura:outlet"
     * @param resourceId      the entity ID
     * @param parentGroupPath the parent group path (role placeholder will be substituted)
     *                        e.g., "/system:1:{role}/institution:001:{role}"
     * @param ancestorPolicies map of role → set of ancestor policy names to link
     */
    public void registerResource(String resourceType, String resourceUrn, String resourceId,
                                  Map<String, String> parentGroupPathByRole,
                                  Map<String, Set<String>> ancestorPoliciesByRole) {

        String resourceName = AuthorizationNaming.resourceName(resourceType, resourceId);

        // 1. Register UMA resource (once, role-agnostic)
        Set<String> allScopes = AuthorizationRoles.ALL_ROLES.stream()
                .flatMap(r -> r.scopes().stream())
                .collect(Collectors.toSet());
        resourceAuthorizationService.registerResource(resourceName, resourceUrn, null, allScopes);

        // 2. For each role: create group, policy, permission
        for (AuthorizationRoles.RoleDefinition role : AuthorizationRoles.ALL_ROLES) {
            String groupName = AuthorizationNaming.groupName(resourceType, resourceId, role.name());
            String policyName = AuthorizationNaming.policyName(resourceType, resourceId, role.name());
            String permissionName = AuthorizationNaming.permissionName(resourceType, resourceId, role.name());
            String parentPath = parentGroupPathByRole.get(role.name());

            // Create group as subgroup of parent
            groupAuthorizationService.createSubGroup(parentPath, groupName);

            // Create group policy with extendToChildren
            String groupPath = parentPath + "/" + groupName;
            groupAuthorizationService.createGroupPolicy(policyName, groupPath, true);

            // Create permission with this role's scopes + ancestor policies
            Set<String> policies = new HashSet<>(ancestorPoliciesByRole.get(role.name()));
            policies.add(policyName);
            groupAuthorizationService.createResourcePermission(
                    permissionName, resourceName, role.scopes(), policies);
        }
    }
}
```

### Step 2.2: Wire the bean

**File:** `application/src/main/java/com/aura/app/configs/ServiceConfig.java`

```java
@Bean
ResourceAuthorizationRegistrar resourceAuthorizationRegistrar(
        ResourceAuthorizationService resourceAuthorizationService,
        GroupAuthorizationService groupAuthorizationService) {
    return new ResourceAuthorizationRegistrar(resourceAuthorizationService, groupAuthorizationService);
}
```

### Step 2.3: Unit test

**File:** `details/src/test/java/com/aura/details/services/ResourceAuthorizationRegistrarTest.java` (new)

Test that `registerResource`:
- Calls `registerResource` once with the union of all role scopes
- Creates 2 groups (admin + viewer) under the correct parent paths
- Creates 2 policies with extendToChildren = true
- Creates 2 permissions: admin with all scopes, viewer with view only
- Each permission links to the correct ancestor policies + its own policy

---

## Phase 3: Refactor Services to Use `ResourceAuthorizationRegistrar`

### Step 3.1: Update `DefaultInstitutionOnboardingStrategy`

**File:** `core/src/main/java/com/aura/core/usecases/onboarding/DefaultInstitutionOnboardingStrategy.java`

The onboarding strategy currently lives in `core/` which has no Spring dependencies. It calls `GroupAuthorizationService` and `ResourceAuthorizationService` directly. Two options:

**Option A:** Move the Keycloak registration out of the strategy and into `OnboardingService` (in `details/`), which can use `ResourceAuthorizationRegistrar`.

**Option B:** Keep inline registration in the strategy but loop over roles.

**Recommended: Option A.** The strategy saves the institution and loads initial data. The Keycloak registration moves to `OnboardingService.onboard()` which calls the strategy first, then registers the resource.

```java
// OnboardingService (details)
public Institution onboard(Institution institution) {
    Institution created = onboardInstitutionUseCase.onboard(institution);

    Map<String, String> parentPaths = Map.of(
        "admin", AuthorizationGroupPath.SYSTEM_ADMIN_GROUP,
        "viewer", AuthorizationGroupPath.SYSTEM_VIEWER_GROUP
    );
    Map<String, Set<String>> ancestorPolicies = Map.of(
        "admin", Set.of(AuthorizationNaming.SYSTEM_ADMIN_POLICY),
        "viewer", Set.of(AuthorizationNaming.SYSTEM_VIEWER_POLICY)
    );

    registrar.registerResource("institution", "urn:aura:institution",
            created.getId(), parentPaths, ancestorPolicies);

    return created;
}
```

`DefaultInstitutionOnboardingStrategy.onboard()` is stripped of all Keycloak calls — it only saves to DB and loads seed data.

### Step 3.2: Update `OutletService`

**File:** `details/src/main/java/com/aura/details/services/OutletService.java`

Replace inline Keycloak calls with `registrar.registerResource(...)`. The parent paths and ancestor policies are built per role:

```java
public Outlet createOutlet(Outlet outlet) {
    Outlet created = createOutletUseCase.createOutlet(outlet);
    String institutionId = created.getTenantId();
    String outletId = created.getId();

    Map<String, String> parentPaths = Map.of(
        "admin", AuthorizationGroupPath.institution(institutionId, "admin"),
        "viewer", AuthorizationGroupPath.institution(institutionId, "viewer")
    );
    Map<String, Set<String>> ancestorPolicies = Map.of(
        "admin", Set.of(
            AuthorizationNaming.SYSTEM_ADMIN_POLICY,
            AuthorizationNaming.policyName("institution", institutionId, "admin")),
        "viewer", Set.of(
            AuthorizationNaming.SYSTEM_VIEWER_POLICY,
            AuthorizationNaming.policyName("institution", institutionId, "viewer"))
    );

    registrar.registerResource("outlet", "urn:aura:outlet", outletId,
            parentPaths, ancestorPolicies);

    return created;
}
```

### Step 3.3: Update `CourseService`

**File:** `details/src/main/java/com/aura/details/services/CourseService.java`

Same pattern — parent is outlet, ancestor policies include institution + outlet + system per role.

### Step 3.4: Update `LessonService`

**File:** `details/src/main/java/com/aura/details/services/LessonService.java`

Same pattern — parent is course (or outlet for standalone), ancestor policies include all ancestors per role.

### Step 3.5: Update unit tests for all services

Update `OutletServiceTest`, `CourseServiceTest`, `LessonServiceTest`, `DefaultInstitutionOnboardingStrategyTest` to verify the new flow. Since the services now delegate to `ResourceAuthorizationRegistrar`, the service tests become simpler — they verify the registrar is called with the correct arguments.

### Step 3.6: Update `ServiceConfig` bean wiring

Replace `ResourceAuthorizationService` + `GroupAuthorizationService` dependencies in service beans with `ResourceAuthorizationRegistrar` where appropriate.

---

## Phase 4: Update `realm-import.json`

### Step 4.1: Add viewer group and policy

**File:** `component-tests/src/test/resources/keycloak/realm-import.json`

Add to the groups array:
```json
{
  "id": "b2c3d4e5-6f7a-8b9c-0d1e-f2a3b4c5d6e7",
  "name": "system:1:viewer",
  "path": "/system:1:viewer",
  "subGroups": [],
  "attributes": {},
  "realmRoles": [],
  "clientRoles": {}
}
```

Add to the policies array:
```json
{
  "name": "system-1-viewer-policy",
  "description": "Grants view access to users in the system:1:viewer group",
  "type": "group",
  "logic": "POSITIVE",
  "decisionStrategy": "UNANIMOUS",
  "config": {
    "groups": "[{\"id\":\"b2c3d4e5-6f7a-8b9c-0d1e-f2a3b4c5d6e7\",\"extendChildren\":true}]"
  }
}
```

### Step 4.2: Add type-level viewer permissions

Add viewer equivalents for each type-level permission:
- `system-1-institution-view-access` → scopes: view, policy: `system-1-viewer-policy`
- `system-1-outlet-view-access` → scopes: view, policy: `system-1-viewer-policy`
- `system-1-course-view-access`, `system-1-lesson-view-access`, etc.

### Step 4.3: Add service account to viewer group (optional)

The service account is already in `system:1:admin`. It doesn't need viewer membership since admin scopes are a superset.

---

## Phase 5: Update `DatabaseCleanupHooks`

### Step 5.1: Preserve viewer group during cleanup

**File:** `component-tests/src/test/java/com/aura/tests/component/hooks/DatabaseCleanupHooks.java`

Update the group cleanup to also preserve `system:1:viewer`:

```java
if (group.getName().equals("system:1:admin") || group.getName().equals("system:1:viewer")) {
    // Remove subgroups only
    ...
} else {
    adminClient.realm("aura").groups().group(group.getId()).remove();
}
```

Update the policy cleanup to also preserve `system-1-` prefixed policies (already done — the current filter `startsWith("system-1-")` covers both `system-1-admin-*` and `system-1-viewer-*`).

---

## Phase 6: Update Component Tests

### Step 6.1: Update `authorization.feature`

Add viewer policies to the verification:

```gherkin
Then Keycloak should have the following policies:
  | name                              | type     |
  | system-1-admin-policy             | group    |
  | system-1-viewer-policy            | group    |
  | system-1-institution-access       | resource |
  | system-1-institution-view-access  | resource |
  ...
```

### Step 6.2: Verify existing tests pass

Run `mvn test -pl component-tests`. Existing tests use the aura-admin/system-admin role — they should still work since admin permissions are unchanged.

### Step 6.3: Add viewer component test (new)

**File:** `component-tests/src/test/resources/features/viewer_access.feature` (new)

```gherkin
@Api
Feature: Viewer role access control

  @ComponentTest
  Scenario: Viewer can read institution details but cannot create outlets
    Given user has "aura-admin" role
    And an institution is onboarded
    And a viewer user is created and assigned to the institution
    When the viewer requests institution details
    Then the request should succeed
    When the viewer tries to create an outlet
    Then the request should fail with access denied
```

---

## Phase 7: Educator Assignment (ADR-002 Phase 9)

With the role-based structure in place, educator assignment becomes:

```java
// Assign educator as viewer of a specific course
groupAuthorizationService.addUserToGroup(userId,
    AuthorizationGroupPath.course(institutionId, outletId, courseId, "viewer"));
```

Or if educators need a distinct role with different scopes than viewers:

1. Add `EDUCATOR` to `AuthorizationRoles.ALL_ROLES` with scopes `[view, manage]`
2. All future resource creations automatically create the educator group/policy/permission
3. Backfill existing resources with educator permissions (31K new permissions)

This is deferred until the educator role scopes are defined.

---

## Execution Order

```
Phase 1 (utilities: AuthorizationGroupPath, AuthorizationNaming, AuthorizationRoles)
  └── Phase 2 (ResourceAuthorizationRegistrar)
        └── Phase 3 (refactor services to use registrar)
              └── Phase 4 (realm-import.json)
                    └── Phase 5 (DatabaseCleanupHooks)
                          └── Phase 6 (component tests)
                                └── Phase 7 (educator assignment — deferred)
```

Build and run unit tests after phases 1-3. Run component tests after phase 6.

---

## What Changes from ADR-002

| Aspect | ADR-002 (current) | ADR-003 (target) |
|---|---|---|
| Groups per resource | 1 (admin) | 2 (admin + viewer) |
| Policies per resource | 1 | 2 |
| Permissions per resource | 1 (all scopes) | 2 (admin: all scopes, viewer: view) |
| Keycloak API calls per resource | 4 | 7 (1 resource + 2 groups + 2 policies + 2 permissions) |
| Inline Keycloak calls in services | Yes | No — delegated to `ResourceAuthorizationRegistrar` |
| `AuthorizationGroupPath` methods | Admin-only | Role-parameterised + admin convenience |
| `AuthorizationNaming` methods | Admin-only | Role-parameterised + admin convenience |

## What Does NOT Change

- **`GroupAuthorizationService` interface** — no changes
- **`KeycloakGroupAuthorizationService` implementation** — no changes
- **`RptPermissionEvaluator`** — still reads `authorization.permissions` from RPT; scoped RPT unchanged
- **`@PreAuthorize` annotations** — still `hasPermission(#id, 'resource', 'scope')`; the scope is what differentiates admin vs viewer access at the controller level
- **Resource type URNs** — unchanged
- **Scoped RPT strategy** — unchanged

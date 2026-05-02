# Implementation Plan: Nested Groups with Extend to Children (ADR-002)

**ADR:** [002-hierarchical-authorization-with-nested-groups.md](../adr/002-hierarchical-authorization-with-nested-groups.md)  
**Date:** 2026-04-25  
**Goal:** Replace flat Keycloak groups + dual-policy AFFIRMATIVE linking with nested groups + "Extend to Children" policies. Register all resources (institution, outlet, course, lesson) in Keycloak. Use scoped RPT requests to prevent token bloat.

---

## Phase 1: Update `GroupAuthorizationService` Interface & Implementation

The foundation — all other phases depend on the new interface.

### Step 1.1: Update the core interface

**File:** `core/src/main/java/com/aura/core/clients/GroupAuthorizationService.java`

Change the interface from:

```java
public interface GroupAuthorizationService {
    void createGroup(String groupName);
    void createGroupPolicy(String policyName, String groupName);
    void createResourcePermission(String permissionName, String resourceName,
                                   Set<String> scopes, Set<String> policyNames);
    void addUserToGroup(String userId, String groupName);
}
```

To:

```java
public interface GroupAuthorizationService {
    void createGroup(String groupName);
    void createSubGroup(String parentGroupPath, String childGroupName);
    void createGroupPolicy(String policyName, String groupPath, boolean extendToChildren);
    void createResourcePermission(String permissionName, String resourceName,
                                   Set<String> scopes, Set<String> policyNames);
    void addUserToGroup(String userId, String groupPath);
    void removeUserFromGroup(String userId, String groupPath);
}
```

Changes:
- `createSubGroup` — creates a nested group under a parent identified by path (e.g., `"/institution:001:admin"`)
- `createGroupPolicy` — adds `boolean extendToChildren` parameter; when true, sets `extendChildren=true` on the `GroupPolicyRepresentation`
- `addUserToGroup` — now takes a group **path** (e.g., `"/institution:001:admin/outlet:005:admin"`) instead of a flat name, since nested groups require path-based lookup
- `removeUserFromGroup` — new method needed for revoking educator access

### Step 1.2: Update the Keycloak implementation

**File:** `details/src/main/java/com/aura/details/keycloak/client/KeycloakGroupAuthorizationService.java`

Key implementation changes:

1. **`createSubGroup`**: Use `groupsResource.group(parentGroupId).subGroup(childGroup)` to create a child group under a parent. The parent is found via `findGroupByPath`.

2. **`createGroupPolicy`**: Set `policy.setExtendChildren(extendToChildren)` on `GroupPolicyRepresentation` before creating. The `groupName` parameter becomes `groupPath` — use `findGroupByPath` instead of `findGroupByName`.

3. **`findGroupByPath`** (new private method): Replace `findGroupIdByName` with path-based lookup. Use `realmResource.getGroupByPath(path)` which returns a `GroupRepresentation` directly. This is the Keycloak admin client's native method for nested group lookup.

4. **`addUserToGroup` / `removeUserFromGroup`**: Use path-based lookup. `removeUserFromGroup` calls `usersResource.get(userId).leaveGroup(groupId)`.

5. **Constructor change**: Add `RealmResource realmResource` as a dependency (needed for `getGroupByPath`). Currently the class has `ClientResource`, `GroupsResource`, and `UsersResource`.

### Step 1.3: Update unit tests

**File:** `details/src/test/java/com/aura/details/keycloak/client/KeycloakGroupAuthorizationServiceTest.java`

Add/update tests:
- `createSubGroup_createsChildGroupUnderParent` — verify the subgroup is created under the correct parent
- `createGroupPolicy_withExtendToChildren_setsFlag` — verify `extendChildren=true` is set on the policy
- `createGroupPolicy_withoutExtendToChildren_doesNotSetFlag` — verify backward compat
- `addUserToGroup_withNestedPath_findsGroupByPath` — verify path-based lookup works
- `removeUserFromGroup_removesUserFromGroup` — verify leave group
- Update existing tests for the new method signatures

### Step 1.4: Update bean wiring

**File:** `application/src/main/java/com/aura/app/configs/KeycloakClientConfig.java`

Add `RealmResource` as a dependency to the `KeycloakGroupAuthorizationService` bean constructor (it's already available as a bean in this config class).

---

## Phase 2: Update Institution Onboarding

### Step 2.1: Update `DefaultInstitutionOnboardingStrategy`

**File:** `core/src/main/java/com/aura/core/usecases/onboarding/DefaultInstitutionOnboardingStrategy.java`

Change from:

```java
groupAuthorizationService.createGroup(groupName);
groupAuthorizationService.createGroupPolicy(policyName, groupName);
```

To:

```java
groupAuthorizationService.createGroup(groupName);
groupAuthorizationService.createGroupPolicy(policyName, "/" + groupName, true);  // extendToChildren = true
```

The institution group is a **top-level group** (not a subgroup), so `createGroup` stays the same. The policy creation changes to pass `extendToChildren = true` and use the path form `"/institution:{id}:admin"`.

No other changes — the permission creation stays the same (single policy link).

### Step 2.2: Write unit test for `DefaultInstitutionOnboardingStrategy`

**File:** `core/src/test/java/com/aura/core/usecases/onboarding/DefaultInstitutionOnboardingStrategyTest.java` (new file)

Test that `onboard()`:
- Saves institution with matching id and tenantId
- Calls `registerResource` with correct name, type, scopes
- Calls `createGroup` with `institution:{id}:admin`
- Calls `createGroupPolicy` with `extendToChildren = true`
- Calls `createResourcePermission` with correct policy name

---

## Phase 3: Update Outlet Creation

### Step 3.1: Update `OutletService.createOutlet()`

**File:** `details/src/main/java/com/aura/details/services/OutletService.java`

Change from:

```java
groupAuthorizationService.createGroup(outletGroupName);
groupAuthorizationService.createGroupPolicy(outletPolicyName, outletGroupName);
groupAuthorizationService.createResourcePermission(
    permissionName, resourceName,
    Set.of("view", "manage", "delegate"),
    Set.of(outletPolicyName, institutionPolicyName)
);
```

To:

```java
String institutionGroupPath = "/institution:" + institutionId + ":admin";

// Create outlet group as SUBGROUP of institution group
groupAuthorizationService.createSubGroup(institutionGroupPath, outletGroupName);

// Create outlet policy with Extend to Children
String outletGroupPath = institutionGroupPath + "/" + outletGroupName;
groupAuthorizationService.createGroupPolicy(outletPolicyName, outletGroupPath, true);

// Permission links to BOTH institution and outlet policies (AFFIRMATIVE)
groupAuthorizationService.createResourcePermission(
    permissionName, resourceName,
    Set.of("view", "manage", "delegate"),
    Set.of(outletPolicyName, institutionPolicyName)
);
```

Key changes:
- `createGroup` → `createSubGroup` (nested under institution)
- `createGroupPolicy` → pass `true` for `extendToChildren`
- Permission still links to both policies (AFFIRMATIVE) — institution admin gets access via institution policy, outlet admin gets access via outlet policy

### Step 3.2: Update `OutletService` unit tests

**File:** `details/src/test/java/com/aura/details/services/OutletServiceTest.java`

Update tests to verify:
- `createSubGroup` is called with correct parent path and child name
- `createGroupPolicy` is called with `extendToChildren = true`
- Permission still links to both policies

---

## Phase 4: Update Course Creation

### Step 4.1: Update `CourseService.createCourseInOutlet()`

**File:** `details/src/main/java/com/aura/details/services/CourseService.java`

The method currently creates flat groups and links to outlet policy. Change to nested groups.

The method needs `institutionId` (tenantId) to construct the full group path. The `Course` entity already has `tenantId` after creation. Update:

```java
public Course createCourseInOutlet(Course course, String outletId) {
    Course created = createCourseUseCase.createCourseInOutlet(course, outletId);

    String courseId = created.getId();
    String institutionId = created.getTenantId();
    String resourceName = "course:" + courseId;
    String courseGroupName = "course:" + courseId + ":admin";
    String coursePolicyName = "course-" + courseId + "-admin-policy";
    String permissionName = "course-" + courseId + "-permission";
    String institutionPolicyName = "institution-" + institutionId + "-admin-policy";
    String outletPolicyName = "outlet-" + outletId + "-admin-policy";

    // Group path: /institution:{instId}:admin/outlet:{outletId}:admin
    String outletGroupPath = "/institution:" + institutionId + ":admin/outlet:" + outletId + ":admin";

    // 1. Register UMA resource
    resourceAuthorizationService.registerResource(resourceName, COURSE_RESOURCE_TYPE, null,
            Set.of("view", "manage", "delegate"));

    // 2. Create course group as SUBGROUP of outlet group
    groupAuthorizationService.createSubGroup(outletGroupPath, courseGroupName);

    // 3. Create course group policy with Extend to Children
    String courseGroupPath = outletGroupPath + "/" + courseGroupName;
    groupAuthorizationService.createGroupPolicy(coursePolicyName, courseGroupPath, true);

    // 4. Permission links to institution + outlet + course policies (AFFIRMATIVE)
    groupAuthorizationService.createResourcePermission(permissionName, resourceName,
            Set.of("view", "manage", "delegate"),
            Set.of(coursePolicyName, outletPolicyName, institutionPolicyName));

    return created;
}
```

### Step 4.2: Update `CourseService` unit tests

**File:** `details/src/test/java/com/aura/details/services/CourseServiceTest.java`

Update tests to verify:
- `createSubGroup` called with outlet group path as parent
- `createGroupPolicy` called with `extendToChildren = true`
- Permission links to 3 policies (institution + outlet + course)

---

## Phase 5: Add Keycloak Authorization to Lesson Creation

Currently `LessonService` has NO Keycloak calls. Add the same pattern.

### Step 5.1: Update `LessonService`

**File:** `details/src/main/java/com/aura/details/services/LessonService.java`

Add `ResourceAuthorizationService` and `GroupAuthorizationService` as dependencies.

Update `createStandaloneLesson` and `createLesson` to register Keycloak resources:

```java
private void registerLessonAuthorization(String lessonId, String outletId, String tenantId, @Nullable String courseId) {
    String resourceName = "lesson:" + lessonId;
    String lessonGroupName = "lesson:" + lessonId + ":admin";
    String lessonPolicyName = "lesson-" + lessonId + "-admin-policy";
    String permissionName = "lesson-" + lessonId + "-permission";
    String institutionPolicyName = "institution-" + tenantId + "-admin-policy";
    String outletPolicyName = "outlet-" + outletId + "-admin-policy";

    // Build parent group path
    String parentGroupPath = "/institution:" + tenantId + ":admin/outlet:" + outletId + ":admin";
    Set<String> policyNames = new java.util.HashSet<>(Set.of(institutionPolicyName, outletPolicyName));

    if (courseId != null) {
        String coursePolicyName = "course-" + courseId + "-admin-policy";
        parentGroupPath += "/course:" + courseId + ":admin";
        policyNames.add(coursePolicyName);
    }

    // 1. Register UMA resource
    resourceAuthorizationService.registerResource(resourceName, LESSON_RESOURCE_TYPE, null,
            Set.of("view", "manage", "delegate"));

    // 2. Create lesson group as subgroup
    groupAuthorizationService.createSubGroup(parentGroupPath, lessonGroupName);

    // 3. Create lesson policy with Extend to Children
    String lessonGroupPath = parentGroupPath + "/" + lessonGroupName;
    groupAuthorizationService.createGroupPolicy(lessonPolicyName, lessonGroupPath, true);

    // 4. Lesson policy + all ancestor policies
    policyNames.add(lessonPolicyName);
    groupAuthorizationService.createResourcePermission(permissionName, resourceName,
            Set.of("view", "manage", "delegate"), policyNames);
}
```

Call `registerLessonAuthorization` from both `createStandaloneLesson` and `createLesson`.

### Step 5.2: Update `ServiceConfig.java` bean wiring

**File:** `application/src/main/java/com/aura/app/configs/ServiceConfig.java`

Add `ResourceAuthorizationService` and `GroupAuthorizationService` to the `LessonService` bean:

```java
@Bean
LessonService lessonService(CreateLessonUseCase createLessonUseCase,
                            AddEducatorsToLessonUseCase addEducatorsToLessonUseCase,
                            LinkLessonsToCourseUseCase linkLessonsToCourseUseCase,
                            ResourceAuthorizationService resourceAuthorizationService,
                            GroupAuthorizationService groupAuthorizationService) {
    return new LessonService(createLessonUseCase, addEducatorsToLessonUseCase,
            linkLessonsToCourseUseCase, resourceAuthorizationService, groupAuthorizationService);
}
```

### Step 5.3: Update/write `LessonService` unit tests

**File:** `details/src/test/java/com/aura/details/services/LessonServiceTest.java`

Add tests for Keycloak resource registration on lesson creation:
- `createStandaloneLesson_registersKeycloakResource`
- `createLesson_withCourse_registersKeycloakResourceUnderCourseGroup`
- `createLesson_withoutCourse_registersKeycloakResourceUnderOutletGroup`
- Verify `createSubGroup`, `createGroupPolicy(extendToChildren=true)`, and `createResourcePermission` with correct ancestor policies

---

## Phase 6: Update `DatabaseCleanupHooks` for Nested Groups

### Step 6.1: Update group cleanup logic

**File:** `component-tests/src/test/java/com/aura/tests/component/hooks/DatabaseCleanupHooks.java`

Current code iterates over top-level groups and deletes non-`aura-admin` groups. With nested groups, deleting the top-level institution group will cascade-delete all subgroups (outlet, course, lesson groups underneath).

The current logic should work as-is since it already deletes all top-level groups except `aura-admin`. When a top-level `institution:{id}:admin` group is deleted, Keycloak automatically deletes all its subgroups.

**Verify:** No code change needed if Keycloak cascade-deletes subgroups (it does). Add a log statement to confirm nested cleanup:

```java
log.info("Removed {} top-level groups (subgroups cascade-deleted): {}", removedGroups.size(), removedGroups);
```

---

## Phase 7: Update `realm-import.json`

### Step 7.1: Verify static aura-admin permissions cover all resource types

**File:** `component-tests/src/test/resources/keycloak/realm-import.json`

The realm import already has type-level aura-admin permissions for institution, outlet, course, lesson. Verify these still work with the nested group structure. They should — aura-admin uses a role-based policy (`aura-admin-policy`), not a group policy, so it's unaffected by group restructuring.

No changes expected unless the permission names or resource type URNs change.

---

## Phase 8: Update Component Tests

### Step 8.1: Verify existing tests still pass

Run `mvn test -pl component-tests` after all code changes. The existing tests create institutions and outlets — they should now create nested groups instead of flat groups.

Key tests to watch:
- `onboard_institution` — creates institution group (now with Extend to Children)
- `create_outlet` — creates outlet subgroup (now nested under institution)
- `create_course` — creates course subgroup (now nested under outlet)
- `create_lesson` / `create_standalone_lesson` — now creates Keycloak resources (previously DB-only)
- `create_institution_admin_account` — adds user to institution group (path-based lookup now)

### Step 8.2: Add educator assignment component test (new)

**Files:**
- `component-tests/src/test/resources/features/assign_educator_to_course.feature` (new)
- `component-tests/src/test/resources/files/assign-educator-to-course.graphql` (new — if a GraphQL mutation exists)
- `component-tests/src/test/java/.../stepdefs/EducatorAssignmentStepDefs.java` (new)

Test scenario:
```gherkin
@Api
Feature: Assign educator to course

  @ComponentTest
  Scenario: Institution admin assigns educator to a specific course
    Given the following institution is onboarded
      | name       | email              |
      | Test Inst  | test@example.com   |
    And the user switches to the institution tenant
    And the following outlet is created
      | name         |
      | Test Outlet  |
    And the following course is created in the outlet
      | name         |
      | Math 101     |
    And an educator account is created
    When the educator is assigned to course "Math 101"
    Then the educator should have access to course "Math 101"
    And the educator should not have access to other courses in the outlet
```

This test verifies that the nested group assignment correctly scopes educator access.

---

## Phase 9: Educator Assignment Flow

### Step 9.1: Add educator-to-course group assignment

When an educator is assigned to a course, add them to the course's nested group so they get access via the course policy.

**File:** `details/src/main/java/com/aura/details/services/AccountService.java` (or a new `EducatorAssignmentService`)

```java
public void assignEducatorToCourse(String userId, String courseId, String outletId, String institutionId) {
    String courseGroupPath = "/institution:" + institutionId + ":admin/outlet:" + outletId + ":admin/course:" + courseId + ":admin";
    groupAuthorizationService.addUserToGroup(userId, courseGroupPath);
}

public void assignEducatorToLesson(String userId, String lessonId, String courseId, String outletId, String institutionId) {
    String lessonGroupPath = "/institution:" + institutionId + ":admin/outlet:" + outletId + ":admin/course:" + courseId + ":admin/lesson:" + lessonId + ":admin";
    groupAuthorizationService.addUserToGroup(userId, lessonGroupPath);
}
```

### Step 9.2: Add revocation

```java
public void revokeEducatorFromCourse(String userId, String courseId, String outletId, String institutionId) {
    String courseGroupPath = "/institution:" + institutionId + ":admin/outlet:" + outletId + ":admin/course:" + courseId + ":admin";
    groupAuthorizationService.removeUserFromGroup(userId, courseGroupPath);
}
```

---

## Phase 10: Build a Group Path Utility

Multiple services now construct group paths manually. Extract a utility to prevent string construction errors.

### Step 10.1: Create `AuthorizationGroupPath`

**File:** `core/src/main/java/com/aura/core/authorization/AuthorizationGroupPath.java` (new)

```java
public class AuthorizationGroupPath {
    public static String institution(String institutionId) {
        return "/institution:" + institutionId + ":admin";
    }

    public static String outlet(String institutionId, String outletId) {
        return institution(institutionId) + "/outlet:" + outletId + ":admin";
    }

    public static String course(String institutionId, String outletId, String courseId) {
        return outlet(institutionId, outletId) + "/course:" + courseId + ":admin";
    }

    public static String lesson(String institutionId, String outletId, String courseId, String lessonId) {
        return course(institutionId, outletId, courseId) + "/lesson:" + lessonId + ":admin";
    }
}
```

Also add a naming utility for policies and permissions:

```java
public class AuthorizationNaming {
    public static String policyName(String resourceType, String id) {
        return resourceType + "-" + id + "-admin-policy";
    }

    public static String permissionName(String resourceType, String id) {
        return resourceType + "-" + id + "-permission";
    }

    public static String resourceName(String resourceType, String id) {
        return resourceType + ":" + id;
    }

    public static String groupName(String resourceType, String id) {
        return resourceType + ":" + id + ":admin";
    }
}
```

### Step 10.2: Refactor services to use the utility

Update `DefaultInstitutionOnboardingStrategy`, `OutletService`, `CourseService`, `LessonService` to use `AuthorizationGroupPath` and `AuthorizationNaming` instead of inline string construction.

### Step 10.3: Unit test the utility

**File:** `core/src/test/java/com/aura/core/authorization/AuthorizationGroupPathTest.java` (new)

Test that paths are constructed correctly for each level, including the full depth case.

---

## Execution Order

The phases have dependencies:

```
Phase 1 (interface + impl)
  ├── Phase 2 (institution onboarding)
  ├── Phase 3 (outlet creation)
  ├── Phase 4 (course creation)
  ├── Phase 5 (lesson creation)
  └── Phase 10 (path utility — can be done early and used in phases 2-5)
Phase 6 (cleanup hooks) — after phases 2-5
Phase 7 (realm-import) — after phases 2-5
Phase 8 (component tests) — after all above
Phase 9 (educator assignment) — after phase 8
```

Recommended order: **1 → 10 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9**

Build and run unit tests after each phase. Run component tests after phase 8.

---

## What Does NOT Change

- **`RptPermissionEvaluator`** — still reads `authorization.permissions` from the RPT JWT. Scoped RPTs just have fewer entries; the evaluator doesn't care.
- **`@PreAuthorize` annotations on controllers** — still `hasPermission(#id, 'resource', 'scope')`. The permission evaluation is unchanged.
- **`PreAuthorizeConventionTest`** — ArchUnit tests are unchanged.
- **`UserRequestContextInterceptor`** — JWT extraction is unchanged.
- **`WebSecurityConfig`** — OAuth2 resource server config is unchanged.
- **Resource type URNs** — `urn:aura:institution`, `urn:aura:outlet`, `urn:aura:course`, `urn:aura:lesson` stay the same.

## Frontend Changes (Out of Scope for Backend)

The frontend must switch from all-permissions RPT to scoped RPT requests. This is a frontend concern documented in ADR-002 but not implemented in this backend plan. The backend is unaffected — it validates whatever RPT it receives.

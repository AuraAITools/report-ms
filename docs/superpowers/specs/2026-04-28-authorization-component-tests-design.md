# Authorization Component Tests Design

## Goal

Write component tests that verify the SpiceDB authorization model for all roles and edge cases. Each test creates a user with a specific SpiceDB role, makes API requests, and asserts access granted or denied.

## Constraints

- Run only the specific scenario under test (not all component tests) — the user has a known bug in another test that stalls
- Command pattern: `mvn test -pl component-tests -Dcucumber.filter.tags="@TagName"`
- `DatabaseCleanupHooks` already cleans SpiceDB relationships and re-creates `system:global#admin` before each scenario — no additional cleanup needed

## New Step Definitions

### AuthRoleStepDefs (new file)

Provides reusable steps for assigning SpiceDB roles and switching user identity.

**Steps:**

1. `Given user "{email}" has "{role}" role on {resourceType} "{resourceId}"`
   - Creates user in Keycloak via admin API (email as username, set temporary password)
   - Gets the user's Keycloak ID
   - Writes SpiceDB relationship: `{resourceType}:{resourceId}#{role}@user:{userId}`
   - Does NOT switch identity — user still acts as current identity until explicitly switched

2. `Given user switches to "{email}" identity`
   - Authenticates as the user via Keycloak password grant (resource owner flow)
   - Sets the resulting JWT on the GraphQL client
   - All subsequent requests use this user's identity

3. `Given user switches back to "aura-admin" identity`
   - Switches GraphQL client back to service account token
   - Reuse existing `keycloak.tokenManager().getAccessToken().getToken()` pattern

**Keycloak password grant details:**
- Create user with `CredentialRepresentation` (type=password, value="test-password", temporary=false)
- Authenticate via `KeycloakBuilder` with grantType=PASSWORD, username=email, password="test-password"
- Client: `aura-application-client` with client secret from `KeycloakTestContainer`

### Missing "tries" step variants (add to existing step def files)

These steps wrap mutations in try-catch and store error state in TestContext:

- `user tries to create a course with details:` — add to `CourseStepDefs`
- `user tries to create a subject with name {string}` — add to `SubjectStepDefs`
- `user tries to create a topic with name {string}` — add to `TopicStepDefs`
- `user tries to query their institution` — add to `QueryStepDefs` (or reuse existing, which already catches errors)
- `user tries to query outlets by page` — add to `QueryStepDefs`
- `user tries to query levels by page` — add to `QueryStepDefs`

## Feature Files

All feature files tagged `@Api` (feature level) and each scenario tagged with both `@ComponentTest` and a role-specific tag (e.g., `@AuthPlatformAdmin`).

### 1. `auth_platform_admin.feature` — Tag: `@AuthPlatformAdmin`

Tests that system admin (the pre-existing aura-admin) can perform all operations.

**Scenarios:**
- Platform admin can create institution (already tested elsewhere, skip)
- Platform admin can create outlet under onboarded institution
- Platform admin can create course under outlet
- Platform admin can create standalone lesson under outlet
- Platform admin can create subject
- Platform admin can create topic
- Platform admin can view institution
- Platform admin can query outlets by page
- Platform admin can query levels by page

### 2. `auth_institution_admin.feature` — Tag: `@AuthInstitutionAdmin`

Tests institution admin permissions and restrictions.

**Setup per scenario:** aura-admin onboards institution, creates outlet (for children tests). Then a test user gets `admin` role on the institution.

**Scenarios (access granted):**
- Institution admin can view their institution
- Institution admin can create outlet
- Institution admin can create subject
- Institution admin can create topic
- Institution admin can create institution admin account
- Institution admin can query outlets by page
- Institution admin can query levels by page

**Scenarios (access denied):**
- Institution admin cannot create institution
- Institution admin cannot view another institution (cross-tenant isolation)

### 3. `auth_institution_viewer.feature` — Tag: `@AuthInstitutionViewer`

**Setup per scenario:** aura-admin onboards institution, creates outlet. Test user gets `viewer` role on institution.

**Scenarios (access granted):**
- Institution viewer can view their institution
- Institution viewer can query outlets by page
- Institution viewer can query levels by page

**Scenarios (access denied):**
- Institution viewer cannot create outlet
- Institution viewer cannot create subject
- Institution viewer cannot create institution admin account

### 4. `auth_outlet_admin.feature` — Tag: `@AuthOutletAdmin`

**Setup per scenario:** aura-admin onboards institution, creates outlet. Test user gets `admin` role on the outlet.

**Scenarios (access granted):**
- Outlet admin can create course under their outlet
- Outlet admin can create standalone lesson under their outlet

**Scenarios (access denied):**
- Outlet admin cannot create institution
- Outlet admin cannot create outlet (requires institution-level permission)

### 5. `auth_outlet_modifier.feature` — Tag: `@AuthOutletModifier`

**Setup per scenario:** aura-admin onboards institution, creates outlet. Test user gets `modifier` role on outlet.

**Scenarios (access granted):**
- Outlet modifier can create course under their outlet
- Outlet modifier can create standalone lesson under their outlet

**Scenarios (access denied):**
- Outlet modifier cannot create outlet (requires institution-level permission)

### 6. `auth_outlet_viewer.feature` — Tag: `@AuthOutletViewer`

**Setup per scenario:** aura-admin onboards institution, creates outlet. Test user gets `viewer` role on outlet.

**Scenarios (access granted):**
- Outlet viewer can query outlets by page (via institution view cascade)

**Scenarios (access denied):**
- Outlet viewer cannot create course
- Outlet viewer cannot create standalone lesson

### 7. `auth_educator.feature` — Tag: `@AuthEducator`

Tests the cascade boundary — educators see institution + outlets but NOT courses/lessons until assigned.

**Setup per scenario:** aura-admin onboards institution, creates outlet, creates course. Test user gets `educator` role on institution.

**Scenarios (access granted):**
- Educator can view their institution
- Educator can query outlets by page

**Scenarios (access denied):**
- Educator cannot create outlet
- Educator cannot create course

**Scenarios (cascade boundary):**
- Educator cannot view course until assigned as course educator
- After assigning educator role on course, educator can view (future — no getCourse query exists yet, skip)

### 8. `auth_parent.feature` — Tag: `@AuthParent`

**Setup per scenario:** aura-admin onboards institution, creates outlet. Test user gets `parent_user` role on institution.

**Scenarios (access granted):**
- Parent can view their institution
- Parent can query outlets by page

**Scenarios (access denied):**
- Parent cannot create outlet
- Parent cannot create course
- Parent cannot create institution admin account

## Test Execution

Run a specific role's tests:
```bash
mvn test -pl component-tests -Dcucumber.filter.tags="@AuthInstitutionAdmin"
```

Run all auth tests:
```bash
mvn test -pl component-tests -Dcucumber.filter.tags="@AuthPlatformAdmin or @AuthInstitutionAdmin or @AuthInstitutionViewer or @AuthOutletAdmin or @AuthOutletModifier or @AuthOutletViewer or @AuthEducator or @AuthParent"
```

## File Changes Summary

| File | Change |
|---|---|
| `component-tests/.../stepdefs/AuthRoleStepDefs.java` | New — role assignment and identity switching steps |
| `component-tests/.../stepdefs/CourseStepDefs.java` | Add `user tries to create a course with details:` |
| `component-tests/.../stepdefs/SubjectStepDefs.java` | Add `user tries to create a subject with name {string}` |
| `component-tests/.../stepdefs/TopicStepDefs.java` | Add `user tries to create a topic with name {string}` |
| `component-tests/.../stepdefs/QueryStepDefs.java` | Add `user tries to query outlets by page` and `user tries to query levels by page` |
| `component-tests/src/test/resources/features/auth_platform_admin.feature` | New |
| `component-tests/src/test/resources/features/auth_institution_admin.feature` | New |
| `component-tests/src/test/resources/features/auth_institution_viewer.feature` | New |
| `component-tests/src/test/resources/features/auth_outlet_admin.feature` | New |
| `component-tests/src/test/resources/features/auth_outlet_modifier.feature` | New |
| `component-tests/src/test/resources/features/auth_outlet_viewer.feature` | New |
| `component-tests/src/test/resources/features/auth_educator.feature` | New |
| `component-tests/src/test/resources/features/auth_parent.feature` | New |

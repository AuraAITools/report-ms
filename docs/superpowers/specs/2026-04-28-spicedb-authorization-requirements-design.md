# SpiceDB Authorization Requirements Design

## Goal

Update the SpiceDB schema, controller annotations, and service relationship writes to implement the full authorization model for all user roles: platform admin, institution admin/viewer, outlet admin/viewer/modifier, course admin/viewer, lesson admin/viewer, educator, and parent.

## Current State

- SpiceDB schema defines resource types but lacks: `parent_system` cascade, `modifier` relation, `educator`/`parent` on institution, `admin`/`viewer` on lesson
- All create mutations use type-level checks (`hasPermission(null, 'resource', 'create')`) routing to `system:global` -- only platform admins can create anything
- Missing relationship writes for: levels, subjects, topics, institution-to-system link
- `PreAuthorizeConventionTest` validates annotations but only allows scopes: `view`, `create`, `manage`

## Authorization Requirements

### Permission Mapping

| Abbreviation | SpiceDB Permission |
|---|---|
| C (Create) | `create_<child_type>` on parent resource |
| R (Read) | `view` |
| U (Update) | `manage` |
| D (Delete) | `manage` |
| De (Delegate) | `delegate` |

### Role Permissions

| Role | Own Resource | Child Resources |
|---|---|---|
| Platform admin (system admin) | -- | CRUD all institutions + all descendants, De on platform admin group |
| Institution admin | R,U,D | CRUD all children |
| Institution viewer | R | R all children |
| Outlet admin | R,U | CRUD children, De on outlet admin group |
| Outlet viewer | R | R children |
| Outlet modifier | R,U | CRUD children (no De) |
| Course admin | R,U | CRUD children |
| Course viewer | R | R children |
| Lesson admin | R,U | CRUD children |
| Lesson viewer | R | R children |
| Educator | R institution, R outlets, R subjects/topics | No course/lesson access until explicitly assigned |
| Parent | R institution, R outlets | No course/lesson access until explicitly assigned to specific instances |

### Educator/Parent Cascade Boundary

Educators and parents get institution-wide read access to institutions, outlets, subjects, and topics. They **cannot** see courses or lessons until explicitly assigned (e.g., educator assigned as `course:{id}#educator`).

This requires a **two-tier view permission**:
- `view` -- includes educator/parent (used for institution and outlet)
- `view_all` -- excludes educator/parent (used for cascading into courses and below)

## Schema Design

```zed
definition user {}

definition system {
    relation admin: user

    permission manage = admin
    permission create_institution = admin
    permission view = admin
    permission delegate = admin
}

definition institution {
    relation parent_system: system
    relation admin: user
    relation viewer: user
    relation educator: user
    relation parent_user: user

    permission manage = admin + parent_system->manage
    permission create_outlet = admin + parent_system->manage
    permission create_account = admin + parent_system->manage
    permission create_subject = admin + parent_system->manage
    permission create_level = admin + parent_system->manage
    permission view = viewer + admin + educator + parent_user + parent_system->view
    permission view_all = viewer + admin + parent_system->view
    permission delegate = admin + parent_system->delegate
}

definition outlet {
    relation parent_institution: institution
    relation admin: user
    relation viewer: user
    relation modifier: user

    permission manage = admin + modifier + parent_institution->manage
    permission create_course = admin + modifier + parent_institution->manage
    permission create_lesson = admin + modifier + parent_institution->manage
    permission view = viewer + admin + modifier + parent_institution->view
    permission view_all = viewer + admin + modifier + parent_institution->view_all
    permission delegate = admin + parent_institution->delegate
}

definition course {
    relation parent_outlet: outlet
    relation admin: user
    relation educator: user
    relation viewer: user

    permission manage = admin + educator + parent_outlet->manage
    permission create_lesson = admin + parent_outlet->manage
    permission view = viewer + educator + admin + parent_outlet->view_all
    permission delegate = admin + parent_outlet->delegate
}

definition lesson {
    relation parent_course: course
    relation parent_outlet: outlet
    relation admin: user
    relation educator: user
    relation viewer: user

    permission manage = admin + educator + parent_course->manage + parent_outlet->manage
    permission view = viewer + admin + educator + parent_course->view + parent_outlet->view_all
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
    relation parent_institution: institution

    permission manage = parent_institution->manage
    permission view = parent_institution->view
}
```

### Key Schema Decisions

- **`parent_system` on institution**: Platform admin permissions cascade automatically to all institutions and descendants without writing per-institution relationships.
- **`view` vs `view_all`**: Educator/parent are included in `view` (institution + outlet) but excluded from `view_all` (courses + lessons). Courses and lessons inherit `parent->view_all`.
- **`modifier` on outlet**: Same permissions as `admin` minus `delegate`.
- **`create_<type>` permissions**: Resource-specific create permissions checked on the parent resource (e.g., `institution#create_outlet`).
- **`admin` + `viewer` on lesson**: Previously only had `educator`. Now supports lesson-admin and lesson-viewer groups.

## Controller Annotation Changes

### New Pattern

Create operations check `create_<type>` on the **parent** resource instead of null/system:global. The parent resource ID is extracted from the input object via SpEL dot notation.

Only `createInstitution` remains type-level (null -> system:global) since institutions have no parent below system.

### Annotation Mapping

| Controller Method | Current | New |
|---|---|---|
| `createInstitution` | `hasPermission(null, 'institution', 'create')` | `hasPermission(null, 'institution', 'create_institution')` |
| `createOutlet` | `hasPermission(null, 'outlet', 'create')` | `hasPermission(#createOutletInput.institutionId, 'institution', 'create_outlet')` |
| `getOutletsByPage` | `hasPermission(#institutionId, 'outlet', 'view')` | `hasPermission(#institutionId, 'institution', 'view')` |
| `createCourse` | `hasPermission(null, 'course', 'create')` | `hasPermission(#createCourseInput.outletId, 'outlet', 'create_course')` |
| `createLesson` | `hasPermission(null, 'lesson', 'create')` | `hasPermission(#createLessonInput.outletId, 'outlet', 'create_lesson')` |
| `createStandaloneLesson` | `hasPermission(null, 'lesson', 'create')` | `hasPermission(#createStandaloneLessonInput.outletId, 'outlet', 'create_lesson')` |
| `createSubject` | `hasPermission(null, 'subject', 'create')` | `hasPermission(#createSubjectInput.institutionId, 'institution', 'create_subject')` |
| `createSubjectTopicLinks` | `hasPermission(null, 'subject', 'manage')` | `hasPermission(#createSubjectTopicLinksInput.institutionId, 'institution', 'manage')` |
| `createTopic` | `hasPermission(null, 'topic', 'create')` | `hasPermission(#topicInput.institutionId, 'institution', 'create_subject')` |
| `getLevelByPage` | `hasPermission(#institutionId, 'level', 'view')` | `hasPermission(#institutionId, 'institution', 'view')` |
| `getInstitution` | `hasPermission(#institutionId, 'institution', 'view')` | no change |
| `getUserInstitutions` | `hasPermission(#institutionId, 'institution', 'view')` | no change |
| `createAccountWithInstitutionAdminRole` | `hasPermission(null, 'account', 'create')` | `hasPermission(#createAccountInput.institutionId, 'institution', 'create_account')` |
| `createParentAccount` | `hasPermission(null, 'account', 'create')` | `hasPermission(#createParentAccountInput.institutionId, 'institution', 'create_account')` |
| `createStudentsInParentAccount` | `hasPermission(null, 'account', 'create')` | `hasPermission(null, 'institution', 'create_account')` |
| `createEducatorsInAccount` | `hasPermission(null, 'account', 'create')` | `hasPermission(null, 'institution', 'create_account')` |
| `addUserToGroup` | `hasPermission(null, 'account', 'create')` | `hasPermission(#addUserToGroupInput.institutionId, 'institution', 'delegate')` |

### SpEL Dot Notation

For input objects like `CreateOutletInput`, the parent ID is accessed via SpEL: `#createOutletInput.institutionId`. This calls `getInstitutionId()` at runtime. The `PreAuthorizeConventionTest` param reference check needs updating to support dot notation (extract root variable name before `.`).

### View Permission Simplification

`getOutletsByPage` and `getLevelByPage` currently check the child resource type (`outlet`/`level`) with `view`. Since `view` permission cascades from institution, these can check `institution` directly -- the hierarchy handles the rest.

## Service Relationship Writes

### Currently Implemented

| Service | Relationship Written |
|---|---|
| `AccountService.createAccountWithInstitutionAdminRole` | `institution:{tenantId}#admin@user:{userId}` |
| `OutletService.createOutlet` | `outlet:{id}#parent_institution@institution:{tenantId}` |
| `CourseService.createCourseInOutlet` | `course:{id}#parent_outlet@outlet:{outletId}` |
| `LessonService.createLesson` (with course) | `lesson:{id}#parent_course@course:{courseId}` |
| `LessonService.createLesson` (no course) | `lesson:{id}#parent_outlet@outlet:{outletId}` |
| `LessonService.createStandaloneLesson` | `lesson:{id}#parent_outlet@outlet:{outletId}` |

### New Relationship Writes Needed

| Service/Location | Relationship | Notes |
|---|---|---|
| `OnboardingService` (or `TenantInitialDataLoaderImpl`) | `institution:{id}#parent_system@system:global` | Written when institution is created during onboarding |
| `SubjectService.createSubject` | `subject:{id}#parent_institution@institution:{tenantId}` | Currently missing |
| `TopicService.createTopic` | `topic:{id}#parent_institution@institution:{tenantId}` | Currently missing; changed from `parent_subject` to `parent_institution` since topics link to subjects many-to-many |
| Level creation (in `TenantInitialDataLoaderImpl`) | `level:{id}#parent_institution@institution:{tenantId}` | Levels are created during onboarding from templates |
| `AccountService.createParentAccount` | `institution:{tenantId}#parent_user@user:{userId}` | Assign parent role on institution |
| `AccountService.createEducatorsInAccount` | `institution:{tenantId}#educator@user:{userId}` | Assign educator role on institution |

### Existing Writes -- No Changes

The existing relationship writes for outlets, courses, and lessons remain unchanged.

## SpiceDbPermissionEvaluator Changes

The evaluator currently routes `null` targetId to `system:global`. This behavior stays -- `createInstitution` still uses `hasPermission(null, 'institution', 'create_institution')` which checks `system:global#create_institution`.

No changes needed to the evaluator itself. The permission name in the annotation maps directly to the SpiceDB permission name.

## PreAuthorizeConventionTest Changes

### Updated VALID_SCOPES

```java
private static final Set<String> VALID_SCOPES = Set.of(
    "view", "manage", "delegate",
    "create_institution", "create_outlet", "create_course",
    "create_lesson", "create_account", "create_subject"
);
```

### Updated VALID_RESOURCES

No change -- resources remain: `institution`, `outlet`, `course`, `lesson`, `level`, `subject`, `topic`, `account`.

### Dot Notation Support

The `param_references_must_match_method_parameters` test currently extracts `#paramName` and checks it against `@Argument` names. Update to handle dot notation: extract root name (before first `.`) and match against either `@Argument` names or method parameter names.

### Type-Level Check Update

The `type_level_checks_must_use_null_first_arg` test filters annotations without `#` and asserts `null`. Since most create operations now use `#input.field`, this test naturally excludes them (they contain `#`). Only `createInstitution`, `createStudentsInParentAccount`, and `createEducatorsInAccount` still use `null`.

## DatabaseCleanupHooks

The `cleanSpiceDbRelationships` method already deletes all relationships and re-creates `system:global#admin`. No changes needed since the cleanup is comprehensive.

## Component Test Impact

Existing component tests that create entities (outlets, courses, lessons, etc.) will need the `system:global#admin` relationship to cascade properly through the new `parent_system` chain. The test user (`2c43b314-3e3c-4897-a3d3-02a8cab3e5e7`) is set up as `system:global#admin` in `DatabaseCleanupHooks`, so permissions cascade automatically once the schema and `parent_system` relationships are in place.

Tests that use the aura-admin user will continue to work because:
1. `system:global#admin@user:{testUserId}` gives the user `create_institution` on system
2. When an institution is onboarded, `institution:{id}#parent_system@system:global` is written
3. The platform admin automatically gets `manage`, `create_outlet`, `create_account`, etc. on the institution via cascade

## File Changes Summary

| File | Change |
|---|---|
| `application/src/main/resources/spicedb/schema.zed` | Full schema rewrite per design above |
| `SpiceDbPermissionEvaluator` | No changes needed |
| `PreAuthorizeConventionTest` | Update VALID_SCOPES, support dot notation in param refs |
| `InstitutionsGqlController` | Update `createInstitution` scope to `create_institution` |
| `OutletsGqlController` | Update `createOutlet` and `getOutletsByPage` annotations |
| `CourseGqlController` | Update `createCourse` annotation |
| `LessonsGqlController` | Update `createLesson` and `createStandaloneLesson` annotations |
| `SubjectsGqlController` | Update `createSubject` and `createSubjectTopicLinks` annotations |
| `TopicsGqlController` | Update `createTopic` annotation |
| `LevelsGqlController` | Update `getLevelByPage` annotation |
| `AccountsGqlController` | Update all account mutation annotations |
| `OnboardingService` or `TenantInitialDataLoaderImpl` | Write `institution#parent_system@system:global` on institution creation |
| `SubjectService` | Write `subject#parent_institution` relationship on create |
| `TopicService` | Write `topic#parent_institution` relationship on create |
| `TenantInitialDataLoaderImpl` | Write `level#parent_institution` relationships during onboarding |
| `AccountService` | Write educator/parent_user relationships on account creation |
| `SpiceDbPermissionEvaluatorTest` | Update test cases for new permission names |
| `PreAuthorizeConventionTest` | Update valid scopes and param matching |

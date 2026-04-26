# ADR-004: ReBAC Authorization with SpiceDB (Zanzibar-style)

**Status:** Accepted  
**Date:** 2026-04-26  
**Decision Makers:** Kevin  
**Supersedes:** ADR-002 (nested groups), ADR-003 (role-based group permissions)

## Context

ADR-002 and ADR-003 established hierarchical authorization using Keycloak's built-in authorization services: nested groups with "Extend to Children" policies, per-role group subtrees, scoped RPT tokens, and `RptPermissionEvaluator` reading JWT claims. While functionally correct, the approach has significant drawbacks at scale:

### Pain Points with Keycloak-Based Instance Authorization

1. **Massive object count**: At modest scale (10 institutions × 5 outlets × 20 courses × 30 lessons = 31,060 resources), the system creates ~186K Keycloak objects (31K resources + 62K groups + 62K policies + 62K permissions for 2 roles). Keycloak's admin console and API degrade with this volume.

2. **4+ API calls per resource creation**: Every entity creation (outlet, course, lesson) requires creating a subgroup, a group policy, a resource permission, and registering a UMA resource — per role. Creating a lesson triggers 8+ Keycloak REST API calls.

3. **RPT token management complexity**: The frontend must manage scoped RPT requests per page context (`grant_type=urn:ietf:params:oauth:grant-type:uma-ticket` with specific `permission` params). Each page transition requires a new RPT fetch, adding latency and frontend complexity.

4. **Token-embedded permissions**: Authorization data is baked into the JWT. Permission changes require a token refresh. No way to query "who has access to X?" or "what can user X access?" without Keycloak admin API calls.

5. **Keycloak is not designed for fine-grained instance authorization**: Keycloak's authorization services are designed for coarse-grained resource-type permissions, not tens of thousands of instance-level resources. We are fighting the tool.

6. **Strict tree hierarchy**: Nested groups enforce a strict tree. A resource cannot belong to two parents (e.g., shared courses across outlets) without duplicating the entire group subtree.

7. **Fragile wiring**: Every resource permission must link to the correct ancestor policies by name. Missing a policy link silently breaks inheritance for that branch.

### What We Actually Need

- **Relationship-based access control (ReBAC)**: Permissions derived from relationships between entities (user is admin of institution, outlet belongs to institution), not from group membership embedded in tokens.
- **Hierarchical inheritance via graph traversal**: Institution admin automatically accesses all descendants — computed at query time, not pre-wired via policy links.
- **Per-instance granularity at all levels**: Educators can be assigned to specific courses/lessons.
- **Real-time permission changes**: No token refresh required when access changes.
- **"List what I can access" queries**: LookupResources for building filtered UIs.
- **DAG support** (future): Shared courses across outlets without duplication.

Since Aura has not gone live yet, we can make breaking changes without migration concerns.

---

## Options

### Option 1: Keep Keycloak Authorization Services (Status Quo)

Continue with the current ADR-002/003 implementation: nested Keycloak groups, "Extend to Children" policies, scoped RPT tokens, `RptPermissionEvaluator` reading JWT claims.

**Pros:**
- Already implemented and partially tested
- Single infrastructure dependency (Keycloak handles both authn and authz)
- No new infrastructure to deploy/operate

**Cons:**
- All pain points listed above remain
- O(roles × hierarchy_depth) API calls per resource creation
- Frontend RPT management adds complexity to every page
- Token-based permission model fundamentally limits real-time access changes
- Keycloak admin console unusable at production scale
- No LookupResources/LookupSubjects equivalent
- Strict tree hierarchy (no DAG)

---

### Option 2: SpiceDB (Zanzibar-style ReBAC) for Instance Authorization

Replace Keycloak's authorization services with SpiceDB for all instance-level permission decisions. Keycloak continues to handle authentication (login, SSO, JWT issuance, identity management). SpiceDB handles authorization (who can do what on which resource).

#### Architecture

```
┌─────────────┐     JWT (identity only)     ┌──────────────────┐
│  Keycloak   │ ──────────────────────────── │  Spring Boot App │
│  (AuthN)    │                              │                  │
└─────────────┘                              │  @PreAuthorize   │
                                             │       │          │
                                             │       ▼          │
                                             │  SpiceDB         │
                                             │  Permission      │──── gRPC ────► SpiceDB
                                             │  Evaluator       │               (AuthZ)
                                             └──────────────────┘
```

#### SpiceDB Schema for Aura

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

#### How Permission Inheritance Works

When relationships are written:
```
institution:inst1#admin@user:alice
outlet:outlet1#parent_institution@institution:inst1
course:course1#parent_outlet@outlet:outlet1
lesson:lesson1#parent_course@course:course1
```

A `CheckPermission(user:alice, lesson:lesson1, "view")` traverses:
1. Is alice a direct educator on lesson1? No
2. Follow `parent_course` → course1. Is alice educator/admin/viewer on course1? No
3. Follow `parent_outlet` → outlet1. Is alice admin/viewer on outlet1? No
4. Follow `parent_institution` → inst1. Is alice admin on inst1? **Yes** → granted

This graph walk is computed by SpiceDB in single-digit milliseconds with caching.

#### Educator Assignment (Per-Course Granularity)

```
course:course42#educator@user:bob
```

Bob can now `manage` and `view` course42 and all its lessons (via `parent_course->manage`), but cannot access course43 or other courses. No extra wiring needed — the schema's arrow operator handles cascading.

#### Relationship Writes at Entity Creation

Instead of 8+ Keycloak API calls, creating an outlet requires **1 SpiceDB WriteRelationships call**:

```java
// In OutletService — single call replaces all Keycloak resource/group/policy/permission creation
permissionsService.writeRelationships(WriteRelationshipsRequest.newBuilder()
    .addUpdates(RelationshipUpdate.newBuilder()
        .setOperation(OPERATION_TOUCH)
        .setRelationship(Relationship.newBuilder()
            .setResource(ObjectReference.newBuilder()
                .setObjectType("outlet").setObjectId(outletId).build())
            .setRelation("parent_institution")
            .setSubject(SubjectReference.newBuilder()
                .setObject(ObjectReference.newBuilder()
                    .setObjectType("institution").setObjectId(institutionId).build())
                .build())
            .build())
        .build())
    .build());
```

Multiple relationships can be batched into a single WriteRelationships call.

#### Spring Security Integration

Replace `RptPermissionEvaluator` with `SpiceDbPermissionEvaluator`:

```java
@RequiredArgsConstructor
public class SpiceDbPermissionEvaluator implements PermissionEvaluator {

    private final PermissionsServiceBlockingStub permissionsService;

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId,
                                 String targetType, Object permission) {
        String userId = ((Jwt) auth.getPrincipal()).getSubject();
        CheckPermissionResponse response = permissionsService.checkPermission(
            CheckPermissionRequest.newBuilder()
                .setConsistency(Consistency.newBuilder().setMinimizeLatency(true).build())
                .setResource(ObjectReference.newBuilder()
                    .setObjectType(targetType)
                    .setObjectId(targetId != null ? targetId.toString() : "").build())
                .setPermission((String) permission)
                .setSubject(SubjectReference.newBuilder()
                    .setObject(ObjectReference.newBuilder()
                        .setObjectType("user").setObjectId(userId).build())
                    .build())
                .build());
        return response.getPermissionship() == PERMISSIONSHIP_HAS_PERMISSION;
    }
}
```

All existing `@PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")` annotations remain unchanged.

#### What Stays in Keycloak vs Moves to SpiceDB

| Concern | Stays in Keycloak | Moves to SpiceDB |
|---------|:-:|:-:|
| Authentication (login, SSO, MFA) | ✓ | |
| Identity management (users, credentials) | ✓ | |
| JWT issuance (identity tokens) | ✓ | |
| OAuth/OIDC client management | ✓ | |
| Resource registration (outlet:{id}) | | ✓ |
| Permission evaluation (can user X do Y on Z?) | | ✓ |
| Role-to-resource mapping | | ✓ |
| Hierarchical permission inheritance | | ✓ |
| LookupResources / LookupSubjects | | ✓ (new capability) |

#### What Gets Removed from Keycloak

- Authorization Services configuration (UMA, resource server)
- All UMA resources, policies, and permissions
- Nested group hierarchy (`system:1:admin`, `institution:*:admin`, etc.)
- RPT token exchange flow
- `authorization.permissions` claim in JWTs

The Keycloak `realm-import.json` shrinks dramatically — only identity configuration remains.

**Pros:**
- **1 API call per entity creation** (WriteRelationships) vs 8+ Keycloak calls
- **Zero Keycloak authorization objects** — no groups/policies/permissions/resources to manage
- **Real-time permission changes** — no token refresh needed
- **LookupResources**: "What courses can this user view?" in a single call
- **LookupSubjects**: "Who has admin access to this institution?" in a single call
- **Schema is the documentation** — the `.zed` schema file is a readable specification of the entire permission model
- **Supports DAGs** — a course can belong to multiple outlets via multiple `parent_outlet` relationships
- **Built for scale** — Zanzibar architecture handles billions of relationships (Google's internal system)
- **Simpler frontend** — no RPT management; JWT is identity-only, permissions checked server-side
- **Simpler backend** — no group/policy/permission wiring; just write relationships
- **Testcontainers support** — in-memory SpiceDB for component tests
- **Clean Architecture fit** — `GroupAuthorizationService` interface replaced with `RelationshipService` interface in core; SpiceDB implementation in details

**Cons:**
- **New infrastructure dependency** — SpiceDB must be deployed alongside Keycloak (Docker, Kubernetes)
- **Network call per permission check** — ~1-5ms per CheckPermission (vs local JWT claim lookup which is ~0ms). Mitigated by SpiceDB's aggressive caching and `minimize_latency` consistency mode
- **Learning curve** — team must learn SpiceDB schema language and Zanzibar concepts
- **No built-in admin UI** — SpiceDB has no equivalent of Keycloak's admin console for browsing permissions (though AuthZed offers a playground and commercial dashboard)
- **Consistency model** — eventual consistency by default (5-second quantization window). Must use `at_least_as_fresh` with ZedToken for read-after-write scenarios
- **Additional operational burden** — monitoring, backup, and scaling of SpiceDB alongside existing services

---

### Option 3: Open Policy Agent (OPA) with Data from Application DB

Use OPA as a sidecar/service for policy evaluation. Authorization data (who has what role on which resource) stays in the application database. OPA policies (written in Rego) query the data and compute permissions.

**Pros:**
- Rego is a general-purpose policy language (can express any rule)
- OPA is widely adopted and well-documented
- No separate authorization data store — roles/relationships live in app DB
- Can co-locate OPA as a sidecar for sub-millisecond decisions

**Cons:**
- **No built-in graph traversal**: Hierarchical inheritance must be hand-coded in Rego. The `parent→child→grandchild` walk that SpiceDB's arrow operator handles automatically must be written and maintained as recursive Rego rules
- **Data synchronization**: OPA needs a snapshot of the authorization data. Must push updates from the app DB to OPA (via bundles or push gateway). Stale data = wrong decisions
- **No LookupResources equivalent**: OPA evaluates "can X do Y?" but cannot efficiently answer "what can X access?" without querying every resource
- **Rego complexity at scale**: Hierarchical policies in Rego become hard to reason about as the hierarchy deepens
- **Not purpose-built for ReBAC**: OPA is an ABAC/PBAC engine. Using it for ReBAC is swimming upstream

---

### Option 4: Application-Level Authorization (Custom Implementation)

Implement authorization logic directly in the application using database queries. Store relationships in a dedicated `authorization_relationships` table and compute permissions via SQL joins/CTEs.

**Pros:**
- No external dependency beyond the existing PostgreSQL database
- Full control over the implementation
- Zero network latency for permission checks (same DB transaction)

**Cons:**
- **Must build everything from scratch**: Graph traversal, caching, consistency, APIs — all need custom implementation
- **Performance at scale**: Recursive SQL queries for deep hierarchies are expensive without careful indexing and caching
- **No LookupResources without custom query layer**: Must write and maintain complex queries for "what can X access?"
- **Maintenance burden**: Authorization is a solved problem; building a custom Zanzibar is significant ongoing engineering
- **Testing complexity**: Must validate authorization logic correctness extensively since there's no battle-tested engine underneath
- **Coupling**: Authorization logic mixed into the application codebase, harder to audit and reason about

---

## Comparison Matrix

| Criterion | Option 1: Keycloak (Status Quo) | Option 2: SpiceDB | Option 3: OPA | Option 4: Custom |
|-----------|:---:|:---:|:---:|:---:|
| API calls per entity creation | 8+ | 1 | 0 (DB write) | 0 (DB write) |
| Permission check latency | ~0ms (JWT local) | 1-5ms (gRPC) | <1ms (sidecar) | ~1ms (SQL) |
| Objects to manage at scale | ~186K | ~31K relationships | Policy files + DB | DB rows |
| Hierarchical inheritance | Manual policy links | Arrow operator (automatic) | Hand-coded Rego | Hand-coded SQL |
| LookupResources | Not available | Built-in | Not available | Custom query |
| LookupSubjects | Not available | Built-in | Not available | Custom query |
| DAG support | No (strict tree) | Yes | Yes (if coded) | Yes (if coded) |
| Real-time permission changes | Requires token refresh | Immediate | Bundle refresh delay | Immediate |
| Frontend complexity | RPT management per page | None (server-side checks) | None | None |
| New infrastructure | None | SpiceDB + PostgreSQL | OPA sidecar | None |
| Schema as documentation | No (Keycloak admin UI) | Yes (.zed file) | Partial (.rego files) | No |
| Existing `@PreAuthorize` compat | N/A (current) | Drop-in (PermissionEvaluator) | Drop-in | Drop-in |
| Operational maturity | High (Keycloak) | Medium (growing adoption) | High (CNCF graduated) | Low (custom) |

---

## Recommendation

**Option 2: SpiceDB** is the recommended approach. The core value proposition:

1. **Eliminates the fundamental awkwardness**: Keycloak is an identity provider being forced into a fine-grained authorization role it wasn't designed for. SpiceDB is purpose-built for exactly this problem.

2. **Dramatic simplification of write path**: 1 WriteRelationships call vs 8+ Keycloak API calls per entity creation. The `ResourceAuthorizationRegistrar` with its group/policy/permission wiring becomes a simple relationship write.

3. **Eliminates frontend RPT complexity**: No more scoped RPT requests per page. The JWT is identity-only (subject, name, email). All permission checks happen server-side via gRPC to SpiceDB.

4. **Schema is the authorization spec**: The `.zed` schema file replaces hundreds of lines of Java naming conventions (`AuthorizationNaming`, `AuthorizationGroupPath`, `AuthorizationRoles`) and Keycloak configuration.

5. **Unlocks new capabilities**: LookupResources ("show me my courses") and LookupSubjects ("who administers this institution") are first-class operations, not afterthoughts.

6. **Clean migration path**: The existing `@PreAuthorize` annotations on controllers don't change. We swap `RptPermissionEvaluator` for `SpiceDbPermissionEvaluator`. Service-layer code simplifies (relationship writes replace registrar calls).

The 1-5ms per-check latency trade-off is acceptable: it replaces the current RPT token exchange latency (~50-200ms per page transition) and the 8+ API calls per entity creation (~200-500ms total).

---

## Implementation Impact

### Files to Create
- `core/src/main/java/com/aura/core/clients/RelationshipAuthorizationService.java` — new core interface replacing `GroupAuthorizationService` + `ResourceAuthorizationService`
- `details/src/main/java/com/aura/details/spicedb/SpiceDbRelationshipService.java` — implementation
- `application/src/main/java/com/aura/app/security/SpiceDbPermissionEvaluator.java` — replaces `RptPermissionEvaluator`
- `application/src/main/java/com/aura/app/configs/SpiceDbConfig.java` — gRPC channel + stubs
- `application/src/main/resources/spicedb/schema.zed` — schema file
- SpiceDB Testcontainer setup for component tests

### Files to Modify
- All service classes (`OnboardingService`, `OutletService`, `CourseService`, `LessonService`, `AccountService`) — replace `ResourceAuthorizationRegistrar` calls with relationship writes
- `application/configs/ServiceConfig.java` — wire new beans
- `application/configs/WebSecurityConfig.java` — swap permission evaluator
- `component-tests/` — add SpiceDB container, replace Keycloak auth setup in step defs
- `realm-import.json` — remove authorization services configuration

### Files to Delete
- `core/clients/GroupAuthorizationService.java`
- `core/clients/ResourceAuthorizationService.java`
- `core/auth/AuthorizationNaming.java`
- `core/auth/AuthorizationGroupPath.java`
- `core/auth/AuthorizationRoles.java`
- `details/keycloak/client/KeycloakGroupAuthorizationService.java`
- `details/keycloak/client/KeycloakResourceAuthorizationService.java`
- `details/services/ResourceAuthorizationRegistrar.java`
- `application/security/RptPermissionEvaluator.java`
- `component-tests/utils/RptTokenExchange.java`

### Controllers — No Changes
All `@PreAuthorize("hasPermission(#id, 'resource', 'scope')")` annotations remain identical.

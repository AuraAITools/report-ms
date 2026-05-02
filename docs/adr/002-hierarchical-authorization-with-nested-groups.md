# ADR-002: Hierarchical Authorization with Nested Groups

**Status:** Accepted  
**Date:** 2026-04-25  
**Decision Makers:** Kevin  
**Supersedes:** ADR-001 (replaces the flat group + dual-policy inheritance mechanism)

## Decision

**Option 1: Nested Groups with "Extend to Children" Policy**, combined with **scoped RPT requests** to prevent token size explosion.

All resources at all hierarchy levels (institution, outlet, course, lesson) are registered in Keycloak with full instance-level granularity. The frontend requests scoped RPTs per page context instead of all permissions at login. See Option 1 below for full details and the Migration Path section for implementation steps.

## Context

ADR-001 established instance-level RBAC using flat Keycloak groups and dual-policy AFFIRMATIVE linking. This works well for a 2-level hierarchy (institution → outlet), but Aura is moving toward deeper hierarchies:

```
Institution → Outlet → Course → Lesson → (future levels)
```

With the current approach, each new depth level compounds the problem:

- **Policy explosion**: Every leaf resource's permission must link to policies from _every_ ancestor level. A lesson permission needs 4 policy links (lesson + course + outlet + institution). At scale (10 institutions × 5 outlets × 20 courses × 30 lessons = 31,060 resources), that's ~91,000 policy-link operations.
- **Keycloak API calls per creation**: Creating a lesson requires 4 API calls + 4 policy links = 8 operations. Each new hierarchy level adds more.
- **Wiring fragility**: Every child resource must know and reference all ancestor policies by name. Missing one link silently breaks inheritance for that branch.

We need a strategy that scales to N levels of hierarchy without linear growth in policies, API calls, or wiring complexity per resource.

Additionally, Aura has concrete requirements for **instance-level access control at every hierarchy level**:

- **Educators** must only access the specific courses and lessons they are assigned to — not all courses in an outlet
- **Lesson-level granularity** is also required — different educators may be assigned to different lessons within the same course

This rules out any approach that delegates course/lesson authorization to a parent level, and requires full Keycloak resource registration at all levels (institution, outlet, course, lesson).

Since Aura has not gone live yet, we can make breaking changes without migration concerns.

---

## Option 1: Nested Groups with "Extend to Children" Policy

### How It Works

Keycloak groups support parent-child nesting. A group policy with **"Extend to Children" enabled** matches members of the target group _and all its descendant subgroups_. This single flag replaces the dual-policy AFFIRMATIVE linking mechanism entirely.

### Group Hierarchy

```
/institution:{institutionId}:admin                              ← Extend to Children = ON
  /outlet:{outletId}:admin                                      ← Extend to Children = ON
    /course:{courseId}:admin                                     ← Extend to Children = ON
      /lesson:{lessonId}:admin                                  ← Extend to Children = ON (for future sub-levels)
```

### Resource Creation Workflow

**Institution onboarding** (4 API calls):
1. Register resource `institution:{id}`
2. Create group `/institution:{id}:admin`
3. Create group policy `institution-{id}-admin-policy` with **extendChildren = true**
4. Create permission `institution-{id}-permission` → links to 1 policy

**Outlet creation** (4 API calls):
1. Register resource `outlet:{id}`
2. Create subgroup `/institution:{instId}:admin/outlet:{id}:admin`
3. Create group policy `outlet-{id}-admin-policy` with **extendChildren = true**
4. Create permission `outlet-{id}-permission` → links to institution policy + outlet policy (AFFIRMATIVE)

**Course creation** (4 API calls):
1. Register resource `course:{id}`
2. Create subgroup `.../outlet:{outletId}:admin/course:{id}:admin`
3. Create group policy `course-{id}-admin-policy` with **extendChildren = true**
4. Create permission `course-{id}-permission` → links to institution policy + outlet policy + course policy (AFFIRMATIVE)

**Lesson creation** (4 API calls):
1. Register resource `lesson:{id}`
2. Create subgroup `.../course:{courseId}:admin/lesson:{id}:admin`
3. Create group policy `lesson-{id}-admin-policy` with **extendChildren = true**
4. Create permission `lesson-{id}-permission` → links to institution policy + outlet policy + course policy + lesson policy (AFFIRMATIVE)

### Access Inheritance

| User's Group Membership | Access Granted To |
|---|---|
| `/institution:001:admin` | institution:001, all outlets, courses, and lessons under it |
| `.../outlet:005:admin` | outlet:005, all courses and lessons under outlet:005 |
| `.../course:042:admin` | course:042 and all lessons under it |
| `.../lesson:099:admin` | lesson:099 only |

### Per-Level Roles (Educator Use Case)

Educators need access to **specific courses and/or lessons** — not all resources in an outlet. The nested group structure supports this naturally:

**Course-level educator:**
- Add educator to group `.../outlet:{outletId}:admin/course:{courseId}:admin`
- Educator can access course:042 and ALL lessons under it (via Extend to Children)
- Educator CANNOT access course:043 in the same outlet

**Lesson-level educator:**
- Add educator to group `.../course:{courseId}:admin/lesson:{lessonId}:admin`
- Educator can access lesson:099 only — not other lessons in the same course

**Key insight:** the group name suffix `:admin` is a role label, not a privilege level. Both institution admins and course educators are members of groups in the same tree — the difference is _where_ in the tree they are placed. The group path determines access scope.

### How "Extend to Children" Eliminates Dual-Policy Wiring

In ADR-001, outlet permissions required linking to BOTH `outlet-{id}-admin-policy` AND `institution-{id}-admin-policy`. With Extend to Children, only the **outlet's own policy** is needed on the outlet permission — but the institution admin still gets access because:

1. Institution admin is a member of `/institution:001:admin`
2. Outlet group `/institution:001:admin/outlet:005:admin` is a child of the institution group
3. `institution-001-admin-policy` has Extend to Children = ON
4. Therefore, the institution admin matches the institution policy, which evaluates true for any permission that links to it

However, for this to work, each resource's permission must still link to **all ancestor policies** so that Keycloak can evaluate them. Extend to Children means the institution policy covers users in descendant groups — but the permission must reference that policy for it to be evaluated.

**Simplification via a single institution-level policy per permission:** Since the institution policy with Extend to Children matches members at ALL levels, every permission in the institution can link to just the institution policy. Per-level policies are only needed if you want to assign users at intermediate levels (outlet-admin, course-educator) without giving them institution-wide access.

In practice, you need per-level policies at every level where distinct roles exist:
- Institution admin → institution policy
- Outlet admin → outlet policy
- Course educator → course policy (or reuse the course admin group)
- Lesson educator → lesson policy

Each permission links to all ancestor policies with AFFIRMATIVE strategy — any matching policy grants access.

### Scoped RPT Requests

Registering every resource (institution, outlet, course, lesson) in Keycloak means the RPT could contain tens of thousands of permission entries if requested without scope. At ~100 bytes per entry, 31K resources = ~3MB JWT — impractical.

**Solution: scoped RPT requests.** Instead of requesting all permissions at login, the frontend requests permissions for only the resources relevant to the current view:

```
POST /realms/{realm}/protocol/openid-connect/token
  grant_type=urn:ietf:params:oauth:grant-type:uma-ticket
  &audience={clientId}
  &permission=course:042#view
  &permission=course:043#view
  &permission=lesson:099#view
  &permission=lesson:100#view
```

This returns an RPT containing only the requested permissions, keeping the token small regardless of total resource count.

**Frontend flow per page context:**

1. User navigates to "Outlet 005 — Courses"
2. Frontend calls backend API: `GET /outlets/005/courses` → returns list of course IDs (filtered by tenant isolation)
3. Frontend requests scoped RPT: `permission=course:042#view&permission=course:043#view&...`
4. Keycloak evaluates only the requested permissions → returns small RPT
5. Frontend uses RPT for subsequent API calls on that page
6. When user navigates to "Course 042 — Lessons", repeat with lesson-scoped RPT

**Trade-offs:**
- **Pro:** RPT stays small (~20-50 entries per context) regardless of total resource count
- **Pro:** Faster Keycloak evaluation (fewer policies to check per request)
- **Pro:** Keycloak returns 403 for resources the user can't access — frontend can use this to hide/show UI elements
- **Con:** Multiple RPT requests per session instead of one upfront
- **Con:** Frontend must know which resources to request (requires a "list resources" API per page)
- **Con:** More complex frontend token management (RPT per context, not global)
- **Con:** Additional latency on page navigation (RPT request before rendering)

**Caching strategy:** RPTs are cached for their lifespan (5 minutes). The frontend caches scoped RPTs by context key (e.g., `outlet:005:courses`). Navigating back to a previously visited page reuses the cached RPT if still valid.

### Scale Analysis (10 inst × 5 outlets × 20 courses × 30 lessons)

| Metric | ADR-001 Extended to 4 Levels | This Option |
|---|---|---|
| Keycloak resources | 31,060 | 31,060 (all levels registered) |
| Groups in Keycloak | 31,060 (flat) | 31,060 (nested) |
| Policies in Keycloak | 31,060 | 31,060 (one per group) |
| Permissions | 31,060 | 31,060 |
| Policy links per lesson permission | 4 (manually wired) | 4 (institution + outlet + course + lesson policies) |
| API calls per lesson creation | 8 | 4 (resource + subgroup + policy + permission) |
| RPT size (scoped per page) | ~3MB (all-at-once) | ~20-50 entries per context |
| Groups per lesson creation | 1 (flat) | 1 (nested under course) |

### Interface Changes

```java
public interface GroupAuthorizationService {
    void createGroup(String groupName);
    void createSubGroup(String parentGroupPath, String childGroupName);  // NEW
    void createGroupPolicy(String policyName, String groupName, boolean extendToChildren);  // CHANGED
    void createResourcePermission(String permissionName, String resourceName,
                                   Set<String> scopes, Set<String> policyNames);
    void addUserToGroup(String userId, String groupPath);  // path-based lookup for nested groups
}
```

### Pros

- **Full instance-level granularity at every level** — educators can be assigned to specific courses OR specific lessons
- **Per-course and per-lesson educator access** — educators are assigned to specific resources via nested subgroups
- **Structural inheritance via Extend to Children** — institution admins automatically access all descendants; outlet admins access their subtree; no manual policy linking across levels
- **Structural inheritance scales to N levels** — adding a new hierarchy level (e.g., "module" between course and lesson) requires no changes to existing permissions — just nest deeper
- **Group hierarchy mirrors business hierarchy** — "where is this user in the org chart?" maps directly to the group tree
- **Reparenting works naturally** — moving a course from outlet-A to outlet-B means moving the subgroup; all educator memberships move with it
- **RPT bloat solved via scoped requests** — frontend requests permissions per page context, keeping tokens at ~20-50 entries regardless of total resource count
- **Consistent model** — every resource at every level follows the same pattern (register resource, create subgroup, create policy, create permission)

### Cons

- **High Keycloak object count** — 31K resources, groups, policies, and permissions at scale. Keycloak handles this, but admin console performance degrades with tens of thousands of objects
- **4 API calls per resource creation** — every course and lesson creation makes 4 Keycloak calls (resource + subgroup + policy + permission), adding latency to write operations
- **Group hierarchy is load-bearing** — accidental restructuring breaks access
- **"Extend to Children" is a subtle flag** — easy to miss or accidentally toggle off in Keycloak admin console
- **Strict tree structure** — a resource cannot belong to two parents (e.g., a course shared between outlets). If your domain is a DAG, not a tree, this doesn't fit
- **Nested group lookup is path-based** — `findGroupByPath("/institution:001:admin/outlet:005:admin")` is less straightforward than `findGroupByName("outlet:005:admin")`
- **All-or-nothing inheritance** — an institution admin gets access to ALL descendants; you can't exclude a specific outlet without removing them from the institution group and adding them to each outlet group individually
- **Scoped RPT adds frontend complexity** — frontend must manage contextual RPT requests per page instead of one upfront token; requires a "list resources" API per context

---

## Option 2: Aggregated Policies with Hierarchical Resolution

### How It Works

Keep flat groups (no nesting), but replace the dual-policy linking with a single **aggregated policy** per institution that encapsulates all descendant policies. When a new child resource is created, its policy is added to the parent's aggregated policy.

### Policy Structure

```
institution-{id}-aggregated-policy (type: aggregated, strategy: AFFIRMATIVE)
  ├── institution-{id}-admin-policy (group policy)
  ├── outlet-{outletId-1}-admin-policy (group policy)
  ├── outlet-{outletId-2}-admin-policy (group policy)
  ├── course-{courseId-1}-admin-policy (group policy)
  └── ...
```

Each resource permission links to **only the institution's aggregated policy**. The aggregated policy internally evaluates all descendant group policies with AFFIRMATIVE strategy — if the user matches ANY sub-policy, access is granted.

### Resource Creation Workflow

**Institution onboarding** (5 API calls):
1. Register resource `institution:{id}`
2. Create group `institution:{id}:admin`
3. Create group policy `institution-{id}-admin-policy`
4. Create aggregated policy `institution-{id}-aggregated-policy` containing `institution-{id}-admin-policy`
5. Create permission `institution-{id}-permission` → links to aggregated policy

**Outlet creation** (5 API calls):
1. Register resource `outlet:{id}`
2. Create group `outlet:{id}:admin`
3. Create group policy `outlet-{id}-admin-policy`
4. **Update** `institution-{instId}-aggregated-policy` to include `outlet-{id}-admin-policy`
5. Create permission `outlet-{id}-permission` → links to `institution-{instId}-aggregated-policy`

**Lesson creation** (2 API calls if no lesson-admin group needed):
1. Register resource `lesson:{id}`
2. Create permission `lesson-{id}-permission` → links to the institution's aggregated policy

### Per-Level Scoping

For outlet-level admins who should only see their own outlet's resources, create a **per-outlet aggregated policy**:

```
outlet-{id}-aggregated-policy (type: aggregated, strategy: AFFIRMATIVE)
  ├── outlet-{id}-admin-policy
  ├── course-{courseId-1}-admin-policy  (courses under this outlet)
  └── ...
```

Course permissions under that outlet link to the outlet's aggregated policy. The outlet aggregated policy is itself included in the institution's aggregated policy (composable).

### Scale Analysis

| Metric | ADR-001 Extended | This Option |
|---|---|---|
| Groups | 31,060 (flat) | 1,060 (only at admin-role levels) |
| Policies | 31,060 | 1,070 (1,060 group + 10 aggregated) |
| Permissions | 31,060 | 31,060 |
| Policy links per lesson | 4 | 1 |
| API calls per lesson | 8 | 2 |

### Interface Changes

```java
public interface GroupAuthorizationService {
    void createGroup(String groupName);
    void createGroupPolicy(String policyName, String groupName);
    void createAggregatedPolicy(String policyName, Set<String> subPolicyNames);  // NEW
    void updateAggregatedPolicy(String policyName, String addSubPolicyName);     // NEW
    void createResourcePermission(String permissionName, String resourceName,
                                   Set<String> scopes, Set<String> policyNames);
    void addUserToGroup(String userId, String groupName);
}
```

### Pros

- **Flat groups** — no nested group complexity; group lookup by name still works
- **Composable policies** — aggregated policies can nest (institution contains outlet aggregated policies, which contain course policies)
- **Explicit hierarchy** — the aggregated policy's sub-policy list is visible in Keycloak admin console
- **DAG-friendly** — a course shared between two outlets can have its policy included in both outlet aggregated policies
- **Per-level scoping** — outlet admins only see their outlet's subtree because outlet permissions link to the outlet aggregated policy, not the institution one

### Cons

- **Aggregated policy mutation** — every child creation requires an UPDATE to the parent's aggregated policy (read-modify-write). Under concurrent creation, this is a race condition unless serialized
- **More policies than Option 1** — still need one group policy per admin-level resource (1,060 vs. 10 in Option 1)
- **Aggregated policy can grow large** — an institution with 1,000 courses has an aggregated policy referencing 1,000+ sub-policies. Keycloak evaluates all of them on every RPT request for users in that institution
- **Cleanup on deletion** — deleting a course requires removing its policy from the parent aggregated policy, not just deleting the policy itself
- **No native Keycloak "add sub-policy" API** — updating an aggregated policy requires fetching the full representation, adding the new sub-policy name, and PUT-ing back

---

## Option 3: Scope-Based Hierarchy with Resource Naming Convention

### How It Works

Instead of creating groups/policies per resource instance, encode the hierarchy in the **resource name** and use a single **regex/wildcard policy** per admin level. The RPT evaluator resolves access by matching resource name patterns.

### Resource Naming Convention

```
institution:{instId}                              → institution resource
institution:{instId}:outlet:{outletId}            → outlet resource
institution:{instId}:outlet:{outletId}:course:{courseId}  → course resource
institution:{instId}:outlet:{outletId}:course:{courseId}:lesson:{lessonId}  → lesson resource
```

### Policy Structure

Only **one group + one policy per admin-level assignment**:

- Group: `institution:{instId}:admin`
- Policy: `institution-{instId}-admin-policy`

The permission for _every_ resource under that institution links to `institution-{instId}-admin-policy`.

Keycloak doesn't need to understand the hierarchy — the resource naming convention encodes parentage, and all resources under an institution share the same policy.

For per-outlet admins:
- Group: `outlet:{outletId}:admin`
- Policy: `outlet-{outletId}-admin-policy`
- All resources whose name starts with `institution:{instId}:outlet:{outletId}:...` link to this policy too (AFFIRMATIVE with institution policy)

### Resource Creation Workflow

**Lesson creation** (2 API calls):
1. Register resource `institution:{instId}:outlet:{outletId}:course:{courseId}:lesson:{lessonId}`
2. Create permission linking to the institution's admin policy (and outlet policy if outlet-level admins exist)

No groups, no policies created per lesson.

### RPT Evaluator Change

The `RptPermissionEvaluator` must be updated to:
1. Accept the full hierarchical resource name: `institution:001:outlet:005:course:042:lesson:999`
2. Check the RPT for an exact match on `rsname`

Controller annotations change to pass the full path:

```java
@PreAuthorize("hasPermission('institution:' + #institutionId + ':outlet:' + #outletId, 'outlet', 'view')")
```

Or, keep short resource names (`lesson:999`) and add a **prefix-matching** mode: if the RPT contains `institution:001` and the resource is `lesson:999` which belongs to institution 001, the evaluator walks up the hierarchy (via DB lookup or encoded path) to find a matching ancestor permission.

### Scale Analysis

| Metric | ADR-001 Extended | This Option |
|---|---|---|
| Groups | 31,060 | 60 (only institution + outlet admin groups) |
| Policies | 31,060 | 60 |
| Permissions | 31,060 | 31,060 |
| Policy links per lesson | 4 | 1-2 |
| API calls per lesson | 8 | 2 |

### Interface Changes

Minimal — `GroupAuthorizationService` stays the same. The main change is in resource naming and `RptPermissionEvaluator`.

### Pros

- **Fewest Keycloak objects** — only groups/policies at levels where admin roles exist (institution + outlet = 60 total)
- **Zero overhead per leaf creation** — no groups, no policies, just resource + permission
- **Hierarchy encoded in data, not Keycloak structure** — adding new levels requires no Keycloak structural changes
- **DAG-friendly** — resource names are just strings; a course shared between outlets could have two resources registered under different paths
- **Simple mental model** — "resource name tells you who owns it"

### Cons

- **Long resource names** — `institution:001:outlet:005:course:042:lesson:999` is verbose; RPT payload grows
- **RPT bloat** — every individual resource still appears in the RPT permissions array. 30,000 lessons = 30,000 entries (same problem as today, just with longer names)
- **Controller annotation complexity** — `@PreAuthorize` must construct hierarchical resource names, requiring more parameters passed to controllers
- **Reparenting requires resource rename** — moving a course between outlets means deleting and re-registering the resource with a new name, plus updating the permission
- **Breaks current `RptPermissionEvaluator` contract** — either resource names become hierarchical (breaking all existing code) or the evaluator needs a DB lookup to resolve ancestry (adding latency)
- **No type-level resource matching in Keycloak** — Keycloak can't natively say "grant access to all resources matching `institution:001:*`". Each resource still needs its own permission

---

## Option 4: Hybrid — Nested Groups + Scoped Permissions per Admin Level

### How It Works

Combine Option 1's nested groups with a **tiered permission strategy**: only create per-instance permissions at levels where fine-grained access control matters (institution, outlet). For deeper levels (course, lesson), use **type-level permissions scoped by ancestor**.

### Permission Tiers

| Level | Per-Instance Permission? | Per-Instance Group? | Policy Linked To |
|---|---|---|---|
| Institution | Yes | Yes | `institution-{id}-admin-policy` |
| Outlet | Yes | Yes (nested under institution) | institution policy + outlet policy |
| Course | **No** — uses type-level `course` permission | Yes (nested under outlet) | institution policy (Extend to Children covers all) |
| Lesson | **No** — uses type-level `lesson` permission | No | institution policy |

### How Type-Level Permissions Work

Instead of registering every course as a separate Keycloak resource, register courses only in the **database**. Authorization for courses is checked by:

1. `@PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")` — if you can access the outlet, you can access its courses
2. Tenant isolation (`x-tenant-id` + `TenantSpecification`) ensures cross-tenant safety

For institution-admin and outlet-admin, access to courses/lessons is **implied** by their outlet/institution access. No Keycloak resources, groups, or policies needed for courses/lessons at all.

### When You Need Course-Level Admins

If you later need a "course admin" who can access only one course (not the whole outlet), you add:
1. A nested subgroup under the outlet group
2. An outlet-scoped policy with Extend to Children
3. A per-course permission

This is an incremental addition — you don't need it from day one.

### Resource Creation Workflow

**Outlet creation** (4 API calls — like Option 1):
1. Register resource `outlet:{id}`
2. Create subgroup `/institution:{instId}:admin/outlet:{id}:admin`
3. Create outlet group policy with Extend to Children = ON
4. Create permission `outlet-{id}-permission` → links to institution policy + outlet policy

**Course creation** (0 Keycloak API calls):
1. Save to DB only
2. Authorization check delegates to outlet-level permission

**Lesson creation** (0 Keycloak API calls):
1. Save to DB only
2. Authorization check delegates to outlet-level (or course-level) permission

### Scale Analysis

| Metric | ADR-001 Extended | This Option |
|---|---|---|
| Groups | 31,060 | 60 (institution + outlet only) |
| Policies | 31,060 | 60 |
| Permissions | 31,060 | 60 |
| Keycloak resources | 31,060 | 60 |
| API calls per lesson | 8 | 0 |
| API calls per course | 8 | 0 |

### Controller Authorization Changes

```java
// Current: per-instance check
@PreAuthorize("hasPermission(#courseId, 'course', 'view')")

// New: delegate to parent outlet
@PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")
```

This requires course/lesson controllers to receive `outletId` as a parameter (or resolve it from the course/lesson entity).

### Pros

- **Fewest Keycloak objects of all options** — 60 resources/groups/policies/permissions vs. 31,060
- **Zero Keycloak overhead for leaf creation** — courses and lessons are DB-only
- **RPT stays small** — only institution and outlet permissions in the token (60 entries max, not 31,060)
- **Simpler Keycloak state** — only 2 levels of authorization objects to manage
- **Incrementally extensible** — if course-level admins are needed later, add them without changing existing structure
- **Fastest RPT evaluation** — fewer permissions to scan in the JWT

### Cons

- **Cannot support educator per-course access** — educators need access to specific courses, not all courses in an outlet. This option delegates course authorization to the outlet level, making per-course granularity impossible without retrofitting. **This is a known requirement for Aura, making this option unsuitable.**
- **Controller change required** — course/lesson controllers must know their parent outlet ID for authorization checks
- **Breaks the "every resource is a Keycloak resource" mental model** — courses and lessons exist only in the DB, not in Keycloak's authorization services
- **Adding per-course admins later requires retrofitting** — if you discover you need course-level admins for 1,000 existing courses, you'll need to register 1,000 resources retroactively
- **Mixed authorization model** — some resources checked via Keycloak permissions (institution, outlet), others via parent delegation. Developers must understand which is which

---

## Comparison Matrix

| Criteria | Option 1: Nested Groups | Option 2: Aggregated Policies | Option 3: Scope-Based Naming | Option 4: Hybrid |
|---|---|---|---|---|
| Keycloak objects (resources/groups/policies/permissions) | ~31K each | ~31K resources, ~1K groups, ~1K policies, ~31K permissions | ~31K resources, ~60 groups, ~60 policies, ~31K permissions | ~60 each |
| API calls per lesson | 4 | 2 | 2 | 0 |
| Supports DAG (shared resources) | No | Yes | Yes | No |
| Per-course educator access | Yes | Yes | Yes | **No** |
| Per-lesson educator access | Yes | Yes | Yes | **No** |
| RPT strategy | Scoped (small per context) | All-at-once (bloated) | All-at-once (bloated) | All-at-once (small) |
| Reparenting complexity | Move subgroup | Update aggregated policy | Re-register resource | Move subgroup |
| Race conditions | None | Yes (aggregated policy update) | None | None |
| Code changes required | Moderate | Moderate | Significant | Moderate |
| Keycloak feature dependency | Extend to Children flag | Aggregated policy type | None | Extend to Children flag |
| Future-proof for deeper hierarchy | Excellent | Good | Good | Poor (must retrofit) |
| Consistent authorization model | Yes (all levels same pattern) | Yes | Yes | No (mixed type-level + instance-level) |

---

## Recommendation

**Option 1 (Nested Groups with Extend to Children)** with **scoped RPT requests** for token size management.

All resources at all levels (institution, outlet, course, lesson) are registered in Keycloak with full instance-level granularity.

### Why Option 1

1. **Full instance-level granularity is a requirement.** Educators must access only specific courses and lessons. Options 1-3 support this; Option 4 does not. This eliminates Option 4.

2. **RPT bloat is solved by scoped RPT requests.** The frontend requests permissions per page context (~20-50 entries) instead of all-at-once (~31K entries). The RPT stays small regardless of total resource count.

3. **Consistent model at every level.** Every resource follows the same pattern: register resource → create nested subgroup → create policy (Extend to Children = ON) → create permission. No mixed authorization models — developers learn one pattern.

4. **Structural inheritance scales to N levels.** Adding new hierarchy levels (e.g., "module" between course and lesson) requires no changes to existing permissions — just nest deeper in the group tree.

5. **Group hierarchy mirrors business hierarchy.** The nested group tree (`/institution:001:admin/outlet:005:admin/course:042:admin`) directly represents the organizational structure. User assignment and access reasoning are intuitive.

6. **No race conditions.** Unlike Option 2's aggregated policy updates, group nesting is atomic — creating a subgroup under a parent doesn't require modifying the parent.

### Why Not the Others

- **Option 2 (Aggregated Policies)**: Concurrent child creation causes race conditions on the aggregated policy update (read-modify-write without locking). Also doesn't solve RPT bloat.
- **Option 3 (Scope-Based Naming)**: Most invasive code changes (hierarchical resource names, controller rework, RPT evaluator rewrite). Doesn't solve RPT bloat either.
- **Option 4 (Hybrid)**: Cannot support per-course or per-lesson educator access — a known requirement. Would need to evolve into Option 1 anyway.

### Migration Path (from ADR-001)

Since Aura hasn't gone live, this is a clean swap:

1. Update `GroupAuthorizationService` interface:
   - Add `createSubGroup(String parentGroupPath, String childGroupName)` for nested group creation
   - Change `createGroupPolicy` to accept `boolean extendToChildren` flag
   - Update `addUserToGroup` to use path-based group lookup for nested groups
2. Restructure `DefaultInstitutionOnboardingStrategy` — create group with Extend to Children = ON
3. Update `OutletService.createOutlet()` — create nested subgroup under institution group, create outlet policy with Extend to Children = ON, permission links to institution + outlet policies
4. Update `CourseService.createCourse()` — register course as Keycloak resource, create nested subgroup under outlet group, create course policy with Extend to Children = ON, permission links to institution + outlet + course policies
5. Update `LessonService.createLesson()` — register lesson as Keycloak resource, create nested subgroup under course group, create lesson policy with Extend to Children = ON, permission links to all ancestor policies
6. Add educator assignment flow — add user to the appropriate nested subgroup (course-level or lesson-level)
7. Update frontend to use scoped RPT requests per page context instead of all-permissions-at-login
8. Update `realm-import.json` — restructure to nested group hierarchy with Extend to Children policies
9. Update `DatabaseCleanupHooks` and component tests for nested group structure
10. Update `RptPermissionEvaluator` — no structural changes needed; it still reads `authorization.permissions` from the RPT JWT. The scoped RPT just contains fewer entries.

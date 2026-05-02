# ADR-003: Role-Based Group Permissions (Admin vs Viewer vs Custom Roles)

**Status:** Accepted  
**Date:** 2026-04-25  
**Decision Makers:** Kevin  
**Depends on:** ADR-002 (nested groups with Extend to Children)

## Decision

**Option 2: Role-Scoped Resource Permissions.** Each role (admin, viewer, future educator) gets its own parallel group subtree and its own resource permission per resource. Adding/removing roles never modifies existing permissions. See Option 2 below for full details.

## Context

ADR-002 established a nested group hierarchy for instance-level authorization:

```
/system:1:admin
  /institution:{id}:admin
    /outlet:{id}:admin
      /course:{id}:admin
        /lesson:{id}:admin
```

All groups use the `:admin` suffix and grant **all scopes** (view, manage, delegate, create) via a single resource permission per resource. There is no way to grant a user view-only access — membership in any group grants full access to that resource and its descendants.

Aura needs role differentiation:

- **Admin** — full access (view, manage, delegate, create)
- **Viewer** — read-only access (view only)
- **Educator** — potentially a middle tier (view + manage lessons, but not create outlets)
- Future roles may be added

The challenge: Keycloak resource permissions grant **all scopes listed on the permission** when any linked policy matches. A single permission with scopes `[view, manage, delegate]` linked to both an admin-policy and a viewer-policy would give viewers manage/delegate access — which is wrong.

## Scale Reference

All scale analysis uses: 10 institutions × 5 outlets × 20 courses × 30 lessons = **31,060 resources**.

Roles assumed: **admin** + **viewer** (2 roles). Each additional role multiplies the relevant metric linearly.

---

## Option 1: Scope-Based Permissions

### How It Works

Replace each **single resource permission** with **one permission per scope** (or per scope-group). Each scope-level permission links only to the policies whose roles should have that scope.

### Permission Structure (per resource)

```
resource: outlet:{id}

outlet-{id}-view-permission
  scopes: [view]
  policies: [system-admin-policy, institution-admin-policy, outlet-admin-policy,
             system-viewer-policy, institution-viewer-policy, outlet-viewer-policy]

outlet-{id}-manage-permission
  scopes: [manage, delegate]
  policies: [system-admin-policy, institution-admin-policy, outlet-admin-policy]

outlet-{id}-create-permission
  scopes: [create]
  policies: [system-admin-policy, institution-admin-policy, outlet-admin-policy]
```

Viewers are linked to the `view` permission only. Admins are linked to all three. Keycloak evaluates each permission independently — a viewer matches the view permission but not manage/create, so their RPT contains only `view` scope for that resource.

### Group Hierarchy

Two parallel subtrees under the system group, one per role:

```
/system:1:admin
  /institution:{id}:admin
    /outlet:{id}:admin
      /course:{id}:admin
        /lesson:{id}:admin

/system:1:viewer
  /institution:{id}:viewer
    /outlet:{id}:viewer
      /course:{id}:viewer
        /lesson:{id}:viewer
```

Each subtree has its own policy with `extendChildren: true`. The view permission links to both admin and viewer policies. The manage/create permissions link only to admin policies.

### Scale Analysis

| Metric | ADR-002 (admin only) | This Option (2 roles) | This Option (3 roles) |
|---|---|---|---|
| Groups | 31,060 | 62,120 (×2) | 93,180 (×3) |
| Policies | 31,060 | 62,120 (×2) | 93,180 (×3) |
| Permissions | 31,060 | 93,180 (×3 scopes) | 93,180 (×3 scopes) |
| Policy links per permission | 4-5 | view: 8-10, manage: 4-5, create: 4-5 | view: 12-15, manage: 4-5, create: 4-5 |
| API calls per resource creation | 4 | 8 (2 groups + 2 policies + 1 resource + 3 permissions) | 10 (3 groups + 3 policies + 1 resource + 3 permissions) |
| Keycloak objects total | ~93K | ~217K | ~279K |
| RPT size per scoped request | ~20-50 entries | ~20-50 entries (scoped RPT unchanged) | ~20-50 entries |

### Adding a New Role

To add a new role (e.g., `educator`):

1. Create a new group subtree: `/system:1:educator/institution:{id}:educator/...`
2. Create policies for each group in the subtree
3. Update **every existing view permission** to also link to the new educator policies
4. Create manage permissions if educators should have manage on some resources

**Cost of adding a role to existing data:** Must update all existing view permissions (31K updates) to add the new educator policy. This is a migration, not just new code.

### Pros

- **Precise scope control** — each scope is independently grantable per role
- **Keycloak-native** — scope-based permissions are Keycloak's intended design for this use case
- **RPT contains exact scopes** — the frontend can read the RPT to know exactly what the user can do (view vs manage)
- **No over-granting** — impossible for a viewer to accidentally get manage access

### Cons

- **3× permissions** — 93K permissions instead of 31K (3 scope groups per resource)
- **2× groups and policies per role** — each new role doubles the group tree
- **Policy links grow with roles** — the view permission links to all role policies; with 5 roles that's 20-25 policy links per view permission
- **Adding a role requires migrating existing permissions** — must update all view permissions to include the new role's policy
- **API calls per resource creation scale with roles** — 2 additional calls per role (group + policy)
- **Keycloak admin console becomes unwieldy** — 200K+ objects at scale with 2 roles

---

## Option 2: Role-Scoped Resource Permissions (Parallel Permissions per Role)

### How It Works

Instead of splitting by scope, create **one resource permission per role**. Each permission lists only the scopes that role should have.

### Permission Structure (per resource)

```
resource: outlet:{id}

outlet-{id}-admin-permission
  scopes: [view, manage, delegate, create]
  policies: [system-admin-policy, institution-admin-policy, outlet-admin-policy]

outlet-{id}-viewer-permission
  scopes: [view]
  policies: [system-viewer-policy, institution-viewer-policy, outlet-viewer-policy]
```

Keycloak evaluates both permissions. An admin matches the admin permission and gets all scopes. A viewer matches the viewer permission and gets only `view`. If a user is in both groups (unlikely but possible), they get the union of scopes.

### Group Hierarchy

Same as Option 1 — parallel subtrees per role:

```
/system:1:admin
  /institution:{id}:admin
    ...

/system:1:viewer
  /institution:{id}:viewer
    ...
```

### Scale Analysis

| Metric | ADR-002 (admin only) | This Option (2 roles) | This Option (3 roles) |
|---|---|---|---|
| Groups | 31,060 | 62,120 (×2) | 93,180 (×3) |
| Policies | 31,060 | 62,120 (×2) | 93,180 (×3) |
| Permissions | 31,060 | 62,120 (×2) | 93,180 (×3) |
| Policy links per permission | 4-5 | 4-5 (unchanged per permission) | 4-5 (unchanged) |
| API calls per resource creation | 4 | 7 (2 groups + 2 policies + 1 resource + 2 permissions) | 10 (3 groups + 3 policies + 1 resource + 3 permissions) |
| Keycloak objects total | ~93K | ~186K | ~279K |
| RPT size per scoped request | ~20-50 entries | ~20-50 entries | ~20-50 entries |

### Adding a New Role

To add a new role (e.g., `educator`):

1. Create a new group subtree: `/system:1:educator/institution:{id}:educator/...`
2. Create policies for each group in the subtree
3. Create a new permission per existing resource: `outlet-{id}-educator-permission` with educator-specific scopes
4. No existing permissions need modification

**Cost of adding a role to existing data:** Must create 31K new permissions (one per resource) and 31K new groups + policies. But existing permissions are untouched — no migration of existing data.

### Pros

- **Existing permissions never modified when adding roles** — new roles only add new permissions, never update existing ones
- **Simpler policy links** — each permission links to only its own role's policies (4-5 links), not all roles
- **Fewer permissions than Option 1** — 2× instead of 3× (scales with roles, not scopes)
- **Clear separation** — each permission is self-contained for one role; easy to audit "what does this role get?"
- **Role removal is clean** — delete the role's permissions, policies, and groups; nothing else changes

### Cons

- **Permissions scale with roles** — each new role adds 31K permissions (vs Option 1 which adds 0 permissions per new role, only policy links)
- **Duplicate scope declarations** — `view` scope appears in both admin and viewer permissions; if you add a new scope, you must add it to every role's permission that should have it
- **Keycloak evaluation overhead** — Keycloak evaluates all permissions for a resource; with 5 roles, that's 5 permission evaluations per resource per RPT request (though only matching ones produce results)

---

## Option 3: Single Permission + Scope-Level Policies (Hybrid)

### How It Works

Keep **one resource permission per resource** (like ADR-002), but use **scope-level policies** that encode which scopes each role gets. Keycloak's policy evaluation with scope-level granularity handles the rest.

### Permission Structure (per resource)

```
resource: outlet:{id}

outlet-{id}-permission
  scopes: [view, manage, delegate, create]
  policies: [outlet-{id}-scope-policy]

outlet-{id}-scope-policy (type: aggregated, strategy: AFFIRMATIVE)
  sub-policies:
    - outlet-{id}-admin-scope-policy (JS or aggregated)
        → if user in admin group → grant: view, manage, delegate, create
    - outlet-{id}-viewer-scope-policy (JS or aggregated)
        → if user in viewer group → grant: view
```

### Why This Doesn't Work Well

Keycloak's standard policy types (group, role, user) don't support **conditional scope granting**. A group policy evaluates to PERMIT or DENY — it doesn't say "PERMIT but only for these scopes." To achieve scope-conditional logic, you need:

- **JavaScript policies** — ADR-001 rejected these (security risk, opaque, hard to debug)
- **Complex aggregated policy trees** — one aggregated policy per resource containing sub-policies per role per scope, which effectively recreates Option 1's structure inside a single permission

This option is theoretically elegant but practically unworkable with Keycloak's policy model.

### Scale Analysis

Not applicable — the implementation complexity makes this impractical.

---

## Comparison Matrix

| Criteria | Option 1: Scope-Based | Option 2: Role-Scoped | Option 3: Hybrid |
|---|---|---|---|
| Permissions (2 roles) | 93K (3× scopes) | 62K (2× roles) | 31K (1×) |
| Permissions (5 roles) | 93K (constant) | 155K (5× roles) | 31K (1×) |
| Groups + policies (2 roles) | 124K | 124K | 124K |
| Policy links per permission | Grows with roles | Constant (4-5) | N/A |
| Adding a role: new objects | 62K (groups + policies) | 93K (groups + policies + permissions) | N/A |
| Adding a role: existing updates | 31K permission updates | 0 | N/A |
| Removing a role | Update 31K permissions | Delete role's objects only | N/A |
| API calls per resource (2 roles) | 8 | 7 | N/A |
| Scope precision | Per-scope | Per-role (bundle of scopes) | Per-scope |
| Keycloak JS policies required | No | No | Yes |
| Implementation complexity | Moderate | Moderate | High |

### Scaling Crossover Point

- **Few roles (2-3):** Option 2 wins — fewer permissions, no existing-permission migrations
- **Many roles (5+):** Option 1 wins — permissions stay constant at 3× resources regardless of role count; Option 2 grows linearly with roles
- **Frequent role additions:** Option 2 wins — no existing permission updates needed
- **Frequent role removals:** Option 2 wins — clean deletion, no permission modification
- **Precise per-scope control needed:** Option 1 wins — each scope is independently linkable

---

## Recommendation

**Option 2 (Role-Scoped Resource Permissions)** for Aura's current stage.

### Why Option 2

1. **Aura has 2-3 roles, not 10.** Admin, viewer, and possibly educator. At this scale, Option 2 creates fewer total objects (186K vs 217K for 2 roles).

2. **No migration when adding roles.** When the educator role is added later, create new groups + policies + permissions. No existing admin or viewer permissions are touched. This is operationally safer.

3. **Simpler mental model.** Each permission is self-contained: "this permission grants these scopes to users matching these policies." No cross-cutting concerns where a view permission must reference every role's policy.

4. **Role removal is clean.** If a role is deprecated, delete its permissions, policies, and groups. Nothing else changes.

5. **Policy link count stays constant.** Each permission links to 4-5 policies (its own role's hierarchy). Option 1's view permission would link to 8-15 policies as roles are added, increasing Keycloak evaluation time.

### When to Reconsider

Switch to Option 1 if:
- Aura grows beyond 5 distinct roles (permissions would exceed 155K in Option 2 vs constant 93K in Option 1)
- Per-scope granularity becomes important (e.g., some roles can `manage` but not `create`, and this varies per resource instance)

### Implementation Impact on ADR-002

The changes to the ADR-002 implementation are additive:

1. **`GroupAuthorizationService`** — no interface changes needed
2. **`AuthorizationGroupPath`** — add methods for viewer paths: `institutionViewer(id)`, `outletViewer(id)`, etc.
3. **`AuthorizationNaming`** — add methods for viewer naming: `viewerPolicyName(type, id)`, `viewerPermissionName(type, id)`
4. **Resource creation services** — create 2 groups, 2 policies, 2 permissions per resource instead of 1 each
5. **`realm-import.json`** — add `system:1:viewer` group + `system-1-viewer-policy` + type-level viewer permissions
6. **Scoped RPT** — unchanged; RPT returns scope-annotated permissions regardless of how they were granted

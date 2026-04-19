# ADR-001: Keycloak UMA Instance-Level Authorization

**Status:** Accepted  
**Date:** 2026-04-19  
**Decision Makers:** Kevin  

## Context

Aura is a multi-tenant education management platform where institutions own outlets, and different users need different levels of access to specific resource instances. The original authorization model used **type-level role-based policies** in Keycloak — an `institution-admin` role granted blanket access to *all* outlets, courses, lessons, etc. regardless of which institution they belonged to.

This is insufficient for a multi-tenant platform because:

- An institution-admin of Institution A could access resources belonging to Institution B
- There was no way to grant a user access to a specific outlet without granting access to all outlets
- No outlet-level administrative roles existed

We needed **instance-level authorization** — protecting individual resource instances (e.g., `outlet:outlet-001`) rather than resource types (e.g., `outlet`).

## Decision

We adopted **Keycloak Groups + Group Policies** for instance-level UMA authorization.

### Core Concepts

Every entity instance is a **separate UMA resource** in Keycloak. **Groups** represent administrative roles scoped to specific instances. **Group policies** evaluate group membership. **Resource permissions** link resources to policies with an `AFFIRMATIVE` decision strategy (any matching policy grants access).

### Resource Lifecycle

#### Institution Creation

When an institution is onboarded:

1. Save institution to DB (generates UUID as both `id` and `tenantId`)
2. Register UMA resource `institution:{institutionId}` (type: `urn:aura:institution`)
3. Create Keycloak group `institution:{institutionId}:admin`
4. Create group policy `institution-{institutionId}-admin-policy`
5. Create resource permission `institution-{institutionId}-permission` linked to the group policy

#### Outlet Creation

When an outlet is created under an institution:

1. Save outlet to DB
2. Register UMA resource `outlet:{outletId}` (type: `urn:aura:outlet`)
3. Create Keycloak group `outlet:{outletId}:admin`
4. Create group policy `outlet-{outletId}-admin-policy`
5. Create resource permission `outlet-{outletId}-permission` linked to **both**:
   - `outlet-{outletId}-admin-policy` (outlet-specific access)
   - `institution-{institutionId}-admin-policy` (institution-wide inheritance)

The dual-policy link with `AFFIRMATIVE` decision strategy means a user matching **either** policy gets access. This is how institution admins automatically inherit access to all outlets under their institution.

#### User Assignment

- **Institution admin** -- add user to group `institution:{institutionId}:admin` -- gains access to the institution resource AND all outlets under it
- **Outlet admin** -- add user to group `outlet:{outletId}:admin` -- gains access to only that specific outlet

### Authorization Evaluation Flow

```
User Request: query getOutletsByPage for outlet-001
  |
  v
KeycloakAuthorizationInterceptor
  1. Extracts operation name from GraphQL document
  2. Looks up @ProtectedOperation annotation -> resource="outlet", scope="view"
  3. Resolves resource ID from request -> "outlet:outlet-001"
  4. Calls PolicyEnforcementService.isAuthorized(token, "outlet:outlet-001", "view")
  |
  v
Keycloak Token Endpoint (UMA ticket grant)
  POST grant_type=urn:ietf:params:oauth:grant-type:uma-ticket
       &permission=outlet:outlet-001#view
       &response_mode=decision
  |
  v
Keycloak evaluates permission "outlet-outlet-001-permission"
  Checks policies (AFFIRMATIVE = any match grants):
    - outlet-outlet-001-admin-policy -> is user in group "outlet:outlet-001:admin"?
    - institution-inst-001-admin-policy -> is user in group "institution:inst-001:admin"?
  |
  v
  GRANT (if either matches) or DENY (if neither matches)
```

### Access Matrix

| User Role | Institution Resource | Outlets Under Own Institution | Outlets Under Other Institution |
|-----------|---------------------|-------------------------------|--------------------------------|
| aura-admin | Full access (wildcard policy) | Full access | Full access |
| institution-admin (inst-001) | View/manage inst-001 | Full access to all | No access |
| outlet-admin (outlet-003) | No access | Only outlet-003 | No access |

### Naming Conventions

| Entity | Group Name | Policy Name | Permission Name |
|--------|-----------|-------------|-----------------|
| Institution `{id}` | `institution:{id}:admin` | `institution-{id}-admin-policy` | `institution-{id}-permission` |
| Outlet `{id}` | `outlet:{id}:admin` | `outlet-{id}-admin-policy` | `outlet-{id}-permission` |

### Super-Admin Bypass

The `aura-admin` role retains a **type-level wildcard policy** (`aura-admin-all-access`) that grants all scopes on all resource types. This is the only type-level policy that remains. All institution-admin policies were removed in favor of instance-level group policies.

## Alternatives Considered

### Alternative 1: JavaScript/ABAC Policy

A single JavaScript policy in Keycloak evaluates token claims (e.g., `managed_institutions`, `managed_outlets` user attributes) against the requested resource at evaluation time.

**Pros:** Minimal Keycloak configuration, scales to thousands of resources without creating thousands of policies.  
**Cons:** JS policies must be explicitly enabled in Keycloak (security risk surface), harder to debug, token bloat if a user manages many outlets, policy logic is opaque and not visible in the Keycloak admin console.

**Rejected because:** Harder to audit, debug, and maintain. Group-based approach makes permissions explicitly visible in the admin console.

### Alternative 2: UMA Permission Tickets (Resource-Owner Managed Access)

Resource owners grant permissions to other users via UMA permission tickets. Keycloak manages the consent flow.

**Pros:** Standard UMA 2.0 flow, owner-managed.  
**Cons:** Adds complexity with permission tickets, requesting party tokens, and consent workflows. Better suited for user-to-user sharing (Google Drive model) than admin-managed hierarchical access.

**Rejected because:** Aura's access model is admin-managed and hierarchical, not user-to-user sharing.

### Alternative 3: Keycloak Resource Hierarchy (Parent-Child)

Set the institution resource as the `owner` of outlet resources and rely on cascading permissions.

**Pros:** Conceptually clean.  
**Cons:** Keycloak does not natively cascade permissions from parent to child resources. Would require a custom policy evaluator to walk the hierarchy, adding significant complexity.

**Rejected because:** Keycloak doesn't support this natively; the group-based approach achieves the same result with standard APIs.

## Consequences

### Positive

- **True instance-level isolation**: Users can only access resources they're explicitly granted (via group membership)
- **Automatic inheritance**: Institution admins automatically access all outlets under their institution, without per-outlet permission management
- **Auditable**: Every permission, policy, and group membership is visible in the Keycloak admin console
- **Scalable user management**: Adding/removing an institution admin is a single group membership change -- no need to update every outlet's permission
- **Standard APIs**: Uses only Keycloak Admin Client APIs (groups, policies, permissions) -- no custom policy evaluators

### Negative

- **More Keycloak API calls on resource creation**: Each institution creation makes 4 Keycloak API calls (register resource, create group, create policy, create permission). Each outlet creation makes 4 calls plus links 2 policies. This adds latency to creation operations.
- **Keycloak state growth**: Each resource instance creates a group, a policy, and a permission in Keycloak. For a platform with thousands of outlets, this means thousands of groups/policies. Keycloak handles this well, but it's a consideration for very large deployments.
- **Cleanup required on deletion**: When an institution or outlet is deleted, the corresponding group, policy, permission, and UMA resource must all be cleaned up in Keycloak.

## Implementation

### Codebase Changes

**New interface** (`core/`):
- `GroupAuthorizationService` -- 4 methods: `createGroup`, `createGroupPolicy`, `createResourcePermission`, `addUserToGroup`

**New implementation** (`details/`):
- `KeycloakGroupAuthorizationService` -- uses Keycloak Admin Client (`GroupsResource`, `ClientResource.authorization()`, `UsersResource`)

**Modified files**:
- `DefaultInstitutionOnboardingStrategy` -- creates group + policy + permission after resource registration
- `OutletService.createOutlet()` -- creates group + policy + permission with institution admin policy inheritance
- `AccountService.createAccountWithInstitutionAdminRole()` -- adds user to institution admin group
- `UseCaseConfig`, `ServiceConfig`, `KeycloakClientConfig` -- bean wiring

**Test configuration**:
- `realm-import.json` -- removed type-level institution-admin policies (`institution-admin-policy`, `institution-admin-resource-access`, `institution-admin-institution-view`, `institution-admin-level-view`), kept `aura-admin-all-access`

### Architecture Alignment

This implementation follows the project's Clean Architecture:
- `GroupAuthorizationService` interface lives in `core/` (no Spring dependencies)
- `KeycloakGroupAuthorizationService` lives in `details/` (infrastructure)
- Bean wiring in `application/configs/` (manual `new`, not `@Component`)
- `@Transactional` only in `details/services/`

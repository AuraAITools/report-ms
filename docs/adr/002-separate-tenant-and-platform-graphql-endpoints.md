# ADR-002: Separate Tenant and Platform GraphQL Endpoints

**Status:** Accepted  
**Date:** 2026-04-20  
**Decision Makers:** Kevin  

## Context

Aura is a multi-tenant education management platform. Every tenant-scoped GraphQL request passes through `UserRequestContextInterceptor`, which requires an `x-tenant-id` header containing a valid UUID and validates the authenticated user has access to that tenant.

This creates a problem for **platform-level operations** like `createInstitution` — the institution (and therefore the tenant) doesn't exist yet. Previously, this was worked around with an aggregate query pattern (`x-tenant-id: *`), but that approach was removed (see aggregate query removal) because it blurred the security boundary between platform-wide and tenant-scoped operations.

We needed a clean separation between:
- **Platform operations** — tenant-independent, performed by aura-admins (e.g., onboarding institutions, platform analytics)
- **Tenant operations** — tenant-scoped, performed by institution-admins and outlet-admins (e.g., creating outlets, managing courses)

## Decision

We introduced **two independent GraphQL endpoints** served from the same Spring Boot application, each with its own schema, interceptor chain, and controller set.

### Endpoint Architecture

```
POST /platform/graphql     (no x-tenant-id header required)
  → CorrelationIdInterceptor
  → KeycloakAuthorizationInterceptor (group-based: aura-admin)
  → Platform schema + controllers

POST /graphql               (x-tenant-id: <uuid> required)
  → CorrelationIdInterceptor
  → UserRequestContextInterceptor (validates tenant access)
  → KeycloakAuthorizationInterceptor (UMA instance-level)
  → Tenant schema + controllers
```

### How It Works

Each endpoint has a fully independent GraphQL pipeline, manually wired in Spring:

| Component | Tenant (`/graphql`) | Platform (`/platform/graphql`) |
|-----------|--------------------|-----------------------------|
| Config class | `TenantGraphQLConfig` (Spring Boot auto-configured) | `PlatformGraphQlConfig` (manually wired) |
| Schema location | `graphql/` (auto-discovered) | `graphql-platform/**/*.graphqls` (pattern-matched) |
| Interceptors | `UserRequestContextInterceptor` → `KeycloakAuthorizationInterceptor` | `KeycloakAuthorizationInterceptor` only |
| Auth strategy | UMA instance-level (evaluates permissions on specific resources) | Group-based (aura-admin group membership) |
| Requires `x-tenant-id` | Yes (valid UUID) | No |
| Controllers | `controllers/graphql/v1/tenant/` | `controllers/graphql/v1/platform/` |

### Authorization Strategy Per Endpoint

| Endpoint | Who can access | How it's enforced |
|----------|---------------|-------------------|
| `/platform/graphql` | aura-admin only | `@ProtectedOperation` evaluated via Keycloak — aura-admin has wildcard policy on all resource types |
| `/graphql` | institution-admins, outlet-admins | `@ProtectedOperation` evaluated via Keycloak UMA — instance-level group policies on specific resources (see ADR-001) |

Platform operations don't need UMA resource evaluation — the `aura-admin-all-access` policy grants access to all resource types. Tenant operations use instance-level group policies created dynamically when resources are provisioned.

### Schema Isolation

Each endpoint has its own schema namespace. Operations defined in the platform schema are invisible on the tenant endpoint, and vice versa:

```
graphql-platform/           # Platform endpoint schemas
  schema.graphqls            # type Query { ... } type Mutation { ... }
  institution.graphqls       # Platform-specific institution operations

graphql/                     # Tenant endpoint schemas (auto-discovered by Spring Boot)
  types/                     # Shared input/output types
  operations/                # Tenant-scoped queries and mutations
```

### Shared Configuration

Both endpoints share the same cross-cutting concerns:
- **Scalars:** `ExtendedScalars.Date`, `ExtendedScalars.GraphQLLong`, `ExtendedScalars.DateTime`
- **Validation:** `ValidationSchemaWiring` for directive-based input validation
- **Exception handling:** `GraphQlExceptionResolver` for consistent error response format
- **Correlation IDs:** `CorrelationIdInterceptor` for request tracing

These are configured independently in each config class (not shared beans) because the platform endpoint bypasses Spring Boot's GraphQL auto-configuration.

## Alternatives Considered

### Alternative 1: Single Schema with Namespace Types

Use wrapper types (`PlatformQueries`, `TenantQueries`) to logically separate operations on a single `/graphql` endpoint.

```graphql
type Query {
  platform: PlatformQueries!
  tenant: TenantQueries!
}
```

**Rejected because:** Same interceptor chain for everything — would need branching logic inside interceptors to skip tenant validation for platform operations. No true schema isolation — all operations visible to all clients. Re-introduces the complexity we removed with the aggregate query cleanup.

### Alternative 2: Separate Microservice

Extract platform operations into a dedicated `admin-ms` service.

**Rejected because:** Operational overhead (separate CI/CD, monitoring, infrastructure) is not justified for a small number of platform operations today. Shared domain logic (entities, repositories, Keycloak clients) would need to be extracted into a shared library. Can be revisited if the platform API grows significantly.

## Consequences

### Positive

- **Clean security boundary** — platform endpoint has no tenant header requirement, tenant endpoint enforces tenant isolation. No conditional branching in interceptors.
- **Schema isolation** — platform operations are invisible to tenant clients and vice versa. Clients only see what's relevant to them.
- **Independent interceptor chains** — each endpoint runs exactly the interceptors it needs, nothing more.
- **Matches industry patterns** — AWS, Stripe, Shopify, and Google Cloud all separate control plane from data plane at the entry point, not deep in business logic.
- **Future-proof** — the platform endpoint can grow (list institutions, platform analytics, system config) without affecting the tenant API. If it grows enough, extracting to a separate microservice is straightforward.

### Negative

- **Verbose setup** — the platform endpoint must be manually wired, bypassing Spring Boot's GraphQL auto-configuration. Each new cross-cutting concern (scalars, exception resolvers, validation) must be configured in both places.
- **Controller auto-registration** — Spring Boot only auto-registers `@Controller` beans with the primary endpoint. The platform endpoint uses a separate `AnnotatedControllerConfigurer` to discover platform controllers.
- **Duplicate configuration** — scalars, validation wiring, and exception resolvers are defined in both `TenantGraphQLConfig` and `PlatformGraphQlConfig`. Changes to shared concerns must be applied in both places.

### Operations Classified

| Operation | Endpoint | Reason |
|-----------|----------|--------|
| `createInstitution` | `/platform/graphql` | Creates a new tenant — no tenant exists yet |
| All other operations (14) | `/graphql` | Tenant-scoped — require `x-tenant-id` for data isolation |

As the system grows, operations like `listInstitutions`, `deactivateInstitution`, and platform analytics would be added to the platform endpoint.

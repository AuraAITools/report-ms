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

POST /tenant/graphql        (x-tenant-id: <uuid> required)
  → CorrelationIdInterceptor
  → UserRequestContextInterceptor (validates tenant access)
  → KeycloakAuthorizationInterceptor (UMA instance-level)
  → Tenant schema + controllers
```

### How It Works

The tenant endpoint uses Spring Boot's auto-configured GraphQL stack. The platform endpoint is a fully manual pipeline built inside a single `RouterFunction` bean — no intermediate beans (`GraphQlSource`, `ExecutionGraphQlService`, `GraphQlHttpHandler`) are exposed, which prevents conflicts with auto-configuration.

| Component | Tenant (`/tenant/graphql`) | Platform (`/platform/graphql`) |
|-----------|--------------------|-----------------------------|
| Config class | `TenantGraphQLConfig` (Spring Boot auto-configured) | `PlatformGraphQlConfig` (single `RouterFunction` bean) |
| Schema location | `classpath:graphql-tenant/` (configured via `spring.graphql.schema.locations`) | `classpath:graphql-platform/**/*.graphqls` (pattern-matched via `ResourcePatternResolver`) |
| Interceptors | Auto-wired `WebGraphQlInterceptor` beans (via `InterceptorConfig`) | Manually wired inline: `CorrelationIdInterceptor` + `KeycloakAuthorizationInterceptor` |
| Auth strategy | UMA instance-level (evaluates permissions on specific resources) | Group-based (aura-admin group membership) |
| Requires `x-tenant-id` | Yes (valid UUID) | No |
| Controllers | `controllers/graphql/v1/` (auto-discovered via `@Controller`) | `controllers/graphql/v1/platform/` (auto-discovered — schema isolation prevents cross-registration) |

### Authorization Strategy Per Endpoint

| Endpoint | Who can access | How it's enforced |
|----------|---------------|-------------------|
| `/platform/graphql` | aura-admin only | `@ProtectedOperation` evaluated via Keycloak — aura-admin has wildcard policy on all resource types |
| `/tenant/graphql` | institution-admins, outlet-admins | `@ProtectedOperation` evaluated via Keycloak UMA — instance-level group policies on specific resources (see ADR-001) |

Platform operations don't need UMA resource evaluation — the `aura-admin-all-access` policy grants access to all resource types. Tenant operations use instance-level group policies created dynamically when resources are provisioned.

### Schema Isolation

Each endpoint has its own schema namespace. Operations defined in the platform schema are invisible on the tenant endpoint, and vice versa:

```
graphql-platform/               # Platform endpoint schemas
  query.graphqls                 # type Query { _ping: String }
  mutation.graphqls              # type Mutation {}
  scalar.graphqls                # shared scalar definitions
  types/
    institution.graphqls         # platform-specific types
  operations/
    institutions/
      mutation.graphqls          # extend type Mutation { createInstitution(...) }

graphql-tenant/                  # Tenant endpoint schemas
  query.graphqls                 # type Query {}
  mutation.graphqls              # type Mutation {}
  scalar.graphqls
  directive.graphqls
  types/                         # domain types (outlet, course, account, etc.)
  operations/                    # extend type Query/Mutation per domain
    outlets/
    accounts/
    courses/
    ...
```

### Shared Configuration

Both endpoints share the same cross-cutting concerns, configured independently:
- **Scalars:** `ExtendedScalars.Date`, `ExtendedScalars.GraphQLLong`, `ExtendedScalars.DateTime`
- **Validation:** `ValidationSchemaWiring` for directive-based input validation
- **Exception handling:** `GraphQlExceptionResolver` for consistent error response format
- **Correlation IDs:** `CorrelationIdInterceptor` for request tracing

The tenant endpoint gets these via auto-configured beans (`TenantGraphQLConfig`). The platform endpoint configures them inline in `PlatformGraphQlConfig`.

## Implementation Details

### Why No Intermediate Beans for Platform

The platform pipeline is built entirely inside a single `RouterFunction<ServerResponse>` bean. No `GraphQlSource`, `ExecutionGraphQlService`, `WebGraphQlHandler`, or `GraphQlHttpHandler` beans are exposed. This is critical because:

1. **Spring Boot's `GraphQlAutoConfiguration`** uses `@ConditionalOnMissingBean` — if any `GraphQlSource` or `GraphQlHttpHandler` bean exists, the auto-configured tenant stack won't be created.
2. **`AnnotatedControllerConfigurer`** implements `RuntimeWiringConfigurer` — if registered as a bean, Spring Boot auto-wires it into the tenant's `GraphQlSource`, causing all controllers (including platform ones) to be registered into the tenant schema.
3. By keeping everything inline, the platform pipeline is completely invisible to Spring Boot's auto-configuration.

### Controller Discovery

Both the auto-configured `AnnotatedControllerConfigurer` (tenant) and the inline one (platform) scan all `@Controller` beans. However, **schema isolation** prevents cross-contamination:
- A platform controller with `@MutationMapping("createInstitution")` won't match any field in the tenant schema — it shows as an "unmapped registration" in the inspection log but causes no harm.
- A tenant controller with `@MutationMapping("createOutlet")` won't match any field in the platform schema.

### Introspection

Both endpoints support GraphQL introspection. The `UserRequestContextInterceptor` skips introspection queries (containing `__schema` or `__type`) so Postman can fetch the tenant schema without an `x-tenant-id` header.

### Configuration

```yaml
# application.yaml
spring:
  graphql:
    path: /tenant/graphql
    schema:
      locations:
        - classpath:graphql-tenant/
        - classpath:graphql-tenant/types/
        - classpath:graphql-tenant/operations/outlets/
        - classpath:graphql-tenant/operations/accounts/
        # ... all subdirectories listed explicitly
```

Schema subdirectories must be listed explicitly because Spring Boot's `schema.locations` scans each directory for `.graphqls` files but doesn't recurse automatically.

## How to Add a New Endpoint or Extend Existing Ones

### Adding a New Operation to an Existing Endpoint

**Tenant endpoint (`/tenant/graphql`):**
1. Add schema file in `graphql-tenant/operations/<domain>/mutation.graphqls` or `query.graphqls` using `extend type Mutation { ... }` or `extend type Query { ... }`
2. Add the subdirectory to `spring.graphql.schema.locations` in `application.yaml` (if it's a new subdirectory)
3. Add type definitions in `graphql-tenant/types/<domain>.graphqls`
4. Create controller in `controllers/graphql/v1/<domain>/` with `@Controller`
5. Run `mvn generate-sources -f application/pom.xml` to regenerate codegen types

**Platform endpoint (`/platform/graphql`):**
1. Add schema file in `graphql-platform/operations/<domain>/mutation.graphqls` or `query.graphqls`
2. Add type definitions in `graphql-platform/types/<domain>.graphqls`
3. Create controller in `controllers/graphql/v1/platform/<domain>/` with `@Controller`
4. No YAML changes needed — `ResourcePatternResolver` with `classpath:graphql-platform/**/*.graphqls` auto-discovers all files

### Adding a Third GraphQL Endpoint

If a new audience needs its own endpoint (e.g., `/public/graphql` for unauthenticated access):

1. Create `PublicGraphQlConfig.java` following the `PlatformGraphQlConfig` pattern
2. Build the entire pipeline inside a single `RouterFunction<ServerResponse>` bean — do NOT expose `GraphQlSource`, `GraphQlHttpHandler`, or `AnnotatedControllerConfigurer` as beans
3. Create schema files in a new directory (e.g., `graphql-public/`)
4. Create controllers in a new package (e.g., `controllers/graphql/v1/public/`)
5. Wire appropriate interceptors (e.g., rate limiting but no auth)

**Key rule:** Only the primary tenant endpoint uses Spring Boot auto-configuration. All additional endpoints must be fully inline to avoid `@ConditionalOnMissingBean` conflicts.

### Extracting to a Separate Microservice

If the platform API grows significantly:
1. Extract platform controllers, schema files, and `PlatformGraphQlConfig` into a new service
2. Share domain logic via a common library (core module)
3. The new service can use Spring Boot's auto-configured GraphQL stack (since it's the only endpoint)
4. Update API gateway routing to point `/platform/graphql` to the new service

## Alternatives Considered

### Alternative 1: Single Schema with Namespace Types

Use wrapper types (`PlatformQueries`, `TenantQueries`) to logically separate operations on a single endpoint.

**Rejected because:** Same interceptor chain for everything — would need branching logic inside interceptors to skip tenant validation for platform operations. No true schema isolation — all operations visible to all clients.

### Alternative 2: Separate Microservice

Extract platform operations into a dedicated `admin-ms` service.

**Rejected because:** Operational overhead (separate CI/CD, monitoring, infrastructure) is not justified for a small number of platform operations today. Can be revisited if the platform API grows significantly.

### Alternative 3: Expose Intermediate Beans

Register `GraphQlSource`, `ExecutionGraphQlService`, etc. as named beans with `@Qualifier`.

**Rejected because:** Spring Boot's `@ConditionalOnMissingBean` checks by type, not by name. Any `GraphQlSource` bean blocks auto-configuration of the tenant stack. Workarounds (`@Primary`, `@ConditionalOnBean`) are fragile and version-dependent.

## Consequences

### Positive

- **Clean security boundary** — platform endpoint has no tenant header requirement, tenant endpoint enforces tenant isolation
- **Schema isolation** — platform operations invisible to tenant clients and vice versa
- **Independent interceptor chains** — each endpoint runs exactly the interceptors it needs
- **No auto-config conflicts** — platform pipeline is fully inline, invisible to Spring Boot
- **Extensible** — new endpoints follow the same `RouterFunction`-only pattern
- **Matches industry patterns** — AWS, Stripe, Shopify all separate control plane from data plane

### Negative

- **Platform config is verbose** — entire pipeline built in one method since no intermediate beans can be exposed
- **Schema locations must be explicit** — each tenant subdirectory listed in YAML
- **Duplicate cross-cutting config** — scalars, validation, exception resolvers configured in both places
- **Controller cross-registration** — both `AnnotatedControllerConfigurer` instances see all `@Controller` beans; schema isolation prevents issues but produces "unmapped registration" warnings in logs

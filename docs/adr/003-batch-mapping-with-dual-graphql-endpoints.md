# ADR-003: @BatchMapping with Dual GraphQL Endpoints

**Status:** Accepted  
**Date:** 2026-04-22  
**Decision Makers:** Kevin  
**Supersedes:** N/A  
**Related:** [ADR-002: Separate Tenant and Platform GraphQL Endpoints](002-separate-tenant-and-platform-graphql-endpoints.md)

## Context

We needed to add nested `outlets` to the `getUserInstitutions` GraphQL query on the tenant endpoint. The outlets field should only trigger a DB call when the client includes it in the selection set, and must avoid N+1 queries when multiple institutions are returned.

Spring GraphQL's `@BatchMapping` is the idiomatic solution — it registers a `DataLoader` that batches all parent objects into a single resolver call. However, adding `@BatchMapping` to any controller immediately crashed the application:

```
java.lang.IllegalStateException: More than one DataLoader named 'InstitutionResponse.outlets'
```

This is a direct consequence of ADR-002's dual-endpoint architecture. The platform endpoint (`PlatformGraphQlConfig`) creates its own `AnnotatedControllerConfigurer` instance to wire controllers into the platform schema. Both the auto-configured (tenant) and manually created (platform) `AnnotatedControllerConfigurer` instances scan **all** `@Controller` beans and register discovered `@BatchMapping` methods with the **same shared `BatchLoaderRegistry` bean** — causing the duplicate DataLoader name.

### How the Duplicate Registration Happens

1. Spring Boot auto-configures an `AnnotatedControllerConfigurer` bean and a `BatchLoaderRegistry` bean
2. During the auto-configured bean's `afterPropertiesSet()`, it scans all `@Controller` beans, finds the `@BatchMapping` method, and registers a batch loader named `InstitutionResponse.outlets` in the shared `BatchLoaderRegistry`
3. `PlatformGraphQlConfig` creates a **second** `AnnotatedControllerConfigurer` instance with `setApplicationContext(applicationContext)` + `afterPropertiesSet()`
4. This second instance also scans all `@Controller` beans, finds the same `@BatchMapping` method, and registers it again in the **same** `BatchLoaderRegistry` (looked up from the shared `ApplicationContext`)
5. At request time, `DefaultBatchLoaderRegistry.registerDataLoader()` throws because the name already exists

## Decision

Isolate the platform's `AnnotatedControllerConfigurer` from the tenant's `BatchLoaderRegistry` by giving it a **child `ApplicationContext`** with its own `BatchLoaderRegistry` instance.

```java
// PlatformGraphQlConfig.java
GenericApplicationContext platformContext = new GenericApplicationContext();
platformContext.setParent(applicationContext);
platformContext.registerBean(BatchLoaderRegistry.class,
        (Supplier<BatchLoaderRegistry>) DefaultBatchLoaderRegistry::new);
platformContext.refresh();

AnnotatedControllerConfigurer controllerConfigurer = new AnnotatedControllerConfigurer();
controllerConfigurer.setApplicationContext(platformContext);
controllerConfigurer.afterPropertiesSet();
```

### How It Works

- The child context **inherits** all beans from the parent (including `@Controller` beans) so controller discovery still works
- The child context **shadows** the parent's `BatchLoaderRegistry` with its own instance, so `@BatchMapping` methods discovered by the platform configurer are registered in an isolated registry that is never used at request time
- The tenant's auto-configured `AnnotatedControllerConfigurer` continues to use the parent's `BatchLoaderRegistry` — the only one that matters
- `@BatchMapping` works normally on controllers with no special annotations or workarounds

### Controller Code

```java
@BatchMapping(typeName = "InstitutionResponse", field = "outlets")
public Map<InstitutionResponse, List<OutletResponse>> outlets(List<InstitutionResponse> institutions) {
    Set<String> institutionIds = institutions.stream()
            .map(InstitutionResponse::getId)
            .collect(Collectors.toSet());

    Map<String, List<Outlet>> outletsByTenantId = outletQueryUseCase
            .findAllByTenantIds(institutionIds).stream()
            .collect(Collectors.groupingBy(Outlet::getTenantId));

    return institutions.stream()
            .collect(Collectors.toMap(
                    institution -> institution,
                    institution -> outletsByTenantId
                            .getOrDefault(institution.getId(), List.of())
                            .stream()
                            .map(OutletMapper.INSTANCE::toDto)
                            .toList()
            ));
}
```

## Alternatives Considered

### Alternative 1: `@SchemaMapping` (No Batching)

Use `@SchemaMapping` instead of `@BatchMapping`. Each parent object triggers a separate resolver call.

```java
@SchemaMapping(typeName = "InstitutionResponse", field = "outlets")
public List<OutletResponse> outlets(InstitutionResponse institution) {
    return outletQueryUseCase.findAllByTenantIds(Set.of(institution.getId())).stream()
            .map(OutletMapper.INSTANCE::toDto)
            .toList();
}
```

**Rejected because:** N+1 queries. If `getUserInstitutions` returns 5 institutions, this fires 5 separate DB calls for outlets.

### Alternative 2: Manual `BatchLoaderRegistry` + `@SchemaMapping` with `DataLoader`

Register the batch loader manually via `BatchLoaderRegistry` in a `@Configuration` class, then use `@SchemaMapping` with `DataFetchingEnvironment` to access the `DataLoader` by name.

```java
// BatchLoaderConfig.java
@Configuration
public class BatchLoaderConfig {
    public BatchLoaderConfig(BatchLoaderRegistry registry, OutletQueryUseCase useCase) {
        registry.<String, List<OutletResponse>>forName("institutionOutlets")
                .registerMappedBatchLoader((ids, env) -> { /* ... */ });
    }
}

// Controller
@SchemaMapping(typeName = "InstitutionResponse", field = "outlets")
public CompletableFuture<List<OutletResponse>> outlets(
        InstitutionResponse institution, DataFetchingEnvironment env) {
    DataLoader<String, List<OutletResponse>> loader = env.getDataLoader("institutionOutlets");
    return loader.load(institution.getId());
}
```

**Rejected because:** More boilerplate — splits the batching logic across two files, requires manual DataLoader name management, and loses the declarative simplicity of `@BatchMapping`.

### Alternative 3: Inject the Auto-Configured `AnnotatedControllerConfigurer` Bean

Reuse the auto-configured bean in `PlatformGraphQlConfig` instead of creating a second instance.

```java
@Bean
RouterFunction<ServerResponse> platformGraphQlRouterFunction(
        AnnotatedControllerConfigurer controllerConfigurer, ...) {
    // Use the injected bean — no afterPropertiesSet() call needed
}
```

**Rejected because:** Doesn't solve the problem. The auto-configured bean has already registered batch loaders in the shared `BatchLoaderRegistry` during its own `afterPropertiesSet()`. Using it as a `RuntimeWiringConfigurer` for the platform `GraphQlSource` is fine, but it doesn't prevent the platform from needing its own controller discovery (the tenant and platform schemas may differ).

### Alternative 4: Exclude `GraphQlAutoConfiguration`

Disable Spring Boot's GraphQL auto-configuration entirely and manually configure both endpoints.

**Rejected because:** Too invasive. The tenant endpoint benefits significantly from auto-configuration (cursors, pagination, exception resolvers, interceptor wiring). Manually recreating all of this is error-prone and version-dependent.

### Alternative 5: Override `ExecutionGraphQlService` to Filter `DataLoaderRegistrar` Beans

Provide a custom `ExecutionGraphQlService` bean that selectively registers only certain `DataLoaderRegistrar` beans.

**Rejected because:** Fragile — depends on internal bean naming and ordering. Also requires understanding Spring Boot's auto-configuration internals, which change between versions.

## Consequences

### Positive

- `@BatchMapping` works idiomatically — no workarounds in controller code
- Single DB call for outlets regardless of how many institutions are returned (no N+1)
- Outlets query only fires when the `outlets` field is in the GraphQL selection set
- Minimal change — only `PlatformGraphQlConfig` is modified (3 lines added)
- Future `@BatchMapping` methods on any controller will work without additional configuration

### Negative

- The child `ApplicationContext` is a non-obvious pattern — needs the comment explaining why it exists
- The platform's isolated `BatchLoaderRegistry` silently absorbs batch loader registrations that are never used (harmless but wasteful)
- If Spring GraphQL changes how `AnnotatedControllerConfigurer` discovers `BatchLoaderRegistry` (e.g., via constructor injection instead of `ApplicationContext` lookup), this workaround may break

## How to Add New @BatchMapping Methods

No special steps required. Add `@BatchMapping` to any `@Controller` method as documented in the [Spring GraphQL reference](https://docs.spring.io/spring-graphql/reference/controllers.html#controllers.batch-mapping). The child context isolation in `PlatformGraphQlConfig` handles the duplicate registration transparently.

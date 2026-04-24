# Aura Report Microservice

Spring Boot 4.x / Java 25 / GraphQL (DGS) education management service using Clean Architecture.

## Commands

```bash
# Build (skip tests — requires running DB/Keycloak)
mvn clean install -DskipTests

# Run component tests
mvn test -pl component-tests

# Regenerate GraphQL Java types after schema changes
mvn generate-sources -f application/pom.xml

# Generate Liquibase changelog from entity diffs
mvn liquibase:diffChangeLog -Pgenerate-db-changelog -f database-runner/pom.xml
```

If you see MapStruct compilation errors on generated files, run `mvn clean` first — this clears corrupted generated
sources.

## Architecture

Five modules in dependency order:

```
core → details → application → component-tests
                 database-runner (depends on details)
```

- **`core/`** — Domain logic. No Spring. Entities (POJOs), repository interfaces, use case interface+impl pairs.
- **`details/`** — Infrastructure. JPA models, Spring Data repos, MapStruct mappers, services (`@Transactional`).
- **`application/`** — Spring Boot app. GraphQL controllers, mappers, Spring config.
- **`database-runner/`** — Liquibase migration runner.
- **`component-tests/`** — Cucumber BDD tests against a running app.

## Key Conventions

**Adding a new use case** (follow this exact pattern):

1. Create interface + impl in `core/usecases/<domain>/`
2. Register as `@Bean` in `application/configs/UseCaseConfig.java` (manual `new`, never `@Component` in core)
3. Add a service method in `details/services/XService.java` (wraps use case, adds `@Transactional`)
4. Register service as `@Bean` in `application/configs/ServiceConfig.java`
5. Add controller method in `application/controllers/graphql/v1/<domain>/XGqlController.java`

Reference: `CreateCourseUseCase` → `CourseService` → `CourseGqlController`.

**Repository pattern** (4 files per domain):

- `core/repositories/XRepository.java` — interface
- `details/springdata/repositories/impl/XRepositoryImpl.java` — implements interface, converts domain↔model via
  MapStruct
- `details/springdata/repositories/jpa/JpaXRepository.java` — extends `JpaRepository`
- `details/springdata/models/<domain>/XModel.java` — `@Entity` extending `TenantBaseModel`

**Naming:**

- JPA entities: `XModel` (e.g., `CourseModel`)
- Domain entities: `X` (e.g., `Course`)
- Use case impls: `XUseCaseImpl`

**Coding style:**

- Use `@RequiredArgsConstructor` for dependency injection instead of manual constructors when possible
- Use `@Slf4j` for logging instead of `LoggerFactory.getLogger(...)` 
- For `@Entity` model classes, use this annotation convention (enforced by `ModelEntityConventionTest` ArchUnit test):
  - `@Getter @Setter @NoArgsConstructor` — never use `@Data` (generates `equals`/`hashCode` that causes JPA issues with
    lazy-loaded proxies and detached entities)
  - `@EqualsAndHashCode` — identity is defined by the entity's ID or composite key:
    - `BaseModel` has `@EqualsAndHashCode(of = {"id"}, callSuper = false)` — uses only the `id` field, excludes
      `AuditMetadata` audit fields
    - Non-composite entities: `@EqualsAndHashCode(of = {}, callSuper = true)` — delegates to `BaseModel`'s `id`
    - Composite key entities: `@EqualsAndHashCode(of = {"compositeField"}, callSuper = true)` — uses `id` from
      `BaseModel` + the additional composite key field(s) from the subclass
    - `InstitutionModel` (extends `AuditMetadata`, has own `@Id`): `@EqualsAndHashCode(of = {"id"})`
    - Never include relationship fields in equals/hashCode — causes stack overflows from circular references
  - `@ToString.Exclude` on all lazy relationship fields (`@ManyToOne(fetch = LAZY)`, `@OneToMany`, etc.) — prevents
    `toString()` from triggering lazy loading and causing `LazyInitializationException`

**Rules:**

- No `@Component`, `@Service`, or Spring imports in `core/`
- `@Transactional` only in `details/services/` — never in use cases or controllers
- All models extend `TenantBaseModel` (provides `tenantId` for multi-tenancy)
- Date/time: stored as `Instant` in DB, exposed as epoch milliseconds (`Long`) in GraphQL
- Controllers get the auth user via `@ContextValue("user") User user`

**Authorization:**

- Authorization is handled by Keycloak server-side policy enforcement — never check roles in controllers
- Annotate every controller method with `@ProtectedOperation(resource = "...", scope = "...")`
- The `KeycloakAuthorizationInterceptor` (Order 3) calls Keycloak's token endpoint to evaluate permissions
- Resources, policies, and permissions are configured in Keycloak (see `realm-import.json` for test config)
- UMA resources are registered dynamically when entities are created (e.g., `outlet:{id}` on outlet creation)
- Tenant isolation remains via `x-tenant-id` header + `TenantSpecification` as defense-in-depth

**Testing requirements:**

- Every concrete implementation class (services, repository impls, Keycloak clients) must have unit tests using JUnit
  5 +
  Mockito + AssertJ. Place them in the same module's `src/test/java/` mirroring the source package.
- After completing a logical flow end-to-end (e.g., a new use case wired through controller → service → repository),
  write a Cucumber component test that exercises the full flow including side effects.
- Run tests and ensure they pass before considering the work done.

## GraphQL Schema & Codegen

Schema files in `application/src/main/resources/graphql/`:

- `types/<domain>.graphqls` — input/output types and enums
- `operations/<domain>/mutation.graphqls` — `extend type Mutation { ... }`
- `operations/<domain>/query.graphqls` — `extend type Query { ... }`

**Generated types** land in `application/target/generated-sources/com/aura/app/codegen/types/`. Never edit them — they
are regenerated from schema. Always run `mvn generate-sources -f application/pom.xml` after schema changes.

**Mappers:**

- `details/springdata/mappers/` — domain entity ↔ JPA model
- `application/mappers/` — GraphQL codegen DTO ↔ domain entity
- `DateToEpochMapper` handles `LocalDateTime`/`Instant` ↔ epoch `Long`

## Component Tests

Tests use Cucumber + TestContainers (PostgreSQL + Keycloak spun up automatically).

**Writing a new test** (follow this 3-file pattern):

1. `component-tests/src/test/resources/features/create_x.feature` — Gherkin scenario tagged `@Api` (feature) +
   `@ComponentTest` (scenario)
2. `component-tests/src/test/resources/files/create-x.graphql` — GraphQL mutation/query document
3. `component-tests/src/test/java/.../stepdefs/XStepDefs.java` — step definitions

Reference: `TopicStepDefs.java` + `create_topic.feature` + `create-topic.graphql`.

**Important:**

- Always include `createdBy`/`updatedBy` in GraphQL selection sets — unselected fields return null
- aura-admin Keycloak test user ID: `2c43b314-3e3c-4897-a3d3-02a8cab3e5e7`
- `TestContext` is `@ScenarioScope` — reset per scenario
- `DatabaseCleanupHooks` cleans DB tables (public + audit) and Keycloak resources before each scenario
- `DataTableTypeConverterConfig` — add `@DataTableType` methods here for new input types
- GraphQL files loaded via `FileUtils.findGraphqlQueryFromFiles("filename.graphql")`
- Cucumber config is in `junit-platform.properties` (not `cucumber.properties`)
- `CucumberTestConfig` is the Cucumber entry point — do not add test logic there

## Gherkin & Component Test Conventions

**Gherkin style:**

- Scenario names should describe WHO does WHAT — e.g., "Aura admin successfully creates institution admin account" not
  "Successfully create an institution admin account"
- `Given` steps should be detailed — use DataTables for entity details instead of hardcoding in step defs
- `Then` assertions should be specific — `the request should fail with http status 500` not `the request should fail
  with an error`. Include error codes, HTTP status, or specific field checks
- Use parameterized steps for reusability — e.g., `the user {string} enabled status should be {string}` instead of
  separate `should be enabled` / `should be disabled` steps
- Use `{institution_id}` placeholders in Gherkin strings — step defs resolve them from `TestContext` at runtime
  (e.g., `"institution:{institution_id}:admin"`)
- Use DataTables for lists of expected values — e.g., policy checks with `| name | type |` instead of chained `And`
  lines
- For Keycloak user assertions, verify the full state: groups, enabled status, email verified status, required actions
- Write Gherkin steps as generic and parameterised as possible to promote step def reusability — prefer
  `the user {string} enabled status should be {string}` over `the user should be enabled`. A single parameterised step
  def should serve multiple scenarios and feature files

**Step definition patterns:**

- Unhappy path steps ("user tries to...") must catch both `FieldAccessException` (GraphQL errors) and
  `HttpServerErrorException` (interceptor rejections). Store HTTP status via `testContext.setLastHttpStatus()`
- Steps that need auth should call `keycloak.tokenManager().getAccessToken().getToken()` and set headers inline — don't
  rely on a separate `Given user has role` step if the feature flow doesn't include one
- Keycloak admin assertions use `KeycloakTestContainer.getSingleton()` with master realm admin client (`admin-cli`)
- No-op steps for unimplemented features should log a skip message and have a `// TODO:` comment

**Environment cleanup (`DatabaseCleanupHooks`):**

- `@Before` hook cleans both DB (truncate) and Keycloak (resources, policies, groups, users)
- Preserve static Keycloak entities: `aura-admin-*` policies, `aura-admin` group, `service-account-*` users
- Log summary of removed items per category (even if empty)

## Liquibase / Database

- `spring.liquibase.enabled: false` in base `application.yaml` — must override in profiles that need it
- `application-component-test.yml` enables it with `change-log: classpath:db/changelog/changelog-master.yaml` and
  `liquibase-schema: liquibase`
- DB schemas: `public` (app data), `audit` (Hibernate Envers history), `liquibase` (migration tracking)
- Seed data in `seed-templates.sql` populates template tables; actual data is created at runtime (e.g., levels created
  when institution onboards)

## Environment Variables

Required when running outside TestContainers:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `KEYCLOAK_CLIENT_SERVER_URL`, `KEYCLOAK_CLIENT_REALM`, `KEYCLOAK_CLIENT_ID`,
`KEYCLOAK_CLIENT_SECRET`

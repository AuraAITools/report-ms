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
- **`details/`** — Infrastructure. JPA tables, Spring Data repos, MapStruct mappers, services (`@Transactional`).
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
- `details/springdata/repositories/impl/XRepositoryImpl.java` — implements interface, converts domain↔table via
  MapStruct
- `details/springdata/repositories/jpa/JpaXRepository.java` — extends `JpaRepository`
- `details/springdata/tables/<domain>/XTable.java` — `@Entity` extending `TenantBaseTable`

**Naming:**

- JPA entities: `XTable` (e.g., `CourseTable`)
- Domain entities: `X` (e.g., `Course`)
- Use case impls: `XUseCaseImpl`

**Rules:**

- No `@Component`, `@Service`, or Spring imports in `core/`
- `@Transactional` only in `details/services/` — never in use cases or controllers
- All tables extend `TenantBaseTable` (provides `tenantId` for multi-tenancy)
- Date/time: stored as `Instant` in DB, exposed as epoch milliseconds (`Long`) in GraphQL
- Controllers get the auth user via `@ContextValue("user") User user`

## GraphQL Schema & Codegen

Schema files in `application/src/main/resources/graphql/`:

- `types/<domain>.graphqls` — input/output types and enums
- `operations/<domain>/mutation.graphqls` — `extend type Mutation { ... }`
- `operations/<domain>/query.graphqls` — `extend type Query { ... }`

**Generated types** land in `application/target/generated-sources/com/aura/app/codegen/types/`. Never edit them — they
are regenerated from schema. Always run `mvn generate-sources -f application/pom.xml` after schema changes.

**Mappers:**

- `details/springdata/mappers/` — domain entity ↔ JPA table
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
- `DatabaseCleanupHooks` truncates all tables (public + audit) before each scenario
- `DataTableTypeConverterConfig` — add `@DataTableType` methods here for new input types
- GraphQL files loaded via `FileUtils.findGraphqlQueryFromFiles("filename.graphql")`
- Cucumber config is in `junit-platform.properties` (not `cucumber.properties`)
- `CucumberTestConfig` is the Cucumber entry point — do not add test logic there

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

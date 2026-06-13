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
- **Cross-aggregate references in `core/entities/` use typed string IDs**, never object references. A domain
  entity holds `private String accountId`, not `private Account account`, when the referenced entity belongs to a
  different aggregate root. Within-aggregate references (e.g., an Account aggregate that owns its Parent) stay as
  object references. The JPA `*Model` layer in `details/` keeps bidirectional links where Hibernate needs them.
  MapStruct mappers translate via `@Named` stub-builders (see `ParentMapper.accountRef` as the reference pattern).
  Rationale: removes the cycle in `core/` that previously forced `@Mapping(target = "...", ignore = true)` on every
  back-reference in every mapper. See `docs/implementation-plans/005-domain-id-refs-mapper-simplification.md`.
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
- **Prefer Spring Data derived `deleteAllByX(...)` methods over `@Modifying @Query("DELETE FROM ...")` in JPA
  repositories** (especially link-table repos). The derived form loads matching entities through the persistence
  context and deletes them via the normal Hibernate lifecycle, which preserves Envers (`@Audited`) history rows for
  the removed entities and triggers any `@PreRemove` / cascade callbacks. The `@Modifying` bulk-DELETE form issues a
  single SQL statement that bypasses the persistence context entirely — faster, but skips Envers auditing and
  lifecycle. Since every link entity in this codebase is `@Audited`, the derived form is the correct default. Reserve
  `@Modifying @Query` for bulk UPDATEs (which Spring Data has no derived equivalent for) and for the rare case where
  audit history of bulk deletes genuinely doesn't matter and the row count justifies skipping the SELECT.

**Rules:**

- No `@Component`, `@Service`, or Spring imports in `core/`
- `@Transactional` only in `details/services/` — never in use cases or controllers
- All models extend `TenantBaseModel` (provides `tenantId` for multi-tenancy)
- Date/time: stored as `Instant` in DB, exposed as epoch milliseconds (`Long`) in GraphQL
- Controllers get the auth user via `@ContextValue("user") User user`

**Authorization (SpiceDB ReBAC — see ADR-004):**

- Authorization uses SpiceDB (Zanzibar-style ReBAC) for instance-level permission checks. Keycloak handles
  authentication
  only (login, JWT issuance, user management).
- SpiceDB schema: `application/src/main/resources/spicedb/schema.zed` — defines resource types, relations, and
  permissions
- Annotate every `@QueryMapping`/`@MutationMapping` controller method with
  `@PreAuthorize("hasPermission(targetId, 'resource', 'scope')")`:
    - System-scoped (creating top-level resources):
      `@PreAuthorize("hasPermission('aura', 'system', 'create_institution')")`
      — checks `create_institution` on `system:aura` (the singleton system instance)
    - Instance-level: `@PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")`
- `SpiceDbPermissionEvaluator` calls SpiceDB's `CheckPermission` gRPC API — no JWT claim parsing
- `PreAuthorizeConventionTest` enforces correct usage via ArchUnit + source-level tests (valid resources, scopes, param
  refs)
- **Every create/update/delete operation must write corresponding SpiceDB relationships.** Consult `schema.zed` to
  determine which relationships are needed. Common mistakes: forgetting to grant role relationships (e.g., creating an
  educator-course link without writing `course:id#educator@user:userId`), or forgetting parent relationships that enable
  permission inheritance.
- Relationships are written in `details/services/XService.java` via
  `RelationshipAuthorizationService.writeRelationship(resourceType, resourceId, relation, subjectType, subjectId)`.
- **Existing relationship patterns** (use as reference for new operations). Every resource definition uses a single
  `parent` relation, never `parent_<type>` — the parent's target type (e.g. `relation parent: institution`) makes the
  intent clear without name suffixes:
    - Onboard institution → `institution:id#parent@system:aura`
    - Create outlet → `outlet:id#parent@institution:tenantId`
    - Create course → `course:id#parent@outlet:outletId` + `course:id#modifier@user:userId` per educator +
      `course:id#viewer@user:userId` per student
- **Two categories of relationships:**
    1. **Structural (parent):** link resource to its parent for permission inheritance — written as `parent` on every
       resource, e.g. `outlet:id#parent@institution:tenantId`. Always required on entity creation.
    2. **Role (user):** grant a user a role on a resource (e.g., `admin`, `viewer`, `modifier`). Required when assigning
       users to resources or creating accounts with specific roles.
- Tenant isolation remains via `x-tenant-id` header + `TenantSpecification` as defense-in-depth
- SpiceDB config: `application.yaml` has `spicedb.target`, `spicedb.preshared-key`, `spicedb.plaintext`
- **Schema convention — arrow into permissions, not relations.** When using the `->` operator across a relation, the
  right-hand side should be a `permission` on the target definition, not a raw `relation`. Define the role union once as
  a permission on the target and arrow into that permission from every consumer. This keeps role inheritance composable
  and avoids restating the union at every consumer site. `zed validate` flags raw-relation arrows as
  `arrow-references-relation` warnings.
    - Bad: `permission read = user_group->viewer + user_group->admin`
    - Good: define `permission read = viewer + admin` on the target, then `permission read = user_group->read` on the
      consumer
- **Per-resource schema files (new pattern, applied in `system.zed` / `institution.zed` / `outlet.zed`).** Each resource
  lives in its own `.zed` file. To use cross-file imports, the file must start with `use import` followed by
  `import '<other>.zed'`. The legacy monolithic `schema.zed` still exists for entities not yet migrated; new entities
  follow this per-file pattern.

    - **`<resource>_user_group` paired definition holds all role state. The resource definition stays thin** (one
      `user_group` relation, optional `parent` relation, permissions that delegate via `user_group->X` and union with
      `parent->X` for inheritance).

    - **Standard `<resource>_user_group` shape:**
        - Relations: `group_admin`, `group_viewer` (group membership management); `admin`, `modifier`, `viewer` (
          resource
          roles)
        - Group-mgmt permissions: `group_view = group_viewer + group_admin`; `group_add_admin`, `group_add_viewer`,
          `group_remove_admin`, `group_remove_viewer` are all `= group_admin`
        - Resource permissions: `view = viewer + modifier + admin`; `update = modifier + admin`; `delete = admin`
        - Role-set helper: `is_admin_or_modifier = admin + modifier` — consumed via arrow from the resource and from
          descendants. Aliases new create-verbs to a single union point.

    - **Decoupling rule:** group-management and resource roles are independent. A `group_admin` cannot reach the
      resource;
      an `admin` cannot manage the group. Never union the two role sets.

    - **Resource definition template:**
      ```zed
      definition <resource> {
          relation parent: <parent_resource>          // omit for root resources (e.g. system)
          relation user_group: <resource>_user_group
  
          permission view   = user_group->view   + parent->view
          permission update = user_group->update + parent->update
          permission delete = user_group->delete + parent->delete
          permission is_admin_or_modifier = user_group->is_admin_or_modifier + parent->is_admin_or_modifier
          permission create_<child> = is_admin_or_modifier   // alias create-verbs to the helper
      }
      ```

    - **Recursive inheritance via `parent` on the resource (not on the user_group).** SpiceDB rejects nested arrows like
      `parent->user_group->view` ("Nested arrows not yet supported") — each `->` is a single hop. But each parent's
      permission resolves recursively, so roles transitively propagate across multi-level chains
      (e.g. `outlet → institution → system`) without needing a chained expression. Inherited users gain permissions on
      the
      *resource* but NOT on the resource's `user_group`.

    - **`create_<child>` permission lives on the PARENT resource, not the child.** A child instance doesn't exist at
      creation-time so its permissions can't be checked. `create_institution` lives on `system`; `create_outlet` lives
      on
      `institution`. Aliasing to `is_admin_or_modifier` means new create-verbs don't pollute ancestor schemas with
      per-verb scaffolding.

    - **Test fixture conventions** (`spicedb/tests/<resource>.test.yaml`): one user per role with descriptive names
      (`inst_admin_user`, `sys_modifier_user`, `out_viewer_user`) plus `non_member_user`. Resource singletons named for
      purpose (`system:aura`); instances by numeric id (`institution:1`, `outlet:1`, `outlet_user_group:1`). Cover four
      things explicitly: direct roles, inherited roles via `parent`, transitively-inherited roles across multi-level
      chains, and isolation (inherited users have no permissions on the descendant's `user_group`).
- **Schema test coverage convention — every relationship/permission change requires a paired test update.** Whenever
  application code adds, removes, or changes a SpiceDB relationship write (in `details/services/XService.java` via
  `RelationshipAuthorizationService.writeRelationship(...)`), or whenever the schema in
  `application/src/main/resources/spicedb/*.zed` gains, removes, or modifies a `relation` or `permission`, the matching
  `application/src/main/resources/spicedb/tests/<schema-file>.test.yaml` file must be updated in the same change.
    - For each new/changed permission: add a positive assertion (a subject who *should* have it) and a negative
      assertion
      (a subject who *should not*) under `assertTrue` / `assertFalse`.
    - For each new relationship written by application code: add it (or a representative example) to the
      `relationships:`
      block of the corresponding test YAML, plus assertions verifying the permissions it confers.
    - For each removed permission/relationship: remove the corresponding assertions.
    - Run `zed validate application/src/main/resources/spicedb/tests/*.test.yaml` locally before committing — it also
      runs
      in CI and blocks the PR on regressions. Fixtures are minimal: one user per role plus one non-member for negative
      assertions (see `system.test.yaml` as the reference pattern).
- **Flattened-leaf resources.** Some entities (`level`, `subject`, `topic`, `outlet_room`) live only in the database — they have no per-instance SpiceDB resource definition. Their controllers check named aliases on the natural parent instead: `@PreAuthorize("hasPermission(#institutionId, 'institution', 'view_level')")` for level/subject/topic (parent is `institution`), and `@PreAuthorize("hasPermission(#updateOutletRoomInput.outletId, 'outlet', 'update_outlet_room')")` for outlet rooms (parent is `outlet`). The alias permissions are defined on the parent definition as `permission view_<x>: user = view`, `permission update_<x>: user = update`, `permission delete_<x>: user = delete`. Services for these entities therefore write no SpiceDB relationships on create and call no `deleteAllRelationshipsForResource` on delete. The corresponding `_user_group` definitions do not exist. Adopt this pattern for any future leaf resource that does not need per-instance role grants — the savings are one schema file, one paired user_group definition, one test-yaml file, two relationship writes per create, two per delete, and a slot in `PermissionRegistry.RESOURCE_PERMISSIONS`.

**Testing requirements:**

- Every concrete implementation class (services, repository impls, Keycloak clients) must have unit tests using JUnit
  5 +
  Mockito + AssertJ. Place them in the same module's `src/test/java/` mirroring the source package.
- Every test class must have `@DisplayName("Human readable description")` on the class
- Every `@Test` and `@TestFactory` method must have `@DisplayName("Should do something specific")` — use sentence case,
  start with "Should" for unit tests
- `@ArchTest` static fields do NOT need `@DisplayName` — they use `.as()` descriptions instead
- After completing a logical flow end-to-end (e.g., a new use case wired through controller → service → repository),
  write a Cucumber component test that exercises the full flow including side effects.
- Run tests and ensure they pass before considering the work done.

## GraphQL Schema & Codegen

Schema files in `application/src/main/resources/graphql/`:

- `types/<domain>.graphqls` — input/output types and enums
- `operations/<domain>/mutation.graphqls` — `extend type Mutation { ... }`
- `operations/<domain>/query.graphqls` — `extend type Query { ... }`

- Array fields in input types must be non-nullable at both levels: `[ID!]!` not `[ID!]`. This ensures the client always
  sends an explicit list (empty `[]` is fine) instead of omitting it, which avoids null-check bugs in service code.
- **Every tenant-scoped query and mutation must declare `institutionId: ID!`** — either as a sibling argument
  (`deleteOutletById(institutionId: ID!, id: ID!)`) or as a field on its input type
  (`input CreateXInput { institutionId: ID! ... }`). This applies to creates, updates, deletes, AND reads — including
  single-entity lookups like `getXById`. The only exemptions are system-scoped operations not bound to a tenant (e.g.
  `createInstitution`, `onboardInstitution`, `getInstitution(id:)` where the id IS the institutionId). The argument
  does not need to be consumed by the controller or service — its purpose is to ensure `user.getCurrentTenantAccessed`
  can be derived from the request on every tenant call. Treat a missing `institutionId` on any tenant operation as a
  bug to fix at schema time.

**Generated types** land in `application/target/generated-sources/com/aura/app/codegen/types/`. Never edit them — they
are regenerated from schema. Always run `mvn generate-sources -f application/pom.xml` after schema changes.

**Mappers:**

- `details/springdata/mappers/` — domain entity ↔ JPA model
- `application/mappers/` — GraphQL codegen DTO ↔ domain entity
- `DateToEpochMapper` handles `LocalDateTime`/`Instant` ↔ epoch `Long`

## Component Tests

Tests use Cucumber + TestContainers (PostgreSQL + Keycloak + SpiceDB spun up automatically).

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
- `DatabaseCleanupHooks` cleans DB tables (public + audit), SpiceDB relationships, and Keycloak test users before each
  scenario
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

- `@Before` hook cleans DB (truncate), SpiceDB relationships (delete all + re-create
  `system:aura#user_group@system_user_group:aura` and `system_user_group:aura#admin@user:<serviceAccountUserId>`), and
  Keycloak test users (preserve `service-account-*`)
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
`KEYCLOAK_CLIENT_SECRET`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `AWS_S3_BUCKET_NAME`

AWS credentials are picked up from the standard env-var pair (`AWS_ACCESS_KEY_ID` + `AWS_SECRET_ACCESS_KEY`) via the
AWS SDK's `DefaultCredentialsProvider` chain. `AWS_REGION` and `AWS_S3_BUCKET_NAME` are read via Spring config and
exposed to the app through `AwsS3Properties`.

# report-ms

Microservice for Aura

This project follows `clean architecture`

- **core**: core module should contain mostly abstractions. classes should be pure and have no reliance on external
  technologies (i.e. spring) as much as possible.
    - domain entities
    - use cases: use cases should contain business logic
- **details**: contains implementation details and adapters.
    - technolgy-specific implementation of **core** abstractions/interfaces using certain technologies (i.e. springdata,
      keycloak)
    - services that orchestrate use cases
    - when using ORM, table entities & mappers between table & domain entities
- **application**: contains runtime configurations, web gateways
    - configurations of beans
    - web APIs: GraphQL, REST
    - DTOs for the web APIs
    - DTO mappers for web APIs
    - ...everything spring-framework! or whichever framework we decide to use next time! we are future proof!
- **database-runner** spring application that performs db migration with liquibase

Note:

- core should NOT depend on spring, details, applications
- details can use core, but not application
- application can use core & details

## Reserved values

- **`00000000-0000-0000-0000-000000000000`** — reserved account id for the **system account**. Returned as
  `AccountMinimalDetail.accountId` on audit fields (`createdBy` / `updatedBy`) when the request has no tenant
  context (e.g. system-scoped queries like `getUserInstitutions`) and a real `Account` lookup is impossible. Paired
  with `name = "system"` and `url = ""`. No row with this id exists in the database; clients should treat it as a
  sentinel, not query for it.

## Local environment

The app depends on Postgres, SpiceDB, and S3. For local development, start everything via Docker Compose.

### 1. Start Postgres + LocalStack (app DB + S3)

```bash
docker compose -f docker-compose-dev.yaml up -d
```

This brings up:

- **Postgres** on `localhost:5434` (user `postgres` / pass `password` / db `aura`)
- **LocalStack S3** on `localhost:4566`. The `docker/localstack/init-s3.sh` init hook auto-creates the `aura-dev` bucket
  on first boot. LocalStack is also configured with `EXTRA_CORS_ALLOWED_ORIGINS=http://localhost:3000` so browser PUTs
  to presigned URLs from a local frontend work.

Verify the bucket exists:

```bash
docker exec localstack-dev awslocal s3 ls
```

### 2. Start SpiceDB (authorization)

```bash
docker compose -f spice-docker-compose.yaml up -d
```

This starts a SpiceDB Postgres on `localhost:5435`, runs migrations, then serves SpiceDB on `localhost:50051` (gRPC) and
`localhost:8443` (HTTP). The preshared key is `dev-secret-key`.

Bootstrap the schema and superadmin (see [SpiceDB section below](#first-time-starting)).

### 3. Required env vars

Request `.env.sample` values (DB / Keycloak) from another developer. In addition, for S3 against LocalStack set:

```bash
AWS_ACCESS_KEY_ID=test
AWS_SECRET_ACCESS_KEY=test
AWS_REGION=ap-southeast-1
AWS_S3_BUCKET_NAME=aura-dev
```

The `local` Spring profile automatically points the S3 client at `http://localhost:4566` via
`aws.s3.endpoint-override` — no app-side change needed to switch between LocalStack and real S3.

## Running the application

1. First create `liquibase` schema in your DB
2. Request credentials found in the `.env.sample` from another developer
3. run the application in `local` profile while developing

```bash
mvn clean compile # compile all modules & generate classes

# install modules into .m2 local repository.
# sometimes you have to run install twice because the generated sources is wonky
mvn install 

mvn spring-boot:run -pl application 
```

## Building & testing across modules

### The `-pl` reactor gotcha

When you edit a file in an upstream module (`core`, `details`, `application`) and then run

```bash
mvn test -pl component-tests
```

the edit can appear to have no effect. The test passes or fails exactly as it did
before your change. This is not a stale-cache or incremental-compile bug — it is how
Maven's reactor resolves inter-module dependencies, and it is worth understanding
because it bites whenever you touch app code while debugging a component test.

#### What `-pl X` actually does

The `-pl` (project list) flag scopes the reactor to **only** module `X`. Maven does
not look at the other modules' source trees during that invocation. To satisfy
`component-tests`'s declared dependency on `application` (which transitively pulls
`details` and `core`), Maven does what it would for any third-party library: it
resolves `com.aura:application:1.0.0` from the **local Maven repository** at
`~/.m2/repository/com/aura/application/1.0.0/application-1.0.0.jar`. That jar was
written there by the last `mvn install` run.

#### Why the change disappears

1. `mvn clean test -pl component-tests` runs `clean` only on `component-tests/target/`.
   It does **not** touch `~/.m2`.
2. Compilation of `component-tests` links against the **old** `details-1.0.0.jar`
   still sitting in `~/.m2`.
3. Spring Boot's test context loads classes from that old jar at runtime. The
   `OutletRoomModel.java` you just edited is never on the classpath. The fix
   appears inert; the failing test keeps failing with the same stack trace.

Unit tests inside `details/` don't have this problem because `mvn test -pl details`
runs the tests against `details/target/classes/` directly — same reactor module,
same compile output, no `~/.m2` round-trip. The pitfall is specific to a *consumer*
module reading its upstream dependency through Maven artifact resolution.

#### The fix

Either rebuild the whole reactor so `~/.m2` is up to date:

```bash
mvn clean install -DskipTests   # rebuilds & installs every module's jar into ~/.m2
mvn test -pl component-tests
```

or use `-am` ("also make") so Maven additionally builds every module that
`component-tests` transitively depends on, in the same reactor invocation:

```bash
mvn test -pl component-tests -am
```

`-am` is faster if you don't need `database-runner` rebuilt; the full-reactor form
is safer when several modules have changed at once. Both are correct.

#### Rule of thumb

| What changed | Safe command |
| --- | --- |
| Only files in `component-tests/` | `mvn clean test -pl component-tests` |
| Anything in `core/`, `details/`, or `application/` | `mvn clean install -DskipTests` then `mvn test -pl component-tests` (or use `-am`) |
| MapStruct-generated sources look corrupt | `mvn clean` first; the codegen output gets confused otherwise |

## Dependencies

Java jdk 25

### Dev Dependencies

#### SdkMan

To standardise JDK versions. use `sdk env` to switch to correct sdk in `.sdkmanrc`
if JDK is not installed, use `sdk env install`

# First time starting

## adding superadmin to spicedb

```shell
# create compiled zed file from composable .zed files
zed schema compile index.zed > artifact.zed
# write schema into spicedb
zed schema write artifact.zed
# run applicaiont
# this will write the system:aura parent system_user_group:aura relationship
# create system_user_group:aura to system:aura relationship
# give user admin & group_admin relation of aura system_user_group
zed relationship create system_user_group:aura admin user:<user_id>
zed relationship create system_user_group:aura group_admin user:<user_id>
```

When running the application, it will idempotently(TOUCH) write the relation

```
zed relationship create system:aura user_group system_user_group:aura
```

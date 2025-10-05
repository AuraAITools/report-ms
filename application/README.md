# Database

## Adding changes to the Database

### How to generate liquibase changelog diffs

If adding a table is needed, always start from creating Hibernate entites. Then generate liquibase changelogs using the
`Liquibase Hibernate extension`

### generating Baseline DDL from hibernate entities

**Step 1**: use hibernate to create tables for you. run the application

```yaml
# application-local.yaml
spring:
  jpa:
    hibernate.ddl-auto: create
    show-sql: true
```

**step 2**: generate SQL DDL from the tables created in database. this uses the `generate-db-changelog` mvn profile,
which uses `liquibase-properties`

```bash
# generate for public schema
mvn liquibase:generateChangeLog -Pgenerate-db-changelog

# generate for audit schema
mvn liquibase:generateChangeLog -Pgenerate-db-changelog -Dliquibase.schemas=audit
```

### Data migrations for a new release

**step 1**: create the new release folder in `details` module in `resource/db/changelog/releases`. i.e. `v1.1.12`.

**step 2**: create 2 folders for each schema (`public` & `audit`). create the changelog files under the folders

**step 3**: create a `release-changelog.yaml` within the release folder (i.e. `v1.1.12`). include the changelogs to
include in this release
.

i.e. `release-changelog.yaml`

```yaml
databaseChangeLog:
  - include:
      file: public/changelog-baseline.sql
      relativeToChangeLogFile: true
  - include:
      file: public/create-school-template.sql
      relativeToChangeLogFile: true
  - include:
      file: audit/changelog-baseline.sql
      relativeToChangeLogFile: true
```

**step 4**: add this release changelog into the `db.master-changelog`

i.e. `db.master-changelog`

```yaml
databaseChangeLog:
  - include:
      file: releases/v1_0_0/release-changelog.yaml
      relativeToChangeLogFile: true
```

### rolling back changesets

```bash
 mvn liquibase:rollback -Prollback -Dliquibase.rollbackCount=1
```

### Generating of SQL diffs for `liquibase upgrade` (TBC)

``` bash
# doesnt work atm
mvn liquibase:diff -Pgenerate-db-changelog
```


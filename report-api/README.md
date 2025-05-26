# liquibase creation

``` bash
mvn liquibase:generateChangeLog -Pgenerate-db-changelog
```

Generating changelogs for audit schema

``` bash
mvn liquibase:generateChangeLog -Pgenerate-db-changelog -Dliquibase.schemas=audit
```

# liquibase create diff

``` bash
mvn liquibase:diff -Pgenerate-db-changelog
```
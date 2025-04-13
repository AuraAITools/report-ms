# liquibase creation

``` bash
mvn liquibase:generateChangeLog -Pgenerate-db-changelog
```

# liquibase create diff

``` bash
mvn liquibase:diff -Pgenerate-db-changelog
```
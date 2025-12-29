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

## Dependencies

Java jdk 24

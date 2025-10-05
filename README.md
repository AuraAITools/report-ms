# report-ms

microservice for Aura

This project follows clean architecture

- **core**: domain entities & use cases
- **details**: contains implementation details. this includes implementation of core abstractions/interfaces, and
  adapters for using certain technologies (i.e. springdata, keycloak). DB migrations are found here too
- **application**: contains runtime configurations, web gateways
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

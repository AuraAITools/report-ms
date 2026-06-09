# Build stage
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /modules
COPY . /modules
RUN mvn clean install -DskipTests

# AOT cache stage — runs the app under the `training` profile with
# spring.context.exit=onRefresh so Spring refreshes every bean (loading and
# linking all classes) and exits before serving traffic. JEP 514 records and
# writes the AOT cache in a single launch via -XX:AOTCacheOutput.
FROM eclipse-temurin:25-jre AS aot
WORKDIR /app
COPY --from=build /modules/application/target/report-ms-exec.jar /app/exec.jar
RUN java -XX:AOTCacheOutput=/app/app.aot \
         -Dspring.context.exit=onRefresh \
         -Dspring.profiles.active=training \
         -jar /app/exec.jar

# Production runtime — uses the prebuilt AOT cache.
FROM eclipse-temurin:25-jre AS production
WORKDIR /app
COPY --from=build /modules/application/target/report-ms-exec.jar /app/exec.jar
COPY --from=aot   /app/app.aot                                   /app/app.aot
EXPOSE 8080
ENTRYPOINT ["java", "-XX:AOTCache=/app/app.aot", "-jar", "exec.jar"]

# Database runner runtime
FROM eclipse-temurin:25-jre AS database-runner
WORKDIR /app
COPY --from=build /modules/database-runner/target/database-runner-*-exec.jar /app/exec.jar
ENTRYPOINT ["java", "-jar", "exec.jar"]

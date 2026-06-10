# Build stage
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /modules
COPY . /modules
RUN mvn clean install -DskipTests

# AOT cache stage — DISABLED.
#
# An AOT cache baked here at image-build time on GitHub's arm64 runner
# (Graviton/Ampere) SIGILLed inside AdapterBlob code when loaded on Apple
# Silicon at runtime, because JEP 514 caches are tied to the specific CPU
# feature set and kernel page size present at training time. To re-enable,
# the right pattern is to train on first pod boot via -XX:AOTMode=auto with
# the cache stored on a PVC, so training and runtime CPU are identical.
#
# FROM eclipse-temurin:25-jre AS aot
# WORKDIR /app
# COPY --from=build /modules/application/target/report-ms-exec.jar /app/exec.jar
# RUN java -XX:AOTCacheOutput=/app/app.aot \
#          -Dspring.context.exit=onRefresh \
#          -Dspring.profiles.active=training \
#          -jar /app/exec.jar

# Production runtime.
FROM eclipse-temurin:25-jre AS production
WORKDIR /app
COPY --from=build /modules/application/target/report-ms-exec.jar /app/exec.jar
# COPY --from=aot   /app/app.aot                                   /app/app.aot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "exec.jar"]
# Re-enable when AOT comes back:
# ENTRYPOINT ["java", "-XX:AOTCache=/app/app.aot", "-jar", "exec.jar"]

# Database runner runtime
FROM eclipse-temurin:25-jre AS database-runner
WORKDIR /app
COPY --from=build /modules/database-runner/target/database-runner-*-exec.jar /app/exec.jar
ENTRYPOINT ["java", "-jar", "exec.jar"]

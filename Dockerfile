# Build stage
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /modules
# Copy source code
COPY . /modules

# Build the application
RUN mvn clean install -DskipTests

# Application runtime
FROM eclipse-temurin:25-jre AS production
WORKDIR /app
# Copy only the JAR file from the build stage
COPY --from=build /modules/application/target/report-ms-exec.jar /app/exec.jar
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "exec.jar"]

# Database runner runtime
FROM eclipse-temurin:25-jre AS database-runner
WORKDIR /app
COPY --from=build /modules/database-runner/target/database-runner-*-exec.jar /app/exec.jar
ENTRYPOINT ["java", "-jar", "exec.jar"]

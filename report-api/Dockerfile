# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .
# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline
# Copy source code
COPY src/ /app/src/
# Build the application
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy only the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar
# Run the application
CMD ["java", "-jar", "app.jar"]

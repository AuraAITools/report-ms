package com.aura.tests.component.initializers;

import com.aura.tests.component.containers.postgres.PostgresTestContainer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

// TODO: make this a test container if possible
@Slf4j
public class MigrationRunner implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        log.info("running db migration");
        PostgreSQLContainer<?> postgres = PostgresTestContainer.getSingleton();
        createLiquibaseSchema(postgres);
        try {
            int exitCode = runProcessAndGetExitCode(postgres);

            if (exitCode != 0) {
                throw new RuntimeException("Database migration failed with exit code: " + exitCode);
            }

            log.info("DB migration completed successfully");

        } catch (Exception e) {
            throw new RuntimeException("Failed to run database migration", e);
        }

    }

    private int runProcessAndGetExitCode(PostgreSQLContainer<?> postgres) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar",
                getJarPath(),
                "--spring.profiles.active=liquibase",
                "--spring.datasource.url=" + postgres.getJdbcUrl(),
                "--spring.datasource.username=" + postgres.getUsername(),
                "--spring.datasource.password=" + postgres.getPassword()
        );

        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        return exitCode;
    }

    private String getJarPath() {
        // Option 1: Relative path (works when running from component-tests directory)
        String relativePath = "../database-runner/target/database-runner-1.0.0-exec.jar";

        // Option 2: Resolve absolute path for reliability
        Path path = Paths.get(relativePath).toAbsolutePath().normalize();

        if (!Files.exists(path)) {
            throw new RuntimeException("Database runner JAR not found at: " + path +
                    ". Run 'mvn package -DskipTests' in database-runner module first.");
        }

        return path.toString();
    }

    private void createLiquibaseSchema(PostgreSQLContainer<?> postgres) {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS liquibase");
            log.info("Created liquibase schema");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create liquibase schema", e);
        }
    }
}

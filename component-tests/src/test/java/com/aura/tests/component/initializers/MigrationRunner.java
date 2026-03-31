package com.aura.tests.component.initializers;

import com.aura.tests.component.containers.postgres.PostgresTestContainer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class MigrationRunner implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        PostgreSQLContainer<?> postgres = PostgresTestContainer.getSingleton();
        createLiquibaseSchema(postgres);
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

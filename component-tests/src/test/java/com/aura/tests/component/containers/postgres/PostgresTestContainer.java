package com.aura.tests.component.containers.postgres;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgresTestContainer {
    private static PostgreSQLContainer<?> singleton;
    private static PostgresCredentials credentials;

    private PostgresTestContainer() {
    }

    public static synchronized PostgreSQLContainer<?> getSingleton() {
        if (singleton == null) {
            try {
                singleton = new PostgreSQLContainer<>("postgres:18.1")
                        .withDatabaseName("aura")
                        .withUsername("postgres")
                        .withPassword("postgres");
                log.info("starting postgres container");
                singleton.start();
                log.info("postgres container successfully started at url={}", singleton.getJdbcUrl());
            } catch (Exception e) {
                log.error("failed to start postgres container", e);
                throw new RuntimeException(e);
            }
        }
        return singleton;
    }


}

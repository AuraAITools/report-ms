package com.aura.database.databaserunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DatabaseRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseRunnerApplication.class, args);
        log.info("✅ Database migration completed successfully");

    }
}

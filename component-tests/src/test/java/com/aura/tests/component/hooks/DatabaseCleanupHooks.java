package com.aura.tests.component.hooks;

import io.cucumber.java.Before;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class DatabaseCleanupHooks {

    private final JdbcTemplate jdbcTemplate;

    @Before
    public void cleanDatabase() {
        log.info("Cleaning database before scenario");
        long start = System.currentTimeMillis();

        List<String> tables = jdbcTemplate.queryForList("""
                SELECT quote_ident(table_schema) || '.' || quote_ident(table_name)
                FROM information_schema.tables
                WHERE table_schema IN ('public', 'audit')
                  AND table_type = 'BASE TABLE'
                """, String.class);

        if (!tables.isEmpty()) {
            String truncateSql = "TRUNCATE " + String.join(", ", tables) + " CASCADE";
            jdbcTemplate.execute(truncateSql);
        }

        log.info("Database cleanup complete in {}ms", System.currentTimeMillis() - start);
    }
}

package com.aura.tests.component.clients;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcClient {

    private final JdbcTemplate jdbcTemplate;

    public JdbcClient(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createInstitution() {
        String sql = """
                """;
        jdbcTemplate.batchUpdate(sql);
    }
}

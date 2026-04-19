package com.aura.tests.component.configs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpSyncGraphQlClient;

@TestConfiguration
public class ClientTestConfig {

    @Bean
    @ScenarioScope
    HttpSyncGraphQlClient httpSyncGraphQlClient(@LocalServerPort int port) {
        String url = String.format("http://localhost:%s/graphql", port);
        return HttpSyncGraphQlClient
                .builder()
                .url(url)
                .build();
    }

    @Bean
    @ScenarioScope
    TestContext testContext(@LocalServerPort int port) {
        var tenantClient = HttpSyncGraphQlClient.builder()
                .url(String.format("http://localhost:%s/graphql", port))
                .build();
        var platformClient = HttpSyncGraphQlClient.builder()
                .url(String.format("http://localhost:%s/platform/graphql", port))
                .build();
        return new TestContext(tenantClient, platformClient);
    }
}

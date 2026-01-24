package com.aura.tests.component.contexts;

import lombok.Data;
import org.springframework.graphql.client.HttpSyncGraphQlClient;

@Data
public class TestContext {

    private HttpSyncGraphQlClient graphQLClient;

    public TestContext(HttpSyncGraphQlClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }
}

package com.aura.tests.component.configs;

import com.aura.core.repositories.email.EmailGateway;
import com.aura.tests.component.containers.localstack.LocalStackTestContainer;
import com.aura.tests.component.contexts.TestContext;
import com.aura.tests.component.email.FakeEmailGateway;
import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@TestConfiguration
public class ClientTestConfig {

    @Bean(destroyMethod = "close")
    S3Client testS3Client() {
        return S3Client.builder()
                .endpointOverride(LocalStackTestContainer.getSingleton().getEndpoint())
                .region(Region.of(LocalStackTestContainer.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(LocalStackTestContainer.ACCESS_KEY, LocalStackTestContainer.SECRET_KEY)))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

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
        var client = HttpSyncGraphQlClient.builder()
                .url(String.format("http://localhost:%s/graphql", port))
                .build();
        return new TestContext(client);
    }

    @Bean
    FakeEmailGateway fakeEmailGateway() {
        return new FakeEmailGateway();
    }

    @Bean
    @Primary
    EmailGateway emailGatewayOverride(FakeEmailGateway fakeEmailGateway) {
        return fakeEmailGateway;
    }
}

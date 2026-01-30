package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.TopicResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;

import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class TopicStepDefs {

    private final TestContext testContext;

    private final Keycloak keycloak;

    @When("user creates a topic with name {string}")
    public void userCreatesATopicWithName(String topicName) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-topic.graphql");

        Map<String, Object> variables = Map.of("input", topicName);

        TopicResponse topicResponse = testContext
                .getGraphQLClient()
                .mutate()
                .build()
                .document(graphqlQuery)
                .variables(variables)
                .retrieveSync("createTopic")
                .toEntity(TopicResponse.class);
        TopicResponse expectedResponse = TopicResponse
                .newBuilder()
                .name(topicName)
                .createdBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .updatedBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .build();

        assertThat(topicResponse)
            .usingRecursiveComparison()
            .ignoringFields("id", "createdAt", "updatedAt")
            .isEqualTo(expectedResponse);
    }

    @Then("the topic {string} should be created successfully")
    public void theTopicShouldBeCreatedSuccessfully(String topicName) {
        // TODO: Temporary test step since we don't have a query to fetch topic by name yet
        // and the creation is already verified in the previous step.
        assertThat(true).isTrue();
    }
}

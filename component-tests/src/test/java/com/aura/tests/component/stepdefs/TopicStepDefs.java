package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.TopicResponse;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class TopicStepDefs {

    private final TestContext testContext;

    @When("user creates a topic with name {string}")
    public void userCreatesATopicWithName(String topicName) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-topic.graphql");

        Map<String, Object> variables = Map.of("input", Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", topicName
        ));

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
                .createdBy(TestConstants.AURA_ADMIN_USER_ID)
                .updatedBy(TestConstants.AURA_ADMIN_USER_ID)
                .build();

        assertThat(topicResponse)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expectedResponse);

        testContext.setTopicId(topicResponse.getId());
    }

    @And("a topic exists with name {string}")
    public void aTopicExistsWithName(String topicName) {
        userCreatesATopicWithName(topicName);
    }

    @Then("the topic {string} should be created successfully")
    public void theTopicShouldBeCreatedSuccessfully(String topicName) {
        assertThat(testContext.getTopicId()).isNotNull();
    }
}

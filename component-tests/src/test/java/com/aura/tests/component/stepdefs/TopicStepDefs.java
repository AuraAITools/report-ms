package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.TopicResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class TopicStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastGetTopicResponse;
    private List<Map<String, Object>> lastGetTopicsResponse;

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

        assertThat(topicResponse.getName()).isEqualTo(topicName);
        assertThat(topicResponse.getId()).isNotNull();

        testContext.setTopicId(topicResponse.getId());
        testContext.getTopicIds().add(topicResponse.getId());
    }

    @And("a topic exists with name {string}")
    public void aTopicExistsWithName(String topicName) {
        userCreatesATopicWithName(topicName);
    }

    @When("user tries to create a topic with name {string}")
    public void userTriesToCreateATopicWithName(String topicName) {
        String query = findGraphqlQueryFromFiles("create-topic.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", Map.of("institutionId", testContext.getInstitutionId(), "name", topicName)))
                    .retrieveSync("createTopic")
                    .toEntity(TopicResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the topic {string} should be created successfully")
    public void theTopicShouldBeCreatedSuccessfully(String topicName) {
        assertThat(testContext.getTopicId()).isNotNull();
    }

    @When("user queries topic by id")
    public void userQueriesTopicById() {
        String query = findGraphqlQueryFromFiles("get-topic-by-id.graphql");
        try {
            lastGetTopicResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("topicId", testContext.getTopicId()))
                    .retrieveSync("getTopicById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries topic with non-existent id")
    public void userQueriesTopicWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-topic-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("topicId", UUID.randomUUID().toString()))
                    .retrieveSync("getTopicById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the topic should be returned with name {string}")
    public void theTopicShouldBeReturnedWithName(String name) {
        assertThat(lastGetTopicResponse).isNotNull();
        assertThat(lastGetTopicResponse.get("name")).isEqualTo(name);
    }

    @When("user queries all topics in the institution")
    @SuppressWarnings("unchecked")
    public void userQueriesAllTopicsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-topics-in-institution.graphql");
        lastGetTopicsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getAllTopicsInInstitution")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the response should contain {int} topics")
    public void theResponseShouldContainTopics(int expectedCount) {
        assertThat(lastGetTopicsResponse).hasSize(expectedCount);
    }

    @And("the topics should include names {string}")
    public void theTopicsShouldIncludeNames(String commaSeparatedNames) {
        List<String> expected = List.of(commaSeparatedNames.split(","));
        assertThat(lastGetTopicsResponse).extracting(m -> m.get("name"))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    private void handleErr(FieldAccessException e) {
        testContext.setRequestFailed(true);
        var errors = e.getResponse().getErrors();
        if (!errors.isEmpty()) {
            var ext = errors.getFirst().getExtensions();
            if (ext != null && ext.containsKey("error_code")) {
                testContext.setLastErrorMessage(ext.get("error_code").toString());
            }
        }
    }
}

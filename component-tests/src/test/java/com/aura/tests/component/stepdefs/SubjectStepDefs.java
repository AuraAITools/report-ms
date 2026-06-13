package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
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
public class SubjectStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastGetSubjectResponse;
    private List<Map<String, Object>> lastGetSubjectsResponse;

    @When("user creates a subject with name {string}")
    public void userCreatesASubjectWithName(String subjectName) {
        SubjectResponse response = executeCreateSubject(subjectName);
        testContext.setLastSubjectResponse(response);
    }

    @And("a subject exists with name {string}")
    public void aSubjectExistsWithName(String subjectName) {
        executeCreateSubject(subjectName);
    }

    @When("user tries to create a subject with name {string}")
    public void userTriesToCreateASubjectWithName(String subjectName) {
        String query = findGraphqlQueryFromFiles("create-subject.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", Map.of("institutionId", testContext.getInstitutionId(), "name", subjectName)))
                    .retrieveSync("createSubject")
                    .toEntity(SubjectResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var ext = errors.getFirst().getExtensions();
                if (ext != null && ext.containsKey("error_code")) {
                    testContext.setLastErrorMessage(ext.get("error_code").toString());
                }
            }
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the subject {string} should be created successfully")
    public void theSubjectShouldBeCreatedSuccessfully(String subjectName) {
        SubjectResponse response = testContext.getLastSubjectResponse();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(subjectName);
        assertThat(response.getCreatedBy()).isNotNull();
        assertThat(response.getUpdatedBy()).isNotNull();
    }

    @When("user links the topic to the subject")
    public void userLinksTopicToSubject() {
        String query = findGraphqlQueryFromFiles("create-subject-topic-links.graphql");

        SubjectResponseWithTopics response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "id", testContext.getSubjectId(),
                        "topicIds", List.of(testContext.getTopicId())
                )))
                .retrieveSync("createSubjectTopicLinks")
                .toEntity(SubjectResponseWithTopics.class);

        testContext.setLastSubjectTopicResponse(response);
    }

    @Then("the subject should have the topic linked")
    public void theSubjectShouldHaveTopicLinked() {
        SubjectResponseWithTopics response = testContext.getLastSubjectTopicResponse();
        assertThat(response).isNotNull();
        assertThat(response.getTopics()).isNotNull().isNotEmpty();
    }

    @When("user queries subject by id")
    public void userQueriesSubjectById() {
        String query = findGraphqlQueryFromFiles("get-subject-by-id.graphql");
        try {
            lastGetSubjectResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "subjectId", testContext.getSubjectId()))
                    .retrieveSync("getSubjectById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries subject with non-existent id")
    public void userQueriesSubjectWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-subject-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "subjectId", UUID.randomUUID().toString()))
                    .retrieveSync("getSubjectById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the subject should be returned with name {string}")
    public void theSubjectShouldBeReturnedWithName(String name) {
        assertThat(lastGetSubjectResponse).isNotNull();
        assertThat(lastGetSubjectResponse.get("name")).isEqualTo(name);
    }

    @And("the subject should have {int} topic(s)")
    @SuppressWarnings("unchecked")
    public void theSubjectShouldHaveTopics(int expectedCount) {
        List<Object> topics = (List<Object>) lastGetSubjectResponse.get("topics");
        assertThat(topics).hasSize(expectedCount);
    }

    @When("user queries all subjects in the institution")
    @SuppressWarnings("unchecked")
    public void userQueriesAllSubjectsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-subjects-in-institution.graphql");
        lastGetSubjectsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getAllSubjectsInInstitution")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the response should contain {int} subjects")
    public void theResponseShouldContainSubjects(int expectedCount) {
        assertThat(lastGetSubjectsResponse).hasSize(expectedCount);
    }

    @And("the subjects should include names {string}")
    public void theSubjectsShouldIncludeNames(String commaSeparatedNames) {
        List<String> expected = List.of(commaSeparatedNames.split(","));
        assertThat(lastGetSubjectsResponse).extracting(m -> m.get("name"))
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

    private SubjectResponse executeCreateSubject(String name) {
        String query = findGraphqlQueryFromFiles("create-subject.graphql");
        SubjectResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of("institutionId", testContext.getInstitutionId(), "name", name)))
                .retrieveSync("createSubject")
                .toEntity(SubjectResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        testContext.setSubjectId(response.getId());
        return response;
    }
}

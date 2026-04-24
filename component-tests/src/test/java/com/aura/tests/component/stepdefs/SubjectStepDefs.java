package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class SubjectStepDefs {

    private final TestContext testContext;

    @When("user creates a subject with name {string}")
    public void userCreatesASubjectWithName(String subjectName) {
        SubjectResponse response = executeCreateSubject(subjectName);
        testContext.setLastSubjectResponse(response);
    }

    @And("a subject exists with name {string}")
    public void aSubjectExistsWithName(String subjectName) {
        executeCreateSubject(subjectName);
    }

    @Then("the subject {string} should be created successfully")
    public void theSubjectShouldBeCreatedSuccessfully(String subjectName) {
        SubjectResponse response = testContext.getLastSubjectResponse();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(subjectName);
        assertThat(response.getCreatedBy()).isEqualTo(TestConstants.AURA_ADMIN_USER_ID);
        assertThat(response.getUpdatedBy()).isEqualTo(TestConstants.AURA_ADMIN_USER_ID);
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

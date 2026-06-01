package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class DeleteStepDefs {

    private final TestContext testContext;

    @When("user deletes the topic by id")
    public void userDeletesTopicById() {
        executeDelete("delete-topic.graphql", "deleteTopicById", testContext.getTopicId());
    }

    @When("user deletes the subject by id")
    public void userDeletesSubjectById() {
        executeDelete("delete-subject.graphql", "deleteSubjectById", testContext.getSubjectId());
    }

    @When("user deletes the course by id")
    public void userDeletesCourseById() {
        executeDelete("delete-course.graphql", "deleteCourseById", testContext.getCourseId());
    }

    @When("user deletes the lesson by id")
    public void userDeletesLessonById() {
        executeDelete("delete-lesson.graphql", "deleteLessonById", testContext.getLessonId());
    }

    @When("user deletes the outlet by id")
    public void userDeletesOutletById() {
        executeDelete("delete-outlet.graphql", "deleteOutletById", testContext.getOutletId());
    }

    @When("user tries to delete a topic with a random id")
    public void userTriesToDeleteTopicWithRandomId() {
        executeDelete("delete-topic.graphql", "deleteTopicById", UUID.randomUUID().toString());
    }

    @When("user tries to delete a subject with a random id")
    public void userTriesToDeleteSubjectWithRandomId() {
        executeDelete("delete-subject.graphql", "deleteSubjectById", UUID.randomUUID().toString());
    }

    @When("user tries to delete a course with a random id")
    public void userTriesToDeleteCourseWithRandomId() {
        executeDelete("delete-course.graphql", "deleteCourseById", UUID.randomUUID().toString());
    }

    @When("user tries to delete a lesson with a random id")
    public void userTriesToDeleteLessonWithRandomId() {
        executeDelete("delete-lesson.graphql", "deleteLessonById", UUID.randomUUID().toString());
    }

    @When("user tries to delete an outlet with a random id")
    public void userTriesToDeleteOutletWithRandomId() {
        executeDelete("delete-outlet.graphql", "deleteOutletById", UUID.randomUUID().toString());
    }

    @When("user tries to delete a level with a random id")
    public void userTriesToDeleteLevelWithRandomId() {
        executeDelete("delete-level.graphql", "deleteLevelById", UUID.randomUUID().toString());
    }

    @Then("the topic should be deleted successfully")
    public void theTopicShouldBeDeletedSuccessfully() {
        assertDeleteSucceeded();
    }

    @Then("the subject should be deleted successfully")
    public void theSubjectShouldBeDeletedSuccessfully() {
        assertDeleteSucceeded();
    }

    @Then("the course should be deleted successfully")
    public void theCourseShouldBeDeletedSuccessfully() {
        assertDeleteSucceeded();
    }

    @Then("the lesson should be deleted successfully")
    public void theLessonShouldBeDeletedSuccessfully() {
        assertDeleteSucceeded();
    }

    @Then("the outlet should be deleted successfully")
    public void theOutletShouldBeDeletedSuccessfully() {
        assertDeleteSucceeded();
    }

    @And("querying the topic by id should fail with error code {string}")
    public void queryingTopicShouldFailWithErrorCode(String errorCode) {
        executeQueryAndExpectError("get-topic-by-id.graphql", "getTopicById", "topicId", testContext.getTopicId(), errorCode);
    }

    @And("querying the subject by id should fail with error code {string}")
    public void queryingSubjectShouldFailWithErrorCode(String errorCode) {
        executeQueryAndExpectError("get-subject-by-id.graphql", "getSubjectById", "subjectId", testContext.getSubjectId(), errorCode);
    }

    @And("querying the course by id should fail with error code {string}")
    public void queryingCourseShouldFailWithErrorCode(String errorCode) {
        executeQueryAndExpectError("get-course-by-id.graphql", "getCourseById", "courseId", testContext.getCourseId(), errorCode);
    }

    @And("querying the lesson by id should fail with error code {string}")
    public void queryingLessonShouldFailWithErrorCode(String errorCode) {
        executeQueryAndExpectError("get-lesson-by-id.graphql", "getLessonById", "lessonId", testContext.getLessonId(), errorCode);
    }

    private void executeDelete(String graphqlFile, String fieldName, String id) {
        String query = findGraphqlQueryFromFiles(graphqlFile);
        try {
            Boolean result = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "id", id))
                    .retrieveSync(fieldName)
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
            assertThat(result).isTrue();
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    private void executeQueryAndExpectError(String graphqlFile, String fieldName, String paramName, String id, String expectedErrorCode) {
        String query = findGraphqlQueryFromFiles(graphqlFile);
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            paramName, id))
                    .retrieveSync(fieldName)
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
        assertThat(testContext.isRequestFailed()).isTrue();
        assertThat(testContext.getLastErrorMessage()).isEqualTo(expectedErrorCode);
    }

    private void assertDeleteSucceeded() {
        assertThat(testContext.isRequestFailed()).isFalse();
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

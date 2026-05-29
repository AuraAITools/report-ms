package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
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
public class LessonStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastLessonResponseMap;
    private List<Map<String, Object>> lastLessonsResponse;

    @When("user creates a lesson with details:")
    public void userCreatesALesson(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        LessonResponse response = executeCreateLesson(
                data.get("name"), data.get("description"),
                data.get("startDate"), data.get("endDate"));
        assertThat(response.getCreatedBy()).isNotNull();
        testContext.setLastLessonResponse(response);
        testContext.setLessonId(response.getId());
    }

    @And("a lesson exists with name {string}")
    public void aLessonExistsWithName(String name) {
        LessonResponse response = executeCreateLesson(name, "A test lesson",
                "2026-03-01T09:00:00Z", "2026-03-01T11:00:00Z");
        testContext.setLastLessonResponse(response);
        testContext.setLessonId(response.getId());
    }

    @Then("the lesson should be created successfully")
    public void theLessonShouldBeCreatedSuccessfully() {
        LessonResponse lesson = testContext.getLastLessonResponse();
        assertThat(lesson).isNotNull();
        assertThat(lesson.getId()).isNotNull();
    }

    @When("user queries lesson by id")
    public void userQueriesLessonById() {
        String query = findGraphqlQueryFromFiles("get-lesson-by-id.graphql");
        try {
            lastLessonResponseMap = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("lessonId", testContext.getLessonId()))
                    .retrieveSync("getLessonById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
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

    @When("user queries lesson with non-existent id")
    public void userQueriesLessonWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-lesson-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("lessonId", UUID.randomUUID().toString()))
                    .retrieveSync("getLessonById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
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

    @Then("the lesson should be returned with name {string}")
    public void theLessonShouldBeReturnedWithName(String expectedName) {
        assertThat(lastLessonResponseMap).isNotNull();
        assertThat(lastLessonResponseMap.get("name")).isEqualTo(expectedName);
    }

    @And("the lesson course should be returned")
    @SuppressWarnings("unchecked")
    public void theLessonCourseShouldBeReturned() {
        assertThat(lastLessonResponseMap).containsKey("course");
        Map<String, Object> course = (Map<String, Object>) lastLessonResponseMap.get("course");
        assertThat(course).isNotNull();
        assertThat(course.get("id")).isEqualTo(testContext.getCourseId());
    }

    @When("user queries all lessons in the course")
    @SuppressWarnings("unchecked")
    public void userQueriesAllLessonsInTheCourse() {
        String query = findGraphqlQueryFromFiles("get-all-lessons-in-course.graphql");
        lastLessonsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("courseId", testContext.getCourseId()))
                .retrieveSync("getAllLessonsInCourse")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the response should contain {int} lessons")
    public void theResponseShouldContainLessons(int expectedCount) {
        assertThat(lastLessonsResponse).hasSize(expectedCount);
    }

    @And("the lessons should include names {string}")
    public void theLessonsShouldIncludeNames(String commaSeparatedNames) {
        List<String> expected = List.of(commaSeparatedNames.split(","));
        assertThat(lastLessonsResponse).extracting(m -> m.get("name"))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    private LessonResponse executeCreateLesson(String name, String description,
                                               String startDate, String endDate) {
        String query = findGraphqlQueryFromFiles("create-lesson.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", name);
        input.put("description", description);
        input.put("lessonStartTimestamptz", startDate);
        input.put("lessonEndTimestamptz", endDate);
        input.put("courseId", testContext.getCourseId());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());
        input.put("levelIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("subjectIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("topicIds", List.of());
        Map<String, Object> defaultPlan = new HashMap<>();
        defaultPlan.put("plan", name + " plan");
        defaultPlan.put("topicIds", List.of());
        defaultPlan.put("studentIds", List.of());
        defaultPlan.put("materialIds", List.of());
        defaultPlan.put("lessonObjectives", List.of());
        input.put("lessonPlans", List.of(defaultPlan));

        LessonResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonInCourse")
                .toEntity(LessonResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(name);
        return response;
    }
}

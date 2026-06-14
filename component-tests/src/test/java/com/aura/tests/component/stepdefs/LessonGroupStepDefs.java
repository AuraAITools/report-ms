package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonGroupStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("the user creates a lesson group in the lesson with the following details:")
    @SuppressWarnings("unchecked")
    public void theUserCreatesALessonGroupInTheLessonWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        String name = data.get("name");
        String plan = data.get("plan");
        String studentIdsRaw = data.getOrDefault("studentIds", "");
        List<String> studentIds = (studentIdsRaw == null || studentIdsRaw.isBlank() || "<empty>".equals(studentIdsRaw))
                ? List.of()
                : List.of(studentIdsRaw.split(","));

        Map<String, Object> planInput = new HashMap<>();
        planInput.put("plan", plan != null ? plan : "");
        planInput.put("topicIds", List.of());
        planInput.put("materialIds", List.of());
        planInput.put("lessonObjectives", List.of());

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("name", name);
        input.put("studentIds", studentIds);
        input.put("plan", planInput);

        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-group.graphql");

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonGroupInLesson")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();

        testContext.setLessonGroupId((String) response.get("id"));
        testContext.setLastLessonGroupResponseMap(response);
    }

    @Then("the lesson group should be created successfully")
    public void theLessonGroupShouldBeCreatedSuccessfully() {
        Map<String, Object> response = testContext.getLastLessonGroupResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
    }

    @Then("the lesson group response should have name {string}")
    public void theLessonGroupResponseShouldHaveName(String expectedName) {
        Map<String, Object> response = testContext.getLastLessonGroupResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("name")).isEqualTo(expectedName);
    }

    @Then("the lesson group response should reference the lesson")
    @SuppressWarnings("unchecked")
    public void theLessonGroupResponseShouldReferenceTheLesson() {
        Map<String, Object> response = testContext.getLastLessonGroupResponseMap();
        assertThat(response).isNotNull();
        Map<String, Object> lesson = (Map<String, Object>) response.get("lesson");
        assertThat(lesson).isNotNull();
        assertThat(lesson.get("id")).isEqualTo(testContext.getLessonId());
    }

    @Then("the lesson group response lessonGroupPlan should have state {string}")
    @SuppressWarnings("unchecked")
    public void theLessonGroupResponseLessonGroupPlanShouldHaveState(String expectedState) {
        Map<String, Object> response = testContext.getLastLessonGroupResponseMap();
        assertThat(response).isNotNull();
        Map<String, Object> lessonGroupPlan = (Map<String, Object>) response.get("lessonGroupPlan");
        assertThat(lessonGroupPlan).isNotNull();
        assertThat(lessonGroupPlan.get("state")).isEqualTo(expectedState);
    }

    @When("the user deletes the existing lesson group")
    public void theUserDeletesTheExistingLessonGroup() {
        String lessonGroupId = testContext.getLessonGroupId();
        assertThat(lessonGroupId).as("LessonGroup id not recorded in TestContext").isNotNull();
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-group-by-id.graphql");
        Boolean response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", lessonGroupId))
                .retrieveSync("deleteLessonGroupById")
                .toEntity(Boolean.class);
        assertThat(response).isTrue();
        testContext.setLastResponseData(Map.of("deleted", response));
    }

    @When("the user tries to delete the existing lesson group")
    public void theUserTriesToDeleteTheExistingLessonGroup() {
        String lessonGroupId = testContext.getLessonGroupId();
        assertThat(lessonGroupId).as("LessonGroup id not recorded in TestContext").isNotNull();
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-group-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of(
                            "institutionId", testContext.getInstitutionId(),
                            "lessonId", testContext.getLessonId(),
                            "id", lessonGroupId))
                    .retrieveSync("deleteLessonGroupById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var ext = errors.getFirst().getExtensions();
                if (ext != null && ext.containsKey("error_code")) {
                    testContext.setLastErrorMessage(ext.get("error_code").toString());
                }
                if (ext != null && ext.containsKey("http_status")) {
                    try {
                        testContext.setLastHttpStatus(Integer.parseInt(ext.get("http_status").toString()));
                    } catch (NumberFormatException ignored) {
                        // ignore
                    }
                }
            }
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the lesson group should be deleted successfully")
    public void theLessonGroupShouldBeDeletedSuccessfully() {
        assertThat(testContext.getLastResponseData()).containsEntry("deleted", true);
    }

    @Then("the lesson group should no longer exist in the database")
    public void theLessonGroupShouldNoLongerExistInTheDatabase() {
        String lessonGroupId = testContext.getLessonGroupId();
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.lesson_groups WHERE id = ?::uuid",
                Integer.class, lessonGroupId);
        assertThat(count).as("Expected lesson group %s to be deleted", lessonGroupId).isZero();
    }

    @When("the user adds student {string} to the existing lesson group")
    @SuppressWarnings("unchecked")
    public void theUserAddsStudentToTheExistingLessonGroup(String studentName) {
        String studentId = testContext.getStudentIdByName(studentName);
        assertThat(studentId).as("Student '%s' not recorded", studentName).isNotNull();
        String lessonGroupId = resolveLessonGroupIdForCurrentLesson();
        assertThat(lessonGroupId).as("LessonGroup id not recorded").isNotNull();

        String graphqlQuery = findGraphqlQueryFromFiles("add-students-to-lesson-group.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", lessonGroupId);
        input.put("studentIds", List.of(studentId));

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("addStudentsToLessonGroup")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        testContext.setLastLessonGroupResponseMap(response);
    }

    @When("the user tries to add student {string} to the existing lesson group")
    public void theUserTriesToAddStudentToTheExistingLessonGroup(String studentName) {
        String studentId = testContext.getStudentIdByName(studentName);
        assertThat(studentId).as("Student '%s' not recorded", studentName).isNotNull();
        String lessonGroupId = resolveLessonGroupIdForCurrentLesson();
        assertThat(lessonGroupId).as("LessonGroup id not recorded").isNotNull();

        String graphqlQuery = findGraphqlQueryFromFiles("add-students-to-lesson-group.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", lessonGroupId);
        input.put("studentIds", List.of(studentId));

        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of("input", input))
                    .retrieveSync("addStudentsToLessonGroup")
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
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the lesson group response should include student {string}")
    @SuppressWarnings("unchecked")
    public void theLessonGroupResponseShouldIncludeStudent(String studentName) {
        String studentId = testContext.getStudentIdByName(studentName);
        Map<String, Object> response = testContext.getLastLessonGroupResponseMap();
        assertThat(response).isNotNull();
        List<Map<String, Object>> lessonStudents = (List<Map<String, Object>>) response.get("lessonStudents");
        assertThat(lessonStudents).extracting(s -> ((Map<String, Object>) s.get("student")).get("id"))
                .as("Expected lesson group to include student '%s' (id=%s)", studentName, studentId)
                .contains(studentId);
    }

    private String resolveLessonGroupIdForCurrentLesson() {
        String cached = testContext.getLessonGroupId();
        if (cached != null) return cached;
        String lessonId = testContext.getLessonId();
        String resolved = jdbcTemplate.queryForObject(
                "SELECT id::text FROM public.lesson_groups WHERE lesson_id = ?::uuid LIMIT 1",
                String.class, lessonId);
        assertThat(resolved)
                .as("No lesson_group row found for lesson id %s", lessonId)
                .isNotNull();
        testContext.setLessonGroupId(resolved);
        return resolved;
    }

}

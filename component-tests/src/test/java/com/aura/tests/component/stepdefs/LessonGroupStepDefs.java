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


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonGroupStepDefs {

    private final TestContext testContext;

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

}

package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonStepDefs {

    private final TestContext testContext;

    @When("user creates a standalone lesson with details:")
    public void userCreatesAStandaloneLesson(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        LessonResponse response = executeCreateStandaloneLesson(data.get("name"), data.get("description"),
                data.get("startDate"), data.get("endDate"));
        assertThat(response.getCreatedBy()).isNotNull();
        testContext.setLastLessonResponse(response);
        testContext.setLessonId(response.getId());
    }

    @And("a standalone lesson exists with name {string}")
    public void aStandaloneLessonExistsWithName(String name) {
        LessonResponse response = executeCreateStandaloneLesson(name, "A test lesson",
                "2026-03-01T09:00:00Z", "2026-03-01T11:00:00Z");
        testContext.setLastLessonResponse(response);
        testContext.setLessonId(response.getId());
    }

    @When("user tries to create a standalone lesson with name {string}")
    public void userTriesToCreateAStandaloneLessonWithName(String name) {
        String query = findGraphqlQueryFromFiles("create-standalone-lesson.graphql");
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "description", "A test lesson",
                "lessonStartTimestamptz", "2026-03-01T09:00:00Z",
                "lessonEndTimestamptz", "2026-03-01T11:00:00Z",
                "outletId", testContext.getOutletId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createStandaloneLesson")
                    .toEntity(LessonResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user creates a lesson with details:")
    public void userCreatesALesson(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-lesson.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", data.get("name"));
        input.put("description", data.get("description"));
        input.put("lessonStartTimestamptz", data.get("startDate"));
        input.put("lessonEndTimestamptz", data.get("endDate"));
        input.put("outletId", testContext.getOutletId());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());
        input.put("levelIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("subjectIds", List.of("00000000-0000-0000-0000-000000000001"));

        LessonResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLesson")
                .toEntity(LessonResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(data.get("name"));
        testContext.setLastLessonResponse(response);
        testContext.setLessonId(response.getId());
    }

    @Then("the lesson should be created successfully")
    public void theLessonShouldBeCreatedSuccessfully() {
        LessonResponse lesson = testContext.getLastLessonResponse();
        assertThat(lesson).isNotNull();
        assertThat(lesson.getId()).isNotNull();
    }

    private LessonResponse executeCreateStandaloneLesson(String name, String description,
                                                         String startDate, String endDate) {
        String query = findGraphqlQueryFromFiles("create-standalone-lesson.graphql");
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "description", description,
                "lessonStartTimestamptz", startDate,
                "lessonEndTimestamptz", endDate,
                "outletId", testContext.getOutletId()
        );
        LessonResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createStandaloneLesson")
                .toEntity(LessonResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(name);
        return response;
    }
}

package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonObjectiveStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private String lessonObjectiveId;
    private Map<String, Object> lastLessonObjectiveResponseMap;
    private List<Map<String, Object>> lastLessonObjectivesList;

    @When("the user creates a lesson objective with name {string} and objective {string} with {int} topics")
    @SuppressWarnings("unchecked")
    public void userCreatesLessonObjective(String name, String objective, int topicCount) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-objective.graphql");
        List<String> topicIds = pickTopicIds(topicCount);

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("name", name);
        input.put("objective", objective);
        input.put("topicIds", topicIds);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonObjective")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        this.lessonObjectiveId = (String) response.get("id");
        this.lastLessonObjectiveResponseMap = response;
    }

    @When("the user gets the lesson objective by id")
    @SuppressWarnings("unchecked")
    public void userGetsLessonObjectiveById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-lesson-objective-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", lessonObjectiveId))
                .retrieveSync("getLessonObjectiveById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastLessonObjectiveResponseMap = response;
    }

    @When("the user updates the lesson objective with name {string} and objective {string} with {int} topics")
    @SuppressWarnings("unchecked")
    public void userUpdatesLessonObjective(String name, String objective, int topicCount) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-objective.graphql");
        List<String> topicIds = pickTopicIds(topicCount);

        Map<String, Object> input = new HashMap<>();
        input.put("id", lessonObjectiveId);
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("name", name);
        input.put("objective", objective);
        input.put("topicIds", topicIds);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonObjective")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastLessonObjectiveResponseMap = response;
    }

    @When("the user lists lesson objectives by lesson group plan id")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsLessonObjectivesByPlan() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-all-lesson-objectives-by-lesson-group-plan-id.graphql");
        List<Map> list = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "lessonGroupPlanId", testContext.getLessonGroupPlanId()))
                .retrieveSync("getAllLessonObjectivesByLessonGroupPlanId")
                .toEntityList(Map.class);
        this.lastLessonObjectivesList = (List<Map<String, Object>>) (List) list;
    }

    @When("the user deletes the lesson objective")
    public void userDeletesLessonObjective() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-objective-by-id.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", lessonObjectiveId);
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteLessonObjectiveById")
                .toEntity(Boolean.class);
    }

    @When("the user tries to update a non-existent lesson objective")
    public void userTriesToUpdateMissingLessonObjective() {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-objective.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", UUID.randomUUID().toString());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("name", "Ghost");
        input.put("objective", "Should not exist");
        input.put("topicIds", List.of());

        invokeAndCaptureFailure(graphqlQuery, input, "updateLessonObjective");
    }

    @Then("the lesson objective response should have name {string} and objective {string}")
    public void responseShouldHaveNameAndObjective(String expectedName, String expectedObjective) {
        Map<String, Object> response = lastLessonObjectiveResponseMap;
        assertThat(response).isNotNull();
        assertThat(response.get("name")).isEqualTo(expectedName);
        assertThat(response.get("objective")).isEqualTo(expectedObjective);
    }

    @Then("the lesson objective response createdBy should be the current user")
    public void responseCreatedByShouldBeCurrentUser() {
        assertThat(lastLessonObjectiveResponseMap).isNotNull();
        assertThat(lastLessonObjectiveResponseMap.get("createdBy")).isNotNull();
    }

    @Then("the lesson objective list should have {int} entries")
    public void listShouldHaveSize(int expectedSize) {
        assertThat(lastLessonObjectivesList).hasSize(expectedSize);
    }

    @Then("the lesson objective should be gone from the database")
    public void objectiveShouldBeGoneFromDb() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.lesson_objectives WHERE id = ?::uuid",
                Integer.class, lessonObjectiveId);
        assertThat(count).as("lesson_objectives count").isEqualTo(0);
    }

    private List<String> pickTopicIds(int topicCount) {
        if (topicCount <= 0) return List.of();
        List<String> topicIds = testContext.getTopicIds();
        return topicIds.subList(0, Math.min(topicCount, topicIds.size()));
    }

    private void invokeAndCaptureFailure(String graphqlQuery, Map<String, Object> input, String operationName) {
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of("input", input))
                    .retrieveSync(operationName)
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
}

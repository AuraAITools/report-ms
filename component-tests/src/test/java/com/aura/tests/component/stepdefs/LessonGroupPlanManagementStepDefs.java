package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonGroupPlanManagementStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("the user updates the lesson group plan with plan {string} and recap {string} and state {string}")
    @SuppressWarnings("unchecked")
    public void userUpdatesLessonGroupPlan(String plan, String recap, String state) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-group-plan.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", testContext.getLessonGroupPlanId());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("plan", plan);
        input.put("recap", recap);
        input.put("state", state);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonGroupPlan")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        testContext.setLastLessonGroupPlanResponseMap(response);
    }

    @Then("the updated lesson group plan response should have plan {string}")
    public void updatedPlanShouldHavePlan(String expected) {
        assertThat(testContext.getLastLessonGroupPlanResponseMap().get("plan")).isEqualTo(expected);
    }

    @Then("the updated lesson group plan response should have recap {string}")
    public void updatedPlanShouldHaveRecap(String expected) {
        assertThat(testContext.getLastLessonGroupPlanResponseMap().get("recap")).isEqualTo(expected);
    }

    @Then("the updated lesson group plan response should have state {string}")
    public void updatedPlanShouldHaveState(String expected) {
        assertThat(testContext.getLastLessonGroupPlanResponseMap().get("state")).isEqualTo(expected);
    }

    @When("the user adds the material to the lesson group plan")
    public void userAddsMaterialsToPlan() {
        String graphqlQuery = findGraphqlQueryFromFiles("add-materials-to-lesson-group-plan.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("materialIds", List.of(testContext.getMaterialId()));

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("addMaterialsToLessonGroupPlan")
                .toEntityList(Map.class);
    }

    @When("the user removes the material from the lesson group plan")
    public void userRemovesMaterialsFromPlan() {
        String graphqlQuery = findGraphqlQueryFromFiles("remove-materials-from-lesson-group-plan.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("materialIds", List.of(testContext.getMaterialId()));

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("removeMaterialsFromLessonGroupPlan")
                .toEntity(Boolean.class);
    }

    @When("the user adds {int} topics to the lesson group plan")
    public void userAddsTopicsToPlan(int topicCount) {
        List<String> topicIds = testContext.getTopicIds().subList(0, topicCount);
        String graphqlQuery = findGraphqlQueryFromFiles("add-topics-to-lesson-group-plan.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("topicIds", topicIds);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("addTopicsToLessonGroupPlan")
                .toEntityList(Map.class);
    }

    @When("the user removes {int} topics from the lesson group plan")
    public void userRemovesTopicsFromPlan(int topicCount) {
        List<String> topicIds = testContext.getTopicIds().subList(0, topicCount);
        String graphqlQuery = findGraphqlQueryFromFiles("remove-topics-from-lesson-group-plan.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("topicIds", topicIds);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("removeTopicsFromLessonGroupPlan")
                .toEntity(Boolean.class);
    }

    @Then("the lesson group plan should have {int} linked topics in the database")
    public void planShouldHaveTopicLinksInDb(int expectedCount) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.lesson_group_plan_topic_links WHERE lesson_group_plan_id = ?::uuid",
                Integer.class, testContext.getLessonGroupPlanId());
        assertThat(count).as("lesson_group_plan_topic_links rows").isEqualTo(expectedCount);
    }
}

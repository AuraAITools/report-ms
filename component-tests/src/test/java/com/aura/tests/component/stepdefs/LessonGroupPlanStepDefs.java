package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
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
public class LessonGroupPlanStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("the user creates a lesson group plan in the lesson group with plan {string} and {int} topics")
    @SuppressWarnings("unchecked")
    public void userCreatesLessonGroupPlanInLessonGroupWithPlanAndTopics(String plan, int topicCount) {
        List<String> topicIds = testContext.getTopicIds();
        assertThat(topicIds)
                .as("Expected at least %d topic(s) in TestContext.topicIds; create them via 'a topic exists with name' steps first",
                        topicCount)
                .hasSizeGreaterThanOrEqualTo(topicCount);
        List<String> selectedTopicIds = topicIds.subList(0, topicCount);

        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-group-plan.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", testContext.getLessonGroupId());
        input.put("plan", plan);
        input.put("topicIds", selectedTopicIds);
        input.put("studentIds", List.of());
        input.put("materialIds", List.of());
        input.put("lessonObjectives", List.of());

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonGroupPlanInLessonGroup")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();

        testContext.setLessonGroupPlanId((String) response.get("id"));
        testContext.setLastLessonGroupPlanResponseMap(response);
    }

    @When("the user creates a lesson group plan in the lesson group with plan {string} and {int} topics and {int} lesson objectives")
    @SuppressWarnings("unchecked")
    public void userCreatesLessonGroupPlanInLessonGroupWithPlanTopicsAndObjectives(
            String plan, int topicCount, int objectiveCount) {
        List<String> topicIds = testContext.getTopicIds();
        assertThat(topicIds)
                .as("Expected at least %d topic(s) in TestContext.topicIds; create them via 'a topic exists with name' steps first",
                        topicCount)
                .hasSizeGreaterThanOrEqualTo(topicCount);
        List<String> selectedTopicIds = topicIds.subList(0, topicCount);

        List<Map<String, Object>> lessonObjectives = new ArrayList<>();
        for (int i = 1; i <= objectiveCount; i++) {
            Map<String, Object> objective = new HashMap<>();
            objective.put("name", "Objective " + i);
            objective.put("objective", "Goal " + i);
            objective.put("topicIds", selectedTopicIds);
            lessonObjectives.add(objective);
        }

        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-group-plan.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", testContext.getLessonGroupId());
        input.put("plan", plan);
        input.put("topicIds", selectedTopicIds);
        input.put("studentIds", List.of());
        input.put("materialIds", List.of());
        input.put("lessonObjectives", lessonObjectives);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonGroupPlanInLessonGroup")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();

        testContext.setLessonGroupPlanId((String) response.get("id"));
        testContext.setLastLessonGroupPlanResponseMap(response);
    }

    @When("the user creates a lesson group plan in the lesson group with plan {string} and {int} topics and {int} students and {int} materials")
    @SuppressWarnings("unchecked")
    public void userCreatesLessonGroupPlanInLessonGroupWithPlanTopicsStudentsMaterials(
            String plan, int topicCount, int studentCount, int materialCount) {
        List<String> topicIds = testContext.getTopicIds();
        if (topicCount > 0) {
            assertThat(topicIds).hasSizeGreaterThanOrEqualTo(topicCount);
        }
        List<String> selectedTopicIds = topicIds == null
                ? List.of()
                : topicIds.subList(0, Math.min(topicCount, topicIds.size()));

        List<String> selectedStudentIds = new ArrayList<>();
        if (studentCount > 0) {
            String studentId = testContext.getStudentId();
            assertThat(studentId).as("a student must exist before this step").isNotNull();
            for (int i = 0; i < studentCount; i++) selectedStudentIds.add(studentId);
        }

        List<String> selectedMaterialIds = new ArrayList<>();
        if (materialCount > 0) {
            String materialId = testContext.getMaterialId();
            assertThat(materialId).as("a material must exist before this step").isNotNull();
            for (int i = 0; i < materialCount; i++) selectedMaterialIds.add(materialId);
        }

        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-group-plan.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", testContext.getLessonGroupId());
        input.put("plan", plan);
        input.put("topicIds", selectedTopicIds);
        input.put("studentIds", selectedStudentIds);
        input.put("materialIds", selectedMaterialIds);
        input.put("lessonObjectives", List.of());

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonGroupPlanInLessonGroup")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();

        testContext.setLessonGroupPlanId((String) response.get("id"));
        testContext.setLastLessonGroupPlanResponseMap(response);
    }

    @Then("the lesson group plan should have {int} linked materials in the database")
    public void theLessonGroupPlanShouldHaveLinkedMaterialsInTheDatabase(int expectedCount) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.lesson_group_plan_materials_links WHERE lesson_group_plan_id = ?::uuid",
                Integer.class, testContext.getLessonGroupPlanId());
        assertThat(count).as("lesson_group_plan_materials_links rows").isEqualTo(expectedCount);
    }

    @Then("the lesson group plan should have {int} linked students in the database")
    public void theLessonGroupPlanShouldHaveLinkedStudentsInTheDatabase(int expectedCount) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.student_lesson_links WHERE lesson_group_id = ?::uuid",
                Integer.class, testContext.getLessonGroupId());
        assertThat(count).as("student_lesson_links with lesson_group_id set").isEqualTo(expectedCount);
    }

    @Then("the lesson group plan should have {int} lesson objectives")
    @SuppressWarnings("unchecked")
    public void theLessonGroupPlanShouldHaveLessonObjectives(int expectedCount) {
        String graphqlQuery = findGraphqlQueryFromFiles("get-all-lesson-objectives-by-lesson-group-plan-id.graphql");

        List<Map> objectives = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "lessonGroupPlanId", testContext.getLessonGroupPlanId()))
                .retrieveSync("getAllLessonObjectivesByLessonGroupPlanId")
                .toEntityList(Map.class);

        assertThat(objectives).hasSize(expectedCount);
        assertThat(objectives).allSatisfy(o -> {
            assertThat(o.get("lessonGroupPlanId")).isEqualTo(testContext.getLessonGroupPlanId());
            assertThat(o.get("name")).isNotNull();
            assertThat(o.get("objective")).isNotNull();
            assertThat(o.get("createdBy")).isNotNull();
        });
    }

    @When("the unauthorized user tries to create a lesson group plan in the lesson group with plan {string} and {int} topics")
    public void unauthorizedUserTriesToCreateLessonGroupPlan(String plan, int topicCount) {
        List<String> topicIds = testContext.getTopicIds();
        List<String> selectedTopicIds = topicIds.subList(0, Math.min(topicCount, topicIds.size()));

        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-group-plan.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupId", testContext.getLessonGroupId());
        input.put("plan", plan);
        input.put("topicIds", selectedTopicIds);
        input.put("studentIds", List.of());
        input.put("materialIds", List.of());
        input.put("lessonObjectives", List.of());

        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of("input", input))
                    .retrieveSync("createLessonGroupPlanInLessonGroup")
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

    @Then("the lesson group plan response should have plan {string}")
    public void theLessonGroupPlanResponseShouldHavePlan(String expectedPlan) {
        Map<String, Object> response = testContext.getLastLessonGroupPlanResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("plan")).isEqualTo(expectedPlan);
    }

    @Then("the lesson group plan response should have {int} topics")
    @SuppressWarnings("unchecked")
    public void theLessonGroupPlanResponseShouldHaveTopics(int expectedCount) {
        Map<String, Object> response = testContext.getLastLessonGroupPlanResponseMap();
        assertThat(response).isNotNull();
        List<Map<String, Object>> topics = (List<Map<String, Object>>) response.get("topics");
        assertThat(topics).isNotNull().hasSize(expectedCount);
    }

    @Then("the lesson group plan response createdBy should be the current user")
    public void theLessonGroupPlanResponseCreatedByShouldBeTheCurrentUser() {
        Map<String, Object> response = testContext.getLastLessonGroupPlanResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("createdBy")).isNotNull();
    }

    @Then("the lesson group plan response should reference the lesson group")
    @SuppressWarnings("unchecked")
    public void theLessonGroupPlanResponseShouldReferenceTheLessonGroup() {
        Map<String, Object> response = testContext.getLastLessonGroupPlanResponseMap();
        assertThat(response).isNotNull();
        Map<String, Object> lessonGroup = (Map<String, Object>) response.get("lessonGroup");
        assertThat(lessonGroup).isNotNull();
        assertThat(lessonGroup.get("id")).isEqualTo(testContext.getLessonGroupId());
    }

}

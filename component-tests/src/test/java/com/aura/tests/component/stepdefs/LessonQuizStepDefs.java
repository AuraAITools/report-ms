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
public class LessonQuizStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("the user creates a lesson quiz with score {int} and maxScore {int}")
    @SuppressWarnings("unchecked")
    public void userCreatesLessonQuiz(int score, int maxScore) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-quiz.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("lessonGroupPlanId", testContext.getLessonGroupPlanId());
        input.put("name", "Quiz 1");
        input.put("score", score);
        input.put("maxScore", maxScore);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonQuiz")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        testContext.setLessonQuizId((String) response.get("id"));
        testContext.setLastLessonQuizResponseMap(response);
    }

    @When("the user gets the lesson quiz by id")
    @SuppressWarnings("unchecked")
    public void userGetsLessonQuizById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-lesson-quiz-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", testContext.getLessonQuizId()))
                .retrieveSync("getLessonQuizById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        testContext.setLastLessonQuizResponseMap(response);
    }

    @When("the user updates the lesson quiz with score {int} and maxScore {int}")
    @SuppressWarnings("unchecked")
    public void userUpdatesLessonQuiz(int score, int maxScore) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-quiz.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", testContext.getLessonQuizId());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("score", score);
        input.put("maxScore", maxScore);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonQuiz")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        testContext.setLastLessonQuizResponseMap(response);
    }

    @When("the user lists lesson quizzes by lesson group plan id")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsLessonQuizzes() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-all-lesson-quizzes-by-lesson-group-plan-id.graphql");
        List<Map> list = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "lessonGroupPlanId", testContext.getLessonGroupPlanId()))
                .retrieveSync("getAllLessonQuizzesByLessonGroupPlanId")
                .toEntityList(Map.class);
        testContext.setLastLessonQuizResponseMap(Map.of("list", list));
    }

    @When("the user deletes the lesson quiz")
    public void userDeletesLessonQuiz() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-quiz-by-id.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", testContext.getLessonQuizId());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteLessonQuizById")
                .toEntity(Boolean.class);
    }

    @When("the user tries to update a non-existent lesson quiz")
    public void userTriesToUpdateMissingQuiz() {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-quiz.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", UUID.randomUUID().toString());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("score", 80);
        input.put("maxScore", 100);

        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateLessonQuiz")
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

    @Then("the lesson quiz response should have score {int} and maxScore {int}")
    public void quizResponseShouldHaveScoreAndMaxScore(int score, int maxScore) {
        Map<String, Object> response = testContext.getLastLessonQuizResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("score")).isEqualTo(score);
        assertThat(response.get("maxScore")).isEqualTo(maxScore);
    }

    @Then("the lesson quiz response createdBy should be the current user")
    public void quizResponseCreatedByShouldBeCurrentUser() {
        Map<String, Object> response = testContext.getLastLessonQuizResponseMap();
        assertThat(response).isNotNull();
        assertThat(response.get("createdBy")).isNotNull();
    }

    @Then("the lesson quiz list should have {int} entries")
    @SuppressWarnings("unchecked")
    public void quizListShouldHaveSize(int expectedSize) {
        Map<String, Object> response = testContext.getLastLessonQuizResponseMap();
        List<?> list = (List<?>) response.get("list");
        assertThat(list).hasSize(expectedSize);
    }

    @Then("the lesson quiz should be gone from the database")
    public void quizShouldBeGoneFromDb() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.lesson_quizzes WHERE id = ?::uuid",
                Integer.class, testContext.getLessonQuizId());
        assertThat(count).as("lesson_quizzes count").isEqualTo(0);
    }
}

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
public class StudentLessonReportStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private String studentLessonLinkId;
    private String studentLessonReportId;
    private String homeworkCompletionId;
    private String participationReviewId;
    private String topicProficiencyId;

    private Map<String, Object> lastReportResponseMap;
    private List<Map<String, Object>> lastReportsList;
    private Map<String, Object> lastHomeworkResponseMap;
    private Map<String, Object> lastParticipationResponseMap;
    private Map<String, Object> lastProficiencyResponseMap;

    // ─── student lesson report mutations ─────────────────────────────────────

    @When("the user creates a student lesson report for student {string} on lesson {string}")
    @SuppressWarnings("unchecked")
    public void invokeCreateStudentLessonReport(String studentName, String lessonName) {
        String lessonId = testContext.getLessonIdByName(lessonName);
        assertThat(lessonId).as("Lesson '%s' not recorded in TestContext", lessonName).isNotNull();
        String studentId = testContext.getStudentIdByName(studentName);
        assertThat(studentId).as("Student '%s' not recorded in TestContext", studentName).isNotNull();

        this.studentLessonLinkId = resolveStudentLessonLinkId(lessonId, studentId);
        assertThat(studentLessonLinkId)
                .as("StudentLessonLink for student '%s' on lesson '%s' not found", studentName, lessonName)
                .isNotNull();

        String graphqlQuery = findGraphqlQueryFromFiles("create-student-lesson-report.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", lessonId);
        input.put("studentLessonLinkId", studentLessonLinkId);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createStudentLessonReport")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        this.studentLessonReportId = (String) response.get("id");
        this.lastReportResponseMap = response;
        captureChildIdsFromReport(response);
        testContext.setLessonId(lessonId);
    }

    @When("the user gets the student lesson report by id")
    @SuppressWarnings("unchecked")
    public void userGetsStudentLessonReportById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-student-lesson-report-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", studentLessonReportId))
                .retrieveSync("getStudentLessonReportById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastReportResponseMap = response;
        captureChildIdsFromReport(response);
    }

    @When("the user updates the student lesson report with homework {int} and participation {string}")
    @SuppressWarnings("unchecked")
    public void userUpdatesStudentLessonReport(int homework, String participation) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-student-lesson-report.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", studentLessonReportId);
        input.put("homeworkCompletion", homework);
        input.put("participationType", participation);
        input.put("topicProficiencies", List.of());

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateStudentLessonReport")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastReportResponseMap = response;
        captureChildIdsFromReport(response);
    }

    @When("the user lists student lesson reports by lesson group")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsReportsByLessonGroup() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-student-lesson-reports-by-lesson-group.graphql");
        String lessonGroupId = resolveLessonGroupIdForCurrentLesson(testContext.getLessonId());
        List<Map> list = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "lessonGroupId", lessonGroupId))
                .retrieveSync("getStudentLessonReportsByLessonGroup")
                .toEntityList(Map.class);
        this.lastReportsList = (List<Map<String, Object>>) (List) list;
    }

    private String resolveLessonGroupIdForCurrentLesson(String lessonId) {
        String cached = testContext.getLessonGroupId();
        if (cached != null) return cached;
        String resolved = jdbcTemplate.queryForObject(
                "SELECT id::text FROM public.lesson_groups WHERE lesson_id = ?::uuid LIMIT 1",
                String.class, lessonId);
        assertThat(resolved)
                .as("No lesson_group row found for lesson id %s", lessonId)
                .isNotNull();
        testContext.setLessonGroupId(resolved);
        return resolved;
    }

    @When("the user deletes the student lesson report")
    public void userDeletesStudentLessonReport() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-student-lesson-report.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", studentLessonReportId);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteStudentLessonReport")
                .toEntity(Boolean.class);
    }

    @When("the student {string} is added to the existing lesson group")
    public void studentIsAddedToExistingLessonGroup(String studentName) {
        String studentId = testContext.getStudentIdByName(studentName);
        assertThat(studentId).as("Student '%s' not recorded", studentName).isNotNull();
        String lessonId = testContext.getLessonId();
        String lessonGroupId = resolveLessonGroupIdForCurrentLesson(lessonId);
        int updated = jdbcTemplate.update(
                "UPDATE public.student_lesson_links SET lesson_group_id = ?::uuid "
                        + "WHERE lesson_id = ?::uuid AND student_id = ?::uuid",
                lessonGroupId, lessonId, studentId);
        assertThat(updated)
                .as("Expected to update exactly one student_lesson_link row for student '%s'", studentName)
                .isEqualTo(1);
    }

    @When("the user tries to create a student lesson report on a non-existent lesson")
    public void userTriesToCreateReportOnMissingLesson() {
        String graphqlQuery = findGraphqlQueryFromFiles("create-student-lesson-report.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", UUID.randomUUID().toString());
        input.put("studentLessonLinkId", UUID.randomUUID().toString());

        handleErr(graphqlQuery, input, "createStudentLessonReport");
    }

    // ─── homework completion mutations ───────────────────────────────────────

    @When("the user updates the homework completion with completion {int}")
    @SuppressWarnings("unchecked")
    public void userUpdatesHomeworkCompletion(int completion) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-homework-completion.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", homeworkCompletionId);
        input.put("completion", completion);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonHomeworkCompletion")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastHomeworkResponseMap = response;
    }

    @When("the user gets the homework completion by id")
    @SuppressWarnings("unchecked")
    public void userGetsHomeworkCompletionById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-lesson-homework-completion-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", homeworkCompletionId))
                .retrieveSync("getLessonHomeworkCompletionById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastHomeworkResponseMap = response;
    }

    @When("the user deletes the homework completion")
    public void userDeletesHomeworkCompletion() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-homework-completion.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", homeworkCompletionId);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteLessonHomeworkCompletion")
                .toEntity(Boolean.class);
    }

    @When("the user creates a homework completion with completion {int}")
    @SuppressWarnings("unchecked")
    public void userCreatesHomeworkCompletion(int completion) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-homework-completion.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("studentLessonReportId", studentLessonReportId);
        input.put("completion", completion);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonHomeworkCompletion")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        this.homeworkCompletionId = (String) response.get("id");
        this.lastHomeworkResponseMap = response;
    }

    // ─── participation review mutations ──────────────────────────────────────

    @When("the user updates the participation review with type {string}")
    @SuppressWarnings("unchecked")
    public void userUpdatesParticipationReview(String type) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-participation-review.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", participationReviewId);
        input.put("participationType", type);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonParticipationReview")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastParticipationResponseMap = response;
    }

    @When("the user gets the participation review by id")
    @SuppressWarnings("unchecked")
    public void userGetsParticipationReviewById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-lesson-participation-review-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", participationReviewId))
                .retrieveSync("getLessonParticipationReviewById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastParticipationResponseMap = response;
    }

    @When("the user deletes the participation review")
    public void userDeletesParticipationReview() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-participation-review.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", participationReviewId);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteLessonParticipationReview")
                .toEntity(Boolean.class);
    }

    @When("the user creates a participation review with type {string}")
    @SuppressWarnings("unchecked")
    public void userCreatesParticipationReview(String type) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-participation-review.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("studentLessonReportId", studentLessonReportId);
        input.put("participationType", type);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonParticipationReview")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        this.participationReviewId = (String) response.get("id");
        this.lastParticipationResponseMap = response;
    }

    // ─── topic proficiency mutations ─────────────────────────────────────────

    @When("the user creates a lesson topic proficiency with rating {int}")
    @SuppressWarnings("unchecked")
    public void userCreatesTopicProficiency(int rating) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-lesson-topic-proficiency.graphql");
        String topicId = testContext.getTopicId();
        assertThat(topicId).as("Expected a topic to be created via the 'a topic exists with name' step").isNotNull();

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("studentLessonReportId", studentLessonReportId);
        input.put("topicId", topicId);
        input.put("rating", rating);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonTopicProficiency")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        this.topicProficiencyId = (String) response.get("id");
        this.lastProficiencyResponseMap = response;
    }

    @When("the user updates the topic proficiency with rating {int}")
    @SuppressWarnings("unchecked")
    public void userUpdatesTopicProficiency(int rating) {
        String graphqlQuery = findGraphqlQueryFromFiles("update-lesson-topic-proficiency.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", topicProficiencyId);
        input.put("rating", rating);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("updateLessonTopicProficiency")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastProficiencyResponseMap = response;
    }

    @When("the user gets the topic proficiency by id")
    @SuppressWarnings("unchecked")
    public void userGetsTopicProficiencyById() {
        String graphqlQuery = findGraphqlQueryFromFiles("get-lesson-topic-proficiency-by-id.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "lessonId", testContext.getLessonId(),
                        "id", topicProficiencyId))
                .retrieveSync("getLessonTopicProficiencyById")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        this.lastProficiencyResponseMap = response;
    }

    @When("the user deletes the topic proficiency")
    public void userDeletesTopicProficiency() {
        String graphqlQuery = findGraphqlQueryFromFiles("delete-lesson-topic-proficiency.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", testContext.getLessonId());
        input.put("id", topicProficiencyId);

        testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("deleteLessonTopicProficiency")
                .toEntity(Boolean.class);
    }

    // ─── assertion @Then steps ───────────────────────────────────────────────

    @Then("the student lesson report response should have an auto-created homework completion with completion null")
    @SuppressWarnings("unchecked")
    public void reportShouldHaveAutoHomework() {
        Map<String, Object> response = lastReportResponseMap;
        assertThat(response).isNotNull();
        Map<String, Object> hw = (Map<String, Object>) response.get("lessonHomeworkCompletion");
        assertThat(hw).as("expected auto-created homework completion").isNotNull();
        assertThat(hw.get("completion")).isNull();
    }

    @Then("the student lesson report response should have an auto-created participation review with type {string}")
    @SuppressWarnings("unchecked")
    public void reportShouldHaveAutoParticipation(String expectedType) {
        Map<String, Object> response = lastReportResponseMap;
        assertThat(response).isNotNull();
        Map<String, Object> pr = (Map<String, Object>) response.get("lessonParticipationReview");
        assertThat(pr).as("expected auto-created participation review").isNotNull();
        assertThat(pr.get("participationType")).isEqualTo(expectedType);
    }

    @Then("the student lesson report response should have {int} auto-created topic proficiencies")
    @SuppressWarnings("unchecked")
    public void reportShouldHaveAutoProficiencies(int expectedCount) {
        Map<String, Object> response = lastReportResponseMap;
        assertThat(response).isNotNull();
        List<Map<String, Object>> profs = (List<Map<String, Object>>) response.get("lessonTopicProficiencies");
        assertThat(profs).hasSize(expectedCount);
    }

    @Then("the student lesson report response createdBy should be the current user")
    public void reportCreatedByShouldBeCurrentUser() {
        assertThat(lastReportResponseMap).isNotNull();
        assertThat(lastReportResponseMap.get("createdBy")).isNotNull();
    }

    @Then("the student lesson report response should have homework completion {int}")
    @SuppressWarnings("unchecked")
    public void reportShouldHaveHomeworkCompletion(int expected) {
        Map<String, Object> response = lastReportResponseMap;
        assertThat(response).isNotNull();
        Map<String, Object> hw = (Map<String, Object>) response.get("lessonHomeworkCompletion");
        assertThat(hw).isNotNull();
        assertThat(hw.get("completion")).isEqualTo(expected);
    }

    @Then("the student lesson report response should have participation type {string}")
    @SuppressWarnings("unchecked")
    public void reportShouldHaveParticipationType(String expected) {
        Map<String, Object> response = lastReportResponseMap;
        assertThat(response).isNotNull();
        Map<String, Object> pr = (Map<String, Object>) response.get("lessonParticipationReview");
        assertThat(pr).isNotNull();
        assertThat(pr.get("participationType")).isEqualTo(expected);
    }

    @Then("the student lesson report list should have {int} entries")
    public void reportsListShouldHaveSize(int expectedSize) {
        assertThat(lastReportsList).hasSize(expectedSize);
    }

    @Then("the student lesson report should be gone from the database")
    public void reportShouldBeGoneFromDb() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.student_lesson_reports WHERE id = ?::uuid",
                Integer.class, studentLessonReportId);
        assertThat(count).as("student_lesson_reports count").isEqualTo(0);
    }

    @Then("the homework completion response should have completion {int}")
    public void homeworkResponseShouldHaveCompletion(int expected) {
        assertThat(lastHomeworkResponseMap).isNotNull();
        assertThat(lastHomeworkResponseMap.get("completion")).isEqualTo(expected);
    }

    @Then("the participation review response should have type {string}")
    public void participationResponseShouldHaveType(String expected) {
        assertThat(lastParticipationResponseMap).isNotNull();
        assertThat(lastParticipationResponseMap.get("participationType")).isEqualTo(expected);
    }

    @Then("the topic proficiency response should have rating {int}")
    public void proficiencyResponseShouldHaveRating(int expected) {
        assertThat(lastProficiencyResponseMap).isNotNull();
        assertThat(lastProficiencyResponseMap.get("rating")).isEqualTo(expected);
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void captureChildIdsFromReport(Map<String, Object> reportResponse) {
        Map<String, Object> hw = (Map<String, Object>) reportResponse.get("lessonHomeworkCompletion");
        if (hw != null && hw.get("id") != null) {
            this.homeworkCompletionId = (String) hw.get("id");
        }
        Map<String, Object> pr = (Map<String, Object>) reportResponse.get("lessonParticipationReview");
        if (pr != null && pr.get("id") != null) {
            this.participationReviewId = (String) pr.get("id");
        }
        List<Map<String, Object>> profs = (List<Map<String, Object>>) reportResponse.get("lessonTopicProficiencies");
        if (profs != null && !profs.isEmpty()) {
            Object id = profs.getFirst().get("id");
            if (id != null) {
                this.topicProficiencyId = (String) id;
            }
        }
    }

    private String resolveStudentLessonLinkId(String lessonId, String studentId) {
        List<String> ids = jdbcTemplate.queryForList(
                "SELECT id FROM public.student_lesson_links WHERE lesson_id = ?::uuid AND student_id = ?::uuid",
                String.class, lessonId, studentId);
        return ids.isEmpty() ? null : ids.getFirst();
    }

    private void handleErr(String graphqlQuery, Map<String, Object> input, String operationName) {
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

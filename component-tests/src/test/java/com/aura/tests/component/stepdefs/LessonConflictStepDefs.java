package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.OutletRoomResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
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
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonConflictStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @Given("the outlet room {string} exists in outlet {string}")
    public void theOutletRoomExistsInOutlet(String roomName, String outletName) {
        String query = findGraphqlQueryFromFiles("create-outlet-room.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("outletId", testContext.getOutletId());
        input.put("name", roomName);
        input.put("size", 20);
        input.put("type", "PHYSICAL");
        input.put("description", "Test room " + roomName);

        OutletRoomResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createOutletRoom")
                .toEntity(OutletRoomResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(roomName);

        testContext.recordOutletRoom(roomName, response.getId());
        log.info("Created outlet room '{}' with id={}", roomName, response.getId());
    }

    @Given("the lesson {string} is created in course {string} in room {string} from {string} to {string}")
    public void theLessonIsCreatedInCourseInRoomFromTo(
            String lessonName, String courseName, String roomName,
            String startIso, String endIso) {
        LessonResponse response = invokeCreateLesson(lessonName, roomName, startIso, endIso);
        testContext.recordLesson(lessonName, response.getId());
        testContext.setLessonId(response.getId());
        log.info("Created lesson '{}' with id={}", lessonName, response.getId());
    }

    @Given("the lesson {string} is created in course {string} with no room from {string} to {string}")
    public void theLessonIsCreatedInCourseWithNoRoomFromTo(
            String lessonName, String courseName, String startIso, String endIso) {
        LessonResponse response = invokeCreateLessonNoRoom(lessonName, startIso, endIso);
        testContext.recordLesson(lessonName, response.getId());
        testContext.setLessonId(response.getId());
        log.info("Created lesson '{}' (no room) with id={}", lessonName, response.getId());
    }

    @When("the user tries to create lesson {string} in course {string} in room {string} from {string} to {string}")
    public void userTriesToCreateLessonInCourseInRoomFromTo(
            String lessonName, String courseName, String roomName,
            String startIso, String endIso) {
        try {
            invokeCreateLesson(lessonName, roomName, startIso, endIso);
            testContext.setRequestFailed(false);
            testContext.setLastException(null);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            extractConflictExtensions(e);
            log.info("Lesson creation failed as expected: {}", e.getMessage());
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            log.info("Lesson creation failed with HTTP error: {}", e.getStatusCode());
        }
    }

    @When("the user creates lesson {string} in course {string} in room {string} from {string} to {string}")
    public void userCreatesLessonInCourseInRoomFromTo(
            String lessonName, String courseName, String roomName,
            String startIso, String endIso) {
        LessonResponse response = invokeCreateLesson(lessonName, roomName, startIso, endIso);
        testContext.recordLesson(lessonName, response.getId());
        testContext.setLessonId(response.getId());
        testContext.setRequestFailed(false);
        log.info("Created lesson '{}' with id={}", lessonName, response.getId());
    }

    @Then("the request should fail with a lesson conflict of type {string}")
    public void requestShouldFailWithLessonConflictOfType(String expectedType) {
        assertThat(testContext.isRequestFailed())
                .as("Expected the request to fail with a lesson conflict but it succeeded")
                .isTrue();
        assertThat(testContext.getLastErrorCode())
                .as("Expected error_code to be LESSON_CONFLICT")
                .isEqualTo("LESSON_CONFLICT");
        assertThat(testContext.getLastConflictType())
                .as("Expected conflict_type to be %s", expectedType)
                .isEqualTo(expectedType);
    }

    @Then("the conflict response should reference lesson {string}")
    public void conflictResponseShouldReferenceLesson(String lessonName) {
        String expectedId = testContext.getLessonIdByName(lessonName);
        assertThat(testContext.getLastConflictingLessonIds())
                .as("conflicting_lesson_ids should include lesson '%s' with id %s", lessonName, expectedId)
                .contains(expectedId);
    }

    @Then("the lesson {string} should be created successfully")
    public void theLessonShouldBeCreatedSuccessfully(String lessonName) {
        String id = testContext.getLessonIdByName(lessonName);
        assertThat(id)
                .as("Expected lesson '%s' to have been recorded in context", lessonName)
                .isNotBlank();
        assertThat(testContext.isRequestFailed())
                .as("Expected the request to succeed but it failed: %s", testContext.getLastErrorMessage())
                .isFalse();
    }

    // ── educator existence step ───────────────────────────────────────────────

    @Given("the educator {string} exists")
    @SuppressWarnings("unchecked")
    public void theEducatorExists(String educatorName) {
        String institutionId = testContext.getInstitutionId();

        // 1. Create an account for the educator
        String accountEmail = educatorName.toLowerCase().replace(" ", ".") + ".account@conflict-test.com";
        String createAccountQuery = findGraphqlQueryFromFiles("create-account.graphql");
        Map<String, Object> accountInput = Map.of(
                "institutionId", institutionId,
                "email", accountEmail,
                "firstName", educatorName.contains(" ") ? educatorName.split(" ")[0] : educatorName,
                "lastName", educatorName.contains(" ") ? educatorName.split(" ")[1] : "Educator",
                "contact", 90000001
        );
        Map<String, Object> accountResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(createAccountQuery)
                .variables(Map.of("input", accountInput))
                .retrieveSync("createAccount")
                .toEntity(Map.class);
        String accountId = (String) accountResponse.get("id");
        assertThat(accountId).isNotNull();

        // 2. Ensure a level exists
        String levelId = ensureLevelExists();

        // 3. Create the educator
        String educatorEmail = educatorName.toLowerCase().replace(" ", ".") + "@conflict-test.com";
        Map<String, Object> educatorDetails = new HashMap<>();
        educatorDetails.put("name", educatorName);
        educatorDetails.put("email", educatorEmail);
        educatorDetails.put("startDate", "2026-01-01");
        educatorDetails.put("dateOfBirth", "1985-01-01");
        educatorDetails.put("employmentType", "FULL_TIME");
        educatorDetails.put("courseIds", List.of());
        educatorDetails.put("subjectIds", List.of());
        educatorDetails.put("outletIds", List.of(testContext.getOutletId()));
        educatorDetails.put("levelIds", List.of(levelId));
        educatorDetails.put("accountId", accountId);
        educatorDetails.put("institutionId", institutionId);

        Map<String, Object> educatorsInput = Map.of(
                "institutionId", institutionId,
                "educators", List.of(educatorDetails)
        );

        String educatorQuery = findGraphqlQueryFromFiles("create-educators-in-account.graphql");
        List<?> educatorResponses = testContext.getGraphQLClient()
                .mutate().build()
                .document(educatorQuery)
                .variables(Map.of("input", educatorsInput))
                .retrieveSync("createEducatorsInAccount")
                .toEntityList(Map.class);

        assertThat(educatorResponses).isNotEmpty();
        @SuppressWarnings("unchecked")
        Map<String, Object> firstEducator = (Map<String, Object>) educatorResponses.getFirst();
        String educatorId = (String) firstEducator.get("id");
        assertThat(educatorId).isNotNull();

        testContext.recordEducator(educatorName, educatorId);
        testContext.setEducatorId(educatorId);
        log.info("Created educator '{}' with id={}", educatorName, educatorId);
    }

    // ── assign-educators steps ────────────────────────────────────────────────

    @Given("the educator {string} is assigned to lesson {string}")
    public void educatorIsAssignedToLesson(String educatorName, String lessonName) {
        invokeAssignEducators(lessonName, List.of(educatorName), false);
    }

    @When("the user tries to assign educator {string} to lesson {string}")
    public void userTriesToAssignEducator(String educatorName, String lessonName) {
        invokeAssignEducators(lessonName, List.of(educatorName), true);
    }

    @When("the user assigns educator {string} to lesson {string}")
    public void userAssignsEducator(String educatorName, String lessonName) {
        invokeAssignEducators(lessonName, List.of(educatorName), false);
    }

    @Then("the lesson {string} should have educator {string} assigned")
    @SuppressWarnings("unchecked")
    public void lessonShouldHaveEducator(String lessonName, String educatorName) {
        Map<String, Object> data = testContext.getLastResponseData();
        assertThat(data).as("Expected a response for lesson '%s'", lessonName).isNotNull();
        List<Map<String, Object>> educators = (List<Map<String, Object>>) data.get("educators");
        String expectedId = testContext.getEducatorIdByName(educatorName);
        assertThat(educators)
                .as("Expected lesson '%s' to have educator '%s' (id=%s)", lessonName, educatorName, expectedId)
                .extracting(e -> e.get("id"))
                .contains(expectedId);
    }

    private void invokeAssignEducators(String lessonName, List<String> educatorNames, boolean expectFailure) {
        String lessonId = testContext.getLessonIdByName(lessonName);
        assertThat(lessonId)
                .as("Lesson '%s' not found in TestContext — was it created in the Background?", lessonName)
                .isNotNull();

        List<String> educatorIds = educatorNames.stream()
                .map(testContext::getEducatorIdByName)
                .toList();

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", lessonId);
        input.put("educatorIds", educatorIds);

        String mutation = findGraphqlQueryFromFiles("assign-educators-to-lesson.graphql");
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) testContext.getGraphQLClient()
                    .mutate().build()
                    .document(mutation)
                    .variables(Map.of("input", input))
                    .retrieveSync("assignEducatorsToLesson")
                    .toEntity(Map.class);
            testContext.setLastResponseData(data);
            testContext.setRequestFailed(false);
            testContext.setLastException(null);
            log.info("assignEducatorsToLesson succeeded for lesson '{}'", lessonName);
        } catch (FieldAccessException e) {
            if (!expectFailure) {
                throw new AssertionError(
                        "Unexpected GraphQL error assigning educators to lesson '" + lessonName + "': " + e.getMessage(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            extractConflictExtensions(e);
            log.info("assignEducatorsToLesson failed as expected for lesson '{}': {}", lessonName, e.getMessage());
        } catch (HttpServerErrorException e) {
            if (!expectFailure) {
                throw new AssertionError(
                        "Unexpected HTTP error assigning educators to lesson '" + lessonName + "': " + e.getStatusCode(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            log.info("assignEducatorsToLesson failed with HTTP error for lesson '{}': {}", lessonName, e.getStatusCode());
        }
    }

    // ── student existence step ────────────────────────────────────────────────

    @Given("the student {string} exists")
    @SuppressWarnings("unchecked")
    public void theStudentExists(String studentName) {
        String institutionId = testContext.getInstitutionId();

        // 1. Create a parent account
        String parentEmail = studentName.toLowerCase().replace(" ", ".") + ".parent@conflict-test.com";
        String createAccountQuery = findGraphqlQueryFromFiles("create-account.graphql");
        Map<String, Object> accountInput = Map.of(
                "institutionId", institutionId,
                "email", parentEmail,
                "firstName", "Parent",
                "lastName", "Of" + studentName.replace(" ", ""),
                "contact", 90000000
        );
        Map<String, Object> accountResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(createAccountQuery)
                .variables(Map.of("input", accountInput))
                .retrieveSync("createAccount")
                .toEntity(Map.class);
        String accountId = (String) accountResponse.get("id");
        assertThat(accountId).isNotNull();

        // 2. Ensure level + school exist
        String levelId = ensureLevelExists();
        String schoolId = ensureSchoolExists();

        // 3. Create parent + student
        String studentEmail = studentName.toLowerCase().replace(" ", ".") + "@conflict-test.com";
        Map<String, Object> studentDetails = new HashMap<>();
        studentDetails.put("name", studentName);
        studentDetails.put("email", studentEmail);
        studentDetails.put("dateOfBirth", "2010-01-01");
        studentDetails.put("levelId", levelId);
        studentDetails.put("schoolId", schoolId);
        studentDetails.put("courseEnrollments", List.of());

        Map<String, Object> parentInput = Map.of(
                "institutionId", institutionId,
                "accountId", accountId,
                "relationship", "PARENT",
                "students", List.of(studentDetails)
        );

        String parentQuery = findGraphqlQueryFromFiles("create-parent-and-students-in-account.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(parentQuery)
                .variables(Map.of("input", parentInput))
                .retrieveSync("createParentAndStudentsInAccount")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        List<Map<String, Object>> students = (List<Map<String, Object>>) response.get("students");
        assertThat(students).isNotEmpty();
        String studentId = (String) students.getFirst().get("id");
        assertThat(studentId).isNotNull();

        testContext.recordStudent(studentName, studentId);
        testContext.setStudentId(studentId);
        log.info("Created student '{}' with id={}", studentName, studentId);
    }

    // ── assign-students steps ─────────────────────────────────────────────────

    @Given("the student {string} is assigned to lesson {string}")
    public void studentIsAssignedToLesson(String studentName, String lessonName) {
        invokeAssignStudents(lessonName, List.of(studentName), false);
    }

    @When("the user tries to assign student {string} to lesson {string}")
    public void userTriesToAssignStudent(String studentName, String lessonName) {
        invokeAssignStudents(lessonName, List.of(studentName), true);
    }

    @When("the user assigns student {string} to lesson {string}")
    public void userAssignsStudent(String studentName, String lessonName) {
        invokeAssignStudents(lessonName, List.of(studentName), false);
    }

    @Then("the lesson {string} should have student {string} assigned")
    @SuppressWarnings("unchecked")
    public void lessonShouldHaveStudent(String lessonName, String studentName) {
        Map<String, Object> data = testContext.getLastResponseData();
        assertThat(data).as("Expected a response for lesson '%s'", lessonName).isNotNull();
        List<Map<String, Object>> students = (List<Map<String, Object>>) data.get("students");
        String expectedId = testContext.getStudentIdByName(studentName);
        assertThat(students)
                .as("Expected lesson '%s' to have student '%s' (id=%s)", lessonName, studentName, expectedId)
                .extracting(s -> s.get("id"))
                .contains(expectedId);
    }

    private void invokeAssignStudents(String lessonName, List<String> studentNames, boolean expectFailure) {
        String lessonId = testContext.getLessonIdByName(lessonName);
        assertThat(lessonId)
                .as("Lesson '%s' not found in TestContext — was it created in the Background?", lessonName)
                .isNotNull();

        List<String> studentIds = studentNames.stream()
                .map(testContext::getStudentIdByName)
                .toList();

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", lessonId);
        input.put("studentIds", studentIds);

        String mutation = findGraphqlQueryFromFiles("assign-students-to-lesson.graphql");
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) testContext.getGraphQLClient()
                    .mutate().build()
                    .document(mutation)
                    .variables(Map.of("input", input))
                    .retrieveSync("assignStudentsToLesson")
                    .toEntity(Map.class);
            testContext.setLastResponseData(data);
            testContext.setRequestFailed(false);
            testContext.setLastException(null);
            log.info("assignStudentsToLesson succeeded for lesson '{}'", lessonName);
        } catch (FieldAccessException e) {
            if (!expectFailure) {
                throw new AssertionError(
                        "Unexpected GraphQL error assigning students to lesson '" + lessonName + "': " + e.getMessage(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            extractConflictExtensions(e);
            log.info("assignStudentsToLesson failed as expected for lesson '{}': {}", lessonName, e.getMessage());
        } catch (HttpServerErrorException e) {
            if (!expectFailure) {
                throw new AssertionError(
                        "Unexpected HTTP error assigning students to lesson '" + lessonName + "': " + e.getStatusCode(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            log.info("assignStudentsToLesson failed with HTTP error for lesson '{}': {}", lessonName, e.getStatusCode());
        }
    }

    private LessonResponse invokeCreateLesson(String lessonName, String roomName,
                                               String startIso, String endIso) {
        String query = findGraphqlQueryFromFiles("create-lesson.graphql");

        String outletRoomId = testContext.getOutletRoomIdByName(roomName);
        assertThat(outletRoomId)
                .as("Outlet room '%s' not found in TestContext — was it created in the Background?", roomName)
                .isNotNull();

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", lessonName);
        input.put("description", lessonName + " description");
        input.put("lessonStartTimestamptz", startIso);
        input.put("lessonEndTimestamptz", endIso);
        input.put("courseId", testContext.getCourseId());
        input.put("outletRoomId", outletRoomId);
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());
        input.put("levelIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("subjectIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("topicIds", List.of());

        Map<String, Object> defaultGroupPlan = new HashMap<>();
        defaultGroupPlan.put("plan", lessonName + " plan");
        defaultGroupPlan.put("topicIds", List.of());
        defaultGroupPlan.put("materialIds", List.of());
        defaultGroupPlan.put("lessonObjectives", List.of());

        Map<String, Object> defaultGroup = new HashMap<>();
        defaultGroup.put("name", lessonName + " group");
        defaultGroup.put("studentIds", List.of());
        defaultGroup.put("plan", defaultGroupPlan);
        input.put("lessonGroups", List.of(defaultGroup));

        LessonResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonInCourse")
                .toEntity(LessonResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(lessonName);
        return response;
    }

    // ── change-outlet-room steps ──────────────────────────────────────────────

    @When("the user tries to change the outlet room of lesson {string} to {string}")
    public void userTriesToChangeRoom(String lessonName, String roomName) {
        invokeChangeRoom(lessonName, roomName, true);
    }

    @When("the user changes the outlet room of lesson {string} to {string}")
    public void userChangesRoom(String lessonName, String roomName) {
        invokeChangeRoom(lessonName, roomName, false);
    }

    @When("the user clears the outlet room of lesson {string}")
    public void userClearsRoom(String lessonName) {
        invokeChangeRoom(lessonName, null, false);
    }

    @Then("the lesson {string} should be in room {string}")
    public void lessonShouldBeInRoom(String lessonName, String roomName) {
        @SuppressWarnings("unchecked")
        Map<String, Object> outletRoom = (Map<String, Object>) testContext.getLastResponseData().get("outletRoom");
        assertThat(outletRoom)
                .as("Expected outletRoom to be non-null for lesson '%s'", lessonName)
                .isNotNull();
        assertThat(outletRoom.get("name"))
                .as("Expected room name to be '%s'", roomName)
                .isEqualTo(roomName);
    }

    @Then("the lesson {string} should have no outlet room")
    public void lessonShouldHaveNoRoom(String lessonName) {
        assertThat(testContext.getLastResponseData().get("outletRoom"))
                .as("Expected outletRoom to be null for lesson '%s'", lessonName)
                .isNull();
    }

    private void invokeChangeRoom(String lessonName, @Nullable String roomName, boolean expectFailure) {
        String lessonId = testContext.getLessonIdByName(lessonName);
        assertThat(lessonId)
                .as("Lesson '%s' not found in TestContext — was it created in the Background?", lessonName)
                .isNotNull();

        String roomId = roomName == null ? null : testContext.getOutletRoomIdByName(roomName);
        if (roomName != null) {
            assertThat(roomId)
                    .as("Outlet room '%s' not found in TestContext — was it created in the Background?", roomName)
                    .isNotNull();
        }

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("lessonId", lessonId);
        input.put("outletRoomId", roomId);

        String mutation = findGraphqlQueryFromFiles("change-lesson-outlet-room.graphql");
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) testContext.getGraphQLClient()
                    .mutate().build()
                    .document(mutation)
                    .variables(Map.of("input", input))
                    .retrieveSync("changeLessonOutletRoom")
                    .toEntity(Map.class);
            testContext.setLastResponseData(data);
            testContext.setRequestFailed(false);
            testContext.setLastException(null);
            log.info("changeLessonOutletRoom succeeded for lesson '{}'", lessonName);
        } catch (FieldAccessException e) {
            if (!expectFailure) {
                throw new AssertionError("Unexpected GraphQL error changing room for lesson '" + lessonName + "': " + e.getMessage(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            extractConflictExtensions(e);
            log.info("changeLessonOutletRoom failed as expected for lesson '{}': {}", lessonName, e.getMessage());
        } catch (HttpServerErrorException e) {
            if (!expectFailure) {
                throw new AssertionError("Unexpected HTTP error changing room for lesson '" + lessonName + "': " + e.getStatusCode(), e);
            }
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            log.info("changeLessonOutletRoom failed with HTTP error for lesson '{}': {}", lessonName, e.getStatusCode());
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private LessonResponse invokeCreateLessonNoRoom(String lessonName, String startIso, String endIso) {
        String query = findGraphqlQueryFromFiles("create-lesson.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", lessonName);
        input.put("description", lessonName + " description");
        input.put("lessonStartTimestamptz", startIso);
        input.put("lessonEndTimestamptz", endIso);
        input.put("courseId", testContext.getCourseId());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());
        input.put("levelIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("subjectIds", List.of("00000000-0000-0000-0000-000000000001"));
        input.put("topicIds", List.of());

        Map<String, Object> defaultGroupPlan = new HashMap<>();
        defaultGroupPlan.put("plan", lessonName + " plan");
        defaultGroupPlan.put("topicIds", List.of());
        defaultGroupPlan.put("materialIds", List.of());
        defaultGroupPlan.put("lessonObjectives", List.of());

        Map<String, Object> defaultGroup = new HashMap<>();
        defaultGroup.put("name", lessonName + " group");
        defaultGroup.put("studentIds", List.of());
        defaultGroup.put("plan", defaultGroupPlan);
        input.put("lessonGroups", List.of(defaultGroup));

        LessonResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLessonInCourse")
                .toEntity(LessonResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(lessonName);
        return response;
    }

    private void extractConflictExtensions(FieldAccessException ex) {
        var errors = ex.getResponse().getErrors();
        if (errors.isEmpty()) {
            return;
        }
        var ext = errors.getFirst().getExtensions();
        if (ext == null) {
            return;
        }
        if (ext.containsKey("error_code")) {
            testContext.setLastErrorCode(ext.get("error_code").toString());
            testContext.setLastErrorMessage(ext.get("error_code").toString());
        }
        if (ext.containsKey("conflict_type")) {
            testContext.setLastConflictType(ext.get("conflict_type").toString());
        }
        Object idsObj = ext.get("conflicting_lesson_ids");
        if (idsObj instanceof List<?> rawList) {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) rawList;
            testContext.setLastConflictingLessonIds(ids);
        }
        if (ext.containsKey("subject_id") && ext.get("subject_id") != null) {
            testContext.setLastSubjectId(ext.get("subject_id").toString());
        }
    }

    private String ensureLevelExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM levels WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.getFirst();
        }
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO levels (id, version, tenant_id, name, category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test Level', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }

    private String ensureSchoolExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM schools WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.getFirst();
        }
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO schools (id, version, tenant_id, name, school_category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test School', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }
}

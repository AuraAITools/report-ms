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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LessonConflictStepDefs {

    private final TestContext testContext;

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
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var ext = errors.getFirst().getExtensions();
                if (ext != null && ext.containsKey("error_code")) {
                    testContext.setLastErrorMessage(ext.get("error_code").toString());
                }
            }
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

    @Then("the request should fail with a room conflict error")
    public void theRequestShouldFailWithARoomConflictError() {
        assertThat(testContext.isRequestFailed())
                .as("Expected the request to fail with a room conflict but it succeeded")
                .isTrue();
        assertThat(testContext.getLastErrorMessage())
                .as("Expected INTERNAL_ERROR error code for room conflict")
                .isEqualTo("INTERNAL_ERROR");
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
}

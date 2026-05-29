package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class CourseStepDefs {

    private final TestContext testContext;
    private final List<String> createdCourseIds = new ArrayList<>();
    private Map<String, Object> lastCourseResponse;
    private List<Map<String, Object>> lastCoursesResponse;

    @When("user creates a course with details:")
    public void userCreatesACourse(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = new java.util.HashMap<>(Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", data.get("name"),
                "maxSize", Integer.parseInt(data.get("maxSize")),
                "lessonFrequency", data.get("lessonFrequency"),
                "priceRecord", Map.of(
                        "price", Double.parseDouble(data.get("price")),
                        "frequency", data.get("priceFrequency")
                ),
                "courseStartTimestamptz", data.get("startDate"),
                "courseEndTimestamptz", data.get("endDate"),
                "outletId", testContext.getOutletId()
        ));
        input.put("subjectIds", List.of());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());

        CourseResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createCourse")
                .toEntity(CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(data.get("name"));
        assertThat(response.getCreatedBy()).isNotNull();

        testContext.setCourseId(response.getId());
    }

    @When("user creates a course with associations and details:")
    public void userCreatesACourseWithAssociations(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = new java.util.HashMap<>(Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", data.get("name"),
                "maxSize", Integer.parseInt(data.get("maxSize")),
                "lessonFrequency", data.get("lessonFrequency"),
                "priceRecord", Map.of(
                        "price", Double.parseDouble(data.get("price")),
                        "frequency", data.get("priceFrequency")
                ),
                "courseStartTimestamptz", data.get("startDate"),
                "courseEndTimestamptz", data.get("endDate"),
                "outletId", testContext.getOutletId()
        ));

        input.put("subjectIds", testContext.getSubjectId() != null ? List.of(testContext.getSubjectId()) : List.of());
        input.put("educatorIds", testContext.getEducatorId() != null ? List.of(testContext.getEducatorId()) : List.of());
        input.put("studentIds", testContext.getStudentId() != null ? List.of(testContext.getStudentId()) : List.of());

        CourseResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createCourse")
                .toEntity(CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(data.get("name"));
        assertThat(response.getCreatedBy()).isNotNull();

        testContext.setCourseId(response.getId());
    }

    @When("user tries to create a course with details:")
    public void userTriesToCreateACourse(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = new java.util.HashMap<>(Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", data.get("name"),
                "maxSize", Integer.parseInt(data.get("maxSize")),
                "lessonFrequency", data.get("lessonFrequency"),
                "priceRecord", Map.of(
                        "price", Double.parseDouble(data.get("price")),
                        "frequency", data.get("priceFrequency")
                ),
                "courseStartTimestamptz", data.get("startDate"),
                "courseEndTimestamptz", data.get("endDate"),
                "outletId", testContext.getOutletId()
        ));
        input.put("subjectIds", List.of());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());

        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createCourse")
                    .toEntity(CourseResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @And("a course exists with name {string}")
    public void aCourseExistsWithName(String name) {
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = new java.util.HashMap<>(Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "maxSize", 10,
                "lessonFrequency", "WEEKLY",
                "priceRecord", Map.of(
                        "price", 100.0,
                        "frequency", "MONTHLY"
                ),
                "courseStartTimestamptz", "2026-01-01T09:00:00Z",
                "courseEndTimestamptz", "2026-12-31T09:00:00Z",
                "outletId", testContext.getOutletId()
        ));
        input.put("subjectIds", List.of());
        input.put("educatorIds", List.of());
        input.put("studentIds", List.of());

        CourseResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createCourse")
                .toEntity(CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();

        testContext.setCourseId(response.getId());
        createdCourseIds.add(response.getId());
    }

    @Then("the course should be created successfully")
    public void theCourseShouldBeCreatedSuccessfully() {
        assertThat(testContext.getCourseId()).isNotNull();
    }

    @When("user queries course by id")
    public void userQueriesCourseById() {
        String query = findGraphqlQueryFromFiles("get-course-by-id.graphql");
        try {
            lastCourseResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("courseId", testContext.getCourseId()))
                    .retrieveSync("getCourseById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        }
    }

    @When("user queries course with non-existent id")
    public void userQueriesCourseWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-course-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("courseId", UUID.randomUUID().toString()))
                    .retrieveSync("getCourseById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        }
    }

    @Then("the course should be returned with id and name {string}")
    public void theCourseShouldBeReturnedWithIdAndName(String expectedName) {
        assertThat(lastCourseResponse).isNotNull();
        assertThat(lastCourseResponse.get("id")).isEqualTo(testContext.getCourseId());
        assertThat(lastCourseResponse.get("name")).isEqualTo(expectedName);
    }

    @And("the course nested associations should be present")
    public void theCourseNestedAssociationsShouldBePresent() {
        assertThat(lastCourseResponse).containsKey("lessons");
        assertThat(lastCourseResponse).containsKey("educators");
        assertThat(lastCourseResponse).containsKey("students");
        assertThat(lastCourseResponse).containsKey("subjects");
        assertThat(lastCourseResponse).containsKey("level");
        assertThat((List<?>) lastCourseResponse.get("lessons")).isNotNull();
        assertThat((List<?>) lastCourseResponse.get("educators")).isNotNull();
        assertThat((List<?>) lastCourseResponse.get("students")).isNotNull();
        assertThat((List<?>) lastCourseResponse.get("subjects")).isNotNull();
    }

    @When("user queries all courses in the outlet")
    public void userQueriesAllCoursesInTheOutlet() {
        String query = findGraphqlQueryFromFiles("get-all-courses-in-outlet.graphql");
        lastCoursesResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("outletId", testContext.getOutletId()))
                .retrieveSync("getAllCoursesInOutlet")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the response should contain {int} courses")
    public void theResponseShouldContainCourses(int expectedCount) {
        assertThat(lastCoursesResponse).hasSize(expectedCount);
    }

    @And("the courses should include names {string}")
    public void theCoursesShouldIncludeNames(String commaSeparatedNames) {
        List<String> expectedNames = List.of(commaSeparatedNames.split(","));
        assertThat(lastCoursesResponse).extracting(m -> m.get("name"))
                .containsExactlyInAnyOrderElementsOf(expectedNames);
    }
}

package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class CourseStepDefs {

    private final TestContext testContext;

    @When("user creates a course with details:")
    public void userCreatesACourse(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = Map.of(
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
        );

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

        Map<String, Object> input = Map.of(
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
        );

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

    @Then("the course should be created successfully")
    public void theCourseShouldBeCreatedSuccessfully() {
        assertThat(testContext.getCourseId()).isNotNull();
    }
}

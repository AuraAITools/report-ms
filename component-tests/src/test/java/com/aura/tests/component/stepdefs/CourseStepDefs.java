package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreatePriceRecordInput;
import com.aura.app.codegen.types.LessonFrequency;
import com.aura.app.codegen.types.PriceFrequency;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

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
        assertThat(response.getCreatedBy()).isEqualTo(TestConstants.AURA_ADMIN_USER_ID);

        testContext.setCourseId(response.getId());
    }

    @Then("the course should be created successfully")
    public void theCourseShouldBeCreatedSuccessfully() {
        assertThat(testContext.getCourseId()).isNotNull();
    }
}

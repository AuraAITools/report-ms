package com.aura.tests.component.stepdefs;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RequiredArgsConstructor
public class OutletCalendarStepDefs {

    private final TestContext testContext;

    @When("the user queries lessons in outlet {string} from {string} to {string}")
    public void userQueriesLessonsInOutletFromTo(String outletName, String startIso, String endIso) {
        invokeQuery(outletName, startIso, endIso, false);
    }

    @When("the user tries to query lessons in outlet {string} from {string} to {string}")
    public void userTriesToQueryLessonsInOutletFromTo(String outletName, String startIso, String endIso) {
        invokeQuery(outletName, startIso, endIso, true);
    }

    @Then("the calendar response should contain exactly the lessons {string}")
    public void calendarResponseShouldContainExactly(String commaSeparatedNames) {
        List<String> expectedIds = java.util.Arrays.stream(commaSeparatedNames.split(","))
                .map(String::trim)
                .map(testContext::getLessonIdByName)
                .toList();
        assertThat(testContext.getCalendarLessonIds())
                .as("Calendar should contain exactly the named lessons in any order")
                .containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Then("the calendar response should contain no lessons")
    public void calendarResponseShouldBeEmpty() {
        assertThat(testContext.getCalendarLessonIds())
                .as("Calendar should be empty")
                .isEmpty();
    }

    @Then("the request should fail with an {string} error code")
    public void requestShouldFailWithErrorCode(String expectedErrorCode) {
        assertThat(testContext.isRequestFailed())
                .as("Expected the request to fail")
                .isTrue();
        assertThat(testContext.getLastErrorCode())
                .as("Expected error_code to be %s", expectedErrorCode)
                .isEqualTo(expectedErrorCode);
    }

    private void invokeQuery(String outletName, String startIso, String endIso, boolean expectFailure) {
        String outletId = testContext.getOutletIdByName(outletName);
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("outletId", outletId);
        input.put("startTimestamp", Instant.parse(startIso).toEpochMilli());
        input.put("endTimestamp", Instant.parse(endIso).toEpochMilli());
        String query = findGraphqlQueryFromFiles("get-lessons-in-outlet-by-date-range.graphql");

        try {
            List<LessonResponse> response = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getLessonsInOutletByDateRange")
                    .toEntityList(LessonResponse.class);
            testContext.setCalendarLessonIds(response.stream().map(LessonResponse::getId).toList());
            testContext.setRequestFailed(false);
            testContext.setLastException(null);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            extractErrorExtensions(e);
            log.info("Calendar query failed as expected: {}", e.getMessage());
            if (!expectFailure) {
                throw e;
            }
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastException(e);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            log.info("Calendar query failed with HTTP error: {}", e.getStatusCode());
            if (!expectFailure) {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void extractErrorExtensions(FieldAccessException e) {
        try {
            var errors = e.getResponse().getErrors();
            if (errors.isEmpty()) return;
            Map<String, Object> extensions = errors.get(0).getExtensions();
            if (extensions == null) return;
            Object errorCode = extensions.get("error_code");
            if (errorCode != null) {
                testContext.setLastErrorCode(errorCode.toString());
            }
        } catch (Exception ignored) {
            // best-effort extraction
        }
    }
}

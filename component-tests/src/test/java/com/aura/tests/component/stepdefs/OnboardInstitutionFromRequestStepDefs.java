package com.aura.tests.component.stepdefs;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class OnboardInstitutionFromRequestStepDefs {

    private static final String GRAPHQL_FILE = "onboard-institution-from-request.graphql";

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("user onboards the institution from the pending request")
    @Given("user onboards the institution from the pending request")
    public void userOnboardsFromPendingRequest() {
        invokeOnboard(testContext.getInstitutionCreationRequestId());
    }

    @When("user tries to onboard the institution from the pending request again")
    public void userTriesToOnboardAgain() {
        tryInvokeOnboard(testContext.getInstitutionCreationRequestId());
    }

    @When("user tries to onboard the institution from creation request id {string}")
    public void userTriesToOnboardWithId(String requestId) {
        tryInvokeOnboard(requestId);
    }

    @Then("the institution should be created with name {string} and email {string}")
    public void institutionCreated(String expectedName, String expectedEmail) {
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
                "SELECT name, email FROM institutions WHERE name = ?", expectedName);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("email")).isEqualTo(expectedEmail);
    }

    @Then("an admin account should be created for email {string}")
    public void adminAccountCreated(String expectedEmail) {
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
                "SELECT email FROM accounts WHERE email = ?", expectedEmail);
        assertThat(results).hasSize(1);
    }

    private void invokeOnboard(String requestId) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) testContext.getGraphQLClient()
                .document(findGraphqlQueryFromFiles(GRAPHQL_FILE))
                .variables(Map.of("institutionCreationRequestId", requestId))
                .retrieveSync("onboardInstitutionFromRequest")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        testContext.setInstitutionId((String) response.get("id"));
    }

    private void tryInvokeOnboard(String requestId) {
        try {
            testContext.getGraphQLClient()
                    .document(findGraphqlQueryFromFiles(GRAPHQL_FILE))
                    .variables(Map.of("institutionCreationRequestId", requestId))
                    .retrieveSync("onboardInstitutionFromRequest")
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
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
}

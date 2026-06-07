package com.aura.tests.component.stepdefs;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class InstitutionCreationRequestStepDefs {

    private static final String GRAPHQL_FILE = "request-institution-creation.graphql";

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("prospect requests an institution to be created with details:")
    public void prospectRequests(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().get(0);
        Map<String, Object> input = buildInput(row);
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) unauthenticatedClient()
                .document(findGraphqlQueryFromFiles(GRAPHQL_FILE))
                .variables(Map.of("input", input))
                .retrieveSync("requestInstitutionCreation")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response.get("institutionName")).isEqualTo(row.get("institutionName"));
        assertThat(response.get("institutionEmail")).isEqualTo(row.get("institutionEmail"));
        assertThat(response.get("state")).isEqualTo("REQUESTED");
        testContext.setLastErrorMessage(null);
    }

    @When("prospect tries to request an institution to be created with details:")
    public void prospectTriesToRequest(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().get(0);
        Map<String, Object> input = buildInput(row);
        try {
            unauthenticatedClient()
                    .document(findGraphqlQueryFromFiles(GRAPHQL_FILE))
                    .variables(Map.of("input", input))
                    .retrieveSync("requestInstitutionCreation")
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

    @Given("a pending institution creation request exists with details:")
    public void aPendingRequestExists(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().get(0);
        Map<String, Object> input = buildInput(row);
        // Seed by hitting the same public mutation — no auth required.
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) unauthenticatedClient()
                .document(findGraphqlQueryFromFiles(GRAPHQL_FILE))
                .variables(Map.of("input", input))
                .retrieveSync("requestInstitutionCreation")
                .toEntity(Map.class);
        testContext.setInstitutionCreationRequestId((String) response.get("id"));
    }

    @Then("the institution creation request should be saved in state {string}")
    public void requestShouldBeSavedInState(String expectedState) {
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
                "SELECT * FROM institution_creation_requests");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("state")).isEqualTo(expectedState);
    }

    private Map<String, Object> buildInput(Map<String, String> row) {
        Map<String, Object> createInstitutionInput = Map.of(
                "name", row.get("institutionName"),
                "email", row.get("institutionEmail"),
                "contactNumber", row.get("institutionContactNumber"),
                "address", row.get("institutionAddress"),
                "uen", row.get("institutionUen")
        );
        return Map.of(
                "createInstitutionInput", createInstitutionInput,
                "requesterFirstName", row.get("requesterFirstName"),
                "requesterLastName", row.get("requesterLastName"),
                "requesterEmail", row.get("requesterEmail"),
                "requesterContactNumber", row.get("requesterContactNumber")
        );
    }

    /**
     * Returns a fresh client with NO Authorization header so we exercise the public
     * mutation path. testContext.getGraphQLClient() may have been mutated by
     * earlier @Given steps (e.g. user has "aura-admin" role) to add a bearer token.
     */
    private HttpSyncGraphQlClient unauthenticatedClient() {
        return testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.remove("Authorization"))
                .build();
    }
}

package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import com.aura.app.codegen.types.InstitutionResponse;
import java.util.List;
import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class QueryStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastInstitutionResponse;
    private List<InstitutionResponse> lastUserInstitutionsResponse;

    @When("user queries their institution")
    public void userQueriesTheirInstitution() {
        String query = findGraphqlQueryFromFiles("get-institution.graphql");

        try {
            lastInstitutionResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId()))
                    .retrieveSync("getInstitution")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        }
    }

    @Then("the institution should be returned with correct details")
    public void theInstitutionShouldBeReturnedWithCorrectDetails() {
        assertThat(lastInstitutionResponse).isNotNull();
        assertThat(lastInstitutionResponse).containsKey("id");
        assertThat(lastInstitutionResponse).containsKey("name");
        assertThat(lastInstitutionResponse).containsKey("email");
        assertThat(lastInstitutionResponse.get("id")).isEqualTo(testContext.getInstitutionId());
    }

    @Then("the request should fail with error code {string}")
    public void theRequestShouldFailWithErrorCode(String errorCode) {
        assertThat(testContext.isRequestFailed()).isTrue();
        assertThat(testContext.getLastErrorMessage()).isEqualTo(errorCode);
    }

    @When("user queries their institutions")
    public void userQueriesTheirInstitutions() {
        String query = findGraphqlQueryFromFiles("get-user-institutions.graphql");

        lastUserInstitutionsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getUserInstitutions")
                .toEntityList(InstitutionResponse.class);
    }

    @Then("the user's institutions should contain the onboarded institution")
    public void theUsersInstitutionsShouldContainTheOnboardedInstitution() {
        assertThat(lastUserInstitutionsResponse).isNotNull().isNotEmpty();
        assertThat(lastUserInstitutionsResponse)
                .extracting(InstitutionResponse::getId)
                .contains(testContext.getInstitutionId());
    }

    @When("user queries levels by page")
    public void userQueriesLevelsByPage() {
        String query = findGraphqlQueryFromFiles("get-levels-by-page.graphql");

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                .retrieveSync("getLevelByPage")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        assertThat(response).containsKey("edges");
        testContext.setLastQuerySucceeded(true);
    }

    @Then("the levels query should respond successfully")
    public void theLevelsQueryShouldRespondSuccessfully() {
        assertThat(testContext.isLastQuerySucceeded()).isTrue();
    }

    @When("user queries outlets by page")
    public void userQueriesOutletsByPage() {
        String query = findGraphqlQueryFromFiles("get-outlets-by-page.graphql");

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                .retrieveSync("getOutletsByPage")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        List<?> edges = (List<?>) response.get("edges");
        assertThat(edges).isNotNull().isNotEmpty();
        testContext.setLastQuerySucceeded(true);
    }

    @Then("at least one outlet should be returned")
    public void atLeastOneOutletShouldBeReturned() {
        assertThat(testContext.isLastQuerySucceeded()).isTrue();
    }

    @When("user queries institution with non-existent id")
    public void userQueriesInstitutionWithNonExistentId() {
        String query = findGraphqlQueryFromFiles("get-institution.graphql");

        try {
            lastInstitutionResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", "00000000-0000-0000-0000-000000000000"))
                    .retrieveSync("getInstitution")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        }
    }

    @When("user tries to query outlets by page")
    public void userTriesToQueryOutletsByPage() {
        String query = findGraphqlQueryFromFiles("get-outlets-by-page.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                    .retrieveSync("getOutletsByPage")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
            testContext.setLastQuerySucceeded(true);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user tries to query levels by page")
    public void userTriesToQueryLevelsByPage() {
        String query = findGraphqlQueryFromFiles("get-levels-by-page.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId(), "first", 10))
                    .retrieveSync("getLevelByPage")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
            testContext.setLastQuerySucceeded(true);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
}

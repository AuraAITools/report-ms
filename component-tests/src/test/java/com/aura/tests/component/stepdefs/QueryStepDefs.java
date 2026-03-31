package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class QueryStepDefs {

    private final TestContext testContext;

    @When("user queries institutions by page")
    public void userQueriesInstitutionsByPage() {
        String query = findGraphqlQueryFromFiles("get-institutions-by-page.graphql");

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("first", 10))
                .retrieveSync("getInstitutionByPage")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        List<?> edges = (List<?>) response.get("edges");
        assertThat(edges).isNotNull().isNotEmpty();
        testContext.setLastQuerySucceeded(true);
    }

    @Then("at least one institution should be returned")
    public void atLeastOneInstitutionShouldBeReturned() {
        assertThat(testContext.isLastQuerySucceeded()).isTrue();
    }

    @When("user queries levels by page")
    public void userQueriesLevelsByPage() {
        String query = findGraphqlQueryFromFiles("get-levels-by-page.graphql");

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("first", 10))
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
                .variables(Map.of("first", 10))
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
}

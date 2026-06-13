package com.aura.tests.component.stepdefs;

import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;

/**
 * Step definitions covering the end-to-end resolution of {@code AccountMinimalDetail}
 * on the {@code createdBy} / {@code updatedBy} audit fields of an {@code Auditable}
 * GraphQL response.
 *
 * <p>The scenario fetches outlets in the current institution, selects the deferred
 * fields ({@code accountId}, {@code name}, {@code url}) of {@code createdBy}, and
 * asserts that the {@link com.aura.app.controllers.graphql.v1.accounts.AccountMinimalDetailGqlController}
 * {@code @BatchMapping} resolvers populate them correctly.
 */
@Slf4j
@RequiredArgsConstructor
public class AccountMinimalDetailStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastCreatedBy;

    @When("user fetches outlets in the institution selecting createdBy fields")
    @SuppressWarnings("unchecked")
    public void userFetchesOutletsSelectingCreatedByFields() {
        String query = findGraphqlQueryFromFiles("get-outlets-in-institution-with-minimal-audit.graphql");
        List<Map<String, Object>> outlets = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getOutletsInInstitution")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();

        Assertions.assertThat(outlets)
                .as("expected at least one outlet in the institution")
                .isNotEmpty();
        lastCreatedBy = (Map<String, Object>) outlets.getFirst().get("createdBy");
        Assertions.assertThat(lastCreatedBy)
                .as("createdBy must be present on every Auditable response")
                .isNotNull();
    }

    @Then("the response createdBy.userId should equal the aura-admin user id")
    public void responseCreatedByUserIdShouldEqualAuraAdmin() {
        Assertions.assertThat(lastCreatedBy.get("userId"))
                .isEqualTo(TestConstants.AURA_ADMIN_USER_ID);
    }

    @Then("the response createdBy.accountId should be non-null")
    public void responseCreatedByAccountIdShouldBeNonNull() {
        Assertions.assertThat(lastCreatedBy.get("accountId"))
                .as("createdBy.accountId should be resolved via AccountMinimalDetailGqlController")
                .isNotNull();
    }

    @Then("the response createdBy.name should match {string}")
    public void responseCreatedByNameShouldMatch(String regex) {
        Object name = lastCreatedBy.get("name");
        Assertions.assertThat(name)
                .as("createdBy.name should be resolved via AccountMinimalDetailGqlController")
                .isNotNull();
        Assertions.assertThat(name.toString())
                .as("createdBy.name should match pattern %s", regex)
                .matches(regex);
    }

    @Then("the response createdBy.url should be null")
    public void responseCreatedByUrlShouldBeNull() {
        Assertions.assertThat(lastCreatedBy.get("url"))
                .as("createdBy.url should be null when the creating account has no profile image")
                .isNull();
    }

    @Then("the response createdBy.url should be empty")
    public void responseCreatedByUrlShouldBeEmpty() {
        Assertions.assertThat(lastCreatedBy.get("url"))
                .as("createdBy.url is an empty string for the system placeholder")
                .isEqualTo("");
    }
}

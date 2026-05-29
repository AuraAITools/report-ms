package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LevelStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastCreateLevelResponse;

    @When("user creates a level in the institution with name {string} and category {string}")
    @SuppressWarnings("unchecked")
    public void userCreatesLevelInInstitution(String name, String category) {
        String query = findGraphqlQueryFromFiles("create-level-in-institution.graphql");

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "category", category
        );

        lastCreateLevelResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLevelInInstitution")
                .toEntity(Map.class);

        assertThat(lastCreateLevelResponse).isNotNull();
        assertThat(lastCreateLevelResponse.get("id")).isNotNull();
    }

    @Then("the level should be created successfully with name {string} and category {string}")
    public void levelShouldBeCreatedWithDetails(String name, String category) {
        assertThat(lastCreateLevelResponse.get("name")).isEqualTo(name);
        assertThat(lastCreateLevelResponse.get("category")).isEqualTo(category);
    }
}

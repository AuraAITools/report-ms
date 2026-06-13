package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class LevelManagementStepDefs {

    private final TestContext testContext;

    private final Map<String, String> levelIdsByName = new HashMap<>();
    private String lastLevelId;
    private Map<String, Object> lastLevelResponse;
    private List<Map<String, Object>> lastLevelList;
    private Map<String, Object> lastLevelDeleteImpact;

    @Given("a level exists with name {string} and category {string}")
    @SuppressWarnings("unchecked")
    public void aLevelExistsWithNameAndCategory(String name, String category) {
        String query = findGraphqlQueryFromFiles("create-level-in-institution.graphql");
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "category", category);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createLevelInInstitution")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        String id = response.get("id").toString();
        levelIdsByName.put(name, id);
        lastLevelId = id;
    }

    @When("user lists levels in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsLevelsInInstitution() {
        String query = findGraphqlQueryFromFiles("get-levels-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getLevelsInInstitution")
                .toEntityList(Map.class);
        lastLevelList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastLevelList).isNotNull();
    }

    @When("user retrieves the level by its id")
    @SuppressWarnings("unchecked")
    public void userRetrievesLevelById() {
        assertThat(lastLevelId).as("a level must exist before this step").isNotNull();
        String query = findGraphqlQueryFromFiles("get-level-by-id-in-institution.graphql");
        lastLevelResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "levelId", lastLevelId))
                .retrieveSync("getLevelByIdInInstitution")
                .toEntity(Map.class);
        assertThat(lastLevelResponse).isNotNull();
    }

    @When("user previews delete impact for the level")
    @SuppressWarnings("unchecked")
    public void userPreviewsDeleteImpactForLevel() {
        assertThat(lastLevelId).as("a level must exist before this step").isNotNull();
        String query = findGraphqlQueryFromFiles("get-level-delete-impact.graphql");
        lastLevelDeleteImpact = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "id", lastLevelId))
                .retrieveSync("getAllAffectedResourcesIfLevelDeleted")
                .toEntity(Map.class);
        assertThat(lastLevelDeleteImpact).isNotNull();
    }

    @When("user tries to preview delete impact for a level with a random id")
    public void userTriesToPreviewLevelImpactWithRandomId() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("institutionId", testContext.getInstitutionId());
        vars.put("id", UUID.randomUUID().toString());

        String query = findGraphqlQueryFromFiles("get-level-delete-impact.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(vars)
                    .retrieveSync("getAllAffectedResourcesIfLevelDeleted")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleFieldAccessException(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the level list should have {int} entries")
    public void levelListShouldHaveEntries(int expected) {
        assertThat(lastLevelList).isNotNull().hasSize(expected);
    }

    @Then("the level list should contain the level {string}")
    public void levelListContains(String name) {
        assertThat(lastLevelList).isNotNull();
        assertThat(lastLevelList).extracting(m -> m.get("name")).contains(name);
    }

    @Then("the level response should have name {string} and category {string}")
    public void levelResponseShouldHave(String name, String category) {
        assertThat(lastLevelResponse).isNotNull();
        assertThat(lastLevelResponse.get("name")).isEqualTo(name);
        assertThat(lastLevelResponse.get("category")).isEqualTo(category);
    }

    @Then("the level delete impact resource type should be {string}")
    public void levelDeleteImpactResourceType(String expected) {
        assertThat(lastLevelDeleteImpact).isNotNull();
        assertThat(lastLevelDeleteImpact.get("resourceType")).isEqualTo(expected);
    }

    @Then("the level delete impact total affected count should be {int}")
    public void levelDeleteImpactTotalAffected(int expected) {
        assertThat(lastLevelDeleteImpact).isNotNull();
        assertThat(((Number) lastLevelDeleteImpact.get("totalAffectedCount")).intValue()).isEqualTo(expected);
    }

    private void handleFieldAccessException(FieldAccessException e) {
        testContext.setRequestFailed(true);
        testContext.setLastErrorMessage(e.getMessage());
        var errors = e.getResponse().getErrors();
        if (!errors.isEmpty()) {
            var ext = errors.getFirst().getExtensions();
            if (ext != null && ext.containsKey("error_code")) {
                testContext.setLastErrorMessage(ext.get("error_code").toString());
            }
        }
    }
}

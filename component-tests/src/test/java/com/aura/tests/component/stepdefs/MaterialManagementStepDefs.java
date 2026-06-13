package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
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
public class MaterialManagementStepDefs {

    private final TestContext testContext;

    private final Map<String, String> materialIdsByName = new HashMap<>();
    private List<Map<String, Object>> lastMaterialList;
    private Map<String, Object> lastMaterialResponse;
    private Boolean lastDeleteMaterialResult;

    @Given("a material exists with name {string}")
    @SuppressWarnings("unchecked")
    public void aMaterialExistsWithName(String name) {
        String query = findGraphqlQueryFromFiles("create-material-in-institution.graphql");

        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", name);
        input.put("description", "Auto-created for material list/delete test");
        input.put("fileKey", null);
        input.put("topicIds", List.of());

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createMaterialInInstitution")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        String id = response.get("id").toString();
        materialIdsByName.put(name, id);
        testContext.setMaterialId(id);
    }

    @When("user lists materials in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsMaterialsInInstitution() {
        String query = findGraphqlQueryFromFiles("get-materials-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getMaterialsInInstitution")
                .toEntityList(Map.class);
        lastMaterialList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastMaterialList).isNotNull();
    }

    @When("user retrieves the material by its id")
    @SuppressWarnings("unchecked")
    public void userRetrievesMaterialById() {
        String query = findGraphqlQueryFromFiles("get-material-by-id.graphql");
        lastMaterialResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "materialId", testContext.getMaterialId()))
                .retrieveSync("getMaterialById")
                .toEntity(Map.class);
        assertThat(lastMaterialResponse).isNotNull();
    }

    @When("user deletes the material by id")
    public void userDeletesMaterialById() {
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "materialId", testContext.getMaterialId());

        String query = findGraphqlQueryFromFiles("delete-material-by-id.graphql");
        lastDeleteMaterialResult = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("deleteMaterialById")
                .toEntity(Boolean.class);
    }

    @When("user tries to delete a material with a random id")
    public void userTriesToDeleteMaterialWithRandomId() {
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "materialId", UUID.randomUUID().toString());

        String query = findGraphqlQueryFromFiles("delete-material-by-id.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteMaterialById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleFieldAccessException(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the material list should have {int} entries")
    public void materialListHasEntries(int expected) {
        assertThat(lastMaterialList).isNotNull().hasSize(expected);
    }

    @Then("the material list should contain the material {string}")
    public void materialListContains(String name) {
        assertThat(lastMaterialList).isNotNull();
        assertThat(lastMaterialList).extracting(m -> m.get("name")).contains(name);
    }

    @Then("the material response should have name {string}")
    public void materialResponseShouldHaveName(String name) {
        assertThat(lastMaterialResponse).isNotNull();
        assertThat(lastMaterialResponse.get("name")).isEqualTo(name);
    }

    @Then("the material delete should succeed")
    public void materialDeleteSucceeded() {
        assertThat(lastDeleteMaterialResult).isTrue();
    }

    @Then("listing materials in the institution should return {int} entries")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void listingMaterialsInInstitutionShouldReturn(int expected) {
        String query = findGraphqlQueryFromFiles("get-materials-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getMaterialsInInstitution")
                .toEntityList(Map.class);
        List<Map<String, Object>> materials = new ArrayList<>();
        for (Map m : raw) {
            materials.add((Map<String, Object>) m);
        }
        assertThat(materials).hasSize(expected);
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

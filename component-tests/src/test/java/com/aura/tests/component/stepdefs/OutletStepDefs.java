package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class OutletStepDefs {

    private final TestContext testContext;
    private List<Map<String, Object>> lastGetOutletsResponse;
    private OutletResponse lastUpdateOutletResponse;

    @When("user creates an outlet with details:")
    public void userCreatesAnOutletWithDetails(CreateOutletInput createOutletInput) {
        OutletResponse response = executeCreateOutlet(createOutletInput);

        assertThat(response.getName()).isEqualTo(createOutletInput.getName());
        assertThat(response.getAddress()).isEqualTo(createOutletInput.getAddress());
        assertThat(response.getPostalCode()).isEqualTo(createOutletInput.getPostalCode());
        assertThat(response.getContactNumber()).isEqualTo(createOutletInput.getContactNumber());
        assertThat(response.getEmail()).isEqualTo(createOutletInput.getEmail());
    }

    @And("an outlet exists with name {string}")
    public void anOutletExistsWithName(String name) {
        executeCreateOutlet(CreateOutletInput.newBuilder()
                .name(name)
                .address("123 Test Street")
                .postalCode(123456)
                .contactNumber(96228693)
                .description("Test outlet")
                .email("outlet@test.com")
                .build());
    }

    @When("user tries to create an outlet with name {string}")
    public void userTriesToCreateAnOutletWithName(String name) {
        String query = findGraphqlQueryFromFiles("create-outlet.graphql");
        try {
            Map<String, Object> inputMap = new java.util.HashMap<>();
            inputMap.put("institutionId", testContext.getInstitutionId());
            inputMap.put("name", name);
            inputMap.put("address", "456 Other Street");
            inputMap.put("postalCode", 654321);
            inputMap.put("contactNumber", 91234567);
            inputMap.put("description", "Another outlet");
            inputMap.put("email", "other@test.com");

            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", inputMap))
                    .retrieveSync("createOutlet")
                    .toEntity(OutletResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the outlet should be created successfully")
    public void theOutletShouldBeCreatedSuccessfully() {
        assertThat(testContext.getOutletId()).isNotBlank();
    }

    @When("user queries all outlets in the institution")
    @SuppressWarnings("unchecked")
    public void userQueriesAllOutletsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-outlets-in-institution.graphql");
        lastGetOutletsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getOutletsInInstitution")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();
    }

    @Then("the response should contain {int} outlets")
    public void theResponseShouldContainOutlets(int expectedCount) {
        assertThat(lastGetOutletsResponse).hasSize(expectedCount);
    }

    @And("the outlets should include names {string}")
    public void theOutletsShouldIncludeNames(String commaSeparatedNames) {
        List<String> expected = List.of(commaSeparatedNames.split(","));
        assertThat(lastGetOutletsResponse).extracting(m -> m.get("name"))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @When("user updates the outlet with details:")
    public void userUpdatesTheOutletWithDetails(DataTable dataTable) {
        Map<String, String> entry = dataTable.asMaps().get(0);
        Map<String, Object> input = new HashMap<>();
        input.put("id", testContext.getOutletId());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", entry.get("name"));
        input.put("address", entry.get("address"));
        input.put("postalCode", Integer.parseInt(entry.get("postalCode")));
        input.put("contactNumber", Integer.parseInt(entry.get("contactNumber")));
        input.put("description", entry.get("description"));
        input.put("email", entry.get("email"));

        String query = findGraphqlQueryFromFiles("update-outlet.graphql");
        lastUpdateOutletResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("updateOutlet")
                .toEntity(OutletResponse.class);
    }

    @Then("the outlet should be updated successfully with name {string}")
    public void theOutletShouldBeUpdatedSuccessfullyWithName(String expectedName) {
        assertThat(lastUpdateOutletResponse).isNotNull();
        assertThat(lastUpdateOutletResponse.getId()).isEqualTo(testContext.getOutletId());
        assertThat(lastUpdateOutletResponse.getName()).isEqualTo(expectedName);
    }

    @When("user tries to update a non-existent outlet")
    public void userTriesToUpdateANonExistentOutlet() {
        Map<String, Object> input = new HashMap<>();
        input.put("id", UUID.randomUUID().toString());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", "Whatever");
        input.put("address", "1 Somewhere");
        input.put("postalCode", 123456);
        input.put("contactNumber", 91234567);
        input.put("description", "Ignored");
        input.put("email", "ignored@test.com");

        String query = findGraphqlQueryFromFiles("update-outlet.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateOutlet")
                    .toEntity(OutletResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }


    private OutletResponse executeCreateOutlet(CreateOutletInput input) {
        String query = findGraphqlQueryFromFiles("create-outlet.graphql");

        Map<String, Object> inputMap = new java.util.HashMap<>();
        inputMap.put("institutionId", testContext.getInstitutionId());
        inputMap.put("name", input.getName());
        inputMap.put("address", input.getAddress());
        inputMap.put("postalCode", input.getPostalCode());
        inputMap.put("contactNumber", input.getContactNumber());
        inputMap.put("description", input.getDescription());
        inputMap.put("email", input.getEmail());

        OutletResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createOutlet")
                .toEntity(OutletResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        testContext.setOutletId(response.getId());
        return response;
    }
}

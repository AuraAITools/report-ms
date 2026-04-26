package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class OutletStepDefs {

    private final TestContext testContext;

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

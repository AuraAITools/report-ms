package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class InstitutionStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    @When("user onboards an institution with details:")
    public void onboardToInstitution(CreateInstitutionInput createInstitutionInput) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");

        Map<String, Object> variables = Map.of("input", createInstitutionInput);
        var institutionResponse = testContext
                .getGraphQLClient()
                .mutate()
                .build()
                .document(graphqlQuery)
                .variables(variables)
                .retrieveSync("createInstitution")
                .toEntity(InstitutionResponse.class);
        var expectedResponse = InstitutionResponse
                .newBuilder()
                .name("testing123")
                .email("testgg@gmail.com")
                .contactNumber("96228693")
                .address("address")
                .uen("test")
                .createdBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .updatedBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .build();

        assertThat(institutionResponse)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expectedResponse);
    }

    @And("an institution is onboarded")
    public void anInstitutionIsOnboarded() {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
        CreateInstitutionInput input = CreateInstitutionInput.newBuilder()
                .name("testing123")
                .email("testgg@gmail.com")
                .contactNumber("96228693")
                .address("address")
                .uen("test")
                .build();
        InstitutionResponse institution = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createInstitution")
                .toEntity(InstitutionResponse.class);
        testContext.setInstitutionId(institution.getId());
    }

    @When("user tries to onboard the same institution again")
    public void userTriesToOnboardTheSameInstitutionAgain() {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
        CreateInstitutionInput input = CreateInstitutionInput.newBuilder()
                .name("testing123")
                .email("testgg@gmail.com")
                .contactNumber("96228693")
                .address("address")
                .uen("test")
                .build();
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(graphqlQuery)
                    .variables(Map.of("input", input))
                    .retrieveSync("createInstitution")
                    .toEntity(InstitutionResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("institution should be created")
    public void institutionIsCreated() {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM institutions");
        assertThat(results).isNotNull().hasSize(1);
    }
}

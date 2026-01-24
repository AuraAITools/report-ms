package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class InstitutionStepDefs {
    private TestContext testContext;

    private JdbcTemplate jdbcTemplate;

    public InstitutionStepDefs(TestContext testContext, JdbcTemplate jdbcTemplate) {
        this.testContext = testContext;
        this.jdbcTemplate = jdbcTemplate;
    }

    @When("user onboards a institution with details:")
    public void onboardToInstitution(CreateInstitutionInput createInstitutionInput) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");

        //build variable
        Map<String, Object> variables = Map.of("input", createInstitutionInput);
        // GQL client
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
                .isEqualTo(expectedResponse)
        ;
    }

    // TODO: make tests better
    @Then("^institution should be created$")
    public void institutionIsCreated() {
        String sql = """
                SELECT * FROM institutions;
                """;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        assertThat(results)
                .isNotNull()
                .hasSize(1);
    }

    // TODO:
    @Then("default initial data should be loaded")
    public void initialDataIsLoaded() {
        String sql = """
                SELECT COUNT(*) subject_level_links
                """;
        log.info("initial data should be loaded");
    }

}

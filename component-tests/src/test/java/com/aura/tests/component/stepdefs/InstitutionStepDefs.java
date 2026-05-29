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
import org.keycloak.admin.client.Keycloak;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class InstitutionStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;
    private final Keycloak keycloak;

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
        testContext.setInstitutionId(institutionResponse.getId());
    }

    @And("an institution is onboarded")
    public void anInstitutionIsOnboarded() {
        CreateInstitutionInput input = CreateInstitutionInput.newBuilder()
                .name("testing123")
                .email("testgg@gmail.com")
                .contactNumber("96228693")
                .address("address")
                .uen("test")
                .build();
        onboardInstitution(input);
    }

    @And("an institution is onboarded with details:")
    public void anInstitutionIsOnboardedWithDetails(CreateInstitutionInput input) {
        onboardInstitution(input);
    }

    private void onboardInstitution(CreateInstitutionInput input) {
        ensureClientAuthenticated();
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
        InstitutionResponse institution = testContext.getGraphQLClient()
                .mutate().build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createInstitution")
                .toEntity(InstitutionResponse.class);
        testContext.setInstitutionId(institution.getId());
    }

    private void ensureClientAuthenticated() {
        String accessToken = keycloak.tokenManager().getAccessToken().getToken();
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(accessToken))
                .build();
        testContext.setGraphQLClient(client);
    }

    @When("user tries to onboard an institution with details:")
    public void userTriesToOnboardAnInstitutionWithDetails(CreateInstitutionInput input) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
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
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("institution should be created")
    public void institutionIsCreated() {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM institutions");
        assertThat(results).isNotNull().hasSize(1);
    }

    @When("user creates an institution with details:")
    public void userCreatesAnInstitutionWithDetails(CreateInstitutionInput input) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
        InstitutionResponse response = testContext
                .getGraphQLClient()
                .mutate()
                .build()
                .document(graphqlQuery)
                .variables(Map.of("input", input))
                .retrieveSync("createInstitution")
                .toEntity(InstitutionResponse.class);

        InstitutionResponse expected = InstitutionResponse.newBuilder()
                .name(input.getName())
                .email(input.getEmail())
                .contactNumber(input.getContactNumber())
                .address(input.getAddress())
                .uen(input.getUen())
                .createdBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .updatedBy("2c43b314-3e3c-4897-a3d3-02a8cab3e5e7")
                .build();

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expected);
        testContext.setInstitutionId(response.getId());
    }

    @When("user tries to create an institution with details:")
    public void userTriesToCreateAnInstitutionWithDetails(CreateInstitutionInput input) {
        String graphqlQuery = findGraphqlQueryFromFiles("create-institution.graphql");
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
            var errors = e.getResponse().getErrors();
            if (!errors.isEmpty()) {
                var extensions = errors.getFirst().getExtensions();
                if (extensions != null && extensions.containsKey("error_code")) {
                    testContext.setLastErrorMessage(extensions.get("error_code").toString());
                }
            }
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the institution should be created successfully")
    public void theInstitutionShouldBeCreatedSuccessfully() {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM institutions");
        assertThat(results).isNotNull().hasSize(1);
        assertThat(testContext.getInstitutionId()).isNotNull();
    }

}

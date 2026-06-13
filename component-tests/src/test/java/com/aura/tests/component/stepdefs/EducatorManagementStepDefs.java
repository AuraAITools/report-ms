package com.aura.tests.component.stepdefs;

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
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class EducatorManagementStepDefs {

    private final TestContext testContext;

    private Map<String, Object> lastEducatorResponse;
    private List<Map<String, Object>> lastEducatorsResponse;
    private Boolean lastDeleteEducatorResponse;

    @When("user queries all educators in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userQueriesAllEducatorsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-educators-in-institution.graphql");
        try {
            List<Map> raw = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId()))
                    .retrieveSync("getAllEducatorsInInstitution")
                    .toEntityList(Map.class);
            lastEducatorsResponse = raw == null
                    ? List.<Map<String, Object>>of()
                    : raw.stream().map(m -> (Map<String, Object>) m).toList();
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the educators response should contain {int} educator(s)")
    public void theEducatorsResponseShouldContain(int expected) {
        assertThat(lastEducatorsResponse).hasSize(expected);
    }

    @When("user queries the noted educator by id")
    @SuppressWarnings("unchecked")
    public void userQueriesTheNotedEducatorById() {
        String query = findGraphqlQueryFromFiles("get-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getEducatorId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastEducatorResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getEducatorById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries educator by a random id")
    public void userQueriesEducatorByRandomId() {
        String query = findGraphqlQueryFromFiles("get-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getEducatorById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the educator response should contain name {string} and email {string}")
    public void theEducatorResponseShouldContainNameAndEmail(String name, String email) {
        assertThat(lastEducatorResponse).as("Expected an educator response to be present").isNotNull();
        assertThat(lastEducatorResponse.get("name")).isEqualTo(name);
        assertThat(lastEducatorResponse.get("email")).isEqualTo(email);
    }

    @Then("the educator response should have employment type {string}")
    public void theEducatorResponseShouldHaveEmploymentType(String employmentType) {
        assertThat(lastEducatorResponse).as("Expected an educator response to be present").isNotNull();
        assertThat(lastEducatorResponse.get("employmentType")).isEqualTo(employmentType);
    }

    @When("user updates the noted educator with details:")
    @SuppressWarnings("unchecked")
    public void userUpdatesTheNotedEducatorWithDetails(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().getFirst();
        String query = findGraphqlQueryFromFiles("update-educator-by-id.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("id", testContext.getEducatorId());
        input.put("institutionId", testContext.getInstitutionId());
        if (row.containsKey("name") && row.get("name") != null) input.put("name", row.get("name"));
        if (row.containsKey("email") && row.get("email") != null) input.put("email", row.get("email"));
        if (row.containsKey("startDate") && row.get("startDate") != null) input.put("startDate", row.get("startDate"));
        if (row.containsKey("dateOfBirth") && row.get("dateOfBirth") != null) {
            input.put("dateOfBirth", row.get("dateOfBirth"));
        }
        if (row.containsKey("employmentType") && row.get("employmentType") != null) {
            input.put("employmentType", row.get("employmentType"));
        }

        try {
            lastEducatorResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateEducatorById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user tries to update an educator with a random id and name {string}")
    public void userTriesToUpdateAnEducatorWithRandomId(String newName) {
        String query = findGraphqlQueryFromFiles("update-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId(),
                "name", newName
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateEducatorById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user deletes the noted educator by id")
    public void userDeletesNotedEducatorById() {
        String query = findGraphqlQueryFromFiles("delete-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getEducatorId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastDeleteEducatorResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteEducatorById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the delete educator response should be true")
    public void theDeleteEducatorResponseShouldBeTrue() {
        assertThat(lastDeleteEducatorResponse).isTrue();
    }

    @And("querying the deleted educator by id should fail with error code {string}")
    public void queryingDeletedEducatorByIdShouldFail(String errorCode) {
        String query = findGraphqlQueryFromFiles("get-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getEducatorId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getEducatorById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
        assertThat(testContext.isRequestFailed()).isTrue();
        assertThat(testContext.getLastErrorMessage()).isEqualTo(errorCode);
    }

    @When("user tries to delete an educator with a random id")
    public void userTriesToDeleteEducatorWithRandomId() {
        String query = findGraphqlQueryFromFiles("delete-educator-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteEducatorById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    private void handleErr(FieldAccessException e) {
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

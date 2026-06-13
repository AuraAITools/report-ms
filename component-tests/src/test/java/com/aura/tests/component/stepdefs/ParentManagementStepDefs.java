package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class ParentManagementStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private String notedParentId;
    private Map<String, Object> lastParentResponse;
    private List<Map<String, Object>> lastParentsResponse;
    private Boolean lastDeleteParentResponse;

    /**
     * The Given step that creates a parent in the account does not stash the id in TestContext
     * (and the constraint of this task forbids editing TestContext). Look it up by account id
     * via JDBC the first time it is needed within the scenario.
     */
    private String resolveNotedParentId() {
        if (notedParentId != null) return notedParentId;
        List<String> ids = jdbcTemplate.queryForList(
                "SELECT id::text FROM public.parents WHERE account_id = ?::uuid LIMIT 1",
                String.class, testContext.getAccountId());
        assertThat(ids).as("Expected a parent to exist for account %s", testContext.getAccountId()).isNotEmpty();
        notedParentId = ids.getFirst();
        return notedParentId;
    }

    @When("user queries all parents in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userQueriesAllParentsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-parents-in-institution.graphql");
        try {
            List<Map> raw = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId()))
                    .retrieveSync("getAllParentsInInstitution")
                    .toEntityList(Map.class);
            lastParentsResponse = raw == null
                    ? List.<Map<String, Object>>of()
                    : raw.stream().map(m -> (Map<String, Object>) m).toList();
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the parents response should contain {int} parent(s)")
    public void theParentsResponseShouldContain(int expected) {
        assertThat(lastParentsResponse).hasSize(expected);
    }

    @When("user queries the noted parent by id")
    @SuppressWarnings("unchecked")
    public void userQueriesTheNotedParentById() {
        String query = findGraphqlQueryFromFiles("get-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", resolveNotedParentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastParentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getParentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries parent by a random id")
    public void userQueriesParentByRandomId() {
        String query = findGraphqlQueryFromFiles("get-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getParentById")
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

    @Then("the parent response should have relationship {string}")
    public void theParentResponseShouldHaveRelationship(String relationship) {
        assertThat(lastParentResponse).as("Expected a parent response to be present").isNotNull();
        assertThat(lastParentResponse.get("relationship")).isEqualTo(relationship);
    }

    @When("user updates the noted parent with relationship {string}")
    @SuppressWarnings("unchecked")
    public void userUpdatesTheNotedParentWithRelationship(String relationship) {
        String query = findGraphqlQueryFromFiles("update-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", resolveNotedParentId(),
                "institutionId", testContext.getInstitutionId(),
                "relationship", relationship
        );
        try {
            lastParentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateParentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user tries to update a parent with a random id and relationship {string}")
    public void userTriesToUpdateAParentWithRandomId(String relationship) {
        String query = findGraphqlQueryFromFiles("update-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId(),
                "relationship", relationship
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateParentById")
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

    @When("user deletes the noted parent by id")
    public void userDeletesTheNotedParentById() {
        String query = findGraphqlQueryFromFiles("delete-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", resolveNotedParentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastDeleteParentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteParentById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the delete parent response should be true")
    public void theDeleteParentResponseShouldBeTrue() {
        assertThat(lastDeleteParentResponse).isTrue();
    }

    @And("querying the deleted parent by id should fail with error code {string}")
    public void queryingDeletedParentByIdShouldFail(String errorCode) {
        String query = findGraphqlQueryFromFiles("get-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", resolveNotedParentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getParentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
        assertThat(testContext.isRequestFailed()).isTrue();
        assertThat(testContext.getLastErrorMessage()).isEqualTo(errorCode);
    }

    @When("user tries to delete a parent with a random id")
    public void userTriesToDeleteAParentWithRandomId() {
        String query = findGraphqlQueryFromFiles("delete-parent-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteParentById")
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

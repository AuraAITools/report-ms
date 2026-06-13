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
public class SchoolManagementStepDefs {

    private final TestContext testContext;

    private final Map<String, String> schoolIdsByName = new HashMap<>();
    private String lastSchoolId;
    private Map<String, Object> lastSchoolResponse;
    private List<Map<String, Object>> lastSchoolList;
    private Map<String, Object> lastSchoolDeleteImpact;
    private Boolean lastDeleteSchoolResult;

    @Given("a school exists with name {string} and category {string}")
    public void aSchoolExistsWithNameAndCategory(String name, String category) {
        createSchool(name, category);
    }

    @When("user creates a school in the institution with name {string} and category {string}")
    public void userCreatesSchoolInInstitution(String name, String category) {
        createSchool(name, category);
    }

    @SuppressWarnings("unchecked")
    private void createSchool(String name, String category) {
        String query = findGraphqlQueryFromFiles("create-school-in-institution.graphql");
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", name,
                "schoolCategory", category);

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createSchoolInInstitution")
                .toEntity(Map.class);
        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        String id = response.get("id").toString();
        schoolIdsByName.put(name, id);
        lastSchoolId = id;
        lastSchoolResponse = response;
    }

    @When("user lists schools in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsSchoolsInInstitution() {
        String query = findGraphqlQueryFromFiles("get-schools-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getSchoolsInInstitution")
                .toEntityList(Map.class);
        lastSchoolList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastSchoolList).isNotNull();
    }

    @When("user previews delete impact for the school")
    @SuppressWarnings("unchecked")
    public void userPreviewsDeleteImpactForSchool() {
        assertThat(lastSchoolId).as("a school must exist before this step").isNotNull();
        String query = findGraphqlQueryFromFiles("get-school-delete-impact.graphql");
        lastSchoolDeleteImpact = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "id", lastSchoolId))
                .retrieveSync("getAllAffectedResourcesIfSchoolDeleted")
                .toEntity(Map.class);
        assertThat(lastSchoolDeleteImpact).isNotNull();
    }

    @When("user deletes the school in the institution")
    public void userDeletesSchoolInInstitution() {
        assertThat(lastSchoolId).isNotNull();
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "schoolId", lastSchoolId);

        String query = findGraphqlQueryFromFiles("delete-school-in-institution.graphql");
        lastDeleteSchoolResult = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("deleteSchoolInInstitution")
                .toEntity(Boolean.class);
    }

    @When("user tries to delete a school with a random id")
    public void userTriesToDeleteSchoolWithRandomId() {
        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "schoolId", UUID.randomUUID().toString());

        String query = findGraphqlQueryFromFiles("delete-school-in-institution.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteSchoolInInstitution")
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

    @Then("the school should be created successfully with name {string} and category {string}")
    public void schoolShouldBeCreated(String name, String category) {
        assertThat(lastSchoolResponse).isNotNull();
        assertThat(lastSchoolResponse.get("name")).isEqualTo(name);
        assertThat(lastSchoolResponse.get("schoolCategory")).isEqualTo(category);
    }

    @Then("the school list should have {int} entries")
    public void schoolListShouldHaveEntries(int expected) {
        assertThat(lastSchoolList).isNotNull().hasSize(expected);
    }

    @Then("the school list should contain the school {string}")
    public void schoolListContains(String name) {
        assertThat(lastSchoolList).isNotNull();
        assertThat(lastSchoolList).extracting(m -> m.get("name")).contains(name);
    }

    @Then("the school delete impact resource type should be {string}")
    public void schoolDeleteImpactResourceType(String expected) {
        assertThat(lastSchoolDeleteImpact).isNotNull();
        assertThat(lastSchoolDeleteImpact.get("resourceType")).isEqualTo(expected);
    }

    @Then("the school delete impact total affected count should be {int}")
    public void schoolDeleteImpactTotalAffected(int expected) {
        assertThat(lastSchoolDeleteImpact).isNotNull();
        assertThat(((Number) lastSchoolDeleteImpact.get("totalAffectedCount")).intValue()).isEqualTo(expected);
    }

    @Then("the school delete impact should include a {string} group for {string} with {int} affected ids")
    @SuppressWarnings("unchecked")
    public void schoolDeleteImpactGroup(String cleanupAction, String resourceType, int expectedCount) {
        assertThat(lastSchoolDeleteImpact).isNotNull();
        List<Map<String, Object>> groups = (List<Map<String, Object>>) lastSchoolDeleteImpact.get("affectedGroups");
        assertThat(groups).isNotNull();
        Map<String, Object> match = groups.stream()
                .filter(g -> resourceType.equals(g.get("resourceType")) && cleanupAction.equals(g.get("cleanupAction")))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No affected group with resourceType=" + resourceType + " cleanupAction=" + cleanupAction));
        List<?> ids = (List<?>) match.get("affectedIds");
        assertThat(ids).hasSize(expectedCount);
    }

    @Then("the school delete should succeed")
    public void schoolDeleteShouldSucceed() {
        assertThat(lastDeleteSchoolResult).isTrue();
    }

    @Then("listing schools in the institution should return {int} entries")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void listingSchoolsInInstitutionShouldReturn(int expected) {
        String query = findGraphqlQueryFromFiles("get-schools-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getSchoolsInInstitution")
                .toEntityList(Map.class);
        List<Map<String, Object>> schools = new ArrayList<>();
        for (Map m : raw) {
            schools.add((Map<String, Object>) m);
        }
        assertThat(schools).hasSize(expected);
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

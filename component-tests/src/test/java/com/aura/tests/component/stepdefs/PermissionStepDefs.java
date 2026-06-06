package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class PermissionStepDefs {

    private final TestContext testContext;
    private Map<String, Object> lastResponse;
    private List<Map<String, Object>> lastGroupPermissionsResponse;

    @When("the aura admin looks up their permissions")
    public void theAuraAdminLooksUpTheirPermissions() {
        String query = findGraphqlQueryFromFiles("lookup-permissions.graphql");

        lastResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .retrieveSync("lookupPermissions")
                .toEntity(Map.class);

        log.info("Lookup permissions response: {}", lastResponse);
    }

    @SuppressWarnings("unchecked")
    @Then("the response should contain permissions for resource type {string} with resource id {string}")
    public void theResponseShouldContainPermissionsForResourceTypeWithId(String resourceType, String resourceId) {
        List<Map<String, Object>> permissions = (List<Map<String, Object>>) lastResponse.get("permissions");

        boolean found = permissions.stream()
                .anyMatch(p -> resourceType.equals(p.get("resourceType"))
                        && resourceId.equals(p.get("resourceId")));
        assertThat(found)
                .as("Expected permissions for %s:%s but got: %s", resourceType, resourceId, permissions)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Then("the response should contain permissions for resource type {string}")
    public void theResponseShouldContainPermissionsForResourceType(String resourceType) {
        List<Map<String, Object>> permissions = (List<Map<String, Object>>) lastResponse.get("permissions");

        boolean found = permissions.stream()
                .anyMatch(p -> resourceType.equals(p.get("resourceType")));
        assertThat(found)
                .as("Expected permissions for resource type %s but got: %s", resourceType, permissions)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @When("the user looks up their group permissions for the institution")
    public void theUserLooksUpTheirGroupPermissions() {
        String query = findGraphqlQueryFromFiles("lookup-group-permissions.graphql");

        lastGroupPermissionsResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variable("institutionId", testContext.getInstitutionId())
                .retrieveSync("lookupGroupPermissions")
                .toEntityList(Map.class)
                .stream()
                .map(m -> (Map<String, Object>) m)
                .toList();

        log.info("Lookup group permissions response: {}", lastGroupPermissionsResponse);
    }

    @Then("the group permissions response should contain resource type {string}")
    public void theGroupPermissionsResponseShouldContainResourceType(String resourceType) {
        boolean found = lastGroupPermissionsResponse.stream()
                .anyMatch(p -> resourceType.equals(p.get("resourceType")));
        assertThat(found)
                .as("Expected group permissions for resource type %s but got: %s",
                        resourceType, lastGroupPermissionsResponse)
                .isTrue();
    }

    @Then("the group permissions response should not contain resource type {string}")
    public void theGroupPermissionsResponseShouldNotContainResourceType(String resourceType) {
        boolean found = lastGroupPermissionsResponse.stream()
                .anyMatch(p -> resourceType.equals(p.get("resourceType")));
        assertThat(found)
                .as("Did not expect group permissions for resource type %s but got: %s",
                        resourceType, lastGroupPermissionsResponse)
                .isFalse();
    }

    @And("every group permissions entry should have a non-empty displayName")
    public void everyGroupPermissionsEntryShouldHaveDisplayName() {
        assertThat(lastGroupPermissionsResponse)
                .allSatisfy(entry -> assertThat((String) entry.get("displayName"))
                        .as("displayName for %s should not be null/empty", entry)
                        .isNotBlank());
    }
}

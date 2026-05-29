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
}

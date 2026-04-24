package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import com.aura.tests.component.contexts.TestContext;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class CourseStepDefs {

    private final TestContext testContext;

    @When("user creates a course with details:")
    public void userCreatesACourse(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        String query = findGraphqlQueryFromFiles("create-course.graphql");

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "name", data.get("name"),
                "maxSize", Integer.parseInt(data.get("maxSize")),
                "lessonFrequency", data.get("lessonFrequency"),
                "priceRecord", Map.of(
                        "price", Double.parseDouble(data.get("price")),
                        "frequency", data.get("priceFrequency")
                ),
                "courseStartTimestamptz", data.get("startDate"),
                "courseEndTimestamptz", data.get("endDate"),
                "outletId", testContext.getOutletId()
        );

        CourseResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createCourse")
                .toEntity(CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(data.get("name"));
        assertThat(response.getCreatedBy()).isEqualTo(TestConstants.AURA_ADMIN_USER_ID);

        testContext.setCourseId(response.getId());
    }

    @Then("the course should be created successfully")
    public void theCourseShouldBeCreatedSuccessfully() {
        assertThat(testContext.getCourseId()).isNotNull();
    }

    @And("a UMA resource should be registered for the course with type {string} and scopes {string}")
    public void umaResourceRegisteredForCourseWithTypeAndScopes(String expectedType, String scopesCsv) {
        String courseId = testContext.getCourseId();
        String expectedResourceName = "course:" + courseId;
        List<String> expectedScopes = List.of(scopesCsv.split(","));

        KeycloakContainer container = KeycloakTestContainer.getSingleton();
        try (Keycloak adminClient = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build()) {

            ClientRepresentation client = adminClient.realm("aura")
                    .clients().findByClientId("aura-application-client").getFirst();
            ClientResource adminClientResource = adminClient.realm("aura")
                    .clients().get(client.getId());

            List<ResourceRepresentation> resources = adminClientResource.authorization()
                    .resources().findByName(expectedResourceName);

            assertThat(resources)
                    .as("UMA resource '%s' should be registered in Keycloak", expectedResourceName)
                    .isNotEmpty();

            ResourceRepresentation resource = resources.getFirst();
            assertThat(resource.getName()).isEqualTo(expectedResourceName);
            assertThat(resource.getType())
                    .as("Resource type should be '%s'", expectedType)
                    .isEqualTo(expectedType);
            assertThat(resource.getOwnerManagedAccess())
                    .as("Resource should have ownerManagedAccess enabled")
                    .isTrue();
            assertThat(resource.getScopes())
                    .extracting("name")
                    .as("Resource should have scopes %s", expectedScopes)
                    .containsExactlyInAnyOrderElementsOf(expectedScopes);
        }
    }
}

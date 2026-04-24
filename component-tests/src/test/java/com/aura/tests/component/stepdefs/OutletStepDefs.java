package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
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

    @And("a UMA resource should be registered for the outlet with type {string} and scopes {string}")
    public void umaResourceRegisteredForOutletWithTypeAndScopes(String expectedType, String scopesCsv) {
        String outletId = testContext.getOutletId();
        String expectedResourceName = "outlet:" + outletId;
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

    @And("a UMA resource should be registered for the outlet")
    public void umaResourceShouldBeRegisteredForTheOutlet() {
        String outletId = testContext.getOutletId();
        String expectedResourceName = "outlet:" + outletId;

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

            assertThat(resources).isNotEmpty();

            ResourceRepresentation resource = resources.getFirst();
            assertThat(resource.getName()).isEqualTo(expectedResourceName);
            assertThat(resource.getType()).isEqualTo("urn:aura:outlet");
            assertThat(resource.getOwnerManagedAccess()).isTrue();
            assertThat(resource.getScopes())
                    .extracting("name")
                    .containsExactlyInAnyOrder("view", "manage", "delegate");
        }
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

package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;


import static com.aura.core.usecases.onboarding.RoleTypes.AURA_ADMIN_ROLE;

@Slf4j
public class RoleStepDefs {

    private TestContext testContext;

    private Keycloak keycloak;

    public RoleStepDefs(TestContext testContext, Keycloak keycloak) {
        this.testContext = testContext;
        this.keycloak = keycloak;
    }

    @Given("user has {string} role")
    public void userHasAuraAdminRole(String role) {
        String token = switch (role) {
            case AURA_ADMIN_ROLE -> keycloak.tokenManager().getAccessToken().getToken();
            default -> throw new RuntimeException(String.format("Role %s is not supported", role));
        };

        var graphqlClient = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.set("x-tenant-id", "*");
                }).build();

        testContext.setGraphQLClient(graphqlClient);
    }

}

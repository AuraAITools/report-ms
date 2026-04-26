package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;

import static com.aura.core.usecases.onboarding.RoleTypes.AURA_ADMIN_ROLE;

@Slf4j
@RequiredArgsConstructor
public class RoleStepDefs {

    private final TestContext testContext;
    private final Keycloak keycloak;

    @Given("user has {string} role")
    public void userHasAuraAdminRole(String role) {
        String accessToken = switch (role) {
            case AURA_ADMIN_ROLE -> keycloak.tokenManager().getAccessToken().getToken();
            default -> throw new RuntimeException(String.format("Role %s is not supported", role));
        };

        // Use access token directly — SpiceDB checks permissions server-side, no RPT needed
        var client = testContext.getGraphQLClient()
                .mutate()
                .headers(headers -> headers.setBearerAuth(accessToken))
                .build();
        testContext.setGraphQLClient(client);
    }

    @Given("the user switches to the institution tenant")
    public void userSwitchesToInstitutionTenant() {
        testContext.setGraphQLClient(
                testContext.getGraphQLClient().mutate()
                        .headers(headers -> headers.set("x-tenant-id", testContext.getInstitutionId()))
                        .build()
        );
    }
}

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

    @Given("the user switches to the institution tenant")
    public void userSwitchesToInstitutionTenant() {
        testContext.setGraphQLClient(
                testContext.getGraphQLClient().mutate()
                        .headers(headers -> headers.set("x-tenant-id", testContext.getInstitutionId()))
                        .build()
        );
    }
}

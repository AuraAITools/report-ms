package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class ErrorStepDefs {

    private final TestContext testContext;

    @Then("the request should fail with an error")
    public void theRequestShouldFailWithAnError() {
        assertThat(testContext.isRequestFailed())
                .as("Expected the request to fail but it succeeded. Last error: %s", testContext.getLastErrorMessage())
                .isTrue();
    }
}

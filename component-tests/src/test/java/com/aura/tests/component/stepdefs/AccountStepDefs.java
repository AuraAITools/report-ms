package com.aura.tests.component.stepdefs;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
import com.aura.tests.component.TestConstants;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import java.util.Map;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AccountStepDefs {

    private final TestContext testContext;

    @When("user creates an institution admin account with details:")
    public void userCreatesInstitutionAdminAccount(CreateInstitutionAdminAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createInstitutionAdminAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();

        AccountResponse expected = AccountResponse.newBuilder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .createdBy(TestConstants.AURA_ADMIN_USER_ID)
                .updatedBy(TestConstants.AURA_ADMIN_USER_ID)
                .build();

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "relationship")
                .isEqualTo(expected);

        testContext.setAccountId(response.getId());
    }

    @Then("the institution admin account should be created successfully")
    public void theAdminAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @When("user tries to create an institution admin account with details:")
    public void userTriesToCreateInstitutionAdminAccount(CreateInstitutionAdminAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-institution-admin-account.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createInstitutionAdminAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user creates a parent account with details:")
    public void userCreatesParentAccount(CreateParentAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-parent-account.graphql");

        AccountResponse response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createParentAccount")
                .toEntity(AccountResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();

        AccountResponse expected = AccountResponse.newBuilder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .createdBy(TestConstants.AURA_ADMIN_USER_ID)
                .updatedBy(TestConstants.AURA_ADMIN_USER_ID)
                .build();

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "relationship")
                .isEqualTo(expected);

        testContext.setAccountId(response.getId());
    }

    @Then("the parent account should be created successfully")
    public void theParentAccountShouldBeCreatedSuccessfully() {
        assertThat(testContext.getAccountId()).isNotNull();
    }

    @When("user tries to create a parent account with details:")
    public void userTriesToCreateParentAccount(CreateParentAccountInput input) {
        String query = findGraphqlQueryFromFiles("create-parent-account.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("createParentAccount")
                    .toEntity(AccountResponse.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            testContext.setRequestFailed(true);
            testContext.setLastErrorMessage(e.getMessage());
        }
    }
}

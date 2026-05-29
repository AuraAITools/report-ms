@PreExistingFailure
@Api
Feature: Get Account By Id

  @ComponentTest
  Scenario: Aura admin retrieves an account by id
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And user in "aura-admin" group creates an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries account by id
    Then the account should be returned with first name "John" and last name "Doe"

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent account
    Given an institution is onboarded
    When user queries account with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

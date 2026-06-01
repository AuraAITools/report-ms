@Api
Feature: Get Account By User Id

  @ComponentTest
  Scenario: Aura admin retrieves an account by the user's userId
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And user in "aura-admin" group creates an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries account by user id
    Then the account by user id should be returned with first name "John" and last name "Doe"

  @ComponentTest
  Scenario: Aura admin fails to retrieve an account for an unknown user
    Given an institution is onboarded
    When user queries account by a non-existent user id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

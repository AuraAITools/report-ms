@PreExistingFailure
@Api
Feature: Get All Accounts In Institution

  @ComponentTest
  Scenario: Aura admin retrieves all accounts in the institution
    Given an institution is onboarded with details:
      | name       | email            | contactNumber | address | uen  |
      | testing123 | testgg@gmail.com | 96228693      | address | test |
    And user in "aura-admin" group creates an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries all accounts in the institution
    Then the accounts response should contain at least 1 account

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when institution has no accounts
    Given an institution is onboarded
    When user queries all accounts in the institution
    Then the accounts response should contain 0 accounts

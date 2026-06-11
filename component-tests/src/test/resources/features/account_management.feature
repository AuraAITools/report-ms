@Api
Feature: Account management in an institution

  An aura admin creates sign-in accounts for the people of an onboarded
  institution and looks them up by id, by owning user, or as a full listing.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant

  @ComponentTest
  Scenario: Aura admin creates an account in the institution
    When user creates an account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    Then the account should be created successfully
    And the user "admin@test.com" enabled status should be "true"
    And the user "admin@test.com" email verified status should be "false"
    And the user "admin@test.com" should have required actions "VERIFY_EMAIL,UPDATE_PASSWORD"

  @ComponentTest
  Scenario: Aura admin retrieves a created account by its id
    Given user creates an account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries account by id
    Then the account should be returned with first name "John" and last name "Doe"

  @ComponentTest
  Scenario: Aura admin retrieves a created account by the owning user's id
    Given user creates an account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries account by user id
    Then the account by user id should be returned with first name "John" and last name "Doe"

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent account
    When user queries account with non-existent id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin fails to retrieve an account for an unknown user
    When user queries account by a non-existent user id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin lists all accounts in the institution
    Given user creates an account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    When user queries all accounts in the institution
    Then the accounts response should contain 1 account

  @ComponentTest
  Scenario: Aura admin sees an empty listing when the institution has no accounts
    When user queries all accounts in the institution
    Then the accounts response should contain 0 accounts

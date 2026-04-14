@Api
Feature: Create Institution Admin Account

  @ComponentTest
  Scenario: Successfully create an institution admin account
    Given user has "aura-admin" role
    And an institution is onboarded
    And user switches to the institution tenant
    When user creates an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    Then the institution admin account should be created successfully

  @ComponentTest
  Scenario: Cannot create an institution admin account without an institution tenant
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to create an institution admin account with details:
      | email          | firstName | lastName | contact  |
      | admin@test.com | John      | Doe      | 96228693 |
    Then the request should fail with an error

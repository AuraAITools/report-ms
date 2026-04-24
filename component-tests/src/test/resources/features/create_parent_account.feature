@Api
Feature: Create Parent Account

  @ComponentTest
  Scenario: Successfully create a parent account
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates a parent account with details:
      | email           | firstName | lastName | contact  | relationship |
      | parent@test.com | Jane      | Doe      | 91234567 | PARENT       |
    Then the parent account should be created successfully

  @ComponentTest
  Scenario: Cannot create a parent account without an institution tenant
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to create a parent account with details:
      | email           | firstName | lastName | contact  | relationship |
      | parent@test.com | Jane      | Doe      | 91234567 | PARENT       |
    Then the request should fail with an error

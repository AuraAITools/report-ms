@Api
Feature: Parent authorization

  A parent attached to a family account holds the "admin" role only on their
  own account's user group. Parents have no institution or outlet roles, so
  they cannot view the institution or create any institution resources.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And an account exists in the institution for "parent@test.com" with details:
      | firstName | lastName | contact  |
      | Jane      | Doe      | 91234567 |
    And user creates a parent in the account with relationship "PARENT"
    And the user "parent@test.com" completes account setup with the test password
    And user switches to "parent@test.com" identity

  @ComponentTest @AuthParent
  Scenario: Parent cannot view the institution
    When user queries their institution
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthParent
  Scenario: Parent cannot create an outlet in the institution
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthParent
  Scenario: Parent cannot create a course in the institution
    When user tries to create a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthParent
  Scenario: Parent cannot create accounts in the institution
    When user tries to create an account with details:
      | email             | firstName | lastName | contact  |
      | newadmin@test.com | New       | Admin    | 91234567 |
    Then the request should fail with error code "FORBIDDEN"

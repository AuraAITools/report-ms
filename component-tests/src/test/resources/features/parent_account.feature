@Api
Feature: Parent joins a family account

  A family gets a sign-in account, then a parent profile is attached to it so
  the parent can follow their children's progress. Attaching the parent also
  fires the parent welcome email.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an account exists in the institution for "parent@test.com" with details:
      | firstName | lastName | contact  |
      | Jane      | Doe      | 91234567 |

  @ComponentTest
  Scenario: Aura admin attaches a parent profile to the family account
    When user creates a parent in the account with relationship "PARENT"
    Then the parent should be created with relationship "PARENT"
    And an email with template "parent-welcome" should have been sent to "parent@test.com"

  @ComponentTest
  Scenario: Aura admin cannot attach a second parent profile to the same account
    Given user creates a parent in the account with relationship "PARENT"
    When user tries to create a parent in the account with relationship "PARENT"
    Then the request should fail with error code "DUPLICATE_RESOURCE"

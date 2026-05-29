@PreExistingFailure
@Api
Feature: Parent welcome email is sent on parent account creation

  @ComponentTest
  Scenario: Successfully creating a parent account fires the welcome email
    Given user has "aura-admin" role
    And an institution is onboarded
    When user creates a parent account with details:
      | email                  | firstName | lastName | contact  | relationship |
      | parent-welcome@aura.io | Pat       | Lee      | 91234567 | PARENT       |
    Then the parent account should be created successfully
    And an email with template "parent-welcome" should have been sent to "parent-welcome@aura.io"
    And exactly 1 email should have been sent

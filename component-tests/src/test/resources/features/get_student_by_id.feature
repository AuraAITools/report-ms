@Api
Feature: Get student by id

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |

  @ComponentTest
  Scenario: Aura admin successfully retrieves student by id
    When user queries the noted student by id
    Then the student response should contain name "Alice Pupil" and email "alice.pupil@example.com"
    And the student response should contain a linked level
    And the student response should contain a linked school

  @ComponentTest
  Scenario: Querying non-existent student returns not found
    When user queries student by a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

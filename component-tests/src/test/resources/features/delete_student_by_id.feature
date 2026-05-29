@Api
Feature: Delete student by id

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |

  @ComponentTest
  Scenario: Aura admin successfully deletes a student by id
    When user deletes the noted student by id
    Then the delete student response should be true
    And querying the deleted student by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Deleting non-existent student returns an error
    When user tries to delete a student with a random id
    Then the request should fail with an error

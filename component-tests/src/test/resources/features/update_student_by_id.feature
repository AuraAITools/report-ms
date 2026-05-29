@Api
Feature: Update student by id

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |

  @ComponentTest
  Scenario: Aura admin updates student name only
    When user updates the noted student with name "Alice Updated"
    Then the student response should contain name "Alice Updated" and email "alice.pupil@example.com"

  @ComponentTest
  Scenario: Updating non-existent student returns an error
    When user tries to update a student with a random id and name "Ghost Pupil"
    Then the request should fail with an error

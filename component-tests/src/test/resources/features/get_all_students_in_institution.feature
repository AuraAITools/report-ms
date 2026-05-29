@Api
Feature: Get all students in institution

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant

  @ComponentTest
  Scenario: Aura admin retrieves all students in an institution with two students
    And aura-admin creates an account with parent and student with details:
      | parentEmail            | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent-one@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    And aura-admin creates an account with parent and student with details:
      | parentEmail            | parentFirstName | parentLastName | parentContact | studentName | studentEmail          | studentDateOfBirth |
      | parent-two@example.com | Parent          | Two            | 91234568      | Bob Pupil   | bob.pupil@example.com | 2013-09-20         |
    When user queries all students in the institution
    Then the students response should contain 2 students

  @ComponentTest
  Scenario: Aura admin queries an institution with no students
    When user queries all students in the institution
    Then the students response should be empty

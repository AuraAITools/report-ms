@Api
Feature: Student management
  As an aura admin
  I want to query, update, and delete students in an institution
  So that I can keep the institution's student records accurate

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant

  @ComponentTest
  Scenario: Aura admin successfully retrieves student by id
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user queries the noted student by id
    Then the student response should contain name "Alice Pupil" and email "alice.pupil@example.com"
    And the student response should contain a linked level
    And the student response should contain a linked school

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent student
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user queries student by a random id
    Then the request should fail with error code "RESOURCE_NOT_FOUND"

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

  @ComponentTest
  Scenario: Aura admin updates student name only
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user updates the noted student with name "Alice Updated"
    Then the student response should contain name "Alice Updated" and email "alice.pupil@example.com"

  @ComponentTest
  Scenario: Aura admin fails to update a non-existent student
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user tries to update a student with a random id and name "Ghost Pupil"
    Then the request should fail with an error

  @ComponentTest
  Scenario: Aura admin successfully deletes a student by id
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user deletes the noted student by id
    Then the delete student response should be true
    And querying the deleted student by id should fail with error code "RESOURCE_NOT_FOUND"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent student
    And aura-admin creates an account with parent and student with details:
      | parentEmail        | parentFirstName | parentLastName | parentContact | studentName | studentEmail            | studentDateOfBirth |
      | parent@example.com | Parent          | One            | 91234567      | Alice Pupil | alice.pupil@example.com | 2012-05-15         |
    When user tries to delete a student with a random id
    Then the request should fail with an error

@Api
Feature: Lesson Management

  As an institution administrator
  I want to create, retrieve, and delete lessons within courses and outlets
  So that the institution's teaching schedule reflects the lessons actually being run

  @ComponentTest
  Scenario: Aura admin successfully creates a lesson under a course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Full Lesson Outlet"
    And a course exists with name "Full Lesson Course"
    When user creates a lesson with details:
      | name        | description   | startDate            | endDate              |
      | Full Lesson | A full lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest
  Scenario: Aura admin retrieves a lesson with its associations
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a lesson exists with name "Lesson 1"
    When user queries lesson by id
    Then the lesson should be returned with name "Lesson 1"
    And the lesson course should be returned

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    When user queries lesson with non-existent id
    # Authorization is evaluated against the resource id before any lookup, so a non-existent
    # lesson is indistinguishable from a forbidden one (no id enumeration)
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin retrieves all lessons in a course
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a lesson exists with name "Lesson 1"
    And a lesson exists with name "Lesson 2"
    When user queries all lessons in the course
    Then the response should contain 2 lessons
    And the lessons should include names "Lesson 1,Lesson 2"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when course has no lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Empty Course"
    When user queries all lessons in the course
    Then the response should contain 0 lessons

  @ComponentTest
  Scenario: Aura admin retrieves all lessons in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a lesson exists with name "Lesson 1"
    And a lesson exists with name "Lesson 2"
    When user queries all lessons in the outlet
    Then the response should contain 2 lessons
    And the lessons should include names "Lesson 1,Lesson 2"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when outlet has no lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Empty Outlet"
    When user queries all lessons in the outlet
    Then the response should contain 0 lessons

  @ComponentTest
  Scenario: Aura admin retrieves empty lessons for an educator with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries lessons by a random educator id
    Then the response should contain 0 lessons

  @ComponentTest
  Scenario: Aura admin retrieves empty lessons for a student with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries lessons by a random student id
    Then the response should contain 0 lessons

  # Authorization is evaluated against the resource id before any lookup, and deletes remove the
  # resource's SpiceDB relationships — so deleted and never-existing resources are both FORBIDDEN
  # (no id enumeration).

  @ComponentTest
  Scenario: Aura admin deletes an existing lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"
    And a course exists with name "Algebra 101"
    And a lesson exists with name "Intro Lesson"
    When user deletes the lesson by id
    Then the lesson should be deleted successfully
    And querying the lesson by id should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a lesson with a random id
    Then the request should fail with error code "FORBIDDEN"

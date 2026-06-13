@Api
Feature: Course management
  As an aura admin
  I want to create, query, and delete courses in an institution's outlets
  So that I can manage the courses offered to educators and students

  @ComponentTest
  Scenario: Aura admin successfully creates a course in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Yishun Outlet"
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Test Course | 20      | WEEKLY          | 200.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-12-31T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest
  Scenario: Aura admin creates a course with subjects, educators, and students
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a subject exists with name "Mathematics"
    And the user switches to the institution tenant
    And user creates an account with details:
      | email                | firstName | lastName | contact  |
      | educator@example.com | Educator  | Admin    | 12345678 |
    And an educator exists with name "John Teacher" and email "john.teacher@example.com"
    And user creates an account with details:
      | email              | firstName | lastName | contact  |
      | parent@example.com | Parent    | User     | 87654321 |
    And user creates a parent in the account with relationship "PARENT"
    And a student exists with name "Jane Student" and email "jane.student@example.com"
    When user creates a course with associations and details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 25      | WEEKLY          | 300.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-12-31T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest
  Scenario: Aura admin retrieves a course with its associations
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    When user queries course by id
    Then the course should be returned with id and name "Math 101"
    And the course nested associations should be present

  @ComponentTest
  Scenario: Aura admin fails to retrieve a non-existent course
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    When user queries course with non-existent id
    # Authorization is evaluated against the resource id before any lookup, so a non-existent
    # course is indistinguishable from a forbidden one (no id enumeration)
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin retrieves all courses in an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Outlet"
    And a course exists with name "Math 101"
    And a course exists with name "Science 101"
    When user queries all courses in the outlet
    Then the response should contain 2 courses
    And the courses should include names "Math 101,Science 101"

  @ComponentTest
  Scenario: Aura admin retrieves an empty list when the outlet has no courses
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Empty Outlet"
    When user queries all courses in the outlet
    Then the response should contain 0 courses

  @ComponentTest
  Scenario: Aura admin retrieves empty courses for an educator with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries courses by a random educator id
    Then the response should contain 0 courses

  @ComponentTest
  Scenario: Aura admin retrieves empty courses for a student with no enrolments
    Given user has "aura-admin" role
    And an institution is onboarded
    When user queries courses by a random student id
    Then the response should contain 0 courses

  # Authorization is evaluated against the resource id before any lookup, and deletes remove the
  # resource's SpiceDB relationships — so deleted and never-existing resources are both FORBIDDEN
  # (no id enumeration).

  @ComponentTest
  Scenario: Aura admin deletes a course and cascades child lessons
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Main Branch"
    And a course exists with name "Algebra 101"
    And a lesson exists with name "Intro Lesson"
    When user deletes the course by id
    Then the course should be deleted successfully
    And querying the course by id should fail with error code "FORBIDDEN"
    And querying the lesson by id should fail with error code "FORBIDDEN"

  @ComponentTest
  Scenario: Aura admin fails to delete a non-existent course
    Given user has "aura-admin" role
    And an institution is onboarded
    When user tries to delete a course with a random id
    Then the request should fail with error code "FORBIDDEN"

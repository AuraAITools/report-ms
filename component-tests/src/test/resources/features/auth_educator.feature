@Api
Feature: Educator authorization

  An educator assigned to a course at creation receives the "modifier" role on
  that course's user group. They can view and work within their assigned
  course, but hold no roles on the outlet or institution, so they cannot
  create outlets or other courses.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And an account exists in the institution for "educator@test.com" with details:
      | firstName | lastName | contact  |
      | Edu       | Cator    | 91234567 |
    And an educator exists with name "Edu Cator" and email "educator@test.com"
    And user creates a course with associations and details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    And the user "educator@test.com" completes account setup with the test password
    And user switches to "educator@test.com" identity

  @ComponentTest @AuthEducator
  Scenario: Educator views their assigned course
    When user queries course by id
    Then the course should be returned with id and name "Math 101"

  @ComponentTest @AuthEducator
  Scenario: Educator creates a lesson in their assigned course
    When user creates a lesson with details:
      | name           | description      | startDate            | endDate              |
      | Algebra Lesson | An algebra class | 2026-03-08T09:00:00Z | 2026-03-08T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create an outlet in the institution
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthEducator
  Scenario: Educator cannot create another course in the outlet
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Physics 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the request should fail with error code "FORBIDDEN"

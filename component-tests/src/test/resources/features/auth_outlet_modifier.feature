@Api
Feature: Outlet modifier authorization

  A user holding the "modifier" role on their outlet's user group can create
  and modify resources under that outlet (courses, lessons), but cannot create
  resources scoped to the institution.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a course exists with name "Seed Course"
    And user "modifier@test.com" has "modifier" role on "outlet_user_group" "{outlet_id}"
    And user switches to "modifier@test.com" identity

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier creates a course under their outlet
    When user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier creates a lesson under a course in their outlet
    When user creates a lesson with details:
      | name        | description   | startDate            | endDate              |
      | Test Lesson | A test lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier cannot create an outlet in the institution
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with error code "FORBIDDEN"

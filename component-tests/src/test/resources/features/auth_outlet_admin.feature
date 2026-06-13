@Api
Feature: Outlet admin authorization

  A user holding the "admin" role on their outlet's user group can manage
  resources under that outlet (courses, lessons), but cannot create resources
  scoped to the institution or the system.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a course exists with name "Seed Course"
    And user "outletadmin@test.com" has "admin" role on "outlet_user_group" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin creates a course under their outlet
    When user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin creates a lesson under a course in their outlet
    When user creates a lesson with details:
      | name        | description   | startDate            | endDate              |
      | Test Lesson | A test lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot create an outlet in the institution
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot onboard a new institution
    When user tries to create an institution with details:
      | name       | email          | contactNumber | address      | uen       |
      | Other Inst | other@test.com | 91234567      | 456 Other St | UEN123456 |
    Then the request should fail with error code "FORBIDDEN"

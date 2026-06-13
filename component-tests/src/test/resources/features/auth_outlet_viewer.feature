@Api
Feature: Outlet viewer authorization

  A user holding the "viewer" role on their outlet's user group can only read
  resources under that outlet. They cannot modify outlet resources, and outlet
  roles grant nothing on the parent institution.

  Background:
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a course exists with name "Seed Course"
    And user "outletviewer@test.com" has "viewer" role on "outlet_user_group" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot view the institution
    When user queries their institution
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create a course under their outlet
    When user tries to create a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the request should fail with error code "FORBIDDEN"

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create a lesson under a course in their outlet
    When user tries to create a lesson with details:
      | name                | description            | startDate            | endDate              |
      | Unauthorized Lesson | An unauthorized lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the request should fail with error code "FORBIDDEN"

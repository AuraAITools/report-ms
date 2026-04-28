@Api
Feature: Outlet viewer authorization

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot view institution directly
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user queries their institution
    Then the request should fail with an error

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user tries to create a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the request should fail with an error

  @ComponentTest @AuthOutletViewer
  Scenario: Outlet viewer cannot create standalone lesson
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletviewer@test.com" has "viewer" role on "outlet" "{outlet_id}"
    And user switches to "outletviewer@test.com" identity
    When user tries to create a standalone lesson with name "Unauthorized Lesson"
    Then the request should fail with an error

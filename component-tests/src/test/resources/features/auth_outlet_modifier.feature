@Api
Feature: Outlet modifier authorization

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier can create course under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user creates a course with details:
      | name        | maxSize | lessonFrequency | price | priceFrequency | startDate              | endDate                |
      | Math 101    | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z   | 2026-06-01T09:00:00Z   |
    Then the course should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier can create standalone lesson under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user creates a standalone lesson with details:
      | name          | description    | startDate              | endDate                |
      | Test Lesson   | A test lesson  | 2026-03-01T09:00:00Z   | 2026-03-01T11:00:00Z   |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletModifier
  Scenario: Outlet modifier cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "modifier@test.com" has "modifier" role on "outlet" "{outlet_id}"
    And user switches to "modifier@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

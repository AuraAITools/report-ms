@PreExistingFailure
@Api
Feature: Outlet admin authorization

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin can create course under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin can create standalone lesson under their outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user creates a standalone lesson with details:
      | name        | description   | startDate            | endDate              |
      | Test Lesson | A test lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot create institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user tries to onboard an institution with details:
      | name       | email          | contactNumber | address      | uen       |
      | Other Inst | other@test.com | 91234567      | 456 Other St | UEN123456 |
    Then the request should fail with an error

  @ComponentTest @AuthOutletAdmin
  Scenario: Outlet admin cannot create outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And user "outletadmin@test.com" has "admin" role on "outlet" "{outlet_id}"
    And user switches to "outletadmin@test.com" identity
    When user tries to create an outlet with name "Unauthorized Outlet"
    Then the request should fail with an error

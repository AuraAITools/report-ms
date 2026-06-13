@Api
Feature: Platform admin authorization

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create outlet under onboarded institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user creates an outlet with details:
      | name        | address     | postalCode | contactNumber | description   | email           |
      | Test Outlet | 123 Test St | 123456     | 91234567      | A test outlet | outlet@test.com |
    Then the outlet should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create course under outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    When user creates a course with details:
      | name     | maxSize | lessonFrequency | price | priceFrequency | startDate            | endDate              |
      | Math 101 | 30      | WEEKLY          | 100.0 | MONTHLY        | 2026-03-01T09:00:00Z | 2026-06-01T09:00:00Z |
    Then the course should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can create lesson under a course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    And a course exists with name "Test Course"
    When user creates a lesson with details:
      | name        | description   | startDate            | endDate              |
      | Test Lesson | A test lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can view institution
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user queries their institution
    Then the institution should be returned with correct details

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can query outlets by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Test Outlet"
    When user queries outlets by page
    Then at least one outlet should be returned

  @ComponentTest @AuthPlatformAdmin
  Scenario: Platform admin can query levels by page
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    When user queries levels by page
    Then the levels query should respond successfully

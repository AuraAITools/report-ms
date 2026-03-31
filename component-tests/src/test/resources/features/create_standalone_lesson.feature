@Api
Feature: Create Standalone Lesson

  @ComponentTest
  Scenario: Successfully create a standalone lesson
    Given user has "aura-admin" role
    And an outlet exists with name "Lesson Outlet"
    When user creates a standalone lesson with details:
      | name        | description         | startDate            | endDate              |
      | Test Lesson | A standalone lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully

  @ComponentTest
  Scenario: Cannot create a standalone lesson with a duplicate name
    Given user has "aura-admin" role
    And an outlet exists with name "Lesson Outlet"
    And a standalone lesson exists with name "Duplicate Lesson"
    When user tries to create a standalone lesson with name "Duplicate Lesson"
    Then the request should fail with an error

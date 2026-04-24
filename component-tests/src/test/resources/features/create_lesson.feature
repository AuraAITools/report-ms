@Api
Feature: Create Lesson

  @ComponentTest
  Scenario: Successfully create a lesson linked to an outlet
    Given user has "aura-admin" role
    And an institution is onboarded
    And an outlet exists with name "Full Lesson Outlet"
    When user creates a lesson with details:
      | name          | description      | startDate             | endDate               |
      | Full Lesson   | A full lesson    | 2026-03-01T09:00:00Z  | 2026-03-01T11:00:00Z  |
    Then the lesson should be created successfully

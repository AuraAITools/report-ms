@Api
Feature: Create Lesson

  @ComponentTest
  Scenario: Successfully create a lesson under a course
    Given user has "aura-admin" role
    And an institution is onboarded
    And the user switches to the institution tenant
    And an outlet exists with name "Full Lesson Outlet"
    And a course exists with name "Full Lesson Course"
    When user creates a lesson with details:
      | name        | description   | startDate            | endDate              |
      | Full Lesson | A full lesson | 2026-03-01T09:00:00Z | 2026-03-01T11:00:00Z |
    Then the lesson should be created successfully
